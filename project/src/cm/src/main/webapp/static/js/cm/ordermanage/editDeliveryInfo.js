function initEditDeliveryInfoScript() {
	function dataBack(){
		/*var win = null;
		if($("#desktop:not(:hidden)").length>0){
			win = $(window.desktop_content.document);
		}else{
			win = $(window.frames[$("iframe[id^=fra_businessmytaskqueryTask]:not(:hidden)").attr("id")].document);
		}*/
		var win;
		var instanceId = $("#instanceId").val();
		var mytask = "iframe[id^=fra_businessmytaskqueryTask]:not(:hidden)";
		var ordermanagelist = "iframe[id^=fra_businessordermanageordermanagelist]:not(:hidden)";
		if ($("#desktop:not(:hidden)").length > 0) {
			win = $(window.desktop_content.document);
		} else {
			if ($(mytask).length > 0) {
				if ($(ordermanagelist).length > 0) {
					if ($(window.frames[$(mytask).attr("id")].document).find("#taskid0").val() == instanceId) {
						win = $(window.frames[$(mytask).attr("id")].document);
					} else if ($(window.frames[$(ordermanagelist).attr("id")].document).find("#taskid0").val() == instanceId) {
						win = $(window.frames[$(ordermanagelist).attr("id")].document);
					}
				} else {
					win = $(window.frames[$(mytask).attr("id")].document);
				}
			} else {
				win = $(window.frames[$(ordermanagelist).attr("id")].document);
			}
		}
		win.find("#recipientname").text($("#erecipientname").val());
		win.find("#recipientmobilephone").text($("#erecipientmobilephone").val());
		win.find("#recipientaddress").text($("#province").children('option:selected').text()
				+$("#city").children('option:selected').text()
				+$("#area").children('option:selected').text()
				+$("#erecipientaddress").val());
		win.find("#zip").text($("#ezip").val());
	}
	 
	//手机号正则表达式
	//var phoneRegex = /^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\d{8}$/;
	var phoneRegex = /^1\d{10}$/;
	//邮编正则表达式
	var zipRegex = /^[0-9]\d{5}(?!\d)$/;
	
	$("#makesure").on("click", function (e){
//		var orderfrom = $.trim($("#orderfrom").html());
		var recipientname = $.trim($("#erecipientname").val());
		var recipientmobilephone = $.trim($("#erecipientmobilephone").val());
		var recipientaddress = $.trim($("#erecipientaddress").val());
		var zip = $.trim($("#ezip").val());
		var inscomcode = $.trim($("#inscomcode").val());
		var taskid = $.trim($("#taskid").val());
		var province = $("#province").children('option:selected').val();
		var city = $("#city").children('option:selected').val();
		var area = $("#area").children('option:selected').val();
		if(!recipientname){
			alertmsg("收件人姓名不能为空！");
			return;
		}
		if(!recipientmobilephone){
			alertmsg("收件人手机号不能为空！");
			return;
		}else{
			if(!phoneRegex.test(recipientmobilephone)){
				alertmsg("收件人手机号格式不正确！");
				return;
			}
		}
		if(!zip){
			alertmsg("邮政编码不能为空！");
			return;
		}else{
			if(!zipRegex.test(zip)){
				alertmsg("邮政编码格式不正确！");
				return;
			}
		}
		if((!province)||(!city)||(!area)){
			alertmsg("请选择省市区！");
			return;
		}
		if(!recipientaddress){
			alertmsg("收件人详细地址不能为空！");
			return;
		}
//		alert(zip+"zip,"+"recipientname"+recipientname+recipientaddress+"recipientaddress"+taskid+"taskid");
		//防止重复提交 
		$(this).prop("disabled", true);
		$.ajax({
			url : 'business/ordermanage/editDeliveryInfo',
			type : 'post',
			data : {
				"recipientname":recipientname,
				"recipientmobilephone":recipientmobilephone,
				"recipientaddress":recipientaddress,
				"zip":zip,
				"inscomcode":inscomcode,
				"taskid":taskid,
				"province":province,
				"city":city,
				"area":area
			},
			dataType : "text",
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$("#makesure").prop("disabled", false);
			},
			success : function(data) {
				if(data){
					if (data == "success") {
						alertmsg("保存成功");
						dataBack();
						$('#xDialog').modal('hide');
					}else if(data == "fail"){
						alertmsg("保存失败");
						//防止重复提交
						$("#makesure").prop("disabled", false);
					}
				}
			}
		});
	});
	
	$("#province").change(function(){
		var selected = $(this).children('option:selected').val();
		$.ajax({
			url : "business/ordermanage/getRegionInfo",
			type : 'GET',
			dataType : "json",
			data : {
				parentid:selected
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				$("#city").empty();
				$("#city").append("<option value=''>请选择</option>");
				for (var i = 0; i < data.length; i++) {
					$("#city").append("<option value='"+data[i].comcode+"'>"+data[i].comcodename+"</option>");
				}
				if(data.length>0){
					$("#city").get(0).selectedIndex=0;
				}
				$("#city").trigger("change"); 
			}
		});
		
	});
	
	$("#city").change(function(){
		var selected = $(this).children('option:selected').val();
		$.ajax({
			url : "business/ordermanage/getRegionInfo",
			type : 'GET',
			dataType : "json",
			data : {
				parentid:selected
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				$("#area").empty();
				$("#area").append("<option value=''>请选择</option>");
				for (var i = 0; i < data.length; i++) {
					$("#area").append("<option value='"+data[i].comcode+"'>"+data[i].comcodename+"</option>");   
				}
				if(data.length>0){
					$("#area").get(0).selectedIndex=0;
				}
				$("#area").trigger("change"); 
			}
		});
		
	});
}
