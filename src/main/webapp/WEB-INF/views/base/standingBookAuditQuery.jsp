<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
    <div>
        <form id="searchFrom" action="">
            <select class="easyui-combobox" id="customID" name="customID" data-options="width:150,prompt:'账单客户'">
                <option value=""></option>
            </select>
            <input type="text" id="codeNum" name="codeNum" class="easyui-validatebox" data-options="width:150,validType:'length[1,20]',prompt:'账单编号'" />
            <input id="yearMonth" name="yearMonth" type="text" class="easyui-my97" datefmt="yyyy-MM" data-options="width:150,prompt:'账单年月'" />
            <select class="easyui-combobox" id="isTrue" name="isTrue" data-options="width:150,prompt:'是否确认'">
                <option value=""></option>
                <option value="1">是</option>
                <option value="2">否</option>
            </select>
            <select class="easyui-combobox" id="auditState" name="auditState" data-options="width:150,prompt:'审批状态'">
                <option value=""></option>
                <option value="0">未审批</option>
                <option value="1">已审批</option>
            </select>
            <input id="startTime" name="startTime" type="text" class="easyui-my97" datefmt="yyyy-MM-dd" value="${defaultStartTime}" data-options="width:150,prompt:'制单开始时间'" />
            <input id="endTime" name="endTime" type="text" class="easyui-my97" datefmt="yyyy-MM-dd" value="${defaultEndTime}" data-options="width:150,prompt:'制单结束时间'" />
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        </form>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-application-view-detail" plain="true" onclick="detail()">详情</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" plain="true" onclick="batchApprove()">一键审批</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="batchDelete()">删除</a>
    </div>
</div>
<table id="dg"></table>
<script type="text/javascript">
var dg;
document.onkeydown = function () {if(event.keyCode == 13){cx();}};
$(document).ready(function(){
    dg = $('#dg').datagrid({
        method: "get",
        url:'${ctx}/cost/standingBook/auditlistjson',
        fit : true,
        fitColumns : true,
        border : false,
        idField : 'codeNum',
        striped:true,
        pagination:true,
        rownumbers:true,
        pageNumber:1,
        pageSize : 20,
        pageList : [10, 20, 30, 40, 50],
        singleSelect:false,
        checkOnSelect:true,
        selectOnCheck:true,
        columns:[[
            {field:'ck',checkbox:true},
            {field:'auditState',title:'审批状态',sortable:false,width:60,
                formatter : function(value) {
                    if(value == 1 || value == '1'){return '已审批';}
                    return '未审批';
                }
            },
            {field:'isTrue',title:'是否确认',sortable:false,width:50,
                formatter : function(value) {
                    if(value == 1 || value == '1'){return '是';}
                    return '否';
                }
            },
            {field:'jsfs',title:'结算方式',sortable:false,width:50,
                formatter : function(value) {
                    if(value == 'Y'){return "<font color='red'>月结</font>";}
                    if(value == 'X'){return "<font color='green'>现结</font>";}
                    return "";
                }
            },
            {field:'codeNum',title:'账单编号',sortable:false,width:90},
            {field:'custom',title:'账单客户',sortable:false,width:120},
            {field:'customID',title:'客户编号',sortable:false,width:70},
            {field:'yearMonth',title:'账单年月',sortable:false,width:70},
            {field:'operator',title:'制单人',sortable:false,width:60},
            {field:'operateTime',title:'制单日期',sortable:false,width:90},
            {field:'remark',title:'备注',sortable:false,width:120}
        ]],
        enableHeaderClickMenu: true,
        enableHeaderContextMenu: true,
        enableRowContextMenu: false,
        toolbar:'#tb'
    });
    $('#customID').combobox({
        method:"GET",
        url:"${ctx}/base/client/getClientAll?tim=1",
        valueField: 'ids',
        textField: 'clientName',
        mode:'remote'
    });
});

function cx(){
    var obj = $("#searchFrom").serializeObject();
    dg.datagrid('load', obj);
}

function detail(){
    var row = dg.datagrid('getSelected');
    if(rowIsNull(row)) return;
    var href = 'cost/standingBook/auditdetail/' + row["codeNum"];
    window.parent.mainpage.mainTabs.addModule('对账单明细', href, 'icon-hamburg-attibutes');
}

function getCheckedCodeNums(requireUnapproved) {
    var rows = dg.datagrid('getChecked');
    if (rows == null || rows.length == 0) {
        parent.$.messager.alert('提示', '请至少选择一条数据。');
        return null;
    }
    var codeNums = [];
    for (var i = 0; i < rows.length; i++) {
        if (requireUnapproved && (rows[i].auditState == 1 || rows[i].auditState == '1')) {
            parent.$.messager.alert('提示', '已审批的数据不能重复审批。');
            return null;
        }
        codeNums.push(rows[i].codeNum);
    }
    return codeNums.join(',');
}

function batchApprove() {
    var codeNums = getCheckedCodeNums(true);
    if (codeNums == null) return;
    submitBatch('approveAutoBatch', codeNums, '确认审批通过已选择的对账单？', '一键审批成功。');
}

function batchDelete() {
    var codeNums = getCheckedCodeNums(false);
    if (codeNums == null) return;
    submitBatch('deleteAutoBatch', codeNums, '确认删除已选择的对账单？', '删除成功。');
}

function submitBatch(action, codeNums, message, successMessage) {
    parent.$.messager.confirm('提示', message, function (data) {
        if (data) {
            $.ajax({
                type: 'post',
                url: "${ctx}/cost/standingBook/" + action,
                data: {codeNums: codeNums},
                success: function (data) {
                    if ("success" == data) {
                        parent.$.easyui.messager.show({title: "操作提示", msg: successMessage, position: "bottomRight"});
                        dg.datagrid('clearChecked');
                        cx();
                    } else {
                        parent.$.messager.alert('提示', '操作失败，请检查所选数据状态。');
                        cx();
                    }
                }
            });
        }
    });
}
</script>
</body>
</html>
