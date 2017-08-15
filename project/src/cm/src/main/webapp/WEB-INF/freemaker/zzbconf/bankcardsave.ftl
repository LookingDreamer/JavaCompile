<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>银行卡管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/system/flat-ui-icon.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/bankcard" ]);
</script>
<body>
	<div class="container-fluid">
		<div class="panel panel-default m-bottom-5">
		  <div class="panel-heading padding-5-5">新增银行卡信息</div>
		   <div class="row">
		  	<div class="panel-body">
	  			<form class="form-inline" role="form" id="bankcardsaveform" action="saveorupdate" method="post">
		  		<div class="alert alert-danger alert-dismissible" id="bankcarderror"
				         role="alert" style="display: none;"></div>
				 <table class="table table-bordered">
					  <tr>
						<td class="col-md-2 active"><label for="exampleInputName">卡号前缀</label></td>
						<td><input class="form-control" type="text" id="cardprefix" name="cardprefix"></td>
					  </tr>
					  <tr>
						<td class="col-md-2 active"><label for="exampleInputName">卡名</label></td>
						<td ><input class="form-control" type="text" id="cardname" name="cardname"></td>
					  </tr>
					  <tr>
						<td class="col-md-2 active"><label for="exampleInputName">发卡行名称</label></td>
						<td><input class="form-control" type="text" id="banktoname" name="banktoname"></td>
					  </tr>
					  <tr>
						<td class="col-md-2 active"><label for="exampleInputName">卡种</label></td>
						<td>
							<select name="cardtype" class="form-control">
								<option selected="selected" value="">请选择</option>
								<option  value="01">贷记卡</option>
								<option  value="02">借记卡</option>
								<option  value="03">预付卡</option>
								<option  value="04">准货记卡</option>
								<option  value="05">其它</option>
							</select>
						</td>
					  </tr>
					  <tr>
						<td class="col-md-2 active"><label for="exampleInputName">卡号长度</label></td>
						<td><input class="form-control" type="text" id="cardnumlength" name="cardnumlength"></td>
					  </tr>
					  <tr>
						<td class="col-md-2 active"><label for="exampleInputName">发卡行机构编码</label></td>
						<td ><input class="form-control" type="text" id="cardbankdeptcode" name="cardbankdeptcode"></td>
					  </tr>
					  <tr>
			      		<td colspan="3">
			      		  <input class="btn btn-primary" id="bankcardsavebutton" type="submit" value="保存">
			      		  <input class="btn btn-primary" id="backbutton" type="button" value="返回"></td>
			     	  </tr>
				  </table>
				</form>
			</div>
		  </div>
		</div>
	</div>
</body>
</html>
