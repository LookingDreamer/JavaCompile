require(["jquery", "bootstrapdatetimepicker", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "bootstrapdatetimepickeri18n","public"], function ($) {
	//默认一页显示十条记录
	var pagesize = 10;
	//当前调用的url
	/*var pageurl = "/cm/business/cartaskmanage/showcartaskmanagelist";*/
	var pageurl = "showcartaskmanagelist";
	//当前调用的url查询参数
	var pageurlparameter = "";
	//简单查询车牌被保人参数
	var simpleparameter = "";
	//简单查询任务状态参数
	var tasktype = "";
	
	/*//隐藏或显示高级查询
	$("#showsuperquerybutton").click(function(){
		$("#superquerypanel").toggle();
		$("#querypanel").toggle();
	});
	$("#superquerypanelclose").click(function(){
		$("#superquerypanel").toggle();
		$("#querypanel").toggle();
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
	      pickerPosition: "bottom-left"
	      // showMeridian: 1
	    });
	// 重置按钮
	$("#resetbutton").on("click", function(e) {
		$("#carlicenseno").val("");
		$("#insuredname").val("");
		$("#usertype").val("");
		$("#tasktype").val("");
		$("#taskstatus").val("");
		$("#taskcreatetimeupTxt").val("");
		$("#taskcreatetimeup").val("");
		$("#taskcreatetimedown").val("");
		$("#taskcreatetimedownTxt").val("");
		$("#agentNum").val("");
		$("#agentName").val("");
	});*/
	//初始化车险任务数据
	$(function() {
		pageurlparameter=$('#agentNum').val();
		$('#table-javascript').bootstrapTable({
            method: 'get',
            /*url: pageurl+pageurlparameter,*/
            url: 'showcartaskmanagelist?agentNum='+pageurlparameter,
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server', 
            pageSize: pagesize,
            minimumCountColumns: 2,
            clickToSelect: true,
            singleSelect: true,
            columns: [{
                field: 'maininstanceId',
                title: '任务id',
                visible: false,
                switchable:false
            }, {
                field: 'carlicenseno',
                title: '车牌',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'insuredname',
                title: '被保人',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'name',
                title: '代理人',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'tasktype',
                title: '任务类型',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'taskcreatetime',
                title: '任务创建时间',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'inscomName',
                title: '保险公司',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'operator',
                title: '处理人',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'dispatchstatus',
                title: '分配状态',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'operating',
                title: '详细信息',
                align: 'center',
                valign: 'middle',
                switchable:false
            }]
        });
	});
	
	$('#tasklist_return').on("click",function(){
		history.go(-1)
	});
	/*// 车险任务记录高级查询条件赋值
	function addParameter(parameterName,parameterValue){
		if(pageurlparameter.length == 1){
			pageurlparameter += parameterName + "=" + parameterValue;
		}else{
			pageurlparameter += "&" + parameterName + "=" + parameterValue;
		}
	}
	// 车险任务记录一般查询条件赋值
	function getParamStr(){
		var paramStr = "?";
		if(simpleparameter){
			paramStr += simpleparameter;
			if(tasktype){
				paramStr += ("&"+tasktype);
			}
		}else{
			if(tasktype){
				paramStr += tasktype;
			}else{
				paramStr = "";
			}
		}
		return paramStr;
	}
	//简单查询任务状态链接点击事件
	reloadCarTaskInfoByTasktype = function(pagetasktype){
		if($.trim(pagetasktype)){
			tasktype = "tasktype="+$.trim(pagetasktype);
		}else{
			tasktype = "";
		}
		var paramStr = getParamStr();
		$('#table-javascript').bootstrapTable('refresh',{url:pageurl+paramStr});
	}
	// 一般查询按钮（车辆任务列表）
	$("#generalquerybutton").on("click", function(e) {
		tasktype = "";
		simpleparameter = "";
		if($.trim($("#carlicensenoOrinsuredname").val())){
			simpleparameter = "carlicensenoOrinsuredname=" + $.trim($("#carlicensenoOrinsuredname").val());
		}
		var paramStr = getParamStr();
		$('#table-javascript').bootstrapTable('refresh',{url:pageurl+paramStr});
	 });
	// 高级查询按钮（车辆任务列表）
	$("#superquerybutton").on("click", function(e) {
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
		pageurlparameter = "?";
		if($.trim($("#carlicenseno").val())){
			addParameter("carlicenseno",$.trim($("#carlicenseno").val()));
		}
		if($.trim($("#agentName").val())){
			addParameter("agentName",$.trim($("#agentName").val()));
		}
		if($.trim($("#agentNum").val())){
			addParameter("agentNum",$.trim($("#agentNum").val()));
		}
		if($.trim($("#usertype").val())){
			addParameter("usertype",$.trim($("#usertype").val()));
		}
		if($.trim($("#insuredname").val())){
			addParameter("insuredname",$.trim($("#insuredname").val()));
		}
		if($.trim($("#tasktype").val())){
			addParameter("tasktype",$.trim($("#tasktype").val()));
		}
		if($.trim($("#taskstatus").val())){
			addParameter("taskstatus",$.trim($("#taskstatus").val()));
		}
		if($.trim($("#taskcreatetimeup").val())){
			addParameter("taskcreatetimeup",$.trim($("#taskcreatetimeup").val()).substring(0,11) + "00:00:00");
		}
		if($.trim($("#taskcreatetimedown").val())){
			addParameter("taskcreatetimedown",$.trim($("#taskcreatetimedown").val()).substring(0,11) + "23:59:59");
		}
		if(pageurlparameter.length == 1){
			pageurlparameter = "";
		}
		$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
	 });*/
	
	//获得选中行的taskid集合
//	function getSelectedRows() {
//	    var data = $('#table-javascript').bootstrapTable('getSelections');
//    	var arraytaskid = new Array();
//    	for(var i=0;i<data.length;i++){
//    		var itemStr = "{\"taskid\":\""+data[i].taskid+"\",\"operator\":\""+(data[i].operator==null?"":data[i].operator)
//    			+"\",\"maininstanceId\":\""+data[i].maininstanceId+"\",\"inscomcode\":\""+data[i].inscomcode
//    			+"\",\"subInstanceId\":\""+data[i].subInstanceId+"\"}";
//    		arraytaskid.push(itemStr)
//    	}
//    	return arraytaskid;
//	}
	
	/*// 车险任务分配功能接口调用（车辆任务列表更新）
	$("#dispatch").on("click", function(e) {
		//组织传递数据
		var carTaskData = $('#table-javascript').bootstrapTable('getSelections');
		if(carTaskData.length == 0){
	    	alertmsg("没有任务被选中！");
	    	return;
	    }else if(carTaskData.length > 1){
	    	alertmsg("每次只能处理一条任务！");
	    	return;
	    }
		//校验此任务是否提供分配
//		if(carTaskData. == ){
//	    	alertmsg("此任务不提供分配！");
//	    	return;
//	    }
		var userid = $.trim($("#personname").val());
		if(!userid){
			alertmsg("请指定分配人员信息！");
	    	return;
		}
		var dispatchParams = {//调度分配参数
				"userCode":userid,
				"operator":carTaskData[0].operator==null?"":carTaskData[0].operator,
				"maininstanceId":carTaskData[0].maininstanceId,
				"inscomcode":carTaskData[0].inscomcode,
				"subInstanceId":carTaskData[0].subInstanceId
				};
//		alertmsg(JSON.stringify(dispatchParams));
		$.ajax({
			url : '/cm/business/cartaskmanage/dispatch',
			type : 'GET',
			dataType : 'json',
			contentType: "application/json",
			data :dispatchParams,
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg(data.msg);
					$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data.status == "fail"){
					alertmsg(data.msg);
				}
			}
		});
	});*/
	
	/*// 车险任务停止调度按钮事件
	$("#stopdispatch").on("click", function(e) {
		//组织传递数据
		var carTaskData = $('#table-javascript').bootstrapTable('getSelections');
		if(carTaskData.length == 0){
	    	alertmsg("没有任务被选中！");
	    	return;
	    }else if(carTaskData.length > 1){
	    	alertmsg("每次只能处理一条任务！");
	    	return;
	    }
		//校验此任务是否提供停止调度
//		if(carTaskData. == ){
//	    	alertmsg("此任务不提供停止调度！");
//	    	return;
//	    }
		var stopdispatchParams = {//停止调度参数
				"maininstanceId":carTaskData[0].maininstanceId,
				"inscomcode":carTaskData[0].inscomcode,
				"subInstanceId":carTaskData[0].subInstanceId
				};
		$.ajax({
			url : '/cm/business/cartaskmanage/stopdispatch',
			type : 'GET',
			dataType : 'json',
			contentType: "application/json",
			data :stopdispatchParams,
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg(data.msg);
					$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data.status == "fail"){
					alertmsg(data.msg);
				}
			}
		});
	});*/
	
	/*// 车险任务重启调度按钮事件
	$("#restartdispatch").on("click", function(e) {
		//组织传递数据
		var carTaskData = $('#table-javascript').bootstrapTable('getSelections');
		if(carTaskData.length == 0){
	    	alertmsg("没有任务被选中！");
	    	return;
	    }else if(carTaskData.length > 1){
	    	alertmsg("每次只能处理一条任务！");
	    	return;
	    }
		//校验此任务是否提供重启调度
//		if(carTaskData. == ){
//	    	alertmsg("此任务不提供重启调度！");
//	    	return;
//	    }
		var restartdispatchParams = {//重启调度参数
				"maininstanceId":carTaskData[0].maininstanceId,
				"inscomcode":carTaskData[0].inscomcode,
				"subInstanceId":carTaskData[0].subInstanceId
				};
		$.ajax({
			url : '/cm/business/cartaskmanage/restartdispatch',
			type : 'GET',
			dataType : 'json',
			contentType: "application/json",
			data :restartdispatchParams,
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg(data.msg);
					$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data.status == "fail"){
					alertmsg(data.msg);
				}
			}
		});
	});*/
	
});
