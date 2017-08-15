<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>人工录单</title>
	<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>
	<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
	<script type="text/javascript">
		requirejs([ "cm/mytask/control" ,"cm/manualadjustment/manualRecord","cm/common/insurancePolicyInfo","cm/manualprice/subMabualPrice"]);
	</script>
	
</head>
<body>
	<div class="alert alert-warning task_fixtop" role="alert" style="color:#ff0000;">
		<b>用户备注：</b>&nbsp;&nbsp;
			<#if (carInsTaskInfoList[0].remarkinfo.usercomment)??>
                【${carInsTaskInfoList[0].remarkinfo.usercomment.typeName}】
				${carInsTaskInfoList[0].remarkinfo.usercomment.userComment.commentcontent!""}
			</#if>
	</div>
	
	<div class="container-fluid manualtask">
		<#include "cm/common/workFlowView1.ftl"/>
	  	
		<div class="myTabdiv">
			<div class="myTabdivfix">
			<ul id="myTab" class="nav nav-pills" role="tablist" style="margin-bottom:0px;">
				<#list carInsTaskInfoList as carInsTaskInfo>
		 			<li <#if (carInsTaskInfo_index = 0)>class="active"</#if> style="margin-right:0px;">
				    	<a href="#${carInsTaskInfo.inscomcode}" role="tab" data-toggle="pill">
				    		<h4>
								${carInsTaskInfo.carInfo.parentinscomname}
							</h4>
							${carInsTaskInfo.carInfo.inscomname}
							<!--$ {carInsTaskInfo.carInfo.insprovincename}分公司-->
							<#if (carInsTaskInfo.carInfo.buybusitype)??>
								-${carInsTaskInfo.carInfo.buybusitype}
							<#else>
								-地面
							</#if>
				    	</a>
				    </li>
	  			</#list>
			</ul>
			</div>
		</div>
		<div id="myTabContent" class="tab-content" >
			<#list carInsTaskInfoList as carInsTaskInfo>
			    <div class="tab-pane fade <#if (carInsTaskInfo_index = 0)>in active</#if>" id="${carInsTaskInfo.inscomcode}" style="border-width:0px;">
			    	<form class="form-inline" role="form" id="insurancePolicyInfoForm${carInsTaskInfo_index}">
				    	<!--引入人工报价子页面-->
				    	<#include "cm/manualadjustment/subManualRecord.ftl"/>
			    	</form>
			    </div>
  			</#list>
		</div>
		<!--参与报价的保险公司数目-->
		<input type="hidden" id="carInsTaskInfoListSize" value="${carInsTaskInfoList?size}"/>
		<input type="hidden" id="isFeeAreaFlag" value="${isfeeflag}"/>
        <!--精灵报价标识-->
        <input type="hidden" id="elfquoteFlag" value="${elfquoteFlag}"/>
	</div>
</body>
</html>

