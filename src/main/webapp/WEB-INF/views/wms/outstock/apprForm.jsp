<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
	<div>
		<form id="mainform" action="${ctx}/supervision/outappr/save"
			method="post">
			<table class="formTable">
				<tr data-options="">
					<td>业务单号：</td>
					<td><input id="LinkId" name="LinkId"
						class="easyui-validatebox" value="${bisOutStock.outLinkId }"
						style="background: #eee" data-options="width:150 " readonly />
					</td>
					<td>申请类型：</td>
					<td><input id="ApprType" name="ApprType"
						class="easyui-combobox"
						data-options="width:150, required:'required'"/></td>
				</tr>
				<tr>
					<td>出入区类型：</td>
					<td><input id="IoType" name="IoType" class="easyui-combobox"
						data-options=" width: 150, required:'required'"/></td>
					<td>关联核注清单编号：</td>
					<td><input id="BondInvtNo" name="BondInvtNo"
						class="easyui-validatebox" data-options="width: 150" value="" /></td>
				</tr>
				<tr>
					<td>件数：</td>
					<td><input id="PackNo" name="PackNo"
						class="easyui-validatebox" data-options="width: 150" value="" /></td>
					<td>毛重：</td>
					<td><input id="GrossWt" name="GrossWt"
						class="easyui-validatebox" data-options="width: 150" value="" /></td>
				</tr>
				<tr>
					<td>主管海关：</td>
					<td><input id="CustomsCode" name="CustomsCode"
						class="easyui-validatebox"
						data-options="width: 150, required:'required'" value="" /></td>
					<td>账册编号：</td>
					<td><input id="EmsNo" name="EmsNo" class="easyui-validatebox"
						data-options="width: 150, required:'required'" value="" /></td>
				</tr>
				<tr>
					<td>底账商品项号：</td>
					<td><input id="GNo" name="GNo" class="easyui-validatebox"
						data-options="width: 150" value="" style="background: #eee" readonly/></td>
				</tr>

			</table>
			<table class="formTable">
				<tr>
					<td>申请备注：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&ensp;</td>
					<td><textarea id="DNote" name="DNote" rows="3" cols="41"
							class="easyui-validatebox"
							data-options="validType:'length[0,50]'"
							style="font-size: 12px; font-family: '微软雅黑'" /></td>
				</tr>
			</table>
		</form>
	</div>

	<script type="text/javascript">
		var list = '${bisOutStockInfo}';
	//	console.log(list);
		var resultList = JSON.parse(list);
		$(function() {
			resetForm();
		});

		function resetForm() {
			var pieces = 0;
			var net = 0;
			var gross = 0;
			for (var i = 0; i < resultList.length; i++) {
				pieces += Number(resultList[i]['piece']);
				net = Number(resultList[i]['netWeight']);
				gross += Number(resultList[i]['grossWeight']);

			}
			$("#PackNo").val(pieces);
			$("#net").val(net);
			$("#GrossWt").val(gross.toFixed(2));

			$("#ApprType").combobox({
				data : [ {
					'value' : '0',
					'text' : '非保税货物申请'
				}, {
					'value' : '1',
					'text' : '非保税转保税货物申请'
				}, {
					'value' : '2',
					'text' : '保税转非保税货物申请'
				}, {
					'value' : '3',
					'text' : '内通道出入区'
				} ],
				valueField : 'value',
				textField : 'text',
				editable:false  
			});

			$("#IoType").combobox({
				data : [ {
					'value' : '0',
					'text' : '无'
				}, {
					'value' : '1',
					'text' : '入区'
				}, {
					'value' : '2',
					'text' : '出区'
				} ],
				valueField : 'value',
				textField : 'text',
				editable:false  
			});
			$("#CustomsCode").val('4258');
			$("#EmsNo").val("NH4230210001");
			$("#BondInvtNo").val("");
			$("#GNo").val("");
			$("#DNote").val("");

		}
		
		//提交表单
		$('#mainform').form({    
		    onSubmit: function(){    
		    	var isValid = $(this).form('validate');
				return isValid;	// 返回false终止表单提交
		    },    
		    success:function(data){

		    	if(data=="success"){
			    	successTip(data,dg,d);
		    	}
		    	else{
				$.messager.confirm('已存在此条记录的申请单');
		    	}
	    	
		    }    
		}); 
	</script>
</body>
</html>