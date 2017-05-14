package com.diablo.dt.diablo.jolimark.model;

import android.content.Context;
import android.util.Log;

import com.diablo.dt.diablo.client.WSaleClient;
import com.diablo.dt.diablo.entity.BlueToothPrinter;
import com.diablo.dt.diablo.entity.Profile;
import com.diablo.dt.diablo.request.sale.NewSaleRequest;
import com.diablo.dt.diablo.response.sale.SalePrintContentResponse;
import com.diablo.dt.diablo.rest.WSaleInterface;
import com.diablo.dt.diablo.utils.DiabloAlertDialog;
import com.diablo.dt.diablo.utils.DiabloEnum;
import com.diablo.dt.diablo.utils.DiabloError;
import com.jolimark.printerlib.VAR;

import de.greenrobot.event.EventBus;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by buxianhui on 17/5/14.
 */

public class PrinterController {
    private final static String LOG_TAG = "PrinterController:";
    private volatile static PrinterController mInstance;

    private BlockingQueue<PrintQueue> mQueue;

    public static PrinterController instance() {
        if (null == mInstance){
            mInstance = new PrinterController();
        }

        return mInstance;
    }

    private PrinterController() {
        mQueue = new ArrayBlockingQueue<>(100);
    }

    private void addToQueue(PrintQueue queue) {
        try {
            mQueue.put(queue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private PrintQueue getFromQueue() {
        PrintQueue queue = null;
        try {
            queue = mQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return queue;
    }

    private static class PrintQueue {
        private Context mContext;
        private Integer mResTitle;
        private String mRSN;

        private PrintQueue(Context context, Integer resTitle, String rsn) {
            mContext = context;
            mResTitle = resTitle;
            mRSN = rsn;
        }

        private Context getContext() {
            return mContext;
        }

        private Integer getResTitle() {
            return mResTitle;
        }

        private String getRSN() {
            return mRSN;
        }
    }

//    public BlockingQueue getQueue() {
//        return mRSNs;
//    }


    public static class PrintProducer implements Runnable {
        private Context mContext;
        private Integer mResTitle;
        // private PrinterController mQueue;
        private String mRSN;

        public PrintProducer(Context context, Integer resTitle, String rsn) {
            mContext = context;
            mResTitle = resTitle;
            // mQueue = queue;
            mRSN = rsn;
        }

        @Override
        public void run() {
            PrinterController.instance().addToQueue(new PrintQueue(mContext, mResTitle, mRSN));
        }
    }


    public static class PrintConsumer implements Runnable{
        // private PrinterController mQueue;
        private BlueToothPrinter mBlueToothPrinter;
        private Context mContext;

        public PrintConsumer(BlueToothPrinter printer) {
            mBlueToothPrinter = printer;
            EventBus.getDefault().register(this);
        }


        @Override
        @SuppressWarnings("InfiniteLoopStatement")
        public void run() {
            PrintQueue queue = PrinterController.instance().getFromQueue();
            mContext = queue.getContext();

            String rsn = queue.getRSN();
            Log.d(LOG_TAG, queue.getRSN());

            PrinterManager pManager = PrinterManager.getInstance();
            pManager.initRemotePrinter(VAR.TransType.TRANS_BT, mBlueToothPrinter.getMac());

            boolean block = false;
            while (!block) {
                Integer status = pManager.connect();
                if (DiabloEnum.PRINTER_CONNECT_SUCCESS.equals(status)) {
                    block = true;
                    startBlueToothPrint(queue.getResTitle(), rsn);
                }
                else if (DiabloEnum.PRINTER_CONNECT_EMPTY_PARAMS.equals(status)) {
                    block = true;
                }
                else {
                    Log.d(LOG_TAG, "connect to blue printer failed, retry after 2s ...");
                    try {
                        Thread.sleep(1000 * 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void startBlueToothPrint(final Integer resTitle, String rsn) {
            final WSaleInterface face = WSaleClient.getClient().create(WSaleInterface.class);
            Call<SalePrintContentResponse> call = face.getPrintContent(Profile.instance().getToken(), new NewSaleRequest.DiabloRSN(rsn));

            call.enqueue(new Callback<SalePrintContentResponse>() {
                @Override
                public void onResponse(Call<SalePrintContentResponse> call, Response<SalePrintContentResponse> response) {
                    SalePrintContentResponse res = response.body();
                    if (response.code() == DiabloEnum.HTTP_OK) {
                        if (res.getCode().equals(DiabloEnum.SUCCESS)) {
                            if (mBlueToothPrinter.getName().equals(DiabloEnum.PRINTER_JOLIMARK)) {
                                PrinterManager pManager = PrinterManager.getInstance();
                                // pManager.initRemotePrinter(VAR.TransType.TRANS_BT, mBlueToothPrinter.getMac());
                                pManager.sendData(pManager.string2Byte(res.getContent()), mContext);
//                                boolean block = false;
//                                while (!block) {
//                                    if (pManager.connect()) {
//                                        block = true;
//
//                                    } else {
//                                        Log.d(LOG_TAG, "connect to blue printer failed, retry after 2s ...");
//                                        try {
//                                            Thread.sleep(1000 * 2);
//                                        } catch (InterruptedException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }
                            }
                        }
                        else {
                            new DiabloAlertDialog(
                                mContext,
                                mContext.getString(resTitle), DiabloError.getError(res.getCode())).create();
                        }
                    }
                    else {
                        new DiabloAlertDialog(
                            mContext,
                            mContext.getString(resTitle),
                            DiabloError.getError(99)).create();
                    }
                }

                @Override
                public void onFailure(Call<SalePrintContentResponse> call, Throwable t) {
                    new DiabloAlertDialog(
                        mContext,
                        mContext.getString(resTitle),
                        DiabloError.getError(99)).create();
                }
            });
        }


        public void onEventMainThread(Event event) {
            PrinterManager.getInstance().onMessage(mContext, event.msg, null);
        }
    }
}
