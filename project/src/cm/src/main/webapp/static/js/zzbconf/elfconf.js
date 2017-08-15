require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","zTree","zTreecheck","jqform","public"], function ($) {
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
            	field: 'rownum',
                title: '序号',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'id',
                title: 'id',
                visible: false,
                switchable:false
            }, {
                field: 'elfname',
                title: '名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'elfpath',
                title: '接口路径',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'operating',
                title: '操作',
                align: 'center',
                valign: 'middle',
                switchable:false,
                formatter: operateFormatter,
                events: operateEvents
            }]
        });
//		返回
		$("#backbutton").on("click",function(e){
			history.go(-1);
		})
//		选择供应商ok(新增)
		$("#prvname").on("click", function(e) {
			$('#showpic').modal();
			$.fn.zTree.init($("#treeDemo"), setting);
		});
		//转到新增页面
		$("#addelfconf").click(function(){
			location.href = "addelfconf";
		})
		
		//新增保存ok
		$("#elfconfsaveform").on("click", function(e) {	
		});
//		显示隐藏控制
		 $("#capconf2").click(function(){
			 if(!this.checked&&!$('#capconf1').prop("checked")){
				 $("#skillsdiv").hide();
			 }else{
				 $("#skillsdiv").show();
			 }
		 })
		 $("#capconf1").click(function(){
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

});
//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "initelfconflist";
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
//根据id删除配置信息
function deleteelfconf(id){
	if(confirm("确定删除吗")){
	$.ajax({
		url : 'deletebyid?id=' + id,
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
				reloaddata("");//重新载入
			}else{
				alertmsg("删除失败！");
			}
		}
		
	});
	}
}
//根据elfconfID修改elf信息跳转到elfconfedit页面
function updateelfconf(id){
	location.href = "elfconfedit?id="+id;
}
//刷新列表
function reloaddata(data){
	$.ajax({
		url : pageurl,
		type : 'GET',
		dataType : "json",
		data: data+"&limit="+pagesize,
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#table-javascript').bootstrapTable('load', data);
		}
	});
}
//添加事件
function operateFormatter(value, row, index) {
    return [
        '<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
        '<i class="glyphicon glyphicon-edit"></i>',
        '</a>',
        '<a class="remove m-left-5" href="javascript:void(0)" title="删除">',
        '<i class="glyphicon glyphicon-remove"></i>',
        '</a>'
    ].join('');
}
//事件响应
window.operateEvents = {
    'click .edit': function (e, value, row, index) {
    	updateelfconf(row.id);
    },
    'click .remove': function (e, value, row, index) {
    	deleteelfconf(row.id);
    }
};
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
	$("#providerid").val(treeNode.prvcode);
	$("#prvname").val(treeNode.name);
	$('#showpic').modal("hide");
}
