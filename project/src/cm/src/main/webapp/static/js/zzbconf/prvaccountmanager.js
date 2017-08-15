require([ "jquery", "zTree", "zTreecheck", "flat-ui", "bootstrap-table",
		"bootstrap", "bootstrapTableZhCn", "public" ], function($) {
	// 数据初始化
	$(function() {
		inittree();
		$("#savebutton").on("click", function(e) {
//			if ($("#form_date").valid()) { 
				$.ajax({
					url:'saveorupdate',
					data:$("#form_date").serialize(),
					type:'post',
					success:function(data){
						alert(data.message);
						var usetype =  $("#queryUsetype").find("option:selected").val();
						var deptid = $("#queryDeptId").val();
						var providerid = $("#queryProviderid").val();
						$('#manageredit').modal("hide");
						$('#prvacc_data_list').bootstrapTable('refresh',{url:'initdatalistpage?deptid='+deptid+'&providerid='+providerid+'&usetype='+usetype});
					}
					
				})
//			}
		});
		
		$("#save_key_button").on("click", function(e) {
			var deptid = $("#queryDeptId").val();
			$.ajax({
				url:'saveorupdatekey?deptId='+deptid,
				data:$("#key_form_data").serialize(),
				type:'post',
				success:function(data){
				
					var usetype =  $("#queryUsetype").find("option:selected").val();
					var deptid = $("#queryDeptId").val();
					var providerid = $("#queryProviderid").val();
					$('#keyedit').modal("hide");
					$('#prvacc_data_list').bootstrapTable('refresh',{url:'initdatalistpage?deptid='+deptid+'&providerid='+providerid+'&usetype='+usetype});
				
					var tempprvid = $("#managerProviderid").val();
					var tempuserType = $("#managerUsetype").val();
					
					
					initKeyDataTwice(deptid,tempuserType,tempprvid);
					
				}
				
			})
		});
		$("#add_button").on("click",function(){
			$(".reset").val('');
			var deptid = $("#queryDeptId").val();
			if(deptid == ""){
				alertmsg("请选择机构！");
			}else{
				//赋值 {deptid: "1237000000", deptname: "山东平台"}
				$.ajax({
					url:'mian2edit?deptid='+deptid,
					type:'get',
					success:function(data){
						console.log(data);
						$("#deptid").val(data.deptid);
						$("#deptname").val(data.deptname);
						$('#manageredit').modal();
					},
					error : function() {
						alertmsg("Connection error");
					}
				});
			}
		});
		
		$.ajax({
			url:'../provider/queryprotreefirst',
			type:'post',
			success:function(data){
				zNodes=data;
			}
		});
		
		//弹出供应商树
		$("#queryProvidername").on("click",function(){
			$('#showpic').modal();
			$('#treeDemosearch').val('');
			$.fn.zTree.init($("#treeDemo"), setting,zNodes);
		});
		
		$("#add_key_button").on("click",function(){
			var managerid = $("#managerid").val();
			
			$("#keyNoti").val('');
			$("#paramvalue").val('');
			$('#paramname').val('');
			$('#keyid').val('');
			
			$('#keyedit').removeData("bs.modal");
			$("#keyedit").modal({
				 show: true
			});
		})
		
		$("#querybutton").on("click",function(){
			var usetype =  $("#queryUsetype").find("option:selected").val();
			var deptid = $("#queryDeptId").val();
			var providerid = $("#queryProviderid").val();
			$('#prvacc_data_list').bootstrapTable('refresh',{url:'initdatalistpage?deptid='+deptid+'&providerid='+providerid+'&usetype='+usetype});
			
			//刷新子表数据，数据清空 TODO
			initKeyData();
		
		})
		$("#resetbutton").on("click",function(){
			$("#queryProvidername").val("");
			$("#queryProviderid").val("");
			 $("#queryUsetype").val("0");
		})
		
		$("#prvName4edit").on("click",function(){
			$('#showpic').modal();
			$.fn.zTree.init($("#treeDemo"), settingup,zNodes);
			$('#manageredit').modal("hide");
		});
		
		$('#prvacc_data_list').bootstrapTable({
			method : 'get',
			url : "initdatalistpage",
			cache : false,
			striped : true,
			pagination : true,
			sidePagination : 'server',
			pageSize : 10,
			singleSelect : 'true',
			clickToSelect : true,
			minimumCountColumns : 2,
			columns : [{
				field : 'prvname',
				title : '供应商',
				align : 'center',
				valign : 'middle',
				sortable : true
			},{
				field : 'deptname',
				title : '机构',
				align : 'center',
				valign : 'middle',
				sortable : true
			},{
				field : 'usetypeStr',
				title : '类型',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'noti',
				title : '备注',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'operating',
				title : '操作',
				align : 'center',
				valign : 'middle',
				switchable : false,
				formatter : operateFormatter1,
				events : operateEvents1
			} ]
		}).on('click-row.bs.table', function(e, row, $element) {
				$("table tr").click(function(){						
					$(this).css("background","#ccc").siblings().css("background","");
				});
				
				var id = row.id;
				if(id=="undifind"){
					id="";
				}
				$("#manageridmain").val(id);
				$("#managerProviderid").val(row.providerid);
				$("#managerUsetype").val(row.usetype);
				$("#managerDeptid").val(row.deptid);
				
				
				 var deptId=$('#queryDeptId').val();
				 var usetype = row.usetype;
				 var providerid=row.providerid;
				 $("#showPrvName").text(row.prvname);
				$('#prvacc_key_list').bootstrapTable('refresh',{url:'initkeylistpage?deptId='+deptId+"&usetype="+usetype+"&providerid="+providerid});
				$table = $('#prvacc_key_list');
		})
		$('#prvacc_key_list').bootstrapTable({
			method : 'get',
			url : "initkeylistpage",
			cache : false,
			striped : true,
			pagination : true,
			sidePagination : 'server',
			pageSize : 10,
			singleSelect : 'true',
			clickToSelect : true,
			minimumCountColumns : 2,
			columns : [{
				field : 'deptname',
				title : '机构',
				align : 'center',
				valign : 'middle',
				sortable : true
			},{
				field : 'paramname',
				title : '参数编码',
				align : 'center',
				valign : 'middle',
				sortable : true
			},{
				field : 'paramvalue',
				title : '参数值',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'noti',
				title : '备注',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'operating',
				title : '操作',
				align : 'center',
				valign : 'middle',
				switchable : false,
				formatter : operateFormatter2,
				events : operateEvents2
			} ]
		});
		$('#querybutton').click();
		var managerid =$("#managerid").val();
		$('#prvacc_key_list').bootstrapTable('refresh',{url:'initkeylistpage?managerid='+managerid});
	});
});
function updatedata(id,deptid){
	var tree_deptid = $("#queryDeptId").val();
	if(tree_deptid!=deptid){
		alert("只能修改本级机构数据");
		return false;
	}
	$.ajax({
		url:'mian2edit?deptid='+tree_deptid+"&id="+id,
		type:'get',
		success:function(data){
			if(data!=null){
				$("#id").val(data.model.id);
				$("#prvName4edit").val(data.prvName);
				$("#providerid4edit").val(data.prvId);
				$("#version").val(data.model.version);
				$("#deptid").val(data.model.deptid);
				$("#deptname").val(data.deptname);
				$("#account").val(data.model.account);
				$("#pwd").val(data.model.pwd);
				$("#loginurl").val(data.model.loginurl);
				$("#org").val(data.model.org);
				$("#noti").val(data.model.noti);
				
				
				if(data.model.permission==1){
					$("#p1").attr("checked","true");
				}else if(data.model.permission==2){
					$("#p2").attr("checked","true");
				}
				
				if(data.model.usetype==1){
					 $("#usetype").find("option[value='1']").attr("selected",true);
				}else if(data.model.usetype==2){
					 $("#usetype").find("option[value='2']").attr("selected",true);
				}
				
				$('#manageredit').modal("show");
			}
		}
	})
}
function updatekeydata(id,deptname){
	$.ajax({
		url:'mian2keyedit?id='+id,
		type:'get',
		success:function(data){
			if(data && data.model){
				$("#paramname").val(data.model.paramname);
				$("#paramvalue").val(data.model.paramvalue);
				$("#keyNoti").val(data.model.noti);
				$("#keyid").val(data.model.id);
				$("#managerid").val(data.model.managerid);
				$("#keyedit").modal({ show: true});
			} else {
				alert("数据获取失败。");
			}
		},
		error: function(data){
			alert("数据获取失败,原因是：" + data);
		}
	
	})
	
}
function deletedata(id,deptid){
	var tree_deptid = $("#queryDeptId").val();
	if(tree_deptid!=deptid){
		alert("只能修改本级机构数据");
		return false;
	}
	$.ajax({
		url:'deletebyid?id='+id,
		type:'get',
		success:function(data){
			alert(data.message);
			$('#prvacc_data_list').bootstrapTable(
					'refresh',
					{url:'initdatalistpage?limit=10&deptid='+tree_deptid});
		}
			
	})
}
function deletekeydata(id,deptname,deptid){
	var tree_deptid = $("#queryDeptId").val();
	if(tree_deptid!=deptid){
		alert("只能修改本级机构数据");
		return false;
	}
	var mid = $("#managerid").val();
	$.ajax({
		url:'deletekeybyid?id='+id,
		type:'get',
		success:function(data){
			alert(data.message);
			 var providerid = $("#managerProviderid").val();
			var usetype = $("#managerUsetype").val();
			var deptId = $("#managerDeptid").val();
			$('#prvacc_key_list').bootstrapTable('refresh',{url:'initkeylistpage?deptId='+deptId+"&usetype="+usetype+"&providerid="+providerid});
		}
	
	})
}

