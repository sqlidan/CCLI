<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title></title>
	<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
<div data-options="region:'center'" title="入库报关单总览" style="overflow-y:auto">
	<div style="padding:5px;height:auto" class="datagrid-toolbar">
		<form id="searchFrom" action="">
			<input type="text" name="cdNum" class="easyui-validatebox" data-options="width:150,prompt: '业务单号'"/>
			<select name="serviceProject" class="easyui-combobox" data-options="width:150,prompt: '服务项目' " >
				<option  value=""></option>
				<option  value="0">报进</option>
				<option  value="1">报出</option>
			</select>
			<select type="text" name="auditingState" class="easyui-combobox" data-options="width:150,prompt: '审核状态'">
				<option  value=""></option>
				<option  value="0">保存</option>
				<option  value="1">已提交</option>
				<option  value="2">已驳回</option>
				<option  value="3">已审核</option>
			</select>
			<select type="text" name="modeTrade" class="easyui-combobox" data-options="width:150,prompt: '贸易方式'">
				<option  value=""></option>
				<option  value="0">保税</option>
				<option  value="1">来料加工</option>
				<option  value="2">进料加工</option>
				<option  value="3">一般贸易</option>
			</select>
			<input type="text" name="billNum" class="easyui-validatebox" data-options="width:150,prompt: '提单号'"/>
			<input type="text" name="customsDeclarationNumber" class="easyui-validatebox" data-options="width:150,prompt: '报关单号'"/>
			<input type="text" id="searchStrTime" name="searchStrTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
				   data-options="width:150,prompt: '申报开始日期'"/>

			- <input type="text" id="searchEndTime" name="searchEndTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss"
					 data-options="width:150,prompt: '申报结束日期'"/>
			<!--    	<select id="searchStock" name="filter_LIKES_clientName" class="easyui-combobox" data-options="width:150,prompt: '客户名称'" >
                        </select>
                        -->
			<!--  <input type="text" name="filter_LIKES_cdNum" class="easyui-validatebox" data-options="width:150,prompt: '联系单号'"/> -->
			<!--  <input type="text" name="filter_GED_declareTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '申报开始时间'"/>
             <input type="text" name="filter_LED_declareTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '申报结束时间'"/> -->
			<span class="toolbar-item dialog-tool-separator"></span>
			<a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a>
		</form>
		<form id="searchFrom3" action="">
		</form>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-add" plain="true" onclick="window.parent.mainpage.mainTabs.addModule('通关管理','wms/customs/clearance/manager')">添加</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" data-options="disabled:false" onclick="del()">删除</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="update()">修改</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="okpass()">审核</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-delete" plain="true" onclick="nopass()">取消审核</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<%--			<shiro:hasPermission name="wms:outstock:export">  </shiro:hasPermission>--%>
		<!-- 				<a href="javascript:void(0)" class="easyui-linkbutton"
                           iconCls="icon-standard-page-excel" plain="true"
                           onclick="exportExcel()">导出EXCEL</a>
                        <span class="toolbar-item dialog-tool-separator"></span> -->
		<a href="javascript:void(0)" class="easyui-linkbutton"
		   iconCls="icon-standard-page-excel" plain="true"
		   onclick="exportExcelOnlyOne()">导出选中EXCEL</a>
		<span class="toolbar-item dialog-tool-separator"></span>

		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportExcel()">导出</a>
		<span class="toolbar-item dialog-tool-separator"></span>
		<%-- <shiro:hasPermission name="wms:customs:export">
          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportExcel()">导出</a>
          <span class="toolbar-item dialog-tool-separator"></span>
      </shiro:hasPermission>
      <shiro:hasPermission name="wms:customs:exportinfo">
          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-page-excel" plain="true" onclick="exportInfo()">导出（带货物信息）</a>
          <span class="toolbar-item dialog-tool-separator"></span>
      </shiro:hasPermission>
      <shiro:hasPermission name="wms:customs:print">
          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="print()">打印报关单统计</a>
          <span class="toolbar-item dialog-tool-separator"></span>
      </shiro:hasPermission>
       <shiro:hasPermission name="wms:customs:printinfo">
          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-printer" plain="true" onclick="printInfo()">打印报关单货物信息统计</a>
          <span class="toolbar-item dialog-tool-separator"></span>
      </shiro:hasPermission>
       <shiro:hasPermission name="wms:customs:extension">
          <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-munich-star" plain="true" onclick="extension()">展期</a>
          <span class="toolbar-item dialog-tool-separator"></span>
      </shiro:hasPermission> --%>
		<!--
 <shiro:hasPermission name="wms:customs:wc">
        	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-user-edit" plain="true" onclick="wc()">完成</a>
        	<span class="toolbar-item dialog-tool-separator"></span>
        </shiro:hasPermission>-->
	</div>
	<table id="dg"></table>
