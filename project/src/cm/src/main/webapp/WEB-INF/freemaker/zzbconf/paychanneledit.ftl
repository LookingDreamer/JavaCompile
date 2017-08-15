<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑支付通道信息</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/system/flat-ui-icon.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/paychannel" , "lib/tsearch" ]);
</script>
<body>
	<div class="container-fluid">
	 <form class="form-inline" role="form"  action="update" method="post">
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">基本配置</div>
		   <div class="row">
		  	<div class="panel-body">
			  	<input type="hidden" id="hiddenid" name="id"  >
				 <table class="table table-bordered">
					  <tr>
						<td style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName" >支付通道ID</label></td>
						<td><input readonly="readonly" style='border-left:0px;border-top:0px;border-right:0px;border-bottom:1px ' class="form-control" type="text" id="paychannelid"  name="paychannelid" value="${paychannel.id!'' }"></td>
					  </tr>
					  <tr>
						<td style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">支付通道名称</label></td>
						<td style="vertical-align: middle;"><input class="form-control" type="text" id="paychannelname" name="paychannelname" value="${paychannel.paychannelname!'' }">
							<input type="radio" value="0" name="stateflag" <#if "${paychannel.stateflag}" == "0" >checked="checked" </#if> >&nbsp;启用&nbsp;
							<input type="radio" value="1" name="stateflag" <#if "${paychannel.stateflag}" == "1" >checked="checked" </#if>>&nbsp;关闭&nbsp;
						</td>
					  </tr>
					  <tr>
						<td class="col-md-2 active"><label for="exampleInputName">支付类型</label></td>
						<td>
						
							<select name="paytype" class="form-control">
								<#list paytype as paytype>
						  				<option value="${paytype.codevalue}" <#if "${paychannel.paytype}" == "${paytype.codevalue}" >selected="selected" </#if>  >${paytype.codename}</option>
				  				</#list>
							</select>

						</td>
					  </tr>
					  <tr>
						<td class="col-md-2 active"><label for="exampleInputName">支付终端</label></td>
						<td>
							<select name="supportterminal" class="form-control">
								<#if paychannel.supportterminal==''>
								<option selected="selected" value="">请选择</option>
								<option  value="01">移动端</option>
								<option  value="02">PC端</option>
								<option  value="03">移动和PC端</option>
								<#elseif paychannel.supportterminal=='01'>
								<option  value="">请选择</option>
								<option  selected="selected" value="01">移动端</option>
								<option  value="02">PC端</option>
								<option  value="03">移动和PC端</option>
								<#elseif paychannel.supportterminal=='02'>
								<option  value="">请选择</option>
								<option  value="01">移动端</option>
								<option  selected="selected" value="02">PC端</option>
								<option  value="03">移动和PC端</option>
								<#elseif paychannel.supportterminal=='03'>
								<option selected="selected" value="">请选择</option>
								<option  value="01">移动端</option>
								<option  value="02">PC端</option>
								<option  selected="selected" value="03">移动和PC端</option>
								</#if>
							</select>
						</td>
					  </tr>
					 <tr>
						<td class="col-md-2 active"><label for="exampleInputName">排序权重</label></td>
						<td style="vertical-align: middle;"><input class="form-control" type="text" id="cardbankdeptcode" name="sort" value="${paychannel.sort!'' }">&nbsp;(数字越大越优先)</td>
					  </tr>

					 <tr>
						 <td class="col-md-2 active">是否仅支持线上EDI核保</td>
						 <td>
                             <label class="checkbox-inline">
                                 <input type="radio" name="onlyedionlineunderwriting" id="onlyEdiOnlineUnderwritingYes" value="true" <#if paychannel.onlyedionlineunderwriting==true>checked</#if>> 是
                             </label>
                             <label class="checkbox-inline">
                                 <input type="radio" name="onlyedionlineunderwriting" id="onlyEdiOnlineUnderwritingNo"  value="false" <#if paychannel.onlyedionlineunderwriting==false>checked</#if>> 否
                             </label>
						 </td>
					 </tr>


					 <tr>
						 <td class="col-md-2 active">终端类型</td>
						 <td>
						 <#assign clienttypes=paychannel.clienttypes?lower_case?split(",")>
						 <label class="checkbox-inline">
							 <input type="checkbox" id="chb-web" <#if clienttypes?seq_contains("web")>checked="checked"</#if> name="clienttypes" value="web">网页版
						 </label>
						 <label class="checkbox-inline">
							 <input type="checkbox" id="chb-wechat" <#if clienttypes?seq_contains("wechat")>checked="checked"</#if> name="clienttypes" value="weChat">微信版
						 </label>
						 <label class="checkbox-inline">
							 <input type="checkbox" id="chb-ios" <#if clienttypes?seq_contains("ios")>checked="checked"</#if> name="clienttypes" value="ios">IOS版
						 </label>
						 <label class="checkbox-inline">
							 <input type="checkbox" id="chb-android" <#if clienttypes?seq_contains("android")>checked="checked"</#if> name="clienttypes" value="android">Android版
						 </label>
						 </td>
					 </tr>
					  <!--
					  <tr>
						<td style="vertical-align: middle;" rowspan="6" class="col-md-2 active"><label for="exampleInputName">不支持的操作系统</label></td>
					  </tr>
					  <tr>
					  	<td class="col-md-6 ">IOS:&nbsp;&nbsp;&nbsp; 
						 	<#list systemios as ioslist>
						 		<input name="iossystem" type="checkbox" value="${ioslist.codevalue}" <#if "${ioscheck }"?contains("${ioslist.codevalue}") >checked="checked" </#if> >${ioslist.codename!'' }&nbsp;&nbsp;
							</#list>
						</td>
					  </tr>
					  <tr>
					  	<td class="col-md-6 ">安卓:&nbsp;&nbsp;&nbsp;
					
					  		<#list systemandroid as androidlist>
						 		<input name="androidsystem" type="checkbox" value="${androidlist.codevalue}" <#if "${androidcheck }"?contains("${androidlist.codevalue}") >checked="checked" </#if> >${androidlist.codename!'' }&nbsp;&nbsp;
							</#list>
					  	</td>
					  </tr>
					  <tr>
					  	<td class="col-md-6 ">Windows Phone:&nbsp;&nbsp;&nbsp;
					  		<#list systemwinphone as winphonelist>
						 		<input name="winphonesystem" type="checkbox" value="${winphonelist.codevalue}" <#if "${winphonecheck }"?contains("${winphonelist.codevalue}") >checked="checked" </#if> >${winphonelist.codename!'' }&nbsp;&nbsp;
							</#list>
					  	</td>
					  </tr>
					  <tr>
					  	<td class="col-md-6 ">Windows:&nbsp;&nbsp;&nbsp;
					  		<#list systemwindows as windowslist>
						 		<input name="winsystem" type="checkbox" value="${windowslist.codevalue}" <#if "${windowscheck }"?contains("${windowslist.codevalue}") >checked="checked" </#if> >${windowslist.codename!'' }&nbsp;&nbsp;
							</#list>
					  	</td>
					  </tr>
					  <tr>
					  	<td class="col-md-6 ">OS:&nbsp;&nbsp;&nbsp;
					  		<#list systemos as oslist>
						 		<input name="ossystem" type="checkbox" value="${oslist.codevalue}" <#if "${oscheck }"?contains("${oslist.codevalue}") >checked="checked" </#if> >${oslist.codename!'' }&nbsp;&nbsp;
							</#list>
					  	</td>
					  </tr>
					  <tr>
						<td class="col-md-2 active"><label for="exampleInputName">支付通道单笔支付限额</label></td>
						<td style="vertical-align: middle;"><input class="form-control" type="text" id="paychannellimit" name="paychannellimit" value="${paychannel.paychannellimit!'' }">&nbsp;元</td>
					  </tr>
					  <tr>
						<td class="col-md-2 active"><label for="exampleInputName">排序权重</label></td>
						<td style="vertical-align: middle;"><input class="form-control" type="text" id="cardbankdeptcode" name="sort" value="${paychannel.sort!'' }">&nbsp;(数字越大越优先)</td>
					  </tr>
					  <tr>
						<td class="col-md-2 active"><label for="exampleInputName">是否支持客户付款</label></td>
						<td style="vertical-align: middle;">
						<input  type="radio" value="0" name="clientpayflag" <#if "${paychannel.clientpayflag}" == "0" >checked="checked" </#if>>&nbsp;支持&nbsp;
						<input  type="radio" value="1" name="clientpayflag" <#if "${paychannel.clientpayflag}" == "1" >checked="checked" </#if>>&nbsp;不支持&nbsp;
						</td>
					  </tr>
					  <tr>
						<td class="col-md-2 active"><label for="exampleInputName">通道描述</label></td>
						<td ><textarea id="explains"  name="channeldescribe" class="form-control"  rows="3" >${paychannel.channeldescribe!'' }</textarea></td>
					  </tr>
					  <tr>
						<td class="col-md-2 active"><label for="exampleInputName">费率优惠描述</label></td>
						<td ><textarea id="explains"  name="costratedescribe" class="form-control"  rows="3" >${paychannel.costratedescribe!'' }</textarea></td>
					  </tr>
					  -->
				  </table>
			</div>
		  </div>
		</div>
		<!--
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">收取代理人手续费配置</div>
		   <div class="row">
		  	<div class="panel-body">
			 <table class="table table-bordered">
				  <tr>
					<td class="col-md-2 active"><label for="exampleInputName">是否显示手续费</label></td>
					<td>
						<input id="dontshow" type="radio" name="showpoundageflag" value="1" <#if "${paychannel.showpoundageflag}" == "1" >checked="checked" </#if>>&nbsp;否&nbsp;
						<input id="show" type="radio" name="showpoundageflag" value="0" <#if "${paychannel.showpoundageflag}" == "0" >checked="checked" </#if>>&nbsp;是&nbsp;
					</td>
					<td class="col-md-2 active"><label for="exampleInputName">是否收取手续费</label></td>
					<td >
						<input id="charge1"type="radio" name="chargepoundagefalg" value="1" <#if "${paychannel.chargepoundagefalg}" == "1" >checked="checked" </#if>>&nbsp;否&nbsp;
						<input id="charge0" type="radio" name="chargepoundagefalg" value="0" <#if "${paychannel.chargepoundagefalg}" == "0" >checked="checked" </#if>>&nbsp;是&nbsp;
					</td>
				  </tr>
				  <tr>
					<td class="col-md-2 active"><label for="exampleInputName">征收比率</label></td>
					<td style="vertical-align: middle;" nowrap='nowrap'><input  type="text" id="ratio" name="ratio" value="${paychannel.ratio!'' }">&nbsp;%&nbsp;</td>
				  </tr>
				  <tr>
					<td class="col-md-2 active"><label for="exampleInputName">手续费区分方法</label></td>
					<td style="vertical-align: middle;">
						<input id="poundageradio1" type="radio" name="poundageflag" value="1" <#if "${paychannel.poundageflag}" == "1" >checked="checked" </#if>>&nbsp;否&nbsp;
						<input id="poundageradio2" type="radio" name="poundageflag" value="0" <#if "${paychannel.poundageflag}" == "0" >checked="checked" </#if>>&nbsp;是&nbsp;
					</td>
				  </tr>
				  <tr>
					<td style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">手续费收取方法</label></td>
					<td style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">手续费比率</label></td>
					<td style="vertical-align: middle;" nowrap='nowrap'><input  type="text" id="poundageratio" name="poundageratio" value="${paychannel.poundageratio!'' }">&nbsp;%&nbsp;+</td>
					<td style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">单笔固定费</label></td>
					<td style="vertical-align: middle;" nowrap='nowrap'><input  type="text" id="fixedfee" name="fixedfee" value="${paychannel.fixedfee!'' }">&nbsp;元&nbsp;</td>
				  </tr>
				  <tr id="credittr">
					<td style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">信用卡手续费收取方法</label></td>
					<td style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">手续费比率</label></td>
					<td style="vertical-align: middle;" nowrap='nowrap'><input  type="text" id="creditpoundageratio" name="creditpoundageratio" value="${paychannel.creditpoundageratio!'' }">&nbsp;%&nbsp;+</td>
					<td style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">单笔固定费</label></td>
					<td style="vertical-align: middle;" nowrap='nowrap'><input  type="text" id="creditfixedfee" name="creditfixedfee" value="${paychannel.creditfixedfee!'' }">&nbsp;元&nbsp;</td>
				  </tr>
				  <tr id="cashtr">
					<td style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">储蓄卡手续费收取方法</label></td>
					<td style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">手续费比率</label></td>
					<td style="vertical-align: middle;" nowrap='nowrap'><input type="text" id="cashpoundageratio" name="cashpoundageratio" value="${paychannel.cashpoundageratio!'' }">&nbsp;%&nbsp;+</td>
					<td style="vertical-align: middle;" class="col-md-2 active"><label for="exampleInputName">单笔固定费</label></td>
					<td style="vertical-align: middle;" nowrap='nowrap'><input type="text" id="cashfixedfee" name="cashfixedfee" value="${paychannel.cashfixedfee!'' }">&nbsp;元&nbsp;</td>
				  </tr>
			  </table>
			</div>
		  </div>
		</div>
		-->
		
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">支持银行卡配置</div>
		   <div class="row">
		  	<div class="panel-body">
			  	<input type="hidden" id="hiddenid" name="id"  >
			  	<#assign sum = bankconfinfo?size >
			  	<input type="hidden" id="hiddensum"  value="${sum }">
				 <table class="table table-bordered" id ="tb1">
					  <tr>
						<td class="col-md-1 active"><input type="checkbox" name="all" id="all">&nbsp;<label for="exampleInputName">全选</label></td>
						<td class="col-md-3 active"><label for="exampleInputName">可支持银行</label></td>
						<td class="col-md-2 active"><label for="exampleInputName">支持银行卡种类</label></td>
						<td class="col-md-2 active"><label for="exampleInputName">单笔支付限额</label></td>
						<td class="col-md-2 active"><label for="exampleInputName">日支付限额</label></td>
					  </tr>
					  
				    
					  
				<#if (bankconfinfo?size>0) >
					<#list bankconfinfo as conflist>
					
					  <tr>
					  	<td	style="vertical-align: middle;" rowspan="2"><input name="bankcardconflist[${conflist_index }].ischecked" type="checkbox" checked="checked"></td>
					  	<td style="vertical-align: middle;" rowspan="2">
					  		<input   type="hidden" name="bankcardconflist[${conflist_index }].id" value="${conflist.id!'' }">
					  		<select name="bankcardconflist[${conflist_index }].insbcodevalue">
					  			<option value="">-请选择银行-</option>
					  			<#list bankcardlist as bankcard>
					  				<option value="${bankcard.codevalue}" <#if "${conflist.insbcodevalue}" == "${bankcard.codevalue}" >selected="selected" </#if>  >${bankcard.codename}</option>
					  			</#list>
					  		</select>&nbsp;
					  		<input  name="bankcardconflist[${conflist_index }].commonlyusedbankflag" type="checkbox" value="1" <#if "${conflist.commonlyusedbankflag}" == "1" >checked="checked" </#if> >&nbsp;常用银行
					  	</td>
					  	<td nowrap="nowrap"><input type="checkbox" name="bankcardconflist[${conflist_index }].cashcardflag" value="1"  <#if "${conflist.cashcardflag}" == "1" >checked="checked" </#if>>&nbsp;储蓄卡</td>
					  	<td nowrap="nowrap"><input type="text" value="${conflist.cashlimit!'' }" onChange="haoba(this.value)"   name="bankcardconflist[${conflist_index }].cashlimit">&nbsp;
					  						<input name="bankcardconflist[${conflist_index }].cashlimitfalg" type="checkbox" value="1" <#if "${conflist.cashlimitfalg}" == "1" >checked="checked" </#if>>&nbsp;无限额</td>
					  	<td nowrap="nowrap"><input type="text" value="${conflist.cashdaylimit!'' }" onChange="haoba(this.value)"  name="bankcardconflist[${conflist_index }].cashdaylimit">&nbsp;
					  						<input name="bankcardconflist[${conflist_index }].cashlimitdayfalg" type="checkbox" value="1" <#if "${conflist.cashlimitdayfalg}" == "1" >checked="checked" </#if> >&nbsp;无限额</td>
					  </tr>
					  <tr>
					  	<td nowrap="nowrap"><input type="checkbox" value="1" name="bankcardconflist[${conflist_index }].creditcardflag" <#if "${conflist.creditcardflag}" == "1" >checked="checked" </#if> >&nbsp;信用卡</td>
					  	<td nowrap="nowrap"><input type="text" value="${conflist.creditlimit!'' }" onChange="haoba(this.value)"  name="bankcardconflist[${conflist_index }].creditlimit" >&nbsp;
					  						<input name="bankcardconflist[${conflist_index }].creditlimitfalg" type="checkbox" value="1" <#if "${conflist.creditlimitfalg}" == "1" >checked="checked" </#if>>&nbsp;无限额</td>
					  	<td nowrap="nowrap"><input type="text" value="${conflist.creditdaylimit!'' }" onChange="haoba(this.value)"  name="bankcardconflist[${conflist_index }].creditdaylimit" >&nbsp;
					  						<input name="bankcardconflist[${conflist_index }].creditlimitdayfalg" type="checkbox" value="1" <#if "${conflist.creditlimitdayfalg}" == "1" >checked="checked" </#if>>&nbsp;无限额</td>
					  </tr>
					</#list>
				<#else>
					    <tr>
					  	<td	style="vertical-align: middle;" rowspan="2"><input name="bankcardconflist[0].ischecked" checked="checked"  type="checkbox" ></td>
					  	<td style="vertical-align: middle;" rowspan="2">
					  	<input type="hidden"  name="bankcardconflist[0].id" >
					  		<select name="bankcardconflist[0].insbcodevalue">
					  			<option value="">-请选择银行-</option>
					  			<#list bankcardlist as bankcard>
					  				<option value="${bankcard.codevalue}" >${bankcard.codename}</option>
					  			</#list>
					  		</select>&nbsp;
					  		<input  name="bankcardconflist[0].commonlyusedbankflag" type="checkbox" value="1" <#if "${conflist.commonlyusedbankflag}" == "1" >checked="checked" </#if> >&nbsp;常用银行
					  	</td>
					  	<td nowrap="nowrap"><input type="checkbox" name="bankcardconflist[0].cashcardflag" value="1"  <#if "${conflist.cashcardflag}" == "1" >checked="checked" </#if>>&nbsp;储蓄卡</td>
					  	<td nowrap="nowrap"><input type="text" value="${conflist.cashlimit!'' }" name="bankcardconflist[0].cashlimit">&nbsp;
					  						<input name="bankcardconflist[0].cashlimitfalg" type="checkbox" value="1" <#if "${conflist.cashlimitfalg}" == "1" >checked="checked" </#if>>&nbsp;无限额</td>
					  	<td nowrap="nowrap"><input type="text" value="${conflist.cashdaylimit!'' }" name="bankcardconflist[0].cashdaylimit">&nbsp;
					  						<input name="bankcardconflist[0].cashlimitdayfalg" type="checkbox" value="1" <#if "${conflist.cashlimitdayfalg}" == "1" >checked="checked" </#if> >&nbsp;无限额</td>
					  </tr>
					  <tr>
					  	<td nowrap="nowrap"><input type="checkbox" value="1" name="bankcardconflist[0].creditcardflag" <#if "${conflist.creditcardflag}" == "1" >checked="checked" </#if> >&nbsp;信用卡</td>
					  	<td nowrap="nowrap"><input type="text" value="${conflist.creditlimit!'' }" name="bankcardconflist[0].creditlimit" >&nbsp;
					  						<input name="bankcardconflist[0].creditlimitfalg" type="checkbox" value="1" <#if "${conflist.creditlimitfalg}" == "1" >checked="checked" </#if>>&nbsp;无限额</td>
					  	<td nowrap="nowrap"><input type="text" value="${conflist.creditdaylimit!'' }" name="bankcardconflist[0].creditdaylimit" >&nbsp;
					  						<input name="bankcardconflist[0].creditlimitdayfalg" type="checkbox" value="1" <#if "${conflist.creditlimitdayfalg}" == "1" >checked="checked" </#if>>&nbsp;无限额</td>
					  </tr>
				</#if>
				  </table>
				  <table cellspacing="50%" cellpadding="80%">
				  	<tr>
					  	<td>
					  		<input type="button" onclick="addtr()" value="+添加支持银行" />
					  		<input type="button" id="delcheckbank" value="-删除已选中银行" />
					  	</td>
				  	</tr>
				  </table>
			</div>
		  </div>
		</div>
		<div style="padding:5px;">
		    <input class="btn btn-primary" id="paychannelsaveform" type="submit" value="保存">
		    <input class="btn btn-primary" id="backbutton" type="button" value="返回">
		</div>
	  </form>
		     	 	
		
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">支付方式配置</div>
		   <div class="row">
		  	<div class="panel-body">
				 <table class="table table-bordered" style="width: 100%">
				  	<tr>
			 			<td align=center>
				 			<input type="hidden" id="deptid"  >
				 			<input type="text"	class="form-control " style="width:70%;" id="deptname" placeholder="请选择机构" >
			 			</td>
	             		<td align=center>
	             			<input type="hidden" id="confid"   >
	             			<input type="hidden" id="providerid"   >
				 			<input type="text"	class="form-control " style="width:70%;" id="prvautoname"  placeholder="请选择供应商" >
			 			</td>
	             		<td style="padding-right: 1px" align=center>
	             		  <input id="queryautoconfig" type="button" class="btn btn-primary" value="查询">
	             		  <input id="add-payway" type="button" class="btn btn-primary"  onclick="addpayway()" value="新增">
	             		  <input id="resetbutton" type="button" class="btn btn-primary" value="重置">
	             		  <#-- <input id="delabilitybyid" type="button" class="btn btn-primary" value="删除"> -->
	             		</td>
	            	 </tr>
				 </table>
		  	   <table id ="table-zffs">
		  	   </table>
		  	</div>
		   </div>
		  	<div class="panel-footer">
		  	   <#-- <button class="btn btn-primary" id="add-payway" onclick="addpayway()">新增</button> -->
		  	</div>
		</div>
		
	</div>
	
	<!-- 支付方式编辑页面弹窗 -->	
    
	<div class="modal fade" id="edit_zffs_new" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog" role="document" style="width: 90%;">
				<div class="modal-content" id="modal-content">
				</div>
			</div>	
	</div>
	
<!-- 机构树 -->
<div class="modal fade" id="myModal_dept" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" id="modal-content">
				<div class="modal-body" style="overflow: auto; height: 400px;">
					<div id="dept_tree" class="ztree"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>


<div id="showdept" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="gridSystemModalLabel">选择机构</h4>
      </div>
      <div class="modal-body">
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

<div id="showautopic" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="gridSystemModalLabel">选择供应商</h4><br/>
		<div><input class="form-control ztree-search" id = "treeDemosearch" data-bind="treeautoDemo" name="treeDemosearch" placeholder="输入供应商名称进行搜索" /></div>
      </div>
      <div class="modal-body">
        <div class="container-fluid">
          <div class="row">
			<div id="treeautoDemo" class="ztree">正在加载供应商数据......</div>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
    </div>
  </div>
</div>

<div id="showdeptpic" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="gridSystemModalLabel">选择机构</h4><br/>
		<div><input class="form-control ztree-search" id = "treesearch" data-bind="treeDemoDept" name="treesearch" placeholder="输入机构名称进行搜索" /></div>
      </div>
      <div class="modal-body">
        <div class="container-fluid">
          <div class="row">
			<ul id="treeDemoDept" class="ztree"></ul>
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
