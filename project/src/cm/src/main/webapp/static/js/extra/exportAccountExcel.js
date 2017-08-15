require(["jquery", "bootstrap-table", "bootstrap", "bootstrapTableZhCn", "zTree", "zTreecheck", "public", "bootstrapdatetimepicker", "bootstrapdatetimepickeri18n"], function ($) {
//数据初始化
    $(function () {
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

        $("#sumstartdate").datetimepicker({
            language: 'zh-CN',
            format: "yyyy-mm",
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

        $("#listType").change(function(){
            var listType = $("#listType  option:selected").val();
            if(listType=='3'){
                var d=new Date();
                var startdate='';
                var enddate='';
                startdate +=d.getFullYear()+'-01-01';
                enddate +=d.getFullYear()+'-';
                enddate +=(d.getMonth()<9? '0'+ (d.getMonth()+1):(d.getMonth()+1))+'-'; //获取当前月份（0——11）
                enddate +=(d.getDate()<10?'0'+d.getDate():d.getDate());
                $("#startdate").val(startdate);
                $("#enddate").val(enddate);
            }else{
                $("#startdate").val('');
                $("#enddate").val('');
            }
        });
    });
    // 查询条件【重置】按钮
    $("#resetbutton").on("click", function (e) {
        $("#startdate").val("");
        $("#enddate").val("");
    });

    // 查询条件【重置】按钮
    $("#sumresetbutton").on("click", function (e) {
        $("#sumstartdate").val("");
    });

//导出业务清单
    $("#businesslist").click(function () {
        var startdate = $("#startdate").val();
        var enddate = $("#enddate").val();
        if(startdate==''){
            alertmsg('起始时间不能为空');
            return ;
        }
        if(enddate==''){
            alertmsg('结束时间不能为空');
            return ;
        }
        $("#exportexcel").submit();
    });

    $("#sumbusinesslist").click(function () {
        var startdate = $("#sumstartdate").val();
        if(startdate==''){
            alertmsg('月份不能为空');
            return ;
        }
        $("#exportsumexcel").submit();
    });
});