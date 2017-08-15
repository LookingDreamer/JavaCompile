<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>错误信息查询</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "system/closstask" ]); 
</script>  
<body> 
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">查询</div>
		  <div class="panel-body">
		    <div class="row"> 
			<div class="col-md-12">
				<form role="form" id="userform">					
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputName">保险公司：</label>
						<input type="hidden" id="inscomcode" name="inscomcode" value=""/>
					        <input type="text" class="form-control" id="inscomname" value=""/>
					</div>					
					<div class="form-group col-md-5 form-inline">
						<label for="exampleInputCode">任务跟踪号</label> <input type="text"
							class="form-control" id="taskid" name="taskid" placeholder="">
					</div>
					
					<div class="row">
						<div class="col-md-12">
							<div class="form-group col-md-8 form-inline">
								<label for="exampleInputTaskcreatetime">任务创建时间:</label>
									<input class="form-control form_datetime" style="color:black;" type="text" id="taskcreatetimeup" name="taskcreatetimeup" readonly value=""/>
								<label for="exampleInputTaskcreatetime">至</label>
									<input class="form-control form_datetime" style="color:black;" type="text" id="taskcreatetimedown" name="taskcreatetimedown" readonly value=""/>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
		  </div>
		  <div class="panel-footer">
						<button id="querybutton" type="button" name="querybutton"
											class="btn btn-primary">查询</button>
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
					<table id="table-javascript"></table>
				</div>
		  </div>
		</div>
	</div>
	<!--供应商选择弹出框-->
	<div id="showpic" class="modal fade"  role="dialog" aria-labelledby="myModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close closeshowpic" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="gridSystemModalLabel_1">选择供应商</h4>
	      </div>
	      <div class="modal-body" style="overflow:auto; height:400px;">
	        <div class="container-fluid">
	          <div class="row">
				<ul id="treeDemo" class="ztree"></ul>
	          </div>
	        </div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default closeshowpic">关闭</button>
	      </div>
	    </div>
	  </div>
	</div>
</body>
</html>
