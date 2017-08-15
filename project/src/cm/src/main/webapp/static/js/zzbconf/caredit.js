require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "bootstrapdatetimepicker","bootstrapdatetimepickeri18n","jqcookie", "jqtreeview","zTree","zTreecheck","zTreeexedit"], function ($) {
	//数据初始化 
	$(function() {
		
		$("#go_back").on("click",function(){
			window.location.href="list";
		});
		
		//车辆明细 车辆相关保单table
		$('#carinfo-list').bootstrapTable({
            method: 'get',
            url: queryUrl(),
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server', 
            pageSize: pagesize,
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            }, {
                field: 'id',
                title: '保单id',
                visible: false,
                switchable:false
            }, {
                field: 'policyno',
                title: '保单号码',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'orderid',
                title: '订单号码',
                align: 'center',
                valign: 'middle',
                sortable: true
            },  {
				field: 'agentnum',
				title: '代理人工号',
				align: 'center',
				valign: 'middle',
				sortable: false
			},  {
				field: 'agentname',
				title: '代理人姓名',
				align: 'center',
				valign: 'middle',
				sortable: false
			}, {
                field: 'carlicenseno',
                title: '车牌号码',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'applicantname',
                title: '客户姓名',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'phonenumber',
                title: '手机号码',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'prvshotname',
                title: '供应商',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'deptname',
                title: '所属机构',
                align: 'center',
                valign: 'middle',
                clickToSelect: true,
                sortable: true
            }, {
                field: 'policystatus',
                title: '保单状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'startdate',
                title: '起保日期',
                align: 'center',
                valign: 'middle',
                sortable: true
            }]
        });
		
	});
	
});
var pagesize = 10;

function queryUrl(){
	var queryurl = 'carinfopolicy?limit=10&policyid='+''
    +'&orderid='+''+'&carnum='+$("#carlicenseno").val()
    +'&custname='+''+'&deptid='+''
    +'&providerid='+''+'&policystatus='+''
    +'&agentnum='+''+'&agentname='+''
    +'&startdate='+''+'&enddate='+'';
	
	return queryurl;
}




