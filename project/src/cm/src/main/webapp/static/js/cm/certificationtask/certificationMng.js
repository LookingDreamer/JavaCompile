require(["jquery",
	"bootstrap-table",
	"bootstrap",
	"bootstrapTableZhCn",
	"bootstrapdatetimepicker",
	"bootstrapdatetimepickeri18n",
	"jqvalidatei18n",
	"additionalmethods",
	"public",
	"jqtreeview",
	"zTree",
	"zTreecheck",
	"multiselect"], function ($) {
	//默认一页显示十条记录
	var pagesize = 10;
	//当前调用的url
	var pageurl = "certificationQuery";
	//当前调用的url查询参数
	var pageurlparameter = "";
	//简单查询车牌被保人参数
	var tasktype = "";

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
		var dateString = date.getFullYear()+"-"+todayMounth+"-"+date.getDate();
		$("#taskcreatetimeup").val(dateString);
		$("#taskcreatetimedown").val(dateString);
	}
	
	
	// 重置按钮
	$("#resetbutton").on("click", function(e) {
		$("#idno").val("");
		$("#taskcreatetimeup").val("");
		$("#taskcreatetimedown").val("");
		$("#agentNum").val("");
		$("#agentName").val("");
		$("#agentphone").val("");
		$("#province").val("");
		$("#city").val("");
		$("#status").val("0");
	});
	
	$(function() {
		initArea();
		initQueryTaskDate();
		//认证任务认领方法
		$("#dispatch").on("click", function(){
			//组织传递数据
			var carTaskData = $('#table-javascript').bootstrapTable('getSelections');
			if(carTaskData.length == 0){
				alertmsg("没有任务被选中！");
				return;
			}else if(carTaskData.length > 1){
				alertmsg("每次只能处理一条任务！");
				return;
			}
			var userid = $.trim($("#personname").val());
			if(!userid){
				alertmsg("请指定分配人员信息！");
				return;
			}
			if (carTaskData[0].orderstatus || carTaskData[0].orderstatus != 0) {
				alertmsg("只能认领认证中的任务！");
				return;
			}
			$.ajax({
				url : '/cm/business/certificationtask/getCertificationTask',
				type : 'GET',
				dataType : 'json',
				data : "cfTaskId=" + carTaskData[0].mInstanceid +"&usercode="+userid,
				cache : false,
				async : true,
				error : function() {
					alertmsg("Connection error");
				},
				success : function(data) {
					if(data.status == "success"){
						//location.href = "/cm/business/certificationtask/showcertificationtask?taskid="+carTaskData[0].mInstanceid;
						$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
						alertmsg(data.msg);
					}else if(data.status == "fail"){
						alertmsg(data.msg);
					}
				}
			});
		});
		
		pageurlparameter = "?";
		if($.trim($("#taskcreatetimeup").val())){
			addParameter("taskcreatetimeup",$.trim($("#taskcreatetimeup").val()).substring(0,10).trim() + " 00:00:00");
		}
		if($.trim($("#taskcreatetimedown").val())){
			addParameter("taskcreatetimedown",$.trim($("#taskcreatetimedown").val()).substring(0,10).trim() + " 23:59:59");
		}

		//分配业管级联右侧输入框点击事件
		$("#personname_select").change(function(){
			$("#personname").val($(this).val());
		})
		
		$('#table-javascript').bootstrapTable({
            method: 'get',
            url:'',
            cache: false,
            striped: true,
            pagination: true,
            pageList: [5, 10, 15, 20],
            sidePagination: 'server', 
            pageSize: pagesize,
            minimumCountColumns: 2,
            clickToSelect: true,
            singleSelect: true,
            showToggle:true,  //test
            cardView: false,  //test
            showColumns: true,//test
            columns: [{
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            }, {
                field: 'agentname',
                title: '代理人姓名',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'agentnum',
                title: '代理人工号',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'phone',
                title: '联系电话',
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
                field: 'buychannel',
                title: '主营业务',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'referrer',
                title: '推荐人工号',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'district',
                title: '所在地区',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'deptname',
                title: '归属网点',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'designatedoperator',
                title: '处理人',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'taskstatus',
                title: '处理状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
				field: 'detail',
				title: ' 详细信息',
				align: 'center',
				valign: 'middle',
				sortable: true,
				formatter:function(value,row,index){
					var url = "/cm/business/certificationtask/showcertificationtask?from=certificationMng&taskid="+row.mInstanceid;
					var e = '<a href='+url+'>查看详情</a> ';
					return e;
				}
			}]
        });

	});


	// 车险任务记录高级查询条件赋值
	function addParameter(parameterName,parameterValue){
		if(pageurlparameter.length == 1){
			pageurlparameter += parameterName + "=" + parameterValue;
		}else{
			pageurlparameter += "&" + parameterName + "=" + parameterValue;
		}
	}

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
		var carlicenseno=$("#agentName").val().trim();
		var idno=$("#idno").val().trim();
		var agentName=$("#status").val().trim();
		var agentNum=$("#agentphone").val().trim();
		var operatorname=$("#province").val().trim();
		var taskid=$("#city").val().trim();

		pageurlparameter = "?";
		if($.trim($("#idno").val())){
			addParameter("idno",$.trim($("#idno").val()));
		}
		if($.trim($("#agentName").val())){
			addParameter("agentName",$.trim($("#agentName").val()));
		}
		if($.trim($("#status").val())){
			addParameter("status",$.trim($("#status").val()));
		}
		if($.trim($("#agentphone").val())){
			addParameter("agentphone",$.trim($("#agentphone").val()));
		}
		if($.trim($("#province").val())){
			addParameter("province",$.trim($("#province").val()));
		}
		if($.trim($("#city").val())){
			addParameter("city",$.trim($("#city").val()));
		}
		if($.trim($("#taskcreatetimeup").val())){
			addParameter("taskcreatetimeup",$.trim($("#taskcreatetimeup").val()).substring(0,10).trim() + " 00:00:00");
		}
		if($.trim($("#taskcreatetimedown").val())){
			addParameter("taskcreatetimedown",$.trim($("#taskcreatetimedown").val()).substring(0,10).trim() + " 23:59:59");
		}
		if(pageurlparameter.length <=13){
//			pageurlparameter = "";
			//alert("至少录入一个查询条件!");
			//return;
		}
		$('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
	 });
	function changeProvince() {
		var parentid=$("#province").val();
		if (!parentid) {
			return;
		}
		$.ajax({
			url : "/cm/region/getregionsbyparentid",
			type : 'GET',
			dataType : "json",
			data : {
				parentid : parentid
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				addHtml(data);
			}
		});
	}
	function addHtml(data){
		if (!data) {
			return;
		}
		if(data.length>=1){
			$("#city").children('option').remove();
			var data_leng = data.length;
			var opp="<option value=''>请选择</option>";
			for(var i=0;i<data_leng;i++){
				opp = opp+"<option value='"+data[i].id+"'>"+data[i].comcodename+"</option>";
			}
			$("#city").append(opp);
		}
	}
});
function initArea(){
	getAreaByParentid("0",$("#province"));
}
function changeprv(){
	getAreaByParentid($("#province").val(),$("#city"));
}
function getAreaByParentid(parentid,selectobject){
	$.ajax({
		url : "/cm/region/getregionsbyparentid",
		type : 'GET',
		dataType : "json",
		data : {
			parentid:parentid
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			selectobject.empty();
			selectobject.append("<option value=''>请选择</option>");
			for (var i = 0; i < data.length; i++) {
				selectobject.append("<option value='"+data[i].id+"'>"+data[i].comcodename+"</option>");
			}
			if(data.length>0){
				selectobject.get(0).selectedIndex=0;
			}
			selectobject.trigger("change");
		}
	});
}