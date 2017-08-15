<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>媒体库</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "system/fileupload" ]);
</script>

</head>
<body>
<div class="container-fluid">
	<div class="panel panel-default" id="simplequery">
	  <div class="panel-heading">
	  <div class="row">
		<div class="col-md-7">
	  	<ul class="nav nav-pills">
	  		<li role="presentation" id="imgli" class="active"><a href="javascript:switchnav('img')">图片库</a></li>
	  		<li role="presentation" id="videoli"><a href="javascript:switchnav('video')">视频库</a></li>
	  		<li role="presentation" id="audioli"><a href="javascript:switchnav('audio')">音频库</a></li>
	  		<li role="presentation" id="otherli"><a href="javascript:switchnav('other')">附件库</a></li>
	  		<li role="presentation" id="classifyli"><a href="javascript:switchnav('classify')">分类管理</a></li>
		</ul>
		</div>
		</div>
		</div>
		<div class="panel-body" id="imgpanelbody">
		  <div class="row">
			<div class="col-md-12">
				<form class="form-inline" role="form" method="POST" id="imgform">
					<div class="form-group col-md-4">
						<label for="exampleInputCode">图片分类&nbsp;&nbsp;</label>
						<select name="imgclassify" class="form-control" id ="imgclassify">
						</select>
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputCode">图片名称</label> <input type="text"
							class="form-control m-left-5" id="imgusercode" name="usercode" placeholder="">
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputName">图片描述</label> <input type="text"
							class="form-control m-left-5" id="imgname" name="name" placeholder="">
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputCode">车牌&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label> <input type="text"
							class="form-control m-left-5" id="imgplatenum" name="name" placeholder="">
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputCode">被保人&nbsp;&nbsp;&nbsp;&nbsp;</label> <input type="text"
							class="form-control m-left-5" id="imginsured" name="name" placeholder="">
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputName">业务编码</label> <input type="text"
							class="form-control m-left-5" id="imgtaskid" name="name" placeholder="">
					</div>
				</form>
			</div>
		   </div>
		</div>
		<div class="panel-body" id="videopanelbody" style="display: none">
		  <div class="row">
			<div class="col-md-12">
				<form class="form-inline" role="form" method="POST" id="videoform">
					<div class="form-group col-md-4">
						<label for="exampleInputCode">视频分类&nbsp;&nbsp;</label>
						<select name="videoclassify" class="form-control" id ="videoclassify">
						</select>
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputCode">视频名称</label> <input type="text"
							class="form-control m-left-5" id="videousercode" name="usercode" placeholder="">
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputName">视频描述</label> <input type="text"
							class="form-control m-left-5" id="videoname" name="name" placeholder="">
					</div>
				</form>
			</div>
		   </div>
		</div>
		<div class="panel-body" id="audiopanelbody" style="display: none">
		  <div class="row">
			<div class="col-md-12">
				<form class="form-inline" role="form" method="POST" id="audioform">
					<div class="form-group col-md-4">
						<label for="exampleInputCode">音频分类&nbsp;&nbsp;</label>
						<select name="audioclassify" class="form-control" id ="audioclassify">
						</select>
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputCode">音频名称</label> <input type="text"
							class="form-control m-left-5" id="audiousercode" name="usercode" placeholder="">
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputName">音频描述</label> <input type="text"
							class="form-control m-left-5" id="audioname" name="name" placeholder="">
					</div>
				</form>
			</div>
		   </div>
		</div>
		<div class="panel-body" id="otherpanelbody" style="display: none">
		  <div class="row">
			<div class="col-md-12">
				<form class="form-inline" role="form" method="POST" id="otherform">
					<div class="form-group col-md-4">
						<label for="exampleInputCode">附件分类&nbsp;&nbsp;</label>
						<select name="otherclassify" class="form-control" id ="otherclassify">
						</select>
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputCode">附件名称</label> <input type="text"
							class="form-control m-left-5" id="otherusercode" name="usercode" placeholder="">
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputName">附件描述</label> <input type="text"
							class="form-control m-left-5" id="othername" name="name" placeholder="">
					</div>
				</form>
			</div>
		   </div>
		</div>
	  <div class="panel-footer" id="imgpanelfooter">
					<button id="imgquerybutton" type="button" name="imgquerybutton"
										class="btn btn-primary">查询</button>
					<button id="imgeditbutton" type="button" name="imgeditbutton"
										class="btn btn-primary">编辑</button>
					<button id="imguploadbutton" type="button" name="imguploadbutton"
										class="btn btn-primary">上传</button>
					<button id="imgdeletebutton" type="button" name="imgdeletebutton"
										class="btn btn-primary">删除</button>
					<button id="imgpreviewbutton" type="button" name="imgpreviewbutton"
										class="btn btn-primary">预览</button>										
		</div>
		<div class="panel-footer" id="videopanelfooter" style="display: none">
					<button id="videoquerybutton" type="button" name="videoquerybutton"
										class="btn btn-primary">查询</button>
					<button id="videoeditbutton" type="button" name="videoeditbutton"
										class="btn btn-primary">编辑</button>
					<button id="videouploadbutton" type="button" name="videouploadbutton"
										class="btn btn-primary">上传</button>
					<button id="videodeletebutton" type="button" name="videodeletebutton"
										class="btn btn-primary">删除</button>
					<button id="videopreviewbutton" type="button" name="videopreviewbutton"
										class="btn btn-primary">预览</button>										
		</div>
		<div class="panel-footer" id="audiopanelfooter" style="display: none">
					<button id="audioquerybutton" type="button" name="audioquerybutton"
										class="btn btn-primary">查询</button>
					<button id="audioeditbutton" type="button" name="audioeditbutton"
										class="btn btn-primary">编辑</button>
					<button id="audiouploadbutton" type="button" name="audiouploadbutton"
										class="btn btn-primary">上传</button>
					<button id="audiodeletebutton" type="button" name="audiodeletebutton"
										class="btn btn-primary">删除</button>
					<button id="audiopreviewbutton" type="button" name="audiopreviewbutton"
										class="btn btn-primary">预览</button>										
		</div>
		<div class="panel-footer" id="otherpanelfooter" style="display: none">
					<button id="otherquerybutton" type="button" name="otherquerybutton"
										class="btn btn-primary">查询</button>
					<button id="othereditbutton" type="button" name="othereditbutton"
										class="btn btn-primary">编辑</button>
					<button id="otheruploadbutton" type="button" name="otheruploadbutton"
										class="btn btn-primary">上传</button>
					<button id="otherdeletebutton" type="button" name="otherdeletebutton"
										class="btn btn-primary">删除</button>
					<button id="otherpreviewbutton" type="button" name="otherpreviewbutton"
										class="btn btn-primary">预览</button>									
		</div>
	</div>
	<div class="panel panel-default" id="imgpanelresult">
		  <div class="panel-heading padding-5-5">
		  <div class="row">
				<div class="col-md-2">
					结果
					</div>
			</div>
			</div>
			<div class="fixed-table-loading" align="center" id="imgloading">
               	正在加载数据中
           </div>
		    <div class="row" id="imgdatas">
				<div class="fixed-table-pagination" style="display: block;">
		  	<!-- <div class="pull-left pagination-detail">
		  		<span class="pagination-info">显示第 1 到第 10 条记录，总共 11 条记录</span>
	  		</div>
	  		<div class="pull-right pagination">
	  			<ul class="pagination">
		  			<li class="page-first disabled"><a href="javascript:void(0)">«</a></li>
		  			<li class="page-pre disabled"><a href="javascript:void(0)">‹</a></li>
		  			<li class="page-number active"><a href="javascript:void(0)">1</a></li>
		  			<li class="page-number"><a href="javascript:void(0)">2</a></li>
		  			<li class="page-next"><a href="javascript:void(0)">›</a></li>
		  			<li class="page-last"><a href="javascript:void(0)">»</a></li>
	  			</ul>
	  		</div> -->
  		  </div>
		  </div>
	</div>
	<div class="panel panel-default" id="videopanelresult" style="display: none">
		  <div class="panel-heading padding-5-5">
		  <div class="row">
				<div class="col-md-2">
					结果
					</div>
			</div>
			</div>
			<div class="fixed-table-loading" align="center" id="videoloading">
               	正在加载数据中
           </div>
		    <div class="row" id="videodatas">
				<div class="fixed-table-pagination" style="display: block;">
  		  </div>
		  </div>
	</div>
	<div class="panel panel-default" id="audiopanelresult" style="display: none">
		  <div class="panel-heading padding-5-5">
		  <div class="row">
				<div class="col-md-2">
					结果
					</div>
			</div>
			</div>
			<div class="fixed-table-loading" align="center" id="audioloading">
               	正在加载数据中
           </div>
		    <div class="row" id="audiodatas">
				<div class="fixed-table-pagination" style="display: block;">
  		  </div>
		  </div>
	</div>
	<div class="panel panel-default" id="otherpanelresult" style="display: none">
		  <div class="panel-heading padding-5-5">
		  <div class="row">
				<div class="col-md-2">
					结果
					</div>
			</div>
			</div>
			<div class="fixed-table-loading" align="center" id="otherloading">
               	正在加载数据中
           </div>
		    <div class="row" id="otherdatas">
				<div class="fixed-table-pagination" style="display: block;">
  		  </div>
		  </div>
	</div>


