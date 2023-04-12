<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<style type="text/css">
body{
}
.button.green{
border:1px solid #F1F1F1;
box-shadow: 0 1px 2px #F1F1F1 inset,0 -1px 0 #F1F1F1 inset,0 -2px 3px #F1F1F1 inset;
background: -webkit-linear-gradient(top,#F1F1F1,#F1F1F1);
background: -moz-linear-gradient(top,#F1F1F1,#F1F1F1);
background: linear-gradient(top,#F1F1F1,#F1F1F1);
width: 70px;
height: 28px;
}
</style>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'">
	<form id="mainForm" method="post">
	<div class="datagrid-toolbar" style="height:auto">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" onclick="submitFrom()">保存</a>
		  	<span class="toolbar-item dialog-tool-separator"></span>
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="myrefresh()">保存并新建</a>
		   	<span class="toolbar-item dialog-tool-separator"></span>
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-old-versions" plain="true" onclick="copyContract()">保存并复制</a>
		    <span class="toolbar-item dialog-tool-separator"></span>
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="okPass()">审核</a>
 		    <span class="toolbar-item dialog-tool-separator"></span>
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-delete" plain="true" onclick="noPass()">取消审核</a>
 		 <!--    <span class="toolbar-item dialog-tool-separator"></span>  -->
	</div>
	<table class="formTable">
		<tr>
			<td>方案名称</td>
			<td>
				<input type="hidden" id="schemeNum" name="schemeNum" value="${scheme.schemeNum }"/>
				<input id="schemeName" name="schemeName" class="easyui-validatebox" data-options="width: 200, required:'required'" value="${scheme.schemeName }" />
			</td>
			<td>客户</td>
			<td>
				<input id="customsName" name="customsName" type="hidden"></input>
				<select id="customsId" name="customsId" class="easyui-combobox" data-options="width: 200" >
				</select>
			</td>
		</tr>
		<tr>
			<td>合同号</td>
			<td>
				<input type="hidden" id="contractNum" name="contractNum" value="${scheme.contractId }"/>
				<select class="easyui-combobox" id="contractId" name="contractId" data-options="width:200, required:'required'" >
				</select>
			</td>
			<td colspan="2">
			  	<input type="radio" id="get" name="ifGet" value="1"  checked /><label for="get">应收</label>
				<input type="radio" id="post" name="ifGet" value="2" /><label for="post">应付</label>
			</td>
		</tr>
		<!--<tr>
			<td>方案类型</td>
			<td>
				<select class="easyui-combobox" id="programType" name="programType" data-options="width:200, required:'required'">
					<option value=""></option>
				</select>
			</td>
			<td>业务类型</td>
			<td>
				<select class="easyui-combobox" id="bisType" name="bisType" data-options="width:200, required:'required'">
					<option value=""></option>
				</select>
			</td>
		</tr>-->
		<tr>
			<td>录入员</td>
			<td>
				<input id="operatorPerson" name="operatorPerson" class="easyui-validatebox" data-options="width: 200" value="${scheme.operatorPerson }" style="background-color:#EBEBE4" readonly/>
			</td>
			<td>录入时间</td>
			<td>
				<input id="operateTime" name="operateTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width: 200" value="<fmt:formatDate value="${scheme.operateTime}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/>
			</td>
		</tr>
		<tr>
			<td>状态</td>
			<td>
				<input id="programStateName" name="programStateName" class="easyui-validatebox" data-options="width: 200" style="background-color:#EBEBE4"
					value="<c:choose><c:when test="${scheme.programState == 1}">审核 </c:when><c:otherwise>未审核 </c:otherwise></c:choose>" readonly />
				<input type="hidden" id="programState" name="programState" value="<c:choose><c:when test="${scheme.programState == '1'}">1</c:when><c:otherwise>0</c:otherwise></c:choose>"/>
			</td>
		</tr>
		<tr>
			<td>备注</td>
			<td colspan="3">
				<textarea rows="3" cols="55" name="remark" style="font-size: 12px; font-family: '微软雅黑'">${scheme.remark }</textarea>
			</td>
		</tr>
		<tr>
			<td>方案共享客户</td>
			<td>
				<input type="hidden" id="clientIds" name="shareIds" value="" />
				<input id="shareClient"  data-options="width:200" class="easyui-validatebox" style="background:#eee"   readonly />
			</td>
			<td>
				&nbsp&nbsp<button type="button" onclick="getClient()" class="button green" >客户选择</button>
			</td>
		</tr>
	</table>
</form>
</div>
<div data-options="region:'south',split:true,border:false,title:'费用方案明细'" style="height: 320px;">
	<div id="tb" style="padding:5px;height:auto">
		<div>
<!-- 	    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addInfo()">新建</a> -->
<!-- 			<span class="toolbar-item dialog-tool-separator"></span> -->
<!-- 			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="updInfo()">修改</a> -->
<!-- 			<span class="toolbar-item dialog-tool-separator"></span> -->
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="delInfo()">删除</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-old-versions" plain="true" onclick="toCopyInfo()">复制</a>
	   		<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="payIt()">垫付</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="nopayIt()">取消垫付</a>
	    </div>
	</div>
	<table id="dg" ></table>
	<div id="dlg" ></div>
	<div id="dlshare" ></div>
</div>
<div data-options="region:'east',split:true,border:false,title:'合同费目列表'" style="width: 480px">
	<div id="tg_tb" style="padding:5px;height:auto">
	  	<div>
	  		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-check" plain="true" onclick="batchAdd()">批量添加</a>
	    </div>
	</div>
  	<table id="dgCost"></table>
</div>

<script type="text/javascript">
var dg;
var d;
var dgCost;
var dshare;
var a;
var b=0;

$(function(){
	var controller = '${controller}';
	if(controller == "updateScheme"){
		$("input[name='ifGet'][value=${scheme.ifGet}]").attr("checked",true);
		window.setTimeout(function(){getContract2()},1100);
		a=1;
	}else{
		a=0;
	}
	selectcom();
	contractFee($('#contractNum').val());
	gridDG();
	//修改和查看时显示共享此费用方案的客户
	if(controller != "createScheme"){
		$.ajax({
			type : "POST",
			url : "${ctx}/base/scheme/getshare/" + $("#schemeNum").val(),
			dataType : "json",
			success : function(date) {
	 			$("#clientIds").val(date[0]);
	 			$("#shareClient").val(date[1]);
			}
		});
	}
});

function selectcom(){
	//客户
 	var getstockId='${scheme.customsId}';
	   $('#customsId').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?setid=${scheme.customsId}",
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
	   	   $('#contractId').combobox("clear");
	   	   window.setTimeout(function(){getContract()},800);
	   },
	   onSelect:function(){
	   		a=1;
	   		b=1;
	   },
	   onLoadSuccess:function(){
			if(getstockId!=null && getstockId!=""){
				 $('#customsId').combobox("select",getstockId);
				 getstockId="";
			}
	   }
   	});


	//方案类型  下拉
	$.ajax({
	   	type: "GET",
	   	async : false,
		url: "${ctx}/system/dict/searchDict/"+"currencyType",
	   	data: "",
	   	dataType: "json",
	   	success: function(date){
	   		for (var i = 0; i < date.length; i++) {
				$('#programType').combobox({
					data: date,
					value: '${scheme.programType }',
					valueField: 'value',
					textField: 'label',
					editable: false
				});
			}
		}
	});

	//业务类型  下拉
	$.ajax({
		type: "GET",
		async : false,
		url: "${ctx}/system/dict/searchDict/"+"currencyType",
		data: "",
		dataType: "json",
		success: function(date) {
			for (var i = 0; i < date.length; i++) {
				$('#bisType').combobox({
					data: date,
					value: '${scheme.bisType }',
					valueField: 'value',
					textField: 'label',
					editable: false
				});
			}
		}
	});
}

