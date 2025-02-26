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
            <input type="text" name="filter_LIKES_orderNum" class="easyui-validatebox"
                   data-options="width:150,prompt: '订单号'"/>
            <input type="text" name="filter_LIKES_outLinkId" class="easyui-validatebox"
                   data-options="width:150,prompt: '联系单号'"/>
            <!--  <input type="text" name="filter_EQS_billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>-->
            <input id="planTime" name="filter_EQD_planTime" type="text" class="easyui-my97" datefmt="yyyy-MM-dd"
                   data-options="width: 150,prompt:'出库时间'" value="<fmt:formatDate value="${asn.inboundTime}"/>"/>
            <select class="easyui-combobox" id="orderState" name="filter_EQS_orderState"
                    data-options="width:150,prompt:'订单状态'">
                <option value=""></option>
            </select>
            
            <select class="easyui-combobox" id="islock" name="filter_EQS_islock"
                    data-options="width:150,prompt:'控车状态'">
                <option value=""></option>
            </select>
            <select id="searchStock" name="filter_LIKES_stockName" class="easyui-combobox"
                    data-options="width:150,prompt: '存货方'">
            </select>
            <select id="searchStock2" name="filter_LIKES_receiverName" class="easyui-combobox"
                    data-options="width:150,prompt: '收货方'">
            </select>
            <span class="toolbar-item dialog-tool-separator"></span>
            <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
        </form>

        <shiro:hasPermission name="bis:loading:add">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
               onclick="add();">添加</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:loading:update">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
               onclick="upd()">修改</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:loading:delete">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
               data-options="disabled:false" onclick="del()">删除</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:loading:addzcd">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="opencl()">生成装车单</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:loading:lock">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="lock()">控车</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <shiro:hasPermission name="bis:loading:unlock">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="unlock()">解控</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
        <a href="javascript:void(0)" class="easyui-linkbutton"
            iconCls="icon-hamburg-old-versions" plain="true"
            onclick="mani()">核放单申请</a>
        <span class="toolbar-item dialog-tool-separator"></span>
        <shiro:hasPermission name="bis:loading:update">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-old-versions" plain="true"
               onclick="createReservation()">生成预约出库信息</a>
            <span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>
    </div>

</div>
<table id="dg"></table>
<div id="dlg"></div>
<div id="dtg"></div>
<div id="win"></div>
<div>

