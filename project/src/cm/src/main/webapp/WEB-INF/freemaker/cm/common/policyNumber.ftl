<style type="text/css">
table{
	width: 100%;
}
table.separate{
	width: 100%;
	border-collapse:separate;
	border-spacing: 2px;
	/* height: 100%; */
	/* border: 1px solid gray; */
}

td.rect {
	border: 1px solid gray;
	padding :2px;
	width: 50%;
}

td.short  {
	padding: 2px;
	border: 1px solid #bbbbbb;
	text-align:right;
	width: 15%;
	background: #F2F2F2;
}

td.normal{
	border: 1px solid gray;
	padding:3px;
	
}

td.long {
	padding: 2px;
	border: 1px solid #bbbbbb;
	width: 85%;
	border-left: 2px solid #6B6B6B;
} 

td.title {
	color:white;
	background-color: #6B6B6B;
	text-align:left;
}

td.opt {
	color:white;
	background-color: #6B6B6B;
	text-align:right;
}

.innertable {
	border: none;
	margin:0px;
}

.innertable td {
	border:none; 
	height:100%;  
}


.padding-6 {
	padding: 6px;
}
</style>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>
<div id="policyNum">
	<table border="1px" class="hovertable col-md-12">
		<tr>
			<td class="col-md-12" colspan="2" style="background-color:#8A8A8A;color:white;">
				<label>保单号</label>
				<#if !(taskcode == "24" || taskcode == "23")><#--配送or打单任务不显示修改按钮-->
					<label style="float:right;"><a href="javascript:window.parent.openDialogForCM('business/ordermanage/preEditPolicyNumber?taskid=${taskid}&inscomcode=${inscomcode}');"><font style="color:white">修改</font></a></label>
				</#if>&nbsp;
			</td>
		</tr>
		<tr>
			<td class="bgg col-md-2">
				<label>商业险保单号:</label>
			</td>
			<td class="col-md-10">
				<label id="businessPolicyNum">${policyInfo.businessPolicyNum }</label>
			</td>
		</tr>
		<tr>
			<td class="bgg col-md-2">
				<label>交强险保单号:</label>
			</td>
			<td class="col-md-10">
				<label id="strongPolicyNum">${policyInfo.strongPolicyNum}</label>
			</td>
		</tr>
	</table>
</div>