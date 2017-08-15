var isRequote = null;
var underCount = 0;
require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public"], function ($) {
    $(function() {
        // 人工录单完成通过按钮事件处理
        $(".adjustment").on("click", function (e) {
            var isFee = $("#isFeeAreaFlag").val();
            //组织传递数据
            var commitNum = $.trim($(this).attr("id").replace(/adjustment/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
            var checkbox = $("#checkbox" + commitNum);
            var isCheckBox = 1;
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
//		if(checkbox.is(':checked')) {
//		    //alertmsg("选中"+commitNum);
//		    isCheckBox = 0;
//		}else{
//			//alertmsg("未选中"+commitNum);
//			isCheckBox = 1;
//		}
            //获取单选框中的数据
            var totalAmountprice = Number($("#totalAmountprice" + commitNum).val());//总保费
            if (totalAmountprice == 0) {
                alertmsg("总保费为0，不能报价通过！");
                return;
            }
            var comnum = $("#carInsTaskInfoListSize").val();
            if (isFee == '0' && comnum > 1) {
                //非费改地区一键全部生效
                for (var i = 0; i < comnum; i++) {
                    var totalAmountpriceTemp = Number($("#totalAmountprice" + i).val());//总保费
                    if (totalAmountpriceTemp == 0) {
                        alertmsg($("#inscomname" + i).val() + "保险公司总保费为0，不能报价通过！");
                        return;
                    }
                }
            }

            //防止重复提交
            $(".btn").prop("disabled", true);
            $.ajax({
                url: '/cm/business/ordermanage/getManualRecording',
                type: 'POST',
                dataType: 'text',
                contentType: "application/json",
                data: "{\"processinstanceid\":\"" + subInstanceId + "\",\"isRebackWriting\":\"" + isCheckBox + "\"}",
                cache: false,
                async: true,
                error: function () {
                    //防止重复提交
                    $(".btn").prop("disabled", false);
                    alertmsg("Connection error");
                },
                success: function (data) {
                    if (data == "success") {
                        alertmsg("人工录单操作成功！", backToMyTask);
                    } else {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("人工录单操作失败！");
                    }
                }
            });
        });
        // 人工录单重新报价
        $(".peopleDo").on("click", function (e) {
            //组织传递数据
            var commitNum = $.trim($(this).attr("id").replace(/peopleDo/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
            //子流程id
            var subInstanceId = $.trim($("#subInstanceId" + commitNum).val());
            var userRemarkPageInfo = $.trim($("#userRemarkPageInfo" + commitNum).text());
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
            
            
            if (userRemarkPageInfo == "" || userRemarkPageInfo == null) {
                alertmsg("请填写给用户的备注信息");
                return;
            }

            var elfquoteFlag = $("#elfquoteFlag").val();//是否有精灵报价能力
            if (inscomcode.substring(0, 4) == "2021" && inscomcode.indexOf("20214401") == -1 && inscomcode.indexOf("20214409") == -1 &&
                inscomcode.indexOf("20214418") == -1&&elfquoteFlag=="1") {
                $("*[id^='xDialog']").modal('hide');
                var temp = Math.floor(Math.random() * 1000);
                var dialog = $('<div class="modal fade" id="xDialog10' + temp + '" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"><div class="modal-dialog" style="width:90%;position:absolute;top:100px;z-index:9991;"><div class="modal-content" id="modal-content">'
                    + '<label for="exampleInputCode" style="padding:8px 5px 5px 20px;font-family:microsoft yahei;">大地加密狗密钥：</label><input type="text" class="form-control m-left-5" id="dadikey10' + temp + '" name="dadikey10' + temp + '" placeholder="" style="width:95%">'
                    + '<div class="modal-footer"><a id="savedadikey10' + temp + '" class="btn btn-success">保存</a><a id="closebutton10' + temp + '" data-dismiss="modal" name="closebutton" class="btn btn-success">关闭</a></div>'
                    + '</div></div></div>');
                dialog.appendTo(document.body).modal({
                    toggle: "modal",
                    top: "500px",
                    width: "720px",
                    backdrop: false,
                    keyboard: true
                });
                $("#savedadikey10" + temp).on("click", function (e) {
                    var keyinfo = $("#dadikey10" + temp).val();
                    if (keyinfo != '') {
                        $.ajax({
                            url: '/cm/interface/savedadikey?taskid=' + subInstanceId + "&inscomcode=" + inscomcode + "&dadikey=" + $("#dadikey10" + temp).val(),
                            type: 'POST',
                            dataType: 'json',
                            contentType: "application/json",
                            cache: false,
                            async: false,
                            error: function () {
                                alertmsg("Connection error");
                            },
                            success: function (data) {
                                if (data == 'success') {
                                    $('#xDialog10' + temp).modal('hide');
                                    //防止重复提交
                                    $(".btn").prop("disabled", true);
                                    $.ajax({
                                        url: '/cm/business/ordermanage/manualRecordingRe',
                                        type: 'POST',
                                        dataType: 'text',
                                        contentType: "application/json",
                                        data: "{\"processinstanceid\":\"" + subInstanceId + "\"}",
                                        cache: false,
                                        async: true,
                                        error: function () {
                                            //防止重复提交
                                            $(".btn").prop("disabled", false);
                                            alertmsg("Connection error");
                                        },
                                        success: function (data) {
                                            if (data == "success") {
                                                alertmsg("人工录单重新报价操作成功！", backToMyTask);
                                            } else {
                                                //防止重复提交
                                                $(".btn").prop("disabled", false);
                                                alertmsg("人工录单重新报价操作失败！");
                                            }
                                        }
                                    });
                                } else {
                                    alertmsg("保存失败");
                                }
                            }
                        });
                    } else {
                        alertmsg("请填写大地加密狗密钥...");
                    }
                });
            } else {
                $(".btn").prop("disabled", true);
                $.ajax({
                    url: '/cm/business/ordermanage/manualRecordingRe',
                    type: 'POST',
                    dataType: 'text',
                    contentType: "application/json",
                    data: "{\"processinstanceid\":\"" + subInstanceId + "\"}",
                    cache: false,
                    async: true,
                    error: function () {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("Connection error");
                    },
                    success: function (data) {
                        if (data == "success") {
                            alertmsg("人工录单重新报价操作成功！", backToMyTask);
                        } else {
                            //防止重复提交
                            $(".btn").prop("disabled", false);
                            alertmsg("人工录重新报价操作失败！");
                        }
                    }
                });
            }
        });


        // 人工录单中 退回修改按钮  事件处理
        $(".backEdit").on("click", function (e) {
            var isFee = $("#isFeeAreaFlag").val();
            //组织传递数据
            var commitNum = $.trim($(this).attr("id").replace(/backEdit/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
            //子流程id
            var subInstanceId = $.trim($("#subInstanceId" + commitNum).val());
            var userRemarkPageInfo = $.trim($("#userRemarkPageInfo" + commitNum).text());
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
            
            if (userRemarkPageInfo == "" || userRemarkPageInfo == null) {
                alertmsg("请填写给用户的备注信息");
                return;
            }
            var comnum = $("#carInsTaskInfoListSize").val();
            if (isFee == '0' && comnum > 1) {
                //非费改地区一键全部返回修改
                for (var i = 0; i < comnum; i++) {
                    var userRemarkPageInfo = $.trim($("#userRemarkPageInfo" + i).text());
                    if (userRemarkPageInfo == "" || userRemarkPageInfo == null) {
                        alertmsg($("#inscomname" + i).val() + "保险公司,请填写给用户的备注信息");
                        return;
                    }
                }
            }
            //防止重复提交
            $(".btn").prop("disabled", true);
            $.ajax({
                url: '/cm/business/ordermanage/manualRecordingBack',
                type: 'POST',
                dataType: 'text',
                contentType: "application/json",
                data: "{\"processinstanceid\":\"" + subInstanceId + "\"}",
                cache: false,
                async: true,
                error: function () {
                    //防止重复提交
                    $(".btn").prop("disabled", false);
                    alertmsg("Connection error");
                },
                success: function (data) {
                    if (data == "success") {
                        alertmsg("人工录单退回修改操作成功！", backToMyTask);
                    } else {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("人工录单退回修改操作失败！");
                    }
                }
            });
        });

        // 人工录单拒绝承保按钮事件处理
        $(".refuseInsurance").on("click", function (e) {
            var isFee = $("#isFeeAreaFlag").val();
          //组织传递数据
            var commitNum = $.trim($(this).attr("id").replace(/refuseInsurance/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
            //子流程id
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
            
            if (window.confirm("确认要拒绝承保吗？") == false) {
                return;
            }
            
            var userRemarkPageInfo = $.trim($("#userRemarkPageInfo" + commitNum).text());
            if (userRemarkPageInfo == "" || userRemarkPageInfo == null) {
                alertmsg("请填写给用户的备注信息");
                return;
            }
            var comnum = $("#carInsTaskInfoListSize").val();
            if (isFee == '0' && comnum > 1) {
                for (var i = 0; i < comnum; i++) {
                    var userRemarkPageInfo = $.trim($("#userRemarkPageInfo" + i).text());
                    if (userRemarkPageInfo == "" || userRemarkPageInfo == null) {
                        alertmsg($("#inscomname" + i).val() + "保险公司,请给用户的备注信息");
                        return;
                    }
                }
            }
            //防止重复提交
            $(".btn").prop("disabled", true);
            $.ajax({
                url: '/cm/business/manualprice/quoteRefuseUnderwrite',
                type: 'POST',
                dataType: 'json',
                contentType: "application/json",
                data: "{\"maininstanceId\":\"" + instanceId + "\",\"subinstanceId\":\"" + subInstanceId + "\",\"inscomcode\":\"" + inscomcode + "\",\"mainorsub\":\"sub\"}",
                cache: false,
                async: true,
                error: function () {
                    //防止重复提交
                    $(".btn").prop("disabled", false);
                    alertmsg("Connection error");
                },
                success: function (data) {
                    if (data.status == "success") {
                        alertmsg("拒绝承保操作成功！", backToMyTask);
                    } else if (data.status == 'trynotice') {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("操作完成！但是有警告：" + data.msg);
                    } else if (data.status == "fail") {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("拒绝承保操作失败！失败原因：" + data.msg);
                    }
                }
            });
        });

        //打回任务
        $(".backTask").on("click", function (e) {
            var isFee = $("#isFeeAreaFlag").val();
            //组织传递数据
            var commitNum = $.trim($(this).attr("id").replace(/backTask/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
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
            
            //子流程id
            var subInstanceId = $.trim($("#subInstanceId" + commitNum).val());
            //alertmsg(instanceId+"--instanceId+inscomcode"+inscomcode);
            var comnum = $("#carInsTaskInfoListSize").val();
            if (isFee == '0' && comnum > 1) {
                for (var i = 0; i < comnum; i++) {
                    var instanceId = $.trim($("#taskid" + i).val());
                    var inscomcode = $.trim($("#thisInscomcode" + i).val());
                    //子流程id
                    var subInstanceId = $.trim($("#subInstanceId" + i).val());
                }
            }
            //防止重复提交
            $(".btn").prop("disabled", true);
            $.ajax({
                url: '/cm/business/manualprice/quoteReleaseTask',
                type: 'POST',
                dataType: 'json',
                contentType: "application/json",
                //instanceType,1主流程，2子流程
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
                    } else if (data.status == "fail") {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("打回任务操作失败！");
                    }
                }
            });
        });

//报价回写 ,这个是 还未修改，就是不是和人工规则报价中的一样
        $(".reQuoteBack").on("click", function () {
            var commitNum = $.trim($(this).attr("id").replace(/reQuoteBack/, ""));
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
            
            var hasbusi = $.trim($("#hasbusi" + commitNum).val());
            var hasstr = $.trim($("#hasstr" + commitNum).val());
            var businessProposalFormNo = $.trim($("#businessProposalFormNo" + commitNum).text());
            var strongProposalFormNo = $.trim($("#strongProposalFormNo" + commitNum).text());
            isRequote = Number($.trim($("#isRequote" + commitNum).val()));
//		alert(hasbusi+","+hasstr);
//		alert(subInstanceId+",s"+inscomcode+",ins"+hasbusi+"-hasbusi--"+hasstr+"-hasstr-"+businessProposalFormNo+","+strongProposalFormNo);
            if (hasbusi == "true" && hasstr == "true") {
                if (strongProposalFormNo == "" || businessProposalFormNo == "") {
                    alertmsg("交强险或商业险投保单号不能为空");
                    return;
                } else {
                }
            } else {
                if (hasbusi == "true" && hasstr == "false") {
                    if (businessProposalFormNo == "") {
                        alertmsg("商业险投保单号不能为空");
                        return;
                    } else {
                    }
                } else if (hasbusi == "false" && hasstr == "true") {
                    if (strongProposalFormNo == "") {
                        alertmsg("交强险投保单号不能为空");
                        return;
                    } else {
                    }
                }

            }
            var isFee = $("#isFeeAreaFlag").val();
            var comnum = $("#carInsTaskInfoListSize").val();
            if (isFee == '0' && comnum > 1) {
                for (var i = 0; i < comnum; i++) {
                    var hasbusi = $.trim($("#hasbusi" + i).val());
                    var hasstr = $.trim($("#hasstr" + i).val());
                    var businessProposalFormNo = $.trim($("#businessProposalFormNo" + i).text());
                    var strongProposalFormNo = $.trim($("#strongProposalFormNo" + i).text());
                    if (hasbusi == "true" && hasstr == "true") {
                        if (strongProposalFormNo == "" || businessProposalFormNo == "") {
                            alertmsg($("#inscomname" + i).val() + "交强险或商业险投保单号不能为空");
                            return;
                        } else {
                        }
                    } else {
                        if (hasbusi == "true" && hasstr == "false") {
                            if (businessProposalFormNo == "") {
                                alertmsg($("#inscomname" + i).val() + "商业险投保单号不能为空");
                                return;
                            } else {
                            }
                        } else if (hasbusi == "false" && hasstr == "true") {
                            if (strongProposalFormNo == "") {
                                alertmsg($("#inscomname" + i).val() + "交强险投保单号不能为空");
                                return;
                            } else {
                            }
                        }

                    }
                }
            }
            var elfquoteFlag = $("#elfquoteFlag").val();//是否有精灵报价能力
            if (inscomcode.substring(0, 4) == "2021" && inscomcode.indexOf("20214401") == -1 && inscomcode.indexOf("20214409") == -1 &&
                inscomcode.indexOf("20214418") == -1&&elfquoteFlag=="1") {
                $("*[id^='xDialog']").modal('hide');
                var temp = Math.floor(Math.random() * 1000);
                var dialog = $('<div class="modal fade" id="xDialog6' + temp + '" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"><div class="modal-dialog" style="width:90%;position:absolute;top:100px;z-index:9991;"><div class="modal-content" id="modal-content">'
                    + '<label for="exampleInputCode" style="padding:8px 5px 5px 20px;font-family:microsoft yahei;">大地加密狗密钥：</label><input type="text" class="form-control m-left-5" id="dadikey6' + temp + '" name="dadikey6' + temp + '" placeholder="" style="width:95%">'
                    + '<div class="modal-footer"><a id="savedadikey6' + temp + '" class="btn btn-success">保存</a><a id="closebutton6' + temp + '" data-dismiss="modal" name="closebutton" class="btn btn-success">关闭</a></div>'
                    + '</div></div></div>');
                dialog.appendTo(document.body).modal({
                    toggle: "modal",
                    top: "500px",
                    width: "720px",
                    backdrop: false,
                    keyboard: true
                });
                $("#savedadikey6" + temp).on("click", function (e) {
                    var keyinfo = $("#dadikey6" + temp).val();
                    if (keyinfo != '') {
                        $.ajax({
                            url: '/cm/interface/savedadikey?taskid=' + subInstanceId + "&inscomcode=" + inscomcode + "&dadikey=" + $("#dadikey6" + temp).val(),
                            type: 'POST',
                            dataType: 'json',
                            contentType: "application/json",
                            cache: false,
                            async: false,
                            error: function () {
                                alertmsg("Connection error");
                            },
                            success: function (data) {
                                if (data == 'success') {
                                    $('#xDialog6' + temp).modal('hide');
                                    //防止重复提交
                                    $(".btn").prop("disabled", true);
                                    underCount += 1;
                                    reQuoteInfo(instanceId, subInstanceId, inscomcode);
                                } else {
                                    alertmsg("保存失败");
                                }
                            }
                        });
                    } else {
                        alertmsg("请填写大地加密狗密钥...");
                    }
                });
            } else {
                //防止重复提交
                $(".btn").prop("disabled", true);
                underCount += 1;
                reQuoteInfo(instanceId, subInstanceId, inscomcode);
            }
        });

        function backToMyTask() {
            if ($(window.top.document).find("#menu").css("display") == "none") {
                $.cmTaskList('my', '', true);
            } else {
                window.location.href = "/cm/business/mytask/queryTask";
            }
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
