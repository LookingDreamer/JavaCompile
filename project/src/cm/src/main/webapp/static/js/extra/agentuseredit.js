require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","jqvalidatei18n", "bootstrapdatetimepicker","bootstrapdatetimepickeri18n","jqcookie", "jqtreeview","zTree","zTreecheck","zzbconf/public"], function ($) {
	//数据初始化 
	$(function() {
		$("#istest").on("change",function(){
			var temp_data = $("#istest").find("option:selected").val();
			if(temp_data==3){
				$("#base_data").append("<tr id='new_tr'><td class='col-md-1' align='right' style='vertical-align: middle;'>关联正式账户</td><td><input class='form-control' type='text' name='jobnum4virtual'></td><td></td><td></td><td></td><td></td></tr>");
			}else{
				$("#new_tr").remove();
			}
		})
		
		
		set_id=$("#setid option:selected").val();  
		agent_id = $("#id").val();
		
		$("#go_back").on("click",function(){
			window.location.href="menu2list";
		})
		//初始化供应商
		$("#setid").on("change",function(){
			
			//功能包修改，联动供应商
			set_id=$(this).val();
			$.fn.zTree.init($("#provider_tree"), provider_setting);
			
			
			//代理人不能编辑功能包信息
//			if(set_id!=""){
//				$("#save_agent_permisssion_provider").attr("disabled","disabled ");
//			}else{
//				$("#save_agent_permisssion_provider").removeAttr("disabled")
//			}
			
			//联动功能包权限列表
			$.ajax({
				url:'initpermissionlistpage',
				type:'get',
				data:({permissionsetId:set_id,agentId:agent_id,limit:10}),
				success:function(data){
					//$('#permisssion_list').bootstrapTable('load', data);
				}
				
			})
		})
		
//		if(set_id!=""){
//			$("#save_agent_permisssion_provider").attr("disabled","disabled ");
//		}
		
		//保存代理人基本信息
		$("#save_agent").on("click",function(){ 
			//alertmsg($("#agent_form").valid()); 
			var agentid = $("#agentid").val();
			var name = $("#name").val();
			var deptname = $("#deptname1").val();
			var deptid = $("#deptid").val();
			var agentkind = $("#agentkind").val();
			var approvesstate = $("#approvesstate").find("option:selected").text();
			var phoneverify1 = $("#phoneverify1").val();
			var phoneverify2 = $("#phoneverify2").val();
			var flag = true;
			if(name.trim() == ""){
				alertmsg("姓名不能为空");
				return false;
			}
			if((deptname == "" || deptname == undefined) && (deptid == "" || deptid == undefined)){
				alertmsg("所属机构不能为空");
				return false;
			}else if(deptid != "" && deptid != undefined){
				$.ajax({
					url:'deptverify',
					type:'get',
					dataType:'json',
					async:false,
					data:({deptid:deptid}),
					success:function(data){
						if(data.msg=="false"){
							alertmsg("所属机构必须为网点");
							flag = false;
						}
					}
				})
			}
			if(flag == false){
				return false;
			}
			if(agentkind == "2" || agentkind == "3"){
				if(approvesstate != "认证通过"){
					alertmsg("您选择的用户类型所对应的认证状态必须为认证通过");
					return false;
				}
			}
			if(phoneverify1 != "" && phoneverify2 != "" && phoneverify1 == phoneverify2){
				alertmsg("两个手机号不能相同");
				return false;
			}
			if(/^1\d{10}$/.test(phoneverify1)){
				$.ajax({
					url:'phoneverify',
					type:'get',
					dataType:'json',
					async:false,
					data:({phonenum:phoneverify1,agentid:agentid}),
					success:function(data){
						if(data.msg=="该手机号已存在"){
							alertmsg("手机号码1已存在");
							flag = false;
						}
					}
				})
			}else{
				alertmsg('请输入有效11位手机号！');
				return false;
			}
			if(flag == false){
				return false;
			}
			if(phoneverify2 != ''){
				if(/^1\d{10}$/.test(phoneverify2)){
					$.ajax({
						url:'phoneverify',
						type:'get',
						dataType:'json',
						async:false,
						data:({phonenum:phoneverify2,agentid:agentid}),
						success:function(data){
							if(data.msg=="该手机号已存在"){
								alertmsg("手机号码2已存在");
								flag = false;
							}
						}
					})
				}else{
					alertmsg('请输入有效11位手机号！');
					return false;
				}
			}
			if(flag == false){
				return false;
			}
			if($("#agent_form").valid()){				
				$.ajax({
					url:'saveorupdateagent',
					type:'post',
					data:$("#agent_form").serialize(),
					success:function(data){
						if(data.length>0){							
							//每次更改最值机构时更新供应商信息（通过字段区分）
							 $("#id").attr("value",data);
							 agent_id =  $("#id").val();
							$.fn.zTree.init($("#provider_tree"), provider_setting);
							alertmsg("保存成功");
						}else{
							alertmsg("保存失败");
						}
					}					
				})
			}			
		});				

		$("#agent_form").validate({
			errorLabelContainer : ".alert-danger",
			errorElement : "p",
	        errorClass : "text-left",
	        focusInvalid : false,
	        rules: {
	        	agentcode:{
	        		required:true, 
		        	remote:{
		        		type:"post",
		        		url:"checkusercode",
		        		data:{
		        			agentcode:function(){return $("#agentcode").val();},
		        			id:function(){return $("#id").val()}
		        		}
		        	}		        		
	        	},
	        	deptid:{
	        		required:true
	        	},
	        	deptid1:{
	        		required:true
	        	},
	        	mobile:{
	        		required:true
	        	},
	        	name:{
	        		required:true
	        	}
	        },  
	        messages: { 
	        	agentcode:{
	        		required: "代理人编码不能为空",
	        		remote: jQuery.format("该代理人编码已存在")
	        	},
	        	deptid:{
	        		required: "所属机构不能为空"
	        	},
	        	deptid1:{
	        		required: "所属机构不能为空"
	        	},
	        	mobile:{
	        		required: "手机号码1不能为空"
	        	},
	        	name:{
	        		required: "姓名不能为空"
	        	}
	        }
	    });
		
		//代理人   供应商 保存 
		$("#save_agent_permisssion_provider").on("click",function(){					
			//得到选中的供应商code(未选中进行提示)
			var treeObj = $.fn.zTree.getZTreeObj('provider_tree');  
		    var nodes = treeObj.getCheckedNodes(true);  
		    var checkedIds = '';  
		    for(var i=0; i<nodes.length; i++){  
		        checkedIds += nodes[i].id + ',';  
		    }  
		    if(checkedIds.length > 0){  
		        checkedIds = checkedIds.substring(0, checkedIds.length-1);  
		    }  
			if(checkedIds==""){
				alertmsg("请选择供应商");
				return false;
			}
		    //保存功能包 权限 供应商关系
		    $.ajax({
		    	url:'saveagentproviderdata',
		    	type:'post',
		    	data:({providerIds:checkedIds,agentId:agent_id}),
		    	success:function(data){
		    		if(data.status=="1"){
		    			alertmsg(data.message);
		    		}else{
		    			alertmsg(data.message);
		    		}
		    	}
		    })
		})
		
		//$.fn.zTree.init($("#provider_tree"), provider_setting);
		
		//弹出机构树
		$("#deptname").on("click",function(){
			$.fn.zTree.init($("#dept_tree"), deptsetting);
			$("#myModal_dept").removeData("bs.modal");
			$("#myModal_dept").modal({
				 show: true
			});
			
		})
		//弹出机构树 常用网点设置
		$("#commonusebranchname").on("click",function(){
			/*$.fn.zTree.init($("#commonusebranchname_tree"), cdeptsetting);*/	
			$.fn.zTree.init($("#commonusebranchname_tree"), cdeptsetting);
			$("#myModal_commonusebranch").removeData("bs.modal");
			$("#myModal_commonusebranch").modal({
				 show: true
			});
		})
		//弹出渠道
		$("#channelname").on("click",function(){
			$.fn.zTree.init($("#channel_tree"), channelsetting);
			$("#myModal_channel").removeData("bs.modal");
			$("#myModal_channel").modal({
				 show: true
			});
			
		})


		//初始化银行卡信息
		$("#agentbankcard_list").bootstrapTable({
			method: 'get',
			url: "/cm/agentbankcard/initbankcardlist",
			cache: false,
			striped: true,
			pagination: true,
			sidePagination: 'server',
			pageSize: 10,
//		minimumCountColumns: 2,
			queryParams : getQueryParam,
			clickToSelect: true,
			columns: [{
				field: 'noti',
				title: '持卡人姓名',
				align: 'center',
				valign: 'middle',
				 width:400,
				clickToSelect: true,
				sortable: false
			},{
				field: 'banktoname',
				title: '银行名称',
				align: 'center',
				width:400,
				valign: 'middle',
				clickToSelect: true,
				sortable: false
			},{
				field: 'cardno',
				title: '卡号',
				width:400,
				align: 'center',
				valign: 'middle',
				clickToSelect: true,
				sortable: false
			},{
				field: 'cardnumlength',
				title: '卡号长度',
				align: 'center',
				valign: 'middle',
				visible: false,
				clickToSelect: true,
				sortable: false
			},{
				field: 'cardbankdeptcode',
				title: '发卡行机构编码',
				align: 'center',
				valign: 'middle',
				 
				 visible: false,
				clickToSelect: true,
				sortable: false
			},{
				field: 'cardagentid',
				title: '卡号所属代理人id',
				align: 'center',
				valign: 'middle',
				visible: false,
				clickToSelect: true,
				sortable: false
			},{
				field: 'branchbank',
				title: '开户行名称',
				align: 'center',
				valign: 'middle',
				width:400,
				clickToSelect: true,
				sortable: false
			}
			//,{
			//	field: 'operating',
			//	title: '操作',
			//	align: 'center',
			//	valign: 'middle',
			//	switchable:false,
			//	formatter: operateFormatter,
			//	events: operateEvents
			//}
			]
		});

		//初始化代理人配送地址信息
		$("#receiveAddress_list").bootstrapTable({
			method: 'get',
			url: "/cm/agentdeliveryaddress/initagentdeliveryaddresslist",
			cache: false,
			striped: true,
			pagination: true,
			sidePagination: 'server',
			pageSize: 10,
//		minimumCountColumns: 2,
			queryParams : getQueryParam2,
			clickToSelect: true,
			columns: [{
				field: 'recipientname',
				title: '收件人姓名',
				align: 'center',
				width:200,
				valign: 'middle',
				clickToSelect: true,
				formatter:veiwFormatter,
				sortable: false
			},{
				field: 'recipientmobilephone',
				title: '收件人手机号',
				width:250,
				align: 'center',
				valign: 'middle',
				clickToSelect: true,
				sortable: false
			},{
				field: 'recipientprovince',
				title: '省份',
				align: 'center',
				width:250,
				valign: 'middle',
				clickToSelect: true,
				sortable: false
			},{
				field: 'recipientcity',
				title: '城市',
				align: 'center',
				width:250,
				valign: 'middle',
				clickToSelect: true,
				sortable: false
			},{
				field: 'recipientarea',
				title: '地/县',
				width:250,
				align: 'center',
				valign: 'middle',
				clickToSelect: true,
				sortable: false
			},{
				field: 'recipientaddress',
				title: '收件人详细地址',
				align: 'center',
				width:300,
				valign: 'middle',
				clickToSelect: true,
				sortable: false
			},{
				field: 'isdefault',
				title: '是否是默认地址',
				visible: false,
				align: 'center',
				width:190,
				valign: 'middle',
				clickToSelect: true,
				formatter:isdefaultFormatter,
				sortable: false
			},{
				field: 'agentid',
				title: '代理人id',
				align: 'center',
				visible: false,
				valign: 'middle',
				clickToSelect: true,
				sortable: false
			},{
				field: 'delivetype',
				title: '配送方式',
				
				align: 'center',
				width:190,
				visible: false,
				valign: 'middle',
				clickToSelect: true,
				sortable: false
			}
				//,{
				//	field: 'operating',
				//	title: '操作',
				//	align: 'center',
				//	valign: 'middle',
				//	switchable:false,
				//	formatter: operateFormatter,
				//	events: operateEvents
				//}
			]
		});

		$("#myOrder_tab_id").trigger("click");
		$("#userbase_tab_id").trigger("click");

		//核审通过
		$("#passAuditId").on("click",function(){
			var agentid = $("#id").val();
			var noti = "认证通过。认证时间：" + date2str(new Date(),"yyyy-MM-dd hh:mm:ss");
			$.ajax({
				url:'auditAgentIdentity',
				type:'post',
				data:({Id:agentid, approvesstate:3, noti:noti}),
				success:function(data){
					if(data.status=="1"){
						alert(data.message);
					}else{
						alertmsg(data.message);
					}
					window.location.reload();
				}
			})
		});

		//核审不通过
		$("#backAuditId").on("click",function(){
			var agentid = $("#agentid").val();
			var noti = $("#noti").val();
			$.ajax({
				url:'auditAgentIdentity',
				type:'post',
				data:({Id:agentid, approvesstate:4, noti:noti}),
				success:function(data){
					if(data.status=="1"){
						alert(data.message);
					}else{
						alertmsg(data.message);
					}
					window.location.reload();
				}
			})
		});

		/*========================order begin===========================*/
		$(".table").hover(
			function(){
				$(this).find(".channelimg").attr("src","/cm/static/images/system/resource/resource2.png");
				if($(this).find("label:contains('渠道来源')").text()!="渠道来源："){
//					$(this).find("label:contains('渠道来源')").css("color","red");
				}
			},
			function(){
				$(this).find(".channelimg").attr("src","/cm/static/images/system/resource/resource1.png");
//				$(this).find("label:contains('渠道来源')").css("color","");
			}
		);
		//时间控件初始化设置
//		 $('.form_datetime').datetimepicker({
//		      language: 'zh-CN',
//		      format: "yyyy-mm-dd",
//		      weekStart: 1,
//		      todayBtn: 1,
//		      autoclose: 1,
//		      todayHighlight: 1,
//		      startView: 2,
//		      forceParse: 0,
//		      minView: 2,
//		      pickerPosition: "bottom-left"
//		      // showMeridian: 1
//		    });
		$("#mcreateTimeStart").datetimepicker({
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
		$("#mcreateTimeEnd").datetimepicker({
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
		//高级搜索
		$("#mseniorsearch").on("click",function(e){
			$("#msimplequery1").hide();
			$("#mseniorquery").show();
		});
		// 搜索条件【重置】按钮
		$("#mresetbutton").on("click", function(e) {
			$("#mnameorcarno").val("");

		});
		// 高级搜索条件【重置】按钮
		$("#mseniorresetbutton").on("click", function(e) {
			$("#minsureName").val("");
			$("#mprvId").val("");
			$("#mprovidername").val("");
			$("#mcarLicenseNo").val("");
			$("#mtaskId").val("");
			$("#mphone").val("");
			$("#mtaskState").val("");
//			$("#musertype").val("");
			$("#mshutter").val("");
			$("#mcreateTimeStart").val("");
			$("#mcreateTimeEnd").val("");
			$("#magentcode").val("");
			$("#mpaymentTransaction").val("");

		});
//		返回
		$("#mbackbutton").on("click",function(e){
			//window.location.href="/cm/agentuser/queryorderlist?agentId="+agent_id;
			window.history.back(-1);
		})
		//关闭高级搜索
		$("#mclosesearch").on("click",function(e){
			$("#msimplequery1").show();
			$("#mseniorquery").hide();
		});
		//快速搜索改变时
//		$("#mnameorcarno").on("change",function(e){
//			$("#mcarLicenseNoorcname").val($("#mnameorcarno").val());
//		});
		//点击搜索
		$("#msimplequerybutton").on("click",function(e){
			$("#mcarLicenseNoorcname").val($("#mnameorcarno").val());
			formsubmit();
		});
		//点击高级搜索
		$("#mseniorquerybutton").on("click",function(e){
			$("#mcurrentPage").val(1);
			$("#mcarLicenseNoorcname").val("");
			var startime = new Date($("#mcreateTimeStart").val());
			var endtime = new Date($("#mcreateTimeEnd").val());
			if(startime > endtime){
			alertt("起始时间不能大于终止时间！");
				return;
			}

			formsubmit();
		});
		//选择供应商
		$("#mcheckprovider").on("click", function(e) {
			$('#mshowprovider').modal();
			$.fn.zTree.init($("#mprovidertree"), providersetting);
		});

		//分页控制
		//首页
		$('#mfirst').on("click",function(e){
			$("#mcurrentPage").val(1);
			formsubmit();
		});
		//末页
		$('#mlast').on("click",function(e){
			$("#mcurrentPage").val($.trim($("#mlblPageCount").text()));
			formsubmit();
		});

		//上一页
		$('#mprevious').on("click",function(e){
			var pagecurrent = $("#mcurrentPage").val();
			if(pagecurrent > 0){
				pagecurrent--;
				$("#mcurrentPage").val(pagecurrent);
				formsubmit();
			}
		});

		//下一页
		$('#mnext').on("click",function(e){
			var pagecurrent = $("#mcurrentPage").val();
			var pagecount = $.trim($("#mlblPageCount").text());
			if(Number(pagecurrent)<Number(pagecount)){
				pagecurrent++;
				$("#mcurrentPage").val(pagecurrent);
				formsubmit();
			}
		});

		//修改起始时间
		$("#mcreateTimeStart").change(function(){
			var startString=$("#mcreateTimeStart").val();//获取当前显示的时间字符串
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
			$("#mcreateTimeEnd").datetimepicker('setEndDate',endTime);
			var showEnd=$("#mcreateTimeEnd").val();//获取当前的截止日期
			var dateShowEnd=Date.parse(showEnd);
			var currentEnd=new Date(dateShowEnd);
			var days=0;
			if(currentEnd>startime){
				days=(currentEnd-startime)/(1000*60*60*24);
			}else{
				days=(startime-currentEnd)/(1000*60*60*24);
			}
			if(days>30){//若页面显示的时间差超过一个月,则将显示的截止时间改为设置的截止时间
				$("#mcreateTimeEnd").val(endString);
			}
		});

		//修改截止时间
		$("#mcreateTimeEnd").change(function(){
			var endString=$("#mcreateTimeEnd").val();
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
			//$("#mcreateTimeStart").datetimepicker('setStartDate',startime);
			var showStart=$("#mcreateTimeStart").val();//获取当前的截止日期
			var dateShowStart=Date.parse(showStart);
			var currentStart=new Date(dateShowStart);
			var days=0;
			if(currentStart>endTime){
				days=(currentStart-endTime)/(1000*60*60*24);
			}else{
				days=(endTime-currentStart)/(1000*60*60*24);
			}
			if(days>30){//若页面显示的时间差超过一个月,则将显示的截止时间改为设置的截止时间
				$("#mcreateTimeStart").val(startString);
			}
		});

		if($("#myOrderActive").val()=="true"){
			$("#myOrder_tab_id").click();
		};

		$("#myOrder_tab_id").on("click",function(){
			$("#mseniorquerybutton").click();
		});


		/*=========================order end=====================================*/

	});
	
});

/// <summary>
/// 格式化显示日期时间
/// </summary>
/// <param name="x">待显示的日期时间，例如new Date()</param>
/// <param name="y">需要显示的格式，例如yyyy-MM-dd hh:mm:ss</param>
function date2str(x,y) {
	var z = {M:x.getMonth()+1,d:x.getDate(),h:x.getHours(),m:x.getMinutes(),s:x.getSeconds()};
	y = y.replace(/(M+|d+|h+|m+|s+)/g,function(v) {return ((v.length>1?"0":"")+eval('z.'+v.slice(-1))).slice(-2)});
	return y.replace(/(y+)/g,function(v) {return x.getFullYear().toString().slice(-v.length)});
}
//alert(date2str(new Date(),"yyyy-MM-dd hh:mm:ss"));
//alert(date2str(new Date(),"yyyy-M-d h:m:s"));

//添加编辑
function operateFormatter(value, row, index) {
    return [
        '<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
        '<i class="glyphicon glyphicon-edit"></i>',
        '</a>'
    ].join('');
}
//弹出编辑窗带参数 id
window.operateEvents = {
    'click .edit': function (e, value, row, index) {
//    	if(set_id!=""){
//    		alertmsg("功能包不能编辑！");
//    		return false;
//    	}
    	var apid="" ;
    	//关系表id
    	if(row.apid!=undefined){
    		apid = row.apid;
    	}
    	 $('#agent_permission_modal').removeData('bs.modal');
    	 
    	 //代理人id
    	 agent_id = $("#id").val();
    	 if(agent_id==""){
    		 alertmsg("请先保存功能包基础信息");
    		 return false;
    	 }else{
    		 $("#agent_permission_modal").modal({
    				show: true,
    				//                              权限id                代理人id        关系表id
    				remote: 'agent2agentpermission?id='+row.id
    			});
    	 }
    	
    	
    }
};



var provider_setting = {
		async: {
			enable: true,
			url:"initprovidertree",
			otherParam:{"agentId":function(){
					//得到当前代理人id
					return agent_id;
				},"setId":function(){
					
					//得到选中的功能包id
					return set_id;
				}
			} 
		},
		check: {
			enable: true,
			autoCheckTrigger: true,
			chkboxType:{  "Y" : "", "N" : "ps"}
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};                                  

function zTreeOnAsyncSuccess(event, treeId, msg) { 
          try { 
        	  var treeObj = $.fn.zTree.getZTreeObj('provider_tree');  
                 //调用默认展开第一个结点 
        	  	var nodes = treeObj.getCheckedNodes(true); 
                 for(var i=0;i<nodes.length;i++ ){
                	 treeObj.expandNode(nodes[i], true); 
                	 var childNodes = treeObj.transformToArray(nodes[i]);
                	 for(var j=0;j<childNodes.length;j++){
                		 treeObj.expandNode(childNodes[1], true);
                	 }
                 }
           } catch (err) { 
        	   
           } 
} 
//得到所有已经选中的节点
function beforeAsync(treeId, treeNode) {
	return true;
}

//常用设置网点，获取的网点数据
function showMenu() {
	var deptObj = $("#commonusebranchname");
	var deptOffset = $("#commonusebranchname").offset();
	$("#commonusebranchname_tree").css({left:deptOffset.left + "px", top:deptOffset.top + deptObj.outerHeight() + "px"}).slideDown("fast");

	$("body").bind("mousedown", onBodyDown);
}

function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "commonusebranchname" || event.target.id == "commonusebranchname_tree" || $(event.target).parents("#commonusebranchname_tree").length>0)) {
		hideMenu();
	}
}
function hideMenu() {
	$("#commonusebranchname_tree").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}
