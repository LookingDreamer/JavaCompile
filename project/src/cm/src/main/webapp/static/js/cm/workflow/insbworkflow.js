require(["jquery", "bootstrapdatetimepicker", "bootstrap-table", "bootstrap","bootstrapTableZhCn","zTree","zTreecheck","flat-ui","bootstrapdatetimepickeri18n","public"], function ($) {
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
	
	$(function() {
		initQueryTaskDate();
		pageurlparameter = "?";
		if($.trim($("#taskcreatetimeup").val())){
			addParameter("taskcreatetimeup",$.trim($("#taskcreatetimeup").val()).substring(0,10).trim() + " 00:00:00");
		}
		if($.trim($("#taskcreatetimedown").val())){
			addParameter("taskcreatetimedown",$.trim($("#taskcreatetimedown").val()).substring(0,10).trim() + " 23:59:59");
		}
		
		$('#table-javascript').bootstrapTable({
            method: 'get',
            url:'',
            cache: false,
            striped: true,
            pagination: true,
            pageList: [10, 20, 30, 40],
            sidePagination: 'server', 
            pageSize: 10,
            minimumCountColumns: 2,
            clickToSelect: true,
            singleSelect: false,
            showToggle:true,  
            cardView: false,  
            showColumns: true,
            columns: [{
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            }, {
                field: 'mainInstanceId',
                title: '流程号',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'taskcodename',
                title: '任务类型',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'tasktype',
                title: '工作流类型',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'createtime',
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
                field: 'operator',
                title: '处理人',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'taskstatus',
                title: '任务状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            }/*,  {
                field: 'operating',
                title: '操作',
                align: 'center',
                valign: 'middle',
                switchable:false,
                formatter: operateFormatter
				//,events: operateEvents
            }*/],
            onCheck: function (row, element) {
                checkTaskStatus();
            },
            onUncheck: function (row, element) {
				checkTaskStatus();
            },
			onCheckAll: function (rows) {
				checkTaskStatus();
			},
			onUncheckAll: function (rows) {
				checkTaskStatus();
			}
        });	
	});

	// 重置按钮
	$("#resetbutton").on("click", function(e) {
		$("#tasktype").val("");
		$("#taskstatus").val("");
		$("#taskcreatetimeup").val("");
		$("#taskcreatetimedown").val("");
		$("#mainInstanceId").val("");
	});

	// 高级查询按钮
	$("#querybutton").on("click", function(e) {
		returnBack();
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

		if(!$("#mainInstanceId").val().replace(/\s+/g,"") && !$.trim($("#tasktype").val())) {
			alertmsg("请选择一种任务类型");
			return;
		}
		if (!$.trim($("#mainInstanceId").val()) && (!$.trim($("#taskcreatetimeup").val()) || !$.trim($("#taskcreatetimedown").val()))) {
			alertmsg("请选择任务创建时间");
			return;
		}
		var taskcreatetimeupvalue = $.trim($("#taskcreatetimeup").val());
		var taskcreatetimedownvalue = $.trim($("#taskcreatetimedown").val());
		var timeupdatestr = taskcreatetimeupvalue.substring(0,11);
		var timedowndatestr = taskcreatetimedownvalue.substring(0,11);

		var timeupdate = new Date((timeupdatestr + " 00:00:00").replace(/-/g,"/"));
		var timedowndate = new Date((timedowndatestr + " 00:00:00").replace(/-/g,"/"));
		var iDays = parseInt(Math.abs(timedowndate - timeupdate) / 1000 / 60 / 60 /24);
		if(iDays > 2) {
			alertmsg("任务创建时间暂时只开放3天的查询区间");
			return;
		}

		pageurlparameter = "?";
		if($.trim($("#tasktype").val())){
			addParameter("tasktype",$.trim($("#tasktype").val()));
		}
		if($.trim($("#taskstatus").val())){
			addParameter("taskstatus",$.trim($("#taskstatus").val()));
		}
		if($.trim($("#taskcreatetimeup").val())){
			addParameter("taskcreatetimeup",$.trim($("#taskcreatetimeup").val()).substring(0,10).trim() + " 00:00:00");
		}
		if($.trim($("#taskcreatetimedown").val())){
			addParameter("taskcreatetimedown",$.trim($("#taskcreatetimedown").val()).substring(0,10).trim() + " 23:59:59");
		}
		if($.trim($("#mainInstanceId").val())){
			addParameter("mainInstanceId",$.trim($("#mainInstanceId").val()));
		}
		if(pageurlparameter.length == 1){
			pageurlparameter = "";
			alert("至少录入一个查询条件!");
			return;
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
		window.parent.openDialogForCM("business/cartaskmanage/showWorkflowTrack?instanceId="+carTaskData[0].mainInstanceId+"&inscomcode="+carTaskData[0].inscomcode);
	});

	//强制转人工
	$("#forceArtificial").on("click", function(e) {
		var datas = $('#table-javascript').bootstrapTable('getSelections');
		var dispatchParams = [];
		var idx = 0;

		for (var i=0; i<datas.length; i+=1) {
			if (datas[i].taskcodename == "关闭") {
				alertmsg("("+datas[i].mainInstanceId+")任务已关闭！");
				return;
			}

			var procInstanceid;
			if (datas[i].type == "sub") {
				procInstanceid = datas[i].instanceid;
			} else if (datas[i].type == "main") {
				procInstanceid = datas[i].mainInstanceId;
			}

			//参数传递
			dispatchParams[idx++] = {
				"type": datas[i].type,
				"instanceid": procInstanceid,
				"maininstanceid": datas[i].mainInstanceId,
				"subinstanceid": datas[i].instanceid,
				"taskcode": datas[i].taskcode,
				"taskname": datas[i].tasktype,
				"cmtaskcode": datas[i].tasktaskcode,
				"cmtaskname": datas[i].taskcodename,
				"inscomcode": datas[i].inscomcode
			};
		}

		if(dispatchParams.length == 0){
			alertmsg("请选择正确的任务进行操作！");
			return;
		}

		//防止重复提交
		$("#forceArtificial").prop("disabled", true);

		$.ajax({
			url : '/cm/business/manualpushflow/pushManualInterface',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			data : JSON.stringify(dispatchParams),
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$("#forceArtificial").prop("disabled", false);
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg(data.msg);
					returnBack();
					$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data.status == "fail"){
					alertmsg(data.msg);
				}
				//防止重复提交
				$("#forceArtificial").prop("disabled", false);
			}
		});
	});
	
	//取消任务
	$("#quietOrder").on("click", function(e) {
		var datas = $('#table-javascript').bootstrapTable('getSelections');
		var dispatchParams = [];
		var idx = 0;

		for (var i=0; i<datas.length; i+=1) {
			if (datas[i].taskcodename == "关闭") {
				alertmsg("("+datas[i].mainInstanceId+")任务已关闭！");
				return;
			}

			//参数传递
			dispatchParams[idx++] = {
				"type": datas[i].type,
				"maininstanceid": datas[i].mainInstanceId,
				"subinstanceid": datas[i].instanceid,
				"taskcode": datas[i].taskcode,
                "cmtaskcode": datas[i].tasktaskcode,
				"inscomcode": datas[i].inscomcode,
                "remark": "cancel"
			};
		}

		if(dispatchParams.length == 0){
			alertmsg("请选择正确的任务进行操作！");
			return;
		}

		//防止重复提交
		$("#quietOrder").prop("disabled", true);

		$.ajax({
			url : '/cm/business/manualpushflow/refuseInsuranceUw',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			data :JSON.stringify(dispatchParams),
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$("#quietOrder").prop("disabled", false);
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg(data.msg);
					returnBack();
					$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data.status == "fail"){
					alertmsg(data.msg);
				}
				//防止重复提交
				$("#quietOrder").prop("disabled", false);
			}
		});
	});
	
	//返回修改
	$("#returnEdit").on("click", function(e) {
		var datas = $('#table-javascript').bootstrapTable('getSelections');
		var dispatchParams = [];
		var idx = 0;

		for (var i=0; i<datas.length; i+=1) {
			if (datas[i].taskcodename == "关闭") {
				alertmsg("("+datas[i].mainInstanceId+")任务已关闭！");
				return;
			}

			var procInstanceid;
			if (datas[i].type == "sub") {
				procInstanceid = datas[i].instanceid;
			} else if (datas[i].type == "main") {
				procInstanceid = datas[i].mainInstanceId;
			}

			//参数传递
			dispatchParams[idx++] = {
				"type": datas[i].type,
				"instanceid": procInstanceid,
				"maininstanceid": datas[i].mainInstanceId,
				"subinstanceid": datas[i].instanceid,
				"taskcode": datas[i].taskcode,
				"cmtaskcode": datas[i].tasktaskcode,
				"inscomcode": datas[i].inscomcode
			};
		}

		if(dispatchParams.length == 0){
			alertmsg("请选择正确的任务进行操作！");
			return;
		}

		//防止重复提交
		$("#returnEdit").prop("disabled", true);

		$.ajax({
			url : '/cm/business/manualpushflow/callBackInterface',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			data :JSON.stringify(dispatchParams),
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$("#returnEdit").prop("disabled", false);
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg(data.msg);
					returnBack();
					$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data.status == "fail"){
					alertmsg(data.msg);
				}
				//防止重复提交
				$("#returnEdit").prop("disabled", false);
			}
		});
	});
	
	//拒绝承保
	$("#refuseInsured").on("click", function(e) {
		var datas = $('#table-javascript').bootstrapTable('getSelections');
		var dispatchParams = [];
		var idx = 0;

		for (var i=0; i<datas.length; i+=1) {
			if (datas[i].taskcodename == "关闭") {
				alertmsg("("+datas[i].mainInstanceId+")任务已关闭！");
				return;
			}

			//参数传递
			dispatchParams[idx++] = {
				"type": datas[i].type,
				"maininstanceid": datas[i].mainInstanceId,
				"subinstanceid": datas[i].instanceid,
				"taskcode": datas[i].taskcode,
                "cmtaskcode": datas[i].tasktaskcode,
				"inscomcode": datas[i].inscomcode
			};
		}

		if(dispatchParams.length == 0){
			alertmsg("请选择正确的任务进行操作！");
			return;
		}

		//防止重复提交
		$("#refuseInsured").prop("disabled", true);

		$.ajax({
			url : '/cm/business/manualpushflow/refuseInsuranceUw',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			data :JSON.stringify(dispatchParams),
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$("#refuseInsured").prop("disabled", false);
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg(data.msg);
					returnBack();
					$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data.status == "fail"){
					alertmsg(data.msg);
				}
				//防止重复提交
				$("#refuseInsured").prop("disabled", false);
			}
		});
	});
	
	//完成任务
	$("#completeOrder").on("click", function(e) {
		var datas = $('#table-javascript').bootstrapTable('getSelections');
		var dispatchParams = [];
		var idx = 0;

		for (var i=0; i<datas.length; i+=1) {
			if (datas[i].taskcodename == "关闭") {
				alertmsg("("+datas[i].mainInstanceId+")任务已关闭！");
				return;
			}

			//参数传递
			dispatchParams[idx++] = {
				"type": datas[i].type,
				"maininstanceid": datas[i].mainInstanceId,
				"subinstanceid": datas[i].instanceid,
				"taskcode": datas[i].taskcode,
				"cmtaskcode": datas[i].tasktaskcode,
				"inscomcode": datas[i].inscomcode
			};
		}

		if(dispatchParams.length == 0){
			alertmsg("请选择正确的任务进行操作！");
			return;
		}

		//防止重复提交
		$("#completeOrder").prop("disabled", true);

		$.ajax({
			url : '/cm/business/manualpushflow/underwritesuccess',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			data :JSON.stringify(dispatchParams),
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$("#completeOrder").prop("disabled", false);
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg(data.msg);
					returnBack();
					$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data.status == "fail"){
					alertmsg(data.msg);
				}
				//防止重复提交
				$("#completeOrder").prop("disabled", false);
			}
		});
	});
	
	//支付状态
	$("#paymentStatus").on("click", function(e) {
		var datas = $('#table-javascript').bootstrapTable('getSelections');
		var dispatchParams = [];
		var idx = 0;

		for (var i=0; i<datas.length; i+=1) {
			if (datas[i].taskcodename == "关闭") {
				alertmsg("("+datas[i].mainInstanceId+")任务已关闭！");
				return;
			}

			//参数传递
			dispatchParams[idx++] = {
				"maininstanceid": datas[i].mainInstanceId,
				"subinstanceid": datas[i].instanceid,
				"cmtaskcode": datas[i].tasktaskcode,
				"inscomcode": datas[i].inscomcode
			};
		}

		if(dispatchParams.length == 0){
			alertmsg("请选择正确的任务进行操作！");
			return;
		}

		//防止重复提交
		$("#paymentStatus").prop("disabled", true);

		$.ajax({
			url : '/cm/business/manualpushflow/queryPayResult',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			data :JSON.stringify(dispatchParams),
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$("#paymentStatus").prop("disabled", false);
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg(data.msg);
					returnBack();
					$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data.status == "fail"){
					alertmsg(data.msg);
				}
				//防止重复提交
				$("#paymentStatus").prop("disabled", false);
			}
		});
	});
	
	//支付失败
	$("#payFailed").on("click", function(e) {
		var datas = $('#table-javascript').bootstrapTable('getSelections');
		var dispatchParams = [];
		var idx = 0;

		for (var i=0; i<datas.length; i+=1) {
			if (datas[i].taskcodename == "关闭") {
				alertmsg("("+datas[i].mainInstanceId+")任务已关闭！");
				return;
			}

			//参数传递
			dispatchParams[idx++] = {
				"type": datas[i].type,
				"maininstanceid": datas[i].mainInstanceId,
				"subinstanceid": datas[i].instanceid,
				"taskcode": datas[i].taskcode,
				"cmtaskcode": datas[i].tasktaskcode,
				"inscomcode": datas[i].inscomcode
			};
		}

		if(dispatchParams.length == 0){
			alertmsg("请选择正确的任务进行操作！");
			return;
		}

		//防止重复提交
		$("#payFailed").prop("disabled", true);

		$.ajax({
			url : '/cm/business/manualpushflow/underwritefail',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			data :JSON.stringify(dispatchParams),
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$("#payFailed").prop("disabled", false);
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg(data.msg);
					returnBack();
					$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data.status == "fail"){
					alertmsg(data.msg);
				}
				//防止重复提交
				$("#payFailed").prop("disabled", false);
			}
		});
	});
	
	//支付成功
	$("#paySuccessed").on("click", function(e) {
		var datas = $('#table-javascript').bootstrapTable('getSelections');
		var dispatchParams = [];
		var idx = 0;

		for (var i=0; i<datas.length; i+=1) {
			if (datas[i].taskcodename == "关闭") {
				alertmsg("("+datas[i].mainInstanceId+")任务已关闭！");
				return;
			}

			//参数传递
			dispatchParams[idx++] = {
				"type": datas[i].type,
				"maininstanceid": datas[i].mainInstanceId,
				"subinstanceid": datas[i].instanceid,
				"taskcode": datas[i].taskcode,
				"cmtaskcode": datas[i].tasktaskcode,
				"inscomcode": datas[i].inscomcode
			};
		}

		if(dispatchParams.length == 0){
			alertmsg("请选择正确的任务进行操作！");
			return;
		}

		//防止重复提交
		$("#paySuccessed").prop("disabled", true);

		$.ajax({
			url : '/cm/business/manualpushflow/paySuccess',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			data :JSON.stringify(dispatchParams),
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$("#paySuccessed").prop("disabled", false);
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg(data.msg);
					returnBack();
					$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data.status == "fail"){
					alertmsg(data.msg);
				}
				//防止重复提交
				$("#paySuccessed").prop("disabled", false);
			}
		});
	});
	
	//承保打单成功
	$("#insuredSuccess, #packageOrder").on("click", function(e) {
		var datas = $('#table-javascript').bootstrapTable('getSelections');
		var dispatchParams = [];
		var idx = 0;

		for (var i=0; i<datas.length; i+=1) {
			if (datas[i].taskcodename == "关闭") {
				alertmsg("("+datas[i].mainInstanceId+")任务已关闭！");
				return;
			}

			var procInstanceid;
			if (datas[i].type == "sub") {
				procInstanceid = datas[i].instanceid;
			} else if (datas[i].type == "main") {
				procInstanceid = datas[i].mainInstanceId;
			}

			//参数传递
			dispatchParams[idx++] = {
				"type": datas[i].type,
				"instanceid": procInstanceid,
				"maininstanceid": datas[i].mainInstanceId,
				"subinstanceid": datas[i].instanceid,
				"taskcode": datas[i].taskcode,
				"cmtaskcode": datas[i].tasktaskcode,
				"inscomcode": datas[i].inscomcode
			};
		}

		if(dispatchParams.length == 0){
			alertmsg("请选择正确的任务进行操作！");
			return;
		}

		//防止重复提交
		$(this).prop("disabled", true);
		var that = this;

		$.ajax({
			url : '/cm/business/manualpushflow/undwrtSuccess',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			data :JSON.stringify(dispatchParams),
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$(that).prop("disabled", false);
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg(data.msg);
					returnBack();
					$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data.status == "fail"){
					alertmsg(data.msg);
				}
				//防止重复提交
				$(that).prop("disabled", false);
			}
		});
	});
	
	//承保打单配送成功
	$("#deliverySuccess, #packagedelivery").on("click", function(e) {
		var datas = $('#table-javascript').bootstrapTable('getSelections');
		var dispatchParams = [];
		var idx = 0;

		for (var i=0; i<datas.length; i+=1) {
			if (datas[i].taskcodename == "关闭") {
				alertmsg("("+datas[i].mainInstanceId+")任务已关闭！");
				return;
			}

			var procInstanceid;
			if (datas[i].type == "sub") {
				procInstanceid = datas[i].instanceid;
			} else if (datas[i].type == "main") {
				procInstanceid = datas[i].mainInstanceId;
			}

			//参数传递
			dispatchParams[idx++] = {
				"type": datas[i].type,
				"instanceid": procInstanceid,
				"maininstanceid": datas[i].mainInstanceId,
				"subinstanceid": datas[i].instanceid,
				"taskcode": datas[i].taskcode,
				"cmtaskcode": datas[i].tasktaskcode,
				"inscomcode": datas[i].inscomcode
			};
		}

		if(dispatchParams.length == 0){
			alertmsg("请选择正确的任务进行操作！");
			return;
		}

		//防止重复提交
		$(this).prop("disabled", true);
        var that = this;

		$.ajax({
			url : '/cm/business/manualpushflow/unwrtdeliverySuccess',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			data :JSON.stringify(dispatchParams),
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$(that).prop("disabled", false);
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg(data.msg);
					returnBack();
					$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data.status == "fail"){
					alertmsg(data.msg);
				}
				//防止重复提交
				$(that).prop("disabled", false);
			}
		});
	});
	
   //配送成功
	$("#delivery").on("click", function(e) {
		var datas = $('#table-javascript').bootstrapTable('getSelections');
		var dispatchParams = [];
		var idx = 0;

		for (var i=0; i<datas.length; i+=1) {
			if (datas[i].taskcodename == "关闭") {
				alertmsg("("+datas[i].mainInstanceId+")任务已关闭！");
				return;
			}

			var procInstanceid;
			if (datas[i].type == "sub") {
				procInstanceid = datas[i].instanceid;
			} else if (datas[i].type == "main") {
				procInstanceid = datas[i].mainInstanceId;
			}

			//参数传递
			dispatchParams[idx++] = {
				"type": datas[i].type,
				"instanceid": procInstanceid,
				"maininstanceid": datas[i].mainInstanceId,
				"subinstanceid": datas[i].instanceid,
				"taskcode": datas[i].taskcode,
				"cmtaskcode": datas[i].tasktaskcode,
				"inscomcode": datas[i].inscomcode
			};
		}

		if(dispatchParams.length == 0){
			alertmsg("请选择正确的任务进行操作！");
			return;
		}

		//防止重复提交
		$("#delivery").prop("disabled", true);

		$.ajax({
			url : '/cm/business/manualpushflow/deliverySuccess',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			data :JSON.stringify(dispatchParams),
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$("#delivery").prop("disabled", false);
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg(data.msg);
					returnBack();
					$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data.status == "fail"){
					alertmsg(data.msg);
				}
				//防止重复提交
				$("#delivery").prop("disabled", false);
			}
		});
	});
	
	//同步cm和task状态
	$("#togetherWorkflow").on("click", function(e) {
		var datas = $('#table-javascript').bootstrapTable('getSelections');
		var dispatchParams = [];
		var idx = 0;

		for (var i=0; i<datas.length; i+=1) {
			if (datas[i].tasktaskcode == datas[i].taskcode && "false" == datas[i].toSync) {
				alertmsg("("+datas[i].mainInstanceId+")任务类型与工作流类型一致,无需同步！");
				return;
			} else if (datas[i].taskcodename == "关闭") {
				alertmsg("("+datas[i].mainInstanceId+")任务已关闭,无需同步！");
				return;
			} else if (datas[i].taskcode == "18" && datas[i].tasktaskcode == "19") {
				alertmsg("("+datas[i].mainInstanceId+")核保退回修改需要前端提交,无需同步！");
				return;
			}

			//组织传递数据
			var procInstanceid;
			if (datas[i].type == "sub") {
				procInstanceid = datas[i].instanceid;
			} else if (datas[i].type == "main") {
				procInstanceid = datas[i].mainInstanceId;
			}

			//参数传递
			dispatchParams[idx++] = {
				"type": datas[i].type,
				"instanceid": procInstanceid,
				"maininstanceid": datas[i].mainInstanceId,
				"subinstanceid": datas[i].instanceid,
				"taskcode": datas[i].taskcode,
				"taskname": datas[i].tasktype,
				"cmtaskcode": datas[i].tasktaskcode,
				"cmtaskname": datas[i].taskcodename,
				"cmtaskstate": datas[i].cmtaskstate,
				"inscomcode": datas[i].inscomcode
			};
		}

		if(dispatchParams.length == 0){
			alertmsg("请选择正确的任务进行操作！");
			return;
		}

		//防止重复提交
		$("#togetherWorkflow").prop("disabled", true);

		$.ajax({
			url : '/cm/business/manualpushflow/togetherWorkflow',
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			data : JSON.stringify(dispatchParams),
			cache : false,
			async : true,
			error : function() {
				alertmsg("Connection error");
				//防止重复提交
				$("#togetherWorkflow").prop("disabled", false);
			},
			success : function(data) {
				if (data.status == "success") {
					alertmsg(data.msg);
					returnBack();
					$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
				}else if (data.status == "fail"){
					alertmsg(data.msg);
				}
				//防止重复提交
				$("#togetherWorkflow").prop("disabled", false);
			}
		});
	});
});

