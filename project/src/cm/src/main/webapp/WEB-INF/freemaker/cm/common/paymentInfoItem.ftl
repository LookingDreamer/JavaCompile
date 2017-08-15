<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改支付信息</title>
<!--<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>-->
<style type="text/css">
body {font-size: 14px;}
</style>
<script type="text/javascript">
	require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public"], function ($) {
		$(function(){
			
		});
	});
//	function dataBack(){
//
//				var win = null;
//				if($("#desktop:not(:hidden)").length>0){
//					win = $(window.desktop_content.document);
//				}else{
//					win = $(window.frames[$("iframe[id^=fra_businessmytaskqueryTask]:not(:hidden)").attr("id")].document);
//				}
//				win.find("#ad_paymentmethod").text($("#paymentmethod").find("option:selected").text());
//				win.find("#ad_tradeno").text($("#tradeno").val());
//				win.find("#ad_insurecono").text($("#insurecono").val());
//				//win.find("#ad_checkcode").text($("#checkcode").val());
//			}
    function dataBack(){
        if($("#desktop:not(:hidden)").length>0){
            var win = $(window.desktop_content);
            if (win && win.length > 0) {
                win = $(window.desktop_content.document);
                setLable(win);
            }

        } else if ($("#menu:not(:hidden)").length>0){
            win = $(window.frames[$("iframe[id^=fra_businessordermanageordermanagelist]:not(:hidden)").attr("id")]);
            if (win && win.length > 0) {
                win = $(window.frames["fra_businessordermanageordermanagelist"].document);
                setLable(win);
            }
            win = $(window.frames[$("iframe[id^=fra_businessmytaskqueryTask]:not(:hidden)").attr("id")]);//mei
            if (win && win.length > 0) {
                win = $(window.frames[$("iframe[id^=fra_businessmytaskqueryTask]:not(:hidden)").attr("id")].document);
                setLable(win);
            }
        }


    }

    function setLable(win) {
        if(win.find("#ad_paymentmethod").length > 0) {
            win.find("#ad_paymentmethod").text($("#paymentmethod").find("option:selected").text());
            win.find("#ad_tradeno").text($("#tradeno").val());
            win.find("#ad_insurecono").text($("#insurecono").val());
        }
    }
			function editpayinfo(){
				$.ajax({
						url : 'business/ordermanage/updatepaymentInfo',
						type : 'post',
						data : {
						"paymentmethod":$("#paymentmethod").val(),
						"insurecono":$("#insurecono").val(),
						"tradeno":$("#tradeno").val(),
						"checkcode":1,
						"taskid":$("#taskid").val(),
						"inscomcode":$("#inscomcode").val(),
						"subInstanceId":$("#subInstanceId").val()
						},
						dataType : "html",
						async : true,
						error : function() {
							alertmsg("Connection error");
						},
			            success : function(data) {
			            	if(data=="success"){
			            		dataBack();
			            	}else if(data=="fail"){
			            		alertmsg("修改失败");
			            	}
						}
					});
			}
</script>

</head>
<body>
<input id="taskid" type="hidden" value="${taskid}"/>	
<input id="inscomcode" type="hidden" value="${inscomcode}"/>
<input id="subInstanceId" type="hidden" value="${subInstanceId}"/>

	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<h6 class="modal-title">支付信息</h6>
	</div>
	<div class="modal-body">
		<form class="form-inline" role="form" >
			<table class="table table-bordered">
				<tr>
					<td class="col-md-2 active">支付方式：</td>
					<td>
						<select class="form-control " name="usertype" id="paymentmethod">
						<#list channelIdMapList as map>
						<#list map?keys as itemKey>
								<option value="${itemKey}" <#if itemKey == paymentinfo.paymentmethodid>selected</#if>>${map[itemKey]}
								</option>
						</#list>
						</#list>	
						</select>
						
					</td>
				</tr>
				<tr>
					<td class="col-md-2 active">支付跟踪号:</td>
					<td>
						<input id="insurecono" type="text" value="${paymentinfo.insurecono}"/>
					</td>
				</tr>
				<tr>
					<td class="col-md-2 active">交易跟踪号:</td>
					<td>
						<label><input id="tradeno" type="text" value="${paymentinfo.tradeno}"/></label>
					</td>
				</tr>
				<!--
				<tr>
					<td class="col-md-2 active">校验码:</td>
					<td>
						 <label><input id="checkcode" type="text" value="${paymentinfo.checkcode}"/></label>
					</td>
				</tr>
				 -->
				
			</table>
			<div class="row">
				<div class="col-md-6">
					<button class="btn btn-default" type="button" name="makesure" onclick="editpayinfo();"
						id="makesure" title="确定" data-dismiss="modal">&nbsp;&nbsp;确定&nbsp;&nbsp;</button>
				</div>
				<div class="col-md-6" align="right">
					<button class="btn btn-default" type="button" name="cancel"
						id="cancel" title="取消" data-dismiss="modal">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>
				</div>
			</div>
		</form>
	</div>
</body>
</html>