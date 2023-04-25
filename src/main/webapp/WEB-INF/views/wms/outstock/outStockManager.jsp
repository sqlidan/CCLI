<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<style type="text/css">
body{ 
}
.button.green{
	border:1px solid #F1F1F1;
	box-shadow: 0 1px 2px #F1F1F1 inset,0 -1px 0 #F1F1F1 inset,0 -2px 3px #F1F1F1 inset;
	background: -webkit-linear-gradient(top,#F1F1F1,#F1F1F1);
	background: -moz-linear-gradient(top,#F1F1F1,#F1F1F1);
	background: linear-gradient(top,#F1F1F1,#F1F1F1);
	width: 70px;
	height: 28px;
}
</style>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">   
<div data-options="region:'center'">
	<form id="downForm" method="post" >
	</form>
		<form id="searchFrom2" action="">
      	    <input type="hidden" id="cx" name="filter_LIKES_outLinkId" class="easyui-validatebox" />
		</form>
	<form id="mainForm"  method="post">
	<div style="padding:5px;height:auto" class="datagrid-toolbar">
		<!--	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="clearIt()">添加</a>
		  	<span class="toolbar-item dialog-tool-separator"></span> -->
		  	<shiro:hasPermission name="wms:outstockma:save">
			    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="submitForm()">保存</a>
			   	<span class="toolbar-item dialog-tool-separator"></span>
		   	</shiro:hasPermission>
		   	<shiro:hasPermission name="wms:outstockma:delete">
			    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteIt()">删除</a>
			    <span class="toolbar-item dialog-tool-separator"></span>
		    </shiro:hasPermission>
		    <shiro:hasPermission name="wms:outstockma:okPass">
			    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="okPass()">审核</a>
			    <span class="toolbar-item dialog-tool-separator"></span>
		    </shiro:hasPermission>
		     <shiro:hasPermission name="wms:outstockma:noPass">
			    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="noPass()">取消审核</a>
			    <span class="toolbar-item dialog-tool-separator"></span>
		    </shiro:hasPermission>
		    <shiro:hasPermission name="wms:outstockma:export">
			    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportIt()">导出</a>
			    <span class="toolbar-item dialog-tool-separator"></span>
		    </shiro:hasPermission>
		    <shiro:hasPermission name="report:inStock:otep">
	       	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-white-acrobat" plain="true" onclick="exportOtePdf()">导出OTE</a>
		       	<span class="toolbar-item dialog-tool-separator"></span>
	       	</shiro:hasPermission>
		    <shiro:hasPermission name="wms:outstockma:makeOrder">
			    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="makeOrder()">制作出库订单</a>
			    <span class="toolbar-item dialog-tool-separator"></span>
		    </shiro:hasPermission>
		    <shiro:hasPermission name="wms:outstockma:adjust">
			    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-credit" plain="true" onclick="outStockAdjust()">费用</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			
			<shiro:hasPermission name="wms:outstockma:adkfjust">
			    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-credit" plain="true" onclick="outStockAdkfjust()">费用</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
	</div>
	<table class="formTable" >
		<tr>
			<td>审核状态</td>
			<td>
			    <input id="blSign" type="hidden" value="0"/>
			    <input id="auditingState"  name="auditingState"  type="hidden"  value="${bisOutStock.auditingState}" />
				<input id="auditingStateC"  class="easyui-validatebox" data-options="width: 180"  value=""  readonly />
			</td>
			<td>联系单号</td>
			<td>
				<input id="outLinkId" name="outLinkId" class="easyui-validatebox" data-options="width: 180"  value="${bisOutStock.outLinkId}" readonly/>
			</td>
			<td>收货方</td>
			<td>
				<input type="hidden" id="receiverIdS" name="receiverIdS" value="${bisOutStock.receiverId}"/>
				<input type="hidden" id="receiver" name="receiver" />
				<select id="receiverId" name="receiverId" class="easyui-combobox"" data-options="width: 180">
				</select>
			</td>
			<td>收货方联系人</td>
			<td>
				<input id="receiverLinker" name="receiverLinker" class="easyui-validatebox"" data-options="width: 180" value="${bisOutStock.receiverLinker}"/>
			</td>
		</tr>
		<tr>
			<td>结算单位</td>
			<td>
				<input type="hidden" id="settleOrgIdS" name="settleOrgIdS" value="${bisOutStock.settleOrgId}"/>
				<input type="hidden" id="settleOrg" name="settleOrg" />
				<select id="settleOrgId" name="settleOrgId" class="easyui-combobox"" data-options="width: 180, required:'required'" />
			</td>
			<td>存货方</td>
			<td >
			    <input type="hidden" id="stockInIdS" name="stockInIdS" value="${bisOutStock.stockInId}" />
			    <input type="hidden" id="stockIn" name="stockIn" />
				<select class="easyui-combobox" id="stockInId" name="stockInId" data-options="width:180" >
				</select>
			</td>
			<td>报关代报公司</td>
			<td>
				<input type="hidden" id="customsCompany" name="customsCompany"/>
				<select id="customsCompanyId" name="customsCompanyId" class="easyui-combobox"   data-options="width: 180" >
				</select>
			</td>
			<td>报关号</td>
			<td>
				<input id="cdNum" name="cdNum" class="easyui-validatebox"  data-options="width: 180" value="${bisOutStock.cdNum}" />
			</td>
		</tr>
		<tr>
			<td>箱量</td>
			<td>
				<input id="boxNum" name="boxNum" class="easyui-validatebox" data-options="width: 180" value="${bisOutStock.boxNum}" onkeyup="ischeckNum(this)"/> 
			</td>
			<td>装箱日期</td>
			<td>
				<input id="boxDate" name="boxDate" class="easyui-my97" data-options="width: 180" 
				value="<fmt:formatDate  value="${bisOutStock.boxDate}"  pattern="yyyy-MM-dd" />"/> 
			</td>
			<td>报检代报公司</td>
			<td>
				<input type="hidden" id="ciqCompany" name="ciqCompany"/>
				<select id="ciqCompanyId" name="ciqCompanyId" class="easyui-combobox"   data-options="width: 180" >
				</select>
			</td>
			<td>报检号</td>
			<td>
				<input id="ciqNum" name="ciqNum" class="easyui-validatebox"  data-options="width: 180" value="${bisOutStock.ciqNum}" />
			</td>
		</tr>
		<tr>
			<td>
			    非保税货物
				<span class="easyui-checkbox">
				<input id="ifBonded" name="ifBonded" type="checkbox" data-options="width: 180" value="1" 
				<c:choose><c:when test="${bisOutStock.ifBonded == 1}">checked</c:when><c:otherwise></c:otherwise></c:choose> > 
				</input>
				</span>
			</td>
			<td>是否审批
					<span class="easyui-checkbox">
						<input id="ifRecord" name="ifRecord" type="checkbox" data-options="width: 180" value="1"
		                <c:choose>
		                       <c:when test="${bisOutStock.ifRecord == 1}">checked</c:when>
		                    <c:otherwise></c:otherwise>
		                </c:choose> />
	                </span>
            </td>
			<td>是否清库</td>
			<td>
				<span class="easyui-checkbox">
				<input type="checkbox" id="ifClearStore" name="ifClearStore" data-options="width: 180"  value="1" 
				<c:choose><c:when test="${bisOutStock.ifClearStore == 1}">checked</c:when><c:otherwise></c:otherwise></c:choose> >
				</input>
				</span>
			</td>
			<td>是否重贴标签</td>
			<td>
				<span class="easyui-checkbox">
				<input id="ifRepeatLable" name="ifRepeatLable" type="checkbox" data-options="width: 180"  value="1" 
				<c:choose><c:when test="${bisOutStock.ifRepeatLable == 1}">checked</c:when><c:otherwise></c:otherwise></c:choose> /> 
				</span>
			</td>
			<td>重贴标签数量</td>
			<td>
				<input id="repeatNum" name="repeatNum" class="easyui-validatebox"  data-options="width: 180" value="${bisOutStock.repeatNum}" maxlength="7"/>
			</td>
			
		</tr>
		<tr>
			<td>是否派车</td>
			<td>
				<span class="easyui-checkbox">
					<input type="checkbox" id="ifUseTruck" name="ifUseTruck" data-options="width: 180" value="1" 
					<c:choose><c:when test="${bisOutStock.ifUseTruck == 1}">checked</c:when><c:otherwise></c:otherwise></c:choose> />
				</span>
			</td> 
			<td>是否缠膜打包</td>
			<td>
				<span class="easyui-checkbox">
				<input id="ifWrap" name="ifWrap" type="checkbox" data-options="width: 180"  value="1" 
				<c:choose><c:when test="${bisOutStock.ifWrap == 1}">checked</c:when><c:otherwise></c:otherwise></c:choose>/>
				</span>
			</td>
			<td>是否称重</td>
			<td>
				<span class="easyui-checkbox">
				  <input type="checkbox" id="ifWeigh" name="ifWeigh"   data-options="width: 180" value="1" 
				  <c:choose><c:when test="${bisOutStock.ifWeigh == 1}">checked</c:when><c:otherwise></c:otherwise></c:choose>	/>
				</span>
			</td> 
			<td>称重数量</td>
			<td>
				<input id="weighNum" name="weighNum" class="easyui-validatebox"  data-options="width: 180"  value="${bisOutStock.weighNum}" onkeyup="ischeckNum(this)" maxlength="7"/>
			</td>
		</tr>
		<tr>
			<td>是否已通关</td>
			<td>
				<span class="easyui-checkbox">
				  <input type="checkbox" id="ifCustomsClearance" name="ifCustomsClearance"  data-options="width: 180" value="1" 
				  <c:choose><c:when test="${bisOutStock.ifCustomsClearance == 1}">checked</c:when><c:otherwise></c:otherwise></c:choose>/>
				</span>
			</td>
			
			<td>是否机械装卸</td>
			<td>
				<span class="easyui-checkbox">
				  <input id="ifMachineHandling" name="ifMachineHandling" type="checkbox" data-options="width: 180"  value="1" 
				  <c:choose><c:when test="${bisOutStock.ifMachineHandling == 1}">checked</c:when><c:otherwise></c:otherwise></c:choose>/>
				</span>
			</td>
			<td>是否人工装卸</td>
			<td>
				<span class="easyui-checkbox">
					<input id="ifManpowerHandling" name="ifManpowerHandling"  type="checkbox" data-options="width: 180" value="1" 
					<c:choose><c:when test="${bisOutStock.ifManpowerHandling == 1}">checked</c:when><c:otherwise></c:otherwise></c:choose>/>
				</span>
			</td>
			
			<td>客户自行清关</td>
			<td>
				<span class="easyui-checkbox">
				 <input type="checkbox" id="ifSelfCustomsClearance" name="ifSelfCustomsClearance"   data-options="width: 180" value="1" 
				 <c:choose><c:when test="${bisOutStock.ifSelfCustomsClearance == 1}">checked</c:when><c:otherwise></c:otherwise></c:choose>/>
				</span>	 
			</td>
		</tr>
		
		<tr>
			<td>是否抄码</td>
			<td>
				<span class="easyui-checkbox">
				  <input type="checkbox" id="ifCodeCopy" name="ifCodeCopy"  data-options="width: 180" value="1" 
				  <c:choose><c:when test="${bisOutStock.ifCodeCopy == 1}">checked</c:when><c:otherwise></c:otherwise></c:choose>/>
				</span>
			</td>
			<td></td>
			<td>
			</td>
			<td></td>
			<td>
			</td>
			
			<td></td>
			<td></td>
		</tr>
		
		<tr>
			<td>特殊作业要求</td>
			<td>
				<input id="sepcialAsk" name="sepcialAsk" class="easyui-validatebox" data-options="width:180" value="${bisOutStock.sepcialAsk}"/>
			</td>
			<td>出库报关票数</td>
			<td>
				<input id="outCustomsCount" name="outCustomsCount" class="easyui-validatebox" data-options="width: 180" value="${bisOutStock.outCustomsCount}" onkeyup="ischeckNum(this)" maxlength="7"/>
			</td>
			<td>出库报检票数</td>
			<td>
				<input id="outCiqCount" name="outCiqCount" class="easyui-validatebox" data-options="width: 180" value="${bisOutStock.outCiqCount}" onkeyup="ischeckNum(this)" maxlength="7"/>
			</td>
			
			<td>审批票数</td>
			<td>
				<input id="approveCount" name="approveCount" class="easyui-validatebox" data-options="width: 180" value="${bisOutStock.approveCount}" onkeyup="ischeckNum(this)" maxlength="7"/>
			</td>
			
		</tr>
		<tr>
			
			<td>是否买方付费</td>
			<td>
				<span class="easyui-checkbox" onclick="ifbp()">
				<input id="ifBuyerPay" name="ifBuyerPay" type="checkbox" data-options="width: 180" value="1" 
				<c:choose><c:when test="${bisOutStock.ifBuyerPay == 1}">checked</c:when><c:otherwise></c:otherwise></c:choose>/>
				</span>
			</td>
			<td>费用方案</td>
                <td>
                    <input type="hidden" id="feeScheme" name="feeScheme" value=""></input>
                    <input type="hidden" id="feeS" name="feeS" value="${bisOutStock.feeScheme}"/>
                    <select class="easyui-combobox" id="feeId" name="feeId"
                            data-options="width:180" value="">
                    </select>
                </td>
			<td>仓储费开始日期</td>
			<td>
				<input id="startStoreTiem" name="startStoreTiem" class="easyui-my97" data-options="width: 180"
				value="<fmt:formatDate  value="${bisOutStock.startStoreTiem}"  pattern="yyyy-MM-dd" />"  />
			</td>
			<td>计划出库日期</td>
			<td>
			    <input type="hidden" id="ckTime" name="ckTime" value="${bisOutStock.ckTime}"/>
				<input id="etdWarehouse" name="etdWarehouse" class="easyui-my97" data-options="width: 180"  
				value="<fmt:formatDate  value="${bisOutStock.etdWarehouse}"  pattern="yyyy-MM-dd" />"  />
			</td>
			</tr>
		<tr>
			<td>备注</td>
			<td colspan='7'>	
			<input id="remark" name="remark" class="easyui-validatebox" data-options="width: 900" value="${bisOutStock.remark}" /> 
			</td>
		</tr>
		<tr>
			<td>报分库提单</td>
			<td>
				<input id="remark" name="remark" class="easyui-validatebox" data-options="width:180" value="${bisOutStock.remark}" /> 
			</td>
			<td>出库仓库</td>
			<td>
				<input type="hidden" id="warehouse" name="warehouse" />
				<select id="warehouseId" name="warehouseId" class="easyui-combobox" data-options="width: 180, required:'required'"  >
				</select>
			</td>
		</tr>
		<tr>
			<td>车号</td>
			<td>
				<input id="carNum" name="carNum" class="easyui-validatebox" data-options="width: 180"  value="${bisOutStock.carNum}" />
			</td>
			<td>打印标签数</td>
			<td>
				<input id="printNum" name="printNum" class="easyui-validatebox" data-options="width: 180" value="${bisOutStock.printNum}" onkeyup="ischeckNum(this)" maxlength="7"/> 
			</td>
		</tr>
		<tr>
			<td>存货方承担</td>
			<td colspan='6'>
				<input type="hidden" id="sellId" value="" />
				<input id="sellFee"  name="sellFee"  data-options="width:800" class="easyui-validatebox" style="background:#eee"   readonly /> 
			</td>
			<td rowspan='2'>
				&nbsp&nbsp<button type="button" onclick="getFee()" class="button green" >费用选择</button>
			</td>
		</tr>
		<tr>
			<td>收货方承担</td>
			<td colspan='6'>
				<input type="hidden" id="buyId" value="" /> 
				<input id="buyFee" name="buyFee" class="easyui-validatebox" data-options="width:800" readonly style="background:#eee"/>
			</td>
		</tr>
		<tr>
			<td>明细出库件数合计：</td>
			<td>
				<input id="pieceInfo"   class="easyui-validatebox" data-options="width: 180" readonly style="background:#eee"/> 
			</td>
			<td>明细净重合计：</td>
			<td>
				<input id="netInfo"   class="easyui-validatebox" data-options="width: 180"  readonly style="background:#eee"/> 
			</td>
			<td>明细毛重合计：</td>
			<td>
				<input id="grossInfo"   class="easyui-validatebox" data-options="width: 180"  readonly style="background:#eee"/> 
			</td>
		</tr>
 		<tr>
			<td>
				<input type="hidden" id="operator" name="operator" class="easyui-validatebox" data-options="width: 180" value="${bisOutStock.operator}" readonly  style="background:#eee"/>
			</td>
			<td>
				<input type="hidden" id="operateTime" name="operateTime"   data-options="width: 180" 
				value="<fmt:formatDate  value="${bisOutStock.operateTime}"  pattern="yyyy-MM-dd HH:mm:ss" />" readonly  style="background:#eee"/>
			</td>
			<td>
				<input type="hidden" id="department" name="department" class="easyui-validatebox" data-options="width: 180" value="${bisOutStock.department}" readonly  style="background:#eee"/>
			</td>
			<td>
			<input type="hidden" id="updateTime" name="updateTime"   data-options="width: 180" 
			value="<fmt:formatDate  value="${bisOutStock.updateTime}"  pattern="yyyy-MM-dd HH:mm:ss" />" readonly  style="background:#eee"/>
			</td>
		</tr>   
	</table>
</form>
</div>
<div data-options="region:'south',split:true,border:false" title="明细"  style="height:320px">
		<div id="tb" style="padding:5px;height:auto" class="datagrid-toolbar">
			<div>
			<shiro:hasPermission name="wms:outstockInfo:add">
		    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addInfo()">添加</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:outstockInfo:edit">
			    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editInfo()">修改</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:outstockInfo:delete">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:outstockInfo:down">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-basket-put" plain="true" data-options="disabled:false" onclick="down()">下载导入模板</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:outstockInfo:import">
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-basket-put" plain="true" data-options="disabled:false" onclick="into()">EXCEL导入</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
	    	</div>
		</div>
       <table id="dg" ></table> 
	   <div id="dlg" ></div> 
</div>

<script type="text/javascript">
var dg;
var d;
var dgCost;
var dgA;
var action = "${action}";

$(function(){   
//获得新联系单号
if($("#outLinkId").val() == "" ){
  $.ajax({
		type : "POST",
		url : "${ctx}/wms/outstock/getnewoutlinkid",
		data : "",
		dataType : "text",
		success : function(date) {
		  $("#outLinkId").val(date);
		}
	});
}else{
	  $.ajax({
		type : "POST",
		url : "${ctx}/wms/outstock/gethxfee/" + $("#outLinkId").val(),
		dataType : "json",
		success : function(date) {
 			$("#buyFee").val(date[0]);
 			$("#sellFee").val(date[1]);
 			$("#buyId").val(date[2]);
 			$("#sellId").val(date[3]);
		}
	});
}

     var stockInId = $("#stockInIdS").val();
     var receiverId = $("#receiverIdS").val();
     var orgId = $("#settleOrgIdS").val();
	 gridDG();
     //仓库
 	 var ck = '${bisOutStock.warehouseId}';
   	 if(action == "create"){
   	 	var ck = "1";
   	 }else if(action == "update"){
   		var newVal = $("#receiverIdS").val();
        feeSelect2(newVal, $("#feeS").val());
   	 }
   	 
	 $.ajax({
	   type: "GET",
	   async : false,
	   url: "${ctx}/base/warehouse/getWarehouse",
	   data: "",
	   dataType: "json",
	   success: function(date){
			   $('#warehouseId').combobox({
				   data: date,
				   value: ck,
				   valueField: 'id',
				   textField: 'warehouseName',
				   editable : false
			   });
	   }
	});
	
 	//报关代报公司
	   var getcustoms='${bisOutStock.customsCompanyId}';
	   $('#customsCompanyId').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=1&setid=${bisOutStock.customsCompanyId}",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode:'remote',
	   onLoadSuccess:function(){
			if(getcustoms!=null && getcustoms!=""){
				 $('#customsCompanyId').combobox("select",getcustoms);
				 getcustoms = "";
			}
	   }
   	});
   	
   	//报检代报公司
	   var getciq='${bisOutStock.ciqCompanyId}';
	   $('#ciqCompanyId').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=1&setid=${bisOutStock.ciqCompanyId}",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode:'remote',
	   onLoadSuccess:function(){
			if(getciq!=null && getciq!=""){
				 $('#ciqCompanyId').combobox("select",getciq);
				 getciq = "";
			}
	   }
   	});
   	
//存货方
   var getstockId='${bisOutStock.stockInId}';
   $('#stockInId').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${bisOutStock.stockInId}",
	   value : stockInId ,
	   valueField: 'ids', 
	   textField: 'clientName',
	   mode:'remote',
	   onChange:function (newVal,oldVal){
	   		if(typeof(newVal) != "undefined" && $('#stockInId').combobox("getValue")!=  $('#stockInId').combobox("getText")){
			var samev=$('#stockInId').combobox("getValue");
				$('#settleOrgId').combobox({
						method:"GET",
						url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid="+samev,
						valueField: 'ids',
				textField: 'clientName',
						mode:'remote',
					onLoadSuccess:function(){
			if(samev!=null && samev!=""){
				$('#settleOrgId').combobox("select",samev);
						samev = "";
				}
						}
					});
	   		}
	   		
	   		if (newVal != "" && newVal != "undefined") {
                   $('#feeId').combobox("clear");
                   // 2016-9-14 因第一次加载时,-
                   // 会触发对 费用方案select控件(feeId) 的修改操作,-
                   // 因此通过计数阻止第一次加载时的修改。 -
                   if (orgFlag != 0) {
                      $('#feeS').val('');
                   }
                   orgFlag++;
                   feeSelect(newVal);
               }
	   },
	   onLoadSuccess:function(){
			if(getstockId!=null && getstockId!=""){
				 $('#stockInId').combobox("select",getstockId);
				 getstockId = "";
			}
	   }
   	});
    //收货方
	var orgFlag = 0;
   	var getreceiver='${bisOutStock.receiverId}';
   $('#receiverId').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${bisOutStock.receiverId}",
	   value : receiverId,
	   valueField: 'ids',
	   textField: 'clientName',
	   mode:'remote',
	   onLoadSuccess:function(){
			if(getreceiver!=null && getreceiver!=""){
				 $('#receiverId').combobox("select",getreceiver);
				 getreceiver ="";
			}
	   },
	   onChange: function (newVal,oldVal){
	   	 if( $('#receiverId').combobox("getValue")!=  $('#receiverId').combobox("getText") ){
	   		$.ajax({
		   			async:false,
	  				type:'GET',
	 				url:"${ctx}/base/client/getcontract/"+newVal ,
	 				dataType: "text",  
	 				success: function(data){
	 					$("#receiverLinker").val(data);
	 				}
	 			});	
   			}
	    }
   	});
   //结算单位
   var getorg='${bisOutStock.settleOrgId}';
   $('#settleOrgId').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${bisOutStock.settleOrgId}",
	   value : orgId,
	   valueField: 'ids',
	   textField: 'clientName',
	   mode:'remote',
	   onLoadSuccess:function(){
			if(getorg!=null && getorg!=""){
				 $('#settleOrgId').combobox("select",getorg);
				 getorg = "";
			}
	   }
   	});
   
});
getState();
function ifbp(){
	var aaa = $("#ifBuyerPay").get(0).checked;
	   if(aaa){
		   var samev=$('#receiverId').combobox("getValue");
		   feeSelect(samev);
		   $('#settleOrgId').combobox({
				method:"GET",
				url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid="+samev,
				valueField: 'ids',
				textField: 'clientName',
			    mode:'remote',
			    onLoadSuccess:function(){
			    if(samev!=null && samev!=""){
					$('#settleOrgId').combobox("select",samev);
					samev = "";
				}
			}
		});
	 }else{
	    	var samev=$('#stockInId').combobox("getValue");
	    	feeSelect(samev);
			$('#settleOrgId').combobox({
				method:"GET",
				url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid="+samev,
				valueField: 'ids',
				textField: 'clientName',
				mode:'remote',
			    onLoadSuccess:function(){
			    if(samev!=null && samev!=""){
					$('#settleOrgId').combobox("select",samev);
							samev = "";
					}
				}
			});
	    };
}

