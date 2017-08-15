<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<title>打包工具</title>
	<link rel="stylesheet" type="text/css" href="${base}/css/lib/one.min.css" media="all" />
	<link rel="stylesheet" type="text/css" href="${base}/css/custom.css" />
	<link rel="stylesheet" type="text/css" href="${base}/js/plugin/bootstrap-3.3.5-dist/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="${base}/js/plugin/mmgrid/mmGrid.css" />
    <#--<link rel="stylesheet" type="text/css" href="${base}/js/plugin/jquery-easyui-1.5/themes/default/easyui.css">-->
    <#--<link rel="stylesheet" type="text/css" href="${base}/js/plugin/jquery-easyui-1.5/themes/icon.css">-->
</head>
<body class="lightgrayBg">
	<!--tab切换-->
	<div class="tabBox">
		<!-- 表单↓↓↓ -->
			<form class="form-horizontal" id="mFrom" name="mFrom" method="post" action="" role="form">
			<div class="panel panel-default">
				<div class="panel-heading">打包工具</h4></div>
				<div class="panel-body">
                    <div class="form-group">
                        <label for="" class="col-sm-3 control-label">项目类型</label>
                        <span class="ui-form-required"> *</span>
                        <div class="col-sm-5">
                            <select class="form-control" id="protype" name="protype" onchange="chgProType(this)">
                                <option value="1">maven-web</option>
                                <option value="2">eclipse</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="" class="col-sm-3 control-label">项目路径</label>
                        <span class="ui-form-required"> *</span>
                        <div class="col-sm-5">
                            <input  id="propath" name="propath" type="text" value="F:\3.85\branches\cm\bug\cm" class="form-control" placeholder="E:\ideaWorkspace\pack">
                        </div>
                    </div>


                        <div class="form-group">
                            <label for="" class="col-sm-3 control-label">源码路径(相对于项目路径)</label>
                            <span class="ui-form-required"> *</span>
                            <div class="col-sm-5">
                                <input  id="srcPath" name="srcPath" type="text" value="\src\main\java" class="form-control" placeholder="\src\main\java">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="" class="col-sm-3 control-label">配置资料文件路径(相对于项目路径)</label>
                            <span class="ui-form-required"> *</span>
                            <div class="col-sm-5">
                                <input  id="resourcesPath" name="resourcesPath" type="text" value="\src\main\resources" class="form-control" placeholder="\src\main\resources">
                            </div>
                        </div>

                    <div class="form-group">
                        <label for="" class="col-sm-3 control-label">WebRoot路径(相对于项目路径)</label>
                        <span class="ui-form-required"> *</span>
                        <div class="col-sm-5">
                            <input  id="wrPath" name="wrPath" type="text" value="\src\main\webapp" class="form-control">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="" class="col-sm-3 control-label">class编译路径(相对于项目路径)</label>
                        <span class="ui-form-required"> *</span>
                        <div class="col-sm-5">
                            <input  id="compilePath" name="compilePath" type="text" value="\target\classes" class="form-control" >
                        </div>
                    </div>


					<div class="form-group">
						<label for="" class="col-sm-3 control-label">时间</label>
                        <span class="ui-form-required"> *</span>
						<div class="col-sm-5">
							<input  id="packtime" name="packtime" type="text" value="2017-08-11 11:54:23" class="form-control" onclick='WdatePicker({dateFmt:"yyyy-MM-dd HH:mm:ss"})' >
						</div>

                        <button class="btn btn-info btn-lg" id="b_getfiles" type="button" style="height:38px;">获取更新文件</button>
					</div>

                    <div class="form-group">
                        <label for="" class="col-sm-3 control-label">更新文件列表</label>

                    </div>
                    <div class="form-group">
                        <table id="datagrid1"></table>
						<!-- 文件名，全路径，操作(排除)-->
                    </div>
                    <div class="form-group">
                        <label for="" class="col-sm-3 control-label">排除文件列表</label>

                    </div>
                    <div class="form-group">
                        <table id="datagrid2"></table>
                        <!-- 文件名，全路径，操作(恢复)-->
                    </div>


				</div>
			</div>
			<div class="panel panel-default">
   		    	<div class="panel-body" style="text-align:center;">
   		    	    <button class="btn btn-success btn-lg" id="b_pack" type="button">打&nbsp;包</button>
			    	<button class="btn btn-default btn-lg" id="b_reset" type="button">重&nbsp;置</button>
				</div>
			</div>
		</form>
		<!-- 表单↑↑↑ -->
	</div>
</body>
</html>
<script type="text/javascript" src="${base}/js/lib/jquery.min.js"></script>
<script type="text/javascript" src="${base}/js/plugin/jquery.validate.min.js"></script>
<script type="text/javascript" src="${base}/js/plugin/layer-v1.9.3/layer.js"></script>
<script type="text/javascript" src="${base}/js/plugin/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
<#--<script type="text/javascript" src="${base}/js/plugin/jquery-easyui-1.5/jquery.easyui.min.js"></script>-->
<script type="text/javascript" src="${base}/js/plugin/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${base}/js/plugin/mmgrid/mmGrid.js"></script>
<script type="text/javascript" src="${base}/page/pack.js"></script>
