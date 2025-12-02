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

            <input type="text" name="asn" class="easyui-validatebox" data-options="width:130,prompt: 'ASN号'"/>
            <input type="text" name="billNum" class="easyui-validatebox" data-options="width:130,prompt: '提单号'"/>
            <input type="text" name="linkId" class="easyui-validatebox" data-options="width:130,prompt: '联系单号'"/>
            <input type="text" name="ctnNum" class="easyui-validatebox" data-options="width:130,prompt: '箱号'"/>

            <select class="easyui-combobox" id="stockIn" name="stockIn" data-options="width:130,prompt:'存货方'">
                <option value=""></option>
            </select>

            <select class="easyui-combobox" id="ifSecondEnter" name="ifSecondEnter"
                    data-options="width:100,prompt:'入库类型'">
                <option value=""></option>
            </select>
            <select class="easyui-combobox" id="asnState" name="asnState" data-options="width:100,prompt:'ASN状态'">
                <option value=""></option>
            </select>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        </form>

        <shiro:hasPermission name="bis:asn:add">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
               onclick="add();">添加</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:asn:update">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
               onclick="upd()">修改</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:asn:delete">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
               data-options="disabled:false" onclick="del()">删除</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:asn:print">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-print" plain="true" onclick="print()">打印ASN</a>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-print" plain="true" onclick="printCard()">打印垛卡</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:asn:endd">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-lock" plain="true"
               onclick="endd()">完结</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:asn:unendd">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-lock-delete" plain="true"
               onclick="unendd()">取消完结</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:asn:update">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-old-versions" plain="true"
               onclick="createReservation()">生成预约入库信息</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
    </div>

    <div>
        件数合计：<input type="text" id="allPiece" value="0" readonly/>
        净重合计：<input type="text" id="allNet" value="0" readonly/>
        毛重合计：<input type="text" id="allGross" value="0" readonly/>
    </div>

