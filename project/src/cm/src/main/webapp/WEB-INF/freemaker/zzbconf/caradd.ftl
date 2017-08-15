<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>车辆编辑</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/caradd" ]);
</script>


<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">编辑</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-md-12">
						<form class="form-inline" role="form" id="agent_form">
							<table class="table table-bordered ">
								<tr>
									<td class="col-md-2 " align="right"
										style="vertical-align: middle;">车主姓名：</td>
									<td class="col-md-2">
										<input class="form-control" type="text" name="ownername" value="${agent.name!''}">
										<input class="form-control" type="hidden" id="owner" name="owner" value="${agent.id!''}">
									</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">联系方式：</td>
									<td class="col-md-2"><input class="form-control"
										type="text" name="phonenumber" value="${agent.idno!''}"></td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">联系地址：</td>
									<td class="col-md-2"><input class="form-control"
										type="text" name="address" value="${agent.address!''}"></td>
								</tr>
								<tr>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">车牌号：</td>
									<td class="col-md-2">
									<input class="form-control" type="text" id="carlicenseno" name="carlicenseno" value="${agent.carlicenseno!'' }">
										</td>
									<td class="col-md-1" align="right"
										style="vertical-align: middle;">车辆识别代号：</td>
									<td class=col-md-2>
									<input class="form-control" type="text" id="vincode" name="vincode" value="${car.vincode!'' }">
									</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">车型信息描述：</td>
									<td class="col-md-2">
									  <select class="form-control m-left-5" id="standardfullname" name="standardfullname" placeholder="请选择">
						     	            <option value="">请选择</option>
							     	        <#list carmodellist as row>
							     	          <option value="${row.carmodelid }">${row.standardfullname }</option>
							     	        </#list>
						     	     	</select>
									</td>
								</tr>
								<tr>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">发动机号：</td>
									<td class="col-md-2"><input class="form-control"
										type="text" name="engineno" value="${car.engineno!'' }">
									</td>
									
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">车辆初始登记日期：</td>
									<td class="col-md-2"><input class="form-control"
										type="text" name="registdate" value="${car.registdate!'' }">
									</td>
									
									<td class="col-md-1" align="right"
										style="vertical-align: middle;">注册许可证：</td>
									<td class="col-md-2"><input class="form-control"
										type="text" name="registlicense" value="${car.registlicense!'' }">
									</td>
									
								</tr>
								<tr>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">过户日期：</td>
									<td class="col-md-2"><input class="form-control"
										type="text" name="transferdate" value="${car.transferdate!'' }">
									</td><#-- 改为时间控件 -->
									
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">过户车：</td>
									<td class="col-md-2">
										<select class="form-control" name="isTransfercar">
											<option value="1" <#if car.isTransfercar==1>selected="selected"</#if>>是</option>
											<option value="2" <#if car.isTransfercar==2>selected="selected"</#if>>否</option>
										</select>
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">是否指定驾驶人：</td>
									<td class="col-md-2">
										<select class="form-control" name="Specifydriver">
											<option value="1" <#if car.Specifydriver==1>selected="selected"</#if>>是</option>
											<option value="2" <#if car.Specifydriver==2>selected="selected"</#if>>否</option>
										</select>
									</td>
								</tr>
								<tr>
									<td class="col-md-2" align="right" style="vertical-align: middle;">行驶区域:</td>
									<td class="col-md-2"><input class="form-control"
										type="text" name="drivingarea" value="${car.drivingarea!'' }">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">平均行驶里程:</td>
									<td class=col-md-2>
										<input class="form-control" type="text" name="mileage" value="${car.mileage!'' } ">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">事故次数:</td>
									<td class=col-md-2>
										<input class="form-control" type="text" name="accidentnum" value="${car.accidentnum!'' } ">
									</td>
								</tr>
								
								<tr>
									<td class="col-md-2" align="right" style="vertical-align: middle;">车辆编号:</td>
									<td class="col-md-2">
									   <input class="form-control" type="text" name="carid" value="${car.carid!'' }">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">使用年限:</td>
									<td class=col-md-2>
										<input class="form-control" type="text" name="useyears" value="${car.useyears!'' } ">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">签署时间:</td>
									<td class=col-md-2>
										<input class="form-control" type="text" name="signdate" value="${car.signdate!'' } ">
									</td>
								</tr>
								
								<tr>
									<td class="col-md-2" align="right" style="vertical-align: middle;">固定停放地点:</td>
									<td class="col-md-2">
									   <input class="form-control" type="text" name="parksite" value="${car.parksite!'' }">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">上年商业承保公司:</td>
									<td class=col-md-2>
										<input class="form-control" type="text" name="preinscode" value="${car.preinscode!'' } ">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">任务状态:</td>
									<td class=col-md-2>
										<input class="form-control" type="text" name="taskstatus" value="${car.taskstatus!'' } ">
									</td>
								</tr>
								
								<tr>
									<td class="col-md-2" align="right" style="vertical-align: middle;">所属性质:</td>
									<td class="col-md-2">
									   <input class="form-control" type="text" name="property" value="${car.property!'' }">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">车辆性质:</td>
									<td class=col-md-2>
										<input class="form-control" type="text" name="carproperty" value="${car.carproperty!'' } ">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">商业起保时间:</td>
									<td class=col-md-2>
										<input class="form-control" type="text" name="businessstartdate" value="${car.businessstartdate!'' } ">
									</td>
								</tr>
								
								<tr>
									<td class="col-md-2" align="right" style="vertical-align: middle;">商业终止时间:</td>
									<td class="col-md-2">
									   <input class="form-control" type="text" name="businessenddate" value="${car.businessenddate!'' }">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">交强起保日期:</td>
									<td class=col-md-2>
										<input class="form-control" type="text" name="strongstartdate" value="${car.strongstartdate!'' } ">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">交强终止日期:</td>
									<td class=col-md-2>
										<input class="form-control" type="text" name="strongenddate" value="${car.strongenddate!'' } ">
									</td>
								</tr>
								
								<tr>
									<td class="col-md-2" align="right" style="vertical-align: middle;">云查询保险公司:</td>
									<td class="col-md-2">
									   <input class="form-control" type="text" name="cloudinscompany" value="${car.cloudinscompany!'' }">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">是否上牌:</td>
									<td class="col-md-2">
										<select class="form-control" name="isLicense">
											<option value="1" <#if car.isLicense==1>selected="selected"</#if>>是</option>
											<option value="2" <#if car.isLicense==2>selected="selected"</#if>>否</option>
										</select>
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">号牌颜色:</td>
									<td class=col-md-2>
										<input class="form-control" type="text" name="platecolor" value="${car.platecolor!'' } ">
									</td>
								</tr>
								
								<tr>
									<td class="col-md-2" align="right" style="vertical-align: middle;">号牌种类:</td>
									<td class="col-md-2">
									   <input class="form-control" type="text" name="plateType" value="${car.plateType!'' }">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">检验有效日期:</td>
									<td class=col-md-2>
										<input class="form-control" type="text" name="ineffectualDate" value="${car.ineffectualDate!'' } ">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">强制有效期:</td>
									<td class=col-md-2>
										<input class="form-control" type="text" name="rejectDate" value="${car.rejectDate!'' } ">
									</td>
								</tr>
								
								<tr>
									<td class="col-md-2" align="right" style="vertical-align: middle;">最近定检日期:</td>
									<td class="col-md-2">
									   <input class="form-control" type="text" name="lastCheckDate" value="${car.lastCheckDate!'' }">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">是否贷款车:</td>
									<td class="col-md-2">
										<select class="form-control" name="isLoansCar">
											<option value="1" <#if car.isLoansCar==1>selected="selected"</#if>>是</option>
											<option value="2" <#if car.isLoansCar==2>selected="selected"</#if>>否</option>
										</select>
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">车辆用途:</td>
									<td class=col-md-2>
										<input class="form-control" type="text" name="carVehicularApplications" value="${car.carVehicularApplications!'' } ">
									</td>
								</tr>
								
								<tr>
									<td class="col-md-2" align="right" style="vertical-align: middle;">是否军牌或外地车:</td>
									<td class="col-md-2">
										<select class="form-control" name="isFieldCar">
											<option value="1" <#if car.isFieldCar==1>selected="selected"</#if>>是</option>
											<option value="2" <#if car.isFieldCar==2>selected="selected"</#if>>否</option>
										</select>
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">车身颜色:</td>
									<td class=col-md-2>
										<input class="form-control" type="text" name="carBodyColor" value="${car.carBodyColor!'' } ">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">是否车贷投保多年标志:</td>
									<td class="col-md-2">
										<select class="form-control" name="loanManyYearsFlag">
											<option value="1" <#if car.loanManyYearsFlag==1>selected="selected"</#if>>是</option>
											<option value="2" <#if car.loanManyYearsFlag==2>selected="selected"</#if>>否</option>
										</select>
									</td>
								</tr>
								
								<tr>
									<td class="col-md-2" align="right" style="vertical-align: middle;">新车标志:</td>
									<td class="col-md-2">
										<select class="form-control" name="isNew">
											<option value="1" <#if car.isNew==1>selected="selected"</#if>>是</option>
											<option value="2" <#if car.isNew==2>selected="selected"</#if>>否</option>
										</select>
									</td>
								</tr>
								
							</table>
						</form>
					</div>
				</div>
			</div>
			<div class="panel-footer">
				<button  type="button"  id="save_agent_permisssion_provider" class="btn btn-primary">保存</button>
				<button  type="button"  id="go_back" class="btn btn-primary">返回</button>
			</div>
		</div>

	
	

</body>
</html>