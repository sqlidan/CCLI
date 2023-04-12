<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
    <script type="text/javascript" src="${ctx}/static/plugins/quartzCron/cron.js" charset="utf-8"></script>
</head>
<body>
<div>
    <input type="hidden" id="yname" value="${client.clientName }"/>
    <input type="hidden" id="ycode" value="${client.clientCode }"/>
    <input type="hidden" id="ytax" value="${client.taxAccount }"/>
    <form id="mainform" action="${ctx }/base/client/${action}" method="post">
        <table class="formTable">
            <tr>
                <td>客户名称：</td>
                <td>
                    <input type="hidden" name="ids" value="${client.ids }"/>
                    <input type="hidden" name="pledgeType" value="${client.pledgeType }"/>
                    <input id="clientName" name="clientName" class="easyui-validatebox" validType="clientName[3,200]"
                           required="true" value="${client.clientName }" data-options="width: 150 ">
                </td>
            </tr>
            <tr>
                <td>客户编号：</td>
                <td>
                    <input id="clientCode" name="clientCode" class="easyui-validatebox" validType="clientCode[3,10]"
                           required="true" value="${client.clientCode }" data-options="width: 150 ">
                </td>
            </tr>
            <tr>
                <td>陆海通平台客户名称：</td>
                <td>
                    <input id="syncClientName" name="syncClientName" class="easyui-validatebox"
                           required="true" value="${client.syncClientName }" data-options="width: 150 ">
                </td>
            </tr>
            <tr>
                <td>陆海通客户名称：</td>
                <td>
                    <input id="realClientName" name="realClientName"  value="${client.realClientName }" >
                </td>
            </tr>
            <tr>
                <td>客户类型：</td>
                <td>
                    <input type="radio" id="clientSort1" name="clientSort" value="0"/><label for="woman">客户</label>
                    <input type="radio" id="clientSort2" name="clientSort" value="1"/><label for="man">供应商</label>
                    <input type="radio" id="clientSort3" name="clientSort" value="2"/><label for="man">装卸队</label>
                </td>
            </tr>
            <tr>
                <td>父客户：</td>
                <td>
                    <input type="hidden" id="fClientName" name="fClientName" value="${fclient.parentName }"/>
                    <input id="fClientId" name="fClientId" class="easyui-combobox" data-options="width: 150 ">
                </td>
            </tr>
            <tr>
                <td>结算日：</td>
                <td>
                    <c:choose>
                        <c:when test="${client.checkDay != null}">
                            <input id="checkDay" name="checkDay" class="easyui-numberbox"
                                   data-options="width: 150 ,required:'required',number:'true',validType:'length[1,2]'"
                                   value="${client.checkDay}"></input>
                        </c:when>
                        <c:otherwise>
                            <input id="checkDay" name="checkDay" class="easyui-numberbox"
                                   data-options="width: 150 ,required:'required',number:'true',validType:'length[1,2]'"
                                   value="26"></input>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td>联系人：</td>
                <td>
                    <input id="contactMan" name="contactMan" class="easyui-validatebox"
                           data-options="width: 150,validType:'length[0,10]'" value="${client.contactMan }">
                </td>
            </tr>
            <tr>
                <td>联系电话：</td>
                <td>
                    <input id="telNum" name="telNum" class="easyui-numberbox"
                           data-options="width: 150 ,number:'true',validType:'length[1,20]'" value="${client.telNum }">
                </td>
            </tr>
            <tr>
                <td>Email：</td>
                <td><input type="text" name="userEMail" value="${client.userEMail }" class="easyui-validatebox"
                           data-options="width: 150,validType:'email'"/></td>
            </tr>
            <tr>
                <td>财务客户码：</td>
                <td>
                    <input id="cwNum" name="cwNum" class="easyui-validatebox"
                           data-options="width: 150 ,validType:'length[1,20]'" value="${client.cwNum }">
                </td>
            </tr>
            <tr>
                <td>财务供应商码：</td>
                <td>
                    <input id="gysNum" name="gysNum" class="easyui-validatebox"
                           data-options="width: 150 ,validType:'length[1,20]'" value="${client.gysNum }">
                </td>
            </tr>
            <tr>
                <td>税号：</td>
                <td>
                    <input id="taxAccount" name="taxAccount" class="easyui-validatebox" data-options="width: 150"
                           validType="taxAccount[0,100]" required="true" value="${client.taxAccount }">
                </td>
            </tr>
            <tr>
                <td>纳税人：</td>
                <td>
                    <input id="taxpayer" name="taxpayer" class="easyui-validatebox"
                           data-options="width: 150,validType:'length[0,50]'" value="${client.taxpayer }">
                </td>
            </tr>
            <tr>
                <td>揽货人：</td>
                <td>
                    <input id="saler" name="saler" class="easyui-validatebox"
                           data-options="width: 150,validType:'length[0,15]'" value="${client.saler }">
                </td>
            </tr>
            <tr>
                <td>人民币银行：</td>
                <td>
                    <input id="rmbBank" name="rmbBank" class="easyui-validatebox"
                           data-options="width: 150,validType:'length[0,30]'" value="${client.rmbBank }">
                </td>
            </tr>
            <tr>
                <td>人民币账号：</td>
                <td>
                    <input id="rmbAccount" name="rmbAccount" class="easyui-validatebox"
                           data-options="width: 150,validType:'length[0,25]'" value="${client.rmbAccount }">
                </td>
            </tr>

            <tr>
                <td>美元银行：</td>
                <td>
                    <input id="usdBank" name="usdBank" class="easyui-validatebox"
                           data-options="width: 150,validType:'length[0,30]'" value="${client.usdBank }">
                </td>
            </tr>
            <tr>
                <td>美元账号：</td>
                <td>
                    <input id="usdAccount" name="usdAccount" class="easyui-validatebox"
                           data-options="width: 150,validType:'length[0,25]'" value="${client.usdAccount }">
                </td>
            </tr>
            <tr>
                <td>地址：</td>
                <td><textarea rows="3" cols="41" class="easyui-validatebox" data-options="validType:'length[0,50]'"
                              name="address" style="font-size: 12px;font-family: '微软雅黑'">${client.address}</textarea>
                </td>
            </tr>
            <tr>
                <td>客服人员编码：</td>
                <td>
                    <input id="serviceCode" name="serviceCode" class="easyui-validatebox"
                           data-options="width: 150,validType:'length[0,15]'" value="${client.serviceCode }">
                </td>
            </tr>
            <tr>
                <td>网上查询权限：</td>
                <td>
                    <input type="radio" id="limit1" name="limit" value="0"/>无</label>
                    <input type="radio" id="limit2" name="limit" value="1"/>有</label>
                </td>
            </tr>
            <tr>
                <td>客户级别</td>
                <td>
                    <select id="customerLevel" name="customerLevel" class="easyui-combobox"
                            data-options="width:150">
                    </select>
                </td>
            </tr>
            <tr>
                <td>备注：</td>
                <td><textarea rows="3" cols="41" class="easyui-validatebox" data-options="validType:'length[0,50]'"
                              name="note" style="font-size: 12px;font-family: '微软雅黑'">${client.note}</textarea></td>
            </tr>

        </table>
    </form>
