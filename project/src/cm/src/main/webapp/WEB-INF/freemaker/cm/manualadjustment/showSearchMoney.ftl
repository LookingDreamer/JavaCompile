<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看报价金额</title>
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
</style>
<body>
<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<h6 class="modal-title">报价金额：</h6>
	</div>
<!--保单配置信息公共页面-->
<div class="modal-body">
<div class="insuranceTable${carInsTaskInfo_index}">
	<table border="1px" class="hovertable col-md-12" style="margin-bottom:0px;">
	    <tr style="background-color:#383838;color:white;">
	    	<td colspan="3"><font size="3">保险配置</font></td>
	    </tr>
	    <tr style="background-color:#8A8A8A;color:white;">
		    <td class="col-md-4">商业险</td>
		    <td class="col-md-5">保额（及其他信息）</td>
		    <td class="col-md-3">保费</td>
	    </tr>
	    <#list insConfigInfo.insConfigList as insConfig>
   			<#if insConfig.isChecked == "Y" && insConfig.inskindtype == "0">
   				<tr>
				    <td class="bgg" style="text-align:left;">${insConfig.inskindname}</td>
				    <td>
				    	<#list insConfig.selecteditem as selectelm>
				    		<#if selectelm.TYPE == "01" || selectelm.TYPE == "03" >
				    			保额：${insConfig.amount!""}
				    			<!--取单位-->
				    			<#list insConfig.amountSlecets as aSelecti>
				    				<#if aSelecti.TYPE == "01">
				    					<#list aSelecti.VALUE as aSelectValue>
				    						<#if aSelectValue.UNIT != "">
				    							${aSelectValue.UNIT}
				    							<#break>
				    						</#if>
				    					</#list>
				    					<#break>
				    				</#if>
				    			</#list>
				    		<#else>
				    			${selectelm.VALUE.KEY!""}${selectelm.VALUE.UNIT!""}
				    		</#if>
				    		<#if selectelm_has_next>;</#if>
				    	</#list>
				    	<#if insConfig.specialRiskkindFlag == "04">
					    	<label style="float:right;">
			    				<a href="javascript:window.parent.openDialogForCM('common/insurancepolicyinfo/showSpecialRiskkindcfg?mInstanceid=${otherInfo.taskid}&inscomcode=${inscomcode}&riskkindcode=${insConfig.inskindcode}&showEdit=0&riskkindname=${insConfig.inskindname}&inscomname=${carInfo.inscomname}&inscomcodeList=<#list carInsTaskInfoList as carInsTaskTemp>${carInsTaskTemp.inscomcode}<#if carInsTaskTemp_has_next>,</#if></#list>');">查看信息</a>
			    			</label>
			    		<#elseif insConfig.specialRiskkindFlag == "05">
			    			;${insConfig.specialRiskkindValue}天
		    			</#if>
				    </td>
				    <td>${insConfig.amountprice!"--"}</td>
			    </tr>
   			</#if>
   		</#list>
	    <tr style="background-color:#8A8A8A;color:white;">
		    <td>商业险附加不计免赔</td>
		    <td>保额（及其他信息）</td>
		    <td>--</td>
	    </tr>
	    <#list insConfigInfo.insConfigList as insConfig>
   			<#if insConfig.isChecked == "Y" && insConfig.inskindtype == "1">
   				<tr>
				    <td class="bgg" style="text-align:left;">${insConfig.inskindname}</td>
				    <td>投保</td>
				    <td>${insConfig.amountprice!"0.0"}</td>
			    </tr>
   			</#if>
   		</#list>
	    <tr style="background-color:#8A8A8A;color:white;">
		    <td>交强险和车船税</td>
		    <td>保额（及其他信息）</td>
		    <td>保费</td>
	    </tr>
	    <#list insConfigInfo.insConfigList as insConfig>
   			<#if insConfig.isChecked == "Y" && (insConfig.inskindtype == "2" || insConfig.inskindtype == "3")>
   				<tr>
				    <td class="bgg" style="text-align:left;">${insConfig.inskindname}</td>
				    <td>
				    	<!--
				    	<#list insConfig.selecteditem as selectelm>
				    		<#if selectelm.TYPE == "01">
				    			保额：${insConfig.amount!""}${selectelm.VALUE.UNIT!""}
				    		<#else>
				    			${selectelm.VALUE.KEY!""}${selectelm.VALUE.UNIT!""}
				    		</#if>
				    		<#if selectelm_has_next>;</#if>
				    	</#list>
				    	-->
				    	<#if insConfig.inskindtype == "2">购买</#if>
				    	<#if insConfig.inskindtype == "3">代缴</#if>
				    </td>
				    <td>${insConfig.amountprice!"--"}</td>
			    </tr>
   			</#if>
   		</#list>
   		<tr style="background-color:#8A8A8A;color:white;">
		    <td>其他</td>
		    <td>保额</td>
		    <td>保费</td>
	    </tr>
	    <tr>
		    <td class="bgg" style="text-align:left;">商业险折扣率</td>
		    <td>--</td>
		    <td>${insConfigInfo.busdiscountrate!"--"}</td>
	    </tr>
	    <tr>
		    <td class="bgg" style="text-align:left;">交强险折扣率</td>
		    <td>--</td>
		    <td>${insConfigInfo.strdiscountrate!"--"}</td>
	    </tr>
    </table>
    <div class="col-md-12" style="border:1px solid black;background-color:#99ccff;margin-bottom:5px;border-top:0px;">
		<table>
			<tr>
				<td>商业险保费:${insConfigInfo.discountBusTotalAmountprice}元,&nbsp;</td>
				<td>交强险保费:${insConfigInfo.discountStrTotalAmountprice}元,&nbsp;</td>
				<td>车船税:${insConfigInfo.discountCarTax}元,&nbsp;</td>
				<td>其他:${insConfigInfo.otherAmountprice}元</td>
			</tr>
			<tr>
				<td><font style="color:red;">保费总额:${insConfigInfo.discountBusTotalAmountprice+
				insConfigInfo.discountStrTotalAmountprice+
				insConfigInfo.discountCarTax+
				insConfigInfo.otherAmountprice}元</font></td>
			</tr>
		</table>
	</div>
</div>
	<div class="row">
		 	<div class="col-md-6"></div>
		 	<div class="col-md-6" align="right">
		 		<button class="btn btn-default" type="button" title="关闭" data-dismiss="modal">&nbsp;&nbsp;关闭&nbsp;&nbsp;</button>
		 	</div>
		</div>
</div>
</body>
</html>
