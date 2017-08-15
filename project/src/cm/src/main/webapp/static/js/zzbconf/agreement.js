require(["jquery",
	"bootstrap-table",
	"bootstrap",
	"bootstrapTableZhCn",
	"bootstrapdatetimepicker",
	"bootstrapdatetimepickeri18n",
	"jqvalidatei18n",
	"additionalmethods",
	"public",
	"jqtreeview",
	"zTree",
	"zTreecheck",
	"multiselect"], function($) {
	// 数据初始化
	$(function() {
		inittree();

		$("#addonedept").on("click", function(e) {

			/*
			 * if(!$("#comcode").val()){ alertmsg("请选择机构!") }
			 */
			$("#upcomcode").val($("#comcode").val());
			$("#comcode").val("");
			$("#deptinnercode").val("");
			$("#comname").val("");
			$("#comtype").val("");
			$("#comgrade").val("");
			$("#rearcomcode").val("");
			$("#province").val("");
			$("#city").val("");
			$("#county").val("");
			$("#address").val("");
			$("#webaddress").val("");
			$("#zipcode").val("");
			$("#phone").val("");
			$("#fax").val("");
			$("#email").val("");
			$("#satrapname").val("");
			$("#satrapphone").val("");
			$("#satrapcode").val("");
			$("#id").val("");
		});
		// 选择地区
//		initArea();
		$("#deldept").on("click", function(e) {
			if (!$("#id").val()) {
				alertmsg("请选择机构!");
			}
			$("#delid").val($("#id").val());
		});

		$('#myTree').on('selected.fu.tree', function(e, selected) {
			getdatafromid(selected.selected[0].dataAttributes.id);
		});

		$('#myTree').on('updated.fu.tree', function(e, selected) {
			getdatafromid(selected.selected[0].dataAttributes.id);
		});

		$('#myTree').on('opened.fu.tree', function(e, info) {
			getdatafromid(info.dataAttributes.id);
		});

		$('#myTree').on('closed.fu.tree', function(e, info) {
			getdatafromid(info.dataAttributes.id);
		});

		$("#savebutton").on("click", function(e) {
			if ($("#orgsaveform").valid()) {
				$("#orgsaveform").submit();
			}
		});

		$('#syncdept').on('click', function() {
			syncDeptData();
		});
		$(window).resize(function () {
			$('#table-agreement').bootstrapTable('resetView');
		});
		$('#table-agreement').bootstrapTable({


			method: 'get',
			url: "getagreementbycomcode",
			cache: false,
			striped: true,
			pagination: true,
			sidePagination: 'server',
			pageSize: pagesize,
			pageList: [5, 10,50,100],
			minimumCountColumns: 2,
			clickToSelect: true,
			singleSelect : false,
			queryParams : queryParams,
			columns : [ {
				field : 'state',
				align : 'center',
				valign : 'middle',
				checkbox : true
			}, {
				field : 'id',
				title : '协议ID',
				visible : false,
				switchable : false
			}, {
				field : 'agreementcode',
				title : '协议编码',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'agreementname',
				title : '协议名称',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'providerid',
				title : '关联供应商',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'deptid',
				title : '关联机构',
				align : 'center',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'modifytime',
				title : '更新时间',
				align : 'center',
				valign : 'middle',
				sortable : true,
				formatter : dateFormatter
			}, {
				field : 'underwritestatus',
				title : '核保状态',
				align : 'center',
				valign : 'middle',
				sortable : true,
				formatter : function (value, row, index) {
					if(1==value) {
						return "已生效"
					} else {
						return "未生效";
					}
				},
			},{
				field : 'agreementstatus',
				title : '状态',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'operating',
				title : '修改',
				align : 'center',
				valign : 'middle',
				switchable : false,
				formatter : operateFormatter1,
				events : operateEvents1
			} ]
		});
		// 选择供应商
		$("#queryprovidername").on("click", function(e) {
			$('#showQueryPro').modal();
			$('#queryProTreesearch').val('');
//			$.fn.zTree.init($("#queryProTree"), );
			queryProSetting();

		});

//		$("#deptname").on("click", function(e) {
//			$('#showdept').modal();
//			$.fn.zTree.init($("#depttree"), deptsetting);
//		});
//		$("#toggle").on("click", function(e) {
//			$('#table-agreement').bootstrapTable('toggleView');
//		});
//		initArea();
	});
});
// function operateFormatter(value, row, index) {
// return [
// '<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
// '<i class="glyphicon glyphicon-edit"></i>',
// '</a>'
// ].join('');
// }
// window.operateEvents = {
// 'click .edit': function (e, value, row, index) {
// updateagreement(row.id);
// }
// };
// 默认一页显示3条记录
var pagesize = 5;
// 当前调用的url
var pageurl = "getagreementbycomcode";
function getSelectedRow() {
	var data = $('#table-agreement').bootstrapTable('getSelections');
	if (data.length == 0) {
		return null;
	} else {
		return data;
	}
}
// 刷新列表
function queryagreement() {
	var postdata;
	$.ajax({
		url : pageurl,
		type : 'GET',
		dataType : "json",
		data : postdata + "&limit=" + pagesize,
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#table-agreement').bootstrapTable('load', data);
		}
	});
}
var deptsetting = {
	async : {
		enable : true,
		url : "../provider/querydepttree",
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
		onCheck : deptTreeOnCheck
	}
};

