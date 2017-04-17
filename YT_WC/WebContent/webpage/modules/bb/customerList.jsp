<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>微信客户信息管理</title>
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
		<h5>微信客户信息列表 </h5>
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
	<form:form id="searchForm" modelAttribute="customer" action="${ctx}/bb/customer/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>微信ID：</span>
				<form:input path="wechatId" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
			<span>联系人：</span>
				<form:input path="contactPerson" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
			<span>电话：</span>
				<form:input path="phone" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
			<span>地址：</span>
				<form:input path="address" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="bb:customer:add">
				<table:addRow url="${ctx}/bb/customer/form" title="微信客户信息"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="bb:customer:edit">
			    <table:editRow url="${ctx}/bb/customer/form" title="微信客户信息" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="bb:customer:del">
				<table:delRow url="${ctx}/bb/customer/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<%-- <shiro:hasPermission name="bb:customer:import">
				<table:importExcel url="${ctx}/bb/customer/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="bb:customer:export">
	       		<table:exportExcel url="${ctx}/bb/customer/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission> --%>
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
				<th  class="sort-column w.wechat_id">微信号</th>
				<th  class="sort-column w.wechat_name">微信昵称</th>
				<th  class="sort-column name">客户名称</th>
				<th  class="sort-column contactPerson">联系人</th>
				<th  class="sort-column phone">电话</th>
				<th  class="sort-column address">地址</th>
				<!-- <th  class="sort-column isUse">是否默认使用</th> -->
				<th  class="sort-column updateDate">注册时间</th>
				<th  class="sort-column remarks">备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="customer">
			<tr>
				<td> <input type="checkbox" id="${customer.id}" class="i-checks"></td>
				<td>
                    ${customer.weChat.wechatId}
                </td>
                <td>
                   ${customer.weChat.wechatName}
                </td>
                <td>
	                <a  href="#" onclick="openDialogView('查看微信客户信息', '${ctx}/bb/customer/form?id=${customer.id}','800px', '500px')">
	                    ${customer.name}
	                </a>
                </td>
				<td>
					${customer.contactPerson}
				</td>
				<td>
					${customer.phone}
				</td>
				<td>
					${customer.address}
				</td>
				<%-- <td>
					<c:choose> 
						<c:when test="${'1' ==  customer.isUse}">   
						  是
						</c:when> 
						<c:otherwise>   
						 否
						</c:otherwise> 
					</c:choose> 
				</td> --%>
				<td>
					<fmt:formatDate value="${customer.createDate}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				<td>
					${customer.remarks}
				</td>
				<td>
					<shiro:hasPermission name="bb:customer:view">
						<a href="#" onclick="openDialogView('查看微信客户信息', '${ctx}/bb/customer/form?id=${customer.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="bb:customer:edit">
    					<a href="#" onclick="openDialog('修改微信客户信息', '${ctx}/bb/customer/form?id=${customer.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="bb:customer:del">
						<a href="${ctx}/bb/customer/delete?id=${customer.id}" onclick="return confirmx('确认要删除该微信客户信息吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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