<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>功能包编辑</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/permissionsetedit" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">功能包编辑</div>
			<form class="form-inline" role="form" id="permission_form" method="post" action="savebasepermissionset">
			<div class="panel-body">
				<div class="row">
					<div class="col-md-12">
							<table class="table table-bordered ">
								<tr> 
										<td class="col-md-2 " align="right"
											style="vertical-align: middle;">权限包名称：</td>
										<td class="col-md-2"> 
										<input type="hidden" name="id" id="permissionsetId" value="${result.set.id!'' }">
										<input type="hidden"  id="op_flag" value="${flag!'' }">
										<input class="form-control" type="text" name="setname" id="setname" value="${result.set.setname!'' }"></td>
										<td class="col-md-2" align="right"
											style="vertical-align: middle;">权限包代码：</td>
										<td class="col-md-2"><input  class=" form_datetime form-control"
											type="text" name="setcode" id="setcode" value="${result.set.setcode!''}"></td>
										<td class="col-md-2" colspan="2"></td>
									</tr>
								<tr>
									<td class="col-md-2 " align="right"
										style="vertical-align: middle;">所属机构：</td>
									<td class="col-md-2">
									<input class="form-control" type="hidden" id="deptid" name="deptid" value="${result.set.deptid }"> 
									<input class="form-control" type="text" id="deptname" value="${result.set.comname }"></td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">用户类型：</td>
									<td class="col-md-2">
										<select id="agentkind" name="agentkind" class="form-control">
										<#if result.set.agentkind=="1">
											<option value="1"  selected="selected">试用</option>
											<option value="2"  >正式</option>
											<option value="3"  >渠道</option>
										<#elseif result.set.agentkind=="2">
											<option value="1"  >试用</option>
											<option value="2"  selected="selected"  >正式</option>
											<option value="3"  >渠道</option>
										<#elseif result.set.agentkind=="3">
											<option value="1"  >试用</option>
											<option value="2"  >正式</option>
											<option value="3"  selected="selected"  >渠道</option>
										<#else>
											<option value="1"  >试用</option>
											<option value="2"  >正式</option>
											<option value="3"  >渠道</option>	
										</#if>
										</select>
										</td>
									<td class="col-md-2" colspan="2"></td>
								</tr>
								<tr>
									<td colspan="6">
										<button  type="button"  id="save_permisssionset" class="btn btn-primary">基础信息保存</button>
									</td>
								</tr>
							</table>
					</div>
				</div>
			</div>
			<div class="panel-footer">
				<button  type="button"  id="save_permisssion_provider" class="btn btn-primary">权限信息保存</button>
				<button  type="button"  id="go_back" class="btn btn-primary">返回</button>
			</div>
			</form>
			<div class="row">
				<div class="col-md-12">
					<ul id="mytab" class="nav nav-tabs">
						<li class="active"><a href="#permission_tab"
							data-toggle="tab">指定权限</a></li>
						<li><a href="#provider_tab" id="init_provider_tree"
							data-toggle="tab">指定协议</a></li>
                        <li><a href="#user_tab"
                                              data-toggle="tab">已关联用户</a></li>
					</ul>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="tab-content">
						<div class="tab-pane active" id="permission_tab">
							<table id="permisssion_list"></table>
						</div>
						<div class="tab-pane" id="provider_tab">
                            <table id="table-agreement"></table>
						</div>
                        <div class="tab-pane" id="user_tab">
                            <div class="panel-body">
                                <div class="col-md-12">
                                    <form class="form-inline" role="form" id="agentform">
                                        <table  class="table table-bordered ">
                                            <tr>
                                                <div class="row">
                                                    <div class="form-group form-inline col-md-4">
                                                        <td><label for="exampleInputCode">编码</label></td>
                                                        <td><input class="form-control" id="usercode" type="text" name="usercode"></td>
                                                    </div>
                                                    <div class="form-group form-inline col-md-4">
                                                        <td><label for="exampleInputCode">名称</label></td>
                                                        <td><input class="form-control" type="text" id="username" name="username"></td>
                                                    </div>
                                                    <div class="form-group form-inline col-md-4">
                                                        <td>
                                                            <button id="userquerybutton" type="button" name="querybutton"
                                                                    class="btn btn-primary">查询</button>

														</td>


                                                    </div>

												</div>
                                            </tr>

                                            </tr>
                                        </table>
                                    </form>
                                </div>
                            </div>
                            <table id="user_list"></table>
                        </div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 组织机构树 -->
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
	<!-- 权限分配 配置 -->
	<div class="modal fade" id="permissionallot_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content" id="modal-content">
			</div>
		</div>
	</div>
</body>
</html>