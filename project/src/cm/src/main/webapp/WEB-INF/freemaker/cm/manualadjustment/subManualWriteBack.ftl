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
	requirejs([ "cm/manualadjustment/manualWriteBack" ]);
</script>
<!--引入保单信息公共页面-->
<#include "cm/common/insurancePolicyInfo.ftl" />
<#include "cm/common/commomPlatInfo.ftl" />
<!--子流程id-->
<input type="hidden" id="subInstanceId${carInsTaskInfo_index}" name="subInstanceId" value="${carInsTaskInfo.subInstanceId}"/>


<div class="task_fixbottom">
	<!-- 备注信息页面引入-->
	<#include "cm/common/remarksInfo.ftl" />
	<!--按钮行-->
  	<div class="btnsbar">
 		<button class="btn btn-primary adjustment" type="button" name="adjustment" id="adjustment${carInsTaskInfo_index}" title="回写完成" >回写完成</button>
		<button class="btn btn-primary backEdit" type="button" name="backEdit" id="backEdit${carInsTaskInfo_index}" title="退回修改" >退回修改</button>
		<button class="btn btn-primary refuseInsurance" type="button" name="refuseInsurance" id="refuseInsurance${carInsTaskInfo_index}" title="拒绝承保" >拒绝承保</button>
		<button class="btn btn-primary backTask" type="button" name="backTask" id="backTask${carInsTaskInfo_index}" title="打回任务" >打回任务</button>
		<a class="btn btn-primary" href="/cm/business/mytask/queryTask">返回我的任务</a>
 	</div>
</div>
