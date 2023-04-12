/**
 * 库位使用情况
 */ 
// 入口 StartShowFra(data); StartShowInfo
//区位框架
function StartShowFra(objBaseTray,width) { 
	var Allheight = 30;//第一行高+数据行高
	var Allwidth = 80;//第一列+最后一列 + 数据行列宽
	$("#showtray").empty();
	$("#showtray").attr("style","text-align: center; padding: 5px; height: 1px;width:1px;");	 
	var eaci = 0;
	var maxA;var minA ; var iWith;var iHEGIHT;
	$(objBaseTray).each(function() {
				// MAXARE,IWITH,IHEGIHT,STOREROOM_NUM, BUILDING_NUM,MINARE, Floor_Num, ACCNUM 
				if (eaci == 0) {
					 maxA = this.MAXARE;
					 minA = this.MINARE;
					 iWith = this.IWITH;
					 iHEGIHT = this.IHEGIHT;
					 Allheight=iHEGIHT*14+Allheight; //Math.floor
					 Allwidth=iWith*16+Allwidth;
				}
				var floNum= this.FLOOR_NUM;//楼层号
				var sNum = this.STOREROOM_NUM; //第一列库房号
				var bNum = this.BUILDING_NUM;
				var accNum = this.ACCNUM;//最后一列占位
				var Fli="";
				if(eaci==0)
				{  
					$("#showtray").attr('style','text-align: center; padding: 5px; height:'+Allheight+'px;width:'+Allwidth+'px');	
					Fli='<ul id="ul1"><li style="height:20px; width:30px;" >房间</li>';	 
					for(i=1;i<=iWith;i++){
						var b =String((parseInt(minA)+(i-1)));
						Fli=Fli+'<li style="height:20px; width:14px;" >'+GetStr(b,2)+'</li>';
					}
					Fli=Fli+'<li style="height:20px; width:30px;" >占位<li/></ul>';
				}					
				Fli=Fli+'<ul id="'+sNum+'"><li id="'+sNum+'ls" style="height:14px; width:30px;" >'+sNum+'</li>';	 
				for(i=1;i<=iWith;i++){
					//仓库号 楼号+楼层-房间号-区域号
					var a=(bNum=="0"?"":bNum)+parseInt(floNum).toString()+'_';  
					a=a+GetStr(sNum.substr(1),2)+"_"; 
					a=a+GetStr(String(parseInt(minA)+(i-1)),3);
					Fli=Fli+ '<li id="'+a+'" style="height:14px; width:14px;" class="bai"></li>';
				}
				Fli=Fli+'<li id="'+sNum+'le" style="height:14px; width:30px;" >'+accNum.toString()+'</li></ul>'; 				
				$(Fli).appendTo($("#showtray"));
				eaci=1; 
			});
	}

//区位 存储情况
function StartShowInfo(objTrayInfo)
{ 
	 // 画第一行 从 最小 到 最大
	if (objTrayInfo != null&& jQuery.trim(objTrayInfo.toString()).length > 0) { 
		$(objTrayInfo).each(function() {
			// BUILDING_NUM, CARGO_LOCATION, FLOOR_NUM, ROOM_NUM, AREA_NUM, STOREROOM_NUM
			var ibNum = this.BUILDING_NUM;
			var ical = this.CARGO_LOCATION;
			var iflo = this.FLOOR_NUM;
			var iroo = this.ROOM_NUM;
			var isto = this.STOREROOM_NUM;
			var aren=this.AREA_NUM;
			//仓库号 楼号+楼层-房间号-区域号
			var a=(ibNum=="0"?"":ibNum)+iflo.toString()+'_'; //楼+楼层
			a=a+GetStr(iroo.toString(),2)+'_';//房间
			a=a+GetStr(aren.toString(),3); //区位号
			// 查询 并替换背景色 
			var aa =$('#'+a);
			$('#'+a).attr('class','lan'); 
		}); 
	  }  
}

