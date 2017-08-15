<!-- 待处理任务 -->
<div class="panel panel-default m-bottom-5" id="myOrderManageResultcontent">
	<div class="panel-body" id="myOrderManageResultList">
		<#list historyTask.orderManageList as orderManageItem>
				<form hidden="true" name="detailinfo" id="detailinfo" action="queryorderdetail" method="POST">
					<input type="text" name="id" id="id" value="123"/>
					<input type="text" name="pid" id="pid"/>
					<input type="text" name="codename" id="codename">
				</form>
				<div id="table${orderManageItem_index}" class="row taskrow2">
					<div class="title col-md-6 col-sm-12">
						<label>任务跟踪号:${orderManageItem.maininstanceid}</label>&nbsp;&nbsp;&nbsp;&nbsp;
						<label>被保人:${orderManageItem.bname}</label>&nbsp;&nbsp;&nbsp;&nbsp;
						<label>车牌:${orderManageItem.carlicenseno}</label>&nbsp;&nbsp;&nbsp;&nbsp;
						<label>车型:${orderManageItem.standardfullname}</label>&nbsp;&nbsp;&nbsp;&nbsp;
						<label>车主：${orderManageItem.carname}</label>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<div class="col-md-6 col-sm-12">
						<label>投保地点:${orderManageItem.insprovincename}&nbsp;${orderManageItem.inscityname}</label>&nbsp;&nbsp;&nbsp;&nbsp;
						<label>代理人：${orderManageItem.agentname}(${orderManageItem.jobnum})</label>&nbsp;&nbsp;&nbsp;&nbsp;
						<label>所属团队:${orderManageItem.team}</label>&nbsp;&nbsp;&nbsp;&nbsp;	
					</div>
					<div class="col-md-6 col-sm-12">
						<label>出单网点：${orderManageItem.shortname}</label><br>
						<label>任务创建时间：${orderManageItem.createtime}</label>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<div class="col-md-6 col-sm-12">
						<label>&nbsp;&nbsp;&nbsp;&nbsp;</label>&nbsp;&nbsp;&nbsp;&nbsp;
						<label>&nbsp;&nbsp;&nbsp;&nbsp;</label>&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<div class="col-md-12 taskbtnbar">
						<div class="col-md-9 hidden-sm hidden-xs taskworkflow">
							<ul class="workflowinfo">
								<#list orderManageItem.workflowinfoList as workflowinfo>
									<li>
										<#assign x = "${workflowinfo_index}"?number>
										<#assign y = "${orderManageItem.workFlowIndex}"?number>
										<#if (x lt y)>
											<div class="alert alert-success alertPosition" role="alert">${workflowinfo.taskName}</div>
											<span class="glyphicon glyphicon-arrow-right flowSign" aria-hidden="true"></span>
										<#elseif (x == y)>
											<#if "${workflowinfo.taskCode}" == "33">
												<div class="alert alert-info alertPosition" role="alert">${workflowinfo.taskName}</div>
											<#else>
												<div class="alert alert-info alertPosition" role="alert">${workflowinfo.taskName}</div>
												<span class="glyphicon glyphicon-arrow-right flowSign" aria-hidden="true"></span>
											</#if>
										 <#else>
											<div class="alert alertPosition" role="alert" style="background-color:#DCDCDC;">${workflowinfo.taskName}</div>
											<#if ("${workflowinfo.taskCode}" != "33")>
												<span class="glyphicon glyphicon-arrow-right flowSign" aria-hidden="true"></span>
											</#if>
										</#if>
									</li>
								</#list>
							</ul>
						</div>
				<div class="col-md-3 col-sm-12" style="padding-right:0px;padding-left:0px;">
						<!--
						<button class="btn btn-primary float_right getTaskBtn" type="button" 
							id="${orderManageItem.mInstanceid}_${orderManageItem.sInstanceid}_${orderManageItem.inscomcode}_${orderManageItem.mainOrsub}_${orderManageItem.taskcode}" title="申请任务">
							<span class="glyphicon glyphicon-eye-open"></span>申请任务
						</button>
						-->
						<#if "${orderManageItem.taskcode}" == "18">
									<button class="btn  btn-primary float_right getTaskPrice" type="button" 
									id="${orderManageItem.instanceid}_${orderManageItem.maininstanceid}_${orderManageItem_index}">
									<span class=" glyphicon glyphicon-eye-open"></span>重新报价
								</button>
								<#elseif "${orderManageItem.taskcode}" == "20">
									<button class="btn  btn-primary float_right getTaskProtect" type="button" 
									id="${orderManageItem.instanceid}_${orderManageItem.maininstanceid}_${orderManageItem.inscomcode}_${orderManageItem_index}" >
									<span class="glyphicon glyphicon-eye-open"></span>重新核保
									</button>
								</#if>
								
								<button class="btn  btn-primary float_right reUnderwriting" type="button" style="margin-right:6px;" 
											id="${orderManageItem.instanceid}_${orderManageItem.maininstanceid}_${orderManageItem.taskcode}_${orderManageItem_index}"
											 onclick="myTaskForward('${orderManageItem.orderid}','${orderManageItem.pid}','${orderManageItem.codename}');">
											<span class="glyphicon glyphicon-eye-open"></span> 详细查询
								</button>		
								
				</div>
			</div>
			</div>	
		</#list>	
	</div>
	
	
	
	
	<div class="panel-footer" style="background-color:#fff;" id="myOrderManageResultFoot">
		<div class="row">
			<div class="col-md-12">
				<div class="col-md-6">
				第<label id="lblCurent">${historyTask.currentPage}</label>页/
				共<labelid="lblPageCount">${historyTask.totalPages}</label>页-
				共<label id="lblToatl">${historyTask.totalSize}</label>条数据 
			</div>
			<div class="col-md-6" style="text-align:right;">
				跳转到：第
				<select name="gotoPage" id="gotoPage">
					<#if historyTask.totalPages?? && historyTask.totalPages gt 0>
						<#list 1..historyTask.totalPages as i> 
							<option value="${i}" <#if historyTask.currentPage == i> selected </#if>>${i}</option>
						</#list>
					<#else>
						<option value="1">1</option>
					</#if>
				</select>页&nbsp;
				<#if historyTask.currentPage != 1>
					<a id="tofirst" class="toPageop" style="cursor:pointer;">首页</a>&nbsp;
					<a id="toprevious" class="toPageop" style="cursor:pointer;">上一页</a>&nbsp;
				<#else>
					首页&nbsp;上一页&nbsp;
				</#if>
				<#if historyTask.totalPages gt 0 && historyTask.currentPage != historyTask.totalPages>
					<a id="tonext" class="toPageop" style="cursor:pointer;">下一页</a>&nbsp;
					<a id="tolast" class="toPageop" style="cursor:pointer;">末页</a>
				<#else>
					下一页&nbsp;末页
				</#if>
				<input type="hidden" id="totalPages" value="${historyTask.totalPages}"/>
			</div>
		</div>
	</div>
</div>

