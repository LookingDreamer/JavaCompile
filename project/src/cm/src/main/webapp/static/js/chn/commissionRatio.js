require([ "jquery", "zTree", "zTreecheck", "flat-ui", "bootstrap-table", "bootstrapdatetimepicker", "bootstrapdatetimepickeri18n",
    "bootstrap", "bootstrapTableZhCn", "public" ], function ($) {
    $(function () {
        inittree();

        $('#table-commissionRatio').bootstrapTable({
            method: 'get',
            url: pageurl,
            queryParams: queryParams,
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server',
            pageSize: pagesize,
            pageList: [15, 30, 50, 100],
            clickToSelect: true,
            minimumCountColumns: 2,
            columns: [
                {
                    field: 'state',
                    align: 'center',
                    valign: 'middle',
                    checkbox: true
                },
                {
                    field: 'id',
                    title: 'id',
                    visible: false,
                    switchable: false
                },
                {
                    field: 'channeleffectivetime',
                    title: 'channeleffectivetime',
                    visible: false,
                    switchable: false
                },
                {
                    field: 'reminder',
                    title: 'reminder',
                    visible: false,
                    switchable: false
                },
                {
                    field: 'paramdatatype',
                    title: '佣金类型code',
                    visible: false,
                    switchable: false
                },
                {
                    field: 'createtime',
                    title: '创建时间',
                    align: 'center',
                    valign: 'middle',
                    sortable: false
                },
                {
                    field: 'commissiontypename',
                    title: '佣金类型',
                    align: 'center',
                    valign: 'middle',
                    sortable: false
                },
                {
                    field: 'taxrate',
                    title: '税率',
                    visible: false,
                    sortable: false
                },
                {
                    field: 'calculatetype',
                    title: '计算方式',
                    align: 'center',
                    valign: 'middle',
                    formatter: calculateTypeFormatter,
                    sortable: false
                },
                {
                    field: 'ratio',
                    title: '佣金系数',
                    align: 'center',
                    valign: 'middle',
                    sortable: false
                },
                {
                    field: 'remark',
                    title: '说明',
                    align: 'center',
                    valign: 'middle',
                    sortable: false
                },
                {
                    field: 'effectivetime',
                    title: '生效时间',
                    align: 'center',
                    valign: 'middle',
                    sortable: false
                },
                {
                    field: 'statusname',
                    title: '状态',
                    align: 'center',
                    valign: 'middle',
                    sortable: false
                },
                {
                    field: 'operating',
                    title: '操作',
                    align: 'center',
                    valign: 'middle',
                    switchable: false,
                    formatter: operateFormatter1,
                    events: operateEvents1
                }
            ]
        });

        $('#table-conditions').bootstrapTable({
            method: 'get',
            url: "queryConditions",
            queryParams: queryConditionsParams,
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server',
            pageSize: pagesize,
            singleSelect: 'true',
            clickToSelect: true,
            minimumCountColumns: 2,
            columns: [
                {
                    field: 'id',
                    title: 'id',
                    visible: false,
                    switchable: false
                },
                {
                    field: 'createtime',
                    title: '创建时间',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'paramdatatype',
                    title: '数据类型',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'paramcnname',
                    title: '参数名称',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'expression',
                    title: '判断式',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'paramvalue',
                    title: '参数值',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'operating',
                    title: '操作',
                    align: 'center',
                    valign: 'middle',
                    switchable: false,
                    formatter: operateFormatter2,
                    events: operateEvents2
                }
            ]
        });

        $("#effectivedate").datetimepicker({
            language: 'zh-CN',
            format: "yyyy-mm-dd",
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            forceParse: 0,
            minView: 2,
            pickerPosition: "bottom-left",
            pickTime: true,
            todayBtn: true,
            pickDate: true,
            autoclose: true,
            startDate: new Date()
        });

        $("#terminaldate").datetimepicker({
            language: 'zh-CN',
            format: "yyyy-mm-dd",
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            forceParse: 0,
            minView: 2,
            pickerPosition: "bottom-left",
            pickTime: true,
            todayBtn: true,
            pickDate: true,
            autoclose: true,
            startDate: new Date()
        });

        $("#cterminaldate").datetimepicker({
            language: 'zh-CN',
            format: "yyyy-mm-dd",
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            forceParse: 0,
            minView: 2,
            pickerPosition: "bottom-left",
            pickTime: true,
            todayBtn: true,
            pickDate: true,
            autoclose: true,
            startDate: new Date()
        });

        // 绑定启用按钮事件
        $("#examinebutton").on("click", function (e) {
            var terminaldate = $("#terminaldate").val();
            var effdate = $("#effectivedate").val();
            if(terminaldate!=''){
                var arrterminal = terminaldate.split("-");
                var terminalTime = new Date(arrterminal[0], parseInt(arrterminal[1]) - 1, arrterminal[2],'23','59','59').format('yyyy-MM-dd hh:mm:ss');
                var currtime = new Date().format('yyyy-MM-dd hh:mm:ss');
                if(terminalTime < currtime){
                    return alertmsg('失效时间设置有误，请重新核对！');
                }
                if(effdate > terminaldate){
                    return alertmsg('失效时间需大于生效时间！');
                }
            }
            updateCommissionRatioStatus("1");
        });

        // 绑定关闭按钮事件
        $("#closebutton").on("click", function (e) {
            $('#singleCloseFlag').val('true');
            $('#cterminaldate').val('');
            $('#colseCommissionRateModel').modal('show');
            //updateCommissionRatioStatus("2");
        });

        $("#clear-terminaldate").on("click", function (e) {
            $("#terminaldate").val('');
        });

        $("#qcommissiontype").change(function (e) {
            query();
        });
        $("#querybutton").on("click", function (e) {
            query();
        });
        $("#clear-keyword").on("click", function (e) {
            $("#keyword").val('');
        });
        $("#defultclose").on("click", function (e) {
            confirmmsg("佣金系数配置是否已经保存？",function(){
                $("#collapseOne").attr("class", "panel-collapse collapse");
            },function(){

            })
        });
        $("#calculatetype").change(function (e) {
            if($("#calculatetype").val() != ''){
                $("#ratio").attr('disabled',false);
            }else{
                $("#ratio").attr('disabled',true);
            }

        });

        $("#confirmDateClose").on("click", function (e) {
            $("#set-terminaldate-tr").show();
        });
        $("#immediateClose").on("click", function (e) {
            $("#set-terminaldate-tr").hide();
            $("#cterminaldate").val('');
        });
        $("#copybutton").on("click", function (e) {
            var arrayratioid = getSelectedRowsId();
            var arrayratestatus = getSelectedRowsStatus();
            if(arrayratioid.length==0){
                alertmsg("请选中操作数据！");
                return false;
            };
            initmodaltree();
            $('#copyRateModal').modal('show');
        });
        //绑定关闭佣金按钮事件
        $("#cexecButton").on("click", function (e) {
            var operate = $('input[name="operatetype"]:checked').val();
            var singleCloseFlag = $("#singleCloseFlag").val();
            var batchCloseFlag = $("#batchCloseFlag").val();
            var terminaldate = $("#cterminaldate").val();
            if(terminaldate!=''){
                var arrterminal = terminaldate.split("-");
                var terminalTime = new Date(arrterminal[0], parseInt(arrterminal[1]) - 1, arrterminal[2],'23','59','59');
            }
            if(operate == '1' && singleCloseFlag == 'true'){
                updateCommissionRatioStatus("2");
            }else if(operate == '1' && batchCloseFlag == 'true'){
                batchCloseRatio();
            }else if(operate == '2' && singleCloseFlag == 'true'){
                if(terminaldate == ''){
                    return alertmsg('失效时间不能为空！');
                }
                specTerminalTimeSingle(terminalTime);
            }else if(operate == '2' && batchCloseFlag == 'true'){
                if(terminaldate == ''){
                    return alertmsg('失效时间不能为空！');
                }
                specTerminalTimeBatch(terminalTime);
            }
            $("#colseCommissionRateModel").modal('hide');
        });
        $("#execCopyButton").on("click",function() {
            // 拿到所有选中菜单ids
            var treeObj = $.fn.zTree.getZTreeObj('deptModalTree');
            var nodes = treeObj.getCheckedNodes(true);
            var checkedIds = '';
            var checkedNames = '';
            var index = 1;
            for (var i = 0; i < nodes.length; i++) {
                if(nodes[i].channelid != ''){
                    checkedIds += nodes[i].channelid + ',';
                    checkedNames += (index) +':'+ nodes[i].name +' ('+ nodes[i].channelid+') <br> ';
                    index +=1;
                }
            }
            if (checkedIds.length > 0) {
                checkedIds = checkedIds.substring(0, checkedIds.length - 1);
                checkedNames = checkedNames.substring(0, checkedNames.length - 1);
            }else{
                alertmsg('请至少选择一家渠道复制！');
                return ;
            }
            var arrayratioid = getSelectedRowsId();
            confirmmsg('是否复制到如下渠道： '+'<br>'+checkedNames,function(e){
                batchCopyCommissionRatio(arrayratioid.toString(),checkedIds);
            },function(e){

            });

        });
        $("#batcheffectivebutton").on("click", function (e) {
            var selectData = $('#table-commissionRatio').bootstrapTable('getSelections');
            if(selectData.length==0){
                alertmsg("请选中操作数据！");
                return false;
            }
            var arrterminal;
            var terminalTime;
            var currtime = new Date().format('yyyy-MM-dd hh:mm:ss');
            for(var i=0;i<selectData.length;i++){
                if(selectData[i].statusname != '待审核'){
                    alertmsg("只有待审核的佣金系数才允许启用，请重新核对！");
                    return false;
                }
                if(selectData[i].channelterminaltime !='' && selectData[i].channelterminaltime != undefined) {
                    arrterminal = selectData[i].channelterminaltime.split("-");
                    terminalTime = new Date(arrterminal[0], parseInt(arrterminal[1]) - 1, arrterminal[2], '23', '59', '59').format('yyyy-MM-dd hh:mm:ss');
                    if (terminalTime < currtime) {
                        return alertmsg('失效时间设置有误，请重新核对！');
                    }
                    if(selectData[i].channeleffectivetime > selectData[i].channelterminaltime){
                        return alertmsg('失效时间需大于生效时间！');
                    }
                }
            }
            var arrayrateid = getSelectedRowsId();
            $.ajax({
                url : 'batchUpdateCommissionRatioStatus?status=1&ratioIds=' + arrayrateid,
                type : 'GET',
                dataType : "json",
                async : true,
                error : function() {
                    alertmsg("Connection error");
                },
                success : function(data) {
                    var result = data.code;
                    if (result=='0') {
                        query();
                        alertmsg("启用成功！");
                    }else{
                        query();
                        alertmsg(data.message);
                    }
                }
            });
        });
        $("#batchclosebutton").on("click", function (e) {
            var arrayrateid = getSelectedRowsId();
            var arrayratestatus = getSelectedRowsStatus();
            if(arrayrateid.length==0){
                alertmsg("请选中操作数据！");
                return false;
            }
            for(var i=0;i<arrayratestatus.length;i++){
                if(arrayratestatus[i] != '已启用'){
                    alertmsg("只有已启用的佣金规则才允许关闭，请重新核对！");
                    return false;
                }
            }
            $("#batchCloseFlag").val('true');
            $('#cterminaldate').val('');
            $("#colseCommissionRateModel").modal('show');
        });

        $("#batchdeletebutton").on("click", function (e) {
            var selectData = $('#table-commissionRatio').bootstrapTable('getSelections');
            if(selectData.length==0){
                alertmsg("请选中操作数据！");
                return false;
            }

            confirmmsg("是否删除佣金系数配置？",function(){

                for(var i=0;i<selectData.length;i++){
                    if(selectData[i].statusname == '已启用'){
                        alertmsg("已启用系数不允许被删除！");
                        return false;
                    }
                }
                var arrayratioid = getSelectedRowsId();
                $.ajax({
                    url : 'batchDeleteCommissionRatio',
                    type : 'POST',
                    dataType : "json",
                    async : true,
                    data: "ratioIds=" + arrayratioid,
                    error : function() {
                        alertmsg("Connection error");
                    },
                    success : function(data) {
                        var result = data.code;
                        if (result=='0') {
                            query();
                            alertmsg(data.msg);
                        }else{
                            alertmsg(data.msg);
                        }
                    }
                });
            },function(){

            })

        });

        // 绑定添加条件按钮事件
        $("#addconditionbutton").on("click", function (e) {
            var commissionratioid = $("#commissionratioid").val();
            if (commissionratioid == "")
                return alertmsg("系统错误");

            $("#conditionTip").val("添加条件");
            $("#conditionsource").val("04");
            $("#sourceid").val(commissionratioid);
            $("#conditionid").val("");
            $("#paramname option:first").prop("selected", true);
            $("#expression").val("=");
            $("#paramvalue").val("");
            $('#editConditionModel').modal('show');
        });

        //绑定条件设置确定按钮事件
        $("#execButton").click(function () {
            if ($("#paramname").val() == "")
                return alertmsg("请填写参数名称");

            $.ajax({
                url: "saveConditions",
                type: 'post',
                dataType: "json",
                data: $("#conditionform").serialize(),
                cache: false,
                async: true,
                error: function () {
                    alertmsg("Connection error");
                },
                success: function (data) {
                    if (data.success) {
                        queryConditions($("#commissionratioid").val());
                        $('#editConditionModel').modal('hide');
                    }
                    else
                        alertmsg(data.result);
                }
            });
        });


    });
});

