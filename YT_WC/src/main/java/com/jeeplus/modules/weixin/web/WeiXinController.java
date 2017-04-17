/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weixin.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.json.ErrorAjaxJson;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bb.entity.WechatMenu;
import com.jeeplus.modules.bb.service.WechatMenuService;
import com.jeeplus.modules.weixin.pojo.Button;
import com.jeeplus.modules.weixin.pojo.Menu;
import com.jeeplus.modules.weixin.course.util.WeixinUtil;
import com.jeeplus.modules.weixin.pojo.CommonButton;
import com.jeeplus.modules.weixin.pojo.ComplexButton;
import com.jeeplus.modules.weixin.pojo.ViewButton;
import com.jeeplus.modules.weixin.course.util.TokenThread;

/**
 * 微信信息Controller
 * @author lp
 * @version 2017-02-24
 */
@Controller
@RequestMapping(value = "${adminPath}/if/wx/menu")
public class WeiXinController extends BaseController {

    @Autowired
    private WechatMenuService wechatMenuService;
	
	/**
	 * 微信信息列表页面
	 */
    @ResponseBody
	@RequestMapping(value = {"createMenu"})
	public AjaxJson createMenu() {
		Menu menu=new Menu();
		List<Button> lstBtn = new ArrayList<Button>();
		Button[] arrButton;
		WechatMenu wcMenu;
		List<WechatMenu> listMenu;
		
		/**查出所有的一级菜单**/
		WechatMenu m1 = new WechatMenu();
		m1.setParent(new WechatMenu("1"));
		listMenu = wechatMenuService.findList(m1);
		for(WechatMenu m : listMenu){
		    lstBtn.add(this.buildComplexButton(m));
		}
		arrButton = new Button[lstBtn.size()];
		menu.setButton(lstBtn.toArray(arrButton));
		int n=WeixinUtil.createMenu(menu, TokenThread.accessToken.getToken());
		if(n==0){
    		AjaxJson j = new AjaxJson();
            j.setMsg("添加成功!");
            return j;
		}else{
		    AjaxJson j = new ErrorAjaxJson("创建失败,失败代码"+n,"A002");
		    return j;
		}
        
	}
    
    /**组件二级菜单**/
    private ComplexButton buildComplexButton(WechatMenu menu) {
        ComplexButton objComplexBtn = new ComplexButton(); // 菜单按钮
        ViewButton objViewBtn; // 链接按钮
        CommonButton objCommonBtn; // 点击按钮
        List<Button> lstBtn = new ArrayList<Button>();
        WechatMenu objMenu;
        List<WechatMenu> listMenu;
        Button[] arrButton;

      
        objMenu = new WechatMenu();
        objMenu.setParent(menu);
        listMenu = wechatMenuService.findList(objMenu);
        if (null == listMenu || listMenu.size() <= 0) {
            listMenu = new ArrayList<WechatMenu>();
        }
        for (int n = 0; n < listMenu.size(); n++) {
            objMenu = listMenu.get(n);
            if (null == objMenu
                    || null == objMenu.getType()
                    || objMenu.getType().length() <= 0
                    || null == objMenu.getName()
                    || objMenu.getName().length() <= 0) {
                continue;
            }
            if ("view".equals(objMenu.getType())) {
                objViewBtn = new ViewButton();
                objViewBtn.setName(objMenu.getName());
                objViewBtn.setUrl(WeixinUtil.project_url + objMenu.getUrl());
                lstBtn.add(objViewBtn);
            } else if ("click".equals(objMenu.getType())) {
                objCommonBtn = new CommonButton();
                objCommonBtn.setName(objMenu.getName());
                objCommonBtn.setKey(objMenu.getKey());
                lstBtn.add(objCommonBtn);
            }
        }
        objComplexBtn.setName(menu.getName());
        arrButton = new Button[lstBtn.size()];
        objComplexBtn.setSub_button(lstBtn.toArray(arrButton));
        return objComplexBtn;
    }
}