<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "system/fileupload" ]);
</script>
<div class="modal-header">
      <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
      <span class="modal-title" id="myModalLabel">新建分类</span>
</div>
<div class="container-fluid">
	<div class="row">
		<form id="classifyform">
			<div id="inputfrom" class="input-group-sm">
				<table  class="table">
					<tr>
						<td><label for="exampleInputCode">新建分类名称</label></td>
					    <td><input type="text" class="form-control m-left-5" id="classifyname" name="classifyname" placeholder=""></td>
					    <input type="hidden" id="classify" name="classify" value="${classify!''}">
					</tr>
				</table>
			</div>
		</form>
		<div class="modal-footer">
			<a id="cancle" class="btn btn-danger" data-dismiss="modal">取消</a>
			<a id="saveclassify" class="btn btn-success">保存</a>
		</div>
	</div>
</div>