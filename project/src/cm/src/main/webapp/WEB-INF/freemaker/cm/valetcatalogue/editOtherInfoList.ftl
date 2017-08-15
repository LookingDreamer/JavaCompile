<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>修改其他信息弹出窗口</title>
		<link href="${staticRoot}/css/lib/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet"/>
		<link href="${staticRoot}/css/lib/fuelux.min.css" rel="stylesheet"/>
		<link href="${staticRoot}/css/lib/core/module.css" rel="stylesheet"/>
		<link href="${staticRoot}/css/appreset.css" rel="stylesheet"/>
		<style type="text/css">
			span.feildtitle {
				font-weight:bold;
				color:#34495E;
			}
			body {font-size: 14px;}
		</style>
		<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
			src="${staticRoot}/js/lib/require.js?ver=${jsver}">
		</script>
	</head>
	<body>
		<!--引入修改其他信息弹出窗口js文件-->
		<script type="text/javascript">
			requirejs([ "cm/valetcatalogue/editOtherInfoList" ],function() {
				require(["jquery", "bootstrapdatetimepicker", "zTree", "zTreecheck", "flat-ui", "bootstrap-table", "bootstrap",
					"bootstrapTableZhCn", "bootstrapdatetimepickeri18n","public"], function ($) {
					$(function() {
						initEditOtherInfoScript();
					});
				});
			});
		</script>
		<!--修改其他信息弹出框-->
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h6 class="modal-title">修改其他信息</h6>
		</div>
		<div class="modal-body">
			<form class="form-inline" role="form" id="otherInfoEditForm">
			  <input type="hidden" id="inscomcode" name="inscomcode" value="${otherInfo.inscomcode}"/>
			  <input type="hidden" id="carinfoId" name="carinfoId" value="${otherInfo.carinfoId}"/>
			  <input type="hidden" id="taskid" name="taskid" value="${otherInfo.taskid}"/>
			  <!-- 选项卡下标 -->
			  <input type="hidden" id="num" name="num" value="${num}"/>
			  <table class="table table-bordered" style="margin-bottom:0px">
				<tr>
					<td colspan="4" class="col-md-12 active">
						<span class="feildtitle">保险期间</span>
					</td>
				</tr>
				<tr id="busiDate" <#if !otherInfo.hasbusi>style="display:none;"</#if>>
					<td class="active col-md-2">商业险起保日期：</td>
					<td class="col-md-4">
						<!--<input type="text" name="businessstartdate" class="form-control col-md-12" value="${otherInfo.businessstartdate}" placeholder="">-->
						<div class="form-group col-md-12">
							<div class="input-group date form_datetime col-md-12"
								data-date-format="yyyy-mm-dd" data-link-field="businessstartdate">
								<input class="form-control changeEnddate" style="color:black;" type="text" id="businessstartdateTxt" readonly value="${otherInfo.businessstartdate}"/>
								<span class="input-group-addon"> 
									<span class="glyphicon glyphicon-remove"></span>
								</span> 
								<span class="input-group-addon"> 
									<span class="glyphicon glyphicon-th"></span>
								</span>
							</div>
							<input type="hidden" id="businessstartdate" name="businessstartdate" value="${otherInfo.businessstartdate!""}"/> 
						</div>
					</td>
					<td class="active col-md-2">商业险终止日期：</td>
					<td class="col-md-4">
						<input type="text" style="color:black;" id="businessenddate" name="businessenddate" class="form-control" 
							value="${otherInfo.businessenddate!""}" readonly="readonly" placeholder=""/>
						<!--
						<div class="form-group col-md-12">
							<div class="input-group date form_datetime col-md-12"
								data-date-format="yyyy-mm-dd" data-link-field="businessenddate">
								<input class="form-control" style="color:black;" type="text" id="businessenddateTxt" readonly value="${otherInfo.businessenddate}"/>
								<span class="input-group-addon"> 
									<span class="glyphicon glyphicon-remove"></span>
								</span> 
								<span class="input-group-addon"> 
									<span class="glyphicon glyphicon-th"></span>
								</span>
							</div>
							<input type="hidden" id="businessenddate" name="businessenddate" value="${otherInfo.businessenddate!""}"/> 
						</div>
						-->
					</td>
				</tr>
				<tr id="stroDate" <#if !otherInfo.hasstr>style="display:none;"</#if>>
					<td class="active col-md-2">交强险起保日期：</td>
					<td class="col-md-4">
						<!--<input type="text" name="compulsorystartdate" class="form-control col-md-12" value="${otherInfo.compulsorystartdate}" placeholder="">-->
						<div class="form-group col-md-12">
							<div class="input-group date form_datetime col-md-12"
								data-date-format="yyyy-mm-dd" data-link-field="compulsorystartdate">
								<input class="form-control changeEnddate" style="color:black;" type="text" id="compulsorystartdateTxt" readonly value="${otherInfo.compulsorystartdate}"/>
								<span class="input-group-addon"> 
									<span class="glyphicon glyphicon-remove"></span>
								</span> 
								<span class="input-group-addon"> 
									<span class="glyphicon glyphicon-th"></span>
								</span>
							</div>
							<input type="hidden" id="compulsorystartdate" name="compulsorystartdate" value="${otherInfo.compulsorystartdate!""}"/> 
						</div>
					</td>
					<td class="active col-md-2">交强险终止日期：</td>
					<td class="col-md-4">
						<input type="text" style="color:black;" id="compulsoryenddate" name="compulsoryenddate" class="form-control" 
							value="${otherInfo.compulsoryenddate!""}" readonly="readonly" placeholder=""/>
						<!--
						<div class="form-group col-md-12">
							<div class="input-group date form_datetime col-md-12"
								data-date-format="yyyy-mm-dd" data-link-field="compulsoryenddate">
								<input class="form-control" style="color:black;" type="text" id="compulsoryenddateTxt" readonly value="${otherInfo.compulsoryenddate}"/>
								<span class="input-group-addon"> 
									<span class="glyphicon glyphicon-remove"></span>
								</span> 
								<span class="input-group-addon"> 
									<span class="glyphicon glyphicon-th"></span>
								</span>
							</div>
							<input type="hidden" id="compulsoryenddate" name="compulsoryenddate" value="${otherInfo.compulsoryenddate!""}"/> 
						</div>
						-->
					</td>
				</tr>
			  </table>
			  <table class="table table-bordered" style="margin-bottom:0px">
				<tr>
					<td colspan="4" class="col-md-12 active">
						<span class="feildtitle">投保信息</span>
					</td>
				</tr>
				<tr>
					<td class="active col-md-3">投保公司：</td>
					<td colspan="3" class="col-md-9">
						<div class="form-group col-md-4 form-inline">
							<input type="hidden" id="preinscode" name="preinscode" value="${otherInfo.preinscode}"/>
					        <input type="text"	class="form-control"  id="preinsname"  value="${otherInfo.preinsShortname}"/>
						</div>
					</td>
				</tr>
				<tr>
					<td class="active col-md-3">出单网点：</td>
					<td colspan="3" class="col-md-9">
						<#if deptListInfo.status == "success">
							<select name="deptCode" id="deptCode" style="WIDTH: 183px" class="form-control">
								<#list deptListInfo.body as deptcodeitem>
									<option value="${deptcodeitem.comcode}" id="dept${deptcodeitem.comcode}" 
										<#if deptcodeitem.comcode == otherInfo.deptComCode>selected</#if>>
										${deptcodeitem.shortname}
									</option>
								</#list>
							</select>
						<#else>
							出单网点选项加载失败
						</#if>
					</td>
				</tr>
				
			  </table>
			  <table class="table table-bordered" style="margin-bottom:0px">
				<tr>
					<td colspan="4" class="col-md-12 active">
						<span class="feildtitle">驾驶员信息</span>
					</td>
				</tr>
				<tr>
					<td class="active col-md-2">是否指定驾驶人：</td>
					<td colspan="3" class="col-md-10">
						<div class="row">
							<div class="col-md-4">
								<select id="pointspecifydriver" name="pointspecifydriver" class="form-control">
							      <option value="1" <#if otherInfo.Specifydriver != "否">selected="selected"</#if>>是</option> 
							      <option value="0" <#if otherInfo.Specifydriver == "否">selected="selected"</#if>>否</option> 
							    </select>
							</div>
							<div class="col-md-4">
								<select id="specifydriver" name="specifydriver" class="form-control related" <#if otherInfo.Specifydriver == "否">disabled</#if>>
									<option value="">请选择</option> 
								  	<#list [0,1,2] as num>
										<#if (driversInfo?size>num)>
							  		 		<option value="${num+1}_${driversInfo[num].specifyDriverId}" <#if otherInfo.Specifydriver == driversInfo[num].specifyDriverId>selected="selected"</#if>>驾驶人${num+1}</option>
							  	  		<#else>
							  	  			<option value="${num+1}_newSpecifydriver">驾驶人${num+1}</option>
							  	  		</#if>
							  	  	</#list>
							    </select>
							</div>
						</div>
					</td>
				</tr>
			  </table>
			  <ul id="myTab" class="nav nav-tabs" role="tablist">
			     <#list [0,1,2] as num>
			  		<li <#if (num = 0)>class="active"</#if>><a href="#driver${num+1}" role="tab" data-toggle="tab">驾驶人${num+1}</a></li>
			  	 </#list>
			  </ul>
			  <div id="myTabContent" class="tab-content">
			  	<#list [0,1,2] as num> 
					 <#if (driversInfo?size>num)>
					 	<div class="tab-pane fade <#if (num = 0)>in active</#if>" id="driver${num+1}">
							 <table class="table table-bordered" style="margin-bottom:0px">
								<tr>
									<td class="active col-md-2">姓名：</td>
									<td class="col-md-4">
										<input type="hidden" id="driversInfo_${num}_id" name="driversInfo[${num}].id" value="${driversInfo[num].driverId}"/>
										<input type="hidden" id="driversInfo_${num}_specifyDriverId" name="driversInfo[${num}].specifyDriverId" value="${driversInfo[num].specifyDriverId}"/>
										<input type="text" id="driversInfo_${num}_name" name="driversInfo[${num}].name" class="form-control col-md-12 related" value="${driversInfo[num].driverName}" placeholder="" 
										<#if otherInfo.Specifydriver == "否">disabled</#if>/>
									</td>
									<td class="active col-md-2">出生日期：</td>
									<td class="col-md-4">
										<!--<input type="text" name="driversInfo[${num}].birthday" class="form-control col-md-12 related" value="${driversInfo[num].driverBirthday}" placeholder="" 
										<#if otherInfo.Specifydriver == "否">disabled</#if>>-->
										<div class="form-group col-md-12 related" <#if otherInfo.Specifydriver == "否">style="display:none"</#if>>
											<div class="input-group date form_datetime col-md-12"
												data-date-format="yyyy-mm-dd" data-link-field="driversInfo_${num}_birthday">
												<input class="form-control related" style="color:black;" type="text" id="driversInfo_${num}_birthdayTxt" readonly value="${driversInfo[num].driverBirthday}"/>
												<span class="input-group-addon"> 
													<span class="glyphicon glyphicon-remove"></span>
												</span> 
												<span class="input-group-addon"> 
													<span class="glyphicon glyphicon-th"></span>
												</span>
											</div>
											<input class="related" type="hidden" id="driversInfo_${num}_birthday" name="driversInfo[${num}].birthday" value="${driversInfo[num].driverBirthday}"/> 
										</div>
									</td>
								</tr>
								<tr>
									<td class="active">性别：</td>
									<td>
										<div class="row">
											<div class="col-md-8">
												<select id="driversInfo_${num}_gender" name="driversInfo[${num}].gender" class="form-control related" <#if otherInfo.Specifydriver == "否">disabled</#if>> 
											      <option value="">请选择</option>
											      	<#list GenderAndLicenseTypeCodeList.GenderList as Genderitem>
														<option value="${Genderitem.codevalue}" 
															<#if Genderitem.codevalue == driversInfo[num].driverGender>selected</#if>>
															${Genderitem.codename}
														</option>
													</#list>
											    </select>
											</div>
										</div>
									</td>
									<td class="active">驾驶证类型：</td>
									<td>
										<select id="driversInfo_${num}_licensetype" name="driversInfo[${num}].licensetype" class="form-control related" <#if otherInfo.Specifydriver == "否">disabled</#if>> 
										 	 <option value="">请选择</option> 
										     <#list GenderAndLicenseTypeCodeList.LicenseTypeList as LicenseTypeitem>
												<option value="${LicenseTypeitem.codevalue}" 
													<#if LicenseTypeitem.codevalue == driversInfo[num].driverLicensetype>selected</#if>>
													${LicenseTypeitem.codename}
												</option>
											</#list> 
									    </select>
									</td>
								</tr>
								<tr>
									<td class="active">驾驶证号：</td>
									<td>
										<input type="text" id="driversInfo_${num}_licenseno" name="driversInfo[${num}].licenseno" class="form-control col-md-12 related" value="${driversInfo[num].driverLicenseno}" placeholder="" 
										<#if otherInfo.Specifydriver == "否">disabled</#if>/>
									</td>
									<td class="active">发照日期：</td>
									<td>
										<!--<input type="text" name="driversInfo[${num}].licensedate" class="form-control col-md-12 related" value="${driversInfo[num].driverLicensedate}" placeholder="" 
										<#if otherInfo.Specifydriver == "否">disabled</#if>>-->
										<div class="form-group col-md-12 related" <#if otherInfo.Specifydriver == "否">style="display:none"</#if>>
											<div class="input-group date form_datetime col-md-12"
												data-date-format="yyyy-mm-dd" data-link-field="driversInfo_${num}_licensedate">
												<input class="form-control related" style="color:black;" type="text" id="driversInfo_${num}_licensedateTxt" readonly value="${driversInfo[num].driverLicensedate}"/>
												<span class="input-group-addon"> 
													<span class="glyphicon glyphicon-remove"></span>
												</span> 
												<span class="input-group-addon"> 
													<span class="glyphicon glyphicon-th"></span>
												</span>
											</div>
											<input class="related" type="hidden" id="driversInfo_${num}_licensedate" name="driversInfo[${num}].licensedate" value="${driversInfo[num].driverLicensedate}"/> 
										</div>
									</td>
								</tr>
							  </table>
						</div>
					 <#else>
					 	<div class="tab-pane fade <#if (num = 0)>in active</#if>" id="driver${num+1}">
							<table class="table table-bordered" style="margin-bottom:0px">
								<tr>
									<td class="active col-md-2">姓名：</td>
									<td class="col-md-4">
										<input type="hidden" id="driversInfo_${num}_id" name="driversInfo[${num}].id" value=""/>
										<input type="hidden" id="driversInfo_${num}_specifyDriverId" name="driversInfo[${num}].specifyDriverId" value=""/>
										<input type="text" id="driversInfo_${num}_name" name="driversInfo[${num}].name" class="form-control col-md-12 related" value="" placeholder="" 
										<#if otherInfo.Specifydriver == "否">disabled</#if>/>
									</td>
									<td class="active col-md-2">出生日期：</td>
									<td class="col-md-4">
										<!--<input type="text" name="driversInfo[${num}].birthday" class="form-control col-md-12 related" value="" placeholder="" 
										<#if otherInfo.Specifydriver == "否">disabled</#if>>-->
										<div class="form-group col-md-12 related" <#if otherInfo.Specifydriver == "否">style="display:none"</#if>>
											<div class="input-group date form_datetime col-md-12"
												data-date-format="yyyy-mm-dd" data-link-field="driversInfo_${num}_birthday">
												<input class="form-control related" style="color:black;" type="text" id="driversInfo_${num}_birthdayTxt" readonly value=""/>
												<span class="input-group-addon"> 
													<span class="glyphicon glyphicon-remove"></span>
												</span> 
												<span class="input-group-addon"> 
													<span class="glyphicon glyphicon-th"></span>
												</span>
											</div>
											<input class="related" type="hidden" id="driversInfo_${num}_birthday" name="driversInfo[${num}].birthday" value=""/> 
										</div>
									</td>
								</tr>
								<tr>
									<td class="active">性别：</td>
									<td>
										<div class="row">
											<div class="col-md-8">
												<select id="driversInfo_${num}_gender" name="driversInfo[${num}].gender" class="form-control related" <#if otherInfo.Specifydriver == "否">disabled</#if>> 
												    <option value="">请选择</option> 
												    <#list GenderAndLicenseTypeCodeList.GenderList as Genderitem>
														<option value="${Genderitem.codevalue}">
															${Genderitem.codename}
														</option>
													</#list>
											    </select>
											</div>
										</div>
									</td>
									<td class="active">驾驶证类型：</td>
									<td>
										<select id="driversInfo_${num}_licensetype" name="driversInfo[${num}].licensetype" class="form-control related" <#if otherInfo.Specifydriver == "否">disabled</#if>> 
									      	<option value="">请选择</option> 
									      	<#list GenderAndLicenseTypeCodeList.LicenseTypeList as LicenseTypeitem>
												<option value="${LicenseTypeitem.codevalue}">
													${LicenseTypeitem.codename}
												</option>
											</#list>  
									    </select>
									</td>
								</tr>
								<tr>
									<td class="active">驾驶证号：</td>
									<td>
										<input type="text" id="driversInfo_${num}_licenseno" name="driversInfo[${num}].licenseno" class="form-control col-md-12 related" value="" placeholder="" 
										<#if otherInfo.Specifydriver == "否">disabled</#if>/>
									</td>
									<td class="active">发照日期：</td>
									<td>
										<!--<input type="text" name="driversInfo[${num}].licensedate" class="form-control col-md-12 related" value="" placeholder="" 
										<#if otherInfo.Specifydriver == "否">disabled</#if>>-->
										<div class="form-group col-md-12 related" <#if otherInfo.Specifydriver == "否">style="display:none"</#if>>
											<div class="input-group date form_datetime col-md-12"
												data-date-format="yyyy-mm-dd" data-link-field="driversInfo_${num}_licensedate">
												<input class="form-control related" style="color:black;" type="text" id="driversInfo_${num}_licensedateTxt" readonly value=""/>
												<span class="input-group-addon"> 
													<span class="glyphicon glyphicon-remove"></span>
												</span> 
												<span class="input-group-addon"> 
													<span class="glyphicon glyphicon-th"></span>
												</span>
											</div>
											<input class="related" type="hidden" id="driversInfo_${num}_licensedate" name="driversInfo[${num}].licensedate" value=""/> 
										</div>
									</td>
								</tr>
							  </table>
						  </div>
					 </#if>
			  	</#list>
			   </div>
			  <div class="row">
			 	 <div class="col-md-6">
			 		<button class="btn btn-default" type="button" id="makesure" title="确定">&nbsp;&nbsp;确定&nbsp;&nbsp;</button>
			 	 </div>
			 	 <div class="col-md-6" align="right">
			 		<button class="btn btn-default" type="button" title="取消" data-dismiss="modal">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>
			 	 </div>
			  </div>
			</form>
		</div>
		<!--供应商选择弹出框-->
		<div id="showpic" class="modal fade"  role="dialog" aria-labelledby="myModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close closeshowpic" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h6 class="modal-title" id="gridSystemModalLabel">选择供应商</h6>
		      </div>
		      <div class="modal-body" style="overflow:auto; height:400px;">
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
	</body>
</html>