</div>
<script type="text/javascript">
    var dg;
    var d, dt;
    var splitJson, tids;//用于记录需要拆分的托盘记录

    document.onkeydown = function () {
        if (event.keyCode == 13) {
            cx();
        }
    };

    $(document).ready(function () {
        //客户
        $('#searchStock').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'clientName',
            textField: 'clientName',
            mode: 'remote'
        });
        
        $('#islock').combobox({
            data:[{'label':'是','value':'1'},{'label':'否','value':'0'}],
            valueField: 'value',
            textField: 'label'
        });

        //存货方
        $('#searchStock2').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll",
            valueField: 'clientName',
            textField: 'clientName',
            mode: 'remote'
        });

        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/bis/loading/listjson',
            fit: true,
            fitColumns: true,
            border: false,
            idField: 'orderNum',
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 20,
            pageList: [10, 20, 30, 40, 50],
            singleSelect: false,
            columns: [[
                {field: 'orderNum', title: '订单号', sortable: true, width: 100},
                {field: 'outLinkId', title: '出库联系单', sortable: true, width: 120},
                {field: 'carNum', title: '车牌号', sortable: true, width: 80},
                {field: 'ifBonded', title: '是否保税', sortable: true, width: 80,
                    formatter: function (value, row, index) {
                        var retStr = "否";
                        switch (value) {
                            case '1':
                                retStr = "是";
                                break;
                        }
                        return retStr;
                    }
                },
                {field: 'islock', title: '控车状态', sortable: true, width: 80,
                	  formatter: function (value, row, index) {
                          var retStr = "否";
                          switch (value) {
                              case '1':
                                  retStr = "是";
                                  break;
                          }
                          return retStr;
                      }
                },
                {
                    field: 'mxjs', title: '件数', sortable: true,
                    formatter: function (value, row, index) {

                        var a;

                        $.ajax({
                            async: false,
                            type: 'get',
                            url: '${ctx}/bis/truck/getOrderSumNum/' + row.orderNum,
                            success: function (data) {
                                a = data;
                            }
                        });

                        return a;
                    }
                },
                {field: 'receiverName', title: '收货方', sortable: true, width: 120},
                {field: 'stockName', title: '存货方', sortable: true, width: 120},
                {
                    field: 'orderState', title: '订单状态', sortable: true,width: 60,
                    formatter: function (value, row, index) {
                        var retStr = "";
                        switch (value) {
                            case '1':
                                retStr = "创建";
                                break;
                            case '2':
                                retStr = "分配中";
                                break;
                            case '3':
                                retStr = "拣货中";
                                break;
                            case '4':
                                retStr = "已出库";
                                break;
                        }
                        return retStr;
                    }
                },
                {field: 'planTime', title: '计划出库时间', sortable: true, width: 80},
                {
                    field: 'lastCar',
                    title: '是最后一车',
                    sortable: true,
                    width: 80,
                    formatter: function (value, row, index) {
                        if (1 == value) {
                            return "是"
                        } else {
                            return "否"
                        }
                    }
                },
                {
                    field: 'ifHasCleared',
                    title: '是否清库结算过',
                    sortable: true,
                    width: 80,
                    formatter: function (value, row, index) {
                        if (1 == value) {
                            return "是"
                        } else {
                            return "否"
                        }
                    }
                }
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
        //出库订单状态
        $.ajax({
            type: "GET",
            url: "${ctx}/system/dict/json",
            data: "filter_LIKES_type=loadingState",
            dataType: "json",
            success: function (date) {
                if (date != null && date.rows != null && date.rows.length > 0) {
                    $('#orderState').combobox({
                        data: date.rows,
                        valueField: 'value',
                        textField: 'label'
                    });
                }
            }
        });
    });
    //查询
    function cx() {
        var obj = $("#searchFrom").serializeObject();
        dg.datagrid('load', obj);
    }
    //添加
    function add(title, href) {
        var href = 'bis/loading/create/000000';
        window.parent.mainpage.mainTabs.addModule('出库订单管理', href, 'icon-hamburg-customers');
    }
    //修改
    function upd() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        var href = '${ctx}/bis/loading/update/' + row.orderNum;
        window.parent.mainpage.mainTabs.addModule('出库订单管理', href, 'icon-hamburg-customers');
    }
    //控车
    function lock() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
    	if (row.islock=="1") {
        	parent.$.easyui.messager.show({
                title: "操作提示",
                msg: "该记录已经完成控车操作，请勿重复操作！",
                position: "bottomRight"
            });
            return;
        }
        parent.$.messager.confirm('提示', '控车后车辆无法进行出闸，请确认操作？', function (data) {
            if (data) {
                $.ajax({
                    type: 'post',
                    url: "${ctx}/bis/loading/lock/" + row.orderNum,
                    success: function (data) {
                        if ("error" == data) {
                            parent.$.easyui.messager.alert("订单号：" + row.orderNum + " 控车失败！");
                        } else if ("state" == data) {
                            parent.$.easyui.messager.alert("订单号：" + row.orderNum + "不存在！");
                        } else {
                            successTip(data, dg);
                        }
                    }
                });
            }
        });
    }
    //解控
    function unlock() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
    	if (row.islock=="0") {
        	parent.$.easyui.messager.show({
                title: "操作提示",
                msg: "该记录已经完成解控操作，请勿重复操作！",
                position: "bottomRight"
            });
            return;
        }
        parent.$.messager.confirm('提示', '解控后车辆可以进行出闸，请确认操作？', function (data) {
            if (data) {
                $.ajax({
                    type: 'post',
                    url: "${ctx}/bis/loading/unlock/" + row.orderNum,
                    success: function (data) {
                        if ("error" == data) {
                            parent.$.easyui.messager.alert("订单号：" + row.orderNum + "解控失败！");
                        } else if ("state" == data) {
                            parent.$.easyui.messager.alert("订单号：" + row.orderNum + "不存在！");
                        } else {
                            successTip(data, dg);
                        }
                    }
                });
            }
        });
    }
    //删除
    function del() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
            if (data) {
                $.ajax({
                    type: 'post',
                    url: "${ctx}/bis/loading/delete/" + row.orderNum,
                    success: function (data) {
                        if ("error" == data) {
                            parent.$.easyui.messager.alert("订单号：" + row.orderNum + " 删除失败！");
                        } else if ("state" == data) {
                            parent.$.easyui.messager.alert("订单号：" + row.orderNum + " 状态不允许删除！");
                        } else {
                            successTip(data, dg);
                        }
                    }
                });
            }
        });
    }
    //打开策略选中页面
    function opencl() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        //执行异步校验 该订单是否可以生成装车单
        $.post("${ctx}/bis/truck/iscreate", {"ordid": row.orderNum}, function (data) {
            if (data != null) {
                if ("success" == data.endStr) {
                    createTruck(row.orderNum);
                } else if ("hsuccess" == data.endStr) {
                    parent.$.messager.confirm('提示', '订单号：' + row.orderNum + '已生成过装车单，确定后之前生成的装车单信息将清空，不可还原。您确定要重新生成吗？', function (data) {
                        if (data) {
                            createTruck(row.orderNum);
                        }
                    });
                } else if ("herror" == data.endStr) {
                    parent.$.easyui.messager.alert("订单号：" + row.orderNum + "的装车单状态不允许重新生成装车单 ！");
                } else if("quyang" == data.endStr){
                	parent.$.messager.confirm('提示', '订单号：' + row.orderNum + '中,'+data.message, function (data) {
                        if (data) {
                            createTruck(row.orderNum);
                        }
                    });
                }else {
                    parent.$.easyui.messager.alert("订单号：" + row.orderNum + " 不允许生成装车单！");
                }
            }
        }, "json");
    }
    //打开策略选择弹窗
    function createTruck2(ordid) {
        d = $("#dlg").dialog({
            title: '出库策略选择',
            width: 300,
            height: 300,
            href: '${ctx}/bis/loading/opencl/' + ordid,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    $('#clform').submit();
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }]
        });
    }
    function createTruck(ordid) {
        d = $("#dlg").dialog({
            title: '出库策略选择',
            width: 1000,
            height: 500,
            href: '${ctx}/base/stategy/check/' + ordid,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    $("#mainform").submit();
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }]
        });
    }
    //打开托盘拆分页面
    function openSplitT(ordid) {
        dt = $("#dtg").dialog({
            title: '托盘拆分',
            width: 500,
            height: 300,
            href: '${ctx}/bis/loading/openct/' + ordid,
            maximizable: true,
            modal: true,
            resizable: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    var getTrayIds = document.getElementsByName("newTCode");
                    var ss = "";
                    for (var i = 0; i < getTrayIds.length; i++) {
                        ss = ss + getTrayIds[i].value + " ";
                    }

                    //		做判断托盘号是否存在
                    $.ajax({
                        type: 'post',
                        data: {ss: ss},
                        url: "${ctx}/bis/loading/confirm",
                        success: function (result) {
                            console.log(result);
                            if (result != "") {
                                alert("托盘号：" + result + " 已经存在，请重新输入");
                            } else {
                                $('#ctform').submit();
                            }
                        }
                    });
                }
            }, {
                text: '返回策略选择',
                handler: function () {
                    dt.panel('close');
                    opencl();
                }
            }, {
                text: '取消',
                handler: function () {
                    dt.panel('close');
                }
            }]
        });
    }
    


    //分类监管 核放单
    function mani() {
        var rows = dg.datagrid('getSelections');
        //如果没有选择行记录
        if(rows.length==0) return;
        //选择行记录中 存在多个车号时的判断
        var carNums= [];
        for(var i=0; i<rows.length; i++){
            carNums.push(rows[i].carNum);
        }
        if(carNums.method2().length>1){
            parent.$.messager.show({title: "提示", msg: "申请核放单时只能选择相同的车号", position: "bottomRight" });
            return;
        }
        var orderNums= [];
        for(var i=0; i<rows.length; i++){
            orderNums.push(rows[i].orderNum);
        }
        // //选择行记录中 存在多个保税或非保税时的判断
        // var ifBondedAry= [];
        // for(var i=0; i<rows.length; i++){
        //     ifBondedAry.push(rows[i].ifBonded);
        // }
        // if(ifBondedAry.method2().length>1){
        //     parent.$.messager.show({title: "提示", msg: "申请核放单时只能选择相同的保税或非保税信息", position: "bottomRight" });
        //     return;
        // }
        // //校验选中数据是保税还是非保税
        // var ifBonded = false;
        // if (ifBondedAry[0] =="1" || ifBondedAry[0] =="是"){
        //     ifBonded = true;
        // }
        //
        // if (!ifBonded){
            d=$("#dlg").dialog({
                title: '核放单申请',
                width: 560,
                height: 380,
                href:'${ctx}/bis/loading/mani/'+orderNums,
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
        <%--}else{--%>
        <%--    parent.$.messager.confirm('提示', '您确定要生成保税核放单信息并跳转画面？', function (data) {--%>
        <%--        if (data) {--%>
        <%--            $.ajax({--%>
        <%--                type: 'post',--%>
        <%--                url: "${ctx}/bis/loading/createPassport",--%>
        <%--                success: function (data) {--%>
        <%--                    if ("success" == data) {--%>
        <%--                        var href = 'wms/passPort/list';--%>
        <%--                        window.parent.mainpage.mainTabs.addModule('核放单', href);--%>
        <%--                    } else {--%>
        <%--                        parent.$.easyui.messager.alert("生成保税核放单失败！");--%>
        <%--                    }--%>
        <%--                }--%>
        <%--            });--%>
        <%--        }--%>
        <%--    });--%>
        <%--}--%>
    }

    //生成预约出库信息
    function createReservation() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        $.ajax({
            type: 'post',
            url: "${ctx}/platform/reservationData/outbound/" + row.orderNum,
            success: function (data) {
                if(data=="success"){
                    successTip(data, dg);
                }else{
                    parent.$.messager.show({ title : "提示",msg: "生成预约出库信息失败！", position: "bottomRight" });
                    return;
                }
            }
        });
    }
    
    //数组消除重复的元素
    Array.prototype.method2 = function(){  
        var h={};    //定义一个hash表  
        var arr=[];  //定义一个临时数组  
          
        for(var i = 0; i < this.length; i++){    //循环遍历当前数组  
            //对元素进行判断，看是否已经存在表中，如果存在则跳过，否则存入临时数组  
            if(!h[this[i]]){  
                //存入hash表  
                h[this[i]] = true;  
                //把当前数组元素存入到临时数组中  
                arr.push(this[i]);  
            }  
        }  
        return arr;  
    }  							
</script>
</body>
</html>