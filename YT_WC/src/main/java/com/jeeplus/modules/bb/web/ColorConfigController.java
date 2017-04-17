/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.web;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.json.ErrorAjaxJson;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bb.entity.Attribute;
import com.jeeplus.modules.bb.service.AttributeService;
import com.jeeplus.modules.bb.service.RefAttributeAttributeService;
import com.jeeplus.modules.bb.tools.AttributeType;
import com.jeeplus.modules.bb.web.paramEntity.ColorConfig;

/**
 * 产品属性Controller
 * @author lp
 * @version 2017-03-04
 */
@Controller
@RequestMapping(value = "${adminPath}/bb/colorConfig")
public class ColorConfigController extends BaseController {

	@Autowired
	private AttributeService attributeService;
	@Autowired
	private RefAttributeAttributeService refAttributeAttributeService;
	
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
	 * 花色列表
	 */
	@RequiresPermissions("bb:colorConfig:list")
    @RequestMapping(value = {"colorList",""})
    public String colorList(Attribute attribute, HttpServletRequest request, HttpServletResponse response, Model model,String selectId) {
	    attribute.setType(AttributeType.COLOR);
	    Page<Attribute> page = attributeService.findPage(new Page<Attribute>(request, response), attribute); 
        model.addAttribute("page", page);
        
        if(StringUtils.isNoneBlank(selectId)){
            model.addAttribute("attr", attributeService.get(selectId));
            model.addAttribute("colorConfig", new ColorConfig());
            //得到除花色外的所有属性，并分组
            Map<String,List<Attribute>> attrMap = 
                    attributeService.groupByAttributeType(attributeService.getAttributeNoColor());
            //得到该花色的所有配置属性
            List<Attribute> configAttrs =
            refAttributeAttributeService.findListByAttribute(new Attribute(selectId));
            for (Map.Entry<String, List<Attribute>> entry : attrMap.entrySet()) {
                List<Attribute> list=entry.getValue();
                //循环判断是否已经选中，选中将sort字段赋值成checked
                for(Attribute a:list){
                    if(configAttrs.contains(a)){
                        a.setSort("checked");
                    }
                }
                model.addAttribute(entry.getKey(), list);
            }
        }
        return "modules/bb/colorConfig";
    }
	

	
	
	 /**
     * 删除产品属性
     */
    @RequiresPermissions("bb:colorConfig:del")
    @RequestMapping(value = "delete")
    public String delete(Attribute attribute, RedirectAttributes redirectAttributes) {
        attributeService.delete(attribute);
        addMessage(redirectAttributes, "删除花色成功");
        return "redirect:"+Global.getAdminPath()+"/bb/colorConfig/?repage";
    }
	
    /**
     * 批量删除产品属性
     */
    @RequiresPermissions("bb:colorConfig:del")
    @RequestMapping(value = "deleteAll")
    public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
        String idArray[] =ids.split(",");
        for(String id : idArray){
            attributeService.delete(attributeService.get(id));
        }
        addMessage(redirectAttributes, "删除产品属性成功");
        return "redirect:"+Global.getAdminPath()+"/bb/colorConfig/?repage";
    }
    
	/**
	 * 查看，增加，编辑产品属性表单页面
	 */
	@RequiresPermissions(value={"bb:colorConfig:add","bb:colorConfig:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Attribute attribute, Model model,String type) {
	    if(StringUtils.isNoneBlank(type)){
	        attribute.setType(type);
	    }
		model.addAttribute("attribute", attribute);
		//model.addAttribute("selectId", selectId);
		return "modules/bb/attributeFormToType";
	}

	/**
	 * 保存产品属性
	 */
	@RequiresPermissions(value={"bb:colorConfig:add","bb:colorConfig:edit"},logical=Logical.OR)
	@ResponseBody
    @RequestMapping(value = "save")
	public AjaxJson save(Attribute attribute, Model model, HttpServletRequest request, HttpServletResponse response){
	    if(!attributeService.checkRepeat(null,attribute)){
	        return new ErrorAjaxJson(attribute.getType()+"名称重复","A0");
	    }
		attributeService.save(attribute);//保存
		AjaxJson j = new AjaxJson();
		j.setMsg("添加成功!");
        j.put("data", attribute);
        return j;
		
	}
	
	/**
	 * 修改产品属性
	 * @throws Exception 
	 */
	@RequiresPermissions(value={"bb:colorConfig:add","bb:colorConfig:edit"},logical=Logical.OR)
	@ResponseBody
    @RequestMapping(value = "edit")
	public AjaxJson edit(ColorConfig colorConfig, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
       /* Attribute t = attributeService.get(attribute.getId());//从数据库取出记录的值
        MyBeanUtils.copyBeanNotNull2Bean(attribute, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
        attributeService.save(t);//保存*/
        AjaxJson j = new AjaxJson();
        j.setMsg("修改成功!");
        j.put("data", colorConfig);
        return j;
	}
	
    @ResponseBody
    @RequestMapping(value = "saveConfig")
    public AjaxJson saveConfig(@RequestBody ColorConfig colorConfig, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
        //修改attribute
        Attribute t = attributeService.get(colorConfig.getAttribute().getId());
        MyBeanUtils.copyBeanNotNull2Bean(colorConfig.getAttribute(), t);        
        if(!attributeService.checkRepeat(attributeService.get(colorConfig.getAttribute().getId()).getName()
                ,colorConfig.getAttribute())){
            return new ErrorAjaxJson(t.getType()+"名称重复","A0");
        }
        //保存属性配置
        refAttributeAttributeService.saveConfig(colorConfig);
        //为配置属性赋值
        t.setNameDesc(attributeService.getDescByConfig(colorConfig.getAttrList()));
        attributeService.save(t);
        
        AjaxJson j = new AjaxJson();
        j.setMsg("配置成功!");
        return j;
    }

}