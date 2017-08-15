<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>影像管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "cm/imagemanagement/imagemanagement" ]);
</script>
<body>
	<div class="container-fluid" id="imagemanagementid" hidden="true">
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">查询</div>
		  <div class="panel-body">
		    <div class="row">
			<div class="col-md-12">
				<form class="form-inline" role="form" id="imagemanagementform">
					<div class="form-group col-md-4">
						<label for="exampleInputCode">车牌号</label> <input type="text"
							class="form-control m-left-5" id="carlicenseno" name="carlicenseno" placeholder="">
					</div>
					<div class="form-group col-md-4">
						<label for="exampleInputCode">被保人</label> <input type="text"
							class="form-control m-left-5" id="insuredname" name="insuredname" placeholder="">
					</div>
				</form>
				<button id="querybutton" type="button" name="querybutton"
											class="btn btn-primary">查询</button>
			</div>
		</div>
		  </div>
		</div>
		<div class="panel panel-default">
		  <div class="panel-heading padding-5-5">
		  <div class="row">
				<div class="col-md-4">
					结果
					</div>
			</div>
			</div>
	    	<div id="imagemanagementdatas" class="row">
			</div>
			<div class="panel panel-default">
		  <div class="panel-body">
		  <div class="fixed-table-pagination" style="display: block;">
			<div class="pull-left pagination-detail" id="pageinfo">
		  		<span class="pagination-info"></span>
	  		</div>
	  		<div class="pull-right pagination" id="pageindexinfo">
	  		</div>
	  			<input type="hidden" id="pageindex" value="1">
	  			<input type="hidden" id="limitdata" value="6">
	  			<input type="hidden" id="maxindex" value="">
	  	</div>
	  	</div></div>
					<!-- <div class="thumbnail">
			 			<div style="width:100%; height:90%; text-align:center; vertical-align:middle;line-height:100%; border: 1px solid #ccc">
			 				<a href="javascript:tochecked('')">
			 				<img style="width: 120px; height: 120px; text-align:center; vertical-align:middle;line-height:120px; border: 1px solid #ccc" src="" title="" width="130px" height="130px"></a>
			 			</div>
			 			<div class="caption">
				 			<p><input id="checkbox" name="imgcheckbox" type="checkbox">&nbsp;&nbsp;
				 				<a class="a" href="javascript:tochecked('')"></a>
				 			</p>
				 			<i id="previewpicture" title="" class="glyphicon glyphicon-zoom-in"></i>&nbsp;&nbsp;
				 			<i id="editimg" title="" class="glyphicon glyphicon-edit"></i>&nbsp;&nbsp;
				 			<i id="deleteimg" title="" class="glyphicon glyphicon-trash"></i>&nbsp;&nbsp;
			 			</div>
			 		</div> -->
		</div>
	</div>
<div class="modal-header" id="detailinfoheaderid"  >
	<button type="button" class="close" data-dismiss="modal"><span aria->&times;</span><span class="sr-only">Close</span></button>
	<span class="modal-title" id="myModalLabel">车牌A112233&nbsp;被保人：被保人测试01(共4张)<label for="exampleInputCode">图片名称</label> <input type="text" class="form-control m-left-5" id="imagetypename" name="imagetypename" placeholder="">
	<button id="querybutton" type="button" name="querybutton" class="btn btn-primary">查询</button></span>
	<input type="hidden" id="detailpageindex" value="1">
	<input type="hidden" id="detaillimitdata" value="6">
	<input type="hidden" id="detailmaxindex" value="">
</div>
	<div id="deatialinfoid">
		<div class="col-xs-7 col-sm-5 col-md-4 col-lg-3" >
		<div class="thumbnail" style="border:1px solid #6680FF;width: 260px;height: 115px;">
				<div style="float:left; width:110px; height:110px;">
	                 <a href="javascript:opendetail('1')"><img style="width: 100px;height: 100px; border: 1px solid #ccc" src="http://cos.myqcloud.com/1002670/img_com/1211191001/921500009/2467de4687ab409eb140cc8b294b4bb0/img1437129352479099.jpg"></a>
	            </div>
	            <form action="" id="aaa">
		            <div style="float:left; width:140px; height:110px;">
		                 <div class="label" style="color:#000000;text-align:left;word-break:break-all; display:block; white-space:pre-wrap;word-wrap : break-word ;overflow: hidden ;">被保人组织机构代码证照</div>
		                 <div class="label" style="color:#000000;text-align:left;word-break:break-all; display:block; white-space:pre-wrap;word-wrap : break-word ;overflow: hidden ;"><input id="fileimg0" name="file" type="file" ></div>
		                 <div class="label" style="color:#AAAAAA;text-align:left;word-break:break-all; display:block; white-space:pre-wrap;word-wrap : break-word ;overflow: hidden ;"><a id="uploadbutton" type="button" class="btn btn-primary">上传</a></div>
		            </div>
		                 <input id="filetype" value="insuredidcardopposite" name="filetype">
	            </form>
	    </div>
	</div>
</div>	
<div class="fixed-table-pagination" style="display: block;">
			<div class="pull-left pagination-detail" id="detailpageinfo">
		  		<span class="pagination-info"></span>
	  		</div>
	  		<div class="pull-right pagination" id="detailpageindexinfo">
	  		</div>
	  	</div>
</body>
</html>
