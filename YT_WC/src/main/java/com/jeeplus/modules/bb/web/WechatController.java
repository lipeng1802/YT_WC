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
import com.jeeplus.modules.bb.entity.Wechat;
import com.jeeplus.modules.bb.service.WechatService;

/**
 * 微信信息Controller
 * @author lp
 * @version 2017-02-24
 */
@Controller
@RequestMapping(value = "${adminPath}/bb/wechat")
public class WechatController extends BaseController {

	@Autowired
	private WechatService wechatService;
	
	@ModelAttribute
	public Wechat get(@RequestParam(required=false) String id) {
		Wechat entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = wechatService.get(id);
		}
		if (entity == null){
			entity = new Wechat();
		}
		return entity;
	}
	
	/**
	 * 微信信息列表页面
	 */
	@RequiresPermissions("bb:wechat:list")
	@RequestMapping(value = {"list", ""})
	public String list(Wechat wechat, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Wechat> page = wechatService.findPage(new Page<Wechat>(request, response), wechat); 
		model.addAttribute("page", page);
		return "modules/bb/wechatList";
	}

	/**
	 * 查看，增加，编辑微信信息表单页面
	 */
	@RequiresPermissions(value={"bb:wechat:view","bb:wechat:add","bb:wechat:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Wechat wechat, Model model) {
		model.addAttribute("wechat", wechat);
		return "modules/bb/wechatForm";
	}

	/**
	 * 保存微信信息
	 */
	@RequiresPermissions(value={"bb:wechat:add","bb:wechat:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Wechat wechat, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, wechat)){
			return form(wechat, model);
		}
		if(!wechat.getIsNewRecord()){//编辑表单保存
			Wechat t = wechatService.get(wechat.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(wechat, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			wechatService.save(t);//保存
		}else{//新增表单保存
			wechatService.save(wechat);//保存
		}
		addMessage(redirectAttributes, "保存微信信息成功");
		return "redirect:"+Global.getAdminPath()+"/bb/wechat/?repage";
	}
	
	/**
	 * 删除微信信息
	 */
	@RequiresPermissions("bb:wechat:del")
	@RequestMapping(value = "delete")
	public String delete(Wechat wechat, RedirectAttributes redirectAttributes) {
		wechatService.delete(wechat);
		addMessage(redirectAttributes, "删除微信信息成功");
		return "redirect:"+Global.getAdminPath()+"/bb/wechat/?repage";
	}
	
	/**
	 * 批量删除微信信息
	 */
	@RequiresPermissions("bb:wechat:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			wechatService.delete(wechatService.get(id));
		}
		addMessage(redirectAttributes, "删除微信信息成功");
		return "redirect:"+Global.getAdminPath()+"/bb/wechat/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bb:wechat:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Wechat wechat, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "微信信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Wechat> page = wechatService.findPage(new Page<Wechat>(request, response, -1), wechat);
    		new ExportExcel("微信信息", Wechat.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出微信信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/wechat/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bb:wechat:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Wechat> list = ei.getDataList(Wechat.class);
			for (Wechat wechat : list){
				try{
					wechatService.save(wechat);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条微信信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条微信信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入微信信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/wechat/?repage";
    }
	
	/**
	 * 下载导入微信信息数据模板
	 */
	@RequiresPermissions("bb:wechat:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "微信信息数据导入模板.xlsx";
    		List<Wechat> list = Lists.newArrayList(); 
    		new ExportExcel("微信信息数据", Wechat.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/wechat/?repage";
    }
	
	
	

}