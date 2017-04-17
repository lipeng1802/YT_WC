<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>微信客户信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="customer" action="${ctx}/bb/customer/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="weChat.id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		        <tr>
                    <td class="width-15 active"><label class="pull-right">微信号：</label></td>
                    <td class="width-35">
                        
                        <form:input path="weChat.wechatId" htmlEscape="false"    class="form-control "/>
                    </td>
                    <td class="width-15 active"><label class="pull-right">微信昵称：</label></td>
                    <td class="width-35">
                        <form:input path="weChat.wechatName" htmlEscape="false"    class="form-control "/>
                    </td>
                </tr>
                <tr>
                    <td class="width-15 active"><label class="pull-right">客户名称：</label></td>
                    <td colspan="3" >
                        <form:input path="name" htmlEscape="false"    class="form-control required"/>
                    </td>
                </tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">联系人：</label></td>
					<td class="width-35">
						<form:input path="contactPerson" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">电话：</label></td>
					<td class="width-35">
						<form:input path="phone" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">地址：</label></td>
					<td colspan="3" >
						<form:input path="address" htmlEscape="false"    class="form-control "/>
					</td>
					<%-- <td class="width-15 active"><label class="pull-right">是否默认使用：</label></td>
					<td class="width-35">
						<form:input path="isUse" htmlEscape="false"    class="form-control "/>
					</td> --%>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td  colspan="3">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>