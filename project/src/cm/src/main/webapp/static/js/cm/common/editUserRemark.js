function initEditUserRemarkScript(){
	//数据回写方法
	function dataBack(num){
		var win;
		var instanceId = $("#instanceId").val();
		var mytask = "iframe[id^=fra_businessmytaskqueryTask]:not(:hidden)";
		var ordermanagelist = "iframe[id^=fra_businessordermanageordermanagelist]:not(:hidden)"
		if($("#desktop:not(:hidden)").length>0){
			win = $(window.desktop_content.document);
//			if($("#desktop_content:not(:hidden)").length>0){
//				win = $(window.desktop_content.document);
//			}else if($("#desktop_tasks:not(:hidden)").length>0){
//				win = $(window.desktop_tasks.document);
//			}
		}else{
			if($(mytask).length>0){
				if($(ordermanagelist).length>0){
					if($(window.frames[$(mytask).attr("id")].document).find("#taskid0").val()==instanceId){
						win = $(window.frames[$(mytask).attr("id")].document);
					}else if($(window.frames[$(ordermanagelist).attr("id")].document).find("#taskid0").val()==instanceId){
						win = $(window.frames[$(ordermanagelist).attr("id")].document);
					}
				}else{
					win = $(window.frames[$(mytask).attr("id")].document);
				}
			}else{
				win = $(window.frames[$(ordermanagelist).attr("id")].document);
			}
		}
		var date = new Date();
		win.find("#userRemarkPageInfo"+num).text($("#commentcontent").val()+"("+date.getFullYear()+"-"+(date.getMonth() + 1)+"-"+date.getDate()
				+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+")");
		win.find("#userRemarkEdit"+num).val("1");
	}
	//提交修改用户备注信息
	$("#makesure").on("click",function(){
		//数据校验
		 if(!$("#commentcontent").val()){
		 	alertmsg("请填写备注内容！");
		 	return;
		 }
		//if(!$("#commenttype").val()){
		//	alertmsg("请选择备注类型！");
		//	return;
		//}
		//if(!$("#commentcontenttype").val()){
		//	alertmsg("请选择备注内容类型！");
		//	return;
		//}


		var selectedItems = "";
		$("select[id=js_multiselect_to_1] option").each(function(){
			selectedItems += ("&codetypes=" + $(this).val());
		});

		//防止重复提交
		$(this).prop("disabled", true);
		//异步提交
		$.ajax({
			url : 'common/remarksinfo/editusercomment',
			type : 'POST',
			data : $("#editUserRemarkForm").serialize() + selectedItems,
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$("#makesure").prop("disabled", false);
			},
			success : function(data) {
				if (data == "success") {
//					alertmsg("添加用户备注成功！");
					//更新原页面信息
					dataBack($("#num").val());
					$('#xDialog').modal('hide');
				}else{
					alertmsg("添加失败！请稍后重试！");
					//防止重复提交
					$("#makesure").prop("disabled", false);
				}
			}
		});
	});
	//备注类型级联
	$("#commenttype").on("change",function(){
		var commenttypeValue = $("#commenttype").val();
		if(!commenttypeValue){
			 $("#commentcontenttype").empty().append("<option value=''>请选择备注内容类型</option>");
			 return;
		}
		$.ajax({
			url : 'common/remarksinfo/changecommentcontenttype',
			type : 'GET',
			dataType : "json",
			data : {
				commenttypeValue:$.trim(commenttypeValue)
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				$("#commentcontenttype").empty().append("<option value=''>请选择备注内容类型</option>");
				$("#commentcontenttypeModelList").empty();
				for (var i = 0; i < data.length; i++) {
					$("#commentcontenttype").append("<option value='"+data[i].codevalue+"'>"+data[i].codename+"</option>");   
					$("#commentcontenttypeModelList").append("<span id='CCTV_"+data[i].codevalue+"'>"+data[i].prop2+"</span>");   
				}
			}
		});
	});
	//备注内容类型填充
	$("#commentcontenttype").on("change",function(){
		var commentcontenttype = $("#commentcontenttype").val();
		if(commentcontenttype){
			$("#commentcontent").val($("#commentcontenttypeModelList").find("span#CCTV_"+$.trim(commentcontenttype)).html());
		}
	});

}
