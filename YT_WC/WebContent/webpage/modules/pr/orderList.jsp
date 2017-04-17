<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
	        //外部js调用
	        laydate({
	            elem: '#beginDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus', //响应事件。如果没有传入event，则按照默认的click
	            choose: function(datas){ //选择日期完毕的回调
	                return datas;
	              }

	        });
	
	       
	    })
	    /**订单注销**/
	    function changeState(state){
			 var path="";
			 switch(state){
			 case "已付款":path="yiFuKuan"; break;
			 case "已生产":path="yiShengChan"; break;
			 case "已发货":path="yiFaHuo"; break;
			 case "完成":path="wanCheng"; break;
			 case "注销":path="cancle"; break;
			 default:path="";
			 }
			 if(path=="")return;
			 var str="";
             var ids="";
             $("#contentTable tbody tr td input.i-checks:checkbox").each(function(){
               if(true == $(this).is(':checked')){
                 str+=$(this).attr("id")+",";
               }
             });
             if(str.substr(str.length-1)== ','){
               ids = str.substr(0,str.length-1);
             }
             if(ids == ""){
               top.layer.alert('请至少选择一条数据!', {icon: 0, title:'警告'});
               return;
             }
             
             var model={"ids":ids};
			 $.ajax({
                 async: false,
                 url: "${ctx}/pr/order/"+path,
                dataType: "json",
                 data:model,
                 success: function (data) {
                     if(data.success){
                    	 $.jBox.tip(data.msg,'success',{timeout:1000});
                    	 search();
                     }else{
                         $.jBox.tip(data.msg,'warning',{timeout:1000});
                     }
                 }
             }); 
		}
	</script>
</head>
<body class="gray-bg">
    <div class="row  gray-bg dashboard-header clearMP" style="margin-top:15px">
         <div class="text-center col-sm-offset-2 clearfix">
             <div class="col-sm-2  text-center fl">
                 <button onclick="top.openTab('${ctx}/bb/product','产品列表', false)" class="btn btn-warning   btn-large-dim  circle" type="button">
                 <i class="fa fa-gavel fast_button_i"></i>
                 <span class="fast_button_span">产品列表</span>
                 </button> 
             </div>
             <div class="col-sm-2  text-center fl">
                 <button onclick="top.openTab('${ctx}/bb/colorConfig/colorList','花色配置', false)" class="btn btn-danger   btn-large-dim  circle" id="btn_1" type="button">
                 <i class="fa fa-object-ungroup fast_button_i"></i>
                 <span class="fast_button_span">花色配置</span>
                 </button> 
             </div>
             <div class="col-sm-2  text-center fl">
                 <button onclick="top.openTab('${ctx}/bb/attribute/list','属性管理', false)" class="btn btn-primary    btn-large-dim  circle" type="button">
                     <i class="fa fa-bug fast_button_i"></i><span class="fast_button_span">属性管理</span>
                 </button>
             </div>
             <div class="col-sm-2  text-center fl">
                 <button onclick="top.openTab('${ctx}/pr/order','订单管理', false)" class="btn btn-info   btn-large-dim  circle" type="button">
                 <i class="fa fa-cart-plus fast_button_i"></i><span class="fast_button_span">订单管理</span>
                     </button>
             </div>
             <div class="col-sm-2  text-center fl">
                 <button onclick="top.openTab('${ctx}/bb/customer','微信客户', false)" class="btn btn-success  btn-large-dim  circle" type="button">
                 <i class="fa fa-wechat fast_button_i"></i><span class="fast_button_span">微信客户</span>
                     </button>
             </div>
         </div>
         
     </div>

	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>订单列表 </h5>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="order" action="${ctx}/pr/order/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>订单信息：</span>
				<form:input path="orderInfo" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/>
