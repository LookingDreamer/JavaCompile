<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/zTree/css/demo.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/zTree/css/zTreeStyle/zTreeStyle.css"
	rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "zzbconf/ruletasksetlist" ]);
</script>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">查询</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-md-12">
						<form class="form-inline" role="form" id="groupform">
							<table class="table table-bordered ">
								<tr>
									<td class="col-md-3 " align="right"
										style="vertical-align: middle;">任务组名称：</td>
									<td class="col-md-9 ">
									<input type="hidden" id="gids" value="${tasksetIds }">
									<input type="hidden" id="id" value="${id }">
									<input class="form-control col-md-5" type="text" id="tasksetname" name="tasksetname" >
									</td>
								</tr>
							</table>
						</form>
					</div>
				</div>
			</div>
			<div class="panel-footer">
				<button id="permissionset_query" type="button" 	class="btn btn-primary">查询</button>
				<button id="resetbutton" type="button" class="btn btn-primary">重置</button>
			</div>
		</div>
		<div class="panel panel-default">
			<div class="row">
				<div class="col-md-12">
					<table id="taskset_list"></table>
				</div>
			</div>
		</div>
		<div class="panel-footer">
				<button type="button" id="save_taskset2rule"  class="btn btn-primary">保存</button>
				<button type="button" class="btn btn-default" id="close_panal">关闭</button>
			</div>
	</div>