</div>
<!-- <div data-options="region:'south',split:true,border:false" title="入库报关货物信息汇总"  style="height:200px">
	<form id="searchFrom2" action="">
			<div style="padding:5px;height:auto" class="datagrid-toolbar">
      	    <input type="text" name="filter_LIKES_itemNum" class="easyui-validatebox" data-options="width:150,prompt: '项号'"/>
	      <input type="text" name="filter_GED_recordTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '录入开始时间'"/>
	        <input type="text" name="filter_LED_recordTime" class="easyui-my97" datefmt="yyyy-MM-dd HH:mm:ss" data-options="width:150,prompt: '录入结束时间'"/>
	       
	        <span class="toolbar-item dialog-tool-separator"></span>
	        <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="zx()">查询</a>
	        </div>
		</form>
<table id="dgg"></table> 
</div> -->

<div id="dlg"></div>
<script type="text/javascript">
	var dg;
	var d;
	var dgg


	document.onkeydown = function () {if(event.keyCode == 13){cx();}};

	$(function(){
		ajaxS();
		gridDG();
	});

	//入库报关单列表
	function gridDG(){
		dg=$('#dg').datagrid({
			method: "get",
			url:'${ctx}/wms/customs/clearance/json',
			fit : true,
			fitColumns : true,
			border : false,
			idField : 'forId',
			sortOrder:'desc',
			striped:true,
			pagination:true,
			rownumbers:true,
			pageNumber:1,
			pageSize : 20,
			pageList : [ 10, 20, 30, 40, 50 ],
			singleSelect:true,
			columns:[[
				{field:'CD_NUM',title:'业务单号',width:35,align:'center'},
				{field:'CLIENT_NAME',title:'客户名称',sortable:true,width:40},
				{field:'SERVICE_PROJECT',title:'服务项目',sortable:true,width:20,align:'center',
					formatter : function(value, row, index) {
						if(value == '0'){
							return "报进";
						}
						if(value == '1'){
							return "报出";
						}
					}},
				{field:'DECLARE_TIME',title:'申报日期',sortable:true,width:20,align:'center'},
				{field:'AUDITING_STATE',title:'审核状态',sortable:true,width:20,
					formatter : function(value, row, index) {
						if(value == '0'){
							return "保存";
						}
						if(value == '1'){
							return "已提交";
						}
						if(value == '2'){
							return "已驳回";
						}
						if(value == '3'){
							return "已审核";
						}
					}},
				{field:'MODE_TRADE',title:'贸易方式',sortable:true,width:20,
					formatter : function(value, row, index) {
						if(value == '0'){
							return "保税";
						}
						if(value == '1'){
							return "来料加工";
						}
						if(value == '2'){
							return "进料加工";
						}
						if(value == '3'){
							return "一般贸易";
						}
					}},
				{field:'BILL_NUM',title:'提单号',sortable:true,width:20},
				{field:'CUSTOMS_DECLARATION_NUMBER',title:'报关单号',sortable:true,width:20},
				{field:'CONTRY_ORAGIN',title:'原产国',sortable:true,width:20},
				{field:'ACCOUNT_BOOK',title:'账册商品序号',sortable:true,width:20},
				{field:'NUM',title:'数量',sortable:true,width:20},
				{field:'NET_WEIGHT',title:'净重',sortable:true,width:20},
				{field:'GROSS_WEIGHT',title:'毛重',sortable:true,width:20},
				{field:'MONEY',title:'金额',sortable:true,width:20},
				{field:'CURRENCY_VALUE',title:'币制',sortable:true,width:20,
					formatter : function(value, row, index) {
						if(value == '1'){
							return "美元";
						}
						if(value == '2'){
							return "日元";
						}
						if(value == '3'){
							return "欧元";
						}
						if(value == '4'){
							return "英镑";
						}
						if(value == '0'){
							return "人民币";
						}
					}},
				{field:'OPERATOR',title:'创建人',sortable:true,width:20},
				{field:'OPERATE_TIME',title:'创建日期',sortable:true,width:20},
				{field:'EXAMINE_PERSON',title:'审核人',sortable:true,width:20},
				{field:'EXAMINE_TIME',title:'审核日期',sortable:true,width:20,align:'center'},
			]],
			/* onClickRow:function(rowIndex, rowData){
                info(rowData.id);
            }, */
			enableHeaderClickMenu: true,
			enableHeaderContextMenu: true,
			enableRowContextMenu: false,
			toolbar:'#tb'
		});
	}

	//客户名称下拉
	function ajaxS(){
		//客户
		$('#searchStock').combobox({
			method:"GET",
			url:"${ctx}/base/client/getClientAll?filter_EQS_clientSort=0",
			valueField: 'ids',
			textField: 'clientName',
			mode:'remote'
		});
	}


	//删除
	function del(){
		var row = dg.datagrid('getSelected');
		var cdNum = row.CD_NUM;
		if(rowIsNull(row)) return;
		if(row.AUDITING_STATE == 3){
			parent.$.messager.alert("提示","已审核的记录不能删除！");
			return;
		}
		parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
			if (data){
				$.ajax({
					type:'get',
					url:"${ctx}/wms/customs/clearance/delete/"+cdNum,
					success: function(data){
						successTip(data,dg);
					}
				});
			}
		});
	}

	//修改
	function update(){
		var row = dg.datagrid('getSelected');
		if(rowIsNull(row)) return;
		window.parent.mainpage.mainTabs.addModule('通关管理修改','wms/customs/clearance/update/' + row.CD_NUM);
	}

	//创建查询对象并查询
	function cx(){
		dg.datagrid('clearSelections');
		var obj=$("#searchFrom").serializeObject();

		dg.datagrid('load',obj);
	}

	function zx(){
		var obj=$("#searchFrom2").serializeObject();
		dgg.datagrid('load',obj);
	}

	//导出
	function exportExcel(){
		var url = "${ctx}/wms/customs/clearance/export";
		var obj=$("#searchFrom").serializeObject();
		var strTime=obj.searchStrTime;
		var endTime=obj.searchEndTime;
		if(!strTime || !endTime){
			parent.$.messager.show({ title : "提示",msg: "请选择申报时间起止范围", position: "bottomRight" });
			return;
		}
		if((new Date(endTime)-new Date(strTime))/(1000*60*60*24)>30){
			parent.$.messager.show({ title : "提示",msg: "申报时间范围不要大于30天", position: "bottomRight" });
			return;
		}

		$("#searchFrom").attr("action",url).submit();
	}
	//导出一条
	function exportExcelOnlyOne(){

		var row = dg.datagrid('getSelected');
		if (rowIsNull(row)){
			parent.$.messager.show({ title : "提示",msg: "请选择一条通关台账数据！", position: "bottomRight" });
			return;

		}
		/*
            var auditingState= $("#searchFrom  select[name='auditingState']").val("");
            $("#searchFrom  select[name='serviceProject']").val("");
            $("#searchFrom  select[name='modeTrade']").val("");

            $("#searchFrom input[name='billNum']").val("");

            $("#searchFrom input[name='customsDeclarationNumber']").val("");



            var cdNUm= $("#searchFrom input[name='cdNum']").val(row.CD_NUM);
            var url = "${ctx}/wms/customs/clearance/export";
	$("#searchFrom").attr("action",url).submit();*/

		var url = "${ctx}/wms/customs/clearance/export/" + row.CD_NUM;
		$("#searchFrom").attr("action", url).submit();

	}

	/*

    //导出(带货物)
    function exportInfo(){
        var row = dg.datagrid('getSelected');
        if(rowIsNull(row)){

           return;
        }
        var url = "${ctx}/wms/customs/exportwith/"+row.id;
 	$("#searchFrom3").attr("action",url).submit();
}
 */

	/* //打印
    function print(){
        var rows = $('#dg').datagrid("getRows");
        var ids= new Array();
        for(var i=0;i<rows.length;i++){
            ids[i] = rows[i].id;
        }
         window.parent.mainpage.mainTabs.addModule('入库报关单打印','wms/customs/print/' + ids);
    }

    //带有货物信息的打印
    function printInfo(){
        var row = dg.datagrid('getSelected');
        if(rowIsNull(row)){
           parent.$.messager.show({ title : "提示",msg: "请选择一条入库报关单数据！", position: "bottomRight" });
           return;
        }
        window.parent.mainpage.mainTabs.addModule('入库报关单（带货物信息）打印','wms/customs/printInfo/' + row.id);
    }
     */

	//审核查验
	function okpass(){
		var row = dg.datagrid('getSelected');
		if(rowIsNull(row)) return;
		if(row.AUDITING_STATE == 3)
		{
			parent.$.easyui.messager.show({title: "操作提示", msg: "已是审核状态！", position: "bottomRight"});
			return;
		}
		/*     if(row.CUSTOMS_DECLARATION_NUMBER == null || row.CUSTOMS_DECLARATION_NUMBER == ""){
                  parent.$.messager.alert("提示","报关单号不能为空！");
                  return;
              }  */
		var cdNum = row.CD_NUM;
		parent.$.messager.confirm('提示', '您确定要审核？', function(data){
			if (data){
				$.ajax({
					type:'get',
					url:"${ctx}/wms/customs/clearance/okpass/" + cdNum,
					success: function(data){
						if(data == "success"){
							successTip(data, dg);
							parent.$.easyui.messager.show({title: "操作提示", msg: "审核成功！", position: "bottomRight"});
						}
						if(data == "error"){
							parent.$.messager.alert("提示","审核人不能为创建人");
						}
					}
				});
			}
		});
	}

	//取消审核查验
	function nopass(){
		var row = dg.datagrid('getSelected');
		if(rowIsNull(row)) return;
		if(row.AUDITING_STATE != 3)
		{
			parent.$.easyui.messager.show({title: "操作提示", msg: "已是未审核状态！", position: "bottomRight"});
			return;
		}
		var cdNum = row.CD_NUM;
		parent.$.messager.confirm('提示', '您确定取消此的审核？', function(data){
			if (data){
				$.ajax({
					type:'get',
					url:"${ctx}/wms/customs/clearance/nopass/" + cdNum,
					success: function(data){
						if(data == "success"){
							successTip(data, dg);
							parent.$.easyui.messager.show({title: "操作提示", msg: "取消审核成功！", position: "bottomRight"});
						}
					}
				});
			}
		});
	}

</script>
</body>
</html>