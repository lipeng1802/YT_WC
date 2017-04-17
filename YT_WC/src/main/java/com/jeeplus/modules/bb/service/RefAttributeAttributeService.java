/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bb.dao.AttributeDao;
import com.jeeplus.modules.bb.dao.RefAttributeAttributeDao;
import com.jeeplus.modules.bb.entity.Attribute;
import com.jeeplus.modules.bb.entity.RefAttributeAttribute;
import com.jeeplus.modules.bb.web.paramEntity.ColorConfig;

/**
 * 花色配置Service
 * @author lp
 * @version 2017-03-04
 */
@Service
@Transactional(readOnly = true)
public class RefAttributeAttributeService extends CrudService<RefAttributeAttributeDao, RefAttributeAttribute> {

    @Autowired
    private RefAttributeAttributeDao refAttributeAttributeDao;
    @Autowired
    private AttributeDao attributeDao;
    
	public RefAttributeAttribute get(String id) {
		return super.get(id);
	}
	
	public List<RefAttributeAttribute> findList(RefAttributeAttribute refAttributeAttribute) {
		return super.findList(refAttributeAttribute);
	}
	
	public Page<RefAttributeAttribute> findPage(Page<RefAttributeAttribute> page, RefAttributeAttribute refAttributeAttribute) {
		return super.findPage(page, refAttributeAttribute);
	}
	
	@Transactional(readOnly = false)
	public void save(RefAttributeAttribute refAttributeAttribute) {
		super.save(refAttributeAttribute);
	}
	
	@Transactional(readOnly = false)
	public void delete(RefAttributeAttribute refAttributeAttribute) {
		super.delete(refAttributeAttribute);
	}
	
	@Transactional(readOnly = false)
    public void saveConfig(ColorConfig config) {
        //1.将原本花色配置，全部删掉
	    refAttributeAttributeDao.deleteRefByAttr(config.getAttribute());
	    
	    //2.重新保存
	    List<Attribute> list=config.getAttrList();
	    for(Attribute attr:list){
	        RefAttributeAttribute refAttributeAttribute=new RefAttributeAttribute();
	        refAttributeAttribute.setAttributeId1(config.getAttribute());
	        refAttributeAttribute.setAttributeId2(new Attribute(attr.getId()));
	        this.save(refAttributeAttribute);
	    }
	    
    }
	/**
	 * 查询配置的某个属性的 所有属性
	 * @param attribute
	 * @return
	 */
	public List<Attribute> findListByAttribute(Attribute attribute){
	    return refAttributeAttributeDao.findListByAttribute(attribute);
	}
	
	
	
}