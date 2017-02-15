package com.google.widget.test.demo;

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
public class Student {
    private String name;
    private int age;
    private boolean frag;

    public synchronized void set(String name, int age){
        if (this.frag){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.name = name;
            this.age = age;

            this.frag = true;
            this.notify();
        }
    }

    public synchronized void get(){
        if (!this.frag){

        }
    }
}
