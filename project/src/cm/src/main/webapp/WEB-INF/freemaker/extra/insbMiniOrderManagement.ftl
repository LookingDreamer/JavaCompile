
<div class="container-fluid">
    <div class="panel panel-default" id="msimplequery1" style="display: none;">
        <div class="panel-heading">
            <div class="row">
                <div class="col-md-7">
                    <ul class="nav nav-pills">
                        <li role="presentation" <#if "${queryModel.taskState}" == '0' || "${queryModel.taskState}" == ''>class="active" </#if> onclick="checkthisstatus(this,'')"><a href="#">全部</a></li>
					<#list statusList as status>
                        <li role="presentation" <#if "${status.codevalue}" == "${queryModel.taskState!'' }">class="active" </#if> onclick="checkthisstatus(this,${status.codevalue })"><a href="#">${status.codename }</a></li>
					</#list>
                    </ul>
                </div>
                <div class="col-md-3" align="right" >
                    <div class="form-group" style="margin-bottom:0px">
                        <input type="text" class="form-control" id="mnameorcarno" name="nameorcarno" placeholder="请输入车牌" value="${queryModel.carLicenseNoorcname!'' }">
                        <br/>
                        <button id="msimplequerybutton" type="button" name="simplequerybutton" class="btn btn-primary">搜索</button>
                        <button id="mresetbutton" type="button" name="resetbutton" class="btn btn-primary">重置</button>
                        <button id="mseniorsearch" type="button" name="seniorsearch" class="btn btn-primary">高级搜索</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="panel panel-default" id="mseniorquery">
        <div class="panel-heading">
            <div class="row">
                <div class="col-md-11">高级搜索</div>
                <div class="col-md-1" style="display:none">
                    <button id="mclosesearch" type="button" name="closesearch" class="btn btn-primary btn-sm">关闭</button>
                </div>
            </div>
        </div>
        <div class="panel-body">
            <div class="row" style="margin-left:-35px;">
                <div class="col-md-12">
                    <form role="form" id="mqueryorder" action="queryorderlist" method="post">
                        <div class="form-group form-inline col-md-4" style="display:none">
                            <label for="exampleInputCode">agentID</label> <input type="text"
                                                                               class="form-control m-left-5" id="magentid" name="agentid" placeholder="" value="${queryModel.agentid!'' }">
                        </div>
                        <div class="form-group form-inline col-md-4" style="display:none">
                            <label for="exampleInputCode">channelUserId</label> <input type="text"
                                                                                 class="form-control m-left-5" id="mchannelUserId" name="channelUserId" placeholder="" value="${queryModel.channelUserId!'' }">
                        </div>
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputCode">车主</label> <input type="text"
                                                                               class="form-control m-left-5" id="minsureName" name="insureName" placeholder="" value="${queryModel.insureName!'' }">
                        </div>
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputName">供应商</label>
                            <input type="hidden" id="mprvId" name="prvId" value="${queryModel.prvId!'' }">
                            <input type="text" value="${queryModel.providername!'' }"	class="form-control m-left-2" id="mprovidername" name="providername" placeholder="请选择" readonly="readonly">
                            <a id="mcheckprovider">选择</a>
                        </div>
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputCode">车牌号码</label> <input type="text"
                                                                              class="form-control m-left-5" id="mcarLicenseNo" name="carLicenseNo" placeholder="" value="${queryModel.carLicenseNo!'' }">
                        </div>
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputCode">业务跟踪号</label>
                            <input type="text"	class="form-control m-left-5" id="mtaskId" name="taskId" placeholder="" value="${queryModel.taskId!'' }">
                        </div>
                        <!-- <div class="form-group form-inline col-md-4" style="display:none">
                            <label for="exampleInputName">联系电话</label> <input type="text"
                                                                              class="form-control m-left-5" id="mphone" name="phone" placeholder="" value="${queryModel.phone!'' }">
                        </div> -->
                        <div class="form-group form-inline col-md-4">
                            <label for="exampleInputOrgName">订单状态&nbsp;&nbsp;&nbsp;&nbsp;</label>
                            <select class="form-control" id="mtaskState" name="taskState">
                                <option value="">全部</option>
							<#list statusList as status>
                                <option value="${status.codevalue }" <#if "${status.codevalue}" == "${queryModel.taskState!'' }">selected </#if> >${status.codename }</option>
							</#list>
                            </select>
                        </div>
                        <div class="form-group form-inline col-md-4" style="display:none">
                            <label for="exampleInputOrgName">用户类型</label>
                            <select class="form-control m-left-2" id="musertype" name="usertype">
                                <option value="">全部</option>
                                <option value="2">正式用户</option>
                                <option value="1">试用用户</option>
                            </select>
                        </div>
                        <div class="form-group form-inline col-md-4" style="display:none">
                            <label for="exampleInputCode">支付流水号&nbsp;&nbsp;&nbsp;</label>
                            <input type="text" class="form-control" id="mpaymentTransaction" name="paymentTransaction" placeholder="" value="${queryModel.paymentTransaction!'' }">
                        </div>
                        <div class="form-group form-inline col-md-4" style="display:none">
                            <label for="exampleInputName">代理人工号</label> <input type="text"
                                                                               class="form-control" id="magentcode" name="agentcode" placeholder="" value="${queryModel.agentcode!'' }">
                        </div>
                        <div class="form-group form-inline col-md-4" style="display:none">
                            <label for="exampleInputCode">业务完成关闭者</label>
                            <input type="text" class="form-control" id="mshutter" name="shutter" placeholder="">
                        </div>
                        <div class="form-group form-inline col-md-10">
                            <label for="exampleInputCode">任务创建时间</label>
                            <input type="text"	class="form-control form_datetime" id="mcreateTimeStart" name="createTimeStart" readonly placeholder="" value="${queryModel.createTimeStart!'' }"> -
                            <input type="text"	class="form-control form_datetime" id="mcreateTimeEnd" name="createTimeEnd" readonly placeholder="" value="${queryModel.createTimeEnd!'' }">
                        </div>
                        <div class="form-group form-inline col-md-2">
                            <input type="hidden" id="mcurrentPage" name="currentPage" value="${allData.currentPage!"0" }">
                            <input type="hidden" id="mcarLicenseNoorcname" name="carLicenseNoorcname" value="">
                            <button id="mseniorquerybutton" type="button" name="seniorquerybutton" class="btn btn-primary">搜索</button>&nbsp;
                            <button id="mseniorresetbutton" type="button"  class="btn btn-primary">重置</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="panel panel-default">
        <div class="panel-body">
            <div class="row">
                <div class="col-md-12">
				<#list allData.rowList as row>
                    <table class="table table-bordered" style="border: 2px solid gray;">
                        <tr>
                            <td valign="middle" bgcolor="#428BCA" width="45%" class="showright"><label>车牌：${row.carLicenseNo!"" }</label><br>
                                <label class="">车主：${row.insureName!"" }</label>
								</td>
                            <td valign="middle"><label>商业险：${row.bizPremium!"" } </label>
                                <label class="m-left-5">保单号：${row.bizPolicyNo!"" }</label><br>
                                <label>交强险：${row.efcPremium!"" }</label>
                                <label class="m-left-5">保单号：${row.efcPolicyNo!"" }</label>
                            </td>
                        </tr>
                        <tr>
                            <td><label>业务跟踪号:${row.taskId!"" }</label></td>
                            <td><label>订单创建时间：${row.createTime!"" }</label></td>
                        </tr>
                        <tr>
                            <td><label>供应商:${row.prvName!"" }</label></td>
                            <td align="right">
                                <div style="float: left;">
                                    <label>订单状态: ${row.taskStateDescription!"" }</label>&nbsp;&nbsp;&nbsp;
                                </div>
                                <div style="float: right;"><button id="mquerybutton" type="button" name="querybutton" class="btn btn-primary" onclick="searchorderdetail('${row.taskId}','${row.taskStateDescription }');">查看详情</button></div>
                            </td>
                        </tr>
                    </table>
				</#list>
                </div>
                <form hidden="true" name="detailinfo" id="mdetailinfo" action="queryorderdetail" method="POST">
                    <input type="text" name="taskId" id="dtaskId" />
                    <input type="text" name="pid" id="mpid"/>
                    <input type="text" name="agentid" id="dagentid"/>
                    <input type="text" name="codename" id="mcodename">
                </form>
            </div>
        </div>
        <div class="panel-footer">
            <div class="row">
                <div class="col-md-12">
                    <div class="col-md-8">
                        共<label id="mlblToatl">${allData.total!"0" }</label>条数据 第[<label id="mlblCurent">${allData.currentPage!"0" }</label>]页/共[<label
                            id="mlblPageCount">${allData.totalPage!"0" }</label>]页
                    </div>
                    <div class="col-md-4">
                        跳转到：第<select name="goto" id="mgoto"
                                     onchange="go(this.options[this.options.selectedIndex].value)">
					<#if allData.totalPage??>
						<#list 1..allData.totalPage as i>
                            <option value="${i}" <#if "${allData.currentPage}" == "${i}">selected </#if>>${i}</option>
						</#list>
					<#else>
                        <option value="1">1</option>
					</#if>
                    </select>页 <a id="mfirst" href="#">首页</a> <a id="mprevious" href="#">上一页</a> <a
                            id="mnext" href="#"> 下一页</a> <a id="mlast" href="#">末页</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="mshowprovider" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel" data-backdrop="false" data-keyboard="false" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="mgridSystemModalLabel">选择供应商</h4>
            </div>
            <div class="modal-body" style="overflow: auto;height:390px;">
                <div class="container-fluid">
                    <div class="row">
                        <ul id="mprovidertree" class="ztree"></ul>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>