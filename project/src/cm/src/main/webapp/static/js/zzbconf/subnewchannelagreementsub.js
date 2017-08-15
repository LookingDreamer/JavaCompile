function initsubnewchannelagreementsub () {
	
	$("#checkProid").click(function(){
			var str="";
			for(var i=0;i<$('input[name="provider"]:checked').length;i++){
				 str+=$('input[name="provider"]:checked')[i].value+","
			}
			 var str=str.substring(0,str.lenght);
			 var channelid=$("#channelid").val()
			 if(str==""){
				 alert("请选择供应商");
				 return;
			 }
			$.ajax({
				url : "/cm/channel/UploadAndSaveProvider",
				type : 'POST',
				dataType : 'text',
				data :{
					"providerid":str
					},
				cache : false,
				async : true,
				success : function(data) {
					if (data == "success") {
						$('#xDialog').modal('hide');
						alertmsg("供应商应用范围保存成功！");
//						alert(parent.window.parent.checkedr());
						var providertable=window.parent.document.getElementById("deptListTable");
//						providertable.bootstrapTable('refresh',{url:"/cm/channel/getNewChannelProtocolInfo?id="+channelid});
					}else if (data == "fail"){
						alertmsg("供应商应用范围保存失败！");
					}
				}
			});
		})	
	$("#closeProid").click(function(){
		$('#xDialog').modal('hide');
	});	
	
	function reloadProvider(){
		$.ajax({
			url : "/cm/channel/getNewChannelProtocolInfo?id="+$("#channelid").val(),
			type : 'get',
			dataType : 'text',
			cache : false,
			async : true,
		});
	}
};
	 