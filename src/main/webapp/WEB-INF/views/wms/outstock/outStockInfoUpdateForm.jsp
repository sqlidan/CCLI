<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>

</head>
<body>
<div>
    <form id="mainform2" action="${ctx}/wms/outstockinfo/${action}" method="post">
        <table class="formTable">
            <tr>
                <td>出库联系单id：</td>
                <td>
                    <input type="hidden" id="id" name="id" value="${outStockInfo.id}"/>
                    <input type="hidden" id="warehouseId" name="warehouseId" value="${outStockInfo.warehouseId}"/>
                    <input type="hidden" id="enterState" name="enterState" class="easyui-validatebox" value="${outStockInfo.enterState}"/>
                    <input type="hidden" id="enterState" name="units" class="easyui-validatebox" value="${outStockInfo.units}"/>
                    <input type="hidden" id="operator" name="operator" class="easyui-validatebox" value="${outStockInfo.operator}"/>
                    <input type="hidden" id="piece" name="piece" class="easyui-validatebox" value="${outStockInfo.piece}"/>
                    <input type="hidden" id="grossSingle" name="grossSingle" class="easyui-validatebox" value="${outStockInfo.grossSingle}"/>
                    <input type="hidden" id="netSingle" name="netSingle" class="easyui-validatebox" value="${outStockInfo.netSingle}"/>
                    <input type="hidden" id="operateTime" name="operateTime"   value="<fmt:formatDate value="${outStockInfo.operateTime}"  pattern="yyyy-MM-dd HH:mm:ss" />"/>
                    <input id="outLinkId" name="outLinkId" class="easyui-validatebox"
                           data-options="width: 150" style="background:#eee" value="${outStockInfo.outLinkId}" readonly/>
                </td>
            </tr>
            <tr>
                <td>提单号：</td>
                <td>
                    <input id="billNum" name="billNum" class="easyui-validatebox"
                           data-options="width:150" style="background:#eee" value="${outStockInfo.billNum}" readonly/>
                </td>
            </tr>
            <tr>
                <td>箱号：</td>
                <td><input id="ctnNum" name="ctnNum" type="text" class="easyui-validatebox"
                           data-options="width:150" style="background:#eee" value="${outStockInfo.ctnNum}" readonly/></td>
            </tr>
            <tr>
                <td>ASN：</td>
                <td><input id="asn" name="asn" type="text" class="easyui-validatebox"
                           data-options="width: 150" style="background:#eee" value="${outStockInfo.asn}" readonly/></td>
            </tr>
            
            <tr>
                <td>品名：</td>
                <td>
                    <input type="text" id="cargoName" name="cargoName" class="easyui-validatebox"
                           data-options="width: 150" style="background:#eee" value="${outStockInfo.cargoName}" readonly/>
                </td>
            </tr>
            <tr>
                <td>SKU：</td>
                <td>
                    <input type="text" id="skuId" name="skuId" class="easyui-validatebox" data-options="width: 150 "
                           value="${outStockInfo.skuId}" style="background:#eee" readonly />
                </td>
            </tr>
            <tr>
                <td>出库件数：</td>
                <td>
                    <input type="text" id="outNum" name="outNum"  class="easyui-validatebox" data-options="width: 150 "
                           value="${outStockInfo.outNum}" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                </td>
            </tr>
            <tr>
                <td>抄码数：</td>
                <td>
                    <input type="text" id="codeNum" name="codeNum"  class="easyui-validatebox" data-options="width: 150 "
                           value="${outStockInfo.codeNum}" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                </td>
            </tr>
            <tr>
                <td>总净重：</td>
                <td>
                    <input type="text" id="netWeight" name="netWeight" class="easyui-validatebox" data-options="width: 150 "
                           value="${outStockInfo.netWeight}" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                </td>
            </tr>
            
            <tr>
                <td>总毛重：</td>
                <td>
                    <input type="text" id="grossWeight" name="grossWeight" class="easyui-validatebox" data-options="width: 150 "
                           value="${outStockInfo.grossWeight}" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                </td>
            </tr>
	        <tr>
	                <td>销售号：</td>
	                <td>
	                    <input id="salesNum" name="salesNum" class="easyui-validatebox"
	                           data-options="width:150" value="${outStockInfo.salesNum}" />
	                </td>
	          </tr>
            <tr>
                <td>规格：</td>
                <td>
                    <input id="typeSize" name="typeSize" class="easyui-validatebox" data-options="width: 150"
                           value="${outStockInfo.typeSize}" style="background:#eee" readonly/>
                </td>
            </tr>
            <tr>
                <td>入库号：</td>
                <td>
                    <input id="rkNum" name="rkNum" class="easyui-validatebox"
                           data-options="width:150" style="background:#eee" value="${outStockInfo.rkNum}" readonly/>
                </td>
            </tr>
            <tr>
                <td>order号：</td>
                <td>
                    <input id="orderNum" name="orderNum" class="easyui-validatebox"
                           data-options="width:150" style="background:#eee" value="${outStockInfo.orderNum}" readonly/>
                </td>
            </tr>
            <tr>
                <td>项目号：</td>
                <td>
                    <input id="projectNum" name="projectNum" class="easyui-validatebox"
                           data-options="width:150" style="background:#eee" value="${outStockInfo.projectNum}" readonly/>
                </td>
            </tr>
            <tr>
                <td>船号：</td>
                <td>
                    <input id="shipNum" name="shipNum" class="easyui-validatebox"
                           data-options="width:150" style="background:#eee" value="${outStockInfo.shipNum}" readonly/>
                </td>
            </tr>
            <tr>
                <td>msc号：</td>
                <td>
                    <input id="mscNum" name="mscNum" class="easyui-validatebox"
                           data-options="width:150" style="background:#eee" value="${outStockInfo.mscNum}" readonly/>
                </td>
            </tr>
            
            <tr>
                <td>lot号：</td>
                <td>
                    <input id="lotNum" name="lotNum" class="easyui-validatebox"
                           data-options="width:150" style="background:#eee" value="${outStockInfo.lotNum}" readonly/>
                </td>
            </tr>
           <tr>
                <td>制造时间：</td>
                <td>
                    <input id="makeTime" name="makeTime" class="easyui-validatebox"
                           data-options="width:150" style="background:#eee" value="<fmt:formatDate value="${outStockInfo.makeTime}"  pattern="yyyy-MM-dd HH:mm:ss" />" readonly/>
                </td>
            </tr>
        </table>
    </form>
</div>

<script type="text/javascript">
    var action = "${action}";
    var linkIdN = "${linkId}";
    var sign = 0;
    //用户 添加修改区分
    if (action == 'create') {
       
    } else if (action == 'update' || action == 'copy') {
        sign = 1;
    } else if (action == 'copy') {
        //清空id,code
        $("#id").val("");
    }

    //提交表单
    $('#mainform2').form({
        onSubmit: function () {
            var isValid = $(this).form('validate');
            return isValid; // 返回false终止表单提交
        },
        success: function (data) {
        	if(data="success"){
               parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight"});
        	}else{
               parent.$.messager.show({title: "提示", msg:"出库件数应小于库存件数减去质押件数！", position: "bottomRight"});
        	}
        }
    });
</script>
</body>
</html>