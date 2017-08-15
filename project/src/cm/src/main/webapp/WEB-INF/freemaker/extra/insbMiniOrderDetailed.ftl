<#include "./macro/isTransferredCar.ftl"/>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>订单详情</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "extra/insbMiniOrderManagement" ]);
</script>
</head>
<body>
	<div class="container-fluid">
        <div class="row">
            <div class="col-md-12">
                <table class="table table-bordered">
                    <tr>
                        <td>
                            <input class="btn btn-primary" type="submit" id="mbackbutton2" value="返回">
                        </td>
                    </tr>
                </table>
            </div>
        </div>
	<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
		<div style="display: none;">
			<input id="agentid" name="agentid" type="text" hidden="true" value="${agentid}" />
            <form role="form" id="mqueryorder" action="/cm/agentuser/queryorderlist" method="post">
                <div class="form-group form-inline col-md-4" style="display:none">
                    <label for="exampleInputCode">agentID</label> <input type="text"
                                                                         class="form-control m-left-5" id="agentid" name="agentid" placeholder="" value="${agentid}" >
                </div>
            </form>
		</div>
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
							<td class="col-md-2">
								<#if "${insborderid }"=="insborderid">
									${detailedModel.insbOrder.deptshortname}
								<#else>
									${detailedModel.inscdept.comname}
								</#if>
							</td>
						</tr>
						<tr>
							<td class="active">业务跟踪号</td>
							<td>
								<#if "${insborderid }"=="insborderid">
									${detailedModel.insbOrder.taskid }
								<#else>
									${detailedModel.policyitem.taskid}
								</#if>
							</td>
							<td class="active">供应商</td>
							<td>${detailedModel.insbProvider.prvshotname }</td>
							<td class="active">提交时间</td>
							<td>
								<#if "${insborderid }"=="insborderid">
									<#if detailedModel.jqPolicyitem.policyno??>
										<#if detailedModel.jqPolicyitem.createtime??>${detailedModel.jqPolicyitem.createtime?string("yyyy-MM-dd HH:mm:ss")}</#if>
									<#else>
										<#if detailedModel.insbPolicyitem.createtime??>${detailedModel.insbPolicyitem.createtime?string("yyyy-MM-dd HH:mm:ss")}</#if>
									</#if>
								<#else>
									<#if detailedModel.policyitem.createtime??>${detailedModel.policyitem.createtime?string("yyyy-MM-dd HH:mm:ss")}</#if>
								</#if>
							</td>
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
        <div id="menuPic">
        
			<#list imginfo as img>
				<a href='javascript:window.parent.openImgView("common/insurancepolicyinfo/previewPicture?pictureId=${img.pictureId}&subInstanceId=${subInstanceId}&taskid=${taskid}")'>
					<img style="width: 120px; height: 120px; text-align:center; vertical-align:middle;line-height:120px; border: 1px solid #ccc" alt="${img.codename }" src="${img.smallfilepath }">
   				</a>
			</#list>
		
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
							<td class="col-md-2 active" align="center">投保地区</td>
							<td class="col-md-4">${detailedModel.province }${detailedModel.city }</td>
						</tr>
						<tr>
							<td class="active" align="center">车牌</td>
							<td>${detailedModel.carinfohis.carlicenseno }</td>
						</tr>
						<tr>
							<td class="active" align="center">车主</td>
							<td><a href="javascript:window.parent.openDialog('policyitem/querypeopleinfo?show=carower&taskid=${detailedModel.carinfohis.taskid}&inscomcode=${detailedModel.insbProvider.id }');">${detailedModel.carinfohis.ownername }</a></td>
						</tr>
						<tr>
							<td class="active" align="center">车型</td>
							<td><a href="javascript:window.parent.openDialog('policyitem/querycarinfo?taskid=${detailedModel.carinfohis.taskid}&inscomcode=${detailedModel.insbProvider.id }');">${detailedModel.carinfohis.standardfullname }</a></td>
						</tr>
						<tr>
							<td class="active" align="center">发动机号</td>
							<td>${detailedModel.carinfohis.engineno }</td>
						</tr>
						<tr>
							<td class="active" align="center">车辆识别代号</td>
							<td>${detailedModel.carinfohis.vincode }</td>
						</tr>
						<tr>
							<td class="active" align="center">车辆初登日期</td>
							<td><#if detailedModel.carinfohis.registdate??>${detailedModel.carinfohis.registdate?string("yyyy-MM-dd HH:mm:ss")}</#if></td>
						</tr>
						<tr>
							<td class="active" align="center">是否过户车</td>
							<td><@isTransferredCar value=detailedModel.carinfohis.isTransfercar /></td>
						</tr>
						<#--
						<tr>
							<td class="active" align="center">平均行驶里程</td>
							<td>${detailedModel.carinfohis.mileage }</td>
						</tr>
						<tr>
							<td class="active" align="center">行驶区域</td>
							<td>${detailedModel.carinfohis.drivingarea }</td>
						</tr>
						-->
	 		</table>
			</div>
			<div class="panel panel-info">
			  <div class="panel-heading">关系人信息</div>
			<table class="table table-hover">
						<tr>
							<td class="col-md-2 active" align="center">被保险人</td>
							<td class="col-md-4">
								<a href="javascript:window.parent.openDialog('policyitem/querypeopleinfo?show=insure&taskid=${taskid}&inscomcode=${detailedModel.insbProvider.id }');">${detailedModel.recognizeePolicyitem.beibaorenname }</a>
							</td>
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
							<td class="col-md-2 active" align="center">是否指定驾驶人</td>
							<td class="col-md-4">
							<#if "${detailedModel.carinfo.specifydriver }" == "是">
							<a href="javascript:window.parent.openDialog('policyitem/querydriverinfo?taskid=${detailedModel.carinfo.taskid}');">${detailedModel.carinfo.specifydriver }</a>
							<#else>
								${detailedModel.carinfo.specifydriver!"" }
							</#if>
							</td>
						</tr>
							<tr>
							<td class="active" align="center">商业险起保日期</td>
							<td>${detailedModel.insbPolicyitem.startdates }
						</tr>
						<tr>
							<td class="active" align="center">商业险终止日期</td>
							<td>${detailedModel.insbPolicyitem.enddates}</td>
						</tr>
							<tr>
							<td class="active" align="center">交强险起保日期</td>
							<td>${detailedModel.jqPolicyitem.startdates}</td>
						</tr>
						<tr>
							<td class="active" align="center">交强险终止日期</td>
							<td>${detailedModel.jqPolicyitem.enddates}</td>
						</tr>
						<tr>
							<td class="active" align="center">上年商业险投保公司</td>
							<td>${detailedModel.carinfo.preinscode }</td>
						</tr>
						<tr>
							<td class="active" align="center">云查询保险公司</td>
							<td>${detailedModel.carinfo.cloudinscompany }</td>
						</tr>
						<tr>
							<td class="active" align="center">用户的备注</td>
							<td>${detailedModel.carinfo.noti }</td>
						</tr>
	 		</table>
			</div>
			
			  <div class="panel-heading">平台信息</div>
			<table class="table table-hover">
						<tr>
							<td class="col-md-2 active" align="center">投保类型</td>
							<td class="col-md-4">
							 ${otherInfo.firstInsureType}
							</td>
						</tr>
							<tr>
							<td class="active" align="center">平台商业险起保日期</td>
							<td>${otherInfo.systartdate }
						</tr>
						<tr>
							<td class="active" align="center">平台商业险终止日期</td>
							<td>${otherInfo.syenddate}</td>
						</tr>
							<tr>
							<td class="active" align="center">平台交强险起保日期</td>
							<td>${otherInfo.jqstartdate}</td>
						</tr>
						<tr>
							<td class="active" align="center">平台交强险终止日期</td>
							<td>${otherInfo.jqenddate}</td>
						</tr>
						<tr>
							<td class="active" align="center">商业险出险次数</td>
							<td>${otherInfo.syclaimtimes }</td>
						</tr>
						<tr>
							<td class="active" align="center">交强险出险次数</td>
							<td>${otherInfo.jqclaimtimes }</td>
						</tr>
						<tr>
							<td class="active" align="center">上年商业险投保公司</td>
							<td>${otherInfo.preinsShortname}</td>
						</tr>
						<tr>
							<td class="active" align="center">无赔款不浮动原因</td>
							<td>${otherInfo.loyaltyreasons}</td>
						</tr>
	 		</table>
			
			
			</div>		
			</div>
			<div class="col-md-7">
			<div class="panel panel-primary">
			  <div class="panel-heading">保险配置</div>
			  <#assign totalbusinessrisks = 0.0>
			  <#assign totalstrongrisk = 0.0>
			  <#assign totaltraveltax = 0.0>
			  <#assign totalother = 0.0>
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
								    		<#if selectelm.TYPE == "01" || insConfig.inskindcode == "VehicleDemageIns">
								    			保额：${insConfig.amount!""}${selectelm.VALUE.UNIT!""}
								    		<#else>
								    			${selectelm.VALUE.KEY!""}${selectelm.VALUE.UNIT!""}
								    		</#if>
								    		<#if selectelm_has_next>;</#if>
								    	</#list>
								    </td>
								    <td>${insConfig.amountprice!"--"}</td>
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
								    <td>${insConfig.amountprice!"0.0"}</td>
							    </tr>
				   			</#if>
				   		</#list>
			  		</tbody>
			 </table>
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
								    	<#list insConfig.selecteditem as selectelm>
								    		<#if selectelm.TYPE == "01">
								    			保额：${insConfig.amount!""}${selectelm.VALUE.UNIT!""}
								    		<#else>
								    			${selectelm.VALUE.KEY!""}${selectelm.VALUE.UNIT!""}
								    		</#if>
								    		<#if selectelm_has_next>;</#if>
								    	</#list>
								    </td>
								    <td>${insConfig.amountprice!"--"}</td>
							    </tr>
				   			</#if>
				   		</#list>
			  		</tbody>
			</table>
			<div class="panel panel-info">
 			  	<div class="panel-heading" style="text-align:center;">
 			  		<label>商业险保费：${carInsTaskInfo.insConfigInfo.discountBusTotalAmountprice?string('0.00')}元     交强险保费：${carInsTaskInfo.insConfigInfo.discountStrTotalAmountprice?string('0.00')}元    车船税：${carInsTaskInfo.insConfigInfo.discountCarTax?string('0.0')}元  其他：${carInsTaskInfo.insConfigInfo.otherAmountprice?string('0.00')}元</label>
 			  		<br><label style=";color:red;">保费总额：${(carInsTaskInfo.insConfigInfo.discountBusTotalAmountprice+carInsTaskInfo.insConfigInfo.discountStrTotalAmountprice+carInsTaskInfo.insConfigInfo.discountCarTax+carInsTaskInfo.insConfigInfo.otherAmountprice)?string('0.00') }元</label>
			  	</div> 
 			</div>
 			<table class="table table-bordered">
 				<tr style="color:red;">
				 	<td class="bgg col-md-2" style="text-align:left;">商业险折扣率：${carInsTaskInfo.insConfigInfo.busdiscountrate}</td>
				 	<td class="bgg col-md-2" style="text-align:left;">交强险折扣率：${carInsTaskInfo.insConfigInfo.strdiscountrate}</td>
				 	<td class="bgg col-md-2" style="text-align:left;">商业险赔付率：</td>
				</tr>
 			</table>
			</div>
			<div style="margin-top:5px;display: none">
				<button class="btn btn-primary mybtn" id="querryLastPrice" onclick="javascript:window.parent.openDialogForCM('/cm/order/lastPriceInfo?instanceid=${detailedModel.insbOrder.taskid }&inscomcode=${detailedModel.insbProvider.prvcode}')">查看最后一次报价</button>
			</div>
		</div>
		</div>
    </div>
  </div>
  </div>
 <div class="panel panel-default">
    <div class="panel-heading" role="tab" id="headingAgent">
      <h4 class="panel-title">
        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1" href="#collapseAgent" aria-expanded="false" aria-controls="collapseAgent"> 保单信息
        </a>
      </h4>
    </div>
    <div id="collapseAgent" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingAgent">
      <div class="panel-body">
        <div class="row">
			<div class="col-md-12">
					<table class="table table-bordered">
			  			<tr>
							<td class="col-md-2" align="center">商业险保单号</td>
							<td class="col-md-10">${detailedModel.insbPolicyitem.policyno }</td>
						</tr>
						<tr>
							<td align="center">交强险保单号</td>
							<td>${detailedModel.jqPolicyitem.policyno }</td>
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
        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1" href="#collapseAgent" aria-expanded="false" aria-controls="collapseAgent"> 支付信息
        </a>
      </h4>
    </div>
    <div id="collapseAgent" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingAgent">
      <div class="panel-body">
  <div class="row">
			<div class="col-md-12">
					<table class="table table-bordered">
			  			<tr>
							<td class="col-md-2" align="center">订单号</td>
							<td class="col-md-10">${detailedModel.insbOrder.orderno }</td>
						</tr>
			  			<tr>
							<td class="col-md-2" align="center">订单状态</td>
							<td class="col-md-10">
								${codename!'' }
							</td>
						</tr>
			  			<tr>
							<td class="col-md-2" align="center">支付方式</td>
							<td class="col-md-10">
								${paytypes!'' }
							</td>
						</tr>
						<tr>
							<td align="center">配送费</td>
							<td>${detailedModel.insbOrder.totaldeliveryamount }</td>
						</tr>
						<tr>
							<td align="center">创建时间</td>
							<td><#if detailedModel.insbOrder.createtime??>${detailedModel.insbOrder.createtime?string("yyyy-MM-dd HH:mm:ss") }</#if></td>
						</tr>
						<tr>
							<td align="center">订单标价总金额</td>
							<td>${detailedModel.insbOrder.totalproductamount }</td>
						</tr>
						<tr>
							<td align="center">优惠金额</td>
							<td>${detailedModel.insbOrder.totalpromotionamount }</td>
						</tr>
						<tr>
							<td align="center">实付金额</td>
							<td>${insbOrderpayment.amount }</td>
						</tr>
			 	</table>
			</div>
  	</div>
  	</div>
  	</div>
  	</div>
  	 <div class="panel panel-default"  <#if "${detailedModel.insbOrderdelivery.delivetype }"=="">style="display:none;"</#if>>
    <div class="panel-heading" role="tab" id="headingAgent">
      <h4 class="panel-title">
        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1" href="#collapseAgent" aria-expanded="false" aria-controls="collapseAgent"><span <#if "${detailedModel.insbOrderdelivery.delivetype }"=="">style="visibility:hidden"</#if>> 配送信息</span>
        </a>
      </h4>
    </div>
    <div id="collapseAgent" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingAgent">
      <div class="panel-body">
  	<div class="row">
			<div class="col-md-12">
					<table class="table table-bordered">
			  			<tr>
							<td class="col-md-2" <#if "${detailedModel.insbOrderdelivery.delivetype }"=="">style="display:none;align:center;"<#else> align="center"</#if>>配送方式</td>
							<td class="col-md-10" <#if "${detailedModel.insbOrderdelivery.delivetype }"=="">style="display:none;"</#if>>
								<#if "${detailedModel.insbOrderdelivery.delivetype }"=="0">	自取
								<#elseif "${detailedModel.insbOrderdelivery.delivetype }"=="1">快递
								<#elseif "${detailedModel.insbOrderdelivery.delivetype }"=="3">电子保单
								<#else>${detailedModel.insbOrderdelivery.delivetype }
								</#if>
							</td>
						</tr>
						<tr>
							<td class="col-md-2" <#if "${detailedModel.insbOrderdelivery.delivetype }"!="0">style="display:none;align:center;"<#else> align="center"</#if>>自取网点</td>
							<td  class="col-md-10" <#if "${detailedModel.insbOrderdelivery.delivetype }"!="0">style="display:none;"</#if>>${detailedModel.address}${detailedModel.addresscodename }</td>
						</tr>
						<tr>
							<td class="col-md-2" <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;align:center;"<#else> align="center"</#if>>物流公司</td>
							<td  class="col-md-10" <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${detailedModel.codenamestyle }</td>
						</tr>
						<tr >
							<td class="col-md-2"<#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;align:center;"<#else> align="center"</#if>>配送编号</td>
							<td class="col-md-10" <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${detailedModel.insbOrderdelivery.tracenumber }</td>
						</tr>
						<tr>
							<td class="col-md-2" <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;align:center;"<#else> align="center"</#if>>配送方</td>
							<td class="col-md-10" <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${detailedModel.insbOrderdelivery.deliveryside }</td>
						</tr>
						<tr>
							<td class="col-md-2" <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;align:center;"<#else> align="center"</#if>>收件人姓名</td>
							<td class="col-md-10" <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${detailedModel.insbOrderdelivery.recipientname }</td>
						</tr>
						<tr>
							<td class="col-md-2" <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;align:center;"<#else> align="center"</#if>>收件人手机号</td>
							<td  class="col-md-10" <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${detailedModel.insbOrderdelivery.recipientmobilephone }</td>
						</tr>
						<tr>
							<td class="col-md-2"<#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;align:center;"<#else> align="center"</#if>>快递收件地址</td>
							<td class="col-md-10" <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${detailedModel.orderdeliveryaddress }</td>
						</tr>
						<tr>
							<td class="col-md-2" <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;align:center;"<#else> align="center"</#if>>邮政编码</td>
							<td class="col-md-10" <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${detailedModel.insbOrderdelivery.zip }</td>
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
        <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1" href="#collapseAgent" aria-expanded="false" aria-controls="collapseAgent"> 备注信息
        </a>
      </h4>
    </div>
    <div id="collapseAgent" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingAgent">
      <div class="panel-body">
  	<div class="row">
			<div class="col-md-12">
					<table class="table table-bordered">
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
			</div>
  	</div>
  	</div>
  	<br/>
        <div class="panel panel-default">
            <div class="panel-heading" role="tab" id="headingAgentCommission">
                <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1" href="#collapseAgent" aria-expanded="false" aria-controls="collapseAgent"> 酬金信息
                    </a>
                </h4>
            </div>
            <div id="collapseAgent" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingAgentCommission">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-12">
                            <table class="table table-bordered">
                                <tr>
                                    <td>
                                        所获佣金（元）
                                    </td>
                                    <td>
                                        佣金类型
                                    </td>
                                    <td>
                                        佣金率
                                    </td>
                                    <td>
                                        佣金描述
                                    </td>
                                </tr>
							<#list commissionList as commission>
                                <tr>
                                    <td>
									${commission.commission}
                                    </td>
                                    <td>
									${commission.type}
                                    </td>
                                    <td>
									${commission.rate}
                                    </td>
                                    <td>
                                    </td>
                                </tr>
							</#list>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <br/>

        <div class="panel panel-default">
            <div class="panel-heading" role="tab" id="headingAgentPrize">
                <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1" href="#collapseAgent" aria-expanded="false" aria-controls="collapseAgent"> 奖券信息
                    </a>
                </h4>
            </div>
            <div id="collapseAgent" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingAgentPrize">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-12">
                            <table class="table table-bordered">
                                <tr>
                                    <td>
                                       奖品名称
                                    </td>
                                    <td>
                                        奖品类型
                                    </td>
                                    <td>
                                       活动名称
                                     </td>
                                    <td>
                                       金额（元）
                                    </td>
                                    <td>
                                       领取时间
                                    </td>
                                    <td>
                                       使用时间
                                    </td>
                                    <td>
                                        生效时间
                                    </td>
                                    <td>
                                        失效时间
                                    </td>
                                </tr>
							<#list prizeInfoList as getPrizeInfo>
								<tr>
									<td>
									${getPrizeInfo.prizename}
									</td>
                                    <td>
									${getPrizeInfo.prizetype}
                                    </td>
									<td>
									${getPrizeInfo.activityname}
									</td>
                                    <td>
									${getPrizeInfo.counts}
                                    </td>
                                    <td>
									${getPrizeInfo.gettime}
                                    </td>
                                    <td>
									${getPrizeInfo.usetime}
                                    </td>
                                    <td>
									${getPrizeInfo.effectivetime}
                                    </td>
                                    <td>
									${getPrizeInfo.terminaltime}
                                    </td>
								</tr>
							</#list>
							</table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <br/>

  	<div class="row">
  		<div class="col-md-12">
			<table class="table table-bordered">
				<tr>
					<td>
	  					<input class="btn btn-primary" type="submit" id="mbackbutton" value="返回">
					</td>
				</tr>
			</table>
		</div>
  	</div>
</div>
</body>
</html>
