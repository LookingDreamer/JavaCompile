<!DOCTYPE html>
<html lang="en" class="fuelux">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>税率配置</title>
     
    <link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
    <link href="${staticRoot}/css/system/flat-ui-icon.css" rel="stylesheet">
 	<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
  	
           
    <style type="text/css">
    	
    	
        .htmleaf-header {
            margin-bottom: 15px;
            font-family: "Segoe UI", "Lucida Grande", Helvetica, Arial, "Microsoft YaHei", FreeSans, Arimo, "Droid Sans", "wenquanyi micro hei", "Hiragino Sans GB", "Hiragino Sans GB W3", "FontAwesome", sans-serif;
        }

        .htmleaf-icon {
            color: #fff;
        }

        tr {
            height: 50px;
        }

        td {
            vertical-align: middle;
        }

        .modal-body {
            overflow: auto;
            height: 380px;
        }
    </style>
   
</head>
<script type="text/javascript">
    requirejs([ "chn/taxrate" ,"lib/tsearch"]);
</script>
<body>
<div class="container-fluid">
	<div class="col-md-3">
        <div class="panel panel-default m-bottom-2">
            <div class="panel-heading padding-5-5">渠道列表</div>
            <div class="panel-body">
                <div><input class="form-control ztree-search" id="treesearch" data-bind="deptTree" name="treesearch"
                            placeholder="输入渠道名称关键字进行搜索"/></div>
                <div class="ztree" id="deptTree" style="width:100%; height:600px; overflow-y:auto;overflow-x:auto;">
                    正在加载渠道数据......
                </div>
            </div>
        </div>
    </div>

    <div class="col-md-9">
	<div class="panel panel-default m-bottom-5">
		<div class="panel-heading padding-5-5">
			<div class="panel-body">
				<form class="form-inline" role="form" id="queryTaxrateFrom">
				
					<table class="table table-bordered ">
						<tr>
							<div class="row">
								<div class="form-group form-inline col-md-4">
									<td><label for="channelcode">渠道&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
									<td><input class="form-control" type="text"
										id="channelname" name="channelname" readonly="readonly" placeholder="请选择渠道">
									<input class="form-control" type="hidden"
										id="channelid" name="channelid"></td>
								</div>
								<div class="form-group form-inline col-md-4">
									<td><label for="status">状态</label></td>
									<td><select class="form-control" id="status" name="status" >
										<option value="">全部</option>
										<#list taxrateList as tt>
										<option value="${tt.codevalue}">${tt.codename}</option>
										</#list>
									</select></td>
								</div>
								<div class="form-group form-inline col-md-4">
									<td><label for="keyword">关键字</label></td>
									<td><input class="form-control" type="text"
										id="keyword" name="keyword" placeholder="税率或税率ID"></td>
								</div>
							</div>
						</tr>
		
					</table>
				</form>
				<div align="right"
					style="padding-top: 10px; padding-bottom: 10px"
					class="btn-group" role="group" aria-label="...">
					<button type="button" class="btn btn-primary" name="querybt" id="querybt">查询</button>
					<button data-toggle="modal" data-target="#myModal"
						type="button" class="btn btn-primary" name="usingbt"
						id="usingbt">启用</button>
					<button data-toggle="modal" data-target="#myModal"
						type="button" class="btn btn-primary" name="addbt"
						id="addbt">新增</button>
					<button data-toggle="modal" data-target="#myModal"
						type="button" class="btn btn-primary" name="closebt"
						id="closebt">关闭</button>
					<button data-toggle="modal" data-target="#myModal"
						type="button" class="btn btn-primary" name="copybt"
						id="copybt">复制</button>
					<button data-toggle="modal" data-target="#myModal"
						type="button" class="btn btn-primary" name="deletebt"
						id="deletebt">删除</button>
					<button class="btn btn-primary" type="button" name="refresh"
						title="Refresh" id="refresh2">
						<i class="glyphicon glyphicon-refresh icon-refresh"></i>
					</button>
				</div>
				<div class="row">
					<div class="col-md-12">
						<table id="taxrate_list"></table>
					</div>
				</div>
				<div class="panel panel-default" id="tablemode" style="display: none;">
                    <div class="panel-heading padding-5-5 form-group col-md-12 form-inline">
                        <h3 class="panel-title">
                            <label for="disInterface">&nbsp;&nbsp;税率设置</label><button id="defultclose" class="btn" data-toggle="collapse" data-parent="#panelOne"
                             style="float:right;position: relative;top: -8px;">关闭
                    </button>                        </h3>
                   <form class="form-inline" role="form" id="addOrUpdateForm">
                	<table class="table table-bordered">
						 		<input id="id" name="id" type="hidden">
						 		<input id="xxchannelid" name="channelid" type="hidden">
						 		<input id="xstatus" name="status" type="hidden">
                              <tr>
                                  <div class="row">
                                      <div class="form-group form-inline col-md-4">
                                          <td><label for="effectivetime">生效时间<span style="color:red">*</span>&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                          <td><input class="form-control" id="effectivedate" type="text" name="effectivedate"></td>
                                      </div>
                                      <div class="form-group form-inline col-md-5" >
                                          <td><label for="terminaltime">失效时间</label></td>
                                          <td>
                                          	<div class="input-group">
                                          <input class="form-control" type="text" id="terminaldate" name="terminaldate">
                                          	<span class="input-group-btn">
                                        	<button id="clear-terminaldate" class="btn btn-default" type="button">X</button>
                                       </span></div>
                                          </td>
                                      </div>
                                      <div class="form-group form-inline col-md-3" >
                                          <td><label for="taxrate">税率 <span style="color:red">*</span></label></td>
                                          <td><input class="form-control" type="text" id="taxrate" name="taxrate"></td>
                                      </div>
                                  </div>
							  </tr>
							  <tr>
                                  <div class="row">
                                      <div class="form-group form-inline col-md-4">
                                          <td><label for="channelcode">应用渠道<span style="color:red">*</span>&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                          <td><input class="form-control" type="text" id="xchannelname" name="channelname" readonly="readonly"></td>
                                      </div>
                                      <div class="form-group form-inline col-md-4">
                                          <td><label for="exampleInputCode"></label></td>
                                          <td></td>
                                      </div>
                                       <td><label for="exampleInputCode"></label></td>
                                      <td></td>
                                  </div>
							  </tr>
		
					</table>
				</form>
					</div>
                <div class="panel-footer">
                <div align="right"
					style="padding-top: 10px; padding-bottom: 10px"
					class="btn-group" role="group" aria-label="...">
					<button data-toggle="modal" data-target="#myModal"
						type="button" class="btn btn-primary" name="savebutton" id="savebutton">保存</button>
					<button data-toggle="modal" data-target="#myModal"
						type="button" class="btn btn-primary" name="usingbutton"
						id="usingbutton">启用</button>
					<button data-toggle="modal" data-target="#myModal"
						type="button" class="btn btn-primary" name="closebutton"
						id="closebutton">关闭</button>
				</div>
                </div>	
                </div>
			</div>	
				<div class="panel panel-default" id="tableAddTaxModel" style="display: none;">
                    <div class="panel-heading padding-5-5 form-group col-md-12 form-inline">
                        <h3 class="panel-title">
                            <label for="disInterface">&nbsp;&nbsp;税率新增</label><button id="xdefultclose" class="btn" data-toggle="collapse" data-parent="#panelOne"
                             style="float:right;position: relative;top: -8px;">关闭
                    </button>                        </h3>
                   <form class="form-inline" role="form" id="xaddOrUpdateForm">
                	<table class="table table-bordered">
						 		<input id="channelids" name="channelids" type="hidden">
                              <tr>
                                  <div class="row">
                                      <div class="form-group form-inline col-md-4">
                                          <td><label for="effectivetime">生效时间<span style="color:red">*</span>&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                          <td><input class="form-control" id="xeffectivedate" type="text" name="effectivedate"></td>
                                      </div>
                                      <div class="form-group form-inline col-md-5" >
                                          <td><label for="terminaltime">失效时间</label></td>
                                          <td>
                                          <div class="input-group">
                                          <input class="form-control" type="text" id="xterminaldate" name="terminaldate">
                                          <span class="input-group-btn">
                                        <button id="clear-terminaldate2" class="btn btn-default" type="button">X</button>
                                       </span>
                                       </div>
                                          </td>
                                      </div>
                                      <div class="form-group form-inline col-md-3" >
                                          <td><label for="taxrate">税率 <span style="color:red">*</span></label></td>
                                          <td><input class="form-control" type="text" id="xtaxrate" name="taxrate"></td>
                                      </div>
                                  </div>
							  </tr>
							  <tr>
                                			<div class="form-group form-inline col-md-4" >
                                          <td><label for="channelcode">应用渠道<span style="color:red">*</span>&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                          <td colspan="5"><input class="form-control" type="text" id="addChannelBt" name="channelnames" style="width:900px" placeholder="请点击选择渠道">
                    					</td>
                                      </div>
							  </tr>
		
					</table>
				</form>
					</div>
                <div class="panel-footer">
                <div align="right"
					style="padding-top: 10px; padding-bottom: 10px"
					class="btn-group" role="group" aria-label="...">
					<button data-toggle="modal" data-target="#myModal"
						type="button" class="btn btn-primary" name="xsavebutton" id="xsavebutton">保存</button>
					<button data-toggle="modal" data-target="#myModal" style="display: none"
						type="button" class="btn btn-primary" name="usingbutton"
						id="xusingbutton">启用</button>
					<button data-toggle="modal" data-target="#myModal" style="display: none"
						type="button" class="btn btn-primary" name="closebutton"
						id="xclosebutton">关闭</button>
				</div>
                </div>	
                </div>
			</div>	
		</div></div>
	</div>
