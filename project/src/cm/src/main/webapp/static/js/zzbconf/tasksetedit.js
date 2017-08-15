require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn",  "bootstrapdatetimepicker","bootstrapdatetimepickeri18n","jqcookie", "jqtreeview","zTree","zTreecheck","multiselect","public"], function ($) {
	//数据初始化 
	$(function() {
		//返回到列表页面
		$("#go_back").on("click",function(){
			window.location.href="menu2list";
		})
		
		var id = $("#id").val();
		$("#insert_dept").on("click",function(){
			var deptid = $("#deptidc5").find("option:selected").val();
			if(deptid != null && deptid != "" && deptid != "请选择"){
				$.ajax({
					url:'savetasksetscop',
					type:'post',
					data:({tasksetId:id,deptId:deptid}),
					success:function(data){
						if(data.status==1){
							alertmsg(data.message);
							$('#tasksetscop_list').bootstrapTable("refresh",{url:"inittasksetscop?tasksetId="+id});
						}else{
							alertmsg(data.message)
						}
					}
				})          
			}else{
				alertmsg("必须选择到最后网点");
			}
		})	
		$("#delete_dept").on("click",function(){
			//得到选中的id
			var dept_id = $('#tasksetscop_list').bootstrapTable('getSelections');
			if(dept_id.length==0){
				return false;
			}
			var dept_id_array = new Array();
			for(var i=0;i<dept_id.length;i++){
				dept_id_array.push(dept_id[i].deptid);
			}
			$.ajax({
				url:'removetetasksetscop',
				type:'post',
				data:({deptIds:dept_id_array.toString()}),
				success:function(data){
					if(data.status==1){
						alertmsg(data.message);
						$('#tasksetscop_list').bootstrapTable("refresh",{url:"inittasksetscop?tasksetId="+id});
					}else{
						alertmsg(data.message)
					}
				}
			})
		})
		
		
		$('#tasksetscop_list').bootstrapTable({
            method: 'get',
           // url: "inittasksetscop?tasksetId="+id,
            url:"",
            cache: false,
            striped: true,
            pagination: true,
            singleSelect:false,
            sidePagination: 'server', 
            pageSize: '5',
            minimumCountColumns: 2,
            clickToSelect: true,
            columns: [{
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            },  {
                field: 'comname',
                title: '网点名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }]
        });
		
		
		var pid;
		$("#deptidc1").change(function(){
			pid = $("#deptidc1").find("option:selected").val();
			get_dept_data(2,pid);
			$("#deptidc3").children('option').remove();
			$("#deptidc4").children('option').remove();
			$("#deptidc5").children('option').remove();
		})
		$("#deptidc2").change(function(){
			pid = $("#deptidc2").find("option:selected").val();
			$("#deptidc4").children('option').remove();
			$("#deptidc5").children('option').remove();
			get_dept_data(3,pid);
		})
		$("#deptidc3").change(function(){
			pid = $("#deptidc3").find("option:selected").val();
			$("#deptidc5").children('option').remove();
			get_dept_data(4,pid);
		})
		$("#deptidc4").change(function(){
			pid = $("#deptidc4").find("option:selected").val();
			get_dept_data(5,pid);
		})
		
		//下拉框联动
		$("#dept_parent_id").change(function(){
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
		
		
		$("#provider_name").on("click",function(){
			var deptObj = $("#provider_name");
			var deptOffset = $("#provider_name").offset();
			$("#provider").css({left:deptOffset.left + "px", top:deptOffset.top + deptObj.outerHeight() + "px"}).slideDown("fast");
			$("body").bind("mousedown", onBody_provider_Down);
		})
		
		$('.js-multiselect').multiselect({
			right: '#js_multiselect_to_1',
			rightAll: '#js_right_All_1',
			rightSelected: '#js_right_Selected_1',
			leftSelected: '#js_left_Selected_1',
			leftAll: '#js_left_All_1'
		});
		
		var count=1;
		
		//选择供应商
		$("#provider_name").on("click",function(){
			$.fn.zTree.init($("#provider_tree"), provider_setting);
		})
		
		$("#save_group_data").on("click",function(){
			$.ajax({
				url:'savegroupdata',
				type:'post',
				data:$("#group_form").serialize(),
				success:function(data){
					if(data.status=="1"){
						alertmsg(data.message);
						window.location.href="menu2list";
					}else{
						alertmsg(data.message);
					}
				}
			})
		})
	})
	
	function get_dept_data(a,pid){
		$.ajax({
			url:'getdeptbypid',
			type:'post',
			data:({parentId:pid}),
			success:function(data){
				var contend="<option>请选择</option>";
				for(var i=0;i<data.length;i++){
					contend+='<option value="'+data[i].code+'">'+data[i].name+'</option>';
				}
				if(a==2){
					$("#deptidc2").children().remove();
					$("#deptidc2").append(contend);
				}else if(a==3){
					$("#deptidc3").children().remove();
					$("#deptidc3").append(contend);
				}else if(a==4){
					$("#deptidc4").children().remove();
					$("#deptidc4").append(contend);
				}else if(a==5){
					$("#deptidc5").children().remove();
					$("#deptidc5").append(contend);
				}
			}
		})
	}
	
	var provider_setting = {
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "level"
		},
		async: {
			enable: true,
			url:"initprovidertree",
			autoParam:["id","checked"],
			dataFilter: filter,
			otherParam:{"providerId":function(){
				return $("#provider_id").val();
			},"taskSetId":function(){
				return $("#id").val();
			}}
		},
		callback: {
			beforeAsync: beforeAsync,
			onAsyncSuccess: zTreeOnAsyncSuccess,
			onCheck:on_provider_Check
		}
	}; 
	function on_provider_Check(e, treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj("provider_tree"),
		nodes = zTree.getCheckedNodes(true),
		v_id="";
		v = "";
		for (var i=0, l=nodes.length; i<l; i++) {
			v = nodes[i].name;
			v_id = nodes[i].id;
		}
		var providerObjName = $("#provider_name");
		var providerObjId = $("#provider_id");
		providerObjName.attr("value", v);
		providerObjId.attr("value",v_id)
		
		hide_provider_Menu();
	}
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
	function onBody_provider_Down(event) {
		if (!(event.target.id == "menuBtn" || event.target.id == "provider_name" || event.target.id == "provider" || $(event.target).parents("#provider").length>0)) {
			hide_provider_Menu();
		}
	}
	function hide_provider_Menu() {
		$("#provider").fadeOut("fast");
		$("body").unbind("mousedown", onBody_provider_Down);
	}

})