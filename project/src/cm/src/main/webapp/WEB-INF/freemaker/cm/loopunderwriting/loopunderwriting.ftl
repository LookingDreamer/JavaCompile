<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>核保轮询任务查询</title>
	<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>
	<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
	<script type="text/javascript">
			requirejs([ "cm/loopunderwriting/loopunderwriting" ]);
	</script>
	<style type="text/css">
		div#selectStype a {
			position:relative;
			left:15px;
			top:10px;
		}
	</style>
</head>
<body>
	<div class="container-fluid" style="margin-bottom:30px">
		<div class="panel panel-default m-bottom-5" id="superquerypanel">
		  <div class="panel-heading padding-5-5">
		  	<label>筛选任务</label>
		  </div>
		  	<div class="panel-body">
				<form role="form" id="cartasksuperqueryform">
					<div class="row">
						<div class="col-md-12">
                            <div class="form-group col-md-4 form-inline">
                                <label for="exampleInputInsuredname">被保险人:</label>
                                <input type="text" class="form-control m-left-2" id="insuredname" name="insuredname" placeholder=""/>
                            </div>
                            <div class="form-group col-md-4 form-inline">
                                <label for="exampleInputCarlicenseno">车牌号:</label>
								<input type="text" class="form-control m-left-2" id="carlicenseno" name="carlicenseno" placeholder=""/>
                            </div>
							<div class="form-group col-md-4 form-inline">
								<label for="exampleInputmainInstanceId">任务号:</label>
								<input type="text" class="form-control" id="mainInstanceId" name="mainInstanceId" placeholder=""/>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group col-md-12 form-inline">
								<label for="exampleInputTaskcreatetime">任务创建时间:</label>
									<input class="form-control form_datetime" style="color:black;" type="text" id="taskcreatetimeup" name="taskcreatetimeup" readonly value=""/>
								<label for="exampleInputTaskcreatetime">至</label>
									<input class="form-control form_datetime" style="color:black;" type="text" id="taskcreatetimedown" name="taskcreatetimedown" readonly value=""/>
							</div>
						</div>
					</div>
				</form>
	  		</div>
		  <div class="panel-footer padding-5-5">
		  	<div class="row">
				<div class="col-md-12">
		  			<div class="col-md-12" align="right">
						<button id="superquerybutton" type="button" name="querybutton"
											class="btn btn-primary">查询</button>
						<button id="resetbutton" type="button" name="resetbutton"
											class="btn btn-primary">重置</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="panel panel-default">
		  <div class="panel-heading padding-5-5">
			  <div class="row">
				 <div class="col-md-2">
				 	<label>任务列表</label>
				 </div>
			  </div>
		  </div>
	  	  <div class="row">
			  <div class="col-md-12">
				  <table id="table-javascript"></table>
			  </div>
	  	  </div>
		</div>
	</div>
</body>
</html>
