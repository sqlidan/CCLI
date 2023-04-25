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
	<div style="padding:5px;height:auto">
<!-- 			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="submitFrom()">保存</a> -->
<!-- 		  	<span class="toolbar-item dialog-tool-separator"></span> -->
<!-- 		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" data-options="disabled:false" onclick="myrefresh()">保存并新建</a> -->
<!-- 		   	<span class="toolbar-item dialog-tool-separator"></span> -->
<!-- 		    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-old-versions" plain="true" onclick="copyContract()">保存并复制</a> -->
<!-- 		    <span class="toolbar-item dialog-tool-separator"></span> -->
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-check" plain="true" onclick="okPass()">审核</a>
	    <span class="toolbar-item dialog-tool-separator"></span>
	    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="noPass()">取消审核</a>
	    <span class="toolbar-item dialog-tool-separator"></span>
	</div>
	<table class="formTable">
		<tr>
			<td>方案名称</td>
			<td>
				<input type="hidden" id="schemeNum" name="schemeNum" value="${scheme.schemeNum }"/>
				<input id="schemeName" name="schemeName" class="easyui-validatebox" data-options="width: 200" value="${scheme.schemeName }" style="background-color:#EBEBE4" readonly/> 
			</td>
			<td>状态</td>
			<td>
				<input id="programStateName" name="programStateName" class="easyui-validatebox" data-options="width: 200" style="background-color:#EBEBE4"
					value="<c:choose><c:when test="${scheme.programState == 1}">审核 </c:when><c:otherwise>未审核 </c:otherwise></c:choose>" readonly />
				<input type="hidden" id="programState" name="programState" value="<c:choose><c:when test="${scheme.programState == '1'}">1</c:when><c:otherwise>0</c:otherwise></c:choose>"/>
			</td>
		</tr>
		<tr>
			<td>合同号</td>
			<td>
				<input type="hidden" id="contractNum" name="contractNum" value="${scheme.contractId }"/>
				<select class="easyui-combobox" id="contractId" name="contractId" data-options="width: 200, required:'required'" readonly disabled>
					<option value=""></option>
				</select>
			</td>
			<td colspan="2">
			  	<input type="radio" id="get" name="ifGet" value="1" readonly disabled/><label for="get">应收</label>
				<input type="radio" id="post" name="ifGet" value="2" readonly disabled/><label for="post">应付</label>
			</td>
		</tr>
		<!--<tr>
			<td>方案类型</td>
			<td>
				<select class="easyui-combobox" id="programType" name="programType" data-options="width: 200, required:'required'" readonly disabled>
					<option value=""></option>
				</select>
			</td>
			<td>业务类型</td>
			<td>
				<select class="easyui-combobox" id="bisType" name="bisType" data-options="width: 200, required:'required'" readonly disabled>
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
			<td>备注</td>
			<td colspan="3">
				<textarea rows="3" cols="55" name="remark" style="font-size: 12px; font-family: '微软雅黑'; background-color:#EBEBE4">${scheme.remark }</textarea>
			</td>
		</tr>
		<tr>
			<td>方案共享客户</td>
			<td>
				<input type="hidden" id="clientIds" name="shareIds" value="" />
				<input id="shareClient"  data-options="width:200" class="easyui-validatebox" style="background:#eee"   readonly /> 
			</td>
		</tr>
	</table>
</form>
</div>
<div data-options="region:'south',split:true,border:false,title:'费用方案明细'" style="height: 350px;">
	<div id="tb" style="padding:5px;height:auto">
		<div>
<!-- 	    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addInfo()">新建</a> -->
<!-- 			<span class="toolbar-item dialog-tool-separator"></span> -->
<!-- 			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="updInfo()">修改</a> -->
<!-- 			<span class="toolbar-item dialog-tool-separator"></span> -->
<!-- 			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="delInfo()">删除</a> -->
<!-- 			<span class="toolbar-item dialog-tool-separator"></span> -->
<!-- 			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-old-versions" plain="true" onclick="toCopyInfo()">复制</a> -->
	    </div>
	</div>
	<table id="dg" ></table> 
	<div id="dlg" ></div> 
</div>
<div data-options="region:'east',split:true,border:false,title:'合同费目列表'" style="width: 480px">
	<div id="tg_tb" style="padding:5px;height:auto">
	  	<div>
<!-- 	  		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-check" plain="true" onclick="batchAdd()">批量添加</a> -->
	    </div>
	</div>
  	<table id="dgCost"></table>
</div> 

<script type="text/javascript">
var dg;
var d;
var dgCost;

$(function(){
	$("input[name='ifGet'][value=${scheme.ifGet}]").attr("checked",true);
	getContract2();
	
	//共享客户
	$.ajax({
			type : "POST",
			url : "${ctx}/base/scheme/getshare/" + $("#schemeNum").val(),
			dataType : "json",
			success : function(date) {
	 			$("#clientIds").val(date[0]);
	 			$("#shareClient").val(date[1]);
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
	
	contractFee($('#contractNum').val());
	gridDG();
});

//获得合同下拉
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
							   editable:false 
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
			{field: 'billUnit',title: '计量单位', sortable: true,width: 100} ,
			{field: 'currencyType', title: '币种', sortable: true, width: 100}
		]],
		enableHeaderClickMenu: true,
		enableHeaderContextMenu: true,
		enableRowContextMenu: false,
		toolbar: '#tg_tb'
	});
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
		idField: 'id',
		striped: true,
		pagination: true,
		rownumbers: true,
		pageNumber: 1,
		pageSize: 20,
		pageList: [ 10, 20, 30, 40, 50 ],
		remoteSort:false,
		singleSelect:true,
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
	    toolbar:'#tb'
	});
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
//					window.parent.mainpage.mainTabs.refCurrentTab();//刷新TAB
					$("#programStateName").val("未审核");
						$("#programState").val('0');
						window.parent.mainpage.mainTabs.addModule('方案修改','base/scheme/updateSchemeForm/' + $("#schemeNum").val());
				}
			});
		} 
	});
}

</script>
</body>
</html>