<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代理人编辑</title> 
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/agentedit"]);
</script>
<body> 
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">编辑</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-md-12">
						<form class="form-inline" role="form" id="agent_form">
						<input class="form-control" type="hidden" id="agentid" name="agentid" value="${agent.id!'' }">
						<div class="alert alert-danger" role="alert" id="showerrormessage" style="display: none;"></div>
							<table class="table table-bordered " id="base_data">
								<tr>
									<td class="col-md-2 " align="right"
										style="vertical-align: middle;">姓名：</td>
									<td class="col-md-2">
										<input class="form-control" type="text" id="name" name="name" value="${agent.name!''}">
										<input class="form-control" type="hidden" id="id" name="id" value="${agent.id!''}">
									</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">证件类别：</td>
									<td class="col-md-2">
										<select class="form-control" name="idnotype" id="idnotype">
										<#list certKinds as ck>
											<option value="${ck.codevalue }" <#if agent.idnotype==ck.codevalue> selected = "selected"</#if>>${ck.codename }</option>
										</#list>
										</select>
									</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">证件号码：</td>
									<td class="col-md-2"><input class="form-control"
										type="text" name="idno" id="idno"  value="${agent.idno!''}"   onblur="if(!/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/.test(this.value)){alert('请输入有效证件号码！');}"></td>
								</tr>
								<tr>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">所属机构：</td>
									<td class="col-md-2">
									<#if agent.deptid!''>
										<input class="form-control" type="hidden" id="deptid1" name="deptid" value="${agent.deptid!'' }">
										<input class="form-control"  type="text" id="deptname1" value="${agent.comname!'' }">
									<#else>
										<input class="form-control" type="hidden" id="deptid" name="deptid" value="${agent.deptid!'' }">
										<input class="form-control" type="text" id="deptname" value="${agent.comname!'' }">
									</#if>
									
										</td>
									<td class="col-md-1" align="right"
										style="vertical-align: middle;">所属渠道：</td>
									<td class="col-md-2">
									<input class="form-control" type="hidden" id="channelid" name="channelid"
										value="${agent.channelid!'' }">
										<input
										class="form-control" type="text" id="channelname"
										value="${agent.channelname!'' }" readonly>
										</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">VIP等级：</td>
									<td class="col-md-2">
									<select name="agentlevel" class="form-control">
									  <#list agentlevelvalue as alv>
											<option value="${alv.codevalue }" <#if agent.agentlevel==alv.codevalue>selected="selected"</#if>>${alv.codename }</option>
									  </#list>
									</select></td>
								</tr>
								<tr>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">手机号码：</td> 
									<td class="col-md-2"><input class="form-control"
										type="text" name="phone" value="${agent.phone!'' }"   id="phoneverify1">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">所属团队：</td> 
									<td class="col-md-2" align="center">
										<input class="form-control" type="text" id="team" value="${agent.teamname!'' }" readonly>
									</td>
									<td class="col-md-1" align="right"
										style="vertical-align: middle;">用户类型：</td>
									<td class="col-md-3">
									<select id="agentkind" name="agentkind" class="form-control">
											<option value="">请选择</option> 
											<option value="1" <#if agent.agentkind==1>selected="selected"</#if>>试用</option>
											<option value="2" <#if agent.agentkind==2>selected="selected"</#if>>正式</option>
											<option value="3" <#if agent.agentkind==3>selected="selected"</#if>>渠道</option>
									</select>
									
									</td>
								</tr>
								<tr>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">电子邮箱：</td>
									<td class="col-md-2"><input class="form-control"
										type="text" name="email" id="email"  value="${agent.email!'' }">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">常用网点设置：</td>
									<td class="col-md-2">
										<input id="commonusebranchname" class="form-control" style="width:100%;" type="text" value="${commonUseBranchNames }" onclick="showMenu();" /> 
										<input class="form-control" type="hidden" id="commonusebranchid" value="${agent.commonusebranch}" name="commonusebranch">
									</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">权限包：</td>
									<td class="col-md-2">
										<select id="setid" name="setid"
											class="form-control" >
											<option value="">请选择</option> 
											<#list setlist as ag>
												<option value="${ag.id }" <#if agent.setid==ag.id> selected="selected" </#if> >${ag.setname }</option> 
											</#list>
										</select></td>
								</tr>
								<tr>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">账户类型：</td>
									<td class="col-md-2"><select name="istest" id="istest"
										class="form-control">
											<option value="1" <#if agent.istest==1>selected="selected" </#if>>测试账户</option>
											<option value="2" <#if agent.istest==2>selected="selected" </#if>>普通账户</option>
											<option value="3" <#if agent.istest==3>selected="selected" </#if>>虚拟账户</option>
									</select></td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">代理人编码：</td>
									<td class="col-md-2"><input class="form-control"
										type="text" readonly="readonly" name="agentcode" id="agentcode"value="${agent.agentcode!'' }">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">认证状态：</td>
									<td class="col-md-2">
										<select class="form-control" id="approvesstate" name="approvesstate">
											<#list approve as data>
											<option value="${data.codevalue }" <#if agent.approvesstate==data.codevalue>selected="selected"</#if>>${data.codename }</option>
											</#list>
										</select>
									</td>
								</tr>
								<tr>
									<td class="col-md-2" align="right" style="vertical-align: middle;">代理人状态：</td>
									<td class=col-md-2>
										<select name="agentstatus" class="form-control">
										<#if agent.agentstatus==1>
											<option value="1" selected="selected">启用</option>
											<option value="2">停用</option>
										<#elseif agent.agentstatus==2>
											<option value="1">启用</option>
											<option value="2" selected="selected">停用</option>
										<#else>
											<option value="1">启用</option>
											<option value="2">停用</option>
										</#if>	
										</select>
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">续保提醒时间：</td>
									<td class=col-md-2>
										<input class="form-control" style="width:50%" type="text" name="renewaltime" value="${agent.renewaltime}">个月
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">非wifi是否压缩：</td>
									<td class=col-md-2>
										<select name="compression" class="form-control">
											<#if agent.compression==1>
											<option value="1" selected="selected">是</option>
											<option value="2">否</option>
										<#elseif agent.compression==2>
											<option value="1">是</option>
											<option value="2" selected="selected">否</option>
										<#else>
											<option value="1">是</option>
											<option value="2">否</option>
										</#if>	
										</select>
									</td>
								</tr>
								<tr >
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">openID：</td>
									<td class="col-md-2"><input class="form-control"
										type="text" name="openid" id="openid"  value="${agent.openid!'' }" readonly>
									</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">推荐人工号：</td>
									<td class="col-md-2"><input class="form-control"
										type="text" name="referrer" id="referrer"  value="${agent.referrer!'' }" readonly>
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">注册时间：</td>
									<td class="col-md-2">
										<input class="form-control" type="text" id="createTime" 
										value="<#if agent.registertime??>${agent.registertime?string('yyyy-MM-dd HH:mm:ss')}</#if>" readonly>	
									</td>
								</tr>
								<tr>
									<td class="col-md-2" align="right" style="vertical-align:middle">
									首登时间：
									</td>
									<td class="col-md-2">
										<input class="form-control" type="text" id="firstLogin"
										value="<#if agent.firstlogintime??>${agent.firstlogintime?string("yyyy-MM-dd HH:mm:ss")}</#if>" readonly>
									</td>
									<td class="col-md-2" align="right" style="vertical-align:middle">
									首单时间：
									</td>
									<td class="col-md-2">
										<input class="form-control" type="text" id="firstCheck"
										value="<#if agent.firstOderSuccesstime??>${agent.firstOderSuccesstime?string("yyyy-MM-dd HH:mm:ss")}</#if>" readonly>
									</td>
									<td class="col-md-2" align="right" style="vertical-align:middle">
									最后登录时间：
									</td>
									<td class="col-md-2">
										<input class="form-control" type="text" id="lastLogin"
										value="<#if agent.lsatlogintime??>${agent.lsatlogintime?string("yyyy-MM-dd HH:mm:ss")}</#if>" readonly>									</td>
								</tr>
								<#if agent.istest==3>
									<tr id='new_tr'> 
									<td class='col-md-1' align='right' style='vertical-align: middle;'>关联正式账户：</td>
									<td><input class='form-control' type='text' name='jobnum4virtual' value="${agent.jobnum4virtual }"></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
								</#if>
							</table>
							<button type="button" id="save_agent" class="btn btn-primary">代理人基础信息保存</button>
						</form>			
					</div>
				</div>
			</div>
			<div class="panel-footer">
				<button  type="button"  id="go_back" class="btn btn-primary">返回</button>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<ul id="mytab" class="nav nav-tabs">
					<li class="active"><a href="#permission_tab" data-toggle="tab">指定权限</a></li>
					<li><a href="#provider_tab" id="init_provider_tree" data-toggle="tab">指定协议</a></li>
				</ul>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="tab-content">
					<div class="tab-pane active" id="permission_tab">
						<table id="permisssion_list"></table>
					</div>
					<div class="tab-pane" id="provider_tab">
						<div class="ztree" id="provider_tree"></div>
                        <table id="table-agreement"></table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 常用网点设置 机构树弹层 -->
	<div class="modal fade" id="myModal_commonusebranch" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content">
				<div class="modal-body" style="overflow: auto; height: 400px;">
					<div id="commonusebranchname_tree" class="ztree"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 机构树弹层 -->
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
	<!-- 渠道弹层 -->
	<div class="modal fade" id="myModal_channel" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content">
				<div class="modal-body"  style="overflow: auto; height: 400px;">
					<div id="channel_tree" class="ztree"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 权限分配 配置 -->
	<div class="modal fade" id="agent_permission_modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content" id="modal-content">
			</div>
		</div>
	</div>
</body>
</html>