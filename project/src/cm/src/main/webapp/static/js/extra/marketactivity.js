require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","zTree","zTreecheck","public","bootstrapdatetimepicker","bootstrapdatetimepickeri18n"], function ($) {
	//数据初始化 
	$(function() {
		
		$('#table-list').bootstrapTable({
            method: 'get',
            url: pageurl,
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server', 
            pageSize: pagesize,
       //     minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
                field: 'activitycode',
                title: '活动ID',
                align: 'center',
                valign: 'middle',
                sortable: true
                
            }, {
                field: 'activityname',
                title: '活动名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
            	field: 'tmpcode',
            	title: 'tmpcode',
            	align: 'center',
            	valign: 'middle',
            	visible: false,
            	sortable: true
            }, {
                field: 'activitytype',
                title: '活动类型',
                align: 'center',
                valign: 'middle',
                sortable: true,
				formatter:activityTypeFormatter
            }, {
				field: 'effectivetime',
				title: '生效时间',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
                field: 'terminaltime',
                title: '结束时间',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'status',
                title: '活动状态',
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter:statusFormatter
            }, {
                field: 'limited',
                title: '限制人数',
                align: 'center',
                valign: 'middle',
                sortable: true
            },{
				field: 'noti',
				title: '活动描述',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
                field: 'createtime',
                title: '创建时间',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
				field: 'operating',
				title: '操作',
				align: 'left',
				valign: 'middle',
				switchable: false,
				formatter: operateFormatter1,
				events: operateEvents1
			}]
        });

		//活动奖品列表refresh
		$('#prize-table-list').bootstrapTable({
			method: 'get',
			url: 'queryActivityPrizeListById?id='+0,
			cache: false,
			striped: true,
			pagination: true,
			sidePagination: 'server',
			pageSize: pagesize,
			minimumCountColumns: 2,
			clickToSelect: true,
			columns: [{
				field: 'id',
				title: 'id',
				visible: false,
				switchable:false
			}, {
				field: 'activityid',
				title: 'activityid',
				visible: false,
				switchable:false
			}, {
				field: 'prizeid',
				title: 'prizeid',
				visible: false,
				switchable:false
			},
				{
				field: 'activityname',
				title: '活动名称',
				visible: false,
				align: 'center',
				valign: 'middle',
				sortable: false
			}, {
				field: 'activitytype',
				title: '活动类型',
					visible: false,
				align: 'center',
				valign: 'middle',
				sortable: false,
				formatter:activityTypeFormatter
			},  {
				field: 'prizename',
				title: '奖品名称',
				align: 'center',
				valign: 'middle',
				sortable: false
			}, {
				field: 'effectivetime',
				title: '奖品生效时间',
				align: 'center',
				valign: 'middle',
				sortable: false
			},
				{
					field: 'terminaltime',
					title: '奖品失效时间',
					align: 'center',
					valign: 'middle',
					sortable: false
				}
				, {
					field: 'status',
					title: '奖品状态',
					align: 'center',
					valign: 'middle',
					sortable: false,
					formatter:statusFormatter
				},
				{
					field: 'operating',
					title: '操作',
					align: 'center',
					valign: 'middle',
					switchable: false,
					formatter: operateFormatter2,
					events: operateEvents2
				}
			]
		});
		//活动奖品列表结束

		//活动规则列表
		$('#conditions-table-list').bootstrapTable({
			method: 'get',
			url: 'queryActivityConditionListById?conditionsource="01"&&id='+0,
			cache: false,
			striped: true,
			pagination: true,
			sidePagination: 'server',
			pageSize: pagesize,
			minimumCountColumns: 2,
			clickToSelect: true,
			columns: [{
				field: 'id',
				title: 'id',
				visible: false,
				switchable:false
			},{
				field: 'sourceid',
				title: 'sourceid',
				visible: false,
				switchable:false
			},
				{
					field: 'conditionsource',
					title: '规则类型',
					visible: false,
					align: 'center',
					valign: 'middle',
					sortable: false
				}, {
					field: 'paramdatatype',
					title: '数据类型',
					align: 'center',
					valign: 'middle',
					sortable: false
				},  {
					field: 'paramcnname',
					title: '参数名称',
					align: 'center',
					valign: 'middle',
					sortable: false
				}, {
					field: 'expression',
					title: '表达式',
					align: 'center',
					valign: 'middle',
					sortable: false
				},
				{
					field: 'paramvalue',
					title: '参数值',
					align: 'center',
					valign: 'middle',
					sortable: false
				}
				, {
					field: 'noti',
					title: '参数描述',
					align: 'center',
					valign: 'middle',
					sortable: false
				},
				{
					field: 'operating',
					title: '操作',
					align: 'center',
					valign: 'middle',
					switchable: false,
					formatter: operateFormatter3,
					events: operateEvents3
				}
			]
		});
		//活动规则列表结束

		$('#prizelist').hide();
		$('#conditionslist').hide();
		//<!--add-->
		 $("#terminaltime").datetimepicker({
			  language: 'zh-CN',
		      format: "yyyy-mm-dd",
		      weekStart: 1,
		      todayBtn: 1,
		      autoclose: 1,
		      todayHighlight: 1,
		      startView: 2,
		      forceParse: 0,
		      minView: 2,
		      pickerPosition: "bottom-right",
		      pickTime:true,
		      todayBtn:true,
		      pickDate:true,
		      autoclose:true
		 });
		 
		 $("#effectivetime").datetimepicker({
			  language: 'zh-CN',
		      format: "yyyy-mm-dd",
		      weekStart: 1,
		      todayBtn: 1,
		      autoclose: 1,
		      todayHighlight: 1,
		      startView: 2,
		      forceParse: 0,
		      minView: 2,
		      pickerPosition: "bottom-left",
		      pickTime:true,
		      todayBtn:true,
		      pickDate:true,
		      autoclose:true
		 })
		 
		  $("#i_terminaltime").datetimepicker({
			  language: 'zh-CN',
		      format: "yyyy-mm-dd",
		      weekStart: 1,
		      todayBtn: 1,
		      autoclose: 1,
		      todayHighlight: 1,
		      startView: 2,
		      forceParse: 0,
		      minView: 2,
		      pickerPosition: "bottom-left",
		      pickTime:true,
		      todayBtn:true,
		      pickDate:true,
		      autoclose:true
		 })
		 
		  $("#i_effectivetime").datetimepicker({
			  language: 'zh-CN',
		      format: "yyyy-mm-dd",
		      weekStart: 1,
		      todayBtn: 1,
		      autoclose: 1,
		      todayHighlight: 1,
		      startView: 2,
		      forceParse: 0,
		      minView: 2,
		      pickerPosition: "bottom-left",
		      pickTime:true,
		      todayBtn:true,
		      pickDate:true,
		      autoclose:true
		 })

		$("#s_terminaltime").datetimepicker({
			language: 'zh-CN',
			format: "yyyy-mm-dd",
			weekStart: 1,
			todayBtn: 1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			forceParse: 0,
			minView: 2,
			pickerPosition: "bottom-left",
			pickTime:true,
			todayBtn:true,
			pickDate:true,
			autoclose:true
		})

		$("#s_effectivetime").datetimepicker({
			language: 'zh-CN',
			format: "yyyy-mm-dd",
			weekStart: 1,
			todayBtn: 1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			forceParse: 0,
			minView: 2,
			pickerPosition: "bottom-left",
			pickTime:true,
			todayBtn:true,
			pickDate:true,
			autoclose:true
		})

		//绑定活动编辑确认事件
		$("#s_execButton").click(function () {
			if($("#s_activityname").val().trim() == ""){
				alertmsg("活动名称不能为空");
				return false;
			}
			if($("#s_effectivetime").val().trim() == ""){
				alertmsg("活动生效日期不能为空");
				return false;
			}
			if($("#s_terminaltime").val().trim() == ""){
				alertmsg("活动失效日期不能为空");
				return false;
			}
			if(!/^[0-9]*$/.test($("#s_limited").val())){
				alertmsg("活动限制人数必须为数字");
				return false;
			}
			//活动状态变更为启动和关闭时让USER确认
			if($("#s_status").val()!="0") {
				if (confirm("活动开启和关闭后将不可编辑，是否确认？")) {
					saveMarketActivity();
				}
			}else{
				saveMarketActivity();
			}
		});
		//新建活动编辑确认事件
		$("#i_execButton").click(function () {
			if($("#i_activityname").val().trim() == ""){
				alertmsg("活动名称不能为空");
				return false;
			}
			if($("#i_effectivetime").val().trim() == ""){
				alertmsg("活动生效日期不能为空");
				return false;
			}
			if($("#i_terminaltime").val().trim() == ""){
				alertmsg("活动失效日期不能为空");
				return false;
			}
			if(!/^[0-9]*$/.test($("#i_limited").val())){
				alertmsg("活动限制人数必须为数字");
				return false;
			}
			$.ajax({
				url: "save",
				type: 'post',
				dataType: "text",
				cache: false,
				async: true,
				data: {
					activityname: $("#i_activityname").val(),
					activitytype: $("#i_activitytype").val(),
					limited: $("#i_limited").val(),
					effectivetime: $("#i_effectivetime").val(),
					terminaltime: $("#i_terminaltime").val(),
					status: $("#i_status").val(),
					noti: $("#i_noti").val()
				},
				error: function () {
					alertmsg("Connection error");
				},
				success: function (data) {
					//if (data == "success") {
						$('#insertMarketActivityModel').modal('hide');
						$("#querybutton").click();
						alertmsg("操作成功");
					//}
					//else
					//	alertmsg("操作失败");
				}
			});
		});
		//新建活动确认结束

		//配置活动奖品确认事件
		$("#i_prize_execButton").click(function () {
			$.ajax({
				url: "saveMarketActivityPrize",
				type: 'post',
				dataType: "text",
				cache: false,
				async: true,
				data: {
					activityid: $("#setting_activityid").val(),
					prizeid: $("#i_prizeid").val()
				},
				error: function () {
					alertmsg("Connection error");
				},
				success: function (data) {
					//if (data == "success") {
					$('#insertMarketActivityPrizeModel').modal('hide');
					$("#queryprizebutton").click();
					alertmsg("操作成功");
					//}
					//else
					//	alertmsg("操作失败");
				}
			});
		});
		//配置活动奖品结束

		//配置活动规则事件
		$("#i_condition_execButton").click(function () {
			if($("#condition_paramvalue").val().trim() ==""){
				alertmsg("参数值不能为空!");
				return false;
			}
			$.ajax({
				url: "saveMarketActivityCondition",
				type: 'post',
				dataType: "text",
				cache: false,
				async: true,
				data: {
					sourceid: $("#condition_activityid").val(),
					conditionsource: '01',
					paramname: $("#condition_paramname").val(),
					expression: $("#condition_expression").val(),
					paramvalue: $("#condition_paramvalue").val()
				},
				error: function () {
					alertmsg("Connection error");
				},
				success: function (data) {
					//if (data == "success") {
					$('#insertMarketActivityConditionModel').modal('hide');
					$("#querycondbutton").click();
					alertmsg("操作成功");
					//}
					//else
					//	alertmsg("操作失败");
				}
			});
		});
		//配置活动规则结束

		//编辑活动规则事件
		$("#u_condition_execButton").click(function () {
			if($("#u_paramvalue").val().trim() ==""){
				alertmsg("参数值不能为空!");
				return false;
			}
			$.ajax({
				url: "saveMarketActivityCondition",
				type: 'post',
				dataType: "text",
				cache: false,
				async: true,
				data: {
					id:$("#u_id").val(),
					sourceid: $("#u_activityid").val(),
					conditionsource: '01',
					paramname: $("#u_paramname").val(),
					expression: $("#u_expression").val(),
					paramvalue: $("#u_paramvalue").val()
				},
				error: function () {
					alertmsg("Connection error");
				},
				success: function (data) {
					//if (data == "success") {
					$('#updateMarketActivityConditionModel').modal('hide');
					$("#querycondbutton").click();
					alertmsg("操作成功");
					//}
					//else
					//	alertmsg("操作失败");
				}
			});
		});
		//编辑活动规则结束

		// 活动列表页面【查询】按钮
		$("#querybutton").on("click", function(e) {
			$('#conditionslist').hide();
			$('#prizelist').hide();
			/**
			var activitycode = $("#activitycode").val();
			var activityname= $("#activityname").val();
			var activitytype= $("#activitytype").val();
			var status= $("#status").val();
			var effectivetime= $("#effectivetime").val();
			var terminaltime= $("#terminaltime").val();**/
			$('#table-list').bootstrapTable(
					'refresh',
					{url:'queryActivityList?'+$("#policymanagent").serialize()
					});
		 });

		// 活动奖品列表页面【查询】按钮
		$("#queryprizebutton").on("click", function(e) {
			var id= $("#temp_activityid").val();
			$('#prize-table-list').bootstrapTable(
				'refresh',
				{url:'queryActivityPrizeListById?limit=10&id='+id
				});
		});

		// 活动规则列表页面【查询】按钮


		$("#querycondbutton").on("click", function(e) {
			var id= $("#temp_activityid2").val();
			$('#conditions-table-list').bootstrapTable(
				'refresh',
				{url:'queryActivityConditionListById?limit=10&conditionsource=01&id='+id
				});
		});

		
