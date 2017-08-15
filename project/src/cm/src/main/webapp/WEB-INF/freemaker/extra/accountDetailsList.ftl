<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>资金流水管理</title>
    <link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
    <script data-ver="${jsver}" data-main="${staticRoot}/js/load"
            src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
    <script type="text/javascript">
        requirejs([ "extra/accountDetailsList" ]);
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
                            <label for="exampleInputOrgName">资金来源</label>
                            <select class="form-control m-left-5" id="fundsource" name="fundsource">
                                <option value="">全部</option>
                            <#list fundSourceList as code>
                                <option value="${code.codevalue }">${code.codename }</option>
                            </#list>
                            </select>
                        </div>
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputOrgName">收支类型</label>
                            <select class="form-control m-left-5" id="fundtype" name="fundtype">
                                <option value="">全部</option>
                            <#list fundTypeList as code>
                                <option value="${code.codevalue }">${code.codename }</option>
                            </#list>
                            </select>
                        </div>
                        <div class="form-group form-inline col-md-12">
                            <label for="exampleInputCode">记账时间&nbsp;&nbsp;&nbsp;</label>
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
        <form hidden="true" name="detailinfo" id="detailinfo" action="querydetail" method="POST">
            <input type="text" name="id" id="id" value="123"/>
            <input type="text" name="risktype" id="risktype" value="0"/>
        </form>
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

<!-- -->
<div class="modal fade" id="notiTableModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="modal-content" style="width: 682px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <div class="panel panel-default m-bottom-5" style="width: 648px;">
                    <div class="panel-heading padding-5-5" style="font-weight: bold;">详情！</div>
                    <div class="panel-body">
                        <!--<input type="hidden" name="id" id="s_id" value=""/>-->
                        <table class="table-no-bordered" id="notiTable">

                        </table>
                    </div>
                    <div class="panel-footer">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 编辑渠道 -->

</body>
</html>
