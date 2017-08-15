require(["jquery", "bootstrapdatetimepicker", "bootstrap-table", "bootstrap","bootstrapTableZhCn","zTree","zTreecheck","flat-ui","bootstrapdatetimepickeri18n","public"], function ($) {
    $(function() {
        //默认一页显示十条记录
        var pagesize = 10;
        //当前调用的url
        var pageurl = "showcartaskmanagelist";
        //当前调用的url查询参数
        var pageurlparameter = "";
        //简单查询车牌被保人参数
        var simpleparameter = "";
        //简单查询任务状态参数
        var tasktype = "";

        //隐藏或显示高级查询
        $("#showsuperquerybutton").click(function(){
            $("#superquerypanel").toggle();
            $("#querypanel").toggle();
        });
        $("#superquerypanelclose").click(function(){
            $("#superquerypanel").toggle();
            $("#querypanel").toggle();
        });
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
            var days = date.getDate() + "";
            if(days.length < 2)
                days = "0" + days;
            var dateString = date.getFullYear()+"-"+todayMounth+"-"+days;
            $("#taskcreatetimeup").val(dateString);
            $("#taskcreatetimedown").val(dateString);
        }


        // 重置按钮
        $("#resetbutton").on("click", function(e) {
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
        if($.trim($("#taskcreatetimeup").val())){
            addParameter("taskcreatetimeup",$.trim($("#taskcreatetimeup").val()).substring(0,10).trim() + " 00:00:00");
        }
        if($.trim($("#taskcreatetimedown").val())){
            addParameter("taskcreatetimedown",$.trim($("#taskcreatetimedown").val()).substring(0,10).trim() + " 23:59:59");
        }
        //初始化分配业管列表数据
        $.ajax({
            url:'initUserList4Dispatch',
            type:'post',
            success:function(data){
                if(data.length>0){
                    var contend;
                    for(var i=0;i<data.length;i++){
                        contend +="<option value='"+data[i].usercode+"'>"+data[i].uname+"</option>"
                    }
                    $("#personname_select").append(contend);
                }
            }
        });

        //分配业管级联右侧输入框点击事件
        $("#personname_select").change(function(){
            $("#personname").val($(this).val());
        });

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
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            }, {
                field: 'maininstanceId',
                title: '流程号',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'carlicenseno',
                title: '车牌',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'insuredname',
                title: '被保人',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'name',
                title: '代理人',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'tasktype',
                title: '任务类型',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'taskcreatetime',
                title: '任务创建时间',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'inscomName',
                title: '保险公司',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'deptname',
                title: '出单网点',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'operator',
                title: '处理人',
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: function(value,row,index){
                	if(value==null && row.taskstateName =="已完成"){
                		//当这几个任务状态的操作人为空时显示超级管理员
                		if(row.taskcode=="26"||row.taskcode=="24"||row.taskcode=="23"){
                			return "超级管理员(admin)" ;
                		}
                		return "" ;
                	}else{ 
                		return value ;
                	}
                }
            }, {
                /*field: 'taskstate',
                 title: '任务状态',
                 align: 'center',
                 valign: 'middle',
                 sortable: true
                 }, {*/
                field: 'taskstateName',
                title: '任务状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
//            {
//                field: 'operatorstate', 
//                title: '调度状态',
//                align: 'center',
//                valign: 'middle',
//                sortable: true
//            }, 
                {
                    field: 'operating',
                    title: '详细信息',
                    align: 'center',
                    valign: 'middle',
                    switchable:false
                },{
                    field: 'flowerror',
                    title: '系统日志',
                    align: 'center',
                    valign: 'middle',
                    switchable:false
                }]
        });

        //点击弹出出单网点选择页面
        //----------------------------------------------------------
        var setting = {
            async : {
                enable : true,
                url : "/cm/business/cartaskmanage/queryparttree",
                autoParam : [ "id" ],//每次重新请求时传回的参数
                dataType : "json",
                type : "post"
            },
            check : {
                enable : true,
                chkStyle : "radio",
                radioType : "all"
            },
            callback : {
//				onClick : zTreeOnCheckDept,//回调函数
                onCheck : zTreeOnCheckDept//回调函数
            }
        };
        //选择后回调函数
        function zTreeOnCheckDept(event, treeId, treeNode) {
            $("#deptcode").val(treeNode.deptinnercode);
            $("#deptname").val(treeNode.name);
            $('#showDeptTree').modal("hide");
        }
        //点击弹出出单网点选择页面
        $("#deptname").on("click", function(e) {
            $('#showDeptTree').modal();
            $.fn.zTree.init($("#deptTreeDemo"), setting);
        });
        $(".closeShowDeptTree").on("click", function(e) {
            $('#showDeptTree').modal("hide");
        });
        //-----------------------------------------------------------
        //点击弹出供应商选择页面
        var comsetting = {
            async : {
                enable : true,
                url : "/cm/provider/queryprotreefirst",
                autoParam : [ "id" ],//每次重新请求时传回的参数
                dataType : "json",
                type : "post"
            },
            check : {
                enable : true,
                chkStyle : "radio",
                radioType : "all"
            },
            callback : {
                onClick : zTreeOnCheckCom,//回调函数
                onCheck : zTreeOnCheckCom//回调函数
            }
        };
        //选择后回调函数
        function zTreeOnCheckCom(event, treeId, treeNode) {
            $("#inscomcode").val(treeNode.prvcode);
            $("#inscomname").val(treeNode.name);
            $('#showpic').modal("hide");
        }
        //点击弹出供应商选择页面
        $("#inscomname").on("click", function(e) {
            $('#showpic').modal();
            $.fn.zTree.init($("#treeDemo"), comsetting);
        });
        $(".closeshowpic").on("click", function(e) {
            $('#showpic').modal("hide");
        });

        
        //--------------------------------------------------

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
        // 车险任务记录一般查询条件赋值
        function getParamStr(){
            var paramStr = "?";
            if(simpleparameter){
                paramStr += simpleparameter;
                if(tasktype){
                    paramStr += ("&"+tasktype);
                }
            }else{
                if(tasktype){
                    paramStr += tasktype;
                }else{
                    paramStr = "";
                }
            }
            return paramStr;
        }
        //简单查询任务状态链接点击事件
        reloadCarTaskInfoByTasktype = function(pagetasktype){
            if($.trim(pagetasktype)){
                tasktype = "tasktype="+$.trim(pagetasktype);
            }else{
                tasktype = "";
            }
            var paramStr = getParamStr();
            $('#table-javascript').bootstrapTable('refresh',{url:pageurl+paramStr});
        };
        // 一般查询按钮（车辆任务列表）
        function executeGeneralQuery(){
            tasktype = "";
            simpleparameter = "";
            if($.trim($("#carlicensenoOrinsuredname").val())){
                simpleparameter = "carlicensenoOrinsuredname=" + $.trim($("#carlicensenoOrinsuredname").val());
            }
            var paramStr = getParamStr();
            $('#table-javascript').bootstrapTable('refresh',{url:pageurl+paramStr});
        }
        $("#generalquerybutton").on("click", function(e) {
            executeGeneralQuery();
        });
        // 查询条件【重置】按钮
        $("#generalresetbutton").on("click", function(e) {
            $("#carlicensenoOrinsuredname").val("");
        });
        //绑定输入框回车查询事件
        $("input#carlicensenoOrinsuredname").keydown(function(event) {
            var e = event || window.event || arguments.callee.caller.arguments[0];
            //13是键盘上面固定的回车键
            if (e && e.keyCode==13) {
                //一般查询
                executeGeneralQuery();
            }
        });
        // 高级查询按钮（车辆任务列表）
        $("#superquerybutton").on("click", function(e) {
            if($.trim($("#taskcreatetimeup").val()) && $.trim($("#taskcreatetimedown").val())){
                var timeupstr = ($.trim($("#taskcreatetimeup").val()).substring(0,11) + " 00:00:00").replace(/-/g,"/");
                var timedownstr = ($.trim($("#taskcreatetimedown").val()).substring(0,11) + " 23:59:59").replace(/-/g,"/");
                var timeup = new Date(timeupstr);
                var timedown = new Date(timedownstr);
                if(timeup >= timedown){
                    alertmsg("任务创建截止时间应晚于开始时间");
                    return;
                }
            }
            //任务类型为"全部"时，必须输入"车牌"、"被保人"、"代理人姓名"、"代理人工号"、"处理人"中的其中一项
            var tasktype=$("#tasktype").val().trim();
            var carlicenseno=$("#carlicenseno").val().trim();
            var insuredname=$("#insuredname").val().trim();
            var agentName=$("#agentName").val().trim();
            var agentNum=$("#agentNum").val().trim();
            var operatorname=$("#operatorname").val().trim();
            var taskid=$("#mainInstanceId").val().trim();
            if(!tasktype){
                if(!(carlicenseno || agentName || agentNum || operatorname || insuredname || taskid)){
                    alert("任务类型为'全部'时，必须输入'车牌'、'被保人'、'代理人姓名'、'代理人工号'、'处理人'、'任务跟踪号'中的其中一项!");
                    return;
                }
            } else {
                var taskcreatetimeupvalue = $.trim($("#taskcreatetimeup").val());
                var taskcreatetimedownvalue = $.trim($("#taskcreatetimedown").val());

                if(!(carlicenseno || agentName || agentNum || operatorname || insuredname || taskid) && (!taskcreatetimeupvalue || !taskcreatetimedownvalue)){
                    alert("请选择任务创建时间");
                    return;
                }

                var timeupdatestr = taskcreatetimeupvalue.substring(0,11);
                var timedowndatestr = taskcreatetimedownvalue.substring(0,11);

                var timeupdate = new Date((timeupdatestr + " 00:00:00").replace(/-/g,"/"));
                var timedowndate = new Date((timedowndatestr + " 00:00:00").replace(/-/g,"/"));
                var iDays = parseInt(Math.abs(timedowndate - timeupdate) / 1000 / 60 / 60 /24);

                if(iDays > 2) {
                    alertmsg("任务创建时间暂时只开放3天的查询区间");
                    return;
                }
            }
            pageurlparameter = "?";
            if($.trim($("#carlicenseno").val())){
                addParameter("carlicenseno",$.trim($("#carlicenseno").val()));
            }
            if($.trim($("#agentName").val())){
                addParameter("agentName",$.trim($("#agentName").val()));
            }
            if($.trim($("#agentNum").val())){
                addParameter("agentNum",$.trim($("#agentNum").val()));
            }
    //		if($.trim($("#usertype").val())){
    //			addParameter("usertype",$.trim($("#usertype").val()));
    //		}
            if($.trim($("#insuredname").val())){
                addParameter("insuredname",$.trim($("#insuredname").val()));
            }
            if($.trim($("#tasktype").val())){
                addParameter("tasktype",$.trim($("#tasktype").val()));
            }
    //		if($.trim($("#taskstatus").val())){
    //			addParameter("taskstatus",$.trim($("#taskstatus").val()));
    //		}
            if($.trim($("#taskcreatetimeup").val())){
                addParameter("taskcreatetimeup",$.trim($("#taskcreatetimeup").val()).substring(0,10).trim() + " 00:00:00");
            }
            if($.trim($("#taskcreatetimedown").val())){
                addParameter("taskcreatetimedown",$.trim($("#taskcreatetimedown").val()).substring(0,10).trim() + " 23:59:59");
            }
            if($.trim($("#mainInstanceId").val())){
                addParameter("ad_instanceId",$.trim($("#mainInstanceId").val()));
            }
            if($.trim($("#operatorname").val())){
                addParameter("operatorname",$.trim($("#operatorname").val()));
            }
            if($.trim($("#deptcode").val())){
                addParameter("deptcode",$.trim($("#deptcode").val()));
            }
            if($.trim($("#inscomcode").val())){
                addParameter("inscomcode",$.trim($("#inscomcode").val()));
            }
            if($.trim($("#taskstate").val())){
                addParameter("taskstate",$.trim($("#taskstate").val()));
            }

            if($.trim($("#channelinnercode").val())){
                addParameter("channelinnercode",$.trim($("#channelinnercode").val()));
            }

            if(pageurlparameter.length <=13){
    //			pageurlparameter = "";
                alert("至少录入一个查询条件!");
                return;
            }
            //车险任务管理,当是已完成的时候,将分配/调度等按钮置为不可用的
            if($.trim($("#taskstate").val())==="02"){
                $("#dispatch").attr("disabled",true);
                $("#stopdispatch").attr("disabled",true);
                $("#restartdispatch").attr("disabled",true);
                $("#personname_select").attr("disabled",true);
                $("#personname").attr("disabled",true);
            }else{
                $("#dispatch").attr("disabled",false);
                $("#stopdispatch").attr("disabled",false);
                $("#restartdispatch").attr("disabled",false);
                $("#personname_select").attr("disabled",false);
                $("#personname").attr("disabled",false);
            }
            $('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
        });

        $("#singlequery").on("click", function(e) {
            if($.trim($("#taskcreatetimeup").val()) && $.trim($("#taskcreatetimedown").val())){
                var timeupstr = ($.trim($("#taskcreatetimeup").val()).substring(0,11) + " 00:00:00").replace(/-/g,"/");
                var timedownstr = ($.trim($("#taskcreatetimedown").val()).substring(0,11) + " 23:59:59").replace(/-/g,"/");
                var timeup = new Date(timeupstr);
                var timedown = new Date(timedownstr);
                if(timeup >= timedown){
                    alertmsg("任务创建截止时间应晚于开始时间");
                    return;
                }
            }
            //任务类型为"全部"时，必须输入"车牌"、"被保人"、"代理人姓名"、"代理人工号"、"处理人"中的其中一项
            var tasktype=$("#tasktype").val().trim();
            var carlicenseno=$("#carlicenseno").val().trim();
            var insuredname=$("#insuredname").val().trim();
            var agentName=$("#agentName").val().trim();
            var agentNum=$("#agentNum").val().trim();
            var operatorname=$("#operatorname").val().trim();
            var taskid=$("#mainInstanceId").val().trim();
            if(!tasktype){
                if(!(carlicenseno || agentName || agentNum || operatorname || insuredname || taskid)){
                    alert("任务类型为'全部'时，必须输入'车牌'、'被保人'、'代理人姓名'、'代理人工号'、'处理人'、'任务跟踪号'中的其中一项!");
                    return;
                }
            } else {
                var taskcreatetimeupvalue = $.trim($("#taskcreatetimeup").val());
                var taskcreatetimedownvalue = $.trim($("#taskcreatetimedown").val());

                if(!(carlicenseno || agentName || agentNum || operatorname || insuredname || taskid) && (!taskcreatetimeupvalue || !taskcreatetimedownvalue)){
                    alert("请选择任务创建时间");
                    return;
                }

                var timeupdatestr = taskcreatetimeupvalue.substring(0,11);
                var timedowndatestr = taskcreatetimedownvalue.substring(0,11);

                var timeupdate = new Date((timeupdatestr + " 00:00:00").replace(/-/g,"/"));
                var timedowndate = new Date((timedowndatestr + " 00:00:00").replace(/-/g,"/"));
                var iDays = parseInt(Math.abs(timedowndate - timeupdate) / 1000 / 60 / 60 /24);

                if(iDays > 2) {
                    alertmsg("任务创建时间暂时只开放3天的查询区间");
                    return;
                }
            }
            pageurlparameter = "?";
            if($.trim($("#carlicenseno").val())){
                addParameter("carlicenseno",$.trim($("#carlicenseno").val()));
            }
            if($.trim($("#agentName").val())){
                addParameter("agentName",$.trim($("#agentName").val()));
            }
            if($.trim($("#agentNum").val())){
                addParameter("agentNum",$.trim($("#agentNum").val()));
            }
            //		if($.trim($("#usertype").val())){
            //			addParameter("usertype",$.trim($("#usertype").val()));
            //		}
            if($.trim($("#insuredname").val())){
                addParameter("insuredname",$.trim($("#insuredname").val()));
            }
            if($.trim($("#tasktype").val())){
                addParameter("tasktype",$.trim($("#tasktype").val()));
            }
            //		if($.trim($("#taskstatus").val())){
            //			addParameter("taskstatus",$.trim($("#taskstatus").val()));
            //		}
            if($.trim($("#taskcreatetimeup").val())){
                addParameter("taskcreatetimeup",$.trim($("#taskcreatetimeup").val()).substring(0,10).trim() + " 00:00:00");
            }
            if($.trim($("#taskcreatetimedown").val())){
                addParameter("taskcreatetimedown",$.trim($("#taskcreatetimedown").val()).substring(0,10).trim() + " 23:59:59");
            }
            if($.trim($("#mainInstanceId").val())){
                addParameter("ad_instanceId",$.trim($("#mainInstanceId").val()));
            }
            if($.trim($("#operatorname").val())){
                addParameter("operatorname",$.trim($("#operatorname").val()));
            }
            if($.trim($("#deptcode").val())){
                addParameter("deptcode",$.trim($("#deptcode").val()));
            }
            if($.trim($("#inscomcode").val())){
                addParameter("inscomcode",$.trim($("#inscomcode").val()));
            }
            if($.trim($("#taskstate").val())){
                addParameter("taskstate",$.trim($("#taskstate").val()));
            }

            if($.trim($("#channelinnercode").val())){
                addParameter("channelinnercode",$.trim($("#channelinnercode").val()));
            }

            if(pageurlparameter.length <=13){
                //			pageurlparameter = "";
                alert("至少录入一个查询条件!");
                return;
            }
            //车险任务管理,当是已完成的时候,将分配/调度等按钮置为不可用的
            if($.trim($("#taskstate").val())==="02"){
                $("#dispatch").attr("disabled",true);
                $("#stopdispatch").attr("disabled",true);
                $("#restartdispatch").attr("disabled",true);
                $("#personname_select").attr("disabled",true);
                $("#personname").attr("disabled",true);
            }else{
                $("#dispatch").attr("disabled",false);
                $("#stopdispatch").attr("disabled",false);
                $("#restartdispatch").attr("disabled",false);
                $("#personname_select").attr("disabled",false);
                $("#personname").attr("disabled",false);
            }
            $('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter+'&singleFlag=1'});
        });

        // 车险任务分配功能接口调用（车辆任务列表更新）
        $("#dispatch").on("click", function(e) {
        	//组织传递数据
            var carTaskData = $('#table-javascript').bootstrapTable('getSelections');
            if(carTaskData.length == 0){
                alertmsg("没有任务被选中！");
                return;
            }else if(carTaskData.length > 1){
                alertmsg("每次只能处理一条任务！");
                return;
            }else if(carTaskData[0].dispatchFlag == "0"){
                alertmsg(carTaskData[0].tasktype+"任务不支持人工调度分配！");
                return;
            }else if(carTaskData[0].taskcode == "20"){
                alertmsg(carTaskData[0].tasktype+"任务不支持人工调度分配！");
                return;
            }else if(carTaskData[0].operatorstateFlag == "pauseTask"){
                alertmsg("此任务已暂停调度！");
                return;
            }
            var userid = $.trim($("#personname").val());
            if(!userid){
                alertmsg("请指定分配人员信息！");
                return;
            }
            var dispatchParams = {//调度分配参数
                "userCode":userid,
                "operator":carTaskData[0].operatorcode==null?"":carTaskData[0].operatorcode,
                "maininstanceId":carTaskData[0].maininstanceId,
                "inscomcode":carTaskData[0].inscomcode,
                "subInstanceId":carTaskData[0].subInstanceId
            };
        	$.ajax({
                url : '/cm/business/cartaskmanage/dispatchCheck',
                type : 'GET',
                dataType : 'json',
                contentType: "application/json",
                data :{
                    "userCode":userid,
                    "operator":"check",
                    "maininstanceId":carTaskData[0].maininstanceId,
                    "inscomcode":carTaskData[0].inscomcode,
                    "subInstanceId":carTaskData[0].subInstanceId
                },
                cache : false,
                async : true,
                error : function() {
                    alertmsg("Connection error");
                },
                success : function(data) {
                    if (data.status == "presuccess") {
                    	confirmmsg(data.msg,function(){
                    		//执行分配
                        	$.ajax({
                                url : '/cm/business/cartaskmanage/dispatch',
                                type : 'GET',
                                dataType : 'json',
                                contentType: "application/json",
                                data :dispatchParams,
                                cache : false,
                                async : true,
                                error : function() {
                                    alertmsg("Connection error");
                                },
                                success : function(data) {
                                    if (data.status == "success") {
                                        messageNotReadCount();
                                        alertmsg(data.msg);
                                        var page = $("ul.pagination li.active").find("a").eq(0).text().trim();
                                        //alert('&limit='+page*10+'&offset='+(page-1)*10);
                                        if($("#superquerypanel:hidden").length>0){
                                            var paramStr = getParamStr();
                                            $('#table-javascript').bootstrapTable('refresh',{query:pageurl+paramStr+'&limit='+page*10+'&offset='+(page-1)*10});
                                        }else{
                                            $('#table-javascript').bootstrapTable('refresh',{query:pageurlparameter+'&limit='+page*10+'&offset='+(page-1)*10});
                                        }

                                    }else if (data.status == "fail"){

                                        alertmsg(data.msg);
                                    }
                                }
                            });
                    	})
                    	//if (window.confirm() == true){
                        //}
                    }else if (data.status == "closed"){
                    	alertmsg(data.msg);
                    }else{//执行分配
                    	$.ajax({
                            url : '/cm/business/cartaskmanage/dispatch',
                            type : 'GET',
                            dataType : 'json',
                            contentType: "application/json",
                            data :dispatchParams,
                            cache : false,
                            async : true,
                            error : function() {
                                alertmsg("Connection error");
                            },
                            success : function(data) {
                                if (data.status == "success") {
                                    messageNotReadCount();
                                    alertmsg(data.msg);
                                    var page = $("ul.pagination li.active").find("a").eq(0).text().trim();
                                    //alert('&limit='+page*10+'&offset='+(page-1)*10);
                                    if($("#superquerypanel:hidden").length>0){
                                        var paramStr = getParamStr();
                                        $('#table-javascript').bootstrapTable('refresh',{query:pageurl+paramStr+'&limit='+page*10+'&offset='+(page-1)*10});
                                    }else{
                                        $('#table-javascript').bootstrapTable('refresh',{query:pageurlparameter+'&limit='+page*10+'&offset='+(page-1)*10});
                                    }

                                }else if (data.status == "fail"){

                                    alertmsg(data.msg);
                                }
                            }
                        });
                    }
                }
            });
    //		alertmsg(JSON.stringify(dispatchParams));
        });

        function messageNotReadCount(){
            $.ajax({
                type: "get",
                url: "/cm/message/getNotReadCount",
                data: {receiver:$("#personname").val()},
                dataType:"text",
                success: function(data){
                    $("#messageNotReadCount",window.parent.document).text(data);
                }
            });
        }

        // 车险任务停止调度按钮事件
        $("#stopdispatch").on("click", function(e) {
            //组织传递数据
            var carTaskData = $('#table-javascript').bootstrapTable('getSelections');
            if(carTaskData.length == 0){
                alertmsg("没有任务被选中！");
                return;
            }else if(carTaskData.length > 1){
                alertmsg("每次只能处理一条任务！");
                return;
            }else if(carTaskData[0].dispatchFlag == "0"){
                alertmsg(carTaskData[0].tasktype+"任务不支持人工停止调度！");
                return;
            }else if(carTaskData[0].taskcode == "20"){
                alertmsg(carTaskData[0].tasktype+"任务不支持人工停止调度！");
                return;
            }else if(carTaskData[0].operatorstateFlag == "pauseTask"){
                alertmsg("此任务已暂停调度！");
                return;
            }else if(carTaskData[0].operatorcode){
                alertmsg("此任务已分配不能暂停调度！");
                return;
            }
            var stopdispatchParams = {//停止调度参数
                "maininstanceId":carTaskData[0].maininstanceId,
                "inscomcode":carTaskData[0].inscomcode,
                "subInstanceId":carTaskData[0].subInstanceId
            };
            $.ajax({
                url : '/cm/business/cartaskmanage/stopdispatch',
                type : 'GET',
                dataType : 'json',
                contentType: "application/json",
                data :stopdispatchParams,
                cache : false,
                async : true,
                error : function() {
                    alertmsg("Connection error");
                },
                success : function(data) {
                    if (data.status == "success") {
                        alertmsg(data.msg);
                        if($("#superquerypanel:hidden").length>0){
                            var paramStr = getParamStr();
                            $('#table-javascript').bootstrapTable('refresh',{url:pageurl+paramStr});
                        }else{
                            $('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
                        }
                    }else if (data.status == "fail"){
                        alertmsg(data.msg);
                    }
                }
            });
        });

        // 车险任务重启调度按钮事件
        $("#restartdispatch").on("click", function(e) {
            //组织传递数据
            var carTaskData = $('#table-javascript').bootstrapTable('getSelections');
            if(carTaskData.length == 0){
                alertmsg("没有任务被选中！");
                return;
            }else if(carTaskData.length > 1){
                alertmsg("每次只能处理一条任务！");
                return;
            }else if(carTaskData[0].dispatchFlag == "0"){
                alertmsg(carTaskData[0].tasktype+"任务不支持人工重启调度！");
                return;
            }else if(carTaskData[0].taskcode == "20"){
                alertmsg(carTaskData[0].tasktype+"任务不支持人工重启调度！");
                return;
            }else if(carTaskData[0].operatorstateFlag != "pauseTask"){
                alertmsg("此任务未暂停调度！");
                return;
            }
            var restartdispatchParams = {//重启调度参数
                "userCode":carTaskData[0].operatorcode,
                "maininstanceId":carTaskData[0].maininstanceId,
                "inscomcode":carTaskData[0].inscomcode,
                "subInstanceId":carTaskData[0].subInstanceId
            };
            $.ajax({
                url : '/cm/business/cartaskmanage/restartdispatch',
                type : 'GET',
                dataType : 'json',
                contentType: "application/json",
                data :restartdispatchParams,
                cache : false,
                async : true,
                error : function() {
                    alertmsg("Connection error");
                },
                success : function(data) {
                    if (data.status == "success") {
                        alertmsg(data.msg);
                        if($("#superquerypanel:hidden").length>0){
                            var paramStr = getParamStr();
                            $('#table-javascript').bootstrapTable('refresh',{url:pageurl+paramStr});
                        }else{
                            $('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
                        }
                    }else if (data.status == "fail"){
                        alertmsg(data.msg);
                    }
                }
            });
        });
        // 查看流程轨迹按钮事件
        $("#showWorkflowTrack").on("click", function(e) {
            //组织传递数据
            var carTaskData = $('#table-javascript').bootstrapTable('getSelections');
            if(carTaskData.length == 0){
                alertmsg("没有任务被选中！");
                return;
            }else if(carTaskData.length > 1){
                alertmsg("只能选择一条任务！");
                return;
            }
            //跳出工作流轨迹弹出框
            window.parent.openDialogForCM("business/cartaskmanage/showWorkflowTrack?instanceId="+carTaskData[0].maininstanceId
                +"&inscomcode="+carTaskData[0].inscomcode);
        });
    });
});