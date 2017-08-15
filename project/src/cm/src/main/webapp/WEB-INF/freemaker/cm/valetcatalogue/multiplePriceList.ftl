<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>多方报价列表</title>
<link rel="stylesheet" href="${staticRoot}/css/modelinsurance/date.css">
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet" />
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "cm/valetcatalogue/multiplePriceList" ]);
</script>
<script type="text/javascript">
	requirejs([ "cm/valetcatalogue/editModelInfo" ]);
</script>
</head>
<body>
	<div class="main-container mar11">
		<div class="panel panel-default">
			<div id="headingAgent" role="tab" class="panel-heading">
				<h4 class="panel-title f6">代理人信息</h4>
			</div>
			<div aria-labelledby="headingAgent" role="tabpanel"
				class="panel-collapse collapse in" id="collapseAgent">
				<div class="panel-body">
					<div class="row">
						<div class="col-md-12">
							<table class="table table-bordered f2">
								<tbody>
									<tr>
										<td class="col-md-2 active">代理人姓名</td>
										<td class="col-md-2  c1 f2">620005685（吴昂）${agentInfo.agentname}</td>
										<td class="col-md-2 active">工号</td>
										<td class="col-md-2">${agentInfo.jobnum}</td>
									</tr>
									<tr>
										<td class="active">身份证号</td>
										<td>${agentInfo.idno}</td>
										<td class="active">联系电话</td>
										<td>${agentInfo.mobile}</td>
									</tr>
									<tr>
									    <td class="active">执业证/展业证号码</td>
										<td>${agentInfo.licenseno}</td>
										<td class="active">所属团队</td>
										<td>${agentInfo.teamname}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="myTabdiv">
			<div class="myTabdivfix">
				<ul id="mytab" class="nav nav-pills" 
					style="margin-bottom: 0px;">
					   <input type="hidden" id="taskid" value="${taskid}"/>
					 <#list carInsTaskInfoList as carInsTaskInfo>
					   <input type="hidden" id="li${carInsTaskInfo_index}" value="${carInsTaskInfo.inscomcode}"/>
					    <li  class="<#if (carInsTaskInfo_index = 0)>active</#if>" style="margin-right: 0px;"><a data-toggle="pill" href="#row${carInsTaskInfo.inscomcode}" onclick="getconfig('${taskid}','${carInsTaskInfo.inscomcode}')" >
							<h4>${carInsTaskInfo.inscomname}</h4>${carInsTaskInfo.deptinfo.comname}
					      </a>
					    </li>
					 </#list>
				</ul>
				<div
				style="width: 100%; height: 525px; overflow-y: auto; overflow-x: auto;"
				class="panel-body">
		       <div id="myTabContent" class="tab-content">
		        <#list carInsTaskInfoList as carInsTaskInfo> 
					<div id="row${carInsTaskInfo.inscomcode}" style="margin-bottom: 20px" class="tab-pane fade <#if (carInsTaskInfo_index = 0)>in active</#if>">
					 <div  class="row" >
						<div class="col-md-6">
							<table border="1px" class="hovertable col-md-12">
								<tr style="background-color: #383838; color: white;">
									<td colspan="2"><font size="3">投保单信息</font></td>
								</tr>
								<tr style="background-color: #8A8A8A; color: white;">
									<td class="col-md-12" colspan="2">
										<div class="row">
											<div class="col-md-9">车辆信息</div>
											<div class="col-md-3" align="right">
												<#if showEditFlag == "1">
 								                      <a href="javascript:window.parent.openLargeDialog('multiplelist/EditInsurancePolicyInfo?mark=${"editCarInfo"}&taskid=${carInsTaskInfo.carInfo.taskid}&num=${carInsTaskInfo_index}&inscomcode=${carInsTaskInfo.inscomcode}');"><font color="white">修改</font></a>
 							                     </#if>
											</div>
										</div>
									</td>
								</tr>
								<tr>
									<td class="bgg">投保地区：</td>
									<td>
										<span id="standardnamePageInfo">
	 								      ${carInsTaskInfo.carInfo.insprovincename}${carInsTaskInfo.carInfo.inscityname}
	 							       </span>
									</td>
								</tr>
								<tr>
									<td class="bgg">车牌：</td>
									<td>${carInsTaskInfo.carInfo.carlicenseno}</td>
								</tr>
								<tr>
									<td class="bgg">车主：</td>
									<td>
									<span id="carownernamePageInfo${carInsTaskInfo.inscomcode}">${carInsTaskInfo.carInfo.ownername}</span>
									</td>
								</tr>
								<tr>
									<td class="bgg">车型：</td>
									<td>
									   <div class="row">
					 						<div class="col-md-7">
					 							<span id="standardnamePageInfo1${carInsTaskInfo.inscomcode}">
					 								${carInsTaskInfo.carInfo.standardfullname}
					 							</span>
					 						</div>
 					                   </div>
									</td>
								</tr>
								<tr>
									<td class="bgg">发动机号：</td>
									<td>
									   <span id="enginenoPageInfo${carInsTaskInfo.inscomcode}">
				    	                  ${carInsTaskInfo.carInfo.engineno}
				                       </span>
									</td>
								</tr>
								<tr>
									<td class="bgg">车辆识别代号：</td>
									<td>
									   <span id="vincodePageInfo${carInsTaskInfo.inscomcode}">
				    	                  ${carInsTaskInfo.carInfo.vincode}
				                       </span>
									</td>
								</tr>
								<tr>
									<td class="bgg">车辆初登日期：</td>
									<td>
									   <span id="registdatePageInfo${carInsTaskInfo.inscomcode}">
				    	                  ${carInsTaskInfo.carInfo.registdate}
				                       </span>
									</td>
								</tr>
								<tr>
									<td class="bgg">是否过户车：</td>
									<td>
									   <span id="isTransfercarPageInfo${carInsTaskInfo.inscomcode}">
				    	                 <#if (carInsTaskInfo.carInfo.isTransfercar = 0)>否</#if>
									     <#if (carInsTaskInfo.carInfo.isTransfercar = 1)>是</#if>
				                       </span>
									</td>
								</tr>
								<tr>
									<td class="bgg">行驶区域：</td>
									<td>
									   <span id="drivingareavaluePageInfo${carInsTaskInfo.inscomcode}">
				    	                  ${carInsTaskInfo.carInfo.drivingareavalue}
				                       </span>
									</td>
								</tr>
								<tr style="background-color: #8A8A8A; color: white">
									<td class="col-md-12" colspan="2">
										<div class="row">
											<div class="col-md-9">关系人信息</div>
											<div class="col-md-3" align="right">
												<#if showEditFlag == "1">
	 							                     <a href="javascript:window.parent.openDialogForCM('multiplelist/EditInsurancePolicyInfo?mark=${"editRelationPersonInfo"}&taskid=${carInsTaskInfo.relationPersonInfo.taskid}&num=${carInsTaskInfo_index}&inscomcode=${carInsTaskInfo.inscomcode}');"><font color="white">修改</font></a>
	 						                    </#if>
											</div>
										</div>
									</td>
								<tr>
								<tr>
									<td class="bgg">被保险人：</td>
									<td>
									<span id="insuredNamePageInfo${carInsTaskInfo.inscomcode}">${carInsTaskInfo.relationPersonInfo.insuredname}</span>
				                    </td>
								</tr>
								<tr>
									<td class="bgg">投保人：</td>
									<td>
									<span id="applicantNamePageInfo${carInsTaskInfo.inscomcode}">${carInsTaskInfo.relationPersonInfo.applicantname}</span>
									</td>
								</tr>
								<tr>
									<td class="bgg">权益索赔人：</td>
									<td>
									<span id="personForRightNamePageInfo${carInsTaskInfo.inscomcode}">${carInsTaskInfo.relationPersonInfo.personForRightname}</span>
									</td>
								</tr>
								<tr>
									<td class="bgg">联系人：</td>
									<td>
									<span id="linkPersonNamePageInfo${carInsTaskInfo.inscomcode}">${carInsTaskInfo.relationPersonInfo.linkPersonname}</span>
									</td>
								</tr>
								<tr style="background-color: #8A8A8A; color: white">
									<td class="col-md-12" colspan="2">
										<div class="row">
											<div class="col-md-9">其他信息</div>
											<div class="col-md-3" align="right">
												<#if showEditFlag == "1">
	 							                        <a href="javascript:window.parent.openLargeDialog('multiplelist/EditInsurancePolicyInfo?mark=${"editOtherInfo"}&taskid=${carInsTaskInfo.otherInfo.taskid}&num=${carInsTaskInfo_index}&inscomcode=${carInsTaskInfo.inscomcode}');"><font color="white">修改</font></a>
	 						                    </#if>
											</div>
										</div>
									</td>
								<tr>
								<tr>
									<td class="bgg">是否指定驾驶人：</td>
									<td>
									 <#if carInsTaskInfo.otherInfo.Specifydriver != "否">
			    		                 <span id="noSpecifydriverPageInfo${carInsTaskInfo.inscomcode}">是</span>
			    	                 <#else>
			    		                 <span id="noSpecifydriverPageInfo${carInsTaskInfo.inscomcode}">${carInsTaskInfo.otherInfo.Specifydriver}</span>
			    	                 </#if>
									</td>
								</tr>
								<tr>
									<td class="bgg">商业险起保日期：</td>
									<td>
									<span id="businessstartdatePageInfo${carInsTaskInfo.inscomcode}">${carInsTaskInfo.otherInfo.businessstartdate}</span>
									</td>
								</tr>
								<tr>
									<td class="bgg">商业险终止日期：</td>
									<td>
									<span id="businessenddatePageInfo${carInsTaskInfo.inscomcode}">${carInsTaskInfo.otherInfo.businessenddate}</span>
									</td>
								</tr>
								<tr>
									<td class="bgg">交强险起保日期：</td>
									<td>
									<span id="compulsorystartdatePageInfo${carInsTaskInfo.inscomcode}">${carInsTaskInfo.otherInfo.compulsorystartdate}</span>
									</td>
								</tr>
								<tr>
									<td class="bgg">交强险终止日期：</td>
									<td>
									<span id="compulsoryenddatePageInfo${carInsTaskInfo.inscomcode}">${carInsTaskInfo.otherInfo.compulsoryenddate}</span>
									</td>
								</tr>
							</table>
						</div>
						<div class="col-md-6">
							<div class="insuranceTable${carInsTaskInfo_index}">
							  <table border="1px" class="hovertable col-md-12" style="margin-bottom:0px;" id="configtable${carInsTaskInfo.inscomcode}">
						      </table>
						    <div class="hovertable col-md-12" style="border:1px solid black;background-color:#99ccff;margin-bottom:1px;border-top:0px;">
								<table id="configtable1${carInsTaskInfo.inscomcode}">
								</table>
							</div>
						</div>
						</div>
					  </div>
					<div class="btnsbar">
						<button class="btn btn-primary passInsurance" type="button" name="${carInsTaskInfo.inscomcode}"
							id="${carInsTaskInfo.inscomcode}" title="马上投保" onclick="policySubmit(this,${carInsTaskInfo.relationPersonInfo.taskid},${agentInfo.jobnum});">马上投保</button>
						<!--<button class="btn btn-primary backEditInsurance" type="button" 
							id="backEditInsurance0" title="复制并创建新报价">复制并创建新报价</button>-->
						<button class="btn btn-primary refuseInsurance" type="button" onclick="getlipei(${taskid})"
							id="refuseInsurance0" title="查看理赔次数">查看理赔次数</button>
						<button class="btn btn-primary peopleDo" type="button" id="plant${carInsTaskInfo.inscomcode}"
							title="查上年险种" onclick="getplant(${carInsTaskInfo.relationPersonInfo.taskid});">查上年险种</button>
					</div>
				   </div>
				 </#list>
						<!--   <div class="taskimgs">
							  <button type="button" class="btn btn-success imgbtn">
								<span class="glyphicon glyphicon-picture" aria-hidden=""></span>&nbsp;影像信息
							  </button>
							  <div class="imglist" style="overflow: scroll; height: 300px">
								<a class="btn btn-success imgbtn" href=''><span
									class="glyphicon glyphicon-picture" aria-hidden="true"></span>&nbsp;添加影像</a>
								<div
									style="width: 100%; text-align: center; vertical-align: middle; line-height: 100%; border: 0px solid #ccc">
									<a href=''> <img
										style="width: 120px; height: 120px; text-align: center; vertical-align: middle; line-height: 120px; border: 1px solid #ccc"
										src=" " title="" width="130px" height="130px">
									</a>
								</div>
								<div class="caption" style="text-align: center;">
									<p>
										<a href=''
											style="display: block; width: 115px; word-break: keep-all; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;"
											title=""> </a>
									</p>
								</div>
							</div>-->
							<div class="modal fade" id="mymodal1">
							  <div class="modal-dialog">
							    <div class="modal-content">
							      <div class="modal-header">
							        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
							        <h4 class="modal-title">上一年险种信息</h4>
							      </div>
							      <div class="modal-body">
									<table id="table-getplant"></table>
							      </div>
							      <div class="modal-footer">
							        <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
							      </div>
							    </div><!-- /.modal-content -->
							  </div><!-- /.modal-dialog -->
							</div><!-- /.modal -->
					</div>
				 </div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="mymodallipei">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title">理赔信息</h4>
				</div>
				<div class="modal-body">
					  <table id="lipeitable" class="table table-bordered f2">
						  <tr>
								<td  class="col-md-3">上年商业险赔付率</td>
								<td  class="col-md-3">
								   <input type="text" class="form-control" id="claimrate" name="claimrate"  value="">
								</td>
						        <td  class="col-md-3">上年商业险理赔次数</td>
								<td  class="col-md-3">
								     <input type="text" class="form-control" id="claimtimes" name="claimtimes"  value="">
								</td>
						  </tr>
						  <tr>
						        <td  class="col-md-3">投保类型</td>
								<td  class="col-md-3">
								     <input type="text" class="form-control" id="firstinsuretype" name="firstinsuretype"  value="">
								</td>
						        <td  class="col-md-3">上年交强险赔付率</td>
								<td  class="col-md-3">
								     <input type="text" class="form-control" id="jqclaimrate" name="jqclaimrate"  value="">
								</td>
						  </tr>
						  <tr>
						        <td  class="col-md-3">上年交强险理赔次数</td>
								<td  class="col-md-3">
								     <input type="text" class="form-control" id="jqclaimtimes" name="jqclaimtimes"  value="">
								</td>
						        <td  class="col-md-3">上年交强险理赔金额</td>
								<td  class="col-md-3">
								     <input type="text" class="form-control" id="jqlastclaimsum" name="jqlastclaimsum"  value="">
								</td>
						  </tr>
						  <tr>
						        <td  class="col-md-3">上年商业险理赔金额</td>
								<td  class="col-md-3">
								     <input type="text" class="form-control" id="lastclaimsum" name="lastclaimsum"  value="">
								</td>
						        <td  class="col-md-3">交通违规次数</td>
								<td  class="col-md-3">
								      <input type="text" class="form-control" id="trafficoffence" name="trafficoffence"  value="">
								</td>
						  </tr>
						  <tr>
						        <td  class="col-md-3">交通违规系数</td>
								<td  class="col-md-3">
								     <input type="text" class="form-control" id="trafficoffencediscount" name="trafficoffencediscount"  value="">
								</td>
						  </tr>
					   </table>
				</div>
				<div class="modal-footer">
					<!--<button type="button" class="btn btn-default" data-dismiss="modal" id="callbackmodify">返回修改</button>-->
					<button type="button" class="btn btn-primary" id="confirmpid">确认</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div>
</body>
</html>

