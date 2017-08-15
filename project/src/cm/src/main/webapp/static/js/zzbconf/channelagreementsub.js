function initchannelagreementScript () {
	//初始化已关联网点表格数据
	//默认一页显示十条记录
	var pagesize = 10;
	//当前调用的url
	var channelid=$("#channelid").val();
	var pageurl = "/cm/channel/getOutstore?";
	//当前调用的url查询参数 ----渠道id
	var pageurlparameter = "channelid="+channelid;
	$('#deptListTable').bootstrapTable({
        method: 'get',
        url: pageurl+pageurlparameter,
        cache: false,
        striped: true,
//        pagination: true,
//        pageList: [5, 10, 15, 20],
        sidePagination: 'server', 
        pageSize: pagesize,
        clickToSelect: true,
        columns: [{
            field: 'state',
            align: 'center',
            valign: 'middle',
            checkbox: true
        }, {
            field: 'deptid1',
            title: '集团',
            align: 'center',
            valign: 'middle',
            sortable: true
        }, {
            field: 'deptid2',
            title: '平台公司',
            align: 'center',
            valign: 'middle',
            sortable: true
        }, {
            field: 'deptid3',
            title: '法人公司',
            align: 'center',
            valign: 'middle',
            sortable: true
        }, {
            field: 'deptid4',
            title: '分公司',
            align: 'center',
            valign: 'middle',
            sortable: true
        }, {
            field: 'deptid5',
            title: '网点',
            align: 'center',
            valign: 'middle',
            sortable: true
        }, {
            field: 'scaleflag',
            title: '优先级',
            align: 'center',
            valign: 'middle',
            sortable: true
        }, {
            field: 'permflag',
            title: '设置权限',
            align: 'center',
            valign: 'middle',
            sortable: true
        }, {
            field: 'operate',
            title: '设置',
            align: 'center',
            valign: 'middle',
            sortable: false,
            //events: operateEvents,
            formatter: operateFormatter
        }]
    });

//	alert($("input[id='ProviderCode']").length);
//	alert($("input[id='prvcode']").length);
	for(var j=0;j<$("input[id='prvcode']").length;j++){
		for(var k=0;k<$("input[id='ProviderCode']").length;k++){
			if($("input.prvcodes")[j].value==$("input.ProviderCode")[k].value){
				$(".prvcode")[j].checked='true';
			}
		}
	}

	//添加关联点击事件
	$("#deptrelative").on("click",function(){
		var deptids1=$("#deptids1").val();
		var deptids2=$("#deptids2").val();
		var deptids3=$("#deptids3").val();
		var deptids4=$("#deptids4").val();
		var deptids5=$("#deptids5").val();
		var agreementid=$("#agreementid").val();
		$.ajax({
			url : '/cm/channel/savescope',
			type : 'POST',
			dataType : 'text',
			data :{
				"agreementid":agreementid,
				"deptid1":deptids1,
				"deptid2":deptids2,
				"deptid3":deptids3,
				"deptid4":deptids4,
				"deptid5":deptids5
			},
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data == "success") {
					alertmsg("添加网点关联成功！");
					//刷新已关联网点表格
					$('#deptListTable').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data == "fail"){
					alertmsg("添加网点关联失败！");
				}
			}
		});
	})
