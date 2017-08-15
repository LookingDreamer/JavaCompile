<!--工作流示意图子页面-->
<div class="taskworkflow">
	<#list workflowinfoList as workflowinfoItem>
		<#if workflowinfoItem.taskcode=="1">
			<div class="flowCompleted">
				<h4>${workflowinfoItem.taskName}</h4>
				<h5>
					<#if (workflowinfoItem.createtime)??>
						${workflowinfoItem.createtime}
					<#else>
						未知时间
					</#if>
				</h5>
				<h5 class="moreInfo"><span>${workflowinfoItem.workflowInfo!""}</span>&nbsp;</h5>
			</div>
			<div class="flowCompleted fArrow">
				<h4>&nbsp;</h4>
				<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
			<#break>
		</#if>
		<#if !(workflowinfoItem_has_next)>
			<div class="flowEstimate">
				<h4>信息录入</h4>
				<h5>&nbsp;</h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
			<div class="flowEstimate fArrow">
				<h4>&nbsp;</h4>
				<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
		</#if>
	</#list>
	<#list workflowinfoList as workflowinfoItem>
		<#if workflowinfoItem.taskcode=="2">
			<div class="flowCompleted">
				<h4>${workflowinfoItem.taskName}</h4>
				<h5>
					<#if (workflowinfoItem.createtime)??>
						${workflowinfoItem.createtime}
					<#else>
						未知时间
					</#if>
				</h5>
				<h5 class="moreInfo"><span>${workflowinfoItem.workflowInfo!""}</span>&nbsp;</h5>
			</div>
			<div class="flowCompleted fArrow">
				<h4>&nbsp;</h4>
				<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
			<#break>
		<#elseif workflowinfoItem.taskcode=="15">
			<div class="flowCompleted">
				<h4>${workflowinfoItem.taskName}</h4>
				<h5>
					<#if (workflowinfoItem.createtime)??>
						${workflowinfoItem.createtime}
					<#else>
						未知时间
					</#if>
				</h5>
				<h5 class="moreInfo"><span>${workflowinfoItem.comName!""}(${workflowinfoItem.workflowInfo!""})</span>&nbsp;</h5>
			</div>
			<div class="flowCompleted fArrow">
				<h4>&nbsp;</h4>
				<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
			<#break>
		</#if>
		<#if !(workflowinfoItem_has_next)>
			<div class="flowEstimate">
				<h4>报价</h4>
				<h5>&nbsp;</h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
			<div class="flowEstimate fArrow">
				<h4>&nbsp;</h4>
				<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
		</#if>
	</#list>
	<#list workflowinfoList as workflowinfoItem>
		<#if workflowinfoItem.taskcode=="16">
			<div class="flowCompleted">
				<h4>${workflowinfoItem.taskName}</h4>
				<h5>
					<#if (workflowinfoItem.createtime)??>
						${workflowinfoItem.createtime}
					<#else>
						未知时间
					</#if>
				</h5>
				<h5 class="moreInfo"><span>${workflowinfoItem.workflowInfo!""}</span>&nbsp;</h5>
			</div>
			<div class="flowCompleted fArrow">
				<h4>&nbsp;</h4>
				<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
			<#break>
		</#if>
		<#if !(workflowinfoItem_has_next)>
			<div class="flowEstimate">
				<h4>核保</h4>
				<h5>&nbsp;</h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
			<div class="flowEstimate fArrow">
				<h4>&nbsp;</h4>
				<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
		</#if>
	</#list>
	<#list workflowinfoList as workflowinfoItem>
		<#if workflowinfoItem.taskcode=="20">
			<div class="flowCompleted">
				<h4>${workflowinfoItem.taskName}</h4>
				<h5>
					<#if (workflowinfoItem.createtime)??>
						${workflowinfoItem.createtime}
					<#else>
						未知时间
					</#if>
				</h5>
				<h5 class="moreInfo"><span>${workflowinfoItem.workflowInfo!""}</span>&nbsp;</h5>
			</div>
			<div class="flowCompleted fArrow">
				<h4>&nbsp;</h4>
				<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
			<#break>
		</#if>
		<#if !(workflowinfoItem_has_next)>
			<div class="flowEstimate">
				<h4>支付</h4>
				<h5>&nbsp;</h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
			<div class="flowEstimate fArrow">
				<h4>&nbsp;</h4>
				<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
		</#if>
	</#list>
	<#list workflowinfoList as workflowinfoItem>
		<#if workflowinfoItem.taskcode=="21">
			<div class="flowCompleted">
				<h4>${workflowinfoItem.taskName}</h4>
				<h5>
					<#if (workflowinfoItem.createtime)??>
						${workflowinfoItem.createtime}
					<#else>
						未知时间
					</#if>
				</h5>
				<h5 class="moreInfo"><span>${workflowinfoItem.workflowInfo!""}</span>&nbsp;</h5>
			</div>
			<div class="flowCompleted fArrow">
				<h4>&nbsp;</h4>
				<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
			<#break>
		</#if>
	</#list>
	<#list workflowinfoList as workflowinfoItem>
		<#if workflowinfoItem.taskcode=="25">
			<div class="flowCompleted">
				<h4>${workflowinfoItem.taskName}</h4>
				<h5>
					<#if (workflowinfoItem.createtime)??>
						${workflowinfoItem.createtime}
					<#else>
						未知时间
					</#if>
				</h5>
				<h5 class="moreInfo"><span>${workflowinfoItem.comName!""}-${workflowinfoItem.workflowInfo!""}</span>&nbsp;</h5>
			</div>
			<div class="flowCompleted fArrow">
				<h4>&nbsp;</h4>
				<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
			<#break>
		</#if>
		<#if !(workflowinfoItem_has_next)>
			<div class="flowEstimate">
				<h4>承保</h4>
				<h5>&nbsp;</h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
			<div class="flowEstimate fArrow">
				<h4>&nbsp;</h4>
				<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
		</#if>
	</#list>
	<#list workflowinfoList as workflowinfoItem>
		<#if workflowinfoItem.taskcode=="23">
			<div class="flowCompleted">
				<h4>${workflowinfoItem.taskName}</h4>
				<h5>
					<#if (workflowinfoItem.createtime)??>
						${workflowinfoItem.createtime}
					<#else>
						未知时间
					</#if>
				</h5>
				<h5 class="moreInfo"><span>${workflowinfoItem.comName!""}-${workflowinfoItem.workflowInfo!""}</span>&nbsp;</h5>
			</div>
			<div class="flowCompleted fArrow">
				<h4>&nbsp;</h4>
				<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
			<#break>
		</#if>
		<!--
		<#if !(workflowinfoItem_has_next)>
			<div class="flowEstimate">
				<h4>打单</h4>
				<h5>&nbsp;</h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
			<div class="flowEstimate fArrow">
				<h4>&nbsp;</h4>
				<h5><span class="glyphicon glyphicon-arrow-right"></span></h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
		</#if>
		-->
	</#list>
	<#list workflowinfoList as workflowinfoItem>
		<#if workflowinfoItem.taskcode=="24">
			<div class="flowCompleted">
				<h4>${workflowinfoItem.taskName}</h4>
				<h5>
					<#if (workflowinfoItem.createtime)??>
						${workflowinfoItem.createtime}
					<#else>
						未知时间
					</#if>
				</h5>
				<h5 class="moreInfo"><span>${workflowinfoItem.comName!""}-${workflowinfoItem.workflowInfo!""}</span>&nbsp;</h5>
			</div>
			<#break>
		</#if>
		<#if !(workflowinfoItem_has_next)>
			<div class="flowEstimate">
				<h4>配送</h4>
				<h5>&nbsp;</h5>
				<h5 class="moreInfo">&nbsp;</h5>
			</div>
		</#if>
	</#list>
	<!--flowProcessing-->
</div>
