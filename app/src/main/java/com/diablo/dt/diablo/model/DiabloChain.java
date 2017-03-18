package com.diablo.dt.diablo.model;

/**
 * Created by buxianhui on 17/3/18.
 */

public abstract class DiabloChain {
    protected DiabloChain successor;

    public void setSuccessor(DiabloChain successor){
        this.successor = successor;
    }

    public abstract void handlerRequest();
}
