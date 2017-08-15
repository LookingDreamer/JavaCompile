require([ "jquery", "zTree", "zTreecheck", "flat-ui", "bootstrap-table",
		"bootstrap", "bootstrapTableZhCn", "public" ], function($) {
	// 数据初始化
	$(function() {
		$("#save_key_button").on("click", function(e) {
			$.ajax({
				url:'saveorupdatekey',
				data:$("#form_date").serialize(),
				type:'post',
				success:function(data){
					var deptId=$("#deptId").val();
					window.location.href="menu2list?deptId="+deptId;
				}
				
			})
		});
		$("#go_back").on("click",function(){
			var managerid = $("#managerid").val();
			window.location.href="menu2list?managerid="+managerid;
		})
	});
});
