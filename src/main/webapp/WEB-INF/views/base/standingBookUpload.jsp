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
				<td>发票客户：</td>
				<td>
				    <input type="hidden" id="operator" name="operator" value="${obj.operator}"/>
				    <input type="hidden" id="codeNum" name="codeNum" value="${obj.codeNum}"/>
					<input type="hidden" id="customID" name="customID" value="${obj.customID}"/>
					<input name="custom" id="custom"  class="easyui-validatebox" data-options="width: 150, required:'required' " value="${custom}" /> 
				</td>
			</tr>
			<c:forEach items="${list}" var="map" varStatus="num">
					<tr>
						<td>${map.FEENAME}：</td>
						<td>
						   <input id="FEENAME${num.index}" name="FEENAME${num.index}" type="hidden" value="${map.FEENAME}"/>
						   <input id="FEECODE${num.index}" name="FEECODE${num.index}" type="hidden" value="${map.FEECODE}"/>
						   <input id="LABEL${num.index}" name="LABEL${num.index}"  type="hidden"  value="${map.LABEL}"/>
						   <input id="RATE${num.index}" name="RATE${num.index}"  type="hidden"  value="${map.RATE}"/>
						   <input id="RMB${num.index}" name="RMB${num.index}" class="easyui-validatebox" data-options="width: 150, required:'required' "  value="${map.RMB}"/>
						</td>
					</tr>
			</c:forEach>
			<tr>
                <td>税盘类型：</td>
                <td>
                    <select id="taxType" name="taxType"  class="easyui-combobox"
                           data-options="width:150,required:'required',prompt:'税盘类型',valueField: 'id',textField:'text',value:'000',data: [{id:'000',text:'主机'},{id:'001',text:'分机'}]"/>
                </td>
            </tr>
            
			<tr>
                <td>发票类型：</td>
                <td>
                    <select id="invoiceType" name="invoiceType"  class="easyui-combobox"
                           data-options="width:150,required:'required',prompt:'发票类型',valueField: 'id',textField:'text',value:'1',data: [{id:'0',text:'普通发票'},{id:'1',text:'专用发票'},{id:'2',text:'电子发票'}]"/>
                </td>
            </tr>
            <tr>
                <td>开票人：</td>
                <td>
                    <select id="drawer" name="drawer"  class="easyui-combobox"
                           data-options="width:150,required:'required',prompt:'开票人',valueField: 'id',textField:'text',value:'049584',data: [{id:'049584',text:'陈昊'},{id:'420545',text:'厉婷'},{id:'320060',text:'李鲁璐'},{id:'049381',text:'陈籽言'}]"/>
                </td>
            </tr>
            <tr>
                <td>收款人：</td>
                <td>
                    <select id="payer" name="payer"  class="easyui-combobox"
                           data-options="width:150,required:'required',prompt:'收款人',valueField: 'id',textField:'text',value:'049381',data: [{id:'049584',text:'陈昊'},{id:'420545',text:'厉婷'},{id:'320060',text:'李鲁璐'},{id:'049381',text:'陈籽言'}]"/>
                </td>
            </tr>
            <tr>
                <td>复核人：</td>
                <td>
                    <select id="checker" name="checker"  class="easyui-combobox"
                           data-options="width:150,required:'required',prompt:'收款人',valueField: 'id',textField:'text',value:'049381',data: [{id:'049584',text:'陈昊'},{id:'420545',text:'厉婷'},{id:'320060',text:'李鲁璐'},{id:'049381',text:'陈籽言'}]"/>
                </td>
            </tr>
            <tr>
                <td>往来户类型：</td>
                <td>
                    <select id="currentAccountType" name="currentAccountType"  class="easyui-combobox"
                           data-options="width:150,prompt:'往来户类型',valueField: 'id',textField:'text',value:'1',data: [{id:'1',text:'客户'},{id:'3',text:'供应商'},{id:'3',text:'公司'}]"/>
                </td>
            </tr>
            
            <tr>
                <td>币种：</td>
                <td>
                    <select id="currencynum" name="currencynum"  class="easyui-combobox"
                           data-options="width:150,prompt:'币种',valueField: 'id',textField:'text',value:'BB01',data: [{id:'BB01',text:'人民币'},{id:'BB02',text:'美元'},{id:'BB03',text:'港元'}]"/>
                </td>
            </tr>
            
            <tr>
                <td>备注：</td>
                <td>
                    <textarea cols="5" id="remark" name="remark" style="font-size:12px;width:150px; font-family: '微软雅黑';">
                    </textarea>
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