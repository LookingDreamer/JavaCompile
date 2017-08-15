function initEditAddMessageScript(){
	//数据回写方法
	function dataBack(num){
		var win;
		if($("#desktop:not(:hidden)").length>0){
			win = $(window.desktop_content.document);
		}else{
			win = $(window.frames[$("iframe[id^=fra_]:not(:hidden)").attr("id")].document);
		}
		$("input.form-control").each(function(){
			if($(this).val()){
				win.find("#"+$(this).attr("id")+"DownPageInfo"+num).text($(this).val());
			}
		});
		$("select.form-control").each(function(){
			if($(this).val()){
				win.find("#"+$(this).attr("id")+"DownPageInfo"+num).text($("#"+$(this).attr("id")+"_"+$(this).val()).text());
			}
		});
	}
	//提交修改用户备注信息
	$("#makesure").on("click",function(){
		obj = document.getElementsByName("inscom");
		var inscomtex = '';
		for(k in obj){
			if(obj[k].checked){
				inscomtex += obj[k].value+',';
			}
		}
		if(inscomtex){
			
			if($("#initFailFlag").length>0){//加载失败
				$('#xDialog').modal('hide');
			}else{//加载成功
			//数据校验
//		$("input.isCheck").each(function(){
//			var cnName = $("#"+$(this).attr("id")+"CnName").text();
//			if($(this).val()){
//				if(isNaN($(this).val())){
//					alertmsg(cnName+"应为数字！");
//					return;
//				}
//			}else{
//				alertmsg(cnName+"不能为空！");
//				return;
//			}
//		});
			var count = $("#paramcount").val();
			var param={paramcount:count,inscoms:inscomtex,instanceId:$("#instanceId").val()};
			for(var i=0; i<count; i++){
				if($("#metadataValue"+i).val().indexOf($("#metadataValue"+i).attr("name"))>=0){
					param["metadataValue"+i]=$("#metadataValue"+i).val();
				}else{
					param["metadataValue"+i]=$("#metadataValue"+i).val()+"@"+$("#metadataValue"+i).attr("name");
				}
			}
			var shouldCheck = $("input.isCheck");
			for(var i=0; i<shouldCheck.size(); i++){
				var thisItem = $(shouldCheck[i]);
				var cnName = $("#"+thisItem.attr("id")+"CnName").text();
				if(thisItem.val()){
					if(isNaN(thisItem.val())){
						alertmsg(cnName+"应为数字！");
						return;
					}
				}else{
					alertmsg(cnName+"不能为空！");
					return;
				}
			}
			//防止重复提交
			$(this).prop("disabled", true);
			//异步提交
			$.ajax({
				url : '/cm/business/manualprice/editlocaldbreplenishinfo',
				type : 'POST',
				data : param,
				async : true,
				error : function() {
					alertmsg("Connection error");
					//防止重复提交
					$("#makesure").prop("disabled", false);
				},
				success : function(data) {
					if (data == "success") {
						//更新原页面信息
						//dataBack($("#num").val());
						if(window.fra_businessmytaskqueryTask){
							window.fra_businessmytaskqueryTask.location.reload();
						}else if(window.desktop_content){
							window.desktop_content.location.reload(); 
						}
						$('#xDialog').modal('hide');
					}else{
						alertmsg("修改失败！请稍后重试！");
						//防止重复提交
						$("#makesure").prop("disabled", false);
					}
				}
			});
		}
		}else{
			alertmsg("请勾选需要保存补充项的保险公司！");
		}
	});
	
}
