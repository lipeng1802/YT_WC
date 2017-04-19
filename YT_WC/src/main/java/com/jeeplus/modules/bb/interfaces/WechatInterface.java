/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.interfaces;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
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
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.bb.entity.Wechat;
import com.jeeplus.modules.bb.service.WechatService;
import com.jeeplus.modules.weixin.course.service.CoreService;

/**
 * 微信信息Controller
 * @author lp
 * @version 2017-02-24
 */
@Controller
@RequestMapping(value = "${adminPath}/if/bb/wechat")
public class WechatInterface extends BaseController {
    private Logger log = Logger.getLogger(WechatInterface.class);
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
     * 
     * @Title accessWechat 
     * @Description 用于用户在微信端连接服务器 
     * @param request
     * @param response
     * @throws Exception       
     * @author Cynara-remix
     * @Date 2016年10月11日 下午3:53:40
     */
    @ResponseBody()
    @RequestMapping(value="/accessWechat",method = {RequestMethod.GET, RequestMethod.POST})
    public void accessWechat(HttpServletRequest request,HttpServletResponse response)throws Exception{
        String token = "veron";
        //设置编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        if (isGet) {
            weChatServerGet(request,response);
        }else {
            weChatServerPost(request,response);
        }
    }
    
    /**
     * 微信服务器Get请求
     * @param request
     * @param response
     */
    private void weChatServerGet(HttpServletRequest request,HttpServletResponse response){
        String token = "veron";
      //微信加密签名
        String signature = request.getParameter("signature");
        //时间戳
        String timestamp = request.getParameter("timestamp");
        //随机数
        String nonce = request.getParameter("nonce");
        //随机字符串
        String echostr = request.getParameter("echostr");
        //将要排序加密的数据放入集合
        List<String> sList = new ArrayList<String>();
        sList.add(token);
        sList.add(timestamp);
        sList.add(nonce);
        //将要sha1加密比对的数据  进行汉字拼音排序
        Collections.sort(sList, new SpellComparator());
        //排序后的数据
        String wxstr = sList.get(0)+sList.get(1)+sList.get(2);
        //比对
        if(DigestUtils.sha1Hex(wxstr).equals(signature.trim())){
            //如果比对成功往微信写echostr
            try {
                response.getWriter().write(echostr);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                log.error("<!----------微信接入失败----------!>");
            }
            log.info("<!----------微信接入服务器成功"+echostr+"----------!>");
        }else{
            log.info("<!----------微信接入服务器失败"+echostr+"----------!>");
        }
    }
    
    /**
     * 微信服务器Post请求
     * @param request
     * @param response
     */
    private void weChatServerPost(HttpServletRequest request,HttpServletResponse response){
        // 调用核心业务类接收消息、处理消息  
        CoreService service=new CoreService();
        String respMessage = service.processRequest(request);  
          
        // 响应消息  
        PrintWriter out;
        try {
            out = response.getWriter();
            out.print(respMessage);  
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("<!----------消息发送失败----------!>");
        }  
        
    }
    
    
    /**
     * 
     * @ClassName SpellComparator 
     * @Description 汉字拼音排序比较器 
     * @author Cynara-remix http://cynara.top
     * E-mail remix7@live.cn 
     * @date 2016年10月11日 下午3:53:22 
     * @version V1.0
     */
    class SpellComparator implements Comparator<Object>{
        public int compare(Object o1, Object o2) {
            try {
                String s1 = new String(o1.toString().getBytes("GB2312"),"ISO-8859-1");
                String s2 = new String(o2.toString().getBytes("GB2312"),"ISO-8859-1");
                return s1.compareTo(s2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
        
    }
	

}