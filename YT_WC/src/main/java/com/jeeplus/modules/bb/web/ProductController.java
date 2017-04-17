/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.bb.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.ServiceException;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.bb.entity.Attribute;
import com.jeeplus.modules.bb.entity.Product;
import com.jeeplus.modules.bb.service.AttributeService;
import com.jeeplus.modules.bb.service.ProductService;
import com.jeeplus.modules.bb.service.RefProductAttributeService;
import com.jeeplus.modules.bb.tools.AttributeType;

/**
 * 产品信息Controller
 * @author lp
 * @version 2017-02-24
 */
@Controller
@RequestMapping(value = "${adminPath}/bb/product")
public class ProductController extends BaseController {
    @Autowired
    private AttributeService attributeService;
	@Autowired
	private ProductService productService;
   @Autowired
    private RefProductAttributeService productAttributeService;
	@ModelAttribute
	public Product get(@RequestParam(required=false) String id) {
		Product entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = productService.get(id);
		}
		if (entity == null){
			entity = new Product();
		}
		return entity;
	}
	
	/**
	 * 产品信息列表页面
	 */
	@RequiresPermissions("bb:product:list")
	@RequestMapping(value = {"list", ""})
	public String list(Product product, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Product> page = productService.findProductAndAttributeList2(new Page<Product>(request, response), product);
		
		for(Product p : page.getList()){
		    p.setAttributeList(productService.findProductAttributeList(p.getId()));
		}
		Attribute searchColor=null;
		if(StringUtils.isNotBlank(product.getReserve01())){
		    searchColor = attributeService.get(product.getReserve01());
		}
		model.addAttribute("searchColor", searchColor);
		model.addAttribute("page", page);
		return "modules/bb/productList";
	}

	/**
	 * 查看，增加，编辑产品信息表单页面
	 */
	@RequiresPermissions(value={"bb:product:view","bb:product:add","bb:product:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Product product, Model model) {
		model.addAttribute("product", product);
        //得到除花色外的所有属性，并分组
        Map<String,List<Attribute>> attrMap = 
                attributeService.groupByAttributeType(attributeService.getAttributeNoColor());
        List<Attribute> productAttrs = null;
        if(!product.getIsNewRecord()){
            productAttrs = productService.getProductAttribute(product).getAttributeList();
            Attribute color=null;
            int i = 0;
            for(Attribute a : productAttrs){
                //花色 可能有两种，分别是 主纸 和 副纸
                if(AttributeType.COLOR.equals( a.getType())){
                    model.addAttribute(i==0?"huase":"fuzhi", a);
                    i++;
                    if(i==2)break;
                }
            }
            
        }
        for (Map.Entry<String, List<Attribute>> entry : attrMap.entrySet()) {
            List<Attribute> list=entry.getValue();
            //循环判断是否已经选中，选中将sort字段赋值成checked
            if(productAttrs!=null && productAttrs.size()>0){
                for(Attribute a:list){
                    if(productAttrs.contains(a)){
                        a.setSort("checked");
                    }
                }
            }
            model.addAttribute(entry.getKey(), list);
        }
		return "modules/bb/productForm";
	}

	/**
	 * 保存产品信息
	 */
	@RequiresPermissions(value={"bb:product:add","bb:product:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Product product, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, product)){
			return form(product, model);
		}
		//产品查重
		try {
            productService.findRepeatByAttribute(product);
        } catch (ServiceException e) {
            addMessage(redirectAttributes, e.getMessage());
            return "redirect:"+Global.getAdminPath()+"/bb/product/?repage";
        }
		//为产品名称赋值
		product=productService.bulidpProductName(product);
		if(product.getName().equals(null)){
		    addMessage(redirectAttributes, "添加产品失败，名称缺少属性，必备属性：花色、厚度、板材、等级、单双面、钢板");
		    
		    return "redirect:"+Global.getAdminPath()+"/bb/product/?repage";
		}else{
    		if(!product.getIsNewRecord()){//编辑表单保存
    			Product t = productService.get(product.getId());//从数据库取出记录的值
    			MyBeanUtils.copyBeanNotNull2Bean(product, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
    			productService.save(t);//保存
    		}else{//新增表单保存
    			productService.save(product);//保存
    		}
		}
		productAttributeService.saveByProduct(product);
		
		addMessage(redirectAttributes, "保存产品信息成功");
		return "redirect:"+Global.getAdminPath()+"/bb/product/?repage";
	}
	
	/**
	 * 删除产品信息
	 */
	@RequiresPermissions("bb:product:del")
	@RequestMapping(value = "delete")
	public String delete(Product product, RedirectAttributes redirectAttributes) {
		productService.delete(product);
		addMessage(redirectAttributes, "删除产品信息成功");
		return "redirect:"+Global.getAdminPath()+"/bb/product/?repage";
	}
	
	/**
	 * 批量删除产品信息
	 */
	@RequiresPermissions("bb:product:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			productService.delete(productService.get(id));
		}
		addMessage(redirectAttributes, "删除产品信息成功");
		return "redirect:"+Global.getAdminPath()+"/bb/product/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("bb:product:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Product product, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "产品信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Product> page = productService.findPage(new Page<Product>(request, response, -1), product);
    		new ExportExcel("产品信息", Product.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出产品信息记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/product/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("bb:product:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Product> list = ei.getDataList(Product.class);
			for (Product product : list){
				try{
					productService.save(product);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条产品信息记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条产品信息记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入产品信息失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/product/?repage";
    }
	
	/**
	 * 下载导入产品信息数据模板
	 */
	@RequiresPermissions("bb:product:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "产品信息数据导入模板.xlsx";
    		List<Product> list = Lists.newArrayList(); 
    		new ExportExcel("产品信息数据", Product.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/bb/product/?repage";
    }
	
	
	

}