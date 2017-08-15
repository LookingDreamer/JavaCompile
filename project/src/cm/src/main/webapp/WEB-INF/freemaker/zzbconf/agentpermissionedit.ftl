<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/agentpermissionedit" ]);
</script>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true">&times;</button>
	<h4 class="modal-title">详细信息</h4>
</div>
<form class="form-horizontal" action="updatepermissionallot" id="agentpermission_form"
	method="post">
	<div class="modal-body">
		<div class="div-height">
			<div class="form-group">
				<label for="rolecode" class="col-md-2 control-label">前端状态：</label>
				<div class="col-md-4">
					<input type="hidden" name="id" value="${apData.id }">
					<input type="hidden" name="permissionname" value="${apData.permissionname }">
					<input type="hidden" id="agentid" value="${apData.agentid }">
					<select name="frontstate" class="form-control"> 
					<#if apData.frontstate=='1'>
						<option value="1" selected="selected">启用</option>
						<option value="0">停用</option> 
					<#elseif apData.frontstate=='0'>
						<option value="1">启用</option>
						<option value="0" selected="selected">停用</option> 
					</#if>
					</select>
				</div>
				<label for="rolename" class="col-md-2 control-label">功能状态：</label>
				<div class="col-md-4">
					<select name="functionstate" class="form-control"> 
					<#if apData.functionstate=='1'>
						<option value="1" selected="selected">启用</option>
						<option value="0">停用</option> 
					<#elseif apData.functionstate=='0'>
						<option value="1">启用</option>
						<option value="0" selected="selected">停用</option> 
					</#if>
					</select>
				</div>
			</div>
			<div class="form-group">
				<label for="branchinnerCode" class="col-md-2 control-label">有效时间起：</label>
				<div class="col-md-4">
					<#if apData.validtimestart?? > 
						<input width="100%" type="text" class="form-control date form_datetime" value="${apData.validtimestart?string('yyyy-MM-dd HH:mm:ss')}"
						name="validtimestartstr" onfocus="this.blur()"> 
					<#else> 
						<input width="100%" type="text" class=" form-control date form_datetime" onfocus="this.blur()" name="validtimestartstr">
					</#if>
				</div>
				<label for="status" class="col-md-2 control-label">有效时间止：</label>
				<div class="col-md-4">
					<#if apData.validtimeend?? > 
						<input width="100%" type="text" class=" form-control date form_datetime"
						value="${apData.validtimeend?string('yyyy-MM-dd HH:mm:ss')}"
						name="validtimeendstr" onfocus="this.blur()"> 
					<#else> 
						<input width="100%" type="text" class=" form-control date form_datetime"
						name="validtimeendstr" onfocus="this.blur()"> 
					</#if>
				</div>
			</div>
			<div class="form-group">
				<label for="branchinnerCode" class="col-md-2 control-label">关闭方式：</label>
				<div class="col-md-4">
					<select name="abort" class="form-control"> 
					<#if apData.abort=='1'>
						<option selected="selected" value="1">启用</option>
						<option value="0">停用</option> 
					<#elseif apData.abort=='0'>
						<option value="1">启用</option>
						<option selected="selected" value="0">停用</option> 
					</#if>
					</select>
				</div>
				<label for="branchinnerCode" class="col-md-2 control-label">试用次数：</label>
				<div class="col-md-4">
					<input class="form-control" type="text" id="trycount" name="num" value="${apData.num }"/>
				</div>
			</div>
			<div class="form-group">
						<label for="branchinnerCode" class="col-md-2 control-label">备注：</label>
						<div class="col-md-10">
							<textarea name="noti" class="form-control" rows="3">${role.noti!''}</textarea>
						</div>
					</div>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" id="delete_permission" class="btn btn-primary" >取消绑定</button>
		<button type="button" id="save_permisssion" class="btn btn-primary">绑定权限</button>
	</div>
</form>