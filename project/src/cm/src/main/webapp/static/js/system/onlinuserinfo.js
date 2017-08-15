require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","bootstrapdatetimepicker","bootstrapdatetimepickeri18n","jqvalidatei18n","additionalmethods","public","zTree",
         "zTreecheck",], function ($) {
	//数据初始化
	$(function() {
		
		//弹出机构树
		$("#deptname").on("click",function(){
			$.fn.zTree.init($("#dept_tree"), deptsetting);
			$("#myModal_dept").removeData("bs.modal");
			$("#myModal_dept").modal({
				 show: true
			});
			
		})
		var deptsetting = {
		async: {
			enable: true,
			url:"initdepttree",
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
		$('#table-javascript').bootstrapTable({
            method: 'get',
            //url: "showinfo",
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server', 
            pageSize: '10',
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
                field: 'usercode',
                title: '用户编码',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'name',
                title: '用户名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'groupname',
                title: '业管组名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            },{
                field: 'comname',
                title: '所属机构名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'status',
                title: '账号状态',
                align: 'center',
                valign: 'middle',
                sortable: true
//            }, {
//                field: '',
//                title: '上次登录时间',
//                align: 'center',
//                valign: 'middle',
//                sortable: true
            }, {
                field: 'unfinishedtasknum',
                title: '已分配任务数',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'onlinestatus',
                title: '在线状态',
                align: 'center',
                valign: 'middle',
//            }, {
//                field: 'modifytime',
//                title: '状态更新时间',
//                align: 'center',
//                valign: 'middle',
//                sortable: true
            }]
        });
		// 用户列表页面【查询】按钮
		$("#querybutton").on("click", function(e) {
			//功能包查询
			var usercode = $.trim($("#usercode").val());
			var username =$.trim($("#username").val());
			var onlinestatus =$("#onlinestatus").val();
			var userorganization =$("#deptid").val();			
			var groupname =$.trim($("#groupname").val());
			if (!usercode && !username && !groupname) {
				alertmsg("用户名,用户编码,业管组名称不能全为空");
				return;
			}
			$('#table-javascript').bootstrapTable(
						'refresh',
						{url:'showinfo?usercode='+usercode
							+'&username='+username+'&onlinestatus='+onlinestatus
							+'&userorganization='+userorganization
							+'&groupname='+groupname});
		 });
		
		// 查询条件【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#usercode").val("");
			$("#username").val("");
			$("#onlinestatus").val('1');
			$("#deptname").val("");
			$("#deptid").val("");
			$("#groupname").val("");
		});
		//卡片列表
		$("#toggle").on("click", function(e) {
			$('#table-javascript').bootstrapTable('toggleView');
		});
		//刷新
		$("#refresh").on("click", function(e) {
			$('#table-javascript').bootstrapTable('refresh');
		});
		
	});
	
});
//默认一页显示十条记录
//var pagesize = 10;
//当前调用的url
//var pageurl = "showinfo";
//刷新列表
function reloaddata(data){
	$.ajax({
		url : "showinfo",
		type : 'GET',
		dataType : "json",
		data:({limit:10,offset:0}),
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#table-javascript').bootstrapTable('load', data);
		}
	});
}
