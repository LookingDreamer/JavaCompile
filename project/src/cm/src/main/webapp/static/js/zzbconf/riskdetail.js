require([ "jquery", "zTree", "zTreecheck", "flat-ui", "bootstrap-table","bootstrap", "bootstrapTableZhCn", "jqvalidatei18n","public","multiselect" ], function($) {
	// 数据初始化
	$(function() {
		//start 险种类型为车险，险种小类显示为车险的小类
		var current = $("#risktype").val();
		var risk_id=$("#riskid").val();
		if(current == "车险") {
			$("#status option").each(function(i){	
				$("#status option[parent!= '1']").hide();
				$("#status option[parent = '1']").show();							
			})
		}
		//end
		$("#init_data").on("click",function(){
			if(confirm("确认要增加默认险别吗？")){
				var id=$("#riskid").val();
				if(id == null || id == ""){
					alert("请先增加险种");
				}else{
					$.ajax({
						url:'initriskdata',
						type:'post',
						data:({riskId:id}),
						success:function(data){
							if(data==0){
								alertmsg("数据已经初始化完成，请勿重复点击");
							}else if(data==2){
								alertmsg("数据已经初始化失败");
							}else{
								
								var postdata;
								var riskid = $('#riskid').val();
								postdata += "&riskid=" + $("#riskid").val();
		//						}
								if ($("#kindname_q").val() != "") {
									postdata += "&kindname=" + $("#kindname_q").val();
								}
								if ($("#kindcode_q").val() != "") {
									postdata += "&kindcode=" + $("#kindcode_q").val();
								}
								$.ajax({
									url : riskpageurl,
									type : 'GET',
									dataType : "json",
									data : postdata + "&limit=" + pagesize + "&offset=" + offset,
									async : true,
									error : function() {
										alertmsg("Connection error");
									},
									success : function(data) {
										$('#table-kind').bootstrapTable('load', data);
									}
								});
							}
						}						
					})
				}				
			}
		})
		
			$('.js-multiselect').multiselect({
			right: '#js_multiselect_to_1',
			rightAll: '#js_right_All_1',
			rightSelected: '#js_right_Selected_1',
			leftSelected: '#js_left_Selected_1',
			leftAll: '#js_left_All_1',
			keepRenderingSort: true
		});
		
		$('#table-kind').bootstrapTable({
			method : 'get',
			url : 'riskkindlist?riskid='+risk_id,
			cache : false,
			striped : true,
			pagination : true,
			sidePagination : 'server',
			pageSize : pagesize,
			singleSelect : true,
			clickToSelect : true,
			columns : [ {
				field : 'state',
				align : 'center',
				valign : 'middle',
				checkbox : true
			}, {
				field : 'id',
				title : '险别ID',
				visible : false,
				switchable : false
			}, {
				field : 'kindcode',
				title : '险别编码',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'kindname',
				title : '险别名称',
				align : 'center',
				valign : 'middle',
				sortable : true
			},{
				field : 'isamount',
				title : '是否保额选项',
				align : 'center',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'amountselect',
				title : '保额选项',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'preriskkind',
				title : '前置保项',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'notdeductible',
				title : '不计免赔',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'noti',
				title : '备注',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'isusing',
				title : '是否启用',
				align : 'center',
				valign : 'middle',
				sortable : true
			} ]

		});
		$('#table-img').bootstrapTable({
			method : 'get',
			url : 'riskimglist?riskid='+risk_id,
			queryParams: queryParams,
			cache : false,
			striped : true,
			pagination : true,
			sidePagination : 'server',
			pageSize : pagesize,
			singleSelect : 'true',
			clickToSelect : true,
			columns : [ {
				field : 'state',
				align : 'center',
				valign : 'middle',
				checkbox : true
			}, {
				field : 'id',
				title : 'itemID',
				visible : false,
				switchable : false
			}, {
				field : 'seriesno',
				title : '序号',
				align : 'center',
				valign : 'middle',
				sortable : true				
			}, {
				field : 'riskimgtype',
				title : '影像类型',
				align : 'center',
				valign : 'middle',
				sortable : true
//			}, {
//				field : 'riskimgname',
//				title : '影像名称',
//				align : 'center',
//				valign : 'middle',
//				sortable : true
			}, {
				field : 'isusing',
				title : '是否启用',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'noti',
				title : '备注',
				align : 'center',
				valign : 'middle',
				sortable : true
			} ]

		});
		
		initRisk();

		$('#kindModal').on('show.bs.modal', function() {

		});
		// 选择供应商
		$("#providename").on("click", function(e) {
			$('#showpic').modal();
			$.fn.zTree.init($("#treeDemo"), setting);
		});
		$("#inputtype").on("change", function(){
			if($("#inputtype").val() == "1"){
				$("#optional").parent().hide();
			}else{
				$("#optional").parent().show();
			}
		});
		$("#riskaddform").validate({
			errorLabelContainer : "#riskerror",
			errorElement : "p",
			errorClass : "text-left",
			rules : {
				riskcode : {
					required : true,
					minlength : 5,
					maxlength : 20
				},
				riskname : {
					required : true,
					maxlength : 30
				},
				riskshortname : {
					maxlength : 30
				},
				provideid : {
					required : true
				},
				noti : {
					maxlength : 1000
				}
			},
			messages : {
				riskcode : {
					required : "险种编码不能为空！",
					minlength : "险种编码长度不能小于5！",
					maxlength : "险种编码长度不能大于20！"
				},
				riskname : {
					required : "险种名称不能为空！",
					minlength : "险种名称长度不能大于30！"
				},
				riskshortname : {
					maxlength : "险种简称长度不能大于30！"
				},
				provideid : {
					required : "保险公司不能为空！"
				},
				noti : {
					maxlength : "说明长度不能超过100字！！"
				}

			},
			highlight : validateHighlight,
			success : validateSuccess
		});

	});
});


