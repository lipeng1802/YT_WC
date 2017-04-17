package com.jeeplus.modules.weixin.pojo;

/**
 * 普通按钮（子按钮）
 * 
 * @author jdq
 * @date 2014-07-01
 */
public class CommonButton extends Button {
	private final String type = "click";
	private String key;

	public String getType() {
		return type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
