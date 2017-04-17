  <%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>产品信息管理</title>
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
					if (element.is(  ":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
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
<form:form id="inputForm" modelAttribute="product" action="${ctx}/bb/product/save" method="post" class="form-horizontal">
        <form:hidden path="id"/>
        <sys:message content="${message}"/>
        <div class="role">
			<div class="col-sm-5">
				<div class="ibox">
					<div class="ibox-title">
						<h5>产品信息</h5>
					</div>
					<div class="ibox-content">
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="col-sm-3"><font color="red">*</font>
										拼装名称：</label>
									<div class="col-sm-9">
									    <form:input path="name" readonly="true" htmlEscape="false" class="form-control"  placeholder="选择属性后系统生成"/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3">关联其它系统名称：</label>
									<div class="col-sm-9">
										<form:input path="relationName" htmlEscape="false"
											class="form-control " />
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3">规格：</label>
									<div class="col-sm-9">
									    <form:input path="style" readonly="true" htmlEscape="false" class="form-control"  placeholder="选择右侧规格型号"/>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3"> 备注信息：</label>
									<div class="col-sm-9">
										<form:textarea path="remarks" htmlEscape="false" rows="4"
											class="form-control " />
									</div>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div>

			<div class="col-sm-7">
				<div class="ibox">
					<div class="ibox-title">
						<h5>产品信息</h5>
					</div>
					<div class="ibox-content">
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label class="col-sm-2 text-right">花色：</label>
									<div class="col-sm-10">
										<sys:gridselect url="${ctx}/bb/attribute/selectattribute"
											id="attributeList0" name="attributeList[0].id" value="${huase.id}" title="选择花色"
											cssClass="form-control required" labelName="huase.name"
											labelValue="${huase.name}" fieldLabels="花色|价格|备注"
											fieldKeys="name|price|remarks" searchLabel="花色"
											searchKey="name"></sys:gridselect>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2  text-right"> 板材：</label>
									<div class="col-sm-10">
									   <c:forEach items="${PLATE }" var="c">
                                          <label class="fontNormal" title="价格：${c.price }"> <input
                                              type="radio" name="attributeList[1].id" class="i-checks " required="required"
                                              value="${c.id }" text="${c.name }" attrType=${c.type } ${c.sort}>${c.name }
                                          </label>
                                       </c:forEach>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2  text-right">板材等级：</label>
									<div class="col-sm-10">
									   <c:forEach items="${GRADE }" var="c">
                                            <label class="m-r-md fontNormal" title="价格：${c.price }"> <input
                                                type="radio" name="attributeList[2].id" class="i-checks required"
                                                value="${c.id }" text="${c.name }" attrType=${c.type }  ${c.sort}>${c.name }
                                            </label>
                                        </c:forEach>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2  text-right">型号：</label>
									<div class="col-sm-10">
									   <c:forEach items="${STYLE}" var="c">
                                            <label class="m-r-md fontNormal" title="价格：${c.price }"> <input
                                                type="radio" name="attributeList[3].id" class="i-checks required"
                                                value="${c.id}" text="${c.name }" attrType=${c.type }  ${c.sort}>${c.name}
                                            </label>
                                        </c:forEach>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 text-right "> 厚度：</label>
									<div class="col-sm-10">
									   <c:forEach items="${THICK}" var="c">
                                           <label class="m-r-md fontNormal" title="价格：${c.price }"> <input
                                               type="radio" name="attributeList[4].id" class="i-checks required"
                                               value="${c.id }" text="${c.name }" attrType=${c.type }  ${c.sort}>${c.name }
                                           </label>
                                       </c:forEach>
									</div>
								</div>
								<div class="form-group">

									<label class="col-sm-2  text-right"> 钢板：</label>
									<div class="col-sm-10">
									   <c:forEach items="${GRAIN}" var="c">
                                           <label class="m-r-md fontNormal" title="价格：${c.price }"> <input
                                               type="radio" name="attributeList[5].id" class="i-checks required"
                                               value="${c.id }" text="${c.name }" attrType=${c.type }  ${c.sort}>${c.name }
                                           </label>
                                       </c:forEach>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2  text-right"> 贴面要求：</label>
									<div class="col-sm-10">
										<c:forEach items="${VENEER}" var="c" >
	                                        <label class="m-r-md fontNormal" title="价格：${c.price }"> <input
	                                            type="radio" name="attributeList[6].id" class="i-checks required"
	                                            value="${c.id }" text="${c.name }" attrType=${c.type }  ${c.sort}>${c.name }
	                                        </label>
	                                    </c:forEach>
									</div>
								</div>
								<div class="form-group">
                                    <label class="col-sm-2  text-right"> 是否现货：</label>
                                    <div class="col-sm-10">
                                        <c:forEach items="${STORAGE}" var="c" >
                                            <label class="m-r-md fontNormal" title="价格：${c.price }"> <input
                                                type="radio" name="attributeList[7].id" class="i-checks required"
                                                value="${c.id }" text="${c.name }" attrType=${c.type }  ${c.sort}>${c.name }
                                            </label>
                                        </c:forEach>
                                    </div>
                                </div>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
</form:form>
</body>
</html>