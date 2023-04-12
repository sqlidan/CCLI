<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>  
<script src="${ctx}/static/plugins/js/Tray/tray.js" type="text/javascript"></script>
<link href="${ctx}/static/css/Tray.css" type="text/css" rel="stylesheet"></link>   
</head>
<body>
	<div id="tb" style="padding: 5px; height: auto">
		<div>
			<form id="searchFrom" action="">
			    <select id="warehouseId" name="filter_LIKES_warehouseId" class="easyui-combobox" data-options="width:150,prompt: '仓库'" >
     	         <option value=""></option>
     	         </select>
				 <input type="text" id="BuildingNum" name="BuildingNum" class="easyui-validatebox" data-options="width:150,prompt: '楼号'" /> 
				<span class="toolbar-item dialog-tool-separator"></span>
				 <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>			  
        	</form>
		</div>
		<div id="divFram" style='margin-left: auto; margin-right: auto; '>
			<div style="text-align: center; padding: 5px; height: auto;">库位情况汇总图</div>
			<div  style='text-align: center;  vertical-align:middle;   padding: 5px; height: auto;'>
			 <div id="showtray" style="text-align: center; padding: 5px; height: auto; width:200px" >
				展示 
			 </div>
			</div>
		</div>
	</div>
	<script type="text/javascript">

		document.onkeydown = function () {if(event.keyCode == 13){cx();}};

		$(document).ready(function() {	 
			GetTray("0","");
		});
		 
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
		//创建查询对象并查询
		function cx() {
			var buildnum = $("#BuildingNum").val(); 
			var houseid = $("#warehouseId").combobox('getValue');
			if (jQuery.trim(buildnum) == "" && jQuery.trim(houseid) == "")
				buildnum = "0";  
			GetTray(buildnum,houseid);
		}
        
		function GetTray(budnum,houseid) { 
			$.ajax({
						type : 'POST',
						url : '${ctx}/report/Tray/GetFram?budnum='+budnum+'&houseid='+houseid,
						async : false, 
						dataType : "json",
						success : function(data) {
							if (data != null && jQuery.trim(data.toString()).length > 0) {
								StartShowFra(data,$(document).width());  
								$.ajax({
											type : 'POST',
											url : '${ctx}/report/Tray/GetFramInfo?budnum='+budnum+'&houseid='+houseid,
											async : true, 
											dataType : "json",
											success : function(datainfo) {
												StartShowInfo(datainfo); 
											}
										});
							} else { 
								$("#showtray").empty();
								$("#showtray").attr("style","text-align: center; padding: 5px; height: auto; ");	
								var housetext = $("#warehouseId").combobox('getText');
								var sStr = $('<div style=" text-align:center ; color:Red; margin:20px; ">'+budnum+'/'+housetext+'的基础数据尚未维护!</div>');
							     $(sStr).appendTo($("#showtray"));  
							}
						}
					});
		}
	</script>
</body>
</html>