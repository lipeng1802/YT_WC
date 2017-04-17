<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>产品信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>产品信息列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="product" action="${ctx}/bb/product/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>拼装名称：</span>
				<form:input path="name" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
			<span>关联其它系统名称：</span>
				<form:input path="relationName" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
			<span>花色：</span>
                <%-- <form:input path="reserve01" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>	 --%>
				<sys:gridselect url="${ctx}/bb/attribute/selectattribute"
                                            id="reserve01" name="reserve01" value="${product.reserve01}" title="选择花色"
                                            cssClass="form-control required" labelName="huase.name"
                                            labelValue="${searchColor.name}" fieldLabels="花色|备注"
                                            fieldKeys="name|remarks" searchLabel="花色"
                                            searchKey="name"></sys:gridselect>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="bb:product:add">
				<table:addRow url="${ctx}/bb/product/form" width="1200px" height="750px" title="产品信息"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="bb:product:edit">
			    <table:editRow url="${ctx}/bb/product/form" width="1200px"  height="750px" title="产品信息" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="bb:product:del">
				<table:delRow url="${ctx}/bb/product/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<%-- <shiro:hasPermission name="bb:product:import">
				<table:importExcel url="${ctx}/bb/product/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission> 
			<shiro:hasPermission name="bb:product:export">
	       		<table:exportExcel url="${ctx}/bb/product/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>--%>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th class="width20F"> <input type="checkbox" class="i-checks"></th>
				<th  class="width20P sort-column name">拼装名称</th>
				<th  class="width20P sort-column relationName">关联其它系统名称</th>
				<th  class="sort-column style">规格</th>
				<th  class="width20P">属性列表</th>
				<th  class="sort-column  remarks">备注信息</th>
				<th class="width15P">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="product">
			<tr>
				<td> <input type="checkbox" id="${product.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看产品信息', '${ctx}/bb/product/form?id=${product.id}','1200px', '750px')">
					${product.name}
				</a></td>
				<td>
					${product.relationName}
				</td>
				<td>
					${product.style}
				</td>
				<td>
                     <c:if test="${!empty product.attributeList}">
                        <ul>
                            <c:forEach items="${product.attributeList}" var="g">
                                <li>${g.type }：${g.name }</li>
                            </c:forEach>                        
                        </ul>
                     </c:if>
                </td>
				
				<td>
					${product.remarks}
				</td>
				<td>
						<a href="#" onclick="openDialogView('查看产品信息', '${ctx}/bb/product/form?id=${product.id}','1200px', '750px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					<shiro:hasPermission name="bb:product:edit">
    					<a href="#" onclick="openDialog('修改产品信息', '${ctx}/bb/product/form?id=${product.id}','1200px', '750px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="bb:product:del">
						<a href="${ctx}/bb/product/delete?id=${product.id}" onclick="return confirmx('确认要删除该产品信息吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>