package com.yubo.feeder.dao;

import java.util.List;
import java.util.Map;

import com.yubo.feeder.dao.pojo.ResourceSvg;

public interface ResourceSvgMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_svg
     *
     * @mbggenerated Sun Jun 12 18:01:10 CST 2016
     */
    int deleteByPrimaryKey(Integer svgId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_svg
     *
     * @mbggenerated Sun Jun 12 18:01:10 CST 2016
     */
    int insert(ResourceSvg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_svg
     *
     * @mbggenerated Sun Jun 12 18:01:10 CST 2016
     */
    int insertSelective(ResourceSvg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_svg
     *
     * @mbggenerated Sun Jun 12 18:01:10 CST 2016
     */
    ResourceSvg selectByPrimaryKey(Integer svgId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_svg
     *
     * @mbggenerated Sun Jun 12 18:01:10 CST 2016
     */
    int updateByPrimaryKeySelective(ResourceSvg record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table resource_svg
     *
     * @mbggenerated Sun Jun 12 18:01:10 CST 2016
     */
    int updateByPrimaryKey(ResourceSvg record);
    
    /**
     * 分页查询
     * @return
     */
    List<ResourceSvg> paging(Map<String,Object> param);
    
    /**
     * 记录条数查询
     * @param param
     * @return
     */
    int countSelective(Map<String,Object> param);
}