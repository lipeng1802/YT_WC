<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>产品属性管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  //loading('正在提交，请稍等...');
		  if(validateForm.form()){
			  //$("#inputForm").submit();
			  var model=$("#inputForm").serializeArray();
			  var r=false;
			  $.ajax({
                  async: false,
                  url: "${ctx}/bb/colorConfig/save",
                  dataType: "json",
                  data:model,
                  success: function (data) {
                      if(data.success){
                    	  console.log(data);
                    	  top.$.jBox.tip(data.msg,'loading',{opacity:0});
                    	  directIframeRefresh(tabName.花色配置,'searchBtn');
                    	  setTimeout(function(){top.layer.close(top.layer.index)}, 100);
                    	  r = true;
                      }else{
                    	  $.jBox.tip(data.msg,'warning',{timeout:1000});
                      }
                  }
              });
		  }
		  return r;
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					
					//form.submit();
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
		<form:form id="inputForm" modelAttribute="attribute" action="${ctx}/bb/colorConfig/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font> ${attribute.type}：</label></td>
					<td class="">
						<form:input path="name" htmlEscape="false"    class="form-control required width60P"/>
						<form:input path="type" type="hidden" value="${attribute.type}"/>
					</td>
					
				</tr>
				<tr>
				    <td class="width-15 active"><label class="pull-right"><font color="red">*</font> 价格：</label></td>
                    <td>
                        <form:input path="price" htmlEscape="false" class="form-control required number width40P"/>
                        <i class="fa fa-exclamation-circle"></i><span class="help-inline">用于计算客户下单时 成品/张 的单价</span>
                    </td>
				</tr>
				
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td>
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>