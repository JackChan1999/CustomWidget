package com.google.widget.test;

import java.io.Serializable;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/6/18 08:59
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class Student implements Serializable {
    private static final long serialVersionUID = -234567890L;
    public String name;
    public transient int age;
}
