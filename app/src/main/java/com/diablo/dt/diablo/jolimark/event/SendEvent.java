package com.diablo.dt.diablo.jolimark.event;

/**
 * Created by buxianhui on 17/5/7.
 */

public class SendEvent {
    private static final String TAG = "SendEvent";
    public int msg;// 事件信息

    public SendEvent(int msg) {
        this.msg = msg;
    }
}
