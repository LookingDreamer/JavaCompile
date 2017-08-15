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
         "multiselect","jsoneditor"], function ($) {
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
		$("#go_back").on("click",function(){
			window.location.href="menu2list";
		})
		$('.js-multiselect').multiselect({
			right: '#js_multiselect_to_1',
			rightAll: '#js_right_All_1',
			beforeMoveToRight:function($left, $right, option){
//			    alert("Select value: "+option.attr('rolename'));
			    if(option.attr('rolename') == "超级管理员"){
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
			rightSelected: '#js_right_Selected_1',
			leftSelected: '#js_left_Selected_1',
			leftAll: '#js_left_All_1'
		});
		
		$("#go_back").on("click",function(){
			window.location.href="ediindex";
		})
		
		//弹出机构树
		$("#orgname").on("click",function(){
			$.fn.zTree.init($("#dept_tree"), deptsetting);
			$("#myModal_dept").removeData("bs.modal");
			$("#myModal_dept").modal({
				 show: true
			});
		})
		
		$('#table-javascript').bootstrapTable({
            method: 'get',
            url: "queryedi?quotetype="+$("#quotetype").val(),
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server', 
            pageSize: pagesize,
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
                field: 'id',
                title: 'id',
                visible: false,
                switchable:false
            },{
                field: 'orgcode',
                title: 'orgcode',
                visible: false,
                switchable:false
            }, {
                field: 'name',
                title: '名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'orgname',
                title: '所属机构',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'status',
                title: '健康状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'total',
                title: '累计任务数',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'suc',
                title: '累计成功任务数',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'avg',
                title: '平均耗时（秒）',
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
		
		$('#table-javascript-1').bootstrapTable({
            method: 'get',
            url: "editaskinfo?monitoeid="+$("#monitoeid").val(),
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server', 
            pageSize: pagesize,
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
                field: 'taskid',
                title: '询价号',
                align: 'center',
                valign: 'middle',
                sortable: true
            },{
                field: 'inscomcode',
                title: '询价号',
                visible: false,
                switchable:false
            }, {
                field: 'inscomname',
                title: '公司名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'plateno',
                title: '车牌号',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'carowername',
                title: '车主',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'tasktypename',
                title: '任务类型',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'taskstatusdes',
                title: '任务状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'startdateString',
                title: '任务接受时间',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'enddateString',
                title: '任务完成时间',
                align: 'center',
                valign: 'middle',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'usetime',
                title: '耗时（秒）',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'origindate',
                title: '原始数据',
                align: 'center',
                valign: 'middle',
                switchable:false,
                formatter:taskBodyFormatter,
                events: taskBodyEvents
            }, {
                field: 'resultdate',
                title: '结果数据',
                align: 'center',
                valign: 'middle',
                switchable:false,
                formatter:taskBodyFormatter,
                events: taskBodyEvents1
            }, {
                field: 'operating',
                title: '操作',
                align: 'center',
                valign: 'middle',
                switchable:false,
                formatter: operateFormatter1,
                events: operateEvents
            }]
        });
		
		$("#zeroerrorcount").on("click", function(e) {
			var monitorid =$("#monitoeid").val();
			var quotetype =$("#quotetype").val();
			$.ajax({
	            url: "zeroerrorcount?monitorid=" + monitorid + "&quotetype=" + quotetype,
	            type:"get",
	            success: function(data) {
	            	setInnerTet("jiankang","健康");
	            }
	        });
		 });
		
		// 用户列表页面【查询】按钮
		$("#querybutton").on("click", function(e) {
			var name =$("#name").val();
			var orgcode =$("#orgcode").val();
			var status =$("#status").val();
			var quotetype =$("#quotetype").val();
			var tageturl = 'queryedi?1=1';
			if(name!=null&&name!=''){
				tageturl = tageturl + "&name=" + name;
			}
			if(orgcode!=null&&orgcode!=''){
				tageturl = tageturl + "&orgcode=" + orgcode;
			}
			if(status!=null&&status!=''){
				tageturl = tageturl + "&status=" + status;
			}
			if(quotetype!=null&&quotetype!=''){
				tageturl = tageturl + "&quotetype=" + quotetype;
			}
			$('#table-javascript').bootstrapTable(
						'refresh',
						{url:tageturl});
		 });
		
		$("#querybuttonInfo").on("click", function(e) {
			var taskid =$("#taskid").val();
			var plateno =$("#plateno").val();
			var tasktype =$("#tasktype").val();
			var taskstatus =$("#taskstatus").val();
			var carowername =$("#carowername").val();
			var tageturl = 'editaskinfo?monitoeid='+$("#monitoeid").val();
			if(taskid!=null&&taskid!=''){
				tageturl = tageturl + "&taskid=" + taskid;
			}
			if(plateno!=null&&plateno!=''){
				tageturl = tageturl + "&plateno=" + plateno;
			}
			if(tasktype!=null&&tasktype!=''){
				tageturl = tageturl + "&tasktype=" + tasktype;
			}
			if(taskstatus!=null&&taskstatus!=''){
				tageturl = tageturl + "&taskstatus=" + taskstatus;
			}
			if(carowername!=null&&carowername!=''){
				tageturl = tageturl + "&carowername=" + carowername;
			}
			$('#table-javascript-1').bootstrapTable(
						'refresh',
						{url:tageturl});
		 });
		$("#all").on("click", function(e) {
			var tageturl = 'editaskinfo?monitoeid='+$("#monitoeid").val() + "&todayflag=true";
			$("#taskid").val("");
			$("#plateno").val("");
			$("#carowername").val("");
			$("#tasktype option:first").prop("selected", 'selected');
			$("#taskstatus option:first").prop("selected", 'selected');
			$('#table-javascript-1').bootstrapTable(
						'refresh',
						{url:tageturl});
		 });
		$("#suc").on("click", function(e) {
			var tageturl = 'editaskinfo?monitoeid='+$("#monitoeid").val() + "&todayflag=true";
			var taskstatus = 2;
			if(taskstatus!=null&&taskstatus!=''){
				tageturl = tageturl + "&taskstatus=" + taskstatus;
			}
			$("#taskid").val("");
			$("#plateno").val("");
			$("#carowername").val("");
			$("#tasktype option:first").prop("selected", 'selected');
			$("#taskstatus option:eq(1)").prop("selected", 'selected');
			$('#table-javascript-1').bootstrapTable(
						'refresh',
						{url:tageturl});
		 });
		$("#error").on("click", function(e) {
			var tageturl = 'editaskinfo?monitoeid='+$("#monitoeid").val() + "&todayflag=true";
			var taskstatus = 1;
			if(taskstatus!=null&&taskstatus!=''){
				tageturl = tageturl + "&taskstatus=" + taskstatus;
			}
			$("#taskid").val("");
			$("#plateno").val("");
			$("#carowername").val("");
			$("#tasktype option:first").prop("selected", 'selected');
			$("#taskstatus option:eq(2)").prop("selected", 'selected');
			$('#table-javascript-1').bootstrapTable(
						'refresh',
						{url:tageturl});
		 });
		$("#totalall").on("click", function(e) {
			var tageturl = 'editaskinfo?monitoeid='+$("#monitoeid").val() + "&todayflag=true";
			var taskstatus = '0';
			if(taskstatus!=null&&taskstatus!=''){
				tageturl = tageturl + "&taskstatus=" + taskstatus;
			}
			$("#taskid").val("");
			$("#plateno").val("");
			$("#carowername").val("");
			$("#tasktype option:first").prop("selected", 'selected');
			$("#taskstatus option:eq(3)").prop("selected", 'selected');
			$('#table-javascript-1').bootstrapTable(
						'refresh',
						{url:tageturl});
		 });
		$("#quoteorder").on("click", function(e) {
			var tageturl = 'editaskinfo?monitoeid='+$("#monitoeid").val() + "&todayflag=true";
			var taskstatus = '0';
			var tasktype = 'quote';
			if(taskstatus!=null&&taskstatus!=''){
				tageturl = tageturl + "&taskstatus=" + taskstatus;
			}
			if(tasktype!=null&&tasktype!=''){
				tageturl = tageturl + "&tasktype=" + tasktype;
			}
			$("#taskid").val("");
			$("#plateno").val("");
			$("#carowername").val("");
			$("#tasktype option:eq(1)").prop("selected", 'selected');
			$("#taskstatus option:eq(3)").prop("selected", 'selected');
			$('#table-javascript-1').bootstrapTable(
						'refresh',
						{url:tageturl});
		 });
		$("#insureorder").on("click", function(e) {
			var tageturl = 'editaskinfo?monitoeid='+$("#monitoeid").val() + "&todayflag=true";
			var taskstatus = '0';
			var tasktype = 'insure';
			if(taskstatus!=null&&taskstatus!=''){
				tageturl = tageturl + "&taskstatus=" + taskstatus;
			}
			if(tasktype!=null&&tasktype!=''){
				tageturl = tageturl + "&tasktype=" + tasktype;
			}
			$("#taskid").val("");
			$("#plateno").val("");
			$("#carowername").val("");
			$("#tasktype option:eq(2)").prop("selected", 'selected');
			$("#taskstatus option:eq(3)").prop("selected", 'selected');
			$('#table-javascript-1').bootstrapTable(
						'refresh',
						{url:tageturl});
		 });
		$("#xborder").on("click", function(e) {
			var tageturl = 'editaskinfo?monitoeid='+$("#monitoeid").val() + "&todayflag=true";
			var taskstatus = '0';
			var tasktype = 'xb';
			if(taskstatus!=null&&taskstatus!=''){
				tageturl = tageturl + "&taskstatus=" + taskstatus;
			}
			if(tasktype!=null&&tasktype!=''){
				tageturl = tageturl + "&tasktype=" + tasktype;
			}
			$("#taskid").val("");
			$("#plateno").val("");
			$("#carowername").val("");
			$("#tasktype option:eq(3)").prop("selected", 'selected');
			$("#taskstatus option:eq(3)").prop("selected", 'selected');
			$('#table-javascript-1').bootstrapTable(
						'refresh',
						{url:tageturl});
		 });
		$("#appovedorder").on("click", function(e) {
			var tageturl = 'editaskinfo?monitoeid='+$("#monitoeid").val() + "&todayflag=true";
			var taskstatus = '0';
			var tasktype = 'approved';
			if(taskstatus!=null&&taskstatus!=''){
				tageturl = tageturl + "&taskstatus=" + taskstatus;
			}
			if(tasktype!=null&&tasktype!=''){
				tageturl = tageturl + "&tasktype=" + tasktype;
			}
			$("#taskid").val("");
			$("#plateno").val("");
			$("#carowername").val("");
			$("#tasktype option:eq(4)").prop("selected", 'selected');
			$("#taskstatus option:eq(3)").prop("selected", 'selected');
			$('#table-javascript-1').bootstrapTable(
						'refresh',
						{url:tageturl});
		 });
		// 查询条件【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#orgcode").val("");
			$("#orgname").val("");
			$("#name").val("");
			$("#name").val("");
			$("#status option:first").prop("selected", 'selected');
		});
		// 查询条件【重置】按钮
		$("#resetbuttonInfo").on("click", function(e) {
			$("#taskid").val("");
			$("#plateno").val("");
			$("#carowername").val("");
			$("#tasktype option:first").prop("selected", 'selected');
			$("#taskstatus option:first").prop("selected", 'selected');
		});
		//转到新增页面
		$("#turn2edite").click(function(){
			window.location.href = "main2edit";
		})
		
	});
	
});
//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "queryedi";
var deptsetting = {
		async: {
			enable: true,
			url:"toporginfo",
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
	$("#orgcode").val(treeNode.prvcode);
	$("#orgname").val(treeNode.name);
	$('#myModal_dept').modal("hide");
}
function viewInfo(id,orgcode){
	location.href = "ediinfo?id=" + id + "&orgcode=" + orgcode;
}
function viewInfo2(taskid,inscomcode,inscomname){
	//跳出工作流轨迹弹出框
	window.parent.openDialogForCM("monitor/showflowerror4monitor?maininstanceid="+taskid
			+"&inscomcode="+inscomcode+"&inscomName="+inscomname);
}

//添加事件
function operateFormatter(value, row, index) {
    return [
        '<a class="view m-left-5"  href="javascript:void(0)" title="查看">',
        '<i class="glyphicon">查看</i>',
        '</a>'
    ].join('');
}
//事件相应
window.operateEvents = {
    'click .view': function (e, value, row, index) {
    	viewInfo(row.id,row.orgcode);
    },
	'click .viewerror': function (e, value, row, index) {
		viewInfo2(row.taskid,row.inscomcode,row.inscomname);
	}
};

//添加事件
function operateFormatter1(value, row, index) {
    return [
        '<a class="viewerror m-left-5"  href="javascript:void(0)" title="查看">',
        '<i class="glyphicon">查看</i>',
        '</a>'
    ].join('');
}



//任务数据行格式化
function taskBodyFormatter(value, row, index) {
    return '<a class="editdata ml10"><button type="button" class="btn btn-primary">查看</button>';
}

//任务数据行事件
window.taskBodyEvents = {
    'click .editdata': function (e, value, row, index) {
    	$.ajax({
            url: "getJaonInfo?url=" + row.origindate,
            type:"get",
            contentType:"application/json",
            success: function(data) {
            	setInnerTet("taskBodyModalLabel", row.taskid);
            	$('#message-text').val(data.toString());
            }
        });
    	  $('#taskBodyModal').modal('show');
    }
};

//任务数据行事件
window.taskBodyEvents1 = {
    'click .editdata': function (e, value, row, index) {
    	$.ajax({
            url: "getJaonInfo?url=" + row.resultdate,
            type:"get",
            contentType:"application/json",
            success: function(data) {
            	setInnerTet("taskBodyModalLabel", row.taskid);
            	$('#message-text').val(data.toString());
            }
        });
    	  $('#taskBodyModal').modal('show');
    }
};

function setInnerTet(id, value) {
    document.getElementById(id).innerText = value;
}

