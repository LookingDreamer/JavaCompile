<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>掌中保-订单列表-业管版</title>
<link rel="stylesheet"
	href="${staticRoot}/css/modelinsurance/bootstrap.min.css">
<link rel="stylesheet"
	href="${staticRoot}/css/modelinsurance/zzb_yg.css">
<link rel="stylesheet" href="${staticRoot}/css/modelinsurance/date.css">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "cm/valetcatalogue/orderList"]);
</script>
</head>
<body>
	<div class="main-container mar11">
		<!--
		<div class="panel panel-default">
			<div id="headingAgent" role="tab" class="panel-heading">
				<h4 class="panel-title f6">代理人信息</h4>
			</div>
			<div aria-labelledby="headingAgent" role="tabpanel"
				class="panel-collapse collapse in" id="collapseAgent">
				<div class="panel-body">
					<div class="row">
						<div class="col-md-12">
							<table class="table table-bordered f2">
								<tbody>
									<tr>
										<td class="col-md-2 active">代理人姓名</td>
										<td class="col-md-2  c1 f2">620005685（吴昂）</td>
										<td class="col-md-2 active">性别</td>
										<td class="col-md-2"></td>
										<td class="col-md-2 active">工号</td>
										<td class="col-md-2"></td>
									</tr>
									<tr>
										<td class="active">资格证号码</td>
										<td></td>
										<td class="active">身份证号</td>
										<td></td>
										<td class="active">联系电话</td>
										<td></td>
									</tr>
									<tr>
										<td class="active">所属机构</td>
										<td colspan="5"></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		-->
		
		<div class="panel panel-default m-bottom-5">
			<div class="panel-heading padding-5-5">查询</div>
			<div class="panel-body">
		    	<div class="row">
					<form role="form" id="orderselect">
						<div class="form-group form-inline col-md-6">
							<label for="exampleInputCode">订单状态</label>
							<select class="form-control m-left-5" id="orderstatus" name="orderstatus">
								<option value="0" selected="selected">全部</option>
								<option value="1">待投保</option>
								<option value="2">待支付</option>
							</select>
						</div>
						<div class="form-group form-inline col-md-6">
							<label for="exampleInputCode">时&nbsp;&nbsp;&nbsp;&nbsp;间&nbsp;&nbsp;&nbsp;段</label> <input type="date"
								class="form-control m-left-5" id="fromtime" name="fromtime" placeholder="">至
								<input type="date"
								class="form-control m-left-5" id="totime" name="totime" placeholder="">
						</div>
						<div class="form-group form-inline col-md-6">
							<label for="exampleInputCode">车&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;牌</label> <input type="text"
								class="form-control m-left-5" id="platenumber" name="platenumber" placeholder="">
						</div>
						<div class="form-group form-inline col-md-6">
							<label for="exampleInputCode">被保人姓名</label> <input type="text"
								class="form-control m-left-5" id="name" name="name" placeholder="">
						</div>
					</form>
					<button id="querybutton" type="button" name="querybutton"
													class="btn btn-primary">查询</button>
				</div>
			</div>
		</div>
			 
		<div class="panel panel-default m-bottom-2 ">
			<div class="panel-heading padding-5-5 f6 clearfix">
				<span class="f-left mar1">订单列表</span>
			</div>
			<div class="clearfix">
				<div class="col-md-7 pad0">
					<ul class="nav nav-tabs f2 mar4 pad2" id="mytab">
						<li><a data-toggle="tab" href="#PolicyNum">待投保</a></li>
						<li><a data-toggle="tab" href="#PaymentOrderNum">待支付</a></li>
						<li class="active"><a data-toggle="tab" href="#all">全部</a></li>
					</ul>
				</div>

			</div>
			<div
				style="width: 100%; height: 500px; overflow-y: auto; overflow-x: auto;"
				class="panel-body">
				<div class="tab-content">
					<div  style="margin-bottom: 20px" class="tab-pane fade "
						id="PolicyNum">
						<table id="table-orderlist1"></table>
					</div>
					<div style="margin-bottom: 20px" class="tab-pane fade "
						id="PaymentOrderNum">
						<table id="table-orderlist2"></table>
					</div>
					<div style="margin-bottom: 20px" class="tab-pane fade in active " id="all">
						<table id="table-orderlist0"></table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
