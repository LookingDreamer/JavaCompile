require(["jquery", "jqform",  "fuelux", "public"], function ($) {

	$(function () {
		
		$("[id$='classify']").change(function(){
			var type = $(this).attr("id").replace("classify","");
			initDatas(type);
		});
		
		
		//【保存修改】 按钮
		$("#updatemenubutton").on("click", function(e) {
			$.ajax({
				url:"updatebyid?id=" + $("#id").val() + "&codename=" + $("#codename").val(),
				type:'POST',
				async:false,
				dataType:'json',
				success:function(data){
					alertmsg("修改成功！");
					initDatas(data.parentcode);
				}
			});
		});
		
		$("#deletemenubutton").on("click", function(e) {
			var nowid=$("#id").val();
			if(confirm("确认要删除吗？")){
				$.ajax({
					url:"deletebyid?id="+nowid,
					type:"POST",
					dataType:"json",
					async:false,           //默认为true,异步处理,改为false,防止后面数据被提前加载
					success:function(data){
						alertmsg("删除成功！");
						initDatas(data.parentcode);
					}
				});
			}
		});
		
		$("#addmenubutton").on("click", function(e) {
			var codename=$("#codename").val();
			if(codename.length>0){
				$.ajax({
					url:"add?codename="+codename,
					type:"POST",
					dataType:"json",
					async:false,
					success:function(data){
						alertmsg("添加成功！");
						initDatas(data.parentcode);

					}
				});
			}

		});
		
		//init全部数据
		initDatas('all');
		//init分类数据
	
			$('#classifytypetree').tree({
				dataSource : function(options, callback) {
					var parentnodecode = "";
					if (!jQuery.isEmptyObject(options)) {
						console.log('options:', options);
						console.log(options.dataAttributes.id);
						parentnodecode = options.dataAttributes.id;
					}
					$.ajax({
						type : "POST",
						url : "/cm/file/initclassifytypedata?parentcode="+ parentnodecode,
						dataType : 'json',
						async:false,
						success : function(data) {
							callback({
								data : data
							});
						}
					});
				},
				multiSelect : false,
				cacheItems : true,
				folderSelect : false,
			});
			$('#classifytypetree').on('selected.fu.tree', function(e, selected) {
				getdatafromid(selected.selected[0].dataAttributes.id);
			});

			$('#classifytypetree').on('updated.fu.tree', function(e, selected) {
				getdatafromid(selected.selected[0].dataAttributes.id);
			});

			$('#classifytypetree').on('opened.fu.tree', function(e, info) {
				//initDatas(info.dataAttributes.parentcode);
				$.ajax({
					type : "POST",
					url : "/cm/file/initclassifytypedata",
					data : "parentcode=" + info.dataAttributes.id,
					dataType : 'json',
					async:false,
					success : function(data) {
						callback({
							data : data
						});
					}
				});
				getdatafromid(info.dataAttributes.id);
			});

			$('#classifytypetree').on('closed.fu.tree', function(e, info) {
				initDatas(info.dataAttributes.parentcode);
				getdatafromid(info.dataAttributes.id);
			});

			$("#iconurlbtn").on("click", function(e) {
				$('#showpic').modal();
			});
			$("#iconurlbtn_add").on("click", function(e) {
				$('#showpic').modal();
			});
			$("#addmenubtn").on("click", function(e) {
				$('#addmenu_modal').modal();
			});                                           
			


		
		$(".fui-icons span").on("click", function(e) {
			$("#iconurl").val($(e.target).attr("class"));
			$("#iconurlpic").html("<span class="+$(e.target).attr("class")+"></span>");
			$('#showpic').modal("hide");
		});
		

		
		function updatemenu(){
			if($("#nodename").val()==""){
				alertmsg("菜单名称不能为空");
				$("#nodename").focus();
				return;
			}
			/*if($("#activeurl").val()==""){
				alertmsg("URL不能为空");
				$("#activeurl").focus();
				return;
			}*/
			if($("#nodecode").val()==""){
				alertmsg("菜单code不能为空");
				$("#nodecode").focus();
				return;
			}
			if($("#parentnodecode").val()==""){
				alertmsg("父菜单code不能为空");
				$("#parentnodecode").focus();
				return;
			}
			if($("#nodelevel").val()==""){
				alertmsg("菜单等级不能为空");
				$("#nodelevel").focus();
				return;
			}
			if($("#childflag").val()==""){
				alertmsg("子菜单标记不能为空");
				$("#childflag").focus();
				return;
			}
			if($("#orderflag").val()==""){
				alertmsg("菜单顺序不能为空");
				$("#orderflag").focus();
				return;
			}
			/*if($("#status").val()==""){
				alertmsg("菜单状态不能为空");
				$("#status").focus();
				return;
			}*/
			if($("#iconurl").val()==""){
				alertmsg("图标样式不能为空");
				$("#iconurl").focus();
				return;
			}
			
			if(confirm("确认修改该菜单吗？")){
				$('#menuform').submit();
				/*$('#menuform').ajaxSubmit({
				url : 'menu/updatemenubyid',
				type : 'POST',
				dataType : "json",
				error : function() {
					alertmsg("Connection error");
				},
				success : function(data) {
					var result = data.flag;
					alertmsg(result);
					if (result=="success") {
						alertmsg("修改成功！");
					}else{
						alertmsg("修改失败！");;
					}
				}
			   });*/
			}
		};
		
		
		//预览按钮
		$("[id$='previewbutton']").on("click",function(){
			var ids = new Array();
			var count = 0;
			var previewtype = $(this).attr("id").replace("previewbutton", "");
			$("input[name^='" + previewtype + "checkbox']:checked").each(function(){
				ids[count] = $(this).attr("id").replace("checkbox", "");
				count = count + 1;
			});
			if(ids.length == 0){
				alertmsg("要选择一条数据！");
				return;
			}else if(ids.length > 1){
				alertmsg("只能选择一条数据！");
				return;
			}else{
				previewpicture(ids[0]);
			}
		});
		//上传按钮
		$("[id$='uploadbutton']").each(function(){
			$(this).on("click", function(e) {
				var type = $(this).attr("id").replace("uploadbutton","");
				var url = "/cm/file/medialibrary?filetype=" + type + "&classifytype=" + $("#" + type + "classify").val();
				window.parent.openDialogForCM(url);
			});
		});
//		//视频预览按钮
//		$("#yulan").on("click", function(e) {
//			alertmsg("yulan");
//			window.location.href="www.baidu.com";
//		});
		//音频试听按钮
		$("#shiting").on("click", function(e) {
			window.parent.previewaudio("111");
		});
		//【删除】按钮
		$("[id$='deletebutton']").on("click", function(e) {
			var ids = new Array();
			var count = 0;
			var deletetype = $(this).attr("id").replace("deletebutton", "");
			$("input[name^='" + deletetype + "checkbox']:checked").each(function(){
				ids[count] = $(this).attr("id").replace("checkbox", "");
				count = count + 1;
			});
			if(ids.length == 0){
				alertmsg("至少要选择一条数据！");
				return;
			}else{
				if(confirm("确认要删除这" + ids.length + "条数据吗？")){
					deletedatas(ids);
					initDatas(deletetype);
				}
			}
		});
		//【编辑】按钮editbutton
		$("[id$='editbutton']").on("click", function(e) {
			var ids = new Array();
			var count = 0;
			var edittype = $(this).attr("id").replace("editbutton", "");
			$("input[name^='"+edittype+"checkbox']:checked").each(function(){
				ids[count] = $(this).attr("id").replace("checkbox", "");;
				count = count + 1;
			});
			if(ids.length == 0){
				alertmsg("要选择一条数据！");
				return;
			}else if(ids.length > 1){
				alertmsg("只能选择一条数据！");
				return;
			}else{
				editone(ids[0]);
				initDatas(edittype);
			}
		});
		
		//【查询】按钮querybutton
		
		$("[id$='querybutton']").on("click", function(e) {
			var querytype = $(this).attr("id").replace("querybutton", "");  //文件类型
			var name=$("#"+querytype+"usercode").val();                                  //文件名称
			var describe=$("#"+querytype+"name").val();                                  //文件描述
			var taskid=$("#"+querytype+"taskid").val();                                  //业务编码
			var platenum=$("#"+querytype+"platenum").val();                                  //文件描述
			var insured=$("#"+querytype+"insured").val();                                  //文件描述
			var fileclassifytype=$("#"+querytype+"classify").val();
			var filetypedatas=querytype+"datas";
		  if(querytype.length>0){
			  $("#"+querytype+"form").ajaxSubmit({
				  url:'/cm/file/queryList?filetype='+querytype+'&fileclassifytype='+fileclassifytype+'&filename='+name+'&filedescribe='+describe+"&taskid="+taskid+"&platenum="+platenum+"&insured="+insured,
				  type:'POST',
				  dataType:'json',
				  success:function (data){
					  //alertmsg(data);
					  if(data.type=="audio"){
						  buildAudioData(data.datas);			  						  
					  }else if(data.type=="other"){
						  buildOtherData(data.datas);
					  }else if(data.type=="video"){
						  buildVideoData(data.datas);
				      }else if(data.type=="img"){
				    	  buildImgData(data.datas);
				      }
				  } 
			  
			  
			  });			  
		  } 
	
		});
		
	});
});

