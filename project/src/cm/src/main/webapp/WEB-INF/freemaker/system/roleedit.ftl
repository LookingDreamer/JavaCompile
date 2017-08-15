
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h4 class="modal-title">详细信息</h4>
		</div>
		<form class="form-horizontal" action="updaterole" method="post">
		<div class="modal-body">
			<div class="div-height">
				
					<div class="form-group">
						<label for="rolecode" class="col-md-2 control-label">角色编码：</label>
						<div class="col-md-4">
							<input type="hidden" name="id" value="${role.id!''}"> <input
								type="text" value="${role.rolecode!''}" name="rolecode"
								class="form-control" id="rolecode">
						</div>
						<label for="rolename" class="col-md-2 control-label">角色名称：</label>
						<div class="col-md-4">
							<input type="text" value="${role.rolename!''}"
								class="form-control" id="rolename" name="rolename">
						</div>
					</div>
					<div class="form-group">
						<label for="branchinnerCode" class="col-md-2 control-label">机构内部编码：</label>
						<div class="col-md-4">
							<input type="text" value="${role.branchinnerCode!''}"
								name="branchinnerCode" class="form-control" id="branchinnerCode">
						</div>
						<label for="status" class="col-md-2 control-label">机构状态：</label>
						<div class="col-md-4">
							<select name="status" class="form-control"> 
							<#if role.status=='1'>
								<option selected="selected" value="1">启用</option>
								<option value="0">停用</option> 
							<#elseif role.status=='0'>
								<option value="1">启用</option>
								<option selected="selected" value="0">停用</option> 
							<#else>
								<option value="1">启用</option>
								<option value="0">停用</option>
							</#if>
							</select>
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
			<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			<button type="submit" class="btn btn-primary">保存</button>
		</div>
		</form>