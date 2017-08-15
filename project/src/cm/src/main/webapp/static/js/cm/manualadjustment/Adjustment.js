require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public"], function ($) {
	$(".mybtn").click(function(){
		var num = $(this).attr("id");
		for(var i=0;i<3;i++){
			if(num=="adjustment"+i){
					//alertmsg($('#Myinstanceid').val());
					//var IntId=$('#instanceid').val();
				//调整完成地址
					$.ajax({
						url : '/cm/business/manualadjustment/finashAdjust',
						type : 'POST',
						dataType : 'html',
						data :{"instanceid":$('#Myinstanceid').val(),"result":"3"},
						cache : false,
						async : true,
						error : function() {
							alertmsg("Connection error");
						},
						success : function(data) {
							if (data=="success") {
								alertmsg("修改其他信息成功！");
							}else{
								alertmsg("修改失败！请稍后重试！");
							}
						}
					});
			}else if(num=="backEdit"+i){
				//退回修改地址
				$.ajax({
					url : '/cm/business/manualadjustment/finashAdjust',
					type : 'POST',
					dataType : 'html',
					data :{"instanceid":$('#Myinstanceid').val(),"result":"2"},
					cache : false,
					async : true,
					error : function() {
						alertmsg("Connection error");
					},
					success : function(data) {
						if (data=="success") {
							alertmsg("修改其他信息成功！");
						}else{
							alertmsg("修改失败！请稍后重试！");
						}
					}
				});
				
			}else if(num=="refuseInsurance"+i){
				if(window.confirm("确认要拒绝承保？")==true){
					
					//TODO
					//alertmsg("3");
					//拒绝承保地址
					var commitNum=$.trim($(this).attr("id").replace(/refuseInsurance/,""));
	//				var instanceId=$.trim($('#taskid'+commitNum).val());
					var subinstanceId=$.trim($('#subInstanceId'+commitNum).val());
					var inscomcode=$("#inscomcode").val();
						$.ajax({
							url : '/cm/business/manualprice/refuseUnderwrite',
							type : 'POST',
							dataType : 'json',
							contentType:"application/json",
							//data :"{\"instanceid\":\""+instanceId+"\"}",
							data :"{\"maininstanceId\":\""+instanceId+"\",\"subinstanceId\":\""+subinstanceId+"\",\"inscomcode\":\""+inscomcode+"\",\"mainorsub\":\"sub\"}",
							cache : false,
							async : true,
							error : function() {
								alertmsg("Connection error");
							},
							success : function(data) {
								if (data=="success") {
									alertmsg("修改其他信息成功！");
								}else{
									alertmsg("打回任务失败！失败愿意"+data.msg);
								}
							}
						});
				}
					
			}else if(num=="backTask"+i){
				//打回任务地址
//					alertmsg("4");
					var commitNum=$.trim($(this).attr("id").replace(/backTask/,""));
//					var instanceId=$.trim($('#taskid'+commitNum).val());
					var subinstanceId=$.trim($('#subInstanceId'+commitNum).val());
					var inscomcode=$.trim($("#thisInscomcode"+commitNum).val());
						alertmsg("3");
						$.ajax({
							url : '/cm/business/manualprice/releaseTask',
							type : 'POST',
							dataType : 'json',
							contentType:"application/json",
							//data :"{\"instanceid\":\""+instanceId+"\",\"inscomcode\":\""+inscomcode+"\"}",
							data :"{\"instanceId\":\""+subinstanceId+"\",\"instanceType\":\""+"1"+"\",\"inscomcode\":\""+inscomcode+"\"}",
							cache : false,
							async : true,
							error : function() {
								alertmsg("Connection error");
							},
							success : function(data) {
								if (data=="success") {
									alertmsg("修改其他信息成功！");
								}else{
									alertmsg("打回任务失败！失败愿意"+data.msg);
								}
							}
						});
			}
		}
	});
});
//修改后刷新页面方法	

