package com.pisien.springbatch.skipRetry;

public class CustomSkipException extends RuntimeException {

    public CustomSkipException() { super(); }

    public CustomSkipException(String message) { super(message); }
}
