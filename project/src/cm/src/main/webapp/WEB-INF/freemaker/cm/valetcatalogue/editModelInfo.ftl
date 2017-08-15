<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>修改车辆信息</title>
		<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>
		<style type="text/css">
			#carModelTable{ 
				background-color:#FFFFFF; 
				width:100%;
			}
			span.feildtitle {
				font-weight:bold;
				color:#34495E;
			}
		</style>
		<script type="text/javascript">
			requirejs([ "cm/valetcatalogue/editModelInfo" ],function() {
				require([ "jquery", "bootstrap-table","bootstrapdatetimepicker" ,"bootstrap",
					 "bootstrapTableZhCn" ,"bootstrapdatetimepickeri18n","public"],	function($) {
					$(function() {
						initEditCarInfoScript();
					});
				});
			});
		</script>
	</head>
	<body>
		<!--车辆信息弹出框-->
		<form class="form-inline" role="form" id="editCarinfo">
			<!-- 将taskid设置为隐藏 -->
			<input type="hidden"  id="taskid"  name="taskid" value="${carInfo.taskid}"/>
			<!-- 选项卡下标 -->
			<input type="hidden" id="num" name="num" value="${num}"/>
			<!-- 将保险公司code设置为隐藏 -->
			<input type="hidden" id="inscomcode" name="inscomcode" value="${Carmodelinfo.inscomcode}"/>
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span>
					<span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title">修改车辆信息</h4>
			</div>
			<div class="modal-body">
				<div class="panel panel-default" style="margin-bottom:0px">
					<div class="panel-heading">
						<div class="row">
							<div class="col-md-12">
								<span class="feildtitle">投保单车辆信息</span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<table class="table table-bordered" id="insurancecarmessage" style="margin-bottom:0px">
								<tr>
									<td class="col-md-2" >车牌:</td>
									<td class="col-md-4">${carInfo.carlicenseno}</td>
									<td class="col-md-2" >车主:</td>
									<td class="col-md-4">${carInfo.ownername}</td>
								</tr>
								<tr>
									<td>所属性质:</td>
									<td>
										<select name="property" id="property" style="WIDTH: 183px" class="form-control">
											<#list carInfo.propertyList as propertyitem>
												<option value="${propertyitem.codevalue}" 
													<#if propertyitem.codevalue == carInfo.property>selected</#if>>
													${propertyitem.codename}
												</option>
											</#list>
										</select>
									</td>
									<td>车辆使用性质:</td>
									<td>
										<select class="form-control" style="WIDTH: 183px" name="carproperty" id="carproperty">
											<#list carInfo.carpropertyList as carpropertyitem>
												<option value="${carpropertyitem.codevalue}" 
													<#if carpropertyitem.codevalue == carInfo.carproperty>selected</#if>>
													${carpropertyitem.codename}
												</option>
											</#list>
										</select>
									</td>
								</tr>
								<tr>
									<td>发动机号:</td>
									<td>
										<input class="form-control" type="text" name="engineno" id="engineno" value="${carInfo.engineno}"/>
									</td>
									<td>车辆识别代号:</td>
									<td>
										<input class="form-control" type="text" name="vincode" id="vincode" value="${carInfo.vincode}"/>
									</td>
								</tr>
								<tr>
									<td>车辆初登日期:</td>
									<td>
										<!--<div class="form-group col-md-12">
											<div class="form-group col-md-12">  input-group-->
												<div class="input-group date form_datetime col-md-12"
													data-date-format="yyyy-mm-dd" data-link-field="registdate">
													<input class="form-control" style="color:black;" type="text" id="registdatetxt" readonly value="${carInfo.registdate}"/>
													<span class="input-group-addon">
														<span class="glyphicon glyphicon-remove"></span>
													</span>
													<span class="input-group-addon">
														<span class="glyphicon glyphicon-th"></span>
													</span>
												</div>
												<input class="form-control" type="hidden" id="registdate" name="registdate" value="${carInfo.registdate}"/>
											<!--</div>
										</div>  -->
									</td>
									<td>是否过户车:</td>
									<td>
										<select name="isTransfercar" id="isTransfercar" style="WIDTH: 183px" class="form-control">
											<option value="1" <#if carInfo.isTransfercar == "1">selected="selected"</#if>>是</option> 
						     			    <option value="0" <#if carInfo.isTransfercar == "0">selected="selected"</#if>>否</option> 
										</select>
									</td>
								</tr>
								<tr id="transferdateBox" <#if carInfo.isTransfercar == "0">style="display:none"</#if>>>
									<td></td>
									<td></td>
									<td>过户时间:</td>
									<td>
										<div class="input-group date form_datetime col-md-12"
											data-date-format="yyyy-mm-dd" data-link-field="transferdate">
											<input class="form-control transferdate" style="color:black;" type="text" id="transferdateqtxt" readonly value="${carInfo.transferdate}"/>
											<span class="input-group-addon">
												<span class="glyphicon glyphicon-remove"></span>
											</span>
											<span class="input-group-addon">
												<span class="glyphicon glyphicon-th"></span>
											</span>
										</div>
										<input type="hidden" class="form-control" class="transferdate" id="transferdate" name="transferdate" value="${carInfo.transferdate}"/>
									</td>
								</tr>
								<tr>
									<td>平均行驶里程:</td>
									<td>
										<select id="mileage" name="mileage" class="form-control" style="WIDTH: 183px">
											<#list carInfo.mileageList as mileageitem>
												<option value="${mileageitem.codevalue}" 
													<#if mileageitem.codevalue == carInfo.mileage>selected</#if>>
													${mileageitem.codename}
												</option>
											</#list>
										</select>
									</td>
									<td>行驶区域:</td>
									<td>
										<select id="drivingarea" name="drivingarea" class="form-control" style="WIDTH: 183px">
											<#list carInfo.drivingareaList as drivingareaitem>
												<option value="${drivingareaitem.codevalue}" 
													<#if drivingareaitem.codevalue == carInfo.drivingarea>selected</#if>>
													${drivingareaitem.codename}
												</option>
											</#list>
										</select>
									</td>
								</tr>
							</table>
							<!--平均行驶里程值集合-->
							<div id="mileagevalueList" style="display:none;" >
								<#list carInfo.mileageList as mileageitem>
									<span id="MILEAGEVALUE_${mileageitem.codevalue}">${mileageitem.codename}</span>
								</#list>
							</div>
							<!--行驶区域值集合-->
							<div id="drivingareavalueList" style="display:none;">
								<#list carInfo.drivingareaList as drivingareaitem>
									<span id="DRIVINGAREAVALUE_${drivingareaitem.codevalue}">${drivingareaitem.codename}</span>
								</#list>
							</div>
						</div>
					</div>
				</div>
	
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="row" id="noSelectCarModel">
							<div class="col-md-6">
								<span class="feildtitle">投保单车型信息</span>
							</div>
							<div class="col-md-6" align="right">
								<span style="padding-left: 80px; display: block">
									<a href="#" id="showSelectCarModel">重选车型</a>
								</span>
							</div>
						</div>
						<!--以下是重选车型隐藏内容-->
						<div class="row" id="selectCarModel" style="display:none">
							<div class="col-md-12">
								<span class="feildtitle">重选车型：</span>
			  					<button class="close" type="button" id="selectCarModelclose">&times;</button>
							</div>
							<div class="col-md-12">
								<div class="form-group">
								    <input type="text" class="form-control" placeholder="请输入车型关键字"
								    	id="standardfullnameTxt" name="standardfullname" value=""/>
							    <button id="carModelQueryButton" type="button" name="carModelQueryButton"
										class="btn btn-default">搜索</button>
								</div>
							</div>
							<div class="col-md-12">
								<div id="carModelTable">
									<table id="table-carModelInfo"></table>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<table class="table table-bordered" style="margin-bottom:0px">
								<tr>
									<td class="col-md-2">品牌型号:</td>
									<td class="col-md-4" >
										<input class="form-control" type="text" name="standardname" id="standardname" value="${Carmodelinfo.standardfullname}"/>
									</td>
									<td class="col-md-2">是否标准车型:</td>
									<td class="col-md-4" >
										<input class="form-control" type="text" style="color:black; WIDTH: 175px;" id="isstandardcar" name="isstandardcar" value="${Carmodelinfo.isstandardcar}" readonly/>
									</td>
								</tr>
								<tr>
									<td>核定载人数:</td>
									<td>
										<input class="form-control" type="text" id="seat" name="seat" value="${Carmodelinfo.seat}"/>座
									</td>
									<td>核定载质量:</td>
									<td>
										<input class="form-control" type="text" style="WIDTH: 175px" id="unwrtweight" name="unwrtweight" value="${Carmodelinfo.unwrtweight}"/>千克
									</td>
								</tr>
								<tr>
									<td>排气量:</td>
									<td>
										<input class="form-control" type="text" id="displacement" name="displacement" value="${Carmodelinfo.displacement}"/>升
									</td>
									<td>整备质量:</td>
									<td>
										<input class="form-control" type="text" style="WIDTH: 175px" id="fullweight" name="fullweight" value="${Carmodelinfo.fullweight}"/>千克
									</td>
								</tr>
								<tr>
									<td>车价选择:</td>
									<td colspan="3">
							             <#list Carmodelinfo.carpriceList as carpriceitem>
							             	<input class="isPolicycarprice" type="radio" name="carprice" value="${carpriceitem.codevalue}" 
							             		<#if carpriceitem.codevalue == "2"> id="thespecifiedprice" </#if> 
							             		<#if Carmodelinfo.carprice == carpriceitem.codevalue>checked</#if>/>${carpriceitem.codename}&nbsp;&nbsp;
										</#list>
							             <!--投保车价-->
										 <input class="form-control" type="text" id="policycarprice" name="policycarprice" value="${Carmodelinfo.policycarprice}" <#if Carmodelinfo.carprice != "2">disabled</#if>/>
									</td> 
								</tr>
								<tr>
									<td>年款:</td>
									<td colspan="3">
										<input class="form-control" type="text" id="listedyear" name="listedyear" value="${Carmodelinfo.listedyear}"/>
									</td>
								</tr>
								<tr>
									<td>新车购置价:</td>
									<td colspan="3">
										<input class="form-control" style="color:black;" type="text" id="purchaseprice" name="price" value="${Carmodelinfo.price}" readonly/>元
										&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="checkbox" id="specifiedpurchaseprice" name="specifiedpurchaseprice"/>指定购置价
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
