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
		"multiselect"],
		function($) {

			// 数据初始化
			$(function() {
				$('#table-fw').bootstrapTable({
					method : 'get',
					url : "",
					clickToSelect : true,
				columns : [{
						field : 'state',
						align : 'center',
						valign : 'middle',
						checkbox : true
					}, {
						field : 'city',
						title : '市区',
						align : 'center',
						valign : 'middle',
						sortable : true
					}, {
						field : 'deptid',
						title : '网点',
						align : 'center',
						valign : 'middle',
						sortable : true
					}]
				});
				$('#table-wd').bootstrapTable({
					method : 'get',
					url : "",
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
						field : 'deptid1',
						title : '集团',
						align : 'center',
						valign : 'middle',
						sortable : true
					}, {
						field : 'deptid2',
						title : '平台公司',
						align : 'center',
						valign : 'middle',
						sortable : true
					}, {
						field : 'deptid3',
						title : '法人公司',
						align : 'center',
						valign : 'middle',
						sortable : true
					}, {
						field : 'deptid4',
						title : '分公司',
						align : 'center',
						align : 'center',
						valign : 'middle',
						sortable : true
					}, {
						field : 'deptid5',
						title : '网点',
						align : 'center',
						valign : 'middle',
						sortable : true
					}, {
						field : 'scaleflag',
						title : '优先级',
						align : 'center',
						valign : 'middle',
						sortable : true
					}, {
						field : 'permflag',
						title : '设置权限',
						align : 'center',
						valign : 'middle',
						sortable : true
					}, {
						field : 'notdeductible',
						title : '设置',
						align : 'center',
						valign : 'middle',
						clickToSelect : false,
						formatter : operateFormatter,
						events : operateEvents
					} ]
				});
				$('#table-trule').bootstrapTable({
					method : 'get',
					url : "",
					clickToSelect : true,
					columns : [ {
						field : 'state',
						align : 'center',
						valign : 'middle',
						checkbox : true
					}, {
						field : 'rule_engine_id',
						title : '规则id',
						align : 'center',
						valign : 'middle',
						sortable : true
					}, {
						field : 'rule_base_postil',
						title : '规则',
						align : 'center',
						valign : 'middle',
						sortable : true
					}, {
						field : 'operating',
						title : '选择',
						align : 'center',
						valign : 'middle',
						clickToSelect : false,
						formatter : operateFormattertrule,
						events : operateEventstrule
					} ]
				});
				// 选择供应商
				$("#providename").on("click", function(e) {
					$('#showpic').modal();
					$('#treeDemosearch').val('');
					initprotree();
				});
				
				$("#deptname").on("click", function(e) {
					$('#showdept').modal();
					$.ajax({
						url : "querydepttree",
						type : 'POST',
						dataType : "json",
						error : function() {
							$("#depttree").html('加载失败，<a href="javascrpt:void(0);" onclick="inittree();">点击这里</a>重试。');
						},
						success : function(data) {
							$.fn.zTree.init($("#depttree"), {
								data : {
									simpleData: {
										enable: true,
										idKey: "id",
										pIdKey: "pId",
										rootPId: "1200000000"
									}
								},
								view: {
									expandSpeed: ""
								},
								check : {
									enable : true,
									chkStyle : "radio",
									radioType : "all"
								},
								callback : {
									onCheck : deptTreeOnCheck
								}
							}, data);
						}
					});
				});
				$("#agreementtrulename").on("click", function(e) {
					$('#showtrule').modal();
					$('#deptnamequery').val($('#deptname').val());
				});
//				$("#deptname2").on("click", function(e) {
//					$('#showdept').modal();
//					$.fn.zTree.init($("#depttree"), deptsetting2);
//				});

                $('.js-multiselect').multiselect({
                    right: '#js_multiselect_to_1',
                    rightAll: '#js_right_All_1',
                    beforeMoveToRight:function($left, $right, option){
                        return true;
                    },
                    rightSelected: '#js_right_Selected_1',
                    leftSelected: '#js_left_Selected_1',
                    leftAll: '#js_left_All_1'
                });

				$("#yyfw").on("click", function(e) {
					var addagreeid=$("#agreementid").val();
					if(addagreeid==null||""==addagreeid){
						alertmsg("请先选择需要修改的协议!");
						return;
					}
					querydeptconn();
				});
				$("#cdwd").on("click", function(e) {
					var addagreeid=$("#agreementid").val();
					if(addagreeid==null||""==addagreeid){
						alertmsg("请先选择需要修改的协议!");
						return;
					}
					querydept();
				});
				$("#psfs").on("click", function(e) {
					var addagreeid=$("#agreementid").val();
					if(addagreeid==null||""==addagreeid){
						alertmsg("请先选择需要修改的协议!");
						return;
					}
					initdbt();
				});
                $("#ksxb").on("click", function(e) {
                    var renewalagreementid=$("#renewalagreementid").val();
                    if(renewalagreementid==null||""==renewalagreementid){
                        alertmsg("请先选择需要修改的协议!");
                        return;
                    }
                    initRenewalItems(renewalagreementid);
                });
				
				//预付选中  输入框可编辑
				$('input[name="distpaytype_2"]').change(
					function(){
						var va = $('input[name="distpaytype_2"]:checked').val();
						if(va == '3'){
							$("#chargefee_2").removeAttr("readonly");
						}else{
							$("#chargefee_2").attr("readonly","readonly");
						}
					}	
				);
				
				$("#agreementform").validate({
					errorLabelContainer : "#agreementerror",
					errorElement : "p",
					errorClass : "text-left",
					rules : {
						agreementcode : {
							required : true,
							minlength : 5,
							maxlength : 20
						},
						agreementname : {
							required : true,
							maxlength : 30,
							minlength : 0
						},
						providename : {
							required : true,
							minlength : 0
						},
						deptname : {
							required : true,
							minlength : 0
						}
					},
					messages : {
						agreementcode : {
							required : "协议编码不能为空！",
							minlength : "协议编码长度需大于5个字！",
							maxlength : "协议编码长度不得大于20个字！"
						},
						agreementname : {
							required : "协议名称不能为空！",
							maxlength : "协议名称长度不得大于30个字！"
						},
						providename : {
							required : "请选择供应商！"
						},
						deptname : {
							required : "请选择关联机构！"
						}

					},
					highlight : validateHighlight,
					success : validateSuccess
				});
//				initArea();
				getAreaByParentid("0", $("#provinceadd1"));
				deptSelect(0);
				deptconnSelect(0);
//				querydept();
//				querydeptconn();
				initpcm();
//				initdbt();

				$("#deptname1").on("click", function(e) {
					$('#showdept1').modal();
					$.ajax({
						url : "querydepttree",
						type : 'POST',
						dataType : "json",
						error : function() {
							$("#depttree1").html('加载失败，<a href="javascrpt:void(0);" onclick="inittree();">点击这里</a>重试。');
						},
						success : function(data) {
							$.fn.zTree.init($("#depttree1"), {
								data : {
									simpleData: {
										enable: true,
										idKey: "id",
										pIdKey: "pId",
										rootPId: "1200000000"
									}
								},
								view: {
									expandSpeed: ""
								},
								check : {
									enable : true,
									chkStyle : "radio",
									radioType : "all"
								},
								callback : {
									onCheck : querydeptTreeOnCheck
								}
							}, data);
						}
					});
				});
			});
		});

