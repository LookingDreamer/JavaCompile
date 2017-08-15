function initEditSupplyParamScript(){
	
//	15位数身份证验证正则表达式：
	var shenfenzhengRegex15 = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
//	18位数身份证验证正则表达式 ：
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
		if (msgstr == null) {
			return "";
		}else if(msgstr=="insured"){
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

	//手机号正则表达式
	//var phoneRegex = /^(1[0-9])\d{9}$/;
	var phoneRegex = /^1\d{10}$/;
	//手机号数据校验
	function checkPhone(){
		var checkList = $(".phone");
		for(var i=0;i<checkList.size();i++){
			if(!$(checkList[i]).prop("disabled")){
				var phoneVal = $.trim($(checkList[i]).val());
				if(phoneVal){
					if(!phoneRegex.test(phoneVal)){
						return null;//$(checkList[i]).attr("name").split(".")[0];
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
						return null;//$(checkList[i]).attr("name").split(".")[0];
					}
				}
			}
		}
		return "success";
	}
	//身份证有效期格式校验 yyyy-MM-dd或长期 闰年闰月校验

	var validDateRegex = /^((((?!0000)[0-9]{4}-((0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-8])|(0[13-9]|1[0-2])-(29|30)|(0[13578]|1[02])-31)|([0-9]{2}(0[48]|[2468][048]|[13579][26])|(0[48]|[2468][048]|[13579][26])00)-02-29))|(长期))$/;
	function checkValidDate(){
		var checkList = $(".validDate");
		for(var i=0;i<checkList.size();i++){
			if(!$(checkList[i]).prop("disabled")){
				var validDateVal = $.trim($(checkList[i]).val());
				var nowDate = new Date();
				if(validDateVal){
					if(!validDateRegex.test(validDateVal)){
						return null;
					}
					if(validDateVal!="长期"){
						var validDate = new Date(validDateVal);
						if(validDate.getTime()<=nowDate.getTime()){//长期的不用判断止期是否大于今天
							return "validDateFaildThrough";
						}
					}
				}
			}
		}
		return "success";
	}
	
	// 保存修改信息
	$("#submitSupplyparams").on("click",function(){
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
		var checkValidDateResult = checkValidDate();
		if(checkValidDateResult!="success"){
			if(checkValidDateResult=="validDateFaildThrough"){
				alertmsg("输入的证件有效止期要大于当天的日期");
			}else {
				alertmsg(getAlertMsg(checkValidDateResult) + "身份证有效止期不合要求！");
			}
			return;
		}
		//校验证件类型和证件号的匹配，没有填写不校验
		/*var checkResult = checkIdcardtype();
		if(checkResult!="success"){
			alertmsg(getAlertMsg(checkResult)+"证件号码不合要求！");
			return;
		}*/

		//防止重复提交
		$(this).prop("disabled", true);

		$.ajax({
			url : 'common/insurancepolicyinfo/savesupplyparam',
			type : 'POST',
			data : $("#editSupplyParamForm").serialize(),
			cache : false,
			async : false,
			success : function(data) {
				if (data == "success") {
					$("#submitSupplyparams").prop("disabled", false);
					$('#xDialog').modal('hide');
					//更新原页面指定选项卡内信息
					dataBack($("#num").val());
				}else{
					alertmsg("修改失败！请稍后重试！");
					$("#submitSupplyparams").prop("disabled", false);
				}
			},
			error : function() {
				alertmsg("Connection error");
				$("#submitSupplyparams").prop("disabled", false);
			}
		});
	});

	//数据回写方法
	function dataBack(num){
		var win;
		if($("#desktop:not(:hidden)").length>0){
			win = $(window.desktop_content.document);
		}else{
			win = $(window.frames[$("iframe[id^=fra_]:not(:hidden)").attr("id")].document);
		}

		win[0].location.reload();
	}
}	
