<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>错误信息配置</title>
    <link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
    <script data-ver="${jsver}" data-main="${staticRoot}/js/load"
            src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
    <script type="text/javascript">
        requirejs([ "extra/codemanage" ]);
    </script>
    <style type="text/css">
		.panel panel-info{
			width:600px;
			height:500px;
			margin:150px 400px auto auto;
		}
	</style>
<body>
<div class="container-fluid">
    <div class="panel panel-default">
        <div class="panel-heading padding-5-5">
            <div class="row">
                <div class="col-md-2">
                    查询结果
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <table id="table-list"></table>
            </div>
        </div>
    </div>
</div>
<!-- 参数编辑操作框 -->
<div class="modal fade" id="editModel" aria-hidden="true" data-backdrop="static">
<div class="modal-dialog">
        <div class="panel panel-info">
			<div class="panel-heading">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4>修改提示</h4>
			</div>
			<form action="" id="codeForm" name="codeForm">
				<div class="panel-body">
	            	<input type="hidden" name="id" id="id" value=""></input>
	             	<table>
	                    <tr>
	                       <td><label>掌中宝前端提示(100字内):</label></td>
	                    </tr>
	                    <tr>
	                       <td>
	                          <textarea rows="3" cols="70" id="codename" name="codename"></textarea>
	                       </td>
	                    </tr>
	                    <tr>
	                        <td><label>渠道提示(100字内):</label></td>
	                    </tr>
	                    <tr>
	                       	<td>
	                        	<textarea rows="3" cols="70" id="prop2" name="prop2"></textarea>
	                        </td>
	                    </tr>
	                    <tr>
	                        <td><label>说明:</label></td>
	                    </tr>
	                    <tr>
	                    <td>
	                          <textarea rows="3" cols="55" id="noti" name="moti"></textarea>
	                    </td>
	                    </tr>
	                </table>
	       		</div>
				<div class="panel-footer" style="text-align: right;">  
	            	<input id="execButton" name="execButton" type="button" class="btn btn-primary" value="确定"/>
					<button class="btn btn-default" type="button" title="关闭" data-dismiss="modal">&nbsp;&nbsp;关闭&nbsp;&nbsp;</button>
				</div>
			</form>
		</div>
	</div>
</div>
</body>
</html>