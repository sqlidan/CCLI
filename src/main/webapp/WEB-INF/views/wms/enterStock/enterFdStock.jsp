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
        <form id="infoForm" method="post">
        </form>
        <form id="searchFrom" action="">
            <input type="text" id="searchItemNum" name="searchItemNum" class="easyui-validatebox"
                   data-options="width:100,prompt: '提单号'"/>

            <select id="searchStockIn" name="searchStockIn" class="easyui-combobox"
                    data-options="width:150,prompt: '存货方'">
            </select>

            <select id="ifBack" name="ifBack" class="easyui-combobox"
                    data-options="width:80,prompt: '是否倒箱'">
                <option></option>
                <option value='0'>否</option>
                <option value='1'>是</option>
            </select>

            <select id="ifBonded" name="ifBonded" class="easyui-combobox"
                    data-options="width:80,prompt: '是否保税'">
                <option></option>
                <option value='0'>否</option>
                <option value='1'>是</option>
            </select>

            <select id="ifToCustoms" name="ifToCustoms" class="easyui-combobox"
                    data-options="width:80,prompt: '是否报关'">
                <option></option>
                <option value='0'>否</option>
                <option value='1'>是</option>
            </select>

            <select id="ifToCiq" name="ifToCiq" class="easyui-combobox"
                    data-options="width:80,prompt: '是否报检'">
                <option></option>
                <option value='0'>否</option>
                <option value='1'>是</option>
            </select>

            <select id="ifSorting" name="ifSorting" class="easyui-combobox"
                    data-options="width:80,prompt: '是否分拣'">
                <option></option>
                <option value='0'>否</option>
                <option value='1'>是</option>
            </select>

            <select id="auditingState" name="auditingState" class="easyui-combobox"
                    data-options="width:100,prompt: '状态'">
                <option></option>
                <option value='0'>暂存</option>
                <option value='1'>已提交</option>
                <option value='2'>已审核</option>
            </select>

            <input type="text" id="searchLinkId" name="searchLinkId" class="easyui-validatebox"
                   data-options="width:100,prompt: '入库联系单号'"/>

            <input type="text" id="operator" name="operator" class="easyui-validatebox"
                   data-options="width:100,prompt: '操作人员'"/>
           
             <input type="text" id="searchCunNum" name="searchCunNum" class="easyui-validatebox"
                   data-options="width:100,prompt: '箱号'"/>
                   
            <input type="text" id="searchStrTime" name="searchStrTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                   data-options="width:150,prompt: '入库日期'"/>
                   
            - <input type="text" id="searchEndTime" name="searchEndTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                     data-options="width:150,prompt: '入库日期'"/>
                     
            <input type="text" id="searchDxStrTime" name="searchDxStrTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                   data-options="width:150,prompt: '倒箱日期'"/>
            - <input type="text" id="searchDxEndTime" name="searchDxEndTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                     data-options="width:150,prompt: '倒箱日期'"/>
                     
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        </form>
        <shiro:hasPermission name="wms:enterStock:add">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true"
               onclick="window.parent.mainpage.mainTabs.addModule('入库联系单管理','wms/enterStock/add')">添加</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:enterStock:update">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">修改</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:enterStock:update">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="copyIt()">复制</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:enterStock:delete">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
               data-options="disabled:false" onclick="del()">删除</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:enterStock:check">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
               onclick="check()">审核</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:enterStock:adjust">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-credit" plain="true"
               onclick="inStockAdjust()">费用</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:enterStock:kfadjust">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-credit" plain="true"
               onclick="inStockkfAdjust()">费用</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:enterStock:lookAsn">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-zoom  " plain="true"
               onclick="lookAsn()">查看ASN</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:enterStock:makeAsn">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true"
               onclick="makeAsn()">制作ASN</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:enterStock:print">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true"
               onclick="print()">打印入库联系单</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:enterStock:exportInfo">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"
               onclick="exportExcel()">入库联系单导出</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:enterStock:exportInfo">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"
               onclick="exportInfo()">入库联系单明细导出</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <%-- <shiro:hasPermission name="wms:enterStock:report">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true"
               onclick="report()">入库报告书</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission> --%>
    </div>
</div>

<table id="dg"></table>
<div id="dlg"></div>

