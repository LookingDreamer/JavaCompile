require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn",  "jqcookie", "jqtreeview"], function ($) {
	//数据初始化 
	$(function() {
		
		//返回到列表页面
		$("#go_back").on("click",function(){
			window.location.href="menu2list";
		})
		//根据群组类型加载不同的群组详情页面
		$("#group_detail").on("click",function(){
			//拿到所有数据传到后台，根据群组类型选择详情页面
			var group_name = $("#groupname").val();
			var pid = $("#pid").val();
			var group_kind=$("#groupkind").val();
			window.location.href="common2detail?groupName="+group_name+"&pid="+pid+"&groupKind="+group_kind;
		})
	})
	
})
