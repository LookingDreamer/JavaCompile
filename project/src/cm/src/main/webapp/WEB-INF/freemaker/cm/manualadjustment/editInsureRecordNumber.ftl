<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>修改投保单号弹出窗口</title>
	<!--引入修改投保单号弹出窗口js文件-->
	<script type="text/javascript">
		requirejs([ "cm/manualadjustment/editInsureRecordNumber" ],function() {
			require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public"], function ($) {
				$(function() {
					initEditInsureRecordNumberScript();
				});
			});
		});
	</script>
</head>
<body>
	<!--修改投保单号弹出框-->
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		<h4 class="modal-title">投保单号</h4>
	</div>
	<div class="modal-body">
		<form class="form-inline" role="form" id="editInsureRecordNumberForm">
			<!--任务编码-->
			<input type="hidden" id="taskid" name="taskid" value="${taskid}"/>
		  <table class="table table-bordered">
			<tr>
				<td class="active col-md-4">商业险投保单号：</td>
				<td class="col-md-8">
					<input type="text" id="businessPolicyNum" name="businessPolicyNum" class="form-control" value="${proposalInfo.businessProposalFormNo!""}" placeholder="">
				</td>
			</tr>
			<tr>
				<td class="active">交强险投保单号：</td>
				<td>
					<input type="text" id="strongPolicyNum" name="strongPolicyNum" class="form-control" value="${proposalInfo.businessPolicyNum!""}" placeholder="">
				</td>
			</tr>
			<tr>
				<td class="active">支付号：</td>
				<td>
					<input type="text" id="payNum" name="payNum" class="form-control" value="${paymentinfo.insurecono!""}" placeholder="">
				</td>
			</tr>
			<tr>
				<td class="active">校验码：</td>
				<td>
					<input type="text" id="checkCode" name="checkCode" class="form-control" value="${paymentinfo.checkcode!""}" placeholder="">
				</td>
			</tr>
		  </table>
		  <div class="row">
		 	 <div class="col-md-8">
		 		<button class="btn btn-default" type="button" name="makesure" id="makesure" title="确定">&nbsp;&nbsp;确定&nbsp;&nbsp;</button>
		 		<!--<div class="checkbox">
					<label>
						<input type="checkbox" id="underwrited" name="underwrited" value="1">
						已在保险公司核保通过
					</label>
				</div>-->
		 	 </div>
		 	 <div class="col-md-4" align="right">
		 		<button class="btn btn-default" type="button" name="cancel" id="cancel" title="取消" data-dismiss="modal">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>
		 	 </div>
		  </div>
		</form>
	</div>
</body>
</html>