function queryParams(params) {
	offset = params.offset
	return {
		offset: params.offset,
		limit: params.limit
	};
}
// 默认一页显示十条记录
var pagesize = 10;
var offset = 0;
// 当前调用的url
var riskpageurl = "riskkindlist";
function getSelectedRow() {
	var data = $('#table-kind').bootstrapTable('getSelections');
	if (data.length == 0) {
		return null;
	} else {
		return data[0].id;
	}
}
function initRisk() {
	kindaquery();
//	itemquery();
//	reitemquery();
	imgquery();
}
function back() {
	var id=$("#riskid").val();
	$.ajax({
		url:'checkhaskind',
		type:'post',
		data:({riskid:id}),
		success:function(data){
			// if(data==0){
			// 	alertmsg("请选择对应的险别！");
			// }else{
				var riskid = getSelectedRow();
				location.href = "list";
			// }
		}
	})	
}
function riskadd() {
	if ($("#riskaddform").valid()) {
		var renewaltype = "";
		$("input:checkbox[name='renewaltype']:checked").each(function(i){
			if(renewaltype!=""){
				renewaltype+=",";
			}
			renewaltype += $(this).val();
		})
		$.ajax({
			url : "riskaddorupdate",
			type : 'POST',
			dataType : "json",
			data : {
				riskcode : $('#riskcode').val(),
				riskname : $('#riskname').val(),
				riskshortname : $('#riskshortname').val(),
				provideid : $('#provideid').val(),
				risktype : "1",//$('#risktype').val(),
				status : $('#status').val(),
				noti : $('#noti').val(),
				id : $('#riskid').val(),
				renewaltype:renewaltype
			
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				$('#riskid').val(data.id);
				alertmsg("保存成功！");
			}
		});
	}
}
function kindadd(flag) {
	if ($('#riskid').val() == null || "" == $('#riskid').val()) {
		alertmsg("请先保存险种详情!");
		return;
	}
	$('#kindidflag').val(flag);
	clearkind();
	$('#kindModal').modal('show');
}
function kinddelete() {
	var kindid = getSelectedRow();
	if (kindid == null || "" == kindid) {
		alertmsg("请选择!");
		return;
	}
	$.ajax({
		url : "riskkinddelete",
		type : 'POST',
		dataType : "json",
		data : "kindid=" + getSelectedRow(),
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			kindaquery();
			alertmsg("删除成功!");
		}
	});
}
function kindupdate(flag) {
	if ($('#riskid').val() == null || "" == $('#riskid').val()) {
		alertmsg("请先保存险种详情!");
		return;
	}
	$('#kindidflag').val(flag);
	var kindid = getSelectedRow();
	if(kindid==null||""==kindid){
		alertmsg("请选择!");
		return;
	}
	clearkind();
	$('#kindModal').modal('show');
	$.ajax({
		url : "getriskkind",
		type : 'GET',
		dataType : "json",
		data : "kindid=" +kindid,
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#kindid').val(data.id);
			$('#kindcode').val(data.kindcode);
			$('#kindname').val(data.kindname);
			//$('#kindtype').val(data.kindtype);
			$('#isamount').val(data.isamount);
			$('#amountselect').val(data.amountselect);
			$('#preriskkind').val(data.preriskkind);
			$('#notdeductible').val(data.notdeductible);
			$('#isusing').val(data.isusing + "");
			$('#kindnoti').val(data.noti);
		}
	});
}

