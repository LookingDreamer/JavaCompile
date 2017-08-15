<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>奖品管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "extra/agentprize" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">查询</div>
		  <div class="panel-body">
		    <div class="row">
			<div class="col-md-12">
				<form role="form" id="policymanagent">
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">用户姓名</label> <input type="text"
							class="form-control m-left-5" id="q_agentname" name="agentname" placeholder="">
					</div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputCode">用户工号</label> <input type="text"
                                                                          class="form-control m-left-5" id="q_jobnum" name="jobnum" placeholder="">
                    </div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">活动名称</label> <input type="text"
							class="form-control m-left-5" id="q_activityname" name="activityname" placeholder="">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputName">奖品名称</label> <input type="text"
							class="form-control m-left-5" id="q_prizename" name="prizename" placeholder="">
					</div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputName">手机号&nbsp;&nbsp;&nbsp;&nbsp;</label> <input type="text"
                                                                          class="form-control m-left-5" id="q_phone" name="phone" placeholder="">
                    </div>
                    <!--<div class="form-group form-inline col-md-4">
                        <label for="exampleInputCode">使用状态</label>
                        <select class="form-control m-left-5" id="q_status" name="status">
                            <option value="">全部</option>
						<#list marketAgentPrizeStatusList as code>
                            <option value="${code.codevalue }">${code.codename }</option>
						</#list>
                        </select>
                    </div>-->
				</form>
			</div>
		</div>
		  </div>
		  <div class="panel-footer">
						<button id="querybutton" type="button" name="querybutton"
											class="btn btn-primary">查询</button>
						<button id="resetbutton" type="button" name="resetbutton"
											class="btn btn-primary">重置</button>
						<button class="btn btn-primary" type="button" name="refresh"
											title="Refresh" id="refresh">
											<i class="glyphicon glyphicon-refresh icon-refresh"></i>
										</button>
						<button class="btn btn-primary" type="button" name="toggle" title="Toggle" id="toggle">
												<i class="glyphicon glyphicon-list-alt icon-list-alt"></i>
										</button>
			</div>
		</div>
		<div class="panel panel-default">
		  <div class="panel-heading padding-5-5">
		  <div class="row">
				<div class="col-md-2">
					结果
					</div>
			</div>
			</div>
		    <div class="row">
				<div class="col-md-12">
					<table id="table-list"></table>
				</div>
		  </div>
		</div>
	</div>
    <!--addrefresh refresh-->
</body>
</html>
