require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "jqcookie", "jqtreeview","zTree","zTreecheck","public"], function ($) {
	//数据初始化 
	$(function() {
		$("#resetbutton").on("click", function(e) {
			$("#param3").val("");
			$("select option[value='0']" ).attr("selected",true);
			$("#dept2").children('option').remove();
			$("#dept3").children('option').remove();
			$("#dept4").children('option').remove();
			$("#dept5").children('option').remove();
		});
		
		//功能包查询
		$("#rulebase_query").on("click",function(){
			var ruletype = $("#ruletype").find("option:selected").val();
			var param2 =$("#param2").find("option:selected").val();
			var rulebaseStatus =$("#rulebaseStatus").find("option:selected").val();
			var dept1 =$("#dept1").find("option:selected").val();
			var dept2 =$("#dept2").find("option:selected").val();
			var dept3 =$("#dept3").find("option:selected").val();
			var dept4 =$("#dept4").find("option:selected").val();
			var dept5 =$("#dept5").find("option:selected").val();
			
			if(undefined==dept2){
				dept2=0;
			}
			if(undefined==dept3){
				dept3=0;
			}
			if(undefined==dept4){
				dept4=0;
			}
			if(undefined==dept5){
				dept5=0;
			}
			
			var dept_str = dept1+"."+dept2+"."+dept3+"."+dept4+"."+dept5;
			
			var param3 =$("#param3").val();
			
			$('#rulebase_list').bootstrapTable(
					'refresh',
					{url:'initrulebaselist?limit=10&ruletype='+ruletype
						+'&param2='+param2
						+'&param3='+param3
						+'&rulebaseStatus='+rulebaseStatus
						+'&deptId='+dept_str});
		})
		$('#dept1').change(function(){
			var dept_id = $("#dept1").find("option:selected").val();
			$.ajax({
				url:'main2addinitdept',
				type:'post',
				data:({deptCode:dept_id}),
				success:function(data){
					$("#dept2").children('option').remove();
					$("#dept3").children('option').remove();
					$("#dept4").children('option').remove();
					$("#dept5").children('option').remove();
					if(data.length>1){
						var data_leng = data.length;
						var opp="<option value='0'>请选择</option>";
						for(var i=0;i<data_leng;i++){
							opp = opp+"<option value='"+data[i].id+"'>"+data[i].comname+"</option>";
						}
						$("#dept2").append(opp);
					}
				}
			})
		})
		$('#dept2').change(function(){
			var dept_id = $("#dept2").find("option:selected").val();
			$.ajax({
				url:'main2addinitdept',
				type:'post',
				data:({deptCode:dept_id}),
				success:function(data){
					$("#dept3").children('option').remove();
					$("#dept4").children('option').remove();
					$("#dept5").children('option').remove();
					if(data.length>1){
						var data_leng = data.length;
						var opp="<option value='0'>请选择</option>";
						for(var i=0;i<data_leng;i++){
							opp = opp+"<option value='"+data[i].id+"'>"+data[i].comname+"</option>";
						}
						$("#dept3").append(opp);
					}
				}
			})
		})
		$('#dept3').change(function(){
			var dept_id = $("#dept3").find("option:selected").val();
			$.ajax({
				url:'main2addinitdept',
				type:'post',
				data:({deptCode:dept_id}),
				success:function(data){
					$("#dept4").children('option').remove();
					$("#dept5").children('option').remove();
					if(data.length>1){
						var data_leng = data.length;
						var opp="<option value='0'>请选择</option>";
						for(var i=0;i<data_leng;i++){
							opp = opp+"<option value='"+data[i].id+"'>"+data[i].comname+"</option>";
						}
						$("#dept4").append(opp);
					}
				}
			})
		})
		$('#dept4').change(function(){
			var dept_id = $("#dept4").find("option:selected").val();
			$.ajax({
				url:'main2addinitdept',
				type:'post',
				data:({deptCode:dept_id}),
				success:function(data){
					$("#dept5").children('option').remove();
					if(data.length>1){
						var data_leng = data.length;
						var opp="<option value='0'>请选择</option>";
						for(var i=0;i<data_leng;i++){
							opp = opp+"<option value='"+data[i].id+"'>"+data[i].comname+"</option>";
						}
						$("#dept5").append(opp);
					}
				}
			})
		})
		
		
		
		
		//转到编辑页面
		$("#update").on("click",function(){
			var data = $('#taskset_list').bootstrapTable('getSelections');
			if(data.length!=1){
				alertmsg("请选择一条");
				return false;
			}else{
				$("#myModal_taskset_add").on("hidden.bs.modal", function() {
				    $(this).removeData("bs.modal");
				});
				$("#myModal_taskset_add").modal({
					backdrop:'static',
					 show: true,
				     remote: 'main2edit?id='+data[0].id
				});
			}
			
		})
		$('#rulebase_list').bootstrapTable({
            method: 'get',
            url: "initrulebaselist",
            cache: false,
            striped: true,
            pagination: true,
            singleSelect:false,
            sidePagination: 'server', 
            pageSize: '10',
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            }, {
                field: 'ruleType',
                title: '规则类别',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'ruleName',
                title: '规则名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'rulePostil',
                title: '规则描述',
                align: 'center',
                valign: 'middle',
                sortable: true
            },{
                field: 'taskSetName',
                title: '规则状态',
                align: 'center',
                valign: 'middle',
                switchable:false,
                formatter: operateFormatter,
                events: operateEvents
            }]
        });
	});
});

function init_dept(dept_id){
	//下拉框联动
	$('#"+[dept_id]+"').change(function(){
		var dept_id = $("#dept_parent_id").find("option:selected").val();
		$.ajax({
			url:'main2addinitdept',
			type:'post',
			data:({deptCode:dept_id}),
			success:function(data){
				$("#dept_id").children('option').remove();
				if(data.length>1){
					var data_leng = data.length;
					var opp="<option>请选择</option>";
					for(var i=0;i<data_leng;i++){
						opp = opp+"<option value='"+data[i].id+"'>"+data[i].comname+"</option>";
					}
					$("#dept_id").append(opp);
				}
			}
		})
	})
}
function operateFormatter(value, row, index) {
    return [
        '<a id="d1" class="edit m-left-5" href="javascript:void(0)" title="关联规则">'+
        row.ruleState+'</a>'
    ].join('');
}
//事件响应
window.operateEvents = {
    'click #d1': function (e, value, row, index) {
    	add_taskset(row.id);
    }
};
function add_taskset(id){
	$("#myModal_taskset_add").on("hidden.bs.modal", function() {
	    $(this).removeData("bs.modal");
	});
	$("#myModal_taskset_add").modal({
		backdrop:'static',
		 show: true,
	     remote: 'mian2tasksetlist?id='+id
	});
}