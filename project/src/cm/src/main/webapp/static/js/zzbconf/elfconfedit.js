require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","zTree","zTreecheck","jqform","public"], function ($) {
	//数据初始化
	$(function(){ 
		var flag1 = $("#capconf2").attr("checked");
		var flag2 = $("#capconf4").attr("checked");
		if(flag1=="checked"||flag2=="checked"){
			 $("#skillsdiv").show();
		 }else{
			 $("#skillsdiv").hide();
		 }
	}); 
	$(function() {
		$('#table-ability').bootstrapTable({
			method: 'get',
	        url: pageurl+"?elfid="+$("#elfconfid").val(),
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
//		选择供应商ok
		$("#prvname").on("click", function(e) {
			$('#showpic').modal();
			$('#treesearch').val('');
			$.fn.zTree.init($("#treeDemo"), setting);
		});
//		自动化配置选择供应商ok
		$("#prvautoname").on("click", function(e) {
			$('#showautopic').modal();
			$('#treesearch1').val('');
			$.fn.zTree.init($("#treeautoDemo"), settingauto);
		});
//		自动化配置选择机构ok
		$("#deptname").on("click", function(e) {
			$('#showdeptpic').modal();
			$('#treesearchDept').val('');
			$.fn.zTree.init($("#treeDemoDept"), settingdept);
		});
//		查询
		$("#queryautoconfig").on("click",function(){
			$('#table-ability').bootstrapTable(
					'refresh',
					{url:"queryautoconfig?providerid="+$("#providerid").val()+"&deptid="+$("#deptid").val()+"&confid="+$("#elfconfid").val()+"&elfconfid="+$("#elfconfid").val()
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
//		添加
		$("#addability").on("click",function(){ 
			var conftypes = [];
			$('input[name="conftype"]:checked').each(function (){
				conftypes.push($(this).val());
			});
			if($("#deptid").val()==null||$("#deptid").val()==""||$("#providerid").val()==null||$("#providerid").val()==""){
				alertmsg("请选择供应商或机构");
			}else{
				$.ajax({
					url : "addautoconf?providerid="+$("#providerid").val()+"&deptid="+$("#deptid").val()+"&conftype="+conftypes+"&confid="+$("#confid").val()+"&elfid="+$("#elfconfid").val(),
					type : 'GET',
					dataType : "json",
					async : true,
					error : function() {
						alertmsg("Connection error");
					},
					success : function(data) {
						alertmsg(data.msg)
						$("#confid").val('');
						$("#prvautoname").val('');
						$("#deptname").val('');
						$("#providerid").val('');
						$("#deptid").val('');
						reloaddata("");
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
						alertmsg("删除成功！");
						$("#confid").val('');
						$("#prvautoname").val('');
						$("#deptname").val('');
						$("#providerid").val('');
						$("#deptid").val('');
						reloaddata("");//重新载入
					}else{
						alertmsg("删除失败！");
					}
				}
			});
		})
		
//		显示隐藏控制
		  $("#capconf2").click(function(){
			 if(!this.checked&&!$('#capconf4').prop("checked")){
				 $("#skillsdiv").hide();
			 }else{
				 $("#skillsdiv").show();
			 }
		 })
		 $("#capconf4").click(function(){
			 if(!this.checked&&!$('#capconf2').prop("checked")){
				 $("#skillsdiv").hide();
			 }else{
				 $("#skillsdiv").show();
			 }
		 })
//		 输入项选中样式
		 $(".mulFindList li").on("click",function(){
				if($(this).hasClass("bg-primary")){
					$(this).removeClass("bg-primary");
					$(this).find('input:first').removeAttr("name","inputcode");
				}else{
					$(this).addClass("bg-primary");
					$(this).find('input:first').attr("name","inputcode");
				};
				return false;
		});
//		输入项添加按钮 
		 $("a.tabNewAdd").on("click",function(){
				var $tabAddNext = $(this).parent("td").next("td").find("ul.mulFindList");
				var $tabAddPrev = $(this).parent("td").prev("td").find("li.bg-primary");
				$tabAddPrev.detach().appendTo($tabAddNext).removeClass();
				return false;
		});
		 
//		 输入项移除按钮
			$("a.tabNewRemove").on("click",function(){
				var $tabAddNext = $(this).parent("td").next("td").find("li.bg-primary");
				var $tabAddPrev = $(this).parent("td").prev("td").find("ul.mulFindList");
				$($tabAddNext).find("input").attr("name","");
				$tabAddNext.detach().appendTo($tabAddPrev).removeClass();
				return false;
			});
//			输入项全选、全不选
			$("a.tabNewAll").on("click",function(){
				var $tabAddPrev = $(this).parent("td").prev("td").find("ul.mulFindList li");
				if($tabAddPrev.hasClass("bg-primary")){
					$tabAddPrev.removeClass("bg-primary");
				}else{
					$tabAddPrev.addClass("bg-primary");
				};
				return false;
			});
//	-----------------------------------------------------------------------------------------------		
//		 输出项选中样式
			$(".mulFindListout li").on("click",function(){
				if($(this).hasClass("bg-primary")){
					$(this).removeClass("bg-primary");
					$(this).find('input:first').removeAttr("name","outputcode");
				}else{
					$(this).addClass("bg-primary");
					$(this).find('input:first').attr("name","outputcode");
				};
				return false;
			});
//		输出项添加按钮 
			$("a.tabNewAddout").on("click",function(){
				var $tabAddNext = $(this).parent("td").next("td").find("ul.mulFindListout");
				var $tabAddPrev = $(this).parent("td").prev("td").find("li.bg-primary");
				$tabAddPrev.detach().appendTo($tabAddNext).removeClass();
				return false;
			});
			
//		 输出项移除按钮
			$("a.tabNewRemoveout").on("click",function(){
				var $tabAddNext = $(this).parent("td").next("td").find("li.bg-primary");
				var $tabAddPrev = $(this).parent("td").prev("td").find("ul.mulFindListout");
				$($tabAddNext).find("input").attr("name","");
				$tabAddNext.detach().appendTo($tabAddPrev).removeClass();
				return false;
			});
//			输出项全选、全不选
			$("a.tabNewAllout").on("click",function(){
				var $tabAddPrev = $(this).parent("td").prev("td").find("ul.mulFindListout li");
				if($tabAddPrev.hasClass("bg-primary")){
					$tabAddPrev.removeClass("bg-primary");
				}else{
					$tabAddPrev.addClass("bg-primary");
				};
				return false;
			});
	});

    var setting = {
        async: {
            enable: true,
            url:"queryprotree"+"?proid="+$("#proid").val(),
            dataType: "json",
            type: "post",
//			autoParam: ["id=''"],
            otherParam:["id","","type","stair"]
//			otherParam:{"id":function(){
//				return $("#commonusebranchid").val();
//			}}

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
            onCheck: zTreeOnCheck
        }
    };
    var settingauto = {
        async: {
            enable: true,
            url:"queryprotreeOfablity",
            autoParam:["id"],
//			otherParam:["id",pubprvid,"type","stair2"],
//			otherParam:{"type":function(){
//				return $("#proid").val();
//			}},
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
});
//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "abilitylist";
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
//刷新列表
function reloaddata(data){
	$.ajax({
		url : pageurl+"?elfid="+$("#elfconfid").val(),
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
function zTreeOnCheck(event, treeId, treeNode) {
	$("#proid").val(treeNode.prvcode);
	$("#prvname").val(treeNode.name);
	$('#showpic').modal("hide");
//	$.ajax({
//		url:'ceshi',
//		type:'get',
//		data:({para:$("#proid").val()}),
//	});
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
function zTreeOnAsyncSuccess(event,treeId,treeNode,msg){
	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	var node = treeObj.getNodeByParam('isParent',true,null);
	treeObj.expandNode(node,true,false,false);
	/*var nodes = treeObj.getNodesByParam('isParent',true,node);
	for(var i=0;i<nodes.length;i++){
		treeObj.expandNode(nodes[i],true,true,true);
	}
	treeObj.selectNode(node.children[0]);*/
}