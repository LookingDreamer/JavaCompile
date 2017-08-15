<#include "./macro/isTransferredCar.ftl"/>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>订单详情</title>
    <link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
    <script data-ver="${jsver}" data-main="${staticRoot}/js/load"
            src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
    <script type="text/javascript">
        requirejs(["zzbconf/insbOrderManagement"]);
    </script>
</head>
<body>
<div class="container-fluid">
    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
        <div class="panel panel-default">
            <div class="panel-heading" role="tab" id="headingTask">
                <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1"
                       href="#collapseTask" aria-expanded="true" aria-controls="collapseTask">任务摘要
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
            <div class="panel-heading" role="tab" id="headingAgent">
                <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1"
                       href="#collapseAgent" aria-expanded="false" aria-controls="collapseAgent"> 代理人信息
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
                                    <td>${detailedModel.agent.purchaserchannel}</td>
                                    <td class="active">mini用户工号</td>
                                    <td>${detailedModel.miniagentcode}</td>
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
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1"
                       href="#collapseImage" aria-expanded="false" aria-controls="collapseImage">影像信息
                    </a>
                </h4>
            </div>


            <div id="collapseImage" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingImage">
                <div class="panel-body">
                    <div id="menuPic">

                    <#list imginfo as img>
                        <a href='javascript:window.parent.openImgView("common/insurancepolicyinfo/previewPicture?pictureId=${img.pictureId}&subInstanceId=${subInstanceId}&taskid=${taskid}")'>
                            <img style="width: 120px; height: 120px; text-align:center; vertical-align:middle;line-height:120px; border: 1px solid #ccc"
                                 alt="${img.codename }" src="${img.smallfilepath }">
                        </a>
                    </#list>

                    </div>
                </div>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading" role="tab" id="headingQuote">
                <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1"
                       href="#collapseQuote" aria-expanded="false" aria-controls="collapseQuote">报价信息
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
                                            <td class="col-md-4">${detailedModel.province }${detailedModel.city }</td>
                                        </tr>
                                        <tr>
                                            <td class="active">车牌</td>
                                            <td>${detailedModel.carinfohis.carlicenseno }</td>
                                        </tr>
                                        <tr>
                                            <td class="active">车主</td>
                                            <td>
                                                <a href="javascript:window.parent.openDialog('policyitem/querypeopleinfo?show=carower&taskid=${detailedModel.carinfohis.taskid}&inscomcode=${detailedModel.insbProvider.id }');">${detailedModel.carinfohis.ownername }</a>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="active">车型</td>
                                            <td>
                                                <a href="javascript:window.parent.openDialog('policyitem/querycarinfo?taskid=${detailedModel.carinfohis.taskid}&inscomcode=${detailedModel.insbProvider.id }');">${detailedModel.carinfohis.standardfullname }</a>
                                            </td>
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
                                            <td><@isTransferredCar value=detailedModel.carinfohis.isTransfercar /></td>
                                        </tr>
                                    <#--
                                    <tr>
                                        <td class="active">平均行驶里程</td>
                                        <td>${detailedModel.carinfohis.mileage }</td>
                                    </tr>
                                    <tr>
                                        <td class="active">行驶区域</td>
                                        <td>${detailedModel.carinfohis.drivingarea }</td>
                                    </tr>
                                    -->
                                    </table>
                                </div>
                                <div class="panel panel-info">
                                    <div class="panel-heading">关系人信息</div>
                                    <table class="table table-hover">
                                        <tr>
                                            <td class="col-md-2 active">被保险人</td>
                                            <td class="col-md-4">
                                                <a href="javascript:window.parent.openDialog('policyitem/querypeopleinfo?show=insure&taskid=${taskid}&inscomcode=${detailedModel.insbProvider.id }');">${detailedModel.recognizeePolicyitem.beibaorenname }</a>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="active">投保人姓名</td>
                                            <td>
                                                <a href="javascript:window.parent.openDialog('policyitem/querypeopleinfo?show=policy&taskid=${taskid}&inscomcode=${detailedModel.insbProvider.id }');">${detailedModel.insurePolicyitem.toubaorenname }</a>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="active">权益索赔人</td>
                                            <td>
                                                <a href="javascript:window.parent.openDialog('policyitem/querypeopleinfo?show=shouyiren&taskid=${taskid}&inscomcode=${detailedModel.insbProvider.id }');">${detailedModel.benefitPolicyitem.shouyirenname }</a>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="active">联系人</td>
                                            <td>
                                                <a href="javascript:window.parent.openDialog('policyitem/querypeopleinfo?show=contacts&taskid=${taskid}&inscomcode=${detailedModel.insbProvider.id }');">${detailedModel.contactsPolicyitem.contactsname }</a>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                                <div class="panel panel-info">
                                    <div class="panel-heading">车辆信息</div>
                                    <table class="table table-hover">
                                        <tr>
                                            <td class="col-md-2 active">是否指定驾驶人</td>
                                            <td class="col-md-4">
                                            <#if "${detailedModel.carinfo.specifydriver }" == "是">
                                                <a href="javascript:window.parent.openDialog('policyitem/querydriverinfo?taskid=${detailedModel.carinfo.taskid}');">${detailedModel.carinfo.specifydriver }</a>
                                            <#else>
                                                ${detailedModel.carinfo.specifydriver!"" }
                                            </#if>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="active">商业险起保日期</td>
                                            <td>${detailedModel.insbPolicyitem.startdates }
                                        </tr>
                                        <tr>
                                            <td class="active">商业险终止日期</td>
                                            <td>${detailedModel.insbPolicyitem.enddates}</td>
                                        </tr>
                                        <tr>
                                            <td class="active">交强险起保日期</td>
                                            <td>${detailedModel.jqPolicyitem.startdates}</td>
                                        </tr>
                                        <tr>
                                            <td class="active">交强险终止日期</td>
                                            <td>${detailedModel.jqPolicyitem.enddates}</td>
                                        </tr>
                                        <tr>
                                            <td class="active">上年商业险投保公司</td>
                                            <td>${detailedModel.carinfo.preinscode }</td>
                                        </tr>
                                        <tr <#if otherInfo.preinsSypolicyno == "">style="display: none;" </#if>>
                                            <td class="active">上年商业险保单号</td>
                                            <td>${otherInfo.preinsSypolicyno }</td>
                                        </tr>
                                        <tr>
                                            <td class="active">云查询保险公司</td>
                                            <td>${detailedModel.carinfo.cloudinscompany }</td>
                                        </tr>
                                        <tr>
                                            <td class="active">用户的备注</td>
                                            <td>${detailedModel.carinfo.noti }</td>
                                        </tr>
                                    </table>
                                </div>
                                <div class="panel panel-info">
                                    <div class="panel-heading">补充数据项</div>
                                    <table class="table table-hover">
                                        <#assign showcolumnfirst=2 showcolumnsecond=4 showcolumnfirstclass="active">
                                        <#include "cm/common/showSupplyParam.ftl" />
                                    </table>
                                </div>

                                <div class="panel-heading">平台信息</div>
                                <table class="table table-hover">
                                    <tr>
                                        <td class="col-md-2 active">投保类型</td>
                                        <td class="col-md-4">
                                        ${otherInfo.firstInsureType}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="active">平台商业险起保日期</td>
                                        <td>${otherInfo.systartdate }
                                    </tr>
                                    <tr>
                                        <td class="active">平台商业险终止日期</td>
                                        <td>${otherInfo.syenddate}</td>
                                    </tr>
                                    <tr>
                                        <td class="active">平台交强险起保日期</td>
                                        <td>${otherInfo.jqstartdate}</td>
                                    </tr>
                                    <tr>
                                        <td class="active">平台交强险终止日期</td>
                                        <td>${otherInfo.jqenddate}</td>
                                    </tr>
                                    <tr>
                                        <td class="active">商业险出险次数</td>
                                        <td>${otherInfo.syclaimtimes }</td>
                                    </tr>
                                    <tr>
                                        <td class="active">交强险出险次数</td>
                                        <td>${otherInfo.jqclaimtimes }</td>
                                    </tr>
                                    <tr>
                                        <td class="active">上年商业险投保公司</td>
                                        <td>${otherInfo.preinsShortname}</td>
                                    </tr>
                                    <tr>
                                        <td class="active">无赔款不浮动原因</td>
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
                                        <label>
                                            商业险保费：${carInsTaskInfo.insConfigInfo.discountBusTotalAmountprice?string('0.00')}元
                                            &nbsp;
                                            交强险保费：${carInsTaskInfo.insConfigInfo.discountStrTotalAmountprice?string('0.00')}元
                                            &nbsp;
                                            车船税：${carInsTaskInfo.insConfigInfo.discountCarTax?string('0.00')}元 &nbsp;
                                            其他：${carInsTaskInfo.insConfigInfo.otherAmountprice?string('0.00')}元
                                        </label>
                                        <br><label
                                            style=";color:red;">保费总额：${(carInsTaskInfo.insConfigInfo.discountBusTotalAmountprice+carInsTaskInfo.insConfigInfo.discountStrTotalAmountprice+carInsTaskInfo.insConfigInfo.discountCarTax+carInsTaskInfo.insConfigInfo.otherAmountprice)?string('0.00')}元</label>
                                    </div>
                                </div>
                                <table class="table table-bordered">
                                    <tr style="color:red;">
                                        <td class="bgg col-md-2" style="text-align:left;">
                                            商业险折扣率：${carInsTaskInfo.insConfigInfo.busdiscountrate}</td>
                                        <td class="bgg col-md-2" style="text-align:left;">
                                            交强险折扣率：${carInsTaskInfo.insConfigInfo.strdiscountrate}</td>
                                        <td class="bgg col-md-2" style="text-align:left;">商业险赔付率：</td>
                                    </tr>
                                </table>
                            </div>
                            <div style="margin-top:5px;">
                                <button class="btn btn-primary mybtn" id="querryLastPrice"
                                        onclick="javascript:window.parent.openDialogForCM('/cm/order/lastPriceInfo?instanceid=${detailedModel.insbOrder.taskid }&inscomcode=${detailedModel.insbProvider.prvcode}')">
                                    查看最后一次报价
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading" role="tab" id="headingAgent">
                <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1"
                       href="#collapseAgent" aria-expanded="false" aria-controls="collapseAgent"> 保单信息
                    </a>
                </h4>
            </div>
            <div id="collapseAgent" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingAgent">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-12">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="col-md-2">商业险保单号</td>
                                    <td class="col-md-10">${detailedModel.insbPolicyitem.policyno }</td>
                                </tr>
                                <tr>
                                    <td>交强险保单号</td>
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
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1"
                       href="#collapseAgent" aria-expanded="false" aria-controls="collapseAgent"> 支付信息
                    </a>
                    <!-- 只有二支(21)且为银联(30)和众安分期保(28)的单才显示 -->
                <#if paymentInfo.taskcode == 21>
                    <#if paymentInfo.paychannelid == 28 || paymentInfo.paychannelid == 30>
                        <a class="btn btn-primary"
                           href="javascript:showqueryorderresult('${paymentInfo.secpayorderrequrl}', '${paymentInfo.payflowno}', '${paymentInfo.payorderno}', '${paymentInfo.paychannelid}','${detailedModel.insurePolicyitem.toubaoidcardno}');">
                            查询订单结果
                        </a>
                    </#if>
                </#if>
                </h4>
            </div>
            <div id="collapseAgent" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingAgent">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-12">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="col-md-2">订单号</td>
                                    <td class="col-md-10">${detailedModel.insbOrder.orderno }</td>
                                </tr>
                                <tr>
                                    <td class="col-md-2">订单状态</td>
                                    <td class="col-md-10">
                                    ${codename!'' }
                                    </td>
                                </tr>
                                <tr>
                                    <td class="col-md-2">支付方式</td>
                                    <td class="col-md-10">
                                    ${paytypes!'' }
                                    </td>
                                </tr>
                                <tr>
                                    <td>配送费</td>
                                    <td>${detailedModel.insbOrder.totaldeliveryamount }</td>
                                </tr>
                                <tr>
                                    <td>创建时间</td>
                                    <td><#if detailedModel.insbOrder.createtime??>${detailedModel.insbOrder.createtime?string("yyyy-MM-dd HH:mm:ss") }</#if></td>
                                </tr>
                                <tr>
                                    <td>订单标价总金额</td>
                                    <td>${detailedModel.insbOrder.totalproductamount }</td>
                                </tr>
                                <tr>
                                    <td>优惠金额</td>
                                    <td>${detailedModel.insbOrder.totalpromotionamount }</td>
                                </tr>
                                <tr>
                                    <td>实付金额</td>
                                    <td>${detailedModel.insbOrder.totalpaymentamount }</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="panel panel-default"
             <#if "${detailedModel.insbOrderdelivery.delivetype }"=="">style="display:none;"</#if>>
            <div class="panel-heading" role="tab" id="headingAgent">
                <h4 class="panel-title">
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1"
                       href="#collapseAgent" aria-expanded="false" aria-controls="collapseAgent"><span
                    <#if "${detailedModel.insbOrderdelivery.delivetype }"=="">style="visibility:hidden"</#if>> 配送信息</span>
                    </a>
                </h4>
            </div>
            <div id="collapseAgent" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingAgent">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-12">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="col-md-2"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"=="">style="display:none;"<#else>
                                        </#if>>配送方式
                                    </td>
                                    <td class="col-md-10"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"=="">style="display:none;"</#if>>
                                    <#if "${detailedModel.insbOrderdelivery.delivetype }"=="0">    自取
                                    <#elseif "${detailedModel.insbOrderdelivery.delivetype }"=="1">快递
                                    <#elseif "${detailedModel.insbOrderdelivery.delivetype }"=="3">电子保单
                                    <#else>${detailedModel.insbOrderdelivery.delivetype }
                                    </#if>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="col-md-2"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="0">style="display:none;"<#else>
                                        </#if>>自取网点
                                    </td>
                                    <td class="col-md-10"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="0">style="display:none;"</#if>>${detailedModel.address}${detailedModel.addresscodename }</td>
                                </tr>
                                <tr>
                                    <td class="col-md-2"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"<#else>
                                        </#if>>物流公司
                                    </td>
                                    <td class="col-md-10"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${detailedModel.codenamestyle }</td>
                                </tr>
                                <tr>
                                    <td class="col-md-2"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"<#else>
                                        </#if>>配送编号
                                    </td>
                                    <td class="col-md-10"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${detailedModel.insbOrderdelivery.tracenumber }</td>
                                </tr>
                                <tr>
                                    <td class="col-md-2"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"<#else>
                                        </#if>>配送方
                                    </td>
                                    <td class="col-md-10"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${detailedModel.insbOrderdelivery.deliveryside }</td>
                                </tr>
                                <tr>
                                    <td class="col-md-2"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"<#else>
                                        </#if>>收件人姓名
                                    </td>
                                    <td class="col-md-10"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${detailedModel.insbOrderdelivery.recipientname }</td>
                                </tr>
                                <tr>
                                    <td class="col-md-2"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"<#else>
                                        </#if>>收件人手机号
                                    </td>
                                    <td class="col-md-10"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${detailedModel.insbOrderdelivery.recipientmobilephone }</td>
                                </tr>
                                <tr>
                                    <td class="col-md-2"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"<#else>
                                        </#if>>快递收件地址
                                    </td>
                                    <td class="col-md-10"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${detailedModel.orderdeliveryaddress }</td>
                                </tr>
                                <tr>
                                    <td class="col-md-2"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"<#else>
                                        </#if>>邮政编码
                                    </td>
                                    <td class="col-md-10"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${detailedModel.insbOrderdelivery.zip }</td>
                                </tr>
                                <tr>
                                    <td class="col-md-2"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"<#else>
                                        </#if>>寄件人单位名称
                                    </td>
                                    <td class="col-md-10"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${senderchannel}</td>
                                </tr>
                                <tr>
                                    <td class="col-md-2"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"<#else>
                                        </#if>>寄件人姓名
                                    </td>
                                    <td class="col-md-10"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${sendername}</td>
                                </tr>
                                <tr>
                                    <td class="col-md-2"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"<#else>
                                        </#if>>寄件人地址
                                    </td>
                                    <td class="col-md-10"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${senderaddress }</td>
                                </tr>
                                <tr>
                                    <td class="col-md-2"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"<#else>
                                        </#if>>寄件人电话
                                    </td>
                                    <td class="col-md-10"
                                        <#if "${detailedModel.insbOrderdelivery.delivetype }"!="1">style="display:none;"</#if>>${senderphone}</td>
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
                    <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion1"
                       href="#collapseAgent" aria-expanded="false" aria-controls="collapseAgent"> 备注信息
                    </a>
                </h4>
            </div>
            <div id="collapseAgent" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingAgent">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-12">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="col-md-2">给用户的备注</td>
                                    <td class="col-md-10">
                                    <#list usercomment as usercomment>
									${usercomment.commentcontent}
								</#list>
                                    </td>
                                </tr>
                                <tr>
                                    <td>给操作员的备注</td>
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
        <div class="row">
            <div class="col-md-12">
                <table class="table table-bordered">
                    <tr>
                        <td>
                            <input class="btn btn-primary" type="submit" id="backbutton" value="返回">
                        </td>
                    </tr>
                </table>
            </div>
        </div>

    <#include "cm/common/querySecondPayInfo.ftl" />
    </div>
</body>
</html>
