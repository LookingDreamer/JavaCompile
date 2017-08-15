<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>EDI配置管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/system/flat-ui-icon.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/ediconfedit" , "lib/tsearch"]);
</script>
<body>
	<div class="container-fluid">
	  <form class="form-inline" role="form" id="usersaveform" action="saveorupdate" method="post" >
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">新增EDI处理器</div>
		   	  <div class="row">
				<div class="col-md-12"> 
				 <table class="table table-bordered">
					  <tr>	
						<td  style="vertical-align: middle;" align="right" class="col-md-2"><label for="exampleInputName">名称:</label></td>
						<td colspan="3" class="col-md-12">
							<input class="form-control" type="text" id="ediname" name="ediname" value="${ediconf.ediname!'' }">
							<input  type="hidden" id="ediconfid"  name="ediconfid" value="${ediconf.id!'' }">
						</td>
<!-- 						<td style="vertical-align: middle;" align="right" class="col-md-2"><label for="exampleInputName">上传EDI配置</label> -->
<!-- 	                		<input type="hidden" id="filetype" value="elfconftype" name="filetype"> -->
<!-- 	             		</td> -->
<!-- 	             		<td class="col-md-4"><input id="fileimg" name="file" type="file" ></td> -->
					  </tr>
<!-- 					  <tr> -->
<!-- 						<td style="vertical-align: middle;" align="right" class="col-md-2"><label for="exampleInputName">配置存放路径</label> -->
<!-- 	                		<input type="hidden" id="filetype" value="elfconftype" name="filetype"> -->
<!-- 	             		</td> -->
<!-- 	             		<td class="col-md-4" colspan="3"><label for="exampleInputName">${ediconf.edipath!'' }</label></td> -->
<!-- 					  </tr> -->
					  <tr>
						<td  style="vertical-align: middle;" align="right" class="col-md-2"><label for="exampleInputName">部署地址:</label></td>
						<td class="col-md-4"><input class="form-control" type="text" id="deployaddress" name="deployaddress" value="${ediconf.deployaddress!'' }"></td>
						<td  style="vertical-align: middle;" align="right" class="col-md-2"><label for="exampleInputName">接口地址:</label></td>
						<td class="col-md-4"><input class="form-control" type="text" id="interfaceaddress" name="interfaceaddress" value="${ediconf.interfaceaddress!'' }"></td>
					  </tr>
					  <tr>
						<td  style="vertical-align: middle;" align="right" class="col-md-2"><label for="exampleInputName">运维负责人:</label></td>
						<td class="col-md-4"><input class="form-control" type="text" id="operationsdirector" name="operationsdirector" value="${ediconf.operationsdirector!'' }"></td>
						<td  style="vertical-align: middle;" align="right" class="col-md-2"><label for="exampleInputName">联系方式:</label></td>
						<td class="col-md-4"><input class="form-control" type="text" id="contactway" name="contactway" value="${ediconf.contactway!'' }"></td>
					  </tr>
					  <tr>
						<td  style="vertical-align: middle;" align="right" class="col-md-2"><label for="exampleInputName">说明</label></td>
						<td colspan="3" class="col-md-10"><textarea id="explains" style="resize:none; width:780px;"  name="explains" class="form-control"  rows="3" >${ediconf.explains!'' }</textarea></td>
					  </tr>
				  </table>
			  </div>
			</div>
		  
<!--   		   <div class="row"> -->
<!-- 		  	<div class="panel-body"> -->
<!-- 				 <table class="table table-bordered"> -->
<!-- 			      <tr> -->
<!-- 			      	<td style="vertical-align: middle;" class="col-md-2 active"  rowspan="2"><label for="exampleInputName">保险公司</label></td> -->
<!-- 			        <td> -->
<!-- 			          	<label for="exampleInputName">已选择的保险公司</label> -->
<!-- 			        </td> -->
<!-- 			        <td> -->
<!-- 			         	<label for="exampleInputName">可选择的保险公司</label> -->
<!-- 			        </td> -->
<!-- 			      </tr> -->
<!-- 			      <tr> -->
<!-- 			        <td style="height:150px;overflow:auto;"> -->
<!-- 			           <div class="modal-body" > -->
<!-- 				        <div class="container-fluid" > -->
<!-- 				          <div class="row"> -->
<!-- 							<ul id="leftselect" > -->
<!-- 								<#assign sum = prolist?size > -->
<!-- 								<input type="hidden" id="hiddensum"  value="${sum }"> -->
								
