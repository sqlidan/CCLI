<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
</head>
<body class="easyui-layout" style="font-family:'微软雅黑'">
<div data-options="region:'center',border:false,title:'费用方案与合同信息'">
    <table class="formTable" style="margin:10px">
        <tr>
            <td>方案编号</td>
            <td><input class="easyui-validatebox" data-options="width:200" value="${scheme.schemeNum}" readonly="readonly"/></td>
            <td>方案名称</td>
            <td><input class="easyui-validatebox" data-options="width:200" value="${scheme.schemeName}" readonly="readonly"/></td>
        </tr>
        <tr>
            <td>方案客户</td>
            <td><input class="easyui-validatebox" data-options="width:200" value="${scheme.customsName}" readonly="readonly"/></td>
            <td>收付类型</td>
            <td><input class="easyui-validatebox" data-options="width:200" value="<c:choose><c:when test="${scheme.ifGet == '1'}">应收</c:when><c:when test="${scheme.ifGet == '2'}">应付</c:when></c:choose>" readonly="readonly"/></td>
        </tr>
        <tr>
            <td>方案状态</td>
            <td><input class="easyui-validatebox" data-options="width:200" value="<c:choose><c:when test="${scheme.programState == '1'}">已审核</c:when><c:otherwise>未审核</c:otherwise></c:choose>" readonly="readonly"/></td>
            <td>创建时间</td>
            <td><input class="easyui-validatebox" data-options="width:200" value="<fmt:formatDate value="${scheme.operateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly="readonly"/></td>
        </tr>
        <tr>
            <td>合同号</td>
            <td><input class="easyui-validatebox" data-options="width:200" value="${scheme.contractNum}" readonly="readonly"/></td>
            <td>合同客户</td>
            <td><input class="easyui-validatebox" data-options="width:200" value="${scheme.contractClientName}" readonly="readonly"/></td>
        </tr>
        <tr>
            <td>合同状态</td>
            <td><input class="easyui-validatebox" data-options="width:200" value="<c:choose><c:when test="${scheme.contractState == '1'}">已审核</c:when><c:otherwise>未审核</c:otherwise></c:choose>" readonly="readonly"/></td>
            <td>揽货人</td>
            <td><input class="easyui-validatebox" data-options="width:200" value="${scheme.canvassionPerson}" readonly="readonly"/></td>
        </tr>
        <tr>
            <td>签订时间</td>
            <td><input class="easyui-validatebox" data-options="width:200" value="<fmt:formatDate value="${scheme.signTime}" pattern="yyyy-MM-dd"/>" readonly="readonly"/></td>
            <td>到期时间</td>
            <td><input class="easyui-validatebox" data-options="width:200" value="<fmt:formatDate value="${scheme.expirationTime}" pattern="yyyy-MM-dd"/>" readonly="readonly"/></td>
        </tr>
        <tr>
            <td>方案备注</td>
            <td colspan="3"><textarea rows="2" cols="72" readonly="readonly" style="font-size:12px;font-family:'微软雅黑';background-color:#EBEBE4">${scheme.schemeRemark}</textarea></td>
        </tr>
        <tr>
            <td>合同备注</td>
            <td colspan="3"><textarea rows="2" cols="72" readonly="readonly" style="font-size:12px;font-family:'微软雅黑';background-color:#EBEBE4">${scheme.contractRemark}</textarea></td>
        </tr>
    </table>
</div>
<div data-options="region:'south',split:true,border:false,title:'费用方案明细'" style="height:330px">
    <table id="schemeInfoGrid"></table>
</div>
<div data-options="region:'east',split:true,border:false,title:'合同费目明细'" style="width:480px">
    <table id="contractInfoGrid"></table>
</div>
<script type="text/javascript">
$(function(){
    $('#schemeInfoGrid').datagrid({
        method:'GET',
        url:'${ctx}/base/schemeInfo/json/${scheme.schemeNum}',
        fit:true,
        fitColumns:true,
        border:false,
        idField:'id',
        striped:true,
        pagination:true,
        rownumbers:true,
        pageNumber:1,
        pageSize:20,
        pageList:[10,20,30,40,50],
        singleSelect:true,
        columns:[[
            {field:'feeName',title:'费目',sortable:true,width:100},
            {field:'unit',title:'价格',sortable:true,width:80},
            {field:'currencyName',title:'币种',sortable:true,width:80},
            {field:'minPrice',title:'下限',sortable:true,width:80},
            {field:'maxPrice',title:'上限',sortable:true,width:80},
            {field:'termattributeName',title:'条件属性',sortable:true,width:90},
            {field:'unitName',title:'计费方式',sortable:true,width:90},
            {field:'ifPay',title:'是否垫付',sortable:true,width:80,formatter:function(value){return value == 1 ? '是' : '否';}},
            {field:'gearCode',title:'档位代码',sortable:true,width:100},
            {field:'gearExp',title:'档位说明',sortable:true,width:120}
        ]]
    });

    $('#contractInfoGrid').datagrid({
        method:'GET',
        url:'${ctx}/base/contractInfo/json/${scheme.contractNum}',
        fit:true,
        fitColumns:true,
        border:false,
        idField:'id',
        striped:true,
        pagination:true,
        rownumbers:true,
        pageNumber:1,
        pageSize:20,
        pageList:[10,20,30,40,50],
        singleSelect:true,
        columns:[[
            {field:'feeType',title:'费用类别',sortable:true,width:90},
            {field:'expenseCode',title:'费目',sortable:true,width:90},
            {field:'cargoName',title:'名称',sortable:true,width:100},
            {field:'price',title:'价格',sortable:true,width:80},
            {field:'remark',title:'备注',sortable:true,width:120},
            {field:'billUnit',title:'计量单位',sortable:true,width:90},
            {field:'currencyType',title:'币种',sortable:true,width:80}
        ]]
    });
});
</script>
</body>
</html>
