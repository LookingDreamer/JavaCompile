<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的历史任务</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "cm/mytask/myHistoryTask" ]);
</script>

<style type="text/css">

table {
	margin-top: 5px;
	border: 2px solid gray;
	width: 100%;
}

td {
	padding: 2px;
	border: 1px solid #bbbbbb;
	/* height: 50px; */
	width: 50%;
}

.graybg1 {
	background-color: #cccccc;
}

.graybg2 {
	background-color: #eeeeee;
}

.innertable {
	border: none;
	margin: 0px;
}

.innertable td {
	border: none;
	height: 100%;
}

.padding-6 {
	padding: 6px;
}

.float_right{
	float: right;
}

.form-inline{
	background-color: #ffffff;
}

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

</style>

</head>

<body>
	<div class="panel panel-default m-bottom-5">
		<div class="panel-body">
			<div class="panel panel-default m-bottom-5 " id="querypanel">
				<div class="panel-heading padding-5-5">
					<form class="form-inline" role="form" id="cartaskmanageform">
						<div class="row  padding-6">
							<div class="col-md-12">
								<div class="form-group col-md-4 form-inline">
									<label for="exampleInputCarlicenseno">任务号:</label> <input type="text" class="form-control m-left-2"
										id="maininstanceid" name="maininstanceid" placeholder=""/>
								</div>
								<div class="form-group col-md-4 form-inline">
									<label for="exampleInputCarlicenseno">车牌号:</label> <input type="text" class="form-control m-left-2"
										id="carlicenseno" name="carlicenseno" placeholder=""/>
								</div>
								<div class="form-group col-md-4 form-inline">
									<label for="exampleInputCarlicenseno">被保人:</label> <input type="text" class="form-control m-left-2"
										id="bname" name="bname" placeholder=""/>
								</div>
							<div>
						</div>
						<p>&nbsp;</p>
						<div class="row padding-6">
							<div class="col-md-12">
								<div class="form-group col-md-8 form-inline">
									<label for="exampleInputTaskcreatetime">任务创建时间:</label>
									<div style=""
										class="input-group date form_datetime col-sm-4"
										data-date-format="yyyy-mm-dd" data-link-field="taskcreatetimeup">
										<input class="form-control" style="color:black;" type="text" id="taskcreatetimeupTxt" readonly value=""/>
										<span class="input-group-addon"> 
											<span class="glyphicon glyphicon-remove"></span>
										</span> 
										<span class="input-group-addon"> 
											<span class="glyphicon glyphicon-th"></span>
										</span>
									</div>
									<input type="hidden" id="taskcreatetimeup" name="taskcreatetimeup" value=""/> 
									
									<label for="exampleInputTaskcreatetime">至</label>
									<div style=""
										class="input-group date form_datetime col-sm-4"
										data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="taskcreatetimedown">
										<input class="form-control" style="color:black;" type="text" id="taskcreatetimedownTxt" readonly value=""/>
										<span class="input-group-addon"> 
											<span class="glyphicon glyphicon-remove"></span>
										</span> 
										<span class="input-group-addon"> 
											<span class="glyphicon glyphicon-th"></span>
										</span>
									</div>
									<input type="hidden" id="taskcreatetimedown" name="taskcreatetimedown" value=""/> 
								</div>
							</div>
						</div>
		</form>
						<div class="panel-footer padding-5-5">
							<div class="row">
								<div class="col-md-12">
										<div class="form-group col-md-12" style="margin-bottom:0px" align="right">
											<button id="generalquerybutton" type="button" class="btn btn-primary"
												name="generalquerybutton">搜索</button>
											<button id="generalrefreshBtn1" type="button" name="generalrefreshBtn1" 
												class="btn btn-primary generalrefreshBtn">刷新</button>
											<button id="generalresetbutton" type="button" name="generalresetbutton" 
												class="btn btn-primary">重置</button>
										</div>
								</div>
							</div>
						</div>
						<div class="col-md-6 col-xs-2" align="right">
							<a id="taskViewList" style="display:none;" href="javascript:$.cmTaskList('my', 'list4deal', false);">
								<span class="glyphicon glyphicon-step-backward" ></span></a>
						</div>
				</div>
			</div>
				<!-- 待处理任务 -->
			<div>
				<div id="myOrderManage" class="tasksview">
					<!-- 任务列表 -->
					<div class="panel panel-default m-bottom-5" id="myOrderManageResultcontent">
						<div class="panel-body" id="myOrderManageResultList">
							正在努力的加载数据中！请稍后…………
						</div>
					</div>
				</div>
			</div>	
		</div>
	</div>
</body>
</html>
