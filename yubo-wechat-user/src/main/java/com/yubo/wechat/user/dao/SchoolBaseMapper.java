package com.yubo.wechat.user.dao;

import com.yubo.wechat.user.dao.pojo.SchoolBase;

public interface SchoolBaseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table school_base
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    int deleteByPrimaryKey(Integer schoolId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table school_base
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    int insert(SchoolBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table school_base
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    int insertSelective(SchoolBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table school_base
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    SchoolBase selectByPrimaryKey(Integer schoolId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table school_base
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    int updateByPrimaryKeySelective(SchoolBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table school_base
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    int updateByPrimaryKey(SchoolBase record);
}