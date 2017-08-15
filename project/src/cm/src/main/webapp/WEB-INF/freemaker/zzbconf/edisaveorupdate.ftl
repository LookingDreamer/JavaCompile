<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>EDI配置管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/system/flat-ui-icon.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/ediconf" ]);
</script>
<body>
	<div class="container-fluid">
	  <form class="form-inline" role="form"  action="saveorupdate" method="post" enctype="multipart/form-data">
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">新增EDI处理器</div>
		   <div class="row">
		  	<div class="panel-body">
				 <table class="table table-bordered">
					  <tr>
						<td class="col-md-2 active"><label for="exampleInputName">名称</label></td>
						<td class="col-md-10">
							<input class="form-control" type="text" id="ediname" name="ediname">
						</td>
					  </tr>
<!-- 					  <tr> -->
<!-- 						<td class="col-md-2 active"><label for="exampleInputName">上传EDI配置</label> -->
<!-- 	                		<input type="hidden" id="filetype" value="elfconftype" name="filetype"> -->
<!-- 	             		</td> -->
<!-- 	             		<td class="col-md-10"><input id="fileimg" name="file" type="file" ></td> -->
<!-- 					  </tr> -->
<!-- 					  <tr> -->
<!-- 						<td  style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">部署地址</label></td> -->
<!-- 						<td ><input class="form-control" type="text" id="deployaddress" name="deployaddress" ></td> -->
<!-- 					  </tr> -->
					   <tr>
						<td  style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">接口地址</label></td>
						<td ><input class="form-control" type="text" id="interfaceaddress" name="interfaceaddress" ></td>
					  </tr>
					  <tr>
						<td  style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">运维负责人</label></td>
						<td ><input class="form-control" type="text" id="operationsdirector" name="operationsdirector" ></td>
					  </tr>
					  <tr>
						<td  style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">联系方式</label></td>
						<td ><input class="form-control" type="text" id="contactway" name="contactway" ></td>
					  </tr>
					  <tr>
						<td class="col-md-2 active" style="vertical-align: middle;"><label for="exampleInputName">说明</label></td>
						<td ><textarea style="resize:none; width:700px;" id="explains"  name="explains" class="form-control" rows="3"></textarea></td>
					  </tr>
<!-- 					  <tr> -->
<!-- 						<td class="col-md-2 active"><label for="exampleInputName">能力配置</label></td> -->
<!-- 						<td > -->
<!-- 							<#list capacityconf as capconf> -->
<%-- 								<input name="capacityconf" type="checkbox" value="${capconf.codevalue }"> --%>
<%-- 								${capconf.codename!'' }&nbsp;&nbsp; --%>
<!-- 							</#list> -->
<!-- 						</td> -->
<!-- 					 </tr> -->
				  </table>
			</div>
		  </div>
		  
		  
<!--   		   <div class="row"> -->
<!-- 		  	<div class="panel-body"> -->
<!-- 				 <table class="table table-bordered"> -->
<!-- 			      <tr> -->
<!-- 			      	<td class="col-md-2 active"  rowspan="2" style="vertical-align: middle;"><label for="exampleInputName">保险公司</label></td> -->
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
<!-- 				        <div class="container-fluid"> -->
<!-- 				          <div class="row"> -->
<!-- 							<ul id="leftselect" > -->
<!-- 							</ul> -->
<!-- 				          </div> -->
<!-- 				        </div> -->
<!-- 				      </div> -->
<!-- 			        </td> -->
<!-- 			        <td> -->
<!-- 						 <div class="modal-body" style="height:150px;overflow:auto;"> -->
<!-- 					        <div class="container-fluid"> -->
<!-- 					          <div class="row"> -->
<!-- 								<ul id="treeDemo" class="ztree"></ul> -->
<!-- 					          </div> -->
<!-- 					        </div> -->
<!-- 				         </div> -->
<!-- 			        </td> -->
<!-- 			      </tr> -->
<!-- 			      <tr> -->
<!-- 			      	<td colspan="3"><input class="btn btn-primary" id="edisaveform" type="submit" value="保存"></td> -->
<!-- 			      </tr> -->
<!-- 			    </table> -->
<!-- 			</div> -->
<!-- 		  </div> -->
		  
		  
<!-- 		   <div class="row"> -->
<!-- 		  	<div class="panel-body"> -->
<!-- 				 <table class="table table-bordered"> -->
<!-- 					  <tr > -->
<!-- 					  	<td rowspan="6" class="col-md-2 active" style="vertical-align: middle;"><label for="exampleInputName">能力描述</label></td> -->
<!-- 					  	<td style="vertical-align: middle;"><label for="exampleInputName"><input readonly="readonly" style="border: 0px;outline: none;" type="text" value="执行精确报价" name="insbedidescription[0].name"></label></td> -->
<!-- 					  	<td><input class="form-control" type="text" id="" name="insbedidescription[0].ippath"></td> -->
<!-- 				  	  </tr> -->
<!-- 					  <tr> -->
<!-- 					  	<td style="vertical-align: middle;"><label for="exampleInputName"><input readonly="readonly" style="border: 0px;outline: none;" type="text" value="提交报后再出单系统暂存" name="insbedidescription[1].name"></label></td> -->
<!-- 					  	<td><input class="form-control" type="text" id="" name="insbedidescription[1].ippath"></td> -->
<!-- 				  	  </tr> -->
<!-- 					  <tr> -->
<!-- 					  	<td style="vertical-align: middle;"><label for="exampleInputName"><input readonly="readonly" style="border: 0px;outline: none;" type="text" value="查询报价结果" name="insbedidescription[2].name"></label></td> -->
<!-- 					  	<td><input class="form-control" type="text" id="" name="insbedidescription[2].ippath"></td> -->
<!-- 				  	  </tr> -->
<!-- 					  <tr> -->
<!-- 					  	<td style="vertical-align: middle;"><label for="exampleInputName"><input readonly="readonly" style="border: 0px;outline: none;" type="text" value="提交核保" name="insbedidescription[3].name"></label></td> -->
<!-- 					  	<td><input class="form-control" type="text" id="" name="insbedidescription[3].ippath"></td> -->
<!-- 				  	  </tr> -->
<!-- 					  <tr> -->
<!-- 					  	<td style="vertical-align: middle;"><label for="exampleInputName"><input readonly="readonly" style="border: 0px;outline: none;" type="text" value="查询核保结果" name="insbedidescription[4].name"></label></td> -->
<!-- 					  	<td><input class="form-control" type="text" id="" name="insbedidescription[4].ippath"></td> -->
<!-- 				  	  </tr> -->
<!-- 					  <tr> -->
<!-- 					  	<td style="vertical-align: middle;"><label for="exampleInputName"><input readonly="readonly" style="border: 0px;outline: none;" type="text" value="查询承保结果" name="insbedidescription[5].name"></label></td> -->
<!-- 					  	<td><input class="form-control" type="text" id="" name="insbedidescription[5].ippath"></td> -->
<!-- 				  	  </tr> -->
<!-- 				  	  <tr>  -->
			      		<td colspan="3">
			      			<input class="btn btn-primary" id="edisaveform" type="submit" value="保存">&nbsp;&nbsp;&nbsp;&nbsp;
			      			<input class="btn btn-primary" value="返回" type="button" id="backbutton">
			      		</td>
<!--  			     	 </tr> -->
<!-- 				  </table> -->
<!-- 			</div> -->
<!-- 		  </div> -->
		</div>
	  </form>
	</div>
</body>
</html>
