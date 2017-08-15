require(["jquery", "bootstrap-table", "bootstrap", "bootstrapTableZhCn", "zTree", "zTreecheck", "public", "bootstrapdatetimepicker", "bootstrapdatetimepickeri18n"], function ($) {
//数据初始化
    $(function () {
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
            columns: [
                {
                    field: 'id',
                    title: '流水号',
                    visible: false,
                    switchable: false
                },
                {
                    field: 'taskId',
                    title: '订单号',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                
                {
                    field: 'name',
                    title: '下单人姓名',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'phoneMini',
                    title: '手机号码',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'insureName',
                    title: '车主姓名',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'carLicenseNo',
                    title: '车牌号码',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                 {
                    field: 'prvName',
                    title: '投保公司',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'insureMoney',
                    title: '保费金额',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'taskState',
                    title: '订单状态',
                    align: 'center',
                    valign: 'middle',
                    
                    sortable: true,
                    formatter:activityTypeFormatter
                }
            ]
        });
		 $("#createTimeEnd").datetimepicker({
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
		 
		 $("#createTimeStart").datetimepicker({
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
    });
    
    //卡片列表
		$("#toggle").on("click", function(e) {
			$('#table-list').bootstrapTable('toggleView');
		});
		// 查询条件【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#orderParam")[0].reset();
			$("#providerid").val("");
			$("#deptid").val("");
			
		});
    // 活动列表页面【查询】按钮
		$("#querybutton").on("click", function(e) {
		
			/**
			var name= $("#name").val();
			var phoneMini= $("#phoneMini").val();
			var taskState= $("#taskState").val();
			var taskId= $("#taskId").val();
			var carLicenseNo= $("#carLicenseNo").val();
			var createTimeStart= $("#createTimeStart").val();
			var createTimeEnd= $("#createTimeEnd").val();
			**/
			$('#table-list').bootstrapTable(
					'refresh',
					{url:'queryRefundList?'+$("#orderParam").serialize()
					}
			);
		 
		});
    
});

//活动类型文字转换
function activityTypeFormatter(value, row, index) {
	if(value=='13'){
		return '退款完毕';
	
	}else{
		return '退款中';
	}
}

//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "queryRefundList";

//刷新列表
/**
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
}**/
	