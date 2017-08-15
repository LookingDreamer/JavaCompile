function initEditOtherInfoScript() {
		//时间控件初始化设置
        $(".form_datetime").datetimepicker({
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
		});
		
    	function dataBackIDCard(taskid){
    		var win = null;
    		if($("#desktop:not(:hidden)").length>0){
    			win = $(window.desktop_content.document);
    		}else{
    			win = $(window.frames[$("iframe[id^=fra_businessmytaskqueryTask]:not(:hidden)").attr("id")].document);
    		}
    		win.find("#insd-name" + taskid).text($("#bbrname").val());
    		win.find("#insd-idcardno" + taskid).text($("#bbridcardno").val());
    		win.find("#insd-sex" + taskid).text($('input:radio[name="sex"]:checked').val() == '1'?'女' : '男');
    		win.find("#insd-nation" + taskid).text($("#bbrnation").val());
    		win.find("#insd-birthday" + taskid).text($("#bbrbirthday").val());
    		win.find("#insd-address" + taskid).text($("#bbraddress").val());
    		win.find("#insd-expdate" + taskid).text($("#bbrexpdate").val());
    		win.find("#insd-authority" + taskid).text($("#bbrauthority").val());
    		win.find("#insd-cellphone" + taskid).text($("#bbrcellphone").val());
			win.find("#insd-expstartdate" + taskid).text($("#bbrexpstartdate").val());
			win.find("#insd-expenddate" + taskid).text($("#bbrexpenddate").val());
			win.find("#insd-email" + taskid).text($("#bbremail").val());
    	}
		//电话正则表达式
		var phoneRegex = /^1\d{10}$/;
		//邮编正则表达式
		var emailRegex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
		//身份证
		var shenfenzhengRegex15 = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
		var shenfenzhengRegex18 = /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
    	$("#submitidcardinfo").on("click", function (){
    		var name = $.trim($("#bbrname").val());
    		var idcardno = $.trim($("#bbridcardno").val());
    		var sex = $.trim($('input:radio[name="sex"]:checked').val());
    		var nation = $.trim($("#bbrnation").val());
    		var birthday = $.trim($("#bbrbirthday").val());
    		var address = $.trim($("#bbraddress").val());
    		var authority = $.trim($("#bbrauthority").val());
    		var expdate = $.trim($("#bbrexpdate").val());
			var cellphone = $.trim($("#bbrcellphone").val());
    		var expstartdate = $.trim($("#bbrexpstartdate").val());
			var expenddate = $.trim($("#bbrexpenddate").val());
			var email = $.trim($("#bbremail").val());

    		if(idcardno.length==15){
				if(!shenfenzhengRegex15.test(idcardno)){
					alertmsg("身份证号格式不正确！");
					return false;
				}
			}
			if(!shenfenzhengRegex18.test(idcardno) && idcardno){
					alertmsg("身份证号格式不正确！");
					return false;
			}
			if(!phoneRegex.test(cellphone) && cellphone){
				alertmsg("请输入格式正确的手机号!");
				return;
			}
			if (!emailRegex.test(email) && email){
				alertmsg("请输入格式正确的邮箱号!");
				return;
			}

			if(expstartdate && expenddate){
				var s1 = new Date(expstartdate.replace(/\-/g, "\/"));
				var e1 = new Date(expenddate.replace(/\-/g, "\/"));
				if(s1 >=e1)
				{
					alertmsg("有效期限开始时间不得大于等于有效期限结束时间！");
					return;
				}
			}

    		//防止重复提交
    		$(this).prop("disabled", true);
    		$.ajax({
    			url : 'business/ordermanage/saveidcardinfo',
    			type : 'POST',
    			data : {
    				"name":name,
    				"idcardno":idcardno,
    				"sex":sex,
    				"nation":nation,
    				"birthday":birthday,
    				"address":address,
    				"authority":authority,
    				"expdate":expdate,
    				"cellphone":cellphone,
    				"inscomcode":$("#inscomcode").val(),
    				"taskid":$("#taskid").val(),
					"expstartdate":expstartdate,
					"expenddate":expenddate,
					"email":email
    			},
    			dataType : "json",
    			cache : false,
    			async : true,
    			error : function() {
    				alertmsg("Connection error");
    				//防止重复提交
    				$("#submitidcardinfo").prop("disabled", false);
    			},
    			success : function(data) {
    				if(data){
    					if (data.status == "success") {
    						alertmsg("修改成功");
    						dataBackIDCard($("#taskid").val());
    						$("#xDialog").modal("hide");
    					}else if(data.status == "fail"){
    						alertmsg("被保人身份证信息修改失败！");
    						//防止重复提交
    						$("#submitidcardinfo").prop("disabled", false);
    					}
    				}else{
    					alertmsg("被保人身份证信息修改失败！");
    					//防止重复提交
    					$("#submitidcardinfo").prop("disabled", false);
    				}
    			}
    		});
    	})

		$(".form_datetime").datetimepicker().on('changeDate', function(ev){
		var expstartdate = $.trim($("#bbrexpstartdate").val());
		var expenddate = $.trim($("#bbrexpenddate").val());
		if(expstartdate && expenddate){
			var newexpstartdate = expstartdate.replace(/-/g,".");
			var newexpenddate = expenddate.replace(/-/g,".");
			$("#bbrexpdate").text(newexpstartdate+'-'+newexpenddate);
			document.getElementById('bbrexpdate').value =newexpstartdate+'-'+newexpenddate;
		}

	});

}