//		返回
		$('#backbutton').on("click",function(){
			history.go(-1)
		});
		// 查询条件【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#policymanagent")[0].reset();
			$("#providerid").val("");
			$("#deptid").val("");
			
		});
		//卡片列表
		$("#toggle").on("click", function(e) {
			$('#table-list').bootstrapTable('toggleView');
		});

		//新建活动按钮
		$("#insertMarketActivity").on("click", function(e) {
			$('#insertMarketActivityModel').modal('show');
			
		});
		
		//配置活动奖品按钮
		$("#insertMarketActivityPrize").on("click", function(e) {
			$('#insertMarketActivityPrizeModel').modal('show');
			
		});
		//配置活动规则按钮
		$("#insertMarketActivityCondition").on("click", function(e) {
			$('#insertMarketActivityConditionModel').modal('show');
		});
		//配置活动奖品按钮
		$("#insertMarketActivityPrize2").on("click", function(e) {
			$('#insertMarketActivityPrizeModel').modal('show');
		});
		//配置活动规则按钮
		$("#insertMarketActivityCondition2").on("click", function(e) {
			$('#insertMarketActivityConditionModel').modal('show');
		});
		//刷新
		$("#refresh").on("click", function(e) {
			$('#table-list').bootstrapTable('refresh');
		});
	});

});

