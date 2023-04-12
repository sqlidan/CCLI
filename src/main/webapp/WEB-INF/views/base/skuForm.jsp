<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform" action="${ctx}/base/sku/${action}" method="post" >
		<table class="formTable">
		<input id="isupdatesku" name ="isupdatesku" value="<c:choose><c:when test="${action == 'update'}">2</c:when><c:otherwise>1</c:otherwise></c:choose>" type="hidden"> 
		
		<c:if test="${action == 'update'}">
			<tr>
				<td>SKU_ID：</td>
				<td>
					<input type="hidden" id="operator" name="operator" value="${sku.operator }"/>
					<input type="hidden" id="operateTime" name="operateTime" value="<fmt:formatDate value="${sku.operateTime }" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					<input id="skuId" name="skuId" class="easyui-validatebox"  data-options="width: 150 '" readonly="readonly" value="${sku.skuId }"> 
				</td>
			</tr>
		</c:if>
			<tr>
				<td>产品名称：</td>
				<td>
					<input id="cargoName" required="true" name="cargoName" class="easyui-validatebox"  data-options="width: 150 ,required:'required',validType:'length[1,1000]'"     value="${sku.cargoName }"> 
					<input id="hidcargoName" name ="hidcargoName" value="${sku.cargoName}" type="hidden">
				</td>
			</tr>
			<tr> 
				<td>产品大类：</td>
				<td>
					<input type="hidden" id="typeName" name="typeName">
					<select class="easyui-combobox" id="cargoType" name="cargoType"  data-options="width:150,required:'required' ">
						<option value=""></option>
					</select>
				</td>
			</tr>
			<tr> 
				<td>产品小类：</td>
				<td>
					<input type="hidden" id="className" name="className">
					<select class="easyui-combobox" id="classType" name="classType"  data-options="width:150,required:'required' ">
						<option value=""></option>
					</select>
				</td>
			</tr>
			<tr> 
				<td>库存类型：</td>
				<td>
					<select class="easyui-combobox" id="cargoState" name="cargoState" data-options="width:150,required:'required' ">
						<option value=""></option>
					</select>
				</td>
			</tr>
			 <tr>
				<td>规格：</td>
				<td>
					<input id="typeSize" name="typeSize" class="easyui-validatebox"  data-options="width: 150 ,validType:'length[0,10]'"     value="${sku.typeSize }"> 
				</td>
			</tr>
			<tr>
				<td>总毛重(KG)：</td>
				<td>
					<input id="grossWeight" name="grossWeight" class="easyui-validatebox"  data-options="width: 150,required:'required' ,number:'true',validType:'length[0,10]',precision:2"  value="${sku.grossWeight}"  oninput="getGrossSingle()" onpropertychange="getGrossSingle()"  onkeyup="value=value.replace(/[^\d+(\.\d{2})?$]/g,'')"> 
				</td>
			</tr>
			<tr>
				<td>总净重(KG)：</td>
				<td>
					<input id="netWeight" name="netWeight" class="easyui-validatebox"  data-options="width: 150 ,required:'required',number:'true',validType:'length[0,10]',precision:2"  value="${sku.netWeight}"   oninput="getNetSingle()" onpropertychange="getNetSingle()"  onkeyup="value=value.replace(/[^\d+(\.\d{2})?$]/g,'')"> 
				</td>
			</tr>
			<tr>
				<td>件数：</td>
				<td>
					<input id="piece" name="piece" class="easyui-validatebox"  data-options="width: 150 ,required:'required',number:'true',validType:'length[0,10]',precision:2"  value="${sku.piece}"  oninput="getWeight()" onpropertychange="getWeight()" onkeyup="value=value.replace(/[^\d]/g,'')"> 
				</td>
			</tr>
			<tr>
				<td>单毛重(KG)：</td>
				<td>
					<input id="grossSingle" name="grossSingle" class="easyui-validatebox"  data-options="width: 150,required:'required' ,number:'true',validType:'length[0,15]',precision:2"  value="${sku.grossSingle }"  style="background:#eee"  readonly > 
				    <input id="hidgrossSingle" name ="hidgrossSingle" value="${sku.grossSingle }" type="hidden">
				</td>
			</tr>
			<tr>
				<td>单净重(KG)：</td>
				<td>
					<input id="netSingle" name="netSingle" class="easyui-validatebox"  data-options="width: 150 ,required:'required',number:'true',validType:'length[0,15]',precision:2"  value="${sku.netSingle }"  style="background:#eee"  readonly > 
				     <input id="hidnetSingle" name ="hidnetSingle" value="${sku.netSingle }" type="hidden">
				</td>
			</tr>
			<!-- 
			<tr>
				<td>计量单位：</td>
				<td>
					<select class="easyui-combobox" id="units" name="units" data-options="width:150 ">
						<option value=""></option>
					</select>
				</td>
			</tr>
			 -->
			<tr>
				<td>有效日期：  </td>
				<td><input name="validityTime" type="text" class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width: 150"  value="<fmt:formatDate value="${sku.validityTime}"/>" /></td>
			</tr>
			<tr>
				<td>属性1：</td>
				<td>
					<input id="attribute1" name="attribute1" class="easyui-validatebox"  data-options="width: 150 ,validType:'length[0,20]',precision:2"     value="${sku.attribute1 }"> 
				</td>
			</tr>
			<tr>
				<td>属性2：</td>
				<td>
					<input id="attribute2" name="attribute2" class="easyui-validatebox"  data-options="width: 150 ,validType:'length[0,20]',precision:2"     value="${sku.attribute2 }"> 
				</td>
			</tr>
			<tr>
				<td>属性3：</td>
				<td>
					<input id="attribute3" name="attribute3" class="easyui-validatebox"  data-options="width: 150 ,validType:'length[0,20]',precision:2"     value="${sku.attribute3 }"> 
				</td>
			</tr>
			<tr>
				<td>MSC：</td>
				<td>
					<input id="mscNum" name="mscNum" class="easyui-validatebox"  data-options="width: 150 ,validType:'length[0,10]',precision:2"     value="${sku.mscNum }"> 
				</td>
			</tr>
			<tr>
				<td>LOT：</td>
				<td>
					<input id="lotNum" name="lotNum" class="easyui-validatebox"  data-options="width: 150 ,validType:'length[0,10]',precision:2"     value="${sku.lotNum }"> 
				</td>
			</tr>
			<tr>
				<td>PRO：</td>
				<td>
					<input id="proNum" name="proNum" class="easyui-validatebox"  data-options="width: 150 ,validType:'length[0,10]',precision:2"     value="${sku.proNum }"> 
				</td>
			</tr>
			<tr>
				<td>入库号：</td>
				<td>
					<input id="rkdh" name="rkdh" class="easyui-validatebox"  data-options="width: 150 ,validType:'length[0,10]',precision:2"     value="${sku.rkdh }"> 
				</td>
			</tr>
			<tr>
				<td>捕捞船号：</td>
				<td>
					<input id="shipNum" name="shipNum" class="easyui-validatebox"  data-options="width: 150 ,validType:'length[0,10]',precision:2"     value="${sku.shipNum }"> 
				</td>
			</tr>
			<tr>
				<td>备注：</td>
				<td><textarea rows="3" cols="41" class="easyui-validatebox" data-options="validType:'length[0,50]'" name="remark" style="font-size: 12px;font-family: '微软雅黑'">${sku.remark}</textarea></td>
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
 