//判断当前入库联系单状态
function getState(){
   var stateN = $("#auditingState").val();
   if(stateN == 1){
	   $("#auditingStateC").val("已审核");
	   $("input[type='checkbox']").click( 
			function(){ 
				this.checked = !this.checked; 
			} 
	   ); 
	   $(":input").attr('readonly','readonly');
	   $(":input").css('background','#eee')
	   $(":select").attr('readonly','readonly');
	   $(":select").css('background','#eee')
   }else{
       $("#auditingStateC").val("未审核");
       $("#auditingState").attr('readonly','readonly');
   }
	if(action =="create"){
		$("#auditingStateC").val("未保存");
	}
}

//清空
function clearIt(){
   window.parent.mainpage.mainTabs.addModule('出库联系单管理','wms/outstock/add');
}

//查询出库联系单明细
function gridDG(){
    var outLinkId = $("#outLinkId").val();
    if(outLinkId == ""){
        outLinkId = 0;
    }
	dg=$('#dg').datagrid({    
		method: "GET",
	    url:'${ctx}/wms/outstockinfo/json/'+outLinkId, 
	    fit : true,
		fitColumns : false,
		border : false,
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 1000,
		pageList : [ 1000, 2000 ],
		singleSelect:false,
	    columns:[[    
			{field:'id',title:'id'}, 
	        {field:'billNum',title:'提单号',sortable:true,width:150},    
	        {field:'ctnNum',title:'箱号',sortable:true,width:150},
	        {field:'asn',title:'ASN',sortable:true,width:150},  
	        {field:'cargoName',title:'品名',sortable:true,width:300},
	        {field:'skuId',title:'sku',sortable:true,width:150},  
	        {field:'outNum',title:'出库件数',sortable:true},  
	        {field:'codeNum',title:'抄码数',sortable:true},  
	        {field:'netWeight',title:'总净重',sortable:true,width:100}, 
	        {field:'grossWeight',title:'总毛重',sortable:true,width:100},  
	        {field:'typeSize',title:'规格',sortable:true,width:150}, 
	        {field:'rkNum',title:'入库号',sortable:true,width:150},
	        {field:'orderNum',title:'order号',sortable:true,width:150},  
	        {field:'projectNum',title:'项目号',sortable:true,width:150},  
	        {field:'shipNum',title:'船号',sortable:true,width:150},
	        {field:'mscNum',title:'msc号',sortable:true,width:150},
	        {field:'lotNum',title:'lot号',sortable:true,width:150},  
	        {field:'makeTime',title:'制造时间',sortable:true,width:150},    	        
	        {field:'enterState',title:'入库类型',sortable:false,width:50,
	        	formatter : function(value, row, index) {
	        		if(value==0){return '成品'}else if(value==1){return '货损'}else if(value==2){return '包装破损'}
	        	}
	        },
	        {field:'salesNum',title:'销售号',sortable:true,width:100},  
	        {field:'units',title:'重量单位',sortable:true,
	        	formatter : function(value, row, index) {
         			  return '千克';
  	        	}
  	        }
	    ]],
	    onLoadSuccess:function(){
	    	insertSum();
	    },
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb2'
	});
} 