function queryProSetting(){
	/*async : {
		enable : true,
		url : "../provider/queryprotree",
		autoParam : [ "id" ],
		dataType : "json",
		type : "post"
	},
	view: {
		fontCss: function (treeId, treeNode) {
			return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
		},
		expandSpeed: ""
	},
	check : {
		enable : true,
		chkStyle : "radio",
		radioType : "all"
	},
	callback : {
		onCheck : queryProTreeOnCheck
	}*/
	$.ajax({
		url : "../provider/queryallprotree",
		type : 'POST',
		dataType : "json",
		error : function() {
			$("#queryProTree").html('加载失败，<a href="javascrpt:void(0);" onclick="queryProSetting();">点击这里</a>重试。');
		},
		success : function(data) {
			$.fn.zTree.init($("#queryProTree"), {
				data : {
					simpleData: {
						enable: true,
						idKey: "id",
						pIdKey: "pId",
						rootPId: ""
					}
				},
				view: {
					fontCss: function (treeId, treeNode) {
						return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
					},
					expandSpeed: ""
				},
				check : {
					enable : true,
					chkStyle : "radio",
					radioType : "all"
				},
				callback : {
					onCheck : queryProTreeOnCheck
				}
			}, data);
		}
	});
};

function queryProTreeOnCheck(event, treeId, treeNode) {
	$("#queryproviderid").val(treeNode.id);
	$("#queryprovidername").val(treeNode.name);
	$('#showQueryPro').modal("hide");
}

function deptTreeOnCheck(event, treeId, treeNode) {
	$("#deptid").val(treeNode.prvcode);
	$("#deptname").val(treeNode.name);
	$('#showdept').modal("hide");
}

