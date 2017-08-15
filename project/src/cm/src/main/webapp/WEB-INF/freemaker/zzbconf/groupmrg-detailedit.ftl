<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>群组详细信息修改</title>
    <link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
    <script data-ver="${jsver}" data-main="${staticRoot}/js/load"
            src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
    <script type="text/javascript">
        requirejs([ "zzbconf/groupmrgedit" ]);
    </script>
</head>
<body>
<div class="container-fluid">
    <div class="panel panel-default m-bottom-5">
        <div class="panel-heading padding-5-5">群组基本信息</div>
        <div class="panel-body">
            <div class="alert alert-danger" role="alert" id="showerrormessage" style="display: none;"></div>
            <div class="row" >
                <table class="table table-bordered ">
                    <form  id="form_group_base_data" action="savebasegroupdata"  method="post">
                        <tr>
                            <td class="col-md-1" align="right" style="vertical-align: middle;">群组名称</td>
                            <td class=" col-md-3">
                                <input class="form-control" type="text" id="groupname" name="groupname"value="${comm.groupname!''}">
                                <input  type="hidden" id="id" value="${comm.id }" name="id">
                                <input  type="hidden" id="groupnum" value="${comm.groupnum }" name="groupnum">
                            </td>
                            <td class=" col-md-1" align="right" style="vertical-align: middle;">群组类型</td>
                            <td class=" col-md-3">
                                <select id="groupkind" name="groupkind" class="form-control">
                                    <option value="0">请选择</option>
                                <#list groupType as gtoup>
                                    <option value="${gtoup.codevalue }"  <#if comm.groupkind==gtoup.codevalue>selected="selected"</#if>>${gtoup.codename }</option>
                                </#list>
                                </select>
                            </td>
                            <td class=" col-md-1" align="right" style="vertical-align: middle;">群组编码</td>
                            <td class=" col-md-3">
                                <input type="text" class="form-control" id="groupcode" name="groupcode"  value="${comm.groupcode!''}">
                            </td>
                        </tr>
                        <tr>
                            <td class=" col-md-1" style="vertical-align: middle;" align="right">所属平台</td>
                            <td class=" col-md-3">
                                <input type="hidden" name="organizationid" id="organizationid" value="${comm.organizationid!''}">
                                <input type="hidden" name="pid" value="1">
                                <input type="text" class="form-control" id="groupDeptOrgName"
                                       name="groupDeptOrgName"  value="${groupDeptOrgName!''}"
                                       placeholder="请选择所属平台">
                            </td>
                            <td class=" col-md-1" align="right" style="vertical-align: middle;">是否生效</td>
                            <td class=" col-md-3">
                                <select class="form-control" name="privilegestate" id="privilegestate">
                                <#list comm.privilegestate as privilegestate>
                                    <#if privilegestate=="1">
                                        <option selected="selected" id="suibian" value="1">是</option>
                                        <option  value="2">否</option>
                                    <#else>
                                        <option  id="suibian" value="1">是</option>
                                        <option selected="selected" value="2">否</option>
                                    </#if>
                                </#list>
                                </select>
                            </td>
                            <td class=" col-md-1" align="right" style="vertical-align: middle;">工作量</td>
                            <td class=" col-md-3">
                                <input type="text" class="form-control" id="workload" name="workload"  value="${comm.workload!''}">
                            </td>
                        </tr>
                        <input id="tasktype" type="hidden" name="tasktype" value="">
                    </form>
                    <tr>
                        <td class=" col-md-1" align="right" style="vertical-align: middle;">
                            任务类型
                        </td>
                        <td class=" col-md-11" colspan="5">
                            <table class="table table-bordered ">
                                <tr>
                                    <td class="col-md-11">
                                        <div class="col-xs-5">
                                            <select name="from" class="js-multiselect form-control" size="8" multiple="multiple">
                                            <#list taskType as all>
                                                <option value="${all.codevalue }">${all.codename }</option>
                                            </#list>
                                            </select>
                                        </div>
                                        <div class="col-xs-2">
                                            <button type="button" id="js_right_All_1" class="btn btn-block"><i class="glyphicon glyphicon-forward"></i></button>
                                            <button type="button" id="js_right_Selected_1" class="btn btn-block"><i class="glyphicon glyphicon-chevron-right"></i></button>
                                            <button type="button" id="js_left_Selected_1" class="btn btn-block"><i class="glyphicon glyphicon-chevron-left"></i></button>
                                            <button type="button" id="js_left_All_1" class="btn btn-block"><i class="glyphicon glyphicon-backward"></i></button>
                                        </div>
                                        <div class="col-xs-5">
                                            <select name="tasktype" id="js_multiselect_to_1" class="form-control" size="8" multiple="multiple">
                                            <#list oldtaskType as all>
                                                <option value="${all.codevalue }">${all.codename }</option>
                                            </#list>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="panel-footer">
                <button type="submit" id="save_base_data_button" class="btn btn-primary">保存</button>
                <!--<button  type="button" id="save_base_data_button" class="btn btn-primary" >保存</button>-->
                <!--<button  type="button" id="add_base_data_button" class="btn btn-primary" >新增</button>-->
                <!--<button  type="button" id="add_base_data_button" class="btn btn-primary" >新增</button>-->
                <button  type="button" class="btn btn-primary" id="go_back">返回</button>
            </div>
        </div>
    </div>
    <div class="panel panel-default m-bottom-5" style="width:100%; height:465px; overflow-y:auto;overflow-x:auto;">
        <div class="panel-heading padding-5-5">
            <ul class="nav nav-pills nav-justified">
                <li role="presentation" id="user" class='active'><a href="#"
                                                                    onclick="showdiv('user')">人员管理</a></li>
                <li role="presentation" id="wang"><a href="#"
                                                     onclick="showdiv('wang')">网点管理</a></li>
                <li role="presentation" id="provider"><a href="#"
                                                         onclick="showdiv('provider')">供应商</a></li>
            </ul>
        </div>

        <div class="panel-body" style="padding:2px;">
            <div class="panel-body" id="userdiv" style="display: block;padding:2px;">
                <div class="modal fade" id="myModal_group_member_add" tabindex="-1" role="dialog"
                     aria-labelledby="myModalLabel" aria-hidden="true">
                    <div class="modal-dialog" style="width: 90%">
                        <div class="modal-content" id="modal-content">
                        </div>
                    </div>
                </div>
                <button type="button" name="savebutton" class="btn btn-primary" id="add_members">添加成员</button>
                <button type="button" name="addbutton" class="btn btn-primary" id="remove_members">移除选中成员</button>
                <table id="group_member_list" class="table table-bordered "></table>
            </div>
            <div class="panel-body" id="wangdiv" style="display: none;padding:2px;">
                <form action="" id="groupform" name="groupform">
                    <div class="form-group " id="deptid2" name="deptid">
                        <div class="form-group col-md-2 form-inline">
                            <select class="form-control" style="width:160px;" id="deptidc1" name="deptid1"
                                    onchange="deptconnchange(1)">
                                <option value="" selected="true" disabled="true">请选择</option>
                            </select>
                        </div>
                        <div class="form-group col-md-2 form-inline">
                            <select class="form-control" style="width:160px;" id="deptidc2" name="deptid2"
                                    onchange="deptconnchange(2)">
                            </select>
                        </div>
                        <div class="form-group col-md-2 form-inline">
                            <select class="form-control" style="width:160px;" id="deptidc3" name="deptid3"
                                    onchange="deptconnchange(3)">
                            </select>
                        </div>
                        <div class="form-group col-md-2 form-inline">
                            <select class="form-control" style="width:160px;" id="deptidc4" name="deptid4"
                                    onchange="deptconnchange(4)">
                            </select>
                        </div>
                        <div class="form-group col-md-2 form-inline">
                            <select class="form-control" style="width:160px;" id="deptidc5" name="deptid5">
                            </select>
                        </div>
                        <div class="form-group col-md-2 form-inline">
                            <button id="savebutton" type="button" name="savebutton"
                                    class="btn btn-primary" onclick="savewang()">网点添加</button>
                        </div>
                </form>
            </div>
            <!-- <button type="button" name="add_wang" class="btn btn-primary" id="add_wang">添加网点</button>-->
            <button type="button" name="remove_wang" class="btn btn-primary" id="remove_wang" onclick="remove_wang()">移除网点</button>
            <table id="group_wang_list" class="table table-bordered "></table>
        </div>
        <div class="panel-body" id="providerdiv" style="display: none;padding:2px;">

                   
        	<input type="hidden" name="groupprovdeid" id="groupprovdeids" value="${prvcode!''}">
            <input type="hidden" class="form-control" id="groupprovde" name="groupprovde"  value="${prvname!''}" placeholder="请选择供应商">
            <button type="button" name="add_groupprovde" class="btn btn-primary" id="add_groupprovde" onclick="add_provider()">新增供应商</button>
            <button type="button" name="remove_provider" class="btn btn-primary" id="remove_provider" onclick="remove_provider()">移除供应商</button>
            <table id="group_provider_list" class="table table-bordered "></table>
        </div>
    </div>
    <div class="modal fade" id="myModal_dept" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content">
                <div class="modal-body"  style="overflow: auto; height: 300px;">
                    <div id="dept_tree" class="ztree"></div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="myModal_group" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel" aria-hidden="true" >
        <div class="modal-dialog" style="width:400px;">
            <div class="modal-content" id="modal-content">
                <div class="modal-body"  style="overflow: auto; height: 500px;">
                    <div id="provider_tree" class="ztree"></div>
                </div>
                <div class="modal-footer">
                	<button id="savebutton" type="button" name="savebutton"
                        class="btn btn-primary" onclick="groupprovideadd()">保存</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myModal_group_privilege_update1" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 50%">
        <div class="modal-content" id="modal-content">
        </div>
    </div>
</div>
</body>
</html>