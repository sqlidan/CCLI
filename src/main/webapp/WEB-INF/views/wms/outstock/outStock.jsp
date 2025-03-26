<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body>
<div id="tb" style="padding: 5px; height: auto">
    <div>
        <form id="infoForm" method="post"></form>
        <form id="searchFrom" action="">

            <input type="text" name="filter_LIKES_outLinkId"
                   class="easyui-validatebox" data-options="width:150,prompt: '联系单ID'"/>

            <select id="searchStock" name="filter_LIKES_stockIn"
                    class="easyui-combobox" data-options="width:150,prompt: '存货方'">
            </select>

            <select id="searchReceiver" name="filter_LIKES_receiver"
                    class="easyui-combobox" data-options="width:150,prompt: '收货方'">
            </select>

            <input type="text" name="filter_INAS_outLinkId"
                   class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
            
            <input type="text" name="filter_INAS_ctnNum"
                   class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>

            <!-- <select id="statesearch" name="filter_EQS_auditingState"
                    class="easyui-combobox" data-options="width:100,prompt: '状态'">
                <option></option>
                <option value='0'>未审核</option>
                <option value='1'>已审核</option>
            </select> -->
            <select id="ifCodeCopy" name="filter_EQS_ifCodeCopy"
                    class="easyui-combobox" data-options="width:100,prompt: '是否抄码'">
                <option></option>
                <option value='1'>是</option>
                <option value='0'>否</option>
            </select>
             
                <select id="backD2" name="filter_NULLS_customsCompanyId"
					class="easyui-combobox" data-options="width:80,prompt: '是否报关'">
					<option></option>
					<option value='0'>否</option>
					<option value='1'>是</option>
				</select> 
				
				<select id="backD3" name="filter_NULLS_ciqCompanyId"
					class="easyui-combobox" data-options="width:80,prompt: '是否报检'">
					<option></option>
					<option value='0'>否</option>
					<option value='1'>是</option>
				</select>
            <input type="text" name="filter_LIKES_operator" class="easyui-validatebox"
                   data-options="width:150,prompt: '操作人员'"/>
            <input type="text" name="filter_INAS_outLinkIda" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                   data-options="width:150,prompt: '装车时间'"/> -
            <input type="text" name="filter_INAS_outLinkIdb" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                   data-options="width:150,prompt: '装车时间'"/>
            <input type="text" name="filter_EQI_outCustomsCount" class="easyui-validatebox" id="outCustomsCount"
                   onkeyup="ischeckNum(this)" data-options="width:150,prompt: '报关票数'"/>
            <input type="text" name="filter_EQI_outCiqCount" class="easyui-validatebox" id="outCiqCount"
                   onkeyup="ischeckNum(this)" data-options="width:150,prompt: '报检票数'"/>
            <input type="text" name="filter_LIKES_remark"
                   class="easyui-validatebox" data-options="width:200,prompt: '备注'"/>
            <span class="toolbar-item dialog-tool-separator">

        </span> <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true"
                   onclick="cx()">查询</a>
        </form>
        <shiro:hasPermission name="wms:outstock:add">
            <a href="javascript:void(0)" class="easyui-linkbutton"
               iconCls="icon-standard-add" plain="true"
               onclick="window.parent.mainpage.mainTabs.addModule('出库联系单管理','wms/outstock/add')">添加</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:outstock:update">
            <a href="javascript:void(0)" class="easyui-linkbutton"
               iconCls="icon-edit" plain="true" onclick="update()">修改</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:outstock:delete">
            <a href="javascript:void(0)" class="easyui-linkbutton"
               iconCls="icon-remove" plain="true" data-options="disabled:false"
               onclick="del()">删除</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:outstock:check">
            <a href="javascript:void(0)" class="easyui-linkbutton"
               iconCls="icon-standard-user-edit" plain="true" onclick="check()">审核</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:outstock:check">
            <a href="javascript:void(0)" class="easyui-linkbutton"
               iconCls="icon-standard-user-edit" plain="true" onclick="uncheck()">取消审核</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:outstock:gogogo">
            <a href="javascript:void(0)" class="easyui-linkbutton"
               iconCls="icon-standard-user-edit" plain="true" onclick="fx()">放行</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <!--      <shiro:hasPermission name="wms:outstock:planok">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="planOk()">计划费用完成</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:outstock:planno">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="planNo()">计划费用取消</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:outstock:finishok">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="finishOk()">费用完成</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:outstock:finishno">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="finishNo()">费用取消</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission> -->
        <shiro:hasPermission name="wms:outstock:adjust">
            <a href="javascript:void(0)" class="easyui-linkbutton"
               iconCls="icon-hamburg-credit" plain="true"
               onclick="outStockAdjust()">费用</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        
        <shiro:hasPermission name="wms:outstock:adkfjust">
            <a href="javascript:void(0)" class="easyui-linkbutton"
               iconCls="icon-hamburg-credit" plain="true"
               onclick="outStockAdkfjust()">费用</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        
        <shiro:hasPermission name="wms:outstock:export">
            <a href="javascript:void(0)" class="easyui-linkbutton"
               iconCls="icon-standard-page-excel" plain="true"
               onclick="exportExcel()">导出EXCEL</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:outstock:exportinfo">
            <a href="javascript:void(0)" class="easyui-linkbutton"
               iconCls="icon-standard-page-excel" plain="true"
               onclick="exportInfo(1)">出库联系单明细导出</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:outstock:exportinfowith">
            <a href="javascript:void(0)" class="easyui-linkbutton"
               iconCls="icon-standard-page-excel" plain="true"
               onclick="exportInfo(2)">出库联系单明细导出(包含船名项目号)</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="wms:outstock:transfer">
            <a href="javascript:void(0)" class="easyui-linkbutton"
               iconCls="icon-standard-printer" plain="true" onclick="transfer()">制作货转联系单</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>

            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true"
               onclick="appr()">申请单申请</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        <%-- <shiro:hasPermission name="wms:outstock:report">
            <a href="javascript:void(0)" class="easyui-linkbutton"
               iconCls="icon-standard-page-excel" plain="true" onclick="report()">出库报告书</a>
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
        //收货方
        $('#searchStock').combobox({
                            method: "GET",
                            url: "${ctx}/base/client/getClientAll?filter_EQS_clientSort=0",
                            valueField: 'clientName',
                            textField: 'clientName',
                            mode: 'remote'
                        });
        //存活方
        $('#searchReceiver').combobox({
                            method: "GET",
                            url: "${ctx}/base/client/getClientAll?filter_EQS_clientSort=0",
                            valueField: 'clientName',
                            textField: 'clientName',
                            mode: 'remote'
                        });
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/wms/outstock/json',
            fit: true,
            fitColumns: false,
            border: false,
            idField: 'outLinkId',
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[{
                field: 'outLinkId',
                title: '出库联系单ID',
                sortable: true,
                width: 140
            }, {
                field: 'stockInId',
                title: '存货方Id',
                hidden: true
            },
            {
                field: 'ifBuyerPay',
                title: '是否买方付款',
                hidden: true
            },
            {
                field: 'stockIn',
                title: '存货方',
                sortable: true,
                width: 200
            }, {
                field: 'receiver',
                title: '收货方',
                sortable: true,
                width: 200
            }, {
                field: 'customsCompany',
                title: '报关代报公司',
                sortable: true,
                width: 100
            }, {
                field: 'ciqCompany',
                title: '报检代报公司',
                sortable: true,
                width: 100
            }, {
                field: 'outCiqCount',
                title: '报检票数',
                sortable: true,
                width: 100
            }, {
                field: 'outCustomsCount',
                title: '报关票数',
                sortable: true,
                width: 100
            },
            {field:'ifBonded',title:'是否保税',sortable:true,
                formatter : function(value, row, index) {
                    return value == '1' ? '是':'否';
                }
            },
            {field:'customsCompanyId',title:'是否报关',sortable:true,
  	        	formatter : function(value, row, index) {
  	       			return value == null ? '否':'是';
  	        	}
  	        },
  	        {field:'ciqCompanyId',title:'是否报检',sortable:true,
  	        	formatter : function(value, row, index) {
  	       			return value == null ? '否':'是';
  	        	}
  	        },
            {
                field: 'ifClearStore',
                title: '是否清库',
                sortable: true,
                formatter: function (value, row, index) {
                    return value == "0" ? '否' : '是';
                }
            },{
                field: 'remark',
                title: '备注',
                sortable: true,
                width: 200
            }, 
            {
                field: 'planFeeState',
                title: '计划费用状态',
                sortable: true,
                formatter: function (value, row, index) {
                    return value == "0" ? '未完成' : '已完成';
                }
            }, {
                field: 'finishFeeState',
                title: '费用完成状态',
                sortable: true,
                formatter: function (value, row, index) {
                    return value == "0" ? '未完成' : '已完成';
                }
            }, {
                field: 'auditingState',
                title: '审核状态',
                sortable: true,
                formatter: function (value, row, index) {
                    return value == "0" ? '未审核' : '已审核';
                }
            }, {
                field: 'ifRelease',
                title: '是否放行',
                sortable: true,
                formatter: function (value, row, index) {
                    return value == "0" ? '不放行' : '放行';
                }
            }, {
                field: 'ckTime',
                title: '出库（装车）时间',
                sortable: true,
                width: 130
            }, {
                field: 'sepcialAsk',
                title: '特殊作业要求',
                sortable: true,
                width: 200
            }, {
                field: 'sellFee',
                title: '存货方承担',
                sortable: true,
                width: 200
            }, {
                field: 'buyFee',
                title: '收货方承担',
                sortable: true,
                width: 200
            }, {
                field: 'operator',
                title: '创建人',
                sortable: true,
                width: 130
            }, {
                field: 'operateTime',
                title: '创建日期',
                sortable: true,
                width: 130
            }, {
                field: 'etdWarehouse',
                title: '计划出库日期',
                sortable: true,
                width: 130
            }]],
            /* 				rowStyler : function(rowIndex, rowData) {
             //已审核的变色
             if (rowData.auditingState == "1") {
             return 'background-color:#eee';
             }
             }, */
            //        onSelect:function(rowIndex,rowData){
            //          console.log( $("tr[datagrid-row-index="+rowIndex+"]").attr("style") );
            //  		console.log( $("tr[style*='rgb(255, 228, 141)']").attr("datagrid-row-index") );
            //  			var tt=$("tr[style*='rgb(255, 228, 141)']").attr('datagrid-row-index');
            // 			var row = $('#dg').datagrid('getData').rows[tt];
            // 			if(typeof(row) != 'undefined'){
            //	 			if( row.auditingState=="1" ){
            //					$("tr[style*='rgb(255, 228, 141)").prop("style","height: 26px; background-color:red");
            //	 			}else{
            //	 				$("tr[style*='rgb(255, 228, 141)").prop("style","height: 26px; background-color:#FFFFFF");
            //	 			}
            //			}
            //			$("tr[datagrid-row-index="+rowIndex+"]").prop("style","height: 26px; background-color:#ffe48d");
            //        },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });

    //删除
    function del() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row))
            return;
        if (row.auditingState == 1) {
            parent.$.messager.show({
                title: "提示",
                msg: "此入库联系单已被审核，无法删除！",
                position: "bottomRight"
            });
            return;
        }
        parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
            if (data) {
                $.ajax({
                    type: 'get',
                    url: "${ctx}/wms/outstock/deleteoutstock/"+ row.outLinkId,
                    success: function (data) {
                    	if(data=="success"){
                        	successTip(data, dg);
                        }else{
                       		 parent.$.messager.show({ title : "提示",msg: "此出库联系单有明细，无法删除！", position: "bottomRight" });
                        }
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
        if (rowIsNull(row))
            return;
        if (row.auditingState == 1) {
            parent.$.messager.show({
                title: "提示",
                msg: "该出库联系单已审核不允许修改！",
                position: "bottomRight"
            });
        }
        window.parent.mainpage.mainTabs.addModule('出库联系单修改',
                'wms/outstock/updateoutstock/' + row.outLinkId);

    }

    //打印
    function print() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row))
            return;
        window.parent.mainpage.mainTabs.addModule('出库联系单打印',
                'wms/outstock/print/' + row.outLinkId);
    }

    //导出
    function exportExcel() {
        var url = "${ctx}/wms/outstock/export";
        $("#searchFrom").attr("action", url).submit();
    }

    //审核
    function check() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row))
            return;
        if (row.auditingState == "1") {
            parent.$.messager.show({
                title: "提示",
                msg: "只能审核审核状态为未审核的数据！",
                position: "bottomRight"
            });
            return;
        }
        
        parent.$.messager.confirm('提示', '您确定要审核此出库联系单？选择买方付款审核后无法取消。', function(data){
    		if (data){
    			$.ajax({
    	            type: 'get',
    	            url: "${ctx}/wms/outstock/passoutstock/" + row.outLinkId,
    	            success: function (data) {
    	            	if(data=='success'){
        	                successTip(data, dg);
        	                parent.$.messager.show({
        	                    title: "提示",
        	                    msg: "审核成功！",
        	                    position: "bottomRight"
        	                });
    	            	}else{
        	                parent.$.messager.alert('出库数量超过质押数量 '+data,'warning');
    	            	}

    	            }
    	        });
    		} 
    	});
    }

    //取消审核
    function uncheck() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row))
            return;
        if (row.auditingState == "0") {
            parent.$.messager.show({
                title: "提示",
                msg: "非审核状态！",
                position: "bottomRight"
            });
            return;
        }
        if (row.ifBuyerPay == "1") {
            parent.$.messager.show({
                title: "提示",
                msg: "买方付款,无法取消审核！",
                position: "bottomRight"
            });
            return;
        }
        
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/outstock/nopassoutstock/" + row.outLinkId,
            success: function (data) {
                successTip(data, dg);
                parent.$.messager.show({
                    title: "提示",
                    msg: "取消审核成功！",
                    position: "bottomRight"
                });
            }
        });
    }

    //放行
    function fx() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row))
            return;
        if (row.ifRelease == "1") {
            parent.$.messager.show({
                title: "提示",
                msg: "已处于放行状态！",
                position: "bottomRight"
            });
            return;
        }
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/outstock/fx/" + row.outLinkId,
            success: function (data) {
                successTip(data, dg);
                parent.$.messager.show({
                    title: "提示",
                    msg: "操作成功！",
                    position: "bottomRight"
                });
            }
        });
    }

    //计划费用完成
    function planOk() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row))
            return;
        if (row.planFeeState == "1") {
            parent.$.messager.show({
                title: "提示",
                msg: "计划费用已在完成状态！",
                position: "bottomRight"
            });
            return;
        }
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/outstock/planok/" + row.outLinkId,
            success: function (data) {
                successTip(data, dg);
                parent.$.messager.show({
                    title: "提示",
                    msg: "操作成功！",
                    position: "bottomRight"
                });
            }
        });
    }

    //计划费用取消
    function planNo() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row))
            return;
        if (row.planFeeState == "0") {
            parent.$.messager.show({
                title: "提示",
                msg: "计划费用已在非完成状态！",
                position: "bottomRight"
            });
            return;
        }
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/outstock/planno/" + row.outLinkId,
            success: function (data) {
                successTip(data, dg);
                parent.$.messager.show({
                    title: "提示",
                    msg: "操作成功！",
                    position: "bottomRight"
                });
            }
        });
    }

    //费用完成
    function finishOk() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row))
            return;
        if (row.finishFeeState == "1") {
            parent.$.messager.show({
                title: "提示",
                msg: "费用已在完成状态！",
                position: "bottomRight"
            });
            return;
        }
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/outstock/finishok/" + row.outLinkId,
            success: function (data) {
                successTip(data, dg);
                parent.$.messager.show({
                    title: "提示",
                    msg: "操作成功！",
                    position: "bottomRight"
                });
            }
        });
    }

    //费用取消
    function finishNo() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row))
            return;
        if (row.finishFeeState == "0") {
            parent.$.messager.show({
                title: "提示",
                msg: "费用已在非完成状态！",
                position: "bottomRight"
            });
            return;
        }
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/outstock/finishno/" + row.outLinkId,
            success: function (data) {
                successTip(data, dg);
                parent.$.messager.show({
                    title: "提示",
                    msg: "操作成功！",
                    position: "bottomRight"
                });
            }
        });
    }

    //制作货转联系单
    function transfer() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row))
            return;
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/outstock/createTransfer/" + row.outLinkId,
            success: function (data) {
                var href = '${ctx}/bis/transfer/update/' + data;
                window.parent.mainpage.mainTabs.addModule('货转单管理', href,
                        'icon-hamburg-cv');
                parent.$.messager.show({
                    title: "提示",
                    msg: "货转联系单制作成功！请手动选择费用方案及计费日期！",
                    position: "bottomRight"
                });
            }
        });
    }

    //进入  出库费用调整
    function outStockAdjust() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row))
            return;
        var linkId = row.outLinkId;
        var stockId = row.stockInId;
	    $.ajax({
				type:'POST',
				url:"${ctx}/base/scheme/checkPermission/"+row.operator,
				success: function(data){
					if(data=="success"){
						window.parent.mainpage.mainTabs.addModule('费用','cost/standingBook/outList/' + linkId + "/" + stockId);
					}else{
					    parent.$.messager.show({ title : "提示",msg: "你不是此联系单的创建人，无法进行修改操作！", position: "bottomRight" });
					}
			   }
		 });
    }
    
    //进入客服出库费用调整
    function outStockAdkfjust() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row))
            return;
        var linkId = row.outLinkId;
        var stockId = row.stockInId;
        parent.$.messager.confirm('提示', '是否确认生成费用？', function (data) {
        	if (data) {
		        $.ajax({
						type:'POST',
						url:"${ctx}/base/scheme/checkPermission/"+row.operator,
						success: function(data){
							if(data=="success"){
								window.parent.mainpage.mainTabs.addModule('费用','cost/standingBook/outkfList/' + linkId + "/" + stockId);
							}else{
								parent.$.messager.show({ title : "提示",msg: "你不是此联系单的创建人，无法进行修改操作！", position: "bottomRight" });
							}
						}
					});
        	}
        });
    }
    //跳转出库报告书
    function report() {
        window.parent.mainpage.mainTabs.addModule('出库报告书',
                '/ccli/wms/outstock/toreport');
    }

    //导出出库联系单明细
    function exportInfo(type) {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row))
            return;
        var url = "/ccli/wms/outstockinfo/exportinfo/" + row.outLinkId
                + "/" + type;
        $("#infoForm").attr("action", url).submit();
    }

    //数字校验
    function ischeckNum(val) {
        if (val.value) {
            if (!isNaN(val.value)) {

            } else {
                parent.$.messager.show({
                    title: "提示",
                    msg: "请输入数字!",
                    position: "bottomRight"
                });
                $("#" + val.id).val("");
                myfm.isnum.select();
                return false;
            }
        }
    }

      //分类监管 申请单
    function appr() {
        var row = dg.datagrid('getSelected');
        if(rowIsNull(row)) return;
        d=$("#dlg").dialog({   
            title: '申请单申请',    
            width: 560,    
            height: 380,    
            href:'${ctx}/wms/outstock/appr/'+row.outLinkId,
            modal:true,
            buttons:[{
                text:'确认',
                handler:function(){             
                    $("#mainform").submit(); 
                
                }
            },{
                text:'重置',
                handler:function(){                 
                    resetForm();
                }
            },{
                text:'取消',
                handler:function(){
                    d.panel('close');
                }
            }]
        });
    }
</script>
</body>
</html>