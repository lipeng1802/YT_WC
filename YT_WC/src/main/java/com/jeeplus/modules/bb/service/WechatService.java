/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bb.entity.Wechat;
import com.jeeplus.modules.bb.dao.WechatDao;

/**
 * 微信信息Service
 * @author lp
 * @version 2017-02-24
 */
@Service
@Transactional(readOnly = true)
public class WechatService extends CrudService<WechatDao, Wechat> {

	public Wechat get(String id) {
		return super.get(id);
	}
	
	public List<Wechat> findList(Wechat wechat) {
		return super.findList(wechat);
	}
	
	public Page<Wechat> findPage(Page<Wechat> page, Wechat wechat) {
		return super.findPage(page, wechat);
	}
	
	@Transactional(readOnly = false)
	public void save(Wechat wechat) {
		super.save(wechat);
	}
	
	@Transactional(readOnly = false)
	public void delete(Wechat wechat) {
		super.delete(wechat);
	}
	
	
	
	
}