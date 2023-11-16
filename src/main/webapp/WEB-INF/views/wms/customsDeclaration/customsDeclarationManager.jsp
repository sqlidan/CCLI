<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title></title>
	<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">


<div data-options="region:'center'">
	<form id="mainForm">
		<div style="height:auto" class="datagrid-toolbar">
			<shiro:hasPermission name="wms:customsDeclaration:edit">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true"
				   data-options="disabled:false" onclick="submitForm()">保存</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
		</div>
		<table class="formTable">
			<tr>
				<td>ID：</td>
				<td>
					<input type="text" id="forId" name="forId" class="easyui-validatebox"
						   value="${bsCustomsDeclaration.forId}" data-options="width:180, required:'required'"
						   readonly style="background:#eee">
				</td>
				<td>核注清单号：</td>
				<td>
					<input type="text" id="checkListNo" name="checkListNo" class="easyui-validatebox"
						   value="${bsCustomsDeclaration.checkListNo}"
						   data-options="width:180, required:'required'" readonly style="background:#eee">
				</td>
				<td>报关单号：</td>
				<td>
					<input type="text" id="cdNum" name="cdNum" class="easyui-validatebox"
						   value="${bsCustomsDeclaration.cdNum}" data-options="width:180, required:'required'">
				</td>
				<td>申报日期：</td>
				<td>
					<input id="sbTime" name="sbTime" class="easyui-my97" data-options="width:180"
						   datefmt="yyyy-MM-dd HH:mm:ss"
						   value="<fmt:formatDate value="${sbTime}" pattern="yyyy-MM-dd HH:mm:ss" />"/>
				</td>
				<td>提运单号：</td>
				<td>
					<input id="billNum" name="billNum" type="text" class="easyui-validatebox"
						   data-options="width:180" maxlength="17" value="${bsCustomsDeclaration.billNum}">
				</td>
			</tr>
			<tr>
				<td>服务项目：</td>
				<td>
					<select id="serviceProject" name="serviceProject" class="easyui-combobox"
							data-options="width: 180">
						<option value="0">报进</option>
						<option value="1">报出</option>
					</select>
				</td>
				<td>贸易方式：</td>
				<td>
					<select id="tradeMode" name="tradeMode" class="easyui-combobox" data-options="width:180">
					</select>
				</td>
				<td>消费使用单位：</td>
				<td>
					<input type="hidden" id="clientName" name="clientName">
					<select id="clientId" name="clientId" class="easyui-combobox" data-options="width:180">
					</select>
				</td>
				<td>申报单位：</td>
				<td>
					<input type="hidden" id="declarationUnit" name="declarationUnit">
					<select id="declarationUnitId" name="declarationUnitId" class="easyui-combobox"
							data-options="width:180" maxlength="30">
					</select>
				</td>
				<td>货物存放地点：</td>
				<td>
					<input id="storagePlace" name="storagePlace" type="text" class="easyui-validatebox"
						   data-options="width:180" maxlength="100"
						   value="${bsCustomsDeclaration.storagePlace}">
				</td>
			</tr>
			<tr>
				<td>件数：</td>
				<td>
					<input type="text" id="dty" name="dty" class="easyui-validatebox" data-options="width: 180"
						   value="${bsCustomsDeclaration.dty}" maxlength="10" oninput="ischeckNum(this)">
				</td>
				<td>毛重：</td>
				<td>
					<input type="text" id="grossWeight" name="grossWeight" class="easyui-validatebox"
						   data-options="width: 160" value="${bsCustomsDeclaration.grossWeight}" maxlength="10"
						   onkeyup="ischeckNum(this)">
					<span style="color: red">KG</span>
				</td>
				<td>净重：</td>
				<td>
					<input type="text" id="netWeight" name="netWeight" class="easyui-validatebox"
						   data-options="width: 160" value="${bsCustomsDeclaration.netWeight}" maxlength="10"
						   onkeyup="ischeckNum(this)">
					<span style="color: red">KG</span>
				</td>
			</tr>
			<tr>
				<td>收货人：</td>
				<td>
					<input type="text" id="consignee" name="consignee" class="easyui-validatebox"
						   data-options="width: 180" value="${bsCustomsDeclaration.consignee}" maxlength="50">
				</td>
				<td>发货人：</td>
				<td>
					<input type="text" id="consignor" name="consignor" class="easyui-validatebox"
						   data-options="width: 180" value="${bsCustomsDeclaration.consignor}" maxlength="50"
					>
				</td>
				<td>贸易国(PER)：</td>
				<td>
					<input type="text" id="myg" name="myg" class="easyui-validatebox" data-options="width: 180"
						   value="${bsCustomsDeclaration.myg}" maxlength="50" >
				</td>
				<td>启运国(PER)：</td>
				<td>
					<input type="text" id="qyg" name="qyg" class="easyui-validatebox" data-options="width: 180"
						   value="${bsCustomsDeclaration.qyg}" maxlength="50" >
				</td>
			</tr>
			<tr>
				<td>报关人：</td>
				<td>
					<input type="text" id="cdBy" name="cdBy" class="easyui-validatebox"
						   data-options="width: 180" value="${bsCustomsDeclaration.consignee}" maxlength="50">
				</td>
				<td>报关日期：</td>
				<td>
					<input id="cdTime" name="cdTime" class="easyui-my97" data-options="width:180"
						   datefmt="yyyy-MM-dd HH:mm:ss"
						   value="<fmt:formatDate value="${cdTime}" pattern="yyyy-MM-dd HH:mm:ss" />"/>
				</td>
			</tr>
		</table>
	</form>
