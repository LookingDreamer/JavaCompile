<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置优先级和权限</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>
<script type="text/javascript">
	requirejs([ "jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","zzbconf/channelagreementsub" ]);
</script>
</head>
<body>
	<input id="agreementDeptid" type="hidden" value="${agreementDept.id}"/>	
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<h4 class="modal-title">设置</h4>
	</div>
	<div class="modal-body">
		<form class="form-inline" role="form" >
			<table class="table table-bordered">
				<tr>
					<td class="col-md-2 active">优先级：</td>
					<td>
						<input id="scaleflag" type="text" value="${agreementDept.scaleflag}"/>
					</td>
				</tr>
				<tr>
					<td class="active">权限设置：</td>
					<td>
						<input id="permflag" type="text" value="${agreementDept.permflag}"/>
					</td>
				</tr>
			</table>
			<div class="row">
				<div class="col-md-6">
					<button class="btn btn-default" type="button" name="makesure" onclick="editChannelDeptLevel();"
						id="makesure" title="确定" data-dismiss="modal">&nbsp;&nbsp;确定&nbsp;&nbsp;</button>
				</div>
				<div class="col-md-6" align="right">
					<button class="btn btn-default" type="button" name="cancel"
						id="cancel" title="取消" data-dismiss="modal">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>
				</div>
			</div>
		</form>
	</div>
</body>
</html>
