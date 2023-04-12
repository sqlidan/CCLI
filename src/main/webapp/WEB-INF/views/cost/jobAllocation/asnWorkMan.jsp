<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>

</head>
<body>
<div>
    <form id="mainform2" action="${ctx}/cost/piecework/assign" method="post">
        <table class="formTable">
            <tr>
                <td>ASN号：</td>
                <td>
                    <input id="asnId" name="asnId" class="easyui-validatebox"
                           style="width: 250, required:'required'" value="${asn}" readonly>
                </td>
                <td>
                    <input type="hidden" id="remindId" name="remindId" class="easyui-validatebox"
                           style="width: 150, required:'required'" value="${remindId}" readonly>
                </td>
            </tr>
            <tr>
                <td>库管人员</td>
                <td>
                    <select id="workMan" name="workMan" class="easyui-combobox"
                            data-options="width: 180, required:'required'">
                    </select>
                </td>
            </tr>
            <tr>
                <td>理货人员</td>
                <td>
                    <select id="assLh" name="lhPerson" class="easyui-combobox"
                            data-options="width: 180, required:'required'">
                    </select>
                </td>
            </tr>
            <tr>
                <td>叉车人员</td>
                <td>
                    <select id="assCC" name="ccPerson" class="easyui-combobox"
                            data-options="width: 180, required:'required'">
                    </select>
                </td>
            </tr>
            <tr>
                <td>叉车人员</td>
                <td>
                    <select id="assCC2" name="ccPerson2" class="easyui-combobox"
                            data-options="width: 180">
                    </select>
                </td>
            </tr>
          </table>
    </form>
</div>

<script type="text/javascript">

$(function () {
	  // 库管人员
	    var man="${workMan}";
        $('#workMan').combobox({
            method: "GET",
            url: "${ctx}/cost/piecework/getKgAll?setid=${workMan}",
            valueField: 'person',
            textField: 'person',
            mode: 'remote',
            onLoadSuccess: function () {
                if (man != null && man != "") {
                    $('#workMan').combobox("select", man);
                    man = "";
                }
            },
            onSelect:function (newVal, oldVal){
            	if(newVal!=oldVal){
            		changeOther(newVal);
            	}
            }
        });
        
        
         //理货人员
        $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/cost/piecework/getOther",
            data: {"workType":'2',"person":man},
            dataType: "json",
            success: function (date) {
            		if (date.length != 0){
		                $('#assLh').combobox({
		                    data: date.rows,
		                    value: '${lhPerson}',
		                    valueField: 'person',
		                    textField: 'person',
		                    editable: false
		                });
		            }else{
		            	 $('#assLh').combobox({
	                        data: date,
	                        valueField: 'person',
		                    textField: 'person',
	                        editable: false
	                    });
		            }
            }
        });
        
         //叉车人员
        $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/cost/piecework/getOther",
            data: {"workType":'3',"person":man},
            dataType: "json",
            success: function (date) {
            	if (date.length != 0){
	                $('#assCC').combobox({
	                    data: date.rows,
	                    value: '${ccPerson}',
	                    valueField: 'person',
	                    textField: 'person',
	                    editable: false
	                });
	            }else{
	            	$('#assCC').combobox({
	                        data: date,
	                        valueField: 'person',
		                    textField: 'person',
	                        editable: false
	                    });
	            }
            }
        });
        
         //叉车人员2（正常关联父节点）
     /*    $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/cost/piecework/getOther",
            data: {"workType":'3',"person":man},
            dataType: "json",
            success: function (date) {
            	if (date.length != 0){
	                $('#assCC2').combobox({
	                    data: date.rows,
	                    value: '${ccPerson2}',
	                    valueField: 'person',
	                    textField: 'person',
	                    editable: false
	                });
	            }else{
	            	$('#assCC2').combobox({
	                        data: date,
	                        valueField: 'person',
		                    textField: 'person',
	                        editable: false
	                    });
	            }
            }
        }); */
        //叉车人员2：可以取所有人 20170818要求修改
     /*    $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/cost/piecework/getAllOther",
            //data: {"workType":'3',"person":man},
            dataType: "json",
            success: function (date) {
            	if (date.length != 0){
	                $('#assCC2').combobox({
	                    data: date.rows,
	                    value: '${ccPerson2}',
	                    valueField: 'person',
	                    textField: 'person',
	                    editable: false
	                });
	            }else{
	            	$('#assCC2').combobox({
	                        data: date,
	                        valueField: 'person',
		                    textField: 'person',
	                        editable: false
	                    });
	            }
            }
        }); */
  	  // 叉车人员2
	    var manAssCC2="${ccPerson2}";
        $('#assCC2').combobox({
            method: "GET",
            url: "${ctx}/cost/piecework/getAllUsers?setid=${ccPerson2}",
            valueField: 'person',
            textField: 'person',
            mode: 'remote',
            onLoadSuccess: function () {
                if (man != null && man != "") {
                    $('#assCC2').combobox("select", manAssCC2);
                    manAssCC2 = "";
                }
            }
        });
})


//库管人员onChange事件
function changeOther(newVal){
	 //理货人员
        $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/cost/piecework/getOther",
            data: {"workType":'2',"person":newVal.person},
            dataType: "json",
            success: function (date) {
	                $('#assLh').combobox({
	                    data: date.rows,
	                    value: '${lhPerson}',
	                    valueField: 'person',
	                    textField: 'person',
	                    editable: false
	                });
            }
        });
        
         //叉车人员
        $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/cost/piecework/getOther",
            data: {"workType":'3',"person":newVal.person},
            dataType: "json",
            success: function (date) {
                $('#assCC').combobox({
                    data: date.rows,
                    value: '${ccPerson}',
                    valueField: 'person',
                    textField: 'person',
                    editable: false
                });
            }
        });
        
        //叉车人员2
      /*   $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/cost/piecework/getOther",
            data: {"workType":'3',"person":newVal.person},
            dataType: "json",
            success: function (date) {
                $('#assCC2').combobox({
                    data: date.rows,
                    value: '${ccPerson2}',
                    valueField: 'person',
                    textField: 'person',
                    editable: false
                });
            }
        }); */
        
        //叉车人员2
      /*   $.ajax({
            type: "GET",
            async: false,
            url: "${ctx}/cost/piecework/getAllOther",
            //data: {"workType":'3',"person":newVal.person},
            dataType: "json",
            success: function (date) {
                $('#assCC2').combobox({
                    data: date.rows,
                    value: '${ccPerson2}',
                    valueField: 'person',
                    textField: 'person',
                    editable: false
                });
            }
        }); */
        var manAssCC2="${ccPerson2}";
        $('#assCC2').combobox({
            method: "GET",
            url: "${ctx}/cost/piecework/getAllUsers?setid=${ccPerson2}",
            valueField: 'person',
            textField: 'person',
            mode: 'remote',
            onLoadSuccess: function () {
                if (man != null && man != "") {
                    $('#assCC2').combobox("select", manAssCC2);
                    manAssCC2 = "";
                }
            }
        });
        
}




    //提交表单
    $('#mainform2').form({
        onSubmit: function () {
            var isValid = $(this).form('validate');
            return isValid; // 返回false终止表单提交
        },
        success: function (data) {
            parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight"});
        }
    });
</script>
</body>
</html>