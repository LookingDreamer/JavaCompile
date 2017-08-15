require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","zTree","zTreecheck","public"], function ($) {
	//数据初始化
	$(function() {
		$('#table-ability').bootstrapTable({
	        method: 'get',
	        url: pageurl+"?ediid="+$("#ediconfid").val(),
	        singleSelect:true,
	        cache: false,
	        striped: true,
	        smartDisplay: false,
	        pagination: true,
	        pageNumber:true,
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
                field: 'confid',
                title: 'id',
                visible: false,
                switchable:false
            }, {
	        	field: 'providername',
	        	title: '供应商名称',
	        	align: 'center',
	        	valign: 'middle',
	        	sortable: true
	        }, {
	        	field: 'deptname',
	        	title: '机构名称',
	        	align: 'center',
	        	valign: 'middle',
	        	sortable: true
	        }, {
	            field: 'ability',
	            title: '能力',
	            align: 'center',
	            valign: 'middle'
	        }]
	    }).on('click-row.bs.table', function (e, row, $element) {
    		var id = row.confid;
    		select_auto_id(id);
   		
    	});
//		返回
		$('#backbutton').on("click",function(){
			window.location.href="list";
		});
//		自动化配置选择供应商ok
		$("#prvautoname").on("click", function(e) {
			$('#showautopic').modal();
			$('#treeDemosearch').val('');
			$.fn.zTree.init($("#treeautoDemo"), settingauto);
		});
//		自动化配置选择机构ok
		$("#deptname").on("click", function(e) {
			$('#showdeptpic').modal();
			$('#treesearch').val('');
			$.fn.zTree.init($("#treeDemoDept"), settingdept);
		});
//		根据供应商id和机构id查询能力配置列表
		$("#queryautoconfig").on("click",function(){
			$('#table-ability').bootstrapTable(
					'refresh',
					{url:"queryautoconfig?providerid="+$("#providerid").val()+"&deptid="+$("#deptid").val()+"&confid="+$("#ediconfid").val()+"&ediid="+$("#ediconfid").val(),
					});
		})
//		重置
		$("#resetbutton").on("click",function(){
			$("#confid").val('');
			$("#prvautoname").val('');
			$("#deptname").val('');
			$("#providerid").val('');
			$("#deptid").val('');
			$("#ability01").prop("checked",false);
			$("#ability02").prop("checked",false);
			$("#ability03").prop("checked",false);
			$("#ability04").prop("checked",false);
			$("#ability05").prop("checked",false);
		})
//		添加能力
		$("#addability").on("click",function(){ 
			var conftypes = [];
			$('input[name="conftype"]:checked').each(function (){
				conftypes.push($(this).val());
			});
			if($("#deptid").val()==null||$("#deptid").val()==""||$("#providerid").val()==null||$("#providerid").val()==""){
				alertmsg("请选择供应商或机构");
			}else{
				$.ajax({
					url : "addautoconf?providerid="+$("#providerid").val()+"&deptid="+$("#deptid").val()+"&conftype="+conftypes+"&confid="+$("#confid").val()+"&ediid="+$("#ediconfid").val(),
					type : 'GET',
					dataType : "json",
					async : true,
					error : function() {
						alertmsg("Connection error");
					},
					success : function(data) {
						alertmsg(data.msg);
						$("#confid").val('');
						$("#prvautoname").val('');
						$("#deptname").val('');
						$("#providerid").val('');
						$("#deptid").val('');
						reloaddata(""); //局部刷新
					}
				})
			}
		})
		
//		根据id删除
		$("#delabilitybyid").on("click",function(){
			var auto_id = $('#table-ability').bootstrapTable('getSelections');
			var data = ""
			if(auto_id.length == 0){
				alertmsg("没有行被选中！");
			}else{
				data = auto_id[0].confid;
			}
			$.ajax({
				url : 'delautoconfbyid?id='+data,
				type : 'GET',
				dataType : "json",
				async : true,
				error : function() {
					alertmsg("Connection error");
				},
				success : function(data) {
					var resultconf = data.count;
					if (resultconf>0) {
						$("#confid").val('');
						$("#prvautoname").val('');
						$("#deptname").val('');
						$("#providerid").val('');
						$("#deptid").val('');
						alertmsg("删除成功！");
						reloaddata("");//重新载入
					}else{
						alertmsg("删除失败！");
					}
				}
			});
		})
	    //双击选项
	    $('#rightselect').dblclick(function() {
	      $("option:selected", this).remove().appendTo('#leftselect');
	    });
		
		//可选择的保险公司列表
		$.fn.zTree.init($("#treeDemo"), setting);