function operateFormatter(value, row, index) {
	return [ '<a class="edit ml10" href="javascript:void(0)" title="Edit">',
			'<i class="glyphicon glyphicon-edit"></i>', '</a>' ].join('');
}

window.operateEvents = {
	'click .edit' : function(e, value, row, index) {
		$('#showoutdept').modal("show");
		$('#outdeptid').val(row.id);
		$('#scaleflag').val(row.scaleflag);
		$('#permflag').val(row.permflag);
	}
};

function operateFormattertrule(value, row, index) {
    return [
		'<button id="selectcarmodel" class="btn btn-default" data-dismiss="modal" name="selectcarmodel">选择</button>'
    ].join('');
}

window.operateEventstrule = {
	    'click #selectcarmodel': function (e, value, row, index) {
	    	$("#agreementtrulename").val(row.rule_base_postil);
	    	$("#agreementtruleid").val(row.rule_engine_id);
	    }
	}
var deptsetting2 = {
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
		onCheck : deptTreeOnCheck2
	}
};
var setting = {
	/*async : {
		enable : true,
		url : "../provider/queryallprotree",
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
	},*/

};
function initprotree(){
	$.ajax({
		url : "../provider/queryallprotree",
		type : 'POST',
		dataType : "json",
		error : function() {
			$("#treeDemo").html('加载失败，<a href="javascrpt:void(0);" onclick="initprotree();">点击这里</a>重试。');
		},
		success : function(data) {
			$.fn.zTree.init($("#treeDemo"), {
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
					onCheck : zTreeOnCheck
				}
			}, data);
		}
	});
}
function zTreeOnCheck(event, treeId, treeNode) {
	$("#providerid").val(treeNode.id);
	$("#providename").val(treeNode.name);
	$('#showpic').modal("hide");

	//初始化规则下拉框
	var provider_id = $("#providerid").val();


}
function deptTreeOnCheck(event, treeId, treeNode) {
	$("#deptid").val(treeNode.id);
	$("#deptname").val(treeNode.name);
	$('#showdept').modal("hide");
}
function deptTreeOnCheck2(event, treeId, treeNode) {
	//$("#deptid2").val(treeNode.id);
	$("#deptname2").val(treeNode.name);
	$('#showdept').modal("hide");
}

function querydeptTreeOnCheck(event, treeId, treeNode) {
	$("#deptid1").val(treeNode.id);
	$("#deptname1").val(treeNode.name);
	$('#showdept1').modal("hide");
}

function showdiv(shortdivname) {
	if (shortdivname=='yyfw') {
		$.ajax({
			url : "getzonebyagreementid",
			type : 'POST',
			dataType : "json",
			data : {
				agreementid: $('#agreementid').val()
			},
			async : true,
			success : function(data) {
				if ($("#citysguanlian").length==0) {
					showzone(data.body);
				}else{
					$("#citysguanlian").remove();
					showzone(data.body);
				}
			}
		});
	}
	$('#jbxxdiv').hide();
	$('#yyfwdiv').hide();
	$('#cdwddiv').hide();
	$('#psfsdiv').hide();
    $('#ksxbdiv').hide();
	$("#jbxx").removeClass();
	$("#yyfw").removeClass();
	$("#cdwd").removeClass();
	$("#psfs").removeClass();
    $("#ksxb").removeClass();
	$("#" + shortdivname).addClass("active");
	$("#" + shortdivname + "div").show();
}

