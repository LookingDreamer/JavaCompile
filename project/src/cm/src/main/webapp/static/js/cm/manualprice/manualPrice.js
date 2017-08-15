var isRequote = null;
var underCount = 0;
require([ "jquery", "bootstrap-table", "bootstrap", "bootstrapTableZhCn","public"],function($) {
	
	 $('body').on("click", ".passInsurancepre" , function(e) {
		var commitNum = $.trim($(this).attr("id").replace(/passInsurancepre/, ""));
		var oDiv = document.getElementById('div'+commitNum);
		oDiv.style.cssText = 'position:absolute; bottom:0px; left:0px; width:200; height:auto; z-index:9999; background:#ccc; display:block;';
		$("#peopleDo"+commitNum).attr("style","display:none");
		$("#passInsurance"+commitNum).attr("style","display:block");
	 });
	 $('body').on("click", ".peopleDopre" , function(e) {
			var commitNum = $.trim($(this).attr("id").replace(/peopleDopre/, ""));
			var oDiv = document.getElementById('div'+commitNum);
			oDiv.style.cssText = 'position:absolute; bottom:0px; left:0px; width:200; height:auto; z-index:9999; background:#ccc; display:block;';
			$("#passInsurance"+commitNum).attr("style","display:none");
			$("#peopleDo"+commitNum).attr("style","display:block");
	 });
	// 人工报价报价通过按钮事件处理 
	 $('body').on("click", ".passInsurance" , function(e) {
		//组织传递数据   
		var commitNum = $.trim($(this).attr("id").replace(/passInsurance/, ""));
		if($("#ischeckedprice").val() == 0){
			alertmsg("没有试算报价，不能报价通过！");
			return;
		}
		var result = false;
		$('.inskindnamecls0').each(function(){
			if('机动车损失保险'==$(this).text()&&($("#basicRiskPremium").text()==""||$("#basicRiskPremium").text().trim()=="")){
				 $("#ischeckedprice").val(0);
				 result = true;
				 return false;
		    }
		});
		if(result){
			alertmsg("机动车车损纯风险基准保费不能为空,请填写之后重新试算");
	    	return;
		}
		var totalAmountprice = Number($("#totalAmountprice"+commitNum).val());//总保费
		if(totalAmountprice == 0){
			alertmsg("总保费为0，不能报价通过！");
			return;
		}
//		var instanceId = $.trim($("#taskid"+commitNum).val());
//		var inscomcode = $.trim($("#thisInscomcode"+commitNum).val());
		var subInstanceId = $.trim($("#subInstanceId"+commitNum).val());
//		alertmsg("{\"instanceId\":\""+subInstanceId+"\",\"quoteResult\":\"3\"}");
//		if($("#isFeeArea").val()=='0'){
//			for(var i=0;i<$("#carInsTaskInfoListSize").val();i++){
//				var totalAmountprice = Number($("#totalAmountprice"+i).val());//总保费
//				if(totalAmountprice == 0){
//					alertmsg($("#inscomname"+i).val()+"保险公司总保费为0，不能报价通过！");
//					return;
//				}
//			}
//		}
		obj = document.getElementsByName("inscomlist"+commitNum);
		var inscomtex = '';
		for(k in obj){
			if(obj[k].checked){
				inscomtex += obj[k].value+',';
			}
		}
		if(!inscomtex){
			alertmsg("请勾选通过报价的保险公司！");
			return;
		}
		var oDiv = document.getElementById('div'+commitNum);
		oDiv.style.cssText = 'position:absolute; bottom:0px; left:0px; width:200; height:auto; z-index:9999; background:#ccc; display:none;';
		//防止重复提交
		$(".btn").prop("disabled", true);
		$.ajax({
			url : '/cm/business/manualprice/quotePricePassOrBackForEdit',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			data : "{\"instanceId\":\""+subInstanceId+"\",\"quoteResult\":\"3\",\"inscomtex\":\""+inscomtex+"\"}",
			cache : false,
			async : true,
			error : function() {
				//防止重复提交
				$(".btn").prop("disabled", false);
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg("报价通过请求成功，请稍后！",backToMyTask);
				}else if (data.status == "fail"){
					//防止重复提交
					$(".btn").prop("disabled", false);
					alertmsg("报价通过操作失败！");
				}
			}
		});
	});
	// 人工报价退回修改按钮事件处理
	$(".backEditInsurance").on("click", function(e) {
		obj = document.getElementsByName("inscomlist");
		var inscomtex = '';
		for(k in obj){
			if(obj[k].checked){
				inscomtex += obj[k].value+',';
			}
		}
		if(!inscomtex){ 
			alertmsg("请勾选退回修改的保险公司！");
			return;
		}
		//组织传递数据
		var commitNum = $.trim($(this).attr("id").replace(/backEditInsurance/, ""));
		var subInstanceId = $.trim($("#subInstanceId"+commitNum).val());
//		alertmsg("{\"instanceId\":\""+subInstanceId+"\",\"quoteResult\":\"2\"}");
		var userRemarkPageInfo = $.trim($("#userRemarkPageInfo"+commitNum).text());
		//alertmsg(instanceId+"--instanceId"+userRemarkPageInfo);
		if(userRemarkPageInfo==""||userRemarkPageInfo==null){
			alertmsg("请填写给用户的备注信息");
			return;
		}
		if($("#isFeeArea").val()=='0'){
			for(var i=0;i<$("#carInsTaskInfoListSize").val();i++){
				var userRemarkPageInfo = $.trim($("#userRemarkPageInfo"+i).text());
				if(userRemarkPageInfo==""||userRemarkPageInfo==null){
					alertmsg($("#inscomname"+i).val()+"保险公司,请填写给用户的备注信息");
					return;
				}
			}
		}
			//防止重复提交
			$(".btn").prop("disabled", true);
			$.ajax({
				url : '/cm/business/manualprice/quotePricePassOrBackForEdit',
				type : 'POST',
				dataType : 'json',
				contentType: "application/json",
				data :"{\"instanceId\":\""+subInstanceId+"\",\"quoteResult\":\"2\",\"inscomtex\":\""+inscomtex+"\"}",
				cache : false,
				async : true,
				error : function() {
					//防止重复提交
					$(".btn").prop("disabled", false);
					alertmsg("Connection error");
				},
				success : function(data) {
					if (data.status == "success") {
						alertmsg("退回修改操作成功！",backToMyTask);
//						if($(window.top.document).find("#menu").css("display")=="none"){
//		            		$.cmTaskList('my', '', true);
//						}else{
//							window.location.href = "/cm/business/mytask/queryTask";
//						}	
					}else if (data.status == "fail"){
						//防止重复提交
						$(".btn").prop("disabled", false);
						alertmsg("退回修改操作失败！");
					}
				}
			});
		
	});
	// 人工报价转人工处理按钮事件处理
	$('body').on("click", ".peopleDo" , function(e) {
		//组织传递数据
		var commitNum = $.trim($(this).attr("id").replace(/peopleDo/, ""));
		obj = document.getElementsByName("inscomlist"+commitNum);
		var inscomtex = '';
		for(k in obj){
			if(obj[k].checked){
				inscomtex += obj[k].value+',';
			}
		}
		if(!inscomtex){
			alertmsg("请勾选转人工修改的保险公司！");
			return;
		}
		
		var subInstanceId = $.trim($("#subInstanceId"+commitNum).val());
//		alertmsg("{\"instanceId\":\""+subInstanceId+"\",\"quoteResult\":\"1\"}");
		var inscomcode = $.trim($("#inscomcode"+commitNum).val());
        var elfquoteFlag = $("#elfquoteFlag").val();//是否有精灵报价能力

        if (inscomcode.substring(0, 4) == "2021" && inscomcode.indexOf("20214401") == -1 && inscomcode.indexOf("20214409") == -1 &&
            inscomcode.indexOf("20214418") == -1&&elfquoteFlag=="1") {
			$("*[id^='xDialog']").modal('hide');
			var temp = Math.floor(Math.random()*1000);
			var dialog = $('<div class="modal fade" id="xDialog11' + temp + '" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"><div class="modal-dialog" style="width:90%;position:absolute;top:100px;z-index:9991;"><div class="modal-content" id="modal-content">'
					+ '<label for="exampleInputCode" style="padding:8px 5px 5px 20px;font-family:microsoft yahei;">大地加密狗密钥：</label><input type="text" class="form-control m-left-5" id="dadikey11' + temp + '" name="dadikey11' + temp + '" placeholder="" style="width:95%">'
					+ '<div class="modal-footer"><a id="savedadikey11' + temp + '" class="btn btn-success">保存</a><a id="closebutton11' + temp + '" data-dismiss="modal" name="closebutton" class="btn btn-success">关闭</a></div>'
					+ '</div></div></div>');
			dialog.appendTo(document.body).modal({
				toggle : "modal",
				top: "500px",
				width : "720px",
				backdrop : false,
				keyboard : true
			});
			$("#savedadikey11" + temp).on("click", function(e) {
				var keyinfo = $("#dadikey11" + temp).val();
				if(keyinfo!=''){
					$.ajax({
						url : '/cm/interface/savedadikey?taskid=' + subInstanceId + "&inscomcode=" + inscomcode + "&dadikey=" + $("#dadikey11" + temp).val(),
						type : 'POST',
						dataType : 'json',
						contentType: "application/json",
						cache : false,
						async : false, 
						error : function() {
							alertmsg("Connection error");
						},
						success : function(data) {
							if (data=='success') {
								$('#xDialog11' + temp).modal('hide');
								//防止重复提交
								$(".btn").prop("disabled", true);
								$.ajax({
									url : '/cm/business/manualprice/quotePricePassOrBackForEdit',
									type : 'POST',
									dataType : 'json',
									contentType: "application/json",
									data :"{\"instanceId\":\""+subInstanceId+"\",\"quoteResult\":\"1\",\"inscomtex\":\""+inscomtex+"\"}",
									cache : false,
									async : true,
									error : function() {
										//防止重复提交
										$(".btn").prop("disabled", false);
										alertmsg("Connection error");
									},
									success : function(data) {
										if (data.status == "success") {
											alertmsg("转人工处理操作成功！",backToMyTask);
//											if($(window.top.document).find("#menu").css("display")=="none"){
//							            		$.cmTaskList('my', '', true);
//											}else{
//												window.location.href = "/cm/business/mytask/queryTask";
//											}	
										}else if (data.status == "fail"){
											//防止重复提交
											$(".btn").prop("disabled", false);
											alertmsg("转人工处理操作失败！");
										}
									}
								});
							}else{
								alertmsg("保存失败");
							}
						}
					});
				}else{
					alertmsg("请填写大地加密狗密钥...");
				}
			});
		} else{
			//防止重复提交
			$(".btn").prop("disabled", true);
			$.ajax({
				url : '/cm/business/manualprice/quotePricePassOrBackForEdit',
				type : 'POST',
				dataType : 'json',
				contentType: "application/json",
				data :"{\"instanceId\":\""+subInstanceId+"\",\"quoteResult\":\"1\",\"inscomtex\":\""+inscomtex+"\"}",
				cache : false,
				async : true,
				error : function() {
					//防止重复提交
					$(".btn").prop("disabled", false);
					alertmsg("Connection error");
				},
				success : function(data) {
					if (data.status == "success") {
						alertmsg("转人工处理操作成功！",backToMyTask);
//						if($(window.top.document).find("#menu").css("display")=="none"){
//		            		$.cmTaskList('my', '', true);
//						}else{
//							window.location.href = "/cm/business/mytask/queryTask";
//						}	
					}else if (data.status == "fail"){
						//防止重复提交
						$(".btn").prop("disabled", false);
						alertmsg("转人工处理操作失败！");
					}
				}
			});
		}
	});
	// 人工报价拒绝承保按钮事件处理
	$(".refuseInsurance").on("click", function(e) {
		
		//组织传递数据
		var commitNum = $.trim($(this).attr("id").replace(/refuseInsurance/, ""));
		var instanceId = $.trim($("#taskid"+commitNum).val());
		var inscomcode = $.trim($("#thisInscomcode"+commitNum).val());
		var subInstanceId = $.trim($("#subInstanceId"+commitNum).val());
		

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
    		return;
    	}
		
		if(window.confirm("确认要拒绝承保吗？")==true){
			
//		alertmsg("{\"maininstanceId\":\""+instanceId+"\",\"subinstanceId\":\""+subInstanceId+"\",\"inscomcode\":\""+inscomcode+"\",\"mainorsub\":\"sub\"}");
		var userRemarkPageInfo = $.trim($("#userRemarkPageInfo"+commitNum).text());
		if(userRemarkPageInfo==""||userRemarkPageInfo==null){
			alertmsg("请填写给用户的备注信息");
			return;
		}
		if($("#isFeeArea").val()=='0'){
			for(var i=0;i<$("#carInsTaskInfoListSize").val();i++){
				var userRemarkPageInfo = $.trim($("#userRemarkPageInfo"+i).text());
				if(userRemarkPageInfo==""||userRemarkPageInfo==null){
					alertmsg($("#inscomname"+i).val()+"保险公司,请填写给用户的备注信息");
					return;
				}
			}
		}
			//防止重复提交
			$(".btn").prop("disabled", true);
			$.ajax({
				url : '/cm/business/manualprice/quoteRefuseUnderwrite',
				type : 'POST',
				dataType : 'json',
				contentType: "application/json",
				data :"{\"maininstanceId\":\""+instanceId+"\",\"subinstanceId\":\""+subInstanceId+"\",\"inscomcode\":\""+inscomcode+"\",\"mainorsub\":\"sub\"}",
				cache : false,
				async : true,
				error : function() {
					//防止重复提交
					$(".btn").prop("disabled", false);
					alertmsg("Connection error");
				},
				success : function(data) {
					if (data.status == "success") {
						alertmsg("拒绝承保操作成功！",backToMyTask);
//						if($(window.top.document).find("#menu").css("display")=="none"){
//							$.cmTaskList('my', '', true);
//						}else{
//							window.location.href = "/cm/business/mytask/queryTask";
//						}	
					}else if (data.status == "fail"){
						//防止重复提交
						$(".btn").prop("disabled", false);
						alertmsg("拒绝承保操作失败！");
					}
				}
			});
	}
	});
	// 人工报价打回任务按钮事件处理
	$('body').on("click", ".backTask" , function(e) {
		//组织传递数据
		var commitNum = $.trim($(this).attr("id").replace(/backTask/, ""));
		var instanceId = $.trim($("#taskid"+commitNum).val());
		var inscomcode = $.trim($("#thisInscomcode"+commitNum).val());
		var subInstanceId = $.trim($("#subInstanceId"+commitNum).val());
		

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
    		return;
    	}
		
//		alertmsg("{\"instanceId\":\""+subInstanceId+"\",\"instanceType\":\""+2+"\",\"inscomcode\":\""+inscomcode+"\"}");
		//防止重复提交
		$(".btn").prop("disabled", true);
		$.ajax({
			url : '/cm/business/manualprice/quoteReleaseTask',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			//instanceId:(主或子)流程实例id，instanceType：1:主 2：子，inscomcode：保险公司code
			data :"{\"instanceId\":\""+subInstanceId+"\",\"instanceType\":\""+2+"\",\"inscomcode\":\""+inscomcode+"\"}",
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
//					if($(window.top.document).find("#menu").css("display")=="none"){
//	            		$.cmTaskList('my', '', true);
//					}else{
//						window.location.href = "/cm/business/mytask/queryTask";
//					}	
				}else if (data.status == "fail"){
//					alertmsg("打回任务操作失败！失败原因："+data.msg);
					//防止重复提交
					$(".btn").prop("disabled", false);
					alertmsg("打回任务操作失败！");
				}
			}
		});
	});
	
	function backToMyTask(){
		if($(window.top.document).find("#menu").css("display")=="none"){
			$.cmTaskList('my', '', true);
		}else{
			window.location.href = "/cm/business/mytask/queryTask";
		}	
	}
	
	
	//报价回写
	$(".reQuoteBack").on("click", function(){
		var commitNum = $.trim($(this).attr("id").replace(/reQuoteBack/, ""));
		var instanceId = $.trim($("#taskid"+commitNum).val());
		var subInstanceId = $.trim($("#subInstanceId"+commitNum).val());
		var inscomcode = $.trim($("#inscomcode"+commitNum).val());
		

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
    		return;
    	}
		
		var hasbusi = $.trim($("#hasbusi"+commitNum).val());
		var hasstr = $.trim($("#hasstr"+commitNum).val());
		var businessProposalFormNo = $.trim($("#businessProposalFormNo"+commitNum).text());
		var strongProposalFormNo = $.trim($("#strongProposalFormNo"+commitNum).text());
		isRequote = Number($.trim($("#isRequote"+commitNum).val()));
//		alert(hasbusi+","+hasstr);
//		alert(subInstanceId+",s"+inscomcode+",ins"+hasbusi+"-hasbusi--"+hasstr+"-hasstr-"+businessProposalFormNo+","+strongProposalFormNo);
		if(hasbusi=="true" && hasstr=="true"){
			if(strongProposalFormNo==""||businessProposalFormNo==""){
				alertmsg("交强险或商业险投保单号不能为空");
				return ;
			}else{
			}
		}else{
			if(hasbusi=="true" && hasstr=="false"){
				if(businessProposalFormNo==""){
					alertmsg("商业险投保单号不能为空");
					return ;
				}else{}
			}else if(hasbusi=="false" && hasstr=="true"){
				if(strongProposalFormNo==""){
					alertmsg("交强险投保单号不能为空");
					return ;
				}else{}
			}
			
		}
		if($("#isFeeArea").val()=='0'){
			for(var i=0;i<$("#carInsTaskInfoListSize").val();i++){
				var hasbusi=$("#hasbusi"+i).val().trim();
				var hasstr=$("#hasstr"+i).val().trim();
				var businessProposalFormNo = $.trim($("#businessProposalFormNo"+i).text());
				var strongProposalFormNo = $.trim($("#strongProposalFormNo"+i).text());
				if(hasbusi=="true" && hasstr=="true"){
					if(businessProposalFormNo==""||strongProposalFormNo==""){
						alert($("#inscomname"+i).val()+"保险公司交强险或商业险投保单号不能为空");
						return;
					}
				}else{
					if(hasbusi=="true" && hasstr=="false"){
						if(businessProposalFormNo==""){
							alert($("#inscomname"+i).val()+"保险公司,商业险投保单号不能为空");
							return;
						}
					}else if(hasbusi=="false" && hasstr=="true"){
						if(strongProposalFormNo==""){
							alert($("#inscomname"+i).val()+"保险公司,交强险投保单号不能为空");
							return;
						}
					}else{}
				}
			}
		}
        var elfquoteFlag = $("#elfquoteFlag").val();//是否有精灵报价能力

        if (inscomcode.substring(0, 4) == "2021" && inscomcode.indexOf("20214401") == -1 && inscomcode.indexOf("20214409") == -1 &&
            inscomcode.indexOf("20214418") == -1&&elfquoteFlag=="1") {
			$("*[id^='xDialog']").modal('hide');
			var temp = Math.floor(Math.random()*1000);
			var dialog = $('<div class="modal fade" id="xDialog4' + temp + '" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"><div class="modal-dialog" style="width:90%;position:absolute;top:100px;z-index:9991;"><div class="modal-content" id="modal-content">'
					+ '<label for="exampleInputCode" style="padding:8px 5px 5px 20px;font-family:microsoft yahei;">大地加密狗密钥：</label><input type="text" class="form-control m-left-5" id="dadikey4' + temp + '" name="dadikey4' + temp + '" placeholder="" style="width:95%">'
					+ '<div class="modal-footer"><a id="savedadikey4' + temp + '" class="btn btn-success">保存</a><a id="closebutton4' + temp + '" data-dismiss="modal" name="closebutton" class="btn btn-success">关闭</a></div>'
					+ '</div></div></div>');
			dialog.appendTo(document.body).modal({
				toggle : "modal",
				top: "500px",
				width : "720px",
				backdrop : false,
				keyboard : true
			});
			$("#savedadikey4" + temp).on("click", function(e) {
				var keyinfo = $("#dadikey4" + temp).val();
				if(keyinfo!=''){
					$.ajax({
						url : '/cm/interface/savedadikey?taskid=' + subInstanceId + "&inscomcode=" + inscomcode + "&dadikey=" + $("#dadikey4" + temp).val(),
						type : 'POST',
						dataType : 'json',
						contentType: "application/json",
						cache : false,
						async : false, 
						error : function() {
							alertmsg("Connection error");
						},
						success : function(data) {
							if (data=='success') {
								$('#xDialog4' + temp).modal('hide');
								//防止重复提交
								$(".btn").prop("disabled", true);
								underCount += 1;
								reQuoteInfo(instanceId,subInstanceId,inscomcode);
							}else{
								alertmsg("保存失败");
							}
						}
					});
				}else{
					alertmsg("请填写大地加密狗密钥...");
				}
			});
		} else {
			//防止重复提交
			$(".btn").prop("disabled", true);
			underCount += 1;
			reQuoteInfo(instanceId,subInstanceId,inscomcode);
		}
	});
	
});

