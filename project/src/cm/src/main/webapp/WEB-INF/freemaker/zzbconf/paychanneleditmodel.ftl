<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑支付通道信息</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/system/flat-ui-icon.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/paychannelagreement" ]);
</script>
<body>
		<div class="modal-header">
        <h4 class="modal-title" id="gridSystemModalLabel">
          <#if paychannelmanager.paychannelid=='1'>
                             移动快刷配置
          <#elseif paychannelmanager.paychannelid=='2'>
			 翼支付
		  <#elseif paychannelmanager.paychannelid=='3'>
			 易联支付
		  <#elseif paychannelmanager.paychannelid=='4'>
		          柜台pos机支付
		  <#elseif paychannelmanager.paychannelid=='5'>
			 转账
		  <#elseif paychannelmanager.paychannelid=='6'>
			 平安支付  
		  </#if>
        </h4>
      </div>
	<form role="form" id="edit_zzfs_form" class="form-inline" action="savepcm" method="post" onsubmit="return onsubmitClick()">
	<input type="hidden" name="id" id="id" value="${paychannelmanager.id!'' }" />
	<input type="hidden" id="paychannelid" name="paychannelid" value="${paychannelmanager.paychannelid!'' }" />
      <div class="modal-body" style="width:100%; height:290px; overflow:auto;">
        <div class="container-fluid">
          <div class="row">
			<div class="col-md-12" id="guanlianagreement">
			  <table class="table table-bordered ">
			  <#if paychannelmanager.paychannelid=='1'>
							<tr>
								<td>收款方：</td>
								<td>
								 <input type="radio" name="collectiontype" value="1" <#if paychannelmanager.collectiontype=='1'>checked="checked"</#if>>
									保险公司 &nbsp; &nbsp; &nbsp; 
							     <input type="radio" name="collectiontype" value="2" <#if paychannelmanager.collectiontype=='2'>checked="checked"</#if>> 
									保网 &nbsp; &nbsp;&nbsp; 
								 <input type="radio" name="collectiontype" value="3" <#if paychannelmanager.collectiontype=='3'>checked="checked"</#if>>
									财险 &nbsp; &nbsp; &nbsp;
                                    <input type="radio" id="collectiontype4" name="collectiontype" value="4" <#if paychannelmanager.collectiontype?length gt 1> checked="checked"</#if>>
                                    其他第三方 &nbsp; &nbsp; &nbsp;
									<select id="collectiontype1" name="collectiontype1" class="form-control" style="display: none">
                                        <option value=''>请选择</option>
										<#list collectiontypes as code>
                                            <option value="${code.codevalue }" <#if paychannelmanager.collectiontype==code.codevalue>selected="selected"</#if>>
												${code.codename }
											</option>
										</#list>
									</select>
								</td>
							</tr>
							<tr>
								<td>快钱的结算账户号：</td>
								<td>
								    账号：<input class="form-control" id="settlementno_1" type="text" name="settlementno" value="${paychannelmanager.settlementno!'' }">
								    名称：<input class="form-control" id="settlementnoname_1" type="text" name="settlementnoname" value="${paychannelmanager.settlementnoname!'' }">
								</td>
							</tr>
							<tr>
								<td>快钱平台账户号：</td>
								<td>
								  账号：<input class="form-control" id="terraceno_1" name="terraceno" value="${paychannelmanager.terraceno!'' }" type="text">
								  名称：<input class="form-control" id="terracenoname_1" name="terracenoname" value="${paychannelmanager.terracenoname!'' }" type="text">
								</td>
							</tr>
							<tr>
								<td>支付目标：</td>
								<td>
								 <input type="radio" id="paytarget_1" name="paytarget" value="1"
								    <#if paychannelmanager.paytarget=='1'>checked="checked"</#if>> 保险公司&nbsp; &nbsp; &nbsp;</td>
							</tr>
							<tr>
								<td>排序权重：</td>
								<td>
								  <input class="form-control" id="sort_1" name="sort" type="text" value="${paychannelmanager.sort!'' }"></td>
							</tr>
							<tr>
								<td>费率优惠描述：</td>
								<td>
								  <input class="form-control" id="favorabledescribe_1" name="favorabledescribe" type="text" value="${paychannelmanager.favorabledescribe!'' }"></td>
							</tr>
			  <#elseif paychannelmanager.paychannelid=='5'>
					        <tr>
								<td>收款方：</td>
								<td><input type="radio"
									name="collectiontype" value="1" <#if paychannelmanager.collectiontype=='1'> checked="checked"</#if>>
									保险公司 &nbsp; &nbsp; &nbsp;

                                    <input type="radio" id="collectiontype4" name="collectiontype" value="4" <#if paychannelmanager.collectiontype?length gt 1> checked="checked"</#if>>
                                    其他第三方 &nbsp; &nbsp; &nbsp;
                                    <select id="collectiontype1" name="collectiontype1" class="form-control" style="display: none">
                                        <option value=''>请选择</option>
										<#list collectiontypes as code>
                                            <option value="${code.codevalue }" <#if paychannelmanager.collectiontype==code.codevalue>selected="selected"</#if>>
											${code.codename }
                                            </option>
										</#list>
                                    </select>
								</td>

							</tr>
							<tr>
								<td>支付目标：</td>
								<td><input type="radio" id="paytarget_5" name="paytarget"
									value="1" <#if paychannelmanager.paytarget=='1'> checked="checked"</#if>> 保险公司 &nbsp; &nbsp; &nbsp;</td>
							</tr>
							<tr>
								<td>转账信息：</td>
								<td><input class="form-control" type="textarea" name="transferdesc" value="${paychannelmanager.transferdesc!'' }"></td>
							</tr>
							<tr>
								<td>排序权重：</td>
								<td><input class="form-control"
									id="sort_5" name="sort" type="text" value="${paychannelmanager.sort!'' }"></td>
							</tr>
							<tr>
								<td>费率优惠描述：</td>
								<td><input class="form-control"
									id="favorabledescribe_5" name="favorabledescribe" type="text" value="${paychannelmanager.favorabledescribe!'' }"></td>
							</tr>
			  <#else>
					        <tr>
								<td>收款方：</td>
								<td>
								  <input type="radio" name="collectiontype" value="1" <#if paychannelmanager.collectiontype=='1'> checked="checked"</#if>>
									保险公司 &nbsp; &nbsp; &nbsp; 
								  <input type="radio" name="collectiontype" value="2" <#if paychannelmanager.collectiontype=='2'> checked="checked"</#if>> 
								         保网 &nbsp; &nbsp;&nbsp; 
								  <input type="radio" name="collectiontype" value="3" <#if paychannelmanager.collectiontype=='3'> checked="checked"</#if>>
									财险 &nbsp; &nbsp; &nbsp;
                                    <input type="radio" id="collectiontype4" name="collectiontype" value="4" <#if paychannelmanager.collectiontype?length gt 1> checked="checked"</#if>>
                                    其他第三方 &nbsp; &nbsp; &nbsp;
                                    <select id="collectiontype1" name="collectiontype1" class="form-control" style="display: none">
                                        <option value=''>请选择</option>
										<#list collectiontypes as code>
                                            <option value="${code.codevalue }" <#if paychannelmanager.collectiontype==code.codevalue>selected="selected"</#if>>
											${code.codename }
                                            </option>
										</#list>
                                    </select>
								</td>
							</tr>
							<tr>
								<td>支付目标：</td>
								<td><input type="radio" id="paytarget_6" name="paytarget" value="1" <#if paychannelmanager.paytarget=='1'> checked="checked"</#if>> 
								  保险公司 &nbsp; &nbsp; &nbsp;</td>
							</tr>
							<tr>
								<td>排序权重：</td>
								<td><input class="form-control"
									id="sort_6" name="sort" type="text" value="${paychannelmanager.sort!'' }"></td>
							</tr>
							<tr>
								<td>费率优惠描述：</td>
								<td><input class="form-control"
									id="favorabledescribe_6" name="favorabledescribe" type="text" value="${paychannelmanager.favorabledescribe!'' }"></td>
							</tr>
			  </#if>
			  
			                 <tr>
								<td class="col-md-2">关联协议/网点：</td>
								<td>
									<input type="hidden" id="pladeptid" name="deptid">
		  							<input type="text" class="form-control" style="width:180px;" id="deptTree" placeholder="请选择平台">&nbsp;&nbsp;&nbsp;
		  							<input type="text" class="form-control" style="width:180px;" id="agreementid1" name="agreementid" readonly="true" placeholder="请从以下选择协议">&nbsp;&nbsp;&nbsp;
							        <!-- <select class="form-control" style="width:180px;" id="agreementid1" name="agreementid" >
									</select>&nbsp;&nbsp;&nbsp;
									<select class="form-control" style="width:180px;" id="wdid2" name="deptid" >
									</select>&nbsp;&nbsp;&nbsp;-->
							    </td>
							</tr>
							<#-- <tr>
								<td class="col-md-2">关联机构：</td>
								<td  class="col-md-10">
								   <input class="form-control m-left-5" type="hidden" id="deptid" name="deptid" value="${dept.id!'' }"> 
								   <input class="form-control" type="text" id="deptname" value="${dept.shortname!'' }">
								   <button class="btn btn-default" id="checkdept2" type="button" onclick="showdeptsel()">修改</button>
								</td>
							</tr>
							<tr id="deptseltr" style="display: none;">
							    <td></td>
							    <td>
							        <select class="form-control" style="width:130px;" id="deptidc1" name="deptid1" onchange="deptconnchange(1)">
								      <option value="" selected="true" disabled="true">请选择</option>
									</select> 
									<select class="form-control" style="width:130px;" id="deptidc2" name="deptid2"
										onchange="deptconnchange(2)">								
									</select> 
									<select class="form-control" style="width:130px;" id="deptidc3" name="deptid3"
										onchange="deptconnchange(3)">
									</select> 
									<select class="form-control" style="width:130px;" id="deptidc4" name="deptid4"
										onchange="deptconnchange(4)">								
									</select> 
									<select class="form-control" style="width:130px;" id="deptidc5" name="deptid5">								
									</select>
									<button id="savebutton" type="button" name="savebutton" class="btn btn-primary" onclick="savedeptsel()">选择</button>
									<button id="hiddendeptselbutton" type="button" class="btn btn-primary" onclick="hiddendeptsel()">隐藏</button>
							    </td>
							</tr>
							
							<tr>
								<td>关联供应商：</td>
								<td>
								   <input class="form-control m-left-5" type="hidden" id="providerid" name="providerid" value="${provider.id!'' }"> 
								   <input class="form-control" type="text" id="providername" value="${provider.prvshotname!'' }">
								   <button class="btn btn-default" id="checkdept2" type="button" onclick="showprosel()">修改</button>
							    </td>
							</tr>
							<tr id="proseltr" style="display: none;">
							    <td></td>
							    <td>
							        <select class="form-control" style="width:130px;" id="provideridp1" name="providerid1" onchange="providerconnchange(1)">
								      <option value="" selected="true" disabled="true">请选择</option>
									</select> 
									<select class="form-control" style="width:130px;" id="provideridp2" name="providerid2" onchange="providerconnchange(2)">								
									</select> 
									<select class="form-control" style="width:130px;" id="provideridp3" name="providerid3" onchange="providerconnchange(3)">
									</select> 
									<select class="form-control" style="width:130px;" id="provideridp4" name="providerid4" onchange="providerconnchange(4)">								
									</select> 
									<button id="savebutton" type="button" name="savebutton" class="btn btn-primary" onclick="saveprosel()">选择</button>
									<button id="hiddenproselbutton" type="button" class="btn btn-primary" onclick="hiddenprosel()">隐藏</button>
							    </td>
							</tr> -->
							
			  </table>
			  
			</div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
		<input type="submit" class="btn btn-primary" style="margin-left: 5px;" value="保存"/>
        <input type="button" class="btn btn-primary"  id="closeMyModel" value="关闭"/>
      </div>
 </form>
 <div id="showdept" class="modal fade" role="dialog"
		aria-labelledby="myModalLabel" data-backdrop="false"
		data-keyboard="false" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="gridSystemModalLabel">选择机构</h4>
				</div>
				<div class="modal-body" style="overflow: auto; height: 400px;">
					<div class="container-fluid">
						<div class="row">
							<ul id="depttree" class="ztree"></ul>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
 </body>
 </html>
      
