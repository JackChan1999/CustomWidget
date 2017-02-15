package com.google.widget.view.swipe;

import android.content.Context;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.widget.R;
import com.google.widget.utils.ToastUtil;
import com.google.widget.view.reminder.GooViewListener;
import com.google.widget.view.swipe.SwipeLayout.SwipeListener;

import java.util.HashSet;

import static com.google.widget.domain.Cheeses.NAMES;
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
public class SwipeListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	HashSet<Integer> mRemoved = new HashSet<Integer>();
	HashSet<SwipeLayout> mUnClosedLayouts = new HashSet<SwipeLayout>();

	public SwipeListAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return 120;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static final int[] HEAD_IDS = new int[]{
		R.mipmap.head_1,
		R.mipmap.head_2,
		R.mipmap.head_3
	};
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		if (convertView != null) {
			mHolder = (ViewHolder) convertView.getTag();
		}else {
			convertView = (SwipeLayout) mInflater.inflate(R.layout.list_item_swipe, null);
			mHolder = ViewHolder.fromValues(convertView);
			convertView.setTag(mHolder);
		}
		SwipeLayout view = (SwipeLayout) convertView;
		
		view.close(false, false);

		view.getFrontView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToastUtil.toast("item click: " + position);
			}
		});

		view.setSwipeListener(mSwipeListener);
		
		mHolder.mImage.setImageResource(HEAD_IDS[position % HEAD_IDS.length]);
		mHolder.mName.setText(NAMES[position % NAMES.length]);

		mHolder.mButtonCall.setTag(position);
		mHolder.mButtonCall.setOnClickListener(onActionClick);

		mHolder.mButtonDel.setTag(position);
		mHolder.mButtonDel.setOnClickListener(onActionClick);
		
		
		TextView mUnreadView = mHolder.mReminder;
		boolean visiable = !mRemoved.contains(position);
		mUnreadView.setVisibility(visiable ? View.VISIBLE : View.GONE);

		if (visiable) {
			mUnreadView.setText(String.valueOf(position));
			mUnreadView.setTag(position);
			GooViewListener mGooListener = new GooViewListener(mContext, mUnreadView) {
				@Override
				public void onDisappear(PointF mDragCenter) {
					super.onDisappear(mDragCenter);

					mRemoved.add(position);
					notifyDataSetChanged();
					ToastUtil.toast("Cheers! We have get rid of it!");
				}

				@Override
				public void onReset(boolean isOutOfRange) {
					super.onReset(isOutOfRange);

					notifyDataSetChanged();
					ToastUtil.toast(isOutOfRange ? "Are you regret?" : "Try again!");
				}
			};
			mUnreadView.setOnTouchListener(mGooListener);
		}

		return view;
	}

	OnClickListener onActionClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Integer p = (Integer) v.getTag();
			int id = v.getId();
			if (id == R.id.bt_call) {
				closeAllLayout();
				ToastUtil.toast("position: " + p + " call");
			} else if (id == R.id.bt_delete) {
				closeAllLayout();
				ToastUtil.toast("position: " + p + " del");
			}
		}
	};
	SwipeListener mSwipeListener = new SwipeListener() {
		@Override
		public void onOpen(SwipeLayout swipeLayout) {
//			Utils.showToast(mContext, "onOpen");
			mUnClosedLayouts.add(swipeLayout);
		}

		@Override
		public void onClose(SwipeLayout swipeLayout) {
//			Utils.showToast(mContext, "onClose");
			mUnClosedLayouts.remove(swipeLayout);
		}

		@Override
		public void onStartClose(SwipeLayout swipeLayout) {
//			Utils.showToast(mContext, "onStartClose");
		}

		@Override
		public void onStartOpen(SwipeLayout swipeLayout) {
//			Utils.showToast(mContext, "onStartOpen");
			closeAllLayout();
			mUnClosedLayouts.add(swipeLayout);
		}

	};
	public int getUnClosedCount(){
		return mUnClosedLayouts.size();
	}
	
	public void closeAllLayout() {
		if(mUnClosedLayouts.size() == 0)
			return;
		
		for (SwipeLayout l : mUnClosedLayouts) {
			l.close(true, false);
		}
		mUnClosedLayouts.clear();
	}
	
	static class ViewHolder {

		public ImageView mImage;
		public Button mButtonCall;
		public Button mButtonDel;
		public TextView mReminder;
		public TextView mName;
		
		private ViewHolder(ImageView mImage, Button mButtonCall,
				Button mButtonDel, TextView mReminder, TextView mName) {
			super();
			this.mImage = mImage;
			this.mButtonCall = mButtonCall;
			this.mButtonDel = mButtonDel;
			this.mReminder = mReminder;
			this.mName = mName;
		}


		public static ViewHolder fromValues(View view) {
			return new ViewHolder(
				(ImageView) view.findViewById(R.id.iv_head),
				(Button) view.findViewById(R.id.bt_call),
				(Button) view.findViewById(R.id.bt_delete),
				(TextView) view.findViewById(R.id.point),
				(TextView) view.findViewById(R.id.tv_name));
		}
	}

}