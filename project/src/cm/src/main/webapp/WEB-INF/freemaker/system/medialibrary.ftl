<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8"/>
<title>文件上传</title>
<link href="${staticRoot}/css/appmain.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
</head>
<body>
<div class="alert alert-info" style="display: none" id="alert-info">
  <strong id="infotitle"></strong><p id="info"></p>
</div>
<#if '${filetype}' ='img'>
<div class="modal-header">
      <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
      <span class="modal-title" id="myModalLabel">上传图片:(支持jpg,gif,jpeg,png,bmp,tif,zip文件上传)</span>
</div>
<div class="container-fluid">
					<div class="row">
							<form id="imgform" method="POST" action="" enctype="multipart/form-data">
								<div id="inputfrom" class="input-group-sm">
									<table  class="table table-bordered">
										<tr>
										    <td data-field="id">图片浏览</td>
										    <td data-field="name">图片名称</td>
										    <td data-field="price">图片描述</td>
										</tr>
										<tr>
										    <td><input id="fileimg0" class="file form-control btn" name="file" type="file" ></td>
										    <td><input type="text" class="form-control m-left-5" id="fileimg0name" name="filename" placeholder=""></td>
										    <td><input type="text" class="form-control m-left-5" id="fileimg0describe" name="filedescribe" placeholder=""></td>
										</tr>
										<tr>
										    <td><input id="fileimg1" class="file form-control btn" name="file" type="file" disabled="disabled"></td>
										    <td><input type="text" class="form-control m-left-5" id="fileimg1name" name="filename" placeholder="" disabled="disabled"></td>
										    <td><input type="text" class="form-control m-left-5" id="fileimg1describe" name="filedescribe" placeholder="" disabled="disabled"></td>
										</tr>
										<tr>
										    <td><input id="fileimg2" class="file form-control btn" name="file" type="file"  disabled="disabled"></td>
										    <td><input type="text" class="form-control m-left-5" id="fileimg2name" name="filename" placeholder="" disabled="disabled"></td>
										    <td><input type="text" class="form-control m-left-5" id="fileimg2describe" name="filedescribe" placeholder="" disabled="disabled"></td>
										</tr>
										<tr>
										    <td><input id="fileimg3" class="file form-control btn" name="file" type="file"  disabled="disabled"></td>
										    <td><input type="text" class="form-control m-left-5" id="fileimg3name" name="filename" placeholder="" disabled="disabled"></td>
										    <td><input type="text" class="form-control m-left-5" id="fileimg3describe" name="filedescribe" placeholder="" disabled="disabled"></td>
										</tr>
										<tr>
										    <td><input id="fileimg4" class="file form-control btn" name="file" type="file"  disabled="disabled"></td>
										    <td><input type="text" class="form-control m-left-5" id="fileimg4name" name="filename" placeholder="" disabled="disabled"></td>
										    <td><input type="text" class="form-control m-left-5" id="fileimg4describe" name="filedescribe" placeholder="" disabled="disabled"></td>
										</tr>
									</table>
								</div>
							</form>
						<div class="modal-footer">
							<a id="saveimgs" class="btn btn-success">保存</a>
							<a id="cancle" class="btn btn-danger" data-dismiss="modal">取消</a>
						</div>
			</div>
	</div>
<#elseif '${filetype}' ='video'>
<div class="modal-header">
      <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
      <span class="modal-title" id="myModalLabel">上传视频:(支持webm,mp4,ogg文件上传)</span>
