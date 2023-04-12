<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body>
<div>
	<form id="mainform" action="${ctx}/wms/enterStock/${action}" method="post">
		<table class="formTable">
			<tr>
					<td>入库联系单号：</td>
					<td><input id=linkId name="linkId" class="easyui-validatebox" 
					data-options="width: 150,required:'required'" value="${bisEnterStock.linkId}" 
					maxlength="100" 
					<c:if test="${action =='sendbsl'}"> readonly style="background:#eee"</c:if>></td>
			</tr>
			<tr>
					<td>提单号：</td>
					<td><input id=itemNum name="itemNum" class="easyui-validatebox" 
					data-options="width: 150,required:'required'" value="${bisEnterStock.itemNum}" 
					maxlength="100" 
					<c:if test="${action =='sendbsl'}"> readonly style="background:#eee"</c:if>></td>
			</tr>
			<tr>
				<td>海关中文船名：</td>
				<td>
					<input id="chineseName" name="chineseName"  class="easyui-validatebox" data-options="width: 150, required:'required'"  value="${bisEnterStock.chineseName}" >
				</td>
			</tr>
			<tr>
				<td>进口航次：</td>
				<td>
				<input id="voyageNum" name="voyageNum" class="easyui-validatebox" data-options="width: 150,required:'required'" value="${bisEnterStock.voyageNum}" maxlength="100" >
				</td>
			</tr>
						<tr>
				<td>船名代码：</td>
				<td>
				<input id="vesselName" name="vesselName" class="easyui-validatebox" data-options="width: 150,required:'required'" value="${bisEnterStock.vesselName}" maxlength="100" >
				<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-zoom" plain="true"
                  onclick="onLoad()">加载</a>
<%--                  <input id="textFilter" name="hsCode" class="easyui-combox" data-options="width: 150, required:'required'" value="${enterStockInfo.hsCode}"/>
 --%>                  
				</td>
			</tr>
			<tr>
				<td>海关英文船名：</td>
				<td>
					<input id="englishName" name="englishName"  class="easyui-validatebox" data-options="width: 150, required:'required'"  value="${bisEnterStock.englishName}" >
				</td>
			</tr>
			<tr>
				<td>码头</td>
				<td><select id="dock" name="dock"
				class="easyui-combobox"
				data-options="width:180,prompt:'码头',value:'${bisEnterStock.dock}',valueField:'id',textField:'text', required:'required',data:[{id:'3',text:'三期'},{id:'4',text:'四期'},{id:'5',text:'五期'}]" />

				</td>			
			</tr>
		</table>
	</form>
</div>

<script type="text/javascript">
var action="${action}";
/* $(function(){  
	if( action =="create"){ 
		jiaoyan();
	}
}); */	

function jiaoyan(){
	$('#code').validatebox({    
	   	 	required: true, 
	    	validType:{
	    	remote:["${ctx}/base/hscode/checkhscode","code"]
	    }
	}); 
}


//提交表单
$('#mainform').form({
	
	onSubmit : function() {
		var isValid = $(this).form('validate');
		return isValid; // 返回false终止表单提交
	},
	success : function(data) {
		parent.$.messager.show({title: "提示", msg: "申报成功！", position: "bottomRight" });
	}
});
function onLoad() {
	 var chineseName = $("#chineseName").val();
	 var voyageNum= $("#voyageNum").val();
	 var linkId= $("#linkId").val()
	// var url='/ccli/wms/enterStock/lisths?voyageNum='+voyageNum+'&chineseName='+chineseName;
     var url=encodeURI(encodeURI('/ccli/wms/enterStock/lisths?voyageNum='+voyageNum+'&chineseName='+chineseName+'&linkId='+linkId));

	 //var url='/ccli/bis/trayinfo/update
     $.ajax({
         type: 'get',
         url: url,
         success: function (data) {
        	 if(data=="fail"){
                 parent.$.messager.show({title: "提示", msg: "未获取到该船名代码", position: "bottomRight"});
 
        	 }else{
           	 $("#vesselName").val(data);
            	 
                 // successTip(data, dg);
                  parent.$.messager.show({title: "提示", msg: "操作成功！", position: "bottomRight"});
       		 
        	 }
        	 
         	 
        	 
        	 }
        	// alert(data);
     
     });
}
 $(function() {
	 var chineseName = $("#chineseName").val();
	 var voyageNum= $("#chineseName").val();
	 var url='/ccli/wms/enterStock/lisths?voyageNum='+voyageNum+'&chineseName='+chineseName;
    $('#itemnameFilter').combobox({
        mode: 'remote',  //模式： 远程获取数据
        url: url,  //远程数据请求地址  
        valueField: 'cargoName',　　//value对应的属性字段
        textField: 'cargoName'　　　 //text对应的属性字段
   });
})
 
</script>
</body>
</html>