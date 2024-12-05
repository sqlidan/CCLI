<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>

</head>
<body>
<div>
    <form id="mainform2" action="${ctx}/wms/enterStockInfo/${action}" method="post">
        <div id="tb" style="padding:5px;height:auto">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
               onclick="openSKUList()">sku库导入</a>
        </div>
        <table class="formTable">
            <tr>
                <td>入库联系单id：</td>
                <td>
                    <input type="hidden" id="feeId" name="feeId" value="${bisEnterStock.feeId}"/>
                    <input type="hidden" id="ifUseTruck" name="ifUseTruck" value="${bisEnterStock.ifUseTruck}"/>
                    <input type="hidden" id="id" name="id" value="${id}"/>
                    <input type="hidden" id="operator" name="operator" class="easyui-validatebox" value="${enterStockInfo.operator}" readonly />
                    <input type="hidden" id="operateTime" name="operateTime"   value="<fmt:formatDate value="${enterStockInfo.operateTime}"  pattern="yyyy-MM-dd HH:mm:ss" />"/>
                    <input id="linkIdF" name="linkId" class="easyui-validatebox" data-options="width: 150, required:'required'" value="${enterStockInfo.linkId}" readonly/>
                </td>
            </tr>
            <tr>
                <td>提单号：</td>
                <td>
                    <input id="itemNumF" name="itemNum" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" value="${enterStockInfo.itemNum}" readonly/>
                </td>
            </tr>
            <tr>
                <td>MR：</td>
                <td><input id="ctnNumF" name="ctnNum" type="text" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" value="${enterStockInfo.ctnNum}"/></td>
            </tr>
            <tr>
                <td>铅封号：</td>
                <td>
                <input id="carNum" name="carNum" type="text" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" value="${enterStockInfo.carNum}"/>
                </td>
            </tr>
            <tr>
                <td>报关单号：</td>
                <td><input id="bgdh" name="bgdh" type="text" class="easyui-validatebox"
                           data-options="width: 150" value="${enterStockInfo.bgdh}"/></td>
            </tr>
            <tr>
                <td>原产国：</td>
                <td><input id="ycg" name="ycg" type="text" class="easyui-validatebox"
                           data-options="width: 150" value="${enterStockInfo.ycg}"/></td>
            </tr>
            
            <tr>
                <td>报关申报时间：</td>
                <td>      
                   <input id="bgdhDate" name="bgdhDate" class="easyui-my97" datefmt="yyyy-MM-dd"
                         data-options="width: 150"
                      value="<fmt:formatDate value="${enterStockInfo.bgdhDate}" pattern="yyyy-MM-dd" />"/>
                </td>
            </tr>
            <%-- <tr>
                <td>派车费目：</td>
                <td>
                   <input type="hidden" id="feeName" name="feeName" />
                   <select id="feeCode" name="feeCode"  class="easyui-combobox"
                           data-options="width:150,prompt:'派车费目'" value="${enterStockInfo.feeCode}"/>     
                 </td>
            </tr> --%>
            <tr>
                <td>是否查验：</td>
                <td>
                <select id="ifCheck" name="ifCheck"  class="easyui-combobox"
                           data-options="width:150,prompt:'是否查验',valueField: 'id',textField:'text',value:'${enterStockInfo.ifCheck}',data: [{id:'0',text:'否'},{id:'1',text:'是'}]"/>
                </td>
            </tr>
            <%-- <tr>
                <td>是否缠膜：</td>
                <td>
                    <select id="ifWrap" name="ifWrap"  class="easyui-combobox"
                           data-options="width:150,prompt:'是否缠膜',valueField: 'id',textField:'text',value:'${enterStockInfo.ifWrap}',data: [{id:'0',text:'否'},{id:'1',text:'是'}]"/>
                </td>
            </tr>
            <tr>
                <td>是否套袋：</td>
                <td>      
                    <select id="ifBagging" name="ifBagging"  class="easyui-combobox"
                           data-options="width:150,prompt:'是否套袋',valueField: 'id',textField:'text',value:'${enterStockInfo.ifBagging}',data: [{id:'0',text:'否'},{id:'1',text:'是'}]"/>
                </td>
            </tr> --%>
            
             <tr>
                <td>是否倒箱：</td>
                <td>
                     <select id="ifBack" name="ifBack"  class="easyui-combobox"
                           data-options="width:150,prompt:'是否倒箱',valueField: 'id',textField:'text',value:'${enterStockInfo.ifBack}',data: [{id:'0',text:'否'},{id:'1',text:'是'}]"/>
                </td>
            </tr>
            
            <tr>
                <td>倒车箱号：</td>
                <td><input id="dctnNum" name="dctnNum" type="text" class="easyui-validatebox"
                           data-options="width: 150" value="${enterStockInfo.dctnNum}" /></td>
            </tr>
            
            <tr>
                <td>倒车车牌号：</td>
                <td><input id="dcarNum" name="dcarNum" type="text" class="easyui-validatebox"
                           data-options="width: 150" value="${enterStockInfo.dcarNum}" /></td>
            </tr>
            <tr>
                <td>品名：</td>
                <td><input id="cargoNameF" name="cargoName" type="text" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" value="${enterStockInfo.cargoName}"/></td>
            </tr>
            <tr>
                <td>账册商品序号：</td>
                <td><input id="accountBook" name="accountBook" type="text" class="easyui-validatebox"
                           data-options="width: 150" value="${enterStockInfo.accountBook}" /></td>
            </tr>
            <tr>
                <td>HS编码：</td>
                <td>
					<input id="textFilter" name="hsCode" class="easyui-combox" data-options="width: 150, required:'required'" value="${enterStockInfo.hsCode}"/>
                </td>
            </tr>
           <tr>
                <td>海关品名：</td>
                <td>
					<input id="itemnameFilter" name="hsItemname" class="easyui-combox" data-options="width: 150, required:'required'" value="${enterStockInfo.hsItemname}"/>
                </td>
            </tr>
            <tr>
                <td>SKU：</td>
                <td>
                    <input type="text" id="skuF" name="sku" class="easyui-validatebox"
                           data-options="width: 150,prompt: '请点击SKU库导入'" value="${enterStockInfo.sku}"
                           style="background:#eee" readonly/>
                </td>
            </tr>
            <tr>
                <td>大类：</td>
                <td>
                    <input id="bigTypeName" name="bigTypeName" type="hidden" value="${enterStockInfo.bigTypeName}"/>
                    <select id="bigType" name="bigType" type="text" class="easyui-combobox"
                            data-options="width:150, required:'required'">
                    </select>
                </td>
            </tr>
            <tr>
                <td>小类：</td>
                <td>
                    <input id="littleTypeName" name="littleTypeName" type="hidden"
                           value="${enterStockInfo.littleType}"/>
                    <select id="littleType" name="littleType" class="easyui-combobox"
                            data-options="width:150, required:'required'">
                    </select>
                </td>
            </tr>
            <tr>
                <td>规格：</td>
                <td>
                    <input id="typeSizeF" name="typeSize" class="easyui-validatebox" data-options="width: 150"
                           value="${enterStockInfo.typeSize}"/>
                </td>
            </tr>
            <tr>
                <td>箱数：</td>
                <td>
                    <input id="pieceF" name="piece" class="easyui-validatebox"
                           data-options="width:150, required:'required'" value="${enterStockInfo.piece}"
                           oninput="getWeight()" onpropertychange="getWeight()"
                           onkeyup="value=value.replace(/[^\d]/g,'')"/>
                </td>
            </tr>
            <tr>
                <td>总净重：</td>
                <td>
                    <input type="text" id="netWeightF" name="netWeight" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" onkeyup="ischeckNum()"
                           value="${enterStockInfo.netWeight}" oninput="getNetSingle()"
                           onpropertychange="getNetSingle()" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                </td>
            </tr>
            <tr>
                <td>总毛重：</td>
                <td>
                    <input type="text" id="grossWeightF" name="grossWeight" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" onkeyup="ischeckNum()"
                           value="${enterStockInfo.grossWeight}" oninput="getGrossSingle()"
                           onpropertychange="getGrossSingle()" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                </td>
            </tr>
            <tr>
                <td>单净：</td>
                <td>
                    <input type="text" id="netSingleF" name="netSingle" class="easyui-validatebox"
                           data-options="width: 150" onkeyup="ischeckNum()" value="${enterStockInfo.netSingle}"
                           style="background:#eee" readonly/>
                </td>
            </tr>
            <tr>
                <td>单毛：</td>
                <td>
                    <input type="text" id="grossSingleF" name="grossSingle" class="easyui-validatebox"
                           data-options="width: 150" onkeyup="ischeckNum()" value="${enterStockInfo.grossSingle}"
                           style="background:#eee" readonly/>
                </td>
            </tr>
            <tr>
                <td>重量单位：</td>
                <td>
                    <input id="unitsF" name="units" type="hidden" class="easyui-validatebox" value="1"
                           data-options="width: 150"/>
                    <input id="unitsFFF" class="easyui-validatebox" value="千克" data-options="width: 150"
                           style="background:#eee" readonly/>
                </td>
            </tr>
            <tr>
                <td>order号：</td>
                <td><input id="orderNum" name="orderNum" type="text" class="easyui-validatebox"
                           data-options="width: 150" value="${enterStockInfo.orderNum}" /></td>
            </tr>
            <tr>
                <td>项目号：</td>
                <td>
                    <input type="text" id="projectNumF" name="projectNum" class="easyui-validatebox"
                           data-options="width: 150" value="${enterStockInfo.projectNum}"/>
                </td>
            </tr>
            <tr>
                <td>MSC：</td>
                <td>
                    <input type="text" id="mscNum" name="mscNum" class="easyui-validatebox" data-options="width: 150 "
                           value="${enterStockInfo.mscNum}" <c:if test="${action =='update'}"> readonly style="background:#eee"</c:if> />
                </td>
            </tr>
            <tr>
                <td>LOT：</td>
                <td>
                    <input type="text" id="lotNum" name="lotNum" class="easyui-validatebox" data-options="width: 150 "
                           value="${enterStockInfo.lotNum}" <c:if test="${action =='update'}"> readonly style="background:#eee"</c:if> />
                </td>
            </tr>
            <tr>
                <td>入库号：</td>
                <td>
                    <input type="text" id="rkNumF" name="rkNum" class="easyui-validatebox" data-options="width: 150 "
                           value="${enterStockInfo.rkNum}"/>
                </td>
            </tr>
            <tr>
                <td>生产时间</td>
                <td>
                    <input id="makeTime" name="makeTime" class="easyui-my97" data-options="width: 180"
                           value="<fmt:formatDate  value="${enterStockInfo.makeTime}"  pattern="yyyy-MM-dd" />"/>
                </td>
            </tr>
            <tr>
                <td>单价：</td>
                <td>
                    <input type="text" id="price" name="price" class="easyui-validatebox" data-options="width: 150"
                           value="${enterStockInfo.price}" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                </td>
            </tr>
            <tr>
                <td>捕捞船名：</td>
                <td>
                    <input type="text" id="shipNum" name="shipNum" class="easyui-validatebox" data-options="width: 150 "
                           value="${enterStockInfo.shipNum}"/>
                </td>
            </tr>
            
        </table>
    </form>
