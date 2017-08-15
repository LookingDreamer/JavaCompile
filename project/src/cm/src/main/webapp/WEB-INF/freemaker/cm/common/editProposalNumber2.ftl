<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改投保单号弹出窗口</title>
<!--<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>-->
<style type="text/css">
body {font-size: 14px;}
</style>
<script type="text/javascript">
	requirejs([ "cm/common/editProposalNumber2" ],function() {
		require([ "jquery", "bootstrap-table", "bootstrap", "bootstrapTableZhCn","public" ],function($) {
			$(function() {
				initEditProposalnoInfoScript();
			});
		});
	});
</script>
</head>
<body>
	<input id="taskid" type="hidden" value="${taskid}"/>	
	<input id="inscomcode" type="hidden" value="${inscomcode}"/>	
	<input id="num" type="hidden" value="${num}"/>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<h6 class="modal-title">投保单号</h6>
	</div>
	<div class="modal-body">
		<form class="form-inline" role="form" >
			<table class="table table-bordered">
				<tr>
					<td class="col-md-4 active">商业险投保单号：</td>
					<td>
						<input id="biproposalno" style="width:60%;" type="text" value="${biProposalInfo}" <#if !hasbusi>disabled="disabled"</#if>/>
						<#if !hasbusi>没有商业险保单记录</#if>
					</td>
				</tr>
				<tr>
					<td class="active">交强险投保单号：</td>
					<td>
						<input id="ciproposalno" style="width:60%;" type="text" value="${ciProposalInfo}" <#if !hasstr>disabled="disabled"</#if>/>
						<#if !hasstr>没有交强险保单记录</#if>
					</td>
				</tr>
			</table>
			<div class="row" align="right">
					<button class="btn btn-primary" type="button" name="makesure"  
						id="makesure" title="确定">&nbsp;&nbsp;确定&nbsp;&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<button class="btn btn-default" type="button" name="cancel"
						id="cancel" title="取消" data-dismiss="modal">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					
			</div>
		</form>
	</div>
</body>
</html>
