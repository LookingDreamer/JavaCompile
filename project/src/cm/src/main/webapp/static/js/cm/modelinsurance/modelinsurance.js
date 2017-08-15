require([ "jquery", "zTree", "zTreecheck", "flat-ui", "bootstrap-table", "bootstrapdatetimepicker","bootstrapdatetimepickeri18n",
		"bootstrap", "bootstrapTableZhCn", "public" ,"ajaxfileupload"], function($) {
	$("#table-carmodelbrand").bootstrapTable({
		method: 'get',
		url: "/cm/modelinsurance/searchcarmodel",
		cache: false,
		striped: true,
		pagination: true,
		sidePagination: 'server', 
		pageSize: 5,
//		minimumCountColumns: 2,
		queryParams : getQueryPara,
		clickToSelect: true,
		columns: [{
			field: 'standardname',
			title: '品牌型号',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: true
		},{
			field: 'displacement',
			title: '排气量',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: true
		},{
			field: 'seat',
			title: '核定载人数',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: true
		},{
			field: 'fullweight',
			title: '整备质量',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: true
		},{
			field: 'loads',
			title: '核定载质量',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: true
		},{
			field: 'listedyear',
			title: '年款',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: true
		},{
			field: 'taxPrice',
			title: '新车购置价',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: true
		},{
            field: 'operating',
            title: '选择',
            align: 'center',
            valign: 'middle',
            switchable:false,
            formatter: operateFormatter,
            events: operateEvents
        }]
	});
	
	$("#comeback").on("click",function(){
		location.href = "/cm/business/valetcatalogue/showaletcataloguelist";
	})
	$("#upload").on("click",function(){
		$.ajax({
			url:'needuploadimage',
			data:{
				processinstanceid:taskid// taskid,
			},
			type:'get',
			datatype:"json",
			success:function(data){
				var listCodetype=data.body;
				var opd="";
				for(var i=0;i<listCodetype.length;i++){
					opd =opd+'<option value="'+listCodetype[i].codetype+'" id="'+listCodetype[i].riskimgname+'">'+listCodetype[i].riskimgtypename+'</option>';
				}
				$("#fileType").append(opd);
			}
		})
	})
	
	$("#cheInfobutton_next").on("click",function(){
		if ($("#standardname1").val()=='') {
			alertmsg("品牌型号不能为空！")
			return;
		}
		if ($("#engineno_nub").val()=='') {
			alertmsg("发动机号不能为空！")
			return;
		}
		if ($("#vehicleframeno_nub").val()=='') {
			alertmsg("车辆识别号码不能为空！")
			return;
		}
		if ($("#testtimeendstr").val()=='') {
			alertmsg("车辆初登日期不能为空！")
			return;
		}
		$.ajax({
			url:'savecarinfo',
			data:{
				processinstanceid: taskid,
				modelCode: $("#standardname1").val(),
				vin: $("#vehicleframeno_nub").val(),
				engineNo: $("#engineno_nub").val(),
				chgOwnerFlag: $("#businesstype01").val(),
				chgOwnerDate: $("#testtimestr").val(),
				firstRegisterDate: $("#testtimeendstr").val(),
				webpagekey: "",
				drivinglicense: ""
			},
			type:'post',
			datatype:"json",
			success:function(data){
				if(issuccess(data.status)){
					$("#cheInfo").hide();
					$("#baseInfo").hide();
					$("#insureConfig").hide();
					$("#insureCom").show();
					$('#baseInfodiv').hide();
					$("#cheInfodiv").hide();
					$('#insureConfigdiv').hide();
					$("#cheInfo").removeClass();
					$("#insureCom").addClass("active");
					$("#insureComdiv").show();
					providerconfig();
				}else{
					alertmsg(data.body.message);
				}
			}
		})
	})
	
	$("#submitCarModel").on("click",function(){
		$.ajax({
			url:'carmodelinfo',
			data:{
				processinstanceid: taskid,
				modelCode: $("#standardname1").val(),
				displacement:$("#displacement").val(),
				approvedLoad:$("#seat").val(),
				rulePriceProvideType:$("#price").val(),
				institutionType:$("#belongquality").val(),
				useProperty:$("#carquality").val(),
				tonnage:$("#weight").val(),
				wholeWeight:$("#curbweight").val(),
				customPrice:$("#price1").val(),
				webpagekey:""
			},
			type:'post',
			datatype:"json",
			success:function(data){
				if(issuccess(data.status)){
					$("#standardname").val($("#standardname1").val());
				}else{
					alertmsg(data.body.message);
				}
			}
		})
	})
	//是否过户车
	changebusinesstype();
	
	$("#search").on("click",function(){
		$("#carmodelbrand").show();
		$("#carmoremodelbrand").hide();
		$.ajax({
			url : "searchcarmodel",
			type : 'GET',
			dataType : "json",
			data: {modelname:$("#searchinfo").val(),
					limit:5,
					offset:0},
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				$('#table-carmodelbrand').bootstrapTable('load', data);
			}
		});
	})
	
	//未上牌
	$("#registration").on("click",function(){
		var admin1=$("#registration").prop("checked");
		if (admin1) {
			$("#carNumber").val("");
   		    $('#carNumber').attr('disabled',true);
		}else{
			$('#carNumber').attr('disabled',false);
		}
	})
	
	
	$(function(){
		$("#updatestyle").on("click",function(){
			$("#updatediv").modal(); 
			zhiding();
		});
	})
	$("#cheInfo").hide();
	$("#insureCom").hide();
	$("#insureConfig").hide();
	$("#baseInfo").show();
	$("#cheInfodiv").hide();
	$('#insureComdiv').hide();
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
				flag:"0"
				},
			type:'post',
			datatype:"json",
			success:function(data){
				if(issuccess(data.status)){
					$("#baseInfo").hide();
					$("#insureCom").hide();
					$("#insureConfig").hide();
					$("#cheInfo").show();
					$('#baseInfodiv').hide();
					$('#insureComdiv').hide();
					$('#insureConfigdiv').hide();
					$("#baseInfo").removeClass();
					$("#cheInfo").addClass("active");
					$("#cheInfodiv").show();
					taskid = data.body.processinstanceid;
					//调用平台查询接口
					callplatformquery(taskid);
				}else{
					alertmsg(data.message);
				}
			}
		})
	});
	
	$("#callPlatform_query").on("click",function(){
		$.ajax({
			url:"getcarinfobycarnumber",
			type:"POST",
			datatype:"json",
			data:{
				carNumber:$("#carNumber").val()
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data.status=="success") {
					var info=data.body;
					$("#carowner_name").val(info.carowner.name);
					$("#vehicleframeno_nub").val(info.lastYearCarinfoBean.vehicleframeno);
					$("#engineno_nub").val(info.lastYearCarinfoBean.engineno);
					$("#testtimeendstr").val(info.lastYearCarinfoBean.registerdate);
					$("#lastsuppliername").val(info.lastYearSupplierBean.suppliername);
					if (info.lastYearCarinfoBean.chgownerflag=="1") {
						$("input[name='businesstype'][value=01]").attr("checked",true);
						changebusinesstype();
					}else{
						$("input[name='businesstype'][value=02]").attr("checked",true);
						changebusinesstype();
					}
				}else{ 
					alertmsg(data.message);
				}
			}
		})
	});
	
	$("#show1").on("click",function(){
		modalshow();
	})
	
	$("#upload").on("click",function(){
		$("#photosupload").modal('show');
	})
	
	$("#confirmpid").on("click",function(){
		callbackyes(piddata);
	})
	
	initcodetypes();
	
})

