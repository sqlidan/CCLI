<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<body class="easyui-layout" style="font-family: '微软雅黑'">

<div data-options="region:'center'">

     <form id="searchFrom" action="">

        <!--  <input type="text" id="filter_LIKES_itemNum" name="filter_LIKES_itemNum" class="easyui-validatebox"
                data-options="width:100,prompt: '提单号'"/>

         <input type="text" id="filter_LIKES_linkId" name="filter_LIKES_linkId" class="easyui-validatebox"
                data-options="width:100,prompt: '入库联系单号'"/>

         <span class="toolbar-item dialog-tool-separator"></span>

         <a href="javascript(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="cx()">查询</a> -->

      </form>

    <form id="downForm" method="post">
    </form>

    <form id="mainForm" method="post">

        <div style="height:auto" class="datagrid-toolbar">
            <shiro:hasPermission name="wms:enterStockma:save">
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-standard-disk" plain="true"
                   data-options="disabled:false" onclick="submitForm(0)">保存</a>
                <span class="toolbar-item dialog-tool-separator"></span>
            </shiro:hasPermission>
        </div>
        <table class="formTable" id="formTable">
            <tr>
                <td>审核状态</td>
                <td>
                    <input id="auditingState" name="auditingState" type="hidden" value="${bisEnterStock.auditingState}"/>
                    <input id="auditingStateC" class="easyui-validatebox" data-options="width: 180" value="" readonly/>
                </td>
                <td>联系单号</td>
                <td>
                    <input id="linkId" name="linkId" class="easyui-validatebox" data-options="width: 180"
                           value="${bisEnterStock.linkId}" readonly/>
                </td>
                <td>存货方</td>
                <td>
                    <input type="hidden" id="stockS" name="stockS" value="${bisEnterStock.stockId}"/>
                    <input type="hidden" id="stockSName" name="stockSName" value="${bisEnterStock.stockIn}"/>
                    <input type="hidden" id="stockIn" name="stockIn"/>
                    <select id="stockId" name="stockId" class="easyui-combobox" data-options="width: 180,
                    required:'required'">
                        <option value=""></option>
                    </select>
                </td>
                <td>费用方案</td>
                <td>
                    <input type="hidden" id="feePlan" name="feePlan" value=""></input>
                    <input type="hidden" id="feeS" name="feeS" value="${bisEnterStock.feeId}"/>
                    <input type="hidden" id="feeSName" name="feeSName" value="${bisEnterStock.feePlan}"/>
                    <select class="easyui-combobox" id="feeId" name="feeId"
                            data-options="width:180, required:'required'" value="">
                    </select>
                </td>
            </tr>
            <tr>
                <td>入库提单号</td>
                <td>
                	<input id="itemNumS" name="itemNumS" class="easyui-validatebox" type="hidden"
                           data-options="width: 180, required:'required',prompt:'提单号请勿包含逗号'" value="${bisEnterStock.itemNum}"/>
                    <input id="itemNum" name="itemNum" class="easyui-validatebox"
                           data-options="width: 180, required:'required',prompt:'提单号请勿包含逗号'" value="${bisEnterStock.itemNum}"/>
                </td>
                <td>结算单位</td>
                <td>
                    <input type="hidden" id="stockOrgIdS" name="stockOrgIdS" value="${bisEnterStock.stockOrgId}"/>
                    <input type="hidden" id="stockOrgIdSName" name="stockOrgIdSName" value="${bisEnterStock.stockOrg}"/>
                    <input type="hidden" id="stockOrg" name="stockOrg"/>
                    <select id="stockOrgId" name="stockOrgId" class="easyui-combobox"
                            data-options="width: 180, required:'required'">
                    </select>
                </td>
                <td>箱型尺寸</td>
                <td>
                    <input id="ctnTypeSize" name="ctnTypeSize" class="easyui-validatebox"
                           data-options="width: 180, required:'required'" value="${bisEnterStock.ctnTypeSize}"/>
                </td>
                <td>查验箱量</td>
                <td>
                    <input id="ctnAmount" name="ctnAmount" class="easyui-validatebox" data-options="width: 180"
                           value="${bisEnterStock.ctnAmount}" onkeyup="ischeckNum(this)"/>
                </td>
            </tr>
            <tr>

                <td>是否分拣</td>
                <td>
				<span class="easyui-checkbox">
				<input id="ifSorting" name="ifSorting" type="checkbox" data-options="width: 180" value="1"
                <c:choose>
                       <c:when test="${bisEnterStock.ifSorting == 1}">checked</c:when>
                    <c:otherwise></c:otherwise>
                </c:choose> >
                </td>
                <td>分拣要求1</td>
                <td>
                    <select id="sortingAsk1" name="sortingAsk1" class="easyui-combobox" data-options="width: 180">
                    </select>
                </td>
                <td>分拣要求2</td>
                <td>
                    <select id="sortingAsk2" name="sortingAsk2" class="easyui-combobox" data-options="width: 180">
                    </select>
                </td>
                <td>分拣要求3</td>
                <td>
                    <select id="sortingAsk3" name="sortingAsk3" class="easyui-combobox" data-options="width: 180">
                    </select>
                </td>
            </tr>
            <tr>
                <td>分拣要求4</td>
                <td>
                    <select id="sortingAsk4" name="sortingAsk4" class="easyui-combobox" data-options="width: 180">
                    </select>
                </td>
                <td>分拣要求5</td>
                <td>
                    <select id="sortingAsk5" name="sortingAsk5" class="easyui-combobox" data-options="width: 180">
                    </select>
                </td>
                <td>分拣要求6</td>
                <td>
                    <select id="sortingAsk6" name="sortingAsk6" class="easyui-combobox" data-options="width: 180">
                    </select>
                </td>
                <td>入库仓库</td>
                <td>
                    <input type="hidden" id="warehouse" name="warehouse"/>
                    <select id="warehouseId" name="warehouseId" class="easyui-combobox"
                            data-options="width: 180, required:'required'">
                    </select>
                </td>
            </tr>
            <tr>
                <td>特殊分拣</td>
                <td colspan='3'>
                    <input id="sortingSpecialAsk" name="sortingSpecialAsk" class="easyui-validatebox"
                           data-options="width: 440" value="${bisEnterStock.sortingSpecialAsk}" maxlength="250"/>
                </td>
                <td>特殊要求</td>
                <td colspan='3'>
                    <input id="sortingSpecial" name="sortingSpecial" class="easyui-validatebox"
                           data-options="width: 440" value="${bisEnterStock.sortingSpecial}" maxlength="250"/>
                </td>
            </tr>
            <tr>
                <td>存储温度</td>
                <td>
                    <select id="temperature" name="temperature" class="easyui-combobox"
                            data-options="width: 180, required:'required'">
                    </select>
                </td>
                <td>物流容器</td>
                <td>
                    <input type="hidden" id="receptacleS" value="${bisEnterStock.receptacle}"/>
                    <select id="receptacle" name="receptacle" class="easyui-combobox"
                            data-options="width: 180, required:'required'">
                    </select>
                </td>
                <td>入库号</td>
                <td>
                    <input id="rkNum" name="rkNum" class="easyui-validatebox" data-options="width: 180"
                           value="${bisEnterStock.rkNum}"/>
                </td>
                <td>是否套袋</td>
                <td>
				<span class="easyui-checkbox">
				<input id="ifBagging" name="ifBagging" type="checkbox" data-options="width: 180" value="1"
                <c:choose>
                       <c:when test="${bisEnterStock.ifBagging == 1}">checked</c:when>
                    <c:otherwise></c:otherwise>
                </c:choose> >
                </td>
            </tr>
            <tr>
                <td>是否保税</td>
                <td>
				<span class="easyui-checkbox">
				<input id="ifBonded" name="ifBonded" type="checkbox" data-options="width: 180" value="1"
                <c:choose>
                       <c:when test="${bisEnterStock.ifBonded == 1}">checked</c:when>
                    <c:otherwise></c:otherwise>
                </c:choose> >
                </td>
                <td>是否称重</td>
                <td>
				<span class="easyui-checkbox">
				<input id="ifWeigh" name="ifWeigh" type="checkbox" data-options="width:180" value="1"
                <c:choose>
                       <c:when test="${bisEnterStock.ifWeigh == 1}">checked</c:when>
                    <c:otherwise></c:otherwise>
                </c:choose> >
                </td>
                <td>是否重贴标签</td>
                <td>
				<span class="easyui-checkbox">
				<input id="ifRepeatLable" name="ifRepeatLable" type="checkbox" data-options="width:180" value="1"
                <c:choose>
                       <c:when test="${bisEnterStock.ifRepeatLable == 1}">checked</c:when>
                    <c:otherwise></c:otherwise>
                </c:choose> >
                </td>
                <td>是否缠膜</td>
                <td>
				<span class="easyui-checkbox">
				<input id="ifWrap" name="ifWrap" type="checkbox" data-options="width: 180" value="1"
                <c:choose>
                       <c:when test="${bisEnterStock.ifWrap == 1}">checked</c:when>
                    <c:otherwise></c:otherwise>
                </c:choose> >
                </td>
            </tr>
            <tr>
                <td>是否报关</td>
                <td>
			 	<span class="easyui-checkbox">  
				<input id="ifToCustoms" name="ifToCustoms" type="checkbox" data-options="width: 180" value="1"
                <c:choose>
                       <c:when test="${bisEnterStock.ifToCustoms == 1}">checked</c:when>
                <c:otherwise> </c:otherwise>
                </c:choose> >
                </td>
                <td>报关代报公司</td>
                <td>
                    <input type="hidden" id="customsCompany" name="customsCompany"/>
                    <select id="customsCompanyId" name="customsCompanyId" class="easyui-combobox"
                            data-options="width: 180">
                    </select>
                </td>
                <td>是否报检</td>
                <td>
				<span class="easyui-checkbox">
				<input id="ifToCiq" name="ifToCiq" type="checkbox" data-options="width: 180" value="1"
                <c:choose>
                       <c:when test="${bisEnterStock.ifToCiq == 1}">checked</c:when>
                    <c:otherwise></c:otherwise>
                </c:choose> >
                </td>
                <td>报检代报公司</td>
                <td>
                    <input type="hidden" id="ciqCompany" name="ciqCompany"/>
                    <select id="ciqCompanyId" name="ciqCompanyId" class="easyui-combobox" data-options="width: 180">
                    </select>
                </td>
            </tr>
            <tr>
                <td>是否MSC认证</td>
                <td>
				<span class="easyui-checkbox">
				<input id="ifMacAdmit" name="ifMacAdmit" type="checkbox" data-options="width: 180" value="1"
                <c:choose>
                       <c:when test="${bisEnterStock.ifMacAdmit == 1}">checked</c:when>
                    <c:otherwise></c:otherwise>
                </c:choose> >
                </td>
                <td>是否报分库</td>
                <td>
				<span class="easyui-checkbox">
				<input id="ifChildWarehouse" name="ifChildWarehouse" type="checkbox" data-options="width: 180" value="1"
                <c:choose>
                       <c:when test="${bisEnterStock.ifChildWarehouse == 1}">checked</c:when>
                    <c:otherwise></c:otherwise>
                </c:choose> >
                </td>
                <td>客户自行清关</td>
                <td>
				<span class="easyui-checkbox">
				<input id="ifSelfCustomsClearance" name="ifSelfCustomsClearance" type="checkbox"
                       data-options="width: 180" value="1"
                <c:choose>
                       <c:when test="${bisEnterStock.ifSelfCustomsClearance == 1}">checked</c:when>
                    <c:otherwise></c:otherwise>
                </c:choose> >
                </td>
                <td>是否派车</td>
                <td>
				<span class="easyui-checkbox">
				<input id="ifUseTruck" name="ifUseTruck" type="checkbox" data-options="width: 180" value="1"
                <c:choose>
                       <c:when test="${bisEnterStock.ifUseTruck == 1}">checked</c:when>
                    <c:otherwise></c:otherwise>
                </c:choose> >
                </td>
            </tr>
            <tr>
                <td>是否带木托盘</td>
                <td>
				<span class="easyui-checkbox">
				<input id="ifWithWooden" name="ifWithWooden" type="checkbox" data-options="width: 180" value="1"
                <c:choose>
                       <c:when test="${bisEnterStock.ifWithWooden == 1}">checked</c:when>
                    <c:otherwise></c:otherwise>
                </c:choose> >
                </td>
                <td>ETA</td>
                <td>
                    <input id="etaShip" name="etaShip" class="easyui-my97" data-options="width: 180"
                           value="<fmt:formatDate  value="${bisEnterStock.etaShip}"  pattern="yyyy-MM-dd" />">
                </td>
                <td>计划入库日期</td>
                <td>
                    <input id="etaWarehouse" name="etaWarehouse" class="easyui-my97" datefmt="yyyy-MM-dd"
                           data-options="width: 180, required:'required'"
                           value="<fmt:formatDate value="${bisEnterStock.etaWarehouse}" pattern="yyyy-MM-dd" />">
                </td>
                <td>预计出库日期</td>
                <td>
                    <input id="etdWarehouse" name="etdWarehouse" class="easyui-my97" datefmt="yyyy-MM-dd"
                           data-options="width: 180"
                           value="<fmt:formatDate value="${bisEnterStock.etdWarehouse}" pattern="yyyy-MM-dd" />">
                </td>
            </tr>
            <tr>
                <td>备注</td>
                <td colspan='3'>
                    <input id="remark" name="remark" class="easyui-validatebox" data-options="width: 440"
                           value="${bisEnterStock.remark}" maxlength="500"/>
                </td>
                <td>是否倒箱</td>
                <td>
				<input id="ifBack"  type="checkbox" data-options="width: 180" value="1"
                <c:choose>
                       <c:when test="${bisEnterStock.backDate!=null}">checked</c:when>
                    <c:otherwise></c:otherwise>
                </c:choose> >
                </td>
                <td>倒箱日期</td>
                <td>
                    <input id="backDate" name="backDate" class="easyui-my97" datefmt="yyyy-MM-dd"
                           data-options="width: 180"
                           value="<fmt:formatDate value="${bisEnterStock.backDate}" pattern="yyyy-MM-dd" />">
                </td>
            </tr>
            <tr>
                <td>明细箱数合计：</td>
                <td>
                    <input id="pieceInfo" class="easyui-validatebox" data-options="width: 180" readonly
                           style="background:#eee"/>
                </td>
                <td>明细净重合计：</td>
                <td>
                    <input id="netInfo" class="easyui-validatebox" data-options="width: 180" readonly
                           style="background:#eee"/>
                </td>
                <td>明细毛重合计：</td>
                <td>
                    <input id="grossInfo" class="easyui-validatebox" data-options="width: 180" readonly
                           style="background:#eee"/>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="hidden" id="operator" name="operator" class="easyui-validatebox"
                           data-options="width: 180" value="${bisEnterStock.operator}" readonly style="background:#eee">
                </td>
                <td>
                    <input type="hidden" id="operateTime" name="operateTime" data-options="width: 180"
                           value="<fmt:formatDate  value="${bisOutStock.operateTime}"  pattern="yyyy-MM-dd HH:mm:ss" />"
                           readonly style="background:#eee">
                </td>
                <td>
                    <input type="hidden" id="updateTime" name="updateTime" data-options="width: 180"
                           value="<fmt:formatDate  value="${bisOutStock.updateTime}"  pattern="yyyy-MM-dd HH:mm:ss" />"
                           readonly style="background:#eee">
                </td>
            </tr>
        </table>
    </form>
