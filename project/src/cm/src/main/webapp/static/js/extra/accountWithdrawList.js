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
                    field: 'createtime',
                    title: '提现申请时间',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'agentname',
                    title: '用户姓名',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'bankname',
                    title: '转入银行',
                    align: 'center',
                    valign: 'middle',
                    clickToSelect: true,
                    sortable: true
                },
                {
                    field: 'cardnumber',
                    title: '转入银行账号',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'amount',
                    title: '提现金额',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'status',
                    title: '提现状态',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'noti',
                    title: '备注',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'operating',
                    title: '状态变更',
                    align: 'center',
                    valign: 'middle',
                    switchable: false,
                    formatter: operateFormatter1,
                    events: operateEvents1
                }
            ]
        });

        $("#startdate").datetimepicker({
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
            endDate: new Date()
        }).on('changeDate', function (ev) {
            var starttime = $('#startdate').val();
            $("#enddate").datetimepicker({
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
                endDate: new Date()
            })
            $("#enddate").datetimepicker('setStartDate', starttime);
            $("#startdate").datetimepicker('hide');
        });

        $('#table-list').bootstrapTable(
            'refresh',
            {url: 'queryWithdrawList' });
    });

    // 用户列表页面【查询】按钮
    $("#querybutton").on("click", function (e) {
        var agentname = $("#agentname").val();
        var cardnumber = $("#cardnumber").val();
        var status = $("#status").val();
        var startdate = $("#startdate").val();
        var enddate = $("#enddate").val();
        $('#table-list').bootstrapTable(
            'refresh',
            {url: 'queryWithdrawList?limit=10&agentname=' + agentname
                + '&cardnumber=' + cardnumber + '&status=' + status
                + '&startdate=' + startdate + '&enddate=' + enddate
            });
    });

    // 查询条件【重置】按钮
    $("#resetbutton").on("click", function (e) {
        $("#agentname").val("");
        $("#cardnumber").val("");
        $("#status").val("");
        $("#startdate").val("");
        $("#enddate").val("");
    });

//绑定提现成功确认事件
    $("#s_execButton").click(function () {
        var tax = $("#tax").val();
        $.ajax({
            url: "/cm/account/updateWithdrawStatus",
            type: 'post',
            dataType: "text",
            data: {
                id: $("#s_id").val(),
                noti: tax != "" ? "扣除个人税费 " + tax + " 元" : "",
                status: "1"
            },
            cache: false,
            async: true,
            error: function () {
                alertmsg("Connection error");
            },
            success: function (data) {
                if (data == "success") {
                    $('#successModel').modal('hide');
                    $("#querybutton").click();
                    alertmsg("操作成功");
                }
                else
                    alertmsg("操作失败");
            }
        });
    });

//绑定提现失败确认事件
    $("#e_execButton").click(function () {
        var noti = $("#e_noti").val();
        if (noti == "") {
            alertmsg("请填写失败原因");
            return false;
        }
        $.ajax({
            url: "/cm/account/updateWithdrawStatus",
            type: 'post',
            dataType: "text",
            data: {
                id: $("#e_id").val(),
                noti: noti,
                status: "2"
            },
            async: true,
            error: function () {
                alertmsg("Connection error");
            },
            success: function (data) {
                if (data == "success") {
                    $('#errorModel').modal('hide');
                    $("#querybutton").click();
                    alertmsg("操作成功");
                }
                else
                    alertmsg("操作失败");
            }
        });
    });
});

function operateFormatter1(value, row, index) {
    return row.status != "提现中" ? [''].join('') : ['<a class="edit m-left-5" href="javascript:void(0)" title="提现成功">', '提现成功', '</a>', '<a class="remove m-left-5" href="javascript:void(0)" title="提现失败">', '提现失败', '</a>'].join('');
}

window.operateEvents1 = {
    'click .edit': function (e, value, row, index) {
        $("#s_id").val(row.id);
        $("#agentid").val(row.agentid);
        $("#s_agentname").val(row.agentname);
        $("#amount").val(row.amount);
        $('#successModel').modal('show');
    },
    'click .remove': function (e, value, row, index) {
        $("#e_id").val(row.id);
        $("#e_noti").val("");
        $('#errorModel').modal('show');
    }
};

function reloaddata(){
    $.ajax({
        url : pageurl,
        type : 'GET',
        dataType : "json",
        data:({limit:10,offset:0}),
        async : true,
        error : function() {
            alertmsg("Connection error");
        },
        success : function(data) {
            $('#table-list').bootstrapTable('load', data);
        }
    });
}

//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "queryWithdrawList";
