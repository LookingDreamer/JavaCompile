<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>退款查询</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<style type="text/css"> 
        tr{height:40px;}
		td{vertical-align: left;}
</style>
<script type="text/javascript">
	requirejs([ "extra/queryRefun" ]);
</script>
<body>
	<div class="panel panel-default m-bottom-5">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-body">
				<div class="row">
					<div class="col-md-12">
						<form role="form" id="orderParam">
							<div class="form-group form-inline col-md-4">
								<label for="exampleInputOrgName">订单号：</label> <input type="text"
																					 class="form-control m-left-5"
																					 id="taskId" name="taskId"
																					 placeholder="">
							</div>
							<div class="form-group form-inline col-md-4">
								<label for="exampleInputOrgName">下单人姓名：</label> <input type="text"
																					 class="form-control m-left-5"
																					 id="name" name="name"
																					 placeholder="">
							</div>
							<div class="form-group form-inline col-md-4">
								<label for="exampleInputOrgName">订单状态：</label> <select class="form-control m-left-5" id="taskState" name="taskState">
								<option value="">全部</option>
								<option value="12">退款中</option>
								<option value="13">退款完毕</option>
							   
							</select>
							</div>
							
							<div class="form-group form-inline col-md-4">
								<label for="exampleInputCode">车牌号码	：</label> <input type="text"
																					 class="form-control m-left-5"
																					 id="carLicenseNo" name="carLicenseNo"
																					 placeholder="">
							</div>														 
							<div class="form-group form-inline col-md-8">
								<label for="exampleInputCode">时间范围：</label><input type="text"
								<input type="text" class="form-control form_datetime" id="createTimeStart" name="createTimeStart"
									   readonly placeholder="">至
									   <input type="text"
								<input type="text" class="form-control form_datetime" id="createTimeEnd" name="createTimeEnd" readonly
									   placeholder="">
							</div>
							<div class="form-group form-inline col-md-4">
								<label for="exampleInputOrgName">手机号码：</label> <input type="text"
																					 class="form-control m-left-5"
																					 id="phoneMini" name="phoneMini"
																					 placeholder="">
							</div>
						
						
						</form>
					</div>	
				</div>
			</div>
			<div class="panel-footer">
				<button id="querybutton" type="button" name="querybutton"
						class="btn btn-primary">查询
				</button>
				<button id="resetbutton" type="button" name="resetbutton"
                                  class="btn btn-primary">重置</button>
				<button class="btn btn-primary" type="button" name="toggle" title="Toggle" id="toggle">
												<i class="glyphicon glyphicon-list-alt icon-list-alt"></i>
										</button>
			</div>
		</div>
        <hr size=5 color=#00ffff style="filter:progid:DXImageTransform.Microsoft.Glow(color=#00ffff,strength=10)>
		
		<div class="panel panel-default">
			<div class="row">
				<div class="col-md-12">
					<table id="table-list"></table>
				</div>
			</div>
		</div>
	</div>

	
</body>