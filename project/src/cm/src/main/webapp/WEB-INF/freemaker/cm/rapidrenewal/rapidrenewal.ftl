<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>掌中保-车险投保-业管版</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "cm/rapidrenewal/rapidrenewal" ]);
</script>
</head>

<body>
  <nav class="zzb-yg-nav">
  	<a>我的任务（6）</a>
  	<a class="active">订单管理</a>
  	<a>车险任务管理</a>
  	<a>代客出单</a>
  	<a>运营数据</a>
  </nav>
  <div class="main-container mar11">
  	<!---->
  	<div class="panel panel-default">
		    <div id="headingAgent" role="tab" class="panel-heading">
		      <h4 class="panel-title f6">
		         代理人信息
		      </h4>
		    </div>
		    <div aria-labelledby="headingAgent" role="tabpanel" class="panel-collapse collapse in" id="collapseAgent">
		      <div class="panel-body">
		        <div class="row">
					<div class="col-md-12">
						<table class="table table-bordered f2">
								<tbody><tr>
									<td class="col-md-2 active">代理人姓名</td>
									<td class="col-md-2  c1 f2"></td>
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
			 			</tbody></table>
					</div>
				</div>
		      </div>
		    </div>
		  </div>
  	<!---->
  	<div class="panel panel-default m-bottom-2 ">
	  <div class="panel-heading padding-5-5 f6 clearfix"><span class="f-left mar1">详投保单的信息</span>	
	  </div>
	  
	   <form enctype="multipart/form-data" method="post" action="saveorupdatepro" id="prosaveupdateform">
	   <div style="width:100%;" class="panel-body">
	   
		<span class="label label-info">车辆信息</span>
	    <div   class="mar4"  id="insureCom" >
	    <div class="row">
		     <div class="col-md-12">
				 <table class="table table-bordered ">
					<tbody><tr>
						<td><label for="exampleInputName">投保地区</label></td>
						<td><select onchange="changeprv()" id="province" class="form-control" name="province"><option value="" selected="selected">市</option></select></td>
						<td><label for="exampleInputName">车牌</label></td>
						<td><input type="text"  placeholder="" name="prvname" id="prvname1" value="" class="form-control w1 "><label class="radio-inline"><input type="checkbox">未上牌</label><span class="btn btn-primary mar12">查询</span></td>
					</tr>
					<tr>
						<td><label for="exampleInputName">车主</label></td>
						<td><input type="text"  placeholder="" name="prvshotname" id="prvshotname" value="" class="form-control "></td>
						
					</tr>
					
				
				  </tbody></table>
		    </div>
		</div>			
	    </div>
	    <!---->
	    <span class="label label-info">保险公司</span>
	    <div   class="mar4"  id="insureCom" >
	    <div class="row">
		     <div class="col-md-12">
		         <table class="table table-hover table-striped f2 border" >
		         	<colgroup>
		         		<col class="col-xs-1">
		         		<col class="col-xs-3">
		         		<col class="col-xs-3">
		         		<col class="col-xs-3">
		         		<col class="col-xs-2">
		         	</colgroup>
		         	<tr>
			         	<th style="text-align: center; vertical-align: middle;">类型</th>
			         	<th style="text-align: center; vertical-align: middle;">公司名称</th>
			         	<th style="text-align: center; vertical-align: middle;">分公司</th>
			         	<th style="text-align: center; vertical-align: middle;">出单网店</th>
			         	<th style="text-align: center; vertical-align: middle;">操作</th>
		         	</tr>
		         	<tr>
		         		<td>
		         			<select onchange="changeprv()" id="province" class="form-control" name="province"><option value="110000">北京市</option><option value="120000">天津市</option><option value="130000">河北省</option><option value="140000">山西省</option><option value="150000">内蒙古自治区</option><option value="210000">辽宁省</option><option value="220000">吉林省</option><option value="230000">黑龙江省</option><option value="310000">上海市</option><option value="320000">江苏省</option><option value="330000">浙江省</option><option value="340000">安徽省</option><option value="350000">福建省</option><option value="360000">江西省</option><option value="370000">山东省</option><option value="410000">河南省</option><option value="420000">湖北省</option><option value="430000">湖南省</option><option value="440000">广东省</option><option value="450000">广西壮族自治区</option><option value="460000">海南省</option><option value="500000">重庆市</option><option value="510000">四川省</option><option value="520000">贵州省</option><option value="530000">云南省</option><option value="540000">西藏自治区</option><option value="610000">陕西省</option><option value="620000">甘肃省</option><option value="630000">青海省</option><option value="640000">宁夏回族自治区</option><option value="650000">新疆维吾尔自治区</option><option value="710000">台湾省</option><option value="810000">香港特别行政区</option><option value="820000">澳门特别行政区</option></select>
		         		</td>
		         		<td>
		         			<select onchange="changeprv()" id="province" class="form-control" name="province"><option value="110000">北京市</option><option value="120000">天津市</option><option value="130000">河北省</option><option value="140000">山西省</option><option value="150000">内蒙古自治区</option><option value="210000">辽宁省</option><option value="220000">吉林省</option><option value="230000">黑龙江省</option><option value="310000">上海市</option><option value="320000">江苏省</option><option value="330000">浙江省</option><option value="340000">安徽省</option><option value="350000">福建省</option><option value="360000">江西省</option><option value="370000">山东省</option><option value="410000">河南省</option><option value="420000">湖北省</option><option value="430000">湖南省</option><option value="440000">广东省</option><option value="450000">广西壮族自治区</option><option value="460000">海南省</option><option value="500000">重庆市</option><option value="510000">四川省</option><option value="520000">贵州省</option><option value="530000">云南省</option><option value="540000">西藏自治区</option><option value="610000">陕西省</option><option value="620000">甘肃省</option><option value="630000">青海省</option><option value="640000">宁夏回族自治区</option><option value="650000">新疆维吾尔自治区</option><option value="710000">台湾省</option><option value="810000">香港特别行政区</option><option value="820000">澳门特别行政区</option></select>
		         		</td>
		         		<td>
		         			<select onchange="changeprv()" id="province" class="form-control" name="province"><option value="110000">北京市</option><option value="120000">天津市</option><option value="130000">河北省</option><option value="140000">山西省</option><option value="150000">内蒙古自治区</option><option value="210000">辽宁省</option><option value="220000">吉林省</option><option value="230000">黑龙江省</option><option value="310000">上海市</option><option value="320000">江苏省</option><option value="330000">浙江省</option><option value="340000">安徽省</option><option value="350000">福建省</option><option value="360000">江西省</option><option value="370000">山东省</option><option value="410000">河南省</option><option value="420000">湖北省</option><option value="430000">湖南省</option><option value="440000">广东省</option><option value="450000">广西壮族自治区</option><option value="460000">海南省</option><option value="500000">重庆市</option><option value="510000">四川省</option><option value="520000">贵州省</option><option value="530000">云南省</option><option value="540000">西藏自治区</option><option value="610000">陕西省</option><option value="620000">甘肃省</option><option value="630000">青海省</option><option value="640000">宁夏回族自治区</option><option value="650000">新疆维吾尔自治区</option><option value="710000">台湾省</option><option value="810000">香港特别行政区</option><option value="820000">澳门特别行政区</option></select>
		         		</td>
		         		<td>
		         			<select onchange="changeprv()" id="province" class="form-control" name="province"><option value="110000">北京市</option><option value="120000">天津市</option><option value="130000">河北省</option><option value="140000">山西省</option><option value="150000">内蒙古自治区</option><option value="210000">辽宁省</option><option value="220000">吉林省</option><option value="230000">黑龙江省</option><option value="310000">上海市</option><option value="320000">江苏省</option><option value="330000">浙江省</option><option value="340000">安徽省</option><option value="350000">福建省</option><option value="360000">江西省</option><option value="370000">山东省</option><option value="410000">河南省</option><option value="420000">湖北省</option><option value="430000">湖南省</option><option value="440000">广东省</option><option value="450000">广西壮族自治区</option><option value="460000">海南省</option><option value="500000">重庆市</option><option value="510000">四川省</option><option value="520000">贵州省</option><option value="530000">云南省</option><option value="540000">西藏自治区</option><option value="610000">陕西省</option><option value="620000">甘肃省</option><option value="630000">青海省</option><option value="640000">宁夏回族自治区</option><option value="650000">新疆维吾尔自治区</option><option value="710000">台湾省</option><option value="810000">香港特别行政区</option><option value="820000">澳门特别行政区</option></select>
		         		</td>
		         		<td class="t-center">
		         			<button class="btn btn-primary w1">添加</button>
		         		</td>
		         	</tr>
		         	
		         </table>
		    </div>
		</div>			
	    </div>
	    <!---->
	    <span class="label label-info">保险配置</span>
	    <div class="mar4"   id="insureConfig">
	    <div class="row">
		     <div class="col-md-12">
				 <table class="table zzb-table  f2 " >
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
			         		<select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select>
			         	</td>
		         	</tr>
		         	<tr class="bg4">
			         	<th style="text-align: center; vertical-align: middle;">险种</th>
			         	<th style="text-align: center; vertical-align: middle;">保额</th>
			         	<th style="text-align: center; vertical-align: middle;">不计免赔</th>
			         	<th style="text-align: center; vertical-align: middle;">险种</th>
			         	<th style="text-align: center; vertical-align: middle;">保额</th>
			         	<th style="text-align: center; vertical-align: middle;">不计免赔</th>
		         	</tr>
		         	<tr>
			         	<td style="text-align: center; vertical-align: middle;">机动车损失保险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label  class="checkbox-inline"><input type="checkbox">不计免赔</label></td>
			         	<td style="text-align: center; vertical-align: middle;">第三者责任险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label class="checkbox-inline" ><input type="checkbox">不计免赔</label></td>
		         	</tr>
		         	<tr>
			         	<td style="text-align: center; vertical-align: middle;">机动车第三者责任保险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label  class="checkbox-inline"><input type="checkbox">不计免赔</label></td>
			         	<td style="text-align: center; vertical-align: middle;">第三者责任险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label class="checkbox-inline" ><input type="checkbox">不计免赔</label></td>
		         	</tr>
		         	<tr>
			         	<td style="text-align: center; vertical-align: middle;">司机责任险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label  class="checkbox-inline"><input type="checkbox">不计免赔</label></td>
			         	<td style="text-align: center; vertical-align: middle;">第三者责任险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label class="checkbox-inline" ><input type="checkbox">不计免赔</label></td>
		         	</tr>
		         	<tr>
			         	<td style="text-align: center; vertical-align: middle;">乘客责任险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label  class="checkbox-inline"><input type="checkbox">不计免赔</label></td>
			         	<td style="text-align: center; vertical-align: middle;">第三者责任险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label class="checkbox-inline" ><input type="checkbox">不计免赔</label></td>
		         	</tr>
		         	<tr>
			         	<td style="text-align: center; vertical-align: middle;">司机动车全车盗抢保险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label  class="checkbox-inline"><input type="checkbox">不计免赔</label></td>
			         	<td style="text-align: center; vertical-align: middle;">第三者责任险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label class="checkbox-inline" ><input type="checkbox">不计免赔</label></td>
		         	</tr>
		         	<tr>
			         	<td style="text-align: center; vertical-align: middle;">玻璃单独破碎险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label  class="checkbox-inline"><input type="checkbox">不计免赔</label></td>
			         	<td style="text-align: center; vertical-align: middle;">第三者责任险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label class="checkbox-inline" ><input type="checkbox">不计免赔</label></td>
		         	</tr>
		         	<tr>
			         	<td style="text-align: center; vertical-align: middle;">自然损失险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label  class="checkbox-inline"><input type="checkbox">不计免赔</label></td>
			         	<td style="text-align: center; vertical-align: middle;">第三者责任险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label class="checkbox-inline" ><input type="checkbox">不计免赔</label></td>
		         	</tr>
		         	<tr>
			         	<td style="text-align: center; vertical-align: middle;">车身划痕损失险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label  class="checkbox-inline"><input type="checkbox">不计免赔</label></td>
			         	<td style="text-align: center; vertical-align: middle;">第三者责任险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label class="checkbox-inline" ><input type="checkbox">不计免赔</label></td>
		         	</tr>
		         	<tr>
			         	<td style="text-align: center; vertical-align: middle;">发动机涉水损失险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label  class="checkbox-inline"><input type="checkbox">不计免赔</label></td>
			         	<td style="text-align: center; vertical-align: middle;">第三者责任险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label class="checkbox-inline" ><input type="checkbox">不计免赔</label></td>
		         	</tr>
		         	<tr>
			         	<td style="text-align: center; vertical-align: middle;">车身划痕损失险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label  class="checkbox-inline"><input type="checkbox">不计免赔</label></td>
			         	<td style="text-align: center; vertical-align: middle;">第三者责任险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label class="checkbox-inline" ><input type="checkbox">不计免赔</label></td>
		         	</tr>
		         	<tr>
			         	<td style="text-align: center; vertical-align: middle;">指定专修厂险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label  class="checkbox-inline"><input type="checkbox">不计免赔</label></td>
			         	<td style="text-align: center; vertical-align: middle;">第三者责任险</td>
			         	<td style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
			         	<td style="text-align: center; vertical-align: middle;"><label class="checkbox-inline" ><input type="checkbox">不计免赔</label></td>
		         	</tr>
		         	<tr>
			         	<td style="text-align: center; vertical-align: middle;">交强险</td>
			         	<td colspan="5" style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
		
		         	</tr>
		         	<tr>
			         	<td style="text-align: center; vertical-align: middle;">车船税</td>
			         	<td colspan="5" style="text-align: center; vertical-align: middle;"><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
		
		         	</tr>
		         	<tr>
			         	<td style="text-align: center; vertical-align: middle;">备注</td>
			         	<td colspan="5" style="text-align: center; vertical-align: middle;">
			         	   <input type="text"  placeholder="请填写" value="" id="parentname" class="form-control "></td>
		
		         	</tr>
		         </table>
		
		    </div>
		</div>			
	    </div>
	    <!---->
	    <span class="label label-info">投保信息</span>
	    <div   class="mar4"  id="insureInfo">
	    <div class="row">
		     <div class="col-md-12">
				 <table class="table zzb-table  f2 " >
		         	<colgroup>
		         		<col class="col-xs-2">
		         		<col class="col-xs-4">
		         		<col class="col-xs-2">
		         		<col class="col-xs-4">
		         	</colgroup>
		         	<tr class="bg4">
		         	   <th colspan="4">请填写以下任意一组查询信息</th>
		         	</tr>
		         	<tr>
		         		<td>车牌</td>
		         		<td><input type="text"  placeholder="请填写" value="" id="parentname" class="form-control "></td>
		         		<td>车主</td>
		         		<td>
                              <input type="text"  placeholder="请填写" value="" id="parentname" class="form-control ">
			         		</td>
		         	</tr>
		         	<tr>
		         		<td>上年保单号</td>
		         		<td><input type="text"  placeholder="请填写" value="" id="parentname" class="form-control "></td>
		         	</tr>

		         	<tr class="bg4">
		         	   <th colspan="4">其他信息</th>
		         	</tr>
		         	<tr>
		         		<td>行驶区域</td>
		         		<td><select  class="form-control" >
			         			<option>保额</option>
			         			<option>保额</option>
			         			<option>保额</option>
			         		</select></td>
		         		<td>指定驾驶员</td>
		         		<td ><input type="text"  placeholder="请填写" value="" id="parentname" class="form-control w1"><span class="pad2"><button class="btn btn-primary">修改</button></span></td>
		         	</tr>
		         	
		         	<tr class="bg4">
		         	   <th colspan="4">影像</th>
		         	</tr>
		         	<tr>
		         	   <td colspan="4">
                        <div class="clearfix">
                        	<div class="col-xs-3">
                        		<div class="sel-b ">
			                        <input type="file" id="fileuploadinput" accept="image/*" zb-fileupload="fileUpload" >
			                        <label class="c5 camera-b bg1" for="fileuploadinput"><img  src="img/driving.jpg"></label>
			                        <span class="dblock2 pad10 t-center">行驶证正面照</span>
			                    </div>
                        		<div class="pad2 clearfix"><button class="btn btn-primary f-left">上传</button><button class="btn btn-defalut f-right">删除</button></div>
                        	</div>
                        	<div class="col-xs-3">
                        		<div class="sel-b ">
			                        <input type="file" id="fileuploadinput" accept="image/*" zb-fileupload="fileUpload" >
			                        <label class="c5 camera-b bg1" for="fileuploadinput"><img  src="img/driving.jpg"></label>
			                        <span class="dblock2 pad10 t-center">行驶证正面照</span>
			                    </div>
                        		<div class="pad2 clearfix"><button class="btn btn-primary f-left">上传</button><button class="btn btn-defalut f-right">删除</button></div>
                        	</div>
                        	<div class="col-xs-3">
                        		<div class="sel-b ">
			                        <input type="file" id="fileuploadinput" accept="image/*" zb-fileupload="fileUpload" >
			                        <label class="c5 camera-b bg1" for="fileuploadinput"><img  src="img/driving.jpg"></label>
			                        <span class="dblock2 pad10 t-center">行驶证正面照</span>
			                    </div>
                        		<div class="pad2 clearfix"><button class="btn btn-primary f-left">上传</button><button class="btn btn-defalut f-right">删除</button></div>
                        	</div>
                        	<div class="col-xs-3">
                        		<div class="sel-b ">
			                        <input type="file" id="fileuploadinput" accept="image/*" zb-fileupload="fileUpload" >
			                        <label class="c5 camera-b bg1" for="fileuploadinput"><img  src="img/driving.jpg"></label>
			                        <span class="dblock2 pad10 t-center">行驶证正面照</span>
			                    </div>
                        		<div class="pad2 clearfix"><button class="btn btn-primary f-left">上传</button><button class="btn btn-defalut f-right">删除</button></div>
                        	</div>
                        </div>
		         	   </td>
		         	</tr>
		         </table>
		
		    </div>
		</div>			
	    </div>
	    </div>
		</form>
	  </div>
  	<!---->
    <button class="btn btn-primary ">提交报价</button>
</body>
</html>