require([ "jquery", "bootstrap-table", "bootstrap", "bootstrapTableZhCn",
		"zTree", "zTreecheck", "bootstrapdatetimepicker",
		"bootstrapdatetimepickeri18n","public" ], function($) {
	// 数据初始化
//	$(function() {
//	});

		// 选择机构
		$("#checkdept").on("click", function(e) {
			$('#showdept').modal();
			$.fn.zTree.init($("#depttree"), deptsetting);
		});
		var deptsetting = {
			async : {
				enable : true,
				url : "querydepttree",
				autoParam : [ "id" ],
				dataType : "json",
				type : "post"
			},
			check : {
				enable : true,
				chkStyle : "radio",
				radioType : "all"
			},
			callback : {
				onCheck : deptTreeOnCheck
			}
		};
		function deptTreeOnCheck(event, treeId, treeNode) {
			$("#deptid").val(treeNode.id);
			$("#deptname").val(treeNode.name);
			$('#showdept').modal("hide");
		}
		
		// 弹出框 选择机构2
		$("#checkdept2").on("click", function(e) {
			$('#showdept').modal();
			$.fn.zTree.init($("#depttree"), deptsetting2);
		});
		var deptsetting2 = {
			async : {
				enable : true,
				url : "querydepttree",
				autoParam : [ "id" ],
				dataType : "json",
				type : "post"
			},
			check : {
				enable : true,
				chkStyle : "radio",
				radioType : "all"
			},
			callback : {
				onCheck : deptTreeOnCheck2
			}
		};
		function deptTreeOnCheck2(event, treeId, treeNode) {
			$("#deptid2").val(treeNode.id);
			$("#deptname2").val(treeNode.name);
			$('#showdept').modal("hide");
		}


	
	// 工作时间【编辑】按钮弹窗 添加删除时间控件----工作时间
	$("#addtrbtn").on("click", function() {

			indexi=indexi+1;

		var tr1 = $("#editworktimetable tr:gt(0):eq(2)").clone();
		var tr2 = $("#editworktimetable tr:gt(0):eq(3)").clone();
		tr1.show();
		tr2.show();
		
		$(tr1.find(' select').get(0)).attr("name" , "workTimeArea["+indexi+"].workdaystart");
		$(tr1.find(' select').get(1)).attr("name" , "workTimeArea["+indexi+"].workdayend");
		$(tr2.find(' input').get(0)).attr("name" , "workTimeArea["+indexi+"].daytimestart");
		$(tr2.find(' input').get(1)).attr("name" , "workTimeArea["+indexi+"].daytimeend");
		
		$("#addtr").after(tr2);
		$("#addtr").after(tr1);

		formattime();
		
		deletetrtr();
	});
	
	// 节假日时间【编辑】按钮弹窗 添加删除时间控件----节假日
	$("#addtrbtn_holiday").on("click", function() {

			indexi=indexi+1;

		var tr1 = $("#editholidaytable tr:gt(0):eq(1)").clone();
		var tr2 = $("#editholidaytable tr:gt(0):eq(2)").clone();
		tr1.show();
		tr2.show();
		
		var holidayOrWork=$("#holidayOrWork").attr("sign");

		if(holidayOrWork=="holidayArea"){
			$(tr1.find('input').get(0)).attr("name" , "holidayArea["+indexi+"].datestart");
			$(tr1.find('input').get(1)).attr("name" , "holidayArea["+indexi+"].dateend");
			$(tr2.find('input').get(0)).attr("name" , "holidayArea["+indexi+"].daytimestart");
			$(tr2.find('input').get(1)).attr("name" , "holidayArea["+indexi+"].daytimeend");
		} else if(holidayOrWork=="holidayWorkArea"){
			$(tr1.find('input').get(0)).attr("name" , "holidayWorkArea["+indexi+"].datestart").addClass("myform_date");
			$(tr1.find('input').get(1)).attr("name" , "holidayWorkArea["+indexi+"].dateend").addClass("myform_date");
			$(tr2.find('input').get(0)).attr("name" , "holidayWorkArea["+indexi+"].daytimestart").addClass("myform_time");
			$(tr2.find('input').get(1)).attr("name" , "holidayWorkArea["+indexi+"].daytimeend").addClass("myform_time");
		} 
		
		$("#addtr_holiday").after(tr2);
		$("#addtr_holiday").after(tr1);
		
		formattime();
		
		deletetrtr();
	});
	
	// 工作时间【编辑】按钮弹窗 添加删除时间控件----工作时间提醒
	$("#addtrbtn_workremind").on("click", function() {

			indexi=indexi+1;

		var tr1 = $("#editworkremindtable tr:gt(0):eq(1)").clone();
		var tr2 = $("#editworkremindtable tr:gt(0):eq(2)").clone();
		tr1.show();
		tr2.show();
		
		$(tr1.find(' select').get(0)).attr("name" , "remindWorkTimeArea["+indexi+"].workdaystart");
		$(tr1.find(' select').get(1)).attr("name" , "remindWorkTimeArea["+indexi+"].workdayend");
		$(tr2.find(' input').get(0)).attr("name" , "remindWorkTimeArea["+indexi+"].daytimestart");
		$(tr2.find(' input').get(1)).attr("name" , "remindWorkTimeArea["+indexi+"].daytimeend");

		$("#addtr_workremind").after(tr2);
		$("#addtr_workremind").after(tr1);

		formattime();
		
		deletetrtr();
	});


	// 选择机构【查询】按钮
	$("#querybutton").on("click", function(e) {
		if ($("#deptid").val()) {
			var postdata = "";
			postdata = $("#deptid").val();
			reloaddata(postdata);
		}
		if (!$("#deptid").val()) {
			
			if($("#onlyfuture").is(':checked')){
				goquery(1);
			}else{
				goquery(2);
			}
		}
	});

	function reloaddata(data) {
		var deptid = $("#deptid").val(data);
		$("#querydeptform").submit();
	}

	// 分页控制
	// 首页
	$('#first').on("click", function(e) {
		if($("#onlyfuture").is(':checked')){
			$("#onlyfuture2").val("1");
		}
		$("#currentPage").val(1);
		$("#currentPageform").submit();
	});
	// 末页
	$('#last').on("click", function(e) {
		if($("#onlyfuture").is(':checked')){
			$("#onlyfuture2").val("1");
		}
		$("#currentPage").val($.trim($("#lblPageCount").text()));
		$("#currentPageform").submit();
	});

	// 上一页
	$('#previous').on("click", function(e) {
		if($("#onlyfuture").is(':checked')){
			$("#onlyfuture2").val("1");
		}
		var pagecurrent = $("#lblCurent").html();
		if (pagecurrent > 0) {
			pagecurrent--;
			$("#currentPage").val(pagecurrent);
			$("#currentPageform").submit();
		}
	});

	// 下一页
	$('#next').on("click", function(e) {
		if($("#onlyfuture").is(':checked')){
			$("#onlyfuture2").val("1");
		}
		var pagecurrent = $("#lblCurent").html();
		var pagecount = $.trim($("#lblPageCount").text());
		if (pagecurrent < pagecount) {
			pagecurrent++;
			$("#currentPage").val(pagecurrent);
			$("#currentPageform").submit();
		}
	});

});

