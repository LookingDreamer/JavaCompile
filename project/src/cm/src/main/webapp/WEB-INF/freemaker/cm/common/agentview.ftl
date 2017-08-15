<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8"/>
<title>文件上传</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${staticRoot}/css/appmain.css" rel="stylesheet">
<link href="${staticRoot}/css/skin/default.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/fileinput.css" media="all" rel="stylesheet" type="text/css" />
<style type="text/css">
body {font-size: 14px;}
.m-left-5{margin-left:13px;}
</style>
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "fileinput", "fileinputi18n", "flat-ui", "audioplayer","mousewheel","public"], function ($) {
	$(function(){
		 $( "#imgView" ).draggable();
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
		});
		$("#nextpic").on("click", nextOne());
		$("#lastpic").on("click", lastOne());
		$("#turnLeft").on("click", turnLeft());
		$("#turnRight").on("click", turnRight());
	})
});
var deg = 0;
var temp = 1;
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
		var next = now + 1;
		$("#indeximgarray").val(next);
		$("#originalimg"+next).show();
		$("#originalimgname"+next).show();
	}
	initsize();
}
function lastOne(){//上一个
	var now = parseInt($("#indeximgarray").val());
	if(now==0){
		
	}else{
		$("#originalimg"+now).hide();
		$("#originalimgname"+now).hide();
		var next = now - 1;
		$("#indeximgarray").val(next);
		$("#originalimg"+next).show();
		$("#originalimgname"+next).show();
	}
	initsize();
}
function zoom(delta){//缩放
	var now = parseInt($("#indeximgarray").val());
	if(delta>0){
		if(temp<4.5)
		temp = parseFloat(temp)+0.5;
	}else{
		if(temp>0.5)
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
function databack(data){
	var tempstr = "<a class=\"btn btn-success imgbtn\" href='javascript:window.parent.openDialogForCM(\"common/insurancepolicyinfo/addImageDialog2)'><span class=\"glyphicon glyphicon-picture\" aria-hidden=\"true\"></span>&nbsp;添加影像</a>";
	for (var i = 0; i < data.imageList.length; i++) {
		var item = data.imageList[i];
		//tempstr += "<div class=\"col-xs-6 col-sm-3 col-md-2 col-lg-2\"><div class=\"thumbnail\">";
		tempstr += "<div style=\"width: 100%;  text-align:center; vertical-align:middle;line-height:100%; border: 0px solid #ccc\">";
		tempstr += "<a href=\'javascript:window.parent.openImgView(\"common/insurancepolicyinfo/previewPicture2)\'>";
		tempstr += "<img style=\"width: 120px; height: 120px; text-align:center; vertical-align:middle;line-height:120px; border: 1px solid #ccc\" src="+item.filepath+"  width=\"130px\" height=\"130px\">";
		tempstr += "</a></div><div class=\"caption\" style=\"text-align:center;\"><p>";
		tempstr += "<a href=\'javascript:window.parent.openImgView(\"common/insurancepolicyinfo/previewPicture2)\'>";
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
</script>
</head>
<body scroll="no">
<div class="modal-header">
      <button type="button" id="closemodel" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
      <#list imageList as image>
			<#if '${index}'=='${image_index}'>
      			<span class="modal-title" id="originalimgname${image_index}">${image.filedescribe}</span>
     		<#else>
      			<span class="modal-title" id="originalimgname${image_index}" style="display:none;">${image.filedescribe}</span>
     		</#if>
     	</#list>
</div>
<div class="container-fluid">
	<div id="myTabContent" class="tab-content">
		<div class="tab-pane fade  in active" id="original" style="height: 400px; text-align: center;overflow:scroll;text-align:center;vertical-align:middle;">
				<#list imageList as list>
					<#if '${index}'=='${list_index}'> 
					   	<img id="originalimg${list_index}" class="originalimg" src="${list.filepath}" >
					<#else>
					   	<img id="originalimg${list_index}" class="originalimg" style="display:none;" src="${list.filepath}">
					</#if>
					<input id="img${list_index}" type="hidden" value="${list.id}"/>
				</#list>
		</div>
	</div>   
	<div class="modal-footer">
		<div class="caption" style="text-align:center;">
			<input id="indeximgarray" type="hidden" value="${index}">
			<input id="imglistsize" type="hidden" value="${imageList?size}">
			<a href="#" onclick="zoom(1);"><i id="zoomin" class="glyphicon glyphicon-zoom-in m-left-5" ></i></a>
			<a href="#" onclick="zoom(-1);"><i id="zoomout" class="glyphicon glyphicon-zoom-out m-left-5"></i></a>
			<a href="#" onclick="lastOne();"><i id="lastpic" class="glyphicon glyphicon-arrow-left m-left-5" ></i></a>
			<a href="#" onclick="nextOne();"><i id="nextpic" class="glyphicon glyphicon-arrow-right m-left-5"></i></a>
			<a href="#" onclick="turnLeft();"><i id="turnLeft" class="glyphicon glyphicon-repeat m-left-5"></i></a>
			<a href="#" onclick="turnRight();"><i id="turnRight" class="glyphicon glyphicon-repeat m-left-5" style="transform: rotateY(180deg);"></i></a>
		</div>
	</div>
</div>
</body>
</html>