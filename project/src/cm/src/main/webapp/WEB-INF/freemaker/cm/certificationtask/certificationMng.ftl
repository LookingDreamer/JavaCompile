<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>车险任务管理</title>
	<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>
	<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
	<script type="text/javascript">
			requirejs([ "cm/certificationtask/certificationMng" ]);
	</script>
	<style type="text/css">
		div#selectStype a {
			position:relative;
			left:15px;
			top:10px;
		}
	</style>
</head>
<body>
	<div class="container-fluid" style="margin-bottom:30px">
		<div class="panel panel-default m-bottom-5" id="superquerypanel">
		  <div class="panel-heading padding-5-5">
		  	<label>筛选任务</label>
		  </div>
		  	<div class="panel-body">
				<form role="form" id="cartasksuperqueryform">
					<div class="row">
						<div class="col-md-12">
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputCarlicenseno">姓名:</label> <input type="text" class="form-control m-left-2"
									id="agentName" name="agentName" placeholder=""/>
							</div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInpuTaskstatus">证件号:</label>
								<input type="text" class="form-control" id="idno" name="idno" placeholder=""/>
							</div>
                            <div class="form-group col-md-4 form-inline">
                                <label for="exampleInputOrgName">任务状态:</label>
                                <select class="form-control" id="status" name="status" style="width:150px;">
									<!-- 认证状态  0-未认证  1-认证中  3-认证通过  2-认证失败-->
                                    <option value="0">认证中</option>
                                    <option value="1">认证成功</option>
                                    <option value="3">认证失败</option>
                                    <option value="2">退回修改</option>
                                </select>
                            </div>
						</div>
					</div>				
					<div class="row">
						<div class="col-md-12">
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputInsuredname">电话:</label>
								<input type="text" class="form-control m-left-2" id="agentphone" name="agentphone" placeholder=""/>
							</div>
                            <div class="form-group col-md-4 form-inline">
                                <label for="exampleInputOrgName">注册区域:</label>
                                <select name="province" id="province" class="form-control" style="width:150px;" onchange="changeprv()">
                                    <option value="">请选择</option>
								<#list provinces as uitem>
                                    <option value="${uitem.id}">
									${uitem.comcodename}
                                    </option>
								</#list>
                                </select>
                                <select class="form-control" id="city" name="city" style="width:150px;">
                                    <option value="">请选择</option>
                                </select>
                            </div>
						</div>
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
		  <div class="panel-footer padding-5-5">
		  	<div class="row">
				<div class="col-md-12">
		  			<div class="col-md-12" align="right">
						<button id="superquerybutton" type="button" name="querybutton"
											class="btn btn-primary">查询</button>
						<button id="resetbutton" type="button" name="resetbutton"
											class="btn btn-primary">重置</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="panel panel-default">
		  <div class="panel-heading padding-5-5">
			  <div class="row">
				 <div class="col-md-2">
				 	<label>任务列表</label>
				 </div>
			  </div>
		  </div>
	  	  <div class="row">
			  <div class="col-md-12">
				  <table id="table-javascript"></table>
			  </div>
	  	  </div>
		</div>
		<form class="form-inline" role="form" id="cartaskdispatchform">
			<div class="panel panel-default m-bottom-5">
			  <div class="panel-heading padding-5-5">
				  <div class="row">
					 <div class="col-md-2 active">
					 	<label>处理：</label>
					 </div>
				  </div>
			  </div>
			  <div class="panel-body">
				  <div class="row">
					 <div class="col-md-12">
					 	<!--<select name="personname" id="personname" class="form-control">
							<option value="">请选择</option>
							<#list uList as uitem>
								<option value="${uitem.usercode}">
									${uitem.name}
								</option>
							</#list>
						</select>-->
						<label>分配给:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
						<select class="form-control" id="personname_select" name="personname_test">
							<option>请选择</option>
						</select>
						<input class="form-control" id="personname" type="text" name="personname"/>
						
 						<!-- <input type="text" class="form-control" id="personname" name="personname" value=""/> -->
						<input id="hiddenText2" type="text" style="display:none"/>
					 </div>
				  </div>
			  </div>
		  	 	<div class="panel-footer padding-5-5">
				  <div class="row">
					 <div class="col-md-12">
						<button class="btn btn-primary" type="button" name="dispatch" id="dispatch" title="分配">&nbsp&nbsp分配&nbsp&nbsp</button>
					 </div>
				  </div>
				</div>
		  	</div>
		 </form>
	</div>
	<!--出单网点选择弹出框-->
	<div id="showDeptTree" class="modal fade"  role="dialog" aria-labelledby="myModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close closeShowDeptTree" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="gridSystemModalLabel">选择出单网点</h4>
	      </div>
	      <div class="modal-body" style="overflow:auto; height:400px;">
	        <div class="container-fluid">
	          <div class="row">
				<ul id="deptTreeDemo" class="ztree"></ul>
	          </div>
	        </div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default closeShowDeptTree">关闭</button>
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
