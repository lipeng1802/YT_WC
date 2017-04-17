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
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.bb.entity.Attribute;
import com.jeeplus.modules.bb.service.AttributeService;
import com.jeeplus.modules.bb.tools.AttributeType;
import com.jeeplus.modules.test.entity.grid.Category;

/**
 * 产品属性Controller
 * @author lp
 * @version 2017-03-04
 */
@Controller
@RequestMapping(value = "${adminPath}/bb/attribute")
public class AttributeController extends BaseController {

	@Autowired
	private AttributeService attributeService;
	
	@ModelAttribute
	public Attribute get(@RequestParam(required=false) String id) {
		Attribute entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = attributeService.get(id);
		}
		if (entity == null){
			entity = new Attribute();
		}
		return entity;
	}
	
	/**
	 * 产品属性列表页面
	 */
	@RequiresPermissions("bb:attribute:list")
	@RequestMapping(value = {"list", ""})
	public String list(Attribute attribute, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Attribute> page = attributeService.findPage(new Page<Attribute>(request, response), attribute); 
		model.addAttribute("page", page);
		return "modules/bb/attributeList";
	}

	/**
	 * 查看，增加，编辑产品属性表单页面
	 */
	@RequiresPermissions(value={"bb:attribute:view","bb:attribute:add","bb:attribute:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Attribute attribute, Model model) {
		model.addAttribute("attribute", attribute);
		return "modules/bb/attributeForm";
	}

	/**
	 * 保存产品属性
	 */
	@RequiresPermissions(value={"bb:attribute:add","bb:attribute:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Attribute attribute, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, attribute)){
			return form(attribute, model);
		}
		if(!attribute.getIsNewRecord()){//编辑表单保存
			Attribute t = attributeService.get(attribute.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(attribute, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			attributeService.save(t);//保存
		}else{//新增表单保存
			attributeService.save(attribute);//保存
		}
		addMessage(redirectAttributes, "保存产品属性成功");
		return "redirect:"+Global.getAdminPath()+"/bb/attribute/?repage";
	}
	
	/**
	 * 删除产品属性
	 */
	@RequiresPermissions("bb:attribute:del")
	@RequestMapping(value = "delete")
	public String delete(Attribute attribute, RedirectAttributes redirectAttributes) {
		attributeService.delete(attribute);
		addMessage(redirectAttributes, "删除产品属性成功");
		return "redirect:"+Global.getAdminPath()+"/bb/attribute/?repage";
	}
	
	/**
	 * 批量删除产品属性
	 */
	@RequiresPermissions("bb:attribute:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			attributeService.delete(attributeService.get(id));
		}
		addMessage(redirectAttributes, "删除产品属性成功");
		return "redirect:"+Global.getAdminPath()+"/bb/attribute/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bb:attribute:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Attribute attribute, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "产品属性"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Attribute> page = attributeService.findPage(new Page<Attribute>(request, response, -1), attribute);
    		new ExportExcel("产品属性", Attribute.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出产品属性记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/attribute/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bb:attribute:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Attribute> list = ei.getDataList(Attribute.class);
			for (Attribute attribute : list){
				try{
					attributeService.save(attribute);
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
		return "redirect:"+Global.getAdminPath()+"/bb/attribute/?repage";
    }
	
	/**
	 * 下载导入产品属性数据模板
	 */
	@RequiresPermissions("bb:attribute:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "产品属性数据导入模板.xlsx";
    		List<Attribute> list = Lists.newArrayList(); 
    		new ExportExcel("产品属性数据", Attribute.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/attribute/?repage";
    }
	
	   /**
     * 选择所属类型
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "selectattribute")
    public String selectAttribute(Attribute attribute, String url, String fieldLabels, 
            String fieldKeys, String searchLabel, 
            String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException {
        attribute.setType(AttributeType.COLOR);
        Page<Attribute> page = attributeService.findPage(new Page<Attribute>(request, response), attribute);
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
        model.addAttribute("obj", attribute);
        model.addAttribute("page", page);
        return "modules/sys/gridselect";
    }
	

}