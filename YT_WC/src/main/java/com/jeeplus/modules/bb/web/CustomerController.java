/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bb.entity.Attribute;
import com.jeeplus.modules.bb.entity.Customer;
import com.jeeplus.modules.bb.entity.Wechat;
import com.jeeplus.modules.bb.service.CustomerService;
import com.jeeplus.modules.bb.service.WechatService;
import com.jeeplus.modules.bb.tools.AttributeType;
import com.jeeplus.modules.weixin.course.util.WeixinUtil;

/**
 * 微信客户信息Controller
 * @author lp
 * @version 2017-02-24
 */
@Controller
@RequestMapping(value = "${adminPath}/bb/customer")
public class CustomerController extends BaseController {

	@Autowired
	private CustomerService customerService;
	@Autowired
    private WechatService weChatService;
	@ModelAttribute
	public Customer get(@RequestParam(required=false) String id) {
		Customer entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = customerService.get(id);
		}
		if (entity == null){
			entity = new Customer();
		}
		return entity;
	}
	
	/**
	 * 微信客户信息列表页面
	 */
	@RequiresPermissions("bb:customer:list")
	@RequestMapping(value = {"list", ""})
	public String list(Customer customer, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Customer> page = customerService.findPage(new Page<Customer>(request, response), customer); 
		model.addAttribute("page", page);
	      System.out.println(WeixinUtil.project_url);
	        System.out.println(WeixinUtil.img_url);
		return "modules/bb/customerList";
	}

	/**
	 * 查看，增加，编辑微信客户信息表单页面
	 */
	@RequiresPermissions(value={"bb:customer:view","bb:customer:add","bb:customer:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Customer customer, Model model) {
		model.addAttribute("customer", customer);
		return "modules/bb/customerForm";
	}

	/**
	 * 保存微信客户信息
	 */
	@RequiresPermissions(value={"bb:customer:add","bb:customer:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Customer customer, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, customer)){
			return form(customer, model);
		}
		
		if(!customer.getWeChat().getIsNewRecord()){//编辑表单保存
            Wechat t = weChatService.get(customer.getWeChat().getId());//从数据库取出记录的值
            MyBeanUtils.copyBeanNotNull2Bean(customer.getWeChat(), t);//将编辑表单中的非NULL值覆盖数据库记录中的值
            weChatService.save(t);//保存
        }else{//新增表单保存
            weChatService.save(customer.getWeChat());//保存
            
        }
		customer.setWechatId(customer.getWeChat().getId());
		if(!customer.getIsNewRecord()){//编辑表单保存
			Customer t = customerService.get(customer.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(customer, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			customerService.save(t);//保存
		}else{//新增表单保存
		    
			customerService.save(customer);//保存
		}
		
		addMessage(redirectAttributes, "保存微信客户信息成功");
		return "redirect:"+Global.getAdminPath()+"/bb/customer/?repage";
	}
	
	/**
	 * 删除微信客户信息
	 */
	@RequiresPermissions("bb:customer:del")
	@RequestMapping(value = "delete")
	public String delete(Customer customer, RedirectAttributes redirectAttributes) {
		customerService.delete(customer);
		addMessage(redirectAttributes, "删除微信客户信息成功");
		return "redirect:"+Global.getAdminPath()+"/bb/customer/?repage";
	}
	
	/**
	 * 批量删除微信客户信息
	 */
	@RequiresPermissions("bb:customer:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			customerService.delete(customerService.get(id));
		}
		addMessage(redirectAttributes, "删除微信客户信息成功");
		return "redirect:"+Global.getAdminPath()+"/bb/customer/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bb:customer:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Customer customer, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "微信客户信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Customer> page = customerService.findPage(new Page<Customer>(request, response, -1), customer);
    		new ExportExcel("微信客户信息", Customer.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出微信客户信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/customer/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bb:customer:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Customer> list = ei.getDataList(Customer.class);
			for (Customer customer : list){
				try{
					customerService.save(customer);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条微信客户信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条微信客户信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入微信客户信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/customer/?repage";
    }
	
	/**
	 * 下载导入微信客户信息数据模板
	 */
	@RequiresPermissions("bb:customer:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "微信客户信息数据导入模板.xlsx";
    		List<Customer> list = Lists.newArrayList(); 
    		new ExportExcel("微信客户信息数据", Customer.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/customer/?repage";
    }
	
    /**
   * 选择所属类型
   * @throws UnsupportedEncodingException 
   */
  @RequestMapping(value = "selectCustomer")
  public String selectCustomer(Customer customer, String url, String fieldLabels, 
          String fieldKeys, String searchLabel, 
          String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException {
      Page<Customer> page = customerService.findPage(new Page<Customer>(request, response), customer);
      try {
          fieldLabels = URLDecoder.decode(fieldLabels, "UTF-8");
          fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
          searchLabel = URLDecoder.decode(searchLabel, "UTF-8");
          searchKey = URLDecoder.decode(searchKey, "UTF-8");
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }
      model.addAttribute("labelNames", fieldLabels.split("\\|"));
      model.addAttribute("labelValues", fieldKeys.split("\\|"));
      model.addAttribute("fieldLabels", fieldLabels);
      model.addAttribute("fieldKeys", fieldKeys);
      model.addAttribute("url", url);
      model.addAttribute("searchLabel", searchLabel);
      model.addAttribute("searchKey", searchKey);
      model.addAttribute("obj", customer);
      model.addAttribute("page", page);
      return "modules/sys/gridselect";
  }
	

}