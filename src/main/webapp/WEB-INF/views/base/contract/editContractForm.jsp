 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
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
<!-- 	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="okPass()">审核</a> -->
<!-- 	    <span class="toolbar-item dialog-tool-separator"></span> -->
<!-- 	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-delete" plain="true" onclick="noPass()">取消审核</a> -->
<!-- 	    <span class="toolbar-item dialog-tool-separator"></span> -->
	</div>
	<table class="formTable">
		<tr>
			<td>合同号</td>
			<td>
				<input id="contractNum" name="contractNum" class="easyui-validatebox" data-options="width: 200" value="${contract.contractNum }" style="background-color:#EBEBE4" readonly/> 
			</td>
			<td>状态</td>
			<td>
				<input id="contractStateName" name="contractStateName" class="easyui-validatebox" data-options="width: 200" style="background-color:#EBEBE4"
					value="<c:choose><c:when test="${contract.contractState == 1}">审核 </c:when><c:otherwise>未审核 </c:otherwise></c:choose>" readonly />
				<input type="hidden" id="contractState" name="contractState" value="<c:choose><c:when test="${contract.contractState == '1'}">1</c:when><c:otherwise>0</c:otherwise></c:choose>"/>
			</td>
		</tr>
		<tr>
			<td>客户</td>
			<td>
				<select class="easyui-combobox" id="clientId" name="clientId" data-options="width: 200, required:'required'">
					<option value=""></option>
				</select>
			</td>
			<td>揽货人</td>
			<td>
				<input id="canvassionPerson" name="canvassionPerson" class="easyui-validatebox" data-options="width: 200, required:'required'" value="${contract.canvassionPerson}" /> 
			</td>
		</tr>
		<tr>
			<td>签订时间</td>
			<td>
				<input id="signTime" name="signTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width: 200, required:'required'" 
					value="<fmt:formatDate value="${contract.signTime}" pattern="yyyy-MM-dd HH:mm:ss" />" readonly /> 
			</td>
			<td>到期时间</td>
			<td>
				<input id="expirationTime" name="expirationTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width: 200, required:'required'" 
					value="<fmt:formatDate value="${contract.expirationTime}" pattern="yyyy-MM-dd HH:mm:ss" />" readonly /> 
			</td>
		</tr>
		<tr>
			<td>录入员</td>
			<td>
				<input id="operatorPerson" name="operatorPerson" class="easyui-validatebox" data-options="width: 200" value="${contract.operatorPerson }" style="background-color:#EBEBE4" readonly/> 
			</td>
			<td>录入时间</td>
			<td>
				<input id="operateTime" name="operateTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width: 200" value="<fmt:formatDate value="${contract.operateTime}"  pattern="yyyy-MM-dd HH:mm:ss" />" disabled/>
			</td>
		</tr>
		<tr>
			<td>报价类型</td>
			<td>
				<select class="easyui-combobox" id="ifMan" name="ifMan" data-options="width: 200" >
					<option value=""></option>
				</select>			
			</td>
		</tr>
		<tr>
			<td>备注</td>
			<td colspan="3">
				<textarea rows="3" cols="55" name="remark" style="font-size: 12px; font-family: '微软雅黑'">${contract.remark }</textarea>
			</td>
		</tr>
	</table>
</form>
</div>
<div data-options="region:'south',split:true,border:false,title:'合同费目明细'" style="height:300px;">
	<div id="tb" style="padding:5px;height:auto">
		<div>
<!-- 	    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addInfo()">新建</a> -->
<!-- 			<span class="toolbar-item dialog-tool-separator"></span> -->
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="updInfo()">修改</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="delInfo()">删除</a>
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-old-versions" plain="true" onclick="toCopyInfo()">复制</a>
	    </div>
	</div>
	<table id="dg" ></table> 
	<div id="dlg" ></div> 
</div>
<div data-options="region:'east',split:true,border:false,title:'费目列表'" style="width: 480px">
	<div id="tg_tb" style="padding:5px;height:auto">
		<form id="codeFrom" action="">
      	    <input type="text" name="filter_code" class="easyui-validatebox" data-options="width:150,prompt: '费目代码'"/>
      	    <input type="text" name="filter_nameC" class="easyui-validatebox" data-options="width:150,prompt: '费目名称'"/>
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="codex()">查询</a>
		</form>
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

document.onkeydown = function () {if(event.keyCode == 13){codex();}};