function queryParams(params) {
    var channelid = $("#channelid").val();
    return {
        channelid: channelid,
        offset: params.offset,
        limit: params.limit
    };
}

function queryConditionsParams(params) {
    var commissionratioid = $("#commissionratioid").val();
    return {
        conditionsource: '04',
        sourceid: commissionratioid,
        offset: params.offset,
        limit: params.limit
    };
}

function specTerminalTimeSingle(terminalTime){
    var commissionratioid = $('#commissionratioid').val();
    $.ajax({
        url: "updateCommissionRatioTerminalTime",
        type: 'post',
        dataType: "json",
        data: {
            id: commissionratioid,
            terminaltime: terminalTime
        },
        cache: false,
        async: true,
        error: function () {
            alertmsg("Connection error");
        },
        success: function (data) {
            if (data.success) {
                query();
                $("#collapseOne").attr("class", "panel-collapse collapse");
                alertmsg("操作成功")
            }
            else
                alertmsg(data.result);
        }
    });
    $("#singleCloseFlag").val('');
    $("#colseCommissionRateModel").modal('hide');
}

function specTerminalTimeBatch(terminalTime){
    var ratioIds = getSelectedRowsId();
    $.ajax({
        url: "batchUpdateCommissionRatioTerminalTime",
        type: 'post',
        dataType: "json",
        data: {
            ratioIds: ratioIds.toString(),
            terminalTime: terminalTime
        },
        cache: false,
        async: true,
        error: function () {
            alertmsg("Connection error");
        },
        success: function (data) {
            if (data.success) {
                query();
                $("#collapseOne").attr("class", "panel-collapse collapse");
                alertmsg("操作成功")
            }
            else
                alertmsg(data.result);
        }
    });
    $("#batchCloseFlag").val('');
    $("#colseCommissionRateModel").modal('hide');
}

