<!DOCTYPE html>
<html lang ="en" class="fuelux" >
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>渠道协议管理</title>
    <link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
    <link href="${staticRoot}/css/system/flat-ui-icon.css" rel="stylesheet">
    <script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
    <style type="text/css">
        .htmleaf-header{margin-bottom: 15px;font-family: "Segoe UI", "Lucida Grande", Helvetica, Arial, "Microsoft YaHei", FreeSans, Arimo, "Droid Sans", "wenquanyi micro hei", "Hiragino Sans GB", "Hiragino Sans GB W3", "FontAwesome", sans-serif;}
        .htmleaf-icon{color: #fff;}
        tr{height:40px;}
        td{vertical-align: middle;}
        .modal-body{overflow: auto; height: 380px;}
        .titlelabel{margin-top:2px;margin-bottom:2px;}
        .panelnobottom{margin-bottom:2px;}
        .shortinput{width: 55px;}
        .pbodynopadding{padding-bottom: 0px;}
    </style>
    <script type="text/javascript">
        requirejs([ "zzbconf/channelagreement","lib/tsearch"]);
    </script>
</head>
<body>
<input id="treesearch1" class="channelname1" type="hidden"/>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-3">
            <div class="panel panel-default m-bottom-2">
                <div class="panel-heading padding-5-5">渠道列表</div>
                <div class="panel-body">
                	<div><input class="form-control ztree-search" id ="treesearch" data-bind="channelTree" name="treesearch" placeholder="输入渠道名称关键字进行搜索" />
                	</div>
                    <div class="ztree" id="channelTree" style="width:100%;  height:470px; overflow-y:auto;overflow-x:auto;"></div>
                </div>
            </div>
            <a href="#" class="btn btn-primary" id="protoSwitch" name="protoSwitch">协议切换</a>
        </div>
        
        <div class="col-md-9" id="channelagreementsubpage">
            <!--引入渠道协议子页面-->
		<#--<#include "./newchannelagreementsub.ftl"/>-->
        </div>
    </div>
</div>

<div class="modal fade" id="myProgressModal" name="myProgressModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-body">
        <div class="progress">
			<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="width: 45%">
	    		<span class="sr-only">45% Complete</span>
			</div>
		</div>
      </div>
    </div>
  </div>
</div>

</body>
</html>