function go(pagenumber) {
	$("#currentPage").val(pagenumber);
	if($("#onlyfuture").is(':checked')){
		$("#onlyfuture2").val("1");
	}else if(!$("#onlyfuture").is(':checked')){
		$("#onlyfuture2").val(null);
	}
	$("#currentPageform").submit();
}

function goquery(checknumber){
	if(checknumber==1){
		$("#onlyfuture2").val("1");
	}else if(checknumber==2){
		$("#onlyfuture2").val(null);
	}
	$("#currentPageform").submit();
}


var indexi= -1;
var alldata;

//打开编辑弹窗  ajax查询js赋值----工作时间
function openmodal(id) {
	$.ajax({
		url : "queryEditOneBydeptid",
		type : 'GET',
		dataType : "json",
		data: "deptid="+id,
		async : true,
		error : function() {
			console.info("Connection error");
		},
		success : function(data) {
			alldata=data;
			console.info(data);
			if(data.rowList[0].workdaylist !=null && data.rowList[0].workdaylist.length > 0){
				indexi=data.rowList[0].workdaylist.length -1;
				for(var i=0 ; i < data.rowList[0].workdaylist.length ; i++){

					
					var tr1 = $("#editworktimetable tr:gt(0):eq(2)").clone();
					var tr2 = $("#editworktimetable tr:gt(0):eq(3)").clone();
					
					$(tr1.find(' select').get(0)).attr("name" , "workTimeArea["+i+"].workdaystart");
					$(tr1.find(' select').get(1)).attr("name" , "workTimeArea["+i+"].workdayend");
					$(tr2.find('input').get(0)).attr("name" , "workTimeArea["+i+"].daytimestart");
					$(tr2.find('input').get(1)).attr("name" , "workTimeArea["+i+"].daytimeend");
					
					$(tr1.find('input').get(1)).attr("name" , "workTimeArea["+i+"].id");
					$(tr1.find('input').get(2)).attr("name" , "workTimeArea["+i+"].insbworktimeid");
					
					tr1.show();
					tr2.show();
					$("#addtr").before(tr1);
					$("#addtr").before(tr2);
					
					var inscdeptid=data.rowList[0].deptid;
					var worktimeid=data.rowList[0].insbworktimeid;
					var areaid=data.rowList[0].workdaylist[i].id;
					var weekstart=data.rowList[0].workdaylist[i].workdaystart;
					var weekend=data.rowList[0].workdaylist[i].workdayend;
					var daytimestart=data.rowList[0].workdaylist[i].daytimestart;
					var daytimeend=data.rowList[0].workdaylist[i].daytimeend;
					
					tr1.find(' select').get(0).value=weekstart;
					tr1.find(' select').get(1).value=weekend;
					tr2.find(' input').get(0).value=daytimestart;
					tr2.find(' input').get(1).value=daytimeend;
					
					$(tr1.find('input').get(0)).attr("timeid",areaid);
					tr1.find('input').get(1).value=areaid;
					tr1.find('input').get(2).value=worktimeid;
					
					
					formattime();
					
					deletetrtr();
				}
			};
			
			if(data.rowList[0].networkstate=='0'){
				$('#networkstate0').attr('checked', true);
			}else if(data.rowList[0].networkstate=='1'){
				$('#networkstate1').attr('checked', true);
			}
			
			$("#insbworktimeid2").val(data.rowList[0].insbworktimeid);
			$("#deptid2").val(data.rowList[0].deptid);
			$("#deptname2").val(data.rowList[0].deptname);
			$("#noworkwords").val(data.rowList[0].noworkwords);
			if (data.rowList[0].paytimetype == "0") {
				$('input[id="payMethod"][value="0"]').attr('checked', true);
			}
			if (data.rowList[0].paytimetype == "1") {
				$('input[id="payMethod"][value="1"]').attr('checked', true);
			}
			
			$('#editworktime').modal();
		}
	});
}

