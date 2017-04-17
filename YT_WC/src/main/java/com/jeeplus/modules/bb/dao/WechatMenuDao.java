/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.dao;

import com.jeeplus.common.persistence.TreeDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.bb.entity.WechatMenu;

/**
 * 微信菜单DAO接口
 * @author lp
 * @version 2017-04-12
 */
@MyBatisDao
public interface WechatMenuDao extends TreeDao<WechatMenu> {
	
}