//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "queryActivityList";

// 添加事件
function operateFormatter1(value, row, index) {
	return row.status != "0" ? [
		'<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
		'<i class="glyphicon glyphicon-edit"></i>',
		'</a>',
		'<a class="prize m-left-5" href="javascript:void(0)" title="奖品列表">',
		'<i class="glyphicon glyphicon-list"></i>',
		'</a>',
		'<a class="conditions m-left-5" href="javascript:void(0)" title="规则列表">',
		'<i class="glyphicon glyphicon-list-alt"></i>',
		'</a>'
	].join('') :
	[
		'<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
		'<i class="glyphicon glyphicon-edit"></i>',
		'</a>',
		'<a class="prize m-left-5" href="javascript:void(0)" title="奖品列表">',
		'<i class="glyphicon glyphicon-list"></i>',
		'</a>',
		'<a class="conditions m-left-5" href="javascript:void(0)" title="规则列表">',
		'<i class="glyphicon glyphicon-list-alt"></i>',
		'</a>',
		'<a class="delete m-left-5" href="javascript:void(0)" title="删除">',
		'<i class="glyphicon glyphicon-remove"></i>',
		'</a>'
	].join('');
}

function operateFormatter2(value, row, index) {
	return $('#temp_status').val() != "编辑" ? [].join(''):['<a class="deleteActivityPrize m-left-5" href="javascript:void(0)" title="删除">',
		    '<i class="glyphicon glyphicon-remove"></i>',
		     '</a>'
	        ].join('');
}