/* zTree初始化数据 */
function inittree() {
    $.ajax({
        url: "initChannelList",
        type: 'POST',
        dataType: "json",
        error: function () {
            $("#deptTree").html('加载失败，<a href="javascrpt:void(0);" onclick="inittree();">点击这里</a>重试。');
        },
        success: function (data) {
            $.fn.zTree.init($("#deptTree"), {
                data: {
                    simpleData: {
                        enable: true,
                        idKey: "id",
                        pIdKey: "pId"
                    }
                },
                view: {
                    fontCss: function (treeId, treeNode) {
                        return (!!treeNode.highlight) ? {color: "#A60000", "font-weight": "bold"} : {color: "#333", "font-weight": "normal"};
                    },
                    expandSpeed: ""
                },
                callback: {
                    onClick: getdatafromid
                }
            }, data);
        }
    });
}

function getdatafromid(event, treeId, treeNode) {
    var channelid = treeNode.channelid;

    if (channelid != "") {
        $("#collapseOne").attr("class", "panel-collapse collapse");
        $("#channelid").val(channelid);
        $("#channelname").val(treeNode.name);
        queryCommissionRatio(channelid);
    }
}

function query(){
    var channelid = $("#channelid").val();
    if(channelid == ""){
        return alertmsg("请选择渠道！");
    }
    var commissiontype = $("#qcommissiontype").val();//add by hwc 20161109
    var keyword = $("#keyword").val();//add by hwc 20161109
    $("#collapseOne").attr("class", "panel-collapse collapse");
    queryCommissionRatio(channelid,commissiontype,keyword);
}

