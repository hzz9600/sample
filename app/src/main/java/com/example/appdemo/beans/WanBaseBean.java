package com.example.appdemo.beans;

public class WanBaseBean<T> {
    public int errorCode;
    public String errorMsg;
    public T data;

    @Override
    public String toString() {
        return "WanBaseBean{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
