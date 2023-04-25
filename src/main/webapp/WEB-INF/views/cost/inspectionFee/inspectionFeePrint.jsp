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
<div data-options="region:'center',title:'查验作业委托单打印浏览'">
    <div style="padding:5px;height:auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true"
           onclick="print()">打印</a>
    </div>
    <div id="home" class="tab-pane">
        <table id="test" class="formTable" border="1" width="800" cellpadding="3" cellspacing="0"
               style="margin:0 auto;">
            <tr>
              <td colspan="10">
                 <h1 style="text-align:center; clear:both;">查验作业委托单</h1>
                 <h3 align="right">编号：${No}</h3>
              </td>
            </tr>
            <tr> 
                <td style="text-align:center; clear:both;"><font size="3">客户</font></td>
                <td colspan="5"><font size="3">${inspectionFee.clientName}</font></td>
                <td style="text-align:center; clear:both;"><font size="3">日期</font></td>
                <td colspan="3"><font size="3">${dateTime}</font></td>
            </tr>
            <tr>
                <td style="text-align:center; clear:both;"><font size="3">业务</font></td>
                <td colspan="9"><font size="3">${checkType}</font></td>
            </tr>
            <tr>
                <td style="text-align:center; clear:both;"><font size="3">序号</font></td>
                <td colspan="2" style="text-align:center; clear:both;"><font size="3">报检号</font></td>
                <td colspan="4" style="text-align:center; clear:both;"><font size="3">提单号</font></td>
                <td colspan="3" style="text-align:center; clear:both;"><font size="3">箱号</font></td>
                
            </tr>
            <c:forEach items="${infoList}" var="pay" varStatus="my">
                 <tr>
                    <td style="text-align:center; clear:both;"><font size="3">${my.index+1}</font></td>
                    <td colspan="2" style="text-align:center; clear:both;"><font size="3">${inspectionFee.inspectionNum}</font></td>
                    <td colspan="4" style="text-align:center; clear:both;"><font size="3">${pay.billNum}</font></td>
                    <td colspan="3" style="text-align:center; clear:both;"><font size="3">${pay.ctnNum}</font></td>
                 </tr>
             </c:forEach>
             <c:if test="${size<6}">
                    <c:forEach var="i" begin="1" end="${6-size}" varStatus="status">
	                    <tr>
		                    <td style="text-align:center; clear:both;"><font size="3">${size+i}</font></td>
		                    <td colspan="2" style="text-align:center; clear:both;"><font size="3"></font></td>
		                    <td colspan="4" style="text-align:center; clear:both;"><font size="3"></font></td>
		                    <td colspan="3" style="text-align:center; clear:both;"><font size="3"></font></td>
	                    </tr>
                    </c:forEach>
             </c:if>
            <tr>
                <td colspan="2" style="text-align:center; clear:both;"><font size="3">其他作业项目</font></td>
                <td colspan="8">
                    <input type="checkbox" id="payWay" value="1" readonly
                            <c:choose>
		                       <c:when test="${ifPlug==1}">checked</c:when>
		                    <c:otherwise></c:otherwise>
		                </c:choose>/>
                    <label><font size="3">插电</font></label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="checkbox" id="payWay" value="2" readonly
                        <c:choose>
		                       <c:when test="${ifHang==1}">checked</c:when>
		                    <c:otherwise></c:otherwise>
		                </c:choose>/><label><font size="3">吊箱</font></label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="checkbox" id="payWay" value="3" readonly
                       <c:choose>
		                       <c:when test="${ifField==1}">checked</c:when>
		                    <c:otherwise></c:otherwise>
		                </c:choose>
                     /><label><font size="3">场地</font></label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="checkbox" id="payWay" value="4" readonly
                        <c:choose>
		                    <c:when test="${ifHanding==1}">checked</c:when>
		                    <c:otherwise></c:otherwise>
		                </c:choose>
                      /><label><font size="3">搬倒 </font></label>
                </td>
            </tr>
            <tr>
                <td style="text-align:center; clear:both;"><font size="3">金额</font></td>
                <td colspan="9"><font size="3">￥&nbsp;&nbsp;&nbsp;&nbsp;${money}</font></td>
            </tr>
            <tr>
                <td style="text-align:center; clear:both;"><font size="3">大写</font></td>
                <td colspan="9"><font size="3">人民币&nbsp;&nbsp;&nbsp;&nbsp;${CnNumber}</font></td>
            </tr>
            <tr>
                <td style="text-align:center; clear:both;"><font size="3">作业部门</font></td>
                <td colspan="4"><font size="3">客服查验</font></td>
                <td style="text-align:center; clear:both;"><font size="3">经办人</font> </td>
                <td colspan="4"></td>
            </tr>
            <tr>
                <td style="text-align:center; clear:both;"><font size="3">领导审批</font></td>
                <td colspan="4"></td>
                <td style="text-align:center; clear:both;"><font size="3">收款人</font></td>
                <td colspan="4"></td>
            </tr>
            <c:if test="${size<6}">
	            </br>
	            </br>
	            <div style="top:600px;position:fixed;">------------------------------------------------------------------------------------------------------------------</div>
            </c:if>
            <!-- <tr style="height:50px;">
                <td style="text-align:center; clear:both;"><font size="3">备注</font></td>
                <td colspan="9"><font size="3">本单一式三联，财务、客服查验、客服收款各执一联</font></td>
            </tr> -->
        </table>
    </div>
</div>

<script type="text/javascript">
    function print() {
        $("#home").jqprint();
    }
</script>
</body>
</html>
