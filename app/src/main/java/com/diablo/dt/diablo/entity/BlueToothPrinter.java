package com.diablo.dt.diablo.entity;

/**
 * Created by buxianhui on 17/5/13.
 */

public class BlueToothPrinter {
    private String name;
    private String mac;

    public BlueToothPrinter(String name, String mac) {
        this.name = name;
        this.mac = mac;
    }

    public String getName() {
        return this.name;
    }

    public String getMac() {
        return this.mac;
    }
}
