/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pr.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.pr.entity.OrderDetail;

/**
 * 订单明细DAO接口
 * @author lp
 * @version 2017-03-31
 */
@MyBatisDao
public interface OrderDetailDao extends CrudDao<OrderDetail> {

	public List<OrderDetail> findByOrder(OrderDetail orderDetail);
}