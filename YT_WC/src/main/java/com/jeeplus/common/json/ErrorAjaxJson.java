package com.jeeplus.common.json;

public class ErrorAjaxJson extends AjaxJson{
    
    public ErrorAjaxJson(){
        super.setSuccess(false);
    }
    public ErrorAjaxJson(String msg,String errorCode){
        super.setSuccess(false);
        super.setErrorCode(errorCode);
        super.setMsg(msg);
    }
}
