<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>

<head>
    <title></title>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>

<body>

<div>
    <form id="mainform3" action="${ctx}/cost/bispayinfo/${action}/${payId}" method="post"
          enctype="multipart/form-data">
        <table class="formTable">
            <tr>
                <td><input name="file" type="file" extend="*.xls;*.xlsx"></td>
            </tr>
        </table>
    </form>
</div>

<script type="text/javascript">
    //提交表单
    $('#mainform3').form({
        onSubmit: function () {
            return true;	// 返回false终止表单提交
        },
        success: function (data) {
            if (data == "success") {
                parent.$.messager.show({title: "提示", msg: "保存成功！", position: "bottomRight"});
                gridDG();
                d.panel('close');
            } else {
            	
            	var array;
    			if(data.indexOf("/") >= 0){
    				
    				array = data.split("/"); 
    				var mess = "EXCEL中第"+array[0]+"条数据的费目不存在或不是对应关系，"+array[1]+"条数据的联系单不存在或不是对应关系 ,无法导入!";
    	    		gridDG();
                    d.panel('close');
    			}else if(data.indexOf("a") >= 0){
    				
    				array = data.split("a");
    				var mess = "EXCEL中第"+array[0]+"条数据的费目不存在或不是对应关系，无法导入!";
    	    		gridDG();
                    d.panel('close');
    			}else if(data.indexOf("b") >= 0){
    				
    				array = data.split("b");
    				var mess = "EXCEL中第"+array[0]+"条数据的联系单不存在或不是对应关系，无法导入!";
    	    		gridDG();
                    d.panel('close');
    			}else{
    				var mess = "EXCEL无数据或值获取失败，导入过程存在异常，请检查确认!";
    	    		gridDG();
                    d.panel('close');
    			}

            }
        }
    });
</script>

</body>

</html>