function queryCommissionRatio(channelid,commissiontype,keyword) {
    $.ajax({
        url: "queryCommissionRatio",
        type: 'GET',
        dataType: "json",
        data: {
            channelid: channelid,
            commissiontype:commissiontype,
            keyword:keyword,
            limit: pagesize
        },
        async: true,
        error: function () {
            alertmsg("Connection error");
        },
        success: function (data) {
            $('#table-commissionRatio').bootstrapTable('load', data);
        }
    });
}

function queryConditions(commissionratioid) {
    $.ajax({
        url: "queryConditions",
        type: 'GET',
        dataType: "json",
        data: {
            conditionsource: '04',
            sourceid: commissionratioid,
            limit: pagesize
        },
        async: true,
        error: function () {
            alertmsg("Connection error");
        },
        success: function (data) {
            $('#table-conditions').bootstrapTable('load', data);
        }
    });
}


function operateFormatter1(value, row, index) {
    return [ '<a class="edit m-left-5" href="#ratio-set" name="edit" title="编辑">',
        '<i class="glyphicon glyphicon-edit"></i>', '</a>',
        '<a class="remove m-left-5" href="javascript:void(0)" title="删除">',
        '<i class="glyphicon glyphicon-remove"></i>', '</a>'
         ].join('');
}
// 事件相应
window.operateEvents1 = {
    'click .edit': function (e, value, row, index) {
        editCommissionRatio(row);
        queryConditions(row.id);
        $("#defultclose").attr("class", "btn");
        $("#collapseOne").attr("class", "panel-body collapse in");
    },
    'click .remove': function (e, value, row, index) {
        if (confirm("确认删除吗?")) {
            delCommissionRatio(row.id)
        }
    },
    'click .copy': function (e, value, row, index) {
        if (confirm("确认复制并新建佣金系数吗?")) {
            copyCommissionRatio(row.channelid,row.id);
        }
    }
};