function insertSum(){
		    var rows = $('#dg').datagrid('getRows');
	    	var pieces=0;
	    	var net=0;
	    	var gross=0;
	    	for(var i=0;i<rows.length;i++){
	    			pieces += Number(rows[i]['outNum']);
	    			net += Number(rows[i]['netWeight']);
	    			gross += Number(rows[i]['grossWeight']);
	    		};
	    	$("#pieceInfo").val(pieces);
	    	$("#netInfo").val(net);
	    	$("#grossInfo").val(gross);	
		
}

//保存
function submitForm(){
	if($("#auditingStateC").val() != "未审核" && $("#auditingStateC").val() != "未保存"){
      parent.$.messager.show({ title : "提示",msg: "此联系单已审核，无法修改！", position: "bottomRight" });
      return;
    }
    if($("#stockInId").combobox("getValue") == ""){
      parent.$.messager.show({ title : "提示",msg: "请选择存货方！", position: "bottomRight" });
      return;
    }
    if($("#receiverId").combobox("getValue") == ""){
      parent.$.messager.show({ title : "提示",msg: "请选择收货方！", position: "bottomRight" });
      return;
    }
    
    //检查要保存的明细是否都有asn
    var getLinkId=$("#outLinkId").val(); 
	$.ajax({
		type:'get',
		url:"${ctx}/wms/outstockinfo/ifAllHasAsn/"+getLinkId,
		
		success: function(data){
			if(data == "false" ){
				parent.$.easyui.messager.show({title: "操作提示", msg: "明细中存在ASN为空,请检查后再保存！", position: "bottomRight"});
				return;
			}else{
				if($("#auditingStateC").val() == "未保存" ){
					var state = 0;
				}else{
			    	var state = 1;
				}
			    
			    $("#feeScheme").val($('#feeId').combobox("getValue"));
				$("#stockIn").val( $('#stockInId').combobox("getText") );
				$("#receiver").val( $('#receiverId').combobox("getText") );
				$("#settleOrg").val( $('#settleOrgId').combobox("getText") );
			    $("input[type='checkbox']").not("input:checked").val("0");
				$(":checkbox").attr("checked",true);
				$("#customsCompany").val( $('#customsCompanyId').combobox("getText") );
				$("#ciqCompany").val( $('#ciqCompanyId').combobox("getText") );
				$("#warehouse").val($('#warehouseId').combobox("getText"));
				if($("#mainForm").form('validate')){
					//用ajax提交form
			 		$.ajax({
			 	  		async: false,
			 	  		type: 'POST',
			 	  		url: "${ctx}/wms/outstock/createoutstock/"+state,
			 	  		data: $('#mainForm').serialize(),
			 	  		dataType: "text",  
			 	  		success: function(msg){
			 	  			if(msg == "success"){
								$(":checkbox[value='0']").attr("checked",false);
								$("input[type='checkbox']").not("input:checked").val("1");
								if($("#buyFee").val() != "" || $("#sellFee").val() != ""){
									var sellId= $('#stockInId').combobox('getValue');
									var buyId = $('#receiverId').combobox('getValue');
									var linkId= $('#outLinkId').val();
									var sellList = $("#sellId").val();
									var buyList = $("#buyId").val();
									$.ajax({
			  							type:'GET',
			 							url:"${ctx}/wms/outstock/addexpense" ,
			 							data : {"sellList":sellList,"buyList":buyList,"buyId":buyId,"sellId":sellId,"linkId":linkId},
			 							success: function(data){
			 							}
			 						});	
								}
			 	  				parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });
			 	  				$("#auditingStateC").val("未审核");
			 	  			}
			 	  		}
			 	  	});
			 	}else{
			 		$(":checkbox[value='0']").attr("checked",false);
					$("input[type='checkbox']").not("input:checked").val("1");
			 	}
			}
		}
	});
}

