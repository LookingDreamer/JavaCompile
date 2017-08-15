<!DOCTYPE html>
<html lang ="en" class="fuelux" >  
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>供应商管理</title>
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
	requirejs([ "zzbconf/provider" ]);
</script>
</head>
<body>

<!-- <div class="col-md-12">
 <div>
	<div class="panel panel-default m-bottom-5">
	 <div class="panel-heading padding-5-5">查询</div>
		  <div class="panel-body">
		    <div class="row">
			<div class="col-md-12">
				<form class="form-inline" role="form" id="prvform">
					<div class="form-group col-md-4">
						<label for="exampleInputCode">供应商编码</label> 
						<input type="text"	class="form-control " id="prvcode" name="prvcode" placeholder="">
						<input id="updateid" type="hidden" name="id" >
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputName">供应商名称</label> 
						<input type="text"	class="form-control " id="prvname" name="prvname" placeholder="">
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputOrgName">供应商层级</label> <select class="form-control " id="prvgrade" name="prvgrade">
						  <option>1</option>
						  <option>2</option>
						  <option>3</option>
						  <option>4</option>
						  <option>5</option>
						</select>
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputCode">供应商类型</label> 
						<select class="form-control " id="businesstype" name="businesstype">
						  <option>1</option>
						  <option>2</option>
						  <option>3</option>
						  <option>4</option>
						  <option>5</option>
						</select>
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputName">关联机构&nbsp;&nbsp;&nbsp;&nbsp;</label> 
						<input type="hidden" id="affiliationorgquery" name="affiliationorg" >
					 	<input type="text"	class="form-control " id="querydeptname"  placeholder="请选择" >
					</div>
				</form>
			</div>
		</div>
	  </div>
	<div class="panel-footer">
		<div style="float: left; margin-right: 5px">
			<button id="querypro" type="button" name="querypro"
								class="btn btn-primary">查询</button>
			<button  id="detailspro" type="button" name="detailspro"
								class="btn btn-primary">查看并修改</button>
			<button id="addpro" type="button" name="addpro"	class="btn btn-primary">新增</button>
		</div>
		<div style="float: left; margin-right: 5px">
			<form id = "deletbyid" action="deletbyid" method="post">
			 <input  type="hidden" id = "delid" name = "id"  >
			 <input id="delpro" type="submit" name="delpro" value="删除"  class="btn btn-primary">
			</form>
		</div>	
		<div>
			<button id="resetbutton" type="button" name="resetbutton" class="btn btn-primary">重置</button>
		</div>
	</div>
	</div>
	
 </div>
</div> -->

