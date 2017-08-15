<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>修改关系人信息</title>
	<!--<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>-->
	<script type="text/javascript">
		requirejs([ "cm/valetcatalogue/editRelationInfo" ],function() {
			require(["jquery","bootstrap-table", "bootstrap","bootstrapTableZhCn","additionalmethods","public"],function ($) {
				$(function(){
					initEditRelationPersonInfoScript();
				});
			});
		});
	</script>
	<style type="text/css">
		#sure{
			position: relative;
			left: 310px;
		}
		#cancel{
			position: relative;
			left: 370px;
		}
		span.feildtitle {
			font-weight:bold;
			color:#34495E;
		}
		body {font-size: 14px;}
	</style>
</head>
<body>
<form id="editRelationPersonInfoForm">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<h6 class="modal-title">修改关系人信息</h6>
	</div>
	<input type="hidden" name="taskid" value="${relationPersonInfo.taskid}"/>
	<input type="hidden" name="inscomcode"  id="inscomcode" value="${relationPersonInfo.inscomcode}"/>
	<!-- 选项卡下标 -->
	<input type="hidden" id="num" name="num" value="${num}"/>
	<div class="modal-body">
		<!--被保人信息-->
		<div class="panel panel-default" style="margin-bottom:0px">
			<div class="panel-heading">
				<div class="row">
					<div class="col-md-12">
						<span class="feildtitle">被保人信息</span>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table class="table table-bordered" style="margin-bottom:0px">
						<tr>
							<td class="col-md-2">姓名:</td>
							<td class="col-md-4">
								<input class="form-control" type="hidden" id="insuredid" name="insuredid" value="${relationPersonInfo.insuredid}"/>
								<input class="form-control required" type="text" id="insuredName" name="insured.name" value="${relationPersonInfo.insuredname}"/>
							</td>
							<td>性别:</td>
							<td>
								<select class="form-control" name="insured.gender">
									<#list relationPersonInfo.GenderList as Genderitem>
										<option value="${Genderitem.codevalue}" 
											<#if Genderitem.codevalue == relationPersonInfo.insuredgender>selected</#if>>
											${Genderitem.codename}
										</option>
									</#list>
								</select>
							</td>
						</tr>
						<tr>
							<td>证件类型:</td>
							<td>
								<select class="form-control idcardtype" name="insured.idcardtype" id="insuredidcardtype">
									<#list relationPersonInfo.CertKindList as CertKinditem>
										<option value="${CertKinditem.codevalue}" 
											<#if CertKinditem.codevalue == relationPersonInfo.insuredidcardtype>selected</#if>>
											${CertKinditem.codename}
										</option>
									</#list>
								</select>
							</td>
							<td>证件号码:</td>
							<td>
								<input class="form-control required" type="text" id="insuredidcardno" name="insured.idcardno" value="${relationPersonInfo.insuredidcardno}"/>
							</td>
						</tr>
						<tr>
							<td>手机号码:</td>
							<td>
								<input class="form-control required phone" type="text" id="insuredcellphone" name="insured.cellphone" value="${relationPersonInfo.insuredcellphone}"/>
							</td>
							<td>E-mail:</td>
							<td>
								<input class="form-control email" type="text" id="insuredemail" name="insured.email" value="${relationPersonInfo.insuredEmail}"/>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<!--投保人信息-->
		<div class="panel panel-default" style="margin-bottom:0px">
			<div class="panel-heading">
				<div class="row">
					<div class="col-md-12">
						<span class="feildtitle">投保人信息</span>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
				  	<div id="form1">
						<table class="table table-bordered" style="margin-bottom:0px">
							<tr>
								<td class="col-md-2">与被保人一致:</td>
								<td class="col-md-4" colspan="3">
									<input class="form-control" type="hidden" id="applicantid" name="applicantid" value="${relationPersonInfo.applicantid}"/>
									<input class="applicantyes" type="radio" name="applicantflag" value="true" id="yes1" <#if (relationPersonInfo.applicantid == relationPersonInfo.insuredid)>checked</#if>/>是&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="applicantflag" value="false" id="no1" <#if (relationPersonInfo.applicantid != relationPersonInfo.insuredid)>checked</#if>/>否
								</td>
							</tr>
							<tr>
								<td class="col-md-2" >姓名:</td>
								<td class="col-md-4" >
									<input class="form-control" type="text" id="applicantName" name="applicant.name" value="${relationPersonInfo.applicantname}"/>
								</td>
								<td>性别:</td>
								<td>
									<select name="applicant.gender" class="form-control">
										<#list relationPersonInfo.GenderList as Genderitem>
											<option value="${Genderitem.codevalue}" 
												<#if Genderitem.codevalue == relationPersonInfo.applicantgender>selected</#if>>
												${Genderitem.codename}
											</option>
										</#list>
									</select>
								</td>
							</tr>
							<tr>
								<td>证件类型:</td>
								<td>
									<select name="applicant.idcardtype" class="form-control idcardtype" id="applicantidcardtype">
										<#list relationPersonInfo.CertKindList as CertKinditem>
											<option value="${CertKinditem.codevalue}" 
												<#if CertKinditem.codevalue == relationPersonInfo.applicantidcardtype>selected</#if>>
												${CertKinditem.codename}
											</option>
										</#list>
									</select>
								</td>
								<td>证件号码:</td>
								<td>
									<input class="form-control" type="text" id="applicantidcardno" name="applicant.idcardno" value="${relationPersonInfo.applicantidcardno}"/>
								</td>
							</tr>
							<tr>
								<td>手机号码:</td>
								<td>
									<input class="form-control phone" type="text" name="applicant.cellphone" value="${relationPersonInfo.applicantcellphone}"/>
								</td>
								<td>E-mail:</td>
								<td>
									<input class="form-control email" type="text" name="applicant.email" value="${relationPersonInfo.applicantEmail}"/>
								</td>
							</tr>
						</table>
					 </div>
				</div>
			</div>
		</div>
		<!--联系人-->
		<div class="panel panel-default" style="margin-bottom:0px">
			<div class="panel-heading">
				<div class="row">
					<div class="col-md-12">
						<span class="feildtitle">联系人信息</span>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
				  	<div id="form2">
						<table class="table table-bordered" style="margin-bottom:0px">
							<tr>
								<td class="col-md-2">与被保人一致:</td>
								<td class="col-md-4" colspan="3" class="form-control">
									<input  class="form-control" type="hidden" id="linkPersonid" name="linkPersonid" value="${relationPersonInfo.linkPersonid}"/>
									<input class="linkPersonyes" type="radio" name="linkPersonflag" value="true" id="yes2" <#if (relationPersonInfo.linkPersonid == relationPersonInfo.insuredid)>checked</#if> />是&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="linkPersonflag" value="false" id="no2" <#if (relationPersonInfo.linkPersonid != relationPersonInfo.insuredid)>checked</#if> />否
								</td>
							</tr>
							<tr>
								<td class="col-md-2">姓名:</td>
								<td class="col-md-4">
									<input class="form-control" type="text" id="linkPersonName" name="linkPerson.name" value="${relationPersonInfo.linkPersonname}"/>
								</td>
								<td>性别:</td>
								<td>
									<select name="linkPerson.gender" class="form-control">
										<#list relationPersonInfo.GenderList as Genderitem>
											<option value="${Genderitem.codevalue}" 
												<#if Genderitem.codevalue == relationPersonInfo.linkPersongender>selected</#if>>
												${Genderitem.codename}
											</option>
										</#list>
									</select>
								</td>
							</tr>
							<tr>
								<td>证件类型:</td>
								<td>
									<select class="form-control idcardtype" name="linkPerson.idcardtype" id="linkPersonidcardtype">
										<#list relationPersonInfo.CertKindList as CertKinditem>
											<option value="${CertKinditem.codevalue}" 
												<#if CertKinditem.codevalue == relationPersonInfo.linkPersonidcardtype>selected</#if>>
												${CertKinditem.codename}
											</option>
										</#list>
									</select>
								</td>
								<td>证件号码:</td>
								<td>
									<input class="form-control" type="text" id="linkPersonidcardno" name="linkPerson.idcardno" value="${relationPersonInfo.linkPersonidcardno}"/>
								</td>
							</tr>
							<tr>
								<td>手机号码:</td>
								<td>
									<input class="form-control phone" type="text" name="linkPerson.cellphone" value="${relationPersonInfo.linkPersoncellphone}"/>
								</td>
								<td>E-mail:</td>
								<td>
									<input class="form-control email" type="text" name="linkPerson.email" value="${relationPersonInfo.linkPersonEmail}"/>
								</td>
							</tr>
						</table>
					 </div>
				</div>
			</div>
		</div>
		<!--索赔权益人-->
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="row">
					<div class="col-md-12">
						<span class="feildtitle">索赔权益人信息</span>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
		 			<div id="form3">
						<table class="table table-bordered" style="margin-bottom:0px">
							<tr>
								<td class="col-md-2">与被保人一致:</td>
								<td class="col-md-4" colspan="3" class="form-control" >
									<input class="form-control" type="hidden" name="personForRightid" id="personForRightid" value="${relationPersonInfo.personForRightid}"/>
									<input class="personForRightyes" type="radio" name="personForRightflag" value="true" id="yes3" <#if (relationPersonInfo.personForRightid == relationPersonInfo.insuredid)>checked</#if>/>是&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="radio" name="personForRightflag" value="false" id="no3" <#if (relationPersonInfo.personForRightid != relationPersonInfo.insuredid)>checked</#if>/>否
								</td>
							</tr>
							<tr>
								<td class="col-md-2">姓名:</td>
								<td class="col-md-4">
									<input class="form-control" type="text" id="personForRightName" name="personForRight.name" value="${relationPersonInfo.personForRightname}"/>
								</td>
								<td>性别:</td>
								<td>
									<select class="form-control" name="personForRight.gender">
										<#list relationPersonInfo.GenderList as Genderitem>
											<option value="${Genderitem.codevalue}" 
												<#if Genderitem.codevalue == relationPersonInfo.personForRightgender>selected</#if>>
												${Genderitem.codename}
											</option>
										</#list>
									</select>
								</td>
							</tr>
							<tr>
								<td>证件类型:</td>
								<td>
									<select class="form-control idcardtype" name="personForRight.idcardtype" id="personForRightidcardtype">
										<#list relationPersonInfo.CertKindList as CertKinditem>
											<option value="${CertKinditem.codevalue}" 
												<#if CertKinditem.codevalue == relationPersonInfo.personForRightidcardtype>selected</#if>>
												${CertKinditem.codename}
											</option>
										</#list>
									</select>
								</td>
								<td>证件号码:</td>
								<td>
									<input class="form-control" type="text" id="personForRightidcardno" name="personForRight.idcardno" value="${relationPersonInfo.personForRightidcardno}"/>
								</td>
							</tr>
							<tr>
								<td>手机号码:</td>
								<td>
									<input class="form-control phone" type="text" name="personForRight.cellphone" value="${relationPersonInfo.personForRightcellphone}"/>
								</td>
								<td>E-mail:</td>
								<td>
									<input class="form-control email" type="text" name="personForRight.email" value="${relationPersonInfo.personForRightEmail}"/>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
		<!--车主信息-->
		<div class="panel panel-default" style="margin-bottom:0px">
			<div class="panel-heading">
				<div class="row">
					<div class="col-md-12">
						<span class="feildtitle">车主信息</span>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table class="table table-bordered" style="margin-bottom:0px">
						<tr>
							<td class="col-md-2">姓名:</td>
							<td class="col-md-4">
								<input class="form-control" type="hidden" id="carOwnerid" name="carOwnerid" value="${relationPersonInfo.carOwnerid}"/>
								<input class="form-control required" type="text" id="carOwnerInfoName" name="carOwnerInfo.name" value="${relationPersonInfo.carownername}"/>
							</td>
							<td>性别:</td>
							<td>
								<select class="form-control" name="carOwnerInfo.gender">
									<#list relationPersonInfo.GenderList as Genderitem>
										<option value="${Genderitem.codevalue}" 
											<#if Genderitem.codevalue == relationPersonInfo.carownergender>selected</#if>>
											${Genderitem.codename}
										</option>
									</#list>
								</select>
							</td>
						</tr>
						<tr>
							<td>证件类型:</td>
							<td>
								<select class="form-control idcardtype" name="carOwnerInfo.idcardtype" id="carOwnerInfoidcardtype">
									<#list relationPersonInfo.CertKindList as CertKinditem>
										<option value="${CertKinditem.codevalue}" 
											<#if CertKinditem.codevalue == relationPersonInfo.carownercardtype>selected</#if>>
											${CertKinditem.codename}
										</option>
									</#list>
								</select>
							</td>
							<td>证件号码:</td>
							<td>
								<input class="form-control required" type="text" id="carOwnerInfoidcardno" name="carOwnerInfo.idcardno" value="${relationPersonInfo.carownercardno}"/>
							</td>
						</tr>
						<tr>
							<td>手机号码:</td>
							<td>
								<input class="form-control required phone" type="text" id="carOwnerInfocellphone" name="carOwnerInfo.cellphone" value="${relationPersonInfo.carownercellphone}"/>
							</td>
							<td>E-mail:</td>
							<td>
								<input class="form-control email" type="text" id="carOwnerInfoemail" name="carOwnerInfo.email" value="${relationPersonInfo.carownerEmail}"/>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<div class="row">
	 	 <div class="col-md-6">
	 		<button class="btn btn-default" type="button" id="makesure" title="确定">&nbsp;&nbsp;确定&nbsp;&nbsp;</button>
	 	 </div>
	 	 <div class="col-md-6" align="right">
	 		<button class="btn btn-default" type="button" title="取消" data-dismiss="modal">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>
	 	 </div>
	  </div>
	</div>
</form>
</body>
</html>
