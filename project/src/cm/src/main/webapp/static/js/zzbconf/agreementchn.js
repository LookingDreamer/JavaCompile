var pagesize = 6;

var deptsetting = {
	async: {
		enable: true,
		url:"/cm/policyitem/querydepttree",
		autoParam:["id"],
		dataType: "json",
		type: "post"
	},
	check: {
		enable: true,
		chkStyle: "radio",
		radioType: "all"
	},
	callback: {
		onCheck: deptTreeOnCheck
	}
};

var providersetting = {
	async: {
		enable: true,
		url:"/cm/policyitem/queryprovidertree",
		autoParam:["id"],
		dataType: "json",
		type: "post"
	},
	check: {
		enable: true,
		chkStyle: "radio",
		radioType: "all"
	},
	callback: {
		onCheck: providerTreeOnCheck
	}
};

var deptsettingsw = {
	async: {
		enable: true,
		url:"/cm/policyitem/querydepttree",
		autoParam:["id"],
		dataType: "json",
		type: "post"
	},
	check: {
		enable: true,
		chkStyle: "radio",
		radioType: "all"
	},
	callback: {
		onCheck: deptTreeOnChecksw
	}
};

var providersettingsw = {
	async: {
		enable: true,
		url:"/cm/policyitem/queryprovidertree",
		autoParam:["id"],
		dataType: "json",
		type: "post"
	},
	check: {
		enable: true,
		chkStyle: "radio",
		radioType: "all"
	},
	callback: {
		onCheck: providerTreeOnChecksw
	}
};

function deptTreeOnCheck(event, treeId, treeNode) {
	$("#deptid").val(treeNode.id);
	$("#deptname").val(treeNode.name);
	$('#showdept').modal("hide");
}

function providerTreeOnCheck(event, treeId, treeNode) {
	$("#providerid").val(treeNode.prvcode);
	$("#providername").val(treeNode.name);
	$('#showprovider').modal("hide");
}

function deptTreeOnChecksw(event, treeId, treeNode) {
	$("#deptidsw").val(treeNode.id);
	$("#deptnamesw").val(treeNode.name);
	$('#showdeptsw').modal("hide");
}

function providerTreeOnChecksw(event, treeId, treeNode) {
	$("#provideridsw").val(treeNode.prvcode);
	$("#providernamesw").val(treeNode.name);
	$('#showprovidersw').modal("hide");
}

function operateFormatter(value, row, index) {
    return [ '<a class="edit m-left-5" href="javascript:void(0)" title="修改">',
             '<i class="glyphicon glyphicon-edit"></i>', '</a>', ].join('');
}

var operateEvents = {
    'click .edit': function (e, value, row, index) {
    	$("#agName").val(row.agreementname);
    	$("#selAgreeId").val(row.id);
    	$("#selAgreeCode").val(row.agreementcode);
    	$("#selPrvId").val(row.providerid);
    	$("#selDeptId").val(row.deptid);
    	$("#editAg").show(); 
    	//console.log("agchnlist:" + row.id);
        $('#chntable-list').bootstrapTable(
			'refresh',
			{url:'agchnlist?agreeid=' + row.id}
		);
        
    }
};

