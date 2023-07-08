package com.pisien.springbatch.repeat.skip;

public class NoSkippableException extends Exception {
    public NoSkippableException(String s) {
        super(s);
    }
}
