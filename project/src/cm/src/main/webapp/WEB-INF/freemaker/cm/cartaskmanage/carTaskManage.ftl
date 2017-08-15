<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>车险任务管理</title>
	<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>
	<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
	<script type="text/javascript">
			requirejs([ "cm/cartaskmanage/carTaskManage","chn/channelTree" ]);
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
		<div class="panel panel-default m-bottom-5" id="querypanel" style="display:none">
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
		
		<div class="panel panel-default m-bottom-5" id="superquerypanel">
		  <div class="panel-heading padding-5-5">
		  	<label>筛选任务</label>
              <!--<button class="close" type="button" id="superquerypanelclose">&times;</button>-->
		  </div>
		  	<div class="panel-body">
				<form role="form" id="cartasksuperqueryform">
					<div class="row">
						<div class="col-md-12">
							<div class="form-group col-md-4  form-inline">
								<label for="exampleInputTasktype">任务类型:</label> 
								<select name="tasktype" id="tasktype" class="form-control" style="width:150px;">
									<option value="">全部</option>
									<#list workFlowNodeList as workFlowNode>
										<option value="${workFlowNode.codevalue}">${workFlowNode.codename}</option>
									</#list>
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
							<div class="form-group col-md-4 form-inline" style="display:none">
								<label for="exampleInputUsertype">用户类型:</label> 
								<select name="usertype" id="usertype" class="form-control" style="width:150px;">
									<option value="">全部</option>
									<option value="2">正式用户</option>
									<option value="1">试用用户</option>
								</select>
							</div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputOrgName">任务状态:</label> 
								<select class="form-control" id="taskstate" name="taskstate" style="width:150px;">
						 			<option value="01">待处理</option>
									<option value="02">已完成</option>
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
							<div class="form-group col-md-4 form-inline" style="display:none">
								<label for="exampleInpuTaskstatus">分配状态:</label>
								<select name="taskstatus" id="taskstatus" class="form-control" style="width:150px;">
									<option value="">全部</option>
									<option value="01">组分配中</option>
									<option value="02">人分配中</option>
									<option value="03">已分配到人</option>
								</select>
							</div>
							<div class="form-group form-inline col-md-4">
								<label>出单网点:</label> 
						     	<input type="hidden" id="deptcode" name="deptcode" value=""/>
						        <input type="text"	class="form-control"  id="deptname"  value=""/>
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
								<label>保险公司:</label> 
						     	<input type="hidden" id="inscomcode" name="inscomcode" value=""/>
						        <input type="text" class="form-control" id="inscomname" value=""/>
							</div>
                            <div class="form-group form-inline col-md-4">
                                <label>渠道来源:</label>
                                <input type="hidden" id="channelinnercode"  value=""/>
                                <input type="text" class="form-control m-left-5" id="channelName" value="" readonly="readonly"/>
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
						<#--<button id="superquerybutton" type="button" name="querybutton"
											class="btn btn-primary">查询</button>-->
                        <button id="singlequery" type="button"
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
						<button class="btn btn-primary" type="button" name="stopdispatch" id="stopdispatch" title="暂停调度">暂停调度</button>
						<button class="btn btn-primary" type="button" name="restartdispatch" id="restartdispatch" title="重启调度">重启调度</button>
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
    <!--渠道来源选择弹出框-->
	<#include "chn/channelTree.ftl"/>
</body>
</html>
