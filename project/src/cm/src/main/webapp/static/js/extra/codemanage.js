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
                    field: 'codevalue',
                    title: '错误码',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'codetype',
                    title: '类型',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'codename',
                    title: '掌中宝前端提示',
                    align: 'center',
                    valign: 'middle',
                    sortable: true
                },
                {
                    field: 'prop2',
                    title: '渠道错误提示',
                    align: 'center',
                    valign: 'middle',
                    clickToSelect: true,
                    sortable: true
                },
                {
                    field: 'noti',
                    title: '说明',
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

        $('#table-list').bootstrapTable('refresh', {url: pageurl });
    });
    
  //绑定编辑确认事件
    $("#execButton").click(function () {
    	var fcodename = $("#codename").val();
    	var fprop2 = $("#prop2").val();
    	var fid = $("#id").val();
    	var fnoti = $("#noti").val();
        if (fcodename.length == 0)
        	return alertmsg("请填写掌中宝前端提示!" + fid);
        if (fcodename.length > 100)
        	return alertmsg("掌中宝前端提示超过100字!");
        
        if (fprop2.length == 0)
        	return alertmsg("请填写渠道提示!");
        if (fprop2.length > 100)
        	return alertmsg("渠道提示超过100字!");

        $.ajax({
            url: "saveeditcode",
            type: 'post',
            dataType: "json",
            data: ({
            	id:fid,
            	codename:fcodename,
            	prop2:fprop2,
            	noti:fnoti
            }),
            cache: false,
            async: true,
            error: function () {
                alertmsg("Connection error");
            },
            success: function (data) {
                if (data.success) {
                    $('#editModel').modal('hide');
                    queryParams();
                    alertmsg(data.message);
                }
                else
                    alertmsg(data.message);
            }
        });
    });
});

//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "querycodelist";


function operateFormatter1(value, row, index) {
    return [ '<a class="edit m-left-5" href="javascript:void(0)" name="edit" title="修改">',
        '<i class="glyphicon glyphicon-edit"></i>', '</a>'].join('');
}
// 事件相应
window.operateEvents1 = {
    'click .edit': function (e, value, row, index) {
        $('#editTip').val("修改提示");
        loadParam(row);
        $('#editModel').modal('show');
    }
};

function queryParams() {
    $.ajax({
        url: pageurl,
        type: 'GET',
        dataType: "json",
        data: {
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
        $('#codename').val(row.codename);
        $('#prop2').val(row.prop2);
        $('#noti').val(row.noti);
    }
}
    