function operateFormatter3(value, row, index) {
	return $('#temp_status').val() != "编辑" ? [].join(''):[
		'<a class="editActivityCondition m-left-5" href="javascript:void(0)" title="编辑">',
		'<i class="glyphicon glyphicon-edit"></i>',
		'</a>',
		'<a class="deleteActivityCondition m-left-5" href="javascript:void(0)" title="删除">',
		'<i class="glyphicon glyphicon-remove"></i>',
		'</a>'
	].join('');
}

//删除活动规则refresh
window.operateEvents3 = {
	'click .deleteActivityCondition' : function(e, value, row, index) {
		var id = row.id;
		$.ajax({
			url : "deleteActivityCondition",
			type : 'POST',
			dataType : "text",
			data : {
				id:id
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
				return null;
			},
			success : function(data) {
				alertmsg("success!");
				var id= $("#temp_activityid2").val();
				$('#conditions-table-list').bootstrapTable(
					'refresh',
					{url:'queryActivityConditionListById?limit=10&conditionsource=01&id='+id
					});
			}
		});
	},
	'click .editActivityCondition' : function(e, value, row, index) {
		$("#u_id").val(row.id);
		$("#u_paramname").val(row.paramname);
		$("#u_expression").val(row.expression);
		$("#u_paramvalue").val(row.paramvalue);
		$('#updateMarketActivityConditionModel').modal('show');
	}
}
//删除活动奖品
window.operateEvents2 = {
	'click .deleteActivityPrize' : function(e, value, row, index) {
		var id = row.id;
		$.ajax({
			url : "deleteActivityPrize",
			type : 'POST',
			dataType : "text",
			data : {
				id:id
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
				return null;
			},
			success : function(data) {
				alertmsg("success!");
				var id= $("#temp_activityid").val();
				$('#prize-table-list').bootstrapTable(
					'refresh',
					{url:'queryActivityPrizeListById?limit=10&id='+id
					});
			}
		});
	}
}
// 事件相应
window.operateEvents1 = {
	'click .edit' : function(e, value, row, index) {
		if(row.status!="0"){
			$(".readonlysetting").attr("readonly","readonly");
			$(".disabledsetting").attr("disabled","ture");
			$("#s_status").empty();
			$("#s_status").append("<option value='1'>开启</option>");
			$("#s_status").append("<option value='2'>关闭</option>");
		}else{
			$(".readonlysetting").removeAttr("readonly");
			$(".disabledsetting").removeAttr("disabled");
			$("#s_status").empty();
			$("#s_status").append("<option value='0'>编辑</option>");
			$("#s_status").append("<option value='1'>开启</option>");
			$("#s_status").append("<option value='2'>关闭</option>");
		}
		$("#s_id").val(row.id);
		$("#s_activitycode").val(row.activitycode);
		$("#s_tmpcode").val(row.tmpcode);
		$("#s_activityname").val(row.activityname);
		$("#s_activitytype").val(row.activitytype);
		$("#s_limited").val(row.limited);
		$("#s_effectivetime").val(row.effectivetime);
		$("#s_terminaltime").val(row.terminaltime);
		$("#s_status").val(row.status);
		$("#s_noti").val(row.noti);
		$('#updateMarketActivityModel').modal('show');
	},
	'click .delete' : function(e, value, row, index) {
		deleteActivity(row.id);
	},
	'click .prize' : function(e, value, row, index) {
		activityPrize(row.id,row.activityname,row.activitytype,row.status);
	},
	'click .conditions' : function(e, value, row, index) {
		activityConditions(row.id,row.activityname,row.activitytype,row.status);
	}
};
//获取活动奖品列表
function activityPrize(id,name,type,status){
	$('#conditionslist').hide();
	$('#prizelist').show();
	$('#temp_activityid').val(id);
	$('#temp_activityname').val(name);
	$('#temp_status').val(statusFormatter(status,'',''));
	$('#temp_activitytype').val(activityTypeFormatter(type,'',''));
	$('#temp_activityname2').val(name);
	$('#temp_status2').val(statusFormatter(status,'',''));
	$('#temp_activitytype2').val(activityTypeFormatter(type,'',''));
	$('#setting_activityid').val(id);
	$('#setting_activityname').val(name);
	//活动奖品列表
	$.ajax({
		url : "queryActivityPrizeListById",
		type : 'get',
		dataType : "json",
		data : {
			id:id
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
			return null;
		},
		success : function(data) {
			$('#prize-table-list').bootstrapTable('load',data);
		}
	});
	//活动奖品列表结束
}

