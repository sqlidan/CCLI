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
	<div class="datagrid-toolbar" style="height:auto" >
	    <shiro:hasPermission name="base:contract:pass">
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="okPass()">审核</a>
		    <span class="toolbar-item dialog-tool-separator"></span>
		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-delete" plain="true" onclick="noPass()">取消审核</a>
		    <span class="toolbar-item dialog-tool-separator"></span>
	    </shiro:hasPermission>
	</div>
	<table class="formTable">
		<tr>
			<td>合同号</td>
			<td>
				<input id="contractNum" name="contractNum" class="easyui-validatebox" data-options="width: 200" value="${contract.contractNum }" style="background-color:#EBEBE4" readonly /> 
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
				<select class="easyui-combobox" id="clientId" name="clientId" data-options="width: 200, required:'required'" readonly disabled>
					<option value=""></option>
				</select>
			</td>
			<td>揽货人</td>
			<td>
				<input id="canvassionPerson" name="canvassionPerson" class="easyui-validatebox" data-options="width: 200, required:'required'" value="${contract.canvassionPerson}" style="background-color:#EBEBE4" readonly /> 
			</td>
		</tr>
		<tr>
			<td>签订时间</td>
			<td>
				<input id="signTime" name="signTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width: 200, required:'required'" 
					value="<fmt:formatDate value="${contract.signTime}" pattern="yyyy-MM-dd HH:mm:ss" />" readonly disabled/> 
			</td>
			<td>到期时间</td>
			<td>
				<input id="expirationTime" name="expirationTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width: 200, required:'required'" 
					value="<fmt:formatDate value="${contract.expirationTime}" pattern="yyyy-MM-dd HH:mm:ss" />" readonly disabled/> 
			</td>
		</tr>
		<tr>
			<td>录入员</td>
			<td>
				<input id="operatorPerson" name="operatorPerson" class="easyui-validatebox" data-options="width: 200" value="${contract.operatorPerson }" style="background-color:#EBEBE4" readonly/> 
			</td>
			<td>录入时间</td>
			<td>
				<input id="operateTime" name="operateTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width: 200" value="<fmt:formatDate value="${contract.operateTime}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/>
			</td>
		</tr>
		<tr>
			<td>报价类型</td>
			<td>
				<select class="easyui-combobox" id="ifMan" name="ifMan" data-options="width: 200" readonly disabled>
					<option value=""></option>
				</select>
			</td>
		</tr>
		<tr>
			<td>备注</td>
			<td colspan="3">
				<textarea rows="3" cols="55" name="remark" style="font-size: 12px; font-family: '微软雅黑'; background-color:#EBEBE4" readonly >${contract.remark }</textarea>
			</td>
		</tr>
	</table>
</form>
</div>
<div data-options="region:'south',split:true,border:false,title:'合同费目明细'" style="height: 300px;">
	<div id="tb" style="padding:5px;height:auto">
		<div>

	    </div>
	</div>
	<table id="dg" ></table> 
	<div id="dlg" ></div> 
</div>
<div data-options="region:'east',split:true,border:false,title:'费目列表'" style="width: 480px">
	<div id="tg_tb" style="padding:5px;height:auto">
	  	<div>

	    </div>
	</div>
  	<table id="dgCost"></table>
</div> 

<script type="text/javascript">
var dg;
var d;
var dgCost;

$(function(){   
	$('#clientId').combobox({
	   method: "GET",
	   url: "${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${contract.clientId}",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode: 'remote',
	   onLoadSuccess:function(){
	   		var client = '${contract.clientId}';
			if(client != null && client != ""){
				$('#clientId').combobox("select", client);
			}
		}
	});

	//查询费目
	dgCost=$('#dgCost').datagrid({    
		method: "GET",
	    url:'${ctx}/base/feecode/json/', 
	    fit : true,
		fitColumns : true,
		border : false,
		idField : 'ID',
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
	        {field:'CURRENCYLABLE',title:'币种',sortable:true,width:100}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tg_tb'
	});
	conType();
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

//查询合同费目明细
function gridDG(){
	var contractNum = $('#contractNum').val();
	dg=$('#dg').datagrid({    
		method: "GET",
	    url:'${ctx}/base/contractInfo/json/' + contractNum, 
	    fit : true,
		/* fitColumns : true, */
		border : false,
		idField : 'id',
		striped:true,
		pagination:true,
		/* nowrap:false, */
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[    
			{field:'id', title:'id',hidden:true}, 
	        {field:'expenseCode',title:'费目',sortable:true,width:100},    
	        {field:'cargoName',title:'费目名称',sortable:true,width:100},
	        {field:'price',title:'价格',sortable:true,width:100,editor:{type:'numberbox',options:{precision:2}}},
	        {field:'onSale',title:'折扣',sortable:true,width:100,editor:{type:'numberbox',options:{precision:2}}},
	        {field:'currencyType',title:'币种',sortable:true,width:100},
	        {field:'minPrice', title:'下限', sortable:true, width:100, editor: "text"},
	        {field:'maxPrice', title:'上限', sortable:true, width:100, editor: "text"},
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
	        {field:'space',title:'规格',sortable:true,width:100},
	        {field:'gearCode', title:'档位代码', sortable:true, width:150, editor: "text"},
	        {field:'gearExp', title:'档位说明', sortable:true, width:150, editor: "text"},
	        {field:'remark',title:'备注',sortable:true,width:250}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
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
					url: "${ctx}/base/feecode/createByTwo",
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
					$("#contractStateName").val("未审核");
					$("#contractState").val('0');
					window.parent.mainpage.mainTabs.addModule('合同修改','base/contract/updateContractForm/' + $("#contractNum").val());
				}
			});
		} 
	});
}
</script>
</body>
</html>