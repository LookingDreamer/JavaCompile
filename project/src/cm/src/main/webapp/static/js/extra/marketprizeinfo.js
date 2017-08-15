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
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
                field: 'id',
                title: 'id',
                visible: false,
                switchable:false
            }, {
                field: 'prizename',
                title: '奖品名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'prizetype',
                title: '奖品类型',
                align: 'center',
                valign: 'middle',
				formatter: prizeFormatter,
                sortable: true
            },  {
				field: 'counts',
				title: '数额',
				align: 'center',
				valign: 'middle',
				sortable: true
			},  {
				field: 'effectivetime',
				title: '奖品生效时间',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
                field: 'terminaltime',
                title: '奖品失效时间',
                align: 'center',
                valign: 'middle',
                sortable: true
            }
			, {
                field: 'effectivedays',
                title: '有效天数',
                align: 'center',
                valign: 'middle',
                sortable: true
            },{
					field: 'status',
					title: '奖品状态',
					align: 'center',
					formatter: prizeStatusFormatter,
					valign: 'middle',
					sortable: true
				},{
					field: 'autouse',
					title: '使用方式',
					align: 'center',
					formatter: prizeAutoUseFormatter,
					valign: 'middle',
					sortable: true
				},
				{
					field: 'noti',
					title: '备注',
					align: 'center',
					valign: 'middle'
				},
				{
					field: 'operating',
					title: '操作',
					align: 'left',
					valign: 'middle',
					switchable: false,
					formatter: operateFormatter1,
					events: operateEvents1
				}]
        });
		$('#conditionslist').hide();
		//活动规则列表
		$('#conditions-table-list').bootstrapTable({
			method: 'get',
			url: 'queryPrizeConditionListById?conditionsource="03"&&id='+0,
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
		
		
		
		//
		
		
		 $("#q_terminaltime").datetimepicker({
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
		 
		 $("#q_effectivetime").datetimepicker({
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
		 });

		 
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
		      pickerPosition: "bottom-right",
		      pickTime:true,
		      todayBtn:true,
		      pickDate:true,
		      autoclose:true
		 });
		 
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
		 });

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
			pickerPosition: "bottom-right",
			pickTime:true,
			todayBtn:true,
			pickDate:true,
			autoclose:true
		});

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
		});

		//配置奖品规则事件
		$("#i_condition_execButton").click(function () {
			if($("#condition_paramvalue").val().trim() ==""){
				alertmsg("参数值不能为空!");
				return false;
			}
			$.ajax({
				url: "saveMarketPrizeCondition",
				type: 'post',
				dataType: "text",
				cache: false,
				async: true,
				data: {
					sourceid: $("#condition_prizeid").val(),
					conditionsource: '03',//奖品规则配置
					paramname: $("#condition_paramname").val(),
					expression: $("#condition_expression").val(),
					paramvalue: $("#condition_paramvalue").val()
				},
				error: function () {
					alertmsg("Connection error");
				},
				success: function (data) {
					//if (data == "success") {
					$('#insertMarketPrizeConditionModel').modal('hide');
					$("#querycondbutton").click();
					alertmsg("操作成功");
					//}
					//else
					//	alertmsg("操作失败");
				}
			});
		});
		//配置奖品规则结束<!--add-->
		//编辑奖品规则事件
		$("#u_condition_execButton").click(function () {
			if($("#u_paramvalue").val().trim() ==""){
				alertmsg("参数值不能为空!");
				return false;
			}
			$.ajax({
				url: "saveMarketPrizeCondition",
				type: 'post',
				dataType: "text",
				cache: false,
				async: true,
				data: {
					id:$("#u_id").val(),
					sourceid: $("#u_prizeid").val(),
					conditionsource: '03',
					paramname: $("#u_paramname").val(),
					expression: $("#u_expression").val(),
					paramvalue: $("#u_paramvalue").val()
				},
				error: function () {
					alertmsg("Connection error");
				},
				success: function (data) {
					//if (data == "success") {
					$('#updateMarketPrizeConditionModel').modal('hide');
					$("#querycondbutton").click();
					alertmsg("操作成功");
					//}
					//else
					//	alertmsg("操作失败");
				}
			});
		});
		//编辑奖品规则结束

		$("#querycondbutton").on("click", function(e) {
			var id= $("#temp_prizeid").val();
			$('#conditions-table-list').bootstrapTable(
				'refresh',
				{url:'queryPrizeConditionListById?limit=10&conditionsource=03&id='+id
				});
		});
		// 用户列表页面【查询】按钮
		$("#querybutton").on("click", function(e) {
			$('#conditionslist').hide();
			/**
			var prizename= $("#q_prizename").val();
			var prizetype= $("#q_prizetype").val();
			var status= $("#q_status").val();
			var effectivetime= $("#q_effectivetime").val();
			var terminaltime= $("#q_terminaltime").val();
			**/
			$('#table-list').bootstrapTable(
					'refresh',
					{url:'queryMarketPrizeList?'+$("#policymanagent").serialize()
					});
		 });
		
		//查看详细 ，获得选中行，只能选择一个
		$("#save").on("click",function(e){
	    	$("#i_prizename").val('');
	    	$("#i_prizetype").val('');
	    	$("#i_counts").val('');
	    	$("#i_effectivetime").val('');
	    	$("#i_terminaltime").val('');
	    	$("#i_effectivedays").val('');
			$("#i_status").val('');
			$("#i_autouse").val('');
			$("#i_noti").val('');
			$('#insertMarketPrizeModal').modal('show');
		});

       $("#i_execButton").click(function(){
		   if($("#i_prizename").val().trim() == ""){
			   alertmsg("奖品名称不能为空");
			   return false;
		   }
		   if($("#i_effectivetime").val().trim() == ""){
			   alertmsg("奖品生效日期不能为空");
			   return false;
		   }
		   if($("#i_terminaltime").val().trim() == ""){
			   alertmsg("奖品失效日期不能为空");
			   return false;
		   }
		   if(!/^[0-9]*$/.test($("#i_counts").val())){
			   alertmsg("奖品数量必须为数字");
			   return false;
		   }
		   if(!/^[0-9]*$/.test($("#i_effectivedays").val())){
			   alertmsg("奖品有效天数必须为数字");
			   return false;
		   }
    	   $.ajax({
    		    type: "POST",
    		    dataType: "text",
    		    url:'save',
    		    data:{
					prizename: $("#i_prizename").val(),
			        prizetype: $("#i_prizetype").val(),
			        counts: $("#i_counts").val(),
			        effectivetime: $("#i_effectivetime").val(),
			        terminaltime: $("#i_terminaltime").val(),
			        effectivedays: $("#i_effectivedays").val(),
				    status: $("#i_status").val(),
					autouse: $("#i_autouse").val(),
				    noti: $("#i_noti").val()
				},// 要提交表单的ID
			   error: function () {
				   alertmsg("Connection error");
			   },
    		    success: function(msg){
					alertmsg("操作成功");
    		    	 $('#insertMarketPrizeModal').modal('hide');
					$("#querybutton").click();
    		    }
    		});
    	//   $("#form1").submit();
    	  
		})	;

		$("#s_execButton").click(function(){
			if($("#s_prizename").val().trim() == ""){
				alertmsg("奖品名称不能为空");
				return false;
			}
			if($("#s_effectivetime").val().trim() == ""){
				alertmsg("奖品生效日期不能为空");
				return false;
			}
			if($("#s_terminaltime").val().trim() == ""){
				alertmsg("奖品失效日期不能为空");
				return false;
			}
			if(!/^[0-9]*$/.test($("#s_counts").val())){
				alertmsg("奖品数量必须为数字");
				return false;
			}
			if(!/^[0-9]*$/.test($("#s_effectivedays").val())){
				alertmsg("奖品有效天数必须为数字");
				return false;
			}
			$.ajax({
				type: "POST",
				dataType: "text",
				url:'save',
				data:{
					id:$("#s_id").val(),
					prizename: $("#s_prizename").val(),
					prizetype: $("#s_prizetype").val(),
					counts: $("#s_counts").val(),
					effectivetime: $("#s_effectivetime").val(),
					terminaltime: $("#s_terminaltime").val(),
					effectivedays: $("#s_effectivedays").val(),
					status: $("#s_status").val(),
					autouse: $("#s_autouse").val(),
					noti: $("#s_noti").val()
				},// 要提交表单的ID
				error: function () {
					alertmsg("Connection error");
				},
				success: function(msg) {
					if (msg == "fail") {
                        alertmsg("请先配置奖品使用规则！");
					} else {
						alertmsg("修改成功！");
						$('#updateMarketPrizeModal').modal('hide');
						$('#table-list').bootstrapTable('refresh');
				}
				}
			});
			//   $("#form1").submit();

		})	;

		$("#i_closeProid").click(function(){
			$('#insertMarketPrizeModal').modal('hide');
		});

		$("#s_closeProid").click(function(){
			$('#updateMarketPrizeModal').modal('hide');
		});

		//配置奖品规则按钮
		$("#insertMarketPrizeCondition").on("click", function(e) {
			$('#insertMarketPrizeConditionModel').modal('show');
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
			$('#conditionslist').hide();
		});
		//卡片列表
		$("#toggle").on("click", function(e) {
			$('#table-list').bootstrapTable('toggleView');
			$('#conditionslist').hide();
		});
		//刷新
		$("#refresh").on("click", function(e) {
			$('#table-list').bootstrapTable('refresh');
			$('#conditionslist').hide();
		});

	});

});
//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "queryMarketPrizeList";
function operateFormatter1(value, row, index) {
	return row.status =="0" ? [
		'<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
		'<i class="glyphicon glyphicon-edit"></i>',
		'</a>',
		'<a class="conditions m-left-5" href="javascript:void(0)" title="规则列表">',
		'<i class="glyphicon glyphicon-list"></i>',
		'</a>',
		'<a class="delete m-left-5" href="javascript:void(0)" title="删除">',
		'<i class="glyphicon glyphicon-remove"></i>',
		'</a>'
	    ].join(''):
		[
			'<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
		    '<i class="glyphicon glyphicon-edit"></i>',
			'</a>',
			'<a class="conditions m-left-5" href="javascript:void(0)" title="规则列表">',
		    '<i class="glyphicon glyphicon-list"></i>',
			'</a>'
		].join('');
}

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
		$("#s_prizename").val(row.prizename);
		$("#s_prizetype").val(row.prizetype);
		$("#s_counts").val(row.counts);
		$("#s_effectivetime").val(row.effectivetime);
		$("#s_terminaltime").val(row.terminaltime);
		$("#s_effectivedays").val(row.effectivedays)
		$("#s_status").val(row.status);
		$("#s_autouse").val(row.autouse);
		$("#s_noti").val(row.noti);
		$('#updateMarketPrizeModal').modal('show');
	},
	'click .delete' : function(e, value, row, index) {
		deletePrize(row.id);
	},
	'click .conditions' : function(e, value, row, index) {
		prizeConditions(row.id,row.prizename,row.prizetype,row.status);
	}
};

