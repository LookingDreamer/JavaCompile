<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>车险任务管理</title>
	<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>
	<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
	<script type="text/javascript">
			requirejs([ "cm/cartaskmanage/carTaskManage" ]);
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
		<!--<div class="panel panel-default" style="margin-bottom:0px">
		  <div class="panel-heading">
	  		<div class="row">
				<div class="col-md-6">
					<a href="/cm/business/mytask/queryTask">我的任务</a>&nbsp;&nbsp;&nbsp;
					<a href="/cm/business/ordermanage/ordermanagelist">订单管理</a>&nbsp;&nbsp;&nbsp;
					<a href="/cm/business/cartaskmanage/cartaskmanagelist">车险任务管理</a>
				</div>
			</div>
		  </div>
		</div>-->
		<div class="panel panel-default m-bottom-5" id="querypanel">
		  <div class="panel-heading padding-5-5">
		  	<form class="form-inline" role="form" id="cartaskgeneralqueryform">
		  		<div class="row">
					<div class="col-md-6" id="selectStype">
						<a href="javascript:reloadCarTaskInfoByTasktype('');">全部</a>&nbsp;&nbsp;
						<a href="javascript:reloadCarTaskInfoByTasktype('A');">报价</a>&nbsp;&nbsp;
						<a href="javascript:reloadCarTaskInfoByTasktype('B');">核保</a>&nbsp;&nbsp;
						<a href="javascript:reloadCarTaskInfoByTasktype('C');">支付</a>&nbsp;&nbsp;
						<a href="javascript:reloadCarTaskInfoByTasktype('D');">二次支付</a>&nbsp;&nbsp;
						<a href="javascript:reloadCarTaskInfoByTasktype('E');">承保打单</a>&nbsp;&nbsp;
						<a href="javascript:reloadCarTaskInfoByTasktype('F');">配送</a>
					</div>
					<div class="col-md-6" align="right">
						<div class="form-group" style="margin-bottom:0px">
						    <input type="text" class="form-control" placeholder="输入车牌或被保人查询"  
						    	id="carlicensenoOrinsuredname" name="carlicensenoOrinsuredname"/>
						    <input id="hiddenText1" type="text" style="display:none"/>
						    <button id="generalquerybutton" type="button" name="generalquerybutton" 
								class="btn btn-primary">搜索</button>
							<button id="generalresetbutton" type="button" name="generalresetbutton" 
								class="btn btn-primary">重置</button>
					    	<button id="showsuperquerybutton" type="button" name="showsuperquerybutton"
								class="btn btn-primary">高级搜索</button>
						</div>
					</div>
				</div>
			</form>
		  </div>
		</div>
		
		<div class="panel panel-default m-bottom-5" id="superquerypanel" style="display:none">
		  <div class="panel-heading padding-5-5">
		  	<label>筛选任务</label>
		  	<button class="close" type="button" id="superquerypanelclose">&times;</button>
		  </div>
		  	<div class="panel-body">
				<form role="form" id="cartasksuperqueryform">
					<div class="row">
						<div class="col-md-12">
							<div class="form-group col-md-4  form-inline">
								<label for="exampleInputTasktype">任务类型:</label> 
								<select name="tasktype" id="tasktype" class="form-control" style="width:150px;">
									<option value="">全部</option>
									<option value="A">报价</option>
									<option value="B">核保</option>
									<option value="C">支付</option>
									<option value="D">二次支付</option>
									<option value="E">承保打单</option>
									<option value="F">配送</option>
								</select>
							</div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputCarlicenseno">车牌号:</label> <input type="text" class="form-control m-left-2"
									id="carlicenseno" name="carlicenseno" placeholder=""/>
							</div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInpuTaskstatus">代理人姓名:</label> 
								<input type="text" class="form-control" id="agentName" name="agentName" placeholder=""/>
							</div>					
						</div>
					</div>				
					<div class="row">
						<div class="col-md-12">
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputUsertype">用户类型:</label> 
								<select name="usertype" id="usertype" class="form-control" style="width:150px;">
									<option value="">全部</option>
									<option value="2">正式用户</option>
									<option value="1">试用用户</option>
								</select>
							</div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputInsuredname">被保人:</label>
								<input type="text" class="form-control m-left-2" id="insuredname" name="insuredname" placeholder=""/>
							</div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInpuTaskstatus">代理人工号:</label> 
								<input type="text" class="form-control" id="agentNum" name="agentNum" placeholder=""/>
							</div>
						</div>
					</div>				
					<div class="row">
						<div class="col-md-12">
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInpuTaskstatus">分配状态:</label>
								<select name="taskstatus" id="taskstatus" class="form-control" style="width:150px;">
									<option value="">全部</option>
									<option value="01">组分配中</option>
									<option value="02">人分配中</option>
									<option value="03">已分配到人</option>
								</select>
							</div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputInsuredname">处理人:</label>
								<input type="text" class="form-control m-left-2" id="operatorname" name="operatorname" placeholder=""/>
							</div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputmainInstanceId">任务跟踪号:</label> 
								<input type="text"  class="form-control" id="mainInstanceId" name="mainInstanceId" placeholder=""/>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group form-inline col-md-4">
								<label>出单网点:</label> 
						     	<input type="hidden" id="deptcode" name="deptcode" value=""/>
						        <input type="text"	class="form-control"  id="deptname"  value=""/>
							</div>
							<div class="form-group form-inline col-md-4">
								<label>保险公司:&nbsp;&nbsp;&nbsp;&nbsp;</label> 
						     	<input type="hidden" id="inscomcode" name="inscomcode" value=""/>
						        <input type="text" class="form-control" id="inscomname" value=""/>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group col-md-8 form-inline">
								<label for="exampleInputTaskcreatetime">任务创建时间:</label>
								<div style=""
									class="input-group date form_datetime col-sm-4"
									data-date-format="yyyy-mm-dd" data-link-field="taskcreatetimeup">
									<input class="form-control" style="color:black;" type="text" id="taskcreatetimeupTxt" readonly value=""/>
									<span class="input-group-addon"> 
										<span class="glyphicon glyphicon-remove"></span>
									</span> 
									<span class="input-group-addon"> 
										<span class="glyphicon glyphicon-th"></span>
									</span>
								</div>
								<input type="hidden" id="taskcreatetimeup" name="taskcreatetimeup" value=""/> 
								
								<label for="exampleInputTaskcreatetime">至</label>
								<div style=""
									class="input-group date form_datetime col-sm-4"
									data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="taskcreatetimedown">
									<input class="form-control" style="color:black;" type="text" id="taskcreatetimedownTxt" readonly value=""/>
									<span class="input-group-addon"> 
										<span class="glyphicon glyphicon-remove"></span>
									</span> 
									<span class="input-group-addon"> 
										<span class="glyphicon glyphicon-th"></span>
									</span>
								</div>
								<input type="hidden" id="taskcreatetimedown" name="taskcreatetimedown" value=""/> 
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
		  	 	<div class="panel-footer padding-5-5">
				  <div class="row">
					 <div class="col-md-12">
						<button class="btn btn-primary" type="button" name="showWorkflowTrack" id="showWorkflowTrack" title="查看流程轨迹">查看流程轨迹</button>
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
