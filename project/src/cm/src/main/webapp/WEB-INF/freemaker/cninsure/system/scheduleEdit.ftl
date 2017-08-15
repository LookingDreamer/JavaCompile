<style>
.modal-body {
	max-height: 400px;
	overflow: scroll;
	overflow-x: hidden
}
</style>
<script type="text/javascript">
	requirejs([ "system/scheduleEdit" ]);

	require([ "jquery", "jqform", "bootstrap", "bootstrapdatetimepicker",
			"bootstrapdatetimepickeri18n","public" ], function($) {
		$(function() {
			initScript();
		});
	});
</script>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true">&times;</button>
	<h4 class="modal-title" id="myModalLabel"><#if
		"${scheEntity.id!''}" != "" >修改<#else>新建</#if>定时计划</h4>
</div>

<div class="modal-body">
  <div  style="margin:0px 8px 0px 8px;">
	<form class="form-horizontal" role="form" id="scheduleForm">
		<div class="row">
			<div class="form-group">
				<label for="tasktypeid" class="col-sm-2 control-label">任务类别&nbsp;<font
					color="#ff0000">*</font></label>
				<div class="col-sm-10">
					<#if "${scheEntity.id!''}" != "" > <input type="text" style="color:#000;"
						class="form-control" id="tasktypename" name="tasktypename"
						value="${scheEntity.tasktypename!''}" readonly="readonly">
					<input type="hidden" id="tasktypeid" name="tasktypeid"
						value="${scheEntity.tasktypeid!''}"> <#else> <select
						class="form-control" id="tasktypeid" name="tasktypeid">
						<option value="" selected="selected">--请选择任务类别--</option> <#list
						taskTypeList as taskType>
						<option value="${taskType.id}">${taskType.codename}</option>
						</#list>
					</select> </#if> <input type="hidden" id="id" name="id"
						value="${scheEntity.id!''}">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label for="taskid" class="col-sm-2 control-label">
					任&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;务&nbsp;<font
					color="#ff0000">*</font>
				</label>
				<div class="col-sm-10">
					<#if "${scheEntity.id!''}" != "" > <input type="text" style="color:#000;"
						class="form-control" id="taskname" name="taskname"
						value="${scheEntity.taskname!''}" readonly="readonly"> <input
						type="hidden" id="taskid" name="taskid"
						value="${scheEntity.taskid!''}"> <#else> <select
						class="form-control" id="taskid" name="taskid">
						<option value="" selected="selected">--请选择任务--</option>
					</select> </#if>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label for="starttime" class="col-sm-2 control-label">起始时间&nbsp;<font
					color="#ff0000">*</font></label>
				<div style="padding-left: 16px;"
					class="input-group date form_datetime col-sm-7"
					data-date="2015-07-02 16:36:07"
					data-date-format="yyyy-mm-dd hh:ii:ss" data-link-field="starttime">
					<input class="form-control" type="text" id="starttimeTxt" style="color:#000;" readonly
						value="${scheEntity.starttimeStr!''}"> <span
						class="input-group-addon"> <span
						class="glyphicon glyphicon-remove"></span>
					</span> <span class="input-group-addon"> <span
						class="glyphicon glyphicon-th"></span>
					</span>
				</div>
				<input type="hidden" id="starttime" name="starttime"
					value="${scheEntity.starttimeStr!''}" />
			</div>
		</div>

		<div class="row">
			<div class="form-group">
				<label class="col-sm-2 control-label">执行周期&nbsp;<font
					color="#ff0000">*</font></label>
				<div class="col-sm-10">
					<div class="col-sm-2">
						<label class="control-label">每隔 </label> <input type="radio"
							name="iscron" id="exePeriodRadioNotcron" value="1"<#if
						"${scheEntity.iscron!''}" != "2">checked="checked"</#if> />
					</div>
					<div class="col-sm-3">
						<input type="text" style="width: 100px;" id="period" name="period"
							placeholder="请输入周期" value="${scheEntity.period!''}"
							class="form-control"<#if "${scheEntity.iscron!''}" ==
						"2">disabled="disabled"</#if> >
					</div>
					<div class="col-sm-4">
						<select id="periodunit" name="periodunit" style="width: 170px;color:#000;"
							class="form-control"<#if "${scheEntity.iscron!''}" ==
							"2">disabled="disabled"</#if> >
							<option value=""<#if "${scheEntity.periodunit!''}" == ""
								> selected="selected" </#if> >--请选择周期单位--</option>
							<option value="1"<#if "${scheEntity.periodunit!''}" ==
								"1" > selected="selected" </#if> >分钟</option>
							<option value="2"<#if "${scheEntity.periodunit!''}" ==
								"2" > selected="selected" </#if> >小时</option>
							<option value="3"<#if "${scheEntity.periodunit!''}" ==
								"3" > selected="selected" </#if> >天</option>
							<option value="4"<#if "${scheEntity.periodunit!''}" ==
								"4" > selected="selected" </#if> >月</option>
						</select>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label class="col-sm-2 control-label"></label>
				<div class="col-sm-10">
					<div class="col-sm-4">
						<label for="exePeriodRadiocron" class="control-label">使用Cron表达式
						</label> <input type="radio" name="iscron" id="exePeriodRadiocron"
							value="2"<#if "${scheEntity.iscron!''}" ==
						"2">checked="checked"</#if> />
					</div>
					<div class="col-sm-5">
						<input id="cronexp" name="cronexp" type="text"
							class="form-control" style="width: 296px; display: block;"
							value="${scheEntity.cronexp!''}" placeholder="请输入Cron表达式"<#if
						"${scheEntity.iscron!''}" != "2">disabled="disabled"</#if> >
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label class="col-sm-2 control-label">启用状态&nbsp;&nbsp;</label>
				<div class="col-sm-10">
					<div class="col-sm-3">
						<label for="useStateStart" class="control-label">启用</label> <input
							type="radio" name="state" id="useStateStart" value="1"<#if
						"${scheEntity.state!''}" != "2">checked="checked"</#if> />
					</div>
					<div class="col-sm-3">
						<label for="useStateStop" class="control-label">停用</label> <input
							type="radio" name="state" id="useStateStop" value="2"<#if
						"${scheEntity.state!''}" == "2">checked="checked"</#if> />

					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label for="ip" class="col-sm-2 control-label">
					主&nbsp;&nbsp;机&nbsp;&nbsp;IP&nbsp;<font color="#ff0000">*</font>
				</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" id="ip" name="ip"
						placeholder="请输入IP地址" value="${scheEntity.ip!''}">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="form-group">
				<label for="comment" class="col-sm-2 control-label">
					描&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述&nbsp;&nbsp;</label>
				<div class="col-sm-10">
					<textarea class="form-control" rows="3" name="comment" id="comment"
						placeholder="请输入描述">${scheEntity.comment!''}</textarea>
				</div>
			</div>
		</div>
	</form>
  </div>
</div>
<div class="modal-footer">
	<button type="button" class="btn btn-default" data-dismiss="modal">关闭
	</button>
	<button type="button" class="btn btn-primary" id="addScheduleBtn">提交</button>
</div>