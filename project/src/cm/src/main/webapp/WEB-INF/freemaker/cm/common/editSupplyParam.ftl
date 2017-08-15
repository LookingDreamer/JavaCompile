<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>修改补充数据项</title>
	<script type="text/javascript">
		requirejs([ "cm/common/editSupplyParam" ],function() {
			require(["jquery","bootstrap-table", "bootstrap","bootstrapTableZhCn","additionalmethods","public"],function ($) {
				$(function(){
                    initEditSupplyParamScript();
				});
			});
		});
	</script>
	<style type="text/css">
		#sure{
			position: relative;
			left: 310px;
		}
		#cancel{
			position: relative;
			left: 370px;
		}
		span.feildtitle {
			font-weight:bold;
			color:#34495E;
		}
		body {font-size: 14px;}
	</style>
</head>
<body>

<form id="editSupplyParamForm">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<h6 class="modal-title">修改补充数据项</h6>
	</div>

	<input type="hidden" name="taskid" value="${taskid}"/>
	<input type="hidden" name="inscomcode" value="${inscomcode}"/>
	<!-- 选项卡下标 -->
	<input type="hidden" id="num" name="num" value="${num}"/>

	<div class="modal-body">
		<div class="panel panel-default" style="margin-bottom:0px">
			<div class="panel-heading">
				<div class="row">
					<div class="col-md-12">
						<span class="feildtitle">&nbsp;</span>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<table class="table table-bordered" style="margin-bottom:0px">
						<tr>
							<td class="col-md-3">被保人身份证地址:</td>
							<td class="col-md-3">
                                <input class="form-control" type="text" name="supplyparam[0].itemvalue" value="${supplyParams.insuredAddress_value}"/>
								<input class="form-control" type="hidden" name="supplyparam[0].itemcode" value="insuredAddress"/>
                                <input class="form-control" type="hidden" name="supplyparam[0].itemname" value="被保人身份证地址"/>
							</td>
							<td>车主手机号码:</td>
							<td>
                                <input class="form-control phone" type="text" name="supplyparam[1].itemvalue" value="${supplyParams.ownerMobile_value}"/>
                                <input class="form-control" type="hidden" name="supplyparam[1].itemcode" value="ownerMobile"/>
                                <input class="form-control" type="hidden" name="supplyparam[1].itemname" value="车主手机号码"/>
							</td>
						</tr>
                        <tr>
                            <td>被保人身份证有效止期:</td>
                            <td>
                                <input class="form-control validDate" type="text" name="supplyparam[2].itemvalue" placeholder="yyyy-MM-dd或长期" value="${supplyParams.insuredIDCardValidDate_value}"/>
                                <input class="form-control" type="hidden" name="supplyparam[2].itemcode" value="insuredIDCardValidDate"/>
                                <input class="form-control" type="hidden" name="supplyparam[2].itemname" value="被保人身份证有效止期"/>
                            </td>
                            <td>车主邮箱:</td>
                            <td>
                                <input class="form-control email" type="text" name="supplyparam[3].itemvalue" value="${supplyParams.ownerEmail_value}"/>
                                <input class="form-control" type="hidden" name="supplyparam[3].itemcode" value="ownerEmail"/>
                                <input class="form-control" type="hidden" name="supplyparam[3].itemname" value="车主邮箱"/>
                            </td>
                        </tr>
                        <tr>
                            <td>被保人手机号码:</td>
                            <td>
                                <input class="form-control phone" type="text" name="supplyparam[4].itemvalue" value="${supplyParams.insuredMobile_value}"/>
                                <input class="form-control" type="hidden" name="supplyparam[4].itemcode" value="insuredMobile"/>
                                <input class="form-control" type="hidden" name="supplyparam[4].itemname" value="被保人手机号码"/>
                            </td>
                            <td>车主身份证地址:</td>
                            <td>
                                <input class="form-control" type="text" name="supplyparam[5].itemvalue" value="${supplyParams.ownerAddress_value}"/>
                                <input class="form-control" type="hidden" name="supplyparam[5].itemcode" value="ownerAddress"/>
                                <input class="form-control" type="hidden" name="supplyparam[5].itemname" value="车主身份证地址"/>
                            </td>
                        </tr>
                        <tr>
                            <td>被保人邮箱:</td>
                            <td>
                                <input class="form-control email" type="text" name="supplyparam[6].itemvalue" value="${supplyParams.insuredEmail_value}"/>
                                <input class="form-control" type="hidden" name="supplyparam[6].itemcode" value="insuredEmail"/>
                                <input class="form-control" type="hidden" name="supplyparam[6].itemname" value="被保人邮箱"/>
                            </td>
                            <td>车主身份证有效止期:</td>
                            <td>
                                <input class="form-control validDate" type="text" name="supplyparam[7].itemvalue" placeholder="yyyy-MM-dd或长期" value="${supplyParams.ownerIDCardValidDate_value}"/>
                                <input class="form-control" type="hidden" name="supplyparam[7].itemcode" value="ownerIDCardValidDate"/>
                                <input class="form-control" type="hidden" name="supplyparam[7].itemname" value="车主身份证有效止期"/>
                            </td>
                        </tr>
                        <tr>
                            <td>投保人手机号码:</td>
                            <td>
                                <input class="form-control phone" type="text" name="supplyparam[8].itemvalue" value="${supplyParams.applicantMobile_value}"/>
                                <input class="form-control" type="hidden" name="supplyparam[8].itemcode" value="applicantMobile"/>
                                <input class="form-control" type="hidden" name="supplyparam[8].itemname" value="投保人手机号码"/>
                            </td>
                            <td>行驶证地址:</td>
                            <td>
                                <input class="form-control" type="text" name="supplyparam[9].itemvalue" value="${supplyParams.drivingLicenseAddress_value}"/>
                                <input class="form-control" type="hidden" name="supplyparam[9].itemcode" value="drivingLicenseAddress"/>
                                <input class="form-control" type="hidden" name="supplyparam[9].itemname" value="行驶证地址"/>
                            </td>
                        </tr>
                        <tr>
                            <td>投保人邮箱:</td>
                            <td>
                                <input class="form-control email" type="text" name="supplyparam[10].itemvalue" value="${supplyParams.applicantEmail_value}"/>
                                <input class="form-control" type="hidden" name="supplyparam[10].itemcode" value="applicantEmail"/>
                                <input class="form-control" type="hidden" name="supplyparam[10].itemname" value="投保人邮箱"/>
                            </td>
                            <td>权益索赔人手机号码:</td>
                            <td>
                                <input class="form-control phone" type="text" name="supplyparam[11].itemvalue" value="${supplyParams.claimantMobile_value}"/>
                                <input class="form-control" type="hidden" name="supplyparam[11].itemcode" value="claimantMobile"/>
                                <input class="form-control" type="hidden" name="supplyparam[11].itemname" value="权益索赔人手机号码"/>
                            </td>
                        </tr>
						<tr>
                            <td>投保人身份证地址:</td>
                            <td>
                                <input class="form-control" type="text" name="supplyparam[12].itemvalue" value="${supplyParams.applicantAddress_value}"/>
                                <input class="form-control" type="hidden" name="supplyparam[12].itemcode" value="applicantAddress"/>
                                <input class="form-control" type="hidden" name="supplyparam[12].itemname" value="投保人身份证地址"/>
                            </td>
                            <td>权益索赔人邮箱:</td>
                            <td>
                                <input class="form-control email" type="text" name="supplyparam[13].itemvalue" value="${supplyParams.claimantEmail_value}"/>
                                <input class="form-control" type="hidden" name="supplyparam[13].itemcode" value="claimantEmail"/>
                                <input class="form-control" type="hidden" name="supplyparam[13].itemname" value="权益索赔人邮箱"/>
                            </td>
						</tr>
                        <tr>
                            <td>投保人身份证有效止期:</td>
                            <td>
                                <input class="form-control validDate" type="text" name="supplyparam[14].itemvalue" placeholder="yyyy-MM-dd或长期" value="${supplyParams.applicantIDCardValidDate_value}"/>
                                <input class="form-control" type="hidden" name="supplyparam[14].itemcode" value="applicantIDCardValidDate"/>
                                <input class="form-control" type="hidden" name="supplyparam[14].itemname" value="投保人身份证有效止期"/>
                            </td>
                            <td>权益索赔人证件类型:</td>
                            <td>
                                <select class="form-control idcardtype" name="supplyparam[15].itemvalue">
                                    <option value="">--请选择--</option>
                                <#list CertKindList as CertKinditem>
                                    <option value="${CertKinditem.codevalue}"
                                            <#if CertKinditem.codevalue == supplyParams.claimantDocumentType_value>selected</#if>>
                                    ${CertKinditem.codename}
                                    </option>
                                </#list>
                                </select>
                                <input class="form-control" type="hidden" name="supplyparam[15].itemcode" value="claimantDocumentType"/>
                                <input class="form-control" type="hidden" name="supplyparam[15].itemname" value="权益索赔人证件类型"/>
                                <input class="form-control" type="hidden" name="supplyparam[15].iteminputtype" value="select"/>
                            </td>
                        </tr>
                        <tr>
                            <td>权益索赔人姓名:</td>
                            <td>
                                <input class="form-control" type="text" name="supplyparam[16].itemvalue" value="${supplyParams.claimantName_value}"/>
                                <input class="form-control" type="hidden" name="supplyparam[16].itemcode" value="claimantName"/>
                                <input class="form-control" type="hidden" name="supplyparam[16].itemname" value="权益索赔人姓名"/>
                            </td>
                            <td>权益索赔人证件号码:</td>
                            <td colspan="3">
                                <input class="form-control idcardno" type="text" name="supplyparam[17].itemvalue" value="${supplyParams.claimantDocumentNumber_value}"/>
                                <input class="form-control" type="hidden" name="supplyparam[17].itemcode" value="claimantDocumentNumber"/>
                                <input class="form-control" type="hidden" name="supplyparam[17].itemname" value="权益索赔人证件号码"/>
                            </td>
                        </tr>
					</table>
				</div>
			</div>
		</div>
		<div class="row">
	 	 	<div class="col-md-6">
	 			<button class="btn btn-default" type="button" id="submitSupplyparams" title="确定">&nbsp;&nbsp;确定&nbsp;&nbsp;</button>
	 	 	</div>
	 	 	<div class="col-md-6" align="right">
	 			<button class="btn btn-default" type="button" title="取消" data-dismiss="modal">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>
	 	 	</div>
	  </div>
	</div>
</form>
</body>
</html>
