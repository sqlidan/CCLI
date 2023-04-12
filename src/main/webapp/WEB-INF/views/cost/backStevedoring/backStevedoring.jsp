<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>

</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">
	<div data-options="region:'north',split:true,border:false"
		style="height: 140px">
		<div style="padding: 5px; height: auto" class="datagrid-toolbar">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-save" plain="true" onclick="finish()">完成</a> <a
				href="javascript:void(0)" class="easyui-linkbutton"
				iconCls="icon-standard-lock-delete" plain="true" onclick="concel()">取消完成</a>
		</div>
		<table>
			<tr>
				<td>提单号</td>
				<td><input id="billNumc" value="" class="easyui-validatebox"
					data-options="width:180" /></td>
				<td><input id="cx" onclick="cx()" value="查询" type="button" /></td>
			</tr>
			<tr>
				<td>状态</td>
				<td><input id="state" class="easyui-validatebox"
					data-options="width:180" value="" readonly style="background: #eee" /></td>
				<td>联系单号</td>
				<td><input id="linkId" class="easyui-validatebox"
					data-options="width:180" value="" readonly style="background: #eee" /></td>
				<td>客户名称</td>
				<td><input id="client" class="easyui-validatebox"
					data-options="width:180" value="" readonly style="background: #eee" />
					<input type="hidden" id="backdateb" name="backdateb" value="" /> <input
					type="hidden" id="stockIdb" name="stockIdb" value="" /></td>
				<td>提单号</td>
				<td><input id="billNum" class="easyui-validatebox"
					data-options="width:180" value="" readonly style="background: #eee" /></td>
			</tr>
			<tr>
				<td>倒箱吨数</td>
				<td><input id="weight" class="easyui-validatebox"
					data-options="width:180" value="" readonly style="background: #eee" />吨</td>
				
			</tr>
			
		</table>
	</div>
	<div
		data-options="region:'center',split:true,border:false,title:'倒箱装卸数量统计信息'"
		style="width: 400px">
		<div id="tb" style="padding: 5px; height: auto"
			class="datagrid-toolbar">
			<div>
				<shiro:hasPermission name="cost:backstevedoring:add">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						iconCls="icon-add" plain="true" onclick="add()">添加</a>
					<span class="toolbar-item dialog-tool-separator"></span>
				</shiro:hasPermission>
				<shiro:hasPermission name="cost:backstevedoring:delete">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						iconCls="icon-standard-delete" plain="true" onclick="deleteIt()">删除</a>
				</shiro:hasPermission>
			</div>
		</div>
		<table id="dgg"></table>
		<div id="dlg"></div>
	</div>


	<script type="text/javascript">
		var dgg;
		var d;
		var billNum;
		var action = "${action}";

		document.onkeydown = function() {
			if (event.keyCode == 13) {
				cx();
			}
		};

		$(function() {
			billNum = 0;
			zx(billNum);
			if (action == 'update') {
				cx();
			}
		})

		function cx() {
			
			if (action == 'update') {
				billNum = "${billNum}";
				$("#billNumc").val(billNum);
			} else {
				billNum = $("#billNumc").val();
			}
			if (billNum == "") {
				parent.$.easyui.messager.show({
					title : "操作提示",
					msg : "请输入提单号！",
					position : "bottomRight"
				});
				return;
			}
			billNum=billNum.replace(new RegExp("/","g" ),"笑脸");
			$.ajax({
				type : 'POST',
				url : "${ctx}/cost/backstevedoring/cx/" + billNum,
				dataType : "json",
				success : function(msg) {
					if (msg != null && msg != "") {
						$("#linkId").val(msg[0]);
						$("#client").val(msg[1]);
						$("#billNum").val(msg[2]);
						$("#weight").val(Number(msg[3]) / 1000);
						$("#stockIdb").val(msg[4]);
						$("#backdateb").val(msg[5]);
						zx(msg[2]);
					} else {
						parent.$.easyui.messager.show({
							title : "操作提示",
							msg : "未查到此提单号！",
							position : "bottomRight"
						});
						return;
					}
				}
			});
		}

		//倒箱装卸货物数量统计信息
		function zx(billNum) {
			billNum=billNum.replace(new RegExp("/","g" ),"笑脸");
			dgg = $('#dgg').datagrid({
				method : "get",
				url : '${ctx}/cost/backstevedoring/zxjson/' + billNum,
				fit : true,
				fitColumns : false,//水平滚动
				border : false,
				idField : 'skuId',
				striped : true,
				pagination : false,
				rownumbers : true,
				pageNumber : 1,
				pageSize : 1000,
				pageList : [ 1000 ],
				singleSelect : false,
				columns : [ [ {
					field : 'ID',
					title : 'ID',
					hidden : true
				}, {
					field : 'CLIENT',
					title : '装卸队',
					sortable : false,
					width : 150
				}, {
					field : 'FEE_PLAN',
					title : '装卸队方案',
					sortable : false,
					width : 100
				}, {
					field : 'MR',
					title : 'MR',
					sortable : false,
					width : 100
				}, {
					field : 'SORTING_NUM',
					title : '在库分拣数量',
					sortable : false,
					width : 100
				}, {
					field : 'MAN_NUM',
					title : '人工装卸数量',
					sortable : false,
					width : 100
				}, {
					field : 'WRAP_NUM',
					title : '缠膜数量',
					sortable : false,
					width : 100
				}, {
					field : 'PACK_NUM',
					title : '打包数量',
					sortable : false,
					width : 100
				},{
					field : 'NLABLE_NUM',
					title : '内标签数量',
					sortable : false,
					width : 100
				},{
					field : 'WLABLE_NUM',
					title : '外标签数量',
					sortable : false,
					width : 100
				},{
					field : 'MT_NUM',
					title : '码托数量',
					sortable : false,
					width : 100
				},{
					field : 'ZTJ_NUM',
					title : '装铁架数量',
					sortable : false,
					width : 100
				},{
					field : 'CTJ_NUM',
					title : '拆铁架数量',
					sortable : false,
					width : 100
				}, {
					field : 'IF_OK',
					title : '是否已完成',
					hidden : true
				}, {
					field : 'DROBACKDATE',
					title : '日期',
					sortable : false,
					width : 100
				},{
					field : 'IF_ALL_MAN',
					title : '是否全人工',
					sortable : false,
					width : 150,
					formatter : function(value, row, index) {
						return value == 0 ? '否' : '是';
					}
				} ] ],
				onLoadSuccess : function(data) {
					if (data.total == 0) {
						$("#state").val("未完成");
					} else {
						var row = dgg.datagrid('getData').rows[0];
						if (row.IF_OK == 0) {
							$("#state").val("未完成");
						} else {
							$("#state").val("已完成");
						}
					}
				},
				enableHeaderClickMenu : true,
				enableHeaderContextMenu : true,
				enableRowContextMenu : false,
				toolbar : '#tb'
			});
		}

		//添加装卸明细
		function add() {
			if ($("#billNum").val() == "") {
				parent.$.messager.show({
					title : "提示",
					msg : "请先进行查询操作！",
					position : "bottomRight"
				});
				return;
			}
			if ($("#state").val() == "已完成") {
				parent.$.messager.show({
					title : "提示",
					msg : "已完成状态无法进行添加操作！",
					position : "bottomRight"
				});
				return;
			}
			//判断是否有相应的费用方案
			d = $("#dlg").dialog({
								title : '新增倒箱装卸',
								width : 480,
								height : 480,
								href : '${ctx}/cost/backstevedoring/add',
								maximizable : true,
								modal : true,
								buttons : [{
											text : '确认',
											handler : function() {
												var sortingNum = $("#sortingNuma").val();
												var manNum=$("#manNuma").val();
												var wrapNum=$("#wrapNuma").val();
												var packNum=$("#packNuma").val();
												var nbqNum=$("#nbqNuma").val();
												var wbqNum=$("#wbqNuma").val();
												var mtNum=$("#mtNuma").val();
												var ztjNum=$("#ztjNuma").val();
												var ctjNum=$("#ctjNuma").val();
												var drobackdate=$("#drobackdate").val();
												var date=$("#date").val();
												var feeId =$('#feeId').combobox("getValue");
												if (manNum == ""|| wrapNum == ""|| packNum == ""|| sortingNum == ""||nbqNum==""||wbqNum==""||mtNum==""||ztjNum==""||ctjNum=="") {
													parent.$.messager.show({
																title : "提示",
																msg : "数量不可为空！",
																position : "bottomRight"
															});
													return;
												}
												if (manNum == 0 && wrapNum == 0&& packNum == 0&& sortingNum == 0&&nbqNum==0&&wbqNum==0&&mtNum==0&&ztjNum==0&&ctjNum==0) {
													parent.$.messager.show({
																title : "提示",
																msg : "数量不可全为0！",
																position : "bottomRight"
															});
													return;
												}
												if (feeId == ""|| $('#clientId').combobox("getValue") == "") {
													parent.$.messager
															.show({
																title : "提示",
																msg : "费用方案及装卸队不可为空！",
																position : "bottomRight"
															});
													return;
												}
												$.ajax({
															async : false,
															type : "GET",
															url : "${ctx}/cost/enterstevedoring/judgefee",
															data : {
																"sortingNum" : sortingNum,
																"manNum" : manNum,
																"wrapNum" : wrapNum,
																"packNum" : packNum,
																"feeId" : feeId,
																"nbqNum":nbqNum,
																"wbqNum":wbqNum,
															    "mtNum":mtNum,
															    "ztjNum":ztjNum,
															    "ctjNum":ctjNum
															},
															dataType : "text",
															success : function(msg) {
																if (msg != "success") {
																	parent.$.messager.confirm('提示',msg,function(data) {
																						if (data) {
																							goonadd();
																						}
																					})
																} else {
																	goonadd();
																}
															}
														});
											}
										}, {
											text : '取消',
											handler : function() {
												d.panel('close');
											}
										} ]
							});
		}

		function goonadd() {
			var clientName = $('#clientId').combobox("getText");
			$("#clientE").val(clientName);
			var feePlan = $('#feeId').combobox("getText");
			$("#feePlan").val(feePlan);
			$("#mainform").submit();
		}

		//删除
		function deleteIt() {
			if ($("#state").val() == "已完成") {
				parent.$.messager.show({
					title : "提示",
					msg : "已完成状态无法进行删除操作！",
					position : "bottomRight"
				});
				return;
			}
			var rows = dgg.datagrid('getSelections');
			var del = dgg.datagrid('getSelected');
			if (del == null) {
				parent.$.messager.show({
					title : "提示",
					msg : "请选择行数据！",
					position : "bottomRight"
				});
				return;
			}
			var ids = [];
			for (var i = 0; i < rows.length; i++) {
				ids.push(rows[i].ID);
			}
			parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data) {
				if (data) {
					$.ajax({
						type : 'get',
						url : "${ctx}/cost/backstevedoring/delete/" + ids,
						success : function(data) {
							dgg.datagrid('clearSelections');
							successTip(data, dgg);
						}
					});
				}
			});
		}

		//完成
		function finish() {
			var billNum = $("#billNum").val();
			var state = $("#state").val();
			if (billNum == "") {
				parent.$.messager.show({
					title : "提示",
					msg : "请先进行查询操作！",
					position : "bottomRight"
				});
				return;
			}
			if (state== "已完成") {
				parent.$.messager.show({
					title : "提示",
					msg : "已完成状态无法再次完成！",
					position : "bottomRight"
				});
				return;
			}
			billNum=billNum.replace(new RegExp("/","g" ),"笑脸");
			$.ajax({
				type : "get",
				url : "${ctx}/cost/backstevedoring/addstandbook/" + billNum,
				dataType : "text",
				success : function(msg) {
					if (msg == "success") {
						parent.$.messager.show({
							title : "提示",
							msg : "保存成功！",
							position : "bottomRight"
						});
						$("#state").val("已完成");
					}
				}
			});
		}

		//取消完成
		function concel() {
			var billNum = $("#billNum").val();
			if (billNum=="") {
				parent.$.messager.show({
					title : "提示",
					msg : "请先进行查询操作！",
					position : "bottomRight"
				});
				return;
			} else if ($("#state").val() == "" || $("#state").val() == "未完成") {
				parent.$.messager.show({
					title : "提示",
					msg : "未完成状态无法取消完成！",
					position : "bottomRight"
				});
				return;
			} else {
				billNum=billNum.replace(new RegExp("/","g" ),"笑脸");
				parent.$.messager.confirm('提示', '取消完成后将删除装卸队费用，确定要取消？',
						function(data) {
							if (data) {
								$.ajax({
									type : "get",
									url : "${ctx}/cost/backstevedoring/concel/"
											+ billNum,
									dataType : "text",
									success : function(msg) {
										if (msg == "success") {
											parent.$.messager.show({
												title : "提示",
												msg : "取消成功！",
												position : "bottomRight"
											});
											$("#state").val("");
										}
									}
								});
							}
						});
			}
		}
	</script>
</body>
</html>