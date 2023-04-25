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
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="upd()">手动指派月台</a>
        <span class="toolbar-item dialog-tool-separator"></span>
    </div>
</div>

<table id="dg"></table>--%>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="来车列表" style="overflow-y:auto">
    <div style="padding:5px;height:auto" class="datagrid-toolbar">
        <form id="searchFrom" action="">
            <input type="text"  name="filter_EQS_platformNo" class="easyui-validatebox" data-options="width:150,prompt: '月台号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
         <%--   <input type="text" name="filter_LIKES_platformName" class="easyui-validatebox" data-options="width:150,prompt: '月台名称'"/>
            <span class="toolbar-item dialog-tool-separator"></span>--%>
            <input type="text" name="filter_LIKES_warehouseNo" class="easyui-validatebox" data-options="width:150,prompt: '库号'"/>

            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text" name="filter_LIKES_platNo" class="easyui-validatebox" data-options="width:150,prompt: '车牌号'"/>

            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text" name="filter_LIKES_containerNo" class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <select id="inoutBoundFlag" name="filter_EQS_inoutBoundFlag" class="easyui-combobox"
                    data-options="width:150,prompt: '出入库'">
                <option></option>
                <option value="1">入库</option>
                <option value="2">出库</option>

            </select>
            <span class="toolbar-item dialog-tool-separator"></span>


            <select id="statusFlag" name="filter_EQS_statusFlag" class="easyui-combobox"
                    data-options="width:150,prompt: '车辆状态'">
                <option></option>
                <option value="0">排队中</option>
                <option value="1">车辆驶入月台</option>
                <option value="2">已开始</option>
                <option value="3">已结束</option>
                <option value="4">车辆离开月台</option>
                <option value="5">车辆出闸</option>
                <option value="6">车辆取消靠口</option>


            </select>
            <span class="toolbar-item dialog-tool-separator"></span>


          <%--  <input type="text" name="filter_GED_queueTime" class="easyui-my97" datefmt="yyyy-MM-dd"
                   data-options="width:150,prompt: '入场日期（开始）'"/>
            <input type="text" name="filter_LED_queueTime" class="easyui-my97" datefmt="yyyy-MM-dd"
                   data-options="width:150,prompt: '入场日期（结束）'"/>

            <span class="toolbar-item dialog-tool-separator"></span>--%>


            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        </form>
        <form id="searchFrom3" action="">
        </form>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="upd()">手动指派月台</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="cancel()">取消作业</a>
        <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" data-options="disabled:false" onclick="resertQueue()">重新排队</a>


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


        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/platform/vehicle/queue/serach',
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
                {field: 'warehouseNo', title: '库号',  width: 70},
                {field: 'platformNo', title: '月台号',  width: 80},
                {field: 'platformName', title: '月台名称',  width: 80},
                {field: 'platNo', title: '车号',  width: 80},
                {field: 'containerNo', title: '箱号',  width: 80},
                {field: 'inoutBoundFlag', title: '出/入库',  width: 60,
                    formatter: function (value, row, index) {
                        return value == "1" ? '入库' : '出库';
                    }
                },
                {field: 'autoManualFlag', title: '自动/手动分配',width: 100,
                    formatter: function (value, row, index) {
                        if (value=="1"||value=="3"){
                            return '自动';
                        }else {
                            return '手动';
                        }
                    }
                },
                {field: 'queueTime', title: '排队时间', width: 130},
                {field: 'statusFlag', title: '状态',width: 90,
                    formatter:function (value, row, index){
                        if (value == "0"){
                            return '排队中';
                        }else if (value == "1"){
                            return '车辆驶入月台';
                        }else if (value == "2"){
                            return '已开始';
                        }else if (value == "3"){
                            return '已结束';
                        }else if (value =="4"){
                            return '车辆离开月台'
                        }else if (value =="5"){
                            return '车辆出闸'
                        } else if (value =="6"){
                            return '车辆取消靠口';
                        }
                    }
                },
                {field: 'platformId',title: 'platformId', hidden:true}


            ]],
            rowStyler: function (rowIndex, rowData) {
               // console.log(rowData);

               if(rowData.statusFlag=='0'){

                   var time= (new Date().getTime()-  new Date(rowData.queueTime).getTime())/( 60 * 60 * 1000);

                   if(time>=4){
                       return 'background-color:red;';
                   }else if(time>=2){
                       return 'background-color:Yellow;';
                   }
               }
              /*  //变成红色
                if (rowData.colorType=='2') {
                    return 'background-color:red;';
                }else if(rowData.colorType=='1'){
                    return 'background-color:Yellow;';
                }*/
            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });

    function upd(){
        var row = dg.datagrid('getSelected');
        if(rowIsNull(row)) return;

        if(row.statusFlag!='0'){
            parent.$.messager.alert("提示","只能指派排队车辆");
            return;
        }

        d=$("#dlg").dialog({
            title: '手动指派月台',
            width: 380,
            height: 340,
            href:'${ctx}/platform/vehicle/queue/assignment/'+row.id,
            maximizable:true,
            modal:true,
            buttons:[{
                text:'修改',
                handler:function(){
                    $('#mainform').submit();
                    cx();
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

    //取消作业 取消预约
    function cancel(){
        var row = dg.datagrid('getSelected');

        if(rowIsNull(row)) return;

        if(row.statusFlag!='0' && row.statusFlag!='1' && row.statusFlag!='2'){
            parent.$.messager.alert("提示","该状态不能取消靠口");
            return;
        }
        var id = row.id;
        parent.$.messager.confirm('提示', '取消车号：'+row.platNo+'作业,回退预约系统,无法恢复您确定要取消作业？', function(data){
            if (data){
                $.ajax({
                    type:'get',
                    url:"${ctx}/platform/vehicle/queue/cancel/"+id,
                    success: function(data){
                        successTip(data,dg);
                        cx();
                    }
                });
            }
        });
    }


    //重新排队
    function resertQueue(){
        var row = dg.datagrid('getSelected');

        if(rowIsNull(row)) return;


        if(!(row.statusFlag=='0'&&row.autoManualFlag==2) && row.statusFlag!='1'){
            parent.$.messager.alert("提示","该状态不能重新排队");
            return;
        }

        //只能   排队中手动指派和驶入月台  重新排队
        var id = row.id;
        parent.$.messager.confirm('提示', '车号：'+row.platNo+'重新排队,无法恢复您确定要重新排队？', function(data){
            if (data){
                $.ajax({
                    type:'get',
                    url:"${ctx}/platform/vehicle/queue/resertQueue/"+id,
                    success: function(data){
                        successTip(data,dg);
                        cx();
                    }
                });
            }
        });
    }

</script>
</body>
</html>