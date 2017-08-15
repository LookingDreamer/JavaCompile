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
				field: 'tempcode',
				title: 'tempcode',
				visible: false,
				switchable:false
			},
				{
                field: 'channelcode',
                title: '渠道编码',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
				{
				field: 'channelname',
				title: '渠道名称',
				align: 'center',
				valign: 'middle',
				sortable: true
			},
				{
				field: 'waynum',
				title: '推广途径数量',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
                field: 'terminaldate',
                title: '截止时间',
                align: 'center',
                valign: 'middle',
                sortable: true
            },{
					field: 'noti',
					title: '渠道描述',
					align: 'center',
					valign: 'middle',
					sortable: true
				}
			, {
                field: 'createtime',
                title: '创建时间',
                align: 'center',
                valign: 'middle',
                sortable: true
            },{
					field: 'operator',
					title: '创建人',
					align: 'center',
					valign: 'middle',
					sortable: true
				},
				{
					field: 'operating',
					title: '操作',
					align: 'left',
					valign: 'middle',
					switchable: false,
					formatter: operateFormatter1,
					events: operateEvents1
				}
				]
        });

		//渠道推广途径
		$('#channelway-table-list').bootstrapTable({
			method: 'get',
			url: 'queryChannelWaylistByCode?channelcode='+0,
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
				field: 'channelcode',
				title: '渠道编码',
				visible: false,
				switchable:false
			}, {
				field: 'wayname',
				title: '推广途径名称',
				switchable:false,
				sortable: true
			},
				{
				field: 'waycode',
				title: '推广途径编号',
				align: 'center',
				valign: 'middle',
				sortable: true
			},
				{
				field: 'noti',
				title: '备注',
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
					field: 'operator',
					title: '创建人',
					align: 'center',
					valign: 'middle',
					sortable: true
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
		//渠道推广途径结束


		$('#channelwaylist').hide();
		$('#querybuttonhide').hide();
		//<!--add-->
		 $("#terminaldate").datetimepicker({
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

		 
		  $("#i_terminaldate").datetimepicker({
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


		$("#s_terminaldate").datetimepicker({
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


		//修改渠道
		$("#s_execButton").click(function () {
			if($("#s_channelname").val().trim() == ""){
				alertmsg("渠道名称不能为空");
				return false;
			}
			if($("#s_terminaldate").val().trim() == ""){
				alertmsg("截止日期不能为空");
				return false;
			}
			saveChannel();
		});
		//新建渠道
		$("#i_execButton").click(function () {
			if($("#i_channelname").val().trim() == ""){
				alertmsg("渠道名称不能为空");
				return false;
			}
			if($("#i_terminaldate").val().trim() == ""){
				alertmsg("渠道截止日期不能为空");
				return false;
			}
			$.ajax({
				url: "saveChannel",
				type: 'post',
				dataType: "text",
				cache: false,
				async: true,
				data: {
					channelname: $("#i_channelname").val(),
					terminaldate: $("#i_terminaldate").val(),
					noti: $("#i_noti").val()
				},
				error: function () {
					alertmsg("Connection error");
				},
				success: function (data) {
					//if (data == "success") {
						$('#insertChannelModel').modal('hide');
						$("#querybutton").click();
						alertmsg("操作成功!");
					//}
					//else
					//	alertmsg("操作失败");
				}
			});
		});
		//新建渠道结束

		//新建渠道推广途径
		$("#i_way_execButton").click(function () {
			if($("#i_wayname").val().trim() == ""){
				alertmsg("推广途径名称不能为空");
				return false;
			}
			$.ajax({
				url: "saveChannelWay",
				type: 'post',
				dataType: "json",
				cache: false,
				async: true,
				data: {
					channelcode: $("#i_waychannelcode").val(),
					wayname: $("#i_wayname").val(),
					noti: $("#i_waynoti").val()
				},
				error: function () {
					alertmsg("Connection error");
				},
				success: function (data) {
					//if (data == "success") {
					$('#insertChannelWayModel').modal('hide');
					$("#querychannelwaybutton").click();
					$("#querybuttonhide").click();
					alertmsg("操作成功!");
					//}
					//else
					//	alertmsg("操作失败");
				}
			});
		});
		//新建渠道推广途径结束

		//修改渠道
		$("#w_execButton").click(function () {
			if($("#w_wayname").val().trim() == ""){
				alertmsg("推广途径名称不能为空");
				return false;
			}
			saveChannelWay();
		});

		// 渠道列表页面【查询】按钮
		$("#querybutton").on("click", function(e) { 
			$('#channelwaylist').hide();
			/**
			var channelname= $("#channelname").val();
			var channelcode= $("#channelcode").val();
			var terminaldate= $("#terminaldate").val();
			*&*/
			$('#table-list').bootstrapTable(
					'refresh',
					{url:'queryChannelList?'+$("#policymanagent").serialize()
					});
		 });

		// 渠道列表页面【查询】按钮
		$("#querybuttonhide").on("click", function(e) {
			//$('#channelwaylist').hide();
			var channelname= $("#channelname").val();
			var channelcode= $("#channelcode").val();
			var terminaldate= $("#terminaldate").val();
			$('#table-list').bootstrapTable(
				'refresh',
				{url:'queryChannelList?limit=10&channelname='+channelname+'&channelcode='+channelcode
				+'&terminaldate='+terminaldate
				});
		});

		// 渠道推广途径列表页面【查询】按钮
		$("#querychannelwaybutton").on("click", function(e) {
			var channelcode= $("#temp_channelcode").val();
			$('#channelway-table-list').bootstrapTable(
				'refresh',
				{url:'queryChannelWaylistByCode?limit=10&channelcode='+channelcode
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

		//新建渠道按钮
		$("#insertMiniChannel").on("click", function(e) {
			$('#insertChannelModel').modal('show');
		});
		//新建渠道推广途径
		$("#insertMiniChannelWay").on("click", function(e) {
			$('#insertChannelWayModel').modal('show');
		});
		//新建渠道推广途径
		$("#insertMiniChannelWay2").on("click", function(e) {
			$('#insertChannelWayModel').modal('show');
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
var pageurl = "queryChannelList";

// 添加事件
function operateFormatter1(value, row, index) {
	return  [
		'<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
		'<i class="glyphicon glyphicon-edit"></i>',
		'</a>',
		'<a class="channelway m-left-5" href="javascript:void(0)" title="推广途径">',
		'<i class="glyphicon glyphicon-list"></i>',
		'</a>',
		'<a class="delete m-left-5" href="javascript:void(0)" title="删除">',
		'<i class="glyphicon glyphicon-remove"></i>',
		'</a>'
	].join('');
}
// 事件相应
window.operateEvents1 = {
	'click .edit' : function(e, value, row, index) {
		$(".readonlysetting").attr("readonly","readonly");
		$(".disabledsetting").attr("disabled","ture");
		$("#s_id").val(row.id);
		$("#s_channelcode").val(row.channelcode);
		$("#s_tempcode").val(row.tempcode);
		$("#s_waynum").val(row.waynum);
		$("#s_channelname").val(row.channelname);
		$("#s_terminaldate").val(row.terminaldate);
		$("#s_noti").val(row.noti);
		$('#updateChannelModel').modal('show');
	},
	'click .delete' : function(e, value, row, index) {
		deleteChannel(row.id,row.channelcode);
	},
	'click .channelway' : function(e, value, row, index) {
		channelWay(row.id,row.channelname,row.channelcode,row.terminaldate);
	}
};
function operateFormatter2(value, row, index) {
	return  [
		'<a class="editChannelWay m-left-5" href="javascript:void(0)" title="编辑">',
		'<i class="glyphicon glyphicon-edit"></i>',
		'</a>',
		'<a class="deleteChannelWay m-left-5" href="javascript:void(0)" title="删除">',
		'<i class="glyphicon glyphicon-remove"></i>',
		'</a>'
	].join('');
}

//推广途径
window.operateEvents2 = {
	'click .editChannelWay' : function(e, value, row, index) {
		$("#w_id").val(row.id);
		$("#w_channelcode").val(row.channelcode);
		$("#w_wayname").val(row.wayname);
		$("#w_waycode").val(row.waycode);
		$("#w_noti").val(row.noti);
		$('#updateChannelWayModel').modal('show');
	},
	'click .deleteChannelWay' : function(e, value, row, index) {
		var id = row.id;
		var channelcode = row.channelcode;
		$.ajax({
			url : "deleteChannelWay",
			type : 'POST',
			dataType : "json",
			data : {
				id:id,
				channelcode:channelcode
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
				return null;
			},
			success : function(data) {
				alertmsg("success!!");
				var channelcode= $("#temp_channelcode").val();
				$('#channelway-table-list').bootstrapTable(
					'refresh',
					{url:'queryChannelWaylistByCode?limit=10&channelcode='+channelcode
					});
				$("#querybuttonhide").click();
			}
		});
	}
}

//获取推广途径列表
function channelWay(id,name,code,terminaldate){
	$('#channelwaylist').show();
	$('#temp_terminaldate').val(terminaldate);
	$('#temp_channelname').val(name);
	$('#temp_channelcode').val(code);
	$('#i_waychannelcode').val(code);
	//获取推广途径列表
	$.ajax({
		url : "queryChannelWaylistByCode",
		type : 'get',
		dataType : "json",
		data : {
			channelcode:code
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
			return null;
		},
		success : function(data) {
			$('#channelway-table-list').bootstrapTable('load',data);
		}
	});
	//渠道推广路径结束
}

function deleteChannel(id,channelcode) {
	$.ajax({
		url : "deleteChannel",
		type : 'POST',
		dataType : "json",
		data : {
			id:id,
			channelcode:channelcode
		},
		async : true,
		error : function() {
			alertmsg("系统错误！");
			return null;
		},
		success : function(data) {
			if(data.result=="success"){
				alertmsg("删除成功!");
				$('#table-list').bootstrapTable('refresh');
			}else if(data.result=="hasdetail"){
				alertmsg("请先删除推广途径!");
			}
			else{
				alertmsg("删除失败！");
			}

		}
	});
	$('#channelwaylist').hide();
}

//保存渠道
function saveChannel(){
	$.ajax({
		url: "saveChannel",
		type: 'post',
		dataType: "json",
		cache: false,
		async: true,
		data: {
			id: $("#s_id").val(),
			channelname: $("#s_channelname").val(),
			channelcode: $("#s_channelcode").val(),
			waynum: $("#s_waynum").val(),
			tempcode: $("#s_tempcode").val(),
			terminaldate: $("#s_terminaldate").val(),
			noti: $("#s_noti").val()
		},
		error: function () {
			alertmsg("Connection error");
		},
		success: function (data) {
			if (data.result == "success") {
				$('#updateChannelModel').modal('hide');
				$("#querybutton").click();
				alertmsg("操作成功!");
			} else if (data.result == "fail") {
				alertmsg("操作失败");
			}
			else
				alertmsg("操作失败");
		}
	});
}
//保存渠道
function saveChannelWay(){
	$.ajax({
		url: "saveChannelWay",
		type: 'post',
		dataType: "json",
		cache: false,
		async: true,
		data: {
			id: $("#w_id").val(),
			wayname: $("#w_wayname").val(),
			channelcode: $("#w_channelcode").val(),
			waycode: $("#w_waycode").val(),
			createtime: $("#w_createtime").val(),
			noti: $("#w_noti").val()
		},
		error: function () {
			alertmsg("Connection error");
		},
		success: function (data) {
			if (data.result == "success") {
				$('#updateChannelWayModel').modal('hide');
				$("#querychannelwaybutton").click();
				alertmsg("操作成功!");
			} else if (data.result == "fail") {
				alertmsg("操作失败");
			}
			else
				alertmsg("操作失败");
		}
	});
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

