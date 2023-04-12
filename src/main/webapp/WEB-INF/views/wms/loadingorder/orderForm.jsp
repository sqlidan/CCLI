<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">

<form id="loadingform" action="${ctx}/bis/loading/${action}" method="post">

    <div data-options="region:'north',split:true,border:false" style="height:150px">

        <div class="easyui-layout" class="datagrid-toolbar" data-options="fit:true">

            <div data-options="region:'north',split:false,border:false" style="height:auto" class="datagrid-toolbar">
                <shiro:hasPermission name="bis:loadingma:save">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true"
                       onclick="save()">保存</a>
                    <span class="toolbar-item dialog-tool-separator"></span>
                </shiro:hasPermission>

            </div>

            <div data-options="region:'south',split:false,border:false" style="height:10px;"></div>
            <div data-options="region:'west',split:false,border:false" style="width:10px;"></div>
            <div data-options="region:'east',split:false,border:false" style="width:10px;"></div>
            <div data-options="region:'center',split:false,border:false" style="padding-top: 5px;">
                <table>
                    <tr>
                        <td style="width:50px; ">订单号：</td>
                        <td>
                            <input type="hidden" id="orderState" name="orderState" value="${order.orderState}" />
                            <input type="text" id="orderNum" name="orderNum" class="easyui-validatebox"
                                   readonly="readonly" data-options="width:150,required:'required'"
                                   value="${order.orderNum}"/></td>
                        <td style="width:80px; ">联系单号：</td>
                        <td>
                            <c:choose>
                                <c:when test="${order.isEdite == 1}">
                                    <input type="text" name="outLinkId" class="easyui-validatebox" readonly="readonly"
                                           data-options="width:150" value="${order.outLinkId}"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="text" id="outLinkId" name="outLinkId" class="easyui-searchbox"
                                           data-options="prompt:'请选择联系单',required:'required',width:150,searcher:doSearch,validType:'length[4,20]'"
                                           value="${order.outLinkId}"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>收货方：</td>
                        <td>
                            <input type="hidden" id="receiverName" name="receiverName" value="${order.receiverName}"/>
                            <c:choose>
                                <c:when test="${order.isEdite == 1}">
                                    <input type="text" id="receiverId" name="receiverId" class="easyui-validatebox"
                                           readonly="readonly" data-options="width:150" value="${order.receiverName}"/>
                                </c:when>
                                <c:otherwise>
                                    <select class="easyui-combobox" id="receiverId" name="receiverId"
                                            data-options="width:150,required:'required'">
                                        <option value=""></option>
                                    </select>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td style="color:red">计费出库日期：</td>
                        <td>
                            <c:choose>
                                <c:when test="${order.isEditeTwo == 1 || order.ifHasCleared == 1}">
                                    <input id="planTime" type="text" name="planTime" class="easyui-validatebox" readonly="readonly"
                                           data-options="width:150"
                                           value="<fmt:formatDate value="${order.planTime}"/>"/>
                                </c:when>
                                <c:otherwise>
                                    <input id="planTime" name="planTime" type="text" class="easyui-my97"
                                           datefmt="yyyy-MM-dd"
                                            <c:choose>
                                                <c:when test="${order.isClear == 1}">
                                                    data-options="width: 150,prompt: '计费按此日期为准'"
                                                </c:when>
                                                <c:otherwise>
                                                    data-options="width: 150,prompt: '计费按此日期为准'"
                                                </c:otherwise>
                                            </c:choose>
                                           value="<fmt:formatDate value="${order.planTime}"/>"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <td>车牌号：</td>
                        <td>
                            <input type="text" name="carNum" id="carNum" class="easyui-validatebox"
                                   data-options="width:150" value="${order.carNum}"/>
                        </td>
                        <td>计划出库日期：</td>
                        <td>
                            <input id="planDateNoPay" type="text" name="planDateNoPay" class="easyui-my97" 
                                           data-options="width:150" datefmt="yyyy-MM-dd"
                                           value="<fmt:formatDate value="${order.planDateNoPay}"/>"/>
                        </td>
                         <td id="ttd" <c:if test="${order.isClear == 0}"> style="display: none;" </c:if>>是最后一车：</td>
                         <td id="ctd" <c:if test="${order.isClear == 0}"> style="display: none;" </c:if>>
								
						 <input type="checkbox" id="lastCar" name="lastCar" data-options="width: 180"
                         value="1"  <c:if test="${order.lastCar == 1}"> checked="checked" </c:if> />
								
                        </td>
                        
                        <td>是否控车：</td>
                        <td>
						 <input type="checkbox" id="islock" name="islock" data-options="width: 180"
                           value="${order.islock}"  <c:if test="${order.islock == 1}"> checked="checked" </c:if> />
                        </td>
                    </tr>
                    <tr style="height: 5px;"></tr>
                    <tr style="height: 5px;">
                        <td>备注：</td>
                        <td colspan="7">
                            <textarea rows="2" name="remark" cols="110"
                                      <c:if test="${order.isEdite == 1}">readonly="readonly" </c:if>
                                      class="easyui-validatebox" data-options="validType:'length[1,50]'"
                                      style="font-size: 12px;font-family: '微软雅黑'">${order.remark}</textarea>
                        </td>
                        <div id="infoListdiv"><!--明细参数提交--></div>
                        <div id="delListdiv"><!--删除明细参数提交--></div>
                    </tr>
                    <tr>
                        <td>明细出库件数合计：</td>
                        <td>
                            <input id="pieceInfo" class="easyui-validatebox" data-options="width: 180" readonly
                                   style="background:#eee"/>
                        </td>
                        <td>明细净重合计：</td>
                        <td>
                            <input id="netInfo" class="easyui-validatebox" data-options="width: 180" readonly
                                   style="background:#eee"/>
                        </td>
                        <td>明细毛重合计：</td>
                        <td>
                            <input id="grossInfo" class="easyui-validatebox" data-options="width: 180" readonly
                                   style="background:#eee"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div data-options="region:'center',split:true,border:false,title:'明细列表'">
        <div id="tb" style="padding:5px;height:auto">
            <div>
                <c:if test="${order.isEdite == 0}">
                    <shiro:hasPermission name="bis:loadingma:add">
                        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                           onclick="addOrderIfno()">添加</a>
                        <span class="toolbar-item dialog-tool-separator"></span>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="bis:loadingma:delete">
                        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
                           data-options="disabled:false" onclick="del()">删除</a>
                        <span class="toolbar-item dialog-tool-separator"></span>
                    </shiro:hasPermission>
                    <!--
                    <span class="toolbar-item dialog-tool-separator"></span>
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-basket-put" plain="true" data-options="disabled:false" onclick="importInfo()">导入出库联系单明细</a>
                    -->
                </c:if>
            </div>
        </div>
        <table id="dg"></table>
    </div>
