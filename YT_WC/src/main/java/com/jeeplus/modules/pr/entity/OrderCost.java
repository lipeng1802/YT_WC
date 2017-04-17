/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pr.entity;

import com.jeeplus.modules.pr.entity.Order;
import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 订单费用Entity
 * @author lp
 * @version 2017-03-31
 */
public class OrderCost extends DataEntity<OrderCost> {
	
	private static final long serialVersionUID = 1L;
	private Order order;		// 订单Id
	private String subject;		// 费用项目
	private Double money;		// 金额
	
	public OrderCost() {
		super();
	}

	public OrderCost(String id){
		super(id);
	}

	@ExcelField(title="订单Id", align=2, sort=1)
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	@ExcelField(title="费用项目", dictType="subject_type", align=2, sort=2)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@NotNull(message="金额不能为空")
	@ExcelField(title="金额", align=2, sort=3)
	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}
	
}