//修改后刷新页面方法	
function reloadInfo() {
	location.reload();
}
var resultCount = 0;

function reQuoteInfo(instanceId,subInstanceId,inscomcode){
	//防止重复提交
	$(".btn").prop("disabled", true);
	$.ajax({
		url : '/cm/business/ordermanage/reQuoteBack',
		type : 'GET',
		dataType : 'json',
		data : "maininstanceId="+instanceId+"&subInstanceId="+subInstanceId+"&inscomcode="+inscomcode+"&underCount="+underCount,
		cache : false,
		async : true,
		error : function() {
			//防止重复提交
			$(".btn").prop("disabled", false);
			alertmsg("Connection error");
		},
		success : function(data) {
			if(data.status == "success"){
				alertmsgno(data.msg);
				//成功后操作
				//刷新页面，
				intervalCount = setInterval("getResult("+instanceId+","+subInstanceId+","+inscomcode+")", 10000);
			}else if(data.status == "fail"){
				if(underCount < isRequote){
					underCount += 1;
					arguments.callee(instanceId,subInstanceId,inscomcode);
				}else{
					underCount = 0;
					//防止重复提交
					$(".btn").prop("disabled", false);
					alertmsg(data.msg);
				}
			}
		}
	});
}

function getResult(instanceId1,subInstanceId1,inscomcode1){
	resultCount += 1;
	//防止重复提交
	$(".btn").prop("disabled", true);
	$.ajax({
		url : '/cm/business/ordermanage/getreQuoteBackResult',
		type : 'GET',
		dataType : 'text',
		data : "taskid="+instanceId1+"&inscomcode="+inscomcode1,
		cache : false,
		async : true,
		error : function() {
			//防止重复提交
			$(".btn").prop("disabled", false);
			alertmsg("Connection error");
		},
		success : function(data) {
			if(data == "true"){
				resultCount = 0;
				underCount = 0;
				clearInterval(intervalCount);
				location.reload();
			}else{
				if(resultCount==5){
					clearInterval(intervalCount);
					resultCount = 0;
					closediv();
					if(underCount < isRequote){
						underCount += 1;
						reQuoteInfo(instanceId1,subInstanceId1,inscomcode1);
					}else{
						underCount = 0;
						//防止重复提交
						$(".btn").prop("disabled", false);
						alertmsg("报价回写数据失败");
					}
				}
			}
		}
	});
}