/* zTree初始化数据 */
function inittree() {
	$.ajax({
		url : "inscdeptlist",
		type : 'POST',
		dataType : "json",
		error : function() {
			$("#deptTree").html('加载失败，<a href="javascrpt:void(0);" onclick="inittree();">点击这里</a>重试。');
		},
		success : function(data) {
			$.fn.zTree.init($("#deptTree"), {
				data : {
					simpleData: {
						enable: true,
						idKey: "id",
						pIdKey: "pId",
						rootPId: "1200000000"
					}
				},
				view: {
					fontCss: function (treeId, treeNode) {
						return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
					},
					expandSpeed: ""
				},
				callback : {
					onClick : getdatafromid
				}
			}, data);
		}
	});
}
var deptSetting = {
	async : {
		enable : true,
		url : "inscdeptlist",
		autoParam : [ "id=root" ]
	},
	callback : {
		onClick : getdatafromid
	}
};

function getdatafromid(event, treeId, treeNode) {
	var id = treeNode.id;
	$("#queryDeptId").val(id);
	$("#queryDeptName").val(treeNode.name);
	var usetype =  $("#queryUsetype").find("option:selected").val();
	var providerid=$("#queryProviderid").val();
	$('#prvacc_data_list').bootstrapTable(
			'refresh',
			{url:'initdatalistpage?deptid='+id+"&providerid="+providerid+"&usetype="+usetype});
	initKeyData();

}

