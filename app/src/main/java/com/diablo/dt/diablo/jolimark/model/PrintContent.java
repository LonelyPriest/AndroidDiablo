package com.diablo.dt.diablo.jolimark.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.diablo.dt.diablo.R;
import com.diablo.dt.diablo.utils.DiabloUtils;
import com.jolimark.printerlib.RemotePrinter;
import com.jolimark.printerlib.VAR.PrinterType;
import com.jolimark.printerlib.util.ByteArrayUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by buxianhui on 17/5/7.
 */

public class PrintContent {
    private static final DiabloUtils UTILS = DiabloUtils.instance();
    /**
     * 获取打印文件数据
     *
     * @param context
     * @param filename
     * @return
     */
    public static byte[] getPrintData(Context context, String filename) {
        byte[] all = null;
        byte[] buffer;
        // 初始化
        all = ByteArrayUtils.twoToOne(all, Command.a17);
        try {
            DataInputStream dsd = new DataInputStream(context.getAssets().open(filename + ".prn"));
            int length = dsd.available();
            buffer = new byte[length];
            dsd.read(buffer);
            all = ByteArrayUtils.twoToOne(all, buffer);
            dsd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        all = ByteArrayUtils.twoToOne(all, Command.a12);// 回车换行
        return all;
    }

    //获取图片数据
    public static byte[] getPicByteData(Context mContext) {
        PrinterType printerType = PrinterManager.getInstance().getPrinterType();// 打印机类型
        // 从资源文件的读取二维码图片文件code.png
//		String DesFileName = Environment.getExternalStorageDirectory().getPath() + "/code.png";
        byte[] bData1 = null;
        int length = 0;
        InputStream in = null;
        try {
            in = mContext.getResources().getAssets().open("code.png");
            length = in.available();
        } catch (IOException e) {
            e.printStackTrace();
            UTILS.makeToast(mContext, mContext.getString(R.string.file_open_failure));
            return null;
        }
        bData1 = new byte[length];
        try {
            in.read(bData1);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            UTILS.makeToast(mContext, mContext.getString(R.string.file_read_failure));
        }

        // 将图片内容转换为图形打印指令。
        Bitmap bmpPic = null;
        bmpPic = BitmapFactory.decodeByteArray(bData1, 0, length);

        byte[] comStr = null;
        byte[] tmpBUf = null;
        if (bmpPic != null && printerType != null)
            tmpBUf = RemotePrinter.ConvertImage(printerType, bmpPic);

        // 打印机初始化
        comStr = ByteArrayUtils.twoToOne(comStr, Command.a17);
        comStr = ByteArrayUtils.twoToOne(comStr, tmpBUf);
        return comStr;
    }

    //获取表格数据
    public static byte[] getFormByteData(Context context) {
        final byte empty[] = { (byte) 0x20 };
        String mm = context.getString(R.string.table_content);
        String dd = "│｜";
        String dl = "─";
        // 打印3行4列表格
        // 表头 ┌──────┬──────┬──────┬──────┐
        String headLine = "┌───┬───┬───┬───┐";
        // 表间 ├──────┼──────┼──────┼──────┤
        String midLine = "├───┼───┼───┼───┤";
        // 表尾 └──────┴──────┴──────┴──────┘
        String endLine = "└───┴───┴───┴───┘";
        // -------------数据拼接--------------------
        // excel[] 表格的byte数组形式容器
        byte excel[] = null;
        // 打印机初始化
        excel = ByteArrayUtils.twoToOne(excel, Command.a17);
        // 设置间距
        excel = ByteArrayUtils.twoToOne(excel, Command.a35);
        // 回车换行
        excel = ByteArrayUtils.twoToOne(excel, Command.a12);
        // 拼接表格标题
        excel = ByteArrayUtils.twoToOne(excel, Command.a14);// 打印中文，需要发送打印中文指令
        String title = context.getString(R.string.table_title);
        excel = ByteArrayUtils.twoToOne(excel, ByteArrayUtils.stringToByte(title));
        excel = ByteArrayUtils.twoToOne(excel, Command.a12);// 回车换行

        excel = ByteArrayUtils.twoToOne(excel, ByteArrayUtils.stringToByte(headLine));
        excel = ByteArrayUtils.twoToOne(excel, Command.a12);// 回车换行

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 4; i++) {
                excel = ByteArrayUtils.twoToOne(excel, ByteArrayUtils.stringToByte(dd));// |
                excel = ByteArrayUtils.twoToOne(excel, empty);// 左间隔
                // 表格里的中文字符串
                excel = ByteArrayUtils.twoToOne(excel, ByteArrayUtils.stringToByte(mm));// 表格中文英文内容
                excel = ByteArrayUtils.twoToOne(excel, empty);// 右间隔
            }
            excel = ByteArrayUtils.twoToOne(excel, ByteArrayUtils.stringToByte(dd));// |
            excel = ByteArrayUtils.twoToOne(excel, Command.a12);// 回车换行

            if (2 == j) {
                break;
            }
            excel = ByteArrayUtils.twoToOne(excel, ByteArrayUtils.stringToByte(midLine));// 表间线
            excel = ByteArrayUtils.twoToOne(excel, Command.a12);// 回车换行
        }
        excel = ByteArrayUtils.twoToOne(excel, ByteArrayUtils.stringToByte(endLine));
        excel = ByteArrayUtils.twoToOne(excel, Command.a12);// 回车换行
        excel = ByteArrayUtils.twoToOne(excel, Command.a15);// 发送取消打印中文指令
        // 取消设置间距
        excel = ByteArrayUtils.twoToOne(excel, Command.a36);
        // 初始化
        excel = ByteArrayUtils.twoToOne(excel, Command.a17);

