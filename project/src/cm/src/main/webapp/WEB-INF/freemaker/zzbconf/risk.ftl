<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>险种管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "zzbconf/risk" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">查询</div>
			<div class="panel-body">
				<div class="row">
				<table class="table table-bordered  ">
					<tr>
						<td class="col-md-1" style="vertical-align: middle;" align="right">险种编码</td>
						<td class="col-md-3">
							<input type="text" class="form-control " id="riskcode" name="riskcode" placeholder="">
						</td>
						<td class="col-md-1" style="vertical-align: middle;" align="right">险种名称</td>
						<td class="col-md-3">
							<input type="text"
									class="form-control " id="riskname" name="riskname"
									placeholder="">
						</td>
						<td class="col-md-1" style="vertical-align: middle;" align="right">保险公司</td>
						<td class="col-md-3">
						    <input type="text" class="form-control " id="providename" name="providename">
						    <input type="hidden" class="form-control " id="provideid" name="provideid">
							<#-- <select class="form-control " id="provideid" name="provideid">
                                    <option value="" selected="selected">请选择</option>
								<#list provider as p>
                                    <option value="${p.id}">${p.name}</option>
								</#list>
                                </select> -->
						</td>
					</tr>
					<tr>
						<td class="col-md-1" style="vertical-align: middle;" align="right">险种类型</td>
						<td class="col-md-3">
							<input type="text" class="form-control " id="risktype" name="risktype" value="车险" readonly>
						</td>
						<td class="col-md-1" style="vertical-align: middle;" align="right">险种小类</td>
						<td class="col-md-3">
							<select class="form-control " id="status" name="status">
								 	<option value="" parent="0" selected="selected">请选择</option>
									<#list riskstatus as riskstatuscode>
									<option value="${riskstatuscode.codevalue}"  parent="${riskstatuscode.prop2}" >${riskstatuscode.codename}</option>
									</#list>
								</select>
						</td>
						<td colspan="2"></td>
						</td>
					</tr>
				</table>
			</div>
			<div class="panel-footer">
				<button id="querybutton" type="button" name="querybutton"
					class="btn btn-primary">查询</button>
					<button id="resetbutton" type="button" name="resetbutton"
					class="btn btn-primary">重置</button>
				<button id="addrisk" type="button" name="addrisk"
					class="btn btn-primary">新增</button>
				<button id="updaterisk" type="button" name="updaterisk"
					class="btn btn-primary">修改</button>
				<button id="deleted" type="button" name="deleted"
					class="btn btn-primary">删除</button>
					
				<button class="btn btn-primary" type="button" name="toggle"
					title="Toggle" id="toggle">
					<i class="glyphicon glyphicon-list-alt icon-list-alt"></i>
				</button>
			</div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading padding-5-5">
				<div class="row">
					<div class="col-md-2">结果</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table id="table-javascript"></table>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="showpic" class="modal fade" role="dialog" aria-labelledby="myModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="gridSystemModalLabel">选择供应商</h4>
      </div>
      <div class="modal-body " style="overflow:auto; height:400px;">
        <div class="container-fluid">
          <div class="row">
			<ul id="treeDemo" class="ztree"></ul>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
    </div>
  </div>
</div>
</body>
</html>