function initKeyDataTwice(deptId,usetype,providerid){
	$('#prvacc_key_list').bootstrapTable('destroy');
	$('#prvacc_key_list').bootstrapTable({
		method : 'get',
		url : 'initkeylistpage?deptId='+deptId+'&usetype='+usetype+'&providerid='+providerid,
		cache : false,
		striped : true,
		pagination : true,
		sidePagination : 'server',
		pageSize : 10,
		singleSelect : 'true',
		clickToSelect : true,
		minimumCountColumns : 2,
		columns : [{
			field : 'deptname',
			title : '机构',
			align : 'center',
			valign : 'middle',
			sortable : true
		},{
			field : 'paramname',
			title : '参数编码',
			align : 'center',
			valign : 'middle',
			sortable : true
		},{
			field : 'paramvalue',
			title : '参数值',
			align : 'center',
			valign : 'middle',
			sortable : true
		}, {
			field : 'noti',
			title : '备注',
			align : 'center',
			valign : 'middle',
			sortable : true
		}, {
			field : 'operating',
			title : '操作',
			align : 'center',
			valign : 'middle',
			switchable : false,
			formatter : operateFormatter2,
			events : operateEvents2
		} ]
	});

}

function initKeyData(){
	$('#prvacc_key_list').bootstrapTable('destroy');
	$('#prvacc_key_list').bootstrapTable({
		method : 'get',
		url : "initkeylistpage",
		cache : false,
		striped : true,
		pagination : true,
		sidePagination : 'server',
		pageSize : 10,
		singleSelect : 'true',
		clickToSelect : true,
		minimumCountColumns : 2,
		columns : [{
			field : 'deptname',
			title : '机构',
			align : 'center',
			valign : 'middle',
			sortable : true
		},{
			field : 'paramname',
			title : '参数编码',
			align : 'center',
			valign : 'middle',
			sortable : true
		},{
			field : 'paramvalue',
			title : '参数值',
			align : 'center',
			valign : 'middle',
			sortable : true
		}, {
			field : 'noti',
			title : '备注',
			align : 'center',
			valign : 'middle',
			sortable : true
		}, {
			field : 'operating',
			title : '操作',
			align : 'center',
			valign : 'middle',
			switchable : false,
			formatter : operateFormatter2,
			events : operateEvents2
		} ]
	});
}
function operateFormatter1(value, row, index) {
	return [
	        '<a class="edit m-left-5"  href="javascript:void(0)" title="编辑">',
	        '<i class="glyphicon glyphicon-edit"></i>',
	        '</a>',
	        '<a class="remove m-left-5" href="javascript:void(0)" title="删除">',
	        '<i class="glyphicon glyphicon-remove"></i>',
	        '</a>'
	    ].join('');
}
// 事件相应
window.operateEvents1 = {
	'click .edit' : function(e, value, row, index) {
		updatedata(row.id,row.deptid);
	},
	'click .remove': function (e, value, row, index) {
    	deletedata(row.id,row.deptid);
    }
};

