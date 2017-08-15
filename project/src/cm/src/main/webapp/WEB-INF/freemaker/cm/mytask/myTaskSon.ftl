<div class="panel-body">
<#list allData.rowList as row>
	<!--<div id="bg${row_index}" class="bg">
		<button id="clsbtn${row_index}" style="float:right;color:black;" type="button" onclick="closeBg('${row_index}');">X</button>
		<span id="doublePaySpan${row_index}" style="color:white;font-size:35px;margin-left:18px;margin-top:28px;">查询结果</span><br><br><br><br>
		<span id="spanBg${row_index}" style="color:white;margin-left:18px;"></span>
	</div>-->
	<#if "${row.taskcode }" == "21">
		<div id="table${row_index}" class="row taskrow1">
			<div class="title col-md-6 col-sm-12 showright">
				<label class="shortlabel">车牌：${row.carlicenseno}</label>
				<label class="shortlabel">被保人：${row.insuredname}</label>
				<label>任务跟踪号：${row.maininstanceid}</label>
				<#if row.purchaserchannel>
				<img src="${staticRoot}/images/system/resource/resource1.png" style="width:35px;height:35px;"/>
				</#if>
				<br>
				<label>代理人：${row.agentname}</label>
			</div>
			<div class="col-md-6 col-sm-12">
				<#list row.policyInfo as policyinfo>
					<#if "${policyinfo.risktype }" == "0">
						<label>商业险：</label>${policyinfo.premium}
						<label>投保单号：</label>${policyinfo.proposalformno}
					</#if>
				</#list>
			</div>
			<div class="col-md-6 col-sm-12">
				<#list row.policyInfo as policyinfo>
					<#if "${policyinfo.risktype }" == "1">
						<label>交强险：</label>${policyinfo.premium}
						<label>投保单号：</label>${policyinfo.proposalformno}
					</#if>
				</#list>
			</div>
			<div class="col-md-6 col-sm-12">
				<label class="hidden-sm hidden-xs">任务创建时间：${row.createtime}</label><br>
				<label class="hidden-sm hidden-xs">配送方式：${row.delivetype}</label>
				<label>支付方式：${row.paychannelname}</label>
			</div>
			<div class="col-md-6 col-sm-12">
				<label>支付号：${row.paymentransaction}</label>
				<label>订单号：${row.orderno}</label>
			</div>
			<div class="col-md-6 col-sm-12">
				<label>给用户备注：${row.usercommentlist}</label><br>
				<label>给操作人备注：${row.operatorcommentList}</label></br>
				<label class="hidden-sm hidden-xs">所属团队：${row.team}</label>
				<label <#if row.purchaserchannel>style="color:red"</#if>>渠道来源：${row.purchaserchannel!'-'}</label>
			</div>
			<div class="col-md-6 col-sm-12">
				<label>供应商：${row.prvname}</label>
				<label>网点：${row.shortname}</label>
			</div>
			<div class="col-md-12 taskbtnbar">
				<div class="col-md-9 hidden-sm hidden-xs taskworkflow">
					<ul class="workflowinfo">
						<#list row.workflowinfoList as workflowinfo>
							<li>
								<#assign x = "${workflowinfo_index}"?number>
								<#assign y = "${row.workFlowIndex}"?number>
								<#if (x lt y)>
									<div class="alert alert-success alertPosition" role="alert">${workflowinfo.taskName}</div>
									<span class="glyphicon glyphicon-arrow-right flowSign" aria-hidden="true"></span>
								<#elseif (x == y)>
									<#if "${workflowinfo.taskCode}" == "24">
										<div class="alert alert-info alertPosition" role="alert">${workflowinfo.taskName}</div>
									<#else>
										<div class="alert alert-info alertPosition" role="alert">${workflowinfo.taskName}</div>
										<span class="glyphicon glyphicon-arrow-right flowSign" aria-hidden="true"></span>
									</#if>
								 <#else>
									<div class="alert alertPosition" role="alert" style="background-color:#DCDCDC;">${workflowinfo.taskName}</div>
									<#if ("${workflowinfo.taskCode}" != "24")>
										<span class="glyphicon glyphicon-arrow-right flowSign" aria-hidden="true"></span>
									</#if>
								</#if>
							</li>
						</#list>
					</ul>
				</div>
				<div class="col-md-3 col-sm-12" style="padding-right:0px;padding-left:0px;">
					<button class="btn btn-primary mybtn" onclick="javascript:window.parent.openDialogForCM('business/mytask/preTransformTask?taskcode=${row.taskcode}&maintaskid=${row.maininstanceid}&providerid=${row.inscomcode}');">
						<span class="hidden-sm hidden-xs">任务转发</span>
						<span class="glyphicon glyphicon-transfer visible-sm visible-xs"></span>
					</button>
					<button class="btn btn-primary mybtn" style="min-width:80px;" type="button" onclick="myTaskForward('${row.maininstanceid}','${row.taskcode}','${row.inscomcode}');" id="toperating1">二次支付</button>
					<!--<button class="btn btn-primary mybtn" type="button" onclick="myTaskForward('${row.instanceid}','${row.taskcode}');"><span class="glyphicon glyphicon-eye-open"></span>二次支付</button>-->
				</div>
			</div>
		</div>
		<#elseif "${row.taskcode }" == "999">
		<div id="table${row_index}" class="row taskrow2">
			<div class="title col-md-6 col-sm-12">
				<label>代理人：${row.agentname}</label>
				<label>手机：${row.shortname}</label>
				<label>所在地区：${row.insprovincename} ${row.inscityname} ${row.inscomcode} </label>
			</div>
			<div class="col-md-6 col-sm-12">
				<label>主营业务：
					<#if "${row.standardfullname}" == "0">车险
						<#elseif "${row.standardfullname}" == "1">寿险
						<#elseif "${row.standardfullname}" == "2">非车险
						<#elseif "${row.standardfullname}" == "3">综合
					</#if>
				</label>
			</div>
			<div class="col-md-6 hidden-sm hidden-xs">
				<label>任务创建时间：${row.createtime}</label><br>
			</div>
			<div class="col-md-6 col-sm-12 taskbtnbar">
				<label>&nbsp;</label>
				<button class="btn btn-primary mybtn" type="button" name="manualprice" onclick="myTaskForward('${row.maininstanceid}','${row.taskcode}');">认证</button>
			</div>
		</div>
			  
		<#else>
		<div id="table${row_index}" class="row taskrow3">
			<div class="title col-md-6 col-sm-12 showright">
				<label class="shortlabel">车牌：${row.carlicenseno}</label>
				<label class="shortlabel">被保人：${row.insuredname}</label>
				<label>任务跟踪号：${row.maininstanceid}</label>
				<#if row.purchaserchannel>
				<img src="${staticRoot}/images/system/resource/resource1.png" style="width:35px;height:35px;"/>
				</#if>
				<br>
				<label>车型：${row.standardfullname}</label>
			</div>
			<div class="col-md-6 col-sm-12">
				<label>供应商：${row.prvname}</label><br>
				<label>出单网点：${row.shortname}</label><br>
			</div>
			<div class="col-md-6 col-sm-12">
				<label>投保地区：${row.insprovincename} ${row.inscityname}</label>
				<label>代理人：${row.agentname}</label>
				<br>
				<label <#if row.purchaserchannel>style="color:red"</#if>>渠道来源：${row.purchaserchannel!'-'}</label>
				<br>
                <label>用户备注：${row.uscs}</label>
			</div>
			<div class="col-md-6 col-sm-12">
				<label class="hidden-sm hidden-xs">所属团队：${row.team}</label><br>
				<label class="hidden-sm hidden-xs">任务创建时间：${row.createtime}</label><br>
				<label>给用户备注：${row.usercommentlist}</label><br>
				<label>给操作人备注：${row.operatorcommentList}</label>
			</div>
			<div class="col-md-12 taskbtnbar">
				<div class="col-md-9 hidden-sm hidden-xs taskworkflow">
					<ul class="workflowinfo">
			 			<#list row.workflowinfoList as workflowinfo>
							<li>
								<#assign x = "${workflowinfo_index}"?number>
								<#assign y = "${row.workFlowIndex}"?number>
								<#if (x lt y)>
									<div class="alert alert-success alertPosition" role="alert">${workflowinfo.taskName}</div>
									<span class="glyphicon glyphicon-arrow-right flowSign" aria-hidden="true"></span>
								<#elseif (x == y)>
									<#if "${workflowinfo.taskCode}" == "24">
										<div class="alert alert-info alertPosition" role="alert">${workflowinfo.taskName}</div>
									<#else>
										<div class="alert alert-info alertPosition" role="alert">${workflowinfo.taskName}</div>
										<span class="glyphicon glyphicon-arrow-right flowSign" aria-hidden="true"></span>
									</#if>
								 <#else>
									<div class="alert alertPosition" role="alert" style="background-color:#DCDCDC;">${workflowinfo.taskName}</div>
									<#if ("${workflowinfo.taskCode}" != "24")>
										<span class="glyphicon glyphicon-arrow-right flowSign" aria-hidden="true"></span>
									</#if>
								</#if>
							</li>
						</#list>
			 		</ul>
					<!-- 这里不显示上一步和查看流程按钮  直接显示流程图 
					<label>上一步流程：</label><label id="tlast">${row.lastnode.taskname}(${row.lastnode.taskstatestr})</label>
					<a style="cursor:pointer;" onclick="showTaskFlow('${row.instanceid}','${row_index}');">查看任务流程</a>
					-->
				</div>
				<div class="col-md-3 col-sm-12" style="padding-right:0px;">
					<button class="btn btn-primary mybtn" onclick="javascript:window.parent.openDialogForCM('business/mytask/preTransformTask?taskcode=${row.taskcode}&maintaskid=${row.maininstanceid}&providerid=${row.inscomcode}');">
						<span class="hidden-sm hidden-xs">任务转发</span>
						<span class="glyphicon glyphicon-transfer visible-sm visible-xs"></span>
					</button>
					<button class="btn btn-primary mybtn" style="min-width:80px;" type="button" onclick="myTaskForward('${row.maininstanceid}','${row.taskcode}','${row.inscomcode}');" id="toperating1">${row.codename}</button>
				</div>
			</div>
		</div>
		</#if>
	</#list>
	</div>
