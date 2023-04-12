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
            <select class="easyui-combobox" id="asnState" name="asnState"
                    data-options="width:100,prompt:'ASN状态'">
                <option value=""></option>
            </select>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx0()">查询</a>
        </form>
        
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
               onclick="assign()">指派库管员</a>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
               onclick="selectRule()">维护计件规则</a>
            <span class="toolbar-item dialog-tool-separator"></span>
    </div>

</div>
<table id="dg"></table>
<div id="dlg"></div>
<div id="ruledlg"></div>

<script type="text/javascript">
    var dg;
    var dlg;
    var d;
	var drule;
//    document.onkeydown = function () {
//        if (event.keyCode == 13) {
//            cx();
//        }
//    };


    $(document).ready(function () {

        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/cost/piecework/asnList',
            fit: true,
            fitColumns: false,
            border: false,
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
            	{field: 'workMan', title: '库管人员', sortable: false, width: 80 , hidden:true},
            	{field: 'lhPerson', title: '分配理货人员', sortable: false, width: 80 , hidden:true},
            	{field: 'ccPerson', title: '分配叉车人员', sortable: false, width: 80 , hidden:true},
            	{field: 'ccPerson2', title: '分配叉车人员', sortable: false, width: 80 , hidden:true},
            	{field: 'remindId', title: '任务提醒ID', hidden:true},
                {field: 'asn', title: 'ASN号', sortable: false, width: 100},
                {   field: 'asnState', title: 'ASN状态', sortable: false, formatter: function (value, row, index) {
                    var asnStr = "";
                    switch (value) {
                        case '1':
                            asnStr = "在途";
                            break;
                        case '2':
                            asnStr = "收货中";
                            break;
                        case '3':
                            asnStr = "已上架";
                            break;
                        case '4':
                        	asnStr = "已完成";
                        	break;
                    }
                    return asnStr;
                } 
                	
                },
                {field: 'ruleJobType', title: '作业类型', sortable: false, width: 80},
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
                }
                },
                {field: 'inboundTime', title: '入库时间', sortable: false, width: 100},
                {field: 'tallyUser', title: '理货人员', sortable: false, width: 60},
                {
                    field: 'isBonded', title: '是否保税', sortable: false, formatter: function (value, row, index) {
                    return value == 0 ? '否' : '是';
                }
                },
                {field: 'punm', title: '入库数量', sortable: false, width: 100},
                {field: 'net', title: '收货净重', sortable: false, width: 100,
                	formatter:function(val,rowData,rowIndex){
                    	if(val!=null)
                        	return val.toFixed(2);
               		}	
                },
                {field: 'gross', title: '收货毛重', sortable: false, width: 100,
                	formatter:function(val,rowData,rowIndex){
                    	if(val!=null)
                        	return val.toFixed(2);
               		}	
                },
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
                if (date != null && date.rows != null && date.rows.length > 0) {
                    $('#ifSecondEnter').combobox({
                        data: date.rows,
                        valueField: 'value',
                        textField: 'label'
                    });
                }
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
                        textField: 'label',
                        onLoadSuccess: function (data1) {
                            if (data1) {
                                $('#asnState').combobox('setValue',data1[0].value);
                            }
                        }
                    });
                }
            }
        });
        //客户
        $('#stockIn').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote'
        });
    });

	//指派库管员
	function assign(){
		var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        d = $("#dlg").dialog({
            title: '入库指派库管作业人员',
            width: 600,
            height: 450,
            href: '${ctx}/cost/piecework/assignManNotToASN/' + row.asn + '/' + '1',
            maximizable: true,
            modal: true,
            /* buttons: [{
                text: '确认',
                handler: function () {
                        $("#mainform2").submit();// TODO 
                        d.panel('close');
                    }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }], */
            onClose: function () {
                window.setTimeout(function () {
                     cx0()
                }, 500);
            }
        });
	}
    
	//指派库管员 
	/* 20170929弃用 库管工作人员不再存入ASN表，
	人员类型和库管人员存入到两张新表中
	BASE_PERSONTYPE_RULE，BIS_ASN_WORKER */
	
/* 	function assign(){
		var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        d = $("#dlg").dialog({
            title: '入库指派库管作业人员',
            width: 380,
            height: 380,
            href: '${ctx}/cost/piecework/assignMan/' + row.asn + '/' + '1',
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                        $("#mainform2").submit();
                        d.panel('close');
                    }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }],
            onClose: function () {
                window.setTimeout(function () {
                     cx0()
                }, 500);
            }
        });
	} */

	//维护计件规则
	function selectRule(){
		var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        d = $("#dlg").dialog({
            title: '维护计件规则',
            width: 380,
            height: 380,
            href: '${ctx}/cost/piecework/selectRule/' + row.asn + '/' + '1',
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                        $("#mainform2").submit();
                        d.panel('close');
                    }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }],
            onClose: function () {
                window.setTimeout(function () {
                     cx0()
                }, 500);
            }
        });
	}


    //查询
    function cx0() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
    }
  
</script>
</body>
</html>