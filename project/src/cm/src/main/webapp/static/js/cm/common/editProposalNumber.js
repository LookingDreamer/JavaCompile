function initEditProposalnoInfoScript(){
	function dataBack(){
		var win = null;
		if($("#desktop:not(:hidden)").length>0){
			win = $(window.desktop_content.document);
		}else{
			win = $(window.frames[$("iframe[id^=fra_businessmytaskqueryTask]:not(:hidden)").attr("id")].document);
		}
		win.find("#businessProposalFormNo").text($("#biproposalno").val());
		win.find("#strongProposalFormNo").text($("#ciproposalno").val());
	}
	$("#makesure").on("click", function (){
		var ciproval = $.trim($("#ciproposalno").val());
		var biproval = $.trim($("#biproposalno").val());
		if(ciproval || biproval){
//			if(ciproval == biproval){
//				alertmsg("商业险和交强险的投保单号不能相同！");
//				return;
//			}
		}else{
			alertmsg("请填写投保单号后再点击确定！");
			return;
		}
		//防止重复提交
		$(this).prop("disabled", true);
		$.ajax({
			url : 'business/ordermanage/editProposalNumber',
			type : 'get',
			data : {
				"ciproposalno":ciproval,
				"biproposalno":biproval,
				"inscomcode":$("#inscomcode").val(),
				"taskid":$("#taskid").val()
			},
			dataType : "json",
			cache : false,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$("#makesure").prop("disabled", false);
			},
			success : function(data) {
				if(data){
					if (data.status == "success") {
						dataBack();
						$('#xDialog').modal('hide');
					}else if(data.status == "fail"){
						alertmsg(data.msg);
						//防止重复提交
						$("#makesure").prop("disabled", false);
					}
				}else{
					alertmsg("投保单号修改失败！");
					//防止重复提交
					$("#makesure").prop("disabled", false);
				}
			}
		});
	})
}