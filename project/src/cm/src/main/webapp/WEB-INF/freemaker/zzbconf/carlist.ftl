<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>车辆管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/carmanager" ]);
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
						<label for="exampleInputName">客户姓名</label> <input type="text"
							class="form-control m-left-5" id="ownername" name="ownername" placeholder="">
					</div>
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputOrgName">保单号码</label> <input type="text"
							class="form-control m-left-5" id="policyno" name="policyno" placeholder="">
					</div>
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputOrgName">车辆识别代号</label> <input type="text"
							class="form-control m-left-5" id="vincode" name="vincode" placeholder="">
					</div>
					 <div class="form-group col-md-4 form-inline">
						<label for="exampleInputOrgName">手机号码</label>
						 <input type="text" class="form-control m-left-5" id="phonenumber" name="phonenumber" placeholder="">
					</div>
					<#--<div class="form-group col-md-4 form-inline">
						<label for="exampleInputOrgName">车&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型</label> 
						<select class="form-control m-left-5" id="standardfullname" name="standardfullname" placeholder="请选择">
						     <option value="">请选择</option>
						   <#list carmodellist as row>
						     <option value="${row.carmodelid }">${row.standardfullname }</option>
						   </#list>
						</select>
					</div> -->
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputOrgName">车牌号码</label> <input type="text"
							class="form-control m-left-5" id="carlicenseno" name="carlicenseno" placeholder="">
					</div>
					<div class="form-group col-md-4 form-inline">
						<label for="exampleInputOrgName">发&nbsp;&nbsp;动&nbsp;&nbsp;机&nbsp;&nbsp;号</label>&nbsp;&nbsp;<input type="text"
							class="form-control m-left-5" id="engineno" name="engineno" placeholder="">
					</div>
				</form>
			</div>
		</div>
		  </div>
		  <div class="panel-footer">
						<button id="querybutton" type="button" name=" 	"
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
						<button class="btn btn-primary" type="button" name="carinfo" id="carinfobtn" title="车辆明细" >车辆明细</button>
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
</body>
</html>
