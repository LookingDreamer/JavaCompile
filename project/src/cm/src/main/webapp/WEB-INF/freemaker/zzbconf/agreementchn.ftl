<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>协议切换</title>
<script type="text/javascript">
	requirejs([ "zzbconf/agreementchn" ], function () {
		require(["jquery","bootstrap-table","bootstrap","bootstrapTableZhCn","zTree","zTreecheck","public","bootstrapdatetimepicker","bootstrapdatetimepickeri18n"], function ($) {
			$(function() {
				initAgreementChn();
				treesearch();
			}); 
		}); 
	});
</script>

<body>
<div class="panel panel-default">
	<div class="panel panel-default m-bottom-5">
		<div class="panel-heading padding-5-5">协议切换</div>
		<div class="panel-body">
		    <div class="row">
			<div class="col-md-12">
				<form role="form" id="qForm">
                    <div class="form-group form-inline col-md-4">
                        <label for="">协议名称</label> 
						<input type="text" class="form-control m-left-5" id="agname" name="agname" placeholder="">
                    </div>
                    <div class="form-group form-inline col-md-4">
                        <label for="">协议编码</label> 
						<input type="text" class="form-control m-left-5" id="agcode" name="agcode" placeholder="">
                    </div>
					<div class="form-group form-inline col-md-4">
						<label for="">所属机构</label> 
						<input type="hidden" id="deptid" name="deptid" >
						<input type="text" class="form-control m-left-5" id="deptname" name="deptname" placeholder="点击选择" readonly="readonly">
						
					</div>
					<div class="form-group form-inline col-md-4">
						<label for="">供&nbsp;&nbsp;应&nbsp;&nbsp;商</label> 
						<input type="hidden" id="providerid" name="providerid" >
						<input type="text" class="form-control m-left-5" id="providername" name="providername" placeholder="点击选择" readonly="readonly">
						
					</div>
				</form>
			</div>
			</div>
		</div>
		<div class="panel-footer">
			<button id="querybutton" type="button" name="querybutton" class="btn btn-primary">查询</button>
			<button id="resetbutton" type="button" name="resetbutton" class="btn btn-primary">重置</button>
			<button class="btn btn-primary" type="button" name="refresh" title="Refresh" id="refresh">
				<i class="glyphicon glyphicon-refresh icon-refresh"></i>
			</button>
			<button class="btn btn-primary" type="button" name="toggle" title="Toggle" id="toggle">
				<i class="glyphicon glyphicon-list-alt icon-list-alt"></i>
			</button>
		</div>
	</div>
	<div class="panel panel-default">
	    <div class="panel-heading padding-5-5">
	    <div class="row">
			<div class="col-md-2">&nbsp;</div>
		</div>
		</div>
		<div class="row">
			<div class="col-md-12"><table id="table-list"></table></div>
	    </div>
	</div>
	
	<div class="panel panel-default" style="display:none;" id="editAg">
	    <div class="panel-heading padding-5-5 form-group col-md-12 form-inline">
<<<<<<< .working
	        <h3 class="panel-title">
	            <label for="">&nbsp;&nbsp;编辑协议：
	                <input id="agName" type="text" class="form-control" disabled/>
	                <input type="hidden" id="selAgreeId" name="selAgreeId" value=""/>
	                <input type="hidden" id="selAgreeCode" name="selAgreeCode" value=""/>
	                <input type="hidden" id="selPrvId" name="selPrvId" value=""/>
	                <input type="hidden" id="selDeptId" name="selDeptId" value=""/>
	            </label>
	        </h3>
=======
	        
            <label for="">&nbsp;&nbsp;编辑协议：
                <input id="agName" type="text" class="form-control" disabled/>
                <input type="hidden" id="selAgreeId" name="selAgreeId" value=""/>
                <input type="hidden" id="selAgreeCode" name="selAgreeCode" value=""/>
                <input type="hidden" id="selPrvId" name="selPrvId" value=""/>
                <input type="hidden" id="selDeptId" name="selDeptId" value=""/>
            </label>
	        
>>>>>>> .merge-right.r12306
	    </div>
	    <div class="panel-body">
	    	<button id="addchnbutton" type="button" name="addchnbutton" class="btn btn-primary">添加渠道</button>
			<button id="delchnbutton" type="button" name="delchnbutton" class="btn btn-primary">移除渠道</button>
			<button id="switchagbutton" type="button" name="switchagbutton" class="btn btn-primary">协议切换</button>
	        <div class="tab-content">
	            <table id="chntable-list"></table>
	        </div>
	    </div>
	</div>