<div class="row" id="classifypanelresult" style="display: none">
<div class="col-md-5">
<div class="panel panel-default m-bottom-2">
<div class="panel-heading padding-5-5">分类列表</div>
<!-- <div class="panel-body"></div> -->
	<ul class="tree tree-folder-select" role="tree" id="classifytypetree">
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

<div class="col-md-5">
			<div class="panel panel-default m-bottom-2">
			<div class="panel-heading padding-5-5">分类详情</div>
			<div class="panel-body">
	         <table>
	          <tr>
				<td>分类名称</td>
				<td colspan="2"><input id = "codename" class="form-control" type="text" name="codename">
				    <input type="hidden" id="id" name="id">
				</td>
		      </tr>
			  <tr>
			  	<td width="120">
			  	      <input id="updatemenubutton" class="btn btn-primary" type="button" value = "保存修改">
			  	</td>
			  	<td>
			  	    <input id="deletemenubutton" class="btn btn-primary" type="button" value = "删除此分类">
			  	</td>
			  	<td></td>
			  	<td>
			  	    <button id="addmenubutton" class="btn btn-success" type="button" name="addmenubutton" value="添加分类" >>添加分类<</button>
			  	</td>
			  </tr>
	         </table>
	         
	         </div>
	         </div>
	        </div>
</div>
</div>
</body>
</html>