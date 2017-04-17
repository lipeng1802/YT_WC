/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.entity;

import com.jeeplus.modules.bb.entity.Attribute;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 花色配置Entity
 * @author lp
 * @version 2017-03-04
 */
public class RefAttributeAttribute extends DataEntity<RefAttributeAttribute> {
	
	private static final long serialVersionUID = 1L;
	private Attribute attributeId1;		// attribute_id1
	private Attribute attributeId2;		// attribute_id2
	
	public RefAttributeAttribute() {
		super();
	}

	public RefAttributeAttribute(String id){
		super(id);
	}

	@ExcelField(title="attribute_id1", align=2, sort=1)
	public Attribute getAttributeId1() {
		return attributeId1;
	}

	public void setAttributeId1(Attribute attributeId1) {
		this.attributeId1 = attributeId1;
	}
	
	@ExcelField(title="attribute_id2", align=2, sort=2)
	public Attribute getAttributeId2() {
		return attributeId2;
	}

	public void setAttributeId2(Attribute attributeId2) {
		this.attributeId2 = attributeId2;
	}
	
}