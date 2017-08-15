require([ "jquery", "bootstrap-table", "bootstrap", "bootstrapTableZhCn","jqcookie", "jqtreeview", "zTree", "zTreecheck", "lodash","zzbconf/public","bootstrapdatetimepicker" ,"bootstrapdatetimepickeri18n"],function($) {
	// 数据初始化
	$(function() {
		/*
		* 新增权限
		*/
		$("#addPer").on("click",function(){
			$("#permissionAddModel").modal('show');
		});
		
		$("#qa_execButton").on("click", function() {
			var permissionname = $("#qa_permissionname").val();
			var url = $("#qa_url").val();
			var percode = $("#qa_percode").val();
			var perdesc = $("#qa_perdesc").val();
			var noti = $("#qa_noti").val();
			
			if($("#qa_permissionname").val().trim() == "") {
				alertmsg("权限名称不能为空");
				return false;
			}else if($("#qa_url").val().trim() == "") {
				alertmsg("地址不能为空！");
				return false;
			}
			$.ajax({
				url: "/cm/miniPermission/addOrUpdateMiniPermission",
				type: 'GET',
//						dataType: "text",
				cache: false,
				async: true,
				data : ({
					permissionname : permissionname,
					url : url,
					percode: percode,
					noti:noti,
					perdesc:perdesc
				}),
				error : function() {
					alertmsg("Connection error");
				},
				success : function(data) {
					if(data == "success") {
						$("#permissionAddModel").modal('hide');
						$('#permission_list').bootstrapTable(
								'refresh',{url:"/cm/miniRolePer/queryPermissionListTr"});
						alertmsg("操作成功！");
					}else if(data == "fail"){
						$("#permissionAddModel").modal('hide');
						alertmsg("操作失败！！");
					}
				}
			});
		});
		/*
		 * 初始化角色用户关系列表
		 */
		$('#role-user-list').bootstrapTable({
			method : 'get',
//			url : "/cm/agentuser/initagentlistpage",
			singleSelect : false,
			cache : false,
			striped : true,
			pagination : true,
			sidePagination : 'server',
			pageSize : 10, 
			minimumCountColumns : 2,
			clickToSelect : true,
			columns: [{
				field: 'state',
				align: 'center',
				valign: 'middle',
				checkbox: true
			}, {
				field: 'agentcode',
				title: '代理人编码',
				visible: false,
				switchable:false,
				sortable: true
			}, {
				field: 'openid',
				title: 'openID',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'id',
				title: 'ID',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'rid',
				title: 'rID',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'nickname',
				title: '昵称',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'approvesstatestr',
				title: '认证状态',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'comname',
				title: '所属机构',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'pushChannel',
				title: 'pushChannel',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'idno',
				title: '身份证号',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'phone',
				title: '手机号',
				align: 'center',
				valign: 'middle',
				visible: true,
				switchable:true
			}, {
				field: 'agentkindstr',
				title: '用户类型',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'channelname',
				title: '渠道名称',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'rname',
				title: '用户属性',
				align: 'center',
				valign: 'middle',
				visible: true,
				sortable: true
			}, {
				field: 'rolecode',
				title: '角色编号',
				visible: false,
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'operating',
				title: '操作',
//				align: 'center',
//				valign: 'middle',
				switchable: false,
				formatter: operateFormatter9,
				events: operateEvents9
			}]
		});
		$('#user_list_Tab').bootstrapTable({
			method : 'get',
//			url : "/cm/agentuser/initagentlistpage",
			singleSelect : false,
			cache : false,
			striped : true,
			pagination : true,
			sidePagination : 'server',
			pageSize : 10, 
			minimumCountColumns : 2,
			clickToSelect : true,
			columns: [{
				field: 'state',
				align: 'center',
				valign: 'middle',
				checkbox: true
			}, {
				field: 'agentcode',
				title: '代理人编码',
				visible: false,
				switchable:false,
				sortable: true
			}, {
				field: 'openid',
				title: 'openID',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'id',
				title: 'ID',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'rid',
				title: 'rID',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'nickname',
				title: '昵称',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'approvesstatestr',
				title: '认证状态',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'comname',
				title: '所属机构',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'pushChannel',
				title: 'pushChannel',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'idno',
				title: '身份证号',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'phone',
				title: '手机号',
				align: 'center',
				valign: 'middle',
				visible: true,
				switchable:true
			}, {
				field: 'agentkindstr',
				title: '用户类型',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'rname',
				title: '用户属性',
				align: 'center',
				valign: 'middle',
				visible: true,
				sortable: true
			}]
		});
		/**
		 * 初始化权限列表
		 */		
		$('#permission_list').bootstrapTable({
			method : 'get',
			url : "/cm/miniRolePer/queryPermissionListTr",
			singleSelect : true,
			cache : false,
			striped : true,
			smartDisplay : true,
			pagination : true,
			pageNumber : true,
			sidePagination : 'server',
			pageSize : 10,
			minimumCountColumns : 2,
			clickToSelect : true,
			columns : [ { 
				field: 'state',
				align: 'center',
				valign: 'middle',
				checkbox: true
			}, {
				field : 'permissionname',
				title : '权限名称',
				align : 'center',
				valign : 'middle'
			}, {
				field : 'id',
				title : 'id',
				align : 'center',
				visible: false,
				valign : 'middle'
			}, {
				field : 'noti',
				title : 'noti',
				align : 'center',
				visible: false,
				valign : 'middle'
			}, {
				field : 'perindex',
				title : 'perindex',
				align : 'center',
				visible: false,
				valign : 'middle'
			}, {
				field : 'percode',
				title : '权限编号',
				align : 'center',
				align : 'center',
				valign : 'middle'
			}, {
				field : 'url',
				title : 'url',
				align : 'center',
				valign : 'middle'	
			}, {
				field : 'perdesc',
				title : '权限描述',
				align : 'center',
				valign : 'middle'	
			}, {
				field: 'operating',
				title: '操作',
				align: 'center',
				valign: 'middle',
				switchable: false,
				formatter: operateFormatter3,
				events: operateEvents3
			}]
		});
		/**
		 * 添加权限到角色列表
		 */		
		$('#add_per2role_tab').bootstrapTable({
			method : 'get',
			url : "/cm/miniRolePer/queryPermissionListTr",
			singleSelect : false,
			cache : false,
			striped : true,
			smartDisplay : true,
			pagination : true,
			pageNumber : true,
			sidePagination : 'server',
			pageSize : 10,
			minimumCountColumns : 2,
			clickToSelect : true,
			columns : [ { 
				field: 'state',
				align: 'center',
				valign: 'middle',
				checkbox: true
			}, {
				field : 'permissionname',
				title : '权限名称',
				align : 'center',
				valign : 'middle'
			}, {
				field : 'id',
				title : 'id',
				align : 'center',
				visible: false,
				valign : 'middle'
			}, {
				field : 'percode',
				title : '权限编号',
				align : 'center',
				valign : 'middle'
			}, {
				field : 'url',
				title : 'url',
				align : 'center',
				valign : 'middle'	
			}]
		});
		/**
		 * 角色下的权限列表
		 */
		$('#permission_tab').bootstrapTable({
			method : 'get',
			url : "/cm/miniRolePer/queryPermissionList",
			singleSelect : true,
			cache : false,
			striped : true,
			smartDisplay : true,
			pagination : true,
			pageNumber : true,
			sidePagination : 'server',
			pageSize : 10,
			minimumCountColumns : 2,
			clickToSelect : true,
			columns : [ { 
				field: 'state',
				align: 'center',
				valign: 'middle',
				checkbox: true
			}, {
				field : 'permissionname',
				title : '权限名称',
				align : 'center',
				valign : 'middle'
			}, {
				field : 'percode',
				title : '权限编号',
				align : 'center',
				valign : 'middle'
			}, {
				field : 'id',
				title : 'id',
				align : 'center',
				visible: false,
				valign : 'middle'
			}, {
				field : 'url',
				title : 'url',
				align : 'center',
				valign : 'middle'	
			}, {
				field: 'operating',
				title: '操作',
				align: 'center',
				valign: 'middle',
				switchable: false,
				formatter: operateFormatter5,
				events: operateEvents5
			}]
		});
		/*
		 * 初始化角色列表
		 * 
		 */
		$('#agent_plist').bootstrapTable({
			method : 'get',
			url : "/cm/miniRolePer/initMiniUserList",
			singleSelect : false,
			pagination : true,
			cache : false,
			sidePagination : 'server',
			striped : true,
			pageSize : 10,
			minimumCountColumns : 2,
			clickToSelect : true,
			columns: [ {
				field: 'state',
				align: 'center',
				valign: 'middle',
				checkbox: true
			}, {
				field: 'agentcode',
				title: '代理人编码',
				visible: false,
				switchable:false,
				sortable: true
			}, {
				field: 'openid',
				title: 'openID',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'id',
				title: 'ID',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'nickname',
				title: '昵称',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'approvesstatestr',
				title: '认证状态',
				visible: false,
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'comname',
				title: '所属机构',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'idno',
				title: '身份证号',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}, {
				field: 'phone',
				title: '手机号',
				align: 'center',
				valign: 'middle',
				visible: true,
				switchable:true
			}, {
				field: 'agentkindstr',
				title: '用户类型',
				align: 'center',
				valign: 'middle',
				visible: false,
				sortable: true
			}]
		});
		/**
		 * 初始化权限列表
		 */
		$('#role-data-list').bootstrapTable({
			method : 'get',
			url : "/cm/miniRolePer/queryRole",
			singleSelect : true,
			cache : false,
			striped : true,
			smartDisplay : true,
			pagination : true,
			pageNumber : true,
			sidePagination : 'server',
			pageSize : 10,
			minimumCountColumns : 2,
			clickToSelect : true,
			columns : [ { 
				field: 'state',
				align: 'center',
				valign: 'middle',
				checkbox: true
			}, {
				field : 'name',
				title : '角色名称',
				align : 'center',
				valign : 'middle'
			}, {
				field : 'id',
				title : 'id',
				align : 'center',
				visible: false,
				valign : 'middle'
			}, {
				field : 'rolecode',
				title : '角色编号',
				align : 'center',
				valign : 'middle'	
			}, {
				field : 'noti',
				title : '备注',
				visible: false,
				align : 'center',
				valign : 'middle'	
			}, {
				
				field : 'description',
				title : '角色描述',
				visible: false,
				align : 'center',
				valign : 'middle'	
			}, {
				
				field: 'operating',
				title: '操作',
				align: 'center',
				valign: 'middle',
				switchable: false,
				formatter: operateFormatter,
				events: operateEvents	
			} ]
		});
		/*
		 * 新增角色信息
		 */
//		$("#main2add").on("click",function(){
//			$("#addRoleModel").modal("show");
//		});
		/**
		 * 点击添加事件
		 */
		$("#add_per2role").on("click", function(){
			$("#qx_querybutton").click();
			var data = $('#role-data-list').bootstrapTable('getSelections');
			if(data.length != 1) {
				alertmsg("请选择一条数据！！！！");
				return false;
			}
//			$("#add_per2roleModel").modal("show");
		});
		/**
		 * 点击保存事件
		 */
		$("#qx_execButton").on("click",function(){
			var data1 = $('#role-data-list').bootstrapTable('getSelections');
			var data2 = $('#add_per2role_tab').bootstrapTable('getSelections');
			if(data2.length == 0) {
				alertmsg("请至少选择一条数据！！");
				return false;
			}
			$.ajax({
				url: "/cm/miniRolePer/addRole2Permission",
				type: 'get',
				cache: false,
				async: true,
				data: {
					"roleJosn":  JSON.stringify($('#role-data-list').bootstrapTable('getSelections')),
					"permissionJosn": JSON.stringify($('#add_per2role_tab').bootstrapTable('getSelections'))
				},
				error: function () {
					alertmsg("Connection error");
				},
				success: function (data) {
					if (data == "success") {
						$('#add_per2roleModel').modal('hide');
						alertmsg("操作成功");
					} else if (data == "fail") {
						alertmsg("操作失败");
					}
				}
			});
		});
		$("#qx_querybutton").on("click",function(){
			$('#add_per2role_tab').bootstrapTable(
					'refresh',
					{url: "/cm/miniRolePer/queryPermissionListTr?"+$("#add_per2roleForm").serialize()});
			
		});
		//刷新按钮
		$("#refresh").on("click", function(e) {
			$('#role-user-list').bootstrapTable('refresh',{url:'/cm/agentuser/initagentlistpage?'});
		});
		$("#refresh2").on("click", function(e) {
			$('#permission_list').bootstrapTable('refresh',{url:'/cm/miniRolePer/queryPermissionListTr?'});
		});
		/**
		 * 新增用户权限按钮
		 */
		$("#x_execButton").on("click", function() {
			$.ajax({
				url: "/cm/miniRolePer/addUserRole",
				type: 'GET',
				dataType: "text",
				cache: false,
				async: true,
				data: {
					rolecode:$("#x_rolecode").val(),
					
					noti: $("#x_noti").val(),
					name: $("#x_name").val(),
					description: $("#x_description").val()
					
					// openid : $("#s_openid").val()
				},
				error: function () {
					alertmsg("Connection error");
				},
				success: function (data) {
					if (data == "success") {
						$('#addRoleModel').modal('hide');
						$('#role-data-list').bootstrapTable(
								'refresh',
								{url:'/cm/miniRolePer/queryRole'});
						alertmsg("操作成功");
					} else if (data == "fail") {
						alertmsg("操作失败");
					}
				}	
			});
		});
		/**
		 * 保存按钮
		 */
		$("#qxq_execButton").on("click", function() {
			$.ajax({
				url: "/cm/miniPermission/addOrUpdateMiniPermission",
				type: 'GET',
				dataType: "text",
				cache: false,
				async: true,
				data: {
					permissionname:$("#qxq_permissionname").val(),
					perindex: $("#qxq_perindex").val(),
					url: $("#qxq_url").val(),
					percode: $("#qxq_percode").val(),
					id: $("#qxq_id").val(),
					perdesc: $("#qxq_perdesc").val(),
					noti: $("#qxq_noti").val()
					// openid : $("#s_openid").val()
				},
				error: function () {
					alertmsg("Connection error");
				},
				success: function (data) {
					if (data == "success") {
						$('#permissionUpdateModel').modal('hide');
						$('#permission_list').bootstrapTable(
								'refresh',
								{url:'/cm/miniRolePer/queryPermissionListTr'});
						alertmsg("操作成功");
					} else if (data == "fail") {
						alertmsg("操作失败");
					}
				}	
			});
		});
		
		/*
		 * 修改角色信息
		 */
		$("#s_execButton").on("click", function() {
			var id = $("#s_id").val();
			$.ajax({
				url: "/cm/miniRolePer/updateRole",
				type: 'GET',
				dataType: "text",
				cache: false,
				async: true,
				data: {
					rolecode:$("#s_rolecode").val(),
					id : $("#s_id").val(),
					noti: $("#s_noti").val(),
					name: $("#s_name").val(),
					description: $("#s_description").val()
					
					// openid : $("#s_openid").val()
				},
				error: function () {
					alertmsg("Connection error");
				},
				success: function (data) {
					if (data == "success") {
						$('#updateRoleModel').modal('hide');
						$('#role-data-list').bootstrapTable(
								'refresh',
								{url:'/cm/miniRolePer/queryRole'});
						$('#role-user-list').bootstrapTable(
								'refresh',
								{url:'/cm/agentuser/initagentlistpage?rid='+id});
						
						alertmsg("操作成功");
					} else if (data == "fail") {
						alertmsg("操作失败");
					}
				}	
			});
		});
		
		/* 添加用户到角色 */
		// 查询
		$("#t_querybutton").on("click",function(){
			$('#agent_plist').bootstrapTable(
					'refresh',
					{url:'/cm/agentuser/initagentlistpage?'+$("#addReferrFrom").serialize()});
			
		});
			
		
		$("#main2adda").on("click",function(){
			$('#permission_list').bootstrapTable(
					'refresh',
					{url:'/cm/miniRolePer/queryPermissionListTr?'+$("#queryPermissionform").serialize()});
			
		});
		$("#menuTab").on('click', function(){
			$("#userlist").hide();
		});
		
		$("#mouseid").on('mouseleave', function(){
			$("#userlist").hide();
		});
			
		$("#t_execButton").on("click", function(){
			var data1 = $('#agent_plist').bootstrapTable('getSelections');
			if(data1.length < 1) {
				
				alertmsg("请至少选择一条信息！！！");
				return false;
			}
			$.ajax({
				url: "/cm/miniRolePer/userAddRole",
				type: 'get',
				dataType: 'text',
				data: {
					
					"miniAgentJson": JSON.stringify($('#agent_plist').bootstrapTable('getSelections')),
					"miniRoleJson": JSON.stringify($('#role-data-list').bootstrapTable('getSelections'))
				},
				cache: false,
				async: true,
				success: function (data) {
					if(data != "fail") {
						var url = "/cm/agentuser/initagentlistpage?rid="+data;
						$('#role-user-list').bootstrapTable('refresh', {url: url});
						alertmsg("操作成功！！！！");
					}else if (data == "fail") {
						alertmsg("操作失败！！");
					}else{
						alertmsg("重置用户角色！！")
					}
					$("#addUserByRoleModel").modal('hide');
				}
			});
			
		});
		/**
		 * 初始化用户
		 */	
		$("#initUserAtrrBt").on("click", function(){
			$.ajax({
				url: "/cm/miniPermission/initUserAtrrBt",
				type: 'get',
				dataType: 'text',
				data: {},
				cache: false,
				async: true,
				success: function (data) {
					if(data == "success") {
						var url = "/cm/agentuser/initagentlistpage?";
						$('#role-user-list').bootstrapTable('refresh', {url: url});
						alertmsg("操作成功！！！！");
					}else if (data == "fail") {
						alertmsg("操作失败！！");
					}else{
						alertmsg("重置用户角色！！")
					}
				}
			});
			
		});
		
		$("#add_users2role").on("click",function(){
			var data = $('#role-data-list').bootstrapTable('getSelections');
			if(data.length != 1) {
				alertmsg("请选择一条信息！");
				return false;
			}
			$("#addUserByRoleModel").modal('show');
		});
		$("#m_execButton").on("click", function(){
			$.ajax({
				url: "/cm/miniPermission/updateMiniUser",
				type: 'get',
				dataType: 'text',
				data: {
					openid: $("#m_openid").val(),
					pushChannel: $("#m_pushChannel").val()
				},
				cache: false,
				async: true,
				success: function (data) {
					if(data == "success") {
						var url = "/cm/agentuser/initagentlistpage?";
						$('#role-user-list').bootstrapTable('refresh', {url: url});
						$("#updateMiniChannelModel").modal('hide');
						alertmsg("操作成功！！！！");
					}else if (data == "fail") {
						alertmsg("操作失败！！");
					}else{
						alertmsg("重置用户角色！！")
					}
				}
			});
			
		});
		
	});	
	
});	 
// 单击左侧加载右侧角色数据展示采用ajax