</div>

<script type="text/javascript">

    var action = "${action}";
    var linkIdN = "${linkId}";
    var feeId="${bisEnterStock.feeId}";
    var ifUseTruck="${bisEnterStock.ifUseTruck}";
    var sign = 0;
    //用户 添加修改区分
    if (action == 'create') {
        $("#linkIdF").val(linkIdN);
        cItemNum();
    } else if (action == 'update' || action == 'copy') {
        addClassType('${enterStockInfo.bigType}');
        $("#cargoNameF").attr('readonly', 'readonly');
        $("#cargoNameF").css('background', '#eee');
        $("#typeSizeF").attr('readonly', 'readonly');
        $("#typeSizeF").css('background', '#eee');
        $("#netWeightF").attr('readonly', 'readonly');
        $("#netWeightF").css('background', '#eee');
        $("#grossWeightF").attr('readonly', 'readonly');
        $("#grossWeightF").css('background', '#eee');
        $("#netSingleF").attr('readonly', 'readonly');
        $("#netSingleF").css('background', '#eee');
        $("#grossSingleF").attr('readonly', 'readonly');
        $("#grossSingleF").css('background', '#eee');
        $("#shipNum").attr('readonly', 'readonly');
        $("#shipNum").css('background', '#eee');
        sign = 1;
    } else if (action == 'copy') {
        //清空id,code
        $("#id").val("");
    }
