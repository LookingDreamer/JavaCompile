require(["jquery", "jqform", "jqgrid", "jqgridi18n", "jqvalidate", "jqmetadata", "jqvalidatei18n", "additionalmethods","public"], function ($) {

	$(function() {
		
		 $('body').on("click", "#updatetaskpoolbutton", function(e) {//$("#updatetaskpoolbutton").on("click", function(e) {
			if(!$("#taskpool").val()){
				$("#taskpool").val(20);
			}
			$('#updatetaskpoolform').ajaxSubmit({
				url : 'user/changetaskpool',
				type : 'POST',
				dataType : "json",
				error : function() {
					alertmsg("Connection error");
				},
				success : function(data) {
					var result = data.msg;
					if (result=='true') {
						alertmsg("修改成功！");
						$('#xDialog').modal('hide');
					}else{
						alertmsg("任务数没有修改！");
					}
				}
			});
		});
	});

});

