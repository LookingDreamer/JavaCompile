require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "jqform","public"], function ($) {
	
	$(function() {
		$("#querybutton").on("click", function(e) {
			loadData();
		});
		loadData();
		
		$("#uploadbutton").on("click", function(e) {
			$("#aaa").ajaxSubmit({
				url:'../file/uploadfile',
				type:'POST',
				dataType:'json',
				success: function (data) {
					alertmsg(data);
				}
			});
		});
		
	});

});

function opendetail(val){
	
	var pageindex = $("#detailpageindex").val();
	var limitdata = $("#detaillimitdata").val();
	var offsetdata = parseInt((parseInt($("#detailpageindex").val())-1)*parseInt(limitdata));
	$.ajax({
		url : 'loaddetailinfo',
		type : 'POST',
		dataType : "json",
		data: {                     //要传递的数据   
			imagetypename: function() {   
				return $("#imagetypename").val();   
			},
			offset: offsetdata,
			limit: limitdata,
			imagelibraryid: val
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			buildDetailData(data);
		}
	});
}

function loadData(){
	$("#loading").show();
	var pageindex = $("#pageindex").val();
	var limitdata = $("#limitdata").val();
	var offsetdata = parseInt((parseInt($("#pageindex").val())-1)*parseInt(limitdata));
	$.ajax({
		url : 'loaddata',
		type : 'POST',
		dataType : "json",
		data: {                     //要传递的数据   
			carlicenseno: function() {   
				return $("#carlicenseno").val();   
			},
			insuredname: function() {   
				return $("#insuredname").val();   
			},
			offset: offsetdata,
			limit: limitdata
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			buildData(data);
		}
	});
	
}

function changePageIndex(newindex){
	$("#pageindex").val(parseInt(newindex));
	loadData();
}
function firstPage(){
	$("#pageindex").val(1);
	loadData();
}
function nextPage(){
	var pageindex = $("#pageindex").val();
	$("#pageindex").val((parseInt(pageindex)+1));
	loadData();
}
function endPage(){
	var pageindex = $("#maxindex").val();
	$("#pageindex").val(pageindex);
	loadData();
}
function upPage(){
	var pageindex = $("#pageindex").val();
	$("#pageindex").val((parseInt(pageindex)-1));
	loadData();
}

