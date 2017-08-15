<!--工作流示意图子页面-->
<div class="taskworkflow">
	 <#list workflowinfoList as workflowinfo> 
	<!-- 已完成 -->
	<#if workflowinfo.taskcode==1>
	<div class="flowCompleted ">
		<!-- ${workflowinfo.taskname}---信息录入 ----2015-10-10 12:13:14--代理人姓名(工号)--> 
		<h4>${workflowinfo.taskname}</h4>
		<h5>${workflowinfo.createtime}</h5>
		<h5 class="moreInfo">
		${workflowinfo.name} ${workflowinfo.jobnum}
		</h5>
	</div>
	</#if>
	<#if "${workflowinfo.taskcode}"=="2">
	<div class="flowCompleted fArrow">
		<h4>&nbsp;</h4>
		<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
		<h5 class="moreInfo">&nbsp;</h5>
	</div>
	<div class="flowCompleted ">
		<h4>${workflowinfo.taskname}</h4>
		<h5>${workflowinfo.createtime}</h5>
		<h5 class="moreInfo"><span>${workflowinfo.operator}</span>&nbsp;</h5>
	</div>
	<#elseif "${workflowinfo.taskcode}"=="14">
	<div class="flowCompleted fArrow">
		<h4>&nbsp;</h4>
		<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
		<h5 class="moreInfo">&nbsp;</h5>
	</div>
	<div class="flowCompleted ">
		<h4>${workflowinfo.taskname}</h4>
		<h5>${workflowinfo.createtime}</h5>
		<h5 class="moreInfo"><span>${workflowinfo.operator}</span>&nbsp;</h5>
	</div>
	
	<!-- 预计 -->
	<#elseif "${workflowinfo.taskcode}"=="16">
	<div class="flowCompleted fArrow">
		<h4>&nbsp;</h4>
		<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
		<h5 class="moreInfo">&nbsp;</h5>
	</div>
	<div class="flowEstimate">
		<h4>${workflowinfo.taskname}</h4>
		<h5>${workflowinfo.createtime}</h5>
		<h5 class="moreInfo">&nbsp;</h5>
	</div>
	</#if>
	<#if "${workflowinfo.taskcode}"=="20">
	<!-- 预计 -->
	<div class="flowEstimate fArrow">
		<h4>&nbsp;</h4>
		<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
		<h5 class="moreInfo">&nbsp;</h5>
	</div>
	<div class="flowEstimate">
		<h4>${workflowinfo.taskname}</h4>
		<h5>${workflowinfo.createtime}</h5>
		<h5 class="moreInfo">${workflowinfo.operator}</h5>
	</div>
	
	</#if>
	<#if "${workflowinfo.taskcode}"=="29">
	<div class="flowEstimate fArrow">
		<h4>&nbsp;</h4>
		<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
		<h5 class="moreInfo">&nbsp;</h5>
	</div>
	<!-- 预计 -->
	<div class="flowEstimate">
		<h4>${workflowinfo.taskname}</h4>
		<h5>${workflowinfo.createtime}</h5>
	</div>
	</#if>
</#list> 
</div>
