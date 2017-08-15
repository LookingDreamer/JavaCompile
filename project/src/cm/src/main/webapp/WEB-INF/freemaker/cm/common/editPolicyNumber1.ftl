<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改保单号弹出窗口</title>
<!--<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>-->
<style type="text/css">
body {font-size: 14px;} 
</style> 
<script type="text/javascript">
	require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public"], function ($) {
		$(function() {
			function dataBack(){
			}
			$("#makesure").on("click", function (){
				var cival = $.trim($("#cipolicyno").val());
				var bival = $.trim($("#bipolicyno").val());
				var comcode = $.trim($("#inscomcode").val());
				if(cival || bival){
				}else{
					alertmsg("请填写保单号后再点击确定！");
					return;
				}
				
				//防止重复提交
				$(this).prop("disabled", true);
				$.ajax({
					url : 'business/ordermanage/editPolicyNumber1',
					type : 'get',
					data : {
					"cipolicyno":cival,
					"bipolicyno":bival,
					"taskid":$("#taskid").val(),
					"inscomcode":$("#inscomcode").val()
					},
					dataType : "json",
					async : true,
					error : function() {
						alertmsg("Connection error");
						//防止重复提交
						$("#makesure").prop("disabled", false);
					},
		            success : function(data){
		            	if(data){
							if (data.status == "success") {
								dataBack();
								$('#xDialog').modal('hide');
							}else if(data.status == "fail"){
								alertmsg(data.msg);
								//防止重复提交
								$("#makesure").prop("disabled", false);
							}
						}else{
							alertmsg("保单号修改失败！");
							//防止重复提交
							$("#makesure").prop("disabled", false);
						}
					}
				});
			})
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
		<h6 class="modal-title">保单号</h6>
	</div>
	<div class="modal-body">
		<form class="form-inline" role="form" >
			<table class="table table-bordered">
				<tr>
					<td class="col-md-4 active">商业险保单号：</td>
					<td>
						<input id="bipolicyno" style="width:60%;" type="text" value="${policyInfo.businessPolicyNum}" <#if !hasbusi>disabled="disabled"</#if>/>
						<#if !hasbusi>没有商业险保单记录</#if>
					</td>
				</tr>
				<tr>
					<td class="active">交强险保单号：</td>
					<td>
						<input id="cipolicyno" style="width:60%;" type="text" value="${policyInfo.strongPolicyNum}" <#if !hasstr>disabled="disabled"</#if>/>
						<#if !hasstr>没有交强险保单记录</#if>
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
