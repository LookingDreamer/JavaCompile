<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务组管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/zTree/css/demo.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/zTree/css/zTreeStyle/zTreeStyle.css"
	rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "zzbconf/tasksetlist" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">查询</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-md-12">
					  <form class="form-inline" role="form" id="groupform">
						<div class="form-group col-md-8 form-inline">
							<label for="exampleInputCode">类别</label> 
							<select  class="form-control" id="tasksetkind" name="taskkind">
								<option value="0" >请选择</option>
								<option value="1" >任务组名称</option>
								<option value="5" >业管组名称</option>
							</select>
							<input class="form-control" type="text" id="tasksetname" name="tasksetname" >
						</div>
						<div class="form-group col-md-4 form-inline">
							<label for="exampleInputCode">权限是否生效</label> 
							<select class="form-control" id="setstatus" name="setstatus">
								<option value="0" >请选择</option>
								<option value="1" >是</option>
								<option value="2" >否</option>
							</select>
						</div>
					  </form>
					</div>
				</div>
			</div>
			<div class="panel-footer">
				<button id="permissionset_query" type="button" name="querybutton"
					class="btn btn-primary">查询</button>
				<button id="resetbutton" type="button" name="resetbutton"
					class="btn btn-primary">重置</button>
				<button class="btn btn-primary" type="button" name="refresh"
					title="Refresh" id="refresh">
					<i class="glyphicon glyphicon-refresh icon-refresh"></i>
				</button>
			</div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading padding-5-5">
				<div class="row">
					<div class="col-md-2">结果</div>
					<div class="col-md-10" align="right">
						<button class="btn btn-primary" type="button" id="add">新增</button>
						<button class="btn btn-primary" type="button" id="update">修改</button>
						<button class="btn btn-primary" type="button" id="delete">删除</button>
						<button class="btn btn-primary" type="button" id="change_status">批量修改任务组状态</button>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table id="taskset_list"></table>
				</div>
			</div>
		</div>
		<div class="modal fade" id="myModal_taskset_add" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" backdrop='static'>
			<div class="modal-dialog" style="width: 90%">
				<div class="modal-content" id="modal-content">
				</div>
			</div>	
		</div>
		<div class="modal fade" id="myModal_group_add" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" >
			<div class="modal-dialog" style="width: 70%">
				<div class="modal-content" id="modal-content">
				</div>
			</div>	
		</div>
		<div class="modal fade" id="myModal_rule_add" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" >
			<div class="modal-dialog" style="width: 70%">
				<div class="modal-content" id="modal-content">
				</div>
			</div>	
		</div>
	</div>
</body>
</html>
