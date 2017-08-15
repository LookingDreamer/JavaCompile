require(["jquery","bootstrap","zTree","zTreecheck","bootstrapdatetimepicker","bootstrapdatetimepickeri18n","public"], function ($) {
	$(function() {
		$(".table").hover(
			function(){
				$(this).find("img").attr("src","/cm/static/images/system/resource/resource2.png");
				if($(this).find("label:contains('渠道来源')").text()!="渠道来源："){
//					$(this).find("label:contains('渠道来源')").css("color","red");
				}
				},
			function(){
				$(this).find("img").attr("src","/cm/static/images/system/resource/resource1.png");
//				$(this).find("label:contains('渠道来源')").css("color","");
			}		
		);
		//时间控件初始化设置
//		 $('.form_datetime').datetimepicker({
//		      language: 'zh-CN',
//		      format: "yyyy-mm-dd",
//		      weekStart: 1,
//		      todayBtn: 1,
//		      autoclose: 1,
//		      todayHighlight: 1,
//		      startView: 2,
//		      forceParse: 0,
//		      minView: 2,
//		      pickerPosition: "bottom-left"
//		      // showMeridian: 1
//		    });
		 $("#startdate").datetimepicker({
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
		 $("#enddate").datetimepicker({
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
		//高级搜索
		$("#seniorsearch").on("click",function(e){
			$("#simplequery1").hide();
			$("#seniorquery").show();
		});
		// 搜索条件【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#nameorcarno").val("");
			
		});
		// 高级搜索条件【重置】按钮
		$("#seniorresetbutton").on("click", function(e) {
			$("#insuredname").val("");
			$("#providerid").val("");
			$("#providername").val("");
			$("#carnum").val("");
			$("#taskid").val("");
			$("#phone").val("");
			$("#orderstatus").val("");
//			$("#usertype").val("");
			$("#shutter").val("");
			$("#startdate").val("");
			$("#enddate").val("");
			$("#agentcode").val("");
			$("#paymentTransaction").val("");
			$("#channelinnercode").val("");
			$("#channelName").val("");
			$("#miniagentcode").val("");
		});
//		返回
		$("#backbutton").on("click",function(e){
			window.location.href="/cm/order/query";
		})
		//关闭高级搜索
		$("#closesearch").on("click",function(e){
			$("#simplequery1").show();
			$("#seniorquery").hide();
		});
		//快速搜索改变时
//		$("#nameorcarno").on("change",function(e){
//			$("#carnumorcname").val($("#nameorcarno").val());
//		});
		//点击搜索
		$("#simplequerybutton").on("click",function(e){
			$("#carnumorcname").val($("#nameorcarno").val());
			formsubmit();
		});
		//点击高级搜索
		$("#seniorquerybutton").on("click",function(e){
			$("#carnumorcname").val("");
			var startime = new Date($("#startdate").val());
			var endtime = new Date($("#enddate").val());
		if(startime > endtime){
			alert("起始时间不能大于终止时间！");
			return;
		}	
			
			formsubmit();
		});
		//选择供应商
		$("#checkprovider").on("click", function(e) {
			$('#showprovider').modal();
			$.fn.zTree.init($("#providertree"), providersetting);
		});
		
		//分页控制
		//首页
		$('#first').on("click",function(e){
			$("#currentPage").val(1);
			formsubmit();
		});
		//末页
		$('#last').on("click",function(e){
			$("#currentPage").val($.trim($("#lblPageCount").text()));
			formsubmit();
		});
		
		//上一页
		$('#previous').on("click",function(e){
			var pagecurrent = $("#currentPage").val();
			if(pagecurrent > 0){
				pagecurrent--;
				$("#currentPage").val(pagecurrent);
				formsubmit();
			}
		});
		
		//下一页
		$('#next').on("click",function(e){
			var pagecurrent = $("#currentPage").val();
			var pagecount = $.trim($("#lblPageCount").text());
			if(pagecurrent<pagecount){
				pagecurrent++;
				$("#currentPage").val(pagecurrent);
				formsubmit();
			}
		});
		
		 //修改起始时间
		 $("#startdate").change(function(){
		    	var startString=$("#startdate").val();//获取当前显示的时间字符串
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
		    	$("#enddate").datetimepicker('setEndDate',endTime);
		    	var showEnd=$("#enddate").val();//获取当前的截止日期
		    	var dateShowEnd=Date.parse(showEnd);
		    	var currentEnd=new Date(dateShowEnd);
		    	var days=0;
		    	if(currentEnd>startime){
		    		days=(currentEnd-startime)/(1000*60*60*24);
		    	}else{
		    		days=(startime-currentEnd)/(1000*60*60*24);
		    	}   	
		    	if(days>30){//若页面显示的时间差超过一个月,则将显示的截止时间改为设置的截止时间
		    		$("#enddate").val(endString);
		    	}
		    });
		 	
		 	//修改截止时间
		    $("#enddate").change(function(){
		    	var endString=$("#enddate").val();
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
		    	//$("#startdate").datetimepicker('setStartDate',startime);
		    	var showStart=$("#startdate").val();//获取当前的截止日期
		    	var dateShowStart=Date.parse(showStart);
		    	var currentStart=new Date(dateShowStart);
		    	var days=0;
		    	if(currentStart>endTime){
		    		days=(currentStart-endTime)/(1000*60*60*24);
		    	}else{
		    		days=(endTime-currentStart)/(1000*60*60*24);
		    	}   	
		    	if(days>30){//若页面显示的时间差超过一个月,则将显示的截止时间改为设置的截止时间
		    		$("#startdate").val(startString);
		    	}
		    });
	});

});
var providersetting = {
		async: {
			enable: true,
			url:"queryprovidertree",
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

//表单提交
function formsubmit(){
	$("#queryorder").submit();
}

function providerTreeOnCheck(event, treeId, treeNode) {
	$("#providerid").val(treeNode.prvcode);
	$("#providername").val(treeNode.name);
	$('#showprovider').modal("hide");
}

function checkthisstatus(obj,status){
	$(".nav-pills li").removeClass("active");
	$(obj).addClass("active");
	$("#orderstatus").val(status);
	formsubmit();
}

function go(pagenumber){
	$("#currentPage").val(pagenumber);
	formsubmit();
}

//查看详情
function searchorderdetail(orderid,pid,codename,payid,taskcode){
	if(orderid == "" && pid == ""){
		alertmsg("Connection error!");
		return false;
	}
	$("#id").val(orderid);
	$("#pid").val(pid);
	$("#codename").val(codename);
	$("#payid").val(payid);
	$("#taskcode").val(taskcode);
	$("#detailinfo").submit();
}
