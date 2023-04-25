<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body class="easyui-layout">
<div data-options="region:'north',split:true,border:false" style="padding:5px;height:auto">
    <div>
        <form id="searchLinkFrom" action="" method="post">
            <input type="text" id="sLinkId" name="sLinkId" class="easyui-validatebox"
                   data-options="width:150,prompt: '联系单号'"/>
            <select class="easyui-combobox" id="nType" name="nType" data-options="width:150,prompt: '出/入类型'">
                <option value="0"></option>
                <option value="1">入</option>
                <option value="2">出</option>
                <option value="3">在库</option>
            </select>
            <!-- <input type="text" id="sBaoGuan" name="sBaoGuan" class="easyui-validatebox" data-options="width:150,prompt: '报关单号'"/> -->
            <input type="text" id="sCustom" name="sCustom" class="easyui-validatebox"
                   data-options="width:150,prompt: '委托单位'"/>
            <input type="text" id="sIBillNum" name="sIBillNum" class="easyui-validatebox"
                   data-options="width:150,prompt: '入库提单号'"/>
            <input type="text" id="sOBillNum" name="sOBillNum" class="easyui-validatebox"
                   data-options="width:150,prompt: '出库提单号'"/>
            <input type="text" id="sFeePlan" name="sFeePlan" class="easyui-validatebox"
                   data-options="width:150,prompt: '费用项'"/>
            <input id="straTime" name="startStoreDate" type="text" class="easyui-my97" datefmt="yyyy-MM"
                   data-options="width: 150,prompt: '开始年月'"/>
            <input id="endTime" name="startStoreDate" type="text" class="easyui-my97" datefmt="yyyy-MM"
                   data-options="width: 150,prompt: '结束年月'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true"
               onclick="cxLink()">查询</a>
        </form>
    </div>
</div>
<div data-options="region:'center',split:true,border:false">
    <div id="tdiv1" class="easyui-panel" data-options="title:'入库列表',height:150">
        <table id="sdg1"></table>
    </div>
    <div style="height: 5px;"></div>
    <div id="tdiv2" class="easyui-panel" data-options="title:'出库列表',height:150">
        <table id="sdg2"></table>
    </div>
    <div style="height: 5px;"></div>
    <div id="tdiv3" class="easyui-panel" data-options="title:'货转列表',height:150">
        <table id="sdg3"></table>
    </div>
