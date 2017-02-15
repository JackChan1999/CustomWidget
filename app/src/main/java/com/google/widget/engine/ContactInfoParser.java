package com.google.widget.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.google.widget.domain.ContactInfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
public class ContactInfoParser {
	/**
	 * 返回联系人的图片
	 * 
	 * @param context
	 * @param contactId
	 * @return
	 */
	public static Bitmap getBitmap(Context context, long contactId) {
		ContentResolver contentResolver = context.getContentResolver();
		// 返回图片的uri
		// 第一个参数：
		// 第二个参数表示图片的id
		Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
				contactId + "");
		// 打开联系人图片的流
		// 第一个参数contentResolver
		// 第二个参数图片的uri 地址
		InputStream is = ContactsContract.Contacts.openContactPhotoInputStream(
				contentResolver, uri);
		Bitmap bitmap = BitmapFactory.decodeStream(is);
		return bitmap;
	}

	/**
	 * 第二种实现方式
	 * 
	 * @param context
	 * @return
	 */
	public static List<ContactInfo> findAll(Context context) {
		ContentResolver contentResolver = context.getContentResolver();
		// ContactsContract.CommonDataKinds.Phone 这个是电话地址
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String[] projection = new String[] {
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,// 获取到用户名
				ContactsContract.CommonDataKinds.Phone.NUMBER,// 电话号码
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID // 联系人id
		};
		// 查询联系人
		Cursor cursor = contentResolver
				.query(uri, projection, null, null, null);
		List<ContactInfo> list = new ArrayList<ContactInfo>();
		while (cursor.moveToNext()) {
			String name = cursor.getString(0);
			String phone = cursor.getString(1);
			int contactId = cursor.getInt(2);
			ContactInfo info2 = new ContactInfo(name,phone,contactId);
			list.add(info2);
		}
		return list;
	}
}
