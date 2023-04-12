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
        <form id="searchFrom" action="" method="post">
            <input type="text" name="trayId" class="easyui-validatebox" data-options="width:120,prompt: '托盘号'"/>
            <input type="text" name="ctnNum" class="easyui-validatebox" data-options="width:120,prompt: '箱号'"/>

            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>

        </form>
    </div>

</div>

<table id="dg"></table>

<script type="text/javascript">

    var dg;

//    document.onkeydown = function () {
//        if (event.keyCode == 13) cx();
//    };

    //创建查询对象并查询
    function cx() {
        dg.datagrid('load', $("#searchFrom").serializeObject());
    }

    $(function () {

        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/stock/check/record/search',
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
                {field: 'id', title: 'id', hidden: true},
                {field: 'trayId', title: '托盘号', sortable: true, width: 100},
                {field: 'billNum', title: '提单号', sortable: true, width: 130},
                {field: 'ctnNum', title: '箱号', sortable: true, width: 130},
                {field: 'skuId', title: 'SKU', sortable: true, width: 120},
                {field: 'realCargoName', title: '反馈产品名称', sortable: true, width: 200},
                {field: 'realPiece', title: '反馈数量', sortable: true, width: 100},
                {field: 'cargoName', title: '产品名称', sortable: true, width: 100},
                {field: 'nowPiece', title: '数量', sortable: true, width: 100},
//                {field: 'netWeight', title: '净重', sortable: true, width: 100},
//                {field: 'grossWeight', title: '毛重', sortable: true, width: 100},
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
                {field: 'checkName', title: '盘点反馈人员', sortable: true, width: 100},
                {field: 'checkTime', title: '盘点时间', sortable: true, width: 170,
                	formatter: function(value,row,index){
                		if( value != null){
                			var unixTimestamp = new Date(value);
                    		return unixTimestamp.toLocaleString();	
                		}
                		
                	}	
                },
                {field: 'description', title: '备注', sortable: true, width: 100}
            ]],
            onLoadSuccess: function (data) {

            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });

    });

</script>
</body>
</html>