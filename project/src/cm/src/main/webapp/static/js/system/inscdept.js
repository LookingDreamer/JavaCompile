require([ "jquery", "fuelux", "zTree", "zTreecheck", "bootstrap-table", "bootstrap","bootstrapTableZhCn","additionalmethods", "jqvalidatei18n","jqcookie", "jqtreeview","lodash","public" ], function($) {

	$(function() {

		inittree();
		
		$("#addonedept").on("click", function(e) {
			if(!$("#addoneid").val(id)) {

			}
			if(!$("#comcode").val()){
				alertmsg("请选择机构!")
			}
			$("#upcomcode").val($("#comcode").val());
			$("#comcode").val("");
			$("#deptinnercode").val("");
			$("#comname").val("");
			$("#shortname").val("");
			$("#combustype").val("1");
			$("#comtype").val("");
			$("#comgrade").val("");
			$("#rearcomcode").val("");
			$("#province").val("");
			$("#city").val("");
	        $("#county").val("");
			$("#address").val("");
			$("#webaddress").val("");
			$("#zipcode").val("");
			$("#phone").val("");
			$("#fax").val("");
			$("#email").val("");
			$("#satrapname").val("");
			$("#satrapphone").val("");
			$("#satrapcode").val("");
			$("#status").val("0");
			$("#id").val("");

			$("#comcode").removeAttr("readonly");
			$("#deptinnercode").removeAttr("readonly");
			//$("#comname").removeAttr("readonly");
			//$("#shortname").removeAttr("readonly");
		});
//		选择地区
		initArea();
//     默认权限
		tryset();
		formalset();
		channelset();

		$("#deletebutton").on("click", function(e) {
			if (!$("#id").val()) {
				alertmsg("请先选择机构再进行删除!");
				return;
			}
			if(confirm("是否要删除选中机构信息？")){
				$.ajax({
					url : 'checkDept',
					type : 'POST',
					dataType : "json",
					data:{id:$("#id").val()},
					error : function() {
						alertmsg("Connection error");
					},
					success : function(data) {
						if(data) {
							if(!data.dept) {
								alertmsg("机构错误");
								return;
							}
							if(data.subNodeCount && data.subNodeCount > 0) {
								alertmsg("该机构存在子机构，不能删除！");
								return;
							}
							$.ajax({
								url: 'deletbyid',
								type: 'POST',
								dataType: "json",
								data: {id: $("#id").val()},
								error: function () {
									alertmsg("Connection error");
								},
								success: function (data) {
									if (data && data.count && data.count==1) {
										alertmsg("删除机构成功!");
										inittree();

										//		选择地区
										initArea();
										//     默认权限
										tryset();
										formalset();
										channelset();
										$('#orgsaveform')[0].reset()
									}
								}
							});
						} else {
							alertmsg("操作失败！");
						}
					}
				});
			}

		});
		
		$('#myTree').on('selected.fu.tree', function (e, selected) {
			getdatafromid(selected.selected[0].dataAttributes.id);
		});

		$('#myTree').on('updated.fu.tree', function (e, selected) {
			getdatafromid(selected.selected[0].dataAttributes.id);
		});
		
		$('#myTree').on('opened.fu.tree', function (e, info) {
			getdatafromid(info.dataAttributes.id);
		});

		$('#myTree').on('closed.fu.tree', function (e, info) {
			getdatafromid(info.dataAttributes.id);
		});
		
		$("#savebutton").on("click", function(e) {
			//$(this).prop("disabled", true);
			var treeObj = $.fn.zTree.getZTreeObj("deptTree");
			var par = treeObj.getNodeByTId($("#tid").val());
			var ppar = treeObj.getNodeByTId($("#ptid").val());
	 		if($("#orgsaveform").valid()){

				if (valid()) return;
				//$("#orgsaveform").submit();
				$.ajax({
					url : 'updatedept',
					type : 'POST',
					dataType : "json",
					async: false,
					data :$("#orgsaveform").serialize(),
					error : function() {
						alertmsg("Connection error");
					},
					success : function(data) {
						if(data) {
							if(data.flag && data.flag == "success") {
								if (!$("#id").val()) {
									$("#id").val(data.dept.id);
									$("#childflag").val(0);
									treeObj.reAsyncChildNodes(ppar, "refresh");
									treeObj.reAsyncChildNodes(par, "refresh");
									var ret = treeObj.expandNode(par, true, true, true);
								}
								//var node = treeObj.getNodeByParam("id", data.dept.id, null);
								//treeObj.selectNode(node,false,false);
								alertmsg("保存机构信息成功！");

							} else {
								alertmsg("操作失败！");
							}
						} else {
							alertmsg("操作失败！");
						}
					}
				});
			}
		});
		function valid() {
			if (!$("#upcomcode").val() && "1200000000"!=$("#comcode").val()) {
				alertmsg("机构上级代码不能为空！");
				return true;
			}
			if (!$("#comcode").val()) {
				alertmsg("机构代码不能为空！");
				return true;
			}
			if (!$("#deptinnercode").val()) {
				alertmsg("机构内部编码不能为空！");
				return true;
			}
			if (!$("#comname").val()) {
				alertmsg("机构名称不能为空！");
				return true;
			}
			if (!$("#shortname").val()) {
				alertmsg("机构简称不能为空！");
				return true;
			}
			if (!$("#combustype").val()) {
				alertmsg("机构业务类型不能为空！");
				return true;
			}
			if (!$("#comtype").val()) {
				alertmsg("机构类型不能为空！");
				return true;
			}
			if (!$("#comgrade").val() && "1200000000"!=$("#comcode").val()) {
				alertmsg("机构级别不能为空！");
				return true;
			}
			if (!$("#province").val() || !$("#city").val() || !$("#county").val()) {
				alertmsg("所在省、市、县不能为空！");
				return true;
			}
			if ($.trim($("#comcode").val()).length != 10) {//去除首尾空格
				alertmsg("机构代码必须为10位！");
				return true;
			}
			if ($("#comtype").val() == "02" && !/^\d{5}$/.test($("#deptinnercode").val())) {
				alertmsg("机构类型为平台，机构内部编码必须为5位数字！");
				return true;
			}
			if (!/^\d+$/.test($("#deptinnercode").val())) {
				alertmsg("机构内部编码必须为数字！");
				return true;
			}
 			var notExist = false;
			$.ajax({
				url : 'checkNewDept',
				type : 'POST',
				dataType : "json",
				async: false,
				data:{id:$("#id").val(),comcode:$("#comcode").val(),deptinnercode:$("#deptinnercode").val(),upcomcode:$("#upcomcode").val()},
				error : function() {
					alertmsg("Connection error");
				},
				success : function(data) {
					if(data) {
						if(data.success == false) {
							alertmsg(data.msg);
							return;
						} else {
							notExist = true;
						}
					} else {
						alertmsg("操作失败！");
					}
				}
			});
			if (!notExist) {
				return true;
			}
			return false;
		}

		$("#orgsaveform").validate({
				errorLabelContainer : ".alert-danger",
				errorElement : "p",
		        errorClass : "text-left",
		        focusInvalid : false,
		        /*rules: {
					upcomcode:"required"
		        },
				rules: {
					comcode:"required"
				},
			rules: {
				deptinnercode:"required"
			},
			rules: {
				comname:"required"
			},
			rules: {
				shortname:"required"
			},
			rules: {
				combustype:"required"
			},
			rules: {
				comtype:"required"
			},
		        messages: {
					upcomcode:{required: "机构上级代码不能为空"},
					comcode:{required: "机构代码不能为空"},
					comcode:{required: "机构内部编码为空"},
					comcode:{required: "机构名称不能为空"},
					comcode:{required: "机构简称不能为空"},
					comcode:{required: "机构业务类型不能为空"},
					comcode:{required: "机构类型不能为空"},
		        }*/
		 });
	});

});

