<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/insbactivityinfo" ]);
</script>

<br>

<div class="col-md-12">
				<form  action="save" method="post">
				 <input type="hidden" id="id" name="id" value="${insbActivityinfo.id}">
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">活动名称</label> <input type="text"id="name" name="name" value="${insbActivityinfo.name}">							
					</div>
					
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">活动类型</label> <input type="text"
id="type" name="type" value="${insbActivityinfo.type}">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputName">活动限制</label> <input type="text" id="limited" name="limited" value="${insbActivityinfo.limited}">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputOrgName">活动生效时间</label> <input type="text"
							<input type="text"	class="form-control form_datetime" id="effectivetime" name="effectivetime" readonly placeholder="${insbActivityinfo.effectivetime}" value="${insbActivityinfo.effectivetime}">
					</div>
                    <input type="submit" value="保存"> <input id="backbutton" type="button" value="返回">
				</form>
			</div>