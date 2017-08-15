<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>补充信息</title>
		<!--<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>-->
		<style type="text/css">
			span.feildtitle {
				font-weight:bold;
				color:#34495E;
			}
			body {font-size: 14px;}
		</style>
		<!--引入修改补充信息信息弹出窗口js文件-->
		<script type="text/javascript">
			requirejs([ "cm/manualprice/editAddMessage" ],function() {
				require([ "jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public"], function($) {
					$(function() {
						initEditAddMessageScript();
					});
				});
			});
		</script>
	</head>
	<body>
		<!--修改补充信息弹出框-->
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">
				<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
			</button>
			<h6 class="modal-title">修改补充信息</h6>
		</div>
		<!--被保人信息-->
		<div class="modal-body">
			<form class="form-inline" role="form" id="editlocaldbreplenishinfo">
				<div class="panel panel-default" style="margin-bottom:0px">
					<div class="panel-heading">
						<div class="row">
							<div class="col-md-12">
								<span class="feildtitle">补充信息<input type="hidden" value="${replenishSelectItemsInfo?size}" id="paramcount"/></span>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<table class="table table-bordered table-condensed table-striped" style="margin-bottom:0px">
								<#if (replenishSelectItemsInfo?size) gt 0>
									<#list replenishSelectItemsInfo as selectItem>
										<#if (selectItem_index % 2) = 0>
											<tr>
												<td class="bgg col-md-2" id="${selectItem.keyid}CnName">${selectItem.metadataName}</td>
							    				<td class="col-md-4">
													<#if (selectItem.metadataValueMapList)??>
										    			<select class="form-control" name="${selectItem.keyid}" id="metadataValue${selectItem_index}">
										    				<#list selectItem.metadataValueMapList as selItem>
																<option value="${selItem.value}@${selectItem.keyid}" <#if selItem.value == selectItem.metadataValueKey>selected</#if>>${selItem.key}</option>
															</#list>
														</select>
														<!--下拉选项值集合-->
														<div id="${selectItem.keyid}valueList" style="display:none;">
															<#list selectItem.metadataValueMapList as selItem>
																<span id="${selectItem.keyid}_${selItem.value}">${selItem.key}</span>
															</#list>
														</div>
										    		<#else>
							    						<input type="text" class="form-control <#if selectItem.isCheck == 'Y'>isCheck</#if>" id="metadataValue${selectItem_index}" name="${selectItem.keyid}" value="${selectItem.metadataValueKey}"/>
										    		</#if>
										    	</td>
										    <#if selectItem_has_next>
										    <#else>
											    	<td class="col-md-2"></td>
													<td class="col-md-4"></td>
												</tr>
										    </#if>
										<#else>
												<td class="bgg col-md-2" id="${selectItem.keyid}CnName">${selectItem.metadataName}</td>
							    				<td class="col-md-4">
													<#if (selectItem.metadataValueMapList)??>
										    			<select class="form-control" name="${selectItem.keyid}" id="metadataValue${selectItem_index}">
										    				<#list selectItem.metadataValueMapList as selItem>
																<option value="${selItem.value}@${selectItem.keyid}" <#if selItem.value == selectItem.metadataValueKey>selected</#if>>${selItem.key}</option>
															</#list>
														</select>
														<!--下拉选项值集合-->
														<div id="${selectItem.keyid}valueList" style="display:none;">
															<#list selectItem.metadataValueMapList as selItem>
																<span id="${selectItem.keyid}_${selItem.value}">${selItem.key}</span>
															</#list>
														</div>
										    		<#else>
							    						<input type="text" class="form-control <#if selectItem.isCheck == 'Y'>isCheck</#if>" id="metadataValue${selectItem_index}" name="${selectItem.keyid}" value="${selectItem.metadataValueKey}"/>
										    		</#if>
										    	</td>
									    	</tr>
										</#if>
							    	</#list>
								<#else>
									<tr>
										<td class="col-md-12">
											<span id="initFailFlag" style="color:#FF0000">补充信息选项初始化错误，请稍后重试！</span>
										</td>
									</tr>
								</#if>
							</table>
						</div>
					</div>
				</div>
				<div class="row">
				 	<div class="col-md-6">
				 		<button class="btn btn-default" type="button" id="makesurepre" title="确定">&nbsp;&nbsp;确定&nbsp;&nbsp;</button>
				 		<div id="div1" style="display:none;"><#list inscomcodes as inscom><div><input type="checkbox" name="inscom"  checked="true"  value="${inscom.inscomcode}"/>${inscom.inscomname}</div></#list>
				 			<div class="btnsbar"><button class="btn btn-primary passInsurance" type="button" 
							id="makesure" title="确定">确定</button></div>
				 		</div>
				 	</div>
				 	<div class="col-md-6" align="right">
				 		<button class="btn btn-default" type="button" title="取消" data-dismiss="modal">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>
				 	</div>
				 	<input type="hidden" id="num" name="num" value="${num}"/>
					<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}"/>
					<input type="hidden" id="inscomcode" name="inscomcode" value="${inscomcode}"/>
				</div>
			</form>
		</div>
		<script>
			var oBtn = document.getElementById('makesurepre');
			var oDiv = document.getElementById('div1');
			oBtn.onclick=function(){
				oDiv.style.cssText = 'position:absolute; bottom:0px; left:0px; width:200; height:100; z-index:10; background:#ccc; display:block;';
			};
		</script>
	</body>
</html>