//获得合同下拉
function getContract(){
	if(a==1){
		var customsId = $("#customsId").combobox("getValue");
		$.ajax({
			   type: "GET",
			   async : false,
			   url: "${ctx}/base/contract/getContractAll",
			   data: {"customsId":customsId},
			   dataType: "json",
			   success: function(date){
			   	   if(date.length != 0){
					   for(var i=0; i<date.length; i++){
						   $('#contractId').combobox({
							   data: date,
							   valueField: 'contractNum',
							   textField: 'contractNum',
							   editable:false,
							   onChange: function (newVal, oldVal){
								   contractFee(newVal);
							   }
						   });
					   }
				   }else{
				   		$('#contractId').combobox({
							   data: date,
							   valueField: 'contractNum',
							   textField: 'contractNum',
							   editable:false,
							   onChange: function (newVal, oldVal){
								   contractFee(newVal);
							   }
						   });
				   }
			   }
		})
	}
}

//获得合同下拉(修改页面打开时)
function getContract2(){
		var customsId = '${scheme.customsId}';
		$.ajax({
			   type: "GET",
			   async : false,
			   url: "${ctx}/base/contract/getContractAll",
			   data: {"customsId":customsId},
			   dataType: "json",
			   success: function(date){
					   for(var i=0; i<date.length; i++){
						   $('#contractId').combobox({
							   data: date,
							   valueField: 'contractNum',
							   textField: 'contractNum',
							   editable:false,
							   onChange: function (newVal, oldVal){
								   contractFee(newVal);
							   }
						   });
					   }
			   }
		})
		$('#contractId').combobox("setValue",'${scheme.contractId }');

}

