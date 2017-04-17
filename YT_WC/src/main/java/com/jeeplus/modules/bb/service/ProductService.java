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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.common.service.ServiceException;
import com.jeeplus.modules.bb.dao.ProductDao;
import com.jeeplus.modules.bb.dao.RefProductAttributeDao;
import com.jeeplus.modules.bb.entity.Attribute;
import com.jeeplus.modules.bb.entity.Product;
import com.jeeplus.modules.bb.tools.AttributeType;
import com.jeeplus.modules.iim.dao.MailBoxDao;
import com.jeeplus.modules.iim.entity.MailBox;

/**
 * 产品信息Service
 * @author lp
 * @version 2017-02-24
 */
@Service
@Transactional(readOnly = true)
public class ProductService extends CrudService<ProductDao, Product> {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private RefProductAttributeDao productAttributeDao;
	public Product get(String id) {
		return productDao.get(id);
	}
	
	public List<Product> findList(Product product) {
		return super.findList(product);
	}
	
	public Page<Product> findPage(Page<Product> page, Product product) {
		return super.findPage(page, product);
	}
	
	@Transactional(readOnly = false)
	public void save(Product product) {
		super.save(product);
	}
	
	@Transactional(readOnly = false)
	public void delete(Product product) {
		super.delete(product);
	}
	
	public Page<Product> findProductAndAttributeList2(Page<Product> page, Product entity){
	    entity.setPage(page);
        page.setList(productDao.findProductAndAttributeList2(entity));
        return page;
	}
	
	/**
	 * 根据产品ID获得产品及属性
	 **/
	
	public Product getProductAttribute(Product product){
	    return this.productDao.findProductAttribute(product).get(0);
	}
	
	/**
	 * 根据产品ID获得产品的所有属性
	 */
	public List<Attribute> findProductAttributeList(String productId){
	    return attributeService.findProductAttributeList(productId);
	}
	
	/**
	 * 判断产品是否添加重复
	 */
	public void findRepeatByAttribute(Product product){
	    List<Attribute> list=product.getAttributeList();
	    List<String> attributeIds=new ArrayList();
	    for(Attribute a : list){
	        attributeIds.add(a.getId());
	    }
	    
	    Integer counts=productDao.findRepeatByAttribute(attributeIds, attributeIds.size(),product.getId());
	    if(counts != null && counts != 0){
	        throw new ServiceException("操作失败：产品设置重复，已有相同的产品！！！！！！");
	    }
	}
	
	
	/**
	 * 根据产品属性拼装产品名称
	 */
	public Product bulidpProductName(Product product){
	    List<Attribute> list=product.getAttributeList();
	    if(list == null || list.size() == 0){
	        return product;
	    }
	    Map<String,Object> map=bulidProductName(list);
	    product.setName((String)map.get("name"));
	    Map<String,Object> attMap=(Map<String,Object>)map.get("map");
	    Attribute style=(Attribute)attMap.get(AttributeType.STYLE);
	    product.setStyle(style.getName());
	    return product;
	}
	
	/**
	 * 组装名称 产品名称格式：主纸/副纸-厚度-板材-板等级-钢板-单双面
	 * @param list
	 * @return null 属性不全，必备属性：花色、厚度、板材、等级、单双面、钢板
	 */
	public Map<String,Object> bulidProductName(List<Attribute> list){
	    String split = "-";
	    Map<String,Object> returnMap=new HashMap();
	    Map<String,Object> map = ProductAttributeList2Map(list);
	    List<Attribute> colors=(List<Attribute>)map.get(AttributeType.COLOR);
	    Attribute plate=(Attribute)map.get(AttributeType.PLATE);
	    Attribute grade=(Attribute)map.get(AttributeType.GRADE);
	    Attribute thick=(Attribute)map.get(AttributeType.THICK);
	    Attribute veneer=(Attribute)map.get(AttributeType.VENEER);
	    Attribute grain=(Attribute)map.get(AttributeType.GRAIN);
	    returnMap.put("map", map);
	    
	    if(colors == null || plate==null || thick==null || grade==null || veneer ==null || grain==null){
	        return returnMap;
	    }
	    
	    StringBuilder sb=new StringBuilder();
	    if(colors != null && colors.size()>0){
	        for(int i=0;i<colors.size();i++){
	            if(i==1){
	                sb.append("/");
	            }
	            sb.append(colors.get(i).getName());
	        }
	        sb.append(split);
	    }
	    if(thick != null){
            sb.append(thick.getName());
        }
	    if(plate != null){
            sb.append(plate.getName());
        }
	    if(grade != null){
            sb.append(grade.getName());
            sb.append(split);
        }
	    if(veneer != null){
            sb.append(veneer.getName());
        }
	    if(grain != null){
            sb.append(grain.getName());
        }
	    String returnValue=sb.toString();
	    if(split.equals(returnValue.substring(returnValue.length()-1,returnValue.length()))){
	        returnValue=returnValue.substring(0,returnValue.length()-1);
	    }
	    returnMap.put("name", returnValue);
	    
	    return returnMap;
	}
	
	
	/**
	 * 将产品属性list转换成map,如果是花色有可能是数组
	 */
	public Map<String,Object> ProductAttributeList2Map(List<Attribute> list){
	    Map<String,Object> m=new HashMap();
        Field[] fields = AttributeType.class.getDeclaredFields();
        List<Attribute> colors=new ArrayList<Attribute>();
        for (Field field : fields) {
            Iterator<Attribute> it=list.iterator();
            String value=null;
            try {
                value = field.get(AttributeType.class).toString();
            } catch (IllegalArgumentException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            //去除数组中"a"的元素
            while(it.hasNext()){
                Attribute a=(Attribute)it.next();
                if(!StringUtils.isNotBlank(a.getName())){
                    a=attributeService.get(a.getId());
                }
                try {
                    if(a.getType().equals(value)){
                        if(AttributeType.COLOR.equals(a.getType())){
                            colors.add(a);
                        }else{
                            m.put(value, a);
                        }
                        //it.remove();
                    }
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } 
            }
            if(AttributeType.COLOR.equals(value)){
                m.put(value, colors);
            }
        }
        return m;
	    
	}
	
	
}