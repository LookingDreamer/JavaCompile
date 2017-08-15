require([ "jquery", "zTree", "zTreecheck", "flat-ui", "bootstrap-table",
		"bootstrap", "bootstrapTableZhCn","public" ], function($) {
	// 数据初始化
	$(function() {
		initTable();
		// 用户列表页面【查询】按钮
		$("#querybutton").on("click", function(e) {
			var qureyparam = {};
			var postdata = "?1=1";
			if ($("#jobnum").val()) {
			 postdata += "&jobnum=" + $("#jobnum").val();
			 qureyparam.jobnum=$("#jobnum").val();
			}
			if ($("#name").val()) {
				postdata += "&name=" + $("#name").val();
				qureyparam.name=$("#name").val();
			}
			if ($("#phone").val()) {
				postdata += "&phone=" + $("#phone").val();
				qureyparam.phone=$("#phone").val();
			}
			if ($("#rownum").val()) {
				postdata += "&rownum=" + $("#rownum").val();
				qureyparam.rownum=$("#rownum").val();
			}
			//reloaddata(postdata);
			$('#agentMessage').bootstrapTable('refresh',{url:pageurl+postdata });

		});

	});
	// 订单列表按钮 跳转到订单列表页面
	$("#orderlist").on("click", function(e) {
		location.href="/cm/orderlist/orderlist";
	});
	// 多方报价列表按钮 跳转到多方报价列表页面
	$("#multiplelist").on("click", function(e) {
		location.href="/cm/multiplelist/multiplelist";
	});
});

function initTable(queryparam){
	$('#agentMessage').bootstrapTable({
		method : 'get',
		url : '',
		//queryParams : queryparam, //参数
		cache : false,
		striped : true,
		pagination : true,
		sidePagination : 'server',
		pageSize : pagesize,
		singleSelect : 'true',
		minimumCountColumns : 2,
		clickToSelect : true,
		columns : [ {
			field : 'id',
			title : '用户id',
			visible : false,
			switchable : false
		}, {
			field : 'licenseno',
			title : '序号',
			align : 'center',
			valign : 'middle',
			sortable : false
		}, {
			field : 'name',
			title : '姓名',
			align : 'center',
			valign : 'middle',
			sortable : false
		}, {
			field : 'jobnum',
			title : '工号',
			align : 'center',
			valign : 'middle',
			sortable : false
		}, {
			field : 'phone',
			title : '电话',
			align : 'center',
			valign : 'middle',
			sortable : false
		}, {
			field : 'idno',
			title : '身份证号',
			align : 'center',
			valign : 'middle',
			sortable : false
		}, {
			field : 'operating',
			title : '操作',
			align : 'center',
			valign : 'middle',
			switchable : false,
			formatter : operateFormatter,
			events : operateEvents
		} ]
	});
}
// 默认一页显示十条记录
var pagesize = 10;
// 当前调用的url
var pageurl = "showagentmessage";

// 添加事件
function operateFormatter(value, row, index) {
	return [
			'<a class="carinsurance m-left-5" href="javascript:void(0)" title="车险投保">',
			'<i class="btn btn-link">车险投保 </i>',
			'</a>',
//			'<a class="imagemanagement m-left-5" href="javascript:void(0)" title="影像管理">',
//			'<i class="btn btn-link">影像管理</i>',
//			'</a>',
			'<a class="rapidrenewal m-left-5" href="javascript:void(0)" title="快速续保">',
			'<i class="btn btn-link">快速续保</i>',
			'</a>',
			'<a class="ordermanagement m-left-5" href="javascript:void(0)" title="订单管理">',
			'<i class="btn btn-link">订单管理</i>', '</a>',

	].join('');
}

// 事件相应
window.operateEvents = {
	'click .carinsurance' : function(e, value, row, index) {
		carInsurance(row.id,row.jobnum,"enteringindex/0");
	},
	'click .imagemanagement' : function(e, value, row, index) {
		imageManagement(row.id);
	},
	'click .rapidrenewal' : function(e, value, row, index) {
		carInsurance(row.id,row.jobnum,"rapidrenewal/");
	},
	'click .ordermanagement' : function(e, value, row, index) {
		carInsurance(row.id,row.jobnum,"myorder/3");
	},

};

function carInsurance(id,jobnum,jumpurl) {
	//alertmsg("id="+id+"&jobnum="+jobnum+"&usercode="+$("#usercode").val());
	//location.href = "http://lzgdev.cninsure.net/zhangzb";
	$.ajax({
		header:{
			"Content-Type":"application/json","appid":"com.accela.inspector"
		},
		url : "gettokenbyjobnum",
		type : 'POST',
		dataType : "json",
		data : {
			jobnum:jobnum
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
			return null;
		},
		success : function(data) {
			if(data.status=="success"){
				window.open(data.body.zhangzburl+"/#/daikeLogin/"+jobnum+"/"+data.body.token+"/"+jumpurl+"/"+data.body.userid);
			}else{
				alertmsg("获取代理人唯一标识失败！");
			}
		}
	});
	// = "http://cittest.cisg.cn/zhangzb/#/login";
	//location.href = "/cm/imagemanagement/imagemanagement?id=" + id;
}
function imageManagement(id) {
	location.href = "/cm/imagemanagement/imagemanagement?id=" + id;
}
function rapidRenewal(id,jobnum) {
	location.href = "/cm/rapidrenewal/init?id=" + id;
	
}
function orderManagement(id) {
	location.href = "/cm/business/ordermanage/ordermanagelist?id=" + id;
}
function loginvalidity(account,password){
	$.ajax({
		header:{
			"Content-Type":"application/json","appid":"com.accela.inspector"
		},
		url : "mobile/login/login",
		type : 'POST',
		dataType : "json",
		data : "{\"account\":\""+account+"\",\"password\":\""+password+"\"}",
		async : true,
		error : function() {
			alertmsg("Connection error");
			return null;
		},
		success : function(data) {
			alertmsg(data.status);
			if(data.status=="success"){
				return data.token;
			}
			return null;
		}
	});
}
// 刷新列表
function reloaddata(data) {
	var postdata;
	if ($("#jobnum").val()) {
		postdata += "&jobnum=" + $("#jobnum").val();
	}
	if ($("#name").val()) {
		postdata += "&name=" + $("#name").val();
	}
	if ($("#phone").val()) {
		postdata += "&phone=" + $("#phone").val();
	}
	if ($("#rownum").val()) {
		postdata += "&rownum=" + $("#rownum").val();
	}
	$.ajax({
		url : pageurl,
		type : 'GET',
		dataType : "json",
		data : data + "&limit=" + pagesize + "&offset=" + 0,
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#agentMessage').bootstrapTable('load', data);
		}
	});

}
