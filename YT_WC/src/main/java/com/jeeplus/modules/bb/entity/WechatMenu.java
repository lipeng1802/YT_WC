/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.jeeplus.common.persistence.TreeEntity;

/**
 * 微信菜单Entity
 * @author lp
 * @version 2017-04-12
 */
public class WechatMenu extends TreeEntity<WechatMenu> {
	
	private static final long serialVersionUID = 1L;
	private WechatMenu parent;		// 父级ID
	private String parentIds;		// 父级ID链
	private String name;		// 按钮名称
	private String type;		// 按钮类型
	private String key;		// 按钮值
	private String url;		// 指向网页地址
	private Integer sort;		// 排序
	
	public WechatMenu() {
		super();
	}

	public WechatMenu(String id){
		super(id);
	}

	@JsonBackReference
	public WechatMenu getParent() {
		return parent;
	}

	public void setParent(WechatMenu parent) {
		this.parent = parent;
	}
	
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}