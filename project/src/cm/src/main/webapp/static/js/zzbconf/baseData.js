require(["jquery", "jqform", "jqgrid", "jqgridi18n", "jqvalidate", "jqmetadata", "jqvalidatei18n", "additionalmethods","public"], function ($) {
	console.info("===============");
	$(function() {
		//jquery validate 校验
//		$("#usersaveform").validate({debug:true});
		//加载用户列表
		$("#jqGrid").jqGrid({
	        url: 'list',
	        mtype: "POST",
	        datatype: "json",
	        colModel: [
	            { label: '机构id', name: 'id', key: true, hidden: true, width: 0 },
	            { label: '机构编码', name: 'nodeCode', width: 75 },
	            { label: '名称', name: 'nodeName', width: 75 },
	            { label: '简称', name: 'shortName', hidden: true, width: 0 },
	            { label: '地址', name: 'address', width: 100 },
	            { label: '省份', name: 'provinceCode', width: 100 },
	            { label: '城市', name: 'cityCode', width: 100 },
	            { label: '创建时间', name: 'createtime', width: 150 },
	            { label: '操作1', name: 'operating1', width: 50 },
	            { label: '操作2', name: 'operating2', width: 50 }
	        ],
	        loadonce: true,
			viewrecords: true,
	        width: 980,
	        height: 350,
	        rowNum: 20,
	        rowList : [20,30,50],
	        rownumbers: true, 
	        rownumWidth: 25, 
	        multiselect: true,
	        pager: "#jqGridPager"
	    });
		// 用户列表页面【查询】按钮
		$("#querybutton").click(function() {
			var postdata = "";
			if($("#nodeCode").val()){
				postdata += "&nodeCode=" + $("#nodeCode").val();
			}
			if($("#username").val()){
				postdata += "&nodeName=" + $("#nodeName").val();
			}
			if($("#deptname").val()){
				postdata += "&shortName=" + $("#shortName").val();
			}
			postdata = postdata.substr(1);
			$("#jqGrid").jqGrid('setGridParam',{ 
	            url:"query",
	            mtype: "POST",
		        datatype: "json",
	            postData: postdata //发送数据 
	        }).trigger("reloadGrid"); //重新载入
		});
		// 【重置】按钮
		$("#resetbutton").on("click", function(e) {
			$("#usercode").val("");
			$("#username").val("");
			$("#userorganization").val("");
		});
		// 添加用户界面【添加】按钮
		$("#addbutton").on("click", function(e) {
//			$.ajax({
//				url : 'toedite',
//				type : 'GET',
//				dataType : "json",
//				error : function() {
//					alertmsg("Connection error");
//				}
//			});
		});
	});

});
//获得选中的信息
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");

    if (!rowKey)
        alertmsg("No rows are selected");
    else {
        var selectedIDs = grid.getGridParam("selarrrow");
        var arrayuserid = new Array();
        for (var i = 0; i < selectedIDs.length; i++) {
        	arrayuserid.push(selectedIDs[i]); 
        }

       return arrayuserid;
    }                
}


//根据userid删除用户
function deleteuser(id){
	$.ajax({
		url : 'deletebyid.do?id=' + id,
		type : 'GET',
		dataType : "json",
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			var result = data.count;
			if (result>0) {
				$("p").html(result+"条数据被删除");
				$("#jqGrid").jqGrid('setGridParam',{ 
		            url:"inituserlist",
		            mtype: "POST",
			        datatype: "json",
		            postData: '' //发送数据 
		        }).trigger("reloadGrid"); //重新载入
			}else{
				$("p").html("error");
			}
		}
	});
}
//根据userid更新用户信息
function updateuser(id){
	location.href = "save?id=" + id;
//	$.ajax({
//		url : 'save?id=' + id,
//		type : 'GET',
//		dataType : "json",
//		async : true,
//		error : function() {
//			alertmsg("Connection error");
//		},
//		success : function(data) {
//			
//		}
//	});
}
