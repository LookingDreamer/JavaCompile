require([ "jquery", "zTree", "zTreecheck", "flat-ui", "bootstrap-table", "bootstrapdatetimepicker","bootstrapdatetimepickeri18n",
		"bootstrap", "bootstrapTableZhCn", "public" ], function($) {
	
	$("#insureConfig").hide();
	$("#baseInfo").show();
	$('#insureConfigdiv').hide();
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
	});
	
	$("#comeback").on("click",function(){
		location.href = "/cm/business/valetcatalogue/showaletcataloguelist";
	})
	
	
	//保存投保基本数据
	$("#submit").on("click",function(){
		$.ajax({
			url:'saveinsurance',
			data:$("#prosaveupdateform").serialize(),
			type:'post',
			success:function(data){
				alertmsg(data.message);
			}
		})
		
	})
	
	$("#baseInfobutton_next").on("click",function(){
		var carownername=$("#carowner_name").val();
		if (carownername=="") {
			alertmsg("车主姓名不能为空！")
			return ;
		}
		if ($("#carNumber").val()=="") {
			alertmsg("车主姓名不能为空！")
			return ;
		}
		$.ajax({
			url:'create',
			data:{
				agentid:$("#agentid").val(),
				plateNumber:$("#carNumber").val(),
				owerName:$("#carowner_name").val(),
				provinceName:$("#provinceName").val(),
				provinceCode:$("#provinceCode").val(),
				cityName:$("#cityName").val(),
				cityCode:$("#cityCode").val(),
				flag:"1"
				},
			type:'post',
			datatype:"json",
			success:function(data){
				if(issuccess(data.status)){
					fastrenewcallplatformquery(data.body.processinstanceid);
				}else{
					alert("该车牌未找到上一年投保信息，请走正常投保流程！");
				}
			}
		})
	});
	
	$("#show1").on("click",function(){
		modalshow();
	})
	
	$("#confirmpid").on("click",function(){
		callbackyes(piddata);
	})
	
	
	$("#insureConfigbutton_last").on("click",function(){
		$("#baseInfo").show();
		$("#insureConfig").hide();
		$('#baseInfodiv').show();
		$('#insureConfigdiv').hide();
		$("#insureConfig").removeClass();
		$("#baseInfo").addclass("active");
	})
	
})

var taskid = "";
var piddata = new Object();

function fastrenewcallplatformquery(processid){
	taskid = processid;
	$.ajax({
		url:'fastrenewcallplatformquery',
		data:{
			processinstanceid:taskid,
			agentid:$("#agentid").val(),
			plateNumber:$("#carNumber").val(),
			agentName:$("#agentname").val(),
			provinceName:$("#provinceName").val(),
			provinceCode:$("#provinceCode").val(),
			cityName:$("#cityName").val(),
			cityCode:$("#cityCode").val(),
			provid:$("#insurecom").val(),
			flag:"1"
			},
		type:'post',
		datatype:"json",
		success:function(data){
			if(issuccess(data.status)){
				if(data.body.queryflag == "0"){
					$("#baseInfo").hide();
					$("#insureConfig").show();
					$('#baseInfodiv').hide();
					$('#insureConfigdiv').show();
					$("#baseInfo").removeClass();
					$("#insureConfig").addClass("active");
					showinsureconfig();
				}else if(data.body.queryflag == "1"){
					fastrenewcallplatformquery(taskid);
				}else{
					alert("该车牌未找到上一年投保信息，请走正常投保流程！");
				}
			}
	//		if(issuccess(data.status)){
	//			
	//			taskid = data.body.processinstanceid;
	//			//调用平台查询接口
	//			callplatformquery(taskid);
	//		}else{
	//			alertmsg(data.message);
	//		}
		}
	})
}

function getQueryPara(params){
	return {
		modelname : $("#searchinfo").val(),
		offset:params.offset,
	    limit:params.limit
	}
}

