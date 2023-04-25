<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
        <div>
        	<form id="searchFrom" action="">
        		<input type="text" id="payId" name="payId" class="easyui-validatebox" data-options="width:150,prompt: '业务单号'"/>
        		<select id="clientName" name="clientName" class="easyui-combobox" data-options="width:150,prompt: '客户名称'">
        		</select>
        		<select id="payee" name="payee" class="easyui-combobox" data-options="width:150,prompt: '收款人'">
        		</select>
        		<input type="text" id="billNum" name="billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
       	        <input type="text" id="searchStrTime" name="searchStrTime" class="easyui-my97" datefmt="yyyy-MM"   data-options="width:150,prompt: '开始年月'"/>
	            <input type="text" id="searchEndTime" name="searchEndTime" class="easyui-my97" datefmt="yyyy-MM"   data-options="width:150,prompt: '结束年月'"/>
				
		        <span class="toolbar-item dialog-tool-separator"></span>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="dc()">导出Excel</a>
		        <input type="radio"  id="all"  name="R" value="all" checked="checked" />汇总
				<input type="radio"  id="detail" name="R" value="detial" />明细
			</form>
        </div>
  </div>
<table id="dg"></table> 
<div id="dlg"></div>  
<script type="text/javascript">
var dg;
var d;

document.onkeydown = function () {if(event.keyCode == 13){cx();}};

//客户名称
	   $('#clientName').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll",
	   valueField: 'clientName',
	   textField: 'clientName',
	   mode:'remote'
   	});

	 //收款人
	   $('#payee').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll",
	   valueField: 'clientName',
	   textField: 'clientName',
	   mode:'remote'
   	});
	 
$(function(){  
	$.ajax({
	  		async: false,
	  		type: 'POST',
	  		url: "${ctx}/report/paymentDetailReportStock/cunchu",
	  		dataType: "text",  
	  		success: function(msg){
	  		}
	  	});
	  all();
});

$("#all").click(function(){
	all();
})

$("#detail").click(function(){
	detail();
})

function all(){
	$("#billNum").hide();
	dg = $('#dg').datagrid({    
		method: "get",
	    url:'${ctx}/report/paymentReportStock/json', 
	    fit : true,
		fitColumns : true,
		border : false,
// 		idField : 'trayCode',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect:true,
	    columns:[[    
	        {field: 'payId',title:'业务单号',sortable:true,width:100},    
	        {field: 'clientName',title:'客户名称',sortable:true,width:100},
	        {field: 'payee',title:'收款人',sortable:true,width:100},
	        {field: 'sum',title:'合计',sortable:true,width:100},
	    ]],queryParams: {
	    	"payId":$("#payId").val(),
	    	"clientName":$("#clientName").combobox("getValue"),
	    	"payee":$("#payee").combobox("getValue"),
	    	"billNum":$("#billNum").val(),
	    	"searchStrTime":$("input[name='searchStrTime']").val(),
	    	"searchEndTime":$("input[name='searchEndTime']").val()
		},
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}

function detail(){
	 $("#billNum").show();

	$.getJSON('${ctx}/report/paymentDetailReportStock/detailhead', {
			"payId" : $("#payId").val(),
			"clientName" : $("#clientName").combobox("getValue"),
			"payee" : $("#payee").combobox("getValue"),
			"billNum" : $("#billNum").val(),
			"searchStrTime" : $("input[name='searchStrTime']").val(),
			"searchEndTime" : $("input[name='searchEndTime']").val()
		}, function(result) {
			var columns = new Array();
			$.each(result, function(i, field) {
				var column = {};
				column["title"] = result[i].lab;
				if (typeof (result[i].labe) != "undefined") {
					column["field"] = result[i].labe;
				} else {
					column["field"] = result[i].lab;
				}
				column["width"] = 50;
				columns.push(column);//当需要formatter的时候自己添加就可以了,原理就是拼接字符串.  
			});

			dg = $('#dg').datagrid({
				method : "get",
				url : '${ctx}/report/paymentDetailReportStock/detail',
				fit : true,
				fitColumns : true,
				border : false,
				//     		idField : 'trayCode',
				striped : true,
				pagination : true,
				rownumbers : true,
				pageNumber : 1,
				pageSize : 20,
				pageList : [ 10, 20, 30, 40, 50 ],
				singleSelect : true,
				columns : [ columns ],
				queryParams : {
					"payId" : $("#payId").val(),
					"clientName" : $("#clientName").combobox("getValue"),
					"payee" : $("#payee").combobox("getValue"),
					"billNum" : $("#billNum").val(),
					"searchStrTime" : $("input[name='searchStrTime']").val(),
					"searchEndTime" : $("input[name='searchEndTime']").val()
				},
				enableHeaderClickMenu : true,
				enableHeaderContextMenu : true,
				enableRowContextMenu : false,
				toolbar : '#tb'
			});
		});

	}

	//创建查询对象并查询
	function cx() {
		var val = $('input:radio[name="R"]:checked').val();
		if (val == "all") {
			all();
		} else {
			detail();
		}

	}
	function dc() {
		var val = $('input:radio[name="R"]:checked').val();
		if (val == "all") {
			var url = "${ctx}/report/paymentReportStock/exportPaymentReportStockExcel";
		} else {
			var url = "${ctx}/report/paymentDetailReportStock/exportPaymentDetailReportStockExcel";
		}
		$("#searchFrom").attr("action", url).submit();
	}
</script>
</body>
</html>