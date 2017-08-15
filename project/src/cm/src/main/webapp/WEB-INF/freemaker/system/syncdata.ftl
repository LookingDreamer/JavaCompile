<!DOCTYPE html>
<html lang ="zh_CN">  
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据同步</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "system/syncdata" ]);
</script>
</head>
<body>
 <div class="panel panel-default m-bottom-5">
 <div class="panel-heading padding-5-5" style="color:red;font-weight:bold;">数据同步，谨慎操作</div>
  <div class="panel-body">
	&nbsp;机构代码：&nbsp;<input  type="text" id="comcode">&nbsp;&nbsp;&nbsp;&nbsp;<button class="btn btn-primary" id="sychdeptbycode" type="button">根据机构代码同步机构</button>&nbsp;&nbsp<button class="btn btn-primary" id="syncdept" type="button">同步全部机构</button>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button class="btn btn-primary" id="syncprovider" type="button">同步供应商</button>
	<hr />
	代理人工号：<input  type="text" id="agcodevalue">&nbsp;&nbsp;&nbsp;&nbsp;<button class="btn btn-primary" id="sychAgtDatabyAgcode" type="button">根据工号同步代理人</button>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<button class="btn btn-primary" id="sychAgtData" type="button">同步全部代理人</button>
	<hr />
	<button class="btn btn-primary" id="sychStatus" type="button">同步状态查询</button>
	<div class="row" id="sychStatusMsg" style="height:100px;padding-left:10px;">
	</div>
  </div>
  </div>
</body>
</html>
