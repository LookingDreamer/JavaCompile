<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>推送工作流 </title>
	<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>
	<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
	<script type="text/javascript">
			requirejs([ "cm/workflow/insbworkflow" ]);
	</script>
	<style type="text/css">
		div#selectStype a {
			position:relative;
			left:15px;
			top:10px;
		}
	</style>
</head>
<body>
	<div class="container-fluid" style="margin-bottom:30px">		
		<div class="panel panel-default m-bottom-5" id="superquerypanel">
			<div class="panel-heading padding-5-5">
				<label>筛选任务</label>
			</div>
			<div class="panel-body">
				<form role="form" id="queryform">
					<div class="row">
						<div class="col-md-12">
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputmainInstanceId">任务跟踪号:</label>
								<input type="text"  class="form-control" id="mainInstanceId" name="mainInstanceId" placeholder=""/>
							</div>
							<div class="form-group col-md-4  form-inline">
								<label for="exampleInputTasktype">任务类型:</label>
								<select name="tasktype" id="tasktype" class="form-control" style="width:150px;">
									<option value="">全部</option>
									<#list workFlowNodeList as workFlowNode>
										<option value="${workFlowNode.codevalue}">${workFlowNode.codename}</option>
									</#list>
								</select>
							</div>
								<div class="form-group col-md-4 form-inline">
								<label for="exampleInpuTaskstatus">任务状态:</label>
								<select name="taskstatus" id="taskstatus" class="form-control" style="width:150px;">
									<option value="">全部</option>
									<option value="01">正常</option>
									<option value="02">异常</option>
								</select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group col-md-8 form-inline">
								<label for="exampleInputTaskcreatetime">任务创建时间:</label>
									<input class="form-control form_datetime" style="color:black;" type="text" id="taskcreatetimeup" name="taskcreatetimeup" readonly value=""/>
								<label for="exampleInputTaskcreatetime">至</label>
									<input class="form-control form_datetime" style="color:black;" type="text" id="taskcreatetimedown" name="taskcreatetimedown" readonly value=""/>
							</div>
						</div>
					</div>
				</form>
			</div>
	  		
			<div class="panel-footer padding-5-5">
				<div class="row">
					<div class="col-md-12">
						<div class="col-md-12" align="right">
							<button id="querybutton" type="button" name="querybutton" class="btn btn-primary">查询</button>
							<button id="resetbutton" type="button" name="resetbutton" class="btn btn-primary">重置</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="panel panel-default">
			<div class="panel-heading padding-5-5">
				<div class="row">
					<div class="col-md-2">
						<label>任务列表</label>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table id="table-javascript"></table>
				</div>
			</div>
		</div>
		
		<div class="panel-footer padding-5-5">
			<div class="row">
                <div id="styleView" style="float:left;">
                    &nbsp;&nbsp;&nbsp;
                    <button class="btn btn-primary" type="button" name="showWorkflowTrack" id="showWorkflowTrack" title="查看流程轨迹">查看流程轨迹</button>
                </div>

				<div id="style" style="float:left;display:none">
                    &nbsp;&nbsp;&nbsp;
					<button class="btn btn-primary" type="button" id="forceArtificial" title="强制转人工任务">强制转人工任务</button>
				</div>

				<div id="styleA" style="float:left;display:none">
                    &nbsp;&nbsp;&nbsp;
					<button class="btn btn-primary" type="button" id="quietOrder" title="任务取消">任务取消</button>
                    &nbsp;&nbsp;
					<button class="btn btn-primary" type="button" id="refuseInsured" title="拒绝承保">拒绝承保</button>
				</div>

                <div id="styleR" style="float:left;display:none">
                    &nbsp;&nbsp;&nbsp;
                    <button class="btn btn-primary" type="button" id="returnEdit" title="退回修改">退回修改</button>
                </div>

				<div id="styleG" style="float:left;display:none">
                    &nbsp;&nbsp;&nbsp;
					<button class="btn btn-primary" type="button" id="completeOrder" title="任务完成">任务完成</button>
			 	</div>

			 	<div id="styleE" style="float:left;display:none">
                    &nbsp;&nbsp;&nbsp;
					<button class="btn btn-primary" type="button" id="packageOrder" title="打单完成">打单完成</button>
                    &nbsp;&nbsp;
					<button class="btn btn-primary" type="button" id="packagedelivery" title="打单配送成功">打单配送成功</button>
			 	</div>

                <div id="styleViewP" style="float:left;display:none">
                    &nbsp;&nbsp;&nbsp;
                    <button class="btn btn-primary" type="button" id="paymentStatus" title="查询支付状态">查询支付状态</button>
                </div>

			 	<div id="styleB" style="float:left;display:none">
                    &nbsp;&nbsp;&nbsp;
					<button class="btn btn-primary" type="button" id="payFailed" title="支付失败">支付失败</button>
				</div>

				<div id="styleF" style="float:left;display:none">
                    &nbsp;&nbsp;&nbsp;
					<button class="btn btn-primary" type="button" id="paySuccessed" title="支付成功">支付成功</button>
			 	</div>

			 	<div id="styleD" style="float:left;display:none">
                    &nbsp;&nbsp;&nbsp;
					<button class="btn btn-primary" type="button" id="delivery" title="配送成功">配送成功</button>
			 	</div>

			 	<div id="styleC" style="float:left;display:none">
                    &nbsp;&nbsp;&nbsp;
					<button class="btn btn-primary" type="button" id="insuredSuccess" title="承保打单成功">承保打单成功</button>
                    &nbsp;&nbsp;
					<button class="btn btn-primary" type="button" id="deliverySuccess" title="承保打单配送成功">承保打单配送成功</button>
				</div>

				<div id="styleH" style="float:left;display:none">
                    &nbsp;&nbsp;&nbsp;
					<button class="btn btn-primary" type="button" name="togetherWorkflow" id="togetherWorkflow" title="以工作流状态为准">工作状态同步</button>
				</div>
			</div>
		</div>
	
	</div>
</body>
</html>