</div>
	
<div id="showdept" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
  <div class="modal-dialog" role="document">
	<div class="modal-content">
	  <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		<h4 class="modal-title" id="gridSystemModalLabel">选择机构</h4>
	  </div>
	  <div class="modal-body" style="overflow: auto;height:390px;">
		<div class="container-fluid">
		  <div class="row">
			<ul id="depttree" class="ztree"></ul>
		  </div>
		</div>
	  </div>
	  <div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	  </div>
	</div>
  </div>
</div>

<div id="showprovider" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
  <div class="modal-dialog" role="document">
	<div class="modal-content">
	  <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		<h4 class="modal-title" id="gridSystemModalLabel">选择供应商</h4>
	  </div>
	  <div class="modal-body" style="overflow: auto;height:390px;">
		<div class="container-fluid">
		  <div class="row">
			<ul id="providertree" class="ztree"></ul>
		  </div>
		</div>
	  </div>
	  <div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	  </div>
	</div>
  </div>
</div>

<div class="modal fade" id="addChnModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="modal-content" style="width: 576px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <div class="panel panel-default m-bottom-5" style="width: 544px;">
                    <div class="panel-heading padding-5-5" style="font-weight: bold;">
                    	<input type="text" id="" style="border: 0px;background-color:transparent;" value="添加渠道" readonly/>
                    </div>  
					<div>
					
					<input class="form-control ztree-search" id ="tresearch" data-bind="channelModalTree" name="treesearch" placeholder="输入渠道名称关键字进行搜索" />
					
					</div>
					<div class="ztree" id="channelModalTree" style="width:100%; height:500px; overflow-y:auto;overflow-x:auto;">
						正在加载渠道数据......
					</div>
                    <div class="panel-footer">
                        <input id="execCopyButton" name="execCopyButton" type="button" class="btn btn-primary" value="下一步"/>
                        <input id="closeCopyButton" name="closeCopyButton" class="btn btn-primary" type="button"
                               data-dismiss="modal" value="取消"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="switchCfgModalAdd" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="modal-content" style="width: 760px;">
            <div class="modal-body" style="width: 760px;height: 550px;overflow-y: auto;overflow-x: hidden;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <div class="panel panel-default m-bottom-5" style="width: 730px;">
                    <div class="panel-heading padding-5-5" style="font-weight: bold;">
                    	<input type="text" id="cfgagcodenameAdd" style="border: 0px;background-color:transparent;" value="协议配置" readonly/>
                    </div>  
					
					<div class="panel-body">
                        <ul class="nav nav-pills nav-justified">
                            <li class="active"><a href="#depttabAdd" data-toggle="pill" id="outdeptAdd">出单网点</a></li>
                            <li><a href="#paytypetabAdd" data-toggle="pill" id="paytypeAdd">支付方式</a></li>
                        </ul>
                        <div id="myTabContentAdd" class="tab-content">
                            <!--出单网点选项卡-->
                            <div class="tab-pane fade in active" id="depttabAdd">
                                <br/>
                                <table class="table table-striped" id="deptnameTableAdd" ></table>
                                <label id="deptradiosAdd"></label>
                            </div>
                            <!--支付方式选项卡-->
                            <div class="tab-pane fade" id="paytypetabAdd">
                                <table class="table table-striped" id="payTypeTableAdd"></table>
                            </div>
                        </div>
                    </div>
                </div>
                
                <input type="hidden" id="selChnNames" name="selChnNames" value=""/>
                <input type="hidden" id="selChnIds" name="selChnIds" value=""/>
                <button id="swactbuttonAdd" type="button" name="swactbuttonAdd" class="btn btn-primary">添加</button>
                <input class="btn btn-primary" type="button" data-dismiss="modal" value="取消"/>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="switchModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="modal-content" style="width: 760px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <div class="panel panel-default m-bottom-5" style="width: 730px;">
                    <div class="panel-heading padding-5-5" style="font-weight: bold;">
                    	<input type="text" id="" style="border: 0px;background-color:transparent;" value="协议切换" readonly/>
                    </div>  
					
					<div class="panel-body">
					    <div class="row">
						<div class="col-md-12">
							<form role="form" id="qFormsw">
			                    <div class="form-group form-inline col-md-6">
			                        <label for="">协议名称</label> 
									<input type="text" class="form-control m-left-5" id="agnamesw" name="agnamesw" placeholder="">
			                    </div>
			                    <div class="form-group form-inline col-md-6">
			                        <label for="">协议编码</label> 
									<input type="text" class="form-control m-left-5" id="agcodesw" name="agcodesw" placeholder="">
			                    </div>
			                    <div class="form-group form-inline col-md-6">
									<label for="">供&nbsp;&nbsp;应&nbsp;&nbsp;商</label> 
									<input type="hidden" id="provideridsw" name="provideridsw" >
									<input type="text" class="form-control m-left-5" id="providernamesw" name="providernamesw" placeholder="点击选择" readonly="readonly">
									
								</div>
								<div class="form-group form-inline col-md-6">
									<label for="">查询机构</label> 
									<input type="hidden" id="deptidsw" name="deptidsw" >
									<input type="text" class="form-control m-left-5" id="deptnamesw" name="deptnamesw" placeholder="点击选择" readonly="readonly">
									
								</div>
							</form>
						</div>
						</div>
					</div>
					
					<div class="panel-footer">
						<button id="querybuttonsw" type="button" name="querybuttonsw" class="btn btn-primary">查询</button>
						<button id="resetbuttonsw" type="button" name="resetbuttonsw" class="btn btn-primary">重置</button>
						<input id="closeSwButton" name="closeSwButton" class="btn btn-primary" type="button" data-dismiss="modal" value="取消"/>
						<input id="swNextButton" name="swNextButton" type="button" class="btn btn-primary" value="下一步"/>
					</div>
					<br/>
					<div class="panel panel-default">
						<div class="row">
							<div class="col-md-12"><table id="tablesw-list"></table></div>
						</div>
					</div>
					
                </div>
            </div>
        </div>
    </div>
