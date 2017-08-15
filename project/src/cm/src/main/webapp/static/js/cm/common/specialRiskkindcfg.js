function initEditSpecialRiskkindcfgScript(){
	//提交修改特殊险别配置信息
	$("#makesure").on("click", function (){
		cfgListSize = Number($("#specialRiskkindcfgListSize").val());
		if(cfgListSize == 0){
			alertmsg("请添加配置信息！");
			return;
		}
		//校验必须填写输入框
		var cfgInfo = $(".required");
		for(var i=0;i<cfgInfo.size();i++){
			if(!($.trim($(cfgInfo[i]).val()))){
				alertmsg("编号"+(Math.ceil((i+1)/2))+"配置信息不能为空！");
				return;
			}
		}
		//校验必须是数字的输入框
		var isNumberList = $(".isNumber");
		for(var i=0;i<isNumberList.size();i++){
			if(isNaN($.trim($(isNumberList[i]).val()))){
				alertmsg("编号"+(i+1)+"配置值应为数字！");
				return;
			}
		}
		//防止重复提交
		$(this).prop("disabled", true);
		$.ajax({
			url : 'common/insurancepolicyinfo/editSpecialRiskkindcfg',
			type : 'POST',
			data : $("#specialRiskkindcfgForm").serialize(),
			dataType : "json",
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$("#makesure").prop("disabled", false);
			},
			success : function(data) {
				if(data){
					if (data.status == "success") {
						$('#xDialog').modal('hide');
					}else if(data.status == "fail"){
						alertmsg(data.msg);
						//防止重复提交
						$("#makesure").prop("disabled", false);
					}
				}else{
					alertmsg("险别配置信息修改失败！");
					//防止重复提交
					$("#makesure").prop("disabled", false);
				}
			}
		});
	});
	function addLanWei(num){
		return "<tr class='kindcfgList' id='kindcfgList"+num+"'><td id='title"+num+"'>"+(num+1)+
			"</td><td><input type='text' class='form-control required' id='kindcfgKey"+num+"' name='spcRiskkindCfg["+num+
			"].cfgKey' value=''/></td><td><input type='text' class='form-control required isNumber' id='kindcfgValue"+num+"' name='spcRiskkindCfg["+num+
			"].cfgValue' value=''/></td><td><a href='#' id='delKindcfg"+num+"' class='delKindcfg'>删除</a></td></tr>";
	}
	//添加栏位点击事件
	$("#addKindcfgItem").on("click", function (){
		cfgListSize = Number($("#specialRiskkindcfgListSize").val());
		$("#specialRiskkindcfgTable").append(addLanWei(cfgListSize)).find("a#delKindcfg"+cfgListSize).on("click", function (){
			cfgListSize = Number($("#specialRiskkindcfgListSize").val());
			var curNum = ($(this).attr("id")).replace("delKindcfg","");
			$("tr").remove("#kindcfgList"+curNum);
			refreshInitIndex();
			$("#specialRiskkindcfgListSize").val(((cfgListSize-1)+""));
		});
		$("#specialRiskkindcfgListSize").val(((cfgListSize+1)+""));
	});
	//删除栏位点击事件
	$(".delKindcfg").on("click", function (){
		cfgListSize = Number($("#specialRiskkindcfgListSize").val());
		var curNum = ($(this).attr("id")).replace("delKindcfg","");
		$("tr").remove("#kindcfgList"+curNum);
		refreshInitIndex();
		$("#specialRiskkindcfgListSize").val(((cfgListSize-1)+""));
	});
	function refreshInitIndex(){
		//循环刷新栏位编号
		$("tr.kindcfgList").each(function(i){
			var oldIndex = ($(this).attr("id")).replace("kindcfgList","");
			$(this).attr("id","kindcfgList"+i);
			$(this).find("td#title"+oldIndex).text(i+1).attr("id","title"+i);
			$(this).find("input#kindcfgKey"+oldIndex).attr("name","spcRiskkindCfg["+i+"].cfgKey").attr("id","kindcfgKey"+i);
			$(this).find("input#kindcfgValue"+oldIndex).attr("name","spcRiskkindCfg["+i+"].cfgValue").attr("id","kindcfgValue"+i);
			$(this).find("a#delKindcfg"+oldIndex).attr("id","delKindcfg"+i);
			if($(this).find("input#kindcfgId"+oldIndex).size()>0){
				$(this).find("input#kindcfgId"+oldIndex).attr("name","spcRiskkindCfg["+i+"].id").attr("id","kindcfgId"+i);
			}
		});
	}
	
}