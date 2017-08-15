<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>渠道管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "extra/minichannel" ]);
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
						<label for="exampleInputCode">渠道名称</label>
                        <input type="text"
							class="form-control m-left-5" id="channelname" name="channelname" placeholder="">
					</div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputCode">渠道编码</label>
                        <input type="text"
                               class="form-control m-left-5" id="channelcode" name="channelcode" placeholder="">
                    </div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputCode">截止日期</label> <input type="text"
                        <input type="text"	class="form-control form_datetime" id="terminaldate" name="terminaldate" readonly placeholder="" >
                    </div>
				</form>
			</div>
		</div>
		  </div>
		  <div class="panel-footer">
						<button id="querybutton" type="button" name="querybutton"
											class="btn btn-primary">查询</button>
						<button id="insertMiniChannel" type="button" name="insertMiniChannel"
											class="btn btn-primary">新增渠道</button>
						<button class="btn btn-primary" type="button" name="refresh"
											title="Refresh" id="refresh">
											<i class="glyphicon glyphicon-refresh icon-refresh"></i>
										</button>
                        <button id="resetbutton" type="button" name="resetbutton"
                                  class="btn btn-primary">重置</button>
						<button class="btn btn-primary" type="button" name="toggle" title="Toggle" id="toggle">
												<i class="glyphicon glyphicon-list-alt icon-list-alt"></i>
										</button>
                        <button id="querybuttonhide" type="button" name="querybuttonhide" hidden="true"
                            class="btn btn-primary">查询2</button>
			</div>
		</div>
        <!--渠道列表-->
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
        <!--推广途径列表-->
        <div id="channelwaylist" class="panel panel-default">
            <div class="panel-heading padding-5-5">
                <div class="row">
                    <div class="col-md-2">
                        推广途径列表
                    </div>
                </div>
            </div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputCode">渠道名称</label>
                            <input type="text" class="form-control m-left-5" id="temp_channelname" name="temp_channelname" placeholder="" disabled="true" readonly/>
                        </div>
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputCode">渠道编码</label>
                            <input type="text" class="form-control m-left-5" id="temp_channelcode" name="temp_channelcode" placeholder="" disabled="true" readonly/>
                        </div>
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputCode">截止日期</label>
                            <input type="text" class="form-control m-left-5" id="temp_terminaldate" name="temp_terminaldate" placeholder="" disabled="true" readonly/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel-footer">
                <input type="hidden" name="temp_channelid" id="temp_channelid" value=""/>
                <button id="insertMiniChannelWay" type="button" name="insertMiniChannelWay"
                        class="btn btn-primary">新增推广途径</button>
                <button id="querychannelwaybutton" type="button" hidden="true" name="querychannelwaybutton"
                        class="btn btn-primary">查询</button>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <table id="channelway-table-list"></table>
                </div>
            </div>
        </div>
        <!--推广途径列表结束--> 
	</div>

    <!-- 编辑渠道-->
    <div class="modal fade" id="updateChannelModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">渠道编辑！</div>
                        <div class="panel-body">
                            <!--<input type="hidden" name="id" id="s_id" value=""/>-->
                            <table class="table-no-bordered">
                                <tr>
                                    <td class="text-right"><label>渠道ID：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_id" name="id"
                                                                           readonly/></td>
                                </tr>
                                <tr  style="display: none">
                                    <td class="text-right"><label>临时编码：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control disabledsetting"   style="width: 280px;"
                                                                           type="text" id="s_tempcode" name="tempcode"
                                                                           readonly/></td>
                                </tr>
                                <tr  style="display: none">
                                    <td class="text-right"><label>渠道推广数量：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control disabledsetting"   style="width: 280px;"
                                                                           type="text" id="s_waynum" name="waynum"
                                                                           readonly/></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>渠道编码：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control readonlysetting disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_channelcode" name="channelcode"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>渠道名称：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control " style="width: 280px;"
                                                                           type="text" id="s_channelname" name="channelname"
                                                                           /></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>截止日期：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                           type="text" id="s_terminaldate" name="terminaldate" readonly />
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>备注：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <textarea class="form-control" rows="3"  id="s_noti" name="noti"></textarea>
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
    <!-- 编辑渠道 -->
    <!-- 新建渠道开始 refreshrefresh-->
    <div class="modal fade" id="insertChannelModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">渠道添加页面！</div>
                        <div class="panel-body">
                            <input type="hidden" name="i_id" id="i_id" value=""/>
                            <table class="table-no-bordered">
                                <tr>
                                    <td class="text-right"><label>渠道名称：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control" style="width: 280px;"
                                                                           type="text" id="i_channelname" name="channelname"
                                                                           /></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>截止日期：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <input type="text"	class="form-control form_datetime" id="i_terminaldate" name="terminaldate" readonly placeholder="" >
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>渠道备注：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <textarea class="form-control" rows="3"  id="i_noti" name="noti"></textarea>
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
    <!-- 新建渠道结束 -->
    <!-- 新建推广途径 -->
    <div class="modal fade" id="insertChannelWayModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">新建推广途径页面！</div>
                        <div class="panel-body">
                            <table class="table-no-bordered">
                                <tr>
                                    <td class="text-right"><label>渠道编码：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <input class="form-control" style="width: 280px;" disabled="true" readonly
                                               type="text" id="i_waychannelcode" name="waychannelcode"
                                                /></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>推广途径名称：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <input class="form-control" style="width: 280px;"
                                                                           type="text" id="i_wayname" name="wayname"
                                            /></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>推广途径备注：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <textarea class="form-control" rows="3"  id="i_waynoti" name="noti"></textarea>
                                    </td>
                                </tr>

                            </table>
                            <div class="col-md-12" style="font-weight: bold; padding-top: 5px;">是否保存？</div>
                        </div>
                        <div class="panel-footer">
                            <input id="i_way_execButton" name="i_way_execButton" type="button" class="btn btn-primary" value="确定"/>
                            <input id="closeButton" name="closeButton" class="btn btn-primary" type="button"
                                   data-dismiss="modal" value="取消"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 新建推广途径结束 -->
    <!-- 编辑渠道-->
    <div class="modal fade" id="updateChannelWayModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">推广途径编辑！</div>
                        <div class="panel-body">
                            <input type="hidden" name="id" id="w_id" value=""/>
                            <input type="hidden" name="channelcode" id="w_channelcode" value=""/>
                            <input type="hidden" name="waycode" id="w_waycode" value=""/>
                            <table class="table-no-bordered">
                                <tr>
                                    <td class="text-right"><label>推广途径名称：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control " style="width: 280px;"
                                                                           type="text" id="w_wayname" name="wayname"
                                            /></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>备注：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <textarea class="form-control" rows="3"  id="w_noti" name="noti"></textarea>
                                    </td>
                                </tr>
                            </table>
                            <div class="col-md-12" style="font-weight: bold; padding-top: 5px;">是否保存？</div>
                        </div>
                        <div class="panel-footer">
                            <input id="w_execButton" name="w_execButton" type="button" class="btn btn-primary" value="确定"/>
                            <input id="closeButton" name="closeButton" class="btn btn-primary" type="button"
                                   data-dismiss="modal" value="取消"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 编辑渠道 -->

</body>
</html>
