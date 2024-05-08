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
        <form id="searchFrom" action="" method="post">
            <span style="padding: 15px">
            客户编号：
            <input type="text" name="customerCode" class="easyui-validatebox" data-options="width:120"/>
            </span>

            <span style="padding: 15px">
            客户名称：
            <input type="text" name="customerName" class="easyui-validatebox" data-options="width:120"/>
            </span>

            <span style="padding: 15px">
            操作状态:
            <select id="operState" name="operState" data-options="width:80">
                <option value="1">成功</option>
                <option value="0">失败</option>
                <option value="2">待审核</option>
                <option value="3">审核通过</option>
                <option value="4">审核拒绝</option>
            </select>
            </span>

            <span style="padding: 15px">
            操作类型:
            <select id="operType" name="operType" data-options="width:80">
                <option value="ZYCheck">质押校验</option>
                <option value="JYCheck">解押校验</option>
                <option value="ZYOper">质押操作</option>
                <option value="JYOper">解押操作</option>
            </select>
            </span>

            <span class="toolbar-item dialog-tool-separator"></span>

            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>

        </form>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true"
           onclick="detailIfo()">详情信息</a>
        <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" plain="true"
           onclick="approvedPass()">审核通过</a>
        <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-no" plain="true"
           onclick="auditReject()">审核拒绝</a>
        <span class="toolbar-item dialog-tool-separator"></span>

    </div>

</div>

<table id="dg"></table>

<div id="dlg"></div>

<script type="text/javascript">

    var dg;
    var dlg;

    document.onkeydown = function () {
        if (event.keyCode == 13) cx();
    };

    //创建查询对象并查询
    function cx() {
        dg.datagrid('load', $("#searchFrom").serializeObject());
    }

    $(function () {
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/wms/bulk/page',
            fit: true,
            fitColumns: true,
            border: false,
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'customerCode', title: '客户编号', hidden: true},
                {field: 'customerName', title: '客户名称', sortable: true, width: 100},
                {field: 'endPosition', title: '场位', sortable: true, width: 80},
                {
                    field: 'operCode', title: '操作编码', sortable: true, width: 100,
                },
                {
                    field: 'operType', title: '操作类型', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        if (value == "ZYCheck") {
                            return "质押校验";
                        } else if (value == "JYCheck") {
                            return "解押校验";
                        }else if (value == "ZYOper") {
                            return "质押操作";
                        }else if (value == "JYOper") {
                            return "解押操作";
                        }
                    }
                },
                {field: 'mkDd', title: '申请时间', sortable: true, width: 100},
                {field: 'execDd', title: '执行时间', sortable: true, width: 100},
                {field: 'operState', title: '操作状态', sortable: true, width: 150,
                    formatter: function (value, row, index) {
                        if (value == "0") {
                            return "失败";
                        } else if (value == "1") {
                            return "成功";
                        }else if (value == "2") {
                            return "待审核";
                        }else if (value == "3") {
                            return "审核通过";
                        }else if (value == "4") {
                            return "审核拒绝";
                        }
                    }
                },
                {field: 'rejectReason', title: '拒绝原因', sortable: true, width: 100}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });

    });

    function detailIfo() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        dlg = $("#dlg").dialog({
            title: '详情信息',
            width: 450,
            height: 380,
            href: '${ctx}/wms/bulk/form/' + row.operCode,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '关闭',
                handler: function () {
                    dlg.panel('close');
                }
            }]
        });

    }

    /**
     * 审核拒绝
     */
    function auditReject() {
        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;


        parent.$.messager.prompt('提示', '请输入拒绝原因。', function(content){
            if (content){
                var url = "${ctx}/wms/bulk/auditReject?id=" + row.operCode+":"+content;
                $.get(url, function (data) {
                    if (data == "success") {
                        toast("'审核拒绝'成功！");
                        cx();
                    } else {
                        toast("'审核拒绝'失败");
                    }
                });
            }else{
                parent.$.messager.show({ title : "提示",msg: "请输入拒绝原因！", position: "bottomRight" });
                return;
            }
        });

    }

    /**
     * 审核通过
     */
    function approvedPass() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        var url = "${ctx}/wms/bulk/approvedPass?id=" + row.operCode;

        $.get(url, function (data) {

            if (data == "success") {
                toast("'审核通过'成功！");
                cx();
            } else {
                toast("'审核通过'失败！");
            }

        });

    }

    function toast(msg) {
        parent.$.messager.show({title: "提示", msg: msg, position: "bottomRight"});
    }

</script>
</body>
</html>