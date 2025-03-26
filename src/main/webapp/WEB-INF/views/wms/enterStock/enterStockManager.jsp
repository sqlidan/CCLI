<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title></title>
	<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">

<div data-options="region:'center'">

	<form id="downForm" method="post"></form>

	<form id="mainForm" method="post">

		<div style="height: auto" class="datagrid-toolbar">
			<shiro:hasPermission name="wms:enterStockma:add">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   iconCls="icon-standard-disk" plain="true"
				   data-options="disabled:false" onclick="submitForm(1)">保存并新建</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:enterStockma:save">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   iconCls="icon-standard-disk" plain="true"
				   data-options="disabled:false" onclick="submitForm(0)">保存</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:enterStockma:delete">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   iconCls="icon-remove" plain="true" onclick="deleteIt()">删除</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:enterStockma:submit">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   iconCls="icon-standard-application-go" plain="true"
				   onclick="submitIt()">提交</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:enterStockma:noPass">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   iconCls="icon-standard-user-delete" plain="true"
				   onclick="noPass()">驳回</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:enterStockma:okPass">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   iconCls="icon-standard-user-edit" plain="true" onclick="okPass()">审核</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:enterStockma:backPass">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   iconCls="icon-standard-user-delete" plain="true"
				   onclick="backPass()">取消审核</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:enterStockma:adjust">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   iconCls="icon-hamburg-credit" plain="true"
				   onclick="inStockAdjust()">费用</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:enterStockma:kfadjust">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   iconCls="icon-hamburg-credit" plain="true"
				   onclick="inStockkfAdjust()">费用</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
		</div>
		<table class="formTable">
			<tr>
				<td>审核状态</td>
				<td><input id="auditingState" name="auditingState"
						   type="hidden" value="${bisEnterStock.auditingState}" /> <input
						id="auditingStateC" class="easyui-validatebox"
						data-options="width: 180" value="" readonly /></td>

				<td>联系单号</td>
				<td><input id="linkId" name="linkId"
						   class="easyui-validatebox" data-options="width: 180"
						   value="${bisEnterStock.linkId}" readonly /></td>

				<td>存货方</td>
				<td><input type="hidden" id="stockS" name="stockS"
						   value="${bisEnterStock.stockId}" /> <input type="hidden"
																	  id="stockIn" name="stockIn" /> <select id="stockId" name="stockId"
																											 class="easyui-combobox"
																											 data-options="width: 180,
                    required:'required'">
					<option value=""></option>
				</select></td>
				<td>费用方案</td>
				<td><input type="hidden" id="feePlan" name="feePlan" value=""></input>
					<input type="hidden" id="feeS" name="feeS"
						   value="${bisEnterStock.feeId}" /> <select class="easyui-combobox"
																	 id="feeId" name="feeId"
																	 data-options="width:180, required:'required'" value="">
					</select></td>
			</tr>
			<tr>
				<td>结算单位</td>
				<td><input type="hidden" id="stockOrgIdS" name="stockOrgIdS"
						   value="${bisEnterStock.stockOrgId}" /> <input type="hidden"
																		 id="stockOrg" name="stockOrg" /> <select id="stockOrgId"
																												  name="stockOrgId" class="easyui-combobox"
																												  data-options="width: 180, required:'required'">
				</select></td>
				<td>箱型尺寸</td>
				<td><select id="ctnTypeSize" name="ctnTypeSize"
							class="easyui-combobox"
							data-options="width:180,prompt:'箱型尺寸',value:'${bisEnterStock.ctnTypeSize}',valueField:'id',textField:'text', required:'required',data:[{id:'20',text:'20'},{id:'40',text:'40'},{id:'散货',text:'散货'}]" />

				</td>

				<td>是否查验</td>
				<td><span class="easyui-checkbox"> <input id="ifCheck"
														  name="ifCheck" type="checkbox" data-options="width: 180"
														  value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifCheck == 1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span></td>

				<td>是否审批</td>
				<td> <span class="easyui-checkbox"> <input
						id="ifRecord" name="ifRecord" type="checkbox"
						data-options="width: 180" value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifRecord == 1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span>
				</td>
			</tr>
			<tr>
				<td>生产日期</td>
				<td><input id="makeTimes" name="makeTimes"
						   class="easyui-my97" datefmt="yyyy-MM-dd"
						   data-options="width: 180"
						   value="<fmt:formatDate value="${bisEnterStock.makeTimes}" pattern="yyyy-MM-dd" />" />
				</td>
				<td>保质期截止时间</td>
				<td><input id="makeTimee" name="makeTimee"
						   class="easyui-my97" datefmt="yyyy-MM-dd"
						   data-options="width: 180"
						   value="<fmt:formatDate value="${bisEnterStock.makeTimee}" pattern="yyyy-MM-dd" />" />
				</td>
			</tr>
			<tr>
