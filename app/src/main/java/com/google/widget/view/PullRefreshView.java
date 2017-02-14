package com.google.widget.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.widget.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/26 22:44
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class PullRefreshView extends ListView implements AbsListView.OnScrollListener {

    private final int PULL_REFRESH = 0;
    private final int RELEASE_REFRESH = 1;
    private final int REFRESHING = 2;
    private int CURRENT_STATE = PULL_REFRESH;

    private View mHeaderView;
    private View mFooterView;
    private TextView mTv_last_time;
    private ImageView mIv_arrow;
    private TextView mTv_state;
    private ProgressBar mPb_rotate;
    private int mHeaderHeight;
    private int mFooterHeight;
    private RotateAnimation upAnimation,downAnimation;
    private LayoutInflater mInflater;

    public PullRefreshView(Context context) {
        this(context, null);
    }

    public PullRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(getContext());
        initHeaderView();
        initFooterView();
        initListener();
        initRotateAnimation();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = mInflater.inflate(R.layout.layout_header, null);
        mTv_last_time = (TextView) mHeaderView.findViewById(R.id.tv_last_time);
        mTv_state = (TextView) mHeaderView.findViewById(R.id.tv_state);
        mIv_arrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        mPb_rotate = (ProgressBar) mHeaderView.findViewById(R.id.pb_rotate);

        mHeaderView.measure(0, 0);
        mHeaderHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);

        mTv_last_time.setText("最后刷新：" + getCurrentTime());

        addHeaderView(mHeaderView);
    }

    /**
     * 初始化脚布局
     */
    private void initFooterView() {
        mFooterView = mInflater.inflate(R.layout.layout_footer, null);
        mFooterView.measure(0, 0);
        mFooterHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0, -mHeaderHeight, 0, 0);
        addFooterView(mFooterView);
    }

    private void initListener() {
        setOnScrollListener(this);
    }

    /**
     * 初始化旋转动画
     */
    private void initRotateAnimation() {
        upAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(300);
        upAnimation.setFillAfter(true);
        downAnimation = new RotateAnimation(-180, -360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(300);
        downAnimation.setFillAfter(true);
    }

    private float downY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (CURRENT_STATE == REFRESHING) {
                    break;
                }

                int paddingTop = (int) (ev.getY() - downY - mHeaderHeight + 0.5f);
                if (paddingTop > -mHeaderHeight && getFirstVisiblePosition() == 0) {
                    mHeaderView.setPadding(0, paddingTop, 0, 0);
                    if (paddingTop >= 0 && CURRENT_STATE == PULL_REFRESH) {
                        CURRENT_STATE = RELEASE_REFRESH;
                        refreshHeaderView();
                    } else if (paddingTop < 0 && CURRENT_STATE == RELEASE_REFRESH) {
                        CURRENT_STATE = PULL_REFRESH;
                        refreshHeaderView();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (CURRENT_STATE == PULL_REFRESH) {
                    mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);
                } else if (CURRENT_STATE == RELEASE_REFRESH){
                    CURRENT_STATE = REFRESHING;
                    mHeaderView.setPadding(0, 0, 0, 0);
                    refreshHeaderView();
                    if (mListener != null) {
                        mListener.onPullRefresh();
                    }
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 根据当前的状态刷新头布局
     */
    private void refreshHeaderView() {
        switch (CURRENT_STATE) {
            case PULL_REFRESH:
                mTv_state.setText("下拉刷新");
                mIv_arrow.startAnimation(downAnimation);
                break;
            case RELEASE_REFRESH:
                mTv_state.setText("松开刷新");
                mIv_arrow.startAnimation(upAnimation);
                break;
            case REFRESHING:
                mIv_arrow.clearAnimation();
                mTv_state.setText("正在刷新...");
                mPb_rotate.setVisibility(View.VISIBLE);
                mIv_arrow.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * 获取当前时间
     * @return
     */
    private String getCurrentTime() {
        return new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 完成刷新操作，重置状态,在你获取完数据并更新完adater之后，去在UI线程中调用该方法
     */
    public void completeRefresh() {
        if (isLoadingMore) {
            isLoadingMore = false;
            mFooterView.setPadding(0, -mFooterHeight, 0, 0);
        } else {
            CURRENT_STATE = PULL_REFRESH;
            mIv_arrow.setVisibility(View.VISIBLE);
            mPb_rotate.setVisibility(View.INVISIBLE);
            mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);
            mTv_state.setText("下拉刷新");
            mTv_last_time.setText("最后刷新：" + getCurrentTime());
        }
    }

    private boolean isLoadingMore;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (OnScrollListener.SCROLL_STATE_IDLE == scrollState && getLastVisiblePosition() ==
                (getCount() - 1) && !isLoadingMore) {
            isLoadingMore = true;
            setSelection(getCount() - 1);
            mFooterView.setPadding(0, 0, 0, 0);
            if (mListener != null) {
                mListener.onLoadingMore(isLoadingMore);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int
            totalItemCount) {

    }

    private OnRefreshListener mListener;

    public interface OnRefreshListener {
        void onPullRefresh();
        void onLoadingMore(boolean isLoadingMore);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

}
