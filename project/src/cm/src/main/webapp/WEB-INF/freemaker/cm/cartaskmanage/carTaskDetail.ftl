<style type="text/css">
	body {font-size: 14px;}
	span.feildtitle {
		font-size:16px;
		font-weight:bold;
		color:#34495E;
	}
	table.hovertable {
		font-size:11px;
		color:#333333;
		border-width: 1px;
		border-color: #999999;
		border-collapse: collapse;
		margin-bottom:5px;
		margin-top:5px;
		background-color:#F5F5F5;
	}
	table.hovertable td {
		border-width: 1px;
		padding: 8px;
		border-style: solid;
		border-color: #383838;
	}
	td.bgg{
		background-color:#E5E5E5; 
		text-align:right;
	}
	table.whiteborder td {
		border-color: white;
	}
	table.orange td {
		border-width:0px;
	}
	table.orange{
		background-color:#FFE4B5;
	}
	table.table-platcarmessage{
		background-color:#F5F5F5;
	}
	
	#showqueryordresult{ 
		width: 80%; 
		position: fixed; 
		margin: 2245px 10px 0px auto; 
		top: 0px; left: 0px; 
		bottom: 0px; 
		right: 0px; 
	}
	#fqshowqueryordresult{ 
		width: 80%; 
		position: fixed; 
		margin: 2220px 10px 0px auto; 
		top: 0px; left: 0px; 
		bottom: 0px; 
		right: 0px; 
	}
	#remarkdivcss{
		height: 100px;
		overflow: auto;
		word-break: break-all;
	}
</style>
<script type="text/javascript" >
	requirejs([ "cm/common/querysecondpayorder" ]);
	$("#hidetoggle").bind("click", function(event) {
		if($("#sysinfodiv").css("display")=="none") {
			$("#sysinfodiv").css("display","block");
		} else {
			$("#sysinfodiv").css("display","none");
		}
	});
</script>
<!--车险任务详细信息弹出框-->
<div class="modal-content" id="modal-content" style="width: 1000px;">
<div class="modal-dialog ui-draggable" style="width: 1000px;">
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
	<h6 class="modal-title">任务详情：</h6>
