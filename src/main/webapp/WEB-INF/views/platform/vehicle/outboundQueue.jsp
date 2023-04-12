<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<%--<body>
<div id="tb" style="padding:5px;height:auto">
    <div>
        <form id="infoForm" method="post">
        </form>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="upd()">手动指派月台</a>
        <span class="toolbar-item dialog-tool-separator"></span>
    </div>
</div>

<table id="dg"></table>--%>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="出库总览列表" style="overflow-y:auto">
    <div style="padding:5px;height:auto" class="datagrid-toolbar">
        <form id="searchFrom" action="">
<%--
            <input type="text"  name="platformNo" class="easyui-validatebox" data-options="width:150,prompt: '月台号'"/>
--%>

            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text" name="carNumber" class="easyui-validatebox" data-options="width:150,prompt: '车牌号'"/>

            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text" name="containerNo" class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>
            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text" name="billNo" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>

            <span class="toolbar-item dialog-tool-separator"></span>
            <input type="text" name="appointDateStart" class="easyui-my97" datefmt="yyyy-MM-dd"   data-options="width:150,prompt: '预约日期（开始）'"/>
            <input type="text" name="appointDateEnd" class="easyui-my97" datefmt="yyyy-MM-dd"   data-options="width:150,prompt: '预约日期（结束）'"/>
            <span class="toolbar-item dialog-tool-separator"></span>


            <select id="statusFlag" name="statusFlag" class="easyui-combobox"
                    data-options="width:150,prompt: '车辆状态'">
                <option></option>
                <option value="0">排队中</option>
                <option value="1">作业中</option>
                <option value="2">作业完成</option>


            </select>
            <span class="toolbar-item dialog-tool-separator"></span>





            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        </form>
        <form id="searchFrom3" action="">
        </form>



    </div>
    <table id="dg"></table>
</div>

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


        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/platform/outBoundQueue/serach',
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
                {field: 'roomNum', title: '预约库号',  width: 50},
                {field: 'warehouseNo', title: '库号',  width: 30},
                {field: 'locationNo', title: '房间号',  width: 50},
                {field: 'platformNo', title: '月台号',  width: 30},
                {field: 'platformName', title: '月台名称',  width: 80},
                {field: 'carNumber', title: '车号',  width: 80},
                {field: 'containerNo', title: '箱号',  width: 100},
                {field: 'billNo', title: '提单号',  width: 100},
                {field: 'productName', title: '货物名称',  width: 130},
                {field: 'productType', title: '品名',  width: 50,
                    formatter: function (value, row, index) {
                        if (value == "1") {
                            return '水产';
                        } else if (value == "2") {
                            return '肉类';
                        } else {
                            return '其他';
                        }
                    }
                },
                {field: 'num', title: '件数',  width: 50},
                {field: 'weight', title: '重量',  width: 50},
                {field: 'originCountry', title: '原产国',  width: 50},
                {field: 'consumeCompany', title: '客户名称',  width: 150},
                {field: 'autoManualFlag', title: '自动/手动分配',width: 50,
                    formatter: function (value, row, index) {
                        if (value=="1"||value=="3"){
                            return '自动';
                        }else if(value=="2") {
                            return '手动';
                        }
                    }
                },

                {field: 'appointDate', title: '预约时间', width: 90},

                {field: 'queueTime', title: '排队时间', width: 130},

                {field: 'statusFlag', title: '状态',width: 90,
                    formatter:function (value, row, index){
                        if (value == "0"){
                            return '排队中';
                        }else if (value == "1"){
                            return '车辆驶入月台';
                        }else if (value == "2"){
                            return '已开始';
                        }else if (value == "3"){
                            return '已结束';
                        }else if (value =="4"){
                            return '车辆离开月台'
                        }else if (value =="5"){
                            return '车辆出闸'
                        } else if (value =="6"){
                            return '车辆取消靠口';
                        }else if(value ==null){
                            return '未入闸';
                        }
                    }
                },
                {field: 'startTime', title: '开始时间', width: 130},

                {field: 'endTime', title: '结束时间', width: 130},

                {field: 'leaveTime', title: '车辆离台时间', width: 130}



            ]],
            rowStyler: function (rowIndex, rowData) {
              //  console.log(rowData.statusFlag);

               if(rowData.statusFlag=='0'){

                   var time= (new Date().getTime()-  new Date(rowData.queueTime).getTime())/( 60 * 60 * 1000);

                   if(time>=4){
                       return 'background-color:red;';
                   }else if(time>=2){
                       return 'background-color:Yellow;';
                   }
               }
              /*  //变成红色
                if (rowData.colorType=='2') {
                    return 'background-color:red;';
                }else if(rowData.colorType=='1'){
                    return 'background-color:Yellow;';
                }*/
            },
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
    });


    //创建查询对象并查询
    function cx() {
        dg.datagrid('clearSelections');
        var obj = $("#searchFrom").serializeObject();

        dg.datagrid('load', obj);
    }

</script>
</body>
</html>