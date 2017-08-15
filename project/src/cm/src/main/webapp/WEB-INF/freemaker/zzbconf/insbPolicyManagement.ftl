<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>保单管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/insbPolicyManagement" ]);
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
						<label for="exampleInputCode">保单号码</label> <input type="text"
							class="form-control m-left-5" id="policyid" name="policyid" placeholder="">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">订单号码</label> <input type="text"
							class="form-control m-left-5" id="orderid" name="orderid" placeholder="">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputName">车&nbsp;牌&nbsp;号&nbsp;码&nbsp;</label> <input type="text"
							class="form-control m-left-5" id="carnum" name="carnum" placeholder="">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputOrgName">客户姓名</label> <input type="text"
							class="form-control m-left-5" id="custname" name="custname" placeholder="">
					</div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputOrgName">工　　号</label> <input type="text"
                            class="form-control m-left-5" id="agentnum" name="agentnum" placeholder="">
                    </div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputOrgName">代理人姓名</label> <input type="text"
                            class="form-control m-left-5" id="agentname" name="agentname" placeholder="">
                    </div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">所属机构</label> 
						 <input type="hidden" id="deptid" name="deptid" >
						 <input type="text"	class="form-control m-left-5" id="deptname" name="deptname" placeholder="请选择" readonly="readonly">
						 <a id="checkdept">选择</a>
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputName">供&nbsp;&nbsp;应&nbsp;&nbsp;商</label> 
						<input type="hidden" id="providerid" name="providerid" >
						<input type="text"	class="form-control m-left-5" id="providername" name="providername" placeholder="请选择" readonly="readonly">
						 <a id="checkprovider">选择</a>
					</div>
					<div class="form-group form-inline col-md-4" style="display:none">
						<label for="exampleInputOrgName">保&nbsp;单&nbsp;状&nbsp;态&nbsp;</label> 
						<select class="form-control m-left-5" id="policystatus" name="policystatus">
						  <option value="">全部</option>
						  <#list codeList as code>
						  	<option value="${code.codevalue }">${code.codename }</option>
						  </#list>
						</select>
					</div>
					<div class="form-group form-inline col-md-12" style="display:none">
						<label for="exampleInputCode">任务创建时间</label> 
						<input type="text"	class="form-control form_datetime" id="startdate" name="startdate" readonly placeholder="" > -
						<input type="text"	class="form-control form_datetime" id="enddate" name="enddate" readonly placeholder="" >
					</div>
				</form>
			</div>
		</div>
		  </div>
		  <div class="panel-footer">
						<button id="querybutton" type="button" name="querybutton"
											class="btn btn-primary">查询</button>
						<button id="querydetail" type="button" name="querydetail"
											class="btn btn-primary">查看详细</button>
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
			<form hidden="true" name="detailinfo" id="detailinfo" action="querydetail" method="POST">
				<input type="text" name="id" id="id" value="123"/>
				<input type="text" name="risktype" id="risktype" value="0"/>
			</form>
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
	
	<div id="showdept" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="gridSystemModalLabel">选择机构</h4>
      </div>
      <div class="modal-body" style="overflow: auto;height:390px;">
        <div class="container-fluid">
          <div class="row">
			<ul id="depttree" class="ztree"></ul>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
    </div>
  </div>
</div>

<div id="showprovider" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="gridSystemModalLabel">选择供应商</h4>
      </div>
      <div class="modal-body" style="overflow: auto;height:390px;">
        <div class="container-fluid">
          <div class="row">
			<ul id="providertree" class="ztree"></ul>
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
