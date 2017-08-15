//页面请求函数
function initEditCarModelInfoScript(){
	//Ajax获取所有车型信息
	//默认一页显示十条记录
	var pagesize = 5;
	//当前调用的url,查询型号相似的车辆信息
	var familyname = $.trim($("#familyname").val());
	var pageurl = "/cm/common/insurancepolicyinfo/showCarModelInfo?standardfullname="+familyname;
//	$('#carmodelinfotable').bootstrapTable({
//        method: 'get',
//        url: pageurl,
//        cache: false,
//        striped: true,
////        pagination: true,
//        sidePagination: 'server', 
//        pageSize: pagesize,
//        minimumCountColumns: 2,
//        clickToSelect: true,
//        singleSelect: true,
//        onClickRow: function (row) {
//            	$UpdateCarModelInfo(row);
//        	},
//        columns: [
////                  {
////            field: 'state',
////            align: 'center',
////            valign: 'middle',
////            checkbox: true
////        }, 
//        {
//            field: 'id',
//            title: '车型id',
//            visible: false,
//            switchable:false
//        }, {
//            field: 'standardfullname',
//            title: '品牌型号',
//            align: 'center',
//            valign: 'middle',
//            sortable: true
//        }, {
//            field: 'displacement',
//            title: '排气量',
//            align: 'center',
//            valign: 'middle',
//            sortable: true
//        }, {
//            field: 'seat',
//            title: '核定载人数',
//            align: 'center',
//            valign: 'middle',
//            sortable: true
//        }, {
//            field: 'fullweight',
//            title: '整备质量',
//            align: 'center',
//            valign: 'middle',
//            sortable: true
//        }, {
//            field: 'unwrtweight',
//            title: '核定载质量',
//            align: 'center',
//            valign: 'middle',
//            sortable: true
//        }, {
//            field: 'listedyear',
//            title: '年款',
//            align: 'center',
//            valign: 'middle',
//            sortable: true
//        }, {
//            field: 'purchaseprice',
//            title: '新车购置价',
//            align: 'center',
//            valign: 'middle',
//            sortable: true
//        }]
//    });
	//设置单击重选车型表格记录更新表中对应信息
	$UpdateCarModelInfo = function(row){
		UpdateItem("standardfullname",row.standardfullname);
		$('#standardname').val(row.standardname);
		$('#familyname').val(row.familyname);
		$('#brandname').val(row.brandname);
		$('#taxprice').val(row.taxprice);
		$('#analogyprice').val(row.analogyprice);
		$('#analogytaxprice').val(row.analogytaxprice);
		$('#aliasname').val(row.aliasname);
		$('#gearbox').val(row.gearbox);
		$('#loads').val(row.loads);
		UpdateItem("seat",row.seat);
		UpdateItem("unwrtweight",row.unwrtweight);
		UpdateItem("displacement",row.displacement);
		UpdateItem("fullweight",row.fullweight);
		UpdateItem("listedyear",row.listedyear);
		UpdateItem("cardeploy",row.standardfullname+","+row.gearBox+",排量"+row.displacement);
		UpdateItem("price",row.price);
	}
	UpdateItem = function(itemId,newValue){
		$('#'+itemId).val(newValue);
		$('#'+itemId+'TXT').text(newValue);
	}
	
	//数据回写方法
	function dataBack(num){
		var win;
		if($("#desktop:not(:hidden)").length>0){
			win = $(window.desktop_content.document);
		}else{
			win = $(window.frames[$("iframe[id^=fra_businessmytaskqueryTask]:not(:hidden)").attr("id")].document);
		}
		win.find("#standardnamePageInfo"+num).text($("#standardfullname").val());//车型名称
	}
	
	// 保存车型修改信息
	$("#makesure").on("click",function(){
		$.ajax({
			url : 'common/insurancepolicyinfo/updatecarmodelinfo',
			type : 'POST',
			data : $("#editCarModelinfo").serialize(),
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if (data == "success") {
//					alertmsg("修改车型信息成功！");
					//更新原页面指定选项卡内信息
					dataBack($("#num").val());
					$('#xDialog').modal('hide');
				}else{
					alertmsg("修改失败！请稍后重试！");
				}
			}
		});
	});
}
