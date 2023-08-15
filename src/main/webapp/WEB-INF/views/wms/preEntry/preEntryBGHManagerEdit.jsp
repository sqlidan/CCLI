<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">

<div class="easyui-tabs">
	<div title="表头" >
		<div data-options="region:'center'">
			<form id="mainForm">
			<div style="height:auto" class="datagrid-toolbar">
				<shiro:hasPermission name="wms:preEntryBGH:edit">
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="submitForm()">保存</a>
					<span class="toolbar-item dialog-tool-separator"></span>
				</shiro:hasPermission>
				<shiro:hasPermission name="wms:preEntryBGH:edit">
					<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true" data-options="disabled:false" onclick="clearIt()">清空</a>
					<span class="toolbar-item dialog-tool-separator"></span>
				</shiro:hasPermission>
			</div>
			<table class="formTable" >
				<tr>
					<td style="text-align:right;">预录入统一编号</td>
					<td>
						<input type="hidden" id="forId" name="forId"  class="easyui-validatebox" value="${bisPreEntry.forId}">
						<input type="text" id="YLRTYBH" name="YLRTYBH"  class="easyui-validatebox" value="${bisPreEntry.YLRTYBH}" data-options="width:180"  disabled>
					</td>
					<td style="text-align:right;">清单编号</td>
					<td>
						<input type="text" id="QDBH" name="QDBH"  class="easyui-validatebox" value="${bisPreEntry.QDBH}" data-options="width:180"  disabled>
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>清单类型</td>
					<td>
						<select id="QDLX" name="QDLX" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.QDLX}">
						</select>
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>账册编号</td>
					<td>
						<input type="text" id="ZCBH" name="ZCBH"  class="easyui-validatebox" value="${bisPreEntry.ZCBH}" data-options="width:180, required:'required'"  >
					</td>
				</tr>
				<tr>
					<td style="text-align:right;"><span style="color: red">*</span>经营单位编码</td>
					<td>
						<input type="text" id="JYDWBM" name="JYDWBM"  class="easyui-validatebox" value="${bisPreEntry.JYDWBM}" data-options="width:180, required:'required'"  >
					</td>
					<td style="text-align:right;">经营单位社会信用代码</td>
					<td>
						<input type="text" id="JYDWSHXYDM" name="JYDWSHXYDM"  class="easyui-validatebox" value="${bisPreEntry.JYDWSHXYDM}" data-options="width:180"  >
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>经营单位名称</td>
					<td>
						<input type="text" id="JYDWMC" name="JYDWMC"  class="easyui-validatebox" value="${bisPreEntry.JYDWMC}" data-options="width:180, required:'required'"  >
					</td>
				</tr>
				<tr>
					<td style="text-align:right;"><span style="color: red">*</span>加工单位编码</td>
					<td>
						<input type="text" id="JGDWBM" name="JGDWBM"  class="easyui-validatebox" value="${bisPreEntry.JGDWBM}" data-options="width:180, required:'required'"  >
					</td>
					<td style="text-align:right;">加工单位社会信用代码</td>
					<td>
						<input type="text" id="JGDWSHXYDM" name="JGDWSHXYDM"  class="easyui-validatebox" value="${bisPreEntry.JGDWSHXYDM}" data-options="width:180"  >
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>加工单位名称<td>
					<input type="text" id="JGDWMC" name="JGDWMC"  class="easyui-validatebox" value="${bisPreEntry.JGDWMC}" data-options="width:180, required:'required'"  >
				</td>
				</tr>
				<tr>
					<td style="text-align:right;"><span style="color: red">*</span>申报单位编码</td>
					<td>
						<input type="text" id="SBDWBM" name="SBDWBM"  class="easyui-validatebox" value="${bisPreEntry.SBDWBM}" data-options="width:180, required:'required'"  >
					</td>
					<td style="text-align:right;">申报单位社会信用代码</td>
					<td>
						<input type="text" id="SBDWSHXYDM" name="SBDWSHXYDM"  class="easyui-validatebox" value="${bisPreEntry.SBDWSHXYDM}" data-options="width:180"  >
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>申报单位名称</td>
					<td>
						<input type="text" id="SBDWMC" name="SBDWMC"  class="easyui-validatebox" value="${bisPreEntry.SBDWMC}" data-options="width:180, required:'required'"  >
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">录入单位编码</td>
					<td>
						<input type="text" id="LRDWBM" name="LRDWBM"  class="easyui-validatebox" value="${bisPreEntry.LRDWBM}" data-options="width:180"  readonly style="background:#eee">
					</td>
					<td style="text-align:right;">录入单位社会信用代码</td>
					<td>
						<input type="text" id="LRDWSHXYDM" name="LRDWSHXYDM"  class="easyui-validatebox" value="${bisPreEntry.LRDWSHXYDM}" data-options="width:180"  readonly style="background:#eee">
					</td>
					<td style="text-align:right;">录入单位名称</td>
					<td>
						<input type="text" id="LRDWMC" name="LRDWMC"  class="easyui-validatebox" value="${bisPreEntry.LRDWMC}" data-options="width:180" readonly style="background:#eee">
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">企业内部编号</td>
					<td>
						<input type="text" id="QYNBBH" name="QYNBBH"  class="easyui-validatebox" value="${bisPreEntry.QYNBBH}" data-options="width:180"  >
					</td>
					<td style="text-align:right;">申报类型</td>
					<td>
						<select id="SBLX" name="SBLX" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.SBLX}" disabled>
						</select>
					</td>
					<td style="text-align:right;">录入日期</td>
					<td>
						<input id="LRRQ" name="LRRQ" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd HH:mm:ss" />" />
					</td>
					<td style="text-align:right;">清单申报日期</td>
					<td>
						<input id="QDSBRQ" name="QDSBRQ" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/>
					</td>
				</tr>
				<tr>
					<td style="text-align:right;"><span style="color: red">*</span>料件、成品标志</td>
					<td>
						<select id="LJCPBZ" name="LJCPBZ" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.LJCPBZ}">
						</select>
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>监管方式</td>
					<td>
						<select id="JGFS" name="JGFS" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.JGFS}">
						</select>
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>运输方式</td>
					<td>
						<select id="YSFS" name="YSFS" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.YSFS}">
						</select>
					</td>
				</tr>
				<tr>
					<td style="text-align:right;"><span style="color: red">*</span>进出境关别</td>
					<td>
						<select id="JCJGB" name="JCJGB" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.JCJGB}">
						</select>
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>主管海关</td>
					<td>
						<select id="ZGHG" name="ZGHG" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.ZGHG}">
						</select>
					</td>
					<td style="text-align:right;">核扣标志</td>
					<td>
						<select id="HKBZ" name="HKBZ" class="easyui-combobox" data-options="width:180" disabled style="background:#eee" value="${bisPreEntry.HKBZ}">
						</select>
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>起运国(地区)</td>
					<td>
						<select id="QYG" name="QYG" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.QYG}">
						</select>
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">清单进出卡口状态</td>
					<td>
						<select id="QGJCKKZT" name="QGJCKKZT" class="easyui-combobox" data-options="width:180" value="${bisPreEntry.QGJCKKZT}">
						</select>
					</td>
					<td style="text-align:right;">申报表编号</td>
					<td>
						<input type="text" id="SBBBH" name="SBBBH"  class="easyui-validatebox" value="${bisPreEntry.SBBBH}" data-options="width:180"  >
					</td>
					<td style="text-align:right;">流转类型</td>
					<td>
						<select id="LZLX" name="LZLX" class="easyui-combobox" data-options="width:180" value="${bisPreEntry.LZLX}">
						</select>
					</td>
				</tr>
				<tr>
					<td style="text-align:right;"><span style="color: red">*</span>报关标志</td>
					<td>
						<select id="BGBZ" name="BGBZ" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.BGBZ}">
						</select>
					</td>
					<td style="text-align:right;"><span style="color: red">*</span>是否系统生成报关单</td>
					<td>
						<select id="SFXTSCBGD" name="SFXTSCBGD" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.SFXTSCBGD}">
						</select>
					</td>
					<td style="text-align:right;">报关单类型</td>
					<td>
						<select id="BGDLX" name="BGDLX" class="easyui-combobox" data-options="width:180" value="${bisPreEntry.BGDLX}">
						</select>
					</td>
					<td style="text-align:right;">报关类型</td>
					<td>
						<select id="BGLX" name="BGLX" class="easyui-combobox" data-options="width:180" value="${bisPreEntry.BGLX}">
						</select>
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">对应报关单编号</td>
					<td>
						<input type="text" id="DYBGDBH" name="DYBGDBH"  class="easyui-validatebox" value="${bisPreEntry.DYBGDBH}" data-options="width:180"  disabled>
					</td>
					<td style="text-align:right;">对应报关单申报单位编码</td>
					<td>
						<input type="text" id="DYBGDDWBM" name="DYBGDDWBM"  class="easyui-validatebox" value="${bisPreEntry.DYBGDDWBM}" data-options="width:180"  >
					</td>
					<td style="text-align:right;">对应报关单申报单位社会信用代码</td>
					<td>
						<input type="text" id="DYBGDDWSHXYDM" name="DYBGDDWSHXYDM"  class="easyui-validatebox" value="${bisPreEntry.DYBGDDWSHXYDM}" data-options="width:180"  >
					</td>
					<td style="text-align:right;">对应报关单申报单位名称</td>
					<td>
						<input type="text" id="DYBGDDWMC" name="DYBGDDWMC"  class="easyui-validatebox" value="${bisPreEntry.DYBGDDWMC}" data-options="width:180" style="width:900px;" >
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">关联报关单编号</td>
					<td>
						<input type="text" id="GLBGDBH" name="GLBGDBH"  class="easyui-validatebox" value="${bisPreEntry.GLBGDBH}" data-options="width:180"  disabled>
					</td>
					<td style="text-align:right;">关联清单编号</td>
					<td>
						<input type="text" id="GLQDBH" name="GLQDBH"  class="easyui-validatebox" value="${bisPreEntry.GLQDBH}" data-options="width:180"  >
					</td>
					<td style="text-align:right;">关联手(账)册备案号</td>
					<td>
						<input type="text" id="GLSCBAH" name="GLSCBAH"  class="easyui-validatebox" value="${bisPreEntry.GLSCBAH}" data-options="width:180"  >
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">关联报关单境内收发货人编码</td>
					<td>
						<input type="text" id="BLBGDJNSFHRBM" name="BLBGDJNSFHRBM"  class="easyui-validatebox" value="${bisPreEntry.BLBGDJNSFHRBM}" data-options="width:180"  >
					</td>
					<td style="text-align:right;">关联报关单境内收发货人社会信用代码</td>
					<td>
						<input type="text" id="BLBGDJNSFHRSHXYDM" name="BLBGDJNSFHRSHXYDM"  class="easyui-validatebox" value="${bisPreEntry.BLBGDJNSFHRSHXYDM}" data-options="width:180"  >
					</td>
					<td style="text-align:right;">关联报关单境内收发货人名称</td>
					<td>
						<input type="text" id="BLBGDJNSFHRMC" name="BLBGDJNSFHRMC"  class="easyui-validatebox" value="${bisPreEntry.BLBGDJNSFHRMC}" data-options="width:180"  >
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">关联报关单生产销售(消费使用)单位编码</td>
					<td>
						<input type="text" id="GLBGDSCXSDWBM" name="GLBGDSCXSDWBM"  class="easyui-validatebox" value="${bisPreEntry.GLBGDSCXSDWBM}" data-options="width:180"  >
					</td>
					<td style="text-align:right;">关联报关单生产销售(消费使用)单位社会信用代码</td>
					<td>
						<input type="text" id="GLBGDSCXSDWSHXYDM" name="GLBGDSCXSDWSHXYDM"  class="easyui-validatebox" value="${bisPreEntry.GLBGDSCXSDWSHXYDM}" data-options="width:180"  >
					</td>
					<td style="text-align:right;">关联报关单生产销售(消费使用)单位名称</td>
					<td>
						<input type="text" id="GLBGDSCXSDWMC" name="GLBGDSCXSDWMC"  class="easyui-validatebox" value="${bisPreEntry.GLBGDSCXSDWMC}" data-options="width:180"  >
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">关联报关单申报单位编码</td>
					<td>
						<input type="text" id="BLBGDSBDWBM" name="BLBGDSBDWBM"  class="easyui-validatebox" value="${bisPreEntry.BLBGDSBDWBM}" data-options="width:180"  >
					</td>
					<td style="text-align:right;">关联报关单申报单位社会信用代码</td>
					<td>
						<input type="text" id="BLBGDSBDWSHXYDM" name="BLBGDSBDWSHXYDM"  class="easyui-validatebox" value="${bisPreEntry.BLBGDSBDWSHXYDM}" data-options="width:180"  >
					</td>
					<td style="text-align:right;">关联报关单申报单位名称</td>
					<td>
						<input type="text" id="BLBGDSBDWMC" name="BLBGDSBDWMC"  class="easyui-validatebox" value="${bisPreEntry.BLBGDSBDWMC}" data-options="width:180"  >
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">报关单申报日期</td>
					<td>
						<input id="BGDSBRQ" name="BGDSBRQ" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${date}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/>
					</td>
					<td style="text-align:right;">备注</td>
					<td>
						<input type="text" id="BZ" name="BZ"  class="easyui-validatebox" value="${bisPreEntry.BZ}" data-options="width:180" style="width: 720px;" >
					</td>
				</tr>
				<tr>
					<td style="text-align:right;">报关单统一编号</td>
					<td>
						<input type="text" id="BGDTYBH" name="BGDTYBH"  class="easyui-validatebox" value="${bisPreEntry.BGDTYBH}" data-options="width:180" disabled  >
					</td>