<%--				<td>是否保税</td>--%>
<%--				<td>--%>
<%--					<c:choose>--%>
<%--						<c:when test="${bisEnterStock.ifBonded == 1}">--%>
<%--								<input id="ifBonded" name="ifBonded" type="hidden" data-optons="width: 180" value="1"/>是--%>
<%--						</c:when>--%>
<%--						<c:otherwise>--%>
<%--							<span class="easyui-checkbox">--%>
<%--								<input--%>
<%--									id="ifBonded" name="ifBonded" type="checkbox"--%>
<%--									data-optons="width: 180" value="1" readonly--%>
<%--									checked/>--%>
<%--							</span>--%>
<%--						</c:otherwise>--%>
<%--					</c:choose>--%>
<%--				</td>--%>
				<td>是否保税</td>
				<td>
					<span class="easyui-checkbox">
						<input id="ifBonded" name="ifBonded" type="checkbox"
							   data-optons="width: 180" value="1" readonly
							<c:choose>
								<c:when test="${bisEnterStock.ifBonded == 1}">checked</c:when>
								<c:otherwise></c:otherwise>
							</c:choose>
						/>
					</span>
				</td>
				<td>核注清单号</td>
				<td><input id="checkListNo" name="checkListNo"
						   class="easyui-validatebox" data-options="width: 180,prompt:'请输入保税核注清单号'"
						   value="${checkListNo}"/>
				</td>
				<td>账册号</td>
				<td><input id="emsNo" name="emsNo"
						   class="easyui-validatebox" data-options="width: 180"
						   value="${emsNo}" readonly/>
				</td>
			</tr>
			<tr>
				<td>入库提单号</td>
				<td><input id="itemNum" name="itemNum"
						   class="easyui-validatebox"
						   data-options="width: 180, required:'required',prompt:'提单号请勿包含逗号'"
						   value="${bisEnterStock.itemNum}" /></td>
				<td>报关单号：</td>
				<td><input id="bgdh" name="bgdh" type="text"
						   class="easyui-validatebox" data-options="width: 180"
						   value="${bisEnterStock.bgdh}" /></td>


				<td>原产国：</td>
				<td><input id="ycg" name="ycg" type="text"
						   class="easyui-validatebox" data-options="width: 180"
						   value="${bisEnterStock.ycg}" /></td>

				<td>报关审报日期</td>
				<td><input id="bgdhDate" name="bgdhDate" class="easyui-my97"
						   datefmt="yyyy-MM-dd" data-options="width: 180"
						   value="<fmt:formatDate value="${bisEnterStock.bgdhDate}" pattern="yyyy-MM-dd" />" />
				</td>
			</tr>
			<tr>
				<td>是否分拣</td>
				<td><span class="easyui-checkbox"> <input
						id="ifSorting" name="ifSorting" type="checkbox"
						data-options="width: 180" value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifSorting == 1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span></td>
				<td>分拣要求1</td>
				<td><select id="sortingAsk1" name="sortingAsk1"
							class="easyui-combobox" data-options="width: 180">
				</select></td>
				<td>特殊分拣</td>
				<td><input id="sortingSpecialAsk" name="sortingSpecialAsk"
						   class="easyui-validatebox" data-options="width: 180"
						   value="${bisEnterStock.sortingSpecialAsk}" maxlength="250" /></td>
				<td>特殊要求</td>
				<td><input id="sortingSpecial" name="sortingSpecial"
						   class="easyui-validatebox" data-options="width: 180"
						   value="${bisEnterStock.sortingSpecial}" maxlength="250" /></td>
			</tr>
			<tr>
				<td>是否派车</td>
				<td><span class="easyui-checkbox"> <input
						id="ifUseTruck" name="ifUseTruck" type="checkbox"
						data-options="width: 180" value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifUseTruck == 1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span></td>

				<td>是否缠膜</td>
				<td><span class="easyui-checkbox"> <input id="ifWrap"
														  name="ifWrap" type="checkbox" data-options="width: 180" value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifWrap == 1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span></td>
				<td>是否套袋</td>
				<td><span class="easyui-checkbox"> <input
						id="ifBagging" name="ifBagging" type="checkbox"
						data-options="width: 180" value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifBagging == 1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span></td>

				<td>是否带木托盘</td>
				<td><span class="easyui-checkbox"> <input
						id="ifWithWooden" name="ifWithWooden" type="checkbox"
						data-options="width: 180" value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifWithWooden == 1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span></td>
			</tr>
			<tr>
				<%-- 					<td>客户自行清关</td>
                                    <td><span class="easyui-checkbox"> <input
                                            id="ifSelfCustomsClearance" name="ifSelfCustomsClearance"
                                            type="checkbox" data-options="width: 180" value="1"
                                            <c:choose>
                                               <c:when test="${bisEnterStock.ifSelfCustomsClearance == 1}">checked</c:when>
                                            <c:otherwise></c:otherwise>
                                        </c:choose> />
                                    </span></td> --%>

				<td>是否MSC认证</td>
				<td><span class="easyui-checkbox"> <input
						id="ifMacAdmit" name="ifMacAdmit" type="checkbox"
						data-options="width: 180" value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifMacAdmit == 1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span></td>
				<td>是否报分库</td>
				<td><span class="easyui-checkbox"> <input
						id="ifChildWarehouse" name="ifChildWarehouse" type="checkbox"
						data-options="width:180" value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifChildWarehouse == 1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span></td>
				<td>是否称重</td>
				<td><span class="easyui-checkbox"> <input id="ifWeigh"
														  name="ifWeigh" type="checkbox" data-options="width:180" value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifWeigh == 1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span></td>
			</tr>

			<tr>
				<td>是否报关</td>
				<td><span class="easyui-checkbox"> <input
						id="ifToCustoms" name="ifToCustoms" type="checkbox"
						data-options="width: 180" value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifToCustoms == 1}">checked</c:when>
							<c:otherwise> </c:otherwise>
						</c:choose> />
					</span></td>
				<td>报关代报公司</td>
				<td><input type="hidden" id="customsCompany"
						   name="customsCompany" /> <select id="customsCompanyId"
															name="customsCompanyId" class="easyui-combobox"
															data-options="width: 180">
				</select></td>
				<td>是否报检</td>
				<td><span class="easyui-checkbox"> <input id="ifToCiq"
														  name="ifToCiq" type="checkbox" data-options="width: 180"
														  value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifToCiq == 1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span></td>
				<td>报检代报公司</td>
				<td><input type="hidden" id="ciqCompany" name="ciqCompany" />
					<select id="ciqCompanyId" name="ciqCompanyId"
							class="easyui-combobox" data-options="width: 180">
					</select></td>
			</tr>
			<tr>
				<td>是否打印标签</td>
				<td><span class="easyui-checkbox"> <input
						id="ifPrintLable" name="ifPrintLable" type="checkbox"
						data-options="width: 180" value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifPrintLable == 1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span></td>
				<td>打印标签数量</td>
				<td><input id="printLableAmount" name="printLableAmount"
						   class="easyui-validatebox" data-options="width: 180"
						   value="${bisEnterStock.printLableAmount}"
						   onkeyup="ischeckNum(this)" /></td>

				<td>是否贴内标签</td>
				<td><span class="easyui-checkbox"> <input
						id="ifPrintNLable" name="ifPrintNLable" type="checkbox"
						data-options="width: 180" value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifPrintNLable == 1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span></td>
				<td>贴内标签数量</td>
				<td><input id="printNLableAmount" name="printNLableAmount"
						   class="easyui-validatebox" data-options="width: 180"
						   value="${bisEnterStock.printNLableAmount}"
						   onkeyup="ischeckNum(this)" /></td>
			</tr>

			<tr>
				<td>是否贴外标签</td>
				<td><span class="easyui-checkbox"> <input
						id="ifPrintWLable" name="ifPrintWLable" type="checkbox"
						data-options="width: 180" value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifPrintWLable == 1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span></td>
				<td>贴外标签数量</td>
				<td><input id="printWLableAmount" name="printWLableAmount"
						   class="easyui-validatebox" data-options="width: 180"
						   value="${bisEnterStock.printWLableAmount}"
						   onkeyup="ischeckNum(this)" /></td>
				<td>是否倒箱</td>
				<td><span class="easyui-checkbox"> <input id="ifBack"
														  name="ifBack" type="checkbox" data-options="width: 180" value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifBack==1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span></td>
				<td>倒箱日期</td>
				<td><input id="backDate" name="backDate" class="easyui-my97"
						   datefmt="yyyy-MM-dd" data-options="width: 180"
						   value="<fmt:formatDate value="${bisEnterStock.backDate}" pattern="yyyy-MM-dd" />" />
				</td>
			</tr>

			<tr>
				<td>是否装铁架</td>
				<td><span class="easyui-checkbox"> <input id="ifOutFit"
														  name="ifOutFit" type="checkbox" data-options="width: 180"
														  value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifOutFit == 1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span></td>
				<td>装铁架数量</td>
				<td><input id="outFitAmount" name="outFitAmount"
						   class="easyui-validatebox" data-options="width: 180"
						   value="${bisEnterStock.outFitAmount}" onkeyup="ischeckNum(this)" />
				</td>

				<td>是否拆铁架</td>
				<td><span class="easyui-checkbox"> <input
						id="ifTakeFit" name="ifTakeFit" type="checkbox"
						data-options="width: 180" value="1"
						<c:choose>
							<c:when test="${bisEnterStock.ifTakeFit == 1}">checked</c:when>
							<c:otherwise></c:otherwise>
						</c:choose> />
					</span></td>
				<td>拆铁架数量</td>
				<td><input id="takeFitAmount" name="takeFitAmount"
						   class="easyui-validatebox" data-options="width: 180"
						   value="${bisEnterStock.takeFitAmount}" onkeyup="ischeckNum(this)" />
				</td>
			</tr>

			<tr>


				<td>入库仓库</td>
				<td><input type="hidden" id="warehouse" name="warehouse" /> <select
						id="warehouseId" name="warehouseId" class="easyui-combobox"
						data-options="width: 180, required:'required'">
				</select></td>
				<td>存储温度</td>
				<td><select id="temperature" name="temperature"
							class="easyui-combobox"
							data-options="width: 180, required:'required'">
				</select></td>
				<%-- 				<td>账册商品序号</td>
                                <td>
                                    <input id="accountBook" name="accountBook" class="easyui-validatebox" data-options="width: 180"
                                           value="${bisEnterStock.accountBook}"/>
                                </td>  --%>
			</tr>

			<tr>
				<td>物流容器</td>
				<td><input type="hidden" id="receptacleS"
						   value="${bisEnterStock.receptacle}" /> <select id="receptacle"
																		  name="receptacle" class="easyui-combobox"
																		  data-options="width: 180, required:'required'">
				</select></td>
				<td>入库号</td>
				<td><input id="rkNum" name="rkNum" class="easyui-validatebox"
						   data-options="width: 180" value="${bisEnterStock.rkNum}" /></td>
				<td>计划入库日期</td>
				<td><input type="hidden" id="rkTime" name="rkTime"
						   value="${bisEnterStock.rkTime}" /> <input id="etaWarehouse"
																	 name="etaWarehouse" class="easyui-my97" datefmt="yyyy-MM-dd"
																	 data-options="width: 180, required:'required'"
																	 value="<fmt:formatDate value="${bisEnterStock.etaWarehouse}" pattern="yyyy-MM-dd" />" />
				</td>
				<td>计划出库日期</td>
				<td><input id="etdWarehouse" name="etdWarehouse"
						   class="easyui-my97" datefmt="yyyy-MM-dd" data-options="width: 180"
						   value="<fmt:formatDate value="${bisEnterStock.etdWarehouse}" pattern="yyyy-MM-dd" />" />
				</td>
			</tr>
			<tr>
				<td>ETA</td>
				<td><input id="etaShip" name="etaShip" class="easyui-my97"
						   data-options="width: 180"
						   value="<fmt:formatDate  value="${bisEnterStock.etaShip}"  pattern="yyyy-MM-dd" />" />
				</td>
				<td>明细箱数合计：</td>
				<td><input id="pieceInfo" class="easyui-validatebox"
						   data-options="width: 180" readonly style="background: #eee" /></td>
				<td>明细净重合计：</td>
				<td><input id="netInfo" class="easyui-validatebox"
						   data-options="width: 180" readonly style="background: #eee" /></td>
				<td>明细毛重合计：</td>
				<td><input id="grossInfo" class="easyui-validatebox"
						   data-options="width: 180" readonly style="background: #eee" /></td>
			</tr>
			<tr>
				<td>备注</td>
				<td colspan='3'><input id="remark" name="remark"
									   class="easyui-validatebox" data-options="width: 440"
									   value="${bisEnterStock.remark}" maxlength="500" /></td>
			</tr>

			<tr>
				<td><input type="hidden" id="operator" name="operator"
						   class="easyui-validatebox" data-options="width: 180"
						   value="${bisEnterStock.operator}" readonly
						   style="background: #eee" /></td>
				<td><input type="hidden" id="operateTime" name="operateTime"
						   data-options="width: 180"
						   value="<fmt:formatDate  value="${bisOutStock.operateTime}"  pattern="yyyy-MM-dd HH:mm:ss" />"
						   readonly style="background: #eee" /></td>
				<td><input type="hidden" id="updateTime" name="updateTime"
						   data-options="width: 180"
						   value="<fmt:formatDate  value="${bisOutStock.updateTime}"  pattern="yyyy-MM-dd HH:mm:ss" />"
						   readonly style="background: #eee" /></td>
			</tr>
		</table>
	</form>
