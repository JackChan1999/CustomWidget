package com.google.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.google.widget.R;

public class ToggleButton extends View {

    private Bitmap switch_on;
    private Bitmap switch_off;
    private Bitmap slide_bitmap;

    private boolean isOpen;
    private boolean isTouching;

    private int currentX;

    private OnToggleChangeListener mListener;

    public ToggleButton(Context context) {
        this(context, null);
    }

    public ToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ToggleButton);
        int indexCount = array.getIndexCount();
        for (int i=0; i<indexCount; i++){
            int attr = array.getIndex(i);
            switch (attr){
                case R.styleable.ToggleButton_switch_on:
                    switch_on = ((BitmapDrawable) array.getDrawable(attr)).getBitmap();
                    break;
                case R.styleable.ToggleButton_switch_off:
                    switch_off = ((BitmapDrawable) array.getDrawable(attr)).getBitmap();
                    break;
                case R.styleable.ToggleButton_current_state:
                    isOpen = array.getBoolean(attr,false);
                    break;
                case R.styleable.ToggleButton_slide_bitmap:
                    slide_bitmap = ((BitmapDrawable) array.getDrawable(attr)).getBitmap();
                    break;
            }
        }
    }

    /**
     * 设置开关为打开状态的背景图片
     * @param resId
     */
    public void setSwitchOnBackground(int resId) {
        switch_on = BitmapFactory.decodeResource(getResources(), resId);
    }

    /**
     * 设置开关为关闭状态的背景图片
     * @param resId
     */
    public void setSwitchOffBackground(int resId) {
        switch_off = BitmapFactory.decodeResource(getResources(), resId);
    }

    /**
     * 设置滑块图片
     * @param resId
     */
    public void setSlideBackground(int resId) {
        slide_bitmap = BitmapFactory.decodeResource(getResources(), resId);
    }

    /**
     * 设置开关状态
     * @param isOpen
     */
    public void setState(boolean isOpen) {
        this.isOpen = isOpen;
    }

    /**
     * 获取当前状态
     * @return
     */
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(switch_on.getWidth(), switch_on.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (isOpen) {
            canvas.drawBitmap(switch_on, 0, 0, null);
        } else {
            canvas.drawBitmap(switch_off, 0, 0, null);
        }

        if (isTouching) {
            int left = currentX - slide_bitmap.getWidth() / 2;
            if (left < 0) {
                left = 0;
            } else if (left > (switch_on.getWidth() - slide_bitmap.getWidth())) {

                left = switch_on.getWidth() - slide_bitmap.getWidth();
            }
            canvas.drawBitmap(slide_bitmap, left, 0, null);
        } else {
            if (isOpen) {
                canvas.drawBitmap(slide_bitmap, switch_on.getWidth()
                        - slide_bitmap.getWidth(), 0, null);
            } else {
                canvas.drawBitmap(slide_bitmap, 0, 0, null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int center = switch_on.getWidth() / 2;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouching = true;
                currentX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                isTouching = true;
                currentX = (int) event.getX();
                boolean move_state = isOpen;
                isOpen = currentX > center;
                if (mListener != null && move_state != isOpen) {
                    mListener.onToggleChange(isOpen);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isTouching = false;
                currentX = (int) event.getX();
                boolean state = isOpen;
                isOpen = currentX > center;
                if (mListener != null && state != isOpen) {
                    mListener.onToggleChange(isOpen);
                }
                invalidate();
                break;
        }

        return true;
    }

    // 滑动开关状态改变监听接口
    public interface OnToggleChangeListener {
        void onToggleChange(boolean isOpen);
    }

    public void setOnToggleChangeListener(OnToggleChangeListener listener) {
        mListener = listener;
    }

}