//添加上班时间
function openmodal_new(){
	$('#editworktime').removeData('bs.modal');
	$('#editworktime').modal();
}

//打开编辑弹窗  ajax查询js赋值----节假日
function openmodal_holiday(id){
	$.ajax({
		url : "queryEditOneBydeptid",
		type : 'GET',
		dataType : "json",
		data: "deptid="+id,
		async : true,
		error : function() {
			console.info("Connection error");
		},
		success : function(data) {
			alldata=data;
			
			console.info(data);
			console.info(data.rowList[0].deptname);
			console.info(data.rowList[0].workwords);
			console.info(data.rowList[0].holidaylist.length+"-----holidaylist");
			
			if(data.rowList[0].holidaylist !=null && data.rowList[0].holidaylist.length > 0){
				indexi=data.rowList[0].holidaylist.length - 1;
				for(var i=0 ; i < data.rowList[0].holidaylist.length ; i++){
					console.info(data.rowList[0].insbworktimeid+"-----holidaylist.insbworktimeid");
					console.info(data.rowList[0].holidaylist[i].datestart+"-----holidaylist.datestart");
					console.info(data.rowList[0].holidaylist[i].dateend+"-----holidaylist.dateend");
					console.info(data.rowList[0].holidaylist[i].daytimestart+"-----holidaylist.daytimestart");
					console.info(data.rowList[0].holidaylist[i].daytimeend+"-----holidaylist.daytimeend");
					

					
					var tr1 = $("#editholidaytable tr:gt(0):eq(1)").clone();
					var tr2 = $("#editholidaytable tr:gt(0):eq(2)").clone();
					
					$(tr1.find('input').get(0)).attr("name" , "holidayArea["+i+"].datestart");
					$(tr1.find('input').get(1)).attr("name" , "holidayArea["+i+"].dateend");
					$(tr2.find('input').get(0)).attr("name" , "holidayArea["+i+"].daytimestart");
					$(tr2.find('input').get(1)).attr("name" , "holidayArea["+i+"].daytimeend");
					
					$(tr1.find('input').get(3)).attr("name" , "holidayArea["+i+"].id");
					$(tr1.find('input').get(4)).attr("name" , "holidayArea["+i+"].insbworktimeid");
					
					tr1.show();
					tr2.show();
					$("#addtr_holiday").before(tr1);
					$("#addtr_holiday").before(tr2);
					
					var inscdeptid=data.rowList[0].deptid;
					var worktimeid=data.rowList[0].insbworktimeid;
					var holidayAreaId=data.rowList[0].holidaylist[i].id;
					var datestart=data.rowList[0].holidaylist[i].datestart;
					var dateend=data.rowList[0].holidaylist[i].dateend;
					var daytimestart=data.rowList[0].holidaylist[i].daytimestart;
					var daytimeend=data.rowList[0].holidaylist[i].daytimeend;
					
					tr1.find('input').get(0).value=datestart;
					tr1.find('input').get(1).value=dateend;
					tr2.find('input').get(0).value=daytimestart;
					tr2.find('input').get(1).value=daytimeend;
					
					$(tr1.find('input').get(2)).attr("holidaylistid",holidayAreaId);
					tr1.find('input').get(3).value=holidayAreaId;
					tr1.find('input').get(4).value=worktimeid;
					
					
					formattime();
					
					deletetrtr();
				}
			};
			
			$("#holidayOrWork").attr("sign","holidayArea");
			$("#insbworktimeid3").val(data.rowList[0].insbworktimeid);
			$("#deptid3").val(data.rowList[0].deptid);
			$("#deptname3").val(data.rowList[0].deptname);
			$("#noworkwords3").val(data.rowList[0].noworkwords);
			$("#paytimetype3").val(data.rowList[0].paytimetype);
			
			formattime();
			$('#edittime_holiday').modal();
		}
	});
}
//打开编辑弹窗  ajax查询js赋值----节假日值班日
function openmodal_holidaywork(id){
	$.ajax({
		url : "queryEditOneBydeptid",
		type : 'GET',
		dataType : "json",
		data: "deptid="+id,
		async : true,
		error : function() {
			console.info("Connection error");
		},
		success : function(data) {
			alldata=data;
			
			console.info(data);
			console.info(data.rowList[0].deptname);
			console.info(data.rowList[0].workwords);
			console.info(data.rowList[0].holidayworklist.length+"-----holidayworklist@@@@@@");
			
			if(data.rowList[0].holidayworklist !=null && data.rowList[0].holidayworklist.length > 0){
				indexi=data.rowList[0].holidayworklist.length - 1;
				for(var i=0 ; i < data.rowList[0].holidayworklist.length ; i++){
					console.info(data.rowList[0].insbworktimeid+"-----holidayworklist.insbworktimeid");
					console.info(data.rowList[0].holidayworklist[i].datestart+"-----holidayworklist.datestart");
					console.info(data.rowList[0].holidayworklist[i].dateend+"-----holidayworklist.dateend");
					console.info(data.rowList[0].holidayworklist[i].daytimestart+"-----holidayworklist.daytimestart");
					console.info(data.rowList[0].holidayworklist[i].daytimeend+"-----holidayworklist.daytimeend");
					


					var tr1 = $("#editholidaytable tr:gt(0):eq(1)").clone();
					var tr2 = $("#editholidaytable tr:gt(0):eq(2)").clone();

					$(tr1.find('input').get(0)).attr("name" , "holidayWorkArea["+i+"].datestart").addClass("myform_date");
					$(tr1.find('input').get(1)).attr("name" , "holidayWorkArea["+i+"].dateend").addClass("myform_date");
					$(tr2.find('input').get(0)).attr("name" , "holidayWorkArea["+i+"].daytimestart").addClass("myform_time");
					$(tr2.find('input').get(1)).attr("name" , "holidayWorkArea["+i+"].daytimeend").addClass("myform_time");
					
					$(tr1.find('input').get(3)).attr("name" , "holidayWorkArea["+i+"].id");
					$(tr1.find('input').get(4)).attr("name" , "holidayWorkArea["+i+"].insbworktimeid");
					
					tr1.show();
					tr2.show();
					$("#addtr_holiday").before(tr1);
					$("#addtr_holiday").before(tr2);
					
					var inscdeptid=data.rowList[0].deptid;
					var worktimeid=data.rowList[0].insbworktimeid;
					var holidayWorkAreaId=data.rowList[0].holidayworklist[i].id;
					var datestart=data.rowList[0].holidayworklist[i].datestart;
					var dateend=data.rowList[0].holidayworklist[i].dateend;
					var daytimestart=data.rowList[0].holidayworklist[i].daytimestart;
					var daytimeend=data.rowList[0].holidayworklist[i].daytimeend;
					
					tr1.find('input').get(0).value=datestart;
					tr1.find('input').get(1).value=dateend;
					tr2.find('input').get(0).value=daytimestart;
					tr2.find('input').get(1).value=daytimeend;
					
					$(tr1.find('input').get(2)).attr("holidaylistid",holidayWorkAreaId);
					tr1.find('input').get(3).value=holidayWorkAreaId;
					tr1.find('input').get(4).value=worktimeid;
					
					
					formattime();
					
					deletetrtr();
				}
			};
			
			$("#holidayOrWork").attr("sign","holidayWorkArea");
			$("#insbworktimeid3").val(data.rowList[0].insbworktimeid);
			$("#deptid3").val(data.rowList[0].deptid);
			$("#deptname3").val(data.rowList[0].deptname);
			$("#noworkwords3").val(data.rowList[0].noworkwords);
			$("#paytimetype3").val(data.rowList[0].paytimetype);
			
			formattime();
			$('#edittime_holiday').modal();
		}
	});
}