</div>
<div data-options="region:'south',split:true,border:false" title="明细"
	 style="height: 400px">
	<div id="tb" style="padding: 5px; height: auto"
		 class="datagrid-toolbar">
		<div>
			<shiro:hasPermission name="wms:enterStockInfo:add">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   iconCls="icon-add" plain="true" onclick="addInfo()">添加</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:enterStockInfo:update">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   iconCls="icon-edit" plain="true" onclick="editInfo()">修改</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:enterStockInfo:delete">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:enterStockInfo:down">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   iconCls="icon-standard-basket-put" plain="true"
				   data-options="disabled:false" onclick="down()">下载导入模板</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:enterStockInfo:import">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   iconCls="icon-standard-basket-put" plain="true"
				   data-options="disabled:false" onclick="into()">EXCEL导入</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<shiro:hasPermission name="wms:enterStockInfo:copy">
				<a href="javascript:void(0)" class="easyui-linkbutton"
				   iconCls="icon-hamburg-old-versions" plain="true"
				   onclick="copyInfo()">复制</a>
				<span class="toolbar-item dialog-tool-separator"></span>
			</shiro:hasPermission>
			<a href="javascript:void(0)" class="easyui-linkbutton"
			   iconCls="icon-hamburg-old-versions" plain="true"
			   onclick="mani()">核放单申请</a>
			<span class="toolbar-item dialog-tool-separator"></span>
		</div>
	</div>
	<table id="dg"></table>
	<div id="dlg"></div>
	<div id="skudlg"></div>
</div>

