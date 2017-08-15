<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>群组成员列表</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
requirejs([ "zzbconf/groupmemberlist" ]);
</script>
<body>
	<div class="container-fluid">
		<form role="form" id="group_form">
			<div class="panel panel-default m-bottom-5">
				<div class="panel-heading padding-5-5">群组基本信息</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-md-12">
							<table class="table table-bordered ">
								<tr>
									<td class="col-md-3 " align="right"
										style="vertical-align: middle;">群组名称：</td>
									<td class="col-md-9">
										<input type="hidden" id="group_id" value="${comm.id }" >
										${comm.groupname!''}
									</td>
								</tr>
								<tr>
									<td class="col-md-3" align="right"
										style="vertical-align: middle;">群组类型：</td>
									<td class="col-md-9">${comm.groupkindstr!''}</td>
								</tr>
								<tr>
									<td class="col-md-3 " align="right"
										style="vertical-align: middle;">直属上级：</td>
									<td class="col-md-9">${pcomm!''}</td>
								</tr>
								<tr>
									<td class="col-md-3" align="right">所属平台：</td>
									<td class="col-md-9">${groupDeptOrgParentName }&nbsp;&nbsp;&nbsp;${groupDeptOrgName }</td>
								</tr>
								<tr>
									<td class="col-md-3" align="right">管理机构：</td>
									<td class="col-md-9">${deptNames }</td>
								</tr>
								<tr>
									<td class="col-md-3" align="right">管理供应商：</td>
									<td class="col-md-9">${providerNames }</td>
								</tr>
								<tr>
									<td class="col-md-3" align="right">管理权限：</td>
									<td class="col-md-9"><#list data2 as d2> ${d2.name }
										</#list></td>
								</tr>
								<tr>
									<td class="col-md-3" align="right">系统权限：</td>
									<td class="col-md-9"><#if comm.privilegestate==1> 有效
										<#else> 无效 </#if></td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
			<div class="panel panel-default m-bottom-5">
				<div class="panel-heading padding-5-5">
					<div class="row">
						<div class="col-md-2">已添加成员</div>
						<div class="col-md-10" align="right">、
							<button type="button" id="go_back" class="btn btn-primary">返回</button>
							<button class="btn btn-primary" type="button" id="add">添加成员</button>
							<button class="btn btn-primary" type="button" id="remove_group_member">移除选中成员</button>
						</div>
					</div>
				</div>
				<div class="panel-body">
					<table id="group_member_list" class="table table-bordered "></table>
				</div>
				<div class="panel-footer">
				</div>

			</div>
		</form>
		<div class="modal fade" id="myModal_group_member_add" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog" style="width: 90%">
				<div class="modal-content" id="modal-content">
				</div>
			</div>	
		</div>
		<div class="modal fade" id="myModal_group_privilege_update" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog" style="width: 50%">
				<div class="modal-content" id="modal-content">
				</div>
			</div>	
		</div>
	</div>
</body>
</html>