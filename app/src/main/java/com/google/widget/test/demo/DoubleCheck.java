package com.google.widget.test.demo;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/19 14:59
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class DoubleCheck {
    private static volatile DoubleCheck ins;

    private DoubleCheck(){}

    public static DoubleCheck getIns(){
        if (ins == null){//第一次检查
            synchronized (DoubleCheck.class){
                if (ins == null){//第二次检查
                    ins = new DoubleCheck();
                }
            }
        }
        return ins;
    }
}
