<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8"/>
<title>查看并修改供应商</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${staticRoot}/css/appmain.css" rel="stylesheet">
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/skin/default.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/provider" ]);
</script>
</head>
<body>
    
<div id="saveupdatediv" class="container-fluid" style="margin-bottom: 20px">
	 <form id = "prosaveupdateform"  action="saveorupdatepro" method="post" enctype="multipart/form-data">
	    <div  class="row">
		     <div class="col-md-12">
		   	  <div class="panel panel-default m-bottom-5">
		   	   <div class="panel-heading padding-5-5">供应商机构配置</div>
		   		<div class="panel-body">
				 <table style="tr{height:40px;}">
					<tr>
						<td><label for="exampleInputName">供应商代码</label></td>
						<td><input class="form-control " type="text" value="${proobject.prvcode!'' }" name="prvcode" placeholder="" readonly="readonly"></td>
						<td><label for="exampleInputName">供应商名称</label></td>
						<td><input class="form-control " type="text" value="${proobject.prvname }" name="prvname" placeholder=""></td>
						<td><label for="exampleInputName">供应商简称</label></td>
						<td><input class="form-control " type="text" value="${proobject.prvshotname!'' }" name="prvshotname" placeholder=""></td>
					</tr>
					<tr>
						<td><label for="exampleInputName">供应商级别</label></td>
						<td>
							<select name="prvgrade" class="form-control " >
							<#if proobject.prvgrade==''>
							<option selected="selected" value="">请选择供应商</option>
							<option value="01">1级</option>
							<option value="02">2级</option>
							<option value="03">3级</option>
							<option value="04">4级</option>
							<option value="05">5级</option>
							<option value="06">6级</option>
							<#elseif proobject.prvgrade=='00'>
							<option selected="selected" value="00">0级</option>
							<option value="01">1级</option>
							<option value="02">2级</option>
							<option value="03">3级</option>
							<option value="04">4级</option>
							<option value="05">5级</option>
							<option value="06">6级</option>
							<#elseif proobject.prvgrade=='01'>
							<option value="00">0级</option>
							<option selected="selected" value="01">1级</option>
							<option value="02">2级</option>
							<option value="03">3级</option>
							<option value="04">4级</option>
							<option value="05">5级</option>
							<option value="06">6级</option>
							<#elseif proobject.prvgrade=='02'>
							<option value="00">0级</option>
							<option value="01">1级</option>
							<option selected="selected" value="02">2级</option>
							<option value="03">3级</option>
							<option value="04">4级</option>
							<option value="05">5级</option>
							<option value="06">6级</option>
							<#elseif proobject.prvgrade=='03'>
							<option value="00">0级</option>
							<option value="01">1级</option>
							<option value="02">2级</option>
							<option selected="selected" value="03">3级</option>
							<option value="04">4级</option>
							<option value="05">5级</option>
							<option value="06">6级</option>
							<#elseif proobject.prvgrade=='04'>
							<option value="00">0级</option>
							<option value="01">1级</option>
							<option value="02">2级</option>
							<option value="03">3级</option>
							<option selected="selected" value="04">4级</option>
							<option value="05">5级</option>
							<option value="06">6级</option>
							<#elseif proobject.prvgrade=='05'>
							<option value="00">0级</option>
							<option value="01">1级</option>
							<option value="02">2级</option>
							<option value="03">3级</option>
							<option value="04">4级</option>
							<option selected="selected" value="05">5级</option>
							<option value="06">6级</option>
							<#elseif proobject.prvgrade=='06'>
							<option value="00">0级</option>
							<option value="01">1级</option>
							<option value="02">2级</option>
							<option value="03">3级</option>
							<option value="04">4级</option>
							<option value="05">5级</option>
							<option selected="selected" value="06">6级</option>
							</#if>
							</select>
						</td>
						<td><label for="exampleInputName">供应商类型</label></td>
						<td>
						<select class="form-control" class="form-control " name="prvtype">
							<#if proobject.prvtype==''>
							<option selected="selected" value="">请选择类型</option>
							<option value="01">类型一</option>
							<option value="02">类型二</option>
							<option value="03">类型三</option>
							<#elseif proobject.prvtype=='01'>
							<option selected="selected" value="01">类型一</option>
							<option value="02">类型二</option>
							<option value="03">类型三</option>
							<#elseif proobject.prvtype=='02'>
							<option value="01">类型一</option>
							<option selected="selected" value="02">类型二</option>
							<option value="03">类型三</option>
							<#elseif proobject.prvtype=='03'>
							<option value="01">类型一</option>
							<option value="02">类型二</option>
							<option selected="selected" value="03">类型三</option>
							</#if>
						</select>
						</td>
						<td><label for="exampleInputName">上级供应商</label></td>
						
						<td>
						 <input type="hidden" id="parentcode" name="parentcode" value="${proobject.parentcode }">
						 <input type="text"	class="form-control"  id="prvname"  value="${pro.prvname }" placeholder="请选择">
						</td>
					</tr>
					<tr>
						<td><label for="exampleInputName">联系人</label></td>
						<td><input class="form-control " type="text" value="${proobject.linkname!'' }" name="linkname" placeholder=""></td>
						<td><label for="exampleInputName">联系方式</label></td>
						<td><input class="form-control " type="text" value="${proobject.linktel!'' }" name="linktel" placeholder=""></td>
						<td><label for="exampleInputName">地址</label></td>
						<td><input class="form-control " type="text" value="${proobject.address!'' }" name="address" placeholder=""></td>
					</tr>
					<tr>
						<td><label for="exampleInputName">客服电话</label></td>
						<td><input class="form-control " type="text" value="${proobject.servictel!'' }" name="servictel" placeholder=""></td>
						<td><label for="exampleInputName">网址</label></td>
						<td><input class="form-control " type="text" value="${proobject.prvurl!'' }" name="prvurl" placeholder=""></td>
					</tr>
					<tr><td><input  type="hidden" id="saveupdateid" name="id" value="${proobject.id!'' }"></td></tr>
					<tr>
						<td><label for="exampleInputName">归属机构</label></td>
						<td>
							<input type="hidden" id="affiliationorg" name="affiliationorg" value="${proobject.affiliationorg }" >
						 	<input type="text"	class="form-control " id="deptname" value="${dept.comname }" placeholder="请选择" >
						</td>
						<td><label for="exampleInputName">关联规则</label></td>
						<td>
							<input type="hidden" id="permissionorg" name="permissionorg" value="${proobject.permissionorg }" >
						 	<input type="text"	class="form-control"  id="rulepostil" placeholder="请选择" value="${rule.rulePostil }">
						</td>
					</tr>
					<tr>
						<td><label for="exampleInputName">业务类型</label></td>
						<td colspan="6">
						<label class="radio-inline">
						  <input type="checkbox"  <#if "${proobject.businesstype}"?contains("01") >checked="checked"</#if> name="businesstype"  value="01"> 传统
						</label>
						<label class="radio-inline">
						  <input type="checkbox" <#if  "${proobject.businesstype}"?contains("02") >checked="checked"</#if> name="businesstype"  value="02"> 网销
						</label>
						<label class="radio-inline">
						  <input type="checkbox" <#if  "${proobject.businesstype}"?contains("03") >checked="checked"</#if> name="businesstype"  value="03"> 电销 
						</label>
						</td>
					</tr>
					<tr>
						<td><label for="exampleInputName">渠道类型</label></td>
						<td>
							<select  class="form-control " name="channeltype">
								<#if proobject.channeltype==''>
								<option selected="selected" value="">请选择类型</option>
								<option value="01">B2C</option>
								<option value="02">B2B</option>
								<option value="03">B2C和B2B</option>
								<#elseif proobject.channeltype=='01'>
								<option selected="selected" value="01">B2C</option>
								<option value="02">B2B</option>
								<option value="03">B2C和B2B</option>
								<#elseif proobject.channeltype=='02'>
								<option value="01">B2C</option>
								<option selected="selected" value="02">B2B</option>
								<option value="03">B2C和B2B</option>
								<#elseif proobject.channeltype=='03'>
								<option value="01">B2C</option>
								<option value="02">B2B</option>
								<option selected="selected" value="03">B2C和B2B</option>
								</#if>
							</select>
						</td>
					</tr>
					<tr><td></td></tr>
					<tr>
						<td><label for="exampleInputName">上传图片</label></td>
						<td>
							<img height="60" width="80" alt="暂无图片" src="${proobject.logo }">
						</td>
						<td colspan="2">
							<input class="form-control " type="file"  name="file" >
						</td>
					</tr>
					<tr><td></td></tr>
					<tr>
						<td><label for="exampleInputName">公司介绍</label></td>
					</tr>
					<tr>
						<td colspan="6"><textarea class="form-control" rows="3"  name="companyintroduce">${proobject.companyintroduce}</textarea></td>
					</tr>
				  </table>
	  		   <div class="row">
			     <div class="col-md-12">
			   	  <div class="panel panel-default m-bottom-5">
			   	   <div class="panel-heading padding-5-5">供应商承保配置</div>
			   		<div class="panel-body">
					  <table>
						  <tr>
							<td><label for="exampleInputName">报价所需时间</label></td>
							<td><input class="form-control " type="text" value="${proobject.quotationtime!'' }" name="quotationtime" placeholder=""></td>
							<td><label for="exampleInputName">分钟</label></td>
						  </tr>
						  <tr>
							<td><label for="exampleInputName">两次报价间隔时间</label></td>
							<td><input class="form-control " type="text" value="${proobject.quotationinterval!'' }" name="quotationinterval" placeholder=""></td>
							<td><label for="exampleInputName">分钟</label></td>
						  </tr>
						  <tr>
							<td><label for="exampleInputName">核保所需时间</label></td>
							<td><input class="form-control " type="text" value="${proobject.insuretime!'' }" name="insuretime" placeholder=""></td>
							<td><label for="exampleInputName">分钟</label></td>
						  </tr>
						  <tr>
							<td><label for="exampleInputName">报价有效周期</label></td>
							<td><input class="form-control " type="text" value="${proobject.quotationvalidity!'' }" name="quotationvalidity" placeholder=""></td>
							<td><label for="exampleInputName">天</label></td>
						  </tr>
						  <tr>
							<td><label for="exampleInputName">支付号有效期</label></td>
							<td><input class="form-control " type="text" value="${proobject.payvalidity!'' }" name="payvalidity" placeholder=""></td>
							<td><label for="exampleInputName">天</label></td>
						  </tr>
						  <tr>
							<td><input type="checkbox"  <#if "${proobject.quickinsureflag}" == "1" >checked="checked" </#if> name="quickinsureflag" value="1"></td>
							<td colspan="2"><label for="exampleInputName">启动快速续保</label></td>
						  </tr>
					  </table>
			   		 </div>
			    	</div>
			   	 </div>
				</div>
		   		 </div>
		    	</div>
		    </div>
		    <div>
			 <input id="saveOrUpdatePro" class="btn btn-primary" type="submit" style="margin-left:50px;" value="保存">
			 <input class="btn btn-primary" type="button" style="margin-left:50px;"  onclick="javascript:history.go(-1)" value="取消"/>
		    </div>	
		</div>			
		</form>
	</div>
	
<div id="showpic" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="gridSystemModalLabel">选择供应商</h4>
      </div>
      <div class="modal-body">
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

<div class="modal fade" id="myModal_rule_add" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true" >
		<div class="modal-dialog" style="width: 70%">
			<div class="modal-content" id="modal-content">
				
				<div class="modal-header">
					<h4 class="modal-title">关联规则</h4>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-md-12">
						<form >
							<table class="table table-bordered ">
								<tr>
									<td class="col-md-2">规则名称：</td>
									<td class="col-md-4">
										<input class="form-control" type="text" id="rule_name" >
									</td>
									<td class="col-md-2">规则描述：</td>
									<td class="col-md-4" >
										<input class="form-control" type="text" id="rule_postil" >
									</td>
								</tr>
								<tr>
									<td colspan="4">
										<button id="querybutton" type="button"
											class="btn btn-primary">查询</button>
										<button id="resetbutton" type="button" 
											class="btn btn-primary">重置</button>
									</td>
								</tr>
							</table>
							</form>
							<table id="taskset_rulebase_list"></table>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" id="close_modal">关闭</button>
				</div>
				
			</div>
		</div>	
</div>
</body>
</html>