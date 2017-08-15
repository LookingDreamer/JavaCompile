require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public"], function ($) {
	//数据初始化 
	$(function() {
		$('#table-javascript').bootstrapTable({
            method: 'get',
            url: pageurl,
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
            }, {
                field: 'id',
                title: '保单id',
                visible: false,
                switchable:false
            }, {
                field: 'policyno',
                title: '保单号码',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'applicantname',
                title: '投保人',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'insuredname',
                title: '被保人',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'phonenumber',
                title: '手机号码',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'startdate',
                title: '起保日期',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'policystatus',
                title: '保单状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            }]
        });
		
		//单击table行  显示车辆
		
		
		// 用户列表页面【查询】按钮
		$("#querybutton").on("click", function(e) {
			var postdata = "";
			if($("#ownername").val()){
				postdata += "&ownername=" + $("#ownername").val();
			}
			if($("#policyno").val()){
				postdata += "&policyno=" + $("#policyno").val();
			}
			if($("#engineno").val()){
				postdata += "&engineno=" + $("#engineno").val();
			}
			if($("#standardfullname").val()){
				postdata += "&standardfullname=" + $("#standardfullname").val();
			}
			if($("#carlicenseno").val()){
				postdata += "&carlicenseno=" + $("#carlicenseno").val();
			}
			if($("#riskkindname").val()){
				postdata += "&riskkindname=" + $("#riskkindname").val();
			}
			reloaddata(postdata);
		 });
		
		// 用户列表页面【批量删除】按钮
		$("#benchdeletebutton").on("click", function(e) {
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
						reloaddata("");//重新载入
					}else{
						alertmsg("删除失败！");
					}
				}
			});
		});
		
		// 查询条件【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#ownername").val("");
			$("#policyno").val("");
			$("#engineno").val("");
			$("#standardfullname").val("");
			$("#carlicenseno").val("");
			$("#riskkindname").val("");
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
				/*$('#usersaveform').ajaxSubmit({
					url : 'saveuser',
					type : 'POST',
					dataType : "json",
					error : function() {
						alertmsg("Connection error");
					},
					success : function(data) {
						var result = data.flag;
						alertmsg(result);
						if (result=="success") {
							alertmsg("保存用户成功");
						}else{
							alertmsg("保存用户失败");;
						}
					}
				});*/
			//}
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
	});

});
//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "initcarinfolist";
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
				reloaddata("");//重新载入
			}else{
				alertmsg("删除失败！");
			}
		}
	});
}
//根据userid更新用户信息跳转到usersave页面
function updateuser(id){
	location.href = "save?id=" + id;
}
//刷新列表
function reloaddata(data){
	$.ajax({
		url : pageurl,
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
        /*'<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
        '<i class="glyphicon glyphicon-edit"></i>',
        '</a>',
        '<a class="remove m-left-5" href="javascript:void(0)" title="删除">',
        '<i class="glyphicon glyphicon-remove"></i>',
        '</a>'*/
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