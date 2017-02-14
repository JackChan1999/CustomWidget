package com.google.widget.base;

import android.os.AsyncTask;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/1 22:41
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class AsyncTaskUtils {

    public static <T> void doAsync(final IDataCallBack<T> callBack){
        new AsyncTask<Void,T,T>(){

            @Override
            protected void onPreExecute() {
                callBack.onTaskBefore();
            }

            @Override
            protected T doInBackground(Void... params) {
                return callBack.onTasking(params);
            }

            @Override
            protected void onProgressUpdate(T... values) {
                callBack.onTaskUpdate(values[0]);
            }

            @Override
            protected void onPostExecute(T result) {
                callBack.onTaskAfter(result);
            }
        }.execute();

    }
}
