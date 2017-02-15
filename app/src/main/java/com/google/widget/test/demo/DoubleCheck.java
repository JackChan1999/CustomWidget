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
