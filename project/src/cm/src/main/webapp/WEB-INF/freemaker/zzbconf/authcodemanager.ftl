<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>授权码列表</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/zTree/css/demo.css" rel="stylesheet">
<link href="${staticRoot}/css/lib/zTree/css/zTreeStyle/zTreeStyle.css"
	rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "zzbconf/authcodemanager" ]);
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="panel panel-default m-bottom-5">
				<div class="panel-heading padding-5-5"><p id="title_role"></p></div>
				<div class="panel-body">
					<div class="row">
						<div class="col-md-12">
							<ul id="mytab" class="nav nav-tabs">
								<li class="active"><a href="#basedata" data-toggle="tab">授权码管理</a></li>
								<li><a href="#stationdata" data-toggle="tab">快刷激活码管理</a></li>
							</ul>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="tab-content">
								<div class="tab-pane active" id="basedata">
<!-- 									<div class="panel-heading padding-5-5"> -->
<!-- 								  		<div class="row"> -->
<!-- 											<div class="col-md-2"> -->
<!-- 												edi处理器列表 -->
<!-- 											</div> -->
<!-- 											<div class="col-md-10" align="right"> -->
<!-- 												<button class="btn btn-primary" type="button"  id="addedi" title="新增" >新增</button> -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</div> -->
									<!-- <div class="panel-heading padding-5-5">查询</div>-->
										<div class="row">
											<div class="col-md-12">
												<form class="form-inline" role="form" id="agentform">									
													<div class="form-group form-inline col-md-4">
														<label for="exampleInputCode">授权码</label> 
														<input class="form-control m-left-4" id="name" type="text" name="name">
													</div>
													<div class="form-group form-inline col-md-4">
														<label for="exampleInputCode">授权码类型</label> 
														<select name="agentkind" id="agentkind" class="form-control m-left-5">
															<option value="0">请选择</option>
															<option value="1">个人</option>
															<option value="2">网点</option>
														</select>
													</div>
													<div class="form-group form-inline col-md-4">
														<label for="exampleInputCode">IMEI/SN</label> 
														<input class="form-control m-left-3" id="name" type="text" name="name">
													</div>
													<div class="form-group form-inline col-md-4">
														<label for="exampleInputCode">设备来源</label> 
														<select name="agentstatus" id="agentstatus" class="form-control m-left-3">
															<option value="0">请选择</option>
															<option value="1">保网机</option>
															<option value="2">自备机</option>
														</select>
													</div>
													<div class="form-group form-inline col-md-4">
														<label for="exampleInputCode">操作系统</label> 
														<select name="agentkind" id="agentkind" class="form-control m-left-2">
															<option value="0">请选择</option>
															<option value="1">android</option>
															<option value="2">ios</option>
														</select>
													</div>	
													<div class="form-group form-inline col-md-4">
														<label for="exampleInputCode">掌中宝绑定状态</label> 
														<select name="agentkind" id="agentkind" class="form-control">
															<option value="0">请选择</option>
															<option value="1">已绑定</option>
															<option value="2">未绑定</option>
														</select>
													</div>		
													<div class="form-group form-inline col-md-4">
														<label for="exampleInputCode">所有代理人登录</label> 
														<select name="agentkind" id="agentkind" class="form-control">
															<option value="0">请选择</option>
															<option value="1">允许</option>
															<option value="2">不允许</option>
														</select>
													</div>			
													<div class="form-group form-inline col-md-4">
														<label for="exampleInputCode">所属机构层级</label> 
														<select name="agentkind" id="agentkind" class="form-control">
															<option value="0">本机构及子机构</option>
															<option value="1">本级机构</option>
															<option value="2">子级机构</option>
														</select>
													</div>			
													<div class="form-group form-inline col-md-4">
														<label for="exampleInputCode">代理人工号</label> 
														<input class="form-control m-left-2" id="name" type="text" name="name">
													</div>		
													<div class="form-group form-inline col-md-4">
														<label for="exampleInputCode">代理人姓名</label> 
														<input class="form-control m-left-2" id="name" type="text" name="name">
													</div>	
													<div class="form-group form-inline col-md-4">
														<label for="exampleInputCode">电话号码</label> 
														<input class="form-control m-left-2" id="name" type="text" name="name">
													</div>	
													<div class="form-group form-inline col-md-4">
														<label for="exampleInputCode">所属机构</label> 
														<input type="hidden" id="affiliationorg" name="affiliationorg" >
						 								<input type="text"	class="form-control  m-left-3" id="deptname"  placeholder="请选择" >
													</div>	
												</form>
											</div>
										</div>
										
										<div class="panel-footer">
											<button id="querybutton" type="button" name="querybutton"
												class="btn btn-primary">查询</button>
											<button id="querybutton" type="button" name="querybutton"
												class="btn btn-primary">导出数据</button>
											<button id="resetbutton" type="button" name="resetbutton"
												class="btn btn-primary">重置</button>
										</div>
										
									 <div class="row">
											<div class="col-md-12">
												<table id="table-javascript"></table>
											</div>
									  </div>
								</div>
								<div class="tab-pane" id="stationdata"></div>
								<div class="tab-pane" id="menudata">
									<div class="row">
										<div class="col-md-12">
											<div class="col-md-5">
												<div id="menuTree" class="ztree"></div>
											</div>
											<div class="col-md-7">
												<button class="btn btn-primary" id="save_menu_ids"  type="button" value="">保存</button>
											</div>
										</div>
									</div>
								</div>
								<div class="tab-pane" id="docdata">444.</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade" id="myModal_add" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog" style="width: 80%">
				<div class="modal-content" id="modal-content">
				</div>
			</div>
		</div>
		<div class="modal fade" id="myModal_eidt" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog" style="width: 80%">
				<div class="modal-content" id="modal-content">
				</div>
			</div>	
		</div>

		<div class="modal fade" id="myModal_show" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content" id="modal-content">
					<div class="modal-body">
						<table id="role-users-add">
						</table>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="button" id="save_users_roleid"
							class="btn btn-primary">保存</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
<div id="showdeptpic" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="gridSystemModalLabel">选择机构</h4>
      </div>
      <div class="modal-body">
        <div class="container-fluid">
          <div class="row">
			<ul id="treeDemoDept" class="ztree"></ul>
          </div>
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
