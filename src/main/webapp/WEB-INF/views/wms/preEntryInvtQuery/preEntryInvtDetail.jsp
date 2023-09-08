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
				<table class="formTable" >
					<tr>
						<td style="text-align:right;">预录入统一编号</td>
						<td>
							<input type="text" id="seqNo" name="seqNo"  class="easyui-validatebox" value="${bisPreEntry.seqNo}" data-options="width:180"  readonly style="background:#eee">
						</td>
						<td style="text-align:right;">清单编号</td>
						<td>
							<input type="text" id="bondInvtNo" name="bondInvtNo"  class="easyui-validatebox" value="${bisPreEntry.bondInvtNo}" data-options="width:180"  readonly style="background:#eee">
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>清单类型</td>
						<td>
							<select id="invtType" name="invtType" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.invtType}" disabled>
							</select>
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>账册编号</td>
						<td>
							<input type="text" id="putrecNo" name="putrecNo"  class="easyui-validatebox" value="${bisPreEntry.putrecNo}" data-options="width:180, required:'required'" readonly style="background:#eee">
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>经营单位编码</td>
						<td>
							<input type="text" id="bizopEtpsno" name="bizopEtpsno"  class="easyui-validatebox" value="${bisPreEntry.bizopEtpsno}" data-options="width:180, required:'required'" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">经营单位社会信用代码</td>
						<td>
							<input type="text" id="bizopEtpsSccd" name="bizopEtpsSccd"  class="easyui-validatebox" value="${bisPreEntry.bizopEtpsSccd}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>经营单位名称</td>
						<td>
							<input type="text" id="bizopEtpsNm" name="bizopEtpsNm"  class="easyui-validatebox" value="${bisPreEntry.bizopEtpsNm}" data-options="width:180, required:'required'" readonly style="background:#eee" >
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>加工单位编码</td>
						<td>
							<input type="text" id="rcvgdEtpsno" name="rcvgdEtpsno"  class="easyui-validatebox" value="${bisPreEntry.rcvgdEtpsno}" data-options="width:180, required:'required'" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">加工单位社会信用代码</td>
						<td>
							<input type="text" id="rvsngdEtpsSccd" name="rvsngdEtpsSccd"  class="easyui-validatebox" value="${bisPreEntry.rvsngdEtpsSccd}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>加工单位名称<td>
						<input type="text" id="rcvgdEtpsNm" name="rcvgdEtpsNm"  class="easyui-validatebox" value="${bisPreEntry.rcvgdEtpsNm}" data-options="width:180, required:'required'" readonly style="background:#eee" >
					</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>申报单位编码</td>
						<td>
							<input type="text" id="dclEtpsno" name="dclEtpsno"  class="easyui-validatebox" value="${bisPreEntry.dclEtpsno}" data-options="width:180, required:'required'" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">申报单位社会信用代码</td>
						<td>
							<input type="text" id="dclEtpsSccd" name="dclEtpsSccd"  class="easyui-validatebox" value="${bisPreEntry.dclEtpsSccd}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>申报单位名称</td>
						<td>
							<input type="text" id="dclEtpsNm" name="dclEtpsNm"  class="easyui-validatebox" value="${bisPreEntry.dclEtpsNm}" data-options="width:180, required:'required'" readonly style="background:#eee" >
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">录入单位编码</td>
						<td>
							<input type="text" id="inputCode" name="inputCode"  class="easyui-validatebox" value="${bisPreEntry.inputCode}" data-options="width:180"  readonly style="background:#eee">
						</td>
						<td style="text-align:right;">录入单位社会信用代码</td>
						<td>
							<input type="text" id="inputCreditCode" name="inputCreditCode"  class="easyui-validatebox" value="${bisPreEntry.inputCreditCode}" data-options="width:180"  readonly style="background:#eee">
						</td>
						<td style="text-align:right;">录入单位名称</td>
						<td>
							<input type="text" id="inputName" name="inputName"  class="easyui-validatebox" value="${bisPreEntry.inputName}" data-options="width:180" readonly style="background:#eee">
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">企业内部编号</td>
						<td>
							<input type="text" id="etpsInnerInvtNo" name="etpsInnerInvtNo"  class="easyui-validatebox" value="${bisPreEntry.etpsInnerInvtNo}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">申报类型</td>
						<td>
							<select id="dclTypecd" name="dclTypecd" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.dclTypecd}" disabled>
							</select>
						</td>
						<td style="text-align:right;">录入日期</td>
						<td>
							<input id="LRRQ" name="LRRQ" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${inputTime}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/>
						</td>
						<td style="text-align:right;">清单申报日期</td>
						<td>
							<input id="QDSBRQ" name="QDSBRQ" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${invtDclTime}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/>
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>料件、成品标志</td>
						<td>
							<select id="mtpckEndprdMarkcd" name="mtpckEndprdMarkcd" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.mtpckEndprdMarkcd}" disabled>
							</select>
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>监管方式</td>
						<td>
							<select id="supvModecd" name="supvModecd" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.supvModecd}" disabled>
							</select>
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>运输方式</td>
						<td>
							<select id="trspModecd" name="trspModecd" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.trspModecd}" disabled>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>进出境关别</td>
						<td>
							<select id="impexpPortcd" name="impexpPortcd" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.impexpPortcd}" disabled>
							</select>
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>主管海关</td>
						<td>
							<select id="dclPlcCuscd" name="dclPlcCuscd" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.dclPlcCuscd}" disabled>
							</select>
						</td>
						<td style="text-align:right;">核扣标志</td>
						<td>
							<select id="vrfdedMarkcd" name="vrfdedMarkcd" class="easyui-combobox" data-options="width:180" disabled style="background:#eee" value="${bisPreEntry.vrfdedMarkcd}" disabled>
							</select>
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>起运国(地区)</td>
						<td>
							<select id="stshipTrsarvNatcd" name="stshipTrsarvNatcd" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.stshipTrsarvNatcd}" disabled>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">清单进出卡口状态</td>
						<td>
							<select id="invtIochkptStucd" name="invtIochkptStucd" class="easyui-combobox" data-options="width:180" value="${bisPreEntry.invtIochkptStucd}" disabled>
							</select>
						</td>
						<td style="text-align:right;">申报表编号</td>
						<td>
							<input type="text" id="applyNo" name="applyNo"  class="easyui-validatebox" value="${bisPreEntry.applyNo}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">流转类型</td>
						<td>
							<select id="listType" name="listType" class="easyui-combobox" data-options="width:180" value="${bisPreEntry.listType}" disabled>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>报关标志</td>
						<td>
							<select id="dclcusFlag" name="dclcusFlag" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.dclcusFlag}" disabled>
							</select>
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>是否系统生成报关单</td>
						<td>
							<select id="genDecFlag" name="genDecFlag" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.genDecFlag}" disabled>
							</select>
						</td>
						<td style="text-align:right;">报关单类型</td>
						<td>
							<select id="decType" name="decType" class="easyui-combobox" data-options="width:180" value="${bisPreEntry.decType}" disabled>
							</select>
						</td>
						<td style="text-align:right;">报关类型</td>
						<td>
							<select id="dclcusTypecd" name="dclcusTypecd" class="easyui-combobox" data-options="width:180" value="${bisPreEntry.dclcusTypecd}" disabled>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">对应报关单编号</td>
						<td>
							<input type="text" id="entryNo" name="entryNo"  class="easyui-validatebox" value="${bisPreEntry.entryNo}" data-options="width:180"  readonly style="background:#eee">
						</td>
						<td style="text-align:right;">对应报关单申报单位编码</td>
						<td>
							<input type="text" id="corrEntryDclEtpsNo" name="corrEntryDclEtpsNo"  class="easyui-validatebox" value="${bisPreEntry.corrEntryDclEtpsNo}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">对应报关单申报单位社会信用代码</td>
						<td>
							<input type="text" id="corrEntryDclEtpsSccd" name="corrEntryDclEtpsSccd"  class="easyui-validatebox" value="${bisPreEntry.corrEntryDclEtpsSccd}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">对应报关单申报单位名称</td>
						<td>
							<input type="text" id="corrEntryDclEtpsNm" name="v"  class="easyui-validatebox" value="${bisPreEntry.corrEntryDclEtpsNm}" data-options="width:180" readonly style="width:900px;background:#eee">
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">关联报关单编号</td>
						<td>
							<input type="text" id="rltEntryNo" name="rltEntryNo"  class="easyui-validatebox" value="${bisPreEntry.rltEntryNo}" data-options="width:180" readonly style="background:#eee">
						</td>
						<td style="text-align:right;">关联清单编号</td>
						<td>
							<input type="text" id="rltInvtNo" name="rltInvtNo"  class="easyui-validatebox" value="${bisPreEntry.rltInvtNo}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">关联手(账)册备案号</td>
						<td>
							<input type="text" id="rltPutrecNo" name="rltPutrecNo"  class="easyui-validatebox" value="${bisPreEntry.rltPutrecNo}" data-options="width:180" readonly style="background:#eee" >
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">关联报关单境内收发货人编码</td>
						<td>
							<input type="text" id="rltEntryBizopEtpsno" name="rltEntryBizopEtpsno"  class="easyui-validatebox" value="${bisPreEntry.rltEntryBizopEtpsno}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">关联报关单境内收发货人社会信用代码</td>
						<td>
							<input type="text" id="rltEntryBizopEtpsSccd" name="rltEntryBizopEtpsSccd"  class="easyui-validatebox" value="${bisPreEntry.rltEntryBizopEtpsSccd}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">关联报关单境内收发货人名称</td>
						<td>
							<input type="text" id="rltEntryBizopEtpsNm" name="rltEntryBizopEtpsNm"  class="easyui-validatebox" value="${bisPreEntry.rltEntryBizopEtpsNm}" data-options="width:180" readonly style="background:#eee" >
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">关联报关单生产销售(消费使用)单位编码</td>
						<td>
							<input type="text" id="rltEntryRcvgdEtpsno" name="rltEntryRcvgdEtpsno"  class="easyui-validatebox" value="${bisPreEntry.rltEntryRcvgdEtpsno}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">关联报关单生产销售(消费使用)单位社会信用代码</td>
						<td>
							<input type="text" id="rltEntryRvsngdEtpsSccd" name="rltEntryRvsngdEtpsSccd"  class="easyui-validatebox" value="${bisPreEntry.rltEntryRvsngdEtpsSccd}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">关联报关单生产销售(消费使用)单位名称</td>
						<td>
							<input type="text" id="rltEntryRcvgdEtpsNm" name="rltEntryRcvgdEtpsNm"  class="easyui-validatebox" value="${bisPreEntry.rltEntryRcvgdEtpsNm}" data-options="width:180" readonly style="background:#eee" >
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">关联报关单申报单位编码</td>
						<td>
							<input type="text" id="rltEntryDclEtpsno" name="rltEntryDclEtpsno"  class="easyui-validatebox" value="${bisPreEntry.rltEntryDclEtpsno}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">关联报关单申报单位社会信用代码</td>
						<td>
							<input type="text" id="rltEntryDclEtpsSccd" name="rltEntryDclEtpsSccd"  class="easyui-validatebox" value="${bisPreEntry.rltEntryDclEtpsSccd}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">关联报关单申报单位名称</td>
						<td>
							<input type="text" id="rltEntryDclEtpsNm" name="rltEntryDclEtpsNm"  class="easyui-validatebox" value="${bisPreEntry.rltEntryDclEtpsNm}" data-options="width:180" readonly style="background:#eee" >
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">报关单申报日期</td>
						<td>
							<input id="BGDSBRQ" name="BGDSBRQ" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${entryDclTime}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/>
						</td>
						<td style="text-align:right;">备注</td>
						<td>
							<input type="text" id="rmk" name="rmk"  class="easyui-validatebox" value="${bisPreEntry.rmk}" data-options="width:180" readonly style="width: 720px;background:#eee">
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">报关单统一编号</td>
						<td>
							<input type="text" id="entrySeqNo" name="entrySeqNo"  class="easyui-validatebox" value="${bisPreEntry.entrySeqNo}" data-options="width:180" readonly style="background:#eee"  >
						</td>
						<td style="text-align:right;">操作员卡号</td>
						<td>
							<input type="text" id="icCardNo" name="icCardNo"  class="easyui-validatebox" value="${bisPreEntry.icCardNo}" data-options="width:180" readonly style="background:#eee" >
						</td>
					</tr>
				</table>
			</form>
		</div>
