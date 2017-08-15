<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>财务报表导出</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
		requirejs([ "extra/exportAccountExcel" ]);
</script>
<body>
	<div class="container-fluid">
        <div class="panel panel-default m-bottom-5">
            <div class="panel-heading padding-5-5">导出清单</div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-12">
                        <form role="form" id="exportexcel" method="post" action="/cm/account/exportBusinessList" target="_blank">
                            <div class="form-group form-inline col-md-12">
                                <label for="exampleInputCode">时间&nbsp;&nbsp;&nbsp;</label>
                                <input type="text" class="form-control form_datetime" id="startdate" name="startdate"
                                       readonly placeholder=""> -
                                <input type="text" class="form-control form_datetime" id="enddate" name="enddate" readonly
                                       placeholder="">
                            </div>
                            <div class="form-group form-inline col-md-12">
                                <label for="exampleInputOrgName">类型</label>
                                <select class="form-control m-left-5" id="listType" name="listType">
                                    <option value="0">业务清单</option>
                                    <option value="1">提现清单</option>
                                    <option value="2">奖金清单</option>
                                    <option value="3">个人财富汇总</option>
                                </select>
                                <label for="exampleInputOrgName">提现状态</label>
                                <select class="form-control m-left-5" id="withdrawStatus" name="withdrawStatus">
                                    <option value="">全部</option>
                                    <option value="0">提现中</option>
                                    <option value="1">提现成功</option>
                                    <option value="2">提现失败</option>
                                </select>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="panel-footer">
                <button id="businesslist" type="button" name="businesslist"
                        class="btn btn-primary">导出</button>
                <button id="resetbutton" type="button" name="resetbutton"
                        class="btn btn-primary">重置</button
            </div>
        </div>
         <div>
             &nbsp;&nbsp;&nbsp;
         </div>
        <div class="panel panel-default m-bottom-5">
            <div class="panel-heading padding-5-5">按月导出清单</div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-12">
                        <form role="form" id="exportsumexcel" method="post" action="/cm/account/exportSumBusinessList" target="_blank">
                            <div class="form-group form-inline col-md-12">
                                <label for="exampleInputCode">月份&nbsp;&nbsp;&nbsp;</label>
                                <input type="text" class="form-control form_datetime" id="sumstartdate" name="startdate"
                                       readonly placeholder="">
                            </div>
                            <div class="form-group form-inline col-md-12">
                                <label for="exampleInputOrgName">类型</label>
                                <select class="form-control m-left-5" id="sumlistType" name="listType">
                                    <option value="0">个人财富按月汇总表</option>
                                    <option value="1">机构交易汇总表</option>
                                </select>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="panel-footer">
                <button id="sumbusinesslist" type="button" name="businesslist"
                        class="btn btn-primary">导出</button>
                <button id="sumresetbutton" type="button" name="resetbutton"
                        class="btn btn-primary">重置</button
            </div>
        </div>

	</div>

<!--add refreshrefresh-->
</body>
</html>
