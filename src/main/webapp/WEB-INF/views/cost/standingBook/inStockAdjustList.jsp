<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>

<body class="easyui-layout" style="font-family: '微软雅黑'">

<div data-options="region:'north', split:true, border:false" style="height:100px;">
    <div class="datagrid-toolbar" style="height:auto">
        <c:choose>
            <c:when test="${finishFeeState == 0}">
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-sign-up" plain="true"
                   data-options="disabled:false" onclick="add()">引入方案</a>
                <span class="toolbar-item dialog-tool-separator"></span>
            </c:when>
        </c:choose>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
           data-options="disabled:false" onclick="del()">删除</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <c:choose>
            <c:when test="${planFeeState == 0}">
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
                   onclick="planOk()">计划费用完成</a>
                <span class="toolbar-item dialog-tool-separator"></span>
            </c:when>
            <c:otherwise>
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
                   onclick="planNo()">计划费用取消</a>
                <span class="toolbar-item dialog-tool-separator"></span>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${finishFeeState == 0}">
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
                   onclick="finishOk()">费用完成</a>
                <span class="toolbar-item dialog-tool-separator"></span>
            </c:when>
            <c:otherwise>
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
                   onclick="finishNo()">费用取消</a>
                <span class="toolbar-item dialog-tool-separator"></span>
            </c:otherwise>
        </c:choose>
        <shiro:hasPermission name="wms:enterStockmaFee:okPass">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
	           onclick="isOK()">审核</a>
	        <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:enterStockmaFee:backPass">
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
	           onclick="isNotOK()">取消审核</a>
	        <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        
        <div style="float:right; text-align:right">
<%--            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-finished" plain="true"--%>
<%--               data-options="disabled:false" onclick="showContract()">最新合同</a>--%>
<%--            <span class="toolbar-item dialog-tool-separator"></span>--%>
            <input id="editDate" name="editDate" class="easyui-my97" datefmt="yyyy-MM" data-options="width: 100"
                   readonly/>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-full-time" plain="true"
               onclick="updateDate()">统一账单日期</a>
        </div>
    </div>
    <input type="hidden" id="stockId" name="stockId" value="${stockId }"/>
    <input type="hidden" id="stockOrgId" name="stockOrgId" value="${stockOrgId}"/>
    <input type="hidden" id="userId" name="userId" value="${userId }"/>
    <table class="formTable">
        <tr>
            <td>入库联系单号</td>
            <td>
                <input id="linkId" name="linkId" class="easyui-validatebox" data-options="width: 200" value="${linkId }"
                       style="background-color:#EBEBE4" readonly/>
            </td>
            <td>提单号</td>
            <td>
                <input id="itemNum" name="itemNum" class="easyui-validatebox" data-options="width: 200"
                       value="${itemNum }" style="background-color:#EBEBE4" readonly/>
            </td>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td>应收合计（RMB）</td>
            <td>
                <input id="yingShou" name="yingShou" class="easyui-validatebox" data-options="width: 200" value="0.00"
                       style="background-color:#EBEBE4" readonly/>
            </td>
            <td>应付合计（RMB）</td>
            <td>
                <input id="yingFu" name="yingFu" class="easyui-validatebox" data-options="width: 200" value="0.00"
                       style="background-color:#EBEBE4" readonly/>
            </td>
            <td>利润（RMB）</td>
            <td>
                <input id="lirui" name="lirui" class="easyui-validatebox" data-options="width: 200" value="0.00"
                       style="background-color:#EBEBE4" readonly/>
            </td>
        </tr>
    </table>
</div>

