require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public"], function ($) {
    $(function() {
    	
    	$(".closeshowpic").on("click",function(){
			$("#showDetail").modal("hide");
		});
		$(".queryordresdig").on("click",function(){
			$("#showqueryordresult").modal("hide");
		});
		$(".fqqueryordresdig").on("click",function(){
			$("#fqshowqueryordresult").modal("hide");
		});
    	
        //调用 二次支付成功
        $(".mediumPay").click(function () {
            var commitNum = $.trim($(this).attr("id").replace(/mediumPay/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
            var subInstanceId = $.trim($("#subInstanceId" + commitNum).val());
            var inscomcode = $.trim($("#inscomcode" + commitNum).val());
            
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
            //alertmsg(subInstanceId+"subInstanceId");
            //alertmsg(instanceId+"--instanceid");
            //防止重复提交
            $(".btn").prop("disabled", true);
            $.ajax({
                url: '/cm/business/ordermanage/mediumPayment',
                type: 'POST',
                dataType: 'text',
                data: "{\"processinstanceid\":\"" + subInstanceId + "\",\"inscomcode\":\"" + inscomcode + "\",\"taskid\":\"" + instanceId + "\"}",
                contentType: 'application/json',
                cache: false,
                async: true,
                error: function () {
                    //防止重复提交
                    $(".btn").prop("disabled", false);
                    alertmsg("调用失败");
                },
                success: function (data) {
                    if (data == "success") {
                        alertmsg("二次支付成功！", backToMyTask);
                    } else if (data == "noright") {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("任务失败,没有执行的权限！");
                    } else {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("二次支付失败！");
                    }
                }
            });
        });
        
      //申请验证码
        $(".applypin").click(function () {
            var commitNum = $.trim($(this).attr("id").replace(/applypin/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
            var inscomcode = $.trim($("#inscomcode" + commitNum).val());
            //alertmsg(instanceId+"-" + inscomcode);
            //alertmsg(instanceId+"--instanceid");
            //防止重复提交
            $(".btn").prop("disabled", true);
			$("#applyingdiv").show();
            $.ajax({
                url: '/cm/business/ordermanage/cmapplypin',
                type: 'POST',
                dataType: 'json',
                data: {
                	"inscomcode":inscomcode,
                	"taskid":instanceId
                },
                cache: false,
                async: true,
                error: function () {
                	$("#applyingdiv").hide();
                    //防止重复提交
                    $(".btn").prop("disabled", false);
                    alertmsg("请求超时!");
                },
                success: function (data) {
                    if (data.status == "success") {
                        window.setTimeout(function(){getpincodebj(instanceId,inscomcode)}, 20000);//调用重新获取二维码查询状态接口
                    } else {
                        alertmsg("申请验证码失败！原因:<br>" + data.errmsg);
                    }
                    $(".btn").prop("disabled", false);
                }
            });
        });

        $(".endTask4Pay").on("click", function () {
            var commitNum = $.trim($(this).attr("id").replace(/endTask4Pay/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
            var subInstanceId = $.trim($("#subInstanceId" + commitNum).val());
            var inscomcode = $.trim($("#inscomcode" + commitNum).val());
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
            //必填用户备注
            var userRemarkPageInfo = $.trim($("#userRemarkPageInfo" + commitNum).text());
            if (userRemarkPageInfo == "" || userRemarkPageInfo == null) {
                alertmsg("请填写给用户的备注信息");
                return;
            }
            var subInstanceId = $.trim($("#subInstanceId" + commitNum).val());
            //关闭二次支付
            $.ajax({
                url: '/cm/business/manualprice/endPayTask',
                type: 'POST',
                data: ({instanceId: subInstanceId}),
                //data :"{\"instanceId\":\""+subInstanceId+"\",\"instanceType\":\""+2+"\",\"inscomcode\":\""+inscomcode+"\"}",
                cache: false,
                async: true,
                error: function () {
                    //防止重复提交
                    $(".btn").prop("disabled", false);
                    alertmsg("Connection error");
                },
                success: function (data) {
                    if (data.status == "success") {
                        alertmsg("关闭任务操作成功！", backToMyTask);
                    } else if (data.status == "noright") {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("工作流调用失败，没有执行的权限！");
                    } else if (data.status == "fail") {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("关闭任务操作失败！");
                    }
                }
            })
        });

        // 退回任务按钮事件处理
        $(".backTask").on("click", function (e) {
            //组织传递数据
            var commitNum = $.trim($(this).attr("id").replace(/backTask/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
            //var inscomcode = $("#inscomcode").val();
            var subInstanceId = $.trim($("#subInstanceId" + commitNum).val());
            var inscomcode = $.trim($("#inscomcode" + commitNum).val());
            
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
            
            
            //alertmsg(instanceId+"--instanceId+inscomcode="+inscomcode+"commitNum="+commitNum);
            //防止重复提交
            $(".btn").prop("disabled", true);


            $.ajax({
                url: '/cm/business/manualprice/releaseTask',
                type: 'POST',
                dataType: 'json',
                contentType: "application/json",
                //data :"{\"instanceId\":\""+instanceId+"\",\"inscomcode\":\""+inscomcode+"\"}",
                //instanceType,1主流程，2子流程
                data: "{\"instanceId\":\"" + subInstanceId + "\",\"instanceType\":\"" + 2 + "\",\"inscomcode\":\"" + inscomcode + "\",\"statu\":\"" + 21 + "\"}",
                cache: false,
                async: true,
                error: function () {
                    //防止重复提交
                    $(".btn").prop("disabled", false);
                    alertmsg("Connection error");
                },
                success: function (data) {
                    if (data.status == "success") {
                        alertmsg("打回任务操作成功！", backToMyTask);
                    } else if (data.status == "noright") {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("工作流调用失败，没有执行的权限！");
                    } else if (data.status == "fail") {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("打回任务操作失败！");
                    }
                }
            });
        });

        function backToMyTask() {
            if ($(window.top.document).find("#menu").css("display") == "none") {
                $.cmTaskList('my', '', true);
            } else {
                window.location.href = "/cm/business/mytask/queryTask";
            }
        }

        function getpincodebj(instanceId,inscomcode){
            var pincodeJson = {//请求结果参数json集合
                "taskId":instanceId,
                "inscomcode":inscomcode,
                "agentId":"admin"
            };
            $.ajax({
                url: '/cm/interface/getpincodebj',
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify(pincodeJson),
                cache: false,
                async: true,
                error: function () {
                    $("#applyingdiv").hide();
                    //防止重复提交
                    $(".btn").prop("disabled", false);
                    alertmsg("请求超时!");
                },
                success: function (data) {
                    $("#applyingdiv").hide();
                    if (data.status == "success") {
                        var pinsta = data.applysta;
                        if(pinsta == "1"){
                            alertmsg("验证码发送成功！");
                        } else if(pinsta == "2"){
                            alertmsg("验证码发送失败");
                        } else if(pinsta == "3"){
                            alertmsg("姓名身份证信息错误，请修改重提！");
                        } else if(pinsta == "4"){
                            alertmsg("申请验证码数据有误，请重新提单！");
                        }  else if(pinsta == "5"){
                            alertmsg("未知异常！");
                        } else {
                            alertmsg("没有状态数据: " + pinsta);
                        }
                    } else {
                        alertmsg("申请验证码失败！原因:<br>" + data.errmsg);
                    }
                    $(".btn").prop("disabled", false);
                }
            });
        }
    });
});

function showdetail(){
	$("#showDetail").modal();
}