//切换tab页
function switchnav(navtab){
	$("[id$='panelbody']").each(function(){
		$(this).hide();
	});
	$("[id$='panelfooter']").each(function(){
		$(this).hide();
	});
	$("[id$='panelresult']").each(function(){
		$(this).hide();
	});
	$("[id$='li']").each(function(){
		$(this).removeClass();
	});
	$("#" + navtab + "panelbody").show();
	$("#" + navtab + "panelfooter").show();
	$("#" + navtab + "panelresult").show();
	//alertmsg(initDatas(navtab));
	$("#" + navtab + "li").attr("class","active");
}

//init数据及分类信息
function initDatas(fileType){
	$("#loading").show();
	if(fileType=='all'){
		$.ajax({
			url:"/cm/file/initdata",
			type:'POST',
			async:true,
			dataType:'json',
			success:function(data){
				buildImgData(eval(data.imgdatas));
				buildVideoData(eval(data.videodatas));
				buildAudioData(eval(data.audiodatas));
				buildOtherData(eval(data.otherdatas));
				buildImgClassifyData(eval(data.imgclassify),eval(data.classifytype));
				buildVideoClassifyData(eval(data.videoclassify),eval(data.classifytype));
				buildAudioClassifyData(eval(data.audioclassify),eval(data.classifytype));
				buildOtherClassifyData(eval(data.otherclassify),eval(data.classifytype));
			}
		});
	}else if(fileType=='img'){
		$.ajax({
			url:"/cm/file/initdata?filetype=" + fileType + "&fileclassifytype=" + $("#imgclassify").val(),
			type:'POST',
			async:true,
			dataType:'json',
			success:function(data){
				buildImgClassifyData(eval(data.imgclassify),eval(data.classifytype));
				buildImgData(eval(data.imgdatas));
			}
		});
	}else if(fileType=='video'){
		$.ajax({
			url:"/cm/file/initdata?filetype=" + fileType+"&fileclassifytype="+$("#videoclassify").val(),       
			type:'POST',
			async:true,
			dataType:'json',
			success:function(data){
				buildVideoClassifyData(eval(data.videoclassify),eval(data.classifytype));
				buildVideoData(eval(data.videodatas));
			}
		});
	}else if(fileType=='audio'){
		$.ajax({
			url:"/cm/file/initdata?filetype="+fileType+"&fileclassifytype="+$("#audioclassify").val(),
			type:'POST',
			async:true,
			dataType:'json',
			success:function(data){
				buildAudioClassifyData(eval(data.audioclassify),eval(data.classifytype));
				buildAudioData(eval(data.audiodatas));
			}
		});
	}else if(fileType=='other'){
		$.ajax({
			url:"/cm/file/initdata?filetype="+fileType+"&fileclassifytype="+$("#otherclassify").val(),
			type:'POST',
			async:true,
			dataType:'json',
			success:function(data){
				buildOtherClassifyData(eval(data.otherclassify),eval(data.classifytype));
				buildOtherData(eval(data.otherdatas));
			}
		});
	}else{
		
	}
}

