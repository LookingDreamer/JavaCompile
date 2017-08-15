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
<!--人工报价子页面   -->
<!--引入保单信息公共页面 -->
<#include "cm/common/insurancePolicyInfo.ftl"/>
<div class="row">
 	<div class="col-md-12">
		<#include "cm/common/proposalNumber2.ftl" />
	</div>
</div>
<!--补充信息框-->
<div class="row">
 	<div class="col-md-12">
		<table border="1px" class="hovertable col-md-12">
		    <tr>
		    	<td class="col-md-12" style="background-color:#8A8A8A;color:white;">
		    		<div class="row">
 						<div class="col-md-9">
 							平台信息
 						</div>
 						<div class="col-md-3" align="right">
 							<!--a href="javascript:window.parent.openLargeDialog('business/manualprice/modifyaddmessage?instanceId=${carInsTaskInfo.carInfo.taskid}&num=${carInsTaskInfo_index}&deptCode=${agentInfo.deptCode}&inscomcode=${carInsTaskInfo.inscomcode}');"><font color="white">修改</font></a-->
 						</div>
 					</div>
		    	</td>
		    </tr>
		    <tr style="background-color:white;">
			    <td class="col-md-12">
			    	<div class="row">
 						<div class="col-md-12">
					    	<table class="orange col-md-12">
					    		<tr>
								    <td class="col-md-12">
								    	<div class="yellowdiv">
								    		<div class="row">
	 											<div class="col-md-2">
		 											<button class="btn btn-primary getruleReplenishInfoAgain" type="button" 
															id="getReplenishInfoAgain${carInsTaskInfo_index}">重新查询</button>
	 											</div>
	 											<div class="col-md-10">
													<h5><label id="getReplenishInfoAgainStatus${carInsTaskInfo_index}">${rulequeryresult}</label></h5><!--查询结果-->
	 											</div>
	 										</div>
	 										<div class="row">
	 											<div class="col-md-6">
													数据最后更新时间：<label id="renewInfoDateUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.renewInfoDate}</label>
												</div>
												<div class="col-md-6">
													数据有效期：<label id="periodOfValidityUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.periodOfValidity}</label>
												</div>
											</div>
										</div>
								    </td>
							    </tr>
							</table>
							
					    	<!--table  class="hovertable col-md-12">
							    <tr>
								    <td class="bgg col-md-3">平台商业险起保日期：</td>
								    <td>${carInsTaskInfo.otherInfo.systartdate}</td>
								    <td class="bgg col-md-3">商业险出险次数：</td>
			    					<td>${carInsTaskInfo.otherInfo.syclaimtimes}</td>
							    </tr>
							    <tr>
								    <td class="bgg">平台商业险终止日期：</td>
								    <td>${carInsTaskInfo.otherInfo.syenddate}</td>
								    <td class="bgg">交强险理赔次数：</td>
			    					<td>${carInsTaskInfo.otherInfo.jqclaimtimes}</td>
							    </tr>
							    <tr>
								    <td class="bgg">平台交强险起保日期：</td>
								    <td>${carInsTaskInfo.otherInfo.jqstartdate}</td>
								    <td class="bgg">上年商业险投保公司：</td>
								    <td>
									    <span id="preinsShortnamePageInfo${carInsTaskInfo_index}">
									    	${carInsTaskInfo.otherInfo.preinsShortname}
									    </span>
								    </td>
							    </tr>
							    <tr>
								    <td class="bgg">平台交强险终止日期：</td>
								    <td>${carInsTaskInfo.otherInfo.jqenddate}</td>
                					<td class="bgg">上年商业险保单号：</td>
               						 <td>
				    					<span id="preinsSypolicynoPageInfo${carInsTaskInfo_index}">
                    					${carInsTaskInfo.otherInfo.preinsSypolicyno}
                    					</span>
               	 					</td>
							    </tr>
							</table-->
							
							<table class="orange col-md-12 ">
					    		<tr>
								    <td class="col-md-3">无赔款优待系数：</td>
								    <td class="col-md-3">
								    	<label id="noClaimDiscountCoefficientUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.noClaimDiscountCoefficient}</label>
								    </td>
								    <td class="col-md-3">无赔款折扣浮动原因：</td>
								    <td class="col-md-3">
								    	<label id="noClaimDiscountReasonUpPageInfo${carInsTaskInfo_index}">${rulequery.noclaimdiscountcoefficientreasons}</label>
								    </td>
							    </tr>
					    		<tr>
								    <td class="col-md-3">平台商业险理赔次数：</td>
								    <td class="col-md-3">
								    	<label id="claimTimesUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.claimTimes}</label>
								    </td>
								    <td class="col-md-3">平台商业险理赔总金额：</td>
								    <td class="col-md-3">
								    	<label id="bwLastClaimSumUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.bwLastClaimSum}</label>
								    </td>
							    </tr>
					    		<tr>
								    <td class="col-md-3">投保类型：</td>
								    <td class="col-md-3">
								    	<label id="firstInsureTypeUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.firstInsureType}</label>
								    </td>
								     <td class="col-md-3">上年商业险赔付率：</td>
								    <td class="col-md-3">
								    	<label id="claimrateUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.claimrate}</label>
								    </td>
							    </tr>
					    		<tr>
								    <td class="col-md-3">酒驾系数： </td>
								    <td class="col-md-3">
								    	<label id="drunkingUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.drunking}</label>
								    </td>
								    <td class="col-md-3">平台客户忠诚度系数：</td>
								    <td class="col-md-3">
								    	<label id="loyaltyUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.loyalty}</label>
								    </td>
							    </tr>
					    		<tr>
								    <td class="col-md-3">平台交强险理赔次数：</td>
								    <td class="col-md-3">
								    	<label id="compulsoryClaimTimesUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.compulsoryClaimTimes}</label>
								    </td>
								    <td class="col-md-3">交强险理赔系数：</td>
								    <td class="col-md-3">
								    	<label id="compulsoryClaimRateUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.compulsoryClaimRate}</label>
								    </td>
							    </tr>
					    		<tr>
					    			<td class="col-md-3">交强险违法浮动：</td>
								    <td class="col-md-3">
								    	<label id="compulsoryIllegalFloatUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.compulsoryIllegalFloat}</label>
								    </td>
					    			<td class="col-md-3">交强险浮动原因：</td>
								    <td class="col-md-3">
								    	<label id="compulsoryFloatReasonUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.compulsoryFloatReason}</label>
								    </td>
							    </tr>
					    		<tr>
								    <td class="col-md-3">交强险折扣保费(部分地区)：</td>
								    <td class="col-md-3">
								    	<label id="compulsoryDiscountPremiumUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.compulsoryDiscountPremium}</label>
								    </td>
								    <td class="col-md-3">交通违法系数(部分地区)：</td>
								    <td class="col-md-3">
								    	<label id="trafficoffencediscountUpPageInfo${carInsTaskInfo_index}">${rulequery.trafficoffencediscount}</label>
								    </td>
							    </tr>
					    		<tr>
								    <td class="col-md-3">车船税滞纳金：</td>
								    <td class="col-md-3">
								    	<label id="traveltaxLateFeeUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.traveltaxLateFee}</label>
								    </td>
								    <td class="col-md-3">往年补缴税额：</td>
								    <td class="col-md-3">
								    	<label id="previousPayTaxUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.previousPayTax}</label>
								    </td>
							    </tr>
					    		<tr>
								    <td class="col-md-3">风险类别：</td>
								    <td class="col-md-3">
								    	<label id="dangerTypeUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.dangerType}</label>
								    </td>
								    <td class="col-md-3">平台车价(部分地区)：</td>
								    <td class="col-md-3">
								    	<label id="platformCarPriceUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.platformCarPrice}</label>
								    </td>
							    </tr>
					    		<tr>
								    <td class="col-md-3">是否通过纯电销投保：</td>
								    <td class="col-md-3">
								    	<label id="isTelemarketingUpPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.replenishInfo.isTelemarketing}</label>
								    </td>
								    <td class="col-md-3"></td>
								    <td class="col-md-3">
								    	
								    </td>
							    </tr>
					    	</table>
					    </div>
					</div>
						    
						    <div class="panel-heading  ">
									  <div class="row">
										 <div class="col-md-12">
										 	<span class="feildtitle">商业险平台记录：</span>
										 </div>
									  </div>
								  	</div>
									 <table class="table table-bordered  ">
									     <tr class="active">
									       <td class="col-md-3">上年投保公司</td>
									       <td class="col-md-3">保单号</td>
									       <td class="col-md-3">平台起保日期</td>
									       <td class="col-md-3">平台终保日期</td>
									     </tr>
										    <tr>
												<td class="col-md-3">${rulequery.inscorpname0}</td>
												<td class="col-md-3">${rulequery.policyid0}</td>
												<td class="col-md-3">${rulequery.policystarttime0}</td>
												<td class="col-md-3">${rulequery.policyendtime0}</td>
											</tr>
									</table>
									<table class="table table-bordered  ">		 
									     <tr class="active">
									       <td >出险时间</td>
									       <td >结案时间</td>
									       <td >理赔金额</td>
									       <td >保单号</td>
									       <td >保险公司</td>
									     </tr>
									     <#list rulequery.syclaimes as syclaim>
										    <tr>
												<td >${syclaim.casestarttimec0}</td>
												<td >${syclaim.caseendtimec0}</td>
												<td >${syclaim.claimamountc0}</td>
												<td >${syclaim.policyidc0}</td>
												<td >${syclaim.inscorpnamec0}</td>
											</tr> 
										</#list>
										<#if (rulequery.syclaimes?size) lte 0>   
											<tr>
												<td ></td>
												<td ></td>
												<td ></td>
												<td ></td>
												<td ></td>
											</tr>
										</#if> 	
									 </table>
									 
									  <div class="panel-heading  ">
										  <div class="row">
											 <div class="col-md-12">
											 	<span class="feildtitle">交强险平台记录：</span>
											 </div>
										  </div>
									  	</div>
										 <table class="table table-bordered  ">
										   <thead>
									     <tr class="active">
									       <td class="col-md-3">上年投保公司</td>
									       <td class="col-md-3">保单号</td>
									       <td class="col-md-3">平台起保日期</td>
									       <td class="col-md-3">平台终保日期</td>
									     </tr>
									   </thead>
									   <tbody>
										    <tr>
												<td class="col-md-3">${rulequery.inscorpname1}</td>
												<td class="col-md-3">${rulequery.policyid1}</td>
												<td class="col-md-3">${rulequery.policystarttime1}</td>
												<td class="col-md-3">${rulequery.policyendtime1}</td>
											</tr> 
									   </tbody>
									   </table>
									<table class="table table-bordered  ">		 
									    <thead>
									     <tr class="active">
									       <td >出险时间</td>
									       <td >结案时间</td>
									       <td >理赔金额</td>
									       <td >保单号</td>
									       <td >保险公司</td>
									     </tr>
									   </thead>
									   <tbody>
									   <#list rulequery.jqclaimes as jqclaim>
										    <tr>
												<td >${jqclaim.casestarttimec1}</td>
												<td >${jqclaim.caseendtimec1}</td>
												<td >${jqclaim.claimamountc1}</td>
												<td >${jqclaim.policyidc1}</td>
												<td >${jqclaim.inscorpnamec1}</td>
											</tr> 
										</#list>
										<#if (rulequery.jqclaimes?size) lte 0>   
											<tr>
												<td ></td>
												<td ></td>
												<td ></td>
												<td ></td>
												<td ></td>
											</tr>
										</#if>   
									   </tbody>
									 </table>
					<div class="row " style="margin-top:5px;">
						<div class="col-md-12">
							<h5><label>平台车价信息:</label></h5>
					    	<table class="col-md-12 whiteborder hovertable">
					    		<tr>
								    <td >品牌名称</td>
								    <td >行业车型编码</td>
								    <td >车型编码</td>
								    <td >新车购置价</td>
								    <td >新车购置价（含税）</td>
								    <td >类比价</td>
								    <td >类比价（含税）</td>
							    </tr>
							    <tr>
								    <td>${rulequery.carbrandname}</td>
								    <td>${rulequery.trademodelcode}</td>
								    <td>${rulequery.modelcode}</td>
								    <td>${rulequery.price}</td>
								    <td>${rulequery.taxprice}</td>
								    <td>${rulequery.analogyprice}</td>
								    <td>${rulequery.analogytaxprice}</td>
							    </tr>
							    <tr>
							    	<td >车辆种类</td>
							    	<td >上市年份</td>
								    <td >核定载客</td>
								    <td >核定载质量</td>
								    <td >整备质量</td>
								    <td >排气量</td>
								    <td ></td>
								</tr> 
								<tr>
								    <td>${rulequery.vehicletype}</td>
								    <td>${rulequery.carmodeldate}</td>
								    <td>${rulequery.seatcnt}</td>
								    <td>${rulequery.modelload}</td>
								    <td>${rulequery.fullload}</td>
								    <td>${rulequery.displacement}</td>
								    <td></td>
							    </tr>   
							</table>
						</div>
					</div>
					<div class="col-md-13">
						<table border="1px" class="hovertable col-md-12">
							    <tr>
							    	<td class="col-md-12" style="background-color:#8A8A8A;color:white;">
							    		<div class="row">
					 						<div class="col-md-9">
					 							规则算价因子
					 						</div>
					 						<div class="col-md-3" align="right">
					 							<a href="javascript:window.parent.openLargeDialog('business/manualprice/modifyaddmessage?instanceId=${carInsTaskInfo.carInfo.taskid}&num=${carInsTaskInfo_index}&deptCode=${agentInfo.deptCode}&inscomcode=${carInsTaskInfo.inscomcode}');"><font color="white">修改</font></a>
					 						</div>
					 					</div>
							    	</td>
							    </tr>
							</table>
 						<div class="col-md-12">
					    	<table border="0px" class="whiteborder hovertable col-md-12">
							    <#list carInsTaskInfo.supplements1 as supplement>
							    <tr>
								    <td class="bgg col-md-2" style="text-align:right;">${supplement.metadataName}:</td>
								    <td class="col-md-4">
								    	<label id="${supplement.keyid?substring(supplement.keyid?index_of('.')+1)}">
								    		<#if (supplement.metadataValueMapList)??>
								    			<#list supplement.metadataValueMapList as valuemap><#if supplement.metadataValueKey == valuemap.value>${valuemap.key}</#if></#list>
								    			<#else>${supplement.metadataValueKey}</#if></label>
								    </td>
								   <#list carInsTaskInfo.supplements2 as sup>
								    <#if supplement_index==sup_index>
								    <td class="bgg col-md-2" style="text-align:right;">${sup.metadataName}:</td>
								    <td class="col-md-4">
								    	<label id="${sup.keyid?substring(sup.keyid?index_of('.')+1)}">
								    		<#if (sup.metadataValueMapList)??>
								    			<#list sup.metadataValueMapList as valuemap2><#if sup.metadataValueKey == valuemap2.value>${valuemap2.key}</#if></#list>
								    			<#else>${sup.metadataValueKey}</#if></label>
								    </td>
								    </#if>
								    </#list>
							    </tr>
							    </#list>
						    </table>
					    </div>
					</div>
			    </td>
		    </tr>
	    </table>
	</div>
