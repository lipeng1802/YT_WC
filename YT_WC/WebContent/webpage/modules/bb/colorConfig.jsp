<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>花色配置</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
	    $("#saveConfig").click(function(){
	    	if($("#inputForm").validate().form()){
	    		 
		    	var attr_id=$("#selectColorId").val();
		    	if(attr_id == ""){
		    		return;
		    	}
		    	loading('正在提交，请稍等...');
		    	var colorConfig={
		    			"attribute":{ 
		    				"id":attr_id,
		    				"name":$("#r_name").val(),
		    				"type":"花色"
		    			 },
		    			"attrList":getCheckBoxValues("attrs")
		    	}
		    	  $.ajax({
	                  async: false,
	                  url: "${ctx}/bb/colorConfig/saveConfig",
	                  type:"post",
	                  dataType: "json",
	                  contentType:"application/json",
	                  data:JSON.stringify(colorConfig),
	                  success: function (data) {
	                      if(data.success){
	                         $("#searchBtn").click();
	                      }else{
	                    	  $.jBox.tip(data.msg,'warning',{timeout:1000});
	                      }
	                  }
	              }); 
		    	
	            
		        
	    	}
	    })
	    
	    function getCheckBoxValues(name){
	    	var arr=[];
	    	$("input[name='"+name+"']:checkbox").each(function(){
                if(true == $(this).is(':checked')){
                	debugger;
                	var a={
                			"id":$(this).val(),
                			"name":$(this).attr("text"),
                			"type":$(this).attr("attrType")
                	}
                    arr.push(a)
                }
            });
	    	return arr;
	    }
	    
	});
	function addAttribute(type){
        openDialog("新增"+type,"/jeeplus/a/bb/colorConfig/form?type="+type,"800px", "500px","");
    }
