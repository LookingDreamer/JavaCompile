require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public"], function ($) {
	$(".mybtn").click(function(){
		var num = $(this).attr("id");
		for(var i=0;i<3;i++){
			if(num=="adjustment"+i){
					alertmsg($('#Myinstanceid').val());
					//var IntId=$('#instanceid').val();
				//调整完成地址
					$.ajax({
						url : '/cm/business/manualadjustment/finashAdjust',
						type : 'POST',
						dataType : 'html',
						data :{"instanceid":$('#Myinstanceid').val()},
						cache : false,
						async : true,
						error : function() {
							alertmsg("Connection error");
						},
						success : function(data) {
							if (data=="true") {
								alertmsg("修改其他信息成功！");
							}else{
								alertmsg("修改失败！请稍后重试！");
							}
						}
					});
			}else if(num=="backEdit"+i){
				//退回修改地址
				
					alertmsg("2");
				
			}else if(num=="refuseInsurance"+i){
				
				if(window.confirm("确认要拒绝承保吗？")==true){
				//拒绝承保地址 TODO
					var commitNum=$.trim($(this).attr("id").replace(/refuseInsurance/,""));
					var instanceId=$.trim($('#taskid'+commitNum).val());
						alertmsg("3");
						$.ajax({
							url : '/cm/business/manualprice/refuseUnderwrite',
							type : 'POST',
							dataType : 'text',
							contentType:"application/json",
							data :"{\"instanceid\":\""+instanceId+"\"}",
							cache : false,
							async : true,
							error : function() {
								alertmsg("Connection error");
							},
							success : function(data) {
								if (data=="true") {
									alertmsg("修改其他信息成功！");
								}else{
									alertmsg("修改失败！请稍后重试！");
								}
							}
						});
				}
			}else if(num=="backTast"+i){
				//打回任务地址
					alertmsg("4");
					var commitNum=$.trim($(this).attr("id").replace(/refuseInsurance/,""));
					var instanceId=$.trim($('#taskid'+commitNum).val());
					var inscomcode=$.trim($("#thisInscomcode"+commitNum).val()).
						alertmsg("3");
						$.ajax({
							url : '/cm/business/manualprice/releaseTask',
							type : 'POST',
							dataType : 'text',
							contentType:"application/json",
							data :"{\"instanceid\":\""+instanceId+"\",\"inscomcode\":\""+inscomcode+"\"}",
							cache : false,
							async : true,
							error : function() {
								alertmsg("Connection error");
							},
							success : function(data) {
								if (data=="true") {
									alertmsg("修改其他信息成功！");
								}else{
									alertmsg("修改失败！请稍后重试！");
								}
							}
						});
			}
		}
	});
});
//修改后刷新页面方法	
function reloadInfo() {
	location.reload();
	}
