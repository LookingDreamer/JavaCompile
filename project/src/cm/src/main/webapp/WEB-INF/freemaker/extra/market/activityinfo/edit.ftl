<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "extra/marketactivity" ]);
</script>

<br>

<div class="col-md-12">
				<form  action="save" method="post">
				 <input type="hidden" id="id" name="id" value="${insbMarketActivity.id}">
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">活动名称</label> <input type="text"id="activityname" name="activityname" value="${insbMarketActivity.activityname}">
					</div>
					
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">活动类型</label>
                        <select class="form-control m-left-5" id="activitytype" name="activitytype">
						<#list marketActivityTypeList as code>
                            <option value="${code.codevalue }" <#if code.codevalue == insbMarketActivity.activitytype> selected="selected"</#if> >${code.codename }</option>
						</#list>
                        </select>
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputName">活动限制</label> <input type="text" id="limited" name="limited" value="${insbMarketActivity.limited}">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputOrgName">活动生效时间</label> <input type="text"
							<input type="text"	class="form-control form_datetime" id="effectivetime" name="effectivetime" readonly placeholder="${insbMarketActivity.effectivetime}" value="${insbMarketActivity.effectivetime}">
					</div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputOrgName">活动失效时间</label> <input type="text"
                        <input type="text"	class="form-control form_datetime" id="terminaltime" name="terminaltime" readonly placeholder="${insbMarketActivity.terminaltime}" value="${insbMarketActivity.terminaltime}">
                    </div>
                    <div class="form-group form-inline col-md-4">
						<select id="status2" name="status" >
							<option value ="1" <#if 1 == insbMarketActivity.status> selected="selected"</#if> >正在进行</option>
							<option value ="2" <#if 2 == insbMarketActivity.status> selected="selected"</#if> >已经结束</option>
						</select>
                    </div>
                    <input type="submit" value="保存"> <input id="backbutton" type="button" value="返回">
				</form>
			</div>