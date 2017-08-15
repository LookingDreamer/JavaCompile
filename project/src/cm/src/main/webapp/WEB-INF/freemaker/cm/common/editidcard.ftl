<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改被保人信息</title>
<link href="${staticRoot}/css/lib/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet"/>
<link href="${staticRoot}/css/lib/fuelux.min.css" rel="stylesheet"/>
<link href="${staticRoot}/css/core/module.css" rel="stylesheet"/>
<link href="${staticRoot}/css/appreset.css" rel="stylesheet"/>
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}">
</script>
<style type="text/css">
body {font-size: 14px;}
.inputshort {
	width: 200px;
	border:1px solid #bdc3c7;
}
.inputlong {
	width: 350px;
	border:1px solid #bdc3c7;
}
.idcardBtn {
	width: 90px;
	background: rgb(202, 207, 210);
	cursor: pointer;
	border-radius: 5px;
	margin-right: 25px;
}
.datetimewidth{
	width: 50%;
}

span.feildtitle {
	font-weight:bold;
	color:#34495E;
}
</style>
</head>
<body>
<script type="text/javascript" >
requirejs([ "cm/common/editidcard" ],function() {
	require(["jquery", "bootstrapdatetimepicker", "zTree", "zTreecheck", "flat-ui", "bootstrap-table", "bootstrap",
			"bootstrapTableZhCn", "bootstrapdatetimepickeri18n","public"], function ($) {
		$(function() {
			initEditOtherInfoScript();
		});
	});
});

function IDCardValid(code) { 
	var city={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
	var tip = "";
	var pass= true;
	if(!city[code.substr(0,2)]){
		tip = "身份证号前两位地址编码错误!";
		pass = false;
	} else if(!code || !/^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i.test(code)){
		tip = "身份证号格式错误,请重新输入!";
		pass = false;
	}
	if(!pass) alertmsg(tip);
	return pass;
}
</script>
<input id="taskid" type="hidden" value="${taskid}"/>	
<input id="inscomcode" type="hidden" value="${inscomcode}"/>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<h6>支付用补充信息</h6>
	</div>
	<div class="modal-body">
	<form id="editidcardform">
			<table class="table table-bordered">
				<tr>
					<td class="col-md-4 active">姓名：</td>
					<td>
						<input id="bbrname" class="form-control inputshort" type="text" value="${name}"/>
					</td>
				</tr>
				<tr>
					<td class="col-md-4 active">身份证号码:</td>
					<td>
						<input id="bbridcardno" class="form-control inputshort" type="text" value="${idcardno}"/>
					</td>
				</tr>
				<tr>
					<td class="col-md-4 active">性别:</td>
					<td>
						<input id="bbrsex" name="sex" type="radio" value="0" <#if sex !=1>checked</#if>/>男
						&nbsp;&nbsp;
						<input id="bbrsex" name="sex" type="radio" value="1" <#if sex ==1>checked</#if>/>女
					</td>
				</tr>
				<tr>
					<td class="col-md-4 active">民族:</td>
					<td>
						<input id="bbrnation" class="form-control inputshort" type="text" value="${nation}"/>
					</td>
				</tr>
				<tr>
					<td class="col-md-4 active">出生日期:</td>
					<td>
						<div class="form-group col-md-12">
							<div class="input-group date form_datetime col-md-12 datetimewidth" data-date-format="yyyy-mm-dd">
								<input class="form-control" style="color:black;" type="text" id="bbrbirthday" readonly value="${birthday}"/>
								<span class="input-group-addon"> 
									<span class="glyphicon glyphicon-remove"></span>
								</span> 
								<span class="input-group-addon"> 
									<span class="glyphicon glyphicon-th"></span>
								</span>
							</div>
						</div>
					</td>
				</tr>
                <tr>
                    <td class="col-md-4 active">有效期限开始:</td>
                    <td>
                        <div class="form-group col-md-12">
                            <div class="input-group date form_datetime col-md-12 datetimewidth" data-date-format="yyyy-mm-dd">
                                <input class="form-control" style="color:black;" type="text" id="bbrexpstartdate" readonly value="${expstartdate}"/>
								<span class="input-group-addon">
									<span class="glyphicon glyphicon-remove"></span>
								</span>
								<span class="input-group-addon">
									<span class="glyphicon glyphicon-th"></span>
								</span>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="col-md-4 active">有效期限结束:</td>
                    <td>
                        <div class="form-group col-md-12">
                            <div class="input-group date form_datetime col-md-12 datetimewidth" data-date-format="yyyy-mm-dd">
                                <input class="form-control" style="color:black;" type="text" id="bbrexpenddate" readonly value="${expenddate}"/>
								<span class="input-group-addon">
									<span class="glyphicon glyphicon-remove"></span>
								</span>
								<span class="input-group-addon">
									<span class="glyphicon glyphicon-th"></span>
								</span>
                            </div>
                        </div>
                    </td>
                </tr>
				<tr>
					<td class="col-md-4 active">身份证有效期:</td>
					<td>
						<input class="form-control inputlong" style="color:#000000" type="text" id="bbrexpdate" readonly value="${expdate}"/>
					</td>
				</tr>
				<tr>
					<td class="col-md-4 active">手机号:</td>
					<td>
						<input id="bbrcellphone" class="form-control inputshort" type="text" value="${cellphone}"/>
					</td>
				</tr>
                <tr>
                    <td class="col-md-4 active">邮箱:</td>
                    <td>
                        <input id="bbremail" class="form-control inputshort" type="text" value="${email}"/>
                    </td>
                </tr>
				<tr>
					<td class="col-md-4 active">签发机关:</td>
					<td>
						<input id="bbrauthority" class="form-control inputlong" type="text" value="${authority}"/>
					</td>
				</tr>
				<tr>
					<td class="col-md-4 active">住址:</td>
					<td>
						<input id="bbraddress" class="form-control inputlong" type="text" value="${address}"/>
					</td>
				</tr>
			</table>
			</form>
	</div>
	<div class="modal-footer" style="text-align: right;">
		<button class="btn btn-default idcardBtn" type="button" name="submitidcardinfo"
						id="submitidcardinfo" title="确定">&nbsp;&nbsp;确定&nbsp;&nbsp;</button>
		<button class="btn btn-default idcardBtn" type="button" name="cancel"
						id="cancel" title="取消" data-dismiss="modal">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>
	</div>
</body>
</html>