<!DOCTYPE html>
<html lang="en" class="fuelux">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>佣金系数管理</title>
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
        requirejs([ "chn/commissionRatio", "lib/tsearch"]);
    </script>
</head>
<body>

<input id="updateid" type="hidden" name="updateid">
<input type="hidden" id="affiliationorgquery" name="affiliationorg">

<div>
    <div class="col-md-3">
        <div class="panel panel-default m-bottom-2">
            <div class="panel-heading padding-5-5">渠道列表</div>
            <div class="panel-body">
                <div><input class="form-control ztree-search" id="treesearch" data-bind="deptTree" name="treesearch"
                            placeholder="输入渠道名称关键字进行搜索"/></div>
                <div class="ztree" id="deptTree" style="width:100%; height:600px; overflow-y:auto;overflow-x:auto;">
                    正在加载渠道数据......
                </div>
            </div>
        </div>
    </div>

    <div class="col-md-9">
        <div class="panel panel-default m-bottom-2">
            <div class="panel panel-default">
                <div class="panel-heading padding-5-5">
                    <div class="row" style="padding-bottom: 5px;">
                        <div class="col-md-4">
                            <input class="form-control" id="channelname" type="text" name="channelname"
                                   readonly="readonly" placeholder="请选择渠道" style="width: 200px;">
                        </div>
                        <div class="col-md-4">
                            <input class="form-control"  id="channelid" type="text" name="channelid"  readonly="readonly" placeholder="渠道ID" style="width: 200px;" value=""/>
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
                        <div class="form-group col-md-5 form-inline">
                            <div class="input-group">
                                <input class="form-control"  id="keyword" style="width:300px" placeholder="关键字"  name="keyword" value=""/>
                                <span class="input-group-btn">
                                    <button id="clear-keyword" class="btn btn-default" type="button">X</button>
                                </span>
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
                            <button id="addratio" type="button" name="addratio"
                                    onclick="addCommissionRatio()"
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
                            <table id="table-commissionRatio"></table>
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
                    <a name="ratio-set" id="ratio-set"></a>
                    <span style="font-weight: bold;">佣金系数设置</span>
                    <a href="#close-ratio-set"></a>
                    <button id="defultclose" class="btn" data-toggle="collapse" data-parent="#panelOne"
                             style="float:right;position: relative;top: -8px;">关闭
                    </button>
                </div>
                <div class="panel-body haoba" id="jbxxdiv" style="display: block;padding:2px;">
                    <div class="alert alert-danger alert-dismissible" id="agreementerror" role="alert"
                         style="display: none;"></div>
                    <form action="" id="commissionRatioForm" name="commissionRatioForm">
                        <input class="form-control" type="hidden" id="commissionratioid" name="commissionratioid"
                               value=""/>
                        <input class="form-control" type="hidden" id="commissionratiostatus" name="commissionratiostatus"
                               value=""/>

                        <div class="row" id="agreementdiv" style="padding-left: 10px;padding-top: 10px;">
                            <div class="row" >
                                <div class="form-group col-md-3 form-inline">
                                    <label for="commissiontype">佣金类型</label>
                                    <select class="form-control m-left-5" id="commissiontype" name="commissiontype">
                                    <#list commissionTypeList as code>
                                        <option value="${code.codevalue }">${code.codename }</option>
                                    </#list>
                                    </select>
                                </div>

                                <div class="form-group col-md-4 form-inline">
                                    <label for="effectivedate">生效时间</label>
                                    <input type="text" class="form-control m-left-5 form_datetime" id="effectivedate"
                                           name="effectivedate"
                                           readonly placeholder="">
                                </div>
                                <div class="form-group col-md-5 form-inline">
                                    <label for="terminaldate">失效时间</label>
                                    <div class="input-group">
                                        <input type="text" class="form-control m-left-5 form_datetime" id="terminaldate"
                                               name="terminaldate"
                                               readonly placeholder="">
                                        <span class="input-group-btn">
                                        <button id="clear-terminaldate" class="btn btn-default" type="button">X</button>
                                       </span>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-md-3 form-inline">
                                    <label for="calculatetype">计算方式</label>
                                    <select class="form-control m-left-5" id="calculatetype" name="calculatetype">
                                        <option value="">请选择</option>
                                        <option value="1">加</option>
                                        <option value="2">减</option>
                                        <option value="3">乘</option>
                                        <option value="4">除</option>
                                    </select>
                                </div>
                                <div class="form-group col-md-4 form-inline">
                                    <label for="ratio">佣金系数</label> <input type="text"
                                                                           class="form-control m-left-5"
                                                                           id="ratio" name="ratio"
                                                                           value="${agreement.ratio!''}">
                                </div>
                                <div class="form-group col-md-5 form-inline" style="display: none">
                                    <label for="taxrate">税率</label> <input type="text"
                                                                           class="form-control m-left-5"
                                                                           id="taxrate" name="taxrate"
                                                                           value="${agreement.ratio!''}">
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-md-12 form-inline">
                                    <label for="exampleInputCode">说明</label>
                                    <textarea rows="3" cols="70"
                                              class="form-control m-left-5"
                                              id="remark" name="remark">${agreement.remark!''}
                                    </textarea>
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer">
                            <button id="savebutton" type="button" name="savebutton"
                                    class="btn btn-primary" onclick="saveCommissionRatio()">保存
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
                                                                                             value="佣金系数复制" readonly/>
                    </div>
                    <div class="panel-body">
                        <div class="panel panel-default m-bottom-2">
                            <div class="panel-heading padding-5-5">渠道列表</div>
                            <div class="panel-body">
                                <div><input class="form-control ztree-search" id="treemodalsearch" data-bind="deptModalTree" name="treesearch"
                                            placeholder="输入渠道关键字进行搜索"/></div>
                                <div class="ztree" id="deptModalTree" style="width:100%; height:600px; overflow-y:auto;overflow-x:auto;">
                                    正在加载渠道数据......
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