</div>

<div id="showdeptsw" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
  <div class="modal-dialog" role="document">
	<div class="modal-content">
	  <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		<h4 class="modal-title" id="gridSystemModalLabel">选择机构</h4>
	  </div>
	  <div class="modal-body" style="overflow: auto;height:390px;">
		<div class="container-fluid">
		  <div class="row">
			<ul id="depttreesw" class="ztree"></ul>
		  </div>
		</div>
	  </div>
	  <div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	  </div>
	</div>
  </div>
</div>

<div id="showprovidersw" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
  <div class="modal-dialog" role="document">
	<div class="modal-content">
	  <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		<h4 class="modal-title" id="gridSystemModalLabel">选择供应商</h4>
	  </div>
	  <div class="modal-body" style="overflow: auto;height:390px;">
		<div class="container-fluid">
		  <div class="row">
			<ul id="providertreesw" class="ztree"></ul>
		  </div>
		</div>
	  </div>
	  <div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	  </div>
	</div>
  </div>
</div>

<div class="modal fade" id="switchCfgModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="modal-content" style="width: 760px;">
            <div class="modal-body" style="width: 760px;height: 550px;overflow-y: auto;overflow-x: hidden;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <div class="panel panel-default m-bottom-5" style="width: 730px;">
                    <div class="panel-heading padding-5-5" style="font-weight: bold;">
                    	<input type="text" id="cfgagcodename" style="border: 0px;background-color:transparent;" value="协议配置" readonly/>
                    </div>  
					
					<div class="panel-body">
                        <ul class="nav nav-pills nav-justified">
                            <li class="active"><a href="#depttab" data-toggle="pill" id="outdept">出单网点</a></li>
                            <li><a href="#paytypetab" data-toggle="pill" id="paytype">支付方式</a></li>
                        </ul>
                        <div id="myTabContent" class="tab-content">
                            <!--出单网点选项卡-->
                            <div class="tab-pane fade in active" id="depttab">
                                <br/>
                                <table class="table table-striped" id="deptnameTable" ></table>
                                <label id="deptradios"></label>
                            </div>
                            <!--支付方式选项卡-->
                            <div class="tab-pane fade" id="paytypetab">
                                <table class="table table-striped" id="payTypeTable"></table>
                            </div>
                        </div>
                    </div>
                </div>
                
                <button id="swactbutton" type="button" name="swactbutton" class="btn btn-primary">切换</button>
                <input id="" name="" class="btn btn-primary" type="button" data-dismiss="modal" value="取消"/>
            </div>
        </div>
    </div>
</div>

</body>
</html>