function showzone(data){
	if(data.length ==0){
		return;
	}
	$("#provinceadd").val(data[0].comcode);
	changcityadd('scope', data[0].city);
}
// 地区初始化 start
function initArea() {
	getAreaByParentid("0", $("#province"));
	getAreaByParentid("0", $("#provinceadd"));
}
function changeprv() {
	getAreaByParentid($("#province").val(), $("#city"));
}
function changecity() {
	getAreaByParentid($("#city").val(), $("#county"));
}
function changprovinceadd() {
	getAreaByParentid($("#provinceadd").val(), $("#cityadd"));
}

function getAreaByParentid(parentid, selectobject) {
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
				selectobject.append("<option value='"+ data[i].id + "'>"+data[i].comcodename + "</option>");
			}
			selectobject.trigger("change");
		}
	});
}
// 地区初始化 end

// 协议基本信息部分 start
function agreementadd() {
	if ($('#agreementcode').val()=="") {
		alertmsg("协议编码不能为空！");
		return;
	}
	if ( $('#agreementname').val()=="") {
		alertmsg("协议名称不能为空！");
		return;
	}
	if ( $('#deptid').val()=="") {
		alertmsg("关联机构不能为空！");
		return;
	}
	if ( $('#providerid').val()=="") {
		alertmsg("供应商不能为空！");
		return;
	}

    $.ajax({
        url : "saveorupdateagreement",
        type : 'POST',
        dataType : "json",
        data : {
            id : $('#agreementid').val(),
            agreementcode : $('#agreementcode').val(),
            agreementname : $('#agreementname').val(),
            deptid : $('#deptid').val(),
            providerid : $('#providerid').val(),
            agreementtrule : $('#agreementtruleid').val(),
            agreementrule : $('#agreementrule').val(),
            agreementdrule : $('#agreementdrule').val(),
			agreementType : $('#agreementType').val(),
            agreementstatus : "0"
        },
        async : true,
        error : function() {
            alertmsg("Connection error");
        },
        success : function(data) {
            if (data.id=="") {
                alertmsg("协议编码已存在，请重新修改！");
                return;
            }
            alertmsg("保存成功");
            $('#agreementid').val(data.id);
        }
    });
}
function back() {
	location.href = "list";
}
// 协议基本信息部分 end

// 覆盖区域 start
function savescop() {
	if ($('#agreementid').val() == null || "" == $('#agreementid').val()) {
		alertmsg("请先保存协议！");
		return;
	}
	$.ajax({
		url : "savescope",
		type : 'POST',
		dataType : "json",
		data :
		{
			agreementid : $('#agreementid').val(),
			/*province : $('#provinceadd').val(),
			city : $('#cityadd').val(),*///覆盖区域功能参数，暂时去掉
			deptid1 : $('#deptidc1').val(),
			deptid2 : $('#deptidc2').val(),
			deptid3 : $('#deptidc3').val(),
			deptid4 : $('#deptidc4').val(),
			deptid5 : $('#deptidc5').val()
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			alertmsg("关联成功");
			querydeptconn();
//			sx();
		}
	});
}
function getSelectedIds() {
	var data = $('#table-fw').bootstrapTable('getSelections');
	if (data.length == 0) {
		return null;
	}
	var ids = new Array();
	for (var i = 0; i < data.length; i++) {
		ids[i] = data[i].id;
	}
	return ids;
}
function deletescope() {
	if (getSelectedIds() == null) {
		alertmsg("请选择!")
		return;
	}
	$.ajax({
		url : "deletescopeids",
		type : 'POST',
		dataType : "json",
		data : {
			ids : getSelectedIds().join(",")
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			alertmsg("保存成功");
			querydeptconn();
		}
	});
}

//覆盖区域 end
//应用范围start

