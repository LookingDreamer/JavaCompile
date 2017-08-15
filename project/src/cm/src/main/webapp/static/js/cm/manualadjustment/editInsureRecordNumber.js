function initEditInsureRecordNumberScript() {
	//修改投保单号信息
	$("#makesure").on("click", function(e) {
		//if(!$.trim($('#businessPolicyNum').val())){
		//	alertmsg("商业险投保单号不能为空！");
		//	return;
		//}
		//if(!$.trim($('#strongPolicyNum').val())){
		//	alertmsg("交强险投保单号不能为空！");
		//	return;
		//}
		if(!$.trim($('#payNum').val())){
			alertmsg("支付号不能为空！");
			return;
		}
		if(!$.trim($('#checkCode').val())){
			alertmsg("校验码不能为空！");
			return;
		}
		//发送后台处理
		$.ajax({
			url : 'business/manualrecord/editInsureRecordNumber',
			type : 'POST',
			data :$("#editInsureRecordNumberForm").serialize(),
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data == "success") {
					alertmsg("修改其他信息成功！");
					$('#xDialog').modal('hide');
					//修改后刷新页面	
					if(window.fra_businessmanualrecordshowmanualrecord){
						window.fra_businessmanualrecordshowmanualrecord.reloadInfo();
					}
				}else{
					alertmsg("修改失败！请稍后重试！");
				}
			}
		});
	});
	
}
