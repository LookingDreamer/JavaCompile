<!DOCTYPE html>
<html lang="en" class="fuelux">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>渠道协议管理子页面</title>
    <script type="text/javascript">
        requirejs([ "zzbconf/newchannelagreementsub" ], function () {
            require([  "jquery", "zTree", "zTreecheck", "flat-ui", "bootstrap-table", "fuelux", "bootstrap", "bootstrapTableZhCn", "public", "ajaxfileupload"], function ($) {
                $(function () {
                    initchannelagreementScript();
                    treesearch();
                });
            });
        });
    </script>
    <style>
        .table {
            margin-bottom: 10px;
        }

        .graylb {
            background-color: #AAA;
        }

        textarea {
            width: 90%;
            height: 85%;
        }
        
        .btn-file {
		    position: relative;
		    overflow: hidden;
		}
		.btn-file input[type=file] {
		    position: absolute;
		    top: 0;
		    right: 0;
		    min-width: 100%;
		    min-height: 100%;
		    font-size: 100px;
		    text-align: right;
		    filter: alpha(opacity=0);
		    opacity: 0;
		    outline: none;
		    background: white;
		    cursor: inherit;
		    display: block;
		}
    </style>
</head>
<body>

<input type="hidden" id="providerid"/>
<input type="hidden" id="deptid"/>
<input type="hidden" id="agreeid"/>
<input type="hidden" id="channelid" name="channelid" value="${channelinfo.channel.id}"/>
<input type="hidden" id="agreementid" name="agreementid" value="${channelinfo.channelagreement.id}"/>
<!--<input type="hidden" name="deptid" value="${channelinfo.channel.deptid}"/>
	<input type="hidden" name="channelid" value="${channelinfo.channel.id}"/>-->
<input type="hidden" name="soulagreeid" id="soulagreeid"/>

<input type="hidden" name="deptid5" id="deptid5"/>