</div>
<div class="container-fluid">
					<div class="row">
							<form id="videoform" method="POST" action="" enctype="multipart/form-data">
								<div id="inputfrom" class="input-group-sm">
									<table  class="table table-bordered">
										<tr>
										    <td data-field="id">视频浏览</td>
										    <td data-field="name">视频名称</td>
										    <td data-field="price">视频描述</td>
										</tr>
										<tr>
										    <td><input id="filevideo0" class="file form-control btn" name="file" type="file" ></td>
										    <td><input type="text" class="form-control m-left-5" id="filevideo0name" name="filename" placeholder=""></td>
										    <td><input type="text" class="form-control m-left-5" id="filevideo0describe" name="filedescribe" placeholder=""></td>
										</tr>
										<tr>
										    <td><input id="filevideo1" class="file form-control btn" name="file" type="file" ></td>
										    <td><input type="text" class="form-control m-left-5" id="filevideo1name" name="filename" placeholder=""></td>
										    <td><input type="text" class="form-control m-left-5" id="filevideo1describe" name="filedescribe" placeholder=""></td>
										</tr>
										<tr>
										    <td><input id="filevideo2" class="file form-control btn" name="file" type="file" ></td>
										    <td><input type="text" class="form-control m-left-5" id="filevideo2name" name="filename" placeholder=""></td>
										    <td><input type="text" class="form-control m-left-5" id="filevideo2describe" name="filedescribe" placeholder=""></td>
										</tr>
										<tr>
										    <td><input id="filevideo3" class="file form-control btn" name="file" type="file" ></td>
										    <td><input type="text" class="form-control m-left-5" id="filevideo3name" name="filename" placeholder=""></td>
										    <td><input type="text" class="form-control m-left-5" id="filevideo3describe" name="filedescribe" placeholder=""></td>
										</tr>
								
									</table>
								</div>
							</form>
						<div class="modal-footer">
							<a id="savevideos" class="btn btn-success">保存</a>
							<a id="cancle" class="btn btn-danger" data-dismiss="modal">取消</a>
						</div>
			</div>
	</div>
<#elseif '${filetype}' ='audio'>
<div class="modal-header">
      <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
      <span class="modal-title" id="myModalLabel">上传音频:(目前只支持mp3文件上传)</span>
</div>
<div class="container-fluid">
					<div class="row">
							<form id="audioform" method="POST" action="" enctype="multipart/form-data">
								<div id="inputfrom" class="input-group-sm">
									<table  class="table table-bordered">
										<tr>
										    <td data-field="id">音频浏览</td>
										    <td data-field="name">音频名称</td>
										    <td data-field="price">音频描述</td>
										</tr>
										<tr>
										    <td><input id="fileaudio0" class="file form-control btn" name="file" type="file" ></td>
										    <td><input type="text" class="form-control m-left-5" id="fileaudio0name" name="filename" placeholder=""></td>
										    <td><input type="text" class="form-control m-left-5" id="fileaudio0describe" name="filedescribe" placeholder=""></td>
										</tr>
										<tr>
										    <td><input id="fileaudio1" class="file form-control btn" name="file" type="file" ></td>
										    <td><input type="text" class="form-control m-left-5" id="fileaudio1name" name="filename" placeholder="" ></td>
										    <td><input type="text" class="form-control m-left-5" id="fileaudio1describe" name="filedescribe" placeholder="" ></td>
										</tr>
										<tr>
										    <td><input id="fileaudio2" class="file form-control btn" name="file" type="file" ></td>
										    <td><input type="text" class="form-control m-left-5" id="fileaudio2name" name="filename" placeholder="" ></td>
										    <td><input type="text" class="form-control m-left-5" id="fileaudio2describe" name="filedescribe" placeholder="" ></td>
										</tr>
										<tr>
										    <td><input id="fileaudio3" class="file form-control btn" name="file" type="file"></td>
										    <td><input type="text" class="form-control m-left-5" id="fileaudio3name" name="filename" placeholder="" ></td>
										    <td><input type="text" class="form-control m-left-5" id="fileaudio3describe" name="filedescribe" placeholder="" ></td>
										</tr>
										
										
										
									</table>
								</div>
							</form>
						<div class="modal-footer">
							<a id="saveaudios" class="btn btn-success">保存</a>
							<a id="cancle" class="btn btn-danger" data-dismiss="modal">取消</a>
						</div>
			</div>
	</div>
