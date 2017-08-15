<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>最后一次报价</title>
<style type="text/css">
	span.feildtitle {
		font-weight:bold;
		color:#34495E;
	}
	body {font-size: 14px;}
</style>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<h6 class="modal-title">最后一次报价</h6>
	</div>
	<div class="modal-body">
		<table class="table table-bordered" style="margin:0px;">
			<thead>
				<tr>
			  		<th class="col-md-2 active">商业险</th>
					<th class="col-md-2 active">保额</th>
					<th class="col-md-2 active">保费</th>
			  	</tr>
			</thead>
			<tbody>
				<#list insConfigInfo.insConfigList as insConfig>
					<#if insConfig.isChecked == "Y" && insConfig.inskindtype == "0">
				   		<tr>
							<td class="bgg" style="text-align:left;">${insConfig.inskindname}</td>
							<td>
								<#list insConfig.selecteditem as selectelm>
									<#if selectelm.TYPE == "01" || insConfig.inskindcode == "VehicleDemageIns">
								                                     保额：${insConfig.amount!""}${selectelm.VALUE.UNIT!""}
								    <#else>
								    		${selectelm.VALUE.KEY!""}${selectelm.VALUE.UNIT!""}
								    </#if>
								    <#if selectelm_has_next>;</#if>
								</#list>
							</td>
							<td>${insConfig.amountprice!"--"}</td>
						</tr>
				   	</#if>
				</#list>		    					  	
			</tbody>
		</table>
		<table class="table table-bordered" style="margin:0px;">
			<thead>
				<tr>
			  		<th class="col-md-2 active">商业险附加不计免赔</th>
					<th class="col-md-2 active">保额</th>
					<th class="col-md-2 active">保费</th>
			  	</tr>
			</thead>
			<tbody>
			  	<#list insConfigInfo.insConfigList as insConfig>
				   	<#if insConfig.isChecked == "Y" && insConfig.inskindtype == "1">
				   		<tr>
							<td class="bgg" style="text-align:left;">${insConfig.inskindname}</td>
							<td>投保</td>
						    <td>${insConfig.amountprice!"0.0"}</td>
						</tr>
				   	</#if>
				</#list>
			</tbody>
		</table>
		<table class="table table-bordered" style="margin:0px;">
			<thead>
			  	<tr>
			  		<th class="col-md-2 active">交强险和车船税</th>
					<th class="col-md-2 active">保额</th>
					<th class="col-md-2 active">保费</th>
			  	</tr>
			</thead>
			<tbody>
			  	<#list insConfigInfo.insConfigList as insConfig>
				   	<#if insConfig.isChecked == "Y" && (insConfig.inskindtype == "2" || insConfig.inskindtype == "3")>
				   		<tr>
							<td class="bgg" style="text-align:left;">${insConfig.inskindname}</td>
							<td>
								<#list insConfig.selecteditem as selectelm>
								    <#if selectelm.TYPE == "01">
								    	保额：${insConfig.amount!""}${selectelm.VALUE.UNIT!""}
								    <#else>
								    	${selectelm.VALUE.KEY!""}${selectelm.VALUE.UNIT!""}
								    </#if>
								    <#if selectelm_has_next>;</#if>
								</#list>
							</td>
							<td>${insConfig.amountprice!"--"}</td>
						</tr>
				   	</#if>
				</#list>
			</tbody>
		</table>
		<div class="panel panel-info" style="margin:0px;">
 			<div class="panel-heading" style="text-align:center;margin:0px;">
 			  	<label>
					商业险保费：${insConfigInfo.discountBusTotalAmountprice?string('0.00')}元 &nbsp;
					交强险保费：${insConfigInfo.discountStrTotalAmountprice?string('0.00')}元 &nbsp;
					车船税：${insConfigInfo.discountCarTax?string('0.00')}元 &nbsp;
					其他：${insConfigInfo.otherAmountprice?string('0.00')}元
				</label>
 			  	<br><label style=";color:red;">保费总额：${(insConfigInfo.discountBusTotalAmountprice+insConfigInfo.discountStrTotalAmountprice+insConfigInfo.discountCarTax+insConfigInfo.otherAmountprice)?string('0.00')}元</label>
			</div> 
 		</div>
 		<table class="table table-bordered" style="margin:0px;">
 			<tr style="color:red;">
				<td class="bgg col-md-2" style="text-align:left;">商业险折扣率：${insConfigInfo.busdiscountrate}</td>
				<td class="bgg col-md-2" style="text-align:left;">交强险折扣率：${insConfigInfo.strdiscountrate}</td>
				<td class="bgg col-md-2" style="text-align:left;">商业险赔付率：</td>
			</tr>
 		</table>
	</div>
</body>
</html>