function operateFormatter2(value, row, index) {
    return [ '<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
        '<i class="glyphicon glyphicon-edit"></i>', '</a>',
        '<a class="remove m-left-5" href="javascript:void(0)" title="删除">',
        '<i class="glyphicon glyphicon-remove"></i>', '</a>'].join('');
}
// 事件相应
window.operateEvents2 = {
    'click .edit': function (e, value, row, index) {
        if ($('#commissionratiostatus').val() != "0")
            return false;

        $("#conditionTip").val("编辑条件");
        $("#conditionid").val(row.id);
        $("#paramname").val(row.paramname);
        $("#expression").val(row.expression);
        $("#paramvalue").val(row.paramvalue);
        $('#editConditionModel').modal('show');
    },
    'click .remove': function (e, value, row, index) {
        if ($('#commissionratiostatus').val() != "0")
            return false;
        if (confirm("确认删除吗?")) {
            delConditions(row.id)
        }
    }
};

function editCommissionRatio(row) {
    if (row.id != null && row.id != "") {
        $('#commissionratioid').val(row.id);
        $('#commissionratiostatus').val(row.status);
        $('#remark').val(row.remark).attr("disabled", row.status != "0");
        $('#commissiontype').val(row.commissiontype).attr("disabled", row.status != "0");
        $('#ratio').val(row.ratio).attr("disabled", row.status != "0");
        $('#effectivedate').val(row.channeleffectivetime).attr("disabled", row.status != "0");
        $('#terminaldate').val(row.channelterminaltime).attr("disabled", row.status != "0");
        $('#taxrate').val(row.taxrate).attr("disabled", row.status != "0");
        $('#calculatetype').val(row.calculatetype).attr("disabled", row.status != "0");
        $("#savebutton").attr("disabled", row.status != "0");
        $("#examinebutton").attr("disabled", row.status != "0");
        $("#closebutton").attr("disabled", row.status == "0" || row.status == "2");
        $("#addconditionbutton").attr("disabled", row.status != "0");
        if($('#calculatetype').val()==''){
            $('#ratio').val(row.ratio).attr("disabled", true);
        }
        if($('#calculatetype').val()!='' && row.status != "0" ){
            $('#ratio').val(row.ratio).attr("disabled", true);
        }
    }
}

