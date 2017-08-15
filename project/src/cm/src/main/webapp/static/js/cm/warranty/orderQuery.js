require(["jquery", "bootstrapdatetimepicker", "bootstrap-table", "bootstrap", "bootstrapTableZhCn", "zTree", "zTreecheck", "flat-ui", "bootstrapdatetimepickeri18n", "public"], function ($) {

    window.operateEvents = {
        'click .review-details': function (e, value, row, index) {
            // alert('You click like action, row: ' + JSON.stringify(row));

            window.parent.openLargeDialog("business/warranty/orderDetails?orderNo=" + row.orderNo);
        }
    };

    function operateFormatter() {
        return [
            '<a class="review-details" href="javascript:void(0)" title="review details">',
            '查看详情',
            '</a>'
        ].join('');
    }

    $(function () {
        //默认一页显示十条记录
        var pagesize = 10;
        //当前调用的url
        var pageurl = "queryOrders";
        //当前调用的url查询参数
        var pageurlparameter = "";
        //简单查询车牌被保人参数
        var simpleparameter = "";

        //时间控件初始化设置
        $('.form_datetime').datetimepicker({
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
            startDate: '-3m',
            endDate: new Date()
        });
        // 时间控件初始化为今天
        function initQueryTaskDate() {
            var date = new Date();
            var todayMounth = (date.getMonth() + 1) + "";
            if (todayMounth.length < 2) {
                todayMounth = "0" + todayMounth;
            }
            var dateString = date.getFullYear() + "-" + todayMounth + "-" + date.getDate();
            $("#createTimeStart").val(dateString);
            $("#createTimeEnd").val(dateString);
        }


        // 重置按钮
        $("#resetbutton").on("click", function (e) {
            $("#carlicenseno").val("");
            $("#insuredname").val("");
            $("#usertype").val("");
            $("#tasktype").val("");
            $("#taskstatus").val("");
            $("#taskcreatetimeup").val("");
            $("#taskcreatetimedown").val("");
            $("#agentNum").val("");
            $("#agentName").val("");
            $("#mainInstanceId").val("");
            $("#deptcode").val("");
            $("#deptname").val("");
            $("#inscomcode").val("");
            $("#inscomname").val("");
            $("#taskstate").val("01");
            $("#operatorname").val("");
            $("#channelinnercode").val("");
            $("#channelName").val("");
            //		initQueryTaskDate();
        });

        initQueryTaskDate();
        pageurlparameter = "?";
        if ($.trim($("#taskcreatetimeup").val())) {
            addParameter("taskcreatetimeup", $.trim($("#taskcreatetimeup").val()).substring(0, 10).trim() + " 00:00:00");
        }
        if ($.trim($("#taskcreatetimedown").val())) {
            addParameter("taskcreatetimedown", $.trim($("#taskcreatetimedown").val()).substring(0, 10).trim() + " 23:59:59");
        }

        $('#table-javascript').bootstrapTable({
            method: 'get',
            url: '',
            cache: false,
            striped: true,
            pagination: true,
            pageList: [5, 10, 15, 20],
            sidePagination: 'server',
            pageSize: pagesize,
            minimumCountColumns: 2,
            clickToSelect: true,
            singleSelect: true,
            showToggle: true,  //test
            cardView: false,  //test
            showColumns: true,//test
            columns: [{
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            }, {
                field: 'orderNo',
                title: '订单号',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'createTime',
                title: '创建时间',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'carownerName',
                title: '车主',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'plateNumber',
                title: '车牌号',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'payChannel',
                title: '支付方式',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'orderStatus',
                title: '订单状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'amount',
                title: '保费',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'other',
                title: '详细信息',
                align: 'center',
                valign: 'middle',
                sortable: false,
                events: operateEvents,
                formatter: operateFormatter
            }]
        });

        //--------------------------------------------------


        //修改起始时间
        $("#taskcreatetimeup").change(function () {
            var startString = $("#taskcreatetimeup").val();//获取当前显示的时间字符串
            var dateStartime = Date.parse(startString);//解析时间字符串
            var startime = new Date(dateStartime);//创建时间
            var endMonth = (startime.getMonth() + 2) + "";//获取时间的月份,在月份的基础上加1个月.因为月份是从0开始,所以+2
            var enday = startime.getDate() + "";//获取日份
            if (endMonth.length < 2) {
                endMonth = "0" + endMonth;
            }
            if (enday.length < 2) {
                enday = "0" + enday;
            }
            var endString = startime.getFullYear() + "-" + endMonth + "-" + enday;//跨度一个月的截止时间字符串
            var dateEndTime = Date.parse(endString);
            var endTime = new Date(dateEndTime);//解析为时间类型
            if (endTime > new Date()) {//若开始时间为今天,那么截止时间可以为下个月的今天,所以需要判读
                endTime = new Date();//如果最大可选时间大于当前的时间,则最大可选时间为当前时间
                var todayMonth = (endTime.getMonth() + 1) + "";
                if (todayMonth.length < 2) {
                    todayMonth = "0" + todayMonth;
                }
                var todayDay = (endTime.getDate()) + "";
                if (todayDay.length < 2) {
                    todayDay = "0" + todayDay;
                }
                endString = endTime.getFullYear() + "-" + todayMonth + "-" + todayDay;
            }
            $("#taskcreatetimedown").datetimepicker('setEndDate', endTime);
            var showEnd = $("#taskcreatetimedown").val();//获取当前的截止日期
            var dateShowEnd = Date.parse(showEnd);
            var currentEnd = new Date(dateShowEnd);
            var days = 0;
            if (currentEnd > startime) {
                days = (currentEnd - startime) / (1000 * 60 * 60 * 24);
            } else {
                days = (startime - currentEnd) / (1000 * 60 * 60 * 24);
            }
            if (days > 30) {//若页面显示的时间差超过一个月,则将显示的截止时间改为设置的截止时间
                $("#taskcreatetimedown").val(endString);
            }
        });

        // 车险任务记录高级查询条件赋值
        function addParameter(parameterName, parameterValue) {
            if (pageurlparameter.length == 1) {
                pageurlparameter += parameterName + "=" + parameterValue;
            } else {
                pageurlparameter += "&" + parameterName + "=" + parameterValue;
            }
        }

        //简单查询任务状态链接点击事件
        reloadCarTaskInfoByTasktype = function (pagetasktype) {
            if ($.trim(pagetasktype)) {
                tasktype = "tasktype=" + $.trim(pagetasktype);
            } else {
                tasktype = "";
            }
            var paramStr = getParamStr();
            $('#table-javascript').bootstrapTable('refresh', {url: pageurl + paramStr});
        };
        // 一般查询按钮（车辆任务列表）
        function executeGeneralQuery() {
            tasktype = "";
            simpleparameter = "";
            if ($.trim($("#carlicensenoOrinsuredname").val())) {
                simpleparameter = "carlicensenoOrinsuredname=" + $.trim($("#carlicensenoOrinsuredname").val());
            }
            var paramStr = getParamStr();
            $('#table-javascript').bootstrapTable('refresh', {url: pageurl + paramStr});
        }

        $("#generalquerybutton").on("click", function (e) {
            executeGeneralQuery();
        });
        // 查询条件【重置】按钮
        $("#generalresetbutton").on("click", function (e) {
            $("#carlicensenoOrinsuredname").val("");
        });
        //绑定输入框回车查询事件
        $("input#carlicensenoOrinsuredname").keydown(function (event) {
            var e = event || window.event || arguments.callee.caller.arguments[0];
            //13是键盘上面固定的回车键
            if (e && e.keyCode == 13) {
                //一般查询
                executeGeneralQuery();
            }
        });
        // 高级查询按钮（车辆任务列表）
        $("#superquerybutton").on("click", function (e) {
            var timeupstr;
            var timedownstr;
            if ($.trim($("#createTimeStart").val()) && $.trim($("#createTimeEnd").val())) {
                timeupstr = ($.trim($("#createTimeStart").val()).substring(0, 10) + " 00:00:00");
                timedownstr = ($.trim($("#createTimeEnd").val()).substring(0, 10) + " 23:59:59");

                var timeup = new Date(timeupstr);
                var timedown = new Date(timedownstr);
                if (timeup >= timedown) {
                    alertmsg("任务创建截止时间应晚于开始时间");
                    return;
                }
            }

            var orderNo = $('#orderNo').val().trim();
            var plateNumber = $('#plateNumber').val().trim();
            var insuredName = $('#insuredName').val().trim();
            var orderStatus = $('#orderStatus').val().trim();
            var payChannel = $('#payChannel').val().trim();
            var transactionId = $('#transactionId').val().trim();

            // if (!(orderNo || plateNumber || insuredName || orderStatus || payChannel || transactionId)) {
            //     alert("查询参数至少有一项不为空");
            //     return;
            // }
            pageurlparameter = "?";
            if (orderNo) {
                addParameter("orderNo", orderNo);
            }
            if (plateNumber) {
                addParameter("plateNumber", plateNumber);
            }
            if (insuredName) {
                addParameter("insuredName", insuredName);
            }
            if (orderStatus) {
                addParameter("orderStatus", orderStatus);
            }
            if (payChannel) {
                addParameter("payChannel", payChannel);
            }
            if (transactionId) {
                addParameter("transactionId", transactionId);
            }
            if (timeupstr) {
                addParameter("startDate", timeupstr);
            }

            if (timedownstr) {
                addParameter("endDate", timedownstr);
            }

            $('#table-javascript').bootstrapTable('refresh', {url: pageurl + pageurlparameter});
        });

        function messageNotReadCount() {
            $.ajax({
                type: "get",
                url: "/cm/message/getNotReadCount",
                data: {receiver: $("#personname").val()},
                dataType: "text",
                success: function (data) {
                    $("#messageNotReadCount", window.parent.document).text(data);
                }
            });
        }
    });


});