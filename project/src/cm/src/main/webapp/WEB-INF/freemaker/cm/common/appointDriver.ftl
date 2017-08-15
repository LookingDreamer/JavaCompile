<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>指定驾驶员信息弹出窗口</title>
		<!--<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>-->
		<style type="text/css">
		body {font-size: 14px;}
		</style>
	</head>
	<body>
		<!--指定驾驶员信息弹出框-->
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h6 class="modal-title">指定驾驶人</h6>
		</div>
		<div class="modal-body">
		  <ul id="myTab" class="nav nav-tabs" role="tablist" style="margin-bottom:0px;">
		  	<#list [0,1,2] as num>
		  		<li <#if (num = 0)>class="active"</#if>><a href="#driver${num+1}" role="tab" data-toggle="tab">驾驶人${num+1}</a></li>
		  	</#list>
		  </ul>
		  <div id="myTabContent" class="tab-content">
			  <#list [0,1,2] as num> 
				 <#if (driversInfo?size>num)>
				 	<div class="tab-pane fade <#if (num = 0)>in active</#if>" id="driver${num+1}">
						 <table class="table table-bordered" style="margin-bottom:0px;">
							<tr>
								<td class="active col-md-2">姓名：</td>
								<td class="col-md-4">${driversInfo[num].driverName}</td>
								<td class="active col-md-2">出生日期：</td>
								<td class="col-md-4">${driversInfo[num].driverBirthday}</td>
							</tr>
							<tr>
								<td class="active">性别：</td>
								<td>${driversInfo[num].driverGenderValue}</td>
								<td class="active">驾驶证类型：</td>
								<td>${driversInfo[num].driverLicensetypeValue}</td>
							</tr>
							<tr>
								<td class="active">驾驶证号：</td>
								<td>${driversInfo[num].driverLicenseno}</td>
								<td class="active">发照日期：</td>
								<td>${driversInfo[num].driverLicensedate}</td>
							</tr>
						  </table>
					</div>
				 <#else>
				 	<div class="tab-pane fade <#if (num = 0)>in active</#if>" id="driver${num+1}">
						<table class="table table-bordered" style="margin-bottom:0px;">
							<tr>
								<td class="active col-md-2">姓名：</td>
								<td class="col-md-4"></td>
								<td class="active col-md-2">出生日期：</td>
								<td class="col-md-4"></td>
							</tr>
							<tr>
								<td class="active">性别：</td>
								<td></td>
								<td class="active">驾驶证类型：</td>
								<td></td>
							</tr>
							<tr>
								<td class="active">驾驶证号：</td>
								<td></td>
								<td class="active">发照日期：</td>
								<td></td>
							</tr>
						  </table>
					  </div>
				 </#if>
			  </#list>
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
