/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pr.entity;

import com.jeeplus.modules.pr.entity.Order;
import com.jeeplus.modules.bb.entity.Product;
import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 订单明细Entity
 * @author lp
 * @version 2017-03-31
 */
public class OrderDetail extends DataEntity<OrderDetail> {
	
	private static final long serialVersionUID = 1L;
	private Order order;		// 订单Id
	private Product product;		// 产品Id
	private Integer size;		// 数量
	private Double price;		// 单价
	private String productDesc;		// 订货产品描述
	private String isStorage;      //是否现货
	public OrderDetail() {
		super();
	}

	public OrderDetail(String id){
		super(id);
	}

	@ExcelField(title="订单", align=2, sort=1)
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	@NotNull(message="产品不能为空")
	@ExcelField(title="产品", align=2, sort=2)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	@NotNull(message="数量不能为空")
	@ExcelField(title="数量", align=2, sort=3)
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	
	@ExcelField(title="单价", align=2, sort=4)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@ExcelField(title="订货产品描述", align=2, sort=5)
	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	@ExcelField(title="是否现货", align=2, sort=5)
    public String getIsStorage() {
        return isStorage;
    }

    public void setIsStorage(String isStorage) {
        this.isStorage = isStorage;
    }
	
	
	
}