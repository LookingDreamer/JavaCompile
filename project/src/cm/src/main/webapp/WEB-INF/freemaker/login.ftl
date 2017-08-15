<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${staticRoot}/css/applogin.css" rel="stylesheet">
<link href="${staticRoot}/favicon.ico" type="image/x-icon" rel="shortcut icon">
<title>掌中保运营管理系统-欢迎登录</title>
<script type="text/javascript">
	if (window.top.location != window.self.location) {
		window.top.location = window.self.location;
	}
</script>
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script src="${staticRoot}/js/lib/jquery.min.js"></script>
<script src="${staticRoot}/js/system/login.js"></script>
</head>
<body>
	<header>
		<div class="topLogo" title="掌中保运营管理系统"><label class="l1">掌中保运营</label><label class="l2">管理系统</label></div>
	</header>
	<section>
		<h2>欢迎登录</h2>
		<#if ismodifypwd == null || ismodifypwd == false>
			<div class="login" id="logindiv">
				<form id="loginform" method="POST" action="${webRoot}/login">
					<span id="userNameLab">登录账号</span> 
					<input id="username" class="logintext" name="username" placeholder="请输入用户名"/> <span id="errorMsg" style="color:red;"></span> 
					<input type="hidden" id="msg" value="${Session['SPRING_SECURITY_LAST_EXCEPTION']}"/>
					<span id="pwdLab">密码</span> 
					<input id="password" class="logintext pwd" name="password" type="text" placeholder="请输入密码" autocomplete="off" onfocus="this.type='password'"/>
					<input type="submit" id="loginBtn" onclick="return validateForm();" value="登录" />
					<div>
						<#if _csrf??><input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" /></#if>
					</div>
				</form>
				<div class="err">
					<#if error??>
					<p>用户名密码错误</p>
					</#if>
				</div>
			</div>
		<#else>
			<div id="updatepassworddiv" class="updatepassword">
				<form id="updatepasswordform">
					<h2><center><b><font color="#0066cb">修 改 密 码</font></b></center></h2>
					<span class="pwdLab"><b>账&nbsp;&nbsp;号：</b><input id="usercode" class="uppwdtext" type="text" name="usercode" value="${insc_user.usercode!""}" readonly="readonly"/></span>
					<br/>
					<span class="pwdLab"><b>旧 密 码：</b><input id="oldpwd" name="oldpwd" class="uppwdtext pwd" type="text"  placeholder="请输入旧密码" onfocus="this.type='password'"/></span> 
					<br/>
					<span class="pwdLab"><b>新  密  码：</b><input id="newpwd" name="newpwd" class="uppwdtext pwd" type="text"  placeholder="请输入新密码" onfocus="this.type='password'"/></span> 
					<br/>
					<span class="pwdLab"><b>确认密码：</b><input id="checknewpwd" name="checknewpwd" class="uppwdtext pwd" type="text" placeholder="请输入确认密码" onfocus="this.type='password'"/></span>
					<h1></h1>
					<span id="pwderrorMsg" style="color:red;"></span>
					<h1></h1>
					<input id="updatepasswordbutton" type="button" class="pwdBtn" name="updatepasswordbutton" value="确定" />
					<input id="returnlogin" type="button" class="pwdBtn" name="returnlogin" value="返回登录" />
				</form>
			</div>
		</#if>
	</section>
	
	

	<footer class="copyright">Copyright © 2015 保网公司版权所有<br/>版本号：${version}</footer>


</body>
</html>
