<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信息展示</title>
</head>
<body>
	<div class="panel panel-default">
		<div class="panel-heading">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
        	<h4 class="modal-title"><#if "${flag }" == "1">车辆信息</#if> <#if "${flag }" == "2">关系人信息</#if> <#if "${flag }" == "3">驾驶人信息</#if></h4>
		</div>
		<div class="panel-body">
			<div class="row">
				<div class="col-md-12">
				<#if "${flag }" == "1">
					<table class="table table-bordered">
						<tr>
							<td class="col-md-4 active">车牌号码</td>
							<td class="col-md-8">${carinfo.carlicenseno !"" }</td>
						</tr>
						<tr>
							<td class="col-md-4 active">所属性质</td>
							<td class="col-md-8">
								${carinfo.propertyName}
							</td>
						</tr>
						<tr>
							<td class=" col-md-4 active">车辆使用性质</td>
							<td class="col-md-8">
								${carinfo.carPropertyName}
							</td>
						</tr>
						<tr>
							<td class=" col-md-4 active">车辆种类</td>
							<td class="col-md-8">
								${carmodelinfo.syvehicletypename}
							</td>
						</tr>
						<tr>
							<td class=" col-md-4 active">品牌型号</td>
							<td class="col-md-8">${carmodelinfo.brandname }</td>
						</tr>
						<tr>
							<td class=" col-md-4 active">核定载人数</td>
							<td class="col-md-8">${carmodelinfo.seat }</td>
						</tr>
						<tr>
							<td class=" col-md-4 active">核定载质量</td>
							<td class="col-md-8">${carmodelinfo.unwrtweight }</td>
						</tr>
						<tr>
							<td class=" col-md-4 active">排气量</td>
							<td class="col-md-8">${carmodelinfo.displacement }</td>
						</tr>
						<tr>
							<td class=" col-md-4 active">整备质量</td>
							<td class="col-md-8">${carmodelinfo.fullweight }</td>
						</tr>
						<tr>
							<td class=" col-md-4 active">年款</td>
							<td class="col-md-8">${carmodelinfo.listedyear }</td>
						</tr>
						<tr>
							<td class=" col-md-4 active">车型配置</td>
							<td class="col-md-8">${carmodelinfo.cardeploy }</td>
						</tr>
						<tr>
							<td class=" col-md-4 active">投保车价</td>
							<td class="col-md-8">${carmodelinfo.policycarprice }</td>
						</tr>
						<tr>
							<td class=" col-md-4 active">新车购置价</td>
							<td class="col-md-8">${carmodelinfo.taxprice }</td>
						</tr>
					</table>
				</#if> <#if "${flag }" == "2">
					<table class="table table-bordered">
						<tr>
							<td class="col-md-3 active">姓名</td>
							<td class="col-md-3">${insbPerson.name !"" }</td>
                            <td class="col-md-3 active">证件类型</td>
                            <td>${codeName}</td>
						</tr>
						<tr>
							<td class="col-md-3 active">性别</td>
							<td><#if "${insbPerson.gender  }" =="0">男 </#if><#if  "${insbPerson.gender  }" =="1">女</#if> </td>
                            <td class="col-md-3 active">证件号码</td>
                            <td>${insbPerson.idcardno !"" }</td>
						</tr>
						<!--tr>
							<td class="col-md-3 active">婚姻状态</td>
							<td>${insbPerson.maritalstatus !"" }</td>
							<td class="col-md-3 active">居住地</td>
							<td>${insbPerson.address !"" }</td>
						</tr>
						<tr>
							<td class="col-md-3 active">年龄</td>
							<td>${insbPerson.age !"" }</td>
                            <td class=" col-md-3 active">英文名</td>
                            <td class="col-md-3">${insbPerson.ename !"" }</td>
						</tr>
						<tr>
							<td class="col-md-3 active">手机号码</td>
							<td>${insbPerson.cellphone !"" }</td>
                            <td class="col-md-3 active">血型</td>
                            <td>${insbPerson.bloodtype !"" }</td>
						</tr>
						<tr>
							<td class="col-md-3 active">生日</td>
							<td><#if insbPerson.birthday??>${insbPerson.birthday?string("yyyy-MM-dd HH:mm:ss") }</#if></td>
							<td class="col-md-3 active">教育信息</td>
							<td>${insbPerson.educateinfo !"" }</td>
						</tr>
						<tr>
							<td class="col-md-3 active">E-mail</td>
							<td>${insbPerson.email !"" }</td>
							<td class="col-md-3 active">邮编</td>
							<td>${insbPerson.zip !"" }</td>
						</tr>
						<tr>
							<td class="col-md-3 active">职业</td>
							<td colspan="3">${insbPerson.job !"" }</td>
						</tr-->
					</table>
				</#if> <#if "${flag }" == "3">
					<ul class="nav nav-tabs" role="tablist">
						<#list [0,1,2] as num>
					  		<li role="presentation" <#if (num = 0)>class="active"</#if>><a href="#driver${num+1}" role="tab" data-toggle="tab">驾驶人${num+1}</a></li>
					  	</#list>
					</ul>
					<div class="tab-content">
						<#list [0,1,2] as num> 
						 <#if (driverList?size>num)>
						 	<div role="tabpanel" class="tab-pane <#if (num = 0)>active</#if>" id="driver${num+1}">
							<table class="table table-bordered">
								<tr>
									<td class="col-md-3 active">姓名</td>
									<td class="col-md-3">${driverList[num].name }</td>
									<td class="col-md-3 active">出生日期</td>
									<td class="col-md-3"><#if driverList[num].birthday??>${driverList[num].birthday?string("yyyy-MM-dd HH:mm:ss") }</#if></td>
								</tr>
								<tr>
									<td class="col-md-3 active">性别</td>
									<td class="col-md-3">${driverList[num].gender }</td>
									<td class="col-md-3 active">驾驶证类型</td>
									<td class="col-md-3">${driverList[num].licensetype }</td>
								</tr>
								<tr>
									<td class="col-md-3 active">驾驶证号</td>
									<td class="col-md-3">${driverList[num].licenseno }</td>
									<td class="col-md-3 active">发照日期</td>
									<td class="col-md-3"><#if driverList[num].licensedate??>${driverList[num].licensedate?string("yyyy-MM-dd HH:mm:ss") }</#if></td>
								</tr>
							</table>
						</div>
						 <#else>
						 	<div role="tabpanel" class="tab-pane <#if (num = 0)>active</#if>" id="driver${num+1}">
							<table class="table table-bordered">
								<tr>
									<td class="col-md-3 active">姓名</td>
									<td class="col-md-3">${driverList[num].name }</td>
									<td class="col-md-3 active">出生日期</td>
									<td class="col-md-3"><#if driverList[num].birthday??>${driverList[num].birthday?string("yyyy-MM-dd HH:mm:ss") }</#if></td>
								</tr>
								<tr>
									<td class="col-md-3 active">性别</td>
									<td class="col-md-3">${driverList[num].gender }</td>
									<td class="col-md-3 active">驾驶证类型</td>
									<td class="col-md-3">${driverList[num].licensetype }</td>
								</tr>
								<tr>
									<td class="col-md-3 active">驾驶证号</td>
									<td class="col-md-3">${driverList[num].licenseno }</td>
									<td class="col-md-3 active">发照日期</td>
									<td class="col-md-3"><#if driverList[num].licensedate??>${driverList[num].licensedate?string("yyyy-MM-dd HH:mm:ss") }</#if></td>
								</tr>
							</table>
						</div>
						 </#if>
					  </#list>
					</div>
				</#if>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
