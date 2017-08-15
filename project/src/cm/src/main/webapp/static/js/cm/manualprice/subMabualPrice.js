require(["jquery","public"], function ($) {
	//规则试算按钮点击事件(试算一家保险公司)
//	$(".ruleCalculationBtn").click(function(){
//		var num = $.trim($(this).attr("id").replace(/ruleCalculation/, ""));//操作的选项卡下标
//		var inscomcode = $.trim($("#thisInscomcode"+num).val());//当前操作的保险公司code
//		var instanceId =  $.trim($("#taskid"+num).val());//主流程实例id
//		var subInstanceId =  $.trim($("#subInstanceId"+num).val());//子流程实例id
//		var priceLimitFlag = false;//是否限制车价浮动范围
//		if($("#carPriceRangeLimitFlag"+num).prop("checked")){
//			priceLimitFlag = true;
//		}
//		var refreshInsConfigParams = {//刷新保险配置参数
//				"instanceId":instanceId,
//				"num":num,
//				"inscomcode":inscomcode
//				};
//		var ruleCalParams = {//规则试算接口参数
//				"instanceId":instanceId,
//				"inscomcode":inscomcode,
//				"subInstanceId":subInstanceId,
//				"priceLimitFlag":priceLimitFlag
//		};
////		alertmsg(JSON.stringify(ruleCalParams));
//		//规则试算
//		$.ajax({
//			url : '/cm/business/manualprice/ruleCalculation',
//			type : 'GET',
//			dataType : 'json',
//			contentType: "application/json",
//			data :ruleCalParams,
//			cache : false,
//			async : true, 
//			error : function() {
//				alertmsg("Connection error");
//			},
//			success : function(data) {
//				if (data.success) {
//					alertmsg("规则试算成功！");
//					refreshInsConfig(refreshInsConfigParams, num);//刷新保险配置信息获取保费信息
//				}else{
//					alertmsg(data.resultMsg);
//				}
//			}
//		});
//	});
	//规则试算按钮点击事件(试算所有保险公司)
	 $('body').on("click", ".ruleCalculationBtn" , function(){
		var num = $.trim($(this).attr("id").replace(/ruleCalculation/, ""));//操作的选项卡下标
		var instanceId =  $.trim($("#taskid"+num).val());//主流程实例id
		var priceLimitFlag = false;//是否限制车价浮动范围
		if($("#carPriceRangeLimitFlag"+num).prop("checked")){
			priceLimitFlag = true;
		}
		var result = 0;
		$('.inskindnamecls0').each(function(){
			if('机动车损失保险'==$(this).text()&&($("#basicRiskPremium").text()==""||$("#basicRiskPremium").text().trim()==""||$("#basicRiskPremium").text()<=0)){
				result=1;
				return false;
		     }else if(($("#commercialClaimTimes").text()==''||$("#commercialClaimTimes").text().trim()=="")&&$("#firstInsureType").text().indexOf('非首次')>=0){
		    	result=2;
				return false;
		     }
		});
		$('.inskindnamecls1').each(function(){
			if(($("#compulsoryClaimTimes").text()==''||$("#compulsoryClaimTimes").text().trim()=="")&&$("#firstInsureType").text().indexOf('非首次')>=0){
				result=3;
				return false;
			}
		});
		if(result==1){
			alertmsg("机动车车损纯风险基准保费不能为空或零以下。");return;
		}else if(result==2){
			alertmsg("必须填写上年商业险理赔次数。");return;
		}else if(result==3){
			alertmsg("必须填写上年交强险理赔次数。");return;
		}
		$("#ischeckedprice").val(1);
		$(this).prop("disabled", true);
		var inscomcodeArray = $("input[id^=\'inscomcode"+num+"_\']");
		$(".ruleCalculationResultSub").empty();
		var disabledFlag = false;
		for(var i=0; i<inscomcodeArray.length; i++){
			if(i==(inscomcodeArray.length-1)){
				disabledFlag = true;
			}
			ruleCalculation(instanceId, i, priceLimitFlag, disabledFlag);
		}
		$(".ruleCalculationResult").css("display","block");
	});
	
	//添加试算结果
	function addRuleCalculationResult(inscomname, resultInfo){
		$(".ruleCalculationResultSub").append("<div class='col-md-2'>"+inscomname+
				"</div><div class='col-md-10'>"+resultInfo+"</div>");
	}
	//规则试算调用方法
	function ruleCalculation(instanceId, num, priceLimitFlag, disabledFlag){
		var inscomcode = $.trim($("#thisInscomcode"+num).val());//当前操作的保险公司code
		var inscomname = $.trim($("#thisComName"+num).val());//当前操作的保险公司名称
		var subInstanceId =  $.trim($("#subInstanceId"+num).val());//子流程实例id
		var inscomcodeArray = $("input[id^=\'inscomcode"+num+"_\']");
		var inscomcodeList = "";
		for(var i=0; i<inscomcodeArray.size(); i++){
			inscomcodeList += (","+$(inscomcodeArray[i]).val());
		}
		if(inscomcodeList.length>0){
			inscomcodeList.substr(1);
		}
		var refreshInsConfigParams = {//刷新保险配置参数
				"instanceId":instanceId,
				"num":num,
				"inscomname":$("#thisInscomname"+num).val(),
				"inscomcodeList":inscomcodeList,
				"inscomcode":inscomcode
				};
		var ruleCalParams = {//规则试算接口参数
				"instanceId":instanceId,
				"inscomcode":inscomcode,
				"subInstanceId":subInstanceId,
				"priceLimitFlag":priceLimitFlag
		};
		var subInfo = priceLimitFlag?"(放开浮动范围报价)":"(仅浮动范围内报价)";
//		alertmsg(JSON.stringify(ruleCalParams));
		//规则试算
		$.ajax({
			url : '/cm/business/manualprice/ruleCalculation',
			type : 'GET',
			dataType : 'json',
			contentType: "application/json",
			data :ruleCalParams,
			cache : false,
			async : true, 
			error : function() {
				alertmsg("Connection error");
				if(disabledFlag){
					$(".ruleCalculationBtn").prop("disabled", false);
				}
			},
			success : function(data) {
//				alertmsg(JSON.stringify(data));
				if (data.success) {
//					alertmsg("规则试算成功！");
//					alertmsg("totalAmountprice="+data.totalAmountprice);
					refreshInsConfig(refreshInsConfigParams, num);//刷新保险配置信息获取保费信息
//					var totalAmountprice = $("#totalAmountprice"+num).val();
					addRuleCalculationResult("<span style='color:blue;'>"+inscomname+
							"</span>", "<span style='color:green;'>试算成功，保费总额："+
							data.totalAmountprice+"元</span>&nbsp;&nbsp;&nbsp;"+subInfo);
					if(disabledFlag){
						$(".ruleCalculationBtn").prop("disabled", false);
					}
					$(".showEditInsurance").hide();
				}else{
//					alertmsg(data.resultMsg);
					addRuleCalculationResult("<span style='color:blue;'>"+inscomname+
							"</span>", "<span style='color:red;'>试算失败!</span>&nbsp;&nbsp;&nbsp;"+subInfo);
					if(disabledFlag){
						$(".ruleCalculationBtn").prop("disabled", false);
					}
				}
			}
		});
	}
	
	//刷新保险配置方法
	function refreshInsConfig(risParams, num){
		$.ajax({
			url : '/cm/common/insurancepolicyinfo/refreshInsuranceConfig',
			type : 'GET',
			dataType : 'html',
			contentType: "application/json",
			data :risParams,
			cache : false,
			async : true, 
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data) {
					$("#insuranceConfigContainer"+num).empty();
					$("#insuranceConfigContainer"+num).html(data);
					$(".editInsuranceTable"+num).hide();
					$(".insuranceTable"+num).show();
					$("#showAllRiskKind"+num).text("显示所有险别");
					//初始化保险配置
					initNotdDisabledStatus1(num);
				}else{
					alertmsg("重新获取保险配置信息失败！");
				}
			}
		});
	}
	//重新触发查询云端补充信息
	 $('body').on("click", ".getruleReplenishInfoAgain" , function(){
		var num = $.trim($(this).attr("id").replace(/getReplenishInfoAgain/, ""));//操作的选项卡下标
		var instanceId =  $.trim($("#taskid"+num).val());//主流程实例id
		var jobnum =  $("#manualPriceAgentJobNum").val();//代理人工号
		var findLastClaimBackInfoParams = {//刷新保险配置参数
				"taskId":instanceId,
				"jobnum":jobnum
				};
//		alertmsg(JSON.stringify(findLastClaimBackInfoParams));
		//防止重复点击
		//$(".getruleReplenishInfoAgain").prop("disabled", true);
		$("#getReplenishInfoAgainStatus"+num).text("");
		//重新触发查询云端补充信息
		$.ajax({
			url : '/cm/static/cif/findrulequeryBackInfo',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			data :JSON.stringify(findLastClaimBackInfoParams),
			cache : false,
			async : true, 
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
//				alertmsg(JSON.stringify(data));
//				alertmsg(data.commonModel.status);
				if (data.commonModel.status=="success") {
//					alertmsg("success");
					$("#getReplenishInfoAgainStatus"+num).text("已提交后台查询平台信息，请耐心等待20秒……");
					window.setTimeout(getReplenishInfoLaters(num),20000);//调用重新获取缓存中平台信息
				}else{
//					alertmsg("fail");
					$("#getReplenishInfoAgainStatus"+num).text("查询平台信息失败！"+(data.commonModel.message?data.commonModel.message:""));
				}
			}
		});
	});
	//重新触发查询云端补充信息
	 $('body').on("click", ".getReplenishInfoAgain" , function(){
		var num = $.trim($(this).attr("id").replace(/getReplenishInfoAgain/, ""));//操作的选项卡下标
		var instanceId =  $.trim($("#taskid"+num).val());//主流程实例id
		var jobnum =  $("#manualPriceAgentJobNum").val();//代理人工号
		var findLastClaimBackInfoParams = {//刷新保险配置参数
				"taskId":instanceId,
				"jobnum":jobnum
				};
//		alertmsg(JSON.stringify(findLastClaimBackInfoParams));
		//防止重复点击
		$(".getReplenishInfoAgain").prop("disabled", true);
		$("#getReplenishInfoAgainStatus"+num).text("");
		//重新触发查询云端补充信息
		$.ajax({
			url : '/cm/static/cif/findLastClaimBackInfo',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			data :JSON.stringify(findLastClaimBackInfoParams),
			cache : false,
			async : true, 
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
//				alertmsg(JSON.stringify(data));
//				alertmsg(data.commonModel.status);
				if (data.commonModel.status=="success") {
//					alertmsg("success");
					$("#getReplenishInfoAgainStatus"+num).text("已提交后台查询平台信息，请耐心等待20秒……");
					window.setTimeout(getReplenishInfoLaters(num),20000);//调用重新获取缓存中平台信息
				}else{
//					alertmsg("fail");
					$("#getReplenishInfoAgainStatus"+num).text("查询平台信息失败！"+(data.commonModel.message?data.commonModel.message:""));
				}
			}
		});
	});
	function getReplenishInfoLaters(num){
		return function(){
			getReplenishInfoLater(num)
		}
	}
	//重新查询缓存补充信息
	function getReplenishInfoLater(num){
		var inscomcode = $.trim($("#thisInscomcode"+num).val());//当前操作的保险公司code
		var instanceId =  $.trim($("#taskid"+num).val());//主流程实例id
		var subInstanceId =  $.trim($("#subInstanceId"+num).val());//子流程实例id
		var getReplenishInfoParams = {//刷新保险配置参数
				"instanceId":instanceId,
				"inscomcode":inscomcode,
				"subInstanceId":subInstanceId
				};
//		alertmsg(JSON.stringify(getReplenishInfoParams));
		//重新查询缓存补充信息
		$.ajax({
			url : '/cm/business/manualprice/getReplenishInfo',
			type : 'GET',
			dataType : 'json',
			contentType: "application/json",
			data :getReplenishInfoParams,
			cache : false,
			async : true, 
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data.status=="success") {
//					alertmsg("success");
					$("#getReplenishInfoAgainStatus"+num).text("平台信息查询完毕！刷新数据中...");
					refreshReplenishInfo(data,num);//调用刷新补充信息内容方法
					setTimeout(function(){
						if(parent.window.fra_businessmytaskqueryTask){
							parent.window.fra_businessmytaskqueryTask.location.reload();
						}else if(parent.window.desktop_content){
							parent.window.desktop_content.location.reload();
						}
					},5000);
					$(".failToHide"+num).css('display', 'block');
				}else{
//					alertmsg("fail");
					$("#getReplenishInfoAgainStatus"+num).text("平台信息查询失败！");
//					$(".failToHide"+num).css('display', 'none');
				}
			}
		});
	}
	//刷新补充信息内容方法
	function refreshReplenishInfo(data,num){
//		alertmsg(JSON.stringify(data));
		$.each(data,function(key,value) {
//			alertmsg(key+">>"+value);
			freshItemInfo(key, value,num);
		});
	}
	function freshItemInfo(itemClassName, itemInfo, num){
		if($("#"+itemClassName+"UpPageInfo"+num).length>0){
			$("#"+itemClassName+"UpPageInfo"+num).text(itemInfo!=null?itemInfo:"");
		}
	}
	function checkHideReplenishInfo(){
		$("label[id^='periodOfValidityUpPageInfo']").each(function(i){
			if(!($(this).text())){
				var thisNum = $(this).attr("id").replace(/periodOfValidityUpPageInfo/, "");
				$(".failToHide"+thisNum).css('display', 'none');
			}
		 });
	}
	checkHideReplenishInfo();
	//初始化保险配置js方法
	function initNotdDisabledStatus1(indexnum){
		//初始化不计免赔的选框disabled状态
		$("#insuranceConfigContainer"+indexnum).find(".checkItemsAll").each(function(){
			var checkItemsId = $(this).attr("id");
			var num = checkItemsId.split('_')[0];
			var checkItemsPreriskkind = $("#"+checkItemsId+"_preriskkind").val();
			var isDisabled = true;
			if(checkItemsPreriskkind){
				var preriskkindList = checkItemsPreriskkind.split(',');
				for(var i=0;i<preriskkindList.length;i++){
					if($("#"+num+"_"+preriskkindList[i]).length>0){
						if($("#"+num+"_"+preriskkindList[i]).prop("checked")){
							isDisabled = false;
							break;
						}
					}
				}
				if(isDisabled){
					$(this).prop("checked",false);
					$(this).prop("disabled",true);
				}else{
					$(this).prop("disabled",false);
				}
			}
		});
		//商业险种的勾选框点击时判断其不计免赔存在时做disabled切换操作
		$("#insuranceConfigContainer"+indexnum).find(".checkItemsAll").on('click', function(){
			var checkItemsId = $(this).attr("id");
			var num = checkItemsId.split('_')[0];
			var checkKindcode = checkItemsId.split('_')[1];
			$(".checkItemsAll[id^="+num+"]").each(function(){
				var ItemsIdTemp = $(this).attr("id");
				var ItemsPreriskkind = $("#"+ItemsIdTemp+"_preriskkind").val();
				var isDisabled = true;
				if(ItemsPreriskkind){
					var preriskkindList = ItemsPreriskkind.split(',');
					if($.inArray(checkKindcode, preriskkindList) != -1){//判断前置险种是否包含此险别
						for(var i=0;i<preriskkindList.length;i++){
							if($("#"+num+"_"+preriskkindList[i]).length>0){
								if($("#"+num+"_"+preriskkindList[i]).prop("checked")){
									isDisabled = false;
									break;
								}
							}
						}
						if(isDisabled){
							$(this).prop("checked",false);
							$(this).prop("disabled",true);
						}else{
							$(this).prop("disabled",false);
						}
					}
				}
			});
		});
		//初始化需要隐藏的险别
		$("#insuranceConfigContainer"+indexnum).find("input.checkItemsAll:not(:checked)").parents("tr").hide();
	}
	
});
function ceshi(num){
	alert(num+"cececcececece");
}
