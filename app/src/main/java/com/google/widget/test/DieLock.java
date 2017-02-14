package com.google.widget.test;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/18 18:10
 * 描 述 ：
 * 修订历史 ：
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