//删除此出库联系单
function deleteIt(){
if($("#auditingState").val() == 1){
  parent.$.messager.show({ title : "提示",msg: "此数据已被审核，无法删除！", position: "bottomRight" });
  return;
}
var outLinkId = $("#outLinkId").val();
$.ajax({
		type: 'get',
		url: "${ctx}/wms/outstock/deleteoutstock/" + outLinkId,
		success: function(data){
			parent.$.messager.show({ title : "提示",msg: "删除成功！", position: "bottomRight" });
			window.parent.mainpage.mainTabs.closeCurrentTab();//关闭TAB
		}
	  });
}

//审核此出库联系单
function okPass(){
    if($("#auditingStateC").val() != "未审核"){
      parent.$.messager.show({ title : "提示",msg: "此联系单不是未审核状态，无法审核！", position: "bottomRight" });
      return;
    }
    if($("#ifBuyerPay").is(":checked") && $("#receiverId").combobox("getValue") == $("#stockInId").combobox("getValue")){
      parent.$.messager.show({ title : "提示",msg: "勾选了买方付费的联系单，收货方和存货方不能相同！", position: "bottomRight" });
      return;
    }
    if($("#ifBuyerPay").is(":checked") && $("#feeId").combobox("getText").trim().length == 0 ){
        parent.$.messager.show({ title : "提示",msg: "勾选了买方付费的联系单，费用方案不能为空！", position: "bottomRight" });
        return;
      }
    //startStoreTiem
    if($("#ifBuyerPay").is(":checked") && $('input[name=startStoreTiem]').val().trim().length == 0 ){
        parent.$.messager.show({ title : "提示",msg: "勾选了买方付费的联系单，仓储费开始日期不能为空！", position: "bottomRight" });
        return;
      }
    
	var outLinkId = $('#outLinkId').val();
	parent.$.messager.confirm('提示', '您确定要审核此出库联系单？选择买方付款审核后无法取消。', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/outstock/passoutstock/" + outLinkId,
				success: function(data){
					if(data == "success"){
						parent.$.messager.show({ title : "提示",msg: "审核成功！", position: "bottomRight" });
						$("#auditingStateC").val("已审核");
   						$("input[type='checkbox']").click( 
							function(){ 
								this.checked = !this.checked; 
							} 
   						); 
  						 $(":input").attr('readonly','readonly');
  						 $(":input").css('background','#eee')
						 $(":select").attr('readonly','readonly');
  						 $(":select").css('background','#eee')
					}
	            	else{
    	                parent.$.messager.alert('警告','出库数量超过质押数量 '+data,'warning');
	            	}
				}
			});
		} 
	});
}

