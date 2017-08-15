<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代理人管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<style type="text/css"> 
        tr{height:40px;}
		td{vertical-align: left;}
</style>
<script type="text/javascript">
	requirejs([ "zzbconf/agent" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">查询</div>
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
									<div class="form-group form-inline col-md-4">
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
									<div class="form-group form-inline col-md-4">
										<td><label for="exampleInputCode">手机号码</label></td>
											<td><input class="form-control" type="text" id="phone" name="phone"></td>
									</div>
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
								<div class="row">
									<div class="form-group form-inline col-md-4">
										<td><label for="exampleInputCode">认证状态</label></td>
										<td><select class="form-control" id="approvesstate" name="approvesstate">
											<option value="0">请选择</option>
											<#list approveData as data>
												<option value="${data.codevalue }">${data.codename }</option>
											</#list>
									    </select></td>
									</div>
								</div>		
						    </tr>
						  </table>							
						</form>
					</div>
				</div>
			</div>
			<div class="panel-footer">
				<button id="querybutton" type="button" name="querybutton"
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
					<button class="btn btn-primary" type="button" id="batchUseFuncs">批量关联权限包</button>
                    <button class="btn btn-primary" type="button" id="querybuttonlist">保单查询</button>
                     <!--<button class="btn btn-primary" type="button" id="add">新增</button>-->
                    <button class="btn btn-primary" type="button" id="update">修改</button>
                    <button class="btn btn-primary" type="button" id="delete">删除</button>
                    <button class="btn btn-primary" type="button" id="resetpwd">密码重置</button>
                    <button class="btn btn-primary" type="button" id="detail">核心数据查看</button>
                    <button class="btn btn-primary" type="button" id="t_handle">处理跟踪</button>
                    <button class="btn btn-primary" type="button" title="绑定工号" id="bound_jobno">绑定工号</button> <#-- 去掉，移到修改页面 -->
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
	<div class="modal fade" id="myModal_agent_detail" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content"></div>
		</div>
	</div>

 <div id="showdeptpic" class="modal fade" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-body" style="overflow: auto; height: 400px;">
                    <div class="container-fluid">
	                    <div class="row">
		                 	<h2>批量添加权限包</h2>
		                 	<hr></hr>
		           		 </div>
                        <div class="row">
								<label  for="funcs" >第一步:选择需要关联的权限包</label>
                                <select   id="funcs" name="funcs" class="form-control" style="width:220px;">
                                </select>
						</div>
						 <div class="row">
								<label  for="funcs2" >第二步:输入需要被关联的工号或手机号(支持同时添加多个,每行一个工号或手机号)</label>
								<textarea id="funcsDeptid" style="resize:none;width:560px;height:190px;"> </textarea>
                        </div>
                       <!-- 
                         <div class="row">
                            <h4 class="modal-title" id="gridSystemModalLabel">应用机构或网点</h4>
                            <ul id="treeDemoDept" class="ztree"></ul>
                        </div>  
                         <input type="hidden" id="funcsDeptid"/>   
                         -->
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-primary" type="button" id="funcsOK">确定</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>

<div id="showdeptpic1" class="modal fade" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-body" style="overflow: auto; height: 400px;">
                   <h1>全部账号关联成功</h1>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
    
    <div id="showdeptpic2" class="modal fade" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-body" style="overflow: auto; height: 400px;">
                    <div class="container-fluid">
                        <h1>权限包关联失败</h1>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>


</body>
</html>
