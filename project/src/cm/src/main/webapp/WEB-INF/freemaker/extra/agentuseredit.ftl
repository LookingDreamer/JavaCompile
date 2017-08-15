<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代理人编辑</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "extra/agentuseredit" ]);
</script>
    <style type="text/css">
        .showright{
            position:relative;
        }
        .channelimg{
            position:absolute;
            top:27px;
            right:30px;
        }
    </style>
<body> 
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5" style="display: none;">详细</div>
			<div class="panel-body" style="display: none;">
				<div class="row">
					<div class="col-md-12">
						<form class="form-inline" role="form" id="agent_form">
						<input class="form-control" type="hidden" id="agentid" name="agentid" value="${agent.id!'' }">
						<div class="alert alert-danger" role="alert" id="showerrormessage" style="display: none;"></div>
                            <table class="table table-bordered " id="base_data">
                                <tr>
                                    <td class="col-md-2 " align="right"
                                        style="vertical-align: middle;">昵称：</td>
                                    <td class="col-md-2">
                                        <input class="form-control" type="hidden" id="name" name="name" value="${agent.name!''}">
                                        <input class="form-control" type="hidden" id="id" name="id" value="${agent.id!''}">
                                        <input class="form-control" type="hidden" id="nickname" name="nickname" value="${agent.nickname!''}">${agent.nickname!''}
                                    </td>
                                    <td class="col-md-2" align="right"
                                        style="vertical-align: middle;">绑定手机号</td>
                                    <td class="col-md-2">
										<input class="form-control" type="hidden" name="phone" value="${agent.phone!'' }"   id="phone">${agent.phone!'' }
                                    </td>
                                    <td class="col-md-2" style="border: hidden;border-left: none"></td><td class="col-md-2" style="border: hidden"></td>
								</tr>
								<tr>
                                    <td class="col-md-2 " align="right" style="vertical-align: middle;">微信头像：</td>
                                    <td class="col-md-2" colspan="3">
										<#if agent?? && agent.weixinheadphotourl?? && agent.weixinheadphotourl!=""?? && agent.weixinheadphotourl!="null">
										<img src="${agent.weixinheadphotourl!''}" style="width: 300px;height: 200;"/>
										<#else>暂无微信头像
										</#if>
									</td>
								</tr>
							</table>
							<table class="table table-bordered " style="display: none" id="base_data">
								<tr>
									<#--<td class="col-md-2 " align="right"
										style="vertical-align: middle;">姓名：</td>
									<td class="col-md-2">
										<input class="form-control" type="text" id="name" name="name" value="${agent.name!''}">
										<input class="form-control" type="hidden" id="id" name="id" value="${agent.id!''}">
									</td>-->
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
										<input class="form-control" disabled="disabled" type="text" id="deptname1" value="${agent.comname!'' }">
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
										value="${agent.channelname!'' }">
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
								<tr><#--
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">手机号码1</td>
									<td class="col-md-2"><input class="form-control"
										type="text" name="mobile" value="${agent.mobile!'' }"   id="phoneverify1">
									</td>-->
									<td class="col-md-2" align="center">
									<#if agent.mobilephone1use==1>
										<input checked="checked" type="radio" name="mobilephone1use"
										value="1">接收短信
									<#else>
										<input
										 type="radio" name="mobilephone1use"
										value="1">接收短信
									</#if>
									</td>
									<td class="col-md-2" align="center">
									<#if agent.mobilephone1use==2>
										<input checked="checked"	 type="radio"  name="mobilephone1use"
										value="2">业务联系
									<#else>
										<input  type="radio"  name="mobilephone1use"
										value="2">业务联系
									</#if>
									</td>
									<td class="col-md-1" align="right"
										style="vertical-align: middle;">用户类型：</td>
									<td class="col-md-3">
									<select id="agentkind" name="agentkind" class="form-control">
											<option value="1" <#if agent.agentkind==1>selected="selected"</#if>>试用</option>
											<option value="2" <#if agent.agentkind==2>selected="selected"</#if>>正式</option>
											<option value="3" <#if agent.agentkind==3>selected="selected"</#if>>渠道</option>
									</select>
									
									</td>
								</tr>
								<tr>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">手机号码2</td>
									<td class="col-md-2"><input class="form-control"
										type="text" name="mobile2" value="${agent.mobile2!'' }" id="phoneverify2"  >
									</td>