// function addagreement(){
// location.href = "addagreementdetail";
// }
function addagreement_agreement() {
	$("#defultclose").attr("class","btn");
	$("#collapseOne").attr("class","panel-body collapse in");
	alertmsg("请添加新协议内容!");
	showdiv('jbxx');
	$('#agreementid').val("");
	$('#agreementcode').val("");
	$('#agreementname').val("");
	$('#deptid').val($('#comcode').val());
	$('#deptname').val($('#comname').val());
//	getagreementtrule();
	initArea();
	$('#providerid').val("");
	$('#providename').val("");
	$('#agreementtruleid').val("");
	$('#agreementtrulename').val("");

	$('#agreementtrule').val("");
	$('#yyfwdiv').find('select').val('');
	$('#yyfwdiv').find('table tbody').html("");
	$('#cdwddiv').find('select').val('');
	$('#cdwddiv').find('table tbody').html("");

	$('#dbt1').val('');
	$('#dbt2').val('');
	$("input[name = 'distritype']:checkbox").attr('checked',false);
	$('#psfsdiv').find('select').val('0');
	$('#psfsdiv').find('textarea').val('');
	$("input[name = 'distpaytype_2']:radio").attr('checked',false);
	$('#chargefee_2').val('');
}
function flushReset(){ 
	$.ajax({
		url : 'updateagreementsupplements',
		type : 'POST',
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function() {
			alertmsg("刷新成功!");
		}
	});
}
function updateagreement(addagreeid) {
	if (addagreeid == null || "" == addagreeid) {
		alertmsg("请选择!");
		return;
	}
	$.ajax({
		url : 'updateagreementdetail',
		type : 'GET',
		dataType : "json",
		data : {
			agreementid : addagreeid
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#agreementid').val(data.agreement.id);
            $('#renewalagreementid').val(data.agreement.id);
			$('#agreementcode').val(data.agreement.agreementcode);
			$('#agreementname').val(data.agreement.agreementname);
			$('#deptid').val(data.agreement.deptid);
			$('#deptname').val(data.deptname);
			$('#providerid').val(data.agreement.providerid);
			$('#providename').val(data.providername);
			$('#agreementtruleid').val(data.agreement.agreementtrule);
			$('#agreementtrulename').val(data.agreementrulename);
			$('#agreementrule').val(data.agreement.agreementrule);
			$('#agreementType').val(data.agreement.agreementType);//协议类型
			showdiv('jbxx');
//			var tradition_data = data.ruleList2;
//			var opt = "";
//			for (var i = 0; i < tradition_data.length; i++) {
//				if (tradition_data[i].ck == "1") {
//					opt = opt + '<option value="'
//							+ tradition_data[i].rule_engine_id + '">'
//							+ tradition_data[i].rule_base_postil + '</option>';
//				}
//			}
//			for (var i = 0; i < tradition_data.length; i++) {
//				if (tradition_data[i].ck == "0") {
//					opt = opt + '<option value="'
//							+ tradition_data[i].rule_engine_id + '">'
//							+ tradition_data[i].rule_base_postil + '</option>';
//				}
//			}

			// 每次选择供应商 清除下拉框信息
			$("#agreementtrule").children().remove();

			//$("#agreementtrule").append(opt);

			if (data.agreement.agreementrule == '1') {
				$("#agreementrule").val('1');
			}
			if (data.agreement.agreementrule == '0') {
				$("#agreementrule").val('0');
			}
		}
	});
	// location.href = "updateagreementdetail?agreementid="+addagreeid;
}

function deletedagreement() {
	var selectedObj = getSelectedRow();
	if (selectedObj == null || selectedObj == "") {
		alertmsg("请选择协议！");
	}
	if(selectedObj.length>1){
		alertmsg("只能选择一条协议删除");
		return;
	}
	var postdata = selectedObj[0].id;
	$.ajax({
		url : "deleteagreement",
		type : 'POST',
		dataType : "json",
		data : "id=" + postdata,
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			alertmsg("删除成功！");
			query();
		}
	});
}

function startagreement() {
	var selectedObj = getSelectedRow();
	if (selectedObj == null || selectedObj == "") {
		alertmsg("请选择！");
	}
	var flag = 0;
	var ids ="";
	//异步刷新控制标记
	var start = 0;
	var end = 0;
	//遍历所有选择的状态
	for(var i=0;i<selectedObj.length;i++){
		var status = selectedObj[i].agreementstatus;
		if(status == "已生效"){
			flag += 1;
		}
	}
	if(flag>0){
		alertmsg("选项含有已生效协议，操作不成功，请重新选择");
		return;
	}
	//复数请求
	for(var i=0;i<selectedObj.length;i++){
		start +=1;
		var postdata = selectedObj[i].id;
		$.ajax({
			url : "startorendagreement",
			type : 'POST',
			dataType : "json",
			data : {
				id : postdata,
				agreementstatus : "1"
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
				return;
			},
			success : function(data) {
				end +=1;
				if(start == end){
					query();
				}
			}
		});
	}
	alertmsg("启动成功！");
}
function setUnderwritestatus(underwritestatus) {
	var selectedObj = getSelectedRow();
	if (!selectedObj || selectedObj.length <1) {
		alertmsg("请选择协议！");
	}
	var ids ="";
	for(var i=0;i<selectedObj.length;i++){
		ids += selectedObj[i].id + ",";
	}

	$.ajax({
		url : "setUnderwritestatus",
		type : 'POST',
		//dataType : "json",
		data : {
			ids : ids,
			underwritestatus : underwritestatus
		},
		error : function() {
			alertmsg("Connection error");
			return;
		},
		success : function(data) {
			if (data) {
				if (underwritestatus) {
					alertmsg("核保功能开启成功！");
				} else {
					alertmsg("核保功能关闭成功！");
				}
				query();
			}
		}
	});

}
function endagreement() {
	var selectedObj = getSelectedRow();
	if (selectedObj == null || selectedObj == "") {
		alertmsg("请选择！");
	}
	var flag = 0;
	var ids ="";
	//异步刷新控制标记
	var start = 0;
	var end = 0;
	//遍历所有选择的状态
	for(var i=0;i<selectedObj.length;i++){
		var status = selectedObj[i].agreementstatus;
		if(status == "未生效"){
			flag += 1;
		}
	}
	if(flag>0){
		alertmsg("选项含有未生效协议，操作不成功，请重新选择");
		return;
	}
	//复数请求
	for(var i=0;i<selectedObj.length;i++){
		start += 1;
		var postdata = selectedObj[i].id;
		$.ajax({
			url : "startorendagreement",
			type : 'POST',
			dataType : "json",
			data : {
				id : postdata,
				agreementstatus : "0"
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
				return;
			},
			success : function(data) {
				end +=1;
				if(start == end){
					query();
				}
			}
		});
	}
	alertmsg("关闭成功！");
}