//取消审核此入库联系单
function noPass(){
    if($("#auditingStateC").val() != "已审核")
    {
      parent.$.messager.show({ title : "提示",msg: "此联系单不是未审核状态，无法审核！", position: "bottomRight" });
      return;
    }
	var outLinkId = $('#outLinkId').val();
	parent.$.messager.confirm('提示', '您确定要审核此出库联系单？', function(data){
		if (data){
			$.ajax({
				type:'get',
				url:"${ctx}/wms/outstock/nopassoutstock/" + outLinkId,
				success: function(data){
					if(data == "success"){
						parent.$.messager.show({ title : "提示",msg: "审核成功！", position: "bottomRight" });
						$("#auditingStateC").val("未审核");
   						$("input[type='checkbox']").click( 
							function(){ 
								this.checked = !this.checked; 
							} 
   						); 
  						 $(":input").attr('readonly','readonly');
  						 $(":input").css('background','#eee')
						 $(":select").attr('readonly','readonly');
  						 $(":select").css('background','#eee')
					}
				}
			});
		} 
	});
}
//修改入库联系单明细修改明细是先判断是否已经审核
function editInfo() {
    	if($("#auditingStateC").val() == "已审核" ){
    		parent.$.messager.show({title: "提示", msg: "已审核数据无法操作明细！", position: "bottomRight" });
    		return;
    	}
        var row = dg.datagrid('getSelected');
        if (rowIsNull(row)){
    		parent.$.messager.show({title: "提示", msg: "请选择一条要修改的操作明细！", position: "bottomRight" });
        	return;
        }
        var outLinkId = $('#outLinkId').val();
        d = $("#dlg").dialog({
            title: '修改出库明细',
            width: 380,
            height: 380,
            href: '${ctx}/wms/outstockinfo/update/'+row.id+'/'+outLinkId,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {
                    var outNum=$("#outNum").val();
                	$.ajax({
                		async:false,
                		type : "GET",
                		url : "${ctx}/wms/outstockinfo/checknum",
                		data : {"id":row.id,
                			  "linkId":outLinkId,
                			  "outNum":outNum
                			},
						success : function(data) {
							if(data=="success"){
								formsub();
								d.panel('close');
							}else{
								parent.$.easyui.messager.show({title: "操作提示", msg:data, position: "bottomRight"});
								return;
							}
						}
					});
                }
            }, {
                text: '取消',
                handler: function () {
                    d.panel('close');
                }
            }],
            onClose: function () {
                window.setTimeout(function () {
                    gridDG()
                }, 100);
            }
        });
}

