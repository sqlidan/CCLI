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
            <input type="text" id="loadingPlanNum" name="loadingPlanNum" class="easyui-validatebox"
                   data-options="width:150,prompt: '订单号'"/>
            <input type="text" id="loadingTruckNum" name="loadingTruckNum" class="easyui-validatebox"
                   data-options="width:150,prompt: '装车单号'"/>
            <input type="text" id="outLinkId" name="outLinkId" class="easyui-validatebox"
                   data-options="width:150,prompt: '出库联系单号'"/>
<!--        <input type="text" id="carNo" name="filter_LIKES_carNo" class="easyui-validatebox"
                   data-options="width:150,prompt: '车牌号'"/>
			<input type="text" id="sku" name="filter_LIKES_skuId" class="easyui-validatebox"
                   data-options="width:150,prompt: 'SKU'"/>
            <input type="text" id="operator" name="filter_LIKES_operator" class="easyui-validatebox"
                   data-options="width:150,prompt: '客服人员'"/>
            <select id="searchStock" name="filter_LIKES_stockId" class="easyui-combobox"
                    data-options="width:150,prompt: '存货方'">
            </select>
			<select id="loadingState" name="loadingState" class="easyui-combobox"
                    data-options="width:100,prompt: '状态'">
            </select>
-->			
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
<div>

</div>
<script type="text/javascript">
    var dg;
    var dlg;
    var d, dt;
    var drule;


    $(document).ready(function () {
        //客户
        $('#searchStock').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote'
        });
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/cost/piecework/loadingList',
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
            	{field: 'workMan', title: '库管人员', sortable: false, width: 80, hidden:true},
            	{field: 'lhPerson', title: '分配理货人员', sortable: false, width: 80,hidden:true},
            	{field: 'ccPerson', title: '分配叉车人员', sortable: false, width: 80,hidden:true},
            	{field: 'ccPerson2', title: '分配叉车人员', sortable: false, width: 80,hidden:true},
            	{field: 'loadingTruckNum', title: '装车单号', sortable: false, width: 80},
            	{field: 'loadingState', title: '状态', hidden: true ,sortable: false, formatter: function (value, row, index) {
                    var loadingStateStr = "";
                    switch (value) {
                        case '0':
                        	loadingStateStr = "已分配";
                            break;
                        case '1':
                        	loadingStateStr = "已拣货";
                            break;
                        case '2':
                        	loadingStateStr = "已装车";
                            break;
                        case '3':
                        	loadingStateStr = "已置换";
                        	break;
                        case '5':
                        	loadingStateStr = "回库理货";
                        	break;
                        case '6':
                        	loadingStateStr = "已回库";
                        	break;
                    }
                    return loadingStateStr;
                }
            		
            	},
                {field: 'ruleJobType', title: '作业类型', sortable: false, width: 180},
                {field: 'loadingPlanNum', title: '出库订单号', sortable: false, width: 150},
                {field: 'outLinkId', title: '出库联系单号', sortable: false, width: 180},
                {field: 'loadingTime', title: '出库时间', sortable: false, width: 180},
                {field: 'carNo', title: '车牌号', sortable: false, width: 100},
                {field: 'piece', title: '总件数', sortable: false, width: 100},
                {field: 'jpiece', title: '计件件数', sortable: false, width: 100},
                {field: 'netWeight', title: '计件净重', sortable: false, width: 100,
                	formatter:function(val,rowData,rowIndex){
                    	if(val!=null)
                        	return val.toFixed(2);
               		}	
                },
                {field: 'grossWeight', title: '计件毛重', sortable: false, width: 100,
                	formatter:function(val,rowData,rowIndex){
                    	if(val!=null)
                        	return val.toFixed(2);
               		}	
                }
               
                
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });

    //查询
    function cx0() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
    }

//指派库管员
	function assign(){
		var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        d = $("#dlg").dialog({
            title: '出库指派库管作业人员',
            width: 600,
            height: 450,
            href: '${ctx}/cost/piecework/assignManNotToASN/' + row.loadingPlanNum+","+row.loadingTruckNum + '/' + '2',
            maximizable: true,
            modal: true,
            /* queryParams:{"loadingTruckNum":row.loadingTruckNum }, */
           /*  buttons: [{
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
            }], */
            onClose: function () {
                window.setTimeout(function () {
                    cx0()
                }, 500);
            }
        });
	}

//维护计件规则
	function selectRule(){
		var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        d = $("#dlg").dialog({
            title: '维护计件规则',
            width: 380,
            height: 380,
            href: '${ctx}/cost/piecework/selectRule/' + row.loadingPlanNum + '/' + '2',
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


</script>
</body>
</html>