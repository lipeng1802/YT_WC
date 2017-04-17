/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pr.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.pr.entity.OrderCost;

/**
 * 订单费用DAO接口
 * @author lp
 * @version 2017-03-31
 */
@MyBatisDao
public interface OrderCostDao extends CrudDao<OrderCost> {

	
}