<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机构管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<style type="text/css">
		.htmleaf-header{margin-bottom: 15px;font-family: "Segoe UI", "Lucida Grande", Helvetica, Arial, "Microsoft YaHei", FreeSans, Arimo, "Droid Sans", "wenquanyi micro hei", "Hiragino Sans GB", "Hiragino Sans GB W3", "FontAwesome", sans-serif;}
		.htmleaf-icon{color: #fff;}
	</style>
<script type="text/javascript">
	requirejs([ "zzbconf/organization" ]);
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">机构基本信息</div>
		  <div class="panel-body">
				<form class="form-inline" role="form" id="usersaveform" action="saveuser" method="post">
				<div class="row">
					<div class="form-group col-md-4">
						<input id="id" name="id" type="hidden" value="${user.id!''}"/>
						<input id="password" name="password" type="hidden" value="${user.password!''}"/>
						<input id="picurl" name="picurl" type="hidden" value="${user.picurl!''}"/>
						<input id="operator" name="operator" type="hidden" value="${user.operator!''}"/>
						<input id="noti" name="noti" type="hidden" value="${user.noti!''}"/>
						<label for="exampleInputCode">机构编码</label> 
						<input type="text" value="${user.usercode!''}" placeholder="">
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputName">机构简称</label> <input type="text" 
							 id="username" name="username" value="${user.username!''}" placeholder="">
					</div>
					<div class="form-group col-md-4">
						<label for="userorganization">地址</label> <input type="text"
							 id="userorganization" name="userorganization" value="${user.userorganization!''}" placeholder="">
					</div>
				</div>
				<div class="row">
					<div class="form-group col-md-4">
						<label for="phone">电话号码</label> <input type="text"
							class="form-control m-left-5" id="phone" name="phone" value="${user.phone!''}" placeholder="">
					</div>
					<div class="form-group col-md-4">
						<label for="email">邮政编码</label> <input type="text"
							 id="email" name="email" value="${user.email!''}" placeholder="">
					</div>
				</div>
				<div class="row">
					<div class="form-group col-md-4">
						<label for="address">地址</label> <input type="text"
							 id="address" name="address" value="${user.address!''}" placeholder="">
					</div>
				</div>
					<div class="form-group col-md-4">
						<button id="addbutton" type="button" name="addbutton" class="btn btn-default">保存</button>
						<button id="resetbutton" type="reset" name="resetbutton" class="btn btn-default">重置</button>
					</div>
				</form>
				<div id="msg" class="msg">
		
				</div>
			</div>
		</div>
	</div>
</body>
</html>