function beforeClick(treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("commonusebranchname_tree");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

function onCheck(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("commonusebranchname_tree"),
	nodes = zTree.getCheckedNodes(true),
	v_id="";
	v = "";
	for (var i=0, l=nodes.length; i<l; i++) {
		v += nodes[i].name + ",";
		v_id += nodes[i].id + ",";
	}
	if (v.length > 0 ) {
		v = v.substring(0, v.length-1);
	}
	if (v_id.length > 0 ) {
		v_id = v_id.substring(0, v_id.length-1);
	}
	var cityObj = $("#commonusebranchname");
	var deptIdObj = $("#commonusebranchid");
	cityObj.attr("value", v);
	deptIdObj.attr("value",v_id)
}
// 常用网点设置  结束
function filter(treeId, parentNode, childNodes) {
	if (!childNodes) return null;
	for (var i=0, l=childNodes.length; i<l; i++) {
		childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
	}
	return childNodes;
}
var deptsetting = {
		async: {
			enable: true,
			url:"initdepttree",
			autoParam:["id"],
			dataType: "json",
			type: "post"
		},
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
			
		},
		callback: {
			onCheck: deptTreeOnCheck
		}
	};
function deptTreeOnCheck(event, treeId, treeNode) {
	$("#deptid").val(treeNode.id);
	$("#deptname").val(treeNode.name);
	$('#myModal_dept').modal("hide");
}