//		获取num
		$(function(){
			
			 num = $("#hiddensum").val();
		});
	});

});

function select_auto_id(id){
	$.ajax({
		url : 'selectautobyid',
		type : 'GET',
		dataType : "json",
		async : true,
		data:({id:id}),
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data){
			$("#ability01").prop("checked",false);
			$("#ability02").prop("checked",false);
			$("#ability03").prop("checked",false);
			$("#ability04").prop("checked",false);
			$("#ability05").prop("checked",false);
			$("#confid").val(id);
			$("#prvautoname").val(data.providername);
			$("#deptname").val(data.deptname);
			$("#providerid").val(data.providerid);
			$("#deptid").val(data.deptid);
			var abilitys = data.ability.split(",");
			for(var i=0;i<abilitys.length;i++){
	            $("#ability"+abilitys[i]).prop("checked", true);
	            $("#ability"+abilitys[i]).val(abilitys[i]);
	        }
		}
	})
}
//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "abilitylist";
//刷新列表
function reloaddata(data){
	$.ajax({
		url : pageurl+"?ediid="+$("#ediconfid").val(),
		type : 'GET',
		dataType : "json",
		data: data+"&limit="+pagesize,
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#table-ability').bootstrapTable('refresh', data);
			
		}
	});
}
//获得选中行的id列表
function getSelectedRows() {
    var data = $('#table-javascript').bootstrapTable('getSelections');
    if(data.length == 0){
    	alertmsg("没有行被选中！");
    }else{
    	var arrayuserid = new Array();
    	for(var i=0;i<data.length;i++){
    		arrayuserid.push(data[i].id)
    	}
    	return arrayuserid;
    }
}
var setting = {
		async: {
			enable: true,
			url:"queryprotree",
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
			onCheck: zTreeOnCheck
		}
};

function zTreeOnCheck(event, treeId, treeNode) {
	if(isNaN(num)){
		num = 1;
	}else{
		num = parseInt(num);
	}
	$("#parentcode").val(treeNode.prvcode);
//	$("#leftselect").append('<li ondblclick="f1('+treeNode.prvcode+')" id='+treeNode.prvcode+'>'+treeNode.name+'</li>');
	$("#leftselect").append('<li ondblclick="f1('+treeNode.prvcode+')" id='+treeNode.prvcode+'>'+ '<input type="hidden" name="insbproviderandedi['+num+'].providerid" readonly="readonly" style="border-left:0px;border-top:0px;border-right:0px;border-bottom:1px" value="'+treeNode.id+'">'+treeNode.name+'</li>');
	num++;
}

function f1(id){
	$("#"+id).remove();
}
var settingauto = {
		async: {
			enable: true,
			url:"queryprotree",
			autoParam:["id"],
			dataType: "json",
			type: "post"
		},
		view: {
			fontCss: function (treeId, treeNode) {
				return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
			},
			expandSpeed: ""
		},
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},
		callback: {
			onCheck: zTreeOnCheckauto
		}
	};
var settingdept = {
		async: {
			enable: true,
			url:"querydepttree",
			autoParam:["id"],
			dataType: "json",
			type: "post"
		},
		view: {
			fontCss: function (treeId, treeNode) {
				return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
			},
			expandSpeed: ""
		},
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},
		callback: {
			onCheck: zTreeOnCheckDept,
            onAsyncSuccess:zTreeOnAsyncSuccess
		}
};
function zTreeOnAsyncSuccess(event,treeId,treeNode,msg){
	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	var node = treeObj.getNodeByParam('isParent',true,null);
	treeObj.expandNode(node,true,false,false);
}
function zTreeOnCheckauto(event, treeId, treeNode) {
	$("#providerid").val(treeNode.prvcode);
	$("#prvautoname").val(treeNode.name);
	$('#showautopic').modal("hide");
}
function zTreeOnCheckDept(event, treeId, treeNode) {
	$("#deptid").val(treeNode.id);
	$("#deptname").val(treeNode.name);
	if(treeNode.comtype != "05"){
		alertmsg("下级机构会被重置");
	}
	$('#showdeptpic').modal("hide");
}