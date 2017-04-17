package com.jeeplus.modules.weixin.course.message.response;

import com.jeeplus.modules.weixin.course.message.response.pojo.Music;
import com.jeeplus.modules.weixin.course.util.MessageUtil;

/**
 * 音乐消息
 * 
 * @author jdq
 *
 */
public class MusicMessage extends BaseMessage {
	// 消息类型
	private final String MsgType = MessageUtil.RESP_MESSAGE_TYPE_MUSIC;
	// 音乐
	private Music Music;

	public String getMsgType() {
		return MsgType;
	}

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}
}