var action="${action}";
$(document).ready(function(){
	$.ajax({
		   type: "GET",
		   url: "${ctx}/base/product/listjson",
		   data: "filter_EQI_printId=0",
		   dataType: "json",
		   success: function(date){
			   if(date!=null && date.rows.length>0){
				   $('#cargoType').combobox({
					   data : date.rows,
					   value : '${sku.cargoType }',
					   valueField:'id',
					   textField:'pName',
					   onSelect:function(){
						   addClassType($('#cargoType').combobox("getValue"));
					   },
					   editable : false
					   <c:if test="${action == 'update'}"> 
					   ,disabled:true
					   </c:if> 
				   });
			   }
			  
		   }
		});
	if("update"==action){
		addClassType('${sku.cargoType }');
	}
	/**
	$.ajax({
		   type: "GET",
		   url: "${ctx}/system/dict/json",
		   data: "filter_EQS_type=unitOfWeight",
		   dataType: "json",
		   success: function(date){
			   if(date!=null && date.rows.length>0){
				   $('#units').combobox({
					   data : date.rows,
					   value : '${sku.units }',
					   valueField:'value',
					   textField:'label',
					   editable : false
				   });
			   }
		   }
		});
	*/
	$.ajax({
			   type: "GET",
			   url: "${ctx}/system/dict/json",
			   data: "filter_EQS_type=inventoryType",
			   dataType: "json",
			   success: function(date){
				   if(date!=null && date.rows.length>0){
					   $('#cargoState').combobox({
						   data : date.rows,
						   value : '${sku.cargoState }',
						   valueField:'value',
						   textField:'label',
						   editable : false
					   });
				   }
			   }
			});
	
	//------------- 
	
	//提交表单 
$('#mainform').form({    
    onSubmit: function(){  
		$("#typeName").val($('#cargoType').combobox("getText")); 
		$("#className").val($('#classType').combobox("getText")); 
    	var isValid =  $(this).form('validate');
    	return isValid; 
    },    
    success:function(data){    
    	if(data == "success"){
 			d.panel('close'); 
 			parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight" });
		}else if(data == "nosuccess"){
			parent.$.messager.show({title: "提示", msg: "保存失败!", position: "bottomRight" });
		}else{
			parent.$.messager.show({title: "提示", msg: "保存失败！", position: "bottomRight" });
		}
    }    
});  
	
});  
//添加货品小类
function addClassType(printeId){
	$.ajax({
		   type: "GET",
		   url: "${ctx}/base/product/listjson",
		   data: "filter_EQI_printId="+printeId,
		   dataType: "json",
		   success: function(date){
			   if(date!=null && date.rows.length>0){
				   $('#classType').combobox({
					   data : date.rows,
					   value : '${sku.classType }',
					   valueField:'id',
					   textField:'pName',
					   editable : false
					   <c:if test="${action == 'update'}"> 
					   ,disabled:true
					   </c:if>
				   });
			   }
			  
		   }
		});
}

