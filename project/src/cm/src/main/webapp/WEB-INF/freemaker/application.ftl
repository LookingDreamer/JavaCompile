<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- mobile not available <meta name="viewport" content="width=device-width, initial-scale=1.0"> -->
<link href="${staticRoot}/css/appmain.css" rel="stylesheet">
<link href="${staticRoot}/css/skin/default.css" rel="stylesheet">
<link href="${staticRoot}/favicon.ico" type="image/x-icon"
	rel="shortcut icon">
<script src="${staticRoot}/js/system/socket.io.js"></script>
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>


<title>掌中保运营管理系统</title>
<script type="text/javascript">
	requirejs([ "system/main","system/msg" ]);
</script>
<style type="text/css">
.taskmsg{ 
	text-align:center;
	margin:0 auto;
	width:266px;
	height:38px
} 
</style>
</head>
<body>
	<nav id="apphead"
		class="navbar navbar-inverse navbar-embossed navbar-fixed-top"
		role="navigation">
		<div class="navbar-left">
			<div class="head-logo" >
				<img src="static/images/system/logo2.png" /><span id="head-title">运营管理 </span>
				<span id="head_show">
					<a class="head-title-a" href="javascript:desktop();">
					<span class="fui-export"></span>
						 <span id="head-title-s">切换至工作桌面</span>
					</a>
				</span>
				<!--  <a id="newAinfo" color="red" href="javascript:$.cmTaskList('my', 'list4deal', true);">
					<span id="newAinfoli" style="display: none;" >您有新任务</span>
				</a>-->
				<a id="findbc"  color="white" href="#" onclick="bitclick();">
						<span id="newAinfoli" style="display: none;" class="fui-export cus-icon" >您有新任务</span>
				</a>
				
			</div>
		</div>
		<div  id="settings"  style="display:none;z-index:9999">
			<ul><li></li><a href="javascript:openDialog('user/updatepassword');">设置最大任务数 </a></li></ul>
		</div>
		<!-- 新任务提示 -->
		<div id="taskmsg" class="taskmsg navbar-fixed-top" style="display:none"><img src="${staticRoot}/images/system/taskmsg.png"/></div>
		<div class="navbar-right">
			<div class="welcome">
				
				<div class="btn-group userLogin-drop">
				  <button id="dropdown-toggle-btn" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				    <span class="fui-user cus-icon" id="incondisplay"></span> 
				    <span class="top_user" id="usernamedisplay">${insc_user.name}</span>
				    <span id="incondisplaydown" class="glyphicon glyphicon-menu-down"></span>
				  </button>
				  <ul class="dropdown-menu">
				    <li status="online"><a href="javascrip:void(0)"><span class="fui-user cus-icon online"></span>在线</a></li>
				    <li status="busy"><a href="javascrip:void(0)"><span class="fui-user cus-icon busy"></span>忙碌</a></li>
				    <li status="offline"><a href="javascrip:void(0)"><span class="fui-user cus-icon offline"></span>离线</a></li>
				  </ul>
				</div>
			&nbsp;&nbsp;&nbsp;&nbsp;${org}</div>
			<ul>
				<li><a id="im_logut" href="user/logout"><span class="fui-power cus-icon"></span>退出</a></li>
				<li> 
					<a href="javascript:openDialog('user/updatetasks');">设置任务数</a>
				</li>				
				<li><a href="javascript:openDialog('user/updatepassword');"><span class="fui-lock cus-icon" id="setting"></span>修改密码 </a></li>
				<li><a id ="im_message" 
					href="javascript:openDialog('user/message/${insc_user.usercode}');">
					<input type="hidden" value="${insc_user.usercode}" id="receiver" >
						<span class="fui-bubble cus-icon" id="messageNotReadCount">0</span>消息
						<!-- <span
						class="navbar-unread cus-navbar-unread">3</span> -->
						
				</a></li>
					<li class="head-task">
					<span id="top_menu1" style="display: none;">
					<a href="javascript:$.cmTaskList('management', 'list', true);">
						<span class="fui-calendar-solid cus-icon"></span>任务管理
					</a>
					</span>
				</li>
				<li class="head-task">
				<span id="top_menu3" style="display: none;">
				<a href="javascript:$.cmTaskList('share', 'list', true);">
						<span class="fui-eye cus-icon"></span>任务池
				</a>
				</span>
				</li>
				<li class="head-task">
				<span id="top_menu2" style="display: none;">
				<a id="im_mytask" href="javascript:$.cmTaskList('my', 'nothing', true);">
						<span class="fui-heart cus-icon"></span>我的任务
				</a>
				</span>
				</li>
				<li><button type="button" class="navbar-toggle"
						data-toggle="collapse" data-target="#appmenu">
						功能导航
					</button></li>
			</ul>
		</div>
	</nav>
	<div class="container-fluid appmid-content" id="menu">
		<div class="row">
			<div class="collapse navbar-collapse" id="appmenu">
				<nav class="navbar navbar-inverse navbar-embossed" role="navigation">
					${menu!""}</nav>
			</div>
			<div id="appcontent" class="col-xs-12 col-sm-9">
				<div class="menutitle" id="showmenu">
					<label class="m-left-10" id="showtitle"></label>
				</div>
				<!-- iframe -->
			</div>
		</div>
	</div>
	<div class="container-fluid appmid-content" id="desktop"
		style="display: none;">
		<iframe id="desktop_tasks" name="desktop_tasks" src="about:blank" style="width:100%; height:100%;"></iframe>
		<iframe id="desktop_content" name="desktop_content" src="about:blank" style="width:80%; height:100%;display: none;"></iframe>
	</div>
	<nav id="apptask"
		class="navbar navbar-inverse navbar-embossed navbar-fixed-bottom"
		role="navigation">
		<ul id="div_tab" class="nav navbar-nav navbar-left"></ul>
		<ul class="nav navbar-nav navbar-right">
			<li class="desk"><a href="javascript:desktop();"><span class="fui-windows-8" ></span></a></li>
		</ul>
		<#if ("true"=showQuickLoopMenu)>
			<ul class="nav navbar-nav navbar-right">
				<li class="desk" id="loopunderwriting">
					<a data-bind="business/loop/loopunderwritinglist" target="fra_content" style="cursor: pointer">核保轮询状态查询</a>
				</li>
			</ul>
		</#if>
	</nav>
	<input type="hidden" value="${username }" id="ulogfute"/>
	<input type="hidden" value="${ppp }" id="qazwsx" />
	<input type="hidden" value="${postfix }" id="usxiazx" />
	<input type="hidden" value="${host }" id="ytyzea" />
    <input type="hidden" value="${messagePushChannel }" id="messagePushChannel" />
    <input type="hidden" value="${websocketServer }" id="websocketServer" />
	<input type="hidden" value="" id="blink" />
	<input type="hidden" id="my_menu" value="${myMenu.myMenu}">
	<input type="hidden" id="user_code" value="${insc_user.usercode}">
	
</body>
</html>
