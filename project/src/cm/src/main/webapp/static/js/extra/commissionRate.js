require([ "jquery", "zTree", "zTreecheck", "flat-ui", "bootstrap-table", "bootstrapdatetimepicker", "bootstrapdatetimepickeri18n",
    "bootstrap", "bootstrapTableZhCn", "public" ], function ($) {
    $(function () {
        inittree();

        $('#table-commissionRate').bootstrapTable({
            method: 'get',
            url: pageurl,
            queryParams: queryParams,
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server',
            pageSize: pagesize,
            singleSelect: 'true',
            clickToSelect: true,
            minimumCountColumns: 2,
            columns: [
//                {
//                    field: 'state',
//                    align: 'center',
//                    valign: 'middle',
//                    checkbox: true
//                },
                {
                    field: 'id',
                    title: 'id',
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
                    sortable: true
                },
                {
                    field: 'commissiontypename',
                    title: '佣金类型',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'rate',
                    title: '佣金率',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'remark',
                    title: '佣金描述',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'effectivetime',
                    title: '生效时间',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'statusname',
                    title: '状态',
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

        // 绑定启用按钮事件
        $("#examinebutton").on("click", function (e) {
            updateCommissionRateStatus("1");
        });

        // 绑定关闭按钮事件
        $("#closebutton").on("click", function (e) {
            updateCommissionRateStatus("2");
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

        // 绑定复制并新建佣金设置按钮事件
        $("#copycommissionrate").on("click", function (e) {
            if (confirm("确认复制并新建佣金设置吗?")) {
                copyCommissionRate();
            }
        });

    });
});

function queryParams(params) {
    var agreementid = $("#agreementid").val();
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

function getdatafromid(event, treeId, treeNode) {
    var agreementid = treeNode.agreementid;
    var agreementcode = treeNode.agreementcode;
    if (agreementid != "") {
        $("#collapseOne").attr("class", "panel-collapse collapse");
        $("#agreementid").val(agreementid);
        $("#agreementcode").val(agreementcode);
        $("#agreementname").val(treeNode.name);
        queryCommissionRate(agreementid);
    }
}

function queryCommissionRate(agreementid) {
    $.ajax({
        url: "queryCommissionRate",
        type: 'GET',
        dataType: "json",
        data: {
            agreementid: agreementid,
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

function operateFormatter1(value, row, index) {
    return [ '<a class="edit m-left-5" href="javascript:void(0)" name="edit" title="编辑">',
        '<i class="glyphicon glyphicon-edit"></i>', '</a>',
        '<a class="remove m-left-5" href="javascript:void(0)" title="删除">',
        '<i class="glyphicon glyphicon-remove"></i>', '</a>'].join('');
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
        $('#commissiontype').val(row.commissiontype).attr("disabled", row.status != "0");
        $('#rate').val(row.rate).attr("disabled", row.status != "0");
        $('#effectivedate').val(row.effectivetime).attr("disabled", row.status != "0");
        $("#savebutton").attr("disabled", row.status != "0");
        $("#examinebutton").attr("disabled", row.status == "1");
        $("#closebutton").attr("disabled", row.status == "0" || row.status == "2");
        $("#addconditionbutton").attr("disabled", row.status != "0");
    }
}

function addCommissionRate() {
    var agreementid = $("#agreementid").val();
    if (agreementid != "") {
        $.ajax({
            url: 'addCommissionRate',
            type: 'POST',
            dataType: "json",
            data: {
                agreementid: agreementid
            },
            async: true,
            error: function () {
                alertmsg("Connection error");
            },
            success: function (data) {
                if (data.success) {
                    queryCommissionRate(agreementid);
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

function copyCommissionRate() {
    var agreementid = $("#agreementid").val();
    var commissionrateid = $("#commissionrateid").val();
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
                    queryCommissionRate(agreementid);
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
        var rate = $("#rate").val();
        var remark = $("#remark").val();
        var effectivedate = $("#effectivedate").val();
        var arr = effectivedate.split("-");
        var effectivetime = new Date(arr[0], parseInt(arr[1]) - 1, arr[2]);
        $.ajax({
            url: 'updateCommissionRate',
            type: 'POST',
            dataType: "json",
            data: {
                id: commissionrateid,
                commissiontype: commissiontype,
                rate: rate,
                remark:remark,
                effectivetime: effectivetime
            },
            async: true,
            error: function () {
                alertmsg("Connection error");
            },
            success: function (data) {
                if (data.success) {
                    queryCommissionRate(agreementid);
                    alertmsg(data.result);
                }
                else
                    alertmsg(data.result)
            }
        });
    }
}

function delCommissionRate(delId) {
    var agreementid = $("#agreementid").val();
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
                    queryCommissionRate(agreementid);
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
                queryCommissionRate(agreementid);
                $("#collapseOne").attr("class", "panel-collapse collapse");
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

// 默认一页显示5条记录
var pagesize = 5;
// 当前调用的url
var pageurl = "queryCommissionRate";