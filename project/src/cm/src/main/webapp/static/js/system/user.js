require(["jquery", 
         "bootstrap-table", 
         "bootstrap",
         "bootstrapTableZhCn",
         "bootstrapdatetimepicker",
         "bootstrapdatetimepickeri18n",
         "jqvalidatei18n",
         "additionalmethods",
         "public",
         "jqtreeview",
         "zTree",
         "zTreecheck",
         "multiselect"], function ($) {
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
		    showMeridian: 0
		});
		
		$('.js-multiselect').multiselect({
			right: '#js_multiselect_to_1',
			rightAll: '#js_right_All_1',
			beforeMoveToRight:function($left, $right, option){
				if($(option).length==1 && $(option).attr('rolename') == "超级管理员"){
					if($("#operator").val() == "admin"){
						return true;
					}else{
						alertmsg("只有ADMIN用户可以操作!");
						return false;
					}
				}else{
					return true;
				}
			},
			afterMoveToRight:function($left, $right, option){
				if($("#operator").val() != "admin"){
					if ($(option).length > 1) {
						for(i=0;i<option.length;i++){
							if($(option[i]).attr('rolename') == "超级管理员"){
								$("#js_multiselect_to_1 option[value='1']").remove();
								$(".js-multiselect").append($(option[i]));
							}
						}
					}
				}
			},
			rightSelected: '#js_right_Selected_1',
			leftSelected: '#js_left_Selected_1',
			leftAll: '#js_left_All_1'
		});

		$("#go_back").on("click",function(){
			window.location.href="menu2list";
		})
		
		//弹出机构树
		$("#deptname").on("click",function(){
			$.fn.zTree.init($("#dept_tree"), deptsetting);
			$("#myModal_dept").removeData("bs.modal");
			$("#myModal_dept").modal({
				 show: true
			});
			
		})
		
		$('#table-javascript').bootstrapTable({
            method: 'get',
            url: "",
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server', 
            pageSize: pagesize,
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            },{
                field: 'id',
                title: '用户id',
                visible: false,
                switchable:false
            }, {
                field: 'usercode',
                title: '用户编码',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'name',
                title: '用户姓名',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'openid',
                title: 'openid',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'userorganization',
                title: '机构编码',
                align: 'center',
                valign: 'middle',
                visible: false,
                switchable:false
            }, {
                field: 'comname',
                title: '所属机构',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'phone',
                title: '联系电话',
                align: 'center',
                valign: 'middle',
                clickToSelect: true,
                sortable: true
            }, {
                field: 'email',
                title: '邮箱',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'statusStr',
                title: '状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'createtime',
                title: '创建时间',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
            	field: 'maturitydata',
            	title: '到期时间',
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
		//初始化角色
		$.ajax({
			url :'rolelist',
			type : 'GET',
			dataType : "json",
			//async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				$.each(data,function(index,data){						
					$("#rolelist").append("<option value='"+data.id+"'>"+data.rolename+"</option>");
				});
			}
		});
		
		// 用户列表页面【查询】按钮
		$("#querybutton").on("click", function(e) {
			//功能包查询
			var usercode = $("#usercode").val();
			var name =$("#name").val();
			var userorganization =$("#deptid").val();
			var selected_roleid = $("#rolelist option:selected").attr("value");

			$('#table-javascript').bootstrapTable(
						'refresh',
						{url:'inituserlist?limit=10&usercode='+usercode
							+'&name='+name+'&userorganization='+userorganization
							+'&roleid='+selected_roleid});
		 });
		
		// 用户列表页面【批量删除】按钮
		$("#benchdeletebutton").on("click", function(e) {
			var arrayuserid = getSelectedRows();
			if(arrayuserid.length==0){
				alertmsg("至少选择一行数据");
			}else{
				if(window.confirm("您选择了"+arrayuserid.length+"条数据，是否确认删除？")){
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
								//功能包查询
								var usercode = $("#usercode").val();
								var name =$("#name").val();
								var userorganization =$("#deptid").val();
								
								$('#table-javascript').bootstrapTable(
											'refresh',
											{url:'inituserlist?limit=10&usercode='+usercode
												+'&name='+name+'&userorganization='+userorganization});
							}else{
								alertmsg("删除失败！");
							}
						}
					});
				}else{
			      return false;
				}	
			}
				
		});
		
		// 用户列表页面【批量停用】按钮
		$("#resetuserstatus").on("click", function(e) {
			var arrayuserid = getSelectedRows();
			$.ajax({
				url : 'resetusersataus?type=1&userIds=' + arrayuserid,
				type : 'GET',
				dataType : "json",
				async : true,
				error : function() {
					alertmsg("Connection error");
				},
				success : function(data) {
					var result = data.code;
					if (result=='0') {
						//功能包查询
						var usercode = $("#usercode").val();
						var name =$("#name").val();
						var userorganization =$("#deptid").val();
						
						$('#table-javascript').bootstrapTable(
									'refresh',
									{url:'inituserlist?limit=10&usercode='+usercode
										+'&name='+name+'&userorganization='+userorganization});
					}else{
						alertmsg(data.message);
					}
				}
			});
		});
		
		// 用户列表页面【批量停用】按钮
		$("#resetuserstatus2on").on("click", function(e) {
			var arrayuserid = getSelectedRows();
			$.ajax({
				url : 'resetusersataus?type=2&userIds=' + arrayuserid,
				type : 'GET',
				dataType : "json",
				async : true,
				error : function() {
					alertmsg("Connection error");
				},
				success : function(data) {
					var result = data.code;
					if (result=='0') {
						//功能包查询
						var usercode = $("#usercode").val();
						var name =$("#name").val();
						var userorganization =$("#deptid").val();
						
						$('#table-javascript').bootstrapTable(
									'refresh',
									{url:'inituserlist?limit=10&usercode='+usercode
										+'&name='+name+'&userorganization='+userorganization});
					}else{
						alertmsg(data.message);
					}
				}
			});
		});
		
		
		// 密码重置按钮
		$("#resetpwd").on("click", function(e) {
			var arrayuserid = getSelectedRows();
			if(arrayuserid.length==0){
				alertmsg("请选中操作数据！");
				return false;
			}
			
			$.ajax({
				url : 'resetpwd?userIds=' + arrayuserid,
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
					}else{
						alertmsg(data.message);
					}
				}
			});
		});
		
		// 查询条件【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#usercode").val("");
			$("#name").val("");
			$("#deptid").val("");
			$("#deptname").val("");
			$("#rolelist").val("");
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
			window.location.href = "main2edit";
		})

		$("#passwordtemp").focus();
		$("#passwordtemp").click(function(){
			$("#pwdmsg").html("(<font color='#666666'><i>说明：密码格式必须是6-16位包含数字、大写字母、小写字母、特殊符号四种任意三种组合</i></font>)");
		});
		// 添加用户界面【添加】按钮
		$("#addbutton").on("click", function(e) {
			if($("#usersaveform").valid()){

				var allOpts = $("#js_multiselect_to_1 option");
				var tasktype=[];
				for(i=0;i<allOpts.length;i++){
					tasktype.push($(allOpts[i]).val());
				}
				$("#js_multiselect_to_1").val(tasktype);

				var pwd = $("#passwordtemp").val();
				var con_pwd = $("#password").val();
				if((pwd == null || pwd == '') ||(con_pwd == null || con_pwd == '')){
					$("#pwdmsg").html("(<font color='red'>* 密码不能为空，请输入!</font>)");
					return;
				}
				if(pwd != con_pwd){
					$("#pwdmsg").html("(<font color='red'>* 两次密码输入不一致, 请重新输入!</font>)");
					return;
				} else {
					var old_pwd = $("#oldpwd").val();
					if(old_pwd != null && old_pwd != ''){
						if(con_pwd != old_pwd){
							var dispatchParams = {
									"oldpwd": old_pwd,
									"newpwd": con_pwd
							};
							$.ajax({
								url : "checkpassword",
								type : 'POST',
								data : dispatchParams,
								dataType : 'json',
								cache : false,
				                async : true,
								error : function() {
									alert("Connection error");
								},
								success : function(data) {
									var result = data.resmsg;
									if (result == 'invalidnewpwd'){
										$("#pwdmsg").html("(<font color='red'>说明：密码格式不规范,必须是6-16位包含数字、大写字母、小写字母、特殊符号四种任意三种组合</font>)");
									} else if (result == 'samepwderr'){
										$("#pwdmsg").html("(<font color='red'>* 新密码不能与旧密码一致, 请重新输入!</font>)");
									} else if(result == 'success'){
										$('#usersaveform').submit();
									}
								}
							});
						} else {
							$('#usersaveform').submit();
						}
					} else {
						var checkpwdreg = /^(?![0-9a-z]+$)(?![0-9A-Z]+$)(?![0-9\W]+$)(?![a-z\W]+$)(?![a-zA-Z]+$)(?![A-Z\W]+$)[a-zA-Z0-9\W_]{6,16}$/;
						if(!checkpwdreg.test(con_pwd)){
							$("#pwdmsg").html("(<font color='red'>说明：密码格式不规范,必须是6-16位包含数字、大写字母、小写字母、特殊符号四种任意三种组合</font>)");
						} else {
							$('#usersaveform').submit();
						}
					}
					
//					//如果"业管账号管理员"和"超级管理员"同时被添加,提示错误信息,否则保存成功
//					if($("#js_multiselect_to_1 option[rolename='业管账号管理员']").length>0
//							&& $("#js_multiselect_to_1 option[rolename='超级管理员']").length>0){
//						alertmsg("业管账号管理员不能有添加“超级管理员”的权限");
//					}else{
						
//					}
				}
			}
		});
	    $(function(){        
	        $.validator.methods.compareDate = function(value, element, param) { 
	            
	            var startDate = $(param).val(); 
	            
	            var date1 = new Date(Date.parse(startDate)); 
	            var date2 = new Date(Date.parse(value)); 
	            return date1 < date2; 
	        }; 
			$("#usersaveform").validate({
				errorLabelContainer : ".alert-danger",
				errorElement : "p",
		        errorClass : "text-left",
		        focusInvalid : false,
		        rules: {
		        	usercode:"required",
		        	name:"required",
		        	deptname:"required",
		        	phone: {  
		        		required: true,  
		        		phone: true  
		        	},  
		        	email: {  
		        		required: true,  
		        		email: true  
		        	},
		        	nowtime:{
		        		
		        	},
		        	maturitydata:{
		        		compareDate: "#nowtime"
		        	}
		        },  
		        messages: { 
		        	usercode:{required: "用户编码不能为空"},
		        	name:{required: "用户名不能为空"},
		        	deptname:{required: "所属机构不能为空"},
		        	phone: {  
		        		required: "请输入手机号",  
		        		phone: "请输入正确的手机号"  
		        	} ,  
		        	email: {  
		        		required: "请输入Email地址",  
		        		email: "请输入正确的email地址"  
		        	},
		        	maturitydata:{
		        		compareDate: "到期日期必须大于当前日期"
		        	}
		        }
		    });
	    });
	});
	
});
//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "inituserlist";
//获得选中行的id列表
function getSelectedRows() {
    var data = $('#table-javascript').bootstrapTable('getSelections');
    if(data.length == 0){
    	alertmsg("至少选择一行数据！");
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
	if(window.confirm("确定要删除吗？")==true){
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
					var usercode = $("#usercode").val();
					var name =$("#name").val();
					//var userorganization =$("#userorganization").val();
					$('#table-javascript').bootstrapTable('refresh',{url:'inituserlist?limit=10&usercode='+usercode+'&name='+name});						
				}else{
					alertmsg("删除失败！");
				}
			}
		});
	}else{
      //alertmsg("取消");
      return false;
	}	
}
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

//根据userid更新用户信息跳转到usersave页面 
function updateuser(id){
	location.href = "main2edit?id=" + id;
}
//刷新列表
function reloaddata(data){
	$.ajax({
		url : "inituserlist",
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
}
//添加事件
function operateFormatter(value, row, index) {
    return [
        '<a class="edit m-left-5"  href="javascript:void(0)" title="编辑">',
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

