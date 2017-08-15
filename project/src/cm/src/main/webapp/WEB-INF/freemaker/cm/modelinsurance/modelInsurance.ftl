<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>掌中保-车险投保-业管版</title>
<link rel="stylesheet"
	href="${staticRoot}/css/modelinsurance/bootstrap.min.css">
<link rel="stylesheet" 
	href="${staticRoot}/css/modelinsurance/zzb_yg.css">
<link rel="stylesheet" href="${staticRoot}/css/modelinsurance/date.css">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "cm/modelinsurance/modelinsurance" ]);
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
										<input type="text" name="jobnum" style="display: none;"
											id="jobnum" value="${INSBAgent.jobnum}">
										<td class="col-md-2 active">代理人姓名</td>
										<td class="col-md-2  c1 f2">${INSBAgent.name}</td>
										<td class="col-md-2 active">性别</td>
										<td class="col-md-2"><#if INSBAgent.sex=0>女</#if><#if INSBAgent.sex=1>男</#if></td>
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
										<td colspan="1">${INSBAgent.comname}</td>
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
				<li role="presentation" id="cheInfo" style="width:279px;height:40px"><a href="#"
					><div id="cheInfovv">车辆信息</div></a></li>
				<li role="presentation" id="insureCom" style="width:279px;height:40px"><a href="#"
					><div id="insureComvv">保险公司</div></a></li>
				<li role="presentation" id="insureConfig" style="width:279px;height:40px"><a href="#"
					><div id="insureConfigvv">保险配置</div></a></li>
				<!-- <li><a  data-toggle="tab" href="#insureInfo">投保信息</a></li>onclick="showinsureconfig()"-->
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
												<td><label for="exampleInputName">投保地区</label></td>
												<td>
												<input type="text" name="provinceNameok" class="form-control w1 "placeholder="" readonly id="provinceNameok"
													value="${quotearea.provinceName} ${quotearea.cityName}"> 
											    <input type="text" name="provinceCode" placeholder=""style="display: none;" id="provinceCode"
													value="${quotearea.provinceCode}">
												<input type="text" name="provinceName" placeholder=""style="display: none;" id="provinceName"
													value="${quotearea.provinceName}">  
											    <input type="text" name="cityCode" placeholder="" style="display: none;"
													id="cityCode" value="${quotearea.cityCode}"></td>
												<input type="text" name="cityName" placeholder="" style="display: none;"
													id="cityName" value="${quotearea.cityName}"></td>
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
						<div style="margin-bottom: 20px" id="cheInfodiv">
							<div class="row">
								<div class="col-md-12">
									<table class="table table-bordered ">
										<tbody>
											<tr>
												<td><label for="exampleInputName">品牌型号</label></td>
												<td><input type="hidden" value="" name="parentcode"
													id="parentcode"> <input type="text"
													placeholder="请选择" readonly="true" value="" id="standardname"
													class="form-control w1">
													<button type="button" class="btn btn-primary"
														data-toggle="modal" id="updatestyle">请修改</button> <!--<span class="btn btn-primary mar12"  id="updatestyle">请修改</span>-->
												</td>
											</tr>
											<tr>
												<td><label for="exampleInputName">发动机号</label></td>
												<td><input type="text" placeholder="请选择" value=""
													id="engineno_nub" class="form-control w1"></td>
											</tr>
											<tr>
												<td><label for="exampleInputName">车辆识别号码</label></td>
												<td><input type="text" placeholder="请选择" value=""
													id="vehicleframeno_nub" class="form-control w1"></td>
											</tr>

											<tr>
												<td><label for="exampleInputName">过户车</label></td>
												<td style="vertical-align: middle;">
												<div>
													<input
													class="form-control w1 date form_datetime" type="text"
													id="testtimestr" name="testtimestr">
													<label class="marl-2" for="exampleInputName" >过户日期</label>
													<label
														class="radio-inline"> <input type="radio"
															value="01" name="businesstype" id="businesstype01" onchange="changebusinesstype()">
															是
													</label> <label class="radio-inline"> <input type="radio"
															value="02" name="businesstype" id="businesstype01" onchange="changebusinesstype()" checked>
															否
													</label> 
												    </td>
													<!--<div id="testtimestrlabel" ></div>-->
											</tr>
											<tr>
												<td><label for="exampleInputName">车辆初登日期</label></td>
												<td style="vertical-align: middle;"><input
													class="form-control w1 date form_datetime" type="text"
													id="testtimeendstr" name="testtimeendstr"></td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<button id="cheInfobutton_last" type="button"
								name="cheInfobutton_last" onclick="showcheInfoIndiv('last')">上一步</button>
							<button class="btn btn-primary " id="cheInfobutton_next"
								type="button" name="cheInfobutton_next"
								onclick="showcheInfoIndiv('next')">下一步</button>
						</div>
						<div style="margin-bottom: 20px" id="insureComdiv">
							<div class="row">
								<div class="col-md-12">
									<table class="table table-hover table-striped f2 border"
										id="checktable" >
										<colgroup>
											<col class="col-xs-1">
											<col class="col-xs-3">
											<col class="col-xs-3">
											<col class="col-xs-3">
										</colgroup>
										<tr>
											<th style="text-align: center; vertical-align: middle;">选中</th>
											<th style="text-align: center; vertical-align: middle;">公司名称</th>
											<th style="text-align: center; vertical-align: middle;">分公司</th>
											<th style="text-align: center; vertical-align: middle;">出单网点</th>
										</tr>
									</table>
								</div>
							</div>
							<button id="insureCombutton_last" type="button"
								name="insureCombutton_last" onclick="insureComshowdiv('last')">上一步</button>
							<button class="btn btn-primary " id="insureCombutton_next"
								type="button" name="insureCombutton_next"
								onclick="insureComshowdiv('next')">下一步</button>
						</div>
						<!---->
						<div style="margin-bottom: 20px" id="insureConfigdiv">
							<div class="row">
								<div class="col-md-12">
									<table id="table-javascript"></table>
									<table class="table zzb-table  f2 ">
										<tr>
											<th style="text-align: center; vertical-align: middle;">证件类型</th>
											<td style="text-align: center; vertical-align: middle;"><select class="form-control w1" name="IDtype" id="IDtype"><option >-请选择-</option></select></th>
										</tr>
										<tr>
											<th style="text-align: center; vertical-align: middle;">证件号码</th>
											<td style="text-align: center; vertical-align: middle;"> <input type="text"
													name="IDnumber" placeholder="" id="IDnumber" class="form-control w1 "/></th>
										</tr>
									</table>
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
								onclick="insureConfigshowdiv('next')">提交报价</button>
							<button class="btn btn-primary" id="upload"
								type="button" name="upload">影像上传</button>
						</div> 
						<!---->
						<div style="margin-bottom: 20px" class="tab-pane fade"
							id="insureInfo">
							<div class="row">
								<div class="col-md-12">
									<table class="table zzb-table  f2 ">
										<colgroup>
											<col class="col-xs-2">
											<col class="col-xs-4">
											<col class="col-xs-2">
											<col class="col-xs-4">
										</colgroup>
										<tr class="bg4">
											<th colspan="4">被投保人</th>
										</tr>
										<tr>
											<td>被保人姓名</td>
											<td><input type="text" placeholder="请填写" value=""
												id="parentname" class="form-control "></td>
											<td>被保人证件类型</td>
											<td><select class="form-control"> <#list
													insuranceinfo.certKinds as certKinds>
													<option value="${certKinds.id}">${certKinds.codename}</option>
													</#list>
											</select></td>
										</tr>
										<tr>
											<td>被保人证件号码</td>
											<td colspan="3"><input type="text" placeholder="请填写"
												value="" id="parentname" class="form-control "></td>
										</tr>

										<tr class="bg4">
											<th colspan="4">投保人</th>
										</tr>
										<tr>
											<td>与被保人一致</td>
											<td colspan="3"><label class="radio-inline"><input
													type="radio" name="same">是</label><label
												class="radio-inline"><input type="radio"
													name="same">否</label></td>
										</tr>
										<tr>
											<td>投保人姓名</td>
											<td><input type="text" placeholder="请填写" value=""
												id="parentname" class="form-control "></td>
											<td>投保人证件类型</td>
											<td><select class="form-control"> <#list
													insuranceinfo.certKinds as certKinds>
													<option value="${certKinds.id}">${certKinds.codename}</option>
													</#list>
											</select></td>
										</tr>
										<tr>
											<td>投保人证件号码</td>
											<td colspan="3"><input type="text" placeholder="请填写"
												value="" id="parentname" class="form-control "></td>
										</tr>
										<tr class="bg4">
											<th colspan="4">车主</th>
										</tr>
										<tr>
											<td>与被保人一致</td>
											<td colspan="3"><label class="radio-inline"><input
													type="radio" name="same">是</label><label
												class="radio-inline"><input type="radio"
													name="same">否</label></td>
										</tr>
										<tr>
											<td>投保人证件类型</td>
											<td><select class="form-control"> <#list
													insuranceinfo.certKinds as certKinds>
													<option value="${certKinds.id}">${certKinds.codename}</option>
													</#list>
											</select></td>
											<td>被保人证件号码</td>

											<td><input type="text" placeholder="请填写" value=""
												id="parentname" class="form-control "></td>
										</tr>
										<tr class="bg4">
											<th colspan="4">联系方式</th>
										</tr>
										<tr>
											<td>联系电话</td>
											<td><input type="text" placeholder="请填写" value=""
												id="parentname" class="form-control "></td>
											<td>E-mail</td>

											<td><input type="text" placeholder="请填写" value=""
												id="parentname" class="form-control "></td>
										</tr>
										<tr class="bg4">
											<th colspan="4">保险期间</th>
										</tr>
										<tr>
											<td>商业起保日期</td>
											<td><input type="text" placeholder="请填写" value=""
												id="sybegin" class="form-control "></td>
											<td>商业终止日期</td>

											<td><input type="text" placeholder="请填写" value=""
												id="syend" class="form-control "></td>
										</tr>
										<tr>
											<td>交强险起保日期</td>
											<td><input type="text" placeholder="请填写" value=""
												id="jqbegin" class="form-control "></td>
											<td>交强险终止日期</td>

											<td><input type="text" placeholder="请填写" value=""
												id="jqend" class="form-control "></td>
										</tr>

										<tr class="bg4">
											<th colspan="4">其他信息</th>
										</tr>
										<tr>
											<td>行驶区域</td>
											<td><select class="form-control"> <#list
													insuranceinfo.carDrivingArea as carDrivingArea>
													<option value="${carDrivingArea.id}">${carDrivingArea.codename}</option>
													</#list>
											</select></td>
											<td>指定驾驶员</td>
											<td><input type="text" placeholder="请填写" value=""
												id="parentname" class="form-control w1"><span
												class="pad2"><button class="btn btn-primary">修改</button></span></td>
										</tr>
										<tr>
											<td>上年商业险投保公司</td>
											<td colspan="3"><input type="text" placeholder="请填写"
												value="" id="lastsuppliername" class="form-control w1"></td>
										</tr>
										<tr class="bg4">
											<th colspan="4">影像</th>
										</tr>
										<tr>
											<td colspan="4">
												<div class="clearfix">
													<div class="col-xs-3">
														<div class="sel-b ">
															<input type="file" id="fileuploadinput" accept="image/*"
																zb-fileupload="fileUpload"> <label
																class="c5 camera-b bg1" for="fileuploadinput"><img
																src="img/driving.jpg"></label> <span
																class="dblock2 pad10 t-center">行驶证正面照</span>
														</div>
														<div class="pad2 clearfix">
															<button class="btn btn-primary f-left">上传</button>
															<button class="btn btn-defalut f-right">删除</button>
														</div>
													</div>
													<div class="col-xs-3">
														<div class="sel-b ">
															<input type="file" id="fileuploadinput" accept="image/*"
																zb-fileupload="fileUpload"> <label
																class="c5 camera-b bg1" for="fileuploadinput"><img
																src="img/driving.jpg"></label> <span
																class="dblock2 pad10 t-center">行驶证正面照</span>
														</div>
														<div class="pad2 clearfix">
															<button class="btn btn-primary f-left">上传</button>
															<button class="btn btn-defalut f-right">删除</button>
														</div>
													</div>
													<div class="col-xs-3">
														<div class="sel-b ">
															<input type="file" id="fileuploadinput" accept="image/*"
																zb-fileupload="fileUpload"> <label
																class="c5 camera-b bg1" for="fileuploadinput"><img
																src="img/driving.jpg"></label> <span
																class="dblock2 pad10 t-center">行驶证正面照</span>
														</div>
														<div class="pad2 clearfix">
															<button class="btn btn-primary f-left">上传</button>
															<button class="btn btn-defalut f-right">删除</button>
														</div>
													</div>
													<div class="col-xs-3">
														<div class="sel-b ">
															<input type="file" id="fileuploadinput" accept="image/*"
																zb-fileupload="fileUpload"> <label
																class="c5 camera-b bg1" for="fileuploadinput"><img
																src="img/driving.jpg"></label> <span
																class="dblock2 pad10 t-center">行驶证正面照</span>
														</div>
														<div class="pad2 clearfix">
															<button class="btn btn-primary f-left">上传</button>
															<button class="btn btn-defalut f-right">删除</button>
														</div>
													</div>
												</div>
											</td>
										</tr>
									</table>

								</div>
							</div>
						</div>
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
	
	<div class="modal fade" id="photosupload">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">影像上传</h4>
	      </div>
	      <div class="modal-body">
            <div>
               	<table class="table zzb-table  f2 ">
					<tr>
					    <input type="hidden" id="jobNum" value="${INSBAgent.jobnum}"/>
					    <input type="hidden" id="fileid" value=""/>
	    	            <input type="hidden" id="fileName" value=""/>
						<td style="text-align: center; vertical-align: middle;">图片类型</td>
						<td><select class="form-control w1" name="fileType" id="fileType"><option >-请选择-</option></select></td>
					</tr>
					<tr>
						<td style="text-align: center; vertical-align: middle;">图片描述</td>
						<td><input type="text" id="fileDescribes" class="form-control w1" placeholder="请填写" value=""/></td>
					</tr>
					<tr>
						<td style="text-align: center; vertical-align: middle;">选择图片</td>
						<td><input type="file" id="file" name="file" onChange="if(this.value)insertTitle(this.value)" class="form-control w1" /></td>
					</tr>
				</table>
				<input type="button" class="btn btn-default" id="uploadbutton" value="上传" onclick="photoupload()"/>
            </div>
	      </div>
	      <div class="modal-footer" id="allphotos">
	      	
	      </div>
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
		
	<div id="datePlugin"></div>

	<div id="updatediv" class="modal fade bs-example-modal-lg"
		tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="gridSystemModalLabel">品牌型号</h4>
				</div>
				<div class="modal-body" style="overflow: auto; height: 500px;">
					<div >
						<input type="text" id="searchinfo" class="form-control w1"  placeholder="请输入车型关键字" />
						<button class="btn btn-primary" value="搜索" id="search">搜索</button>
					</div>
					<div id="carmodelbrand"><table id="table-carmodelbrand"></table></div>
					<div id="carmoremodelbrand"><table id="table-carmoremodelbrand"></table></div>
					<table class="table table-bordered f2" border="1">
						<tr>
							<td>品牌型号：</td>
							<td colspan="3"><input type="text" class="form-control" id="standardname1"
								name="standardname1" value="" /></td>
						</tr>
						<tr>
							<td>车辆性质：</td>
							<td><select id="carquality" class="form-control w1" name="carquality">
									<#list UseProps as useProps>
									<option value="${useProps.codevalue }"<#if useProps.codename=="非营业个人"> selected</#if>>${useProps.codename }</option>
									</#list>
							</select></td>
							<td>所属性质：</td>
							<td><select id="belongquality" class="form-control w1" name="belongquality">
									<#list UserType as userType>
									<option value="${userType.codevalue }"<#if userType.codename=="个人用车"> selected</#if>>${userType.codename }</option>
									</#list>
							</select></td>
						</tr>
						<tr>
							<td>核定载人数：</td>
							<td><input type="text" id="seat" class="form-control  w1" name="seat" />座</td>
							<td>核定载质量：</td>
							<td><input type="text" id="weight" class="form-control  w1" name="weight" />千克</td>
						</tr>
						<tr>
							<td>排气量：</td>
							<td><input type="text" id="displacement" class="form-control  w1" name="displacement" />升
							</td>
							<td>整备质量：</td>
							<td><input type="text" id="curbweight" class="form-control  w1" name="curbweight" />千克
							</td>
						</tr>
						<tr>
							<td>车价选择：</td>
							<td>
							<select id="price" class="form-control" name="price" onchange="zhiding()">
									<#list priceselection as ps>
									<option value="${ps.codevalue}"<#if ps.codename=="最低"> selected</#if>>${ps.codename}</option>
									</#list>
							</select></td>
							<td colspan="2"><div id="carshow">
							<input type="text" class="form-control  w1" id="price1" name="price1" placeholder="请填写"/>元</div></td>
						</tr>
						<tr>
							<td>备注：</td>
							<td colspan="3"><input type="text" id="remark" class="form-control" name="remark" />
							</td>
						</tr>
					</table>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal"
							id="submitCarModel">确定</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>


