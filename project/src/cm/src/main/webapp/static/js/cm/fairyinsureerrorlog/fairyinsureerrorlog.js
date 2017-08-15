/**
 * Created by Administrator on 2017/3/27.
 */
// require(["jquery", "bootstrapdatetimepicker", "bootstrap-table", "bootstrap", "bootstrapTableZhCn", "bootstrap-table-export","tableExport","zTree",
//     "zTreecheck", "flat-ui", "bootstrapdatetimepickeri18n", "public"], function ($) {
require(["jquery", "bootstrapdatetimepicker", "zTree","bootstrap-table", "bootstrap","bootstrapTableZhCn",
    "bootstrapdatetimepickeri18n","public","bootstrap-table-export","tableExport"], function ($) {
    window.operateEvents = {
        'click .review-details': function (e, value, row, index) {
            // alert('You click like action, row: ' + JSON.stringify(row));

            // window.parent.openLargeDialog("business/warranty/orderDetails?orderNo=" + row.orderNo);
        }
    };

    function operateFormatter() {
        return [
            '<a class="review-details" href="javascript:void(0)" title="review details">',
            '查看详情',
            '</a>'
        ].join('');
    }

    function timeFormatter(value) {
        if (value) {
            return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
        }
    }

    function currencyFormatter(value) {
        if(!isNaN(value)) {
            return value.toFixed(2);
        }
    }

    function insureStatusFormatter(value) {
        if (value=='D') {
            return "成功";
        }
        return "失败";
    }

    $(function () {
        //默认一页显示十条记录
        var pagesize = 10;
        //当前调用的url
        var pageurl = "fairyinsureerrors";
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
            $("#handleTimeStart").val(dateString);
            $("#handleTimeEnd").val(dateString);
        }

        // 重置按钮
        $("#resetbutton").on("click", function (e) {
            $("#taskId").val("");
            initQueryTaskDate();
        });

        initQueryTaskDate();
        pageurlparameter = "?";
        if ($.trim($("#handleTimeStart").val())) {
            addParameter("handleTimeStart", $.trim($("#handleTimeStart").val()).substring(0, 10).trim() + " 00:00:00");
        }
        if ($.trim($("#handleTimeEnd").val())) {
            addParameter("handleTimeEnd", $.trim($("#handleTimeEnd").val()).substring(0, 10).trim() + " 23:59:59");
        }

        $('#table-javascript').bootstrapTable({
            method: 'get',
            url: '',
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server',
            minimumCountColumns: 2,
            clickToSelect: true,
            singleSelect: true,
            showToggle: true,  //test
            cardView: false,  //test
            showColumns: true,//test
            toolbarAlign: 'left',
            columns: [{
                field: 'taskId',
                title: '任务号',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'carlicenseno',
                title: '车牌号',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'insuredname',
                title: '被保人',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'prvshotname',
                title: '保险公司',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'agentname',
                title: '代理人姓名',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'paychannelname',
                title: '支付方式',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'totalAmount',
                title: '支付金额',
                align: 'center',
                valign: 'middle',
                sortable: false,
                formatter: currencyFormatter
            }/*, {
                field: 'payTime',
                title: '支付时间',
                align: 'center',
                valign: 'middle',
                sortable: false,
                formatter: timeFormatter
            }*/, {
                field: 'handleTime',
                title: '精灵处理时间',
                align: 'center',
                valign: 'middle',
                sortable: false,
                formatter: timeFormatter
            }, {
                field: 'errorCode',
                title: '承保是否成功',
                align: 'center',
                valign: 'middle',
                sortable: false,
                formatter: insureStatusFormatter
            }, {
                field: 'errorDesc',
                title: '描述',
                align: 'center',
                valign: 'middle',
                sortable: false
            }]
        });

        //--------------------------------------------------
        var $table = $('#table-javascript');
        $(function () {
            $('#toolbar').find('select').change(function () {
                $table.bootstrapTable('destroy').bootstrapTable({
                    exportDataType: $(this).val(),
                });
            });
        });

        //修改起始时间
        $("#handleTimeStart").change(function () {
            var startString = $("#handleTimeStart").val();//获取当前显示的时间字符串
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
            $("#handleTimeEnd").datetimepicker('setEndDate', endTime);
            var showEnd = $("#handleTimeEnd").val();//获取当前的截止日期
            var dateShowEnd = Date.parse(showEnd);
            var currentEnd = new Date(dateShowEnd);
            var days = 0;
            if (currentEnd > startime) {
                days = (currentEnd - startime) / (1000 * 60 * 60 * 24);
            } else {
                days = (startime - currentEnd) / (1000 * 60 * 60 * 24);
            }
            if (days > 30) {//若页面显示的时间差超过一个月,则将显示的截止时间改为设置的截止时间
                $("#handleTimeEnd").val(endString);
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
        var reloadCarTaskInfoByTasktype = function (pagetasktype) {
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
        $("input#taskId").keydown(function (event) {
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
            if ($.trim($("#handleTimeStart").val()) && $.trim($("#handleTimeEnd").val())) {
                timeupstr = ($.trim($("#handleTimeStart").val()).substring(0, 10) + " 00:00:00");
                timedownstr = ($.trim($("#handleTimeEnd").val()).substring(0, 10) + " 23:59:59");

                var timeup = new Date(timeupstr);
                var timedown = new Date(timedownstr);
                if (timeup >= timedown) {
                    alertmsg("任务创建截止时间应晚于开始时间");
                    return;
                }
            }

            var taskId = $('#taskId').val().trim();
            // var plateNumber = $('#plateNumber').val().trim();

            // if (!(orderNo || plateNumber || insuredName || orderStatus || payChannel || transactionId)) {
            //     alert("查询参数至少有一项不为空");
            //     return;
            // }
            pageurlparameter = "?";

            if (taskId) {
                addParameter("taskId", taskId);
            }
            // if (plateNumber) {
            //     addParameter("plateNumber", plateNumber);
            // }
            if (timeupstr) {
                addParameter("startTime", timeupstr);
            }

            if (timedownstr) {
                addParameter("endTime", timedownstr);
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