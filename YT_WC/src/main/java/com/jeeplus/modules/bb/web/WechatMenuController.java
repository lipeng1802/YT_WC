/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.bb.entity.WechatMenu;
import com.jeeplus.modules.bb.service.WechatMenuService;

/**
 * 微信菜单Controller
 * @author lp
 * @version 2017-04-12
 */
@Controller
@RequestMapping(value = "${adminPath}/bb/wechatMenu")
public class WechatMenuController extends BaseController {

	@Autowired
	private WechatMenuService wechatMenuService;
	
	@ModelAttribute
	public WechatMenu get(@RequestParam(required=false) String id) {
		WechatMenu entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wechatMenuService.get(id);
		}
		if (entity == null){
			entity = new WechatMenu();
		}
		return entity;
	}
	
	/**
	 * 微信菜单列表页面
	 */
	@RequiresPermissions("bb:wechatMenu:list")
	@RequestMapping(value = {"list", ""})
	public String list(WechatMenu wechatMenu, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<WechatMenu> list = wechatMenuService.findList(wechatMenu); 
		model.addAttribute("list", list);
		return "modules/bb/wechatMenuList";
	}

	/**
	 * 查看，增加，编辑微信菜单表单页面
	 */
	@RequiresPermissions(value={"bb:wechatMenu:view","bb:wechatMenu:add","bb:wechatMenu:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(WechatMenu wechatMenu, Model model) {
		if (wechatMenu.getParent()!=null && StringUtils.isNotBlank(wechatMenu.getParent().getId())){
			wechatMenu.setParent(wechatMenuService.get(wechatMenu.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(wechatMenu.getId())){
				WechatMenu wechatMenuChild = new WechatMenu();
				wechatMenuChild.setParent(new WechatMenu(wechatMenu.getParent().getId()));
				List<WechatMenu> list = wechatMenuService.findList(wechatMenu); 
				if (list.size() > 0){
					wechatMenu.setSort(list.get(list.size()-1).getSort());
					if (wechatMenu.getSort() != null){
						wechatMenu.setSort(wechatMenu.getSort() + 30);
					}
				}
			}
		}
		if (wechatMenu.getSort() == null){
			wechatMenu.setSort(30);
		}
		model.addAttribute("wechatMenu", wechatMenu);
		return "modules/bb/wechatMenuForm";
	}

	/**
	 * 保存微信菜单
	 */
	@RequiresPermissions(value={"bb:wechatMenu:add","bb:wechatMenu:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(WechatMenu wechatMenu, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, wechatMenu)){
			return form(wechatMenu, model);
		}
		if(!wechatMenu.getIsNewRecord()){//编辑表单保存
			WechatMenu t = wechatMenuService.get(wechatMenu.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(wechatMenu, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			wechatMenuService.save(t);//保存
		}else{//新增表单保存
			wechatMenuService.save(wechatMenu);//保存
		}
		addMessage(redirectAttributes, "保存微信菜单成功");
		return "redirect:"+Global.getAdminPath()+"/bb/wechatMenu/?repage";
	}
	
	/**
	 * 删除微信菜单
	 */
	@RequiresPermissions("bb:wechatMenu:del")
	@RequestMapping(value = "delete")
	public String delete(WechatMenu wechatMenu, RedirectAttributes redirectAttributes) {
		wechatMenuService.delete(wechatMenu);
		addMessage(redirectAttributes, "删除微信菜单成功");
		return "redirect:"+Global.getAdminPath()+"/bb/wechatMenu/?repage";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<WechatMenu> list = wechatMenuService.findList(new WechatMenu());
		for (int i=0; i<list.size(); i++){
			WechatMenu e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
}