function buildImgData(datas){
	$("#imgdatas").empty();
	for (var i = 0; i < datas.length; i++) {
		 var item = datas[i];
		 var tempstr = "";
		 tempstr += "<div class=\"col-xs-6 col-sm-3 col-md-2 col-lg-2\"><div class=\"thumbnail\">";
		 tempstr += "<div style=\"width: 100%; height: 90%; text-align:center; vertical-align:middle;line-height:100%; border: 0px solid #ccc\">";
		 tempstr += "<a href=\"javascript:tochecked(\'" + item.id + "\')\">";
		 tempstr += "<img style=\"width: 120px; height: 120px; text-align:center; vertical-align:middle;line-height:120px; border: 1px solid #ccc\" src=\"" + item.filepath + "\" title=\"" + item.filename + "\" width=\"130px\" height=\"130px\">";
		 tempstr += "</a></div><div class=\"caption\"><p><input id=\"checkbox" + item.id + "\" name=\"imgcheckbox\" type=\"checkbox\">&nbsp;&nbsp;";
		 tempstr += "<a class=\"a\" href=\"javascript:tochecked(\'" + item.id + "\')\" style=\"float:right;display:block;width:88px;word-break:keep-all;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;\" title=\""+item.filename+"\">" + item.filename + "</a></p>";
		 tempstr += "<i id=\"previewpicture" + item.id + "\" title=\"" + item.id + "\" class=\"glyphicon glyphicon-zoom-in\"></i>";
		 tempstr += "&nbsp;&nbsp;<i id=\"editimg" + item.id + "\" title=\"" + item.id + "\" class=\"glyphicon glyphicon-edit\"></i>&nbsp;&nbsp;<i id=\"deleteimg" + item.id + "\" title=\"" + item.id + "\" class=\"glyphicon glyphicon-trash\"></i>&nbsp;&nbsp;</div></div></div>";
		 $("#imgdatas").append(tempstr);
    }
	$("[id^='previewpicture']").each(function(){
		$(this).on("click", function(e) {
			var imgid = $(this).attr("title");
			previewpicture(imgid);
		});
	});
	 $("[id^='deleteimg']").each(function(){
		 $(this).on("click", function(e) {
			 if(confirm("确认要删除吗？")){
				var imgid = $(this).attr("title");
				if(typeof(imgid) == "undefined" || imgid=='' || imgid=='null'){
					return;
				}else{
					deleteone(imgid);
					initDatas("img");
				}
			 }
		});
	 });
	 $("[id^='editimg']").each(function(){
		 $(this).on("click", function(e) {
			var imgid = $(this).attr("title");
			editone(imgid);
		});
	 });
}