</div>
<div data-options="region:'south',split:true,border:false" title="明细" style="height:400px">
    <div id="tb" style="padding:5px;height:auto" class="datagrid-toolbar">
        <div>
            <shiro:hasPermission name="wms:enterStockInfo:update">
                <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
                   onclick="editInfo()">修改</a>
                <span class="toolbar-item dialog-tool-separator"></span>
            </shiro:hasPermission>
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
    var cha = 0;
    var flag = "";

    $(function () {
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
                {field: 'ctnNum', title: '箱号', sortable: true, width: 150},
                {field: 'orderNum', title: 'order号', sortable: true, width: 150},
                {field: 'mscNum', title: 'msc', sortable: true, width: 150},
                {field: 'projectNum', title: '项目号', sortable: true, width: 150},
                {field: 'cargoName', title: '品名', sortable: true, width: 300},
                {field: 'sku', title: 'sku', sortable: true, width: 150},
                {field: 'rkNum', title: '入库号', sortable: true, width: 150},
                {field: 'lotNum', title: 'lot', sortable: true, width: 150},
                {field: 'shipNum', title: '捕捞船名', sortable: true, width: 150},
                {field: 'typeSize', title: '规格', sortable: true, width: 100},
                {field: 'piece', title: '箱数', sortable: true},
                {field: 'netWeight', title: '总净重', sortable: true, width: 100},
                {field: 'grossWeight', title: '总毛重', sortable: true, width: 100},
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
        ;
        $("#pieceInfo").val(pieces);
        $("#netInfo").val(net.toFixed(2));
        $("#grossInfo").val(gross.toFixed(2));

    }

    //保存
    function submitForm(copys) {
        checkIfUpdate();
        
       	if(flag == "error0"){
       		parent.$.easyui.messager.show({
                title: "操作提示",
                msg: "货物已出库或货转，不能修改当前入库联系单！",
                position: "bottomRight"
            });
            return;
       	}else if(flag == "error1"){
       		parent.$.easyui.messager.show({
                title: "操作提示",
                msg: "联系单已生成费用，不能修改当前入库联系单！",
                position: "bottomRight"
            });
            return;
       	}
        
        if ($("#auditingStateC").val() == "未保存") {
            var state = 0;
        } else {
            var state = 1;
        }

        //	console.log('${bisEnterStock.backDate}');

        if ('${bisEnterStock.backDate}' != "" && ( $('#backDate').datebox('getValue') + " 00:00:00.0") != '${bisEnterStock.backDate}') {

            // 		console.log('${bisEnterStock.backDate}');
            //		console.log($('#backDate').datebox('getValue'));
            changecha('7');
        }

       /*  if ($("#auditingStateC").val() == "待审核" || $("#auditingStateC").val() == "已审核") {
            if (cha != "0") {
                parent.$.easyui.messager.show({
                    title: "操作提示",
                    msg: "已提交或已审核状态下，客户、费用方案、仓库及倒箱日期不允许修改！",
                    position: "bottomRight"
                });
                return;
            }
        } */

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
                            url: "${ctx}/wms/enterStock/createEnterStockUpdate/" + state,
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
                                    /* if (copys == 1) {
                                        saveAndAdd();
                                    } */
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
                    url: "${ctx}/wms/enterStock/createEnterStockUpdate/" + state,
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
                        }
                    }
                });
            }
        } else {
            $(":checkbox[value='0']").attr("checked", false);
            $("input[type='checkbox']").not("input:checked").val("1");
        }
    }


    //修改入库联系单明细
    function editInfo() {
		
    	   checkIfUpdate();
           
          	if(flag == "error0"){
          		parent.$.easyui.messager.show({
                   title: "操作提示",
                   msg: "货物已出库或货转，不能修改当前入库联系单！",
                   position: "bottomRight"
               });
               return;
          	}else if(flag == "error1"){
          		parent.$.easyui.messager.show({
                   title: "操作提示",
                   msg: "联系单已生成费用，不能修改当前入库联系单！",
                   position: "bottomRight"
               });
               return;
          	}
    	
       /*  if ($("#auditingStateC").val() == "已审核") {
            parent.$.messager.show({title: "提示", msg: "已审核数据无法操作明细！", position: "bottomRight"});
            return;
        } */

        var row = dg.datagrid('getSelected');

        if (rowIsNull(row)) return;

        d = $("#dlg").dialog({
            title: '修改入库明细-箱号',
            width: 380,
            height: 380,
            href: '${ctx}/wms/enterStockInfo/updateUpdate/' + row.id,
            maximizable: true,
            modal: true,
            buttons: [{
                text: '确认',
                handler: function () {

                    //$("#bigTypeName").val($("#bigType").combobox("getText"));
                    //$("#littleTypeName").val($("#littleType").combobox("getText"));

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


    // 用于 费用方案 计数判断
    var orgFlag = 0;

    //下拉框
    function selectAJAX() {
        var tFeeId = $("#feeS").val();
        var tStockId = $("#stockS").val();
        var treceptacle = $("#receptacleS").val();
        var orgId = $("#stockOrgIdS").val();

        //结算单位
        var getorg = '${bisEnterStock.stockOrgId}';

        // 结算单位
        $('#stockOrgId').combobox({
            method: "GET",
            url: "${ctx}/base/client/getClientAll?filter_EQS_clientSort=0&setid=${bisEnterStock.stockOrgId}",
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
                    // -------------------------------------------------


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

        //分拣要求2
        $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/system/dict/json",
            data: "filter_LIKES_type=sorting",
            dataType: "json",
            success: function (date) {
                $('#sortingAsk2').combobox({
                    data: date.rows,
                    value: '${bisEnterStock.sortingAsk2}',
                    valueField: 'label',
                    textField: 'label',
                    editable: false
                });
            }
        });

        //分拣要求3
        $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/system/dict/json",
            data: "filter_LIKES_type=sorting",
            dataType: "json",
            success: function (date) {
                $('#sortingAsk3').combobox({
                    data: date.rows,
                    value: '${bisEnterStock.sortingAsk3}',
                    valueField: 'label',
                    textField: 'label',
                    editable: false
                });
            }
        });

        //分拣要求4
        $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/system/dict/json",
            data: "filter_LIKES_type=sorting",
            dataType: "json",
            success: function (date) {
                $('#sortingAsk4').combobox({
                    data: date.rows,
                    value: '${bisEnterStock.sortingAsk4}',
                    valueField: 'label',
                    textField: 'label',
                    editable: false
                });
            }
        });

        //分拣要求5
        $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/system/dict/json",
            data: "filter_LIKES_type=sorting",
            dataType: "json",
            success: function (date) {
                $('#sortingAsk5').combobox({
                    data: date.rows,
                    value: '${bisEnterStock.sortingAsk5}',
                    valueField: 'label',
                    textField: 'label',
                    editable: false
                });
            }
        });

        //分拣要求6
        $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/system/dict/json",
            data: "filter_LIKES_type=sorting",
            dataType: "json",
            success: function (date) {
                $('#sortingAsk6').combobox({
                    data: date.rows,
                    value: '${bisEnterStock.sortingAsk6}',
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
                // -------------------------------------------------

                changecha('3');

                if ($('#stockId').combobox("getValue") != $('#stockId').combobox("getText")) {
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
                }

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

    //保存并新建
/*     function saveAndAdd() {
        var linkId = $("#linkId").val();
        $.ajax({
            type: 'get',
            url: "${ctx}/wms/enterStock/saveAndAdd/" + linkId,
            dataType: "text",
            success: function (data) {
                console.log(data);
                window.parent.mainpage.mainTabs.addModule('入库联系单修改', 'wms/enterStock/updateEnterStock/' + data);
            }
        });
    } */


    function changecha(i) {
// 	console.log("被运行了"+i);
        cha = 1;
    }
	
    function cx(){
    	var itemNum = $("#filter_LIKES_itemNum").val();
    	var linkId = $("#filter_LIKES_linkId").val();
    	 $.ajax({
    		 async:false,
             type: 'POST',
             url: "${ctx}/wms/enterStock/serchEnterStockUpdate",
             dataType: "text",
             data:{"itemNum":itemNum,"linkId":linkId},
             success: function (data) {
            	 
            	 if(data.length>0){
            		 

            	 }
                 //window.parent.mainpage.mainTabs.closeCurrentTab();
                 //window.parent.mainpage.mainTabs.addModule('入库联系单修改', 'wms/enterStock/managerUpdate?linkId=' + data);
            	 
                  window.parent.mainpage.mainTabs.addModule('入库联系单修改', 'wms/enterStock/managerUpdate?linkId=' + data);
                 
                 /* window.setTimeout(function () {
                	 window.parent.mainpage.mainTabs.addModule('入库联系单修改', 'wms/enterStock/managerUpdate?linkId=' + data);
                     
                    
                 }, 10);
                 window.setTimeout(function () {
                	
                     
                     window.parent.mainpage.mainTabs.closeCurrentTab();
                 }, 20);
 				 window.setTimeout(function () {
                	
                     
 					window.parent.mainpage.mainTabs.refCurrentTab();//刷新TAB
                 }, 100);  */
                 
                
             }
             
            	
             
         });
    	
    }
    //检查是否符合条件 
    function checkIfUpdate(){
    	var linkId = $("#linkId").val();
 
    	$.ajax({
    		async:false,
    		type:'POST',
    		url:"${ctx}/wms/enterStock/checkEnterStockUpdate",
    		dataType: "text",
            data:{"linkId":linkId},
            success: function (data) {
              
                flag = data;
               
            }
    	});
    	
    }
</script>
</body>
</html>