/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bb.entity.Attribute;
import com.jeeplus.modules.bb.entity.RefAttributeAttribute;

/**
 * 花色配置DAO接口
 * @author lp
 * @version 2017-03-04
 */
@MyBatisDao
public interface RefAttributeAttributeDao extends CrudDao<RefAttributeAttribute> {

    /**
     * 删除属性配置所有记录
     * @param attribute
     * @return
     */
    public int deleteRefByAttr(Attribute attribute);
    
    /**
     * 查询配置的某个属性的 所有属性
     */
    public List<Attribute> findListByAttribute(Attribute attribute);
}