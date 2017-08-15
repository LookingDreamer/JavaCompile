<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>活动管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "extra/miniOrderTrace" ]);
</script>
<body>

	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">
              <h3 style="color:salmon">操作说明：</h3>
              <h5 style="color:salmon">1.每日可手动进行查询；</h5>
              <h5 style="color:salmon">2.跟单无效后，可对超期订单进行退款处理。列表已按照当前时间距离用户起保时间从近到远排序，请优先处理排在前面的订单；</h5>
              <h5 style="color:salmon">3.操作退款后，订单状态变更为“退款中”，3个工作日后自动变更为“退款完毕”。</h5>
          </div>
		  <div class="panel-body">
		    <div class="row">
			<div class="col-md-12">
				<form role="form" id="orderTraceManagement">
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">任务号</label> <input type="text"
							class="form-control m-left-5" id="taskid" name="taskid" placeholder="">
					</div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputCode">下单人姓名</label> <input type="text"
                                                                         class="form-control m-left-5" id="agentname" name="agentname" placeholder="">
                    </div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputOrgName">订单状态</label>
                        <select class="form-control m-left-5" id="taskstate" name="taskstate">
                            <option value="">全部</option>
                            <option value="5">待补齐影像</option>
                            <option value="19">核保失败</option>
                            <option value="12">退款中</option>
                            <option value="13">已退款</option>
                        </select>
                    </div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputCode">车牌号</label> <input type="text"
                                                                           class="form-control m-left-5" id="carlicenseno" name="carlicenseno" placeholder="">
                    </div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputCode">车主姓名</label> <input type="text"
                                                                         class="form-control m-left-5" id="carowner" name="carowner" placeholder="">
                    </div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputCode">处理状态</label>
                        <select class="form-control m-left-5" id="operatestate" name="operatestate">
                            <option value="">全部</option>
						<#list operateStateList as code>
                            <option value="${code.codevalue }">${code.codename }</option>
						</#list>
                        </select>
                    </div>

                    <div class="form-group form-inline col-md-8">
                        <label for="exampleInputCode">时间范围</label>
                        <input type="text"	class="form-control form_datetime" id="createtimestart" name="createtimestart" readonly placeholder="" >
                        &nbsp;&nbsp;--&nbsp;&nbsp;
                        <input type="text"	class="form-control form_datetime" id="createtimeend" name="createtimeend" readonly placeholder="" >
                     </div>

                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputOrgName">当前时间是否超过起保日期</label>
                        <select class="form-control m-left-5" id="startdateflag" name="startdateflag">
                            <option value="">全部</option>
                            <option value="0">是</option>
                            <option value="1">否</option>
                        </select>
                    </div>
				</form>
			</div>
		</div>
		  </div>
		  <div class="panel-footer">
						<button id="querybutton" type="button" name="querybutton"
											class="btn btn-primary">查询</button>
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
        <!--跟单列表-->
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
       </div>

    <!-- 跟进处理 -->
    <div class="modal fade" id="operateModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">跟进处理！</div>
                        <div class="panel-body">
                            <input type="text" hidden="true" id="operateTaskId" />
                            <input type="text" hidden="true" id="operateTaskState" />
                            <input type="text" hidden="true" id="operateProvidercode" />
                            <input type="text" hidden="true" id="orderTraceId" />
                            <table class="table-no-bordered">

                                <tr>
                                     <td style="padding-bottom:5px;">
                                        <span id="waitUpLoadSpan"><input type="radio" name="operatetype" id="waitUpLoad" value="1" checked  >
                                         待用户上传 &nbsp; &nbsp; &nbsp;
                                        </span>
                                         <input type="radio" name="operatetype" id="waitRefund" value="2" >
                                         待用户申请退款 &nbsp; &nbsp; &nbsp;
                                        <input type="radio" name="operatetype" id="immediateRefund" value="3"  >
                                        发起退款 &nbsp; &nbsp;&nbsp;
                                         </td>
                                </tr>
                                <tr id="refundReasonTr" style="display: none">
                                    <td class="text-right">
                                        <select class="form-control" id="refundReason" name="refundReason">
                                            <option value="">请选择退款理由</option>
                                         <#list refundReasonList as code>
                                            <option value="${code.codevalue }">${code.codename }</option>
                                        </#list>
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
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 跟进处理 -->
    <!-- 查看MSG -->
    <div class="modal fade" id="msgModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" id="msgHead" style="font-weight: bold;">MSG！</div>
                        <div class="panel-body">
                           <div id="showmsg"></div>
                        </div>
                        <div class="panel-footer">
                             <input id="msgCloseButton" name="msgCloseButton" class="btn btn-primary" type="button"
                                   data-dismiss="modal" value="确定"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 查看退款理由 -->
</body>
</html>
