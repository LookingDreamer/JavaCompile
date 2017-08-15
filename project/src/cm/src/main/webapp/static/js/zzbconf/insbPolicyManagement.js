require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","zTree","zTreecheck","public","bootstrapdatetimepicker","bootstrapdatetimepickeri18n"], function ($) {
	//数据初始化 
	$(function() {
		$('#table-list').bootstrapTable({
            method: 'get',
            url: '',
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server', 
            pageSize: pagesize,
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            }, {
                field: 'id',
                title: '保单id',
                visible: false,
                switchable:false
            }, {
                field: 'policyno',
                title: '保单号码',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'orderid',
                title: '订单号码',
                align: 'center',
                valign: 'middle',
                sortable: true
            },  {
				field: 'agentnum',
				title: '代理人工号',
				align: 'center',
				valign: 'middle',
				sortable: true
			},  {
				field: 'agentname',
				title: '代理人姓名',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
                field: 'carlicenseno',
                title: '车牌号码',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'taskid',
                title: '任务流水号',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'phonenumber',
                title: '手机号码',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'prvshotname',
                title: '供应商',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'deptname',
                title: '所属机构',
                align: 'center',
                valign: 'middle',
                clickToSelect: true,
                sortable: true
            }, {
                field: 'policystatus',
                title: '保单状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'startdate',
                title: '起保日期',
                align: 'center',
                valign: 'middle',
                sortable: true
            }]
        });
		
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
		      pickTime:true,
		      todayBtn:true,
		      pickDate:true,
		      autoclose:true,
		      endDate:new Date()
		 }).on('changeDate',function(ev){
			 var starttime =$('#startdate').val();
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
			      pickTime:true,
			      todayBtn:true,
			      pickDate:true,
			      autoclose:true,
			      endDate:new Date()
			 })
			 $("#enddate").datetimepicker('setStartDate',starttime);
			 $("#startdate").datetimepicker('hide');
		 });
		// 用户列表页面【查询】按钮
		$("#querybutton").on("click", function(e) {
//			var postdata = "";
//			if($("#policyid").val()){
//				postdata += "&policyid=" + $("#policyid").val();
//			}
//			if($("#orderid").val()){
//				postdata += "&orderid=" + $("#orderid").val();
//			}
//			if($("#carnum").val()){
//				postdata += "&carnum=" + $("#carnum").val();
//			}
//			if($("#custname").val()){
//				postdata += "&custname=" + $("#custname").val();
//			}
//			if($("#deptid").val()){
//				postdata += "&deptid=" + $("#deptid").val();
//			}
//			if($("#providerid").val()){
//				postdata += "&providerid=" + $("#providerid").val();
//			}
//			if($("#policystatus").val()){
//				postdata += "&policystatus=" + $("#policystatus").val();
//			}
//			if($("#agentnum").val()){
//				postdata += "&agentnum=" + $("#agentnum").val();
//			}
//			if($("#agentname").val()){
//				postdata += "&agentname=" + $("#agentname").val();
//			}
//			if($("#startdate").val()){
//				postdata += "&startdate=" + $("#startdate").val();
//			}
//			if($("#enddate").val()){
//				postdata += "&enddate=" + $("#enddate").val();
//			}
//			reloaddata(postdata);
			var policyid= $("#policyid").val();
			var orderid= $("#orderid").val();
			var carnum= $("#carnum").val();
			var custname= $("#custname").val();
			var deptid= $("#deptid").val();
			var providerid= $("#providerid").val();
//			var policystatus= $("#policystatus").val();
			var agentnum= $("#agentnum").val();
			var agentname= $("#agentname").val();
//			var startdate= $("#startdate").val();
//			var enddate= $("#enddate").val();
			$('#table-list').bootstrapTable(
					'refresh',
					{url:'querylist?limit=10&policyid='+policyid
						+'&orderid='+orderid+'&carnum='+carnum
						+'&custname='+custname+'&deptid='+deptid
						+'&providerid='+providerid+'&agentnum='+agentnum+'&agentname='+agentname
					});
		 });
		
		//查看详细 ，获得选中行，只能选择一个
		$("#querydetail").on("click",function(e){
			var data = $('#table-list').bootstrapTable('getSelections');
		    if(data.length == 0){
		    	alertmsg("没有行被选中！");
		    	return;
		    }else if(data.length == 1){
		    	$("#id").val(data[0].id);
		    	$("#risktype").val(data[0].risktype);
				$("#detailinfo").submit();
		    }else{
		    	alertmsg("一次只能操作一行！");
		    	return;
		    }
		});
//		返回
		$('#backbutton').on("click",function(){
//			history.go(-1)
			window.location.href="query";
		});
		// 查询条件【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#policymanagent")[0].reset();
			$("#providerid").val("");
			$("#deptid").val("");
			
		});
		//卡片列表
		$("#toggle").on("click", function(e) {
			$('#table-list').bootstrapTable('toggleView');
		});
		//刷新
		$("#refresh").on("click", function(e) {
			$('#table-list').bootstrapTable('refresh');
		});
		//选择机构
		$("#checkdept").on("click", function(e) {
			$('#showdept').modal();
			$.fn.zTree.init($("#depttree"), deptsetting);
		});
		//选择供应商
		$("#checkprovider").on("click", function(e) {
			$('#showprovider').modal();
			$.fn.zTree.init($("#providertree"), providersetting);
		});
		
	});

});
//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "querylist";
var deptsetting = {
		async: {
			enable: true,
			url:"querydepttree",
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
			onCheck: deptTreeOnCheck
		}
	};
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

//刷新列表
function reloaddata(data){
	$.ajax({
		url : pageurl,
		type : 'GET',
		dataType : "json",
		data: data+"&limit="+pagesize,
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#table-list').bootstrapTable('load', data);
		}
	});
}

function deptTreeOnCheck(event, treeId, treeNode) {
	$("#deptid").val(treeNode.id);
	$("#deptname").val(treeNode.name);
	$('#showdept').modal("hide");
}
function providerTreeOnCheck(event, treeId, treeNode) {
	$("#providerid").val(treeNode.prvcode);
	$("#providername").val(treeNode.name);
	$('#showprovider').modal("hide");
}
