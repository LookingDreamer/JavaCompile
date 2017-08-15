<div class="container-fluid">
	<div class="row">
		<form id="classifyform">
			<div id="inputfrom" class="input-group-sm">
				<table  class="table">
					<tr>
						<td><label for="exampleInputCode">大地加密狗密钥：</label></td>
					    <td><input type="text" class="form-control m-left-5" id="dadikey" name="dadikey" placeholder=""></td>
					    <input type="hidden" class="form-control m-left-5" id="taskid" name="taskid" placeholder="" value=${taskid!''}>
					    <input type="hidden" class="form-control m-left-5" id="inscomcode" name="inscomcode" placeholder="" value="${inscomcode!''}">
					</tr>
				</table>
			</div>
		</form>
		<div class="modal-footer">
			<a id="savedadikey" class="btn btn-success">保存</a>
			<a id="closebutton" data-dismiss="modal" name="closebutton" class="btn btn-success">关闭</a>
		</div>
	</div>
</div>
<script type="text/javascript">
	requirejs([ "system/dadi"]);
</script>
