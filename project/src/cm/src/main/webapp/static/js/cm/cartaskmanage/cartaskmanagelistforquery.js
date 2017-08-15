require(["jquery", "bootstrapdatetimepicker", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "bootstrapdatetimepickeri18n","public"], function ($) {
	//默认一页显示十条记录
	var pagesize = 10;
	//当前调用的url
	var pageurl = "showcartaskmanagelist";
	//当前调用的url查询参数
	var pageurlparameter = "";
	//简单查询车牌被保人参数
	var simpleparameter = "";
	//简单查询任务状态参数
	var tasktype = "";
	
	//隐藏或显示高级查询
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
	 
	// 时间控件初始化为今天
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
		
	// 重置按钮
	$("#resetbutton").on("click", function(e) {
		$("#carlicenseno").val("");
		$("#insuredname").val("");
		$("#usertype").val("");
		$("#tasktype").val("");
		$("#taskstatus").val("");
//		$("#taskcreatetimeupTxt").val("");
//		$("#taskcreatetimeup").val("");
//		$("#taskcreatetimedown").val("");
//		$("#taskcreatetimedownTxt").val("");
		$("#agentNum").val("");
		$("#agentName").val("");
		$("#mainInstanceId").val("");
		$("#deptcode").val("");
		$("#deptname").val("");
		$("#inscomcode").val("");
		$("#inscomname").val("");
		initQueryTaskDate();
	});
	
	$(function() {
		$('#table-javascript').bootstrapTable({
            method: 'get',
            url: pageurl+pageurlparameter,
            cache: false,
            striped: true,
            pagination: true,
            pageList: [5, 10, 15, 20],
            sidePagination: 'server', 
            pageSize: pagesize,
            minimumCountColumns: 2,
            clickToSelect: true,
            singleSelect: true,
            columns: [{
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            }, {
                field: 'maininstanceId',
                title: '流程号',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'carlicenseno',
                title: '车牌',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'insuredname',
                title: '被保人',
                align: 'center',
                valign: 'middle',
                sortable: true
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
                sortable: true
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
                sortable: true
            }, {
                field: 'deptname',
                title: '出单网点',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'operator',
                title: '处理人',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'dispatchstatus',
                title: '分配状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, 
//            {
//                field: 'operatorstate',
//                title: '调度状态',
//                align: 'center',
//                valign: 'middle',
//                sortable: true
//            }, 
            {
                field: 'operating',
                title: '详细信息',
                align: 'center',
                valign: 'middle',
                switchable:false
            },{
                field: 'flowerror',
                title: '系统日志',
                align: 'center',
                valign: 'middle',
                switchable:false
            }]
        });
		//点击弹出出单网点选择页面
		//----------------------------------------------------------
		var setting = {
			async : {
				enable : true,
				url : "/cm/business/cartaskmanage/querydepttree",
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
				onClick : zTreeOnCheckDept,//回调函数
				onCheck : zTreeOnCheckDept//回调函数
			}
		};
		//选择后回调函数
		function zTreeOnCheckDept(event, treeId, treeNode) {
			$("#deptcode").val(treeNode.comcode);
			$("#deptname").val(treeNode.name);
			$('#showDeptTree').modal("hide");
		}
		//点击弹出供应商选择页面
		$("#deptname").on("click", function(e) {
			$('#showDeptTree').modal();
			$.fn.zTree.init($("#deptTreeDemo"), setting);
		});
		$(".closeShowDeptTree").on("click", function(e) {
			$('#showDeptTree').modal("hide");
		});
		//-----------------------------------------------------------
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
		//--------------------------------------------------
	});
	// 车险任务记录高级查询条件赋值
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
	function executeGeneralQuery(){
		tasktype = "";
		simpleparameter = "";
		if($.trim($("#carlicensenoOrinsuredname").val())){
			simpleparameter = "carlicensenoOrinsuredname=" + $.trim($("#carlicensenoOrinsuredname").val());
		}
		var paramStr = getParamStr();
		$('#table-javascript').bootstrapTable('refresh',{url:pageurl+paramStr});
	}
	$("#generalquerybutton").on("click", function(e) {
		executeGeneralQuery();
	 });
	// 查询条件【重置】按钮
	$("#generalresetbutton").on("click", function(e) {
		$("#carlicensenoOrinsuredname").val("");
	});
	//绑定输入框回车查询事件
	$("input#carlicensenoOrinsuredname").keydown(function(event) {
		var e = event || window.event || arguments.callee.caller.arguments[0];
		//13是键盘上面固定的回车键
		if (e && e.keyCode==13) {
		   //一般查询
			executeGeneralQuery();
		}
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
		if($.trim($("#mainInstanceId").val())){
			addParameter("ad_instanceId",$.trim($("#mainInstanceId").val()));
		}
		if($.trim($("#operatorname").val())){
			addParameter("operatorname",$.trim($("#operatorname").val()));
		}
		if($.trim($("#deptcode").val())){
			addParameter("deptcode",$.trim($("#deptcode").val()));
		}
		if($.trim($("#inscomcode").val())){
			addParameter("inscomcode",$.trim($("#inscomcode").val()));
		}
		if(pageurlparameter.length == 1){
			pageurlparameter = "";
		}
		$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
	 });
	
	// 查看流程轨迹按钮事件
	$("#showWorkflowTrack").on("click", function(e) {
		//组织传递数据
		var carTaskData = $('#table-javascript').bootstrapTable('getSelections');
		if(carTaskData.length == 0){
			alertmsg("没有任务被选中！");
			return;
		}else if(carTaskData.length > 1){
			alertmsg("只能选择一条任务！");
			return;
		}
		//跳出工作流轨迹弹出框
		window.parent.openDialogForCM("business/cartaskmanage/showWorkflowTrack?instanceId="+carTaskData[0].maininstanceId
				+"&inscomcode="+carTaskData[0].inscomcode);
	});
	
});