function addCommissionRatio() {
    var channelid = $("#channelid").val();
    var commissiontype = $("#qcommissiontype").val();

    if (channelid != "") {
        $.ajax({
            url: 'addCommissionRatio',
            type: 'POST',
            dataType: "json",
            data: {
                channelid: channelid,
                commissiontype:commissiontype
            },
            async: true,
            error: function () {
                alertmsg("Connection error");
            },
            success: function (data) {
                if (data.success) {
                    query();
                    editCommissionRatio(data.result);
                    queryConditions(data.result.id);
                    $("#defultclose").attr("class", "btn");
                    $("#collapseOne").attr("class", "panel-body collapse in");
                }
                else alertmsg(data.result)
            }
        });
    }
}

function copyCommissionRatio(channelid,commissionratioid) {
    if (channelid != "" && commissionratioid != "") {
        $.ajax({
            url: 'copyCommissionRatio',
            type: 'POST',
            dataType: "json",
            data: {
                commissionratioid: commissionratioid
            },
            async: true,
            error: function () {
                alertmsg("Connection error");
            },
            success: function (data) {
                if (data.success) {
                    $("#collapseOne").attr("class", "panel-collapse collapse");
                    query();
                }
                else alertmsg(data.result)
            }
        });
    }
}

function batchCloseRatio(){
    var arrayratioid = getSelectedRowsId();
    $.ajax({
        url : 'batchUpdateCommissionRatioStatus?status=2&ratioIds=' + arrayratioid,
        type : 'GET',
        dataType : "json",
        async : true,
        error : function() {
            alertmsg("Connection error");
        },
        success : function(data) {
            var result = data.code;
            if (result=='0') {
                query();
                $("#batchCloseFlag").val('');
                $("#colseCommissionRateModel").modal('hide');
                alertmsg("关闭成功！");
            }else{
                alertmsg(data.message);
            }
        }
    });
}

function batchCopyCommissionRatio(arrayratioid,checkedIds){
    $.ajax({
        url: 'batchCopyCommissionRatio',
        type: 'POST',
        dataType: "json",
        data: {
            ratioIds: arrayratioid,
            channelIds:checkedIds
        },
        async: true,
        error: function () {
            alertmsg("Connection error");
        },
        success: function (data) {
            $('#copyRateModal').modal('hide');
            query();
            alertmsg('复制成功！')
        }
    });

}

