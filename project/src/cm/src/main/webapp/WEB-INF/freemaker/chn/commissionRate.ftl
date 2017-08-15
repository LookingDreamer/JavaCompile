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
        requirejs([ "chn/commissionRate", "lib/tsearch"]);
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
                <div class="ztree" id="deptTree" style="width:100%; height:840px; overflow-y:auto;overflow-x:auto;">
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
                        <div class="form-group col-md-4 form-inline">
                            <input class="form-control" id="agreementname" type="text" name="agreementname"
                                   readonly="readonly" placeholder="请选择机构" style="width: 200px;">
                            <input class="form-control" type="hidden" id="agreementid" name="agreementid" value=""/>
                        </div>
                        <div class="form-group col-md-4 form-inline">
                            <label for="exampleInputCode">供应商ID</label>
                            <input class="form-control"   id="providerid"  readonly="readonly" name="providerid" value=""/>
                        </div>
                        <div class="form-group col-md-4 form-inline">
                            <label for="exampleInputCode">协议编码</label>
                            <input class="form-control"   id="agreementcode"  readonly="readonly" name="agreementcode" value=""/>
                        </div>
                    </div>
                    <div class="row" style="padding-bottom: 5px;">
                        <div class="form-group col-md-4 form-inline">
                            <label for="exampleInputCode">佣金类型</label>
                            <select class="form-control m-left-5" id="qcommissiontype" name="qcommissiontype">
                                <option value="">全部</option>
                            <#list commissionTypeList as code>
                                <option value="${code.codevalue }">${code.codename }</option>
                            </#list>
                            </select>
                        </div>
                        <div class="form-group col-md-3 form-inline">
                            <label for="qcommissionstatus">状态</label>
                            <select class="form-control m-left-5" id="qcommissionstatus" name="qcommissionstatus">
                                <option value="">全部</option>
                                <option value="0">待审核</option>
                                <option value="1">启用</option>
                                <option value="2">关闭</option>
                            </select>
                        </div>
                        <div class="form-group col-md-5 form-inline">
                            <div class="input-group">
                               <input class="form-control"  id="keyword" style="width:300px" placeholder="关键字"  name="keyword" value=""/>
                                <span class="input-group-btn">
                                    <button id="clear-keyword" class="btn btn-default" type="button">X</button>
                                </span>
                            </div>
                        </div>
                        <div class="form-group col-md-3 form-inline" style="display: none">
                            <input class="form-control"  id="qchannelsource"  name="qchannelsource" value=""/>
                        </div>
                    </div>
                </div>

                <div class="panel-footer">
                    <a name="close-rate-set" id="close-rate-set"></a>
                    <button id="querybutton" type="button" name="querybutton"
                            class="btn btn-primary">查询</button>
                    <button id="batcheffectivebutton" type="button" name="batcheffectivebutton"
                            class="btn btn-primary">启用</button>
                    <button class="btn btn-primary" type="button" name="copybutton"
                            title="copy" id="copybutton"> 复制
                    </button>
                    <a href="#jbxx">
                        <button id="addagreement" type="button" name="addagreement"
                                class="btn btn-primary">新增
                        </button>
                    </a>
                    <button class="btn btn-primary" type="button" name="batchclosebutton" title="batchclosebutton" id="batchclosebutton">
                        关闭
                    </button>
                    <button class="btn btn-primary" type="button" name="batchdeletebutton"
                            title="delete" id="batchdeletebutton"> 删除
                    </button>
                </div>

                <div class="row">
                    <div class="col-md-12" style="height：22px;">
                        <table id="table-commissionRate"></table>
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
                    <a name="rate-set-btn" href="#rate-set" id="rate-set-btn"></a>
                    <a name="rate-set" id="rate-set"></a>
                    <span style="font-weight: bold;">佣金设置</span>
                    <a href="#close-rate-set">
                    <button id="defultclose" class="btn" data-toggle="collapse" data-parent="#panelOne"
                              style="float:right;position: relative;top: -8px;">关闭
                    </button>
                    </a>
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
                                    <option value="">全部</option>
                                <#list commissionTypeList as code>
                                    <option value="${code.codevalue }">${code.codename }</option>
                                </#list>
                                </select>
                            </div>
                            <div class="form-group col-md-4 form-inline" style="display:none">
                                <label for="exampleInputCode">应用渠道</label>
                                <select class="form-control m-left-5" id="channelsource" name="channelsource">
                                <#list channelSourceList as code>
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
                                <label for="exampleInputCode">失效时间</label>
                                <div class="input-group">
                                    <input type="text" class="form-control m-left-5 form_datetime" id="terminaldate"
                                           name="terminaldate"
                                       readonly placeholder="">
                                    <span class="input-group-btn">
                                    <button id="clear-terminaldate" class="btn btn-default" type="button">X</button>
                                   </span>
                                </div>
                            </div>
                            <div class="form-group col-md-12 form-inline">
                                <label for="exampleInputCode">佣金提醒</label>
                                <textarea rows="3" cols="70"
                                          class="form-control m-left-5"
                                          id="reminder" name="reminder">${agreement.reminder!''}
                                </textarea>
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