//提交表单
function formsub(){
    $("#mainform2").submit();
}
//增加明细时，先判断是否已保存
function addInfo(){
if($("#auditingStateC").val() == "已审核" ){
		parent.$.messager.show({title: "提示", msg: "已审核数据无法操作明细！", position: "bottomRight" });
		return;
	}
var getLinkId=$("#outLinkId").val(); 
$.ajax({
				type:'get',
				url:"${ctx}/wms/outstock/ifsave/"+getLinkId,
				dataType:"text",
				success: function(data){
					if(data != "success"){
						parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存出库联系单！", position: "bottomRight"});
						return;
					}else{
					    addInfoT();
					}
				}
			});	

}

//出库联系单明细弹窗增加
function addInfoT() {
 if($("[name='checkB']").is(":checked")){
   $("#blSign").val("1");
 }
 if($("[name='checkS']").is(":checked")){
   $("#blSign").val("1");
 } 
	var getLinkId=$("#outLinkId").val(); 
		d=$("#dlg").dialog({   
		    title: '出库联系单明细选择',    
		    width: 1050,    
		    height: 500,    
		    href:'${ctx}/wms/outstockinfo/addinfo/'+getLinkId,
		    maximizable:true,
		    modal:true,
		    buttons:[{
				text:'确认',
				handler:function(){
					var addrows = $("#infodg").datagrid("getSelections");
					var obj;
					var bug=0;
					var leng=addrows.length
					for(var i=0;i<leng;i++){
						obj=addrows[i];
						if(!checkInfoIsHave(obj['LAB'])){
							if(""!=$("#piece"+Number(obj['NUM'])).val()){
								var outNum = $("#piece"+obj['NUM']).val();
						    	var saleNum = $("#sale"+obj['NUM']).val();
						    	var codeNum = $("#codenum"+obj['NUM']).val();
						    	var billNum = obj.BILL_NUM;
								var ctnNum = obj.CTN_NUM;
								var asn = obj.ASN
								var sku = obj.SKU_ID;
								var outLinkIdA = $("#outLinkId").val();
								var enterState = obj.ENTER_STATE;
							    addOutInfo(outNum,billNum,ctnNum,asn,sku,outLinkIdA,saleNum,enterState,codeNum);
							}
						}
					}
					/* for(var i=leng-1;i>=0;i--){
						obj=addrows[i];
						if(!checkInfoIsHave(obj['LAB'])){
							//TODO 需要判断输入件数要小于（库存数量+质押量）
							if(""!=$("#piece"+Number(obj['NUM'])).val()){
 							    var j = parseInt(obj.NUM);
 							    if(bug==1){
 							    	 var outNum = 0;
 							    	 var saleNum="";
 							    }else{
 							    	 var outNum = $("#piece"+j).val();
 							    	 var saleNum = $("#sale"+j).val();
 							    }
 							    if( leng>1 && i>0 && j==0){
 							    	 bug =1;
 							    }
 							    var billNum = obj.BILL_NUM;
 							    var ctnNum = obj.CTN_NUM;
 							    var asn = obj.ASN
 							    var sku = obj.SKU_ID;
 							    var outLinkIdA = $("#outLinkId").val();
 							    var enterState = obj.ENTER_STATE;
 								addOutInfo(outNum,billNum,ctnNum,asn,sku,outLinkIdA,saleNum,enterState);
							}
						}
					} */
					$.ajax({
							async: false,
							type:'get',
							url:"${ctx}/wms/outstockinfo/checkRank" ,
							data : {"outLinkId":getLinkId},
							success: function(data){
								if(data != "success"){
									 parent.$.easyui.messager.show({title: "操作提示", msg: "此客户剩余库存已超出警戒线！无法继续出库！", position: "bottomRight"});
								}else{
									parent.$.easyui.messager.show({title: "操作提示", msg: "保存成功！", position: "bottomRight"});
								}
							}
						});	
					window.setTimeout(function(){gridDG()},500);
					d.panel('close');
				}
			},{
				text:'取消',
				handler:function(){
					d.panel('close');
				}
			}]
		}); 
}


