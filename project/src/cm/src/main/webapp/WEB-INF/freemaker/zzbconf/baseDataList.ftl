<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>基础数据</title>
<style type="text/css">
.m-left {
	margin-left: 10px;
}

.m-top {
	margin-top: 10px;
}

.m-buttom {
	margin-bottom: 10px;
}

.m-buttom input {
	height: 35px;
	font-size: 13px;
	width: 100px;
}

.m-buttom label {
	height: 35px;
	font-size: 13px;
}

.m-buttom button {
	height: 35px;
	font-size: 12px;
}
</style>
<link href="${staticRoot}/css/appmain.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" media="screen" href="../static/css/lib/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="../static/css/lib/ui.jqgrid.css" />
<script data-ver="${jsver}" data-main="../static/js/load" src="../static/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "zzbconf/baseData" ]);
</script>	
<body>
<form id="base_data_from" class="form-inline  m-buttom">
		<div class="form-group m-top ">
			<div class="form-group m-left">
				<label for="exampleInputCode">机构编码</label> <input type="text"
					class="form-control" id="nodeCode" name="nodeCode" placeholder="">
			</div>

			<div class="form-group m-left">
				<label for="exampleInputName">名称</label> <input type="email"
					class="form-control" id="nodeName" name="nodeName" placeholder="">
			</div>

			<div class="form-group m-left">
				<label for="exampleInputOrgName">简称</label> <input type="email"
					class="form-control" id="shortName" name="shortName" placeholder="">
			</div>
		</div>
		<div class="form-group ">
			<button id="querybutton" type="button" name="querybutton"
				class="btn btn-sm m-left">查询</button>
			<button id="benchdeletebutton" type="button" name="benchdeletebutton"
				class="btn btn-sm  m-left">批量删除</button>
			<button id="resetbutton" type="button" name="resetbutton"
				class="btn btn-sm  m-left">重置</button>
			<button id="addbutton" type="button" name="resetbutton"
				class="btn btn-sm  m-left">新增</button>
		</div>
	</form>
	
	<table id="jqGrid"></table>
    <div id="jqGridPager"></div>
       
</body>
</html>
