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
	/*	var inp = $('#num');
		inp.blur(function(){
			var inpVal = inp.val();
			if(inpVal) {
				if(!isNaN(inpVal)){
						
				}else{
					alert('请输入数字');
					inp.focus();
				}
			}

		})*/
		
		$("#close_modal").on("click",function(){
			 var permissionset_id=$("#permissionsetId").val();
			 window.location.href="mian2edit?permissionsetId="+permissionset_id+"&flag="+ $("#op_flag").val();
		})
		$("#save_permisssionallot").on("click",function(){
			var inpVal = $('#num').val();
			var warningtimes = $('#warningtimes').val();
			var name = $("#permissionname").val();
			if(name !="支付" &&  name !="平台查询"){
				if(!isNaN(inpVal)){
					if(parseInt(inpVal)!=inpVal ){
						alert('试用次数,请输入整数');
						return;
					}
				}else{
					alert('试用次数,请输入数字');
					return;
				}
			}
			if(name =="车险投保" ||  name =="快速续保"||  name =="人工报价"||  name =="提交核保"){
				if (warningtimes) {
					if(!isNaN(warningtimes)){
						if(parseInt(warningtimes)!=warningtimes ||  warningtimes <=0 || parseInt(warningtimes) >= parseInt(inpVal)){
							alert('预警次数只能为整数，数值不能为0或负数且需比使用（试用）次数小');
							return;
						}
					}else{
						alert('预警次数,请输入数字');
						return;
					}
				}

			}
				$.ajax({
					url:'updatepermissionallot',
					type:"post",
					data:$("#permissionallot_form").serialize(),
					 success: function(data){
						 if(data.length>2){
							 alertmsg("保存成功");
							 $("#permissionallot_modal").modal('hide');
							 var permissionset_id=$("#permissionsetId").val();
							 window.location.href="mian2edit?permissionsetId="+permissionset_id+"&flag="+ $("#op_flag").val();
						 }else if(data="1"){
							 alertmsg("修改成功");
							 $("#permissionallot_modal").modal('hide');
							 var permissionset_id=$("#permissionsetId").val();
							 window.location.href="mian2edit?permissionsetId="+permissionset_id+"&flag="+ $("#op_flag").val();
						 }else{
							 alertmsg("保存失败，请稍后重试！");
							 var permissionset_id=$("#permissionsetId").val();
							 window.location.href="mian2edit?permissionsetId="+permissionset_id+"&flag="+ $("#op_flag").val();
						 }
					 }
				})		
		})
		
		//删除当前关系表数据
		$("#delete_permissionallot").on("click",function(){
			$.ajax({
				url:'deletepermissionallot',
				type:"post",
				data:$("#permissionallot_form").serialize(),
				 success: function(data){
					 if(data.status=="1"){
						 alertmsg(data.message);
						 $("#permissionallot_modal").modal('hide');
						 var permissionset_id=$("#permissionsetId").val();
						 window.location.href="mian2edit?permissionsetId="+permissionset_id+"&flag="+ $("#op_flag").val();
					 }else{
						 alertmsg(data.message);
					 }
					
				 }
				
			})
		})
	});
	
	
})