function buildVideoData(datas){
	$("#videodatas").empty();
	for (var i = 0; i < datas.length; i++) {
		 var item = datas[i];
		 var tempstr = "";
		 tempstr += "<div class=\"col-xs-6 col-sm-3 col-md-2 col-lg-2\"><div class=\"thumbnail\">";
		 tempstr += "<div style=\"width: 100%; height: 90%; text-align:center; vertical-align:middle;line-height:100%; border: 0px solid #ccc\">";
		 tempstr += "<a href=\"javascript:tochecked(\'" + item.id + "\')\">";
		 //tempstr += "<img style=\"width: 120px; height: 120px; text-align:center; vertical-align:middle;line-height:120px; border: 1px solid #ccc\" src=\"" + item.filepath + "\" title=\"" + item.filename + "\" width=\"130px\" height=\"130px\">";
		 tempstr += "</a></div><div class=\"caption\"><p><input id=\"checkbox" + item.id + "\" name=\"videocheckbox\" type=\"checkbox\">&nbsp;&nbsp;";
		 tempstr += "<a class=\"a\" href=\"javascript:tochecked(\'" + item.id + "\')\" style=\"float:right;display:block;width:88px;word-break:keep-all;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;\" title=\""+item.filename+"\">" + item.filename + "</a></p>";
		 tempstr += "<i id=\"yulan" + item.id + "\" title=\"" + item.id + "\" class=\"glyphicon glyphicon-play\"></i>";
		 tempstr += "&nbsp;&nbsp;<i id=\"editvideo" + item.id + "\" title=\"" + item.id + "\" class=\"glyphicon glyphicon-edit\"></i>&nbsp;&nbsp;<i id=\"deletevideo" + item.id + "\" title=\"" + item.id + "\" class=\"glyphicon glyphicon-trash\"></i>&nbsp;&nbsp;</div></div></div>";
		 $("#videodatas").append(tempstr);
    }
	$("[id^='yulan']").each(function(){
		$(this).on("click", function(e) {
			var videoid = $(this).attr("title");
			//var url = "/cm/file/medialibrary?filetype=" + type + "&classifytype=" + $("#" + type + "classify").val();
			var url = "/cm/file/mediavideoshow?previewvideoid="+videoid;
			window.parent.openDialogForCM(url);
					
		});
	});
	 $("[id^='deletevideo']").each(function(){
		 $(this).on("click", function(e) {
			 if(confirm("确认要删除吗？")){
			var videoid = $(this).attr("title");
			if(typeof(videoid) == "undefined" || videoid=='' || videoid=='null'){
				return;
			}else{
				deleteone(videoid);
				initDatas("video");
			}
			 }
		});
	 });
	 $("[id^='editvideo']").each(function(){
		 $(this).on("click", function(e) {
			var videoid = $(this).attr("title");
			editone(videoid);
		});
	 });
}

