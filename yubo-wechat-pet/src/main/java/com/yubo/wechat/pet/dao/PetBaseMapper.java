package com.yubo.wechat.pet.dao;

import com.yubo.wechat.pet.dao.pojo.PetBase;

public interface PetBaseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pet_base
     *
     * @mbggenerated Tue May 17 06:26:41 PDT 2016
     */
    int deleteByPrimaryKey(Integer petId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pet_base
     *
     * @mbggenerated Tue May 17 06:26:41 PDT 2016
     */
    int insert(PetBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pet_base
     *
     * @mbggenerated Tue May 17 06:26:41 PDT 2016
     */
    int insertSelective(PetBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pet_base
     *
     * @mbggenerated Tue May 17 06:26:41 PDT 2016
     */
    PetBase selectByPrimaryKey(Integer petId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pet_base
     *
     * @mbggenerated Tue May 17 06:26:41 PDT 2016
     */
    int updateByPrimaryKeySelective(PetBase record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pet_base
     *
     * @mbggenerated Tue May 17 06:26:41 PDT 2016
     */
    int updateByPrimaryKey(PetBase record);
}