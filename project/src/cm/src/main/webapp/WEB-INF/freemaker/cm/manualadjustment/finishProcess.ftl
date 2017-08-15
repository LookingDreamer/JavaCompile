<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>已完成流程查看</title>
	<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>
	<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
	
	<script type="text/javascript">
		requirejs([ "cm/mytask/control" ]);
	</script>
	<style type="text/css">
		.task_fixtop {margin-bottom: 0px;position: fixed;top: 0px;z-index: 99999;width: 100%;padding: 5px 15px;}
		.taskworkflow {vertical-align: top;}
		.taskworkflow h4 {font-weight: bold;text-align: center;}
		.taskworkflow h4, .taskworkflow h5 {font-size: 12px;margin: 0;padding: 0;line-height: 18px;}
		.taskworkflow .flowCompleted {margin: 10px 0px;display: inline-block;width: 140px;height: 60px;border-radius: 4px; border: 1px solid; border-color: #BCE8F1;background: #D9EDF7;padding: 3px 5px;}
		.taskworkflow .flowCompleted h4, .taskworkflow .flowCompleted h5 {color: #31708F;}
		.taskworkflow .flowProcessing {margin: 10px 0px;display: inline-block;width: 140px;height: 60px;border-radius: 4px; border: 1px solid; border-color: #EBCCD1;background: #F2DEDE;padding: 3px 5px;}
		.taskworkflow .flowProcessing h4, .taskworkflow .flowProcessing h5 {color: #A94442;}
		.taskworkflow .flowEstimate {margin: 10px 0px;display: inline-block;width: 140px;height: 60px;border-radius: 4px; border: 1px dashed; border-color: #E1E1E8;background: #F7F7F9;padding: 3px 5px;}
		.taskworkflow .flowEstimate h4, .taskworkflow .flowEstimate h5 {color: #8A6D3B;}	
		.taskworkflow .fArrow {width: 28px;line-height: 60px;border-color: transparent;background-color: transparent;}
		.taskworkflow .moreInfo {width: 128px;height: 18px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;}
		.taskworkflow .moreInfo:hover span {position: absolute;display: block;width: 128px;background: #FCF8E3;white-space: normal;}
		.taskworkflow .fArrow .moreInfo {width: 10px;}
		
		
		.manualtask .nav-pills>li>a {border-radius: 4px 4px 0px 0px;padding: 5px 10px;}
		.manualtask .myTabdiv {height: 69px;margin: 0px -15px;}
		.manualtask .myTabdivfix {z-index: 99990;padding: 0px 15px;width: 100%;}
		.manualtask #myTab {background: #FFF;border-bottom: 3px solid #337ab7;}
		.manualtask #myTab h4 {margin: 0;}
		.manualtask #myTabContent {border:1px solid #337ab7;padding:6px;position: relative;margin-bottom: 76px;}
		.manualtask .remark {margin: 0px 0px 5px 0px;border: 1px solid #faebcc;padding: 2px 0px;}
		.manualtask .remark .info {overflow: hidden;white-space: nowrap;text-overflow: ellipsis;}
		.task_fixbottom {border-top: 3px solid #337ab7;position: fixed;bottom: 0px;z-index: 99991;width: 100%;padding: 5px 15px;background: #fff;margin-left: -22px;}
	</style>
</head>
<body>
	<div class="alert alert-warning task_fixtop" role="alert" >
		<b>用户备注：</b>&nbsp;&nbsp;
			<#if (carInsTaskInfoList[0].remarkinfo.usercomment)??>
				${carInsTaskInfoList[0].remarkinfo.usercomment.commentcontent!""}
			</#if>
	</div>
	
	<div class="container-fluid manualtask">
		<#include "cm/common/workFlowView.ftl"/>
	  	
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
				    	<#include "cm/common/subfinishProcess.ftl"/>
			    	</form>
			    </div>
  			</#list>
		</div>
		<!--参与报价的保险公司数目-->
		<input type="hidden" id="carInsTaskInfoListSize" value="${carInsTaskInfoList?size}"/>
	</div>
</body>
</html>

