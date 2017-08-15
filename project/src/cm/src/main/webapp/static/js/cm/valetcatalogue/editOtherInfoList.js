function initEditOtherInfoScript() {
	//时间控件初始化设置
	var today = $("#today").val();
	//从第二天的凌晨开始
	var todays=new Date(today);
	var todaytime=todays.getTime()+(24*3600*1000);
	var todaydates=new Date(todaytime);
	//到90天的第二天结束
	var nintytodays=new Date(today);
	var time=nintytodays.getTime()+(90*24*3600*1000);
	var newninetydates=new Date(time);
	 $('.form_datetime').datetimepicker({
	      language: 'zh-CN',
	      format: "yyyy-mm-dd",
	      weekStart: 1,
	      todayBtn: 1,
	      autoclose: 1,
	      todayHighlight: 1,
	      startView: 2,
	      forceParse: 0,
	      minView: 2,
	      pickerPosition: "bottom-right",
	      startDate:todaydates,
	      endDate:newninetydates
	      // showMeridian: 1
	    });
//	 $('.form_endtime').datetimepicker({
//		 language: 'zh-CN',
//	     format: "yyyy-mm-dd",
//	     weekStart: 1,
//	     todayBtn: 1,
//	     autoclose: 1,
//	     todayHighlight: 1,
//	     startView: 2,
//	     forceParse: 0,
//	     minView: 2,
//	     pickerPosition: "bottom-right",
//	    });
	//起保日期和终止日期联动操作
	$(".changeEnddate").on("change", function(){
		var dateStrVal = $.trim($(this).val());
		var dateId = $(this).attr("id").replace("startdateTxt", "enddate");
		if(dateStrVal){//时间有数据
			var dateVal = new Date(dateStrVal.replace(/-/g,"/"));
			dateVal.setFullYear(dateVal.getFullYear()+1); 
			dateVal.setDate(dateVal.getDate()-1); 
			$("#"+dateId).val(dateVal.getFullYear()+"-"+(dateVal.getMonth()<9?("0"+(dateVal.getMonth()+1)):(dateVal.getMonth()+1))
					+"-"+dateVal.getDate());
		}else{//时间没有数据
			$("#"+dateId).val("");
		}
	});
	//通过是否指定驾驶人判断置灰驾驶人选择修改信息
	$("#pointspecifydriver").change(function(){
		var isFlag = $.trim($("#pointspecifydriver").val());
		if(isFlag == "1"){
			$(".related").removeAttr("disabled");
		}else if(isFlag == "0"){
			$(".related").attr("disabled","true");
		}
		$("div.related").toggle();
	})
	
	//商业险或交强险是否有记录标记
	var isHasbusIns = true;
	var isHasstrIns = true;
	if($("#busiDate").css("display")=="none"){
		isHasbusIns = false;
	}
	if($("#stroDate").css("display")=="none"){
		isHasstrIns = false;
	}
	// 修改其他信息提交按钮
	$("#makesure").on("click", function(e) {
		//数据校验
		//商业险数据校验
//		if(isHasbusIns){
//			var BSDStr = $("#businessstartdate").val();
//			var BEDStr = $("#businessenddate").val();
//			if(BSDStr && BEDStr && $.trim(BSDStr) && $.trim(BEDStr)){
//				//商业险起始日期应早于终止日期
//				var businessstartdate = new Date($.trim(BSDStr).replace(/-/g,"/"));
//				var businessenddate = new Date($.trim(BEDStr).replace(/-/g,"/"));
//				if(businessstartdate >= businessenddate){
//					alertmsg("商业险起保日期应早于终止日期！");
//					return;
//				}
//			}else{
//				if(!((!(BSDStr && $.trim(BSDStr))) && (!(BEDStr && $.trim(BEDStr))))){
//					//商业险起止日期不应只存在一个有值
//					alertmsg("商业险起止日期应填写完整！");
//					return;
//				}
//			}
//		}
		//交强险数据校验
//		if(isHasstrIns){
//			var CSDStr = $("#compulsorystartdate").val();
//			var CEDStr = $("#compulsoryenddate").val();
//			if(CSDStr && $.trim(CSDStr) && CEDStr && $.trim(CEDStr)){
//				//交强险起始日期应早于终止日期
//				var businessstartdate = new Date($.trim(CSDStr).replace(/-/g,"/"));
//				var businessenddate = new Date($.trim(CEDStr).replace(/-/g,"/"));
//				if(businessstartdate >= businessenddate){
//					alertmsg("交强险起保日期应早于终止日期！");
//					return;
//				}
//			}else{
//				if(!((!(CSDStr && $.trim(CSDStr))) && (!(CEDStr && $.trim(CEDStr))))){
//					//交强险起止日期不应只存在一个有值
//					alertmsg("交强险起止日期应填写完整！");
//					return;
//				}
//			}
//		}
		//指定驾驶人信息校验
		for (var i=0;i<3;i++){
			var name = $("#driversInfo_"+i+"_name").val();
			var birthday = $("#driversInfo_"+i+"_birthday").val();
			var gender = $("#driversInfo_"+i+"_gender").val();
			var licensetype = $("#driversInfo_"+i+"_licensetype").val();
			var licenseno = $("#driversInfo_"+i+"_licenseno").val();
			var licensedate = $("#driversInfo_"+i+"_licensedate").val();
			if(!(name && $.trim(name) && birthday && $.trim(birthday) && gender && $.trim(gender)
				 && licensetype && $.trim(licensetype) && licenseno && $.trim(licenseno) && licensedate && $.trim(licensedate))){
				if(!((!(name && $.trim(name))) && (!(birthday && $.trim(birthday))) && (!(gender && $.trim(gender)))
					 && (!(licensetype && $.trim(licensetype))) && (!(licenseno && $.trim(licenseno))) && (!(licensedate && $.trim(licensedate))))){
					//指定的驾驶人信息如果有就要填写完整
					alertmsg("请完善驾驶人"+(i+1)+"的信息！");
					return;
				}
			}
		}
		//指定驾驶人校验
		var pointspecifydriver = $.trim($("#pointspecifydriver").val());
		var specifydriver = $("#specifydriver").val();
		if(pointspecifydriver == "1"){
			if(!(specifydriver && $.trim(specifydriver))){
				//如果选择指定驾驶人，必须指定到具体驾驶人
				alertmsg("请选择要指定的驾驶人！");
				return;
			}else{
				//指定到具体的驾驶人必须信息完整
				var tempArray = $.trim(specifydriver).split("_"); 
				var specifydriverNum = parseInt(tempArray[0]);
				var specifydrivername = $("#driversInfo_"+(specifydriverNum-1)+"_name").val();
				if(!(specifydrivername && $.trim(specifydrivername))){
					alertmsg("指定的驾驶人信息不存在！");
					return;
				}
			}
		}
		//数据传递前表单置为可用
		if(pointspecifydriver == "0"){
			$(".related").removeAttr("disabled");
		}
		//防止重复提交
		$(this).prop("disabled", true);
		//发送后台处理
		$.ajax({
			url : 'common/insurancepolicyinfo/editOtherInfo',
			type : 'POST',
			data :$("#otherInfoEditForm").serialize(),
			cache : false,
			dataType : "json",
			async : true,
			error : function() {
				//数据传递完成还原表单禁用状态
				if(pointspecifydriver == "0"){
					$(".related").attr("disabled","true");
				}
				alertmsg("Connection error");
				//防止重复提交
				$("#makesure").prop("disabled", false);
			},
			success : function(data) {
				//数据传递完成还原表单禁用状态
				if(pointspecifydriver == "0"){
					$(".related").attr("disabled","true");
				}
				if(data.status == "success") {
//					alertmsg("修改其他信息成功！");
					//更新原页面指定选项卡内信息
					dataBack($("#inscomcode").val());
					$('#xDialog').modal('hide');
				}else if(data.status == "fail"){
					alertmsg(data.msg);
					//防止重复提交
					$("#makesure").prop("disabled", false);
				}
			}
		});
	});
	
	//数据回写方法
	function dataBack(inscomcode){
		var win;
		if($("#desktop:not(:hidden)").length==0){
			win = $(window.frames[$("iframe[id^=fra_orderlistorderlist]:not(:hidden)").attr("id")].document);
		}else{
			win = $(window.desktop_content.document);
		}
		if($("#pointspecifydriver").val()=="0"){
			win.find("#noSpecifydriverPageInfo"+inscomcode).text("否");//指定驾驶人否
		}else if($("#pointspecifydriver").val()=="1"){
			win.find("#noSpecifydriverPageInfo"+inscomcode).text("是");//指定驾驶人是
		}
		if(isHasbusIns){
			win.find("#businessstartdatePageInfo"+inscomcode).text($("#businessstartdate").val().substring(0,11));//商业险起保日期
			win.find("#businessenddatePageInfo"+inscomcode).text($("#businessenddate").val().substring(0,11));//商业险终止日期
		}
		if(isHasstrIns){
			win.find("#compulsorystartdatePageInfo"+inscomcode).text($("#compulsorystartdate").val().substring(0,11));//交强险起保日期
			win.find("#compulsoryenddatePageInfo"+inscomcode).text($("#compulsoryenddate").val().substring(0,11));//交强险终止日期
		}
//		win.find("#preinsShortnamePageInfo"+inscomcode).text($("#preinsname").val());//上年商业险投保公司
//		var deptName = $("#deptCode").val();
//		win.find("#deptComNamePageInfo"+inscomcode).text($("#dept"+deptName).text());//出单网点
	}
	
	//点击弹出供应商选择页面
	var setting = {
		async : {
			enable : true,
			url : "/cm/provider/queryprotree",
			autoParam : [ "id" ],//每次重新请求时传回的参数
			dataType : "json",
			type : "post"
		},
		check : {
			enable : true,
			chkStyle : "radio",
			radioType : "all"
		},
		callback : {
			onClick : zTreeOnCheck,//回调函数
			onCheck : zTreeOnCheck//回调函数
		}
	};
	//选择后回调函数
	function zTreeOnCheck(event, treeId, treeNode) {
		$("#preinscode").val(treeNode.prvcode);
		$("#preinsname").val(treeNode.name);
		$('#showpic').modal("hide");
	}
	//点击弹出供应商选择页面
	$("#preinsname").on("click", function(e) {
		$('#showpic').modal();
		$.fn.zTree.init($("#treeDemo"), setting);
	});
	$(".closeshowpic").on("click", function(e) {
		$('#showpic').modal("hide");
	})
	
}
