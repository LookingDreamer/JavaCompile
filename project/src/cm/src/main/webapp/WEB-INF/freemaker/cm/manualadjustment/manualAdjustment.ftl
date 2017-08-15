<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>人工调整</title>
	<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>
	<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
	<script type="text/javascript">
		requirejs([ "cm/manualadjustment/manualAdjustment" ]);
	</script>
	<style type="text/css">
		table.hovertable {
			font-size:11px;
			color:#333333;
			border-width: 1px;
			border-color: #999999;
			border-collapse: collapse;
			margin-bottom:5px;
			margin-top:5px;
		}
		table.hovertable tr { 
			background-color:#F5F5F5; 
		} 
		table.hovertable td {
			border-width: 1px;
			padding: 8px;
			border-style: solid;
			border-color: #383838;
		}
		td.bgg{
			background-color:#E5E5E5; 
			text-align:right;
		}
		span.rightPosition {
			position:relative;
			top:12px;
		}
		div.topbotPosition {
			margin-top:4px;
			margin-bottom:4px;
		}
		td.abstract {
			border:1px solid #ccc;
		}
	</style>
</head>
<body>
	<div class="container-fluid" style="margin-bottom:40px">
		<div class="panel panel-default" style="margin-bottom:0px">
		  <div class="panel-heading">
	  		<div class="row">
				<div class="col-md-6">
					<a href="/cm/business/mytask/queryTask">我的任务</a>&nbsp;&nbsp;&nbsp;
					<a href="/cm/business/ordermanage/QueryOrdermanagelist">订单管理</a>&nbsp;&nbsp;&nbsp;
					<a href="/cm/business/cartaskmanage/cartaskmanagelist">车险任务管理</a>&nbsp;&nbsp;&nbsp;
					<a href="/cm/business/valetcatalogue/showaletcataloguelist">代客录单</a>&nbsp;&nbsp;&nbsp;
					<a href="">运营数据</a>
				</div>
			</div>
		  </div>
		</div>
		<div class="panel panel-default" style="margin-bottom:0px">
			<div class="panel-heading padding-5-5">
				<div class="row">
				 	<div class="col-md-12">
						<h3 style="margin-top:5px;margin-bottom:5px;">任务摘要</h3>
					</div>
			  	</div>
			</div>
			<div class="panel-body" style="padding:0px;">
				<table class="table table-bordered" style="margin-bottom:0px;border-width:0px;">
					<tr>
						<td class="col-md-6 abstract">
							<div class="row">
					 			<div class="col-md-4">
						 			<h4 style="color:#3276b1;">${carInsTaskInfoList[0].carInfo.carlicenseno}</h4>
						 			<input type="hidden" id="taskid" name="taskid" value="${carInsTaskInfoList[0].carInfo.taskid}" />
					 			</div>
					 			<div class="col-md-8">
					 				<span class="rightPosition">被保人：${carInsTaskInfoList[0].relationPersonInfo.insuredname}</span>
					 			</div>
					 		</div>
					 		<div class="row">
					 			<div class="col-md-12 topbotPosition">
					 				<span>
					 					投保公司：
							 			<#list carInsTaskInfoList as carInsTaskInfo>
							 				${carInsTaskInfo.carInfo.inscomname}
								 			<#if carInsTaskInfo_has_next>,</#if>
				  						</#list>
				  					</span>
					 			</div>
					 		</div>
					 		<div class="row">
					 			<div class="col-md-12 topbotPosition">
					 				<span style="color:red">用户备注：
						 				<#if (carInsTaskInfoList[0].remarkinfo.usercomment)??>
						 					${carInsTaskInfoList[0].remarkinfo.usercomment.commentcontent!""}
						 				</#if>
					 				</span>
					 			</div>
					 		</div>
						</td>
						<td class="col-md-6 abstract">
							<div class="row">
					 			<div class="col-md-5">
					 				<h4 style="color:#3276b1;">${agentInfo.jobnum}(${agentInfo.agentname})</h4>
					 			</div>
					 			<div class="col-md-7">
					 				<span class="rightPosition">所属团队： ${agentInfo.team}</span>
					 			</div>
					 		</div>
					 		<div class="row">
					 			<div class="col-md-6 topbotPosition">
					 				<span>电话：${agentInfo.mobile}</span>
					 			</div>
					 			<div class="col-md-6 topbotPosition">
					 				<span>${agentInfo.idnotype}：${agentInfo.idno}</span>
					 			</div>
					 		</div>
					 		<div class="row">
					 			<div class="col-md-6 topbotPosition">
					 				<span>资格证号：${agentInfo.cgfns}</span>
					 			</div>
					 			<div class="col-md-6 topbotPosition">
					 				<span>执业证/展业证号码：${agentInfo.licenseno}</span>
					 			</div>
					 		</div>
						</td>
					</tr>
					<tr>
						<td colspan="2" class="col-md-12 abstract">
							<!--引入工作流程子页面-->
						    <#include "cm/common/workFlowView.ftl"/>
						</td>
					</tr>
				</table>
			</div>
	  	</div>
		<div class="panel panel-default">
			<div class="panel-heading padding-5-5">
				<div class="row">
				 	<div class="col-md-12">
						<h3 style="margin-top:5px;margin-bottom:5px;">任务处理</h3>
					</div>
			  	</div>
			</div>
			<ul id="myTab" class="nav nav-pills" role="tablist" style="margin-bottom:0px";>
			    <#list carInsTaskInfoList as carInsTaskInfo>
		 			<li <#if (carInsTaskInfo_index = 0)>class="active"</#if>>
				    	<a href="#${carInsTaskInfo.inscomcode}" role="tab" data-toggle="pill">
				    		<h4>
								${carInsTaskInfo.carInfo.inscomname}
							</h4>
							${carInsTaskInfo.carInfo.inscomname}${carInsTaskInfo.carInfo.insprovincename}分公司-地面
				    	</a>
				    </li>
	  			</#list>
			</ul>
			<div id="myTabContent" class="tab-content" style="border:1px solid #4F94CD;border-top:5px solid #4F94CD;padding:6px;">
			<input id="Myinstanceid" type="hidden" value="${carInsTaskInfoList[0].carInfo.taskid}" name="instanceId"/>
			    <#list carInsTaskInfoList as carInsTaskInfo>
				    <div class="tab-pane fade <#if (carInsTaskInfo_index = 0)>in active</#if>" id="${carInsTaskInfo.inscomcode}" style="border-width:0px;">
					    <form class="form-inline" role="form" id="insurancePolicyInfoForm${carInsTaskInfo_index}">
					    	<!--引入保单信息公共页面-->
					    	<#include "cm/common/insurancePolicyInfo.ftl" />
						  	<!--引入用户备注公共页面-->
						  	<#include "cm/common/remarksInfo.ftl" />
						  	<div class="row">
							 	<div class="col-md-12">
							 		<button class="mybtn btn passInsurance" type="button" name="adjustment" id="adjustment${carInsTaskInfo_index}" title="调整完成" >调整完成</button>
							 		<button class="mybtn btn backEditInsurance" type="button" name="backEdit" id="backEdit${carInsTaskInfo_index}" title="退回修改" >退回修改</button>
							 		<button class="mybtn btn refuseInsurance" type="button" name="refuseInsurance" id="refuseInsurance${carInsTaskInfo_index}" title="拒绝承保" >拒绝承保</button>
							 		<button class="mybtn btn backTask" type="button" name="backTast" id="backTast${carInsTaskInfo_index}" title="打回任务" >打回任务</button>
							 	</div>
							</div>
						</form>
				    </div>
	  			</#list>
			</div>
		</div>
		
	</div>
</body>
</html>

