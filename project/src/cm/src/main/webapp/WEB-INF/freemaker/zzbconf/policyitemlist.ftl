<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代理人管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<style type="text/css">
        tr{height:40px;}
		td{vertical-align: left;}
</style>
<script type="text/javascript">
	requirejs([ "zzbconf/policyitemlist" ]);
</script>
<body>
	<body>
	<div class="container-fluid">
		<div class="panel panel-default">
		  <div class="panel-heading padding-5-5">
		  <div class="row">
				<div class="col-md-12">
					保单列表 
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
	<div class="container-fluid">
  		<div class="col-md-12">
  			<input class="btn btn-primary" id="backbutton" value="返回" type="button">
  		</div>
  	</div>
</body>
</html>
