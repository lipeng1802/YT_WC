/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 微信信息Entity
 * @author lp
 * @version 2017-02-24
 */
public class Wechat extends DataEntity<Wechat> {
	
	private static final long serialVersionUID = 1L;
	private String wechatId;		// 微信ID
	private String wechatName;		// 微信昵称
	
	public Wechat() {
		super();
	}

	public Wechat(String id){
		super(id);
	}

	@ExcelField(title="微信ID", align=2, sort=1)
	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}
	
	@ExcelField(title="微信昵称", align=2, sort=2)
	public String getWechatName() {
		return wechatName;
	}

	public void setWechatName(String wechatName) {
		this.wechatName = wechatName;
	}
	
}