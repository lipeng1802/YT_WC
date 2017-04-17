package com.jeeplus.modules.weixin.pojo;

/**
 * 普通按钮（子按钮）
 * 
 * @author jdq
 * @date 2014-07-01
 */
public class ViewButton extends Button {
	private final String type = "view";
	private String url;

	public String getType() {
		return type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
