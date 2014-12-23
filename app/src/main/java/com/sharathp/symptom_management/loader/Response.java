package com.sharathp.symptom_management.loader;

public class Response<T> {
    private T mResult;
    private Exception mException;

    public static <T> Response<T> createSuccess(final T data) {
        final Response<T> success = new Response<T>();
        success.mResult = data;
        return success;
    }

    public static <T> Response<T> createFailure(final Exception exception) {
        final Response<T> failure = new Response<T>();
        failure.mException = exception;
        return failure;
    }

    public boolean isSuccess() {
        return mException == null;
    }

    public T getResult() {
        return mResult;
    }

    public Exception getException() {
        return mException;
    }
}