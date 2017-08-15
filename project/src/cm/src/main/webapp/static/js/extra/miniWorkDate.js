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
                    title: 'id',
                    visible: false,
                    switchable: false
                },
            	{
                    field: 'day',
                    title: 'day',
                    visible: false,
                    switchable: false
                },
                {
                    field: 'year',
                    title: '年份',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'month',
                    title: '月份',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                
                {
                    field: 'datestr',
                    title: '日期',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'weekday',
                    title: '星期',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'datetype',
                    title: '日期类型',
                    align: 'center',
                    valign: 'middle',
                    formatter:activityTypeFormatter,
                    sortable: true
                },
                {
                    field: 'noti',
                    title: '备注',
                    align: 'center',
                    valign: 'middle',
                    
                    sortable: true
                },
                {
					field: 'operating',
					title: '操作',
					align: 'center',
					valign: 'middle',
					switchable: false,
					formatter: operateFormatter1,
					events: operateEvents1
				}
            ]
        });
		 $("#datestr").datetimepicker({
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
		 })
		 
	});
    
    //卡片列表
		$("#toggle").on("click", function(e) {
			$('#table-list').bootstrapTable('toggleView');
		});
		
		// 查询条件【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#miniDate")[0].reset();
			
		});
		//刷新
		$("#refresh").on("click", function(e) {
			$('#table-list').bootstrapTable('refresh');
		});	
		$("#initYear").on("click", function(e) {
//			alert("ni de dedede");
			$('#initMiniDateModel').modal('show');
		});
	//新建日期按钮
		//绑定日期编辑确认事件
		$("#s_execButton").click(function () {
			if($("#s_datetype").val().trim() == ""){
				alertmsg("日期不能为空");
				return false;
			}else{
				saveMiniDate()
			
			}
			
			
		});
    // 日期列表页面【查询】按钮
		$("#querybutton").on("click", function(e) {
		
			
			/**var year= $("#year").val();
			var month= $("#month").val();
			var datestr= $("#datestr").val();
			var datetype= $("#datetype").val();
			var weekday = $("#weekday").val();
			
			**/
			$('#table-list').bootstrapTable(
					'refresh',
					{url:'queryMiniDate?' + $("#miniDate").serialize()});
						
						//limit='+pagesize+'&year=' + year + '&month=' + month + '&datestr='
					//+ datestr + '&weekday=' + weekday + '&datetype=' + datetype}
					//);
		 
		});
		//初始化年份
		$("#i_execButton").on("click", function(e){
//			alertmsg("您确定要初始化年份！如果已经初始化本年度将是删除的哦！");
			$.ajax({
				url: "initMiniDate",
				type: 'get',
				dataType: "text",
				cache: false,
				async: true,
				data: {
					year: $("#i_year").val()
				},
				error: function () {
					alertmsg("Connection error");
				},
				success: function (data) {
					if (data == "success") {
						$('#initMiniDateModel').modal('hide');
						$("#querybutton").click();
						alertmsg("操作成功");
					}else if (data == "fail") {
						alertmsg("日期未被初始化");
					}else{
						
					
//						alert("ddddddddddddd");
						$("#querybutton").click();
						
						alertmsg("已删除原有的数据，系统再次初始化");
						$('#initMiniDateModel').modal('hide');
					}
					//else
					//	alertmsg("操作失败");
				}
		
			});
		});
});

//活动类型文字转换
function activityTypeFormatter(value, row, index) {
	if(value=='01'){
		return '<span class="label label-success">工作日</span>';
	
	}else{
		return '<span class="label label-danger">节假日</span>';
	}
}

//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "queryMiniDate";

//保存
function saveMiniDate(){
	$.ajax({
		url: "updateMiniDatetype",
		type: 'post',
		dataType: "text",
		cache: false,
		async: true,
		data: {
			datestr:$("#s_datestr").val(),
			id: $("#s_id").val(),
			datetype: $("#s_datetype").val(),
			year: $("#s_year").val(),
			month: $("#s_month").val(),
			day: $("#s_day").val(),
			weekday:$("#s_weekday").val(),
			noti: $("#s_noti").val()
		},
		error: function () {
			alertmsg("Connection error");
		},
		success: function (data) {
			if (data == "success") {
				$('#updateMiniDateModel').modal('hide');
				$("#querybutton").click();
				alertmsg("操作成功");
			} else if (data == "fail") {
				alertmsg("日期未配置");
			}
			else
				alertmsg("你已经删除原来的数据，需要重新初始化");
			
		}
	});
}

//初始化
function initMiniDate(){
	$.ajax({
		url: "initMiniDate",
		type: 'get',
		dataType: "text",
		cache: false,
		async: true,
		data: {
			year:$("#year").val()
			
		},
		error: function () {
			alertmsg("Connection error");
		},
		success: function (data) {
			if (data == "success") {
				$("#querybutton").click();
				alertmsg("操作成功");
			} else if (data == "fail") {
				alertmsg("日期未被初始化");
			}else{
				alert("ddddddddddddd");
				$("#querybutton").click();
				alertmsg("已删除原有的数据，请再次初始化");
			}
		}
	});
}



function operateFormatter1(value, row, index) {
	
	return ['<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
		'<i class="glyphicon glyphicon-edit"></i>',
		'</a>'].join('');

}
window.operateEvents1 = {
	'click .edit' : function(e, value, row, index) {
	
		//$(".readonlysetting").removeAttr("readonly");
		//$(".disabledsetting").removeAttr("disabled");
		//$("#s_datetype").empty();
		//$("#s_datetype").append("<option value='0'>编辑</option>");
		$("#s_id").val(row.id);
		$("#s_year").val(row.year);
		$("#s_month").val(row.month);
		$("#s_weekday").val(row.weekday);
		$("#s_datestr").val(row.datestr);
		$("#s_datetype").val(row.datetype);
		$("#s_day").val(row.day);
		$("#s_noti").val(row.noti);
		$('#updateMiniDateModel').modal('show');
	}
};


















	