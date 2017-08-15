<!DOCTYPE html>
<html lang ="en" class="fuelux" >  
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>保险公司协议管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/system/flat-ui-icon.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<style type="text/css"> 
		.htmleaf-header{margin-bottom: 15px;font-family: "Segoe UI", "Lucida Grande", Helvetica, Arial, "Microsoft YaHei", FreeSans, Arimo, "Droid Sans", "wenquanyi micro hei", "Hiragino Sans GB", "Hiragino Sans GB W3", "FontAwesome", sans-serif;}
		.htmleaf-icon{color: #fff;}
		tr{height:40px;}
		td{vertical-align: middle;}
		.modal-body{overflow: auto; height: 380px;}
	</style>
<script type="text/javascript">
	requirejs([ "zzbconf/prvaccountmanager", "lib/tsearch" ]);
</script>
</head>
<body>
  <div class="row">
	<div class="col-md-3">
		<div class="panel panel-default m-bottom-2" >
			<div class="panel-heading" >机构列表</div>
			<div class="panel-body"  >
			  <div><input class="form-control ztree-search" id = "treesearch" data-bind="deptTree" name="treesearch" placeholder="输入机构名称关键字进行搜索" /></div>
			  <div><div class="ztree" id="deptTree" style="width:100%; height:500px; overflow-y:auto;overflow-x:auto;">正在加载机构数据......</div></div>
			</div>
		</div>
	</div>
	<div class="col-md-9">
		<div class="panel panel-default m-bottom-2" >
	    <div class="panel-heading" > 查询   </div>
	    <div class="panel-body" >
	    	<form class="form-inline" role="form" id="agentform">	
			<table  class="table table-bordered ">
			   <tr>
					<td  style="vertical-align: middle;">所属机构：</td>
					<td>
						<input  type="text" id="queryDeptId" hidden="true" >
						<input  type="text" id="queryDeptName" name="deptName" readonly="readonly">
					</td>
			  </tr>
			  <tr>		
			 	      <td  style="vertical-align: middle;">所属保险公司：</td>
					<td>
						<input  type="text" id="queryProviderid" hidden="true" >
						<input  type="text" id="managerProviderid" hidden="true" >
						<input  type="text" id="managerUsetype" hidden="true" >
						<input  type="text" id="managerDeptid" hidden="true" >
						<input  type="text" id="queryProvidername" name="providername" readonly="readonly">
					</td>
					<td style="vertical-align: middle;">类型：</td>
					<td>
						<select id="queryUsetype" name="usetype">
							<option value="0">请选择</option>
							<option value="1">精灵</option>
							<option value="2">EDI</option>
						</select>
					</td>
				</tr>
			</table>	
				<button id="querybutton" type="button" id="querybutton" class="btn btn-primary">查询</button>
				<button id="resetbutton" type="button" name="resetbutton" class="btn btn-primary">重置</button>				
				<button  type="button"  id="add_button" class="btn btn-primary">新增</button>				
		</form>
	  </div>
	 <div class="panel-footer"> 
	    	<table id="prvacc_data_list"></table>
	    	<button  type="button"  id="add_key_button" class="btn btn-primary">新增</button>&nbsp;&nbsp;&nbsp;&nbsp;<label id="showPrvName" style="font-size: 20"></label>	
	    	<table id="prvacc_key_list"></table>
	 </div>
	</div>
	</div>
	</div>
	<div class="modal fade" id="myModal_key_add" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog" style="width: 90%">
				<div class="modal-content" id="modal-content">
				</div>
			</div>	
		</div>
		
	<div id="showpic" class="modal fade" role="dialog"
		aria-labelledby="myModalLabel" data-backdrop="false"
		data-keyboard="false" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="gridSystemModalLabel">选择供应商</h4><br/>
					<div><input class="form-control ztree-search" id = "treeDemosearch" data-bind="treeDemo" name="treeDemosearch" placeholder="输入供应商名称进行搜索" /></div>
				</div>
				<div class="modal-body" style="overflow: auto; height: 400px;">
					<div class="container-fluid">
						<div class="row">
							<ul id="treeDemo" class="ztree"></ul>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
	
	<div id="manageredit" class="modal fade bs-example-modal-lg" role="dialog" aria-labelledby="myLargeModalLabel">
  	<div class="modal-dialog modal-lg">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">编辑</h4>
	      </div>
	      <div class="modal-body">
	        <div class="row">
					<div class="col-md-12">
						<div class="alert alert-danger" role="alert" id="showerrormessage" style="display: none;"></div>
							<form class="form-inline"  id="form_date" action="saveorupdate" method="post">
								<table class="table table-bordered " id="base_data">
								<tr>
									<td class="col-md-2 " align="right"
										style="vertical-align: middle;">归属保险公司：</td>
									<td class="col-md-2">
										<input class="form-control reset" type="hidden" id="providerid4edit" name="providerid" >
										<input class="form-control reset" type="text" id="prvName4edit" name="prvName" >
										<input class="form-control reset" type="hidden" id="id" name="id" >
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">版本：</td>
									<td class="col-md-2">
										<input class="form-control reset" type="text" id="version" name="version" >
									</td>
								</tr>
								<tr>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">归属机构：</td>
									<td class="col-md-2">
										<input class="form-control reset" type="hidden" name="deptid" id="deptid"  >
										<input class="form-control reset" type="text" disabled="disabled"  id="deptname"  ></td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">账号：</td>
									<td class="col-md-2">
										<input class="form-control reset" type="text" id="account" name="account" >
									</td>
								</tr>
								<tr>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">密码：</td>
									<td class="col-md-2">
										<input class="form-control reset" type="password" id="pwd" name="pwd" >
										</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">权限控制：</td>
									<td class="col-md-2">
										<input id="p1" type="radio" value="1" name="permission" checked="checked">作业
										<input id="p2" value="2" type="radio" name="permission">查询
									</td>
								</tr>
								<tr>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">登陆地址：</td>
									<td class="col-md-2">
										<input class="form-control reset"  type="text" name="loginurl"  id="loginurl">
									</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">保险公司内部机构：</td>
									<td class="col-md-2" >
										<input class="form-control reset"  type="text" id="org" name="org" >
									</td>
								</tr>
								<tr>
									<td class="col-md-2" align="right" style="vertical-align: middle;">类型：</td>
									<td class="col-md-2" >
										<select id="usetype" class="form-control"  name="usetype">
											<option value="0">请选择</option>
											<option value="1" selected="selected">精灵</option>
											<option value="2">EDI</option>
										</select>
									</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">备注：</td>
									<td class="col-md-10" colspan="5">
										<input class="form-control reset"   type="text" name="noti"  id="noti">
									</td>
								</tr>
							</table>
							</form>
					</div>
				</div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	        <button type="button" class="btn btn-primary" id="savebutton" name="savebutton">保存</button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	
	<div id="keyedit" class="modal fade " role="dialog" aria-labelledby="myLargeModalLabel">
  	<div class="modal-dialog ">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">编辑</h4>
	      </div>
	      <div class="modal-body">
	        <div class="row">
					<div class="col-md-12">
						<div class="alert alert-danger" role="alert" id="showerrormessage" style="display: none;"></div>
							<form class="form-inline"  id="key_form_data"  method="post">
								<table class="table table-bordered "  id="prvacc_key_list">
								<tr>
									<td class="col-md-2 " align="right"
										style="vertical-align: middle;">参数编码：</td>
									<td class="col-md-4">
										<input class="form-control" type="text" id="paramname" name=paramname >
										<input class="form-control" type="hidden" id="keyid" name="id" >
										<input class="form-control" type="hidden" id="managerid" name="managerid" >
										<input class="form-control" type="hidden" id="manageridmain" name="manageridmain" >
									</td>
								</tr>
								<tr>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">参数值：</td>
									<td class="col-md-4">
										<input class="form-control" type="text" id="paramvalue" name="paramvalue">
									</td>
								</tr>
								<tr>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">备注：</td>
									<td class="col-md-4">
										<input class="form-control" type="text" name="noti"   id="keyNoti" ></td>
								</tr>
							</table>
							</form>
					</div>
				</div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	        <button type="button" class="btn btn-primary" id="save_key_button" name="savebutton">保存</button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
</body>
</html>
