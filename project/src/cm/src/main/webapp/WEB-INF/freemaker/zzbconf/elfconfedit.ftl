<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>精灵配置管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/system/flat-ui-icon.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/elfconfedit" , "lib/tsearch" ]);
</script>
<body>
	<div class="container-fluid">
	  <form class="form-inline" role="form"  action="saveorupdateelfconf" method="post" >
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">查看并修改精灵配置</div>
		  	<div class="panel-body">
		   		<div class="row">
				  <div class="col-md-12">
				  <table class="table table-bordered">
					  <tr>
						<td style="vertical-align: middle;"  class="col-md-2 active"><label for="exampleInputName">名称</label></td>
						<td class="col-md-10" colspan="3" >
							<input class="form-control" type="text" id="elfname" name="elfname" value="${elfconf.elfname!'' }">
							<input type="hidden" id = "elfconfid" name="elfid" value="${elfconf.id!'' }">
						</td>
					  </tr>
					 <tr>
						<td  style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">能力配置</label></td>
						<td class="col-md-10">
							<#list capacityconf as capconf>
								${capconf.codename!'' }
								<input id="capconf${capconf_index }" name="capacityconf" type="checkbox" value="${capconf.codevalue }" <#if "${elfconf.capacityconf }"?contains("${capconf.codevalue}") >checked="checked" </#if> >
								&nbsp;&nbsp;
							</#list>
						</td>
					 </tr>
					 <tr>
						<td  style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">精灵类型</label></td>
						
						<td class="col-md-10">
							半自动类型:
							&nbsp;&nbsp;<input  type="radio"  name="elftype" value="1" <#if "${elfconf.elftype }" == "1"> checked="checked" </#if> >&nbsp;半自动类型<br>
							全自动类型:
							&nbsp;&nbsp;<input  type="radio"  name="elftype" value="2" <#if "${elfconf.elftype }" == "2"> checked="checked" </#if> >&nbsp;统一接口
							&nbsp;&nbsp;<input  type="radio"  name="elftype" value="3" <#if "${elfconf.elftype }" == "3"> checked="checked" </#if> >&nbsp;非统一接口
						</td>
					 </tr>
<!-- 					 <tr> -->
<!-- 						<td  style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">接口路径</label></td> -->
<!-- 						<td class="col-md-10"><label for="exampleInputName"> ${elfconf.elfpath!'' }"</label></td> -->
<!-- 					 </tr> -->
					 <tr>
						<td  style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">接口路径</label></td>
						<td class="col-md-10">
							<input class="form-control" type="text" id="elfpath" name="elfpath" value="${elfconf.elfpath!'' }">
						</td>
					 </tr>
					 <tr>
					 	<td style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">关联供应商</label></td>
					 	<td class="col-md-10">
							<input type="hidden" id="proid" name="proid" value="${elfconf.proid }" >
						 	<input type="text"	class="form-control " id="prvname" value="${provider.prvname }" placeholder="请选择" >
						</td>
					 </tr>
