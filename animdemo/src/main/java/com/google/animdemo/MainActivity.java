package com.google.animdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private MyTextView  mTv;
    private Button mBtn;
    private ImageView mImage;
    private PageTabStrip mPageTabStrip;
    private String[] title = new String[]{"选项1","选项2","选项3","选项4","选项5","选项6","选项7"};
    private FloatingActionButton mFab;

    private boolean isVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* mTv = (MyTextView) findViewById(R.id.tv);
        mBtn = (Button) findViewById(R.id.btn);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTv.setTranslationX(0);
            }

        });*/

       /* mImage = (ImageView) findViewById(R.id.iv);
        ColorGenerator generator = ColorGenerator.DEFAULT;
        TextDrawable textDrawable = TextDrawable.builder().buildRound("A", generator.getColor("A"));
        mImage.setImageDrawable(textDrawable);*/

       /* mPageTabStrip = (PageTabStrip) findViewById(R.id.pageTabStrip);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        mPageTabStrip.setViewPager(title,5,point.x);*/

        mBtn = (Button) findViewById(R.id.btn);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });
    }

    private void startAnimation() {
        int height = mFab.getHeight();
        int translationY = isVisible ? 0 : height + getMarginBottom();
        Log.e("height---------",height+"");
        Log.e("MarginBottom-----------",getMarginBottom()+"");

        mFab.animate().setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(200).translationY(translationY);
        isVisible = !isVisible;
    }

    private int getMarginBottom() {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = mFab.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }
}
