require(["jquery", "bootstrapdatetimepicker", "bootstrap-table", "bootstrap","bootstrapTableZhCn","zTree","zTreecheck","flat-ui","bootstrapdatetimepickeri18n","public"], function ($) {
    //默认一页显示十条记录
    var pagesize = 10;
    //当前调用的url
    var pageurl = "showloopunderwritinglist";
    //当前调用的url查询参数
    var pageurlparameter = "";

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
        startDate:'-3m',
        endDate:new Date()
    });
    // 时间控件初始化为今天
    function initQueryTaskDate(){
        var date = new Date();
        var todayMounth = (date.getMonth()+1)+"";
        if(todayMounth.length<2){
            todayMounth = "0"+todayMounth;
        }
        var dateString = date.getFullYear()+"-"+todayMounth+"-"+date.getDate();
        $("#taskcreatetimeup").val(dateString);
        $("#taskcreatetimedown").val(dateString);
    }

    // 重置按钮
    $("#resetbutton").on("click", function(e) {
        $("#insuredname").val("");
        $("#carlicenseno").val("");
        $("#mainInstanceId").val("");
    });

    $(function() {
        initQueryTaskDate();
        pageurlparameter = "?";
        if($.trim($("#taskcreatetimeup").val())){
            addParameter("taskcreatetimeup",$.trim($("#taskcreatetimeup").val()).substring(0,10).trim() + " 00:00:00");
        }
        if($.trim($("#taskcreatetimedown").val())){
            addParameter("taskcreatetimedown",$.trim($("#taskcreatetimedown").val()).substring(0,10).trim() + " 23:59:59");
        }

        $('#table-javascript').bootstrapTable({
            method: 'get',
            url:'',
            cache: false,
            striped: true,
            pagination: true,
            pageList: [5, 10, 15, 20],
            sidePagination: 'server',
            pageSize: pagesize,
            minimumCountColumns: 2,
            clickToSelect: true,
            singleSelect: true,
            showToggle:true,  //test
            cardView: false,  //test
            showColumns: true,//test
            columns: [{
                field: 'taskid',
                title: '任务号',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'platenumber',
                title: '车牌号',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'insuredname',
                title: '被保险人',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'loopstatus',
                title: '轮询状态',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'taskcreatetime',
                title: '任务创建时间',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'detail',
                title: '操作',
                align: 'center',
                valign: 'middle',
                switchable:false
            }]
        });
    });

    //修改起始时间
    $("#taskcreatetimeup").change(function(){
        var startString=$("#taskcreatetimeup").val();//获取当前显示的时间字符串
        var dateStartime=Date.parse(startString);//解析时间字符串
        var startime=new Date(dateStartime);//创建时间
        var endYear = startime.getFullYear();
        var endMonth=(startime.getMonth()+2)+"";//获取时间的月份,在月份的基础上加1个月.因为月份是从0开始,所以+2
        if (endMonth > 12) {//计算下个月的月份超过12则为下年1月
            endYear += 1;
            endMonth = endMonth - 12 + "";
        }
        var days = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);//平年每月天数
        var enday=startime.getDate()+"";//获取日份
        if (days[endMonth - 1] < enday) {//如果起始时间有31号下个月不一定有31号
            if ((endYear % 4 == 0 && endYear % 400 != 0 || endYear % 400 == 0) && endMonth == 2)//闰年2月
                enday = days[endMonth - 1] + 1;
            else
                enday = days[endMonth - 1];
        }
        if(endMonth.length<2){
            endMonth="0"+endMonth;
        }
        if(enday.length<2){
            enday="0"+enday;
        }
        var endString = endYear+"-"+endMonth+"-"+enday;//跨度一个月的截止时间字符串
        var dateEndTime=Date.parse(endString);
        var endTime=new Date(dateEndTime);//解析为时间类型
        if(endTime>new Date()){//若开始时间为今天,那么截止时间可以为下个月的今天,所以需要判读
            endTime=new Date();//如果最大可选时间大于当前的时间,则最大可选时间为当前时间
            var todayMonth=(endTime.getMonth()+1)+"";
            if(todayMonth.length<2){
                todayMonth="0"+todayMonth;
            }
            var todayDay=(endTime.getDate())+"";
            if(todayDay.length<2){
                todayDay="0"+todayDay;
            }
            endString=endTime.getFullYear()+"-"+todayMonth+"-"+todayDay;
        }
        $("#taskcreatetimedown").datetimepicker('setEndDate',endTime);
        var showEnd=$("#taskcreatetimedown").val();//获取当前的截止日期
        var dateShowEnd=Date.parse(showEnd);
        var currentEnd=new Date(dateShowEnd);
        var days=0;
        if(currentEnd>startime){
            days=(currentEnd-startime)/(1000*60*60*24);
        }else{
            days=(startime-currentEnd)/(1000*60*60*24);
        }
        if(days>30){//若页面显示的时间差超过一个月,则将显示的截止时间改为设置的截止时间
            $("#taskcreatetimedown").val(endString);
        }
    });

    //修改截止时间
    $("#taskcreatetimedown").change(function(){
        var endString=$("#taskcreatetimedown").val();
        var dateEnd=Date.parse(endString);
        var endTime=new Date(dateEnd);
        var startMonth=endTime.getMonth();
        if(startMonth==0){//当是一月的时候得到就是0,显示的时候就是0月.所以要+1
            startMonth=1;
        }
        startMonth=startMonth+"";
        if(startMonth.length<2){//改变月份显示为两位显示
            startMonth="0"+startMonth;
        }
        var startDay=endTime.getDate()+"";
        if(startDay.length<2){
            startDay="0"+startDay;
        }
        var startString=endTime.getFullYear()+"-"+startMonth+"-"+startDay;
        var startime=new Date(Date.parse(startString));
        var today=new Date();
        var beforeMonth=today.getMonth()-2;
        var beforeYear=today.getFullYear();
        if(beforeMonth<0){
            beforeMonth=13+beforeMonth;
            beforeYear=beforeYear-1;//往前推一年
        }
        beforeMonth=beforeMonth+"";
        if(beforeMonth.length<2){
            beforeMonth="0"+beforeMonth;
        }
        var beforeDay=today.getDate()+"";
        if(beforeDay.length<2){
            beforeDay="0"+beforeDay;
        }
        var beforeString=beforeYear+"-"+beforeMonth+"-"+beforeDay;
        var beforeDate=new Date(Date.parse(beforeString));//从今天往前三个月的时间
        if(startime<beforeDate){
            startime=beforeDate;
            startString=beforeString;
        }
        //$("#taskcreatetimeup").datetimepicker('setStartDate',startime);
        var showStart=$("#taskcreatetimeup").val();//获取当前的截止日期
        var dateShowStart=Date.parse(showStart);
        var currentStart=new Date(dateShowStart);
        var days=0;
        if(currentStart>endTime){
            days=(currentStart-endTime)/(1000*60*60*24);
        }else{
            days=(endTime-currentStart)/(1000*60*60*24);
        }
        if(days>30){//若页面显示的时间差超过一个月,则将显示的截止时间改为设置的截止时间
            $("#taskcreatetimeup").val(startString);
        }
    });

    // 车险任务记录高级查询条件赋值
    function addParameter(parameterName,parameterValue){
        if(pageurlparameter.length == 1){
            pageurlparameter += parameterName + "=" + parameterValue;
        }else{
            pageurlparameter += "&" + parameterName + "=" + parameterValue;
        }
    }

    // 高级查询按钮（车辆任务列表）
    $("#superquerybutton").on("click", function(e) {
        var taskcreatetimeupvalue = $.trim($("#taskcreatetimeup").val());
        var taskcreatetimedownvalue = $.trim($("#taskcreatetimedown").val());

        if(taskcreatetimeupvalue && taskcreatetimedownvalue){
            var timeupdatestr = taskcreatetimeupvalue.substring(0,11);
            var timedowndatestr = taskcreatetimedownvalue.substring(0,11);
            var timeupstr = (timeupdatestr + " 00:00:00").replace(/-/g,"/");
            var timedownstr = (timedowndatestr + " 23:59:59").replace(/-/g,"/");

            var timeup = new Date(timeupstr);
            var timedown = new Date(timedownstr);
            if(timeup >= timedown){
                alertmsg("任务创建截止时间应晚于开始时间");
                return;
            }

            var timeupdate = timeup;
            var timedowndate = new Date((timedowndatestr + " 00:00:00").replace(/-/g,"/"));
            var iDays = parseInt(Math.abs(timedowndate - timeupdate) / 1000 / 60 / 60 /24);

            if(iDays > 2) {
                alertmsg("任务创建时间暂时只开放3天的查询区间");
                return;
            }
        }

        var insuredname=$.trim($("#insuredname").val());
        var carlicenseno=$.trim($("#carlicenseno").val());
        var taskid=$.trim($("#mainInstanceId").val());

        if(!(insuredname || carlicenseno || taskid || taskcreatetimeupvalue || taskcreatetimedownvalue)){
            alert("请输入筛选条件!");
            return;
        }

        pageurlparameter = "?";
        if(insuredname){
            addParameter("insuredname", insuredname);
        }
        if(carlicenseno){
            addParameter("carlicenseno", carlicenseno);
        }
        if(taskid){
            addParameter("mainInstanceId", taskid);
        }
        if(taskcreatetimeupvalue){
            addParameter("taskcreatetimeup",taskcreatetimeupvalue.substring(0,10).trim() + " 00:00:00");
        }
        if(taskcreatetimedownvalue){
            addParameter("taskcreatetimedown",taskcreatetimedownvalue.substring(0,10).trim() + " 23:59:59");
        }

        if(pageurlparameter.length <=1){
            alert("至少录入一个查询条件!");
            return;
        }

        $('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
    });

});