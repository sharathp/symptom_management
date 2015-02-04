package com.sharathp.symptom_management.loader;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public abstract class RetrofitLoader<T, SVC> extends AsyncTaskLoader<Response<T>> {
    private SVC mService;
    private Response<T> mCachedResponse;

    public RetrofitLoader(Context context, SVC service) {
        super(context);
        mService = service;
    }

    @Override
    public Response<T> loadInBackground() {
        try {
            final T data = call(mService);
            return Response.createSuccess(data);
        } catch (final Exception ex) {
            return Response.createFailure(ex);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mCachedResponse != null) {
            deliverResult(mCachedResponse);
        }

        if (takeContentChanged() || mCachedResponse == null) {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(final Response<T> data) {
        if (isReset()) {
            return;
        }

        // cache data..
        mCachedResponse = data;

        // Only deliver result if the loader is started..
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        clearResources();
    }

    private void clearResources() {
        mCachedResponse = null;
    }

    public abstract T call(final SVC service);
}