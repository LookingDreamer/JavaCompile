<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>延保订单查询</title>
    <link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>
    <script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
    <script type="text/javascript">
        requirejs([ "cm/warranty/orderQuery","chn/channelTree" ]);
    </script>
    <style type="text/css">
        div#selectStype a {
            position:relative;
            left:15px;
            top:10px;
        }
    </style>
</head>
<body>
<div class="container-fluid" style="margin-bottom:30px">

    <div class="panel panel-default m-bottom-5" id="superquerypanel">
        <div class="panel-heading padding-5-5">
            <label>查询订单</label>
            <button class="close" type="button" id="superquerypanelclose">&times;</button>
        </div>
        <div class="panel-body">
            <form role="form" id="cartasksuperqueryform">
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group col-md-4  form-inline">
                            <label for="exampleInputOrderNo">订单号</label>
                            <input type="text" class="form-control" id="orderNo" name="orderNo" placeholder=""/>
                        </div>
                        <div class="form-group col-md-4 form-inline">
                            <label for="exampleInputPlateNumber">车牌号</label>
                            <input type="text" class="form-control m-left-2" id="plateNumber" name="plateNumber " placeholder=""/>
                        </div>
                        <div class="form-group col-md-4 form-inline">
                            <label for="exampleInpuInsuredName">被保人</label>
                            <input type="text" class="form-control" id="insuredName" name="insuredName" placeholder=""/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group col-md-4 form-inline">
                            <label for="exampleInputOrderStatus">订单状态</label>
                            <select name="orderStatus" id="orderStatus" class="form-control" style="width:150px;">
                                <option value="">全部</option>
                                <option value="0">未支付</option>
                                <option value="1">待承保</option>
                                <option value="2">已承保</option>
                            </select>
                        </div>
                        <div class="form-group col-md-4 form-inline">
                            <label for="exampleInputPayChannel">支付方式</label>
                            <select class="form-control" id="payChannel" name="payChannel" style="width:150px;">
                                <option value="">全部</option>
                                <option value="01">宝付微信支付</option>
                                <option value="02">宝付支付宝支付</option>
                            </select>
                        </div>
                        <div class="form-group col-md-4 form-inline">
                            <label for="exampleInputPaymentFlowNo">支付流水号:</label>
                            <input type="text" class="form-control m-left-2" id="transactionId" name="transactionId" placeholder=""/>
                        </div>
                    </div>
                </div>
                <div class="row">

                    <#--<div class="col-md-12">-->
                        <#--<div class="form-group col-md-8 form-inline">-->
                    <#--<label for="exampleInputTaskcreatetime">创建时间:</label>-->
                            <#--<input class="form-control form_datetime" style="color:black;" type="text" id="taskcreatetimeup" name="taskcreatetimeup" readonly value=""/>-->
                            <#--<label for="exampleInputTaskcreatetime">至</label>-->
                            <#--<input class="form-control form_datetime" style="color:black;" type="text" id="taskcreatetimedown" name="taskcreatetimedown" readonly value=""/>-->
                        <#--</div>-->
                    <#--</div>-->
                    <div class="col-md-12">
                        <div class="form-group col-md-4 form-inline">
                            <label for="exampleInpuTaskstatus">创建时间:</label>
                            <input type="text"	class="form-control form_datetime"  id="createTimeStart" name="createTimeStart" />
                            <label for="exampleInputTaskcreatetime">至</label>
                            <input type="text"	class="form-control form_datetime"  id="createTimeEnd" name="createTimeEnd" />
                        </div>

                    </div>
                </div>
            </form>
        </div>
        <div class="panel-footer padding-5-5">
            <div class="row">
                <div class="col-md-12">
                    <div class="col-md-12" align="right">
                        <button id="superquerybutton" type="button" name="querybutton"
                                class="btn btn-primary">查询</button>
                        <button id="singlequery" type="button"
                                class="btn btn-primary">优化查询</button>
                        <button id="resetbutton" type="button" name="resetbutton"
                                class="btn btn-primary">重置</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="panel panel-default">
        <div class="panel-heading padding-5-5">
            <div class="row">
                <div class="col-md-2">
                    <label>订单列表</label>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <table id="table-javascript"></table>
            </div>
        </div>
    </div>

</div>
<!--出单网点选择弹出框-->
<div id="showDeptTree" class="modal fade"  role="dialog" aria-labelledby="myModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close closeShowDeptTree" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="gridSystemModalLabel">选择出单网点</h4>
            </div>
            <div class="modal-body" style="overflow:auto; height:400px;">
                <div class="container-fluid">
                    <div class="row">
                        <ul id="deptTreeDemo" class="ztree"></ul>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default closeShowDeptTree">关闭</button>
            </div>
        </div>
    </div>
</div>
<!--供应商选择弹出框-->
<div id="showpic" class="modal fade"  role="dialog" aria-labelledby="myModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close closeshowpic" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="gridSystemModalLabel_1">选择供应商</h4>
            </div>
            <div class="modal-body" style="overflow:auto; height:400px;">
                <div class="container-fluid">
                    <div class="row">
                        <ul id="treeDemo" class="ztree"></ul>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default closeshowpic">关闭</button>
            </div>
        </div>
    </div>
</div>
<!--渠道来源选择弹出框-->
<#include "chn/channelTree.ftl"/>
</body>
</html>
