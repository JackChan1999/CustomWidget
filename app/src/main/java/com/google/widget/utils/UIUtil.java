package com.google.widget.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import com.google.widget.base.BaseApplication;

/**
 * ============================================================
 * 
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 
 * 作 者 : 陈冠杰
 * 
 * 版 本 ： 1.0
 * 
 * 创建日期 ： 2016-2-21 下午6:06:31
 * 
 * 描 述 ：
 * 
 * 
 * 修订历史 ：
 * 
 * ============================================================
 **/
public class UIUtil {
	/**
	 * 获取全局的上下文
	 * 
	 * @return
	 */
	public static Context getContext() {
		return BaseApplication.getContext();
	}

	/**
	 * 获取全局的Handler
	 * 
	 * @return
	 */
	public static Handler getHandler() {
		return BaseApplication.getHandler();
	}

	/**
	 * 获取主线程的方法
	 * 
	 * @return
	 */
	public static Thread getThread() {
		return BaseApplication.getMainThread();
	}

	/**
	 * 获取主线程的id
	 * 
	 * @return
	 */
	public static int getMainThreadId() {
		return BaseApplication.getMainThreadId();
	}

	/**
	 * 
	 * @param layoutId
	 *            布局文件的资源id
	 * @return 布局文件资源id 对应的view
	 */
	public static View inflate(int layoutId) {
		return View.inflate(getContext(), layoutId, null);
	}

	/**
	 * 
	 * @return 获取资源文件夹对象
	 */
	public static Resources getResources() {
		return getContext().getResources();
	}

	/**
	 * 
	 * @param stringid
	 *            字符串对应的资源id
	 * @return string.xml 中字符串对应资源id 对应的值
	 */
	public static String getString(int stringid) {
		return getResources().getString(stringid);
	}

	/**
	 * 
	 * @param stringArrayid
	 *            字符串数组对应的资源id
	 * @return string.xml 中字符串数组对应资源id 对应的值
	 */
	public static String[] getStringArray(int stringArrayid) {
		return getResources().getStringArray(stringArrayid);
	}

	// dip2px 1:0.75 1:1 1:0.5 1:2 1:3
	public static int dip2px(int dip) {
		float density = getResources().getDisplayMetrics().density;
		return (int) (density * dip + 0.5);
	}

	// px2dip
	public static int px2dip(int px) {
		float density = getResources().getDisplayMetrics().density;
		return (int) (px / density + 0.5);
	}

	/**
	 * 
	 * @param runnable
	 *            保证方法在主线程中运行
	 */
	public static void runOnMainThread(Runnable runnable) {
		if (android.os.Process.myTid() == getMainThreadId()) {
			// 在主线程中的方法直接运行
			runnable.run();
		} else {
			// 不在主线程中放入主线程中运行
			getHandler().post(runnable);
		}
	}

	public static ColorStateList getColorStateList(int mTabTextColorResId) {
		return getResources().getColorStateList(mTabTextColorResId);
	}

	public static Drawable getDrawable(int drawableId) {
		return getResources().getDrawable(drawableId);
	}
}