//保存
function submitFrom(){
	if($("#mainForm").form('validate')){
		//判断  开始时间与结束时间
		var start = new Date(Date.parse($("input[name='signTime']").val()));  
		var end =  new Date(Date.parse($("input[name='expirationTime']").val()));
		
		if(start.getTime() >= end.getTime()){
			parent.$.messager.show({title: "提示", msg: "签订时间不得大于等于到期时间！", position: "bottomRight" });
		}else{
			//用ajax提交form
	 		$.ajax({
	 	  		async: false,
	 	  		type: 'POST',
	 	  		url: "${ctx}/base/contract/createContract",
	 	  		data: $('#mainForm').serialize(),
	 	  		dataType: "text",  
	 	  		success: function(data){
	 	  			if(data == "success"){
	 	  				parent.$.messager.show({title: "提示",msg: "保存成功！", position: "bottomRight" });
// 	 	  				setTimeout(function (){
// 	 	  					window.parent.mainpage.mainTabs.closeCurrentTab();
// 	 	  				}, 1000);
	 	  			}
	 	  		}
	 	  	});
		}
	}
}

//新建合同
function myrefresh(){
	if($("#mainForm").form('validate')){
		//判断  开始时间与结束时间
		var start = new Date(Date.parse($("input[name='signTime']").val()));  
		var end =  new Date(Date.parse($("input[name='expirationTime']").val()));
		
		if(start.getTime() >= end.getTime()){
			parent.$.messager.show({title: "提示", msg: "签订时间不得大于等于到期时间！", position: "bottomRight" });
		}else{
			parent.$.messager.confirm('提示', '您确定要保存现有合同新建新合同吗？', function(data){
				if (data){
					var controller = '${controller}';
					if(controller == 'createContract'){
						$.ajax({
					  		async: false,
					  		type: 'POST',
					  		url: "${ctx}/base/contract/createContract",
					  		data: $('#mainForm').serialize(),
					  		dataType: "text",  
					  		success: function(data){
					  			if(data == "success"){
					  				window.parent.mainpage.mainTabs.refCurrentTab();
					  			}
					  		}
					  	});
					}else if(controller == 'updateContract'){
						window.parent.mainpage.mainTabs.addModule('合同录入','base/contract/createContractForm')
					}
				} 
			});
		}
	}
}

//复制合同（保存并复制）
function copyContract(){
	if($("#mainForm").form('validate')){
		//判断  开始时间与结束时间
		var start = new Date(Date.parse($("input[name='signTime']").val()));  
		var end =  new Date(Date.parse($("input[name='expirationTime']").val()));
		
		if(start.getTime() >= end.getTime()){
			parent.$.messager.show({title: "提示", msg: "签订时间不得大于等于到期时间！", position: "bottomRight" });
		}else{
			parent.$.messager.confirm('提示', '您确定要保存并复制新合同吗？', function(data){
				if (data){
					//用ajax提交form
					$.ajax({
				  		async: false,
				  		type: 'POST',
				  		url: "${ctx}/base/contract/createCopyContract",
				  		data: $('#mainForm').serialize(),
				  		dataType: "text",  
				  		success: function(data){
				  			$('#contractNum').val(data);
				  			parent.$.messager.show({ title : "提示",msg: "保存并复制成功！", position: "bottomRight" });
				  		}
				  	});
				}
			});
		}
	}
}

$(function(){   
	//客户下拉
	var client = '${contract.clientId}';
	$('#clientId').combobox({
	   method: "GET",
	   url: "${ctx}/base/client/getClientAll?setid=${contract.clientId}",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode: 'remote',
	   onChange: function (newVal, oldVal){
					   getSaler(newVal);
				   },
	   onLoadSuccess:function(){
			if(client != null && client != ""){
				$('#clientId').combobox("select", client);
				client = "";
			}
		}
	});
	conType();
	gridDgCost();
	gridDG();
});

//合同报价类型
function conType(){
	$.ajax({
	   type: "GET",
	   url: "${ctx}/system/dict/getjson",
	   data: "filter_LIKES_type=conType",
	   dataType: "json",
	   success: function (date) {
                $('#ifMan').combobox({
                    data: date.rows,
                    value: '${contract.ifMan}',
                    valueField: 'value',
                    textField: 'label',
                    editable: false
                });
            }
	})
}

//获取客户的揽货人
function getSaler(newVal){
	if( null != newVal && ""!=newVal && typeof(newVal)!="undefined" && newVal != $('#clientId').combobox("getText")){
		$.ajax({
					type: 'get',
					url: "${ctx}/base/contract/getSaler/" + newVal,
					dataType: "text",  
					success: function(data){
						$("#canvassionPerson").val(data);
					}
		  		});
	}
}

