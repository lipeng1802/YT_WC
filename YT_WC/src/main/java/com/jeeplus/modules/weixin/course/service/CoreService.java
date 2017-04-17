package com.jeeplus.modules.weixin.course.service;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

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

	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
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
				//respContent = "您发送的是地理位置消息！";
//				locationX = "34.747190";
//				locationY = "113.625350";
				/*if (null == locationX
						|| locationX.length() <= 0
						|| null == locationY
						|| locationY.length() <= 0) {
					respContent = "获取地理位置有误！";
				} else {
					objSalesModel = WeixinUtil.doMinLocation(Double.parseDouble(locationX), Double.parseDouble(locationY));
					if (null == objSalesModel) {
						respContent = "您发送的是地理位置消息！";
					} else {
						if (null == label || label.length() <= 0) {
							respContent = "您的位置" ;
						} else {
							respContent = "您的位置是" + label;
						}
						respContent += "，经度:" + locationY
								+ "，纬度" + locationX + "，距离您最近的销售是 :" + ""
								+ objSalesModel.getBbsaName() + "\n" + "手机号："
								+ objSalesModel.getBbsaMobile() + "\nQQ号："
								+ objSalesModel.getBbsaQq();
					}}*/
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
					/*
					respContent = "公司总部位于北京亦庄经济技术开发区亦城国际B座18层。主要生产装饰纸、浸渍纸和装饰板。"
							+ "企业自有品牌“三优”在行业内享有很高声誉。2013年投资8000余万在天津宁河成立了天津宏冠装饰材料有限公司。"
							+ "引进领先的自动化生产线，采用天然气能源技术。 <a href=\""
							+ WeixinUtil.project_url + "/aboutus.jsp\">公司详情</a>";
					 */
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
					switch (Integer.parseInt(eventKey)) {
					case 33:
						NewsMessage newsMessage = new NewsMessage();
						// 复制基本属性
						PropertyUtils.copyProperties(newsMessage, textMessage);
						newsMessage.setArticles(WeixinUtil.getNewArticle());
						respMessage = MessageUtil.newsMessageToXml(newsMessage);
						return respMessage;
					default:
						respContent = "开发中，敬请期待!";
						break;
					}
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

//					System.out.println("用户名：" + toUserName + "，openID："
//							+ fromUserName + "，经度:" + longitude + "，纬度"
//							+ latitude + "，精度" + precision);
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