function deptconnchange(numb) {
	if (numb > 4) {
		return;
	}
	var partid = "deptidc" + numb;
	var nextid = "deptidc" + (numb + 1);
	$("#" + nextid).empty();
	$("#" + nextid).append("<option value='0'>请选择</option>")
	deptconnSelect(numb);
}
function deptconnSelect(numb) {
	$.ajax({
		url : "../provider/querydepttree",
		type : 'POST',
		dataType : "json",
		data : {
			id : $("#deptidc" + numb).val(),
		},
		async : true,
		error : function() {
			alert("Connection error");
		},
		success : function(data) {
			for (var int = 0; int < data.length; int++) {
				$("#" + "deptidc" + (numb+1)).append(
						"<option value='" + data[int].id + "'>"
								+ data[int].name + "</option>");
			}
			$("#" + "deptidc" + (numb+1)).get(0).selectedIndex;
//			deptconnchange(numb+1);
		}
	});
}
	//解除关联点击事件
	$("#disdeptrelative").on("click",function(){
		var agreementid=$("agreementid").val();
		var carTaskData = $('#deptListTable').bootstrapTable('getSelections');
		//alert("解除关联点击事件"+carTaskData.length);
		$.ajax({
			url : '/cm/channel/deleteAgreementdept',
			type : 'POST',
			dataType : 'text',
			data :{
				"deptList":JSON.stringify($('#deptListTable').bootstrapTable('getSelections'))
			},
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data == "success") {
					alertmsg("解除网点关联成功！");
					//刷新已关联网点表格
					$('#deptListTable').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data == "fail"){
//					alertmsg(data.msg);
					alertmsg("解除网点关联失败！");
				}
			}
		});
	})
	//关联网点的级联操作
	$(".deptchange").on("change",function(){
		var thnum = $(this).attr("id").replace("deptids","");
		deptchange(parseInt(thnum));
	})
	function deptchange(numb) {
		if (numb > 4) {
			return;
		}
		var partid = "deptids" + numb;
		var nextid = "deptids" + (numb + 1);
		$("#" + nextid).empty();
		$("#" + nextid).append("<option value='0'>请选择</option>")
		deptSelect(numb);
	}
	function deptSelect(numb) {
		$.ajax({
			url : "/cm/provider/querydepttree",
			type : 'POST',
			dataType : "json",
			data : {
				id : $("#deptids" + numb).val()
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				for (var int = 0; int < data.length; int++) {
					$("#deptids" + (numb + 1)).append(
							"<option value='" + data[int].id + "'>"
									+ data[int].name + "</option>");
				}
				deptchange(numb + 1);
			}
		});
	}
	//初始化第一个下拉框
	$.ajax({
		url : "/cm/provider/querydepttree",
		type : 'POST',
		dataType : "json",
		data : {
			id : ""
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			for (var int = 0; int < data.length; int++) {
				$("#deptids1").append(
						"<option value='" + data[int].id + "'>"
								+ data[int].name + "</option>");
			}
		}
	});
	//结算方式保存
//	$("#accountmodeformbtn").on("click",function(){
//		alert("xxx");
//		alert("结算方式保存");
//		$.ajax({
//			url : '/cm/channel/updateBillType',
//			type : 'POST',
//			dataType : 'json',
//			contentType: "application/json",
//			data :$("#accountmodeform").serialize(),
//			cache : false,
//			async : true,
//			error : function() {
//				alertmsg("Connection error");
//			},
//			success : function(data) {
//				if (data.status == "success") {
//					alertmsg("结算方式保存成功！");
//				}else if (data.status == "fail"){
////					alertmsg(data.msg);
//					alertmsg("结算方式保存失败！");
//				}
//			}
//		});
//	})
	//供应商应用范围保存
	$("#providerrangeformbtn").on("click",function(){
		var agreementid=$("#agreementid").val();
		 var str="";
		 for(var i=0;i<$("input[id='prvcode']:checked").length;i++){
			 str+=$("input[id='prvcode']:checked")[i].value+","
			 //$(".prvcode:checked")[i].value+",";
		 }
		 var str=str.substring(0,str.lenght);
		 if(str==""){
			 alert("请选择供应商");
			 return;
		 }
		 //alert(str);
		$.ajax({
			url : "/cm/channel/UploadAndSaveProvider",
			type : 'POST',
			dataType : 'text',
			data :{
				"agreementid":agreementid,
				"providerid":str
				},
			cache : false,
			async : true,
			success : function(data) {
				if (data == "success") {
					alertmsg("供应商应用范围保存成功！");
				}else if (data == "fail"){
//					alertmsg(data.msg);
					alertmsg("供应商应用范围保存失败！");
				}
			}
		});
	})
}

