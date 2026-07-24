<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body class="easyui-layout" style="font-family:'Microsoft YaHei'">
<form id="autoDetailForm" action="${ctx}/cost/standingBook/saveAutoDetail" method="post">
    <div data-options="region:'north',split:true,border:false" style="height:145px">
        <div class="easyui-layout" data-options="fit:true">
            <div class="datagrid-toolbar" data-options="region:'north',split:false,border:false" style="height:auto;">
                <shiro:hasPermission name="bis:checkbook:update">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveAutoDetail()">保存</a>
                    <span class="toolbar-item dialog-tool-separator"></span>
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addInfo()">添加</a>
                    <span class="toolbar-item dialog-tool-separator"></span>
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
                </shiro:hasPermission>
            </div>
            <div data-options="region:'center',split:false,border:false" style="padding:8px 10px 0 10px;">
                <table>
                    <tr>
                        <td style="width:70px;">是否确认：</td>
                        <td>
                            <select id="isTrue" name="isTrue" class="easyui-combobox" data-options="width:150,required:'required'">
                                <option value="1">是</option>
                                <option value="2">否</option>
                            </select>
                        </td>
                        <td style="width:70px;">结算方式：</td>
                        <td>
                            <select id="jsfs" name="jsfs" class="easyui-combobox"
                                    data-options="width:150,required:'required',valueField:'id',textField:'text',data:[{id:'Y',text:'月结'},{id:'X',text:'现结'}]"></select>
                        </td>
                        <td style="width:80px;">对账单号：</td>
                        <td><input id="codeNum" name="codeNum" class="easyui-validatebox" readonly="readonly" data-options="width:150" value="${obj.codeNum}"/></td>
                        <td style="width:70px;">账单客户：</td>
                        <td>
                            <input class="easyui-validatebox" readonly="readonly" data-options="width:180" value="${obj.custom}"/>
                            <select id="customID" name="customID" style="display:none;"></select>
                        </td>
                        <td style="width:70px;">账单年月：</td>
                        <td><input class="easyui-validatebox" readonly="readonly" data-options="width:100" value="${obj.yearMonth}"/></td>
                    </tr>
                    <tr style="height:5px;"></tr>
                    <tr>
                        <td>制单人：</td>
                        <td><input class="easyui-validatebox" readonly="readonly" data-options="width:150" value="${obj.operator}"/></td>
                        <td>备注：</td>
                        <td colspan="7"><textarea id="remark" name="remark" rows="2" cols="110" style="font-size:12px;font-family:'Microsoft YaHei'">${obj.remark}</textarea></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div data-options="region:'center',split:true,border:false">
        <div id="zdiv1" class="easyui-panel" data-options="title:'入库列表',height:100">
            <table id="dg1"></table>
        </div>
        <div style="height:5px;"></div>
        <div id="zdiv2" class="easyui-panel" data-options="title:'出库列表',height:100">
            <table id="dg2"></table>
        </div>
        <div style="height:5px;"></div>
        <div id="zdiv3" class="easyui-panel" data-options="title:'货转列表',height:100">
            <table id="dg3"></table>
        </div>
    </div>
    <div id="dlg"></div>
</form>
<script type="text/javascript">
var codeNum = '${obj.codeNum}';
var d;

$(document).ready(function () {
    $("#isTrue").combobox("setValue", "${obj.isTrue}");
    $("#jsfs").combobox("setValue", "${obj.jsfs}");
    $("#customID").combobox({
        method: "GET",
        url: "${ctx}/base/client/getClientAll?tim=1&setid=${obj.customID}",
        valueField: 'ids',
        textField: 'clientName',
        mode: 'remote',
        onLoadSuccess: function () {
            if ("${obj.customID}" != "") {
                $("#customID").combobox("select", Number("${obj.customID}"));
            }
        }
    });
    inTInfo("dg1", 1);
    inTInfo("dg2", 2);
    inTInfo("dg3", 3);
    $("#autoDetailForm").form({
        onSubmit: function () {
            return $(this).form("validate");
        },
        success: function (data) {
            if ("success" == data) {
                parent.$.easyui.messager.show({title: "操作提示", msg: "保存成功", position: "bottomRight"});
                refreshAuditList();
            } else {
                parent.$.messager.alert("提示", "保存失败，请检查当前数据状态。");
            }
        }
    });
});

function saveAutoDetail() {
    $("#autoDetailForm").submit();
}

