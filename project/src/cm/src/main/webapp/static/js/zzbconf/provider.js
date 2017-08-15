require(["jquery","ajaxfileupload","zTree","zTreecheck","fuelux","jqvalidatei18n", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "jqcookie", "jqtreeview","lodash","public"], function ($) {

	//数据初始化 
	$(function() {
		inittree();
		//新增ok
		$("#addpro").on("click", function(e) {
			window.location.href="jumpadd";
		});
		//查看修改ok
		$("#detailspro").on("click", function(e) {	
			if(!$("#updateid").val()){
				alertmsg("请选择要查看的供应商!");
				return;
			}
			window.location.href="queryproinfobyid?id="+$("#updateid").val();
		});
		
		//查看修改保存ok
		$("#saveOrUpdatePro").on("click", function(e) {	
//			$("#saveupdateid").val(id);
			if($("#prosaveupdateform").valid()){
				$("#prosaveupdateform").submit();
			}
		});
		$("#prosaveupdateform").validate({
			errorLabelContainer : "#providererror",
			errorElement : "p",
			errorClass : "text-left",
			rules : {
				quotationtime : {
					digits:true
				},
				quotationinterval : {
					digits:true
				},
				insuretime : {
					digits:true
				},
				quotationvalidity : {
					digits:true
				},
				orderflag : {
					digits:true
				},
				advancequote :{
					digits:true
				}
				
			},
			messages : {
				quotationtime : {
					digits : "请输入合法的整数！"
				},
				quotationinterval : {
					digits : "请输入合法的整数！"
				},
				insuretime : {
					digits : "请输入合法的整数！"
				},
				quotationvalidity : {
					digits : "请输入合法的整数！"
				},
				payvalidity : {
					digits : "请输入合法的整数！"
				},
				orderflag : {
					digits : "请输入合法的整数！"
				},
				advancequote : {
					digits : "请输入合法的整数！"
				}
			}
//			highlight : validateHighlight,
//			success : validateSuccess
		})
//		选择供应商ok
		$("#prvname").on("click", function(e) {
			$('#showpic').modal();
			$.fn.zTree.init($("#treeDemo"), setting);
		});
//		选择机构ok
		$("#deptname").on("click", function(e) {
			$('#showdeptpic').modal();
			$.fn.zTree.init($("#treeDemoDept"), settingdept);
		});
//		关联规则
//		$("#rulepostil").on("click", function(e) {
//			$("#myModal_rule_add").modal();
//		});
		
//		list查询选择机构ok
		$("#querydeptname").on("click", function(e) {
			$('#showquerydeptpic').modal();
			$.fn.zTree.init($("#treequeryDept"), settingquerydept);
		});
		
//		删除ok
		/*$("#delpro").on("click", function(e) {	
			if(!$("#updateid").val()){
				alertmsg("请选择供应商!");
				return;
			}
			$("#delid").val($("#updateid").val());
		});*/
		$("#delpro").on("click", function(e) {	
			if(!$("#updateid").val()){
				alertmsg("请选择供应商!");
				return;
			}
			$("#delid").val($("#updateid").val());
			var did=$("#updateid").val();
			
			if(confirm("确认要删除吗？")){
				$.ajax({
					url:'deletbyid',
					type:'post',
					data:({id:did}),
					success:function(data){
						alertmsg("删除成功");
						inittree();
						/*location.reload();
						if(data.status=="1"){
						alertmsg(data.message);
						reloaddata();
					}else{
						alertmsg(data.message);
					}*/
					}
				});
			}
		});
		
		// 查询条件【重置】按钮
		/*$("#resetbutton").on("click", function(e) {
			$("#prvcode").val("");
			$("#prvname").val("");
			$("#prvgrade").val("");
			$("#businesstype").val("");
			$("#querydeptname").val("");
		});*/
		$("#resetbutton").on("click", function(e) {
			 $("#prvcode").val("");
			 $("#prvname1").val("");
			 $("#prvshotname").val("");
			 $("#prvname").val("");//上级供应商代码  ？？
			 $("#parentcode").val("");//上级供应商代码  ？？ type="hidden"
			 $("#prvgrade").val("");
			 $("#prvtype").val("");
			 $("#linkname").val("");
			 $("#linktel").val("");
			 $("#address").val("");
			 $("#prvurl").val("");
			 $("#servictel").val("");
			 $("#deptname").val("");//归属机构  ？？
			 $("#rulepostil").val("");//关联规则  ？？	
			 $("#channeltype").val("");//渠道类型  ？？
			 $("#province").val("");
			 $("#city").val("");
			 $("#logo").val("");//上传图片  ？？
			 $("#companyintroduce").val("");
			 
			 $("#quotationtime").val(""); //报价所需时间
			 $("#quotationinterval").val(""); //两次报价间隔时间
			 $("#insuretime").val("");  //核保所需时间
			 $("#quotationvalidity").val("");
			 $("#payvalidity").val("");
			 $("#quickinsureflag").val("");
			 
			 /**
			  * type="hidden"
			  */
			 $("#updateid").val("");
			 $("#affiliationorgquery").val("");
			 $("#affiliationorg").val("");
			 //$("#permissionorg").val("");
			 $("#saveupdateid").val("");
			 $("#advancequote").val("");
			 
		});
		
		// 供应商树页面【查询】按钮
		$("#querypro").on("click", function(e) {
			window.location.href="queryprotreelist";
//			var postdata = "";
//			if($("#prvcode").val()){
//				postdata += "&prvcode=" + $("#prvcode").val();
//			}
//			if($("#prvname").val()){
//				postdata += "&prvname=" + $("#prvname").val();
//			}
//			if($("prvgrade").val()){
//				postdata += "&prvgrade=" + $("#prvgrade").val();
//			}
//			if($("#businesstype").val()){
//				postdata += "&businesstype=" + $("#businesstype").val();
//			}
//			if($("#querydeptname").val()){
//				postdata += "&querydeptname=" + $("#querydeptname").val();
//			}
//			reloaddata(postdata);
		 });
		
		/*$('#myTree').on('selected.fu.tree', function (e, selected) {
			getdatafromid(selected.selected[0].dataAttributes.id);
			 $("#hiddenid").val(selected.selected[0].dataAttributes.id);
		});

		$('#myTree').on('updated.fu.tree', function (e, selected) {
			getdatafromid(selected.selected[0].dataAttributes.id);
		});
		
		$('#myTree').on('opened.fu.tree', function (e, info) {
			getdatafromid(info.dataAttributes.id);
		});

		$('#myTree').on('closed.fu.tree', function (e, info) {
			getdatafromid(info.dataAttributes.id);
		});*/
		
		
//		prorulelist 页面内容
		
//		选择地区
		initArea();
//		查询
		$("#querybutton").on("click",function(){
			var rulename = $("#rule_name").val();
			var rulepostil = $("#rule_postil").val();
			$('#taskset_rulebase_list').bootstrapTable('refresh',
										{url:'initrulelist?limit=10&rulePostil='+rulepostil+'&ruleName='+rulename});
		})
//		重置
		$('#resetbutton').on("click",function(){
			$('#rule_name').val('');
			$('#rule_postil').val('');
		})
//		关闭
//		$("#close_modal").on("click",function(){
//			$('#myModal_rule_add').modal('hide');
//		})
//		初始化规则列表
		$('#taskset_rulebase_list').bootstrapTable({
            method: 'get',
            url: "initrulelist",
            cache: false,
            striped: true,
            pagination: true,
            singleSelect:false,
            sidePagination: 'server', 
            pageSize: '10',
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
                field: 'id',
                title: '规则id',
                align: 'center',
                valign: 'middle',
                sortable: true
            },{
                field: 'ruleName',
                title: '业规则名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            },{
                field: 'rulePostil',
                title: '规则描述',
                align: 'center',
                valign: 'middle',
                sortable: true
            },{
                field: 'operating',
                title: '操作',
                align: 'center',
                valign: 'middle',
                switchable:false,
                formatter: operateFormatter,
                events: operateEvents
            }]
        });
		
		
		$("#uploadbutton").click(function(){
			var file = $("#uploadfileid").val();
			if(file){
				if(file=/^(([a-zA-Z]:)|(\\{2}\w+)\$?)(\\(\w[\w].*))(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.png|.PNG)$/.test(file)){
					if(!$("#updateid").val()){
						alertmsg("请选择供应商!");
						return;
					}else{
						ajaxFileUpload($("#updateid").val());
					}
				}else{
						alertmsg("请选择.jpg|.gif|.jpeg|.png格式的图片！");
					}
			}else{
				alertmsg("请选择上传图片");
			}
		});
//		树结构
//		================================>>>>>>>
	});
});

