<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<link rel="stylesheet" type="text/css" media="screen" href="../static/css/lib/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../static/css/lib/ui.jqgrid.css" />
<script data-ver="${jsver}" data-main="../static/js/load"
	src="../static/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "zzbconf/demo" ]);
</script>
<body>
	<form id="loginform" method="POST" action="${webRoot}/demo/demotest">
				<span id="userNameLab">用户名：</span> <input id="username"
					name="name" placeholder="请输入用户名" value="u0001" /> <span
					id="pwdLab">密&nbsp;&nbsp;码：</span> <input id="password"
					name="id" placeholder="请输入ID" value="123456" />
				<input type="submit" id="loginBtn" onclick="return validateForm();"
					value="登录" />
			</form>
    
</body>
</html>
