<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加用户</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "system/user"]);
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">用户基本信息</div>
		  <div class="panel-body">
				<form  id="usersaveform" action="saveuser" method="post">
				<div class="alert alert-danger" role="alert" id="showerrormessage" style="display: none;"></div>
				<div class="row">
					<div class="form-group form-inline col-md-4">
						<input id="id" name="id" type="hidden" value="${user.id!''}"/>
						<label for="exampleInputCode">用户编码</label> 
							<input type="text" class="form-control m-left-5" id="usercode" name="usercode" value="${user.usercode!''}" onchange="checkusercode()" placeholder="" class="required">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputName">用户姓名</label> 
						<input type="text" 
							class="form-control m-left-5" id="username" name="name" value="${user.name!''}" placeholder="" class="required">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="deptname">所属机构</label> 
						<input class="form-control " type="hidden" id="deptid" name="userorganization" value="${user.userorganization!'' }"> 
						<input class="form-control m-left-5"  type="text" id="deptname" value="${user.comname!''}" class="required" name="deptname">
					</div>				
				</div>
				<div class="row">
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputName">用户密码</label>
						<input id="oldpwd" type="hidden" value="${user.password!''}"/>
						<input type="password" 
							class="form-control m-left-5" id="passwordtemp" value="${user.password!''}" placeholder="" class="required">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputName">确认密码</label> 
						<input type="password" 
							class="form-control m-left-5" id="password" name="password" value="${user.password!''}" placeholder="" class="required">
					</div>	
					
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputOrgName">openid</label>
						<label class=" m-left-5">
							<input class="form-control m-left-5"  type="text" name="openid" value="${user.openid}">
						</label>
					</div>
				</div>
				<div class="row">
					<div class="form-group form-inline col-md-4">
						<label for="phone">移动电话</label> <input type="text"
							class="form-control m-left-5" id="phone" name="phone" value="${user.phone!''}" placeholder="" class="required phone">
					</div>	
					<div class="form-group form-inline col-md-4">
						<label for="email">邮箱地址</label> <input type="text"
							class="form-control m-left-5" id="email" name="email" value="${user.email!''}" placeholder="" class="required email">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="address">联系地址</label> <input type="text"
							class="form-control m-left-5" id="address" name="address" value="${user.address!''}" placeholder="">
					</div>
				</div>
				<div class="row">
					<div class="form-group form-inline col-md-4">
						<label for="status">用户状态</label>
						<label class="radio-inline m-left-5">
 						  <input type="radio" name="status" id="status1" value="1" <#if "${user.status!''}" == "1" || "${user.status!''}" == "">checked="checked"</#if>/> 启用 
						</label>
						<label class="radio-inline">
 						  <input type="radio" name="status" id="status2" value="0" <#if "${user.status!''}" == "0">checked="checked"</#if>/> 停用
						</label>
					</div>
					<div id="maturitydiv" class="form-group form-inline col-md-8">
						<label for="address">到期日期</label> 
						<input class="form-control m-left-5 date form_datetime" type="hidden"
													id="nowtime" name="nowtime" value="${newDate}">
						<input class="form-control m-left-5 date form_datetime" type="text"
													id="maturitydata" name="maturitydata" value="${user.maturitydata!''}"> 
					</div>
				</div>
				<label id="pwdmsg" style="margin-left:5%;">
					(<font color='#666666'><i>说明：密码格式必须是6-16位包含数字、大写字母、小写字母、特殊符号四种任意三种组合</i></font>)
				</label>
				<br/><br/>
				<label for="address">选择角色</label>
				<input type="hidden" id="operator" value="${usercode}"/>
				<div class="row">					
					<div class="col-xs-5">
						<select name="from" class="js-multiselect form-control" data-right-selected="#right_Selected_1" size="8" multiple="multiple">
							<#list roleList as role>									
						  		<option  value="${role.id }" rolename="${role.rolename!"" }"> ${role.rolename!"" }</option>					  																		  		
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
						<select name="to" id="js_multiselect_to_1" class="form-control" size="8" multiple="multiple">
						<#list roleList as role>
						<#list addList as selectedrole>		
							<#if role.id==selectedrole.id>
								<option value="${role.id }" rolename="${role.rolename!"" }"> ${role.rolename!"" }</option>					  				
							</#if>				  		
						  </#list>
						  </#list>
						</select>
					</div>
				</div>
				
				<div class="row">
					<div class="form-group form-inline col-md-4">
						<button id="addbutton" type="button" name="addbutton" class="btn btn-primary">保存</button>
						<button  type="button"  id="go_back" class="btn btn-primary">返回</button>
					</div>
				</div>
				</form>
			</div>
		</div>
	</div>
	<div class="modal fade" id="myModal_dept" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content">
				<div class="modal-body"  style="overflow: auto; height: 400px;">
					<div id="dept_tree" class="ztree"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