<div data-options="region:'center'">
    <div class="easyui-accordion" data-options="multiple:true">
        <div id="open1" title="应收明细" style="height: 320px;width:auto; " data-options="collapsed:false">
            <div id="tb1">
            </div>
            <table id="dg1"></table>
            <div id="dlg1"></div>
        </div>
        <div id="open2" title="应付明细" style="height: 320px;width: auto; " data-options="collapsed:false">
            <div id="tb2">
            </div>
            <table id="dg2"></table>
            <div id="dlg2"></div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var dg1;
    var d1;
    
    var dg2;
    var d2;

    //当前时间
    var nowDate = new Date();

    //添加行设置
    var editRow1 = undefined;
    var editRow2 = undefined;

    $(function () {
        sunCost();
        var linkId = $('#linkId').val(); 
        var itemNum=$('#itemNum').val(); 
        dg1 = $('#dg1').datagrid({
            method: "get",
            url: '${ctx}/cost/standingBook/getInData?filter_EQS_linkId=' + linkId + "&filter_EQI_ifReceive=1&filter_EQS_billNum="+itemNum,
            fit: true,
            fitColumns: false,
            border: false,
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 30,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: false,
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'payId', title: '业务付款单号', hidden: true},
                {field: 'standingNum', title: 'standingNum', hidden: true},
                {
                    field: 'examineSign', title: '审核', align: 'center', width: 30,
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return '<input type="checkbox" readonly disabled/>';
                        }
                        if (value == 1) {
                            return '<input type="checkbox" checked readonly disabled/>';
                        }
                    }
                },
                {
                    field: 'reconcileSign', title: '对账', align: 'center', width: 50,
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return '<input type="checkbox" readonly disabled/>';
                        }
                        if (value == 1) {
                            return '<input type="checkbox" checked readonly disabled/>';
                        }
                    }
                },
                {
                    field: 'settleSign', title: '结算', align: 'center', width: 50,
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return '<input type="checkbox" readonly disabled/>';
                        }
                        if (value == 1) {
                            return '<input type="checkbox" checked readonly disabled/>';
                        }
                    }
                },
                {
                    field: 'chargeSign', title: '收款', align: 'center', width: 50,
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return '<input type="checkbox" readonly disabled/>';
                        }
                        if (value == 1) {
                            return '<input type="checkbox" checked readonly disabled/>';
                        }
                    }
                },
                {
                    field: 'paySign', title: '垫付', align: 'center', width: 50,
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return '<input type="checkbox" readonly disabled/>';
                        }
                        if (value == 1) {
                            return '<input type="checkbox" checked readonly disabled/>';
                        }
                    }
                },
                {field: 'asn', title: 'ASN', width: 100, editor: "text"},
                {field: 'payNum', title: '业务付款单号',width: 100},
                {field: 'reconcileNum', title: '对账单号',width: 100},
                {field: 'feeName', title: 'feeName', hidden: true},
                {field: 'feeCode', title: '费用名称', width: 80,formatter: function (value, row, index) {
                        return row.feeName;
                    },
                    editor: {
                        type: 'combobox',
                        options: {
                            onBeforeLoad: function (param) {
                                var row = $('#dg1').datagrid('getSelected');
                                param.setid = row.feeCode;
                            },
                            url: "${ctx}/base/feecode/getFeeCodeAll",
                            valueField: 'code',
                            textField: 'nameC',
                            mode: 'remote',
                            method: 'GET'
                        }
                    }
                },
                {field: 'customsName', title: 'customsName', hidden: true},
                {field: 'customsNum', title: '客户', width: 220,
                    formatter: function (value, row, index) {
                        return row.customsName;
                    },
                    editor: {
                        type: 'combobox',
                        options: {
                            onBeforeLoad: function (param) {
                                var row = $('#dg1').datagrid('getSelected');
                                param.setid = row.customsNum;
                            },
                            url: "${ctx}/base/client/getClientAll",
                            valueField: 'ids',
                            textField: 'clientName',
                            mode: 'remote',
                            method: 'GET'
                        }
                    }
                },
                //{field:'sku',title:'SKU',  width:50}, //gzq 20160628 不显示SKU
                {field: 'num', title: '数量', width: 50, editor: {type: 'numberbox', options: {precision: 4}}},
                {field: 'price', title: '单价', width: 50, editor: {type: 'numberbox', options: {precision: 2}}},
                {field: 'receiveAmount', title: '金额', width: 50,
				    formatter:function(val,rowData,rowIndex){
				        if(val!=null){
				            return val.toFixed(2);
				        }
				   }
				},
                {field: 'currency', title: '币种', width: 50,
                    editor: {
                        type: 'combobox',
                        options: {
                            url: "${ctx}/system/dict/searchDict/currencyType",
                            valueField: 'value',
                            textField: 'label',
                            editable: false, //不允许手动输入
                            method: 'GET'
                        }
                    },
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return '人民币';
                        }
                        if (value == 1) {
                            return '美元';
                        }
                        if (value == 2) {
                            return '日元';
                        }
                        if (value == '201') {
                            return "阿尔及利亚第纳尔";
                        }
                    }
                },
                {field: 'exchangeRate', title: '汇率', width: 100},
                {field: 'taxRate', title: '税率', width: 100, editor: "numberbox",
                    formatter: function (value, row, index) {
                        return row.taxRate;
                    }, 
                    editor: {
                        type: 'combobox',
                        options: {
                            data: [
                                {"id": "0", "text": "0"},
                                {"id": "6", "text": "6"},
                                {"id": "11", "text": "11"},
                                {"id": "13", "text": "13"},
                                {"id": "17", "text": "17"}
                            ],
                            valueField: 'id',
                            textField: 'text',
                            editable: false, //不允许手动输入
                            method: 'GET'
                        }
                    }
                },
                {field: 'billDate', title: '账单日期', width: 150, editor: "my97M"},
                {
                    field: 'fillSign', title: '冲补', align: 'center', width: 50,
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return 'N';
                        }
                        if (value == 1) {
                            return 'Y';
                        }
                    }
                },
                {
                    field: 'shareSign', title: '分摊', align: 'center', width: 50,
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return 'N';
                        }
                        if (value == 1) {
                            return 'Y';
                        }
                    }
                },
                {field: 'remark', title: '备注', width: 150, editor: "text"},
                {field: 'inputPersonId', title: 'inputPersonId', hidden: true},
                {field: 'inputPerson', title: '创建人', width: 80},
                {field: 'inputDate', title: '创建时间', width: 80},
                {field: 'updatePerson', title: '修改人', width: 80},
                {field: 'updateDate', title: '修改时间', width: 80}
            ]],
            rowStyler: function (rowIndex, rowData) {
                //已审核的变色
                if (rowData.examineSign == "1") {
                    return 'background-color:#4BB64D';
                }
            },

            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb1',
           //collapsible: true,
            onCancelEdit: function (rowIndex, rowData, changes) {//取消
                editRow1 = undefined;
                dg1.datagrid('reload');
            },
            onAfterEdit: function (rowIndex, rowData, changes) {//确定
                sureUpd1(rowData);//1:应收
            }
        });

        //添加工具栏
        if ("${finishFeeState}" == 0) {
            var pager1 = $('#dg1').datagrid().datagrid('getPager');
            pager1.pagination({
                buttons: [
                    {
                        iconCls: 'icon-add',
                        handler: function () {
                            if (editRow1 == undefined) {
                                $("#dg1").datagrid('insertRow', {
                                    index: 0,
                                    row: {
                                        currency: "0",
                                        num: "1",
                                        price: "1",
                                        taxRate: "0",
                                        customsNum: $("#stockOrgId").val(),  // 客户名称
                                        billDate: new Date().format("yyyy-MM")

                                    }
                                });
                                $('#dg1').datagrid('selectRow', 0);

                                $("#dg1").datagrid('beginEdit', 0);
                                editRow1 = 0;
                            }
                        }
                    },
                    {
                        iconCls: 'icon-edit',
                        handler: function () {
                            var row = dg1.datagrid('getSelected');
                            if (rowIsNull(row)) return;
                            if (row.examineSign == 1) {
                                parent.$.messager.show({title: "提示", msg: "审核后不得修改！", position: "bottomRight"});
                                dg1.datagrid('reload');
//                           } else if ("" != row.payId && null != row.payId) {
//                                alert(row.payId);
//                               parent.$.messager.show({title: "提示", msg: "业务付款单生成的数据不得修改！", position: "bottomRight"});
//                               dg1.datagrid('reload');
                            } else {
                                var rowIndex = dg1.datagrid('getRowIndex', row);
                                dg1.datagrid('beginEdit', rowIndex);
                            }
                        }
                    }
                ]
            });
        }

        //应付费用调整
        dg2 = $('#dg2').datagrid({
            method: "get",
            url: '${ctx}/cost/standingBook/getInData?filter_EQS_linkId=' + linkId + "&filter_EQI_ifReceive=2",
            fit: true,
            fitColumns: false,
            border: false,
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 30,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: false,
            columns: [[
                {field: 'ck', checkbox: true},
                {field: 'payId', title: 'payId', hidden: true},
                {field: 'standingNum', title: 'standingNum', hidden: true},
                {
                    field: 'examineSign', title: '审核', align: 'center', width: 30,
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return '<input type="checkbox" readonly disabled/>';
                        }
                        if (value == 1) {
                            return '<input type="checkbox" checked readonly disabled/>';
                        }
                    }
                },
                {
                    field: 'reconcileSign', title: '对账', align: 'center', width: 50,
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return '<input type="checkbox" readonly disabled/>';
                        }
                        if (value == 1) {
                            return '<input type="checkbox" checked readonly disabled/>';
                        }
                    }
                },
                {
                    field: 'settleSign', title: '结算', align: 'center', width: 50,
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return '<input type="checkbox" readonly disabled/>';
                        }
                        if (value == 1) {
                            return '<input type="checkbox" checked readonly disabled/>';
                        }
                    }
                },
                {
                    field: 'chargeSign', title: '收款', align: 'center', width: 50,
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return '<input type="checkbox" readonly disabled/>';
                        }
                        if (value == 1) {
                            return '<input type="checkbox" checked readonly disabled/>';
                        }
                    }
                },
                {
                    field: 'paySign', title: '垫付', align: 'center', width: 50,
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return "<input type='checkbox' onclick='toPaySign(" + row.standingNum + "," + row.examineSign + ")' />";
                        }
                        if (value == 1) {
                            return "<input type='checkbox' onclick='deletePaySign(" + row.standingNum + "," + row.examineSign + ")' checked />";
                        }
                    }
                },
                {field: 'asn', title: 'ASN', width: 100, editor: "text"},
                {field: 'payNum', title: '业务付款单号',width: 100},
                {field: 'reconcileNum', title: '对账单号',width: 100},
                {field: 'feeName', title: 'feeName', hidden: true},
                {
                    field: 'feeCode', title: '费用名称', width: 80,
                    formatter: function (value, row, index) {
                        return row.feeName;
                    },
                    editor: {
                        type: 'combobox',
                        options: {
                            onBeforeLoad: function (param) {
                                var row = $('#dg2').datagrid('getSelected');
                                param.setid = row.feeCode;
                            },
                            url: "${ctx}/base/feecode/getFeeCodeAll",
                            valueField: 'code',
                            textField: 'nameC',
                            mode: 'remote',
                            method: 'GET'
                        }
                    }
                },
                {field: 'customsName', title: 'customsName', hidden: true},
                {
                    field: 'customsNum', title: '客户', width: 220,
                    formatter: function (value, row, index) {
                        return row.customsName;
                    },
                    editor: {
                        type: 'combobox',
                        options: {
                            onBeforeLoad: function (param) {
                                var row = $('#dg2').datagrid('getSelected');
                                param.setid = row.customsNum;
                            },
                            url: "${ctx}/base/client/getClientAll",
                            valueField: 'ids',
                            textField: 'clientName',
                            mode: 'remote',
                            method: 'GET'
                        }
                    }
                },
                //{field:'sku',title:'SKU',  width:50},//gzq 20160628 不显示SKU
                {field: 'num', title: '数量', width: 50, editor: {type: 'numberbox', options: {precision: 4}}},
                {field: 'price', title: '单价', width: 50, editor: {type: 'numberbox', options: {precision: 2}}},
                {field: 'receiveAmount', title: '金额', width: 50,
				    formatter:function(val,rowData,rowIndex){
				        if(val!=null){
				            return val.toFixed(2);
				        }
				   }
				},
                {
                    field: 'currency', title: '币种', width: 50,
                    editor: {
                        type: 'combobox',
                        options: {
                            url: "${ctx}/system/dict/searchDict/currencyType",
                            valueField: 'value',
                            textField: 'label',
                            editable: false, //不允许手动输入
                            method: 'GET'
                        }
                    },
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return '人民币';
                        }
                        if (value == 1) {
                            return '美元';
                        }
                        if (value == 2) {
                            return '日元';
                        }
                        if (value == '201') {
                            return "阿尔及利亚第纳尔";
                        }
                    }
                },
                {field: 'exchangeRate', title: '汇率', width: 100},
                {
                    field: 'taxRate', title: '税率', width: 100, editor: "numberbox",
                    formatter: function (value, row, index) {
                        return row.taxRate;
                    },
                    editor: {
                        type: 'combobox',
                        options: {
                            data: [
                                {"id": "0", "text": "0"},
                                {"id": "6", "text": "6"},
                                {"id": "11", "text": "11"},
                                {"id": "13", "text": "13"},
                                {"id": "17", "text": "17"}
                            ],
                            valueField: 'id',
                            textField: 'text',
                            editable: false, //不允许手动输入
                            method: 'GET'
                        }
                    }
                },
                {field: 'billDate', title: '账单日期', width: 150, editor: "my97M"},
                {
                    field: 'fillSign', title: '冲补', align: 'center', width: 50,
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return 'N';
                        }
                        if (value == 1) {
                            return 'Y';
                        }
                    }
                },
                {
                    field: 'shareSign', title: '分摊', align: 'center', width: 50,
                    formatter: function (value, row, index) {
                        if (value == 0) {
                            return 'N';
                        }
                        if (value == 1) {
                            return 'Y';
                        }
                    }
                },
                {field: 'remark', title: '备注', width: 150, editor: "text"},
                {field: 'inputPersonId', title: 'inputPersonId', hidden: true},
                {field: 'inputPerson', title: '创建人', width: 80},
                {field: 'inputDate', title: '创建时间', width: 80},
                {field: 'updatePerson', title: '修改人', width: 80},
                {field: 'updateDate', title: '修改时间', width: 80}
            ]],
            rowStyler: function (rowIndex, rowData) {
                //已审核的变色
                if (rowData.examineSign == "1") {
                    return 'background-color:#4BB64D';
                }
            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb2',
// 	    autoEditing: true,    //该属性启用双击行时自定开启该行的编辑状态
            onCancelEdit: function (rowIndex, rowData, changes) {//取消
                editRow2 = undefined;
                dg2.datagrid('reload');
            },
            onAfterEdit: function (rowIndex, rowData, changes) {//确认
                sureUpd2(rowData);//2:应付
            }
        });

        //添加工具栏
        if ("${finishFeeState}" == 0 && "${planFeeState}" == 0) {
            var pager2 = $('#dg2').datagrid().datagrid('getPager');
            pager2.pagination({
                buttons: [{
                    iconCls: 'icon-add',
                    handler: function (row) {
                        if (editRow2 == undefined) {
                            $("#dg2").datagrid('insertRow', {
                                index: 0,
                                row: {
                                    currency: "0",
                                    num: "1",
                                    price: "1",
                                    taxRate: "0",
                                    customsNum: $("#stockOrgId").val(),
                                    billDate: new Date().format("yyyy-MM")
                                }
                            });
                            $('#dg2').datagrid('selectRow', 0);

                            $("#dg2").datagrid('beginEdit', 0);
                            editRow2 = 0;
                        }
                    }
                }, {
                    iconCls: 'icon-edit',
                    handler: function () {
                        var row = dg2.datagrid('getSelected');
                        if (rowIsNull(row)) return;
                        if (row.examineSign == 1) {
                            parent.$.messager.show({title: "提示", msg: "审核后不得修改！", position: "bottomRight"});
                            dg2.datagrid('reload');
                        } else if ("" != row.payId && null != row.payId) {
                            parent.$.messager.show({title: "提示", msg: "业务付款单生成的数据不得修改！", position: "bottomRight"});
                            dg2.datagrid('reload');
                        } else {
                            var rowIndex = dg2.datagrid('getRowIndex', row);
                            dg2.datagrid('beginEdit', rowIndex);
                        }
                    }
                }]
            });
        }
    });
    //编辑  应收
    function sureUpd1(rowData) {
        editRow1 = undefined;
        //判断是否审核
        if (rowData.examineSign == 1) {
            parent.$.messager.show({title: "提示", msg: "审核后不得修改！", position: "bottomRight"});
            dg1.datagrid('reload');
        } else if (rowData.feeCode == "") {
            parent.$.messager.show({title: "提示", msg: "费用不能为空，请选择费用！", position: "bottomRight"});
            dg1.datagrid('reload');
        } 
        /* 20180713 去掉验证账单日期是否为空
        else if (rowData.billDate == "") {
            parent.$.messager.show({title: "提示", msg: "账单日期不能为空，请选择日期！", position: "bottomRight"});
            dg1.datagrid('reload');
        } */ 
        else {
            parent.$.messager.confirm('提示', '确定要修改费目信息？', function (data) {
                if (data) {
                    var stockId = rowData.customsNum;
                    var linkId = $("#linkId").val();
                    var itemNum = $("#itemNum").val();
                    $.ajax({
                        type: 'GET',
                        async: false,
                        url: "${ctx}/cost/standingBook/" + rowData.standingNum + "/" + linkId + "/editStandingBookByFee",
                        data: "crkSign=1&type=1&stockId=" + stockId + "&billNum=" + itemNum + "&asn=" + rowData.asn + "&num=" + rowData.num + "&price=" + rowData.price + "&taxRate=" + rowData.taxRate
                        + "&feeCode=" + rowData.feeCode + "&currency=" + rowData.currency + "&billDate=" + rowData.billDate + "&remark=" + rowData.remark,
                        success: function (data) {
                            if (data == 'success') {
                                parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
                            }
                            dg1.datagrid('reload');
                            sunCost();
                        }
                    });
                } else {
                    dg1.datagrid('reload');
                }
            });
        }
    }

    //编辑  应付
    function sureUpd2(rowData, type) {
        editRow2 = undefined;
        //判断是否审核
        if (rowData.examineSign == 1) {
            parent.$.messager.show({title: "提示", msg: "审核后不得修改！", position: "bottomRight"});
            dg2.datagrid('reload');
        } else if (rowData.feeCode == "") {
            parent.$.messager.show({title: "提示", msg: "费用不能为空，请选择费用！", position: "bottomRight"});
            dg2.datagrid('reload');
        } 
        /*	20180713 去掉验证账单日期是否为空
        else if (rowData.billDate == "") {
            parent.$.messager.show({title: "提示", msg: "账单日期不能为空，请选择日期！", position: "bottomRight"});
            dg2.datagrid('reload');
        } */ 
        else if (rowData.customsNum == "") {
            parent.$.messager.show({title: "提示", msg: "客户不能为空，请选择客户！", position: "bottomRight"});
            dg2.datagrid('reload');
        } else {
            parent.$.messager.confirm('提示', '确定要修改费目信息？', function (data) {
                if (data) {
                    var linkId = $("#linkId").val();
                    var itemNum = $("#itemNum").val();
                    $.ajax({
                        type: 'GET',
                        async: false,
                        url: "${ctx}/cost/standingBook/" + rowData.standingNum + "/" + linkId + "/editStandingBookByFee",
                        data: "crkSign=1&type=2&stockId=" + rowData.customsNum + "&billNum=" + itemNum + "&asn=" + rowData.asn + "&num=" + rowData.num + "&price=" + rowData.price + "&taxRate=" + rowData.taxRate
                        + "&feeCode=" + rowData.feeCode + "&currency=" + rowData.currency + "&billDate=" + rowData.billDate + "&remark=" + rowData.remark,
                        success: function (data) {
                            if (data == 'success') {
                                parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
                            }
                            dg2.datagrid('reload');
                            sunCost();
                        }
                    });
                } else {
                    dg2.datagrid('reload');
                }
            });
        }
    }

    // 垫付 操作
    function toPaySign(standingNum, examineSign) {
        if (examineSign == 1) {
            parent.$.messager.show({title: "提示", msg: "审核后不得垫付！", position: "bottomRight"});
            dg2.datagrid('reload');
        } else {
            parent.$.messager.confirm('提示', '确定要垫付此费目？', function (data) {
                if (data) {
                    var stockId = $("#stockId").val();
                    $.ajax({
                        type: 'GET',
                        async: false,
                        url: "${ctx}/cost/standingBook/" + standingNum + "/updatePaySignByStandingNum",
                        data: "stockId=" + stockId,
                        success: function (data) {
                            if (data == 'success') {
                                parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
                            }
                            dg1.datagrid('reload');
                            dg2.datagrid('reload');
                            sunCost();
                        }
                    });
                }
            });
        }
    }

    //取消垫付
    function deletePaySign(standingNum, examineSign) {
        if (examineSign == 1) {
            parent.$.messager.show({title: "提示", msg: "审核后不得垫付！", position: "bottomRight"});
            dg2.datagrid('reload');
        } else {
            parent.$.messager.confirm('提示', '确定要取消垫付此费目？', function (data) {
                if (data) {
                    var stockId = $("#stockId").val();
                    $.ajax({
                        type: 'GET',
                        async: false,
                        url: "${ctx}/cost/standingBook/" + standingNum + "/deletePaySignByStandingNum",
                        data: "stockId=" + stockId,
                        success: function (data) {
                            if (data == 'success') {
                                parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
                            } else {
                                parent.$.messager.show({title: "提示", msg: "应收应付审核后不得垫付！", position: "bottomRight"});
                            }
                            dg1.datagrid('reload');
                            dg2.datagrid('reload');
                            sunCost();
                        }
                    });
                }
            });
        }
    }

    //统一时间
    function updateDate() {
        var linkId = $("#linkId").val();
        var editDate = $("input[name='editDate']").val();

        if (editDate == "") {
            parent.$.messager.show({title: "提示", msg: "请选择日期！", position: "bottomRight"});
        } else {
            $.ajax({
                type: 'GET',
                async: false,
                url: "${ctx}/cost/standingBook/" + linkId + "/updateDateByLinkId",
                data: "editDate=" + editDate,
                success: function (data) {
                    if (data == 'success') {
                        parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
                    }
                    dg1.datagrid('reload');
                    dg2.datagrid('reload');
                }
            });
        }
    }

    //应收  应付计算
    function sunCost() {
        var linkId = $("#linkId").val();
        $.ajax({
            type: 'GET',
            url: "${ctx}/cost/standingBook/" + linkId + "/sumStandingBookByLinkId",
            data: "",
            async: false,
            success: function (data) {
                $('#yingShou').val(data.ys);
                $('#yingFu').val(data.yf);
                $('#lirui').val(data.lr);
            }
        });
    }


    //弹窗增加
    function add() {
        var stockId = $("#stockOrgId").val();
        var linkId = $("#linkId").val();
        var planFeeState = "${planFeeState}";
        d1 = $("#dlg1").dialog({
            title: '引入方案费目',
            width: 1000,
            height: 480,
            href: '${ctx}/base/scheme/inEasySchemeList/' + stockId + "/" + linkId + "/" + planFeeState,
            maximizable: true,
            modal: true
        });
    }

    //删除
    function del() {
        editRow1 = undefined;
        editRow2 = undefined;

        var row1 = dg1.datagrid('getSelected');
        var row2 = dg2.datagrid('getSelected');

        if (row1 == null && row2 == null) {
            parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight"});
        } else {
            parent.$.messager.confirm('提示', '其中审核通过的、业务付款单生成的数据删除无效！同时非创建人不允许删除！', function (data) {
                if (data) {
                    var userId = $("#userId").val();
                    var newIdsList = [];
                    var datas1 = dg1.datagrid('getSelections');
                    for (var i = 0; i < datas1.length; i++) {
                        if (datas1[i].examineSign != 1 && datas1[i].inputPersonId == userId && "" != datas1[i].payId) {
                            newIdsList.push(datas1[i].standingNum);
                        }
                    }

                    var datas2 = dg2.datagrid('getSelections');
                    for (var i = 0; i < datas2.length; i++) {
                        if (datas2[i].examineSign != 1 && datas2[i].inputPersonId == userId && "" != datas2[i].payId) {
                            newIdsList.push(datas2[i].standingNum);
                        }
                    }

                    $.ajax({
                        async: false,
                        type: 'POST',
                        data: JSON.stringify(newIdsList),
                        contentType: 'application/json;charset=utf-8',
                        url: "${ctx}/cost/standingBook/deleteStandingBookBatch",
                        success: function (data) {
                            dg1.datagrid('reload');
                            dg2.datagrid('reload');
                        }
                    });
                }
            });
        }
    }

    //审核
    function isOK() {
    	var lirui=$("#lirui").val();
        
        editRow1 = undefined;
        editRow2 = undefined;

        var row1 = dg1.datagrid('getSelected');
        var row2 = dg2.datagrid('getSelected');
        
        if (row1 == null && row2 == null) {
            parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight"});
        } else {
            parent.$.messager.confirm('提示', '确定要审核通过吗？', function (data) {
                if (data) {
                    var newIdsList = [];
                    var datas1 = dg1.datagrid('getSelections');
                    for (var i = 0; i < datas1.length; i++) {
                        if (datas1[i].examineSign != 1) {
                            newIdsList.push(datas1[i].standingNum);
                        }
                    }

                    var datas2 = dg2.datagrid('getSelections');
                    for (var i = 0; i < datas2.length; i++) {
                        if (datas2[i].examineSign != 1) {
                            newIdsList.push(datas2[i].standingNum);
                        }
                    }

                    $.ajax({
                        async: false,
                        type: 'POST',
                        data: JSON.stringify(newIdsList),
                        contentType: 'application/json;charset=utf-8',
                        url: "${ctx}/cost/standingBook/isOkStandingBookBatch",
                        success: function (data) {
                            parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
                            dg1.datagrid('reload');
                            dg2.datagrid('reload');
                        }
                    });
                }
            });
        }
    }

    //取消审核
    function isNotOK() {
        editRow1 = undefined;
        editRow2 = undefined;

        var row1 = dg1.datagrid('getSelected');
        var row2 = dg2.datagrid('getSelected');

        if (row1 == null && row2 == null) {
            parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight"});
        } else {
            parent.$.messager.confirm('提示', '确定要取消审核吗？提示：其中已对账状态的数据取消审核操作无效！', function (data) {
                if (data) {
                    var newIdsList = [];
                    var datas1 = dg1.datagrid('getSelections');
                    for (var i = 0; i < datas1.length; i++) {
                        if (datas1[i].examineSign != 0 && datas1[i].reconcileSign != 1) {
                            newIdsList.push(datas1[i].standingNum);
                        }
                    }

                    var datas2 = dg2.datagrid('getSelections');
                    for (var i = 0; i < datas2.length; i++) {
                        if (datas2[i].examineSign != 0 && datas2[i].reconcileSign != 1) {
                            newIdsList.push(datas2[i].standingNum);
                        }
                    }

                    $.ajax({
                        async: false,
                        type: 'POST',
                        data: JSON.stringify(newIdsList),
                        contentType: 'application/json;charset=utf-8',
                        url: "${ctx}/cost/standingBook/isNotOkStandingBookBatch",
                        success: function (data) {
                            parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
                            dg1.datagrid('reload');
                            dg2.datagrid('reload');
                        }
                    });
                }
            });
        }
    }

    //计划费用完成
    function planOk() {
    	var lirui=$("#lirui").val();
        if ('${planFeeState}' == "1") {
            parent.$.messager.show({title: "提示", msg: "计划费用已在完成状态！", position: "bottomRight"});
            return;
        }
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/enterStock/planOk/" + '${linkId}',
            success: function (data) {
                parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
                window.parent.mainpage.mainTabs.refCurrentTab();//刷新TAB
            }
        });
    }

    //计划费用取消
    function planNo() {
        if ('${planFeeState}' == "0") {
            parent.$.messager.show({title: "提示", msg: "计划费用已在非完成状态！", position: "bottomRight"});
            return;
        }
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/enterStock/planNo/" + '${linkId}',
            success: function (data) {
                parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
                window.parent.mainpage.mainTabs.refCurrentTab();//刷新TAB
            }
        });
    }


    //费用完成
    function finishOk() {
    	var lirui=$("#lirui").val();
        if ('${finishFeeState}' == "1") {
            parent.$.messager.show({title: "提示", msg: "费用已在完成状态！", position: "bottomRight"});
            return;
        }
        var rowOne = dg1.datagrid("getRows");
        var rowTwo = dg2.datagrid("getRows");
        var noFinish=0;
        for(var i=0;i<rowOne.length;i++){
        	if(rowOne[i].examineSign==0){
        		noFinish=1;
        		break;
        	}
        }
        if(noFinish==0){
	        for(var j=0;j<rowTwo.length;j++){
	        	if(rowTwo[j].examineSign==0){
	        		noFinish=1;
	        		break;
        		}
	        }
	    }
	    if(noFinish==1){
	    	parent.$.messager.show({title: "提示", msg: "还有未审核的费用，不能进行此操作！", position: "bottomRight"});
            return;
	    }
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/enterStock/finishOk/" + '${linkId}',
            success: function (data) {
                parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
                window.parent.mainpage.mainTabs.refCurrentTab();//刷新TAB
            }
        });
    }


    //费用取消
    function finishNo() {
        if ('${finishFeeState}' == "0") {
            parent.$.messager.show({title: "提示", msg: "费用已在非完成状态！", position: "bottomRight"});
            return;
        }
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/enterStock/finishNo/" + '${linkId}',
            success: function (data) {
                parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
                window.parent.mainpage.mainTabs.refCurrentTab();//刷新TAB
            }
        });
    }

    //查看 最新合同
    function showContract() {
        var stockId = $("#stockId").val();
        $.ajax({
            async: false,
            type: 'GET',
            contentType: 'application/json;charset=utf-8',
            url: "${ctx}/base/contract/showContract?filter_EQS_clientId=" + stockId,
            success: function (data) {
                window.parent.mainpage.mainTabs.addModule('合同查看' + data.rows[0].contractNum, 'base/contract/checkContractForm/' + data.rows[0].contractNum);
            }
        });
    }
</script>
</body>
</html>