function initAgreementChn() {
	$('#chntable-list').bootstrapTable({
        method: 'get',
        url: '',
        cache: false,
        striped: true,
        sidePagination: 'server', 
        minimumCountColumns: 2,
        clickToSelect: true,
        columns: [{
		    field: 'state',
		    align: 'center',
		    valign: 'middle',
		    checkbox: true
		},
        {
            field: 'channelname',
            title: '渠道',
            align: 'center',
            valign: 'middle',
            sortable: false
        }]
    });
	
	$('#table-list').bootstrapTable({
        method: 'get',
        url: '',
        cache: false,
        striped: true,
        pagination: true,
        sidePagination: 'server', 
        pageSize: pagesize,
        minimumCountColumns: 2,
        clickToSelect: true,
        columns: [{
            field: 'state',
            align: 'center',
            valign: 'middle',
            radio: true
        }, {
            field: 'agreementcode',
            title: '协议编码',
            visible: true,
            switchable:false
        }, {
            field: 'agreementname',
            title: '协议名称',
            align: 'center',
            valign: 'middle',
            sortable: true
        }, {
            field: 'prvshotname',
            title: '关联供应商',
            align: 'center',
            valign: 'middle',
            sortable: true
        }, {
			field: 'shortname',
			title: '关联机构',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'modifytime',
			title: '更新时间',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
            field: 'underwritestatusname',
            title: '核保状态',
            align: 'center',
            valign: 'middle',
            sortable: true
        }, {
            field: 'agreementstatusname',
            title: '状态',
            align: 'center',
            valign: 'middle',
            sortable: true
        }, {
            field: 'operating',
            title: '操作',
            align: 'center',
            valign: 'middle',
            switchable: false,
            formatter: operateFormatter,
            events: operateEvents
        }]
    });	
	
	$('#tablesw-list').bootstrapTable({
        method: 'get',
        url: '',
        cache: false,
        striped: true,
        pagination: true,
        sidePagination: 'server', 
        pageSize: 3,
        minimumCountColumns: 2,
        clickToSelect: true,
        columns: [{
            field: 'state',
            align: 'center',
            valign: 'middle',
            radio: true
        }, {
            field: 'agreementcode',
            title: '协议编码',
            visible: true,
            switchable:false
        }, {
            field: 'agreementname',
            title: '协议名称',
            align: 'center',
            valign: 'middle',
            sortable: true
        }, {
            field: 'prvshotname',
            title: '关联供应商',
            align: 'center',
            valign: 'middle',
            sortable: true
        }, {
			field: 'shortname',
			title: '关联机构',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
			field: 'modifytime',
			title: '更新时间',
			align: 'center',
			valign: 'middle',
			sortable: true
		}, {
            field: 'underwritestatusname',
            title: '核保状态',
            align: 'center',
            valign: 'middle',
            sortable: true
        }, {
            field: 'agreementstatusname',
            title: '状态',
            align: 'center',
            valign: 'middle',
            sortable: true
        }]
    });
	
	$('#payTypeTable').bootstrapTable({
        method: 'post',
        url: '',
        cache: false,
        striped: true,
        sidePagination: 'server',
        clickToSelect: true,
        columns: [
            {
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            },
            {
                field: 'id',
                title: '序号',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'paychannelname',
                title: '支付通道名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'paytypename',
                title: '支付类型',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'modifytime',
                title: '最近更新时间',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'check',
                title: '当前状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            }
        ]
    });
	
<<<<<<< .working
	$('#deptnameTable').bootstrapTable({
        method: 'get',
        url: '',
        cache: false,
        striped: true,
        sidePagination: 'server',
        columns: [
			{
			    field: 'state',
			    align: 'center',
			    valign: 'middle',
			    radio: true
			},
            {
                field: 'd1name',
                title: '集团',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'd2name',
                title: '平台公司',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'd3name',
                title: '法人公司',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'd4name',
                title: '分公司',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'd5name',
                title: '网点',
                align: 'center',
                valign: 'middle',
                sortable: true
            }
        ]
    });
	
=======
	$('#deptnameTable').bootstrapTable({
        method: 'get',
        url: '',
        cache: false,
        striped: true,
        sidePagination: 'server',
        columns: [
			{
			    field: 'state',
			    align: 'center',
			    valign: 'middle',
			    radio: true
			},
            {
                field: 'd1name',
                title: '集团',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'd2name',
                title: '平台公司',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'd3name',
                title: '法人公司',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'd4name',
                title: '分公司',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'd5name',
                title: '网点',
                align: 'center',
                valign: 'middle',
                sortable: true
            }
        ]
    });
	
	$('#payTypeTableAdd').bootstrapTable({
        method: 'post',
        url: '',
        cache: false,
        striped: true,
        sidePagination: 'server',
        clickToSelect: true,
        columns: [
            {
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            },
            {
                field: 'id',
                title: '序号',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'paychannelname',
                title: '支付通道名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'paytypename',
                title: '支付类型',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'modifytime',
                title: '最近更新时间',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'check',
                title: '当前状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            }
        ]
    });
	
	$('#deptnameTableAdd').bootstrapTable({
        method: 'get',
        url: '',
        cache: false,
        striped: true,
        sidePagination: 'server',
        columns: [
			{
			    field: 'state',
			    align: 'center',
			    valign: 'middle',
			    radio: true
			},
            {
                field: 'd1name',
                title: '集团',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'd2name',
                title: '平台公司',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'd3name',
                title: '法人公司',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'd4name',
                title: '分公司',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                field: 'd5name',
                title: '网点',
                align: 'center',
                valign: 'middle',
                sortable: true
            }
        ]
    });
	
>>>>>>> .merge-right.r12306
	// 【查询】按钮
	$("#querybutton").on("click", function(e) {
		var agname= $("#agname").val();
		var agcode= $("#agcode").val();
		var deptid= $("#deptid").val();
		var providerid= $("#providerid").val();

		$('#table-list').bootstrapTable(
			'refresh',
			{url:'aglist?limit=' + pagesize + '&agname=' + agname + '&agcode=' + agcode + '&deptid=' + deptid + '&providerid=' + providerid}
		);
	});
	$("#querybuttonsw").on("click", function(e) {
		var agname= $("#agnamesw").val();
		var agcode= $("#agcodesw").val();
		var deptid= $("#deptidsw").val();
		var providerid= $("#provideridsw").val();

		$('#tablesw-list').bootstrapTable(
			'refresh',
			{url:'aglist?limit=3&agname=' + agname + '&agcode=' + agcode + '&deptid=' + deptid + '&providerid=' + providerid}
		);
	});
	
	// 查询条件【重置】按钮
	$("#resetbutton").on("click", function(e) {
		$("#qForm")[0].reset();
		$("#providerid").val("");
		$("#deptid").val("");
	});
	$("#resetbuttonsw").on("click", function(e) {
		$("#qFormsw")[0].reset();
		$("#provideridsw").val("");
		$("#deptidsw").val("");
	});
	
	//卡片列表
	$("#toggle").on("click", function(e) {
		$('#table-list').bootstrapTable('toggleView');
	});
	
	//刷新
	$("#refresh").on("click", function(e) {
		$('#table-list').bootstrapTable('refresh');
	});
	
	//选择机构
	$("#deptname").on("click", function(e) {
		$('#showdept').modal();
		$.fn.zTree.init($("#depttree"), deptsetting);
	});
	
	//选择供应商
	$("#providername").on("click", function(e) {
		$('#showprovider').modal();
		$.fn.zTree.init($("#providertree"), providersetting);
	});
	
	//选择机构
	$("#deptnamesw").on("click", function(e) {
		$('#showdeptsw').modal();
		$.fn.zTree.init($("#depttreesw"), deptsettingsw);
	});
	
	//选择供应商
	$("#providernamesw").on("click", function(e) {
		$('#showprovidersw').modal();
		$.fn.zTree.init($("#providertreesw"), providersettingsw);
	});
	
	$("#delchnbutton").on("click", function(e) {
		delchn();
	});
	
	$("#addchnbutton").on("click", function(e) {
		addchn();
	});
	
	$("#switchagbutton").on("click", function(e) {
		var chns = $('#chntable-list').bootstrapTable('getSelections');
		if (chns.length > 0) {
			var agname= $("#agnamesw").val();
			var agcode= $("#agcodesw").val();
			var deptid= $("#deptidsw").val();
			var providerid= $("#provideridsw").val();

			$('#tablesw-list').bootstrapTable(
				'refresh',
				{url:'aglist?limit=3&agname=' + agname + '&agcode=' + agcode + '&deptid=' + deptid + '&providerid=' + providerid}
			);
			
			$('#switchModal').modal('show');
		} else {
			alertmsg('请至少选择一家渠道');
		}
	});
	
	$("#swNextButton").on("click", function(e) {
		var swags = $('#tablesw-list').bootstrapTable('getSelections');
		if (swags.length > 0) {
			$('#switchCfgModal').modal('show');
			//deptlist(swags[0].id);
			$("#cfgagcodename").val("协议配置：" + swags[0].agreementname);
			
			$('#deptnameTable').bootstrapTable('refresh', {url: "agdeptlist?agreeid=" + swags[0].id});
			
			var url = "/cm/channel/getDeptPayType?providerid=&deptid=&agreeid=";
			$('#payTypeTable').bootstrapTable('refresh', {url: url});
		} else {
			alertmsg('请选择协议');
		}
	});

	$("#execCopyButton").on("click",function() {
		addchnnext();
	});
	$("#swactbuttonAdd").on("click",function() {
		addchnact();
	});
	
	$("#swactbutton").on("click",function() {
		var depts = $('#deptnameTable').bootstrapTable('getSelections');
		var deptId = "";
		if (depts.length > 0 ) {
			deptId = depts[0].deptid5;
		}
		if (deptId == "") {
			alertmsg('请选择出单网点！');
			return;
		}
		
		var pays = $('#payTypeTable').bootstrapTable('getSelections');
		var payIds = "";
		for(var i = 0; i < pays.length; i++){
			if ( i == 0 ) {
				payIds += pays[i].id;
			} else {
				payIds += "," + pays[i].id;
			}
		}
		if (payIds == "") {
			alertmsg('请至少选择一个支付方式！');
			return;
		}
		
		//var selAgreeId = $('#selAgreeId').val();
		var selAgreeCode = $('#selAgreeCode').val();
		var agName = $('#agName').val();
		var prvId = $('#selPrvId').val();
		var deptid = $('#selDeptId').val();
		
		var swags = $('#tablesw-list').bootstrapTable('getSelections');
		var toAgreeId = swags[0].id;
		var toAgreeName = swags[0].agreementname;
		var toAgreeCode = swags[0].agreementcode;
		var toPrvId = swags[0].providerid;
		var toDeptId= swags[0].deptid;
		
		if (selAgreeCode == toAgreeCode) {
			alertmsg('同一个协议不能切换！');
			return;
		}
		//if (deptid != toDeptId) {
		//	alertmsg('协议不应跨平台切换！');
		//	return;
		//}
		var pidsub = prvId.substr(0, 4);
		var topidsub = toPrvId.substr(0, 4);
		console.log('pidsub->' + pidsub); 
		console.log('topidsub->' + topidsub);  
		if (pidsub != topidsub) {
			alertmsg('不同供应商之间的协议不能相互切换！');
			return;
		}
		
		confirmmsg( '是否将' + selAgreeCode + '-' + agName + '原有关联渠道切换到' + toAgreeCode + '-' + toAgreeName + '？', function(e){
			
			confirmmsg( '该协议原有配置的渠道数据将被删除，请确认是否切换！', function(e){
				switchagact(toAgreeId, payIds, deptId);
		    }, function(e){} );
			
	    }, function(e){} );
		
	});
}

function deptlist(agreeid) {
    $.ajax({
        url: "/cm/channel/getAllDeptName",
        type: 'POST',
        dataType: 'json',
        data: {
            "agreementid": '',
            "providerid": '',
            "soulagreeid": agreeid
        },
        cache: false,
        async: true,
        success: function (data) {
            $("#deptradios").empty();
            var arrList = new Array();
            arrList.push(1);
            if (data.chosed != null) {
                for (var j = 0; j < data.chosed.length; j++) {
                    arrList.push(data.chosed[j].deptid5);
                }
            }
            for (var i = 0; i < data.all.length; i++) {
                $("#deptradios").append("<input type='radio' id='radiobutton" + i + "' name='deptradiobutton' value='" + data.all[i].deptid + "'/>" + data.all[i].comname + "<br>");
                if ($.inArray(data.all[i].comname, arrList) != -1) {
                    $('#radiobutton' + i).attr("checked", "checked");
                }
            }
        }
    });
}

function switchagact(toAgreeId, payIds, deptId) {
	var agreeId = $('#selAgreeId').val();
	var chns = $('#chntable-list').bootstrapTable('getSelections');
	var channelIds = "";
	for(var i = 0; i < chns.length; i++){
		if ( i == 0 ) {
			channelIds += chns[i].channelid;
		} else {
			channelIds += "," + chns[i].channelid;
		}
	}
	//var swags = $('#tablesw-list').bootstrapTable('getSelections');
	//var toAgreeId = swags[0].id;
	
	//var deptIdo = $('input[name="deptradiobutton"]:checked').val();
	//alert(deptIdo);
	
	//alert(deptId);
	
	$.ajax({
        url: 'swagchn',
        type: 'POST',
        dataType: "text",
        data: {
        	channelIds: channelIds,
        	agreeId: agreeId,
        	toAgreeId: toAgreeId,
        	payIds: payIds,
        	deptId: deptId
        },
        error: function () {
            alertmsg("切换失败，请重新切换");
        },
        success: function (data) {
        	if (data == "success") {
        		alertmsg("切换成功！");
        		$('#switchCfgModal').modal('hide');
        		$('#switchModal').modal('hide');
        		$('#chntable-list').bootstrapTable('refresh');
        	} else {
        		alertmsg(data);
        	}
        }
    });
}

function addchn() {
	initmodaltree();
    $('#addChnModal').modal('show');
}

function initmodaltree() {
	$.ajax({
		url : "/cm/channel/queryTreeListDim",
		type : 'POST',
		dataType : "json",
		error : function() {
			$("#channelModalTree").html('加载失败，<a href="javascrpt:void(0);" onclick=";">点击这里</a>重试。');
		},
		success : function(data) {
			$.fn.zTree.init($("#channelModalTree"), {
				data : {
					simpleData: {
						enable: true,
						idKey: "id",
						pIdKey: "pId",
						rootPId: null
					}
				},
				check : {
					enable : true
				},
				view: {
					fontCss: function (treeId, treeNode) {
						return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
					},
					expandSpeed: ""
				},
				callback : {
					
				}
			}, data);
		}
	});
}

function addchnnext() {
	var treeObj = $.fn.zTree.getZTreeObj('channelModalTree');
    var nodes = treeObj.getCheckedNodes(true);
    var checkedIds = '';
    var checkedNames = '';
    var index = 1;
    for (var i = 0; i < nodes.length; i++) {
    	var pId = nodes[i].pId ;
        if(nodes[i].pId != '' && nodes[i].pId != null){
            checkedIds += nodes[i].id + ',';
            checkedNames += (index) +':'+ nodes[i].name +'<br> ';
            index +=1;
        }
    }
    if (checkedIds.length > 0) {
        checkedIds = checkedIds.substring(0, checkedIds.length - 1);
        checkedNames = checkedNames.substring(0, checkedNames.length - 1);
    }else{
        alertmsg('请至少选择一家渠道');
        return ;
    }
    $("#selChnIds").val(checkedIds);
    $("#selChnNames").val(checkedNames);
    
	$("#cfgagcodenameAdd").val("协议配置：" + $("#agName").val());
	
	$('#deptnameTableAdd').bootstrapTable('refresh', {url: "agdeptlist?agreeid=" + $("#selAgreeId").val()});
	
	var url = "/cm/channel/getDeptPayType?providerid=&deptid=&agreeid=";
	$('#payTypeTableAdd').bootstrapTable('refresh', {url: url});
	
    $('#switchCfgModalAdd').modal('show');
}

function addchnact() {
	var depts = $('#deptnameTableAdd').bootstrapTable('getSelections');
	var deptId = "";
	if (depts.length > 0 ) {
		deptId = depts[0].deptid5;
	}
	if (deptId == "") {
		alertmsg('请选择出单网点！');
		return;
	}
	
	var pays = $('#payTypeTableAdd').bootstrapTable('getSelections');
	var payIds = "";
	for(var i = 0; i < pays.length; i++){
		if ( i == 0 ) {
			payIds += pays[i].id;
		} else {
			payIds += "," + pays[i].id;
		}
	}
	if (payIds == "") {
		alertmsg('请至少选择一个支付方式！');
		return;
	}
    
    var agreeId = $('#selAgreeId').val();
    var checkedIds = $("#selChnIds").val();
    confirmmsg('是否添加如下渠道： '+'<br>'+$("#selChnNames").val(),function(e){
        batchCopyChannelAg(agreeId, checkedIds, deptId, payIds);
    },function(e){

    });
}

function batchCopyChannelAg(agreeId, checkedIds, deptId, payIds){
    $.ajax({
        url: 'addchn',
        type: 'POST',
        dataType: "text",
        data: {
        	agreeId: agreeId,
            channelIds: checkedIds,
            deptId: deptId,
            payIds: payIds
        },
        error: function () {
            alertmsg("Connection error");
        },
        success: function (data) {
        	alertmsg(data);
        	if (data == "success") {
        		$('#switchCfgModalAdd').modal('hide');
        		$('#addChnModal').modal('hide');
        		$('#chntable-list').bootstrapTable('refresh');
        	} 
        }
    });
}

function delchn() {
	var chns = $('#chntable-list').bootstrapTable('getSelections');
	//console.log("selected : -->" + JSON.stringify(chns));
	var ids = "";
	for(var i = 0; i < chns.length; i++){
		if ( i == 0 ) {
			ids += chns[i].id;
		} else {
			ids += "," + chns[i].id;
		}
	}
	if (ids == "") {
		alertmsg("请勾选渠道");
		return;
	} 
	
	$.ajax({
        url: "delchn",
        type: 'post',
        dataType: 'text',
        data: {
			"ids": ids
		},
        error: function () {
            alertmsg("Connection error");
        },
        success: function (data) {
        	alertmsg(data);
        	if (data == "success") {
        		$('#chntable-list').bootstrapTable('refresh');
        	} 
        }
    });
}

function treesearch() {
	var nodeList = [];
	var targetTree = null;
	$(".ztree-search").on("change", function(){
		targetTree = $.fn.zTree.getZTreeObj($(this).attr("data-bind"));
		if (!targetTree) {
			return;
		}
		updateNodes(false);
		targetTree.expandAll(false);
		var searchKey =  $(this).val();
		if (searchKey == "") {
			//targetTree.showNodes(targetTree.getNodes());
			return;
		}
		//alertmsg(targetTree.find("li[treenode]").size());
		//targetTree.hideNodes(targetTree.getNodes());
		nodeList = targetTree.getNodesByParamFuzzy("name", searchKey);
		//targetTree.showNodes(nodeList);
		updateNodes(true);
	});
	
	function updateNodes(highlight) {
		for( var i=0, l=nodeList.length; i<l; i++) {
			nodeList[i].highlight = highlight;
			targetTree.updateNode(nodeList[i]);
			showParentNode(nodeList[i].getParentNode(), highlight);
			targetTree.selectNode(nodeList[0]);
		}
	}
	function showParentNode(parentNode, showFlag) {
		if (parentNode) {
			//targetTree.showNode(parentNode);
			targetTree.expandNode(parentNode, showFlag, null, null, null);
			showParentNode(parentNode.getParentNode(), showFlag);
		}
	}
}
