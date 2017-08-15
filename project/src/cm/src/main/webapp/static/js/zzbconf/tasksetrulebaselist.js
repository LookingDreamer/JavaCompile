require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "jqcookie", "jqtreeview","lodash","public"], function ($) {
	//数据初始化 
	$(function() {
		taskset_id=$("#tasksetid").val();
		
		$("#save_rulebase2taskset").on("click",function(){
			//得到所有选中的人员
			$.ajax({
				url:'saverulebase2taskset',
				type:'post',
				data:({ruleIds:selections.toString(),tasksetid:taskset_id}),
				success:function(data){
					if(data.status=="1"){
						alertmsg(data.message);
						$('#myModal_rule_add').modal('hide');
						window.location.href="menu2list";
					}else{
						alertmsg(data.message);
					}
				}
			})
		})
		
		//分页选中-------------------------------------------------------------------------
		  var $table = $('#taskset_rulebase_list');
		var gid_str = $("#gids").val();
		  var gid_array =gid_str.split(",");
	      var selections = [];
	      selections =  gid_array;
		  
	     
	      $table.on('check.bs.table check-all.bs.table ' +
	                'uncheck.bs.table uncheck-all.bs.table',
	                function (e, rows) {
	            		var ids = $.map(!$.isArray(rows) ? [rows] : rows, function (row) {
	                    return row.id.toString();
	                }),
	                func = $.inArray(e.type, ['check', 'check-all']) > -1 ? 'union' : 'difference';
	                selections =_[func](selections, ids);
	                
	        });
	        
		  function responseHandler(res) {
			    $.each(res.rows, function (i, row) {
			        row.state = $.inArray(row.id.toString(), selections) !== -1;
			    });
			    return res;
			}
		//查询
		$("#querybutton").on("click",function(){
			var rulename = $("#rule_name").val();
			var rulepostil = $("#rule_postil").val();
			$('#taskset_rulebase_list').bootstrapTable('refresh',{url:'inittasksetrulelist?limit=10&rulePostil='+rulepostil+'&ruleName='+rulename});
		})
		$("#close_modal").on("click",function(){
			$('#myModal_rule_add').modal('hide');
			window.location.href="menu2list";
		})
		
		//重置
		$('#resetbutton').on("click",function(){
			  $('#groupname').val('');
		})
		
		$('#taskset_rulebase_list').bootstrapTable({
            method: 'get',
            url: "inittasksetrulelist",
            cache: false,
            striped: true,
            pagination: true,
            singleSelect:false,
            sidePagination: 'server', 
            pageSize: '10',
            minimumCountColumns: 2,
            responseHandler:responseHandler,
            clickToSelect: true,
            columns: [{
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            },{
                field: 'id',
                title: '规则id',
                align: 'center',
                valign: 'middle',
                sortable: true
            },{
                field: 'ruleName',
                title: '业规则名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            },{
                field: 'rulePostil',
                title: '规则描述',
                align: 'center',
                valign: 'middle',
                sortable: true
            }]
        });
	});
});