//获取奖品规则refreshrefresh
function prizeConditions(id,name,type,status){
	if(status!="0"){
		$("#insertMarketPrizeCondition").attr("disabled","ture");
	}else{
		$("#insertMarketPrizeCondition").removeAttr("disabled");
	}
	$('#conditionslist').show();
	$('#temp_prizeid').val(id);
	$('#temp_prizename').val(name);
	$('#temp_prizetype').val(prizeFormatter(type,'',''));
	$('#temp_status').val(prizeStatusFormatter(status,'',''));
	$('#condition_prizeid').val(id);
	//$('#condition_prizename').val(name);
	$.ajax({
		url : "queryPrizeConditionListById",
		type : 'get',
		dataType : "json",
		data : {
			id:id,
			conditionsource:'03'
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

function deletePrize(id) {
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
			}else if(data=="delConditionFirst"){
				alertmsg("请先删除奖品使用规则！");
			}
			 else{
				alertmsg("该奖品已经在活动中配置，无法删除！");
			}

		}
	});
}

function operateFormatter3(value, row, index) {
	return $('#temp_status').val() != "编辑" ? [].join(''):[
		'<a class="editPrizeCondition m-left-5" href="javascript:void(0)" title="编辑">',
		'<i class="glyphicon glyphicon-edit"></i>',
		'</a>',
		'<a class="deletePrizeCondition m-left-5" href="javascript:void(0)" title="删除">',
		'<i class="glyphicon glyphicon-remove"></i>',
		'</a>'
	].join('');
}

