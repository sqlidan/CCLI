<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">

<div id="tb">

    <div style="padding:5px;height:auto" class="datagrid-toolbar">

        <form id="searchFrom" action="">

            <input type="text" name="ctnNum" class="easyui-validatebox" data-options="width:120,prompt: '箱号'"/>
            <input type="text" name="billNum" class="easyui-validatebox" data-options="width:120,prompt: '提单号'"/>
            <input type="text" name="asn" class="easyui-validatebox" data-options="width:120,prompt: 'ASN'"/>
            <select id="clientId" name="clientId" class="easyui-combobox" data-options="width:120,prompt: '装卸单位'">
            </select>

            <input type="text" name="startDate" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                   data-options="width:150,prompt: '开始日期'"/>

            <input type="text" name="endDate" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
                   data-options="width:150,prompt: '结束日期'"/>

            <span class="toolbar-item dialog-tool-separator"></span>

            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>

        </form>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
           onclick="addModule('闸口管理 - 添加','wms/gate/info/add')">进闸</a>

        <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
           onclick="onUpdate()">修改</a>

        <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
           onclick="onDel()">删除</a>

        <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
           onclick="selectCar()">安排车辆</a>

        <span class="toolbar-item dialog-tool-separator"></span>
        
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
           onclick="callCar()">呼叫车辆</a>

        <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
           onclick="onCancelCar()">取消车辆</a>

        <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
           onclick="onOutGate()">出闸</a>

        <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
           onclick="onEmptyCarIn()">空车进闸</a>

        <span class="toolbar-item dialog-tool-separator"></span>

        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true"
           onclick="onEmptyCarOut()">空车出闸</a>

        <span class="toolbar-item dialog-tool-separator"></span>

    </div>

</div>

<table id="dg"></table>
<div id="dlg"></div>

