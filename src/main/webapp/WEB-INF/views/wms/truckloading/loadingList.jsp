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
        <form id="searchFrom" action="">
            <input type="text" id="loadingPlanNum" name="filter_LIKES_loadingPlanNum" class="easyui-validatebox"
                   data-options="width:150,prompt: '订单号'"/>
            <input type="text" id="loadingTruckNum" name="filter_LIKES_loadingTruckNum" class="easyui-validatebox"
                   data-options="width:150,prompt: '装车单号'"/>
            <input type="text" id="outLinkId" name="filter_LIKES_outLinkId" class="easyui-validatebox"
                   data-options="width:150,prompt: '出库联系单号'"/>
            <input type="text" id="billNum" name="filter_LIKES_billNum" class="easyui-validatebox"
                   data-options="width:150,prompt: '提单号'"/>
            <input type="text" id="carNo" name="filter_LIKES_carNo" class="easyui-validatebox"
                   data-options="width:150,prompt: '车牌号'"/>
			<input type="text" id="sku" name="filter_LIKES_skuId" class="easyui-validatebox"
                   data-options="width:150,prompt: 'SKU'"/>
            <select type="text" id="loadingState" name="filter_LIKES_loadingState"
                   class="easyui-combobox" data-options="width:150,prompt: '状态'">
                <option></option>
                <option value='0'>已分配</option>
                <option value='1'>已拣货</option>
                <option value='2'>已装车</option>
                <option value='5'>回库理货</option>
                <option value='6'>已回库</option>
            </select>

            <input type="text" id="operator" name="filter_LIKES_operator" class="easyui-validatebox"
                   data-options="width:150,prompt: '客服人员'"/>
            <select id="searchStock" name="filter_LIKES_stockId" class="easyui-combobox"
                    data-options="width:150,prompt: '存货方'">
            </select>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        </form>
        <shiro:hasPermission name="bis:truck:reload">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="opencl()">重新生成装车单</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:truck:reload">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="refFloor()">调整装车仓库</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:truck:reload">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="delcl()">删除装车单</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:truck:reload">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-lock" plain="true"
               onclick="ended()">完结</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:truck:print">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-print" plain="true" onclick="printT()">打印装车单</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:truck:printinfo">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-print" plain="true"
               onclick="printInfo()">打印随车清单</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        数量合计：<input type="text" id="allpiece" readonly/>
        净重合计：<input type="text" id="allnet" readonly/>
        毛重合计：<input type="text" id="allgross" readonly/>
    </div>

</div>
<table id="dg"></table>
<div id="dlg"></div>
<div id="dtg"></div>
<div id="win"></div>
<div>