//查询合同费目
function contractFee(val) {
	if (val == "") {
		val = "0";
	}
	//查询费目
	dgCost = $('#dgCost').datagrid({
		method : "GET",
		url : '${ctx}/base/contractInfo/json/' + val,
		fit : true,
		fitColumns : true,
		border : false,
		idField : 'id',
		striped : true,
		pagination : true,
		rownumbers : true,
		pageNumber : 1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect : false,
		columns : [ [
			{field: 'feeType', title: '费用类别', sortable: true, width: 100},
			{field: 'id', title: 'id', hidden: true},
		   	{field: 'expenseCode', title: '费目', sortable: true, width: 100},
		   	{field: 'cargoName', title: '名称', sortable: true, width: 100},
		   	{field: 'price', title: '价格', sortable: true, width: 100 },
            {field: 'remark', title: '备注', sortable: false, width: 200},
			{field: 'billUnit',title: '计量单位', sortable: true,width: 100},
            {field: 'currencyType', title: '币种', sortable: true, width: 100}
		]],
		onDblClickRow:function(rowIndex,rowData){
			batchAdd2(rowIndex,rowData);
        },
		enableHeaderClickMenu: true,
		enableHeaderContextMenu: true,
		enableRowContextMenu: false,
		toolbar: '#tg_tb'
	});
}

//保存
function submitFrom() {
	$("#customsName").val($("#customsId").combobox("getText"));
	if ($("#mainForm").form('validate')) {
		//用ajax提交form
		$.ajax({
			async: false,
			type: 'POST',
			url: "${ctx}/base/scheme/createScheme",
			data: $('#mainForm').serialize(),
			dataType: "text",
			success: function(data) {
				if (data == "success") {
					parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight"});
				}
			}
		});
	}
}

//查询方案明细
function gridDG(){
	var schemeNum = $('#schemeNum').val();

	dg = $('#dg').datagrid({
		method: "GET",
	    url:'${ctx}/base/schemeInfo/json/' + schemeNum,
	    fit: true,
		fitColumns: true,
		border: false,
		striped: true,
		pagination: true,
		rownumbers: true,
		pageNumber: 1,
		pageSize: 20,
		pageList: [ 10, 20, 30, 40, 50 ],
		remoteSort:false,
		singleSelect:false,
	    columns:[[
			{field:'id',title:'id',hidden:true}, 
			{field:'feeName',title:'费目',sortable:true,width:100},    
			{field:'unit',title:'价格',sortable:true,width:100},
			{field:'currency',title:'币种',hidden:true},
			{field:'currencyName',title:'币种',sortable:true,width:100},
			{field:'minPrice', title:'下限', sortable:true, width:100, editor: "text"},
			{field:'maxPrice', title:'上限', sortable:true, width:100, editor: "text"},
			{field:'termAttribute', title:'条件属性',hidden:true},
			{field:'termattributeName', title:'条件属性',sortable:true, width:100},
			{field:'billing', title:'计费方式', sortable:true,width:10,hidden:true},
			{field:'unitName', title:'计费方式', sortable:true, width:100},
			{field:'ifPay',title:'是否垫付',sortable:true,width:100,
			 	formatter : function(value, row, index) {
			 		if(value == 1){
				    	return '是';
				    }else{
				      	return '否';
				    }
				}
			},  
			{field:'gearCode', title:'档位代码', sortable:true, width:150, editor: "text"},
			{field:'gearExp', title:'档位说明', sortable:true, width:150, editor: "text"}
	    ]],
	    onDblClickRow:function(rowIndex,rowData){
	    	var ids= [];
			ids.push(rowData.id);
			$.ajax({
				type: 'get',
				url: "${ctx}/base/schemeInfo/deleteSchemeInfo/" + ids,
				success: function(data){
					successTip(data, dg);
				}
			});
	    },
	    toolbar:'#tb'
	});
}

