<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/permissionallotedit" ]);
</script>
<div class="modal-header">
	<button type="button" class="close" id="close_modal">&times;</button>
	<h4 class="modal-title">详细信息</h4>
</div>
<form class="form-horizontal" action="updatepermissionallot" id="permissionallot_form"
	method="post">
	<div class="modal-body">
		<div class="div-height">
			<div class="form-group"> 
<!--				 
				<label for="rolecode" class="col-md-2 control-label">前端状态：</label>
				<div class="col-md-4">
-->				
					<input type="hidden" name="id" value="${allotData.id }">
					<input type="hidden" name="setid" value="${allotData.setid }">
					<input type="hidden" id="permissionname" name="permissionname" value="${allotData.permissionname }">				
					<input type="hidden" name="permissionid" value="${allotData.permissionid }">
<!--					
					<select name="frontstate" class="form-control"> <#if
						allotData.frontstate=='0'>
						<option value="0" selected="selected">停用</option>
						<option value="1">启用</option> <#elseif allotData.frontstate=='1'>
						<option value="0">停用</option>
						<option value="1" selected="selected">启用</option> <#else>
						<option value="0">停用</option>
						<option value="1">启用</option> </#if>
					</select>
				</div>
-->
				<label for="rolename" class="col-md-2 control-label">功能状态：</label>
				<div class="col-md-4">
					<select name="functionstate" class="form-control"> <#if
						allotData.functionstate=='0'>
						<option value="0" selected="selected">停用</option>
						<option value="1">启用</option> <#elseif
						allotData.functionstate=='1'>
						<option value="0">停用</option>
						<option value="1" selected="selected">启用</option> <#else>
						<option value="0">停用</option>
						<option value="1">启用</option> </#if>
					</select>
				</div>
 			</div>  
<!-- 			<div class="form-group" style="display: none;"> -->
<!-- 			<label for="rolecode" class="col-md-2 control-label">网页登录：</label> -->
<!-- 				<div class="col-md-4"> -->
<!-- 					<select name="weblogin" class="form-control">  -->
<!-- 					<#if allotData.weblogin=='0'> -->
<!-- 						<option value="0" selected="selected">停用</option> -->
<!-- 						<option value="1">启用</option>  -->
<!-- 					<#elseif allotData.weblogin=='1'> -->
<!-- 						<option value="0">停用</option> -->
<!-- 						<option value="1" selected="selected">启用</option>  -->
<!-- 					<#else> -->
<!-- 						<option value="0">停用</option> -->
<!-- 						<option value="1">启用</option>  -->
<!-- 					</#if> -->
<!-- 					</select> -->
<!-- 				</div> -->
<!-- 				<label for="rolename" class="col-md-2 control-label">手机登录：</label> -->
<!-- 				<div class="col-md-4"> -->
<!-- 					<select name="phonelogin" class="form-control">  -->
<!-- 					<#if allotData.phonelogin=='0'> -->
<!-- 						<option value="0" selected="selected">停用</option> -->
<!-- 						<option value="1">启用</option>  -->
<!-- 					<#elseif allotData.phonelogin=='1'> -->
<!-- 						<option value="0">停用</option> -->
<!-- 						<option value="1" selected="selected">启用</option>  -->
<!-- 					<#else> -->
<!-- 						<option value="0">停用</option> -->
<!-- 						<option value="1">启用</option>  -->
<!-- 					</#if> -->
<!-- 					</select> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<div class="form-group" style="display: none;"> -->
<!-- 				<label for="rolecode" class="col-md-2 control-label">所有设备登录：</label> -->
<!-- 				<div class="col-md-4"> -->
<!-- 					<select name="deviceslogin" class="form-control">  -->
<!-- 					<#if allotData.deviceslogin=='0'> -->
<!-- 						<option value="0" selected="selected">停用</option> -->
<!-- 						<option value="1">启用</option>  -->
<!-- 					<#elseif allotData.deviceslogin=='1'> -->
<!-- 						<option value="0">停用</option> -->
<!-- 						<option value="1" selected="selected">启用</option>  -->
<!-- 					<#else> -->
<!-- 						<option value="0">停用</option> -->
<!-- 						<option value="1">启用</option>  -->
<!-- 					</#if> -->
<!-- 					</select> -->
<!-- 				</div> -->
<!-- 			</div> -->
 				
<div class="form-group">
<!--			<label for="branchinnerCode" class="col-md-2 control-label">有效时间起：</label>
				<div class="col-md-4">
					<#if allotData.validtimestart?? > <input width="100%" type="text"
						class="form-control date form_datetime"
						value="${allotData.validtimestart?string('yyyy-MM-dd HH:mm:ss')}"
						name="validtimestartstr" onfocus="this.blur()"> <#else> <input width="100%"
						type="text" class=" form-control date form_datetime" onfocus="this.blur()" name="validtimestartstr">
					</#if>
				</div>
				<label for="status" class="col-md-2 control-label">有效时间止：</label>
				<div class="col-md-4">
					<#if allotData.validtimeend?? > 
						<input width="100%" type="text" class=" form-control date form_datetime"
						value="${allotData.validtimeend?string('yyyy-MM-dd HH:mm:ss')}"
						name="validtimeendstr" onfocus="this.blur()"> 
					<#else> 
						<input width="100%" type="text" class=" form-control date form_datetime"
						name="validtimeendstr" onfocus="this.blur()"> 
						<input type="hidden" value="${allotData.permissionname }">
					</#if>
				</div>
			</div>
			<div class="form-group">
			<label for="branchinnerCode" class="col-md-2 control-label">关闭方式：</label>
				<div class="col-md-4">
					<select name="abort" class="form-control"> 
					<#if allotData.abort=='1'>
						<option selected="selected" value="1">启用</option>
						<option value="0">停用</option> 
					<#elseif allotData.abort=='0'>
						<option value="1">启用</option>
						<option selected="selected" value="0">停用</option> 
					<#else>
						<option value="1">启用</option>
						<option value="0">停用</option> 
					</#if>
					</select>
				</div>
-->
				<#if "${allotData.permissionname}" !="支付" &&  "${allotData.permissionname}" !="平台查询">
						<label for="num" class="col-md-2 control-label">试用次数：</label>
							<div class="col-md-4">
								<input id="num" class="form-control" type="text" name="num" value="${allotData.num }" style="ime-mode:Disabled
								onkeyup="value=this.value.replace(/\D+/g,'')">
							</div>
						</div>
				</#if>

			</div>
    <div class="form-group">
		<#if "${allotData.permissionname}" =="车险投保" ||  "${allotData.permissionname}" =="快速续保" ||  "${allotData.permissionname}" =="人工报价" ||  "${allotData.permissionname}" =="提交核保">
			<label for="num" class="col-md-2 control-label">预警次数：</label>
			<div class="col-md-4">
				<input id="warningtimes" class="form-control" type="text" name="warningtimes" value="${allotData.warningtimes }" style="ime-mode:Disabled
												onkeyup="value=this.value.replace(/\D+/g,'')">
			</div>
			</div>
		</#if>
    </div>
<!--
			<div class="form-group">
						<label for="branchinnerCode" class="col-md-2 control-label">备注：</label>
						<div class="col-md-10">
							<textarea name="noti" class="form-control" rows="3">${allotData.noti!''}</textarea>
						</div>
			</div>
-->
	</div>
	<div class="modal-footer">
		<button type="button" id="delete_permissionallot" class="btn btn-primary" >取消绑定</button>
		<button type="button" id="save_permisssionallot" class="btn btn-primary">绑定权限</button>
	</div>
</form>