<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改配送信息弹出窗口</title>
<!--<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>-->
<style type="text/css">
body {font-size: 14px;}
</style>
<script type="text/javascript">
	requirejs([ "cm/ordermanage/editDeliveryInfo" ], function() {
		require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public"],function($) {
			$(function() {
				initEditDeliveryInfoScript();
			});
		});
	});
</script>
</head>
<body>
	<input id="taskid" type="hidden" value="${taskid}"/>	
	<input id="inscomcode" type="hidden" value="${inscomcode}"/>	
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<h6 class="modal-title">配送信息修改</h6>
	</div>
	<div class="modal-body">
		<form class="form-inline" role="form" >
			<table class="table table-bordered">
				<tr>
					<td class="col-md-2 active">收件人:</td>
					<td class="col-md-4">
						<input id="erecipientname" class="form-control" type="text" value="${orderDelivery.recipientname}"/>
					</td>
					<td class="col-md-2 active">收件人手机:</td>
					<td class="col-md-4">
						<input id="erecipientmobilephone" class="form-control" type="text" value="${orderDelivery.recipientmobilephone}"/>
					</td>
				</tr>
				<tr>
					<td class="active">邮政编码:</td>
					<td>
						<input id="ezip" class="form-control" type="text" value="${orderDelivery.zip}"/>
					</td>
					<td class="active"></td>
					<td></td>
				</tr>
				<tr>
					<td class="active" colspan="4">收件人省市区:</td>
				</tr>
				<tr>
					<td colspan="4">
						<select name="province" id="province" class="form-control" style="width:30%">
							<option value="">请选择</option>
							<#list province as pr>
								<option value="${pr.comcode}" <#if pr.comcode == orderDelivery.recipientprovince>selected="selected"</#if>>
									${pr.comcodename}
								</option>
							</#list>
						</select>
						<select name="city" id="city" class="form-control" style="width:30%">
							<option value="">请选择</option>
							<#list city as ci>
								<option value="${ci.comcode}" <#if ci.comcode == orderDelivery.recipientcity>selected="selected"</#if>>
									${ci.comcodename}
								</option>
							</#list>
						</select>
						<select name="area" id="area" class="form-control" style="width:30%">
							<option value="">请选择</option>
							<#list area as pr>
								<option value="${pr.comcode}" <#if pr.comcode == orderDelivery.recipientarea>selected="selected"</#if>>
									${pr.comcodename}
								</option>
							</#list>
						</select>
					</td>
				</tr>
				<tr>
					<td class="active" colspan="4">收件人详细地址:</td>
				</tr>
				<tr>
					<td colspan="4">
						<textarea id="erecipientaddress" class="form-control" rows="2" style="width:80%">${orderDelivery.recipientaddress}</textarea>
					</td>
				</tr>
			</table>
			<div class="row">
				<div class="col-md-6">
					<button class="btn btn-default" type="button" name="makesure" 
						id="makesure" title="确定">&nbsp;&nbsp;确定&nbsp;&nbsp;</button>
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