//获取活动规则
function activityConditions(id,name,type,status){
	$('#prizelist').hide();
	$('#conditionslist').show();
	$('#temp_activityid').val(id);
	$('#temp_activityid2').val(id);
	$('#temp_activityname').val(name);
	$('#temp_status').val(statusFormatter(status,'',''));
	$('#temp_activitytype').val(activityTypeFormatter(type,'',''));
	$('#temp_activityname2').val(name);
	$('#temp_activitytype2').val(activityTypeFormatter(type,'',''));
	$('#temp_status2').val(statusFormatter(status,'',''));
	$('#condition_activityid').val(id);
	//$('#condition_activityname').val(name);
	$.ajax({
		url : "queryActivityConditionListById",
		type : 'get',
		dataType : "json",
		data : {
			id:id,
			conditionsource:'01'
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
			return null;
		},
		success : function(data) {
			$('#conditions-table-list').bootstrapTable('load',data);
		}
	});
}

function deleteActivity(id) {
	$.ajax({
		url : "delete",
		type : 'POST',
		dataType : "text",
		data : {
			id:id
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
			return null;
		},
		success : function(data) {
			if(data=="success"){
				alertmsg("success!");
				$('#table-list').bootstrapTable('refresh');
			}else{
				alertmsg("请先删除活动奖品或活动规则！");
			}

		}
	});
}