function clearkind() {	
	$('#kindModal').modal('hide');
	$('#kindaddfrom')[0].reset();
}
//险别提交保存
function saveorupdatekind() {
	//var kindid = getSelectedRow();
	var kindidflag = $('#kindidflag').val();
	//当为新增时，将选中的kindid 值清空
	if(kindidflag == '0'){
		$('#kindid').val("");
	}
	$.ajax({
		url : "checkkindcode",
		type : 'POST',
		dataType : "json",
		cache:false,
		data:{
			id:function(){return $('#kindid').val()},
			kindcode:function(){return $("#kindcode").val()},
			riskid:function(){return $("#riskid").val()}
		},
	error : function() {
		alertmsg("Connection error");
	},
	success : function(res) {
		if(!$("#kindcode").val()){
			alertmsg("险别编码不能为空！险别名称不能为空！");
		}else if(res == true){
			$.ajax({
				url : "riskkindaddorupdate",
				type : 'POST',
				dataType : "json",
				data : 
					//$("#kindaddfrom").serialize(),
				{
					riskid : $('#riskid').val(),
					id : $('#kindid').val(),
					kindcode : $('#kindcode').val(),
					kindname : $('#kindname').val(),
					//kindtype : $('#kindtype').val(),
					isamount : $('#isamount').val(),
					amountselect : $('#amountselect').val(),
					preriskkind : $('#preriskkind').val(),
					notdeductible : $('#notdeductible').val(),
					isusing : $('#isusing').val(),
					noti : $('#kindnoti').val()
				},
				async : false,
				error : function() {
					alertmsg("Connection error");
				},
				success : function(data) {
					clearkind();
					kindaquery();
				}
			});
		}else{
			alertmsg("已存在");
			}
		}
	});
}