<%--					<td style="text-align:right;">报关单草稿(备注)</td>--%>
<%--					<td>--%>
<%--						<input type="text" id="BGDCGBZ" name="BGDCGBZ"  class="easyui-validatebox" value="${bisPreEntry.BGDCGBZ}" data-options="width:180" disabled >--%>
<%--					</td>--%>
					<td style="text-align:right;">操作员卡号</td>
					<td>
						<input type="text" id="CZYKH" name="CZYKH"  class="easyui-validatebox" value="${bisPreEntry.CZYKH}" data-options="width:180" disabled >
					</td>
				</tr>
			</table>
		</form>
		</div>
		<div data-options="region:'south',split:true,border:false" title="随附单据" style="height:500px">
			<div id="tb" style="padding:5px;height:auto" class="datagrid-toolbar">
				<div>
					<shiro:hasPermission name="wms:preEntryBGH:edit">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addDJInfo()">上传</a>
						<span class="toolbar-item dialog-tool-separator"></span>
					</shiro:hasPermission>
					<shiro:hasPermission name="wms:preEntryBGH:edit">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delDJInfo()">删除</a>
						<span class="toolbar-item dialog-tool-separator"></span>
					</shiro:hasPermission>
					<shiro:hasPermission name="wms:preEntryBGH:edit">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="submit()">提交</a>
						<span class="toolbar-item dialog-tool-separator"></span>
					</shiro:hasPermission>
				</div>
			</div>
			<table id="dg" ></table>
			<div id="dlg"></div>
		</div>
	</div>
	<div title="表体" >
		<div data-options="region:'center'">
			<form id="mainForm2"  >
				<table class="formTable" >
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>序号</td>
						<td>