<!-- 								<#list prolist as list> -->
<!-- 										<li ondblclick="f1(${list.prvcode})" id="${list.prvcode}"><input type="hidden" name="insbproviderandedi[${list_index }].providerid"   value="${list.id}">${list.prvname}</li> -->
<!-- 								</#list> -->
<!-- 							</ul> -->
<!-- 				          </div> -->
<!-- 				        </div> -->
<!-- 				      </div> -->
<!-- 			        </td> -->
<!-- 			        <td> -->
<!-- 			     		 <div class="modal-body" style="height:150px;overflow:auto;"> -->
<!-- 					        <div class="container-fluid" > -->
<!-- 					          <div class="row"> -->
<!-- 								<ul id="treeDemo" class="ztree"></ul> -->
<!-- 					          </div> -->
<!-- 					        </div> -->
<!-- 				         </div> -->
<!-- 			        </td> -->
<!-- 			      </tr> -->
                <div style="padding-bottom: 10px;padding-left: 5px;">
			      <tr>
			      	<td colspan="3"><input class="btn btn-primary" type="submit" value="保存"></td>
			      	<td colspan="3">&nbsp;<input class="btn btn-primary" type="button" value="返回" id="backbutton"></td>
			      </tr>
			    </div>
<!-- 			    </table> -->
<!-- 			</div> -->
<!-- 		  </div> -->
	   
<!-- 		   <div class="row"> -->
<!-- 		  	<div class="panel-body"> -->
<!-- 			 	<table class="table table-bordered"> -->
<!-- 					  <tr > -->
<!-- 					  	<td style="vertical-align: middle;" rowspan="7" class="col-md-2 active" ><label for="exampleInputName">能力描述</label></td> -->
<!-- 				  	  </tr> -->
<!-- 	 				<#list edidesc as edilist> -->
<!-- 					  <tr > -->
<!-- 					  	<td> -->
<!-- 					  		<label for="exampleInputName">${edilist.name }</label> -->
<!-- 					  		<input type="hidden" name="insbedidescription[${edilist_index }].name" value="${edilist.name }"> -->
<!-- 					  	</td> -->
<!-- 					  	<td><input class="form-control" type="text" id="eidname" name="insbedidescription[${edilist_index }].ippath"  value="${edilist.ippath }"></td> -->
<!-- 				  	  </tr> -->
<!-- 		  	  	 	</#list> -->
<!-- 		  	  	 	<tr>  -->
<!--  			      		<td colspan="3"> -->
<!--  			      			<input class="btn btn-primary" type="submit" value="保存">&nbsp;&nbsp;&nbsp;&nbsp; -->
<!--  			      			<input class="btn btn-primary" type="submit" id="backbutton" value="返回"> -->
<!--  			      		</td> -->
<!-- 			         </tr>  -->
<!-- 			  </table> -->
<!-- 			</div> -->
<!-- 		  </div> -->
		  
	     <div class="panel panel-default m-bottom-5">
		    <div class="panel-body">
			  <div class="row">
			    <div class="col-md-12">
				  <table>
				  	<tr>
	             		<td style="vertical-align: middle;" class="col-md-2">能力维护</td>
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
								<input id="ability${conftype.codevalue }" name="conftype" type="checkbox" value="${conftype.codevalue }">
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
				  <div class="panel-heading padding-5-5">能力配置</div>
				   <div class="row">
					<div class="col-md-12">
						<table id="table-ability"></table>
					</div>
		 		  </div>
	 		   </div>

		</div>
	</form>
 </div>
 <div id="showautopic" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="gridSystemModalLabel">选择供应商</h4><br/>
		<div><input class="form-control ztree-search" id = "treeDemosearch" data-bind="treeautoDemo" name="treeDemosearch" placeholder="输入供应商名称进行搜索" /></div>
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
		<div><input class="form-control ztree-search" id = "treesearch" data-bind="treeDemoDept" name="treesearch" placeholder="输入机构名称进行搜索" /></div>
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
