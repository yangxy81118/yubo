package com.yubo.wechat.user.dao;

import com.yubo.wechat.user.dao.pojo.FeederBase;

public interface FeederBaseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table feeder_base
     *
     * @mbggenerated Tue Jun 07 17:09:36 CST 2016
     */
    int deleteByPrimaryKey(Integer keeperId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table feeder_base
     *
     * @mbggenerated Tue Jun 07 17:09:36 CST 2016
     */
    int insert(FeederBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table feeder_base
     *
     * @mbggenerated Tue Jun 07 17:09:36 CST 2016
     */
    int insertSelective(FeederBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table feeder_base
     *
     * @mbggenerated Tue Jun 07 17:09:36 CST 2016
     */
    FeederBase selectByPrimaryKey(Integer keeperId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table feeder_base
     *
     * @mbggenerated Tue Jun 07 17:09:36 CST 2016
     */
    int updateByPrimaryKeySelective(FeederBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table feeder_base
     *
     * @mbggenerated Tue Jun 07 17:09:36 CST 2016
     */
    int updateByPrimaryKey(FeederBase record);
    
    
    /**
     * 根据微信ID
     * @param wechatId
     * @return
     */
    FeederBase selectByWechatId(String wechatId);
}