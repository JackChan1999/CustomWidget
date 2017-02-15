package com.google.widget.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
public class ThreadPoolProxy {
	ThreadPoolExecutor	mExecutor;			// 只需创建一次
	int					mCorePoolSize;
	int					mMaximumPoolSize;
	long				mKeepAliveTime;

	public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
		super();
		mCorePoolSize = corePoolSize;
		mMaximumPoolSize = maximumPoolSize;
		mKeepAliveTime = keepAliveTime;
	}

	private ThreadPoolExecutor initThreadPoolExecutor() {//双重检查加锁
		if (mExecutor == null) {
			synchronized (ThreadPoolProxy.class) {
				if (mExecutor == null) {
					TimeUnit unit = TimeUnit.MILLISECONDS;
					BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();// 无界队列
					ThreadFactory threadFactory = Executors.defaultThreadFactory();
					RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();// 丢弃任务并抛出RejectedExecutionException异常。
					mExecutor = new ThreadPoolExecutor(//
							mCorePoolSize,// 核心的线程数
							mMaximumPoolSize,// 最大的线程数
							mKeepAliveTime, // 保持时间
							unit, // 保持时间对应的单位
							workQueue,// 缓存队列/阻塞队列
							threadFactory, // 线程工厂
							handler// 异常捕获器
					);
				}
			}
		}
		return mExecutor;
	}

	/**执行任务*/
	public void execute(Runnable task) {
		initThreadPoolExecutor();
		mExecutor.execute(task);
	}

	/**提交任务*/
	public Future<?> submit(Runnable task) {
		initThreadPoolExecutor();
		return mExecutor.submit(task);
	}

	/**移除任务*/
	public void removeTask(Runnable task) {
		initThreadPoolExecutor();
		mExecutor.remove(task);
	}
}
