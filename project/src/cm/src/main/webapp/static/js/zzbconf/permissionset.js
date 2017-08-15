require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "jqcookie", "jqtreeview","zTree","zTreecheck","jqvalidatei18n","public"], function ($) {
	//数据初始化 
	$(function() {
		$("#resetbutton").on("click", function(e) {
			$("#deptid").val("");
			$("#deptname").val("");
			$("#agentkind option[value='0']" ).attr("selected",true);
		});
		
		//功能包查询
		$("#permissionset_query").on("click",function(){
			/*$.ajax({
				url:'initpermissionsetlistpage?limit=10&offset=0',
				type:'get',
				dataType : "json",
				data:$("#userform").serialize(),
				success:function(data){
					$('#permisssionset_list').bootstrapTable('load', data);
				}
			})*/
			var deptid = $("#deptid").val();
			var agentkind = $("#agentkind").val();
			$('#permisssionset_list').bootstrapTable(
					'refresh',
					{
					url:'initpermissionsetlistpage?&deptid='+deptid
					+'&agentkind='+agentkind
					});
		})
		
		$("#resetbutton").on("click",function(){
			 $('#deptname').val('');
             $('#deptid').val('');
             $('#agentkind').val('');
		})
		//清除弹出机构树缓存
		$("#myModal_dept").on("hidden.bs.modal", function() {
		    $(this).removeData("bs.modal");
		});
		$("#add").on("click",function(){
			window.location.href="mian2edit?flag=add&permissionsetId=";
		})
		
		
		//删除所有代理人关系表关系
		$("#stop_permissionset").on("click",function(){
			var data = $('#permisssionset_list').bootstrapTable('getSelections');
			if(data.length!=1){
				alertmsg("请选择一条");
				return false;
			}else{
				$.ajax({
					url:'stoppermissionset',
					type:'post',
					data:({setId:data[0].id}),
					success:function(data){
						if(data.status=="1"){
							alertmsg(data.message);
							reloaddata();
						}else{
							alertmsg(data.message);
							reloaddata();
						}
					}
				})
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
						if(data.status=="1"){
							alertmsg(data.message);
							reloaddata();
						}else{
							alertmsg(data.message);
							reloaddata();
						}
					}
						
				})
			}
			
		})
		$("#delete").on("click",function(){
			//得到选中的id
			var data = $('#permisssionset_list').bootstrapTable('getSelections');
			if(confirm("删除"+data[0].setname+"权限包会将具有该权限的用户权限清空，是否确认删除？")){
				$.ajax({
					url:'deletepermissionsetbyid',
					type:'get',
					data:({setId:data[0].id}),
					success:function(data){
						if("1"==data.status){
							alertmsg(data.message);
							reloaddata();
						}else if(data.status=='2'){
							alertmsg(data.message);
						}else{
							alertmsg(data.message);
						}
					}

				})
			}
		})

		$("#update").on("click",function(){
			
			//拿到选中的setid
			var p_set_id = $('#permisssionset_list').bootstrapTable('getSelections');
			var id= p_set_id[0].id;
			window.location.href="mian2edit?flag=update&permissionsetId="+id;
		})
		//弹出机构树
		$("#deptname").on("click",function(){
			$.fn.zTree.init($("#dept_tree"), deptsetting);
			$("#myModal_dept").modal({
				 show: true
			});
			
		})
		$('#permisssionset_list').bootstrapTable({
            method: 'get',
            url: "initpermissionsetlistpage",
            cache: false,
            striped: true,
            pagination: true,
            singleSelect:true,
            pageNumber:true,
	        pageList:"[10, 20, 50, 100, 200]",
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
                title: '权限包名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'setcode',
                title: '权限包代码',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'comname',
                title: '所属机构',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'agentkindstr',
                title: '用户类型',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'statusstr',
                title: '状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            }]
        });
	});
});
var deptsetting = {
		async: {
			enable: true,
			url:"initDeptTreeByAgent",
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

function reloaddata(){
	$.ajax({
		url : 'initpermissionsetlistpage',
		type : 'GET',
		dataType : "json",
		data:({limit:10,offset:0}),
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#permisssionset_list').bootstrapTable('load', data);
		}
	});
}