function inTInfo(sTableid, nType) {
    $('#' + sTableid).datagrid({
        method: "post",
        url: '${ctx}/cost/standingBook/auditdetailjson',
        fit: true,
        fitColumns: true,
        border: false,
        striped: true,
        pagination: false,
        rownumbers: true,
        columns: [[
            {field: 'chekbox', title: '选择', checkbox: true, sortable: false, width: 50},
            {field: 'LINK_ID', title: '联系单', sortable: false, width: 100},
            {field: 'BILL_NUM', title: '提单号', sortable: false, width: 100},
            {field: 'CUSTOMS_NAME', title: '委托单位', sortable: false, width: 100},
            {field: 'Money', title: '费用金额', sortable: false, width: 80},
            {field: 'FEE_NAME', title: '明细费用名称', sortable: false, width: 100},
            {field: 'RMB', title: '明细金额', sortable: false, width: 80},
            {
                field: 'CURRENCY', title: '币种', sortable: false, width: 80,
                formatter: function (value) {
                    if (value == '0') { return "人民币"; }
                    if (value == '1') { return "美元"; }
                    if (value == '2') { return "日元"; }
                    if (value == '201') { return "阿尔及利亚第纳尔"; }
                    return value;
                }
            },
            {field: 'EXCHANGE_RATE', title: '汇率', sortable: false, width: 70},
            {
                field: 'BILL_DATE', title: '年月', sortable: false, width: 80,
                formatter: function (value) {
                    if (value != null && value != "") {
                        var nindex = value.lastIndexOf("-");
                        return nindex > 0 ? value.substring(0, nindex) : value;
                    }
                    return value;
                }
            },
            {
                field: 'PAY_SIGN', title: '是否垫付', sortable: false, width: 70,
                formatter: function (value) {
                    return value == 1 ? '是' : '否';
                }
            },
            {field: 'IDS', title: '', hidden: true}
        ]],
        queryParams: {
            nType: nType,
            codeNum: codeNum
        },
        onLoadSuccess: function () {
            mergeRows(sTableid, nType);
        },
        enableHeaderClickMenu: true,
        enableHeaderContextMenu: true,
        enableRowContextMenu: false
    });
}

function mergeRows(sTableid, nType) {
    var rows = $("#" + sTableid).datagrid('getRows');
    if (rows == null || rows.length == 0) {
        $("#zdiv" + nType).panel('resize', {height: 100});
        return;
    }
    var linkId = "";
    var startIndex = 0;
    var rowSpan = 0;
    var money = 0;
    for (var i = 0; i < rows.length; i++) {
        var row = rows[i];
        if (linkId != "" && linkId != row["LINK_ID"]) {
            mergeGroup(sTableid, startIndex, rowSpan, money);
            linkId = row["LINK_ID"];
            startIndex = i;
            rowSpan = 1;
            money = Number(row["RMB"]);
        } else {
            if (linkId == "") {
                linkId = row["LINK_ID"];
                startIndex = i;
            }
            rowSpan++;
            money += Number(row["RMB"]);
        }
        if (i == rows.length - 1) {
            mergeGroup(sTableid, startIndex, rowSpan, money);
        }
    }
    $("#zdiv" + nType).panel('resize', {height: rows.length * 25 + 100});
}

function mergeGroup(sTableid, startIndex, rowSpan, money) {
    $("#" + sTableid).datagrid('updateRow', {
        index: startIndex,
        row: {Money: money.toFixed(2)}
    });
    var fields = ['LINK_ID', 'BILL_NUM', 'CUSTOMS_NAME', 'Money'];
    for (var i = 0; i < fields.length; i++) {
        $("#" + sTableid).datagrid('mergeCells', {
            index: startIndex,
            field: fields[i],
            rowspan: rowSpan,
            colspan: 1
        });
    }
}

function addInfo() {
    d = $("#dlg").dialog({
        title: '对账单管理添加明细',
        width: 1000,
        height: 500,
        href: '${ctx}/cost/standingBook/addInfo/${obj.codeNum}',
        maximizable: true,
        modal: true,
        buttons: [{
            text: '确认',
            handler: function () {
                var ids1 = getTabCheckIds("sdg1");
                var ids2 = getTabCheckIds("sdg2");
                var ids3 = getTabCheckIds("sdg3");
                postAddAutoInfo(ids1, ids2, ids3);
            }
        }, {
            text: '取消',
            handler: function () {
                d.panel('close');
            }
        }]
    });
}

function postAddAutoInfo(ids1, ids2, ids3) {
    $.post('${ctx}/cost/standingBook/postaddAutoInfo', {
        code: codeNum,
        ids1: ids1,
        ids2: ids2,
        ids3: ids3
    }, function (data) {
        if (data != null && "success" == data.endStr) {
            d.panel('close');
            reloadDetails();
        } else {
            parent.$.messager.alert("提示", "添加失败，请检查选择的明细。");
        }
    }, "json");
}

function delInfo() {
    parent.$.messager.confirm('提示', '确认删除选中的对账单明细？', function (data) {
        if (data) {
            $.post('${ctx}/cost/standingBook/postdelAutoInfo', {
                code: codeNum,
                ids1: getTabCheckIds("dg1"),
                ids2: getTabCheckIds("dg2"),
                ids3: getTabCheckIds("dg3")
            }, function (data) {
                if (data != null && "success" == data.endStr) {
                    reloadDetails();
                } else {
                    parent.$.messager.alert("提示", "删除失败，请先勾选要删除的明细。");
                }
            }, "json");
        }
    });
}

function getTabCheckIds(tabId) {
    var reIds = "";
    var rows = $("#" + tabId).datagrid('getChecked');
    if (rows != null && rows.length > 0) {
        for (var i = 0; i < rows.length; i++) {
            if (rows[i]["IDS"] == null || rows[i]["IDS"] == "") {
                continue;
            }
            reIds = reIds == "" ? rows[i]["IDS"] : reIds + "," + rows[i]["IDS"];
        }
    }
    return reIds;
}

function reloadDetails() {
    inTInfo("dg1", 1);
    inTInfo("dg2", 2);
    inTInfo("dg3", 3);
    refreshAuditList();
}

function refreshAuditList() {
    var tabs = window.parent.$("#mainTabs");
    var tab = tabs.tabs("getTab", "审批对账单");
    if (tab) {
        var index = tabs.tabs("getTabIndex", tab);
        tabs.tabs("refresh", index);
    }
}
</script>
</body>
</html>
