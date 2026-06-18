<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body class="easyui-layout" style="font-family:'Microsoft YaHei'">
<div data-options="region:'north',split:true,border:false" style="height:145px">
    <div class="easyui-layout" data-options="fit:true">
        <div class="datagrid-toolbar" data-options="region:'north',split:false,border:false" style="height:auto;">
            <shiro:hasPermission name="bis:checkbook:update">
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" plain="true" onclick="approvePass()">审批通过</a>
            </shiro:hasPermission>
        </div>
        <div data-options="region:'center',split:false,border:false" style="padding:8px 10px 0 10px;">
            <table>
                <tr>
                    <td style="width:70px;">是否确认：</td>
                    <td><input id="isTrueText" class="easyui-validatebox" readonly="readonly" data-options="width:150"/></td>
                    <td style="width:70px;">结算方式：</td>
                    <td><input id="jsfsText" class="easyui-validatebox" readonly="readonly" data-options="width:150"/></td>
                    <td style="width:80px;">对账单号：</td>
                    <td><input id="codeNum" class="easyui-validatebox" readonly="readonly" data-options="width:150" value="${obj.codeNum}"/></td>
                    <td style="width:70px;">账单客户：</td>
                    <td><input class="easyui-validatebox" readonly="readonly" data-options="width:180" value="${obj.custom}"/></td>
                    <td style="width:70px;">账单年月：</td>
                    <td><input class="easyui-validatebox" readonly="readonly" data-options="width:100" value="${obj.yearMonth}"/></td>
                </tr>
                <tr style="height:5px;"></tr>
                <tr>
                    <td>制单人：</td>
                    <td><input class="easyui-validatebox" readonly="readonly" data-options="width:150" value="${obj.operator}"/></td>
                    <td>备注：</td>
                    <td colspan="7"><textarea rows="2" cols="110" readonly="readonly" style="font-size:12px;font-family:'Microsoft YaHei'">${obj.remark}</textarea></td>
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
<script type="text/javascript">
var codeNum = '${obj.codeNum}';
$(document).ready(function () {
    $("#isTrueText").val('${obj.isTrue}' == '1' ? '是' : '否');
    $("#jsfsText").val('${obj.jsfs}' == 'Y' ? '月结' : ('${obj.jsfs}' == 'X' ? '现结' : ''));
    inTInfo("dg1", 1);
    inTInfo("dg2", 2);
    inTInfo("dg3", 3);
});

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

function approvePass() {
    submitAudit('approveAuto', '确认审批通过当前对账单？', '审批通过成功。');
}

function submitAudit(action, message, successMessage) {
    if (codeNum == null || codeNum == "") {
        parent.$.messager.alert('提示', '未找到待审批对账单。');
        return;
    }
    parent.$.messager.confirm('提示', message, function (data) {
        if (data) {
            $.ajax({
                type: 'post',
                url: "${ctx}/cost/standingBook/" + action + "/" + codeNum,
                success: function (data) {
                    if ("success" == data) {
                        parent.$.easyui.messager.show({title: "操作提示", msg: successMessage, position: "bottomRight"});
                        refreshAuditList();
                        window.parent.mainpage.mainTabs.closeCurrentTab();
                    } else {
                        parent.$.messager.alert('提示', '审批处理失败，请检查当前数据状态。');
                        refreshAuditList();
                        window.parent.mainpage.mainTabs.closeCurrentTab();
                    }
                }
            });
        }
    });
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
