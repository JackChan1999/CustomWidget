package com.google.widget.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.widget.R;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * ============================================================
 * 
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 
 * 作 者 : 陈冠杰
 * 
 * 版 本 ： 1.0
 * 
 * 创建日期 ： 2016-2-21 下午6:06:45
 * 
 * 描 述 ：
 * 
 * 
 * 修订历史 ：
 * 
 * ============================================================
 **/
public class BaseApplication extends Application {
	private static Context mContext;
	private static Handler mHandler;
	// 主线程
	private static Thread mMainThread;
	// 主线程id
	private static int mMainThreadId;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		mHandler = new Handler();
		// application 运行在主线程中
		mMainThread = Thread.currentThread();
		// 主线程的id，就是application 的id
		mMainThreadId = Process.myTid();

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

		ToastMgr.builder.init(mContext);
	}

	public static Context getContext() {
		return mContext;
	}

	public static Handler getHandler() {
		return mHandler;
	}

	public static Thread getMainThread() {
		return mMainThread;
	}

	public static int getMainThreadId() {
		return mMainThreadId;
	}

	class ExceptionHandler implements UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			ex.printStackTrace();
			try {
				PrintWriter printWriter = new PrintWriter(Environment.getExternalStorageDirectory() + "/MyDemo.log");
				ex.printStackTrace(printWriter);
				printWriter.close(); 
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Process.killProcess(Process.myPid());
		}
	}

	public enum ToastMgr {
		builder;

		private View view;
		private TextView tv;
		private Toast toast;

		/**
		 * 初始化toast
		 */
		public void init(Context context){
			view = LayoutInflater.from(context).inflate(R.layout.toast_view,null);
			tv = (TextView) view.findViewById(R.id.tv_toast);
			toast = new Toast(context);
			toast.setView(view);
		}

		/**
		 * 显示toast
		 * @param content 显示的内容
		 * @param duration 持续时间
         */
		public void display(CharSequence content, int duration){
			if (content.length() != 0){
				tv.setText(content);
				toast.setDuration(duration);
				toast.setGravity(Gravity.CENTER,0,0);
				toast.show();
			}
		}

	}
}