function gridDgCost(){
	//查询费目
	dgCost = $('#dgCost').datagrid({    
		method: "GET",
	    url:'${ctx}/base/feecode/json/', 
	    fit : true,
	    idField : 'ID',
		fitColumns : true,
		border : false,
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [10, 20, 30, 40, 50 ],
		singleSelect:false,
	    columns:[[    
			{field:'ID',title:'ID',hidden:true}, 
			{field:'FEETYPELABLE',title:'费用类别',sortable:true},
	        {field:'CODE',title:'费目代码',sortable:true,width:100},    
	        {field:'NAME_C',title:'名称',sortable:true,width:100},
 	        {field:'PRICE_BASE',title:'价格',sortable:true,width:100},
 	        {field:'MIN_PRICE',title:'下限',sortable:true,width:100},
 	        {field:'MAX_PRICE',title:'上限',sortable:true,width:100},
	        {field:'UNITLABLE',title:'计量单位',sortable:true,width:100},
	        {field:'TERMLABLE',title:'条件属性',sortable:true,width:100},
	        {field:'CURRENCYLABLE',title:'币种',sortable:true,width:100} 
	    ]],
	    onDblClickRow:function(rowIndex,rowData){  
			batchAdd2(rowIndex,rowData);
        }, 
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tg_tb'
	});
}

//创建查询对象并查询
function codex(){
	var obj=$("#codeFrom").serializeObject();
	dgCost.datagrid('load',obj); 
}

//查询合同费目明细
function gridDG(){
	var contractNum = $('#contractNum').val();
	dg = $('#dg').datagrid({    
		method: "GET",
	    url:'${ctx}/base/contractInfo/json/' + contractNum, 
	    fit : true,
		fitColumns : true,
		border : false,
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:false,
	    columns:[[    
			{field:'id', title:'id',hidden:true},
			{field:'unit', title:'unit',hidden:true},
			{field:'term', title:'term',hidden:true},
	        {field:'expenseCode',title:'费目',sortable:true,width:100},    
	        {field:'cargoName',title:'费目名称',sortable:true,width:100},
	        {field:'price',title:'价格',sortable:true,width:100,editor:{type:'numberbox',options:{precision:4}}},
	        {field:'onSale',title:'折扣',sortable:true,width:100,editor:{type:'numberbox',options:{precision:4}}},
	        {field:'currencyType',title:'币种',sortable:true,width:90},
	        {field:'minPrice', title:'下限（包含）', sortable:true, width:100, editor: "text"},
	        {field:'maxPrice', title:'上限（不包含）', sortable:true, width:100, editor: "text"},
	        {field:'billUnit', title:'计量单位', sortable:true, width:140,
	        	editor:{
	        		type:'combobox',
                    options:{
                    	url: "${ctx}/system/dict/searchDict/units",
                        valueField: 'value',
                        textField: 'label',
                        editable: false, //不允许手动输入
                        method: 'GET'
	        		}
	        	}
	        },
	        {field:'termAttribute', title:'条件属性',sortable:true, width:100,
	        	editor:{
	        		type:'combobox',
                    options:{
                    	url: "${ctx}/system/dict/searchDict/termAttribute",
                        valueField: 'value',
                        textField: 'label',
                        editable: false, //不允许手动输入
                        method: 'GET'
	        		}
	        	}
	        },
	        {field:'feeType',title:'费用类别',sortable:true,width:100},
//	        {field:'space',title:'规格',sortable:true,width:100},
	        {field:'gearCode', title:'档位代码', sortable:true, width:150, editor: "text"},
	        {field:'gearExp', title:'档位说明', sortable:true, width:150, editor: "text"},
 	        {field:'remark',title:'备注',sortable:true,width:150, editor: "text"}
	    ]],
	    onDblClickRow:function(rowIndex,rowData){
	    	var ids= [];
			ids.push(rowData.id);
			$.ajax({
				type: 'get',
				url: "${ctx}/base/contractInfo/deleteContractInfo/" + ids,
				success: function(data){
					successTip(data, dg);
				}
			});
	    },
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb',
	    autoEditing: false,//该属性启用双击行时自定开启该行的编辑状态
	    singleEditing: true,
	    onAfterEdit: function(rowIndex, rowData, changes){
	    	sureUpd(rowData);
	    }
	});
} 

//修改方案明细
function updInfo(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	var rowIndex = dg.datagrid('getRowIndex', row);
    dg.datagrid('beginEdit', rowIndex);
}

