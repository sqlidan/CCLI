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
        <form id="searchFrom" action="">

    </div>
</div>

<table id="dg"></table>--%>

<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="预约出库记录" style="overflow-y:auto">
    <div style="padding:5px;height:auto" class="datagrid-toolbar">
        <form id="searchFrom" action="">
            <input type="text"  name="filter_LIKES_carNumber" class="easyui-validatebox" data-options="width:150,prompt: '车号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text"  name="filter_LIKES_driverName" class="easyui-validatebox" data-options="width:150,prompt: '司机姓名'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text"  name="filter_LIKES_roomNum" class="easyui-validatebox" data-options="width:150,prompt: '库号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>

            <input type="text" name="filter_GED_appointDate" class="easyui-my97" datefmt="yyyy-MM-dd"   data-options="width:150,prompt: '预约日期（开始）'"/>
            <input type="text" name="filter_LED_appointDate" class="easyui-my97" datefmt="yyyy-MM-dd"   data-options="width:150,prompt: '预约日期（结束）'"/>

            <span class="toolbar-item dialog-tool-separator"></span>
            <select id="status" name="filter_EQS_status" class="easyui-combobox"
                    data-options="width:150,prompt: '状态'">
                <option></option>
                <option value="0">已保存</option>
                <option value="1">已入闸</option>
                <option value="2">已出闸</option>
                <option value="3">已取消</option>
            </select>

            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
            <span class="toolbar-item dialog-tool-separator"></span>

            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="synchronousInformation()">同步信息</a>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="cancel()">取消预约</a>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" data-options="disabled:false" onclick="update()">编辑</a>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" data-options="disabled:false" onclick="deleteMothed()">删除</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </form>
        <form id="searchFrom3" action="">
        </form>

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
            url: '${ctx}/platform/reservation/outbound/selectList',
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
                {field: 'yyid', title: '预约id', width: 100},
                {field: 'customerService', title: '客服',  width: 50},
                {field: 'appointDate', title: '预约出库日期',  width: 80},
                {field: 'consumeCompany', title: '客户名称',  width: 100},
                {field: 'billNo', title: '提单号',width: 60},
                {field: 'status', title: '状态', width: 100,
                    formatter:function (val,row,index){
                        if (val=="0"){
                            return "已保存";
                        }else if (val =="1"){
                            return "已入闸";
                        }else if (val =="2") {
                            return "已出闸";
                        }else if (val =="3") {
                            return "已取消";
                        }
                    }
                },
                {field: 'containerNo', title: '箱号',  width: 50

                },

                {field: 'originCountry', title: '原产国', width: 100},
                {field: 'productType', title: '货类', width: 50,
                    formatter:function (value, row, index){
                        if (value == "1"){
                            return '水产';
                        }else if (value == "2"){
                            return '肉类';
                        }else {
                            return '其他';
                        }
                    }
                },
                {
                    field: 'productName', title: '货物名称(品名)', width: 100},
                {
                    field: 'num', title: '件数', width: 50,
                },
                {field: 'weight', title: '重量',  width: 60},
                {
                    field: 'isBulkCargo', title: '是否散货', width: 100,
                    formatter: function (val, row, index) {
                        if (val == "1") {
                            return "散货";
                        } else if (val == "0") {
                            return "";
                        }
                    }
                },
                {field: 'roomNum', title: '库号', width: 70},
                {field: 'locationNo', title: '房间号', width: 70},
                {field: 'carNumber', title: '车牌号', width: 70},
                {field: 'queuingTime', title: '排队时间', width: 120},
                {field: 'driverName', title: '司机姓名', width: 100},
                {field: 'driverMobile', title: '电话', width:100},
                // {
                //     field: 'dirverIdNumber', title: '身份证号',width: 100,
                //
                // },
                // {
                //     field: 'desCompany', title: '货物流向目的地公司名称',width: 100
                // },
                // {
                //     field: 'province', title: '省',width: 70
                // },
                // {field: 'city', title: '市', width: 70},
                // {
                //     field: 'area', title: '区', width: 60,
                // },
                // {field: 'address', title: '详细地址',width: 200},
                // {field: 'desContactName', title: '目的地联系人',  width: 80},
                // {field: 'desContactPhone', title: '目的地联系电话',  width: 100},
                {
                    field: 'type', title: '类型', width: 60,
                    formatter: function (val, row, index) {
                        return val == "1" ? '系统生成' : '同步生成';
                    }
                }


            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });

    //创建查询对象并查询
    function cx(){
        dg.datagrid('clearSelections');
        var obj=$("#searchFrom").serializeObject();

        dg.datagrid('load',obj);
    }

    //取消预约
    function cancel(){
        var row = dg.datagrid('getSelected');
        if(rowIsNull(row)) return;

        if(row.type=='1'){
            parent.$.messager.alert("提示","该信息不能取消预约");
            return;
        }

        if(row.status!='0'){
            parent.$.messager.alert("提示","该状态不能取消预约");
            return;
        }
        var id = row.id;
        parent.$.messager.confirm('提示', '取消车号：'+row.carNumber+'预约,无法恢复您确定要取消？', function(data){
            if (data){
                $.ajax({
                    type:'get',
                    url:"${ctx}/platform/reservation/outbound/cancel/"+id,
                    success: function(data){
                        successTip(data,dg);
                    }
                });
            }
        });
    }

    //同步信息
    function synchronousInformation(){
        var row = dg.datagrid('getSelected');
        if(rowIsNull(row)) return;

        if(row.type=='1'){
            parent.$.messager.alert("提示","该信息不能同步");
            return;
        }

        if(row.status!='0'){
            parent.$.messager.alert("提示","该状态不能同步信息");
            return;
        }
        if(row.isBulkCargo!='1'){
            parent.$.messager.alert("提示","不是散货，不需要同步信息");
            return;
        }
        var id = row.id;
        parent.$.messager.confirm('提示', '是否同步：'+row.carNumber+'信息？', function(data){
            if (data){
                $.ajax({
                    type:'get',
                    url:"${ctx}/platform/reservation/outbound/synchronousInformation/"+id,
                    success: function(data){
                        successTip(data,dg);
                    }
                });
            }
        });
    }

    //编辑
    function update() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;

        if(row.type!='1'){
            parent.$.messager.alert("提示","该信息不能编辑");
            return;
        }

        d = $("#dlg").dialog({
            title: '编辑',
            width: 380,
            height: 380,
            href: '${ctx}/platform/reservationData/updateOutbound/' + row.id,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    if ($("#mainform").form('validate')) {
                        $("#mainform").submit();
                        d.panel('close');
                    }
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }],
            onClose: function () {
                window.setTimeout(function () {
                    cx()
                }, 100);
            }
        });
    }

    //删除
    function deleteMothed(){
        var row = dg.datagrid('getSelected');
        if(rowIsNull(row)) return;

        if(row.type!='1'){
            parent.$.messager.alert("提示","该信息不能删除");
            return;
        }
        var id = row.id;
        parent.$.messager.confirm('提示', '您确定要取消预约信息吗？', function(data){
            if (data){
                $.ajax({
                    type:'get',
                    url:"${ctx}/platform/reservationData/deleteOutbound/"+id,
                    success: function(data){
                        successTip(data,dg);
                    }
                });
            }
        });
    }
</script>
</body>
</html>