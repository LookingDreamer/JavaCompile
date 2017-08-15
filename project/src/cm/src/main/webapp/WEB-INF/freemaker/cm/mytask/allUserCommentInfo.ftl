<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>备注信息</title>
<!--<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>-->
<link href="${staticRoot}/css/cm/mytask/allUserCommentInfo.css" rel="stylesheet"/>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<h6 class="modal-title">${notiType}</h6>
	</div>
	<div class="modal-body word-break">
			<div class="row">
				<div class="col-md-12">
					<table class="table table-bordered" style="margin-bottom:0px">
						<#if notiType == "给操作员备注">
							<#list allComment as comment>
                                <tr>
                                    <td class="col-md-12">${comment}</td>
                                </tr>
							</#list>
						<#else>
							<#list allComment as comment>
                                <tr>
                                    <td class="col-md-12">
										<#if comment.typeName??>【${comment.typeName}】-</#if>
										<#if comment.userComment.commentcontent??>${comment.userComment.commentcontent}-</#if>
										<#if comment.userComment.operator??>${comment.userComment.operator}-</#if>
										<#if comment.userComment.createtime??>${comment.userComment.createtime?datetime?string("yyyy-MM-dd HH:mm:ss")}</#if>
									</td>
                                </tr>
							</#list>
						</#if>
					</table>
				</div>
			</div>
		</div>
		<div class="row">
		 	<div class="col-md-6"></div>
		 	<div class="col-md-6" align="right">
		 		<button class="btn btn-default" type="button" title="关闭" data-dismiss="modal">&nbsp;&nbsp;关闭&nbsp;&nbsp;</button>
		 	</div>
		</div>
	</div>
</body>
</html>
