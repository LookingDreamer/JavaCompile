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
				<label>投保单号</label>
				<#if !(taskcode == "21" || taskcode == "23" || taskcode == "24")>
					<label style="float:right;"><a href="javascript:window.parent.openDialogForCM('business/ordermanage/preEditProposalNumber2?taskid=${taskid}&inscomcode=${carInsTaskInfo.inscomcode}&num=${carInsTaskInfo_index}');"><font style="color:white">修改</font></a></label>
				</#if>
			</td>
		</tr>
		<tr>
			<td class="bgg col-md-2">
				<label>商业险投保单号:</label>
			</td>
			<td class="col-md-10">
				<div calss="row">
					<label id="businessProposalFormNo${carInsTaskInfo_index}">${carInsTaskInfo.proposalInfo.businessProposalFormNo}</label>
				</div>
			</td>
		</tr>
		<tr>
			<td class="bgg col-md-2">
				<label>交强险投保单号:</label>
			</td>
			<td class="col-md-10">
				<div calss="row">
					<label id="strongProposalFormNo${carInsTaskInfo_index}">${carInsTaskInfo.proposalInfo.strongProposalFormNo}</label>
				</div>
			</td>
		</tr>
	</table>
