package com.google.widget.utils;

import android.widget.Toast;

import com.google.widget.base.BaseApplication;

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
 * des ：
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 **/
public class ToastUtil {
	/**
	 * 展示一个安全的土司
	 * 
	 * @param content 内容
	 * @param duration 持续时间
	 */
	public static void toast(String content, int duration) {
		if (content == null){
			return;
		}else {
			BaseApplication.ToastMgr.builder.display(content,duration);
			/*// 当在主线程时，土司直接弹出
			if (Thread.currentThread().getName().equals("main")) {
				BaseApplication.ToastMgr.builder.display(content,duration);
			} else {
				// 在子线程时，让土司在主线程中弹出
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {

						BaseApplication.ToastMgr.builder.display(content,duration);
					}
				});
			}*/
		}

	}

	/**
	 * 显示默认持续时间为short的Toast
	 * @param content 内容
     */
	public static void toast(String content){
		if (content == null){
			return;
		}else {
			BaseApplication.ToastMgr.builder.display(content, Toast.LENGTH_SHORT);
		}
	}

}
