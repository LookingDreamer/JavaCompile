<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>险种管理-险种详情/修改</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "zzbconf/riskdetail" ]);
</script>
<body>
	<div class="container-fluid">
		<form role="form" id="riskaddform" action="riskaddorupdate"
			method="post">
			<div class="alert alert-danger alert-dismissible" id="riskerror"
				role="alert" style="display: none;"></div>
			<div class="panel panel-default m-bottom-5">
				<div class="panel-heading padding-5-5">险种详情</div>
				<div class="panel-body">
					<div class="row">
						<div class="form-group col-md-4 form-inline">
							<label for="exampleInputCode">险种编码</label> 
							<input type="text"
								class="form-control " id="riskcode" name="riskcode"
								placeholder="" value="${risk.riskcode!''}">
						</div>
						<div class="form-group col-md-4 form-inline">
							<label for="exampleInputName">险种名称</label> <input type="text"
								class="form-control " id="riskname" name="riskname"
								placeholder="" value="${risk.riskname!''}">
						</div>
						<div class="form-group col-md-4 form-inline">
							<label for="exampleInputName">险种简称</label> <input type="text"
								class="form-control " id="riskshortname" name="riskshortname"
								placeholder="" value="${risk.riskshortname!''}">
						</div>
						<div class="form-group col-md-4 form-inline">
							<label for="exampleInputCode">保险公司</label> <input type="hidden"
								id="providecode" name="providecode" value="${risk.provideid!''}">
                            <select class="form-control " id="provideid" name="provideid" style="width:240px;">
                                <!-- <option value="" selected="selected">请选择</option> -->
                                <option value="">请选择</option>
								<#list provider as p>
	                                <option value="${p.id}" <#if risk.provideid==p.id> selected="selected" </#if>>${p.name}</option>
								</#list>
                            </select>
						</div>
						<div class="form-group col-md-4  form-inline">
							<label for="exampleInputName">险种类型</label> 
							<!-- <select class="form-control " id="risktype" name="risktype">
								<option value="" selected="selected">请选择</option>
								<#list risktype as risktypecode>
								<option value="${risktypecode.codevalue}">${risktypecode.codename}</option>
								</#list>
							</select> -->
							<input type="text" class="form-control " id="risktype" name="risktype" value="车险" readonly>
						</div>
						<div class="form-group col-md-4  form-inline">
							<label for="exampleInputOrgName">险种小类</label> 
							 <select class="form-control " id="status" name="status">
								 	<option value="">请选择</option>
									<#list riskstatus as riskstatuscode>
									<!-- <option value="${riskstatuscode.codevalue}">${riskstatuscode.codename}</option> -->
									<option value="${riskstatuscode.codevalue}" parent="${riskstatuscode.prop2}" <#if risk.status==riskstatuscode.codevalue> selected="selected" </#if>>${riskstatuscode.codename}</option>
									</#list>
								</select>
						</div>
						<div class="form-group col-md-12  form-inline">
							<label for="exampleInputOrgName">险种说明</label>
							<textarea class="form-control " rows="2" cols="138" name="noti"
								id="noti">${risk.noti!''}</textarea>

						</div>
					</div>
				</div>
				<div class="panel-footer">
					<button id="savebutton" type="button" name="savebutton"
						class="btn btn-primary" onclick="riskadd()">保存</button>
					<button id="savebutton" type="button" name="backbutton"
						class="btn btn-primary" onclick="back()">返回</button>
				</div>
			</div>
		</form>
		<ul class="nav nav-tabs nav-justified">
			<li role="presentation" id="kindli" class='active'><a href="#"
				onclick="showdiv('kind')">险别定义</a></li>
