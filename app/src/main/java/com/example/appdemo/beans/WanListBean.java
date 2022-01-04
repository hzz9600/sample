package com.example.appdemo.beans;

import java.util.List;

public class WanListBean<T>{
    public int curPage;
    public int offset;
    public boolean over;
    public int pageCount;
    public int size;
    public int total;
    public List<T> datas;
}
