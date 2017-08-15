<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>单方车型修改</title>
		<!--<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>-->
		<style type="text/css">
			span.feildtitle {
				font-weight:bold;
				color:#34495E;
			}
			body {font-size: 14px;}
		</style>
		<script type="text/javascript">
			requirejs([ "cm/common/editCarModelInfo" ],function() {
				require([ "jquery", "bootstrap-table", "bootstrap", "bootstrapTableZhCn","public" ],function($) {
					$(function() {
						initEditCarModelInfoScript();
					});
				});
			});
		</script>
	</head>
	<body>
	<!--车辆信息弹出框-->
		<form class="form-inline" role="form" id="editCarModelinfo">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<h6 class="modal-title">车型信息详情</h6>
			</div>
			<div class="modal-body">
				<div class="panel panel-default" style="margin-bottom:0px">
					<div class="panel-heading">
						<div class="row">
							<div class="col-md-12">
								<span class="feildtitle">车型信息</span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<table class="table table-bordered" id="carmodelinfolist" style="margin-bottom:0px">
								<tr>
									<td class="col-md-2">
										车牌号码:
									</td>
									<td class="col-md-4">
										<input type="text" hidden="true" id="instanceId" name="instanceId" value="${carInfo.taskid}"/>
										<!-- 选项卡下标 -->
										<input type="hidden" id="num" name="num" value="${num}"/>
										<input type="text" hidden="true" id="inscomcode" name="inscomcode" value="${Carmodelinfo.inscomcode}"/>
										${carInfo.carlicenseno}
									</td>
									<td class="col-md-2">
										所属性质:
									</td>
									<td class="col-md-4">
										${carInfo.propertyvalue}
									</td>
								</tr>
								<tr>
									<td>车辆使用性质:</td>
									<td>
										${carInfo.carpropertyvalue}
										<!--
										<select class="form-control" name="carproperty" id="carproperty">
											<#list carInfo.carpropertyList as carpropertyitem>
												<option value="${carpropertyitem.codevalue}" 
													<#if carpropertyitem.codevalue == carInfo.carproperty>selected</#if>>
													${carpropertyitem.codename}
												</option>
											</#list>
										</select>
										-->
									</td>
									<td>品牌型号:</td>
									<td>
										<input type="text" hidden="true" id="standardfullname" name="standardfullname" value="${Carmodelinfo.standardfullname}"/>
										<input type="text" hidden="true" id="standardname" name="standardname" value="${Carmodelinfo.standardname}"/>
										<input type="text" hidden="true" id="familyname" name="familyname" value="${Carmodelinfo.familyname}"/>
										<input type="text" hidden="true" id="brandname" name="brandname" value="${Carmodelinfo.brandname}"/>
										<span id="standardfullnameTXT">${Carmodelinfo.standardfullname}</span>
									</td>
								</tr>
								<tr>
									<td>车辆种类:</td>
									<td>
										${carInfo.vehicleType}
									</td>
									<td>核定载人数:</td>
									<td>
										<input type="text" hidden="true" id="seat" name="seat" value="${Carmodelinfo.seat}"/>
										<span id="seatTXT">${Carmodelinfo.seat}</span>
									</td>
								</tr>
								<tr>
									<td>核定载质量:</td>
									<td>
										<input type="text" hidden="true" id="unwrtweight" name="unwrtweight" value="${Carmodelinfo.unwrtweight}"/>
										<span id="unwrtweightTXT">${Carmodelinfo.unwrtweight}</span>
									</td>
									<td>排气量:</td>
									<td>
										<input type="text" hidden="true" id="displacement" name="displacement" value="${Carmodelinfo.displacement}"/>
										<span id="displacementTXT">${Carmodelinfo.displacement}</span>
									</td>
								</tr>
								<tr>
									<td>整备质量:</td>
									<td>
										<input type="text" hidden="true" id="fullweight" name="fullweight" value="${Carmodelinfo.fullweight}"/>
										<span id="fullweightTXT">${Carmodelinfo.fullweight}</span>
									</td>
									<td>年款:</td>
									<td>
										<input type="text" hidden="true" id="listedyear" name="listedyear" value="${Carmodelinfo.listedyear}"/>
										<span id="listedyearTXT">${Carmodelinfo.listedyear}</span>
									</td>
								</tr>
								<tr>
									<td>车型配置:</td>
									<td>
										<input type="text" hidden="true" id="cardeploy" name="cardeploy" value="${Carmodelinfo.cardeploy}"/>
										<span id="cardeployTXT">${Carmodelinfo.cardeploy}</span>
									</td>
									<td>投保车价:</td>
									<td>
										${Carmodelinfo.policycarprice}
										<!--
										<input class="form-control" type="text" id="policycarprice" name="policycarprice" value="${Carmodelinfo.policycarprice}"/>
										-->
									</td>
								</tr>
								<tr>
									<td>新车购置价:</td>
									<td>
										<input type="text" hidden="true" id="price" name="price" value="${Carmodelinfo.price}"/>
										<input type="text" hidden="true" id="taxprice" name="taxprice" value="${Carmodelinfo.taxprice}"/>
										<input type="text" hidden="true" id="analogyprice" name="analogyprice" value="${Carmodelinfo.analogyprice}"/>
										<input type="text" hidden="true" id="analogytaxprice" name="analogytaxprice" value="${Carmodelinfo.analogytaxprice}"/>
										<input type="text" hidden="true" id="aliasname" name="aliasname" value="${Carmodelinfo.aliasname}"/>
										<input type="text" hidden="true" id="gearbox" name="gearbox" value="${Carmodelinfo.gearbox}"/>
										<input type="text" hidden="true" id="loads" name="loads" value="${Carmodelinfo.loads}"/>
										<span id="priceTXT">${Carmodelinfo.price}</span>
									</td>
									<td></td>
									<td>
										 
									</td>
								</tr>
							</table>
						</div>
					</div>
				</div>
				<!--bug2689要求不显示车型修改信息
				<div class="panel panel-default">
					<div class="panel-heading">
						<div class="row">
							<div class="col-md-12">
								<span class="feildtitle">选择车型配置</span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<table id="carmodelinfotable"></table>
						</div>
					</div>
				</div>
				-->
				<div class="row">
				 	<div class="col-md-6">
				 		<!--bug699 不能修改仅查看功能
				 		<button class="btn btn-default" type="button" id="makesure" title="确定">&nbsp;&nbsp;确定&nbsp;&nbsp;</button>
				 		-->
				 	</div>
				 	<div class="col-md-6" align="right">
				 		<button class="btn btn-default" type="button" title="取消" data-dismiss="modal">&nbsp;&nbsp;关闭&nbsp;&nbsp;</button>
				 	</div>
				</div>
			</div>
		</form>
	</body>
</html>
