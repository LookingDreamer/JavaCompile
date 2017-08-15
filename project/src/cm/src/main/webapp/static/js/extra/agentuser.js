require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn",  "bootstrapdatetimepicker","bootstrapdatetimepickeri18n","jqcookie", "jqtreeview","zTree","zTreecheck","zzbconf/public"], function ($) {
	//数据初始化 
	$(function() {
		$('.form_datetime').datetimepicker({
		    language: 'zh-CN',
		    format: "yyyy-mm-dd",
		    weekStart: 1,
		    todayBtn: 1,
		    autoclose: 1,
		    todayHighlight: 1,
		    startView: 2,
		    forceParse: 0,
		    minView: 2
		    // showMeridian: 1
		});

		$("#detail").on("click",function(){
			var agent_id = $('#agent_list').bootstrapTable('getSelections');
			if(agent_id.length!=1){
				alertmsg("请选择一条数据查看详情");
				return false;
			}
			var id= agent_id[0].id;
			 $('#myModal_agent_detail').removeData('bs.modal');
			 $("#myModal_agent_detail").modal({
 				show: true,
 				remote: 'main2detail?id='+id
 			});
		});

		$("#update").on("click",function(){
			var agent_id = $('#agent_list').bootstrapTable('getSelections');
			if(agent_id.length!=1){
				alertmsg("请选择一条数据进行显示");
				return false;
			}
			var id= agent_id[0].id;
			window.location.href="mian2edit?agentId="+id;
		});

		//查询
		$("#querybutton_test").on("click",function(){
			var name = $('#name').val();
            var idno = $('#idno').val();
			var agentcode = $('#agentcode').val();
			var deptname = $('#deptname').val();
			var deptid = $('#deptid').val();
			var agentkind = $('#agentkind').find("option:selected").val();
			var phone = $('#phone').val();
			var registertimestr = $('#registertimestr').val();
			var registertimeendstr = $('#registertimeendstr').val();
			var testtimestr = $('#testtimestr').val();
			var testtimeendstr = $('#testtimeendstr').val();
			var agentstatus = $('#agentstatus').find("option:selected").val();
			var approvesstate = $('#approvesstate').find("option:selected").val();
			var nickname = $('#nickname').val();
			var jobnum = $('#jobnum').val();
			var rname = $('#rname').find("option:selected").val();
			var str = name + rname + idno + agentcode + deptid + deptname + agentkind + phone + registertimestr
				+ registertimeendstr + testtimestr + agentstatus + approvesstate + nickname + rname + jobnum;
			if(str == '000') {
				alertmsg("请至少选择一个条件！！");
				return false;
			}
			$('#agent_list').bootstrapTable(
					'refresh',
					{url:'initagentlistpage?'+$("#agentform").serialize()});

		});
		//查询
		$("#x_querybutton").on("click",function(){
			
			$('#agent_plist').bootstrapTable(
					'refresh',
					{url:'initagentlistpage?'+$("#addReferrFrom").serialize()});
			
		});
		$("#z_querybutton").on("click",function(){
			var agent_id = $('#agent_list').bootstrapTable('getSelections');
			var rolecode = agent_id[0].rolecode;
			var rname = agent_id[0].rname;
			if(agent_id.length!=1){
				alertmsg("请选择一条数据进行显示");
				return false;
			}
			if($.trim(rolecode) == '01') {
				alertmsg("该用户无法添加！");
				return false;
			}
//			$('#agent_plist').bootstrapTable('refresh',{url:'initagentlistpage?rolecode=01'});
			$("#addReferreridModel").modal('show');
			
			
		});
		//重置
		$('#resetbutton').on("click",function(){
			$('#agentform')[0].reset();
		});
		//弹出机构树
		$("#deptname").on("click",function(){
			$.fn.zTree.init($("#dept_tree"), deptsetting);
			$("#myModal_dept").removeData("bs.modal");
			$("#myModal_dept").modal({
				 show: true
			});

		});
		$('#agent_plist').bootstrapTable({
            method: 'get',
//            url: "initagentlistpage?rolecode=01",
            cache: false,
            striped: true,
            pagination: true,
            singleSelect:true,
            sidePagination: 'server',
            pageSize: 5,
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            },{
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
            }, {
                field: 'registertimestr',
                title: '注册时间',
                align: 'center',
                valign: 'middle',
				visible: true,
                sortable: true
            }, {
                field: 'lsatlogintimestr',
                title: '最后登录时间',
                align: 'center',
                valign: 'middle',
				visible: true,
                sortable: true
            },{
            	field: 'rname',
                title: '用户属性',
                align: 'center',
                valign: 'middle',
//                formatter:activityTypeFormatter,
 				visible: true,
                sortable: true
            
			}]
        });
		$('#agent_list').bootstrapTable({
            method: 'get',
//            url: "initagentlistpage",
            cache: false,
            striped: true,
            pagination: true,
            singleSelect:true,
            sidePagination: 'server',
            pageSize: 10,
            minimumCountColumns: 2,
            clickToSelect: true,
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
                field: 'registertimestr',
                title: '注册时间',
                align: 'center',
                valign: 'middle',
				visible: true,
                sortable: true
            }, {
                field: 'lsatlogintimestr',
                title: '最后登录时间',
                align: 'center',
                valign: 'middle',
				visible: true,
                sortable: true
            },{
            	field: 'rname',
                title: '用户属性',
                align: 'center',
                valign: 'middle',
 				visible: true,
                sortable: true
            },{
            	field: 'rolecode',
            	title: 'rolecode',
            	align: 'center',
            	visible: false,
            	valign: 'middle',
            	sortable: true
            }]
        });
		$("#s_execButton").click(function () {
			if($("#s_rname").val().trim() == ""){
				alertmsg("角色不能为空");
				return false;
			}else{
				saveRole();
			
			}
		});
		//保存
		$("#x_execButton").click(function () {
			var data = $('#agent_plist').bootstrapTable('getSelections');
		    if(data.length != 1){
		    	alertmsg("请选择一条记录！");
		    }else{
		    	$.ajax({
		            url: "/cm/miniRolePer/updateRefer",
		            type: 'get',
		            dataType: 'text',
		            data: {
		                
		                "miniAgentJson": JSON.stringify($('#agent_plist').bootstrapTable('getSelections')),
		                "miniAgentedJson": JSON.stringify($('#agent_list').bootstrapTable('getSelections'))
		            },
		            cache: false,
		            async: true,
		            success: function (data) {
//		                var url = "/cm/agentuser/initagentlistpage?";
//		                $('#agent_list').bootstrapTable('refresh', {url: url});
		                alertmsg(data);
		                $("#addReferreridModel").modal('hide');
		            }
		        });
				
			}
		});
		// 用户列表页面【查询】按钮
		$("#querybutton").on("click", function(e) {
			var postdata = "";
			if($("#usercode").val()){
				postdata += "&usercode=" + $("#usercode").val();
			}
			if($("#username").val()){
				postdata += "&username=" + $("#username").val();
			}
			if($("#comname").val()){
				postdata += "&comname=" + $("#comname").val();
			}
//			reloaddata(postdata);
		 });

		//获得代理人列表agent_list选中行的id列表
		function getAgentSelectedRows() {
		    var data = $('#agent_list').bootstrapTable('getSelections');
		    if(data.length == 0){
		    	alertmsg("没有行被选中！");
		    }else{
		    	var arrayuserid = new Array();
		    	for(var i=0;i<data.length;i++){
		    		arrayuserid.push(data[i].id)
		    	}
		    	return arrayuserid;
		    }
		}

		// 查询条件【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#usercode").val("");
			$("#username").val("");
			$("#comname").val("");
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
	
