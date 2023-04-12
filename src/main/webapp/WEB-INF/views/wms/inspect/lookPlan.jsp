<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">

<div style="width:48%" data-options="region:'center'">

    <div style="height:auto" class="datagrid-toolbar">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="" plain="true"
           data-options="disabled:false" onclick=""></a>
    </div>

    <form id="mainForm" action="" method="post">

       <%--  <input type="hidden" id="inspectId" name="inspectId" value="${inspectId}"> --%>

        <table>
            <tr>
                <td>取样单号</td>
                <td><input type="text" name="inspectId" id="inspectId" class="easyui-validatebox"
                           data-options="width:200,prompt: '取样单号'"value="${inspectId}" readonly/>
                </td>
            </tr>
            <tr>
                <td>客户名称</td>
                <td>
                	<input id="stockName" name="stockName" class="easyui-combobox"
                    data-options="width:200,prompt: '客户名称'" readonly></input>
                </td>
            </tr>
            <tr>
                 <td>状态</td>
                <td><input type="text" name="operateState" id="operateState" class="easyui-validatebox"
                           data-options="width:200,prompt: '状态'" value="" readonly/>
                </td>
            </tr>
            <tr>
                <td>备注</td>
            </tr>
            <tr>
                <td></td>
                <td colspan="3">
                    <textarea class="easyui-validatebox" data-options="" name="description" id="description"
                              cols="55" rows="6" style="font-size: 12px;font-family: '微软雅黑'"></textarea>
                </td>

            </tr>

        </table>
    </form>
</div>
<div style="width:900px;" data-options="region:'east',title:'取样明细'">
	<table id="tb_inspectinfo"></table>
	   <div id="tb1"></div>
</div>
<div id="dlg_inspectTotal" ></div>
<div id="dlg_check" ></div>
<script type="text/javascript">

    var dg;
	var inspectinfo;
	var dlg_inspectTotal;
	var dlg_check;
	var inspectID = document.getElementById("inspectId").value;
    $(function () {
        initData();
        //refreshDataGrid();
        refreshDataGridInspectInfo();
        
    	$('#stockName').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'clientName',
            textField: 'clientName',
            mode: 'remote'
        });
    	$('#clientName').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'clientName',
            textField: 'clientName',
            mode: 'remote'
        });
    });

    function initData() {

        var url = "${ctx}/wms/inspect/plan/getIMaster?inspectId=${inspectId}";

        $.get(url, function (result) {

//            var result = JSON.parse(data);

            console.log(result);

            $("#inspectId").val(result.inspectId);
            //$("#stockName").val(result.stockName);
            $('#stockName').combobox('setValue',result.stockName);
            $("#operateState").val(result.operateState==0?"未审核":"已审核");
            $("#description").val(result.description);
        });

    }

    function refreshDataGridInspectInfo(){
    	var inspectMasterId = $("#inspectId").val();
    	
    	inspectinfo = $('#tb_inspectinfo').datagrid({
            method: "get",
            url: '${ctx}/wms/inspect/plans/getInspectInfo?inspectMasterId='+inspectMasterId,
            fit: true,
            fitColumns: true,
            border: false,
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
				{field: 'inspectId', title: '从表主键', sortable: true, hidden:true, width: 100},
                {field: 'trayId', title: '托盘号', sortable: true, width: 100},
                {field: 'billNum', title: '提单号', sortable: true, width: 130},
                {field: 'ctnNum', title: '箱号', sortable: true, width: 130},
                {field: 'asn', title: 'ASN', sortable: true, width: 90},
                {field: 'skuId', title: 'SKU', sortable: true, width: 120},
                /* {field: 'contactNum', title: '联系单号', sortable: true, width: 135}, */
                {field: 'cargoLocation', title: '库位号', sortable: true, width: 135},
                /* {field: 'clientName', title: '客户名称', sortable: true, width: 150}, */
                {field: 'warehouse', title: '仓库名', sortable: true, width: 80},
                /* {field: 'locationCode', title: '库位号', sortable: true, width: 80}, */
                {field: 'cargoName', title: '产品名称', sortable: true, width: 100},
                {
                    field: 'operateState', title: '状态', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        if (value == '0') {
                            return '未审核';
                        }
                        if (value == '1') {
                            return '已审核';
                        }
                    }
                },
                {field: 'inspectTotal',title: '件数',editor: 'text', sortable: true,width:60},
                {field: 'sampleUnit',title: '单位',sortable: true,width:60}
                
            ]],
            onLoadSuccess: function (data) {
            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb1'
        });
    }
    
</script>
</body>
</html>