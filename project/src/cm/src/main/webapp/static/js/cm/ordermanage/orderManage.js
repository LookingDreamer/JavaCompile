require(["jquery","bootstrap-table", "bootstrap","bootstrapdatetimepicker","bootstrapTableZhCn","zTree","zTreecheck","flat-ui","public","bootstrapdatetimepickeri18n"], function($) {
	$(function($){
		//默认一页显示十条记录
		var limit = 5;
		//当前页码
		var currentpage = 1;
		//高级查询参数
		var pageurlparameter = "";
		//简单查询车牌被保人参数
		var simpleparameter = "";
		//简单查询任务状态参数
		var tasktype = "";

        //配送方式
        var deliveryType = "";
		//是否简单查询
		var isSimpleQuery = true;
		
		//点击弹出出单网点选择页面
		//----------------------------------------------------------
		var deptsetting = {
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
			$("#deptcode").val(treeNode.comcode);
			$("#deptname").val(treeNode.name);
			$('#showDeptTree').modal("hide");
		}
		//点击弹出出单网点选择页面
		$("#deptname").on("click", function(e) {
			$('#showDeptTree').modal();
			$.fn.zTree.init($("#deptTreeDemo"), deptsetting);
		});
		$(".closeShowDeptTree").on("click", function(e) {
			$('#showDeptTree').modal("hide");
		});
		//-----------------------------------------------------------

		//隐藏或显示高级查询
		$("#showsuperquerybutton").click(function(){
			$("#superquerypanel").toggle();
			$("#querypanel").toggle();
		});
		$("#superquerypanelclose").click(function(){
			$("#superquerypanel").toggle();
			$("#querypanel").toggle();
		});
		//刷新列表按钮
		$(".generalrefreshBtn").click(function(){
			refreshTaskManageList();
		});
		//点击弹出供应商选择页面
		var setting = {
			async : {
				enable : true,
				url : "/cm/provider/queryprotree",
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
				onClick : zTreeOnCheck,//回调函数
				onCheck : zTreeOnCheck//回调函数
			}
		};
		//选择后回调函数
		function zTreeOnCheck(event, treeId, treeNode) {
			$("#inscomcode").val(treeNode.prvcode);
			$("#inscomname").val(treeNode.name);
			$('#showpic').modal("hide");
		}
		//点击弹出供应商选择页面
		$("#inscomname").on("click", function(e) {
			$('#showpic').modal();
			$.fn.zTree.init($("#treeDemo"), setting);
		});
		$(".closeshowpic").on("click", function(e) {
			$('#showpic').modal("hide");
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
				// showMeridian: 1
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
			$("#taskcreatetimeupTxt").val(dateString);
			$("#taskcreatetimeup").val(dateString+" 00:00:00");
			$("#taskcreatetimedownTxt").val(dateString);
			$("#taskcreatetimedown").val(dateString+" 23:59:59");
		}
		initQueryTaskDate();
		// 重置按钮
		$("#resetbutton").on("click", function(e) {
			$("#tasktype").val("");
			//触发写好的change事件
			$("#tasktype").trigger("change");
			$("#insuredname").val("");
			$("#agentName").val("");
//			$("#usertype").val("");
			$("#carlicenseno").val("");
			$("#agentNum").val("");
			$("#agentphone").val("");
			$("#maininstanceid").val("");
			$("#taskcreatetimeupTxt").val("");
			$("#taskcreatetimeup").val("");
			$("#taskcreatetimedown").val("");
			$("#taskcreatetimedownTxt").val("");
			$("#inscomcode").val("");
			$("#inscomname").val("");
			$("#deptcode").val("");
			$("#deptname").val("");
			$("#channelinnercode").val("");
			$("#channelName").val("");
//			initQueryTaskDate();
		});
		$("#generalresetbutton").on("click", function(e) {
			$("#carlicensenoOrinsuredname").val("");
		});
		//刷新任务池任务列表方法
		function refreshTaskManageList(){
			$("#myOrderManageResultList").text("正在努力的加载数据中！请稍后...");
			$("#myOrderManageResultFoot").empty();
			var queryData = "";
			if(isSimpleQuery){
				queryData = "limit=" + limit + "&currentpage=" + currentpage + simpleparameter + tasktype + deliveryType;
			}else{
				queryData = "limit=" + limit + "&currentpage=" + currentpage + pageurlparameter;
			}
			$.ajax({
				url : '/cm/business/ordermanage/QueryOrdermanagelist',
				type : 'GET',
				dataType : 'html',
				data : queryData,
				cache : false,
				async : true,
				error : function() {
					alertmsg("Connection error");
					$("#myOrderManageResultList").text("加载数据失败！请重试！");
				},
				success : function(data) {
					if(data){
						$("#myOrderManage").empty();
						$("#myOrderManage").html(data);
						if(!$.trim($("#myOrderManageResultList").text())){
							$("#myOrderManageResultList").text("没有查询到数据！");
						}
						initMyJs();
					}else{
						$("#myOrderManageResultList").text("加载数据失败！请重试！");
						alertmsg("查询失败，请稍后重试！");
					}
				}
			});
		}
		//简单查询任务状态链接点击事件
		reloadCarTaskInfoByTasktype = function (pagetasktype){
			if($.trim(pagetasktype)){
				tasktype = "&taskstatus="+$.trim(pagetasktype);
			}else{
				tasktype = "";
			}
			currentpage = 1;
			isSimpleQuery = true;
            $("#deliveryTypeWrapper").css("display", "03" === pagetasktype ? "" : "none");
            if ("03" != pagetasktype) {
                deliveryType = "";
            }
			refreshTaskManageList();
		}
		// 一般查询按钮（车辆任务列表）
		function executeGeneralQuery(){
			simpleparameter = "";
			if($.trim($("#carlicensenoOrinsuredname").val())){
				simpleparameter = "&carlicensenoOrinsuredname=" + $.trim($("#carlicensenoOrinsuredname").val());
			}
            deliveryType = ("&deliveryType=" + $.trim($('#deliveryType').val()));
			currentpage = 1;
			isSimpleQuery = true;
			refreshTaskManageList();
		}
		$("#generalquerybutton").on("click", function(e) {
			executeGeneralQuery();
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
		// 车险任务记录高级查询条件赋值
		function addParameter(parameterName,parameterValue){
			pageurlparameter += "&" + parameterName + "=" + parameterValue;
		}
		// 高级查询按钮（车辆任务列表）
		$("#superquerybutton").on("click", function(e) {
			if($.trim($("#taskcreatetimeup").val()) && $.trim($("#taskcreatetimedown").val())){
				var timeupstr = ($.trim($("#taskcreatetimeup").val()).substring(0,11) + " 00:00:00").replace(/-/g,"/");
				var timedownstr = ($.trim($("#taskcreatetimedown").val()).substring(0,11) + " 23:59:59").replace(/-/g,"/");
				var timeup = new Date(timeupstr);
				var timedown = new Date(timedownstr);
				if(timeup >= timedown){
					alertmsg("任务创建截止时间应晚于开始时间！");
					return;
				}
			}
			//任务类型为"全部"时，必须输入"车牌"、"被保人"、"代理人姓名"、"代理人工号"、"处理人"中的其中一项
			var tasktype=$("#tasktype").val().trim();
			var carlicenseno=$("#carlicenseno").val().trim();
			var insuredname=$("#insuredname").val().trim();
			var agentName=$("#agentName").val().trim();
			var agentNum=$("#agentNum").val().trim();
			var taskid=$("#maininstanceid").val().trim();
			if(!tasktype){
				if(!(carlicenseno || agentName || agentNum || insuredname || taskid)){
					alert("任务类型为'全部'时，必须输入'车牌'、'被保人'、'代理人姓名'、'代理人工号'、'任务跟踪号'中的其中一项!");
					return;
				}
			} else {
				var taskcreatetimeupvalue = $.trim($("#taskcreatetimeup").val());
				var taskcreatetimedownvalue = $.trim($("#taskcreatetimedown").val());

				if(!(carlicenseno || agentName || agentNum || insuredname || taskid) && (!taskcreatetimeupvalue || !taskcreatetimedownvalue)){
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
			pageurlparameter = "";
			if($.trim($("#tasktype").val())){
				addParameter("taskstatus",$.trim($("#tasktype").val()));
			}
			if($.trim($("#deliveryType").val())){
				addParameter("deliveryType",$.trim($("#deliveryType").val()));
			}
			if($.trim($("#insuredname").val())){
				addParameter("insuredname",$.trim($("#insuredname").val()));
			}
			if($.trim($("#agentName").val())){
				addParameter("agentName",$.trim($("#agentName").val()));
			}
//			if($.trim($("#usertype").val())){
//				addParameter("usertype",$.trim($("#usertype").val()));
//			}
			if($.trim($("#carlicenseno").val())){
				addParameter("carlicenseno",$.trim($("#carlicenseno").val()));
			}
			if($.trim($("#agentNum").val())){
				addParameter("agentNum",$.trim($("#agentNum").val()));
			}
			if($.trim($("#inscomcode").val())){
				addParameter("inscomcode",$.trim($("#inscomcode").val()));
			}
			if($.trim($("#agentphone").val())){
				addParameter("agentphone",$.trim($("#agentphone").val()));
			}
			if($.trim($("#maininstanceid").val())){
				addParameter("maininstanceid",$.trim($("#maininstanceid").val()));
			}
			if($.trim($("#deptcode").val())){
	            addParameter("deptcode",$.trim($("#deptcode").val()));
	        }
			if($.trim($("#taskcreatetimeup").val())){
				addParameter("taskcreatetimeup",$.trim($("#taskcreatetimeup").val()).substring(0,10).trim() + " 00:00:00");
			}
			if($.trim($("#taskcreatetimedown").val())){
				addParameter("taskcreatetimedown",$.trim($("#taskcreatetimedown").val()).substring(0,10).trim() + " 23:59:59");
			}
			if($.trim($("#channelinnercode").val())){
				addParameter("channelinnercode",$.trim($("#channelinnercode").val()));
			}
			if(($("input[name='paymentMethod']:checked").val())==4){
				addParameter("paymentMethod",$("input[name='paymentMethod']:checked").val());
			};
			if($.trim($("#deliType").val())){
				addParameter("deliveryType",$.trim($("#deliType").val()));
			}
			currentpage = 1;
			isSimpleQuery = false;
			refreshTaskManageList();
			
			
		});
		
		//隐藏非柜台支付时相关的方法
		function hidePaymentMethod(){
			if(($("input[name='paymentMethod']:checked").val())!=4){
				$("[name='need2dotask']").hide();
			}
		};
		
		//隐藏非自取时相关的方法
		function hideDelivery(){
			if(($("#deliType").val())!='0'){
				$("[name='need2delivery']").hide();
			}
		};
		
		//切换任务类型时隐藏相关的方法
		$("#tasktype").change(function(){
			$("#paymentMethod").prop('checked',false);
			$("[name='payment']").hide();
			$(".need2delivery").hide();
			$("#deliType").val("");
			if($("#tasktype").val()=="01"){
				$("[name='payment']").show();
				return;
			}
			if($("#tasktype").val()=="03"){
				$(".need2delivery").show();
				return;
			}
		});
		
		//没有查询到任务时隐藏功能按钮
		function hideButton(){
			if($(".chx").val()!="on"){
				$("#chxButton").hide();
			}
		}
		
		//只用任务号查询一个单刚好是柜台pos支付的话, 显示
		function showBatchPaySuccess(){
			//如果带任务号查询只能找到一个任务
			if($("#tasktype").val()=="01" & $("#maininstanceid").val()!=""){
				//如果查到任务
				if($(".chx").val()=="on"){
					$(".chx").each(function(){
						var paramList = $(this).attr("id").split("+");
						if(paramList[5]=="柜台Pos支付"){
							//如果该任务是柜台支付
							$("[name='need2dotask']").show();
						}
					});
				}
			}
		};
		 
		//只用任务查询一个单, 刚好是配送任务, 选的是全部, 查出来是自取, 也给他走批量
		function showBatchDeliverySuccess(){
			if($("#tasktype").val()=="03" & $("#deliType").val()==""){
				if($("#maininstanceid").val()!=""){
					//如果查到任务
					if($(".chx").val()=="on"){
						$(".chx").each(function(){
							var paramList = $(this).attr("id").split("+");
							if(paramList[5]=="0"){
								//如果该任务是自取任务
								$("[name='need2delivery']").show();
							}
						});
					}
				}
			}
		}
		
		
		function initMyJs(){
			
			//全选事件
			$(".checkAll").click(function(){
				if(this.checked){
				       $(".chx").prop('checked', true);
				   }else{
				       $(".chx").prop('checked', false);
				   };
			});
			//全选按钮相关事件
			$(".chx").change(function(){
				var checked = 0;
				var flag = 0;
				$(".chx").each(function(){
					flag +=1 ;
					if(this.checked){
						checked += 1 ;
					}
				});
			    if(flag == checked){
			        //如果其它的复选框全部被勾选了，那么全选勾中
			        $('.checkAll').prop('checked',true);
			    }else{        
			        $('.checkAll').prop('checked',false);
			    };
			});
			
			//翻页首页末页点击事件
			$(".toPageop").on("click", function() {
				var toPageid = $(this).attr("id");
				if(toPageid == "tofirst"){
					currentpage = 1;
				}else if(toPageid == "toprevious"){
					currentpage -= 1;
				}else if(toPageid == "tonext"){
					currentpage += 1;
				}else if(toPageid == "tolast"){
					currentpage = Number($("#totalPages").val());
				}
				refreshTaskManageList();
			});
			//选择页码跳转
			$("#gotoPage").on("change", function() {
				var toPage = $(this).val();
				//不是选择的当前页就跳转
				if(toPage != currentpage){
					currentpage = Number(toPage);
					refreshTaskManageList();
				}
			});
			//认证任务认领方法
			$(".getCfTaskBtn").on("click", function(){
				var cfinfoId = $(this).attr("id");
//				alertmsg("认证任务认领&"+cfinfoId);
				$.ajax({
					url : '/cm/business/ordermanage/getCertificationTask',
					type : 'GET',
					dataType : 'json',
					data : "cfTaskId=" + cfinfoId,
					cache : false,
					async : true,
					error : function() {
						alertmsg("Connection error");
					},
					success : function(data) {
						if(data.status == "success"){
							location.href = "/cm/business/certificationtask/showcertificationtask?taskid="+cfinfoId;
						}else if(data.status == "fail"){
							alertmsg(data.msg);
						}
					}
				});
			});
			//车险任务认领方法
			$(".getTaskBtn").on("click", function(){
				var paramList = $(this).attr("id").split("_");
				//alertmsg("车险任务认领&"+paramList[0]+"&"+paramList[1]+"&"+paramList[2]+"&"+paramList[3]+"&"+paramList[4]);
				$.ajax({
					url : '/cm/business/ordermanage/getCarInsureTask',
					type : 'GET',
					dataType : 'json',
					data : "maininstanceId="+paramList[0]+"&subInstanceId="+paramList[1]+"&inscomcode="+paramList[2]+"&mainOrsub="+paramList[3]+"&statu="+paramList[4],
					cache : false,
					async : true,
					error : function() {
						alertmsg("Connection error");
					},
					success : function(data) {
						var forwardURL = "";
						if(data.status == "success"){
							if(paramList[4] == "20"){
								//支付
//								location.href = "/cm/business/ordermanage/underpaidding?taskid="+paramList[0]+"&taskcode="+paramList[4]+"&inscomcode="+paramList[2];
								forwardURL = "/cm/business/ordermanage/underpaidding?frompage=orderManage&taskid="+paramList[0]+"&taskcode="+paramList[4]+"&inscomcode="+paramList[2];
							}else if(paramList[4] == "23" || paramList[4] == "27" || paramList[4] == "28"){
								//承保打单
								forwardURL = "/cm/business/ordermanage/underwriting?frompage=orderManage&taskid="+paramList[0]+"&taskcode="+paramList[4]+"&inscomcode="+paramList[2];
							}else if(paramList[4] == "24"){
								//配送
								forwardURL = "/cm/business/ordermanage/delivery?frompage=orderManage&taskid="+paramList[0]+"&taskcode="+paramList[4]+"&inscomcode="+paramList[2];
							}
							if (forwardURL != "") {
								if($(window.top.document).find("#menu").css("display")=="none"){
									$.cmTaskList('my', 'list4deal', true);
//									$.insLoading();
									$(window.top.document).find("#desktop_content").attr("src", forwardURL);
								}else{
									location.href=forwardURL;
								}
							}
						}else if(data.status == "fail"){
							alertmsg(data.msg);
						}
					}
				});
			});
			
			//待支付任务承保查询
			$(".getPayUnderwritequery").on("click", function(){
				var paramList = $(this).attr("id").split("_");
				//alertmsg("车险任务认领&"+paramList[0]+"&"+paramList[1]+"&"+paramList[2]+"&"+paramList[3]+"&"+paramList[4]);
				$.ajax({
					url : '/cm/business/ordermanage/payunderwriteSearch',
					type : 'GET',
					dataType : 'json',
					data : "taskid="+paramList[0]+"&subInstanceId="+paramList[1]+"&inscomcode="+paramList[2],
					cache : false,
					async : true,
					error : function() {
						alertmsg("Connection error");
					},
					success : function(data) {
						alertmsg(data.msg);
					}
				});
			});
			//支付任务重新发起核保
//			$(".reUnderwriting").on("click", function(){
//				var paramList = $(this).attr("id").split("_");
////				alertmsg("车险任务认领&"+paramList[0]+"&"+paramList[1]+"&"+paramList[2]);
//				$.ajax({
//					url : '/cm/business/ordermanage/reUnderwriting',
//					type : 'GET',
//					dataType : 'json',
//					data : "maininstanceId="+paramList[0]+"&subInstanceId="+paramList[1]+"&inscomcode="+paramList[2],
//					cache : false,
//					async : true,
//					error : function() {
//						alertmsg("Connection error");
//					},
//					success : function(data) {
//						if(data.status == "success"){
//							alertmsg(data.msg);
//							refreshTaskManageList();
//						}else if(data.status == "fail"){
//							alertmsg(data.msg);
//						}
//					}
//				});
//			});
			//批量支付成功
			$("#batchPaySuccess").on("click", function(){
				var flag = 0;
				var callback =0;
				//置灰按钮
				var button = $(this);
				button.prop("disabled", true);
				//获取选中任务
				$(".chx").each(function(){
					if(this.checked){
						flag += 1;
						var paramList = $(this).attr("id").split("+");
						//alertmsg("车险任务认领&"+paramList[0]+"&"+paramList[1]+"&"+paramList[2]);
						$.ajax({
							url : '/cm/business/ordermanage/getAndPaySuccess',
							type : 'POST',
							dataType : 'text',
							data : "{\"processinstanceid\":\""+paramList[1]+"\",\"inscomcode\":\""+paramList[2]+"\",\"taskid\":\""+paramList[0]+"\",\"underWritingFlag\":\"1\",\"issecond\":\""+"0"+"\"}",
							contentType: 'application/json',
							cache : false,
							async : true,
							error : function() {
								button.prop("disabled", false);
								alertmsg("Connection error");
							},
							success : function(data) {
								callback +=1;
								/*if (data=="success") {
									alertmsg("申请重新核保成功！");
									refreshTaskManageList();
								}else if(data=="fail"){
									alertmsg("申请重新核保失败！");
								}else if(data=="paying"){
									alertmsg("订单正在支付中，不能重新核保！");
								}else{
									alertmsg(data);
								}*/
								//当所有异步完成重新查询一边
								if(flag==callback){
									//恢复按钮
									button.prop("disabled", false);
									$("#superquerybutton").trigger("click");
									alertmsg("批量支付任务成功");
								}
							}
						});
					}else{
						setTimeout(function(){
							if(flag == 0){
								//提示没有选择任务
								alertmsg("请选择需要处理的任务");
								button.prop("disabled", false);
							}
						},20);
					}
				});
			});
			//批量自取成功
			$("#batchDeliverySuccess").on("click", function(){
				var flag = 0;
				var callback =0;
				//按钮置灰
				var button = $(this);
				button.prop("disabled", true);
				//获取选中任务
				$(".chx").each(function(){
					if(this.checked){
						flag += 1;
						var paramList = $(this).attr("id").split("+");
						//alertmsg("车险任务认领&"+paramList[0]+"&"+paramList[1]+"&"+paramList[2]);
						$.ajax({
							url : '/cm/business/ordermanage/batchDeliSuccess',
							type : 'POST',
							dataType : 'text',
							data : "{\"processinstanceid\":\""+paramList[0]+"\",\"inscomcode\":\""+paramList[2]+"\",\"tracenumber\":\"\",\"deltype\":\""+"0"+"\",\"codevalue\":\"1\"}",
							contentType: 'application/json',
							cache : false,
							async : true,
							error : function() {
								button.prop("disabled", false);
								alertmsg("Connection error");
							},
							success : function(data) {
								callback +=1;
								//当所有异步完成重新查询一边
								if(flag==callback){
									button.prop("disabled", false);
									$("#superquerybutton").trigger("click");
									alertmsg("批量配送任务成功");
								}
							}
						});
					}else{
						setTimeout(function(){
							if(flag == 0){
								//提示没有选择任务
								alertmsg("请选择需要处理的任务");
								button.prop("disabled", false);
							}
						},20);
					}
				});
			});
			//支付任务重新发起核保
			$(".reUnderwriting").on("click", function(){
				var paramList = $(this).attr("id").split("_");
//				alertmsg("车险任务认领&"+paramList[0]+"&"+paramList[1]+"&"+paramList[2]);
				$.ajax({
					url : '/cm/business/ordermanage/getPaySuccess',
					type : 'POST',
					dataType : 'text',
					data : "{\"processinstanceid\":\""+paramList[1]+"\",\"inscomcode\":\""+paramList[2]+"\",\"taskid\":\""+paramList[0]+"\",\"underWritingFlag\":\"0\",\"issecond\":\""+"2"+"\"}",
					contentType: 'application/json',
					cache : false,
					async : true,
					error : function() {
						alertmsg("Connection error");
					},
					success : function(data) {
						if (data=="success") {
							alertmsg("申请重新核保成功！");
							refreshTaskManageList();
						}else if(data=="fail"){
							alertmsg("申请重新核保失败！");
						}else if(data=="paying"){
							alertmsg("订单正在支付中，不能重新核保！");
						}else{
							alertmsg(data);
						}
					}
				});
			});
			//隐藏柜台支付相关
			hidePaymentMethod();
			//隐藏配送相关
			hideDelivery();
			//如果没有数据时隐藏全选功能按钮
			hideButton();
			//查询到的是单个任务
			showBatchPaySuccess();
			showBatchDeliverySuccess();
		}
		//初始化列表数据
//		refreshTaskManageList();
		//-----------------
		 //修改起始时间
		 $("#taskcreatetimeupTxt").change(function(){
		    	var startString=$("#taskcreatetimeupTxt").val();//获取当前显示的时间字符串
		    	$("#taskcreatetimeup").val(startString);
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
		    	$("#taskcreatetimedownTxt").datetimepicker('setEndDate',endTime);
		    	var showEnd=$("#taskcreatetimedownTxt").val();//获取当前的截止日期
		    	var dateShowEnd=Date.parse(showEnd);
		    	var currentEnd=new Date(dateShowEnd);
		    	var days=0;
		    	if(currentEnd>startime){
		    		days=(currentEnd-startime)/(1000*60*60*24);
		    	}else{
		    		days=(startime-currentEnd)/(1000*60*60*24);
		    	}   	
		    	if(days>30){//若页面显示的时间差超过一个月,则将显示的截止时间改为设置的截止时间
		    		$("#taskcreatetimedownTxt").val(endString);
		    		$("#taskcreatetimedown").val(endString);
		    	}
		    });
		 	
		 	//修改截止时间
		    $("#taskcreatetimedownTxt").change(function(){
		    	var endString=$("#taskcreatetimedownTxt").val();
		    	$("#taskcreatetimedown").val(endString);
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
		    	//$("#taskcreatetimeupTxt").datetimepicker('setStartDate',startime);
		    	var showStart=$("#taskcreatetimeupTxt").val();//获取当前的截止日期
		    	var dateShowStart=Date.parse(showStart);
		    	var currentStart=new Date(dateShowStart);
		    	var days=0;
		    	if(currentStart>endTime){
		    		days=(currentStart-endTime)/(1000*60*60*24);
		    	}else{
		    		days=(endTime-currentStart)/(1000*60*60*24);
		    	}   	
		    	if(days>30){//若页面显示的时间差超过一个月,则将显示的截止时间改为设置的截止时间
		    		$("#taskcreatetimeupTxt").val(startString);
		    		$("#taskcreatetimeup").val(startString);
		    	}
		    });
		    
		
	});
})

function openNoti(taskid, inscomcode, notiType) {
	window.parent.openLargeDialog("/cm/business/mytask/queryusercomment?taskid="+taskid+"&inscomcode="
			+inscomcode+"&notiType="+notiType);
}
