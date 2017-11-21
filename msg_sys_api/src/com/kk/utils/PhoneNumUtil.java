package com.kk.utils;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

public class PhoneNumUtil {

	public static boolean isMobileNO(String mobiles) {

		//根据实际的开通的电话号码段匹配
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		System.out.println(m.matches() + "---");

		return m.matches();
	}

	public static boolean flagisPhoneNum(String number) {

		String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";

		Pattern p = Pattern.compile(regExp);

		Matcher m = p.matcher(number);

		return m.find();// boolean
	}

	public static void main(String[] args) {
		System.out.println(flagisPhoneNum("13911132523"));
	}
}