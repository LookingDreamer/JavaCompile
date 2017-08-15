require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public"], function ($) {
	$(function(){
		if($("#deliverytype").children('option:selected').val()=="0"){
			$(".dediv").hide();
		}else if($("#deliverytype").children('option:selected').val()=="1"){
			$(".indiv").hide();
		}else{
			$(".indiv").hide();
			$(".dediv").hide();
		}
	});
	
	
	//调用配送 
	$(".deliverysuccess").click(function(){
		
		var commitNum = $.trim($(this).attr("id").replace(/deliverysuccess/, ""));
		//var instanceId = $.trim($("#taskid"+commitNum).val());
		var tracenumber = $.trim($("#tracenumber"+commitNum).val());
		var comcodevalue = $.trim($("#comcode"+commitNum).val());
		var instanceId = $("#myInstanceId").val();
		var inscomcode = $("#inscomcode").val();
		
		var close = false;
		$.ajax({
			url : "/cm/business/mytask/checkCloseTask",
			type : 'get',
			dataType : "json",
			async: false,
			data: {
				"instanceId":instanceId,
				"providerid":inscomcode
			},
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if(data && data == true) {
						close=true;
				}
			}
		});
		if (close == true) {
			alert("当前任务已关闭或已分配给其他业管处理，请返回我的任务列表");
			backToMyTask();
			return;
		}
		
		//alertmsg(instanceId+"--instanceId---"+comcodevalue+"comcodevalue--commitNum"+commitNum+"tracenumber"+tracenumber);
		var deltype = $("#deliverytype").children('option:selected').val();
//		alert($("#deliverytype").val());
		if($("#deliverytype").val()=="1"){
			var recipientname = $("#recipientname").html();
			var recipientmobilephone = $("#recipientmobilephone").html();
			var recipientaddress = $("#recipientaddress").html();
			var zip = $("#zip").html();
			if(recipientname==""||recipientmobilephone==""||recipientaddress==""||zip==""){
				alertmsg("配送信息不完整");
				return ;
			}
			if(!comcodevalue){
				alertmsg("物流公司不能为空");
				return ;
			}
			if(tracenumber==""||tracenumber==null||comcodevalue==""||comcodevalue==null){
				alertmsg("配送编号不能为空");
				return ;
			}
			
		}
		//防止重复提交
		$(".btn").prop("disabled", true);
		$.ajax({
			url : '/cm/business/ordermanage/getDeliverySuccess',
			type : 'POST',
			dataType : 'text',
			data : "{\"processinstanceid\":\""+instanceId+"\",\"inscomcode\":\""+inscomcode+"\",\"tracenumber\":\""+tracenumber+"\",\"codevalue\":\""+comcodevalue+"\",\"deltype\":\""+deltype+"\"}",
			contentType: 'application/json',
			cache : false,
			async : true,
			error : function() {
				//防止重复提交
				$(".btn").prop("disabled", false);
				alertmsg("调用失败");
			},
			success : function(data) {
				if (data=="success") {
					alertmsg("配送成功！");
					//调用保存配送编号接口,三个参数
//					$.ajax({
//						url : '/cm/business/ordermanage/saveTracenumber',
//						type : 'POST',
//						dataType : 'text',
//						data : "{\"processinstanceid\":\""+instanceId+"\",\"tracenumber\":\""+tracenumber+"\",\"codevalue\":\""+comcodevalue+"\",\"inscomcode\":\""+inscomcode+"\",\"deltype\":\""+deltype+"\"}",
//						contentType: 'application/json',
//						cache : false,
//						async : true,
//						error : function() {
//							//防止重复提交
//							$(".btn").prop("disabled", false);
//							alertmsg("调用失败保存数据");
//						},
//						success : function(data) {
//							if (data=="success") {
//								alertmsg("保存配送信息成功！",backToMyTask);
//							}else{
//								alertmsg("保存配送信息失败！");
//								//防止重复提交
//								$(".btn").prop("disabled", false);
//							}
//						}
//					});
					if($("#frompage").val()=='orderManage'){
						window.location.href="/cm/business/ordermanage/ordermanagelist";
					}else{
						window.location.href="../mytask/queryTask";
					}
					//跳转到我的任务页面
				}else{
					alertmsg("配送失败！");
				}
				//防止重复提交
				$(".btn").prop("disabled", false);
			}
		});
	
	});
	// 退回任务按钮事件处理
	$(".backTask").on("click", function(e) {
		
		
		//组织传递数据
		var commitNum = $.trim($(this).attr("id").replace(/backTask/, ""));
		var instanceId = $("#myInstanceId").val();
		//var instanceId = $.trim($("#taskid"+commitNum).val());
		//var inscomcode = $.trim($("#thisInscomcode"+commitNum).val());
		var inscomcode = $("#inscomcode").val();
		
		var close = false;
		$.ajax({
			url : "/cm/business/mytask/checkCloseTask",
			type : 'get',
			dataType : "json",
			async: false,
			data: {
				"instanceId":instanceId,
				"providerid":inscomcode
			},
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if(data && data == true) {
						close=true;
				}
			}
		});
		if (close == true) {
			alert("当前任务已关闭或已分配给其他业管处理，请返回我的任务列表");
			backToMyTask();
			return;
		}
		
		//alertmsg(instanceId+"=instanceId+inscomcode-"+inscomcode);
		//防止重复提交
		$(".btn").prop("disabled", true);
		$.ajax({
			url : '/cm/business/manualprice/releaseTask',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			//data :"{\"instanceId\":\""+instanceId+"\",\"inscomcode\":\""+inscomcode+"\"}",
			//instanceType,1主流程，2子流程
			data :"{\"instanceId\":\""+instanceId+"\",\"instanceType\":\""+1+"\",\"inscomcode\":\""+inscomcode+"\"}",
			cache : false,
			async : true,
			error : function() {
				//防止重复提交
				$(".btn").prop("disabled", false);
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg("打回任务操作成功！",backToMyTask);
				}else if (data.status == "fail"){
					//防止重复提交
					$(".btn").prop("disabled", false);
					alertmsg("打回任务操作失败！");
				}
			}
		});
	});
	$("#deliverytype").change(function(){
		var selected = $(this).children('option:selected').val();
		if(selected=="0"){
			$(".indiv").show();
			$(".dediv").hide();
		}else if(selected=="1"){
			$(".dediv").show();
			$(".indiv").hide();
		}else{
			$(".dediv").hide();
			$(".indiv").hide();
		}
	});
	
	function backToMyTask(){
		if($(window.top.document).find("#menu").css("display")=="none"){
			$.cmTaskList('my', '', true);
		}else{
			if($("#frompage").val()=='orderManage'){
				window.location.href="/cm/business/ordermanage/ordermanagelist";
			}else{
				window.location.href = "/cm/business/mytask/queryTask";
			}
		}	
	}
});