function initArea(){
	getAreaByParentid("0",$("#province"));
}
function changeprv(){
	 getAreaByParentid($("#province").val(),$("#city"));
}
function changecity(){
	 getAreaByParentid($("#city").val(),$("#county"));
}
function tryset(){
	getDefaultPermission($("#comcode").val(),$("#tryset"),1);
}
function formalset(){
	getDefaultPermission($("#comcode").val(),$("#formalset"),2);
}
function channelset(){
	getDefaultPermission($("#comcode").val(),$("#channelset"),3);
}

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
function getDefaultPermission(comcode,selectobject,value){
	$.ajax({
		url : "../dept/querybyparentcodes",
		type : 'GET',
		dataType : "json",
		data : {
			deptid:comcode,
	        agentkind:value
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			selectobject.empty();
			selectobject.append("<option value=' '>"+"请选择"+"</option>");
			for (var i = 0; i < data.length; i++) {
				selectobject.append("<option value='"+data[i].setcode+"'>"+data[i].setname+"</option>");   
			}
			/*if(data.length>0){
				selectobject.get(0).selectedIndex=0;
			}*/
			selectobject.trigger("change");
		}
	});
}
function getDefaultPermission2(comcode,selectobject,type,value){
	$.ajax({
		url : "../dept/querybyparentcodes",
		type : 'GET',
		dataType : "json",
		data : {
			deptid:comcode,
	        agentkind:type
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			selectobject.empty(); 
			selectobject.append("<option value=' '>"+"请选择"+"</option>");
			for (var i = 0; i < data.length; i++) {		
				if(data[i].setcode == value){
					selectobject.append("<option selected='selected' value='"+data[i].setcode+"'>"+data[i].setname+"</option>");
				}else{
					selectobject.append("<option value='"+data[i].setcode+"'>"+data[i].setname+"</option>");
				}
			}
			/*if(data.length>0){
				selectobject.get(0).selectedIndex=0;
			}*/
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
			//selectobject.trigger("change");
		}
	});
}
/*function getdatafromid(id){*/
function getdatafromid(event,treeId,treeNode){
	var id=treeNode.id;
	$("#tid").val(treeNode.tId);
	$("#ptid").val(treeNode.parentTId);
	$("#addoneid").val(id);
	$("#deldept").show();
	$.ajax({
	   type: "POST",
	   url: "../dept/querybyid",
	   data: "id="+id,
	   dataType:"json",
	   success: function(datainfo){
		 $("#id").val(datainfo.orgobject.id);
		 $("#upcomcode").val(datainfo.orgobject.upcomcode);
	     $("#comcode").val(datainfo.orgobject.comcode);
	     $("#deptinnercode").val(datainfo.orgobject.deptinnercode);
	     $("#comname").val(datainfo.orgobject.comname);
		 $("#shortname").val(datainfo.orgobject.shortname);
		 $("#combustype").val(datainfo.orgobject.combustype);
	     $("#comtype").val(datainfo.orgobject.comtype);
	     $("#comgrade").val(datainfo.orgobject.comgrade);
	     $("#rearcomcode").val(datainfo.orgobject.rearcomcode);
    	 $("#province").val(datainfo.orgobject.province);
    	 getAreaByParentid2($("#province").val(),$("#city"),datainfo.orgobject.city);
    	 getAreaByParentid2(datainfo.orgobject.city,$("#county"),datainfo.orgobject.county);
         $("#city").val(datainfo.orgobject.city);
         $("#county").val(datainfo.orgobject.county);
    	 $("#address").val(datainfo.orgobject.address);
	     $("#webaddress").val(datainfo.orgobject.webaddress);
	     $("#zipcode").val(datainfo.orgobject.zipcode);
	     $("#phone").val(datainfo.orgobject.phone);
	     $("#fax").val(datainfo.orgobject.fax);
	     $("#email").val(datainfo.orgobject.email);
	     $("#satrapname").val(datainfo.orgobject.satrapname);
	     $("#satrapphone").val(datainfo.orgobject.satrapphone);
	     $("#satrapcode").val(datainfo.orgobject.satrapcode);
	     $("#status").val(datainfo.orgobject.status);
	     $("#parentcodes").val(datainfo.orgobject.parentcodes);
		   $("#childflag").val(datainfo.orgobject.childflag);

	     if(datainfo.orgobject.tryset!=null){
	    	 $("#tryset").val(datainfo.orgobject.tryset);  	 
	     }
	     if(datainfo.orgobject.formalset!=null){
	    	 $("#formalset").val(datainfo.orgobject.formalset);
	     }
	     if(datainfo.orgobject.channelset!=null){
	    	 $("#channelset").val(datainfo.orgobject.channelset); 
	     }
	     getDefaultPermission2($("#comcode").val(),$("#tryset"),1,datainfo.orgobject.tryset);
	     getDefaultPermission2($("#comcode").val(),$("#formalset"),2,datainfo.orgobject.formalset);
	     getDefaultPermission2($("#comcode").val(),$("#channelset"),3,datainfo.orgobject.channelset);
	 	 /*getDefaultPermission($("#comcode").val(),$("#tryset"),1);
	     getDefaultPermission($("#comcode").val(),$("#formalset"),2);
	   	 getDefaultPermission($("#comcode").val(),$("#channelset"),3);*/

		   $("#comcode").attr("readonly","readonly");
		   $("#deptinnercode").attr("readonly","readonly");
		   //$("#comname").attr("readonly","readonly");
		   //$("#shortname").attr("readonly","readonly");
	   }
	});
}

/* zTree初始化数据 */
function inittree() {
	$.fn.zTree.init($("#deptTree"), deptSetting);
}
var deptSetting = {
		async: {
			enable: true,
			url: "../dept/inscdeptlist",
			autoParam: ["id=root"]
		},
		/*check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},*/
		callback: {
			onClick: getdatafromid
		}
};
/* bootstrap-tree初始化数据 */
/*function inittree() {
	$('#myTree').tree({
	dataSource: function (options, callback) {
		var parentcode ="";
		var id = "";
		if(!jQuery.isEmptyObject(options)){
//			console.log('options:', options);
//			console.log(options.dataAttributes.parentcode);
			parentcode =options.dataAttributes.parentcode;
			id = options.dataAttributes.id;
		}
		
		$.ajax({
			type : "POST",
			url : "../dept/inscdeptlist",
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