</form>
<div id="dlg"></div>
<form id="checknumform" action="${ctx}/bis/loading/check" method="post">
    <div id="skuNumdiv" style="display: none;"><!--质押校验提交--></div>
    <div id="outLinkdiv" style="display: none;">
        <input type="hidden" id="outlinkid" name="outLinkId" readonly="readonly"/>
    </div>
</form>
<script type="text/javascript">
    var dg;
    var d;
    var model = '${action}';
    var trNum = 0;//用于记录行号
    var addrows;
    $(document).ready(function () {
        gridDG();
        //收货方
        var getstockIn = '${order.receiverId}';
        $('#receiverId').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${order.receiverId}",
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote',
            onLoadSuccess: function () {
                if (getstockIn != null && getstockIn != "") {
                    $('#receiverId').combobox("select", getstockIn);
                    getstockIn = "";
                }
            }
        });

        //表单提交校验
        $('#loadingform').form({
            onSubmit: function () {
                var isValid = $(this).form('validate');
                if (isValid) {
                    //仓库名称赋值
                    var faild = $('#receiverId').combobox("getText");
                    $('#receiverName').val(faild);
                    //给明细div添加参数
                    $("#infoListdiv").html("");
                    $($('#dg').datagrid('getRows')).each(function (i) {
                        $("#infoListdiv").append("<input type=\"hidden\" name=\"skuList\" value=\"" + $(this).attr("SKU_ID") + "\" >");
                        $("#infoListdiv").append("<input type=\"hidden\" name=\"billList\" value=\"" + $(this).attr("BILL_NUM") + "\" >");
                        $("#infoListdiv").append("<input type=\"hidden\" name=\"ctnList\" value=\"" + $(this).attr("CTN_NUM") + "\" >");
                        $("#infoListdiv").append("<input type=\"hidden\" name=\"asnList\" value=\"" + $(this).attr("ASN") + "\" >");
                        $("#infoListdiv").append("<input type=\"hidden\" name=\"foodList\" value=\"" + $(this).attr("CARGO_NAME") + "\" >");
                        $("#infoListdiv").append("<input type=\"hidden\" name=\"intoList\" value=\"" + $(this).attr("ENTER_STATE") + "\" >");
                        $("#infoListdiv").append("<input type=\"hidden\" name=\"peacList\" value=\"" + $(this).attr("PIECE") + "\" >");
                        $("#infoListdiv").append("<input type=\"hidden\" name=\"netList\" value=\"" + $(this).attr("NET_SINGLE") + "\" >");
                        $("#infoListdiv").append("<input type=\"hidden\" name=\"grossList\" value=\"" + $(this).attr("GROSS_SINGLE") + "\" >");
                        $("#infoListdiv").append("<input type=\"hidden\" name=\"rkList\" value=\"" + $(this).attr("RK_NUM") + "\" >");
                        $("#infoListdiv").append("<input type=\"hidden\" name=\"mscList\" value=\"" + $(this).attr("MSC_NUM") + "\" >");
                        $("#infoListdiv").append("<input type=\"hidden\" name=\"lotList\" value=\"" + $(this).attr("LOT_NUM") + "\" >");
                        $("#infoListdiv").append("<input type=\"hidden\" name=\"remarkList\" value=\"" + $(this).attr("REMARK1") + "\" >");
                        $("#infoListdiv").append("<input type=\"hidden\" name=\"typeSizeList\" value=\"" + $(this).attr("TYPE_SIZE") + "\" >");
                    });
                }
                return isValid;	// 如果表格为空,返回false终止表单提交
            },
            success: function (data) {
                // 车牌号
                var value = $("#carNum").val();
                $.ajax({
                    type: "post",
                    url: "${ctx}/bis/loading/update",
                    data: {value: value},
                    dataType: "text",
                    success: function (date) {
                        if ("success" == data) {
                            parent.$.easyui.messager.show({title: "操作提示", msg: "保存成功！", position: "bottomRight"});
                        } else {
                            parent.$.easyui.messager.alert("保存失败！");
                        }
                    }
                });
            }
        });
        //库存数量校验
        $('#checknumform').form({
            onSubmit: function () {
                $("#outlinkid").val($("#outLinkId").searchbox('getValue'));
                var url = "${ctx }/bis/loading/check?time=" + Math.random();
                $('#checknumform').attr("action", url);
                return true;
            },
            success: function (data) {
                var jsonObj = eval('(' + data + ')');
                if ("success" == jsonObj.retStr) {
                    var dgrows = $('#dg').datagrid('getRows');
                    var rowsLength = dgrows.length;
                    var abcd;
                    var bug = 0;
                    for (var i = addrows.length - 1; i >= 0; i--) {
                        obj = addrows[i];
                        var j = parseInt(obj['NUM']);
                        abcd = $("#piece" + Number(obj['NUM'])).val();
                        aabb = $("#remark" + Number(obj['NUM'])).val();
                        if (typeof(abcd) == "undefined" || abcd == "" || bug == 1) {
                            index = $('#dg').datagrid('getRowIndex', obj['LAB']);
                            if (index >= 0) {
                                var newrow2 = dgrows[index];
                                abcd = newrow2.PIECE;
                                $("#piece" + Number(obj['NUM'])).val(abcd);
                            }
                        }
                        if (addrows.length > 1 && i > 0 && j == 0) {
                            bug = 1;
                        }
                        if ("" != abcd && abcd > 0) {
                            addTBRow(obj, abcd, aabb);
                        }
                    }
                    for (var i = rowsLength; i > 0; i--) {
                        $("#delListdiv").append("<input type=\"hidden\" name=\"delList\" value=\"" + dgrows[i - 1]["LAB"] + "\" >");
                        $('#dg').datagrid('deleteRow', i - 1);
                    }
                    d.panel('close');
                } else {
                    parent.$.easyui.messager.alert("SKU:" + jsonObj.sku + " 质押数量加出库数量大于库存量！");
                }
            }
        });
        $("input[name='outLinkId']").attr("readonly", "readonly");
    });

    function gridDG() {
        var getId = 'order.orderNum';
        if (getId != null && getId != "") {
            dg = $('#dg').datagrid({
                method: "get",
                url: '${ctx}/bis/loadinginfo/alljson',
                fit: true,
                fitColumns: false,//水平滚动
                border: false,
                idField: 'LAB',
                striped: true,
                pagination: false,
                rownumbers: true,
                pageNumber: 1,
                pageSize: 1000,
                pageList: [1000],
                singleSelect: true,
                columns: [[
                    {field: 'STOCK_NAME', title: '存货方', sortable: false, width: 300},
                    {field: 'BILL_NUM', title: '提单号', sortable: false, width: 150},
                    {field: 'CTN_NUM', title: '箱号', sortable: false, width: 150},
                    {field: 'ASN', title: 'ASN', sortable: false, width: 150},
                    {field: 'SKU_ID', title: 'SKU', sortable: false, width: 150},
                    {field: 'MSC_NUM', title: 'msc号', sortable: true, width: 150},
                    {field: 'LOT_NUM', title: 'lot号', sortable: true, width: 150},
                    {field: 'TYPE_SIZE', title: '规格', sortable: true, width: 150},
                    {field: 'RK_NUM', title: '入库号', sortable: false, width: 150},
                    {field: 'CARGO_NAME', title: '品名', sortable: false, width: 250},
                    {
                        field: 'ENTER_STATE', title: '入库类型', sortable: false, width: 80,
                        formatter: function (value, row, index) {
                            if (value == 0) {
                                return '成品'
                            } else if (value == 1) {
                                return '货损'
                            }
                        }
                    },
                    {field: 'PIECE', title: '出库件数', sortable: false, width: 100},
                    {field: 'REMARK1', title: '备注', sortable: false, width: 100},
                    {field: 'NET_SINGLE', title: '单净', hidden: true},
                    {field: 'GROSS_SINGLE', title: '单毛', hidden: true},
                    {field: 'LAB', title: 'lab', hidden: true},
                    {field: 'NUM', title: '', hidden: true},
                    {field: 'ADDORDEL', title: '', hidden: true}
                ]],
                queryParams: {
                    filter_EQS_loadingPlanNum: '${order.orderNum}'
                },
                onLoadSuccess: function () {
                    var rows = $('#dg').datagrid('getRows');
                    var getState = $("#asnState").val();
                    insertSum();
                },
                enableHeaderClickMenu: true,
                enableHeaderContextMenu: true,
                enableRowContextMenu: false,
                toolbar: '#tb'
            });
        }
    }

    function insertSum() {
        var rows = $('#dg').datagrid('getRows');
        var pieces = 0;
        var net = 0;
        var gross = 0;
        for (var i = 0; i < rows.length; i++) {
            pieces += Number(rows[i]['PIECE']);
            net += Number(rows[i]['NET_SINGLE']) * Number(rows[i]['PIECE']);
            gross += Number(rows[i]['GROSS_SINGLE']) * Number(rows[i]['PIECE']);
        }
        ;
        $("#pieceInfo").val(pieces);
        $("#netInfo").val(net.toFixed(2));
        $("#grossInfo").val(gross.toFixed(2));

    }

    //联系单查询
    function doSearch() {
        d = $("#dlg").dialog({
            title: '出库联系单查询',
            width: 650,
            height: 500,
            href: '${ctx}/bis/loading/outLinklist',
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    var row = dglx.datagrid('getSelected');
                    if (rowIsNull(row)) return;
                    //判断联系单号是否改变
                    var ylink = $("#outLinkId").searchbox('getValue');
                    $.ajax({
						async: false,
						type:'get',
						url:"${ctx}/wms/outstockinfo/checkRank" ,
						data : {"outLinkId":row["OUT_LINK_ID"]},
						success: function(data){
							if(data != "success"){
								parent.$.easyui.messager.show({title: "操作提示", msg: "此客户剩余库存已超出警戒线！无法制作出库订单！", position: "bottomRight"});
								$("#outLinkId").searchbox('clear');
								d.panel('close');
							} else{
								addrows = $('#dg').datagrid('getRows');
			                    var rowsLength = addrows.length;
			                    if (!rowIsNull(addrows) && ylink != "" && ylink != row["OUT_LINK_ID"]) {
			                        parent.$.messager.confirm('提示', '改变联系单号已经添加的出库明细将清空，您确定要改变联系单号吗？', function (data) {
			                            if (true == data) {
			                                $("#skuNumdiv").html("");//情况之前添加的数据，用于校验添加的SKU数量是否符合库存>输入+动态质押
			                                //清空已经添加的明细信息，添加明细删除记录
			                                for (var i = rowsLength; i > 0; i--) {
			                                    $("#delListdiv").append("<input type=\"hidden\" name=\"delList\" value=\"" + addrows[i - 1]["LAB"] + "\" >");
			                                    $('#dg').datagrid('deleteRow', i - 1);
			                                }
			                                addFromVals(row)
			                                d.panel('close');
			                            }
			                        });
			                    } else {
			                        addFromVals(row)
			                        var getisClear = row["IF_CLEAR_STORE"];
			                        if (1 == getisClear) {
			                            $("#ttd").show();
			                            $("#ctd").show();
			                            $("#lastCar").prop("checked",true);
			                        } else {
			                            $("#ttd").hide();
			                            $("#ctd").hide();
			                            $("#lastCar").removeAttr("checked");
			                        }
			                        d.panel('close');
			                    }  //end if 改变了联系单号
							} // end ajax success if	
						}// end ajax success
					});	 // end ajax
                } //end handler
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }]
        });
    }
    //选中出库联系单，追加表单信息
    function addFromVals(row) {
        //联系单
        $("#outLinkId").searchbox('setValue', row["OUT_LINK_ID"]);
        //收货方
        var nstockIn = row["RECEIVER_ID"];
        $('#receiverId').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=" + nstockIn,
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote',
            onLoadSuccess: function () {
                if (nstockIn != null && nstockIn != "") {
                    $('#receiverId').combobox("select", nstockIn);
                }
                getstockIn = "";
            }
        });
    }
    //open明细添加
    function addOrderIfno() {
        var getLinkId = $("#outLinkId").searchbox("getValue");
        if (getLinkId == null || getLinkId == "") {
            parent.$.easyui.messager.show({title: "操作提示", msg: "请先选择出库联系单！", position: "bottomRight"});
            return false;
        } else {
            d = $("#dlg").dialog({
                title: '出库订单明细选择',
                width: 1200,
                height: 500,
                href: '${ctx}/bis/loading/openinfos/' + getLinkId + "/" + $("#orderNum").val(),
                maximizable: true,
                modal: true,
                buttons: [{
                    text: '确认',
                    handler: function () {
                        addrows = $("#infodg").datagrid("getSelections");
                        var obj;
                        var abc;
                        var index;
                        var rows = $('#dg').datagrid('getRows');
                        // 需要判断输入件数要小于（库存数量+质押量）
                        if (addrows != null && addrows.length > 0) {
                            var bug = 0;
                            $("#skuNumdiv").html("");
                            for (var i = addrows.length - 1; i >= 0; i--) {
                                var row = addrows[i];
                                var j = parseInt(row['NUM']);
                                abc = $("#piece" + row['NUM']).val();
                                if (typeof(abc) == "undefined" || abc == "" || bug == 1) {
                                    index = $('#dg').datagrid('getRowIndex', row['LAB']);
                                    if (index >= 0) {
                                        var newrow = rows[index];
                                        abc = newrow.PIECE;
                                    }
                                }
                                if (addrows.length > 1 && i > 0 && j == 0) {
                                    bug = 1;
                                }
                                if ($("#SKU" + row["SKU_ID"]).length > 0) {
                                    var getVal = $("#VAL" + row["SKU_ID"]).val();
                                    $("#VAL" + row["SKU_ID"]).val(Number(getVal) + Number(abc));
                                } else {
                                    $("#skuNumdiv").append("<input type=\"text\" id=\"SKU" + row["SKU_ID"] + "\" name=\"skuList\" value=\"" + row["SKU_ID"] + "\" >");
                                    $("#skuNumdiv").append("<input type=\"text\" id=\"VAL" + row["SKU_ID"] + "\" name=\"peacList\" value=\"" + abc + "\" >");
                                }
                            }
                            //提交数据校验库存
                            $("#checknumform").submit();
                        }
                    }
                }, {
                    text: '取消',
                    handler: function () {
                        d.panel('close');
                    }
                }]
            });
        } //end if
    }
    //校验明细是否已经添加过
    function checkInfoIsHave(infos) {
        var rows = $('#dg').datagrid('getRows');
        for (var i = 0; i < rows.length; i++) {
            if (infos == rows[i]["LAB"]) {
                return true;
            }
        }
        return false;
    }
    //执行出库订单明细添加一行
    function addTBRow(rowObj, abcd, aabb) {
        var rows = $('#dg').datagrid('getRows');
        $('#dg').datagrid('insertRow', {
            index: rows.length + 1,
            row: {
                STOCK_NAME: rowObj["STOCK_NAME"],
                BILL_NUM: rowObj["BILL_NUM"],
                CTN_NUM: rowObj["CTN_NUM"],
                ASN:rowObj["ASN"],
                SKU_ID: rowObj["SKU_ID"],
                CARGO_NAME: rowObj["CARGO_NAME"],
                ENTER_STATE: rowObj["ENTER_STATE"],
                PIECE: abcd,
                NET_SINGLE: rowObj["NET_SINGLE"],
                GROSS_SINGLE: rowObj["GROSS_SINGLE"],
                LAB: rowObj["LAB"],
                MSC_NUM: rowObj["MSC_NUM"],
                LOT_NUM: rowObj["LOT_NUM"],
                TYPE_SIZE: rowObj["TYPE_SIZE"],
                RK_NUM: rowObj["RK_NUM"],
                REMARK1: aabb,
                ADDORDEL: 1
            }
        });
        insertSum();
    }
    //遍历表单获取行号
    function getIndex(id) {
        var nIndex = '10000000';
        var rows = $('#dg').datagrid('getRows');
        for (var i = 0; i < rows.length; i++) {
            if (id == rows[i]["LAB"]) {
                nIndex = i;
                break;
            }
        }
        return nIndex;
    }
    //删除
    function del() {
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)) return;
        parent.$.messager.confirm('提示', '您确定要删除选中的行吗？', function (data) {
            if (true == data) {
                $("#delListdiv").append("<input type=\"hidden\" name=\"delList\" value=\"" + row["LAB"] + "\" >");
                $('#dg').datagrid('deleteRow', getIndex(row["LAB"]));
                insertSum();
            }
        });
    }

    //出库订单保存
    function save() {
    	var isEditeTwo="${order.isEditeTwo}";
    	var ifHasCleared="${order.ifHasCleared}";
    	var planTime;
    	if(isEditeTwo=='1'||ifHasCleared=='1'){
    		planTime=$("#planTime").val();
    	}else{
    		planTime=$("#planTime").datebox("getValue");
    	}
    	if ($("#lastCar").is(":checked")&&planTime== "") {
        	parent.$.easyui.messager.show({
                title: "操作提示",
                msg: "选择了最后一车的出库订单请填写计费出库日期后再进行保存！",
                position: "bottomRight"
            });
            return;
        } else {
        	var orderId = '${order.orderNum}';
        	if($("#islock").is(":checked")){
        		$("#islock").val("1");
        	}
            $.ajax({
                type: "GET",
                url: "${ctx}/bis/truck/ifmakeloading/" + orderId,
                dataType: "text",
                success: function (date) {
                    $("#loadingform").submit();
                }
            });
        }
    }

    //导入出库联系单明细
    function importInfo() {
        var orderId = '${order.orderNum}';
        var outLinkId = $("input[name='outLinkId']").val();
        $.ajax({
            type: "GET",
            url: "${ctx}/bis/loading/ifsave/" + orderId,
            dataType: "text",
            success: function (date) {
                if (date == "success") {
                    parent.$.easyui.messager.show({title: "操作提示", msg: "出库订单未保存！", position: "bottomRight"});
                    return;
                } else {
                    $.ajax({
                        type: "GET",
                        url: "${ctx}/bis/loading/importinfo",
                        data: {"orderId": orderId, "outLinkId": outLinkId},
                        dataType: "text",
                        success: function (date) {
                            if (date == "success") {
                                parent.$.easyui.messager.show({title: "操作提示", msg: "导入成功！", position: "bottomRight"});
                                gridDG();
                            } else {
                                parent.$.easyui.messager.show({
                                    title: "操作提示",
                                    msg: "对应出库联系单无明细，导入失败！",
                                    position: "bottomRight"
                                });
                            }
                        }
                    });
                }
            }
        });
    }
    
    
</script>
</body>
</html>