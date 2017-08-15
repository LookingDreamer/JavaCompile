<!DOCTYPE html>
<html lang ="en" class="fuelux" >  
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>菜单管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/system/flat-ui-icon.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<style type="text/css">
		.htmleaf-header{margin-bottom: 15px;font-family: "Segoe UI", "Lucida Grande", Helvetica, Arial, "Microsoft YaHei", FreeSans, Arimo, "Droid Sans", "wenquanyi micro hei", "Hiragino Sans GB", "Hiragino Sans GB W3", "FontAwesome", sans-serif;}
		.htmleaf-icon{color: #fff;}
		tr{height:40px;}
	</style>
<script type="text/javascript">
	requirejs([ "zzbconf/menusave" ]);
</script>
</head>
<body>
<div class="container-fluid">

<div class="row">

<div class="col-md-5">
<div>
<div class="panel panel-default m-bottom-2">
<div class="panel-heading padding-5-5">菜单列表>>新建菜单</div>
<div class="panel-body">
  <div style="width:100%; height:450px; overflow-y:auto;overflow-x:auto;">
	<#-- <ul class="tree tree-folder-select" role="tree" id="myTree">
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
	
	<div class="ztree" id="menuTree"></div>
  </div>
</div></div></div>
</div>

<div class="col-md-5">
			 <form id="addmenuform" action="addmenu" method="post" class="form-inline">
				
			  <div class="panel panel-default m-bottom-2">
			  <div class="panel-heading padding-5-5">菜单详情>>新建菜单</div>
			  <div class="panel-body">
				 
	         <table>
	          <tr>
				<td>菜单名称</td>
				<td colspan="2"><input id = "nodename" class="form-control" type="text" name="nodename">
				    <input type="hidden" id="id" name="id">
				</td>
		      </tr>
	          <tr>
				<td>URL</td>
				<td colspan="2"><input class="form-control" id = "activeurl" type="text"  name="activeurl">
				</td>
		      </tr>
		      <tr>
				<td>菜单code</td>
				<td colspan="2"><input class="form-control" id = "nodecode" type="text" name="nodecode"></td>
			  </tr>
		      <tr>
				<td>父级菜单code</td>
				<td colspan="2"><input class="form-control" id = "parentnodecode" type="text" name="parentnodecode" placeholder="'点击提示'" 
				                  data-container="body" data-toggle="popover" data-placement="right" data-trigger="focus" 
				                  data-content="新建子菜单，可点击左侧相应父菜单，自动填充。新建一级菜单请填写‘0’。"></td>
			  </tr>
		      <tr>
				<td>父级菜单name</td>
				<td colspan="2"><input class="form-control" id = "parentnodecodename" type="text" name="parentnodecodename" placeholder="'点击提示'"
				                  data-container="body" data-toggle="popover" data-placement="right" data-trigger="focus" 
				                  data-content="新建子菜单，可点击左侧相应父菜单，自动填充。新建一级菜单可不填为空。"></td>
			  </tr>
		      <tr>
				<td>菜单等级</td>
				<td colspan="2"><input class="form-control" id="nodelevel" type="text" name="nodelevel" placeholder="'1'：一级；'2'：二级... " 
				                  data-container="body" data-toggle="popover" data-placement="right" data-trigger="focus" 
				                  data-content="新建子菜单，可点击左侧相应父菜单，自动填充。新建一级菜单请填写‘1’。"></td>
			  </tr>
		      <tr>
				<td>同级菜单顺序</td>
				<td colspan="2"><input class="form-control" id="orderflag" type="text" name="orderflag" placeholder="1,2,3,4..."></td>
			  </tr>
			  <!-- <tr>
				<td>是否显示</td>
				<td colspan="2"><input class="form-control" id="status" type="text" name="status" placeholder="'1'是，'0'否"></td>
			  </tr> -->
			  <tr>
				<td>是否有子菜单</td>
				<td>
				 &nbsp;&nbsp;<input id="childflag" type="radio" name="childflag" value="1"/>是&nbsp;&nbsp;&nbsp;
				 <input id=childflag" type="radio"  name="childflag" value="0"/>否
				</td>
			  </tr>
			  <tr>
				<td>是否显示</td>
				<td>
				 &nbsp;&nbsp;<input id="status" type="radio" checked="checked" name="status" value="1"/>是&nbsp;&nbsp;&nbsp;
				 <input id=status" type="radio"  name="status" value="0"/>否
				</td>
			  </tr>
	          <tr>
				<td>图标样式 :</td>
				<td>
				<span class="fui-icons" id="iconurlpic"></span>
				</td>
				<td align="right">
				<input type="hidden" id="iconurl" name="iconurl" >
				<span id = "iconurlbtn" class="btn btn-info"  type="button"  name="iconurlbtn" value="更换">更换</span>
				</td>
		      </tr>
		      <tr>
				<td>备注</td>
				<td colspan="2"><input class="form-control" id="noti" type="text" name="noti"></td>
			  </tr>
			  <tr>
			  	<td width="120">
			  	      <button id="reset" class="btn btn-primary" type="button" name="reset" value="重置" >重置</button>
			  	</td>
			  	<td>
			  	      <button id="addmenubutton" class="btn btn-success" type="button" name="addmenubutton">确认新建</button>
			  	</td>
			  	<td></td>
			  	<td>
			  	      <button id="return" class="btn btn-info" type="button" name="return" onclick="history.go(-1)">返回</button>
			  	</td>
			  </tr>
	         </table>
	         </div>
	         </div>
	         </form>
	        </div>

    
    </div>
</div>

<div id="showpic" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="gridSystemModalLabel">选择图标</h4>
      </div>
      <div class="modal-body">
        <div class="container-fluid">
          <div class="row">
           <div class="fui-icons">
		          <span class="fui-window"></span>
		          <span class="fui-windows"></span>
		          <span class="fui-upload"></span>
		          <span class="fui-mic"></span>
		          <span class="fui-export"></span>
		          <span class="fui-heart"></span>
		          <span class="fui-location"></span>
		          <span class="fui-plus"></span>
		          <span class="fui-list"></span>
		          <span class="fui-new"></span>
		          <span class="fui-video"></span>
		          <span class="fui-photo"></span>
		          <span class="fui-time"></span>
		          <span class="fui-eye"></span>
		          <span class="fui-chat"></span>
		          <span class="fui-home"></span>
		          <span class="fui-search"></span>
		          <span class="fui-user"></span>
		          <span class="fui-mail"></span>
		          <span class="fui-lock"></span>
		          <span class="fui-power"></span>
		          <span class="fui-star"></span>
		          <span class="fui-calendar"></span>
		          <span class="fui-gear"></span>
		          <span class="fui-bookmark"></span>
		          <span class="fui-exit"></span>
		          <span class="fui-trash"></span>
		          <span class="fui-folder"></span>
		          <span class="fui-bubble"></span>
		          <span class="fui-calendar-solid"></span>
		          <span class="fui-star-2"></span>
		          <span class="fui-credit-card"></span>
		          <span class="fui-clip"></span>
		          <span class="fui-link"></span>
		          <span class="fui-pause"></span>
		          <span class="fui-volume"></span>
		          <span class="fui-mute"></span>
		          <span class="fui-resize"></span>
		          <span class="fui-tag"></span>
		          <span class="fui-document"></span>
		          <span class="fui-image"></span>
		          <span class="fui-checkbox-checked"></span>
		          <span class="fui-list-thumbnailed"></span>
		          <span class="fui-list-small-thumbnails"></span>
		          <span class="fui-list-numbered"></span>
		          <span class="fui-list-large-thumbnails"></span>
		          <span class="fui-list-columned"></span>
		          <span class="fui-list-bulleted"></span>
		          <span class="fui-facebook"></span>
		          <span class="fui-youtube"></span>
		          <span class="fui-vimeo"></span>
		          <span class="fui-twitter"></span>
		          <span class="fui-stumbleupon"></span>
		          <span class="fui-spotify"></span>
		          <span class="fui-skype"></span>
		          <span class="fui-pinterest"></span>
		          <span class="fui-path"></span>
		          <span class="fui-linkedin"></span>
		          <span class="fui-google-plus"></span>
		          <span class="fui-dribbble"></span>
		          <span class="fui-behance"></span>
		          <span class="fui-yelp"></span>
		          <span class="fui-wordpress"></span>
		          <span class="fui-windows-8"></span>
		          <span class="fui-vine"></span>
		          <span class="fui-tumblr"></span>
		          <span class="fui-paypal"></span>
		          <span class="fui-lastfm"></span>
		          <span class="fui-instagram"></span>
		          <span class="fui-html5"></span>
		          <span class="fui-github"></span>
		          <span class="fui-foursquare"></span>
		          <span class="fui-dropbox"></span>
		          <span class="fui-android"></span>
		          <span class="fui-apple"></span>
		      </div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>

</body>
</html>