//删除活动规则
window.operateEvents3 = {
	'click .deletePrizeCondition' : function(e, value, row, index) {
		var id = row.id;
		$.ajax({
			url : "deletePrizeCondition",
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
				var id= $("#temp_prizeid").val();
				$('#conditions-table-list').bootstrapTable(
					'refresh',
					{url:'queryPrizeConditionListById?limit=10&conditionsource=03&id='+id
					});
			}
		});
	},
	'click .editPrizeCondition' : function(e, value, row, index) {
		$("#u_id").val(row.id);
		//$("#u_prizename").val($("#temp_prizename").val());
		//$("#u_paramdatatype").val(row.paramdatatype);
		$("#u_paramname").val(row.paramname);
		$("#u_expression").val(row.expression);
		$("#u_paramvalue").val(row.paramvalue);
		//$("#u_noti").val(row.noti);
		$('#updateMarketPrizeConditionModel').modal('show');
	}
}

//奖品类型
function prizeFormatter(value, row, index) {
	if(value=='01'){
		return '奖券';
	}else{
		return '奖金';
	}
}
//奖品状态
function prizeStatusFormatter(value, row, index) {
	if(value=='1'){
		return '开启';
	}else if(value=='2'){
		return '关闭';
	}else
	{
		return '编辑';
	}
}
//奖品状态
function prizeAutoUseFormatter(value, row, index) {
	if(value=='1'){
		return '自动';
	}else
	{
		return '手动';
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

