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
            <input type="text" id="ordId" name="ordId" class="easyui-validatebox"
                   data-options="width:150,prompt: '出库单号'" value="${ordId }">
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
            url: '${ctx}/base/floor/listFloorTray',
            fit: true,
            fitColumns: true,
            border: false,
            idField: 'floorNum',
            striped: true,
            pagination: false,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 10,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: true,
            columns: [[
                {field: 'floorNum', title: '楼层', sortable: false, width: 100},
                {field: 'billNum', title: '提单号', sortable: false, width: 100},
                {field: 'ctnNum', title: '箱号', sortable: false, width: 100},
                {field: 'skuId', title: 'skuId', sortable: false, width: 100},
                {field: 'allNum', title: '库存数量', sortable: false, width: 100}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tblx'
        });


    });

    function cxLink() {
        // var data = $("#searchLinkFrom").serializeObject();
        var ordId = $("#ordId").val();
        if (ordId == "") {
            parent.$.easyui.messager.alert("请填写查询条件！");
            return false;
        }
        $.ajax({
            type: "post",
            url: "${ctx}/base/floor/listFloorTray",
            data: {"ordId": ordId},
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

    //给Link表单添加信息
    function addLinkTBRow(jsonObj) {
        var rows = $('#dglx').datagrid('getRows');
        $('#dglx').datagrid('insertRow', {
            index: rows.length + 1,
            row: {
                floorNum: jsonObj.FLOORNUM,
                billNum: jsonObj.BILLNUM,
                ctnNum: jsonObj.CTNNUM,
                skuId: jsonObj.SKUID,
                allNum: jsonObj.ALLNUM
            }
        });

    }
</script>
</body>
</html>