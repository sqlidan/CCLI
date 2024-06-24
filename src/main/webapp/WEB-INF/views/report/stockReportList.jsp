<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
    <script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
    <div>
        <form id="searchFrom" action="">
            <input type="text" name="trayCode" class="easyui-validatebox" data-options="width:120,prompt: '托盘号'"/>
            <input type="text" name="billCode" class="easyui-validatebox" data-options="width:120,prompt: '提单号'"/>
            <input type="text" name="ctnNum" class="easyui-validatebox" data-options="width:120,prompt: '箱号'"/>
            <input type="text" name="asn" class="easyui-validatebox" data-options="width:120,prompt: 'ASN'"/>
            <input type="text" name="sku" class="easyui-validatebox" data-options="width:120,prompt: 'SKU'"/>
            <input type="text" name="contactCode" class="easyui-validatebox" data-options="width:120,prompt: '联系单号'"/>
            <select id="clientName" name="clientName" class="easyui-combobox" data-options="width:120,prompt: '客户名称'">
            </select>
            <select id="state" name="state" class="easyui-combobox" data-options="width:120,prompt: '库存状态'">
            	<option></option>
            	<option value='00'>已收货</option>
            	<option value='01'>已上架</option>
            	<option value='10'>出库中</option>
            	<option value='11'>出库理货</option>
            	<option value='12'>已出库</option>
            	<option value='20'>待回库</option>
            	<option value='21'>回库收货</option>
            	<option value='99'>报损</option>
            </select>
            <select id="warehouseId" name="warehouseId" class="easyui-combobox" data-options="width:120,prompt: '仓库'">
            </select>
            <input type="text" name="strartTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"    data-options="width:150,prompt: '报关申报开始时间'"/>
	        <input type="text" name="endTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"    data-options="width:150,prompt: '报关申报结束时间'"/>
            <input type="text" name="createUser" class="easyui-validatebox" data-options="width:120,prompt: '客服员'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        </form>
    </div>
    <div>
        <shiro:hasPermission name="bis:tray:updateTime">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
               onclick="changeTheDate()">修改入库日期</a>
            <input id="theDate" name="theDatee" class="easyui-my97" data-options="width:200,prompt: '修改入库时间'"
                   datefmt="yyyy-MM-dd HH:mm:ss"/>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:tray:updatePiece">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
               onclick="changeThePiece()">修改上架件数</a>
            <input id="thePiece" name="thePiece" class="easyui-validatebox"
                   data-options="width:200,prompt: '修改上架件数(请输入数字)'" onkeyup="this.value=this.value.replace(/\D/g,'')"
                   onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
            <shiro:hasPermission name="bis:trayinfo:update">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="change()">是否保税转换</a>
            <span class="toolbar-item dialog-tool-separator"></span>
    </shiro:hasPermission>
    </div>
    <div>
        现有数量合计：<input type="text" id="allpiece" readonly/>
        净重合计：<input type="text" id="allnet" readonly/>
        毛重合计：<input type="text" id="allgross" readonly/>
    </div>