//当前调用的url
var pageurl = "showtasklist";
//当前调用的url查询参数
var pageurlparameter = "";

function displayElement(eleId, display) {
	var ele = document.getElementById(eleId);
	if (ele) {
		ele.style.display = display;
	}
}

function edit(row, index){
	returnBack();
	var data = $('#table-javascript').bootstrapTable('getSelections');
	if(data.length == 0){
		alertmsg("请选择正确的任务进行操作！");
		return;
	}

	var dates=Number(row.taskcode);//task表为主
	if(dates==8 || dates==18){ //人工
		displayElement("styleA", "block");
		displayElement("styleR", "block");
		displayElement("styleG", "block");
	}
	else if(dates==3 ||  dates==4 || dates==16 || dates==17 || dates==40 || dates==41){ //自动报价、核保
		displayElement("style", "block");
		displayElement("styleA", "block");
	}
	else if(dates==7){//人工规则报价
		displayElement("style", "block");
	}
	else if(dates==20){//支付
		displayElement("styleB", "block");
		displayElement("styleF", "block");
	}
	else if(dates==21){//二支
		displayElement("styleF", "block");
	}
	else if(dates==27){
		displayElement("styleC", "block");
	}
	else if(dates==23){//打单
	 	displayElement("styleE", "block");
	}
	else if(dates==25 || dates==26){//自动承保
		displayElement("style", "block");
	}
	else if(dates==24){//配送
		displayElement("styleD", "block");
	}
	else if(dates==53){//平台查询
		displayElement("styleG", "block");
	}
	else if(dates==38){//核保查询
		displayElement("style", "block");
		displayElement("styleG", "block");
	}
}

