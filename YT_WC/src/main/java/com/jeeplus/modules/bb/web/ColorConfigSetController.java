package com.jeeplus.modules.bb.web;

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

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bb.entity.Attribute;
import com.jeeplus.modules.bb.service.AttributeService;
import com.jeeplus.modules.bb.web.paramEntity.ColorConfig;
@Controller
@RequestMapping(value = "${adminPath}/bb/ccs")
public class ColorConfigSetController extends BaseController {
    @Autowired
    private AttributeService attributeService;
    

    @RequiresPermissions(value={"bb:colorConfig:add","bb:colorConfig:edit"},logical=Logical.OR)
    @ResponseBody
    @RequestMapping(value = "edit")
    public AjaxJson edit(ColorConfig colorConfig, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
        AjaxJson j = new AjaxJson();
        j.setMsg("修改成功!");
        j.put("data", colorConfig);
        return j;
    }
}
