require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public"], function ($) {
    $(function() {
        //保存到单方保险配置
        $(".saveEditInsurance").on("click", function () {
            //获取要提交的选项卡
            var num = $.trim($(this).attr("id").replace(/saveEditInsurance/, ""));
            //与上年一致是否勾选
            var issameaslastyearchecked = $("#insureconfigsameaslastyear" + num + ":checked").size() > 0;
            //获得要提交的选项卡内的选中的商业交强险车船税险别集合
            var commitItems = $(".checkItems_" + num + ":checked");
            //商业交强险车船税--勾选保额必须填写且为数字--选项要素必须选择
            for (var i = 0; i < commitItems.size(); i++) {
                var checkItemsName = $(commitItems[i]).attr("name");
                if (issameaslastyearchecked && checkItemsName && (checkItemsName.indexOf("busiKindprice") > -1 || checkItemsName.indexOf("notdKindprice") > -1)) {
                    alertmsg("保险配置不能同时选择“与上年一致”");
                    return;
                }
                var checkItemsId = $(commitItems[i]).attr("id");
                var commitItemName = $("#" + checkItemsId + "_inskindname").val();
                var commitItemAmount = $("#" + checkItemsId + "_amount");
                var commitItemAmountprice = $.trim($("#" + checkItemsId + "_amountprice").val());
                var commitItemAmountSlecet = $("." + checkItemsId + "_amountSlecet");
                var commitItemSpecialRiskkindValue = $("#" + checkItemsId + "_specialRiskkindValue");//特殊险别修理期间费用补偿险天数校验
                if (commitItemAmount.length > 0) {
                    if ($.trim(commitItemAmount.val())) {
                        if (isNaN($.trim(commitItemAmount.val()))) {
                            alertmsg(commitItemName + "的保额项填写格式不正确！");
                            return;
                        } else {
                            if (Number($.trim(commitItemAmount.val())) < 0) {
                                alertmsg(commitItemName + "的保额项不能为负数！");
                                return;
                            }
                        }
                    } else {
                        alertmsg(commitItemName + "的保额项不能为空！");
                        return;
                    }
                }
                //如果有特殊险别修理期间费用补偿险，天数校验
                if (commitItemSpecialRiskkindValue.length > 0) {
                    if ($.trim(commitItemSpecialRiskkindValue.val())) {
                        if (isNaN($.trim(commitItemSpecialRiskkindValue.val()))) {
                            alertmsg(commitItemName + "的数据项填写格式不正确！");
                            return;
                        } else {
                            if (Number($.trim(commitItemSpecialRiskkindValue.val())) < 0) {
                                alertmsg(commitItemName + "的数据项不能为负数！");
                                return;
                            }
                        }
                    } else {
                        alertmsg(commitItemName + "的数据项不能为空！");
                        return;
                    }
                }
                if (commitItemAmountprice) {
                    if (isNaN(commitItemAmountprice)) {
                        alertmsg(commitItemName + "的保费项填写格式不正确！");
                        return;
                    } else {
                        if (Number(commitItemAmountprice) < 0) {
                            alertmsg(commitItemName + "的保费项不能为负数！");
                            return;
                        }
                    }
                } else {
                    alertmsg(commitItemName + "的保费项不能为空！");
                    return;
                }
//			if(commitItemAmountSlecet){
//				for(var j=0;j<commitItemAmountSlecet.size();j++){
//					if($(commitItemAmountSlecet[j]).val()){
//						alertmsg("请将"+commitItemName+"的选择要素填写完整！");
//						return;
//					}
//				}
//			}
            }
            //校验商业险和交强险折扣率
            var busdiscountrateNode = $("#" + num + "_busdiscountrate");
            var strdiscountrateNode = $("#" + num + "_strdiscountrate");
            if (busdiscountrateNode.length > 0) {
                var busdiscountrate = $.trim(busdiscountrateNode.val());
                if (busdiscountrate) {
                    if (isNaN(busdiscountrate)) {
                        alertmsg("商业险折扣率填写格式不正确！");
                        return;
                    }
                } else {
                    alertmsg("商业险折扣率不能为空！");
                    return;
                }
            }
            if (strdiscountrateNode.length > 0) {
                var strdiscountrate = $.trim(strdiscountrateNode.val());
                if (strdiscountrate) {
                    if (isNaN(strdiscountrate)) {
                        alertmsg("交强险折扣率填写格式不正确！");
                        return;
                    }
                } else {
                    alertmsg("交强险折扣率不能为空！");
                    return;
                }
            }
            //防止重复提交
            $(this).prop("disabled", true);
            $.ajax({
                url: '/cm/common/insurancepolicyinfo/saveInsuranceConfig',
                type: 'POST',
                dataType: 'html',
                data: $("#insurancePolicyInfoForm" + num).serialize(),
                cache: false,
                async: true,
                error: function () {
                    alertmsg("Connection error");
                    //防止重复提交
                    $(this).prop("disabled", false);
                },
                success: function (data) {
                	//alert(data);
                	if(data.indexOf("changingflagtrue")>0){
                		alertmsg("此任务被关闭或者修改处理人，请返回任务列表！");
                	}else{
                		//防止重复提交
                        $("#saveEditInsurance" + num).prop("disabled", false);
                        if (data) {
                            var isEditAll = $("#edit2AllList" + num).prop("checked");
                            if (isEditAll) {
                                var inscomcodeArray = $("input[id^=\'inscomcode" + num + "_\']");
                                var inscomcodeList = "";
                                for (var i = 0; i < inscomcodeArray.size(); i++) {
                                    inscomcodeList += ("," + $(inscomcodeArray[i]).val());
                                }
                                if (inscomcodeList.length > 0) {
                                    inscomcodeList.substr(1);
                                }
                                var instanceId1 = $("#taskid" + num).val();
                                for (var i = 0; i < inscomcodeArray.length; i++) {
                                    refreshSingleInsConfig({//刷新保险配置参数
                                        "instanceId": instanceId1,
                                        "num": i,
                                        "inscomname": $("#thisInscomname" + i).val(),
                                        "inscomcodeList": inscomcodeList,
                                        "inscomcode": $(inscomcodeArray[i]).val()
                                    }, i);
                                }
                            } else {
                                $EditInstoggle(num);
                                $("#insuranceConfigContainer" + num).empty();
                                $("#insuranceConfigContainer" + num).html(data);
                                $("#showAllRiskKind" + num).text("显示所有险别");
                                //初始化保险配置
                                initNotdDisabledStatus2(num);
                                if(parent.window.fra_businessmytaskqueryTask){
        							parent.window.fra_businessmytaskqueryTask.location.reload();
        						}else if(parent.window.desktop_content){
        							parent.window.desktop_content.location.reload();
        						}
                            }
//    					alertmsg("修改保险配置信息成功！");
                        } else {
                            alertmsg("修改失败！请稍后重试！");
                            $EditInstoggle(num);
                        }
                	}
                }
            });
        });
        //保险配置修改显示隐藏切换方法
        $EditInstoggle = function (num) {
            $(".insuranceTable" + num).toggle();
            $(".editInsuranceTable" + num).toggle();
        };
        //隐藏或显示保险配置编辑表格
        $(".showEditInsurance").click(function () {
            var num = $(this).attr("id").replace(/showEditInsurance/, "");
            $EditInstoggle(num);
            checkIns();
        });

        //下载全部
        $("#downloadAllId").click(function () {
                // 取得要提交的参数
                var taskId = "taskid";//$.trim($('#ipt').val());
                var filePaths = "";
                $("input:checkbox[name=allImgPathId]:checked").each(function(i){
                    if(0==i){
                        filePaths = $(this).val();
                    }else{
                        filePaths += (","+$(this).val());
                    }
                });
                if(filePaths.length<=0){
                    alert("无影像下载")
                    return false;
                }
                // 取得要提交页面的URL
                var action = "/cm/mobile/registered/filesPackDownload";
                try {
                    var downloadAllForm = document.getElementById("downloadAllFormId")
                    document.body.removeChild(downloadAllForm);
                    console.trace("删除之前的downloadAllForm成功");
                } catch(err) {
                    console.trace("删除之前的downloadAllForm失败，之前没有");
                }
                // 创建Form
                var form = $('<form id="downloadAllFormId" style="display: none"></form>');
                // 设置属性
                form.attr('action', action);
                form.attr('method', 'post');
                // form的target属性决定form在哪个页面提交
                // _self -> 当前页面 _blank -> 新页面
                form.attr('target', '_self');
                // 创建Input
                var taskId_input = $('<input type="text" name="taskId" />');
                taskId_input.attr('value', taskId);
                var filePaths_input = $('<input type="text" name="files" />');
                filePaths_input.attr('value', filePaths);
                // 附加到Form
                form.append(taskId_input);
                form.append(filePaths_input);
                $(document.body).append(form);
                // 提交表单
                form.submit();
        });

        //初始化保险配置js方法
        function initNotdDisabledStatus() {
            //初始化不计免赔的选框disabled状态
            $(".checkItemsAll").each(function () {
                var checkItemsId = $(this).attr("id");
                var num = checkItemsId.split('_')[0];
                var checkItemsPreriskkind = $("#" + checkItemsId + "_preriskkind").val();
                var isDisabled = true;
                if (checkItemsPreriskkind) {
                    var preriskkindList = checkItemsPreriskkind.split(',');
                    for (var i = 0; i < preriskkindList.length; i++) {
                        if ($("#" + num + "_" + preriskkindList[i]).length > 0) {
                            if ($("#" + num + "_" + preriskkindList[i]).prop("checked")) {
                                isDisabled = false;
                                break;
                            }
                        }
                    }
                    if (isDisabled) {
                        $(this).prop("checked", false);
                        $(this).prop("disabled", true);
                    } else {
                        $(this).prop("disabled", false);
                    }
                }
            });
            //商业险种的勾选框点击时判断其不计免赔存在时做disabled切换操作
            $(".checkItemsAll").on('click', function () {
                var checkItemsId = $(this).attr("id");
                var num = checkItemsId.split('_')[0];
                var checkKindcode = checkItemsId.split('_')[1];

                if ($(this).prop("checked")) {
                    var checkItemsName = $(this).attr("name");
                    if (checkItemsName && (checkItemsName.indexOf("busiKindprice") > -1 || checkItemsName.indexOf("notdKindprice") > -1)) {
                        $("#insureconfigsameaslastyear" + num).prop("checked", false);
                    }
                }

                $(".checkItemsAll[id^=" + num + "]").each(function () {
                    var ItemsIdTemp = $(this).attr("id");
                    var ItemsPreriskkind = $("#" + ItemsIdTemp + "_preriskkind").val();
                    var isDisabled = true;
                    if (ItemsPreriskkind) {
                        var preriskkindList = ItemsPreriskkind.split(',');
                        if ($.inArray(checkKindcode, preriskkindList) != -1) {//判断前置险种是否包含此险别
                            for (var i = 0; i < preriskkindList.length; i++) {
                                if ($("#" + num + "_" + preriskkindList[i]).length > 0) {
                                    if ($("#" + num + "_" + preriskkindList[i]).prop("checked")) {
                                        isDisabled = false;
                                        break;
                                    }
                                }
                            }
                            if (isDisabled) {
                                $(this).prop("checked", false);
                                $(this).prop("disabled", true);
                            } else {
                                $(this).prop("disabled", false);
                            }
                        }
                    }
                });
                checkIns();
            });
            //勾选与上年一致
            $("input[name=insureconfigsameaslastyear]").on('click', function () {
                checkIns();
            });
            //初始化需要隐藏的险别
            $("input.checkItemsAll:not(:checked)").parents("tr").hide();
        }

        //检查险种是否已选择
        function checkIns(){
            var mainInsurace = $("input[name^=busiKindprice]:checked").length;//已选择商业主险个数
            var subInsurace = $("input[name^=notdKindprice]:checked").length;//已选择商业险附加险&不计免赔个数
            var flag = $("input[id$=_VehicleCompulsoryIns]").is(":checked");//是否选择交强险
            var busRate = $("input[name=busRate]").val();
            var strRate = $("input[name=strRate]").val();
            var count = mainInsurace + subInsurace;
            var index = $("input[id$=_VehicleCompulsoryIns]").attr("id");
            index = index.substr(0, index.indexOf("_"));
            if (count > 0) {
                var target = "<input type='text' id='" + index + "_busdiscountrate' name='busdiscountrate' value='" + busRate + "'}' />";
                $(".busdiscountrate").html(target);
            } else {
                $(".busdiscountrate").html("<span style='color:red;'>无保单数据，不支持修改</span>");
            }
            if (flag) {
                var target = "<input type='text' id='" + index + "_strdiscountrate' name='strdiscountrate' value='" + strRate + "'}' />";
                $(".strdiscountrate").html(target);
            } else {
                $(".strdiscountrate").html("<span style='color:red;'>无保单数据，不支持修改</span>");
            }
        }

        //初始化保险配置
        initNotdDisabledStatus();
        //更新保险配置后局部初始化操作
        function initNotdDisabledStatus2(indexnum) {
            //初始化不计免赔的选框disabled状态
            $("#insuranceConfigContainer" + indexnum).find(".checkItemsAll").each(function () {
                var checkItemsId = $(this).attr("id");
                var num = checkItemsId.split('_')[0];
                var checkItemsPreriskkind = $("#" + checkItemsId + "_preriskkind").val();
                var isDisabled = true;
                if (checkItemsPreriskkind) {
                    var preriskkindList = checkItemsPreriskkind.split(',');
                    for (var i = 0; i < preriskkindList.length; i++) {
                        if ($("#" + num + "_" + preriskkindList[i]).length > 0) {
                            if ($("#" + num + "_" + preriskkindList[i]).prop("checked")) {
                                isDisabled = false;
                                break;
                            }
                        }
                    }
                    if (isDisabled) {
                        $(this).prop("checked", false);
                        $(this).prop("disabled", true);
                    } else {
                        $(this).prop("disabled", false);
                    }
                }
            });
            //商业险种的勾选框点击时判断其不计免赔存在时做disabled切换操作
            $("#insuranceConfigContainer" + indexnum).find(".checkItemsAll").on('click', function () {
                var checkItemsId = $(this).attr("id");
                var num = checkItemsId.split('_')[0];
                var checkKindcode = checkItemsId.split('_')[1];
                $(".checkItemsAll[id^=" + num + "]").each(function () {
                    var ItemsIdTemp = $(this).attr("id");
                    var ItemsPreriskkind = $("#" + ItemsIdTemp + "_preriskkind").val();
                    var isDisabled = true;
                    if (ItemsPreriskkind) {
                        var preriskkindList = ItemsPreriskkind.split(',');
                        if ($.inArray(checkKindcode, preriskkindList) != -1) {//判断前置险种是否包含此险别
                            for (var i = 0; i < preriskkindList.length; i++) {
                                if ($("#" + num + "_" + preriskkindList[i]).length > 0) {
                                    if ($("#" + num + "_" + preriskkindList[i]).prop("checked")) {
                                        isDisabled = false;
                                        break;
                                    }
                                }
                            }
                            if (isDisabled) {
                                $(this).prop("checked", false);
                                $(this).prop("disabled", true);
                            } else {
                                $(this).prop("disabled", false);
                            }
                        }
                    }
                });
            });
            //初始化需要隐藏的险别
            $("#insuranceConfigContainer" + indexnum).find("input.checkItemsAll:not(:checked)").parents("tr").hide();
        }

        //刷新保险配置方法
        function refreshSingleInsConfig(risParams, num) {
            $.ajax({
                url: '/cm/common/insurancepolicyinfo/refreshInsuranceConfig',
                type: 'GET',
                dataType: 'html',
                contentType: "application/json",
                data: risParams,
                cache: false,
                async: true,
                error: function () {
                    alertmsg("Connection error");
                },
                success: function (data) {
                    if (data) {
                        $("#insuranceConfigContainer" + num).empty();
                        $("#insuranceConfigContainer" + num).html(data);
                        $(".editInsuranceTable" + num).hide();
                        $(".insuranceTable" + num).show();
                        $("#showAllRiskKind" + num).text("显示所有险别");
                        //初始化保险配置
                        initNotdDisabledStatus2(num);
                    } else {
                        alertmsg("重新获取保险配置信息失败！");
                    }
                }
            });
        }

        //显示所有险别和收起未选择险别功能
        $(".showAllRiskKind").on('click', function () {
            //获取操作的选项卡编号
            var num = $.trim($(this).attr("id").replace(/showAllRiskKind/, ""));
            if ($(this).attr("title") == "showAll") {
                //显示没有选择的险别
                $("div.editInsuranceTable" + num).find("tr:hidden").show();
                $(this).attr("title", "unShowAll");
                $(this).text("收起未选险别");
            } else if ($(this).attr("title") == "unShowAll") {
                //隐藏没有选择的险别
                $("div.editInsuranceTable" + num).find("input.checkItemsAll:not(:checked)").parents("tr").hide();
                $(this).attr("title", "showAll");
                $(this).text("显示所有险别");
            }
        })
    });
});
