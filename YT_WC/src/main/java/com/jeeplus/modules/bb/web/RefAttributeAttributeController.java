/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.web;

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
import com.jeeplus.modules.bb.entity.RefAttributeAttribute;
import com.jeeplus.modules.bb.service.RefAttributeAttributeService;

/**
 * 花色配置Controller
 * @author lp
 * @version 2017-03-04
 */
@Controller
@RequestMapping(value = "${adminPath}/bb/refAttributeAttribute")
public class RefAttributeAttributeController extends BaseController {

	@Autowired
	private RefAttributeAttributeService refAttributeAttributeService;
	
	@ModelAttribute
	public RefAttributeAttribute get(@RequestParam(required=false) String id) {
		RefAttributeAttribute entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = refAttributeAttributeService.get(id);
		}
		if (entity == null){
			entity = new RefAttributeAttribute();
		}
		return entity;
	}
	
	
	
	
	
	/**
	 * 花色配置列表页面
	 */
	@RequiresPermissions("bb:refAttributeAttribute:list")
	@RequestMapping(value = {"list", ""})
	public String list(RefAttributeAttribute refAttributeAttribute, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RefAttributeAttribute> page = refAttributeAttributeService.findPage(new Page<RefAttributeAttribute>(request, response), refAttributeAttribute); 
		model.addAttribute("page", page);
		return "modules/bb/refAttributeAttributeList";
	}

	/**
	 * 查看，增加，编辑花色配置表单页面
	 */
	@RequiresPermissions(value={"bb:refAttributeAttribute:view","bb:refAttributeAttribute:add","bb:refAttributeAttribute:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(RefAttributeAttribute refAttributeAttribute, Model model) {
		model.addAttribute("refAttributeAttribute", refAttributeAttribute);
		return "modules/bb/refAttributeAttributeForm";
	}

	/**
	 * 保存花色配置
	 */
	@RequiresPermissions(value={"bb:refAttributeAttribute:add","bb:refAttributeAttribute:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(RefAttributeAttribute refAttributeAttribute, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, refAttributeAttribute)){
			return form(refAttributeAttribute, model);
		}
		if(!refAttributeAttribute.getIsNewRecord()){//编辑表单保存
			RefAttributeAttribute t = refAttributeAttributeService.get(refAttributeAttribute.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(refAttributeAttribute, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			refAttributeAttributeService.save(t);//保存
		}else{//新增表单保存
			refAttributeAttributeService.save(refAttributeAttribute);//保存
		}
		addMessage(redirectAttributes, "保存花色配置成功");
		return "redirect:"+Global.getAdminPath()+"/bb/refAttributeAttribute/?repage";
	}
	
	/**
	 * 删除花色配置
	 */
	@RequiresPermissions("bb:refAttributeAttribute:del")
	@RequestMapping(value = "delete")
	public String delete(RefAttributeAttribute refAttributeAttribute, RedirectAttributes redirectAttributes) {
		refAttributeAttributeService.delete(refAttributeAttribute);
		addMessage(redirectAttributes, "删除花色配置成功");
		return "redirect:"+Global.getAdminPath()+"/bb/refAttributeAttribute/?repage";
	}
	
	/**
	 * 批量删除花色配置
	 */
	@RequiresPermissions("bb:refAttributeAttribute:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			refAttributeAttributeService.delete(refAttributeAttributeService.get(id));
		}
		addMessage(redirectAttributes, "删除花色配置成功");
		return "redirect:"+Global.getAdminPath()+"/bb/refAttributeAttribute/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bb:refAttributeAttribute:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(RefAttributeAttribute refAttributeAttribute, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "花色配置"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<RefAttributeAttribute> page = refAttributeAttributeService.findPage(new Page<RefAttributeAttribute>(request, response, -1), refAttributeAttribute);
    		new ExportExcel("花色配置", RefAttributeAttribute.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出花色配置记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/refAttributeAttribute/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bb:refAttributeAttribute:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<RefAttributeAttribute> list = ei.getDataList(RefAttributeAttribute.class);
			for (RefAttributeAttribute refAttributeAttribute : list){
				try{
					refAttributeAttributeService.save(refAttributeAttribute);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条花色配置记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条花色配置记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入花色配置失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/refAttributeAttribute/?repage";
    }
	
	/**
	 * 下载导入花色配置数据模板
	 */
	@RequiresPermissions("bb:refAttributeAttribute:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "花色配置数据导入模板.xlsx";
    		List<RefAttributeAttribute> list = Lists.newArrayList(); 
    		new ExportExcel("花色配置数据", RefAttributeAttribute.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/refAttributeAttribute/?repage";
    }
	
	
	

}