/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.bb.dao.AttributeDao;
import com.jeeplus.modules.bb.entity.Attribute;
import com.jeeplus.modules.bb.tools.AttributeType;

/**
 * 产品属性Service
 * @author lp
 * @version 2017-03-04
 */
@Service
@Transactional(readOnly = true)
public class AttributeService extends CrudService<AttributeDao, Attribute> {
    @Autowired
    private AttributeDao attributeDao;
    
	public Attribute get(String id) {
		return super.get(id);
	}
	
	public List<Attribute> findList(Attribute attribute) {
		return super.findList(attribute);
	}
	
	public Page<Attribute> findPage(Page<Attribute> page, Attribute attribute) {
		return super.findPage(page, attribute);
	}
	
	@Transactional(readOnly = false)
	public void save(Attribute attribute) {
		super.save(attribute);
	}
	
	@Transactional(readOnly = false)
	public void delete(Attribute attribute) {
		super.delete(attribute);
	}
	
	/**
	 * 属性名查重，根据 name 和 type
	 * @param attribute
	 * @return true 不重复 ； false 重复
	 */
	public boolean checkRepeat(String oldName,Attribute att) {
	    String name=att.getName();
	    if(name != null && name.equals(oldName)){
	        return true;
	    }
	    if(name != null && getByName(att) == null){
	        return true;
	    }
	    return false;
    }
	/**
     * 根据attribute 获得属性列表
     * @param attribute
     * @return
     */
	public List<Attribute> getListByName(Attribute attribute) {
	    return  attributeDao.getAttributeByName(attribute);
    }
	
	/**
	 * 根据attribute 获得属性
	 * @param attribute
	 * @return
	 */
	public Attribute getByName(Attribute attribute){
	    List<Attribute> list=getListByName(attribute);
	    if(list != null && list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
	}
	
	/**
	 * 获得除花色以外的所有属性
	 * @return
	 */
	public List<Attribute> getAttributeNoColor(){
	    List notTypes=new ArrayList();
	    notTypes.add(AttributeType.COLOR);
	    return attributeDao.findListByTypes(null,notTypes);
	}
	
	/**
	 * 根据花色配置，更新花色的配置说明
	 */
	public String getDescByConfig(List<Attribute> list){
	    Map<String,List<Attribute>> map= groupByAttributeType(list);
	    StringBuilder sb = new StringBuilder();
	    for (Map.Entry<String, List<Attribute>> entry : map.entrySet()) {
	        if(entry.getValue().size()>0){
	            try {
                    Field field = AttributeType.class.getField(entry.getKey());
                    try {
                        sb.append(field.get(AttributeType.class).toString()+"：");
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
    	        for(Attribute a : entry.getValue()){
    	            sb.append(a.getName()+",");
    	        }
    	        sb.append("；");
	        }
	    }
	    return sb.toString();
	    
	}
	
	
	   /**
     * 根据属性类型分类成不同的List
     * @param list
     * @return
     */
    public Map<String,List<Attribute>> groupByAttributeType(List<Attribute> list){
        Map<String,List<Attribute>> m=new HashMap();
        Field[] fields = AttributeType.class.getDeclaredFields();
      //  Iterator<Attribute> it=list.iterator();
        for (Field field : fields) {
            List<Attribute> l=new ArrayList();
            
            Iterator<Attribute> it=list.iterator();
            //去除数组中"a"的元素
            while(it.hasNext()){
                Attribute a=(Attribute)it.next();
                try {
                    if(a.getType().equals(field.get(AttributeType.class).toString())){
                        l.add(a);
                        it.remove();
                    }
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            m.put(field.getName(), l);
        }
        return m;
    }
	
    public List<Attribute> findProductAttributeList(String productId){
        return this.attributeDao.findProductAttributeList(productId);
    }
}