</div>
<div data-options="region:'south',split:true,border:false" title="列表" style="height:600px">
	<div id="tb2" style="padding:5px;height:auto" class="datagrid-toolbar">
		<div>
			<shiro:hasPermission name="wms:customsDeclarationInfo:add">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
				   onclick="addInfo()">添加</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:customsDeclarationInfo:add">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editInfo()">修改</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:customsDeclarationInfo:delete">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
				   onclick="delInfo()">删除</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
		</div>
	</div>
	<table id="dg2"></table>
	<div id="dlg" ></div>
</div>


<script type="text/javascript">
	var dg;
	var dg2;
	var sfdj;
	var d;
	var dlg;
	var forId = "${bsCustomsDeclaration.forId}";
	var b = 0;
	var dhs;
	var dt;

	var bztAry;//币制
	var ycgAry;//原产国
	var jldwAry;//计量单位
	var zmfsAry;//征免方式
	var xgbzAry;//修改标志


	//初始化
	$(function () {
		// gridDG(forId);
		gridDG2(forId);
		selectAjax();
	});

	function selectAjax() {
		//表头
		//报关公司
		var getorg='${bsCustomsDeclaration.declarationUnitId}';
		$('#declarationUnitId').combobox({
			method:"GET",
			url:"${ctx}/base/client/getClientAll?setid=${bsCustomsDeclaration.declarationUnitId}",
			valueField: 'ids',
			textField: 'clientName',
			mode:'remote',
			onLoadSuccess:function(){
				if(getorg!=null && getorg!=""){
					$('#declarationUnitId').combobox("select",getorg);
					getorg="";
				}
			}
		});

		//物流容器
		$.ajax({
			type : "GET",
			async : false,
			url : "${ctx}/system/dict/json",
			data : "filter_LIKES_type=tradeType",
			dataType : "json",
			success : function(date) {
				$('#tradeMode').combobox({
					data : date.rows,
					value : '${bsCustomsDeclaration.tradeMode}',
					valueField : 'value',
					textField : 'label',
					editable : false
				});
			}
		});

		//客户
		var getstockId='${bsCustomsDeclaration.clientId}';
		$('#clientId').combobox({
			method:"GET",
			url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${bsCustomsDeclaration.clientId}",
			valueField: 'ids',
			textField: 'clientName',
			mode:'remote',
			onChange:function(){
				if(b==1){
					a=1;
					b=0;
				}else{
					a=0;
				}
			},
			onSelect:function(){
				a=1;
				b=1;
			},
			onLoadSuccess:function(){
				if(getstockId!=null && getstockId!=""){
					$('#clientId').combobox("select",getstockId);
					getstockId="";
				}
			}
		});
		$('#serviceProject').combobox('select','${bsCustomsDeclaration.serviceProject}');

		//表体
		//原产国/最终目的国
		$.ajax({
			type: "GET",
			async: false,
			url: "${ctx}/wms/preEntry/dictData/CUS_STSHIP_TRSARV_NATCD",
			success: function (date) {
				ycgAry = date.rows;
				$('#ycg').combobox({
					data: date.rows,
					value: '${bsCustomsDeclarationInfo.ycg}',
					valueField: 'value',
					textField: 'label',
					editable: false
				});
				$('#zzmdg').combobox({
					data: date.rows,
					value: '${bsCustomsDeclarationInfo.zzmdg}',
					valueField: 'value',
					textField: 'label',
					editable: false
				});
			}
		});
		//币种
		$.ajax({
			type: "GET",
			async: false,
			url: "${ctx}/wms/preEntry/dictData/CUS_DCLCURRCD",
			success: function (date) {
				bztAry = date.rows;
				$('#bzt').combobox({
					data: date.rows,
					value: '${bsCustomsDeclarationInfo.bzt}',
					valueField: 'value',
					textField: 'label',
					editable: false
				});
			}
		});
		//征免方式
		$.ajax({
			type: "GET",
			async: false,
			url: "${ctx}/wms/preEntry/dictData/CUS_LVYRLFMODECD",
			success: function (date) {
				zmfsAry = date.rows;
				$('#zmfs').combobox({
					data: date.rows,
					value: '${bsCustomsDeclarationInfo.zmfs}',
					valueField: 'value',
					textField: 'label',
					editable: false
				});
			}
		});
		//申报计量单位/法定计量单位/第二法定计量单位
		$.ajax({
			type: "GET",
			async: false,
			url: "${ctx}/system/dict/json",
			data: "filter_LIKES_type=unitOfWeight",
			dataType: "json",
			success: function (date) {
				jldwAry = date.rows;
				$('#sbjldw').combobox({
					data: date.rows,
					value: '${bsCustomsDeclarationInfo.sbjldw}',
					valueField: 'value',
					textField: 'label',
					editable: false
				});
			}
		});
	}

	//================================================================================================================================
	//查询预录入表体
	function gridDG2(forId) {
		dg2 = $('#dg2').datagrid({
			method: "get",
			url: '${ctx}/wms/customsDeclarationInfo/json/' + forId,
			fit: true,
			fitColumns: true,
			border: false,
			sortOrder: 'desc',
			striped: true,
			pagination: true,
			rownumbers: true,
			pageNumber: 1,
			pageSize: 20,
			pageList: [20, 50, 100],
			singleSelect: false,
			columns: [[
				{field: 'id', hidden: true},
				{field: 'xh', title: '项号', sortable: true},
				{field: 'accountBook', title: '账册商品序号', sortable: true},
				{field: 'spbh', title: '商品编号', sortable: true},
				{field: 'spmc', title: '商品名称', sortable: true},
				{field: 'ggxh', title: '规格型号', sortable: true},
				{field: 'sbsl', title: '申报数量', sortable: true},
				{
					field: 'sbjldw', title: '计量单位', sortable: true,
					formatter: function (value, row, index) {
						var lbelStr;
						for (let i = 0; i < jldwAry.length; i++) {
							let row = jldwAry[i];
							if (row.value == value) {
								lbelStr = row.label;
								break;
							}
						}
						return lbelStr;
					}
				},
				{field: 'qysbdj', title: '单价', sortable: true},
				{field: 'mytjzje', title: '总价(美元)', sortable: true},
				{
					field: 'bzt', title: '币制', sortable: true,
					formatter: function (value, row, index) {
						var lbelStr;
						for (let i = 0; i < bztAry.length; i++) {
							let row = bztAry[i];
							if (row.value == value) {
								lbelStr = row.label;
								break;
							}
						}
						return lbelStr;
					}
				},
				{
					field: 'ycg', title: '原产国(地区)', sortable: true,
					formatter: function (value, row, index) {
						var lbelStr;
						for (let i = 0; i < ycgAry.length; i++) {
							let row = ycgAry[i];
							if (row.value == value) {
								lbelStr = row.label;
								break;
							}
						}
						return lbelStr;
					}
				},
				{
					field: 'zzmdg', title: '最终目的国', sortable: true,
					formatter: function (value, row, index) {
						var lbelStr;
						for (let i = 0; i < ycgAry.length; i++) {
							let row = ycgAry[i];
							if (row.value == value) {
								lbelStr = row.label;
								break;
							}
						}
						return lbelStr;
					}
				},
				{field: 'jnmdd', title: '境内目的地', sortable: true},
				{
					field: 'zmfs', title: '征免方式', sortable: true,
					formatter: function (value, row, index) {
						var lbelStr;
						for (let i = 0; i < zmfsAry.length; i++) {
							let row = zmfsAry[i];
							if (row.value == value) {
								lbelStr = row.label;
								break;
							}
						}
						return lbelStr;
					}
				},
				{field: 'remark', title: '备注', sortable: true}
			]],
			enableHeaderClickMenu: true,
			enableHeaderContextMenu: true,
			enableRowContextMenu: false,
			toolbar: '#tb2'
		});
	}

	//================================================================================================================================
	//清空
	function clearIt() {
		$('#mainForm').form('clear');
	}

	//表头保存
	function submitForm() {
		$("#clientName").val( $("#clientId").combobox("getText") );
		$("#declarationUnit").val( $("#declarationUnitId").combobox("getText") );
		if ($("#mainForm").form('validate')) {
			//用ajax提交form
			$.ajax({
				async: false,
				type: 'POST',
				url: "${ctx}/wms/customsDeclaration/update",
				data: $('#mainForm').serialize(),
				dataType: "text",
				success: function (msg) {
					if (msg == "success") {
						parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight"});
					}
				}
			});
		}
	}

	//================================================================================================================================
	//表体添加
	function addInfo(){
		$.ajax({
			type:'get',
			url:"${ctx}/wms/customsDeclaration/ifsave/"+forId,
			dataType:"text",
			success: function(data){
				if(data == "success"){
					addInfoT();
				}else{
					parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存报关单！", position: "bottomRight"});
					return;
				}
			}
		});
	}
	//添加货物明细
	function addInfoT(){
		dlg=$("#dlg").dialog({
			title: '货物信息添加',
			width: 1300,
			height: 300,
			href:'${ctx}/wms/customsDeclarationInfo/create/'+forId,
			maximizable:true,
			modal:true,
			buttons:[{
				text:'确认',
				handler:function(){
					if($("#mainform2").form('validate')){
						$("#mainform2").submit();
						dlg.panel('close');
						parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight"});
						window.setTimeout(function () {
							gridDG2(forId)
						}, 100);
					}
				}
			},{
				text:'取消',
				handler:function(){
					dlg.panel('close');
				}
			}],
			onClose: function (){
				window.setTimeout(function(){gridDG2(forId)},100);
			}
		});
	}
	//修改
	function editInfo(){
		var row = dg2.datagrid('getSelected');
		if(row == null) {
			parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
			return;
		}
		if(row.id == null ||row.id =='') {
			parent.$.messager.show({title: "提示", msg: "选中的记录没有保存，不能修改！", position: "bottomRight" });
			return;
		}
		dlg=$("#dlg").dialog({
			title: '修改货物信息',
			width: 1300,
			height: 300,
			href:'${ctx}/wms/customsDeclarationInfo/update/'+row.id,
			maximizable:true,
			modal:true,
			buttons:[{
				text:'确认',
				handler:function(){
					if($("#mainform2").form('validate')){
						$("#mainform2").submit();
						dlg.panel('close');
						parent.$.messager.show({title: "提示", msg: "修改成功！", position: "bottomRight"});
						window.setTimeout(function () {
							gridDG2(forId)
						}, 100);
					}
				}
			},{
				text:'取消',
				handler:function(){
					dlg.panel('close');
				}
			}],
			onClose: function (){
				window.setTimeout(function(){gridDG2(forId)},100);
			}
		});
	}

	//表体删除
	function delInfo() {
		var rows = dg2.datagrid('getSelections');
		var del = dg2.datagrid('getSelected');
		if (del == null) {
			parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight"});
			return;
		}
		var ids = [];
		for (var i = 0; i < rows.length; i++) {
			ids.push(rows[i].id);
		}
		parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
			if (data) {
				$.ajax({
					type: 'get',
					url: "${ctx}/wms/customsDeclarationInfo/deleteinfo/" + ids,
					success: function (data) {
						dg2.datagrid('clearSelections');
						successTip(data, dg2);
					}
				});
			}
		});
	}
</script>
</body>
</html>