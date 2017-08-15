<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "system/user" ]);
</script>
<body> 
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">查询</div>
		  <div class="panel-body">
		    <div class="row">
			<div class="col-md-12">
				<form  role="form" id="userform">
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputCode">用户编码</label> <input type="text"
							class="form-control m-left-5" id="usercode" name="usercode" placeholder="">
					</div>
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputName">用户姓名</label> <input type="text"
							class="form-control m-left-5" id="name" name="name" placeholder="">
					</div>
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputName">角色</label>
						<select name="from" class="form-control  m-left-5" id="rolelist">								
						  	<option  value="" selected = "selected">请选择</option>			  																		  		
						</select>
					</div>
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputOrgName">所属机构</label> 
						<input class="form-control  m-left-5" type="hidden" id="deptid" name="userorganization"> 
						<input class="form-control  m-left-5" type="text" id="deptname">
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
					<div class="col-md-10" align="right">
						<button class="btn btn-primary" type="button" name="turn2edite" id="turn2edite" title="新增" >新增</button>
						<button class="btn btn-primary" type="button" name="resetpwd" id="resetpwd" title="密码重置" >密码重置</button>
						<button class="btn btn-primary" type="button" name="resetuserstatus" id="resetuserstatus" title="用户停用" >用户停用</button>
						<button class="btn btn-primary" type="button" name="resetuserstatus" id="resetuserstatus2on" title="用户启用" >用户启用</button>
						<button class="btn btn-primary" type="button" name="benchdeletebutton" title="批量删除" id="benchdeletebutton">批量删除</button>
						
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
