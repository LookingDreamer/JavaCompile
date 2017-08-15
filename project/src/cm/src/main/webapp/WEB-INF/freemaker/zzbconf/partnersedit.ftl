<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8"/>
<title>查看并修改合作商</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${staticRoot}/css/appmain.css" rel="stylesheet">
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/skin/default.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/partners" ]);
</script>
</head>
<body>
    
<div id="saveupdatediv" class="container-fluid" style="margin-bottom: 20px">
	 <form id = "prosaveupdateform"  action="saveorupdatepar" method="post" enctype="multipart/form-data">
	    <div  class="row">
		     <div class="col-md-12">
		   	  <div class="panel panel-default m-bottom-5">
		   	   <div class="panel-heading padding-5-5">合作商机构配置</div>
		   		<div class="panel-body">
				 <table>
					<tr>
						<td><label for="exampleInputName">合作商代码</label></td>
						<td><input class="form-control " type="text" value="${parobject.prvcode!'' }" name="prvcode" placeholder="" readonly="readonly"></td>
						<td><label for="exampleInputName">合作商名称</label></td>
						<td><input class="form-control " type="text" value="${parobject.prvname }" name="prvname" placeholder=""></td>
						<td><label for="exampleInputName">合作商简称</label></td>
						<td><input class="form-control " type="text" value="${parobject.prvshotname!'' }" name="prvshotname" placeholder=""></td>
					</tr>
					<tr>
						<td><label for="exampleInputName">合作商级别</label></td>
						<td>
							<select name="prvgrade" class="form-control " >
							<#if parobject.prvgrade==''>
							<option selected="selected" value="">请选择合作商</option>
							<option value="01">1级</option>
							<option value="02">2级</option>
							<option value="03">3级</option>
							<option value="04">4级</option>
							<option value="05">5级</option>
							<option value="06">6级</option>
							<#elseif parobject.prvgrade=='00'>
							<option selected="selected" value="00">0级</option>
							<option value="01">1级</option>
							<option value="02">2级</option>
							<option value="03">3级</option>
							<option value="04">4级</option>
							<option value="05">5级</option>
							<option value="06">6级</option>
							<#elseif parobject.prvgrade=='01'>
							<option value="00">0级</option>
							<option selected="selected" value="01">1级</option>
							<option value="02">2级</option>
							<option value="03">3级</option>
							<option value="04">4级</option>
							<option value="05">5级</option>
							<option value="06">6级</option>
							<#elseif parobject.prvgrade=='02'>
							<option value="00">0级</option>
							<option value="01">1级</option>
							<option selected="selected" value="02">2级</option>
							<option value="03">3级</option>
							<option value="04">4级</option>
							<option value="05">5级</option>
							<option value="06">6级</option>
							<#elseif parobject.prvgrade=='03'>
							<option value="00">0级</option>
							<option value="01">1级</option>
							<option value="02">2级</option>
							<option selected="selected" value="03">3级</option>
							<option value="04">4级</option>
							<option value="05">5级</option>
							<option value="06">6级</option>
							<#elseif parobject.prvgrade=='04'>
							<option value="00">0级</option>
							<option value="01">1级</option>
							<option value="02">2级</option>
							<option value="03">3级</option>
							<option selected="selected" value="04">4级</option>
							<option value="05">5级</option>
							<option value="06">6级</option>
							<#elseif parobject.prvgrade=='05'>
							<option value="00">0级</option>
							<option value="01">1级</option>
							<option value="02">2级</option>
							<option value="03">3级</option>
							<option value="04">4级</option>
							<option selected="selected" value="05">5级</option>
							<option value="06">6级</option>
							<#elseif parobject.prvgrade=='06'>
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
						<td><label for="exampleInputName">合作商类型</label></td>
						<td>
						<select class="form-control" class="form-control " name="prvtype">
							<#if parobject.prvtype==''>
							<option selected="selected" value="">请选择类型</option>
							<option value="01">类型一</option>
							<option value="02">类型二</option>
							<option value="03">类型三</option>
							<#elseif parobject.prvtype=='01'>
							<option selected="selected" value="01">类型一</option>
							<option value="02">类型二</option>
							<option value="03">类型三</option>
							<#elseif parobject.prvtype=='02'>
							<option value="01">类型一</option>
							<option selected="selected" value="02">类型二</option>
							<option value="03">类型三</option>
							<#elseif parobject.prvtype=='03'>
							<option value="01">类型一</option>
							<option value="02">类型二</option>
							<option selected="selected" value="03">类型三</option>
							</#if>
						</select>
						</td>
						<td><label for="exampleInputName">上级合作商</label></td>
						
						<td>
						 <input type="hidden" id="parentcode" name="parentcode" value="${parobject.parentcode }">
						 <input type="text"	class="form-control"  id="prvname"  value="${pro.prvname }" placeholder="请选择">
						</td>
					</tr>
					<tr>
						<td><label for="exampleInputName">联系人</label></td>
						<td><input class="form-control " type="text" value="${parobject.linkname!'' }" name="linkname" placeholder=""></td>
						<td><label for="exampleInputName">联系方式</label></td>
						<td><input class="form-control " type="text" value="${parobject.linktel!'' }" name="linktel" placeholder=""></td>
						<td><label for="exampleInputName">地址</label></td>
						<td><input class="form-control " type="text" value="${parobject.address!'' }" name="address" placeholder=""></td>
					</tr>
					<tr>
						<td><label for="exampleInputName">网址</label></td>
						<td><input class="form-control " type="text" value="${parobject.prvurl!'' }" name="prvurl" placeholder=""></td>
						<td><label for="exampleInputName">传真</label></td>
						<td><input class="form-control " type="text" value="${parobject.fax!'' }" name="fax" placeholder=""></td>
						<td><label for="exampleInputName">邮件</label></td>
						<td><input class="form-control " type="text" value="${parobject.email!'' }" name="email" placeholder=""></td>
					</tr>
					<tr><td><input  type="hidden" id="saveupdateid" name="id" value="${parobject.id!'' }"></td></tr>
					<tr>
						<td><label for="exampleInputName">权限机构</label></td>
						<td>
							<input type="hidden" id="affiliationorg" name="affiliationorg" value="${parobject.affiliationorg }" >
						 	<input type="text"	class="form-control " id="deptname" value="${dept.comname }" placeholder="请选择" >
						</td>
					</tr>
				  </table>
		   		 </div>
		    	</div>
		    </div>
		</div>
		
		 <div class="row">
		     <div class="col-md-12">
		   	  <div class="panel panel-default m-bottom-5">
		   		<div class="panel-body">
				  <table>
					  <tr>
						<td ><input id="saveOrUpdatePro" class="btn btn-primary" type="submit" value="保存"></td>
						<td>&nbsp;</td>
						<td ><input class="btn btn-primary" type="button"   onclick="javascript:history.go(-1)" value="取消"/></td>
					</tr>
				  </table>
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
        <h4 class="modal-title" id="gridSystemModalLabel">选择合作商</h4>
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
</body>
</html>