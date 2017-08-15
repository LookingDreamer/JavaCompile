<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>群组管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/zTree/css/demo.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/zTree/css/zTreeStyle/zTreeStyle.css"
	rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "zzbconf/groupmrglist" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">查询</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-md-12">								
						<div class="form-group col-md-4 form-inline">
							<label for="exampleInputCode">群组名称</label> 
							<input class="form-control m-left-5"	type="text" id="groupname" name="groupname" >
						</div>
						<div class="form-group col-md-4 form-inline">
							<label for="exampleInputCode">群组类型</label> 
							<select id="groupkind"  class="form-control m-left-5" name="agentkind">
								<option value="0" >请选择</option>
								<option value="1000000" >代理人及设备管理组</option>
								<option value="2000000" >呼叫中心组</option>
								<option value="3000000" >车险供应商管理组</option>
								<option value="4000000" >业管组</option>
								<option value="5000000" >积分商城管理组</option>
								<option value="6000000" >管理组</option>
								<option value="7000000" >非车险产品管理组</option>
							</select>
						</div>
						<div class="form-group col-md-4 form-inline">
							<label for="exampleInputCode">权限是否生效</label> 
							<select id="privilegestate"  class="form-control m-left-5" name="privilegestate">
								<option value="0" >请选择</option>
								<option value="1" >是</option>
								<option value="2" >否</option>
							</select>
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
<!-- 						<button class="btn btn-primary" type="button" id="update_group_member">编辑成员</button> -->
						<button class="btn btn-primary" type="button" id="delete">删除</button>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table id="group_list"></table>
				</div>
			</div>
		</div>
		<div class="modal fade" id="myModal_common_edit" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content" id="modal-content">
					<div class="modal-body">
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
