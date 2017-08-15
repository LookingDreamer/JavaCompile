require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "jqcookie", "jqtreeview","zTree","zTreecheck","public"], function ($) {
	//数据初始化 
	$(function() {
		group_id=$("#group_id").val();
		
		//返回到列表页面
		$("#go_back").on("click",function(){
			window.location.href="menu2list";
		})
		//转到新增页面弹出
		$("#add").on("click",function(){
			/*$("#myModal_group_member_add").on("hidden.bs.modal", function() {
			    $(this).removeData("bs.modal");
			});*/
			$('#myModal_group_member_add').removeData("bs.modal");
			$("#myModal_group_member_add").modal({
				 show: true,
			     remote: 'group2memberadd?gropId='+group_id
			});
		})
		
		//转到通用新增页面
		/*$("#add").on("click",function(){
			$('#myModal_common_edit').removeData("bs.modal");
			window.location.href="mian2add";
		})*/
		
		
		//移除群组成员
		$("#remove_group_member").on("click",function(){
			if(window.confirm("确定要删除吗？")==true){
				//得到选中的id
				var data = $('#group_member_list').bootstrapTable('getSelections');
				var idArray = new Array();
				for(var i=0;i<data.length;i++){
					idArray.push(data[i].id);
				}
				$.ajax({
					url:'deletegroupmember',
					type:'post',
					data:({ids:idArray.toString(),groupId:group_id}),
					success:function(data){
						if(1==data.status){
							alertmsg(data.message);
							reloaddata();
						}else{
							alertmsg(data.message);
						}
					}
					
				})
			}
		})
		$('#group_member_list').bootstrapTable({
            method: 'get',
            url: "initgroupmemberlist?groupId="+group_id,
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
                field: 'userorganization',
                title: '所属层级',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'groupprivilegestr',
                title: '任务权限',
                align: 'center',
                valign: 'middle',
                sortable: true
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
	});
});
function reloaddata(){
	$.ajax({
        url: "initgroupmemberlist?groupId="+group_id,
		type : 'GET',
		dataType : "json",
		data:({limit:10,offset:0}),
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#group_member_list').bootstrapTable('load', data);
		}
	});
}
//添加事件
function operateFormatter(value, row, index) {
    return [
        '<a class="edit m-left-5" href="javascript:void(0)" title="修改能力">',
        '<i class="glyphicon glyphicon-edit"></i>',
        '</a>',
    ].join('');
}
//事件相应
window.operateEvents = {
    'click .edit': function (e, value, row, index) {
    	updat_group_privilege(row.id);
    }
};

//修改群组成员权限
function updat_group_privilege(id){
	$("#myModal_group_privilege_update").on("hidden.bs.modal", function() {
	    $(this).removeData("bs.modal");
	});
	$("#myModal_group_privilege_update").modal({
		 show: true,
	     remote: 'group2privilegeupdate?userId='+id+'&groupId='+group_id
	});
	
}
