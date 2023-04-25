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
<div data-options="region:'center',title:'跺卡打印浏览'">
    <div style="padding:5px;height:auto">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true"
           onclick="print()">打印</a>
    </div>
    <div id="home" class="tab-pane">
        <table id="test" class="formTable" border="1" width="800" cellpadding="3" cellspacing="0"
               style="margin:0 auto;">
            <tr>
              <td colspan="10">
                 <h1 style="text-align:center; clear:both;">货垛标识卡</h1>
              </td>
            </tr>
            <tr> 
                <td style="text-align:center; clear:both;"><font size="3">客户名称</font></td>
                <td colspan="9"><font size="3">${asn.stockName}</font></td>
            </tr>
            
            <tr> 
                <td style="text-align:center; clear:both;"><font size="3">提单号</font></td>
                <td colspan="4"><font size="3">${asn.billNum}</font></td>
                <td style="text-align:center; clear:both;"><font size="3">报关单号</font></td>
                <td colspan="4"><font size="3">${enterInfo.bgdh}</font></td>
            </tr>
            
            <tr> 
                <td style="text-align:center; clear:both;"><font size="3">原产国</font></td>
                <td colspan="4"><font size="3">${enter.ycg}</font></td>
                <td style="text-align:center; clear:both;"><font size="3">货物状态</font></td>
                <td colspan="4">
                     <input type="checkbox" id="isBonded" value="1" readonly
                            <c:choose>
		                       <c:when test="${asn.isBonded==1}">checked</c:when>
		                    <c:otherwise>
		                    </c:otherwise>
		                </c:choose>/>
                    <label><font size="3">保税</font></label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="checkbox" id="isBonded" value="0" readonly
                        <c:choose>
		                       <c:when test="${asn.isBonded==0}">checked</c:when>
		                    <c:otherwise></c:otherwise>
		                </c:choose>/><label><font size="3">非保税</font></label>&nbsp;&nbsp;&nbsp;&nbsp;
                </td>
            </tr>
            <tr> 
                <td style="text-align:center; clear:both;"><font size="3">集装箱号</font></td>
                <td colspan="4"><font size="3">${asn.ctnNum}</font></td>
                <td style="text-align:center; clear:both;"><font size="3">仓库区位</font></td>
                <td colspan="4"><font size="3">${location}</font></td>
            </tr>
            
            <tr> 
                <td style="text-align:center; clear:both;"><font size="3">是否MSC</font></td>
                <td colspan="4">
                   <input type="checkbox" id="ifMacAdmit" value="1" readonly
                            <c:choose>
		                       <c:when test="${ifMacAdmit==1}">checked</c:when>
		                    <c:otherwise>
		                    </c:otherwise>
		                </c:choose>/>
                    <label><font size="3">是</font></label>&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="checkbox" id="ifMacAdmit" value="0" readonly
                        <c:choose>
		                       <c:when test="${ifMacAdmit!=1}">checked</c:when>
		                    <c:otherwise></c:otherwise>
		                </c:choose>/><label><font size="3">否</font></label>&nbsp;&nbsp;&nbsp;&nbsp;                    
                </td>
                <td style="text-align:center; clear:both;"><font size="3">货物品名</font></td>
                <td colspan="4"><font size="3">${enterInfo.cargoName}</font></td>
            </tr>
            
            <tr> 
                <td style="text-align:center; clear:both;"><font size="3">入库日期</font></td>
                <td colspan="4"><font size="3">${enterTime}</font></td>
                <td style="text-align:center; clear:both;"><font size="3">厂号</font></td>
                <td colspan="4"><font size="3"></font></td>
            </tr>
            
            <tr> 
                <td style="text-align:center; clear:both;"><font size="3">入库件数</font></td>
                <td colspan="4"><font size="3">${num}</font></td>
                <td style="text-align:center; clear:both;"><font size="3">重量（吨）</font></td>
                <td colspan="4"><font size="3">${znet}</font></td>
            </tr>
            
            <tr> 
                <td style="text-align:center; clear:both;"><font size="3">出库日期</font></td>
                <td colspan="4"><font size="3"></font></td>
                <td style="text-align:center; clear:both;"><font size="3">剩余件数</font></td>
                <td colspan="4"><font size="3"></font></td>
            </tr>
            
            <tr> 
                <td style="text-align:center; clear:both;"><font size="3">出库件数</font></td>
                <td colspan="4"><font size="3"></font></td>
                <td style="text-align:center; clear:both;"><font size="3">重量（吨）</font></td>
                <td colspan="4"><font size="3"></font></td>
            </tr>
            
            <tr style="height:120px;" align="left" valign="top">
                <td colspan="10" style="text-align:left; clear:both;">
                  <font size="3">备注：1.垛卡系统自助打印</font>
                </td>
            </tr>
	         </br>
	         </br>
	         <!-- <div style="top:600px;position:fixed;">------------------------------------------------------------------------------------------------------------------</div> -->
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
