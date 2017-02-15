package com.google.widget.factory;
import com.google.widget.manager.ThreadPoolProxy;

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
public class ThreadPoolFactory {
	static ThreadPoolProxy mNormalPool;
	static ThreadPoolProxy	mDownLoadPool;

	/**得到一个普通的线程池*/
	public static ThreadPoolProxy getNormalPool() {
		if (mNormalPool == null) {
			synchronized (ThreadPoolProxy.class) {
				if (mNormalPool == null) {
					mNormalPool = new ThreadPoolProxy(5, 5, 3000);
				}
			}
		}
		return mNormalPool;
	}
	/**得到一个下载的线程池*/
	public static ThreadPoolProxy getDownLoadPool() {
		if (mDownLoadPool == null) {
			synchronized (ThreadPoolProxy.class) {
				if (mDownLoadPool == null) {
					mDownLoadPool = new ThreadPoolProxy(3, 3, 3000);
				}
			}
		}
		return mDownLoadPool;
	}
}