function buildAudioData(datas){
	$("#audiodatas").empty();
	for (var i = 0; i < datas.length; i++) {
		 var item = datas[i];
		 var tempstr = "";
		 tempstr += "<div class=\"col-xs-6 col-sm-3 col-md-2 col-lg-2\"><div class=\"thumbnail\">";
		 tempstr += "<div style=\"width: 100%; height: 90%; text-align:center; vertical-align:middle;line-height:100%; border: 0px solid #ccc\">";
		 tempstr += "<a href=\"javascript:tochecked(\'" + item.id + "\')\">";
		 //tempstr += "<img style=\"width: 120px; height: 120px; text-align:center; vertical-align:middle;line-height:120px; border: 1px solid #ccc\" src=\"" + item.filepath + "\" title=\"" + item.filename + "\" width=\"130px\" height=\"130px\">";
		 tempstr += "</a></div><div class=\"caption\"><p><input id=\"checkbox" + item.id + "\" name=\"audiocheckbox\" type=\"checkbox\">&nbsp;&nbsp;";
		 tempstr += "<a class=\"a\" href=\"javascript:tochecked(\'" + item.id + "\')\"  style=\"float:right;display:block;width:88px;word-break:keep-all;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;\" title=\""+item.filename+"\">" + item.filename + "</a></p>";
		 tempstr += "<i id=\"previewaudio" + item.id + "\" title=\"" + item.id + "\" class=\"glyphicon glyphicon-play\"></i>";
		 tempstr += "&nbsp;&nbsp;<i id=\"editaudio" + item.id + "\" title=\"" + item.id + "\" class=\"glyphicon glyphicon-edit\"></i>&nbsp;&nbsp;<i id=\"deleteaudio" + item.id + "\" title=\"" + item.id + "\" class=\"glyphicon glyphicon-trash\"></i>&nbsp;&nbsp;</div></div></div>";
		 $("#audiodatas").append(tempstr);
    }
	$("[id^='previewaudio']").each(function(){
		$(this).on("click", function(e) {
			var audioid = $(this).attr("title");
			var url = "/cm/file/mediaaudioshow?previewaudioid="+audioid;
			window.parent.openDialogForCM(url);
		});
	});
	 $("[id^='deleteaudio']").each(function(){
		 $(this).on("click", function(e) {
			 if(confirm("确认要删除吗？")){
			var audioid = $(this).attr("title");
			if(typeof(audioid) == "undefined" || audioid=='' || audioid=='null'){
				return;
			}else{
				deleteone(audioid);
				initDatas("audio");
			}
			 }
		});
	 });
	 $("[id^='editaudio']").each(function(){
		 $(this).on("click", function(e) {
			var audioid = $(this).attr("title");
			editone(audioid);
		
		});
	 });
}