</div>
<div class="modal-body"><!--加入此标签弹出框会有内边距-->
	<div class="panel panel-default" style="margin-bottom:0px">
		<div class="panel-heading">
			<div class="row">
				 <div class="col-md-12">
				 	<span class="feildtitle">任务跟踪号：${carInfo.taskid}</span>
				 </div>
			  </div>
		</div>
	</div>
	<div class="panel panel-default" style="margin-bottom:0px">
		<div class="panel-heading">
			<div class="row">
				<div class="col-md-12">
					<span class="feildtitle">代理人信息：</span>
				</div>
			</div>
		</div>
		<table class="table table-bordered">
			<tr>
				<td class="col-md-2 active">代理人姓名:</td>
				<td class="col-md-4">${agentInfo.agentname}</td>
				<td class="col-md-2 active">工号:</td>
				<td class="col-md-4">${agentInfo.jobnum}</td>
			</tr>
			<tr>
				<td class="active">执业证/展示证号码:</td>
				<td>${agentInfo.licenseno}</td>
				<td class="active">${agentInfo.idnotype}:</td>
				<td>${agentInfo.idno}</td>
			</tr>
			<tr>
				<!--
				<td class="active">执业/展业号码:</td>
				<td>${agentInfo.licenseno}</td>
				-->
				<td class="active">出单网点:</td>
				<td>${otherInfo.deptComName}</td>
				<td class="active">联系电话:</td>
				<td>${agentInfo.mobile}</td>
			</tr>
			<tr>
				<td class="active">所属团队:</td>
				<td colspan="1">${agentInfo.team}</td>
				<td class="active">渠道来源:</td>
				<td colspan="1">${agentInfo.purchaserchannel}</td>
			</tr>
		 </table>
	</div>
	
	<div class="panel panel-default" style="margin-bottom:0px">
		<div class="panel-heading">
		  	<div class="row">
			 	<div class="col-md-12">
			 		<span class="feildtitle">车辆信息：</span>
			 	</div>
		  	</div>
	  	</div>
		<table class="table table-bordered">
			<tr>
				<td class="col-md-2 active">投保地区:</td>
				<td class="col-md-4">${carInfo.insprovincename}${carInfo.inscityname}</td>
				<td class="col-md-2 active">发动机号:</td>
				<td class="col-md-4">${carInfo.engineno}</td>
			</tr>
			<tr>
				<td class="active">车牌:</td>
				<td>${carInfo.carlicenseno}</td>
				<td class="active">车辆识别代号:</td>
				<td>${carInfo.vincode}</td>
			</tr>
			<tr>
				<td class="active">车主:</td>
				<td>${carInfo.ownername}</td>
				<td class="active">车辆初登日期:</td>
				<td>${carInfo.registdate}</td>
			</tr>
			<tr>
				<td class="active">车型:</td>
				<td>${carInfo.standardfullname}</td>
				<td class="active">是否过户车:</td>
				<td>
					<#if carInfo.isTransfercar == "1">是(过户时间：${carInfo.transferdate!"未知"})</#if>
					<#if carInfo.isTransfercar == "0">否</#if>
				</td>
			</tr>
		</table>
	</div>

	<div class="panel panel-default" style="margin-bottom:0px">
		<div class="panel-heading">
		  	<div class="row">
			 	<div class="col-md-12">
			 		<span class="feildtitle">车型信息：</span>
			 	</div>
		  	</div>
	  	</div>
		<table class="table table-bordered">
			<tr>
				<td class="col-md-2 active">所属性质:</td>
				<td class="col-md-4">${carInfo.propertyvalue}</td>
				<td class="col-md-2 active">车辆使用性质:</td>
				<td class="col-md-4">${carInfo.carpropertyvalue}</td>
			</tr>
			<tr>
				<td class="active">品牌型号:</td>
				<td>${carmodelinfo.standardfullname}</td>
				<td class="active">核定载人数:</td>
				<td>${carmodelinfo.seat}</td>
			</tr>
			<tr>
				<td class="active">整备质量:</td>
				<td>${carmodelinfo.fullweight}</td>
				<td class="active">核定载质量:</td>
				<td>${carmodelinfo.unwrtweight}</td>
			</tr>
			<tr>
				<td class="active">排气量:</td>
				<td>${carmodelinfo.displacement}</td>
				<td class="active">新车购置价:</td>
				<td>${carmodelinfo.price}</td>
			</tr>
			<tr>
				<td class="active">车型配置:</td>
				<td>${carmodelinfo.gearbox}</td>
				<td class="active">年款:</td>
				<td>${carmodelinfo.listedyear}</td>
			</tr>
			<tr>
				<td class="active">投保车价:</td>
				<td>${carmodelinfo.policycarprice}</td>
				<td class="active">车辆种类</td>
				<td>${carmodelinfo.vehicletypename}</td>
			</tr>
		</table>
	</div>

	<#if showbbrIDinfo == true>
		<div class="panel panel-default" style="margin-bottom:0px">
		  	<div class="panel-heading">
			  	<div class="row">
				 	<div class="col-md-12">
				 		<span class="feildtitle">支付用补充信息：</span>
					</div>
			  	</div>
		  	</div>
			<table class="table table-bordered">
				<tr>
					<td class="col-md-2 active">姓名:</td>
					<td class="col-md-4">${relationPersonInfo.insuredname}</td>
					<td class="col-md-2 active">身份证号:</td>
					<td class="col-md-4">${relationPersonInfo.insuredidcardno}</td>
				</tr>
				<tr>
					<td class="col-md-2 active">性别:</td>
					<td class="col-md-4">
					<#if relationPersonInfo.insuredsex == 1>
                        女
					<#else>
                        男
					</#if>
                    </td>
					<td class="col-md-2 active">民族:</td>
					<td class="col-md-4">${relationPersonInfo.insurednation}</td>
				</tr>
				<tr>
					<td class="col-md-2 active">出生日期:</td>
					<td class="col-md-4">${relationPersonInfo.insuredbirthday}</td>
					<td class="col-md-2 active">证件有效期:</td>
					<td class="col-md-4">${relationPersonInfo.insuredexpdate}</td>
				</tr>
				<tr>
					<td class="col-md-2 active">手机号:</td>
					<td class="col-md-4">${relationPersonInfo.insuredcellphone}</td>
					<td class="col-md-2 active">邮箱:</td>
					<td class="col-md-4">${relationPersonInfo.insuredemail}</td>
				</tr>
				<tr>
					<td class="col-md-2 active">前端输入的验证码:</td>
					<td class="col-md-4">${relationPersonInfo.insuredpincode}</td>
					<td class="col-md-2 active">住址:</td>
					<td class="col-md-4">${relationPersonInfo.insuredaddress}</td>
				</tr>
				<tr>
					<td class="col-md-2 active">签发机关:</td>
					<td colspan="col-md-4">${relationPersonInfo.insuredauthority}</td>
                    <td class="col-md-2 active">有效期限开始:</td>
                    <td colspan="col-md-4">${relationPersonInfo.insuredexpstartdate}</td>
				</tr>
                <tr>
                    <td class="col-md-2 active">有效期限结束:</td>
                    <td colspan="3">${relationPersonInfo.insuredexpenddate}</td>
                </tr>
			</table>
		</div>
	</#if>

	<div class="panel panel-default" style="margin-bottom:0px">
	  	<div class="panel-heading">
		  	<div class="row">
			 	<div class="col-md-12">
			 		<span class="feildtitle">关系人信息：</span>
			 	</div>
		  	</div>
	  	</div>
		<table class="table table-bordered">
			<tr>
				<td class="col-md-2 active">被保人:</td>
				<td class="col-md-4">${relationPersonInfo.insuredname}</td>
				<td class="col-md-2 active">${relationPersonInfo.insuredidcardtypeValue}:</td>
				<td class="col-md-4">${relationPersonInfo.insuredidcardno}</td>
			</tr>
			<tr>
				<td class="active">投保人:</td>
				<td>${relationPersonInfo.applicantname}</td>
				<td class="active">${relationPersonInfo.applicantidcardtypeValue}:</td>
				<td>${relationPersonInfo.applicantidcardno}</td>
			</tr>
			<tr>
				<td class="col-md-2 active">权益索赔人:</td>
				<td class="col-md-4">${relationPersonInfo.personForRightname}</td>
				<td class="col-md-2 active">${relationPersonInfo.personForRightidcardtypeValue}:</td>
				<td class="col-md-4">${relationPersonInfo.personForRightidcardno}</td>
			</tr>
			<tr>
				<td class="active">联系人:</td>
				<td>${relationPersonInfo.linkPersonname}</td>
				<td class="active">${relationPersonInfo.linkPersonidcardtypeValue}:</td>
				<td>${relationPersonInfo.linkPersonidcardno}</td>
			</tr>
		</table>
	</div>
	
	<div class="panel panel-default" style="margin-bottom:0px">
	  	<div class="panel-heading">
		  	<div class="row">
			 	<div class="col-md-12">
			 		<span class="feildtitle">其他信息：</span>
			 	</div>
		  	</div>
	  	</div>
		<table class="table table-bordered">
			<tr>
				<td class="col-md-2 active">是否指定驾驶人:</td>
				<td class="col-md-4">
					<#if otherInfo.Specifydriver != "否">
			    		是
			    	<#else>
			    		${otherInfo.Specifydriver}
			    	</#if>
				</td>
				<td class="col-md-2 active">平均行驶里程:</td>
				<td class="col-md-4">${carInfo.mileagevalue}</td>
			</tr>
			<tr>
				<td class="active">商业险起保日期:</td>
				<td><#if otherInfo.hasbusi>${otherInfo.businessstartdate}<#else>没有商业险保单记录</#if></td>
				<td class="active">商业险终止日期:</td>
				<td><#if otherInfo.hasbusi>${otherInfo.businessenddate}<#else>没有商业险保单记录</#if></td>
			</tr>
			<tr>
				<td class="active">交强险起保日期:</td>
				<td><#if otherInfo.hasstr>${otherInfo.compulsorystartdate}<#else>没有交强险保单记录</#if></td>
				<td class="active">交强险终止日期:</td>
				<td><#if otherInfo.hasstr>${otherInfo.compulsoryenddate}<#else>没有交强险保单记录</#if></td>
			</tr>
            <tr <#if otherInfo.preinsSypolicyno == "">style="display:none;"</#if>>
                <td class="active">上年商业险保单号:</td>
                <td colspan="3">${otherInfo.preinsSypolicyno}</td>
            </tr>
			<tr <#if otherInfo.invoicetype  == "">style="display:none;"</#if>>
						<td class="active">发票类型</td>
					<#if otherInfo.invoicetype == "0">
						<td >增值税普通发票</td>
					<#elseif otherInfo.invoicetype == "1">
						<td >增值税专用发票</td>
					<#elseif otherInfo.invoicetype == "2">
						<td >增值税普通发票(需资料)</td>
					<#else>
						<td >无</td>
					</#if>
			</tr>
			<tr <#if otherInfo.invoicetype == "" >style="display:none;</#if>>
				<td class="active"<#if otherInfo.invoicetype == "">style="display:none;</#if>>开户银行名称:</td>
				<td>${otherInfo.bankname}</td>
				<td class="active"<#if otherInfo.invoicetype == "" >style="display:none;</#if>>银行账号:</td>
				<td>${otherInfo.accountnumber}</td>
			</tr>
			<tr <#if otherInfo.invoicetype  == "">style="display:none;</#if>>
				<td class="active" <#if otherInfo.invoicetype  == "">style="display:none;</#if>>纳税人识别号/统一社会信用代码:</td>
				<td>${otherInfo.identifynumber}</td>
				<td class="active" <#if otherInfo.invoicetype  == "">style="display:none;</#if>>纳税登记电话:</td>
				<td>${otherInfo.registerphone}</td>
			</tr>
			<tr  <#if otherInfo.invoicetype  == "">style="display:none;</#if>>
				<td class="active" <#if otherInfo.invoicetype == "">style="display:none;</#if>>纳税登记地址:</td>
				<td>${otherInfo.registeraddress}</td>
				<td class="active" <#if otherInfo.invoicetype  == "">style="display:none;</#if>>电子邮箱:</td>
				<td>${otherInfo.email}</td>
			</tr>
			<tr>
				<td class="active">云查询保险公司:</td>
				<td>${otherInfo.cloudinscompany}</td>
			</tr>
			<tr>
                <td class="active">用户备注:</td>
                <td>
                    <div id="remarkdivcss">
					<#list otherInfo.userComments as usc>
						<#if (usc.typeName)??>【${usc.typeName}】</#if>
						<#if (usc.userComment.commentcontent)??>${usc.userComment.commentcontent}</#if>
						<#if (usc.userComment.operator)??>-${usc.userComment.operator}</#if>
						<#if (usc.userComment.createtime)??>-${usc.userComment.createtime?datetime?string("yyyy-MM-dd HH:mm:ss")}</#if>
						<#if usc_has_next><br/></#if>
					</#list>
                    </div>
                </td>
                <td class="active">给用户备注:</td>
                <td>
                    <div id="remarkdivcss">
					<#--<#if (otherInfo.noti)??>
						<#if (otherInfo.noti.typeName)>【${otherInfo.noti.typeName}】</#if>
					    ${otherInfo.noti.userComment.commentcontent}
                        -
						<#if (otherInfo.noti.userComment.modifytime)??>
						${otherInfo.noti.userComment.modifytime?datetime?string("yyyy-MM-dd HH:mm:ss")}
						<#elseif (otherInfo.noti.userComment.createtime)??>
						${otherInfo.noti.userComment.createtime?datetime?string("yyyy-MM-dd HH:mm:ss")}
						<#else>
                            null
						</#if>
					</#if>-->
					<#list otherInfo.noti as nt>
						<#if (nt.typeName)??>【${nt.typeName}】</#if>
						<#if (nt.userComment.commentcontent)??>${nt.userComment.commentcontent}</#if>
						<#if (nt.userComment.operator)??>-${nt.userComment.operator}</#if>
						<#if (nt.userComment.createtime)??>-${nt.userComment.createtime?datetime?string("yyyy-MM-dd HH:mm:ss")}</#if>
						<#if nt_has_next><br/></#if>
					</#list>
                    </div>
                </td>
            </tr>
			<tr>
				<td class="active">给操作员备注:</td>
				<td>
					<div id="remarkdivcss">
						<#list otherInfo.operatorcomment as agcomment>
							${agcomment.commentcontent}-${agcomment.operator}
							<#if (agcomment.modifytime)??>
								${agcomment.modifytime?datetime?string("yyyy-MM-dd HH:mm:ss")}
							<#elseif (agcomment.createtime)?? >
								${agcomment.createtime?datetime?string("yyyy-MM-dd HH:mm:ss")}
							<#else>
								null
							</#if>
							<#if opcomment_has_next><br/></#if>
						</#list>
					</div>
				</td>
                <td class="active"></td>
                <td></td>
			</tr> 
		</table>
	</div>

    <div class="panel panel-default" style="margin-bottom:0px">
        <div class="panel-heading">
            <div class="row">
                <div class="col-md-12">
                    <span class="feildtitle">补充数据项：</span>
                </div>
            </div>
        </div>
        <table class="table table-bordered">
			<#assign showcolumnfirst=3 showcolumnsecond=9 showcolumnfirstclass="active">
			<#include "cm/common/showSupplyParam.ftl" />
        </table>
    </div>

	<div class="panel panel-default" style="margin-bottom:0px">
		<div class="panel-heading">
			<div class="row">
				<div class="col-md-12">
					<span class="feildtitle">平台信息：</span>
				</div>
			</div>
		</div>
		<table class="table table-bordered">
			<tr>
				<td class="col-md-3 active">投保类型:</td>
				<td class="col-md-3">${otherInfo.firstInsureType}</td>
				<td class="col-md-3 active">上年商业险投保公司:</td>
				<td class="col-md-3"><#if otherInfo.preinsShortname>${otherInfo.preinsShortname}<#else></#if></td>
			</tr>
			<tr>
				<td class="col-md-3 active">商业险出险次数:</td>
				<td class="col-md-3"><#if otherInfo.syclaimtimes>${otherInfo.syclaimtimes}<#else></#if></td>
				<td class="col-md-3 active">交强险出险次数:</td>
				<td class="col-md-3"><#if otherInfo.jqclaimtimes>${otherInfo.jqclaimtimes}<#else></#if></td>
			</tr>
			<tr>
				<td class="col-md-3 active">平台商业险起保日期:</td>
				<td class="col-md-3"><#if otherInfo.systartdate>${otherInfo.systartdate}<#else></#if></td>
				<td class="col-md-3 active">平台商业险终止日期:</td>
				<td class="col-md-3"><#if otherInfo.syenddate>${otherInfo.syenddate}<#else></#if></td>
			</tr>
			<tr>
				<td class="col-md-3 active">平台交强险起保日期:</td>
				<td class="col-md-3"><#if otherInfo.jqstartdate>${otherInfo.jqstartdate}<#else></#if></td>
				<td class="col-md-3 active">平台交强险终止日期:</td>
				<td class="col-md-3"><#if otherInfo.jqenddate>${otherInfo.jqenddate}<#else></#if></td>
			</tr>
			<tr>
				<td class="col-md-3 active">无赔款不浮动原因:</td>
				<td class="col-md-3"><#if rulequery.loyaltyreasons>${rulequery.loyaltyreasons}<#else></#if></td>
				<td class="col-md-3 active">是否纯电销</td>
				<td class="col-md-3"><#if otherInfo.pureesale == "1">是<#elseif otherInfo.pureesale == "0">否</#if></td>
			</tr>
		</table>
	</div>
		
	<div class="panel-heading">
		<div class="row">
			<div class="col-md-12">
				<span class="feildtitle">查询详细信息：</span>
				<button id="hidetoggle" class="glyphicon glyphicon-list-alt icon-list-alt"></button>
			</div>
		</div>
	</div>
	<div id="sysinfodiv" class="panel panel-default" style="margin-bottom:0px;display:none">
		<table class="table table-bordered">
			<tr>
				<td class="col-md-3 active">无赔款折扣浮动原因:</td>
				<td class="col-md-3"><#if rulequery.noclaimdiscountcoefficientreasons>${rulequery.noclaimdiscountcoefficientreasons}<#else></#if></td>
				<td class="col-md-3 active"><!--客户忠诚度原因:--></td>
				<td class="col-md-3"></td>
			</tr>
			<tr>
				<td class="col-md-3 active">平台查询保险公司:</td>
				<td class="col-md-3"><#if rulequery.insureco>${rulequery.insureco}<#else></#if></td>
				<td class="col-md-3 active">平台查询公司车损保费:</td>
				<td class="col-md-3"></td>
			</tr>
			<tr>
				<td class="col-md-3 active">平台查询交通违法系数:</td>
				<td class="col-md-3"><#if rulequery.trafficoffencediscount>${rulequery.trafficoffencediscount}<#else></#if></td>
				<td class="col-md-3 active">平台查询自主核保系数:</td>
				<td class="col-md-3"><#if rulequery.selfinsurerate>${rulequery.selfinsurerate}<#else></#if></td>
			</tr>
			<tr>
				<td class="col-md-3 active">平台查询自主渠道系数:</td>
				<td class="col-md-3"><#if rulequery.selfchannelrate>${rulequery.selfchannelrate}<#else></#if></td>
				<td class="col-md-3 active">平台查询NCD系数:</td>
				<td class="col-md-3"><#if rulequery.noclaimdiscountcoefficient>${rulequery.noclaimdiscountcoefficient}<#else></#if></td>
			</tr>
			<tr>
				<td class="col-md-3 active">平台查询自主系数:</td>
				<td class="col-md-3"></td>
				<td class="col-md-3 active">机动车车损纯风险基准保费:</td>
				<td class="col-md-3"><#if rulequery.basicriskpremium>${rulequery.basicriskpremium}<#else></#if></td>
			</tr>
		</table>
		<div class="panel-heading">
			<div class="row">
				<div class="col-md-12">
					<span class="feildtitle">商业险平台记录：</span>
				</div>
			</div>
		</div>
		<table class="table table-bordered">
			<tr class="active">
				<td class="col-md-3">上年投保公司</td>
				<td class="col-md-3">保单号</td>
				<td class="col-md-3">平台起保日期</td>
				<td class="col-md-3">平台终保日期</td>
			</tr>
			<tr>
				<td class="col-md-3">${rulequery.inscorpname0}</td>
				<td class="col-md-3">${rulequery.policyid0}</td>
				<td class="col-md-3">${rulequery.policystarttime0}</td>
				<td class="col-md-3">${rulequery.policyendtime0}</td>
			</tr>
		</table>
		<table class="table table-bordered">
			<tr class="active">
				<td >出险时间</td>
				<td >结案时间</td>
				<td >理赔金额</td>
				<td >保单号</td>
				<td >保险公司</td>
			</tr>
			<#list rulequery.syclaimes as syclaim>
				<tr>
					<td >${syclaim.casestarttimec0}</td>
					<td >${syclaim.caseendtimec0}</td>
					<td >${syclaim.claimamountc0}</td>
					<td >${syclaim.policyidc0}</td>
					<td >${syclaim.inscorpnamec0}</td>
				</tr>
			</#list>
			<#if (rulequery.syclaimes?size) lte 0>
				<tr>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
					<td ></td>
				</tr>
			</#if>
		</table>
			 
		<div class="panel-heading">
			<div class="row">
				<div class="col-md-12">
					<span class="feildtitle">交强险平台记录：</span>
				</div>
			</div>
		</div>
		<table class="table table-bordered">
			<thead>
				<tr class="active">
					<td class="col-md-3">上年投保公司</td>
			       	<td class="col-md-3">保单号</td>
			       	<td class="col-md-3">平台起保日期</td>
			       	<td class="col-md-3">平台终保日期</td>
			     </tr>
			</thead>
			<tbody>
				<tr>
					<td class="col-md-3">${rulequery.inscorpname1}</td>
					<td class="col-md-3">${rulequery.policyid1}</td>
					<td class="col-md-3">${rulequery.policystarttime1}</td>
					<td class="col-md-3">${rulequery.policyendtime1}</td>
				</tr>
			</tbody>
		</table>
		<table class="table table-bordered">
			<thead>
				<tr class="active">
					<td>出险时间</td>
			       	<td>结案时间</td>
			       	<td>理赔金额</td>
			       	<td>保单号</td>
			       	<td>保险公司</td>
				</tr>
			</thead>
			<tbody>
			  	<#list rulequery.jqclaimes as jqclaim>
				    <tr>
						<td>${jqclaim.casestarttimec1}</td>
						<td>${jqclaim.caseendtimec1}</td>
						<td>${jqclaim.claimamountc1}</td>
						<td>${jqclaim.policyidc1}</td>
						<td>${jqclaim.inscorpnamec1}</td>
					</tr> 
				</#list>
				<#if (rulequery.jqclaimes?size) lte 0>   
					<tr>
						<td ></td>
						<td ></td>
						<td ></td>
						<td ></td>
						<td ></td>
					</tr>
				</#if>
			</tbody>
		</table>
		<div class="panel-heading">
			<div class="row">
				<div class="col-md-12">
					<span class="feildtitle">平台车型信息：</span>
				</div>
			</div>
		</div>
		<table class="table table-bordered">
			<tr class="active">
				<td >品牌名称</td>
				<td >行业车型码</td>
				<td >车型编码</td>
				<td >新车购置价</td>
				<td >新车购置价（含税）</td>
				<td >类比价</td>
				<td >类比价（含税）</td>
			</tr>
			<tr>
				<td >${rulequery.carbrandname}</td>
				<td >${rulequery.trademodelcode}</td>
				<td >${rulequery.modelcode}</td>
				<td >${rulequery.price}</td>
				<td >${rulequery.taxprice}</td>
				<td >${rulequery.analogyprice}</td>
				<td >${rulequery.analogytaxprice}</td>
			</tr>
			<tr class="active">
				<td >车辆种类</td>
				<td >上市年份</td>
				<td >核定载客</td>
				<td >核定载质量</td>
				<td >整备质量</td>
				<td >排气量</td>
			</tr>
			<tr>
				<td >${rulequery.vehicletype}</td>
				<td >${rulequery.carmodeldate}</td>
				<td >${rulequery.seatcnt}</td>
				<td >${rulequery.modelload}</td>
				<td >${rulequery.fullload}</td>
				<td >${rulequery.displacement}</td>
			</tr>
			</tbody>
		</table>
	</div>
	
	<div class="panel panel-default" style="margin-bottom:0px">
		<div class="panel-heading">
		  	<div class="row">
			 	<div class="col-md-12">
					<span class="feildtitle">投保单号及保单号：</span>
			 	</div>
		  	</div>
		</div>
		<table class="table table-bordered">
			<tr>
				<td class="col-md-2 active">商业险投保单号:</td>
				<td class="col-md-4"><#if otherInfo.hasbusi>${otherInfo.busProposalformno}<#else>没有商业险保单记录</#if></td>
				<td class="col-md-2 active">交强险投保单号:</td>
				<td class="col-md-4"><#if otherInfo.hasstr>${otherInfo.strProposalformno}<#else>没有交强险保单记录</#if></td>
			</tr>
			<tr>
				<td class="col-md-2 active">商业险保单号:</td>
				<td class="col-md-4"><#if otherInfo.hasbusi>${otherInfo.busPolicyno}<#else>没有商业险保单记录</#if></td>
				<td class="col-md-2 active">交强险保单号:</td>
				<td class="col-md-4"><#if otherInfo.hasstr>${otherInfo.strPolicyno}<#else>没有交强险保单记录</#if></td>
			</tr>
			<tr>
				<td class="active">支付号:</td>
				<td>${otherInfo.insurecono}</td>
				<td class="active">校验码:</td>
				<td>${otherInfo.checkcode}</td>
			</tr>
		</table>
	</div>
	
	<div class="panel panel-default">
	  	<div class="panel-heading">
		  	<div class="row">
			 	<div class="col-md-12">
			 		<span class="feildtitle">保险配置：</span>
			 	</div>
		  	</div>
	  	</div>
	  	<table class="table table-bordered">
		   	<thead>
		     	<tr class="active">
		       	<th class="col-md-4">商业险</th>
		       	<th class="col-md-4">保额（及其他信息）</th>
		       	<th class="col-md-4">保费</th>
		     	</tr>
		   	</thead>
			<tbody>
		   		<#if "1" == carInfo.insureconfigsameaslastyear>
           		<tr>
               		<td class="bgg" style="text-align:left;">
                   		<input type="checkbox" checked disabled>与上年一致
               		</td>
               		<td>&nbsp;</td>
               		<td>&nbsp;</td>
           		</tr>
		   		</#if>

		   		<#list insConfigInfo.insConfigList as insConfig>
		   			<#if insConfig.isChecked == "Y" && insConfig.inskindtype == "0">
		   				<tr>
						    <td class="bgg" style="text-align:left;">${insConfig.inskindname}</td>
						    <td>
						    	<#list insConfig.selecteditem as selectelm>
						    		<#if selectelm.TYPE == "01" || insConfig.inskindcode == "VehicleDemageIns">
						    			${insConfig.amount!""}
						    			<!--取单位-->
						    			<#list insConfig.amountSlecets as aSelecti>
						    				<#if aSelecti.TYPE == "01">
						    					<#list aSelecti.VALUE as aSelectValue>
						    						<#if aSelectValue.UNIT != "">
						    							${aSelectValue.UNIT}
						    							<#break>
						    						</#if>
						    					</#list>
						    					<#break>
						    				</#if>
						    			</#list>
						    		<#else>
						    			${selectelm.VALUE.KEY!""}${selectelm.VALUE.UNIT!""}
						    		</#if>
						    		<#if selectelm_has_next>;</#if>
						    	</#list>
						    	<#if insConfig.specialRiskkindFlag == "04">
						    		<br/>设备列表：
							    	<#list insConfig.specialKVList as kvItem>
							    		<br/>${kvItem}
							    		<#if kvItem_has_next>;</#if>
							    	</#list>
					    		<#elseif insConfig.specialRiskkindFlag == "05">
					    			;${insConfig.specialRiskkindValue}天
		    					</#if>
						    </td>
						    <td>${insConfig.amountprice!"--"}</td>
					    </tr>
		   			</#if>
		   		</#list>
		   	</tbody>
		</table>
	  	<table class="table table-bordered">
		   	<thead>
		     	<tr class="active">
		       	<th class="col-md-4">商业险附加不计免赔</th>
		       	<th class="col-md-4">保额（及其他信息）</th>
		       	<th class="col-md-4">保费</th>
		     	</tr>
		   	</thead>
		   	<tbody>
		   		<#list insConfigInfo.insConfigList as insConfig>
		   			<#if insConfig.isChecked == "Y" && insConfig.inskindtype == "1">
		   				<tr>
						    <td class="bgg" style="text-align:left;">${insConfig.inskindname}</td>
						    <td>投保</td>
						    <td>${insConfig.amountprice!"--"}</td>
					    </tr>
		   			</#if>
		   		</#list>
		   	</tbody>
		</table>
	  	<table class="table table-bordered">
		   	<thead>
		     	<tr class="active">
		       	<th class="col-md-4">交强险和车船税</th>
		       	<th class="col-md-4">保额（及其他信息）</th>
		       	<th class="col-md-4">保费</th>
		     	</tr>
		   	</thead>
		   	<tbody>
		   		<#list insConfigInfo.insConfigList as insConfig>
		   			<#if insConfig.isChecked == "Y" && (insConfig.inskindtype == "2" || insConfig.inskindtype == "3")>
		   				<tr>
						    <td class="bgg" style="text-align:left;">${insConfig.inskindname}</td>
						    <td>
						    	<!--
						    	<#list insConfig.selecteditem as selectelm>
						    		<#if selectelm.TYPE == "01">
						    			${insConfig.amount!""}${selectelm.VALUE.UNIT!""}
						    		<#else>
						    			${selectelm.VALUE.KEY!""}${selectelm.VALUE.UNIT!""}
						    		</#if>
						    		<#if selectelm_has_next>;</#if>
						    	</#list>
						    	-->
						    	<#if insConfig.inskindtype == "2">购买</#if>
		    					<#if insConfig.inskindtype == "3">代缴</#if>
						    </td>
						    <td>${insConfig.amountprice!"--"}</td>
					    </tr>
		   			</#if>
		   		</#list>
			</tbody>
		</table>
	  	<table class="table table-bordered">
		   	<thead>
		     	<tr class="active">
		       	<th class="col-md-4">其他信息</th>
		       	<th class="col-md-4">--</th>
		       	<th class="col-md-4">--</th>
		     	</tr>
		   	</thead>
		   	<tbody>
				<tr>
				    <td class="bgg" style="text-align:left;">商业险折扣率</td>
				    <td>--</td>
				    <td>${insConfigInfo.busdiscountrate!"--"}</td>
			    </tr>
			    <tr>
				    <td class="bgg" style="text-align:left;">交强险折扣率</td>
				    <td>--</td>
				    <td>${insConfigInfo.strdiscountrate!"--"}</td>
			    </tr>
			    <tr>
				    <td class="bgg" style="text-align:left;">业务类别</td>
				    <td>--</td>
				    <td>${insConfigInfo.biztype!"--"}</td>
			    </tr>
		   	</tbody>
		</table>
		
	  	<table class="table table-bordered">
			<thead>
		    	<tr class="active">
		    		<th class="col-md-12">保费合计</th>
		     	</tr>
		   	</thead>
		   	<tbody>
		     	<tr>
		       		<td class="col-md-12">
		       			商业险保费：${insConfigInfo.discountBusTotalAmountprice} 元 ;交强险保费：${insConfigInfo.discountStrTotalAmountprice} 元 ;
		       			车船税：${insConfigInfo.discountCarTax} 元;其他：${insConfigInfo.otherAmountprice} 元;
		       			保费总额：${insConfigInfo.discountBusTotalAmountprice+insConfigInfo.discountStrTotalAmountprice+insConfigInfo.discountCarTax+insConfigInfo.otherAmountprice} 元
		       		</td>
		     	</tr>
		   	</tbody>
		</table>
	</div>
	<div class="panel panel-default" style="margin-bottom:0px">
	  	<div class="panel-heading">
		  	<div class="row">
			 	<div class="col-md-12">
			 		<span class="feildtitle">支付信息：</span>
			 	</div>
		  	</div>
	  	</div>
	  	<table class="table table-bordered">
		   	<thead>
		     	<tr class="active">
		       	<th >操作</th>
		       	<th >支付流水号</th>
		       	<th >时间</th>
		       	<th >交易跟踪号</th>
		       	<th >金额</th>
		       	<th>货币</th>
		       	<th>支付方式</th>
		       	<th>退款类型</th>
		       	<th>支付平台流水号</th>
		       	<th>支付状态</th>
				</tr>
		   	</thead>
		   	<tbody>
				<#list payInfos as payInfo>
				<tr>
					<#if selectelm_has_next>;</#if>
					<td>
					<!-- 只有二支(21)且为银联(30)和众安分期保(28)的单才显示 -->
					<#if payInfo.taskcode != null && payInfo.taskcode == 21>
						<#if payInfo.paychannelid == 28 || payInfo.paychannelid == 30>
							<a class="btn btn-primary" href="javascript:showqueryorderresult('${payInfo.secpayorderrequrl}', '${payInfo.payflowno}', '${payInfo.payorderno}', '${payInfo.paychannelid}','${relationPersonInfo.applicantidcardno}');">
								查询<br>订单<br>结果
							</a>
						</#if>
					</#if>
					</td>
					<td class="bgg" style="text-align:left;">${payInfo.paymentransaction!""}</td>
					<td>${payInfo.createtime?datetime?string("yyyy-MM-dd HH:mm:ss")}</td>
					<td>${payInfo.tradeno!""}</td>
					<td>${payInfo.amount!""}</td>
					<td>${payInfo.currencycode!""}</td>
					<td>${payInfo.paychannelname}</td>
					<td>${payInfo.refundtype}</td>
					<td>${payInfo.payflowno!""}</td>
					<td>${payInfo.payresult}</td>
				</tr>
				</#list>
		   </tbody>
	   	</table>
	</div>
	<div class="row">
	 	<div class="col-md-6"></div>
	 	<div class="col-md-6" align="right">
	 		<button class="btn btn-default" type="button" title="关闭" data-dismiss="modal">&nbsp;&nbsp;关闭&nbsp;&nbsp;</button>
	 	</div>
	</div>