function getWeight(){
  	  if( $("#netWeight").val() != "" && $("#piece").val() !="" ){
  	  	$("#netSingle").val( ($("#netWeight").val()/$("#piece").val()).toFixed(8) );
  	  }
  	  if( $("#grossWeight").val() != "" && $("#piece").val() !="" ){
  	  	$("#grossSingle").val( ($("#grossWeight").val()/$("#piece").val()).toFixed(8) );
  	  }
}

//计算单净
function getNetSingle(){
	  if( $("#netWeight").val() != "" && $("#piece").val() !="" ){
  	  	$("#netSingle").val( ($("#netWeight").val()/$("#piece").val()).toFixed(8) );
  	  }
}

//计算单毛
function getGrossSingle(){
	  if( $("#grossWeight").val() != "" && $("#piece").val() !="" ){
  	  	$("#grossSingle").val( ($("#grossWeight").val()/$("#piece").val()).toFixed(8) );
  	  }
}


<!--//提交表单
$('#mainform').form({    
    onSubmit: function(){ 
		$("#typeName").val($('#cargoType').combobox("getText"));
		$("#className").val($('#classType').combobox("getText"));
    	var isValid = $(this).form('validate');
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){   
    	successTip(data,dg,d);
    }    
});    
-->
 
</script>
</body>
</html>