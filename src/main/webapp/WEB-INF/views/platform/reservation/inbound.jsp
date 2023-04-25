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

    </div>
</div>

<table id="dg"></table>--%>

<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="预约入库记录" style="overflow-y:auto">
    <div style="padding:5px;height:auto" class="datagrid-toolbar">
        <form id="searchFrom" action="">
            <input type="text" name="filter_LIKES_billNo" class="easyui-validatebox"
                   data-options="width:150,prompt: '提单号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text" name="filter_LIKES_containerNo" class="easyui-validatebox"
                   data-options="width:150,prompt: '箱号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text" name="filter_LIKES_carNumber" class="easyui-validatebox"
                   data-options="width:150,prompt: '车牌号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>


            <input type="text" name="filter_GED_appointDate" class="easyui-my97" datefmt="yyyy-MM-dd"
                   data-options="width:150,prompt: '预约日期（开始）'"/>
            <input type="text" name="filter_LED_appointDate" class="easyui-my97" datefmt="yyyy-MM-dd"
                   data-options="width:150,prompt: '预约日期（结束）'"/>

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

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="cancel()">取消预约</a>
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
            url: '${ctx}/platform/reservation/inbound/selectList',
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
                {field: 'customerService', title: '客服', width: 50},
                {field: 'consumeCompany', title: '客户名称', width: 100},
                {field: 'billNo', title: '提单号', width: 100},
                {
                    field: 'status', title: '状态', width: 70,
                    formatter: function (val, row, index) {
                        if (val == "0") {
                            return "已保存";
                        } else if (val == "1") {
                            return "已入闸";
                        } else if(val == "2"){
                            return "已出闸";
                        }else if(val == "3"){
                            return "已取消";
                        }
                    }
                },
                {field: 'containerNo', title: '箱号', width: 70},
                {field: 'queuingTime', title: '排队时间', width: 120},
                {field: 'productName', title: '品名', width: 50},
                {
                    field: 'productType', title: '货类', width: 50,
                    formatter: function (value, row, index) {
                        if (value == "1") {
                            return '水产';
                        } else if (value == "2") {
                            return '肉类';
                        } else {
                            return '其他';
                        }
                    }
                },
                {field: 'num', title: '件数', width: 50},
                {field: 'netWeight', title: '净重（KG)', width: 50},
                {field: 'carNumber', title: '车牌号', width: 70,},
                {field: 'driverName', title: '司机姓名', width: 70},
                {field: 'driverMobile', title: '联系电话', width: 100},
                {field: 'driverIdNumber', title: '身份证号', width: 100},
                {field: 'appointDate', title: '预约日期', width: 100},
                {field: 'storageTemperature', title: '存放温度', width: 60},
                {
                    field: 'isCheck', title: '是否查验', width: 60,
                    formatter: function (val, row, index) {
                        return val == "1" ? '是' : '否';
                    }
                },
                {
                    field: 'isFreetax', title: '是否保税', width: 60,
                    formatter: function (val, row, index) {
                        return val == "1" ? '是' : '否';
                    }
                },
                {
                    field: 'isZdmt', title: '是否带木托', width: 60,
                    formatter: function (val, row, index) {
                        return val == "1" ? '是' : '否';
                    }
                },
                {field: 'sealNo', title: '铅封号', width: 60},
                {field: 'originCountry', title: '原产国', width: 100},
                {field: 'factoryNo', title: '厂号', width: 50},
                {field: 'reportNumber', title: '报关单号', width: 100},
                {field: 'shippingPort', title: '启运港', width: 100},
                {field: 'txmtgs', title: '提箱码头公司', width: 100},
                {field: 'arrivePortTime', title: '冻柜到港时间', width: 130},
                {field: 'customsClearanceTime', title: '海关放行时间', width: 130},
                {field: 'reportBillContact', title: '报关单境内收货人', width: 100},
                {field: 'reportBillConsignor', title: '报关单境内发货人', width: 100},
                {field: 'checkInstructions', title: '查验指令', width: 60},
                {field: 'dgfkcz', title: '冻柜返空场站', width: 100},
                {field: 'customsDeclarationValue', title: '报关单货值（美元）', width: 70},
                {field: 'disinfectionReportCustomer', title: '消毒报告客户名称', width: 70},
                {field: 'nucleicAcidProofCustomer', title: '核酸证明客户名', width: 100},
                {field: 'yyid', title: '预约id', width: 100}

            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });

    //创建查询对象并查询
    function cx() {
        dg.datagrid('clearSelections');
        var obj = $("#searchFrom").serializeObject();

        dg.datagrid('load', obj);
    }

    //取消预约
    function cancel(){
        var row = dg.datagrid('getSelected');

        if(rowIsNull(row)) return;

        if(row.status!='0'){
            parent.$.messager.alert("提示","该状态不能取消预约");
            return;
        }
        var id = row.id;
        parent.$.messager.confirm('提示', '取消车号：'+row.carNumber+'预约,无法恢复您确定要取消？', function(data){
            if (data){
                $.ajax({
                    type:'get',
                    url:"${ctx}/platform/reservation/inbound/cancel/"+id,
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