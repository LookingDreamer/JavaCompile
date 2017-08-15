<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代理人管理-机构权限批量指定</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/zTree/css/demo.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/zTree/css/zTreeStyle/zTreeStyle.css"
	rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "zzbconf/permissionset" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">查询</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-md-12">
						<div class="form-inline" role="form" id="userform">							
								<div class="row">
									<div class="form-group form-inline col-md-4">
										<label for="exampleInputCode">所属机构</label>
										<input class="form-control" type="hidden" id="deptid" name="deptid" value="${dept.deptid!'' }"> 
											<input class="form-control m-left-5" name="deptname"type="text" id="deptname" value="${dept.comname!'' }">
									</div>
									<div class="form-group form-inline col-md-4">
										<label for="exampleInputCode">用户类型</label>
										<select id="agentkind"  class="form-control m-left-5" name="agentkind">
											<option value="" >请选择</option>
											<option value="1" >试用</option>
											<option value="2" >正式</option>
											<option value="3" >渠道</option>
										</select>
									</div>
								</div>									
						</div>
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
						<button class="btn btn-primary" type="button" id="apply_permissionset">权限包启用</button>
						<button class="btn btn-primary" type="button" id="stop_permissionset">权限包停用</button>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table id="permisssionset_list"></table>
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
	</div>
</body>
</html>
