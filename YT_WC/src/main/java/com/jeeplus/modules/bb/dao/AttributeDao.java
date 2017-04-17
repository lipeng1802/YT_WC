/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bb.entity.Attribute;
import com.jeeplus.modules.sys.entity.User;

/**
 * 产品属性DAO接口
 * @author lp
 * @version 2017-03-04
 */
@MyBatisDao
public interface AttributeDao extends CrudDao<Attribute> {

    /**
     * 根据属性名查询属性
     * @param loginName
     * @return
     */
    public List<Attribute> getAttributeByName(Attribute attribute);
    
    /**
     * 得到类别得到属性列表
     * @param types
     * @param notTypes
     * @return
     */
    public List<Attribute> findListByTypes(@Param("types")List types, @Param("notTypes")List notTypes);
    
    /**
     * 根据产品ID 得到所有产品属性
     * @param productId
     * @return
     */
    List<Attribute> findProductAttributeList(@Param("productId")String productId);
}