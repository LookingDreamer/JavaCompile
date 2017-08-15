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
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
</head>
<body>
<div class="modal-header">
      <button type="button" id="closemodel" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
      <span class="modal-title" id="myModalLabel">图片预览</span>
</div>
<div class="container-fluid">
<div class="row">
	<ul id="myTab" class="nav nav-tabs" style="display: block">
   <li class="active"><a href="#original" data-toggle="tab" id="originalsize"></a>
   </li>
   <li><a href="#thumbnail" data-toggle="tab">缩略图(240X180)</a>
   </li>
</ul>
	<div id="myTabContent" class="tab-content" style="/*height: 300px*/">
		<div class="tab-pane fade  in active" id="original" align="center">
			<div id="originaldiv" style="width:300px;height:300px;text-align:center;vertical-align:middle;line-height:300px;border:0px solid #ccc;display:table-cell">
				<#list imgslist as list>
					<#if '${index!""}'=='${list_index}'>
						   	<img id="originalimg${list_index}" title="${list.filedescribe}" src="${list.filepath}" alt="${list.id}">
					<#else>
						   	<img id="originalimg${list_index}" title="${list.filedescribe}" src="${list.filepath}" alt="${list.id}" style="display:none">
					</#if>
				</#list>
			</div>
		</div>
			
		<div class="tab-pane fade" id="thumbnail" align="center">
		  	<div id="originaldiv" style="width:300px;height:300px;text-align:center;vertical-align:middle;line-height:300px;border:0px solid #ccc;display:table-cell">
		  		<#list imgslist as list>
					<#if '${index!""}'=='${list_index}'>
						   	<img id="thumbnailimg${list_index}" title="${list.filedescribe}" src="${list.filepath}" alt="${list.id}" width="240" height="180">
					<#else>
						 	<img id="thumbnailimg${list_index}" title="${list.filedescribe}" src="${list.filepath}" alt="${list.id}" width="240" height="180" style="display:none">
					</#if>
				</#list>
			</div>
		</div>
		<#list imgslist as list>
			<#if '${index!""}'=='${list_index}'>
				<table class="table" id="describe${list_index}">
					<tr >
						<td>图片名称：${list.filename}</td>
					</tr>
					<tr >
						<td>图片描述：${list.filedescribe}</td>
					</tr>
				</table>
			<#else>
				<table class="table" id="describe${list_index}" style="display:none">
					<tr >
						<td>图片名称：${list.filename}</td>
					</tr>
					<tr >
						<td>图片描述：${list.filedescribe}</td>
					</tr>
				</table>
			</#if>
		</#list>
	</div>
	<div class="modal-footer">
		<input id="indeximgarray" type="hidden" value="${index}">
		<input id="imglistsize" type="hidden" value="${imgslist?size}">
		<a id="deleteimg" class="btn btn-primary">删除</a>
		<a id="lastone" class="btn btn-warning">上一张</a>
		<a id="nextone" class="btn btn-warning">下一张</a>
		<a id="cancle" class="btn btn-danger" data-dismiss="modal">返回</a>
	</div>	
</div>
</div>
</body>
<script type="text/javascript">
		requirejs([ "system/previewpicture" ]);
</script>
<script type="text/javascript">
$(document).ready(function(){
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
	
$("#nextone").on("click", function(e) {
	var now = parseInt($("#indeximgarray").val());
	var listzise = parseInt($("#imglistsize").val());
	if(now==listzise-1){
		
	}else{
		$("#originalimg"+now).hide();
		$("#thumbnailimg"+now).hide();
		$("#describe"+now).hide();
		var next = now + 1;
		$("#indeximgarray").val(next);
		$("#originalimg"+next).show();
		$("#thumbnailimg"+next).show();
		$("#describe"+next).show();
		initsize();
	}
});

$("#lastone").on("click", function(e) {
	var now = parseInt($("#indeximgarray").val());
	if(now==0){
		
	}else{
		$("#originalimg"+now).hide();
		$("#thumbnailimg"+now).hide();
		$("#describe"+now).hide();
		var next = now - 1;
		$("#indeximgarray").val(next);
		$("#originalimg"+next).show();
		$("#thumbnailimg"+next).show();
		$("#describe"+next).show();
		initsize();
	}
});

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

function initsize(){
	var ind = parseInt($("#indeximgarray").val());
	var screenImage = $("#originalimg"+ind);
	var theImage = new Image();
	theImage.src = screenImage.attr("src");
	var imageWidth = theImage.width;
	var imageHeight = theImage.height;
	if(imageWidth>390&&imageHeight<300){
		$("#originalsize").text("原图(" + imageWidth + "X" + imageHeight + ")|显示(" + 390 + "X" + imageHeight + ")");
		screenImage.css({"width":"390", "height":"\"" + imageHeight + "\""});
	}else if(imageWidth<390&&imageHeight>300){
		$("#originalsize").text("原图(" + imageWidth + "X" + imageHeight + ")|显示(" + imageWidth + "X" + 300 + ")");
		screenImage.css({"width":"\"" + imageWidth + "\"", "height":"300"});
	}else if(imageWidth>390&&imageHeight>300){
		$("#originalsize").text("原图(" + imageWidth + "X" + imageHeight + ")|显示(" + 390 + "X" + 300 + ")");
		screenImage.css({"width":"390", "height":"300"});
	}else{
		$("#originalsize").text("原图(" + imageWidth + "X" + imageHeight + ")");
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
})
</script>
</html>