<!-- 佣金复制 -->
<div class="modal fade" id="copyRateModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
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
                                                                                             value="佣金率复制" readonly/>
                    </div>
                    <div class="panel-body">
                        <div class="panel panel-default m-bottom-2">
                            <div class="panel-heading padding-5-5">供应商协议列表</div>
                            <div class="panel-body">
                                <div><input class="form-control ztree-search" id="treemodalsearch" data-bind="deptModalTree" name="treesearch"
                                            placeholder="输入机构名称关键字进行搜索"/></div>
                                <div class="ztree" id="deptModalTree" style="width:100%; height:600px; overflow-y:auto;overflow-x:auto;">
                                    正在加载供应商协议数据......
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input id="execCopyButton" name="execCopyButton" type="button" class="btn btn-primary" value="复制"/>
                        <input id="closeCopyButton" name="closeCopyButton" class="btn btn-primary" type="button"
                               data-dismiss="modal" value="取消"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 新增佣金配置 -->
<div class="modal fade" id="addCommissionRateModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="modal-content" style="width: 676px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <div class="panel panel-default m-bottom-5" style="width: 644px;">
                    <div class="panel-heading padding-5-5" style="font-weight: bold;"><input type="text"
                                                                                             id="conditionTip"
                                                                                             style="border: 0px;background-color:transparent;"
                                                                                             value="新增佣金配置" readonly/>
                    </div>
                    <div class="panel-body">
                        <form id="addrateform">
                            <div class="row" id="iagreementdiv" style="padding-left: 10px;padding-top: 10px;">
                                <div class="form-group col-md-4 form-inline">
                                    <label for="exampleInputCode">佣金类型<span style="color:red">*</span></label>
                                    <select class="form-control m-left-5" id="icommissiontype" name="icommissiontype">
                                        <option value="">请选择</option>
                                    <#list commissionTypeList as code>
                                        <option value="${code.codevalue }">${code.codename }</option>
                                    </#list>
                                    </select>
                                </div>
                                <div class="form-group col-md-3 form-inline" style="display:none">
                                    <label for="exampleInputCode">应用渠道</label>
                                    <select class="form-control m-left-5" id="ichannelsource" name="ichannelsource">
                                    <#list channelSourceList as code>
                                        <option value="${code.codevalue }">${code.codename }</option>
                                    </#list>
                                    </select>
                                </div>
                                <div class="form-group col-md-3 form-inline">
                                    <label for="exampleInputCode">佣金率<span style="color:red">*</span></label> <input type="text"
                                                                                     class="form-control m-left-5"
                                                                                     id="irate" name="irate"
                                                                                     value="${agreement.agreementname!''}">
                                </div>
                                <div class="form-group col-md-6 form-inline">
                                    <label for="exampleInputCode">生效时间<span style="color:red">*</span></label>
                                    <input type="text" class="form-control m-left-5 form_datetime" id="ieffectivedate"
                                           name="ieffectivedate"
                                           readonly placeholder="">
                                </div>
                                <div class="form-group col-md-6 form-inline">
                                    <label for="exampleInputCode">失效时间</label>
                                    <input type="text" class="form-control m-left-5 form_datetime" id="iterminaldate"
                                           name="iterminaldate"
                                           readonly placeholder="">
                                </div>
                                <div class="form-group col-md-12 form-inline">
                                    <label for="exampleInputCode">佣金提醒</label>
                                <textarea rows="3" cols="70"
                                          class="form-control m-left-5"
                                          id="ireminder" name="ireminder">
                                </textarea>
                                </div>
                                <div class="form-group col-md-12 form-inline">
                                    <label for="exampleInputCode">佣金描述<span style="color:red">*</span></label>
                                <textarea rows="3" cols="70"
                                          class="form-control m-left-5"
                                          id="iremark" name="iremark">
                                </textarea>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="panel-footer">
                        <input id="iexecButton" name="execButton" type="button" class="btn btn-primary" value="保存"/>
                        <input id="iresetButton" name="iresetButton" type="button" class="btn btn-primary" value="重置"/>
                        <input id="icloseButton" name="closeButton" class="btn btn-primary" type="button"
                               data-dismiss="modal" value="取消"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 关闭处理 -->
<div class="modal fade" id="colseCommissionRateModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="modal-content" style="width: 482px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <div class="panel panel-default m-bottom-5" style="width: 448px;">
                    <div class="panel-heading padding-5-5" style="font-weight: bold;">关闭佣金配置！</div>
                    <div class="panel-body">
                        <table class="table-no-bordered">
                            <input type="text" hidden="true" id="singleCloseFlag" />
                            <input type="text" hidden="true" id="batchCloseFlag" />
                            <tr>
                                <td>关闭模式</td>
                                <td style="padding-bottom:5px;">
                                    &nbsp; &nbsp; &nbsp;
                                    <input type="radio" name="operatetype" id="immediateClose" value="1" >
                                    立即关闭 &nbsp; &nbsp; &nbsp;
                                    <input type="radio" name="operatetype" id="confirmDateClose" value="2"  >
                                    特定时间关闭 &nbsp; &nbsp;&nbsp;
                                </td>
                            </tr>
                            <tr id="set-terminaldate-tr" style="display: none">
                                <td>失效时间</td>
                                <td class="text-right">
                                    <div class="form-group col-md-12 form-inline">
                                        <input type="text" class="form-control form_datetime" id="cterminaldate"
                                               name="cterminaldate"
                                               readonly placeholder="">
                                    </div>
                                </td>
                            </tr>

                        </table>
                    </div>
                    <div class="panel-footer">
                        <input id="cexecButton" name="execButton" type="button" class="btn btn-primary" value="确定"/>
                        <input id="ccloseButton" name="closeButton" class="btn btn-primary" type="button"
                               data-dismiss="modal" value="取消"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 关闭处理 -->

</body>
</html>
