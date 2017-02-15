package com.google.widget.utils;

import com.google.widget.pinyin.Pinyin;

import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
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
public class PinyinUtils {
	/**
	 * 根据传入的字符串(包含汉字),得到拼音 
	 * 黑马 -> HEIMA 
	 * 黑 马*& -> HEIMA 
	 * 黑马f5 -> HEIMA
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static String getPinyin(String str) {

		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		StringBuilder sb = new StringBuilder();

		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			// 如果是空格, 跳过
			if (Character.isWhitespace(c)) {
				continue;
			}//c >= -127 && c < 128
			if (!Pinyin.isChinese(c)) {
				// 肯定不是汉字
				sb.append(c);
			} else {
//				String s = "";
//				try {
//					// 通过char得到拼音集合. 单 -> dan, shan
//					s = PinyinHelper.toHanyuPinyinStringArray(c, format)[0];
//					sb.append(s);
//				} catch (BadHanyuPinyinOutputFormatCombination e) {
//					e.printStackTrace();
//					sb.append(s);
//				}
				String pinyin = Pinyin.toPinyin(c);
				sb.append(pinyin);
			}
		}

		return sb.toString();
	}
}
