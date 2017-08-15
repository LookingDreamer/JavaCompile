<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>机构各网点时效分析表</title>
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
		<div class="panel-heading">机构各网点时效分析表</div>
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
					<label style="float:left;line-height:40px;padding-right:10px;"><font color="red">*</font>开始时间</label>
					<div style="float:left;"
						class="input-group date form_datetime col-sm-6"
						data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="starttime">
						<input class="form-control" style="color:black;" type="text" id="starttimeTxt" readonly value=""/>
						<span class="input-group-addon"> 
							<span class="glyphicon glyphicon-remove"></span>
						</span> 
						<span class="input-group-addon"> 
							<span class="glyphicon glyphicon-th"></span>
						</span>
					</div>
					<input type="hidden" id="starttime" name="starttime">
				</div>
				<div class="col-md-4">
					<label style="float:left;line-height:40px;padding-right:10px;"><font color="red">*</font>结束时间</label>
					<div style="float:left;"
						class="input-group date form_datetime col-sm-6"
						data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="endtime">
						<input class="form-control" style="color:black;" type="text" id="endtimeTxt" readonly value=""/>
						<span class="input-group-addon"> 
							<span class="glyphicon glyphicon-remove"></span>
						</span> 
						<span class="input-group-addon"> 
							<span class="glyphicon glyphicon-th"></span>
						</span>
					</div>
					<input type="hidden" id="endtime" name="endtime">
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<button type="button" name="Submit" id="Submit" class="btn btn-primary" onclick="doSearch();">查询</button>
				</div>
			</div>
		</div>
	</div>
	<input id="raq" name="raq" value="efficiencyOrgWebsite.raq" type="hidden">
</form>
<script>
    function doSearch(){
    	if(!$("#starttime").val() || !$("#endtime").val()){
    		alert("请输入开始时间和结束时间");
    		return;
    	}
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
