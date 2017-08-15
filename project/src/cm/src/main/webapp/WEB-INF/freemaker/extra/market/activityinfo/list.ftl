<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>活动管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "extra/marketactivity" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">查询</div>
		  <div class="panel-body">
		    <div class="row">
			<div class="col-md-12">
				<form role="form" id="policymanagent">
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">活动名称</label> <input type="text"
							class="form-control m-left-5" id="activityname" name="activityname" placeholder="">
					</div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputCode">活动类型</label>
                        <select class="form-control m-left-5" id="activitytype" name="activitytype">
                            <option value="">全部</option>
						<#list marketActivityTypeList as code>
                            <option value="${code.codevalue }">${code.codename }</option>
						</#list>
                        </select>
                    </div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputOrgName">活动状态</label>
                        <select class="form-control m-left-5" id="status" name="status">
                            <option value="">全部</option>
                            <option value="0">编辑</option>
                            <option value="1">开启</option>
                            <option value="2">关闭</option>
                        </select>
                    </div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputCode">生效时间</label> <input type="text"
                        <input type="text"	class="form-control form_datetime" id="effectivetime" name="effectivetime" readonly placeholder="" >
                    </div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputCode">失效时间</label> <input type="text"
                        <input type="text"	class="form-control form_datetime" id="terminaltime" name="terminaltime" readonly placeholder="" >
                    </div>
                    
                     <div class="form-group form-inline col-md-4">
                        <label for="exampleInputCode">活动ID</label> <input type="text"
							class="form-control m-left-5" id="activitycode" name="activitycode" placeholder="">
                    </div>
				</form>
			</div>
		</div>
		  </div>
		  <div class="panel-footer">
						<button id="querybutton" type="button" name="querybutton"
											class="btn btn-primary">查询</button>
						<button id="insertMarketActivity" type="button" name="insertMarketActivity"
											class="btn btn-primary">新增活动</button>
						<button class="btn btn-primary" type="button" name="refresh"
											title="Refresh" id="refresh">
											<i class="glyphicon glyphicon-refresh icon-refresh"></i>
										</button>
                        <button id="resetbutton" type="button" name="resetbutton"
                                  class="btn btn-primary">重置</button>
						<button class="btn btn-primary" type="button" name="toggle" title="Toggle" id="toggle">
												<i class="glyphicon glyphicon-list-alt icon-list-alt"></i>
										</button>
			</div>
		</div>
        <!--活动列表-->
		<div class="panel panel-default">
		  <div class="panel-heading padding-5-5">
		  <div class="row">
				<div class="col-md-2">
                    结果
				</div>
			</div>
			</div>
		    <div class="row">
				<div class="col-md-12">
					<table id="table-list"></table>
				</div>
		  </div>
		</div>
        <!--活动奖品列表-->
        <div id="prizelist" class="panel panel-default">
            <div class="panel-heading padding-5-5">
                <div class="row">
                    <div class="col-md-2">
                        活动奖品列表
                    </div>
                </div>
            </div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputCode">活动名称</label>
                            <input type="text" class="form-control m-left-5" id="temp_activityname" name="temp_activityname" placeholder="" disabled="true" readonly/>
                        </div>
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputCode">活动类型</label>
                            <input type="text" class="form-control m-left-5" id="temp_activitytype" name="temp_activitytype" placeholder="" disabled="true" readonly/>
                        </div>
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputCode">活动状态</label>
                            <input type="text" class="form-control m-left-5" id="temp_status" name="temp_status" placeholder="" disabled="true" readonly/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel-footer">
                <input type="hidden" name="temp_activityid" id="temp_activityid" value=""/>
                <button id="insertMarketActivityPrize" type="button" name="insertMarketActivityPrize"
                        class="btn btn-primary">奖品配置</button>
                <button id="queryprizebutton" type="button" hidden="true" name="queryprizebutton"
                        class="btn btn-primary">查询</button>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <table id="prize-table-list"></table>
                </div>
            </div>
        </div>
        <!--活动奖品列表结束-->
        <!--活动规则列表-->
        <div id="conditionslist" class="panel panel-default">
            <div class="panel-heading padding-5-5">
                <div class="row">
                    <div class="col-md-2">
                        活动规则列表
                    </div>
                </div>
            </div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputCode">活动名称</label>
                            <input type="text" class="form-control m-left-5" id="temp_activityname2" name="temp_activityname" placeholder="" disabled="true" readonly/>
                        </div>
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputCode">活动类型</label>
                            <input type="text" class="form-control m-left-5" id="temp_activitytype2" name="temp_activitytype" placeholder="" disabled="true" readonly/>
                        </div>
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputCode">活动状态</label>
                            <input type="text" class="form-control m-left-5" id="temp_status2" name="temp_status2" placeholder="" disabled="true" readonly/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel-footer">
                <input type="hidden" name="temp_activityid" id="temp_activityid2" value=""/>
                <button id="insertMarketActivityCondition" type="button" name="insertMarketActivityCondition"
                        class="btn btn-primary">规则配置</button>
                <button id="querycondbutton" type="button" hidden="true" name="querycondbutton"
                        class="btn btn-primary">查询</button>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <table id="conditions-table-list"></table>
                </div>
            </div>
        </div>
        <!--活动规则列表结束-->
	</div>

    <!-- 编辑活动 -->
    <div class="modal fade" id="updateMarketActivityModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">编辑活动，请注意！</div>
                        <div class="panel-body">
                            <!--<input type="hidden" name="id" id="s_id" value=""/>-->
                            <table class="table-no-bordered">
                                <tr  style="display: none;">
                                    <td class="text-right"><label>活动编号：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_id" name="id"
                                                                           readonly/></td>
                                </tr>
                                <tr  style="display: none;">
                                    <td class="text-right"><label>活动编号：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_activitycode" name="activitycode"
                                                                           readonly/></td>
                                </tr>
                                <tr  style="display: none;">
                                    <td class="text-right"><label>临时编号：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_tmpcode" name="tmpcode"
                                                                           readonly/></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>活动名称：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control readonlysetting disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_activityname" name="activityname"
                                                                           /></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>活动类型：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <select class="form-control readonlysetting disabledsetting" id="s_activitytype" name="activitytype">
                                        <#list marketActivityTypeList as code>
                                            <option value="${code.codevalue }">${code.codename }</option>
                                        </#list>
                                        </select>
                                       <!-- <input class="form-control readonlysetting disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_activitytype" name="activitytype"
                                                                           />
                                        -->
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>限制人数：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control readonlysetting disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_limited" name="limited"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>生效日期：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control disabledsetting " style="width: 280px;"
                                                                           type="text" id="s_effectivetime" name="effectivetime"
                                                                           readonly /></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>失效日期：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_terminaltime" name="terminaltime" readonly />
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>活动状态：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <select id="s_status" name="status">
                                            <option value ="0">编辑</option>
                                            <option value ="1">开启</option>
                                            <option value ="2">关闭</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>备注：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <textarea class="form-control" rows="3"  id="s_noti" name="noti">
                                        </textarea>
                                    </td>
                                </tr>
                            </table>
                            <div class="col-md-12" style="font-weight: bold; padding-top: 5px;">是否保存？</div>
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
    <!-- 编辑活动结束 -->
    <!-- 新建活动 refreshrefresh-->
    <div class="modal fade" id="insertMarketActivityModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">活动添加页面！</div>
                        <div class="panel-body">
                            <input type="hidden" name="i_id" id="i_id" value=""/>
                            <table class="table-no-bordered">
                     
                                <tr>
                                    <td class="text-right"><label>活动名称：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                           type="text" id="i_activityname" name="activityname"
                                                                           /></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>活动类型：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <select class="form-control " id="i_activitytype" name="activitytype">
										<#list marketActivityTypeList as code>
                                            <option value="${code.codevalue }">${code.codename }</option>
										</#list>
                                        </select>
									</td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>限制人数：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                           type="text" id="i_limited" name="limited"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>生效日期：</label></td>
                                    <td style="padding-bottom:5px;"><input type="text"	class="form-control form_datetime" id="i_effectivetime" name="effectivetime" readonly placeholder="" >
                                </tr>
                                <tr>
                                    <td class="text-right"><label>失效日期：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <input type="text"	class="form-control form_datetime" id="i_terminaltime" name="terminaltime" readonly placeholder="" >
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>活动状态：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <select id="i_status" name="status">
                                            <option value ="0">编辑</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>备注：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <textarea class="form-control" rows="3"  id="i_noti" name="noti">
                                        </textarea>
                                    </td>
                                </tr>
                            </table>
                            <div class="col-md-12" style="font-weight: bold; padding-top: 5px;">是否保存？</div>
                        </div>
                        <div class="panel-footer">
                            <input id="i_execButton" name="i_execButton" type="button" class="btn btn-primary" value="确定"/>
                            <input id="closeButton" name="closeButton" class="btn btn-primary" type="button"
                                   data-dismiss="modal" value="取消"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 新建活动结束 -->
    <!-- 配置活动奖品 -->
    <div class="modal fade" id="insertMarketActivityPrizeModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">活动奖品配置页面！</div>
                        <div class="panel-body">
                            <table class="table-no-bordered">
                                <input type="hidden" name="activityid" id="setting_activityid" value=""/>
                                <tr>
                                    <td class="text-right"><label>活动名称：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <input class="form-control" style="width: 280px;"
                                                                           type="text" id="setting_activityname" name="activityname" disabled="true" readonly
                                            /></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>奖品列表：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <select class="form-control " id="i_prizeid" name="prizeid">
                                        <#list marketPrizeList as code>
                                            <option value="${code.id }">${code.prizename }</option>
                                        </#list>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                            <div class="col-md-12" style="font-weight: bold; padding-top: 5px;">是否保存？</div>
                        </div>
                        <div class="panel-footer">
                            <input id="i_prize_execButton" name="i_prize_execButton" type="button" class="btn btn-primary" value="确定"/>
                            <input id="closeButton" name="closeButton" class="btn btn-primary" type="button"
                                   data-dismiss="modal" value="取消"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 配置活动奖品结束 -->

    <!-- 配置活动规则 -->
    <div class="modal fade" id="insertMarketActivityConditionModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">活动规则配置页面！</div>
                        <div class="panel-body">
                            <form id="i_conditionform">
                            <table class="table-no-bordered">
                                <input type="hidden" name="sourceid" id="condition_activityid" value=""/>
                                <tr>
                                    <td class="text-center"><label>参数名称</label></td>
                                    <td class="text-center"><label>判断式</label></td>
                                    <td class="text-center"><label>参数值</label></td>
                                </tr>
                                <tr>
                                    <td class="padding-5-5">
                                        <select class="form-control" style="width: 200px;" id="condition_paramname"
                                                name="paramname">
                                        <#list paramList as code>
                                            <option value="${code.paramname}">${code.paramcnname}</option>
                                        </#list>
                                        </select>
                                    </td>
                                    <td class="padding-5-5">
                                        <select class=" form-control" style="width: 60px;" id="condition_expression"
                                                name="expression">
                                            <option value="=">=</option>
                                            <option value="!=">!=</option>
                                            <option value=">">&gt;</option>
                                            <option value=">=">&gt;=</option>
                                            <option value="<">&lt;</option>
                                            <option value="<=">&lt;=</option>
                                            <option value="contain">contain</option>
                                        </select></td>
                                    <td class="padding-5-5"><input class="form-control" style="width: 140px;"
                                                                   type="text" id="condition_paramvalue" name="paramvalue"/></td>
                                </tr>
                            </table>
                            </form>
                            <div class="col-md-12" style="font-weight: bold; padding-top: 5px;">是否保存？</div>
                        </div>
                        <div class="panel-footer">
                            <input id="i_condition_execButton" name="i_condition_execButton" type="button" class="btn btn-primary" value="确定"/>
                            <input id="closeButton" name="closeButton" class="btn btn-primary" type="button"
                                   data-dismiss="modal" value="取消"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 配置活动规则结束 -->

    <!-- 编辑活动规则 -->
    <div class="modal fade" id="updateMarketActivityConditionModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">活动规则配置页面！</div>
                        <div class="panel-body">
                            <form id="u_conditionform">
                            <table class="table-no-bordered">
                                <input type="hidden" name="id" id="u_id" value=""/>
                                <input type="hidden" name="sourceid" id="u_activityid" value=""/>
                                <tr>
                                    <td class="text-center"><label>参数名称</label></td>
                                    <td class="text-center"><label>判断式</label></td>
                                    <td class="text-center"><label>参数值</label></td>
                                </tr>
                                <tr>
                                    <td class="padding-5-5">
                                        <select class="form-control" style="width: 200px;" id="u_paramname"
                                                name="paramname">
                                        <#list paramList as code>
                                            <option value="${code.paramname}">${code.paramcnname}</option>
                                        </#list>
                                        </select>
                                    </td>
                                    <td class="padding-5-5">
                                        <select class=" form-control" style="width: 60px;" id="u_expression"
                                                name="expression">
                                            <option value="=">=</option>
                                            <option value="!=">!=</option>
                                            <option value=">">&gt;</option>
                                            <option value=">=">&gt;=</option>
                                            <option value="<">&lt;</option>
                                            <option value="<=">&lt;=</option>
                                            <option value="contain">contain</option>
                                            <option value="un-contain">un-contain</option>
                                        </select></td>
                                    <td class="padding-5-5"><input class="form-control" style="width: 140px;"
                                                                   type="text" id="u_paramvalue" name="paramvalue"/></td>
                                </tr>
                            </table>
                            </form>
                            <div class="col-md-12" style="font-weight: bold; padding-top: 5px;">是否保存？</div>
                        </div>
                        <div class="panel-footer">
                            <input id="u_condition_execButton" name="u_condition_execButton" type="button" class="btn btn-primary" value="确定"/>
                            <input id="closeButton" name="closeButton" class="btn btn-primary" type="button"
                                   data-dismiss="modal" value="取消"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- --编辑活动规则结束-- -->
</body>
</html>
