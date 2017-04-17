package com.jeeplus.modules.weixin.course.message.response;

import com.jeeplus.modules.weixin.course.message.response.pojo.Voice;
import com.jeeplus.modules.weixin.course.util.MessageUtil;

/**
 * 语音消息
 * 
 * @author jdq
 *
 */
public class VoiceMessage extends BaseMessage {
	// 消息类型
	private final String MsgType = MessageUtil.RESP_MESSAGE_TYPE_VIDEO;
	// 语音
	private Voice Voice;

	public String getMsgType() {
		return MsgType;
	}

	public Voice getVoice() {
		return Voice;
	}

	public void setVoice(Voice voice) {
		Voice = voice;
	}

}
