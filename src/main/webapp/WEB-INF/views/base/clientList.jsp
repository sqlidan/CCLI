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
            <input type="text" id="clientName" name="filter_LIKES_clientName" class="easyui-validatebox"
                   data-options="width:150,prompt: '客户名称'"/>
            <input type="text" id="syncClientName" name="filter_LIKES_syncClientName" class="easyui-validatebox"
                   data-options="width:150,prompt: '陆海通平台客户名称'"/>
            <input type="text" id="clientCode" name="filter_LIKES_clientCode" class="easyui-validatebox"
                   data-options="width:150,prompt: '客户编号'"/>
            <select class="easyui-combobox" id="cxClientSort" name="filter_EQS_clientSort"
                    data-options="width:150,prompt:'客户类型'">
                <option value=""></option>
            </select>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
            <input type="text"  name="syncClientName" class="easyui-validatebox"
                   data-options="width:200,prompt: '接口同步:陆海通平台客户名称'"/>
            <input type="text" name="syncTaxAccount" class="easyui-validatebox"
                   data-options="width:200,prompt: '接口同步:税号'"/>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-help" plain="true" onclick="tb()">陆海通平台客户同步</a>
        </form>

        <shiro:hasPermission name="base:client:add">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
               onclick="add();">添加</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>

        <shiro:hasPermission name="base:client:update">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
               onclick="upd()">修改</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="base:client:delete">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
               data-options="disabled:false" onclick="del()">删除</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="base:client:export">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"
               onclick="exportExcel()">导出EXCEL</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
    </div>

</div>
<table id="dg"></table>
<div id="dlg"></div>
<script type="text/javascript">
    var dg;
    var d;
    $(document).keypress(function (e) {
        // 回车键事件
        if (e.which == 13) {
            cx();
        }
    });
    $(document).ready(function () {
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/base/client/listjson',
            fit: true,
            fitColumns: true,
            border: false,
            idField: 'ids',
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'ids', title: 'ids', hidden: true},
                {field: 'clientName', title: '客户名称', sortable: true, width: 100},
                {field: 'clientCode', title: '客户编码', sortable: true, width: 40},
                {field: 'syncClientName', title: '陆海通平台客户名称', sortable: true, width: 100},
                {field: 'realClientName', title: '陆海通客户名称', sortable: true, width: 100},
                {field: 'syncComcode', title: '陆海通平台客户code', sortable: true, width: 100},
                {
                    field: 'clientSort', title: '客户类型', sortable: true,
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return "客户";
                        } else if (value == 1) {
                            return "供应商";
                        } else if (value == 2) {
                            return "装卸队";
                        }
                    }
                },
                {field: 'contactMan', title: '联系人', sortable: true, width: 50},
                {field: 'saler', title: '揽货人', sortable: true, width: 50},
                {field: 'telNum', title: '联系电话', sortable: true, width: 100},
                {field: 'userEMail', title: 'EMail', sortable: true, width: 100},
                {field: 'taxAccount', title: '税号', sortable: true, width: 100},
                {field: 'checkDay', title: '结算日', sortable: true},
                {
                    field: 'pledgeType', title: '质押状态', sortable: true,
                    formatter: function (value, row, index) {
                        return value == 1 ? '质押' : '正常';
                    }
                },
                {field: 'serviceCode', title: '客服人员编码', sortable: true},
                {
                    field: 'limit', title: '网上查询权限', sortable: true,
                    formatter: function (value, row, index) {
                        return value == 1 ? '有' : '无';
                    }
                },
                {field: 'customerLevel', title: '客户级别', sortable: true},
                {field: 'note', title: '备注', sortable: true}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
        // ---

        $.ajax({
            type: "GET",
            url: "${ctx}/system/dict/json",
            data: "filter_LIKES_type=client",
            dataType: "json",
            success: function (date) {
                if (date != null && date.rows != null && date.rows.length > 0) {
                    $('#cxClientSort').combobox({
                        data: date.rows,
                        valueField: 'value',
                        textField: 'label'
                    });
                }
            }
        })
    });

    //添加
    function add(title, href) {
        if (typeof(href) == 'undefined') {
            title = "客户添加";
            href = '${ctx}/base/client/create';
        }
        d = $("#dlg").dialog({
            title: title,
            width: 450,
            height: 450,
            href: href,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    setTimeout(function(){
                        debugger
                    // .form('validate')
                        if($("#mainform").form('validate')) {
                            debugger
                            $("#mainform").submit();
                            d.panel('close');
                          /*  successTip(data, dg);*/
                        }
                    }, 500)


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
        var row = dg.datagrid('getSelected');
        console.log(row);
        if (rowIsNull(row)) return;
        var href = '${ctx}/base/client/update/' + row.ids;
        add("客户修改", href);
    }
    //查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        // console.log(obj);
        dg.datagrid('load', obj);
    }
    // 同步
    function tb() {
        var params=$('#searchFrom').serialize();
        $.ajax({
            type: "GET",
            url: "${ctx}/base/client/listTb",
            data: params,
            dataType: "json",
            success: function (result) {
                if(result.syn){
                    // 同步数据
                    $('#syncClientName').val(result.syn)

                    // 清空其他数据
                    $('#clientName').val('')
                    $('#clientCode').val('')
                    $('#cxClientSort').combobox('clear');
                    cx()
                }else{
                    $.messager.show({
                        title:'',
                        msg:'陆海通平台查询不到该用户！',
                        timeout:2000,
                        showType:'slide'
                    });
                }

            }
        })

    }
    //删除
    function del() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
            if (data) {
                $.ajax({
                    type: 'get',
                    url: "${ctx}/base/client/delete/" + row.ids,
                    success: function (data) {
                        successTip(data, dg);
                        cx();
                    }
                });
            }
        });
    }

    //导出
    function exportExcel() {
        var url = "${ctx}/base/client/export";
        $("#searchFrom").attr("action", url).submit();
    }
</script>
</body>
</html>