var cdeptsetting = {
		view: {
			selectedMulti: true
		},
		async: {
			enable: true,
			url:"initdepttree",
			autoParam:["id","checked"],
			dataFilter: filter,
			otherParam:{"commonusebranchIds":function(){
				return $("#commonusebranchid").val();
			}}
		},
		check: {
			enable: true,
			autoCheckTrigger: true,
			chkboxType:{ "Y" : "", "N" : "ps"}
		},callback: {
			beforeAsync: beforeDeptAsync,
			onAsyncSuccess: zTreeDeptOnAsyncSuccess,
			onCheck:on_dept_Check
		}
	};		
function on_dept_Check(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("commonusebranchname_tree"),
	nodes = zTree.getCheckedNodes(true),
	v_id="";
	v = "";
	for (var i=0, l=nodes.length; i<l; i++) {
		v += nodes[i].name + ",";
		v_id += nodes[i].id + ",";
	}
	if (v.length > 0 ) {
		v = v.substring(0, v.length-1);
	}
	if (v_id.length > 0 ) {
		v_id = v_id.substring(0, v_id.length-1);
	}
	var deptObjName = $("#commonusebranchname");
	var deptObjId = $("#commonusebranchid");
	deptObjName.attr("value", v);
	deptObjId.attr("value",v_id)
}
function zTreeDeptOnAsyncSuccess(event, treeId, msg) { 
    try { 
  	  var treeObj = $.fn.zTree.getZTreeObj('commonusebranchname_tree');  
           //调用默认展开第一个结点 
  	  	var nodes = treeObj.getCheckedNodes(true); 
           for(var i=0;i<nodes.length;i++ ){
          	 treeObj.expandNode(nodes[i], true); 
          	 var childNodes = treeObj.transformToArray(nodes[i]);
          	 for(var j=0;j<childNodes.length;j++){
          		 treeObj.expandNode(childNodes[1], true);
          	 }
           }
     } catch (err) { 
  	   
     } 
} 
//得到所有已经选中的节点
function beforeDeptAsync(treeId, treeNode) {
	return true;
}

