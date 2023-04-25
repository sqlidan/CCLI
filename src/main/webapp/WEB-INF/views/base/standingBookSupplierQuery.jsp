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
            <select class="easyui-combobox" id="sCustom" name="sCustom" data-options="width:150,prompt: '供应商名称'">
					<option value=""></option>
			</select>
            <select class="easyui-combobox" id="customsNum" name="customsNum" data-options="width:150,prompt: '客户名称'">
					<option value=""></option>
			</select>
            <input type="text" name="billNum" class="easyui-validatebox"
                   data-options="width:150,prompt: '提单号（多个提单号请用英文的逗号分割）'"/>
            <select id="reconcileSign" name="reconcileSign" class="easyui-combobox" data-options="width:80,prompt: '是否对账'">
                <option value='2'></option>
                <option value='0'>否</option>
                <option value='1'>是</option>
            </select>       
            <input type="text" name="straTime" class="easyui-my97" datefmt="yyyy-MM"
                   data-options="width:150,prompt:'创建日期起',required:'required'"/>
            <input type="text" name="endTime" class="easyui-my97" datefmt="yyyy-MM"
                   data-options="width:150,prompt:'创建日期止',required:'required'"/>

            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        </form>
        <shiro:hasPermission name="bis:checkbook:supplierreport">
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
            url: '${ctx}/cost/standingBook/listsupplierjson',
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
                {field: 'crkType', title: '类型', sortable: true, width:60},
                {field: 'straTime', title: '日期', sortable: true, width: 100},
                {field: 'gysName', title: '供应商名称', sortable: true, width:150},
                {field: 'customsName', title: '客户名称', sortable: true, width:150},
                {field: 'billNum', title: '提单号', sortable: true, width: 150},
                {field: 'reconcileType', title: '是否对账', sortable: true, width:80},
                {field: 'reconcileNum', title: '对账单号', sortable: true, width: 100},
                {field: 'bg', title: '报关', sortable: true, width: 80,
                	formatter:function(val,rowData,rowIndex){
                        if(val!=null)
                            return val.toFixed(2);
                   }	
                },
                {field: 'bj', title: '报检', sortable: true, width: 80,
                	formatter:function(val,rowData,rowIndex){
                        if(val!=null)
                            return val.toFixed(2);
                   }	
                },
                {field: 'sp', title: '审批', sortable: true, width: 80,
                	formatter:function(val,rowData,rowIndex){
                        if(val!=null)
                            return val.toFixed(2);
                   }	
                },
                {field: 'sjyh', title: '商检验货', sortable: true, width: 100,
                	formatter:function(val,rowData,rowIndex){
                        if(val!=null)
                            return val.toFixed(2);
                   }	
                },
                {field: 'hgyh', title: '海关验货', sortable: true, width: 100,
                	formatter:function(val,rowData,rowIndex){
                        if(val!=null)
                            return val.toFixed(2);
                   }	
                },
                {field: 'lR', title: '金额', sortable: true, width: 100,
                	formatter:function(val,rowData,rowIndex){
                        if(val!=null)
                            return val.toFixed(2);
                   }	
                },
                {field: 'inputPerson', title: '客服签字', sortable: true, width: 100},
            ]],
            rowStyler: function (rowIndex, rowData) {
                //已对账的变色
                if (rowData.reconcileType == "是") {
                    return 'background-color:#4BB64D';
                }
            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar:'#tb'
        });
        
        $('#sCustom').combobox({
 		   method:"GET",
 		   url:"${ctx}/base/client/getClientAll",
 		   valueField: 'ids',
 		   textField: 'clientName',
 		   mode:'remote' 
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
	        var url = "${ctx}/cost/standingBook/exportSupplierExcel";
	        $("#searchFrom").attr("action", url).submit();
    	}
    }
</script>
</body>
</html>