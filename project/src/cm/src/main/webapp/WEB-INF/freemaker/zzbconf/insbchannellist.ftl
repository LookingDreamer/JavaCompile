<!DOCTYPE html>
<html lang="en" class="fuelux">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>渠道管理</title>
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
        .childItem {
            display: none ;
        }
        .sender {width: 120px;align:center;font-weight:bold;
        }
    </style>
    <script type="text/javascript">
        requirejs(["zzbconf/insbchannel","lib/tsearch"]);
    </script>
</head>
<body>

<#--通用参数-->
<div style="display: none;">
    <input type="hidden" id="channelid" name="channelid" />
    <input type="hidden" id="agreementid" name="agreementid" />
</div>
<#--通用参数-->

<#--渠道列表-->
<div class="col-md-3">
    <div class="panel panel-default m-bottom-5">
        <div class="panel-heading padding-5-5">渠道列表</div>
        <div class="panel-body">
			<div><input class="form-control ztree-search" id ="treesearch" data-bind="channelTree" name="treesearch" placeholder="输入渠道名称关键字进行搜索" />
			</div>
            <!--渠道树-->
            <div class="ztree" id="channelTree" style="width:100%; height:442px; overflow-y:auto;overflow-x:auto;">
            </div>

            <!--渠道操作按钮-->
            <div>
                <div style="float: left; margin-right: 5px">
                    <input class="btn btn-primary" id="addButton" type="button" value="添加渠道">
                </div>
                <div>
                    <input style="display: none;" class="btn btn-primary" id="deleteButton" type="button" value="删除渠道">
                </div>
            </div>

        </div>
    </div>
</div>
<#--渠道列表 end-->

<#--渠道详情信息-->
<div class="col-md-9">
    <div class="panel panel-default m-bottom-5">

	<#--渠道详情导航栏-->
        <div class="panel-heading padding-3-3">
            <ul id="navList" class="nav nav-tabs">
                <li id="channelDetailNav"><a href="#channelDetailTab" data-toggle="tab">渠道详细信息</a></li>
            </ul>
        </div>
	<#--渠道详情导航栏 end-->

	<#--渠道详情内容-->
        <div class="tab-content">
            <div class="tab-pane active" id="channelDetailTab">
                <table class="table table-bordered ">
                    <tr>
                        <td><label for="upchannelcode">渠道上级代码</label></td>
                        <td colspan="3"><input class="form-control" id="upchannelcode" type="text" name="upchannelcode" readonly="readonly"></td>
                    </tr>
                    <tr>
                        <td><label for="channelcode">渠道编码</label></td>
                        <td colspan="3"><input class="form-control" id="channelcode" type="text" name="channelcode" readonly="readonly"></td>
                    </tr>
                    <tr>
                        <td><label for="channelsecret">渠道密钥</label></td>
                        <td colspan="3"><input class="form-control" id="channelsecret" type="text" name="channelsecret"></td>
                    </tr>

                    <tr>
                        <td><label for="channelinnercode">渠道ID</label></td>
                        <td colspan="3"><input class="form-control" id="channelinnercode" type="text" name="channelinnercode"></td>
                    </tr>
                    <tr>
                        <td><label for="channelname">渠道名称</label></td>
                        <td colspan="3"><input class="form-control" id="channelname" type="text" name="channelname"></td>
                    </tr>
                    <tr class="channeltype">
                        <td><label for="channeltype" >对接模式</label></td>
                        <td colspan="3">
                        		<select id="channeltype" name="channeltype" class="form-control">
                        			<option value="">请选择</option>
                        			<option value="02">掌中保流程</option>
                        			<option value="03">原流程</option>
                        			<option value="01">支付前置流程</option>
                        			
                        		</select>
                        </td>
                    </tr>
                    <tr class="webaddress">
                        <td ><label for="webaddress">渠道回调地址</label></td>
                        <td colspan="3"><input class="form-control" id="webaddress" type="text" name="webaddress"></td>
                    </tr>
                    <tr class="payaddress">
                        <td><label for="address">支付前置流程中<br/>间层回调地址</label></td>
                        <td colspan="3"><input class="form-control" id="address" type="text" name="address"></td>
                    </tr>
                    <tr class="childItem">
                        <td><label for="deptid">所属机构</label></td>
                        <input type="hidden" id="deptid" name="deptid" />
                        <td colspan="3"><input class="form-control" id="deptname" type="text" name="deptname" placeholder="请选择"></td>
                    </tr>
                    <tr class="childItem">
                        <td><label for="jobnum">出单工号</label></td>
                        <td colspan="3"><input class="form-control" id="jobnum" type="text" name="jobnum"></td>
                    </tr>
                    <tr class="childItem">
                        <td><label>当前状态:</label></td>
                        <td colspan="3">
                            <select id="agreementstatus" name="agreementstatus" class="form-control">
                                <option value="0" <#if "${channelinfo.channelagreement.agreementstatus}"=="0">selected</#if>>关闭</option>
                                <option value="1" <#if "${channelinfo.channelagreement.agreementstatus}"=="1">selected</#if>>开启</option>
                            </select>
                        </td>
                    </tr>
                    <tr class="childItem">
                        <td><label for="areaDiv">应用地区</label></td>
                        <td colspan="3" id="areaDiv">
                            <div>
                                <label>省份:</label>
                                <select name="province" id="province"></select>
                            </div>
                            <div>
                                <label>地市:</label>
                                <div class="form-group" id="cityDiv"></div>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td><label for="isdefined">是否自定义寄件人信息</label></td>
                        <td colspan="3"><select class="form-control" id="isdefined" type="text" name="isdefined">
                        		<option value="0">否</option>
                        		<option value="1">是</option>
                        	</select></td>
                    </tr>
                    <tr>
                        <td rowspan=5><label for="jobnum">寄件人详细信息</label></td>
                    </tr>
                    <tr>
                        <td class="sender"><label for="senderchannel">寄件人单位名称</label></td>
                         <td><input class="form-control" id="senderchannel" type="text" name="senderchannel" readonly="readonly"></td>
                    </tr>
                    <tr>
                        <td  class="sender"><label for="sendername">寄件人姓名</label></td>
                        <td><input class="form-control" id="sendername" type="text" name="sendername" readonly="readonly"></td>
                    </tr>
                    
					<tr>
                        <td class="sender"><label for="senderaddress">寄件人地址</label></td>
                        <td><input class="form-control" id="senderaddress" type="text" name="senderaddress" readonly="readonly"></td>
                    </tr>
                    <tr>
                        <td class="sender"><label for="senderphone">寄件人电话</label></td>
                        <td><input class="form-control" id="senderphone" type="text" name="senderphone" readonly="readonly"></td>
                    </tr>
                    <tr>
                        <td><label for="illustration">渠道说明</label></td>
                        <td colspan="3"><textarea rows="2" class="form-control" id="illustration" name="illustration"></textarea></td>
                    </tr>
                    <tr>
                        <td><label for="noti">备注</label></td>
                        <td colspan="3"><textarea rows="2" class="form-control" id="noti" name="noti"></textarea></td>
                    </tr>
                </table>
                <input class="btn btn-primary" id="saveButton" type="button" value="保 存"/>
            </div>
        </div>
	<#--渠道详情内容 end-->
    </div>
</div>
<#--渠道详情信息 end-->

<#--选择机构弹窗-->
<div class="modal fade" id="deptDialog" role="dialog" aria-labelledby="gridSystemModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="gridSystemModalLabel">选择机构</h4>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <ul id="deptTree" class="ztree"></ul>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default " data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<#--选择机构弹窗 end-->

</body>
</html>