function operateFormatter(value, row, index) {
	return [
		'<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
		'<i class="glyphicon glyphicon-edit"></i>',
		'</a>',
		'<a class="permission m-left-5" href="javascript:void(0)" title="权限列表">',
		'<i class="glyphicon glyphicon-list"></i>',
		'</a>',
		'<a class="conditions m-left-5" href="javascript:void(0)" title="用户列表">',
		'<i class="glyphicon glyphicon-list-alt"></i>',
		'</a>',
		'<a class="remove m-left-5" href="javascript:void(0)" title="删除">',
		'<i class="glyphicon glyphicon-remove"></i>',
		'</a>'
	].join('');
}
function operateFormatter9(value, row, index) {
	return row.rolecode == '01'?[
		'<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
		'<i class="glyphicon glyphicon-edit"></i>',
		'</a>',
		'<a class="conditions m-left-5" href="javascript:void(0)" title="下属列表">',
		'<i class="glyphicon glyphicon-list-alt"></i>',
		'</a>'
		
		].join(''):"";
}
function operateFormatter5(value, row, index) {
	return [
		
		'<a class="remove m-left-5" href="javascript:void(0)" title="解除权限">',
		'<i class="glyphicon glyphicon-remove-sign"></i>',
		'</a>'
		].join('');
}

