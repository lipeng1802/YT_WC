package com.jeeplus.modules.weixin.course.message.request;

public class ImageMessage extends BaseMessage{
    // 图片链接  
    private String PicUrl;  
  
    public String getPicUrl() {  
        return PicUrl;  
    }  
  
    public void setPicUrl(String picUrl) {  
        PicUrl = picUrl;  
    }  
}