<%--		<div data-options="region:'south',split:true,border:false" title="随附单据" style="height:500px">--%>
<%--			<table id="dg" ></table>--%>
<%--			<div id="dlg"></div>--%>
<%--		</div>--%>
	</div>
	<div title="表体" >
		<div data-options="region:'south',split:true,border:false" title="列表" style="height:600px">
			<table id="dg2" ></table>
		</div>
	</div>
</div>


<script type="text/javascript">
	var dg;
	var dg2;
	var sfdj;
	var d;
	var id = "${ID}";
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
		// gridDG(id);
		gridDG2();
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
				$('#invtType').combobox({
					data : date.rows,
					value : '${bisPreEntry.invtType}',
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
				$('#dclTypecd').combobox({
					data : date.rows,
					value : '${bisPreEntry.dclTypecd}',
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
				$('#mtpckEndprdMarkcd').combobox({
					data : date.rows,
					value : '${bisPreEntry.mtpckEndprdMarkcd}',
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
				$('#trspModecd').combobox({
					data : date.rows,
					value : '${bisPreEntry.trspModecd}',
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
				$('#vrfdedMarkcd').combobox({
					data : date.rows,
					value : '${bisPreEntry.vrfdedMarkcd}',
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
				$('#invtIochkptStucd').combobox({
					data : date.rows,
					value : '${bisPreEntry.invtIochkptStucd}',
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
				$('#listType').combobox({
					data : date.rows,
					value : '${bisPreEntry.listType}',
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
				$('#dclcusFlag').combobox({
					data : date.rows,
					value : '${bisPreEntry.dclcusFlag}',
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
				$('#genDecFlag').combobox({
					data : date.rows,
					value : '${bisPreEntry.genDecFlag}',
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
				$('#decType').combobox({
					data : date.rows,
					value : '${bisPreEntry.decType}',
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
				$('#dclcusTypecd').combobox({
					data : date.rows,
					value : '${bisPreEntry.dclcusTypecd}',
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
				$('#supvModecd').combobox({
					data : date.rows,
					value : '${bisPreEntry.supvModecd}',
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
				$('#impexpPortcd').combobox({
					data : date.rows,
					value : '${bisPreEntry.impexpPortcd}',
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
				$('#dclPlcCuscd').combobox({
					data : date.rows,
					value : '${bisPreEntry.dclPlcCuscd}',
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
				$('#stshipTrsarvNatcd').combobox({
					data : date.rows,
					value : '${bisPreEntry.stshipTrsarvNatcd}',
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
			}
		});
		//征免方式
		$.ajax({
			type : "GET",
			async : false,
			url : "${ctx}/wms/preEntry/dictData/CUS_LVYRLFMODECD",
			success : function(date) {
				zmfsAry = date.rows;
			}
		});
		//修改标志
		$.ajax({
			type : "GET",
			async : false,
			url : "${ctx}/wms/preEntry/dictData/CUS_MODFMARKCD",
			success : function(date) {
				xgbzAry = date.rows;
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
			}
		});
	}
//================================================================================================================================
	//查询预录入表体
	function gridDG2(){
		dg2 =$('#dg2').datagrid({
			method: "get",
			url:'${ctx}/wms/preEntryInvtQuery/jsonInvtList',
			fit : true,
			fitColumns : true,
			border : false,
			sortOrder:'desc',
			striped:true,
			pagination:true,
			rownumbers:true,
			pageNumber:1,
			pageSize : 100,
			pageList : [20, 30, 50, 100 ],
			singleSelect:false,
			columns:[[
				{field:'gdsSeqno',title:'序号',sortable:true},
				{field:'putrecSeqno',title:'备案序号',sortable:true},
				{field:'gdsMtno',title:'商品料号',sortable:true},
				{field:'entryGdsSeqno',title:'报关单商品序号',sortable:true},
				{field:'applyTbSeqno',title:'流转申报表序号',sortable:true},
				{field:'gdecd',title:'商品编号',sortable:true},
				{field:'gdsNm',title:'商品名称',sortable:true},
				{field:'gdsSpcfModelDesc',title:'规格型号',sortable:true},
				{field:'dclCurrcd',title:'币制',sortable:true,
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
				{field:'dclUnitcd',title:'申报计量单位',sortable:true,
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
				{field:'lawfUnitcd',title:'法定计量单位',sortable:true,
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
				{field:'secdLawfUnitcd',title:'法定第二计量单位',sortable:true,
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
				{field:'dclQty',title:'申报数量',sortable:true},
				{field:'lawfQty',title:'法定数量',sortable:true},
				{field:'secdLawfQty',title:'第二法定数量',sortable:true},
				{field:'dclUprcAmt',title:'企业申报单价',sortable:true},
				{field:'dclTotalAmt',title:'企业申报总价',sortable:true},
				{field:'usdStatTotalAmt',title:'美元统计总金额',sortable:true},
				{field:'natcd',title:'原产国(地区)',sortable:true,
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
				{field:'wtSfVal',title:'重量比例因子',sortable:true},
				{field:'fstSfVal',title:'第一比例因子',sortable:true},
				{field:'secdSfVal',title:'第二比例因子',sortable:true},
				{field:'grossWt',title:'毛重',sortable:true},
				{field:'netWt',title:'净重',sortable:true},
				// {field:'useCd',title:'用途代码',sortable:true},
				{field:'lvyrlfModecd',title:'征免方式',sortable:true,
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
				{field:'ucnsVerno',title:'单耗版本号',sortable:true},
				{field:'destinationNatcd',title:'最终目的国',sortable:true,
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
				{field:'modfMarkcd',title:'修改标志',sortable:true,
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
				{field:'rmk',title:'备注',sortable:true}
			]],
			enableHeaderClickMenu: true,
			enableHeaderContextMenu: true,
			enableRowContextMenu: false,
			toolbar:'#tb2'
		});
	}

	<%--//查询单据列表--%>
	<%--function gridDG(id){--%>
	<%--	dg =$('#dg').datagrid({--%>
	<%--		method: "get",--%>
	<%--		url:'${ctx}/wms/preEntryInvtQuery/jsonDJ/'+id,--%>
	<%--		fit : true,--%>
	<%--		fitColumns : true,--%>
	<%--		border : false,--%>
	<%--		sortOrder:'desc',--%>
	<%--		striped:true,--%>
	<%--		pagination:true,--%>
	<%--		rownumbers:true,--%>
	<%--		pageNumber:1,--%>
	<%--		pageSize : 100,--%>
	<%--		singleSelect:false,--%>
	<%--		columns:[[--%>
	<%--			{field:'id',hidden:true},--%>
	<%--			{field:'fileName',title:'文件名称',sortable:true},--%>
	<%--			{field:'fileSize',title:'大小(B)',sortable:true},--%>
	<%--			{field:'createBy',title:'上传人',sortable:true},--%>
	<%--			{field:'createTime',title:'上传时间',sortable:true},--%>
	<%--			{field:'remark',title:'备注',sortable:true}--%>
	<%--		]],--%>
	<%--		enableHeaderClickMenu: true,--%>
	<%--		enableHeaderContextMenu: true,--%>
	<%--		enableRowContextMenu: false,--%>
	<%--		toolbar:'#tb'--%>
	<%--	});--%>
	<%--}--%>
//================================================================================================================================

</script>
</body>
</html>