<script type="text/javascript">

    var dg;
    var dlg;

    /**
     * 修改记录
     */
    function onUpdate() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        addModule('闸口管理 - 修改', 'wms/gate/info/update?ctnNum=' + row.ctnNum);
    }

    /**
     * 删除记录
     */
    function onDel() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row))
            return;

        parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {

            if (data) {
                delInfo(row);
            }

        });

    }

    function delInfo(row) {
        $.ajax({
            type: 'post',
            data: {ctnNum: row.ctnNum},
            url: "${ctx}/wms/gate/info/delete",
            success: function (data) {

                if (data == "success") {
                    toast("删除成功");
                    cx();
                } else {
                    toast("删除失败");
                }

            }
        });
    }

    /**
     * 选择安排车辆
     */
    function selectCar() {

        var record = dg.datagrid('getSelected');

        if (rowIsNull(record))
            return;

        console.log(record.outCarNum);

        if (record.outCarNum != null) {
            toast("已安排车辆");
            return;
        }

        dlg = $("#dlg").dialog({
            title: '派车',
            width: 380,
            height: 380,
            href: "${ctx}/wms/gate/car/form",
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {

                    var row = $('#carList').datagrid('getSelected');

                    if (rowIsNull(row))
                        return;

                    addOutCar(row, record.ctnNum);

                }
            }, {
                text: '取消',
                handler: function () {
                    dlg.panel('close');
                }
            }]
        });

    }

    /**
     * 添加安排车辆
     */
    function addOutCar(info, ctnNum) {

        var url = "${ctx}/wms/gate/car/addOut";
        var params = {
            ctnNum: ctnNum,
            carNum: info.CARNUM,
            driverName: info.DRIVERNAME
        };

        $.post(url, params, function (data) {

            if (data == "success") {
                dlg.panel('close');
                toast("安排车辆成功");
                cx();
            } else {
                toast("安排车辆失败");
            }

        });

    }
    
    
    /**
     * 呼叫安排车辆
     */
    function callCar() {
		var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) 
        	return;
        if (row.outCarNum == null) {
            toast("未安排出闸车辆");
            return;
        }
        voiceCar(row.outCarNum);
    }

    /**
     * 取消安排车辆
     */
    function onCancelCar() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row))
            return;

        if (row.outCarNum == null) {
            toast("没有安排的车辆");
            return;
        }

        var url = "${ctx}/wms/gate/car/cancelOut";
        var params = {
            ctnNum: row.ctnNum,
            carNum: row.outCarNum,
            driverName: row.outDriverName
        };

        $.post(url, params, function (data) {

            if (data == "success") {
                toast("取消车辆成功");
                cx();
            } else {
                toast("取消车辆失败");
            }

        });

    }

    /**
     * 出闸功能
     */
    function onOutGate() {

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row))
            return;

        if (row.outCarNum == null) {
            toast("没有安排出闸车辆");
            return;
        }

        parent.$.messager.confirm('提示', '确定进行出闸操作？', function (data) {

            if (data) {
                outGate(row);
            }

        });

    }

    function outGate(row) {
        $.ajax({
            type: 'post',
            data: {ctnNum: row.ctnNum},
            url: "${ctx}/wms/gate/out",
            success: function (data) {

                if (data == "success") {
                    toast("出闸成功");
                    cx();
                } else {
                    toast("出闸失败");
                }

            }
        });
    }

    function onEmptyCarIn() {

        dlg = $("#dlg").dialog({
            title: '空车进闸',
            width: 380,
            height: 380,
            href: "${ctx}/wms/gate/car/empty/form",
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    emptyCarIn();
                    dlg.panel('close');
                }
            }, {
                text: '取消',
                handler: function () {
                    dlg.panel('close');
                }
            }]
        });

    }

    function emptyCarIn() {
        $.ajax({
            type: 'post',
            data: $("#mainForm").serialize(),
            url: "${ctx}/wms/gate/car/empty/in",
            success: function (data) {

                if (data == "success") {
                    toast("进闸成功");
                    cx();
                } else {
                    toast("进闸失败");
                }

            }
        });

    }

    function onEmptyCarOut() {

        dlg = $("#dlg").dialog({
            title: '空车出闸',
            width: 380,
            height: 380,
            href: "${ctx}/wms/gate/car/empty/form",
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    emptyCarOut();
                    dlg.panel('close');
                }
            }, {
                text: '取消',
                handler: function () {
                    dlg.panel('close');
                }
            }]
        });


    }

    function emptyCarOut() {

        $.ajax({
            type: 'post',
            data: $("#mainForm").serialize(),
            url: "${ctx}/wms/gate/car/empty/out",
            success: function (data) {
                if (data == "success") {
                    toast("出闸成功");
                    cx();
                } else {
                    toast("出闸失败，车辆已安排或已出闸");
                }

            }
        });

    }

    function cx() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
    }

    $(document).ready(function () {

        $('#clientId').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote'
        });

        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/wms/gate/page',
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
                {field: 'ctnNum', title: '箱号', sortable: true, width: 100},
                {field: 'ctnVersion', title: '箱型', sortable: true, width: 50},
                {field: 'billNum', title: '提单号', sortable: true, width: 100},
                {field: 'asn', title: 'ASN', sortable: true, width: 100},
                {field: 'position', title: '场位', sortable: true, width: 50},
                {field: 'clientName', title: '装卸单位', sortable: true, width: 100},
                {field: 'carNum', title: '进闸车', sortable: true, width: 100},
                {field: 'driverName', title: '进闸司机名', hidden: true, width: 100},
                {field: 'outCarNum', title: '出闸车', sortable: true, width: 100},
                {field: 'outDriverName', title: '出闸司机名', hidden: true, width: 100},
                {field: 'createDate', title: '创建时间', sortable: true, width: 100}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });

    });

    function toast(msg) {
        parent.$.messager.show({title: "提示", msg: msg, position: "bottomRight"});
    }

    function addModule(title, url) {
        window.parent.mainpage.mainTabs.addModule(title, url);
    }

function voiceCar(carNum){
	var zhText ="车号为" + carNum + "的车辆已安排出闸"; 
	zhText = encodeURI(zhText);
	document.write("<audio autoplay=\"autoplay\">");
	document.write("<source src=\"http://tts.baidu.com/text2audio?lan=zh&ie=UTF-8&spd=2&text="+ zhText +"\" type=\"audio/mpeg\">");
	document.write("<embed height=\"0\" width=\"0\" src=\"http://tts.baidu.com/text2audio?lan=zh&ie=UTF-8&spd=2&text="+ zhText +"\">");
	document.write("</audio>");
}
</script>

</body>

</html>