</div>
<!-- 规则试算框-->
<div class="row">
 	<div class="col-md-12">
 		<table border="1px" class="hovertable col-md-12">
		    <tr>
		    	<td>
		    		<!--div class="checkbox">
						<label>
							<input type="checkbox" id="carPriceRangeLimitFlag${carInsTaskInfo_index}"/>
							放开对所有公司车价浮动范围的限制(以规则试算后每个公司的报价结果为准)
						</label>
					</div><br/-->
					<button class="btn btn-primary ruleCalculationBtn" type="button" 
						id="ruleCalculation${carInsTaskInfo_index}" title="规则试算">规则试算</button>
						<input type="hidden" id="ischeckedprice" value="0"/>
					<div class="row ruleCalculationResult" style="display:none;font-size:14px;">
						<br/>
					 	<div class="col-md-4"><b>供应商名称</b></div>
					 	<div class="col-md-8"><b>试算结果</b></div>
					 	<div class="ruleCalculationResultSub"></div>
					</div>
		    	</td>
		    </tr>
	    </table>
	</div>
</div>
<input type="hidden" id="subInstanceId${carInsTaskInfo_index}" name="subInstanceId" value="${carInsTaskInfo.subInstanceId}"/>
<input type="hidden" id="inscomcode${carInsTaskInfo_index}" name="inscomcode" value="${carInsTaskInfo.inscomcode}"/>
<input type="hidden" id="hasbusi${carInsTaskInfo_index}" name="hasbusi" value="${carInsTaskInfo.proposalInfo.hasbusi}"/>
<input type="hidden" id="hasstr${carInsTaskInfo_index}" name="hasstr" value="${carInsTaskInfo.proposalInfo.hasstr}"/>
<input type="hidden" id="inscomname${carInsTaskInfo_index}" name="insccomname" value="${carInsTaskInfo.carInfo.inscomname}"/>
<div class="task_fixbottom">
	<!-- 备注信息页面引入-->
	<#include "cm/common/remarksInfo.ftl" />
	<!--按钮行-->
  	<div class="btnsbar">
 		<button class="btn btn-primary passInsurancepre" type="button" 
			id="passInsurancepre${carInsTaskInfo_index}" title="报价通过">报价通过</button>
		<button class="btn btn-primary peopleDopre" type="button" 
			id="peopleDopre${carInsTaskInfo_index}" title="转人工报价">转人工报价</button>
		<button class="btn btn-primary backTask" type="button" 
			id="backTask${carInsTaskInfo_index}" title="打回任务">打回任务</button>
		<a class="btn btn-primary" href="/cm/business/mytask/queryTask">返回我的任务</a>
 	</div>
 	<div id="div${carInsTaskInfo_index}" style="display:none;"><#list inscomcodes as inscom><div><input type="checkbox" value="${inscom.inscomcode}" checked="true"  name="inscomlist${carInsTaskInfo_index}" />${inscom.inscomname}</div></#list>
 			<div class="btnsbar"><button class="btn btn-primary passInsurance" type="button" style="display:none;"
								  id="passInsurance${carInsTaskInfo_index}" title="确定">确定</button>
								 <button class="btn btn-primary peopleDo" type="button" style="display:none;"
								  id="peopleDo${carInsTaskInfo_index}" title="确定">转人工</button></div>
 		</div>
</div>

 