        return excel;
    }

    //获取文本数据
    public static byte[] getTextByteData(Context context) {
        PrinterType printerType = PrinterManager.getInstance().getPrinterType();// 打印机类型
        String locale = java.util.Locale.getDefault().getDisplayName();
        // 数据容器strToByte[]
        byte strToByte[] = null;
        // 打印机初始化
        strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a17);

        String str = "文本打印示例：\r\n\r\n";
        String chineseContent = "中文：欢迎使用映美无线打印机！\r\n";
        String englishContent = "ENGLISH:Welcome to use the jolimark wireless printer!\r\n\r\n";
        System.out.println("-------------类型：" + printerType);

        if (printerType == PrinterType.PT_DOT24) { // PT_DOT24支持的打印机指令
            // 打印中文，需要发送打印中文指令，0x1C 0x26是打印中文的指令
            // twoToOne()函数是byte类型数组连接的工具类
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a14);
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(str));
            // 打印默认字体
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(context.getString(R.string.default_typeface)+"\r\n"));
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(chineseContent));
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(englishContent));
//			// 打印斜体
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte("斜体：\r\n"));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a18);
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(chineseContent));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(englishContent));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a19);
            // 打印粗体
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte("粗体：\r\n"));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a20);
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(chineseContent));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(englishContent));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a21);
            // 重叠打印
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte("重叠打印：\r\n"));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a22);
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(chineseContent));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(englishContent));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a23);
            // 下划线 一条实线
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte("下划线一条实线：\r\n"));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a24);
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(chineseContent));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(englishContent));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a26);
            // 下划线 一条虚线
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte("下划线一条虚线：\r\n"));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a25);
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(chineseContent));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(englishContent));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a26);
//			// 倍宽打印
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte("倍宽打印：\r\n"));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a27);
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(chineseContent));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(englishContent));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a28);
            // 倍高倍宽
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte("倍高倍宽打印：\r\n"));
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a29);
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(chineseContent));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(englishContent));
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a30);
            // 倍高
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte("倍高打印：\r\n"));
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a31);
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(chineseContent));
//			strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(englishContent));
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a32);
            // 发送取消打印中文指令 0x1c 0x2e
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a15);
        } else if (printerType == PrinterType.PT_THERMAL || printerType == PrinterType.PT_DOT9) {
            // 默认模式
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a14);// 打印中文，需要发送打印中文指令，0x1C
            // 0x26是打印中文的指令
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(context.getString(R.string.default_typeface)+"\r\n"));
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(chineseContent));
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(englishContent));
            // strToByte = ArrayUtils.twoToOne(strToByte, a15);// 发送取消打印中文指令
            // 0x1c 0x2e
            // 倍宽
            // strToByte = ArrayUtils.twoToOne(strToByte, a14);//中文打印模式
            if (locale.contains("中国")){
                strToByte = ByteArrayUtils.twoToOne(strToByte, Command.b1);
            }else {
                strToByte = ByteArrayUtils.twoToOne(strToByte, Command.b4);
            }
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(context.getString(R.string.double_width)+"\r\n"));
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.b1);
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(chineseContent));
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a15);// 取消中文打印模式
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.b4);
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(englishContent));
            // 倍高

            if (locale.contains("中国")){
                strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a14);// 中文打印模式
                strToByte = ByteArrayUtils.twoToOne(strToByte, Command.b2);
            }else {
                strToByte = ByteArrayUtils.twoToOne(strToByte, Command.b5);
            }
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(context.getString(R.string.double_height)+"\r\n"));
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a14);// 中文打印模式
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.b2);
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(chineseContent));
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a15);// 取消中文打印模式
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.b5);
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(englishContent));
            // 倍宽、倍高字体
            if (locale.contains("中国")){
                strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a14);// 中文打印模式
                strToByte = ByteArrayUtils.twoToOne(strToByte, Command.b3);
            }else {
                strToByte = ByteArrayUtils.twoToOne(strToByte, Command.b6);
            }
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(context.getString(R.string.double_height_and_double_width)+"\r\n"));
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a14);// 中文打印模式
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.b3);
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(chineseContent));
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a15);// 取消中文打印模式
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.b6);
            strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(englishContent));
            // 取消倍宽倍高模式
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.b11);
            strToByte = ByteArrayUtils.twoToOne(strToByte, Command.b12);
        }
        // 打印机初始化
        strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a17);
        strToByte = ByteArrayUtils.twoToOne(strToByte, strToByte);
        return strToByte;
    }

    //退纸(有些打印机不一定支持)
    public static byte[] getBackByteData() {
        byte[] bData3 = { 27, 106, 100 };// 打印并退纸100个单位，其它指令请参考《映美打印机编程手册》。
        return bData3;
    }

    //进纸
    public static byte[] getFeedByteData() {
        byte[] bData2 = { 27, 74, 100 };// 打印并进纸100个单位，其它指令请参考《映美打印机编程手册》。
        return bData2;
    }

