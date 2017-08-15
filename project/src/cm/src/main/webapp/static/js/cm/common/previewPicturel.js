require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "fileinput", "fileinputi18n", "flat-ui", "audioplayer","mousewheel","public"], function ($) {
	$(function(){
		// imgView 通过样式隐藏 不用拖动 
		 //$( "#imgView" ).draggable();
		 $( ".originalimg" ).draggable();
		$(document).keydown(function(event){
			if(event.keyCode=="37"){
				lastOne();
			}else if(event.keyCode=="39"){
				nextOne();
			}
		}); 
		initsize();
		$("[id^='originalimg']").each(function(){
			$(this).hover(function(){
				$(this).css("border","1px solid #ccc"); 
		       },function(){
			    $(this).css("border","0px solid #ccc");
		    });
		});
		$("[id^='thumbnailimg']").each(function(){
			$(this).hover(function(){
				$(this).css("border","1px solid #ccc"); 
		       },function(){
			    $(this).css("border","0px solid #ccc");
		    });

		});

		//绑定滚轮事件
		$("#original").on("mousewheel",function(event,delta) {
			var now = parseInt($("#indeximgarray").val());
			if(delta>0){
				if(temp<4.5)
				temp = parseFloat(temp)+0.5;
			}else{
				if(temp>0.5)
				temp = parseFloat(temp)-0.5;
			}
//			alert(martix);
//			var a = martix.substring(martix.indexOf('(')+1,martix.indexOf(','));
//			var b = martix.split(',')[1];
//			var c = martix.split(',')[2];
//			var d = martix.split(',')[3];
//			var e = martix.split(',')[4];
//			var f = martix.split(',')[5];
//			var x = 0;
//			var y = 0;
//			x = a*temp+c*temp;
//			y = b*temp+d*temp;
//			alert("matrix("+a+","+b+","+c+","+d+","+e+","+f);
//			$("#originalimg"+now).css("-moz-transform","matrix("+a+","+b+","+c+","+d+","+e+","+f);
//			$("#originalimg"+now).css("filter","progid:DXImageTransform.Microsoft.BasicImage(rotation=1)");
//			$("#originalimg"+now).css("-o-transform","matrix("+a+","+b+","+c+","+d+","+e+","+f);
//			$("#originalimg"+now).css("-webkit-transform","matrix("+a+","+b+","+c+","+d+","+e+","+f);
//			$("#originalimg"+now).css("transform","matrix("+a+","+b+","+c+","+d+","+e+","+f);
//			alert(martix);
//			alert("("+x+","+y+")");
			//var martix = $("#originalimg"+now).css("transform");
			var martix = null;
			if(clickcount==1){
				martix = "rotate(90deg)";
			}else if(clickcount==2){
				martix = "rotate(180deg)";
			}else if(clickcount==3){
				martix = "rotate(270deg)";
			}else{
				martix = "rotate(0deg)";
			}
			var x = "scale("+temp+") "+martix;
//			alert(x);
			$("#originalimg"+now).css("-moz-transform",x);
			$("#originalimg"+now).css("filter","progid:DXImageTransform.Microsoft.BasicImage(rotation=1)");
			$("#originalimg"+now).css("-o-transform",x);
			$("#originalimg"+now).css("-webkit-transform",x);
			$("#originalimg"+now).css("transform",x);
//			$("#originalimg"+now).css("-moz-transform","scale("+x+","+y+")");
//			$("#originalimg"+now).css("filter","progid:DXImageTransform.Microsoft.BasicImage(rotation=1)");
//			$("#originalimg"+now).css("-o-transform","scale("+x+","+y+")");
//			$("#originalimg"+now).css("-webkit-transform","scale("+x+","+y+")");
//			$("#originalimg"+now).css("transform","scale("+x+","+y+")");
		});
		$("#nextpic").on("click", nextOne());
		$("#lastpic").on("click", lastOne());
		$("#deleteimg").on("click", function(e) {
			if(confirm("确认要删除吗？")){
				var now = parseInt($("#indeximgarray").val());
				var listzise = parseInt($("#imglistsize").val());
				var reloadindex = "";
				if(now==listzise-1){
					reloadindex = 0; 
				}else{
					reloadindex = now + 1;
				}
				var reloadid = $("#originalimg"+reloadindex).attr("alt");
				var deleteid = $("#originalimg"+now).attr("alt");
			    window.fra_fileindex.deleteone(deleteid);
				window.fra_fileindex.previepic(reloadid);
				window.fra_fileindex.initDatas('img');
			}
		});
		$("#turnLeft").on("click", turnLeft());
		$("#turnRight").on("click", turnRight());
	})
});
var deg = 0;
var temp = 0;
var clickcount = 0
function turnLeft(){//左旋转
	var now = parseInt($("#indeximgarray").val());
	deg=parseInt(deg)+90;
	$("#originalimg"+now).css("-moz-transform","rotate("+deg+"deg)");
	$("#originalimg"+now).css("filter","progid:DXImageTransform.Microsoft.BasicImage(rotation=1)");
	$("#originalimg"+now).css("-o-transform","rotate("+deg+"deg)");
	$("#originalimg"+now).css("-webkit-transform","rotate("+deg+"deg)");
	$("#originalimg"+now).css("transform","rotate("+deg+"deg)");
	if(clickcount==3){
		clickcount = 0;
	}else{
		clickcount++;
	}
	
	//initsize();
}
function turnRight(){//右旋转
	var now = parseInt($("#indeximgarray").val());
	deg=parseInt(deg)-90;
	$("#originalimg"+now).css("-moz-transform","rotate("+deg+"deg)");
	$("#originalimg"+now).css("filter","progid:DXImageTransform.Microsoft.BasicImage(rotation=1)");
	$("#originalimg"+now).css("-o-transform","rotate("+deg+"deg)");
	$("#originalimg"+now).css("-webkit-transform","rotate("+deg+"deg)");
	$("#originalimg"+now).css("transform","rotate("+deg+"deg)");
	if(clickcount==0){
		clickcount =3;
	}else{
		clickcount--;
	}
	//initsize();
}
function nextOne(){//下一个
	var now = parseInt($("#indeximgarray").val());
	var listzise = parseInt($("#imglistsize").val());
	if(now==listzise-1){
		
	}else{
		$("#originalimg"+now).hide();
		$("#originalimgname"+now).hide();
		//$("#thumbnailimg"+now).hide();
		var next = now + 1;
		$("#indeximgarray").val(next);
		$("#originalimg"+next).show();
		$("#originalimgname"+next).show();
		//$("#thumbnailimg"+next).show();
		initsize();
	}
	initsize();
}
function lastOne(){//上一个
	var now = parseInt($("#indeximgarray").val());
	if(now==0){
		
	}else{
		$("#originalimg"+now).hide();
		$("#originalimgname"+now).hide();
		//$("#thumbnailimg"+now).hide();
		var next = now - 1;
		$("#indeximgarray").val(next);
		$("#originalimg"+next).show();
		$("#originalimgname"+next).show();
		//$("#thumbnailimg"+next).show();
	}
	initsize();
}
function zoom(delta){//缩放
	var now = parseInt($("#indeximgarray").val());
	if(delta>0){
		temp = parseFloat(temp)+0.5;
	}else{
		temp = parseFloat(temp)-0.5;
	}
	var martix = null;
	if(clickcount==1){
		martix = "rotate(90deg)";
	}else if(clickcount==2){
		martix = "rotate(180deg)";
	}else if(clickcount==3){
		martix = "rotate(270deg)";
	}else{
		martix = "rotate(0deg)";
	}
	var x = "scale("+temp+") "+martix;
	$("#originalimg"+now).css("-moz-transform",x);
	$("#originalimg"+now).css("filter","progid:DXImageTransform.Microsoft.BasicImage(rotation=1)");
	$("#originalimg"+now).css("-o-transform",x);
	$("#originalimg"+now).css("-webkit-transform",x);
	$("#originalimg"+now).css("transform",x);
	initsize();
    return false;
}
function deleteimg(){
	if(confirm("确认要删除吗？")){
		var now = parseInt($("#indeximgarray").val());
		var imgid = $("#img"+now).val();
		//var imgid = $(this).attr("title");
		if(typeof(imgid) == "undefined" || imgid=='' || imgid=='null'){
			return;
		}else{
			$.ajax({
				url:"/cm/common/insurancepolicyinfo/deleteimg",
				data:{
					"imgid":imgid,
					"taskid":$("#taskid").val(),
					"subInstanceId":$("#subInstanceId").val()
				},
				type:'POST',
				async:false,
				dataType:'json',
				success:function(data){
					//var temp = eval(data);
					//alertmsg(data);
					//alertmsg(imglist.length);
					alertmsg("删除成功");
					databack(data);
					
				}
			});
		}
	 }
}
function databack(data){
	var tempstr = "<a class=\"btn btn-success imgbtn\" href='javascript:window.parent.openDialogForCM(\"common/insurancepolicyinfo/addImageDialog?subInstanceId="+data.subInstanceId+"&taskid="+data.taskid+"\")'><span class=\"glyphicon glyphicon-picture\" aria-hidden=\"true\"></span>&nbsp;添加影像</a>";
	for (var i = 0; i < data.imageList.length; i++) {
		var item = data.imageList[i];
		//tempstr += "<div class=\"col-xs-6 col-sm-3 col-md-2 col-lg-2\"><div class=\"thumbnail\">";
		tempstr += "<div style=\"width: 100%;  text-align:center; vertical-align:middle;line-height:100%; border: 0px solid #ccc\">";
		tempstr += "<a href=\'javascript:window.parent.openImgView(\"common/insurancepolicyinfo/previewPicture?pictureId="+item.id+"&subInstanceId="+data.subInstanceId+"&taskid="+data.taskid+"\")\'>";
		tempstr += "<img style=\"width: 120px; height: 120px; text-align:center; vertical-align:middle;line-height:120px; border: 1px solid #ccc\" src="+item.filepath+"  width=\"130px\" height=\"130px\">";
		tempstr += "</a></div><div class=\"caption\" style=\"text-align:center;\"><p>";
		tempstr += "<a href=\'javascript:window.parent.openImgView(\"common/insurancepolicyinfo/previewPicture?pictureId="+item.id+"&subInstanceId="+data.subInstanceId+"&taskid="+data.taskid+"\")\'>";
		//tempstr += item.filedescribe;
		tempstr += "</a></p></div>";
    }
	var win = null;
	if($(window.top.document).find("#menu").css("display")=="none"){
		win = $(window.frames[1].document);
	}else{
		win = $(window.frames[2].document);
	}
	win.find(".imglist").empty();
	win.find(".imglist").append(tempstr);
	closepic();
}
function closepic(){
	$("#imgView").hide();
}
function initsize(){
	var ind = parseInt($("#indeximgarray").val());
	var screenImage = $("#originalimg"+ind);
	var theImage = new Image();
	theImage.src = screenImage.attr("src");
	var imageWidth = theImage.width;
	var imageHeight = theImage.height;
	if(imageWidth>390&&imageHeight<300){
		screenImage.css({"width":"390", "height":"\"" + imageHeight + "\""});
	}else if(imageWidth<390&&imageHeight>300){
		screenImage.css({"width":"\"" + imageWidth + "\"", "height":"300"});
	}else if(imageWidth>390&&imageHeight>300){
		screenImage.css({"width":"390", "height":"300"});
	}else{
	}
	var listzise = parseInt($("#imglistsize").val());
	if(listzise-1==ind){
		$("#nextone").removeClass().addClass("btn");
	}else{
		$("#nextone").removeClass().addClass("btn btn-warning");
	}
	if(ind==0){
		$("#lastone").removeClass().addClass("btn");
	}else{
		$("#lastone").removeClass().addClass("btn btn-warning");
	}
}