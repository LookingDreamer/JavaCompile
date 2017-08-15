<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>精灵配置管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/system/flat-ui-icon.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/elfconf" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">添加精灵配置</div>
		   <div class="row">
		  	<div class="panel-body">
	  			<form class="form-inline" role="form" action="saveorupdateelfconf" method="post" >
				 <table class="table table-bordered">
					  <tr>
						<td  style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">名称</label></td>
						<td>
							<input class="form-control" type="text" id="elfname" name="elfname" >
						</td>
					  </tr>
					  <tr>
						<td  style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">能力配置</label></td>
						<td >
							<#list capacityconf as capconf>
								${capconf.codename!'' }
								<input id="capconf${capconf_index }" name="capacityconf" type="checkbox" value="${capconf.codevalue }">
								&nbsp;&nbsp;
							</#list>
						</td>
					 </tr>
					  <tr>
						<td  style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">精灵类型</label></td>
						<td >
							半自动类型:
							&nbsp;&nbsp;<input  type="radio"  name="elftype" value="1">&nbsp;半自动类型<br>
							全自动类型:
							&nbsp;&nbsp;<input  type="radio"  name="elftype" value="2">&nbsp;统一接口
							&nbsp;&nbsp;<input  type="radio"  name="elftype" value="3">&nbsp;非统一接口
						</td>
					 </tr>
					  <tr>
						<td  style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">接口路径</label></td>
						<td class="col-md-10">
							<input class="form-control" type="text" id="elfpath" name="elfpath">
						</td>
					 </tr>
					 <tr>
					 	<td style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">关联供应商</label></td>
					 	<td>
							<input type="hidden" id="proid" name="proid"  >
						 	<input type="text"	class="form-control " id="prvname"  placeholder="请选择" >
						</td>
					 </tr>
<!-- 					  <tr> -->
<!-- 	             		<td class="col-md-2 active"><label for="exampleInputName">上传精灵配置</label> -->
<!-- 	                		<input type="hidden" id="filetype" value="elfconftype" name="filetype"> -->
<!-- 	             		</td> -->
<!-- 	             		<td><input id="fileimg0" name="file" type="file" ></td> -->
<!-- 	            	  </tr> -->
				  </table>
  				   <div class="row" id="skillsdiv" style="display: none;">
				  	<div class="panel-body">
				  	<div class="panel panel-default m-bottom-5">
					 	 <div class="panel-heading padding-5-5">技能</div>
						 <table class="table table-bordered">
					      <tr>
					      	<td style="vertical-align: middle;"><label for="exampleInputName" >技能名</label></td>
					      	<td colspan="3"><input class="form-control"  name="skillname"></td>
					      </tr>
					      <tr>
					      	<td style="vertical-align: middle;"><label for="exampleInputName" >输入项</label></td>
					        <td>
					           <div class="modal-body" style="width:300px;height:150px;overflow:auto;">
						        <div class="container-fluid">
						          <div class="row">
									<ul id="leftselectinput" class="mulFindList">
										<#list inputItem as item>
											<li >${item.codename }<input type="hidden"  value="${item.codevalue }"></li>
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
											<li>${item.codename } <input type="hidden"  value="${item.codevalue }"></li>
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
				  
				  
				  
				  
				  <div>
				  	<input class="btn btn-primary" type="submit" value="保存" id="elfconfsaveform">&nbsp;&nbsp;&nbsp;&nbsp;
				    <input class="btn btn-primary" value="返回" type="button" id="backbutton">
				  </div>
				</form>
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
</body>
</html>
