<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>掌中保-快速续保-业管版</title>
<link rel="stylesheet"
	href="${staticRoot}/css/modelinsurance/bootstrap.min.css">
<link rel="stylesheet"
	href="${staticRoot}/css/modelinsurance/zzb_yg.css">
<link rel="stylesheet" href="${staticRoot}/css/modelinsurance/date.css">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "cm/modelinsurance/rapidrenewal" ]);
</script>
<script src="${staticRoot}/js/modelinsurance/date.js"></script>
<script src="${staticRoot}/js/modelinsurance/iscroll.js"></script>
</head>
<body>
	<div class="main-container mar11">
		<div class="panel panel-default">
			<div id="headingAgent" role="tab" class="panel-heading">
				<h4 class="panel-title f6">代理人信息</h4>
			</div>
			<div aria-labelledby="headingAgent" role="tabpanel"
				class="panel-collapse collapse in" id="collapseAgent">
				<div class="panel-body">
					<div class="row">
						<table id="tabletest"></table>
					</div>
					<div class="row">
						<div class="col-md-12">
							<table class="table table-bordered f2">
								<tbody>
									<tr>
										<input type="text" name="agentid" style="display: none;"
											id="agentid" value="${INSBAgent.id}">
										<input type="text" name="agentname" style="display: none;"
											id="agentname" value="${INSBAgent.name}">
										<td class="col-md-2 active">代理人姓名</td>
										<td class="col-md-2  c1 f2">${INSBAgent.name}</td>
										<td class="col-md-2 active">性别</td>
										<td class="col-md-2">${INSBAgent.sex}</td>
										<td class="col-md-2 active">工号</td>
										<td class="col-md-2">${INSBAgent.jobnum}</td>
									</tr>
									<tr>
										<td class="active">执业证/展示证号码</td>
										<td>${INSBAgent.licenseno}</td>
										<td class="active">身份证号</td>
										<td>${INSBAgent.idno}</td>
										<td class="active">联系电话</td>
										<td>${INSBAgent.mobile}</td>
									</tr>
									<tr>
										<td class="active">所属机构</td>
										<td colspan="1">>${INSBAgent.comname}以上数据为空的原因是数据库没有数据而已</td>
										<td class="active">渠道来源</td>
										<td colspan="3">${INSBAgent.purchaserchannel}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!---->
		<div class="panel panel-default m-bottom-2 ">
			<div class="panel-heading padding-5-5 f6 clearfix">
				<span class="f-left mar1">详投保单的信息</span>
			</div>
			<ul class="nav nav-pills nav-justified" id="mytab">
				<li role="presentation" class="active" id="baseInfo" style="width:279px;height:40px"><a
					href="#" ><div id="baseInfovv">基本信息</div></a></li>
				<li role="presentation" id="insureConfig" style="width:279px;height:40px"><a href="#"
					><div id="insureConfigvv">保险配置</div></a></li>
			</ul>
			<form enctype="multipart/form-data" method="post"
				action="saveorupdatepro" id="prosaveupdateform">
				<div
					style="width: 100%; height: 525px; overflow-y: auto; overflow-x: auto;"
					class="panel-body">
					<div class="tab-content">
						<div style="margin-bottom: 20px" id="baseInfodiv">
							<div class="row">
								<div class="col-md-12">
									<table class="table table-bordered ">
										<tbody>
											<tr>
												<td><label for="exampleInputName">保险公司</label></td>
												<td>
													<select id="insurecom" class="form-control w1" name="insurecom">
														<option >--请选择--</option>
														<#list providers as p>
														<option value="${p.prvcode }">${p.prvshotname }</option>
														</#list>
													</select>
												</td>
											</tr>
											<tr>
												<td><label for="exampleInputName">投保地区</label></td>
												<td><input type="text" name="provinceName" class="form-control w1 "
													placeholder="" readonly id="provinceName"
													value="${quotearea.provinceName} ${quotearea.cityName}"> <input
													type="text" name="provinceCode" placeholder=""
													style="display: none;" id="provinceCode"
													value="${quotearea.provinceCode}">  <input type="text"
													name="cityCode" placeholder="" style="display: none;"
													id="cityCode" value="${quotearea.cityCode}"></td>
											</tr>
											<tr>
												<td><label for="exampleInputName">车牌</label></td>
												<td><input type="text" placeholder="" name="carNumber"
													id="carNumber" value="" class="form-control w1 "><label
													class="radio-inline"><input id="registration" type="checkbox">未上牌</label></td>
												<!--<td><input type="text"  placeholder="" name="prvname" id="prvname1" value="" class="form-control w1 "><label class="radio-inline"><input type="checkbox">未上牌</label><span class="btn btn-primary mar12"  id="selectstyle">查询</span></td>-->
											</tr>
											<tr>
												<td><label for="exampleInputName">车主</label></td>
												<td><input type="text" placeholder=""
													name="carowner_name" id="carowner_name" value=""
													class="form-control w1"></td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<button class="btn btn-primary" id="comeback"
								type="button" name="comeback">返回</button>
							<button class="btn btn-primary" id="baseInfobutton_next"
								type="button" name="baseInfobutton_next">下一步</button>
						</div>
						<!---->
						<div style="margin-bottom: 20px" id="insureConfigdiv">
							<div class="row">
								<div class="col-md-12">
									<table id="table-javascript"></table>
									<table class="table zzb-table  f2 ">
										<colgroup>
											<col class="col-xs-2">
											<col class="col-xs-2">
											<col class="col-xs-2">
											<col class="col-xs-2">
											<col class="col-xs-2">
											<col class="col-xs-2">
										</colgroup>
										<tr>
											<td style="text-align: center; vertical-align: middle;">保险配置类型</td>
											<td style="text-align: center; vertical-align: middle;">
												<select class="form-control" onchange="configtype()"
												id="configtype_id"></select>
											</td>
										</tr>
									</table>
									<table class="table zzb-table  f2 " id="insuredconfrtable">
										<tr class="bg4">
											<th style="text-align: center; vertical-align: middle;">险种</th>
											<th style="text-align: center; vertical-align: middle;">保额</th>
											<th style="text-align: center; vertical-align: middle;">不计免赔</th>
										</tr>
									</table>
								</div>
							</div>
							<button id="insureConfigbutton_last" type="button"
								name="insureConfigbutton_last"
								onclick="insureConfigshowdiv('last')">上一步</button>
							<button class="btn btn-primary " id="insureConfigbutton_next"
								type="button" name="insureConfigbutton_next"
								onclick="insureConfigshowdiv('next')">提交核保</button>
						</div>
						<!---->
					</div>
				</div>

			</form>
		</div>
	</div>
	
	<div class="modal fade" id="mymodal1">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<h4 class="modal-title">提示：</h4>
				</div>
				<div class="modal-body">
					<p id="content"></p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal" id="callbackmodify">返回修改</button>
					<button type="button" class="btn btn-primary" id="confirmpid">确认</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div>
		
	<div class="modal fade" id="addequipment">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">新增设备</h4>
	      </div>
	      <div class="modal-body">
			<table class="table table-bordered" id="equipmentmsg">
				<tr><td>设备名称</td><td>金额</td></tr>
				<tr><td><input class="form-control w1" type="text"
						id="equipmentname" name="equipmentname"></td><td><input class="form-control w1" type="text"
						id="equipmentprice" name="equipmentprice"></td></tr>
			</table>
				<button type="button" class="btn btn-primary" onclick=addmsgtotable();>新增</button>
			<table class="table table-bordered" id="allequipment">
				<tr><td>设备名称</td><td>金额</td><td>操作</td></tr>
			</table>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
		
	<div id="datePlugin"></div>

</body>
</html>
