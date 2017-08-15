<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>关系人信息</title>
<!--<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>-->
<style type="text/css">
	span.feildtitle {
		font-weight:bold;
		color:#34495E;
	}
	body {font-size: 14px;}
</style>
<body>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<h6 class="modal-title">关系人信息</h6>
	</div>
	<!--被保人信息-->
	<div class="modal-body">
		<div class="panel panel-default">
			<div class="panel-heading">
				<div class="row">
					<div class="col-md-12">
						<span class="feildtitle">${person.showtype}信息</span>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table class="table table-bordered" style="margin-bottom:0px">
						<tr>
							<td class="col-md-2">姓名:</td>
							<td class="col-md-4">${person.personname}</td>
                            <td>证件类型</td>
                            <td>${person.personidcardtypeValue}</td>
						</tr>
						<tr>
							<td>性别</td>
							<td>${person.persongenderValue}</td>
                            <td>证件号码</td>
                            <td>${person.personidcardno}</td>
						</tr>
						<!--tr>
							<td>婚姻状态</td>
							<td>${person.personmaritalstatusValue}</td>
							<td>居住地</td>
							<td>${person.personaddress}</td>
						</tr>
						<tr>
							<td>英文住址</td>
							<td>${person.personeaddress}</td>
                            <td>英文名</td>
                            <td>${person.personename}</td>
						</tr>
						<tr>
							<td>手机号码:</td>
							<td>${person.personcellphone}</td>
                            <td>血型</td>
                            <td>${person.personbloodtypeValue}</td>
						</tr>
						<tr>
							<td>生日</td>
							<td>${person.personbirthday}</td>
							<td>教育信息</td>
							<td>${person.personeducateinfoValue}</td>
						</tr>
						<tr>
							<td>年龄</td>
							<td>${person.personage}</td>
							<td>E-mail:</td>
							<td>${person.personEmail}</td>
						</tr>
						<tr>
							<td>邮编</td>
							<td>${person.personzip}</td>
							<td>职业</td>
							<td>${person.personjob}</td>
						</tr-->
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
