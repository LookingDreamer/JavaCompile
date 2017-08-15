<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>任务组管理/</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript"> requirejs([ "zzbconf/tasksetedit" ]);</script>
<body>
<div class="container-fluid">
	<form role="form" action="saveorupdatetasksetdata" id="group_form" method="post">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">任务组信息</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-md-12">
						<form action="">
						<table class="table table-bordered ">
							<tr>
								<td class="col-md-2 " align="right"
									style="vertical-align: middle;">任务组名称：</td>
								<td class="col-md-10"  colspan="5">
									<input type="hidden" name="id" id="id" value="${setModel.id }">
									<input class="form-control"
									type="text" name="setname" value="${setModel.setname }">
								</td>
							</tr>
							<tr>
								<td class="col-md-2" style="vertical-align: middle;" align="right">任务组所属平台：</td>
								<td class="col-md-5"  colspan="2">
									<select class="form-control"  id="dept_parent_id">
										<option selected="selected" >请选择</option>
										<#list deptParentList as dp>
											<#if deptModel.upcomcode==dp.comcode>
											<option selected="selected" value="${dp.comcode }">${ dp.comname}</option>
											<#else>
											<option value="${dp.comcode }">${ dp.comname}</option>
											</#if>
										</#list>
									</select>
									</td>
									<td class="col-md-5"  colspan="3">
									<select class="form-control" id="dept_id" name="deptid">
										<#list deptList as de>
											<#if deptModel.comcode==de.comcode>
												<option selected="selected" value="${de.comcode }">${ de.comname}</option>
											<#else>
												<option  value="${de.comcode }">${ de.comname}</option>
											</#if>
										</#list>
									</select>
								</td>
							</tr>
							<tr>
								<td class="col-md-32" align="right"
									style="vertical-align: middle;">任务组状态：</td>
								<td class="col-md-10"  colspan="5">
									<#if setModel.setstatus==2>
										<input type="radio" name="setstatus" value="1">启用 
										<input type="radio" checked="checked"  name="setstatus" value="2">停用
									<#else>
										<input type="radio"  name="setstatus" checked="checked" value="1">启用 
										<input type="radio" name="setstatus" value="2">停用
									</#if>
								</td>
							</tr>
							<tr>
								<td class="col-md-2 " align="right"
									style="vertical-align: middle;">业管组调度策略：</td>
								<td class="col-md-10"  colspan="5">
									<#if setModel.shedulingpolicy==1>
										<input name="shedulingpolicy" checked="checked" value="1" type="radio">最短处理时间&nbsp;&nbsp;&nbsp;
										<input name="shedulingpolicy" type="radio" value="2">顺序优先
									<#elseif setModel.shedulingpolicy==2>
										<input name="shedulingpolicy" value="1" type="radio">最短处理时间&nbsp;&nbsp;&nbsp;
										<input name="shedulingpolicy" checked="checked" type="radio" value="2">顺序优先
									
									<#else>
									<input name="shedulingpolicy" checked="checked" value="1" type="radio">最短处理时间&nbsp;&nbsp;&nbsp;
									<input name="shedulingpolicy" type="radio" value="2">顺序优先
									</#if>
								</td>
							</tr>
							<tr>
								<td class="col-md-2" style="vertical-align: middle;" align="right">保险公司：</td>
								<td class="col-md-10"  colspan="5">
									<input id="provider_name" class="form-control" type="text" readonly value="${providerName }"  /> 
									<input class="form-control" type="hidden" id="provider_id" value="${setModel.providerid }" name="providerid">
								</td>
							</tr>
							<tr>
								<td class="col-md-2" style="vertical-align: middle;" align="right">任务类型：</td>
								<td class="col-md-10" colspan="5">
									<div class="col-xs-5">
										<select name="from" class="js-multiselect form-control" size="8" multiple="multiple">
										  	<#list taskType as all>
										  		<option value="${all.codevalue }">${all.codename }</option>
										  	</#list>
										</select>
									</div>					
									<div class="col-xs-2">
										<button type="button" id="js_right_All_1" class="btn btn-block"><i class="glyphicon glyphicon-forward"></i></button>
										<button type="button" id="js_right_Selected_1" class="btn btn-block"><i class="glyphicon glyphicon-chevron-right"></i></button>
										<button type="button" id="js_left_Selected_1" class="btn btn-block"><i class="glyphicon glyphicon-chevron-left"></i></button>
										<button type="button" id="js_left_All_1" class="btn btn-block"><i class="glyphicon glyphicon-backward"></i></button>
									</div>					
									<div class="col-xs-5">
										<select name="tasktype" id="js_multiselect_to_1" class="form-control" size="8" multiple="multiple">
											<#list oldtaskType as all>
										  		<option value="${all.codevalue }">${all.codename }</option>
										  	</#list>
										</select>
									</div>
								</td>
							</tr>
							<tr>
								<td class="col-md-2 " align="right" style="vertical-align: middle;">任务组描述：</td>
								<td class="col-md-10" colspan="5">			
									<textarea  id="setdescription" style="height:150px;max-height:250px;resize:none;overflow:auto;"
										name="setdescription" class="form-control">${setModel.setdescription}</textarea>
								</td>
							</tr>
							<tr>
								<td class="col-md-2" style="vertical-align: middle;" align="right" >出单网点：</td>
								<td class="col-md-2">
									<select class="form-control" id="deptidc1" name="deptid1" >
										<option value="" selected="true" disabled="true">请选择</option>
										<#list deptParentList as dp>
											<#if deptModel.upcomcode==dp.comcode>
											<option  value="${dp.comcode }">${ dp.comname}</option>
											<#else>
											<option value="${dp.comcode }">${ dp.comname}</option>
											</#if>
										</#list>
									</select>
								</td>
								<td class="col-md-2">
									<select class="form-control" id="deptidc2" name="deptid2">
									</select> 
								</td>
								<td class="col-md-2">
									<select class="form-control" id="deptidc3" name="deptid3" >
								</select> 
								</td>
								<td class="col-md-2">
									<select class="form-control" id="deptidc4" name="deptid4">	
								</select> 
								</td>
								<td class="col-md-2">
									<select class="form-control" id="deptidc5" name="deptid5">	
									</select>
								</td>
							</tr>
							<tr>
								<td class="col-md-2" style="vertical-align: middle;" align="right">已关联网点：</td>
								<td class="col-md-10" colspan="5">
									<div  class="col-md-12">
										<button type="button" id="insert_dept"  class="btn btn-primary">关联</button>
										<button type="button" id="delete_dept" class="btn btn-primary" >取消关联</button>
									</div>
									<div  class="col-md-12">
									<table id="tasksetscop_list"></table>
									</div>
								</td>
							</tr>
						</table>
						</form>
						<div id="provider" style="display: none; position: absolute;">
							<ul   id="provider_tree" class="ztree "
								style="margin-top: 0;background-color: gray;">
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="panel-footer">
				<button type="submit"  class="btn btn-primary">保存</button>
					<button type="button" id="go_back" class="btn btn-primary" >返回</button>
			</div>
		</div>
	</form>
</div>
</body>
</html>