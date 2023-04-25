<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <%@ include file="/WEB-INF/views/include/easyui.jsp" %>
</head>
<style>
 .datagrid-cell {
 		font-size: 20px;font-family:'宋体';font-weight:bold;
 	}
  .datagrid-header .datagrid-cell span{
  		font-size: 22px;
  		line-height:40px;
  }
 .datagrid-row {
        height: 60px;
    }
.div{font-size:33px;font-weight:bold;} 
</style>
<div class="easyui-layout div" style="font-family:'微软雅黑';text-align:center;">青港物流冷链中心车辆调度平台</div> 
<table id="dg" style="height:450px;"></table>
<div id="dlg"></div>
<div id="inn" align="center"></div>
<div id="ti" align="center"></div>
<div id="v3" ></div>
<script type="text/javascript">

    var dg;
    var dlg;
	var result=1;
	

    $(document).ready(function () {
        dg = $('#dg').datagrid({
            method: "get",
            url: '${ctx}/wms/gate/fullJson',
            fit: false,
            fitColumns: true,
            border: false,
            striped: true,
            pagination: true,
            rownumbers: true,
            pageNumber: 1,
            pageSize: 6,
            pageList: [6],
            singleSelect: true,
            columns: [[
                {field: 'id', title: 'id', hidden:true},
            	{field: 'carNum', title: '车牌号', sortable: true, width: 100},
                {field: 'driverName', title: '司机名', hidden: true, width: 100},
                {field: 'ctnNum', title: '箱号', sortable: true, width: 100},
                {field: 'platform', title: '月台口', sortable: true, width: 60},
                {field: 'platformtwo', title: '月台口2', sortable: true, width: 60},
                {field: 'bisType', title: '业务类型', sortable: true, width: 40,
	  	        	formatter : function(value, row, index) {
	 	       			if(value==1){
	 	       				return "入库";
	 	       			}else if(value == 2){
	 	       				return "出库";
	 	       			}else{
	 	       				return "无业务";
	 	       			}
	 	        	}
 	        	},
                {field: 'createDate', title: '入场日期', sortable: true, width: 170}
            ]],
            enableHeaderClickMenu: true,
            enableHeaderContextMenu: true,
            enableRowContextMenu: false,
            toolbar: '#tb'
        });
			setTimeout("flip()", 10000);
			setTimeout("callCar()", 10000);
    });

    function toast(msg) {
        parent.$.messager.show({title: "提示", msg: msg, position: "bottomRight"});
    }

	

	//翻页
	function flip(){
    	 var grid = $('#dg');  
		 var options = grid.datagrid('getPager').data("pagination").options;  
		 var total = options.total;  
		 var pageNum=Math.ceil(total/options.pageSize);
		 if(pageNum>1){
			 if(result<pageNum){
			 	result = result+1;
			 }else{
			 	result = 1;
			 }
			 $('#dg').datagrid({pageNumber: result});
		 }else{
		 	$('#dg').datagrid({pageNumber: result});
		 }
		 setTimeout("flip()", 10000);
	} 
	
	
	//呼叫车辆
	function callCar(){
		$.ajax({
            type: 'post',
            url: "${ctx}/wms/gate/callCarList",
            dataType: "json",
            success: function (data) {
            	console.log("callCarList="+data);
				if(data.length>0){
					var test=new Array();
					for(var i=0;i<data.length;i++){
						$.ajax({
							async:false,
				            type: 'post',
				            data: {carNum:data[i].carNum},
				            url: "${ctx}/wms/gate/reduce",
				            success: function (data) {
				            
				            }
				        });
						var zhText ="请车号为" + data[i].carNum + "的车辆到" + data[i].platform + "月台口停靠" ; 
        				/* var tv = "<audio autoplay=\"autoplay\">" +
           						 "<source src=\"http://tts.baidu.com/text2audio?lan=zh&ie=UTF-8&spd=2&text=" + zhText + "\" type=\"audio/mpeg\">" +
            					 "<embed height=\"0\" width=\"0\" src=\"http://tts.baidu.com/text2audio?lan=zh&ie=UTF-8&spd=2&text=" + zhText + "\">" +
                                 "</audio>"; */
                    	 var tv = "<audio autoplay=\"autoplay\">" +
   						 "<source src=\"https://tsn.baidu.com/text2audio?tex=" + zhText + "&lan=zh&spd=2&cuid=30-9C-23-16-06-13&ctp=1&tok=24.4f08a552035b52e26527751b9d18af86.2592000.1534300721.282335-11539613\" type=\"audio/mpeg\">" +
     					 "<embed height=\"0\" width=\"0\" src=\"https://tsn.baidu.com/text2audio?tex=" + zhText + "&lan=zh&spd=2&cuid=30-9C-23-16-06-13&ctp=1&tok=24.4f08a552035b52e26527751b9d18af86.2592000.1534300721.282335-11539613\">" +
                         "</audio>";
        			    $("#v3").append(tv);
        			    sleep(10000);
					}
					setTimeout("callCar()", 5000);
				}else{
					setTimeout("callCar()", 10000);
				}
            }
        });
	}
	
	//停顿几秒
	function sleep(numberMillis) {
	    var now = new Date();
	    var exitTime = now.getTime() + numberMillis;
	    while (true) {
	        now = new Date();
	        if (now.getTime() > exitTime)
	            return;
	    }
	}
	
	
	
	//时钟
	function time1(){
          var inn=document.getElementById('inn');
          var ti=document.getElementById('ti');
         var date = new Date();
         var month = date.getMonth()+1;
          var year=date.getFullYear();
         var day=date.getDate();
          var week = date.getDay();
          var hour = date.getHours();
         var min = date.getMinutes();
          var sec = date.getSeconds();
          var week1;
          switch(week)
          {
              case 0: week1='星期日'; break;
              case 1: week1='星期一'; break;
              case 2: week1='星期二'; break;
              case 3: week1='星期三'; break;
              case 4: week1='星期四'; break;
              case 5: week1='星期五'; break;
              case 6: week1='星期六'; break;
          }
          inn.innerHTML=year+'年'+month+'月'+day+'日'+'    '+week1;
          ti.innerHTML= hour+':'+min+':'+sec;
          var innn=inn.innerHTML;
          setTimeout(time1,1000);
      }
      time1();
	
</script>

</body>

</html>