</div>

<script type="text/javascript">
    var action = "${action}";
    var cc = "${client}"
    $(document).ready(function () {
        var messagestr = "请修改输入值";
        //自定义validatebox的验证方法
        $.extend($.fn.validatebox.defaults.rules, {
            clientName: {
                validator: function (value, param) {
                    var flag = false;
                    var rules = $.fn.validatebox.defaults.rules;
                    //更新时，值和原值一致返回true，否则进行校验
                    if ("update" == action && value == $("#yname").val()) {
                        flag = true;
                    } else {
                        //校验用户名是否唯一
                        $.ajax({
                            type: 'GET',
                            url: '${ctx}/base/client/checkClientName',
                            data: 'clientName=' + value,
                            dataType: 'text',
                            async: false,
                            success: function (data) {
                                if (data == 'true') {
                                    flag = true;
                                } else {
                                    flag = false;
                                }
                            }
                        });
                    }//end if

                    if (!rules.length.validator(value, param)) {
                        rules.rangelength.message = "请输入一个长度介于 {0} 和 {1} 之间的字符串";
                        return false;
                    }
                    return flag;
                },
                message: '输入的客户名已存在！'
            },
            clientCode: {
                validator: function (value, param) {
                    var flag = false;
                    if ("update" == action && value == $("#ycode").val()) {
                        flag = true;
                    } else {
                        //校验用户名是否唯一
                        $.ajax({
                            type: 'GET',
                            url: '${ctx}/base/client/checkClientCode',
                            data: 'clientCode=' + value,
                            dataType: 'text',
                            async: false,
                            success: function (data) {
                                if (data == 'true') {
                                    flag = true;
                                } else {
                                    flag = false;
                                }
                            }
                        });
                    }//end if
                    return flag;
                },
                message: '输入的客户编码已存在！'
            },
            taxAccount: {
                validator: function (value, param) {
                    var flag = false;
                    if ("update" == action && value == $("#ytax").val()) {
                        flag = true;
                    } else {
                        //校验用户名是否唯一
                        $.ajax({
                            type: 'GET',
                            url: '${ctx}/base/client/checkClientTax',
                            data: 'tax=' + value,
                            dataType: 'text',
                            async: false,
                            success: function (data) {
                                if (data == 'true') {
                                    flag = true;
                                } else {
                                    flag = false;
                                }
                            }
                        });
                    }//end if
                    return flag;
                },
                message: '输入的税号已存在！'
            }
        });

        //客户类型 添加
        if (action == 'create') {
            $("input[name='clientSort'][value=0]").attr("checked", true);
        } else if (action == 'update') {
            $("input[name='clientSort'][value=${client.clientSort}]").attr("checked", true);
        }

        console.log(cc);

        //网上权限添加
        if (action == 'create') {

            $("input[name='limit'][value=0]").attr("checked", true);

        } else if (action == 'update') {

            if ('${client.limit}' != "") {
                $("input[name='limit'][value=${client.limit}]").attr("checked", true);
            } else {
                $("input[name='limit'][value=0]").attr("checked", true);
            }

        }

        //父客户
        var getReceiverOrgId = "${fclient.parentId}";
        $('#fClientId').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll?tim=1&setid=${fclient.parentId}",
            valueField: 'ids',
            textField: 'clientName',
            mode: 'remote',
            onLoadSuccess: function () {
                if (getReceiverOrgId != null && getReceiverOrgId != "") {
                    $('#fClientId').combobox("select", Number(getReceiverOrgId));
                    getReceiverOrgId = "";
                }
            }
        });
        //


        //客户优先级
        var rank = '${client.customerLevel}';
        $('#customerLevel').combobox({
            method: "GET",
            url: "${ctx}/base/rank/getRankAll?setid=${client.customerLevel}",
            valueField: 'rank',
            textField: 'rank',
            mode: 'remote',
            onLoadSuccess: function () {
                if (rank != null && rank != "") {
                    $('#customerLevel').combobox("select", rank);
                    rank = "";
                }
            }
        });
    });


    //提交表单
    $('#mainform').form({

        onSubmit: function () {

            $("#fClientName").val($('#fClientId').combobox("getText"));
            var isValid = $(this).form('validate');
            console.log(isValid,'11111')
            return isValid;	// 返回false终止表单提交
        },
        success: function (data) {

            successTip(data, dg, d);
        }
    });
</script>
</body>
</html>
