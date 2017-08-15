<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>订单管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "cm/ordermanage/orderManage","chn/channelTree"]);
</script>

<style type="text/css">
table {
	margin-top: 5px;
	border: 2px solid gray;
	width: 100%;
}

td {
	padding: 2px;
	border: 1px solid #bbbbbb;
	/* height: 50px; */
	width: 50%;
}

.graybg1 {
	background-color: #cccccc;
}

.graybg2 {
	background-color: #eeeeee;
}

.innertable {
	border: none;
	margin: 0px;
}

.innertable td {
	border: none;
	height: 100%;
}

.padding-6 {
	padding: 6px;
}


.float_right{
	float: right;
}

.zdy-row{
	display: -webkit-box;
    display: -webkit-flex;
    display: flex;
}
.zdy-cell{
	-webkit-box-flex: 1;
    -webkit-flex: 1;
    flex: 1;
    min-width: 0;
}
.zdy-row-ver{

    -webkit-box-align: center;
    -webkit-align-items: center;
    align-items: center;
}

</style>

</head>

<body>
	<div class="panel panel-default m-bottom-5">
		<!--<div class="panel-heading padding-5-5">
			&nbsp;<a href="/cm/business/mytask/queryTask">我的任务</a> &nbsp;&nbsp;
			<a href="/cm/business/ordermanage/ordermanagelist">订单管理</a> &nbsp;
			<a href="/cm/business/cartaskmanage/cartaskmanagelist">车险任务管理</a>&nbsp;&nbsp;&nbsp;
		</div>-->
		<div class="panel-body">
		
			<div class="panel panel-default m-bottom-5" id="querypanel" style="display:none;">
				<div class="panel-heading padding-5-5">
					<form class="form-inline" role="form" id="cartaskmanageform">
						<div class="row">
							<div class="col-md-12">
								<div class="col-md-6 padding-6">
									<a href="javascript:reloadCarTaskInfoByTasktype('');">全部</a>&nbsp;&nbsp;
									<a href="javascript:reloadCarTaskInfoByTasktype('01');">待支付</a>&nbsp;&nbsp;
									<a href="javascript:reloadCarTaskInfoByTasktype('02');">待承保打单</a>&nbsp;&nbsp;
									<a href="javascript:reloadCarTaskInfoByTasktype('03');">待配送</a>&nbsp;&nbsp;
									<a href="javascript:reloadCarTaskInfoByTasktype('04');">认证任务</a>&nbsp;&nbsp;
								</div>
								<div class="col-md-6" align="right">
									<div class="form-group" style="margin-bottom:0px">
                                        <span id="deliveryTypeWrapper" style="display: none">
                                            <label>配送方式</label>&nbsp;&nbsp;
                                            <select name="deliveryType" id="deliveryType" class="form-control" >
                                                <option value="">全部</option>
                                                <option value="0">自取</option>
                                                <option value="1">快递</option>
                                                <option value="3">电子保单</option>
                                            </select>
                                        </span>
										<input id="carlicensenoOrinsuredname" type="text" class="form-control" 
											 name="carlicensenoOrinsuredname" placeholder="输入车牌或跟踪号查询">
										<input id="hiddenText1" type="text" style="display:none"/>
										<button id="generalquerybutton" type="button" class="btn btn-primary"
											name="generalquerybutton">搜索</button>
										<button id="generalrefreshBtn1" type="button" name="generalrefreshBtn1" 
											class="btn btn-primary generalrefreshBtn">刷新</button>
										<button id="generalresetbutton" type="button" name="generalresetbutton" 
											class="btn btn-primary">重置</button>
										<button id="showsuperquerybutton" type="button" class="btn btn-primary">高级搜索</button>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
			
			<!-- 高级搜索hidden="true"-->
			<div class="panel panel-default m-bottom-5" id="superquerypanel">
				<div class="panel-heading padding-5-5">
					<label>高级搜索</label>
                    <!--<button class="close" type="button" id="superquerypanelclose">&times;</button>-->
				</div>
				<div class="panel-body">
					<div class="row">

						<div class="form-group form-inline col-md-4 zdy-row">
							<label class="zdy-row zdy-row-ver">任务类型:</label> 
							<select name="tasktype" class="form-control" id="tasktype" style="width:150px;">
								<!--<option value="">全部</option>-->
								<option value="01">待支付</option>
								<option value="02">待承保打单</option>
								<option value="03">待配送</option>
								<option value="04">认证任务</option>
							</select>
							<label class="need2delivery zdy-row zdy-row-ver" style="display:none">&nbsp;&nbsp;&nbsp;配送方式:</label>
							<select name="deliveryType" class="form-control need2delivery" id="deliType" style="width:auto;display:none">
								<option value="">全部</option>
                                <option value="0">自取</option>
                                <option value="1">快递</option>
							</select>

						</div>
						<div class="form-group form-inline col-md-4">
							<label for="exampleInputCode">被保人:</label> 
							<input type="text" class="form-control m-left-2" id="insuredname" name="insuredname"/>
						</div>
						<div class="form-group form-inline col-md-4">
							<label>代理人姓名:</label> 
							<input type="text" class="form-control" id="agentName" name="agentName"/>
						</div>
					</div>
					<div class="row">
						<div class="form-group form-inline col-md-4" style="display:none">
							<label for="exampleInputCode">用户类型:</label> 
							<select	class="form-control" name="usertype" id="usertype" style="width:150px;">
								<option value="">全部</option>
								<option value="2">正式用户</option>
								<option value="1">试用用户</option>
							</select>
						</div>
						<div class="form-group form-inline col-md-4">
							<label>保险公司:</label> 
					     	<input type="hidden" id="inscomcode" name="inscomcode" value=""/>
					        <input type="text"	class="form-control"  id="inscomname"  value=""/>
						</div>
						<div class="form-group form-inline col-md-4">
							<label for="exampleInputCode">车牌号:</label> 
							<input type="text" class="form-control m-left-2" id="carlicenseno" name="carlicenseno"/>
						</div>
						<div class="form-group form-inline col-md-4">
							<label>代理人工号:</label> 
							<input type="text" class="form-control" id="agentNum" name="agentNum"/>
						</div>
					</div>
					<div class="row">
						
						<div class="form-group form-inline col-md-4">
							<label>联系电话:</label> 
							<input type="text" class="form-control m-left-1" id="agentphone" name="agentphone"/>
						</div>
						<div class="form-group form-inline col-md-4">
							<label for="exampleInputCode">任务跟踪号:&nbsp;</label> 
							<input type="text" class="form-control" id="maininstanceid" name="maininstanceid"/>
						</div>
						<div class="form-group form-inline col-md-4">
							<label>出单网点:&nbsp;&nbsp;&nbsp;&nbsp;</label> 
						    <input type="hidden" id="deptcode" name="deptcode" value=""/>
						    <input type="text"	class="form-control"  id="deptname"  value=""/>
						</div>
					</div>	
					<div class="row">
                        <div class="form-group form-inline col-md-4">
                            <label>渠道来源:</label>
                            <input type="hidden" id="channelinnercode"  value=""/>
                            <input type="text" class="form-control" id="channelName" value="" readonly="readonly"/>
                        </div>
						<div class="form-group form-inline col-md-4">
							<label>任务创建时间:</label>
							<div style=""
								class="input-group date col-sm-4"
								data-date-format="yyyy-mm-dd " data-link-field="taskcreatetimeup">
								<input class="form-control form_datetime" style="color:black;" type="text" id="taskcreatetimeupTxt" readonly value=""/>
								<!--<span class="input-group-addon"> 
									<span class="glyphicon glyphicon-remove"></span>
								</span> 
								<span class="input-group-addon"> 
									<span class="glyphicon glyphicon-th"></span>
								</span>-->
							</div>
							<input type="hidden" id="taskcreatetimeup" name="taskcreatetimeup" value=""/> 
							
							<label>至</label>
							<div style=""
								class="input-group date col-sm-4"
								data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="taskcreatetimedown">
								<input class="form-control form_datetime" style="color:black;" type="text" id="taskcreatetimedownTxt" readonly value=""/>
								<!--<span class="input-group-addon"> 
									<span class="glyphicon glyphicon-remove"></span>
								</span> 
								<span class="input-group-addon"> 
									<span class="glyphicon glyphicon-th"></span>
								</span>-->
							</div>
							<input type="hidden" id="taskcreatetimedown" name="taskcreatetimedown" value=""/> 
						</div>
							<div class="form-group form-inline col-sm-4" name="payment">
								<input type="checkbox" id="paymentMethod" name="paymentMethod" value="4">
								<label>支付方式：柜台支付</label>
							</div>
					</div>
				</div>
				<div class="panel-footer padding-5-5">
					<div class="row">
						<div class="col-md-12">
				  			<div class="col-md-12" id="bomBoot" align="right">
								<button id="superquerybutton" type="button" name="querybutton" class="btn btn-primary">查询</button>
								<button id="generalrefreshBtn2" type="button" name="generalrefreshBtn2" class="btn btn-primary generalrefreshBtn">刷新</button>
								<button id="resetbutton" type="button" name="resetbutton" class="btn btn-primary">重置</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- 待处理任务 -->
			<div>
				<div id="myOrderManage" class="tasksview">
					<!-- 任务列表 -->
					<div class="panel panel-default m-bottom-5" id="myOrderManageResultcontent">
						<div class="panel-body" id="myOrderManageResultList">
							<!--正在努力的加载数据中！请稍后…………-->
						</div>
					</div>
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
	        <h4 class="modal-title" id="gridSystemModalLabel">选择供应商</h4>
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
	<br/>
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
    <!--渠道来源选择弹出框-->
	<#include "chn/channelTree.ftl"/>
</body>
</html>
