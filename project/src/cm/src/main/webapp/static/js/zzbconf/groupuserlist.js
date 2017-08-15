require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "jqcookie", "jqtreeview","zTree","zTreecheck","lodash","public"], function ($) {
	//数据初始化 
	$(function() {
		group_id=$("#groupId").val();
		
		
		
		//弹出机构树
		$("#deptname").on("click",function(){
			$.fn.zTree.init($("#dept_tree"), deptsetting);
			$("#myModal_dept").removeData("bs.modal");
			$("#myModal_dept").modal({
				 show: true
			});
			
		})
		
		
		//关闭模式对话框
		$("#close_user_modal").on("click",function(){
			$('#myModal_group_member_add').modal('hide');
			window.location.href="mian2edit?id="+group_id;
		})
		$("#save_user2group").on("click",function(){
			//得到所有选中的人员
			$.ajax({
				url:'savegroupusers',
				type:'post',
				data:({userIds:selections.toString(),groupId:group_id}),
				success:function(data){
					if(data.status=="1"){
						alertmsg(data.message);
						$('#myModal_group_member_add').modal('hide');
						window.location.href="mian2edit?id="+group_id;
					}else{
						alertmsg(data.message);
					}
				}
			})
		})
		
		//分页选中-------------------------------------------------------------------------
		  var $table = $('#group_user_list'),
	      selections = [];
		
		  
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
		//查询
		$("#querybutton").on("click",function(){
			var groupId = $("#groupId").val();
			var name = $("#name").val();
			var usercode = $("#usercode").val();
			var deptid = $("#deptid").val();
			
			$('#group_user_list').bootstrapTable(
					'refresh',
					{url:'initgroupuserlist?name='+name
						+'&groupId='+groupId
						+'&usercode='+usercode
						+'&deptid='+deptid});
		})
		// 查询条件【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#groupId").val("");
			$("#name").val("");
			$("#usercode").val("");
			$("#deptid").val("");
			$("#deptname").val("");
		});
		
		$('#group_user_list').bootstrapTable({
			contentType:"application/x-www-form-urlencoded",
            method: 'get',
            url: "initgroupuserlist?groupId="+group_id,
            cache: false,
            striped: true,
            pagination: true,
            singleSelect:false,
            sidePagination: 'server', 
            pageSize: '3',
            minimumCountColumns: 2,
            responseHandler:responseHandler,
            clickToSelect: true,
            columns: [{
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            }, {
                field: 'usercode',
                title: '管理员账号',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'name',
                title: '姓名',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'comname',
                title: '所属层级',
                align: 'center',
                valign: 'middle',
                sortable: true
            }]
        });
	});
	var deptsetting = {
			async: {
				enable: true,
				url:"initmemberdepttree",
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
	function deptTreeOnCheck(event, treeId, treeNode) {
		$("#deptid").val(treeNode.id);
		$("#deptname").val(treeNode.name);
		$('#myModal_dept').modal("hide");
	}

});