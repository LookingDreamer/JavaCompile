<!-- 待处理任务 -->
<#include "../../macro/deliveryType.ftl"/>
<div class="panel panel-default m-bottom-5" id="myOrderManageResultcontent">
	<div class="panel-body" id="myOrderManageResultList">
		<#list orderManage.orderManageList as orderManageItem>
			 <#if orderManageItem.taskcode == "20">
			 	<#-- 待支付 -->
			 	 <div class="zdy-row zdy-row-ver">
				 <input type="checkbox" name="need2dotask" class="chx" id="${orderManageItem.mInstanceid}+${orderManageItem.sInstanceid}+${orderManageItem.inscomcode}+${orderManageItem.mainOrsub}+${orderManageItem.taskcode}+${orderManageItem.paychannelname!"未知"}">
				 
				 <div id="table${orderManageItem_index}" class="row taskrow1 zdy-cell">
				 	<div class="title col-md-6 col-sm-12">
						<label>车牌号：${orderManageItem.carlicenseno}</label>&nbsp;&nbsp;
					 	<label>被保人：${orderManageItem.insuredName}</label>&nbsp;&nbsp;
					 	<label>代理人：${orderManageItem.agentname}(${orderManageItem.agentnum})</label><br/>
						<label>代理人电话：${orderManageItem.phone}</label>&nbsp;&nbsp;
						<label>所属团队：${orderManageItem.team}</label>
						<label>渠道来源：${orderManageItem.purchaserchannel}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>商业险：${orderManageItem.busdischarge}</label>&nbsp;&nbsp;
						<label>投保单号：${orderManageItem.busproposalformno}</label><br/>
						<label>&nbsp;&nbsp;</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>业务跟踪号：${orderManageItem.mInstanceid}</label><br/>
						<label>订单创建时间：${orderManageItem.taskcreatetime}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>交强险：${orderManageItem.strdischarge}</label>&nbsp;&nbsp;
						<label>投保单号：${orderManageItem.strproposalformno}</label><br/>
						<label>&nbsp;&nbsp;</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>供应商：${orderManageItem.inscomname}-${orderManageItem.buychannel}</label><br>
                        <label>用户备注：${orderManageItem.uscs}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>给用户备注：${orderManageItem.usercommentlist}</label><br>
						<label>给操作员备注：${orderManageItem.operatorcommentList}</label>
					</div>
					<div class="col-md-6 col-sm-12">
					    <label>网点：${orderManageItem.deptname}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>订单状态：${orderManageItem.orderstatus}</label>&nbsp;&nbsp;&nbsp;&nbsp;
						<label>支付方式：${orderManageItem.paychannelname!"未知"}</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <label>配送方式：<@deliveryType type=orderManageItem.deliverytype /></label>
					</div>
					<div class="col-md-12 col-sm-12">
						<button class="btn btn-primary float_right getTaskBtn" type="button" 
							id="${orderManageItem.mInstanceid}_${orderManageItem.sInstanceid}_${orderManageItem.inscomcode}_${orderManageItem.mainOrsub}_${orderManageItem.taskcode}" title="申请任务">
							<span class="glyphicon glyphicon-eye-open"></span>申请任务
						</button>
						<button class="btn btn-primary float_right reUnderwriting" type="button" style="margin-right:6px;" 
							id="${orderManageItem.mInstanceid}_${orderManageItem.sInstanceid}_${orderManageItem.inscomcode}" title="重新核保">
							<span class="glyphicon glyphicon-eye-open"></span>重新核保
						</button>
						
					</div>
				</div>
				</div>
			<#elseif orderManageItem.taskcode == "999">
				<div id="table${orderManageItem_index}" class="row taskrow2">
					<div class="title col-md-6 col-sm-12">
						<label>代理人：${orderManageItem.agentname}(${orderManageItem.agentnum})</label>&nbsp;&nbsp;&nbsp;&nbsp;
						<label>手机号：${orderManageItem.phone}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>所在地区：${orderManageItem.strproposalformno}${orderManageItem.busproposalformno}${orderManageItem.buspolicyno}</label>&nbsp;&nbsp;&nbsp;
						<label>主营业务：${orderManageItem.buychannel}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>任务创建时间：${orderManageItem.taskcreatetime}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<button class="btn btn-primary float_right getCfTaskBtn" type="button" id="${orderManageItem.mInstanceid}" 
							title="申请任务">
							<span class="glyphicon glyphicon-eye-open"></span>申请任务
						</button>
					</div>
				</div>
			<#elseif orderManageItem.taskcode == "24">
				<#-- 待配送部分 --> 
				<div class="zdy-row zdy-row-ver">
				 		<input type="checkbox" name="need2delivery" class="chx" id="${orderManageItem.mInstanceid}+${orderManageItem.sInstanceid}+${orderManageItem.inscomcode}+${orderManageItem.mainOrsub}+${orderManageItem.taskcode}+${orderManageItem.deliverytype}">
				 
				<div id="table${orderManageItem_index}" class="row taskrow1 zdy-cell">
				 	<div class="title col-md-6 col-sm-12">
						<label>车牌号：${orderManageItem.carlicenseno}</label>&nbsp;&nbsp;
					 	<label>被保人：${orderManageItem.insuredName}</label>&nbsp;&nbsp;
					 	<label>代理人：${orderManageItem.agentname}(${orderManageItem.agentnum})</label><br/>
						<label>代理人电话：${orderManageItem.phone}</label>&nbsp;&nbsp;
						<label>所属团队：${orderManageItem.team}</label>
						<label>渠道来源：${orderManageItem.purchaserchannel}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>商业险：${orderManageItem.busdischarge}</label>&nbsp;&nbsp;
						<label>投保单号：${orderManageItem.busproposalformno}</label><br/>
						<label>保单号：${orderManageItem.buspolicyno}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>业务跟踪号：${orderManageItem.mInstanceid}</label><br/>
						<label>订单创建时间：${orderManageItem.taskcreatetime}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>交强险：${orderManageItem.strdischarge}</label>&nbsp;&nbsp;
						<label>投保单号：${orderManageItem.strproposalformno}</label><br/>
						<label>保单号：${orderManageItem.strpolicyno}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>供应商：${orderManageItem.inscomname}-${orderManageItem.buychannel}</label><br>
                        <label>用户备注：${orderManageItem.uscs}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>给用户备注：${orderManageItem.usercommentlist}</label><br>
						<label>给操作员备注：${orderManageItem.operatorcommentList}</label>
					</div>
					<div class="col-md-6 col-sm-12">
					    <label>网点：${orderManageItem.deptname}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>订单状态：${orderManageItem.orderstatus}</label>&nbsp;&nbsp;&nbsp;&nbsp;
						<label>支付方式：${orderManageItem.paychannelname!"未知"}</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <label>配送方式：<@deliveryType type=orderManageItem.deliverytype /></label>
					</div>
					<div class="col-md-12 col-sm-12">
						<button class="btn btn-primary float_right getTaskBtn" type="button" 
							id="${orderManageItem.mInstanceid}_${orderManageItem.sInstanceid}_${orderManageItem.inscomcode}_${orderManageItem.mainOrsub}_${orderManageItem.taskcode}" title="申请任务">
							<span class="glyphicon glyphicon-eye-open"></span>申请任务
						</button>
					</div>
				</div>
				</div>
			<#else>
				<div id="table${orderManageItem_index}" class="row taskrow1">
				 	<div class="title col-md-6 col-sm-12">
						<label>车牌号：${orderManageItem.carlicenseno}</label>&nbsp;&nbsp;
					 	<label>被保人：${orderManageItem.insuredName}</label>&nbsp;&nbsp;
					 	<label>代理人：${orderManageItem.agentname}(${orderManageItem.agentnum})</label><br/>
						<label>代理人电话：${orderManageItem.phone}</label>&nbsp;&nbsp;
						<label>所属团队：${orderManageItem.team}</label>
						<label>渠道来源：${orderManageItem.purchaserchannel}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>商业险：${orderManageItem.busdischarge}</label>&nbsp;&nbsp;
						<label>投保单号：${orderManageItem.busproposalformno}</label><br/>
						<label>保单号：${orderManageItem.buspolicyno}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>业务跟踪号：${orderManageItem.mInstanceid}</label><br/>
						<label>订单创建时间：${orderManageItem.taskcreatetime}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>交强险：${orderManageItem.strdischarge}</label>&nbsp;&nbsp;
						<label>投保单号：${orderManageItem.strproposalformno}</label><br/>
						<label>保单号：${orderManageItem.strpolicyno}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>供应商：${orderManageItem.inscomname}-${orderManageItem.buychannel}</label><br>
                        <label>用户备注：${orderManageItem.uscs}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>给用户备注：${orderManageItem.usercommentlist}</label><br>
						<label>给操作员备注：${orderManageItem.operatorcommentList}</label>
					</div>
					<div class="col-md-6 col-sm-12">
					    <label>网点：${orderManageItem.deptname}</label>
					</div>
					<div class="col-md-6 col-sm-12">
						<label>订单状态：${orderManageItem.orderstatus}</label>&nbsp;&nbsp;&nbsp;&nbsp;
						<label>支付方式：${orderManageItem.paychannelname!"未知"}</label>&nbsp;&nbsp;&nbsp;&nbsp;
                        <label>配送方式：<@deliveryType type=orderManageItem.deliverytype /></label>
					</div>
					<div class="col-md-12 col-sm-12">
						<button class="btn btn-primary float_right getTaskBtn" type="button" 
							id="${orderManageItem.mInstanceid}_${orderManageItem.sInstanceid}_${orderManageItem.inscomcode}_${orderManageItem.mainOrsub}_${orderManageItem.taskcode}" title="申请任务">
							<span class="glyphicon glyphicon-eye-open"></span>申请任务
						</button>
					</div>
				</div>
			</#if>
		</#list>
	</div>
	<div class="panel-footer" style="background-color:#fff;" id="myOrderManageResultFoot">
		<div class="row">
			<div class="col-md-12">
				<div class="col-md-6">
				第<label id="lblCurent">${orderManage.currentPage}</label>页/
				共<labelid="lblPageCount">${orderManage.totalPages}</label>页-
				共<label id="lblToatl">${orderManage.totalSize}</label>条数据 
			</div>
			<div class="col-md-6" style="text-align:right;">
				跳转到：第
				<select name="gotoPage" id="gotoPage">
					<#if orderManage.totalPages?? && orderManage.totalPages gt 0>
						<#list 1..orderManage.totalPages as i> 
							<option value="${i}" <#if orderManage.currentPage == i> selected </#if>>${i}</option>
						</#list>
					<#else>
						<option value="1">1</option>
					</#if>
				</select>页&nbsp;
				<#if orderManage.currentPage != 1>
					<a id="tofirst" class="toPageop" style="cursor:pointer;">首页</a>&nbsp;
					<a id="toprevious" class="toPageop" style="cursor:pointer;">上一页</a>&nbsp;
				<#else>
					首页&nbsp;上一页&nbsp;
				</#if>
				<#if orderManage.totalPages gt 0 && orderManage.currentPage != orderManage.totalPages>
					<a id="tonext" class="toPageop" style="cursor:pointer;">下一页</a>&nbsp;
					<a id="tolast" class="toPageop" style="cursor:pointer;">末页</a>
				<#else>
					下一页&nbsp;末页
				</#if>
				<input type="hidden" id="totalPages" value="${orderManage.totalPages}"/>
			</div>
		</div>
	</div>
	<div id="chxButton">
		<#-- 批量支付成功 -->
		<input type="checkbox" name="need2dotask" class="checkAll" id="checkAll2Pay">
		<span name="need2dotask">全选</span>
		<button id="batchPaySuccess" type="button" name="need2dotask" class="btn btn-primary">关闭柜台支付任务</button>
		<#-- 批量自取成功 -->
		<input type="checkbox" name="need2delivery" class="checkAll" id="checkAll2Deliver">
		<span name="need2delivery">全选</span>
		<button id="batchDeliverySuccess" type="button" name="need2delivery" class="btn btn-primary">自取完成</button>
	</div>
</div>
