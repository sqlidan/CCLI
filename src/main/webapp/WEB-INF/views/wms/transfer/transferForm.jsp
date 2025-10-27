<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<form id="downForm" method="post" >
	</form>
<form id="transferform" action="${ctx }/bis/transfer/${action}" method="post">   
	<div data-options="region:'north',split:true,border:false" style="height:230px" >
         <div class="easyui-layout" data-options="fit:true">
			<div class="datagrid-toolbar" data-options="region:'north',split:false,border:false" style="height:auto;" >
				<c:if test="${obj.isTSpliet !=2}">
					<shiro:hasPermission name="bis:transfer:save">
			       		<a id="topbutton" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="save();">保存</a>
					</shiro:hasPermission>
				</c:if>
		    	<span id="topbuttonspan" class="toolbar-item dialog-tool-separator"></span>
				<shiro:hasPermission name="bis:transfer:adjust">
				 	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-credit" plain="true" onclick="transferAdjust()">费用</a>
			       	<span class="toolbar-item dialog-tool-separator"></span>
		       	</shiro:hasPermission>
		     </div>
			
			<div data-options="region:'south',split:false,border:false" style="height:10px;"></div>
			<div data-options="region:'west',split:false,border:false" style="width:10px;"></div>
			<div data-options="region:'east',split:false,border:false" style="width:10px;"></div>
			<div data-options="region:'center',split:false,border:false" style="padding-top: 5px;" >
					<table>
						<tr>
							<td style="width:80px; ">货转单号：</td><td>
							<input type="hidden"  name="operator" value="${obj.operator}" />
							<input type="hidden"   name="operateTime" value="${obj.operateTime}" />
							<input type="text" id="transferId" name="transferId"  class="easyui-validatebox"  readonly="readonly"  data-options="width:150,required:'required'" value="${obj.transferId}" /></td>
							<td style="width:60px; ">收货方：</td><td>
							<input type="hidden" id="receiverName" name="receiverName" value="${obj.receiverName}" />
								<c:choose>
								<c:when test="${obj.isEdite == 1}">
									<input type="text" id="receiver" name="receiver"  class="easyui-validatebox" readonly="readonly"   data-options="width:150" value="${obj.receiverName}" />
								</c:when>
								<c:otherwise>
									<select class="easyui-combobox" id="receiver" name="receiver" data-options="width:150,required:'required'">
										<option value=""></option>
									</select>
								</c:otherwise>
							</c:choose>
							</td>
							<td style="width:100px; ">收货方联系人：</td><td>
							<input type="text" id="receiverLinker" name="receiverLinker"  class="easyui-validatebox" <c:if test="${obj.isEdite == 1}"> readonly="readonly" </c:if> data-options="width:150,required:'required',validType:'length[1,20]'" value="${obj.receiverLinker}"/></td>
							<td style="width:100px; ">收货方结算单位：</td><td>
							<input type="hidden" name="receiverOrg" id="receiverOrg"  value="${obj.receiverOrg}"  />
							<c:choose>
								<c:when test="${obj.isEdite == 1}">
									<input type="text" name="receiverOrgId" class="easyui-validatebox" readonly="readonly"   data-options="width:150"  value="${obj.receiverOrg}"  />
								</c:when>
								<c:otherwise>
									<select class="easyui-combobox" id="receiverOrgId" name="receiverOrgId" data-options="width:150,required:'required'">
										<option value=""></option>
									</select>
								</c:otherwise>
							</c:choose>
							</td>
						</tr>
						<tr style="height: 5px;"></tr>
						<tr>
							<td>费用方案：</td><td>
								<input type="hidden" id="feePlan" name="feePlan" value=""></input>
								<select class="easyui-combobox" id="feeId" name="feeId" data-options="width:150" value="">
								</select>
							</td>
							<td >存货方：</td><td>
							<input type="hidden" id="stockIn" name="stockIn" value="${obj.stockIn}" />
								<c:choose>
								<c:when test="${obj.isEdite == 1}">
									<input type="text" name="stockIn" class="easyui-validatebox" readonly="readonly"   data-options="width:150"  value="${obj.stockInId}"  />
								</c:when>
								<c:otherwise>
									<select class="easyui-combobox" id="stockInId" name="stockInId" data-options="width:150, required:'required'">
										<option value=""></option>
									</select>
								</c:otherwise>
								</c:choose>
							</td>
							<td>仓库：</td><td>
								<c:choose>
								<c:when test="${obj.isEdite == 1}">
									<input type="text" name="warehouse" class="easyui-validatebox" readonly="readonly"   data-options="width:150"  value="${obj.warehouse}"  />
								</c:when>
								<c:otherwise>
									<select class="easyui-combobox" id="warehouseId" name="warehouseId" data-options="width:150, required:'required'">
										<option value=""></option>
									</select>
								</c:otherwise>
								</c:choose>
							</td>
							<!--  
							<td>是否已清库：</td><td>
								<span class="easyui-checkbox">
									<input id="ifClearStore" name="ifClearStore" type="checkbox" <c:if test="${obj.isBuyFee =='1'}"> checked="checked" </c:if>  data-options="width: 180"  value="1" />
								</span>
								</td>
							<td>客户自行清关：</td><td>
							 	<span class="easyui-checkbox">
									<input id="ifSelfCustomsClearance" name="ifSelfCustomsClearance" <c:if test="${obj.ifSelfCustomsClearance == '1'}"> checked="checked" </c:if> type="checkbox"  data-options="width: 180"  value="1" />
								</span>
							</td>
							-->
						</tr>
						<tr>
							<td>报关号：</td><td>
							<input type="text" name="cdNum" class="easyui-validatebox" <c:if test="${obj.isEdite == 1}"> readonly="readonly" </c:if> data-options="width:150,validType:'length[0,20]'" value="${obj.cdNum}"/></td>
							<td>代报公司：</td><td>
							<input type="text" name="supplyCompany" class="easyui-validatebox" <c:if test="${obj.isEdite == 1}"> readonly="readonly" </c:if> data-options="width:150,validType:'length[0,20]'" value="${obj.supplyCompany}"/></td>
							<td>买方承担：</td><td>
									<input id="isBuyFee" name="isBuyFee" type="checkbox"  <c:if test="${obj.isBuyFee == '1'}"> checked="checked" </c:if>  data-options="width: 180"  value="1" />
								</td>
							<td>计费日期：</td><td>
							 	 <c:choose>
									<c:when test="${obj.isEdite == 1}">
										<input type="text" name="startStoreDate" class="easyui-validatebox" readonly="readonly"   data-options="width:150, required:'required'" value="<fmt:formatDate value="${obj.startStoreDate}"/>" />
									</c:when>
									<c:otherwise>
									 	<input id="startStoreDate" name="startStoreDate" type="text"   class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width: 150, required:'required',prompt: '计费按此日期为准'"  value="<fmt:formatDate value="${obj.startStoreDate}"/>" />
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					<!--	<tr>
							<td>存货方承担</td>
							<td colspan='7'>
								<input id="sellId" name="sellFee" type="hidden" value="${obj.sellFee}" />
								<input id="sellFee"  name="sellFeeName" value="${obj.sellFeeName}" <c:if test="${obj.isEdite == 0}">onclick="getFee()"</c:if>  class="easyui-validatebox" data-options="width:960" readonly style="background:#eee">
							</td>
						</tr>
						<tr>
							<td>收货方承担</td>
							<td colspan='7'>
								<input id="buyId" name="buyFee" type="hidden" value="${obj.buyFee}"  />
								<input id="buyFee" name="buyFeeName" value="${obj.buyFeeName}" <c:if test="${obj.isEdite == 0}">onclick="getFee()"</c:if>   class="easyui-validatebox" data-options="width:960" readonly style="background:#eee">
							</td>
						</tr> -->
						<tr style="height: 5px;">
							<td>备注：</td>
							<td colspan="7"><textarea rows="2" name="remark" cols="110" <c:if test="${obj.isEdite == 1}"> readonly="readonly" </c:if>    class="easyui-validatebox" data-options="validType:'length[1,50]'"   style="font-size: 12px;font-family: '微软雅黑'">${obj.remark}</textarea></td>
							<div id="skuList" style="display: none;"></div>
							<div id="infoListdiv"><!--明细参数提交--></div>
							<div id="delListdiv"><!--删除明细参数提交--></div>
						</tr>
						<tr>
							<td>明细箱数合计：</td>
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
					</table>
			</div>
        </div>