</div><!-- container-fluid end  -->

<!-- 税率复制 -->
<div class="modal fade" id="copyTaxrateModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="modal-content" style="width: 576px;">
            <div class="modal-header">
                 <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span></button>
                <div class="panel panel-default m-bottom-5" style="width: 544px;">
                    <div class="panel-heading padding-5-5" style="font-weight: bold;"><input type="text"
                                                                                             id="conditionTip"
                                                                                             style="border: 0px;background-color:transparent;"
                                                                                             value="" readonly/>
                    </div>
                    <div class="panel-body">
                        <div class="panel panel-default m-bottom-2">
                            <div class="panel-heading padding-5-5">渠道列表</div>
                            <div class="panel-body">
                                <div><input class="form-control ztree-search" id="treemodalsearch" data-bind="deptModalTree" name="treesearch"
                                            placeholder="输入渠道关键字进行搜索"/></div>
                                <div class="ztree" id="deptModalTree" style="width:100%; height:600px; overflow-y:auto;overflow-x:auto;">
                                    正在加载渠道数据......
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                     <div align="right"
					style="padding-top: 10px; padding-bottom: 10px"
					class="btn-group" role="group" aria-label="...">
                        <input id="execAddButton" name="execCopyButton" data-toggle="modal" data-target="#myModal"
						type="button" class="btn btn-primary" value="确定" style="display: none;"/>
                        <input id="execCopyButton" name="execCopyButton" data-toggle="modal" data-target="#myModal"
						type="button" class="btn btn-primary" value="复制" style="display: none;"/>
                        <input id="closeCopyButton" name="closeCopyButton" class="btn btn-primary" type="button"
                               data-dismiss="modal" value="取消"/></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<!-- 关闭处理 -->
