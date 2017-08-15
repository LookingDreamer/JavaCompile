<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>第三方支付管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/bankcard" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default">
		  <div class="panel-heading padding-5-5">
			 <div class="row">
				<div class="col-md-3">
					<#-- <input type="hidden" id="bankcardid"  > -->
				 	<#-- <input type="text"	class="form-control " style="width:100%;" id="bankname" placeholder="请选择发卡银行" > -->
				 	<select class="form-control m-left-5" id="banktonameselect" name="banktonameselect">
						     <option value="">请选择发卡银行</option>
						   <#list banknamelist as row>
						     <option value="${row.banktoname }">${row.banktoname }</option>
						   </#list>
					</select>
				</div>
				<div class="col-md-3" align="left">
					<input id="queryByBanktoname" type="button" class="btn btn-primary" value="查询">
					<input id="refresh" type="button" class="btn btn-primary" value="刷新">
				</div>
				<div class="col-md-6" align="right">
					<button class="btn btn-primary" type="button"  id="addbankcard" title="新增" >添加银行卡信息</button>
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