function initArea() {
	getAreaByParentid("0", $("#province"));
}
function changeprv() {
	getAreaByParentid($("#province").val(), $("#city"));
}
function changecity() {
	getAreaByParentid($("#city").val(), $("#county"));
}

function getAreaByParentid(parentid, selectobject) {
	selectobject.empty();
	$.ajax({
		url : "../region/getregionsbyparentid",
		type : 'GET',
		dataType : "json",
		data : {
			parentid : parentid
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			selectobject.empty();
			for (var i = 0; i < data.length; i++) {
				selectobject.append("<option value='" + data[i].id + "'>"
						+ data[i].comcodename + "</option>");
			}
			if (data.length > 0) {
				selectobject.get(0).selectedIndex = 0;
			}
			selectobject.trigger("change");
		}
	});
}

function query() {
	var up = "";
	var me = "";
	var down = "";
	var comcode = $("#comcode").val();
	var queryagreementcode = $("#queryagreementcode").val();
	var queryagreementname = $("#queryagreementname").val();

	var citys="";
	$("input[name^='querycity']:checked").each(function(){
		var a = $(this).val();
		if (a!==null&&a!=="") {
			citys += "city:" + a;
		}
	});

	if (comcode == "") {
		return;
	}
	if ($('input[id="shang_input"]:checked').prop("checked") == true) {
		up += 1;
	} else {
		up += 0;
	}
	if ($('input[id="ben_input"]:checked').prop("checked") == true) {
		me += 1;
	} else {
		me += 0;
	}
	if ($('input[id="xia_input"]:checked').prop("checked") == true) {
		down += 1;
	} else {
		down += 0;
	}
	var agreementcode = up + me + down;
	if (agreementcode == "000") {
		alertmsg("请至少选择一种协议类型！");
		return;
	}
	$("#table-agreement").bootstrapTable('refresh');
	/*$('#table-agreement').bootstrapTable(
		'refresh',
		{url:'getagreementbycomcode'});*/

	/*$.ajax({
		url : "getagreementbycomcode",
		type : 'GET',
		dataType : "json",
		data : {
			agreementcode : agreementcode,
			comcode : comcode,
			queryagreementcode : queryagreementcode,
			queryagreementname : queryagreementname,
			providerid:$("#queryproviderid").val(),
			citys : citys,
			// offset:0,
			limit : pagesize
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#table-agreement').bootstrapTable('load', data);
		}
	});*/
}

function queryAgreement() {
	query();
}

function queryReset() {
	$("#queryproviderid").val('');
	$("#queryprovidername").val('');
	$("#queryagreementcode").val('');
	$("#queryagreementname").val('');
	getAreaByParentid("0", $("#provinceadd1"));
}

function queryParams(params) {
	var up = "";
	var me = "";
	var down = "";
	if ($('input[id="shang_input"]:checked').prop("checked") == true) {
		up += 1;
	} else {
		up += 0;
	}
	if ($('input[id="ben_input"]:checked').prop("checked") == true) {
		me += 1;
	} else {
		me += 0;
	}
	if ($('input[id="xia_input"]:checked').prop("checked") == true) {
		down += 1;
	} else {
		down += 0;
	}
	var comcode = $("#comcode").val();
	if (comcode == "") {
		comcode = "12";
	}
	var agreementcode = up + me + down;

	var queryagreementcode = $("#queryagreementcode").val();
	var queryagreementname = $("#queryagreementname").val();

	var citys="";
	$("input[name^='querycity']:checked").each(function(){
		var a = $(this).val();
		if (a!==null&&a!=="") {
			citys += "city:" + a;
		}
	});

	return {
		queryagreementcode : queryagreementcode,
		queryagreementname : queryagreementname,
		providerid:$("#queryproviderid").val(),
		citys : citys,
		agreementcode : agreementcode,
		comcode : comcode,
		agreementtype : $("#agreementtype").val(),
		offset : params.offset,
		limit : params.limit
	};

}

