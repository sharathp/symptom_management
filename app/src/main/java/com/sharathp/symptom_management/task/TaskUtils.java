package com.sharathp.symptom_management.task;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

public class TaskUtils {

    /**
     * Execute the async task on a ThreadPool consistently across platforms.
     *
     * @param task - async task
     * @param params - params to the async task
     * @param <T> - param type
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }
}
