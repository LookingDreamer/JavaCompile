function initImageAddDialogScript() {

	//影像信息提交
	$("#makesure").on("click",function(){
		//获取文件用途类型和用途描述信息
		var strArr=$("#fileType").val().split(",");
		//数据校验
		if ($("#file").val().length <= 0) {
            alertmsg("请选择上传图片！");
            return;
        }
		//图片类型不重复校验
		//if (checkImgTypeExist(strArr[0])) {
		//	alertmsg("图片类型重复！");
		//}else{
			openWindow();
			$.ajaxFileUpload({
				url : "common/insurancepolicyinfo/addImage?fileType="+strArr[0]+"&fileDescribes="+strArr[1]+"&processinstanceid="+$("#subInstanceId").val()+"&taskid="+$("#taskid").val(),
				secureuri:false,//是否启用安全提交,默认为false
				fileElementId:"file",//提交文件的id值
				dataType : "json",
				error : function() {
					alertmsg("服务器连接失败！");
				},
				success : function(data) {
					if(data[0].status=="success"){
						dataBack(data);
						alertmsg("上传成功!",function(){
							$('#xDialog').modal('hide');
						});
					}else{
						alertmsg("影像信息提交失败！",function(){
							alertmsg(data.msg);
						});
					}
					closeWindow();
				}
			});
		//}
		//var img = new Image();
		//img.src = $("#file").val();
		//alertmsg($("#file").val());
		//alertmsg(img.fileSize);
		//if(img.fileSize>12*1024){
		//	alertmsg("图片不能大于5M,,,,,,,");
		//}
	});

}


function openWindow(){
	document.getElementById('light').style.display='block';
	document.getElementById('fade').style.display='block';
}
function closeWindow(){
	document.getElementById('light').style.display='none';
	document.getElementById('fade').style.display='none';
}

function checkImgTypeExist(fileType){
	var flag = false;
	$.ajax({
		url : "common/insurancepolicyinfo/checkImgTypeExist?fileType="+fileType+"&processinstanceid="+$("#subInstanceId").val()+"&taskid="+$("#taskid").val(),
		type : 'post',
		dataType : "html",
		async : false,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			if(data=="faild"){
				flag = true;
			}
		}
	});
	return flag;
}

function dataBack(datas){
	for(var i =0 ; i<datas.length; i++){
		var data =datas[i];
		var tempstr = "";
		//tempstr += "<div class=\"col-xs-6 col-sm-3 col-md-2 col-lg-2\"><div class=\"thumbnail\">";
		tempstr += "<div style=\"width: 100%; text-align:center; vertical-align:middle;line-height:100%; border: 0px solid #ccc\">";
		tempstr += "<input type=\"checkbox\" name=\"allImgPathId\" value=\""+data["filepath"]+"\" style=\"display: none\" checked/>";
		tempstr += "<a href=\'javascript:window.parent.openImgView(\"common/insurancepolicyinfo/previewPicture?pictureId="+data["fileid"]+"&subInstanceId="+data["subInstanceId"]+"&taskid="+data["taskid"]+"\")\'>";
		tempstr += "<img style=\"width: 120px; height: 120px; text-align:center; vertical-align:middle;line-height:120px; border: 1px solid #ccc\" src=\""+data["smallfilepath"]+"\"  width=\"130px\" height=\"130px\">";
		tempstr += "</a></div><div class=\"caption\" style=\"text-align:center;\"><p>";
		tempstr += "<a href=\'javascript:window.parent.openImgView(\"common/insurancepolicyinfo/previewPicture?pictureId="+data["fileid"]+"&subInstanceId="+data["subInstanceId"]+"&taskid="+data["taskid"]+"\")\'>";
		//tempstr += data["filedescribe"];
		tempstr += "</a></p></div>";
	//	var win = $(window.frames[2].document);
		var win = null;
		if($(window.top.document).find("#menu").css("display")=="none"){
			win = $(window.frames[1].document);
			try{
				console.log(tempstr)
				$(window.frames[2].document).find(".imglist").append(tempstr);
				console.log($(window.frames[2].document).find(".imglist"));
			}catch(e){
				console.log(e.message);
			}
		}else{
			win = $(window.frames[2].document);
			try{
				$(window.frames[1].document).find(".imglist").append(tempstr);
			}catch(e){
				console.log(e.message);
			}
		}
		win.find(".imglist").append(tempstr);
	}
}