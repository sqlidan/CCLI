<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
<div data-options="region:'center'" title="预约出库拼箱" style="overflow-y:auto">
    <div style="padding:5px;height:auto" class="datagrid-toolbar">
        <form id="searchFrom" action="">
            <input type="text" name="filter_LIKES_carNumber" class="easyui-validatebox"
                   data-options="width:150,prompt: '车号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text" name="filter_LIKES_driverName" class="easyui-validatebox"
                   data-options="width:150,prompt: '司机姓名'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text" name="filter_LIKES_roomNum" class="easyui-validatebox"
                   data-options="width:150,prompt: '库号'"/>
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
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true"
               onclick="cx()">查询</a>
            <span class="toolbar-item dialog-tool-separator"></span>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
               data-options="disabled:false" onclick="chaifen()">拆分</a>
            <span class="toolbar-item dialog-tool-separator"></span>

            <a id="submit" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add"
               plain="true" onclick="declare()">申报</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </form>
        <form id="searchFrom3" action="">
        </form>

    </div>
    <table id="dg"></table>
</div>
<div id="dlg"></div>
<div id="splitThePopUpBox" class="easyui-window" title="拆分" style="width:1000px;height:400px"
     data-options="iconCls:'icon-remove',modal:true" closed="true" collapsible="false" minimizable="false"
     maximizable="false" closable="true">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
               onclick="splitPopUpBoxAdded()">新增</a>
            <table id="splitThePopUpTable"></table>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)"
               onclick="splitPopUpTableSave()">保存</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)"
               onclick="splitPopUpTableCanceled()">取消</a>
        </div>
    </div>
</div>
<div id="splitPopUpBoxAddsPopUpBox" class="easyui-window" title="新增拆分列表" style="width:1000px;height:500px"
     data-options="iconCls:'icon-add',modal:true" closed="true" collapsible="false" minimizable="false"
     maximizable="false" closable="true">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'" style="padding:5px;height:auto">
            <form id="splitPopUpBoxAddsPopUpForm" action="">
                <input type="text" name="filter_billNo" class="easyui-validatebox"
                       data-options="width:150,prompt: '提单号'"/>
                <span class="toolbar-item dialog-tool-separator"></span>
                <input type="text" name="filter_containerNo" class="easyui-validatebox"
                       data-options="width:150,prompt: '箱号'"/>
                <span class="toolbar-item dialog-tool-separator"></span>
                <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true"
                   onclick="splitPopUpBoxAddsPopUpQuery()">查询</a>
                <span class="toolbar-item dialog-tool-separator"></span>
            </form>
            <table id="splitPopUpBoxAddsAPopUpTable"></table>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)"
               onclick="splitPopUpBoxAddedPopUpBoxSave()">保存</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)"
               onclick="splitPopUpBoxAddedPopUpBoxCancellation()">取消</a>
        </div>
    </div>
</div>

