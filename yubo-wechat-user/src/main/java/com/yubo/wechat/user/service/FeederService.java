package com.yubo.wechat.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yubo.wechat.user.dao.FeederBaseMapper;
import com.yubo.wechat.user.dao.pojo.FeederBase;
import com.yubo.wechat.user.vo.FeederVO;

/**
 * 管理员相关服务，暂时规划到会员中心
 * @author yangxy8
 *
 */
@Service
public class FeederService {

	/**
	 * 根据微信ID获取管理员信息
	 * 
	 * @param wechatId
	 * @return
	 */
	public FeederVO getFeederInfoByWechatId(String wechatId) {
		
		FeederBase fb = feederBaseMapper.selectByWechatId(wechatId);
		if(fb!=null){
			return new FeederVO();
		}else{
			return null;
		}
	}

	
	@Autowired
	FeederBaseMapper feederBaseMapper;
	
	
}
