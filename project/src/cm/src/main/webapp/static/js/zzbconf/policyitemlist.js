require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","zTree","zTreecheck","public"], function ($) {
	//数据初始化 
	$(function() {
		$('#table-javascript').bootstrapTable({
            method: 'get',
            url: pageurl,
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server', 
            pageSize: pagesize,
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
            	 field: 'applicantname',
                 title: '投保人',
                 align: 'center',
                 valign: 'middle',
                 sortable: true
            },{
                field: 'insuredname',
                title: '被保人',
                align: 'center',
                valign: 'middle',
                sortable: true
            },{
            	field: 'carownername',
            	title: '车主',
            	align: 'center',
            	valign: 'middle',
            	sortable: true
            },{
            	field: 'policyno',
            	title: '保单号',
            	align: 'center',
            	valign: 'middle',
            	sortable: true
            }, {
                field: 'risktype',
                title: '险种',
                align: 'center',
                valign: 'middle',
                sortable: true
            }]
        });
//		返回
		$("#backbutton").on("click",function(e){
			history.go(-1);
		})
	});

});
//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "policcyitembyagentid";