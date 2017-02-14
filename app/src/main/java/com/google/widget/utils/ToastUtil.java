package com.google.widget.utils;

import android.widget.Toast;

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
 * 创建日期 ： 2016-2-21 上午10:47:56
 * 
 * 描 述 ：
 * 		土司工具类
 * 
 * 修订历史 ：
 * 
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
