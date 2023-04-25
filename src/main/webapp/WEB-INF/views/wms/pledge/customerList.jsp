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
        <form id="urSearchFrom" action="">
            <input type="text" id="clientName" name="filter_LIKES_clientName" class="easyui-validatebox"
                   data-options="width:150,prompt: '客户名称'"/>
            <input type="text" id="taxAccount" name="filter_LIKES_taxAccount" class="easyui-validatebox"
                   data-options="width:150,prompt: '税号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        </form>
    </div>
</div>
<table id="ur_dg"></table>
<script type="text/javascript">
    var ur_dg;
    ur_dg = $('#ur_dg').datagrid({
        method: "get",
        url: '${ctx}/base/client/listjson',
        fit: true,
        fitColumns: true,
        border: false,
        idField: 'ids',
        pagination: true,
        rownumbers: true,
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 40, 50],
        striped: true,
        columns: [[
            {field: 'ck', checkbox: true},
            {field: 'ids', title: 'ids', hidden: true, sortable: true, width: 100},
            {field: 'clientName', title: '客户名称', sortable: true, width: 100},
            {field: 'clientCode', title: '客户编码', sortable: true, width: 100},
            {field: 'taxAccount', title: '税号', sortable: true, width: 100, tooltip: true}
        ]],
        onLoadSuccess: function () {
            //获取用户拥有角色,选中
            $.ajax({
                async: false,
                type: 'get',
                url: "${ctx}/pledge/customerApply/${recordId}/customerInfo",
                success: function (data) {
                    if (data) {
                        for (var i = 0, j = data.length; i < j; i++) {
                            ur_dg.datagrid('selectRecord', data[i]);
                        }
                    }
                }
            });

        }
    });

    //保存 客户
    function saveCustomerInfo() {
        var newCustomerList = [];
        var data = ur_dg.datagrid('getSelections');
        console.log(data);
        console.log('----------------');
        //所选的角色列表
        for (var i = 0, j = data.length; i < j; i++) {
            newCustomerList.push(data[i].ids);
        }
        console.log(newCustomerList);
        $.ajax({
            async: false,
            type: 'POST',
            data: JSON.stringify(newCustomerList),
            contentType: 'application/json;charset=utf-8',	//必须
            url: "${ctx}/pledge/customerApply/${recordId}/updateCustomerInfo",
            success: function (data) {
                if (data == 'success') {
                    parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
                    dg.datagrid('load');
                } else {
                    $.easyui.messager.alert(data);
                }
            }
        });
    }

    function cx() {
        var obj = $("#urSearchFrom").serializeObject();
         console.log(obj);
        ur_dg.datagrid('load', obj);
    }
</script>
</body>
</html>