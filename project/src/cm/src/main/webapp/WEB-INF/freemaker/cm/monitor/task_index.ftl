
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "cm/monitor/monitor_task" ]);
</script>
<body> 
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">当前任务处理概况</div>
			<div class="row">
				<div class="col-md-12">
					<a style="color: #286090;" id="all">当天任务处理数:${todaytotal}</a>&nbsp;&nbsp;&nbsp;&nbsp;<a style="color: #286090;" id="suc">成功任务数:${todaysuc}</a>&nbsp;&nbsp;&nbsp;&nbsp;<a style="color: red;" id="error">失败任务数:${todayfail}</a> <br>
					<a style="color: #286090;" id="totalall">当前排队任务数:${todaytotalorder}</a>&nbsp;&nbsp;&nbsp;&nbsp;<a style="color: #286090;" id="quoteorder">报价排队:${todayqouteorder}</a>&nbsp;&nbsp;&nbsp;&nbsp;<a style="color: #286090;" id="insureorder">核保排队:${todayinsureorder}</a>&nbsp;&nbsp;&nbsp;&nbsp;<a style="color: #286090;" id="xborder">续保排队:${todayxborder}</a>&nbsp;&nbsp;&nbsp;&nbsp;<a style="color: #286090;" id="appovedorder">承保排队:${todayappovedorder}</a>&nbsp;&nbsp;&nbsp;&nbsp;<a style="color: #286090;" id="plateseracchorder">平台排队:${todayplateseracchorder}(暂未开发)</a>
					<input class="form-control" type="text"
							id="keyone" name="keyone" value="${keyone}"
							style="display:none;" >
				</div>
			</div>
		</div>
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">任务查询(仅支持精确查询)</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-md-12">
						<form role="form" id="userform">
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputCode">询价号	</label> <input type="text"
									class="form-control m-left-5" id="taskid" name="taskid"
									placeholder="">
							</div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputOrgName">车牌</label> <input
									class="form-control  m-left-5" id="plateno"
									name="plateno">
							</div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputOrgName">车主</label> <input
									class="form-control  m-left-5" id="carowername"
									name="carowername">
							</div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputName">任务类型</label> <select name="from"
									class="form-control  m-left-5" id="tasktype">
									<option value="" selected="selected">请选择</option>
									<option value="quote">报价</option>
									<option value="insure">核保</option>
									<option value="xb">续保</option>
									<option value="approved">承保</option>
									<option value="1">平台（暂未开发）</option>
								</select>
							</div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputName">任务状态</label> <select name="from"
									class="form-control  m-left-5" id="taskstatus">
									<option value="" selected="selected">请选择</option>
									<option value="2">成功</option>
									<option value="1">失败</option>
									<option value="0">执行中</option>
								</select>
							</div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputName">能力类型</label> <select name="from"
									class="form-control  m-left-5" id="quotetype">
									<option value="" selected="selected">请选择</option>
									<option value="robot">精灵</option>
									<option value="edi">EDI</option>
								</select>
							</div>
							<button id="querybuttonInfo" type="button" name="querybuttonInfo"
								class="btn btn-primary">查询</button>
							<button id="resetbuttonInfo" type="button" name=resetbuttonInfo
								class="btn btn-primary">重置</button>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="panel panel-default">
		  <div class="panel-heading padding-5-5">
		  <div class="row">
				<div class="col-md-2">
					任务列表
					</div>
			</div>
			</div>
		    <div class="row">
				<div class="col-md-12">
					<table id="table-javascript-1"></table>
				</div>
		  </div>
		</div>
		<div tabindex="-1" class="modal fade" id="taskBodyModal" role="dialog" aria-hidden="true" aria-labelledby="taskBodyModalLabel" style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button class="close" aria-hidden="true" type="button" data-dismiss="modal">×</button>
                    <h4 class="modal-title" id="taskBodyModalLabel"></h4>
                </div>
                <div class="modal-body">
                    <div id="taskBody" style="height: 460px;width: 510px;">
                    	<label for="message-text" class="control-label">文本内容 : </label>
						<textarea name="interfaceDesc" class="form-control text-area" id="message-text" rows="20" readonly="readonly" style="margin: 0px 14px 0px 0px; height: 414px; width: 490px;"></textarea>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-default" type="button" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
	</div>
</body>
</html>
