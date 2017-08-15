<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>保单详情</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/insbPolicyManagement" ]);
</script>
<body>
	<div class="container-fluid">
	<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
  <div class="panel panel-default">
    <div class="panel-heading" role="tab" id="headingTask">
      <h4 class="panel-title">
        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1" href="#collapseTask" aria-expanded="true" aria-controls="collapseTask">任务摘要
        </a>
      </h4>
    </div>
    <div id="collapseTask" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingTask">
      <div class="panel-body">
        <div class="row">
			<div class="col-md-12">
				<table class="table table-bordered">
						<tr>
							<td class="col-md-2 active">车牌号码</td>
							<td class="col-md-2">${detailedModel.carinfohis.carlicenseno }</td>
							<td class="col-md-2 active">被保险人</td>
							<td class="col-md-2">${detailedModel.recognizeePolicyitem.beibaorenname }</td>
							<td class="col-md-2 active">出单网点</td>
							<td class="col-md-2">${detailedModel.insbOrder.deptshortname }</td>
						</tr>
						<tr>
							<td class="active">业务跟踪号</td>
							<td>${detailedModel.insbOrder.taskid }</td>
							<td class="active">供应商</td>
							<td>${detailedModel.insbProvider.prvshotname }</td>
							<td class="active">提交时间</td>
							<td><#if detailedModel.insbPolicyitem.createtime??>${detailedModel.insbPolicyitem.createtime?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
						</tr>
	 			</table>
			</div>
		</div>
		</div>
    </div>
  </div>
  <div class="panel panel-default">
    <div class="panel-heading" role="tab" id="headingAgent">
      <h4 class="panel-title">
        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1" href="#collapseAgent" aria-expanded="false" aria-controls="collapseAgent"> 代理人信息
        </a>
      </h4>
    </div>
    <div id="collapseAgent" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingAgent">
      <div class="panel-body">
        <div class="row">
			<div class="col-md-12">
				<table class="table table-bordered">
						<tr>
							<td class="col-md-2 active">代理人姓名</td>
							<td class="col-md-2">${detailedModel.agent.name }</td>
							<td class="col-md-2 active">性别</td>
							<td class="col-md-2">${detailedModel.agent.sex }</td>
							<td class="col-md-2 active">工号</td>
							<td class="col-md-2">${detailedModel.agent.jobnum }</td>
						</tr>
						<tr>
							<td class="active">执业证/展示证号码</td>
							<td>${detailedModel.agent.licenseno }</td>
							<td class="active">身份证号</td>
							<td>${detailedModel.agent.idno }</td>
							<td class="active">联系电话</td>
							<td>${detailedModel.agent.phone }</td>
						</tr>
						<tr>
							<td class="active">所属机构</td>
							<td colspan="1">${detailedModel.agent.deptid }</td>
							<td class="active">渠道来源</td>
							<td colspan="3">${detailedModel.agent.purchaserchannel}</td>
						</tr>
	 			</table>
			</div>
		</div>
      </div>
    </div>
  </div>
  <div class="panel panel-default">
    <div class="panel-heading" role="tab" id="headingImage">
      <h4 class="panel-title">
        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1" href="#collapseImage" aria-expanded="false" aria-controls="collapseImage">影像信息
        </a>
      </h4>
    </div>
    <div id="collapseImage" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingImage">
      <div class="panel-body">
        <div class="row">
			<#list imginfo as img>
				<img style="width: 120px; height: 120px; text-align:center; vertical-align:middle;line-height:120px; border: 1px solid #ccc" alt="${img.codename }" src="${img.smallfilepath }">
			</#list>
		</div>
		</div>
    </div>
  </div>
  <div class="panel panel-default">
    <div class="panel-heading" role="tab" id="headingElecPolicy">
      <h4 class="panel-title">
        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1" href="#collapseElecPolicy" aria-expanded="false" aria-controls="collapseElecPolicy">
        	电子保单信息
        </a>
      </h4>
    </div>
    <div id="collapseElecPolicy" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingElecPolicy">
      <div class="panel-body">
        <div class="row">
        	<center>
				<#if jqElecPolicyFilePath != null>
					<a href="${jqElecPolicyFilePath}" target="_blank">交强险电子保单下载</a>
				<#else>
					无交强险电子保单
				</#if>
				/
				<#if syElecPolicyFilePath != null>
					<a href="${syElecPolicyFilePath}" target="_blank">商业险电子保单下载</a>
				<#else>
					无商业险电子保单
				</#if>
			</center>
		</div>
		</div>
    </div>
  </div>
  <div class="panel panel-default">
    <div class="panel-heading" role="tab" id="headingQuote">
      <h4 class="panel-title">
        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1" href="#collapseQuote" aria-expanded="false" aria-controls="collapseQuote">报价信息
        </a>
      </h4>
    </div>
    <div id="collapseQuote" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingQuote">
      <div class="panel-body">
        <div class="row">
			<div class="col-md-5">
			<div class="panel panel-primary">
			  <div class="panel-heading">投保单信息</div>
			  <div class="panel panel-info">
			  <div class="panel-heading">车辆信息</div>
			<table class="table table-hover">
						<tr>
							<td class="col-md-2 active">投保地区</td>
							<td class="col-md-4">${detailedModel.province}${detailedModel.city }</td>
						</tr>
						<tr>
							<td class="active">车牌</td>
							<td>${detailedModel.carinfohis.carlicenseno }</td>
						</tr>
						<tr>
							<td class="active">车主</td>
							<td><a href="javascript:window.parent.openDialog('policyitem/querypeopleinfo?show=carower&taskid=${detailedModel.carinfohis.taskid}&inscomcode=${detailedModel.insbProvider.id }');">${detailedModel.carinfohis.ownername }</a></td>
						</tr>
						<tr>
							<td class="active">车型</td>
							<td><a href="javascript:window.parent.openDialog('policyitem/querycarinfo?taskid=${detailedModel.carinfohis.taskid}&inscomcode=${detailedModel.carinfohis.inscomcode}');">${detailedModel.carinfohis.standardfullname }</a></td>
						</tr>
						<tr>
							<td class="active">发动机号</td>
							<td>${detailedModel.carinfohis.engineno }</td>
						</tr>
						<tr>
							<td class="active">车辆识别代号</td>
							<td>${detailedModel.carinfohis.vincode }</td>
						</tr>
						<tr>
							<td class="active">车辆初登日期</td>
							<td><#if detailedModel.carinfohis.registdate??>${detailedModel.carinfohis.registdate?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
						</tr>
						<tr>
							<td class="active">是否过户车</td>
							<td><#if detailedModel.carinfohis.isTransfercar =="1">是<#else>否</#if></td>
						</tr>
						<#--
						<tr>
							<td class="active">平均行驶里程</td>
							<td>${detailedModel.carinfohis.mileage }</td>
						</tr>
						<tr>
							<td class="active">行驶区域</td>
							<td>
								<#if detailedModel.carinfohis.drivingarea =="0">境内
								<#elseif detailedModel.carinfohis.drivingarea =="1">出入境
								<#elseif detailedModel.carinfohis.drivingarea =="2">省内
								<#elseif detailedModel.carinfohis.drivingarea =="3">场内
								<#elseif detailedModel.carinfohis.drivingarea =="4">固定线路
								<#else>其它
								</#if>
							</td>
						</tr>
						-->
	 		</table>
			</div>
			<div class="panel panel-info">
			  <div class="panel-heading">关系人信息</div>
			<table class="table table-hover">
						<tr>
							<td class="col-md-2 active" align="center">被保险人</td>
							<td class="col-md-4"><a href="javascript:window.parent.openDialog('policyitem/querypeopleinfo?show=insure&taskid=${taskid}&inscomcode=${detailedModel.insbProvider.id }');">${detailedModel.recognizeePolicyitem.beibaorenname }</a></td>
						</tr>
						<tr>
							<td class="active" align="center">投保人姓名</td>
							<td><a href="javascript:window.parent.openDialog('policyitem/querypeopleinfo?show=policy&taskid=${taskid}&inscomcode=${detailedModel.insbProvider.id }');">${detailedModel.insurePolicyitem.toubaorenname }</a></td>
						</tr>
						<tr>
							<td class="active" align="center">权益索赔人</td>
							<td><a href="javascript:window.parent.openDialog('policyitem/querypeopleinfo?show=shouyiren&taskid=${taskid}&inscomcode=${detailedModel.insbProvider.id }');">${detailedModel.benefitPolicyitem.shouyirenname }</a></td>
						</tr>
						<tr>
							<td class="active" align="center">联系人</td>
							<td><a href="javascript:window.parent.openDialog('policyitem/querypeopleinfo?show=contacts&taskid=${taskid}&inscomcode=${detailedModel.insbProvider.id }');">${detailedModel.contactsPolicyitem.contactsname }</a></td>
						</tr>
	 		</table>
			</div>
			<div class="panel panel-info">
			  <div class="panel-heading">车辆信息</div>
			<table class="table table-hover">
						<tr>
							<td class="col-md-2 active">是否指定驾驶人</td>
							<td class="col-md-4">
							<#if "${detailedModel.carinfohis.specifydriver }" == "是">
							<a href="javascript:window.parent.openDialog('policyitem/querydriverinfo?taskid=${detailedModel.carinfohis.taskid}');">${detailedModel.carinfohis.specifydriver }</a>
							<#else>
								${detailedModel.carinfohis.specifydriver!"" }
							</#if>
							</td>
						</tr>
						<#if "${risktype }"=="0">
							<tr>
								<td class="active">商业险起保日期</td>
								<td>${detailedModel.insbPolicyitem.startdates }</td>
							</tr>
							<tr>
								<td class="active">商业险终止日期</td>
								<td>${detailedModel.insbPolicyitem.enddates }</td>
							</tr>
						<#elseif "${risktype }"=="1">
							<tr>
								<td class="active">交强险起保日期</td>
								<td>${detailedModel.jqPolicyitem.startdates }</td>
							</tr>
							<tr>
								<td class="active">交强险终止日期</td>
								<td>${detailedModel.jqPolicyitem.enddates }</td>
							</tr>
						</#if>
						<tr>
							<td class="active">上年商业险投保公司</td>
							<td>${detailedModel.carinfohis.preinscode }</td>
						</tr>
						<tr>
							<td class="active">云查询保险公司</td>
							<td>${detailedModel.carinfohis.cloudinscompany }</td>
						</tr>
						<tr>
							<td class="active">用户的备注</td>
							<td>${detailedModel.carinfohis.noti }</td>
						</tr>
	 		</table>
			</div>
			</div>		
			</div>
			<div class="col-md-7">
			<div class="panel panel-primary">
			  <div class="panel-heading">保险配置</div>
			  <#assign totalbusinessrisks = 0.0>
			  <#assign totalstrongrisk = 0.0>
			  <#assign totaltraveltax = 0.0>
		  		<#if "${risktype }"=="0">
			  	<table class="table table-bordered">
			  		<thead>
			  			<tr>
			  				<th class="col-md-2 active">商业险</th>
							<th class="col-md-2 active">保额</th>
							<th class="col-md-2 active">保费</th>
			  			</tr>
			  		</thead>
			  		<tbody>
					    <#list carInsTaskInfo.insConfigInfo.insConfigList as insConfig>
				   			<#if insConfig.isChecked == "Y" && insConfig.inskindtype == "0">
				   				<tr>
								    <td class="bgg" style="text-align:left;">${insConfig.inskindname}</td>
								    <td>
								    	<#list insConfig.selecteditem as selectelm>
								    		<#if selectelm.TYPE == "01">
								    			保额：${insConfig.amount!""}${selectelm.VALUE.UNIT!""}
								    		<#else>
								    			${selectelm.VALUE.KEY!""}${selectelm.VALUE.UNIT!""}
								    		</#if>
								    		<#if selectelm_has_next>;</#if>
								    	</#list>
								    </td>
							    	<#assign totalbusinessrisks = totalbusinessrisks + insConfig.amountprice>
								    <td>${insConfig.amountprice!"0.0"}</td>
							    </tr>
				   			</#if>
				   		</#list>					  	
			  		</tbody>
			 </table>
			 <table class="table table-bordered">
			  		<thead>
			  			<tr>
			  				<th class="col-md-2 active">商业险附加不计免赔</th>
							<th class="col-md-2 active">保额</th>
							<th class="col-md-2 active">保费</th>
			  			</tr>
			  		</thead>
			  		<tbody>
			  			<#list carInsTaskInfo.insConfigInfo.insConfigList as insConfig>
				   			<#if insConfig.isChecked == "Y" && insConfig.inskindtype == "1">
				   				<tr>
								    <td class="bgg" style="text-align:left;">${insConfig.inskindname}</td>
								    <td>投保</td>
							    	<#assign totalstrongrisk = totalstrongrisk + insConfig.amountprice>
								    <td>${insConfig.amountprice!"0.0" }</td>
							    </tr>
				   			</#if>
				   		</#list>
			  		</tbody>
			 </table>
			 <#elseif "${risktype }"=="1">
			 <table class="table table-bordered">
			  		<thead>
			  			<tr>
			  				<th class="col-md-2 active">交强险和车船税</th>
							<th class="col-md-2 active">保额</th>
							<th class="col-md-2 active">保费</th>
			  			</tr>
			  		</thead>
			  		<tbody>
				   		 <#list carInsTaskInfo.insConfigInfo.insConfigList as insConfig>
				   			<#if insConfig.isChecked == "Y" && (insConfig.inskindtype == "2" || insConfig.inskindtype == "3")>
				   				<tr>
								    <td class="bgg" style="text-align:left;">${insConfig.inskindname}</td>
								    <td>
								    	<!--
								    	<#list insConfig.selecteditem as selectelm>
								    		<#if selectelm.TYPE == "01">
								    			保额：${insConfig.amount!""}${selectelm.VALUE.UNIT!""}
								    		<#else>
								    			${selectelm.VALUE.KEY!""}${selectelm.VALUE.UNIT!""}
								    		</#if>
								    		<#if selectelm_has_next>;</#if>
								    	</#list>
								    	-->
								    	<#if insConfig.inskindtype == "2">购买</#if>
								    	<#if insConfig.inskindtype == "3">代缴</#if>
								    </td>
								    <#assign totaltraveltax = totaltraveltax + insConfig.amountprice>
								    <td>${insConfig.amountprice!"0.0"}</td>
							    </tr>
				   			</#if>
				   		</#list>
			  		</tbody>
			 </table>
			 </#if>
			  </div>
			  <#if "${risktype }"=="0">
				  <div class="panel panel-info">
					  <div class="panel-heading">
					  	<label>商业险保费：${totalbusinessrisks???string(totalbusinessrisks,'0.0')}元    商业险附加不计免赔：${totalstrongrisk???string(totalstrongrisk,'0.0')} 元    </label>
					  	<br><label style=";color:red;">保费总额：${carInsTaskInfo.insConfigInfo.discountBusTotalAmountprice
					} 元</label> 
				  	 </div>
				  </div>
			   <#elseif "${risktype }"=="1">
				  <div class="panel panel-info">
					  <div class="panel-heading">
					  	<label>交强险和车船税保费：${totaltraveltax???string(totaltraveltax,'0.0')}元  </label>
					  	<br><label style=";color:red;">保费总额：${carInsTaskInfo.insConfigInfo.discountStrTotalAmountprice+
					carInsTaskInfo.insConfigInfo.discountCarTax+
					carInsTaskInfo.insConfigInfo.otherAmountprice} 元</label> 
				  	 </div>
				  </div>
			   </#if>
			</div>
		</div>
		</div>
    </div>
  </div>
   <div class="row">
			<div class="col-md-12">
					<table class="table table-bordered">
			  			<tr>
			  				<td class="active" colspan="2">保单号</td>
			  			</tr>
			  			<#if "${risktype }"=="0">
			  			<tr>
							<td class="col-md-2" align="center">商业险保单号</td>
							<td class="col-md-10">${detailedModel.insbPolicyitem.policyno }</td>
						</tr>
						<#elseif "${risktype }"=="1">
						<tr>
							<td align="center">交强险保单号</td>
							<td>${detailedModel.jqPolicyitem.policyno }</td>
						</tr>
						<#else>
						<tr>
							<td align="center">保单号</td>
							<td>--</td>
						</tr>
						</#if>
			 	</table>
			</div>
  		</div>
  <div class="row">
			<div class="col-md-12">
					<table class="table table-bordered">
			  			<tr>
			  				<td class="active" colspan="2">备注信息</td>
			  			</tr>
			  			<tr>
							<td class="col-md-2" align="center">给用户的备注</td>
							<td class="col-md-10">
								<#list usercomment as usercomment>
									${usercomment.commentcontent}
								</#list>	
							</td>	
						</tr>
						<tr>
							<td align="center">给操作员的备注</td>
							<td class="col-md-10">
								<#list operatorcommentList as operatorcomment>
									${operatorcomment.commentcontent}
								</#list>	
							</td>	
						</tr>
			 	</table>
			</div>
  	</div>
  	<div class="row">
  		<div class="col-md-12">
  			<input class="btn btn-primary" id="backbutton" value="返回" type="button">
  		</div>
  	</div>
</div>
</div>
</body>
</html>