//打开编辑弹窗  ajax查询js赋值----工作时间提醒
function openmodal_workremind(id) {
	$.ajax({
		url : "queryEditOneBydeptid",
		type : 'GET',
		dataType : "json",
		data: "deptid="+id,
		async : true,
		error : function() {
			console.info("Connection error");
		},
		success : function(data) {
			alldata=data;
			
			console.info(data);
			console.info(data.rowList[0].deptname);
			console.info(data.rowList[0].workwords);
			console.info(data.rowList[0].workdayremindList.length+"-----@@@@@@workdayremindList");
			
			if(data.rowList[0].workdayremindList !=null && data.rowList[0].workdayremindList.length > 0){
				indexi= data.rowList[0].workdayremindList.length -1 ;
				for(var i=0 ; i < data.rowList[0].workdayremindList.length ; i++){

					
					var tr1 = $("#editworkremindtable tr:gt(0):eq(1)").clone();
					var tr2 = $("#editworkremindtable tr:gt(0):eq(2)").clone();
					
					$(tr1.find(' select').get(0)).attr("name" , "remindWorkTimeArea["+i+"].workdaystart");
					$(tr1.find(' select').get(1)).attr("name" , "remindWorkTimeArea["+i+"].workdayend");
					$(tr2.find('input').get(0)).attr("name" , "remindWorkTimeArea["+i+"].daytimestart");
					$(tr2.find('input').get(1)).attr("name" , "remindWorkTimeArea["+i+"].daytimeend");
					
					$(tr1.find('input').get(1)).attr("name" , "remindWorkTimeArea["+i+"].id");
					$(tr1.find('input').get(2)).attr("name" , "remindWorkTimeArea["+i+"].insbworktimeid");
					
					
					tr1.show();
					tr2.show();
					$("#addtr_workremind").before(tr1);
					$("#addtr_workremind").before(tr2);
					
					var inscdeptid=data.rowList[0].deptid;
					var worktimeid=data.rowList[0].insbworktimeid;
					var areaid=data.rowList[0].workdayremindList[i].id;
					var weekstart=data.rowList[0].workdayremindList[i].workdaystart;
					var weekend=data.rowList[0].workdayremindList[i].workdayend;
					var daytimestart=data.rowList[0].workdayremindList[i].daytimestart;
					var daytimeend=data.rowList[0].workdayremindList[i].daytimeend;
					
					tr1.find(' select').get(0).value=weekstart;
					tr1.find(' select').get(1).value=weekend;
					tr2.find(' input').get(0).value=daytimestart;
					tr2.find(' input').get(1).value=daytimeend;
					
					$(tr1.find('input').get(0)).attr("timeid",areaid);
					tr1.find('input').get(1).value=areaid;
					tr1.find('input').get(2).value=worktimeid;
					
					
					formattime();
					
					deletetrtr();
				}
			};
			
			$("#insbworktimeid4").val(data.rowList[0].insbworktimeid);
			$("#deptid4").val(data.rowList[0].deptid);
			$("#deptname4").val(data.rowList[0].deptname);
			$("#workwords").val(data.rowList[0].workwords);
			
			$("#noworkwords4").val(data.rowList[0].noworkwords);
			$("#paytimetype4").val(data.rowList[0].paytimetype);
			
			$('#edittime_workremind').modal();
		}
	});
}


