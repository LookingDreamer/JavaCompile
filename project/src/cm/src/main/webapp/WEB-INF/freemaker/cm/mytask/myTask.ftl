<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的任务</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "cm/mytask/myTask" ]);
</script>
<style type="text/css">
.workflowinfo{
	padding:0px;
}
.workflowinfo li{
	float:left;
	list-style-type:none;
}
.workflowinfo li div{
	display:inline;
	padding-bottom:10px;
	padding-top:10px;
}
.mybtn{
	height:35px;
}
.shortlabel{
	margin-right:25px;
}
.showright{
	position:relative;
}
img{
	position:absolute;
	top:7px;
	right:30px;
}
#task-query{
	margin-top: 0px;
    color: #8a6d3b;
    background-color: #fff6bb;
    border-color: #e5e5e5;
    margin-bottom: 0px;
    position: fixed;
    top: 0px;
    z-index: 99999;
    width: 100%;
}
#myTaskSon{
	margin-top: 45px;
}

</style>
</head>
<body>
	<div class="container-fluid tasks">
		<div class="panel panel-default">
			<!-- 搜索  -->
			<div class="panel-heading" id="task-query">
				<!--注释掉响应键盘enter事件<form class="form-inline" role="form" id="queryMySimpleTask" method="post" action="queryMySimpleTask">-->
					<div class="row">
						<div class="col-md-6 col-xs-10" align="left">
							<input id="simplequery" type="text" placeholder="请输入车牌或被保人" class="">
							<a id="querybutton" href="javascript:fmsubmit();"><span class="glyphicon glyphicon-search"></span></a>&nbsp;&nbsp;&nbsp;
							<a id="querybutton" href="javascript:refreshTask();"><span class="glyphicon glyphicon-refresh"></span></a>
							<input id="currentpage" type="hidden" value="${allData.myTaskVo.currentpage}">
						</div>
						<div class="col-md-6 col-xs-2" align="right">
							<a id="taskViewList" style="display:none;" href="javascript:$.cmTaskList('my', 'list4deal', false);">
								<span class="glyphicon glyphicon-step-backward" ></span></a>
						</div>
					</div>
				<!--</form>-->
			</div>
			<!-- 待处理任务 -->
			<div id="myTaskSon" class="tasksview"><#include "cm/mytask/myTaskSon.ftl" /></div>
		</div>
	</div>
</body>
</html>
