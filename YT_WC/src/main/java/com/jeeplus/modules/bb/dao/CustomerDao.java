/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bb.entity.Customer;

/**
 * 微信客户信息DAO接口
 * @author lp
 * @version 2017-02-24
 */
@MyBatisDao
public interface CustomerDao extends CrudDao<Customer> {

	
}