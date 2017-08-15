<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置任务数</title>
<script type="text/javascript">
	require([ "system/updatetaskpool" ]);
</script>
<style> 
	.div-height{height:300px;} 
</style> 

</head>
<body>
	<div class="modal-header">
       <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
       <span class="modal-title" id="myModalLabel">修改设置最大任务数</span>
	</div>
	
	<div class="modal-body">
	<div class="div-height">  
	<form id="updatetaskpoolform">
	  <div id="inputfrom" class="input-group-sm">
		<input id="usercode" type="hidden" name="usercode" value="${insc_user.usercode!""}" />
		<table style="margin-left:20%;margin-top:5%;">
		  <div class="input-group-sm">
			<tr><td>最大任务数：</td><td><input id="taskpool" name="taskpool" value="${taskpool!""}" class="form-control" placeholder="20"></td></tr>
		  </div>
		</table>
	   </div>
	</form>
	</div>
	
	<!--<p></p>-->
	
	<div class="modal-footer">
	  <input id="updatetaskpoolbutton" type="submit" class="btn btn-primary" name="updatetaskpoolbutton" value="确定" />
	  <input id="closebutton" type="button" class="btn btn-defult" data-dismiss="modal" name="closebutton" value="关闭" />
	</div>
	</div>
	

</body>
</html>
