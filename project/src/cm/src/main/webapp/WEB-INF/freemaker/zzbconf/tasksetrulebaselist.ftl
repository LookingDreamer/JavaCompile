<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
requirejs([ "zzbconf/tasksetrulebaselist" ]);
</script>
<div class="modal-header">
	<h4 class="modal-title">关联规则</h4>
</div>
<div class="modal-body">
	<div class="row">
		<div class="col-md-12">
		<form id="group_param">
			<table class="table table-bordered ">
				<tr>
					<td class="col-md-3">
					已选择的任务组：
					</td>
					<td class="col-md-9" colspan="3">
						${model.setname }
					</td>
				</tr>
				<tr>
					<td class="col-md-2">规则名称：</td>
					<td class="col-md-4">
						<input class="form-control" type="hidden" id="gids" value="${rulebaseIds}">
						<input class="form-control" type="hidden" id="tasksetid" name="tasksetid" value="${model.id }">
						<input class="form-control" type="text" id="rule_name" name="ruleName">
					</td>
					<td class="col-md-2">规则描述：</td>
					<td class="col-md-4" >
						<input class="form-control" type="text" id="rule_postil" name="rulePostil">
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<button id="querybutton" type="button" name="querybutton"
							class="btn btn-primary">查询</button>
						<button id="resetbutton" type="button" name="resetbutton"
							class="btn btn-primary">重置</button>
					</td>
				</tr>
			</table>
			</form>
			<table id="taskset_rulebase_list"></table>
		</div>
	</div>
</div>
<div class="modal-footer">
	<button type="button" class="btn btn-default" id="close_modal">关闭</button>
	<button type="button" id="save_rulebase2taskset" class="btn btn-primary">确认添加</button>
</div>
