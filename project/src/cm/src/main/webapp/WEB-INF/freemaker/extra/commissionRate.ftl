<!DOCTYPE html>
<html lang="en" class="fuelux">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>协议管理</title>
    <link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
    <link href="${staticRoot}/css/system/flat-ui-icon.css" rel="stylesheet">
    <script data-ver="${jsver}" data-main="${staticRoot}/js/load"
            src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
    <style type="text/css">
        .htmleaf-header {
            margin-bottom: 15px;
            font-family: "Segoe UI", "Lucida Grande", Helvetica, Arial, "Microsoft YaHei", FreeSans, Arimo, "Droid Sans", "wenquanyi micro hei", "Hiragino Sans GB", "Hiragino Sans GB W3", "FontAwesome", sans-serif;
        }

        .htmleaf-icon {
            color: #fff;
        }

        tr {
            height: 40px;
        }

        td {
            vertical-align: middle;
        }

        .modal-body {
            overflow: auto;
            height: 380px;
        }
    </style>
    <script type="text/javascript">
        requirejs([ "extra/commissionRate", "lib/tsearch"]);
    </script>
</head>
<body>

<input id="updateid" type="hidden" name="updateid">
<input type="hidden" id="affiliationorgquery" name="affiliationorg">

<div>
    <div class="col-md-3">
        <div class="panel panel-default m-bottom-2">
            <div class="panel-heading padding-5-5">供应商协议列表</div>
            <div class="panel-body">
                <div><input class="form-control ztree-search" id="treesearch" data-bind="deptTree" name="treesearch"
                            placeholder="输入机构名称关键字进行搜索"/></div>
                <div class="ztree" id="deptTree" style="width:100%; height:600px; overflow-y:auto;overflow-x:auto;">
                    正在加载供应商协议数据......
                </div>
            </div>
        </div>
    </div>

    <div class="col-md-9">
        <div class="panel panel-default m-bottom-2">
            <div class="panel panel-default">
                <div class="panel-heading padding-5-5">
                    <div class="row" style="padding-bottom: 5px;">
                        <div class="col-md-3">
                            <input class="form-control" id="agreementname" type="text" name="agreementname"
                                   readonly="readonly" placeholder="请选择机构" style="width: 200px;">
                            <input class="form-control" type="hidden" id="agreementid" name="agreementid" value=""/>
                        </div>
                        <div class="col-md-3">
                            <input class="form-control"   id="agreementcode"  readonly="readonly" name="agreementcode" value=""/>
                        </div>
                        <div class="col-md-6" align="right">
                            <a href="#jbxx">
                                <button id="addagreement" type="button" name="addagreement"
                                        onclick="addCommissionRate()"
                                        class="btn btn-primary">新增佣金设置
                                </button>
                            </a>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12" style="height：22px;">
                            <table id="table-commissionRate"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="col-md-9">
        <div class="panel-collapse collapse" style="padding: 0px" id="collapseOne">
            <div class="panel panel-default m-bottom-2">
                <div class="panel-heading" style="height: 40px;padding: 10px;">
                <#--<button type="button" class="close" data-dismiss="modal" href="#collapseOne"><span aria-hidden="true">×</span></button>-->
                    <span style="font-weight: bold;">佣金设置</span>
                    <button id="defultclose" class="btn" data-toggle="collapse" data-parent="#panelOne"
                            href="#collapseOne" style="float:right;position: relative;top: -8px;">关闭
                    </button>
                </div>
                <div class="panel-body haoba" id="jbxxdiv" style="display: block;padding:2px;">
                    <div class="alert alert-danger alert-dismissible" id="agreementerror" role="alert"
                         style="display: none;"></div>
                    <form action="" id="commissionRateForm" name="commissionRateForm">
                        <input class="form-control" type="hidden" id="commissionrateid" name="commissionrateid"
                               value=""/>
                        <input class="form-control" type="hidden" id="commissionratestatus" name="commissionratestatus"
                               value=""/>

                        <div class="row" id="agreementdiv" style="padding-left: 10px;padding-top: 10px;">
                            <div class="form-group col-md-4 form-inline">
                                <label for="exampleInputCode">佣金类型</label>
                                <select class="form-control m-left-5" id="commissiontype" name="commissiontype">
                                <#list commissionTypeList as code>
                                    <option value="${code.codevalue }">${code.codename }</option>
                                </#list>
                                </select>
                            </div>
                            <div class="form-group col-md-4 form-inline">
                                <label for="exampleInputCode">佣金率</label> <input type="text"
                                                                                 class="form-control m-left-5"
                                                                                 id="rate" name="rate"
                                                                                 value="${agreement.agreementname!''}">
                            </div>
                            <div class="form-group col-md-4 form-inline">
                                <label for="exampleInputCode">生效时间</label>
                                <input type="text" class="form-control m-left-5 form_datetime" id="effectivedate"
                                       name="effectivedate"
                                       readonly placeholder="">
                            </div>
                            <div class="form-group col-md-12 form-inline">
                                <label for="exampleInputCode">佣金描述</label>
                                <textarea rows="3" cols="70"
                                          class="form-control m-left-5"
                                          id="remark" name="remark">${agreement.remark!''}
                                </textarea>
                            </div>
                        </div>
                        <div class="panel-footer">
                            <button id="savebutton" type="button" name="savebutton"
                                    class="btn btn-primary" onclick="saveCommissionRate()">保存
                            </button>
                            <button id="examinebutton" type="button" name="examinebutton"
                                    class="btn btn-primary">启用
                            </button>
                            <button id="closebutton" type="button" name="closebutton"
                                    class="btn btn-primary">关闭
                            </button>
                            <button id="addconditionbutton" type="button" name="addconditionbutton"
                                    class="btn btn-primary">添加条件
                            </button>
                            <button id="copycommissionrate" type="button" name="copycommissionrate"
                                    class="btn btn-primary" style="float:right;position: relative;">复制并新增佣金设置
                            </button>
                        </div>
                    </form>
                </div>
                <div class="row">
                    <div class="col-md-12" style="height：22px;">
                        <table id="table-conditions"></table>
                    </div>
                </div>
            <#--<#include "commissionRateDetail.ftl"/>-->
            </div>
        </div>
    </div>