<%--							<input type="hidden" id="forId" name="forId"  class="easyui-validatebox" value="${bisPreEntryInfo.forId}">--%>
							<input type="text" id="xh" name="xh"  class="easyui-validatebox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.xh}">
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>备案序号</td>
						<td>
							<input type="text" id="baxh" name="baxh"  class="easyui-validatebox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.baxh}">
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>商品料号</td>
						<td>
							<input type="text" id="splh" name="splh"  class="easyui-validatebox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.splh}" >
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">报关单商品序号</td>
						<td>
							<input type="text" id="bgdspxh" name="bgdspxh"  class="easyui-validatebox" data-options="width:180" value="${bisPreEntryInfo.bgdspxh}">
						</td>
						<td style="text-align:right;">流转申报表序号</td>
						<td>
							<input type="text" id="lzsbbxh" name="lzsbbxh"  class="easyui-validatebox" data-options="width:180" value="${bisPreEntryInfo.lzsbbxh}">
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>商品编号</td>
						<td>
							<input type="text" id="spxh" name="spxh"  class="easyui-validatebox" data-options="width:180, required:'required'"  value="${bisPreEntryInfo.spxh}">
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>商品名称</td>
						<td>
							<input type="text" id="spmc" name="spmc"  class="easyui-validatebox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.spmc}">
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>规格型号</td>
						<td>
							<input type="text" id="ggxh" name="ggxh"  class="easyui-validatebox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.ggxh}">
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>币制</td>
						<td>
							<select id="bzt" name="bzt" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.bzt}">
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>申报计量单位</td>
						<td>
							<select id="sbjldw" name="sbjldw" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.sbjldw}">
							</select>
						</td>
						<td style="text-align:right;">法定计量单位</td>
						<td>
							<select id="fdjldw" name="fdjldw" class="easyui-combobox" data-options="width:180" value="${bisPreEntryInfo.fdjldw}">
							</select>
						</td>
						<td style="text-align:right;">法定第二计量单位</td>
						<td>
							<select id="fddejldw" name="fddejldw" class="easyui-combobox" data-options="width:180" value="${bisPreEntryInfo.fddejldw}">
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>申报数量</td>
						<td>
							<input type="text" id="sbsl" name="sbsl"  class="easyui-validatebox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.sbsl}">
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>法定数量</td>
						<td>
							<input type="text" id="fdsl" name="fdsl"  class="easyui-validatebox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.fdsl}">
						</td>
						<td style="text-align:right;">第二法定数量</td>
						<td>
							<input type="text" id="defdsl" name="defdsl"  class="easyui-validatebox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.defdsl}"  >
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>企业申报单价</td>
						<td>
							<input type="text" id="qysbdj" name="qysbdj"  class="easyui-validatebox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.qysbdj}">
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>企业申报总价</td>
						<td>
							<input type="text" id="qysbzj" name="qysbzj"  class="easyui-validatebox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.qysbzj}">
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>美元统计总金额</td>
						<td>
							<input type="text" id="mytjzje" name="mytjzje"  class="easyui-validatebox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.mytjzje}">
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>原产国(地区)</td>
						<td>
							<select id="ycg" name="ycg" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.ycg}">
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">重量比例因子</td>
						<td>
							<input type="text" id="zlblyz" name="zlblyz"  class="easyui-validatebox" data-options="width:180" value="${bisPreEntryInfo.zlblyz}">
						</td>
						<td style="text-align:right;">第一比例因子</td>
						<td>
							<input type="text" id="dyblyz" name="dyblyz"  class="easyui-validatebox" data-options="width:180" value="${bisPreEntryInfo.dyblyz}">
						</td>
						<td style="text-align:right;">第二比例因子</td>
						<td>
							<input type="text" id="deblyz" name="deblyz"  class="easyui-validatebox" data-options="width:180" value="${bisPreEntryInfo.deblyz}"  >
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">毛重</td>
						<td>
							<input type="text" id="mz" name="mz"  class="easyui-validatebox" data-options="width:180" value="${bisPreEntryInfo.mz}">
						</td>
						<td style="text-align:right;">净重</td>
						<td>
							<input type="text" id="jz" name="jz"  class="easyui-validatebox" data-options="width:180" value="${bisPreEntryInfo.jz}">
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>征免方式</td>
						<td>
							<select id="zmfs" name="zmfs" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.zmfs}">
							</select>
						</td>
						<td style="text-align:right;">单耗版本号</td>
						<td>
							<input type="text" id="dhbbh" name="dhbbh"  class="easyui-validatebox" data-options="width:180" value="${bisPreEntryInfo.dhbbh}">
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>最终目的国</td>
						<td>
							<select id="zzmdg" name="zzmdg" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.zzmdg}">
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>修改标志</td>
						<td>
							<select id="xgbz" name="xgbz" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntryInfo.xgbz}">
							</select>
						</td>
						<td style="text-align:right;">备注</td>
						<td>
							<input type="text" id="remark" name="remark"  class="easyui-validatebox" data-options="width:180" value="${bisPreEntryInfo.remark}"  >
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'south',split:true,border:false" title="列表" style="height:600px">
			<div id="tb2" style="padding:5px;height:auto" class="datagrid-toolbar">
				<div>
					<shiro:hasPermission name="wms:preEntryBGH:edit">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addInfo()">添加</a>
						<span class="toolbar-item dialog-tool-separator"></span>
					</shiro:hasPermission>
					<shiro:hasPermission name="wms:preEntryBGH:edit">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delInfo()">删除</a>
						<span class="toolbar-item dialog-tool-separator"></span>
					</shiro:hasPermission>
					<shiro:hasPermission name="wms:preEntryBGH:edit">
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="copyInfo()">复制</a>
						<span class="toolbar-item dialog-tool-separator"></span>
					</shiro:hasPermission>
				</div>
			</div>
			<table id="dg2" ></table>
		</div>
	</div>
