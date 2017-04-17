/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 产品属性Entity
 * @author lp
 * @version 2017-03-04
 */
public class Attribute extends DataEntity<Attribute> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 属性值
	private String nameDesc;		// 属性描述
	private String sort;		// 排序
	private String type;		// 类型
	private Double price;		// 价格
	
	public Attribute() {
		super();
	}

	public Attribute(String id){
		super(id);
	}

	@ExcelField(title="属性值", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="属性描述", align=2, sort=2)
	public String getNameDesc() {
		return nameDesc;
	}

	public void setNameDesc(String nameDesc) {
		this.nameDesc = nameDesc;
	}
	
	@ExcelField(title="排序", align=2, sort=3)
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	@ExcelField(title="类型", dictType="attribute_type", align=2, sort=4)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@ExcelField(title="价格", align=2, sort=5)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
}