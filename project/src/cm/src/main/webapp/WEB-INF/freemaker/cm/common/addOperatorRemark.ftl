<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>添加操作员备注信息弹出窗口</title>
		<!--<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>-->
		<style type="text/css">
			textarea#commentcontent {
				height:200px;
				width:100%;
			}
			span.feildtitle {
				font-weight:600;
				color:#34495E;
			}
			body {font-size: 14px;}
		</style>
		<!--引入添加操作员备注信息弹出窗口js文件-->
		<script type="text/javascript">
			requirejs([ "cm/common/addOperatorRemark" ],function() {
				require([ "jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public"], function($) {
					$(function() {
						initAddOperatorRemarkScript();
					});
				});
			});
		</script>
	</head>
	<body>
		<!--添加操作员备注信息弹出框-->
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h6 class="modal-title">给业管的备注</h6>
		</div>
		<div class="modal-body">
			<form class="form-inline" role="form" id="addOperatorRemarkForm">
			  <table class="table table-bordered">
				<tr>
					<td colspan="2" class="active">
						<span class="feildtitle">给操作员的备注</span>
					</td>
				</tr>
				<tr>
					<td class="col-md-2 active">填写人：</td>
					<td class="col-md-10 active"><span id="loginName">${loginName}</span>(${loginUserName})</td>
				</tr>
				<tr>
					<td class="active">内容：</td>
					<td>
					  <textarea style="resize:none;" class="form-control" id="commentcontent" name="commentcontent"></textarea><br/>
					  <span style="color:red">限500字</span>
					</td>
				</tr>
			  </table>
			  <div class="row">
			 	 <div class="col-md-6">
			 		<button class="btn btn-default" type="button" name="makesure" id="makesure" title="确定">&nbsp;&nbsp;确定&nbsp;&nbsp;</button>
			 	 </div>
			 	 <div class="col-md-6" align="right">
			 		<button class="btn btn-default" type="button" name="cancel" id="cancel" title="取消" data-dismiss="modal">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>
			 	 </div>
			  </div>
			  <input type="hidden" id="trackid" name="trackid" value="${trackid}"/>
			  <input type="hidden" id="tracktype" name="tracktype" value="${mainOrSub}"/>
			  <input type="hidden" id="noti" name="noti" value="${loginName}(${loginUserName})"/>
			  <input type="hidden" id="num" name="num" value="${num}"/>
			  <input type="hidden" id="instanceId" name="instanceId" value="${instanceId}"/>
			</form>
		</div>
	</body>
</html>