function query1() {
	$('#table-agreement').bootstrapTable('refresh', {
		url : 'getagreementbycomcode'
	});
}
function getdatafromid(event, treeId, treeNode) {
	var id = treeNode.id;
	$("#deldept").show();
	$.ajax({
		type : "POST",
		url : "../dept/querybyid",
		data : "id=" + id,
		dataType : "json",
		success : function(datainfo) {
			$("#id").val(datainfo.orgobject.id);
			$("#upcomcode").val(datainfo.orgobject.upcomcode);
			$("#comcode").val(datainfo.orgobject.comcode);
			$("#deptinnercode").val(datainfo.orgobject.deptinnercode);
			$("#comname").val(datainfo.orgobject.comname);
			$("#comtype").val(datainfo.orgobject.comtype);
			$("#comgrade").val(datainfo.orgobject.comgrade);
			$("#rearcomcode").val(datainfo.orgobject.rearcomcode);
			$("#province").val(datainfo.orgobject.province);
			$("#city").val(datainfo.orgobject.city);
			$("#county").val(datainfo.orgobject.county);
			$("#address").val(datainfo.orgobject.address);
			$("#webaddress").val(datainfo.orgobject.webaddress);
			$("#zipcode").val(datainfo.orgobject.zipcode);
			$("#phone").val(datainfo.orgobject.phone);
			$("#fax").val(datainfo.orgobject.fax);
			$("#email").val(datainfo.orgobject.email);
			$("#satrapname").val(datainfo.orgobject.satrapname);
			$("#satrapphone").val(datainfo.orgobject.satrapphone);
			$("#satrapcode").val(datainfo.orgobject.satrapcode);
			query()
		}
	});
}

/* zTree初始化数据 */
function inittree() {
	$.ajax({
		url : "inscdeptlist",
		type : 'POST',
		dataType : "json",
		error : function() {
			$("#deptTree").html('加载失败，<a href="javascrpt:void(0);" onclick="inittree();">点击这里</a>重试。');
		},
		success : function(data) {
			$.fn.zTree.init($("#deptTree"), {
				data : {
					simpleData: {
						enable: true,
						idKey: "id",
						pIdKey: "pId",
						rootPId: "1200000000"
					}
				},
				view: {
					fontCss: function (treeId, treeNode) {
						return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
					},
					expandSpeed: ""
				},
				callback : {
					onClick : getdatafromid
				}
			}, data);
		}
	});

}
function operateFormatter1(value, row, index) {
	return [ '<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
			'<i class="glyphicon glyphicon-edit"></i>', '</a>'].join('');
}
// 事件相应
var tm=null;
window.operateEvents1 = {
	'click .edit' : function(e, value, row, index) {
		$("#defultclose").attr("class","btn");
		$("#collapseOne").attr("class","panel-body collapse in");
		updateagreement(row.id);
		initArea();
		$('#table-agreement input[name="btSelectItem"]').prop('checked',false);
		if(tm){
				clearTimeout(tm)}
		tm=setTimeout(function(){
			$('#table-agreement input[data-index='+index+']').prop('checked',true);
		},50)
	}
};
function getagreementtrule() {
	var dept_id = $("#deptid").val();
		//根据供应商过滤规则信息
		$.ajax({
			type:'post',
			url:'initrulebyprovider',
			data:({deptId:dept_id}),
			datatype:'json',
			success:function(data){
//				var dispatch_data = data.result4;
				var tradition_data = data.result2;
//				var opd="";
				var opt="";
//				for(var i=0;i<dispatch_data.length;i++){
//					opd =opd+ '<option value="'+dispatch_data[i].rule_engine_id+'">'+dispatch_data[i].rule_base_postil+'</option>';
//				}

				for(var i=0;i<tradition_data.length;i++){
					opt =opt+'<option value="'+tradition_data[i].rule_engine_id+'">'+tradition_data[i].rule_base_postil+'</option>';
				}

				//每次选择供应商 清除下拉框信息
//				$("#agreementdrule").children().remove();
				$("#agreementtrule").children().remove();

//				$("#agreementdrule").append(opd);
				$("#agreementtrule").append(opt);
			}
		})


}