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
      <button type="button" id="closemodel" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
      <span class="modal-title" id="myModalLabel">视频预览</span>
</div>
<div class="container-fluid">
<div class="row">
	<div class="tab-pane fade  in active" id="original" align="center">
		<video id="myVideo" src="${videopath}" type="video/${videotype}" controls="controls" poster="http://video-js.zencoder.com/oceans-clip.png" height="100%" width="100%">

		</video>
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
	var video=document.getElementById("myVideo");
	video.pause();
	video.currentTime = 0.0;
	//alert("close");
	});
	
	$("#cancle").on("click", function(e) {
	var video=document.getElementById("myVideo");
	video.pause();           //暂停
	video.currentTime = 0.0; //播放时间设为0.0
	//alert("close");
	});
	
</script>

</html>