
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h4 class="modal-title">权限信息</h4>
		</div>
		<form class="form-horizontal" action="updatgroupmemberprivilege" method="post">
		<div class="modal-body">
			<div class="div-height">
			<div class="row">
				<table>
					<tr>
						<td class="col-md-6" align="right">已选择业管：</td>
						<td class="col-md-6">
							<input type="hidden" name="id" value="${privalege.id }">
							<input type="hidden" name="groupId" value="${privalege.groupid }">
							${user.name }</td>
					</tr>
					<tr>
						<td class="col-md-6" align="right">任务权限：</td>
						<td class="col-md-6">
							<#if privalege.groupprivilege==1>
								<input type="checkbox" value="1" name="groupprivilege" checked="checked" disabled="disabled">查看&nbsp;&nbsp;&nbsp;
								<input type="checkbox" value="2" name="groupprivilege">执行
							<#elseif privalege.groupprivilege==2>
								<input type="checkbox" value="1" name="groupprivilege" checked="checked" disabled="disabled" >查看&nbsp;&nbsp;&nbsp;
								<input type="checkbox" value="2" name="groupprivilege" checked="checked">执行
							<#else>
								<input type="checkbox" value="1" name="groupprivilege"  checked="checked" disabled="disabled">查看&nbsp;&nbsp;&nbsp;
								<input type="checkbox" value="2" name="groupprivilege" checked="checked">执行
							</#if></td>
					</tr>
				</table>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			<button type="submit" class="btn btn-primary">保存</button>
		</div>
		</form>