function returnBack(){
	displayElement("style", "none");
	displayElement("styleA", "none");
	displayElement("styleB", "none");
	displayElement("styleC", "none");
	displayElement("styleD", "none");
	displayElement("styleE", "none");
	displayElement("styleF", "none");
	displayElement("styleG", "none");
	displayElement("styleH", "none");
	displayElement("styleR", "none");
	displayElement("styleViewP", "none");
}

function showWorkflowButton(show) {
	if (show) {
		displayElement("styleView", "block");
	} else {
		displayElement("styleView", "none");
	}
}

function showSyncButton(show) {
	if (show) {
		displayElement("styleH", "block");
	} else {
		displayElement("styleH", "none");
	}
}

function showViewPayButton(show) {
	if (show) {
		displayElement("styleViewP", "block");
	} else {
		displayElement("styleViewP", "none");
	}
}

function checkTaskStatus() {
	var datas = $('#table-javascript').bootstrapTable('getSelections');
	if (datas.length == 0) {
		returnBack();
		showWorkflowButton(true);
		return;
	}

	if (datas.length > 1) {
		showWorkflowButton(false);

		for (var i = 0; i < datas.length-1; i += 1) {
			for (var j = i+1; j < datas.length; j += 1) {
				if (datas[i].tasktaskcode != datas[j].tasktaskcode && datas[i].taskcodename != "关闭" && datas[j].taskcodename != "关闭") {
					returnBack();
					alertmsg("状态不一致，无法进行批量处理");
					return;
				}
			}
		}
	} else {
		showWorkflowButton(true);
	}

	edit(datas[0]);

	if ("20" == datas[0].taskcode && datas.length == 1) {
		showViewPayButton(true);
	}

	for (var i = 0; i < datas.length; i += 1) {
		if (datas[i].tasktaskcode != datas[i].taskcode || "true" == datas[i].toSync) {
			showSyncButton(true);
			return;
		}
	}
}

//添加事件
function operateFormatter(value, row, index) {
    return [
        '<a class="edit m-left-5 " href="javascript:void(0)" title="处理">',
        '<i class="glyphicon glyphicon-edit"></i>',
        '</a>'
    ].join('');
}

//事件相应
window.operateEvents = {
    'click .edit': function (e, value, row, index) {
    	returnBack();
    	edit(row, index);
    }
};

// 查询条件赋值
function addParameter(parameterName, parameterValue){
	if(pageurlparameter.length == 1){
		pageurlparameter += parameterName + "=" + parameterValue;
	}else{
		pageurlparameter += "&" + parameterName + "=" + parameterValue;
	}
}

/*Array.prototype.Contain = function (item) {
    var i = 0;
    for (var i; i < this.length; i++) {
        if (this[i] === item)
            return true;
    }
    return false;
}*/

/*function sleep(numberMillis) { 
	var now = new Date(); 
	var exitTime = now.getTime() + numberMillis; 
	while (true) { 
	now = new Date(); 
	if (now.getTime() > exitTime) 
	   return; 
	} 
}*/
