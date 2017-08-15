<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "zzbconf/prizeinfo" ]);
</script>

<br>

<div class="col-md-12">
				<form  action="save" method="post">
				 <input type="hidden" id="id" name="id" value="${insbPrizeinfo.id}">
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">用户名称</label> <input type="text"
							 id="name" name="name" value="${insbPrizeinfo.name}">							
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputCode">活动奖品</label> <input type="text"
							 id="type" name="type" value="${insbPrizeinfo.name}">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputName">状态</label> <input type="text"
							 id="counts" name="counts" value="${insbPrizeinfo.counts}">
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="exampleInputOrgName">获取时间</label> <input type="text"
							<input type="text"	class="form-control form_datetime" id="effectivetime" name="effectivetime" readonly placeholder="${insbPrizeinfo.effectivetime}" value="${insbPrizeinfo.effectivetime}">
					</div>
                    <div class="form-group form-inline col-md-4">
                        <label for="exampleInputOrgName">使用时间</label> <input type="text"
                        <input type="text"	class="form-control form_datetime" id="invalidtime" name="invalidtime" readonly placeholder="${insbPrizeinfo.invalidtime}" value="${insbPrizeinfo.invalidtime}" >
                    </div>
                   <div class="form-group form-inline col-md-4">
						<label for="exampleInputName">数额</label> <input type="text"
						 id="effectiveDates" name="effectiveDates" value="${insbPrizeinfo.effectiveDates}">
					</div>
                    <input type="submit" value="保存"> <input id="backbutton" type="button" value="返回">
				</form>
			</div>