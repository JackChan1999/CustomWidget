package com.google.widget.base;

import android.os.AsyncTask;

/**
 * ============================================================
 * Copyright：Google有限公司版权所有 (c) 2017
 * Author：   陈冠杰
 * Email：    815712739@qq.com
 * GitHub：   https://github.com/JackChen1999
 * 博客：     http://blog.csdn.net/axi295309066
 * 微博：     AndroidDeveloper
 * <p>
 * Project_Name：Widgets
 * Package_Name：com.google.widget
 * Version：1.0
 * time：2016/2/15 14:09
 * des ：AsyncTaskUtils
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
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
