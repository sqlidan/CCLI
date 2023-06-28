<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'">
    <form id="searchFrom3" action="">
    </form>
    <form id="mainForm" method="post">
        <div style="height:auto" class="datagrid-toolbar">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true"
               data-options="disabled:false" onclick="submitForm()">保存</a>
            <span class="toolbar-item dialog-tool-separator"></span>

        </div>
        <table class="formTable">
            <tr>


                <input id="id" name="id" class="easyui-validatebox" type="hidden" value="${baseBounded.id}">

                <input type="hidden" id="createdTime" name="createdTime"
                       value="<fmt:formatDate value="${baseBounded.createdTime}"  pattern="yyyy-MM-dd HH:mm:ss" />"/>
                <input type="hidden" id="createdTime" name="createdTime"
                       value="<fmt:formatDate value="${baseBounded.updatedTime}"  pattern="yyyy-MM-dd HH:mm:ss" />"/>

                <%--<td>月台</td>
                <td>
                    <input id="platformName" name="platformName" class="easyui-validatebox" data-options="width: 180" value="${baseBounded.platformName}"  />

                </td>--%>

            </tr>


            <tr>

                <td>客户</td>
                <td><input id="clientName" name="clientName" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" value="${baseBounded.clientName}"/></td>

                <td>提单号</td>
                <td><input id="billNum" name="billNum" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" value="${baseBounded.billNum}"/></td>

            </tr>
            <tr>

                <td>报关单号</td>
                <td><input id="cdNum" name="cdNum" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" value="${baseBounded.cdNum}"/></td>

                <td>所属客服</td>
                <td><input id="customerServiceName" name="customerServiceName" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" value="${baseBounded.customerServiceName}"/>
                </td>


            </tr>
       <%--     <tr>

                <td>货物描述</td>
                <td><input id="itemName" name="itemName" class="easyui-validatebox"
                           data-options="width: 150" value="${baseBounded.itemName}"/></td>

                <td>件数</td>
                <td><input id="piece" name="piece" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" value="${baseBounded.piece}"
                           onkeyup="ischeckNum(this)"/></td>

            </tr>

            <tr>
                <td>总净值</td>
                <td><input id="netWeight" name="netWeight" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" value="${baseBounded.netWeight}"
                           onkeyup="ischeckNum(this)"/></td>

                <td>MR/集装箱号</td>
                <td><input id="ctnNum" name="ctnNum" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" value="${baseBounded.ctnNum}"/></td>

            </tr>--%>

            <tr>

                <td>hs编码</td>
                <td><input id="hsCode" name="hsCode" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" value="${baseBounded.hsCode}"/></td>

                <td>海关品名</td>
                <td><input id="hsItemname" name="hsItemname" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" value="${baseBounded.hsItemname}"/></td>

            </tr>

            <tr>

                <td>账册商品序号</td>
                <td><input id="accountBook" name="accountBook" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" value="${baseBounded.accountBook}"/></td>

                <td>海关库存重量</td>
                <td><input id="hsQty" name="hsQty" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" onkeyup="ischeckNum(this)"
                           value="${baseBounded.hsQty}"/></td>

            </tr>
            <tr>

                <td>申报重量</td>
                <td><input id="dclQty" name="dclQty" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" onkeyup="ischeckNum(this)"
                           value="${baseBounded.dclQty}"/></td>

                <td>申报计量单位</td>
                <td><input id="dclUnit" name="dclUnit" class="easyui-validatebox"
                           data-options="width: 150, required:'required'" value="${baseBounded.dclUnit}"/></td>


            </tr>


            <tr>

                <td>规格</td>
                <td><input id="typeSize" name="typeSize" class="easyui-validatebox"
                           data-options="width: 150" value="${baseBounded.typeSize}"/></td>

<%--
				<input id="cargoLocation" name="cargoLocation" class="easyui-validatebox" type="hidden" value="${baseBounded.cargoLocation}">
--%>
<%--
                <td>库位号</td>
                <td><input id="cargoLocation" name="cargoLocation" class="easyui-validatebox"
                           data-options="width: 500" value="${baseBounded.cargoLocation}" readonly/></td>--%>

            </tr>
            <tr>
                <td>库位号</td>
                <td colspan="3">
                    <textarea rows="3" cols="55" readonly name="cargoLocation" style="font-size: 12px;  font-family: '微软雅黑'">${baseBounded.cargoLocation }   </textarea>
                </td>
            </tr>
            <tr>
                <td>库区</td>
                <td colspan="3">
                    <textarea rows="3" cols="55" readonly name="cargoLocation" style="font-size: 12px;  font-family: '微软雅黑'">${baseBounded.cargoArea }   </textarea>
                </td>
            </tr>



         <%--   <tr>
                <td>入库时间</td>
                <td>
                    <input id="storageDate" required="true" name="storageDate" class="easyui-my97" datefmt="yyyy-MM-dd"
                           data-options="width: 200" , required:'required' value="<fmt:formatDate
                        value="${baseBounded.storageDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
                </td>

            </tr>--%>

        </table>
    </form>
</div>


<script type="text/javascript">
    var dg;
    var d;
    var action = "${action}";
    var a;
    var b = 0;
    var dhs;
    var des;


    console.log(action);
    $(function () {

        selectAjax();
    });


    function selectAjax() {


        var client = '${baseBounded.clientId}';
        $('#clientId').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll?setid=${contract.clientId}",
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote',
            onChange: function (newVal, oldVal) {

            },
            onLoadSuccess: function () {
                if (client != null && client != "") {
                    $('#clientId').combobox("select", client);
                    client = "";
                }
            },
            onHidePanel: function () {
                var _options = $(this).combobox('options');
                var _data = $(this).combobox('getData');/* 下拉框所有选项 */
                var _value = $(this).combobox('getValue');/* 用户输入的值 */
                var _b = false;/* 标识是否在下拉列表中找到了用户输入的字符 */
                for (var i = 0; i < _data.length; i++) {
                    if (_data[i][_options.valueField] == _value) {
                        _b = true;
                        break;
                    }
                }
                if (!_b) {
                    $(this).combobox('setValue', '');
                    //userId="";
                }
            }
        });


    }

    var result = "";

    //保存
    function submitForm() {

        //$("#customsName").val($("#customsId").combobox("getText"));

        //console.log($("#platformId").combobox("getData"));


        if ($("#mainForm").form('validate')) {


            //用ajax提交form
            $.ajax({
                async: false,
                type: 'POST',
                url: "${ctx}/supervision/bonded/" + action,
                data: $('#mainForm').serialize(),

                success: function (msg) {
                    result = msg;
                    if (msg == "success") {
                        parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight"});

                    }


                }
            });
        } else {

            //parent.$.messager.alert("提示","必填项必填，请确认");
        }
    }


    //数字校验
    function ischeckNum(val) {
        if (val.value) {
            if (!isNaN(val.value)) {

            } else {
                parent.$.messager.show({title: "提示", msg: "请输入数字!", position: "bottomRight"});
                $("#" + val.id).val("");
                myfm.isnum.select();
                return false;
            }
        }
    }


</script>
</body>
</html>