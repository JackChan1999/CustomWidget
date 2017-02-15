package com.google.widget.domain;

import com.google.widget.utils.PinyinUtils;
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
public class ContactInfo implements Comparable<ContactInfo> {
	public String name;
	public String pinyin;
	public String phone;
	public long id;

	public ContactInfo(String name, String phone, long id) {
		super();
		this.name = name;
		this.phone = phone;
		this.id = id;
		this.pinyin = PinyinUtils.getPinyin(name);
	}

	@Override
	public int compareTo(ContactInfo another) {
		return this.pinyin.compareTo(another.pinyin);
	}

}