function configtype(){
	$.ajax({
		url:"/cm/modelinsurance/insurancescheme?processinstanceid="+taskid+"&plankey="+$("#configtype_id").val(),
		type:"GET",
		datatype:"json",
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			//先清空
			deletetabletr();
			createtabletr(data.body);
		}
	});
}
function deleteprovidertabletr(){
	var listtr = $("#checktable").find("tr");
	if(listtr.length > 0){
		for(var i=1;i<listtr.length;i++){
			$(listtr[i]).remove();
		}
	}
}


function insureConfigshowdiv(val){
	var conf = GetInfoFromTable();
	if(conf == 1){
		return ;
	}
	$.ajax({
   		url:'/cm/modelinsurance/insuredconfig',
   		type:'post',
   		datatype:"json",
   		contentType: "application/json; charset=utf-8",
   		data:JSON.stringify(conf),
   		success:function(data){
   			if(issuccess(data.status)){
//	   				alertmsg(data.status);
   				verificationinsuredconfig(conf);
   			}else{
   				alertmsg(data.message);
   			}
   		}
	});
}

function verificationinsuredconfig(conf){
	$.ajax({
		url:'/cm/modelinsurance/verificationinsuredconfig',
		type:'post',
		datatype:"json",
		contentType: "application/json; charset=utf-8",
		data:JSON.stringify(conf),
		success:function(data){
			if(issuccess(data.status)){
//				alertmsg(data.status);
				modalshow(data);
			}else{
				alertmsg(data.message);
			}
		}
	})
}

function modalshow(data){
	var message = "";
	if(data.body.flag == true){
		$.each(data.body.guiZeCheckBeans, function(key, value) { 
			var m = value.prvshotname + "[" + value.reason + "]" + "系统将在报价中自动去掉该保险公司。请确认！<br>";
			message += m;
		}); 
	}
	$.each(data.body.configBeans, function(key, value) { 
		var m = "以下公司不支持" + value.kindname + "。系统将在这些公司的报价中自动去掉该险种。请确认！<br>";
		message += m;
		$.each(value.providers, function(n, msg) { 
			message += msg.prvshotname + "<br>";
		})
	}); 
	piddata = data;
	if(message != ""){
		$("#content").html(message);
		$("#mymodal1").modal('show');
	}else{
		callbackyes(piddata);
	}
}

function callbackyes(data){
	$("#mymodal1").modal('hide');
	var paramsList = new Array();
	$.each(data.body.guiZeCheckBeans, function(key, value) { 
		paramsList.push(value.pid);
	}); 
	$.ajax({
			url:'/cm/modelinsurance/workflowstartquote',
			type:'post',
			datatype:"json",
			contentType: "application/json; charset=utf-8",
			data:JSON.stringify(
					{processinstanceid:taskid,
					pids:paramsList,
					flag:1
					}),
			success:function(data){
				if(issuccess(data.status)){
//					location.href="/cm/multiplelist/multiplelist?taskid=" + taskid;
					alert("快速续保提交核保成功");
				}else{
					alertmsg(data.message);
				}
			}
		})
}

function issuccess(status){
	return $.trim(status) == "success";
}

//为true是，obj为null或者undefined
function isexecit(obj){
	return obj== undefined;
}

function showinsureconfig(){
	$.ajax({
		url:'/cm/modelinsurance/schemelist?processinstanceid=' + taskid,//+taskid,
		type:'get',
		datatype:"json",
		success:function(data){
			if(issuccess(data.status)){
				$("#configtype_id").children().remove();
				$.each(data.body, function(key, value) { 
					$("#configtype_id").append("<option value='"+key+"'>"+value+"</option>");
					configtype();
				}); 
			}else{
				alertmsg(data.message);
			}
		}
	});
}

function changeflag(i){
	if($("#selectedOption" + i).val() == 0){
		$('input[name="selectedflag' +i+'"]:checkbox').val("0");
		$('input[name="selectedflag' +i+'"]:checkbox').attr("checked",false);
		if("CompensationDuringRepairIns" == $("#selectedOption" + i).attr("name")){
			$("#compensationdays").hide();
		}
	}else{
		if("CompensationDuringRepairIns" == $("#selectedOption" + i).attr("name")){
			$("#compensationdays").show();
		}
	}
}

