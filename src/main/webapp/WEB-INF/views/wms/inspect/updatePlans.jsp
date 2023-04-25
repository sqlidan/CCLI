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
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true"
           data-options="disabled:false" onclick="submitForm()">保存</a>
    </div>

    <form id="mainForm" action="" method="post">

       <%--  <input type="hidden" id="inspectId" name="inspectId" value="${inspectId}"> --%>

        <table>
            <tr>
               <!--  <td>托盘号</td>
                <td><input type="text" name="trayId" id="trayId" class="easyui-validatebox"
                           data-options="width:200,prompt: '托盘号'" readOnly="true"/>

                <td>提单号</td>
                <td><input type="text" name="billNum" id="billNum" class="easyui-validatebox"
                           data-options="width:200,prompt: '提单号'" readOnly="true"/>
                </td>
            </tr>
            <tr>
                <td>箱号</td>
                <td><input type="text" name="ctnNum" id="ctnNum" class="easyui-validatebox"
                           data-options="width:200,prompt: '箱号'" readOnly="true"/>
                </td>
                <td>SKU</td>
                <td><input type="text" name="skuId" id="skuId" class="easyui-validatebox"
                           data-options="width:200,prompt: 'SKU'" readOnly="true"/>
                </td> -->
                <td>取样单号</td>
                <td><input type="text" name="inspectId" id="inspectId" class="easyui-validatebox"
                           data-options="width:200,prompt: '取样单号'"value="${inspectId}" readonly/>
                </td>
            </tr>
            <!-- <tr>
                <td>联系单号</td>
                <td><input type="text" name="contactNum" id="contactNum" class="easyui-validatebox"
                           data-options="width:200,prompt: '联系单号'" readOnly="true"/></td>
                <td>ASN</td>
                <td><input type="text" name="asn" id="asn" class="easyui-validatebox"
                           data-options="width:200,prompt: 'ASN'" readOnly="true"/>
                </td>
            </tr> -->

            <tr>
                <td>客户名称</td>
                <td>
                	<input id="stockName" name="stockName" class="easyui-combobox"
                    data-options="width:200,prompt: '客户名称'"></input>
                </td>
                <!-- <td><input type="text" name="stockName" id="stockName" class="easyui-validatebox"
                           data-options="width:200,prompt: '客户名称'" "/></td> -->
                <!-- <td>仓库名</td>
                <td><input type="text" name="warehouse" id="warehouse" class="easyui-validatebox"
                           data-options="width:200,prompt: '仓库名'" readOnly="true"/>
                </td> -->
            </tr>
            <!-- <tr>
                </td>
                <td>产品名</td>
                <td><input type="text" name="cargoName" id="cargoName" class="easyui-validatebox"
                           data-options="width:200,prompt: '产品名'" readOnly="true"/></td>
            </tr> -->
            <tr>
                <!-- <td>件数</td>
                <td><input type="text" name="inspectTotal" id="inspectTotal" class="easyui-validatebox"
                           data-options="width:200,prompt: '件数'"/>
                </td> -->
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
<div style="width:700px;" data-options="region:'east',title:'取样明细'">
	<table id="tb_inspectinfo"></table>
	   <div id="tb1">
	   
	   <shiro:hasPermission name="wms:addPlans:check">
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
	           onclick="auditPlan()">审核</a>
	   </shiro:hasPermission>
	   
	   <shiro:hasPermission name="wms:addPlans:uncheck">
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
	           onclick="onCancelAudit()">取消审核</a>  
	   </shiro:hasPermission>
	   
	   <shiro:hasPermission name="wms:addPlans:delete">
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
	           onclick="onDelete()">删除</a>   
	   </shiro:hasPermission>
    </div>
</div>
<div id="dlg_inspectTotal" ></div>
<div id="dlg_check" ></div>
<div data-options="region:'south',split:true,border:false" title="库存总览" style="height:300px">
    <div id="tb">
        <form id="searchFrom" action="">
            <input type="text" name="trayCode" class="easyui-validatebox" data-options="width:120,prompt: '托盘号'"/>
            <input type="text" name="billCode" class="easyui-validatebox" data-options="width:120,prompt: '提单号'"/>
            <input type="text" name="ctnNum" class="easyui-validatebox" data-options="width:120,prompt: '箱号'"/>
            <input type="text" name="asn" class="easyui-validatebox" data-options="width:120,prompt: 'ASN'"/>
            <input type="text" name="sku" class="easyui-validatebox" data-options="width:120,prompt: 'SKU'"/>
            <input type="text" name="contactCode" class="easyui-validatebox" data-options="width:120,prompt: '联系单号'"/>
            <select id="clientName" name="clientName" class="easyui-combobox" data-options="width:120,prompt: '客户名称'">
            </select>
            <select id="warehouseId" name="warehouseId" class="easyui-combobox" data-options="width:120,prompt: '仓库'">
            </select>

            <span class="toolbar-item dialog-tool-separator"></span>

            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true"
               onclick="searchStock()">查询</a>

            <span class="toolbar-item dialog-tool-separator"></span>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
               onclick="selectStock()">确定</a>

        </form>
    </div>

    <table id="dg"></table>

