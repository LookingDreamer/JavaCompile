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
                    title: '记账时间',
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
                    field: 'fundsource',
                    title: '资金来源',
                    align: 'center',
                    valign: 'middle',
                    clickToSelect: true,
                    sortable: true
                },
                {
                    field: 'fundtype',
                    title: '收支类型',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'amount',
                    title: '金额',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'balance',
                    title: '余额',
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
            {url: 'queryDetailsList' });
    });

    // 用户列表页面【查询】按钮
    $("#querybutton").on("click", function (e) {
        var agentname = $("#agentname").val();
        var fundsource = $("#fundsource").val();
        var fundtype = $("#fundtype").val();
        var startdate = $("#startdate").val();
        var enddate = $("#enddate").val();
        $('#table-list').bootstrapTable(
            'refresh',
            {url: 'queryDetailsList?limit=10&agentname=' + agentname
                + '&fundsource=' + fundsource + '&fundtype=' + fundtype
                + '&startdate=' + startdate + '&enddate=' + enddate
            });
    });

    // 查询条件【重置】按钮
    $("#resetbutton").on("click", function (e) {
        $("#agentname").val("");
        $("#fundsource").val("");
        $("#fundtype").val("");
        $("#startdate").val("");
        $("#enddate").val("");
    });
});

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
var pageurl = "queryDetailsList";

// 添加事件
function operateFormatter1(value, row, index) {
    return  [
        '<a class="noti m-left-5" href="javascript:void(0)" title="详情">',
        '详情',
        '</a>'
    ].join('');
}


// 事件相应
window.operateEvents1 = {
    'click .noti' : function(e, value, row, index) {
        var noti = row.noti;
        var htmls = "";
        if(noti.match("^\{(.+:.+,*){1,}\}$")){
            var jsonNoti = $.parseJSON(noti);
            htmls = htmls + '<tr>';
            var count = 0;
            for(var key in jsonNoti){
                count++;
                var jValue=jsonNoti[key];//key所对应的value
                //alert(key+jValue);
                htmls = htmls +
                ' <td class="text-left" style="width: 200px">'+key+'：'+jValue+'</td>';
               if(count%3==0){
                   htmls = htmls + ' </tr>'+'<tr>';
               }
            }
            htmls = htmls + '</tr>';
        }else{
            htmls = htmls +
            '<tr>'+
            ' <td class="text-left" style="width: 600px">'+noti+'</td>'+
            ' </tr>';
        }
        $("#notiTable").html(htmls);
        $('#notiTableModel').modal('show');
    }
};