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




			<input id="id" name="id" class="easyui-validatebox"  type="hidden"   value="${platformWorkTicket.id}">

			<input id="yyid" name="yyid" class="easyui-validatebox"  type="hidden"   value="${platformWorkTicket.yyid}">
			<input id="deletedFlag" name="deletedFlag" class="easyui-validatebox"  type="hidden"   value="${platformWorkTicket.deletedFlag}">

			<input type="hidden" id="createdTime" name="createdTime"   value="<fmt:formatDate value="${platformWorkTicket.createdTime}"  pattern="yyyy-MM-dd HH:mm:ss" />"/>


			<input id="asnTransNum" name="asnTransNum" class="easyui-validatebox"  type="hidden"   value="${platformWorkTicket.asnTransNum}">
			<input id="inOutBoundFlag" name="inOutBoundFlag" class="easyui-validatebox"  type="hidden"   value="${platformWorkTicket.inOutBoundFlag}">

		<%--<td>月台</td>
			<td>
				<input id="platformName" name="platformName" class="easyui-validatebox" data-options="width: 180" value="${platformUser.platformName}"  />

			</td>--%>

			<td>理货：</td>
			<td>
				<select id="tallyId" name="tallyId" class="easyui-combobox" data-options="width:180, required:'required'" >
				</select>
			</td>


		</tr>

		<tr>
			<td>现场叉车：</td>
			<td>
				<select id="forkliftSceneId" name="forkliftSceneId" class="easyui-combobox" data-options="width:180, required:'required'"  maxlength="30" >
				</select>
			</td>


		</tr>

		<tr>
			<td>楼上叉车：</td>
			<td>
				<input type="hidden" id="userName" name="userName" >
				<select id="forkliftUpId" name="forkliftUpId" class="easyui-combobox" data-options="width:180, required:'required'"  maxlength="30" >
				</select>
			</td>


		</tr>

		<tr>
			<td>装卸队：</td>
			<td>
				<select id="clientId" name="clientId" class="easyui-combobox" data-options="width:180"  editable="false" maxlength="30" >
				</select>
			</td>


		</tr>

		<tr>
			<td>装卸工：</td>
			<td>
				<input type="hidden" id="stevedoreName" name="stevedoreName" >
				<input type="hidden" id="stevedoreId" name="stevedoreId"/>
<%--
				<select id="yjrw" name="stevedoreId" class="easyui-combobox" data-options="width:180, required:'required'"  maxlength="30" > 				</select>

