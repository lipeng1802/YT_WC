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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.json.ErrorAjaxJson;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bb.service.ProductService;
import com.jeeplus.modules.pr.entity.Order;
import com.jeeplus.modules.pr.entity.OrderCost;
import com.jeeplus.modules.pr.entity.OrderDetail;
import com.jeeplus.modules.pr.service.OrderCostService;
import com.jeeplus.modules.pr.service.OrderDetailService;
import com.jeeplus.modules.pr.service.OrderService;

/**
 * 订单Controller
 * @author lp
 * @version 2017-03-31
 */
@Controller
@RequestMapping(value = "${adminPath}/pr/order")
public class OrderController extends BaseController {

	@Autowired
	private OrderService orderService;
	@Autowired
    private ProductService productService;
	@Autowired
    private OrderDetailService orderDetailService;
	@Autowired
    private OrderCostService orderCostService;
    
	
	@ModelAttribute
	public Order get(@RequestParam(required=false) String id) {
		Order entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = orderService.get(id);
		}
		if (entity == null){
			entity = new Order();
		}
		return entity;
	}
	
	/**
	 * 订单列表页面
	 */
	@RequiresPermissions("pr:order:list")
	@RequestMapping(value = {"list", ""})
	public String list(Order order, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Order> page = orderService.findPage(new Page<Order>(request, response), order); 
		model.addAttribute("page", page);
		return "modules/pr/orderList";
	}

	/**
	 * 查看订单明细
	 */
	@RequestMapping(value = "orderDetail")
    public String orderDetail(Order order, HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("order", order);
        OrderDetail detail=new OrderDetail();
        detail.setOrder(order);
        List<OrderDetail> details=orderDetailService.findByOrder(detail);
        for(OrderDetail o : details){
                o.getProduct().setAttributeList(productService.findProductAttributeList(o.getProduct().getId()));
        }
        
        model.addAttribute("details",details);
        
        //费用明细
        OrderCost cost = new OrderCost();
        cost.setOrder(order);
        model.addAttribute("costs",orderCostService.findList(cost));
        
        return "modules/pr/orderDetail";
    }
	/**
	 * 查看，增加，编辑订单表单页面
	 */
	@RequiresPermissions(value={"pr:order:view","pr:order:add","pr:order:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Order order, Model model) {
		model.addAttribute("order", order);
		return "modules/pr/orderForm";
	}
	
	
	

	/**
	 * 保存订单
	 */
	@RequiresPermissions(value={"pr:order:add","pr:order:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Order order, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, order)){
			return form(order, model);
		}
		if(!order.getIsNewRecord()){//编辑表单保存
			Order t = orderService.get(order.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(order, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			orderService.save(t);//保存
		}else{//新增表单保存
			orderService.save(order);//保存
		}
		addMessage(redirectAttributes, "保存订单成功");
		return "redirect:"+Global.getAdminPath()+"/pr/order/?repage";
	}
	
	/**
	 * 删除订单
	 */
	@RequiresPermissions("pr:order:del")
	@RequestMapping(value = "delete")
	public String delete(Order order, RedirectAttributes redirectAttributes) {
		orderService.delete(order);
		addMessage(redirectAttributes, "删除订单成功");
		return "redirect:"+Global.getAdminPath()+"/pr/order/?repage";
	}
	
	/**
	 * 批量删除订单
	 */
	@RequiresPermissions("pr:order:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			orderService.delete(orderService.get(id));
			//删除订单明细
			OrderDetail detail=new OrderDetail();
			detail.setOrder(new Order(id));
			orderDetailService.delete(detail);
			//删除费用明细
			OrderCost cost=new OrderCost();
			cost.setOrder(new Order(id));
            orderCostService.delete(cost);
		}
		addMessage(redirectAttributes, "删除订单成功");
		return "redirect:"+Global.getAdminPath()+"/pr/order/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("pr:order:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Order order, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "订单"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Order> page = orderService.findPage(new Page<Order>(request, response, -1), order);
    		new ExportExcel("订单", Order.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出订单记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pr/order/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("pr:order:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Order> list = ei.getDataList(Order.class);
			for (Order order : list){
				try{
					orderService.save(order);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条订单记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条订单记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入订单失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pr/order/?repage";
    }
	
	/**
	 * 下载导入订单数据模板
	 */
	@RequiresPermissions("pr:order:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "订单数据导入模板.xlsx";
    		List<Order> list = Lists.newArrayList(); 
    		new ExportExcel("订单数据", Order.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/pr/order/?repage";
    }
	
	
	/****************************************************************/
	
	@RequiresPermissions(value={"pr:order:fukuan"})
    @ResponseBody
    @RequestMapping(value = "yiFuKuan")
    public AjaxJson yiFuKuan(@RequestParam String ids){
        try {
            String idArray[] =ids.split(",");
            for(String id : idArray){
                Order order=orderService.get(id);
                order.setState("已付款");
                orderService.save(order);
            }
            //不发送微信消息——
            
        } catch (Exception e) {
            return new ErrorAjaxJson("操作失败","A0");
        }
        AjaxJson j = new AjaxJson();
        j.setMsg("操作成功!");
        return j;
        
    }
	
	@RequiresPermissions(value={"pr:order:shengchan"})
    @ResponseBody
    @RequestMapping(value = "yiShengChan")
    public AjaxJson yiShengChan(@RequestParam String ids){
	    try {
	        String idArray[] =ids.split(",");
            for(String id : idArray){
                Order order=orderService.get(id);
                order.setState("已生产");
                orderService.save(order);
                
                //发送微信消息——
            }
        } catch (Exception e) {
            return new ErrorAjaxJson("操作失败","A0");
        }
        AjaxJson j = new AjaxJson();
        j.setMsg("操作成功!");
        return j;
        
    }
	@RequiresPermissions(value={"pr:order:fahuo"})
    @ResponseBody
    @RequestMapping(value = "yiFaHuo")
    public AjaxJson yiFaHuo(@RequestParam String ids){
        try {
            
            String idArray[] =ids.split(",");
            for(String id : idArray){
                Order order=orderService.get(id);
                order.setState("已发货");
                orderService.save(order);
              //发送微信消息——
            }
            
        } catch (Exception e) {
            return new ErrorAjaxJson("操作失败","A0");
        }
        AjaxJson j = new AjaxJson();
        j.setMsg("操作成功!");
        return j;
        
    }
	@RequiresPermissions(value={"pr:order:wancheng"})
    @ResponseBody
    @RequestMapping(value = "wanCheng")
    public AjaxJson wanCheng(@RequestParam String ids){
        try {
           
            String idArray[] =ids.split(",");
            for(String id : idArray){
                Order order=orderService.get(id);
                order.setState("完成");
                orderService.save(order);
                //发送微信消息——
            }
           
            
        } catch (Exception e) {
            return new ErrorAjaxJson("操作失败","A0");
        }
        AjaxJson j = new AjaxJson();
        j.setMsg("操作成功!");
        return j;
        
    }
	@RequiresPermissions(value={"pr:order:cancle"})
    @ResponseBody
    @RequestMapping(value = "cancle")
    public AjaxJson cancle(@RequestParam String ids){
        try {
           
            String idArray[] =ids.split(",");
            for(String id : idArray){
                Order order=orderService.get(id);
                order.setState("注销");
                orderService.save(order);
              //发送微信消息——
            }
            
            
            
        } catch (Exception e) {
            return new ErrorAjaxJson("注销失败","A0");
        }
        AjaxJson j = new AjaxJson();
        j.setMsg("操作成功!");
        return j;
        
    }
	
	

}