<div class="panel-footer">
	<div class="">
<!-- 		我的任务不需要分页，此处现实业管的作业量  当日完成任务数 当月完成任务数 点击弹出新窗口工作量统计表（统计表暂不实现） -->
		<label>当日完成任务数：${countDayTask}</label>&nbsp;&nbsp;&nbsp;
		<label>当月完成任务数：${countMonthTask}</label>
		<!-- <button class="btn btn-primary mybtn" style="min-width:80px;" type="button" onclick="$.cmTaskList('my', 'list4deal', true);" id="xxxx1">点击刷新</button>-->
	</div>
</div>
<input id="currentpage" type="hidden" value="${allData.myTaskVo.currentpage}">
<input id="totalpage" type="hidden" value="${allData.myTaskVo.totalpage}">
<input type="hidden" id="soncarlicenseno" value="${allData.myTaskVo.carlicenseno}"/>
<input type="hidden" id="sonjobnumtype" value="${allData.myTaskVo.jobnumtype}"/>
<input type="hidden" id="sonstartdate" value="${allData.myTaskVo.startdate}"/>
<input type="hidden" id="sonenddate" value="${allData.myTaskVo.enddate}"/>
<input type="hidden" id="soninsuredname" value="${allData.myTaskVo.insuredname}"/>
<input type="hidden" id="sontaskcode" value="${allData.myTaskVo.taskcode}"/>
<input type="hidden" id="soncurrentpage" value="${allData.myTaskVo.currentpage}"/>
<input type="hidden" id="sontotalpage" value="${allData.myTaskVo.totalpage}"/>
<input type="hidden" id="sonsimplequery" value="${allData.myTaskVo.simplequery}"/>


