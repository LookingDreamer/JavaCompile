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
	<!-- 投保单号 -->
	<table border="1px" class="hovertable col-md-12">
		<tr>
			<td class="col-md-12" colspan="2" style="background-color:#8A8A8A;color:white;">
				<label>退款信息</label>
			</td>
		</tr>
		<tr>
			<td class="bgg col-md-2">
				<label>退款方式:</label>
			</td>
			<td class="col-md-10">
				<div calss="row">
					<label id="businessProposalFormNo">
							<#if "01" == insbOrderpayment.refundtype>
								全额退款
							<#elseif "02" == insbOrderpayment.refundtype>
								差额退款
							<#else>
							状态错误
							</#if>
					</label>
				</div>
			</td>
		</tr>
		<tr>
			<td class="bgg col-md-2">
				<label>退款金额:</label>
			</td>
			<td class="col-md-10">
				<div calss="row">
					<label id="strongProposalFormNo">${-insbOrderpayment.amount}</label>
				</div>
			</td>
		</tr>
	</table>