function buildData(data){
	var pageindex = $("#pageindex").val();
	var limitdata = $("#limitdata").val();
	var offsetdata = parseInt((parseInt($("#pageindex").val())-1)*parseInt(limitdata));
	$("#imagemanagementdatas").empty();
	var datas = eval(data.returndatas.datas);
	var index = eval(data.returndatas.index);
	var count = eval(data.returndatas.count);
	$("#pageinfo").empty();
	if(parseInt(count)>(parseInt(index)*parseInt(limitdata))){
		var pageinfodata = "<span class=\"pagination-info\">显示" + ((parseInt(index)-1)*parseInt(limitdata)+1) + "-" + (parseInt(index)*parseInt(limitdata)) + "条记录，共" + count + "条记录</span>";
	}else{
		var pageinfodata = "<span class=\"pagination-info\">显示" + ((parseInt(index)-1)*parseInt(limitdata)+1) + "-" + parseInt(count) + "条记录，共" + count + "条记录</span>";
	}
	$("#pageinfo").html(pageinfodata);
	var maxindex = Math.floor((parseInt(count)/parseInt(limitdata))) + 1;
	$("#maxindex").val(maxindex);
	var tem = "<ul class=\"pagination\">";
	if((parseInt(index))==1){
		tem += "<li class=\"page-first disabled\"><a href=\"javascript:firstPage()\">«</a></li>";
		tem += "<li class=\"page-first disabled\"><a href=\"javascript:upPage()\">‹</a></li>";
	}else{
		tem += "<li class=\"page-first\"><a href=\"javascript:firstPage()\">«</a></li>";
		tem += "<li class=\"page-first\"><a href=\"javascript:upPage()\">‹</a></li>";
	}
	var showcount = 5;
	for(var i=1;i<=maxindex;i++){
		if((parseInt(index))<3){
			if(i>5){
				break;
			}
			if(i==(parseInt(index))){
				tem += "<li class=\"page-number active\"><a href=\"javascript:void(0)\">"+ i + "</a></li>";
			}else{
				tem += "<li class=\"page-number\"><a href=\"javascript:changePageIndex(" + i + ")\">"+ i + "</a></li>";
			}
		}else if((maxindex-parseInt(index))<2){
			if(maxindex-i>4){
				continue;
			}
			if(i==(parseInt(index))){
				tem += "<li class=\"page-number active\"><a href=\"javascript:void(0)\">"+ i + "</a></li>";
			}else{
				tem += "<li class=\"page-number\"><a href=\"javascript:changePageIndex(" + i + ")\">"+ i + "</a></li>";
			}
		}else{
			tem += "<li class=\"page-number\"><a href=\"javascript:changePageIndex(" + (index-2) + ")\">"+ (index-2) + "</a></li>";
			tem += "<li class=\"page-number\"><a href=\"javascript:changePageIndex(" + (index-1) + ")\">"+ (index-1) + "</a></li>";
			tem += "<li class=\"page-number active\"><a href=\"javascript:void(0)\">"+ index + "</a></li>";
			tem += "<li class=\"page-number\"><a href=\"javascript:changePageIndex(" + (index+1) + ")\">"+ (index+1) + "</a></li>";
			tem += "<li class=\"page-number\"><a href=\"javascript:changePageIndex(" + (index+2) + ")\">"+ (index+2) + "</a></li>";
			break;
		}
	}
	if((parseInt(index))==maxindex){
		tem += "<li class=\"page-next disabled\"><a href=\"javascript:nextPage()\">›</a></li>";
		tem += "<li class=\"page-last disabled\"><a href=\"javascript:endPage()\">»</a></li>";
	}else{
		tem += "<li class=\"page-next\"><a href=\"javascript:nextPage()\">›</a></li>";
		tem += "<li class=\"page-last\"><a href=\"javascript:endPage()\">»</a></li>";
	}
	tem += "</ul>";
	$("#pageindexinfo").empty();
	$("#pageindexinfo").html(tem);
//	<ul class="pagination">
//		<li class="page-first disabled"><a href="javascript:void(0)">«</a></li>
//		<li class="page-pre disabled"><a href="javascript:void(0)">‹</a></li>
//		<li class="page-number active"><a href="javascript:void(0)">1</a></li>
//		<li class="page-number"><a href="javascript:void(0)">2</a></li>
//		<li class="page-number"><a href="javascript:void(0)">3</a></li>
//		<li class="page-number"><a href="javascript:void(0)">4</a></li>
//		<li class="page-number"><a href="javascript:void(0)">5</a></li>
//		<li class="page-number" style="display: none"><a href="javascript:void(0)">6</a></li>
//		<li class="page-number" style="display: none"><a href="javascript:void(0)">7</a></li>
//		<li class="page-number" style="display: none"><a href="javascript:void(0)">8</a></li>
//		<li class="page-number" style="display: none"><a href="javascript:void(0)">9</a></li>
//		<li class="page-number" style="display: none"><a href="javascript:void(0)">10</a></li>
//		<li class="page-next"><a href="javascript:void(0)">›</a></li>
//		<li class="page-last"><a href="javascript:void(0)">»</a></li>
//	</ul>
	for (var i = 0; i < datas.length; i++) {
		 var item = datas[i];
		 var tempstr = "<div class=\"col-xs-7 col-sm-5 col-md-4 col-lg-3\"><div class=\"thumbnail\" style=\"border:1px solid #6680FF;width: 260px;height: 115px;\"><div style=\"float:left; width:110px; height:110px;\"><a href=\"javascript:opendetail(\'" + item.id + "\')\">";
		 tempstr += "<img style=\"width: 100px;height: 100px; border: 1px solid #ccc\" src=\"" + item.filepath + "\"></a></div><div style=\"float:left; width:140px; height:110px;\">";
		 tempstr += "<div class=\"label\" style=\"color:#000000;text-align:left;word-break:break-all; display:block; white-space:pre-wrap;word-wrap : break-word ;overflow: hidden ;\">车牌：" + item.carlicenseno + "</div>";
		 tempstr += "<div class=\"label\" style=\"color:#000000;text-align:left;word-break:break-all; display:block; white-space:pre-wrap;word-wrap : break-word ;overflow: hidden ;\">被保人：" + item.insuredname + "</div>";
		 tempstr += "<div class=\"label\" style=\"color:#000000;text-align:left;word-break:break-all; display:block; white-space:pre-wrap;word-wrap : break-word ;overflow: hidden ;\">" + item.createtime + "</div>";
		 tempstr += "</div></div></div>";
		 $("#imagemanagementdatas").append(tempstr);
	}
//	<div class="col-xs-7 col-sm-5 col-md-4 col-lg-3">
//	<div class="thumbnail" style="border:1px solid #6680FF;width: 260px;height: 115px;">
//			<div style="float:left; width:110px; height:110px;">
//                 <a href="javascript:opendetail('asfasdfasdfasd')"><img style="width: 100px;height: 100px; border: 1px solid #ccc" src="http://cos.myqcloud.com/1002670/img_com/1211191001/921500009/2467de4687ab409eb140cc8b294b4bb0/img1437129352479099.jpg"></a>
//            </div>
//            <div style="float:left; width:140px; height:110px;">
//                 <div class="label" style="color:#000000;text-align:left;word-break:break-all; display:block; white-space:pre-wrap;word-wrap : break-word ;overflow: hidden ;">车牌：鲁A23133</div>
//                 <div class="label" style="color:#000000;text-align:left;word-break:break-all; display:block; white-space:pre-wrap;word-wrap : break-word ;overflow: hidden ;">被保人：中华人民共和国中央人民政府</div>
//                 <div class="label" style="color:#AAAAAA;text-align:left;word-break:break-all; display:block; white-space:pre-wrap;word-wrap : break-word ;overflow: hidden ;">2015-07-17 18:23:33</div>
//            </div>
//    </div>
//</div>

}

