<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>在线用户列表</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "system/onlinuserinfo" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">查询</div>
		  <div class="panel-body">
		    <div class="row">
			<div class="col-md-12">
				<form role="form" id="userform">
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputCode">用户名</label> <input type="text"
							class="form-control m-left-5" id="username" name="username" placeholder="">
					</div>
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputCode">用户编码</label> <input type="text"
							class="form-control" id="usercode" name="usercode" placeholder="">
					</div>
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputCode">业管组名称</label> <input type="text"
							class="form-control m-left-5" id="groupname" name="groupname" placeholder="">
					</div>
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputCode">所属机构</label> 
						<input class="form-control" type="hidden" id="deptid" name="userorganization"> 
						<input class="form-control" type="text" id="deptname">
					</div>					
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputOrgName">状态</label> 
						<select name="onlinestatus" class="form-control m-left-2" id="onlinestatus">
						<option value="1">在线</option>
						<option value="2">忙碌</option>
						<option value="0">离线</option>
						</select>
					</div>
				</form>
			</div>
		</div>
		  </div>
		  <div class="panel-footer">
						<button id="querybutton" type="button" name="querybutton"
											class="btn btn-primary">查询</button>
						<button id="resetbutton" type="button" name="resetbutton"
											class="btn btn-primary">重置</button>
						<button class="btn btn-primary" type="button" name="refresh"
											title="Refresh" id="refresh">
											<i class="glyphicon glyphicon-refresh icon-refresh"></i>
										</button>
						<button class="btn btn-primary" type="button" name="toggle" title="Toggle" id="toggle">
												<i class="glyphicon glyphicon-list-alt icon-list-alt"></i>
										</button>
			</div>
		</div>
		<div class="panel panel-default">
		  <div class="panel-heading padding-5-5">
		  <div class="row">
				<div class="col-md-2">
					结果
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
	<div class="modal fade" id="myModal_dept" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content">
				<div class="modal-body"  style="overflow: auto; height: 400px;">
					<div id="dept_tree" class="ztree"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