function saveRole() {
	$.ajax({
		url: "/cm/miniRolePer/updateRole",
		type: 'GET',
		dataType: "text",
		cache: false,
		async: true,
		data: {
			rname:$("#s_rname").val(),
			id : $("#s_id").val()
		//	openid : $("#s_openid").val()
		},
		error: function () {
			alertmsg("Connection error");
		},
		success: function (data) {
			if (data == "success") {
				$('#updateRoleModel').modal('hide');
				$("#querybutton_test").click();
				alertmsg("操作成功");
			} else if (data == "fail") {
				alertmsg("操作失败");
			}
			else
				alertmsg("你已经删除原来的数据，需要重新初始化");
			
		}
	});
}
//获得选中行的id列表
function getSelectedRows() {
    var data = $('#table-javascript').bootstrapTable('getSelections');
    if(data.length == 0){
    	alertmsg("没有行被选中！");
    }else{
    	var arrayuserid = new Array();
    	for(var i=0;i<data.length;i++){
    		arrayuserid.push(data[i].id)
    	}
    	return arrayuserid;
    }
}

//根据userid更新用户信息跳转到usersave页面
function updateuser(id){
	location.href = "save?id=" + id;
}

//添加事件
function operateFormatter(value, row, index) {
	return [
		'<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
		'<i class="glyphicon glyphicon-edit"></i>',
		'</a>',
		'<a class="remove m-left-5" href="javascript:void(0)" title="删除">',
		'<i class="glyphicon glyphicon-remove"></i>',
		'</a>'
	].join('');
}


function operateFormatter1(value, row, index) {
	
	return ['<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
		'<i class="glyphicon glyphicon-edit"></i>',
		'</a>'].join('');

}
window.operateEvents1 = {
	'click .edit' : function(e, value, row, index) {
	
		//$(".readonlysetting").removeAttr("readonly");
		//$(".disabledsetting").removeAttr("disabled");
		//$("#s_datetype").empty();
		//$("#s_datetype").append("<option value='0'>编辑</option>");
		$("#s_rname").val(row.rname);
		$("#s_id").val(row.id);
		
		$('#updateRoleModel').modal('show');
	}
};

//事件相应
window.operateEvents = {
	'click .edit': function (e, value, row, index) {
		updateuser(row.id);
	},
	'click .remove': function (e, value, row, index) {
		deleteuser(row.id);
	}
};

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
		url : 'initagentlistpage',
		type : 'GET',
		dataType : "json",
		data:({limit:10,offset:0}),
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#agent_list').bootstrapTable('load', data);
		}
	});
}