function buildOtherData(datas){
	$("#otherdatas").empty();
	for (var i = 0; i < datas.length; i++) {
		 var item = datas[i];
		 var tempstr = "";
		 tempstr += "<div class=\"col-xs-6 col-sm-3 col-md-2 col-lg-2\"><div class=\"thumbnail\">";
		 tempstr += "<div style=\"width: 100%; height: 90%; text-align:center; vertical-align:middle;line-height:100%; border: 0px solid #ccc\">";
		 tempstr += "<a href=\"javascript:tochecked(\'" + item.id + "\')\">";
		 //tempstr += "<img style=\"width: 120px; height: 120px; text-align:center; vertical-align:middle;line-height:120px; border: 1px solid #ccc\" src=\"" + item.filepath + "\" title=\"" + item.filename + "\" width=\"130px\" height=\"130px\">";
		 tempstr += "</a></div><div class=\"caption\"><p><input id=\"checkbox" + item.id + "\" name=\"othercheckbox\" type=\"checkbox\">&nbsp;&nbsp;";
		 tempstr += "<a class=\"a\" href=\"javascript:tochecked(\'" + item.id + "\')\" style=\"float:right;display:block;width:88px;word-break:keep-all;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;\" title=\""+item.filename+"\">" + item.filename + "</a></p>";
		 tempstr += "<i id=\"downloadother" + item.id + "\" title=\"" + item.id + "\" class=\"glyphicon glyphicon-save\"/>";
		 tempstr += "&nbsp;&nbsp;<i id=\"editother" + item.id + "\" title=\"" + item.id + "\" class=\"glyphicon glyphicon-edit\"></i>&nbsp;&nbsp;<i id=\"deleteother" + item.id + "\" title=\"" + item.id + "\" class=\"glyphicon glyphicon-trash\"></i>&nbsp;&nbsp;</div></div></div>";
		 $("#otherdatas").append(tempstr);
    }
	$("[id^='downloadother']").each(function(){
		$(this).on("click", function(e) {
			var fileid = $(this).attr("title");
			window.open("download?fileid="+fileid);
		});
	});
	 $("[id^='deleteother']").each(function(){
		 $(this).on("click", function(e) {
			 if(confirm("确认要删除吗？")){
				var imgid = $(this).attr("title");
				if(typeof(imgid) == "undefined" || imgid=='' || imgid=='null'){
					return;
				}else{
					deleteone(imgid);
					initDatas("other");
				}
			 }
		});
	 });
	 $("[id^='editother']").each(function(){
		 $(this).on("click", function(e) {
			var imgid = $(this).attr("title");
			editone(imgid);
		});
	 });
}
function buildImgClassifyData(datas,classifytype){
	$("#imgclassify").empty();
	for (var i = 0; i < datas.length; i++) {
		var item = datas[i];
		if(classifytype==item.codevalue){
			$("#imgclassify").append("<option value='" + item.codevalue + "' selected='selected'>" + item.codename + "</option>");			
		}else if("null"==item.codevalue||""==item.codevalue||item.codevalue=="undefined"||"null"==classifytype||""==classifytype||classifytype=="undefined"){
			if(i==0){
				$("#imgclassify").append("<option value='" + item.codevalue + "' selected='selected'>" + item.codename + "</option>");			
			}else{
				$("#imgclassify").append("<option value='" + item.codevalue + "'>" + item.codename + "</option>");
			}
		}else{
			$("#imgclassify").append("<option value='" + item.codevalue + "'>" + item.codename + "</option>");
		}
	}
}

