require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "bootstrapdatetimepicker","bootstrapdatetimepickeri18n","jqcookie", "jqtreeview","zTree","zTreecheck","public"], function ($) {
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
		    // showMeridian: 1
		});
		
		$("#save_permisssion").on("click",function(){
			$.ajax({
				url:'updateagentpermission',
				type:"post",
				data:$("#agentpermission_form").serialize(),
				 success: function(data){
					 if(data.status=="1"){
						 alertmsg(data.message);
						 $("#permissionallot_modal").modal('hide');
						 var agent_id=$("#agentid").val();
						 window.location.href="mian2edit?agentId="+agent_id;
					 }else{
						 alertmsg(data.message);
					 }
					
				 }
				
			})
		})
		
		//删除当前关系表数据
		$("#delete_permission").on("click",function(){
			$.ajax({
				url:'deleteagentpermission',
				type:"post",
				data:$("#permissionallot_form").serialize(),
				 success: function(data){
					 if(data.status=="1"){
						 alertmsg(data.message);
						 $("#permissionallot_modal").modal('hide');
						 var agent_id=$("#agentid").val();
						 window.location.href="mian2edit?agentId="+agent_id;
					 }else{
						 alertmsg(data.message);
					 }
				 }
			})
		})
	});
	
	
})