</div>
<div data-options="region:'center',split:true,border:false" class="easyui-tabs" style="fit:true">
	    <div title="货转明细"  style="height:auto" >
	    	<div id="tbo" class="datagrid-toolbar">
	    		<shiro:hasPermission name="bis:transfer:add">
			    	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addTransferIfno()">添加</a>
			  		<span class="toolbar-item dialog-tool-separator"></span>
		  		</shiro:hasPermission>
		  		<shiro:hasPermission name="bis:transfer:delete">
			  		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
			  		<span class="toolbar-item dialog-tool-separator myadd"></span>
		  		</shiro:hasPermission>
		  		<c:if test="${obj.isTSpliet == 1}">
		  			<shiro:hasPermission name="bis:transfer:tray">
				  		<a href="javascript:void(0)" id="tga" class="easyui-linkbutton myadd" iconCls="icon-add" plain="true" data-options="disabled:false" onclick="createTruck()">托盘货转</a>
		    			<span class="toolbar-item dialog-tool-separator myadd"></span>
	    			</shiro:hasPermission>
	    		</c:if>
	    		<c:if test="${obj.isTSpliet ==2}">
	    			<shiro:hasPermission name="bis:transfer:tray">
				  		<a href="javascript:void(0)" id="dtga" class="easyui-linkbutton myadd" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="delTruck()">取消托盘货转</a>
		    			<span class="toolbar-item dialog-tool-separator myadd"></span>
	    			</shiro:hasPermission>
	    		</c:if>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-basket-put" plain="true" data-options="disabled:false" onclick="down()">下载导入模板</a>
					<span class="toolbar-item dialog-tool-separator"></span>
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-basket-put" plain="true" data-options="disabled:false" onclick="into()">EXCEL导入</a>
					<span class="toolbar-item dialog-tool-separator"></span>
	    	</div>
			<table id="dg"></table>
	    </div>
	    <div title="货转托盘明细" data-options="closable:false" style="height:100%;">
		 	<div id="tbt"></div>
			<table id="dgt"></table>
	    </div>
</div>
</form>
<div id="dlg"></div>
<div id="dtg"></div>
<div id="dlimp"></div>
<form id="checknumform" action="${ctx }/bis/transfer/check" method="post">   
	<div id="skuNumdiv" style="display: none;"><!--质押校验提交--></div>
	<div id="outLinkdiv" style="display: none;">
		<input type="hidden"  id="cstockIn" name="stockIn" readonly="readonly"/>
		<input type="hidden"  id="cwarehouseId" name="warehouseId" readonly="readonly"/>
	</div>
</form> 
<form id="checkusernumform" action="${ctx }/bis/transfer/usercheck" method="post">   
	 <input type="hidden"  id="ustockIn" name="stockIn" readonly="readonly"/>
	 <input type="hidden"  id="uwarehouseId" name="warehouseId" readonly="readonly"/>
	 <div id="addinfo"></div>