</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	   <div class="role">
			<div class="col-sm-7">
				<div class="ibox">
					<div class="ibox-title">
						<h5>花色列表</h5>
						<div class="ibox-tools">
							<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
							</a> <a class="dropdown-toggle" data-toggle="dropdown" href="#">
								<i class="fa fa-wrench"></i>
							</a> <a class="close-link"> <i class="fa fa-times"></i>
							</a>
						</div>
					</div>

					<div class="ibox-content">
						<sys:message content="${message}" />

						<!--查询条件-->
						<div class="row">
							<div class="col-sm-12">
								<form:form id="searchForm" modelAttribute="attribute"
									action="${ctx}/bb/colorConfig/colorList" method="post"
									class="form-inline text-center">
									<input type="hidden" name="selectId" id="selectColorId" value="${attr.id}">
									<input id="pageNo" name="pageNo" type="hidden"
										value="${page.pageNo}" />
									<input id="pageSize" name="pageSize" type="hidden"
										value="${page.pageSize}" />
									<table:sortColumn id="orderBy" name="orderBy"
										value="${empty page.orderBy?'createDate DESC':page.orderBy}"
										callback="sortOrRefresh();" />
									<!-- 支持排序 -->
									<div class="form-group input_w100P">
										<span>花色：</span>
										<form:input path="name" htmlEscape="false" maxlength="255"
											class=" form-control input_w70P" />
										<a href="#" id="searchBtn" onclick="sortOrRefresh()"
											class="btn btn-primary btn-sm"><i class="fa fa-search"></i>
											查询 </a>
									</div>
								</form:form>
								<br />
							</div>
						</div>

						<!-- 工具栏 -->
						<div class="row">
							<div class="col-sm-12">
								<div class="pull-left">
									<shiro:hasPermission name="bb:colorConfig:add">
										<table:addRow url="${ctx}/bb/colorConfig/form?type=花色"
											title="花色"></table:addRow>
										<!-- 增加按钮 -->
									</shiro:hasPermission>
									<shiro:hasPermission name="bb:colorConfig:del">
										<table:delRow url="${ctx}/bb/colorConfig/deleteAll"
											id="contentTable"></table:delRow>
										<!-- 删除按钮 -->
									</shiro:hasPermission>
								</div>
							</div>
						</div>
						<!-- 表格 -->
						<table id="contentTable"
							class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
							<thead>
								<tr>
									<th class="width20F"><input type="checkbox"
										class="i-checks"></th>
									<th class="width20P sort-column name">花色</th>
									<th class="sort-column price">配置描述</th>
									<th class="width50F">操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${page.list}" var="g">
									<tr>
										<td><input type="checkbox" id="${g.id}"
											class="i-checks"></td>
										<td>${g.name}</td>
										<td>${g.nameDesc}</td>
										<td><shiro:hasPermission name="bb:colorConfig:edit">
												<a href="#" onclick="$('#selectColorId').val('${g.id}');sortOrRefresh()" id="configBtn" class="btn btn-success btn-xs"><i class="fa fa-edit"></i>
													配置
												</a>
											</shiro:hasPermission></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<!-- 分页代码 -->
						<table:page page="${page}"></table:page>
						<br /> <br />
					</div>
					
				</div>
			</div>
		    <div class="col-sm-5" id="rightDiv">
                <div class="ibox">
                    <div class="ibox-title">
                        <h5>花色配置</h5>
                    </div>
					
						<div class="ibox-content">
						<form:form id="inputForm" modelAttribute="colorConfig"
                        action="${ctx}/bb/colorConfig/saveConfig" method="post" class="form-horizontal">
							<div class="row form-body form-horizontal m-t">
								<div class="col-md-12">
									<div class="form-group">
										<label class="col-sm-2">花色：</label>
										<div class="col-sm-10">
											<input type="text" class="form-control required" id="r_name" placeholder="花色不能为空"
												value="${attr.name }"> <input type="hidden"
												name="attribute.id" value="${attr.id }">
										</div>
									</div>
									<div class="form-group">
									    
										<label class="col-sm-2 cursor-p" onclick="addAttribute('板材')">
										  <i class="fa fa-plus text-danger"></i> 
										  板材：</label>
										<div class="col-sm-10">
											<c:forEach items="${PLATE }" var="c">
													<label class="block fontNormal" title="价格：${c.price }"> <input
														type="checkbox" name="attrs" class="i-checks"
														value="${c.id }" text="${c.name }" attrType=${c.type } ${c.sort}>${c.name }
													</label>
											</c:forEach>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 cursor-p"  onclick="addAttribute('板材等级')"><i class="fa fa-plus text-danger"></i> 板材等级：</label>
										<div class="col-sm-10">
												<c:forEach items="${GRADE }" var="c">
													<label class="m-r-md fontNormal" title="价格：${c.price }"> <input
														type="checkbox" name="attrs" class="i-checks"
														value="${c.id }" text="${c.name }" attrType=${c.type }  ${c.sort}>${c.name }
													</label>
												</c:forEach>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 cursor-p" onclick="addAttribute('型号')"><i class="fa fa-plus text-danger"></i> 型号：</label>
										<div class="col-sm-10">
												<c:forEach items="${STYLE}" var="c">
													<label class="m-r-md fontNormal" title="价格：${c.price }"> <input
														type="checkbox" name="attrs" class="i-checks"
														value="${c.id}" text="${c.name }" attrType=${c.type }  ${c.sort}>${c.name}
													</label>
												</c:forEach>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 cursor-p " onclick="addAttribute('厚度')"><i class="fa fa-plus text-danger"></i> 厚度：</label>
										<div class="col-sm-10">
												<c:forEach items="${THICK}" var="c">
													<label class="m-r-md fontNormal" title="价格：${c.price }"> <input
														type="checkbox" name="attrs" class="i-checks"
														value="${c.id }" text="${c.name }" attrType=${c.type }  ${c.sort}>${c.name }
													</label>
												</c:forEach>
										</div>
									</div>
									<div class="form-group">

										<label class="col-sm-2  cursor-p" onclick="addAttribute('钢板')"><i class="fa fa-plus text-danger"></i> 钢板：</label>
										<div class="col-sm-10">
												<c:forEach items="${GRAIN}" var="c">
													<label class="m-r-md fontNormal" title="价格：${c.price }"> <input
														type="checkbox" name="attrs" class="i-checks"
														value="${c.id }" text="${c.name }" attrType=${c.type }  ${c.sort}>${c.name }
													</label>
												</c:forEach>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 cursor-p" onclick="addAttribute('贴面要求')"><i class="fa fa-plus text-danger "></i> 贴面要求：</label>
										<div class="col-sm-10">
												<c:forEach items="${VENEER}" var="c" >
													<label class="m-r-md fontNormal" title="价格：${c.price }"> <input
														type="checkbox" name="attrs" class="i-checks"
														value="${c.id }" text="${c.name }" attrType=${c.type }  ${c.sort}>${c.name }
													</label>
												</c:forEach>
										</div>
									</div>
								</div>
							</div>
							<div class="row text-center">
							     <a href="#" id="saveConfig" 
                                            class="btn btn-danger"><i class="fa fa-save"></i>
                                                                                    保存配置</a>
							</div>
							</form:form>
						</div>
					
				</div>
            </div>
           
	   </div>
	
		
	</div>
</body>
</html>