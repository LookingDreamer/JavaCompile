require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "bootstrapdatetimepicker","bootstrapdatetimepickeri18n","jqcookie", "jqtreeview","zTree","zTreecheck","zTreeexedit","public"], function ($) {
	//数据初始化 
	$(function() {
		set_id=$("#permissionsetId").val();
		 
		is_try=$("#agentkind").find("option:selected").val();
		//点击保存功能包（带回标记当前是新增还是修改）
		 
		if(set_id != ''){
			$('#agentkind').attr("disabled",true); 
			$('#deptname').attr('disabled',true);
		}
		
		$("#go_back").on("click",function(){
			window.location.href="menu2list";
		})
		$("#save_permisssionset").on("click",function(){
			if(!$("#setname").val()){
				alertmsg("权限包名称不能为空");
				return;
			}
			if(!$("#setcode").val()){
				alertmsg("权限包代码不能为空");
				return;
			}
			if(!$("#deptname").val()){
				alertmsg("所属机构不能为空");
				return;
			}
			if(!$("#agentkind").val()){
				alertmsg("用户类型不能为空");
				return;
			}
			$.ajax({
				type:"post",
        		url:"checksetcode",
        		data:{
        			setcode:function(){return $("#setcode").val();},
        			id:function(){return $("#permissionsetId").val()}
        		},
        		error : function() {
        			alertmsg("Connection error");
        		},
        		success : function(res) {
        			//alet();
        			if(res==false){
        				if(!$("#setcode").val()){
        					alertmsg("权限包代码不能为空");
        				}else{
        					alertmsg("权限包代码已存在");
        				}
        			}else{
        				$.ajax({
        					url:'savebasepermissionset',
        					type:"post",
        					data:$("#permission_form").serialize(),
        					 success: function(data){
        						 if(data.length>0){
        							 $("#permissionsetId").attr("value",data);
									 set_id = data;
									 reloaddata();
        							 alertmsg("保存成功");
        							 $('#agentkind').attr("disabled",true); 
        							 $('#deptname').attr('disabled',true);
        						 }else{
        							 alertmsg("保存失败，请稍后重试！");
        						 }
        					 }
        				})
        			}
        		}
				
			})
		})
		
		$("#agentkind").on("change",function(){
			reloaddata();
		});
		
		
		//功能包（用来保存权限代理人关系表信息）   供应商 保存
		$("#save_permisssion_provider").on("click",function(){
			
			var op_flag=$("#op_flag").val();
			//得到选中的供应商code(未选中进行提示)
			var data = $('#table-agreement').bootstrapTable('getSelections');
			var checkedIds ='';
		    for(var i=0; i<data.length; i++){
		        checkedIds += data[i].id + ',';
		    }  
		    if(checkedIds.length > 0){  
		        checkedIds = checkedIds.substring(0, checkedIds.length-1);  
		    }  
		    //保存功能包 权限 供应商关系
		    $.ajax({
		    	url:'savesetproviderallotdata',
		    	type:'post',
		    	data:({providerIds:checkedIds,setId:set_id,opFlag:op_flag}),
		    	success:function(data){
		    		if(data.status=="1"){
		    			alertmsg(data.message);
		    		}else{
		    			alertmsg(data.message);
		    		}
		    	}
		    })
		})
		
		$.fn.zTree.init($("#provider_tree"), provider_setting);
		
		//弹出机构树
		$("#deptname").on("click",function(){
			$.fn.zTree.init($("#dept_tree"), deptsetting);
			$("#myModal_dept").removeData("bs.modal");
			$("#myModal_dept").modal({
				 show: true
			});
			
		})
		//权限列表 编辑权限分配信息	//+"&istry="+is_try
		$('#permisssion_list').bootstrapTable({
            method: 'get',
        	singleSelect:false,
            url: "initpermissionlistpage?permissionsetId="+set_id,
            cache: false,
            striped: true,
            pagination: true,
            sidePagination: 'server', 
            pageSize: '10',
            minimumCountColumns: 2,
            clickToSelect: false,
            columns: [{
                field: 'permissionname',
                title: '功能名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'functionstatestr',
                title: '功能状态',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
				field: 'num',
				title: '限制次数',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'warningtimes',
				title: '预警次数',
				align: 'center',
				valign: 'middle',
				sortable: true,
				formatter :function (value,row,index){
					if(value ==null){
						return '无';
					} else {
						return value;
					}
				}
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
		$('#table-agreement').bootstrapTable({
			method: 'get',
			url: "getagreementbycomcode?comcode="+$("#deptid").val()+"&setid="+$("#permissionsetId").val(),
			cache: false,
			striped: true,
			//pagination: false,
			sidePagination: 'server',
			//pageSize: pagesize,
			//pageList: [5, 10],
			minimumCountColumns: 2,
			clickToSelect: true,
			singleSelect : false,
			columns : [ {
				field : 'state',
				align : 'center',
				valign : 'middle',
				checkbox : true
			}, {
				field : 'id',
				title : '协议ID',
				visible : false,
				switchable : false
			}, {
				field : 'renewalenable',
				title : 'renewalenable',
				visible : false,
				switchable : false
			}, {
				field : 'agreementcode',
				title : '协议编码',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'agreementname',
				title : '协议名称',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'providerid',
				title : '关联供应商',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'deptid',
				title : '关联机构',
				align : 'center',
				align : 'center',
				valign : 'middle',
				sortable : true
			}, {
				field : 'modifytime',
				title : '更新时间',
				align : 'center',
				valign : 'middle',
				sortable : true,
				formatter : dateFormatter
			}, {
				field : 'agreementstatus',
				title : '状态',
				align : 'center',
				valign : 'middle',
				sortable : true
			}],
			onLoadSuccess: function (data) {
				$("#table-agreement").bootstrapTable("checkAll");

				if ($("#permissionsetId").val() && "add" != $("#op_flag").val()) {
					$("#table-agreement").bootstrapTable("uncheckAll");
					$("#table-agreement").bootstrapTable("checkBy", {field:"renewalenable", values:[0]});
				}
				return false;
			},
		});

		//权限包关联用户列表
		$('#user_list').bootstrapTable({
			method: 'get',
			singleSelect:false,
			url: "initpermissionsetUser?permissionsetId="+set_id+"&agentkind="+is_try,
			cache: false,
			striped: true,
			pagination: true,
			sidePagination: 'server',
			pageSize: '10',
			minimumCountColumns: 2,
			clickToSelect: true,
			columns: [
				/*{
				field: 'state',
				align: 'center',
				valign: 'middle',
				checkbox: true
			}, */
				{
				field: 'rownum',
				title: '序号',
				align: 'center',
				valign: 'middle',
				sortable: true
			},{
				field: 'jobnum',
				title: '代理人编码',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'openid',
				title: 'openID',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'name',
				title: '姓名',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'comname',
				title: '所属机构',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'idno',
				title: '身份证号',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'agentstatus',
				title: '状态',
				align: 'center',
				valign: 'middle',
				sortable: true
			}, {
				field: 'agentkind',
				title: '用户类型',
				align: 'center',
				valign: 'middle',
				sortable: true
			}]
		});

		$("#userquerybutton").on("click", reloaduserdata);

	});
});	

//添加编辑
function operateFormatter(value, row, index) {
    return [
        '<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
        '<i class="glyphicon glyphicon-edit"></i>',
        '</a>'
    ].join('');
}
//弹出编辑窗带参数 id
window.operateEvents = {
    'click .edit': function (e, value, row, index) {
    	var id="" ;
    	var pName="";
    	//角色分配表id
    	if(row.allotid!=undefined){
    		id = row.allotid;
    	}
    	if(row.permissionname!=undefined){
    		pName = row.permissionname;
    	}
    	 $('#permissionallot_modal').removeData('bs.modal');
    	 
    	 //功能包id
    	 set_id = $("#permissionsetId").val();
    	 if(set_id==""){
    		 alertmsg("请先保存功能包基础信息");
    		 return false;
    	 }else{
    		 $("#permissionallot_modal").modal({
    				show: true,
    				remote: 'permissionset2allot?id='+id+'&setId='+set_id+'&pId='+row.id+'&pName='+pName//权限id
    			});
    	 }
    	
    	
    }
};
var provider_setting = {
		view: {
			selectedMulti: true
		},
		async: {
			enable: true,
			url:"initprovidertree",
			autoParam:["id","checked"],
			dataFilter: filter,
			otherParam:{"permissionSetId":function(){
				return set_id;
			}} 
		},
		check: {
			enable: true,
			autoCheckTrigger: true,
			chkboxType:{  "Y" : "", "N" : "ps"}
		},callback: {
			beforeAsync: beforeAsync,
			onAsyncSuccess: zTreeOnAsyncSuccess
		}
	};

function zTreeOnAsyncSuccess(event, treeId, msg) { 
          try { 
        	  var treeObj = $.fn.zTree.getZTreeObj('provider_tree');  
                 //调用默认展开第一个结点 
        	  	var nodes = treeObj.getCheckedNodes(true); 
                 for(var i=0;i<nodes.length;i++ ){
                	 treeObj.expandNode(nodes[i], true); 
                	 var childNodes = treeObj.transformToArray(nodes[i]);
                	 for(var j=0;j<childNodes.length;j++){
                		 treeObj.expandNode(childNodes[1], true);
                	 }
                 }
           } catch (err) { 
        	   
           } 
} 
//得到所有已经选中的节点
function beforeAsync(treeId, treeNode) {
	return true;
}

function filter(treeId, parentNode, childNodes) {
	if (!childNodes) return null;
	for (var i=0, l=childNodes.length; i<l; i++) {
		childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
	}
	return childNodes;
}
var deptsetting = {
		async: {
			enable: true,
//			url:"initdepttree",
			url:"initDeptTreeByAgent",
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
			onCheck: deptTreeOnCheck
		}
	};

function deptTreeOnCheck(event, treeId, treeNode) {
	$("#deptid").val(treeNode.id);
	$("#deptname").val(treeNode.name);
	$('#myModal_dept').modal("hide");
	$('#table-agreement').bootstrapTable('refresh', {url : "getagreementbycomcode?comcode=" + $("#deptid").val()+"&setid="+$("#permissionsetId").val()});
}
function reloaddata(){
	$.ajax({
		url : 'initpermissionlistpage',
		type : 'GET',
		dataType : "json",
		data:({limit:10,offset:0,permissionsetId:set_id}),
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#permisssion_list').bootstrapTable('load', data);
		}
	});
	reloaduserdata();
}

function reloaduserdata(){
	$.ajax({
		url : 'initpermissionsetUser',
		type : 'GET',
		dataType : "json",
		data:({limit:10,offset:0,permissionsetId:set_id,jobnum:$("#usercode").val(),name:$("#username").val(),agentkind:is_try}),
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#user_list').bootstrapTable('load', data);
		}
	});
}

