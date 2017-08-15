//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "showmytask"; 
//当前页
var pagecurrent=1;
//总页数
var pagecount=0;

require(["jquery","core","public"], function ($) {
	if($(window.top.document).find("#menu").css("display")=="none"){
		$("#taskViewList").show();
	}
	
	//回车快捷查询 
	$("#simplequery").keypress(function (e) { 
		var keyCode = e.keyCode ? e.keyCode : e.which ? e.which : e.charCode; 
		if (keyCode == 13){ 
			fmsubmit();
		} 
	});
	function refreshTask() {
		$.ajax({
			url : "queryMyTask",
			type : 'post',
			dataType : "html",
			data: {
				"jobnumtype":$("#jobnumtype").val(),
				"insuredname":$("#insuredname").val(),
				"carlicenseno":$("#carlicenseno").val(),
				"enddate":$("#enddate").val(),
				"startdate":$("#startdate").val(),
				"taskcode":$("#taskcode").val(),
				"agentname":$("#agentname").val(),
				"agentnum":$("#agentnum").val(),
				"currentpage":1,
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				$("#myTaskSon").empty();
				$("#myTaskSon").html(data);
			}
		});
	}
	
	//数据初始化 
	$(function() {
		$(window.top.document).find("#im_mytask").removeClass("blink");
		
		$(".row").hover(function(){
			$(this).find("img").attr("src","/cm/static/images/system/resource/resource2.png");
			if($(this).find("label:contains('渠道来源')").text()!="渠道来源：-"){
//				$(this).find("label:contains('渠道来源')").css("color","red");
			}
		},
		function(){
			$(this).find("img").attr("src","/cm/static/images/system/resource/resource1.png");
//			$(this).find("label:contains('渠道来源')").css("color","");
		}
		);
/*		$(".form_datetime").datetimepicker({
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
		$("#ad_search").hide();
		
		//高级搜索
		$("#gotoad_search").on("click", function(e) {
			$("#ad_search").show();
			$("#simplequery").val("");
			$("#normal_search").hide();
		 });
		//返回
		$("#return").on("click", function(e) {
			$("#ad_search").hide();
			$("#normal_search").show();
		});*/
		
	});
});

//封装 简单条件
//function queryrule1(){
//	var postdata = "";
//	
//	//简单查询条件
//	if($("#queryrule").val()){
//        postdata += "&carlicenseno=" + $("#queryrule").val();
//        postdata += "&insuredname=" + $("#queryrule").val();
//    }
//    postdata += "&searchtype=N";
//    return postdata;
// }
////封装高级条件
//function queryrule2(){
//	var postdata = "";
//	
//	//简单查询条件
//	if($("#ad_carlicenseno").val()){
//		postdata += "&carlicenseno=" + $("#ad_carlicenseno").val();
//	}
//	if($("#ad_insuredname").val()){
//		postdata += "&insuredname=" + $("#ad_insuredname").val();
//	}
//	//用户类型
//	if($("#usertype").val()){
//		alertmsg($("#usertype").val());
//		postdata += "&usertype=" + $("#usertype").val();
//	}
//    postdata += "&searchtype=A";
//	return postdata;
//}
//
////添加事件
//function operateFormatter(value, row, index) {
//    return [
//        '<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
//        '<i class="glyphicon glyphicon-edit"></i>',
//        '</a>',
//        '<a class="remove m-left-5" href="javascript:void(0)" title="删除">',
//        '<i class="glyphicon glyphicon-remove"></i>',
//        '</a>'
//    ].join('');
//}
////事件相应
//window.operateEvents = {
//    'click .edit': function (e, value, row, index) {
//    	updateuser(row.id);
//    },
//    'click .remove': function (e, value, row, index) {
//    	deleteuser(row.id);
//    }
//};
//
//function loaddata(data){
//	$("#tables").text("");
//	//获取总数据条数
//	$("#tasknum").text("（"+data.total+"）");
//	$("#lblToatl").text(data.total);
//	
//	if(pagecount!=data.pages){
//		//获取总的页数
//		pagecount=data.pages;
//		if(pagecurrent>pagecount){
//			pagecurrent=pagecount;
//		}
//		//设置跳转页
//		$("#goto").text("");
//		for(var i=1;i<pagecount+1;i++){
//			$("#goto").append("<option id=\"page"+i+"\" value=\""+i+"\">"+i+"</option>");
//		}
//	}
//	
//	//获取页面大小
//	pagesize=data.pagesize;
//	$("#lblPageCount").text(pagecount);
//	$("#lblCurent").text(pagecurrent);
//	showdata(data.rows);
//}
//
//function go(pagenumber){
//	pagecurrent=pagenumber;
//	reloaddata(queryrule());
//	
//}
//
//function link(taskid){
//	location.href = "/cm/business/ordermanage/underwriting?taskid="+taskid;
//}
//
//function showdata(list){
//	//取出主数据
//	for(var i=0;i<list.length;i++){
//	    if(list[i].ownername=="周杰伦"){
//    		var tabletemplate = $("#tabletemplate");
//    		tabletemplate.find("#tinsuredname").text(list[i].insuredname);
//    		tabletemplate.find("#tcarlicenseno").text(list[i].carlicenseno);
//    		tabletemplate.find("#tagentname").text(list[i].agentname);
//    		tabletemplate.find("#tinsurer").text(list[i].selectins);
//    		tabletemplate.find("#tteam").text(list[i].team);
//    		tabletemplate.find("#tnoti").text(list[i].noti);
//    		tabletemplate.find("#tstandardfullname").text(list[i].standardfullname);
//    		// tabletemplate.find("#toperating1").text(list[i].operating1);
//    		tabletemplate.find("#toperating1").attr("onclick",list[i].operating1);
//    		tabletemplate.find("#tlast").text("报价创建（完成）");
//    		$("#tables").append(tabletemplate.html());
//    	}else{
//    	    var tabletemplate = $("#tabletemplate2");
//            tabletemplate.find("#tinsuredname").text(list[i].insuredname);
//            tabletemplate.find("#tcarlicenseno").text(list[i].carlicenseno);
//            tabletemplate.find("#tagentname").text(list[i].agentname);
//            tabletemplate.find("#tpaymentmetod").text(list[i].paymentmetod);
//            tabletemplate.find("#torderno").text(list[i].orderno);
//            tabletemplate.find("#tprvid").text(list[i].prvid);
//            tabletemplate.find("#tdeptcode").text(list[i].deptcode);
//    		tabletemplate.find("#toperating1").attr("onclick",list[i].operating1);
//            tabletemplate.find("#tinsurer").text(list[i].selectins);
//            tabletemplate.find("#tnoti").text(list[i].noti);
//            tabletemplate.find("#tstandardfullname").text(list[i].standardfullname);
//            tabletemplate.find("#tlast").text("核保通过（完成）");
//            $("#tables").append(tabletemplate.html());
//    	}
//	}
//}
//
//function showMyTask(status){
//	$.ajax({
//			url : 'showmytask?page='+pagecurrent+'&status='+status,
//			type : 'get',
//			dataType : "json",
//			async : true,
//			error : function() {
//				alertmsg("Connection error");
//			},
//            success : function(data) {
//            	loaddata(data);
//			}
//		});
//}
/******************************************************start******************************************************/
/**
 * 重置
 */
