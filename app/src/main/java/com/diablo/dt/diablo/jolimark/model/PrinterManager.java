package com.diablo.dt.diablo.jolimark.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.jolimark.event.ConnectEvent;
import com.diablo.dt.diablo.jolimark.event.SendEvent;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.jolimark.printerlib.RemotePrinter;
import com.jolimark.printerlib.UsbPrinter;
import com.jolimark.printerlib.VAR;
import com.jolimark.printerlib.VAR.PrinterType;
import com.jolimark.printerlib.VAR.TransType;
import com.jolimark.printerlib.util.ByteArrayUtils;

import de.greenrobot.event.EventBus;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by buxianhui on 17/5/7.
 */

public class PrinterManager {
    private static PrinterManager printerManager = null;// 单例
    private RemotePrinter myPrinter = null;
    private UsbPrinter usbPrinter = null;
    private VAR.TransType transType = null;// 传输方式（蓝牙，wifi）
    private VAR.PrinterType printerType = null;// 打印机类型
    private String devAddress = null;// 目标地址（wifi格式<IP：port>；蓝牙格式<Mac地址>）
    private boolean isSendingData = false;// 是否正在发送数据
    private boolean isConnecting = false;// 是否正在连接打印机

    private boolean openTwoWayFlag = false; //防丢单开启标识

    private boolean startFlag = false;
    private boolean sendFlag = false;
    private boolean finishFlag = false;

    // private Context mContext;
    private byte[] sendData;

    private SendThread sendThread; //发送主线程
    private Handler mHandler;

    private void initHandler(Context context) {
        if (null == mHandler){
            mHandler = new PrintHandler(context, this);
        }
    }

    private SendThread createSendThread(Context context) {
        return new SendThread(context);
    }

    private class SendThread extends Thread {
        private final Object mPauseLock;
        private Context mContext;

        private SendThread(Context context) {
            mPauseLock = new Object();
            mContext = context;
        }

        private void onResume() {
            synchronized (mPauseLock) {
                mPauseLock.notifyAll();
            }
        }

        private void pauseThread() {
            synchronized (mPauseLock) {
                try {
                    mPauseLock.wait();
                } catch (InterruptedException e) {
                    Log.v("thread", "wait fails");
                }
            }
        }