function operateFormatter2(value, row, index) {
	return [
	        '<a class="edit m-left-5"  href="javascript:void(0)" title="编辑">',
	        '<i class="glyphicon glyphicon-edit"></i>',
	        '</a>',
	        '<a class="remove m-left-5" href="javascript:void(0)" title="删除">',
	        '<i class="glyphicon glyphicon-remove"></i>',
	        '</a>'
	    ].join('');
}
// 事件相应
window.operateEvents2 = {
	'click .edit' : function(e, value, row, index) {
		updatekeydata(row.id,row.deptname);
	},
	'click .remove': function (e, value, row, index) {
    	deletekeydata(row.id,row.deptname,row.deptid);
    }
};

var zNodes;
var setting = {
		check : {
			enable : true,
			chkStyle : "radio",
			radioType : "all"
		},
		view: {
			fontCss: function (treeId, treeNode) {
				return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
			},
			expandSpeed: ""
		},
		callback : {
			onCheck : zTreeOnCheck
		}
	};

var settingup = {
		check : {
			enable : true,
			chkStyle : "radio",
			radioType : "all"
		},
		view: {
			fontCss: function (treeId, treeNode) {
				return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
			},
			expandSpeed: ""
		},
		callback : {
			onCheck : zTreeOnCheckup
		}
	};

function zTreeOnCheck(event, treeId, treeNode) {
	//$("#org").val(treeNode.id);
	$("#queryProvidername").val(treeNode.name);
	$("#queryProviderid").val(treeNode.id);
	
	
	$('#showpic').modal("hide");
}

function zTreeOnCheckup(event, treeId, treeNode) {
	$("#providerid4edit").val(treeNode.id);
	$("#prvName4edit").val(treeNode.name);
	$('#showpic').modal("hide");
	$('#manageredit').modal();
}