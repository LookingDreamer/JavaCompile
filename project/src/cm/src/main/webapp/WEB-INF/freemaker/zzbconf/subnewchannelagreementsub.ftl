<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>选择供应商</title>
	<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>
	<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
	<script type="text/javascript">
		requirejs([ "zzbconf/subnewchannelagreementsub" ],function() {
			require([  "jquery", "zTree", "zTreecheck", "flat-ui", "bootstrap-table", "fuelux", "bootstrap", "bootstrapTableZhCn","public"],function($) {
				$(function() {
					initsubnewchannelagreementsub();
				});
			});
		});
	</script>
</head>
<body>
	查询机构：<input id="province" value=""/>
	 <input type="hidden" name="channelid" id="channelid" value=${channelid} />
	<div class="container-fluid" style="margin-bottom:30px">
		<#list provider as providers>
		  <input type="checkbox" name="provider" id="provider" value=${providers.id}/>${providers.prvname}
		   <input type="hidden" name="agreementid" id="agreementid" value=${providers.agreementid} /><br/>
	   </#list>
	   <button class="btn btn-primary" type="button" id="checkProid" title="showAll">确定</button>
	   <button class="btn btn-primary" type="button" id="closeProid">取消</button>
	</div>
</body>
</html>
