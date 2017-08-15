<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${staticRoot}/css/appmain.css" rel="stylesheet">
<link href="${staticRoot}/css/skin/default.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<title>访问错误</title>
<style type="text/css">
.modal-header {
	display: none;
}
h4 {font-size: 14px;}
#xDialog .modal-header {
	display: block;
}
</style>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<h4 class="modal-title">系统错误</h4>
	</div>
	<div class="row" style="text-align: center;">
		<h3>系统错误</h3>
	</div>
	<div class="row">
		<p>${message!""}</p>
	</div>

</body>
</html>