//校验明细是否已经添加过
function  checkInfoIsHave(infos){
	var rows = $('#dg').datagrid('getRows');
	for(var i=0;i<rows.length;i++){
		if(infos==rows[i]["LAB"]){
			return true;
		}
	}
	return false;
}

//添加明细到数据库
function addOutInfo(outNum,billNum,ctnNum,asn,sku,outLinkIdA,saleNum,enterState,codeNum){
			$.ajax({
				async:false,
				type:'get',
				url:"${ctx}/wms/outstockinfo/addoutinfo" ,
				data : {"outNum":outNum,"billNum":billNum,"ctnNum":ctnNum,"asn":asn,"sku":sku,"outLinkId":outLinkIdA,"saleNum":saleNum,"enterState":enterState,"codeNum":codeNum},
				success: function(data){
					if(data != "success"){
						 
					}
				}
			});	
}




//删除出库联系单明细
function delInfo(){
	if($("#auditingStateC").val() == "已审核" ){
		parent.$.messager.show({title: "提示", msg: "已审核数据无法操作明细！", position: "bottomRight" });
		return;
	}
	var rows = dg.datagrid('getSelections');
	var del = dg.datagrid('getSelected');
	if(del == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	var ids= [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].id);
		
	}
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type: 'get',
				url: "${ctx}/wms/outstockinfo/deleteoutstockinfo/" + ids,
				success: function(data){
					dg.datagrid('clearSelections');
					successTip(data, dg);
				}
			});
		} 
	});
}


function getFee(){
	var linkId= $('#outLinkId').val();
	$.ajax({
		type: 'post',
		dataType:'text',
		async:false,
		data:{outLinkId:linkId},
		url: "${ctx}/wms/outstock/checkBillNum",
		success: function(data){
			if(data=="success"){
				d=$("#dlg").dialog({   
				    title: '费用选择',    
				    width: 700,    
				    height: 340,    
				    href:'${ctx}/wms/outstock/getfee/'+linkId,
				    maximizable:true,
				    modal:true,
				    buttons:[{
						text:'增加',
						handler:function(){
			               abc();
						}
					},{
						text:'取消',
						handler:function(){
							d.panel('close');
						}
					}]
				});
			}else{
				parent.$.messager.show({title: "提示", msg:data, position: "bottomRight" });
			}
		}
	});
}

function abc(){
	var linkId= $('#outLinkId').val();
	var sellId= $('#stockInId').combobox('getValue');
	var buyId = $('#receiverId').combobox('getValue');
	var sellList = new Array();
	var buyList = new Array();
	var i = 1;
	var j = 0;
	var k = 1;
	var l = 0;
	//遍历卖方承担
	for(i=1;i<sellNum+1;i++){
	   if($("input[id="+i+"][name='sell']").is(":checked")){
	       sellList[j] = $("input[id="+i+"][name='sell']").val();
	        j=j+1;
	       if($("input[id="+i+"][name='checkS']").is(":checked")){
	          sellList[j] = "1";
	       }else{
	          sellList[j] = "0";
	       }
	       j=j+1;
	   }
	}
	//遍历买方承担
	for(k=1;k<buyNum+1;k++){
	   if($("input[id="+k+"][name='buy']").is(":checked")){
	       buyList[l] = $("input[id="+k+"][name='buy']").val();
	        l=l+1;
	       if($("input[id="+k+"][name='checkB']").is(":checked")){
	          buyList[l] = "1";
	       }else{
	          buyList[l] = "0";
	       }
	       l=l+1;
	   }
	}
	sellList = sellList.join(",");
	buyList = buyList.join(",");
	$("#sellId").val(sellList);
	$("#buyId").val(buyList);
	//回显费目名称
	$.ajax({
	 	async:false,
		type:'get',
		url:"${ctx}/wms/outstock/hxcode" ,
		data : {"sellList":sellList,"buyList":buyList,"linkId":linkId,"sellId":sellId,"buyId":buyId},
		dataType : "json",
		success: function(data){
			$("#sellFee").val(data[0]);
	 		$("#buyFee").val(data[1]);
	 		d.panel('close');
		}
	});	
}

//制造出库订单
function makeOrder(){
     if($("#auditingStateC").val() == "未审核"){
         parent.$.messager.show({ title : "提示",msg: "请审核后再制作出库订单！", position: "bottomRight" });
         return;
     }
	var outLinkId = $("#outLinkId").val();
	var href='bis/loading/create/'+outLinkId;
	$.ajax({
			async: false,
			type:'get',
			url:"${ctx}/wms/outstockinfo/checkRank" ,
			data : {"outLinkId":outLinkId},
			success: function(data){
				if(data != "success"){
					parent.$.easyui.messager.show({title: "操作提示", msg: "此客户剩余库存已超出警戒线！无法制作出库订单！", position: "bottomRight"});
					return;
				}else{
					window.parent.mainpage.mainTabs.addModule('制作出库订单管理',href,'icon-hamburg-customers');
				}
			}
		});	
} 

