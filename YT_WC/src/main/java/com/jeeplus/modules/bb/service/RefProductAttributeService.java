/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bb.entity.Attribute;
import com.jeeplus.modules.bb.entity.Product;
import com.jeeplus.modules.bb.entity.RefProductAttrubite;
import com.jeeplus.modules.bb.dao.RefProductAttributeDao;

/**
 * 产品属性Service
 * @author lp
 * @version 2017-03-23
 */
@Service
@Transactional(readOnly = true)
public class RefProductAttributeService extends CrudService<RefProductAttributeDao, RefProductAttrubite> {

    @Autowired
    private RefProductAttributeDao productAttributeDao;
	public RefProductAttrubite get(String id) {
		return super.get(id);
	}
	
	public List<RefProductAttrubite> findList(RefProductAttrubite refProductAttrubite) {
		return super.findList(refProductAttrubite);
	}
	
	public Page<RefProductAttrubite> findPage(Page<RefProductAttrubite> page, RefProductAttrubite refProductAttrubite) {
		return super.findPage(page, refProductAttrubite);
	}
	
	@Transactional(readOnly = false)
	public void save(RefProductAttrubite refProductAttrubite) {
		super.save(refProductAttrubite);
	}
	
	@Transactional(readOnly = false)
	public void delete(RefProductAttrubite refProductAttrubite) {
		super.delete(refProductAttrubite);
	}
	
	@Transactional(readOnly = false)
    public void deleteByProduct(String productId) {
	    productAttributeDao.deleteByProduct(productId);
    }
	@Transactional(readOnly = false)
	public void saveByProduct(Product product){
	    this.deleteByProduct(product.getId());
	    for(Attribute a : product.getAttributeList()){
	        RefProductAttrubite pa=new RefProductAttrubite();
	        pa.setAttribute(a);
	        pa.setProduct(product);
	        save(pa);
	    }
	}
	
	
}