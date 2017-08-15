require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn",  "bootstrapdatetimepicker","bootstrapdatetimepickeri18n","jqcookie", "jqtreeview","zTree","zTreecheck","public"], function ($) {
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
		    minView: 2,
		    // showMeridian: 1
		});
		
		$("#add").on("click",function(){
			window.location.href="mian2edit";
		})
		$("#querybuttonlist").on("click",function(){
			var agent_id = $('#agent_list').bootstrapTable('getSelections');
			if(agent_id.length!=1){
				alertmsg("请选择一条数据查看详情");
				return false;
			} 
			var id= agent_id[0].id;
			window.location.href="querybuttonlist?agentId="+id;
		})
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
		})
		
		$("#update").on("click",function(){
			var agent_id = $('#agent_list').bootstrapTable('getSelections');
			if(agent_id.length!=1){
				alertmsg("请选择一条数据进行修改");
				return false;
			}
			var id= agent_id[0].id;
			window.location.href="mian2edit?agentId="+id;
		})
		
		//处理跟踪
		$("#t_handle").on("click",function(){
			var agent_id = $('#agent_list').bootstrapTable('getSelections');
			if(agent_id.length!=1){
				alertmsg("请选择一条数据进行修改");
				return false;
			}
			var id= agent_id[0].id;
//			window.location.href="showcartaskmanagelist?agentId="+id;
			window.location.href="showcartasklist?agentId="+id;
		})
		
		//可以批量删除
		$("#delete").on("click",function(){
				var agent_id = $('#agent_list').bootstrapTable('getSelections');
				if(agent_id.length==0){
					alertmsg("请选择至少一条或多条");
					return false;
				}
				var agent_id_array = new Array();
				for(var i=0;i<agent_id.length;i++){
					agent_id_array.push(agent_id[i].id);
				}
				if(confirm("您选择了"+agent_id.length+"条数据，是否确认删除")){
				$.ajax({
					url:'batchdeleteagentbyid',
					type:'post',
					data:({ids:agent_id_array.toString()}),
					success:function(data){
						if(data.status=="1"){
							alertmsg(data.message);
							reloaddata();
						}else{
							alertmsg(data.message);
							return ;
						}	
					}
				})
				reload();
			}
			
		})
		
		//查询
		$("#querybutton").on("click",function(){
			
			var name = $("#name").val();
			var idno = $("#idno").val();
			var agentcode = $("#agentcode").val();
			var deptid = $("#deptid").val();
			var agentstatus =$("#agentstatus").find("option:selected").val();
			var phone = $("#phone").val();
			var agentkind = $("#agentkind").val();
			var registertimestr = $("#registertimestr").val();
			var registertimeendstr = $("#registertimeendstr").val();
			var testtimestr = $("#testtimestr").val();
			var testtimeendstr = $("#testtimeendstr").val();
			var approvesstate = $("#approvesstate").val();
			
			var str = name + idno + agentcode + deptid + agentstatus + phone  + agentkind + registertimestr + registertimeendstr + testtimestr + testtimeendstr + approvesstate;
			if(str == '000'){
				alertmsg("请至少选择一个条件进行查询");
				return;
			}
			
			$('#agent_list').bootstrapTable(
					'refresh',
					{url:'initagentlistpage?name='+name
						+'&idno='+idno
						+'&agentcode='+agentcode
						+'&deptid='+deptid
						+'&agentstatus='+agentstatus
						+'&phone='+phone
						+'&agentkind='+agentkind
						+'&registertimestr='+registertimestr
						+'&registertimeendstr='+registertimeendstr
						+'&testtimestr='+testtimestr
						+'&testtimeendstr='+testtimeendstr
						+'&approvesstate='+approvesstate});
			
		})
		
		//重置
		$('#resetbutton').on("click",function(){
			  $('#name').val('');
              $('#idno').val('');
              $('#agentcode').val('');
              $('#deptname').val('');
              $('#deptid').val('');
              $('#agentkind').val('0');
              $('#phone').val('');
              $('#registertimestr').val('');
              $('#registertimeendstr').val('');
              $('#testtimestr').val('');
              $('#testtimeendstr').val('');
              $('#agentstatus').val('0');
              $('#approvesstate').val('0');
		})
		//弹出机构树
		$("#deptname").on("click",function(){
			$.fn.zTree.init($("#dept_tree"), deptsetting);
			$("#myModal_dept").removeData("bs.modal");
			$("#myModal_dept").modal({
				 show: true
			});
			
		})
		//弹出批量使用功能包机构树
