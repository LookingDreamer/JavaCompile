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
	requirejs([ "cm/ordermanage/underpaidding" ]);
</script>

<div class="row">
 	<div class="col-md-12">
<!--人工报价子页面-->
<!--引入保单信息公共页面-->
<#include "cm/common/insurancePolicyInfo.ftl" />
<#include "cm/common/commomPlatInfo.ftl" />
<!--子流程id-->
<input type="hidden" id="subInstanceId${carInsTaskInfo_index}" name="subInstanceId" value="${carInsTaskInfo.subInstanceId}"/>
<input type="hidden" id="inscomcode${carInsTaskInfo_index}" name="inscomcode" value="${carInsTaskInfo.inscomcode}"/>
<input type="hidden" id="deptcode" name="deptcode" value="${paymentinfo.deptcode}"/>
<#include "cm/common/proposalNumber.ftl"/>	
<!-- 被保人身份证信息, 支付和二支任务 -->
<#if showbbrIDinfo == true>
	<#include "cm/common/idcardinfo.ftl" /><br>
</#if>
<table border="1px" class="hovertable col-md-12" >
		<tr>
			<td class="col-md-12" colspan="2"  style="background-color:#8A8A8A;color:white;">
				<div class="row">
			<div class="col-md-9">
				支付信息
			</div>

			<div class="col-md-3" align="right" <#if "refund" == taskcode || "refunded" == taskcode>style="display: none"</#if> >
				<a href="javascript:window.parent.openDialogForCM('business/ordermanage/editpaymentInfo?taskid=${taskid}&inscomcode=${inscomcode}&subInstanceId=${carInsTaskInfo.subInstanceId}&deptcode=${paymentinfo.deptcode}');"><font color="white">修改</font></a>
			</div>
		</div>
			</td>
		</tr>
		<tr>
			<td  class="bgg col-md-2 " >
				<label>支付方式:</label>
			</td>
			<td  class=" col-md-10 ">
				<label id="ad_paymentmethod">${paytype}</label>
			</td>
		</tr>
		<tr>
			<td  class="bgg col-md-2 " >
				<label>支付跟踪号:</label>
			</td>
			<td  class=" col-md-10 ">
				<label id="ad_insurecono">${insbOrderpayment.insurecono}</label>
			</td>
		</tr>
		<tr>
			<td  class="bgg col-md-2 " >
				<label>交易跟踪号:</label>
			</td>
			<td  class=" col-md-10 ">
				<label id="ad_tradeno">${insbOrderpayment.tradeno}</label>
			</td>
		</tr>
		<!--
		<tr>
			<td  class="bgg col-md-2 " >
				<label>校验码:</label>
			</td>
			<td  class=" col-md-10 ">
				<label id="ad_checkcode">${paymentinfo.checkcode}</label>
			</td>
		</tr>
		-->
		<tr>
			<td  class="bgg col-md-2 " >
				<label>保费:</label>
			</td>
			<td  class=" col-md-10 ">
				<label>${paymentinfo.premium}</label>
			</td>
		</tr>
		<tr>
			<td  class="bgg col-md-2 " >
				<label>快递费:</label>
			</td>
			<td  class=" col-md-10 ">
				<label>${paymentinfo.totaldeliveryamount}</label>
			</td>
		</tr>
		<tr>
			<td  class="bgg col-md-2 " >
				<label>支付手续费:</label>
			</td>
			<td  class=" col-md-10 ">
				<label>${paymentinfo.paypoundage}</label>
			</td>
		</tr>
		<tr>
			<td  class="bgg col-md-2 " >
				<label>总计:</label>
			</td>
			<td  class=" col-md-10 ">
				<label>${paymentinfo.total}</label>
			</td>
		</tr>

	</table>
		<!-- 退款信息-->
		<#if "refund" == taskcode || "refunded" == taskcode>
			<#include "cm/common/refundInfo.ftl" />
		</#if>
	</div>
</div>
<div class="task_fixbottom">
	<!-- 备注信息页面引入-->
	<#include "cm/common/remarksInfo.ftl" />
	<!--按钮行-->
  	<div class="col-md-12">
<#if "refund" == taskcode>

	<#if "40" == insbOrderpayment.paychannelid || "41" == insbOrderpayment.paychannelid>
        <button id="refund${carInsTaskInfo_index}" type="button" class="btn btn-primary refund"	name="refund" <#if "1" == insbOrderpayment.refundstatus> disabled="disabled" </#if>>退款申请</button>
	<#else>
        <button id="refunded${carInsTaskInfo_index}" type="button" class="btn btn-primary refunded"	name="refunded" <#if "1" == insbOrderpayment.refundstatus> disabled="disabled" </#if>>退款成功</button>
	</#if>
    <a href="/cm/business/refundtaskmanage/refundtaskmanagelist"><button type="button" class="btn btn-primary btn-sm refundbtn">返回</button></a>
<#elseif "refunded" == taskcode>
    <a href="/cm/business/refundtaskmanage/refundtaskmanagelist"><button type="button" class="btn btn-primary btn-sm">返回</button></a>
<#else>
 		<button id="paiSuccess${carInsTaskInfo_index}" type="button" class="btn btn-primary paiSuccess"	name="paiSuccess${carInsTaskInfo_index}">支付成功</button>
		<button id="backTask${carInsTaskInfo_index}" type="button" class="btn btn-primary backTask"	name="backTask${carInsTaskInfo_index}">打回任务</button>
		<button class="btn btn-primary restartUnderWriting" type="button" name="restartUnderWriting" id="restartUnderWriting${carInsTaskInfo_index}" title="重新核保" >重新核保</button>
		<a class="btn btn-primary" href="/cm/business/mytask/queryTask">返回我的任务</a>
		<#if showbbrIDinfo == true>
			<button id="applypin${carInsTaskInfo_index}" type="button" class="btn btn-primary applypin"	name="applypin">申请验证码</button>
		</#if>
</#if>
 	</div>

</div>

<div class="modal" id="applyingdiv" role="dialog" style="margin-top: 15%;">
		<div class="modal-dialog">
			<div class="modal-content" style="width: 400px;height:200px;">
				<div class="modal-body">
					<center><label style="font-size:18px;margin-top: 70px;">验证码申请中，请勿关闭....</label></center>
				</div>
			</div>
		</div>
	</div>