function closeModal() {
	$('#editworktime').removeData('bs.modal');
	$('#edittime_holiday').removeData('bs.modal');
	$('#edittime_workremind').removeData('bs.modal');
	window.location.reload();
}

//弹出框 保存按钮1
function saveedit(){
	
	var inscdeptid=$("#deptid2").val();
	
	if(inscdeptid==""||inscdeptid==null){
		alertmsg("没有选择机构");
	}else{
		
		
		$('#editform').submit();
	}
	$('#editworktime').modal('hide');
	
}
//弹出框 保存按钮2
function saveedit_holiday(){
	
	var inscdeptid3=$("#deptid3").val();
	
	if(inscdeptid3==""||inscdeptid3==null){
		alertmsg("没有选择机构");
	}else{
		if(checkFormDate()) {
			return;
		}
		if(checkFormTime()) {
			return;
		}
		$('#editform_holiday').submit();
		$('#edittime_holiday').modal('hide');

	}
}

function checkFormDate() {
	var ret = false;
	$(".myform_date").each(function(index, domEle){
		if(!$(domEle).val()){
			alertmsg("请选择日期！");
			ret = true;
			return false;
		}
	});

	return ret;
}

function checkFormTime() {
	var ret = false;
	$(".myform_time").each(function(index, domEle){
		if(!$(domEle).val()){
			alertmsg("请选择时间！");
			ret = true;
			return false;
		}
	});
	return ret;
}

