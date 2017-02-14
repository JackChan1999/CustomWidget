package com.google.widget.ui.activity;

import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.widget.R;
import com.google.widget.domain.Cheeses;
import com.google.widget.utils.ToastUtil;
import com.google.widget.view.HeaderTab;
import com.google.widget.view.drag.DragLayout;
import com.google.widget.view.drag.DragLayout.Direction;
import com.google.widget.view.drag.DragLayout.OnDragListener;
import com.google.widget.view.drag.DragLayout.Status;
import com.google.widget.view.drag.DragRelativeLayout;
import com.google.widget.view.swipe.SwipeListAdapter;
import com.nineoldandroids.view.ViewHelper;

import java.util.Random;


public class QQDemoActivity extends ListActivity implements OnClickListener {

	private DragLayout mDragLayout;
	private ImageView mHeader;
	private SwipeListAdapter adapter;
	private ListView mLeftList;
	private HeaderTab mTab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_qq);

		initLeftContent();
		initMainContent();
	}

	private void initMainContent() {

		mTab = (HeaderTab) findViewById(R.id.tab);
		mTab.bindTab(mCallback,"消息","电话");
		mTab.leftClick();

		mDragLayout = (DragLayout) findViewById(R.id.dsl);
		mDragLayout.setDragListener(mDragListener);

		DragRelativeLayout mMainView = (DragRelativeLayout) findViewById(R.id.rl_main);
		mMainView.setDragLayout(mDragLayout);
		mHeader = (ImageView) findViewById(R.id.iv_head);
		mHeader.setOnClickListener(this);
	
		mBtRight = findViewById(R.id.iv_head_right);
		mBtRight.setOnClickListener(this);
		mHeader.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				mDragLayout.switchScaleEnable();
				return true;
			}
		});

		
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings));
		adapter = new SwipeListAdapter(QQDemoActivity.this);
		setListAdapter(adapter);
		mDragLayout.setAdapterInterface(adapter);
		
		getListView().setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
					adapter.closeAllLayout();
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		
	}

	private void initLeftContent() {

		mLeftList = (ListView) findViewById(R.id.lv_left);
		mLeftList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings){
			@Override
			public View getView(int position, View convertView,
					ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView mText = (TextView) view.findViewById(android.R.id.text1);
				mText.setTextColor(Color.WHITE);
				return view;
			}
		});
	}
	
	private OnDragListener mDragListener = new OnDragListener() {

		@Override
		public void onOpen() {
			mLeftList.smoothScrollToPosition(new Random().nextInt(30));
		}
		@Override
		public void onClose() {
			shakeHeader();
			mBtRight.setSelected(false);
		}

		@Override
		public void onDrag(final float percent) {
			Log.d("TAG", "onDrag: " + percent);
			// 主界面左上角头像渐渐消失
			ViewHelper.setAlpha(mHeader, 1 - percent);
		}

		@Override
		public void onStartOpen(Direction direction) {
			ToastUtil.toast("onStartOpen: " + direction.toString());
		}
	};
	private View mBtRight;

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_head:
			mDragLayout.open(true);
			break;
		case R.id.iv_head_right:
			mDragLayout.open(true, Direction.Right);
			mBtRight.setSelected(true);
			break;

		default:
			break;
		}

	};

	private void shakeHeader() {
		mHeader.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
	}

	@Override
	public void onBackPressed() {

		if (mDragLayout.getStatus() != Status.Close) {
			mDragLayout.close();
			return;
		}
		super.onBackPressed();
	}

	private HeaderTab.ITabActionCallback mCallback = new HeaderTab.ITabActionCallback() {
		@Override
		public void onLeftTabClick() {
			ToastUtil.toast("left tab click");
		}

		@Override
		public void onRightTabClick() {
			ToastUtil.toast("right tab click");
		}
	};

}