function deptconnchange(numb) {
	if (numb > 4) {
		return;
	}
	var partid = "deptidc" + numb;
	var nextid = "deptidc" + (numb + 1);
	$("#" + nextid).empty();
	$("#" + nextid).append("<option value=''>请选择</option>");
	deptconnSelect(numb);
}
function deptconnSelect(numb) {
	var sVal = $("#deptidc" + numb).val();
	if (numb == 0) {
		sVal = "";
	} else if ($("#deptidc" + numb).val() == "") {
		deptconnchange(numb + 1);
		return;
	}
	$.ajax({
		url : "inscdeptlist",
		type : 'POST',
		dataType : "json",
		data : {
			root : sVal
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {


			for (var int = 0; int < data.length; int++) {
				$("#" + "deptidc" + (numb+1)).append(
						"<option value='" + data[int].id + "'>"
								+ data[int].name + "</option>");
			}
			if (data.length == 1) {
				$("#" + "deptidc" + (numb + 1)).val(data[0].id);
			}
			deptconnchange(numb+1);
		}
	});
}

function querydeptconn() {
	if ($('#agreementid').val() == null || $('#agreementid').val() == "") {
		return;
	}
	$.ajax({
		url : "listscopedept",
		type : 'GET',
		dataType : "json",
		data : {
			agreementid : $('#agreementid').val()
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#table-fw').bootstrapTable('load', data);
		}
	});
}

// 出单网点 start
function deptchange(numb) {
	if (numb > 4) {
		return;
	}
	var partid = "deptids" + numb;
	var nextid = "deptids" + (numb + 1);
	$("#" + nextid).empty();
	$("#" + nextid).append("<option value=''>请选择</option>");
	deptSelect(numb);
}
function deptSelect(numb) {
	var sVal = $("#deptids" + numb).val();
	if (numb == 0) {
		sVal = "";
	} else if ($("#deptids" + numb).val() == "") {
		deptchange(numb + 1);
		return;
	}
	$.ajax({
		url : "inscdeptlist",
		type : 'POST',
		dataType : "json",
		data : {
			root : sVal
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			for (var int = 0; int < data.length; int++) {
				$("#" + "deptids" + (numb + 1)).append(
						"<option value='" + data[int].id + "'>"
								+ data[int].name + "</option>");
			}
			if (data.length == 1) {
				$("#" + "deptids" + (numb + 1)).val(data[0].id);
			}
			deptchange(numb + 1);
		}
	});
}
function addoutorderdept() {
	if ($('#agreementid').val() == null || $('#agreementid').val() == "") {
		alertmsg("请先保存协议！");
		return;
	}
	$.ajax({
		url : "saveorupdateoutdept",
		type : 'POST',
		dataType : "json",
		data : {
			agreementid : $('#agreementid').val(),
			deptid1 : $('#deptids1').val(),
			deptid2 : $('#deptids2').val(),
			deptid3 : $('#deptids3').val(),
			deptid4 : $('#deptids4').val(),
			deptid5 : $('#deptids5').val(),
			scaleflag : "1",
			permflag : "5",
			underwritinginter : $('#underwriting').val()
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			alertmsg("保存成功");
			querydept();
		}
	});
}
function getSelectedRowIds() {
	var data = $('#table-wd').bootstrapTable('getSelections');
	if (data.length == 0) {
		return null;
	}
	var ids = new Array();
	for (var i = 0; i < data.length; i++) {
		ids[i] = data[i].id;
	}
	return ids;
}
function deleteoutdept() {
	if (getSelectedRowIds() == null) {
		alertmsg("请选择!");
		return;
	}
	$.ajax({
		url : "deleteoutdeptids",
		type : 'POST',
		dataType : "json",
		data : {
			ids : getSelectedRowIds().join(",")
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			alertmsg("保存成功");
			querydept();
		}
	});
}
function querydept() {
	if ($('#agreementid').val() == null || $('#agreementid').val() == "") {
		return;
	}
	$.ajax({
		url : "listoutdept",
		type : 'GET',
		dataType : "json",
		data : {
			agreementid : $('#agreementid').val()
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#table-wd').bootstrapTable('load', data);
		}
	});
}

function updateoutdept() {
	var scaleflag = $('#scaleflag').val();
	var permflag =$('#permflag').val();
	var re = /^[0-9]*[1-9][0-9]*$/;
	if (!re.test(scaleflag)) {
		alertmsg("优先级请输入正整数！");
		return;
	}
	if (!re.test(permflag)) {
		alertmsg("设置权限请输入正整数！");
		return;
	}
	$.ajax({
		url : "updateoutdept",
		type : 'POST',
		dataType : "json",
		data : {
			id : $('#outdeptid').val(),
			scaleflag : $('#scaleflag').val(),
			permflag : $('#permflag').val()
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#showoutdept').modal("hide");
			querydept();
		}
	});
}
// 出单网点end

// 支付通道配置 start
function initpcm() {
	var agreementid = $('#agreementid').val();
	if (agreementid != null && agreementid != "") {
		$.ajax({
			url : "initpcm",
			type : 'POST',
			dataType : "json",
			data : {
				agreementid : $('#agreementid').val()
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				showPcm(data);
			}
		});
	}
}
function showPcm(data) {
	var pcm1 = data.pcm1;
	var pcm2 = data.pcm2;
	var pcm3 = data.pcm3;
	var pcm4 = data.pcm4;
	var pcm5 = data.pcm5;
	var pcm6 = data.pcm6;
	if (pcm1 != null) {
		$("#pcm_1").val(pcm1.id);
		$('input[name="collectiontype_1"]:radio[value="' + pcm1.collectiontype + '"]').attr('checked', 'true');
		$('#settlementno_1').val(pcm1.settlementno);
		$('#terraceno_1').val(pcm1.terraceno);
		$('input[name="paytarget_1"]:radio[value="' + pcm1.paytarget + '"]')
				.attr('checked', 'true');
		$('#sort_1').val(pcm1.sort);
		$('#favorabledescribe_1').val(pcm1.favorabledescribe);

		$('input[name="paychannelid"]:checkbox[value="'
						+ pcm1.paychannelid + '"]').attr('checked', 'true');
	}
	if (pcm2 != null) {
		$("#pcm_2").val(pcm2.id);
		$('input[name="collectiontype_2"]:radio[value="'
						+ pcm2.collectiontype + '"]').attr('checked', 'true');
		$('#settlementno_2').val(pcm2.settlementno);
		$('#terraceno_2').val(pcm2.terraceno);
		$('input[name="paytarget_2"]:radio[value="' + pcm2.paytarget + '"]')
				.attr('checked', 'true');
		$('#sort_2').val(pcm2.sort);
		$('#favorabledescribe_2').val(pcm1.favorabledescribe);
		$('input[name="paychannelid"]:checkbox[value="'
						+ pcm2.paychannelid + '"]').attr('checked', 'true');
		$('#deptcode_2').val(pcm2.deptcode);
		$('#collectionname_2').val(pcm2.collectionname);
	}
	if (pcm3 != null) {
		$("#pcm_3").val(pcm3.id);
		$('input[name="collectiontype_3"]:radio[value="'
						+ pcm3.collectiontype + '"]').attr('checked', 'true');
		$('#settlementno_3').val(pcm3.settlementno);
		$('#terraceno_3').val(pcm3.terraceno);
		$('input[name="paytarget_3"]:radio[value="' + pcm3.paytarget + '"]')
				.attr('checked', 'true');
		$('#sort_3').val(pcm3.sort);
		$('#favorabledescribe_3').val(pcm1.favorabledescribe);
		$('input[name="paychannelid"]:checkbox[value="'
						+ pcm3.paychannelid + '"]').attr('checked', 'true');
	}
	if (pcm4 != null) {
		$("#pcm_4").val(pcm4.id);
		$('input[name="collectiontype_4"]:radio[value="'
						+ pcm4.collectiontype + '"]').attr('checked', 'true');
		$('#settlementno_4').val(pcm4.settlementno);
		$('#terraceno_4').val(pcm4.terraceno);
		$('input[name="paytarget_4"]:radio[value="' + pcm4.paytarget + '"]')
				.attr('checked', 'true');
		$('#sort_4').val(pcm4.sort);
		$('#favorabledescribe_4').val(pcm1.favorabledescribe);
		$('input[name="paychannelid"]:checkbox[value="'
						+ pcm4.paychannelid + '"]').attr('checked', 'true');
	}
	if (pcm5 != null) {
		$("#pcm_5").val(pcm5.id);
		$('input[name="collectiontype_5"]:radio[value="'
						+ pcm5.collectiontype + '"]').attr('checked', 'true');
		$('#settlementno_5').val(pcm5.settlementno);
		$('#terraceno_5').val(pcm5.terraceno);
		$('input[name="paytarget_5"]:radio[value="' + pcm5.paytarget + '"]')
				.attr('checked', 'true');
		$('#sort_5').val(pcm5.sort);
		$('#favorabledescribe_5').val(pcm1.favorabledescribe);
		$('input[name="paychannelid"]:checkbox[value="'
						+ pcm5.paychannelid + '"]').attr('checked', 'true');
	}
	if (pcm6 != null) {
		$("#pcm_6").val(pcm6.id);
		$('input[name="collectiontype_6"]:radio[value="'
						+ pcm6.collectiontype + '"]').attr('checked', 'true');
		$('#settlementno_6').val(pcm6.settlementno);
		$('#terraceno_6').val(pcm6.terraceno);
		$('input[name="paytarget_6"]:radio[value="' + pcm6.paytarget + '"]')
				.attr('checked', 'true');
		$('#sort_6').val(pcm6.sort);
		$('#favorabledescribe_6').val(pcm1.favorabledescribe);
		$('input[name="paychannelid"]:checkbox[value="'
						+ pcm6.paychannelid + '"]').attr('checked', 'true');
	}

}
function jqchk(names) {
	var checkvalues = [];
	$('input[name="' + names + '"]:checked').each(function() {
		checkvalues.push($(this).val());
	});
	return checkvalues;
}
function savepcm() {
	if ($('#agreementid').val() == "") {
		alertmsg("请保存协议！");
		return;
	}
	var paychannelids = jqchk("paychannelid").join(",");
	var pcm1 = null;
	var pcm2 = null;
	var pcm3 = null;
	var pcm4 = null;
	var pcm5 = null;
	var pcm6 = null;
	var pcm = [];
	if (paychannelids.indexOf("1") > -1) {
		pcm1 = {
			id : $("#pcm_1").val(),
			agreementid : $('#agreementid').val(),
			collectiontype : $('input[name="collectiontype_1"]:checked').val(),
			settlementno : $('#settlementno_1').val(),
			terraceno : $('#terraceno_1').val(),
			paytarget : $('input[name="paytarget_1"]:checked').val(),
			favorabledescribe : $('#favorabledescribe_1').val(),
			sort : $('#sort_1').val(),
			paychannelid : "1"
		}
		pcm.push(pcm1);
	}
	if (paychannelids.indexOf("2") > -1) {
		pcm2 = {
			id : $("#pcm_2").val(),
			agreementid : $('#agreementid').val(),
			collectiontype : $('input[name="collectiontype_2"]:checked ').val(),
			settlementno : $('#settlementno_2').val(),
			terraceno : $('#terraceno_2').val(),
			paytarget : $('input[name="paytarget_2"]:checked ').val(),
			deptcode : $('#deptcode_2').val(),
			collectionname : $('#collectionname_2').val(),
			favorabledescribe : $('#favorabledescribe_2').val(),
			sort : $('#sort_2').val(),
			paychannelid : "2"
		}
		pcm.push(pcm2);
	}
	if (paychannelids.indexOf("3") > -1) {
		pcm3 = {
			id : $("#pcm_3").val(),
			agreementid : $('#agreementid').val(),
			collectiontype : $('input[name="collectiontype_3"]:checked ').val(),
			paytarget : $('input[name="paytarget_3"]:checked ').val(),
			favorabledescribe : $('#favorabledescribe_3').val(),
			sort : $('#sort_3').val(),
			paychannelid : "3"
		}
		pcm.push(pcm3);
	}
	if (paychannelids.indexOf("4") > -1) {
		pcm4 = {
			id : $("#pcm_4").val(),
			agreementid : $('#agreementid').val(),
			collectiontype : $('input[name="collectiontype_4"]:checked ').val(),
			paytarget : $('input[name="paytarget_4"]:checked ').val(),
			favorabledescribe : $('#favorabledescribe_4').val(),
			sort : $('#sort_4').val(),
			paychannelid : "4"
		}
		pcm.push(pcm4);
	}
	if (paychannelids.indexOf("5") > -1) {
		pcm5 = {
			id : $("#pcm_5").val(),
			agreementid : $('#agreementid').val(),
			collectiontype : $('input[name="collectiontype_5"]:checked ').val(),
			paytarget : $('input[name="paytarget_5"]:checked ').val(),
			favorabledescribe : $('#favorabledescribe_5').val(),
			sort : $('#sort_5').val(),
			paychannelid : "5"
		}
		pcm.push(pcm5);
	}
	if (paychannelids.indexOf("6") > -1) {
		pcm6 = {
			id : $("#pcm_6").val(),
			agreementid : $('#agreementid').val(),
			collectiontype : $('input[name="collectiontype_6"]:checked ').val(),
			settlementno : $('#settlementno_6').val(),
			terraceno : $('#terraceno_6').val(),
			paytarget : $('input[name="paytarget_6"]:checked ').val(),
			sort : $('#sort_6').val(),
			favorabledescribe : $('#favorabledescribe_6').val(),
			paychannelid : "6"
		}
		pcm.push(pcm6);
	}
	if (pcm.length == 0) {
		alertmsg("请选择！");
		return;
	}
	var pcmstr = JSON.stringify(pcm);
	$.ajax({
		url : "savepcm",
		type : 'POST',
		dataType : "json",
		data : {
			voStr : pcmstr
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			alertmsg("保存成功");
		}
	});
}

// 支付通道配置 end

// 配送方式 start
function initdbt() {
	if ($('#agreementid').val() == "") {
		return;
	}
	$.ajax({
		url : "initdbt",
		type : 'POST',
		dataType : "json",
		data : {
			agreementid : $('#agreementid').val()
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			showdbt(data);
		}
	});
}
function showdbt(data) {
    $('input[name="distritype"]').each(function(){
        $(this).prop("checked", false);
    });
    $('input[name="distpaytype_2"]').each(function() {
        $(this).prop("checked", false);
    });
    $('#dbt1').val("");
    $('#noti_1').val("");
    $('#dbt2').val("");
    $('#noti_2').val("");
    //$('#distrcompany_2').val("0");
    $('#distrcompany_2').val("7");//默认顺丰快递
    $('#chargefee_2').val("");
    $('#dbt3').val("");

	for (var i = 0; i < data.length; i++) {
		var dbt = data[i];

		$('input[name="distritype"][value="' + dbt.distritype + '"]').prop('checked', true);

		if ("1" == dbt.distritype) {//自取
			$('#dbt1').val(dbt.id);
			$('#noti_1').val(dbt.noti);
			$('input[id="paperPolicy"]').prop('checked', true);//勾选纸质保单
		} else if("2" == dbt.distritype) {//快递
			var distrcompany = $.trim(dbt.distrcompany);//快递公司
			if("0" == distrcompany || distrcompany == "") {//默认顺丰快递
				distrcompany = "7";
			}
			$('input[name="distpaytype_2"][value="'+ dbt.distpaytype + '"]').prop('checked', true);
			$('#dbt2').val(dbt.id);
			$('#noti_2').val(dbt.noti);
			//$('#distrcompany_2').val(dbt.distrcompany);
			$('#distrcompany_2').val(distrcompany);
			$('#chargefee_2').val(dbt.chargefee);
			$('input[id="paperPolicy"]').prop('checked', true);//勾选纸质保单
		} else if("3" == dbt.distritype) {//电子保单
			$('#dbt3').val(dbt.id);
			$('#noti_3').val(dbt.noti);
		}
	}
	/*var platformCode = $("#comcode").val();
	if(platformCode == '1211000000'){
		if($('input[name="distritype"][value="3"]:checked').val() != null){
			$('input[name="distritype"][value="3"]').attr('disabled', 'disabled');
		} else {
			$('input[name="distritype"][value="3"]').removeAttr('disabled');
		}
	} else {
		$('input[name="distritype"][value="3"]').removeAttr('disabled');
	}*/
	//如果勾选了电子保单则将自取与快递去除勾选&设置为不可选
	if ($('input[id="elePolicy"]:checked').val() != null) {
		$('input[name="distritype"][value="1"]').prop('checked', false);
		$('input[name="distritype"][value="2"]').prop('checked', false);
		$('input[name="distritype"][value="1"]').attr('disabled', 'disabled');
		$('input[name="distritype"][value="2"]').attr('disabled', 'disabled');
	} else {
		$('input[name="distritype"][value="1"]').removeAttr('disabled');
		$('input[name="distritype"][value="2"]').removeAttr('disabled');
	}

	//预付选中  输入框可编辑
	var va = $('input[name="distpaytype_2"]:checked').val();
	if(va == '3'){
		$("#chargefee_2").removeAttr("readonly");
	}else{
		$("#chargefee_2").attr("readonly","readonly");
	}
}

function savedbt() {
	if ($('#agreementid').val() == "") {
		alertmsg("请保存协议！");
		return;
	}

	var dbt = [];
	var distritypes = jqchk("distritype").join(",");
	if (distritypes.indexOf("1") > -1) {
		dbt.push({
			id : $('#dbt1').val(),
			agreementid : $('#agreementid').val(),
			noti : $('#noti_1').val(),
			distritype : "1"
		});
	}
	if (distritypes.indexOf("2") > -1) {
		if ($('input[name="distpaytype_2"]:checked').val()==null) {
			alertmsg("请选择收费方式！");
			return ;
		}
		var chargefee=$('#chargefee_2').val();
		var re = /^[0-9].*$/;
		String.prototype.trim=function() {
		    return this.replace(/(^\s*)|(\s*$)/g,'');
		};
		if($('input[name="distpaytype_2"]:checked').val()==3){
			if (chargefee.trim()=="" | !re.test(chargefee.trim()) | chargefee<0) {
				/*alertmsg("预收金额请输入正整数！");*/
				alertmsg("预收金额请输入正数！");
				return;
			}
		}else{
			$('#chargefee_2').val('');
		}
		dbt.push({
			id : $('#dbt2').val(),
			agreementid : $('#agreementid').val(),
			distrcompany : $('#distrcompany_2').val(),
			noti : $('#noti_2').val(),
			chargefee : $('#chargefee_2').val().trim(),
			distpaytype : $('input[name="distpaytype_2"]:checked').val(),
			distritype : "2"
		});
	}
	if (distritypes.indexOf("3") > -1) {
		dbt.push({
			id : $('#dbt3').val(),
			agreementid : $('#agreementid').val(),
			noti : $('#noti_3').val(),
			distritype : "3"
		});
	}
	if (dbt.length == 0) {
		alertmsg("请至少选择一种配送方式！");
		return;
	}

	$.ajax({
		url : "savedbt",
		type : 'POST',
		dataType : "json",
		data : {
			voStr : JSON.stringify(dbt)
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			alertmsg("保存成功");
			initdbt();
		}
	});
}
function money(){
	var chargefee=$('#chargefee_2').val();
	var re = /^[0-9].*$/;
	String.prototype.trim=function() {
	    return this.replace(/(^\s*)|(\s*$)/g,'');
	};
	if (chargefee.trim()=="" | !re.test(chargefee.trim()) | chargefee<0) {
		/*alertmsg("预收金额请输入正整数！");*/
		alertmsg("预收金额请输入正数！");
		return;
	}
}
// 配送方式 end

//快速续保 start
function initRenewalItems(renewalagreementid) {
    $.ajax({
        url : "initrenewalitems",
        type : 'GET',
        dataType : "json",
        data : {
            agreementid: renewalagreementid
        },
        async : true,
        error : function() {
            alertmsg("Connection error");
        },
        success : function(data) {
            if(data.renewalEnable == 1) {
                $("#renewalEnable").prop("checked",true);
            } else {
                $("#renewalEnable").prop("checked",false);
            }

            var selectobject = $("#allRenewalItems");
            selectobject.empty();
            for (var i = 0; i < data.allItems.length; i++) {
                var ismatch = false;
                if(data.configItems) {
                    for (var j = 0; j < data.configItems.length; j++) {
                        if(data.allItems[i].itemcode == data.configItems[j].itemcode) {
                            ismatch = true;
                            break;
                        }
                    }
                }
                if(!ismatch) {
                    selectobject.append("<option value='" + data.allItems[i].itemcode + "'>" + data.allItems[i].itemname + "</option>");
                }
            }

            selectobject = $("#js_multiselect_to_1");
            selectobject.empty();
            if(data.configItems) {
                for (i = 0; i < data.configItems.length; i++) {
                    selectobject.append("<option value='" + data.configItems[i].itemcode + "'>" + data.configItems[i].itemname + "</option>");
                }
            }
        }
    });
}

function saveRenewalItems() {
    if($("#renewalEnable").prop("checked")&&$("select[name=to] option").size() == 0) {
        alertmsg("请添加快速续保查询条件");
        return;
    }

    //防止重复提交
    $(this).prop("disabled", true);
    $("select[id=allRenewalItems] option").attr("selected", false);

    var selectedItems = "";
    if($("#renewalEnable").prop("checked")){
        $("select[id=js_multiselect_to_1] option").each(function(){
            selectedItems += ("&to=" + $(this).val());
        });
    }else{
        selectedItems ="&to=";
    }

    $.ajax({
        url : 'saverenewalitems',
        type : 'POST',
        dataType : 'json',
        data :$("#renewalform").serialize() + selectedItems,
        cache : false,
        async : true,
        error : function() {
            alertmsg("Connection error");
            //防止重复提交
            $("#renewalItemSaveButton").prop("disabled", false);
        },
        success : function(data) {
            if (data.status == "ok") {
                alertmsg("保存成功！");
            }else{
                alertmsg("保存失败！");
            }
            //防止重复提交
            $("#renewalItemSaveButton").prop("disabled", false);
        }
    });
}
//快速续保 end

function addHtml(data,value){
	if (value=='scope') {
		if ($("#addcity").length>0) {
			$("#addcity").remove();
		}
		var html = "<div id='addcity'><form action='' method='get'>";
			$.each(data, function(j, item) {
				 html +="<label><input name='city' type='checkbox' value='"+item.id+"'/>"+item.comcodename+"</label>";
			});
			html +="</form></div>";
		$("#provincecode").append(html);
	} else {
		if ($("#addcitytrule").length>0) {
			$("#addcitytrule").remove();
		}
		var html = "<div id='addcitytrule'><form action='' method='get'>";
			$.each(data, function(j, item) {
				 html +="<label><input name='citytrule' type='checkbox' value='"+item.id+"'/>"+item.comcodename+"</label>";
			});
			html +="</form></div>";
		$("#querytrule").append(html);
	}
}
function changcityadd(value, selData) {
	if (value=='scope') {
		var parentid=$("#provinceadd").val();
	} else {
		var parentid=$("#province").val();
	}
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
			addHtml(data,value);
			if (selData && selData.length > 0) {
				$.each(selData, function(j, item) {
					//html +=item.comcodename+'&nbsp&nbsp';
					$("#provincecode").find("input[name='city'][value='"+item.comcode+"']").attr("checked",true);
				});
			}
		}
	});
}
function changcityadd1() {

		var parentid=$("#provinceadd1").val();

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
			addHtml1(data);

		}
	});
}
function addHtml1(data){

		if ($("#addcity1").length>0) {
			$("#addcity1").remove();
		}
		var html = "<div id='addcity1'><form action='' method='get'>";
		$.each(data, function(j, item) {
			html +="<label><input name='querycity' type='checkbox' value='"+item.id+"'/>"+item.comcodename+"</label>";
		});
		html +="</form></div>";
		$("#provincecode1").append(html);

}

