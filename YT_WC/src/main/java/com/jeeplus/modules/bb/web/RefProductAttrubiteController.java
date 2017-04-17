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
import com.jeeplus.modules.bb.entity.RefProductAttrubite;
import com.jeeplus.modules.bb.service.RefProductAttributeService;

/**
 * 产品属性Controller
 * @author lp
 * @version 2017-03-23
 */
@Controller
@RequestMapping(value = "${adminPath}/bb/refProductAttrubite")
public class RefProductAttrubiteController extends BaseController {

	@Autowired
	private RefProductAttributeService refProductAttrubiteService;
	
	@ModelAttribute
	public RefProductAttrubite get(@RequestParam(required=false) String id) {
		RefProductAttrubite entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = refProductAttrubiteService.get(id);
		}
		if (entity == null){
			entity = new RefProductAttrubite();
		}
		return entity;
	}
	
	/**
	 * 产品属性列表页面
	 */
	@RequiresPermissions("bb:refProductAttrubite:list")
	@RequestMapping(value = {"list", ""})
	public String list(RefProductAttrubite refProductAttrubite, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RefProductAttrubite> page = refProductAttrubiteService.findPage(new Page<RefProductAttrubite>(request, response), refProductAttrubite); 
		model.addAttribute("page", page);
		return "modules/bb/refProductAttrubiteList";
	}

	/**
	 * 查看，增加，编辑产品属性表单页面
	 */
	@RequiresPermissions(value={"bb:refProductAttrubite:view","bb:refProductAttrubite:add","bb:refProductAttrubite:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(RefProductAttrubite refProductAttrubite, Model model) {
		model.addAttribute("refProductAttrubite", refProductAttrubite);
		return "modules/bb/refProductAttrubiteForm";
	}

	/**
	 * 保存产品属性
	 */
	@RequiresPermissions(value={"bb:refProductAttrubite:add","bb:refProductAttrubite:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(RefProductAttrubite refProductAttrubite, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, refProductAttrubite)){
			return form(refProductAttrubite, model);
		}
		if(!refProductAttrubite.getIsNewRecord()){//编辑表单保存
			RefProductAttrubite t = refProductAttrubiteService.get(refProductAttrubite.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(refProductAttrubite, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			refProductAttrubiteService.save(t);//保存
		}else{//新增表单保存
			refProductAttrubiteService.save(refProductAttrubite);//保存
		}
		addMessage(redirectAttributes, "保存产品属性成功");
		return "redirect:"+Global.getAdminPath()+"/bb/refProductAttrubite/?repage";
	}
	
	/**
	 * 删除产品属性
	 */
	@RequiresPermissions("bb:refProductAttrubite:del")
	@RequestMapping(value = "delete")
	public String delete(RefProductAttrubite refProductAttrubite, RedirectAttributes redirectAttributes) {
		refProductAttrubiteService.delete(refProductAttrubite);
		addMessage(redirectAttributes, "删除产品属性成功");
		return "redirect:"+Global.getAdminPath()+"/bb/refProductAttrubite/?repage";
	}
	
	/**
	 * 批量删除产品属性
	 */
	@RequiresPermissions("bb:refProductAttrubite:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			refProductAttrubiteService.delete(refProductAttrubiteService.get(id));
		}
		addMessage(redirectAttributes, "删除产品属性成功");
		return "redirect:"+Global.getAdminPath()+"/bb/refProductAttrubite/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bb:refProductAttrubite:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(RefProductAttrubite refProductAttrubite, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "产品属性"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<RefProductAttrubite> page = refProductAttrubiteService.findPage(new Page<RefProductAttrubite>(request, response, -1), refProductAttrubite);
    		new ExportExcel("产品属性", RefProductAttrubite.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出产品属性记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/refProductAttrubite/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bb:refProductAttrubite:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<RefProductAttrubite> list = ei.getDataList(RefProductAttrubite.class);
			for (RefProductAttrubite refProductAttrubite : list){
				try{
					refProductAttrubiteService.save(refProductAttrubite);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条产品属性记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条产品属性记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入产品属性失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/refProductAttrubite/?repage";
    }
	
	/**
	 * 下载导入产品属性数据模板
	 */
	@RequiresPermissions("bb:refProductAttrubite:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "产品属性数据导入模板.xlsx";
    		List<RefProductAttrubite> list = Lists.newArrayList(); 
    		new ExportExcel("产品属性数据", RefProductAttrubite.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/refProductAttrubite/?repage";
    }
	
	
	

}