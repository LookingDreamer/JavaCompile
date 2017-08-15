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
	//requirejs([ "cm/manualadjustment/manualUnderWriting" ]);
</script>
<!--引入保单信息公共页面 -->
<#include "cm/common/insurancePolicyInfo.ftl" />
<#include "cm/common/commomPlatInfo.ftl" />
<div class="row">
 	<div class="col-md-12">
		<#include "cm/common/proposalNumber.ftl" />
		<#include "cm/common/paymentInfo.ftl" />
	</div>
</div>
<!--子流程id-->
<input type="hidden" id="subInstanceId${carInsTaskInfo_index}" name="subInstanceId" value="${carInsTaskInfo.subInstanceId}"/>
<input type="hidden" id="inscomcode${carInsTaskInfo_index}" name="inscomcode" value="${carInsTaskInfo.inscomcode}"/>
<input type="hidden" id="hasbusi" name="hasbusi" value="${proposalInfo.hasbusi}"/>
<input type="hidden" id="hasstr" name="hasstr" value="${proposalInfo.hasstr}"/>


<div class="task_fixbottom">
	<!-- 备注信息页面引入-->
	<#include "cm/common/remarksInfo.ftl" />
	<!--按钮行-->
  	<div class="btnsbar">
 		<button class="btn btn-primary adjustment" type="button" name="adjustment" id="adjustment${carInsTaskInfo_index}" title="核保完成" >核保完成</button>
		<button class="btn btn-primary backEdit" type="button" name="backEdit" id="backEdit${carInsTaskInfo_index}" title="退回修改" >退回修改</button>
		<button class="btn btn-primary refuseInsurance" type="button" name="refuseInsurance" id="refuseInsurance${carInsTaskInfo_index}" title="拒绝承保" >拒绝承保</button>
		<button class="btn btn-primary searchMoney" type="button" name="searchMoney" id="searchMoney${carInsTaskInfo_index}" title="查看报价金额" >查看报价金额</button>
		<button class="btn btn-primary backTask" type="button" name="backTask" id="backTask${carInsTaskInfo_index}" title="打回任务" >打回任务</button>
		<#if isReub != "0" >
			<input type="hidden" id="isReub" name="isReub" value="${isReub}"/>
			<button class="btn btn-primary reUnderwriting" type="button" name="reUnderwriting" id="reUnderwriting${carInsTaskInfo_index}" title="核保回写" >核保回写</button>
		</#if>
		<#if isReub != "0" >
			<button class="btn btn-primary loopUnderwriting" type="button" id="loopUnderwriting${carInsTaskInfo_index}" title="核保轮询" >核保轮询</button>
		</#if>
		<!-- isautoinsure=0为普通单，isautoinsure=1为有自核暂存能力的单(根据轨迹表taskcode=40,41) -->
		<#if isautoinsure == "0" || (!proposalInfo.businessProposalFormNo?? && !proposalInfo.strongProposalFormNo??)>
			<button class="btn btn-primary restart" type="button" name="restart" id="restart${carInsTaskInfo_index}" title="重新发起核保" >重新发起核保</button>
		</#if>
		<a class="btn btn-primary" href="/cm/business/mytask/queryTask">返回我的任务</a>
	</div>
</div>
