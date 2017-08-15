<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代客录单</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "cm/valetcatalogue/valetCatalogue" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">查询</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-md-12">
						<form role="form" id="agencymessage">
							<div class="form-group form-inline col-md-4">
								<label for="exampleInputCode">工号：</label> <input type="text"
									class="form-control m-left-5" id="jobnum" name="jobnum"
									placeholder="">
							</div>
							<div class="form-group form-inline col-md-4">
								<label for="exampleInputCode">代理人姓名：</label> <input type="text"
									class="form-control m-left-5" id="name" name="name"
									placeholder="">
							</div>
							<div class="form-group form-inline col-md-4">
								<label for="exampleInputCode">联系电话：</label> <input type="text"
									class="form-control m-left-5" id="phone" name="phone"
									placeholder="">
							</div>
							<input type="hidden" value="1" id="rownum" name="rownum"/>
						</form>
					</div>
				</div>
			</div>
			<div class="panel-footer">
				<button id="querybutton" type="button" name="querybutton"
					class="btn btn-primary">查询</button>
					
			    <!--<button id="multiplelist" type="button" name="multiplelist">多方报价列表</button>-->
			</div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading padding-5-5">
				<div class="row">
					<div class="col-md-2">结果</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table id="agentMessage"></table>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" id="usercode" name="usercode" value="${loginUser.usercode}"/>
</body>
</html>