<div class="modal fade" id="colseCommissionRateModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="modal-content" style="width: 482px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <div class="panel panel-default m-bottom-5" style="width: 448px;">
                    <div class="panel-heading padding-5-5" style="font-weight: bold;">关闭税率配置！</div>
                    <div class="panel-body">
                        <table class="table-no-bordered">
                        	<input type="hidden" id="taxrateId" name="taxrateId"> 
                          
                            <tr>
                                <td>关闭模式</td>
                                <td style="padding-bottom:5px;">
                                    &nbsp; &nbsp; &nbsp;
                                    <input type="radio" name="operatetype" id="immediateClose" value="1" >
                                    立即关闭 &nbsp; &nbsp; &nbsp;
                                    <input type="radio" name="operatetype" id="confirmDateClose" value="2"  >
                                    特定时间关闭 &nbsp; &nbsp;&nbsp;
                                </td>
                            </tr>
                            <tr id="set-terminaldate-tr" style="display: none">
                                <td>失效时间</td>
                                <td class="text-right">
                                    <div class="form-group col-md-12 form-inline">
                                        <input type="text" class="form-control form_datetime" id="cterminaldate"
                                               name="cterminaldate"
                                               readonly placeholder="">
                                    </div>
                                </td>
                            </tr>

                        </table>
                    </div>
                    <div class="panel-footer">
                        <input id="cexecButton" name="execButton" type="button" class="btn btn-primary" value="确定"/>
                        <input id="ccloseButton" name="closeButton" class="btn btn-primary" type="button"
                               data-dismiss="modal" value="取消"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 关闭处理 -->
</body>

</html>