/*     if(ifUseTruck=="1"){
	    $.ajax({
	        type: "GET",
	        url: "${ctx}/base/schemeInfo/getSchemeNum/${bisEnterStock.feeId}",
	        dataType: "json",
	        success: function (date) {
	            if (date != null && date.length > 0) {
	                $('#feeCode').combobox({
	                    data: date,
	                    value: '${enterStockInfo.feeCode}',
	                    valueField: 'feeCode',
	                    textField: 'feeName',
	                    onSelect: function () {
	                        $("#feeName").val($('#feeCode').combobox("getText"));
	                    },
	                    editable: true
	                });
	            }
	        }
	    });
    } */

    $.ajax({
        type: "GET",
        url: "${ctx}/base/product/listjson",
        data: "filter_EQI_printId=0",
        dataType: "json",
        success: function (date) {
            if (date != null && date.rows.length > 0) {
                $('#bigType').combobox({
                    data: date.rows,
                    value: '${enterStockInfo.bigType}',
                    valueField: 'id',
                    textField: 'pName',
                    onSelect: function () {
                        addClassType2($('#bigType').combobox("getValue"));
                        $("#bigTypeName").val($('#bigType').combobox("getText"));
                    },
                    editable: true
                    <c:if test="${action == 'update'}">
                    , disabled: false
                    </c:if>
                });
            }

        }
    });

    //添加货品小类
    function addClassType(printeId) {
        $.ajax({
            type: "GET",
            url: "${ctx}/base/product/listjson",
            data: "filter_EQI_printId=" + printeId,
            dataType: "json",
            success: function (date) {
                if (date != null && date.rows.length > 0) {
                    $('#littleType').combobox({
                        data: date.rows,
                        value: '${enterStockInfo.littleType}',
                        valueField: 'id',
                        textField: 'pName',
                        onSelect: function () {
                            $("#littleTypeName").val($('#littleType').combobox("getText"));
                        },
                        editable: true
                        <c:if test="${action == 'update'}">
                        , disabled: false
                        </c:if>
                    });
                }
            }
        });
    }

    function addClassType2(printeId) {
        $.ajax({
            type: "GET",
            url: "${ctx}/base/product/listjson",
            data: "filter_EQI_printId=" + printeId,
            dataType: "json",
            success: function (date) {
                if (date != null && date.rows.length > 0) {
                    $('#littleType').combobox({
                        data: date.rows,
                        value: '',
                        valueField: 'id',
                        textField: 'pName',
                        onSelect: function () {
                            $("#littleTypeName").val($('#littleType').combobox("getText"));
                        },
                        editable: true
                        <c:if test="${action == 'update'}">
                        , disabled: false
                        </c:if>
                    });
                }

            }
        });
    }


    //数字校验
    function ischeckNum() {
        var num = document.getElementById('priceBase').value;
        if (num) {
            if (!isNaN(num)) {

            } else {
                parent.$.messager.show({title: "提示", msg: "请输入数字!", position: "bottomRight"});
                $("#priceBaseF").val("");
                myfm.isnum.select();
                return false;
            }
        }
    }
    //下拉列表ajax
    function selectXL(skuId) {
        $.ajax({
            type: "GET",
            url: "${ctx}/wms/enterStockInfo/selSku",
            data: "",
            dataType: "json",
            success: function (date) {
                for (var i = 0; i < date.length; i++) {
                    $('#skuF').combobox({
                        data: date,
                        value: skuH,
                        valueField: 'skuId',
                        textField: 'producingArea',
                        editable: true,
                        onChange: function (newVal, oldVal) {
                            cSku(newVal);
                        }
                    });
                }
            }
        });
    }

    //根据入库联系单号获得提单号
    function cItemNum() {
        $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/wms/enterStockInfo/changeItemNum",
            data: "linkId=" + linkIdN,
            dataType: "text",
            success: function (date) {
                $("#itemNumF").val(date);
            }
        });
    }

    //根据skuId获得sku基本信息
    function cSku(skuId) {
        $("#skuF").val(skuId);
        $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/wms/enterStockInfo/cSku",
            data: "sku=" + skuId,
            dataType: "json",
            success: function (date) {
                $("#cargoNameF").val(date[0]);
                $("#typeSizeF").val(date[1]);
                $("#netSingleF").val(date[2]);
                $("#grossSingleF").val(date[3]);
//          $("#unitsF").val(date[4]);
                $("#bigTypeName").val(date[6]);
                $("#littleTypeName").val(date[8]);
                $("#mscNum").val(date[9]);
                $("#lotNum").val(date[10]);
                $("#projectNumF").val(date[11]);
                $("#rkNumF").val(date[12]);
                $("#shipNum").val(date[13]);
                $("#netWeightF").val("");
                $("#grossWeightF").val("");

                $.ajax({
                    type: "GET",
                    url: "${ctx}/base/product/listjson",
                    data: "filter_EQI_printId=0",
                    dataType: "json",
                    success: function (msg) {
                        if (msg != null && msg.rows.length > 0) {
                            $('#bigType').combobox({
                                data: msg.rows,
                                value: date[5],
                                valueField: 'id',
                                textField: 'pName',
                                editable: false,
                                disabled: true
                            });
                        }
                    }
                });

                $.ajax({
                    type: "GET",
                    url: "${ctx}/base/product/listjson",
                    data: "filter_EQI_printId=" + date[5],
                    dataType: "json",
                    success: function (msg) {
                        if (msg != null && msg.rows.length > 0) {
                            $('#littleType').combobox({
                                data: msg.rows,
                                value: date[7],
                                valueField: 'id',
                                textField: 'pName',
                                editable: false,
                                disabled: true
                            });
                        }

                    }
                });

                $("#cargoNameF").attr('readonly', 'readonly');
                $("#cargoNameF").css('background', '#eee');
                $("#typeSizeF").attr('readonly', 'readonly');
                $("#typeSizeF").css('background', '#eee');
                $("#netWeightF").attr('readonly', 'readonly');
                $("#netWeightF").css('background', '#eee');
                $("#grossWeightF").attr('readonly', 'readonly');
                $("#grossWeightF").css('background', '#eee');
                $("#netSingleF").attr('readonly', 'readonly');
                $("#netSingleF").css('background', '#eee');
                $("#grossSingleF").attr('readonly', 'readonly');
                $("#grossSingleF").css('background', '#eee');

                $("#mscNum").attr('readonly', 'readonly');
                $("#mscNum").css('background', '#eee');
                $("#lotNum").attr('readonly', 'readonly');
                $("#lotNum").css('background', '#eee');
                $("#projectNumF").attr('readonly', 'readonly');
                $("#projectNumF").css('background', '#eee');
                $("#rkNumF").attr('readonly', 'readonly');
                $("#rkNumF").css('background', '#eee');
                $("#shipNum").attr('readonly', 'readonly');
                $("#shipNum").css('background', '#eee');

            }
        });
    }

    //选择sku时计算总净总毛，新建SKU时计算单毛单净
    function getWeight() {
        if (sign == 1) {
            if ($("#skuF").val() == "") {
                parent.$.messager.show({title: "提示", msg: "请先选择SKU！", position: "bottomRight"});
                $("#pieceF").val("");
                return;
            }
            var piece = $("#pieceF").val();
            var netWeight = (piece * $("#netSingleF").val()).toFixed(4);
            var grossWeight = (piece * $("#grossSingleF").val()).toFixed(4);
            $("#netWeightF").val(netWeight);
            $("#grossWeightF").val(grossWeight);
        } else {
            if ($("#netWeightF").val() != "" && $("#pieceF").val() != "") {
                $("#netSingleF").val(($("#netWeightF").val() / $("#pieceF").val()).toFixed(8));
            }
            if ($("#grossWeightF").val() != "" && $("#pieceF").val() != "") {
                $("#grossSingleF").val(($("#grossWeightF").val() / $("#pieceF").val()).toFixed(8));
            }
        }
    }

    //计算单净
    function getNetSingle() {
        if (sign == 0) {
            if ($("#netWeightF").val() != "" && $("#pieceF").val() != "") {
                $("#netSingleF").val(($("#netWeightF").val() / $("#pieceF").val()).toFixed(8));
            }
        }
    }

    //计算单毛
    function getGrossSingle() {
        if (sign == 0) {
            if ($("#grossWeightF").val() != "" && $("#pieceF").val() != "") {
                $("#grossSingleF").val(($("#grossWeightF").val() / $("#pieceF").val()).toFixed(8));
            }
        }
    }

    //SKU库添加
    function openSKUList() {

        dsku = $("#skudlg").dialog({
            title: 'SKU选择',
            width: 650,
            height: 500,
            href: '${ctx}/wms/enterStockInfo/skulist',
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    var rows = $('#skudg').datagrid('getSelected');
                    var skuId = rows.skuId;
                    cSku(skuId);
                    sign = 1;
                    dsku.panel('close');
                }
            }, {
                text: '取消',
                handler: function () {
                    dsku.panel('close');
                }
            }]
        });

    }

    //提交表单
    $('#mainform2').form({
        onSubmit: function () {
            var isValid = $(this).form('validate');
            return isValid; // 返回false终止表单提交
        },
        success: function (data) {
            if(data=="success"){
                parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight"});
            } else {
                parent.$.easyui.messager.alert(data);
            }
        }
    });
    $(function() {
        $('#textFilter').combobox({
            mode: 'remote',  //模式： 远程获取数据
            url: '${ctx}/base/hscode/lisths',  //远程数据请求地址  
            valueField: 'code',　　//value对应的属性字段
            textField: 'code'　　　 //text对应的属性字段
       });
        $('#itemnameFilter').combobox({
            mode: 'remote',  //模式： 远程获取数据
            url: '${ctx}/base/itemname/lisths',  //远程数据请求地址  
            valueField: 'cargoName',　　//value对应的属性字段
            textField: 'cargoName'　　　 //text对应的属性字段
       });
    })



    
</script>
</body>
</html>