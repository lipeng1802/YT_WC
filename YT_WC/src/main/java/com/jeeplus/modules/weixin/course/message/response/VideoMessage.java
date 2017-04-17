package com.jeeplus.modules.weixin.course.message.response;

import com.jeeplus.modules.weixin.course.message.response.pojo.Video;
import com.jeeplus.modules.weixin.course.util.MessageUtil;

/**
 * 视频消息
 * 
 * @author jdq
 *
 */
public class VideoMessage extends BaseMessage {
	// 消息类型
	private final String MsgType = MessageUtil.RESP_MESSAGE_TYPE_VOICE;
	// 视频
	private Video Video;

	public String getMsgType() {
		return MsgType;
	}

	public Video getVideo() {
		return Video;
	}

	public void setVideo(Video video) {
		Video = video;
	}

}