</form> 
<script type="text/javascript">
var dg;
var d;
var dimp;
var model='${action}';
var trNum=0;//用于记录行号
var splitJson,tids;//用于记录需要拆分的托盘记录
$(document).ready(function(){
	var getId="000000";
	if(getId!=null && getId!=""){
		dg=$('#dg').datagrid({    
			method: "get",
		    url:'${ctx}/bis/transferinfo/alljson', 
		    fit : true,
			fitColumns :true,//水平滚动
			border : false,
			idField : 'LAB',
			striped:true,
			pagination:false,
			rownumbers:true,
			pageNumber:1,
			pageSize :1000,
			pageList : [1000],
			singleSelect:true,
		    columns:[[    
				{field:'STOCK_NAME',title:'存货方',sortable:false,width:250},    
		        {field:'BILL_NUM',title:'提单号',sortable:false,width:150},
		        {field:'CTN_NUM',title:'箱号',sortable:false,width:150 },
		        {field:'SKU_ID',title:'SKU',sortable:false,width:150},
		        {field:'RK_NUM',title:'入库号',sortable:false,width:150},
		        {field:'CARGO_NAME',title:'品名',sortable:false,width:150},
		        {field:'ENTER_STATE',title:'入库类型',sortable:false,width:80,
		        	formatter : function(value, row, index) {
		        		if(value==0){return '成品'}else if(value==1){return '货损'}
		        	}
		        },
		        {field:'PIECE',title:'出库件数',sortable:false,width:100},
		        {field:'NET_SINGLE',title:'',hidden:true},
		        {field:'GROSS_SINGLE',title:'',hidden:true},
		        {field:'LAB',title:'lab',hidden:true},
		        {field:'NUM',title:'',hidden:true},
		        {field:'ADDORDEL',title:'',hidden:true}
		    ]],
		    queryParams: {
		    	transferId:'${obj.transferId}'
			},
			onLoadSuccess:function(){
				var rows = $('#dg').datagrid('getRows');
				if(rows!=null && rows.length>0){
					for(var i = 0; i<rows.length; i++){
						$('#dg').datagrid('updateRow',{
                    		index:i,
                    		row: {
                    			STOCK_NAME:'${obj.stockIn}',
                    			NUM:i+1
                    		}
                    	});
					}
				}else{
					 //删除拆托按钮
	    			 rmtga();
				}
				 insertSum();
			},
		    enableHeaderClickMenu: true,
		    enableHeaderContextMenu: true,
		    enableRowContextMenu: false,
		    toolbar:'#tb'
		});
	}//end if
	<c:if test="${obj.isTSpliet ==2}">
	intoTTaryTabl();
	</c:if>
	//结算单位
	var getReceiverOrgId='${obj.receiverOrgId}';
	$('#receiverOrgId').combobox({
		   method:"GET",
		   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${obj.receiverOrgId}",
		   valueField: 'ids',
		   textField: 'clientName',
		   mode:'remote',
		   onSelect:function(){
 				var getTxt=$('#receiverOrgId').combobox("getText");
 				$("#receiverOrg").val(getTxt);
		   },
		   
		   onLoadSuccess:function(){
				if(getReceiverOrgId!=null && getReceiverOrgId!=""){
					 $('#receiverOrgId').combobox("select",Number(getReceiverOrgId));
					 getReceiverOrgId="";
				}
		   }
   	});
	//原货主
	var getStockInId='${obj.stockInId}';
	$('#stockInId').combobox({
		   method:"GET",
		   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${obj.stockInId}",
		   valueField: 'ids',
		   textField: 'clientName',
		   mode:'remote',
		   onSelect:function(){
			   	var getOldTxt=$("#stockIn").val();
			   	var getId=$('#stockInId').combobox("getValue");
			   	var receiverOrgId=$('#receiverOrgId').combobox("getValue");
			   	var getTxt=$('#stockInId').combobox("getText");
				$("#stockIn").val(getTxt);
				//原货主改变
			   	if(getOldTxt!=null && getOldTxt!="" && getOldTxt!=getTxt){
			   		var inforows =$('#dg').datagrid('getRows');
					if(!rowIsNull(inforows) ){
						$("#skuNumdiv").html("");//情况之前添加的数据，
						//清空已经添加的明细信息，添加明细删除记录
						for(var i=inforows.length;i>0;i--){
							$("#delListdiv").append("<input type=\"hidden\" name=\"delList\" value=\""+inforows[i-1]["LAB"]+"\" >");
						    $('#dg').datagrid('deleteRow',i-1); 
						}
					}
			   	}
			   	
			   	//判断是否需要改变存货方结算单位
				if(!$("#isBuyFee").is(":checked")){
				   //添加联系人
					 if(""== $("#receiverLinker").val()){
						 addLinkUser(getId);
					 }
				    //如果收货方跟结算方不一样要以结算方为准
				    if(getId!=receiverOrgId){
				    	getId=receiverOrgId;
				    }
					$("#receiverOrg").val(getTxt);
					$('#receiverOrgId').combobox({
						   method:"GET",
						   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid="+getId,
						   valueField: 'ids',
						   textField: 'clientName',
						   mode:'remote',
						   onChange: function (newVal,oldVal){
									if(newVal != ""){
										//清除费用分摊
										$('#feeId').combobox("clear");
										feeSelect(newVal);
									}
						   },
						   onLoadSuccess:function(){
								if(getId!=null && getId!=""){
									 $('#receiverOrgId').combobox("select",getId);
									 getId="";
								}
						   }
				   	});
				}
		   },
		   onLoadSuccess:function(){
				if(getStockInId!=null && getStockInId!=""){
					 $('#stockInId').combobox("select",Number(getStockInId));
					 getStockInId="";
				}
		   }
	});
	//收货方
	var getReceiver='${obj.receiver}';
	$('#receiver').combobox({
	   method:"GET",
	   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${obj.receiver}",
	   valueField: 'ids',
	   textField: 'clientName',
	   mode:'remote',
	   onSelect:function(){
	   		var getId=$('#receiver').combobox("getValue");
			var getTxt=$('#receiver').combobox("getText");
			$("#receiverName").val(getTxt);
	   		if($("#isBuyFee").is(":checked")){
			   //添加联系人
				 if(""== $("#receiverLinker").val()){
					 addLinkUser(getId);
				 }
				$("#receiverOrg").val(getTxt);
				$('#receiverOrgId').combobox({
					   method:"GET",
					   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid="+getId,
					   valueField: 'ids',
					   textField: 'clientName',
					   mode:'remote',
					   onChange: function (newVal,oldVal){
								if(newVal != ""){
									//清除费用分摊
									$('#feeId').combobox("clear");
									feeSelect(newVal);
								}
					   },
					   onLoadSuccess:function(){
							if(getId!=null && getId!=""){
								 $('#receiverOrgId').combobox("select",getId);
								 getId="";
							}
					   }
			   	});
			}
	   },
	   onLoadSuccess:function(){
			if(getReceiver!=null && getReceiver!=""){
				 $('#receiver').combobox("select",Number(getReceiver));
				 getReceiver="";
			}
	   }
   	});
	//添加仓库
	$.ajax({
	   type: "GET",
	   url: "${ctx}/base/warehouse/getWarehouse",
	   data: "",
	   dataType: "json",
	   success: function(date){
		   if(date!=null && date.length>0){
			   $('#warehouseId').combobox({
				   data : date,
				   value:'${obj.warehouseId}',
				   valueField:'id',
				   textField:'warehouseName',
				   editable : false,
				   onSelect:function(){
					   //清空已经添加的明细信息，添加明细删除记录
						var rowsLength=$('#dg').datagrid('getRows').length;
						for(var i=rowsLength;i>0;i--){
							$("#delListdiv").append("<input type=\"hidden\" name=\"delList\" value=\""+addrows[i-1]["LAB"]+"\" >");
						    $('#dg').datagrid('deleteRow',i-1); 
						}
				   }
				   
			   });
		   }
	   }
	});
	
	//库存数量校验
	$('#checknumform').form({    
	    onSubmit: function(){
	    	$("#cstockIn").val($("#stockInId").combobox('getValue')); 
	    	$("#cwarehouseId").val($("#warehouseId").combobox('getValue'));
	    	var url="${ctx }/bis/transfer/check?time="+Math.random();
	    	$('#checknumform').attr("action",url);
			return true;
	    },    
	    success:function(data){
	    	var jsonObj=eval('(' + data + ')');
	    	 if("success"==jsonObj.retStr){
	    		var dgrows =$('#dg').datagrid('getRows');
	 			var rowsLength=dgrows.length;
//	 	    	for(var i=rowsLength;i>0;i--){
//	 				$("#delListdiv").append("<input type=\"hidden\" name=\"delList\" value=\""+dgrows[i-1]["LAB"]+"\" >");
//	 			    $('#dg').datagrid('deleteRow',i-1); 
//	 			}
				var abcd;
				var bug=0;
	    		for(var i=addrows.length-1;i>=0;i--){
						obj=addrows[i];
						console.log(obj);
						var j = parseInt(obj['NUM']);
						abcd=$("#piece"+Number(obj['NUM'])).val() ;
 		            		if( typeof(abcd) == "undefined" || abcd=="" || bug==1){
 		            			index = $('#dg').datagrid('getRowIndex',obj['LAB']);
 		            			if(index>=0){
	 		            			var newrow2 = dgrows[index];
	 		            			abcd = newrow2.PIECE;
	 		            			$("#piece"+Number(obj['NUM'])).val(abcd);
	// 		            			console.log( $("#piece"+Number(obj['NUM'])).val() );
								}
 		            		}
 		            		if(addrows.length>1 && i>0 && j==0){
 		            			bug =1;
 		            		}
						//if(!checkInfoIsHave(obj['LAB'])){
							if(""!=abcd && abcd>0 ){
								addTBRow(obj,abcd);
							}
						//}
					}
					for(var i=rowsLength;i>0;i--){
		 				$("#delListdiv").append("<input type=\"hidden\" name=\"delList\" value=\""+dgrows[i-1]["LAB"]+"\" >");
		 			    $('#dg').datagrid('deleteRow',i-1); 
		 			}
					d.panel('close');
	    	 }else if("pledged"==jsonObj.retStr){
	    		 parent.$.easyui.messager.alert("货种小类:"+jsonObj.classType+" 本次出库数数量 "+jsonObj.countNum+"超过当前可用数量"+jsonObj.availableNum+" 请确认!");
	    	 }
	    	 
	    	 else{
	    		 parent.$.easyui.messager.alert("SKU:"+jsonObj.sku+" 质押数量加货转数量大于库存量！");
	    	 }
	    }    
	});
})	

