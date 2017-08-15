<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>认证任务</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}">
</script>
</head>
<body>
<script type="text/javascript">
	requirejs([ "cm/certificationtask/certificationtask", "lib/tsearch"]);
</script>
<form id="fm" action="downloadpic" method="post">
<input type="hidden" id="pic1" name="pic1" value="${certificationVo.pic.agentidcardpositive}"/>
<input type="hidden" id="pic2" name="pic2" value="${certificationVo.pic.agentidcardopposite}"/>
<input type="hidden" id="pic3" name="pic3" value="${certificationVo.pic.agentbankcard}"/>
<input type="hidden" id="pic4" name="pic4" value="${certificationVo.pic.agentqualification}"/>
<input type="hidden" id="pic5" name="pic5" value="${certificationVo.pic.qualificationoppositive}"/>
<input type="hidden" id="picName1" name="picName1" value="身份证正面照"/>
<input type="hidden" id="picName2" name="picName2" value="身份证反面照"/>
<input type="hidden" id="picName3" name="picName3" value="银行卡正面照"/>
<input type="hidden" id="picName4" name="picName4" value="资格证照片页"/>
<input type="hidden" id="picName5" name="picName5" value="资格证信息页"/>
<input type="hidden" id="tempjobnum" value="${certificationVo.agentnum}"/>
<input type="hidden" id="agentname" name="agentname" value="${certificationVo.name} ">
</form>
<div class="container-fluid">
<div class="panel panel-default m-bottom-5">
	<div class="panel-heading padding-5-5">
		<div class="row" id="main">
			<div class="col-md-12">
				<h4>任务处理</h4>
			</div>
		</div>
	</div>
	<div class="panel-body">
	<div class="row"> 
		<div class="col-md-9" style="border: 1px solid #ccc;">
			<table class="table table-bordered">
				<thead> 
					<tr>
						<th bgcolor="#0066FF">
							<span id="picName" style="color: #FFF">身份证正面照</span>
							<button style="margin-left:20px;" type="button" class="btn btn-default btn-sm" id="lastPic" onclick="lastPic();" >上一张</button> 
							<button type="button" class="btn btn-default btn-sm" id="nextPic" onclick="nextPic();">下一张</button>
							<button type="button" class="btn btn-default btn-sm" id="turnLeft" onclick="turnLeft();">左旋转</button>
							<button type="button" class="btn btn-default btn-sm" id="turnRight" onclick="turnRight();">右旋转</button>
							<button type="button" class="btn btn-default btn-sm" id="download" onclick="download();">下载全部</button>
						</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							<div id="picDiv" style="width:600px;height:600px;overflow:hidden;">
								<img id="cetificationPic" style="width:100%;height:100%;" src="${certificationVo.pic.agentidcardpositive}"/>
					
									</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="col-md-3">
			<table class="table table-bordered" id="agentmessage">
				<thead>
					<tr>
						<th bgcolor="#0066FF" style="color: #FFF">认证信息填写</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							<font color="red"></font>主营业务 <br>
							<select id="mainbiz">
								<#list busTypeList as bustype>
									<option value="${bustype.codevalue}" <#if certificationVo.mainbiz == bustype.codevalue>selected="selected"</#if>>${bustype.codename}</option>
								</#list>
							</select>
						</td>
					</tr>
					<tr>
						<td>
							<font color="red"></font>所在地区<br>
							<select id="province" onchange="changeprv(${certificationVo.city})"></select>
							<select id="city" name="city"></select>
							<input type="hidden" id="provinceVal" value="${certificationVo.province}">
						</td>
					</tr>
					<tr>
						<td>
							<font color="red"></font>所属机构<br>
							<input type="hidden" id="deptid" value="${certificationVo.deptid!'' }"> 
							<input type="text" id="deptname" value="${certificationVo.deptname!'' }">
						</td>
					</tr>
					<tr>
						<td>
							所属渠道<br>
							<input type="hidden" id="channelid" value="${certificationVo.channelid!'' }">
							<input type="text" id="channelname" value="${certificationVo.channelname!'' }">
						</td>
					</tr>
					<tr>
						<td>
							<font color="red"></font>真实姓名<br>
							<input type="text" id="name" value="${certificationVo.name}">
						</td>
					</tr>
					<tr>
						<td>
							<font color="red"></font>手机号码<br>
							<input type="text" id="mobile" value="${certificationVo.mobile}">
						</td>
					</tr>
					<tr>
						<td>
							<font color="red"></font>身份证号<br>
							<input type="text" id="idcard" value="${certificationVo.idcard}">
						</td>
					</tr>
					<tr>
						<td>
							<font color="red"></font>开户行<br>
							<select id="belongs2bank">
                                <option value="0" >请选择</option>
								<#list bankCardList as bankCard>
									<#--<#if "${bankCard.codevalue }" != "001">-->
										<option value="${bankCard.codevalue }" <#if "${bankCard.codevalue }" == "${certificationVo.belongs2bank}">selected</#if>>${bankCard.codename }</option>
									<#--</#if>-->
								</#list>
							</select>
						</td>
					</tr>
					<tr>
						<td><font color="red"></font>银行卡号 <br>
							<input type="text" id="bankcard" name="bankcard" value="${certificationVo.bankcard }">
						</td>
					</tr>
			        <tr>
						<td>资格证号<br>
							<input type="text" id="cgfns" name="cgfns" value="${certificationVo.cgfns}">
							<input type="hidden" id="agentid" name="agentid" value="${certificationVo.agentid}">
						</td>
					</tr>
					 <tr>
						<td>推荐人工号<br>
							<input type="text" id="referrer" name="referrer" value="${certificationVo.referrer}">
						</td>
					</tr>
					 <tr>
						<td>推荐人<br>
							<input type="text" id="referrername" name="referrername" value="${certificationVo.referrername}">
						</td>
					</tr>
                    <tr>
                        <td>
                            <font color="red"></font>业务员职级<br>
                            <select id="rank">
                                <#--<option value="0" >请选择</option>-->
							<#list rankList as rank>

                                    <option value="${rank.codevalue }" <#if "${rank.codevalue }" == "${certificationVo.rank}">selected</#if>>${rank.codename }</option>

							</#list>
                            </select>
                        </td>
                    </tr>
			        <tr>
						<td><font color="red"></font>正式工号<br>
							<input type="text" id="formalnum" disabled="disabled" value="${certificationVo.formalnum}">
						</td>
					</tr>
					<tr>
						<td>用户的备注<br>
							${certificationVo.noti}
						</td>
					</tr>
					<tr>
						<td>给用户备注<br>
							<input type="text" id="commentcontent" name="commentcontent" value="${certificationVo.commentcontent}">
							<input type="hidden" id="returnfrom" name="returnfrom" value="${from}">
						</td>
					</tr>
					<tr>
						<td>
							<ul style="list-style-type:none;">
							<#if "${edit}" == "true">
								<li><button type="button" class="btn btn-primary btn-sm" onclick="certificate('${certificationVo.id}','0');">保存信息</button></li>
								<li><button type="button" class="btn btn-primary btn-sm" onclick="getFormalNum('${certificationVo.id}');" id="getNum" disabled="disabled">认证成功</button></li>
								<li><button type="button" class="btn btn-primary btn-sm" onclick="certificate('${certificationVo.id}','3');">认证失败</button></li>
								<li><button type="button" class="btn btn-primary btn-sm" onclick="certificate('${certificationVo.id}','2');">退回修改</button></li>
							</#if>
                                <#if "${from}" == "true">
                                	<li><a href="/cm/business/certificationtask/certificationMng"><button type="button" class="btn btn-primary btn-sm">返回</button></a></li>
                               </#if> 	
                                <#if "${from}" != "true">
                                	<li><a href="/cm/business/mytask/queryTask"><button type="button" class="btn btn-primary btn-sm">返回</button></a></li>
                               </#if> 
							</ul>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="modal fade" id="myModal_dept" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content has-feedback" id="modal-content">
				<div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	                <h4 class="modal-title" id="myModalLabel">选择机构</h4>
	                <br>
		            <div class="input-group" style="width:100%">
		                <input type="text" class="form-control ztree-search" id = "treesearch" data-bind="dept_tree" name="treesearch" placeholder="输入机构名称进行搜索"/>
		                <span class="glyphicon glyphicon-search form-control-feedback" style="display:inline-block;"></span>
		            </div>
            	</div>
				<div class="modal-body" style="overflow: auto; height: 400px;">
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
	</div><!--row -->
	</div><!--panel-body-->
</div>
</div>
</body>
</html>
