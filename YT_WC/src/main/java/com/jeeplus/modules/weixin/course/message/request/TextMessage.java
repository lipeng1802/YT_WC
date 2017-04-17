package com.jeeplus.modules.weixin.course.message.request;

public class TextMessage extends BaseMessage {
    // 消息内容  
    private String Content;  
  
    public String getContent() {  
        return Content;  
    }  
  
    public void setContent(String content) {  
        Content = content;  
    }  
}
