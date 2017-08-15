var resultCount = 0 ;
var intervalCount = null;
var isReub = null;
var underCount = 0;
require(["jquery", "bootstrapdatetimepicker","bootstrap-table", "bootstrap","bootstrapTableZhCn","zTree","zTreecheck","flat-ui","bootstrapdatetimepickeri18n","public"], function ($) {
	$(function() {
		
		$(".closeshowpic").on("click",function(){
			$("#showDetail").modal("hide");
		});
        //查看报价金额 按钮  事件处理
        $(".searchMoney").on("click", function (e) {
            //组织传递数据
            var commitNum = $.trim($(this).attr("id").replace(/searchMoney/, ""));
            var instanceid = $.trim($("#taskid" + commitNum).val());
            var inscomcode = $.trim($("#inscomcode" + commitNum).val());
            //alert("inscomcode='"+inscomcode);
            //跳出报价金额弹出框
            window.parent.openDialogForCM("business/manualrecord/searchMoney?instanceid=" + instanceid + "&inscomcode=" + inscomcode);
        });

        // 人工核保完成通过按钮事件处理
        $(".adjustment").on("click", function (e) {
            //组织传递数据
            var commitNum = $.trim($(this).attr("id").replace(/adjustment/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
            var subInstanceId = $.trim($("#subInstanceId" + commitNum).val());
            var inscomcode = $.trim($("#inscomcode" + commitNum).val());
            var businessProposalFormNo = $.trim($("#businessProposalFormNo").text());
            var strongProposalFormNo = $.trim($("#strongProposalFormNo").text());
            
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
            

            if ($("#insureconfigsameaslastyear" + commitNum + ":checked").size() > 0) {
                alertmsg("请取消“与上年一致”后再核保完成");
                return;
            }

            var totalAmountprice = Number($("#totalAmountprice" + commitNum).val());//总保费
            if (totalAmountprice == 0) {
                alertmsg("总保费为0，不能报价通过！");
                return;
            }


            //放到服务器端校验
            /*var businessProposalFormNo = $.trim($("#businessProposalFormNo").text());
            var strongProposalFormNo = $.trim($("#strongProposalFormNo").text());
            var hasbusi = $.trim($("#hasbusi").val());
            var hasstr = $.trim($("#hasstr").val());

            if (hasbusi == "true" && hasstr == "true") {
                if (strongProposalFormNo == "" || businessProposalFormNo == "") {
                    alertmsg("交强险和商业险投保单号不能为空");
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
            }*/

            //防止重复提交
            $(".btn").prop("disabled", true);
            $.ajax({
                url: '/cm/business/ordermanage/manualUnderWritingSuccess',
                type: 'POST',
                dataType: 'text',
                contentType: "application/json",
                data: "{\"processinstanceid\":\"" + subInstanceId + "\",\"taskid\":\"" + instanceId + "\",\"inscomcode\":\"" + inscomcode + "\",\"ciPolicyNo\":\""+strongProposalFormNo+"\",\"biPolicyNo\":\""+businessProposalFormNo+"\"}",
                cache: false,
                async: true,
                error: function () {
                    //防止重复提交
                    $(".btn").prop("disabled", false);
                    alertmsg("Connection error");
                    center($("#alert_div"));
                },
                success: function (data) {
                    data = JSON.parse(data || "{}");
                    if (data.status == "success") {
                        alertmsg("人工核保操作成功！", backToMyTask(3000));
                        center($("#alert_div"));
                    } else {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("人工核保操作失败！" + (data.message || ""));
                        center($("#alert_div"));
                    }
                }
            });

        });

        function center(obj){
            var windowWidth = document.documentElement.clientWidth;
            var windowHeight = document.documentElement.clientHeight;
            var popupHeight = $(obj).height();
            var popupWidth = $(obj).width();
            $(obj).css({
                "position": "absolute",
                "top": (windowHeight)/2,
                "z-index":99999
                //"left": (windowWidth)/2
            });
        }
        
        // 人工核保中 退回修改按钮  事件处理
        $(".backEdit").on("click", function (e) {
            //组织传递数据
            var commitNum = $.trim($(this).attr("id").replace(/backEdit/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
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
            
            //alertmsg(instanceId+"--instanceId"+userRemarkPageInfo);
            if (!$("#userRemarkEdit" + commitNum).val()) {
                alertmsg("请填写给用户的备注信息,或填写此次退回原因");
            } else {
                //防止重复提交
                $(".btn").prop("disabled", true);
                $.ajax({
                    url: '/cm/business/ordermanage/manualUnderWritingBack',
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
                            alertmsg("退回修改操作成功！", backToMyTask);
                        } else if (data == "fail") {
                            //防止重复提交
                            $(".btn").prop("disabled", false);
                            alertmsg("退回修改操作失败！");
                        }
                    }
                });
            }

        });

        // 人工核保拒绝承保按钮事件处理
        $(".refuseInsurance").on("click", function (e) {
        	
        	//组织传递数据
            var commitNum = $.trim($(this).attr("id").replace(/refuseInsurance/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
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

            if (window.confirm("确认要拒绝承保吗？") == true) {
                
                if (userRemarkPageInfo == "" || userRemarkPageInfo == null) {
                    alertmsg("请填写给用户的备注信息");
                } else {
                    //防止重复提交
                    $(".btn").prop("disabled", true);
                    $.ajax({
                        url: '/cm/business/manualprice/refuseUnderwrite',
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
                            } else if (data.status == "fail") {
                                //防止重复提交
                                $(".btn").prop("disabled", false);
                                alertmsg("拒绝承保操作失败！");
                            }
                        }
                    });
                }
            }
        });

        //打回任务
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
            
            //alertmsg(instanceId+"-instanceId---inscomcode-"+inscomcode);
            //防止重复提交
            $(".btn").prop("disabled", true);
            $.ajax({
                url: '/cm/business/manualprice/releaseTask',
                type: 'POST',
                dataType: 'json',
                contentType: "application/json",
                //data :"{\"instanceId\":\""+instanceId+"\",\"inscomcode\":\""+inscomcode+"\"}",
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

        //重新发起核保
        $(".restart").on("click", function (e) {
            //组织传递数据
            var commitNum = $.trim($(this).attr("id").replace(/restart/, ""));
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
            //var businessProposalFormNo = $.trim($("#businessProposalFormNo").text());
            //var strongProposalFormNo = $.trim($("#strongProposalFormNo").text());
            //alertmsg(instanceId+"--instanceId"+"strongProposalFormNo"+strongProposalFormNo+"businessProposalFormNo"+businessProposalFormNo);
            //alertmsg(inscomcode+"inscomcode"+subInstanceId+"subInstanceId"+instanceId+"instanceId"+commitNum+"commitNum");
            //if(strongProposalFormNo==""&&businessProposalFormNo==""){
            //alertmsg("交强险或商业险投保单号不能为空");
            var elfquoteFlag = $("#elfquoteFlag").val();//是否有精灵报价能力

            if (inscomcode.substring(0, 4) == "2021" && inscomcode.indexOf("20214401") == -1 && inscomcode.indexOf("20214409") == -1 &&
                inscomcode.indexOf("20214418") == -1&&elfquoteFlag=="1") {
                $("*[id^='xDialog']").modal('hide');
                var temp = Math.floor(Math.random() * 1000);
                var dialog = $('<div class="modal fade" id="xDialog1' + temp + '" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"><div class="modal-dialog" style="width:90%;position:absolute;top:100px;z-index:9991;"><div class="modal-content" id="modal-content">'
                    + '<label for="exampleInputCode" style="padding:8px 5px 5px 20px;font-family:microsoft yahei;">大地加密狗密钥：</label><input type="text" class="form-control m-left-5" id="dadikey1' + temp + '" name="dadikey1' + temp + '" placeholder="" style="width:95%">'
                    + '<div class="modal-footer"><a id="savedadikey1' + temp + '" class="btn btn-success">保存</a><a id="closebutton1' + temp + '" data-dismiss="modal" name="closebutton" class="btn btn-success">关闭</a></div>'
                    + '</div></div></div>');
                dialog.appendTo(document.body).modal({
                    toggle: "modal",
                    top: "500px",
                    width: "720px",
                    backdrop: false,
                    keyboard: true
                });
                $("#savedadikey1" + temp).on("click", function (e) {
                    var keyinfo = $("#dadikey1" + temp).val();
                    if (keyinfo != '') {
                        $.ajax({
                            url: '/cm/interface/savedadikey?taskid=' + subInstanceId + "&inscomcode=" + inscomcode + "&dadikey=" + $("#dadikey1" + temp).val(),
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
                                    $('#xDialog1' + temp).modal('hide');
                                    //防止重复提交
                                    $(".btn").prop("disabled", true);
                                    $.ajax({
                                        url: '/cm/business/ordermanage/manualUnderWritingRestart',
                                        type: 'POST',
                                        dataType: 'text',
                                        contentType: "application/json",
                                        data: "{\"processinstanceid\":\"" + subInstanceId + "\",\"taskid\":\"" + instanceId + "\",\"inscomcode\":\"" + inscomcode + "\"}",
                                        cache: false,
                                        async: true,
                                        error: function () {
                                            //防止重复提交
                                            $(".btn").prop("disabled", false);
                                            alertmsg("Connection error");
                                        },
                                        success: function (data) {
                                            if (data == "success") {
                                                alertmsg("已重新发起核保！", backToMyTask);
                                            } else {
                                                //防止重复提交
                                                $(".btn").prop("disabled", false);
                                                alertmsg("重新发起核保失败！");
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
                //防止重复提交
                $(".btn").prop("disabled", true);
                $.ajax({
                    url: '/cm/business/ordermanage/manualUnderWritingRestart',
                    type: 'POST',
                    dataType: 'text',
                    contentType: "application/json",
                    data: "{\"processinstanceid\":\"" + subInstanceId + "\",\"taskid\":\"" + instanceId + "\",\"inscomcode\":\"" + inscomcode + "\"}",
                    cache: false,
                    async: true,
                    error: function () {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("Connection error");
                    },
                    success: function (data) {
                        if (data == "success") {
                            alertmsg("已重新发起核保！", backToMyTask);
                        } else {
                            //防止重复提交
                            $(".btn").prop("disabled", false);
                            alertmsg("重新发起核保失败！");
                        }
                    }
                });
            }
        });

//	核保回写
        $(".reUnderwriting").on("click", function () {
            isReub = Number($.trim($("#isReub").val()));
            var commitNum = $.trim($(this).attr("id").replace(/reUnderwriting/, ""));
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
            
//            var hasbusi = $.trim($("#hasbusi").val());
//            var hasstr = $.trim($("#hasstr").val());
            var businessProposalFormNo = $.trim($("#businessProposalFormNo").text());
            var strongProposalFormNo = $.trim($("#strongProposalFormNo").text());

//		alert(hasbusi+","+hasstr);
//            if (hasbusi == "true" && hasstr == "true") {
//                if (strongProposalFormNo == "" || businessProposalFormNo == "") {
//                    alertmsg("交强险或商业险投保单号不能为空");
//                    return;
//                } else {
//                }
//            } else {
//                if (hasbusi == "true" && hasstr == "false") {
//                    if (businessProposalFormNo == "") {
//                        alertmsg("商业险投保单号不能为空");
//                        return;
//                    } else {
//                    }
//                } else if (hasbusi == "false" && hasstr == "true") {
//                    if (strongProposalFormNo == "") {
//                        alertmsg("交强险投保单号不能为空");
//                        return;
//                    } else {
//                    }
//                }
//
//            }
            var elfquoteFlag = $("#elfquoteFlag").val();//是否有精灵报价能力

            if (inscomcode.substring(0, 4) == "2021" && inscomcode.indexOf("20214401") == -1 && inscomcode.indexOf("20214409") == -1 &&
                inscomcode.indexOf("20214418") == -1&&elfquoteFlag=="1") {
                $("*[id^='xDialog']").modal('hide');
                var temp = Math.floor(Math.random() * 1000);
                var dialog = $('<div class="modal fade" id="xDialog2' + temp + '" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"><div class="modal-dialog" style="width:90%;position:absolute;top:100px;z-index:9991;"><div class="modal-content" id="modal-content">'
                    + '<label for="exampleInputCode" style="padding:8px 5px 5px 20px;font-family:microsoft yahei;">大地加密狗密钥：</label><input type="text" class="form-control m-left-5" id="dadikey2' + temp + '" name="dadikey2' + temp + '" placeholder="" style="width:95%">'
                    + '<div class="modal-footer"><a id="savedadikey2' + temp + '" class="btn btn-success">保存</a><a id="closebutton2' + temp + '" data-dismiss="modal" name="closebutton" class="btn btn-success">关闭</a></div>'
                    + '</div></div></div>');
                dialog.appendTo(document.body).modal({
                    toggle: "modal",
                    top: "500px",
                    width: "720px",
                    backdrop: false,
                    keyboard: true
                });
                $("#savedadikey2" + temp).on("click", function (e) {
                    var keyinfo = $("#dadikey2" + temp).val();
                    if (keyinfo != '') {
                        $.ajax({
                            url: '/cm/interface/savedadikey?taskid=' + subInstanceId + "&inscomcode=" + inscomcode + "&dadikey=" + $("#dadikey2" + temp).val(),
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
                                    $('#xDialog2' + temp).modal('hide');
                                    underCount += 1;
                                    getUnderWritingInfo(instanceId, subInstanceId, inscomcode, strongProposalFormNo, businessProposalFormNo);
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
                underCount += 1;
                getUnderWritingInfo(instanceId, subInstanceId, inscomcode, strongProposalFormNo, businessProposalFormNo);
            }
        });

        function backToMyTask(i) {
        	if(!i){
        		i = 0;
        	}
            if ($(window.top.document).find("#menu").css("display") == "none") {
            	setTimeout(function (){$.cmTaskList('my', '', true)},i);
            } else {
            	setTimeout(function (){window.location.href = "/cm/business/mytask/queryTask"},i);
            }
        }

        //------------------------------------------------------------------
        //核保轮询
        $(".loopUnderwriting").on("click", function () {
            var commitNum = $.trim($(this).attr("id").replace(/loopUnderwriting/, ""));
            var instanceId = $.trim($("#taskid" + commitNum).val());
            var subInstanceId = $.trim($("#subInstanceId" + commitNum).val());
            var inscomcode = $.trim($("#inscomcode" + commitNum).val());
            var elfquoteFlag = $("#elfquoteFlag").val();//是否有精灵报价能力


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
            
            if (inscomcode.substring(0, 4) == "2021" && inscomcode.indexOf("20214401") == -1 && inscomcode.indexOf("20214409") == -1 &&
                inscomcode.indexOf("20214418") == -1&&elfquoteFlag=="1") {
                $("*[id^='xDialog']").modal('hide');
                var temp = Math.floor(Math.random() * 1000);
                var dialog = $('<div class="modal fade" id="xDialog3' + temp + '" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"><div class="modal-dialog" style="width:90%;position:absolute;top:100px;z-index:9991;"><div class="modal-content" id="modal-content">'
                    + '<label for="exampleInputCode" style="padding:8px 5px 5px 20px;font-family:microsoft yahei;">大地加密狗密钥：</label><input type="text" class="form-control m-left-5" id="dadikey3' + temp + '" name="dadikey3' + temp + '" placeholder="" style="width:95%">'
                    + '<div class="modal-footer"><a id="savedadikey3' + temp + '" class="btn btn-success">保存</a><a id="closebutton3' + temp + '" data-dismiss="modal" name="closebutton" class="btn btn-success">关闭</a></div>'
                    + '</div></div></div>');
                dialog.appendTo(document.body).modal({
                    toggle: "modal",
                    top: "500px",
                    width: "720px",
                    backdrop: false,
                    keyboard: true
                });
                $("#savedadikey3" + temp).on("click", function (e) {
                    var keyinfo = $("#dadikey3" + temp).val();
                    if (keyinfo != '') {
                        $.ajax({
                            url: '/cm/interface/savedadikey?taskid=' + subInstanceId + "&inscomcode=" + inscomcode + "&dadikey=" + $("#dadikey3" + temp).val(),
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
                                    $('#xDialog3' + temp).modal('hide');
                                    //防止重复提交
                                    $(".btn").prop("disabled", true);
                                    $.ajax({
                                        url: '/cm/business/ordermanage/loopUnderwriting',
                                        type: 'GET',
                                        dataType: 'json',
                                        data: "maininstanceId=" + instanceId + "&subInstanceId=" + subInstanceId + "&inscomcode=" + inscomcode,
                                        cache: false,
                                        async: true,
                                        error: function () {
                                            //防止重复提交
                                            $(".btn").prop("disabled", false);
                                            alertmsg("Connection error");
                                        },
                                        success: function (data) {
                                            //成功调用后跳转我的任务页面
                                            if (data.status == "success") {
                                                alertmsg("正在进行核保轮询，请在轮询状态中查看", function () {
                                                    backToMyTask();
                                                });
                                            } else {
                                                alertmsg(data.msg);
                                                //防止重复提交
                                                $(".btn").prop("disabled", false);
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
                //防止重复提交
                $(".btn").prop("disabled", true);
                $.ajax({
                    url: '/cm/business/ordermanage/loopUnderwriting',
                    type: 'GET',
                    dataType: 'json',
                    data: "maininstanceId=" + instanceId + "&subInstanceId=" + subInstanceId + "&inscomcode=" + inscomcode,
                    cache: false,
                    async: true,
                    error: function () {
                        //防止重复提交
                        $(".btn").prop("disabled", false);
                        alertmsg("Connection error");
                    },
                    success: function (data) {
                        //成功调用后跳转我的任务页面
                        if (data.status == "success") {
                            alertmsg("正在进行核保轮询，请在轮询状态中查看", function () {
                                backToMyTask();
                            });
                        } else {
                            alertmsg(data.msg);
                            //防止重复提交
                            $(".btn").prop("disabled", false);
                        }
                    }
                });
            }
        })
        
        
    });
});

function showdetail(){
	$("#showDetail").modal();
}

function getUnderWritingInfo(instanceId,subInstanceId,inscomcode,strongProposalFormNo,businessProposalFormNo){
	//防止重复提交
	$(".btn").prop("disabled", true);
	$.ajax({
		url : '/cm/business/ordermanage/reUnderwriting',
		type : 'GET',
		dataType : 'json',
		data : "maininstanceId="+instanceId+"&subInstanceId="+subInstanceId+"&inscomcode="+inscomcode+"&underCount="+underCount+"&ciPolicyNo="+strongProposalFormNo+"&biPolicyNo="+businessProposalFormNo,
		cache : false,
		async : true,
		error : function() {
			//防止重复提交
			$(".btn").prop("disabled", false);
			alertmsg("Connection error");
		},
		success : function(data) {
			if(data.status == "success"){
//				alertmsg(data.msg);
				alertmsgno(data.msg);
				//成功后操作,刷新页面
				intervalCount = setInterval("getResult("+instanceId+","+subInstanceId+","+inscomcode+")", 15000);
			}else if(data.status == "fail"){
				if(underCount < isReub){
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
	$.ajax({
		url : '/cm/business/ordermanage/getUnderWritingResult',
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
//				location.reload();
				closediv();
				$(".btn").prop("disabled", false);
				alertmsg("核保回写数据成功!", reloadTask);
			}else{
				if(resultCount==5){
					clearInterval(intervalCount);
					resultCount = 0;
					closediv();
					if(underCount < isReub){
						underCount += 1;
						getUnderWritingInfo(instanceId1,subInstanceId1,inscomcode1);
					}else{
						underCount = 0;
						//防止重复提交
						$(".btn").prop("disabled", false);
						alertmsg("核保回写数据失败", reloadTask);
					}
				}
			}
		}
	});
	
}

function reloadTask() {
	location.reload();
}
