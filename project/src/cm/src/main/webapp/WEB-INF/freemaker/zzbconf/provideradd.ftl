<!DOCTYPE HTML>
<html>
<head>
<meta charset="UTF-8"/>
<title>新增供应商</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<style type="text/css">
		tr{height:40px;}
</style>
<script type="text/javascript">
	requirejs([ "zzbconf/provider" ]);
</script>
</head>
<body>
    
<div id="saveupdatediv" class="container-fluid" style="margin-bottom: 20px">
	 <form id = "prosaveupdateform"  action="saveorupdatepro" method="post" enctype="multipart/form-data">
	    <div class="row">
		     <div class="col-md-12">
		   	  <div class="panel panel-default m-bottom-5">
		   	   <div class="panel-heading padding-5-5">添加供应商</div>
		   		<div class="panel-body">
				 <table>
					<tr>
						<td><label for="exampleInputName">供应商代码</label></td>
						<td><input class="form-control " type="text"  name="prvcode" placeholder="" readonly="readonly"></td>
						<td><label for="exampleInputName">供应商名称</label></td>
						<td><input class="form-control " type="text"  name="prvname" placeholder=""></td>
						<td><label for="exampleInputName">供应商简称</label></td>
						<td><input class="form-control " type="text"  name="prvshotname" placeholder=""></td>
					</tr>
					<tr>
						<td><label for="exampleInputName">供应商级别</label></td>
						<td>
							<select name="prvgrade" class="form-control " >
								<option selected="selected" value="">请选择供应商</option>
								<option value="01">1级</option>
								<option value="02">2级</option>
								<option value="03">3级</option>
								<option value="04">4级</option>
								<option value="05">5级</option>
								<option value="06">6级</option>
							</select>
						</td>
						<td><label for="exampleInputName">供应商类型</label></td>
						<td>
						<select class="form-control" class="form-control " name="prvtype">
							<option selected="selected" value="">请选择类型</option>
							<option value="01">类型一</option>
							<option value="02">类型二</option>
							<option value="03">类型三</option>
						</select>
						</td>
						<td><label for="exampleInputName">上级供应商</label></td>
						<td>
						 <input type="hidden" id="parentcode" name="parentcode" >
						 <input type="text"	class="form-control " id="prvname"  placeholder="请选择" >
						</td>
					</tr>
					<tr>
						<td><label for="exampleInputName">联系人</label></td>
						<td><input class="form-control " type="text"  name="linkname" placeholder=""></td>
						<td><label for="exampleInputName">联系方式</label></td>
						<td><input class="form-control " type="text"  name="linktel" placeholder=""></td>
						<td><label for="exampleInputName">地址</label></td>
						<td><input class="form-control " type="text"  name="address" placeholder=""></td>
					</tr>
					<tr>
						<td><label for="exampleInputName">客服电话</label></td>
						<td><input class="form-control " type="text"  name="servictel" placeholder=""></td>
						<td><label for="exampleInputName">网址</label></td>
						<td><input class="form-control " type="text"  name="prvurl" placeholder=""></td>
						<td><input type="hidden" id="saveupdateid" name="id" ></td>
					</tr>
					<tr>
						<td><label for="exampleInputName">归属机构</label></td>
						<td>
							<input type="hidden" id="affiliationorg" name="affiliationorg" >
						 	<input type="text"	class="form-control " id="deptname"  placeholder="请选择" >
						</td>
						<td><label for="exampleInputName">关联规则</label></td>
						<td>
							<input type="hidden" id="permissionorg" name="permissionorg" >
						 	<input type="text"	class="form-control"  id="rulepostil" placeholder="请选择">
						</td>
					</tr>
					<tr>
						<td><label for="exampleInputName">业务类型</label></td>
						<td colspan="5"><label class="radio-inline">
						  <input type="checkbox"  name="businesstype"  value="01"> 传统
						</label>
						<label class="radio-inline">
						  <input type="checkbox"  name="businesstype"  value="02"> 网销
						</label>
						<label class="radio-inline">
						  <input type="checkbox"  name="businesstype"  value="03"> 电销 
						</label>
						</td>
					</tr>
					<tr>
						<td><label for="exampleInputName">渠道类型</label></td>
						<td>
							<select class="form-control" class="form-control " name="channeltype">
								<option selected="selected" value="">请选择类型</option>
								<option value="01">B2C</option>
								<option value="02">B2B</option>
								<option value="03">B2C和B2B</option>
							</select>
						</td>
					</tr>
					<tr>
						<td><label for="exampleInputName">上传图片</label></td>
						<td colspan="2">
							<input class="form-control " type="file" name="file" >
						</td>
						<td>
							<input type="hidden" id="logo" value="prologo" name="logo">
						</td>
					</tr>
					<tr>
						<td><label for="exampleInputName">公司介绍</label></td>
					</tr>
					<tr>
						<td colspan="6"><textarea class="form-control" rows="3" name="companyintroduce"></textarea></td>
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
						<td><input class="form-control " type="text"  name="quotationtime" placeholder=""></td>
						<td><label for="exampleInputName">分钟</label></td>
					  </tr>
					  <tr>
						<td><label for="exampleInputName">两次报价间隔时间</label></td>
						<td><input class="form-control " type="text"  name="quotationinterval" placeholder=""></td>
						<td><label for="exampleInputName">分钟</label></td>
					  </tr>
					  <tr>
						<td><label for="exampleInputName">核保所需时间</label></td>
						<td><input class="form-control " type="text"  name="insuretime" placeholder=""></td>
						<td><label for="exampleInputName">分钟</label></td>
					  </tr>
					  <tr>
						<td><label for="exampleInputName">报价有效周期</label></td>
						<td><input class="form-control " type="text"  name="quotationvalidity" placeholder=""></td>
						<td><label for="exampleInputName">天</label></td>
					  </tr>
					  <tr>
						<td><label for="exampleInputName">支付号有效期</label></td>
						<td><input class="form-control " type="text"  name="payvalidity" placeholder=""></td>
						<td><label for="exampleInputName">天</label></td>
					  </tr>
					  <tr>
						<td><input type="checkbox" name="quickinsureflag" value="1"></td>
						<td colspan="2"><label for="exampleInputName">启动快速续保</label></td>
					  </tr>
					  <tr>
						<td ><input id="saveOrUpdatePro" class="btn btn-primary" type="submit" value="保存"></td>
						<td ><input class="btn btn-primary" type="button"   onclick="javascript:history.go(-1)" value="返回 "/></td>
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
			 <input id="saveOrUpdatePro" class="btn btn-primary" type="submit"  style="margin-left:50px;" value="保存">
			 <input class="btn btn-primary" type="button"  style="margin-left:50px;"  onclick="javascript:history.go(-1)" value="取消"/>
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