function fmreset(){
	$("#jobnumtype").val("");
	$("#insuredname").val("");
	$("#carlicenseno").val("");
	$("#enddate").val("");
	$("#startdate").val("");
	$("#taskcode").val("");
	$("#taskstatus").val("");
}
/**
 * 搜索按钮查询
 */
function fmsubmit(){
	$.ajax({
		url : "queryMyTask",
		type : 'post',
		dataType : "html",
		data: {
			"simplequery":$("#simplequery").val(),
			"jobnumtype":$("#jobnumtype").val(),
			"insuredname":$("#insuredname").val(),
			"carlicenseno":$("#carlicenseno").val(),
			"enddate":$("#enddate").val(),
			"startdate":$("#startdate").val(),
			"taskcode":$("#taskcode").val(),
			"agentname":$("#agentname").val(),
			"agentnum":$("#agentnum").val(),
			"currentpage":1,
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$("#myTaskSon").empty();
			$("#myTaskSon").html(data);
		}
	});
}
/**
 * 分页查询
 */
function toPage(page){
	var currentpage = parseInt($("#soncurrentpage").val());
	var totalpage = parseInt($("#sontotalpage").val());
	if(totalpage>0){
		if(page == "next" && currentpage<totalpage ){
			page = currentpage+1;
		}else if(page=="last"){
			page = currentpage-1;
		}else if(isNaN(parseInt(page))){
			page = 1;
		}
		$.ajax({
			url : "queryMyTask",
			type : 'post',
			dataType : "html",
			data: {
				"simplequery":$("#sonsimplequery").val(),
				"jobnumtype":$("#sonjobnumtype").val(),
				"insuredname":$("#soninsuredname").val(),
				"carlicenseno":$("#soncarlicenseno").val(),
				"enddate":$("#sonenddate").val(),
				"startdate":$("#sonstartdate").val(),
				"taskcode":$("#sontaskcode").val(),
				"currentpage":page,
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				$("#myTaskSon").empty();
				$("#myTaskSon").html(data);
			}
		});
	}
	
}
/**
 * 我的任务流程跳转
 */