//修改  方案明细
function sureUpd(row){
	if(isNaN(row.minPrice) || isNaN(row.maxPrice)){
		parent.$.messager.show({title: "提示", msg: "下限值或上限值不是数字！", position: "bottomRight"});
		gridDG();
	}else{
		if(row.minPrice > row.maxPrice){
			parent.$.messager.show({title: "提示", msg: "下限值不能大于上限值！", position: "bottomRight"});
			gridDG();
		}else{
			parent.$.messager.confirm('提示', '确定要修改方案明细？', function(data){
				if (data){
					$.ajax({
						type: 'GET',
						url: "${ctx}/base/schemeInfo/"+ row.id +"/updateSchemeInfo",
						data: "minPrice=" + row.minPrice + "&maxPrice="+  row.maxPrice + "&termAttribute="+ row.termAttribute +"&gearCode="+  row.gearCode +"&gearExp="+ row.gearExp,
						success: function(data){
							if(data == 'success'){
								parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
							}
							gridDG();
						}
					});
				}
			});
		}
	}
}

//修改 方案 明细参数
function updInfo(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	var rowIndex = dg.datagrid('getRowIndex', row);
	dg.datagrid('beginEdit', rowIndex);
}

//新建合同
function myrefresh(){
	if($("#mainForm").form('validate')){
		parent.$.messager.confirm('提示', '您确定要保存现有合同新建新合同吗？', function(data){
			if (data){
				var controller = '${controller}';
				if(controller == 'createScheme'){
					$.ajax({
				  		async: false,
				  		type: 'POST',
				  		url: "${ctx}/base/scheme/createScheme",
				  		data: $('#mainForm').serialize(),
				  		dataType: "text",
				  		success: function(data){
				  			if(data == "success"){
				  				window.parent.mainpage.mainTabs.refCurrentTab();
				  			}
				  		}
				  	});
				}else if(controller == 'updateScheme'){
					window.parent.mainpage.mainTabs.addModule('方案录入','base/scheme/createSchemeForm')
				}
			}
		});
	}
}

//批量保存明细
function batchAdd(){
	var schemeNum = $('#schemeNum').val();

	var row = dgCost.datagrid('getSelected');
	if(row == null) {
		parent.$.messager.show({title: "提示", msg: "请选择费目！", position: "bottomRight" });
		return;
	}
	var ids = row.id;
	parent.$.messager.confirm('提示', '确认添加？', function(data){
		if (data){
			var newIdsList = [];
			var datas = dgCost.treegrid('getSelections');
			for(var i=0; i < datas.length; i++){
				newIdsList.push(datas[i].id);
			}
			$.ajax({
				async: false,
				type: 'POST',
				data: JSON.stringify(newIdsList),
				contentType: 'application/json;charset=utf-8',
				url: "${ctx}/base/schemeInfo/"+ schemeNum +"/createSchemeInfoBatch",
				success: function(data){
					gridDG();
					parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight" });
				}
			});
		}
	});
}

//双击保存
function batchAdd2(rowIndex,rowData){
	var schemeNum = $('#schemeNum').val();
	var ids = rowData.id;
//	parent.$.messager.confirm('提示', '确认添加？', function(data){
//		if (data){
			var newIdsList = [];
			newIdsList.push(ids);
			$.ajax({
				async: false,
				type: 'POST',
				data: JSON.stringify(newIdsList),
				contentType: 'application/json;charset=utf-8',
				url: "${ctx}/base/schemeInfo/"+ schemeNum +"/createSchemeInfoBatch",
				success: function(data){
					gridDG();
					parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight" });
				}
			});
//		}
//	});
}

//删除明细
function delInfo(){
	var rows = dg.datagrid('getSelections');
	var del = dg.datagrid('getSelected');
	if(del == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	var ids= [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].id);
	}
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type: 'get',
				url: "${ctx}/base/schemeInfo/deleteSchemeInfo/" + ids,
				success: function(data){
					dg.datagrid('clearSelections');
					successTip(data, dg);
				}
			});
		}
	});
}

