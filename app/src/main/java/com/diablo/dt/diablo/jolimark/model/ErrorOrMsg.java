package com.diablo.dt.diablo.jolimark.model;

/**
 * Created by buxianhui on 17/5/7.
 */

public class ErrorOrMsg {
    // --------manager
    public static final int CONNECTING = 0x122; //正在连接中
    public static final int CONNECT_SUCCESS = 0x123;// 连接成功
    public static final int CONNECT_FAILED = 0x124;// 连接失败
    public static final int CONNECT_EXIST = 0x125;// 已经连接
    public static final int CONFIG_NULL = 0x126;// 参数没设置

    public static final int SEND_SUCCESS = 0x127;// 发送成功
    public static final int SEND_FAILED = 0x128;// 发送失败

    public static final int UNCONNECT = 0x129;// 无连接
    public static final int PRINTER_NULL = 0x130;// printer对象为空

    // --------manager
    // --------文件操作
    public static final int OPEN_FILE_FAILE = 0x131;
    public static final int READ_FILE_FAILE = 0x132;
    // --------文件操作
    // --------操作状态
    public static final int STATE_OK = 0x133; // 打印完成
    public static final int STATE_COMM_ER = 0x134; // 通讯失败
    public static final int STATE_CONNECT_ER = 0x135; // 连接失败
    public static final int STATE_NO_SELECT_PRINTER = 0x136; // 未选择打印机
    public static final int PICK_REQUEST_TEXT = 0x137; // 发送打印机指令文件
    // --------操作状态
    public static final int DEVICE_FOUND = 0x138; // 发现打印机设备

    public static final int CLOSE_SUCCESS = 0x139;// 关闭连接成功
    public static final int CLOSE_FAILED = 0x140;// 关闭连接失败

    public static final int BT_CLASSIC = 0x141;// 传统蓝牙
    public static final int BT_BLE = 0x142;// ble蓝牙

    public static final int SEARCH_FINISHED = 0x143;// 搜索完成
    public static final int DATA_EMPTY = 0x144;// 数据为空

    public static final int PRINTING = 0x145;// 打印中
    public static final int PRINT_WAIT = 0x146;// 打印中,请稍后

    public static final int TASK_CANCEL_SUCCESS = 0x150;//取消任务成功
    public static final int TASK_CANCEL_FAILED = 0x151;//取消任务失败

    public static final int FIND_USB_PRINTER = 0x160;
    public static final int USB_PRINTER_STYLE_ERROR = 0x161;
}
