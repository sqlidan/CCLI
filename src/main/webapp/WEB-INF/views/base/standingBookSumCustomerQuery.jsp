<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
    <div>
        <form id="searchFrom" action="" method="post">
            <select class="easyui-combobox" id="customsNum" name="customsNum" data-options="width:150,prompt: '客户名称'">
					<option value=""></option>
			</select>
            <input type="text" name="billNum" class="easyui-validatebox"
                   data-options="width:150,prompt: '提单号（多个提单号请用英文的逗号分割）'"/>
            <input type="text" name="feeName" class="easyui-validatebox"
                   data-options="width:150,prompt: '费目名称'"/>
            <input type="text" name="straTime" class="easyui-my97" datefmt="yyyy-MM-dd"
                   data-options="width:150,required:'required',prompt: '创建日期起'"/>
            <input type="text" name="endTime" class="easyui-my97" datefmt="yyyy-MM-dd"
                   data-options="width:150,required:'required',prompt: '创建日期止'"/>

            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        </form>
        <shiro:hasPermission name="bis:checkbook:customerreport">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"
               onclick="report()">Excel导出</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
    </div>
</div>

<table id="dg"></table>
<div id="dlg"></div>

<script type="text/javascript">
    var dg;
    var obj = $("#searchFrom").serializeObject();
    document.onkeydown = function () {if(event.keyCode == 13){cx();}};
    $(document).ready(function(){
        //加载页面数据表格
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/cost/standingBook/listcustomerjson',
            fit: true,
            fitColumns: false,
            border: false,
            sortOrder: 'desc',
            queryParams:obj,
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'customsName', title: '客户名称', sortable: true, width: 200},
                {field: 'billNum', title: '提单号', sortable: true, width: 150},
                {field: 'feeName', title: '费目名称', sortable: true, width: 200},
                {field: 'straTime', title: '创建日期', sortable: true, width: 100},
                {field: 'sL', title: '数量（件/吨）', sortable: true, width: 100},
                {field: 'ysdj', title: '应收单价（RMB）', sortable: true, width: 100,
                	formatter:function(val,rowData,rowIndex){
                        if(val!=null)
                            return val.toFixed(2);
                   }	
                },
                {field: 'yfdj', title: '应付单价（RMB）', sortable: true, width: 100,
                	formatter:function(val,rowData,rowIndex){
                        if(val!=null)
                            return val.toFixed(2);
                   }	
                },
                {field: 'yS', title: '应收金额（RMB）', sortable: true, width: 100,
                	formatter:function(val,rowData,rowIndex){
                        if(val!=null)
                            return val.toFixed(2);
                   }	
                },
                {field: 'yF', title: '应付金额（RMB）', sortable: true, width: 100,
                	formatter:function(val,rowData,rowIndex){
                        if(val!=null)
                            return val.toFixed(2);
                   }	
                },
                {field: 'lR', title: '利润（RMB）', sortable: true, width: 100,
                	formatter:function(val,rowData,rowIndex){
                        if(val!=null)
                            return val.toFixed(2);
                   }	
                }
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar:'#tb'
        });
        
        $('#customsNum').combobox({
 		   method:"GET",
 		   url:"${ctx}/base/client/getClientAll",
 		   valueField: 'ids',
 		   textField: 'clientName',
 		   mode:'remote' 
 	   });
    });

    //创建查询对象并查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
    }
    //导出Excel
    function report() {
    	if ($("#searchFrom").form('validate')) {
	        var url = "${ctx}/cost/standingBook/exportCustomerExcel";
	        $("#searchFrom").attr("action", url).submit();
    	}
    }
</script>
</body>
</html>