</div>
<table id="dg"></table>
<div id="dlg"></div>
<div id="clg"></div>
<script type="text/javascript">
    var dg;
    var d;
    var cd;

    document.onkeydown = function () {
        if (event.keyCode == 13) {
            cx();
        }
    };

    //客户
    $('#clientName').combobox({
        method: "GET",
        url: "${ctx}/base/client/getClientAll",
        valueField: 'clientName',
        textField: 'clientName',
        mode: 'remote'
    });

    //仓库
    $.ajax({
        type: "GET",
        async: false,
        url: "${ctx}/base/warehouse/getWarehouse",
        data: "",
        dataType: "json",
        success: function (date) {
            for (var i = 0; i < date.length; i++) {
                $('#warehouseId').combobox({
                    data: date,
                    valueField: 'id',
                    textField: 'warehouseName',
                    editable: false
                });
            }
        }
    });

    $(function () {
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/report/stock/json',
            fit: true,
            fitColumns: false,
            border: false,
            //idField : 'trayCode',
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: false,
            columns: [[
                {field: 'trayCode', title: '托盘号', sortable: true, width: 100},
                {field: 'billCode', title: '提单号', sortable: true, width: 130},
                {field: 'isBonded', title: '是否一般贸易', sortable: true, width: 70,
                	formatter:function(val,rowData,rowIndex){
                        if(val=="0")
                            return "是";
                        else
                            return "";
                   }
               },
                {field: 'ctnNum', title: '箱号', sortable: true, width: 130},
                {field: 'bgdhdate', title: '报关单申报时间', sortable: true, width: 130},
                {field: 'createUser', title: '客服员', sortable: true, width: 130},
                {field: 'asn', title: 'ASN', sortable: true, width: 90},
                {field: 'sku', title: 'SKU', sortable: true, width: 120},
                {field: 'contactCode', title: '联系单号', sortable: true, width: 135},
                {field: 'clientName', title: '客户名称', sortable: true, width: 150},
                {field: 'warehouse', title: '仓库名', sortable: true, width: 80},
                {field: 'locationCode', title: '库位号', sortable: true, width: 80},
                {field: 'cargoName', title: '产品名称', sortable: true, width: 100},
                {field: 'nowNum', title: '现有数量', sortable: true, width: 100},
                {field: 'netWeight', title: '净重', sortable: true, width: 100},
                {field: 'grossWeight', title: '毛重', sortable: true, width: 100},
                {field: 'allpiece', title: '总数量', hidden: true},
                {field: 'allnet', title: '总净重', hidden: true},
                {field: 'allgross', title: '总毛重', hidden: true},
                {field: 'units', title: '单位', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        if (value == '1') {
                            return '千克';
                        }
                        if (value == '2') {
                            return '吨';
                        }
                    }
                },
                {field: 'state', title: '状态', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        if (value == '00') {
                            return '已收货';
                        }
                        if (value == '01') {
                            return '已上架';
                        }
                        if (value == '10') {
                            return '出库中';
                        }
                        if (value == '11') {
                            return '出库理货';
                        }
                        if (value == '12') {
                            return '已出库';
                        }
                        if (value == '20') {
                            return '待回库';
                        }
                        if (value == '21') {
                            return '回库收货';
                        }
                        if (value == '99') {
                            return '货损';
                        }
                    }
                },
                {field: 'enterTime', title: '入库理货时间', sortable: true, width: 130},
                {field: 'inTime', title: '入库确认时间', sortable: true, width: 130},
                {field: 'uploader', title: '转一般贸易操作人', sortable: true, width: 130},
                {field: 'uploadDate', title: '转一般贸易时间', sortable: true, width: 130},
                {field: 'enterPerson', title: '入库理货员', sortable: true, width: 100},
                {field: 'enterOp', title: '入库操作员', sortable: true, width: 100},
                {field: 'days', title: '月数差', sortable: true, width: 100}
            ]],
            rowStyler: function (rowIndex, rowData) {
                //变成红色
                if (rowData.days>=10) {
                	return 'background-color:red;';
                }
            },
            onLoadSuccess: function (data) {
                if (data.total != 0) {
                    var row = dg.datagrid('getData').rows[0];
                    $("#allpiece").val(row.allpiece);
                    $("#allnet").val(row.allnet);
                    $("#allgross").val(row.allgross);
                }
            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });
    //保税、一般贸易转换  liuyu
    function change() {
    	var rows = dg.datagrid('getSelections');
		var ids=new Array();
		for(var i=0;i<rows.length;i++){
			var row=rows[i];
			if(row.isBonded=="0"){
				 parent.$.messager.show({title: "提示", msg: "请选择不是一般贸易货物", position: "bottomRight"});
				 return;
			}
			ids.push(row.trayCode);
		}
    	var url='${ctx}/bis/trayinfo/update?trayCode='+ids.toString();
    	//var url='${ctx}/bis/trayinfo/update
        if (rowIsNull(row)) return;
        d = $("#dlg").dialog({
            title: '是否保税转换',
            width: 380,
            height: 380,
            href: url,
          //  data:{ids:ids.toString()},
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    if ($("#mainform").form('validate')) {
                        $("#mainform").submit();
                        d.panel('close');
                    }
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }],
            onClose: function () {
                window.setTimeout(function () {
                   // gridDG()
                }, 100);
            }
        });
    }
    //修改入库时间
    function changeTheDate() {
        var row = dg.datagrid('getSelected');
        var rowIndex = $('#dg').datagrid('getRowIndex', row)
        if (rowIsNull(row)) return;
        if (row.state != '01') {
            parent.$.messager.show({title: "提示", msg: "此托盘不处于已上架状态，无法修改入库日期！", position: "bottomRight"});
            return;
        }
        var theDate = $("#theDate").datebox("getValue");
        if (theDate == "" || theDate == null) {
            parent.$.messager.show({title: "提示", msg: "请填写需要修改的入库时间！", position: "bottomRight"});
            return;
        }
        $.ajax({
            type: 'POST',
            url: "${ctx}/bis/trayinfo/changeInbound/" + row.trayCode + "/" + theDate,
            dataType: "text",
            success: function (msg) {
                if (msg == "reset") {
                    parent.$.messager.show({title: "提示", msg: "修改失败！请重试", position: "bottomRight"});
                    return;
                } else if (msg == "success") {
                    parent.$.messager.show({title: "提示", msg: "修改成功！", position: "bottomRight"});
                    $('#dg').datagrid('updateRow', {
                        index: rowIndex,
                        row: {
                            inTime: theDate,
                        }
                    });
                }
            }
        });
    }




    //修改上架件数
    function changeThePiece() {
        var row = dg.datagrid('getSelected');
        var rowIndex = $('#dg').datagrid('getRowIndex', row);
        var thePiece = $('#thePiece').val();
        if (thePiece == "" || thePiece == null) {
            parent.$.messager.show({title: "提示", msg: "请填写需要修改的上架件数！", position: "bottomRight"});
            return;
        }
        if (rowIsNull(row)) return;
        if (row.state != '01') {
            parent.$.messager.show({title: "提示", msg: "此托盘不处于已上架状态，无法修改上架件数！", position: "bottomRight"});
            return;
        }
        $.ajax({
            type: 'POST',
            url: "${ctx}/bis/trayinfo/examOutStock",
            data: {"sku": row.sku, "billNum": row.billCode, "ctnNum": row.ctnNum, "trayId": row.trayCode},
            dataType: "text",
            success: function (msg) {
                if (msg == "out") {
                    parent.$.messager.show({title: "提示", msg: "此库存已在出库联系单中使用，无法进行修改！", position: "bottomRight"});
                    return;
                } else if (msg == "transfer") {
                    parent.$.messager.show({title: "提示", msg: "此库存已在货转联系单中使用，无法进行修改！", position: "bottomRight"});
                    return;
                } else if (msg == "huozhuan") {
                    parent.$.messager.show({title: "提示", msg: "此托盘已拆托，无法进行修改！", position: "bottomRight"});
                    return;
                } else if (msg == "pledge") {
                    parent.$.messager.show({title: "提示", msg: "此客户下有此SKU的质押记录，无法进行件数修改！", position: "bottomRight"});
                    return;
                } else {
                    changePiece(row, rowIndex, thePiece);
                }
            }
        });
    }


    function changePiece(row, rowIndex, thePiece) {
        $.ajax({
            type: 'POST',
            url: "${ctx}/bis/trayinfo/changePiece/" + row.trayCode + "/" + thePiece,
            data: {"sku": row.sku, "ctnNum": row.ctnNum},
            dataType: "text",
            success: function (msg) {
                if (msg == "reset") {
                    parent.$.messager.show({title: "提示", msg: "修改失败！请重试", position: "bottomRight"});
                    return;
                } else if (msg == "success") {
                    parent.$.messager.show({title: "提示", msg: "修改成功！", position: "bottomRight"});
                    $('#dg').datagrid('updateRow', {
                        index: rowIndex,
                        row: {
                            nowNum: thePiece
                        }
                    });
                }
            }
        });
    }


    //创建查询对象并查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
    }

</script>
</body>
</html>