<div class="panel panel-default">
    <div class="panel-body">
        <ul class="nav nav-pills nav-justified">
            <li class="active"><a href="#providertab" data-toggle="pill">供应商管理</a></li>
            <li><a href="#interfacetab" data-toggle="pill">权限管理</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane fade in active" id="providertab">
                <div class="panel panel-default m-bottom-2">
                    <div class="panel-heading padding-5-5"><h4 class="titlelabel">供应商管理</h4></div>
                    <br>
                    <button id="savebutton" type="button" class="btn btn-primary" onclick="addprovider()">新增供应商</button>
                    <button class="btn btn-primary" type="button" name="refresh" title="Refresh" id="refresh">
						<i class="glyphicon glyphicon-refresh icon-refresh">刷新</i>
					</button>
					<span class="btn btn-primary btn-file">	
	    				批量导入<input type="file" id="prvFile" name="prvFile"> 
					</span>
					<a href="/cm/channel/dlPrvTempFile" class="btn btn-primary">导入模板下载</a>
                    <button class="btn btn-primary" type="button" name="copybutton"
                            title="copy" id="copybutton"> 复制
                    </button>
                    <table class="table table-striped" id="deptListTable"></table>
                    <br>

                </div>
                <div class="panel panel-default" style="display:none;" id="editprovider">
                    <div class="panel-heading padding-5-5 form-group col-md-12 form-inline">
                        <h3 class="panel-title">
                            <label for="disprv">编辑供应商：
                                <input id="disprv" type="text" class="form-control" disabled/>
                            </label>
                        </h3>
                    </div>
                    <div class="panel-body">
                        <ul class="nav nav-pills nav-justified">
                            <li class="active"><a href="#depttab" data-toggle="pill" id="outdept">出单网点</a></li>
                            <li><a href="#paytypetab" data-toggle="pill" id="paytype">支付方式</a></li>
                            <li><a href="#distributiontab" data-toggle="pill" id="distribution">配送方式</a></li>
                        </ul>
                        <div id="myTabContent" class="tab-content">
                            <!--出单网点选项卡-->
                            <div class="tab-pane fade in active" id="depttab">
                                <br/>
                                <button id="savebutton" type="button" class="btn btn-primary"
                                        onclick="updept()">修改网点
                                </button>
                                <table class="table table-striped" id="deptnameTable"></table>
                            </div>
                            <!--支付方式选项卡-->
                            <div class="tab-pane fade" id="paytypetab">
                                <table class="table table-striped" id="payTypeTable"></table>
                                <input class="btn btn-primary" type="button" id="openpaytype" value="保存"/>
                                <input class="btn btn-primary" type="button" id="closepaytype" value="关闭" style="display: none"/>
                            </div>
                            <!--配送方式选项卡-->
                            <div class="tab-pane fade" id="distributiontab">
                                <form id="distributionform">
                                    <table class="table table-bordered">
                                        <tr>
                                            <td>
                                                <input type="checkbox" name="selfdistritype" id="selfdisttype"
                                                       value="1">自取
                                            </td>
                                            <td colspan="2" style="padding-bottom: 0px;">
                                                <label>说明 :</label>
                                                <textarea name="selfnoti" id="selfnoti"></textarea>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td rowspan="3">
                                                <input type="checkbox" name="distdistritype" id="distdisttype"
                                                       value="2">快递 &nbsp;&nbsp;&nbsp;
                                            </td>
                                            <td colspan="2">快递公司
                                                <select name="distrcompany" id="distrcompany">
                                                    <option value="0">请选择快递公司</option>
                                                    <option value="1">顺丰快递</option>
                                                    <option value="2">申通快递</option>
                                                    <option value="3">中通快递</option>
                                                    <option value="4">圆通快递</option>
                                                    <option value="5">宅急送</option>
                                                    <option value="6">邮政EMS</option>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>收费方式 : <br>
                                                <input type="radio" name="distpaytype"  id="distpaytype1" value="1"> 到付<br>
                                                <input type="radio" name="distpaytype"  id="distpaytype2" value="2"> 包邮<br>
                                                <input type="radio" name="distpaytype"  id="distpaytype3" value="3"> 预收&nbsp;&nbsp;
                                                <input type="text" name="chargefee" id="chargefee">
                                            </td>
                                            <td>
                                                <label>说明 :</label>
                                                <textarea name="distnoti" id="distnoti"></textarea>
                                            </td>
                                        </tr>
                                    </table>
                                    <input type="hidden" name="providerid" id="distproviderid"/>
                                    <input type="hidden" name="deptid" id="distdeptid"/>
                                    <input type="hidden" name="agreementid" id="distagreementid"/>
                                    <input class="btn btn-primary" type="button" id="savedistribution" value="保存"/>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- providertab div-->
            <div class="tab-pane fade" id="interfacetab">
                <div class="panel panel-default">
                    <div class="panel-heading"><font size="4">权限管理</font>
                   
                    <button id="bashCloseInterface" type="button" name="bashCloseInterface" style="float:right;margin-left: 4px;"
                                  class="btn btn-primary">批量关闭</button>
                    <button id="bashOpenInterface" type="button" name="bashOpenInterface" style="float:right;"
                                  class="btn btn-primary">批量开启</button>
              <!--      <input class="btn btn-primary" type="button" id="bashOpenInterface" style="margin-left:800px" value="批量开启"/>
                    <input class="btn btn-primary" type="button" id="bashCloseInterface" style="margin-left:1px" value="批量关闭"/>-->
                   </div>
                    
                    <div class="panel-body">
                        <table class="table table-striped" id="interfacesTable"></table>
                    </div>
                </div>

                <div class="panel panel-default" style="display:none;" id="editInterface">
                    <div class="panel-heading padding-5-5 form-group col-md-12 form-inline">
                        <h3 class="panel-title">
                            <label for="disInterface">&nbsp;&nbsp;接口详情：
                                <input id="disInterface" type="text" class="form-control" disabled/>
                            </label>
                        </h3>
                    </div>
                    <div class="panel-body">
                        <div class="tab-content">
                            <form id="interfaceform">
                                <table class="table table-bordered">
                                    <tr>
                                        <td style="width:15%; text-align:center;">是否开通</td>
                                        <td>
                                            <input type="radio" name="check" id="intfOpen" value="1"> 开启 &nbsp;&nbsp;&nbsp;&nbsp;
                                            <input type="radio" name="check" id="intfClose" value="0"> 关闭
                                            <span id="platinfo" style="display: none">
                                                <br/>
                                                <input type="checkbox" name="pv1" id="firstInsureType" value="1">投保类型
                                                <br/>
                                                <input type="checkbox" name="pv2" id="claimtimes" value="1">出险次数
                                                <br/>
                                                <input type="checkbox" name="pv3" id="lastYearPolicyInfo" value="1">上年保单信息
                                                <br/>
                                                <input type="checkbox" name="pv4" id="claimesInfo" value="1">出险信息
                                            </span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:15%; text-align:center;">是否免费</td>
                                        <td>
                                            <input type="radio" name="isfree" id="intfFree" value="1"> 收费 &nbsp;&nbsp;&nbsp;&nbsp;
                                            <input type="radio" name="isfree" id="intfNotFree" value="0"> 免费
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:15%; text-align:center;">免费使用次数</td>
                                        <td><input type="text" name="monthfree" id="monthfree" class="form-control" style="width: 200px;"/></td>
                                    </tr>
								<#--<tr>
                                    <td style="width:15%; text-align:center;">每次费用</td>
                                    <td><input type="text" name="perfee" id="perfee" class="form-control" style="width: 200px;"/></td>
                                </tr>-->
                                    <tr>
                                        <td style="width:15%; text-align:center;">免费次数用完后能否继续使用</td>
                                        <td><input type="radio" name="extendspattern" value="0"> 收费使用 &nbsp;&nbsp;&nbsp;&nbsp;
                                            <input type="radio" name="extendspattern" value="1" checked="checked"> 禁止使用</td>
                                    </tr>
								<#--<tr>
									<td style="width:15%; text-align:center;">收费规则</td>
									<td><table class="table table-striped" id="interChargeTable"></table></td>
								</tr>-->
                                </table>
							<#--<input class="btn btn-primary" type="button" id="interfacechargeadd" value="新增收费规则"/>-->
                                <input class="btn btn-primary" type="button" id="interfacesave" value="保存"/>
                                <input type="hidden" name="channelinnercode" id="channelinnercode" value="${channelinfo.channel.channelinnercode}"/>
                                <input type="hidden" name="agreementinterfaceid" id="agreementinterfaceid"/>
                                <input type="hidden" name="interfaceid" id="interfaceid"/>
                            </form>
                        </div>
                    </div>
                </div>

            </div>
        </div>

    </div>