        @Override
        public void run() {
            //0.先判断连接
            EventBus.getDefault().post(new Event(ErrorOrMsg.PRINTING));
            if (transType != null && transType.equals(VAR.TransType.TRANS_USB)) {
                if (!connectUsbPrinter(mContext)) {
                    return;
                }
            } else {
                if (!DiabloEnum.PRINTER_CONNECT_SUCCESS.equals(connect())) {
                    return;
                }
            }
            isSendingData = true;
            int sendDataLength;// 发送数据长度，用来校验数据完整
            int calcDataLength = 0;// 统计发送的数据长度，用来校验数据完整

            try {
                //双向连接,防丢单使用
                if (openTwoWayFlag) {
                    //1.调用开始任务函数,代表一个打印任务的开始
                    if (transType.equals(VAR.TransType.TRANS_USB))
                        startFlag = usbPrinter.startSendData();
                    else
                        startFlag = myPrinter.startSendData();
                    if (!startFlag) {
                        //通过Handler处理第一阶段的错误
                        mHandler.sendEmptyMessage(0x001);
                        //阻塞该线程，等第一阶段任务处理完成之后再执行下面的任务
                        pauseThread();
                    }
                    //如果用户选择"取消",则直接跳出
                    if (!startFlag) {
                        isSendingData = false;
                        return;
                    }
                    //2.发送真正的数据，当数据量很大的时候，可以拆分成多次调用,
                    if (transType.equals(VAR.TransType.TRANS_USB)) {
                        sendDataLength = usbPrinter.sendData(sendData);
                    }
                    else {
                        sendDataLength = myPrinter.sendData(sendData);
                    }

                    sendFlag = sendDataLength == sendData.length;
//                    sendFlag = false;
//                    if (sendDataLength == sendData.length) {
//                        sendFlag = true;
//                    }

                    if (!sendFlag) {// 校验数据完整性
                        //通过Handler处理第二阶段的错误
                        mHandler.sendEmptyMessage(0x002);
                        //阻塞该线程，等第二阶段任务处理完成之后再执行下面的任务
                        pauseThread();
                    }

                    //如果用户选择"取消",则直接跳出
                    if (!sendFlag) {
                        isSendingData = false;
                        return;
                    }
                    //3.调用结束任务函数
                    if (transType.equals(VAR.TransType.TRANS_USB))
                        finishFlag = usbPrinter.finishSendData();
                    else
                        finishFlag = myPrinter.finishSendData();
                    if (!finishFlag) {
                        //通过Handler处理第三阶段的错误
                        mHandler.sendEmptyMessage(0x003);
                        //阻塞该线程，等第三阶段任务处理完成之后再执行下面的任务
                        pauseThread();
                    }
                    //如果用户选择"取消",则直接跳出
                    if (!finishFlag) {
                        isSendingData = false;
                        return;
                    }
                    //发送成功
                    EventBus.getDefault().post(new Event(ErrorOrMsg.SEND_SUCCESS));

                } else {
                    //非防丢单程序:
                    List<byte[]> dataList = ByteArrayUtils.fen(sendData, 1024); // 分包1K,最大不超过2K
                    if (transType.equals(VAR.TransType.TRANS_USB)){
                        //USB发送之前查一下状态
                        byte[] status  = getUsbPrinterStatus(1000,mContext);
                        if (status == null){
                            EventBus.getDefault().post(new SendEvent(ErrorOrMsg.SEND_FAILED));
                            Log.i("发送结果：", "未读取到状态数据");
                            return ;
                        }
                        //这里没有对具体那种类型错误进行判断
                        if ((status[0] & 0x08) == 0){
                            EventBus.getDefault().post(new SendEvent(ErrorOrMsg.SEND_FAILED));
                            Log.i("发送结果：", "打印机出错:"+status[0]);
                            return ;
                        }
                    }
                    for (int i = 0; i < dataList.size(); i++) {
                        // 第三步：使用sendData()方法发送数据
                        if (transType.equals(VAR.TransType.TRANS_USB)){
                            sendDataLength = usbPrinter.sendData(dataList.get(i));
                            if (sendDataLength < 0){
                                EventBus.getDefault().post(new SendEvent(ErrorOrMsg.SEND_FAILED));
                                Log.i("发送结果：", "失败:"+sendDataLength);
                                break;
                            }
                        }
                        else {
                            sendDataLength = myPrinter.sendData(dataList.get(i));
                        }
                        calcDataLength += sendDataLength;
                        try {
                            Thread.sleep(1200);// 睡眠1200ms，自己根据情况设置
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (calcDataLength == sendData.length) {
                        EventBus.getDefault().post(new Event(ErrorOrMsg.SEND_SUCCESS));
                    } else {
                        EventBus.getDefault().post(new Event(ErrorOrMsg.SEND_FAILED));
                    }
                }
            } catch (Exception e) {
                Log.d("PrinterManager-sendData", "捕获到异常");
                e.printStackTrace();
                EventBus.getDefault().post(new Event(ErrorOrMsg.SEND_FAILED));
            }
            isSendingData = false;
        }

    }



    private static class PrintHandler extends Handler {
        private Context mContext;
        WeakReference<PrinterManager> mPrintManager;

        private PrintHandler(Context context, PrinterManager manager) {
            mContext = context;
            mPrintManager = new WeakReference<>(manager);
        }

        @Override
        public void handleMessage(Message msg) {
            final PrinterManager  p = (mPrintManager.get());
            // final boolean startFlag = p.startFlag;
            final boolean sendFlag = p.sendFlag;

            final TransType transType = p.transType;
            final UsbPrinter usbPrinter = p.usbPrinter;
            final RemotePrinter myPrinter = p.myPrinter;

            switch (msg.what) {
                case 0x001:
                    //处理第一阶段的错误
                    if (!p.startFlag) {
                        EventBus.getDefault().post(new Event(ErrorOrMsg.SEND_FAILED));
                        //根据错误信息弹出对话框，如果取消任务失败的话，会得到"有未完成的打印任务"的错误信息
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        if (transType.equals(VAR.TransType.TRANS_USB)) {
                            builder.setTitle(p.getErrorMessage(p.usbPrinter.getLastErrorCode(), mContext)
                                + "(" + usbPrinter.getLastErrorCode() + ")");
                        }
                        else {
                            builder.setTitle(p.getErrorMessage(p.myPrinter.getLastErrorCode(), mContext)
                                + "(" + myPrinter.getLastErrorCode() + ")");
                        }

                        builder.setMessage(mContext.getString(R.string.continue_send_data));
                        //0x705 代表有未完成的任务
                        if (transType.equals(VAR.TransType.TRANS_USB)) {
                            if (usbPrinter.getLastErrorCode() == 0x705) {
                                builder.setMessage(mContext.getString(R.string.force_print));
                            }
                        } else {
                            if (myPrinter.getLastErrorCode() == 0x705) {
                                builder.setMessage(mContext.getString(R.string.force_print));
                            }
                        }
                        builder.setCancelable(false);
                        builder.setPositiveButton(mContext.getString(R.string.continue_confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EventBus.getDefault().post(new Event(ErrorOrMsg.PRINTING));
                                new Thread() {
                                    @Override
                                    public void run() {
                                        //如果用户选择"确定"，继续发送
                                        if (transType.equals(VAR.TransType.TRANS_USB))
                                            p.startFlag = usbPrinter.continueSendData();
                                        else
                                            p.startFlag = myPrinter.continueSendData();
                                        //续发失败，继续处理错误
                                        if (!p.startFlag) {
                                            p.mHandler.sendEmptyMessage(0x001);
                                        } else {
                                            //续发成功，唤醒线程，继续往下执行（执行第二阶段任务）
                                            p.sendThread.onResume();
                                        }
                                    }
                                }.start();
                            }
                        });
                        builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //如果用户选择"取消",唤醒线程结束跳出
                                p.sendThread.onResume();
                                //通讯异常，关闭连接
                                if (transType.equals(VAR.TransType.TRANS_USB))
                                    usbPrinter.close();
                                else
                                    myPrinter.close();
                            }
                        });
                        builder.create().show();

                    }
                    break;
                case 0x002:
                    if (!sendFlag) {
                        EventBus.getDefault().post(new Event(ErrorOrMsg.SEND_FAILED));
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        if (transType.equals(VAR.TransType.TRANS_USB)) {
                            builder.setTitle(p.getErrorMessage(usbPrinter.getLastErrorCode(), mContext)
                                + "(" + usbPrinter.getLastErrorCode() + ")");
                        }
                        else {
                            builder.setTitle(p.getErrorMessage(myPrinter.getLastErrorCode(), mContext)
                                + "(" + myPrinter.getLastErrorCode() + ")");
                        }

                        builder.setMessage(mContext.getString(R.string.continue_send_data));
                        builder.setCancelable(false);
                        builder.setPositiveButton(mContext.getString(R.string.continue_confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EventBus.getDefault().post(new Event(ErrorOrMsg.PRINTING));
                                new Thread() {
                                    @Override
                                    public void run() {
                                        //如果用户选择"确定"，继续发送
                                        if (transType.equals(VAR.TransType.TRANS_USB))
                                            p.sendFlag = usbPrinter.continueSendData();
                                        else
                                            p.sendFlag = myPrinter.continueSendData();
                                        //续发失败，继续处理错误
                                        if (!p.sendFlag) {
                                            p.mHandler.sendEmptyMessage(0x002);
                                        } else {
                                            //续发成功，唤醒线程，继续往下执行（执行第三阶段任务）
                                            p.sendThread.onResume();
                                        }
                                    }
                                }.start();
                            }
                        });
                        builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        //如果用户选择在发送数据时候取消，这是必须结束任务
                                        //1.先清除打印机缓存
                                        //2.调用结束任务函数
                                        if (transType.equals(VAR.TransType.TRANS_USB)) {
                                            boolean cleanFlag = usbPrinter.cleanPrinterCache();
                                            if (cleanFlag) {
                                                boolean finishFlag = usbPrinter.finishSendData();
                                                if (finishFlag) {
                                                    //取消任务成功
                                                    EventBus.getDefault().post(new Event(ErrorOrMsg.TASK_CANCEL_SUCCESS));
                                                } else {
                                                    //如果错误是0x801(打印中途出错)，也属于取消任务成功
                                                    if (usbPrinter.getLastErrorCode() == 0x801) {
                                                        EventBus.getDefault().post(new Event(ErrorOrMsg.TASK_CANCEL_SUCCESS));
                                                    } else {
                                                        //取消任务失败
                                                        EventBus.getDefault().post(new Event(ErrorOrMsg.TASK_CANCEL_FAILED));
                                                    }
                                                }
                                            } else {
                                                //取消任务失败
                                                EventBus.getDefault().post(new Event(ErrorOrMsg.TASK_CANCEL_FAILED));
                                            }
                                            //唤醒线程结束跳出
                                            p.sendThread.onResume();
                                            usbPrinter.close();
                                        } else {
                                            boolean cleanFlag = myPrinter.cleanPrinterCache();
                                            if (cleanFlag) {
                                                boolean finishFlag = myPrinter.finishSendData();
                                                if (finishFlag) {
                                                    //取消任务成功
                                                    EventBus.getDefault().post(new Event(ErrorOrMsg.TASK_CANCEL_SUCCESS));
                                                } else {
                                                    //如果错误是0x801(打印中途出错)，也属于取消任务成功
                                                    if (myPrinter.getLastErrorCode() == 0x801) {
                                                        EventBus.getDefault().post(new Event(ErrorOrMsg.TASK_CANCEL_SUCCESS));
                                                    } else {
                                                        //取消任务失败
                                                        EventBus.getDefault().post(new Event(ErrorOrMsg.TASK_CANCEL_FAILED));
                                                    }
                                                }
                                            } else {
                                                //取消任务失败
                                                EventBus.getDefault().post(new Event(ErrorOrMsg.TASK_CANCEL_FAILED));
                                            }
                                            //唤醒线程结束跳出
                                            p.sendThread.onResume();
                                            myPrinter.close();
                                        }
                                    }
                                }.start();
                            }
                        });
                        builder.create().show();

                    }
                    break;
                case 0x003:
                    if (!p.finishFlag) {
                        EventBus.getDefault().post(new Event(ErrorOrMsg.SEND_FAILED));
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        if (transType.equals(VAR.TransType.TRANS_USB)) {
                            builder.setTitle(p.getErrorMessage(usbPrinter.getLastErrorCode(), mContext)
                                + "(" + usbPrinter.getLastErrorCode() + ")");
                        }
                        else {
                            builder.setTitle(p.getErrorMessage(myPrinter.getLastErrorCode(), mContext)
                                + "(" + myPrinter.getLastErrorCode() + ")");
                        }

                        builder.setMessage(mContext.getString(R.string.continue_send_data));
                        //0x801 代表中途打印出错，只要打印中途（不是发送中途）发生纸尽、上盖打开等就会出现这个错误
                        if (transType.equals(VAR.TransType.TRANS_USB)) {
                            if (usbPrinter.getLastErrorCode() == 0x801) {
                                builder.setMessage(mContext.getString(R.string.reprint));
                            }
                        } else {
                            if (myPrinter.getLastErrorCode() == 0x801) {
                                builder.setMessage(mContext.getString(R.string.reprint));
                            }
                        }
                        builder.setCancelable(false);
                        builder.setPositiveButton(mContext.getString(R.string.continue_confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EventBus.getDefault().post(new Event(ErrorOrMsg.PRINTING));
                                new Thread() {
                                    @Override
                                    public void run() {
                                        //如果是中途出错，就是重新打印

                                        if (transType.equals(TransType.TRANS_USB)) {
                                            if (usbPrinter.getLastErrorCode() == 0x801) {
                                                usbPrinter.close(); //关闭之前的连接
                                                p.sendThread = p.createSendThread(mContext);
                                                p.sendThread.start();
                                            } else {
                                                //其他情况，继续发送
                                                p.finishFlag = usbPrinter.continueSendData();
                                                //续发失败，继续处理错误
                                                if (!p.finishFlag) {
                                                    p.mHandler.sendEmptyMessage(0x003);
                                                } else {
                                                    //续发成功，唤醒线程继续执行
                                                    p.sendThread.onResume();
                                                }
                                            }
                                        } else {
                                            if (myPrinter.getLastErrorCode() == 0x801) {
                                                myPrinter.close(); //关闭之前的连接
                                                p.sendThread = p.createSendThread(mContext);
                                                p.sendThread.start();
                                            } else {
                                                //其他情况，继续发送
                                                p.finishFlag = myPrinter.continueSendData();
                                                //续发失败，继续处理错误
                                                if (!p.finishFlag) {
                                                    p.mHandler.sendEmptyMessage(0x003);
                                                } else {
                                                    //续发成功，唤醒线程继续执行
                                                    p.sendThread.onResume();
                                                }
                                            }
                                        }
                                    }
                                }.start();
                            }
                        });
                        builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //如果用户选择"取消",唤醒线程结束跳出
                                p.sendThread.onResume();
                                if (transType.equals(VAR.TransType.TRANS_USB))
                                    usbPrinter.close();
                                else
                                    myPrinter.close();
                            }
                        });
                        builder.create().show();
                    }
                    break;
            }
        }
    }

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0x001:
//                    //处理第一阶段的错误
//                    if (!startFlag) {
//                        EventBus.getDefault().post(new Event(ErrorOrMsg.SEND_FAILED));
//                        //根据错误信息弹出对话框，如果取消任务失败的话，会得到"有未完成的打印任务"的错误信息
//                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                        if (transType.equals(VAR.TransType.TRANS_USB))
//                            builder.setTitle(getErrorMessage(usbPrinter.getLastErrorCode())+ "(" + usbPrinter.getLastErrorCode() + ")");
//                        else
//                            builder.setTitle(getErrorMessage(myPrinter.getLastErrorCode())+ "(" + myPrinter.getLastErrorCode() + ")");
//                        builder.setMessage(mContext.getString(R.string.continue_send_data));
//                        //0x705 代表有未完成的任务
//                        if (transType.equals(VAR.TransType.TRANS_USB)) {
//                            if (usbPrinter.getLastErrorCode() == 0x705) {
//                                builder.setMessage(mContext.getString(R.string.force_print));
//                            }
//                        } else {
//                            if (myPrinter.getLastErrorCode() == 0x705) {
//                                builder.setMessage(mContext.getString(R.string.force_print));
//                            }
//                        }
//                        builder.setCancelable(false);
//                        builder.setPositiveButton(mContext.getString(R.string.continue_confirm), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                EventBus.getDefault().post(new Event(ErrorOrMsg.PRINTING));
//                                new Thread() {
//                                    @Override
//                                    public void run() {
//                                        //如果用户选择"确定"，继续发送
//                                        if (transType.equals(VAR.TransType.TRANS_USB))
//                                            startFlag = usbPrinter.continueSendData();
//                                        else
//                                            startFlag = myPrinter.continueSendData();
//                                        //续发失败，继续处理错误
//                                        if (!startFlag) {
//                                            mHandler.sendEmptyMessage(0x001);
//                                        } else {
//                                            //续发成功，唤醒线程，继续往下执行（执行第二阶段任务）
//                                            sendThread.onResume();
//                                        }
//                                    }
//                                }.start();
//                            }
//                        });
//                        builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //如果用户选择"取消",唤醒线程结束跳出
//                                sendThread.onResume();
//                                //通讯异常，关闭连接
//                                if (transType.equals(VAR.TransType.TRANS_USB))
//                                    usbPrinter.close();
//                                else
//                                    myPrinter.close();
//                            }
//                        });
//                        builder.create().show();
//
//                    }
//                    break;
//                case 0x002:
//                    if (!sendFlag) {
//                        EventBus.getDefault().post(new Event(ErrorOrMsg.SEND_FAILED));
//                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                        if (transType.equals(VAR.TransType.TRANS_USB))
//                            builder.setTitle(getErrorMessage(usbPrinter.getLastErrorCode())+ "(" + usbPrinter.getLastErrorCode() + ")");
//                        else
//                            builder.setTitle(getErrorMessage(myPrinter.getLastErrorCode()) + "(" + myPrinter.getLastErrorCode() + ")");
//                        builder.setMessage(mContext.getString(R.string.continue_send_data));
//                        builder.setCancelable(false);
//                        builder.setPositiveButton(mContext.getString(R.string.continue_confirm), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                EventBus.getDefault().post(new Event(ErrorOrMsg.PRINTING));
//                                new Thread() {
//                                    @Override
//                                    public void run() {
//                                        //如果用户选择"确定"，继续发送
//                                        if (transType.equals(VAR.TransType.TRANS_USB))
//                                            sendFlag = usbPrinter.continueSendData();
//                                        else
//                                            sendFlag = myPrinter.continueSendData();
//                                        //续发失败，继续处理错误
//                                        if (!sendFlag) {
//                                            mHandler.sendEmptyMessage(0x002);
//                                        } else {
//                                            //续发成功，唤醒线程，继续往下执行（执行第三阶段任务）
//                                            sendThread.onResume();
//                                        }
//                                    }
//                                }.start();
//                            }
//                        });
//                        builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                new Thread() {
//                                    @Override
//                                    public void run() {
//                                        //如果用户选择在发送数据时候取消，这是必须结束任务
//                                        //1.先清除打印机缓存
//                                        //2.调用结束任务函数
//                                        if (transType.equals(VAR.TransType.TRANS_USB)) {
//                                            boolean cleanFlag = usbPrinter.cleanPrinterCache();
//                                            if (cleanFlag) {
//                                                boolean finishFlag = usbPrinter.finishSendData();
//                                                if (finishFlag) {
//                                                    //取消任务成功
//                                                    EventBus.getDefault().post(new Event(ErrorOrMsg.TASK_CANCEL_SUCCESS));
//                                                } else {
//                                                    //如果错误是0x801(打印中途出错)，也属于取消任务成功
//                                                    if (usbPrinter.getLastErrorCode() == 0x801) {
//                                                        EventBus.getDefault().post(new Event(ErrorOrMsg.TASK_CANCEL_SUCCESS));
//                                                    } else {
//                                                        //取消任务失败
//                                                        EventBus.getDefault().post(new Event(ErrorOrMsg.TASK_CANCEL_FAILED));
//                                                    }
//                                                }
//                                            } else {
//                                                //取消任务失败
//                                                EventBus.getDefault().post(new Event(ErrorOrMsg.TASK_CANCEL_FAILED));
//                                            }
//                                            //唤醒线程结束跳出
//                                            sendThread.onResume();
//                                            usbPrinter.close();
//                                        } else {
//                                            boolean cleanFlag = myPrinter.cleanPrinterCache();
//                                            if (cleanFlag) {
//                                                boolean finishFlag = myPrinter.finishSendData();
//                                                if (finishFlag) {
//                                                    //取消任务成功
//                                                    EventBus.getDefault().post(new Event(ErrorOrMsg.TASK_CANCEL_SUCCESS));
//                                                } else {
//                                                    //如果错误是0x801(打印中途出错)，也属于取消任务成功
//                                                    if (myPrinter.getLastErrorCode() == 0x801) {
//                                                        EventBus.getDefault().post(new Event(ErrorOrMsg.TASK_CANCEL_SUCCESS));
//                                                    } else {
//                                                        //取消任务失败
//                                                        EventBus.getDefault().post(new Event(ErrorOrMsg.TASK_CANCEL_FAILED));
//                                                    }
//                                                }
//                                            } else {
//                                                //取消任务失败
//                                                EventBus.getDefault().post(new Event(ErrorOrMsg.TASK_CANCEL_FAILED));
//                                            }
//                                            //唤醒线程结束跳出
//                                            sendThread.onResume();
//                                            myPrinter.close();
//                                        }
//                                    }
//                                }.start();
//                            }
//                        });
//                        builder.create().show();
//
//                    }
//                    break;
//                case 0x003:
//                    if (!finishFlag) {
//                        EventBus.getDefault().post(new Event(ErrorOrMsg.SEND_FAILED));
//                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                        if (transType.equals(VAR.TransType.TRANS_USB))
//                            builder.setTitle(getErrorMessage(usbPrinter.getLastErrorCode())+ "(" + usbPrinter.getLastErrorCode() + ")");
//                        else
//                            builder.setTitle(getErrorMessage(myPrinter.getLastErrorCode())+ "(" + myPrinter.getLastErrorCode() + ")");
//                        builder.setMessage(mContext.getString(R.string.continue_send_data));
//                        //0x801 代表中途打印出错，只要打印中途（不是发送中途）发生纸尽、上盖打开等就会出现这个错误
//                        if (transType.equals(VAR.TransType.TRANS_USB)) {
//                            if (usbPrinter.getLastErrorCode() == 0x801) {
//                                builder.setMessage(mContext.getString(R.string.reprint));
//                            }
//                        } else {
//                            if (myPrinter.getLastErrorCode() == 0x801) {
//                                builder.setMessage(mContext.getString(R.string.reprint));
//                            }
//                        }
//                        builder.setCancelable(false);
//                        builder.setPositiveButton(mContext.getString(R.string.continue_confirm), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                EventBus.getDefault().post(new Event(ErrorOrMsg.PRINTING));
//                                new Thread() {
//                                    @Override
//                                    public void run() {
//                                        //如果是中途出错，就是重新打印
//
//                                        if (transType.equals(VAR.TransType.TRANS_USB)) {
//                                            if (usbPrinter.getLastErrorCode() == 0x801) {
//                                                usbPrinter.close(); //关闭之前的连接
//                                                sendThread = new SendThread();
//                                                sendThread.start();
//                                            } else {
//                                                //其他情况，继续发送
//                                                finishFlag = usbPrinter.continueSendData();
//                                                //续发失败，继续处理错误
//                                                if (!finishFlag) {
//                                                    mHandler.sendEmptyMessage(0x003);
//                                                } else {
//                                                    //续发成功，唤醒线程继续执行
//                                                    sendThread.onResume();
//                                                }
//                                            }
//                                        } else {
//                                            if (myPrinter.getLastErrorCode() == 0x801) {
//                                                myPrinter.close(); //关闭之前的连接
//                                                sendThread = new SendThread();
//                                                sendThread.start();
//                                            } else {
//                                                //其他情况，继续发送
//                                                finishFlag = myPrinter.continueSendData();
//                                                //续发失败，继续处理错误
//                                                if (!finishFlag) {
//                                                    mHandler.sendEmptyMessage(0x003);
//                                                } else {
//                                                    //续发成功，唤醒线程继续执行
//                                                    sendThread.onResume();
//                                                }
//                                            }
//                                        }
//                                    }
//                                }.start();
//                            }
//                        });
//                        builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //如果用户选择"取消",唤醒线程结束跳出
//                                sendThread.onResume();
//                                if (transType.equals(VAR.TransType.TRANS_USB))
//                                    usbPrinter.close();
//                                else
//                                    myPrinter.close();
//                            }
//                        });
//                        builder.create().show();
//                    }
//                    break;
//            }
//        }
//    };


    private String getErrorMessage(int code, Context context){
        String msg;
        switch (code){
            case 0xFFFE:
                msg = context.getString(R.string.er_unknown);
                break;
            case 0x0001:
            case 0x701:
                msg = context.getString(R.string.er_connect);
                break;
            case 0x0002:
                msg = context.getString(R.string.er_printer_type);
                break;
            case 0x407:
            case 0x4071:
            case 0x408:
            case 0x409:
            case 0x4041:
            case 0x4051:
            case 0x4052:
            case 0x4053:
            case 0x4054:
                msg =  context.getString(R.string.er_send_data);
                break;
            case 0x501:
            case 0x5021:
            case 0x5022:
            case 0x5023:
            case 0x503:
            case 0x5031:
            case 0x504:
            case 0x5041:
            case 0x505:
                msg = context.getString(R.string.er_no_response);
                break;

            case 0x6001:
                msg =  context.getString(R.string.er_no_select);
                break;
            case 0x6002:
                msg =  context.getString(R.string.er_money_box);
                break;
            case 0x6003:
                msg =  context.getString(R.string.er_cover);
                break;
            case 0x6004:
                msg =  context.getString(R.string.er_paper);
                break;
            case 0x6005:
                msg =  context.getString(R.string.er_knife);
                break;
            case 0x6006:
                msg =  context.getString(R.string.er_over_heat);
                break;
            case 0x702:
            case 0x703:
                msg =  context.getString(R.string.er_clean_cache);
                break;
            case 0x704:
            case 0x7041:
                msg =  context.getString(R.string.er_get_status);
                break;
            case 0x705:
                msg =  context.getString(R.string.er_unfinished_task);
                break;
            case 0x801:
                msg =  context.getString(R.string.er_printing);
                break;
            default:
                msg =  context.getString(R.string.er_unknown);
        }
        return msg;
    }

    private PrinterManager() {
//		 openTimeToClose();
    }

    /**
     * 获取管理实例对象
     *
     * @return
     */
    public static PrinterManager getInstance() {
        if (printerManager != null) {
            return printerManager;
        } else {
            printerManager = new PrinterManager();
            return printerManager;
        }
    }

    /**
     * 清空配置信息
     */
    public void resetConfig() {
        if (myPrinter != null && myPrinter.isConnected()) {
            myPrinter.close();
        }
        printerType = null;
        transType = null;
        devAddress = null;
    }

    /**
     * 是否打印机设置好
     *
     * @return
     */
    public boolean isPrinterReady() {
        return myPrinter != null;
    }

    /**
     * 初始化RemotePrinter
     *
     * @param tp
     * @param addr
     */
    public void initRemotePrinter(VAR.TransType tp, String addr) {
        if (tp.equals(transType) && addr.equals(devAddress)) {
            // 相同参数就不再初始化
        } else {
            if (myPrinter != null) { // 如果已经存在myPrinter 对象，先关闭旧的连接
                myPrinter.close();
                myPrinter = null;
            }
            this.transType = tp;
            this.devAddress = addr;
        }
    }

    /**
     * 连接
     */
    public Integer connect() {
        if ((!TextUtils.isEmpty(String.valueOf(transType))) && (!TextUtils.isEmpty(devAddress))) { // printer设置参数不为空
            if (myPrinter == null) {// myPrinter 对象不存在
                // 第一步：创建RemotePrinter对象，传入参数：
                // transType 为传输方式
                // devAddress 为设备地址（wifi格式<IP：port>；蓝牙格式<Mac地址>）
                myPrinter = new RemotePrinter(transType, devAddress);
            }
            if (myPrinter.isConnected()) { // myPrinter 对象已经连接
                // 已经连接不需要再连接
                // Log.d("connect", "-------已连接，不需要连接");
                EventBus.getDefault().post(new Event(ErrorOrMsg.CONNECT_EXIST));
                return DiabloEnum.PRINTER_CONNECT_SUCCESS;
            } else { // myPrinter 对象没连接，先连接
                // 第二步：调用open()方法打开连接
                try {
                    isConnecting = true;
                    boolean ret = myPrinter.open(openTwoWayFlag);
                    isConnecting = false;
                    Log.d("connect", "-------没连接，需要open：" + ret);
                    if (ret) {
                        printerType = myPrinter.getPrinterType();// 获取打印机类型，在open()调用之后
                        EventBus.getDefault().post(new Event(ErrorOrMsg.CONNECT_SUCCESS));
                        return DiabloEnum.PRINTER_CONNECT_SUCCESS;
                    } else {
                        EventBus.getDefault().post(new Event(ErrorOrMsg.CONNECT_FAILED));// 连接失败
                        return DiabloEnum.PRINTER_CONNECT_FAILED;
                    }
                } catch (Exception e) {
                    EventBus.getDefault().post(new Event(ErrorOrMsg.CONNECT_FAILED));// 连接失败
                    e.printStackTrace();
                    return DiabloEnum.PRINTER_CONNECT_FAILED;
                }
            }
        } else {
            EventBus.getDefault().post(new Event(ErrorOrMsg.CONFIG_NULL));// 配置为空
            return DiabloEnum.PRINTER_CONNECT_EMPTY_PARAMS;
        }
    }

    public boolean connectUsbPrinter(Context context) {
        if (!TextUtils.isEmpty(String.valueOf(transType)) && transType.equals(VAR.TransType.TRANS_USB)) {
            if (usbPrinter == null) {
                usbPrinter = new UsbPrinter(context);
            }
            if (usbPrinter.isConnected()) {
                EventBus.getDefault().post(new ConnectEvent(ErrorOrMsg.CONNECT_EXIST));
                return true;
            } else {
                boolean ret = usbPrinter.open(3000, openTwoWayFlag);
                if (ret) {
                    printerType = usbPrinter.getPrinterType();
                    if (printerType == null) {
                        EventBus.getDefault().post(new ConnectEvent(ErrorOrMsg.USB_PRINTER_STYLE_ERROR));
                        Log.d("connect", "-------------------------类型失败");
                        usbPrinter.close();
                        return false;
                    } else {
                        EventBus.getDefault().post(new ConnectEvent(ErrorOrMsg.FIND_USB_PRINTER));
                        Log.d("connect", "-------------------------找到打印机");
                        return true;
                    }
                } else {
                    EventBus.getDefault().post(new ConnectEvent(ErrorOrMsg.CONNECT_FAILED));
                    Log.d("connect", "-------------------------权限获取失败");
                    usbPrinter.close();
                    return false;
                }
            }
        } else {
            EventBus.getDefault().post(new ConnectEvent(ErrorOrMsg.CONFIG_NULL));// 配置为空
            return false;
        }
    }



    /**
     * 获取USB打印机状态（1个字节），具体每一位代表的信息如下：
     * bit 0-2:保留字段
     * bit 3:判断是否出错（1 = 没有错误，0 = 出错）
     * bit 4:判断是否联机（1 = 联机， 0 = 脱机）
     * bit 5:判断是否纸尽（1 = 纸尽, 0 = 正常）
     * bit 6-7:保留字段
     * 想要知道是否出错判断bit3就行，具体哪一种类型错误，这里只能查到两种（bit4和bit5）。
     *
     * @param timeout 超时时间ms
     * @return 接收到状态字节数组（只有一个字节或者出错为null）
     */

    public byte[] getUsbPrinterStatus(int timeout,Context context){
        byte[] status = null;
        if (transType != null && transType.equals(VAR.TransType.TRANS_USB)){
            if (usbPrinter!= null){
                if (!usbPrinter.isConnected()){
                    if (!connectUsbPrinter(context)){
                        return null;
                    }
                }
                status = usbPrinter.getUsbPrinterStatus(timeout);
            }
        }
        return status;
    }


    /**
     * 1.发送数据，一次只能有一个线程发送数据
     * 2.分防丢单程序和非防丢单程序的使用
     * 3.防丢单程序步骤(具体函数说明参看文档)：
     * 	 (1)判断连接
     * 	 (2)调用开始任务函数
     * 	 (3)发送数据
     * 	 (4)调用结束任务函数
     * 注:防丢单程序主要分为三个阶段：开始，发送，结束。当其中一个阶段出现错误的时候.
     * 可以通过getMessage()和getLastErrorCode()分开获得相关的错误信息(纸尽，上盖打开等).
     * 当某阶段发生错误后，可以调用续发函数，来续发当前阶段。比如开始阶段出错，
     * 调用续发函数可以续发开始任务阶段。具体说明参看文档。
     * 4.非防丢单程序步骤：
     * 	 (1)进行分包,即分成一个数组列表，每包最大不超过2K
     * 	 (2)对分好的数据进行发送
     * 	 (3)每包之间进行适当延迟(自己根据打印机情况设置)，因为数据发送太快，有些打印机缓冲区小，导致溢出乱码
     * @param sendData
     *
     */
    public void sendData(final byte[] sendData, final Context context) {
        if (sendData == null) {
            EventBus.getDefault().post(new Event(ErrorOrMsg.DATA_EMPTY));// 数据为空
            return;
        }
        if(isConnecting){
            EventBus.getDefault().post(new Event(ErrorOrMsg.CONNECTING));
            return;
        }
        if(isSendingData){
            EventBus.getDefault().post(new Event(ErrorOrMsg.PRINT_WAIT));
            return;
        }
        // mContext = context;
        getInstance().initHandler(context);
        this.sendData = sendData;
        sendThread = createSendThread(context); //创建一个发送主线程
        sendThread.start();
    }

    public byte[] string2Byte(String content) {
        byte strToByte[] = null;
        strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a17);
        strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a61);
        int contentLen = content.split("\\r\\n").length;
        if (contentLen < 38) {
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a60);
        }


        // strToByte = ByteArrayUtils.twoToOne(strToByte, Command.feed((byte)20));
        // strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a60);
        strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(content));
        strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte("\r\n\r\n\r\n"));
        if (contentLen < 38) {
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a33);
        }
        strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a17);
        // strToByte = ByteArrayUtils.twoToOne(strToByte, Command.feed((byte)20));
        // strToByte = ByteArrayUtils.twoToOne(strToByte, strToByte);
        return strToByte;
    }


    /**
     * 是否连接
     *
     * @return
     */
    public boolean isConnected() {
        if (transType != null && transType.equals(VAR.TransType.TRANS_USB)){
            return usbPrinter!= null && usbPrinter.isConnected();
        }else {
            return myPrinter != null && myPrinter.isConnected();
        }
    }

    /**
     * 关闭连接
     *
     * @return
     */
    public int close() {

        if (transType != null && transType.equals(VAR.TransType.TRANS_USB)){
            if (usbPrinter != null){
                boolean ret = usbPrinter.close();
                if (ret) {
                    // EventBus.getDefault().post(new Event(ErrorOrMsg.CLOSE_SUCCESS));
                    return ErrorOrMsg.CLOSE_SUCCESS;
                } else {
                    // EventBus.getDefault().post(new Event(ErrorOrMsg.CLOSE_FAILED));
                    return ErrorOrMsg.CLOSE_FAILED;
                }
            }else {
                // EventBus.getDefault().post(new Event(ErrorOrMsg.PRINTER_NULL));
                return ErrorOrMsg.PRINTER_NULL;
            }
        } else {
            if (myPrinter != null) {
                // 第四步：调用close()关闭连接
                boolean ret = myPrinter.close();
                if (ret) {
                    // EventBus.getDefault().post(new Event(ErrorOrMsg.CLOSE_SUCCESS));
                    return ErrorOrMsg.CLOSE_SUCCESS;
                } else {
                    // EventBus.getDefault().post(new Event(ErrorOrMsg.CLOSE_FAILED));
                    return ErrorOrMsg.CLOSE_FAILED;
                }
            } else {
                // EventBus.getDefault().post(new Event(ErrorOrMsg.PRINTER_NULL));
                return ErrorOrMsg.PRINTER_NULL;
            }
        }
    }

    /**
     * 获取打印机类型
     *
     * @return
     */
    public PrinterType getPrinterType() {
        return printerType;
    }

    /**
     * 获取传输类型
     *
     * @return
     */
    public TransType getTransType() {
        return transType;
    }

    /**
     * 获取传输地址
     *
     * @return
     */
    public String getAddress() {
        return devAddress;
    }

    /**
     * 定时关闭连接
     */
    public void openTimeToClose() {
        new Thread() {
            public void run() {
                while (true) {
                    if (transType != null) {
                        if (myPrinter != null) {
                            if (myPrinter.isConnected()) {
                                if (!isSendingData) {
                                    myPrinter.close();
                                }
                            }
                        }
                    }
                    try {
                        Thread.sleep(1000 * 20); // 睡眠20秒检测一次连接状态
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    public boolean isOpenTwoWayFlag() {
        return openTwoWayFlag;
    }

    public void setOpenTwoWayFlag(boolean openTwoWayFlag) {
        this.openTwoWayFlag = openTwoWayFlag;
    }

    public interface OnPrintListener {
        void onSuccessToSendData();
    }

    // 接收发送事件信息
    public void onMessage(Context context, int message, @Nullable OnPrintListener listener) {
        DiabloUtils utils = DiabloUtils.instance();
        switch (message) {
            case ErrorOrMsg.SEND_SUCCESS:
                Log.d("SEND_SUCCESS", "-------------------------发送成功");
                if (null != listener) {
                    listener.onSuccessToSendData();
                }

                utils.makeToast(context, context.getResources().getString(R.string.send_success));
                if (printerManager.isConnected()) {
                    new Thread() {
                        public void run() {
                            printerManager.close();//打完关闭连接
                        }
                    }.start();
                    Log.d("发送成功", "---------------- 关闭连接");
                }
                break;
            case ErrorOrMsg.SEND_FAILED:
                utils.makeToast(context, context.getString(R.string.send_failed));
                Log.d("SEND_FAILED", "-------------------------发送失败");
//			if (printerManager.isConnected()) {
//				new Thread() {
//					public void run() {
//						printerManager.close();//打完关闭连接
//					}
//				}.start();
//				Log.d("发送失败", "---------------- 关闭连接");
//			}
                break;
            case ErrorOrMsg.CONFIG_NULL:
                Log.d("tag", "-------------------------配置为空");
                utils.makeToast(context, R.string.no_choose);
                break;
            case ErrorOrMsg.DATA_EMPTY:
                utils.makeToast(context, R.string.data_empty);
                break;
            case ErrorOrMsg.PRINTING:
                break;
            case ErrorOrMsg.CONNECT_FAILED:
                utils.makeToast(context, R.string.connect_failed);// 连接失败
                break;
            case ErrorOrMsg.CONNECTING:
                utils.makeToast(context, R.string.connecting);
                break;
            case ErrorOrMsg.PRINT_WAIT:
                utils.makeToast(context, R.string.printing);// 打印中,请稍后
                break;

            case ErrorOrMsg.TASK_CANCEL_SUCCESS:
                utils.makeToast(context, R.string.task_cancel_success);
                break;
            case ErrorOrMsg.TASK_CANCEL_FAILED:
                utils.makeToast(context, R.string.task_cancel_failed);
                break;
            default:
                break;
        }
    }
}