var pagesize = 5;
var taskid = "";
var piddata = new Object();

function photoupload(){
	var msg =  getFileMsg();
	if(	$("#" + msg.fileType).length > 0 ){
		alert("请勿重复上传相同类型图片！！！");
		return ;
	}
	$.ajaxFileUpload({
        url: "fileUpload?fileType="+msg.fileType+"&fileDescribes="+msg.fileDescribes+"&processinstanceid=0&taskid="+msg.taskId,
        secureuri: false, 
        fileElementId:"file",//提交文件的id值
		dataType : "json",
        success: function(data){
        	alert(data.status);
        	addPhoto2Html(msg.fileType,data.filepath,$('#fileType option:selected').attr("id"),data.fileid);
        },
        error: function(){
        	alert("Connection Error!")
        }
	})
}

function getFileMsg(){
	return {
		taskId:taskid,
		file:$("#file"),
		fileName:$("#fileName").val(),
		fileType:$("#fileType").val(),
		fileDescribes:$("#fileDescribes").val(),
		jobNum:$("#jobnum").val()
	}
}

function addPhoto2Html(fileType,filePath,fileName,id){
	var html = "<div class='col-xs-3' id='"+ fileType + 
	"'><div class='sel-b '>" + 
	" <img " +
	" src='" + filePath + "'class='img-responsive'></label> <span " +
	" class='dblock2 pad10 t-center'>" + fileName + "</span> " +
	" </div> " +
	" <div class='pad2 clearfix' id='" + id + "'> " +
	" <button class='btn btn-primary f-right' onclick='deletephoto(this)'>删除</button> " +
	" </div> " +
	"</div>";
	$("#allphotos").append(html);
	$("#file").val("");
	$("#fileName").val("");
	$("#fileType").val("");
	$("#fileDescribes").val("");
}