//險別新增里的查詢險別功能 
function selectkind() {
	var riskCode = $("#selectKind").val();
	if (!riskCode) {
		return;
	}
	$.ajax({
		url : "riskkindbycode",
		type : 'get',
		dataType : "json",
		data :{
			riskkindcode:riskCode
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(datainfo) {
			 $("#kindcode").val(datainfo.riskkindcode);
			 $("#kindname").val(datainfo.riskkindname);	 
			 $("#preriskkind").val(datainfo.prekindcode);	
		}
	});
}
function kindaquery() {
	var postdata;
	var riskid = $('#riskid').val();
	postdata += "&riskid=" + $("#riskid").val();
//	if ($("#kindtype_q").val() != "") {
//		postdata += "&kindtype=" + $("#kindtype_q").val();
//	}
	if ($("#kindname_q").val() != "") {
		postdata += "&kindname=" + $("#kindname_q").val();
	}
	if ($("#kindcode_q").val() != "") {
		postdata += "&kindcode=" + $("#kindcode_q").val();
	}
	var riskid = $('#riskid').val();
	$.ajax({
		url : riskpageurl,
		type : 'GET',
		dataType : "json",
		data : postdata + "&limit=" + pagesize + "&offset=" + offset,
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#table-kind').bootstrapTable('load', data);
		}
	});
}
//function getSelecteditemRow() {
//	var data = $('#table-item').bootstrapTable('getSelections');
//	if (data.length == 0) {
//		return null;
//	} else {
//		return data[0].id;
//	}
//}
//function itemdelete() {
//	var itemid = getSelecteditemRow();
//	if (itemid == null || "" == itemid) {
//		alertmsg("请选择!");
//		return;
//	}
//	$.ajax({
//		url : "riskitemdelete",
//		type : 'POST',
//		dataType : "json",
//		data : "itemid=" + getSelecteditemRow(),
//		async : true,
//		error : function() {
//			alertmsg("Connection error");
//		},
//		success : function(data) {
//			//kindaquery();
//			itemquery();
//			alertmsg("删除成功!");
//		}
//	});
//}
//function itemquery() {
//	var postdata;
//	var riskid = $('#riskid').val();
//	postdata += "&riskid=" + $("#riskid").val();
//	if ($("#itemtype_q").val() != "") {
//		postdata += "&itemtype=" + $("#itemtype_q").val();
//	}
//	if ($("#itemname_q").val() != "") {
//		postdata += "&itemname==" + $("#itemname_q").val();
//	}
//	if ($("#iteminputtype_q").val() != "") {
//		postdata += "&inputtype=" + $("#iteminputtype_q").val();
//	}
//	$.ajax({
//		url : "riskitemlist",
//		type : 'GET',
//		dataType : "json",
//		data : postdata + "&limit=" + pagesize,
//		async : true,
//		error : function() {
//			alertmsg("Connection error");
//		},
//		success : function(data) {
//			$('#table-item').bootstrapTable('load', data);
//		}
//	});
//}
//function itemadd() {
//	if ($('#riskid').val() == null || "" == $('#riskid').val()) {
//		alertmsg("请先保存险种详情!");
//		return;
//	}
//	clearitem();
//	$('#itemModal').modal('show');
//}
//function clearitem() {
//	$('#itemModal').modal('hide');
//	$('#itemaddform')[0].reset() 
//}
//function itemupdate() {
//	if ($('#riskid').val() == null || "" == $('#riskid').val()) {
//		alertmsg("请先保存险种详情!");
//		return;
//	}
//	var kindid = getSelecteditemRow();
//	if(kindid==null||""==kindid){
//		alertmsg("请选择!");
//		return;
//	}
//	clearitem();
//	$('#itemModal').modal('show');
//	$.ajax({
//		url : "getriskitem",
//		type : 'GET',
//		dataType : "json",
//		data : "itemid=" + kindid,
//		async : true,
//		error : function() {
//			alertmsg("Connection error");
//		},
//		success : function(data) {
//			$('#itemid').val(data.id);
//			$('#itemcode').val(data.itemcode);
//			$('#itemname').val(data.itemname);
//			$('#itemtype').val(data.itemtype);
//			$('#itemisusing').val(data.isusing);
//			$('#inputtype').val(data.inputtype);
//			$('#optional').val(data.optional);
//			$('#isquotemust').val(data.isquotemust);
//			$('#isunderwritemust').val(data.isunderwritemust);
//			$('#isunderwriteusing').val(data.isunderwriteusing);
//			$('#itemnoti').val(data.noti);
//		}
//	});
//}
//function saveorupdateitem() {
//	if ($("#itemaddform").valid()) {
//		$.ajax({
//			url : "riskitemaddorupdate",
//			type : 'POST',
//			dataType : "json",
//			data : {
//				id : $('#itemid').val(),
//				noti : $('#itemnoti').val(),
//				riskid : $('#riskid').val(),
//				itemname : $('#itemname').val(),
//				itemcode : $('#itemcode').val(),
//				itemtype : $('#itemtype').val(),
//				isusing : $('#itemisusing').val(),
//				isquotemust : $('#isquotemust').val(),
//				inputtype : $('#inputtype').val(),
//				optional : $('#optional').val(),
//				isunderwriteusing : $('#isunderwriteusing').val(),
//				isunderwritemust : $('#isunderwritemust').val(),
//			},
//			async : true,
//			error : function() {
//				alertmsg("Connection error");
//			},
//			success : function(data) {
//				clearitem();
//				itemquery();
//			}
//		});
//	}
//}
//function getSelectedreitemRow() {
//	var data = $('#table-reitem').bootstrapTable('getSelections');
//	if (data.length == 0) {
//		return null;
//	} else {
//		return data[0].id;
//	}
//}
//function reitemdelete() {
//	var reitemid = getSelectedreitemRow();
//	if (reitemid == null || "" == reitemid) {
//		alertmsg("请选择!");
//		return;
//	}
//	$.ajax({
//		url : "riskrenewalitemdelete",
//		type : 'POST',
//		dataType : "json",
//		data : "reitemid=" + reitemid,
//		async : true,
//		error : function() {
//			alertmsg("Connection error");
//		},
//		success : function(data) {
//			//kindaquery();
//			reitemquery();
//			alertmsg("删除成功!");
//		}
//	});
//}
//function reitemquery() {
//	var postdata;
//	var riskid = $('#riskid').val();
//	postdata += "&riskid=" + $("#riskid").val();
//	if ($("#reitemname_q").val() != "") {
//		postdata += "&reitemname=" + $("#reitemname_q").val();
//	}
//	//显示单买交强为‘是’的险别
//	if($("#reitemtype_q").val() == "istraffic"){
//		postdata += "&istraffic=" + "1";
//	}
//	//显示单买商业为‘是’的险别
//	if ($("#reitemtype_q").val() == "isbusiness") {
//		postdata += "&isbusiness=" + "1";
//	}
//	//显示交强+商业为‘是’的险别
//	if ($("#reitemtype_q").val() == "istrafficandbusiness") {
//		postdata += "&istrafficandbusiness=" + "1";
//	}
//	$.ajax({
//		url : "riskrenewalitemlist",
//		type : 'GET',
//		dataType : "json",
//		data : postdata + "&limit=" + pagesize,
//		async : true,
//		error : function() {
//			alertmsg("Connection error");
//		},
//		success : function(data) {
//			$('#table-reitem').bootstrapTable('load', data);
//		}
//	});
//}
//function reitemadd() {
//	if ($('#riskid').val() == null || "" == $('#riskid').val()) {
//		alertmsg("请先保存险种详情!");
//		return;
//	}
//	clearreitem();
//	$('#reitemModal').modal('show');
//}
//function clearreitem() {
//	$('#reitemModal').modal('hide');
//	$('#reitemfrom')[0].reset(); 
//}
//function reitemupdate() {
//	if ($('#riskid').val() == null || "" == $('#riskid').val()) {
//		alertmsg("请先保存险种详情!");
//		return;
//	}
//	var kindid = getSelectedreitemRow();
//	if(kindid==null||""==kindid){
//		alertmsg("请选择!");
//		return;
//	}
//	clearreitem();
//	$('#reitemModal').modal('show');
//	$.ajax({
//		url : "getriskrenewalitem",
//		type : 'GET',
//		dataType : "json",
//		data : "itemid=" + kindid,
//		async : true,
//		error : function() {
//			alertmsg("Connection error");
//		},
//		success : function(data) {
//			$('#reitemid').val(data.id);
//			$('#reitemname').val(data.reitemname);
//			$('#isbusiness').val(data.isbusiness);
//			$('#istrafficandbusiness').val(data.istrafficandbusiness);
//			$('#istraffic').val(data.istraffic);
//			$('#presentation').val(data.presentation);
//			$('#reitemnoti').val(data.noti);
//		}
//	});
//}
//function saveorupdatereitem() {
//	if ($("#reitemfrom").valid()) {
//		$.ajax({
//			url : "riskrenewalitemaddorupdate",
//			type : 'POST',
//			dataType : "json",
//			data : {
//				id : $('#reitemid').val(),
//				riskid : $('#riskid').val(),
//				reitemname : $('#reitemname').val(),
//				isbusiness : $('#isbusiness').val(),
//				istrafficandbusiness : $('#istrafficandbusiness').val(),
//				istraffic : $('#istraffic').val(),
//				presentation : $('#presentation').val(),
//				noti : $('#reitemnoti').val(),
//			},
//			async : true,
//			error : function() {
//				alertmsg("Connection error");
//			},
//			success : function(data) {
//				clearreitem();
//				reitemquery();
//			}
//		});
//	}
//
//}
function getSelectedimgRow() {
	var data = $('#table-img').bootstrapTable('getSelections');
	if (data.length == 0) {
		return null;
	} else {
		return data[0].id;
	}
}
function imgdelete() {
	var imgid = getSelectedimgRow();
	if (imgid == null || "" == imgid) {
		alertmsg("请选择!");
		return;
	}
	$.ajax({
		url : "riskimgdelete",
		type : 'POST',
		dataType : "json",
		data : "imgid=" + imgid,
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			imgquery();
			alertmsg("删除成功!");
		}
	});
}
function imgquery() {
	var postdata;
	var riskid = $('#riskid').val();
	postdata += "&riskid=" + $("#riskid").val();
//	if ($("#riskimgname_q").val() != "") {
//		postdata += "&riskimgname=" + $("#riskimgname_q").val();
//	}
	if ($("#riskimgtype_q").val() != "") {
		postdata += "&riskimgtype=" + $("#riskimgtype_q").val();
	}
	$.ajax({
		url : "riskimglist",
		type : 'GET',
		dataType : "json",
		data : postdata + "&limit=" + pagesize+"&offset="+offset,
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#table-img').bootstrapTable('load', data);
		}
	});
}