function saveCommissionRatio() {
    var commissionratioid = $("#commissionratioid").val();
    if (commissionratioid != "") {
        var channelid = $("#channelid").val();
        var commissiontype = $("#commissiontype").val();
        var ratio = $("#ratio").val();
        var remark = $("#remark").val();
        var effectivedate = $("#effectivedate").val();
        var terminaldate = $("#terminaldate").val();
        var taxrate = $("#taxrate").val();
        var calculatetype = $("#calculatetype").val();
        if(effectivedate==''){
            alertmsg("生效时间不能为空！")
            return;
        }
        if(calculatetype==''){
            alertmsg("计算方式不能为空！")
            return;
        }
        if(ratio==''){
            alertmsg("佣金系数不能为空！")
            return;
        }
        if(!/^[0-9]+\.{0,1}[0-9]{0,4}$/.test(ratio)){
            alertmsg("佣金系数必须为数字类型！");
            return false;
        }
//        if(taxrate==''){
//            alertmsg("税率不能为空！")
//            return;
//        }
//        if(!/^[0-9]+\.{0,1}[0-9]{0,4}$/.test(taxrate)){
//            alertmsg("税率必须为数字类型！");
//            return false;
//        }

        var arr = effectivedate.split("-");
        var effectivetime = new Date(arr[0], parseInt(arr[1]) - 1, arr[2]);
        if(terminaldate != ''){
            var arr2 = terminaldate.split("-");
            var terminaltime = new Date(arr2[0], parseInt(arr2[1]) - 1, arr2[2],'23','59','59');
            var terminalTime2 = terminaltime.format('yyyy-MM-dd hh:mm:ss');
            var currtime = new Date().format('yyyy-MM-dd hh:mm:ss');
            if(terminalTime2 < currtime){
                return alertmsg('失效时间设置有误，请重新核对！');
            }
            if(effectivedate > terminaldate){
                return alertmsg('失效时间需大于生效时间！');
            }

            $.ajax({
                url: 'updateCommissionRatio',
                type: 'POST',
                dataType: "json",
                data: {
                    id: commissionratioid,
                    commissiontype: commissiontype,
                    ratio: ratio,
                    remark:remark,
                    effectivetime: effectivetime,
                    terminaltime:terminaltime,
                    taxrate:taxrate,
                    calculatetype:calculatetype
                },
                async: true,
                error: function () {
                    alertmsg("Connection error");
                },
                success: function (data) {
                    if (data.success) {
                        query();
                        alertmsg(data.result);
                    }
                    else
                        alertmsg(data.result)
                }
            });
        }else {

            $.ajax({
                url: 'updateCommissionRatio',
                type: 'POST',
                dataType: "json",
                data: {
                    id: commissionratioid,
                    commissiontype: commissiontype,
                    ratio: ratio,
                    remark: remark,
                    effectivetime: effectivetime,
                    taxrate:taxrate,
                    calculatetype:calculatetype
                },
                async: true,
                error: function () {
                    alertmsg("Connection error");
                },
                success: function (data) {
                    if (data.success) {
                        query();
                        alertmsg(data.result);
                    }
                    else
                        alertmsg(data.result)
                }
            });
        }
    }
}

function delCommissionRatio(delId) {
    var channelid = $("#channelid").val();
    if (delId != "" && channelid != "") {
        $.ajax({
            url: "delCommissionRatio",
            type: 'POST',
            dataType: "json",
            data: "id=" + delId,
            async: true,
            error: function () {
                alertmsg("Connection error");
            },
            success: function (data) {
                if (data.success) {
                    $("#collapseOne").attr("class", "panel-collapse collapse");
                    query();
                    alertmsg("删除成功！");
                }
                else
                    alertmsg(data.result);
            }
        });
    }
}

