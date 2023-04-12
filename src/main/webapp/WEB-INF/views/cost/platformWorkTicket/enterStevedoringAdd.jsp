<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body>
<div>
	<form id="mainform" action="${ctx}/cost/enterNotPaid/${action}" method="post">
		<table class="formTable">
			<tr>
				<td>ASN：</td>
				<td> 
                    <input id="droMR" name="droMR" type="hidden" value=""/> 
                    <input id="droStockInId" name="droStockInId" type="hidden" value="" /> 
                    <input id="droEnterStockTime" name="droEnterStockTime"  value="" type="hidden"  /> 
					<input type="hidden" id="droStockInName" name="droStockInName" value=""/> 
					
					<input type="hidden" id="netW" name="netWeight" value=""/>					
					<input id="asnE" name="asnId" class="easyui-validatebox" data-options="width: 150, required:'required'" value="" readonly/> 
				</td>
			</tr>
			<tr>
				<td>装卸队：</td>
				<td>
				    <input type="hidden" id="client" name="client" value=""></input>
					<select id="clientId" name="clientId" class="easyui-combobox" data-options="width: 150, required:'required'" value="" > 
					</select>
				</td>
			</tr>
			<tr>
				<td>费用方案：</td>
				<td>
					<input type="hidden" id="feePlan" name="feePlan" value=""></input>
					<select id="feeId" name="feeId" class="easyui-combobox" data-options="width: 150, required:'required'" value="" > 
					</select>
				</td>
				<td>重量系数：</td>
				<td>
					<select id="numPlus" name="numPlus" class="easyui-combobox" data-options="width: 50, required:'required'" value="" >
					    <option value=1>1</option>
					    <option value=1.5>1.5</option>
					    <option value=2>2</option>
					    <option value=2.5 selected>2.5</option>
					    <option value=3>3</option>
					    <option value=3.5>3.5</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>应付分拣数量：</td>
				<td>
					<input id="sortingNuma" name="sortingNum" class="easyui-validatebox" style="width: 150, required:'required'" value=""  onkeyup="ischeckNum(this)"> 
				吨</td>
			</tr>
			<tr>
				<td>人工装卸数量：</td>
				<td><input id="manNuma" name="manNum" type="text" class="easyui-validatebox" data-options="width: 150" value=""  onkeyup="ischeckNum(this)" >吨</td>
			</tr>
			
			<tr>
				<td>缠膜数量：</td>
				<td>
				<input type="text" id="wrapNuma" name="wrapNum" class="easyui-validatebox" data-options="width: 150 " value=""  onkeyup="ischeckNum(this)" >
				吨</td>
			</tr>
			<tr>
				<td>打包数量：</td>
				<td><input id="packNuma" name="packNum" type="text" class="easyui-validatebox"  data-options="width: 150 " value=""   onkeyup="ischeckNum(this)" >吨</td>
			</tr>
			<tr>
				<td>是否全人工：</td>
				<td>
				<input id="ifAllMan" name="ifAllMan" type="checkbox"  value="1" >
				</td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
var action="${action}";

var numPlus='${platformWorkTicket.numPlus}';
$('#numPlus').val(numPlus);

var asn='${platformWorkTicket.asnTransNum}'
$("#asnE").val(asn);

$.ajax({
	type: 'POST',
	url: "${ctx}/cost/enterstevedoring/cxasn/"+asn,
	dataType: "json",
	success: function(msg){
		if(msg != null && msg!=""){
			//console.log(Number(msg[6])/1000);

			//重量
			$("#netW").val(Number(msg[6])/1000);

			$("#droMR").val(msg[3]); //droMR  droSstockInId  droEnterStockTime
			$("#droStockInId").val(msg[7]);
			$("#droStockInName").val(msg[4]);
			$("#droEnterStockTime").val(msg[8]);

			$("#ifAllMan").click();
		}else{
			parent.$.easyui.messager.show({title: "操作提示", msg: "未查到此条ASN信息！", position: "bottomRight"});
			return;
		}


	}
})


$("input[id$='Numa']").val(0);
//$("#ifAllMan").attr("checked",true);
//$("#ifAllMan").click();
//$("#manNuma").val($("#netW").val()*numPlus);
//客户
/*
	   $('#clientId').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=2",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode:'remote',
	   onChange: function (newVal,oldVal){
	   				if(newVal != ""){
	   					feeSelect(newVal);
	   				}
	   			 }
   	});
*/

var clientId='${platformWorkTicket.clientId}';

$('#clientId').combobox({
	method:"GET",
	url:"${ctx}/cost/platformWorkTicket/findZxd",
	valueField: 'id',
	textField: 'name',
	onLoadSuccess:function(){
		if(clientId!=null && clientId!=""){
			$('#clientId').combobox("select",clientId);
			clientId="";
		}
	},
	onHidePanel : function() {
		var _options = $(this).combobox('options');
		var _data = $(this).combobox('getData');/* 下拉框所有选项 */
		var _value = $(this).combobox('getValue');/* 用户输入的值 */
		var _b = false;/* 标识是否在下拉列表中找到了用户输入的字符 */
		for (var i = 0; i < _data.length; i++) {
			if (_data[i][_options.valueField] == _value) {
				_b=true;
				break;
			}
		}
		if(!_b){
			$(this).combobox('setValue', '');
			//platformId="";
		}
	},
	onChange: function (newVal,oldVal){
		if(newVal != ""){

			feeSelect(newVal);
		}
	}
});
 
//费用方案下拉框
function feeSelect(newVal){ 
var clientId = newVal;
   	$.ajax({
   		async:false,
		type : "GET",
		url : "${ctx}/wms/enterStock/selFeePlan",
		data : {"clientId":clientId},
		dataType : "json",
		success : function(date) {
				$('#feeId').combobox({
					data : date,
					valueField : 'schemeNum',
					textField : 'schemeName',
					editable : false
				});
		}
	});
}

//checkbox点击事件
$("#ifAllMan").click(function (){
	if( $("#ifAllMan").is(":checked") ){
 		var ta = $("#netW").val();
 		var plus = $("#numPlus").combobox("getValue");
 		$("#manNuma").val(ta*plus);
// 	  	$("input[id$='Numa']").val(ta);
 	}
});


//数字校验
function ischeckNum(val) {
	if (val.value) {
		if (!isNaN(val.value)) {

		} else {
			parent.$.messager.show({title: "提示", msg: "请输入数字!", position: "bottomRight" });
			$("#"+val.id).val("");
			myfm.isnum.select();
			return false;
		}
	}
}

//提交表单
$('#mainform').form({
	onSubmit : function() {
		var isValid = $(this).form('validate');
		return isValid; // 返回false终止表单提交
	},
	success : function(msg) {
			if(msg == "success"){
				d.panel('close');
				asn = $("#asn").val();
				cx();
				//zx(asn);
				parent.$.messager.show({title: "提示", msg: "添加成功！", position: "bottomRight" });
			}
			else if(msg == "sorting"){
				parent.$.messager.show({title: "提示", msg: "应付分拣数量大于未应付分拣数量！", position: "bottomRight" });
			}
			else if(msg == "man"){
				parent.$.messager.show({title: "提示", msg: "人工装卸数量大于未人工装卸数量！", position: "bottomRight" });
			}
			else if(msg == "wrap"){
				parent.$.messager.show({title: "提示", msg: "缠膜数量大于未缠膜数量！", position: "bottomRight" });
			}
			else{
				parent.$.messager.show({title: "提示", msg: "打包数量大于未打包数量！", position: "bottomRight" });
		    }
		
	}
});
</script>
</body>
</html>