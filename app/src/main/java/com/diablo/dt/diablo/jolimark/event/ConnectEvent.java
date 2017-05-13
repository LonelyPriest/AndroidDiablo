package com.diablo.dt.diablo.jolimark.event;

/**
 * Created by buxianhui on 17/5/7.
 */

public class ConnectEvent {
    private static final String TAG = "ConnectEvent";
    public int msg;// 事件信息

    public ConnectEvent(int msg) {
        this.msg = msg;
    }
}