window.operateEvents = {
	'click .remove' : function(e, value, row, index) {
		var id = row.id;
		$.ajax({
			url : "/cm/miniRolePer/deleteRole",
			type : 'get',
			dataType : "text",
			data : {
				id:id
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
				return null;
			},
			success : function(data) {
				alertmsg("success!");
				$('#role-data-list').bootstrapTable(
					'refresh',
					{url:'/cm/miniRolePer/queryRole'
				});
			}
		});
	},
	'click .edit' : function(e, value, row, index) {
		$("#s_id").val(row.id);
		$("#s_name").val(row.name);
		$("#s_rolecode").val(row.rolecode);
		$("#s_noti").val(row.noti);
		$("#s_description").val(row.description);
		$('#updateRoleModel').modal('show');
	},
	'click .permission' : function(e, value, row, index) {
		var roleid = row.id;
		$('#permission_tab').bootstrapTable(
				'refresh',
				{url:"/cm/miniRolePer/queryPermissionList?roleid="+roleid});
					$('#permissionModel').modal('show');
	},
	'click .conditions' : function(e, value, row, index) {
		var roleid = row.id;
		$('#role-user-list').bootstrapTable(
			'refresh',{url: '/cm/agentuser/initagentlistpage?rid='+roleid})
	}
}
window.operateEvents9 = {
	'click .remove' : function(e, value, row, index) {
		var id = row.rid;
		var uid = row.id;
		$.ajax({
			url : "/cm/miniPermission/relieveRoleToUser",
			type : 'get',
			dataType : "text",
			data : {
				id:id,
				uid:uid
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
				return null;
			},
			success : function(data) {
				if(data == "success") {
					$('#role-user-list').bootstrapTable(
							'refresh',
							{url:'/cm/agentuser/initagentlistpage?rid='+id
					});
					alertmsg("success!");
				} else {
					alertmsg("fail");
				}
			}
		});
	},
	'click .edit' : function(e, value, row, index) {
		$("#m_openid").val(row.openid);
		$("#m_pushChannel").val(row.pushChannel);
		if(row.rolecode != "01") {
			alertmsg("改用户无此修改！");
			return false;
		}
		$('#updateMiniChannelModel').modal('show');
	},
	'click .conditions' : function(e, value, row, index) {
		var referrerid = row.id;
		if(row.rolecode != "01") {
			alertmsg("改用户无此权限！！");
			return false;
		}
		$('#user_list_Tab').bootstrapTable(
			'refresh',{url: '/cm/agentuser/initagentlistpage?referrerid='+referrerid});
		$("#userlist").show();
	}
}
window.operateEvents5 = {
	'click .remove' : function(e, value, row, index) {
		var rdata = $('#role-data-list').bootstrapTable('getSelections');
		if(rdata.length != 1) {
			alertmsg("请选择一条信息");
			return false;
		}
		var id = row.id;
		$.ajax({
			url : "/cm/miniPermission/relievePermissionToRole",
			type : 'get',
			dataType : "text",
			data : {
				"roleJson":JSON.stringify($('#role-data-list').bootstrapTable('getSelections')),
				id: id
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
				return null;
			},
			success : function(data) {
				if(data != "fail") {
					$('#permission_tab').bootstrapTable(
							'refresh',
							{url:"/cm/miniRolePer/queryPermissionList?roleid="+data
					});
					alertmsg("success!");
					$('#permissionModel').modal('hide');
				}else {
					alertmsg("fail");
				}
			}
		});
	}
}

