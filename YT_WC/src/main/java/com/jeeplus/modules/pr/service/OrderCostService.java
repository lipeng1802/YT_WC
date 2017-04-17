/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pr.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.pr.entity.OrderCost;
import com.jeeplus.modules.pr.dao.OrderCostDao;

/**
 * 订单费用Service
 * @author lp
 * @version 2017-03-31
 */
@Service
@Transactional(readOnly = true)
public class OrderCostService extends CrudService<OrderCostDao, OrderCost> {

	public OrderCost get(String id) {
		return super.get(id);
	}
	
	public List<OrderCost> findList(OrderCost orderCost) {
		return super.findList(orderCost);
	}
	
	public Page<OrderCost> findPage(Page<OrderCost> page, OrderCost orderCost) {
		return super.findPage(page, orderCost);
	}
	
	@Transactional(readOnly = false)
	public void save(OrderCost orderCost) {
		super.save(orderCost);
	}
	
	@Transactional(readOnly = false)
	public void delete(OrderCost orderCost) {
		super.delete(orderCost);
	}
	
	
	
	
}