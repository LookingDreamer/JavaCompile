<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8"/>
<title>播放视频</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${staticRoot}/css/lib/bootstrap.min.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/flat-ui.min.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
</head>
<body>
<div class="modal-header">
      <button type="button" id="closemodel" class="close" data-dismiss="modal"><span aria-hidden="false">&times;</span><span class="sr-only">Close</span></button>
      <span class="modal-title" id="myModalLabel">音频播放</span>
</div>
<div class="container-fluid">
<div class="row">
	<div class="tab-pane fade  in active" id="original" align="center" >
		<audio id="myAudio" src="${audiopath}" type="audio/${audiotype}" preload="auto" controls="controls" autoplay="autoplay" loop="loop" style="width: 100%;height: 100%">
		    <!-- <source src="${audiopath}" type="audio/${audiotype}"/>-->
		</audio>
	</div>
	<div class="modal-footer">
		<a id="cancle" class="btn btn-danger" data-dismiss="modal">返回</a>
	</div>	
</div>
</div>
</body>
<script type="text/javascript">
		requirejs([ "system/previewpicture" ]);
</script>
<script type="text/javascript">
	$("#closemodel").on("click", function(e) {
	var audio=document.getElementById("myAudio");
	audio.pause();
	audio.currentTime = 0.0;
	//alert("close");
	});
	
	$("#cancle").on("click", function(e) {
	var audio=document.getElementById("myAudio");
	audio.pause();
	audio.currentTime = 0.0;
	//alert("close");
	});
	
	
</script>

</html>