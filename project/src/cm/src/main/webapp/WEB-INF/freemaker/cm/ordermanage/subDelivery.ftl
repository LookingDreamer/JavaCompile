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
	#supplyparamtable td{
        padding-bottom: 7px;
        border: 1px solid #bbbbbb;
    }
</style>
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "cm/ordermanage/delivery" ],function($) {
			
		});
</script>

<div class="row">
 	<div class="col-md-12">
 		<!-- 保单号信息 -->
		<#include "cm/common/policyNumber.ftl" /><br>
		<br>
		<!-- 支付金额-->
		<table>
			<tr>
				<td class="short title">
					<label>支付金额</label>
				</td>
				<td class="long opt">
					<label></label>
				</td> 
			</tr>
			<tr>
				<td class="short">
					<label>保费:</label>
				</td>
				<td class="long">
					<label>${paymentinfo.premium}</label>
				</td>
			</tr>
			<tr>
				<td class="short">
					<label>快递费:</label>
				</td>
				<td class="long">
					<label>${paymentinfo.totaldeliveryamount}</label>
				</td>
			</tr>
			<tr>
				<td class="short">
					<label>支付手续费:</label>
				</td>
				<td class="long">
					<label>${paymentinfo.paypoundage}</label>
				</td>
			</tr>
			<tr>
				<td class="short">
					<label>总计:</label>
				</td>
				<td class="long">
					<label>${paymentinfo.total}</label>
				</td>
			</tr>
		</table>
		<br>
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
					<select id="comcode${carInsTaskInfo_index}">
						<#list comList as com>
							<#--<option value="${com.codevalue}" <#if com.codevalue == orderdeliveryinfo.logisticscompany>selected="selected"</#if>>${com.codename}</option>-->
                            <option value="${com.codevalue}"
									<#if orderdeliveryinfo.logisticscompany?? && com.codevalue == orderdeliveryinfo.logisticscompany>selected="selected"
									<#elseif !orderdeliveryinfo.logisticscompany && com.codevalue == "7">selected="selected"</#if>>${com.codename}</option><#--优先配送表的物流公司后默认顺丰快递(codevalue=7)-->
					    </#list>
					</select>
				</td>
			</tr>
			<tr class="dediv">
				<td class="short">
					<label>配送编号:</label>
				</td>
				<td class="long">
					<input type="text" id="tracenumber${carInsTaskInfo_index}" value="${orderdeliveryinfo.tracenumber}"/>
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
			<tr class="dediv">
				<td class="short">
					<label>寄件人单位名称:</label>
				</td>
				<td class="long">
					<label id="senderchannel">${orderdeliveryinfo.senderchannel}</label>
				</td>
			</tr>
			<tr class="dediv">
				<td class="short">
					<label>寄件人名字:</label>
				</td>
				<td class="long">
					<label id="sendername">${orderdeliveryinfo.sendername}</label>
				</td>
			</tr>
			<tr class="dediv">
				<td class="short">
					<label>寄件人地址:</label>
				</td>
				<td class="long">
					<label id="senderaddress">${orderdeliveryinfo.senderaddress}</label>
				</td>
			</tr>
			<tr class="dediv">
				<td class="short">
					<label>寄件人电话:</label>
				</td>
				<td class="long">
					<label id="senderphone">${orderdeliveryinfo.senderphone}</label>
				</td>
			</tr>
		</table>
        <br/>
        <!-- 补充数据项-->
        <table id="supplyparamtable">
            <tr>
                <td class="short title" style="border: #6B6B6B">
                    <label >补充数据项 </label>
                </td >
                <td class="long opt" style="border: #6B6B6B">
                </td>
            </tr>
			<#assign showcolumnfirst=0 showcolumnsecond=0 showcolumnfirstclass="short">
			<#include "cm/common/showSupplyParam.ftl" />
        </table>
	</div>
</div>
<div class="task_fixbottom">
	<!-- 备注信息页面引入-->
	<#include "cm/common/remarksInfo.ftl" />
	<!--按钮行-->
  	<div class="btnsbar">
		<input type="hidden" id="inscomcode" value="${inscomcode}"/>
		<button id="deliverysuccess${carInsTaskInfo_index}" type="button" class="btn btn-primary deliverysuccess"	name="deliverysuccess">配送完成</button>
		<button id="backTask${carInsTaskInfo_index}" type="button" class="btn btn-primary backTask"	name="backTask">打回任务</button>
		<a class="btn btn-primary" href="/cm/business/mytask/queryTask">返回我的任务</a>
	</div>
</div>
