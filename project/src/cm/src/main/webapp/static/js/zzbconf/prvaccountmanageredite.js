require([ "jquery", "zTree", "zTreecheck", "flat-ui", "bootstrap-table",
		"bootstrap", "bootstrapTableZhCn", "public" ], function($) {
	// 数据初始化
	$(function() {
		$("#savebutton").on("click", function(e) {
			var use_type = $("#usetype").find("option:selected").val();
			if(use_type==0){
				alert("请选择精灵EDI");
				return false;
			}
			$.ajax({
				url:'saveorupdate',
				data:$("#form_date").serialize(),
				type:'post',
				success:function(data){
					alert(data.message);
					window.location.href="menu2list";
				}
				
			})
		});
		//弹出机构树
		$("#orgname").on("click",function(){
			$('#showpic').modal();
			$.fn.zTree.init($("#treeDemo"), setting,zNodes);
			
		})
		$("#go_back").on("click",function(){
			var deptid = $("#deptid").val();
			window.location.href="menu2list?deptId="+deptid;
		})
		
		$.ajax({
			url:'../provider/queryprotreefirst',
			type:'post',
			success:function(data){
				zNodes=data;
			}
		})
	});
});
var zNodes;
var setting = {
//		async : {
//			enable : true,
//			url : "../provider/queryprotree",
//			dataType : "json",
//			type : "post"
//		},
		check : {
			enable : true,
			chkStyle : "radio",
			radioType : "all"
		},
		callback : {
			onCheck : zTreeOnCheck
		}
	};
function zTreeOnCheck(event, treeId, treeNode) {
	$("#org").val(treeNode.id);
	$("#orgname").val(treeNode.name);
	$('#showpic').modal("hide");
}