//库位空框架
function DrawingStoreF(objBaseTray,width)
{ 
	 $("#showtray").empty();
     $("#showtray").attr("style","padding: 5px; height: auto;");	 
	var builnum; //BUILDING_NUM ,FLOOR_NUM,ROOM_NUM,STOREROOM_NUM,FWIDTH,MFWIDTH[最大宽度],CLASSCOU --,FHEGITH
	var floornum;
	var roomnum;
	var storenum;//库房号
	var fwidth;//每一楼的库房数 	//var fhegith = this.FHEGITH;//楼层数据 用于计算高度
	var mfwidth = this.MFWIDTH;//最大库房数据 （层） 
	var classcou= this.CLASSCOU;//库房
	var strhtml='';
	var ri=0;
	var jlcount=0;
	$(objBaseTray).each(function() {
		if(ri!=0&&this.FLOOR_NUM!=floornum)
		{  
			var w=GetNullTd(this.MFWIDTH,jlcount); 
			strhtml=strhtml+w+'</tr><tr><th colspan=\"'+(this.MFWIDTH*2).toString()+'\" style=\"text-align:left;\" >穿堂</th></tr>';
			jlcount=0;
		} 
		 floornum = this.FLOOR_NUM;
		 roomnum = this.ROOM_NUM;  
		 mfwidth = this.MFWIDTH;//最大库房数据 （层）  
		 var kfh = floornum.toString()+GetStr(roomnum.toString(),2);
		if(jlcount==0)  
			strhtml=strhtml+'<tr id=\"tr'+this.FLOOR_NUM.toString()+'\"><td>'+kfh+'<br/>-18</td><td id ='+kfh+'></td>';  
		else 
			strhtml=strhtml+'<td>'+kfh+'<br/>-18</td><td id ='+kfh+'></td>';  
		jlcount++;
		ri=1;
	});
	if(jQuery.trim(strhtml).length>0)
		strhtml=strhtml+GetNullTd(mfwidth,jlcount)+'</tr>';
	strhtml='<table id=\"tabletrayf\" class=\"gridtable\">'+strhtml+'</table>';
	$(strhtml).appendTo($("#showtray"));
}
//库位货物存储统计
function DrawingStoreInfo(objTrayInfo) {
	var allsum=0;	var prosum=0;	
	var sotrnum='';var clatype; var claname; var dm;	var dj; var isxjm; //var clacou		//BUILDING_NUM , STOREROOM_NUM ,CLASS_TYPE,CLASS_NAME,DM,DJ ,ISXJ
	var i =0;
	var tdstr="";
	$(objTrayInfo).each(function() { 
		isxjm = this.ISXJ;
		if(jQuery.trim(sotrnum).length>0&&sotrnum!=this.STOREROOM_NUM){ 
			tdstr=tdstr+'<font style=\"font-weight:bold;\" >总合计</font>'+prosum.toFixed(3).toString();//入库长度不够补充br +GetNullBr(clacou,(i-1))
			$(tdstr).appendTo('#'+sotrnum.toString());
			prosum=0;tdstr='';i=0; 
		}
		sotrnum= this.STOREROOM_NUM;//更新后赋值
		var tdm=parseFloat(this.DM.toString()); 
		if(isxjm.toString().trim()=="1"){
			prosum=parseFloat(prosum.toString())+tdm; 
			allsum=parseFloat(allsum.toString())+tdm;
			var claname =this.CLASS_NAME.toString().indexOf('合计')>=0?('<font style=\"font-weight:bold;\" >'+this.CLASS_NAME+'</font>：'):this.CLASS_NAME;
			tdstr=tdstr+claname+':'+tdm.toString()+'<br/>'; 
		}
		i++; 
	});
	if(jQuery.trim(tdstr).length>0) {
		tdstr=tdstr+'<font style=\"font-weight:bold;\" >总合计</font>'+prosum.toFixed(3).toString();//入库长度不够补充br  GetNullBr(clacou,(i-1))+
		$(tdstr).appendTo($('#'+sotrnum.toString()));
	}
	$("#fl").empty(); 
	$('#fl').html('今日在库统计：'+allsum.toFixed(3).toString());//$( ).appendTo($('#zktj'));
}

function DrawingProductSum(dataf){  //foot 
	var i =0; var strhtml='';var strhtml1='';var clanamea=''; var sdm='/';
	$(dataf).each(function() {  
         i++; 
	     clanamea =this.PNAME.toString().indexOf('合计')>=0?('<font style=\"font-weight:bold;\" >'+this.PNAME.toString()+'</font>'):this.PNAME.toString();  
         sdm=this.DM!=null&&!isNaN(this.DM)?this.DM.toString():'/';
         if(this.PNAME.toString().indexOf('合计')>=0)
        	 strhtml1=strhtml1+clanamea+':'+sdm+'&nbsp;&nbsp;'; 
         else
             strhtml = strhtml+clanamea+':'+sdm+'&nbsp;&nbsp;'; 
         if(i%8==0)
             strhtml =strhtml +'<br/>';          
	}); 
	strhtml=strhtml+'<br/>'+strhtml1;
    $('#foot').empty();
    $('#foot').html(strhtml);
    //$(strhtml).appendTo('#foot');
}
function GetNullTd(mfwidth,jlcount)
{
	var w='';
    for (var i =0;i<(mfwidth-jlcount);i++) 
	   w=w.toString()+'<td></td><td></td>'; 
    return w;
}
function GetNullBr(m,j)
{
	var w=''; 
	if(m>1){
    for (var i =0;i<(m-j);i++) 
	   w=w.toString()+'<br/><br/>'; 
	}
    return w;
}
function GetStr(strh,strl)
{
	var strHao = strh.toString();
	strl=parseInt(strl);
	while (strHao.length < strl)
		strHao = "0" + strHao;
	return strHao;	
}

function xround(x, num){
    Math.round(x * Math.pow(10, num)) / Math.pow(10, num) ;
}
/*<div>
<ul>
  <li style="height:20px; width:40px;">房间</li>
  <li style="height:20px; width:20px;">01</li>
  <li style="height:20px; width:20px;">02</li>
  <li style="height:20px; width:20px;">03</li>
  <li style="height:20px; width:40px;">占位</li>
</ul>

<ul>
  <li style="height:20px; width:40px;">101</li>
  <li style="height:20px; width:20px; background-color:#0000ff;"></li>
  <li style="height:20px; width:20px;"></li>
  <li style="height:20px; width:20px; background-color:#0000ff;"></li>
  <li style="height:20px; width:40px;">37</li>
</ul>*/