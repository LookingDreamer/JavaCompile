require(["jquery", "bootstrapdatetimepicker", "zTree","bootstrap-table", "bootstrap","bootstrapTableZhCn", "bootstrapdatetimepickeri18n","zTreecheck","flat-ui","public"], function ($) {
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
                field: 'inscomcode',
                title: '保险公司',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
            	field: 'comname',
            	title: '所属机构',
            	align: 'center',
            	valign: 'middle',
            	sortable: true
            }, {
                field: 'errordesc',
                title: '错误描述',
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
            	field: 'modifytime',
            	title: '更新时间',
            	align: 'center',
            	valign: 'middle',
            	sortable: true
            }, {
                field: 'operator',
                title: '操作人',
                align: 'center',
                valign: 'middle',
            	sortable: true
            }, {
                field: 'operating',
                title: '重新推送操作',
                align: 'center',
                valign: 'middle',
                sortable: true
            }]
        });
		 //时间控件初始化设置
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
	        pickerPosition: "bottom-left",
	        startDate:'-3m',
	        endDate:new Date()
	    });
	    // 时间控件初始化为今天
	    function initQueryTaskDate(){
	        var date = new Date();
	        var todayMounth = (date.getMonth()+1)+"";
	        if(todayMounth.length<2){
	            todayMounth = "0"+todayMounth;
	        }
			var days = date.getDate() + "";
			if(days.length < 2)
				days = "0" + days;
	        var dateString = date.getFullYear()+"-"+todayMounth+"-"+days;
	        $("#taskcreatetimeup").val(dateString);
	        $("#taskcreatetimedown").val(dateString);
	    }
	    initQueryTaskDate();
		// 错误信息页面【查询】按钮
		$("#querybutton").on("click", function(e) {
			if($.trim($("#taskcreatetimeup").val()) && $.trim($("#taskcreatetimedown").val())){
				var timeupstr = ($.trim($("#taskcreatetimeup").val()).substring(0,11) + " 00:00:00").replace(/-/g,"/");
				var timedownstr = ($.trim($("#taskcreatetimedown").val()).substring(0,11) + " 23:59:59").replace(/-/g,"/");
				var timeup = new Date(timeupstr);
				var timedown = new Date(timedownstr);
				if(timeup >= timedown){
					alertmsg("任务创建截止时间应晚于开始时间");
					return;
				}
			}
			var taskcreatetimeupvalue = $.trim($("#taskcreatetimeup").val());
			var taskcreatetimedownvalue = $.trim($("#taskcreatetimedown").val());
			var timeupdatestr = taskcreatetimeupvalue.substring(0,11);
			var timedowndatestr = taskcreatetimedownvalue.substring(0,11);
			
			var timeupdate = new Date((timeupdatestr + " 00:00:00").replace(/-/g,"/"));
			var timedowndate = new Date((timedowndatestr + " 00:00:00").replace(/-/g,"/"));
			var iDays = parseInt(Math.abs(timedowndate - timeupdate) / 1000 / 60 / 60 /24);
			if(iDays > 0) {
	        	alertmsg("任务创建时间暂时只开放1天的查询区间");
	        	return;
	        }
			//错误信息查询
			var taskid = $("#taskid").val();
			var inscomcode =$("#inscomcode").val();
			var firoredi =$("#firoredi").val(); 
			var selected_flowcode = $("#flowcode option:selected").attr("value");

			
			$('#table-javascript').bootstrapTable(
						'refresh',
						{url:'showpushtocoreinfo?limit=10&taskid='+taskid
							+'&inscomcode='+inscomcode+'&firoredi='+firoredi
							+'&flowcode='+selected_flowcode
							+'&taskcreatetimeup='+$.trim($("#taskcreatetimeup").val()).substring(0,10).trim() + " 00:00:00"
							+'&taskcreatetimedown='+$.trim($("#taskcreatetimedown").val()).substring(0,10).trim() + " 23:59:59"
							+'&deptcode='+$.trim($("#deptcode").val())});
		 });
		
		// 查询条件【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#taskid").val("");
			$("#inscomcode").val("");
			$("#inscomname").val("");
			$("#firoredi").val("");
			$("#flowcode").val("");
			initQueryTaskDate();
	        $("#deptcode").val("");
	        $("#deptname").val("");
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
		
		//点击弹出出单网点选择页面
		//----------------------------------------------------------
		var setting = {
		    async : {
		        enable : true,
		        url : "/cm/business/cartaskmanage/queryparttreecheckall",
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
//				onClick : zTreeOnCheckDept,//回调函数
		        onCheck : zTreeOnCheckDept//回调函数
		    }
		};
		//选择后回调函数
		function zTreeOnCheckDept(event, treeId, treeNode) {
		    $("#deptcode").val(treeNode.deptinnercode);
		    $("#deptname").val(treeNode.name);
		    $('#showDeptTree').modal("hide");
		}

		//点击弹出出单网点选择页面
		$("#deptname").on("click", function(e) {
		    $('#showDeptTree').modal();
		    $.fn.zTree.init($("#deptTreeDemo"), setting);
		});
		$(".closeShowDeptTree").on("click", function(e) {
		    $('#showDeptTree').modal("hide");
		});
		//-----------------------------------------------------------
		//修改起始时间
		$("#taskcreatetimeup").change(function(){
			var startString=$("#taskcreatetimeup").val();//获取当前显示的时间字符串
			var dateStartime=Date.parse(startString);//解析时间字符串
			var startime=new Date(dateStartime);//创建时间
			var endYear = startime.getFullYear();
			var endMonth=(startime.getMonth()+2)+"";//获取时间的月份,在月份的基础上加1个月.因为月份是从0开始,所以+2
			if (endMonth > 12) {//计算下个月的月份超过12则为下年1月
				endYear += 1;
				endMonth = endMonth - 12 + "";
			}
			var days = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);//平年每月天数
			var enday=startime.getDate()+"";//获取日份
			if (days[endMonth - 1] < enday) {//如果起始时间有31号下个月不一定有31号
				if ((endYear % 4 == 0 && endYear % 400 != 0 || endYear % 400 == 0) && endMonth == 2)//闰年2月
					enday = days[endMonth - 1] + 1;
				else
					enday = days[endMonth - 1];
			}
			if(endMonth.length<2){
				endMonth="0"+endMonth;
			}
			if(enday.length<2){
				enday="0"+enday;
			}
			var endString = endYear+"-"+endMonth+"-"+enday;//跨度一个月的截止时间字符串
			var dateEndTime=Date.parse(endString);
			var endTime=new Date(dateEndTime);//解析为时间类型
			if(endTime>new Date()){//若开始时间为今天,那么截止时间可以为下个月的今天,所以需要判读
				endTime=new Date();//如果最大可选时间大于当前的时间,则最大可选时间为当前时间
				var todayMonth=(endTime.getMonth()+1)+"";
				if(todayMonth.length<2){
					todayMonth="0"+todayMonth;
				}
				var todayDay=(endTime.getDate())+"";
				if(todayDay.length<2){
					todayDay="0"+todayDay;
				}
				endString=endTime.getFullYear()+"-"+todayMonth+"-"+todayDay;
			}
			$("#taskcreatetimedown").datetimepicker('setEndDate',endTime);
			var showEnd=$("#taskcreatetimedown").val();//获取当前的截止日期
			var dateShowEnd=Date.parse(showEnd);
			var currentEnd=new Date(dateShowEnd);
			var days=0;
			if(currentEnd>startime){
				days=(currentEnd-startime)/(1000*60*60*24);
			}else{
				days=(startime-currentEnd)/(1000*60*60*24);
			}
			if(days>30){//若页面显示的时间差超过一个月,则将显示的截止时间改为设置的截止时间
				$("#taskcreatetimedown").val(endString);
			}
		});
		
		//修改截止时间
		$("#taskcreatetimedown").change(function(){
			var endString=$("#taskcreatetimedown").val();
			var dateEnd=Date.parse(endString);
			var endTime=new Date(dateEnd);
			var startMonth=endTime.getMonth();
			if(startMonth==0){//当是一月的时候得到就是0,显示的时候就是0月.所以要+1
				startMonth=1;
			}
			startMonth=startMonth+"";
			if(startMonth.length<2){//改变月份显示为两位显示
				startMonth="0"+startMonth;
			}
			var startDay=endTime.getDate()+"";
			if(startDay.length<2){
				startDay="0"+startDay;
			}
			var startString=endTime.getFullYear()+"-"+startMonth+"-"+startDay;
			var startime=new Date(Date.parse(startString));
			var today=new Date();
			var beforeMonth=today.getMonth()-2;
			var beforeYear=today.getFullYear();
			if(beforeMonth<0){
				beforeMonth=13+beforeMonth;
				beforeYear=beforeYear-1;//往前推一年
			}
			beforeMonth=beforeMonth+"";
			if(beforeMonth.length<2){
				beforeMonth="0"+beforeMonth;
			}
			var beforeDay=today.getDate()+"";
			if(beforeDay.length<2){
				beforeDay="0"+beforeDay;
			}
			var beforeString=beforeYear+"-"+beforeMonth+"-"+beforeDay;
			var beforeDate=new Date(Date.parse(beforeString));//从今天往前三个月的时间
			if(startime<beforeDate){
				startime=beforeDate;
				startString=beforeString;
			}
			//$("#taskcreatetimeup").datetimepicker('setStartDate',startime);
			var showStart=$("#taskcreatetimeup").val();//获取当前的截止日期
			var dateShowStart=Date.parse(showStart);
			var currentStart=new Date(dateShowStart);
			var days=0;
			if(currentStart>endTime){
				days=(currentStart-endTime)/(1000*60*60*24);
			}else{
				days=(endTime-currentStart)/(1000*60*60*24);
			}
			if(days>30){//若页面显示的时间差超过一个月,则将显示的截止时间改为设置的截止时间
				$("#taskcreatetimeup").val(startString);
			}
		});

	});
});
//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "showpushtocoreinfo";
//刷新列表
function reloaddata(data){
	$.ajax({
		url : "showpushtocoreinfo",
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

var map={};
function pushtocore(data){
	if(data == null || data == "") {
		alertmsg("参数为空");
		return;
	}
	var datas = data.split("&");
	if(datas == null || datas.length < 2) {
		alertmsg("参数不符合要求");
		return;
	}
	var hasvalue = true;
	for (var i=0; i<datas.length; i+=1) {
		var params = datas[i].split("=");
		if(params == null || params.length < 2 || params[1].replace(/\s+/, "").length == 0) {
			hasvalue = false;
			break;
		}
	}
	if(!hasvalue) {
		alertmsg("参数不符合要求");
		return;
	}

	var currentTime=new Date();
	var lastTime=map[data];
	if(lastTime!=null&&currentTime.getTime()-lastTime.getTime()<5000){
		alertmsg("请等待5秒后重新推送");
		return;
	}

	map[data]=currentTime;
	$.ajax({
		url : "/cm/policyInterface/pushtocore?"+data,
		type : 'GET',
		dataType : "json",
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function() {
			alertmsg("推送完成");
			reloaddata("");
		}
	});
}