//弹出框 保存按钮3
function saveedit_workremind(){
	
	var inscdeptid4=$("#deptid4").val();
	
	if(inscdeptid4==""||inscdeptid4==null){
		alertmsg("没有选择机构");
	}else{
		$('#editform_workremind').submit();
	}
	$('#edittime_workremind').modal('hide');
	
}

//【编辑】按钮弹窗删除  成对的星期时间
function deletetrtr(){
	$(".deleteClass").on("click", function() {
		indexi=indexi-1;
		
		var areaid=$(this).attr("timeid");
		var holidayareaid=$(this).attr("holidaylistid");
		var remindareaid=$(this).attr("timeid");
		
		console.info(areaid);
		console.info(holidayareaid);
		
		if(areaid!=null){
			$.ajax({
				url : "deleteOneWorktimeByid",
				type : 'GET',
				dataType : "json",
				data: "areaid="+areaid,
				async : true,
				error : function() {
					console.info("Connection error");
				},
				success : function(data) {
					console.info("success??");
				}
			});
		}
		else if(holidayareaid!=null){
			$.ajax({
				url : "deleteOneHolidayByid",
				type : 'GET',
				//dataType : "json",
				data: "holidayareaid="+holidayareaid,
				async : true,
				error : function() {
					console.info("Connection error");
				},
				success : function(data) {
					console.info("success??");
				}
			});
		}
		else if(remindareaid!=null){
			$.ajax({
				url:"deleteOneWorktimeByid",
				type:'GET',
				datatype:'json',
				data:"areaid"+remindareaid,
				async:true,
				error: function(){
					console.info("Connection error");
				},
				success: function(){
					console.info("success??");
				}
			});
		}
		
		console.info($(this).parent().parent().next().remove());
		console.info($(this).parent().parent().remove());
	});
	
}

function formattime(){
	$('.form_date').datetimepicker({
        language:  'zh-CN',
        format: 'yyyy-mm-dd',
        weekStart: 1,
        todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		minView: 2,
		forceParse: 0
    });
	$('.form_time').datetimepicker({
		language : 'zh-CN',
		weekStart : 1,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startView : 1,
		minView : 0,
		maxView : 1,
		forceParse : 0
	});
}

