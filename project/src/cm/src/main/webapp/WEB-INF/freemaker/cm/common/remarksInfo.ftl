<!--用户备注公共页面-->
<form id="remarkform" action="">
<div class="row remark alert alert-warning" role="alert">
	<div class="col-md-6">
		<div class="row">
			<div class="remarklabel"><a href="javascript:window.parent.openDialogForCM('common/remarksinfo/showeditremark?mark=editUserRemark&instanceId=${carInsTaskInfoList[0].carInfo.taskid}&inscomcode=${carInsTaskInfo.inscomcode}&num=${carInsTaskInfo_index}&subInstanceId=${carInsTaskInfo.subInstanceId!""}');">给用户备注：</a></div>
		    <div class="remarkMoreInfo">
				<div id="userRemarkPageInfo${carInsTaskInfo_index}">
					<#if (carInsTaskInfo.remarkinfo.dqusercomment)??>
						${carInsTaskInfo.remarkinfo.dqusercomment.commentcontent}
						-
                        <#if (carInsTaskInfo.remarkinfo.dqusercomment.modifytime)??>
						    ${carInsTaskInfo.remarkinfo.dqusercomment.modifytime?datetime?string("yyyy-MM-dd HH:mm:ss")}
                        <#elseif (carInsTaskInfo.remarkinfo.dqusercomment.createtime)??>
                            ${carInsTaskInfo.remarkinfo.dqusercomment.createtime?datetime?string("yyyy-MM-dd HH:mm:ss")}
                        <#else>
                            null
                        </#if>
					</#if>
				</div>
				<input type='hidden' id='userRemarkEdit${carInsTaskInfo_index}'/>
		    </div>
	    </div>
    </div>
    <div class="col-md-6">
	    <div class="row">
		    <div class="remarklabel"><a href="javascript:window.parent.openDialogForCM('common/remarksinfo/showeditremark?mark=addOperatorRemark&instanceId=${carInsTaskInfoList[0].carInfo.taskid}&inscomcode=${carInsTaskInfo.inscomcode}&num=${carInsTaskInfo_index}&subInstanceId=${carInsTaskInfo.subInstanceId!""}');">给操作员备注：</a></div>
		    <div class="remarkMoreInfo">
				<div id="operatorRemarkPageInfo${carInsTaskInfo_index}">
					<#list carInsTaskInfo.remarkinfo.opcommentList as opcomment>
						<span style="color:#FF6633;" class="glyphicon glyphicon-triangle-right"></span>
						${opcomment.commentcontent}-${opcomment.operator}
						<#if (opcomment.modifytime)??>
							${opcomment.modifytime?datetime?string("yyyy-MM-dd HH:mm:ss")}
						<#elseif (opcomment.createtime)??>
							${opcomment.createtime?datetime?string("yyyy-MM-dd HH:mm:ss")}
                        <#else>
                            null
                        </#if>
						<#if opcomment_has_next><br/></#if>
					</#list>
				</div>
			</div>
		</div>
	</div>
</div>
</form>

