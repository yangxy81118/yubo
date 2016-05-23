package com.yubo.wechat.user.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IdentificationCodeGenerator {

	static String studentName = "养小鱼";
	
	static String studentNo = "20163021232";

	static String maskKey = "yubo2016STUDENT";

	private static String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
			System.out.println("MD5(" + sourceStr + ",32) = " + result);
			System.out.println("MD5(" + sourceStr + ",16) = "
					+ buf.toString().substring(8, 24));
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return result;
	}

	public static byte[] convert16To8ByteXor(final byte[] byteToConvert16) {
		byte[] xored8Byte = new byte[8];
		// xor msb(1st 8 byte) and lsb (last 8 byte)
		for (int i = 0; i < byteToConvert16.length / 2; i++) {
			xored8Byte[i] = (byte) (byteToConvert16[i] ^ byteToConvert16[i + 8]);
		}
		return xored8Byte;
	}

	public static void main(String[] args) {
		IdentificationCodeGenerator generator = new IdentificationCodeGenerator();
		String bitStr16 = generator.MD5(studentName + studentNo + maskKey)
				.substring(8, 24);
		
		

	}

}
