<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>提现任务管理</title>
    <link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
    <script data-ver="${jsver}" data-main="${staticRoot}/js/load"
            src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
    <script type="text/javascript">
        requirejs([ "extra/accountWithdrawList" ]);
    </script>
<body>
<div class="container-fluid">
    <div class="panel panel-default m-bottom-5">
        <div class="panel-heading padding-5-5">查询</div>
        <div class="panel-body">
            <div class="row">
                <div class="col-md-12">
                    <form role="form" id="listManagent">
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputOrgName">用户姓名</label> <input type="text"
                                                                                 class="form-control m-left-5"
                                                                                 id="agentname" name="agentname"
                                                                                 placeholder="">
                        </div>
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputOrgName">转入银行账号</label> <input type="text"
                                                                                   class="form-control m-left-5"
                                                                                   id="cardnumber" name="cardnumber"
                                                                                   placeholder="">
                        </div>
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputOrgName">提现状态</label>
                            <select class="form-control m-left-5" id="status" name="status">
                                <option value="">全部</option>
                            <#list withdrawStatusList as code>
                                <option value="${code.codevalue }">${code.codename }</option>
                            </#list>
                            </select>
                        </div>
                        <div class="form-group form-inline col-md-12">
                            <label for="exampleInputCode">提现申请时间&nbsp;&nbsp;&nbsp;</label>
                            <input type="text" class="form-control form_datetime" id="startdate" name="startdate"
                                   readonly placeholder=""> -
                            <input type="text" class="form-control form_datetime" id="enddate" name="enddate" readonly
                                   placeholder="">
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="panel-footer">
            <button id="querybutton" type="button" name="querybutton"
                    class="btn btn-primary">查询
            </button>
            <button id="resetbutton" type="button" name="resetbutton"
                    class="btn btn-primary">重置
            </button>
        </div>
    </div>
    <div class="panel panel-default">
        <div class="panel-heading padding-5-5">
            <div class="row">
                <div class="col-md-2">
                    查询结果
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <table id="table-list"></table>
            </div>
        </div>
    </div>
</div>
<!-- 提现成功操作框 -->
<div class="modal fade" id="successModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="modal-content" style="width: 482px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <div class="panel panel-default m-bottom-5" style="width: 448px;">
                    <div class="panel-heading padding-5-5" style="font-weight: bold;">状态变更，请注意！</div>
                    <div class="panel-body">
                        <input type="hidden" name="s_id" id="s_id" value=""/>
                        <table class="table-no-bordered">
                            <tr>
                                <td class="text-right"><label>用户ID：</label></td>
                                <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                       type="text"
                                                                       id="agentid" readonly/></td>
                            </tr>
                            <tr>
                                <td class="text-right"><label>用户姓名：</label></td>
                                <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                       type="text" id="s_agentname"
                                                                       readonly/></td>
                            </tr>
                            <tr>
                                <td class="text-right"><label>当月已提现金额：</label></td>
                                <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                       type="text" id="monthAmount"
                                                                       readonly/></td>
                            </tr>
                            <tr>
                                <td class="text-right"><label>当次提现金额：</label></td>
                                <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                       type="text" id="amount" readonly/>
                                </td>
                            </tr>
                            <tr>
                                <td class="text-right"><label>当次需计税金额：</label></td>
                                <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                       type="text" id="taxAmount"
                                                                       readonly/></td>
                            </tr>
                            <tr>
                                <td class="text-right"><label>需扣除个人税费：</label></td>
                                <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                       type="text" id="tax" readonly/>
                                </td>
                            </tr>
                        </table>
                        <div class="col-md-12" style="font-weight: bold; padding-top: 5px;">是否确认提现成功？</div>
                    </div>
                    <div class="panel-footer">
                        <input id="s_execButton" name="s_execButton" type="button" class="btn btn-primary" value="确定"/>
                        <input id="closeButton" name="closeButton" class="btn btn-primary" type="button"
                               data-dismiss="modal" value="取消"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 提现失败操作框 -->
<div class="modal fade" id="errorModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="modal-content" style="width: 482px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <div class="panel panel-default m-bottom-5" style="width: 448px;">
                    <div class="panel-heading padding-5-5">状态变更，请注意！</div>
                    <div class="panel-body">
                        <input type="hidden" name="e_id" id="e_id" value=""/>

                        <div class="col-md-12" style="font-weight: bold; padding-bottom: 5px;">是否确认提现失败？</div>
                        <div class="col-md-12" style="padding-bottom: 5px;">失败原因：</div>
                        <div class="col-md-12" style="padding-bottom: 5px;"><textarea class="form-control"
                                                                                      style="width:380px;height:96px;resize: none;"
                                                                                      id="e_noti" value=""
                                                                                      maxlength="50"></textarea></div>
                    </div>
                    <div class="panel-footer">
                        <input id="e_execButton" name="e_execButton" type="button" class="btn btn-primary" value="确定"/>
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
