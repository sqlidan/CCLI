<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
    <script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
<div id="tb" style="padding:5px;height:auto">
    <div>
        <form id="searchFromEasy" action="">
            <input type="hidden" id="linkId" name="linkId" value="${linkId }"/>

            <input type="text" name="filter_LIKES_contractId" class="easyui-validatebox"
                   data-options="width:150,prompt: '合同号'"/>
            <select class="easyui-combobox" id="clientId" name="filter_EQS_customsId"
                    data-options="width:150, prompt: '客户名称'">
            </select>
            <select id="ifGet" class="easyui-combobox" name="filter_EQS_ifGet" data-options="width:150,prompt:'应收&应付'">
                <option value="1">应收</option>
                <c:choose>
                    <c:when test="${planFeeState == '0'}">
                        <option value="2">应付</option>
                    </c:when>
                </c:choose>
            </select>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cxc()">查询</a>
        </form>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
           onclick="addScheme()">引入</a>
        <span class="toolbar-item dialog-tool-separator"></span>
    </div>
</div>
<table id="dg"></table>
<div id="dlg"></div>
<script type="text/javascript">

    document.onkeydown = function () {
        if (event.keyCode == 13) {
            cxc();
        }
    };

    var dg;
    var d;

    $(function () {

        $('#ifGet').combobox({
            onChange: function (newValue, oldValue) {
                if (newValue == "2") {
                    $('#clientId').combobox("select", "");
                }
            }
        });

        dg = $('#dg').datagrid({

            method: "get",
            url: '${ctx}/base/scheme/json2',
            fit: true,
            fitColumns: true,
            border: false,
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: false,
            columns: [[
                {field: 'SCHEME_NUM', title: '方案编号', sortable: true, width: 100},
                {field: 'SCHEME_NAME', title: '方案名称', sortable: true, width: 100},
                {field: 'CONTRACT_ID', title: '合同号', sortable: true, width: 100},
                {
                    field: 'PROGRAM_TYPE', title: '方案类型', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        if (value == 1) {
                            return '1';
                        }
                        if (value == 2) {
                            return '2';
                        }
                        if (value == 3) {
                            return '3';
                        }
                        if (value == 4) {
                            return '4';
                        }
                    }
                },
                {field: 'CUSTOMS_NAME', title: '客户名称', sortable: true, width: 100,
		        	formatter: function(value, row, index) {
			        	if($("#ifGet").val()==1){
					 		return $("#clientId").combobox("getText");
					 	}
		        	}
		        },
                {
                    field: 'BIS_TYPE', title: '业务类型', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        if (value == 1) {
                            return '1';
                        }
                        if (value == 2) {
                            return '2';
                        }
                        if (value == 3) {
                            return '3';
                        }
                        if (value == 4) {
                            return '4';
                        }
                    }
                },
                {
                    field: 'PROGRAM_STATE', title: '方案状态', sortable: true, width: 100,
                    formatter: function (value, row, index) {
                        return value == 1 ? '已审核' : '未审核';
                    }
                }
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });

        //客户下拉
        var client = ${stockId};
        $('#clientId').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll?setid=${stockId}",
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote',
            onLoadSuccess: function () {
                if (client != null && client != "") {
                    $('#clientId').combobox("select", client);
                    client = "";
                    cxc();
                }
            }
        });

        // 因为初始化查询数据有误,所以延迟500ms后再查询一次
        window.setTimeout(function () {
            cxc()
        }, 500);
    });

    //创建查询对象并查询
    function cxc() {
        var obj = $("#searchFromEasy").serializeObject();
        dg.datagrid('load', obj);
    }

    //添加  方案费目
    function addScheme() {
        var linkId = $('#linkId').val();

        var row = dg.datagrid('getSelected');
        if (row == null) {
            parent.$.messager.show({title: "提示", msg: "请选择方案！", position: "bottomRight"});
            return;
        }
        parent.$.messager.confirm('提示', '确认添加？', function (data) {
            if (data) {
                var newIdsList = [];
                var datas = dg.treegrid('getSelections');
                for (var i = 0; i < datas.length; i++) {
                    newIdsList.push(datas[i].SCHEME_NUM);
                }
                $.ajax({
                    async: false,
                    type: 'POST',
                    data: JSON.stringify(newIdsList),
                    contentType: 'application/json;charset=utf-8',
                    url: "${ctx}/cost/standingBook/" + linkId + "/addOutStandingSchemeBatch",
                    success: function (data) {
                        parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
                        window.dg1.datagrid('reload');
                        window.dg2.datagrid('reload');
                        window.sunCost();
                        $("#dlg1").panel("close");
                    }
                });

            }
        });
    }

</script>
</body>
</html>