require(["jquery", "zTree","zTreecheck","flat-ui","bootstrap-table", "bootstrap","bootstrapTableZhCn","public"], function ($) {
	// 数据初始化
	$(function() {
		//start 险种类型为车险，险种小类显示为车险的小类
		var current = $("#risktype").val();
		if(current == "车险") {
			$("#status option").each(function(i){	
				$("#status option[parent!= '1']").hide();
				$("#status option[parent = '1']").show();							
			})
		}
		//end
		$('#table-javascript').bootstrapTable({
            method: 'get',
            url: pageurl,
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server',
            pageSize: 10,
            minimumCountColumns: 2,
            //pageSize: pagesize,
            singleSelect:true,
            clickToSelect: true,
            columns: [{
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            }, {
                field: 'id',
                title: '险种ID',
                visible : false,
				switchable : false
            }, {
                field: 'riskcode',
                title: '险种编码',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'riskname',
                title: '险种名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'providename',
                title: '所属保险公司',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'risktype',
                title: '险类',
                align: 'center',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'status',
                title: '小类',
                align: 'center',
                valign: 'middle',
                sortable: true
            }
            ]
        });
			//选择供应商
			$("#providename").on("click", function(e) {
				$('#showpic').modal();
				$.fn.zTree.init($("#treeDemo"), setting);
			});
			// 用户列表页面【查询】按钮
			$("#querybutton").on("click", function(e) {
				reloaddata();
			 });
			$("#resetbutton").on("click", function(e) {
				$("#riskcode").val("");
				$("#riskname").val("");
				$("#provideid").val("");
				$("#providename").val("");
				$("#status").val("");
			});
			$("#updaterisk").on("click", function(e) {
				    var data = $('#table-javascript').bootstrapTable('getSelections');
				  
				    if(data.length == 0){
				    	alertmsg("至少选择一行数据！");
				    	return false;
				    }else{
				    	var arrayuserid = new Array();
				    	for(var i=0;i<data.length;i++){
				    		arrayuserid.push(data[i].id)
				    	}				    	
//				    	if(riskid!=null && riskid!=undefined){
//				    		location.href = "updateriskshow?riskId="+riskid;
			    		if(arrayuserid!=null && arrayuserid!=undefined){
				    		location.href = "updateriskshow?riskId="+arrayuserid;
				    	}else{
				    		return false;
				    	}
				    }
				    return false;
			 });
			$("#addrisk").on("click", function(e) {
				location.href = "addriskshow";
			 });
			$("#toggle").on("click", function(e) {
				$('#table-javascript').bootstrapTable('toggleView');
			});
			// 险种页面【删除】按钮(可以批量删除)
			$("#deleted").on("click", function(e) {
				var arrayuserid = getSelectedRow();
				var data = $('#table-javascript').bootstrapTable('getSelections');
				if(data.length>0){
					
					if(confirm("您选择了"+data.length+"条数据，是否确认删除")){				 
						$.ajax({
							url : 'delete?arrayid=' + arrayuserid,
							type : 'GET',
							dataType : "json",
							async : true,
							error : function() {
								alertmsg("Connection error");
							},
							success : function(data) {
								var result = data.count;
								if (result>0) {
									window.event.returnValue = false;
									alertmsg("删除成功！");
									reloaddata();// 重新载入
								}else{
									alertmsg("删除失败！");
								}
							}
							
						});
						}
				}else{
					return false;
				}
				return false;
			});
			 
	});
});
// 默认一页显示十条记录
var pagesize = 10;
var offset = 0;
// 当前调用的url
var pageurl = "querylist";
//获得选中行的id
function getSelectedRow() {
//    var data = $('#table-javascript').bootstrapTable('getSelections');
//    
//    if(data.length == 0){
//    	alertmsg("没有行被选中！");
//    }else{
//    	return data[0].id ;
//    }
	 var data = $('#table-javascript').bootstrapTable('getSelections');
	    if(data.length == 0){
	    	alertmsg("至少选择一行数据！");
	    }else{
	    	var arrayuserid = new Array();
	    	for(var i=0;i<data.length;i++){
	    		arrayuserid.push(data[i].id)
    	}
    	return arrayuserid;
	 }
   
}

// 刷新列表
function reloaddata(){
	var postdata = "";
	if($("#riskcode").val()){
		postdata += "&riskcode=" + $("#riskcode").val();
	}
	if($("#riskname").val()){
		postdata += "&riskname=" + $("#riskname").val();
	}
	if($("#providename").val()){
		postdata += "&providename=" + $("#providename").val();
	}
	if($("#provideid").val()){
		postdata += "&provideid=" + $("#provideid").val();
	}
	/*if(!!$("#risktype").val()){
		postdata += "&risktype=" + $("#risktype").val();
	}*/
	if(!!$("#status").val()){
		postdata += "&status=" + $("#status").val();
	}
	$.ajax({
		url : pageurl,
		type : 'GET',
		dataType : "json",
		data: postdata+"&limit="+pagesize+"&offset="+offset,
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#table-javascript').bootstrapTable('load', data);
		}
	});
	
}


var setting = {
		async: {
			enable: true,
			url:"../provider/queryprotree",
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
	$("#provideid").val(treeNode.prvcode);
	$("#providename").val(treeNode.name);
	$('#showpic').modal("hide");
}

//$(function(){
//		$('#risktype').change(function(){
//				var current = $("#risktype").val();
//				if(current == "1") {
//					//$("#status option").removeAttr("disabled");
//					$("#status option").each(function(j){
//						if($(this).attr("parent") == "1" || $(this).attr("parent") == "0"){
//							$(this).show();
//						}else{
//							$(this).hide();
//						}
//					})
//					$("#status option").get(0).selected = true
//				}else if(current == "2"){
//					//$("#status option").removeAttr("disabled");
//					$("#status option").each(function(j){
//							if($(this).attr("parent") == "2" || $(this).attr("parent") == "0"){
//								$(this).show();
//							}else{
//								$(this).hide();
//							}
//					})
//					$("#status option").get(0).selected = true
//				}else{
//					//$("#status option").attr("disabled", "true");
//					$("#status option").each(function(j){
//						$(this).show();
//					})
//				}
//		})
//})	