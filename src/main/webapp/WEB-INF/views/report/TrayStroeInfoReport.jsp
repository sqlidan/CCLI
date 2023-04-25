<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>  
<script src="${ctx}/static/plugins/js/Tray/tray.js" type="text/javascript"></script> 

<link href="${ctx}/static/css/Tray.css" type="text/css" rel="stylesheet"></link>   
<style type="text/css">
 html,body {height:100%; *overflow:hidden; font-family: verdana,arial,sans-serif; } 
</style>
</head>
<body  >
	<div id="tb" style="padding: 5px; height: auto">
		<div>
			<form id="searchFrom" action="">
			     <select id="warehouseId" name="filter_LIKES_warehouseId" class="easyui-combobox"  data-options="width:150,prompt: '仓库'" >
     	         <option value=""></option>
     	         </select>
				 <input type="text" id="BuildingNum" name="BuildingNum" class="easyui-validatebox" data-options="width:150,prompt: '楼号'" /> 
				<span class="toolbar-item dialog-tool-separator"></span>
				 <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>			  
        	</form>
		</div>
		<div id="divFram" style='margin-left: auto; margin-right:auto; text-align:center; '>
			  <div id="top" >
				  <div class="content">
	                <div class="fl leftcol" id="fl">在库统计</div> 
	                <div class="fr rightcol" id="fr">日期 /(重量:吨)</div>
	                <div class="main">冷库货物存储示意图</div>
	                <div class="cl"></div>
	              </div>
			  </div>
			  <div id="nav" >
               <div id="showtray" style="padding: 5px; height: auto; width:auto;">展示 </div>
              </div>
              <div class="cl"></div>
			  <div id="foot" style='font-size:12px;'  >各类产品统计</div>
		</div>
	</div>
	<script type="text/javascript">

		document.onkeydown = function () {if(event.keyCode == 13){cx();}};

		$(document).ready(function() {	 
			
			 $.ajax({
				   type: "GET",
				   async : false,
				   url: "${ctx}/base/warehouse/getWarehouse",
				   data: "",
				   dataType: "json",
				   success: function(date){
						   $('#warehouseId').combobox({
							   data: date, 
							   valueField: 'id',
							   textField: 'warehouseName',
							   editable : false
						   });
				   }});
		  GetTrayProduct('','1'); 
		  $("#fr").empty();
		  $('#fr').html((new Date()).toLocaleString()+'&nbsp;&nbsp;(重量:吨)');
		  //$().appendTo($('#fr'));
		});
		
		//创建查询对象并查询
		function cx() {
			var buildnum = $("#BuildingNum").val(); 
			var houseid = $("#warehouseId").combobox('getValue');;
			if (jQuery.trim(buildnum) == "" && jQuery.trim(houseid) == "")
				houseid = "1";  
			GetTrayProduct(buildnum,houseid);
		}
        
		function GetTrayProduct(budnum,houseid) { 
			$.ajax({
						type : 'POST',
						url : '${ctx}/report/Tray/GetStroeSaveF?budnum='+budnum+'&houseid='+houseid,
						async : false, 
						dataType : "json",
						success : function(data) {
							if (data != null && jQuery.trim(data.toString()).length > 0) {
								DrawingStoreF(data,$(document).width());  
								$.ajax({
											type : 'POST',
											url : '${ctx}/report/Tray/GetTraySaveInfo?budnum='+budnum+'&houseid='+houseid,
											async : true, 
											dataType : "json",
											success : function(datainfo) {
												DrawingStoreInfo(datainfo); 
											}
										});
							} else { 
								$("#showtray").empty();
								$("#showtray").attr("style","text-align: center; padding: 5px; height: auto; ");								
								var housetext = $("#warehouseId").combobox('getText');
								var sStr = $('<div style=" text-align:center ; color:Red; margin:20px; ">'+budnum+'/'+housetext+'的基础数据尚未维护!</div>');
							    $(sStr).appendTo($("#showtray"));  
							    $('#fl').html('今日在库统计：--');
							}
						}
					});
			$.ajax({
				type : 'POST',
				url : '${ctx}/report/Tray/GetTrayProductSum?budnum='+budnum+'&houseid='+houseid,
				async : true, 
				dataType : "json",
				success : function(data) {
					DrawingProductSum(data); //foot
				}
			 });
		} 
	</script>
</body>
</html>