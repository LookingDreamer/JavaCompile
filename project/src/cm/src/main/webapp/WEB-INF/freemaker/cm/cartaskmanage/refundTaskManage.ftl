<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>车险任务管理</title>
	<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>
	<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
	<script type="text/javascript">
			requirejs([ "cm/cartaskmanage/refundTaskManage" ]);
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
              <!--<button class="close" type="button" id="superquerypanelclose">&times;</button>-->
		  </div>
		  	<div class="panel-body">
				<form role="form" id="cartasksuperqueryform">
					<div class="row">
						<div class="col-md-12">
							<div class="form-group col-md-4  form-inline">
								<label for="exampleInputTasktype">支付流水号:</label>
                                <input type="text" class="form-control" id="paymentransaction" name="paymentransaction" placeholder=""/>
							</div>
							<div class="form-group col-md-4 form-inline">
                                <label for="exampleInpuTaskstatus">任务号:</label>
                                <input type="text" class="form-control m-left-3" id="taskid" name="taskid" placeholder=""/>
							</div>
							<div class="form-group col-md-4 form-inline">
                                <label for="exampleInputCarlicenseno">车牌号码:</label>
								<input type="text" class="form-control " id="carlicenseno" name="carlicenseno" placeholder=""/>

							</div>					
						</div>
					</div>				
					<div class="row">
						<div class="col-md-12">
                            <div class="form-group form-inline col-md-4">
                                <label>渠道来源:</label>
                                <input type="hidden" id="channelinnercode"  value=""/>
                                <input type="text" class="form-control m-left-5" id="channelName" value="" readonly="readonly"/>
                            </div>
                            <div class="form-group form-inline col-md-4">
                                <label>出单网点:</label>
                                <input type="hidden" id="deptcode" name="deptcode" value=""/>
                                <input type="text"	class="form-control m-left-2"  id="deptname"  value="" />
                            </div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputOrgName">任务状态:</label> 
								<select class="form-control" id="refundstatus" name="refundstatus" style="width:150px;">
						 			<option value="0">未完成</option>
									<option value="1">已完成</option>
								</select>
							</div>
						</div>
					</div>				
					<div class="row">
						<div class="col-md-12">
                            <div class="form-group col-md-4 form-inline">
                                <label for="exampleInputInsuredname">支付金额:</label>
                                <input type="text" class="form-control m-left-5" id="payamount" name="payamount" placeholder=""/>元
                            </div>

							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputInsuredname">退款金额:</label>
								<input type="text" class="form-control m-left-2" id="refundamount" name="refundamount" placeholder=""/>元
							</div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputmainInstanceId">退款类型:</label>
                                <select class="form-control" id="refundtype" name="refundtype" style="width:150px;">
                                    <option value="01">全额退款</option>
                                    <option value="02">差额退款</option>
                                </select>
							</div>
						</div>
					</div>

                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group col-md-8 form-inline">
                                <label for="exampleInputTaskcreatetime">支付时间:</label>
                                <input class="form-control form_datetime m-left-5" style="color:black;" type="text" id="taskcreatetimeup" name="taskcreatetimeup" readonly value=""/>
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
