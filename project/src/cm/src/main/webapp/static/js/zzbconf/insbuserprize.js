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
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            }, {
                field: 'id',
                title: 'id',
                visible: false,
                switchable:false
            }, {
                field: 'hid',
                title: 'hid',
                visible: false,
                switchable:false
            }, {
                field: 'jid',
                title: 'jid',
                visible: false,
                switchable:false
            }, {
                field: 'uid',
                title: 'uid',
                visible: false,
                switchable:false
            }, {
                field: 'name',
                title: '用户',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'hname',
                title: '活动名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'jname',
                title: '奖品名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'gettime',
                title: '获取时间',
                align: 'center',
                valign: 'middle',
                sortable: true
            },  {
				field: 'usetime',
				title: '使用时间',
				align: 'center',
				valign: 'middle',
				sortable: true
			},  {
				field: 'counts',
				title: '奖品数额',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
                field: 'status',
                title: '奖品状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            }
			]
        });
		
		
		
		//
		
		
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
		 
		 $("#effectivetime").datetimepicker({
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
		      autoclose:true
		 })
		
		// 用户列表页面【查询】按钮
		$("#querybutton").on("click", function(e) {
			var name= $("#name").val();
			var type= $("#type").val();
			var counts= $("#counts").val();
			var effectivetime= $("#effectivetime").val();
			var invalidtime= $("#invalidtime").val();
			$('#table-list').bootstrapTable(
					'refresh',
					{url:'querylist?limit=10'
					});
		 });
		
		//查看详细 ，获得选中行，只能选择一个
		$("#save").on("click",function(e){
			var data = $('#table-list').bootstrapTable('getSelections');
		    if(data.length == 0){
		    	location.href = "edit";
		    }else if(data.length == 1){
		    	$("#id").val(data[0].id);
		    	location.href = "edit";
		    }else{
		    	alertmsg("一次只能操作一行！");
		    	return;
		    }
		});
		
		
		//查看详细 ，获得选中行，只能选择一个
		$("#edit").on("click",function(e){
			var data = $('#table-list').bootstrapTable('getSelections');
		    if(data.length == 0){
		    	alertmsg("请选择一行！");
		    	return;
		    }else if(data.length == 1){
		    	$("#id").val(data[0].id);
		    	location.href = "edit?id="+data[0].id;
		    }else{
		    	alertmsg("一次只能操作一行！");
		    	return;
		    }
		});
		
		
		//查看详细 ，获得选中行，只能选择一个
		$("#delete").on("click",function(e){
			var data = $('#table-list').bootstrapTable('getSelections');
		    if(data.length == 0){
		    	alertmsg("请选择一行！");
		    	return;
		    }else if(data.length == 1){
		    	//$("#id").val(data[0].id);
		    //	location.href = "delete?id="+data[0].id;
		    	
		    	$.ajax({
		    		url : "delete?id="+data[0].id,
		    		type : 'post',
		    		dataType : "json",
		    		async : true,
		    		error : function() {
		    			alertmsg("Connection error");
		    		},
		    		success : function(data) {
		    			$('#table-list').bootstrapTable('load', data);
		    		}
		    	});
		    	
		    	$('#table-list').bootstrapTable(
						'refresh',
						{url:'querylist?limit=10'
						});
		    }else{
		    	alertmsg("一次只能操作一行！");
		    	return;
		    }
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

