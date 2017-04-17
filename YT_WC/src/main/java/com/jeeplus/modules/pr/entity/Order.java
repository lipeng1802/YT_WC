/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pr.entity;

import com.jeeplus.modules.bb.entity.Attribute;
import com.jeeplus.modules.bb.entity.Wechat;
import com.jeeplus.modules.bb.entity.Customer;

import javax.validation.constraints.NotNull;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 订单Entity
 * @author lp
 * @version 2017-03-31
 */
public class Order extends DataEntity<Order> {
	
	private static final long serialVersionUID = 1L;
	private Wechat wechat;		// 微信Id
	private Customer customer;		// 客户Id
	private String sn;		// 订单编码
	private Date orderDate;		// 定货日期
	private String state;		// 订单状态
	private String postMethod;		// 邮寄方式
	private String payMethod;		// 付款方式
	private String invoice;		// 发票
	private Double total;      // 总金额
	private List<OrderDetail> detailList = Lists.newArrayList(); // 按明细设置数据范围
	
	//查询字段
	private String orderInfo;
	private Date beginDate;
	private Date endDate;
	
	public Order() {
		super();
	}

	public Order(String id){
		super(id);
	}

	@ExcelField(title="微信Id", align=2, sort=1)
	public Wechat getWechat() {
		return wechat;
	}

	public void setWechat(Wechat wechat) {
		this.wechat = wechat;
	}
	
	@NotNull(message="客户不能为空")
	@ExcelField(title="客户", align=2, sort=2)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

	
	@ExcelField(title="订单编码", align=2, sort=3)
	public String getSn() {
		return sn;
	}


    public void setSn(String sn) {
		this.sn = sn;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="定货日期", align=2, sort=4)
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	
	@ExcelField(title="订单状态", dictType="order_type", align=2, sort=5)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@ExcelField(title="邮寄方式", align=2, sort=6)
	public String getPostMethod() {
		return postMethod;
	}

	public void setPostMethod(String postMethod) {
		this.postMethod = postMethod;
	}
	
	@ExcelField(title="付款方式", align=2, sort=7)
	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	
	@ExcelField(title="发票", align=2, sort=8)
	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	@ExcelField(title="总金额", align=2, sort=4)
    public List<OrderDetail> getDetailList() {
        return detailList;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setDetailList(List<OrderDetail> detailList) {
        this.detailList = detailList;
    }

    @Transient
    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
    @Transient
    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    @Transient
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    
    
	
}