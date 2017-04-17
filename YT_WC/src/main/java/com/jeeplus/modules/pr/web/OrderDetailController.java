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
import com.jeeplus.modules.pr.entity.OrderDetail;
import com.jeeplus.modules.pr.service.OrderDetailService;

/**
 * 订单明细Controller
 * @author lp
 * @version 2017-03-31
 */
@Controller
@RequestMapping(value = "${adminPath}/pr/orderDetail")
public class OrderDetailController extends BaseController {

	@Autowired
	private OrderDetailService orderDetailService;
	
	@ModelAttribute
	public OrderDetail get(@RequestParam(required=false) String id) {
		OrderDetail entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = orderDetailService.get(id);
		}
		if (entity == null){
			entity = new OrderDetail();
		}
		return entity;
	}
	
	/**
	 * 订单明细列表页面
	 */
	@RequiresPermissions("pr:orderDetail:list")
	@RequestMapping(value = {"list", ""})
	public String list(OrderDetail orderDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OrderDetail> page = orderDetailService.findPage(new Page<OrderDetail>(request, response), orderDetail); 
		model.addAttribute("page", page);
		return "modules/pr/orderDetailList";
	}

	/**
	 * 查看，增加，编辑订单明细表单页面
	 */
	@RequiresPermissions(value={"pr:orderDetail:view","pr:orderDetail:add","pr:orderDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(OrderDetail orderDetail, Model model) {
		model.addAttribute("orderDetail", orderDetail);
		return "modules/pr/orderDetailForm";
	}

	/**
	 * 保存订单明细
	 */
	@RequiresPermissions(value={"pr:orderDetail:add","pr:orderDetail:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(OrderDetail orderDetail, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, orderDetail)){
			return form(orderDetail, model);
		}
		if(!orderDetail.getIsNewRecord()){//编辑表单保存
			OrderDetail t = orderDetailService.get(orderDetail.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(orderDetail, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			orderDetailService.save(t);//保存
		}else{//新增表单保存
			orderDetailService.save(orderDetail);//保存
		}
		addMessage(redirectAttributes, "保存订单明细成功");
		return "redirect:"+Global.getAdminPath()+"/pr/orderDetail/?repage";
	}
	
	/**
	 * 删除订单明细
	 */
	@RequiresPermissions("pr:orderDetail:del")
	@RequestMapping(value = "delete")
	public String delete(OrderDetail orderDetail, RedirectAttributes redirectAttributes) {
		orderDetailService.delete(orderDetail);
		addMessage(redirectAttributes, "删除订单明细成功");
		return "redirect:"+Global.getAdminPath()+"/pr/orderDetail/?repage";
	}
	
	/**
	 * 批量删除订单明细
	 */
	@RequiresPermissions("pr:orderDetail:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			orderDetailService.delete(orderDetailService.get(id));
		}
		addMessage(redirectAttributes, "删除订单明细成功");
		return "redirect:"+Global.getAdminPath()+"/pr/orderDetail/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("pr:orderDetail:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(OrderDetail orderDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "订单明细"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<OrderDetail> page = orderDetailService.findPage(new Page<OrderDetail>(request, response, -1), orderDetail);
    		new ExportExcel("订单明细", OrderDetail.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出订单明细记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pr/orderDetail/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("pr:orderDetail:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<OrderDetail> list = ei.getDataList(OrderDetail.class);
			for (OrderDetail orderDetail : list){
				try{
					orderDetailService.save(orderDetail);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条订单明细记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条订单明细记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入订单明细失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pr/orderDetail/?repage";
    }
	
	/**
	 * 下载导入订单明细数据模板
	 */
	@RequiresPermissions("pr:orderDetail:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "订单明细数据导入模板.xlsx";
    		List<OrderDetail> list = Lists.newArrayList(); 
    		new ExportExcel("订单明细数据", OrderDetail.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pr/orderDetail/?repage";
    }
	
	
	

}