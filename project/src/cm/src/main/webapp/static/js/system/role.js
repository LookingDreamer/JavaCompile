require([ "jquery", "bootstrap-table", "bootstrap", "bootstrapTableZhCn","jqcookie", "jqtreeview", "zTree", "zTreecheck", "lodash","public" ],
		function($) {
			// 数据初始化
			$(function() {
				var selected_role_id="";
				
				$table = $('#role-user-list');
				selections = [];

				$table.on('check.bs.table check-all.bs.table '
						+ 'uncheck.bs.table uncheck-all.bs.table', function(e,
						rows) {
					var ids = $.map(!$.isArray(rows) ? [ rows ] : rows,
							function(row) {
								return row.id;
							}), func = $.inArray(e.type,
							[ 'check', 'check-all' ]) > -1 ? 'union'
							: 'difference';
					selections = _[func](selections, ids);
				});

				function responseHandler(res) {
					$.each(res.rows, function(i, row) {
						row.state = $.inArray(row.id, selections) !== -1;
					});
					return res;
				}
				
				/*
				 * 初始化用户列表
				 */
				$('#role-users-add').bootstrapTable({
					method : 'get',
					url : "",
					singleSelect : false,
					pagination : true,
					cache : false,
					sidePagination : 'server',
					striped : true,
					minimumCountColumns : 2,
					clickToSelect : true,
					columns : [ {
						field : 'cb',
						align : 'center',
						valign : 'middle',
						checkbox : true
					}, {
						field : 'usercode',
						title : '用户编码',
						align : 'center',
						valign : 'middle',
					}, {
						field : 'name',
						title : '用户姓名',
						align : 'center',
						valign : 'middle',
					}, {
						field : 'status',
						title : '用户状态',
						align : 'center',
						valign : 'middle',
						formatter : function (value, row, index) {
							if(1==value) {
								return "启用"
							} else {
								return "停用";
							}
						},
					}
						/*, {
						field : 'rolenames',
						title : '所属角色',
						align : 'center',
						valign : 'middle',
					} */
					]
				});
				
				
				
				/*
				 * 初始化角色用户关系列表
				 */
				$('#role-user-list').bootstrapTable({
					method : 'get',
					url : "getusersbyroleid",
					singleSelect : false,
					cache : false,
					striped : true,
					pagination : true,
					responseHandler : responseHandler,
					sidePagination : 'server',
					pageList : "[10, 20, 50, 100, 200]",
					pageSize : 10,
					minimumCountColumns : 2,
					clickToSelect : true,
					columns : [ {
						field : 'state',
						align : 'center',
						valign : 'middle',
						checkbox : true
					}, {
						field : 'usercode',
						title : '用户编码',
						align : 'center',
						valign : 'middle'
					}, {
						field : 'name',
						title : '用户姓名',
						align : 'center',
						valign : 'middle'
					}, {
						field : 'status',
						title : '用户状态',
						align : 'center',
						valign : 'middle',
						formatter : function (value, row, index) {
							if(1==value) {
								return "启用"
							} else {
								return "停用";
							}
						},
					}, {
						field : 'userorganization',
						title : '所属机构',
						align : 'center',
						valign : 'middle'
					}, {
						field : 'rolenames',
						title : '所属角色',
						align : 'center',
						valign : 'middle'
					} ]

				});
				
				/*
				 * 初始化角色列表
				 */
				$('#role-data-list').bootstrapTable({
					method : 'get',
					url : "showrolelist",
					singleSelect : true,
					cache : false,
					striped : true,
					smartDisplay : true,
					pagination : true,
					pageNumber : true,
					sidePagination : 'server',
					pageList : "[10, 20, 50, 100, 200]",
					pageSize : 10,
					minimumCountColumns : 2,
					clickToSelect : true,
					columns : [ {field : 'rolename',title : '角色名称',align : 'center',valign : 'middle'} ]
				}).on('click-row.bs.table', function(e, row, $element) {
					
					$("table tr").click(function(){						
						$(this).css("background","#ccc").siblings().css("background","");
					});
					//得到当前选中的角色id
					var id = row.id;
					selected_role_id = id;
					/*
					 * 初始化角色信息
					 */
					$.ajax({
						url : 'main2show',
						type : 'POST',
						dataType : "json",
						async : true,
						data : ({
							id : id
						}),
						error : function() {
							alertmsg("Connection error");
						},
						success : function(data) {
							$("#rolename").html(data.rolename);
							$("#rolecode").html(data.rolecode);
							$("#branchinnerCode").html(data.branchinnerCode);
							$("#statusstr").html(data.statusstr);
							$("#noti").html(data.noti);
							$("#title_role").html(data.rolename);
						}
					});
					init_menu_tree(id);
					// 初始化角色用户列表
					$('#role-user-list').bootstrapTable(
							'refresh',
							{url:'getusersbyroleid?roleId='+selected_role_id});
					$table = $('#role-user-list');

				});
				
				
				/*
				 * 新增角色信息
				 */
				$("#main2add").on("click", function() {
					$("#myModal_add").on("hidden.bs.modal", function() {
						$(this).removeData("bs.modal");
					});
					$("#myModal_add").modal({
						show : true,
						remote : 'main2add'
					});
				});
				
				/*
				 * 修改角色信息
				 */
				$("#main2edit").on("click", function() {
					if(selected_role_id==""){
						alertmsg("请选择角色");
						return false;
					}
					$("#myModal_eidt").on("hidden.bs.modal", function() {
						$(this).removeData("bs.modal");
					});
					$("#myModal_eidt").modal({
						show : true,
						remote : 'main2edit?id=' + selected_role_id
					});
				});
				
				/*
				 * 删除角色信息
				 */
				$("#delete_role").on("click", function() {
					if(selected_role_id==""){
						alertmsg("请选择角色");
						return false;
					}
					if(window.confirm("确定要删除吗？")==true){
						window.location.href = "rmoverole?id=" + selected_role_id;
					}
				});
				
				/*
				 * 菜单权限保存
				 */
				$("#save_menu_ids").on("click",function() {
					// 拿到所有选中菜单ids
					var treeObj = $.fn.zTree.getZTreeObj('menuTree');
					var nodes = treeObj.getCheckedNodes(true);
					var checkedIds = '';
					for (var i = 0; i < nodes.length; i++) {
						checkedIds += nodes[i].menuid + ',';
					}
					if (checkedIds.length > 0) {
						checkedIds = checkedIds.substring(0,checkedIds.length - 1);
					}
					$.ajax({
						url : 'bindingrolemenu',type : 'POST',dataType : "json",async : true,
						data : ({
							menuIds : checkedIds,
							roleId : selected_role_id
						}),
						error : function() {
							alertmsg("Connection error");
						},
						success : function(data) {
							alertmsg(data.message);
							init_menu_tree(selected_role_id);
						}
					});
				})

				
				/*
				 * 选择tab页初始化菜单树
				 */
				$("#menuTab").on("click", function() {
					init_menu_tree(selected_role_id);
				})
				
				/*
				 * 角色添加用户查询
				 */
				$("#queryUser").on(	"click",function() {
					var name = $("#name").val();
					var usercode = $("#usercode").val();
							
					$('#role-users-add').bootstrapTable(
						'refresh',
						{url:'loaduserlist?roleId='+selected_role_id+ '&usercode=' + usercode+'&name='+name,});
				})

				// 弹出用户列表信息
				$("#add_users2role").on("click",function() {
					if(selected_role_id==""){
						alertmsg("请选择角色");
						return false;
					}
					
					$("#myModal_show").on("hidden.bs.modal", function() {
						$(this).removeData("bs.modal");
					});
					$("#myModal_show").modal({
						show : true
					});
					
					/*$('#role-users-add').bootstrapTable(
							'refresh',
							{url:'loaduserlist?roleId='+selected_role_id});*/
				});

				/*
				 * 批量删除当前角色对应用户信息
				 */
				$("#benchdeletebutton").on("click", function() {
					var ids_str = selections.toString();
					if (selections.length == 0) {
						alertmsg("请选择用户");
						return false;
					}
					if(window.confirm("确定要删除吗？")==true){
						$.ajax({
							url : 'benchdeleteusersbyroleid',
							type : 'POST',
							dataType : "json",
							data : ({
								userIds : ids_str,
								roleId : selected_role_id
							}),
							async : true,
							error : function() {
								alertmsg("Connection error");
							},
							success : function(data) {
								if ("0" == data.code) {
									alertmsg(data.message);
								} else {
									alertmsg(data.message);
								}
								$('#role-user-list').bootstrapTable(
										'refresh',
										{url:'getusersbyroleid?roleId='+selected_role_id});
								$table = $('#role-user-list');
							}
	
						})
					}
				});
				

				/*
				 * 保存选中的用户信息
				 */
				$("#save_users_roleid").on("click",	function() {
					var data = $('#role-users-add').bootstrapTable('getSelections');
					var arrayuserid = new Array();
					if (data.length == 0) {
						alertmsg("没有行被选中！");
					} else {
						for (var i = 0; i < data.length; i++) {
							arrayuserid.push(data[i].id)
						}
					}
					$.ajax({
						url : 'saveroleusers?userIds='+ arrayuserid.toString(),
						type : 'POST',
						dataType : "json",
						data : ({roleId : selected_role_id}),
						async : true,
						error : function() {
							alertmsg("Connection error");
						},
						success : function(data) {
							$('#myModal_show').modal('hide');
							$("#myModal_show").on("hidden.bs.modal", function() {
								$(this).removeData("bs.modal");
							});
							$("#usercode").val("");
							$("#name").val("");
							
							$('#role-user-list').bootstrapTable(
									'refresh',
									{url:'getusersbyroleid?roleId='+selected_role_id});
						}
					});

				})
			});
		});