$("#isBuyFee").click(function(){
	if($("#isBuyFee").is(":checked")){
		 var getId=$('#receiver').combobox("getValue");
		 var getTxt=$('#receiver').combobox("getText");
		 $("#receiverOrg").val(getTxt);
		 $('#receiverOrgId').combobox({
		       method:"GET",
			   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid="+getId,
			   valueField: 'ids',
			   textField: 'clientName',
			   mode:'remote',
			   onChange: function (newVal,oldVal){
					if(newVal != ""){
						//清除费用分摊
						$('#feeId').combobox("clear");
						feeSelect(newVal);
					}
			   },
			   onLoadSuccess:function(){
					if(getId!=null && getId!=""){
						$('#receiverOrgId').combobox("select",getId);
						getId="";
					}
			   }
		});
	}else{
					var getId=$('#stockInId').combobox("getValue");
					var getTxt=$('#stockInId').combobox("getText");
					$("#receiverOrg").val(getTxt);
					$('#receiverOrgId').combobox({
						   method:"GET",
						   url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid="+getId,
						   valueField: 'ids',
						   textField: 'clientName',
						   mode:'remote',
						   onChange: function (newVal,oldVal){
									if(newVal != ""){
										//清除费用分摊
										$('#feeId').combobox("clear");
										feeSelect(newVal);
									}
						   },
						   onLoadSuccess:function(){
								if(getId!=null && getId!=""){
									 $('#receiverOrgId').combobox("select",getId);
									 getId="";
								}
						   }
				   	});
	}
});


function insertSum(){
		    var rows = $('#dg').datagrid('getRows');
	    	var pieces=0;
	    	var net=0;
	    	var gross=0;
	    	for(var i=0;i<rows.length;i++){
	    			pieces += Number(rows[i]['PIECE']);
	    			net += Number(rows[i]['PIECE']) * Number(rows[i]['NET_SINGLE']);
	    			gross += Number(rows[i]['PIECE']) * Number(rows[i]['GROSS_SINGLE']);
	    		};
	    	$("#pieceInfo").val(pieces);
	    	$("#netInfo").val(net.toFixed(8));
	    	$("#grossInfo").val(gross.toFixed(8));	
		
}

