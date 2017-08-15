<!DOCTYPE html>
<html lang ="en" class="fuelux" >  
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>渠道协议管理子页面初始化</title>
<script type="text/javascript">
	requirejs([ "zzbconf/channelagreementsub" ]);
</script>
</head>
<body>
<div class="panel panel-default m-bottom-2">
	<div class="panel-heading padding-5-5"><h4 class="titlelabel">基本信息</h4></div>
	<div class="panel-body pbodynopadding">
	    <div class="form-group col-md-6 form-inline">
			<label for="channelname">渠道名称:</label>
			<input type="text" class="form-control" id="channelname" name="channelname" value="" disabled/>
		</div>
		<div class="form-group col-md-6 form-inline">
			<label for="channelrelativedept">关联机构:</label>
			<input type="text" class="form-control" id="channelrelativedept" name="channelrelativedept" value="" disabled/>
		</div>
	</div>
	<div class="panel-heading padding-5-5"><h4 class="titlelabel">渠道协议详情</h4></div>
	<div class="panel-body">
	     <ul id="myTab" class="nav nav-pills nav-justified" role="tablist">
	     	<li class="active"><a href="#accountmode" role="tab" data-toggle="pill">结算方式</a></li>
			<li><a href="#providerrange" role="tab" data-toggle="pill">供应商范围</a></li>
			<li><a href="#deptrange" role="tab" data-toggle="pill">出单网点</a></li>
			<!--<li><a href="#deliverymode" role="tab" data-toggle="pill">配送方式</a></li>-->
		 </ul>
		 <div id="myTabContent" class="tab-content">
		 	<div class="tab-pane fade in active" id="accountmode">
		 		<form id="accountmodeform">
			 		<div class="panel panel-default m-bottom-2 panelnobottom">
						<div class="panel-heading padding-5-5">结算配置</div>
						<div class="panel-body pbodynopadding">
							<table class="table table-striped">
								<!--<tr>
									<td>
										<label class="checkbox-inline">
										  <input type="checkbox" id="" value="option1"> 车型库接口
										</label>
									</td>
									<td>
										<label class="radio-inline">
											<input type="radio" name="inlineRadioOptions" id="" value="option1"> 免费
										</label>
										<label class="radio-inline">
											<input type="radio" name="inlineRadioOptions" id="" value="option2"> 收费
										</label>
									</td>
									<td>
										每月<input type="text" class="shortinput" id="" name="" value=""/>次免费，超出收取
										<input type="text" class="shortinput" id="" name="" value=""/>元/次
									</td>
								</tr>
								<tr>
									<td>
										<label class="checkbox-inline">
										  <input type="checkbox" id="" value="option1"> 修改投保信息接口
										</label>
									</td>
									<td>
										<label class="radio-inline">
											<input type="radio" name="inlineRadioOptions" id="" value="option1"> 免费
										</label>
										<label class="radio-inline">
											<input type="radio" name="inlineRadioOptions" id="" value="option2"> 收费
										</label>
									</td>
									<td>
										每月<input type="text" class="shortinput" id="" name="" value=""/>次免费，超出收取
										<input type="text" class="shortinput" id="" name="" value=""/>元/次
									</td>
								</tr>-->
							</table>
						</div>
					</div>
			 		<div class="panel panel-default m-bottom-2 panelnobottom">
						<div class="panel-heading padding-5-5">结算周期</div>
						<div class="panel-body pbodynopadding">
							<div class="form-group form-inline">
								<label for="channelrelativedept">T+</label>
								<input type="text" class="form-control" id="accountperiod" name="" value=""/>
							</div>
						</div>
					</div>
			 		<div class="panel panel-default m-bottom-2 panelnobottom">
						<div class="panel-heading padding-5-5">结算方式</div>
						<div class="panel-body pbodynopadding">
							<div class="row">
								<div class="col-md-12">
									<label class="col-md-3">
										<input type="radio" name="inlineRadioOptions1" id="bankaccount" value="option1"> 银行转账
									</label>
									<label class="col-md-3">
										<input type="radio" name="inlineRadioOptions1" id="otheraccount" value="option2"> 其他方式
									</label>
									<div class="form-group col-md-6 form-inline">
										<label for="balanceaccount">结算账户:</label>
										<input type="text" class="form-control" id="balanceaccount" name="" value=""/>
									</div>
								</div>
							</div>
						</div>
					</div>
			 		<div class="panel panel-default m-bottom-2 panelnobottom">
						<div class="panel-heading padding-5-5">产品接入方式</div>
						<div class="panel-body">
							<label class="checkbox-inline col-md-3">
							  <input type="checkbox" id="weixin" value="option1"> 微信公众号
							</label>
							<label class="checkbox-inline col-md-3">
							  <input type="checkbox" id="mobileapp" value="option2"> App
							</label>
							<label class="checkbox-inline col-md-3">
							  <input type="checkbox" id="webpage" value="option3"> 网页
							</label>
						</div>
					</div>
					<button type="button" class="btn btn-primary" id="accountmodeformbtn">保存</button>
				</form>
		 	</div>
		 	<div class="tab-pane fade" id="providerrange">
		 		<form id="providerrangeform">
			 		<div class="panel panel-default m-bottom-2 panelnobottom">
						<div class="panel-heading padding-5-5">供应商选择</div>
						<div class="panel-body pbodynopadding">
							<table class="table table-striped">
								<!--<tr>
									<td>
										<input type="checkbox" id="" value="option3">
									</td>
									<td>
										中环联合财产保险公司
									</td>
								</tr>
								<tr>
									<td>
										<input type="checkbox" id="" value="option3">
									</td>
									<td>
										太平洋财产保险公司
									</td>
								</tr>
								<tr>
									<td>
										<input type="checkbox" id="" value="option3">
									</td>
									<td>
										太保财产保险公司
									</td>
								</tr>-->
							</table>
						</div>
					</div>
					<button type="button" class="btn btn-primary" id="providerrangeformbtn">保存</button>
				</form>
		 	</div>
		 	<div class="tab-pane fade" id="deptrange">
		 		<form id="deptrangeform">
			 		<div class="panel panel-default m-bottom-2 panelnobottom">
						<div class="panel-heading padding-5-5">选择网点</div>
						<div class="panel-body pbodynopadding">
							<div class="row">
								<div class="col-md-12">
									<div class="form-group col-md-4 form-inline">
										<select name="" id="deptone" class="form-control">
											<option value="">请选择</option>
										</select>
									</div>
									<div class="form-group col-md-4 form-inline">
										<select name="" id="depttwo" class="form-control"></select>
									</div>
									<div class="form-group col-md-4 form-inline">
										<select name="" id="deptthree" class="form-control"></select>
									</div>
									<div class="form-group col-md-4 form-inline">
										<select name="" id="deptfour" class="form-control"></select>
									</div>
									<div class="form-group col-md-4 form-inline">
										<select name="" id="deptfive" class="form-control"></select>
									</div>
									<button type="button" class="btn btn-primary" id="deptrelative">添加关联</button>
								</div>
							</div>
						</div>
					</div>
			 		<div class="panel panel-default m-bottom-2 panelnobottom">
						<div class="panel-heading padding-5-5">已关联网点</div>
						<div class="panel-body pbodynopadding">
							<table class="table table-striped" id="deptListTable"></table>
							<button type="button" class="btn btn-primary" id="disdeptrelative">取消关联</button>
						</div>
					</div>
				</form>
		 	</div>
		 	<!--<div class="tab-pane fade" id="deliverymode">配送方式</div>-->
		 </div>
	</div>
</div>
</body>
</html>
