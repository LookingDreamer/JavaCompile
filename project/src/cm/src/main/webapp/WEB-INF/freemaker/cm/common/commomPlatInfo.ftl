<div class="row">
 	<div class="col-md-12">
		<table border="1px" class="hovertable col-md-12">
		    <tr>
		    	<td class="col-md-12" style="background-color:#8A8A8A;color:white;">
		    		<div class="row">
 						<div class="col-md-9">
 							平台查询信息
 						</div>
 						<div class="col-md-3" align="right">
 							<#if !(taskcode == "8" || taskcode == "18")>
								<label style="float:right;"><a href="javascript:window.parent.openDialogForCM('business/ordermanage/editCommonPlatinfo?taskid=${taskid}&inscomcode=${carInsTaskInfo.inscomcode}&num=${carInsTaskInfo_index}');"><font style="color:white">修改</font></a></label>
							</#if> 
 						</div>
 					</div>
		    	</td>
		    </tr>
		    <tr style="background-color:white;">
			    <td class="col-md-12">
			    	<div class="row">
 						<div class="col-md-12">
					    	 
					    </div>
						<div class="col-md-12">
					    	<table  class="hovertable col-md-12">
							    <tr>
								    <td class="bgg col-md-3">平台商业险起保日期：</td>
								    <td ><span id="systartdateInfo${carInsTaskInfo_index}">${carInsTaskInfo.otherInfo.systartdate}</span></td>
								    <td class="bgg col-md-3">商业险出险次数：</td>
			    					<td ><span id="id="syclaimtimesInfo${carInsTaskInfo_index}"">${carInsTaskInfo.otherInfo.syclaimtimes}</span></td>
							    </tr>
							    <tr>
								    <td class="bgg">平台商业险终止日期：</td>
								    <td ><span id="syenddateInfo${carInsTaskInfo_index}">${carInsTaskInfo.otherInfo.syenddate}</span></td>
								    <td class="bgg">交强险出险次数：</td>
			    					<td ><span id="jqclaimtimesInfo${carInsTaskInfo_index}">${carInsTaskInfo.otherInfo.jqclaimtimes}</span></td>
							    </tr>
							    <tr>
								    <td class="bgg">平台交强险起保日期：</td>
								    <td ><span id="jqstartdateInfo${carInsTaskInfo_index}">${carInsTaskInfo.otherInfo.jqstartdate}</span></td>
								    <td class="bgg">上年商业险投保公司：</td>
								    <td>
									    <span id="preinsPageInfo${carInsTaskInfo_index}">
									    	${carInsTaskInfo.otherInfo.preinsShortname}
									    </span>
								    </td>
							    </tr>
							    <tr>
								    <td class="bgg">平台交强险终止日期：</td>
								    <td ><span id="jqenddateInfo${carInsTaskInfo_index}">${carInsTaskInfo.otherInfo.jqenddate}</span></td>
                					<td class="bgg">无赔款不浮动原因：</td>
               						 <td>
				    					<span id="loyaltyreasons${carInsTaskInfo_index}">
                    					${carInsTaskInfo.otherInfo.loyaltyreasons}
                    					</span>
               	 					</td>
							    </tr>
							</table>
					    </div>
					</div>
			    </td>
		    </tr>
	    </table>
	</div>
</div>