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

				<!-- 支付信息 -->
				<table border="1px" class="hovertable col-md-12" >
					<tr style="background-color:#8A8A8A;color:white;">
						<td class="col-md-12" colspan="12"  >
							<div class="row">
								<div class="col-md-9">支付信息</div>
								<div class="col-md-3" align="right">
 									<a href="javascript:showdetail();"><font color="white">查看详情</font></a>
								</div>
							</div>
						</td>	
					</tr>
					<tr>
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
						<td  class="col-md-2" style="background-color:#E5E5E5" align="center">
							支付状态
						</td>
					</tr>
					<tr>
						<td align="center">${paymentinfo.paymentransaction}</td>
						<td align="center">${paymentinfo.creatime}</td>
						<td align="center">${paymentinfo.tradeno}</td>
						<td align="center">${paymentinfo.paymentmethod}</td>
						<td align="center">${paymentinfo.refundtype}</td>
						<td align="center">${paymentinfo.amount}</td>
						<td align="center">${paymentinfo.result}</td>
					</tr> 
					<!--<tr>
						<td  class="bgg col-md-2 " >
							<label>支付方式:</label>
						</td>
						<td  class=" col-md-10 ">
							<label>${paymentinfo.paymentmethod}</label>
						</td>
					</tr>
					<tr>
						<td  class="bgg col-md-2 " >
							<label>支付跟踪号:</label>
						</td>
						<td  class=" col-md-10 ">
							<label>${paymentinfo.insurecono}</label>
						</td>
					</tr>
					<tr>
						<td  class="bgg col-md-2 " >
							<label>支付流水号:</label>
						</td>
						<td  class=" col-md-10 ">
							<label>${paymentinfo.tradeno}</label>
						</td>
					</tr>-->
					<!-- 
						<tr>
							<td  class="bgg col-md-2 " >
								<label>校验码:</label>
							</td>
							<td  class=" col-md-10 ">
								<label>${paymentinfo.checkcode}</label>
							</td>
						</tr>
					-->
					<!--<tr>
						<td  class="bgg col-md-2 " >
							<label>保费:</label>
						</td>
						<td  class=" col-md-10 ">
							<label>${paymentinfo.premium}</label>
						</td>
					</tr>
					<tr>
						<td  class="bgg col-md-2 " >
							<label>快递费:</label>
						</td>
						<td  class=" col-md-10 ">
							<label>${paymentinfo.totaldeliveryamount}</label>
						</td>
					</tr>
					<tr>
						<td  class="bgg col-md-2 " >
							<label>支付手续费:</label>
						</td>
						<td  class=" col-md-10 ">
							<label>${paymentinfo.paypoundage}</label>
						</td>
					</tr>
					<tr>
						<td  class="bgg col-md-2 " >
							<label>总计:</label>
						</td>
						<td  class=" col-md-10 ">
							<label>${paymentinfo.total}</label>
						</td>
					</tr>-->
				</table>
				
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
									<tr>
										<td class="bgg col-md-5">保费:</td>
										<td >${paymentinfo.premium}</td>
									</tr>
									<tr>
										<td class="bgg col-md-5"">快递费:</td>
										<td>${paymentinfo.totaldeliveryamount}</td>
									</tr>
									<tr>
										<td class="bgg col-md-5"">支付手续费:</td>
										<td>${paymentinfo.paypoundage}</td>
									</tr>
									<tr>
										<td class="bgg col-md-5"">总计:</td>
										<td>${paymentinfo.total}</td>
									</tr>
									
	          					</table>
	        				</div>
	      				</div>
	      				<div class="modal-footer">
	        				<button type="button" class="btn btn-default closeshowpic">关闭</button>
	      				</div>
	    			</div>
	    		  </div>
				</div>
	<div id="" class="modal fade"  role="dialog" aria-labelledby="myModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close closeshowpic" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="gridSystemModalLabel_1">选择供应商</h4>
	      </div>
	      <div class="modal-body" style="overflow:auto; height:200px;">
	        <div class="container-fluid">
	          <div class="row">
				<ul id="treeDemo" class="ztree"></ul>
	          </div>
	        </div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default closeshowpic">关闭</button>
	      </div>
	    </div>
	  </div>
	</div>