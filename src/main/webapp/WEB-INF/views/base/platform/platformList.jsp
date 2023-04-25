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
        
            <input type="text" name="filter_LIKES_platform" class="easyui-validatebox"
                   data-options="width:150,prompt: '月台口'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        </form>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
               onclick="add()">添加</a>
            <span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
               onclick="upd()">修改</a>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
               data-options="disabled:false" onclick="del()">删除</a>
            <span class="toolbar-item dialog-tool-separator"></span>
    </div>

</div>
<table id="dg"></table>
<div id="dlg"></div>
<script type="text/javascript">
    var dg;
    var d;
    $(document).ready(function () {
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/base/platform/json',
            fit: true,
            fitColumns: true,
            border: false,
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 10,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'id', title: 'id', hidden:true},
                {field: 'platform', title: '月台口', sortable: true, width: 50}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });

    //添加
    function add() {
        d = $("#dlg").dialog({
            title: "月台口添加",
            width: 450,
            height: 450,
            href: '${ctx}/base/platform/create',
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    $("#mainform").submit();
                     d.panel('close');
                     cx();
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }]
        });
    }

//修改
    function upd() {
        d = $("#dlg").dialog({
            title: "月台口修改",
            width: 450,
            height: 450,
            href: '${ctx}/base/platform/update',
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    $("#mainform").submit();
                     d.panel('close');
                     cx();
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }]
        });
    }
    
    //删除
    function del() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
            if (data) {
                $.ajax({
                    type: 'get',
                    url: "${ctx}/base/platform/delete/" + row.id,
                    success: function (data) {
                    	if(data=="success"){
                        	successTip(data, dg);
                        }else{
                        	parent.$.messager.show({ title : "提示",msg: "此月台口已被使用，无法删除！", position: "bottomRight" });
                        	return;
                        }
                    }
                });
            }
        });
    }

//查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
    }
</script>
</body>
</html>