package com.google.widget.test;

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
public class DieLock extends Thread{
    private boolean frag;

    public DieLock(boolean frag){
        this.frag = frag;
    }

    @Override
    public void run() {
        super.run();
        if (frag){
            synchronized (MyLock.objA){
                System.out.println("if objA");
                synchronized (MyLock.objB){
                    System.out.println("if objB");
                }
            }
        }else {
            synchronized (MyLock.objB){
                System.out.println("else objB");
                synchronized (MyLock.objA){
                    System.out.println("else objA");
                }
            }
        }
    }
}