function init_riskimg() {
	if(confirm("确认要置为默认影像吗？")){
		var id = $("#riskid").val();
        if(id == null || id == ""){
            alert("请先增加险种");
        }else{
			$.ajax({
				url : "riskimgaddinit",
				type : 'POST',
				dataType : "json",
				data : {
					riskid : id
				},
				async : true,
				success : function(data) {
					if(data == 0){
						alertmsg("数据已经初始化完成，请勿重复点击");
						clearimg();
						imgquery();
                    }else if(data == 2){
                        alertmsg("数据初始化失败");
                    }
				}
			});
        }
	}
}

function imgadd() {
	if ($('#riskid').val() == null || "" == $('#riskid').val()) {
		alertmsg("请先保存险种详情!");
		return;
	}
	//未添加的影像类型
	$.ajax({
		url : "notselectedriskimgshow",
		type : 'GET',
		dataType : "json",
		data : "riskid=" + $('#riskid').val(),
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$("select[name='from']").empty();
			for(var i=0; i<data.length; i++){
				$("select[name='from']").append("<option value='"+data[i].codevalue+"'>"+data[i].codename+"</option>");
			}			
		}
	})
	//已添加的影像类型
	$.ajax({
		url : "addriskimgshow",
		type : 'GET',
		dataType : "json",
		data : "riskid=" + $('#riskid').val(),
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$("select[name='to']").empty();
			for(var i=0; i<data.length; i++){
				$("select[name='to']").append("<option value='"+data[i].riskimgtype+"'>"+data[i].riskimgname+"</option>");
			}
			
		}
	})
	clearaddimg();
	$('#addimgModal').modal('show');
}
function clearaddimg() {
	$('#addimgModal').modal('hide');
	$('#addimgfrom')[0].reset();
}
function clearimg() {
	$('#imgModal').modal('hide');
	$('#imgfrom')[0].reset();
}
function imgupdate() {
	if ($('#riskid').val() == null || "" == $('#riskid').val()) {
		alertmsg("请先保存险种详情!");
		return;
	}
	var imgid = getSelectedimgRow();
	if(imgid==null||""==imgid){
		alertmsg("请选择!");
		return;
	}
	clearimg();
	$('#imgModal').modal('show');
	
	$.ajax({
		url : "getriskimg",
		type : 'GET',
		dataType : "json",
		data : "imgid=" + imgid,
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#imgid').val(data.id);
			$('#riskimgname').val(data.riskimgname);
			$('#riskimgtype').val(data.riskimgtype);
			$('#imgisusing').val(data.isusing);
			$('#imgnoti').val(data.noti);
		}
	})
}
//修改的提交保存按钮
function updateimg() {
	var optionValueListStr = "update";
		$.ajax({
			url : "riskimgaddorupdate/"+optionValueListStr,
			type : 'POST',
			dataType : "json",
			data : {
				id : $('#imgid').val(),
				riskid : $('#riskid').val(),
				riskimgname : $('#rt_'+$('#riskimgtype').val()).html(),
				riskimgtype : $('#riskimgtype').val(),
				isusing : $('#imgisusing').val(),
				noti : $('#imgnoti').val()
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				clearimg();
				clearaddimg();
				imgquery();
			}
		});
	}
