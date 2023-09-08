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
							<input type="text" id="YLRTYBH" name="YLRTYBH"  class="easyui-validatebox" value="${bisPreEntry.seqNo}" data-options="width:180"  readonly style="background:#eee">
						</td>
						<td style="text-align:right;">清单编号</td>
						<td>
							<input type="text" id="QDBH" name="QDBH"  class="easyui-validatebox" value="${bisPreEntry.bondInvtNo}" data-options="width:180"  readonly style="background:#eee">
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>清单类型</td>
						<td>
							<select id="QDLX" name="QDLX" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.invtType}" disabled>
							</select>
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>账册编号</td>
						<td>
							<input type="text" id="ZCBH" name="ZCBH"  class="easyui-validatebox" value="${bisPreEntry.putrecNo}" data-options="width:180, required:'required'" readonly style="background:#eee">
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>经营单位编码</td>
						<td>
							<input type="text" id="JYDWBM" name="JYDWBM"  class="easyui-validatebox" value="${bisPreEntry.bizopEtpsno}" data-options="width:180, required:'required'" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">经营单位社会信用代码</td>
						<td>
							<input type="text" id="JYDWSHXYDM" name="JYDWSHXYDM"  class="easyui-validatebox" value="${bisPreEntry.bizopEtpsSccd}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>经营单位名称</td>
						<td>
							<input type="text" id="JYDWMC" name="JYDWMC"  class="easyui-validatebox" value="${bisPreEntry.bizopEtpsNm}" data-options="width:180, required:'required'" readonly style="background:#eee" >
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>加工单位编码</td>
						<td>
							<input type="text" id="JGDWBM" name="JGDWBM"  class="easyui-validatebox" value="${bisPreEntry.rcvgdEtpsno}" data-options="width:180, required:'required'" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">加工单位社会信用代码</td>
						<td>
							<input type="text" id="JGDWSHXYDM" name="JGDWSHXYDM"  class="easyui-validatebox" value="${bisPreEntry.rvsngdEtpsSccd}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>加工单位名称<td>
						<input type="text" id="JGDWMC" name="JGDWMC"  class="easyui-validatebox" value="${bisPreEntry.rcvgdEtpsNm}" data-options="width:180, required:'required'" readonly style="background:#eee" >
					</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>申报单位编码</td>
						<td>
							<input type="text" id="SBDWBM" name="SBDWBM"  class="easyui-validatebox" value="${bisPreEntry.dclEtpsno}" data-options="width:180, required:'required'" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">申报单位社会信用代码</td>
						<td>
							<input type="text" id="SBDWSHXYDM" name="SBDWSHXYDM"  class="easyui-validatebox" value="${bisPreEntry.dclEtpsSccd}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>申报单位名称</td>
						<td>
							<input type="text" id="SBDWMC" name="SBDWMC"  class="easyui-validatebox" value="${bisPreEntry.dclEtpsNm}" data-options="width:180, required:'required'" readonly style="background:#eee" >
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">录入单位编码</td>
						<td>
							<input type="text" id="LRDWBM" name="LRDWBM"  class="easyui-validatebox" value="${bisPreEntry.inputCode}" data-options="width:180"  readonly style="background:#eee">
						</td>
						<td style="text-align:right;">录入单位社会信用代码</td>
						<td>
							<input type="text" id="LRDWSHXYDM" name="LRDWSHXYDM"  class="easyui-validatebox" value="${bisPreEntry.inputCreditCode}" data-options="width:180"  readonly style="background:#eee">
						</td>
						<td style="text-align:right;">录入单位名称</td>
						<td>
							<input type="text" id="LRDWMC" name="LRDWMC"  class="easyui-validatebox" value="${bisPreEntry.inputName}" data-options="width:180" readonly style="background:#eee">
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">企业内部编号</td>
						<td>
							<input type="text" id="QYNBBH" name="QYNBBH"  class="easyui-validatebox" value="${bisPreEntry.etpsInnerInvtNo}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">申报类型</td>
						<td>
							<select id="SBLX" name="SBLX" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.dclTypecd}" disabled>
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
							<select id="LJCPBZ" name="LJCPBZ" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.mtpckEndprdMarkcd}" disabled>
							</select>
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>监管方式</td>
						<td>
							<select id="JGFS" name="JGFS" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.supvModecd}" disabled>
							</select>
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>运输方式</td>
						<td>
							<select id="YSFS" name="YSFS" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.trspModecd}" disabled>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>进出境关别</td>
						<td>
							<select id="JCJGB" name="JCJGB" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.impexpPortcd}" disabled>
							</select>
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>主管海关</td>
						<td>
							<select id="ZGHG" name="ZGHG" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.dclPlcCuscd}" disabled>
							</select>
						</td>
						<td style="text-align:right;">核扣标志</td>
						<td>
							<select id="HKBZ" name="HKBZ" class="easyui-combobox" data-options="width:180" disabled style="background:#eee" value="${bisPreEntry.vrfdedMarkcd}" disabled>
							</select>
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>起运国(地区)</td>
						<td>
							<select id="QYG" name="QYG" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.stshipTrsarvNatcd}" disabled>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">清单进出卡口状态</td>
						<td>
							<select id="QGJCKKZT" name="QGJCKKZT" class="easyui-combobox" data-options="width:180" value="${bisPreEntry.invtIochkptStucd}" disabled>
							</select>
						</td>
						<td style="text-align:right;">申报表编号</td>
						<td>
							<input type="text" id="SBBBH" name="SBBBH"  class="easyui-validatebox" value="${bisPreEntry.applyNo}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">流转类型</td>
						<td>
							<select id="LZLX" name="LZLX" class="easyui-combobox" data-options="width:180" value="${bisPreEntry.listType}" disabled>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align:right;"><span style="color: red">*</span>报关标志</td>
						<td>
							<select id="BGBZ" name="BGBZ" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.dclcusFlag}" disabled>
							</select>
						</td>
						<td style="text-align:right;"><span style="color: red">*</span>是否系统生成报关单</td>
						<td>
							<select id="SFXTSCBGD" name="SFXTSCBGD" class="easyui-combobox" data-options="width:180, required:'required'" value="${bisPreEntry.genDecFlag}" disabled>
							</select>
						</td>
						<td style="text-align:right;">报关单类型</td>
						<td>
							<select id="BGDLX" name="BGDLX" class="easyui-combobox" data-options="width:180" value="${bisPreEntry.decType}" disabled>
							</select>
						</td>
						<td style="text-align:right;">报关类型</td>
						<td>
							<select id="BGLX" name="BGLX" class="easyui-combobox" data-options="width:180" value="${bisPreEntry.dclcusTypecd}" disabled>
							</select>
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">对应报关单编号</td>
						<td>
							<input type="text" id="DYBGDBH" name="DYBGDBH"  class="easyui-validatebox" value="${bisPreEntry.entryNo}" data-options="width:180"  readonly style="background:#eee">
						</td>
						<td style="text-align:right;">对应报关单申报单位编码</td>
						<td>
							<input type="text" id="DYBGDDWBM" name="DYBGDDWBM"  class="easyui-validatebox" value="${bisPreEntry.corrEntryDclEtpsNo}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">对应报关单申报单位社会信用代码</td>
						<td>
							<input type="text" id="DYBGDDWSHXYDM" name="DYBGDDWSHXYDM"  class="easyui-validatebox" value="${bisPreEntry.corrEntryDclEtpsSccd}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">对应报关单申报单位名称</td>
						<td>
							<input type="text" id="DYBGDDWMC" name="DYBGDDWMC"  class="easyui-validatebox" value="${bisPreEntry.corrEntryDclEtpsNm}" data-options="width:180" readonly style="width:900px;background:#eee">
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">关联报关单编号</td>
						<td>
							<input type="text" id="GLBGDBH" name="GLBGDBH"  class="easyui-validatebox" value="${bisPreEntry.rltEntryNo}" data-options="width:180" readonly style="background:#eee">
						</td>
						<td style="text-align:right;">关联清单编号</td>
						<td>
							<input type="text" id="GLQDBH" name="GLQDBH"  class="easyui-validatebox" value="${bisPreEntry.rltInvtNo}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">关联手(账)册备案号</td>
						<td>
							<input type="text" id="GLSCBAH" name="GLSCBAH"  class="easyui-validatebox" value="${bisPreEntry.rltPutrecNo}" data-options="width:180" readonly style="background:#eee" >
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">关联报关单境内收发货人编码</td>
						<td>
							<input type="text" id="BLBGDJNSFHRBM" name="BLBGDJNSFHRBM"  class="easyui-validatebox" value="${bisPreEntry.rltEntryBizopEtpsno}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">关联报关单境内收发货人社会信用代码</td>
						<td>
							<input type="text" id="BLBGDJNSFHRSHXYDM" name="BLBGDJNSFHRSHXYDM"  class="easyui-validatebox" value="${bisPreEntry.rltEntryBizopEtpsSccd}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">关联报关单境内收发货人名称</td>
						<td>
							<input type="text" id="BLBGDJNSFHRMC" name="BLBGDJNSFHRMC"  class="easyui-validatebox" value="${bisPreEntry.rltEntryBizopEtpsNm}" data-options="width:180" readonly style="background:#eee" >
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">关联报关单生产销售(消费使用)单位编码</td>
						<td>
							<input type="text" id="GLBGDSCXSDWBM" name="GLBGDSCXSDWBM"  class="easyui-validatebox" value="${bisPreEntry.rltEntryRcvgdEtpsno}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">关联报关单生产销售(消费使用)单位社会信用代码</td>
						<td>
							<input type="text" id="GLBGDSCXSDWSHXYDM" name="GLBGDSCXSDWSHXYDM"  class="easyui-validatebox" value="${bisPreEntry.rltEntryRvsngdEtpsSccd}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">关联报关单生产销售(消费使用)单位名称</td>
						<td>
							<input type="text" id="GLBGDSCXSDWMC" name="GLBGDSCXSDWMC"  class="easyui-validatebox" value="${bisPreEntry.rltEntryRcvgdEtpsNm}" data-options="width:180" readonly style="background:#eee" >
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">关联报关单申报单位编码</td>
						<td>
							<input type="text" id="BLBGDSBDWBM" name="BLBGDSBDWBM"  class="easyui-validatebox" value="${bisPreEntry.rltEntryDclEtpsno}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">关联报关单申报单位社会信用代码</td>
						<td>
							<input type="text" id="BLBGDSBDWSHXYDM" name="BLBGDSBDWSHXYDM"  class="easyui-validatebox" value="${bisPreEntry.rltEntryDclEtpsSccd}" data-options="width:180" readonly style="background:#eee" >
						</td>
						<td style="text-align:right;">关联报关单申报单位名称</td>
						<td>
							<input type="text" id="BLBGDSBDWMC" name="BLBGDSBDWMC"  class="easyui-validatebox" value="${bisPreEntry.rltEntryDclEtpsNm}" data-options="width:180" readonly style="background:#eee" >
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">报关单申报日期</td>
						<td>
							<input id="BGDSBRQ" name="BGDSBRQ" class="easyui-my97" data-options="width:180" datefmt="yyyy-MM-dd HH:mm:ss"  value="<fmt:formatDate value="${entryDclTime}" pattern="yyyy-MM-dd HH:mm:ss" />" disabled/>
						</td>
						<td style="text-align:right;">备注</td>
						<td>
							<input type="text" id="BZ" name="BZ"  class="easyui-validatebox" value="${bisPreEntry.rmk}" data-options="width:180" readonly style="width: 720px;background:#eee">
						</td>
					</tr>
					<tr>
						<td style="text-align:right;">报关单统一编号</td>
						<td>
							<input type="text" id="BGDTYBH" name="BGDTYBH"  class="easyui-validatebox" value="${bisPreEntry.entrySeqNo}" data-options="width:180" readonly style="background:#eee"  >
						</td>
						<td style="text-align:right;">操作员卡号</td>
						<td>
							<input type="text" id="CZYKH" name="CZYKH"  class="easyui-validatebox" value="${bisPreEntry.icCardNo}" data-options="width:180" readonly style="background:#eee" >
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
		gridDG2(id);
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
			}
		});
	}
//================================================================================================================================
	//查询预录入表体
	function gridDG2(id){
		dg2 =$('#dg2').datagrid({
			method: "get",
			url:'${ctx}/wms/preEntryInvtQuery/jsonInvtList/'+id,
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