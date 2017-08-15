var timer = 20000;
var forwardURL = null;
var win = null;
var intervalCount = null;
var underCount = 0;
var isReunw = null;
var resultCount = 0 ;
require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public"], function ($) {

	$(function(){
		if($(window.top.document).find("#menu").css("display")!="none"){
			$("#backToMyTask").show();
		}
		if($("#desktop:not(:hidden)").length>0){
			win = $(window.desktop_content.document);
		}else{
			win = $("#fra_businessmytaskqueryTask").attr("document");
		}

		/**
		 * 进入页面时配送方式的显示
		 */
		if($("#deliverytype").children('option:selected').val()=="0"){
			$(".dediv").hide();
		}else if($("#deliverytype").children('option:selected').val()=="1"){
			$(".indiv").hide();
		}else{
			$(".indiv").hide();
			$(".dediv").hide();
		}

        /**
         * 配送方式的不同
         */
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

        //人工承保拒绝承保按钮事件处理
        $(".refuseInsurance").on("click", function(e) {
        	
        	//组织传递数据
            var commitNum = $.trim($(this).attr("id").replace(/refuseInsurance/, ""));
            var instanceId = $.trim($("#taskid"+commitNum).val());
            var subInstanceId = $.trim($("#subInstanceId"+commitNum).val());
            var userRemarkPageInfo = $.trim($("#userRemarkPageInfo"+commitNum).text());
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
        	
        	
            if(window.confirm("确认要拒绝承保吗？")==true){
                
                if(userRemarkPageInfo==""||userRemarkPageInfo==null){
                    alertmsg("请填写给用户的备注信息");
                }else{
                    //防止重复提交
                    $(".btn").prop("disabled", true);
                    $.ajax({
                        url : '/cm/business/manualprice/refuseUnderwrite',
                        type : 'POST',
                        dataType : 'json',
                        contentType: "application/json",
                        data : "{\"maininstanceId\":\""+instanceId+"\",\"subinstanceId\":\""+subInstanceId+"\",\"inscomcode\":\""+inscomcode+"\",\"mainorsub\":\"main\"}",
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
                            }else if (data.status == "fail"){
                                //防止重复提交
                                $(".btn").prop("disabled", false);
                                alertmsg("拒绝承保操作失败！");
                            }
                        }
                    });
                }
            }
        });
	});
}); 	

/**
 * 承保打单成功按钮跳转
 */
