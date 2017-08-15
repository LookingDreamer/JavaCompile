require(["jquery","zTree","zTreecheck","fuelux","public"], function ($) {
	//数据初始化 
	$(function() {
		inittree();
		
		//新增ok
		$("#addpro").on("click", function(e) {
			window.location.href="jumpadd";
		});
		//新增保存ok
		$("#savePro").on("click", function(e) {	
		}); 
		//查看修改ok
		$("#detailspro").on("click", function(e) {	
			if(!$("#updateid").val()){
				alertmsg("请选择要查看的供应商!");
				return;
			}
			window.location.href="queryparinfobyid?id="+$("#updateid").val();
		});
		//查看修改保存ok
		$("#saveOrUpdatePro").on("click", function(e) {	
			$("#saveupdateid").val(id);
		});
//		选择供应商ok
		$("#prvname").on("click", function(e) {
			$('#showpic').modal();
			$.fn.zTree.init($("#treeDemo"), setting);
		});
//		选择机构ok
		$("#deptname").on("click", function(e) {
			$('#showdeptpic').modal();
			$.fn.zTree.init($("#treeDemoDept"), settingdept);
		});
		
//		list查询选择机构ok
		$("#querydeptname").on("click", function(e) {
			$('#showquerydeptpic').modal();
			$.fn.zTree.init($("#treequeryDept"), settingquerydept);
		});
		
//		删除ok
		$("#delpro").on("click", function(e) {	
			if(!$("#updateid").val()){
				alertmsg("请选择供应商!");
				return;
			}
			$("#delid").val($("#updateid").val());
		});
		
		// 查询条件【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#prvcode").val("");
			$("#prvname").val("");
			$("#prvgrade").val("");
			$("#prvtype").val("");
			$("#querydeptname").val("");
		});
		
		$('#myTree').on('selected.fu.tree', function (e, selected) {
			getdatafromid(selected.selected[0].dataAttributes.id);
			 $("#hiddenid").val(selected.selected[0].dataAttributes.id);
		});

		$('#myTree').on('updated.fu.tree', function (e, selected) {
			getdatafromid(selected.selected[0].dataAttributes.id);
		});
		
		$('#myTree').on('opened.fu.tree', function (e, info) {
			getdatafromid(info.dataAttributes.id);
		});

		$('#myTree').on('closed.fu.tree', function (e, info) {
			getdatafromid(info.dataAttributes.id);
		});
		
		
	/*	$("#uploadbutton").click(function(){
			var file = $("#uploadfileid").val();
			alertmsg(file);
			if(file){
				if(file!=''&&/\S+\.xlsx$/.test(file)){
					var nnn = $("#comcode").val();
					$("#channelid").val(nnn);
					alertmsg("正在导入用户信息...");
					flm.submit();
				}else{
						alertmsg("请选择.xlsx格式的文件！");
					}
			}else{
				alertmsg("请选择上传文件");
			}
		});*/
		
//		树结构
//		================================>>>>>>>
		
	
	});
	
});
var setting = {
		async: {
			enable: true,
			url:"querypartree",
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
var settingdept = {
		async: {
			enable: true,
			url:"querydepttree",
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
			onCheck: zTreeOnCheckDept
		}
};
var settingquerydept = {
		async: {
			enable: true,
			url:"querydepttree",
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
			onCheck: zTreeOnCheckQueryDept
		}
};
function getdatafromid(id){
	$.ajax({
	   type: "POST",
	   url: "../partners/queryid",
	   data: "parentcode="+id,
	   dataType:"json",
	   success: function(datainfo){
		 $("#updateid").val(id);
	   }
	});
}
//初始化树结构
function inittree() {
	$('#myTree').tree({
	dataSource: function (options, callback) {
		var parentcode ="";
		id = "";
		if(!jQuery.isEmptyObject(options)){
			parentcode =options.dataAttributes.parentcode;
			id = options.dataAttributes.id;
		}
		$.ajax({
			type : "POST",
			url : "../partners/inittreeparlist",
			data :"root="+parentcode,
			dataType : 'json',
			success : function(data) {
				callback({
					data: data
				});
			}
		});
	},
	multiSelect : false,
	cacheItems : true,
	folderSelect : false,
});
	
}
function zTreeOnCheck(event, treeId, treeNode) {
	$("#parentcode").val(treeNode.prvcode);
	$("#prvname").val(treeNode.name);
	$('#showpic').modal("hide");
}
function zTreeOnCheckDept(event, treeId, treeNode) {
	$("#affiliationorg").val(treeNode.id);
	$("#deptname").val(treeNode.name);
	$('#showdeptpic').modal("hide");
}
function zTreeOnCheckQueryDept(event, treeId, treeNode) {
	$("#affiliationorgquery").val(treeNode.id);
	$("#querydeptname").val(treeNode.name);
	$('#showquerydeptpic').modal("hide");
}