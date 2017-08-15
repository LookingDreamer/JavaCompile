require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","zTree","zTreecheck","public"], function ($) {
	//数据初始化 
	$(function() {
		
		
		$('#table-javascript').bootstrapTable({
            method: 'get',
            //url: "initcarlist",
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server', 
            pageSize: 10,
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            }, {
	           	 field: 'rownum',
	             title: '序号',
	             align: 'center',
	             valign: 'middle',
            }, {
                field: 'id',
                title: 'id',
                visible: false,
                switchable:true
            },  {
                field: 'carlicenseno',
                title: '车牌号码',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'vincode',
                title: '车辆识别代号',
                align: 'center',
                valign: 'middle',
                sortable: true
            },  {
                field: 'ownername',
                title: '客户姓名',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'idcardno',
                title: '发动机号',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'phonenumber',
                title: '手机号码',
                align: 'center',
                valign: 'middle',
                clickToSelect: true,
                sortable: true
            }]
        });
		
		// 用户列表页面【查询】按钮
		$("#querybutton").on("click", function(e) {
			
			var ownername= $("#ownername").val();
			var vincode= $("#vincode").val();
			var carlicenseno= $("#carlicenseno").val();
			var phonenumber= $("#phonenumber").val();
			var engineno= $("#engineno").val();
			var policyno = $("#policyno").val();
			var tCount = 0;
			if (ownername.length > 0) {
				tCount++;
			}
			if (vincode.length > 0) {
				tCount++;
			}
			if (carlicenseno.length > 0) {
				tCount++;
			}
			if (phonenumber.length > 0) {
				tCount++;
			}
			if (engineno.length > 0) {
				tCount++;
			}
			if (policyno.length > 0) {
				tCount++;
			}
			if (tCount == 0) {
				alertmsg("请至少输入一个条件");
				return;
			}
			$('#table-javascript').bootstrapTable(
					'refresh',
					{url:'initcarlist?limit=10&ownername='+ownername
						+'&vincode='+vincode+'&carlicenseno='+carlicenseno+'&policyno='+policyno
						+'&phonenumber='+phonenumber+'&engineno='+engineno});
		 });
		
		// 查询条件【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#ownername").val("");
			$("#policyno").val("");
			$("#vincode").val("");
			$("#phonenumber").val("");
			$("#carlicenseno").val("");
			$("#engineno").val("");
		});
		
		//卡片列表
		$("#toggle").on("click", function(e) {
			$('#table-javascript').bootstrapTable('toggleView');
		});
		//刷新
		$("#refresh").on("click", function(e) {
			$('#table-javascript').bootstrapTable('refresh');
		});
		//车辆明细 按钮
		$("#carinfobtn").on("click",function(){
			var agent_id = $('#table-javascript').bootstrapTable('getSelections');
			if(agent_id.length!=1){
				alertmsg("请选择一条数据进行查看");
				return false;
			}
			var id= agent_id[0].id;
			window.location.href="carinfobtn?id="+id;
		})
	});
});



