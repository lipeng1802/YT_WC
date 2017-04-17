package com.jeeplus.modules.weixin.course.message.response;

import java.util.ArrayList;
import java.util.List;

import com.jeeplus.modules.weixin.course.message.response.pojo.Article;
import com.jeeplus.modules.weixin.course.util.MessageUtil;

/**
 * 图文消息
 * 
 * @author jdq
 *
 */
public class NewsMessage extends BaseMessage {
	// 消息类型
	private final String MsgType = MessageUtil.RESP_MESSAGE_TYPE_NEWS;
	// 图文消息个数，限制为10条以内
	private int ArticleCount;
	// 多条图文消息信息，默认第一个item为大图
	private List<Article> Articles;

	public String getMsgType() {
		return MsgType;
	}

	public int getArticleCount() {
		return ArticleCount;
	}

	private void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		// 图文消息最大数量10个，否则无响应
		if (null == articles) {
			articles = new ArrayList<Article>();
		} else if (articles.size() > 10) {
			articles.subList(10, articles.size()).clear();
		}
		this.setArticleCount(articles.size());
		Articles = articles;
	}
}