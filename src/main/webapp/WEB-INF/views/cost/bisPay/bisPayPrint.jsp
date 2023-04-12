<%@page import="com.haiersoft.ccli.common.utils.NumberToCN" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
    <style type="text/css">
        .formTable {
            border-collapse: collapse;
            overflow: auto;
            border: #000 1px solid;
        }
    </style>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center',title:'业务付款单打印浏览'">
    <div style="padding:5px;height:auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true"
           onclick="print()">打印</a>
    </div>
    <div id="home" class="tab-pane">
        <h5 style=" text-align:center; clear:both;">青岛港怡之航冷链物流有限公司</h5>
        <h3 style=" text-align:center; clear:both;">业务付款申请单</h3>
        <h3 style=" text-align:right; clear:both;">单号：${bisPay.payId}</h3>
        <table id="test" class="formTable" border="1" width="800" cellpadding="3" cellspacing="0"
               style="margin:0 auto;">

            <tr>
                <td colspan="2">申请人</td>
                <td colspan="1">${bisPay.askMan}</td>
                <td colspan="1">申请部门</td>
                <td colspan="1">客服中心</td>
                <td colspan="1">部门主管</td>
                <td colspan="1">管辉</td>
                <td>日期</td>
                <td colspan="3">${bisPay.askDate}</td>
            </tr>

            <tr>
                <td colspan="1">序号</td>
                <td>客户名称</td>
                <td>揽货人</td>
                <td colspan="2">提单号</td>
                <td>收费项目</td>
                <td colspan="2">金额</td>
                <td>代垫代付</td>
                <td>备注</td>
            </tr>

            <c:forEach items="${bisPayInfoList}" var="pay" varStatus="my">
                <tr>
                    <td colspan="1">${my.index+1}</td>
                    <td>${pay.clientName}</td>
                    <td></td><%--${pay.saler}--%>
                    <td colspan="2">${pay.billNum}</td>
                    <td>${pay.feeName}</td>
                    <td colspan="2">${pay.totelPrice}</td>
                    <td>
                    	<c:if test="${pay.helpPay == 0}"></c:if>
                    	<c:if test="${pay.helpPay == 1}">已垫付</c:if>
                    </td>
                    <td>${pay.remark}&nbsp;</td>
                </tr>
            </c:forEach>
            <tr>
                <td colspan="2">金额合计</td>
                <td colspan="8">${money}</td>
            </tr>
            <tr>
                <td colspan="2">大写金额</td>
                <td colspan="8">${CnNumber}</td>
            </tr>
            <tr>
                <td colspan="2">收款人</td>
                <td colspan="3">${bisPay.clientName}</td>
                <td colspan="2">开户银行</td>
                <td colspan="3">${bisPay.bank}</td>
            </tr>
            <tr>
                <td colspan="2">账号</td>
                <td colspan="3">${bisPay.account}</td>
                <td colspan="2">发票到达日期</td>
                <td colspan="3">${bisPay.receipt}</td>
            </tr>
            <tr>
                <td colspan="2">支付方式</td>
                <td colspan="8">
                    <input type="checkbox" id="payWay" value="1" readonly/><label>电汇</label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="checkbox" id="payWay" value="2" readonly/><label>支票 </label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="checkbox" id="payWay" value="3" readonly/><label>现金</label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="checkbox" id="payWay" value="4" readonly/><label>其它 </label>
                </td>
            </tr>
            <tr>
                <td colspan="2">付款周期</td>
                <td colspan="8">
                    <input type="checkbox" id="payCycle" value="1" readonly/><label>当日付款</label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="checkbox" id="payCycle" value="2" readonly/><label>2日付款</label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="checkbox" id="payCycle" value="3" readonly/><label>3日付款 </label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="checkbox" id="payCycle" value="4" readonly/><label>一周付款</label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="checkbox" id="payCycle" value="5" readonly/><label>合同 </label>
                </td>
            </tr>
            <tr>
                <td colspan="2">合作类型</td>
                <td colspan="10">
                    <input type="checkbox" id="teamType" value="1" readonly/><label>合同收费</label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="checkbox" id="teamType" value="2" readonly/><label>公司确认</label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="checkbox" id="teamType" value="3" readonly/><label>其它 </label>
                </td>
                <!-- <td colspan="5">财务确认：</td> -->
            </tr>
            <tr>
                <td colspan="2">情况说明</td>
                <td colspan="8">${bisPay.situation}</td>
            </tr>
            <%-- <tr>
                <td colspan="2">财务</td>
                <td colspan="1">财务主管：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp${bisPay.billManager}</td>
                <td colspan="2">批准人：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp${bisPay.approver}</td>
                <td colspan="2">领款人：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp${bisPay.drawMoney}</td>
                <td colspan="3">附单据：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${bisPay.piece}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;张</td>
            </tr> --%>
        </table>
        <table  border="0" width="800" cellpadding="6" cellspacing="0"
               style="margin:0 auto;">
        	<tr>
        		<td width="160px"></td>
        		<td width="100px"></td>
        		<td ></td>
        	</tr>
        	<tr height="70px">
        		<td width="160px">经办人：</td>
        		<td width="100px"></td>
        		<td>部室负责人：</td>
        	</tr>
        	<tr height="70px">
        		<td width="160px">财务会计：</td>
        		<td width="100px"></td>
        		<td>财务负责人：</td>
        	</tr>
        	<tr height="70px">
        		<td width="160px">中心领导：</td>
        		<td width="100px"></td>
        		<td></td>
        	</tr>    
        </table>
    </div>
</div>

<script type="text/javascript">

    $(function () {
        $("input[id='payWay'][value=${bisPay.payWay}]").attr("checked", true);
        $("input[id='payCycle'][value=${bisPay.payCycle}]").attr("checked", true);
        $("input[id='teamType'][value=${bisPay.teamType}]").attr("checked", true);
    })

    function print() {
        $("#home").jqprint();
    }
</script>
</body>
</html>