//添加收货联系人
function addLinkUser(id){
	$.post("${ctx }/base/client/get/"+id, {},function(data){
			     if(data.ids!=null && data.ids>0){
			    	 $("#receiverLinker").val(data.contactMan);
			     }
    }, "json");
}
//添加货转托盘信息
function intoTTaryTabl(){
	$('#dgt').datagrid({    
		method: "get",
	    url:'${ctx}/bis/ttrayinfo/alljson', 
	    fit : true,
		fitColumns :false,//水平滚动
		border : false,
		idField : 'stockNum',
		striped:true,
		pagination:false,
		rownumbers:true,
		pageNumber:1,
		pageSize :1000,
		pageList : [1000],
		singleSelect:true,
	    columns:[[ 
			{field:'stockNum',title:'托盘号',sortable:false,width:150},
	        {field:'billNum',title:'提单号',sortable:false,width:150},
	        {field:'ctnNum',title:'箱号',sortable:false,width:150 },
	        {field:'sku',title:'SKU',sortable:false,width:150},
	        {field:'cargoName',title:'品名',sortable:false,width:150},
	        {field:'enterState',title:'入库类型',sortable:false,width:80,
	        	formatter : function(value, row, index) {
	        		if(value==0){return '成品'}else if(value==1){return '货损'}
	        	}
	        },
	        {field:'piece',title:'件数',sortable:false,width:100},
	        {field:'grossWeight',title:'',hidden:true},
	        {field:'netWeight',title:'',hidden:true}
	    ]],
	    queryParams: {
	    	transferId:'${obj.transferId}'
		},
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tbt'
	});
 	$("#topbutton").remove();
}
//删除托盘货转按钮
function rmtga(){
	var addhtm="<a href=\"javascript:void(0)\" id=\"dtga\" class=\"easyui-linkbutton l-btn l-btn-small l-btn-plain myadd\" iconCls=\"icon-remove\" plain=\"true\" data-options=\"disabled:false\" onclick=\"delTruck()\"><span class=\"l-btn-left l-btn-icon-left\"><span class=\"l-btn-text\">取消托盘货转</span><span class=\"l-btn-icon icon-remove\">&nbsp;</span></span></a>" ;
	$("#tga").before(addhtm);
	$("#tga").remove();
}
//根据收货方获得费用方案列表
var getVal="${obj.feeId}";
function feeSelect(newVal){
var clientId = newVal;
	$.ajax({
   		async:false,
		type : "GET",
		url : "${ctx}/wms/enterStock/selFeePlan",
		data : {"clientId":clientId},
		dataType : "json",
		success : function(date) {
		    if(date.length == 0){
				$('#feeId').combobox({
					data : date,
					valueField : 'schemeNum',
					textField : 'schemeName',
					editable : false
				});
				 
		    }else{
				$('#feeId').combobox({
					data : date,
					valueField : 'schemeNum',
					textField : 'schemeName',
					editable : false,
					onSelect:function(){
							var getTxt=$('#feeId').combobox("getText");;
							$("#feePlan").val(getTxt);
					},
					onLoadSuccess:function(){
						if(getVal!=null && getVal!=""){
							 $('#feeId').combobox("select",Number(getVal));
							 getVal="";
						}
				    }
				});
			}
		}
	});
}
//货转表单提交校验
$('#transferform').form({    
    onSubmit: function(){
    	var isValid = $(this).form('validate');
    	if(isValid){
			$("#stockIn").val($('#stockInId').combobox("getText"));
    		//给明细div添加参数
			$("#infoListdiv").html("");
        	$($('#dg').datagrid('getRows')).each(function(i){
        		$("#infoListdiv").append("<input type=\"hidden\" name=\"skuList\" value=\""+$(this).attr("SKU_ID")+"\" >");
        		$("#infoListdiv").append("<input type=\"hidden\" name=\"billList\" value=\""+$(this).attr("BILL_NUM")+"\" >");
        		$("#infoListdiv").append("<input type=\"hidden\" name=\"ctnList\" value=\""+$(this).attr("CTN_NUM")+"\" >");
        		$("#infoListdiv").append("<input type=\"hidden\" name=\"foodList\" value=\""+$(this).attr("CARGO_NAME")+"\" >");
        		$("#infoListdiv").append("<input type=\"hidden\" name=\"intoList\" value=\""+$(this).attr("ENTER_STATE")+"\" >");
        		$("#infoListdiv").append("<input type=\"hidden\" name=\"peacList\" value=\""+$(this).attr("PIECE")+"\" >");
        		$("#infoListdiv").append("<input type=\"hidden\" name=\"netList\" value=\""+$(this).attr("NET_SINGLE")+"\" >");
        		$("#infoListdiv").append("<input type=\"hidden\" name=\"grossList\" value=\""+$(this).attr("GROSS_SINGLE")+"\" >");
        		$("#infoListdiv").append("<input type=\"hidden\" name=\"rkList\" value=\""+$(this).attr("RK_NUM")+"\" >");
    		});
    	}else{
    		savenum=0;
    	}
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){
    	var jsonObj=eval('(' + data + ')');
    	 if("SUCCESS"==jsonObj.endStr){
    		 parent.$.easyui.messager.show({title: "操作提示", msg: "保存成功！", position: "bottomRight"});
    		 var rows = $('#dg').datagrid('getRows');
   			 if(rows!=null && rows.length>0){
    				$(".myadd").remove();
       	     	var inhtml="<span class=\"toolbar-item dialog-tool-separator myadd\"></span><a href=\"javascript:void(0)\" id=\"tga\" class=\"easyui-linkbutton l-btn l-btn-small l-btn-plain myadd\" iconCls=\"icon-add\" plain=\"true\" data-options=\"disabled:false\" onclick=\"createTruck()\"><span class=\"l-btn-left l-btn-icon-left\"><span class=\"l-btn-text\">托盘货转</span><span class=\"l-btn-icon icon-add\">&nbsp;</span></span></a>";
    		  	 	$("#tbo").append(inhtml);
   			 }
   			$('#transferform').attr("action","${ctx }/bis/transfer/update"); 
   			savenum=0;
    	 }else if("OVER"==jsonObj.endStr){
    	 	parent.$.easyui.messager.show({title: "操作提示", msg: "保存成功！但此存货方库存已达到警戒线，无法继续货转！", position: "bottomRight"});
    		 var rows = $('#dg').datagrid('getRows');
   			 if(rows!=null && rows.length>0){
    				$(".myadd").remove();
       	     	var inhtml="<span class=\"toolbar-item dialog-tool-separator myadd\"></span><a href=\"javascript:void(0)\" id=\"tga\" class=\"easyui-linkbutton l-btn l-btn-small l-btn-plain myadd\" iconCls=\"icon-add\" plain=\"true\" data-options=\"disabled:false\" onclick=\"createTruck()\"><span class=\"l-btn-left l-btn-icon-left\"><span class=\"l-btn-text\">托盘货转</span><span class=\"l-btn-icon icon-add\">&nbsp;</span></span></a>";
    		  	 	$("#tbo").append(inhtml);
   			 }
   			$('#transferform').attr("action","${ctx }/bis/transfer/update"); 
   			savenum=0;
    	 }else{
    		 parent.$.easyui.messager.alert("保存失败！");
    		 savenum=0;
    	 }
    }    
});
//打开费用分摊
function getFee(){
	if( $('#receiver').combobox('getValue') == "" || $('#stockInId').combobox('getValue') ==""){
		parent.$.messager.show({ title : "提示",msg: "请先选择收货方和存货方！", position: "bottomRight" });
		return;
	}
	d=$("#dlg").dialog({   
		    title: '费用选择',    
		    width: 700,    
		    height: 340,    
		    href:'${ctx}/wms/outstock/getfee',
		    maximizable:true,
		    modal:true,
		    buttons:[{
				text:'增加',
				handler:function(){
	               addFee();
				}
			},{
				text:'取消',
				handler:function(){
					d.panel('close');
				}
			}]
		});
}
//添加费用分摊数据
function addFee(){
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
		data : {"sellList":sellList,"buyList":buyList},
		dataType : "json",
		success: function(data){
			$("#sellFee").val(data[0]);
	 		$("#buyFee").val(data[1]);
	 		d.panel('close');
		}
	});	
}
//open货转明细添加
function addTransferIfno(){
	var getClientId=$("#stockInId").combobox("getValue"); 
	var getCKId= $('#warehouseId').combobox("getValue");
	if(getClientId==null || getClientId==""){
		parent.$.easyui.messager.show({title: "操作提示", msg: "请选择存货方！", position: "bottomRight"});
		return false;
	}else if(getCKId==null || getCKId==""){
		parent.$.easyui.messager.show({title: "操作提示", msg: "请选择仓库！", position: "bottomRight"});
		return false;
	}else{
		d=$("#dlg").dialog({   
		    title: '货转明细选择',    
		    width: 800,    
		    height: 500,    
		    href:'${ctx}/bis/transfer/openinfos/'+getClientId+"/"+getCKId,
		    maximizable:true,
		    modal:true,
		    buttons:[{
				text:'确认',
 				handler:function(){
					addrows = $("#infodg").datagrid("getSelections");
					var obj;
					var abc;
					var index;
					var rows = $('#dg').datagrid('getRows');
					// 需要判断输入件数要小于（库存数量+质押量）
					if(addrows!=null && addrows.length>0){
						var bug=0;
						$("#skuNumdiv").html("");
						for(var i=addrows.length-1;i>=0;i--){
//						$(addrows).each(function(i){
		            		var row=addrows[i];
		            		var j = parseInt(row['NUM']);
		            		abc = $("#piece"+row['NUM']).val();
		            		if(typeof(abc) == "undefined" || abc=="" || bug==1){
		            			index = $('#dg').datagrid('getRowIndex',row['LAB']);
		            			if(index >=0){
			            			var newrow = rows[index];
			            			abc = newrow.PIECE;
		            			}
		            		}
		            		if(addrows.length>1 && i>0 && j==0){
		            			bug =1;
		            		}
		            		if($("#SKU"+row["SKU_ID"]).length>0){
		            			var getVal=$("#VAL"+row["SKU_ID"]).val();
		            			$("#VAL"+row["SKU_ID"]).val(Number(getVal)+Number(abc));
		            		}else{
		            			$("#skuNumdiv").append("<input type=\"text\" id=\"SKU"+row["SKU_ID"]+"\" name=\"skuList\" value=\""+row["SKU_ID"]+"\" >");
			            		$("#skuNumdiv").append("<input type=\"text\" id=\"VAL"+row["SKU_ID"]+"\" name=\"peacList\" value=\""+abc+"\" >"); 
		            		}
		            	}
						//提交数据校验库存
						$("#checknumform").submit();
					}
				}
			},{
				text:'取消',
				handler:function(){
					d.panel('close');
				}
			}]
		}); 
	} //end if
}

