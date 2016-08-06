package com.yubo.wechat.user.vo;

/**
 * 用户验证信息VO
 * 
 * @author young.jason
 *
 */
public class AuthorizeVO {

	private String studentName;
	private String studentClass;
	private String studentNo;
	private String studentSex;
	private Integer schoolId;
	private String schoolName;
	private String identiCode;
	private Integer identiId;

	public Integer getIdentiId() {
		return identiId;
	}

	public void setIdentiId(Integer identiId) {
		this.identiId = identiId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentClass() {
		return studentClass;
	}

	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getStudentSex() {
		return studentSex;
	}

	public void setStudentSex(String studentSex) {
		this.studentSex = studentSex;
	}

	public Integer getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Integer schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getIdentiCode() {
		return identiCode;
	}

	public void setIdentiCode(String identiCode) {
		this.identiCode = identiCode;
	}

	@Override
	public String toString() {
		return "AuthorizeVO [studentName=" + studentName + ", studentClass="
				+ studentClass + ", studentNo=" + studentNo + ", studentSex="
				+ studentSex + ", schoolId=" + schoolId + ", schoolName="
				+ schoolName + ", identiCode=" + identiCode + ", identiId="
				+ identiId + "]";
	}

}