</div>
</div>

<!-- 银联 -->
<div id="showqueryordresult" class="modal fade" role="dialog" aria-labelledby="myModalLabel" data-backdrop="false"
				 data-keyboard="false" aria-hidden="true"> 
	<div class="panel panel-info">
		<div class="panel-heading">
			<button type="button" class="close cx_queryordresdig"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4>查询订单结果</h4>
		</div>
		<div class="panel-body">
	        <table border="1px" class="hovertable col-md-12">
	          	<tr style="background-color:#8A8A8A;color:white;">
					<td colspan="3"><b>查询订单结果</b></td>	
				</tr>
				<tr>
					<td class="bgg col-md-4">支付方式：</td>
					<td><span id="tx_yl_channelId"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">支付类型：</td>
					<td><span id="tx_yl_payType"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">支付平台流水号：</td>
					<td><span id="tx_yl_bizTransactionId"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">支付金额(元)：</td>
					<td><span id="tx_yl_amount"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">银联订单号：</td>
					<td><span id="tx_yl_paySerialNo"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">订单状态：</td>
					<td><span id="tx_yl_orderState"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">支付时间：</td>
					<td><span id="tx_yl_transDate"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">支付流水号：</td>
					<td><span id="tx_yl_bizId"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">参考号：</td>
					<td><span id="tx_yl_referNo"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">银行卡号：</td>
					<td><span id="tx_yl_cardNo"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">查询状态：</td>
					<td><span id="tx_yl_description"></span></td>
				</tr>
			</table>
		</div>			
		<div class="panel-footer" style="text-align: right;">  
			<button class="btn btn-default cx_queryordresdig" type="button" title="关闭">&nbsp;&nbsp;关闭&nbsp;&nbsp;</button>
		</div>
	</div> 
