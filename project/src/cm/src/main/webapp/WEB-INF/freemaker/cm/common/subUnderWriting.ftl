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
<!--人工报价子页面-->
<!--引入保单信息公共页面-->
<#include "cm/common/insurancePolicyInfo.ftl" />
<#include "cm/common/commomPlatInfo.ftl" />
<!--子流程id-->
<input type="hidden" id="subInstanceId${carInsTaskInfo_index}" name="subInstanceId" value="${carInsTaskInfo.subInstanceId}"/>
<input type="hidden" id="subInstanceId" name="subInstanceId" value="${carInsTaskInfo.subInstanceId}"/>
<!--补充信息框-->
<!-- 支付与配送信息 -->
<div class="row">
 	<div class="col-md-12" id="policyNumber">
		<#include "cm/common/policyNumber.ftl"/>
		<#include "cm/common/proposalNumber2.ftl" />
		<table border="1px" class="hovertable col-md-12">
			<tr>
				<td class="col-md-12" colspan="2" style="background-color:#8A8A8A;color:white;">
					<label>支付与配送信息</label>
				</td>
			</tr>
			<tr>
				<td class="bgg col-md-2">
					<label>支付方式:</label>
				</td>
				<td class="col-md-10">
					<label>${paymentinfo.paymentmethod }</label>
				</td>
			</tr>
			<tr>
				<td class="bgg col-md-2">
					<label>配送方式:</label>
				</td>
				<td class="col-md-10">
					<label>${paymentinfo.deliverymethod}</label>
				</td>
			</tr>
			<tr>
				<td class="bgg col-md-2">
					<label>保费:</label>
				</td>
				<td class="col-md-10">
					<label>${paymentinfo.premium}</label>
				</td>
			</tr>
			<tr>
				<td class="bgg col-md-2">
					<label>快递费:</label>
				</td>
				<td class="col-md-10">
					<label>${paymentinfo.totaldeliveryamount}</label>
				</td>
			</tr>
			<tr>
				<td class="bgg col-md-2">
					<label>支付手续费:</label>
				</td>
				<td class="col-md-10">
					<label></label>
				</td>
			</tr>
			<tr>
				<td class="bgg col-md-2">
					<label>总计:</label>
				</td>
				<td class="col-md-10">
					<label>${paymentinfo.total}</label>
				</td>
			</tr>
		</table>
		<!-- 承保信息 -->
		<table border="1px" class="hovertable col-md-12">
			<tr>
				<td class="col-md-12" colspan="2" style="background-color:#8A8A8A;color:white;">
					<label>承保信息</label>
				</td>
			</tr>
			<tr>
				<td class="bgg col-md-2">
					<label>处理结果:</label>
				</td>
				<td class="col-md-10">
					<lable id="policystatus">
						<#if taskcode == "23">
							已承保
						<#else>
							承保中
						</#if>
					</lable>
				</td>
			</tr>
			<tr>
				<td class="bgg col-md-2">
					<label>异常反馈:</label>
				</td>
				<td class="col-md-10">
					<label></label>
				</td>
			</tr>
		</table>
		<!-- 备注信息-->
		<table >
			<tr>
				<td class="short title">
					<label >备注信息 </label>
				</td >
				<td class="long opt">
				</td>
			</tr>
			<tr>
				<td class="short">
					<label>配送方式:</label>
				</td>
				<td class="long">
					<!--<label id="deliverytype">${orderdeliveryinfo.deliverytype}</label>-->
					<select id="deliverytype">
						<#list deliveryTypeList as deliveryTypeItem>
							<option value="${deliveryTypeItem.codevalue}" <#if deliveryTypeItem.codevalue == orderdeliveryinfo.deliverytype>selected="selected"</#if>>${deliveryTypeItem.codename}</option>
					    </#list>
					</select>
				</td>
			</tr>
			<tr class="dediv">
				<td class="short" >
					<label>物流公司</label>
				</td>
				<td class="long">
					<select id="comcode">
						<#list comList as com>
							<option value="${com.codevalue}" <#if com.codevalue == '7'>selected="selected"</#if>>${com.codename}</option><#--默认顺丰快递(codevalue=7)-->
					    </#list>
					</select>
				</td>
			</tr>
			<tr class="dediv">
				<td class="short">
					<label>配送编号:</label>
				</td>
				<td class="long">
					<input type="text" id="tracenumber"/>
				</td>
			</tr>
			<tr class="dediv">
				<td class="short">
					<label>配送结果:</label>
				</td>
				<td class="long">
					<label></label>
				</td>
			</tr>
			<tr class="indiv">
				<td class="short">
					<label>自取网点:</label>
				</td>
				<td class="long">
					<label>${deptInfo.address}${deptInfo.comname}</label>
				</td>
			</tr>
		</table>
		<br/>
		<!-- 配送信息 -->
		<table>
			<tr class="dediv">
				<td class="short title">
					<label>配送信息</label>
				</td>
			    <td class="long opt">
					<label></label>
					<label style="float:right;"><a href="javascript:window.parent.openDialogForCM('business/ordermanage/preEditDeliveryInfo?taskid=${taskid}&inscomcode=${inscomcode}');"><font style="color:white">修改</font></a></label>
				</td>
			</tr>
			<tr class="dediv">
				<td class="short">
					<label>订单来源:</label>
				</td>
				<td class="long">
					<label id="orderfrom">${orderdeliveryinfo.orderfrom}</label>
				</td>
			</tr>
			<tr class="dediv">
				<td class="short">
					<label>配送方:</label>
				</td>
				<td class="long">
					<label id="deliveryside">${orderdeliveryinfo.deliveryside}</label>
				</td>
			</tr>
			<tr class="dediv">
				<td class="short">
					<label>收件人:</label>
				</td>
				<td class="long">
					<label id="recipientname">${orderdeliveryinfo.recipientname}</label>
				</td>
			</tr>
			<tr class="dediv">
				<td class="short">
					<label>收件人手机:</label>
				</td>
				<td class="long">
					<label id="recipientmobilephone">${orderdeliveryinfo.recipientmobilephone}</label>
				</td>
			</tr>
			<tr class="dediv">
				<td class="short">
					<label>详细地址:</label>
				</td>
				<td class="long">
					<label id="recipientaddress">${orderdeliveryinfo.recipientaddress}</label>
				</td>
			</tr>
			<tr class="dediv">
				<td class="short">
					<label>邮政编码:</label>
				</td>
				<td class="long">
					<label id="zip">${orderdeliveryinfo.zip}</label>
				</td>
			</tr>
		</table>
	</div>