function undwrtSuccess(taskid,taskcode,inscomcode,bustotal,jqtotal){
	
	var close = false;
	$.ajax({
		url : "/cm/business/mytask/checkCloseTask",
		type : 'get',
		dataType : "json",
		async: false,
		data: {
			"instanceId":taskid,
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
	
	console.log($("#businessPolicyNum").text()==null);
	console.log($("#strongPolicyNum").text()==null);
	console.log($("#businessPolicyNum").text()=="");
	console.log($("#strongPolicyNum").text()=="");
	//if(($("#businessPolicyNum").text()==null && $("#strongPolicyNum").text()==null) || ($("#businessPolicyNum").text()=="" && $("#strongPolicyNum").text()=="")){
	//	alertmsg("请填写保单号!");
	//	return;
	//}
	//alert(bustotal>0);
	//alert(bustotal<=0);
	//alert(jqtotal>0);
	//alert(jqtotal<=0);
	//alert(bustotal);
	//alert(jqtotal);
	if(bustotal>0&&($("#businessPolicyNum").text()==null||$("#businessPolicyNum").text()=="")){
		alertmsg("请填写商业险保单号!");
		return;
	}
	if(jqtotal>0&&($("#strongPolicyNum").text()==null||$("#strongPolicyNum").text()=="")){
		alertmsg("请填写交强险保单号!");
		return;
	}
	//防止重复提交
	$(".btn").prop("disabled", true);
	$.ajax({
		url : "undwrtsuccess?taskid="+taskid+"&taskcode="+taskcode+"&inscomcode="+inscomcode,
		type : "GET",
		dataType : "json",
		async : true,
		error : function() {
			alertmsg("Connection error");
			//防止重复提交
			$(".btn").prop("disabled", false);
		},
		success : function(data) {
			if (data.status=="success") {
				if(taskcode == "23"){
					alertmsg("打单成功！");
				}else{
					alertmsg("承保打单成功！");
				}
				if($(window.top.document).find("#menu").css("display")=="none"){
            		$.cmTaskList('my', '', true);
				}else{
					if($("#frompage").val()=='orderManage'){
						window.location.href="/cm/business/ordermanage/ordermanagelist";
					}else{
						window.location.href = "/cm/business/mytask/queryTask";
					}
				}	
			} else if (data.status=="fail") {
				alertmsg(data.msg);
			} else{
				if(taskcode == "23"){
					alertmsg("打单失败！");
				}else{
					alertmsg("承保打单失败！");
				}
			}
			//防止重复提交
			$(".btn").prop("disabled", false);
		}
	});
}

/**
 * 承保打单配送成功按钮 
 */
function allundwrtSuccess(taskid,taskcode,inscomcode,bustotal,jqtotal){
	//if(($("#businessPolicyNum").text()==null && $("#strongPolicyNum").text()==null) || ($("#businessPolicyNum").text()=="" && $("#strongPolicyNum").text()=="")){
	//	alertmsg("请填写保单号!");
	//	return;
	//}
	
	var close = false;
	$.ajax({
		url : "/cm/business/mytask/checkCloseTask",
		type : 'get',
		dataType : "json",
		async: false,
		data: {
			"instanceId":taskid,
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
	
	if(bustotal>0&&($("#businessPolicyNum").text()==null||$("#businessPolicyNum").text()=="")){
		alertmsg("请填写商业险保单号!");
		return;
	}
	if(jqtotal>0&&($("#strongPolicyNum").text()==null||$("#strongPolicyNum").text()=="")){
		alertmsg("请填写交强险保单号!");
		return;
	}
	var tracenumber = $.trim($("#tracenumber").val());
	var comcodevalue = $.trim($("#comcode").val());
	var instanceId = $("#myInstanceId").val();
	var inscomcode = $("#inscomcode").val();
	var deltype = $("#deliverytype").children('option:selected').val();
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
		url : "undwrtDeliverySuccess?taskid="+taskid+"&taskcode="+taskcode+"&inscomcode="+inscomcode+"&tracenumber="+tracenumber+"&comcodevalue="+comcodevalue+"&deltype="+deltype,
		type : "GET",
		dataType : "json",
		contentType: 'application/json',
		error : function() {
			alertmsg("Connection error");
			//防止重复提 交
			$(".btn").prop("disabled", false);
		},
		success : function(data) {
			if (data.status=="success") {
				//后台使用了异步处理，所以这里要加个定时
                setTimeout(function() {
                    if(taskcode == "23"){
                        alertmsg("打单配送成功！");
                    }else{
                        alertmsg("承保打单配送成功！");
                    }
                    if($(window.top.document).find("#menu").css("display")=="none"){
                        $.cmTaskList('my', '', true);
                    }else{
                        if($("#frompage").val()=='orderManage'){
                            window.location.href="/cm/business/ordermanage/ordermanagelist";
                        }else{
                            window.location.href = "/cm/business/mytask/queryTask";
                        }
                    }
				}, 4000);
			} else if (data.status=="fail") {
				alertmsg(data.msg);
			} else if(data.status=="error1"){
				if(taskcode == "23"){
					alertmsg("打单配送失败,打单环节失败！");
				}else{
					alertmsg("承保打单配送失败,承保打单环节失败！");
				}
			}else if(data.status=="error2"){
				if(taskcode=="23"){
					alertmsg("打单配送失败,配送环节失败!");
				}else{
					alertmsg("承保打单配送失败,配送环节失败!");
				}
			}else if(data.status=="taskcoderror"){
				alertmsg("系统错误,稍后再试!");
			}
			//防止重复提交
			$(".btn").prop("disabled", false);
		}
	});
}

/**
 * 查询承保结果按钮跳转
 */
function undwrtSearch(taskid,inscomcode,type){
	
	var close = false;
	$.ajax({
		url : "/cm/business/mytask/checkCloseTask",
		type : 'get',
		dataType : "json",
		async: false,
		data: {
			"instanceId":taskid,
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
	
	isReunw = type;
	var subInstanceId = $.trim($("#subInstanceId").val());
	if (inscomcode.substring(0,4) == "2021" && inscomcode.indexOf("20214401")==-1 && inscomcode.indexOf("20214409")==-1 && inscomcode.indexOf("20214418")==-1) {
		$("*[id^='xDialog']").modal('hide');
		var temp = Math.floor(Math.random()*1000);
		var dialog = $('<div class="modal fade" id="xDialog5' + temp + '" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"><div class="modal-dialog" style="width:90%;position:absolute;top:100px;z-index:9991;"><div class="modal-content" id="modal-content">'
				+ '<label for="exampleInputCode" style="padding:8px 5px 5px 20px;font-family:microsoft yahei;">大地加密狗密钥：</label><input type="text" class="form-control m-left-5" id="dadikey5' + temp + '" name="dadikey5' + temp + '" placeholder="" style="width:95%">'
				+ '<div class="modal-footer"><a id="savedadikey5' + temp + '" class="btn btn-success">保存</a><a id="closebutton5' + temp + '" data-dismiss="modal" name="closebutton" class="btn btn-success">关闭</a></div>'
				+ '</div></div></div>');
		dialog.appendTo(document.body).modal({
			toggle : "modal",
			top: "500px",
			width : "720px",
			backdrop : false,
			keyboard : true
		});
		$("#savedadikey5" + temp).on("click", function(e) {
			var keyinfo = $("#dadikey5" + temp).val();
			if(keyinfo!=''){
				$.ajax({
					url : '/cm/interface/savedadikey?taskid=' + subInstanceId + "&inscomcode=" + inscomcode + "&dadikey=" + $("#dadikey5" + temp).val(),
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
							$('#xDialog5' + temp).modal('hide');
							underCount += 1;
							getUnderWritingInfo(taskid,subInstanceId,inscomcode);
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
		underCount += 1;
		getUnderWritingInfo(taskid,subInstanceId,inscomcode);
	}
}

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

function getUnderWritingInfo(instanceId,subInstanceId,inscomcode){
	//防止重复提交
	$(".btn").prop("disabled", true);
	$.ajax({ 
		url : "/cm/business/ordermanage/undwrtsearch",
		data: "taskid="+instanceId+"&inscomcode="+inscomcode+"&subInstanceId="+subInstanceId+"&underCount="+underCount,
		type : 'get',
		dataType : 'json',
		async : true,
		error : function() {
			alertmsg("Connection error");
			//防止重复提交
			$(".btn").prop("disabled", false);
		},
		success : function(data) {
			if(data.status == "success"){
//				alertmsg(data.msg);
				alertmsgno(data.msg);
				//成功后操作,刷新页面
				intervalCount = setInterval("getResult("+instanceId+","+subInstanceId+","+inscomcode+")", 10000);
			}else if(data.status == "fail"){
				if(underCount < isReunw){
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

/**
 *打回任务按钮跳转
 */
function undwrtBack(taskid,inscomcode){
	
	var close = false;
	$.ajax({
		url : "/cm/business/mytask/checkCloseTask",
		type : 'get',
		dataType : "json",
		async: false,
		data: {
			"instanceId":taskid,
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
	
	//防止重复提交
	$(".btn").prop("disabled", true);
	$.ajax({
		url : '/cm/business/manualprice/releaseTask',
		type : 'POST',
		dataType : 'json',
		contentType: "application/json",
		data :"{\"instanceId\":\""+taskid+"\",\"instanceType\":\""+1+"\",\"inscomcode\":\""+inscomcode+"\"}",
		cache : false,
		async : true,
		error : function() {
			alertmsg("Connection error");
			//防止重复提交
			$(".btn").prop("disabled", false);
		},
		success : function(data) {
			if (data.status == "success") {
				alertmsg("打回任务操作成功！",backToMyTask);
				/*if($(window.top.document).find("#menu").css("display")=="none"){
            		$.cmTaskList('my', '', true);
				}else{
					if($("#frompage").val()=='orderManage'){
						window.location.href="/cm/business/ordermanage/ordermanagelist";
					}else{
						window.location.href = "/cm/business/mytask/queryTask";
					}
				}	*/
			}else if (data.status == "fail"){
				alertmsg("打回任务操作失败！失败原因："+data.msg);
			}
			//防止重复提交
			$(".btn").prop("disabled", false);
		}
	});
}

var acount=0;

function getResult(instanceId1,subInstanceId1,inscomcode1){
	resultCount += 1;
	$.ajax({
		url : '/cm/business/ordermanage/getUnderwResult',
		type : 'GET',
		dataType : 'text',
		data : "taskid="+instanceId1+"&inscomcode="+inscomcode1,
		cache : false,
		async : true,
		error : function() {
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
					if(underCount < isReunw){
						underCount += 1;
						getUnderWritingInfo(instanceId1,subInstanceId1,inscomcode1);
					}else{
						underCount = 0;
						//防止重复提交
						$(".btn").prop("disabled", false);
						alertmsg("重新查询承保结果失败");
					}
				}
			}
		}
	});
	
}