//复制
function toCopyInfo(){
	var row = dg.datagrid('getSelected');
	if(row == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	parent.$.messager.confirm('提示', '您确定要复制该合同？', function(data){
		if (data){
			$.ajax({
				type: 'get',
				url: "${ctx}/base/schemeInfo/copySchemeInfo/" + row.id,
				success: function(data){
					successTip(data, dg);
				}
			});
		}
	});
}

//费用方案明细 弹窗增加
function addInfo() {
	var schemeNum = $('#schemeNum').val();
	d = $("#dlg").dialog({
    	title: '新增费目明细',
	    width: 380,
	    height: 380,
	    href:'${ctx}/base/schemeInfo/createSchemeFeeCodeForm',
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				if($("#mainFormFeeCode").form('validate')){//验证
					//用ajax提交form
					$.ajax({
				  		async: false,
				  		type: 'POST',
				  		url: "${ctx}/base/schemeInfo/createSchemeInfoFeeCode/"+ schemeNum,
				  		data: $('#mainFormFeeCode').serialize(),
				  		dataType: "text",
				  		success: function(data){
				  			gridDG();
				  			parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });
				  			d.panel('close');

				  		}
				  	});
				}
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	});
}

//复制合同（保存并复制）
function copyContract(){
	if($("#mainForm").form('validate')){
		parent.$.messager.confirm('提示', '您确定要保存并复制新合同吗？', function(data){
			if (data){
				//用ajax提交form
				$.ajax({
			  		async: false,
			  		type: 'POST',
			  		url: "${ctx}/base/scheme/createCopyScheme",
			  		data: $('#mainForm').serialize(),
			  		dataType: "text",
			  		success: function(data){
			  			$('#schemeNum').val(data);
			  			parent.$.messager.show({ title : "提示",msg: "保存并复制成功！", position: "bottomRight" });
			  		}
			  	});
			}
		});
	}
}

//审核通过
function okPass(){
	var schemeNum = $('#schemeNum').val();
	parent.$.messager.confirm('提示', '您确定要审核通过？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/base/scheme/passOkScheme/" + schemeNum,
				success: function(data){
					if(data == "success"){
						window.parent.mainpage.mainTabs.refCurrentTab();//刷新TAB
					}
				}
			});
		}
	});
}

//取消审核
function noPass(){
	var schemeNum = $('#schemeNum').val();
	parent.$.messager.confirm('提示', '您确定要取消审核通过？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/base/scheme/passNoScheme/" + schemeNum,
				success: function(data){
					window.parent.mainpage.mainTabs.refCurrentTab();//刷新TAB
				}
			});
		}
	});
}

//获取共享费用方案的客户列表
function getClient(){
	var customsId = $("#customsId").combobox("getValue");
	if(customsId != "" && typeof(customsId) != "undefined"){
		dshare=$("#dlshare").dialog({
			    title: '费用选择',
			    width: 700,
			    height: 340,
			    href:'${ctx}/base/scheme/getclient/'+customsId,
			    maximizable:true,
			    modal:true,
			    buttons:[{
					text:'增加',
					handler:function(){
		               abc();
					}
				},{
					text:'取消',
					handler:function(){
						dshare.panel('close');
					}
				}]
			});
	}else{
		parent.$.messager.show({ title : "提示",msg: "请先选择客户！", position: "bottomRight" });
		return;
	}
}

function abc(){
	var clientId= $('#customsId').combobox("getValue");
	var clientList = new Array();
	var i = 1;
	var j = 0;
	//遍历
	for(i=1;i<parseInt(num)+1;i++){
	   if($("input[id="+i+"][name='client']").is(":checked")){
	       clientList[j] = $("input[id="+i+"][name='client']").val();
	       j++;
	   }
	}
	var shareIds = clientList.join(",");
	$("#clientIds").val(shareIds);
	//回显客户名称
	$.ajax({
	 	async:false,
		type:'get',
		url:"${ctx}/base/scheme/hxname" ,
		data : {"shareIds":shareIds},
		dataType : "text",
		success: function(data){
			$("#shareClient").val(data);
	 		dshare.panel('close');
		}
	});
}

//垫付
function payIt(){
	var rows = dg.datagrid('getSelections');
	var del = dg.datagrid('getSelected');
	if(del == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	var ids= [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].id);
	}
	parent.$.messager.confirm('提示', '确定进行垫付操作？', function(data){
		if (data){
			$.ajax({
				type: 'get',
				url: "${ctx}/base/schemeInfo/pay/" + ids,
				success: function(data){
					successTip(data, dg);
				}
			});
		}
	});
}

//垫付
function nopayIt(){
	var rows = dg.datagrid('getSelections');
	var del = dg.datagrid('getSelected');
	if(del == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	var ids= [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].id);
	}
	parent.$.messager.confirm('提示', '确定进行取消垫付操作？', function(data){
		if (data){
			$.ajax({
				type: 'get',
				url: "${ctx}/base/schemeInfo/nopay/" + ids,
				success: function(data){
					successTip(data, dg);
				}
			});
		}
	});
}


</script>
</body>
</html>