//执行出库订单明细添加一行
function addTBRow(rowObj,abcd){
	var rows = $('#dg').datagrid('getRows');
	$('#dg').datagrid('insertRow',{
		index: rows.length+1,
		row:{
			STOCK_NAME:rowObj["STOCK_NAME"],
			BILL_NUM:rowObj["BILL_NUM"],
			CTN_NUM:rowObj["CTN_NUM"],
			SKU_ID:rowObj["SKU_ID"],
			CARGO_NAME:rowObj["CARGO_NAME"],
			ENTER_STATE:rowObj["ENTER_STATE"],
			PIECE:abcd,
			NET_SINGLE:rowObj["NET_SINGLE"],
			GROSS_SINGLE:rowObj["GROSS_SINGLE"],
			LAB:rowObj["LAB"],
			RK_NUM:rowObj["RK_NUM"],
			ADDORDEL:1
		}
	});
}
var savenum=0;
//执行保存操作
function save(){
	if( $("#isBuyFee").is(":checked") ){
		if( $("#startStoreDate").datebox("getValue")!="" && $("#feeId").combobox("getValue") !="" ){
			if(savenum==0){
				savenum++;
				// 显示loading
				parent.$.messager.progress({title: "处理中", msg: "正在保存数据，请稍候...", text: "加载中..."});
				// 5秒后自动关闭loading
				setTimeout(() => {
					parent.$.messager.progress('close');
				}, 5000);
				$("#transferform").submit();
				// const progress = parent.$.messager.progress({
				// 	title: "处理中",
				// 	msg: "正在保存数据，请稍候...",
				// 	text: "加载中..."
				// });
				// // 5秒自动关闭（兜底）
				// const timer = setTimeout(() => {
				// 	parent.$.messager.progress('close');
				// }, 5000);
				// // 异步提交表单
				// $.ajax({
				// 	url: $("#transferform").attr("action"), // 表单提交地址
				// 	type: $("#transferform").attr("method") || "post", // 提交方式（默认post）
				// 	data: $("#transferform").serialize(), // 序列化表单数据
				// 	success: function(response) {
				// 		// 提交成功（根据实际后端返回判断）
				// 		clearTimeout(timer); // 清除兜底定时器
				// 		parent.$.messager.progress('close'); // 关闭loading
				// 		// 可添加成功提示
				// 		parent.$.messager.show({ title: "提示", msg: "保存成功！", position: "bottomRight" });
				// 		// 如需跳转或刷新页面，这里处理（例如：parent.location.reload();）
				// 	},
				// 	error: function() {
				// 		// 提交失败
				// 		clearTimeout(timer); // 清除兜底定时器
				// 		parent.$.messager.progress('close'); // 关闭loading
				// 		parent.$.messager.show({ title: "错误", msg: "保存失败，请重试！", position: "bottomRight" });
				// 	}
				// });
			} 
		}else{
			parent.$.messager.show({ title : "提示",msg: "请填写费用方案和计费日期！", position: "bottomRight" });
			return;
		}
	}else{
		if( $("#startStoreDate").datebox("getValue")!="") {
			if (savenum == 0) {
				savenum++;
				parent.$.messager.progress({title: "处理中", msg: "正在保存数据，请稍候...", text: "加载中..."});
				// 5秒后自动关闭loading
				setTimeout(() => {
					parent.$.messager.progress('close');
				}, 5000);
				$("#transferform").submit();
				// // 显示 loading
				// const progress = parent.$.messager.progress({
				// 	title: "处理中",
				// 	msg: "正在保存数据，请稍候...",
				// 	text: "加载中..."
				// });
				// // 5秒自动关闭（兜底）
				// const timer = setTimeout(() => {
				// 	parent.$.messager.progress('close');
				// }, 5000);
				// // 异步提交表单
				// $.ajax({
				// 	url: $("#transferform").attr("action"), // 表单提交地址
				// 	type: $("#transferform").attr("method") || "post", // 提交方式（默认post）
				// 	data: $("#transferform").serialize(), // 序列化表单数据
				// 	success: function(response) {
				// 		// 提交成功（根据实际后端返回判断）
				// 		clearTimeout(timer); // 清除兜底定时器
				// 		parent.$.messager.progress('close'); // 关闭loading
				// 		// 可添加成功提示
				// 		parent.$.messager.show({ title: "提示", msg: "保存成功！", position: "bottomRight" });
				// 		// 如需跳转或刷新页面，这里处理（例如：parent.location.reload();）
				// 	},
				// 	error: function() {
				// 		// 提交失败
				// 		clearTimeout(timer); // 清除兜底定时器
				// 		parent.$.messager.progress('close'); // 关闭loading
				// 		parent.$.messager.show({ title: "错误", msg: "保存失败，请重试！", position: "bottomRight" });
				// 	}
				// });
			}
		}else{
			parent.$.messager.show({ title : "提示",msg: "请填写计费日期！", position: "bottomRight" });
			return;
		}
	}
}
//遍历表单获取行号
function  getIndex(id){
	var nIndex='10000000';
	var rows = $('#dg').datagrid('getRows');
	for(var i=0;i<rows.length;i++){
		if(id==rows[i]["LAB"]){
			nIndex = i;
			break;
		}
	}
	return nIndex;
}
//明细删除
function del(){
	var row = dg.datagrid('getSelected');
	if(rowIsNull(row)) return;
	parent.$.messager.confirm('提示', '您确定要删除选中的行吗？', function(data){
		if(true==data){
			$("#delListdiv").append("<input type=\"hidden\" name=\"delList\" value=\""+row["LAB"]+"\" >");
			$('#dg').datagrid('deleteRow',getIndex(row["LAB"])); 
		}
	});
}
//打开拣货策略
function createTruck(){
	var rows = $('#dg').datagrid('getRows');
	if(rows==null || rows.length==0){
		 parent.$.easyui.messager.alert("请添加货转明细，保存后再进行托盘货转操作！");
		 return false;
	}
	$.ajax({
			async: false,
			type:'get',
			url:"${ctx}/bis/transfer/checkRank" ,
			data : {"transferId":$("#transferId").val(),"type":"1"},
			success: function(data){
				if(data != "success"){
					parent.$.easyui.messager.show({title: "操作提示", msg: "存货方剩余库存已超出警戒线！无法进行货转！", position: "bottomRight"});
					return;
				}else{
					// 需要 验证添加的货转明细是否是客户在该库下的最后的货
					$('#checkusernumform').submit();
				}
			}
		});	
	
}

