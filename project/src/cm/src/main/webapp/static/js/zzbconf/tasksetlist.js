require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "jqcookie", "jqtreeview","zTree","zTreecheck","public"], function ($) {
	//数据初始化 
	$(function() {
		$("#resetbutton").on("click", function(e) {
			$("#groupname").val("");
			$("#groupkind option[value='0']" ).attr("selected",true);
			$("#privilegestate option[value='0']" ).attr("selected",true);
			
			$('#tasksetname').val('');
            $('#tasksetkind').val('');
            $('#setstatus').find("option[value='0']").attr("selected","selected");
		});
		
		$("#go_back").on("click",function(){
			window.location.href="menu2list";
		})
		
		//功能包查询
		$("#permissionset_query").on("click",function(){
			var tasksetname = $("#tasksetname").val();
			var tasksetkind =$("#tasksetkind").find("option:selected").val();
			var setstatus =$("#setstatus").find("option:selected").val();
			console.info(tasksetname+"###"+tasksetkind+"###"+setstatus);
			$('#taskset_list').bootstrapTable(
					'refresh',
					{url:'inittasksetlist?limit=10&tasksetkind='+tasksetkind
						+'&setstatus='+setstatus
						+'&tasksetname='+tasksetname});
		})
		
		$("#resetbutton").on("click",function(){
			 $('#deptname').val('');
             $('#deptid').val('');
             $('#agentkind').find("option[value='0']").attr("selected","selected");
             
             $('#tasksetname').val('');
             $('#tasksetkind').find("option[value='0']").attr("selected","selected");
             $('#setstatus').find("option[value='0']").attr("selected","selected");
		})
		
		//转到通用新增页面
			$("#add").on("click",function(){
				$("#myModal_taskset_add").on("hidden.bs.modal", function() {
				    $(this).removeData("bs.modal");
				});
				$("#myModal_taskset_add").modal({
					backdrop:'static',
					 show: true,
				     remote: 'main2edit'
				});
			})
		
		//转到编辑页面
		$("#update").on("click",function(){
			var data = $('#taskset_list').bootstrapTable('getSelections');
			if(data.length!=1){
				alertmsg("请选择一条");
				return false;
			}else{
				
				window.location.href="main2edit?id="+data[0].id;
//				$("#myModal_taskset_add").on("hidden.bs.modal", function() {
//				    $(this).removeData("bs.modal");
//				});
//				$("#myModal_taskset_add").modal({
//					backdrop:'static',
//					 show: true,
//				     remote: 'main2edit?id='+data[0].id
//				});
			}
			
		})
		$("#delete").on("click",function(){
			//得到选中的id
			var data = $('#taskset_list').bootstrapTable('getSelections');
			if(data.length==0){
				alertmsg("请选择至少一条或多条");
				return false;
			}
			var idArray = new Array();
			for(var i=0;i<data.length;i++){
				idArray.push(data[i].id);
			}
			
			$.ajax({
				url:'deletebyids',
				type:'post',
				data:({ids:idArray.toString()}),
				success:function(data){
					if(confirm("确定删除吗")){
						if(1==data.status){
							alertmsg(data.message);
							reloaddata();
						}else{
							alertmsg(data.message);
						}
					}
					
				}
				
			})
		})
		
		//禁用
		$("#change_status").on("click",function(){
			//得到选中的id
			var data = $('#taskset_list').bootstrapTable('getSelections');
			if(data.length==0){
				alertmsg("请选择要修改数据");
				return false;
			}
			var idArray = new Array();
			for(var i=0;i<data.length;i++){
				idArray.push(data[i].id);
			}
			
			$.ajax({
				url:'changestatus',
				type:'post',
				data:({ids:idArray.toString()}),
				success:function(data){
					if(1==data.status){
						alertmsg(data.message);
						reloaddata();
					}else{
						alertmsg(data.message);
					}
				}
				
			})
		})
		$('#taskset_list').bootstrapTable({
            method: 'get',
            url: "inittasksetlist",
            cache: false,
            striped: true,
            pagination: true,
            singleSelect:false,
            sidePagination: 'server', 
            pageSize: '10',
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            }, {
           	 field: 'rownum',
             title: '序号',
             align: 'center',
             valign: 'middle',
             sortable: true
            },{
                field: 'setname',
                title: '任务组名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'setdescription',
                title: '任务组描述',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'setstatusstr',
                title: '状态',
                align: 'center',
                valign: 'middle'
            }, 
//            {
//                field: 'rulebasename',
//                title: '分配规则',
//                align: 'center',
//                valign: 'middle',
//                sortable: true
//            }, 
            {
                field: 'groupname',
                title: '业管组名称',
                align: 'center',
                valign: 'middle'
            }, {
                field: 'operating',
                title: '操作',
                align: 'center',
                valign: 'middle',
                switchable:false,
                formatter: operateFormatter,
                events: operateEvents
            }]
        });
		
		//刷新 走查询
		$('#refresh').on("click",function(){
			reloaddata();
			$('#tasksetname').val('');
            $('#tasksetkind').find("option[value='0']").attr("selected","selected");
            $('#setstatus').find("option[value='0']").attr("selected","selected");
		});
	});
});
function operateFormatter(value, row, index) {
    return [
//        '<a id="d1" class="edit m-left-5" href="javascript:void(0)" title="关联规则">',
//        '关联规则',
//        '</a>',
        '<a id="d2" class="edit m-left-5" href="javascript:void(0)" title="关联也管组">',
        '关联业管组',
        '</a>'
    ].join('');
}
//事件响应
window.operateEvents = {
    'click #d1': function (e, value, row, index) {
    	add_rule(row.id);
    },
    'click #d2': function (e, value, row, index) {
    	add_group(row.id);
    }
};


/**
 * 关联业管组
 * @param id
 */
function add_group(id){
	$("#myModal_group_add").on("hidden.bs.modal", function() {
	    $(this).removeData("bs.modal");
	});
	$("#myModal_group_add").modal({
		backdrop:'static',
		 show: true,
	     remote: 'mian2grouplist?id='+id
	});
}
/**
 * 关联规则
 * @param id
 */
function add_rule(id){
	$("#myModal_rule_add").on("hidden.bs.modal", function() {
	    $(this).removeData("bs.modal");
	});
	$("#myModal_rule_add").modal({
		backdrop:'static',
		 show: true,
	     remote: 'mian2rulelist?id='+id
	});
}
function reloaddata(){
	$.ajax({
		url : 'inittasksetlist',
		type : 'GET',
		dataType : "json",
		data:({limit:10,offset:0}),
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#taskset_list').bootstrapTable('refresh', data);
		}
	});
}