</div>


<script type="text/javascript">
var dg;
var dg2;
var sfdj;
var d;
var forId = "${bisPreEntry.forId}";
var b=0;
var dhs;
var dt;

var bztAry;//币制
var ycgAry;//原产国
var jldwAry;//计量单位
var zmfsAry;//征免方式
var xgbzAry;//修改标志


//初始化
$(function(){
	gridDG(forId);
	gridDG2(forId);
	selectAjax();

	//报关标识
	$('#BGBZ').combobox({
		onSelect: function (record) {
			var out = $('#BGBZ').combobox("getValue");
			console.log("out== "+out)
			if(out == 2 ){
				$('#SFXTSCBGD').combobox('select','1');
				$('#SFXTSCBGD').combobox({disabled:true});
				$('#BGDLX').combobox('select','2');
				$('#BGDLX').combobox({disabled:true});
				$('#BGLX').combobox('select','2');
				$('#BGLX').combobox({disabled:true});
			}else{
				$('#SFXTSCBGD').combobox('select','0');
				$('#SFXTSCBGD').combobox({ disabled: false });
				$('#BGDLX').combobox('select','');
				$('#BGDLX').combobox({ disabled: false });
				$('#BGLX').combobox('select','');
				$('#BGLX').combobox({ disabled: false });
			}
		}
	})
});
function selectAjax(){
	//表头
	//清单类型
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_INVT_TYPE",
		success : function(date) {
			$('#QDLX').combobox({
				data : date.rows,
				value : '${bisPreEntry.QDLX}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//申报类型
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_DCLTYPECD",
		success : function(date) {
			$('#SBLX').combobox({
				data : date.rows,
				value : '${bisPreEntry.SBLX}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//料件、成品标志
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_MTPCKENDPRDMARKCD",
		success : function(date) {
			$('#LJCPBZ').combobox({
				data : date.rows,
				value : '${bisPreEntry.LJCPBZ}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//运输方式
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_TRSPMODECD",
		success : function(date) {
			$('#YSFS').combobox({
				data : date.rows,
				value : '${bisPreEntry.YSFS}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//核扣标志
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_VRFDEDMARKCD",
		success : function(date) {
			$('#HKBZ').combobox({
				data : date.rows,
				value : '${bisPreEntry.HKBZ}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//清单进出卡口状态
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_INVTIOCHKPTSTUCD",
		success : function(date) {
			$('#QGJCKKZT').combobox({
				data : date.rows,
				value : '${bisPreEntry.QGJCKKZT}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//流转类型
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_LISTTYPE",
		success : function(date) {
			$('#LZLX').combobox({
				data : date.rows,
				value : '${bisPreEntry.LZLX}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//报关标志
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_DCLCUSFLAG",
		success : function(date) {
			$('#BGBZ').combobox({
				data : date.rows,
				value : '${bisPreEntry.BGBZ}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//是否系统生成报关单
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_GENDECFLAG",
		success : function(date) {
			$('#SFXTSCBGD').combobox({
				data : date.rows,
				value : '${bisPreEntry.SFXTSCBGD}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//报关单类型
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_DECTYPE",
		success : function(date) {
			$('#BGDLX').combobox({
				data : date.rows,
				value : '${bisPreEntry.BGDLX}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//报关类型
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_DCLCUSTYPECD",
		success : function(date) {
			$('#BGLX').combobox({
				data : date.rows,
				value : '${bisPreEntry.BGLX}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//监管方式
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_SUPVMODECD",
		success : function(date) {
			$('#JGFS').combobox({
				data : date.rows,
				value : '${bisPreEntry.JGFS}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//进出境关别
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_IMPEXPPORTCD",
		success : function(date) {
			$('#JCJGB').combobox({
				data : date.rows,
				value : '${bisPreEntry.JCJGB}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//主管海关
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_DCL_PLC_CUSCD",
		success : function(date) {
			$('#ZGHG').combobox({
				data : date.rows,
				value : '${bisPreEntry.ZGHG}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//起运国/原产国/最终目的国
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_STSHIP_TRSARV_NATCD",
		success : function(date) {
			ycgAry = date.rows;
			$('#QYG').combobox({
				data : date.rows,
				value : '${bisPreEntry.QYG}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
			$('#ycg').combobox({
				data : date.rows,
				value : '${bisPreEntryInfo.ycg}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
			$('#zzmdg').combobox({
				data : date.rows,
				value : '${bisPreEntryInfo.zzmdg}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});

	//表体
	//币种
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_DCLCURRCD",
		success : function(date) {
			bztAry = date.rows;
			$('#bzt').combobox({
				data : date.rows,
				value : '${bisPreEntryInfo.bzt}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//征免方式
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_LVYRLFMODECD",
		success : function(date) {
			zmfsAry = date.rows;
			$('#zmfs').combobox({
				data : date.rows,
				value : '${bisPreEntryInfo.zmfs}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//修改标志
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/wms/preEntry/dictData/CUS_MODFMARKCD",
		success : function(date) {
			xgbzAry = date.rows;
			$('#xgbz').combobox({
				data : date.rows,
				value : '${bisPreEntryInfo.xgbz}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
	//申报计量单位/法定计量单位/第二法定计量单位
	$.ajax({
		type : "GET",
		async : false,
		url : "${ctx}/system/dict/json",
		data : "filter_LIKES_type=unitOfWeight",
		dataType : "json",
		success : function(date) {
			jldwAry = date.rows;
			$('#sbjldw').combobox({
				data : date.rows,
				value : '${bisPreEntryInfo.sbjldw}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
			$('#fdjldw').combobox({
				data : date.rows,
				value : '${bisPreEntryInfo.fdjldw}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
			$('#fddejldw').combobox({
				data : date.rows,
				value : '${bisPreEntryInfo.fddejldw}',
				valueField : 'value',
				textField : 'label',
				editable : false
			});
		}
	});
}
//================================================================================================================================
//查询单据列表
function gridDG(forId){
   dg =$('#dg').datagrid({
		method: "get",
	    url:'${ctx}/wms/preEntryInfo/jsonDJ/'+forId,
	    fit : true,
		fitColumns : true,
		border : false,
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 20, 50, 100],
		singleSelect:false,
	    columns:[[
	    	{field:'id',hidden:true},
	        {field:'fileName',title:'文件名称',sortable:true},
 	        {field:'fileSize',title:'大小(B)',sortable:true},
			{field:'createBy',title:'上传人',sortable:true},
 	        {field:'createTime',title:'上传时间',sortable:true},
 	        {field:'remark',title:'备注',sortable:true}
	    ]],
	    enableHeaderClickMenu: true,
	    enableHeaderContextMenu: true,
	    enableRowContextMenu: false,
	    toolbar:'#tb'
	});
}
//查询预录入表体
function gridDG2(forId){
	dg2 =$('#dg2').datagrid({
		method: "get",
		url:'${ctx}/wms/preEntryInfo/json/'+forId,
		fit : true,
		fitColumns : true,
		border : false,
		sortOrder:'desc',
		striped:true,
		pagination:true,
		rownumbers:true,
		pageNumber:1,
		pageSize : 20,
		pageList : [ 20, 50, 100],
		singleSelect:false,
		columns:[[
			{field:'id',hidden:true},
			{field:'xh',title:'序号',sortable:true},
			{field:'baxh',title:'备案序号',sortable:true},
			{field:'splh',title:'商品料号',sortable:true},
			{field:'bgdspxh',title:'报关单商品序号',sortable:true},
			{field:'lzsbbxh',title:'流转申报表序号',sortable:true},
			{field:'spxh',title:'商品编号',sortable:true},
			{field:'spmc',title:'商品名称',sortable:true},
			{field:'ggxh',title:'规格型号',sortable:true},
			{field:'bzt',title:'币制',sortable:true,
				formatter : function(value, row, index) {
					var lbelStr;
					for (let i = 0; i < bztAry.length; i++) {
						let row = bztAry[i];
						if(row.value == value){
							lbelStr = row.label;
							break;
						}
					}
					return lbelStr;
				}},
			{field:'sbjldw',title:'申报计量单位',sortable:true,
				formatter : function(value, row, index) {
					var lbelStr;
					for (let i = 0; i < jldwAry.length; i++) {
						let row = jldwAry[i];
						if(row.value == value){
							lbelStr = row.label;
							break;
						}
					}
					return lbelStr;
				}},
			{field:'fdjldw',title:'法定计量单位',sortable:true,
				formatter : function(value, row, index) {
					var lbelStr;
					for (let i = 0; i < jldwAry.length; i++) {
						let row = jldwAry[i];
						if(row.value == value){
							lbelStr = row.label;
							break;
						}
					}
					return lbelStr;
				}},
			{field:'fddejldw',title:'法定第二计量单位',sortable:true,
				formatter : function(value, row, index) {
					var lbelStr;
					for (let i = 0; i < jldwAry.length; i++) {
						let row = jldwAry[i];
						if(row.value == value){
							lbelStr = row.label;
							break;
						}
					}
					return lbelStr;
				}},
			{field:'sbsl',title:'申报数量',sortable:true},
			{field:'fdsl',title:'法定数量',sortable:true},
			{field:'defdsl',title:'第二法定数量',sortable:true},
			{field:'qysbdj',title:'企业申报单价',sortable:true},
			{field:'qysbzj',title:'企业申报总价',sortable:true},
			{field:'mytjzje',title:'美元统计总金额',sortable:true},
			{field:'ycg',title:'原产国(地区)',sortable:true,
				formatter : function(value, row, index) {
					var lbelStr;
					for (let i = 0; i < ycgAry.length; i++) {
						let row = ycgAry[i];
						if(row.value == value){
							lbelStr = row.label;
							break;
						}
					}
					return lbelStr;
				}},
			{field:'zlblyz',title:'重量比例因子',sortable:true},
			{field:'dyblyz',title:'第一比例因子',sortable:true},
			{field:'deblyz',title:'第二比例因子',sortable:true},
			{field:'mz',title:'毛重',sortable:true},
			{field:'jz',title:'净重',sortable:true},
			// {field:'ytdm',title:'用途代码',sortable:true},
			{field:'zmfs',title:'征免方式',sortable:true,
				formatter : function(value, row, index) {
					var lbelStr;
					for (let i = 0; i < zmfsAry.length; i++) {
						let row = zmfsAry[i];
						if(row.value == value){
							lbelStr = row.label;
							break;
						}
					}
					return lbelStr;
				}},
			{field:'dhbbh',title:'单耗版本号',sortable:true},
			{field:'zzmdg',title:'最终目的国',sortable:true,
				formatter : function(value, row, index) {
					var lbelStr;
					for (let i = 0; i < ycgAry.length; i++) {
						let row = ycgAry[i];
						if(row.value == value){
							lbelStr = row.label;
							break;
						}
					}
					return lbelStr;
				}},
			{field:'xgbz',title:'修改标志',sortable:true,
				formatter : function(value, row, index) {
					var lbelStr;
					for (let i = 0; i < xgbzAry.length; i++) {
						let row = xgbzAry[i];
						if(row.value == value){
							lbelStr = row.label;
							break;
						}
					}
					return lbelStr;
				}},
			// {field:'whpbz',title:'危化品标志',sortable:true},
			{field:'remark',title:'备注',sortable:true}
		]],
		enableHeaderClickMenu: true,
		enableHeaderContextMenu: true,
		enableRowContextMenu: false,
		toolbar:'#tb2'
	});
}
//================================================================================================================================
//清空
function clearIt(){
	$('#mainForm').form('clear');
}
//表头保存
function submitForm(){
	if($("#mainForm").form('validate')){
		//用ajax提交form
		$.ajax({
			async: false,
			type: 'POST',
			url: "${ctx}/wms/preEntry/updateBGH",
			data: $('#mainForm').serialize(),
			dataType: "text",
			success: function(msg){
				if(msg == "success"){
					parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });
				}
			}
		});
 	}
}
//================================================================================================================================
//随附单据上传
function addDJInfo(){
	dt=$("#dlg").dialog({
		title: '上传随附单据',
		width: 500,
		height: 300,
		href:'${ctx}/wms/preEntryInfo/createDJ/'+forId,
		maximizable:true,
		modal:true,
		buttons:[{
			text:'确认',
			handler:function(){
				if($("#mainform").form('validate')){
					$("#mainform").submit();
					dt.panel('close');
				}
			}
		},{
			text:'取消',
			handler:function(){
				dt.panel('close');
			}
		}],
		onClose: function (){
			window.setTimeout(function(){gridDG(forId)},100);
		}
	});
}
//随附单据删除
function delDJInfo(){
	var rows = dg.datagrid('getSelections');
	var del = dg.datagrid('getSelected');
	if(del == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	var ids= [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].id);
	}
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type: 'get',
				url: "${ctx}/wms/preEntryInfo/deleteDJinfo/" + ids,
				success: function(data){
					dg.datagrid('clearSelections');
					successTip(data, dg);
				}
			});
		}
	});
}
//随附单据提交
function submit(){
	parent.$.messager.confirm('提示', '您确定要申报提交吗？', function(data){
		if (data){
			$.ajax({
				type: 'get',
				url: "${ctx}/wms/preEntryInfo/submitDJinfo/" + forId,
				success: function(data){
					dg.datagrid('clearSelections');
					successTip(data, dg);
				}
			});
		}
	});
}
//================================================================================================================================
//表体添加
function addInfo(){
	if($("#mainForm2").form('validate')){
		//用ajax提交form
		$.ajax({
			async: false,
			type: 'POST',
			url: "${ctx}/wms/preEntryInfo/create/"+forId,
			data: $('#mainForm2').serialize(),
			dataType: "text",
			success: function(msg){
				if(msg == "success"){
					$('#mainForm2').form('clear');
					parent.$.messager.show({ title : "提示",msg: "保存成功！", position: "bottomRight" });
					window.setTimeout(function(){gridDG2(forId)},100);
				}
			}
		});
	}
}
//表体删除
function delInfo(){
	var rows = dg2.datagrid('getSelections');
	var del = dg2.datagrid('getSelected');
	if(del == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	var ids= [];
	for(var i=0; i<rows.length; i++){
		ids.push(rows[i].id);
	}
	parent.$.messager.confirm('提示', '删除后无法恢复您确定要删除？', function(data){
		if (data){
			$.ajax({
				type: 'get',
				url: "${ctx}/wms/preEntryInfo/deleteinfo/" + ids,
				success: function(data){
					dg2.datagrid('clearSelections');
					successTip(data, dg2);
				}
			});
		}
	});
}
//表体复制
function copyInfo(){
	var copy = dg2.datagrid('getSelected');
	if(copy == null) {
		parent.$.messager.show({title: "提示", msg: "请选择行数据！", position: "bottomRight" });
		return;
	}
	var rows = dg2.datagrid('getSelections');
	if(rows.length > 1){
		parent.$.messager.show({title: "提示", msg: "请选择一行数据进行复制！", position: "bottomRight" });
		return;
	}
	$("#xh").val(copy.xh);
	$("#baxh").val(copy.baxh);
	$("#splh").val(copy.splh);
	$("#bgdspxh").val(copy.bgdspxh);
	$("#lzsbbxh").val(copy.lzsbbxh);
	$("#spxh").val(copy.spxh);
	$("#spmc").val(copy.spmc);
	$("#ggxh").val(copy.ggxh);
	$('#bzt').combobox('select',copy.bzt);
	$('#sbjldw').combobox('select',copy.sbjldw);
	$('#fdjldw').combobox('select',copy.fdjldw);
	$('#fddejldw').combobox('select',copy.fddejldw);
	$("#sbsl").val(copy.sbsl);
	$("#fdsl").val(copy.fdsl);
	$("#defdsl").val(copy.defdsl);
	$("#qysbdj").val(copy.qysbdj);
	$("#qysbzj").val(copy.qysbzj);
	$("#mytjzje").val(copy.mytjzje);
	$('#ycg').combobox('select',copy.ycg);
	$("#zlblyz").val(copy.zlblyz);
	$("#dyblyz").val(copy.dyblyz);
	$("#deblyz").val(copy.deblyz);
	$("#mz").val(copy.mz);
	$("#jz").val(copy.jz);
	// $('#ytdm').combobox('select',copy.ytdm);
	$('#zmfs').combobox('select',copy.zmfs);
	$("#dhbbh").val(copy.dhbbh);
	$('#zzmdg').combobox('select',copy.zzmdg);
	$('#xgbz').combobox('select',copy.xgbz);
	// $('#whpbz').combobox('select',copy.whpbz);
	$("#remark").val(copy.remark);
}
</script>
</body>
</html>