//		$("#batchUseFuncs").on("click",function(){
//			$('#showdeptpic').modal();
//			$.fn.zTree.init($("#treeDemoDept"), batchDeptsetting);
//			initFuncs();
//		});
		//弹出批量使用功能包
		$("#funcsOK").on("click",function(){
				funcsOK();
		});
		function funcsOK(){
			var setcode = $("#funcs").val().trim();
//			var deptid1 =document.getElementById("funcsDeptid").value;
			var deptid1=$("#funcsDeptid").val().trim();
			var deptid = deptid1.replace(/\n/g,",");
			if (!setcode){
				alertmsg("请选择需要关联的权限包！");
				return;
			}
			if (!deptid){
				alertmsg("请输入需要被关联的工号或手机号！");
				return;
			}
			if(confirm("确定要关联权限包吗？")){
				$.insLoading();
				$.ajax({
					url:'/cm/agent/addfuncsAll',
					type:'POST',
					data:{"setcode":setcode,"deptid":deptid},
					datatype:"json",
					success:function(data){
						$.insLoaded();
						$("#funcsDeptid").val('');
						if (data.status==1){
							$('#showdeptpic').modal("hide");
							$('#showdeptpic1').modal();
						} else if(data.status==2){
							$('#showdeptpic').modal("hide");
							$('#showdeptpic2').modal();
							downloadExcel(data.result);
						}else{
							alert(data.message);
						}
					},
					error : function() {
						$.insLoaded();
						alertmsg("Connection error");					
					}
				});
			}
		}

		function downloadExcel(result){
			var ulr ="/cm/agent/download";
			window.open(ulr);
		}

		function initFuncs(){
			$.ajax({
				url:'getSetList',
				type:'get',
				datatype:"json",
				success:function(data){
					$("#funcs").children().remove();
					$("#funcs").append("<option value=''>请选择</option>");
					if(data){
						$.each(data, function(key, ag) {
							$("#funcs").append("<option value='"+ag.id+"'>"+ag.setname+"</option>");
						});
					}else{
						alertmsg("获取功能包错误");
					}
				}
			});
		}

		$('#agent_list').bootstrapTable({
            method: 'get',
          //  url: "initagentlistpage",
            cache: false,
            striped: true,
            pagination: true,
            singleSelect:false,
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
           	 field: 'rownum',
             title: '序号',
             align: 'center',
             valign: 'middle',
             sortable: true
            },{
                field: 'agentcode',
                title: '代理人编码',
                visible: true,
                switchable:false,
                sortable: true
            }, {
                field: 'openid',
                title: 'openID',
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
                title: '所属机构',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'idno',
                title: '身份证号',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'phone',
                title: '手机号',
                align: 'center',
                valign: 'middle',
                visible: false,
                switchable:false
            }, {
                field: 'agentstatusstr',
                title: '状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'agentkindstr',
                title: '用户类型',
                align: 'center',
                valign: 'middle',
                sortable: true
            }]
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
		
		// 用户列表页面【批量删除】按钮
		$("#benchdeletebutton").on("click", function(e) {
			if(confirm("删除不可恢复，是否删除？")){
				var arrayuserid = getSelectedRows();
				$.ajax({
					url : 'benchdeletebyids?arrayid=' + arrayuserid,
					type : 'GET',
					dataType : "json",
					async : true,
					error : function() {
						alertmsg("Connection error");
					},
					success : function(data) {
						var result = data.count;
						if (result>0) {
							alertmsg("删除成功！");
//						reloaddata("");//重新载入
						}else{
							alertmsg("删除失败！");
						}
					}
				});
			}
		});
		//绑定工号
		$("#bound_jobno").on("click", function(e) {
			 var data = $('#agent_list').bootstrapTable('getSelections');
			    if(data.length == 0){
			    	alertmsg("没有行被选中！");
			    	return false;
			    }else if(data[0].agentkindstr == '正式'){
			    	alertmsg("正式工号无法进行绑定！");
			    	return false;
			    }
			var jobno = prompt("请输入要绑定的工号：");
			if(jobno){
				$.ajax({
					url : 'bandjobno?jobno=' + jobno + '&idno=' + data[0].idno + '&phone=' + data[0].phone,
					type : 'GET',
					dataType : "json",
					async : true,
					error : function() {
						alertmsg("Connection error");
					},
					success : function(data) { 
						if (data.statu=="1") {
							alertmsg("绑定成功！");
							$("#querybutton").click();
						}else if (data.statu=="2"){
							alertmsg("绑定失败，请完善代理身份证号码！");
						}else if (data.statu=="3"){
							alertmsg("绑定失败，试用代理人不存在！");
						}else{
							alertmsg("绑定失败，请核实该代理人工号是否存在并且已经申请认证与核心同步！");
						}
					}
				});
			}
		});
		// 用户列表页面【批量停用】按钮
		$("#resetuserstatus").on("click", function(e) {
			var arrayuserid = getSelectedRows();
			$.ajax({
				url : 'resetusersataus?userIds=' + arrayuserid,
				type : 'GET',
				dataType : "json",
				async : true,
				error : function() {
					alertmsg("Connection error");
				},
				success : function(data) {
					var result = data.code;
					if (result=='0') {
						alertmsg(data.message);
//						reloaddata("");//重新载入
					}else{
						alertmsg(data.message);
					}
				}
			});
		});
		
		// 密码重置按钮
		$("#resetpwd").on("click", function(e) {
			var arrayuserid = getAgentSelectedRows();
			$.ajax({
				url : 'resetpwd?Ids=' + arrayuserid,
				type : 'GET',
				dataType : "json",
				async : true,
				error : function() {
					alertmsg("Connection error");
				},
				success : function(data) {
					var result = data.code;
					if (result=='0') {
						alertmsg(data.message);
//						reloaddata("");//重新载入
					}else{
						alertmsg(data.message);
					}
				}
			});
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
		
		//转到新增页面
		$("#turn2edite").click(function(){
			location.href = "save";
		})
		
		// 添加用户界面【添加】按钮
		$("#addbutton").on("click", function(e) {
			//if($("#usersaveform").valid()){
			if($("#usercode").val()==""){
				alertmsg("用户编码不能为空");
				$("#usercode").focus();
				return;
			}
			if($("#username").val()==""){
				alertmsg("用户名不能为空");
				$("#username").focus();
				return;
			}
			if($("#userorganization").val()==""){
				alertmsg("所属机构不能为空");
				$("#userorganization").focus();
				return;
			}
			$('#usersaveform').submit();
		});
		
		//修改密码【确定】按钮
		$("#updatepasswordbutton").on("click", function(e) {
			if(!$("#oldpwd").val()){
				alertmsg("请输入旧密码");
				return;
			}else if(!$("#newpwd").val()){
				alertmsg("请输入新密码");
				return;
			}else if(!$("#checknewpwd").val()){
				alertmsg("请输入确认密码");
				return;
			}else if($("#newpwd").val()!=$("#checknewpwd").val()){
				alertmsg("输入的新密码不一致");
				return;
			}
			$('#userupdatepasswordform').ajaxSubmit({
				url : 'changepassword',
				type : 'POST',
				dataType : "json",
				error : function() {
					alertmsg("Connection error");
				},
				success : function(data) {
					var result = data.msg;
					if (result=='true') {
						$("p").html("update ok");
					}else{
						$("p").html("error");
					}
				}
			});
		});
		
		//批量关联权限包
		$("#batchUseFuncs").on("click",function(){
//			window.parent.openDialogForCM('/cm/pset/showbulkpackage');
			$.ajax({
				url : "/cm/pset/showbulkpackage",
				type : 'GET',
				error:function(){
					alert("批量操作页面初始化失败！");
				},
				success:function(data){
					$("#funcs").children().remove();
					$("#funcs").append("<option value=''>请选择</option>");
					if(data){
						$.each(data, function(key, ag) {
							$("#funcs").append("<option value='"+ag.id+"'>"+ag.setname+"</option>");
						});
					}
					$("#showdeptpic").modal();
				}
			});
		});

		
	});

});
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
//根据userid删除用户
function deleteuser(id){
	if(confirm("删除不可恢复，确定删除吗？")){
		$.ajax({
			url : 'deletebyid?id=' + id,
			type : 'GET',
			dataType : "json",
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				var result = data.count;
				if (result>0) {
					alertmsg("删除成功！");
//				reloaddata("");//重新载入
				}else{
					alertmsg("删除失败！");
				}
			}
		});
	}
}
//根据userid更新用户信息跳转到usersave页面
function updateuser(id){
	location.href = "save?id=" + id;
}
//刷新列表
/*function reloaddata(data){
	$.ajax({
		url : '',
		type : 'GET',
		dataType : "json",
		data: data+"&limit="+pagesize,
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#table-javascript').bootstrapTable('load', data);
		}
	});
}*/
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
//事件相应
window.operateEvents = {
    'click .edit': function (e, value, row, index) {
    	updateuser(row.id);
    },
    'click .remove': function (e, value, row, index) {
    	deleteuser(row.id);
    }
};
//验证usercode是否存在
function checkusercode(val){
	$.ajax({
		url : 'usercodecheck',
		type : 'POST',
		dataType : "json",
		data: {                     //要传递的数据   
			usercode: function() {   
				return $("#usercode").val();   
			},
			id: function() {   
				return $("#id").val();   
			}
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			if (!data) {
				alertmsg("用户编码已经存在");
				$("#usercode").focus();
			}
		}
	});
}
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
var batchDeptsetting = {
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
		onCheck: batchDeptTreeOnCheck
	}
};

/*function batchDeptTreeOnCheck(event, treeId, treeNode) {
	$("#funcsDeptid").val(treeNode.id);
}*/
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
