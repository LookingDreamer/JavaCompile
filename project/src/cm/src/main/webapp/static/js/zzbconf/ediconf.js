require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","zTree","zTreecheck","public"], function ($) {
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
            	 field: 'rownum',
                 title: '序号',
                 align: 'center',
                 valign: 'middle',
                 sortable: true
            },{
                field: 'ediname',
                title: '名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'explains',
                title: '说明',
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
		// 用户列表页面【批量删除】按钮
//		返回
		$("#backbutton").on("click",function(e){
			history.go(-1);
		})
	    //双击选项
	    $('#rightselect').dblclick(function() {
	      $("option:selected", this).remove().appendTo('#leftselect');
	    });
		//转到新增页面
		$("#addedi").click(function(){
			location.href = "addedi";
		})
		
		//新增保存ok
		$("#edisaveform").on("click", function(e) {	
		});
		//可选择的保险公司列表
		$.fn.zTree.init($("#treeDemo"), setting);
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
//			$('#usersaveform').submit();
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
		$(function(){
			
			 num = $("#hiddensum").val();
		});
	});

});
//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "initedilist";
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
//根据id删除配置信息
function deleteuser(id){
	if(confirm("确定删除吗")){
	$.ajax({
		url : 'deletebyid?id=' + id,
		type : 'GET',
		dataType : "json",
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			var resultconf = data.confcount;
			
				
			if (resultconf>0) {
				alertmsg("删除成功！");
				reloaddata("");//重新载入
			}else{
				alertmsg("删除失败！");
			}
		
		}
	});
	}
}
//根据ediID修改edi信息跳转到ediedit页面
function updateuser(id){
	location.href = "ediedit?id="+id;
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
var setting = {
		async: {
			enable: true,
			url:"queryprotree",
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
			onCheck: zTreeOnCheck
		}
};

function zTreeOnCheck(event, treeId, treeNode) {
	if(isNaN(num)){
		num = 1;
	}else{
		num = parseInt(num);
	}
	$("#parentcode").val(treeNode.prvcode);
//	$("#leftselect").append('<li ondblclick="f1('+treeNode.prvcode+')" id='+treeNode.prvcode+'>'+treeNode.name+'</li>');
	$("#leftselect").append('<li ondblclick="f1('+treeNode.prvcode+')" id='+treeNode.prvcode+'>'+ '<input type="hidden" name="insbproviderandedi['+num+'].providerid" readonly="readonly" style="border-left:0px;border-top:0px;border-right:0px;border-bottom:1px" value="'+treeNode.id+'">'+treeNode.name+'</li>');
	num++;
}

function f1(id){
	$("#"+id).remove();
}