<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>轮询任务详情</title>
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
			<h6 class="modal-title">轮询任务详情</h6>
		</div>
		<div class="modal-body">
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="row">
						<div class="col-md-12">
							<span class="feildtitle">
								任务摘要<br>
								任务号：<#if (data.loopObj)??>${data.loopObj.taskid}</#if><br>
								车牌号：${data.platenumber}<br>
								被保险人：${data.insuredname}<br>
								轮询任务创建时间：<#if (data.loopObj)??>${data.loopObj.taskcreatetime?datetime?string("yyyy-MM-dd HH:mm:ss")}</#if>
							</span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<table class="table table-bordered" style="margin-bottom:0px">
							<#if (data.detailList)??>
								<#list data.detailList as loopDetail>
									<tr>
										<td style="word-break: break-all; word-wrap: break-word">
											${loopDetail.starttime?datetime?string("yyyy-MM-dd HH:mm:ss")}<br>
											第${loopDetail_index+1}次轮询&nbsp;&nbsp;结果：
                                            <#if ("fail"=loopDetail.loopresult)>
                                                失败
                                            <#elseif ("start"=loopDetail.loopresult)>
                                                轮询中
                                            <#else>
                                                成功
                                            </#if><br>
											报错信息：${loopDetail.msg}
										</td>
									</tr>
								</#list>
							</#if>
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