</div>
<div class="task_fixbottom">
	<!-- 备注信息页面引入-->
	<#include "cm/common/remarksInfo.ftl" />
	<!--按钮行-->
  	<div class="btnsbar">
  		<input type="hidden" id="inscomcode" value="${inscomcode}"/>
 		<button id="undwrtsuccess" type="button" class="btn btn-primary" onclick="undwrtSuccess('${taskid}','${taskcode}','${carInsTaskInfo.inscomcode}','${carInsTaskInfo.insConfigInfo.discountBusTotalAmountprice}','${carInsTaskInfo.insConfigInfo.discountStrTotalAmountprice+carInsTaskInfo.insConfigInfo.discountCarTax}');">
	 		<#if taskcode == "27" || taskcode == "28">
	 			承保打单成功
	 		<#elseif taskcode == "23">
	 			打单成功
	 		</#if>
 		</button>
 		<button id="allundwrtsuccess" type="button" class="btn btn-primary" onclick="allundwrtSuccess('${taskid}','${taskcode}','${carInsTaskInfo.inscomcode}','${carInsTaskInfo.insConfigInfo.discountBusTotalAmountprice}','${carInsTaskInfo.insConfigInfo.discountStrTotalAmountprice+carInsTaskInfo.insConfigInfo.discountCarTax}');">
 			<#if taskcode == "27">
 				承保打单配送成功
 			<#elseif taskcode="23">
 				打单配送成功
 			</#if>
 		</button>
 		<#if taskcode == "27" || taskcode == "28">
			<#if isReunw == "1" >
				<button id="undwrtsearch1" type="button" class="btn btn-primary" onclick="undwrtSearch('${taskid}','${carInsTaskInfo.inscomcode}','${isReunw }');">重新查询承保结果</button>
			<#elseif isReunw == "2">
				<button id="undwrtsearch2" type="button" class="btn btn-primary" onclick="undwrtSearch('${taskid}','${carInsTaskInfo.inscomcode}','${isReunw }');">重新查询承保结果</button> 
			</#if>
 		</#if>
		<button id="undwrtback" type="button" class="btn btn-primary" onclick="undwrtBack('${taskid}','${carInsTaskInfo.inscomcode}');">打回任务</button>
		<button class="mybtn btn btn-primary refuseInsurance" type="button" name="refuseInsurance" id="refuseInsurance${carInsTaskInfo_index}" title="拒绝承保" >拒绝承保</button>
		<a id="backToMyTask" style="display:none;" class="btn btn-primary" href="/cm/business/mytask/queryTask">返回我的任务</a>
 	</div>
</div>
