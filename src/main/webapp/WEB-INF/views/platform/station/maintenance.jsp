<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<%--<body>
<div id="tb" style="padding:5px;height:auto">
    <div>
        <form id="infoForm" method="post">
        </form>

        <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-edit" onclick="updType()">修改月台类型</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-edit" onclick="updStatus()">修改月台状态</a>
        <span class="toolbar-item dialog-tool-separator"></span>
    </div>
</div>

<table id="dg"></table>--%>

<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="月台维护" style="overflow-y:auto">
    <div style="padding:5px;height:auto" class="datagrid-toolbar">
        <form id="searchFrom" action="">
            <input type="text"  name="filter_EQI_platformNo" class="easyui-validatebox" data-options="width:150,prompt: '月台号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text" name="filter_LIKES_platform" class="easyui-validatebox" data-options="width:150,prompt: '月台名称'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text" name="filter_LIKES_trayRoomNum" class="easyui-validatebox" data-options="width:150,prompt: '库号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>


            <select id="platformStatus" name="filter_EQS_platformStatus" class="easyui-combobox"
                    data-options="width:150,prompt: '月台状态'">
                <option></option>

                <option value='2'>启用</option>
                <option value='3'>停止</option>

            </select>
            <span class="toolbar-item dialog-tool-separator"></span>


         <%--   <select id="platformType" name="filter_EQS_platformType" class="easyui-combobox"
                    data-options="width:150,prompt: '月台类型'">
                <option></option>
                <option value="1">自动</option>
                <option value="2">手动</option>


            </select>--%>

            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text" name="filter_EQS_sort" class="easyui-validatebox" data-options="width:150,prompt: '排序'"/>


            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        </form>
        <form id="searchFrom3" action="">
        </form>
       <%-- <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-edit" onclick="updType()">修改月台类型</a>
        <span class="toolbar-item dialog-tool-separator"></span>--%>
        <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-edit" onclick="updStatus()">修改月台状态</a>

        <span class="toolbar-item dialog-tool-separator"></span>

    </div>
    <table id="dg"></table>
</div>



<div id="dlg"></div>

<script type="text/javascript">
    var dg;
    var d;

    document.onkeydown = function () {
        if (event.keyCode == 13) {
            cx();
        }
    };

    $(function () {
        //客户
        $('#searchStockIn').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote'
        });

        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/platform/station/maintenance/json',
            fit: true,
            fitColumns: false,
            border: false,
            idField: 'linkId',
            sortOrder: 'desc',
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'id',title: 'ID',width: 50,},
                {field: 'platformNo', title: '月台号',  width: 70},
                {field: 'platform', title: '月台名称',  width: 70},
                {field: 'trayRoomNum', title: '库号',  width: 60},
                {field: 'platformStatus', title: '月台状态',  width: 70,
                    formatter:function (val, row, index){
                        if (val == "3"){
                            return '停止';
                        }else {
                            return '启用';
                        }
                    }
                },
                {field: 'platformType', title: '月台类型',width: 70,
                    formatter:function (val, row, index){
                        if (val == "1"){
                            return '自动';
                        }else if (val == "2"){
                            return '手动';
                        }

                    }
                },
                {field: 'inoutBoundFlag', title: '出入库类型',width: 70,
                    formatter:function (val, row, index){
                        return val == "1" ? "入库" : "出库";
                    }
                },
                {field: 'sort', title: '排序',  width: 60}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });
    //修改
    function updType() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;

        d=$("#dlg").dialog({
            title: '修改月台状态',
            width: 450,
            height: 320,
            href:'${ctx}/platform/station/maintenance/updateType/'+row.id,
            maximizable:true,
            modal:true,
            buttons:[{
                text:'确认',
                handler:function(){
                    $("#mainform").submit();
                }
            },{
                text:'取消',
                handler:function(){
                    d.panel('close');
                }
            }]
        });
    }

    function updStatus() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;

        d=$("#dlg").dialog({
            title: '修改月台状态',
            width: 450,
            height: 320,
            href:'${ctx}/platform/station/maintenance/updateStatus/'+row.id,
            maximizable:true,
            modal:true,
            buttons:[{
                text:'确认',
                handler:function(){
                    $("#mainform").submit();
                }
            },{
                text:'取消',
                handler:function(){
                    d.panel('close');
                }
            }]
        });
    }

    //创建查询对象并查询
    function cx(){
        dg.datagrid('clearSelections');
        var obj=$("#searchFrom").serializeObject();

        dg.datagrid('load',obj);
    }

</script>
</body>
</html>