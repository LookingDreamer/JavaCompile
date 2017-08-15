<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>群组信息新增</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
requirejs([ "zzbconf/groupmrgedit" ]);
</script>
<body>
	<div class="container-fluid">
		<form role="form" id="group_form">
			<div class="panel panel-default m-bottom-5">
				<div class="panel-heading padding-5-5">群组基本信息</div>
				<div class="panel-body">
				<form  id="form_group_base_data" >
				<div class="row" >
					<table class="table table-bordered ">
						<tr>
							<td class="col-md-1" align="right" style="vertical-align: middle;">群组名称</td>
							<td class=" col-md-3">
							<input class="form-control" type="text" id="groupname" name="groupname"value="${comm.groupname!''}">
							<input  type="hidden" id="id" value="${comm.id }" name="id">
							<input  type="hidden" id="groupnum" value="${comm.groupnum }" name="groupnum">
							</td>
							<td class=" col-md-1" align="right" style="vertical-align: middle;">群组类型</td>
							<td class=" col-md-3">
								<select id="groupkind" name="groupkind" class="form-control">
										<option value="0">请选择</option>
										<#list groupType as gtoup>
											<option value="${gtoup.codevalue }"  <#if comm.groupkind==gtoup.codevalue>selected="selected"</#if>>${gtoup.codename }</option>
										</#list>	
								</select>
							</td>
							<td class=" col-md-1" align="right" style="vertical-align: middle;">群组编码</td>
							<td class=" col-md-3">
								<input type="text" class="form-control" id="groupcode" name="groupcode" onmouseout="ononon()" value="${comm.groupcode!''}">
							</td>
						</tr>
						<tr>
							<td class=" col-md-1" style="vertical-align: middle;" align="right">所属平台</td>
							<td class=" col-md-3">
							<input type="hidden" name="organizationid" id="deptid">
							<input type="hidden" name="pid" value="1">
								<input type="text" class="form-control" id="deptname"
								name="deptname" readonly="readonly" value="${groupDeptOrgName!''}"
								placeholder="请选择所属平台">
							</td>
							<td class=" col-md-1" align="right" style="vertical-align: middle;">是否生效</td>
							<td class=" col-md-3">
								<select class="form-control" name="privilegestate" id="privilegestate">
								<#list comm.privilegestate as privilegestate>
									<#if privilegestate=="1">
									<option selected="selected" id="suibian" value="${privilegestate }">是</option>
									<option  value="${privilegestate}">否</option>
									<#else>
									<option  id="suibian" value="${privilegestate }">是</option>
									<option selected="selected" value="${privilegestate}">否</option>
									</#if>
								</#list>
								</select>
							</td>
							<td class=" col-md-1" align="right" style="vertical-align: middle;">工作量</td>
							<td class=" col-md-3">
								<input type="text" class="form-control" id="workload" name="workload"  value="${comm.workload!''}">
							</td>
						</tr>
						<tr>
							<td class=" col-md-1" align="right" style="vertical-align: middle;">
								任务类型
							</td>
							<td class=" col-md-11" colspan="5">
								<table class="table table-bordered ">
				        			<tr>
										<td class="col-md-11">
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
					 			 </table>
							</td>
						</tr>
					</table>
					</div>
					<div class="panel-footer">
						<button  type="button" id="save_base_data_button" class="btn btn-primary" >保存</button>
						<!--<button  type="button" id="add_base_data_button" class="btn btn-primary" >新增</button>-->
						<button  type="button" class="btn btn-primary" id="go_back">返回</button>
				    </div>
				  </form>
				</div>
			</div>
		</form>
		<div id="menuContent" style="display: none; position: absolute;">
			<ul   id="dept_tree" class="ztree "
				style="margin-top: 0;background-color: gray;">
			</ul>

		</div>
		<div id="provider" style="display: none; position: absolute;">
			<ul   id="provider_tree" class="ztree "
				style="margin-top: 0;background-color: gray;">
			</ul>

		</div>
	</div>
</body>
</html>