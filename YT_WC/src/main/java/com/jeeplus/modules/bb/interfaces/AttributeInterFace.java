/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.interfaces;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.json.ErrorAjaxJson;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.bb.entity.Attribute;
import com.jeeplus.modules.bb.service.AttributeService;
import com.jeeplus.modules.bb.tools.AttributeType;
import com.jeeplus.modules.pr.entity.Order;
import com.jeeplus.modules.test.entity.grid.Category;

/**
 * 产品属性Controller
 * @author lp
 * @version 2017-03-04
 */
@Controller
@RequestMapping(value = "${adminPath}/if/bb/attribute")
public class AttributeInterFace extends BaseController {

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
	@ResponseBody
	@RequestMapping(value = {"colorList", ""})
	public AjaxJson colorList(Attribute attribute, HttpServletRequest request, HttpServletResponse response) {
	    Page<Attribute> page=null;
		try {
		    attribute.setType("花色");
	        page = attributeService.findPage(new Page<Attribute>(request, response), attribute); 
        } catch (Exception e) {
            return new ErrorAjaxJson("操作失败","A0");
        }
        AjaxJson j = new AjaxJson();
        j.put("data", page);
        j.setMsg("操作成功!");
        return j;
		
	}


	

}