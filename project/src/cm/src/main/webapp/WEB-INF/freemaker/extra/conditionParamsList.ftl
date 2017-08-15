<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>条件参数设配置</title>
    <link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
    <script data-ver="${jsver}" data-main="${staticRoot}/js/load"
            src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
    <script type="text/javascript">
        requirejs([ "extra/conditionParamsList" ]);
    </script>
<body>
<div class="container-fluid">
    <div class="panel panel-default m-bottom-5">
        <div class="panel-heading padding-5-5">查询</div>
        <div class="panel-body">
            <div class="row">
                <div class="col-md-12">
                    <form role="form" id="listManagent">
                        <input type="hidden" id="lastparamkey" name="lastparamkey" />
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputOrgName">参数名称</label> <input type="text"
                                                                                 class="form-control m-left-5"
                                                                                 id="paramkey" name="paramkey"
                                                                                 placeholder="">
                        </div>
                    <#--<div class="form-group form-inline col-md-4">-->
                    <#--<label for="exampleInputOrgName">资金来源</label>-->
                    <#--<select class="form-control m-left-5" id="fundsource" name="fundsource">-->
                    <#--<option value="">全部</option>-->
                    <#--<#list fundSourceList as code>-->
                    <#--<option value="${code.codevalue }">${code.codename }</option>-->
                    <#--</#list>-->
                    <#--</select>-->
                    <#--</div>-->
                    <#--<div class="form-group form-inline col-md-4">-->
                    <#--<label for="exampleInputOrgName">收支类型</label>-->
                    <#--<select class="form-control m-left-5" id="fundtype" name="fundtype">-->
                    <#--<option value="">全部</option>-->
                    <#--<#list fundTypeList as code>-->
                    <#--<option value="${code.codevalue }">${code.codename }</option>-->
                    <#--</#list>-->
                    <#--</select>-->
                    <#--</div>-->
                    <#--<div class="form-group form-inline col-md-12">-->
                    <#--<label for="exampleInputCode">记账时间&nbsp;&nbsp;&nbsp;</label>-->
                    <#--<input type="text" class="form-control form_datetime" id="startdate" name="startdate"-->
                    <#--readonly placeholder=""> --->
                    <#--<input type="text" class="form-control form_datetime" id="enddate" name="enddate" readonly-->
                    <#--placeholder="">-->
                    <#--</div>-->
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
            <button id="addparambutton" type="button" name="addparambutton"
                    class="btn btn-primary">添加参数
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
<!-- 参数编辑操作框 -->
<div class="modal fade" id="editModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="modal-content" style="width: 482px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <div class="panel panel-default m-bottom-5" style="width: 448px;">
                    <div class="panel-heading padding-5-5" style="font-weight: bold;"><input type="text"
                                                                                             id="editTip"
                                                                                             style="border: 0px;background-color:transparent;"
                                                                                             value="编辑参数" readonly/>
                    </div>
                    <form action="" id="paramForm" name="paramForm">
                        <div class="panel-body">
                            <input type="hidden" name="id" id="id" value=""/>
                            <table class="table-no-bordered">
                                <tr>
                                    <td class="text-right"><label>参数名称：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                           type="text"
                                                                           id="paramname" name="paramname"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>参数中文名称：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                           type="text" id="paramcnname"
                                                                           name="paramcnname"/></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>数据类型：</label></td>
                                    <td style="padding-bottom:5px;"><select class=" form-control" style="width: 100px;"
                                                                            id="datatype"
                                                                            name="datatype">
                                        <option value="String">String</option>
                                        <option value="Boolean">Boolean</option>
                                        <option value="Integer">Integer</option>
                                        <option value="Long">Long</option>
                                        <option value="Double">Double</option>
                                    </select></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>是否有默认值：</label></td>
                                    <td style="padding-bottom:5px;"><select class=" form-control" style="width: 100px;"
                                                                            id="isdefault"
                                                                            name="isdefault">
                                        <option value="1">是</option>
                                        <option value="0">否</option>
                                    </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>默认值：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                           type="text"
                                                                           id="defaultvalue" name="defaultvalue"/></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>查询脚本名称：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                           type="text"
                                                                           id="sqlname" name="sqlname"/></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>字段名称：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                           type="text"
                                                                           id="fieldname" name="fieldname"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>适用范围：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <div>
                                        <#list paramTagList as code>
                                            <label><input style="margin-right: 5px;" id="paramtag${code.codevalue }" name="paramtag" type="checkbox"
                                                          value="${code.codevalue }"/>${code.codename }</label>
                                        </#list>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>状态：</label></td>
                                    <td style="padding-bottom:5px;"><select class=" form-control" style="width: 100px;"
                                                                            id="status"
                                                                            name="status">
                                        <option value="1">启动</option>
                                        <option value="0">关闭</option>
                                    </select>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="panel-footer">
                            <input id="execButton" name="execButton" type="button" class="btn btn-primary" value="确定"/>
                            <input id="closeButton" name="closeButton" class="btn btn-primary" type="button"
                                   data-dismiss="modal" value="取消"/>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>