function buildDetailData(data){
	var pageindex = $("#detailpageindex").val();
	var limitdata = $("#detaillimitdata").val();
	var offsetdata = parseInt((parseInt($("#detailpageindex").val())-1)*parseInt(limitdata));
	$("#deatialinfoid").empty();
	var datas = eval(data.returndatas.datas);
	var index = eval(data.returndatas.index);
	var count = eval(data.returndatas.count);
	$("#detailpageinfo").empty();
	if(parseInt(count)>(parseInt(index)*parseInt(limitdata))){
		var pageinfodata = "<span class=\"pagination-info\">显示" + ((parseInt(index)-1)*parseInt(limitdata)+1) + "-" + (parseInt(index)*parseInt(limitdata)) + "条记录，共" + count + "条记录</span>";
	}else{
		var pageinfodata = "<span class=\"pagination-info\">显示" + ((parseInt(index)-1)*parseInt(limitdata)+1) + "-" + parseInt(count) + "条记录，共" + count + "条记录</span>";
	}
	$("#detailpageindexinfo").html(pageinfodata);
	var maxindex = Math.floor((parseInt(count)/parseInt(limitdata))) + 1;
	$("#detailmaxindex").val(maxindex);
	var tem = "<ul class=\"pagination\">";
	if((parseInt(index))==1){
		tem += "<li class=\"page-first disabled\"><a href=\"javascript:firstPage()\">«</a></li>";
		tem += "<li class=\"page-first disabled\"><a href=\"javascript:upPage()\">‹</a></li>";
	}else{
		tem += "<li class=\"page-first\"><a href=\"javascript:firstPage()\">«</a></li>";
		tem += "<li class=\"page-first\"><a href=\"javascript:upPage()\">‹</a></li>";
	}
	var showcount = 5;
	for(var i=1;i<=maxindex;i++){
		if((parseInt(index))<3){
			if(i>5){
				break;
			}
			if(i==(parseInt(index))){
				tem += "<li class=\"page-number active\"><a href=\"javascript:void(0)\">"+ i + "</a></li>";
			}else{
				tem += "<li class=\"page-number\"><a href=\"javascript:changePageIndex(" + i + ")\">"+ i + "</a></li>";
			}
		}else if((maxindex-parseInt(index))<2){
			if(maxindex-i>4){
				continue;
			}
			if(i==(parseInt(index))){
				tem += "<li class=\"page-number active\"><a href=\"javascript:void(0)\">"+ i + "</a></li>";
			}else{
				tem += "<li class=\"page-number\"><a href=\"javascript:changePageIndex(" + i + ")\">"+ i + "</a></li>";
			}
		}else{
			tem += "<li class=\"page-number\"><a href=\"javascript:changePageIndex(" + (index-2) + ")\">"+ (index-2) + "</a></li>";
			tem += "<li class=\"page-number\"><a href=\"javascript:changePageIndex(" + (index-1) + ")\">"+ (index-1) + "</a></li>";
			tem += "<li class=\"page-number active\"><a href=\"javascript:void(0)\">"+ index + "</a></li>";
			tem += "<li class=\"page-number\"><a href=\"javascript:changePageIndex(" + (index+1) + ")\">"+ (index+1) + "</a></li>";
			tem += "<li class=\"page-number\"><a href=\"javascript:changePageIndex(" + (index+2) + ")\">"+ (index+2) + "</a></li>";
			break;
		}
	}
	if((parseInt(index))==maxindex){
		tem += "<li class=\"page-next disabled\"><a href=\"javascript:nextPage()\">›</a></li>";
		tem += "<li class=\"page-last disabled\"><a href=\"javascript:endPage()\">»</a></li>";
	}else{
		tem += "<li class=\"page-next\"><a href=\"javascript:nextPage()\">›</a></li>";
		tem += "<li class=\"page-last\"><a href=\"javascript:endPage()\">»</a></li>";
	}
	tem += "</ul>";
	$("#detailpageindexinfo").empty();
	$("#detailpageindexinfo").html(tem);
	for (var i = 0; i < datas.length; i++) {
		 var item = datas[i];
		 var tempstr = "<div class=\"col-xs-7 col-sm-5 col-md-4 col-lg-3\"><div class=\"thumbnail\" style=\"border:1px solid #6680FF;width: 260px;height: 115px;\"><div style=\"float:left; width:110px; height:110px;\"><a href=\"javascript:opendetail(\'" + item.id + "\')\">";
		 tempstr += "<img style=\"width: 100px;height: 100px; border: 1px solid #ccc\" src=\"" + item.filepath + "\"></a></div><div style=\"float:left; width:140px; height:110px;\">";
		 tempstr += "<div class=\"label\" style=\"color:#000000;text-align:left;word-break:break-all; display:block; white-space:pre-wrap;word-wrap : break-word ;overflow: hidden ;\">车牌：" + item.id + "</div>";
		 tempstr += "<div class=\"label\" style=\"color:#000000;text-align:left;word-break:break-all; display:block; white-space:pre-wrap;word-wrap : break-word ;overflow: hidden ;\">被保人：" + item.id + "</div>";
		 tempstr += "<div class=\"label\" style=\"color:#000000;text-align:left;word-break:break-all; display:block; white-space:pre-wrap;word-wrap : break-word ;overflow: hidden ;\">" + item.createtime + "</div>";
		 tempstr += "</div></div></div>";
		 $("#deatialinfoid").append(tempstr);
	}
}