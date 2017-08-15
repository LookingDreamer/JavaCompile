function initEditRelationPersonInfoScript(){
//	$("#personMessage").validate();
	//页面加载后判断其他人是否和被保人一致，如果一致禁用修改信息
	var insuredid = $.trim($("#insuredid").val());
	var applicantid = $.trim($("#applicantid").val());
	var linkPersonid = $.trim($("#linkPersonid").val());
	var personForRightid = $.trim($("#personForRightid").val());
	if(applicantid==insuredid){
		changePersonInfo("#form"+"1","");
	}
	if(linkPersonid==insuredid){
		changePersonInfo("#form"+"2","");
	}
	if(personForRightid==insuredid){
		changePersonInfo("#form"+"3","");
	}
	//投保人信息  "是"操作
	$("#yes1").click(function(){
		changePersonInfo("#form"+"1","");
	});
	//投保人信息  "否"操作
	$("#no1").click(function(){
		changePersonInfo("","#form"+"1");
	});
	//车主信息  "是"操作
	$("#yes2").click(function(){
		changePersonInfo("#form"+"2","");
	});
	//车主信息  "否"操作
	$("#no2").click(function(){
		changePersonInfo("","#form"+"2");
	});
	//权益索赔人信息  "是"操作
	$("#yes3").click(function(){
		changePersonInfo("#form"+"3","");
	});
	//权益索赔人信息  "否"操作
	$("#no3").click(function(){
		changePersonInfo("","#form"+"3");
	});
	//禁用表单公共方法
	function changePersonInfo(formtrue,formfalse){
		if(formtrue){
			$(formtrue).find("input[type=text]").attr("disabled",true);
			$(formtrue).find("select").attr("disabled",true);
		}else if(formfalse){
			$(formfalse).find("input[type=text]").removeAttr("disabled");
			$(formfalse).find("select").removeAttr("disabled");
		}
	}
	
	//数据回写方法
	function dataBack(num){
		var win;
		if($("#desktop:not(:hidden)").length>0){
			win = $(window.desktop_content.document);
		}else{
			win = $(window.frames[$("iframe[id^=fra_]:not(:hidden)").attr("id")].document);
		}
		win.find("#insuredNamePageInfo"+num).text($("#insuredName").val());//被保人姓名
		win.find("#carownernamePageInfo"+num).text($("#carOwnerInfoName").val());//车主姓名
		win.find("#applicantNamePageInfo"+num).text(($("#yes1").prop("checked"))?$("#insuredName").val():$("#applicantName").val());//投保人姓名
		win.find("#personForRightNamePageInfo"+num).text(($("#yes3").prop("checked"))?$("#insuredName").val():$("#personForRightName").val());//权益索赔人姓名
		win.find("#linkPersonNamePageInfo"+num).text(($("#yes2").prop("checked"))?$("#insuredName").val():$("#linkPersonName").val());//联系人姓名
	}
	
//	15位数身份证验证正则表达式：
//	isIDCard1=/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/; 
//	18位数身份证验证正则表达式 ：
//	isIDCard2=/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
	var shenfenzhengRegex15 = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
	var shenfenzhengRegex18 = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
	//社会信用代码证正则表达式：
	var xinyongdaimaRegex = /^[a-zA-Z0-9]{2}(\d{6})[a-zA-Z0-9]{10}$/;
	//组织代码证正则表达式：
	var zuzhidaimaRegex=/^[a-zA-Z0-9]{1}([a-zA-Z0-9]{7})[-][0-9]{1}$/;
	//香港身份证正则表达式：
	var hkRegex = /^[A-Z]{1}[0-9]{6}\([0-9A]\)$/;

	//香港身份证号码校验码位校验
	function checkHkIdcarNum(cardNum) {
		var checkNum = cardNum.substring(cardNum.indexOf("(") + 1, cardNum.indexOf(")"));
		var sum = 0;
		var n = 0;
		for (var i = 8; i >= 2; i--, n++) {
			var temp;
			var index = cardNum.charAt(n);
			if (n == 0) {
				temp = index.charCodeAt(0) - 64;
			} else {
				temp = index.charCodeAt(0) - 48;
			}
			sum = sum + temp * i;
		}
		var result = sum % 11;
		if (result == 0) {
			return checkNum == "0";
		} else if (result == 1) {
			return checkNum == "A";
		} else {
			var modNum = 11 - result;
			return checkNum == modNum;
		}
	}
	
	//根据证件类型校验证件号码
	function checkIdcardtype(){
		var checkList = $(".idcardtype");
		for(var i=0;i<checkList.size();i++){
			if(!$(checkList[i]).prop("disabled")){
				var ictv = $(checkList[i]).val();
				var icvidfor = $(checkList[i]).attr("id").replace("idcardtype", "");
				var idNoVal = $.trim($("#"+icvidfor+"idcardno").val());
				if(idNoVal){
					if(ictv=="0"){//身份证校验
						if(idNoVal.length==15){
							if(!shenfenzhengRegex15.test(idNoVal)){
								return icvidfor;
							}
						}else if(idNoVal.length==18){
							if(!shenfenzhengRegex18.test(idNoVal)){
								return icvidfor;
							}
						}else{
							return icvidfor;
						}
					}else if(ictv=="8"){//社会信用代码证校验
						if(!xinyongdaimaRegex.test(idNoVal)){
							return icvidfor;
						}
					}else if(ictv=="6"){//组织代码证校验
						if(!zuzhidaimaRegex.test(idNoVal)){
							return icvidfor;
						}
					}else if(ictv == "11") {//香港身份证校验
						if (idNoVal.indexOf("（") != -1 || idNoVal.indexOf("）") != -1) {
							alertmsg("请输入正确的" + getAlertMsg(icvidfor) + "证件号码（括号符号为西文符号）");
							return;
						}
						if (!hkRegex.test(idNoVal)) {
							return icvidfor;
						}
						var result = checkHkIdcarNum(idNoVal);
						if (result == false) {
							return icvidfor;
						}
					}
				}
			}
		}
		return "success";
	}
	function getAlertMsg(msgstr){
		if(msgstr=="insured"){
			return "被保人";
		}else if(msgstr=="applicant"){
			return "投保人";
		}else if(msgstr=="linkPerson"){
			return "联系人";
		}else if(msgstr=="personForRight"){
			return "权益索赔人";
		}else if(msgstr=="carOwnerInfo"){
			return "车主";
		}
	}
	//校验被保人和车主信息不能为空
	function checkInsuredAndCarOwnerIsEmpty(){
		var iaoInfo = $(".required");
		for(var i=0;i<iaoInfo.size();i++){
			if(!($.trim($(iaoInfo[i]).val()))){
				return $(iaoInfo[i]).attr("name").split(".")[0];
			}
		}
		return "success";
	}
	
	//手机号正则表达式
	//var phoneRegex = /^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\d{8}$/;
	var phoneRegex = /^1\d{10}$/;
	//手机号数据校验
	function checkPhone(){
		var checkList = $(".phone");
		for(var i=0;i<checkList.size();i++){
			if(!$(checkList[i]).prop("disabled")){
				var phoneVal = $.trim($(checkList[i]).val());
				if(phoneVal){
					if(!phoneRegex.test(phoneVal)){
						return $(checkList[i]).attr("name").split(".")[0];
					}
				}
			}
		}
		return "success";
	}
	
	//邮箱正则表达式
	var emailRegex = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
	//邮箱数据校验
	function checkEmail(){
		var checkList = $(".email");
		for(var i=0;i<checkList.size();i++){
			if(!$(checkList[i]).prop("disabled")){
				var emailVal = $.trim($(checkList[i]).val());
				if(emailVal){
					if(!emailRegex.test(emailVal)){
						return $(checkList[i]).attr("name").split(".")[0];
					}
				}
			}
		}
		return "success";
	}
	
	// 保存关系人修改信息
	$("#makesure").on("click",function(){
		//校验被保人和车主信息不能为空
		var isEmptyResult = checkInsuredAndCarOwnerIsEmpty();
		if(isEmptyResult!="success"){
			alertmsg(getAlertMsg(isEmptyResult)+"信息不完整！");
			return;
		}
		//校验邮箱手机号是否规范，没有填写不校验
		var checkPhoneResult = checkPhone();
		if(checkPhoneResult!="success"){
			alertmsg(getAlertMsg(checkPhoneResult)+"手机号码不合要求！");
			return;
		}
		var checkEmailResult = checkEmail();
		if(checkEmailResult!="success"){
			alertmsg(getAlertMsg(checkEmailResult)+"邮箱不合要求！");
			return;
		}
		//校验证件类型和证件号的匹配，没有填写不校验
		var checkResult = checkIdcardtype();
		if(checkResult!="success"){
			alertmsg(getAlertMsg(checkResult)+"证件号码不合要求！");
			return;
		}
		//防止重复提交
		$(this).prop("disabled", true);
		$.ajax({
			url : 'common/insurancepolicyinfo/editRelationPersonInfo',
			type : 'POST',
			data : $("#editRelationPersonInfoForm").serialize(),
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$("#makesure").prop("disabled", false);
			},
			success : function(data) {
				if (data == "success") {
//				alertmsg("修改其他信息成功！");
					//更新原页面指定选项卡内信息
					dataBack($("#num").val());
					$('#xDialog').modal('hide');
				}else{
					alertmsg("修改失败！请稍后重试！");
					//防止重复提交
					$("#makesure").prop("disabled", false);
				}
			}
		});
	});
	
	
}	
