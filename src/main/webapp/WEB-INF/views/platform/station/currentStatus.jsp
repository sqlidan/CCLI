<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%--<meta http-equiv="refresh" content="5">--%>
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


    </div>
</div>

<table id="dg"></table>
<div id="dlg"></div>

<script type="text/javascript">
    var dg;
    var d;

    // document.onkeydown = function () {
    //     if (event.keyCode == 13) {
    //         cx();
    //     }
    // };

    $(function () {
        getInitData();
        run();

    });
    var interval;
    function run(){
        interval = setInterval(getIntervalData,"3000");
    }

    function getIntervalData(){
        $.ajax({
            type: 'post',
            url: "${ctx}/platform/station/current/search",
            dateType: 'json',
            success: function (data){
                $('#dg').datagrid("loadData",data);
            }

            });
    }

    function getInitData(){
        //客户
        <%--$('#searchStockIn').combobox({--%>
        <%--    method: "GET",--%>
        <%--    url: "${ctx}/base/client/getClientAll",--%>
        <%--    valueField: 'ids',--%>
        <%--    textField: 'clientName',--%>
        <%--    mode: 'remote'--%>
        <%--});--%>

        dg = $('#dg').datagrid({
            method: "post",
            url: '${ctx}/platform/station/current/search',
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
                {field: 'PLATFORMNO', title: '月台号',  width: 90},
                {field: 'PLATFORM', title: '月台',  width: 90},
                {field: 'WAREHOUSENO', title: '库号',  width: 90},
                {field: 'PLATFORMSTATUS', title: '月台状态', width: 100,
                    formatter:function (val,row,index){
                        if (val == "3"){
                            return '停止';
                        }else {
                            return '启用';
                        }
                    }

                },
                {field: 'QUEUESTATUS', title: '车辆状态', width: 100,
                    formatter:function (val,row,index){
                        if (val=="0"){
                            return "空闲";
                        }else if (val == "1"){
                            return "车辆驶入月台";
                        }else if (val == "2"){
                            return "开始作业";
                        }else if (val =="3") {
                            return "结束作业";
                        }else if(val =="4"){
                            return "车辆离开月台";
                        }
                    }

                },
                {field: 'OPERATIONFLAG', title: '出库/入库',  width: 70,
                    formatter:function (val,row,index){
                        if (val=="1"){
                            return "入库";
                        }else if(val=="2"){
                            return "出库";
                        }
                    }
                },
                {field: 'BILLNO', title: '提单号',  width: 90},
                {field: 'CARNUMBER', title: '车号',  width: 90},
                {field: 'CTNNUM', title: '箱号',  width: 90}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    }

</script>
</body>
</html>