<#else>
<div class="modal-header">
      <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
      <span class="modal-title" id="myModalLabel">上传附件:(允许大小 20.0M,支持格式 doc,docx,xls,xlsx,ppt,pptx,pdf,swf,rar,zip,txt,xml,html,htm,css,js,db,dat文件上传)</span>
</div>
<div class="container-fluid">
					<div class="row">
							<form id="otherform" method="POST" action="" enctype="multipart/form-data">
								<div id="inputfrom" class="input-group-sm">
									<table  class="table table-bordered">
										<tr>
										    <td data-field="id">附件浏览</td>
										    <td data-field="name">附件名称</td>
										    <td data-field="price">附件描述</td>
										</tr>
										<tr>
										    <td><input id="fileother0" class="file form-control btn" name="file" type="file" ></td>
										    <td><input type="text" class="form-control m-left-5" id="fileother0name" name="filename" placeholder=""></td>
										    <td><input type="text" class="form-control m-left-5" id="fileother0describe" name="filedescribe" placeholder=""></td>
										</tr>
										<tr>
										    <td><input id="fileother1" class="file form-control btn" name="file" type="file" ></td>
										    <td><input type="text" class="form-control m-left-5" id="fileother1name" name="filename" placeholder=""></td>
										    <td><input type="text" class="form-control m-left-5" id="fileother1describe" name="filedescribe" placeholder=""></td>
										</tr>
										<tr>
										    <td><input id="fileother2" class="file form-control btn" name="file" type="file" ></td>
										    <td><input type="text" class="form-control m-left-5" id="fileother2name" name="filename" placeholder=""></td>
										    <td><input type="text" class="form-control m-left-5" id="fileother2describe" name="filedescribe" placeholder=""></td>
										</tr>
										<tr>
										    <td><input id="fileother3" class="file form-control btn" name="file" type="file" ></td>
										    <td><input type="text" class="form-control m-left-5" id="fileother3name" name="filename" placeholder=""></td>
										    <td><input type="text" class="form-control m-left-5" id="fileother3describe" name="filedescribe" placeholder=""></td>
										</tr>
										<tr>
										    <td><input id="fileother4" class="file form-control btn" name="file" type="file" ></td>
										    <td><input type="text" class="form-control m-left-5" id="fileother4name" name="filename" placeholder=""></td>
										    <td><input type="text" class="form-control m-left-5" id="fileother4describe" name="filedescribe" placeholder=""></td>
										</tr>
									</table>
								</div>
							</form>
						<div class="modal-footer">
							<a id="saveothers" class="btn btn-success">保存</a>
							<a id="cancle" class="btn btn-danger" data-dismiss="modal">取消</a>
						</div>
			</div>
	</div>
</#if>
<input type="hidden" id="imgclassifytype" name="" value="${imgclassifytype!'0'}">
<input type="hidden" id="videoclassifytype" name="" value="${videoclassifytype!'0'}">
<input type="hidden" id="audioclassifytype" name="" value="${audioclassifytype!'0'}">
<input type="hidden" id="otherclassifytype" name="" value="${otherclassifytype!'0'}">

</body>
<script type="text/javascript">
		requirejs([ "system/medialibrary"]);
