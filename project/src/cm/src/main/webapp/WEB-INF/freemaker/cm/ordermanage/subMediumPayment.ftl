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
	requirejs([ "cm/ordermanage/mediumPayment" ]);
</script>
<!--引入保单信息公共页面-->
<#include "cm/common/insurancePolicyInfo.ftl" />
<#include "cm/common/commomPlatInfo.ftl" />
<!--子流程id-->
<div class="row">
 	<div class="col-md-12">
 		<#include "cm/common/proposalNumber.ftl" /><br>
 		
 		<!-- 被保人身份证信息, 支付和二支任务 -->
 		<#if showbbrIDinfo == true>
 			<#include "cm/common/idcardinfo.ftl" /><br>
 		</#if>
 		
		<#include "cm/common/secondPaymentInfo.ftl" />
 	</div>
</div>
<input type="hidden" id="subInstanceId${carInsTaskInfo_index}" name="subInstanceId" value="${carInsTaskInfo.subInstanceId}"/>
<input type="hidden" id="inscomcode${carInsTaskInfo_index}" name="inscomcode" value="${carInsTaskInfo.inscomcode}"/>
<div class="task_fixbottom">
	<!-- 备注信息页面引入-->
	<#include "cm/common/remarksInfo.ftl" />
	<!--按钮行-->
	
  	<div class="btnsbar">
  		<button id="mediumPay${carInsTaskInfo_index}" type="button" class="btn btn-primary mediumPay"	name="mediumPay">二次支付成功</button>
		<button id="endTask4Pay${carInsTaskInfo_index}" type="button" class="btn btn-primary endTask4Pay"	name="endTask">支付失败</button>
		<button id="backTask${carInsTaskInfo_index}" type="button" class="btn btn-primary backTask"	name="backTask">打回任务</button>
		<a class="btn btn-primary" href="/cm/business/mytask/queryTask">返回我的任务</a>
		<#if showbbrIDinfo == true>
			<button id="applypin${carInsTaskInfo_index}" type="button" class="btn btn-primary applypin"	name="applypin">申请验证码</button>
		</#if>
	</div>
</div>
<!--遮罩层<div class="zhezhao" style="z-index: 9998;position:fixed;top:0;left:0;width:100%;height:100%;background:#ada2a2;opacity:0.4;filter:alpha(opacity=40);display:none;"></div>-->
	<div class="modal" id="applyingdiv" role="dialog" style="margin-top: 15%;">
		<div class="modal-dialog">
			<div class="modal-content" style="width: 400px;height:200px;">
				<div class="modal-body">
					<center><label style="font-size:18px;margin-top: 70px;">验证码申请中，请勿关闭....</label></center>
				</div>
			</div>
		</div>
	</div>
