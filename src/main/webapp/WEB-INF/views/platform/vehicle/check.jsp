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

        <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-edit" onclick="check()">核验</a>
        <span class="toolbar-item dialog-tool-separator"></span>
    </div>
</div>

<table id="dg"></table>--%>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="核验列表" style="overflow-y:auto">
    <div style="padding:5px;height:auto" class="datagrid-toolbar">
        <form id="searchFrom" action="">
            <input type="text"  name="filter_LIKES_billNo" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <%--   <input type="text" name="filter_LIKES_platformName" class="easyui-validatebox" data-options="width:150,prompt: '月台名称'"/>
               <span class="toolbar-item dialog-tool-separator"></span>--%>

            <input type="text" name="filter_LIKES_vehicleNo" class="easyui-validatebox" data-options="width:150,prompt: '车号'"/>

            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text" name="filter_LIKES_containerNo" class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>

            <select id="statusFlag" name="filter_EQS_checkStatus" class="easyui-combobox"
                    data-options="width:150,prompt: '核验状态'">
                <option></option>
                <option value="0">未核验</option>
                <option value="1">已核验</option>



            </select>
            <span class="toolbar-item dialog-tool-separator"></span>


              <input type="text" name="filter_GED_gateEntryTime" class="easyui-my97" datefmt="yyyy-MM-dd"
                     data-options="width:150,prompt: '入闸日期（开始）'"/>
              <input type="text" name="filter_LED_gateEntryTime" class="easyui-my97" datefmt="yyyy-MM-dd"
                     data-options="width:150,prompt: '入闸日期（结束）'"/>

              <span class="toolbar-item dialog-tool-separator"></span>


            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        </form>
        <form id="searchFrom3" action="">
        </form>

        <a href="#" class="easyui-linkbutton" plain="true" iconCls="icon-edit" onclick="check()">核验</a>
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
            url: '${ctx}/platform/vehicle/check/search',
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

                {field: 'billNo', title: '提单号', width: 100},
                {field: 'containerNo', title: '箱号',  width: 70},
                {field: 'vehicleNo', title: '车号',  width: 90},
                {field: 'actualVehicleNo', title: '实际车号',  width: 90},
                {field: 'gateEntryTime', title: '入闸时间',  width: 130},
                {field: 'checkStatus', title: '核验状态', width: 80,
                    formatter:function (value, row, index){
                        return value == "1" ? "已核验":"未核验";
                    }
                },
                {
                    field: 'checkTime', title: '核验时间', width: 80
                }
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });

    /*function check(){
        var row = dg.datagrid('getSelected');
        if(rowIsNull(row)) return;
        parent.$.messager.confirm('提示', '确认通过核验吗？', function(data){
            if (data){
                $.ajax({
                    type:'post',
                    url:"${ctx}/platform/vehicle/check/edit/"+row.id,
                    success: function(data){
                        successTip(data,dg);
                    }
                });
            }
        });
    }*/

    function check() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        if (row.checkStatus=='1'){
            parent.$.messager.alert('提示', '该记录不能重复核验！');
            return;
        }
        d=$("#dlg").dialog({
            title: '车辆核验',
            width: 450,
            height: 320,
            href:"${ctx}/platform/vehicle/check/updateStatus/"+row.id,
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
    function cx() {
        dg.datagrid('clearSelections');
        var obj = $("#searchFrom").serializeObject();

        dg.datagrid('load', obj);
    }
</script>
</body>
</html>