function operateFormatter3(value, row, index) {
	return [
		'<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
		'<i class="glyphicon glyphicon-edit"></i>',
		'</a>',
		'<a class="remove m-left-5" href="javascript:void(0)" title="删除">',
		'<i class="glyphicon glyphicon-remove"></i>',
		'</a>'
		
		].join('');
}

window.operateEvents3 = {
	'click .remove' : function(e, value, row, index) {
		var id = row.id;
		$.ajax({
			url : "/cm/miniPermission/deletePermision",
			type : 'get',
			dataType : "text",
			data : {
				id:id
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
				return null;
			},
			success : function(data) {
				if(data == "success") {
					
					$('#permission_list').bootstrapTable(
							'refresh',
							{url:'/cm/miniRolePer/queryPermissionListTr'
							});
					alertmsg("操作成功!");
				} else {
					alertmsg("操作失败！");
				}
			}
		});
	},
	'click .edit' : function(e, value, row, index) {
		$("#qxq_permissionname").val(row.permissionname);
		$("#qxq_url").val(row.url);
		$("#qxq_id").val(row.id);
		$("#qxq_noti").val(row.noti);
		$("#qxq_perindex").val(row.perindex);
		$("#qxq_percode").val(row.percode);
		$("#qxq_perdesc").val(row.perdesc);
		$('#permissionUpdateModel').modal('show');
	}
}

// 点击角色初始化菜单树
