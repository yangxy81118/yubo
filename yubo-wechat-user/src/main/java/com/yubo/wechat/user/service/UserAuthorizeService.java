package com.yubo.wechat.user.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.wechat.user.dao.UserBaseMapper;
import com.yubo.wechat.user.dao.UserIdentificationMapper;
import com.yubo.wechat.user.dao.pojo.UserBase;
import com.yubo.wechat.user.dao.pojo.UserIdentification;
import com.yubo.wechat.user.vo.AuthorizeVO;

/**
 * 用户验证相关服务
 * 
 * @author young.jason
 *
 */
@Service
public class UserAuthorizeService {

	/**
	 * 通过激活码获取验证用户信息，如果获取成功，则绑定到对应的UserBase上 TODO 这里估计要做事务？
	 * 
	 * @param authCode
	 * @param userId
	 * @return
	 */
	public AuthorizeVO authorizeInfoByCode(String authCode, int userId) {

		UserIdentification identification = userIdentificationMapper
				.selectByCode(authCode);

		if (identification == null) {
			return null;
		}

		AuthorizeVO vo = new AuthorizeVO();
		vo.setIdentiCode(authCode);
		vo.setSchoolId(identification.getSchoolId());
		vo.setStudentName(identification.getStudentName());
		vo.setStudentNo(identification.getStudentNo());
		vo.setStudentClass(identification.getStudentClass());
		vo.setStudentSex(getSex(identification.getStudentSex()));

		UserBase userBaseRecord = new UserBase();
		userBaseRecord.setUserId(userId);
		userBaseRecord.setIdentificationId(identification.getIdentiId());
		userBaseMapper.updateByPrimaryKeySelective(userBaseRecord);

		return vo;
	}

	/**
	 * 根据code获取认证信息
	 * @param authCode
	 * @return
	 */
	public AuthorizeVO getAuthorizeByCode(String authCode) {

		UserIdentification identification = userIdentificationMapper
				.selectByCode(authCode);
		
		if(identification==null){
			return null;
		}

		AuthorizeVO vo = new AuthorizeVO();
		vo.setStudentName(identification.getStudentName());
		vo.setIdentiId(identification.getIdentiId());
		return vo;

	}

	/**
	 * 该用户是否已经激活过
	 * 
	 * @param userId
	 * @return
	 */
	public boolean userIsAuthorized(int userId) {

		UserBase user = userBaseMapper.selectByPrimaryKey(userId);
		return user.getIdentificationId() != null;
	}

	private String getSex(Short studentSex) {

		if (studentSex == null) {
			return null;
		}

		switch (studentSex) {
		case 0:
			return "Girl";
		default:
			return "body";
		}
	}

	@Autowired
	UserIdentificationMapper userIdentificationMapper;

	@Autowired
	UserBaseMapper userBaseMapper;
}