<!-- 					 <tr> -->
<!-- 	             		<td class="col-md-2 active"><label for="exampleInputName">上传精灵配置</label> -->
<!-- 	                		<input type="hidden" id="filetype" value="elfconftype" name="filetype"> -->
<!-- 	             		</td> -->
<!-- 	             		<td class="col-md-10"><input id="fileimg0" name="file" type="file" ></td> -->
<!-- 	            	 </tr> -->
				  </table>
				   <div class="row" id="skillsdiv" style="display: none;">
				  	<div class="panel-body">
				 	  <div class="panel panel-default m-bottom-5">
					 	 <div class="panel-heading padding-5-5">技能</div>
						 <table class="table table-bordered">
					      <tr>
					      	<td style="vertical-align: middle;"><label for="exampleInputName" >技能名</label></td>
					      	<td colspan="3"><input class="form-control"  name="skillname" value="${sbskill }"></td>
					      </tr>
					      <tr>
					      	<td style="vertical-align: middle;"><label for="exampleInputName" >输入项</label></td>
					        <td>
					           <div class="modal-body" style="width:300px;height:150px;overflow:auto;">
						        <div class="container-fluid">
						          <div class="row">
									<ul id="leftselectinput" class="mulFindList">
										<#list inputItem as item>
											<#if "${filterin }"?contains("${item.codevalue }")>
											<#else>
												<li >${item.codename }<input type="hidden"  value="${item.codevalue }"></li>
											</#if>
										</#list>
									</ul>
						          </div>
						        </div>
						      </div>
					        </td>
					         <td style="vertical-align: middle;"> 
                                <a href="#" class="tabNewAdd">&nbsp;添加 &gt;</a><br><br>
                                <a href="#" class="tabNewRemove">&nbsp;&lt; 移除</a><br><br>
                                <a href="#" class="tabNewAll">&nbsp;全选/全不选</a>
                             </td>
					        <td>
					     		 <div class="modal-body" style="width:300px;height:150px;overflow:auto;">
							        <div class="container-fluid">
							          <div class="row">
										<ul id="rightselectinput" class="mulFindList">
											<#list skillInputList as item>
												<li>${item.skillname } <input type="hidden" name="inputcode" value="${item.inputcode }"></li>
											</#list>
										</ul>
							          </div>
							        </div>
						         </div>
					        </td>
					      </tr>
					      <tr>
					      	<td style="vertical-align: middle;"><label for="exampleInputName" >输出项</label></td>
					        <td>
					           <div class="modal-body" style="width:300px;height:150px;overflow:auto;">
						        <div class="container-fluid" >
						          <div class="row">
									<ul id="leftselectoutput" class="mulFindListout">
										<#list outputItem as item>
											<#if "${filterout }"?contains("${item.codevalue }")>
											<#else>
												<li>${item.codename } <input type="hidden"  value="${item.codevalue }"></li>
											</#if>
										</#list>
									</ul>
						          </div>
						        </div>
						      </div>
					        </td>
					         <td style="vertical-align: middle;"> 
                                <a href="#" class="tabNewAddout">&nbsp;添加 &gt;</a><br><br>
                                <a href="#" class="tabNewRemoveout">&nbsp;&lt; 移除</a><br><br>
                                <a href="#" class="tabNewAllout">&nbsp;全选/全不选</a>
                             </td>
					        <td>
					     		 <div class="modal-body" style="width:300px;height:150px;overflow:auto;">
							        <div class="container-fluid">
							          <div class="row">
										<ul id="rightselectoutput" class="mulFindListout">
											<#list skillOutputList as item>
												<li>${item.skillname }<input type="hidden" name="outputcode" value="${item.outputcode }"> </li>
											</#list>
										</ul>
							          </div>
							        </div>
						         </div>
					        </td>
					      </tr>
					    </table>
					</div>
					</div>
				  </div>
				  
				  <div style="margin-top:1px;padding:10px;"><input class="btn btn-primary" type="submit" value="保存" id="elfconfsaveform">&nbsp;
				  	   <input class="btn btn-primary" value="返回" type="button" id="backbutton">
				  </div>
				  
		  <div class="panel panel-default m-bottom-5">
		   <div class="panel-body">
			  <div class="row">
			    <div class="col-md-12">
				  <table>
				  	<tr>
	             		<td style="vertical-align: middle;" class="col-md-2">选择能力</td>
	             		<td class="col-md-2">
	             		    <input type="hidden" id="confid"   >
	             			<input type="hidden" id="providerid"   >
				 			<input type="text"	class="form-control " id="prvautoname"  placeholder="请选择供应商" >
			 			</td>
			 			<td class="col-md-2">
				 			<input type="hidden" id="deptid"  >
				 			<input type="text"	class="form-control " id="deptname" placeholder="请选择机构" >
			 			</td>
			 			<td class="col-md-6">
				 			<#list conftype as conftype>
								${conftype.codename!'' }
								<input  id="ability${conftype.codevalue }" name="conftype" type="checkbox" value="${conftype.codevalue }" >
							</#list>
	             		</td>
	             		<td style="padding-right: 1px"><input id="addability" type="button" class="btn btn-primary" value="添加"></td>
	             		<td style="padding-right: 1px"><input id="delabilitybyid" type="button" class="btn btn-primary" value="删除"></td>
	             		<td style="padding-right: 1px"><input id="queryautoconfig" type="button" class="btn btn-primary" value="查询"></td>
	             		<td style="padding-right: 1px"><input id="resetbutton" type="button" class="btn btn-primary" value="重置"></td>
	            	 </tr>
				 	 </table>
				 	 </div>
		 		 	 </div>
		 		 	 </div>
				  </div>
				   <div class="panel panel-default m-bottom-5">
					  <div class="panel-heading padding-5-5">能力列表</div>
					   <div class="row">
						<div class="col-md-12">
							<table id="table-ability"></table>
						</div>
			 		  </div>
		 		   </div>
				  <#-- <div><input class="btn btn-primary" type="submit" value="保存" id="elfconfsaveform">&nbsp;&nbsp;&nbsp;&nbsp;
				  	   <input class="btn btn-primary" value="返回" type="button" id="backbutton">
				  </div> -->
				</div>
			</div>
		  </div>
		</div>
	</form>
 </div>
 <div id="showpic" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="gridSystemModalLabel">选择供应商</h4><br/>
		<div><input class="form-control ztree-search" id = "treesearch" data-bind="treeDemo" name="treesearch" placeholder="输入供应商名称进行搜索" /></div>
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
 <div id="showautopic" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="gridSystemModalLabel">选择供应商</h4><br/>
		<div><input class="form-control ztree-search" id = "treesearch1" data-bind="treeautoDemo" name="treesearch1" placeholder="输入供应商名称进行搜索" /></div>
      </div>
      <div class="modal-body">
        <div class="container-fluid">
          <div class="row">
			<ul id="treeautoDemo" class="ztree"></ul>
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
        <h4 class="modal-title" id="gridSystemModalLabel">选择机构</h4><br/>
		<div><input class="form-control ztree-search" id = "treesearchDept" data-bind="treeDemoDept" name="treesearchDept" placeholder="输入机构名称进行搜索" /></div>
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
