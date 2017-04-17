package com.jeeplus.modules.weixin.course.message.response;

import com.jeeplus.modules.weixin.course.util.MessageUtil;

/**
 * 文本消息
 * 
 * @author jdq
 *
 */
public class TextMessage extends BaseMessage {
	// 消息类型
	private final String MsgType = MessageUtil.RESP_MESSAGE_TYPE_TEXT;
	// 回复的消息内容
	private String Content;

	public String getMsgType() {
		return MsgType;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
