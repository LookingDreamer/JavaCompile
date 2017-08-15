require([ "jquery", "zTree", "zTreecheck", "bootstrap-table", "bootstrapdatetimepicker", "bootstrapdatetimepickeri18n",
    "bootstrap", "bootstrapTableZhCn", "public" ], function ($) {
    $(function () {
        inittree();
        initmodaltree();

        $('#table-commissionRate').bootstrapTable({
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
                    field: 'channelsource',
                    title: '应用渠道',
                    align: 'center',
                    valign: 'middle',
                    formatter:channelSourceFormatter,
                    sortable: false
                },
                {
                    field: 'rate',
                    title: '佣金率',
                    align: 'center',
                    valign: 'middle',
                    sortable: false
                },
                {
                    field: 'remark',
                    title: '佣金描述',
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

        $("#ieffectivedate").datetimepicker({
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

        $("#iterminaldate").datetimepicker({
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
            updateCommissionRateStatus("1");
        });

        // 绑定关闭按钮事件
        $("#closebutton").on("click", function (e) {
            $('#singleCloseFlag').val('true');
            $('#cterminaldate').val('');
            $('#colseCommissionRateModel').modal('show');
            //updateCommissionRateStatus("2");
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
                updateCommissionRateStatus("2");
            }else if(operate == '1' && batchCloseFlag == 'true'){
                batchCloseRate();
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

        });

        // 绑定添加条件按钮事件
        $("#addconditionbutton").on("click", function (e) {
            var commissionrateid = $("#commissionrateid").val();
            if (commissionrateid == "")
                return alertmsg("系统错误");

            $("#conditionTip").val("添加条件");
            $("#conditionsource").val("02");
            $("#sourceid").val(commissionrateid);
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
                        queryConditions($("#commissionrateid").val());
                        $('#editConditionModel').modal('hide');
                    }
                    else
                        alertmsg(data.result);
                }
            });
        });

        //
        $("#qcommissiontype").change(function (e) {
            query();
        });
        $("#qcommissionstatus").change(function (e) {
            query();
        });
        $("#querybutton").on("click", function (e) {
            query();
        });
        $("#clear-keyword").on("click", function (e) {
            $("#keyword").val('');
        });
        $("#confirmDateClose").on("click", function (e) {
            $("#set-terminaldate-tr").show();
        });
        $("#immediateClose").on("click", function (e) {
            $("#set-terminaldate-tr").hide();
            $("#cterminaldate").val('');
        });
        $("#iresetButton").on("click", function (e) {
            initCommissionRateForm();
        });

        $("#clear-terminaldate").on("click", function (e) {
            $("#terminaldate").val('');
        });

        $("#defultclose").on("click", function (e) {
           confirmmsg("佣金配置是否已经保存？",function(){
               $("#collapseOne").attr("class", "panel-collapse collapse");
           },function(){

            })
        });

        $("#addagreement").on("click", function (e) {
            var agreementid = $("#agreementid").val();
            if(agreementid == ""){
                return alertmsg("请选择一家供应商协议");
            }
            initCommissionRateForm();
            $('#addCommissionRateModel').modal('show');
        });
        $("#iexecButton").on("click", function (e) {
            addCommissionRate();
        });
        $("#copybutton").on("click", function (e) {
            var arrayrateid = getSelectedRowsId();
            var arrayratestatus = getSelectedRowsStatus();
            if(arrayrateid.length==0){
                alertmsg("请选中操作数据！");
                return false;
            };
            initmodaltree();
            $('#copyRateModal').modal('show');
        });
        $("#execCopyButton").on("click",function() {
            // 拿到所有选中菜单ids
            var treeObj = $.fn.zTree.getZTreeObj('deptModalTree');
            var nodes = treeObj.getCheckedNodes(true);
            var checkedIds = '';
            var checkedNames = '';
            var index = 1;
            for (var i = 0; i < nodes.length; i++) {
                if(nodes[i].agreementid != ''){
                    checkedIds += nodes[i].agreementid + ',';
                    checkedNames += (index) +':'+ nodes[i].name +' ('+ nodes[i].providerid+') <br> ';
                    index +=1;
                }
            }
            if (checkedIds.length > 0) {
                checkedIds = checkedIds.substring(0, checkedIds.length - 1);
                checkedNames = checkedNames.substring(0, checkedNames.length - 1);
            }else{
                alertmsg('请至少选择一家供应商复制！');
                return ;
            }
            var arrayrateid = getSelectedRowsId();
            confirmmsg('是否复制到如下供应商： '+'<br>'+checkedNames,function(e){
                batchCopyCommissionRate(arrayrateid.toString(),checkedIds);
            },function(e){

            });

        });
        $("#batcheffectivebutton").on("click", function (e) {
            var selectData = $('#table-commissionRate').bootstrapTable('getSelections');
            if(selectData.length==0){
                alertmsg("请选中操作数据！");
                return false;
            }
            var arrterminal;
            var terminalTime;
            var currtime = new Date().format('yyyy-MM-dd hh:mm:ss');
            for(var i=0;i<selectData.length;i++){
                if(selectData[i].statusname != '待审核'){
                    alertmsg("只有待审核的佣金规则才允许启用，请重新核对！");
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
                url : 'batchUpdateCommissionRateStatus?status=1&rateIds=' + arrayrateid,
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
                        alertmsg(data.message);
                    }
                }
            });
        });

        $("#batchdeletebutton").on("click", function (e) {
            var selectData = $('#table-commissionRate').bootstrapTable('getSelections');
            if(selectData.length==0){
                alertmsg("请选中操作数据！");
                return false;
            }

            confirmmsg("是否删除佣金配置？",function(){

                for(var i=0;i<selectData.length;i++){
                    if(selectData[i].statusname == '已启用'){
                        alertmsg("已启用规则不允许被删除！");
                        return false;
                    }
                }
                var arrayrateid = getSelectedRowsId();
                $.ajax({
                    url : 'batchDeleteCommissionRate',
                    type : 'POST',
                    dataType : "json",
                    async : true,
                    data: "rateIds=" + arrayrateid,
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

    });
});

function specTerminalTimeBatch(terminalTime){
    var rateIds = getSelectedRowsId();
    $.ajax({
        url: "batchUpdateCommissionRateTerminalTime",
        type: 'post',
        dataType: "json",
        data: {
            rateIds: rateIds.toString(),
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

function specTerminalTimeSingle(terminalTime){
    var commissionrateid = $('#commissionrateid').val();
    $.ajax({
        url: "updateCommissionRateTerminalTime",
        type: 'post',
        dataType: "json",
        data: {
            id: commissionrateid,
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
function batchCloseRate(){
    var arrayrateid = getSelectedRowsId();
    $.ajax({
        url : 'batchUpdateCommissionRateStatus?status=2&rateIds=' + arrayrateid,
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
//获得选中行的id列表
function getSelectedRowsId() {
    var data = $('#table-commissionRate').bootstrapTable('getSelections');
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
    var data = $('#table-commissionRate').bootstrapTable('getSelections');
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

//获得选中行的id列表
function getSelectedRowsTerminalTime() {
    var data = $('#table-commissionRate').bootstrapTable('getSelections');
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

function batchCopyCommissionRate(arrayrateid,checkedIds){
        $.ajax({
            url: 'batchCopyCommissionRate',
            type: 'POST',
            dataType: "json",
            data: {
                rateIds: arrayrateid,
                agreementIds:checkedIds
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

function queryParams(params) {
    var agreementid = $("#agreementid").val();
    var channelsource = $("#qchannelsource").val();//add by hwc 20161109
    return {
        agreementid: agreementid,
        offset: params.offset,
        limit: params.limit
    };
}

function queryConditionsParams(params) {
    var commissionRateId = $("#commissionrateid").val();
    return {
        conditionsource: '02',
        sourceid: commissionRateId,
        offset: params.offset,
        limit: params.limit
    };
}

/* zTree初始化数据 */
function inittree() {
    $.ajax({
        url: "initAgreementList",
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

/* zTree初始化数据 */
function initmodaltree() {
    $.ajax({
        url: "initAgreementList",
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

function getdatafromid(event, treeId, treeNode) {
    var agreementid = treeNode.agreementid;
    var agreementcode = treeNode.agreementcode;
    var providerid = treeNode.providerid;
    if (agreementid != "") {
        $("#collapseOne").attr("class", "panel-collapse collapse");
        $("#agreementid").val(agreementid);
        $("#agreementcode").val(agreementcode);
        $("#providerid").val(providerid);
        $("#agreementname").val(treeNode.name);
        queryCommissionRate(agreementid);
    }
}

function query(){
    var agreementid = $("#agreementid").val();
    if(agreementid == ""){
        return alertmsg("请选择供应商协议！");
    }
    var channelsource = $("#qchannelsource").val();//add by hwc 20161109
    var commissiontype = $("#qcommissiontype").val();//add by hwc 20161109
    var status = $("#qcommissionstatus").val();//add by hwc 20161109
    var keyword = $("#keyword").val();//add by hwc 20161109
    $("#collapseOne").attr("class", "panel-collapse collapse");
    queryCommissionRate(agreementid,channelsource,commissiontype,status,keyword);
}

function queryCommissionRate(agreementid,channelsource,commissiontype,status,keyword) {
    $.ajax({
        url: "queryCommissionRate",
        type: 'GET',
        dataType: "json",
        data: {
            agreementid: agreementid,
            channelsource:channelsource,
            commissiontype:commissiontype,
            status:status,
            keyword:keyword,
            limit: pagesize
        },
        async: true,
        error: function () {
            alertmsg("Connection error");
        },
        success: function (data) {
            $('#table-commissionRate').bootstrapTable('load', data);
        }
    });
}

function queryConditions(commissionRateId) {
    $.ajax({
        url: "queryConditions",
        type: 'GET',
        dataType: "json",
        data: {
            conditionsource: '02',
            sourceid: commissionRateId,
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

//活动类型文字转换
function channelSourceFormatter(value, row, index) {
    if(value=='nqd_minizzb2016'){
        return 'mini掌中保';
    }else if(value=='channel'){
        return '渠道';
    }else {
        return value;
    }
}

function operateFormatter1(value, row, index) {
    return [ '<a class="copy m-left-5" href="javascript:void(0)" title="复制">',
        '<i class="glyphicon glyphicon-copy"></i>', '</a>',
        '<a class="edit m-left-5" href="#rate-set" name="edit" title="编辑">',
        '<i class="glyphicon glyphicon-edit"></i>', '</a>',
        '<a class="remove m-left-5" href="javascript:void(0)" title="删除">',
        '<i class="glyphicon glyphicon-remove"></i>', '</a>'
         ].join('');
}
// 事件相应
window.operateEvents1 = {
    'click .edit': function (e, value, row, index) {
        editCommissionRate(row);
        queryConditions(row.id);
        $("#defultclose").attr("class", "btn");
        $("#collapseOne").attr("class", "panel-body collapse in");
    },
    'click .remove': function (e, value, row, index) {
        if (confirm("确认删除吗?")) {
            delCommissionRate(row.id)
        }
    },
    'click .copy': function (e, value, row, index) {
        if (confirm("确认复制并新建佣金设置吗?")) {
            copyCommissionRate(row.agreementid,row.id);
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
        if ($('#commissionratestatus').val() != "0")
            return false;

        $("#conditionTip").val("编辑条件");
        $("#conditionid").val(row.id);
        $("#paramname").val(row.paramname);
        $("#expression").val(row.expression);
        $("#paramvalue").val(row.paramvalue);
        $('#editConditionModel').modal('show');
    },
    'click .remove': function (e, value, row, index) {
        if ($('#commissionratestatus').val() != "0")
            return false;
        if (confirm("确认删除吗?")) {
            delConditions(row.id)
        }
    }
};

function editCommissionRate(row) {
    if (row.id != null && row.id != "") {
        $('#commissionrateid').val(row.id);
        $('#commissionratestatus').val(row.status);
        $('#remark').val(row.remark).attr("disabled", row.status != "0");
        $('#reminder').val(row.reminder).attr("disabled", row.status != "0");
        $('#commissiontype').val(row.commissiontype).attr("disabled", row.status != "0");
        $('#channelsource').val(row.channelsource).attr("disabled", row.status != "0");
        $('#rate').val(row.rate).attr("disabled", row.status != "0");
        $('#effectivedate').val(row.channeleffectivetime).attr("disabled", row.status != "0");
        $('#terminaldate').val(row.channelterminaltime).attr("disabled", row.status != "0");
        $("#clear-terminaldate").attr("disabled", row.status != "0");
        $("#savebutton").attr("disabled", row.status != "0");
        $("#examinebutton").attr("disabled", row.status != "0");
        $("#closebutton").attr("disabled", row.status == "0" || row.status == "2");
        $("#addconditionbutton").attr("disabled", row.status != "0");
    }
}
/*
function addCommissionRate() {
    var agreementid = $("#agreementid").val();
    var channelsource = $("#qchannelsource").val();
    if(channelsource==''){
        channelsource='channel';
    }
    if (agreementid != "") {
        $.ajax({
            url: 'addCommissionRate',
            type: 'POST',
            dataType: "json",
            data: {
                agreementid: agreementid,
                channelsource:channelsource
            },
            async: true,
            error: function () {
                alertmsg("Connection error");
            },
            success: function (data) {
                if (data.success) {
                    queryCommissionRate(agreementid,channelsource);
                    editCommissionRate(data.result);
                    queryConditions(data.result.id);
                    $("#defultclose").attr("class", "btn");
                    $("#collapseOne").attr("class", "panel-body collapse in");
                }
                else alertmsg(data.result)
            }
        });
    }
}
*/
function copyCommissionRate(agreementid,commissionrateid) {
    var channelsource = $("#qchannelsource").val();
    if (agreementid != "" && commissionrateid != "") {
        $.ajax({
            url: 'copyCommissionRate',
            type: 'POST',
            dataType: "json",
            data: {
                commissionRateId: commissionrateid
            },
            async: true,
            error: function () {
                alertmsg("Connection error");
            },
            success: function (data) {
                if (data.success) {
                    $("#collapseOne").attr("class", "panel-collapse collapse");
                    queryCommissionRate(agreementid,channelsource);
                }
                else alertmsg(data.result)
            }
        });
    }
}

function saveCommissionRate() {
    var commissionrateid = $("#commissionrateid").val();
    if (commissionrateid != "") {
        var agreementid = $("#agreementid").val();
        var commissiontype = $("#commissiontype").val();
        var channelsource = $("#channelsource").val();
        var rate = $("#rate").val();
        var remark = $("#remark").val();
        var reminder = $("#reminder").val();
        var effectivedate = $("#effectivedate").val();
        var terminaldate = $("#terminaldate").val();
        if(commissiontype == ''){
            alertmsg("请选择佣金类型！")
            return;
        }
        if(!/^[0-9]+\.{0,1}[0-9]{0,4}$/.test(rate)){
            alertmsg("佣金率必须为数字类型！");
            return false;
        }
        if(rate == '' || rate >= 1){
            alertmsg("佣金率不能为空或者不能大于1！")
            return;
        }
        if(effectivedate == ''){
            alertmsg("生效时间不能为空！")
            return;
        }
//        if(reminder!=''&&rate!='0'){
//            alertmsg("启用佣金提醒，佣金率需设置为0！")
//            return;
//        }
        var arr = effectivedate.split("-");
        var effectivetime = new Date(arr[0], parseInt(arr[1]) - 1, arr[2]);
        var terminaltime;
        if(terminaldate!=''){
            var arrterminal = terminaldate.split("-");
             terminaltime = new Date(arrterminal[0], parseInt(arrterminal[1]) - 1, arrterminal[2],'23','59','59');
            $.ajax({
                url: 'updateCommissionRate',
                type: 'POST',
                dataType: "json",
                data: {
                    id: commissionrateid,
                    commissiontype: commissiontype,
                    channelsource: channelsource,
                    rate: rate,
                    remark:remark,
                    reminder:reminder,
                    effectivetime: effectivetime,
                    terminaltime:terminaltime
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
        }else{
            $.ajax({
                url: 'updateCommissionRate',
                type: 'POST',
                dataType: "json",
                data: {
                    id: commissionrateid,
                    commissiontype: commissiontype,
                    channelsource: channelsource,
                    rate: rate,
                    remark:remark,
                    reminder:reminder,
                    effectivetime: effectivetime
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

function addDate(date, days) {
    if (days == undefined || days == '') {
        days = 1;
    }
    var date = new Date(date);
    date.setDate(date.getDate() + days);
    var month = date.getMonth() + 1;
    var day = date.getDate();
    return date.getFullYear() + '-' + getFormatDate(month) + '-' + getFormatDate(day);
}
function getFormatDate(arg) {
    if (arg == undefined || arg == '') {
        return '';
    }

    var re = arg + '';
    if (re.length < 2) {
        re = '0' + re;
    }

    return re;
}
function initCommissionRateForm(){
    var commissiontype = $("#qcommissiontype").val();
    $("#icommissiontype").val(commissiontype);
    $("#ichannelsource").val('channel');
    $("#irate").val('');
    $("#iremark").val('');
    $("#ireminder").val('');
    var effectivedate = addDate(new Date());
    $("#ieffectivedate").val(effectivedate);
    $("#iterminaldate").val('');
}

function addCommissionRate() {
    var agreementid = $("#agreementid").val();
    if(agreementid == ''){
        alertmsg("请选择供应商协议！")
        return;
    }
    var commissiontype = $("#icommissiontype").val();
    var channelsource = $("#ichannelsource").val();
    var rate = $("#irate").val();
    var remark = $("#iremark").val();
    var reminder = $("#ireminder").val();
    var effectivedate = $("#ieffectivedate").val();
    var terminaldate = $("#iterminaldate").val();
    if(commissiontype == ''){
        alertmsg("请选择佣金类型！")
        return;
    }
    if(!/^[0-9]+\.{0,1}[0-9]{0,4}$/.test(rate)){
        alertmsg("佣金率必须为数字类型！");
        return false;
    }
    if(rate == '' || rate >= 1){
        alertmsg("佣金率不能为空或者不能大于1！")
        return;
    }
    if(effectivedate == ''){
        alertmsg("生效时间不能为空！")
        return;
    }
    if(remark == ''){
        alertmsg("佣金描述不能为空！")
        return;
    }

    var arr = effectivedate.split("-");
    var effectivetime = new Date(arr[0], parseInt(arr[1]) - 1, arr[2]);
    if(terminaldate!=''){
        var arrterminal = terminaldate.split("-");
        var terminaltime = new Date(arrterminal[0], parseInt(arrterminal[1]) - 1, arrterminal[2],'23','59','59');
    }
    $.ajax({
        url: 'addCommissionRate',
        type: 'POST',
        dataType: "json",
        data: {
            agreementid:agreementid,
            commissiontype: commissiontype,
            channelsource: channelsource,
            rate: rate,
            remark:remark,
            reminder:reminder,
            effectivetime: effectivetime,
            terminaltime:terminaltime
        },
        async: true,
        error: function () {
            alertmsg("Connection error");
        },
        success: function (data) {
            if (data.success) {
                query();
                $('#addCommissionRateModel').modal('hide');
                alertmsg('保存成功！');
            }
            else
                alertmsg('保存成功，提示有误！')
        }
    });

}

function delCommissionRate(delId) {
    var agreementid = $("#agreementid").val();
    var channelsource = $("#qchannelsource").val();
    if (delId != "" && agreementid != "") {
        $.ajax({
            url: "delCommissionRate",
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
                    //queryCommissionRate(agreementid,channelsource);
                    query();
                    alertmsg("删除成功！");
                }
                else
                    alertmsg(data.result);
            }
        });
    }
}

function updateCommissionRateStatus(status) {
    var agreementid = $("#agreementid").val();
    var commissionrateid = $("#commissionrateid").val();
    var channelsource = $("#channelsource").val();
    var commissiontype = $("#commissiontype").val();
    if (agreementid == "" || commissionrateid == "")
        return alertmsg("系统错误");

    $.ajax({
        url: "updateCommissionRateStatus",
        type: 'post',
        dataType: "json",
        data: {
            id: commissionrateid,
            agreementid: agreementid,
            channelsource:channelsource,
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
    var commissionrateid = $("#commissionrateid").val();
    if (delId != "" && commissionrateid != "") {
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
                    queryConditions(commissionrateid);
                    alertmsg("删除成功！");
                }
                else
                    alertmsg(data.result);
            }
        });
    }
}

function getSelectedRow() {
    var data = $('#table-commissionRate').bootstrapTable('getSelections');
    if (data.length == 0) {
        return null;
    } else {
        return data[0].id;
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
var pageurl = "queryCommissionRate";