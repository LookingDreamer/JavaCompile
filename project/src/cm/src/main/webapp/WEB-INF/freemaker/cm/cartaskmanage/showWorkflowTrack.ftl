<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>车险任务轨迹信息</title>
<style type="text/css">
	span.feildtitle {
		font-weight:bold;
		color:#34495E;
	}
	
	span.indexIcon{
		width:1px;
		position:relative;
		top:3px;
		left:1px;
	}
	body {font-size: 14px;}
</style>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<h6 class="modal-title">任务流程轨迹信息：</h6>
	</div>
	<!--任务流程轨迹信息-->
	<div class="modal-body">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="row">
					<div class="col-md-12">
						<span class="feildtitle">任务跟踪号：${workflowTrackInfo.instanceId}</span>&nbsp;&nbsp;
						<span class="feildtitle">保险公司：${workflowTrackInfo.inscomName}</span>
					</div>
				</div>
			</div>
			<div class="panel-body">
				<#list workflowTrackInfo.workflowTrackList as workflowTrackItem>
					<div class="row">
						<div class="col-md-1">
							${workflowTrackItem_index+1}
							<span class="glyphicon glyphicon-menu-right indexIcon"></span>
						</div>
						<div class="col-md-11">
							<#if (workflowTrackItem.createtime)??>
	 							${workflowTrackItem.createtime?datetime?string("yyyy-MM-dd HH:mm:ss")}：
	 						</#if>
							${workflowTrackItem.taskName}&nbsp;&nbsp;
							<span style="color:#6A5ACD;">(${workflowTrackItem.opPerson})</span>
						</div>
						<#if (workflowTrackItem.noti)?? &&(workflowTrackItem.taskcode)==37>
							<div class="col-md-1"></div>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;原因：
							<span style="color:#6A5ACD;">${workflowTrackItem.noti}</span>
							&nbsp;&nbsp;
						</#if>
					</div>
				</#list>
			</div>
		</div>
		<div class="row">
		 	<div class="col-md-6"></div>
		 	<div class="col-md-6" align="right">
		 		<button class="btn btn-default" type="button" title="关闭" data-dismiss="modal">&nbsp;&nbsp;关闭&nbsp;&nbsp;</button>
		 	</div>
		</div>
	</div>
</body>
</html>
