/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bb.entity.WechatMenu;
import com.jeeplus.modules.bb.dao.WechatMenuDao;

/**
 * 微信菜单Service
 * @author lp
 * @version 2017-04-12
 */
@Service
@Transactional(readOnly = true)
public class WechatMenuService extends TreeService<WechatMenuDao, WechatMenu> {

	public WechatMenu get(String id) {
		return super.get(id);
	}
	
	public List<WechatMenu> findList(WechatMenu wechatMenu) {
		if (StringUtils.isNotBlank(wechatMenu.getParentIds())){
			wechatMenu.setParentIds(","+wechatMenu.getParentIds()+",");
		}
		return super.findList(wechatMenu);
	}
	
	@Transactional(readOnly = false)
	public void save(WechatMenu wechatMenu) {
		super.save(wechatMenu);
	}
	
	@Transactional(readOnly = false)
	public void delete(WechatMenu wechatMenu) {
		super.delete(wechatMenu);
	}
	
}