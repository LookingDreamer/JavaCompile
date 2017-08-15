<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>修改用户备注信息弹出窗口
		</title>
		<!--<link href="${staticRoot}/css/appmodule.css" rel="stylesheet"/>-->
		<style type="text/css">
			textarea#commentcontent {
				height:200px;
				width:100%;
			}
			body {font-size: 14px;}
		</style>

        
        <script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
		<!--引入修改用户备注信息弹出窗口js文件-->
		<script type="text/javascript">
			requirejs([ "cm/common/editUserRemark" ],function() {
				require([ "jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","public","multiselect"],	function($) {
					$(function() {
						initEditUserRemarkScript();

                        $('.js-multiselect').multiselect({
                            right: '#js_multiselect_to_1',
                            rightAll: '#js_right_All_1',
                            rightSelected: '#js_right_Selected_1',
                            leftSelected: '#js_left_Selected_1',
                            leftAll: '#js_left_All_1',
                            keepRenderingSort: true
                        });
					});
				});
			});
		</script>
		<script type="text/javascript">
            $(function()
            {
                $("#preparedSelectId").change(function()
                {
					var preOptVal = $(this).val();
					if(preOptVal == 1) { // 1: “常规投保资料”( 行驶证正页照 、行驶证副页照 、被保人身份证正面照 、被保人身份证反面照)
                        $("#from option").each(function () {
							var val = $(this).val();
                            if (val == "drivinglicensemain" || val == "drivinglicensevice" || val == "insuredidcardpositive" || val == "insuredidcardopposite") {
                                $(this).dblclick();
                            }
                        });
                    }
					if(preOptVal == 2) { // 1: “验车照”( 车辆正面照片 、车辆正后照片 、车辆前左45度照片 、车辆前右45度照片 、车辆后左45度照片、车辆后右45度照片 、带车架号照片 、人车合影、车辆正左面照、车辆正右面照)
                        $("#from option").each(function () {
                            var val = $(this).val();
                            if (val == "carfront" || val == "carbehind" || val == "carleftfront45" || val == "carrightfront45" || val == "carleftbehind45"
							|| val == "carrightbehind45" || val == "framenumber" || val == "personandcar" || val == "carfrontleftimage" || val == "carfrontrightimage") {
                                $(this).dblclick();
                            }
                        });
                    }
                });
            });
		</script>
	</head>
	<body>
		<!--修改用户备注弹出框-->
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">
				<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
			</button>
			<h6 class="modal-title">给用户的备注</h6>
		</div>
		<div class="modal-body">
			<form class="form-inline" role="form" id="editUserRemarkForm">
				<table class="table table-bordered">
					<tr>
						<td class="col-md-2 active">备注类型:</td>
						<td class="col-md-8 active">
							<div class="row">
								<div class="col-md-4">
									<select id="commenttype" name="commenttype">
										<option value="">请选择备注类型</option>
										<#list commenttypeList as commenttypeitem>
											<option value="${commenttypeitem.codevalue}"
												<#if commenttypeitem.codevalue == usercomment.commenttype>selected</#if>>
												${commenttypeitem.codename}
											</option>
										</#list>
									</select>
	
								</div>
								<div class="col-md-3">
								<lable>备注内容</lable>
								</div>
								<div class="col-md-5">
									<select id="commentcontenttype" name="commentcontenttype" style="width:160px;">
										<option value="">请选择备注内容类型</option>
										<#list commentcontenttypeList as commentcontenttypeitem>
											<option value="${commentcontenttypeitem.codevalue}"
												<#if commentcontenttypeitem.codevalue == usercomment.commentcontenttype>selected</#if>>
												${commentcontenttypeitem.codename}
											</option>
										</#list>
									</select>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td class="active"></td>
						<td>
							<textarea style="resize:none;" class="form-control" id="commentcontent" name="commentcontent"><#if usercomment??>${usercomment.commentcontent!""}</#if></textarea>
							<input type="hidden" id="trackid" name="trackid" value="${trackid}"/>
			  				<input type="hidden" id="tracktype" name="tracktype" value="${mainOrSub}"/>
			  				<input type="hidden" id="id" name="id" value="<#if usercomment??>${usercomment.id}</#if>"/>
			  				<input type="hidden" id="num" name="num" value="${num}"/>
			  				<input type="hidden" id="instanceId" name="instanceId" value="${instanceId}"/>
						</td>
					</tr>
				</table>

			<#if taskcode == "18"><!-- 人工核保 -->
                <div style="display:inline-block">待选列表：<span><select id="preparedSelectId">
					<option value="0">预设影像分组选择</option>
					<option value="1">常规投保资料</option>
					<option value="2">验车照</option>
				</select></span></div>
                <div style="display:inline-block;padding-left:200px;">已选列表</div>
                <div class="row">
                    <div class="col-xs-5">
                        <select name="from" id="from" class="js-multiselect form-control" data-right-selected="#right_Selected_1" size="8" multiple="multiple" style="width:260px;">
						<#list allList as insuranceImageBean>
                            <option  value="${insuranceImageBean.codetype }" rolename="${insuranceImageBean.riskimgtypename!"" }"> ${insuranceImageBean.riskimgtypename!"" }
							</option>
						</#list>
                        </select><#--
						<div>待选图片</div>-->
                    </div>
                    <#--<label for="address">待选图片</label>-->
                    <div class="col-xs-2">
                        <button type="button" id="js_right_All_1" class="btn btn-block"><i class="glyphicon glyphicon-forward"></i></button>
                        <button type="button" id="js_right_Selected_1" class="btn btn-block"><i class="glyphicon glyphicon-chevron-right"></i></button>
                        <button type="button" id="js_left_Selected_1" class="btn btn-block"><i class="glyphicon glyphicon-chevron-left"></i></button>
                        <button type="button" id="js_left_All_1" class="btn btn-block"><i class="glyphicon glyphicon-backward"></i></button>
                    </div>
                    <div class="col-xs-5">
                        <select name="to" id="js_multiselect_to_1" class="form-control" size="8" style="width:260px;">
						<#list allList as insuranceImageBean>
							<#list addList as selecteImage>
								<#if insuranceImageBean.codetype==selecteImage.codetype>
                                    <option value="${insuranceImageBean.codetype }" rolename="${insuranceImageBean.riskimgtypename!"" }"> ${insuranceImageBean.riskimgtypename!"" }
									</option>
								</#if>
							</#list>
						</#list>
                        </select><#--
						<div>已选图片</div>-->
                    </div>
                    <#--<label for="address">已选图片</label>-->
                </div>
			</#if>

				<div class="row">
					<div class="col-md-6" style="padding-left: 100px;">
						<#--<p>待选图片</p>-->
						<button class="btn btn-default" type="button" name="makesure"
							id="makesure" title="确定">&nbsp;&nbsp;确定&nbsp;&nbsp;</button>
					</div>
					<div class="col-md-6" style="padding-left:160px;">
                        <#--<p>已选图片</p>-->
						<button class="btn btn-default" type="button" name="cancel"
							id="cancel" title="取消" data-dismiss="modal">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>
					</div>
				</div>
				<div id="commentcontenttypeModelList" style="display:none;">
					<#list commentcontenttypeList as commentcontenttypeitem>
						<span id="CCTV_${commentcontenttypeitem.codevalue}">${commentcontenttypeitem.prop2}</span>
					</#list>
				</div>
			</form>
		</div>
	</body>
</html>