</div>
<table id="dg"></table>
<div id="dlg"></div>
<script type="text/javascript">
    var dg;
    var d;
    var action = "${action}";
    var linkId = "${linkId}";

    var allPiece;
    var allGross;
    var allNet;

    document.onkeydown = function () {
        if (event.keyCode == 13) {
            cx();
        }
    };
    $(document).ready(function () {
        allPiece = $('#allPiece');
        allGross = $('#allGross');
        allNet = $('#allNet');
        searchAllCount(null);
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/bis/asn/listjson',
            fit: true,
            fitColumns: false,
            border: false,
            idField: 'asn',
            sortOrder: 'desc',
            sortName:'createTime',
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'asn', title: 'ASN号', sortable: false, width: 80},
                {field: 'billNum', title: '提单号', sortable: false, width: 150},
                {field: 'ctnNum', title: '箱号', sortable: false, width: 100},
                {field: 'stockName', title: '存货方', sortable: false, width: 280},
                {field: 'linkId', title: '联系单号', sortable: false, width: 200},
                {
                    field: 'ifSecondEnter', title: '入库类型', sortable: false, formatter: function (value, row, index) {
                    var retStr = "";
                    switch (value) {
                        case '1':
                            retStr = "正常";
                            break;
                        case '2':
                            retStr = "重收";
                            break;
                        case '3':
                            retStr = "分拣";
                            break;
                    }
                    return retStr;
                }},
                {
                    field: 'asnState', title: 'ASN状态', sortable: false, formatter: function (value, row, index) {
                    var retStr = "";
                    switch (value) {
                        case '1':
                            retStr = "在途";
                            break;
                        case '2':
                            retStr = "收货中";
                            break;
                        case '3':
                            retStr = "已上架";
                            break;
                        case '4':
                            retStr = "已完结";
                            break;
                    }
                    return retStr;
                }
                },
                {field: 'tallyUser', title: '理货人员', sortable: false, width: 60},
                {
                    field: 'isBonded', title: '是否保税', sortable: false, formatter: function (value, row, index) {
                    return value == 0 ? '否' : '是';
                }
                },
                {field: 'createTime', title: '创建时间', sortable: true, width: 150},
                {field: 'punm', title: '入库数量', sortable: false, width: 100},
                {field: 'net', title: '收货净重', sortable: false, width: 100},
                {field: 'gross', title: '收货毛重', sortable: false, width: 100},
                {
                    field: 'pieceinfo', title: '计划入库数量', sortable: false, width: 100,
                    formatter: function (value, row, index) {
                        var a;
                        $.ajax({
                            async: false,
                            type: 'get',
                            url: "${ctx}/bis/asn/getinfonum/" + row.asn,
                            success: function (data) {
                                a = data;
                            }
                        });
                        return a;
                    }
                }
            ]],
            onLoadSuccess: function () {
                //asn总览表增加实际入库数量
                var rows = $('#dg').datagrid('getRows');
                var asnIds = "";//存放ASNCode集合
                if (rows != "") {
                    var rowObj;
                    for (var i = 0; i < rows.length; i++) {
                        rowObj = rows[i];
                        asnIds += rowObj['asn'] + ",";
                    }
                    //异步获取实际入库数量
                    $.post("${ctx}/bis/asn/pnumjson", {"asnIds": asnIds}, function (data) {
                        if (data != null) {
                            for (var i = 0; i < rows.length; i++) {
                                //先填充0,如果未查询到入库数据则数据填充0
                                $('#dg').datagrid('updateRow', {
                                    index: i, row: {
                                        punm: 0, gross: 0, net: 0
                                    }
                                });
                                for (var k = 0; k < data.length; k++) {
                                    if (rows[i]['asn'] == data[k].ASN) {
                                        $('#dg').datagrid('updateRow', {
                                            index: i,
                                            row: {
                                                punm: data[k].PNUM,
                                                gross: data[k].GROSS,
                                                net: data[k].NET
                                            }
                                        });
                                        break;
                                    }
                                }//end for
                            }
                        }
                    }, "json");
                }

            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
        //入库类型
        $.ajax({
            type: "GET",
            url: "${ctx}/system/dict/json",
            data: "filter_LIKES_type=asnType",
            dataType: "json",
            success: function (date) {
                // for(var i=0;i<date.rows.length;i++){
                if (date != null && date.rows != null && date.rows.length > 0) {
                    $('#ifSecondEnter').combobox({
                        data: date.rows,
                        valueField: 'value',
                        textField: 'label'
                    });
                }
                //}
            }
        });
        //asn状态
        $.ajax({
            type: "GET",
            url: "${ctx}/system/dict/json",
            data: "filter_LIKES_type=asnState",
            dataType: "json",
            success: function (date) {
                if (date != null && date.rows != null && date.rows.length > 0) {
                    $('#asnState').combobox({
                        data: date.rows,
                        valueField: 'value',
                        textField: 'label'
                    });
                }
            }
        });
        //客户
        $('#stockIn').combobox({
            method: "GET",
            url: encodeURI(encodeURI("${ctx}/base/client/getClientAll?filter_EQS_clientSort=0")),
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote'
        });
        //是否是入库联系单跳转来的
        if (action == "enter") {
            $("input[name='linkId']").val(linkId);
            window.setTimeout(function () {
                cx()
            }, 100);
        }
    });

    //添加
    function add(title, href) {
        var href = 'bis/asn/create/000000';
        window.parent.mainpage.mainTabs.addModule('ASN管理', href, 'icon-hamburg-customers');
    }
    //修改
    function upd() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        var href = '${ctx}/bis/asn/update/' + row.asn;
        window.parent.mainpage.mainTabs.addModule('ASN管理', href, 'icon-hamburg-customers');
    }

    //查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
        searchAllCount(obj);
    }

    function searchAllCount(obj) {
        $.ajax({
            type: "post",
            url: "${ctx}/bis/asn/getInfoCount",
            data: obj,
            async: true,
            success: function (data) {
                allPiece.val(data["PNUM"] == null ? 0 : data["PNUM"]);
                allGross.val(data["GROSS"] == null ? 0 : data["GROSS"]);
                allNet.val(data["NET"] == null ? 0 : data["NET"]);
            }
        });
    }
    //删除
    function del() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
            if (data) {
                $.ajax({
                    type: 'post',
                    url: "${ctx}/bis/asn/delete/" + row.asn,
                    success: function (data) {
                        if ("error" == data) {
                            parent.$.easyui.messager.alert("ASN号：" + row.asn + " 删除失败！");
                        } else if ("state" == data) {
                            parent.$.easyui.messager.alert("ASN号：" + row.asn + " 状态不允许删除！");
                        } else {
                            successTip(data, dg);
                        }
                    }
                });
            }
        });
    }
    //完结操作
    function endd() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        if (3 == row.asnState) {
            $.ajax({
                type: 'post',
                url: "${ctx}/bis/asn/endd/" + row.asn,
                success: function (data) {
                    if ("error" == data) {
                        parent.$.easyui.messager.alert("ASN号：" + row.asn + "操作失败！");
                    } else if ("state" == data) {
                        parent.$.easyui.messager.alert("ASN号：" + row.asn + " 现在状态不允许完结！");
                    } else if ("nofee" == data) {
                        parent.$.easyui.messager.alert("ASN号：" + row.asn + " 对应联系单的费用方案中缺少出入库费的费用，无法计算出入库费！");
                    } else if ("nofjok" == data) {
                        parent.$.easyui.messager.alert("ASN号：" + row.asn + " 对应联系单的费用方案中缺少对应的限价数，无法计算分拣费！");
                    } else if ("nofjfee" == data) {
                        parent.$.easyui.messager.alert("ASN号：" + row.asn + " 对应联系单的费用方案中缺少分拣费的费用，无法计算分拣费！");
                    } else {
                        successTip(data, dg);
                    }
                }
            });
        } else {
            parent.$.easyui.messager.alert("ASN号：" + row.asn + " 现在状态不允许完结！");
        }

    }
    //取消完结
    function unendd() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        if (4 == row.asnState) {
            $.ajax({
                type: 'post',
                url: "${ctx}/bis/asn/unendd/" + row.asn,
                success: function (data) {
                    if ("error" == data) {
                        parent.$.easyui.messager.alert("ASN号：" + row.asn + "操作失败！");
                    } else if ("state" == data) {
                        parent.$.easyui.messager.alert("ASN号：" + row.asn + " 现在状态不允许取消完结！");
                    } else {
                        successTip(data, dg);
                    }
                }
            });
        } else {
            parent.$.easyui.messager.alert("ASN号：" + row.asn + " 现在状态不允许取消完结！");
        }

    }
    //打印
    function print() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        window.parent.mainpage.mainTabs.addModule('打印','${ctx}/bis/asn/print/'+row.asn);
       // window.parent.open("${ctx}/bis/asn/print/" + row.asn);
    }
    //打印跺卡
    function printCard(){
    	var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
    	window.parent.mainpage.mainTabs.addModule('打印垛卡','${ctx}/bis/asn/printCard/'+row.asn);
    }

    //生成预约入库信息
    function createReservation() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        if (3 == row.asnState || 4 == row.asnState) {
            parent.$.easyui.messager.alert("ASN号：" + row.asn + " 未上架或未完成，不允许生成！");
        } else {
            parent.$.messager.prompt('提示', '请输入预约入库日期(格式为：20260101)。', function(content){
                if (content){
                    var params = "";
                    params = row.asn +"-"+ content;
                    $.ajax({
                        type: 'post',
                        url: "${ctx}/platform/reservationData/inbound/" + params,
                        success: function (data) {
                            if(data=="success"){
                                successTip(data, dg);
                            }else if(data=="warn"){
                                parent.$.messager.show({ title : "提示",msg: "当前ASN信息已生成预约信息，请删除后再生成！", position: "bottomRight" });
                                return;
                            }else{
                                parent.$.messager.show({ title : "提示",msg: "生成预约入库信息失败！", position: "bottomRight" });
                                return;
                            }
                        }
                    });
                }else{
                    parent.$.messager.show({ title : "提示",msg: "请输入预约入库日期！", position: "bottomRight" });
                    return;
                }
            });
        }
    }
</script>
</body>
</html>