function saveAccountMode(){
	//alert($("#accountmodeform").serialize());
	var flag = true;
	if($("#channelrelativedept").val()==null||$("#channelrelativedept").val()==""){
		alert("请先为该渠道配置机构信息");
		flag = false;
		return false;
	}
	$(".agreementinter").each(function(){
		//alert($(this).find("input[type='checkbox']").prop("checked"));
		if($(this).find("input[type='checkbox']").prop("checked")==true){
			//alert($(this).find(".shortinput").val());
			if($(this).find(".isfree:checked").val()==null){
				alert("请录入是否免费");
				flag = false;
				return false;
			}
			if($(this).find("#perfee").val()==""||$(this).find("#perfee").val()==null){
				alert("请录入单次费用");
				flag = false;
				return false;
			}
			if($(this).find("#monthfree").val()==""||$(this).find("#monthfree").val()==null){
				alert("请录入免费次数");
				flag = false;
				return false;
			}
		}
	});
	if(flag)
		$.ajax({
			url : '/cm/channel/updateBillType',
			type : 'POST',
			data :$("#accountmodeform").serialize(),
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data == "success") {
					alertmsg("结算方式保存成功！");
				}else if (data == "fail"){
					alertmsg("结算方式保存失败！");
				}
			}
		});
}

 function operateFormatter(value, row, index) {
    return [
        '<a class="btn btn-primary" href="javascript:window.parent.openDialogForCM(\'channel/preEditChannelDeptLevel?agreementDeptid='+row.id+'\')">设置',
        '</a>'
    ].join('');
}

 function editChannelDeptLevel(){
	
		$.ajax({
			url : 'channel/editChannelDeptLevel',
		type : 'get',
		data : {
		"scaleflag":$("#scaleflag").val(),
		"permflag":$("#permflag").val(),
		"agreementDeptid":$("#agreementDeptid").val()
		},
		dataType : "text",
		async : true,
		error : function() {
			alert("Connection error");
		},
     success : function(data){
     	if (data == "success") {
	     		$('#xDialog').modal('hide');
     			dataBack();
			}else{
				alert("修改失败！请稍后重试！");
			}
		}
	});
}
 function dataBack(){
		var win = $(window.frames[2].document);
		var channelid=win.find("#channelid").val();
//		var pageurl = "/cm/channel/getOutstore?";
//		var pageurlparameter = "channelid="+channelid;
//		var a = $('#deptListTable');
//		win.find('#deptListTable').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
//		$('#deptListTable').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
//		$('#deptListTable').bootstrapTable('refresh');
		var pagesize = 10;
		//当前调用的url
		var pageurl = "/cm/channel/getOutstore?";
		//当前调用的url查询参数 ----渠道id
		var pageurlparameter = "channelid="+channelid;
		win.find('#deptListTable').bootstrapTable({
	        method: 'get',
	        url: pageurl+pageurlparameter,
	        cache: false,
	        striped: true,
//	        pagination: true,
//	        pageList: [5, 10, 15, 20],
	        sidePagination: 'server', 
	        pageSize: pagesize,
	        clickToSelect: true,
	        columns: [{
	            field: 'state',
	            align: 'center',
	            valign: 'middle',
	            checkbox: true
	        }, {
	            field: 'deptid1',
	            title: '集团',
	            align: 'center',
	            valign: 'middle',
	            sortable: true
	        }, {
	            field: 'deptid2',
	            title: '平台公司',
	            align: 'center',
	            valign: 'middle',
	            sortable: true
	        }, {
	            field: 'deptid3',
	            title: '法人公司',
	            align: 'center',
	            valign: 'middle',
	            sortable: true
	        }, {
	            field: 'deptid4',
	            title: '分公司',
	            align: 'center',
	            valign: 'middle',
	            sortable: true
	        }, {
	            field: 'deptid5',
	            title: '网点',
	            align: 'center',
	            valign: 'middle',
	            sortable: true
	        }, {
	            field: 'scaleflag',
	            title: '优先级',
	            align: 'center',
	            valign: 'middle',
	            sortable: true
	        }, {
	            field: 'permflag',
	            title: '设置权限',
	            align: 'center',
	            valign: 'middle',
	            sortable: true
	        }, {
	            field: 'operate',
	            title: '设置',
	            align: 'center',
	            valign: 'middle',
	            sortable: false,
	            //events: operateEvents,
	            formatter: operateFormatter
	        }]
	    });
}
	 