<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务转发弹出窗口</title>
<script type="text/javascript">
	require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn"], function ($) {
	});
	/*function dataBack(){
		var win = $(window.frames[2].document);
		win.find("#businessPolicyNum").text($("#bipolicyno").val());
		win.find("#strongPolicyNum").text($("#cipolicyno").val());
	}*/
	function transformTask(){
		$.ajax({
			url : 'business/mytask/transformTask',
			type : 'get',
			data : {
                "maintaskid":$("#maintaskid").val(),
                "providerid":$("#providerid").val(),
                "tousercode":$("#tousercode").val(),
                "taskcode":$("#taskcode").val(),
                "inscomcode":$('input[name="inscomcode"]:checked').val(),
                "subinstanceid":$("#subinstanceid").val()
			},
			dataType : "html",
			async : true,
			error : function() {
				alert("Connection error");
			},
            success : function(data){
            	if (data == "success") {
            		alert("转发成功！");
            		if($(window.top.document).find("#menu").css("display")=="none"){
	            		$.cmTaskList('my', 'nothing', true);
					}else{
						window.frames[2].location.reload();
					}
            		//location.href="/cm/business/mytask/queryTask";
				}else{
					alert("转发失败！请稍后重试！");
				}
			}
		});
	}
</script>

</head>
<body>
	<input id="taskid" type="hidden" value="${taskid}"/>	
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<h4 class="modal-title">任务转发</h4>
	</div>
	<div class="modal-body">
		<form class="form-inline" role="form" >
			<table class="table table-bordered">
				<tr>
					<td class="col-md-2 active">保险公司: </td>
					<td>
						<#list providerList as provider>
							<input type="radio" class="inscomcode" name="inscomcode" value="${provider.prvcode}"/> ${provider.prvshotname}&nbsp;
						</#list>
					</td>
				</tr>	
				<tr>
					<td class="col-md-2 active">业管编号：</td>
					<td>
						<input id="tousercode" type="text"/>
						<input id="maintaskid" type="hidden" value="${maintaskid}"/>
						<input id="providerid" type="hidden" value="${providerid}"/>
						<input id="taskcode" type="hidden" value="${taskcode}"/>
                        <input id="subinstanceid" type="hidden" value="${subinstanceid}"/>
					</td>
				</tr>
			</table>
			<div class="row">
				<div class="col-md-6">
					<button class="btn btn-default" type="button" name="makesure" onclick="transformTask();"
						id="makesure" title="确定" data-dismiss="modal">&nbsp;&nbsp;确定&nbsp;&nbsp;</button>
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
