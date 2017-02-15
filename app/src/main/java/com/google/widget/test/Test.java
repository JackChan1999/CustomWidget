package com.google.widget.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.View;

import com.google.widget.R;
import com.google.widget.test.proxy.AfterAdvice;
import com.google.widget.test.proxy.BeforeAdvice;
import com.google.widget.test.proxy.ManWaiter;
import com.google.widget.test.proxy.ProxyFactory;
import com.google.widget.test.proxy.Waiter;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

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
public class Test extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                Palette.Swatch darkVibrant = palette.getDarkVibrantSwatch();
                Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();
                Palette.Swatch muted = palette.getMutedSwatch();
                Palette.Swatch darkMuted = palette.getDarkMutedSwatch();
                Palette.Swatch lightMuted = palette.getLightMutedSwatch();

                int bodyTextColor = vibrant.getBodyTextColor();
                int titleTextColor = vibrant.getTitleTextColor();
                int rgb = vibrant.getRgb();
                int population = vibrant.getPopulation();

                int vibrantColor = palette.getVibrantColor(Color.WHITE);
                int mutedColor = palette.getMutedColor(Color.WHITE);
            }
        });

        Class clazz = (Class) ((ParameterizedType)this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        String name = clazz.getName();

        ProxyFactory factory = new ProxyFactory();
        factory.setTarget(new ManWaiter());//设置目标对象
        factory.setBeforeAdvice(new BeforeAdvice() {
            @Override
            public void before() {
                // do something
            }
        });
        factory.setAfterAdvice(new AfterAdvice() {
            @Override
            public void after() {
                //do something
            }
        });

        Waiter waiter = (Waiter) factory.createProxy();
        waiter.shouqian();





    }


    public void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                deleteFolder(file);
            } else {
                file.delete();
            }
        }
        folder.delete();
    }

    public void setProperty(Object obj, String propertyName, Object value) throws
            NoSuchFieldException, IllegalAccessException {
        Class clazz = obj.getClass();
        Field field = clazz.getDeclaredField("propertyName");
        field.setAccessible(true);
        field.set(obj,value);
    }

    private class MyInvocationHandler implements InvocationHandler{
        private Object target;

        public MyInvocationHandler(Object target){
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            long start = System.currentTimeMillis();
            SystemClock.sleep(100);
            Object result = method.invoke(proxy, args);
            System.out.println(method.getName() + "方法运行了"
                    + (System.currentTimeMillis() - start)+"毫秒");

            return result;
        }
    }

    class MyBehavior extends CoordinatorLayout.Behavior{

        public MyBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View
                directTargetChild, View target, int nestedScrollAxes) {
            return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                    nestedScrollAxes);
        }

        @Override
        public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View
                target, int dx, int dy, int[] consumed) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        }
    }



}