function myTaskForward(taskid,taskcode,inscomcode){
	var forwardURL = "";
	var close = false;
	$.ajax({
		url : "checkCloseTask",
		type : 'get',
		dataType : "json",
		async: false,
		data: {
			"instanceId":taskid,
			"providerid":inscomcode
		},
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			if(data && data == true) {
					close=true;
			}
		}
	});
	if (close == true) {
		alert("当前任务已关闭或已分配给其他业管处理，请刷新当前页面");
		refreshTask();
		return;
	}

	if(taskcode == "6"){//人工调整
		forwardURL="/cm/business/manualadjustment/showmanualadjustment?taskid="+taskid+"&taskcode="+taskcode+"&inscomcode="+inscomcode;
	}else if(taskcode == "8"){//人工录单(人工报价)
		forwardURL="/cm/business/manualrecord/showmanualrecord?taskid="+taskid+"&taskcode="+taskcode;
	}else if(taskcode == "12"){//人工回写
		forwardURL="/cm/business/manualrecord/showWriteBackRecord?taskid="+taskid+"&taskcode="+taskcode;
	}else if(taskcode == "18"){//人工核保
		forwardURL="/cm/business/manualrecord/showUnderwritingRecord?taskid="+taskid+"&taskcode="+taskcode+"&inscomcode="+inscomcode;
	}else if(taskcode == "23" || taskcode == "27"){//待承保打单taskcode == "25" || taskcode == "26" || 
		forwardURL="/cm/business/ordermanage/underwriting?taskid="+taskid+"&taskcode="+taskcode+"&inscomcode="+inscomcode;
	}else if(taskcode == "20"){//待支付
		forwardURL="/cm/business/ordermanage/underpaidding?taskid="+taskid+"&taskcode="+taskcode+"&inscomcode="+inscomcode;
	}else if(taskcode == "24"){//配送
		forwardURL="/cm/business/ordermanage/delivery?taskid="+taskid+"&taskcode="+taskcode+"&inscomcode="+inscomcode;
	}else if(taskcode == "21"){//二次支付任务
		forwardURL="/cm/business/mytask/mediumpayment?taskid="+taskid+"&taskcode="+taskcode+"&inscomcode="+inscomcode;
	}else if(taskcode == "7"){//人工规则报价
		forwardURL="/cm/business/manualprice/manualpricelist?taskid="+taskid+"&taskcode="+taskcode;
	}else if(taskcode == "999"){//认证任务
		forwardURL="/cm/business/certificationtask/showcertificationtask?taskid="+taskid+"&inscomcode="+inscomcode;
	}
	
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
/**
 * 二次支付查看支付结果
 */
function getPayResult(taskid,index){
	$.ajax({
		url : "getPayResult",
		type : 'get',
		dataType : "text",
		data: {
			"taskid":taskid,
		},
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$("#doublePaySpan"+index).show();
			$("#spanBg"+index).text(data);
			$("#spanBg"+index).show();
			showBg(index);
		}
	});
}

function refreshTask(event){
	var forwardURL = "business/mytask/queryTask";
	if($(window.top.document).find("#menu").css("display")=="none"){
//		var pos = mouseMove(event);之前是根据坐标，现修改方式
//		alert(1);
//		alert(111+pos.x);
//		if(pos.x<400){
//			alert(2);
//			$.cmTaskList('my', 'list4deal', true);
//		}else{
//			alert(3);
//			location.href=/cm/+forwardURL;
//		}
//		$.insLoading();
//		$(window.top.document).find("#desktop_content").attr("src", forwardURL);
		if($("#taskViewList span").hasClass("glyphicon-fullscreen")){
			$.cmTaskList('my', 'list4deal', true);
		}else{
			location.href=/cm/+forwardURL;
		}
	}else{
//		$.cmTaskList('my', 'list4deal', true);  //get
		location.href=/cm/+forwardURL;
	}
}
function mousePosition(ev){
	if(ev.pageX || ev.pageY){
		return {x:ev.pageX, y:ev.pageY};  
	}
	return{
		x:ev.clientX + document.body.scrollLeft - document.body.clientLeft, 
		y:ev.clientY + document.body.scrollTop -document.body.clientTop  
	}
}
function mouseMove(ev){
	ev = ev || window.event;  
	return mousePosition(ev); 
}

function openNoti(taskid, inscomcode, notiType) {
	window.parent.openLargeDialog("/cm/business/mytask/queryusercomment?taskid="+taskid+"&inscomcode="
			+inscomcode+"&notiType="+notiType);
}

/**被干掉的遮罩层 需要时放开
function showTaskFlow(taskid,index){
	$("#doublePaySpan"+index).hide();
	$("#spanBg"+index).hide();
	showBg(index);
}
function showBg(index){
	$("#bg"+index).height($("#table"+index).height()+"px");
	$("#bg"+index).width($("#table"+index).width()+"px");
	$("#bg"+index).css("top",$("#table"+index).offset().top+"px");
	$("#bg"+index).css("left",$("#table"+index).offset().left+"px");
	$("#bg"+index).show();
}
function closeBg(index){
	$("#bg"+index).hide();
}
*/