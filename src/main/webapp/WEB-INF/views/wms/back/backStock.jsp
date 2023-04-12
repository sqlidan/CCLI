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

            <input type="text" name="filter_LIKES_trayId" class="easyui-validatebox"
                   data-options="width:100,prompt: '托盘号'"/>
			<input type="text" name="filter_LIKES_billNum" class="easyui-validatebox"
                   data-options="width:100,prompt: '提单号'"/>
            <input type="text" name="filter_LIKES_skuId" class="easyui-validatebox"
                   data-options="width:100,prompt: 'sku'"/>         
			<input type="text" name="filter_LIKES_asn" class="easyui-validatebox"
                   data-options="width:100,prompt: 'asn'"/>
			<input type="text" name="filter_LIKES_loadingNum" class="easyui-validatebox"
                   data-options="width:100,prompt: '装车单号'"/> 

            <input type="text" name="filter_GED_backTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                   data-options="width:150,prompt: '回库上架日期'"/>

            - <input type="text" name="filter_LED_backTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                     data-options="width:150,prompt: '回库上架日期'"/>
                      <span class="toolbar-item dialog-tool-separator"></span>
	 <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
			<span class="toolbar-item dialog-tool-separator"></span>
	 <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="jf()">计费</a>
        </form>
    </div>
</div>

<table id="dg"></table>
<div id="dlg"></div>

<script type="text/javascript">
    var dg;
    var d;


    $(function () {

        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/wms/back/json',
            fit: true,
            fitColumns: false,
            border: false,
            idField: 'id',
            sortOrder: 'desc',
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'id', title: 'id', hidden: true},
                {field: 'trayId', title: '托盘号', sortable: true, width: 150},
                {field: 'ctnNum', title: '箱号',sortable: true, width: 150},
                {field: 'billNum', title: '提单号', sortable: true, width: 200},
                {field: 'asn', title: 'asn', sortable: true, width: 200},
                {field: 'skuId', title: 'sku', sortable: true, width: 150},
                {field: 'loadingNum', title: '装车单号', sortable: true, width: 150},
                {field: 'stockName', title: '客户名称', sortable: true},
                {field: 'nowPiece', title: '件数', sortable: true},
                {field: 'cargoName', title: '产品名称', sortable: true, width: 150},
                {field: 'cargoType', title: '产品类型', sortable: true, width: 150},
                {field: 'netWeight', title: '总净重', sortable: true, width: 150},
                {field: 'grossWeight', title: '总毛重', sortable: true, width: 100},
                {field: 'warehouse', title: '仓库', sortable: true, width: 100},
                {field: 'cargoLocation', title: '库位号', sortable: true},
                {field: 'backPerson', title: '回库操作员', sortable: true},
                {field: 'backTime', title: '回库时间', sortable: true, width: 150}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });



    //创建查询对象并查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
    }
    
    //计费
	function jf(){
		var row = dg.datagrid('getSelected');
		if (rowIsNull(row)) return;
		
		$.ajax({
			async:false,
			type:'POST',
			url:"${ctx}/wms/back/checkifjf",
			data:{"loadingNum":row.loadingNum,"billNum":row.billNum},
			dataType:"text",
			success: function(msg){
				if(msg == "success"){
					
			        $.ajax({
			            async: false,
			            type: 'POST',
			            url: "${ctx}/wms/back/jf",
			            data: {"loadingNum":row.loadingNum},
			            dataType: "text",
			            queryParams: {//往后台查询数据时，添加额外的参数
			                
			            }, 
			            success: function (msg) {

			                if (msg == "success") {
			                	
			                	
			                    toast("计费成功!");
			                } else {
			                    toast("计费失败!");
			                }

			            }
			        });
				}else{
					toast("已计算过费用，不能重复计费!");
					return;
				}
				
			}
		});
		
		
		
		

	}

    function toast(msg) {
        parent.$.messager.show({title: "提示", msg: msg, position: "bottomRight"});
    }
</script>
</body>
</html>