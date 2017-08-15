<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>活动奖品管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/activityprize" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">查询</div>
		  <div class="panel-body">
		    <div class="row">
			<div class="col-md-12">
				<form role="form" id="policymanagent">
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">活动名称</label> 
						<select id="activityid" name="activityid">
						<#list alist as alist>
						 <option value ="${alist.id}">${alist.name}</option>
						</#list>
						</select>
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">奖品名称</label>
						<select id="prizeid" name="prizeid">
						<#list plist as plist>
						 <option value ="${plist.id}">${plist.name}</option>
						</#list>
						</select>
					</div>
				</form>
			</div>
		</div>
		  </div>
		  <div class="panel-footer">
						<button id="querybutton" type="button" name="querybutton"
											class="btn btn-primary">查询</button>
						<button id="save" type="button" name="save"
											class="btn btn-primary">新增</button>
						<button id="edit" type="button" name="edit"
											class="btn btn-primary">编辑</button>
						<button id="delete" type="button" name="delete"
											class="btn btn-primary">删除</button>
						<button id="resetbutton" type="button" name="resetbutton"
											class="btn btn-primary">重置</button>
						<button class="btn btn-primary" type="button" name="refresh"
											title="Refresh" id="refresh">
											<i class="glyphicon glyphicon-refresh icon-refresh"></i>
										</button>
						<button class="btn btn-primary" type="button" name="toggle" title="Toggle" id="toggle">
												<i class="glyphicon glyphicon-list-alt icon-list-alt"></i>
										</button>
			</div>
		</div>
		<div class="panel panel-default">
		  <div class="panel-heading padding-5-5">
		  <div class="row">
				<div class="col-md-2">
					结果
					</div>
			</div>
			</div>
		    <div class="row">
				<div class="col-md-12">
					<table id="table-list"></table>
				</div>
		  </div>
		</div>
	</div>
	<div class="modal fade" id="providersource" tabindex="-1" role="dialog"	aria-labelledby="myModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
					<div class="form-group form-inline col-md-4">
					<input type="hidden" id="id" name="id">
						<label for="exampleInputCode">活动名称</label> 
						<select id="activityid2" name="activityid2">
						<#list alist as alist>
						 <option value ="${alist.id}">${alist.name}</option>
						</#list>
						</select>
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">奖品名称</label>
						<select id="prizeid2" name="prizeid2">
						<#list plist as plist>
						 <option value ="${plist.id}">${plist.name}</option>
						</#list>
						</select>
					</div>
					<div class="modal-body " style="overflow: auto; height: 400px;">
						<button class="btn btn-primary" type="button" id="checkProid" title="showAll">确定</button>
						<button class="btn btn-primary" type="button" id="closeProid">取消</button>
	   				</div>
				</div>
			</div>
		</div>
	</div>
<!--add refreshrefresh-->
</body>
</html>