//    public static void createPDFFile(Context context) {
//        Document document = new Document();
//        PdfPTable table = new PdfPTable(new float[] { 2, 1, 2 });
//        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.addCell("Name");
//        table.addCell("Age");
//        table.addCell("Location");
//        table.setHeaderRows(1);
//        PdfPCell[] cells = table.getRow(0).getCells();
//        for (int j=0;j<cells.length;j++){
//            cells[j].setBackgroundColor(BaseColor.GRAY);
//        }
//        for (int i=1;i<5;i++){
//            table.addCell("Name:"+i);
//            table.addCell("Age:"+i);
//            table.addCell("Location:"+i);
//        }
//        try{
//            // String filePath = context.getFilesDir().getPath() + "/sample3.pdf";
//            File f = new File(context.getFilesDir(), "sample3.pdf");
//            PdfWriter.getInstance(document, new FileOutputStream(f));
//            document.open();
//            document.add(table);
//            document.close();
//
//            // pdfToBitmap(f, context);
//        } catch (Exception e) {
//            Log.d("Content:", e.toString());
//        }
//    }

//    private static ArrayList<Bitmap> pdfToBitmap(File pdfFile, Context context) {
//        ArrayList<Bitmap> bitmaps = new ArrayList<>();
//
//        try {
//            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));
//
//            Bitmap bitmap;
//            final int pageCount = renderer.getPageCount();
//            for (int i = 0; i < pageCount; i++) {
//                PdfRenderer.Page page = renderer.openPage(i);
//
//                int width = getResources().getDisplayMetrics().densityDpi / 72 * page.getWidth();
//                int height = getResources().getDisplayMetrics().densityDpi / 72 * page.getHeight();
//                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//
//                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//
//                bitmaps.add(bitmap);
//
//                // close the page
//                page.close();
//
//            }
//
//            // close the renderer
//            renderer.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return bitmaps;
//
//    }
}
