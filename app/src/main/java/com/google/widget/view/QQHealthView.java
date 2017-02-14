package com.google.widget.view;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.google.widget.R;
import com.google.widget.ui.activity.QQHealthViewActivity;
import com.google.widget.utils.UIUtil;

public class QQHealthView extends View {

    private int mySize, rank, averageSize;
    private String myaverageTxt;


    //字体颜色,大小,竖线的颜色
    private int textColor, lineColor;
    //背景的画笔
    private Paint backgroundPaint;
    //背景的坐标
    private int radiusBg, widthBg, heightBg;
    private Path pathBg, linePath;
    //圆弧的画笔
    private Paint arcPaint;
    private RectF arcRect;
    //数字的画笔
    private Paint textPaint;
    private PathEffect effects;

    //虚线的画笔
    private Paint linePaint;


    //圆角竖条的距离,高度,平均高度
    private float rectSize, rectAgHeight;
    //圆角竖条的画笔
    private Paint rectPaint;
    private Path rectPath;

    //底部波纹
    private Paint weavPaint;
    private Path weavPath;
    private float weavX;
    private float weavY;

    //动画实现
    //动画效果的添加
    private AnimatorSet animSet;
    private int walkNum, rankNum;
    private float arcNum;


    public void setMySize(int mySize) {
        this.mySize = mySize;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setAverageSize(int averageSize) {
        this.averageSize = averageSize;
    }

    public QQHealthView(Context context) {
        this(context, null);
    }

    public QQHealthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQHealthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取我们自定义的样式属性
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.QQHealthView, defStyleAttr, 0);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.QQHealthView_titleColor:
                    // 默认颜色设置为黑色
                    textColor = array.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.QQHealthView_lineColor:
                    lineColor = array.getColor(attr, Color.GRAY);
                    break;
            }

        }
        array.recycle();
        init();
    }

    //初始化操作
    private void init() {
        pathBg = new Path();
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePath = new Path();
        effects = new DashPathEffect(new float[]{5,5}, 1);
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPath = new Path();
        weavPaint = new Paint();
        weavPaint.setAntiAlias(true);
        weavPath = new Path();
        animSet = new AnimatorSet();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        //如果布局里面设置的是固定值,这里取布局里面的固定值;如果设置的是match_parent,则取父布局的大小
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {

            //如果布局里面没有设置固定值,这里取布局的宽度的1/2
            width = (int) (widthSize * 1.0f / 2);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            //如果布局里面没有设置固定值,这里取布局的高度的3/4
            height = (int) (heightSize * 3.0f / 4);
        }
        widthBg = width;
        heightBg = height;
        setMeasuredDimension(width, height);
        startAnim();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制最底层的背景
        radiusBg = widthBg / 20;
        pathBg.moveTo(0, heightBg);
        pathBg.lineTo(0, radiusBg);
        pathBg.quadTo(0, 0, radiusBg, 0);
        pathBg.lineTo(widthBg - radiusBg, 0);
        pathBg.quadTo(widthBg, 0, widthBg, radiusBg);
        pathBg.lineTo(widthBg, heightBg);
        pathBg.lineTo(0, heightBg);
        backgroundPaint.setColor(Color.WHITE);
        canvas.drawPath(pathBg, backgroundPaint);

        //绘制圆弧
        arcPaint.setStrokeWidth(widthBg / 20);
        //设置空心
        arcPaint.setStyle(Paint.Style.STROKE);
        //防抖动
        arcPaint.setDither(true);
        //连接处为圆弧
        arcPaint.setStrokeJoin(Paint.Join.ROUND);
        //画笔的笔触为圆角
        arcPaint.setStrokeCap(Paint.Cap.ROUND);
        arcPaint.setColor(lineColor);
        //圆弧范围
        arcRect = new RectF(widthBg * 1 / 4, widthBg * 1 / 10, widthBg * 3 / 4, widthBg * 6 / 10);
        //绘制背景大圆弧
        canvas.drawArc(arcRect, 120, 300, false, arcPaint);
        arcPaint.setColor(textColor);
        //绘制分数小圆弧
        canvas.drawArc(arcRect, 120, arcNum, false, arcPaint);

        //绘制圆圈内的数字
        textPaint.setColor(textColor);
        textPaint.setTextSize(widthBg / 10);
        canvas.drawText(String.valueOf(walkNum), widthBg * 3 / 8, widthBg * 2 / 5, textPaint);
        //绘制名次
        textPaint.setTextSize(widthBg / 15);
        if (rankNum < 10){
            canvas.drawText("0"+String.valueOf(rankNum), widthBg * 75 / 160, widthBg * 12 / 20, textPaint);
        }else {
            canvas.drawText(String.valueOf(rankNum), widthBg * 75 / 160, widthBg * 12 / 20, textPaint);
        }


        //绘制其他文字
        textPaint.setColor(lineColor);
        textPaint.setTextSize(widthBg / 25);
        canvas.drawText("截止13:45已走", widthBg * 3 / 8 - 10, widthBg * 2/ 7, textPaint);
        canvas.drawText("好友平均2781步", widthBg * 3 / 8 - 10, widthBg * 1 / 2, textPaint);
        canvas.drawText("第", widthBg * 27 / 64, widthBg * 12 / 20, textPaint);
        canvas.drawText("名", widthBg * 35 / 64, widthBg * 12 / 20 , textPaint);

        //绘制圆圈外的文字
        canvas.drawText("最近7天", widthBg * 1 / 15, widthBg*7/10, textPaint);
        myaverageTxt = String.valueOf(averageSize);
        canvas.drawText("平均", widthBg * 10 / 15 - 15, widthBg*7/10, textPaint);
        canvas.drawText(myaverageTxt, widthBg * 11 / 15, widthBg*7/10, textPaint);
        canvas.drawText("步/天", widthBg * 12 / 15 + 20, widthBg*7/10, textPaint);

        //绘制虚线
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2);
        linePaint.setColor(lineColor);
        linePath.moveTo(widthBg * 1 / 15, widthBg*8/10);
        linePath.lineTo(widthBg * 14 / 15, widthBg*8/10);
        linePaint.setPathEffect(effects);
        canvas.drawPath(linePath, linePaint);

        rectSize = widthBg / 12;
        rectAgHeight = widthBg / 10;
        //绘制虚线上的圆角竖线
        for (int i = 0; i < 7; i++) {
            rectPaint.setStrokeWidth(widthBg / 25);
            rectPaint.setStyle(Paint.Style.STROKE);
            rectPaint.setStrokeJoin(Paint.Join.ROUND);
            rectPaint.setStrokeCap(Paint.Cap.ROUND);
            float startHeight = heightBg*25/40 + rectAgHeight;
            rectPath.moveTo(rectSize, startHeight);
            double percentage = Double.valueOf(QQHealthViewActivity.sizes.get(i)) / Double.valueOf(averageSize);
            double height = percentage * rectAgHeight;
            rectPath.lineTo(rectSize, (float) (startHeight - height));
            rectPaint.setColor(textColor);
            canvas.drawPath(rectPath, rectPaint);
            //canvas.drawLine(rectSize, startHeight,rectSize, (float) (startHeight - height),rectPaint);
            //绘制下方的文字
            textPaint.setColor(lineColor);
            canvas.drawText("0" + (i + 1) + "日", rectSize- UIUtil.dip2px(13), startHeight+UIUtil.dip2px(30), textPaint);
            rectSize += widthBg / 7;
        }
        //绘制底部波纹
        weavPaint.setColor(textColor);
        weavPath.reset();
        weavPath.moveTo(0, heightBg);
        weavPath.lineTo(0, heightBg * 10 / 12);
        weavPath.cubicTo(weavX, weavY,widthBg * 3 / 10, heightBg * 11 / 12, widthBg, heightBg * 10 / 12);
        weavPath.lineTo(widthBg, heightBg);
        //weavPath.lineTo(0, heightBg);
        canvas.drawPath(weavPath, weavPaint);

        //绘制底部文字
        weavPaint.setColor(Color.WHITE);
        weavPaint.setTextSize(widthBg / 20);
        canvas.drawText("成绩不错,继续努力哟!", widthBg * 1 / 10, heightBg * 11 / 12 + 50, weavPaint);
    }

    private void startAnim() {
        //步数动画的实现
        ValueAnimator walkAnimator = ValueAnimator.ofInt(0, mySize);
        walkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                walkNum = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        //排名动画的实现
        ValueAnimator rankAnimator = ValueAnimator.ofInt(0, rank);
        rankAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rankNum = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        double size = mySize;
        double avgSize = averageSize;
        if (size > avgSize) {
            size = avgSize;
        }
        //圆弧动画的实现
        ValueAnimator arcAnimator = ValueAnimator.ofFloat(0, (float) (size / avgSize * 300));
        arcAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                arcNum = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        //水波纹动画的实现
        ValueAnimator weavXAnimator = ValueAnimator.ofFloat(widthBg * 1 / 10, widthBg * 2/ 10);
        weavXAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                weavX = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        ValueAnimator weavYAnimator = ValueAnimator.ofFloat(heightBg*10/12, heightBg*11/12);
        weavYAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                weavY = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        animSet.setDuration(3000);
        animSet.playTogether(walkAnimator, rankAnimator, arcAnimator,weavXAnimator,weavYAnimator);
        animSet.start();
    }

    public void reSet(int mysize, int myrank, int myaverageSize) {
        walkNum = 0;
        arcNum = 0;
        rankNum = 0;
        mySize = mysize;
        rank = myrank;
        averageSize = myaverageSize;
        startAnim();
    }
}
