/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bb.entity.Attribute;
import com.jeeplus.modules.bb.entity.Product;

/**
 * 产品信息DAO接口
 * @author lp
 * @version 2017-02-24
 */
@MyBatisDao
public interface ProductDao extends CrudDao<Product> {

	public List<Product> findProductAndAttributeList2(Product product);
	
	public List<Product> findProductAttribute(Product product);
	
	public Integer findRepeatByAttribute(@Param("attributeIds")List attributeIds,
	        @Param("attributeCount")int attributeCount,
	        @Param("notProductId")String notProductId);

    
}