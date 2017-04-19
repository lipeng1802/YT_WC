package com.jeeplus.modules.weixin.course.service;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bb.service.WechatMenuService;
import com.jeeplus.modules.bb.service.WechatService;
import com.jeeplus.modules.weixin.course.message.response.NewsMessage;
import com.jeeplus.modules.weixin.course.message.response.TextMessage;
import com.jeeplus.modules.weixin.course.util.MessageUtil;
import com.jeeplus.modules.weixin.course.util.WeixinUtil;

/**
 * 核心服务类
 * 
 * @author jdq
 * @date 2014-07-01
 */

public class CoreService {
    @Autowired
    private WechatMenuService menuService;
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public  String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍候尝试！";
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);

			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			// 回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setFuncFlag(0);

			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				respContent = "您发送的是文本消息！";
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "您发送的是图片消息！";
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				// 地理位置纬度
				String locationX = requestMap.get("Location_X");
				// 地理位置经度
				String locationY = requestMap.get("Location_Y");
				String label = requestMap.get("Label");
				respContent = "您发送的是地理位置消息！";
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是音频消息！";
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 事件KEY值
				String eventKey = requestMap.get("EventKey");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					
					respContent = "感谢关注Veron欧派贝隆，『欧派贝隆』板材扎根于华北地区，是天津是欧派商贸有限公司2015年重点打造的项目，品牌自创建之初旨在代表对高品质矢志不渝的崇尚，代表对环保健康永不妥协的信仰，代表对伙伴关系亲密无间的珍视，代表对环保乃至于可持续发展孜孜不倦的追求。公司以天津生产工厂为主要生产基地，面向全国家具行业客户、木质板材经销商、和其他工程类商业组织提供产品和服务。今天，欧派贝隆的产品被广泛的应用于厨房、卫生间、起居室、卧室、办公室、书房、以及其他各种家用及商用领域之中。";
					
					NewsMessage newsMessage = new NewsMessage();
					// 复制基本属性
					PropertyUtils.copyProperties(newsMessage, textMessage);
					newsMessage.setArticles(WeixinUtil.showAboutUs());
					respMessage = MessageUtil.newsMessageToXml(newsMessage);
					return respMessage;
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
				    String remarks = menuService.getMenuBykey(eventKey);
				    if(remarks != null){
				        respContent = remarks;
				    }else{
				        respContent = "开发中，敬请期待!";
				    }
					/*switch (Integer.parseInt(eventKey)) {
					case 32:
						NewsMessage newsMessage = new NewsMessage();
						// 复制基本属性
						PropertyUtils.copyProperties(newsMessage, textMessage);
						newsMessage.setArticles(WeixinUtil.getNewArticle());
						respMessage = MessageUtil.newsMessageToXml(newsMessage);
						return respMessage;
					default:
						respContent = "开发中，敬请期待!";
						break;
					}*/
				}
				// 链接菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_VIEW)) {
					// 链接类不需要回复消息
					return "";
				}
				// 地理位置
				else if (eventType.equals(MessageUtil.EVENT_TYPE_LOCATION)) {
					// 地理位置纬度
					String latitude = requestMap.get("Latitude");
					// 地理位置经度
					String longitude = requestMap.get("Longitude");
					// 地理位置精度
					String precision = requestMap.get("Precision");

					return "";
				}
			}

			textMessage.setContent(respContent);
			respMessage = MessageUtil.textMessageToXml(textMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return respMessage;
	}
}
