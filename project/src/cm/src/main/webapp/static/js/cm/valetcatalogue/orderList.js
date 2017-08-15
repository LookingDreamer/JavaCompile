require([ "jquery", "zTree", "zTreecheck", "flat-ui", "bootstrap-table", "bootstrapdatetimepicker","bootstrapdatetimepickeri18n",
		"bootstrap", "bootstrapTableZhCn","date","iscroll" ], function($) {
	$("#table-orderlist0").bootstrapTable({
		method: 'get',
		url: "/cm/orderlist/initOrderList",
		cache: false,
		striped: true,
		pagination: true,
		sidePagination: 'server', 
		pageSize: 10,
//minimumCountColumns: 2,
		queryParams : getQueryPara0,
		clickToSelect: true,
		columns: [{
			field: 'platenumber',
			title: '车牌',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: false
		},{
			field: 'name',
			title: '被保人',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: false
		},{
			field: 'prvshotname',
			title: '报价公司',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: false
		},{
			field: 'createtime',
			title: '时间',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: false
		},{
			field: 'taskid',
			title: '订单跟踪号',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: false
		},{
            field: 'operating',
            title: '操作',
            align: 'center',
            valign: 'middle',
            switchable:false,
            formatter: operateFormatter,
            events: operateEvents
        }]
	})
	
	$("#table-orderlist1").bootstrapTable({
		method: 'get',
		url: "/cm/orderlist/initOrderList",
		cache: false,
		striped: true,
		pagination: true,
		sidePagination: 'server', 
		pageSize: 10,
//		minimumCountColumns: 2,
		queryParams : getQueryPara1,
		clickToSelect: true,
		columns: [{
			field: 'platenumber',
			title: '车牌',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: false
		},{
			field: 'name',
			title: '被保人',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: false
		},{
			field: 'prvshotname',
			title: '报价公司',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: false
		},{
			field: 'createtime',
			title: '时间',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: false
		},{
			field: 'taskid',
			title: '订单跟踪号',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: false
		},{
            field: 'operating',
            title: '操作',
            align: 'center',
            valign: 'middle',
            switchable:false,
            formatter: operateFormatter,
            events: operateEvents
        }]
	})
	
	$("#table-orderlist2").bootstrapTable({
		method: 'get',
		url: "/cm/orderlist/initOrderList",
		cache: false,
		striped: true,
		pagination: true,
		sidePagination: 'server', 
		pageSize: 10,
//		minimumCountColumns: 2,
		queryParams : getQueryPara2,
		clickToSelect: true,
		columns: [{
			field: 'platenumber',
			title: '车牌',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: false
		},{
			field: 'name',
			title: '被保人',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: false
		},{
			field: 'prvshotname',
			title: '报价公司',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: false
		},{
			field: 'createtime',
			title: '时间',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: false
		},{
			field: 'taskid',
			title: '订单跟踪号',
			align: 'center',
			valign: 'middle',
			clickToSelect: true,
			sortable: false
		},{
            field: 'operating',
            title: '操作',
            align: 'center',
            valign: 'middle',
            switchable:false,
            formatter: operateFormatter,
            events: operateEvents
        }]
	})
	
	$("#querybutton").on("click",function(e) {
		var orderstatus = $("#orderstatus").val();
		var fromtime = $("#fromtime").val();
		var totime = $("#totime").val();
		var platenumber = $("#platenumber").val();
		var name = $("#name").val();
		var li = 0;
		var divid='';
		var outdiv1='';
		var outdiv2='';
		if(orderstatus == "0"){
			li = 2;
			divid='#all';
			outdiv1='#PolicyNum';
			outdiv2='#PaymentOrderNum';
		}else if(orderstatus == "1"){
			li = 0;
			divid='#PolicyNum';
			outdiv1='#all';
			outdiv2='#PaymentOrderNum';
		}else {
			li = 1;
			divid='#PaymentOrderNum';
			outdiv1='#all';
			outdiv2='#PolicyNum';
		}
		$('ul#mytab li').each(function(index){
			if(li==index){
				$(this).addClass("active");
				$(divid).addClass("active in");
			}else{
				$(this).removeClass("active");
				$(outdiv1).removeClass("active in");
				$(outdiv2).removeClass("active in");
			}
		});
		$.ajax({
			url : "searchOrderList",
			type : 'GET',
			dataType : "json",
			data: {orderstatus:orderstatus,
					from:fromtime,
					to:totime,
					platenumber:platenumber,
					name:name,
					offset:0,
					limit:10
					},
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				if(orderstatus == "0"){
					$('#table-orderlist0').bootstrapTable('load', data);
				}else if(orderstatus == "1"){
					$('#table-orderlist1').bootstrapTable('load', data);
				}else {
					$('#table-orderlist2').bootstrapTable('load', data);
				}
			}
		});
	})
})

function operateFormatter(value, row, index) {
    return [
		'<button id="selectorder" class="btn btn-default" name="selectorder">选择</button>'
    ].join('');
}

window.operateEvents = {
	    'click #selectorder': function (e, value, row, index) {
	    	location.href="/cm/multiplelist/multiplelist?taskid=" + row.taskid;
	    }
	}

function getQueryPara0(params){
	orderListVo = new Object();
	return {
		offset:params.offset,
	    limit:params.limit,
	    orderstatus:"0"
	}
}

function getQueryPara1(params){
	return {
		offset:params.offset,
		limit:params.limit,
		orderstatus:"1"
	}
}

function getQueryPara2(params){
	return {
		offset:params.offset,
		limit:params.limit,
		orderstatus:"2"
	}
}