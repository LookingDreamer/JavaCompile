<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">

<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>

<title>计划管理</title>
<script type="text/javascript">
	requirejs([ "system/scheduleList" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default">
			<div class="panel-heading padding-5-5 m-bottom-5">
				<div class="row">
					<div class="col-md-10" align="left">
						<button id="addNewbtn" class="btn  btn-primary" type="button" data-toggle="modal" data-target="#modal-content" >新建</button>
						<button id="modifybtn" class="btn btn-primary " type="button">修改</button>
						<button id="deletebtn" class="btn  btn-primary" type="button">删除</button>
						<button id="handExebtn" class="btn  btn-primary" type="button">手动执行</button>
						<button class="btn btn-primary" type="button" name="refresh"
											title="Refresh" id="refresh">
											<i class="glyphicon glyphicon-refresh icon-refresh"></i>
										</button>
						<button class="btn btn-primary" type="button" name="toggle" title="Toggle" id="toggle">
												<i class="glyphicon glyphicon-list-alt icon-list-alt"></i>
										</button>
					</div>
				</div>
			</div>
			<div class="row">
					<div class="col-md-12">
						<table id="scheduleListTableId"></table>
					</div>
			</div>
		</div>
	</div>
</body>
</html>
