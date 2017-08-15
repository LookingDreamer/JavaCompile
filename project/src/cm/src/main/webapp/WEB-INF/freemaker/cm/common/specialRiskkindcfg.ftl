<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改特殊险别配置信息弹出窗口</title>
<!--<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>-->
<style type="text/css">
	body {font-size: 14px;}
</style>
<script type="text/javascript">
	requirejs([ "cm/common/specialRiskkindcfg" ],function() {
		require([ "jquery", "bootstrap-table", "bootstrap", "bootstrapTableZhCn","public" ],function($) {
			$(function() {
				initEditSpecialRiskkindcfgScript();
			});
		});
	});
</script>
</head>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<h6 class="modal-title">${inscomname}-${riskkindname}配置信息<#if showEdit=="1">修改</#if></h6>
	</div>
	<div class="modal-body">
		<form class="form-inline" role="form" id="specialRiskkindcfgForm">
			<input id="mInstanceid" type="hidden" name="mInstanceid" value="${mInstanceid}"/><!--主流程id-->
			<input id="inscomcode" type="hidden" name="inscomcode" value="${inscomcode}"/><!--保险公司code-->
			<input id="riskkindcode" type="hidden" name="riskkindcode" value="${riskkindcode}"/><!--特殊险别code-->
			<input id="inscomcodeList" type="hidden" name="inscomcodeList" value="${inscomcodeList}"/><!--父页面所有保险公司code集合-->
			<input id="specialRiskkindcfgListSize" type="hidden" value="${specialRiskkindcfgList?size}"/><!--配值集合size-->
			<input id="showEdit" type="hidden" value="${showEdit}"/><!--是否显示修改标志-->
			<#if showEdit=="1">
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-primary" type="button" id="addKindcfgItem">添加栏位</button>
					</div>
				</div>
			</#if>
			<table class="table table-bordered" id="specialRiskkindcfgTable">
				<tr>
					<th class="col-md-2 active">序号</th>
					<th class="col-md-4 active">配置名称</th>
					<th class="col-md-4 active">配置值</th>
					<#if showEdit=="1">
						<th class="col-md-2 active">操作</th>
					</#if>
				</tr>
				<#list specialRiskkindcfgList as kindcfg>
					<tr class="kindcfgList" id="kindcfgList${kindcfg_index}">
						<td id="title${kindcfg_index}">
							${kindcfg_index+1}
						</td>
						<td>
							<input type="hidden" id="kindcfgId${kindcfg_index}" name="spcRiskkindCfg[${kindcfg_index}].id" value="${kindcfg.id}"/>
							<#if showEdit=="1">
								<input type="text" class="form-control required" id="kindcfgKey${kindcfg_index}" name="spcRiskkindCfg[${kindcfg_index}].cfgKey" value="${kindcfg.key}"/>
							<#else>
								${kindcfg.key}
							</#if>
						</td>
						<td>
							<#if showEdit=="1">
								<input type="text" class="form-control required isNumber" id="kindcfgValue${kindcfg_index}" name="spcRiskkindCfg[${kindcfg_index}].cfgValue" value="${kindcfg.value}"/>
							<#else>
								${kindcfg.value}
							</#if>
						</td>
						<#if showEdit=="1">
							<td>
								<a href="#" id="delKindcfg${kindcfg_index}" class="delKindcfg">删除</a>
							</td>
						</#if>
					</tr>
				</#list>
			</table>
			<div class="row">
				<div class="col-md-6">
					<#if showEdit=="1">
						<button class="btn btn-default" type="button" name="makesure" 
							id="makesure" title="确定">&nbsp;&nbsp;确定&nbsp;&nbsp;</button>
						<div class="checkbox">
							<label>
								<input type="checkbox" id="editToAll" name="editToAll" value="Y"/>修改到所有单方
							</label>
						</div>
					</#if>
				</div>
				<div class="col-md-6" align="right">
					<button class="btn btn-default" type="button" name="cancel" id="cancel" title="取消" data-dismiss="modal">
						<#if showEdit=="1">
							&nbsp;&nbsp;取消&nbsp;&nbsp;
						<#else>
							&nbsp;&nbsp;关闭&nbsp;&nbsp;
						</#if>
					</button>
				</div>
			</div>
		</form>
	</div>
</body>
</html>
