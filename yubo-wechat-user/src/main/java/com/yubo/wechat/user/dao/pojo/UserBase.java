package com.yubo.wechat.user.dao.pojo;

import java.util.Date;

public class UserBase {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_base.user_id
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_base.wechat_id
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    private Integer wechatId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_base.identification_id
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    private Integer identificationId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_base.pet_id
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    private Integer petId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_base.last_modify
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    private Date lastModify;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_base.valid
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    private Boolean valid;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_base.user_id
     *
     * @return the value of user_base.user_id
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_base.user_id
     *
     * @param userId the value for user_base.user_id
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_base.wechat_id
     *
     * @return the value of user_base.wechat_id
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    public Integer getWechatId() {
        return wechatId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_base.wechat_id
     *
     * @param wechatId the value for user_base.wechat_id
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    public void setWechatId(Integer wechatId) {
        this.wechatId = wechatId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_base.identification_id
     *
     * @return the value of user_base.identification_id
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    public Integer getIdentificationId() {
        return identificationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_base.identification_id
     *
     * @param identificationId the value for user_base.identification_id
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    public void setIdentificationId(Integer identificationId) {
        this.identificationId = identificationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_base.pet_id
     *
     * @return the value of user_base.pet_id
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    public Integer getPetId() {
        return petId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_base.pet_id
     *
     * @param petId the value for user_base.pet_id
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_base.last_modify
     *
     * @return the value of user_base.last_modify
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    public Date getLastModify() {
        return lastModify;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_base.last_modify
     *
     * @param lastModify the value for user_base.last_modify
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_base.valid
     *
     * @return the value of user_base.valid
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    public Boolean getValid() {
        return valid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_base.valid
     *
     * @param valid the value for user_base.valid
     *
     * @mbggenerated Tue May 17 06:26:02 PDT 2016
     */
    public void setValid(Boolean valid) {
        this.valid = valid;
    }
}