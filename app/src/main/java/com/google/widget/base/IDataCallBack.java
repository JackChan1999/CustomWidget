package com.google.widget.base;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/1 22:42
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public interface IDataCallBack<T> {
    void onTaskBefore();

    T onTasking(Void... params);

    void onTaskUpdate(T progress);

    void onTaskAfter(T result);

}
