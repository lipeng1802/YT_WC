<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单明细管理</title>
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
		<h5>订单信息</h5>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
				<tr>

					<td class="width8P active"><label class="pull-right">微信 / 客户：</label></td>
					<td class="width25P">${order.wechat.wechatName } / 
						${order.customer.name}</td>
					<td class="width8P active"><label class="pull-right">联系人 / 电话：</label></td>
					<td class="width25P">${order.customer.contactPerson} / 
						${order.customer.phone}</td>
					<td class="width8P active"><label class="pull-right">送货地址：</label></td>
					<td class="width25P">${order.customer.address}</td>
				</tr>
				<tr>
					<td class="width8P active"><label class="pull-right">订单号：</label></td>
					<td class="width25P">${order.sn}</td>
					<td class="width8P active"><label class="pull-right">订单日期：</label></td>
					<td class="width25P"><fmt:formatDate
							value="${order.orderDate}" pattern="yyyy-MM-dd HH:mm" /></td>
					<td class="width8P active"><label class="pull-right">是否要发票：</label></td>
					<td class="width25P">${order.invoice }</td>
				</tr>
				<tr>
					<td class="width8P active"><label class="pull-right">邮寄方式：</label></td>
					<td class="width25P">${order.postMethod }</td>
					<td class="width8P active"><label class="pull-right">付款方式：</label></td>
					<td class="width25P">${order.payMethod }</td>
					<td class="width8P active"><label class="pull-right ">订单状态：</label></td>
					<td class="width25P"><font class="totalMoney">${order.state }</font></td>
				</tr>
				<tr>
					<td class="width8P active"><label class="pull-right">备注：</label></td>
					<td class="width25P">${order.remarks }</td>
					<td class="width8P active"><label class="pull-right">总金额：</label></td>
					<td class="width25P" colspan="3"><font class="totalMoney">
					<fmt:formatNumber value="${order.total }" pattern="##00.00#"/>
					</font></td>
					
				</tr>
				<tr>
                    <td class="width8P active"><label class="pull-right">其它费用明细：</label></td>
                    <td class="" colspan="5">
                         <ul>
                            <c:forEach items="${costs}" var="g">
                                <li>${g.subject }：${g.money }</li>
                            </c:forEach>                        
                        </ul>
                    
                    </td>
                    
                </tr>
		</table>
	<hr>
				<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
			    <th  class="">顺序</th>
				<th  class="">产品名称（系统）</th>
				<th  class="">产品属性</th>
				<th  class="">产品名称（内部）</th>
				<th  class="">订货产品描述</th>
				<th  class="">型号</th>
				<th  class="">现货</th>
				<th  class="">数量(张)</th>
				<th  class="">单价(元)</th>
				<th  class="">小计(元)</th>
				<th  class="">备注</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${details}" var="orderDetail"  varStatus="status">
			<tr>
			    <td class="text-center">${status.index+1 }</td>
				<td>
					${orderDetail.product.name}
				</td>
				<td>
				    <c:if test="${!empty orderDetail.product.attributeList}">
                        <ul>
                            <c:forEach items="${orderDetail.product.attributeList}" var="g">
                                <li>${g.type }：${g.name }</li>
                            </c:forEach>                        
                        </ul>
                     </c:if>
				</td>
				<td>
                    ${orderDetail.product.relationName}
                </td>
                <td>
                    ${orderDetail.productDesc}
                </td>
                <td>
                    ${orderDetail.product.style}
                </td>
                <td>
                    ${orderDetail.isStorage}
                </td>
				<td>
					${orderDetail.size}
				</td>
				<td class="text-right">
					<fmt:formatNumber value="${orderDetail.price}" pattern="##00.00#"/>
				</td>
				<td class="text-right">
                    <fmt:formatNumber value="${orderDetail.price*orderDetail.size}" pattern="##00.00#"/> 
                </td>
				<td>
					${orderDetail.remarks}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>