package com.yubo.wechat.user.dao;

import com.yubo.wechat.user.dao.pojo.TalkHistory;

public interface TalkHistoryMapper {

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table talk_history
	 * @mbggenerated  Tue May 17 08:27:43 PDT 2016
	 */
	int deleteByPrimaryKey(Long talkId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table talk_history
	 * @mbggenerated  Tue May 17 08:27:43 PDT 2016
	 */
	int insert(TalkHistory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table talk_history
	 * @mbggenerated  Tue May 17 08:27:43 PDT 2016
	 */
	int insertSelective(TalkHistory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table talk_history
	 * @mbggenerated  Tue May 17 08:27:43 PDT 2016
	 */
	TalkHistory selectByPrimaryKey(Long talkId);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table talk_history
	 * @mbggenerated  Tue May 17 08:27:43 PDT 2016
	 */
	int updateByPrimaryKeySelective(TalkHistory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table talk_history
	 * @mbggenerated  Tue May 17 08:27:43 PDT 2016
	 */
	int updateByPrimaryKeyWithBLOBs(TalkHistory record);

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table talk_history
	 * @mbggenerated  Tue May 17 08:27:43 PDT 2016
	 */
	int updateByPrimaryKey(TalkHistory record);
}