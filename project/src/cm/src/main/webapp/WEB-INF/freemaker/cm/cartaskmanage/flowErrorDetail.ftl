<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>展示错误信息</title>
		<!--<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>-->
		<style type="text/css">
			span.feildtitle {
				font-weight:bold;
				color:#34495E;
			}
			body {font-size: 14px;}
		</style>
		<script type="text/javascript">
			require([ "jquery", "bootstrap-table","bootstrapdatetimepicker" ,"bootstrap",
				 "bootstrapTableZhCn" ,"bootstrapdatetimepickeri18n","public"],	function($) {
			});
		</script>
	</head>
	<body>
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">
				<span aria-hidden="true">&times;</span>
				<span class="sr-only">Close</span>
			</button>
			<h6 class="modal-title">流程错误信息</h6>
		</div>
		<div class="modal-body">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="row">
						<div class="col-md-12">
							<span class="feildtitle">
								任务号：${maininstanceid}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								保险公司： ${inscomName}
							</span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<table class="table table-bordered" style="margin-bottom:0px">
							<tr>
								<th class="col-md-2">任务类型</th>
								<th class="col-md-2">任务状态</th>
								<th class="col-md-1">错误码</th>
								<th class="col-md-4">错误描述</th>
								<th class="col-md-2">创建时间</th>
								<th class="col-md-1">操作人</th>
							</tr>
							<#list errorList as erroritem>
								<tr>
									<td>${erroritem.taskstatus}
										<#if erroritem.firoredi=="1">
											(EDI)
										<#elseif erroritem.firoredi=="0">
											(精灵)
										</#if>
									</td>
									<td>${erroritem.flowname}</td>
									<td>${erroritem.errorcode}</td>
									<#if (erroritem.errordesc?length) gt 64><td><span title='${erroritem.errordesc}'>${erroritem.errordesc?substring(0,63)}...</span></td>
									<#else><td>${erroritem.errordesc}</td></#if>
									<td>
										<#if (erroritem.createtime)??>
											${erroritem.createtime?datetime?string("yyyy-MM-dd HH:mm:ss")}
										<#else>
											未知时间
										</#if>
									</td>
									<td>${erroritem.operator}</td>
								</tr>
							</#list>
						</table>
					</div>
				</div>
			</div>
	
			<div class="row">
			 	<div class="col-md-6">
			 	</div>
			 	<div class="col-md-6" align="right">
			 		<button class="btn btn-default" type="button" title="关闭" data-dismiss="modal">&nbsp;&nbsp;关闭&nbsp;&nbsp;</button>
			 	</div>
			</div>
		</div>
	</body>
</html>
