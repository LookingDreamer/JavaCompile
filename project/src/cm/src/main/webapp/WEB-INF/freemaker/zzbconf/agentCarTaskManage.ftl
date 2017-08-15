<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>代理人相关车险任务管理</title>
	<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>
	<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
	<script type="text/javascript">
			requirejs([ "zzbconf/agentcarTask" ]);
	</script>
	<style type="text/css">
		div#selectStype a {
			position:relative;
			left:15px;
			top:10px;
		}
	</style>
</head>
<body>
	<div class="container-fluid" style="margin-bottom:30px">
	  <from>
		<input id="agentNum" type="hidden" value="${agentNum }"/>
	  </from>	
		<div class="panel panel-default">
		  <div class="panel-heading padding-5-5">
			  <div class="row">
				 <div class="col-md-2">
				 	<label>任务列表：</label>
				 </div>
				 <div  style="margin-right: 40px;float: right;">
				   <button class="btn btn-primary" id="tasklist_return">返回</button>
				 </div>
			  </div>
		  </div>
	  	  <div class="row">
			  <div class="col-md-12">
				  <table id="table-javascript"></table>
			  </div>
	  	  </div>
		</div>
		 
	</div>
</body>
</html>
