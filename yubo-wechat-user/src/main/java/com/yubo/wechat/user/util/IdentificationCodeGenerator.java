package com.yubo.wechat.user.util;


public class IdentificationCodeGenerator {

	public static void main(String[] args) {
		String s = "";
		int max = 10;
		for (int i = 0; i < 14; i++) {
			int t = (int) (Math.random() * max);
			s += t + "";
		}
		System.out.println(s);
	}
}
