package com.jeeplus.modules.weixin.course.message.push;

import com.jeeplus.modules.weixin.course.message.push.pojo.Filter;
import com.jeeplus.modules.weixin.course.message.push.pojo.Mpnews;

/**
 * 推送的图文消息
 * 
 * @author jdq
 * 
 */
public class NewsMessage extends Message {
	private Filter filter;
	private Mpnews mpnews;
	private final String msgtype = Message.PUSH_MESSAGE_TYPE_NEWS;

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public Mpnews getMpnews() {
		return mpnews;
	}

	public void setMpnews(Mpnews mpnews) {
		this.mpnews = mpnews;
	}

	public String getMsgtype() {
		return msgtype;
	}

}