package com.jeeplus.modules.bb.web.paramEntity;

import java.util.List;

import com.jeeplus.modules.bb.entity.Attribute;

public class ColorConfig {
    private Attribute attribute;
    
    private List<Attribute> attrList;

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public List<Attribute> getAttrList() {
        return attrList;
    }

    public void setAttrList(List<Attribute> attrList) {
        this.attrList = attrList;
    }



}
