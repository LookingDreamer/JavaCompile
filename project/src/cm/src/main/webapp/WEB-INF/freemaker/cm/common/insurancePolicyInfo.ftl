<!--保单信息公共页面样式表-->
<style type="text/css">
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
	.taskimgs {position: absolute;right: 20px;top: -60px;text-align: right;z-index: 999999;}
	.taskimgs .imgbtn {border-radius: 4px;}

	.taskimgs .imglist {display: none;  background: #DFF0D8;padding: 10px 15px;border: 2px solid #398439;height: 500px;overflow: scroll;overflow-x: hidden;}
</style>
<!--保单信息公共页面-->
<div class="taskimgs">
	<label style="font-weight: bold; color: red;">${carInsTaskInfo.carInfo.taskid} - ${carInsTaskInfo.carInfo.carlicenseno}&nbsp;&nbsp;&nbsp;&nbsp;</label><button type="button" class="btn btn-success imgbtn"><span class="glyphicon glyphicon-picture" aria-hidden="true"></span>&nbsp;影像信息</button>
 	<div class="imglist"><a class="btn btn-success imgbtn" href="#" id="downloadAllId">下载全部</a>
        <a class="btn btn-success imgbtn" href='javascript:window.parent.openDialogForCM("common/insurancepolicyinfo/addImageDialog?subInstanceId=${carInsTaskInfo.subInstanceId}&taskid=${carInsTaskInfoList[0].carInfo.taskid}")'><span class="glyphicon glyphicon-picture" aria-hidden="true"></span>&nbsp;添加影像</a>
		<#list carInsTaskInfo.imageList as imageInfo>
   			<div style="width: 100%; text-align:center; vertical-align:middle;line-height:100%; border: 0px solid #ccc">
                <input type="checkbox" name="allImgPathId" value="${imageInfo.filepath}" style="display: none" checked/>
   				<a href='javascript:window.parent.openImgView("common/insurancepolicyinfo/previewPicture?pictureId=${imageInfo.id}&subInstanceId=${carInsTaskInfo.subInstanceId}&taskid=${carInsTaskInfoList[0].carInfo.taskid}")'>
   					<img style="width: 120px; height: 120px; text-align:center; vertical-align:middle;line-height:120px; border: 1px solid #ccc" src="${imageInfo.smallfilepath}" alt="${imageInfo.filedescribe}" title="${imageInfo.filepath}" width="130px" height="130px">
   				</a>
   			</div>
    		<div class="caption" style="text-align:center;">
	    		<p>
	    		<a href='javascript:window.parent.openImgView("common/insurancepolicyinfo/previewPicture?pictureId=${imageInfo.id}&subInstanceId=${carInsTaskInfo.subInstanceId}&taskid=${carInsTaskInfoList[0].carInfo.taskid}")' style="display:block; width:115px; word-break:keep-all;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;" title="${imageInfo.filedescribe}">
	    		<#--${imageInfo.filedescribe}--></a>
	    		</p>
    		</div>
    	</#list>
	</div>
</div>
<div class="row">
	<div class="col-md-12">
		<table border="0px" class="hovertable col-md-12" cellpadding="1">
			<tr style="background-color:#383838;color:white;">
		    	<td colspan="8"><font size="3">代理人信息</font></td>
		    </tr>
			<tr>
			    <td class="col-md-1" style="background-color:#E5E5E5;">姓名：</td>
			    <td class="col-md-2">${agentInfo.agentname}</td>
			    <td class="col-md-1" style="background-color:#E5E5E5;">工号：</td>
			    <td class="col-md-2">${agentInfo.jobnum}</td>
			    <td class="col-md-1" style="background-color:#E5E5E5;">执业证/展示证号码：</td>
			    <td class="col-md-2">${agentInfo.licenseno}</td>
			    <td class="col-md-1" style="background-color:#E5E5E5;">身份证：</td>
			    <td class="col-md-2">${agentInfo.idno}</td>
		    </tr>
		    <tr>
			    <td class="col-md-1" style="background-color:#E5E5E5;">联系电话：</td>
			    <td class="col-md-2">${agentInfo.mobile}</td>
			    <td class="col-md-1" style="background-color:#E5E5E5;">所属团队：</td>
			    <td class="col-md-2">${agentInfo.teamname}</td>
			    <td class="col-md-1" style="background-color:#E5E5E5;">所属网点：</td>
			    <td colspan="1">${agentInfo.comname}</td>
			    <td class="col-md-1" style="background-color:#E5E5E5;">渠道来源：</td>
			    <td colspan="1">${agentInfo.purchaserchannel}</td> 
		    </tr>
            <tr>
                <td class="col-md-1" style="background-color:#E5E5E5;">渠道说明：</td>
                <td colspan="7">${agentInfo.channelinfo}</td>
            </tr>
		</table>
	</div>
 	<div class="col-md-6">
		<table border="1px" class="hovertable col-md-12" >
		    <tr style="background-color:#383838;color:white;">
		    	<td colspan="2"><font size="3">投保单信息 </font></td>
		    </tr>
		    <tr style="background-color:#8A8A8A;color:white;">
		    	<td class="col-md-12" colspan="2">
		    		<div class="row">
 						<div class="col-md-9">
 							车辆信息
 						</div>
 						<div class="col-md-3" align="right">
 							<#if showEditFlag == "1">
 								<a href="javascript:window.parent.openLargeDialog('common/insurancepolicyinfo/showOrEditInsurancePolicyInfo?mark=${"editCarInfo"}&taskid=${carInsTaskInfo.carInfo.taskid}&num=${carInsTaskInfo_index}&inscomcode=${carInsTaskInfo.inscomcode}');"><font color="white">修改</font></a>
 							</#if>
 						</div>
 					</div>
		    	</td>
		    <tr>
		    <tr>
			    <td class="bgg col-md-5">投保地区：</td>
			    <td class="col-md-7">${carInsTaskInfo.carInfo.insprovincename}${carInsTaskInfo.carInfo.inscityname}</td>
		    </tr>
		    <tr>
			    <td class="bgg">车牌：</td>
			    <td>${carInsTaskInfo.carInfo.carlicenseno}</td>
		    </tr>
		    <tr>
			    <td class="bgg">车主：</td>
			    <td><a id="carownernamePageInfo${carInsTaskInfo_index}" href="javascript:window.parent.openDialogForCM('common/insurancepolicyinfo/showOrEditInsurancePolicyInfo?mark=${"showOwnerInfo"}&taskid=${carInsTaskInfo.carInfo.taskid}&inscomcode=${carInsTaskInfo.inscomcode}');">${carInsTaskInfo.carInfo.ownername}</a></td>
		    </tr>
		    <tr>
			    <td class="bgg">车型：</td>
			    <td>
			    	<div class="row">
 						<div class="col-md-7">
 							<span id="standardnamePageInfo${carInsTaskInfo_index}">
 								${carInsTaskInfo.carInfo.standardfullname}
 							</span>
 						</div>
 						<div class="col-md-5" align="right">
 							<#if showEditFlag == "1">
 								<a href="javascript:window.parent.openLargeDialog('common/insurancepolicyinfo/showOrEditInsurancePolicyInfo?mark=${"editCarModelInfo"}&taskid=${carInsTaskInfo.carInfo.taskid}&num=${carInsTaskInfo_index}&inscomcode=${carInsTaskInfo.inscomcode}');">查看详情</a>
 							</#if>
 						</div>
 					</div>
			    </td>
		    </tr>
		    <tr>
			    <td class="bgg">发动机号：</td>
			    <td>
				    <span id="enginenoPageInfo${carInsTaskInfo_index}">
				    	${carInsTaskInfo.carInfo.engineno}
				    </span>
			    </td>
		    </tr>
		    <tr>
			    <td class="bgg">车辆识别代号：</td>
			    <td>
				    <span id="vincodePageInfo${carInsTaskInfo_index}">
				    	${carInsTaskInfo.carInfo.vincode}
				    </span>
			    </td>
		    </tr>
		    <tr>
			    <td class="bgg">车辆初登日期：</td>
			    <td>
				    <span id="registdatePageInfo${carInsTaskInfo_index}">
				   		${carInsTaskInfo.carInfo.registdate}
				    </span>
			    </td>
		    </tr>
		    <tr>
			    <td class="bgg" style="color:#ff0000">是否过户车：</td>
			    <td>
				    <span id="isTransfercarPageInfo${carInsTaskInfo_index}" style="color:#ff0000">
				    	<#if carInsTaskInfo.carInfo.isTransfercar == "1">是</#if>
						<#if carInsTaskInfo.carInfo.isTransfercar == "0">否</#if>
					</span>
			    </td>
		    </tr>
		    <tr>
			    <td class="bgg" style="color:#ff0000">过户日期：</td>
			    <td>
				    <span id="transferdatePageInfo${carInsTaskInfo_index}" style="color:#ff0000">
				    	${carInsTaskInfo.carInfo.transferdate}
					</span>
			    </td>
		    </tr>
		    <#--
		    <tr>
			    <td class="bgg">平均行驶里程：</td>
			    <td>
				    <span id="mileagevaluePageInfo${carInsTaskInfo_index}">
				    	${carInsTaskInfo.carInfo.mileagevalue}
				    </span>
			    </td>
		    </tr>
		    <tr>
			    <td class="bgg">行驶区域：</td>
			    <td>
				    <span id="drivingareavaluePageInfo${carInsTaskInfo_index}">
				    	${carInsTaskInfo.carInfo.drivingareavalue}
				    </span>
			    </td>
		    </tr>
		    -->
		    <tr style="background-color:#8A8A8A;color:white">
		    	<td class="col-md-12" colspan="2">
		    		<div class="row">
 						<div class="col-md-9">
 							关系人信息
 						</div>
 						<div class="col-md-3" align="right">
	 						<#if showEditFlag == "1">
	 							<a href="javascript:window.parent.openDialogForCM('common/insurancepolicyinfo/showOrEditInsurancePolicyInfo?mark=${"editRelationPersonInfo"}&taskid=${carInsTaskInfo.relationPersonInfo.taskid}&num=${carInsTaskInfo_index}&inscomcode=${carInsTaskInfo.inscomcode}');"><font color="white">修改</font></a>
	 						</#if>
 						</div>
 					</div>
		    	</td>
		    <tr>
		    <tr>
			    <td class="bgg">被保险人：</td>
			    <td><a id="insuredNamePageInfo${carInsTaskInfo_index}" href="javascript:window.parent.openDialogForCM('common/insurancepolicyinfo/showOrEditInsurancePolicyInfo?mark=${"showInsuredInfo"}&taskid=${carInsTaskInfo.relationPersonInfo.taskid}&inscomcode=${carInsTaskInfo.inscomcode}');">${carInsTaskInfo.relationPersonInfo.insuredname}</a></td>
		    </tr>
		    <tr>
			    <td class="bgg">投保人：</td>
			    <td><a id="applicantNamePageInfo${carInsTaskInfo_index}" href="javascript:window.parent.openDialogForCM('common/insurancepolicyinfo/showOrEditInsurancePolicyInfo?mark=${"showApplicantInfo"}&taskid=${carInsTaskInfo.relationPersonInfo.taskid}&inscomcode=${carInsTaskInfo.inscomcode}');">${carInsTaskInfo.relationPersonInfo.applicantname}</a></td>
		    </tr>
		    <tr>
			    <td class="bgg">权益索赔人：</td>
			    <td><a id="personForRightNamePageInfo${carInsTaskInfo_index}" href="javascript:window.parent.openDialogForCM('common/insurancepolicyinfo/showOrEditInsurancePolicyInfo?mark=${"showPersonForRightInfo"}&taskid=${carInsTaskInfo.relationPersonInfo.taskid}&inscomcode=${carInsTaskInfo.inscomcode}');">${carInsTaskInfo.relationPersonInfo.personForRightname}</a></td>
		    </tr>
		    <tr>
			    <td class="bgg">联系人：</td>
			    <td><a id="linkPersonNamePageInfo${carInsTaskInfo_index}" href="javascript:window.parent.openDialogForCM('common/insurancepolicyinfo/showOrEditInsurancePolicyInfo?mark=${"showLinkPersonInfo"}&taskid=${carInsTaskInfo.relationPersonInfo.taskid}&inscomcode=${carInsTaskInfo.inscomcode}');">${carInsTaskInfo.relationPersonInfo.linkPersonname}</a></td>
		    </tr>
		    <tr style="background-color:#8A8A8A;color:white">
		    	<td class="col-md-12" colspan="2">
		    		<div class="row">
 						<div class="col-md-9">
 							其他信息
 						</div>
 						<div class="col-md-3" align="right">
	 						<#if showEditFlag == "1">
	 							<a href="javascript:window.parent.openLargeDialog('common/insurancepolicyinfo/showOrEditInsurancePolicyInfo?mark=${"editOtherInfo"}&taskid=${carInsTaskInfo.otherInfo.taskid}&num=${carInsTaskInfo_index}&inscomcode=${carInsTaskInfo.inscomcode}');"><font color="white">修改</font></a>
	 						</#if>
 						</div>
 					</div>
		    	</td>
		    <tr>
		    <tr>
			    <td class="bgg">是否指定驾驶人：</td>
			    <td>
			    	<#if carInsTaskInfo.otherInfo.Specifydriver != "否">
			    		<a id="yesSpecifydriverPageInfo${carInsTaskInfo_index}" href="javascript:window.parent.openDialogForCM('common/insurancepolicyinfo/showOrEditInsurancePolicyInfo?mark=${"appointDriver"}&taskid=${carInsTaskInfo.otherInfo.taskid}&inscomcode=${carInsTaskInfo.inscomcode}');">是</a>
			    		<span id="noSpecifydriverPageInfo${carInsTaskInfo_index}"></span>
			    	<#else>
			    		<a style="display:none;" id="yesSpecifydriverPageInfo${carInsTaskInfo_index}" href="javascript:window.parent.openDialogForCM('common/insurancepolicyinfo/showOrEditInsurancePolicyInfo?mark=${"appointDriver"}&taskid=${carInsTaskInfo.otherInfo.taskid}&inscomcode=${carInsTaskInfo.inscomcode}');"></a>
			    		<span id="noSpecifydriverPageInfo${carInsTaskInfo_index}">${carInsTaskInfo.otherInfo.Specifydriver}</span>
			    	</#if>
			    </td>
		    </tr>
		    <tr>
			    <td class="bgg">商业险起保日期：</td>
			    <td>
				    <span id="businessstartdatePageInfo${carInsTaskInfo_index}">
				    	<#if carInsTaskInfo.otherInfo.hasbusi>${carInsTaskInfo.otherInfo.businessstartdate}<#else>没有商业险保单记录</#if>
				    </span>
			    </td>
		    </tr>
		    <tr>
			    <td class="bgg">商业险终止日期：</td>
			    <td>
				    <span id="businessenddatePageInfo${carInsTaskInfo_index}">
				    	<#if carInsTaskInfo.otherInfo.hasbusi>${carInsTaskInfo.otherInfo.businessenddate}<#else>没有商业险保单记录</#if>
				    </span>
			    </td>
		    </tr>
		    <tr>
			    <td class="bgg">交强险起保日期：</td>
			    <td>
				    <span id="compulsorystartdatePageInfo${carInsTaskInfo_index}">
				    	<#if carInsTaskInfo.otherInfo.hasstr>${carInsTaskInfo.otherInfo.compulsorystartdate}<#else>没有交强险保单记录</#if>
				    </span>
			    </td>
		    </tr>
		    <tr>
			    <td class="bgg">交强险终止日期：</td>
			    <td>
				    <span id="compulsoryenddatePageInfo${carInsTaskInfo_index}">
				   		<#if carInsTaskInfo.otherInfo.hasstr>${carInsTaskInfo.otherInfo.compulsoryenddate}<#else>没有交强险保单记录</#if>
				    </span>
			    </td>
		    </tr>
            <tr>
                <td class="bgg">上年商业险保单号：</td>
                <td>
                    <span id="preinsSypolicynoPageInfo${carInsTaskInfo_index}">
                    ${carInsTaskInfo.otherInfo.preinsSypolicyno}
                    </span>
                </td>
            </tr>
            <#-- 是否纯电销 -->
            <tr>
			    <td class="bgg">是否纯电销：</td>
			    <td>
				    <span id="isPureESale${carInsTaskInfo_index}">
				    	<#if carInsTaskInfo.otherInfo.pureesale == "1">是</#if>
				    	<#if carInsTaskInfo.otherInfo.pureesale == "0">否</#if>
					</span>
			    </td>
		    </tr>
		    <tr>
			    <td class="bgg">出单网点：</td>
			    <td>
				    <span id="deptComNamePageInfo${carInsTaskInfo_index}">
				    	${carInsTaskInfo.otherInfo.deptComName}
				    </span>
			    </td>
		    </tr>
		    <tr <#if carInsTaskInfo.otherInfo.invoicetype == "">style="display:none;</#if>>
			    <td class="bgg" <#if carInsTaskInfo.otherInfo.invoicetype == "">style="display:none;</#if>>发票类型：</td>
			    <td>
					    <span id="invoicetypePageInfo${carInsTaskInfo_index}">
						    <#if carInsTaskInfo.otherInfo.invoicetype == "0">
								   增值税普通发票
							<#elseif carInsTaskInfo.otherInfo.invoicetype == "1">
								   增值税专用发票
							<#elseif carInsTaskInfo.otherInfo.invoicetype == "2">
								   增值税普通发票(需资料)
							</#if>
						</span>
			    </td>
		    </tr>
		      <tr <#if carInsTaskInfo.otherInfo.invoicetype == "">style="display:none;</#if>>
			    <td class="bgg" <#if carInsTaskInfo.otherInfo.invoicetype == "">style="display:none;</#if>>开户银行名称：</td>
			    <td>
				    <span id="banknamePageInfo${carInsTaskInfo_index}">
				    	${carInsTaskInfo.otherInfo.bankname}
				    </span>
			    </td>
		    </tr>
		      <tr <#if carInsTaskInfo.otherInfo.invoicetype == "">style="display:none;</#if>>
			    <td class="bgg" <#if carInsTaskInfo.otherInfo.invoicetype == "">style="display:none;</#if>>银行账号：</td>
			    <td>
				    <span id="accountnumberPageInfo${carInsTaskInfo_index}">
				    	${carInsTaskInfo.otherInfo.accountnumber}
				    </span>
			    </td>
		    </tr>
		      <tr <#if carInsTaskInfo.otherInfo.invoicetype == "">style="display:none;</#if>>
			    <td class="bgg"<#if carInsTaskInfo.otherInfo.invoicetype == "">style="display:none;</#if>>纳税人识别号/统一社会信用代码：</td>
			    <td>
				    <span id="identifynumberPageInfo${carInsTaskInfo_index}">
				    	${carInsTaskInfo.otherInfo.identifynumber}
				    </span>
			    </td>
		    </tr>
		      <tr <#if carInsTaskInfo.otherInfo.invoicetype == "">style="display:none;</#if>>
			    <td class="bgg" <#if carInsTaskInfo.otherInfo.invoicetype == "">style="display:none;</#if>>纳税登记电话：</td>
			    <td>
				    <span id="registerphonePageInfo${carInsTaskInfo_index}">
				    	${carInsTaskInfo.otherInfo.registerphone}
				    </span>
			    </td>
		    </tr>
		      <tr <#if carInsTaskInfo.otherInfo.invoicetype == "">style="display:none;</#if>>
			    <td class="bgg" <#if carInsTaskInfo.otherInfo.invoicetype == "">style="display:none;</#if>>纳税登记地址：</td>
			    <td>
				    <span id="registeraddressPageInfo${carInsTaskInfo_index}">
				    	${carInsTaskInfo.otherInfo.registeraddress}
				    </span>
			    </td>
		    </tr>
		      <tr <#if carInsTaskInfo.otherInfo.invoicetype == "">style="display:none;</#if>>
			    <td class="bgg" <#if carInsTaskInfo.otherInfo.invoicetype == "">style="display:none;</#if>>电子邮箱：</td>
			    <td >
				    <span id="emailPageInfo${carInsTaskInfo_index}">
				    	${carInsTaskInfo.otherInfo.email}
				    </span>
			    </td>
		    </tr>
		    <tr>
			    <td class="bgg">客户忠诚度：</td>
			    <td>
				    ${carInsTaskInfo.otherInfo.customerinsurecode}
			    </td>
		    </tr>
            <tr style="color:#ff0000;">
                <td class="bgg">用户备注：</td>
                <td>
				    <span class="dis_userRemarkPageInfo${carInsTaskInfo_index}">
					<#list carInsTaskInfo.usercomment as usc>
						<#if usc.userComment.commenttype == 1 && (usc.userComment.commentcontent!='' && usc.userComment.commentcontent!='null')>
                            【${usc.typeName}】
						    ${usc.userComment.commentcontent}-${usc.userComment.operator}
							<#if (usc.userComment.modifytime)??>
							    ${usc.userComment.modifytime?datetime?string("yyyy-MM-dd HH:mm:ss")}
							<#elseif (usc.userComment.createtime)??>
							    ${usc.userComment.createtime?datetime?string("yyyy-MM-dd HH:mm:ss")}
							<#else>
                                null
							</#if>
							<#if usc_has_next>;<br/></#if>
						</#if>
					</#list>
                    </span>
                </td>
            </tr>

            <tr style="background-color:#8A8A8A;color:white;">
                <td class="col-md-12" colspan="2">
                    <div class="row">
                        <div class="col-md-9">
                            补充数据项
                        </div>
                        <div class="col-md-3" align="right">
						<#if showEditFlag == "1">
                            <a href="javascript:window.parent.openLargeDialog('common/insurancepolicyinfo/showOrEditInsurancePolicyInfo?mark=${"editSupplyParam"}&taskid=${carInsTaskInfo.carInfo.taskid}&num=${carInsTaskInfo_index}&inscomcode=${carInsTaskInfo.inscomcode}');"><font color="white">修改</font></a>
						</#if>
                        </div>
                    </div>
                </td>
            <tr>
			<#assign showcolumnfirst=5 showcolumnsecond=7 showcolumnfirstclass="bgg">
			<#include "cm/common/showSupplyParam.ftl" />
	    </table>
	</div>
	
	<div class="col-md-6">
		<!--所有报价公司code-->
		<#list carInsTaskInfoList as carInsTaskTemp>
			<input type="hidden" id="inscomcode${carInsTaskInfo_index}_${carInsTaskTemp_index}" name="inscomcodeList[${carInsTaskTemp_index}]" value="${carInsTaskTemp.inscomcode}"/>
		</#list>
		<!--任务编码-->
		<input type="hidden" id="taskid${carInsTaskInfo_index}" name="taskInstanceId" value="${carInsTaskInfo.otherInfo.taskid}"/>
		<!--遍历下标-->
		<input type="hidden" id="${carInsTaskInfo_index}" name="carInsTaskInfoindex" value="${carInsTaskInfo_index}"/>
		<!--操作的报价公司code-->
		<input type="hidden" id="thisInscomcode${carInsTaskInfo_index}" name="thisInscomcode" value="${carInsTaskInfo.inscomcode}"/>
		<!--操作的报价公司名称-->
		<input type="hidden" id="thisInscomname${carInsTaskInfo_index}" name="thisInscomname" value="${carInsTaskInfo.carInfo.inscomname}"/>
		<!--操作的报价公司名称-->
		<input type="hidden" id="thisComName${carInsTaskInfo_index}" 
			value="${carInsTaskInfo.carInfo.inscomname}
			<#if (carInsTaskInfo.carInfo.buybusitype)??>
				-${carInsTaskInfo.carInfo.buybusitype}
			<#else>
				-地面
			</#if>"/>
		<!--报价子流程id-->
		<input type="hidden" id="subInstanceId${carInsTaskInfo_index}" name="subInstanceId" value="${carInsTaskInfo.subInstanceId}"/>
		<div id="insuranceConfigContainer${carInsTaskInfo_index}">
		 	<!--引入保单配置信息公共页面-->
			<#include "cm/common/insuranceConfig.ftl" />
		</div>
		<div class="insuranceTable${carInsTaskInfo_index}">
			<div class="col-md-12">
				<#if showEditFlag == "1">
					 <button class="btn btn-primary showEditInsurance" type="button" id="showEditInsurance${carInsTaskInfo_index}" title="修改" >&nbsp;&nbsp;修改&nbsp;&nbsp;</button>
				</#if>
			</div>
		</div>
		<div class="editInsuranceTable${carInsTaskInfo_index}" style="display:none">
			<div class="col-md-12">
				<button class="btn btn-primary saveEditInsurance" type="button" id="saveEditInsurance${carInsTaskInfo_index}" title="保存单方配置">保存单方配置</button>
				<div class="checkbox">
					<label>
						<input type="checkbox" id="edit2AllList${carInsTaskInfo_index}" name="edit2AllList" value="Y"/>
						修改到所有单方
					</label>
				</div>
				<button class="btn btn-primary showAllRiskKind" style="float:right;" type="button" id="showAllRiskKind${carInsTaskInfo_index}" title="showAll">显示所有险别</button>
			</div>
		</div>
	</div>
</div>
<!--引入保单信息页面js文件-->
<script type="text/javascript">
	requirejs([ "cm/common/insurancePolicyInfo" ]);
</script>