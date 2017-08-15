<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/zTree/css/demo.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/zTree/css/zTreeStyle/zTreeStyle.css"
	rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "system/role" ]);
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-4">
				<div class="panel panel-default m-bottom-5">
					<div class="panel-heading padding-5-5">角色列表</div>
					<div class="panel-body">
						<table class="table-striped linenums prettyprint"
							id="role-data-list"></table>
					</div>
				</div>
			</div>
			<div class="col-md-8">
				<div class="panel panel-default m-bottom-5">
					<div class="panel-heading padding-5-5"><p id="title_role"></p></div>
					<div class="panel-body">
						<div class="row">
							<div class="col-md-12">
								<ul id="mytab" class="nav nav-tabs">
									<li class="active"><a href="#basedata" data-toggle="tab">基本信息</a></li>
<!-- 									<li><a href="#stationdata" data-toggle="tab">站点权限</a></li> -->
									<li><a href="#menudata" id="menuTab" data-toggle="tab">菜单权限</a></li>
<!-- 									<li><a href="#docdata" data-toggle="tab">文档权限</a></li> -->
								</ul>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="tab-content">
									<div class="tab-pane active" id="basedata">
										<div align="right" style="padding-top: 10px; padding-bottom: 10px" class="btn-group" role="group" aria-label="...">
											<button data-toggle="modal" data-target="#myModal" type="button" class="btn btn-primary" name="add" title="新增"
												id="main2add">新增</button>
											<button data-toggle="modal" data-target="#myModal" type="button" class="btn btn-primary" name="main2edit"
												title="修改" id="main2edit">修改</button>
											<button type="button" class="btn btn-primary"
												name="delete_role" title="删除" id="delete_role">删除</button>
										</div>
										<div class="table-responsive">
											<table class="table table-bordered ">
												<tr>
													<td class="col-md-2" align="right">角色编码：</td>
													<td class="col-md-4" id="rolecode"></td>
													<td class="col-md-2" align="right">角色名称：</td>
													<td class="col-md-4" id="rolename">
													</td>
												</tr>
												<tr>
													<td class="col-md-2" align="right">机构内部编码：</td>
													<td class="col-md-4" id="branchinnerCode"></td>
													<td class="col-md-2" align="right">机构状态：</td>
													<td class="col-md-4" id="statusstr"></td>
												</tr>
												<tr>
													<td class="col-md-2" align="right">备注：</td>
													<td class="col-md-10" colspan="3">
													<input type="hidden" id="roleid">
													<textarea
															disabled="disabled" id="noti" name="noti"
															class="form-control" rows="3"></textarea></td>
												</tr>
											</table>
										</div>
										<div align="center"
											style="padding-top: 10px; padding-bottom: 10px"
											class="btn-group " role="group" aria-label="...">
											<button data-toggle="modal" data-target="#myModal"
												type="button" class="btn btn-primary" id="add_users2role">添加用户到角色</button>
											<button type="button" class="btn btn-primary"
												id="benchdeletebutton">批量删除用户</button>
										</div>
										<table id="role-user-list"></table>
									</div>
<!-- 									<div class="tab-pane" id="stationdata"></div> -->
									<div class="tab-pane" id="menudata">
										<div class="row">
											<div class="col-md-12">
												<div class="col-md-5">
													<div id="menuTree" class="ztree"></div>
												</div>
												<div class="col-md-7">
													<button class="btn btn-primary" id="save_menu_ids"  type="button" value="">保存</button>
												</div>
											</div>
										</div>
									</div>
<!-- 									<div class="tab-pane" id="docdata">444.</div> -->
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade" id="myModal_add" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog" style="width: 80%">
				<div class="modal-content" id="modal-content">
				</div>
			</div>
		</div>
		<div class="modal fade" id="myModal_eidt" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog" style="width: 80%">
				<div class="modal-content" id="modal-content">
				</div>
			</div>	
		</div>

		<div class="modal fade" id="myModal_show" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content" id="modal-content">
					<div>
						<form action="" class="form-inline">
							<div style="margin-top:10px;margin-left:10px;">
								<label for="exampleInputCode">用户编码</label> <input type="text"
									class="form-control m-left-5" id="usercode" name="usercode" placeholder="">
								<label for="exampleInputName">用户姓名</label> <input type="text"
									class="form-control" id="name" name="name" placeholder="">
								<button data-toggle="" data-target=""
									type="button" class="btn btn-primary" id="queryUser">查询</button>
							</div>																		
						</form>
					</div>
					<div class="modal-body" style="overflow: auto; height: 360px;">
						<table id="role-users-add">
						</table>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="button" id="save_users_roleid"
							class="btn btn-primary">保存</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

</html>
