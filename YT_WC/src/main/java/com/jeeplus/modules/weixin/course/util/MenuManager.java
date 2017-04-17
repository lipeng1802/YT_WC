package com.jeeplus.modules.weixin.course.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeeplus.modules.weixin.pojo.AccessToken;
import com.jeeplus.modules.weixin.pojo.Button;
import com.jeeplus.modules.weixin.pojo.CommonButton;
import com.jeeplus.modules.weixin.pojo.ComplexButton;
import com.jeeplus.modules.weixin.pojo.Menu;
import com.jeeplus.modules.weixin.pojo.ViewButton;

/**
 * 菜单管理器类
 * 
 * @author jdq
 * @date 2014-07-01
 */
public class MenuManager {
	private static Logger log = LoggerFactory.getLogger(MenuManager.class);

	public static void main(String[] args) {
		// 第三方用户唯一凭证
		String appId = "aaaa";
		// 第三方用户唯一凭证密钥
		String appSecret = "bbb";

		// 调用接口获取access_token
		AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

		TokenThread.accessToken = at;
		if (null != at) {
			// 调用接口创建菜单
			int result = WeixinUtil.createMenu(getMenu(), at.getToken());

			// 判断菜单创建结果
			if (0 == result)
				log.info("菜单创建成功！");
			else
				log.info("菜单创建失败，错误码：" + result);
		}
	}

	/**
	 * 组装菜单数据
	 * 
	 * @return
	 */
	private static Menu getMenu() {
		String url = "http://221.123.163.10/WeiXinServices/";

		ViewButton btn11 = new ViewButton();
		btn11.setName("关于我们");
		btn11.setUrl(url + "aboutus.jsp");

		ViewButton btn12 = new ViewButton();
		btn12.setName("荣誉殿堂");
		btn12.setUrl(url + "honor.jsp");

		CommonButton btn13 = new CommonButton();
		btn13.setName("合作伙伴");
		btn13.setKey("13");

		ViewButton btn14 = new ViewButton();
		btn14.setName("联系我们");
		btn14.setUrl(url + "link.jsp");

		ViewButton btn15 = new ViewButton();
		btn15.setName("联系销售");
		btn15.setUrl(url + "sales.jsp");

		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("公司简介");
		mainBtn1.setSub_button(new Button[] { btn11, btn12, btn13, btn14, btn15 });

		ViewButton btn21 = new ViewButton();
		btn21.setName("高油");
		btn21.setUrl(url + "show.jsp?id=1");

		ViewButton btn22 = new ViewButton();
		btn22.setName("普通");
		btn22.setUrl(url + "show.jsp?id=2");

		ViewButton btn23 = new ViewButton();
		btn23.setName("高珠");
		btn23.setUrl(url + "show.jsp?id=3");

		ViewButton btn24 = new ViewButton();
		btn24.setName("油珠");
		btn24.setUrl(url + "show.jsp?id=4");

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("产品展示");
		mainBtn2.setSub_button(new Button[] { btn21, btn22, btn23, btn24 });

		CommonButton btn31 = new CommonButton();
		btn31.setName("公告");
		btn31.setKey("31");

		CommonButton btn32 = new CommonButton();
		btn32.setName("新闻");
		btn32.setKey("32");

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("公告");
		mainBtn3.setSub_button(new Button[] { btn31, btn32 });

		/**
		 * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项<br>
		 * 
		 * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
		 * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br>
		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
		 */
		Menu menu = new Menu();
		menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });

		return menu;
	}
}