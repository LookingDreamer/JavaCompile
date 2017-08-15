<!DOCTYPE html>
<html lang ="en" class="fuelux" >   
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>协议管理</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/system/flat-ui-icon.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load" src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<style type="text/css">
		.htmleaf-header{margin-bottom: 15px;font-family: "Segoe UI", "Lucida Grande", Helvetica, Arial, "Microsoft YaHei", FreeSans, Arimo, "Droid Sans", "wenquanyi micro hei", "Hiragino Sans GB", "Hiragino Sans GB W3", "FontAwesome", sans-serif;}
		.htmleaf-icon{color: #fff;}
		tr{height:40px;}
		td{vertical-align: middle;}
		.modal-body{overflow: 200px; height: auto; width:auto;}
		.modal-content{width:320px;}
	</style>
<script type="text/javascript">
	requirejs([ "zzbconf/agreement", "lib/tsearch"]);
</script>
</head>
<body>

<input id="updateid" type="hidden" name="updateid" >
<input type="hidden" id="affiliationorgquery" name="affiliationorg" >

  <div>
	<div class="col-md-3">
	<div class="panel panel-default m-bottom-2" >
	<div class="panel-heading padding-5-5">机构列表</div>
	<div class="panel-body"  >
	  <div><input class="form-control ztree-search" id = "treesearch" data-bind="deptTree" name="treesearch" placeholder="输入机构名称关键字进行搜索" /></div>
	  <div class="ztree" id="deptTree" style="width:100%; height:600px; overflow-y:auto;overflow-x:auto;">正在加载机构数据......</div>

	</div>
	</div>
	</div>

	<div class="col-md-9">
        <div class="panel panel-default m-bottom-2" >
	      <div class="panel panel-default m-bottom-5" style="height:200px;">
	                <div class="panel-heading" style="height:200px;">
							<div class="row" style="padding-top:1px">
							    <input type="hidden" id="comcode" name="comcode" >
							    <div class="col-md-2" align="right">
							        <label for="exampleInputCode"><h4>查询机构:</h4></label>
							    </div>
							    <div class="col-md-3" align="left" >
							       <input class="form-control" id = "comname" onchange="query()" type="text" name="comname" readonly="readonly" placeholder="请选择机构">
							    </div>

                                <input type="hidden" id="queryproviderid" name="providerid" value="${agreement.providerid!''}">
                                <div class="col-md-2" align="right">
                                    <label for="exampleInputCode"><h4>供应商:</h4></label>
                                </div>
                                <div class="col-md-3" align="left" >
                                    <input type="text" class="form-control" id="queryprovidername" name="providename" readonly="readonly" placeholder="请选择供应商"
                                           value="${providername!''}">
                                </div>
							</div>
                        <div class="row" style="padding-top:1px">

                            <div class="col-md-2" align="right">
                                <label for="exampleInputCode"><h4>协议编码:</h4></label>
                            </div>
                            <div class="col-md-3" align="left" >
                                <input type="text"class="form-control " id="queryagreementcode" name="agreementcode"
                                       value="${agreement.agreementcode!''}">
                            </div>

                            <div class="col-md-2" align="right">
                                <label for="exampleInputCode"><h4>协议名称:</h4></label>
                            </div>
                            <div class="col-md-3" align="left" >
                                <input type="text" class="form-control " id="queryagreementname" name="agreementname"
                                       value="${agreement.agreementname!''}">
                            </div>
                        </div>
                        <div class="row" style="padding-top:1px">
                            <div class="col-md-2" align="right">
                                <label for="exampleInputCode"><h4>区域网点关联 :</h4></label>
                            </div>
                            <div class="col-md-3" align="left" >
                                <select class="form-control" id="provinceadd1" name="province"
                                        onchange="changcityadd1('scope')" placeholder="请选择 ">
                                </select>
                            </div>
                            <div class="col-md-2" align="right">
                                <label for="exampleInputCode"><h4>协议类型 :</h4></label>
                            </div>
                            <div class="col-md-3" align="left" >
                                <select class="form-control" id="agreementtype" name="agreementtype">
                                    <option value="-1">全部</option>
                                    <option value="0">内部协议</option>
                                    <option value="1">外部协议</option>
                                </select>
                            </div>
                        </div>
                        <div class="row" style="padding-top:1px">
                            <div class="col-md-4" align="right" >
                                <button type="button"  onclick="flushReset()"
                                        class="btn btn-primary">刷新协议规则项</button>
                            </div>
                            <div class="col-md-4" align="right" >
                                <button id="queryAgreement" type="button" name="queryAgreement"  onclick="queryAgreement()"
                                        class="btn btn-primary">查询</button>
                            </div>
                            <div class="col-md-4" align="left" >
                                <button type="button"  onclick="queryReset()"
                                        class="btn btn-primary">重置</button>
                            </div>
                        </div>
                        <div class="row" style="padding-top:1px;padding-left:100px" id="provincecode1">

                        </div>
                        <div class="row" style="padding-top:1px">

                        </div>
					</div>
					<div class="panel-body" style="height:50px;" >
							<div class="row">
								<div class="col-md-12">
								   <form name="myform" method="post" >
<!-- 										<div class="form-group col-md-3 form-inline"> -->
<!-- 											<div class="checkbox"> -->
<!-- 											    <input type="hidden" id="shang" name="shang" > -->
<!--                                              <label for="exampleInputCode"><input type="checkbox" name="checkbox" id="shang_input" onchange="query()" value="1" >上级协议</label>  -->
<!--                                              </div> -->
<!-- 										</div> -->
										<div class="form-group col-md-3 form-inline">
										     <div class="checkbox">
                                                <label for="exampleInputCode"><input type="checkbox" name="checkbox" id="ben_input" onchange="query()" value="2" checked>本级协议</label>
												<input type="hidden" id="ben" name="ben" >
                                             </div>
										</div>
										<div class="form-group col-md-3 form-inline">
											<div class="checkbox">
                                                <label for="exampleInputCode"><input type="checkbox" name="checkbox" id="xia_input" onchange="query()" value="3">下级协议</label>
												<input type="hidden" id="xia" name="xia" >
                                             </div>
										</div>
										<#--<div class="form-group col-md-3 form-inline">
											<button id="querybutton" type="button" name="querybutton" style="color:#337ab7" onclick="query()">查询</button>
										</div>-->
									</form>
								</div>
							</div>
						</div>
					</div>
			      <div class="panel panel-default">
						<div class="panel-heading padding-5-5">
							<div class="row" >
								<div class="col-md-2"><h4>结果</h4></div>
								<div class="col-md-10" align="right">
									   <a href="#jbxx"><button id="addagreement" type="button" name="addagreement"  onclick="addagreement_agreement()"
					                            class="btn btn-primary">新增</button></a>
									  <button id="deletedagreement" type="button" name="deletedagreement" style="display:none" onclick="deletedagreement()"
								                class="btn btn-primary">删除</button>
									   <button id="startagreement" type="button" name="startagreement" onclick="startagreement()"
						                        class="btn btn-primary">启动</button>
									   <button id="endagreement" type="button" name="endagreement" onclick="endagreement()"
						                        class="btn btn-primary">关闭</button>
                                    <button id="setUnderwritestatusOff" type="button" name="setUnderwritestatusOff" onclick="setUnderwritestatus(0)"
                                            class="btn btn-primary">关闭核保</button>
                                    <button id="setUnderwritestatusOn" type="button" name="setUnderwritestatusOn" onclick="setUnderwritestatus(1)"
                                            class="btn btn-primary">开启核保</button>
							    </div>
							</div>

						</div>
                      <div class="row">
                          <div class="col-md-12 " style="height：22px;">
                              <table id="table-agreement" data-height="300"></table>
                          </div>
                      </div>
		           </div>
	     <div class="panel-heading padding-5-5">
	        <button id="defultclose" class="btn" data-toggle="collapse" data-parent="#panelOne" href="#collapseOne">
		               协议详情</button>
	     </div>
	          <div class="panel-collapse collapse" id="collapseOne">
				<#include "zzbconf/agreementdetail.ftl"/>
			  </div>
	    </div>
        </div>
	</div>
  </div>


<div id="showdept1" class="modal fade" role="dialog"
     aria-labelledby="myModalLabel" data-backdrop="false"
     data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog" role="document" style="height: 400px;wit">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="gridSystemModalLabel1">选择机构</h4>
            </div>
            <div class="modal-body" style="overflow: auto; height: 400px;">
                <div class="container-fluid">
                    <div class="row">
                        <ul id="depttree1" class="ztree">正在加载机构数据......</ul>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<div id="showQueryPro" class="modal fade" role="dialog"
     aria-labelledby="myModalLabel" data-backdrop="false"
     data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">选择供应商</h4><br/>
                <div><input class="form-control ztree-search" id = "queryProTreesearch" data-bind="queryProTree" name="treeDemosearch" placeholder="输入供应商名称进行搜索" /></div>
            </div>
            <div class="modal-body" style="overflow: auto; height: 400px;">
                <div class="container-fluid">
                    <div class="row">
                        <div id="queryProTree" class="ztree">正在加载供应商数据......</div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
