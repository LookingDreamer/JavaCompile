<!--保单配置信息公共页面样式表--> 
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
<!--保单配置信息公共页面-->
<div class="insuranceTable${carInsTaskInfo_index}"><#if "true" == changingflag>changingflag${changingflag}</#if>
	<table border="1px" class="hovertable col-md-12" style="margin-bottom:0px;">
	    <tr style="background-color:#383838;color:white;">
	    	<td colspan="3"><font size="3">保险配置</font></td>
	    </tr>
	    <tr style="background-color:#8A8A8A;color:white;">
		    <td class="col-md-4">商业险</td>
		    <td class="col-md-5">保额（及其他信息）</td>
		    <td class="col-md-3">保费</td>
	    </tr>

		<#if "1" == carInsTaskInfo.carInfo.insureconfigsameaslastyear>
        <tr>
            <td class="bgg" style="text-align:left;">
				<input type="checkbox" checked disabled>与上年一致
			</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
		</tr>
		</#if>

	    <#list carInsTaskInfo.insConfigInfo.insConfigList as insConfig>
   			<#if insConfig.isChecked == "Y" && insConfig.inskindtype == "0">
   				<tr>
				    <td class="bgg inskindnamecls0" style="text-align:left;">${insConfig.inskindname}</td>
				    <td>
				    	<#list insConfig.selecteditem as selectelm>
				    		<#if selectelm.TYPE == "01">
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
				    		<#elseif selectelm.TYPE == "03" >
				    			<#list insConfig.amountSlecets as aSelecti>
				    				<!-- 单独处理的  -->
				    				<#if insConfig.inskindcode == "VehicleDemageIns">
				    					保额：${insConfig.amount!""}
				    				<#else>
				    					<#list aSelecti.VALUE as aSelectValue>
				    						<#if aSelectValue.KEY != "">
					    						${aSelectValue.KEY}
					    						<#break> 
					    					</#if>
				    					</#list>
			    					</#if>
			    					<#break>
				    			</#list>
				    		<#else>
				    			${selectelm.VALUE.KEY!""}${selectelm.VALUE.UNIT!""}
				    		</#if>
				    		<#if selectelm_has_next>;</#if>
				    	</#list>
				    	<#if insConfig.specialRiskkindFlag == "04">
					    	<label style="float:right;">
			    				<a href="javascript:window.parent.openDialogForCM('common/insurancepolicyinfo/showSpecialRiskkindcfg?mInstanceid=${carInsTaskInfo.otherInfo.taskid}&inscomcode=${carInsTaskInfo.inscomcode}&riskkindcode=${insConfig.inskindcode}&showEdit=0&riskkindname=${insConfig.inskindname}&inscomname=${carInsTaskInfo.carInfo.inscomname}&inscomcodeList=<#list carInsTaskInfoList as carInsTaskTemp>${carInsTaskTemp.inscomcode}<#if carInsTaskTemp_has_next>,</#if></#list>');">查看信息</a>
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
	    <#list carInsTaskInfo.insConfigInfo.insConfigList as insConfig>
   			<#if insConfig.isChecked == "Y" && insConfig.inskindtype == "1">
   				<tr>
				    <td class="bgg inskindnamecls" style="text-align:left;">${insConfig.inskindname}</td>
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
	    <#list carInsTaskInfo.insConfigInfo.insConfigList as insConfig>
   			<#if insConfig.isChecked == "Y" && (insConfig.inskindtype == "2" || insConfig.inskindtype == "3")>
   				<tr>
				    <td class="bgg inskindnamecls1" style="text-align:left;">${insConfig.inskindname}</td>
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
		    <td>${carInsTaskInfo.insConfigInfo.busdiscountrate!"--"}</td>
	    </tr>
	    <tr>
		    <td class="bgg" style="text-align:left;">交强险折扣率</td>
		    <td>--</td>
		    <td>${carInsTaskInfo.insConfigInfo.strdiscountrate!"--"}</td>
	    </tr>
    </table>
    <div class="col-md-12" style="border:1px solid black;background-color:#99ccff;margin-bottom:5px;border-top:0px;">
		<table>
			<tr>
				<td>商业险保费:${carInsTaskInfo.insConfigInfo.discountBusTotalAmountprice}元,&nbsp;</td>
				<td>交强险保费:${carInsTaskInfo.insConfigInfo.discountStrTotalAmountprice}元,&nbsp;</td>
				<td>车船税:${carInsTaskInfo.insConfigInfo.discountCarTax}元,&nbsp;</td>
				<td>其他:${carInsTaskInfo.insConfigInfo.otherAmountprice}元</td>
			</tr>
			<tr>
				<td><font style="color:red;">保费总额:${carInsTaskInfo.insConfigInfo.discountBusTotalAmountprice+
				carInsTaskInfo.insConfigInfo.discountStrTotalAmountprice+
				carInsTaskInfo.insConfigInfo.discountCarTax+
				carInsTaskInfo.insConfigInfo.otherAmountprice}元</font></td>
			</tr>
		</table>
	</div>
