require(["jquery", "bootstrap-table", "bootstrap", "bootstrapTableZhCn", "zTree", "zTreecheck", "public", "bootstrapdatetimepicker", "bootstrapdatetimepickeri18n"], function ($) {
//数据初始化
    $(function () {
        $('#table-list').bootstrapTable({
            method: 'get',
            url: pageurl,
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server',
            pageSize: pagesize,
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [
                {
                    field: 'id',
                    title: '流水号',
                    visible: false,
                    switchable: false
                },
                {
                    field: 'paramname',
                    title: '参数名称',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'paramcnname',
                    title: '参数中文名称',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'datatype',
                    title: '数据类型',
                    align: 'center',
                    valign: 'middle',
                    clickToSelect: true,
                    sortable: true
                },
                {
                    field: 'isdefaultview',
                    title: '是否有默认值',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'defaultvalue',
                    title: '默认值',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'paramtag',
                    title: '适用范围',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'statusview',
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

        $('#table-list').bootstrapTable(
            'refresh',
            {url: pageurl });
    });

    // 页面【查询】按钮
    $("#querybutton").on("click", function (e) {
        $("#lastparamkey").val($("#paramkey").val());
        queryParams();
    });

    // 查询条件【重置】按钮
    $("#resetbutton").on("click", function (e) {
        $("#lastparamkey").val("");
        $("#paramkey").val("");
        queryParams();
    });

    // 页面【添加参数】按钮
    $("#addparambutton").on("click", function (e) {
        $('#editTip').val("添加参数");
        $('#id').val("");
        $('#paramname').val("");
        $('#paramcnname').val("");
        $('#datatype').val("String");
        $('#isdefault').val("0");
        $('#defaultvalue').val("");
        $('#sqlname').val("");
        $('#fieldname').val("");
        $('#status').val("1");
        $("input[name='paramtag']").prop("checked", false);
        $("#paramname").attr("readonly", false);
        $('#editModel').modal('show');
    });

    //绑定编辑确认事件
    $("#execButton").click(function () {

        if ($('#paramname').val() == "")
            return alertmsg("请填写参数名称");

        if ($('#paramcnname').val() == "")
            return alertmsg("请填写参数中文名称");

        if ($("input[name='paramtag']:checked").length == 0)
            return alertmsg("请选择适用范围");

        $.ajax({
            url: "saveConditionParam",
            type: 'post',
            dataType: "json",
            data: $('#paramForm').serialize(),
            cache: false,
            async: true,
            error: function () {
                alertmsg("Connection error");
            },
            success: function (data) {
                if (data.success) {
                    $('#editModel').modal('hide');
                    queryParams();
                    alertmsg("操作成功");
                }
                else
                    alertmsg(data.result);
            }
        });
    });
});

function operateFormatter1(value, row, index) {
    return [ '<a class="edit m-left-5" href="javascript:void(0)" name="edit" title="编辑">',
        '<i class="glyphicon glyphicon-edit"></i>', '</a>',
        '<a class="remove m-left-5" href="javascript:void(0)" name="del" title="删除">',
        '<i class="glyphicon glyphicon-remove"></i>', '</a>'].join('');
}
// 事件相应
window.operateEvents1 = {
    'click .edit': function (e, value, row, index) {
        $('#editTip').val("编辑参数");
        loadParam(row);
        $('#editModel').modal('show');
    },
    'click .remove': function (e, value, row, index) {
        if (confirm("确认删除吗?")) {
            delParam(row.id)
        }
    }
};

function queryParams() {
    var paramkey = $("#lastparamkey").val();
    $.ajax({
        url: pageurl,
        type: 'GET',
        dataType: "json",
        data: {
            paramkey: paramkey,
            limit: pagesize
        },
        async: true,
        error: function () {
            alertmsg("Connection error");
        },
        success: function (data) {
            $('#table-list').bootstrapTable('load', data);
        }
    });
}

function loadParam(row) {
    if (row.id != null && row.id != "") {
        $('#id').val(row.id);
        $('#paramname').val(row.paramname);
        $('#paramcnname').val(row.paramcnname);
        $('#datatype').val(row.datatype);
        $('#isdefault').val(row.isdefault);
        $('#defaultvalue').val(row.defaultvalue);
        $('#sqlname').val(row.sqlname);
        $('#fieldname').val(row.fieldname);
        $('#status').val(row.status);
        var paramtaglist = row.paramtag.split(",");
        $("input[name='paramtag']").prop("checked", false);
        for (var i = 0; i < paramtaglist.length; i++) {
            $('#paramtag' + paramtaglist[i]).prop("checked", true);
        }
        $("#paramname").attr("readonly", true);
    }
}

function delParam(delId) {
    if (delId != "") {
        $.ajax({
            url: "delConditionParam",
            type: 'POST',
            dataType: "json",
            data: "id=" + delId,
            async: true,
            error: function () {
                alertmsg("Connection error");
            },
            success: function (data) {
                if (data.success) {
                    queryParams();
                    alertmsg("删除成功！");
                }
                else
                    alertmsg(data.result);
            }
        });
    }
}

function reloaddata() {
    $.ajax({
        url: pageurl,
        type: 'GET',
        dataType: "json",
        data: ({limit: pagesize, offset: 0}),
        async: true,
        error: function () {
            alertmsg("Connection error");
        },
        success: function (data) {
            $('#table-list').bootstrapTable('load', data);
        }
    });
}

//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "queryParamsList";
