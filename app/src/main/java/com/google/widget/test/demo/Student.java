package com.google.widget.test.demo;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/18 21:13
 * 描 述 ：
 * 修订历史 ：
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
