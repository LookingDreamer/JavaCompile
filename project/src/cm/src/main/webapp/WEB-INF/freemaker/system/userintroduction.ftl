<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<link rel="stylesheet" type="text/css" media="screen" href="${staticRoot}/css/lib/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="${staticRoot}/css/lib/ui.jqgrid.css" />
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}">
</script>
<style> 
	.div-height{height:300px;} 
</style> 
</head>
<body>
	<div class="modal-header">
       <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
       <span class="modal-title" id="myModalLabel">个人资料</span>
	</div>
	<div class="modal-body">
		<div class="div-height">        
			<form action="/user/doedit" method="post">
			   	<table>
					<tr><td>姓名：</td><td>${insc_user.username!""}</td></tr>
					<tr><td>性别：</td><td><#if insc_user.sex=="0">男</#if><#if insc_user.sex=="1">女</#if></td></tr>
					<tr><td>电话：</td><td>${insc_user.phone!""}</td></tr>
				</table>
			</form>
		</div>
		<div class="modal-footer">
		  <button type="button" class="btn btn-primary">保存</button>
		  <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
		</div>
	</div>
</body>
</html>
