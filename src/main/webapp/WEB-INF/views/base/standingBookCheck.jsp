<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<form id="checkform" action="${ctx }/cost/standingBook/${action}" method="post">
    <div data-options="region:'north',split:true,border:false" style="height:150px">
        <div class="easyui-layout" data-options="fit:true">
            <div id="topbutton" class="datagrid-toolbar" data-options="region:'north',split:false,border:false"
                 style="height:auto;">
                <shiro:hasPermission name="bis:checkbook:save">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true"
                       onclick="save();">保存</a>
                    <span class="toolbar-item dialog-tool-separator"></span>
                </shiro:hasPermission>
            </div>
            <div data-options="region:'west',split:false,border:false" style="width:10px;"></div>
            <div data-options="region:'east',split:false,border:false" style="width:10px;"></div>
            <div data-options="region:'center',split:false,border:false" style="padding-top: 5px;">
                <table>
                    <tr>
                        <td style="width:60px; ">是否确认：</td>
                        <td>
                            <select class="easyui-combobox" id="isTrue" name="isTrue"
                                    data-options="width:150,required:'required'">
                                <option value="1">是</option>
                                <option value="2">否</option>
                            </select>
                        </td>
                        <td style="width:60px; ">结算方式：</td>
                        <td>
                            <select id="jsfs" name="jsfs"  class="easyui-combobox"
                             data-options="width:150,required:'required',prompt:'结算方式',valueField: 'id',textField:'text',value:'${obj.jsfs}',data: [{id:'Y',text:'月结'},{id:'X',text:'现结'}]"/>
                        </td>
                        <td style="width:60px; ">对账单号：</td>
                        <td>
                            <input type="text" id="codeNum" name="codeNum" class="easyui-validatebox"
                                   readonly="readonly"
                                   data-options="width:150,required:'required',validType:'length[1,20]'"
                                   value="${obj.codeNum}"/></td>
                        <td style="width:60px; ">账单客户：</td>
                        <td>
                            <input type="hidden" id="custom" name="custom" value="${obj.custom}"></input>
                            <select class="easyui-combobox" id="customID" name="customID"
                                    data-options="width:150,required:'required'">
                                <option value=""></option>
                            </select>
                        </td>
                        <td>账单年月：</td>
                        <td>
                            <input id="yearMonth" name="yearMonth" type="text" class="easyui-my97" datefmt="yyyy-MM"
                                   data-options="width: 150,prompt: '账单年月',required:'required'"
                                   value="${obj.yearMonth}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>账单总额：</td>
                        <td><input id="getTotal" class="easyui-validatebox" readonly="readonly"
                                   data-options="width:150"/></td>
                    </tr>
                    <tr style="height: 5px;"></tr>
                    <tr style="height: 5px;">
                        <td>备注：</td>
                        <td colspan="7"><textarea rows="2" name="remark" cols="110" class="easyui-validatebox"
                                                  data-options="validType:'length[1,50]'"
                                                  style="font-size: 12px;font-family: '微软雅黑'">${obj.remark}</textarea>
                        </td>
                    </tr>
                </table>

            </div>
            <div data-options="region:'south',split:false,border:false" style="height:auto">
                <div id="tbo" class="datagrid-toolbar">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                       onclick="addInfo()">添加</a>
                    <span class="toolbar-item dialog-tool-separator"></span>
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
                       data-options="disabled:false" onclick="del()">删除</a>

                    <select class="easyui-combobox" id="nType" data-options="width:100,prompt: '导出类型选择'">
                        <option value=""></option>
                        <option value="1">中文(小计)</option>
                        <option value="2">中文</option>
                        <option value="3">英文(小计)</option>
                        <option value="4">英文</option>
                    </select>
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel"
                       plain="true" onclick="report()">导出对账单</a>
                    <span class="toolbar-item dialog-tool-separator"></span>
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-white-acrobat"
                       plain="true" onclick="reportpdf()">导出对账单</a>
                    <span class="toolbar-item dialog-tool-separator"></span>
                </div>
            </div>

        </div>
    </div>
    <div data-options="region:'center',split:true,border:false">
        <div id="zdiv1" class="easyui-panel" data-options="title:'入库列表',height:100">
            <table id="dg1"></table>
        </div>
        <div style="height: 5px;"></div>
        <div id="zdiv2" class="easyui-panel" data-options="title:'出库列表',height:100">
            <table id="dg2"></table>
        </div>
        <div style="height: 5px;"></div>
        <div id="zdiv3" class="easyui-panel" data-options="title:'货转列表',height:100">
            <table id="dg3"></table>
        </div>
    </div>
    <div id="dlg"></div>
