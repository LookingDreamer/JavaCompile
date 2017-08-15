<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/prvaccountkeyedite"]);
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
										style="vertical-align: middle;">参数编码：</td>
									<td class="col-md-2">
										<input class="form-control" type="text" id="paramname" name=paramname value="${datakey.paramname!''}">
										<input class="form-control" type="hidden" id="id" name="id" value="${datakey.id!''}">
										<input class="form-control" type="hidden" id="deptId" name="deptId" value="${deptId!''}">
										<input class="form-control" type="hidden" id="managerid" name="managerid" value="${managerid!''}">
									</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">参数值：</td>
									<td class="col-md-2">
										<input class="form-control" type="text" id="paramvalue" name="paramvalue" value="${datakey.paramvalue!''}">
									</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">备注：</td>
									<td class="col-md-2">
										<input class="form-control" type="text" name="noti"   id="noti"  value="${datakey.noti!''}" ></td>
								</tr>
							</table>
					</div>
				</div>
			</div>
			<div class="panel-footer">
				<button  type="button" id="save_key_button"  class="btn btn-primary">保存</button>
				<button  type="button"  id="go_back" class="btn btn-primary">返回</button>
			</div>
			</form>		
		</div>
	</div>
</body>
</html>