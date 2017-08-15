
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色管理</title>
<link href="/cm/static/css/appmodule.css" rel="stylesheet">
<link href="/cm/static/css/lib/zTree/css/demo.css" rel="stylesheet">
<link href="/cm/static/css/lib/zTree/css/zTreeStyle/zTreeStyle.css"
	rel="stylesheet">
<script data-ver="1.0.1" data-main="/cm/static/js/load"
	src="/cm/static/js/lib/require.js?ver=1.0.1"></script>
<script type="text/javascript">
	requirejs([ "extra/miniRole" ]);
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
					<div align="right" style="padding-top: 10px; padding-bottom: 10px"
						class="btn-group" role="group" aria-label="...">
						<button style="display: none" data-toggle="modal" data-target="#myModal" type="button"
							class="btn btn-primary" name="add" id="initUserAtrrBt">初始化用户属性</button>
					</div>
				</div>
			</div>
			<div class="col-md-8" id="mouseid">
				<div class="panel panel-default m-bottom-5">
					<div class="panel-heading padding-5-5">
						<p id="title_role"></p>
					</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-md-12">
								<ul id="mytab" class="nav nav-tabs">
									<li class="active"><a href="#basedata" data-toggle="tab">角色信息</a></li>

									<li><a href="#menudata" id="menuTab" data-toggle="tab">权限管理</a></li>
								</ul>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<div class="tab-content">
									<div class="tab-pane active" id="basedata">
										<div align="center"
											style="padding-top: 10px; padding-bottom: 10px"
											class="btn-group " role="group" aria-label="...">
											<button data-toggle="modal" data-target="#addRoleModel"
												type="button" class="btn btn-primary" name="add"
												id="main2add">新增角色</button>
											<button data-toggle="modal" data-target="#add_per2roleModel"
												type="button" class="btn btn-primary" id="add_per2role">添加权限到角色</button>
											<button data-toggle="modal" data-target="#myModal" style="display: none;"
												type="button" class="btn btn-primary" id="add_users2role">添加用户到角色</button>
											<button class="btn btn-primary" type="button" name="refresh"
												title="Refresh" id="refresh">
												<i class="glyphicon glyphicon-refresh icon-refresh"></i>
											</button>
										</div>
										<table id="role-user-list"></table>
										
									</div>
									 
									<!-- 									<div class="tab-pane" id="stationdata"></div> -->
									<div class="tab-pane" id="menudata">
										<form class="form-inline" role="form" id="queryPermissionform">
											<table class="table table-bordered ">
												<tr>
													<div class="row">
														<div class="form-group form-inline col-md-4">
															<td><label for="exampleInputCode">权限名称&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
															<td><input class="form-control" id="permissionname"
																type="text" name="permissionname"></td>
														</div>
														<div class="form-group form-inline col-md-4">
															<td><label for="exampleInputCode">url</label></td>
															<td><input class="form-control" type="text" id="url"
																name="url"></td>
														</div>
														<div class="form-group form-inline col-md-4">
															<td><label for="exampleInputCode">权限编号</label></td>
															<td><input class="form-control" type="text"
																id="percode" name="percode"></td>
														</div>
													</div>
												</tr>

											</table>
										</form>
										<div align="right"
											style="padding-top: 10px; padding-bottom: 10px"
											class="btn-group" role="group" aria-label="...">
											<button data-toggle="modal" data-target="#myModal"
												type="button" class="btn btn-primary" name="add" id="addPer">新增权限</button>
											<button data-toggle="modal" data-target="#myModal"
												type="button" class="btn btn-primary" name="add"
												id="main2adda">查询权限</button>
											<button class="btn btn-primary" type="button" name="refresh"
												title="Refresh" id="refresh2">
												<i class="glyphicon glyphicon-refresh icon-refresh"></i>
											</button>
										</div>
										<div class="row">

											<div class="col-md-12">
												<table id="permission_list"></table>
											</div>

										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="panel panel-default" style="display:none;" id="userlist">
                    <div class="panel-heading padding-5-5 form-group col-md-12 form-inline">
                        <h3 class="panel-title">
                            <label for="disInterface">&nbsp;&nbsp;其下属的团队成员：
                              
                            </label>
                        </h3>
                    </div>
                    <div class="panel-body">
                        <div class="rows">
						
						<table id="user_list_Tab"></table>
					</div>	
