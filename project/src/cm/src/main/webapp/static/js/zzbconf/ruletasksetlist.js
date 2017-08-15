require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "jqcookie", "jqtreeview","lodash","public"], function ($) {
	//数据初始化 
	$(function() {
		
		//查询条重置
		$("#resetbutton").on("click", function(e) {
			$("#tasksetname").val("");
		});
		
		//功能包查询
		$("#permissionset_query").on("click",function(){
			var tasksetname = $("#tasksetname").val();
			$('#taskset_list').bootstrapTable(
					'refresh',
					{url:'inittasksetlist?limit=10&tasksetname='+tasksetname});
		})
		
		
		//关闭panal
		$("#close_panal").on("click",function(){
			$('#myModal_taskset_add').modal('hide');
			window.location.href="menu2list";
		})
		
		
		//保存
		$("#save_taskset2rule").on("click",function(){
			var id = $("#id").val();
			//得到所有选中的人员
			$.ajax({
				url:'savetaskset2rule',
				type:'post',
				data:({tasksetId:selections.toString(),ruleId:id}),
				success:function(data){
					if(data.status=="1"){
						alertmsg(data.message);
						$('#myModal_group_add').modal('hide');
						window.location.href="menu2list";
					}else{
						alertmsg(data.message);
					}
				}
			})
		})
		
		
		
		//分页选中-------------------------------------------------------------------------
		  var $table = $('#taskset_list');
		var gid_str = $("#gids").val();
		  var gid_array =gid_str.split(",");
	      var selections = [];
	      selections =  gid_array;

		  
		  $table.on('check.bs.table check-all.bs.table ' +
	                'uncheck.bs.table uncheck-all.bs.table', function (e, rows) {
	            var ids = $.map(!$.isArray(rows) ? [rows] : rows, function (row) {
	                    return row.id;
	                }),
	                func = $.inArray(e.type, ['check', 'check-all']) > -1 ? 'union' : 'difference';
	                selections =_[func](selections, ids);
	                
	        });
	        
		  function responseHandler(res) {
			    $.each(res.rows, function (i, row) {
			        row.state = $.inArray(row.id, selections) !== -1;
			    });
			    return res;
			}
		
		
		
		$('#taskset_list').bootstrapTable({
            method: 'get',
            url: "inittasksetlist",
            cache: false,
            striped: true,
            pagination: true,
            singleSelect:false,
            responseHandler:responseHandler,
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
                field: 'id',
                title: 'id',
                align: 'center',
                valign: 'middle',
                sortable: true
            },{
                field: 'setname',
                title: '任务组名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }]
        });
	});
});