//保存营销活动
function saveMarketActivity(){
	$.ajax({
		url: "save",
		type: 'post',
		dataType: "text",
		cache: false,
		async: true,
		data: {
			activitycode:$("#s_activitycode").val(),
			tmpcode:$("#s_tmpcode").val(),
			id: $("#s_id").val(),
			activityname: $("#s_activityname").val(),
			activitytype: $("#s_activitytype").val(),
			limited: $("#s_limited").val(),
			effectivetime: $("#s_effectivetime").val(),
			terminaltime: $("#s_terminaltime").val(),
			status: $("#s_status").val(),
			noti: $("#s_noti").val()
		},
		error: function () {
			alertmsg("Connection error");
		},
		success: function (data) {
			if (data == "success") {
				$('#updateMarketActivityModel').modal('hide');
				$("#querybutton").click();
				alertmsg("操作成功");
			} else if (data == "fail") {
				alertmsg("活动奖品或规则未配置");
			}
			else
				alertmsg("操作失败");
		}
	});
}

//状态名称文字转换
function statusFormatter(value, row, index) {
	if(value=='0'){
		return '编辑';
	}else if(value=='1'){
		return '开启';
	}else{
		return '关闭';
	}
}

//活动类型文字转换
function activityTypeFormatter(value, row, index) {
	if(value=='01'){
		return '交易活动';
	}else if(value=='02'){
		return '注册活动';
	}else if(value=='03'){
		return '分享活动';
	}else if(value=='04'){
		return '推荐活动';
	}else{
		return '抽奖活动';
	}
}
//刷新列表
function reloaddata(data){
	$.ajax({
		url : pageurl,
		type : 'GET',
		dataType : "json",
		data: data+"&limit="+pagesize,
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#table-list').bootstrapTable('load', data);
		}
	});
}

