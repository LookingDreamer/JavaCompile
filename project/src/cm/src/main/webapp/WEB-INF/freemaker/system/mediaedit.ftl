<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8"/>
<title>文件上传</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${staticRoot}/css/appmain.css" rel="stylesheet">
<link href="${staticRoot}/css/skin/default.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/bootstrap-table.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/fileinput.css" media="all" rel="stylesheet" type="text/css" />
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		require([ "system/fileupload" ], function ($) {
			initDatas("all");
		});
</script>
</head>
<body>
<div class="alert alert-info" style="display: none" id="alert-info">
  <strong id="infotitle"></strong><p id="info"></p>
</div>
<div class="modal-header">
      <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
      <span class="modal-title" id="myModalLabel">编辑图片:(支持jpg,gif,jpeg,png,bmp,tif,zip文件上传)</span>
</div>
<div class="container-fluid">
<input type="hidden" id="filetype" value="${entity.filetype}">
<input type="hidden" id="filecodevalue" value="${entity.filecodevalue}">
					<div class="row">
							<form id="imgform" method="POST" action="" enctype="multipart/form-data">
									<table  class="table">
										<#if '${entity.filetype}'=='img'>
										<tr>
											<td>
											</td>
											<td align="center">
												<div id="editimgdiv" style="width:250px;height:160px;text-align:center;vertical-align:middle;line-height:160px;border:0px solid #ccc;display:table-cell">
									   				<img id="imgid" title="${entity.filename}" src="${entity.filepath}" alt="${entity.id}" width="200px" height="160px">
												</div>
											</td>
											<td>
											</td>
										</tr>
										</#if>
										<tr>
											<td align="right">
												<label class="exampleInputCode">重新上传：</label>
											</td>
											<td>
												<input id="fileimg" class="file form-control btn" name="file" type="file" >
											</td>
											<td>
												<input id="uploadfileimg" class="btn btn-success" type="button" value="上传">
											</td>
										</tr>
										<tr>
											<td align="right">
												<label class="exampleInputCode">图片名称：</label>
											</td>
											<td>
												<input type="text" class="form-control m-left-5" id="filename" name="filename" placeholder="" value="${entity.filename}">
											</td>
											<td>
											</td>
										</tr>
										<tr>
											<td align="right">
												<#if '${entity.filetype}'=='img'>
													<label class="exampleInputCode">图片分类：</label>
												</#if>
												<#if '${entity.filetype}'=='audio'>
													<label class="exampleInputCode">音频分类：</label>
												</#if>
												<#if '${entity.filetype}'=='other'>
													<label class="exampleInputCode">附件分类：</label>
												</#if>
												<#if '${entity.filetype}'=='video'>
													<label class="exampleInputCode">视频分类：</label>
												</#if>
											</td>
											<td>
												<#if '${entity.filetype}'=='img'>
													<select name="imgclassify" class="form-control classify" id ="imgclassify">
													</select>
												</#if>
												<#if '${entity.filetype}'=='audio'>
													<select name="audioclassify" class="form-control classify" id ="audioclassify">
													</select>
												</#if>
												<#if '${entity.filetype}'=='other'>
													<select name="otherclassify" class="form-control classify" id ="otherclassify">
													</select>
												</#if>
												<#if '${entity.filetype}'=='video'>
													<select name="videoclassify" class="form-control classify" id ="videoclassify">
													</select>
												</#if>
											</td>
											<td>
											</td>
										</tr>
										<tr>
											<td align="right">
												<label class="exampleInputCode">图片描述：</label>
											</td>
											<td>
												<input type="text" class="form-control m-left-5" id="filedescribe" name="filedescribe" placeholder="" value="${entity.filedescribe}">
											</td>
											<td>
											</td>
										</tr>
										<tr>
											<td align="right">
												<label class="exampleInputCode">修改人：</label>
											</td>
											<td>
												<input type="text" class="form-control m-left-5" id="operator" name="operator1" placeholder="" disabled="disabled" value="${entity.operator}">
											</td>
											<td>
											</td>
										</tr>
										<tr>
											<td align="right">
												<label class="exampleInputCode">修改时间：</label>
											</td>
											<td>
												<input type="text" class="form-control m-left-5" id="creatdate1" name="creatdate1" placeholder="" disabled="disabled" value="${entity.createtime?string("yyyy-MM-dd HH:mm:ss")}">
											</td>
											<td>
											</td>
										</tr>
									</table>
									<input id="editimgid" type="hidden" value="${entity.id}">
							</form>
						<div class="modal-footer">
							<a id="saveeditimg" class="btn btn-success">保存</a>
							<a id="cancle" class="btn btn-danger" data-dismiss="modal">取消</a>
					</div>
			</div>
	</div>
<input type="hidden" id="imgclassifytype" name="" value="${imgclassifytype!'0'}">
<input type="hidden" id="videoclassifytype" name="" value="${videoclassifytype!'0'}">
<input type="hidden" id="audioclassifytype" name="" value="${audioclassifytype!'0'}">
<input type="hidden" id="otherclassifytype" name="" value="${otherclassifytype!'0'}">	
</body>
<script type="text/javascript">
		requirejs([ "system/medialibrary" ]);