<input id="updateid" type="hidden" name="updateid" >
<input type="hidden" id="affiliationorgquery" name="affiliationorg" >

  <div>
	<div class="col-md-4">
	<div class="panel panel-default m-bottom-2" >
	<div class="panel-heading padding-5-5">供应商列表</div>
	<div class="panel-body"  >
	
	  <!-- <div style="margin-bottom: 3px;">
		<div style="float: left; margin-right: 5px">
			<button  id="detailspro" type="button" name="detailspro" class="btn btn-primary">查看并修改</button>
			<button id="addpro" type="button" name="addpro"	class="btn btn-primary">新增</button>
		</div>
		<div style="float: left; margin-right: 5px">
			<form id = "deletbyid" action="deletbyid" method="post">
			 <input  type="hidden" id = "delid" name = "delid"  >
			 <input id="delpro" type="submit" name="delpro" value="删除"  class="btn btn-primary">
			</form>
		</div>	
		<div>
			<button id="resetbutton1" type="button" name="resetbutton" class="btn btn-primary">重置</button>
		</div>
	</div> -->
	
	<!-- <ul class="tree tree-folder-select" role="tree" id="myTree"  style="width:100%; height:442px; overflow-y:auto;overflow-x:auto;">
	  <li class="tree-branch hide" data-template="treebranch" role="treeitem" aria-expanded="false">
	    <div class="tree-branch-header">
	      <button class="glyphicon icon-caret glyphicon-play"><span class="sr-only">Open</span></button>
	      <button class="tree-branch-name">
	        <span class="glyphicon icon-folder glyphicon-folder-close"></span>
	        <span class="tree-label"></span>
	      </button>
	    </div>
	    <ul class="tree-branch-children" role="group"></ul>
	    <div class="tree-loader" role="alert">Loading...</div>
	  </li>
	  <li class="tree-item hide" data-template="treeitem" role="treeitem">
	    <button class="tree-item-name">
	      <span class="glyphicon icon-item fueluxicon-bullet"></span>
	      <span class="tree-label"></span>
	    </button>
	  </li>
	</ul> -->
	  <div>
	    <div class="ztree" id="menuTree" style="width:100%; height:442px; overflow-y:auto;overflow-x:auto;"></div>
	  </div>
	</div>
	</div>
	</div>
	
	<div class="col-md-8">
	 <div class="panel panel-default m-bottom-2" >
	  <div class="panel-heading padding-5-5">详情</div>
	   <form id = "prosaveupdateform"  action="saveorupdatepro" method="post" enctype="multipart/form-data">
	   	<div class="alert alert-danger alert-dismissible" id="providererror"
				role="alert" style="display: none;"></div>
	   <div class="panel-body"   style="width:100%; height:425px; overflow-y:auto;overflow-x:auto;">
	   <div id="saveupdatediv" class="container-fluid" style="margin-bottom: 20px">
	    <div  class="row">
		     <div class="col-md-12">
				 <table class="table table-bordered ">
					<tr>
						<td><label for="exampleInputName">供应商代码</label></td>
						<td><input class="form-control " type="text" value="${proobject.prvcode!'' }" id="prvcode" name="prvcode" placeholder="" readonly="readonly" ></td>
						<td><label for="exampleInputName">供应商名称</label></td>
						<td><input class="form-control " type="text" value="${proobject.prvname }" id="prvname1" name="prvname" placeholder="" readonly="readonly"></td>
					</tr>
					<tr>
						<td><label for="exampleInputName">供应商简称</label></td>
						<td><input class="form-control " type="text" value="${proobject.prvshotname!'' }" id="prvshotname" name="prvshotname" placeholder="" readonly="readonly"></td>
						<td><label for="exampleInputName">上级供应商</label></td>
						<td>
						 <input type="hidden" id="parentcode" name="parentcode" value="${proobject.parentcode }">
						 <input type="text" class="form-control" id="parentname" value="${proobject.parentcode }" placeholder="请选择" readonly="readonly">
						</td>
					</tr>
					<tr>
						<td><label for="exampleInputName">供应商级别</label></td>
						<td>
							<select id="prvgrade" name="prvgrade" class="form-control " disabled="disabled">							
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
						<select class="form-control" class="form-control " id="prvtype" name="prvtype"  disabled="disabled">
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
					</tr>					
					<tr>
						<!--
						<td><label for="exampleInputName">关联规则</label></td>
						<td>
							<input type="hidden" id="permissionorg" name="permissionorg" value="${proobject.permissionorg }" >
						 	<input type="text"	class="form-control"  id="rulepostil" placeholder="请选择" value="${rule.rulePostil }">
						</td>
						<td><label for="exampleInputName">客服电话</label></td>
						<td><input class="form-control " type="text" value="${proobject.servictel!'' }" id="servictel" name="servictel"></td>
						-->
						<td><label for="exampleInputName">渠道类型</label></td>
						<td>
							<select  class="form-control " id="channeltype" name="channeltype">
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
						<td><label for="exampleInputName">排序权重</label></td>
						<td><input class="form-control " type="text" value="${proobject.orderflag!'' }" id="orderflag" name="orderflag" placeholder=""></td>
					</tr>
					<tr>
						<td><label for="exampleInputName">所属省</label></td>
						<td style="vertical-align: middle;">
							<select name="province" class="form-control" id ="province" onchange="changeprv()">
								<option value="" selected="selected">省份</option>
							</select>
						</td>
						<td><label for="exampleInputName">所属市</label></td>
						<td style="vertical-align: middle;">
							<select name="city" class="form-control" id ="city" onchange="">
								<option value="" selected="selected">市</option>
							</select>
						</td>
					</tr>
					<tr>
						<td><label for="exampleInputName">联系人</label></td>
						<td><input class="form-control " type="text" value="${proobject.linkname!'' }" id="linkname" name="linkname" placeholder=""></td>
						<td><label for="exampleInputName">联系方式</label></td>
						<td><input class="form-control " type="text" value="${proobject.linktel!'' }" id="linktel" name="linktel" placeholder=""></td>
					</tr>
					<tr>
						<td><label for="exampleInputName">地址</label></td>
						<td><input class="form-control " type="text" value="${proobject.address!'' }" id="address" name="address" placeholder=""></td>
						<td><label for="exampleInputName">网址</label></td>
						<td><input class="form-control " type="text" value="${proobject.prvurl!'' }" id="prvurl" name="prvurl" placeholder=""></td>
					</tr>
					<tr>
						<td><label for="exampleInputName">业务类型</label></td>
						<td colspan="3">
						<label class="radio-inline">
						  <input type="checkbox" id="businesstype01" name="businesstype"  value="01"  <#if "${proobject.businesstype}"?contains("01") >checked="checked"</#if>> 传统
						</label>
						<label class="radio-inline">
						  <input type="checkbox" id="businesstype02" name="businesstype"  value="02" <#if  "${proobject.businesstype}"?contains("02") >checked="checked"</#if>> 网销
						</label>
						<label class="radio-inline">
						  <input type="checkbox" id="businesstype03" name="businesstype"  value="03" <#if  "${proobject.businesstype}"?contains("03") >checked="checked"</#if>> 电销 
						</label>
						</td>
					</tr>
					<tr>
						<td><label for="exampleInputName">上传图片</label></td>
						<td>
							<img id="logo"  height="60" width="80" alt="暂无图片" src="${proobject.logo }">
						</td>
						<td colspan="2">
							<input id="uploadfileid" type="file" name="file" />
							<input id="uploadbutton" class="btn btn-primary" type="button" name="uploadbutton" value="上传" />
						</td>
					</tr>
					<tr>
						<td><label for="exampleInputName">公司介绍</label></td>
						<td colspan="3"><textarea class="form-control" rows="3" id="companyintroduce" name="companyintroduce">${proobject.companyintroduce}</textarea></td>
					</tr>
				  </table>
		 <div class="row">
		     <div class="col-md-12">
		   	  <div class="panel panel-default m-bottom-5">
		   	   <div class="panel-heading padding-5-5">供应商承保配置</div>
		   		<div class="panel-body">
				  <table>
					  <tr>
						<td><label for="exampleInputName">EDI有效时间</label></td>
						<td><input class="form-control " type="text" value="${proobject.quotationtime!'' }" id="quotationtime" name="quotationtime" placeholder=""></td>
						<td><label for="exampleInputName">分钟</label></td>
					  </tr>
					  <tr>
						<td><label for="exampleInputName">精灵报价时间</label></td>
						<td><input class="form-control " type="text" value="${proobject.quotationinterval!'' }" id="quotationinterval" name="quotationinterval" placeholder=""></td>
						<td><label for="exampleInputName">分钟</label></td>
					  </tr>
					  <tr>
						<td><label for="exampleInputName">核保所需时间</label></td>
						<td><input class="form-control " type="text" value="${proobject.insuretime!'' }" id="insuretime" name="insuretime" placeholder=""></td>
						<td><label for="exampleInputName">分钟</label></td>
					  </tr>
					  <tr>
						<td><label for="exampleInputName">报价有效周期</label></td>
						<td><input class="form-control " type="text" value="${proobject.quotationvalidity!'' }" id="quotationvalidity" name="quotationvalidity" placeholder=""></td>
						<td><label for="exampleInputName">天</label></td>
					  </tr>
					  <tr>
						<td><label for="exampleInputName">支付号有效期</label></td>
						<td><input class="form-control " type="text" value="${proobject.payvalidity!'' }" id="payvalidity" name="payvalidity" placeholder=""></td>
						<td><label for="exampleInputName">天</label></td>
					  </tr>
					  <tr>
						<td><label for="exampleInputName">允许提前报价</label></td>
						<td><input class="form-control " type="text" value="${proobject.advancequote!'' }" id="advancequote" name="advancequote" placeholder=""></td>
						<td><label for="exampleInputName">天</label></td>
					  </tr>
					  <tr>
						<td><input type="checkbox" id="quickinsureflag"  name="quickinsureflag" value="1"  <#if "${proobject.quickinsureflag}" == "1" >checked="" </#if>></td>
						<td colspan="2"><label for="exampleInputName">启动快速续保</label></td>
					  </tr>
				  </table>
		   		 </div>
		    	</div>
		    </div>
		</div>
		          <input  type="hidden" id="saveupdateid" name="id" value="${proobject.id!'' }">
		    </div>
		</div>			
	    </div>
	   </div>
		    <div style="margin-bottom: 5px;margin-left: 15px;">
			 <input id="saveOrUpdatePro" class="btn btn-primary" type="button" style="margin-left:15px;" value="保存">
			 <!-- <button id="resetbutton" type="button" style="margin-left:5px" name="resetbutton" class="btn btn-primary">重置</button>
			 <button id="delpro" type="button" name="delpro" class="btn btn-primary">删除</button> -->
		    </div>
		</form>
	   <!-- <div class="panel-footer"  style="height:200%;">
	   </div>
	   </form> -->
	  </div>
	</div>
	
  </div>


<div id="showquerydeptpic" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="gridSystemModalLabel">选择机构</h4>
      </div>
      <div class="modal-body">
        <div class="container-fluid">
          <div class="row">
			<ul id="treequeryDept" class="ztree"></ul>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
    </div>
  </div>
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

<!-- <div class="modal fade" id="myModal_rule_add" tabindex="-1" role="dialog"
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
 -->
</body>
</html>
