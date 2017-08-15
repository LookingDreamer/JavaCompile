<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代理人编辑</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/groupmrgcommonedit" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">群组基本信息</div>
			<div class="panel-body">
				<div class="row">
				<div class="col-md-12">
					<form class="form-inline" role="form" id="agentform">
						<table class="table table-bordered ">
							<tr>
								<td class="col-md-4" align="right" style="vertical-align: middle;">群组名称：</td>
								<td class="col-md-8">
									<input class="form-control" id="groupname"
										type="text" name="groupname" >
								</td>
							</tr>
							<tr style="display: none;">
								<td class="col-md-4" align="right" style="vertical-align: middle;">直属上级：</td>
								<td class="col-md-8">
									<select class="form-control" id="pid" name="pid" class="form-control">
										<#list parentGroup as pg>
											<option value="${pg.id }">${pg.groupname }</option>
										</#list>
									</select>
								</td>
							</tr>
							<tr id="end_tr">
								<td class="col-md-4" align="right" style="vertical-align: middle;">群组类型：</td>
								<td class="col-md-8">
									<select id="groupkind" name="groupkind" class="form-control">
										<option value="0">请选择</option>
										<#list groupType as gtoup>
											<option value="${gtoup.codevalue }">${gtoup.codename }</option>
										</#list>	
									</select>
								</td>
							</tr>
						</table>
					</form>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-footer">
			<button  type="button" id="group_detail"
				class="btn btn-primary">下一步</button>
			<button  type="button" id="go_back"
				class="btn btn-primary">返回</button>
		</div>
	</div>
</body>
</html>