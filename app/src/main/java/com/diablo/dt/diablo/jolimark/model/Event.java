package com.diablo.dt.diablo.jolimark.model;

/**
 * 事件对象
 * @author zhrjian
 *
 */
public class Event {
    public int msg;// 事件信息
    public String message;
    public int  code;

    public Event(int msg) {
        this.msg = msg;
    }

//    public Event(int msg,String message) {
//        this.msg = msg;
//        this.message = message;
//    }
//
//    public Event(int msg,String message,int code){
//        this.msg = msg;
//        this.message = message;
//        this.code = code;
//    }
}