//用户库存数量校验-- 验证添加的货转明细是否是客户在该库下的最后的货
$('#checkusernumform').form({    
    onSubmit: function(){
    	$("#ustockIn").val($("#stockInId").combobox('getValue')); 
    	$("#uwarehouseId").val($("#warehouseId").combobox('getValue'));
    	$("#addinfo").html("");
    	$($('#dg').datagrid('getRows')).each(function(i){
    		$("#addinfo").append("<input type=\"hidden\" name=\"skuList\" value=\""+$(this).attr("SKU_ID")+"\" >");
    		$("#addinfo").append("<input type=\"hidden\" name=\"billList\" value=\""+$(this).attr("BILL_NUM")+"\" >");
    		$("#addinfo").append("<input type=\"hidden\" name=\"ctnList\" value=\""+$(this).attr("CTN_NUM")+"\" >");
    		$("#addinfo").append("<input type=\"hidden\" name=\"intoList\" value=\""+$(this).attr("ENTER_STATE")+"\" >");
    		$("#addinfo").append("<input type=\"hidden\" name=\"peacList\" value=\""+$(this).attr("PIECE")+"\" >");
    	});
		return true;
    },    
    success:function(data){
    	var jsonObj=eval('(' + data + ')');
    	 if("success"==jsonObj.retStr){
    		 openTruckCL();
    	 }else{
    		 parent.$.messager.confirm('提示', '货转的明细是存货方在该仓库最后的货物是否进行托盘货转操作？', function(data){
   				if(true==data){
    		 		openTruckCL();
   				}
 			 });
    	 }
    }    
});
//打开货转策略
function openTruckCL(){
	d=$("#dlg").dialog({   
	    title: '货转策略选择',    
	    width: 1000,    
	    height:500,    
	    href:'${ctx}/base/stategy/check/${obj.transferId}',
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				$("#mainform").submit();
			}
		},{
			text:'取消',
			handler:function(){
				d.panel('close');
			}
		}]
	}); 
}
//打开托盘拆分页面
function openSplitT(trasperid){
	dt=$("#dtg").dialog({   
	    title: '托盘拆分',    
	    width: 500,    
	    height: 300,    
	    href:'${ctx}/bis/loading/openct/'+trasperid,
	    maximizable:true,
	    modal:true,
	    resizable:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				$('#ctform').submit(); 
			}
		},{
			text:'返回策略选择',
			handler:function(){
				dt.panel('close');
				createTruck(); 
			}
		},{
			text:'取消',
			handler:function(){
				dt.panel('close');
			}
		}]
	}); 
}