function savebuttonbycity() {
	var paramsList=new Array();
	if ($("input[name^='city']:checked").length==0) {
		alert("请至少选择一个市区！");
		return;
	}
	$("input[name^='city']:checked").each(function(){
	       var a = $(this).val();
	       if (a!==null&&a!=="") {
	    	   paramsList.push(a);
		}
	});
	$.ajax({
		headers: {
            "Content-type":"application/json",
            	"Accept":"application/json"
        },
		url : "onekeysavescope",
		type : 'POST',
		secureuri: false,
		data:JSON.stringify(
				{
					agreementid:$('#agreementid').val(),
					citys:paramsList,
				    province:$("#provinceadd").val()
				}),
		dataType : "json",
		error : function() {
			alert("Connection error");
		},
		success : function(data) {
			alert(data.message);
		}
	});
}
function queryTrule() {
	var trulename=$("#trulename").val();
	var list=new Array();
	if ($("input[name^='city']:checked").length==0) {
		alert("请至少选择一个市区！");
		return;
	}
	$("input[name^='citytrule']:checked").each(function(){
		var a=$(this).val();
	       if (a!==null&&a!=="") {
	    	   list.push(a);
		}
	});
	$.ajax({
		headers: {
            "Content-type":"application/json",
            	"Accept":"application/json"
        },
		url : "gettrulebydeptid",
		type : 'POST',
		secureuri: false,
		data:JSON.stringify({
				citys:list,
				trulename:trulename
			}),
		dataType : "json",
		error : function() {
			alert("Connection error");
		},
		success : function(data) {
			$('#table-trule').bootstrapTable('load', data.body);
		}
	});
}

//点击单选按钮电子保单or纸质保单的事件
function checkStatus(event) {
	if (event == null) {
		return;
	}
	var targer = event.target;
	var id = $(targer).attr('id');
	if (id == "elePolicy") {//选择电子保单时去除自取与快递的勾选&设置为不可选
		$('input[name="distritype"][value="1"]').prop('checked', false);
		$('input[name="distritype"][value="2"]').prop('checked', false);
		$('input[name="distritype"][value="1"]').attr('disabled', 'disabled');
		$('input[name="distritype"][value="2"]').attr('disabled', 'disabled');
	} else if (id == "paperPolicy") {//选择纸质保单时去除自取与快递的不可选性
		$('input[name="distritype"][value="1"]').removeAttrs('disabled');
		$('input[name="distritype"][value="2"]').removeAttrs('disabled');
	}
}