<script type="text/javascript">
    var dg;
    var d;
    var splitThePopUpTable;
    var splitPopUpBoxAddsAPopUpTable;
    var row = {}
    var rows = []
    document.onkeydown = function () {
        if (event.keyCode == 13) {
            cx();
            $('#splitThePopUpBox').window('close');

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
            url: '${ctx}/supervision/Lcl/selectList',
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
                {field: 'customerService', title: '客服', width: 50},
                {field: 'appointDate', title: '预约出库日期', width: 80},
                {field: 'consumeCompany', title: '客户名称', width: 100},
                {field: 'billNo', title: '提单号', width: 60},
                {
                    field: 'status', title: '状态', width: 100,
                    formatter: function (val, row, index) {
                        if (val == "0") {
                            return "已保存";
                        } else if (val == "1") {
                            return "已入闸";
                        } else if (val == "2") {
                            return "已出闸";
                        } else if (val == "3") {
                            return "已取消";
                        }
                    }
                },
                {
                    field: 'containerNo', title: '箱号', width: 50

                },

                {field: 'originCountry', title: '原产国', width: 100},
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
                {
                    field: 'productName', title: '货物名称(品名)', width: 100
                },
                {
                    field: 'num', title: '件数', width: 50,
                },
                {field: 'weight', title: '重量', width: 60},
                {field: 'roomNum', title: '库号', width: 70},
                {field: 'locationNo', title: '房间号', width: 70},
                {field: 'carNumber', title: '车牌号', width: 70},
                {field: 'isSplit', title: '是否已拆分', width: 70,
                    formatter: function (val, row, index) {
                        if (val == "1") {
                            return "已拆分";
                        } else
                            return "未拆分";

                    }},
                {field: 'queuingTime', title: '排队时间', width: 120},
                {field: 'driverName', title: '司机姓名', width: 100},
                {field: 'driverMobile', title: '电话', width: 100},
                {
                    field: 'dirverIdNumber', title: '身份证号', width: 100,

                },
                {
                    field: 'desCompany', title: '货物流向目的地公司名称', width: 100
                },
                {
                    field: 'province', title: '省', width: 70
                },
                {field: 'city', title: '市', width: 70},
                {
                    field: 'area', title: '区', width: 60,
                },
                {field: 'address', title: '详细地址', width: 200},
                {field: 'desContactName', title: '目的地联系人', width: 80},
                {field: 'desContactPhone', title: '目的地联系电话', width: 100}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });

    // 预约出库拼箱-查询
    function cx() {
        dg.datagrid('clearSelections');
        var obj = $("#searchFrom").serializeObject();

        dg.datagrid('load', obj);
    }

    // 预约出库拼箱-拆分
    function chaifen() {
        if (dg.datagrid('getSelected') == null) {
            $.messager.alert('提示', '请选择拆分数据');
        } else {
            var rowDate = dg.datagrid('getSelected');
            console.log("rowDate",rowDate)
            if (rowDate.isSplit == '1') {
               return  $.messager.alert('提示', '该预约信息已拆分');
            }
            rows = []
            // dg.datagrid('getSelected').id += (rows.length + 1).toString()
            rows.push({...dg.datagrid('getSelected')})
            $('#splitThePopUpBox').window('open');
            splitThePopUpTable = $('#splitThePopUpTable').datagrid({
                data: rows,
                fit: true,
                fitColumns: false,
                border: false,
                idField: 'linkId',
                sortOrder: 'desc',
                striped: true,
                pagination: true,
                rownumbers: true,
                singleSelect: true,
                autoEditing: true,          //该属性启用双击行时自定开启该行的编辑状态
                extEditing: true,           //该属性启用行编辑状态的 ExtEditing 风格效果，该属性默认为 true。
                singleEditing: true,        //该属性启用datagrid的只允许单行编辑效果，该属性默认为 true。
                columns: [[
                    {
                        field: 'action', title: '操作栏', width: 80, align: 'center',
                        formatter: function (value, row, index) {
                            if (row.editing) {
                                var s = '<a href="javascript:void(0)" onclick="saverow(this)">保存</a> ';
                                var c = '<a href="javascript:void(0)" onclick="cancelrow(this)">取消</a>';
                                return s + c;
                            } else {
                                var e = '<a href="javascript:void(0)" onclick="editrow(this)">编辑</a> ';
                                var d = '<a href="javascript:void(0)" onclick="deleterow(this)">删除</a>';
                                return e + d;
                            }
                        }
                    },
                    {field: 'yyid', title: '预约id', width: 100},
                    {field: 'customerService', title: '客服', width: 50},
                    {field: 'appointDate', title: '预约出库日期', width: 80},
                    {field: 'consumeCompany', title: '客户名称', width: 100},
                    {field: 'billNo', title: '提单号', width: 60},
                    {
                        field: 'status', title: '状态', width: 100,
                        formatter: function (val, row, index) {
                            if (val == "0") {
                                return "已保存";
                            } else if (val == "1") {
                                return "已入闸";
                            } else if (val == "2") {
                                return "已出闸";
                            } else if (val == "3") {
                                return "已取消";
                            }
                        }
                    },
                    {field: 'containerNo', title: '箱号', width: 50},
                    {field: 'originCountry', title: '原产国', width: 100},
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
                    {field: 'productName', title: '货物名称(品名)', width: 100},
                    {
                        field: 'num', title: '件数', width: 50,
                        editor:{
                            type:'numberbox',
                            options:{
                                min:0,
                                required:true
                            }
                        }
                    },
                    {
                        field: 'weight', title: '重量', width: 60,
                        editor:{
                            type:'numberbox',
                            options:{
                                min:0,
                                required:true
                            }
                        }
                    },
                    {field: 'roomNum', title: '库号', width: 70},
                    {field: 'locationNo', title: '房间号', width: 70},
                    {field: 'carNumber', title: '车牌号', width: 70},
                    {field: 'queuingTime', title: '排队时间', width: 120},
                    {field: 'driverName', title: '司机姓名', width: 100},
                    {field: 'driverMobile', title: '电话', width: 100},
                    {field: 'dirverIdNumber', title: '身份证号', width: 100},
                    {field: 'desCompany', title: '货物流向目的地公司名称', width: 100},
                    {field: 'province', title: '省', width: 70},
                    {field: 'city', title: '市', width: 70},
                    {field: 'area', title: '区', width: 60,},
                    {field: 'address', title: '详细地址', width: 200},
                    {field: 'desContactName', title: '目的地联系人', width: 80},
                    {field: 'desContactPhone', title: '目的地联系电话', width: 100}
                ]],
                onBeforeEdit: function (index, row) {
                    row.editing = true;
                    $(this).datagrid('refreshRow', index);
                },
                onAfterEdit: function (index, row) {
                    row.editing = false;
                    $(this).datagrid('refreshRow', index);
                },
                onCancelEdit: function (index, row) {
                    row.editing = false;
                    $(this).datagrid('refreshRow', index);
                },
                enableHeaderClickMenu: true,
                enableHeaderContextMenu: true,
                enableRowContextMenu: false,
                toolbar: '#tb'
            });
        }
    }

    function getRowIndex(target) {
        var tr = $(target).closest('tr.datagrid-row');
        return parseInt(tr.attr('datagrid-row-index'));
    }

    //拆分-编辑
    function editrow(target) {
        splitThePopUpTable.datagrid('beginEdit', getRowIndex(target));
    }

    //拆分-编辑-删除
    function deleterow(target) {
        $.messager.confirm('Confirm', '你确定删除该数据?', function (r) {
            if (r) {
                splitThePopUpTable.datagrid('deleteRow', getRowIndex(target));
            }
        });
    }

    //拆分-编辑-保存
    function saverow(target) {
        splitThePopUpTable.datagrid('endEdit', getRowIndex(target));
    }

    //拆分-编辑-取消
    function cancelrow(target) {
        splitThePopUpTable.datagrid('cancelEdit', getRowIndex(target));
    }

    //拆分-新增
    function splitPopUpBoxAdded() {
        // dg.datagrid('getSelected').id += (rows.length + 1).toString()
        // rows.push({...dg.datagrid('getSelected')})
        // $('#splitThePopUpTable').datagrid({
        //     data: rows
        // })
        $('#splitPopUpBoxAddsPopUpBox').window('open');
        // $("#splitPopUpBoxAddsPopUpForm").serializeObject().billNo = dg.datagrid('getSelected').billNo
        // $("#splitPopUpBoxAddsPopUpForm").serializeObject().containerNo = dg.datagrid('getSelected').containerNo
        // splitPopUpBoxAddsPopUpQuery()
        splitPopUpBoxAddsAPopUpTable = $('#splitPopUpBoxAddsAPopUpTable').datagrid({
            method: "get",
            url: '${ctx}/supervision/baseAccountInfo/getAccountDataByNo',
            fit: true,
            fitColumns: false,
            border: false,
            // idField: 'linkId',
            sortOrder: 'desc',
            striped: true,
            pagination: true,
            rownumbers: true,
            singleSelect: false,
            columns: [[
                {checkbox: true},
                {field: 'yyid', title: '预约id', width: 100},
                {field: 'customerService', title: '客服', width: 50},
                {field: 'appointDate', title: '预约出库日期', width: 80},
                {field: 'consumeCompany', title: '客户名称', width: 100},
                {field: 'billNo', title: '提单号', width: 60},
                {
                    field: 'status', title: '状态', width: 100,
                    formatter: function (val, row, index) {
                        if (val == "0") {
                            return "已保存";
                        } else if (val == "1") {
                            return "已入闸";
                        } else if (val == "2") {
                            return "已出闸";
                        } else if (val == "3") {
                            return "已取消";
                        }
                    }
                },
                {
                    field: 'containerNo', title: '箱号', width: 50

                },

                {field: 'originCountry', title: '原产国', width: 100},
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
                {
                    field: 'productName', title: '货物名称(品名)', width: 100
                },
                {
                    field: 'num', title: '件数', width: 50,
                },
                {field: 'weight', title: '重量', width: 60},
                {field: 'roomNum', title: '库号', width: 70},
                {field: 'locationNo', title: '房间号', width: 70},
                {field: 'carNumber', title: '车牌号', width: 70},
                {field: 'queuingTime', title: '排队时间', width: 120},
                {field: 'driverName', title: '司机姓名', width: 100},
                {field: 'driverMobile', title: '电话', width: 100},
                {
                    field: 'dirverIdNumber', title: '身份证号', width: 100,

                },
                {
                    field: 'desCompany', title: '货物流向目的地公司名称', width: 100
                },
                {
                    field: 'province', title: '省', width: 70
                },
                {field: 'city', title: '市', width: 70},
                {
                    field: 'area', title: '区', width: 60,
                },
                {field: 'address', title: '详细地址', width: 200},
                {field: 'desContactName', title: '目的地联系人', width: 80},
                {field: 'desContactPhone', title: '目的地联系电话', width: 100}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    }

    //拆分-新增-查询
    function splitPopUpBoxAddsPopUpQuery() {
        splitPopUpBoxAddsAPopUpTable.datagrid('clearSelections');
        var obj = $("#splitPopUpBoxAddsPopUpForm").serializeObject();
        console.log($("#splitPopUpBoxAddsPopUpForm").serializeObject(), $("#splitPopUpBoxAddsPopUpForm"));
        splitPopUpBoxAddsAPopUpTable.datagrid('load', obj);
    }

    //拆分-保存
    function splitPopUpTableSave() {
        console.log("拆分信息rows=====xhehe=====" ,rows)
        $.ajax({
            async: true,
            type: "post",
            url: "${ctx}/supervision/baseAccountInfo/saveInfo",
            data: JSON.stringify(rows),
            contentType: 'application/json;charset=utf-8',
            dataType: "Text",
            success: function(data) {
               console.log("==data======",data)
                if (data == "success") {
                    parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight"});
                    $('#splitThePopUpBox').window('close');
                } else {
                    parent.$.messager.show({title: "提示", msg: "保存失败，请重新填写数据！", position: "bottomRight"});
                }

            }
        });
    }

    //拆分-新增-保存
    function splitPopUpBoxAddedPopUpBoxSave() {
        rows= [...rows,...$('#splitPopUpBoxAddsAPopUpTable').datagrid('getSelections')]
        $('#splitThePopUpTable').datagrid({
            data: rows
        })
        $('#splitPopUpBoxAddsPopUpBox').window('close');
    }

    //拆分-取消
    function splitPopUpTableCanceled() {
        $('#splitThePopUpBox').window('close');
    }

    //拆分-新增-取消
    function splitPopUpBoxAddedPopUpBoxCancellation() {
        console.log(rows)
        $('#splitPopUpBoxAddsPopUpBox').window('close');
    }

    //申报
    function declare() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        $('#submit').linkbutton({disabled: true});
        $.get('${ctx}/supervision/Lcl/apply/' + row.id,
            function (data) {
                if (data == "success") {
                    successTip("核放成功");
                    dg.datagrid('load');
                    $('#submit').linkbutton({disabled: false});
                } else {
                    parent.$.messager.alert(data);
                    $('#submit').linkbutton({disabled: false});
                }
            });
    }
</script>
</body>

</html>