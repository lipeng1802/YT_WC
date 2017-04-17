/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 微信客户信息Entity
 * @author lp
 * @version 2017-02-24
 */
public class Customer extends DataEntity<Customer> {
	
	private static final long serialVersionUID = 1L;
	private String wechatId;		// 微信ID
	private String name;
	private String contactPerson;		// 联系人
	private String phone;		// 电话
	private String address;		// 地址
	private String isUse;		// 是否默认使用
	
	
	private Wechat weChat;      // 是否默认使用
	
	public Customer() {
		super();
	}

	public Customer(String id){
		super(id);
	}

	@ExcelField(title="微信ID", align=2, sort=1)
	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}
	@ExcelField(title="客户名称", align=2, sort=2)
	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ExcelField(title="联系人", align=2, sort=2)
	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	
	@ExcelField(title="电话", align=2, sort=3)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ExcelField(title="地址", align=2, sort=4)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@ExcelField(title="是否默认使用", align=2, sort=5)
	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

    public Wechat getWeChat() {
        return weChat;
    }

    public void setWeChat(Wechat weChat) {
        this.weChat = weChat;
    }
	
	
}