//导出
function exportIt(){
 	var url = "${ctx}/wms/outstock/export";
 	$("#cx").val($("#outLinkId").val());
 	$("#searchFrom2").attr("action",url).submit();
 }

//导出OTE-PDF(出库日期为计划出库日期)
function exportOtePdf(){
	var url = "${ctx}/wms/outstock/exportotepdf";
	$("#cx").val($("#outLinkId").val());
	$("#searchFrom2").attr("action",url).submit();
}

//进入  出库费用调整  
function outStockAdjust(){
	if($("#mainForm").form('validate')){
		var linkId = $("#outLinkId").val();
		var stockId = $("input[name='settleOrgId']").val();
		//parent.$.messager.confirm('提示', '是否确认生成费用？', function (data) {
	    //if (data) {
			$.ajax({
				type:'get',
				url:"${ctx}/wms/outstock/ifsave/"+linkId,
				dataType:"text",
				success: function(data){
					if(data != "success"){
						parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存出库联系单！", position: "bottomRight"});
						return;
					}else{
	                        if(action=="create"){
	                        	window.parent.mainpage.mainTabs.addModule('费用', 'cost/standingBook/outList/' + linkId + "/" + stockId);
	                    	}else{
	                    		var operator="${bisOutStock.operator}";
	                    		$.ajax({
									type:'POST',
									url:"${ctx}/base/scheme/checkPermission/"+operator,
									success: function(data){
										if(data=="success"){
											window.parent.mainpage.mainTabs.addModule('费用', 'cost/standingBook/outList/' + linkId + "/" + stockId);
										}else{
											parent.$.messager.show({ title : "提示",msg: "你不是此费用的创建人，无法进行修改操作！", position: "bottomRight" });
										}
									}
								});
	                    	}
					}
				}
			  });
	    //}
	  //});
	}
}

//进入 出库客服费用调整  
function outStockAdkfjust(){
	if($("#mainForm").form('validate')){
		var linkId = $("#outLinkId").val();
		var stockId = $("input[name='settleOrgId']").val();
		//parent.$.messager.confirm('提示', '是否确认生成费用？', function (data) {
        //if (data) {
			$.ajax({
				type:'get',
				url:"${ctx}/wms/outstock/ifsave/"+linkId,
				dataType:"text",
				success: function(data){
					if(data != "success"){
						parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存出库联系单！", position: "bottomRight"});
						return;
					}else{
	                        if(action=="create"){
	                        	window.parent.mainpage.mainTabs.addModule('费用', 'cost/standingBook/outkfList/' + linkId + "/" + stockId);
	                    	}else{
	                    		var operator="${bisOutStock.operator}";
	                    		$.ajax({
									type:'POST',
									url:"${ctx}/base/scheme/checkPermission/"+operator,
									success: function(data){
										if(data=="success"){
											window.parent.mainpage.mainTabs.addModule('费用', 'cost/standingBook/outkfList/' + linkId + "/" + stockId);
										}else{
											parent.$.messager.show({ title : "提示",msg: "你不是此费用的创建人，无法进行修改操作！", position: "bottomRight" });
										}
									}
								});
	                    	}
					}
				}
			});	
         //}
	  //});
	}
}

//下载
function down(){
	var url = "${ctx}/wms/outstockinfo/download";
	$("#downForm").attr("action",url).submit();
}

//导入
function into(){
if($("#auditingStateC").val() == "已审核" ){
		parent.$.messager.show({title: "提示", msg: "已审核数据无法操作明细！", position: "bottomRight" });
		return;
	}
var outLinkId=$("#outLinkId").val(); 
$.ajax({
		type:'get',
		url:"${ctx}/wms/outstock/ifsave/"+outLinkId,
		dataType:"text",
		success: function(data){
			if(data != "success"){
				parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存出库联系单！", position: "bottomRight"});
				return;
			}else{
				intoc(outLinkId);
			}
		}
	});	
}


function intoc(outLinkId){
	d=$("#dlg").dialog({   
	    title: "出库联系单明细导入",    
	    width: 450,    
	    height: 450,    
	    href:'${ctx}/wms/outstockinfo/into/'+outLinkId,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				$("#mainform3").submit(); 
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	}); 
}

//生成货转单
function makeTransfer(){
	if($("#auditingStateC").val() == "未保存" ){
		parent.$.messager.show({title: "提示", msg: "未保存的联系单无法生成货转单！", position: "bottomRight" });
		return;
	}
	window.parent.mainpage.mainTabs.addModule("货转管理", "bis/transfer/createTransfer/"+$("#outLinkId").val());
}

//数字校验
function ischeckNum(val) {
	if (val.value) {
		if (!isNaN(val.value)) {

		} else {
			parent.$.messager.show({title: "提示", msg: "请输入数字!", position: "bottomRight" });
			$("#"+val.id).val("");
			myfm.isnum.select();
			return false;
		}
	}
}

//根据收货方获得费用方案列表
function feeSelect(newVal) {
    var clientId = newVal;
    $.ajax({
        async: false,
        type: "GET",
        url: "${ctx}/wms/enterStock/selFeePlan",
        data: {"clientId": clientId},
        dataType: "json",
        success: function (date) {
            if (date.length == 0) {
                $('#feeId').combobox({
                    data: date,
                    valueField: 'schemeNum',
                    textField: 'schemeName',
                    editable: false
                   
                });

            } else {
                $('#feeId').combobox({
                    data: date,
                    value: $("#feeS").val(),
                    valueField: 'schemeNum',
                    textField: 'schemeName',
                    editable: false
                   
                });
            }
        }
    });
}

//修改时渲染费用下拉框
function feeSelect2(newVal, fee) {
    var clientId = newVal;
    $.ajax({
        async: false,
        type: "GET",
        url: "${ctx}/wms/enterStock/selFeePlan",
        data: {"clientId": clientId},
        dataType: "json",
        success: function (date) {
            $('#feeId').combobox({
                data: date,
                value: fee,
                valueField: 'schemeNum',
                textField: 'schemeName',
                editable: false
                
            });
        }
    });
}

</script>
</body>
</html>