</div>					
				</div>
				
			</div>
		</div>
	</div>
	<div class="modal fade" id="updateRoleModel" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" data-backdrop="false"
		data-keyboard="false" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content" style="width: 482px;">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span class="label label-danger">X</span><span class="sr-only">Close</span>
					</button>
					<div class="panel panel-default m-bottom-5" style="width: 448px;">
						<div class="panel-heading padding-5-5" style="font-weight: bold;">修改角色，请注意！</div>
						<div class="panel-body">
							<table class="table-no-bordered">
								<tr>
									<td class="text-right"><label>用户角色：</label></td>
									<td style="padding-bottom: 5px;">
										<input class="form-control" style="width: 280px;" type="text"id="s_name" name="name"/>
									</td>
								</tr>
								<tr>
									<td class="text-right"><label>角色编号：</label></td>
									<td style="padding-bottom: 5px;">
										<input class="form-control"  style="width: 280px;" type="text" id="s_rolecode" name="rolecode" readonly/>
									</td>
								</tr>
								<tr style="display: none;">
									<td class="text-right"><label>id：</label></td>
									<td style="padding-bottom: 5px;">
										<input class="form-control readonlysetting disabledsetting"
											style="width: 280px;" type="text" id="s_id" name="id"/>
									</td>
								</tr>
								<tr>
									<td class="text-right"><label>角色描述：</label></td>
									<td style="padding-bottom: 5px;">
									<input class="form-control readonlysetting disabledsetting"
										style="width: 280px;" type="text" id="s_description"
										name="description"/>
									</td>
								</tr>
								<tr>
									<td class="text-right"><label>备注：</label></td>
									<td style="padding-bottom: 5px;"><textarea
											class="form-control" rows="3" id="s_noti" name="noti">
                                        </textarea></td>
								</tr>
							</table>
							<div class="col-md-12"
								style="font-weight: bold; padding-top: 5px;">是否保存？</div>
						</div>
						<div class="panel-footer">
							<input id="s_execButton" name="s_execButton" type="button"
								class="btn btn-primary" value="确定" /> <input id="dcloseButton"
								name="closeButton" class="btn btn-primary" type="button"
								data-dismiss="modal" value="取消" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="addRoleModel" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" data-backdrop="false"
		data-keyboard="false" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content5" style="width: 482px;">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span class="label label-danger">X</span><span class="sr-only">Close</span>
					</button>
					<div class="panel panel-default m-bottom-5" style="width: 448px;">
						<div class="panel-heading padding-5-5" style="font-weight: bold;">新增角色，请注意！</div>
						<div class="panel-body">
							<table class="table-no-bordered">
								<tr>
									<td class="text-right"><label>用户角色：</label></td>
									<td style="padding-bottom: 5px;"><input
										class="form-control"  
										style="width: 280px;"
                                        type="text"
										id="x_name" name="name"/></td>
								</tr>
								<tr>
									<td class="text-right"><label>角色编号：</label></td>
									<td style="padding-bottom: 5px;"><input
										class="form-control"  
										style="width: 280px;"
                                        type="text"
										id="x_rolecode" name="rolecode" /></td>
								</tr>
								<tr style="display: none;">
									<td class="text-right"><label>id：</label></td>
									<td style="padding-bottom: 5px;"><input
										class="form-control readonlysetting disabledsetting"
										style="width: 280px;" type="text" id="x_id" name="id" /></td>
								</tr>
								<tr>
									<td class="text-right"><label>角色描述：</label></td>
									<td style="padding-bottom: 5px;"><input
										class="form-control readonlysetting disabledsetting"
										style="width: 280px;" type="text" id="x_description"
										name="description" /></td>
								</tr>

								<tr>
									<td class="text-right"><label>备注：</label></td>
									<td style="padding-bottom: 5px;"><textarea
											class="form-control" rows="3" id="x_noti" name="noti">
                                        </textarea></td>
								</tr>

							</table>
							<div class="col-md-12"
								style="font-weight: bold; padding-top: 5px;">是否保存？</div>
						</div>
						<div class="panel-footer">
							<input id="x_execButton" name="x_execButton" type="button"
								class="btn btn-primary" value="确定" /> <input id="closeButton"
								name="rcloseButton" class="btn btn-primary" type="button"
								data-dismiss="modal" value="取消" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>


	<div class="modal fade" id="addUserByRoleModel" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" data-backdrop="false"
		data-keyboard="false" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content4">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span class="label label-danger">X</span><span class="sr-only">Close</span>
					</button>
					<div class="panel panel-default m-bottom-5">
						<div class="panel-heading padding-5-5" style="font-weight: bold;">添加用户到角色，请注意！</div>
						<form id="addReferrFrom" class="form-inline" role="form">
							<table class="table table-bordered ">
								<tr>
									<td class="text-right"><label>手机号码：</label></td>
									<td style="padding-bottom: 5px;"><input
										class="form-control readonlysetting disabledsetting"
										style="width: 280px;" type="text" id="t_phone" name="phone" /></td>
								</tr>
								<tr>
									<td class="text-right"><label>昵称：</label></td>
									<td style="padding-bottom: 5px;"><input
										class="form-control readonlysetting disabledsetting"
										style="width: 280px;" type="text" id="t_nickname"
										name="nickname" /></td>
								</tr>
							</table>
						</form>
						<button id="t_querybutton" type="button" name="querybutton"
							class="btn btn-primary">查询</button>
						<div class="panel-body">
							<div class="row">
								<div class="col-md-12">
									<table id="agent_plist"></table>
								</div>
							</div>
						</div>
						<div class="panel-footer">
							<input id="t_execButton" name="t_execButton" type="button"
								class="btn btn-primary" value="保存" /> <input id="t_closeButton"
								name="closeButton" class="btn btn-primary" type="button"
								data-dismiss="modal" value="取消" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	</div>
	<div class="modal fade" id="updateMiniChannelModel" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" data-backdrop="false"
		data-keyboard="false" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content4">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span class="label label-danger">X</span><span class="sr-only">Close</span>
					</button>
					<div class="panel panel-default m-bottom-5">
						<div class="panel-heading padding-5-5" style="font-weight: bold;">更改mini渠道！</div>
						<form id="updateMiniChannel" class="form-inline" role="form">
						<table class="table table-bordered ">
								<tr style="display: none;">
									<td class="text-right"><label>openid：</label></td>
									<td style="padding-bottom: 5px;"><input
										class="form-control"
										style="width: 280px;" type="text" id="m_openid"
										name="openid" /></td>
								</tr>
								<tr>
									<td class="text-right"><label>渠道：</label></td>
									<td style="padding-bottom: 5px;"><input
										class="form-control"
										style="width: 280px;" type="text" id="m_pushChannel"
										name="pushChannel" /></td>
								</tr>
							</table>
						</form>
						<div class="panel-footer">
							<input id="m_execButton" name="t_execButton" type="button"
								class="btn btn-primary" value="保存" /> <input id="m_closeButton"
								name="closeButton" class="btn btn-primary" type="button"
								data-dismiss="modal" value="取消" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	</div>
	<div class="modal fade" id="add_per2roleModel" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" data-backdrop="false"
		data-keyboard="false" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content4s">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span class="label label-danger">X</span><span class="sr-only">Close</span>
					</button>
					<div class="panel panel-default m-bottom-5">
						<div class="panel-heading padding-5-5" style="font-weight: bold;">添加权限到角色，请注意！</div>
						<form id="add_per2roleForm" class="form-inline" role="form">
							<table class="table table-bordered ">
								<tr>
									<td class="text-right"><label>权限名称：</label></td>
									<td style="padding-bottom: 5px;"><input
										class="form-control readonlysetting disabledsetting"
										style="width: 280px;" type="text" id="qx_permissionname"
										name="permissionname" /></td>
								</tr>
								<tr>
									<td class="text-right"><label>权限编号：</label></td>
									<td style="padding-bottom: 5px;"><input
										class="form-control readonlysetting disabledsetting"
										style="width: 280px;" type="text" id="qx_percode"
										name="percode" /></td>
								</tr>
							</table>
						</form>
						<button id="qx_querybutton" type="button" name="querybutton"
							class="btn btn-primary">查询</button>
						<div class="panel-body">
							<div class="row">
								<div class="col-md-12">
									<table id="add_per2role_tab"></table>
								</div>
							</div>
						</div>
						<div class="panel-footer">
							<input id="qx_execButton" name="t_execButton" type="button"
								class="btn btn-primary" value="保存" /> <input
								id="qx_closeButton" name="closeButton" class="btn btn-primary"
								type="button" data-dismiss="modal" value="取消" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	</div>
	<div class="modal fade" id="permissionModel" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" data-backdrop="false"
		data-keyboard="false" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content3">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span class="label label-danger">X</span><span class="sr-only">Close</span>
					</button>
					<div class="panel panel-default m-bottom-5">
						<div class="panel-heading padding-5-5" style="font-weight: bold;">此角色下的权限</div>
						<div class="row">
							<div class="col-md-12">
								<table id="permission_tab"></table>
							</div>
						</div>
					</div>
					<div class="panel-footer"></div>
				</div>
			</div>
		</div>
	</div>
	</div>
	</div>
	<div class="modal fade" id="permissionUpdateModel" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" data-backdrop="false"
		data-keyboard="false" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content2">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span class="label label-danger">X</span><span class="sr-only">Close</span>
					</button>
					<div class="panel panel-default m-bottom-5">
						<div class="panel-heading padding-5-5" style="font-weight: bold;">权限的修改</div>
						<form id="addReferrFrom0" class="form-inline" role="form">
							<table class="table table-bordered ">
								<tr>
									<td class="text-right"><label>权限名称：</label></td>
									<td style="padding-bottom: 5px;"><input
										class="form-control" style="width: 280px;" type="text"
										id="qxq_permissionname" name="permissionname" /></td>
								</tr>
								<tr>
									<td class="text-right"><label>url：</label></td>
									<td style="padding-bottom: 5px;"><input
										class="form-control" style="width: 280px;" type="text"
										id="qxq_url" name="url" /></td>
								</tr>
								<tr>
									<td class="text-right"><label>权限编号：</label></td>
									<td style="padding-bottom: 5px;"><input
										class="form-control" style="width: 280px;" type="text"
										id="qxq_percode" name="percode" readonly /></td>
								</tr>
								<tr style="display: none;">
									<td class="text-right"><label>id：</label></td>
									<td style="padding-bottom: 5px;"><input
										class="form-control" style="width: 280px;" type="text"
										id="qxq_id" name="id" /></td>
								</tr>
								<tr style="display: none;">
									<td class="text-right"><label>临时编码：</label></td>
									<td style="padding-bottom: 5px;"><input
										class="form-control" style="width: 280px;" type="text"
										id="qxq_perindex" name="perindex" /></td>
								</tr>
								<tr>
									<td class="text-right"><label>权限描述：</label></td>
									<td style="padding-bottom: 5px;"><input
										class="form-control" style="width: 280px;" type="text"
										id="qxq_perdesc" name="perdesc" /></td>
								</tr>
								<tr>
									<td class="text-right"><label>备注：</label></td>
									<td style="padding-bottom: 5px;"><textarea
										class="form-control" rows="3" type="text"
										id="qxq_noti" name="noti" ></textarea></td>
								</tr>
							</table>
							<div class="panel-footer">
								<input id="qxq_execButton" name="t_execButton" type="button"
									class="btn btn-primary" value="保存" /> <input
									id="qx_closeButton" name="xcloseButton" class="btn btn-primary"
									type="button" data-dismiss="modal" value="取消" />
							</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	</div>
	<div class="modal fade" id="permissionAddModel" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" data-backdrop="false"
		data-keyboard="false" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content1">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span class="label label-danger">X</span><span class="sr-only">Close</span>
					</button>
					<div class="panel panel-default m-bottom-5">
						<div class="panel-heading padding-5-5" style="font-weight: bold;">权限的添加</div>
						<table class="table table-bordered ">
							<tr>
								<td class="text-right"><label>权限名称：</label></td>
								<td style="padding-bottom: 5px;"><input
									class="form-control"  
									style="width: 280px;"
									id="qa_permissionname" name="permissionname" /></td>
							</tr>
							<tr>
								<td class="text-right"><label>url：</label></td>
								<td style="padding-bottom: 5px;"><input
									class="form-control" style="width: 280px;"
                                    type="text"
									id="qa_url" name="url"/></td>
							</tr>
							<tr>
								<td class="text-right"><label>权限编号：</label></td>
								<td style="padding-bottom: 5px;"><input
									class="form-control"  
									style="width: 280px;"
                                    type="text"
									id="qa_percode" name="percode" readonly /></td>
							</tr>
							<tr>
								<td class="text-right"><label>权限描述：</label></td>
								<td style="padding-bottom: 5px;"><input
									class="form-control"  
									style="width: 280px;"
                                    type="text"
									id="qa_perdesc" name="perdesc" /></td>
							</tr>
							<tr>
								<td class="text-right"><label>备注：</label></td>
								<td style="padding-bottom: 5px;"><textarea
									class="form-control" rows="3" id="qa_noti" name="noti">
                                        </textarea></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="panel-footer">
					<input id="qa_execButton" name="ta_execButton" type="button"
						class="btn btn-primary" value="保存" /> <input id="qa_closeButton"
						name="acloseButton" class="btn btn-primary" type="button"
						data-dismiss="modal" value="取消"/>
				</div>
			</div>
		</div>
	</div>
	</div>
	</div>
	</div>
</body>
</html>