function updateCommissionRatioStatus(status) {
    var channelid = $("#channelid").val();
    var commissionratioid = $("#commissionratioid").val();
    var commissiontype = $("#commissiontype").val();

    if (channelid == "" || commissionratioid == "")
        return alertmsg("系统错误");

    $.ajax({
        url: "updateCommissionRatioStatus",
        type: 'post',
        dataType: "json",
        data: {
            id: commissionratioid,
            channelid: channelid,
            commissiontype:commissiontype,
            status: status
        },
        cache: false,
        async: true,
        error: function () {
            alertmsg("Connection error");
        },
        success: function (data) {
            if (data.success) {
                query();
                $("#collapseOne").attr("class", "panel-collapse collapse");
                $("#singleCloseFlag").val('');
                $("#colseCommissionRateModel").modal('hide');
                alertmsg("操作成功")
            }
            else
                alertmsg(data.result);
        }
    });
}

function delConditions(delId) {
    var commissionratioid = $("#commissionratioid").val();
    if (delId != "" && commissionratioid != "") {
        $.ajax({
            url: "delConditions",
            type: 'POST',
            dataType: "json",
            data: "id=" + delId,
            async: true,
            error: function () {
                alertmsg("Connection error");
            },
            success: function (data) {
                if (data.success) {
                    queryConditions(commissionratioid);
                    alertmsg("删除成功！");
                }
                else
                    alertmsg(data.result);
            }
        });
    }
}

function getSelectedRow() {
    var data = $('#table-commissionRatio').bootstrapTable('getSelections');
    if (data.length == 0) {
        return null;
    } else {
        return data[0].id;
    }
}

/* zTree初始化数据 */
function initmodaltree() {
    $.ajax({
        url: "initChannelList",
        type: 'POST',
        dataType: "json",
        error: function () {
            $("#deptModalTree").html('加载失败，<a href="javascrpt:void(0);" onclick="initmodaltree();">点击这里</a>重试。');
        },
        success: function (data) {
            $.fn.zTree.init($("#deptModalTree"), {
                data: {
                    simpleData: {
                        enable: true,
                        idKey: "id",
                        pIdKey: "pId"
                    }
                },
                check : {
                    enable : true
                },
                view: {
                    fontCss: function (treeId, treeNode) {
                        return (!!treeNode.highlight) ? {color: "#A60000", "font-weight": "bold"} : {color: "#333", "font-weight": "normal"};
                    },
                    expandSpeed: ""
                },
                callback: {

                }
            }, data);
        }
    });
}
//获得选中行的id列表
function getSelectedRowsId() {
    var data = $('#table-commissionRatio').bootstrapTable('getSelections');
    if(data.length == 0){
        alertmsg("至少选择一行数据！");
    }else{
        var arrayrateid = new Array();
        for(var i=0;i<data.length;i++){
            arrayrateid.push(data[i].id)
        }
        return arrayrateid;
    }
}

//获得选中行的id列表
function getSelectedRowsStatus() {
    var data = $('#table-commissionRatio').bootstrapTable('getSelections');
    if(data.length == 0){
        alertmsg("至少选择一行数据！");
    }else{
        var arrayratestatus = new Array();
        for(var i=0;i<data.length;i++){
            arrayratestatus.push(data[i].statusname)
        }
        return arrayratestatus;
    }
}

//活动类型文字转换
function calculateTypeFormatter(value, row, index) {
    if(value=='1'){
        return '加';
    }else if(value=='2'){
        return '减';
    }else if(value=='3'){
        return '乘';
    }else if(value=='4'){
        return '除';
    }else{
        return '';
    }
}

//获得选中行的id列表
function getSelectedRowsTerminalTime() {
    var data = $('#table-commissionRatio').bootstrapTable('getSelections');
    if(data.length == 0){
        alertmsg("至少选择一行数据！");
    }else{
        var arrayrateterminaltime = new Array();
        for(var i=0;i<data.length;i++){
            if(data[i].channelterminaltime != '' && data[i].channelterminaltime != undefined){
                arrayrateterminaltime.push(data[i].channelterminaltime)
            }
        }
        return arrayrateterminaltime;
    }
}
Date.prototype.format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt)) {
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
}
// 默认一页显示15条记录
var pagesize = 15;
// 当前调用的url
var pageurl = "queryCommissionRatio";