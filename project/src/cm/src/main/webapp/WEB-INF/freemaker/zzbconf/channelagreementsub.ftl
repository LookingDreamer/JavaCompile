<!DOCTYPE html>
<html lang ="en" class="fuelux" >  
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>渠道协议管理子页面</title>
<script type="text/javascript">
			requirejs([ "zzbconf/channelagreementsub" ],function() {
				require([  "jquery", "zTree", "zTreecheck", "flat-ui", "bootstrap-table", "fuelux", "bootstrap", "bootstrapTableZhCn","public"],function($) {
					$(function() {
						initchannelagreementScript();
					});
				});
			});
		</script>
</head>
<body>
<div class="panel panel-default m-bottom-2">
	<div class="panel-heading padding-5-5"><h4 class="titlelabel">基本信息</h4></div>
	<div class="panel-body pbodynopadding">
	    <div class="form-group col-md-6 form-inline">
			<label for="channelname">渠道名称:</label>
			<input type="text" class="form-control" id="channelname" name="channelname" value="${billTypeVo.channelname}" disabled/>
		</div>
		<div class="form-group col-md-6 form-inline">
			<label for="channelrelativedept">关联机构:</label>
			<input type="text" class="form-control" id="channelrelativedept" name="channelrelativedept" value="${billTypeVo.deptname}" disabled/>
		</div>
	</div>
	<div class="panel-heading padding-5-5"><h4 class="titlelabel">渠道协议详情</h4></div>
	<div class="panel-body">
	     <ul id="myTab" class="nav nav-pills nav-justified" role="tablist">
	     	<li class="active"><a href="#accountmode" role="tab" data-toggle="pill">结算方式</a></li>
			<li><a href="#providerrange" role="tab" data-toggle="pill">供应商范围</a></li>
			<li><a href="#deptrange" role="tab" data-toggle="pill" >出单网点</a></li>
			<!--<li><a href="#deliverymode" role="tab" data-toggle="pill">配送方式</a></li>-->
		 </ul>
		 <div id="myTabContent" class="tab-content">
		 	<div class="tab-pane fade in active" id="accountmode">
		 		<form id="accountmodeform">
					<input type="hidden" id="channelid" name="channelid" value="${billTypeVo.channelid}">
					<input type="hidden" id="agreementid" name="agreementid" value="${billTypeVo.agreementid}">
					<input type="hidden" id="deptid" name="deptid" value="${billTypeVo.deptid}">
			 		<div class="panel panel-default m-bottom-2 panelnobottom">
						<div class="panel-heading padding-5-5">结算配置</div>
						<div class="panel-body pbodynopadding">
							<table class="table table-striped">
								<#list billTypeVo.agreementInterfaceList as interface>
								<tr class="agreementinter">
									<td>
										<label class="checkbox-inline">
										  <input type="checkbox" name="agreementInterfaceList[${interface_index}].check" id="inlineCheckbox01" value="1" <#if ("${interface.check}"=="1")>checked="checked" </#if>> ${interface.interfacename}
										  <input type="hidden" name="agreementInterfaceList[${interface_index}].interfaceid" value="${interface.interfaceid}" />
										  <input type="hidden" name="agreementInterfaceList[${interface_index}].checkflag" value="${interface.check}" />
										  <input type="hidden" name="agreementInterfaceList[${interface_index}].agreementinterfaceid" value="${interface.agreementinterfaceid}" />
										</label>
									</td>
									<td>
										<label class="radio-inline">
											<input type="radio" class="isfree" name="agreementInterfaceList[${interface_index}].isfree" value="0" <#if ("${interface.isfree}"=="0")>checked </#if>> 免费
										</label>
										<label class="radio-inline">
											<input type="radio" class="isfree" name="agreementInterfaceList[${interface_index}].isfree" value="1" <#if ("${interface.isfree}"=="1")>checked </#if>> 收费
										</label>
									</td>
									<td>
										每月<input type="text" class="shortinput" id="monthfree" name="agreementInterfaceList[${interface_index}].monthfree" value="${interface.monthfree}"/>次免费，超出收取
										<input type="text" class="shortinput" id="perfee" name="agreementInterfaceList[${interface_index}].perfee" value="${interface.perfee}"/>元/次
									</td>
								</tr>
								</#list>
							</table>
						</div>
					</div>
			 		<div class="panel panel-default m-bottom-2 panelnobottom">
						<div class="panel-heading padding-5-5">结算周期</div>
						<div class="panel-body pbodynopadding">
							<div class="form-group form-inline">
								<label for="channelrelativedept">T+</label>
								<input type="text" class="form-control" id="accountperiod" name="payperiod" value="${billTypeVo.payperiod}"/>
							</div>
						</div>
					</div>
			 		<div class="panel panel-default m-bottom-2 panelnobottom">
						<div class="panel-heading padding-5-5">结算方式</div>
						<div class="panel-body pbodynopadding">
							<div class="row">
								<div class="col-md-12">
									<label class="col-md-3">
										<input type="radio" name="paytype" id="bankaccount" value="0" <#if ("${billTypeVo.paytype}"=="0")>checked </#if>> 银行转账
									</label>
									<label class="col-md-3">
										<input type="radio" name="paytype" id="otheraccount" value="1" <#if ("${billTypeVo.paytype}"=="1")>checked </#if>> 其他方式
									</label>
									<div class="form-group col-md-6 form-inline">
										<label for="balanceaccount">结算账户:</label>
										<input type="text" class="form-control" id="balanceaccount" style="width:200px;" name="payaccount" value="${billTypeVo.payaccount}"/>
									</div>
								</div>
							</div>
						</div>
					</div>
			 		<div class="panel panel-default m-bottom-2 panelnobottom">
						<div class="panel-heading padding-5-5">产品接入方式</div>
						<div class="panel-body">
							<label class="checkbox-inline col-md-3">
							  <input type="checkbox" id="weixin" name="interfacetype" value="0" <#if ("${billTypeVo.interfacetype}"?contains("0"))>checked </#if>> 微信公众号
							</label>
							<label class="checkbox-inline col-md-3">
							  <input type="checkbox" id="mobileapp" name="interfacetype" value="1" <#if ("${billTypeVo.interfacetype}"?contains("1"))>checked </#if>> App
							</label>
							<label class="checkbox-inline col-md-3">
							  <input type="checkbox" id="webpage" name="interfacetype" value="2" <#if ("${billTypeVo.interfacetype}"?contains("2"))>checked </#if>> 网页
							</label>
						</div>
					</div>
					<button type="button" class="btn btn-primary" id="accountmodeformbtn" onclick="saveAccountMode();">保存</button>
				</form>
		 	</div>
		 	<div class="tab-pane fade" id="providerrange">
		 		<form id="providerrangeform">
			 		<div class="panel panel-default m-bottom-2 panelnobottom">
						<div class="panel-heading padding-5-5">供应商选择</div>
						 <#list checkedProvider as Provider>
						 <input type="hidden" class="ProviderCode" id="ProviderCode" value="${Provider.providerid}" />
						 </#list>
						<div class="panel-body pbodynopadding">
							<table class="table table-striped">
							 <#list provider as insConfig>
								<tr>
									<td>
									 	<input type="hidden" class="prvcodes" id="prvcodes" value="${insConfig.id}" />
										<input type="checkbox" class="prvcode" id="prvcode" value="${insConfig.id}" />
									</td>
									<td>
										${insConfig.prvname}
									</td>
								</tr>
							</#list>
							</table>
						</div>
					</div>
					<button type="button" class="btn btn-primary" id="providerrangeformbtn">保存</button>
				</form>
		 	</div>
		 	<div class="tab-pane fade" id="deptrange">
		 		<form id="deptrangeform">
			 		<div class="panel panel-default m-bottom-2 panelnobottom">
						<div class="panel-heading padding-5-5">选择网点</div>
						<div class="panel-body pbodynopadding">
							<div class="row">
								<div class="col-md-12">
									<div class="form-group col-md-4 form-inline">
										<select class="form-control deptchange" style="width:180px;" id="deptids1" onchange="deptconnchange(1)">
											<option value="" selected="true" disabled="true">请选择</option>
										</select>
									</div>
									<div class="form-group col-md-4 form-inline">
										<select class="form-control deptchange" style="width:180px;" id="deptids2" onchange="deptconnchange(2)">
										</select>
									</div>
									<div class="form-group col-md-4 form-inline">
										<select class="form-control deptchange" style="width:180px;" id="deptids3" onchange="deptconnchange(3)">
										</select>
									</div>
									<div class="form-group col-md-4 form-inline">
										<select class="form-control deptchange" style="width:180px;" id="deptids4" onchange="deptconnchange(4)">
										</select>
									</div>
									<div class="form-group col-md-4 form-inline">
										<select class="form-control deptchange" style="width:180px;" id="deptids5" onchange="deptconnchange(5)">
										</select>
									</div>
									<button type="button" class="btn btn-primary" id="deptrelative">添加关联</button>
								</div>
							</div>
						</div>
					</div>
			 		<div class="panel panel-default m-bottom-2 panelnobottom">
						<div class="panel-heading padding-5-5">已关联网点</div>
						<div class="panel-body pbodynopadding">
							<table class="table table-striped" id="deptListTable"></table>
							<button type="button" class="btn btn-primary" id="disdeptrelative">取消关联</button>
						</div>
					</div>
				</form>
		 	</div>
		 	<!--<div class="tab-pane fade" id="deliverymode">配送方式</div>-->
		 </div>
	</div>
</div>
</body>
</html>
