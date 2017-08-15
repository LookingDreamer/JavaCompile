<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>奖品管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "extra/marketprizeinfo" ]);
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
						<label for="exampleInputCode">奖品名称</label> <input type="text"
							class="form-control m-left-5" id="q_prizename" name="prizename" placeholder="">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">奖品类型</label>
                        <select class="form-control m-left-5" id="q_prizetype" name="prizetype">
                            <option value="">全部</option>
						<#list marketPrizeTypeList as code>
                            <option value="${code.codevalue }">${code.codename }</option>
						</#list>
                        </select>
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputName">奖品状态</label>
                        <select class="form-control m-left-5" id="q_status" name="status">
                            <option value="">全部</option>
                            <option value="0">编辑</option>
                            <option value="1">开启</option>
                            <option value="2">关闭</option>
                        </select>
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputOrgName">奖品生效时间</label> <input type="text"
							<input type="text"	class="form-control form_datetime" id="q_effectivetime" name="effectivetime" readonly placeholder="" >
					</div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputOrgName">奖品失效时间</label> <input type="text"
                        <input type="text"	class="form-control form_datetime" id="q_terminaltime" name="terminaltime" readonly placeholder="" >
                    </div>
				</form>
			</div>
		</div>
		  </div>
		  <div class="panel-footer">
						<button id="querybutton" type="button" name="querybutton"
											class="btn btn-primary">查询</button>
						<button id="save" type="button" name="save"
											class="btn btn-primary">新增</button>
						<button id="resetbutton" type="button" name="resetbutton"
											class="btn btn-primary">重置</button>
						<button class="btn btn-primary" type="button" name="refresh"
											title="Refresh" id="refresh">
											<i class="glyphicon glyphicon-refresh icon-refresh"></i>
										</button>
						<button class="btn btn-primary" type="button" name="toggle" title="Toggle" id="toggle">
												<i class="glyphicon glyphicon-list-alt icon-list-alt"></i>
										</button>
			</div>
		</div>
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

        <!--活动规则列表-->
        <div id="conditionslist" class="panel panel-default">
            <div class="panel-heading padding-5-5">
                <div class="row">
                    <div class="col-md-2">
                        奖品规则列表
                    </div>
                </div>
            </div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputCode">奖品名称</label>
                            <input type="text" class="form-control m-left-5" id="temp_prizename" name="temp_prizename" placeholder="" disabled="true" readonly/>
                        </div>
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputCode">奖品类型</label>
                            <input type="text" class="form-control m-left-5" id="temp_prizetype" name="temp_prizetype" placeholder="" disabled="true" readonly/>
                        </div>
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputCode">奖品状态</label>
                            <input type="text" class="form-control m-left-5" id="temp_status" name="temp_status" placeholder="" disabled="true" readonly/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel-footer">
                <input type="hidden" name="temp_prizeid" id="temp_prizeid" value=""/>
                <button id="insertMarketPrizeCondition" type="button" name="insertMarketPrizeCondition"
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

    <!-- 新建奖品refresh refresh-->
    <div class="modal fade" id="insertMarketPrizeModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">奖品添加页面！</div>
                        <div class="panel-body">
                            <input type="hidden" name="id" id="i_id" value=""/>
                            <table class="table-no-bordered">
                                <tr>
                                    <td class="text-right"><label>奖品名称：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                           type="text" id="i_prizename" name="prizename"
                                            /></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>奖品类型：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <select class="form-control" id="i_prizetype" name="prizetype">
										<#list marketPrizeTypeList as code>
                                            <option value="${code.codevalue }">${code.codename }</option>
										</#list>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>数额：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                           type="text" id="i_counts" name="counts"/>
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
                                    <td class="text-right"><label>有效天数：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                           type="text" id="i_effectivedays" name="effectivedays"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>奖品状态：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <select id="i_status" name="status">
                                            <option value ="0" selected>编辑</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>自动使用：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <select id="i_autouse" name="autouse">
                                            <option value ="0">手动</option>
                                            <option value ="1">自动</option>
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
                            <input id="i_closeButton" name="i_closeButton" class="btn btn-primary" type="button"
                                   data-dismiss="modal" value="取消"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 新建奖品结束 -->
    <!-- 编辑奖品-->
    <div class="modal fade" id="updateMarketPrizeModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">奖品编辑页面！</div>
                        <div class="panel-body">
                            <input type="hidden" name="id" id="s_id" value=""/>
                            <table class="table-no-bordered">
                                <tr>
                                    <td class="text-right"><label>奖品名称：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control readonlysetting disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_prizename" name="prizename"
                                            /></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>奖品类型：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <select class="form-control  readonlysetting disabledsetting" id="s_prizetype" name="prizetype">
										<#list marketPrizeTypeList as code>
                                            <option value="${code.codevalue }">${code.codename }</option>
										</#list>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>数额：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control  readonlysetting disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_counts" name="counts"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>生效日期：</label></td>
                                    <td style="padding-bottom:5px;">
										<input type="text"	class="form-control form_datetime disabledsetting" id ="s_effectivetime" name="effectivetime" readonly placeholder="" >
                                </tr>
                                <tr>
                                    <td class="text-right"><label>失效日期：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <input type="text"	class="form-control form_datetime disabledsetting" id="s_terminaltime" name="terminaltime" readonly placeholder="" >
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>有效天数：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control  readonlysetting disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_effectivedays" name="effectivedays"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>奖品状态：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <select id="s_status" name="status">
                                            <option value ="0">编辑</option>
                                            <option value ="1">开启</option>
                                            <option value ="2">关闭</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>使用方式：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <select class="readonlysetting disabledsetting" id="s_autouse" name="autouse">
                                            <option value ="0">手动</option>
                                            <option value ="1">自动</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>备注：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <textarea class="form-control  readonlysetting disabledsetting" rows="3"  id="s_noti" name="noti">

                                        </textarea>
                                    </td>
                                </tr>
                            </table>
                            <div class="col-md-12" style="font-weight: bold; padding-top: 5px;">是否保存？</div>
                        </div>
                        <div class="panel-footer">
                            <input id="s_execButton" name="s_execButton" type="button" class="btn btn-primary" value="确定"/>
                            <input id="s_closeButton" name="s_closeButton" class="btn btn-primary" type="button"
                                   data-dismiss="modal" value="取消"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 编辑奖品结束 -->


    <!-- 配置奖品使用规则 -->
    <div class="modal fade" id="insertMarketPrizeConditionModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">奖品规则配置页面！</div>
                        <div class="panel-body">
                            <form id="i_condition">
                            <table class="table-no-bordered">
                                <input type="hidden" name="sourceid" id="condition_prizeid" value=""/>
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
                                            <option value="un-contain">un-contain</option>
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
    <!-- 配置奖品使用规则结束 -->

    <!-- 编辑奖品使用规则 -->
    <div class="modal fade" id="updateMarketPrizeConditionModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">规则配置页面！</div>
                        <div class="panel-body">
                            <form id="u_condition">
                            <table class="table-no-bordered">
                                <input type="hidden" name="id" id="u_id" value=""/>
                                <input type="hidden" name="sourceid" id="u_prizeid" value=""/>
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
    <!-- --编辑奖品使用规则-- -->

</body>
</html>
