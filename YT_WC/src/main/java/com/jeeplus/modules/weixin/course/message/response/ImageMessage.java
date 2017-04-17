package com.jeeplus.modules.weixin.course.message.response;

import com.jeeplus.modules.weixin.course.message.response.pojo.Image;
import com.jeeplus.modules.weixin.course.util.MessageUtil;

/**
 * 图片消息
 * 
 * @author jdq
 *
 */
public class ImageMessage extends BaseMessage {
	// 消息类型
	private final String MsgType = MessageUtil.RESP_MESSAGE_TYPE_IMAGE;
	// 图片
	private Image Image;

	public String getMsgType() {
		return MsgType;
	}

	public Image getImage() {
		return Image;
	}

	public void setImage(Image image) {
		Image = image;
	}

}
