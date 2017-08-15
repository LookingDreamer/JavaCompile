<!DOCTYPE html>
<html lang ="en" class="fuelux" >  
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>合作商管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/system/flat-ui-icon.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<style type="text/css">
		.htmleaf-header{margin-bottom: 15px;font-family: "Segoe UI", "Lucida Grande", Helvetica, Arial, "Microsoft YaHei", FreeSans, Arimo, "Droid Sans", "wenquanyi micro hei", "Hiragino Sans GB", "Hiragino Sans GB W3", "FontAwesome", sans-serif;}
		.htmleaf-icon{color: #fff;}
</style>
<script type="text/javascript">
	requirejs([ "zzbconf/partners" ]);
</script>
</head>
<body>

<div class="col-md-12">
 <div>
	<div class="panel panel-default m-bottom-5">
<!-- 	 <div class="panel-heading padding-5-5">查询</div> -->
<!-- 		  <div class="panel-body"> -->
<!-- 		    <div class="row"> -->
<!-- 			<div class="col-md-12"> -->
<!-- 				<form class="form-inline" role="form" id="prvform"> -->
<!-- 					<div class="form-group col-md-4"> -->
<!-- 						<label for="exampleInputCode">合作商编码</label>  -->
<!-- 						<input type="text"	class="form-control " id="prvcode" name="prvcode" placeholder=""> -->
						<input id="updateid" type="hidden" name="id" >
<!-- 					</div> -->
<!-- 					<div class="form-group col-md-4"> -->
<!-- 						<label for="exampleInputName">合作商名称</label>  -->
<!-- 						<input type="text"	class="form-control " id="prvname" name="prvname" placeholder=""> -->
<!-- 					</div> -->
<!-- 					<div class="form-group col-md-4"> -->
<!-- 						<label for="exampleInputOrgName">合作商层级</label> <select class="form-control " id="prvgrade" name="prvgrade"> -->
<!-- 						  <option>1</option> -->
<!-- 						  <option>2</option> -->
<!-- 						  <option>3</option> -->
<!-- 						  <option>4</option> -->
<!-- 						  <option>5</option> -->
<!-- 						</select> -->
<!-- 					</div> -->
<!-- 					<div class="form-group col-md-4"> -->
<!-- 						<label for="exampleInputCode">合作商类型</label>  -->
<!-- 						<select class="form-control " id="prvtype" name="prvtype"> -->
<!-- 						  <option>1</option> -->
<!-- 						  <option>2</option> -->
<!-- 						  <option>3</option> -->
<!-- 						  <option>4</option> -->
<!-- 						  <option>5</option> -->
<!-- 						</select> -->
<!-- 					</div> -->
<!-- 					<div class="form-group col-md-4"> -->
<!-- 						<label for="exampleInputName">关联机构&nbsp;&nbsp;&nbsp;&nbsp;</label>  -->
						<input type="hidden" id="affiliationorgquery" name="affiliationorg" >
<!-- 					 	<input type="text"	class="form-control " id="querydeptname"  placeholder="请选择" > -->
<!-- 					</div> -->
<!-- 				</form> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 		  </div> -->
	<div class="panel-footer">
		<div style="float: left; margin-right: 5px">
<!-- 			<button id="querypro" type="button" name="querypro" -->
<!-- 								class="btn btn-primary">查询</button> -->
			<button  id="detailspro" type="button" name="detailspro"
								class="btn btn-primary">查看并修改</button>
			<button id="addpro" type="button" name="addpro"	class="btn btn-primary">新增</button>
		</div>
		<div style="float: left; margin-right: 5px">
			<form id = "deletbyid" action="deletbyid" method="post">
			 <input  type="hidden" id = "delid" name = "id"  >
			<input id="delpro" type="submit" name="delpro" value="删除"
								class="btn btn-primary">
			</form>
		</div>	
		<div>
			<button id="resetbutton" type="button" name="resetbutton"
											class="btn btn-primary">重置</button>
		</div>
	</div>
	</div>
	<h4>合作商列表</h4>
	<div style="width:100%; height:450px; overflow-y:scroll;overflow-x:hidden;">
	<ul class="tree tree-folder-select" role="tree" id="myTree">
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
	</ul>
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
</body>
</html>
