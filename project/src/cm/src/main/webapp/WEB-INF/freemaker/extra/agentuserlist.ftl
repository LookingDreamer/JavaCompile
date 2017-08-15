<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<style type="text/css"> 
        tr{height:40px;}
		td{vertical-align: left;}
</style>
<script type="text/javascript">
	requirejs([ "extra/agentuser" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">查 询</div>
			<div class="panel-body">
					<div class="col-md-12">
						<form class="form-inline" role="form" id="agentform">
						  <table  class="table table-bordered ">
                              <tr>
                                  <div class="row">
                                      <div class="form-group form-inline col-md-4">
                                          <td><label for="exampleInputCode">姓&nbsp;&nbsp;&nbsp;名&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                          <td><input class="form-control" id="name" type="text" name="name"></td>
                                      </div>
                                      <div class="form-group form-inline col-md-4" >
                                          <td><label for="exampleInputCode">手机号</label></td>
                                          <td><input class="form-control" type="text" id="phone" name="phone"></td>
                                      </div>
                                      <div class="form-group form-inline col-md-4" >
                                          <td><label for="exampleInputCode">mini用户工号</label></td>
                                          <td><input class="form-control" type="text" id="jobnum" name="jobnum"></td>
                                      </div>
                                  </div>
							  </tr>
							  <tr>
                                  <div class="row">
                                      <div class="form-group form-inline col-md-4">
                                          <td><label for="exampleInputCode">昵&nbsp;&nbsp;&nbsp;称&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                          <td><input class="form-control"  id="nickname" type="text" name="nickname"></td>
                                      </div>
                                      <div class="form-group form-inline col-md-4">
                                          <td><label for="exampleInputCode">认证状态</label></td>
                                          <td><select class="form-control" id="approvesstate" name="approvesstate">
                                              <option value="0">请选择</option>
										  <#list approveData as data>
                                              <option value="${data.codevalue }">${data.codename }</option>
										  </#list>
                                          </select></td>
                                      </div>
                                       <td><label for="exampleInputCode">mini用户角色</label></td>
                                      <td><select class="form-control" id="rname" name="rname">
                                              <option value="">请选择</option>
										  		<#list listRole as list>
										  		
										  			<option value="${list.name}">${list.name}</option>
										  		</#list>
                                
										  
                                          </select></td>
                                  </div>
							  </tr>
						  </table>

						  <table  class="table table-bordered " style="display:none;"><#-- 先隐藏不需要的查询条件 -->
                              <tr>
                                  <div class="row">
                                      <#--<div class="form-group form-inline col-md-4">
                                          <td><label for="exampleInputCode">姓&nbsp;&nbsp;&nbsp;名&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                          <td><input class="form-control" id="name" type="text" name="name"></td>
                                      </div>-->
                                      <div class="form-group form-inline col-md-4" >
                                          <td><label for="exampleInputCode">身份证号</label></td>
                                          <td><input class="form-control" type="text" id="idno" name="idno"></td>
                                      </div>
                                      <div class="form-group form-inline col-md-4">
                                          <td><label for="exampleInputCode">工&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号&nbsp;&nbsp;&nbsp;</label></td>
                                          <td><input class="form-control" type="text" id="agentcode" name="agentcode"></td>
                                      </div>
                                  </div>
						    </tr>
						    <tr>
								<div class="row">
									<div class="form-group form-inline col-md-4">
										<td><label for="exampleInputCode">所属机构</label></td>
											<td><input class="form-control" type="hidden" id="deptid" name="deptid" value="${agent.deptid!'' }"> 
											<input class="form-control" type="text" id="deptname" value="${agent.comname!'' }"></td>
									</div>
									<div class="form-group form-inline col-md-4">
										<td><label for="exampleInputCode">代理人状态</label></td>
											<td><select name="agentstatus" id="agentstatus" class="form-control">
												<option value="0">请选择</option>
												<option value="1">启用</option>
												<option value="2">停用</option>
											</select></td>
									</div>
									<#--<div class="form-group form-inline col-md-4">
										<td><label for="exampleInputCode">手机号码</label></td>
											<td><input class="form-control" type="text" id="phone" name="phone"></td>
									</div>-->
								</div>
						    </tr>
						    <tr>
								<div class="row">
									<div class="form-group form-inline col-md-4">
										<td><label for="exampleInputCode">用户类型</label></td>
											<td><select name="agentkind" id="agentkind" class="form-control">
												<option value="0">请选择</option>
												<option value="1">试用</option>
												<option value="2">正式</option>
												<option value="3">渠道</option>
											</select></td>
									</div>
									<div class="form-group form-inline col-md-4">
						       
										<td><label for="exampleInputCode">注册日期起</label></td>
											<td><input class="form-control date form_datetime" type="text"
													id="registertimestr" name="registertimestr"></td>
									</div>
									<div class="form-group form-inline col-md-4">
										<td><label for="exampleInputCode">注册日期止</label></td>
											<td><input class="form-control date form_datetime" type="text"
													id="registertimeendstr" name="registertimeendstr"></td>
									</div>
								</div>
						    </tr>
						    <tr>
								<div class="row">
									<div class="form-group form-inline col-md-4">
									    <td><label for="exampleInputCode">验证日期起</label></td>
										<td><input class="form-control date form_datetime" type="text"
													id="testtimestr" name="testtimestr"></td>
									</div>
									<div class="form-group form-inline col-md-4">
										<td><label for="exampleInputCode">验证日期止</label></td>
											<td><input class="form-control date form_datetime" type="text"
													id="testtimeendstr" name="testtimeendstr"></td>
									</div>
									<#-- <div class="form-group form-inline col-md-4">
										<td><label for="exampleInputCode">处理跟踪状态</label></td>
											<td><input class="form-control" type="text" id=""></td>
									</div> --><#-- 暂时隐藏 -->
								</div>
						    <#-- </tr>
						    <tr> -->
						    </tr>
						  </table>							
						</form>
					</div>
				</div>
			</div>
			<div class="panel-footer">

				<button id="querybutton_test" type="button" name="querybutton"
					class="btn btn-primary">查询</button>
				<button id="resetbutton" type="button" name="resetbutton"
					class="btn btn-primary">重置</button>
			</div>
		</div>
		<div class="panel panel-default">
			<div class="panel-heading padding-5-5">
				<div class="row"> 
					<div class="col-md-2">查询结果</div>
					<div class="col-md-10" align="right">
						<button id="z_querybutton" type="button" name="querybutton"
									class="btn btn-primary">增加推荐人</button>
                        <button class="btn btn-primary" type="button" id="update">详细</button>
						<button class="btn btn-primary" type="button" id="detail" style="display: none">核心数据查看</button>
						<#-- <button class="btn btn-primary" type="button" title="批量删除" id="bound_jobno">绑定工号</button> --><#-- 去掉，移到修改页面 -->
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table id="agent_list"></table>
				</div>
			</div>
		</div>
	<div class="modal fade" id="myModal_dept" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content">
				<div class="modal-body" style="overflow: auto; height: 400px;">
					<div id="dept_tree" class="ztree"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="updateRoleModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span class="label label-danger">X</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">修改角色，请注意！</div>
                        <div class="panel-body">
                           
                            <table class="table-no-bordered">
                               
                               
                                <tr>
                                    <td class="text-right"><label>用户角色：</label></td>
                                    <td style="padding-bottom:5px;"><select class="form-control" id="s_rname" name="rname">
                                             
										  
                                              <#list listRole as list>
										  		
										  			<option value="${list.rname}">${list.name}</option>
										  		</#list>
                                
										  
                                          </select></td>
                                </tr>
                                <tr style="display: none;">
                                    <td class="text-right"><label>id：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control readonlysetting disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_id" name="id"
                                                                           /></td>
                                </tr>
                                <tr style="display: none;">
                                    <td class="text-right"><label>openid：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control readonlysetting disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_openid" name="openid"
                                                                           /></td>
                                </tr>
                                
                                
                            </table>
                            <div class="col-md-12" style="font-weight: bold; padding-top: 5px;">是否保存？</div>
                        </div>
                        <div class="panel-footer">
                            <input id="s_execButton" name="s_execButton" type="button" class="btn btn-primary" value="确定"/>
                            <input id="closeButton" name="closeButton" class="btn btn-primary" type="button"
                                   data-dismiss="modal" value="取消"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
	<div class="modal fade" id="addReferreridModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" >
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span class="label label-danger">X</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" >
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">添加推荐人，请注意！</div>
                       	<form id="addReferrFrom" class="form-inline" role="form" >
                       	<table  class="table table-bordered ">
                       		<tr>
                                    <td class="text-right"><label>手机号码：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                           type="text" id="x_phone" name="phone"
                                                                           /></td>
                               
                                                            
                                
                       			</tr>
                       		<tr style="display: none">
                                    <td class="text-right"><label>角色：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                           type="text" id="x_rolecode" name="rolecode" value="01"
                                                                           /></td>
                               
                                                            
                                
                       			</tr>
                       	
                       	</table>
                       	</form>
                       	 <button id="x_querybutton" type="button" name="querybutton"
									class="btn btn-primary">查询</button>
                        <div class="panel-body">
                           
                            <div class="row">
						<div class="col-md-12">
							<table id="agent_plist"></table>
						</div>
					</div>
                           
                        </div>
                        <div class="panel-footer">
                            <input id="x_execButton" name="x_execButton" type="button" class="btn btn-primary" value="保存"/>
                            <input id="x_closeButton" name="closeButton" class="btn btn-primary" type="button"
                                   data-dismiss="modal" value="取消"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
	<div class="modal fade" id="myModal_agent_detail" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content"></div>
		</div>
	</div>
</body>
</html>