</div>
<!--保单修改表格默认隐藏-->
<div class="editInsuranceTable${carInsTaskInfo_index}" style="display:none">
	<table border="1px" class="hovertable col-md-12" >
	    <tr style="background-color:#383838;color:white;">
	    	<td colspan="3"><font size="3">保险配置</font></td>
	    </tr>
	    <tr style="background-color:#8A8A8A;color:white;">
		    <td class="col-md-4">商业险</td>
		    <td class="col-md-5">保额（及其他信息）</td>
		    <td class="col-md-3">保费</td>
	    </tr>

		<#if true == isRenewal>
		<tr>
			<td class="bgg" style="text-align:left;">
				<input type="checkbox" name="insureconfigsameaslastyear" id="insureconfigsameaslastyear${carInsTaskInfo_index}" value="1"
					   <#if "1" == carInsTaskInfo.carInfo.insureconfigsameaslastyear>checked</#if>/>与上年一致
			</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		</#if>

	    <#list carInsTaskInfo.insConfigInfo.insConfigList as insConfig>
   			<#if insConfig.inskindtype == "0">
   				<tr>
				    <td class="bgg" style="text-align:left;">
				    	<input class="checkItemsAll checkItems_${carInsTaskInfo_index}" id="${carInsTaskInfo_index}_${insConfig.inskindcode}" type="checkbox" name="busiKindprice[${insConfig_index}].isChecked" value="Y" <#if insConfig.isChecked == "Y">checked</#if>/><!--是否选中标记-->
				    	${insConfig.inskindname}
				    	<input type="hidden" name="busiKindprice[${insConfig_index}].id" value="${insConfig.id}"/><!--险种在carkindprice表中的id-->
				    	<input type="hidden" name="busiKindprice[${insConfig_index}].riskcode" value="${insConfig.riskcode}"/><!--险种编码-->
				    	<input type="hidden" name="busiKindprice[${insConfig_index}].riskname" value="${insConfig.riskname}"/><!--险种名称-->
				    	<input type="hidden" name="busiKindprice[${insConfig_index}].inskindcode" value="${insConfig.inskindcode}"/><!--险别编码-->
				    	<input type="hidden" id="${carInsTaskInfo_index}_${insConfig.inskindcode}_inskindname" name="busiKindprice[${insConfig_index}].inskindname" value="${insConfig.inskindname}"/><!--险别名称-->
				    	<input type="hidden" name="busiKindprice[${insConfig_index}].inskindtype" value="${insConfig.inskindtype}"/><!--险别类型-->
				    	<input type="hidden" name="busiKindprice[${insConfig_index}].notdeductible" value="${insConfig.notdeductible}"/><!--不计免赔-->
				    	<input type="hidden" id="${carInsTaskInfo_index}_${insConfig.inskindcode}_preriskkind" name="busiKindprice[${insConfig_index}].preriskkind" value="${insConfig.preriskkind!""}"/><!--前置险种-->
				    	<input type="hidden" name="busiKindprice[${insConfig_index}].specialRiskkindFlag" value="${insConfig.specialRiskkindFlag!""}"/><!--特殊险种标记-->
				    </td>
				    <td>
				    	<#list insConfig.amountSlecets as amountSlecet>
				    		<#if amountSlecet.TYPE == "01">
				    			保额：<input type="text" id="${carInsTaskInfo_index}_${insConfig.inskindcode}_amount" name="busiKindprice[${insConfig_index}].amount" value="${insConfig.amount!""}"/><!--保额-->
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
				    			
				    		<#elseif amountSlecet.TYPE == "03">
				    				投保
					    			<!--投保、不投保选项-->
					    			<input type="hidden" name="busiKindprice[${insConfig_index}].selecteditemStrs[${amountSlecet_index}]" value='{"TYPE":"03","VALUE":{"KEY":"投保","UNIT":"","VALUE":"1"}}'/>
							<#else>
				    			<input type="hidden" id="${carInsTaskInfo_index}_${insConfig.inskindcode}_amount" name="busiKindprice[${insConfig_index}].amount" value="${insConfig.amount!"1"}"/><!--保额-->
				    			<select class="${carInsTaskInfo_index}_${insConfig.inskindcode}_amountSlecet" name="busiKindprice[${insConfig_index}].selecteditemStrs[${amountSlecet_index}]" ><!--险别要素选项-->
				    				<#list amountSlecet.VALUE as amountOpt>
				    					<#if amountOpt.KEY != "不投保">
					    					<option value='{"TYPE":"${amountSlecet.TYPE}","VALUE":{"KEY":"${amountOpt.KEY}","UNIT":"${amountOpt.UNIT}","VALUE":"${amountOpt.VALUE}"}}' 
					    					<#list insConfig.selecteditem as selectelm>
							    				<#if selectelm.TYPE == amountSlecet.TYPE && amountOpt.VALUE == selectelm.VALUE.VALUE>selected</#if>
							    			</#list>>
					    						${amountOpt.KEY}${amountOpt.UNIT}
					    					</option>
				    					</#if>
				    				</#list>
				    			</select>
				    		</#if>
				    		<#if amountSlecet_has_next>;</#if>
				    	</#list>
				    	<#if insConfig.specialRiskkindFlag == "04">
				    		<label style="float:right;">
			    				<a href="javascript:window.parent.openDialogForCM('common/insurancepolicyinfo/showSpecialRiskkindcfg?mInstanceid=${carInsTaskInfo.otherInfo.taskid}&inscomcode=${carInsTaskInfo.inscomcode}&riskkindcode=${insConfig.inskindcode}&showEdit=1&riskkindname=${insConfig.inskindname}&inscomname=${carInsTaskInfo.carInfo.inscomname}&inscomcodeList=<#list carInsTaskInfoList as carInsTaskTemp>${carInsTaskTemp.inscomcode}<#if carInsTaskTemp_has_next>,</#if></#list>');">修改信息</a>
			    			</label>
				    	<#elseif insConfig.specialRiskkindFlag == "05">
			    			<br/>天数：
			    			<input type="text" id="${carInsTaskInfo_index}_${insConfig.inskindcode}_specialRiskkindValue" name="busiKindprice[${insConfig_index}].specialRiskkindValue" value="${insConfig.specialRiskkindValue!""}"/><!--修理期间费用补偿险天数-->
			    			天
		    			</#if>
				    </td>
				    <td>
				    	<input type="text" id="${carInsTaskInfo_index}_${insConfig.inskindcode}_amountprice" name="busiKindprice[${insConfig_index}].amountprice" value="${insConfig.amountprice!"0.0"}"/><!--保费-->
				    </td>
			    </tr>
   			</#if>
   		</#list>
	    <tr style="background-color:#8A8A8A;color:white;">
		    <td>商业险附加不计免赔</td>
		    <td>保额（及其他信息）</td>
		    <td>--</td>
	    </tr>
	    <#list carInsTaskInfo.insConfigInfo.insConfigList as insConfig>
   			<#if insConfig.inskindtype == "1">
   				<tr>
				    <td class="bgg" style="text-align:left;">
				    	<input class="checkItemsAll checkItems_${carInsTaskInfo_index}" type="checkbox" id="${carInsTaskInfo_index}_${insConfig.inskindcode}" name="notdKindprice[${insConfig_index}].isChecked" value="Y" <#if insConfig.isChecked == "Y">checked</#if>/><!--是否选中标记-->
				    	${insConfig.inskindname}
				    	<input type="hidden" name="notdKindprice[${insConfig_index}].id" value="${insConfig.id}"/><!--险种在carkindprice表中的id-->
				    	<input type="hidden" name="notdKindprice[${insConfig_index}].riskcode" value="${insConfig.riskcode}"/><!--险种编码-->
				    	<input type="hidden" name="notdKindprice[${insConfig_index}].riskname" value="${insConfig.riskname}"/><!--险种名称-->
				    	<input type="hidden" name="notdKindprice[${insConfig_index}].inskindcode" value="${insConfig.inskindcode}"/><!--险别编码-->
				    	<input type="hidden" id="${carInsTaskInfo_index}_${insConfig.inskindcode}_inskindname" name="notdKindprice[${insConfig_index}].inskindname" value="${insConfig.inskindname}"/><!--险别名称-->
				    	<input type="hidden" name="notdKindprice[${insConfig_index}].inskindtype" value="${insConfig.inskindtype}"/><!--险别类型-->
				    	<input type="hidden" name="notdKindprice[${insConfig_index}].notdeductible" value="${insConfig.notdeductible}"/><!--不计免赔-->
				    	<input type="hidden" id="${carInsTaskInfo_index}_${insConfig.inskindcode}_preriskkind" name="notdKindprice[${insConfig_index}].preriskkind" value="${insConfig.preriskkind}"/><!--前置险种-->
				    </td>
				    <td>投保</td>
				    <td>
				    	<input type="text" id="${carInsTaskInfo_index}_${insConfig.inskindcode}_amountprice" name="notdKindprice[${insConfig_index}].amountprice" value="${insConfig.amountprice!"0.0"}"/><!--保费-->
				    </td>
			    </tr>
   			</#if>
   		</#list>
	    <tr style="background-color:#8A8A8A;color:white;">
		    <td>交强险和车船税</td>
		    <td>保额（及其他信息）</td>
		    <td>保费</td>
	    </tr>
	    <#list carInsTaskInfo.insConfigInfo.insConfigList as insConfig>
   			<#if insConfig.inskindtype == "2" || insConfig.inskindtype == "3">
   				<tr>
				    <td class="bgg" style="text-align:left;">
				    	<input class="checkItemsAll checkItems_${carInsTaskInfo_index}" id="${carInsTaskInfo_index}_${insConfig.inskindcode}" type="checkbox" name="stroKindprice[${insConfig_index}].isChecked" value="Y" <#if insConfig.isChecked == "Y">checked</#if>/><!--是否选中标记-->
				    	${insConfig.inskindname}
				    	<input type="hidden" name="stroKindprice[${insConfig_index}].id" value="${insConfig.id}"/><!--险种在carkindprice表中的id-->
				    	<input type="hidden" name="stroKindprice[${insConfig_index}].riskcode" value="${insConfig.riskcode}"/><!--险种编码-->
				    	<input type="hidden" name="stroKindprice[${insConfig_index}].riskname" value="${insConfig.riskname}"/><!--险种名称-->
				    	<input type="hidden" name="stroKindprice[${insConfig_index}].inskindcode" value="${insConfig.inskindcode}"/><!--险别编码-->
				    	<input type="hidden" id="${carInsTaskInfo_index}_${insConfig.inskindcode}_inskindname" name="stroKindprice[${insConfig_index}].inskindname" value="${insConfig.inskindname}"/><!--险别名称-->
				    	<input type="hidden" name="stroKindprice[${insConfig_index}].inskindtype" value="${insConfig.inskindtype}"/><!--险别类型-->
				    	<input type="hidden" name="stroKindprice[${insConfig_index}].notdeductible" value="${insConfig.notdeductible}"/><!--不计免赔-->
				    	<input type="hidden" id="${carInsTaskInfo_index}_${insConfig.inskindcode}_preriskkind" name="stroKindprice[${insConfig_index}].preriskkind" value="${insConfig.preriskkind}"/><!--前置险种-->
				    </td>
				    <td>
				    	<!--
				    	<#list insConfig.amountSlecets as amountSlecet>
				    		<#if amountSlecet.TYPE == "01">
				    			保额：<input type="text" id="${carInsTaskInfo_index}_${insConfig.inskindcode}_amount" name="stroKindprice[${insConfig_index}].amount" value="${insConfig.amount!""}"/>
				    			<#list insConfig.selecteditem as selectelm>
				    				<#if selectelm.TYPE == "01">${selectelm.VALUE.UNIT}</#if>
				    			</#list>
				    		<#else>
				    			<select class="${carInsTaskInfo_index}_${insConfig.inskindcode}_amountSlecet" name="stroKindprice[${insConfig_index}].selecteditemStrs[${amountSlecet_index}]">
				    				<#list amountSlecet.VALUE as amountOpt>
				    					<option value='{"TYPE":"${amountSlecet.TYPE}","VALUE":{"KEY":"${amountOpt.KEY}","UNIT":"","VALUE":"${amountOpt.VALUE}"}}' 
				    					<#list insConfig.selecteditem as selectelm>
						    				<#if selectelm.TYPE == amountSlecet.TYPE && amountOpt.VALUE == selectelm.VALUE.VALUE>selected</#if>
						    			</#list>>
				    						${amountOpt.KEY}${amountOpt.UNIT}
				    					</option>
				    				</#list>
				    			</select>
				    		</#if>
				    		<#if amountSlecet_has_next>;</#if>
				    	</#list>
				    	-->
				    	<#if insConfig.inskindtype == "2">购买</#if>
				    	<#if insConfig.inskindtype == "3">代缴</#if>
				    </td>
				    <td>
				    	<input type="text" id="${carInsTaskInfo_index}_${insConfig.inskindcode}_amountprice" name="stroKindprice[${insConfig_index}].amountprice" value="${insConfig.amountprice!"0.0"}"/><!--保费-->
				    </td>
			    </tr>
   			</#if>
   		</#list>
   		<tr style="background-color:#8A8A8A;color:white;">
		    <td>其他</td>
		    <td></td>
		    <td>折扣率</td>
	    </tr>
	    <tr>
		    <td class="bgg" style="text-align:left;">商业险折扣率</td>
		    <td>--<input name="busRate" type="hidden" value="${carInsTaskInfo.insConfigInfo.busdiscountrate!""}"></td>
		    <td class="busdiscountrate">
		    	<#--<#if ((carInsTaskInfo.insConfigInfo.busdiscountrateFlag)?? || (true == isRenewal))>
		    		<input type="text" id="${carInsTaskInfo_index}_busdiscountrate" name="busdiscountrate" value="${carInsTaskInfo.insConfigInfo.busdiscountrate!""}"/>
		    	<#else>
		    		<span style="color:red;">无保单数据，不支持修改</span>
		    		<!--
		    		<input type="hidden" id="${carInsTaskInfo_index}_busdiscountrate" name="busdiscountrate" value="${carInsTaskInfo.insConfigInfo.busdiscountrate!"0.0"}"/>
		    		&ndash;&gt;
		    	</#if>-->
		    </td>
	    </tr>
	    <tr>
		    <td class="bgg" style="text-align:left;">交强险折扣率</td>
		    <td>--<input name="strRate" type="hidden" value="${carInsTaskInfo.insConfigInfo.strdiscountrate!""}"></td>
		    <td class="strdiscountrate">
		    	<#--<#if ((carInsTaskInfo.insConfigInfo.strdiscountrateFlag)?? || (true == isRenewal))>
			    	<input type="text" id="${carInsTaskInfo_index}_strdiscountrate" name="strdiscountrate" value="${carInsTaskInfo.insConfigInfo.strdiscountrate!""}"/>
		    	<#else>
		    		<span style="color:red;">无保单数据，不支持修改</span>
		    		<!--
			    	<input type="hidden" id="${carInsTaskInfo_index}_strdiscountrate" name="strdiscountrate" value="${carInsTaskInfo.insConfigInfo.strdiscountrate!"0.0"}"/>
			    	&ndash;&gt;
		    	</#if>-->
		    </td>
	    </tr>
    </table>
    <!--总保费-->
	<input type="hidden" id="totalAmountprice${carInsTaskInfo_index}" value="${carInsTaskInfo.insConfigInfo.discountBusTotalAmountprice+
		carInsTaskInfo.insConfigInfo.discountStrTotalAmountprice+carInsTaskInfo.insConfigInfo.discountCarTax+
		carInsTaskInfo.insConfigInfo.otherAmountprice}"/>
</div>
