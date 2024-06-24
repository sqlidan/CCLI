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
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'west'" style="width:700px;height:auto;padding:5px;">
	<div id="tb1" style="padding:5px;height:auto">
		<div data-options="region:'center'" title="移库">
			<div style="padding:5px;height:auto" class="datagrid-toolbar">
				<form id="searchFrom1" action="">
					<input type="text" name="filter_LIKES_asn" class="easyui-validatebox" data-options="width:150,prompt: 'ASN'"/>
					<span class="toolbar-item dialog-tool-separator"></span>
					<input type="text" name="filter_LIKES_skuId" class="easyui-validatebox" data-options="width:150,prompt: 'SKU'"/>
					<span class="toolbar-item dialog-tool-separator"></span>
					<input type="text" name="filter_LIKES_billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
					<span class="toolbar-item dialog-tool-separator"></span>
					<input type="text" name="filter_LIKES_ctnNum" class="easyui-validatebox" data-options="width:150,prompt: '箱号'"/>
					<span class="toolbar-item dialog-tool-separator"></span>
					<input type="text" name="filter_LIKES_trayId" class="easyui-validatebox" data-options="width:150,prompt: '托盘号'"/>
					<span class="toolbar-item dialog-tool-separator"></span>
					<input type="text" name="filter_LIKES_warehouse" class="easyui-validatebox" data-options="width:150,prompt: '仓库号'"/>
					<span class="toolbar-item dialog-tool-separator"></span>
					<input type="text" name="filter_LIKES_buildingNum" class="easyui-validatebox" data-options="width:150,prompt: '楼号'"/>
					<span class="toolbar-item dialog-tool-separator"></span>
					<input type="text" name="filter_LIKES_floorNum" class="easyui-validatebox" data-options="width:150,prompt: '楼层号'"/>
					<span class="toolbar-item dialog-tool-separator"></span>
					<input type="text" name="filter_LIKES_RoomNum" class="easyui-validatebox" data-options="width:150,prompt: '房间号'"/>
					<span class="toolbar-item dialog-tool-separator"></span>
					<input type="text" name="filter_LIKES_areaNum" class="easyui-validatebox" data-options="width:150,prompt: '区位号'"/>
					<span class="toolbar-item dialog-tool-separator"></span>
					<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">搜索</a>
					<span class="toolbar-item dialog-tool-separator"></span>
					<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="clearSearch()">重置</a>
					<span class="toolbar-item dialog-tool-separator"></span>
				</form>
			</div>
		</div>
	</div>
	<table id="dg"></table>
