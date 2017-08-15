<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>规则列表</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/zTree/css/demo.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/zTree/css/zTreeStyle/zTreeStyle.css"
	rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "zzbconf/rulebase" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">查询</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-md-12">
						<form class="form-inline" role="form" id="groupform">											
							<div class="form-group col-md-12 form-inline">
								<label for="exampleInputCode">规则类别</label> 
								<select  class="form-control" id="ruletype" name="ruletype">
											<option value="0" >请选择</option>
											<option value="1" >调度规则</option>
											<option value="2" >权重规则</option>
								</select>
								<select  class="form-control" id="param2" name="param2">
											<option value="0" >请选择</option>
											<option value="1" >规则名称</option>
											<option value="2" >规则描述</option>
								</select>
								<input class="form-control" type="text" id="param3" name="param3" >
							</div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputCode">规则状态</label> 
								<select class="form-control" id="rulebaseStatus" name="rulebaseStatus">
									<option value="0" >请选择</option>
									<option value="1" >已关联</option>
									<option value="2" >未关联</option>
								</select>
							</div>
							<div class="form-group col-md-12 form-inline">
								<label for="exampleInputCode">机&nbsp;&nbsp;构&nbsp;&nbsp;</label> 
								<select  class="form-control m-left-5" id="dept1" name="dept1">
									<option value="0" >请选择</option>
									<#list deptParentList as d1>
										<option value="${d1.comcode }" >${d1.comname }</option>
									</#list>
								</select>
								<select class="form-control"  id="dept2" name="dept2">
									<option value="0" >请选择</option>
								</select>
								<select class="form-control"  id="dept3" name="dept3">
									<option value="0" >请选择</option>
								</select>
								<select class="form-control"  id="dept4" name="dept4">
									<option value="0" >请选择</option>
								</select>
								<select class="form-control"  id="dept5" name="dept5">
									<option value="0" >请选择</option>
								</select>
							</div>																	
						</form>
					</div>
				</div>
			</div>
			<div class="panel-footer">
				<button id="rulebase_query" type="button" name="querybutton"
					class="btn btn-primary">查询</button>
					
				<button id="resetbutton" type="button" name="resetbutton"
					class="btn btn-primary">重置</button>
				<button class="btn btn-primary" type="button" name="refresh"
					title="Refresh" id="refresh">
					<i class="glyphicon glyphicon-refresh icon-refresh"></i>
				</button>
			</div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading padding-5-5">
			</div>
			<div class="row">
				<div class="col-md-12">
					<table id="rulebase_list"></table>
				</div>
			</div>
		</div>
		<div class="modal fade" id="myModal_taskset_add" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" backdrop='static'>
			<div class="modal-dialog" style="width: 50%">
				<div class="modal-content" id="modal-content">
				</div>
			</div>	
		</div>
	</div>
</body>
</html>
