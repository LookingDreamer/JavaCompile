<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
requirejs([ "zzbconf/groupuserlist" ]);
</script>
<div class="modal-body">
	<div class="row">
		<div class="col-md-12">
		<form id="user_param">
			<table class="table table-bordered ">
				<tr>
					<td align="right">成员账号：</td>
					<td>
					<input type="hidden" id="groupId" name="groupId" value="${groupId }" >
					<input class="form-control" type="text" id="usercode" name="usercode"></td>
					<td align="right">成员姓名：</td>
					<td><input class="form-control" type="text" id="name"  name="name"></td>
				</tr>
				<tr>
					<td align="right">所属机构：</td>
					<td>
						<input class="form-control " type="hidden" id="deptid"> 
						<input class="form-control " type="text" id="deptname">
					</td>
					<td align="right"></td>
					<td>
<!-- 						<select class="form-control" id="grade" name="grade"> -->
<!-- 							<option value="1">本级及子级机构</option> -->
<!-- 							<option value="2">本级机构</option> -->
<!-- 							<option value="3">子级机构</option> -->
<!-- 						</select> -->
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<input id="querybutton" class="btn btn-primary" type="button" value="查询">
						<input id="resetbutton" type="button" class="btn btn-primary" value="重置">
						
					</td>
				</tr>
			</table>
			</form>
			<table id="group_user_list"></table>
		</div>
	</div>
</div>
<div class="modal fade" id="myModal_dept" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content">
				<div class="modal-body"  style="overflow: auto; height: 200px;">
					<div id="dept_tree" class="ztree"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
<div class="modal-footer">
	<button type="button" class="btn btn-default" id="close_user_modal">关闭</button>
	<button type="button" id="save_user2group" class="btn btn-primary">确认添加</button>
</div>
