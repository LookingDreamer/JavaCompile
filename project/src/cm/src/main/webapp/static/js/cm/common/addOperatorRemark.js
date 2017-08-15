function initAddOperatorRemarkScript() {
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
		var dateStr = date.getFullYear()+"-"+(date.getMonth() + 1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
		win.find("#operatorRemarkPageInfo"+num).prepend("<span style='color:#FF6633;' class='glyphicon glyphicon-triangle-right'></span>"
			+$("#commentcontent").val()+"("+$("#loginName").text()+dateStr+")"+"<br/>");
	}
	//操作员备注信息提交添加
	$("#makesure").click(function(){
		//数据校验
		if(!$("#commentcontent").val()){
			alertmsg("请填写备注内容！");
			return;
		}else if($("#commentcontent").val().length>500){
			alertmsg("备注内容不可超过500字！");
			return;
		}
		//防止重复提交
		$(this).prop("disabled", true);
		//异步提交
		$.ajax({
			url : 'common/remarksinfo/addoperatorcomment',
			type : 'POST',
			data : $("#addOperatorRemarkForm").serialize(),
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$("#makesure").prop("disabled", false);
			},
			success : function(data) {
				if (data == "success") {
//					alertmsg("添加操作员备注成功！");
					//更新原页面指定选项卡内信息
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
	
}