function buildVideoClassifyData(datas,classifytype){
	$("#videoclassify").empty();
	for (var i = 0; i < datas.length; i++) {
		var item = datas[i];
		if(classifytype==item.codevalue){
			$("#videoclassify").append("<option value='" + item.codevalue + "' selected='selected'>" + item.codename + "</option>");			
		}else if("null"==item.codevalue||""==item.codevalue||item.codevalue=="undefined"||"null"==classifytype||""==classifytype||classifytype=="undefined"){
			if(i==0){
				$("#videoclassify").append("<option value='" + item.codevalue + "' selected='selected'>" + item.codename + "</option>");			
			}else{
				$("#videoclassify").append("<option value='" + item.codevalue + "'>" + item.codename + "</option>");
			}
		}else{
			$("#videoclassify").append("<option value='" + item.codevalue + "'>" + item.codename + "</option>");
		}
	}
}

function buildAudioClassifyData(datas,classifytype){
	$("#audioclassify").empty();
	for (var i = 0; i < datas.length; i++) {
		var item = datas[i];
		if(classifytype==item.codevalue){
			$("#audioclassify").append("<option value='" + item.codevalue + "' selected='selected'>" + item.codename + "</option>");			
		}else if("null"==item.codevalue||""==item.codevalue||item.codevalue=="undefined"||"null"==classifytype||""==classifytype||classifytype=="undefined"){
			if(i==0){
				$("#audioclassify").append("<option value='" + item.codevalue + "' selected='selected'>" + item.codename + "</option>");			
			}else{
				$("#audioclassify").append("<option value='" + item.codevalue + "'>" + item.codename + "</option>");
			}
		}else{
			$("#audioclassify").append("<option value='" + item.codevalue + "'>" + item.codename + "</option>");
		}
	}
}

function buildOtherClassifyData(datas,classifytype){
	$("#otherclassify").empty();
	for (var i = 0; i < datas.length; i++) {
		var item = datas[i];
		if(classifytype==item.codevalue){
			$("#otherclassify").append("<option value='" + item.codevalue + "' selected='selected'>" + item.codename + "</option>");			
		}else if("null"==item.codevalue||""==item.codevalue||item.codevalue=="undefined"||"null"==classifytype||""==classifytype||classifytype=="undefined"){
			if(i==0){
				$("#otherclassify").append("<option value='" + item.codevalue + "' selected='selected'>" + item.codename + "</option>");			
			}else{
				$("#otherclassify").append("<option value='" + item.codevalue + "'>" + item.codename + "</option>");
			}
		}else{
			$("#otherclassify").append("<option value='" + item.codevalue + "'>" + item.codename + "</option>");
		}
	}
}

//删除图标
function deleteone(id) {
	var ids = new Array();
	ids[0] = id;
	deletedatas(ids);
}
//删除
function deletedatas(ids) {
	$.ajax({
		url:"deletedata?ids=" + ids,
		type:'POST',
		async:false,
		dataType:'json',
		success:function(data){
			alertmsg("删除成功！");
			if(data.result){
				return;
			}
		}
	});
}
//编辑图片
function editone(imgid){
	var url = "/cm/file/mediaedit?previewpicturelid=" + imgid + "&fileclassifytype=" + $("#imgclassify").val();
	window.parent.openDialogForCM(url);
}
//图片预览model
function previepic(imgid){
	window.parent.previewpicture(imgid);
}
//视频预览model
function previevideo(videoid){
	window.parent.previewvideo(videoid);
}
//点击图片选中多选框
function tochecked(imgid) {
	if($("#checkbox"+imgid).prop("checked")){
		$("#checkbox"+imgid).prop("checked",false); 
	}else{
		$("#checkbox"+imgid).prop("checked",true); 
	}
}
//预览图片
function previewpicture(imgid){
	var url = "/cm/file/previewpicturel?previewpicturelid=" + imgid + "&fileclassifytype=" + $("#imgclassify").val();
	window.parent.openDialogForCM(url);
}
function getdatafromid(id) {
	$.ajax({
		type : "POST",
		url : "/cm/file/querybyid",
		data : "id=" + id,
		dataType : "json",
		success : function(datainfo) {
			initDatas(datainfo.menuobject.parentcode);
			$("#codename").val(datainfo.menuobject.codename);
			$("#id").val(datainfo.menuobject.id);
		}
	});
}


