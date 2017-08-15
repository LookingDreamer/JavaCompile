<style type="text/css">
	table.hovertable {
		font-size:11px;
		color:#333333;
		border-width: 1px;
		border-color: #999999;
		border-collapse: collapse;
		margin-bottom:5px;
		margin-top:5px;
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
	    
	div.yellowdiv{
		background-color:#FFEC8B;
		padding:10px;
		border:2px solid #FFA500;
	}
	table.whiteborder td {
		border-color: white;
	}
	table.orange td {
		border-width:0px;
	}
	table.orange{
		background-color:#FFE4B5;
	}
	table.table-platcarmessage{
		background-color:#F5F5F5;
	}
	table.table-bordered.carmodel{
		background-color:#F5F5F5;
	}
</style>
<script type="text/javascript">
	requirejs([ "cm/manualadjustment/manualRecord" ]);
</script>
<!--引入保单信息公共页面-->
<#include "cm/common/insurancePolicyInfo.ftl" />
<#include "cm/common/commomPlatInfo.ftl" />
<div class="row">
 	<div class="col-md-12">
		<#include "cm/common/proposalNumber2.ftl" />
	</div>
</div>
<!--子流程id-->
<input type="hidden" id="subInstanceId${carInsTaskInfo_index}" name="subInstanceId" value="${carInsTaskInfo.subInstanceId}"/>
<input type="hidden" id="inscomcode${carInsTaskInfo_index}" name="inscomcode" value="${carInsTaskInfo.inscomcode}"/>
<input type="hidden" id="hasbusi" name="hasbusi" value="${carInsTaskInfo.proposalInfo.hasbusi}"/>
<input type="hidden" id="hasstr" name="hasstr" value="${carInsTaskInfo.proposalInfo.hasstr}"/>
<input type="hidden" id="inscomname${carInsTaskInfo_index}" name="inscomname" value="${carInsTaskInfo.carInfo.inscomname}"/>

<div class="task_fixbottom">
	<!-- 备注信息页面引入  -->
	<#include "cm/common/remarksInfo.ftl" />
	<!--按钮行-->
  	<div class="btnsbar">
 		<button class="btn btn-primary adjustment" type="button" name="adjustment" id="adjustment${carInsTaskInfo_index}" title="录单完成" >录单完成</button>
		<input type="checkbox" name="checkbox" id="checkbox${carInsTaskInfo_index}" style="display:none"/><!--&nbsp;同时完成回写&nbsp;&nbsp;&nbsp;-->
 		<button class="btn btn-primary backEdit" type="button" name="backEdit" id="backEdit${carInsTaskInfo_index}" title="退回修改" >退回修改</button>
 		<button class="btn btn-primary refuseInsurance" type="button" name="refuseInsurance" id="refuseInsurance${carInsTaskInfo_index}" title="拒绝承保" >拒绝承保</button>
 		<#if isfeeflag =="1">
 		<button class="btn btn-primary peopleDo" type="button" 
			id="peopleDo${carInsTaskInfo_index}" title="转人工处理">重新报价</button>
		</#if>
 		<#if carInsTaskInfo.isRequote != "0" >
 			<input type="hidden" id="isRequote${carInsTaskInfo_index}" name="isRequote${carInsTaskInfo_index}" value="${carInsTaskInfo.isRequote}"/>
 			<button class="btn btn-primary reQuoteBack" type="button" name="reQuoteBack" id="reQuoteBack${carInsTaskInfo_index}" title="报价回写" >报价回写</button>
 		</#if>
 		<button class="btn btn-primary backTask" type="button" name="backTask" id="backTask${carInsTaskInfo_index}" title="打回任务" >打回任务</button>
 		<a class="btn btn-primary" href="/cm/business/mytask/queryTask">返回我的任务</a>
	</div>
</div>