</script>
<script type="text/javascript">
$(document).ready(function(){
	//校验imgid
	function checkNull(filetype){
		var result = false;
		if(!$("#fileimg").val()){
			$("#fileimg").css({"border":"2px solid #F70A37"});
			return result;
		}else if(!$("#filename").val()){
			$("#filename").css({"border":"2px solid #F70A37"});
			return result;
		}else{
			result = true;
		}
		return result;
	}
	$("#fileimg").blur(function(){
    	if($(this).val()){
    		$(this).css({"border":"2px solid #bdc3c7"});
    	}
    });
	$("#filename").blur(function(){
    	if($(this).val()){
    		$(this).css({"border":"2px solid #bdc3c7"});
    	}
    });
	//校验文件
	$("input[id^='file']").each(function(){
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
	        }else if ($(this).attr("id").indexOf('video') > 0 &&type!='' && type!='asx'&&type!='flv'&&type!='avi'&&type!='wmv'&&type!='mp4'&&type!='mov'&&type!='asf'&&type!='mpg'&&type!='rm'&&type!='rmvb'){
	        	$("#alert-info").show();
	        	$("#infotitle").html("文件类型错误！")
	        	$("#info").html("仅支持asx,flv,avi,wmv,mp4,mov,asf,mpg,rm,rmvb文件上传");
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
	$("#uploadfileimg").on("click", function(e) {
		if(checkNull('img')){
			str=$("#editimgid").val();
   			var path1 = str.lastIndexOf("\\");
            var tempname = str.substring(path1+1);
            var path2 = tempname.lastIndexOf(".");
            var name = tempname.substring(0,path2);
			$("#imgform").ajaxSubmit({
				url:'file/uploadonefile?id=' + $("#editimgid").val() + "&fileclassifytype=" + $("#imgclassifytype").val(),
				type:'POST',
				dataType:'json',
				clearForm: true,
				success: function (data) {
					var entity = eval(data.entity)
					if(data.result=='filebatchuploadcomplete'){
						$("#imgid").attr(entity.filepath);
						$("#filename").val(entity.filename);
						$("#filedescribe").val(entity.filedescribe);
						$("#operator").val(entity.operator);
						$("#creatdate1").val(entity.createtime);
						alert("上传成功！");
						window.fra_fileindex.initDatas("img");
					}else{
						$("#imgid").attr(entity.filepath);
						$("#filename").val(entity.filename);
						$("#filedescribe").val(entity.filedescribe);
						$("#operator").val(entity.operator);
						$("#creatdate1").val(entity.createtime);
						alert("上传失败！");
						window.fra_fileindex.initDatas("img");
					}
				}
			});
		}
	});
	$("#saveeditimg").on("click", function(e) {
		if($("#filename").val()){
			$("#imgform").ajaxSubmit({
				url:'file/uploadonefile?id=' + $("#editimgid").val() + "&fileclassifytype=" + $(".classify").val(),
				type:'POST',
				dataType:'json',
				clearForm: true,
				success: function (data) {
						var entity = eval(data.entity)
					if(data.result=='filebatchuploadcomplete'){
						$("#imgid").attr("src",data.newimgsrc);
						$("#filename").val(entity.filename);
						$("#filedescribe").val(entity.filedescribe);
						$("#operator").val(entity.operator);
						$("#creatdate1").val(entity.createtime);
						alert("保存成功！");
						$('#xDialog').modal('hide');
						window.fra_fileindex.initDatas(entity.filetype);
					}else{
						$("#imgid").attr(entity.filepath);
						$("#filename").val(entity.filename);
						$("#filedescribe").val(entity.filedescribe);
						$("#operator").val(entity.operator);
						$("#creatdate1").val(entity.createtime);
						alert("保存失败！");
						window.fra_fileindex.initDatas(entity.filetype);
					}
				}
			});
		}else{
			$("#filename").css({"border":"2px solid #F70A37"});
		}
		
	});
	$("#savevideos").on("click", function(e) {
		if(checkNull('video')){
			$("#videoform").ajaxSubmit({
				url:'file/uploadonefile?id=' + $("#editimgid").val() + "&fileclassifytype=" + $("#imgclassifytype").val(),
				type:'POST',
				dataType:'json',
				clearForm: true,
				success: function (data) {
					if(data.result=='filebatchuploadcomplete'){
						$("#alert-info").show();
			        	$("#infotitle").html("上传成功！");
			        	$("#info").html(data.count + "个视频上传成功！");
					}
				}
			});
		}
	});
	$("#saveaudios").on("click", function(e) {
		if(checkNull('audio')){
			$("#audioform").ajaxSubmit({
				url:'file/uploadfiles?filetype=audio',
				type:'POST',
				dataType:'json',
				clearForm: true,
				success: function (data) {
					if(data.result=='filebatchuploadcomplete'){
						$("#alert-info").show();
			        	$("#infotitle").html("上传成功！");
			        	$("#info").html(data.count + "个音频上传成功！");
					}
				}
			});
		}
	});
	$("#saveothers").on("click", function(e) {
		if(checkNull('other')){
			$("#otherform").ajaxSubmit({
				url:'file/uploadfiles?filetype=other',
				type:'POST',
				dataType:'json',
				clearForm: true,
				success: function (data) {
					if(data.result=='filebatchuploadcomplete'){
						$("#alert-info").show();
			        	$("#infotitle").html("上传成功！");
			        	$("#info").html(data.count + "个附件上传成功！");
					}
				}
			});
		}
	});
});
</script>
</html>