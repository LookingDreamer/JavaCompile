<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>协议管理新增/修改</title>
<link href="${staticRoot}/css/appmodule.css" rel="stylesheet">
<link href="${staticRoot}/css/system/flat-ui-icon.css" rel="stylesheet">
<script data-ver="${jsver}" data-main="${staticRoot}/js/load"
	src="${staticRoot}/js/lib/require.js?ver=${jsver}"></script>
<script type="text/javascript">
	requirejs([ "zzbconf/agreementdetail" , "lib/tsearch"]);
</script>
<body>
    <div class="panel panel-default m-bottom-5" style="width:100%; height:465px; overflow-y:auto;overflow-x:auto;">
        <div class="panel-heading padding-5-5">
            <ul class="nav nav-pills nav-justified">
                <li role="presentation" id="jbxx" class='active'><a href="#"
                    onclick="showdiv('jbxx')">基本信息</a></li>
                <li role="presentation" id="yyfw"><a href="#"
                    onclick="showdiv('yyfw')">应用范围</a></li>
                <li role="presentation" id="cdwd"><a href="#"
                    onclick="showdiv('cdwd')">出单网点</a></li>
                <li role="presentation" id="psfs"><a href="#"
                    onclick="showdiv('psfs')">配送方式</a></li>
                <li role="presentation" id="ksxb"><a href="#"
                    onclick="showdiv('ksxb')">快速续保</a></li>
            </ul>
        </div>

        <div class="panel-body" style="padding:2px;">

            <div class="panel-body haoba" id="jbxxdiv" style="display: block;padding:2px;">
                <div class="alert alert-danger alert-dismissible" id="agreementerror" role="alert" style="display: none;"></div>
                <form action="" id="agreementform" name="agreementform">
                    <div class="row" id="agreementdiv">
                        <div class="form-group col-md-4 form-inline">
                            <label for="exampleInputCode">协议编码</label>
                            <input type="hidden" id="agreementid" name="agreementid" value="${agreement.id!''}">
                            <input type="text"class="form-control " id="agreementcode" name="agreementcode"
                                value="${agreement.agreementcode!''}">
                        </div>
                        <div class="form-group col-md-4 form-inline">
                            <label for="exampleInputCode">协议名称</label> <input type="text"
                                class="form-control " id="agreementname" name="agreementname"
                                value="${agreement.agreementname!''}">
                        </div>
                        <div class="form-group col-md-4 form-inline">
                            <label for="exampleInputCode">&nbsp;&nbsp;&nbsp;供应商</label>
                            <input type="hidden" id="providerid" name="providerid" value="${agreement.providerid!''}">
                            <input type="text" class="form-control" id="providename" name="providename" readonly="readonly" placeholder="请选择供应商"
                                value="${providername!''}">
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-md-4 form-inline">
                            <label for="exampleInputCode">关联机构</label> <input type="hidden"
                                id="deptid" name="deptid" value="${agreement.deptid!''}">
                            <input type="text" class="form-control" id="deptname"
                                name="deptname" readonly="readonly" value="${deptname!''}"
                                placeholder="请选择 机构">
                        </div>
                        <div class="form-group col-md-4 form-inline">
                            <label for="exampleInputCode">车险规则</label>
                            <input type="hidden"
                                id="agreementtruleid" name="agreementtruleid" value="">
                            <input type="text" class="form-control" id="agreementtrulename"
                                name="agreementtrulename" readonly="readonly" value=""
                                placeholder="请选择规则">
                            <!--<select class="form-control selectpicker" id="agreementtrule" name="agreementtrule">
                            <#if ruleList2?exists>
                                <#list ruleList2 as rule2>
                                    <#if rule2.ck=="1">
                                    <option selected="selected"  value="${rule2.rule_engine_id}">${rule2.rule_base_postil }</option>
                                    <#else>
                                    <option  value="${rule2.rule_engine_id }">${rule2.rule_base_postil }</option>
                                    </#if>
                                </#list>
                            </#if>
                            </select>	-->
                        </div>
                        <div class="form-group col-md-4 form-inline">
                            <label for="exampleInputCode">规则报价是否启用</label><br>
                            <select class="form-control selectpicker" name="agreementrule" id="agreementrule">
                              <option>无</option>
                              <option value="1" <#if "${agreement.agreementrule}" == "1">selected="selected" </#if>>启用</option>
                                <!-- <option value="0" <#if "${agreement.agreementrule}" == "0">selected="selected" </#if>>禁用</option>-->
                            </select>
                        </div>
                        <div class="form-group col-md-4 form-inline">
                            <label for="exampleInputCode">协议类型</label>
                            <select class="form-control selectpicker" name="agreementType" id="agreementType">
                                <option value="0">内部协议</option>
                                <option value="1">外部协议</option>
                            </select>
                        </div>
                    </div>
                    <div class="panel-footer" align="center">
                        <button id="savebutton" type="button" name="savebutton"
                            class="btn btn-primary" onclick="agreementadd()">保存</button>
                    </div>
                </form>
            </div>
            <div class="panel-body haoba" id="yyfwdiv" style="display: none;padding:2px;">
                <div class="form-group col-md-12 form-inline" id="provincecode">
                    <label for="exampleInputCode">区域网点关联 </label> <select
                        class="form-control" id="provinceadd" name="province"
                        onchange="changcityadd('scope')" placeholder="请选择 ">
                    </select>
                    <button id="savebuttonbycity" type="button" name="savebuttonbycity"
                            class="btn btn-primary" onclick="savebuttonbycity()">一键关联</button>
                    <!--<select class="form-control " id="cityadd" name="city">
                    </select>onchange="changprovinceadd()"-->
                </div>
                <div class="form-group col-md-12  form-inline">
                    <label for="exampleInputCode">关联网点</label>
                        <div class="form-group" id="deptid2" name="deptid">
                        <select class="form-control" style="width:130px;" id="deptidc1" name="deptid1"
                            onchange="deptconnchange(1)">
                            <option value="" selected="true" >请选择</option>
                        </select>
                        <select class="form-control" style="width:180px;" id="deptidc2" name="deptid2"
                            onchange="deptconnchange(2)">
                        </select>
                        <select class="form-control" style="width:180px;" id="deptidc3" name="deptid3"
                            onchange="deptconnchange(3)">
                        </select>
                        <select class="form-control" style="width:180px;" id="deptidc4" name="deptid4"
                            onchange="deptconnchange(4)">
                        </select>
                        <select class="form-control" style="width:200px;" id="deptidc5" name="deptid5">
                        </select>
                        <button id="savebutton" type="button" name="savebutton"
                            class="btn btn-primary" onclick="savescop()">关联</button>
                    </div>
                </div>
                <div class="form-group col-md-12 form-inline" id="guanliandept">
                    <!--	<label for="exampleInputCode"><h4>已关联区域</h4></label>  -->
                </div>
                <div class="form-group col-md-12 form-inline">
                        <label for="exampleInputCode">已关联网点</label>
                        <table id="table-fw"></table>
                    <div class="col-md-12" align="center">
                        <button id="savebutton" type="button" name="backbutton"
                            class="btn btn-primary" onclick="deletescope()" >取消关联</button>
                    </div>
                </div>
            </div>
            <div class="panel-body haoba" id="cdwddiv" style="display: none;padding:2px;">
                <div class="form-group col-md-12 form-inline">
                    <label for="exampleInputCode">选择网点</label>
                    <div class="form-group" id="deptchose">
                        <select class="form-control" style="width:130px;" id="deptids1"
                            onchange="deptchange(1)">
                            <option value="" selected="true" >请选择</option>
                        </select> <select class="form-control" style="width:180px;" id="deptids2"
                            onchange="deptchange(2)">
                        </select> <select class="form-control" style="width:180px;" id="deptids3"
                            onchange="deptchange(3)">
                        </select> <select class="form-control" style="width:180px;" id="deptids4"
                            onchange="deptchange(4)">
                        </select> <select class="form-control" style="width:200px;" id="deptids5">
                        </select>
                        <button id="savebutton" type="button" name="savebutton"
                            class="btn btn-primary" onclick="addoutorderdept()">关联</button>
                    </div>
                </div>
                <div class="form-group col-md-12">
                    <label for="exampleInputCode">已关联网点</label>
                        <table id="table-wd"></table>
                    <div class="col-md-12" align="center">
                        <button id="savebutton" type="button" name="backbutton"
                            class="btn btn-primary" onclick="deleteoutdept()">取消关联</button>
                    </div>
                </div>
            </div>

            <div class="panel-body haoba" id="psfsdiv" style="display: none;padding:2px;">
                <table class="table table-bordered" data-height="299"
                    data-show-header="false" data-toolbar="#transform-buttons">
                    <tbody>
                    	<tr>
                        	<td><input type="hidden" id="dbt3">
                                	<input id="elePolicy" type="radio" name="distritype" value="3" onclick="checkStatus(event)">电子保单
                            </td>
                            <td></td>
                            <td>说明 <textarea cols="30" rows="2" id="noti_3"></textarea></td>
                        </tr>
                        <tr>
                            <td rowspan="4"><input id="paperPolicy" type="radio" name="distritype" onclick="checkStatus(event)">纸质保单</td>
                            <td><input type="hidden" id="dbt1">
                                <input type="checkbox" name="distritype" value="1">自取</td>
                            <td>说明 <textarea cols="30" rows="2" id="noti_1"></textarea></td>
                        </tr>
                        <tr>
                            <td rowspan="3"><input type="hidden" id="dbt2">
                                <input type="checkbox" name="distritype" value="2">快递</td>
                            <td>快递公司
                               <#-- <input type="text" id="distrcompany_2" name="distrcompany_2"> -->
                               <#--<select id="distrcompany_2" name="distrcompany_2">
                                 <option value="0">请选择快递公司</option>
                                 <option value="1">顺丰快递</option>
                                 <option value="2">申通快递</option>
                                 <option value="3">中通快递</option>
                                 <option value="4">圆通快递</option>
                                 <option value="5">宅急送</option>
                                 <option value="6">邮政EMS</option>
                               </select>-->
                                <select id="distrcompany_2" name="distrcompany_2">
                                <#list comList as com>
                                    <option value="${com.codevalue}">${com.codename}</option>
                                </#list>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>收费方式 : <br />
                            <input type="radio" name="distpaytype_2" value="1"> 到付 <br />
                            <input type="radio" name="distpaytype_2" value="2"> 包邮<br />
                            <input type="radio" name="distpaytype_2" id="yushou_radio" value="3"> 预收&nbsp;&nbsp;
                            <input type="text" id="chargefee_2" readonly="readonly">
                            </td>
                        </tr>
                        <tr>
                            <td>说明 <textarea cols="30" rows="2" id="noti_2"></textarea>
                            </td>
                        </tr>

                    </tbody>
                </table>
                <div class="col-md-12" align="center">
                    <button id="savebutton" type="button" name="backbutton"
                        class="btn btn-primary" onclick="savedbt()">保存</button>
                </div>
            </div>

            <div class="panel-body haoba" id="ksxbdiv" style="display: none;padding:2px;">
                <form action="" id="renewalform" name="renewalform">
                    <div class="col-md-12 form-inline">
                        <input type="checkbox" name="renewalEnable" id="renewalEnable" value="1">开通快速续保
                        <input type="hidden" name="renewalagreementid" id="renewalagreementid" value="${agreement.id!''}">
                    </div>
                    <div class="col-md-12">
                        <table class="table table-bordered">
                            <tr>
                                <td width="10%">查询条件</td>
                                <td>
                                    <div class="col-xs-5">
                                        <select name="from" class="js-multiselect form-control" data-right-selected="#right_Selected_1" size="8" multiple="multiple" id="allRenewalItems">
                                        </select>
                                    </div>
                                </td width="40%">
                                <td width="10%">
                                    <div class="col-xs-2">
                                        <button type="button" id="js_right_Selected_1" class="btn btn-block"><i class="glyphicon glyphicon-chevron-right"></i></button>
                                        <button type="button" id="js_right_All_1" class="btn btn-block"><i class="glyphicon glyphicon-forward"></i></button>
                                        <button type="button" id="js_left_Selected_1" class="btn btn-block"><i class="glyphicon glyphicon-chevron-left"></i></button>
                                        <button type="button" id="js_left_All_1" class="btn btn-block"><i class="glyphicon glyphicon-backward"></i></button>
                                    </div>
                                </td>
                                <td width="40%">
                                    <div class="col-xs-5">
                                        <select name="to" id="js_multiselect_to_1" class="form-control" size="8" multiple="multiple">
                                        </select>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="col-md-12" align="center">
                        <button id="renewalItemSaveButton" type="button" name="renewalItemSaveButton" class="btn btn-primary" onclick="saveRenewalItems()">保存</button>
                    </div>
                </form>
            </div>
        </div>

        <div id="showpic" class="modal fade" role="dialog"
            aria-labelledby="myModalLabel" data-backdrop="false"
            data-keyboard="false" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title" id="gridSystemModalLabel">选择供应商</h4><br/>
                        <div><input class="form-control ztree-search" id = "treeDemosearch" data-bind="treeDemo" name="treeDemosearch" placeholder="输入供应商名称进行搜索" /></div>
                    </div>
                    <div class="modal-body" style="overflow: auto; height: 400px;">
                        <div class="container-fluid">
                            <div class="row">
                                <div id="treeDemo" class="ztree">正在加载供应商数据......</div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="showdept" class="modal fade" role="dialog"
            aria-labelledby="myModalLabel" data-backdrop="false"
            data-keyboard="false" aria-hidden="true">
            <div class="modal-dialog" role="document" style="height: 400px;wit">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title" id="gridSystemModalLabel">选择机构</h4>
                    </div>
                    <div class="modal-body" style="overflow: auto; height: 400px;">
                        <div class="container-fluid">
                            <div class="row">
                                <ul id="depttree" class="ztree">正在加载机构数据......</ul>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    </div>
                </div>
            </div>
        </div>

		<div id="showtrule" class="modal fade" role="dialog"
            aria-labelledby="myModalLabel" data-backdrop="false"
            data-keyboard="false" aria-hidden="true">
            <div class="modal-dialog" role="document" style="height: 75%;width:1000px;">
                <div class="modal-content" style="width:1000px;">
                    <div class="modal-header" style="height: 75%;width:1000px;">
                        <div class="row">
                            <div class="form-group col-md-5 form-inline" id='querytrule'>
                            <label for="exampleInputCode">规则查询</label> <select
                                class="form-control" id="province" name="province"
                                onchange="changcityadd('trule')" placeholder="请选择 ">
                                  </select>
                            </div>
                            <div class="form-group col-md-5 form-inline">
                                  <label for="exampleInputCode"><h4>规则名称：</h4></label>
                                  <input type="text" class="form-control w1" id="trulename"
                                     name="trulename"  value="">
                            </div>
                            <div class="form-group col-md-2 form-inline">
                                  <button id="querytrule" type="button" name="querytrule"
                                class="btn btn-primary" onclick="queryTrule()">查询</button>
                            </div>
                        </div>
                    </div>
                    <div class="modal-body" style="overflow: auto; height: 400px;width:1000px;">
                        <div class="container-fluid">
                           <table id="table-trule"></table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="showoutdept" class="modal fade" role="dialog"
            aria-labelledby="myModalLabel" data-backdrop="false"
            data-keyboard="false" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"
                            aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title" id="gridSystemModalLabel">选项</h4>
                    </div>
                    <div class="modal-body">
                        <form id="outdeptfrom">
                            <input type="hidden" id="outdeptid"></input>
                            <table class="table table-bordered" data-height="299"
                                data-show-header="false" data-toolbar="#transform-buttons">
                                <tbody>
                                    <tr>
                                        <td><label for="exampleInputCode">优先级 </label></td>
                                        <td><label for="exampleInputCode">设置权限 </label></td>
                                        <td><label for="exampleInputCode">操作 </label></td>
                                    </tr>
                                    <tr>
                                        <td><input class="form-control input-sm" type="text"
                                            id="scaleflag" nValidate="{required:true}"></td>
                                        <td><input class="form-control input-sm" type="text"
                                            id="permflag" nValidate="{required:true}"></td>
                                        <td><button id="savebutton" type="button"
                                                name="savebutton" class="btn btn-primary"
                                                onclick="updateoutdept()">保存</button></td>
                                    </tr>
                                </tbody>
                            </table>
                        </form>
                    </div>

                </div>
            </div>
        </div>
    </div>
</body>
</html>