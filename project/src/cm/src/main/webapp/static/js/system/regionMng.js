require([ "jquery", "fuelux", "zTree", "zTreecheck", "bootstrap-table", "bootstrap","bootstrapTableZhCn","additionalmethods", "jqvalidatei18n","jqcookie", "jqtreeview","lodash","public" ], function($) {

	$(function() {
		inittree();

		$("#savebutton").on("click", function(e) {
			submit();
		});
		//点击弹出出单网点选择页面
		$("#choose").on("click", function(e) {
			$('#showDeptTree').modal();
			$.fn.zTree.init($("#deptTreeDemo"), setting);
		});
		$(".closeShowDeptTree").on("click", function(e) {
			$('#showDeptTree').modal("hide");
		});

	});	
	
});

function submit(){
	var id = $('#id').val();
	if (!id) {
		alertmsg("请选择地区！");
	}
	var shortname =  $('#shortname').val();
	if (!shortname || $.trim(shortname) == '') {
		$('#deptid').val('');
		$('#shortname').val('');
	}
	var register = $('#register').val();
	var sp = $('#setDistrict').html().split(" ");

	if (register && register ==1){
		if (!$('#deptid').val() && sp && sp.length != 4) {
			alertmsg("开放注册地区，必须选择默认出单网点！");
			return;
		}
	}
	$.ajax({
		url : "../region/update",
		type : 'POST',
		dataType : "json",
		data : {
			"id":$('#id').val(),
			"register" : $('#register').val(),
			"deptid" : $('#deptid').val(),
			"shortname" : $('#shortname').val()
		},
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			if (data) {
				if (data == 1) {
					inittree();
					alertmsg("更新成功！");
				} else {
					alertmsg("更新失败！");
				}
			}
		}
	});
}

function getdatafromid(event,treeId,treeNode){
	$('#setDistrict').html(treeNode.setDistrict)
	$('#id').val(treeNode.id)
	$('#register').val(treeNode.register);
	$('#deptid').val(treeNode.deptid);
	$('#shortname').val(treeNode.shortname);
}

/* zTree初始化数据 */
function inittree() {
	$.fn.zTree.init($("#deptTree"), deptSetting);
}
var deptSetting = {
		async: {
			enable: true,
			url: "../region/initRegion",
			autoParam: ["id"]
		},
		/*check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},*/
		callback: {
			onClick: getdatafromid
		}
};

var setting = {
	async : {
		enable : true,
		url : "/cm/business/cartaskmanage/queryparttree",
		autoParam : [ "id" ],//每次重新请求时传回的参数
		dataType : "json",
		type : "post"
	},
	check : {
		enable : true,
		chkStyle : "radio",
		radioType : "all"
	},
	callback : {
		onCheck : zTreeOnCheckDept//回调函数
	}
};

//选择后回调函数
function zTreeOnCheckDept(event, treeId, treeNode) {
	$("#deptid").val(treeNode.id);
	$("#shortname").val(treeNode.name);
	$('#showDeptTree').modal("hide");
}