//修改  方案明细 后保存
function sureUpd(row){
	var billUnit=row.billUnit;
	var termAttribute=row.termAttribute;
	//如果不是数字
    if(isNaN(billUnit)){
    	billUnit=row.unit;
	}
	if(isNaN(termAttribute)){
		termAttribute=row.term;
	}
	if(isNaN(row.price) || row.price == "" ){
		parent.$.messager.show({title: "提示", msg: "单价的值不能为空并且必须为数字！", position: "bottomRight"});
		return;
	}else if(row.billUnit == ""){
		parent.$.messager.show({title: "提示", msg: "计量单位不能为空！", position: "bottomRight"});
		return;
	} else if(isNaN(row.minPrice) || isNaN(row.maxPrice)){
		parent.$.messager.show({title: "提示", msg: "下限值或上限值不是数字！", position: "bottomRight"});
		return;
	}else{
		if(parseInt(row.minPrice) > parseInt(row.maxPrice)){
			parent.$.messager.show({title: "提示", msg: "下限值不能大于等于上限值！", position: "bottomRight"});
			return;
		}else{
			parent.$.messager.confirm('提示', '确定要修改方合同明细？', function(data){
				if (data){
					$.ajax({
						type: 'GET',
						url: "${ctx}/base/contractInfo/"+ row.id +"/updateContractInfo",
						data: "price=" + row.price + "&billUnit=" +billUnit+"&minPrice=" + row.minPrice + "&maxPrice="+  row.maxPrice + "&termAttribute="+termAttribute+"&gearCode="+  row.gearCode +"&gearExp="+ row.gearExp + "&onSale=" + row.onSale + "&remark=" + row.remark,
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

//保存明细
function batchAdd(){
	var contractNum = $('#contractNum').val();
	var row = dgCost.datagrid('getSelected');
	if(row == null) {
		parent.$.messager.show({title: "提示", msg: "请选择费目！", position: "bottomRight" });
		return;
	}
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
				url: "${ctx}/base/contractInfo/"+ contractNum +"/createContractInfoIds",
				success: function(data){
					if(data == "success"){
						gridDG();
						var datas = dgCost.treegrid('getSelections');
						for(var i=0; i < datas.length; i++){
							$('#dgCost').datagrid("unselectRow", datas[i].id);
						}
						gridDgCost();
						parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight" });
					}else{
						gridDgCost();
						parent.$.messager.show({title: "提示", msg: "操作失败，请重新操作！", position: "bottomRight" });
					}
					
				}
			});
		} 
	});
}

//双击保存明细
function batchAdd2(rowIndex,rowData){
	var contractNum = $('#contractNum').val();
	var ids = rowData.ID;
//	parent.$.messager.confirm('提示', '确认添加？', function(data){
//		if (data){
			var newIdsList = [];
			newIdsList.push(ids);
			$.ajax({
				async: false,
				type: 'POST',
				data: JSON.stringify(newIdsList),
				contentType: 'application/json;charset=utf-8',
				url: "${ctx}/base/contractInfo/"+ contractNum +"/createContractInfoIds",
				success: function(data){
					if(data == "success"){
						gridDG();
						var datas = dgCost.treegrid('getSelections');
						for(var i=0; i < datas.length; i++){
							$('#dgCost').datagrid("unselectRow", datas[i].id);
						}
						gridDgCost();
						parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight" });
					}else{
						gridDgCost();
						parent.$.messager.show({title: "提示", msg: "操作失败，请重新操作！", position: "bottomRight" });
					}
					
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
				url: "${ctx}/base/contractInfo/deleteContractInfo/" + ids,
				success: function(data){
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
				url: "${ctx}/base/contractInfo/copyContractInfo/" + row.id,
				success: function(data){
					successTip(data, dg);
				}
			});
		} 
	});
}

//弹窗增加
function addInfo() {
	var contractNum = $('#contractNum').val();
	d=$("#dlg").dialog({   
    	title: '新增费目代码',    
	    width: 380,    
	    height: 380,    
	    href:'${ctx}/base/feecode/create',
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				var code = $("#code").val();
				$.ajax({
					async: false,
					type: 'POST',
					url: "${ctx}/base/feecode/createByAjax",
					data: $('#mainform').serialize(),
					dataType: "text",  
					success: function(data){
						$.ajax({
							async: false,
							type: 'POST',
							data: "",
							contentType: 'application/json;charset=utf-8',
							url: "${ctx}/base/contractInfo/"+ contractNum +'/'+ code +"/createContractInfoByCode",
							success: function(data){
								dgCost.datagrid('reload');
								dg.datagrid('reload');
								d.panel('close');
							}
						});
					}
				});
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	});
}

//审核通过
function okPass(){
	var contractNum = $('#contractNum').val();
	parent.$.messager.confirm('提示', '您确定要审核通过？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/base/contract/passOkContract/" + contractNum,
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
	var contractNum = $('#contractNum').val();
	parent.$.messager.confirm('提示', '您确定要取消审核通过？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/base/contract/passNoContract/" + contractNum,
				success: function(data){
					window.parent.mainpage.mainTabs.refCurrentTab();//刷新TAB
				}
			});
		} 
	});
}

</script>
</body>
</html>