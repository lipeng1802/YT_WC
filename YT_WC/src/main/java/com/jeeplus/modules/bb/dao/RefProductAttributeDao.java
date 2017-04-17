/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bb.entity.RefProductAttrubite;

/**
 * 产品属性DAO接口
 * @author lp
 * @version 2017-03-23
 */
@MyBatisDao
public interface RefProductAttributeDao extends CrudDao<RefProductAttrubite> {

    void deleteByProduct(@Param("productId")String productId);
    

}