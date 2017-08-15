<div class="modal-body">
	<div class="div-height">
		<table class="table table-bordered ">
			<tr>
				<td class="col-md-3 control-label" align="right">代理人工号：</td>
				<td class="col-md-3">${agent.jobnum }</td>
				<td class="col-md-6"></td>
			</tr>
			<tr>
				<td class="col-md-3 control-label" align="right">手机账号：</td>
				<td class="col-md-3">${agent.phone }</td>
				<td class="col-md-6"></td>
			</tr>
			<tr>
				<td class="col-md-3 control-label" align="right">真实姓名：</td>
				<td class="col-md-3">${agent.name }</td>
				<td class="col-md-6"></td>
			</tr>
			<tr>
				<td class="col-md-3 control-label" align="right">主法人机构网点：</td>
				<td class="col-md-3">${stationdeptname }</td>
				<td class="col-md-6"></td>
			</tr>
			<tr>
				<td class="col-md-3 control-label" align="right">用户备注：</td>
				<td class="col-md-3" style="word-break:break-all;word-wrap:break-word;white-space: pre-wrap; ">${comment }</td>
				<td class="col-md-6"></td>
			</tr>
			<tr>
				<td class="col-md-3 control-label" align="right">身份证号：</td>
				<td class="col-md-3">${agent.idno }</td>
				<td class="col-md-6">
					<button onclick='javascript:window.parent.openImgView("common/insurancepolicyinfo/agentview?jobnum=${agent.jobnum }&types=ids")'>
						<a href='javascript:window.parent.openImgView("common/insurancepolicyinfo/agentview?jobnum=${agent.jobnum }&types=ids")'>
							查看影像
	   					</a>
					</button>
				</td>
			</tr>
			<tr>
				<td class="col-md-3 control-label" align="right">银行卡号：</td>
				<td class="col-md-3">${agent.bankcard }</td>
				<td class="col-md-6">
				<button onclick='javascript:window.parent.openImgView("common/insurancepolicyinfo/agentview?jobnum=${agent.jobnum }&types=bank")'>
					<a href='javascript:window.parent.openImgView("common/insurancepolicyinfo/agentview?jobnum=${agent.jobnum }&types=bank")'>
						查看影像
	   				</a>
   				</button>
   				</td>
			</tr>
			<tr>
				<td class="col-md-3 control-label" align="right">资格证号：</td>
				<td class="col-md-3">${agent.cgfns }</td>
				<td class="col-md-6">
					<button onclick='javascript:window.parent.openImgView("common/insurancepolicyinfo/agentview?jobnum=${agent.jobnum }&types=qus")'>
						<a href='javascript:window.parent.openImgView("common/insurancepolicyinfo/agentview?jobnum=${agent.jobnum }&types=qus")'>
							查看影像
	   					</a>
					</button>
				</td>
			</tr>
			<tr>
				<td class="col-md-3 control-label" align="right">执业证号：</td>
				<td class="col-md-3">${agent.licenseno }</td>
				<td class="col-md-6"></td>
			</tr>
			<tr>
				<td class="col-md-3 control-label" align="right">推荐人：</td>
				<td class="col-md-3">${agent.referrer }</td>
				<td class="col-md-6"></td>
			</tr>
			<tr>
				<#if agent.registertime?? > 
					<td class="col-md-3 control-label" align="right">注册日期：</td>
					<td class="col-md-3">${agent.registertime?string('yyyy-MM-dd HH:mm:ss') }</td>
					<td class="col-md-6"></td>
				<#else>
					<td class="col-md-3 control-label" align="right">注册日期：</td>
					<td class="col-md-3"></td>
					<td class="col-md-6"></td>
				</#if>
			</tr>
				<tr>
				<#if agent.testtime?? > 
					<td class="col-md-3 control-label" align="right">验证日期起：</td>
					<td class="col-md-3">${agent.testtime?string('yyyy-MM-dd HH:mm:ss') }</td>
					<td class="col-md-6"></td>
				<#else>
					<td class="col-md-3 control-label" align="right">验证日期起：</td>
					<td class="col-md-3"></td>
					<td class="col-md-6"></td>
				</#if>
			</tr>
			<tr>
				<#if agent.testtimeend?? > 
					<td class="col-md-3 control-label" align="right">验证日期止：</td>
					<td class="col-md-3">${agent.testtimeend?string('yyyy-MM-dd HH:mm:ss') }</td>
					<td class="col-md-6"></td>
				<#else>
					<td class="col-md-3 control-label" align="right">验证日期止：</td>
					<td class="col-md-3"></td>
					<td class="col-md-6"></td>
				</#if>
			</tr>
			<tr>
				<#if agent.firstlogintime?? > 
					<td class="col-md-3 control-label" align="right">认证首登：</td>
					<td class="col-md-3">${agent.firstlogintime?string('yyyy-MM-dd HH:mm:ss') }</td>
					<td class="col-md-6"></td>
				<#else>
					<td class="col-md-3 control-label" align="right">认证首登：</td>
					<td class="col-md-3"></td>
					<td class="col-md-6"></td>
				</#if>
			</tr>
			<tr>
				<td class="col-md-3 control-label" align="right">签约状态：</td>
				<td class="col-md-3">${agent.approvesstatestr }</td>
				<td class="col-md-6"></td>
			</tr>
			<tr>
				<td class="col-md-3 control-label" align="right">用户来源：</td>
				<td class="col-md-3">${agent.usersource }</td>
				<td class="col-md-6"></td>
			</tr>
			<tr>
				<#if agent.lsatlogintime?? > 
					<td class="col-md-3 control-label" align="right">最后登陆时间：</td>
					<td class="col-md-3">${agent.lsatlogintime?string('yyyy-MM-dd HH:mm:ss') }</td>
					<td class="col-md-6"></td>
				<#else>
					<td class="col-md-3 control-label" align="right">最后登陆时间：</td>
					<td class="col-md-3"></td>
					<td class="col-md-6"></td>
				</#if>
			</tr>
			<tr>
				<td class="col-md-3 control-label" align="right">所属城市区县：</td>
				<td class="col-md-3">${agent.livingcityid }</td>
				<td class="col-md-6"></td>
			</tr>
			<tr>
				<td class="col-md-3 control-label" align="right">是否是测试账号：</td>
				<td class="col-md-3">${agent.isteststr }</td>
				<td class="col-md-6"></td>
			</tr>
		</table>
	</div>
</div>
<div class="modal-footer">
	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
</div>