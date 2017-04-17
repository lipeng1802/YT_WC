/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.pr.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.pr.entity.OrderCost;
import com.jeeplus.modules.pr.service.OrderCostService;

/**
 * 订单费用Controller
 * @author lp
 * @version 2017-03-31
 */
@Controller
@RequestMapping(value = "${adminPath}/pr/orderCost")
public class OrderCostController extends BaseController {

	@Autowired
	private OrderCostService orderCostService;
	
	@ModelAttribute
	public OrderCost get(@RequestParam(required=false) String id) {
		OrderCost entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = orderCostService.get(id);
		}
		if (entity == null){
			entity = new OrderCost();
		}
		return entity;
	}
	
	/**
	 * 订单费用列表页面
	 */
	@RequiresPermissions("pr:orderCost:list")
	@RequestMapping(value = {"list", ""})
	public String list(OrderCost orderCost, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OrderCost> page = orderCostService.findPage(new Page<OrderCost>(request, response), orderCost); 
		model.addAttribute("page", page);
		return "modules/pr/orderCostList";
	}

	/**
	 * 查看，增加，编辑订单费用表单页面
	 */
	@RequiresPermissions(value={"pr:orderCost:view","pr:orderCost:add","pr:orderCost:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(OrderCost orderCost, Model model) {
		model.addAttribute("orderCost", orderCost);
		return "modules/pr/orderCostForm";
	}

	/**
	 * 保存订单费用
	 */
	@RequiresPermissions(value={"pr:orderCost:add","pr:orderCost:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(OrderCost orderCost, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, orderCost)){
			return form(orderCost, model);
		}
		if(!orderCost.getIsNewRecord()){//编辑表单保存
			OrderCost t = orderCostService.get(orderCost.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(orderCost, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			orderCostService.save(t);//保存
		}else{//新增表单保存
			orderCostService.save(orderCost);//保存
		}
		addMessage(redirectAttributes, "保存订单费用成功");
		return "redirect:"+Global.getAdminPath()+"/pr/orderCost/?repage";
	}
	
	/**
	 * 删除订单费用
	 */
	@RequiresPermissions("pr:orderCost:del")
	@RequestMapping(value = "delete")
	public String delete(OrderCost orderCost, RedirectAttributes redirectAttributes) {
		orderCostService.delete(orderCost);
		addMessage(redirectAttributes, "删除订单费用成功");
		return "redirect:"+Global.getAdminPath()+"/pr/orderCost/?repage";
	}
	
	/**
	 * 批量删除订单费用
	 */
	@RequiresPermissions("pr:orderCost:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			orderCostService.delete(orderCostService.get(id));
		}
		addMessage(redirectAttributes, "删除订单费用成功");
		return "redirect:"+Global.getAdminPath()+"/pr/orderCost/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("pr:orderCost:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(OrderCost orderCost, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "订单费用"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<OrderCost> page = orderCostService.findPage(new Page<OrderCost>(request, response, -1), orderCost);
    		new ExportExcel("订单费用", OrderCost.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出订单费用记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pr/orderCost/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("pr:orderCost:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<OrderCost> list = ei.getDataList(OrderCost.class);
			for (OrderCost orderCost : list){
				try{
					orderCostService.save(orderCost);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条订单费用记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条订单费用记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入订单费用失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pr/orderCost/?repage";
    }
	
	/**
	 * 下载导入订单费用数据模板
	 */
	@RequiresPermissions("pr:orderCost:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "订单费用数据导入模板.xlsx";
    		List<OrderCost> list = Lists.newArrayList(); 
    		new ExportExcel("订单费用数据", OrderCost.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pr/orderCost/?repage";
    }
	
	
	

}