<!-- 									onblur="if(/^(13|14|15|17|18)\d{9}$/.test(this.value)){alert('手机号正确');} else {alert('请输入有效11位手机号！');}" -->
									<td class="col-md-2" align="center">
									<#if agent.mobilephone2use==1>
										<input
										 checked="checked" type="radio" name="mobilephone2use"
										value="1">接收短信
									<#else>
										<input
										 type="radio" name="mobilephone2use"
										value="1">接收短信
									</#if>
									</td>
									<td class="col-md-2" align="center">
									<#if agent.mobilephone2use==2>
										<input checked="checked"	type="radio"  name="mobilephone2use"
										value="2">业务联系
									<#else>
										<input type="radio"  name="mobilephone2use"
										value="2">业务联系
									</#if>
									</td>
									<td class="col-md-2" align="right"
										style="vertical-align: middle;">功能包：</td>
									<td class="col-md-2">
										<select id="setid" name="setid"
											class="form-control">
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
										type="text" readonly="readonly" name="agentcode" id="agentcode" value="${agent.agentcode!'' }">
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
									<td class="col-md-2" align="right" style="vertical-align: middle;">代理人状态:</td>
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
									<td class="col-md-2" align="right" style="vertical-align: middle;">续保提醒时间:</td>
									<td class=col-md-2>
										<input class="form-control" style="width:50%" type="text" name="renewaltime" value="${agent.renewaltime}">个月
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">非wifi是否压缩:</td>
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
										style="vertical-align: middle;">电子邮箱：</td>
									<td class="col-md-2"><input class="form-control"
										type="text" name="email" id="email"  value="${agent.email!'' }">
									</td>
									<td class="col-md-2" align="right" style="vertical-align: middle;">常用网点设置：</td>
									<td class="col-md-2">
										<input id="commonusebranchname" class="form-control" style="width:100%;" type="text" value="${commonUseBranchNames }" onclick="showMenu();" /> 
										<input class="form-control" type="hidden" id="commonusebranchid" value="${commonusebranchIds}" name="commonusebranch">
									</td>
                                    <td class="col-md-2" align="right"
                                        style="vertical-align: middle;">手机号码1</td>
                                    <td class="col-md-2">
                                        <input class="form-control" type="hidden" name="mobile" value="${agent.mobile!'' }"   id="phoneverify1">${agent.mobile!'' }
                                    </td>
                                    <td class="col-md-2" style="border: hidden;border-left: none"></td><td class="col-md-2" style="border: hidden"></td>
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
							<button type="button" id="save_agent" class="btn btn-primary" style="display: none">代理人基础信息保存</button>
						</form>			
					</div>
				</div>
			</div>
			<div class="panel-footer">
				<button  type="button"  id="save_agent_permisssion_provider" class="btn btn-primary" style="display: none">权限信息保存</button>
				<button  type="button"  id="go_back" class="btn btn-primary">返回</button>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<ul id="mytab" class="nav nav-tabs">
					<li><a href="#provider_tab" id="init_provider_tree" data-toggle="tab" style="display: none">指定供应商</a></li>
					<li id="userbaseli" class="active"><a href="#userbase_tab" data-toggle="tab" id="userbase_tab_id">用户信息</a></li>
                    <li><a href="#realNameTheAuthentication_tab" data-toggle="tab">实名认证信息</a></li>
                    <li><a href="#myRecommendPersons_tab" data-toggle="tab" id="myRecommendPersons_tab_id">推荐关系信息</a></li>
					<li id="myOrderli"><a href="#myOrder_tab" data-toggle="tab" id="myOrder_tab_id">订单查询</a></li>
					<li style="display: none"><a href="#" id="agentbankcard_tab_id" data-toggle="tab">历史报价</a></li>
				</ul>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="tab-content">
					<div class="tab-pane" id="provider_tab">
						<div class="ztree" id="provider_tree"></div>
					</div>

					<#-- 用户基本信息显示 -->
                    <div class="tab-pane" id="userbase_tab">
                    <div class="panel-footer">用户基本信息</div>
						<table class="table table-bordered">
							<tr>
								
								<td class="col-md-2"  rowspan="4" align="center">
										<#if agent?? && agent.weixinheadphotourl?? && agent.weixinheadphotourl!=""?? && agent.weixinheadphotourl!="null">
										<img src="${agent.weixinheadphotourl!''}" style="width: 120px;height: 80;"/>
										<#else>暂无微信头像
										</#if>
								</td>
								
								
								
                                <td class="col-md-2" align="center"
                                        style="vertical-align: middle;"> <label>微信昵称</label> </td>
                                
                                <td align="center"
                                        style="vertical-align: middle;">
                                	${agent.nickname!''}
                                </td>
								<td class="col-md-2" align="center"
                                        style="vertical-align: middle;">
									<label>微信openid</label>
								</td>
								<td align="center"
                                        style="vertical-align: middle;">${agent.openid!'' }</td>
							</tr>
                            <tr>
                            
                                <td class="col-md-2" align="center"
                                        style="vertical-align: middle;"> <label>mini用户工号</label> </td>
                                <td align="center"
                                        style="vertical-align: middle;">${agent.tempjobnum!''}
								
							
								</td>
                                
                                <td class="col-md-2" align="center"
                                        style="vertical-align: middle;"><label>绑定手机号</label></td>
                                <td align="center"
                                        style="vertical-align: middle;"> ${agent.phone!'' }</td>
                            </tr>
                            <tr>
                                <td class="col-md-2" align="center"
                                        style="vertical-align: middle;"> <label>性别</label> </td>
                                 <td align="center"
                                        style="vertical-align: middle;"><#if agent.sex==0>男
								<#elseif agent.sex==1>女
								<#else>
								</#if>
								</td>
								<td class="col-md-2" align="center"
                                        style="vertical-align: middle;"> <label>来源渠道</label> </td>
                                 <td align="center"
                                <td>${agent.pushChannel!'' } </td>
                                
                            </tr>
                            <tr>
                                <td class="col-md-2" align="center"
                                        style="vertical-align: middle;"><label>来源途经</label></td>
                                <td>${agent.pushWay!'' }</td>
                                <td></td>
                                <td></td>
                            </tr>
							<#--<tr>
								<td> <label>手机号1:</label> </td>
								<td>${agent.mobile!'' } </td>
								<td> <label>email:</label> </td>
								<td>${agent.email!'' } </td>
							</tr>
							<tr>
								<td><label>证件类别:</label></td>
								<td><#list certKinds as ck>
										<#if agent.idnotype==ck.codevalue>${ck.codename }</#if>
									</#list>
								</td>
								<td><label>证件号码:</label></td>
								<td>${agent.idno!''}</td>
							</tr>
							<tr>
								<td><label>所属机构:</label></td>
								<td>${agent.comname!'' } </td>
								<td><label>所属渠道:</label></td>
								<td>${agent.channelname!'' }</td>
							</tr>
							<tr>
								<td><label>VIP等级:</label></td>
								<td><#list agentlevelvalue as alv>
                                    	<#if agent.agentlevel==alv.codevalue>${alv.codename }</#if>
									</#list>
								</td>
								<td><label>用户类型:</label></td>
								<td><#if agent.agentkind==1>试用</#if>
                                    <#if agent.agentkind==2>正式</#if>
                                    <#if agent.agentkind==3>渠道</#if>
                                </td>
							</tr>
							<tr>
								<td><label>代理人状态:</label></td>
								<td><#if agent.agentstatus==1>启用
									<#elseif agent.agentstatus==2>停用
									<#else>未设置
									</#if>
								</td>
								<td><label>手机号码2:</label></td>
								<td>${agent.mobile2!'' }
                                </td>
							</tr>-->
						</table>
						
						<div class="panel-footer">收货地址</div>
						
							
							<div class="tab-pane" id="receiveAddress_tab">
                        		<div id="receiveAddress_list"></div>
                    		</div>
                   			
						
						
						<div class="panel-footer">银行卡信息</div>
							<div class="tab-pane" id="agentbankcard_tab">
                        		<div id="agentbankcard_list"></div>
                   			 </div>
                    </div>

                    <#-- 历史报价 -->
					<div class="tab-pane" id="historicalQuote_tab">
                        <div id="receiveAddress_list"></div>
                    </div>

					<#-- 订单信息 -->
                    <div class="tab-pane" id="myOrder_tab">
						<input type ="text" id="myOrderActive" hidden="true" value="${myOrderActive}" />
                        <div id="myOrder_list">
						  <#include "./extra/insbMiniOrderManagement.ftl">
                        </div>
                    </div>

                    <#-- 实名认证信息 -->
                    <div class="tab-pane" id="realNameTheAuthentication_tab">
						<#if certificationVo?? && certificationVo.pic?? && certificationVo.pic['agentidcardpositive']?? && certificationVo.pic['agentidcardpositive'] != "">
							<div><strong>审核详情</strong></div>
							<div style="margin-bottom:10px;">
							${agent.approvesstatestr!'' } <br/>
							
							</div>
							<#if agent.approvesstate !=4>
							${agent.noti!'' } <br/>
                        	<form class="form-inline" role="form" id="auditform">
								<table class="table table-bordered">
									<tr>
										<td><label>姓名:</label></td>
										<td>${agent.name!''}</td>
									</tr>
									<tr>
										<td><label>身份证号:</label></td>
										<td>${agent.idno!''}</td>
									</tr>
									<tr>
										<td> <label>身份证正面:</label> </td>
										<td><img src="${certificationVo.pic['agentidcardpositive']}" style="width: 400px;height: 300;"/> </td>
									</tr>
								</table>
								<#if agent.approvesstate==2>
									<div style="margin-bottom:10px;"><button type="button"  id="passAuditId" class="btn btn-primary">审核通过</button></div>
                                    <div style="margin-bottom:10px;">
                                        <button type="button"  id="backAuditId" class="btn btn-primary">审核退回</button>
                                        <select id="noti" name="noti">
                                            <option value="身份证信息不清晰">身份证信息不清晰</option>
                                            <option value="用户姓名与身份证姓名不符">用户姓名与身份证姓名不符</option>
                                        </select>
                                    </div>
								</#if>
							</form>
							
							<#else>
								${agent.noti!'' } <br/>
							</#if>
						<#else>
                            <div>
								<span style="margin-bottom:10px;"><button type="button"  id="passAuditId" class="btn btn-primary" style="display: none;">审核通过</button></span>
                                <span style="margin-bottom:10px;"><button type="button"  id="backAuditId" class="btn btn-primary" style="display: none;">审核退回</button></span>
							</div>
							<div>未提交认证</div>
						</#if>
                    </div>

					<#-- 推荐关系信息 -->
                    <div class="tab-pane" id="myRecommendPersons_tab">
                        <table class="table table-bordered">
                            <tr>
                                <td colspan="8"> <label><b>我的推荐人信息：</b></label> </td>
                            </tr>
							<#if myReferrer?? && myReferrer.id?? && myReferrer.id!="">
                                <tr>
                                    <td> <label>昵称:</label> </td>
                                    <td> ${myReferrer.nickname } </td>
                                    <td> <label>姓名:</label> </td>
                                    <td> ${myReferrer.name } </td>
                                    <td> <label>性别:</label> </td>
                                    <td> <#if myReferrer.sex==0>男
									<#elseif myReferrer.sex==1>女
									<#else>
									</#if> </td>
                                    <td><label>手机号:</label></td>
                                    <td> ${myReferrer.phone }</td>
                                </tr>
							<#else>
								<tr>
									<td colspan="8"> <label>无</label> </td>
								</tr>
							</#if>
							</table>
                        <#--<div id="myRecommendPersons_list"></div> 不用再取数据，已返回数据直接显示,以后如果人数据 太多可以改为点击再去查询-->
						<table class="table table-bordered">
							<tr>
								<td colspan="8"> <label><b>我推荐的用户信息：</b></label> </td>
							</tr>
						<#if myRecommendPersons?? && myRecommendPersons?size gt 0 >
							<#list myRecommendPersons as myRecommendPerson>
								<tr>
									<td> <label>昵称:</label> </td>
									<td> ${myRecommendPerson.nickname } </td>
									<td> <label>姓名:</label> </td>
									<td> ${myRecommendPerson.name } </td>
									<td> <label>性别:</label> </td>
									<td> <#if myRecommendPerson.sex==0>男
									<#elseif myRecommendPerson.sex==1>女
									<#else>
									</#if> </td>
									<td><label>手机号:</label></td>
									<td> ${myRecommendPerson.phone }</td>
								</tr>
							</#list>
						<#else>
							<tr>
								<td colspan="8"> <label>无</label> </td>
							</tr>
						</#if>
						</table>
                    </div>

				</div>
			</div>
		</div>
		<!-- <div id="menuContent" style="display: none; position: absolute;">
			<ul   id="commonusebranchname_tree-old" class="ztree "
				style="margin-top: 0;background-color: gray;">
			</ul>				
		</div> -->
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