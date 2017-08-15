require(["jquery","bootstrap-table", "bootstrap","bootstrapdatetimepicker","bootstrapTableZhCn","zTree","zTreecheck","flat-ui","public","bootstrapdatetimepickeri18n"], function($) {
	$(function($){	
	//默认每页显示的5条
	var  limit = 5;
	//当前页数,默认是第1页
	var currentpage=1;
	//查询参数
	var pageurlparameter = "";
	//刷新按钮功能
	$(".generalrefreshBtn").click(function(){
		currentpage=1;
		refreshList();
	});
	
	//时间控件初始化
	$(".form_datetime").datetimepicker({
		language:"zh-CN",
		format:"yyyy-mm-dd",
		weekStart:1,
		todayBtn:1,
		autoclose:1,
		todayHighlight:1,
		startView:2,
		forceParse:0,
		minView:2,
		pickerPosition: "bottom-left"
	});
	//时间初始化为今天
	function initQueryTaskDate(){
		var date = new Date();
		var todayMounth = (date.getMonth()+1)+"";
		if(todayMounth.length<2){
			todayMounth = "0"+todayMounth;
		}
		var dateString = date.getFullYear()+"-"+todayMounth+"-"+date.getDate();
		$("#taskcreatetimeupTxt").val(dateString);
		$("#taskcreatetimeup").val(dateString+" 00:00:00");
		$("#taskcreatetimedownTxt").val(dateString);
		$("#taskcreatetimedown").val(dateString+" 23:59:59");
	}
	initQueryTaskDate();
	
	//重置按钮
	$("#generalresetbutton").click(function(){
		$("#maininstanceid").val("");
		$("#carlicenseno").val("");
		$("#protectname").val("");
		$("#taskcreatetimeupTxt").val("");
		$("#taskcreatetimeup").val("");
		$("#taskcreatetimedownTxt").val("");
		$("#taskcreatetimedown").val("");
	});
	
	
	//查询条件赋值
	function addParameter(parameterName,parameterValue){
		pageurlparameter += "&" + parameterName + "=" + parameterValue;
	}
	
	//搜索
	$("#generalquerybutton").click(function(){
		if($.trim($("#taskcreatetimeupTxt").val())&& $.trim($("#taskcreatetimedownTxt").val())){
			var timeupstr = ($.trim($("#taskcreatetimeup").val()).substring(0,11) + "00:00:00").replace(/-/g,"/");
			var timedownstr = ($.trim($("#taskcreatetimedown").val()).substring(0,11) + "23:59:59").replace(/-/g,"/");
			var timeup = new Date(timeupstr);
			var timedown = new Date(timedownstr);
			if(timeup >= timedown){
				alertmsg("任务创建截止时间应晚于开始时间！");
				return;
			}
		}
		pageurlparameter = "";
		if($.trim($("#maininstanceid").val())){
			addParameter("maininstanceid",$.trim($("#maininstanceid").val()));
		}
		if($.trim($("#carlicenseno").val())){
			addParameter("carlicenseno",$.trim($("#carlicenseno").val()));
		}
		if($.trim($("#bname").val())){
			addParameter("bname",$.trim($("#bname").val()));
		}
		if($.trim($("#taskcreatetimeup").val())){
			addParameter("taskcreatetimeup",$.trim($("#taskcreatetimeup").val()).substring(0,11) + "00:00:00");
		}
		if($.trim($("#taskcreatetimedown").val())){
			addParameter("taskcreatetimedown",$.trim($("#taskcreatetimedown").val()).substring(0,11) + "23:59:59");
		}
		currentpage=1;
		refreshList();
	});
	
	//翻页首页末页点击事件
	function initMyJs(){
		//翻页首页末页点击事件
		$(".toPageop").on("click", function(e) {
			var toPageid = $(this).attr("id");
			if(toPageid == "tofirst"){
				currentpage = 1;
			}else if(toPageid == "toprevious"){
				currentpage -= 1;
			}else if(toPageid == "tonext"){
				currentpage += 1;
			}else if(toPageid == "tolast"){
				currentpage = Number($("#totalPages").val());
			}
			refreshList();
		});
		
		//选择页码跳转
		$("#gotoPage").on("change", function(e) {
			var toPage = $(this).val();
			//不是选择的当前页就跳转
			if(toPage != currentpage){
				currentpage = Number(toPage);
				refreshList();
			}
		});
			
		//按钮重新报价  
		$(".getTaskPrice").on("click", function(e) {
				//防止重复提交
				$(".btn").prop("disabled", true);
				//组织传递数据
					var index =$(this).attr("id").split("_");
					var instanceid=index[0];	
					$.ajax({
						url : '/cm/business/ordermanage/manualRecordingRe',
						type : 'POST',
						dataType : 'text',
						contentType: "application/json",
						data :"{\"processinstanceid\":\""+instanceid+"\"}",
						cache : false,
						async : true,
						error : function() {
							//防止重复提交
							$(".btn").prop("disabled", false);
							alertmsg("Connection error");
						},
						success : function(data) {
							if (data == "success") {
								alertmsg("重新报价   操作成功！",backToMyTask);
							}else if (data == "fail"){
								//防止重复提交
								$(".btn").prop("disabled", false);
								alertmsg("重新报价   操作失败！");
							}
						}
					});
			});
		
		//按钮重新核保
		$(".getTaskProtect").on("click", function(e) {
				//组织传递数据
					var index =$(this).attr("id").split("_");
					var instanceid=index[0];
					var maininstanceid = index[1];
					var inscomcode =  index[2];
					//防止重复提交
					$(".btn").prop("disabled", true);
					$.ajax({
						url : '/cm/business/ordermanage/getPaySuccess',
						type : 'POST',
						dataType : 'text',
						contentType: "application/json",
						data : "{\"processinstanceid\":\""+subInstanceId+"\",\"inscomcode\":\""+inscomcode+"\",\"underWritingFlag\":\"1\",\"taskid\":\""+instanceId+"\",\"issecond\":\""+"2"+"\"}",
						cache : false,
						async : true,
						error : function() {
							//防止重复提交
							$(".btn").prop("disabled", false);
							alertmsg("Connection error");
						},
						success : function(data) {
							if (data=="success") {
								alertmsg("重新核保成功！",backToMyTask);
							}else if(data=="fail"){
								alertmsg("重新核保失败！");
							}else if(data=="paying"){
								alertmsg("订单正在支付中，不能重新核保！");
							}else{
								alertmsg(data);
							}
							//防止重复提交
							$(".btn").prop("disabled", false);
						}
					});
			});
	}	
	
	//返回我的历史任务
	$("#backbutton").on("click",function(e){
		window.location.href="historyTask";
	})
	
	//再次刷新任务池列表方法
	function refreshList(){
		$("#myOrderManageResultList").text("正在加载任务列表！请稍等......");
		$("#myOrderManageResultFoot").empty();
		var queryData = "limit=" + limit + "&currentpage=" + currentpage + pageurlparameter;		
		$.ajax({
			url:"../history/queryMyHistoryTask",
			type:"get",
			dataType:"html",
			data:queryData,
			cache:false,
			async : true,
			success:function(data){
				if(data){
					$("#myOrderManage").empty();
					$("#myOrderManage").html(data);
					if(!$.trim($("#myOrderManageResultList").text())){
						$("#myOrderManageResultList").text("没有查询到数据！");
					}
					initMyJs();
				}else{
					$("#myOrderManageResultList").text("加载数据失败，请重试！");
					alertmsg("查询数据失败！");
				}
			}
		});
	}
	
	function backToMyTask(){
		if($(window.top.document).find("#menu").css("display")=="none"){
			$.cmTaskList('my', '', true);
		}else{
			//window.location.href =  "/cm/business/history/queryMyHistoryTask";
			currentpage=1;
			refreshList();
		}	
	}
	
	//初次刷新任务池列表方法
	function refreshTaskList(){
		$("#myOrderManageResultList").text("正在加载任务列表！请稍等......");
		$("#myOrderManageResultFoot").empty();
		
		var datetime = new Date();
		var todayMounth = (datetime.getMonth()+1)+"";
		if(todayMounth.length<2){
				todayMounth = "0"+todayMounth;
		}
		var datetimes = datetime.getFullYear()+"-"+todayMounth+"-"+datetime.getDate();
		addParameter("taskcreatetimeup",datetimes+" 00:00:00");	
		addParameter("taskcreatetimedown",datetimes+" 23:59:59");
		var queryData = "limit=" + limit + "&currentpage=" + currentpage + pageurlparameter;				
		$.ajax({
			url:"../history/queryMyHistoryTask",
			type:"get",
			dataType:"html",
			data:queryData,
			cache:false,
			async : true,
			success:function(data){
				if(data){
					$("#myOrderManage").empty();
					$("#myOrderManage").html(data);
					if(!$.trim($("#myOrderManageResultList").text())){
						$("#myOrderManageResultList").text("没有查询到数据！");
					}
					initMyJs();
				}else{
					$("#myOrderManageResultList").text("加载数据失败，请重试！");
					alertmsg("查询数据失败！");
				}
			},
		});
	}
	refreshTaskList();
	});
})

function myTaskForward(orderid,pid,codename){
	/*if(orderid == "" && pid == ""){
		alertmsg("Connection error!");
		return false;
	}
	$("#id").val(orderid);
	$("#pid").val(pid);
	$("#codename").val(codename);
	$("#detailinfo").submit();*/
	var forwardURL = "../history/queryorderdetail?id="+orderid+"&pid="+pid+"&codename"+codename;	
	if (forwardURL != "") {
		if($(window.top.document).find("#menu").css("display")=="none"){
			$.cmTaskList('my', 'list4deal', false);
			$.insLoading();
			$(window.top.document).find("#desktop_content").attr("src", forwardURL);
		}else{
			location.href=forwardURL;
		}
	}
}


