package com.google.widget.domain;

import com.google.widget.utils.PinyinUtils;

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