<!-- 			<li role="presentation" id="itemli"><a href="#" -->
<!-- 				onclick="showdiv('item')">数据项配置</a></li> -->
<!-- 			<li role="presentation" id="xbli"><a href="#" -->
<!-- 				onclick="showdiv('xb')">续保配置</a></li> -->
			<li role="presentation" id="imgli"><a href="#"
				onclick="showdiv('img')">影像件配置</a></li>
		</ul>
		<!-- kinddiv -->
		<div class="panel-body" id="kinddiv">
			<div class="panel panel-default m-bottom-5">
				<div class="panel-heading padding-5-5">查询</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-md-12 form-inline">
							<div class="form-group col-md-4">
								<label for="exampleInputCode">险别名称</label>
								<input type="hidden" class="form-control " id="riskid" name="riskid"
									placeholder="" value="${risk.id!''}">
								 <input type="text" class="form-control " id="kindname_q" name="kindname_q">
							</div>
							<div class="form-group col-md-4">
								<label for="exampleInputCode">险别编码</label> <input type="text"
									class="form-control " id="kindcode_q" name="kindcode_q">
							</div>
						</div>
					</div>
				</div>
				<div class="panel-footer">
					<button  type="button" class="btn btn-primary" 
					    id="init_data">置为默认险别</button>
					<button id="savebutton" type="button" name="savebutton"
						class="btn btn-primary" onclick="kindaquery()">查询</button>
					<button id="savebutton" type="button" name="savebutton"
						class="btn btn-primary" onclick="kindadd('0')">新增</button>
					<button id="savebutton" type="button" name="savebutton"
						class="btn btn-primary" onclick="kindupdate('1')">修改</button>
					<button id="savebutton" type="button" name="savebutton"
						class="btn btn-primary" onclick="kinddelete()">删除</button>
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading padding-5-5">
					<div class="row">
						<div class="col-md-2">结果</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<table id="table-kind"></table>
					</div>
				</div>
			</div>
		</div>
		<!-- itemdiv -->
	<!-- 	<div class="panel-body" id="itemdiv" style="display: none;">
			<div class="panel panel-default m-bottom-5">
				<div class="panel-heading padding-5-5">查询</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-md-12 form-inline">
							<div class="form-group col-md-4">
								<label for="exampleInputCode">要素名称</label> <input type="text"
									class="form-control " id="itemname_q" name="itemname_q">
							</div>
							<div class="form-group col-md-4">
								<label for="exampleInputCode">要素类型</label> <select
									class="form-control " id="itemtype_q" name="itemtype_q">
									<option value="">请选择</option> <#list itemtype as itemtypecode>
									<option value="${itemtypecode.codevalue }">${itemtypecode.codename}</option>
									</#list>
								</select>
							</div>
							<div class="form-group col-md-4">
								<label for="exampleInputCode">录入方式</label> <select
									class="form-control " id="iteminputtype_q"
									name="iteminputtype_q">
									<option value="">请选择</option> <#list iteminputtype as
									iteminputtypecode>
									<option value="${iteminputtypecode.codevalue }">${iteminputtypecode.codename}</option>
									</#list>
								</select>
							</div>
						</div>
					</div>
				</div>
				<div class="panel-footer">
					<button id="savebutton" type="button" name="savebutton"
						class="btn btn-primary" onclick="itemquery()">查询</button>
					<button id="savebutton" type="button" name="savebutton"
						class="btn btn-primary" onclick="itemadd()">新增</button>
					<button id="savebutton" type="button" name="savebutton"
						class="btn btn-primary" onclick="itemupdate()">修改</button>
					<button id="savebutton" type="button" name="savebutton"
						class="btn btn-primary" onclick="itemdelete()">删除</button>
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading padding-5-5">
					<div class="row">
						<div class="col-md-2">结果</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<table id="table-item"></table>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body" id="xbdiv" style="display: none;">
			<div class="">
				<div class="form-group col-md-5 form-inline">
					<#list renewaltype as renewal>
						<input id="renewal${renewal_index }" name="renewaltype" type="checkbox" value="${renewal.codevalue }"
						<#if "${risk.renewaltype }"?contains("${renewal.codevalue}") >checked="checked" </#if>>
						${renewal.codename!'' }&nbsp;&nbsp;	
											
					</#list>
				</div>
				<button id="savebutton" type="button" class="btn btn-primary" onclick="riskadd()">保存</button>
			</div>				
			<br/>
			<div class="panel panel-default m-bottom-5">
				<div class="panel-heading padding-5-5">查询</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-md-12 form-inline">
							<div class="form-group col-md-4">
								<label for="exampleInputCode">数据项名称</label> <input type="text"
									class="form-control " id="reitemname_q" name="reitemname_q">
							</div>
							<div class="form-group col-md-4">
								<label for="exampleInputCode">使用通道</label> <select
									class="form-control " id="reitemtype_q" name="reitemtype_q">
									<option value="0">请选择</option> 
									<option value="istraffic">单买交强</option> 
									<option value="isbusiness">单买商业</option> 
									<option value="istrafficandbusiness">商业+交强</option>
								</select>
							</div>
						</div>
					</div>
				</div>
				<div class="panel-footer">
					<button id="savebutton" type="button" class="btn btn-primary"
						onclick="reitemquery()">查询</button>
					<button id="savebutton" type="button" class="btn btn-primary"
						onclick="reitemadd()">新增</button>
					<button id="savebutton" type="button" class="btn btn-primary"
						onclick="reitemupdate()">修改</button>
					<button id="savebutton" type="button" class="btn btn-primary"
						onclick="reitemdelete()">删除</button>
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading padding-5-5">
					<div class="row">
						<div class="col-md-2">结果</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<table id="table-reitem"></table>
					</div>
				</div>
			</div>
		</div> -->
		<div class="panel-body" id="imgdiv" style="display: none;">
			<div class="panel panel-default m-bottom-5">
				<div class="panel-heading padding-5-5">查询</div>
				<div class="panel-body">
					<div class="row">
						<div class="col-md-12">
							<!-- <div class="form-group col-md-4">
								<label for="exampleInputCode">影像名称</label> <input type="text"
									class="form-control " id="riskimgname_q" name="riskimgname_q">
							</div> -->
							<div class="form-group col-md-4">
								<label for="exampleInputCode">影像类型</label> 
								 <select class="form-control " id="riskimgtype_q" name="riskimgtype_q">
									<option value="">请选择</option> <#list riskimgtype as
									riskimgtypecode>
									<option value="${riskimgtypecode.codevalue }">${riskimgtypecode.codename}</option>
									</#list>
								</select> 				
						   </div>
					</div>
				</div>
				<div class="panel-footer">
					<button id="init_riskimg" type="button" class="btn btn-primary" 
					    onclick="init_riskimg()">置为默认影像</button>
					<button id="savebutton" type="button" class="btn btn-primary"
						onclick="imgquery()">查询</button>
					<button id="savebutton" type="button" class="btn btn-primary"
						onclick="imgadd()">新增</button>
					<button id="savebutton" type="button" class="btn btn-primary"
						onclick="imgupdate()">修改</button>
					<button id="savebutton" type="button" class="btn btn-primary"
						onclick="imgdelete()">删除</button>
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading padding-5-5">
					<div class="row">
						<div class="col-md-2">结果</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<table id="table-img"></table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- kindaddmodal -->
	<form id="kindaddfrom" name="kindaddfrom" class="remote">
		<div class="modal fade" id="kindModal" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" data-backdrop="false"
			data-keyboard="false" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content" id="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">×</span><span class="sr-only">Close</span>
						</button>
						<span class="modal-title form-inline">险别新增</span>
                        <select name="selectKind" class="form-control" id="selectKind" style="width:150px;" onchange="selectkind();">
                            <option value=""></option>
							<#list riskKinds as rk>
                                <option value="${rk.riskkindcode}">${rk.riskkindname}</option>
							</#list>
                        </select>
					</div>
					<div class="modal-body " style="overflow: auto; height: 400px;">
						<div class="alert alert-danger alert-dismissible"
							id="kindadderror" role="alert" style="display: none;"></div>
						<input type="hidden" id='kindid' name='kindid' />
						<input type="hidden" id='kindidflag' name='kindidflag' />
						<div class="form-group   form-inline">
							<label for="exampleInputCode">险别编码</label> <input type="text"
								class="form-control " id="kindcode" name="kindcode" value="${riskkindconfig.riskkindcode!'' }" readonly/>
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputCode">险别名称</label> <input type="text"
								class="form-control " id="kindname" name="kindname" value="${riskkindconfig.riskkindname!'' }" readonly/>
						</div>
						<div class="form-group  form-inline">
							<label for="exampleInputName">是否保全选项</label> <select
								class="form-control " id="isamount" name="isamount">
								<#list kindisamount as isamountcode>
								<option value="${isamountcode.codevalue}" <#if (isamountcode_index==0) > selected="selected" </#if> >
								${isamountcode.codename}</option>
								</#list>
							</select>
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputName">保额选项</label>
								<input class="form-control " type="text" name="amountselect" id="amountselect" value="${riskkindconfig.amountselect!'' }"/>
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputName">前置险别</label>
							<input class="form-control " type="text" id="preriskkind" name="preriskkind"value="${riskkindconfig.prekindcode!'' }" readonly>
							<!--<select
								class="form-control " id="preriskkind" name="preriskkind">
								 <#list riskkindName as kind>
									 <option value="${kind}" >${kind}</option>
								</#list> 
							</select>-->
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputName">不计免赔</label> <select
								class="form-control " id="notdeductible" name="notdeductible">
								<#list notdeductible as notdeductiblecode>
								<option value="${notdeductiblecode.codevalue}" <#if (notdeductiblecode_index==0) > selected="selected" </#if>>${notdeductiblecode.codename}</option>
								</#list>
							</select>
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputName">是否启用</label> <select
								class="form-control " id="isusing" name="isusing">
								<#list isusing as isusingcode>
								<option value="${isusingcode.codevalue}"<#if (isusingcode_index==0) > selected="selected" </#if>>${isusingcode.codename}</option>
								</#list>
							</select>
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputName">备注</label>
							<textarea class="form-control " rows="2" cols="10"
								name="kindnoti" id="kindnoti" value="${riskkindconfig.kindnoti!'' }"></textarea>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="button" class="btn btn-primary"
							onclick="saveorupdatekind()">提交更改</button>
					</div>

				</div>
			</div>
		</div>
	</form>
	<!-- itemaddmodal -->
	<!-- <form action="" id="itemaddform" name="itemaddform">
		<div class="modal fade" id="itemModal" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" data-backdrop="false"
			data-keyboard="false" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content" id="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">×</span><span class="sr-only">Close</span>
						</button>
						<span class="modal-title form-inline">要素编辑</span>
					</div>
					<div class="modal-body " style="overflow: auto; height: 400px;">
						<div class="alert alert-danger alert-dismissible"
							id="itemadderror" role="alert" style="display: none;"></div>
						<input type="hidden" id='itemid' name='itemid' />
						<div class="form-group   form-inline">
							<label for="exampleInputCode">要素编码</label> <input type="text"
								class="form-control " id="itemcode" name="itemcode" />
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputCode">要素名称</label> <input type="text"
								class="form-control " id="itemname" name="itemname" />
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputCode">要素类型</label> <select
								class="form-control " id="itemtype" name="itemtype">
								<#list itemtype as itemtypecode>
								<option value="${itemtypecode.codevalue }" <#if (itemtypecode_index==0) > selected="selected" </#if>>${itemtypecode.codename}</option>
								</#list>
							</select>
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputName">是否启用</label> <select
								class="form-control " id="itemisusing" name="itemisusing">
								<#list isusing as isusingcode>
								<option value="${isusingcode.codevalue}" <#if (itemisusing_index==0) > selected="selected" </#if>>${isusingcode.codename}</option>
								</#list>
							</select>
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputName">录入方式</label> <select
								class="form-control " id="inputtype" name="inputtype">
								<#list iteminputtype as iteminputtypecode>
								<option value="${iteminputtypecode.codevalue}" <#if (iteminputtypecode_index==0) > selected="selected" </#if>>${iteminputtypecode.codename}</option>
								</#list>
							</select>
						</div>
						<div class="form-group   form-inline" style="display: none;">
							<label for="exampleInputCode">可选内容</label> <input type="text" placeholder="可选条目以“,”分隔"
								class="form-control " id="optional" name="optional" />
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputName">报价必录</label> <select
								class="form-control " id="isquotemust" name="isquotemust">
								<option value="0" selected="selected">否</option>
								<option value="1">是</option>
							</select>
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputName">数据提核保</label> <select
								class="form-control " id="isunderwriteusing"
								name="isunderwriteusing">
								<option value="0" selected="selected">否</option>
								<option value="1">是</option>
							</select>
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputName">核保必录</label> <select
								class="form-control " id="isunderwritemust"
								name="isunderwritemust">
								<option value="0" selected="selected">否</option>
								<option value="1">是</option>
							</select>
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputName">备注</label>
							<textarea class="form-control " rows="2" cols="10"
								name="itemnoti" id="itemnoti"></textarea>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="button" class="btn btn-primary"
							onclick="saveorupdateitem()">提交更改</button>
					</div>
				</div>
			</div>
		</div>
	</form> -->
	<!-- reitemaddmodal -->
	<!-- <form id="reitemfrom" name="reitemfrom">
		<div class="modal fade" id="reitemModal" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" data-backdrop="false"
			data-keyboard="false" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content" id="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">×</span><span class="sr-only">Close</span>
						</button>
						<span class="modal-title form-inline">续保配置编辑</span>
					</div>
					<div class="modal-body " style="overflow: auto; height: 400px;">
						<div class="alert alert-danger alert-dismissible" id="reitemerror"
							role="alert" style="display: none;"></div>
						<input type="hidden" id='reitemid' name='eritemid' />
						<div class="form-group   form-inline">
							<label for="exampleInputCode">数据项名称</label> <input type="text"
								class="form-control " id="reitemname" name="reitemname" />
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputName">单买交强</label> <select
								class="form-control " id="istraffic" name="istraffic">
								<option value="0" selected="selected">否</option>
								<option value="1">是</option>
							</select>
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputName">单买商业</label> <select
								class="form-control " id="isbusiness" name="isbusiness">
								<option value="0" selected="selected">否</option>
								<option value="1">是</option>
							</select>
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputName">商业+交强</label> <select
								class="form-control " id="istrafficandbusiness"
								name="istrafficandbusiness">
								<option value="0" selected="selected">否</option>
								<option value="1">是</option>
							</select>
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputName">提示信息</label>
							<textarea class="form-control " rows="2" cols="10"
								name="presentation" id="presentation">
						</textarea>
						</div>
						<div class="form-group   form-inline">
							<label for="exampleInputName">备注</label>
							<textarea class="form-control " rows="2" cols="10"
								name="itemnoti" id="reitemnoti"></textarea>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="button" class="btn btn-primary"
							onclick="saveorupdatereitem()">提交更改</button>
					</div>
				</div>
			</div>
		</div>
		
	</form> -->
	<!-- imgmodal 修改影像类型页面-->
	<form id="imgfrom" name="imgfrom">
		<div class="modal fade" id="imgModal" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" data-backdrop="false"
			data-keyboard="false" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content" id="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">×</span><span class="sr-only">Close</span>
						</button>
						<span class="modal-title form-inline">影像编辑</span>
					</div>
					<div class="modal-body " style="overflow: auto; height: 400px;">
						<div class="alert alert-danger alert-dismissible" id="imgerror"
							role="alert" style="display: none;"></div>
						<input type="hidden" id='imgid' name='imgid' />
						<!-- <input type="hidden" id='imgidflag' name='imgidflag' /> -->
						<!-- <div class="form-group col-md-12   form-inline">
							<label for="exampleInputCode">影像名称</label> <input type="text"
								class="form-control " id="riskimgname" name="riskimgname" />
						</div> -->
						<div class="form-group col-md-4 form-inline">
							<label for="exampleInputCode">影像类型</label> <select
								class="form-control " id="riskimgtype" name="riskimgtype">
								<#list riskimgtype as
								riskimgtypecode>
								<option id="rt_${riskimgtypecode.codevalue}" value="${riskimgtypecode.codevalue}" <#if (riskimgtypecode_index==0) > selected="selected" </#if>>${riskimgtypecode.codename}</option>
								</#list>
							</select>
						</div>
						<div class="form-group col-md-12  form-inline">
							<label for="exampleInputName">是否启用</label> <select
								class="form-control " id="imgisusing" name="isusing">
								<option value="0" selected="selected">否</option>
								<option value="1">是</option>
							</select>
						</div>
						<div class="form-group col-md-12 form-inline">
							<label for="exampleInputName">备注</label>
							<textarea class="form-control " rows="2" cols="10" name="noti"
								id="imgnoti"></textarea>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="button" class="btn btn-primary"
							onclick="updateimg()">提交更改</button>
					</div>
				</div>
			</div>
		</div>
	</form>
	<!-- addimgModal  新增影像类型页面 -->
	<form id="addimgfrom" name="imgfrom">
		<div class="modal fade" id="addimgModal" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" data-backdrop="false"
			data-keyboard="false" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content" id="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">×</span><span class="sr-only">Close</span>
						</button>
						<span class="modal-title form-inline">影像新增</span>
					</div>
					<div class="modal-body " style="overflow: auto; height: 400px;">
						<div class="alert alert-danger alert-dismissible" id="imgerror"
							role="alert" style="display: none;"></div>
						<input type="hidden" id='imgid' name='imgid' />
						<!-- <input type="hidden" id='imgidflag' name='imgidflag' /> -->
						<div class="col-md-12">
								<div class="row">					
									<div class="col-xs-5">
										<select name="from" class="js-multiselect form-control" data-right-selected="#right_Selected_1" size="8" multiple="multiple">
											<!-- <#list riskimgtype as imgtype>									
										  		<option  value="${imgtype.codevalue }" > ${imgtype.codename}</option>					  																		  		
										  	</#list> -->
										  	 <option value=""></option>
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
											<!-- <#list  riskimgtype as imgtype>
												 <#list addImgList as selectedimgtype>		
													<#if imgtype.codevalue==selectedimgtype.riskimgtype>
														<option value="${imgtype.codevalue }"> ${imgtype.codename!"" }</option>					  				
													</#if>				  		
												  </#list>
											  </#list> -->
											  <option value=""></option>
										</select>
									</div>
								</div>
							</div>
						<div class="form-group col-md-12  form-inline">
							<label for="exampleInputName">是否启用</label> <select
								class="form-control " id="imgisusingadd" name="isusing">
								<option value="0" selected="selected">否</option>
								<option value="1">是</option>
							</select>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="button" class="btn btn-primary"
							onclick="saveimg()">提交更改</button>
					</div>
				</div>
			</div>
		</div>
	</form>
	</div>
	<!--选择公司modal -->
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
					<h4 class="modal-title" id="gridSystemModalLabel">选择供应商</h4>
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


</body>
</html>