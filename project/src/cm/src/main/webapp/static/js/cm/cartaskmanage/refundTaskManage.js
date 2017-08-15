require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn",  "bootstrapdatetimepicker","bootstrapdatetimepickeri18n","jqcookie", "jqtreeview","zTree","zTreecheck","public"], function ($) {
    $(function() {
        //默认一页显示十条记录
        var pagesize = 10;
        //当前调用的url
        var pageurl = "showrefundtaskmanagelist";
        //当前调用的url查询参数
        var pageurlparameter = "";
        //简单查询车牌被保人参数
        var simpleparameter = "";
        //简单查询任务状态参数
        var tasktype = "";

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
            //startDate:'-3m',
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
            $("#cartasksuperqueryform")[0].reset();
            $("#deptcode").val("");
            $("#channelinnercode").val("");
            
        });

        //initQueryTaskDate();
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
            //clickToSelect: true,
            //singleSelect: true,
            showToggle:true,  //test
            cardView: false,  //test
            showColumns: true,//test
            columns: [
                {
                field: 'taskid',
                title: '任务号',
                align: 'center',
                valign: 'middle',
                sortable: false
            }, {
                field: 'paymentransaction',
                title: '支付流水号',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'carlicenseno',
                title: '车牌号码',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'paytime',
                title: '支付时间',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'paychannelname',
                title: '支付方式',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'payamount',
                title: '支付金额',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'refundamount',
                title: '退款金额',
                align: 'center',
                valign: 'middle',
                sortable: true,
                    formatter: function(value,row,index){
                        return -value;
                    }

            }, {
                field: 'refundtype',
                title: '退款类型',
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: function(value,row,index){
                    if(value=='01'){
                        return "全额退款" ;
                    } else if (value=='02') {
                        return "差额退款" ;
                    }
                }
            }, {
                field: 'comname',
                title: '出单网点',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'channelname',
                title: '渠道来源',
                align: 'center',
                valign: 'middle',
                sortable: true
            },{
                field: 'refundstatus',
                title: '任务状态',
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: function(value,row,index){
                    if(value=='1'){
                        return "已完成" ;
                    }else{
                        return "未完成" ;
                    }
                }
            },
                {
                    field: 'operation',
                    title: '操作',
                    align: 'center',
                    valign: 'middle',
                    switchable:false,
                    formatter:function(value,row,index){
                        if (row.refundstatus == '0') {
                            var url = "/cm/business/ordermanage/underpaidding?taskid="+row.taskid+"&taskcode=refund&inscomcode="+row.inscomcode
                            var e = '<a href='+url+'>处理</a> ';
                            return e;
                        } else {
                            var url = "/cm/business/ordermanage/underpaidding?taskid="+row.taskid+"&taskcode=refunded&inscomcode="+row.inscomcode
                            var e = '<a href='+url+'>查看</a> ';

                            return e;

                            //if($(window.top.document).find("#menu").css("display")=="none"){
                                //$.cmTaskList('my', 'list4deal', false);
                                //$.insLoading();
                                //$(window.top.document).find("#desktop_content").attr("src", url);
                           // }else{
                            //    location.href=url;
                           // }


                        }

                    }
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
        // $("#taskcreatetimeup").change(function(){
        //     var startString=$("#taskcreatetimeup").val();//获取当前显示的时间字符串
        //     var dateStartime=Date.parse(startString);//解析时间字符串
        //     var startime=new Date(dateStartime);//创建时间
        //     var endYear = startime.getFullYear();
        //     var endMonth=(startime.getMonth()+2)+"";//获取时间的月份,在月份的基础上加1个月.因为月份是从0开始,所以+2
        //     if (endMonth > 12) {//计算下个月的月份超过12则为下年1月
        //         endYear += 1;
        //         endMonth = endMonth - 12 + "";
        //     }
        //     var days = new Array(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);//平年每月天数
        //     var enday=startime.getDate()+"";//获取日份
        //     if (days[endMonth - 1] < enday) {//如果起始时间有31号下个月不一定有31号
        //         if ((endYear % 4 == 0 && endYear % 400 != 0 || endYear % 400 == 0) && endMonth == 2)//闰年2月
        //             enday = days[endMonth - 1] + 1;
        //         else
        //             enday = days[endMonth - 1];
        //     }
        //     if(endMonth.length<2){
        //         endMonth="0"+endMonth;
        //     }
        //     if(enday.length<2){
        //         enday="0"+enday;
        //     }
        //     var endString = endYear+"-"+endMonth+"-"+enday;//跨度一个月的截止时间字符串
        //     var dateEndTime=Date.parse(endString);
        //     var endTime=new Date(dateEndTime);//解析为时间类型
        //     if(endTime>new Date()){//若开始时间为今天,那么截止时间可以为下个月的今天,所以需要判读
        //         endTime=new Date();//如果最大可选时间大于当前的时间,则最大可选时间为当前时间
        //         var todayMonth=(endTime.getMonth()+1)+"";
        //         if(todayMonth.length<2){
        //             todayMonth="0"+todayMonth;
        //         }
        //         var todayDay=(endTime.getDate())+"";
        //         if(todayDay.length<2){
        //             todayDay="0"+todayDay;
        //         }
        //         endString=endTime.getFullYear()+"-"+todayMonth+"-"+todayDay;
        //     }
        //     $("#taskcreatetimedown").datetimepicker('setEndDate',endTime);
        //     var showEnd=$("#taskcreatetimedown").val();//获取当前的截止日期
        //     var dateShowEnd=Date.parse(showEnd);
        //     var currentEnd=new Date(dateShowEnd);
        //     var days=0;
        //     if(currentEnd>startime){
        //         days=(currentEnd-startime)/(1000*60*60*24);
        //     }else{
        //         days=(startime-currentEnd)/(1000*60*60*24);
        //     }
        //     if(days>30){//若页面显示的时间差超过一个月,则将显示的截止时间改为设置的截止时间
        //         $("#taskcreatetimedown").val(endString);
        //     }
        // });

        //修改截止时间
        // $("#taskcreatetimedown").change(function(){
        //     var endString=$("#taskcreatetimedown").val();
        //     var dateEnd=Date.parse(endString);
        //     var endTime=new Date(dateEnd);
        //     var startMonth=endTime.getMonth();
        //     if(startMonth==0){//当是一月的时候得到就是0,显示的时候就是0月.所以要+1
        //         startMonth=1;
        //     }
        //     startMonth=startMonth+"";
        //     if(startMonth.length<2){//改变月份显示为两位显示
        //         startMonth="0"+startMonth;
        //     }
        //     var startDay=endTime.getDate()+"";
        //     if(startDay.length<2){
        //         startDay="0"+startDay;
        //     }
        //     var startString=endTime.getFullYear()+"-"+startMonth+"-"+startDay;
        //     var startime=new Date(Date.parse(startString));
        //     var today=new Date();
        //     var beforeMonth=today.getMonth()-2;
        //     var beforeYear=today.getFullYear();
        //     if(beforeMonth<0){
        //         beforeMonth=13+beforeMonth;
        //         beforeYear=beforeYear-1;//往前推一年
        //     }
        //     beforeMonth=beforeMonth+"";
        //     if(beforeMonth.length<2){
        //         beforeMonth="0"+beforeMonth;
        //     }
        //     var beforeDay=today.getDate()+"";
        //     if(beforeDay.length<2){
        //         beforeDay="0"+beforeDay;
        //     }
        //     var beforeString=beforeYear+"-"+beforeMonth+"-"+beforeDay;
        //     var beforeDate=new Date(Date.parse(beforeString));//从今天往前三个月的时间
        //     if(startime<beforeDate){
        //         startime=beforeDate;
        //         startString=beforeString;
        //     }
        //     //$("#taskcreatetimeup").datetimepicker('setStartDate',startime);
        //     var showStart=$("#taskcreatetimeup").val();//获取当前的截止日期
        //     var dateShowStart=Date.parse(showStart);
        //     var currentStart=new Date(dateShowStart);
        //     var days=0;
        //     if(currentStart>endTime){
        //         days=(currentStart-endTime)/(1000*60*60*24);
        //     }else{
        //         days=(endTime-currentStart)/(1000*60*60*24);
        //     }
        //     if(days>30){//若页面显示的时间差超过一个月,则将显示的截止时间改为设置的截止时间
        //         $("#taskcreatetimeup").val(startString);
        //     }
        // });
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
        // reloadCarTaskInfoByTasktype = function(pagetasktype){
        //     if($.trim(pagetasktype)){
        //         tasktype = "tasktype="+$.trim(pagetasktype);
        //     }else{
        //         tasktype = "";
        //     }
        //     var paramStr = getParamStr();
        //     $('#table-javascript').bootstrapTable('refresh',{url:pageurl+paramStr});
        // };
        // 一般查询按钮（车辆任务列表）
        // function executeGeneralQuery(){
        //     tasktype = "";
        //     simpleparameter = "";
        //     if($.trim($("#carlicensenoOrinsuredname").val())){
        //         simpleparameter = "carlicensenoOrinsuredname=" + $.trim($("#carlicensenoOrinsuredname").val());
        //     }
        //     var paramStr = getParamStr();
        //     $('#table-javascript').bootstrapTable('refresh',{url:pageurl+paramStr});
        // }


        $("#singlequery").on("click", function(e) {
            if($.trim($("#taskcreatetimeup").val()) && $.trim($("#taskcreatetimedown").val())){
                var timeupstr = ($.trim($("#taskcreatetimeup").val()).substring(0,11) + " 00:00:00").replace(/-/g,"/");
                var timedownstr = ($.trim($("#taskcreatetimedown").val()).substring(0,11) + " 23:59:59").replace(/-/g,"/");
                var timeup = new Date(timeupstr);
                var timedown = new Date(timedownstr);
                if(timeup >= timedown){
                    alertmsg("支付截止时间应晚于开始时间");
                    return;
                }
            }
            // //任务类型为"全部"时，必须输入"车牌"、"被保人"、"代理人姓名"、"代理人工号"、"处理人"中的其中一项
            // var tasktype=$("#tasktype").val().trim();
            // var carlicenseno=$("#carlicenseno").val().trim();
            // var insuredname=$("#insuredname").val().trim();
            // var agentName=$("#agentName").val().trim();
            // var agentNum=$("#agentNum").val().trim();
            // var operatorname=$("#operatorname").val().trim();
            // var taskid=$("#mainInstanceId").val().trim();
            // if(!tasktype){
            //     if(!(carlicenseno || agentName || agentNum || operatorname || insuredname || taskid)){
            //         alert("任务类型为'全部'时，必须输入'车牌'、'被保人'、'代理人姓名'、'代理人工号'、'处理人'、'任务跟踪号'中的其中一项!");
            //         return;
            //     }
            // } else {
            //     var taskcreatetimeupvalue = $.trim($("#taskcreatetimeup").val());
            //     var taskcreatetimedownvalue = $.trim($("#taskcreatetimedown").val());
            //
            //     if(!(carlicenseno || agentName || agentNum || operatorname || insuredname || taskid) && (!taskcreatetimeupvalue || !taskcreatetimedownvalue)){
            //         alert("请选择支付时间");
            //         return;
            //     }
            //
            //     var timeupdatestr = taskcreatetimeupvalue.substring(0,11);
            //     var timedowndatestr = taskcreatetimedownvalue.substring(0,11);
            //
            //     var timeupdate = new Date((timeupdatestr + " 00:00:00").replace(/-/g,"/"));
            //     var timedowndate = new Date((timedowndatestr + " 00:00:00").replace(/-/g,"/"));
            //     var iDays = parseInt(Math.abs(timedowndate - timeupdate) / 1000 / 60 / 60 /24);
            //
            //     if(iDays > 2) {
            //         alertmsg("支付时间暂时只开放3天的查询区间");
            //         return;
            //     }
            // }
            pageurlparameter = "?";
            if($.trim($("#carlicenseno").val())){
                addParameter("carlicenseno",$.trim($("#carlicenseno").val()));
            }
            if($.trim($("#taskid").val())){
                addParameter("taskid",$.trim($("#taskid").val()));
            }
            if($.trim($("#paymentransaction").val())){
                addParameter("paymentransaction",$.trim($("#paymentransaction").val()));
            }
            if($.trim($("#deptcode").val())){
                addParameter("deptcode",$.trim($("#deptcode").val()));
            }
            if($.trim($("#channelinnercode").val())){
                addParameter("channelinnercode",$.trim($("#channelinnercode").val()));
            }
            if($.trim($("#refundstatus").val())){
                addParameter("refundstatus",$.trim($("#refundstatus").val()));
            }
            if($.trim($("#taskcreatetimeup").val())){
                addParameter("taskcreatetimeup",$.trim($("#taskcreatetimeup").val()).substring(0,10).trim() + " 00:00:00");
            }
            if($.trim($("#taskcreatetimedown").val())){
                addParameter("taskcreatetimedown",$.trim($("#taskcreatetimedown").val()).substring(0,10).trim() + " 23:59:59");
            }

            if($.trim($("#payamount").val())){
                addParameter("payamount",$.trim($("#payamount").val()));
            }
            if($.trim($("#refundamount").val())){
                addParameter("refundamount",$.trim($("#refundamount").val()));
            }
            if($.trim($("#refundtype").val())){
                addParameter("refundtype",$.trim($("#refundtype").val()));
            }

            if(pageurlparameter.length <=13){
                alert("至少录入一个查询条件!");
                return;
            }

            $('#table-javascript').bootstrapTable('refresh',{url:pageurl+pageurlparameter});
        });

        var chnsetting = {
            async : {
                enable : true,
                url : "/cm/channel/queryTreeTopList",
                autoParam: ["id=parentCode"],
                dataType : "json",
                type : "post"
            },
            check : {
                enable : true,
                chkStyle : "radio",
                radioType : "all"
            },
            callback : {
                onClick : zTreeOnCheckChn,//回调函数
                onCheck : zTreeOnCheckChn//回调函数
            }
        };

        $(".closeshowchannel").on("click", function(e) {
            $('#showchannel').modal("hide");
        });

        //选择后回调函数
        function zTreeOnCheckChn(event, treeId, treeNode) {
            $("#channelinnercode").val(treeNode.innercode);
            $("#channelName").val(treeNode.name);
            $('#showchannel').modal("hide");
        }
        //点击弹出供应商选择页面
        $("#channelName").on("click", function(e) {
            $('#showchannel').modal();
            $.fn.zTree.init($("#channelTree"), chnsetting);
        });

    });
});