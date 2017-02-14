package com.google.widget.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.widget.R;
import com.google.widget.utils.AnimUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RotateMenuActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.iv_home)
    public ImageView mIv_home;
    @Bind(R.id.iv_menu)
    public ImageView mIv_menu;
    @Bind(R.id.rl_level1)
    public RelativeLayout mLevel1;
    @Bind(R.id.rl_level2)
    public RelativeLayout mLevel2;
    @Bind(R.id.rl_level3)
    public RelativeLayout mLevel3;
    @Bind(R.id.btn_menu)
    public Button btn_menu;

    private boolean isShowlevel2 = true;
    private boolean isShowlevel3 = true;
    private boolean isShowmenu = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        setContentView(R.layout.activity_rotate_menu);
        ButterKnife.bind(this);

        SpannableString title = new SpannableString("三级旋转菜单");
        title.setSpan(new ForegroundColorSpan(Color.WHITE),0,title.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
    }

    private void initListener() {
        mIv_home.setOnClickListener(this);
        mIv_menu.setOnClickListener(this);
        btn_menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_home:
                if (AnimUtil.animCount != 0){
                    return;
                }
                if (isShowlevel2){
                    int startOffset = 0;
                    if (isShowlevel3){
                        AnimUtil.closeMenu(mLevel3,startOffset);
                        startOffset += 200;
                        isShowlevel3 = false;
                    }
                    AnimUtil.closeMenu(mLevel2,startOffset);
                }else {
                    AnimUtil.openMenu(mLevel2,0);
                }
                isShowlevel2 = !isShowlevel2;
                break;
            case R.id.iv_menu:
                if (AnimUtil.animCount != 0){
                    return;
                }
                if (isShowlevel3){
                    AnimUtil.closeMenu(mLevel3,0);
                }else {
                    AnimUtil.openMenu(mLevel3,0);
                }
                isShowlevel3 = !isShowlevel3;
                break;
            case R.id.btn_menu:
                showMenu();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU){
            showMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showMenu(){
        if (isShowmenu){
            int startOffset = 0;
            if (isShowlevel3){
                AnimUtil.closeMenu(mLevel3,startOffset);
                isShowlevel3 = false;
                startOffset += 200;
            }
            if (isShowlevel2){
                AnimUtil.closeMenu(mLevel2,startOffset);
                isShowlevel2 = false;
                startOffset += 200;
            }
            AnimUtil.closeMenu(mLevel1,startOffset);
        }else {
            AnimUtil.openMenu(mLevel1,0);
            AnimUtil.openMenu(mLevel2,200);
            isShowlevel2 = true;
            AnimUtil.openMenu(mLevel3,400);
            isShowlevel3 = true;
        }
        isShowmenu = !isShowmenu;
    }
}