</form>
<form id="reportFrom" action="#" method="post">
    <input type="hidden" id="repcodeNum" name="codeNum"/>
    <input type="hidden" id="repnType" name="nType"/>
</form>
<script type="text/javascript">
    var dg, d;
    var model = '${action}';
    $(document).ready(function () {
        //加载结算单位
        var getReceiverOrgId = '${obj.customID}';
        $('#customID').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll?tim=1&setid=${obj.customID}",
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote',
            onSelect: function () {
                var getTxt = $('#customID').combobox("getText");
                $("#custom").val(getTxt);
            },
            onLoadSuccess: function () {
                if (getReceiverOrgId != null && getReceiverOrgId != "") {
                    $('#customID').combobox("select", Number(getReceiverOrgId));
                    getReceiverOrgId = "";
                }
            }
        });

        inTInfo("dg1", 1);
        inTInfo("dg2", 2);
        inTInfo("dg3", 3);
        //提交表单校验
        $('#checkform').form({
            onSubmit: function () {
                var isValid = $(this).form('validate');
                var getTxt = $('#customID').combobox("getText");
                $("#custom").val(getTxt);
                return isValid;
            },
            success: function (data) {
                if ("success" == data) {
                    parent.$.easyui.messager.show({title: "操作提示", msg: "保存成功！", position: "bottomRight"});
                    $(this).attr("action", "${ctx }/cost/standingBook/update");
                } else {
                    parent.$.easyui.messager.alert("保存对账单信息失败！");
                }
            }
        });

        //总金额
        $.ajax({
            async: false,
            type: 'POST',
            data: {"codeNum": $("#codeNum").val()},
            url: "${ctx}/cost/standingBook/gettotal",
            success: function (data) {
                $("#getTotal").val(data);
            }
        });
    })
    function save() {
        $('#checkform').submit();
    }
    //明细删除操作
    function del() {
        parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
            if (data) {
                var getDIds1 = getTabCheckIds("dg1");
                var getDIds2 = getTabCheckIds("dg2");
                var getDIds3 = getTabCheckIds("dg3");

//                var getDIds1 = checkTrueArr("dg1");
//                var getDIds2 = checkTrueArr("dg2");
//                var getDIds3 = checkTrueArr("dg3");

                postDelDZInfo(getDIds1, getDIds2, getDIds3);
            }
        })

    }

    //异步删除对账单明细
    function postDelDZInfo(ids1, ids2, ids3) {
        $.post('${ctx}/cost/standingBook/postdelInfo', {
            "code": "${obj.codeNum}",
            "ids1": ids1,
            "ids2": ids2,
            "ids3": ids3
        }, function (data) {
            if (data != null) {
                if ("success" == data.endStr) {
                    if ("" != ids1) {
                        inTInfo("dg1", 1);
                    }
                    if ("" != ids2) {
                        inTInfo("dg2", 2);
                    }
                    if ("" != ids3) {
                        inTInfo("dg3", 3);
                    }
                }
            }
        }, "json");
    }

    var checkArr1 = {};
    var checkArr2 = {};
    var checkArr3 = {};

    var checkTrue1;
    var checkTrue2;
    var checkTrue3;

    function checkTrueArr(sTableId) {

        var checkTrue = "";

//        if (sTableId == "dg1") {
//            checkTrue = checkTrue1;
//        } else if (sTableId == "dg2") {
//            checkTrue = checkTrue2;
//        } else if (sTableId == "dg3") {
//            checkTrue = checkTrue3;
//        }

        var rows = $('#' + sTableId).datagrid("getRows");

        var rowLength = rows.length;

        var panel = $('#' + sTableId).datagrid("getPanel");

        for (var i = 0; i < rowLength - 1; i++) {

            var isCheck = panel.find("input[name='chekbox']:eq(" + i + ")").attr("checked");

            if (isCheck) {

                if (checkTrue.length > 0) {
                    checkTrue += ( "," + rows[i]["IDS"] );
                } else {
                    checkTrue = rows[i]["IDS"];
                }

            }

        }

        console.log(checkTrue);

        if (sTableId == "dg1") {
            checkTrue1 = checkTrue;
        } else if (sTableId == "dg2") {
            checkTrue2 = checkTrue;
        } else if (sTableId == "dg3") {
            checkTrue3 = checkTrue;
        }

    }

    function inTInfo(sTableid, nType) {

        $('#' + sTableid).datagrid({
                method: "post",
                url: '${ctx}/cost/standingBook/jsoncheckinfo',
                fit: true,
                fitColumns: true,
                border: false,
                striped: true,
                pagination: false,
                rownumbers: true,
                pageNumber: 1,
                pageSize: 10,
                pageList: [10, 20, 30, 40, 50],
                columns: [[
                    {
                        field: 'checkAll', title: '全选',
                        formatter: function (value, row, index) {
                            return '<input type="checkbox" name="checkAll">';
                        }
                    },
//                    {field: 'checkAll', title: '全选', sortable: false, width: 100, checkbox: true},
                    {field: 'LINK_ID', title: '联系单', sortable: false, width: 100},
                    {
                        field: 'BILL_NUM', title: '提单号', sortable: false, width: 100,
                        formatter: function (value, row, index) {

                            var tt = value;

                            // console.log(row.LINK_ID + "," + value);
                            if ((value == "" || value == null) && sTableid == "dg2") {
                                $.ajax({
                                    async: false,
                                    type: 'POST',
                                    data: {"linkId": row.LINK_ID},
                                    url: "${ctx}/cost/standingBook/connectBillNum",
                                    success: function (data) {
                                        tt = data;
                                    }
                                });
                            }
                            return tt;
                        }
                    },
                    {field: 'CUSTOMS_NAME', title: '委托单位', sortable: false, width: 100},
                    {field: 'Money', title: '费用金额', sortable: false},
                    {
                        field: 'chekbox',
                        title: '明细选择',
                        sortable: false,
                        width: 100,
                        checkbox: true
                    },
                    {field: 'FEE_NAME', title: '明细费用名称', sortable: false, width: 100},
                    {field: 'RMB', title: '明细金额', sortable: false, width: 100},
                    {
                        field: 'CURRENCY', title: '币种', sortable: false, width: 100,
                        formatter: function (value, row, index) {
                            if (value == '0') {
                                return "人民币";
                            }
                            if (value == '1') {
                                return "美元";
                            }
                            if (value == '2') {
                                return "日元";
                            }
                            if (value == '201') {
                                return "阿尔及利亚第纳尔";
                            }
                        }
                    },
                    {field: 'EXCHANGE_RATE', title: '汇率', sortable: false, width: 100},
                    {field: 'BILL_DATE', title: '年月', sortable: false, width: 100},
                    {
                        field: 'PAY_SIGN',
                        title: '是否垫付',
                        sortable: false,
                        width: 100,
                        formatter: function (value, row, index) {
                            return value == 1 ? '是' : '否';
                        }
                    },
                    {field: 'IDS', title: '', hidden: true}
                ]],
                queryParams: {
                    nType: nType,
                    codeNum: function () {
                        return "${obj.codeNum}";
                    }
                },

                onClickCell: function (index, field, value) {

                    // 已合并的单元的数组
                    var mergeArr;

                    if (sTableid == "dg1") {
                        mergeArr = checkArr1;
                    } else if (sTableid == "dg2") {
                        mergeArr = checkArr2;
                    } else if (sTableid == "dg3") {
                        mergeArr = checkArr3;
                    }

                    var panel = $(this).datagrid("getPanel");

                    if (field == "checkAll") {

                        // 是否选中
                        var isCheck = panel.find("input[name='checkAll']:eq(" + index + ")").attr("checked");

                        // 需要选中的行
                        var lines = mergeArr[index];

                        if (lines != null || lines.length != 0) {

                            for (var i = 0; i < lines.length; i++) {

                                // 需要操作的行号
                                var row = lines[i];

                                // 行是否已经选则
                                var selected = false;

                                // 所有已选中行
                                var selectionRows = $(this).datagrid("getSelections");

                                for (var k = 0; k < selectionRows.length; k++) {

                                    // 已选中行的数据
                                    var selectionRow = selectionRows[k];

                                    // 已选中行的行号
                                    var selectionIndex = $(this).datagrid("getRowIndex", selectionRow);

                                    // 如果行在已选中行中存在，则将行设置为选中状态
                                    if (selectionIndex == row) {
                                        selected = true;
                                        break;
                                    }

                                }

                                console.log(selected);

                                if (isCheck) {
                                    // panel.find("input[name='chekbox']:eq(" + row + ")").attr("checked", true);

                                    // 如果未选中行，则选中行
                                    if (!selected) $(this).datagrid('selectRow', row);

                                    if (index == row) $(this).datagrid('unselectRow', row);

                                } else {
                                    // panel.find("input[name='chekbox']:eq(" + row + ")").attr("checked", false);

                                    // 如果已选中行，则取消选中行
                                    if (selected) $(this).datagrid('unselectRow', row);

                                    if (index == row) $(this).datagrid('selectRow', row);
                                }

                            }

                        }

                    }

                },
                onClickRow: function (index, data) {
//                    $(this).datagrid('unselectRow', index);
                },
                onLoadSuccess: function () {

                    var rows = $("#" + sTableid).datagrid('getRows');

                    if (rows == null || rows.length == 0) return;

                    var getLinkId = "";
                    var getRow;
                    var sumMoney = 0, mySpan = 0;

                    var oldLinkId = "";
                    var flagIndex = 0;

                    var arr;
                    var aSize = 0;

                    var mergeArray = {};

                    //合并单元格
                    for (var i = 0; i < rows.length; i++) {

                        getRow = rows[i];

                        var linkId = getRow["LINK_ID"];

                        if (oldLinkId == linkId) {
//                            mergeArray[flagIndex] = mergeArray[flagIndex] + "," + i;

                            arr = mergeArray[flagIndex];

//                            console.log(arr);

                            arr[aSize] = i;

                            aSize++;

                            mergeArray[flagIndex] = arr;

                        } else {
                            oldLinkId = linkId;
                            flagIndex = i;

                            arr = new Array;
                            aSize = 0;

                            arr[aSize] = i;

                            aSize++;

                            mergeArray[flagIndex] = arr;
                        }

                        if (getLinkId != "" && getLinkId != getRow["LINK_ID"]) {

                            $("#" + sTableid).datagrid('updateRow', {
                                index: (i - mySpan) >= 0 ? (i - mySpan) : 0,
                                row: {Money: sumMoney.toFixed(2)}
                            });

                            $("#" + sTableid).datagrid('mergeCells', {
                                index: i - mySpan,
                                field: 'checkAll',
                                rowspan: mySpan,
                                colspan: 1
                            });

                            $("#" + sTableid).datagrid('mergeCells', {
                                index: i - mySpan,
                                field: 'LINK_ID',
                                rowspan: mySpan,
                                colspan: 1
                            });

                            $("#" + sTableid).datagrid('mergeCells', {
                                index: i - mySpan,
                                field: 'BILL_NUM',
                                rowspan: mySpan,
                                colspan: 1
                            });

                            $("#" + sTableid).datagrid('mergeCells', {
                                index: i - mySpan,
                                field: 'CUSTOMS_NAME',
                                rowspan: mySpan,
                                colspan: 1
                            });

                            $("#" + sTableid).datagrid('mergeCells', {
                                index: i - mySpan,
                                field: 'Money',
                                rowspan: mySpan,
                                colspan: 1
                            });

                            getLinkId = getRow["LINK_ID"];
                            sumMoney = 0;
                            mySpan = 1;
                            sumMoney += Number(getRow["RMB"]);

                            if (i == rows.length - 1) {
                                $("#" + sTableid).datagrid('updateRow', {index: i, row: {Money: (sumMoney).toFixed(2)}});
                            }

                        } else if (getLinkId != "" && i == rows.length - 1) {

                            $("#" + sTableid).datagrid('updateRow', {
                                index: i - mySpan,
                                row: {Money: (sumMoney + Number(getRow["RMB"])).toFixed(2)}
                            });

                            $("#" + sTableid).datagrid('mergeCells', {
                                index: i - mySpan,
                                field: 'checkAll',
                                rowspan: mySpan + 1,
                                colspan: 1
                            });

                            $("#" + sTableid).datagrid('mergeCells', {
                                index: i - mySpan,
                                field: 'LINK_ID',
                                rowspan: mySpan + 1,
                                colspan: 1
                            });
                            $("#" + sTableid).datagrid('mergeCells', {
                                index: i - mySpan,
                                field: 'BILL_NUM',
                                rowspan: mySpan + 1,
                                colspan: 1
                            });
                            $("#" + sTableid).datagrid('mergeCells', {
                                index: i - mySpan,
                                field: 'CUSTOMS_NAME',
                                rowspan: mySpan + 1,
                                colspan: 1
                            });
                            $("#" + sTableid).datagrid('mergeCells', {
                                index: i - mySpan,
                                field: 'Money',
                                rowspan: mySpan + 1,
                                colspan: 1
                            });

                        } else {
                            getLinkId = getRow["LINK_ID"];
                            sumMoney += Number(getRow["RMB"]);
                            mySpan++;
                        }

                    }//end for

                    if (sTableid == "dg1") {
                        checkArr1 = mergeArray;
                    } else if (sTableid == "dg2") {
                        checkArr2 = mergeArray;
                    } else if (sTableid == "dg3") {
                        checkArr3 = mergeArray;
                    }

//                    //添加总合计
//                    var rowss = $("#" + sTableid).datagrid('getRows')//获取当前的数据行
//                    var totals = 0;
//                    for (var i = 0; i < rowss.length; i++) {
//                        if (rowss[i]["IDS"] != null && rowss[i]["IDS"] != "") {
//                            totals += rowss[i]['RMB'];
//                        }
//                    }
//
//                    $("#" + sTableid).datagrid("insertRow", {
//                        index: $("#" + sTableid).datagrid('getData').total,
//                        row: {
//                            LINK_ID: '总计',
//                            Money: totals
//                        }
//                    });

                    //重新定义span的高度
                    var getRowsSize = $("#" + sTableid).datagrid('getData').total;
                    $("#zdiv" + nType).panel('resize', {
                        height: getRowsSize * 25 + 100
                    });

                },
                enableHeaderClickMenu: true,
                enableHeaderContextMenu: true,
                enableRowContextMenu: false
            }
        )
        ;
    }

    function checkAll() {

        alert("checkAll");

    }

    //打开添加页面
    function addInfo() {
        var getId = $("#customID").combobox("getValue");
        if (getId == null || "" == getId) {
            parent.$.easyui.messager.alert("请先选择账单客户！");
            return;
        }
        var codeNum = $("#codeNum").val();
    	$.ajax({
    		type:'get',
    		url:"${ctx}/cost/standingBook/ifsave/"+codeNum,
    		success: function(data){
    			if(data != "success"){
    				parent.$.messager.show({title: "提示", msg: "未保存！", position: "bottomRight" });
    				return;
    			}else{
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

    		                    postAddDZInfo(ids1, ids2, ids3);
    		                    //d.panel('close');
    		                }
    		            }, {
    		                text: '取消',
    		                handler: function () {
    		                    d.panel('close');
    		                }
    		            }]
    		        });
    			}
    		}
    	});
    }

    //异步添加对账单明细
    function postAddDZInfo(ids1, ids2, ids3) {
        $.post('${ctx}/cost/standingBook/postaddInfo', {
            "code": "${obj.codeNum}",
            "ids1": ids1,
            "ids2": ids2,
            "ids3": ids3
        }, function (data) {
            if (data != null) {
                if ("success" == data.endStr) {
                    d.panel('close');
                    if ("" != ids1) {
                        inTInfo("dg1", 1);
                    }
                    if ("" != ids2) {
                        inTInfo("dg2", 2);
                    }
                    if ("" != ids3) {
                        inTInfo("dg3", 3);
                    }
                }
            }
        }, "json");
    }

    //获取弹窗选中的对账明细集合ids
    function getTabCheckIds(tabId) {
        var reIds = "";
        var rows = $("#" + tabId).datagrid('getChecked');
        if (rows != null && rows.length > 0) {
            for (var i = 0; i < rows.length; i++) {
                if (0 == i) {
                    reIds = rows[i]["IDS"];
                } else {
                    reIds = reIds + "," + rows[i]["IDS"];
                }
            }
        }

        console.log(reIds);

        return reIds;
    }

    //导出
    function report() {
        var getType = $("#nType").combobox("getValue");
        if (getType == null || "" == getType) {
            parent.$.easyui.messager.alert("请选择导出类型！");
            return;
        }
        $("#repcodeNum").val($("#codeNum").val());
        $("#repnType").val(getType);
        $("#reportFrom").attr("action", "${ctx}/cost/checkingbook/report");
        $("#reportFrom").submit();
    }
    function reportpdf() {
        var getType = $("#nType").combobox("getValue");
        if (getType == null || "" == getType) {
            parent.$.easyui.messager.alert("请选择导出类型！");
            return;
        }
        $("#repcodeNum").val($("#codeNum").val());
        $("#repnType").val(getType);
        $("#reportFrom").attr("action", "${ctx}/cost/checkingbook/reportpdf");
        $("#reportFrom").submit();
    }
</script>
</body>
</html>