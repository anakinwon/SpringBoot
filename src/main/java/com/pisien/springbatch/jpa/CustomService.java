package com.pisien.springbatch.jpa;

public class CustomService<T> {

    public void customWrite(T item) {
        System.out.println(" \t\t CustomService.customWrite.item = " + item);
    }


}