//新增的提交保存按钮
function saveimg() {
	var optionValueListStr = ",";
	var optionList = $('select[name="to"]').find("option");
	for(var i=0; i<optionList.size(); i++){
		optionValueListStr += optionList[i].value + "_" + optionList[i].innerHTML + ",";
	}
	if(optionValueListStr.length > 1){
		optionValueListStr.substr(0, (optionValueListStr.length-2));
	}
	//alert(optionValueListStr);
		$.ajax({
			url : "riskimgaddorupdate/"+optionValueListStr,
			type : 'POST',
			dataType : "json",
			data : {
				id : $('#imgid').val(),
				riskid : $('#riskid').val(),
				riskimgname : $('#riskimgname').val(),
				riskimgtype : $('#riskimgtype').val(),
				isusing : $('#imgisusingadd').val(),
				noti : $('#imgnoti').val()
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				clearimg();
				clearaddimg();
				imgquery();
			}
		});
	}
function showdiv(shortdivname) {
	$('#kinddiv').hide();
	$('#itemdiv').hide();
	$('#xbdiv').hide();
	$('#imgdiv').hide();
	$("#kindli").removeClass();
	$("#itemli").removeClass();
	$("#imgli").removeClass();
	$("#xbli").removeClass();
	$("#" + shortdivname + "li").addClass("active");
	$("#" + shortdivname + "div").show();
}
var setting = {
	async : {
		enable : true,
		url : "../provider/queryprotree",
		autoParam : [ "id" ],
		dataType : "json",
		type : "post"
	},
	check : {
		enable : true,
		chkStyle : "radio",
		radioType : "all"
	},
	callback : {
		onCheck : zTreeOnCheck
	}
};
function zTreeOnCheck(event, treeId, treeNode) {
	$("#providecode").val(treeNode.prvcode);
	$("#providename").val(treeNode.name);
	$('#showpic').modal("hide");
}
//$(function(){
//	$('#risktype').change(function(){
//	$('#risktype option').each(function(i){
//			if($(this).attr('selected')){	
//			var current = $("#risktype").val();
//			alertmsg("12345");
//			alertmsg(current);
//			if(current == "1") {
//				$("#riskstatus option").each(function(j){								
//						$("#riskstatus option[value!= '"+current+"']").hide();
//						$("#riskstatus option[value = '"+current+"']").show();							
//				})
//			}
//			if(current == "2"){
//				$("#riskstatus option").each(function(j){	
//						$("#riskstatus option[value!= '"+current+"']").hide(); 
//						$("#riskstatus option[value = '"+current+"']").show(); 							 													
//				})
//			}
//			}
//	}) 	
//})
//});