</script>
<script type="text/javascript">
$(document).ready(function(){
	//校验
	function checkNull(filetype){
		var result = false;
		for(var i=4;i>=0;i--){
			if($("#file"+filetype+i).val()){
				if(!($("#file"+filetype+i+"name").val())){
		    		$("#file"+filetype+i+"name").css({"border":"2px solid #F70A37"});
		    		$("#file"+filetype+i+"name").attr('placeholder','请输入文件名称');
		    		return result;
				}
				for(var j=0;j<i;j++){
					if(!$("#file"+filetype+j).val()){
						$("#alert-info").show();
			        	$("#infotitle").html("顺序填写！");
			        	$("#info").html("请按顺序填写上传！");
						$("#file"+filetype+j).css({"border":"2px solid #F70A37"});
						$("#file"+filetype+j+"name").css({"border":"2px solid #F70A37"});
						$("#file"+filetype+j+"describe").css({"border":"2px solid #F70A37"});
						return result;
					}
				}
			}else{
				
			}
		}
		$("input[id^='file" + filetype + "']").each(function(){
			if($(this).val()!=''){
				result = true;
			}
		});
		if(!result){
			$("#alert-info").show();
	    	$("#infotitle").html("未选择文件！");
    		$("#info").html("请选择要上传的文件！");
		}
		return result;
	}
	//
	$("input[name='filename']").blur(function(){
    	if($(this).val()){
    		$(this).css({"border":"2px solid #bdc3c7"});
    	}
    });
	//自动填充文件名称和描述
	$("input[name='file']").each(function(){
	    $(this).change(function(){
	    	str=$(this).val();
		var path1 = str.lastIndexOf("\\");
            var tempname = str.substring(path1+1);
            var path2 = tempname.lastIndexOf(".");
            var name = tempname.substring(0,path2);
            $("#" + $(this).attr("id") +"name").val(name);
            $("#" + $(this).attr("id") +"describe").val(name);
            var tempid = $(this).attr("id");
            var nowindex = tempid.replace("fileimg","");
            if(nowindex<5){
	            $("#fileimg" + (parseInt(nowindex)+1)).attr("disabled",false);
	            $("#fileimg" + (parseInt(nowindex)+1) + "name").attr("disabled",false);
	            $("#fileimg" + (parseInt(nowindex)+1) + "describe").attr("disabled",false);
            }
            if($(this).val()){
            	$(this).css({"border":"2px solid #bdc3c7"});
            	$("#" + $(this).attr("id") +"name").css({"border":"2px solid #bdc3c7"});
            	$("#" + $(this).attr("id") +"describe").css({"border":"2px solid #bdc3c7"});
            }
		});
	});
	//校验文件
	$("input[type='file']").each(function(){
		$(this).change(function(){
			str=$(this).val();
			var path1 = str.lastIndexOf("\\");
	        var tempname = str.substring(path1+1);
	        var path2 = tempname.lastIndexOf(".");
	        var type = tempname.substring(path2+1);
	        if($(this).attr("id").indexOf('img') > 0 &&type!='' && type!='jpg'&&type!='gif'&&type!='jpeg'&&type!='png'&&type!='bmp'&&type!='tif'){
	        	$("#alert-info").show();
	        	$("#infotitle").html("文件类型错误！");
	        	$("#info").html("仅支持jpg,gif,jpeg,png,bmp,tif文件上传");
	        	$(this).val("");
	        	$("#"+$(this).attr("id")+"name").val("");
	            $("#"+$(this).attr("id")+"describe").val("");
	        }else if ($(this).attr("id").indexOf('video') > 0 &&type!='' &&type!='webm'&&type!='ogg'&&type!='mp4'){
	        	$("#alert-info").show();
	        	$("#infotitle").html("文件类型错误！")
	        	$("#info").html("仅支持webm,mp4,ogg文件上传");
	        	$(this).val("");
	        	$("#"+$(this).attr("id")+"name").val("");
	            $("#"+$(this).attr("id")+"describe").val("");
	        }else if ($(this).attr("id").indexOf('audio') > 0 &&type!='' && type!='mp3'){
	        	$("#alert-info").show();
	        	$("#infotitle").html("文件类型错误！")
	        	$("#info").html("目前只支持mp3文件上传");
	        	$(this).val("");
	        	$("#"+$(this).attr("id")+"name").val("");
	            $("#"+$(this).attr("id")+"describe").val("");
	        }else if ($(this).attr("id").indexOf('other') > 0 &&type!='' && type!='doc'&&type!='docx'&&type!='xls'&&type!='xlsx'&&type!='ppt'&&type!='pptx'&&type!='pdf'&&type!='swf'&&type!='rar'&&type!='zip'&&type!='txt'&&type!='xml'&&type!='html'&&type!='htm'&&type!='css'&&type!='js'&&type!='db'&&type!='dat'){
	        	$("#alert-info").show();
	        	$("#infotitle").html("文件类型错误！")
	        	$("#info").html("仅支持格式 doc,docx,xls,xlsx,ppt,pptx,pdf,swf,rar,zip,txt,xml,html,htm,css,js,db,dat文件上传");
	        	$(this).val("");
	        	$("#"+$(this).attr("id")+"name").val("");
	            $("#"+$(this).attr("id")+"describe").val("");
	        }else{
	        	$("#alert-info").hide();
	        	$("#info").html("");
	        	$("#infotitle").html("")
	        }
		})
	});
	$("#saveimgs").on("click", function(e) {
		if(checkNull('img')){
			$("#imgform").ajaxSubmit({
				url:'file/uploadfiles?filetype=img&fileclassifytype=' + $("#imgclassifytype").val(),
				type:'POST',
				dataType:'json',
				clearForm: true,
				error: function() {
					$("#alert-info").show();
		        	$("#infotitle").html("上传失败！");
		        	$("#info").html("服务器错误，请联系管理员！");
		      	},
				success: function (data) {
					if(data.result=='filebatchuploadcomplete'){
						$("#alert-info").show();
			        	$("#infotitle").html("上传成功！");
			        	$("#info").html(data.count + "个图片上传成功！");
					}
			        $('#xDialog').modal('hide');
					window.fra_fileindex.initDatas("img");
				}
			});
		}
	});
	$("#savevideos").on("click", function(e) {
		if(checkNull('video')){
			$("#videoform").ajaxSubmit({
				url:'file/uploadfiles?filetype=video&fileclassifytype=' + $("#videoclassifytype").val(),
				type:'POST',
				dataType:'json',
				clearForm: true,
				error: function() {
					$("#alert-info").show();
		        	$("#infotitle").html("上传失败！");
		        	$("#info").html("服务器错误，请联系管理员！");
		      	},
				success: function (data) {
					if(data.result=='filebatchuploadcomplete'){
						$("#alert-info").show();
			        	$("#infotitle").html("上传成功！");
			        	$("#info").html(data.count + "个视频上传成功！");
					}
					 $('#xDialog').modal('hide');
					window.fra_fileindex.initDatas("video");
					
				}
			});
		}
	});

	$("#saveaudios").on("click", function(e) {
		$('#xDialog').modal('hide');
		$.insLoading();
		if(checkNull('audio')){
			$("#audioform").ajaxSubmit({
				url:'file/uploadfiles?filetype=audio&fileclassifytype=' + $("#audioclassifytype").val(),
				type:'POST',
				dataType:'json',
				clearForm: true,
				error: function() {
					$("#alert-info").show();
		        	$("#infotitle").html("上传失败！");
		        	$("#info").html("服务器错误，请联系管理员！");
		      	},
				success: function (data) {
					$.insLoaded();
					if(data.result=='filebatchuploadcomplete'){
						$("#alert-info").show();
			        	$("#infotitle").html("上传成功！");
			        	$("#info").html(data.count + "个音频上传成功！");
					}
					window.fra_fileindex.initDatas("audio");
				}
			});
		}
	});

	$("#saveothers").on("click", function(e) {
		if(checkNull('other')){
			$("#otherform").ajaxSubmit({
				url:'file/uploadfiles?filetype=other&fileclassifytype=' + $("#otherclassifytype").val(),
				type:'POST',
				dataType:'json',
				clearForm: true,
				error: function() {
					$("#alert-info").show();
		        	$("#infotitle").html("上传失败！");
		        	$("#info").html("服务器错误，请联系管理员！");
		      	},
				success: function (data) {
					if(data.result=='filebatchuploadcomplete'){
						$("#alert-info").show();
			        	$("#infotitle").html("上传成功！");
			        	$("#info").html(data.count + "个附件上传成功！");
					}
					$('#xDialog').modal('hide');
					window.fra_fileindex.initDatas("other");
				}
			});
		}
	});
});
</script>
</html>