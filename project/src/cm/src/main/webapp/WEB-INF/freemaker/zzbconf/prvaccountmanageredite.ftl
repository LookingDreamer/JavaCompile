<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代理人编辑</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/prvaccountmanageredite"]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">编辑</div>
			<form class="form-inline"  id="form_date" action="saveorupdate" method="post">
			<div class="panel-body">
				<div class="row">
					<div class="col-md-12">
						
						<div class="alert alert-danger" role="alert" id="showerrormessage" style="display: none;"></div>
							<table class="table table-bordered " id="base_data">
								<tr>
									<td class="col-md-2 " align="right"
										style="vertical-align: middle;">归属保险公司：</td>
									<td class="col-md-2">
										<input class="form-control" type="hidden" id="org" name="providerid" value="${prvId!''}">
										<input class="form-control" type="text" id="orgname" name="orgname" value="${prvName!''}">
										<input class="form-control" type="hidden" id="id" name="id" value="${data.id!''}">
									</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">版本：</td>
									<td class="col-md-2">
										<input class="form-control" type="text" id="version" name="version" value="${data.version!''}">
									</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">归属机构 ：</td>
									<td class="col-md-2">
										<input class="form-control" type="hidden" name="deptid" id="deptid"  value="${deptid}" >
										<input class="form-control" type="text" disabled="disabled"  id="deptname"  value="${deptname!''}" ></td>
								</tr>
								<tr>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">账号：</td>
									<td class="col-md-2">
										<input class="form-control" type="text" id="account" name="account" value="${data.account!'' }">
									</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">密码：</td>
									<td class="col-md-2">
										<input class="form-control" type="password" id="pwd" name="pwd" value="${data.pwd!'' }">
										</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">权限控制：</td>
									<td class="col-md-2">
										<#if data.permission=="1">
										<input type="radio" value="1" name="permission" checked="checked">作业<input value="2" type="radio" name="permission">查询
										<#elseif data.permission=="2">
										<input type="radio" value="1" name="permission" >作业<input value="2" type="radio"  checked="checked" name="permission">查询
										<#else>
										<input type="radio" value="1" name="permission">作业<input value="2" type="radio" name="permission">查询
										</#if>
									</td>
								</tr>
								<tr>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">登陆地址：</td>
									<td class="col-md-2">
										<input class="form-control"  type="text" name="loginurl" value="${data.loginurl!'' }"   id="loginurl">
									</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">保险公司内部机构：</td>
									<td class="col-md-2" >
										<input class="form-control"  type="text" name="org" value="${data.org!'' }">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">类型：</td>
									<td class="col-md-2" >
										<select id="usetype" class="form-control"  name="usetype">
												<option value="0">请选择</option>
										<#if data.usetype==1>
											<option value="1" selected="selected">精灵</option>
											<option value="2">EDI</option>
											<#elseif data.usetype==2>
											<option value="1" >精灵</option>
											<option value="2" selected="selected">EDI</option>
											<#else>
											<option value="1" >精灵</option>
											<option value="2">EDI</option>
											</#if>
										</select>
									</td>
								</tr>
								<tr>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">备注：</td>
									<td class="col-md-10" colspan="5">
										<input class="form-control"  type="text" name="noti" value="${data.noti!'' }"   id="noti">
									</td>
								</tr>
							</table>
					</div>
				</div>
			</div>
			<div class="panel-footer">
				<button  type="button" id="savebutton"  class="btn btn-primary">保存</button>
				<button  type="button"  id="go_back" class="btn btn-primary">返回</button>
			</div>
			</form>		
		</div>
	</div>
	<div id="showpic" class="modal fade" role="dialog"
		aria-labelledby="myModalLabel" data-backdrop="false"
		data-keyboard="false" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="gridSystemModalLabel">选择供应商</h4>
				</div>
				<div class="modal-body" style="overflow: auto; height: 400px;">
					<div class="container-fluid">
						<div class="row">
							<ul id="treeDemo" class="ztree"></ul>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>