</div>
<!-- 模态框 -->
<div class="modal fade" id="providersource" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     data-backdrop="false" data-keyboard="false" aria-hidden="true" >
    <div class="modal-dialog" style="width: 700px;">
        <div class="modal-content" id="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <div class="modal-body " style="overflow: auto; height: 500px;">
                    <div class="form-group col-md-10 form-inline">
                        <label for="exampleInputCode">查询机构</label>
                        <input id="province" value="${dept}" class="form-control" disabled/><br/>
					<#list provider as providers>
                        <input type="checkbox" name="provider" id="provider${providers_index}" value="${providers.id}"
							   <#if checkedProvider?seq_contains(providers.id + "#" + providers.agreeid)>checked="checked"</#if>/>${providers.prvname}(${providers.agreementname})
                        <input type="hidden" name="agreeid" id="agreeid${providers_index}"
                               value="${providers.agreeid}"></input><br/>
					</#list>
                    </div>
                    <button class="btn btn-primary" type="button" id="checkProid" title="showAll">确定</button>
                    <button class="btn btn-primary" type="button" id="closeProid">取消</button>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="deptsource" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="false"
     data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <div class="modal-body " style="overflow: auto;  height: 400px;">
                    <div class="form-group col-md-8 form-inline">
                        <label for="exampleInputCode">供应商</label>
                        <input id="providers" class="form-control" disabled/><br/>
                        <label id="providerRead"></label>
                    </div>
                    <button class="btn btn-primary" type="button" id="checkdeptname" title="showAll">确定</button>
                    <button class="btn btn-primary" type="button" id="closedept">取消</button>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="chargesource" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog" style="width: 730px;">
        <div class="modal-content" id="modal-content" >
            <div class="modal-header" >
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <div class="panel panel-default m-bottom-5" style="width: 680px;">
                    <div class="panel-heading padding-5-5" style="font-weight: bold;">月支付转化率</div>
                    <div class="panel-body">
                        <form id="chargesourceform">
                            <input type="hidden" id="agreeinterfaceid" name="agreeinterfaceid"/>
                            <table class="table-no-bordered">
                                <tr>
                                    <td class="padding-5-5">
                                        <select class="form-control" style="width: 65px;" id="converstartcompar" name="converstartcompar">
                                            <option value="<">&lt;</option>
                                            <option value="<=">&lt;=</option>
                                            <option value="=">=</option>
                                            <option value=">">&gt;</option>
                                            <option value=">=" selected>&gt;=</option>
                                        </select>
                                    </td>
                                    <td class="padding-5-5"><input class="form-control" style="width: 80px;" type="text" id="converstart" name="converstart"/></td>
                                    <td class="padding-5-5">%&nbsp;&nbsp;&nbsp;&nbsp;并且&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                    <td class="padding-5-5">
                                        <select class="form-control" style="width: 65px;" id="converendcompar" name="converendcompar">
                                            <option value="<">&lt;</option>
                                            <option value="<=" selected>&lt;=</option>
                                            <option value="=">=</option>
                                            <option value=">">&gt;</option>
                                            <option value=">=">&gt;=</option>
                                        </select>
                                    </td>
                                    <td class="padding-5-5"><input class="form-control" style="width: 80px;" type="text" id="converend" name="converend"/></td>
                                    <td class="padding-5-5">%，收费</td>
                                    <td class="padding-5-5"><input class="form-control" style="width: 80px;" type="text" id="charge" name="charge"/></td>
                                    <td class="padding-5-5">元/笔</td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="panel-footer">
                        <input id="interChargeSaveBt" name="interChargeSaveBt" type="button" class="btn btn-primary" value="确定"/>
                        <input id="interChargeCancelBt" name="interChargeCancelBt" type="button" class="btn btn-primary" value="取消"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 供应商复制 -->
<div class="modal fade" id="copyProviderModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" id="modal-content" style="width: 576px;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span
                        class="sr-only">Close</span></button>
                <div class="panel panel-default m-bottom-5" style="width: 544px;">
                    <div class="panel-heading padding-5-5" style="font-weight: bold;"><input type="text"
                                                                                             id="conditionTip"
                                                                                             style="border: 0px;background-color:transparent;"
                                                                                             value="供应商复制" readonly/>
                    </div>
                    <div class="panel-body">
                        <div class="panel panel-default m-bottom-2">
                            <div class="panel-heading padding-5-5">渠道列表</div>
                            <div class="panel-body">
                            <div><input class="form-control ztree-search" id ="tresearch" data-bind="channelModalTree" name="treesearch" placeholder="输入渠道名称关键字进行搜索" />
                			</div>
                                <div class="ztree" id="channelModalTree" style="width:100%; height:500px; overflow-y:auto;overflow-x:auto;">
                                    正在加载渠道数据......
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input id="execCopyButton" name="execCopyButton" type="button" class="btn btn-primary" value="复制"/>
                        <input id="closeCopyButton" name="closeCopyButton" class="btn btn-primary" type="button"
                               data-dismiss="modal" value="取消"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
