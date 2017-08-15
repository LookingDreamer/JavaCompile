require(["jquery", "bootstrapdatetimepicker", "zTree","bootstrap-table", "bootstrap","bootstrapTableZhCn", "bootstrapdatetimepickeri18n","public"], function ($) {
	//数据初始化  
	$(function() {
		$('.form_datetime').datetimepicker({
		    language: 'zh-CN',
		    format: "yyyy-mm-dd",
		    weekStart: 1,
		    todayBtn: 1,
		    autoclose: 1, 
		    todayHighlight: 1,
		    startView: 2,
		    forceParse: 0,
		    minView: 2,
		    showMeridian: 0
		});
		$('#table-javascript').bootstrapTable({
            method: 'get',
            url: "",
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server', 
            pageSize: pagesize,
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
                field: 'taskid',
                title: '任务跟踪号',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'prvshotname',
                title: '保险公司',
                align: 'center',
                valign: 'middle',
                sortable: true
            },{
                field: 'carlicenseno',
                title: '车牌',
                align: 'center',
                valign: 'middle',
                sortable: true
            },  {
                field: 'createtime',
                title: '创建时间',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'operating',
                title: '关闭任务操作',
                align: 'center',
                valign: 'middle',
                sortable: true
            }]
        });
		
		// 错误信息页面【查询】按钮
		$("#querybutton").on("click", function(e) {
			//错误信息查询
			var taskid = $("#taskid").val();
			var inscomcode =$("#inscomcode").val();
			if($.trim($("#taskcreatetimeup").val()) && $.trim($("#taskcreatetimedown").val())){
				var timeupstr = ($.trim($("#taskcreatetimeup").val()).substring(0,11) + "00:00:00").replace(/-/g,"/");
				var timedownstr = ($.trim($("#taskcreatetimedown").val()).substring(0,11) + "23:59:59").replace(/-/g,"/");
				var timeup = new Date(timeupstr);
				var timedown = new Date(timedownstr);
				if(timeup >= timedown){
					alertmsg("任务创建截止时间应晚于开始时间");
					return;
				}
			}
			$('#table-javascript').bootstrapTable(
						'refresh',
						{url:'showclosstaskinfo?limit=10&taskid='+taskid
							+'&inscomcode='+inscomcode+'&taskcreatetimeup='+$.trim($("#taskcreatetimeup").val()).substring(0,10).trim() + " 00:00:00"
							+'&taskcreatetimedown='+$.trim($("#taskcreatetimedown").val()).substring(0,10).trim() + " 23:59:59"});
		 });
		
		// 查询条件【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#taskid").val("");
			$("#inscomcode").val("");
			$("#inscomname").val("");
			$("#taskcreatetimeup").val("");
			$("#taskcreatetimedown").val("");
		});
		//卡片列表
		$("#toggle").on("click", function(e) {
			$('#table-javascript').bootstrapTable('toggleView');
		});
		//刷新
		$("#refresh").on("click", function(e) {
			$('#table-javascript').bootstrapTable('refresh');
		});
		
		//点击弹出供应商选择页面
		var comsetting = {
			async : {
				enable : true,
				url : "/cm/provider/queryprotree",
				autoParam : [ "id" ],//每次重新请求时传回的参数
				dataType : "json",
				type : "post"
			},
			check : {
				enable : true,
				chkStyle : "radio",
				radioType : "all"
			},
			callback : {
				onClick : zTreeOnCheckCom,//回调函数
				onCheck : zTreeOnCheckCom//回调函数
			}
		};
		//选择后回调函数
		function zTreeOnCheckCom(event, treeId, treeNode) {
			$("#inscomcode").val(treeNode.prvcode);
			$("#inscomname").val(treeNode.name);
			$('#showpic').modal("hide");
		}
		//点击弹出供应商选择页面
		$("#inscomname").on("click", function(e) {
			$('#showpic').modal();
			$.fn.zTree.init($("#treeDemo"), comsetting);
		});
		$(".closeshowpic").on("click", function(e) {
			$('#showpic').modal("hide");
		});

		initQueryTaskDate();
	});
});
//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "showclosstaskinfo";
//刷新列表
function reloaddata(data){
	$.ajax({
		url : "closstask",
		type : 'GET',
		dataType : "json",
		data: data+"&limit="+pagesize,
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#table-javascript').bootstrapTable('load', data);
		}
	});
}
function closstask(data){
	$.ajax({
		url : "/cm/policyInterface/closstask?"+data,
		type : 'GET',
		dataType : "json",
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function() {
			alertmsg("关闭成功");
			reloaddata("");
		}
	});
}
// 时间控件初始化为今天
function initQueryTaskDate(){
	var date = new Date();
	var todayMounth = (date.getMonth()+1)+"";
	if(todayMounth.length<2){
		todayMounth = "0"+todayMounth;
	}
	var todayDate = date.getDate()+"";
	if(todayDate.length<2){
		todayDate = "0"+todayDate;
	}
	var dateString = date.getFullYear()+"-"+todayMounth+"-"+todayDate;
	$("#taskcreatetimeup").val(dateString);
	$("#taskcreatetimedown").val(dateString);
}