//进入  货转费用调整  
function transferAdjust(){
	if($("#transferform").form('validate')){
		var linkId = $("#transferId").val();
		var stockId = $("input[name='stockInId']").val();

		$.ajax({
			type:'post',
			url:"${ctx}/bis/transfer/checkstate/",
			data : {"transferId": linkId},
			success: function(data){
				if(data.end == "0"){
					parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存货转联系单！", position: "bottomRight"});
					return;
				}else{
					window.parent.mainpage.mainTabs.addModule('费用', 'cost/standingBook/transferList/' + linkId + "/" + stockId);
				}
			}
		});	
	}
}

//下载
function down(){
	var url = "${ctx}/bis/transferinfo/download";
	$("#downForm").attr("action",url).submit();
}

//导入
function into(){
var transferId=$("#transferId").val(); 
$.ajax({
		type:'get',
		url:"${ctx}/bis/transfer/ifsave/"+transferId,
		dataType:"text",
		success: function(data){
			if(data != "success"){
				parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存货转联系单！", position: "bottomRight"});
				return;
			}else{
				intoc(transferId);
			}
		}
	});	
}


function intoc(transferId){
	dimp=$("#dlimp").dialog({   
	    title: "货转联系单明细导入",    
	    width: 450,    
	    height: 450,    
	    href:'${ctx}/bis/transferinfo/into/'+transferId,
	    maximizable:true,
	    modal:true,
	    buttons:[{
			text:'确认',
			handler:function(){
				$("#mainform3").submit(); 
				dimp.panel('close');
			}
		},{
			text:'取消',
			handler:function(){
				dimp.panel('close');
			}
		}],
		onClose: function (){
			window.setTimeout(function(){gridDgg()},400);
		}
		
	}); 
}

function gridDgg(){
	var getId="000000";
	if(getId!=null && getId!=""){
		dg=$('#dg').datagrid({    
			method: "get",
		    url:'${ctx}/bis/transferinfo/alljson', 
		    fit : true,
			fitColumns :false,//水平滚动
			border : false,
			idField : 'LAB',
			striped:true,
			pagination:false,
			rownumbers:true,
			pageNumber:1,
			pageSize :1000,
			pageList : [1000],
			singleSelect:true,
		    columns:[[    
				{field:'STOCK_NAME',title:'存货方',sortable:false,width:150},    
		        {field:'BILL_NUM',title:'提单号',sortable:false,width:150},
		        {field:'CTN_NUM',title:'箱号',sortable:false,width:150 },
		        {field:'SKU_ID',title:'SKU',sortable:false,width:150},
		        {field:'CARGO_NAME',title:'品名',sortable:false,width:150},
		        {field:'ENTER_STATE',title:'入库类型',sortable:false,width:80,
		        	formatter : function(value, row, index) {
		        		if(value==0){return '成品'}else if(value==1){return '货损'}
		        	}
		        },
		        {field:'PIECE',title:'出库件数',sortable:false,width:100},
		        {field:'NET_SINGLE',title:'',hidden:true},
		        {field:'GROSS_SINGLE',title:'',hidden:true},
		        {field:'LAB',title:'lab',hidden:true},
		        {field:'NUM',title:'',hidden:true},
		        {field:'ADDORDEL',title:'',hidden:true}
		    ]],
		    queryParams: {
		    	transferId:'${obj.transferId}'
			},
			onLoadSuccess:function(){
				var rows = $('#dg').datagrid('getRows');
				if(rows!=null && rows.length>0){
					if(model=="create"){
						for(var i = 0; i<rows.length; i++){
							$('#dg').datagrid('updateRow',{
	                    		index:i,
	                    		row: {
	                      			STOCK_NAME:$('#stockInId').combobox("getText"),
	                    			NUM:i+1
	                    		}
	                    	});
						}
					}else{
						for(var i = 0; i<rows.length; i++){
							$('#dg').datagrid('updateRow',{
	                    		index:i,
	                    		row: {
	                     			STOCK_NAME:'${obj.stockIn}',
	                    			NUM:i+1
	                    		}
	                    	});
						}
					}
				}else{
					 //删除拆托按钮
	    			 rmtga();
				}
			},
		    enableHeaderClickMenu: true,
		    enableHeaderContextMenu: true,
		    enableRowContextMenu: false,
		    toolbar:'#tb'
		});
	}//end if
}
//取消托盘货转
function delTruck(){
	 $.ajax({
			async: false,
			type:'get',
			url:"${ctx}/bis/transfer/checkRank" ,
			data : {"transferId":$("#transferId").val(),"type":"1"},
			success: function(data){
				if(data != "success"){
					parent.$.easyui.messager.show({title: "操作提示", msg: "收货方剩余库存已超出警戒线！无法取消货转！", position: "bottomRight"});
					return;
				}else{
					parent.$.messager.confirm('提示', '确定要执行取消货转明细吗？', function(data){
					if(true==data){
						$.ajax({
							type:'post',
							url:"${ctx}/bis/transfer/deltruck/"+$("#transferId").val(),
							data : "",
							dataType:"json",
							success: function(data){
								if(data.endstr == "success"){
								    intoTTaryTabl();
									var addhtm="<a href=\"javascript:void(0)\" id=\"tga\" class=\"easyui-linkbutton l-btn l-btn-small l-btn-plain myadd\" iconCls=\"icon-add\" plain=\"true\" data-options=\"disabled:false\" onclick=\"createTruck()\"><span class=\"l-btn-left l-btn-icon-left\"><span class=\"l-btn-text\">托盘货转</span><span class=\"l-btn-icon icon-add\">&nbsp;</span></span></a>";
		 							var savehtm="<a id=\"topbutton\" href=\"javascript:void(0)\" class=\"easyui-linkbutton l-btn l-btn-small l-btn-plain\" iconcls=\"icon-save\" plain=\"true\" onclick=\"save();\"  ><span class=\"l-btn-left l-btn-icon-left\"><span class=\"l-btn-text\">保存</span><span class=\"l-btn-icon icon-save\">&nbsp;</span></span></a>";
									$("#dtga").before(addhtm);
									$("#dtga").remove();
		 							$("#topbuttonspan").before(savehtm);
									return;
								}else if(data.endstr == "dis"){
									parent.$.easyui.messager.alert("已经进行过拆托操作的货物不得执行取消货转明细操作！"); 
								}else{
									 parent.$.easyui.messager.alert("执行取消货转明细操作失败！"); 
								}
							}
						});	
					}
					});
				}
			}
		});	
}
</script>
</body>
</html>