function getphotonum(){
	return $("#allphotos").find("img").length;
}

function deletephoto(p1){
	var id = $(p1).parent().attr("id");
	$.ajax({
		url:"deleteupdateimage",
		type:"GET",
		datatype:"json",
		data: {processinstanceid:taskid,
			fileid:id},
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			alert(data.status);
		}
	});
	$(p1).parent().parent().remove();
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
		url:"insurancescheme?processinstanceid="+taskid+"&plankey="+$("#configtype_id").val(),
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

function initcodetypes(){
	$.ajax({
		url:"initcodetypes?types=CertKinds",
		type:"GET",
		datatype:"json",
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$.each(data, function(j, item) {
				$("#IDtype").append("<option value=" + item.codeValue + ">" + item.codeName + "</option>");
			})
		}
	});
}

function providerconfig(){
	$.ajax({
		url:"getproviderinfo?processinstanceid="+taskid+"&agentid="+$("#agentid").val(),
		type:"GET",
		datatype:"json",
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			//先清空
			deleteprovidertabletr();
			createprovidertabletr(data.body);
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


function createprovidertabletr(object){
	var html = "";
	$.each(object, function(i, item) {
		html += '<tr><td style="text-align: center; vertical-align: middle;"><label class="checkbox-inline"> ';
		html += '<input type="checkbox" class="provider_check" name="items"  value="' + item.proId + '"/></label></td>';
		html += '<td>'+item.prvname+'</td>';
		html += '<td>'+item.branchProBeans[0].comname+'</td>';
		html += '<td style="text-align: center; vertical-align: middle;">';
		html += '<select class="form-control" id="select'+item.proId+'">';
		$.each(item.branchProBeans[0].singleSiteBeans, function(j, sitem) {
			html += '<option  value="'+item.branchProBeans[0].agreementid+'#'+item.branchProBeans[0].comcode+'#'+sitem.siteId+'#'+item.channeltype+'" ';
			if(item.selected == true){
				html += ' selected>'+sitem.siteName+'</option> ';
			}else{
				html += ' >'+sitem.siteName+'</option> ';
			}
		});
		html += '</select></td></tr>';
	}); 
	$("#checktable").append(html);
}
//function showbaseIndiv(val){
//	$('#baseInfodiv').hide();
//	$('#insureComdiv').hide();
//	$('#insureConfigdiv').hide();
//	$("#baseInfo").removeClass();
//	$("#cheInfo").addClass("active");
//	$("#cheInfodiv").show();
//}
function showcheInfoIndiv(val){
	if(val=="last"){
		$("#cheInfo").hide();
		$("#insureCom").hide();
		$("#insureConfig").hide();
		$("#baseInfo").show();
		$("#cheInfodiv").hide();
		$('#insureComdiv').hide();
		$('#insureConfigdiv').hide();
		$("#cheInfo").removeClass();
		$("#baseInfo").addClass("active");
		$("#baseInfodiv").show();
	}else{
//		$('#baseInfodiv').hide();
//		$("#cheInfodiv").hide();
//		$('#insureConfigdiv').hide();
//		$("#cheInfo").removeClass();
//		$("#insureCom").addClass("active");
//		$("#insureComdiv").show();
	}
}

function insureComshowdiv(val){
	if(val=="last"){
		$("#insureConfig").hide();
		$("#baseInfo").hide();
		$("#insureCom").hide();
		$("#cheInfo").show();
		$('#baseInfodiv').hide();
		$('#insureComdiv').hide();
		$('#insureConfigdiv').hide();
		$("#insureCom").removeClass();
		$("#cheInfo").addClass("active");
		$("#cheInfodiv").show();
	}else{	
		//保存保险公司
		var paramsList= "";
		var sum=0;
		 $(".provider_check").each(function(){
			 var isChecked = $(this)[0].checked;
			 if(isChecked == true){
				 paramsList += $("#select"+$(this).val()).find("option:selected").val()+",";
				 sum ++;
				}
	       })
	       if (sum==0) {
			alertmsg("请至少选择一个保险公司！");
			return;
		    }
	       paramsList = paramsList.substring(0,paramsList.length - 1);
	       console.log(paramsList);
	       $.ajax({
	   		url:'choiceproviderids',
	   		type:'post',
	   		datatype:"json",
	   		data:{processinstanceid:taskid,paramsList:paramsList},
	   		success:function(data){
	   			if(issuccess(data.status)){
//	   				alertmsg(data.status);
	   			}else{
	   				alertmsg(data.message);
	   			}
	   		}
	   	});
	        $("#cheInfo").hide();
			$("#baseInfo").hide();
			$("#insureCom").hide();
			$("#insureConfig").show();
			$('#baseInfodiv').hide();
			$("#cheInfodiv").hide();
			$('#insureComdiv').hide();
			$("#insureCom").removeClass();
			$("#insureConfig").addClass("active");
			$("#insureConfigdiv").show();
			showinsureconfig();
	}
}


function insureConfigshowdiv(val){
	if(val=="last"){
		$('#baseInfo').hide();
		$("#cheInfo").hide();
		$('#insureConfig').hide();
		$("#insureCom").show();
		$('#baseInfodiv').hide();
		$("#cheInfodiv").hide();
		$('#insureConfigdiv').hide();
		$("#insureConfig").removeClass();
		$("#insureCom").addClass("active");
		$("#insureComdiv").show();
	}else{
		var conf = GetInfoFromTable();
		if(getphotonum()<1){
			alert("请上传至少一张图片！");
			return;
		}
		if(conf == 1){
			return ;
		}
		$.ajax({
	   		url:'insuredconfig',
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
}

function verificationinsuredconfig(conf){
	$.ajax({
		url:'verificationinsuredconfig',
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
			var m = value.prvshotname + "[" + value.reason + "]" + "系统将在报价中自动去掉该保险公司。请确认！\r\n";
			message += m;
		}); 
	}
	$.each(data.body.configBeans, function(key, value) { 
		var m = "以下公司不支持" + value.kindname + "。系统将在这些公司的报价中自动去掉该险种。请确认！\r\n";
		message += m;
		$.each(value.providers, function(n, msg) { 
			message += msg.prvshotname + "\r\n";
		})
	}); 
	piddata = data;
	if(message != ""){
		$("#content").text(message);
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
			url:'workflowstartquote',
			type:'post',
			datatype:"json",
			contentType: "application/json; charset=utf-8",
			data:JSON.stringify(
					{processinstanceid:taskid,
					pids:paramsList
					}),
			success:function(data){
				if(issuccess(data.status)){
					location.href="/cm/multiplelist/multiplelist?taskid=" + taskid;
				}else{
					alertmsg(data.message);
				}
			}
		})
}

function showdiv(shortdivname) {
	$('#baseInfodiv').hide();
	$('#cheInfodiv').hide();
	$('#insureComdiv').hide();
	$('#insureConfigdiv').hide();
	$("#baseInfo").removeClass();
	$("#cheInfo").removeClass();
	$("#insureCom").removeClass();
	$("#insureConfig").removeClass();
    $("#" + shortdivname).addClass("active");
	$("#" + shortdivname + "div").show();
}


function issuccess(status){
	return $.trim(status) == "success";
}

var perm = 0;

function callplatformquery(taskid){
	$.ajax({
		url:'callplatformquery',
		data:{agentid:$("#agentid").val(),
			plateNumber:$("#carNumber").val(),
			provinceCode:$("#provinceCode").val(),
			cityCode:$("#cityCode").val(),
			flag:"0",
			agentName:$("#agentname").val(),
			processinstanceid:taskid
			},
		type:'post',
		datatype:"json",
		success:function(data){
			if(issuccess(data.status)){
				var queryflag = data.body.resultMap.queryflag;
				//页面值回填
				if("1" == queryflag){
					perm ++;
					if(perm <= 10){
						setTimeout(function () { 
							callplatformquery(taskid);
						}, 5000);
					}
				}else if("0" == queryflag){
					lastyeardatabackfill(data.body.resultMap);
				}
				var type = data.body.resultMap.insureinfobean.carowerinfo.certificateType;
				$("#IDtype").val(type);
				$("#IDnumber").val(data.body.resultMap.insureinfobean.carowerinfo.certNumber);
				//下一步
				showbaseIndiv('next')
			}
		}
	})
}
//为true是，obj为null或者undefined
function isexecit(obj){
	return obj== undefined;
}

function lastyeardatabackfill(json){
	//车辆信息回填
	if(!isexecit(json.carinfobean)){
		$("#engineno_nub").val(json.carinfobean.engineNo);
		$("#vehicleframeno_nub").val(json.carinfobean.vin);
		$("#testtimeendstr").val(json.carinfobean.firstRegisterDate);
		$("#parentname").val(json.carinfobean.modelCode);
	}
	//车型信息回填
	//多个个车型或者没有
	if(json.carmodel == false){
		//多个车型
		if(!isexecit(json.carmodelbean)){
			$("#updatediv").modal();
			zhiding();
			$("#carmodelbrand").hide();
			$("#carmoremodelbrand").show();
			$('#table-carmoremodelbrand').bootstrapTable({
				cache: false,
		        striped: true,
		        pagination: true,
		        sidePagination: 'cliect', 
		        pageSize: 5,
		        minimumCountColumns: 2,
		        clickToSelect: true,
			    columns: [{
	                field: 'brandname',
	                title: '系列名称',
	                visible: false
	            },{
			        field: 'standardname',
			        title: '品牌型号'
			    }, {
			        field: 'displacement',
			        title: '排气量'
			    }, {
			        field: 'seat',
			        title: '核定载人数'
			    },{
			        field: 'fullweight',
			        title: '整备质量'
			    }, {
			        field: 'loads',
			        title: '核定载质量'
			    }, {
			        field: 'listedyear',
			        title: '年款'
			    }, {
			        field: 'taxPrice',
			        title: '新车购置价'
			    }, {
	                field: 'operating',
	                title: '选择',
	                align: 'center',
	                valign: 'middle',
	                switchable:false,
	                formatter: operateFormatter,
	                events: operateEvents
	            }],
			    data:json.carmodelbean
			});
			$(".fixed-table-loading").hide();
		}
	}else{
		//肯定只有一个
		$("#standardname").val(json.carmodelbean.modelCode);
		$("#standardname1").val(json.carmodelbean.modelCode);
		$("#displacement").val(json.carmodelbean.displacement);
		$("#seat").val(json.carmodelbean.approvedLoad);
		$("#price").val(json.carmodelbean.rulePriceProvideType);
		$("#belongquality").val(json.carmodelbean.institutionType);
		$("#carquality").val(json.carmodelbean.useProperty);
		$("#weight").val(json.carmodelbean.tonnage);
		$("#curbweight").val(json.carmodelbean.wholeWeight);
	}
}

//获得选中行的id列表
function getSelectedRows() {
    var data = $('#table-javascript').bootstrapTable('getSelections');
    if(data.length == 0){
    	alertmsg("至少选择一行数据！");
    }else{
    	var arrayuserid = new Array();
    	for(var i=0;i<data.length;i++){
    		arrayuserid.push(data[i].id)
    	}
    	return arrayuserid;
    }
}

//添加事件
function operateFormatter(value, row, index) {
    return [
		'<button id="selectcarmodel" class="btn btn-default" name="selectcarmodel">选择</button>'
    ].join('');
}

//事件相应
window.operateEvents = {
    'click #selectcarmodel': function (e, value, row, index) {
    	row.processinstanceid = taskid;
    	$.ajax({
			url:'selectcarmodel',
			data:row,
			type:'post',
			datatype:"json",
			success:function(data){
				if(issuccess(data.status)){
					$("#standardname1").val(row.standardname);
					$("#displacement").val(row.displacement);
					$("#seat").val(row.seat);
					$("#weight").val(row.loads);
					$("#curbweight").val(row.fullweight);
				}else{
					alertmsg(data.message);
				}
			}
		});	
    }
}

function showinsureconfig(){
	showdiv('insureConfig');
	$.ajax({
		url:'schemelist?processinstanceid=' + taskid,//+taskid,
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

function changValue(obj){
	var v = $(obj).val();
	if(v == "0"){
		$(obj).val("1");
	}else if(v == "1"){
		$(obj).val("0");
	}
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
			remark : remark,
			certNumber : $("#IDnumber").val() ,
			certificateType : $("#IDtype").val()
			}
}
function zhiding(){
	var value=$("#price").val();
	if (value=="3") {
		$("#carshow").show();
	} else{
		$("#carshow").hide();
	}
}
function changebusinesstype(){
	var value=$('input[name="businesstype"]:checked').val();
	if (value=="01") {
		$('#testtimestr').val("");
		$('#testtimestr').attr('disabled',false);
	} else{
		$('#testtimestr').val("");
		$('#testtimestr').attr('disabled',true);
	}
} 

function insertTitle(tValue){  
	   var t1 = tValue.lastIndexOf("\\");  
	   if(t1 >= 0 && t1 < tValue.length){  
		   $("#fileName").val(tValue.substring(t1 + 1));  
	   }  
	}  