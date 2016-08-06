package com.yubo.wechat.user.util;


public class IdentificationCodeGenerator {

	static String sql = "INSERT INTO user_identification (student_name,student_class,student_no,student_sex,school_id,identi_code) VALUES ($1,\"测试班级\",20160808,1,2,?);";
	
	
	public static void main(String[] args) {
		
		
		for (int j = 0; j < 30; j++) {
			String currentSQL = sql;
			String code = "";
			int max = 10;
			for (int i = 0; i < 14; i++) {
				int t = (int) (Math.random() * max);
				code += t + "";
			}
			currentSQL = currentSQL.replace("?", code);
			currentSQL = currentSQL.replace("$1", "\"测试"+j+"\"");
			System.out.println(currentSQL);
		}
		
	}
}
