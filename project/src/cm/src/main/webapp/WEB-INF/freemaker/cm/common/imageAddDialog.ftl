<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">

		<script type="text/javascript">
				requirejs([ "cm/common/imageAddDialog" ],function() {
						require([ "jquery", "ajaxfileupload" ,"bootstrap","public"],	function($) {
							$(function() {
								initImageAddDialogScript();
							});
						});
					});
		</script>

        <style type="text/css">
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 100%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.8;
                opacity:.80;
                filter: alpha(opacity=80);
            }
            .white_content {
                display: none;
                position: absolute;
                top: 25%;
                left: 25%;
                width: 50%;
                height: 50%;
                padding: 16px;
                border: 2px solid orange;
                background-color: white;
                z-index:1002;
                overflow: auto;
            }
        </style>

	</head>
	<body>
		<div class="modal-header">
	       <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
	       <span class="modal-title" id="myModalLabel">添加影像</span>
		</div>
		<div class="modal-body">
			<form class="form-inline" role="form" id="editUserRemarkForm">
				<table class="table table-bordered">
					<tr style="display: none">
						<td class="col-md-3 active">
							<span>图片类型：</span>
						</td>
						<td class="col-md-9">
							<select id="fileType" name="fileType" class="form-control m-left-5" style="max-width:300px;">
					 			<#--<#list data.inscCodeList as inscCode>
								    <option value="${inscCode.codetype},${inscCode.codename}">${inscCode.codename}</option>
								</#list>-->
								<option value="otherimage,其它">其它</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="active"><span>图片：</span></td>
						<td>
							<input type="file" id="file" name="file" multiple/>
							<input type="hidden" id="subInstanceId" value="${data.subInstanceId}"/>
							<input type="hidden" id="taskid" value="${data.taskid}"/>
						</td>
					</tr>
				</table>
				<div class="row">
					<div class="col-md-6">
						<button class="btn btn-default" type="button" name="makesure"
							id="makesure" title="确定">&nbsp;&nbsp;确定&nbsp;&nbsp;</button>
					</div>
					<div class="col-md-6" align="right">
						<button class="btn btn-default" type="button" name="cancel"
							id="cancel" title="取消" data-dismiss="modal">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>
					</div>
				</div>
			</form>
		</div>


        <#--<p>可以根据自己要求修改css样式
            <input type="button" value="点击这里打开窗口" onclick="openWindow()"/></p>-->
        <div id="light" class="white_content">
            <#--<img src="https://10.68.4.104/data/staticfiles/upload/upload/img/2017-02-21/aaaa.gif"/><br/>-->
            正在上传......<br/>
            <a href="#" onClick="closeWindow()"> Close</a>
		</div>
        <div id="fade" class="black_overlay"></div>


	</body>
</html>
