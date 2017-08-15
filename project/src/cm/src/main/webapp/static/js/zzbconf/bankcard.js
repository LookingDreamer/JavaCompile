require(["jquery", "bootstrap-table","jqvalidatei18n", "bootstrap","bootstrapTableZhCn","zTree","zTreecheck","public"], function ($) {
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
            },{
           	 field: 'rownum',
             title: '序号',
             align: 'center',
             valign: 'middle',
             sortable: true
            },{
                field: 'id',
                title: 'id',
                visible: false,
                switchable:false
            }, {
                field: 'cardprefix',
                title: '卡号前缀',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'cardname',
                title: '卡名',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
            	field: 'banktoname',
            	title: '发卡行名称',
            	align: 'center',
            	valign: 'middle',
            	sortable: true
            }, {
            	field: 'cardtype',
            	title: '卡种',
            	align: 'center',
            	valign: 'middle',
            	sortable: true
            }, {
            	field: 'cardnumlength',
            	title: '卡号长度',
            	align: 'center',
            	valign: 'middle',
            	sortable: true
            }, {
            	field: 'cardbankdeptcode',
            	title: '发卡行机构编码',
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
		
		//根据 下拉框 银行卡名称 查询
		$("#queryByBanktoname").click(function(){
			if(!$("#banktonameselect").val()){
				alertmsg("请选择发卡行");
				$('#table-javascript').bootstrapTable('refresh');
			}else{
				$.ajax({
					url : 'queryByBanktoname?banktoname=' + $("#banktonameselect").val()+'&limit='+10,
					type : 'GET',
					dataType : "json",
					async : true,
					error : function() {
						alertmsg("Connection error");
					},
					success : function(data) {
						$('#table-javascript').bootstrapTable('load', data);
					}
				});
			}
		});
		//刷新
		$("#refresh").click(function(){
			$('#table-javascript').bootstrapTable('refresh');
		});
		
		// 页面【批量删除】按钮
		
		//转到新增页面
		$("#addbankcard").click(function(){
			location.href = "addbankcard";
		})
		
		//新增保存ok
		$("#bankcardsavebutton").on("click", function(e) {	
			if($("#bankcardsaveform").valid()){
				$("#bankcardsaveform").submit();
			}
		});
		$("#bankcardsaveform").validate({
			errorLabelContainer : "#bankcarderror",
			errorElement : "p",
			errorClass : "text-left alert-danger",
			rules : {
				cardprefix:{
					required: true,
					digits: true,
					rangelength: [6,10]
				},
				cardnumlength: {
					maxlength:2,
					digits:true
				},
				cardbankdeptcode:{
					digits: true,
					rangelength: [8,8]
				}
			},
			messages : {
				cardprefix:{
					required: "请输入卡号前缀",
					digits: "请输入合法的整数",
					rangelength: "请输入6-10位数字"
				},
				cardnumlength:{
					maxlength : "长度不能超过2！",
					digits: "请输入合法的整数"
				},
				cardbankdeptcode:{
					digits: "请输入合法的整数",
					rangelength: "请输入8位有效数字"
				}
			}
		});
		// 添加用户界面【添加】按钮
		$("#addbutton").on("click", function(e) {
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
		//返回
		$('#backbutton').on("click",function(){
			history.go(-1)
		});
	});

});
//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "initbankcardlist";
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
}
//根据银行卡ID修改银行卡信息跳转到bankcardedit页面
function updateuser(id){
	location.href = "bankcardedit?id="+id;
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