--%>
				<input class="easyui-combobox" id="yjrw" name="yjrw" type="text" style="width: 145px;">
			</td>


		</tr>

        <tr>
            <td>是否全人工：</td>
            <td>

                <select id="ifAllMan" name="ifAllMan" class="easyui-combobox" data-options="width: 50, required:'required'">
                    <option  value="1">是</option>
                    <option  value="0">否</option>

                </select>
            </td>


        </tr>
		<tr>
			<td>重量系数：</td>
			<td>

				<select id="numPlus" name="numPlus" class="easyui-combobox" data-options="width: 50, required:'required'">
					<option  value="1">1</option>
					<option  value="1.5">1.5</option>
					<option  value="2">2</option>
					<option  value="2.5">2.5</option>
					<option  value="3">3</option>
					<option  value="3.5">3.5</option>
				</select>
			</td>


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

	var numPlus='${platformWorkTicket.numPlus}';


    $('#numPlus').val(numPlus);

    var ifAllMan='${platformWorkTicket.ifAllMan}';


    $('#ifAllMan').val(ifAllMan);



	//操作员
	var tallyId='${platformWorkTicket.tallyId}';
	$('#tallyId').combobox({
		method:"GET",
		url:"${ctx}/cost/platformWorkTicket/findTally",
		valueField: 'id',
		textField: 'name',
		mode:'remote',
		onLoadSuccess:function(){
			if(tallyId!=null && tallyId!=""){
				$('#tallyId').combobox("select",tallyId);
				tallyId="";
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


	var forkliftSceneId='${platformWorkTicket.forkliftSceneId}';

	$('#forkliftSceneId').combobox({
		method:"GET",
		url:"${ctx}/cost/platformWorkTicket/findForklift",
		valueField: 'id',
		textField: 'name',
		mode:'remote',
		onLoadSuccess:function(){
			if(forkliftSceneId!=null && forkliftSceneId!=""){
				$('#forkliftSceneId').combobox("select",forkliftSceneId);
				forkliftSceneId="";
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
	var forkliftUpId='${platformWorkTicket.forkliftUpId}';


	$('#forkliftUpId').combobox({
		method:"GET",
		url:"${ctx}/cost/platformWorkTicket/findForklift",
		valueField: 'id',
		textField: 'name',
		mode:'remote',
		onLoadSuccess:function(){
			if(forkliftUpId!=null && forkliftUpId!=""){
				$('#forkliftUpId').combobox("select",forkliftUpId);
				forkliftUpId="";
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

				loadRwlbInfos(newVal);
				if(oldVal != ""){
					$("#yjrw").combobox("setValues", '');

				}
			}
		}
	});





	loadUI();//选择抽查类型
	loadRwlbInfos(clientId);//加载抽查类型
}
var stevedoreId='${platformWorkTicket.stevedoreId}';
//加载选择框的数据
function loadRwlbInfos(clientId) {
	if(clientId==null || clientId==""){
		return
	}


	$.get('${ctx}/cost/platformWorkTicket/findZxg/'+clientId, function(data) {

			var codedata = [];
			for (var i = 0; i < data.length; i++) {
				codedata.push({
					"text" : data[i].name,
					"id" : data[i].id
				});
			}
			$("#yjrw").combobox("loadData", codedata);
		/*	if (yjrw != '') {
				$("#yjrw").combobox("setValues", yjrw.split(','));
			} else {
				$("#yjrw").combobox("setValues", '');
			}*/
		if(stevedoreId!=null && stevedoreId!=""){
			console.log("stevedoreId....",stevedoreId.split(','))
			$('#yjrw').combobox("setValues",stevedoreId.split(','));
			$('#stevedoreId').val($('#yjrw').combobox('getValues'));
			$('#stevedoreName').val($('#yjrw').combobox('getText'));
			stevedoreId="";
		}

	}, 'json');
}
//选择
function loadUI(){


	$("#yjrw").combobox({
		valueField:'id',
		textField:'text',
		multiple:true,
		editable:false,
		formatter: function(row){
			var opts = $(this).combobox('options');
			return '<input type="checkbox" class="combobox-checkbox">' + row[opts.textField];
		},
		onSelect: function (row) {
			var options = $(this).combobox('options');
			var el = options.finder.getEl(this, row[options.valueField]);
			el.find('input.combobox-checkbox')._propAttr('checked', true);
			if(row.id!=''){
				$('#stevedoreId').val($('#yjrw').combobox('getValues'));
				$('#stevedoreName').val($('#yjrw').combobox('getText'));
			}
		},
		onUnselect: function (row) {
			console.log(row);
			var options = $(this).combobox('options');
			var el = options.finder.getEl(this, row[options.valueField]);
			el.find('input.combobox-checkbox')._propAttr('checked', false);
			if(row.id!=''){
				$('#stevedoreId').val($('#yjrw').combobox('getValues'));
				$('#stevedoreName').val($('#yjrw').combobox('getText'));
			}
		},
		onLoadSuccess:function(){

			/*	if (yjrw != '') {
				$("#yjrw").combobox("setValues", yjrw.split(','));
			} else {
				$("#yjrw").combobox("setValues", '');
			}*/
		},
	});
}

var result="";
//保存
function submitForm(){

	//$("#platformName").val( $("#platformId").combobox("getText") );

	//console.log($("#platformId").combobox("getData"));

	//$("#userName").val( $("#userId").combobox("getText") );
	$('#stevedoreId').val($('#yjrw').combobox('getValues'));
	$('#stevedoreName').val($('#yjrw').combobox('getText'));
	if($("#mainForm").form('validate')){




		//用ajax提交form
 		$.ajax({
 	  		async: false,
 	  		type: 'POST',
 	  		url: "${ctx}/cost/platformWorkTicket/"+action,
 	  		data: $('#mainForm').serialize(),

 	  		success: function(msg){
				result=msg;
 	  			if(msg == "success"){
 	  				parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });

 	  			}

 	  		}
 	  	});
 	}else{

		//parent.$.messager.alert("提示","必填项必填，请确认");
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