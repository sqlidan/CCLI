<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

</head>
<body>
<div>
	<form id="mainform" action="${ctx}/cost/backstevedoring/${action}" method="post">
		<table class="formTable">
			<tr>
				<td>提单号：</td>
				<td> 
				<input type="hidden" id="drobackdate" name="drobackdate" value=""/>
				<input type="hidden" id="drostockId" name="drostockId" value=""/>
				<input type="hidden" id="drostockIn" name="drostockIn" value=""/> 
					<input type="hidden" id="netW" name="weight" value=""/> 
					<input id="billNumE" name="billNum" class="easyui-validatebox" data-options="width: 150, required:'required'" value="" readonly/> 
				</td>
			</tr>
			<tr>
				<td>装卸队：</td>
				<td>
				    <input type="hidden" id="clientE" name="client" value=""></input>
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
			</tr>
			<tr>
				<td>MR：</td>
				<td>
					<input id="mr" name="mr" class="easyui-validatebox" style="width: 150" value="" /> 
				</td>
			</tr>
			<tr>
				<td>在库分拣数量：</td>
				<td>
					<input id="sortingNuma" name="sortingNum" class="easyui-validatebox" style="width: 150, required:'required'" value=""  onkeyup="ischeckNum(this)"/> 
				吨</td>
			</tr>
			<tr>
				<td>人工装卸数量：</td>
				<td><input id="manNuma" name="manNum" type="text" class="easyui-validatebox" data-options="width: 150" value=""  onkeyup="ischeckNum(this)" />吨</td>
			</tr>
			
			<tr>
				<td>缠膜数量：</td>
				<td>
				<input type="text" id="wrapNuma" name="wrapNum" class="easyui-validatebox" data-options="width: 150 " value=""  onkeyup="ischeckNum(this)" />
				箱</td>
			</tr>
			<tr>
				<td>打包数量：</td>
				<td><input id="packNuma" name="packNum" type="text" class="easyui-validatebox"  data-options="width: 150 " value=""   onkeyup="ischeckNum(this)" />吨</td>
			</tr>
			<tr>
				<td>内标签数量：</td>
				<td><input id="nbqNuma" name="nbqNum" type="text" class="easyui-validatebox"  data-options="width: 150 " value=""   onkeyup="ischeckNum(this)" />张</td>
			</tr>
			
			<tr>
				<td>外标签数量：</td>
				<td><input id="wbqNuma" name="wbqNum" type="text" class="easyui-validatebox"  data-options="width: 150 " value=""   onkeyup="ischeckNum(this)" />张</td>
			</tr>
			
			<tr>
				<td>码托数量：</td>
				<td><input id="mtNuma" name="mtNum" type="text" class="easyui-validatebox"  data-options="width: 150 " value=""   onkeyup="ischeckNum(this)" />吨</td>
			</tr>
			
			<tr>
				<td>装铁架数量：</td>
				<td><input id="ztjNuma" name="ztjNum" type="text" class="easyui-validatebox"  data-options="width: 150 " value=""   onkeyup="ischeckNum(this)" /></td>
			</tr>
			
			<tr>
				<td>拆铁架数量：</td>
				<td><input id="ctjNuma" name="ctjNum" type="text" class="easyui-validatebox"  data-options="width: 150 " value=""   onkeyup="ischeckNum(this)" /></td>
			</tr>
			<tr>
				<td>是否全人工：</td>
				<td>
				<input id="ifAllMan" name="ifAllMan" type="checkbox"  value="1" />
				</td>
			</tr>
			<tr>
			<td>日期</td>
				<td><input type="text" name="date" id="date" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width:150,prompt: '选择日期'"/></td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
	    var action="${action}";
		$("#netW").val( $("#weight").val());
		$("#billNumE").val( $("#billNum").val() );
		$("#drobackdate").val($("#backdateb").val());
		$("#drostockId").val($("#stockIdb").val()); 
		$("#drostockIn").val($("#client").val()); 
		$("input[id$='Numa']").val(0);
        //客户
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
 		var ta = $("#weight").val();
 		$("#manNuma").val(ta);
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
				billNum = $("#billNum").val();
				zx(billNum);
				parent.$.messager.show({title: "提示", msg: "添加成功！", position: "bottomRight" });
			}
			else if(msg == "sorting"){
				parent.$.messager.show({title: "提示", msg: "在库分拣数量大于未应付分拣数量！", position: "bottomRight" });
			}
			else if(msg == "man"){
				parent.$.messager.show({title: "提示", msg: "人工装卸数量大于未人工装卸数量！", position: "bottomRight" });
			}else if(msg=="data"){
				parent.$.messager.show({title: "提示", msg: "日期不能为空！", position: "bottomRight" });
	        }else{
				parent.$.messager.show({title: "提示", msg: "打包数量大于未打包数量！", position: "bottomRight" });
			}
	}
});
</script>
</body>
</html>