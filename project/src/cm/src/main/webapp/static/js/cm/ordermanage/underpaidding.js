//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "handlepaidding";
//当前页
var pagecurrent = 1;
//总页数
var pagecount = 0;

require(["jquery", "bootstrap-table", "bootstrap", "bootstrapTableZhCn","public"], function($) {

    $(function() {
//    //数据初始化
//
//        //加载数据
//        $.ajax({
//            url : 'tasksummary?page=' + pagecurrent + '&taskid=1',
//            type : 'get',
//            dataType : "json",
//            async : true,
//            error : function() {
//                alertmsg("Connection error");
//            },
//            success : function(data) {
//                //展示数据
//                loaddata(data);
//            }
//        });

        //调用支付
        $(".paiSuccess").click(function () {
            var commitNum = $.trim($(this).attr("id").replace(/paiSuccess/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
            var subInstanceId = $.trim($("#subInstanceId" + commitNum).val());
            var inscomcode = $.trim($("#thisInscomcode" + commitNum).val());
            
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
            //alertmsg(instanceId+"--");
            //防止重复提交
            $(".btn").prop("disabled", true);
            $.ajax({
                url: '/cm/business/ordermanage/getPaySuccess',
                type: 'POST',
                dataType: 'text',
                data: "{\"processinstanceid\":\"" + subInstanceId + "\",\"inscomcode\":\"" + inscomcode + "\",\"underWritingFlag\":\"1\",\"taskid\":\"" + instanceId + "\",\"issecond\":\"" + "0" + "\"}",
                contentType: 'application/json',
                cache: false,
                async: true,
                error: function () {
                    //防止重复提交
                    $(".btn").prop("disabled", false);
                    alertmsg("Connection error");
                },
                success: function (data) {
                    if (data == "success") {
                        alertmsg("支付成功！", backToMyTask);
                    } else if (data == "noright") {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("没有执行的权限。");
                    } else if (data == "payFail") {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("代理人不是正式账户，不允许支付，需转为正式用户！");
                    } else {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg(data + " 支付失败！");
                    }
                }
            });
        });
        // 退回任务按钮事件处理
        $(".backTask").on("click", function (e) {
            //组织传递数据
            var commitNum = $.trim($(this).attr("id").replace(/backTask/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
            var subInstanceId = $.trim($("#subInstanceId" + commitNum).val());
            var inscomcode = $.trim($("#thisInscomcode" + commitNum).val());
            
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
            
            //alertmsg("isntanceId="+instanceId+"inscomcode="+inscomcode);
            //防止重复提交
            $(".btn").prop("disabled", true);
            $.ajax({
                url: '/cm/business/manualprice/releaseTask',
                type: 'POST',
                dataType: 'json',
                contentType: "application/json",
                data: "{\"instanceId\":\"" + subInstanceId + "\",\"instanceType\":\"" + 2 + "\",\"inscomcode\":\"" + inscomcode + "\"}",
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
                        $(".btn").prop("disabled", false);
                        alertmsg(data.msg);
                    } else if (data.status == "fail") {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("打回任务操作失败！");
                    }
                }
            });
        });

        $(".restartUnderWriting").on("click", function (e) {
            var commitNum = $.trim($(this).attr("id").replace(/restartUnderWriting/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
            var subInstanceId = $.trim($("#subInstanceId" + commitNum).val());
            var inscomcode = $.trim($("#thisInscomcode" + commitNum).val());
            
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
            //alertmsg(instanceId+"--");
            //防止重复提交
            $(".btn").prop("disabled", true);
            $.ajax({
                url: '/cm/business/ordermanage/getPaySuccess',
                type: 'POST',
                dataType: 'text',
                data: "{\"processinstanceid\":\"" + subInstanceId + "\",\"inscomcode\":\"" + inscomcode + "\",\"underWritingFlag\":\"1\",\"taskid\":\"" + instanceId + "\",\"issecond\":\"" + "2" + "\"}",
                contentType: 'application/json',
                cache: false,
                async: true,
                error: function () {
                    //防止重复提交
                    $(".btn").prop("disabled", false);
                    alertmsg("Connection error");
                },
                success: function (data) {
                    if (data == "success") {
                        alertmsg("重新核保成功！", backToMyTask);
                    } else if (data == "noright") {
                        alertmsg("没有执行权限！");
                    } else if (data == "fail") {
                        alertmsg("重新核保失败！");
                    } else if (data == "paying") {
                        alertmsg("订单正在支付中，不能重新核保！");
                    } else {
                        alertmsg(data);
                    }
                    //防止重复提交
                    $(".btn").prop("disabled", false);
                }
            });
        });

        function backToMyTask() {
            if ($(window.top.document).find("#menu").css("display") == "none") {
                $.cmTaskList('my', '', true);
            } else {
            	if($("#frompage").val()=='orderManage'){
    				window.location.href="/cm/business/ordermanage/ordermanagelist";
    			}else{
    				window.location.href = "/cm/business/mytask/queryTask";
    			}
            }
        }
        
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

        //退款成功
        $(".refunded").on("click", function(){
            var commitNum = $.trim($(this).attr("id").replace(/refunded/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
            refund(instanceId, "");
        });

        //退款申请
        $(".refund").on("click", function(){
            var commitNum = $.trim($(this).attr("id").replace(/refund/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
            refund(instanceId, "tl");
        });


    });

});
function refund(instanceId,f) {

    //防止重复提交
    $(".btn").prop("disabled", true);
    $.ajax({
        url: '/cm/business/refundtaskmanage/refund',
        type: 'POST',
        dataType: 'json',
        data: {"taskid": instanceId,"tl" :f},
        //contentType: 'application/json',
        cache: false,
        async: true,
        error: function () {
            //防止重复提交
            $(".btn").prop("disabled", false);
            alertmsg("Connection error");
        },
        success: function (data) {
            if (data && data.msg) {
                alertmsg(data.msg);

                if (data.success) {
                    $(".refund").prop("disabled", true);
                    $(".refundbtn").prop("disabled", false);
                } else {
                    $(".btn").prop("disabled", false);
                }
            } else {
                alertmsg("Connection error");
                //防止重复提交
                $(".btn").prop("disabled", false);
            }
        }
    });
}
//封装条件
function queryrule() {
    var postdata = "";
    
    //简单查询条件
    if($("#queryrule").val()){
        postdata += "&carlicenseno=" + $("#queryrule").val();
        postdata += "&name=" + $("#queryrule").val();
    }
    return postdata;
}

//添加事件
function operateFormatter(value, row, index) {
    return ['<a class="edit m-left-5" href="javascript:void(0)" title="编辑">', '<i class="glyphicon glyphicon-edit"></i>', '</a>', '<a class="remove m-left-5" href="javascript:void(0)" title="删除">', '<i class="glyphicon glyphicon-remove"></i>', '</a>'].join('');
}

//事件相应
window.operateEvents = {
    'click .edit' : function(e, value, row, index) {
        updateuser(row.id);
    },
    'click .remove' : function(e, value, row, index) {
        deleteuser(row.id);
    }
};

function loaddata(data) {
    showdata(data.rows);
}


function showdata(list) {
    //取出主数据
    for (var i = 0; i < list.length; i++) {
        var tabletemplate = $("#task_summary");
        tabletemplate.find("#tcarlicenseno").text(list[i].carlicenseno);
        tabletemplate.find("#tinsuredname").text(list[i].insuredname);
        tabletemplate.find("#tagent").text(list[i].agent);
        tabletemplate.find("#tteam").text(list[i].team);
        // tabletemplate.find("#tbusinessproposalformno").text(list[i].businessproposalformno);
        tabletemplate.find("#tbusinesspolicyno").text(list[i].businesspolicyno);
        tabletemplate.find("#tcreatetime").text(list[i].createtime);
        //
        // tabletemplate.find("#tprv").text(list[i].prvid);
        tabletemplate.find("#tdeptcode").text(list[i].deptcode);
        tabletemplate.find("#tnoti").text(list[i].noti);
        tabletemplate.find("#torderstatus").text(list[i].orderstatus);
        tabletemplate.find("#tmobile").text(list[i].mobile);
        tabletemplate.find("#tcgfns").text(list[i].cgfns);
        tabletemplate.find("#tidno").text(list[i].idno);
        tabletemplate.find("#tlicenseno").text(list[i].licenseno);
        // tabletemplate.find("#tlast").text("报价创建（完成）");
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

