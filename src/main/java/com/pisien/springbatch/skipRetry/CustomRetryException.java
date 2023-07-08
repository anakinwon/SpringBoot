package com.pisien.springbatch.skipRetry;

public class CustomRetryException extends Exception {

    public CustomRetryException(String message) { super(message); }

}
