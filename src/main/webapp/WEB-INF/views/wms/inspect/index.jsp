<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">

<div id="tb">

    <div style="padding:5px;height:auto" class="datagrid-toolbar">

        <form id="searchFrom" action="">

            <input type="text" name="inspectId" class="easyui-validatebox" data-options="width:120,prompt: '计划单号'"/>
<!--             <input type="text" name="billNum" class="easyui-validatebox" data-options="width:120,prompt: '提单号'"/>
            <input type="text" name="ctnNum" class="easyui-validatebox" data-options="width:120,prompt: '箱号'"/>
            <input type="text" name="skuId" class="easyui-validatebox" data-options="width:120,prompt: 'SKU'"/> -->
            <select id="stockName" name="stockName" class="easyui-combobox"
                    data-options="width:120,prompt: '客户名称'"></select>

            <span class="toolbar-item dialog-tool-separator"></span>

            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>

        </form>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
           onclick="addModule('新增取样计划','wms/inspect/plans/add?inspectId=000000')">添加</a>

        <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
           onclick="onUpdate()">修改</a>

        <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
           onclick="onDelete()">删除</a>

        <!-- <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
           onclick="auditPlan()">审核</a> -->

       <!--  <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
           onclick="onCancelAudit()">取消审核</a> -->
        
		<span class="toolbar-item dialog-tool-separator"></span>
        
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" 
           onclick="printIt()">打印</a>
        
        <span class="toolbar-item dialog-tool-separator"></span>
        
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-zoom  " plain="true"
           onclick="lookInspectStockM()">查看</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        


    </div>

</div>

<table id="dg"></table>
<div id="dlg"></div>

<script type="text/javascript">

    var dg;
    var dlg;

    function auditPlan() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        if (row.operateState > 0) {
            toast("取样计划已审核!");
            return;
        }

        dlg = $("#dlg").dialog({
            title: '审核',
            width: 300,
            height: 400,
            href: '${ctx}/wms/inspect/plans/audit?inspectId=' + row.inspectId,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {

                    if ($("#mainform").form('validate')) {

                        $.ajax({
                            async: false,
                            type: 'POST',
                            url: "${ctx}/wms/inspect/plans/audit",
                            data: $("#mainform").serializeArray(),
                            dataType: "text",
                            success: function (msg) {

                                if (msg == "success") {
                                    toast("审核成功!");
                                    dlg.panel('close');
                                    cx();
                                } else {
                                    toast("审核失败!");
                                }

                            }
                        });

                    }
                }
            }, {
                text: '取消',
                handler: function () {
                    dlg.panel('close');
                }
            }]
        });

    }

    function onCancelAudit() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        if (row.operateState == 0) {
            toast("取样计划未审核!");
            return;
        }

        parent.$.messager.confirm('提示', '确定取消审核？', function (data) {

            if (data) {

                var url = "${ctx}/wms/inspect/plans/audit/cancel?inspectId=" + row.inspectId;

                $.get(url, function (data) {

                    if (data == "success") {
                        toast("取消成功");
                        cx();
                    } else {
                        toast("取消失败");
                    }

                });

            }

        });

    }

    function onUpdate() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        if (row.operateState == 1) {
            toast("已审核不能修改")
            return;
        }
        //addModule('取样计划管理', 'wms/inspect/plans/add?inspectId=' + row.inspectId);
        addModule('取样计划管理', 'wms/inspect/plans/update?inspectId=' + row.inspectId);
    }

    function onDelete() {

        parent.$.messager.confirm('提示', '确定删除取样计划？', function (data) {

            if (data) {

                var row = dg.datagrid('getSelected');

                if (rowIsNull(row)) return;

                var url = "${ctx}/wms/inspect/plans/deleteAll?inspectId=" + row.inspectId;

                $.get(url, function (data) {

                    if (data == "success") {
                        toast("删除成功");
                        cx();
                    } else {
                        toast("删除失败");
                    }

                });

            }

        });

    }

    function addModule(title, url) {
        window.parent.mainpage.mainTabs.addModule(title, url);
    }

    $(document).ready(function () {

        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/wms/inspect/plans/list',
            fit: true,
            fitColumns: false,
            border: false,
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'inspectId', title: '计划单号', sortable: true, width: 180},
                /* {field: 'trayId', title: '托盘号', sortable: true, width: 120}, */
                /* {field: 'billNum', title: '提单号', sortable: true, width: 130},
                {field: 'ctnNum', title: '箱号', sortable: true, width: 130},
                {field: 'skuId', title: 'SKU', sortable: true, width: 120},
                {field: 'contactNum', title: '联系单号', sortable: true, width: 120},
                {field: 'asn', title: 'ASN', sortable: true, width: 120}, */
                {field: 'stockName', title: '客户名称', sortable: true, width: 120},
                {field: 'sampleDate', title: '实际取样时间', sortable: true, width: 120,
                	formatter: function (value, row, index) {
                        return value==null ? "" :formatDate(value);
                    }	
                },
                /* {field: 'warehouse', title: '仓库名', sortable: true, width: 120},
                {field: 'cargoName', title: '产品名', sortable: true, width: 120}, */
                {field: 'inspectTotal', title: '件数', sortable: true, width: 150},
                {
                    field: 'operateState', title: '状态', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        if (value == '0') {
                            return '未审核';
                        }
                        if (value == '1') {
                            return '已审核';
                        }
                    }
                },
                {
                    field: 'operateDate', title: '上次操作时间', sortable: true, width: 130,
                    formatter: function (value, row, index) {
                        return formatDate(value);
                    }
                },
                {field: 'operateUserName', title: '上次操作人', sortable: true, width: 130},
                {
                    field: 'createDate',
                    title: '创建时间',
                    sortable: true,
                    width: 150,
                    formatter: function (value, row, index) {
                        return formatDate(value);
                    }
                },
                {field: 'createUserName', title: '创建人', sortable: true, width: 100},
                {field: 'description', title: '备注', sortable: true, width: 200}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });

    $('#stockName').combobox({
        method: "GET",
        url: "${ctx}/base/client/getClientAll",
        valueField: 'clientName',
        textField: 'clientName',
        mode: 'remote'
    });

    function cx() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
    }

    function formatDate(strTime) {
//        var date = new Date(strTime);
//        return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
        var unixTimestamp = new Date(strTime);
        return unixTimestamp.toLocaleString();
    }

    function toast(msg) {
        parent.$.messager.show({title: "提示", msg: msg, position: "bottomRight"});
    }
    
  //打印
    function printIt(){
    	var row = dg.datagrid('getSelected');
    	if(rowIsNull(row)) return;
    	/* if(rowIsNull(row.InspectId)){
    		 toast("不能打印");
    		 return;
    	}  */
    	window.parent.mainpage.mainTabs.addModule('取样计划单打印','wms/inspect/print/' + row.inspectId);
    }

  //查看
  function lookInspectStockM(){
	  var row = dg.datagrid('getSelected');

      if (rowIsNull(row)) return;

      //addModule('取样计划管理', 'wms/inspect/plans/add?inspectId=' + row.inspectId);
      addModule('取样计划查看', 'wms/inspect/plans/havealook?inspectId=' + row.inspectId);
  }
  
  
</script>

</body>

</html>