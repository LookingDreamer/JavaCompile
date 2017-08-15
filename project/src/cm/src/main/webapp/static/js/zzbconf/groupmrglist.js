require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "jqcookie", "jqtreeview","zTree","zTreecheck","public"], function ($) {
	//数据初始化 
	$(function() {
		
		$("#resetbutton").on("click", function(e) {
			$("#groupname").val("");
			$("#groupkind option[value='0']" ).attr("selected",true);
			$("#privilegestate option[value='0']" ).attr("selected",true);
		});
		
		//功能包查询
		$("#permissionset_query").on("click",function(){
			var groupname = $("#groupname").val();
			var groupkind =$("#groupkind").find("option:selected").val();
			var privilegestate =$("#privilegestate").find("option:selected").val();

			$('#group_list').bootstrapTable(
					'refresh',
					{url:'initgrouplist?limit=10&groupname='+groupname+'&groupkind='+groupkind+'&privilegestate='+privilegestate});
		
			
		})
		
		$("#resetbutton").on("click",function(){
			 $('#deptname').val('');
             $('#deptid').val('');
             $('#agentkind').find("option[value='0']").attr("selected","selected");
		})
		
		//转到通用新增页面
		$("#add").on("click",function(){
			$('#myModal_common_edit').removeData("bs.modal");
			window.location.href="mian2add";
//			window.location.href="mian2edit";
		})
		
		//转到编辑页面
		$("#update").on("click",function(){
			var data = $('#group_list').bootstrapTable('getSelections');
			if(data.length!=1){
				alertmsg("请选择一条");
				return false;
			}else{
				window.location.href="mian2edit?id="+data[0].id;
			}
			
		})
		//转到添加成员界面
		$("#update_group_member").on("click",function(){
			var data = $('#group_list').bootstrapTable('getSelections');
			if(data.length!=1){
				alertmsg("请选择一条");
				return false;
			}else{
				window.location.href="mian2groupmember?groupId="+data[0].id;
			}
			
		})
		
		$("#apply_permissionset").on("click",function(){
			var data = $('#permisssionset_list').bootstrapTable('getSelections');
			if(data.length!=1){
				alertmsg("请选择一条");
				return false;
			}else{
				$.ajax({
					url:'applicpermissionset',
					type:'post',
					data:({setId:data[0].id,isNew:data[0].status}),
					success:function(data){
						if(data.code=="1"){
							alertmsg(data.message);
							reloaddata();
						}else{
							alertmsg(data.message);
						}
					}
						
				})
			}
			
		})
		$("#delete").on("click",function(){
			//得到选中的id
			var data = $('#group_list').bootstrapTable('getSelections');
			var idArray = new Array();
			for(var i=0;i<data.length;i++){
				idArray.push(data[i].id);
			}
			if(data.length==0){
				alertmsg("请选择至少一条数据");
				return false;
			}else{
				if(confirm("您选择了"+data.length+"条数据，是否确认删除")){
			   $.ajax({
					url:'deletegroupbyid',
					type:'post',
					data:({ids:idArray.toString()}),
					success:function(data){
						
							if(1==data.status){
								alertmsg(data.message);
								reloaddata();
							}else{
								alertmsg(data.message);
								return ;
							}
						}
					
					
				})
				}
			}
		})
		$('#group_list').bootstrapTable({
            method: 'get',
            url: "initgrouplist",
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
                field: 'groupname',
                title: '群组名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'groupkindstr',
                title: '群组类型',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'groupnum',
                title: '群组成员数量',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'privilegestatestr',
                title: '权限是否生效',
                align: 'center',
                valign: 'middle'
            }]
        });
	});
});
function reloaddata(){
	$.ajax({
		url : 'initgrouplist',
		type : 'GET',
		dataType : "json",
		data:({limit:10,offset:0}),
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#group_list').bootstrapTable('load', data);
		}
	});
}