// 单击左侧加载右侧角色数据展示采用ajax
function init_role_users_list(id) {

	// 重新选择角色时清除userid 数组信息
	selected_role_id = id;
	$.ajax({
		url : 'main2show',
		type : 'POST',
		dataType : "json",
		async : true,
		data : ({
			id : id
		}),
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$("#rolename").html(data.rolename);
			$("#rolecode").html(data.rolecode);
			$("#branchinnerCode").html(data.branchinnerCode);
			$("#statusstr").html(data.statusstr);
			$("#noti").html(data.noti);
			$("#title_role").html(data.rolename);
			$.ajax({
				url : "getusersbyroleid?limit=10&offset=0",
				type : 'get',
				dataType : "json",
				data : ({
					roleId : id
				}),
				async : true,
				error : function() {
					alertmsg("Connection error");
				},
				success : function(data) {

					$('#role-user-list').bootstrapTable('load', data);
				}
			});
		}
	});
}

// 点击角色初始化菜单树
var init_menu_tree = function(id) {
	var setting = {
		data : {
			simpleData : {
				enable : true,
				pIdKey : 'pid',
				idKey : "id"
			}
		},
		check : {
			enable : true
		}
	};
	var zNodes;
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		data : ({
			roleId : id
		}),
		dataType : "json",
		url : "initroletree",//请求的action路径  
		error : function() {//请求失败处理函数  
			alertmsg('请求失败');
		},
		success : function(data) { //请求成功后处理函数。    
			zNodes = data; //把后台封装好的简单Json格式赋给treeNodes  
		}
	});
	$.fn.zTree.init($("#menuTree"), setting, zNodes);
}