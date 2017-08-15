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
<script type="text/javascript">
	requirejs([ "cm/common/querysecondpayorder" ]);
</script>


				<!-- 支付信息 -->
				<table border="1px" class="hovertable col-md-12" >
					<tr style="background-color:#8A8A8A;color:white;">
						<td class="col-md-13" colspan="12"  >
							<div class="row">
								<div class="col-md-9">支付信息</div>
								<div class="col-md-3" align="right">
 									<a href="javascript:showdetail();"><font color="white">查看详情</font></a>
								</div>
								<#if balanceFlag == "1">
                                    <div class="col-md-9" style="font:bold 28px 微软雅黑;color: yellow;">
                                        <span class="glyphicon glyphicon-bell" style="color: #ff6600;"></span>
										该单存在差额补齐，请务必与保网明确补款事宜再做支付！！！
									</div>
								</#if>
							</div>
						</td>	
					</tr>
					<tr>
						<td class="col-md-1" style="background-color:#E5E5E5" align="center">
						操作
						</td>
						<td  class="col-md-2" style="background-color:#E5E5E5" align="center">
							支付流水号
						</td>
						<td  class="col-md-2" style="background-color:#E5E5E5" align="center">
							创建时间
						</td>
						<td  class="col-md-2" style="background-color:#E5E5E5" align="center">
							交易跟踪号
						</td>
						<td  class="col-md-2" style="background-color:#E5E5E5" align="center">
							支付方式
						</td>
						<td  class="col-md-1" style="background-color:#E5E5E5" align="center">
							退款类型
						</td>
						<td  class="col-md-1" style="background-color:#E5E5E5" align="center">
							金额
						</td>
						<td class="col-md-2" style="background-color:#E5E5E5" align="center">
							支付状态
						</td>
						
					</tr>
					<#list paymentinfo as payinfo>
					<tr>
						<td align="center">
						<#if payinfo.showqueryorderbtn == 1>
							<a class="btn btn-primary" href="javascript:showqueryorderresult('${payinfo.secpayorderrequrl}', '${payinfo.payflowno}', '${payinfo.orderno}', '${payinfo.paychannelid}','${carInsTaskInfo.relationPersonInfo.applicantidcardno}');">
								查询订单结果
							</a>
						</#if>
						</td>
						<td align="center">${payinfo.paymentransaction}</td>
						<td align="center">${payinfo.creatime}</td>
						<td align="center">${payinfo.tradeno}</td>
						<td align="center">${payinfo.paymentmethod}</td>
						<td align="center">${payinfo.refundtype}</td>
						<td align="center">${payinfo.amount}</td>
						<td align="center">${payinfo.result}</td>
					</tr>
					</#list> 
				</table>
				
				<div id="showqueryordresult" class="modal fade"  role="dialog" aria-labelledby="myModalLabel" data-backdrop="false"
				 data-keyboard="false" aria-hidden="true">
				  <div class="modal-dialog">
					<div class="modal-content">
					    <h4>&nbsp;</h4>
	      				<div class="modal-header">
	        				<button type="button" class="close queryordresdig" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        				<h2>查询订单结果</h2>
	      				</div>
	      				<div class="modal-body" style="overflow:auto; height:300px;">
	        				<div class="container-fluid">
	          					<table border="1px" class="hovertable col-md-12" >
	          						<tr style="background-color:#8A8A8A;color:white;">
										<td colspan="3"><b>查询订单结果</b></td>	
									</tr>
									<tr>
										<td class="bgg col-md-5">支付方式：</td>
										<td><span id="tx_yl_channelId"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">支付类型：</td>
										<td><span id="tx_yl_payType"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">支付平台流水号：</td>
										<td><span id="tx_yl_bizTransactionId"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">支付金额(元)：</td>
										<td><span id="tx_yl_amount"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">银联订单号：</td>
										<td><span id="tx_yl_paySerialNo"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">订单状态：</td>
										<td><span id="tx_yl_orderState"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">支付时间：</td>
										<td><span id="tx_yl_transDate"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">支付流水号：</td>
										<td><span id="tx_yl_bizId"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">参考号：</td>
										<td><span id="tx_yl_referNo"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">银行卡号：</td>
										<td><span id="tx_yl_cardNo"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">查询状态：</td>
										<td><span id="tx_yl_description"></span></td>
									</tr>
	          					</table>
	        				</div>
	      				</div>
	      				<div class="modal-footer">
	        				<button type="button" class="btn btn-default queryordresdig">关闭</button>
	      				</div>
	    			</div>
	    		  </div>
				</div>
				
				
				<div id="fqshowqueryordresult" class="modal fade"  role="dialog" aria-labelledby="myModalLabel" data-backdrop="false"
				 data-keyboard="false" aria-hidden="true">
				  <div class="modal-dialog">
					<div class="modal-content">
					    <h4>&nbsp;</h4>
	      				<div class="modal-header">
	        				<button type="button" class="close fqqueryordresdig" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        				<h2>查询订单结果</h2>
	      				</div>
	      				<div class="modal-body" style="overflow:auto; height:300px;">
	        				<div class="container-fluid">
	          					<table border="1px" class="hovertable col-md-12" >
	          						<tr style="background-color:#8A8A8A;color:white;">
										<td colspan="3"><b>查询订单结果</b></td>	
									</tr>
									<tr>
										<td class="bgg col-md-5">支付金额(保费金额, 元)：</td>
										<td><span id="tx_fq_amount"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">保网订单状态：</td>
										<td><span id="tx_fq_orderState"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">交易成功时间：</td>
										<td><span id="tx_fq_transDate"></span></td>
									</tr>
									<tr style="background-color:#9F9F9F;color:white;">
										<td colspan="3">众安订单详情</td>	
									</tr>
									<tr>
										<td class="bgg col-md-5">众安订单状态：</td>
										<td><span id="tx_fq_orderStatus"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">众安放款状态：</td>
										<td><span id="tx_fq_fundStatus"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">众安商户订单号：</td>
										<td><span id="tx_fq_partnerOrderNo"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">众安内部支付流水号(参考号)：</td>
										<td><span id="tx_fq_innerPayNo"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">放款成功时间：</td>
										<td><span id="tx_fq_fundSuccDate"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">支付成功时间：</td>
										<td><span id="tx_fq_paySuccDate"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">支付号：</td>
										<td><span id="tx_fq_payNo"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">支付总金额(保费+服务费, 元)：</td>
										<td><span id="tx_fq_payAmount"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">保费(元)：</td>
										<td><span id="tx_fq_fundAmount"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">平台服务费(元)：</td>
										<td><span id="tx_fq_partnerFee"></span></td>
									</tr>
									<tr>
										<td class="bgg col-md-5">保网订单号：</td>
										<td><span id="tx_fq_partnerPayNo"></span></td>
									</tr>
	          					</table>
	        				</div>
	      				</div>
	      				<div class="modal-footer">
	        				<button type="button" class="btn btn-default fqqueryordresdig">关闭</button>
	      				</div>
	    			</div>
	    		  </div>
				</div>
				
				
				<div id="showDetail" class="modal fade"  role="dialog" aria-labelledby="myModalLabel" data-backdrop="false"
				 data-keyboard="false" aria-hidden="true">
				  <div class="modal-dialog">
					<div class="modal-content">
	      				<div class="modal-header">
	        				<button type="button" class="close closeshowpic" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        				<h4 class="modal-title">支付详情</h4>
	      				</div>
	      				<div class="modal-body" style="overflow:auto; height:200px;">
	        				<div class="container-fluid">
	          					<table border="1px" class="hovertable col-md-12" >
	          						<tr style="background-color:#8A8A8A;color:white;">
										<td colspan="3"><font>支付详情</font></td>	
									</tr>
									<#list paymentinfo as info>
									<#if info_index == 0>
									<tr>
										<td class="bgg col-md-5">保费:</td>
										<td >${info.premium}</td>
									</tr>
									<tr>
										<td class="bgg col-md-5"">快递费:</td>
										<td>${info.totaldeliveryamount}</td>
									</tr>
									<tr>
										<td class="bgg col-md-5"">支付手续费:</td>
										<td>${info.paypoundage}</td>
									</tr>
									<tr>
										<td class="bgg col-md-5"">总计:</td>
										<td>${info.total}</td>
									</tr>
									</#if>
									</#list>
	          					</table>
	        				</div>
	      				</div>
	      				<div class="modal-footer">
	        				<button type="button" class="btn btn-default closeshowpic">关闭</button>
	      				</div>
	    			</div>
	    		  </div>
				</div>
	