<script type="text/javascript">
	var dg;
	var d;
	var dgCost;
	var dgA;
	var dsku;
	var action = "${action}";
	var checkListNo = "${checkListNo}";
	var emsNo = "${emsNo}";
	var cha = 0;
	$(function () {
		//获得新联系单号
		if ($("#linkId").val() == "") {
			//生成新的联系单号
			$.ajax({
				async: false,
				type: "POST",
				url: "${ctx}/wms/enterStock/getNewLinkId",
				data: "",
				dataType: "text",
				success: function (date) {
					$("#linkId").val(date);
					var linkId = $("#linkId").val();
				}
			});
		}
		window.setTimeout(function () {
			jiaoyan();
		}, 200);
		if (action == "update") {
			var newVal = $("#stockOrgIdS").val();
			feeSelect2(newVal, $("#feeS").val());
		}
		gridDG();
		selectAJAX();
		window.setTimeout(function () {
			cha = 0
		}, 1000);
	});


	getState();
	function jiaoyan() {
		$('#itemNum').validatebox({
			required: true,
			validType: {
				length: [1, 150],
				remote: ["${ctx}/wms/enterStock/checkItemNum/" + $("#linkId").val(), "itemNum"]
			}
		});
	}

	//判断当前入库联系单状态
	function getState() {
		var stateN = $("#auditingState").val();
		if (stateN == 2) {
			$("#auditingStateC").val("已审核");
			$("#itemNum").attr("readonly", "readonly");
			$("#itemNum").css('background', '#eee');
		}
		else if (stateN == 1) {
			$("#auditingStateC").val("待审核");
			$("#itemNum").attr("readonly", "readonly");
			$("#itemNum").css('background', '#eee');
		}
		else {
			$("#auditingStateC").val("暂存");
		}
		if (action == "create") {
			$("#auditingStateC").val("未保存");
		}
	}

	//清空
	function clearIt() {
		window.parent.mainpage.mainTabs.addModule('入库联系单管理', 'wms/enterStock/add');
	}

	//查询入库联系单明细
	function gridDG() {
		var linkId = $("#linkId").val();
		if (linkId == "") {
			linkId = 0;
		}
		dg = $('#dg').datagrid({
			method: "GET",
			url: '${ctx}/wms/enterStockInfo/json/' + linkId,
			fit: true,
			fitColumns: false,
			border: false,
			striped: true,
			pagination: true,
			rownumbers: true,
			pageNumber: 1,
			pageSize: 1000,
			pageList: [1000, 2000],
			singleSelect: false,
			columns: [[
				{field: 'id', title: 'id', hidden: true},
				{field: 'itemNum', title: '提单号', sortable: true, width: 150},
				{field: 'accountBook', title: '账册商品序号', sortable: true, width: 150},
				{field: 'ctnNum', title: '箱号', sortable: true, width: 150},
				{field: 'hsCode', title: 'HS编码', sortable: true, width: 150},
				{field: 'hsItemname', title: '海关品名', sortable: true, width: 200},
				{field: 'cargoName', title: '品名', sortable: true, width: 200},
				{field: 'typeSize', title: '规格', sortable: true, width: 60},
				{field: 'piece', title: '箱数', sortable: true},
				{field: 'netWeight', title: '总净重', sortable: true, width: 100},
				{field: 'grossWeight', title: '总毛重', sortable: true, width: 100},
				{field: 'carNum', title: '车牌号', sortable: true, width: 150},
				{field: 'bgdh', title: '报关单号', sortable: true, width: 150},
				{field: 'ycg', title: '原产国', sortable: true, width: 150},
				/* {field: 'feeCode', title: '派车费目', sortable: true, width: 80,
                    formatter: function (value, row, index) {
                        if(value=="tc")
                          return '短倒费';
                        else if(value=="tcwq")
                          return '短倒费（五期）';
                        else if(value=="tcsq")
                          return '短倒费（三期）';
                        else if(value=="tssiqi")
                            return '短倒费（四期）';
                    }
                }, */
				{field: 'ifCheck', title: '是否查验', sortable: true, width: 80,
					formatter: function (value, row, index) {
						if(value=="1")
							return '是';
						else
							return '否';
					}
				},
				/* {field: 'ifWrap', title: '是否缠膜', sortable: true, width: 80,
                    formatter: function (value, row, index) {
                        if(value=="1")
                          return '是';
                        else
                          return '否';
                    }
                },
                {field: 'ifBagging', title: '是否套袋', sortable: true, width: 80,
                    formatter: function (value, row, index) {
                        if(value=="1")
                          return '是';
                        else
                          return '否';
                    }
                }, */
				{field: 'ifBack', title: '是否倒箱', sortable: true, width: 80,
					formatter: function (value, row, index) {
						if(value=="1")
							return '是';
						else
							return '否';
					}
				},
				{field: 'dctnNum', title: '倒车箱号', sortable: true, width: 150},
				{field: 'dcarNum', title: '倒车车牌号', sortable: true, width: 150},
				{field: 'sku', title: 'sku', sortable: true, width: 150},
				{field: 'netSingle', title: '单净', sortable: true, width: 100,
					formatter:function(val,rowData,rowIndex){
						if(val!=null)
							return val.toFixed(4);
					}
				},
				{field: 'grossSingle', title: '单毛', sortable: true, width: 100,
					formatter:function(val,rowData,rowIndex){
						if(val!=null)
							return val.toFixed(4);
					}
				},
				{
					field: 'units', title: '重量单位', sortable: true,
					formatter: function (value, row, index) {
						return '千克';
					}
				},
				{field: 'orderNum', title: 'order号', sortable: true, width: 150},
				{field: 'mscNum', title: 'msc', sortable: true, width: 150},
				{field: 'projectNum', title: '项目号', sortable: true, width: 150},
				{field: 'rkNum', title: '入库号', sortable: true, width: 150},
				{field: 'lotNum', title: 'lot', sortable: true, width: 150},
				{field: 'shipNum', title: '捕捞船名', sortable: true, width: 150},
				{field: 'operator', title: '创建人', sortable: true, width: 180},
				{field: 'operateTime', title: '创建时间', sortable: true, width: 180},
				{field: 'makeTime', title: '生产时间', sortable: true, width: 180}
			]],
			onLoadSuccess: function () {
				insertSum();
			},
			enableHeaderClickMenu: true,
			enableHeaderContextMenu: true,
			enableRowContextMenu: false,
			toolbar: '#tb2'
		});

	}

	function insertSum() {
		var rows = $('#dg').datagrid('getRows');
		var pieces = 0;
		var net = 0;
		var gross = 0;
		for (var i = 0; i < rows.length; i++) {
			pieces += Number(rows[i]['piece']);
			net += Number(rows[i]['netWeight']);
			gross += Number(rows[i]['grossWeight']);
		}
		$("#pieceInfo").val(pieces);
		$("#netInfo").val(net.toFixed(2));
		$("#grossInfo").val(gross.toFixed(2));
	}

	//保存
	function submitForm(copys) {
		if($("#ifPrintLable").is(":checked")){
			if($("#printLableAmount").val()==0||$("#printLableAmount").val()==""||$("#printLableAmount").val()==null){
				parent.$.easyui.messager.show({
					title: "操作提示",
					msg: "请输入打印标签数量！",
					position: "bottomRight"
				});
				return;
			}
		}
		if($("#ifBonded").is(":checked")) {
			console.log("makeTimes", $("#makeTimes").datebox("getValue"))
			console.log("makeTimee", $("#makeTimee").datebox("getValue"))

			var makeTimes = $("#makeTimes").datebox("getValue");
			if (makeTimes == "" || makeTimes == null) {
				parent.$.easyui.messager.show({
					title: "操作提示",
					msg: "请选择生产日期！",
					position: "bottomRight"
				});
				return;
			}
			var makeTimee = $("#makeTimee").datebox("getValue");
			if (makeTimee == "" || makeTimee == null) {
				parent.$.easyui.messager.show({
					title: "操作提示",
					msg: "请选择保质期截止时间！",
					position: "bottomRight"
				});
				return;
			}
		}
		/*     	if($("#ifBonded").is(":checked")){
                    if($("#accountBook").val()==0||$("#accountBook").val()==""||$("#accountBook").val()==null){
                        parent.$.easyui.messager.show({
                            title: "操作提示",
                            msg: "请输入账册商品序号！",
                            position: "bottomRight"
                        });
                        return;
                    }
            } */
		if($("#ifPrintNLable").is(":checked")){
			if($("#printNLableAmount").val()==0||$("#printNLableAmount").val()==""||$("#printNLableAmount").val()==null){
				parent.$.easyui.messager.show({
					title: "操作提示",
					msg: "请输入贴内标签数量！",
					position: "bottomRight"
				});
				return;
			}
		}
		if($("#ifPrintWLable").is(":checked")){
			if($("#printWLableAmount").val()==0||$("#printWLableAmount").val()==""||$("#printWLableAmount").val()==null){
				parent.$.easyui.messager.show({
					title: "操作提示",
					msg: "请输入贴外标签数量！",
					position: "bottomRight"
				});
				return;
			}
		}
		if($("#ifOutFit").is(":checked")){
			if($("#outFitAmount").val()==0||$("#outFitAmount").val()==""||$("#outFitAmount").val()==null){
				parent.$.easyui.messager.show({
					title: "操作提示",
					msg: "请输入装铁架数量！",
					position: "bottomRight"
				});
				return;
			}
		}
		if($("#ifTakeFit").is(":checked")){
			if($("#takeFitAmount").val()==0||$("#takeFitAmount").val()==""||$("#takeFitAmount").val()==null){
				parent.$.easyui.messager.show({
					title: "操作提示",
					msg: "请输入拆铁架数量！",
					position: "bottomRight"
				});
				return;
			}
		}
		if ($("#auditingStateC").val() == "已审核") {
			parent.$.easyui.messager.show({
				title: "操作提示",
				msg: "已审核状态下无法保存！",
				position: "bottomRight"
			});
			return;
		}
		if ($("#auditingStateC").val() == "未保存") {
			var state = 0;
		} else {
			var state = 1;
		}
		if ('${bisEnterStock.backDate}' != "" && ( $('#backDate').datebox('getValue') + " 00:00:00.0") != '${bisEnterStock.backDate}') {
			changecha('7');
		}
		if ($("#auditingStateC").val() == "待审核" || $("#auditingStateC").val() == "已审核") {
			if (cha != "0") {
				parent.$.easyui.messager.show({
					title: "操作提示",
					msg: "已提交或已审核状态下，客户、费用方案、仓库及倒箱日期不允许修改！",
					position: "bottomRight"
				});
				return;
			}
		}

		$("#feePlan").val($('#feeId').combobox("getText"));
		var linkId = $("#linkId").val();
		$("input[type='checkbox']").not("input:checked").val("0");
		$(":checkbox").attr("checked", true);
		$("#stockIn").val($('#stockId').combobox("getText"));
		$("#stockOrg").val($('#stockOrgId').combobox("getText"));
		$("#customsCompany").val($('#customsCompanyId').combobox("getText"));
		$("#ciqCompany").val($('#ciqCompanyId').combobox("getText"));
		$("#warehouse").val($('#warehouseId').combobox("getText"));
		if ($("#mainForm").form('validate')) {
			var backdatet = $('#backDate').datebox('getValue');
			if ( (backdatet == "" || backdatet == null) && $("#ifBack").val()!='0') {
				parent.$.messager.confirm('提示', '倒箱日期为空，您确认不保存此项吗？', function (data) {
					if (data) {
						//用ajax提交form
						$.ajax({
							async: false,
							type: 'POST',
							url: "${ctx}/wms/enterStock/createEnterStock/" + state,
							data: $('#mainForm').serialize(),
							dataType: "text",
							success: function (msg) {
								if (msg == "success") {
									$(":checkbox[value='0']").attr("checked", false);
									$("input[type='checkbox']").not("input:checked").val("1");
									parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight"});
									cha = 0;
									if ($("#auditingStateC").val() == "未保存") {
										$("#auditingState").val("0");
										$("#auditingStateC").val("暂存");
									}
									if (copys == 1) {
										saveAndAdd();
									}
								}else{
									$.messager.confirm('保存失败:'+msg);
								}
							}
						});
					}
				});
			} else {
				//用ajax提交form
				$.ajax({
					async: false,
					type: 'POST',
					url: "${ctx}/wms/enterStock/createEnterStock/" + state,
					data: $('#mainForm').serialize(),
					dataType: "text",
					success: function (msg) {
						if (msg == "success") {
							$(":checkbox[value='0']").attr("checked", false);
							$("input[type='checkbox']").not("input:checked").val("1");
							parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight"});
							cha = 0;
							if ($("#auditingStateC").val() == "未保存") {
								$("#auditingState").val("0");
								$("#auditingStateC").val("暂存");
							}
						}else{
							$.messager.confirm('保存失败:'+msg);
						}
					}
				});
			}
		} else {
			$(":checkbox[value='0']").attr("checked", false);
			$("input[type='checkbox']").not("input:checked").val("1");
		}
	}

	//删除此入库联系单
	function deleteIt() {
		if ($("#auditingStateC").val() == "待审核") {
			parent.$.easyui.messager.show({title: "操作提示", msg: "此数据已提交，无法删除！", position: "bottomRight"});
			return;
		} else if ($("#auditingStateC").val() == "已审核") {
			parent.$.easyui.messager.show({title: "操作提示", msg: "此数据已审核，无法删除！", position: "bottomRight"});
			return;
		} else if ($("#auditingStateC").val() == "未保存") {
			parent.$.easyui.messager.show({title: "操作提示", msg: "此数据未保存，无法删除！", position: "bottomRight"});
			return;
		} else {
			var linkId = $("#linkId").val();
			parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
				if (data) {
					$.ajax({
						type: 'get',
						url: "${ctx}/wms/enterStock/deleteEnterStock/" + linkId,
						success: function (data) {
							parent.$.easyui.messager.show({title: "操作提示", msg: "删除成功！", position: "bottomRight"});
							window.parent.mainpage.mainTabs.closeCurrentTab();//关闭TAB
						}
					});
				}
			})
		}
	}

	//提交入库联系单
	function submitIt() {
		if ($("#auditingStateC").val() != "暂存") {
			parent.$.easyui.messager.show({title: "操作提示", msg: "此联系单不是暂存状态，无法提交！", position: "bottomRight"});
			return;
		}
		var linkId = $('#linkId').val();
		parent.$.messager.confirm('提示', '您确定要提交此入库联系单？', function (data) {
			if (data) {
				$.ajax({
					type: 'get',
					url: "${ctx}/wms/enterStock/submitEnterStock/" + linkId,
					success: function (data) {
						if (data == "success") {
							$("#auditingStateC").val("待审核");
							$("#auditingState").val("1");
							$("#itemNum").attr("readonly", "readonly");
							$("#itemNum").css('background', '#eee');
						}
					}
				});
			}
		});
	}

	//审核此入库联系单
	function okPass() {
		if ($("#auditingStateC").val() != "待审核") {
			parent.$.easyui.messager.show({title: "操作提示", msg: "此联系单不是待审核状态，无法审核！", position: "bottomRight"});
			return;
		}
		var linkId = $('#linkId').val();
		$.ajax({
			type: 'get',
			url: "${ctx}/wms/enterStock/checkPassEnterStock/" + linkId,
			success: function (data) {
				if(data == "success"){
					$.ajax({
						type: 'get',
						url: "${ctx}/wms/enterStock/passEnterStock/" + linkId,
						success: function (data) {
							successTip(data, dg);
							$("#auditingStateC").val("已审核");
							$("#auditingState").val("2");
							parent.$.messager.show({title: "提示", msg: "审核成功！", position: "bottomRight"});
						}
					});
				}else{
					//提示是否修改
					parent.$.messager.confirm('提示', '审核后将按照新ASN计费客户结算费用,确认要继续？', function (data) {
						if(data){
							$.ajax({
								type: 'get',
								url: "${ctx}/wms/enterStock/passEnterStockAndUpdateASNActions/" + linkId,
								success: function (data) {
									successTip(data, dg);
									$("#auditingStateC").val("已审核");
									$("#auditingState").val("2");
									parent.$.messager.show({title: "提示", msg: "审核成功！", position: "bottomRight"});
								}
							});

						}else{
							$("#auditingStateC").val("已审核");
							$("#auditingState").val("2");
							parent.$.messager.show({title: "提示", msg: "未修改计费客户审核成功！", position: "bottomRight"});

						}
					});
				}
			}
		});
	}

	//取消审核此入库联系单
	function backPass() {
		if ($("#auditingStateC").val() != "已审核") {
			parent.$.easyui.messager.show({title: "操作提示", msg: "此联系单未审核，无法取消审核！", position: "bottomRight"});
			return;
		}
		var linkId = $('#linkId').val();
		parent.$.messager.confirm('提示', '您确定取消此入库联系单的审核？', function (data) {
			if (data) {
				$.ajax({
					type: 'get',
					url: "${ctx}/wms/enterStock/backPassEnterStock/" + linkId,
					success: function (data) {
						if (data == "success") {
							$("#auditingStateC").val("待审核");
							$("#auditingState").val("1");
						} else {
							$("#auditingStateC").val("待审核");
							$("#auditingState").val("1");
						}
					}
				});
			}
		});
	}

	//驳回此入库联系单
	function noPass() {
		if ($("#auditingStateC").val() != "待审核") {
			parent.$.easyui.messager.show({title: "操作提示", msg: "此联系单未提交，无法驳回！", position: "bottomRight"});
			return;
		}
		var linkId = $('#linkId').val();
		parent.$.messager.confirm('提示', '您确定驳回此入库联系单的提交？', function (data) {
			if (data) {
				$.ajax({
					type: 'get',
					url: "${ctx}/wms/enterStock/noPassEnterStock/" + linkId,
					success: function (data) {
						if (data == "success") {
							$("#auditingStateC").val("暂存");
							$("#auditingState").val("0");
							$("#itemNum").removeAttr("readonly");
							$("#itemNum").css('background', '');
						}
					}
				});
			}
		});
	}

	//增加明细时，先判断是否已保存
	function addInfo() {
		if ($("#auditingStateC").val() == "已审核") {
			parent.$.messager.show({title: "提示", msg: "已审核数据无法操作明细！", position: "bottomRight"});
			return;
		}
		var getLinkId = $("#linkId").val();
		$.ajax({
			type: 'get',
			url: "${ctx}/wms/enterStock/ifsave/" + getLinkId,
			dataType: "text",
			success: function (data) {
				if (data != "success") {
					parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存入库联系单！", position: "bottomRight"});
					return;
				} else {
					addInfoT();
				}
			}
		});
	}

	//入库明细弹窗增加
	function addInfoT() {
		var linkId = $('#linkId').val();
		d = $("#dlg").dialog({
			title: '新增入库明细',
			width: 380,
			height: 380,
			href: '${ctx}/wms/enterStockInfo/create/' + linkId,
			maximizable: true,
			modal: true,
			buttons: [{
				text: '确认',
				handler: function () {
					$("#bigTypeName").val($("#bigType").combobox("getText"));
					$("#littleTypeName").val($("#littleType").combobox("getText"));
					if ($("#mainform2").form('validate')) {
						$("#mainform2").submit();
						d.panel('close');
					}
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
				}, 500);
			}
		});

	}

	//修改入库联系单明细
	function editInfo() {
		if ($("#auditingStateC").val() == "已审核") {
			parent.$.messager.show({title: "提示", msg: "已审核数据无法操作明细！", position: "bottomRight"});
			return;
		}
		var row = dg.datagrid('getSelected');
		if (rowIsNull(row)) return;
		d = $("#dlg").dialog({
			title: '修改入库明细',
			width: 380,
			height: 380,
			href: '${ctx}/wms/enterStockInfo/update/' + row.id,
			maximizable: true,
			modal: true,
			buttons: [{
				text: '确认',
				handler: function () {
					$("#bigTypeName").val($("#bigType").combobox("getText"));
					$("#littleTypeName").val($("#littleType").combobox("getText"));
					if ($("#mainform2").form('validate')) {
						$("#mainform2").submit();
						d.panel('close');
					}
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

	//复制弹框
	function copyInfo() {
		if ($("#auditingStateC").val() == "已审核") {
			parent.$.messager.show({title: "提示", msg: "已审核数据无法操作明细！", position: "bottomRight"});
			return;
		}
		var row = dg.datagrid('getSelected');
		if (rowIsNull(row)) return;
		d = $("#dlg").dialog({
			title: '新增入库明细',
			width: 380,
			height: 340,
			href: '${ctx}/wms/enterStockInfo/copy/' + row.id,
			maximizable: true,
			modal: true,
			buttons: [{
				text: '增加',
				handler: function () {
					if ($("#mainform2").form('validate')) {
						$('#mainform2').submit();
						d.panel('close');
					}
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

	//删除入库联系单明细
	function delInfo() {
		if ($("#auditingStateC").val() == "已审核") {
			parent.$.messager.show({title: "提示", msg: "已审核数据无法操作明细！", position: "bottomRight"});
			return;
		}
		var rows = dg.datagrid('getSelections');
		var del = dg.datagrid('getSelected');
		if (del == null) {
			parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight"});
			return;
		}
		var ids = [];
		for (var i = 0; i < rows.length; i++) {
			ids.push(rows[i].id);
		}
		parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function (data) {
			if (data) {
				$.ajax({
					type: 'get',
					url: "${ctx}/wms/enterStockInfo/deleteEnterStockInfo/" + ids,
					success: function (data) {
						dg.datagrid('clearSelections');
						successTip(data, dg);
					}
				});
			}
		});
	}

	// 用于 费用方案 计数判断
	var orgFlag = 0;

	//下拉框
	function selectAJAX() {
		var tFeeId = $("#feeS").val();
		var tStockId = $("#stockS").val();
		var treceptacle = $("#receptacleS").val();
		var orgId = $("#stockOrgIdS").val();

		//结算单位
		var url="${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${bisEnterStock.stockOrgId}";
		var getorg = '${bisEnterStock.stockOrgId}';
		// url=encode(url)
		//url=encodeURI(url);
		//url=encodeURI(url);
		// 结算单位
		$('#stockOrgId').combobox({
			method: "GET",
			url: url,
			valueField: 'ids',
			textField: 'clientName',
			mode: 'remote',
			onChange: function (newVal, oldVal) {
				if (newVal != "" && newVal != "undefined") {
					$('#feeId').combobox("clear");
					// -------------------------------------------------
					// 2016-9-14 因第一次加载时,                        -
					// 会触发对 费用方案select控件(feeId) 的修改操作, -
					// 因此通过计数阻止第一次加载时的修改。                 -
					if (orgFlag != 0) {
						$('#feeS').val('');
					}
					orgFlag++;
					feeSelect(newVal);
				}
				changecha('1');
			},
			onLoadSuccess: function () {
				if (getorg != null && getorg != "") {
					$('#stockOrgId').combobox("select", getorg);
					getorg = "";
				}
			}
		});
		//报关代报公司
		var getcustoms = '${bisEnterStock.customsCompanyId}';
		$('#customsCompanyId').combobox({
			method: "GET",
			url: "${ctx}/base/client/getClientAll?setid=${bisEnterStock.customsCompanyId}",
			valueField: 'ids',
			textField: 'clientName',
			mode: 'remote',
			onLoadSuccess: function () {
				if (getcustoms != null && getcustoms != "") {
					$('#customsCompanyId').combobox("select", getcustoms);
					getcustoms = "";
				}
			}
		});

		//报检代报公司
		var getciq = '${bisEnterStock.ciqCompanyId}';
		$('#ciqCompanyId').combobox({
			method: "GET",
			url: "${ctx}/base/client/getClientAll?setid=${bisEnterStock.ciqCompanyId}",
			valueField: 'ids',
			textField: 'clientName',
			mode: 'remote',
			onLoadSuccess: function () {
				if (getciq != null && getciq != "") {
					$('#ciqCompanyId').combobox("select", getciq);
					getciq = "";
				}
			}
		});

		//仓库
		var ck = '${bisEnterStock.warehouseId}';
		if (action == "create") {
			var ck = "1";
		}
		$.ajax({
			type: "GET",
			async: false,
			url: "${ctx}/base/warehouse/getWarehouse",
			data: "",
			dataType: "json",
			success: function (date) {
				$('#warehouseId').combobox({
					data: date,
					value: ck,
					valueField: 'id',
					textField: 'warehouseName',
					editable: false,
					onChange: function (newVal, oldVal) {
						changecha('2');
					}
				});
			}
		});

		//分拣要求1
		$.ajax({
			type: "GET",
			async: false,
			url: "${ctx}/system/dict/json",
			data: "filter_LIKES_type=sorting",
			dataType: "json",
			success: function (date) {
				$('#sortingAsk1').combobox({
					data: date.rows,
					value: '${bisEnterStock.sortingAsk1}',
					valueField: 'label',
					textField: 'label',
					editable: false
				});
			}
		});
		//客户
		var getstockId = '${bisEnterStock.stockId}';
		var stockFlag = 0;
		// 存货方
		$('#stockId').combobox({
			method: "GET",
			url: "${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${bisEnterStock.stockId}",
			valueField: 'ids',
			textField: 'clientName',
			mode: 'remote',
			onChange: function (newVal, oldVal) {
				// -------------------------------------------------
				// 2016-9-12 因第一次加载时,                        -
				// 会触发对 结算单位select控件(stockOrgId) 的修改操作, -
				// 因此通过计数阻止第一次加载时的修改。                 -
				if (stockFlag == 0) {
					stockFlag++;
					return;
				}
				changecha('3');
				/*       if ($('#stockId').combobox("getValue") != $('#stockId').combobox("getText")) {
                          if (typeof(newVal) != "undefined") {
                              var samev = $('#stockId').combobox("getValue");
                              $('#stockOrgId').combobox({
                                  method: "GET",
                                  url: "${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=" + samev,
                            valueField: 'ids',
                            textField: 'clientName',
                            mode: 'remote',
                            onLoadSuccess: function () {
                                if (samev != null && samev != "") {
                                    $('#stockOrgId').combobox("select", samev);
                                    samev = "";
                                }
                            }
                        });
                    }
                } */
			},
			onLoadSuccess: function () {
				if (getstockId != null && getstockId != "") {
					$('#stockId').combobox("select", getstockId);
					getstockId = "";
				}
			}
		});

		//温度temperature
		var wd = '${bisEnterStock.temperature}'
		if (action == "create") {
			wd = "-18℃";
		}
		$.ajax({
			type: "GET",
			async: false,
			url: "${ctx}/system/dict/json",
			data: "filter_LIKES_type=temp",
			dataType: "json",
			success: function (date) {
				$('#temperature').combobox({
					data: date.rows,
					value: wd,
					valueField: 'label',
					textField: 'label',
					editable: false
				});
			}
		});

		//物流容器receptacle
		if (treceptacle == "") {
			treceptacle = "天然木托";
		}
		$.ajax({
			type: "GET",
			async: false,
			url: "${ctx}/system/dict/json",
			data: "filter_LIKES_type=receptacle",
			dataType: "json",
			success: function (date) {
				$('#receptacle').combobox({
					data: date.rows,
					value: treceptacle,
					valueField: 'label',
					textField: 'label',
					editable: false
				});
			}
		});
	}

	//根据结算单位获得费用方案列表
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
						editable: false,
						onChange: function (newVal, oldVal) {
							changecha('4');
						}
					});
				} else {
					$('#feeId').combobox({
						data: date,
						value: $("#feeS").val(),
						valueField: 'schemeNum',
						textField: 'schemeName',
						editable: false,
						onChange: function (newVal, oldVal) {
							changecha('5');
						}
					});
				}
			}
		});
	}

	//数字校验
	function ischeckNum(val) {
		if (val.value) {
			if (!isNaN(val.value)) {

			} else {
				parent.$.messager.show({title: "提示", msg: "请输入数字!", position: "bottomRight"});
				$("#" + val.id).val("");
				myfm.isnum.select();
				return false;
			}
		}
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
					editable: false,
					onChange: function (newVal, oldVal) {
						changecha('6');
					}
				});
			}
		});
	}

	//下载
	function down() {
		var url = "${ctx}/wms/enterStockInfo/download";
		$("#downForm").attr("action", url).submit();
	}

	//导入
	function into() {
		if ($("#auditingStateC").val() == "已审核") {
			parent.$.messager.show({title: "提示", msg: "已审核数据无法操作明细！", position: "bottomRight"});
			return;
		}
		var getLinkId = $("#linkId").val();
		$.ajax({
			type: 'get',
			url: "${ctx}/wms/enterStock/ifsave/" + getLinkId,
			dataType: "text",
			success: function (data) {
				if (data != "success") {
					parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存入库联系单！", position: "bottomRight"});
					return;
				} else {
					intoc(getLinkId);
				}
			}
		});
	}

	function intoc(getLinkId) {
		d = $("#dlg").dialog({
			title: "入库联系单明细导入",
			width: 450,
			height: 450,
			href: '${ctx}/wms/enterStockInfo/into/' + getLinkId,
			maximizable: true,
			modal: true,
			buttons: [{
				text: '确认',
				handler: function () {
					$("#mainform3").submit();
				}
			}, {
				text: '取消',
				handler: function () {
					d.panel('close');
				}
			}]
		});
	}
	//进入入库费用调整
	function inStockAdjust() {
		if ($("#mainForm").form('validate')) {
			var linkId = $("#linkId").val();
			var itemNum = $("#itemNum").val();
			var stockId = $("input[name='stockOrgId']").val();
			$.ajax({
				type: 'get',
				url: "${ctx}/wms/enterStock/ifsave/" + linkId,
				dataType: "text",
				success: function (data) {
					if (data != "success") {
						parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存入库联系单！", position: "bottomRight"});
						return;
					} else {
						itemNum = itemNum.replace(/\//g, "（╯' - ')╯︵ ┻━┻");
						if(action=="create"){
							var feeId=$("input[name='feeId']").val();
							window.parent.mainpage.mainTabs.addModule('费用', 'cost/standingBook/inList/' + linkId + "/" + itemNum + "/" + stockId+"/"+feeId);
						}else{
							var feeId=$("#feeS").val();
							$.ajax({
								type:'POST',
								async:false,
								url:"${ctx}/cost/standingBook/saveCwCost",
								data:{
									linkId:linkId,
									feeId:feeId
								},
								success: function(data){
									//if(data=="success"){
									window.parent.mainpage.mainTabs.addModule('费用', 'cost/standingBook/inList/' + linkId + "/" + itemNum + "/" + stockId+"/"+feeId);
									/* }else{
                                        parent.$.messager.show({ title : "提示",msg:data, position: "bottomRight" });
                                    } */
								}
							});
						}
					}
				}
			});
		}
	}


	//进入客服入库费用调整
	function inStockkfAdjust() {
		if ($("#mainForm").form('validate')) {
			var linkId = $("#linkId").val();
			var itemNum = $("#itemNum").val();
			var stockId = $("input[name='stockOrgId']").val();
			$.ajax({
				type: 'get',
				url: "${ctx}/wms/enterStock/ifsave/" + linkId,
				dataType: "text",
				success: function (data) {
					if (data != "success") {
						parent.$.easyui.messager.show({title: "操作提示", msg: "请先保存入库联系单！", position: "bottomRight"});
						return;
					} else {
						itemNum = itemNum.replace(/\//g, "（╯' - ')╯︵ ┻━┻");
						if(action=="create"){
							var feeId=$("input[name='feeId']").val();
							window.parent.mainpage.mainTabs.addModule('费用', 'cost/standingBook/inKfList/' + linkId + "/" + itemNum + "/" + stockId+"/"+feeId);
						}else{
							var feeId=$("#feeS").val();
							$.ajax({
								type:'POST',
								async:false,
								url:"${ctx}/cost/standingBook/saveCost",
								data:{
									linkId:linkId,
									feeId:feeId
								},
								success: function(data){
									if(data=="success"){
										window.parent.mainpage.mainTabs.addModule('费用', 'cost/standingBook/inKfList/' + linkId + "/" + itemNum + "/" + stockId+"/"+feeId);
									}else{
										parent.$.messager.show({ title : "提示",msg:data, position: "bottomRight" });
									}
								}
							});
						}
					}
				}
			});
		}
	}

	//保存并新建
	function saveAndAdd() {
		var linkId = $("#linkId").val();
		$.ajax({
			type: 'get',
			url: "${ctx}/wms/enterStock/saveAndAdd/" + linkId,
			dataType: "text",
			success: function (data) {
				window.parent.mainpage.mainTabs.addModule('入库联系单修改', 'wms/enterStock/updateEnterStock/' + data);
			}
		});
	}

	function changecha(i) {
		cha = 1;
	}

	//分类监管 核放单
	function mani() {
		var rows = dg.datagrid('getSelections');
		//如果没有选择行记录
		if(rows.length==0) return;
		//选择行记录中 存在多个箱号时的判断
		var ctnNums= [];
		for(var i=0; i<rows.length; i++){
			ctnNums.push(rows[i].ctnNum);
		}
		// if(ctnNums.method2().length>1){
		// 	parent.$.messager.show({title: "提示", msg: "申请核放单时只能选择相同的箱号", position: "bottomRight" });
		// 	return;
		// }

		var ids= [];
		for(var i=0; i<rows.length; i++){
			ids.push(rows[i].id);
		}
		d=$("#dlg").dialog({
			title: '核放单申请',
			width: 560,
			height: 380,
			href:'${ctx}/wms/enterStockInfo/mani/'+ids,
			modal:true,
			buttons:[{
				text:'确认',
				handler:function(){
					$("#mainform").submit();

				}
			},{
				text:'重置',
				handler:function(){
					resetForm();
				}
			},{
				text:'取消',
				handler:function(){
					d.panel('close');
				}
			}]
		});
	}

	//数组消除重复的元素
	Array.prototype.method2 = function(){
		var h={};    //定义一个hash表
		var arr=[];  //定义一个临时数组

		for(var i = 0; i < this.length; i++){    //循环遍历当前数组
			//对元素进行判断，看是否已经存在表中，如果存在则跳过，否则存入临时数组
			if(!h[this[i]]){
				//存入hash表
				h[this[i]] = true;
				//把当前数组元素存入到临时数组中
				arr.push(this[i]);
			}
		}
		return arr;
	}
	//================================================================================================================================
	//依据核注清单号带出表头信息信息
	function getInfo(){
		if($("#ifBonded").is(":checked")){
			if($("#checkListNo").val()==0||$("#checkListNo").val()==""||$("#checkListNo").val()==null){
				parent.$.easyui.messager.alert({
					title: "操作提示",
					msg: "请先输入保税核注清单号！",
					position: "bottomRight"
				});
				return;
			}
			var checkListNo = $("#checkListNo").val();
			//用ajax提交form
			$.ajax({
				async: false,
				type: 'GET',
				url: "${ctx}/wms/preEntryInvtQuery/getOnePreEntryInvtInfo/"+checkListNo,
				dataType: "text",
				success: function(resStr){
					var res = JSON.parse(resStr);
					if(res.msg == "success"){
						var data = res.data;

						$("#gdsMtno").val(data.gdsMtno);
						$("#gdecd").val(data.hsCode);
						$("#gdsNm").val(data.hsItemname);
						$('#dclUnitcd').combobox('select',data.dclUnitcd);
						allDclQty = data.dclTotalAmt;
						$("#dclQty").val(data.dclQty);
						var remainingQuantity = parseFloat(data.dclTotalAmt) - parseFloat(data.dclQty);
						$("#remainingQuantity").val(remainingQuantity+"");
						$("#grossWt").val(data.grossWeight);
						$("#netWt").val(data.netWeight);
					}else{
						parent.$.messager.alert({title: "提示", msg: res.msg, position: "bottomRight" });
					}
				}
			});
		}
	}
	//================================================================================================================================
</script>
</body>
</html>