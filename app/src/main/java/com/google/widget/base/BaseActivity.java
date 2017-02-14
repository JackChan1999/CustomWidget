package com.google.widget.base;

import android.app.Activity;
import android.os.Bundle;

import com.google.widget.utils.ToastUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * ============================================================
 * 
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 
 * 作 者 : 陈冠杰
 * 
 * 版 本 ： 1.0
 * 
 * 创建日期 ： 2016年4月26日 下午2:19:30
 * 
 * 描 述 ：抽取BaseActivity 管理所有activity 方便退出
 *      
 * 修订历史 ：
 * 
 * ============================================================
 **/
public abstract class BaseActivity extends Activity {
	// 管理运行的所有的activity
	public final static List<BaseActivity> mActivities = new LinkedList<BaseActivity>();

	// private KillAllReceiver receiver;
	// private class KillAllReceiver extends BroadcastReceiver{
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// finish();
	// }
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// receiver=new KillAllReceiver();
		// IntentFilter filter=new IntentFilter("com.itheima.google.killall");
		// registerReceiver(receiver, filter);

		synchronized (mActivities) {
			mActivities.add(this);
		}
		initView();
		initData();
		initListener();
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		synchronized (mActivities) {
			mActivities.remove(this);
		}
		// if(receiver!=null){
		// unregisterReceiver(receiver);
		// receiver=null;
		// }
	}

	public void killAll() {
		// 复制了一份mActivities 集合
		List<BaseActivity> copy;
		synchronized (mActivities) {
			copy = new LinkedList<BaseActivity>(mActivities);
		}
		for (BaseActivity activity : copy) {
			activity.finish();
		}
		// 杀死当前的进程
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	protected abstract void initView();

	protected void initData() {
	}
	
	protected void initListener() {
	}

	public <T> void doAsync(IDataCallBack<T> callBack){
		AsyncTaskUtils.doAsync(callBack);
	}

	public void toast(String content){
		ToastUtil.toast(content);
	}

	public void toast(String content,int duration){
		ToastUtil.toast(content,duration);
	}
}
