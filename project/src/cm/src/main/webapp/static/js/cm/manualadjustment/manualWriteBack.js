require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public"], function ($) {
	// 人工回写完成通过按钮事件处理
	$(".adjustment").on("click", function(e) {
		//组织传递数据
		var commitNum = $.trim($(this).attr("id").replace(/adjustment/, ""));
		var instanceId = $.trim($("#taskid"+commitNum).val());
		//子流程
		var subInstanceId = $.trim($("#subInstanceId"+commitNum).val());
		
		//alertmsg(instanceId+"--instanceId"+"subInstanceId"+subInstanceId);
		$.ajax({
			url : '/cm/business/ordermanage/manualWriteBackSuccess',
			type : 'POST',
			dataType : 'text',
			contentType: "application/json",
			data : "{\"processinstanceid\":\""+subInstanceId+"\"}",
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data == "success") {
					alertmsg("人工回写操作成功！");
					window.location.href = "/cm/business/mytask/queryTask";
				}else {
					alertmsg("人工回写操作失败！");
				}
			}
		});
	});
	
	
	// 人工回写中 退回修改按钮  事件处理
	$(".backEdit").on("click", function(e) {
		//组织传递数据
		var commitNum = $.trim($(this).attr("id").replace(/backEdit/, ""));
		var instanceId = $.trim($("#taskid"+commitNum).val());
		//子流程
		var subInstanceId = $.trim($("#subInstanceId"+commitNum).val());
		//alertmsg(instanceId+"--taskid");
		$.ajax({
			url : '/cm/business/ordermanage/manualWriteBack',
			type : 'POST',
			dataType : 'text',
			contentType: "application/json",
			data : "{\"processinstanceid\":\""+subInstanceId+"\"}",
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data == "success") {
					alertmsg("人工回写退回修改操作成功！");
					window.location.href = "/cm/business/mytask/queryTask";
				}else {
					alertmsg("人工回写退回修改操作失败！");
				}
			}
		});
	});
	
	// 人工回写拒绝承保按钮事件处理
	$(".refuseInsurance").on("click", function(e) {
		
		if(window.confirm("确认要拒绝承保吗？")==true){
			
		//组织传递数据
		var commitNum = $.trim($(this).attr("id").replace(/refuseInsurance/, ""));
		var instanceId = $.trim($("#taskid"+commitNum).val());
		var inscomcode = $.trim($("#thisInscomcode"+commitNum).val());
		
		//子流程
		var subInstanceId = $.trim($("#subInstanceId"+commitNum).val());
		//alertmsg(instanceId+"--instanceId+inscomcode"+inscomcode);
		$.ajax({
			url : '/cm/business/manualprice/refuseUnderwrite',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			data : "{\"maininstanceId\":\""+instanceId+"\",\"subinstanceId\":\""+subInstanceId+"\",\"inscomcode\":\""+inscomcode+"\",\"mainorsub\":\"sub\"}",
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg("拒绝承保操作成功！");
					window.location.href = "/cm/business/mytask/queryTask";
				}else if (data.status == "fail"){
					alertmsg("拒绝承保操作失败！失败原因："+data.msg);
				}
			}
		});
	}
	});
	
	//打回任务
	$(".backTask").on("click", function(e) {
		//组织传递数据
		var commitNum = $.trim($(this).attr("id").replace(/backTask/, ""));
		var instanceId = $.trim($("#taskid"+commitNum).val());
		var inscomcode = $.trim($("#thisInscomcode"+commitNum).val());
		
		//子流程
		var subInstanceId = $.trim($("#subInstanceId"+commitNum).val());
		//alertmsg(instanceId+"--instanceId+inscomcode"+inscomcode);
		$.ajax({
			url : '/cm/business/manualprice/releaseTask',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			//data :"{\"instanceId\":\""+instanceId+"\",\"inscomcode\":\""+inscomcode+"\"}",
			//instanceType,1主流程，2子流程
			data :"{\"instanceId\":\""+subInstanceId+"\",\"instanceType\":\""+2+"\",\"inscomcode\":\""+inscomcode+"\"}",
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg("打回任务操作成功！");
					if($(window.top.document).find("#menu").css("display")=="none"){
	            		$.cmTaskList('my', '', true);
					}else{
						window.location.href = "/cm/business/mytask/queryTask";
					}	
				}else if (data.status == "fail"){
					alertmsg("打回任务操作失败！");
				}
			}
		});
	});
});
