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
			requirejs([ "cm/common/editOtherInfo" ],function() {
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
			<h6 class="modal-title">修改平台信息 </h6>
		</div>
		<div class="modal-body">
			<form class="form-inline" role="form" id="otherInfoEditForm">
			  <input type="hidden" id="inscomcode" name="inscomcode" value="${inscomcode}"/>
			  <input type="hidden" id="taskid" name="taskid" value="${taskid}"/>
			  <input type="hidden" id="infoType" name="infoType" value="platform"/>
			  <!-- 选项卡下标 -->
			  <input type="hidden" id="num" name="num" value="${num}"/>
			  <table class="table table-bordered" style="margin-bottom:0px">
				<tr id="busiDate">
					<td class="active col-md-3">平台商业险起保日期：</td>
					<td class="col-md-6">
						<!--<input type="text" name="businessstartdate" class="form-control col-md-12" value="${otherInfo.businessstartdate}" placeholder="">-->
						<div class="form-group col-md-12">
							<div class="input-group date form_datetime1 col-md-12"
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
					<td class="active col-md-3">平台商业险终止日期：</td>
					<td class="col-md-1">
						<input type="text" style="color:black;" id="businessenddate" name="businessenddate" class="form-control" 
							value="${otherInfo.businessenddate!""}" readonly="readonly" placeholder=""/>
					</td>
				</tr>
				<tr id="stroDate" >
					<td class="active col-md-3">平台交强险起保日期：</td>
					<td class="col-md-6">
						<div class="form-group col-md-12">
							<div class="input-group date form_datetime1 col-md-12"
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
					<td class="active col-md-3">平台交强险终止日期：</td>
					<td class="col-md-1">
						<input type="text" style="color:black;" id="compulsoryenddate" name="compulsoryenddate" class="form-control" 
							value="${otherInfo.compulsoryenddate!""}" readonly="readonly" placeholder=""/>
					</td>
				</tr>
				<tr >
					<td class="active col-md-2 ">上年商业险投保公司：</td>
					<td class="col-md-4">
						<select name="preinscode" id="preinscode" style="width:auto" class="form-control " >
								<option value=""> -请选择- </option>
								<#list commercialProviderMaplist as provideritem>
									<option value="${provideritem.value}">${provideritem.key}</option>
								</#list>
							</select>
					</td>
					<td class="active col-md-2">无赔款不浮动原因：</td>
					<td class="col-md-4">
						<input type="text" id="loyaltyreasons" name="loyaltyreasons"  class="form-control col-md-12  invoice"  value="${otherInfo.loyaltyreasons}"/>
					</td>
				</tr>
				<tr>
					<td class="active col-md-3">平台商业险出险次数：</td>
					<td colspan="3" class="col-md-10">
							<select name="commercialtimes" id="commercialtimes" style="width:auto" class="form-control " >
								<option value=""> -请选择- </option>
								<#list commercialmatedataMaplist as commercialitem>
									<option value="${commercialitem.key}">${commercialitem.key}</option>
								</#list>
							</select>
					</td>
				</tr>
				<tr>
					<td class="active col-md-3">平台交强险出险次数：</td>
					<td colspan="3" class="col-md-10">
							<select name="compulsorytimes" id="compulsorytimes" style="width:auto" class="form-control " >
								<option value=""> -请选择- </option>
								<#list compulsorymatedataMaplist as compulsoryitem>
									<option value="${compulsoryitem.key}">${compulsoryitem.key}</option>
								</#list>
							</select>
					</td>
				</tr>
			  </table>
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
	</body>
</html>
