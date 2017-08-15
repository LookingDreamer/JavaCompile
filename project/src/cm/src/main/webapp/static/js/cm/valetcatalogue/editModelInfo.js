
function initEditCarInfoScript() {
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
      pickerPosition: "bottom-right"
     
    });
	//通过是否过户车判断要不要显示过户时间
	$("#isTransfercar").change(function(){
		var isFlag = $.trim($("#isTransfercar").val());//获得值
		if(isFlag == "1"){//"是"的情况下
			$(".transferdate").removeAttr("disabled");
		}else if(isFlag == "0"){//"否"的情况下
			$(".transferdate").attr("disabled","true");
		}
		$("#transferdateBox").toggle(); 
	})
	//是否指定车价
	$(".isPolicycarprice").click(function() {
		var flag = $("#thespecifiedprice").prop("checked");
		if(flag){//将disable的移除
			$("#policycarprice").removeAttr("disabled");
		}else{
			$("#policycarprice").attr("disabled","disabled");
		}
		
	});
	//指定购置价
	$("#specifiedpurchaseprice").change(function(){
		var flag = $("#specifiedpurchaseprice").prop("checked");
		if(flag){//将readonly的移除
			$("#purchaseprice").removeAttr("readonly").css("color", "#555555");
		}else{
			$("#purchaseprice").attr("readonly","readonly");
		}
	});
	//数据校验方法
	function isEmpty(objVal,Str){
		if(objVal && $.trim(objVal)){
			return false;
		}else{
			alertmsg(Str+"不能为空！");
			return true;
		}
	}
	function isNotNum(objVal,Str){
		if(isNaN(objVal)){
			alertmsg(Str+"应为数字格式！");
			return true;
		}else{
			return false;
		}
	}
	
	//数据回写方法
	function dataBack(inscomcode){
		var win;
		if($("#desktop:not(:hidden)").length==0){
			win = $(window.frames[$("iframe[id^=fra_orderlistorderlist]:not(:hidden)").attr("id")].document);
		}else{
			win = $(window.desktop_content.document);
		}
		win.find("#standardnamePageInfo1"+inscomcode).text($("#standardname").val());//品牌型号
		win.find("#enginenoPageInfo"+inscomcode).text($("#engineno").val());//发动机号
		win.find("#vincodePageInfo"+inscomcode).text($("#vincode").val());//车辆识别代码
		win.find("#registdatePageInfo"+inscomcode).text($("#registdate").val().substring(0,11));//车辆初登日期
		win.find("#isTransfercarPageInfo"+inscomcode).text(($("#isTransfercar").val())=="1"?"是":"否");//是否过户车
//		win.find("#mileagevaluePageInfo"+inscomcode).text($("#MILEAGEVALUE_"+$("#mileage").val()).text());//平均行驶里程
		win.find("#drivingareavaluePageInfo"+inscomcode).text($("#DRIVINGAREAVALUE_"+$("#drivingarea").val()).text());//行驶区域
//	    alert('haoyunlai');
	}
	
	// 保存车辆修改信息
	$("#makesure").on("click",function(){
		var engineno = $("#engineno").val();
		var vincode = $("#vincode").val();
		var registdate = $("#registdate").val();
		var isTransfercar = $("#isTransfercar").val();
		var transferdate = $("#transferdate").val();
		if(isEmpty(engineno,"发动机号")){
			return;
		}
		if(isEmpty(vincode,"车辆识别代号")){
			return;
		}
		if(isEmpty(registdate,"车辆初登日期")){
			return;
		}
		if(isTransfercar == "1"){
			if(isEmpty(transferdate,"过户时间")){
				return;
			}
		}
		var standardname = $("#standardname").val();
		var seat = $("#seat").val();
		var unwrtweight = $("#unwrtweight").val();
		var displacement = $("#displacement").val();
		var fullweight = $("#fullweight").val();
		var thespecifiedprice = $("#thespecifiedprice").prop("checked");
		var listedyear = $("#listedyear").val();
		var purchaseprice = $("#purchaseprice").val();
		if(isEmpty(standardname,"品牌型号")){
			return;
		}
		if(isEmpty(seat,"核定载人数")){
			return;
		}else{
			if(isNotNum(seat,"核定载人数")){
				return;
			}
		}
		if(isEmpty(unwrtweight,"核定载质量")){
			return;
		}else{
			if(isNotNum(unwrtweight,"核定载质量")){
				return;
			}
		}
		if(isEmpty(displacement,"排气量")){
			return;
		}else{
			if(isNotNum(displacement,"排气量")){
				return;
			}
		}
		if(isEmpty(fullweight,"整备质量")){
			return;
		}else{
			if(isNotNum(fullweight,"整备质量")){
				return;
			}
		}
		if(thespecifiedprice){
			var policycarprice = $("#policycarprice").val();
			if(isEmpty(policycarprice,"指定车价")){
				return;
			}else{
				if(isNotNum(policycarprice,"指定车价")){
					return;
				}
			}
		}
		if(isEmpty(listedyear,"年款")){
			return;
		}
		if(isEmpty(purchaseprice,"新车购置价")){
			return;
		}else{
			if(isNotNum(purchaseprice,"新车购置价")){
				return;
			}
		}
		$.ajax({
			url : 'common/insurancepolicyinfo/savecarinfo',
			type : 'POST',
			data : $("#editCarinfo").serialize(),
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data == "success") {
//					alertmsg("修改车辆信息成功！");
					//更新原页面指定选项卡内信息
//					alert("I'm back!")
					dataBack($("#inscomcode").val());
					$('#xDialog').modal('hide');
//					location.href="/cm/multiplelist/multiplelist?taskid=18968";
				}else{
					alertmsg("修改失败！请稍后重试！");
				}
			}
		});
	});
	//隐藏或显示重选车型模块
	$("#showSelectCarModel").click(function(){
		$("#noSelectCarModel").toggle();
		$("#selectCarModel").toggle();
	});
	$("#selectCarModelclose").click(function(){
		$("#noSelectCarModel").toggle();
		$("#selectCarModel").toggle();
	});
	//Ajax获取所有车型信息
	//默认一页显示十条记录
	var pagesize = 5;
	//当前调用的url
	var pageurl = "/cm/common/insurancepolicyinfo/showCarModelInfo";
	//增加所搜条件重新加载车型数据
	$("#carModelQueryButton").click(function(){
		var paramStr = "";
		var standardfullname = $("#standardfullnameTxt").val();
		if(standardfullname && $.trim(standardfullname)){
			paramStr += "?standardfullname=" + $.trim(standardfullname);
		}
		$('#table-carModelInfo').bootstrapTable('refresh',{url:pageurl+paramStr});
	});
	$("#showSelectCarModel").click(function(){
		$('#table-carModelInfo').bootstrapTable({
            method: 'get',
            url: pageurl,
            cache: false,
            striped: true,
//            pagination: true,
            sidePagination: 'server', 
            pageSize: pagesize,
            minimumCountColumns: 2,
            clickToSelect: true,
            singleSelect: true,
            onClickRow: function (row) {
                	$UpdateCarModelInfo(row);
            	},
            columns: [
//                      {
//                field: 'state',
//                align: 'center',
//                valign: 'middle',
//                checkbox: true
//            }, 
            {
                field: 'id',
                title: '车型id',
                visible: false,
                switchable:false
            }, {
                field: 'standardfullname',
                title: '品牌型号',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'displacement',
                title: '排气量',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'seat',
                title: '核定载人数',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'fullweight',
                title: '整备质量',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'unwrtweight',
                title: '核定载质量',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'listedyear',
                title: '年款',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'purchaseprice',
                title: '新车购置价',
                align: 'center',
                valign: 'middle',
                sortable: true
            }]
        });
	});
	
	//车辆属性匹配
	$("#property").change(function(){
		var selected = $(this).children('option:selected').val();
		//alertmsg($(this).children('option:selected').val());
		
		if(selected==1){
			//alert(1);
			$("#carproperty option[value=1]").remove();
			$("#carproperty option[value=11]").remove();
			$("#carproperty option[value=10]").remove();
			$("#carproperty").append("<option value=10>非营业企业</option>");
			$("#carproperty").append("<option value=11>非营业机关</option>");
//			$("#carproperty").empty();
//			$("#carproperty").append("<option value=11>非营业机关</option>");
		}else if(selected==2){
			//alert(2);
			$("#carproperty option[value=1]").remove();
			$("#carproperty option[value=11]").remove();
			$("#carproperty option[value=10]").remove();
			$("#carproperty").append("<option value=10>非营业企业</option>");
			$("#carproperty").append("<option value=11>非营业机关</option>");
		}else if(selected==0){
			//alert(0);
			$("#carproperty").append("<option value=1>家庭自用</option>");
			$("#carproperty option[value=11]").remove();
			$("#carproperty option[value=10]").remove();
		}
		
		//---------------这是调用接口，可能会用到--------------------
		/*
		$.ajax({
			url : "/cm/carmanager/getCarPropertyEx",
			type : 'GET',
			dataType : "json",
			data : {
				carproperty:selected
			},
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				$("#carproperty").empty();
				for (var i = 0; i < data.length; i++) {
					$("#carproperty").append("<option value='"+data[i].codevalue+"'>"+data[i].codename+"</option>");   
				}
				if(data.length>0){
					$("#carproperty").get(0).selectedIndex=0;
				}
				$("#carproperty").trigger("change"); 
			}
		});
		*/
		
	});
	
	$("#carproperty").click(function(){
		var select1 = $("#property").children('option:selected').val();
		//alert("select1"+select1);
		if(select1==0){
			$("#carproperty option[value=11]").remove();
			$("#carproperty option[value=10]").remove();
		}
	});
	
	//设置单击重选车型表格记录更新表中对应信息
	$UpdateCarModelInfo = function(row){
		$('#standardname').val(row.standardfullname);
		$('#displacement').val(row.displacement);
		$('#seat').val(row.seat);
		$('#fullweight').val(row.fullweight);
		$('#unwrtweight').val(row.unwrtweight);
		$('#listedyear').val(row.listedyear);
		$('#isstandardcar').val(row.isstandardcar);
		$("input[name=carprice]:eq(0)").attr("checked",'checked');
		$('#policycarprice').val("").attr("readonly","readonly");
		$('#purchaseprice').val(row.purchaseprice.split("-")[0]);
	}
	
}

