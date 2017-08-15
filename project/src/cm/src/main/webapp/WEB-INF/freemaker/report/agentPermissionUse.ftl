<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>代理人权限使用情况</title>
    <link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
    <link rel="stylesheet" href="${staticRoot}/css/modelinsurance/date.css">
    <script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
    <script type="text/javascript">
        requirejs([ "cm/report/report"]);
    </script>
</head>
<body>
<div class="container-fluid">
<form action="/cm/report/showReport" name="fm1" method="post" id="fm1" target="report_frame">
	<div class="panel panel-default">
		<div class="panel-heading">代理人权限使用情况</div>
		<div class="panel-body">
			<div class="row">
				<div class="col-md-4">
					<label><font color="red">*</font>机构平台<label><select class="form-control m-left-5" id="comcode" name="comcode" placeholder="请选择">
					     <option value="">请选择</option>
					   <#list deptList as row>
					     <option value="${row.id }">${row.name }</option>
					   </#list>
					</select>
				</div>

				<div class="col-md-4">
                        <label>代理人工号<label>
                            <input type="text" id="jobnum" name="jobnum">
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<button type="button" name="Submit" id="Submit" class="btn btn-primary" onclick="doSearch();">查询</button>
				</div>
			</div>
		</div>
	</div>
	<input id="raq" name="raq" value="agentPermissionUse.raq" type="hidden">
</form>
<script>
    function doSearch(){
    	if(!$("#comcode").val()){
    		alert("请选择平台机构");
    		return;
    	}
    	$.insLoading();
        $("#fm1").submit();
    }
</script>
</div>
<iframe id="report_frame" name="report_frame" style="width:100%; border: none;"></iframe>
</body>

</html>