//生成table tr
function createtabletr(object){
	var html = "";
	$.each(object, function(i, item) {
		var selectedid = "selectedid"+i;
		html += '<tr><td style="text-align: center; vertical-align: middle;"> ';
		html += '<input type="text" style="display:none;" id="selectedtext' + i + '" value="' + item.riskkindname + '"/>';
		html += item.riskkindname+'</td><td style="text-align: center; vertical-align: middle;"><select class="form-control" name="' + item.riskkindcode + '" id="selectedOption'+i+'" onchange="changeflag(' + i + ')"> ';
		if(item.plankey == "snbxpz"){
			$.each(item.insuredConfig.value, function(j, sitem) {
				html += '<option ' + i + ' value="'+sitem.value+'" ';
				if(item.isSelect == true){
					if(sitem.value == item.coverage){
						html += ' selected="true">'+sitem.key+'</option> ';
					}else{
						html += ' >'+sitem.key+'</option> ';
					}
				}else{
					if(sitem.value == "0"){
						html += ' selected="true">'+sitem.key+'</option> ';
					}else{
						html += ' >'+sitem.key+'</option> ';
					}
				}
			});
		}else{
			$.each(item.insuredConfig.value, function(j, sitem) {
				html += '<option ' + i + ' value="'+sitem.value+'" ';
				if(item.isSelect == true){
					if(sitem.key == item.coverage){
						html += ' selected="true">'+sitem.key+'</option> ';
					}else{
						html += ' >'+sitem.key+'</option> ';
					}
				}else{
					if(sitem.value == "0"){
						html += ' selected="true">'+sitem.key+'</option> ';
					}else{
						html += ' >'+sitem.key+'</option> ';
					}
				}
			});
		}
		html += '</select></td><td style="text-align: center; vertical-align: middle;"> ';
		if(item.type === "0"){
			if(item.isdeductible == "1"){
				html += '<label class="checkbox-inline"><input name="selectedflag' + i + '" id="selectedflag"' + i + ' type="checkbox" onclick="changValue(this)"';
				if(item.flag == true){
					html += 'checked="true" value="0" />不计免赔</label>';
				}else{
					html += ' value="1"/>不计免赔</label>';
				}
			}
		}
		if("NewEquipmentIns" == item.riskkindcode){
			html += '<button class="btn btn-primary peopleDo" type="button" id="equipment' + selectedid +'" onclick=addnewequipment(); >新增设备</button>';
		}
		if("CompensationDuringRepairIns" == item.riskkindcode){
			html += '<input type="text" class="form-control w1" style="display:none;" placeholder="请输入天数" id="compensationdays" />';
		}
		html += '<input type="text" style="display:none;" id="selectedmaintype' + i + '" value="' + item.type + '"/>';
		html += '<input type="text" style="display:none;" id="selectedcode' + i + '" value="' + item.riskkindcode + '"/>';
		html += '<input type="text" style="display:none;" id="selectedunit' + i + '" value="' + item.insuredConfig.value[1].unit + '"/>';
		html += '<input type="text" style="display:none;" id="selectedtype' + i + '" value="' + item.insuredConfig.type + '"/></td></tr>';
	}); 
	html += '<tr><td style="text-align: center; vertical-align: middle;">备注</td><td colspan="5" style="text-align: center; vertical-align: middle;">';
	html += '<input type="text"  placeholder="请填写" value="" id="remark" class="form-control "></td></tr>';
	$("#insuredconfrtable").append(html);
}

function changValue(obj){
	var v = $(obj).val();
	if(v == "0"){
		$(obj).val("1");
	}else if(v == "1"){
		$(obj).val("0");
	}
}

function addnewequipment(){
	$('#addequipment').modal('show');
}

var equipmentnum = 0;

