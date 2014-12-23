package com.sharathp.symptom_management.loader;

public interface Callback<T> {
    void onSuccess(T result);

    void onFailure(Exception e);
}