var channelsetting = {
		async: {
			enable: true,
			url:"initchanneltree",
			autoParam:["id"],
			dataType: "json",
			type: "post"
		},
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},
		callback: {
			onCheck: channelTreeOnCheck
		}
	};
function channelTreeOnCheck(event, treeId, treeNode) {
	$("#channelid").val(treeNode.id);
	$("#channelname").val(treeNode.name);
	$('#myModal_channel').modal("hide");
}


function getQueryParam(params){
	return {
		offset:params.offset,
		limit:params.limit,
		cardagentid:$("#agentid").val()
	}
}

function getQueryParam2(params){
	return {
		offset:params.offset,
		limit:params.limit,
		agentid:$("#agentid").val()
	}
}

/*=========================order begin=====================*/

var providersetting = {
	async: {
		enable: true,
		url:"queryprovidertree",
		autoParam:["id"],
		dataType: "json",
		type: "post"
	},
	check: {
		enable: true,
		chkStyle: "radio",
		radioType: "all"
	},
	callback: {
		onCheck: providerTreeOnCheck
	}
};

//表单提交
function formsubmit(){
	$("#mqueryorder").submit();
}

function providerTreeOnCheck(event, treeId, treeNode) {
	$("#mprvId").val(treeNode.prvcode);
	$("#mprovidername").val(treeNode.name);
	$('#mshowprovider').modal("hide");
}

function checkthisstatus(obj,status){
	$(".nav-pills li").removeClass("active");
	$(obj).addClass("active");
	$("#mtaskState").val(status);
	formsubmit();
}

function go(pagenumber){
	$("#mcurrentPage").val(pagenumber);
	formsubmit();
}

//查看详情
function searchorderdetail(taskId,codename){
	if(taskId == ""){
		alertmsg("Connection error!");
		return false;
	}
	$("#dtaskId").val(taskId);
	$("#mcodename").val(codename);
	$("#dagentid").val(agent_id);
	$("#mdetailinfo").submit();
}
//活动类型文字转换
function isdefaultFormatter(value, row, index) {
	if(value=='1'){
		return '是';
	
	}else{
		return '否';
	}
}
function veiwFormatter(value, row, index) {
	if(row.isdefault=='1'){
		return '<span class="label label-danger">默认</span>    '+row.recipientname;
	
	}else{
		return row.recipientname;
	}
}