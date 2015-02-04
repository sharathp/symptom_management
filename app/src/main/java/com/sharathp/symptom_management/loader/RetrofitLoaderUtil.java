package com.sharathp.symptom_management.loader;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public class RetrofitLoaderUtil {

    public static <T, SVC> void init(final LoaderManager manager, final int loaderId,
                                   final RetrofitLoader<T, SVC> loader, final Callback<T> callback) {

        manager.initLoader(loaderId, Bundle.EMPTY, new LoaderManager.LoaderCallbacks<Response<T>>() {

            @Override
            public Loader<Response<T>> onCreateLoader(int id, Bundle args) {
                return loader;
            }

            @Override
            public void onLoadFinished(Loader<Response<T>> loader, Response<T> data) {
                if (data.isSuccess()) {
                    callback.onSuccess(data.getResult());
                } else {
                    callback.onFailure(data.getException());
                }
            }

            @Override
            public void onLoaderReset(Loader<Response<T>> loader) {
                // no-op
            }
        });
    }
}