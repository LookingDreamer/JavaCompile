require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","zTree","zTreecheck","public","bootstrapdatetimepicker","bootstrapdatetimepickeri18n"], function ($) {
	//数据初始化 
	$(function() {
		
		$('#table-list').bootstrapTable({
            method: 'get',
            url: pageurl,
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server', 
            pageSize: pagesize,
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
                field: 'id',
                title: 'id',
                visible: false,
                switchable:false
            },
             {
                field: 'activityid',
                title: 'activityid',
                visible: false,
                switchable:false
            },
            {
                field: 'prizeid',
                title: 'prizeid',
                visible: false,
                switchable:false
            },
				{
					field: 'phone',
					title: '手机号',
					align: 'center',
					valign: 'middle',
					sortable: false
				},
            {
                field: 'agentname',
                title: '用户姓名',
                align: 'center',
                valign: 'middle',
                sortable: false
            },
				{
					field: 'nickname',
					title: '用户昵称',
					align: 'center',
					valign: 'middle',
					sortable: false
				},
            {
                field: 'activityname',
                title: '活动名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'prizename',
                title: '奖品名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
					field: 'effectivetime',
					title: '有效时间',
					align: 'center',
					valign: 'middle',
					sortable: false
				},{
					field: 'effectivedays',
					title: '有效天数',
					align: 'center',
					valign: 'middle',
					sortable: false
				},
				{
					field: 'terminaltime',
					title: '失效时间',
					align: 'center',
					valign: 'middle',
					sortable: false
				},
				{
                field: 'createtime',
                title: '获取时间',
                align: 'center',
                valign: 'middle',
                sortable: false
            },
				{
                field: 'modifytime',
                title: '使用时间',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'counts',
                title: '数额',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'status',
                title: '状态',
                align: 'center',
                valign: 'middle',
                sortable: false
            }]
        });

		//<!--add-->
		
		
		 $("#invalidtime").datetimepicker({
			  language: 'zh-CN',
		      format: "yyyy-mm-dd",
		      weekStart: 1,
		      todayBtn: 1,
		      autoclose: 1,
		      todayHighlight: 1,
		      startView: 2,
		      forceParse: 0,
		      minView: 2,
		      pickerPosition: "bottom-right",
		      pickTime:true,
		      todayBtn:true,
		      pickDate:true,
		      autoclose:true
		 });
		 
		
		// 用户列表页面【查询】按钮
		$("#querybutton").on("click", function(e) {
			
			$('#table-list').bootstrapTable(
				'refresh',
				{url:'queryAgentPrizeList?'+$("#policymanagent").serialize()
				});
		});
		//查看详细 ，获得选中行，只能选择一个
		$("#save").on("click",function(e){
			$("#id").attr("value",'');
			$('#providersource').modal('show');
		});
		
		$("#checkProid").click(function(){
			
	    	$.ajax({
	    		url : "save?id="+$("#id").val()+"&activityid="+$("#activityid2").val()+"&prizeid="+$("#prizeid2").val(),
	    		type : 'post',
	    		dataType : "json",
	    		async : true,
	    		error : function() {
	    			alertmsg("Connection error");
	    		},
	    		success : function(data) {
	    			$('#providersource').modal('hide');
	    		}
	    	});
	    	
			
		})	;
		$("#closeProid").click(function(){
			$('#providersource').modal('hide');
		});
		
//		返回
		$('#backbutton').on("click",function(){
			history.go(-1)
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
var pageurl = "queryAgentPrizeList";
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

//刷新列表 refreshrefresh
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

