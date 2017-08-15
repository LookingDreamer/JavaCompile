<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>订单管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/insbOrderManagement","chn/channelTree" ]);
</script>
<style type="text/css">
	.showright{
	position:relative;
}
img{
	position:absolute;
	top:27px;
	right:30px;
}
</style>
</head> 
<body>
	<div class="container-fluid">
		<div class="panel panel-default" id="simplequery1" style="display: none;">
		  <div class="panel-heading">
		  <div class="row">
			<div class="col-md-7">
		  	<ul class="nav nav-pills">
		  		<li role="presentation" <#if "${queryModel.orderstatus}" == '0' || "${queryModel.orderstatus}" == ''>class="active" </#if> onclick="checkthisstatus(this,'')"><a href="#">全部</a></li>
			  	<#list statusList as status>
					<li role="presentation" <#if "${status.codevalue}" == "${queryModel.orderstatus!'' }">class="active" </#if> onclick="checkthisstatus(this,${status.codevalue })"><a href="#">${status.codename }</a></li>
				</#list>
			</ul>
			</div>
			<div class="col-md-3" align="right" >
			<div class="form-group" style="margin-bottom:0px">
			  	<input type="text" class="form-control" id="nameorcarno" name="nameorcarno" placeholder="请输入车牌" value="${queryModel.carnumorcname!'' }">
			  	<br/>
			  	<button id="simplequerybutton" type="button" name="simplequerybutton" class="btn btn-primary">搜索</button>
			  	<button id="resetbutton" type="button" name="resetbutton" class="btn btn-primary">重置</button>
			  	<button id="seniorsearch" type="button" name="seniorsearch" class="btn btn-primary">高级搜索</button>
			  </div>
			   </div>
		  </div>
		  </div>
		</div>
	
		<div class="panel panel-default" id="seniorquery">
		  <div class="panel-heading">
		  <div class="row">
			<div class="col-md-11">高级搜索</div>
		 	<div class="col-md-1">
		  		<button id="closesearch" type="button" name="closesearch" class="btn btn-primary btn-sm">关闭</button>
		  	</div>
		  </div>
		  </div>
		  <div class="panel-body">
		    <div class="row" style="margin-left:-35px;">
				<div class="col-md-12">
					<form role="form" id="queryorder" action="queryorderlist" method="post">
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">被保人姓名</label> <input type="text"
							class="form-control m-left-5" id="insuredname" name="insuredname" placeholder="" value="${queryModel.insuredname!'' }">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputName">供应商</label> 
						<input type="hidden" id="providerid" name="providerid" value="${queryModel.providerid!'' }">
						<input type="text" value="${queryModel.providername!'' }"	class="form-control m-left-2" id="providername" name="providername" placeholder="请选择" readonly="readonly">
						 <a id="checkprovider">选择</a>
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">车牌号码</label> <input type="text"
							class="form-control m-left-5" id="carnum" name="carnum" placeholder="" value="${queryModel.carnum!'' }">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">业务跟踪号</label> 
						 <input type="text"	class="form-control m-left-5" id="taskid" name="taskid" placeholder="" value="${queryModel.taskid!'' }">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputName">联系电话</label> <input type="text"
							class="form-control m-left-5" id="phone" name="phone" placeholder="" value="${queryModel.phone!'' }">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputOrgName">订单状态&nbsp;&nbsp;&nbsp;&nbsp;</label> 
						<select class="form-control" id="orderstatus" name="orderstatus">
						  <option value="">全部</option>
						  <#list statusList as status>
						  	<option value="${status.codevalue }" <#if "${status.codevalue}" == "${queryModel.orderstatus!'' }">selected </#if> >${status.codename }</option>
						  </#list>
						</select>
					</div>
					<div class="form-group form-inline col-md-4" style="display:none">
						<label for="exampleInputOrgName">用户类型</label> 
						<select class="form-control m-left-2" id="usertype" name="usertype">
						  <option value="">全部</option>
						  <option value="2">正式用户</option>
						  <option value="1">试用用户</option>
						</select>
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">支付流水号&nbsp;&nbsp;&nbsp;</label> 
						<input type="text" class="form-control" id="paymentTransaction" name="paymentTransaction" placeholder="" value="${queryModel.paymentTransaction!'' }">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputName">代理人工号</label> <input type="text"
							class="form-control" id="agentcode" name="agentcode" placeholder="" value="${queryModel.agentcode!'' }">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">业务完成关闭者</label> 
						<input type="text" class="form-control" id="shutter" name="shutter" placeholder="">
					</div>
                        <div class="form-group form-inline col-md-4">
                            <label>渠道来源&nbsp;&nbsp;&nbsp;&nbsp;</label>
                            <input type="hidden" id="channelinnercode" name="channelinnercode" value="${queryModel.channelinnercode!'' }"/>
                            <input type="text" class="form-control" id="channelName" name="channelName" value="${queryModel.channelName!'' }" readonly="readonly"/>
                        </div>
                        <div class="form-group form-inline col-md-8">
                            <label for="miniagentcode">mini用户工号</label>
							<input type="text" class="form-control" id="miniagentcode" name="miniagentcode" placeholder="" value="${queryModel.miniagentcode!'' }">
                        </div>
					<div class="form-group form-inline col-md-6">
						<label for="exampleInputCode">任务创建时间</label> 
						<input type="text"	class="form-control form_datetime" id="startdate" name="startdate" readonly placeholder="" value="${queryModel.startdate!'' }"> -
						<input type="text"	class="form-control form_datetime" id="enddate" name="enddate" readonly placeholder="" value="${queryModel.enddate!'' }">
					</div>
					<div class="form-group form-inline col-md-2">
						<input type="hidden" id="currentPage" name="currentPage" value="${allData.currentPage!"0" }">
						<input type="hidden" id="carnumorcname" name="carnumorcname" value="">
						<button id="seniorquerybutton" type="button" name="seniorquerybutton" class="btn btn-primary">搜索</button>&nbsp;
						<button id="seniorresetbutton" type="button"  class="btn btn-primary">重置</button>
					</div>
				</form>
				</div>
		  	</div>
		  </div>
		</div>
		<div class="panel panel-default">
		  <div class="panel-body">
		    <div class="row">
				<div class="col-md-12">
					<#list allData.rowList as row>
						<table class="table table-bordered" style="border: 2px solid gray;">
						<tr>
							<td valign="middle" bgcolor="#428BCA" width="45%" class="showright"><label>车牌：${row.carlicenseno!"" }</label>   
							<label class="m-left-5">被保人：${row.insuredname!"" }</label> 
							<#if row.purchaserchannel>
							<img src="${staticRoot}/images/system/resource/resource1.png" style="width:35px;height:35px;"/>
							</#if>
							<br> 
							<label>代理人：${row.agentcode!"" }(${row.agentname!"" })</label><br>
							<label>所属团队：${row.teamname!"" }</label><label class="m-left-5 resource" <#if row.purchaserchannel>style="color:white"</#if>>渠道来源：${row.purchaserchannel!"" }</label></td>
							<td valign="middle"><label>商业险：${row.premium!"" } </label>  
								<label class="m-left-5">投保单号：${row.proposalformno!"" }</label><label class="m-left-5">保单号：${row.policyno!"" }</label><br>
							<label>交强险：${row.jqpremium!"" }</label>      
							<label class="m-left-5">投保单号：${row.jqproposalformno!"" }</label><label class="m-left-5">保单号：${row.jqpolicyno!"" }</label>
							
							</td>
						</tr>
						<tr>
							<td><label>业务跟踪号:${row.taskid!"" }</label></td>
							<td><label>任务创建时间：${row.createtime!"" }</label></td>
						</tr>
						<tr>
							<td><label>供应商:${row.prvshotname!"" }</label></td>
							<td><label>用户的备注：${row.noti!"" }</label></td>
						</tr>
						<tr>
							<td><label>网点: ${row.deptname!"" }</label></td>
							<td align="right">
							<div style="float: left;">
								<label>订单状态: ${row.codename!"" }</label>&nbsp;&nbsp;&nbsp;
								<label>支付方式: ${row.payname!"未知" }</label>&nbsp;&nbsp;&nbsp;
								<label>二次支付时间: ${row.orderPayTime!"未知" }</label>
								</br>
									<label>
										支付状态: 
										<#if row.payresult == "01" || row.payresult == "00" || row.payresult == "0">
											未支付
										<#elseif row.payresult == "02">
											支付成功
										<#elseif row.payresult == "03">
											退款
										<#elseif row.payresult == "04">
											已过期
										<#elseif row.payresult == "05">
											支付失败
										<#else>
											未知
										</#if>
									</label>
								&nbsp;&nbsp;&nbsp;
								<label>支付平台流水号: ${row.payflowno!"未知" }</label>
							</div>
							<div style="float: right;"><button id="querybutton" type="button" name="querybutton" class="btn btn-primary" onclick="searchorderdetail('${row.id}','${row.pid }','${row.codename }','${row.payid}','${row.taskcode}');">查看详情</button></div>
							</td>
						</tr>
					</table>
					</#list>
				</div>
				<form hidden="true" name="detailinfo" id="detailinfo" action="queryorderdetail" method="POST">
					<input type="text" name="id" id="id" value="123"/>
					<input type="text" name="pid" id="pid"/>
					<input type="text" name="payid" id="payid"/>
					<input type="text" name="taskcode" id="taskcode"/>
					<input type="text" name="codename" id="codename"/>
				</form>
		  	</div>
		  </div>
		  <div class="panel-footer">
		  <div class="row">
				<div class="col-md-12">
			  		<div class="col-md-8">
						共<label id="lblToatl">${allData.total!"0" }</label>条数据 第[<label id="lblCurent">${allData.currentPage!"0" }</label>]页/共[<label
							id="lblPageCount">${allData.totalPage!"0" }</label>]页
					</div>
					<div class="col-md-4">
						跳转到：第<select name="goto" id="goto"
							onchange="go(this.options[this.options.selectedIndex].value)">
							<#if allData.totalPage??>
							<#list 1..allData.totalPage as i> 
								<option value="${i}" <#if "${allData.currentPage}" == "${i}">selected </#if>>${i}</option>
							</#list>
							<#else>
								<option value="1">1</option>
							</#if>
						</select>页 <a id="first" href="#">首页</a> <a id="previous" href="#">上一页</a> <a
							id="next" href="#"> 下一页</a> <a id="last" href="#">末页</a>
					</div>
			</div>
		  	</div>
		  </div>
		</div>
</div>
<div id="showprovider" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="gridSystemModalLabel">选择供应商</h4>
      </div>
      <div class="modal-body" style="overflow: auto;height:390px;">
        <div class="container-fluid">
          <div class="row">
			<ul id="providertree" class="ztree"></ul>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
    </div>
  </div>
</div>
    <!--渠道来源选择弹出框-->
	<#include "chn/channelTree.ftl"/>
</body>
</html>