</div>
<div id="dlglx"></div>
<script type="text/javascript">

    var dglx;
    var sdg1, sdg2, sdg3;
    var checkArr1 = {};
    var checkArr2 = {};
    var checkArr3 = {};

    var checkTrue1;
    var checkTrue2;
    var checkTrue3;
    $(function () {

        var getReceiverOrgId = $("#customID").combobox("getValue");

        $('#sCustom').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll?tim=1&setid=" + getReceiverOrgId,
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote',
            onLoadSuccess: function () {

                if (getReceiverOrgId != null && getReceiverOrgId != "") {
                    $('#sCustom').combobox("select", Number(getReceiverOrgId));
                    getReceiverOrgId = "";
                }

            }
        });

        intoTable("sdg1", 1, 0);
        intoTable("sdg2", 2, 0);
        intoTable("sdg3", 3, 0);

    });

    function cxLink() {

        var nType = $("#nType").combobox("getValue");
        // var sObj = $("#searchLinkFrom").serializeObject();

        if (1 == nType) {
            intoTable("sdg1", 1, 1);
        } else if (2 == nType) {
            intoTable("sdg2", 2, 1);
        } else if (3 == nType) {
            intoTable("sdg3", 3, 1);
        } else {
            intoTable("sdg1", 1, 1);
            intoTable("sdg2", 2, 1);
            intoTable("sdg3", 3, 1);
        }

    }

    function intoTable(sTableId, nType, ncx) {

        sdg1 = $("#" + sTableId).datagrid({
            method: "post",
            url: '${ctx}/cost/standingBook/jsongroup',
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
                {field: 'LINK_ID', title: '联系单', sortable: false, width: 100},
                {field: 'BILL_NUM', title: '提单号', sortable: false, width: 100},
                {field: 'CUSTOMS_NAME', title: '委托单位', sortable: false, width: 100},
                {field: 'Money', title: '费用金额', sortable: false, width: 100},
                {field: 'chekbox', title: '明细选择', checkbox: true, sortable: false, width: 100},
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
                {
                    field: 'BILL_DATE',
                    title: '年月',
                    sortable: false,
                    width: 100,
                    formatter: function (value, row, index) {
                        if (value != null && "" != value) {
                            nindex = value.lastIndexOf("-");
                            return value.substring(0, nindex);
                        }
                        return value;
                    }
                },
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
                sDZId: "${code}",
                sLinkId: function () {
                    return $("#sLinkId").val();
                },
                sCustom: function () {
                    return $("#sCustom").combobox("getValue");
                },
                sIBillNum: function () {
                    return $("#sIBillNum").val();
                },
                sOBillNum: function () {
                    return $("#sOBillNum").val();
                },
                sFeePlan: function () {
                    return $("#sFeePlan").val();
                },
                straTime: function () {
                    return $("#straTime").datebox("getValue");
                },
                endTime: function () {
                    return $("#endTime").datebox("getValue");
                },
                isCX: ncx
            },
            //yyyyy
            onClickCell: function (index, field, value) {

                    // 已合并的单元的数组
                    var mergeArr;

                    if (sTableId == "sdg1") {
                        mergeArr = checkArr1;
                    } else if (sTableId == "sdg2") {
                        mergeArr = checkArr2;
                    } else if (sTableId == "sdg3") {
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
            onLoadSuccess: function () {
                var rows = $("#" + sTableId).datagrid('getRows');
                if (rows == null || rows.length == 0) {
                    $("#tdiv" + nType).panel('resize', {
                        height: 150
                    });
                    return;
                }
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
//                        mergeArray[flagIndex] = mergeArray[flagIndex] + "," + i;

                        arr = mergeArray[flagIndex];

//                        console.log(arr);

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
                        $("#" + sTableId).datagrid('updateRow', {
                            index: (i - mySpan) >= 0 ? (i - mySpan) : 0,
                            row: {Money: sumMoney.toFixed(2)}
                        });
                        $("#" + sTableId).datagrid('mergeCells', {
                            index: i - mySpan,
                            field: 'checkAll',
                            rowspan: mySpan,
                            colspan: 1
                        });
                        $("#" + sTableId).datagrid('mergeCells', {
                            index: i - mySpan,
                            field: 'LINK_ID',
                            rowspan: mySpan,
                            colspan: 1
                        });
                        $("#" + sTableId).datagrid('mergeCells', {
                            index: i - mySpan,
                            field: 'BILL_NUM',
                            rowspan: mySpan,
                            colspan: 1
                        });
                        $("#" + sTableId).datagrid('mergeCells', {
                            index: i - mySpan,
                            field: 'CUSTOMS_NAME',
                            rowspan: mySpan,
                            colspan: 1
                        });
                        $("#" + sTableId).datagrid('mergeCells', {
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
                            $("#" + sTableId).datagrid('updateRow', {index: i, row: {Money: (sumMoney).toFixed(2)}});
                        }
                    } else if (getLinkId != "" && i == rows.length - 1) {
                        $("#" + sTableId).datagrid('updateRow', {
                            index: i - mySpan,
                            row: {Money: (sumMoney + Number(getRow["RMB"])).toFixed(2)}
                        });
                        $("#" + sTableId).datagrid('mergeCells', {
                            index: i - mySpan,
                            field: 'checkAll',
                            rowspan: mySpan + 1,
                            colspan: 1
                        });
                        $("#" + sTableId).datagrid('mergeCells', {
                            index: i - mySpan,
                            field: 'LINK_ID',
                            rowspan: mySpan + 1,
                            colspan: 1
                        });
                        $("#" + sTableId).datagrid('mergeCells', {
                            index: i - mySpan,
                            field: 'BILL_NUM',
                            rowspan: mySpan + 1,
                            colspan: 1
                        });
                        $("#" + sTableId).datagrid('mergeCells', {
                            index: i - mySpan,
                            field: 'CUSTOMS_NAME',
                            rowspan: mySpan + 1,
                            colspan: 1
                        });
                        $("#" + sTableId).datagrid('mergeCells', {
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
                    
                    if (sTableId == "sdg1") {
                        checkArr1 = mergeArray;
                    } else if (sTableId == "sdg2") {
                        checkArr2 = mergeArray;
                    } else if (sTableId == "sdg3") {
                        checkArr3 = mergeArray;
                    }
                    /*if(getLinkId!="" && getLinkId!=getRow["LINK_ID"] ){
                     $("#"+sTableId).datagrid('updateRow', {index:(i-1-mySpan)>=0?(i-1-mySpan):0,row:{Money:sumMoney.toFixed(2)}});
                     $("#"+sTableId).datagrid('mergeCells', {index:i-1-mySpan,field:'LINK_ID',rowspan:mySpan+1,colspan:1});
                     $("#"+sTableId).datagrid('mergeCells', {index:i-1-mySpan,field:'BILL_NUM',rowspan:mySpan+1,colspan:1});
                     $("#"+sTableId).datagrid('mergeCells', {index:i-1-mySpan,field:'CUSTOMS_NAME',rowspan:mySpan+1,colspan:1});
                     $("#"+sTableId).datagrid('mergeCells', {index:i-1-mySpan,field:'Money',rowspan:mySpan+1,colspan:1});
                     getLinkId=getRow["LINK_ID"];
                     sumMoney=0;
                     mySpan=0;
                     sumMoney+=Number(getRow["RMB"]);
                     }else if(getLinkId!="" && i==rows.length-1){
                     $("#"+sTableId).datagrid('updateRow', {index:i-1-mySpan,row:{Money:(sumMoney+Number(getRow["RMB"])).toFixed(2)}});
                     $("#"+sTableId).datagrid('mergeCells', {index:i-1-mySpan,field:'LINK_ID',rowspan:mySpan+2,colspan:1});
                     $("#"+sTableId).datagrid('mergeCells', {index:i-1-mySpan,field:'BILL_NUM',rowspan:mySpan+2,colspan:1});
                     $("#"+sTableId).datagrid('mergeCells', {index:i-1-mySpan,field:'CUSTOMS_NAME',rowspan:mySpan+2,colspan:1});
                     $("#"+sTableId).datagrid('mergeCells', {index:i-1-mySpan,field:'Money',rowspan:mySpan+2,colspan:1});
                     }else{
                     getLinkId=getRow["LINK_ID"];
                     sumMoney+=Number(getRow["RMB"]);
                     mySpan++;
                     }*/

                }//end for
                //重新定义span的高度
                var getRowsSize = $("#" + sTableId).datagrid('getData').total;
                $("#tdiv" + nType).panel('resize', {
                    height: getRowsSize * 25 + 70
                });
            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false
        });
    }

</script>
</body>
</html>