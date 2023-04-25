<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body>
<div id="tblx" style="padding:5px;height:auto">
    <div>
        <form id="searchLinkFrom" action="" method="post">
            <input type="text" id="outId" name="filter_LIKES_outLinkId" class="easyui-validatebox"
                   data-options="width:150,prompt: '出库单号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true"
               onclick="cxLink()">查询</a>
        </form>
    </div>
</div>
<table id="dglx"></table>
<div id="dlglx"></div>
<script type="text/javascript">
    var dglx;
    $(function () {
        dglx = $('#dglx').datagrid({
            method: "post",
            url: '${ctx}/wms/outstock/jsonlist',
            fit: true,
            fitColumns: true,
            border: false,
            idField: 'linkId',
            striped: true,
            pagination: false,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 10,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'OUT_LINK_ID', title: '联系单号', sortable: false, width: 100},
                {field: 'RECEIVER', title: '收货方', sortable: false, width: 100},
                {field: 'RECEIVER_ID', hidden: true},
                {field: 'RECEIVER_LINKER', title: '收货联系人', sortable: false, width: 100},
                {field: 'STOCK_IN', title: '存货方', sortable: false, width: 100},
                {field: 'STOCK_IN_ID', hidden: true},
                {field: 'OPERATOR', title: '创建人', sortable: false, width: 100},
                {field: 'OPERATE_TIME', title: '创建时间', sortable: false, width: 100},
                {field: 'IF_CLEAR_STORE', title: 'l', hidden: false, width: 100}

            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tblx'
        });


    });

    function cxLink() {
        var data = $("#searchLinkFrom").serializeObject();
        var outId = $("#outId").val();
        var receiverNum = $('#receiverNum').combobox('getValue');
        var stockNum = $('#stockNum').combobox('getValue');
        if (outId == "") {
            parent.$.easyui.messager.alert("请填写查询条件！");
            return false;
        }
        $.ajax({
            type: "post",
            url: "${ctx}/wms/outstock/jsonlist2",
            data: {"outId": outId, "receiverNum": receiverNum, "stockNum": stockNum},
            dataType: "json",
            success: function (data) {
                var rows = $('#dglx').datagrid('getRows');
                for (var i = rows.length; i > 0; i++) {
                    $('#dglx').datagrid('deleteRow', i + 1);
                }
                if (data != null && data.length > 0) {
                    for (var i = 0; i < data.length; i++) {
                        addLinkTBRow(data[i]);
                    }
                }
            }
        });
    }

</script>
</body>
</html>