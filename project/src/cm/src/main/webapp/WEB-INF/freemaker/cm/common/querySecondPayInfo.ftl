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
	
	#showqueryordresult{ 
		width: 80%; 
		position: fixed; 
		margin: 0px auto; 
		top: 0px; left: 0px; 
		bottom: 0px; 
		right: 0px; 
		z-index: 1050; 
	}
	#fqshowqueryordresult{ 
		width: 80%; 
		position: fixed; 
		margin: 0px auto; 
		top: 0px; left: 0px; 
		bottom: 0px; 
		right: 0px; 
		z-index: 1050; 
	}
</style>
<script type="text/javascript" >
	requirejs([ "cm/common/querysecondpayorder" ]);
</script>
<!-- 银联 -->
<div id="showqueryordresult" class="modal fade"> 
	<div class="panel panel-info">
		<div class="panel-heading">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4>查询订单结果</h4>
		</div>
		<div class="panel-body">
	        <table border="1px" class="hovertable col-md-12">
	          	<tr style="background-color:#8A8A8A;color:white;">
					<td colspan="3"><b>查询订单结果</b></td>	
				</tr>
				<tr>
					<td class="bgg col-md-4">支付方式：</td>
					<td><span id="tx_yl_channelId"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">支付类型：</td>
					<td><span id="tx_yl_payType"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">支付平台流水号：</td>
					<td><span id="tx_yl_bizTransactionId"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">支付金额(元)：</td>
					<td><span id="tx_yl_amount"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">银联订单号：</td>
					<td><span id="tx_yl_paySerialNo"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">订单状态：</td>
					<td><span id="tx_yl_orderState"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">支付时间：</td>
					<td><span id="tx_yl_transDate"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">支付流水号：</td>
					<td><span id="tx_yl_bizId"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">参考号：</td>
					<td><span id="tx_yl_referNo"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">银行卡号：</td>
					<td><span id="tx_yl_cardNo"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">查询状态：</td>
					<td><span id="tx_yl_description"></span></td>
				</tr>
			</table>
		</div>			
		<div class="panel-footer" style="text-align: right;">  
			<button class="btn btn-default" type="button" title="关闭" data-dismiss="modal">&nbsp;&nbsp;关闭&nbsp;&nbsp;</button>
		</div>
	</div> 
</div>

<!-- 分期保 -->
<div id="fqshowqueryordresult" class="modal fade"> 
	<div class="panel panel-info">
		<div class="panel-heading">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4>查询订单结果</h4>
		</div>
		<div class="panel-body">		
	        <table border="1px" class="hovertable col-md-12">
	          	<tr style="background-color:#8A8A8A;color:white;">
					<td colspan="3"><b>查询订单结果</b></td>	
				</tr>
				<tr>
					<td class="bgg col-md-4">支付金额(保费金额, 元)：</td>
					<td><span id="tx_fq_amount"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">保网订单状态：</td>
					<td><span id="tx_fq_orderState"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">交易成功时间：</td>
					<td><span id="tx_fq_transDate"></span></td>
				</tr>
				<tr style="background-color:#9F9F9F;color:white;">
					<td colspan="3">众安订单详情</td>	
				</tr>
				<tr>
					<td class="bgg col-md-4">众安订单状态：</td>
					<td><span id="tx_fq_orderStatus"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">众安放款状态：</td>
					<td><span id="tx_fq_fundStatus"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">众安商户订单号：</td>
					<td><span id="tx_fq_partnerOrderNo"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">众安内部支付流水号(参考号)：</td>
					<td><span id="tx_fq_innerPayNo"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">放款成功时间：</td>
					<td><span id="tx_fq_fundSuccDate"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">支付成功时间：</td>
					<td><span id="tx_fq_paySuccDate"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">支付号：</td>
					<td><span id="tx_fq_payNo"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">支付总金额(保费+服务费, 元)：</td>
					<td><span id="tx_fq_payAmount"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">保费(元)：</td>
					<td><span id="tx_fq_fundAmount"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">平台服务费(元)：</td>
					<td><span id="tx_fq_partnerFee"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">保网订单号：</td>
					<td><span id="tx_fq_partnerPayNo"></span></td>
				</tr>
	        </table>
	    </div>
	    <div class="panel-footer" style="text-align: right;">  
			<button class="btn btn-default" type="button" title="关闭" data-dismiss="modal">&nbsp;&nbsp;关闭&nbsp;&nbsp;</button>
		</div>
	</div>
</div>