<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>日期配置</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<style type="text/css"> 
        tr{height:40px;}
		td{vertical-align: left;}
</style>
<script type="text/javascript">
	requirejs(["extra/miniWorkDate"]);
</script>
<body>
	<div class="panel panel-default m-bottom-5">
		<div class="panel panel-default m-bottom-5">
			<div class="panel-body">
				<div class="row">
					<div class="col-md-12">
						<form role="form" id="miniDate">
							<div class="form-group form-inline col-md-4">
								<label for="exampleInputCode">日期</label>
								<input type="text" class="form-control form_datetime" id="datestr" name="datestr"
									   readonly placeholder="">
							</div>
							
							<div class="form-group form-inline col-md-8">
								<label for="exampleInputOrgName">日期类型</label> <select class="form-control m-left-5" id="datetype" name="datetype">
								<option value="">全部</option>
								<option value="01">工作日</option>
								<option value="02">节假日</option>
							   
							</select>
							</div>
							<div class="form-group form-inline col-md-4">
								<label for="exampleInputOrgName">年份</label> <input type="text"
																					 class="form-control m-left-5"
																					 id="year" name="year"
																					 placeholder="" value="">
							</div>
							<div class="form-group form-inline col-md-4">
								<label for="exampleInputOrgName">月份</label> <input type="text"
																					 class="form-control m-left-5"
																					 id="month" name="month"
																					 placeholder="" value="">
							
							
							</div>
							<div class="form-group form-inline col-md-4">
								<label for="exampleInputOrgName">星期</label> <input type="text"
																					 class="form-control m-left-5"
																					 id="weekday" name="weekday"
																					 placeholder="" value="">
							</div>
																			 
						
						
						</form>
					</div>	
				</div>
			</div>
			<div class="panel-footer">
				<button id="querybutton" type="button" name="querybutton"
						class="btn btn-primary">查询
				</button>
				
				<button id="resetbutton" type="button" name="resetbutton"
                                  class="btn btn-primary">重置</button>
				
				<button class="btn btn-primary" type="button" name="refresh"
											title="Refresh" id="refresh">
											<i class="glyphicon glyphicon-refresh icon-refresh"></i>
										</button>						
										
				<button class="btn btn-primary" type="button" name="toggle" title="Toggle" id="toggle">
												<i class="glyphicon glyphicon-list-alt icon-list-alt"></i>
										</button>
										
				<button id="initYear" type="button" name="initYear" style="float:right;"
                                  class="btn btn-primary">初始化年份</button>
				
			</div>
		</div>
      
		
		<div class="panel panel-default">
			<div class="row">
				<div class="col-md-12">
					<table id="table-list"></table>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="updateMiniDateModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span class="label label-danger">X</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">修改日期类型，请注意！</div>
                        <div class="panel-body">
                           
                            <table class="table-no-bordered">
                               
                                <tr style="display: none;">
                                    <td class="text-right"><label>id：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control readonlysetting disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_id" name="id"
                                                                           /></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>日期：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control readonlysetting disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_datestr" name="date"
                                                                           /></td>
                                </tr>
                                <tr style="display: none;">
                                    <td class="text-right"><label>月份：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control readonlysetting disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_month" name="month"
                                                                           /></td>
                                </tr>
                                <tr style="display: none;">
                                    <td class="text-right"><label>号子：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control readonlysetting disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_day" name="day"
                                                                           /></td>
                                </tr>
                                <tr>
                                    <td class="text-right"><label>星期：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control readonlysetting disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_weekday" name="weekday"
                                                                           /></td>
                                </tr>
                             
                                <tr>
                                    <td class="text-right"><label>日期类型：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <select class="form-control readonlysetting disabledsetting" id="s_datetype" name="datetype">
                                        <#list datetypeList as code>
                                            <option value="${code.codevalue }">${code.codename }</option>
                                        </#list>
                                        </select>
                                       <!-- <input class="form-control readonlysetting disabledsetting" style="width: 280px;"
                                                                           type="text" id="s_activitytype" name="activitytype"
                                                                           />
                                        -->
                                    </td>
                                </tr>
                             
                               
                                <tr>
                                    <td class="text-right"><label>备注：</label></td>
                                    <td style="padding-bottom:5px;">
                                        <textarea class="form-control" rows="3"  id="s_noti" name="noti">
                                        </textarea>
                                    </td>
                                </tr>
                            </table>
                            <div class="col-md-12" style="font-weight: bold; padding-top: 5px;">是否保存？</div>
                        </div>
                        <div class="panel-footer">
                            <input id="s_execButton" name="s_execButton" type="button" class="btn btn-primary" value="确定"/>
                            <input id="closeButton" name="closeButton" class="btn btn-primary" type="button"
                                   data-dismiss="modal" value="取消"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
	<div class="modal fade" id="initMiniDateModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         data-backdrop="false" data-keyboard="false" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content" id="modal-content" style="width: 482px;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span class="label label-danger">X</span><span
                            class="sr-only">Close</span></button>
                    <div class="panel panel-default m-bottom-5" style="width: 448px;">
                        <div class="panel-heading padding-5-5" style="font-weight: bold;">初始化本年度，请注意！</div>
                        <div class="panel-body">
                           
                            <table class="table-no-bordered">
                               
                            
                                <tr>
                                    <td class="text-right"><label>年份：</label></td>
                                    <td style="padding-bottom:5px;"><input class="form-control readonlysetting disabledsetting" style="width: 280px;"
                                                                           type="text" id="i_year" name="year" 
                                                                           /></td>
                                </tr>
                              
                            </table>
                            <div class="col-md-12" style="font-weight: bold; padding-top: 5px;">是否初始化？</div>
                        </div>
                        <div class="panel-footer">
                            <input id="i_execButton" name="i_execButton" type="button" class="btn btn-primary" value="确定"/>
                            <input id="closeButton" name="closeButton" class="btn btn-primary" type="button"
                                   data-dismiss="modal" value="取消"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

	
</body>