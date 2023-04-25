<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<div data-options="region:'center'">
<form id="searchFrom3" action="">
</form>
	<form id="mainForm"   method="post">
<%--	<div style="height:auto" class="datagrid-toolbar">--%>
<%--	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="submitForm()">保存</a>--%>
<%--	   	<span class="toolbar-item dialog-tool-separator"></span>--%>

<%--	</div>--%>
	<table class="formTable" >
		<tr>




			<input id="id" name="id" class="easyui-validatebox"  type="hidden"   value="${platformUser.id}">

			<input type="hidden" id="createdTime" name="createdTime"   value="<fmt:formatDate value="${platformUser.createdTime}"  pattern="yyyy-MM-dd HH:mm:ss" />"/>
			<input id="deletedFlag" name="deletedFlag" class="easyui-validatebox"  type="hidden"   value="${platformUser.deletedFlag}">

			<%--<td>月台</td>
			<td>
				<input id="platformName" name="platformName" class="easyui-validatebox" data-options="width: 180" value="${platformUser.platformName}"  />

			</td>--%>

			<td>月台：</td>
			<td>
				<input type="hidden" id="platformName" name="platformName">
				<select id="platformId" name="platformId" class="easyui-combobox" data-options="width:180, required:'required'" >
				</select>
			</td>


		</tr>

		<tr>
			<td>操作人：</td>
			<td>
				<input type="hidden" id="userName" name="userName" >
				<select id="userId" name="userId" class="easyui-combobox" data-options="width:180, required:'required'"  maxlength="30" >
				</select>
			</td>



		 <%-- <td>月台操作人</td>
			<td>
				<input id="userName" name="userName" class="easyui-validatebox" data-options="width: 180" value="${platformUser.userName}"   />
			</td>	--%>

		</tr>
		

	</table>
</form>
</div>


<script type="text/javascript">
var dg;
var d;
var action = "${action}";
var a;
var b=0;
var dhs;
var des;





console.log(action);
$(function(){

	selectAjax();
});


function selectAjax(){




	//操作员
	var userId='${platformUser.userId}';
	$('#userId').combobox({
		method:"GET",
		url:"${ctx}/platform/user/manage/getUserAll?setid=${platformUser.userId}",
		valueField: 'id',
		textField: 'name',
		mode:'remote',
		onLoadSuccess:function(){
			if(userId!=null && userId!=""){
				$('#userId').combobox("select",userId);
				userId="";
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
				//userId="";
			}
		}
	});


	var platformId='${platformUser.platformId}';

	$('#platformId').combobox({
		method:"GET",
		url:"${ctx}/platform/user/manage/getPlatformAll",
		valueField: 'id',
		textField: 'platform',
		onLoadSuccess:function(){
			if(platformId!=null && platformId!=""){
				$('#platformId').combobox("select",platformId);
				platformId="";
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
		}
	});


}

var result="";
//保存
function submitForm(){

	$("#platformName").val( $("#platformId").combobox("getText") );

	//console.log($("#platformId").combobox("getData"));

	$("#userName").val( $("#userId").combobox("getText") );
	if($("#mainForm").form('validate')){
		if(action=="update"){
			submitFormBefore();



			if(result == "userIdFail"){
				parent.$.messager.alert("提示","操作员已存在，请确认");
				return;
			}

			if(result == "platformIdFail"){
				parent.$.messager.alert("提示","月台已存在，请确认");
				return;
			}

		}




		//用ajax提交form
 		$.ajax({
 	  		async: false,
 	  		type: 'POST',
 	  		url: "${ctx}/platform/user/manage/"+action,
 	  		data: $('#mainForm').serialize(),

 	  		success: function(msg){
				result=msg;
 	  			if(msg == "success"){
 	  				parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });

 	  			}
 	  			if(msg == "userIdFail"){
 	  				parent.$.messager.alert("提示","操作员已存在，请确认");
 	  			}

				if(msg == "platformIdFail"){
					parent.$.messager.alert("提示","月台已存在，请确认");
				}

 	  		}
 	  	});
 	}else{

		//parent.$.messager.alert("提示","必填项必填，请确认");
	}
}

function submitFormBefore(){

	$("#platformName").val( $("#platformId").combobox("getText") );
	$("#userName").val( $("#userId").combobox("getText") );
	if($("#mainForm").form('validate')){


		//用ajax提交form
		$.ajax({
			async: false,
			type: 'POST',
			url: "${ctx}/platform/user/manage/"+"existOfSelf",
			data: $('#mainForm').serialize(),

			success: function(msg){
				result=msg;
				return result;
			}
		});
	}
}



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


</script>
</body>
</html>