function addmsgtotable(){
	var name = "";
	var price = "";
	if($("#equipmentname").val() != "" && $("#equipmentprice").val() != ""){
		name = $("#equipmentname").val();
		price = $("#equipmentprice").val();
	}else{
		alert("设备名称或金额不能为空");
		return ;
	}
	var reg = new RegExp("^\\d+(\\.\\d+)?$"); 
	price = $("#equipmentprice").val();
	if(!reg.test(price)){  
        alert("金额输入有误，请重新输入!");
        return ;
    }  
	equipmentnum ++;
	var html = '<tr><td>' + name + '<input type="text" style="display:none;" id="equipmentaddname' 
	+ equipmentnum + '" value="' + name + '"/></td><td>' + price 
	+ '<input type="text" style="display:none;" id="equipmentaddprice' + equipmentnum + '" value="' + price + '"/></td><td><button type="button" class="btn btn-primary" onclick=deleterow(this);>删除</button></td></tr>';
	$("#allequipment").append(html);
	$("#equipmentname").val("");
	$("#equipmentprice").val("");
}

function deleterow(row){
	$(row).parent().parent().remove();
}

function getAllEquipment(){
	var equipmentInsBeans = new Array();
	for(var i=1;i<=equipmentnum;i++){
		var name = $("#equipmentaddname"+i).val();
		var price =  $("#equipmentaddprice"+i).val();
		var row = {"key" : name,"value" : price};
		equipmentInsBeans.push(row);
	}
	return equipmentInsBeans;
}


function deletetabletr(){
	var listtr = $("#insuredconfrtable").find("tr");
	if(listtr.length > 0){
		for(var i=1;i<listtr.length;i++){
			$(listtr[i]).remove();
		}
	}
}

function checkequipment(){
	if($("select[name=NewEquipmentIns]").length > 0){
		if($("select[name=NewEquipmentIns]").val() != 0){
			if($("#allequipment tr").length <= 1){
				alert("选择新增设备损失险时，必须输入新增设备");
				return 1;
			}
		}
	}
}

function checkCompensationDuringRepairIns(){
	var reg = new RegExp("^[0-9]*$"); 
	days = $("#compensationdays").val();
	if($("select[name=CompensationDuringRepairIns]").length > 0){
		if($("select[name=CompensationDuringRepairIns]").val() != 0){
			if($("#compensationdays").val() == ""){
				alert("选择修理期间费用补偿险时，天数不能为空");
				return 1;
			}
			if(!reg.test(days)){
				alert("请正确输入修理期间费用补偿险天数!");
				return 1;
			}
		}
	}
}

function GetInfoFromTable() {
	if(checkequipment() == 1 || checkCompensationDuringRepairIns() == 1){
		return 1;
	}
	var equipmentInsBeans = getAllEquipment();
	var plankey = $("#configtype_id").val();
	var processinstanceid = taskid;
	var businessRisks = new Array();
	var strongRisk = new Array();
	var length = $("#insuredconfrtable tr").length;
	
	for(var i = 0;i < length - 2 ; i ++){
		if($("#selectedOption"+i).val() == 0){
			continue;
		}
		var obj = new Object();
		if($("#selectedmaintype"+i).val() == 0){
			obj.name = $("#selectedtext"+i).val();
			obj.coverage = $("#selectedOption"+i).val();
			obj.selectedOption = $("#selectedOption"+ i + " option:selected").text();
			obj.code = $("#selectedcode"+i).val();
			obj.type = $("#selectedtype"+i).val();
			obj.unit = $("#selectedunit"+i).val();
			obj.flag = $('input[name="selectedflag' +i+'"]:checkbox').val();
			var risk = {"name" :obj.name, "coverage" :obj.coverage, "selectedOption" :obj.selectedOption,
			 "code" : obj.code, "type" :obj.type,  "unit" :obj.unit,
			 "flag" :obj.flag};
			businessRisks.push(risk);
		}else{
			obj.code = $("#selectedcode"+i).val();
			obj.name = $("#selectedtext"+i).val();
			obj.selected = $("#selectedOption"+ i + " option:selected").text();
			var risk = {"code" :obj.code, "name" :obj.name,"selected" :obj.selected};
			strongRisk.push(risk);
		}
	}
	var remark = $("#remark").val();
	var data = new Object();
	var compensationdays = 0;
	return {
			plankey : plankey,
			processinstanceid : processinstanceid,
			businessRisks : businessRisks,
			strongRisk : strongRisk,
			equipmentInsBeans : equipmentInsBeans,
			compensationdays : $("#compensationdays").val(),
			remark : remark
			}
}
