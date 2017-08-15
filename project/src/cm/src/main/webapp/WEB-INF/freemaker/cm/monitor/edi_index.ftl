<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "cm/monitor/monitor_edi" ]);
</script>
<body> 
	<input type="hidden" id="quotetype" name="quotetype" value=${quotetype}>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">EDI查询</div>
		  <div class="panel-body">
		    <div class="row">
			<div class="col-md-12">
				<form  role="form" id="userform">
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputCode">EDI名称</label> <input type="text"
							class="form-control m-left-5" id="name" name="name" placeholder="">
					</div>
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputOrgName">所属机构</label> 
						<input class="form-control  m-left-5" type="hidden" id="orgcode" name="orgcode">
						<input class="form-control  m-left-5" type="text" id="orgname">
					</div>
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputName">健康状态</label>
						<select name="from" class="form-control  m-left-5" id="status">	
							<option  value="" selected = "selected">请选择</option>								
						  	<option value="0">健康</option>
						  	<option value="1">异常</option>	  																		  		
						</select>
					</div>
						<button id="querybutton" type="button" name="querybutton"
											class="btn btn-primary">查询</button>
						<button id="resetbutton" type="button" name="resetbutton"
											class="btn btn-primary">重置</button>
				</form>
			</div>
		</div>
		  </div>
		</div>
		<div class="panel panel-default">
		  <div class="panel-heading padding-5-5">
		  <div class="row">
				<div class="col-md-2">
					EDI列表
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
	<div class="modal fade" id="myModal_dept" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content">
				<div class="modal-body"  style="overflow: auto; height: 400px;">
					<div id="dept_tree" class="ztree"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