</div>

<!-- 分期保 -->
<div id="fqshowqueryordresult" class="modal fade" role="dialog" aria-labelledby="myModalLabel" data-backdrop="false"
				 data-keyboard="false" aria-hidden="true"> 
	<div class="panel panel-info">
		<div class="panel-heading">
			<button type="button" class="close cx_fqqueryordresdig"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4>查询订单结果</h4>
		</div>
		<div class="panel-body">		
	        <table border="1px" class="hovertable col-md-12">
	          	<tr style="background-color:#8A8A8A;color:white;">
					<td colspan="3"><b>查询订单结果</b></td>	
				</tr>
				<tr>
					<td class="bgg col-md-4">支付金额(保费金额, 元)：</td>
					<td><span id="tx_fq_amount"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">保网订单状态：</td>
					<td><span id="tx_fq_orderState"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">交易成功时间：</td>
					<td><span id="tx_fq_transDate"></span></td>
				</tr>
				<tr style="background-color:#9F9F9F;color:white;">
					<td colspan="3">众安订单详情</td>	
				</tr>
				<tr>
					<td class="bgg col-md-4">众安订单状态：</td>
					<td><span id="tx_fq_orderStatus"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">众安放款状态：</td>
					<td><span id="tx_fq_fundStatus"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">众安商户订单号：</td>
					<td><span id="tx_fq_partnerOrderNo"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">众安内部支付流水号(参考号)：</td>
					<td><span id="tx_fq_innerPayNo"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">放款成功时间：</td>
					<td><span id="tx_fq_fundSuccDate"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">支付成功时间：</td>
					<td><span id="tx_fq_paySuccDate"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">支付号：</td>
					<td><span id="tx_fq_payNo"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">支付总金额(保费+服务费, 元)：</td>
					<td><span id="tx_fq_payAmount"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">保费(元)：</td>
					<td><span id="tx_fq_fundAmount"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">平台服务费(元)：</td>
					<td><span id="tx_fq_partnerFee"></span></td>
				</tr>
				<tr>
					<td class="bgg col-md-4">保网订单号：</td>
					<td><span id="tx_fq_partnerPayNo"></span></td>
				</tr>
	        </table>
	    </div>
	    <div class="panel-footer" style="text-align: right;">  
			<button class="btn btn-default cx_fqqueryordresdig" type="button" title="关闭">&nbsp;&nbsp;关闭&nbsp;&nbsp;</button>
		</div>
	</div>
</div>
