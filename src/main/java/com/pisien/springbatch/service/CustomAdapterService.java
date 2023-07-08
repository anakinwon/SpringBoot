package com.pisien.springbatch.service;

// 제너릭으로 만든다.
public class CustomAdapterService<T> {

    private int cnt = 0;

    public T customAdapterReader() {
        return (T)("item"+ cnt++);
    }
}
