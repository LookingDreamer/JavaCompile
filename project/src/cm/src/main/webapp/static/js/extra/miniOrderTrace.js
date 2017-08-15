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
                field: 'id',
                title: 'ID',
                align: 'center',
                valign: 'middle',
				visible: false,
                sortable: true
                
            },{
				field: 'providercode',
				title: 'providercode',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true

			},{
				field: 'reasonmsg',
				title: 'reasonmsg',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			},{
				field: 'reason',
				title: 'reason',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			},   {
                field: 'taskid',
                title: '任务号',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
				field: 'agentname',
				title: '客户姓名',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'phone',
				title: '手机号码',
				align: 'center',
				valign: 'middle',
				sortable: true
			},{
				field: 'insuredname',
				title: '被保人姓名',
				align: 'center',
				valign: 'middle',
				sortable: true
			},{
				field: 'carowner',
				title: '车主姓名',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'carlicenseno',
				title: '车牌号码',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'prvname',
				title: '投保公司',
				align: 'center',
				valign: 'middle',
				sortable: true
			},{
				field: 'premium',
				title: '保费金额',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'paytime',
				title: '支付时间',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'starttime',
				title: '起保时间',
				align: 'center',
				valign: 'middle',
				sortable: true
			},{
				field: 'taskstate',
				title: '订单状态',
				align: 'center',
				valign: 'middle',
				sortable: true,
			    formatter:taskstateFormatter
			}, {
				field: 'operatestate',
				title: '处理状态',
				align: 'center',
				valign: 'middle',
				sortable: true,
				formatter:operatestateFormatter
			}
			, {
					field: 'operating',
					title: '操作',
					align: 'left',
					valign: 'middle',
					width: 200,
					switchable: false,
					formatter: operateFormatter1,
					events: operateEvents1
				}
				]
        });

		//<!--add-->
		 $("#createtimeend").datetimepicker({
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
		 
		 $("#createtimestart").datetimepicker({
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
		 }) ;


		$("#querybutton").on("click", function(e) {
			$('#table-list').bootstrapTable('refresh',{url:'queryOrderList?'+$("#orderTraceManagement").serialize()});
		});

		$('#operateModel').modal('hide');
		$('#msgModel').modal('hide');
		$('input[name="operatetype"]').change(function(e){

		});

		$("#execButton").on("click", function(e) {
			var operateval = $('input[name="operatetype"]:checked').val();
			var operatestate = operateval;
			var reason =  $('#refundReason').val();
			if(reason=='' && operatestate!= '1'){
				alertmsg("请选择退款原因！");
				return ;
			}
			var id = $('#orderTraceId').val();
			var taskid = $('#operateTaskId').val();
			var taskstate = $('#operateTaskState').val();
			var providercode = $('#operateProvidercode').val();
			$.ajax({
				url: "updateOrderOperateState",
				type: 'post',
				dataType: "json",
				cache: false,
				async: true,
				data: {
					id:id,
					taskid:taskid,
					taskstate:taskstate,
					reason: reason,
					providercode:providercode,
					operatestate: operatestate
				},
				error: function () {
					alertmsg("Connection error");
				},
				success: function (data) {
					//if (data == "success") {
					$('#operateModel').modal('hide');
					alert(data.result);
					$("#querybutton").click();
					//}
					//else
					//	alertmsg("操作失败");
				}
			});
		});

		$('#waitUpLoad').on("click",function(){
			$('#refundReason').val('');
			$('#refundReasonTr').hide();
		});
		$('#waitRefund').on("click",function(){
			$('#refundReasonTr').show();
		});
		$('#immediateRefund').on("click",function(){
			$('#refundReasonTr').show();
		});
		
//		返回
		$('#backbutton').on("click",function(){
			history.go(-1)
		});
		// 查询条件【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#taskid").val("");
			$("#agentname").val("");
			$("#taskstate").val("");
			$("#carlicenseno").val("");
			$("#carowner").val("");
			$("#operatestate").val("");
			$("#createtimestart").val("");
			$("#createtimeend").val("");
			$("#startdateflag").val("");
			
		});
		//卡片列表
		$("#toggle").on("click", function(e) {
			$('#table-list').bootstrapTable('toggleView');
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
var pageurl = "queryOrderList";

// 添加事件
function operateFormatter1(value, row, index) {
	if (row.taskstate == "19" && row.operatestate == "0"){
        return [
			'<a class="trace m-left-5" href="javascript:void(0)" title="跟进处理">',
			'跟进处理',
			'</a> <br/>',
			'<a class="error m-left-5" href="javascript:void(0)" title="查看失败原因">',
			'查看失败原因',
			'</a>'
		].join('');
	}else if (row.taskstate == "19" && row.operatestate == "1"){
		return [
			'<a class="trace m-left-5" href="javascript:void(0)" title="跟进处理">',
			'跟进处理',
			'</a> <br/>',
			'<a class="error m-left-5" href="javascript:void(0)" title="查看失败原因">',
			'查看失败原因',
			'</a>'
		].join('');
	}else if (row.taskstate == "19" && row.operatestate == "2"){
		return [
			'<a class="trace m-left-5" href="javascript:void(0)" title="跟进处理">',
			'跟进处理',
			'</a> <br/>',
			'<a class="refund m-left-5" href="javascript:void(0)" title="查看退款理由">',
			'查看退款理由',
			'</a>'
		].join('');
	}else if (row.taskstate == "19" && row.operatestate == "3"){
		return [
			'<a class="refund m-left-5" href="javascript:void(0)" title="查看退款理由">',
			'查看退款理由',
			'</a>'
		].join('');
	}else if (row.taskstate == "5"  && (row.operatestate == "0"||row.operatestate == "1")){
		return [
			'<a class="trace m-left-5" href="javascript:void(0)" title="跟进处理">',
			'跟进处理',
			'</a>'
		].join('');
	}else if (row.taskstate == "5"  && (row.operatestate == "2"||row.operatestate == "3")){
		return [
			'<a class="trace m-left-5" href="javascript:void(0)" title="跟进处理">',
			'跟进处理',
			'</a> <br/>',
			'<a class="refund m-left-5" href="javascript:void(0)" title="查看退款理由">',
			'查看退款理由',
			'</a>'
		].join('');
	} else if (row.taskstate == "12" || row.taskstate == "13" ){
		return [
			'<a class="refund m-left-5" href="javascript:void(0)" title="查看退款理由">',
			'查看退款理由',
			'</a>'
		].join('');
	}else {
		return [
			''
		].join('');
	}
}


// 事件相应
window.operateEvents1 = {
	'click .trace' : function(e, value, row, index) {
		$("#orderTraceId").val(row.id);
		$("#operateTaskId").val(row.taskid);
		$("#operateTaskState").val(row.taskstate);
		$("#operateProvidercode").val(row.providercode);
		var starttime = row.starttime;
		var currdate = date2str(new Date(),"yyyy-MM-dd");
		if(row.taskstate == '19' || starttime <= currdate ){
			$('#waitUpLoadSpan').hide();
			$('#waitRefund').click();
		}else{
			$('#waitUpLoadSpan').show();
		}
		$('#msgModel').modal('hide');
		$('#operateModel').modal('show');

	},
	'click .refund' : function(e, value, row, index) {
		var msg = row.reasonmsg;
		if(msg==undefined){
			msg='';
		}
		if(row.reason=='5'){
		  msg = '用户已由上传意向转变为申请退款';
		}else if(row.reason=='6'){
			msg = '该笔订单用户主动申请了退款';
		}
		$('#operateModel').modal('hide');
		$('#msgHead').html('退款理由');
		$('#showmsg').html('<p>'+msg+'</p>');
		$('#msgModel').modal('show');

	},
	'click .error' : function(e, value, row, index) {
		var msg = row.msg;
		if(msg==undefined){
			msg='';
		}
		$('#operateModel').modal('hide');
		$('#msgHead').html('核保失败原因');
		$('#showmsg').html('<p>'+msg+'</p>');
		$('#msgModel').modal('show');
	}
};

function date2str(x,y) {
	var z = {M:x.getMonth()+1,d:x.getDate(),h:x.getHours(),m:x.getMinutes(),s:x.getSeconds()};
	y = y.replace(/(M+|d+|h+|m+|s+)/g,function(v) {return ((v.length>1?"0":"")+eval('z.'+v.slice(-1))).slice(-2)});
	return y.replace(/(y+)/g,function(v) {return x.getFullYear().toString().slice(-v.length)});
}
//状态名称文字转换
function taskstateFormatter(value, row, index) {
	if(value=='5'){
		return '待补齐影像';
	}else if(value=='19'){
		return '核保失败';
	}else if(value=='12'){
		return '退款中';
	}else if(value=='13'){
		return '已退款';
	}else if(value=='6'){
		return '待承保';
	}else if(value=='11'){
		return '待配送';
	}else if(value=='15'){
		return '已完成';
	}else if(value=='14'){
		return '已关闭';
	}else if(value=='18'){
		return '承保政策限制';
	}else if(value=='22'){
		return '拒绝承保';
	}else{
		return '待承保';
	}
}


//活动类型文字转换
function operatestateFormatter(value, row, index) {
	if(value=='0'){
		return '待处理';
	}else if(value=='1'){
		return '待用户上传';
	}else if(value=='2'){
		return '待用户申请退款';
	}else if(value=='3'){
		return '已发起退款';
	}else if(value=='4'){
		return '用户已发起退款';
	}else if(value=='5'){
		return '待完成交易';
	}else if(value=='6'){
		return '已促成交易';
	}
	else{
		return '待处理';
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