</div>
<script type="text/javascript">
    var dg;
    var d, dt;
    var splitJson, tids;//用于记录需要拆分的托盘记录

    document.onkeydown = function () {
        if (event.keyCode == 13) {
            cx();
        }
    };

    $(document).ready(function () {
        //客户
        $('#searchStock').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote'
        });
        all();
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/bis/truck/listjson',
            fit: true,
            fitColumns: false,
            border: false,
            idField: 'id',
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'id', title: 'id', sortable: false, hidden: true},
                {field: 'trayId', title: '托盘号', sortable: false, width: 100},
                {field: 'outLinkId', title: '出库联系单', sortable: false, width: 180},
                {field: 'loadingPlanNum', title: '订单号', sortable: false, width: 150},
                {field: 'loadingTruckNum', title: '装车号', sortable: false, width: 150},
                {
                    field: 'loadingState', title: '装车状态', sortable: false, formatter: function (value, row, index) {
                    var retStr = "";
                    switch (value) {
                        case '1':
                            retStr = "已拣货";
                            break;
                        case '2':
                            retStr = "已装车";
                            break;
                        case '3':
                            retStr = "已置换";
                            break;
                        case '0':
                            retStr = "已分配";
                            break;
                        case '4':
                            retStr = "待回库";
                            break;
                        case '5':
                            retStr = "回库理货";
                            break;
                        case '6':
                            retStr = "已回库";
                            break;
                    }
                    return retStr;
                }
                },
                {field: 'loadingTime', title: '装车时间', sortable: false, width: 180},
                {
                    field: 'stockId', title: '存货方', sortable: false, width: 200,
                    formatter: function (value, row, index) {
                        var aa = "";
                        $.ajax({
                            async: false,
                            type: 'get',
                            url: "${ctx}/base/client/getname/" + value,
                            success: function (data) {
                                aa = data;
                            }
                        });
                        return aa;
                    }
                },
                {field: 'mscNum', title: 'msc号', sortable: false, width: 100},
                {field: 'lotNum', title: 'lot号', sortable: false, width: 100},
                {field: 'typeSize', title: '规格', sortable: false, width: 100},
                {field: 'carNo', title: '车牌号', sortable: false, width: 100},
                {field: 'operator', title: '客服人员', sortable: false, width: 100},
                {field: 'cargoName', title: '品名', sortable: false, width: 200},
                {field: 'piece', title: '件数', sortable: false, width: 100},
                {field: 'cargoLocation', title: '库位号', sortable: false, width: 100},
                {field: 'ctnNum', title: '箱号', sortable: false, width: 100},
                {field: 'skuId', title: 'SKU', sortable: false, width: 180},
                {field: 'loadingPerson', title: '理货员', sortable: false, width: 100},
                {field: 'libraryManager', title: '库管员', sortable: false, width: 100},
                {
                    field: 'enterState', title: '货物状态', sortable: false, formatter: function (value, row, index) {
                    if ("1" == value) {
                        return "货损"
                    } else if ("0" == value) {
                        return "成品"
                    }
                }
                }
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });

    //查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        all();
        dg.datagrid('load', obj);
    }

    function all() {
        $.ajax({
            async: false,
            type: "POST",
            url: "${ctx}/bis/truck/findall",
            data: {
                "loadingPlanNum": $("#loadingPlanNum").val(),
                "loadingTruckNum": $("#loadingTruckNum").val(),
                "outLinkId": $("#outLinkId").val(),
                "billNum": $("#billNum").val(),
                "carNo": $("#carNo").val(),
                "operator": $("#operator").val(),
                "stockId": $("#searchStock").combobox("getValue")
            },
            dataType: "json",
            success: function (date) {
                $("#allpiece").val(date[0]);
                $("#allnet").val(date[1]);
                $("#allgross").val(date[2]);
            }
        });
    }

    //打开策略选中页面
    function opencl() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        //执行异步校验 该订单是否可以生成装车单
        $.post("${ctx}/bis/truck/iscreate", {"ordid": row.loadingPlanNum}, function (data) {
            if (data != null) {
                if ("success" == data.endStr) {
                    createTruck(row.loadingPlanNum);
                } else if ("hsuccess" == data.endStr) {
                    parent.$.messager.confirm('提示', '订单号：' + row.loadingPlanNum + '已生成过装车单，确定后之前生成的装车单信息将清空，不可还原。您确定要重新生成吗？', function (data) {
                        if (data) {
                            createTruck(row.loadingPlanNum);
                        }
                    });
                } else if ("herror" == data.endStr) {
                    parent.$.easyui.messager.alert("订单号：" + row.loadingPlanNum + "的装车单状态不允许重新生成装车单 ！");
                } else {
                    parent.$.easyui.messager.alert("订单号：" + row.loadingPlanNum + " 不允许生成装车单！");
                }
            }
        }, "json");

    }

    //打开策略选择弹窗
    function createTruck2(ordid) {
        d = $("#dlg").dialog({
            title: '出库策略选择',
            width: 300,
            height: 300,
            href: '${ctx}/bis/loading/opencl/' + ordid,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    $('#clform').submit();
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }]
        });
    }
    function createTruck(ordid) {
        d = $("#dlg").dialog({
            title: '出库策略选择',
            width: 1000,
            height: 500,
            href: '${ctx}/base/stategy/check/' + ordid,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    $("#mainform").submit();
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }]
        });
    }

    //打开托盘拆分页面
    function openSplitT(ordid) {
        dt = $("#dtg").dialog({
            title: '托盘拆分',
            width: 500,
            height: 300,
            href: '${ctx}/bis/loading/openct/' + ordid,
            maximizable: true,
            modal: true,
            resizable: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    $('#ctform').submit();
                }
            }, {
                text: '返回策略选择',
                handler: function () {
                    dt.panel('close');
                    opencl();
                }
            }, {
                text: '取消',
                handler: function () {
                    dt.panel('close');
                }
            }]
        });
    }
    //删除装车单
    function delcl() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        $.post("${ctx}/bis/truck/isdel/" + row.loadingTruckNum, {}, function (data) {
            if (data.endstr == "truckerror") {
                parent.$.easyui.messager.alert("装车单号：" + row.loadingTruckNum + "当前状态不允许删除！");
            } else if (data.endstr == "outerror") {
                parent.$.easyui.messager.alert("装车单号：" + row.loadingTruckNum + " 出库联系单已经生成最后一车订单！");
            } else if (data.endstr == "error") {
                parent.$.easyui.messager.alert("校验装车单号：" + row.loadingTruckNum + "是否可删除失败！");
            } else {
                parent.$.messager.confirm('提示', '确定要删除该装车单吗', function (data) {
                    if (data) {
                        zxdel(row.loadingPlanNum);
                    }
                });
            }

        }, "json");
    }
    //执行删除操作
    function zxdel(ordNum) {
        $.post("${ctx}/bis/truck/zxdel/" + ordNum, {}, function (data) {
            cx();
        }, "json");
    }

    function printT() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        //window.parent.open("${ctx}/bis/truck/print/" + row.loadingTruckNum);

        window.parent.mainpage.mainTabs.addModule('打印','/ccli/bis/truck/print/'+row.loadingTruckNum);
    }

    function printInfo() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        $.ajax({
            type: "POST",
            url: "${ctx}/wms/outstock/iffx/"+ row.outLinkId+"/"+row.loadingPlanNum,
            dataType: "text",
            success: function (date) {
                if (date == "success") {
                    window.parent.open("${ctx}/bis/truck/printinfo/" + row.loadingTruckNum);
                } else {
                    parent.$.messager.show({title: "提示", msg:date, position: "bottomRight"});
                    return;
                }
            }
        });

    }

    // 装车单号可否完结查询
    function ended() {

        var row = dg.datagrid('getSelected');
        if (rowIsNull(row))return;

        $.get("${ctx}/bis/truck/endOrderFind/" + row.loadingTruckNum, function (data) {

            if (data == '0') {
                parent.$.messager.show({
                    title: "提示",
                    msg: "装车号(" + row.loadingPlanNum + ")中托盘已经全部装车,不能完结。",
                    position: "bottomRight"
                });
            } else if (data == '1') {
                parent.$.messager.show({
                    title: "提示",
                    msg: "装车号(" + row.loadingPlanNum + ")中有托盘已经拣货,不能完结。",
                    position: "bottomRight"
                });
            } else if (data == '5') {
                parent.$.messager.show({
                    title: "提示",
                    msg: "装车号(" + row.loadingPlanNum + ")中有托盘回库理货,不能完结。",
                    position: "bottomRight"
                });
            } else if (data == '6') {
                parent.$.messager.show({
                    title: "提示",
                    msg: "装车号(" + row.loadingPlanNum + ")中托盘已经全部回库,不能完结。",
                    position: "bottomRight"
                });
            } else {
                parent.$.messager.confirm('提示', data, function (data) {
                    if (data)endedOrder(row.loadingTruckNum, row.outLinkId, row.loadingPlanNum)
                });
            }

        });
    }

    // 装车订单完结
    function endedOrder(loadingTruckNum, outLinkId, loadingPlanNum) {
        $.post("${ctx}/bis/truck/endOrder/" + loadingTruckNum + "/" + outLinkId + "/" + loadingPlanNum, function (data) {

            if (data == "ok") {
                parent.$.messager.show({
                    title: "提示",
                    msg: "装车号(" + loadingTruckNum + ")完结操作成功",
                    position: "bottomRight"
                });

                cx(); //完结操作成功,刷新表格

            } else {
                parent.$.messager.show({
                    title: "提示",
                    msg: "装车号(" + loadingTruckNum + ")完结操作失败",
                    position: "bottomRight"
                });
            }

        });
    }

    //打开重新选择仓库
    function refFloor() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;

        //执行异步校验 该订单是否可以生成装车单
        parent.$.messager.confirm('提示', '是否确认调整订单：' + row.loadingPlanNum + '的拣货仓库吗？', function (data) {
            if (data) {
                chooseFloor(row.loadingPlanNum);
            }
        });

    }

    // 打开拣货仓库选择页面
    function chooseFloor(ordid) {
        d = $("#dlg").dialog({
            title: '拣货仓库选择',
            width: 1000,
            height: 500,
            href: '${ctx}/base/floor/choose/' + ordid,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    var row = dglx.datagrid('getSelected');
                    console.log(row);

                    if (!row || !row.floorNum) {
                        parent.$.messager.show({
                            title: "提示",
                            msg: "请选择要变更的仓库",
                            position: "bottomRight"
                        });
                        return;
                    }

                    $.ajax({
                        async: false,
                        type: 'POST',
                        url: "${ctx}/base/floor/refFloor/" + ordid + "/" + row.floorNum,
                        data: {
                            "ordid": ordid,
                            "floorNum": row.floorNum
                        },
                        success: function (data) {
                            console.log(data);
                            if (data.code === "200") {
                                if (data.needSplit == "Y") {
                                    d.panel('close');
                                    // 需要拆托
                                    tids = data.trayIds;
                                    splitJson = data.splitTray;
                                    openSplitT(ordid);
                                } else {
                                    cx();
                                    d.panel('close');
                                }
                            } else {
                                parent.$.messager.show({
                                    title: "提示",
                                    msg: data.msg,
                                    position: "bottomRight"
                                });
                            }
                        }
                    });

                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }]
        });
    }

</script>
</body>
</html>