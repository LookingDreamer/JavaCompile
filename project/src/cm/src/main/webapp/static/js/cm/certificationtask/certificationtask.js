require(["jquery", 
	"bootstrap-table",
	"core",
	"bootstrap",
	"bootstrapTableZhCn",
	"jqvalidatei18n",
	"mousewheel",
	"jqtreeview",
	"zTree",
	"zTreecheck",
	"public"], function ($) {
	$(function() {   
		// 保存修改信息
		$("#saveMessage").on("click",function(){
			
			//保存修改信息 
			var para= "deptid="+$("#deptid").val()+
				"&name="+$("#name").val()+
				"&mobile="+$("#mobile").val()+
				"&idno="+$("#idno").val()+
				"&bankcard="+$("#bankcard").val()+
				"&cgfns="+$("#cgfns").val()+
				"&mainbusiness="+$("#mainbusiness").val()+
				"&id="+$("#id").val();
		//if(checkMobile()&&checkID_Card()&&checkBank_Card()){
		    $.ajax({
				url : 'updateagent?'+para,       
				type : 'GET',
				dataType : "json",
				async : true,
				error : function() {
					alertmsg("Connection error");
				},
				success : function(){
					alertmsg("保存成功");
				    location.href="updateagent";
				}
		    });
	//	}
		});
		
//		//手机号码校验，长度为11位数字。
//		function checkMobile(Str) {
//		var Str=$("#mobile").val();
//		 RegularExp=/^[0-9]{11}$/
//		 if (RegularExp.test(Str)) {
//		  return true;
//		 }
//		 else {
//		  alertmsg("手机号格式不正确！应该为11位长度的数字！");
//		  return false;
//		 }
//		}
//		//身份证号码校验
//		function checkID_Card(Str)
//		{
//		   var Str=$("#idno").val();
//		   RegularExp = /^[0-9]{17}[0-9A-Za-z]{1}$|^[0-9]{14}[0-9A-Za-z]{1}$/
//		   if (RegularExp.test(Str))
//		   {
//		       return true;
//		   }else{
//		       alertmsg("身份证号格式不对！");
//		       return false;
//		   }
//		}	
//		
//		//银行卡号校验
//		function checkBank_Card(Str)
//	{
//			 var Str=$("#bankcard").val();
//			 RegularExp  = /^\d{19}$/g; // 以19位数字开头，以19位数字结尾 
//			 if (RegularExp.test(Str))
//			   {
//			       return true;
//			   }else{
//			       alertmsg("银行卡号格式不对！");
//		       return false;
//		   }
//		}
		initArea($("#provinceVal").val());
		changeprv($("#cityVal").val())
		var temp = 1;
		var deptsetting = {
				async: {
					enable: true,
					url:"/cm/agent/initdepttree",
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
		var channelsetting = {
				async: {
					enable: true,
					url:"/cm/agent/initchanneltree",
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
		//缩放图片
		$("#picDiv").on("mousewheel",function(event,delta) {
			if(delta>0){
				temp = parseFloat(temp)+0.5;
			}else{
				temp = parseFloat(temp)-0.5;
			}
			$("#cetificationPic").css("-moz-transform","scale("+temp+")");
			$("#cetificationPic").css("filter","progid:DXImageTransform.Microsoft.BasicImage(rotation=1)");
			$("#cetificationPic").css("-o-transform","scale("+temp+")");
			$("#cetificationPic").css("-webkit-transform","scale("+temp+")");
			$("#cetificationPic").css("transform","scale("+temp+")");
		    return false;
		});
		//弹出机构树
		$("#deptname").on("click",function(){
			$("#myModal_dept").removeData("bs.modal");
			$("#myModal_dept").modal({
				 show: true
			});
			inittree();
		})
		//弹出渠道
		$("#channelname").on("click",function(){
			$.fn.zTree.init($("#channel_tree"), channelsetting);
			$("#myModal_channel").removeData("bs.modal");
			$("#myModal_channel").modal({
				 show: true
			});
		})

	});
});
//身份证验证正则表达式：
var shenfenzhengRegex15 = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
var shenfenzhengRegex18 = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
//手机号正则表达式/^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\d{8}$/
//var phoneRegex = /^1[3|4|5|7|8]\d{9}$/;
var phoneRegex = /^1\d{10}$/;
function deptTreeOnCheck(event, treeId, treeNode) {
	$("#deptid").val(treeNode.id);
	$("#deptname").val(treeNode.name);
	$('#myModal_dept').modal("hide");
}
function channelTreeOnCheck(event, treeId, treeNode) {
	$("#channelid").val(treeNode.id);
	$("#channelname").val(treeNode.name);
	$('#myModal_channel').modal("hide");
}
/****************************************************************************************************/
var deg = 0;
var choose= 1;
function lastPic(){
	if(choose>1){
		choose = parseInt(choose)-1;
		showPic(choose);
	}
}
function nextPic(){
	if(choose<5){
		choose = parseInt(choose)+1;
		showPic(choose);
	}
}
function showPic(choose){
	$("#cetificationPic").attr("src",$("#pic"+choose).val());
	$("#picName").text($("#picName"+choose).val());
}
function turnLeft(){
	deg=parseInt(deg)-90;
	$("#cetificationPic").css("-moz-transform","rotate("+deg+"deg)");
	$("#cetificationPic").css("filter","progid:DXImageTransform.Microsoft.BasicImage(rotation=1)");
	$("#cetificationPic").css("-o-transform","rotate("+deg+"deg)");
	$("#cetificationPic").css("-webkit-transform","rotate("+deg+"deg)");
	$("#cetificationPic").css("transform","rotate("+deg+"deg)");
}
function turnRight(){
	deg=parseInt(deg)+90;
	$("#cetificationPic").css("-moz-transform","rotate("+deg+"deg)");
	$("#cetificationPic").css("filter","progid:DXImageTransform.Microsoft.BasicImage(rotation=1)");
	$("#cetificationPic").css("-o-transform","rotate("+deg+"deg)");
	$("#cetificationPic").css("-webkit-transform","rotate("+deg+"deg)");
	$("#cetificationPic").css("transform","rotate("+deg+"deg)");
}
function certificate(id,status){
	if(status=="3" || status=="2" || (status=="0" && checkForm("0")) || (status=="1" && checkForm("1"))){
		if(status=="3" || status=="2"){
			if($.trim($("#commentcontent").val())==""){
				alertmsg("请填写备注。");
				return false;
			}
		}
		$.insLoading();
		$.ajax({
			url : "certificate",
			type : 'post',
			async: false,
			data: {
				"id":id,
				"mainbiz":$("#mainbiz").val(),
				"agentid":$("#agentid").val(),
				"region":$("#city").val(),
				"deptid":$("#deptid").val(),
				"channelid":$("#channelid").val(),
				"name":$.trim($("#name").val()),
				"mobile":$.trim($("#mobile").val()),
				"idcard":$.trim($("#idcard").val()),
				"bankcard":$.trim($("#bankcard").val()),
				"belongs2bank":$("#belongs2bank").val(),
				"rank":$("#rank").val(),
				"cgfns":$.trim($("#cgfns").val()),
				"formalnum":$.trim($("#formalnum").val()),
				"tempjobnum":$.trim($("#tempjobnum").val()),
				"referrer":$.trim($("#referrer").val()),
				"referrername":$.trim($("#referrername").val()),
				"commentcontent":$.trim($("#commentcontent").val()),
				"status":status,
			},
			success : function(data) {
				$.insLoaded();
				if(data == 0){
					alertmsg("操作失败 请稍后再试");
				}else if(data == -1){
					alertmsg("正式工号重复 请重新录入");
				}else if(data == 3){
					alertmsg("更新LDAP数据出错");
				}else if(data == 100){
					alertmsg("此手机号已注册！");
				}else if(data == 101){
					alertmsg("此身份证号已注册！");
				}else if(data == 102){
					alertmsg("此资格证号已注册！");
				}else if(data == 103){
					alertmsg("此正式工号已存在！请通知管理员绑定工号。");
				}else if(data == 104){
					alertmsg("此推荐人工号与推荐人不一致！");
				}else if(data == 105){
					alertmsg("此推荐人工号不存在！");
				}else if(data == 106){
					alertmsg("此推荐人姓名不能为空！");
				}else if(data == 107){
					alertmsg("此推荐人工号不能为空！");
				}else if(status == "0"){
					alertmsg("保存成功");
					$("#getNum").removeAttr("disabled");
					$("#handle").removeAttr("disabled");
					$("#formalnum").removeAttr("disabled");
				}else{
					alertmsg("操作成功");
					if($(window.top.document).find("#menu").css("display")=="none"){
	            		$.cmTaskList('my', '', true);
					}else{
						if($('#returnfrom').val()=='true'){
							window.location.href = "/cm/business/certificationtask/certificationMng";
						}else{
							window.location.href = "/cm/business/mytask/queryTask";
						}
					}
				}
			}
		});
	}
}
function checkForm(statusFlag){
	if(statusFlag == "1"){
		//主营业务数据校验
		if(!$.trim($("#mainbiz").val())){
			alertmsg("主营业务不能为空！");
			return false;
		}
		//所在地区数据校验
		if(!$.trim($("#province").val()) || !$.trim($("#city").val())){
			alertmsg("所在地区不能为空！");
			return false;
		}
		//所属机构数据校验
		if(!$.trim($("#deptname").val())){
			alertmsg("所属机构不能为空！");
			return false;
		}
		//真实姓名数据校验
		if(!$.trim($("#name").val())){
			alertmsg("真实姓名不能为空！");
			return false;
		}
		//手机号数据校验
		var phoneVal = $.trim($("#mobile").val());
		if(!phoneRegex.test(phoneVal)){
			alertmsg("手机号格式不正确！");
			return false;
		}
		//身份证号数据校验
		var idNoVal = $.trim($("#idcard").val());
		if(idNoVal.length==15){
			if(!shenfenzhengRegex15.test(idNoVal)){
				alertmsg("身份证号格式不正确！");
				return false;
			}
		}else if(idNoVal.length==18){
			if(!shenfenzhengRegex18.test(idNoVal)){
				alertmsg("身份证号格式不正确！");
				return false;
			}
		}else{
			alertmsg("身份证号格式位数不合要求！");
			return false;
		}
		//开户行数据校验
		if(!$.trim($("#belongs2bank").val())){
			alertmsg("开户行不能为空！");
			return false;
		}
		//银行卡号数据校验
		if(!$.trim($("#bankcard").val())){
			alertmsg("银行卡号不能为空！");
			return false;
		}else if(isNaN($.trim($("#bankcard").val()))){
			alertmsg("银行卡号应为数字！");
			return false;
		}
	}
	return true;
}
function download(){
	$("#fm").submit();
}

function initArea(provinceVal){
	getAreaByParentid("0",$("#province"),provinceVal);
}
function changeprv(cityVal){
	 getAreaByParentid($("#province").val(),$("#city"),cityVal);
}
function changecity(){
	 getAreaByParentid($("#city").val(),$("#county"));
}

function getAreaByParentid(parentid,selectobject,val){
	//selectobject.empty();
	$.ajax({
		url : "/cm/region/getregionsbyparentid",
		type : 'GET',
		dataType : "json",
		data : {
			parentid:parentid
		},
		async : false,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			selectobject.empty();
			for (var i = 0; i < data.length; i++) {
				if(data[i].id == $.trim(val)){
					selectobject.append("<option selected='selected' value='"+data[i].id+"'>"+data[i].comcodename+"</option>");   
				}else{
					selectobject.append("<option value='"+data[i].id+"'>"+data[i].comcodename+"</option>");   
				}
			}
//			if(data.length>0){
//				selectobject.get(0).selectedIndex=0;
//			}
//			selectobject.trigger("change");  
		}
	});
}

function getFormalNum(id){
	$.ajax({
		url : "certificate",
		type : 'post',
		async: false,
		data: {
			"id":id,
			"mainbiz":$("#mainbiz").val(),
			"agentid":$("#agentid").val(),
			"region":$("#city").val(),
			"deptid":$("#deptid").val(),
			"channelid":$("#channelid").val(),
			"name":$.trim($("#name").val()),
			"mobile":$.trim($("#mobile").val()),
			"idcard":$.trim($("#idcard").val()),
			"bankcard":$.trim($("#bankcard").val()),
			"belongs2bank":$("#belongs2bank").val(),
			"cgfns":$.trim($("#cgfns").val()),
			"formalnum":$.trim($("#formalnum").val()),
			"tempjobnum":$.trim($("#tempjobnum").val()),
			"referrer":$.trim($("#referrer").val()),
			"referrername":$.trim($("#referrername").val()),
			"commentcontent":$.trim($("#commentcontent").val()),
			"status":0,
		},
		success : function(data) {
		}
	});
	if(checkForm("1")){
		$.insLoading();
		$.ajax({
			url : "getFormalNum",
			type : 'post',
			dataType : "json",
			data: {
				"id":id,
				"mainbiz":$("#mainbiz").val(),
				"agentid":$("#agentid").val(),
				"region":$("#city").val(),
				"deptid":$("#deptid").val(),
				"channelid":$("#channelid").val(),
				"name":$.trim($("#name").val()),
				"mobile":$.trim($("#mobile").val()),
				"idcard":$.trim($("#idcard").val()),
				"bankcard":$.trim($("#bankcard").val()),
				"belongs2bank":$("#belongs2bank").val(),
				"cgfns":$.trim($("#cgfns").val()),
				"formalnum":$.trim($("#formalnum").val()),
				"tempjobnum":$.trim($("#tempjobnum").val()),
				"referrer":$.trim($("#referrer").val()),
				"referrername":$.trim($("#referrername").val()),
				"commentcontent":$.trim($("#commentcontent").val()),
				"status":1,
			},
			success : function(data) {
				$.insLoaded();
				if(data.status == "success"){
					$("#formalnum").val(data.agentcode);
					if(data.updateStatu == "0"){
						alertmsg("操作失败 请稍后再试");
					}else if(data.updateStatu == "-1"){
						alertmsg(data.agentcode + "正式工号重复 请重新录入");
					}else if(data.updateStatu == "3"){
						alertmsg("更新LDAP数据出错");
					}else if(data.updateStatu == "100"){
						alertmsg($.trim($("#mobile").val())+"此手机号已注册！");
					}else if(data.updateStatu == "101"){
						alertmsg($.trim($("#idcard").val())+"此身份证号已注册！");
					}else if(data.updateStatu == "102"){
						alertmsg($.trim($("#cgfns").val())+"此资格证号已注册！");
					}else if(data.updateStatu == "103"){
						alertmsg(data.agentcode + "此正式工号已存在！请通知管理员绑定工号。");
					}else if(data.updateStatu == "104"){
						alertmsg("此推荐人工号与推荐人不一致！");
					}else if(data.updateStatu == "105"){
						alertmsg("此推荐人工号不存在！");
					}else if(data.updateStatu == "106"){
						alertmsg("此推荐人姓名不能为空！");
					}else if(data.updateStatu == "107"){
						alertmsg("此推荐人工号不能为空！");
					}else{
						if(data.result == "Y"){
							alertmsg("该身份证已经注册,工号："+data.agentcode+",请等候，正在同步代理人!");
							setTimeout(function(){
								if($('#returnfrom').val()=='true'){
									window.location.href = "/cm/business/certificationtask/certificationMng";
								}else{
									window.location.href = "/cm/business/mytask/queryTask";
								}
							},1000);
						}else{
							alertmsg("已申请到工号："+data.agentcode+"!");
							setTimeout(function(){
								if($('#returnfrom').val()=='true'){
									window.location.href = "/cm/business/certificationtask/certificationMng";
								}else{
									window.location.href = "/cm/business/mytask/queryTask";
								}
							},1000);
						}
					}
				}else{
					alertmsg(data.agentcode+",获取失败!");
					$("#formalnum").removeAttr("disabled");
				}
			}
		});
	}
}
/* zTree初始化数据 */
function inittree() {
	$.ajax({
		url : "/cm/agent/initdepttree2",
		type : 'POST',
		dataType : "json",
		error : function() {
			$("#dept_tree").html('加载失败，<a href="javascrpt:void(0);" onclick="inittree();">点击这里</a>重试。');
		},
		success : function(data) {
			$.fn.zTree.init($("#dept_tree"), {
				check: {
					enable: true,
					chkStyle: "radio",
					radioType: "all"
				},
				data : {
					simpleData: {
						enable: true,
						idKey: "id",
						pIdKey: "pId",
						rootPId: "1200000000"
					}
				},
				view: {
					fontCss: function (treeId, treeNode) {
						return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
					},
					expandSpeed: ""
				},
				callback : {
					onCheck : deptTreeOnCheck,
					
				}
			}, data);
		}
	});

}
