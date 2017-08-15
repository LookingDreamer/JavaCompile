<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>订单详情</title>
    <link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
    <script data-ver="${jsver}" data-main="${staticRoot}/js/load"
            src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
    <script type="text/javascript">
        requirejs(["cm/warranty/orderDetails", "chn/channelTree"]);
    </script>
</head>
<body>
<!--车险任务详细信息弹出框-->
<div class="modal-content" id="modal-content" style="width: 100%;">
    <div class="modal-dialog ui-draggable" style="width: 100%;">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                    class="sr-only">Close</span></button>
            <h3 class="modal-title">订单详情</h3>
        </div>
        <div class="modal-body"><!--加入此标签弹出框会有内边距-->
            <div class="container-fluid">
                <div class="panel-group" id="accordionAgent" role="tablist" aria-multiselectable="true">
                    <div class="panel panel-default">
                        <div class="panel-heading" role="tab" id="headingAgent">
                            <h4 class="panel-title">
                                <a class="collapsed" role="button" data-toggle="collapse" data-parent="#headingAgent"
                                   href="#collapseAgent" aria-expanded="true" aria-controls="collapseAgent">代理人信息</a>
                            </h4>
                        </div>
                        <div id="collapseAgent" class="panel-collapse collapse in" role="tabpanel"
                             aria-labelledby="headingAgent">
                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-md-12">
                                        <table class="table table-bordered">
                                            <tr>
                                                <td class="col-md-2 active">代理人姓名</td>
                                                <td class="col-md-2">${agent.name }</td>
                                                <td class="col-md-2 active">工号</td>
                                                <td class="col-md-2">${agent.jobnum }</td>
                                            </tr>
                                            <tr>
                                                <td class="active">执业证/展示证号码</td>
                                                <td>${agent.licenseno }</td>
                                                <td class="active">身份证号</td>
                                                <td>${agent.idno }</td>
                                            </tr>

                                            <tr>
                                                <td class="active">出单网点</td>
                                                <td>${agent.licenseno }</td>
                                                <td class="active">联系电话</td>
                                                <td>${agent.phone }</td>
                                            </tr>

                                            <tr>
                                                <td class="active">所属机构</td>
                                                <td colspan="1">${dept.comname }</td>
                                                <td class="active">渠道来源</td>
                                                <td>${agent.purchaserchannel}</td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel panel-default">
                        <div class="panel-heading" role="tab" id="headingCar">
                            <h4 class="panel-title">
                                <a class="collapsed" role="button" data-toggle="collapse" data-parent="#headingCar"
                                   href="#collapseCar" aria-expanded="true" aria-controls="collapseCar">车辆信息</a>
                            </h4>
                        </div>
                        <div id="collapseCar" class="panel-collapse collapse in" role="tabpanel"
                             aria-labelledby="headingTask">
                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-md-12">
                                        <table class="table table-bordered">
                                            <tr>
                                                <td class="col-md-2 active">投保地区</td>
                                                <td class="col-md-2"></td>
                                                <td class="col-md-2 active">发动机号</td>
                                                <td class="col-md-2">${car.engineno }</td>
                                            </tr>
                                            <tr>
                                                <td class="col-md-2 active">车牌号</td>
                                                <td class="col-md-2">${car.carlicenseno }</td>
                                                <td class="col-md-2 active">车辆识别号</td>
                                                <td class="col-md-2">${car.vincode }</td>
                                            </tr>
                                            <tr>
                                                <td class="active">车主</td>
                                                <td>${car.ownername }</td>
                                                <td class="active">车辆初登日期</td>
                                                <td>
                                                ${car.registdate?datetime?string("yyyy-MM-dd")}
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="col-md-2 active">车型信息</td>
                                                <td class="col-md-2">${car.standardfullname }</td>
                                                <td class="col-md-2 active">原厂质保期</td>
                                                <td class="col-md-2">${car.origialwarrantyperiod } 年</td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-default">
                        <div class="panel-heading" role="tab" id="headingPerson">
                            <h4 class="panel-title">
                                <a class="collapsed" role="button" data-toggle="collapse" data-parent="#headingPerson"
                                   href="#collapsePerson" aria-expanded="true" aria-controls="collapseCar">关系人信息</a>
                            </h4>
                        </div>
                        <div id="collapsePerson" class="panel-collapse collapse in" role="tabpanel"
                             aria-labelledby="headingTask">
                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-md-12">
                                        <table class="table table-bordered">
                                            <tr>
                                                <td class="col-md-2 active">被保人</td>
                                                <td class="col-md-2">${insured.name}</td>
                                                <td class="col-md-2 active">身份证</td>
                                                <td class="col-md-2">${insured.idcardno }</td>
                                            </tr>
                                            <tr>
                                                <td class="col-md-2 active">投保人</td>
                                                <td class="col-md-2">${applicant.name }</td>
                                                <td class="col-md-2 active">车辆识别号</td>
                                                <td class="col-md-2">${applicant.idcardno }</td>
                                            </tr>
                                            <tr>
                                                <td class="active">权益索取人</td>
                                                <td>${legalrightclaim.name }</td>
                                                <td class="active">身份证</td>
                                                <td>
                                                ${legalrightclaim.idcardno}
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="col-md-2 active">联系人</td>
                                                <td class="col-md-2">${carownerinfo.name }</td>
                                                <td class="col-md-2 active">原厂质保期</td>
                                                <td class="col-md-2">${carownerinfo.idcardno }</td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="panel panel-default">
                        <div class="panel-heading" role="tab" id="headingPlan">
                            <h4 class="panel-title">
                                <a class="collapsed" role="button" data-toggle="collapse" data-parent="#headingPlan"
                                   href="#collapsePlan" aria-expanded="true" aria-controls="collapseAgent">延保方案</a>
                            </h4>
                        </div>
                        <div id="collapsePlan" class="panel-collapse collapse in" role="tabpanel"
                             aria-labelledby="collapseAgent">
                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-md-12">
                                        <table class="table table-bordered">
                                            <tr>
                                                <td class="col-md-2 active">保障范围</td>
                                                <td class="col-md-2">
                                                <#switch policy.extendwarrantytype>
                                                    <#case 0>
                                                        全面保修
                                                        <#break>
                                                    <#case 1>
                                                        三大部件保修
                                                        <#break>
                                                    <#case 2>
                                                        两大部件保修
                                                        <#break>
                                                    <#default>
                                                        未知
                                                </#switch>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="col-md-2 active">延长保修期</td>
                                                <td class="col-md-2">
                                                ${policy.startdate?datetime?string("yyyy-MM-dd")} 到
                                                ${policy.enddate?datetime?string("yyyy-MM-dd")}
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>

                    <div class="panel panel-default">
                        <div class="panel-heading" role="tab" id="accordionPayment">
                            <h4 class="panel-title">
                                <a data-toggle="collapse" data-parent="#accordionPayment"
                                   href="#collapsePayment">支付信息</a>
                            </h4>
                        </div>
                        <div id="collapsePayment" class="panel-collapse collapse in" role="tabpanel"
                             aria-labelledby="accordionPayment">
                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-md-12">
                                        <table class="table table-bordered">
                                            <tr>
                                                <td class="col-md-2 active">订单号</td>
                                                <td class="col-md-2">${order.orderno }</td>
                                            </tr>
                                            <tr>
                                                <td class="col-md-2 active">支付状态</td>
                                                <td class="col-md-2">
                                                <#switch order.orderstatus>
                                                    <#case 0>
                                                        待支付
                                                        <#break>
                                                    <#case 1>
                                                        待承保
                                                        <#break>
                                                    <#case 2>
                                                        承保成功
                                                        <#break>
                                                    <#case 3>
                                                        订单锁定
                                                        <#break>
                                                    <#case 4>
                                                        承保失败
                                                        <#break>
                                                    <#default>
                                                        未知状态
                                                </#switch>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="col-md-2 active">支付方式</td>
                                                <td class="col-md-2">${order.paymethod }</td>
                                            </tr>
                                            <tr>
                                                <td class="col-md-2 active">配送费</td>
                                                <td class="col-md-2">${order.totaldeliveryamount }</td>
                                            </tr>
                                            <tr>
                                                <td class="col-md-2 active">创建时间</td>
                                                <td class="col-md-2">
                                                ${order.createtime?datetime?string("yyyy-MM-dd HH:mm:ss")}
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="col-md-2 active">订单标价总金额</td>
                                                <td class="col-md-2">${order.totalproductamount }</td>
                                            </tr>
                                            <tr>
                                                <td class="col-md-2 active">优惠金额</td>
                                                <td class="col-md-2">${order.totalpromotionamount}</td>
                                            </tr>
                                            <tr>
                                                <td class="col-md-2 active">实付金额</td>
                                                <td class="col-md-2">${order.totalpaymentamount }</td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>

                    <div class="panel panel-default">
                        <div class="panel-heading" role="tab" id="headingOther">
                            <h4 class="panel-title">
                                <a class="collapsed" role="button" data-toggle="collapse" data-parent="#headingOther"
                                   href="#collapseOther" aria-expanded="true" aria-controls="collapseAgent">其他信息</a>
                            </h4>
                        </div>
                        <div id="collapseOther" class="panel-collapse collapse in" role="tabpanel"
                             aria-labelledby="headingTask">
                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-md-12">
                                        <table class="table table-bordered">
                                            <tr>
                                                <td class="col-md-2 active">电子保单接收邮箱</td>
                                                <td class="col-md-2">${order.invoiceemail }</td>
                                            </tr>
                                            <tr>
                                                <td class="col-md-2 active">是否需要电子发票</td>
                                                <td class="col-md-2">${order.needinvoice?string('是','否') }</td>
                                            </tr>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal-footer">
            <button type="button" class="btn btn-default"
                    data-dismiss="modal">关闭
            </button>

        </div>
    </div>
</div>
</body>
</html>
