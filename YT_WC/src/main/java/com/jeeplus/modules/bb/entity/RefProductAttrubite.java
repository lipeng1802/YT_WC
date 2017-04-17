/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.entity;

import com.jeeplus.modules.bb.entity.Product;
import com.jeeplus.modules.bb.entity.Attribute;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 产品属性Entity
 * @author lp
 * @version 2017-03-23
 */
public class RefProductAttrubite extends DataEntity<RefProductAttrubite> {
	
	private static final long serialVersionUID = 1L;
	private Product product;		// 产品Id
	private Attribute attribute;		// 属性Id
	
	public RefProductAttrubite() {
		super();
	}

	public RefProductAttrubite(String id){
		super(id);
	}

	@ExcelField(title="产品Id", align=2, sort=1)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	@ExcelField(title="属性Id", align=2, sort=2)
	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	
}