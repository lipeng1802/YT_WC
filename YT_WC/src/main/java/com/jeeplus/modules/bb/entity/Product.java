/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.entity;


import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Menu;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 产品信息Entity
 * @author lp
 * @version 2017-02-24
 */
public class Product extends DataEntity<Product> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 拼装名称
	private String style;       // 规格型号
	private String reserve01;       // 预留1
	private String reserve02;       // 预留2
	private String relationName;		// 关联其它系统名称
	private List<Attribute> attributeList = Lists.newArrayList(); // 按明细设置数据范围
	
	public Product() {
		super();
	}

	public Product(String id){
		super(id);
	}

	@ExcelField(title="拼装名称", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="关联其它系统名称", align=2, sort=2)
	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	@ExcelField(title="规格型号", align=2, sort=3)
    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    @ExcelField(title="其它属性01", align=2, sort=4)
    public String getReserve01() {
        return reserve01;
    }

    public void setReserve01(String reserve01) {
        this.reserve01 = reserve01;
    }
    @ExcelField(title="其它属性02", align=2, sort=5)
    public String getReserve02() {
        return reserve02;
    }

    public void setReserve02(String reserve02) {
        this.reserve02 = reserve02;
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }
    
    public List<String> getAttributeIdList() {
        List<String> attributeIdList = Lists.newArrayList();
        for (Attribute a : attributeList) {
            attributeIdList.add(a.getId());
        }
        return attributeIdList;
    }

    public void setAttributeIdList(List<String> attributeIdList) {
        attributeList = Lists.newArrayList();
        for (String menuId : attributeIdList) {
            Attribute menu = new Attribute();
            menu.setId(menuId);
            attributeList.add(menu);
        }
    }

    public String getAttributeIds() {
        return StringUtils.join(getAttributeIdList(), ",");
    }
    
    public void setAttributeIds(String attributeIds) {
        attributeList = Lists.newArrayList();
        if (attributeIds != null){
            String[] ids = StringUtils.split(attributeIds, ",");
            setAttributeIdList(Lists.newArrayList(ids));
        }
    }
    

	
}