<%-- 				<sys:gridselect url="${ctx}/bb/customer/selectCustomer"
                                            id="customerId" name="customerId" value="${customer.id}" title="选择花色"
                                            cssClass="form-control" labelName="customer.name"
                                            labelValue="${customer.name}" fieldLabels="微信昵称|客户名称|联系人|电话"
                                            fieldKeys="weChat.wechatName|name|contactPerson|phone" searchLabel="客户名称"
                                            searchKey="name"></sys:gridselect> --%>
				
			<%-- <span>订单号：</span>
				<form:input path="sn" htmlEscape="false" maxlength="255"  class=" form-control input-sm"/> --%>
			<span>定货日期：</span>
				<input id="beginDate" name="beginDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
                value="<fmt:formatDate value="${order.beginDate}" pattern="yyyy-MM-dd"/>"/>
                ~
				<input id="endDate" name="endDate" type="text" maxlength="20" class=" laydate-icon form-control layer-date input-sm"
                value="<fmt:formatDate value="${order.endDate}" pattern="yyyy-MM-dd"/>" />
                
			<span>订单状态：</span>
				<form:select path="state"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('order_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<button  class="btn btn-primary btn-rounded btn-outline btn-sm m-l-sm" onclick="search()" ><i class="fa fa-search"></i> 查询</button>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-right">
		    <shiro:hasPermission name="pr:order:fukuan">
                <a href="#" onclick="changeState('已付款')" class="btn btn-warning btn-sm" ><i class="fa fa-hourglass-3"></i> 订单已付款</a>
            </shiro:hasPermission>
			<shiro:hasPermission name="pr:order:shengchan">
                <a href="#" onclick="changeState('已生产')" class="btn btn-info btn-sm" ><i class="fa fa-hourglass-3"></i> 订单已生产</a>
            </shiro:hasPermission>
            
            <shiro:hasPermission name="pr:order:fahuo">
                <a href="#" onclick="changeState('已发货')" class="btn btn-primary btn-sm" ><i class="fa fa-fighter-jet"></i> 订单已发货</a>
            </shiro:hasPermission>
            
            <shiro:hasPermission name="pr:order:wancheng">
                <a href="#" onclick="changeState('完成')" class="btn btn-success btn-sm" ><i class="fa fa-check"></i> 订单完成</a>
            </shiro:hasPermission>
			<shiro:hasPermission name="pr:order:cancle">
                <a href="#" onclick="changeState('注销')" class="btn btn-danger btn-sm" ><i class="fa fa-remove"></i> 注销</a>
            </shiro:hasPermission>
			
			<shiro:hasPermission name="pr:order:del">
				<table:delRow url="${ctx}/pr/order/deleteAll" id="contentTable"></table:delRow>
			</shiro:hasPermission>

			<shiro:hasPermission name="pr:order:export">
	       		<table:exportExcel url="${ctx}/pr/order/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-white btn-sm dis-none" data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
			<%-- 
			<shiro:hasPermission name="pr:order:add">
                <table:addRow url="${ctx}/pr/order/form" title="订单"></table:addRow><!-- 增加按钮 -->
            </shiro:hasPermission>
			
			<shiro:hasPermission name="pr:order:edit">
                <table:editRow url="${ctx}/pr/order/form" title="订单" id="contentTable"></table:editRow><!-- 编辑按钮 -->
            </shiro:hasPermission> --%>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column c.name">微信/ 客户</th>
				<th  class="sort-column c.name">联系人/ 电话</th>
				<th  class="sort-column sn">订单号</th>
				<th  class="sort-column orderDate">定货日期</th>
				<th  class="sort-column state">订单状态</th>
				<th  class="sort-column postMethod">邮寄方式</th>
				<th  class="sort-column payMethod">付款方式</th>
				<th  class="sort-column invoice">发票</th>
				<th  class="sort-column total">总金额</th>
				<th  class="sort-column remarks">备注信息</th>
				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="order">
			<tr>
				<td> <input type="checkbox" id="${order.id}" class="i-checks"></td>
				<td><a  href="#" onclick="top.openTab('${ctx}/pr/order/orderDetail?id=${order.id}','订单明细', false)">
					 ${order.wechat.wechatName } / ${order.customer.name}
				</a></td>
				<td>
                    ${order.customer.contactPerson} / ${order.customer.phone}
                </td>
				<td>
					${order.sn}
				</td>
				<td>
					<fmt:formatDate value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				<td>
					${fns:getDictLabel(order.state, 'order_type', '')}
				</td>
				<td>
					${order.postMethod}
				</td>
				<td>
					${order.payMethod}
				</td>
				<td>
					${order.invoice}
				</td>
				<td>
					${order.total}
				</td>
				<td>
					${order.remarks}
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