//图片上传
function  ajaxFileUpload(pid){
 $.ajaxFileUpload({
         url: '../file/uploadprologo?proid='+pid, 
         secureuri: false, 
         fileElementId: 'uploadfileid', 
         dataType: 'json',
         success: function (data, status)  //服务器成功响应处理函数
         {
             alertmsg("上传成功");
             var imgurl = data.provider.logo;
             $("#logo").attr("src",imgurl);
//           location.reload();
         },
         error: function (data, status, e)//服务器响应失败处理函数
         {
             alertmsg("上传失败");
         }
     })
 return false;
}
function initArea(){
	getAreaByParentid("0",$("#province"));
}
function changeprv(){
	 getAreaByParentid($("#province").val(),$("#city"));
}
//function changecity(){
//	 getAreaByParentid($("#city").val(),$("#county"));
//}

function getAreaByParentid(parentid,selectobject){
	$.ajax({
		url : "../region/getregionsbyparentid",
		type : 'GET',
		dataType : "json",
		data : {
			parentid:parentid
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			selectobject.empty();
			for (var i = 0; i < data.length; i++) {
				selectobject.append("<option value='"+data[i].id+"'>"+data[i].comcodename+"</option>");   
			}
			if(data.length>0){
				selectobject.get(0).selectedIndex=0;
			}
			selectobject.trigger("change"); 
		}
	});
}
function getAreaByParentid2(parentid,selectobject,value){
	$.ajax({
		url : "../region/getregionsbyparentid",
		type : 'GET',
		dataType : "json",
		data : {
			parentid:parentid
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			selectobject.empty();
			for (var i = 0; i < data.length; i++) {
				if(data[i].id == value){
					selectobject.append("<option selected='selected' value='"+data[i].id+"'>"+data[i].comcodename+"</option>");   
				}else{
					selectobject.append("<option value='"+data[i].id+"'>"+data[i].comcodename+"</option>");   
				}
			}
			selectobject.trigger("change"); 
		}
	});
}
function operateFormatter(value, row, index) {
    return [
        '<a class="edit m-left-5" href="javascript:void(0)" title="添加">',
        '<i class="glyphicon glyphicon-plus"></i>',
        '</a>'
    ].join('');
}
window.operateEvents = {
	    'click .edit': function (e, value, row, index) {
	    	//$("#permissionorg").val(row.id);
			$("#rulepostil").val(row.rulePostil);
			//$('#myModal_rule_add').modal('hide');
	    }
};
function addrulePostil(id){
	window.location.href="queryprotreelist";
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
var settingdept = {
		async: {
			enable: true,
			url:"querydepttree",
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
			onCheck: zTreeOnCheckDept
		}
};
var settingquerydept = {
		async: {
			enable: true,
			url:"querydepttree",
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
			onCheck: zTreeOnCheckQueryDept
		}
};
/*function getdatafromid(id){
	$.ajax({
		type: "POST",
		url: "../provider/queryid",
	    data: "parentcode="+id,
		dataType:"json",
		success: function(datainfo){
			$("#updateid").val(id);
		}
	});*/

function reloaddata(data){
	$('#myTree').tree({
		render: function (options, callback) {
			var parentcode ="";
			id = "";
			if(!jQuery.isEmptyObject(options)){
				parentcode =options.dataAttributes.parentcode;
				id = options.dataAttributes.id;
			}
			$.ajax({
				type : "GET",
				url : "../provider/queryprotreelist",
				data :data+"&parentcode="+parentcode,
				dataType : 'json',
				success : function(data) {
					callback({
						data: data
					});
				}
			});
		},
		multiSelect : false,
		cacheItems : true,
		folderSelect : false,
	});
}
//bootstrap-tree初始化树结构 
/*function inittree() {
	$('#myTree').tree({
	dataSource: function (options, callback) {
		var parentcode ="";
		id = "";
		if(!jQuery.isEmptyObject(options)){
			parentcode =options.dataAttributes.parentcode;
			id = options.dataAttributes.id;
		}
		$.ajax({
			type : "POST",
			url : "../provider/inittreeprolist",
			data :"root="+parentcode,
			dataType : 'json',
			success : function(data) {
				callback({
					data: data
				});
			}
		});
	},
	multiSelect : false,
	cacheItems : true,
	folderSelect : false,
});
}*/

//zTree初始化树	
function inittree(){
	$.fn.zTree.init($("#menuTree"), providerSetting);
}
var providerSetting = {
		async: {
			enable: true,
			url:"../provider/inittreeprolist",
			autoParam:["id=root"],
			dataType: "json",
			type: "post"
		},
		callback: {
			onClick: zTreeOnCheckProviderData
		}
};

function zTreeOnCheckProviderData(event, treeId, treeNode){
	var id=treeNode.id;
	/*$("#updateid").val(id);
	window.location.href="queryproinfobyid?id="+$("#updateid").val();*/
	if($("#prvgrade").val() =='01'){
		$("#upimg").show();
	}else{
		$("#upimg").hide();
	}
	$.ajax({
	   type: "GET",
	   url: "../provider/queryproinfobyid",
	   data: "id="+id,
	   dataType:"json",
	   success: function(datainfo){
		   cleanpro();
		 $("#prvcode").val(datainfo.proobject.prvcode);
		 $("#prvname1").val(datainfo.proobject.prvname);
		 $("#prvshotname").val(datainfo.proobject.prvshotname);
		 $("#prvgrade").val(datainfo.proobject.prvgrade);
		 $("#prvtype").val(datainfo.proobject.prvtype);
		 $("#linkname").val(datainfo.proobject.linkname);
		 $("#linktel").val(datainfo.proobject.linktel);
		 $("#address").val(datainfo.proobject.address);
		 $("#prvurl").val(datainfo.proobject.prvurl);
		 $("#servictel").val(datainfo.proobject.servictel);
		 $("#channeltype").val(datainfo.proobject.channeltype);//渠道类型
		 $("#province").val(datainfo.proobject.province);
		 getAreaByParentid2($("#province").val(),$("#city"),datainfo.proobject.city); //城市
    	 $("#city").val(datainfo.proobject.city);
    	 $("#companyintroduce").val(datainfo.proobject.companyintroduce);
		 $("#logo").attr("src",datainfo.proobject.logo);
		 /**
		  * 供应商承保配置
		  */
		 $("#quotationtime").val(datainfo.proobject.quotationtime); //报价所需时间
		 $("#quotationinterval").val(datainfo.proobject.quotationinterval); //两次报价间隔时间
		 $("#insuretime").val(datainfo.proobject.insuretime);  //核保所需时间
		 $("#quotationvalidity").val(datainfo.proobject.quotationvalidity);
		 $("#payvalidity").val(datainfo.proobject.payvalidity);
		 
		 /**
		  * type="hidden"
		  */
		 $("#updateid").val(id);
		 $("#saveupdateid").val(datainfo.proobject.id);
		 $("#parentcode").val(datainfo.proobject.parentcode);//上级供应商代码 type="hidden"
		 $("#affiliationorgquery").val(datainfo.proobject.affiliationorgquery);
		 $("#affiliationorg").val(datainfo.proobject.affiliationorg);
		 $("#orderflag").val(datainfo.proobject.orderflag);
		 $("#advancequote").val(datainfo.proobject.advancequote);
		// $("#permissionorg").val(datainfo.proobject.permissionorg);

		 if(datainfo.pro!=null){
			 $("#parentname").val(datainfo.pro.prvname);//上级供应商名称
		 }
		 if(datainfo.rule!=null){
			 $("#rulepostil").val(datainfo.rule.rulePostil);//关联规则 
		 }
		 var bstype=datainfo.proobject.businesstype;  //业务类型
		 if(bstype!=null && bstype.indexOf("1")  > 0){
			 $('#businesstype01').prop('checked', true);
		 }
		 if(bstype!=null && bstype.indexOf("2")  > 0){
			 $('#businesstype02').prop('checked', true);
		 }
		 if(bstype!=null && bstype.indexOf("3")  > 0){
			 $('#businesstype03').prop('checked', true);
		 }
		 if(datainfo.proobject.quickinsureflag=='1'){
			 $("#quickinsureflag").prop('checked', true);//启动快速续保
		 }
		 
	   }
	});
}

function zTreeOnCheck(event, treeId, treeNode) {
	$("#parentcode").val(treeNode.prvcode);
	$("#prvname").val(treeNode.name);
	$('#showpic').modal("hide");
}
function zTreeOnCheckDept(event, treeId, treeNode) {
	$("#affiliationorg").val(treeNode.id);
	$("#deptname").val(treeNode.name);
	$('#showdeptpic').modal("hide");
}
function zTreeOnCheckQueryDept(event, treeId, treeNode) {
	$("#affiliationorgquery").val(treeNode.id);
	$("#querydeptname").val(treeNode.name);
	$('#showquerydeptpic').modal("hide");
}

function cleanpro(){
	 $("#prvcode").val("");
	 $("#prvname1").val("");
	 $("#prvshotname").val("");
	 $("#parentname").val("");//上级供应商名称
	 $("#prvgrade").val("");
	 $("#prvtype").val("");
	 $("#linkname").val("");
	 $("#linktel").val("");
	 $("#address").val("");
	 $("#prvurl").val("");
	 $("#servictel").val("");
	 $("#deptname").val("");//归属机构
	 $("#rulepostil").val("");//关联规则
	 $("#channeltype").val("");//渠道类型
	 $("#province").val("");
	 $("#city").val("");
	 $("#logo").val("");//上传图片??
	 $("#companyintroduce").val("");
	 
	 $("#quotationtime").val(""); //报价所需时间
	 $("#quotationinterval").val(""); //两次报价间隔时间
	 $("#insuretime").val("");  //核保所需时间
	 $("#quotationvalidity").val("");
	 $("#payvalidity").val("");
	 $("#quickinsureflag").prop('checked', false); 
	 $('#businesstype01').prop('checked', false);
	 $('#businesstype02').prop('checked', false);
	 $('#businesstype03').prop('checked', false);
	 
	 /**
	  * type="hidden"
	  */
	 $("#updateid").val("");
	 $("#saveupdateid").val("");
	 $("#parentcode").val("");//上级供应商代码
	 $("#affiliationorgquery").val("");
	 $("#affiliationorg").val("");
	 $("#orderflag").val("");
	 $("#advancequote").val("");
	// $("#permissionorg").val("");
}