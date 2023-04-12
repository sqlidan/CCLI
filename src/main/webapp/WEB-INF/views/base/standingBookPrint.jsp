<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform" action="${ctx}/cost/standingBook/${action}" method="post">
		<table class="formTable">
		    <tr>
				<td>发票号码：</td>
				<td>
					<input name="invoiceNo" id="invoiceNo"  class="easyui-validatebox" data-options="width: 150, required:'required' " value="${invoiceNo}" readonly="readonly"/> 
				</td>
			</tr>
			<tr>
				<td>发票代码：</td>
				<td>
					<input name="invoiceCode" id="invoiceCode"  class="easyui-validatebox" data-options="width: 150, required:'required' " value="${invoiceCode}" readonly="readonly"/> 
				</td>
			</tr>
			<tr>
				<td>开票客户：</td>
				<td>
				    <input type="hidden" id="codeNum" name="codeNum" value="${codeNum}"/>
					<input name="custom" id="custom"  class="easyui-validatebox" data-options="width: 150, required:'required' " value="${custom}" /> 
				</td>
			</tr>
			<tr>
                <td>税盘类型：</td>
                <td>
                    <select id="taxType" name="taxType"  class="easyui-combobox"
                           data-options="width:150,prompt:'税盘类型',valueField: 'id',textField:'text',value:'000',data: [{id:'000',text:'主机'},{id:'001',text:'分机'}]"/>
                </td>
            </tr>
		</table>
	</form>
</div>
<script type="text/javascript">
//提交表单
$('#mainform').form({
	onSubmit : function() {
		var isValid = $(this).form('validate');
		return isValid; // 返回false终止表单提交
	},
	success : function(data) {
		successTip(data, dg, d);
	}
});
</script>
</body>
</html>