</div>
<!-- 条件编辑操作框 -->
<div class="modal fade" id="editConditionModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="modal-content" style="width: 576px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <div class="panel panel-default m-bottom-5" style="width: 544px;">
                    <div class="panel-heading padding-5-5" style="font-weight: bold;"><input type="text"
                                                                                             id="conditionTip"
                                                                                             style="border: 0px;background-color:transparent;"
                                                                                             value="编辑条件" readonly/>
                    </div>
                    <div class="panel-body">
                        <form id="conditionform">
                            <input type="hidden" name="id" id="conditionid" value=""/>
                            <input type="hidden" name="sourceid" id="sourceid" value=""/>
                            <input type="hidden" name="conditionsource" id="conditionsource" value=""/>
                            <table class="table-no-bordered">
                                <tr>
                                    <td class="text-center"><label>参数名称</label></td>
                                    <td class="text-center"><label>判断式</label></td>
                                    <td class="text-center"><label>参数值</label></td>
                                </tr>
                                <tr>
                                    <td class="padding-5-5">
                                        <select class="form-control" style="width: 220px;" id="paramname"
                                                name="paramname">
                                        <#list paramList as code>
                                            <option value="${code.paramname}">${code.paramcnname}</option>
                                        </#list>
                                        </select>
                                    </td>
                                    <td class="padding-5-5">
                                        <select class=" form-control" style="width: 120px;" id="expression"
                                                name="expression">
                                            <option value="=">=</option>
                                            <option value="!=">!=</option>
                                            <option value=">">&gt;</option>
                                            <option value=">=">&gt;=</option>
                                            <option value="<">&lt;</option>
                                            <option value="<=">&lt;=</option>
                                            <option value="contain">contain</option>
                                            <option value="un-contain">un-contain</option>
                                            <option value="in">in</option>
                                            <option value="un-in">un-in</option>
                                        </select></td>
                                    <td class="padding-5-5"><input class="form-control" style="width: 140px;"
                                                                   type="text" id="paramvalue" name="paramvalue"/></td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="panel-footer">
                        <input id="execButton" name="execButton" type="button" class="btn btn-primary" value="确定"/>
                        <input id="closeButton" name="closeButton" class="btn btn-primary" type="button"
                               data-dismiss="modal" value="取消"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