</div>

<script type="text/javascript">

    var dg;
	var inspectinfo;
	var dlg_inspectTotal;
	var dlg_check;
	var inspectID = document.getElementById("inspectId").value;
    $(function () {
        initData();
        refreshDataGrid();
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
    	
    	$("#stockName").combobox({

        	onChange: function (n,o) {
        		var stockName = n;
        		$('#clientName').combobox('setValue',stockName);
    			searchStock();
        	}

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

    function refreshDataGrid() {

        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/report/stock/json',
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
                {field: 'trayCode', title: '托盘号', sortable: true, width: 100},
                {field: 'billCode', title: '提单号', sortable: true, width: 130},
                {field: 'ctnNum', title: '箱号', sortable: true, width: 130},
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
                {
                    field: 'units', title: '单位', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        if (value == '1') {
                            return '千克';
                        }
                        if (value == '2') {
                            return '吨';
                        }
                    }
                },
                {
                    field: 'state', title: '状态', sortable: true, width: 100,
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
                {field: 'enterPerson', title: '入库理货员', sortable: true, width: 100},
                {field: 'enterOp', title: '入库操作员', sortable: true, width: 100}
            ]],
            onLoadSuccess: function (data) {

            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
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
            	refreshMasterLineState();
            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb1'
        });
    }
    
	//刷新主表显示状态的方法
	
    function refreshMasterLineState(){
    	var flag2 = 1;
    	var rows = $("#tb_inspectinfo").datagrid("getRows");
    	
    	if(rows.length > 0){
    		for(var i=0;i<rows.length;i++){
    			if(rows[i].operateState == 0){
    				flag2 = 0;
    			}
        	}
        	if(flag2 == 1){
        		$("#operateState").val("已审核");
        	}else{
        		$("#operateState").val("未审核");
        	}
    	}else if($("#operateState").val()=="已审核"){
    		$("#operateState").val("未审核");
    	}
    }
    //创建查询对象并查询
    function searchStock() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
    }
    var stockNameFlag = document.getElementById("stockName").value;
    function selectStock() {
    	
    	
        var row = dg.datagrid('getSelected');
		
        if (rowIsNull(row)) return;
		//需要验证客户名称是否是相同的
      
		//判断当前取样单的状态
		if ($("#operateState").val() == "未审核") {
			
			dlg_inspectTotal = $("#dlg_inspectTotal").dialog({
	            title: '取样件数',
	            width: 300,
	            height: 400,
	            href: '${ctx}/wms/inspect/plans/addInspectNumInfo',
	            maximizable: true,
	            modal: true,
	            buttons: [{
	                text: '确认',
	                handler: function () {
						
	                    if ($("#mainform").form('validate')) {
							//正常情况
							
							var billNum = row.billCode;
							var ctnNum = row.ctnNum;
							var skuId = row.sku;
							var stockName = row.clientName;
							var warehouse = row.warehouse;
							var cargoName = row.cargoName;
				        	var contactNum = row.contactCode;
				        	var asn = row.asn;
				        	var cargoLocation = row.locationCode;
				        	var inspectMasterId = $("#inspectId").val();
				        	var inspectNum = $("#mainform_num").serializeArray()[0].value;
				        	var sampleUnit = $("#mainform_num").serializeArray()[1].value;
				        	
				        	/* if(!stockNameFlag == "" && stockNameFlag != stockName){
								
				        		
				        		toast("取样客户名称不一致");
				        		return;
				        	} */
				        	if(row.nowNum < inspectNum){
				        		toast("取样数量大于现有库存");
				        		
				        		return;
				        	}
				        	
				        	$.ajax({
				                async: false,
				                type: "POST",
				                url: "${ctx}/wms/inspect/plans/addInspectInfo",
				                data: {"billNum":billNum,"ctnNum":ctnNum,"skuId":skuId,"stockName":stockName,
				                	"warehouse":warehouse,"cargoName":cargoName,"contactNum":contactNum,"asn":asn,
				                	"inspectMasterId":inspectMasterId,"inspectNum":inspectNum,"cargoLocation":cargoLocation,
				                	"sampleUnit":sampleUnit
				                },
				                dataType: "text",
				                success: function (date) {
				                		
				                		toast("保存成功!");
				                		dlg_inspectTotal.panel('close');
				                		stockNameFlag = stockName;
				                		//$("#stockName").val(stockNameFlag);
				                		$('#stockName').combobox('setValue',stockNameFlag);  
				                		refreshDataGridInspectInfo();
				                	
				                },
				                error:function(){
				                	
				                }
				            });
	                        

	                    }
	                }
	            }, {
	                text: '取消',
	                handler: function () {
	                	dlg_inspectTotal.panel('close');
	                }
	            }]
	        });
			
			
			
		
        	
		}else if($("#operateState").val() == "已审核"){
			toast("已审核不能继续添加样品");
			return;
		}else if($("#operateState").val() == "未保存"){
			toast("未保存不能添加样品");
			return;
		}

    
    	
    	

       /*  var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        // $("#trayId").val(row.trayCode);
        $("#warehouse").val(row.warehouse);
        $("#cargoName").val(row.cargoName);
        $("#contactNum").val(row.contactCode);
        $("#asn").val(row.asn);
        $("#billNum").val(row.billCode);
        $("#ctnNum").val(row.ctnNum);
        $("#skuId").val(row.sku);
        $("#stockName").val(row.clientName); */

    }

    function submitForm() {

        if ($("#mainForm").form('validate')) {

            var description = $('#description').val();

            if (description == "" || description == null) {

                parent.$.messager.confirm('提示', '备注信息为空，您确认不填写此项吗？', function (data) {
                    if (data) submitMainForm();
                });

            } else {
                submitMainForm();
            }
        }

    }

    function submitMainForm() {
        $.ajax({
            async: false,
            type: 'POST',
            url: "${ctx}/wms/inspect/plans/updateMasterLine",//替换了‘plans/update’
            data: $('#mainForm').serialize(),
            dataType: "text",
            success: function (msg) {

                if (msg == "success") {
                    toast("保存成功!");
                } else {
                    toast("保存失败!");
                }

            }
        });

    }

    function toast(msg) {
        parent.$.messager.show({title: "提示", msg: msg, position: "bottomRight"});
    }
    //审核的方法
    function auditPlan() {

        var row = inspectinfo.datagrid('getSelected');

        if (rowIsNull(row)) return;

        if (row.operateState > 0) {
            toast("取样计划已审核!");
            return;
        }
		
        dlg_check = $("#dlg_check").dialog({
            title: '审核',
            width: 300,
            height: 400,
            href: '${ctx}/wms/inspect/plans/auditInfo?inspectId=' + row.inspectId,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {

                    if ($("#mainform11").form('validate')) {

                        $.ajax({
                            async: false,
                            type: 'POST',
                            url: "${ctx}/wms/inspect/plans/audit",
                            data: $("#mainform11").serializeArray(),
                            dataType: "text",
                            success: function (msg) {

                                if (msg == "success") {
                                    toast("审核成功!");
                                    dlg_check.panel('close');
                                    refreshDataGridInspectInfo();
                                } else {
                                    toast("审核失败!");
                                }

                            }
                        });

                    }
                }
            }, {
                text: '取消',
                handler: function () {
                	dlg_check.panel('close');
                }
            }]
        });

    }
    
    function onCancelAudit() {

        var row = inspectinfo.datagrid('getSelected');

        if (rowIsNull(row)) return;

        if (row.operateState == 0) {
            toast("取样计划未审核!");
            return;
        }

        parent.$.messager.confirm('提示', '确定取消审核？', function (data) {

            if (data) {

                var url = "${ctx}/wms/inspect/plans/audit/cancel?inspectId=" + row.inspectId;

                $.get(url, function (data) {

                    if (data == "success") {
                        toast("取消成功");
                        refreshDataGridInspectInfo();
                    } else {
                        toast("取消失败");
                    }

                });

            }

        });

    }
    //删除取样明细
    function onDelete() {

        parent.$.messager.confirm('提示', '确定删除该条取样明细？', function (data) {

            if (data) {

                var row = inspectinfo.datagrid('getSelected');

                if (rowIsNull(row)) return;

                var url = "${ctx}/wms/inspect/plans/delete?inspectId=" + row.inspectId;

                $.get(url, function (data) {

                    if (data == "success") {
                        toast("删除成功");
                        refreshDataGridInspectInfo();
                    } else {
                        toast("删除失败");
                    }

                });

            }

        });

    }
</script>
</body>
</html>