</div>
<div data-options="region:'center'" style="width:700px;height:auto;padding:5px;">
	<div id="tb2" style="padding:5px;height:auto">
		<div data-options="region:'center'" title="货架信息">
			<div style="padding:5px;height:auto" class="datagrid-toolbar">
				<form id="searchFrom2" action="">
					<%--					<input type="text" id="buildingNum" name="buildingNum" class="easyui-validatebox" data-options="width:150,prompt: '楼号'"/>--%>
					<%--					<span class="toolbar-item dialog-tool-separator"></span>--%>
					<input type="text" id="floorNum" name="floorNum" class="easyui-validatebox" data-options="width:150,prompt: '楼层号'"/>
					<span class="toolbar-item dialog-tool-separator"></span>
					<input type="text" id="roomNum" name="roomNum" class="easyui-validatebox" data-options="width:150,prompt: '房间号'"/>
					<span class="toolbar-item dialog-tool-separator"></span>
					<input type="text" id="areaNum" name="areaNum" class="easyui-validatebox" data-options="width:150,prompt: '区位号'"/>
					<span class="toolbar-item dialog-tool-separator"></span>
					<%--					<input type="text" id="storeRoomNum" name="storeRoomNum" class="easyui-validatebox" data-options="width:150,prompt: '库房号'"/>--%>
					<%--					<span class="toolbar-item dialog-tool-separator"></span>--%>
					<input type="text" id="layers" name="layers" class="easyui-validatebox" data-options="width:150,prompt: '层数'"/>
					<span class="toolbar-item dialog-tool-separator"></span>
					<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="preview()">预览</a>
					<span class="toolbar-item dialog-tool-separator"></span>
					<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="confirm()">入库确认</a>
				</form>
			</div>
			<div id="divFram" style='margin-left: auto; margin-right:auto; text-align:center; '>
				<div style="text-align: center; padding: 5px; height: auto;">货架情况汇总图</div>
				<div  style='text-align: center;  vertical-align:middle;   padding: 5px; height: auto;'>
					<div id="showtray" style="text-align: center; padding: 5px; height: auto; width:200px" >
						展示
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	var dg;
	var d;
	document.onkeydown = function () {if(event.keyCode == 13){cx();}};

	$(function(){
		// selectAjax();
		gridDG();
	});

	function selectAjax(){
		//客户
		$('#clientName').combobox({
			method: "GET",
			url: "${ctx}/base/client/getClientAll",
			valueField: 'ids',
			textField: 'clientName',
			mode: 'remote'
		});
	}

	function gridDG(){
		dg=$('#dg').datagrid({
			method: "get",
			url:'${ctx}/wms/relocation/json',
			fit : true,
			border : false,
			fitColumns : true,
			idField : 'id',
			sortOrder:'desc',
			striped:true,
			pagination:true,
			rownumbers:true,
			pageNumber:1,
			pageSize : 20,
			pageList : [20, 30, 50, 100 ],
			singleSelect:false,//true-单选；false-复选
			frozenColumns: [[
				{field: 'id', title: 'ID',hidden:true},
			]],
			columns:[[
				{field:'asn',title:'ASN',sortable:true},
				{field:'skuId',title:'SKU',sortable:true},
				{field:'billNum',title:'提单号',sortable:true},
				{field:'ctnNum',title:'箱号',sortable:true},
				{field:'trayId',title:'托盘号',sortable:true},
				{field:'cargoType',title:'产品类型',sortable:true},
				{field:'cargoName',title:'产品名称',sortable:true},
				{field:'warehouse',title:'仓库号',sortable:true},
				{field:'buildingNum',title:'楼号',sortable:true},
				{field:'floorNum',title:'楼层号',sortable:true},
				{field:'RoomNum',title:'房间号',sortable:true},
				{field:'areaNum',title:'区位号',sortable:true},
				{field:'actualStoreroomX',title:'X坐标',sortable:true},
				{field:'actualStoreroomZ',title:'Z坐标',sortable:true},
			]],
			enableHeaderClickMenu: true,
			enableHeaderContextMenu: true,
			enableRowContextMenu: false,
			toolbar:'#tb1'
		});
	}

	//搜索
	function cx(){
		dg.datagrid('clearSelections');
		var obj=$("#searchFrom1").serializeObject();
		dg.datagrid('load',obj);
	}
	//清空搜索条件
	function clearSearch(){
		dg.datagrid('clearSelections');
		$('#searchFrom1').form('clear');
	}
	//预览
	function preview(){
		// var buildingNum = $("#buildingNum").val();//楼号
		var floorNum = $("#floorNum").val();//楼层号
		var roomNum = $("#roomNum").val();//房间号
		var areaNum = $("#areaNum").val();//区号
		// var storeRoomNum = $("#storeRoomNum").val();//库房号
		var layers = $("#layers").val();//货架层数
		if (floorNum == "") {
			parent.$.messager.show({title: "提示", msg: "请输入楼层号！", position: "bottomRight"});
			return;
		}
		if (roomNum == "") {
			parent.$.messager.show({title: "提示", msg: "请输入房间号号！", position: "bottomRight"});
			return;
		}
		if (areaNum == "") {
			parent.$.messager.show({title: "提示", msg: "请输入区号号！", position: "bottomRight"});
			return;
		}
		var rows = dg.datagrid('getSelections');
		var ids= [];
		if(rows !== undefined && rows !== null && rows.length > 0){
			if (layers == "") {
				parent.$.messager.show({title: "提示", msg: "请输入货架层数号！", position: "bottomRight"});
				return;
			}
			for(var i=0; i<rows.length; i++){
				ids.push(rows[i].id);
			}
		}
		GetTray(ids,floorNum,roomNum,areaNum,layers);
	}

	function GetTray(ids,floorNum,roomNum,areaNum,layers) {
		$.ajax({
			type : 'POST',
			url : '${ctx}/wms/relocation/GetData?ids='+ids+'&floorNum='+floorNum+'&roomNum='+roomNum+'&areaNum='+areaNum+'&layers='+layers,
			async : false,
			dataType : "json",
			success : function(data) {
				if (data != null && jQuery.trim(data.toString()).length > 0) {
					StartShowHJ(data,$(document).width(),data[0].MAXX,data[0].MAXZ);
				} else {
					$("#showtray").empty();
					$("#showtray").attr("style","text-align: center; padding: 5px; height: auto; ");
					var sStr = $('<div style=" text-align:center ; color:Red; margin:20px; ">'+'基础数据尚未维护!</div>');
					$(sStr).appendTo($("#showtray"));
				}
			}
		});
	}
	// =================================================================================
	function confirm(){
		// var buildingNum = $("#buildingNum").val();//楼号
		var floorNum = $("#floorNum").val();//楼层号
		var roomNum = $("#roomNum").val();//房间号
		var areaNum = $("#areaNum").val();//区号
		// var storeRoomNum = $("#storeRoomNum").val();//库房号
		var layers = $("#layers").val();//货架层数
		if (floorNum == "") {
			parent.$.messager.show({title: "提示", msg: "请输入楼层号！", position: "bottomRight"});
			return;
		}
		if (roomNum == "") {
			parent.$.messager.show({title: "提示", msg: "请输入房间号号！", position: "bottomRight"});
			return;
		}
		if (areaNum == "") {
			parent.$.messager.show({title: "提示", msg: "请输入区号号！", position: "bottomRight"});
			return;
		}
		// if (layers == "") {
		// 	parent.$.messager.show({title: "提示", msg: "请输入货架层数号！", position: "bottomRight"});
		// 	return;
		// }
		var rows = dg.datagrid('getSelections');
		var ids= [];
		for(var i=0; i<rows.length; i++){
			ids.push(rows[i].id);
		}
		var target = floorNum+"-"+roomNum+"-"+areaNum;
		parent.$.messager.confirm('提示', '确认入库后无法恢复您确定要入库么？', function(data){
			if (data){
				$.ajax({
					async: false,
					type: 'POST',
					url: "${ctx}/wms/relocation/confirm/"+target,
					data: JSON.stringify(rows),
					success: function(res){
						dg.datagrid('clearSelections');
						successTip(res, dg);
					}
				});
			}
		});
	}
</script>

</body>
</html>