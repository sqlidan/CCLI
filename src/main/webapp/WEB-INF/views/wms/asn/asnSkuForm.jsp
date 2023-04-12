<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainSKUform" action="${ctx }/base/sku/createjx" method="post">
		<table class="formTable">
			<tr>
				<td>产品名称：</td>
				<td>
					<input id="cargoName" name="cargoName" class="easyui-validatebox"  data-options="width: 150 ,required:'required',validType:'length[1,1000]'" > 
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
                <td>账册商品序号：</td>
                <td><input id="accountBook" name="accountBook" type="text" class="easyui-validatebox"
                           data-options="width: 150"  /></td>
            </tr>
            <tr>
                <td>HS编码：</td>
                <td>
					<input id="textFilter" name="hsCode" class="easyui-combox" data-options="width: 150, required:'required'" />
                </td>
            </tr>
           <tr>
                <td>海关品名：</td>
                <td>
					<input id="itemnameFilter" name="hsItemname" class="easyui-combox" data-options="width: 150, required:'required'" />
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
					<input id="typeSize" name="typeSize" class="easyui-validatebox"  data-options="width: 150 ,validType:'length[0,10]'"> 
				</td>
			</tr>
			<tr>
				<td>件数：</td>
				<td>
					<input id="piece" name="piece" class="easyui-numberbox"  data-options="width: 150 ,number:'true',validType:'length[0,10]',required:'required'"> 
				</td>
			</tr>
			<tr>
				<td>总净重(KG)：</td>
				<td>
					<input id="netWeight" name="netWeight" class="easyui-numberbox"  data-options="width: 150 ,required:'required',number:'true',validType:'length[0,10]',precision:2"     value="${sku.netWeight }"> 
				</td>
			</tr>
			<tr>
				<td>总毛重(KG)：</td>
				<td>
					<input id="grossWeight" name="grossWeight" class="easyui-numberbox"  data-options="width: 150,required:'required' ,number:'true',validType:'length[0,10]',precision:2" > 
				</td>
			</tr>

			<!-- 
			<tr>
				<td>计量单位：</td>
				<td>
					<select class="easyui-combobox" id="units" name="units" data-options="width:150,required:'required'">
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
	/**
	$.ajax({
		   type: "GET",
		   url: "${ctx}/system/dict/json",
		   data: "filter_EQS_type=unitOfWeight",
		   dataType: "json",
		   success: function(date){
			   for(var i=0;i<date.rows.length;i++){
				   $('#units').combobox({
					   data : date.rows,
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
				   for(var i=0;i<date.rows.length;i++){
					   $('#cargoState').combobox({
						   data : date.rows,
						   valueField:'value',
						   textField:'label',
						   editable : false
					   });
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
//提交表单
$('#mainSKUform').form({   
    onSubmit: function(){  
    	$("#typeName").val($('#cargoType').combobox("getText"));
		$("#className").val($('#classType').combobox("getText"));
    	var isValid = $(this).form('validate');
		return isValid;	// 返回false终止表单提交
    },    
    success:function(data){
    	var obj=eval('(' + data + ')');
    	addTBRow(obj.skuId,obj.cargoName,obj.cargoState,"",obj.piece,obj.netWeight,obj.grossWeight,obj.netSingle,obj.grossSingle,obj.accountBook,obj.hsCode,obj.hsItemname,obj.remark);
    	d.panel('close');
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