<script type="text/javascript">
    var dg;
    var d;

    document.onkeydown = function () {
        if (event.keyCode == 13) {
            cx();
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
            url: '${ctx}/wms/enterStock/json',
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
                {field: 'linkId', title: '入库联系单号', sortable: true, width: 150},
                {field: 'itemNum', title: '提单号', sortable: true, width: 150},
                {field: 'vesselName', title: '箱号', sortable: true, width: 200},
                {field: 'stockId', title: '存货方ID', hidden: true},
                {field: 'stockIn', title: '存货方', sortable: true, width: 200},
                {field: 'feeId', title: '费用方案ID',hidden: true},
                {field: 'feePlan', title: '费用方案', sortable: true, width: 200},
                {field: 'planFeeState', title: '计划费用状态', sortable: true,
                    formatter: function (value, row, index) {
                        return value == "0" ? '未完成' : '已完成';
                    }
                },
	            {field: 'finishFeeState', title: '费用完成状态', sortable: true,
	                    formatter: function (value, row, index) {
	                        return value == "0" ? '未完成' : '已完成';
	                    }
	            },
                {field: 'etaWarehouse', title: '计划入库日期', sortable: true, width: 150},
                {field: 'backDate', title: '倒箱日期', sortable: true, width: 150},
                {field: 'rkTime', title: '入库日期', sortable: true, width: 150},
                {field: 'operator', title: '创建人', sortable: true, width: 80},
                {field: 'ifBonded', title: '是否保税', sortable: true,
                    formatter: function (value, row, index) {
                        return value == "0" ? '否' : '是';
                    }
                },
                {
                    field: 'ifSorting', title: '是否分拣', sortable: true,
                    formatter: function (value, row, index) {
                        return value == "0" ? '否' : '是';
                    }
                },
                {
                    field: 'ifToCustoms', title: '是否报关', sortable: true,
                    formatter: function (value, row, index) {
                        return value == "0" ? '否' : '是';
                    }
                },
                {field: 'customsCompany', title: '报关代理公司', hidden: true,sortable: true, width: 225},
                {
                    field: 'ifToCiq', title: '是否报检', sortable: true,
                    formatter: function (value, row, index) {
                        return value == "0" ? '否' : '是';
                    }
                },
                {field: 'ciqCompany', title: '报检代理公司', sortable: true,hidden: true, width: 225},
                {field: 'temperature', title: '储存温度', sortable: true, width: 100},
                {field: 'sortingSpecial', title: '特殊要求', sortable: true,hidden: true, width: 150},
                {field: 'warehouse', title: '入库仓库', sortable: true, width: 100},
                {field: 'ifWrap', title: '是否缠膜', sortable: true,
                    formatter: function (value, row, index) {
                        return value == "0" ? '否' : '是';
                    }
                },
                {field: 'ifBagging', title: '是否套袋', sortable: true,
                    formatter: function (value, row, index) {
                        return value == "0" ? '否' : '是';
                    }
                },
                {field: 'ifWithWooden', title: '是否带木托', sortable: true,
                    formatter: function (value, row, index) {
                        return value == "0" ? '否' : '是';
                    }
                },
                {field: 'ifMacAdmit', title: '是否MSC认证', sortable: true,hidden: true,
                    formatter: function (value, row, index) {
                        return value == "0" ? '否' : '是';
                    }
                },
                {field: 'operateTime', title: '创建时间', sortable: true, width: 150},
                {field: 'auditingState', title: '审核状态', sortable: true, width: 50,
                    formatter: function (value, row, index) {
                        if (value == "0") {
                            return '暂存';
                        }
                        else if (value == "1") {
                            return '待审核';
                        }
                        else {
                            return '已审核';
                        }
                    }
                },
                {field: 'ctnTypeSize', title: '箱型尺寸', sortable: true, width:60},
                {field: 'remark', title: '备注', sortable: true, width: 150}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });

    //删除
    function del() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        if (row.auditingState == 2) {
            parent.$.messager.show({title: "提示", msg: "此入库联系单已被审核，无法删除！", position: "bottomRight"});
            return;
        }
        parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
            if (data) {
                $.ajax({
                    type: 'get',
                    url: "${ctx}/wms/enterStock/deleteEnterStock/" + row.linkId,
                    success: function (data) {
                        successTip(data, dg);
                    },
                });
            }
        });
    }
    //创建查询对象并查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
    }

    //修改
    function update() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        if (row.contractState == 1) {
            parent.$.messager.show({title: "提示", msg: "该入库联系单已提交不允许修改！", position: "bottomRight"});
        } else {
            window.parent.mainpage.mainTabs.addModule('入库联系单修改', 'wms/enterStock/updateEnterStock/' + row.linkId);
        }
    }

    //修改
    function copyIt() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/enterStock/copyit/" + row.linkId,
            dataType: "text",
            success: function (data) {
                window.parent.mainpage.mainTabs.addModule('入库联系单修改', 'wms/enterStock/updateEnterStock/' + data);
            }
        });
    }


    //查看ASN
    function lookAsn() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        window.parent.mainpage.mainTabs.addModule('ASN总览', 'bis/asn/list2/' + row.linkId);
    }

    //制作ASN
    function makeAsn() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        if (row.auditingState != '2') {
            parent.$.messager.show({title: "提示", msg: "该入库联系单还未审核，无法制作ASN！", position: "bottomRight"});
            return;
        }
        var href = 'bis/asn/create/' + row.linkId;
        window.parent.mainpage.mainTabs.addModule('ASN管理', href, 'icon-hamburg-customers');
    }

    //打印
    function print() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        window.parent.mainpage.mainTabs.addModule('入库联系单打印', 'wms/enterStock/print/' + row.linkId);
    }

    function printInfo() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        window.parent.mainpage.mainTabs.addModule('入库联系单明细打印', 'wms/enterStockInfo/printInfo/' + row.linkId);
    }

    //审核
    function check() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        if (row.auditingState != "1") {
            parent.$.messager.show({title: "提示", msg: "只能审核审核状态为待审核的数据！", position: "bottomRight"});
            return;
        }
        //20180529 yhn 审核前追加验证，检验如果修改了入库联系单的结算单位，是否影响已经生成的ASN计费区间。是否要修改ASN计费区间表。
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/enterStock/checkPassEnterStock/" + row.linkId,
            success: function (data) {
                if(data=="success"){
                	$.ajax({
                        type: 'get',
                        url: "${ctx}/wms/enterStock/passEnterStock/" + row.linkId,
                        success: function (data) {
                            successTip(data, dg);
                            parent.$.messager.show({title: "提示", msg: "审核成功！", position: "bottomRight"});
                        }
                    });
                	
                }else{
                	//提示是否修改
                	parent.$.messager.confirm('提示', '审核后将按照新ASN计费客户结算费用,确认要继续？', function (data) {
                		$.ajax({
                            type: 'get',
                            url: "${ctx}/wms/enterStock/passEnterStockAndUpdateASNActions/" + row.linkId,
                            success: function (data) {
                                successTip(data, dg);
                                parent.$.messager.show({title: "提示", msg: "审核成功！", position: "bottomRight"});
                            }
                        });
                    });
                }
            }
        });
    }

    //计划费用完成
    function planOk() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        if (row.planFeeState == "1") {
            parent.$.messager.show({title: "提示", msg: "计划费用已在完成状态！", position: "bottomRight"});
            return;
        }
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/enterStock/planOk/" + row.linkId,
            success: function (data) {
                successTip(data, dg);
                parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
            }
        });
    }
    //计划费用取消
    function planNo() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        if (row.planFeeState == "0") {
            parent.$.messager.show({title: "提示", msg: "计划费用已在非完成状态！", position: "bottomRight"});
            return;
        }
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/enterStock/planNo/" + row.linkId,
            success: function (data) {
                successTip(data, dg);
                parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
            }
        });
    }
    //费用完成
    function finishOk() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        if (row.finishFeeState == "1") {
            parent.$.messager.show({title: "提示", msg: "费用已在完成状态！", position: "bottomRight"});
            return;
        }
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/enterStock/finishOk/" + row.linkId,
            success: function (data) {
                successTip(data, dg);
                parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
            }
        });
    }
    //费用取消
    function finishNo() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        if (row.finishFeeState == "0") {
            parent.$.messager.show({title: "提示", msg: "费用已在非完成状态！", position: "bottomRight"});
            return;
        }
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/enterStock/finishNo/" + row.linkId,
            success: function (data) {
                successTip(data, dg);
                parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
            }
        });
    }
    //客服费用审核调整计算模式处理
    function inStockkfAdjust(){
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        var linkId = row.linkId;
        var itemNum = row.itemNum;
        var stockId = row.stockId;
        var feeId=row.feeId;
        itemNum = itemNum.replace(/\//g, "(╯' - ')╯︵ ┻━┻");
		$.ajax({
			type:'POST',
			async:false,
			url:"${ctx}/cost/standingBook/saveCost",
			data:{
				linkId:linkId,
				feeId:feeId
			},
			success: function(data){
				if(data=="success"){
					window.parent.mainpage.mainTabs.addModule('费用', 'cost/standingBook/inKfList/' + linkId + "/" + itemNum + "/" + stockId+"/"+feeId);
				}else{
					parent.$.messager.show({ title : "提示",msg:data, position: "bottomRight" });
				}
			}
		});
    }
    //进入 入库费用调整
    function inStockAdjust() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        var linkId = row.linkId;
        var itemNum = row.itemNum;
        var stockId = row.stockId;
        var feeId=row.feeId;
        itemNum = itemNum.replace(/\//g, "(╯' - ')╯︵ ┻━┻");
        $.ajax({
			type:'POST',
			async:false,
			url:"${ctx}/cost/standingBook/saveCwCost",
			data:{
				linkId:linkId,
				feeId:feeId
			},
			success: function(data){
			  if(data=="success"){
					window.parent.mainpage.mainTabs.addModule('费用', 'cost/standingBook/inList/' + linkId + "/" + itemNum + "/" + stockId+"/"+feeId);
			  }else{
					parent.$.messager.show({ title : "提示",msg:data, position: "bottomRight" });
			   }
			}
	   });
    }
    //导出
    function exportExcel() {
        var url = "${ctx}/wms/enterStock/export";
        $("#searchFrom").attr("action", url).submit();
    }

    //导出入库库联系单明细
    function exportInfo() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        var url = "/ccli/wms/enterStockInfo/exportinfo/" + row.linkId;
        $("#infoForm").attr("action", url).submit();
    }

    //跳转入库报告书
    function report() {
        window.parent.mainpage.mainTabs.addModule('入库报告书', '/ccli/wms/enterStock/toreport');
    }

</script>
</body>
</html>