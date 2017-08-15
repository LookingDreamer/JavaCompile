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
	vertical-align:top;
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


label.bluemessage{
	font:normal;
	font-size:18px;
	color:#0066FF;
}

</style>

				<table class="separate" id="task_summary">
					<tr>
						<td class="rect">
								<label id="tcarlicenseno" class="bluemessage">${tasksummary.carlicenseno }</label>&nbsp;<label>被保人：</label><label id="tinsuredname">${tasksummary.insuredname }</label>
								<br><label>投保公司：</label><label id="inscom">${tasksummary.inscom }</label>
								<br><div class="c-display">
									<label>网点：</label><label id="tdeptcode">${tasksummary.deptname }</label>
									<br><label>商业险：${tasksummary.bipremium }</label><label id=""></label>&nbsp;<label>投保单号：</label><label id="tbusinesspolicyno">${tasksummary.biproposalno }</label>
									<br><label>交强险：${tasksummary.cipremium }</label><label id=""></label>&nbsp;<label>投保单号：</label><label id="">${tasksummary.ciproposalno }</label>
								</div>
								<label>用户备注：</label><label id="tnoti">${tasksummary.noti }</label>
						</td>
						<td class="rect">
								<label id="tagent" class="bluemessage">${tasksummary.agentcode }(${tasksummary.agentname })</label>&nbsp;<label>所属团队：</label><label id="tteam">${tasksummary.agentteam }</label>
								<br><label>电话：</label><label id="tmobile">${tasksummary.mobile }</label>
								<br><label>资格证号：</label><label id="tcgfns">${tasksummary.cgfns }</label>
								<br><label>身份证：</label><label id="tidno">${tasksummary.idno }</label>
								<br><label>执业证/展业证号码：</label><label id="tlicenseno">${tasksummary.licenseno }</label>
						</td>
					</tr>
					<tr>
						<td colspan="2" class="rect">
							<div class="rect">
							<label id="torderstatus">${tasksummary.orderstatus }</label>&nbsp;<label>创建时间：</label><label id="tcreatetime">${tasksummary.createtime }</label>&nbsp;<label>订单有效期：</label><label id=""></label>
							</div>

						</td>
					</tr>
				</table>
<!--用户备注公共页面样式表-->
<!-- 
<style type="text/css">
	table.hovertable {
		font-size:11px;
		color:#333333;
		border-width: 1px;
		border-color: #999999;
		border-collapse: collapse;
		margin-bottom:5px;
		margin-top:5px;
	}
	table.hovertable tr { 
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
</style>

-->

<!--用户备注公共页面-->
<!--
<div class="row">
 	<div class="col-md-12">
		<table border="1px" class="hovertable col-md-12">
		    <tr>
		    	<td class="col-md-12" colspan="2"  style="background-color:#8A8A8A;color:white;">
		    		备注信息
		    	</td>
		    </tr>
		    <tr>
			    <td class="bgg col-md-2">给用户的备注:</td>
			    <td class="col-md-10">
			    	<div class="row">
 						<div class="col-md-10">
 						
 						</div>
 						<div class="col-md-2" align="right">
 							<a href="javascript:window.parent.openDialog('common/remarksinfo/showeditremark?mark=${"editUserRemark"}');">修改</a>
 						</div>
 					</div>
			    </td>
		    </tr>
		    <tr>
			    <td class="bgg col-md-2">给操作员的备注:</td>
			    <td class="col-md-10">
			    	<div class="row">
 						<div class="col-md-10">
 						
 						</div>
 						<div class="col-md-2" align="right">
 							<a href="javascript:window.parent.openDialog('common/remarksinfo/showeditremark?mark=${"addOperatorRemark"}');">新增</a>
 						</div>
 					</div>
			    </td>
		    </tr>
	    </table>
	</div>
</div>

 -->