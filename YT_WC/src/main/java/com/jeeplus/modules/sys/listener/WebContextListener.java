package com.jeeplus.modules.sys.listener;

import javax.servlet.ServletContext;

import org.apache.poi.hssf.util.HSSFColor.GOLD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;

import com.jeeplus.common.config.Global;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.weixin.course.util.TokenThread;
import com.jeeplus.modules.weixin.course.util.WeixinUtil;

public class WebContextListener extends org.springframework.web.context.ContextLoaderListener {
    private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);
	@Override
	public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
		if (!SystemService.printKeyLoadMessage()){
			return null;
		}
		TokenThread.appid =  Global.getConfig("appid");
        // 第三方用户唯一凭证密钥
        TokenThread.appsecret = Global.getConfig("appsecret");

        log.info("weixin api appid:{}", TokenThread.appid);
        log.info("weixin api appsecret:{}", TokenThread.appsecret);

        // 未配置appid、appsecret时给出提示
        if ("".equals(TokenThread.appid) || "".equals(TokenThread.appsecret)) {
            log.error("appid and appsecret configuration error, please check carefully.");
        } else {
            // 启动定时获取access_token的线程
            new Thread(new TokenThread()).start();
        }
		
		return super.initWebApplicationContext(servletContext);
	}
}
