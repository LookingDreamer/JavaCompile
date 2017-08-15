require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn", "jqvalidatei18n", "bootstrapdatetimepicker","bootstrapdatetimepickeri18n","jqcookie", "jqtreeview","zTree","zTreecheck","multiselect","public"], function ($) {
    //数据初始化
    $(function() {

        $("#groupDeptOrgName").on("click",function(){
            $("#myModal_dept").removeData("bs.modal");
            $("#myModal_dept").modal({
                show: true
            });
            $.fn.zTree.init($("#dept_tree"), deptsetting);
            //重选机构时清空供应商相关数据
            $.fn.zTree.destroy("provider_tree");
            $("#groupprovdeids").val("");
            $("#groupprovde").val("");
        })
        
        //新增供应商列表
        $("#groupprovde").on("click",function(){
        	
        	var groupDeptOrgName=$("#groupDeptOrgName").val();
        	if(groupDeptOrgName!=""){
        		var treeObj=$.fn.zTree.getZTreeObj("provider_tree");
        		var nodes = [{"id":1,"name":"所有供应商","isParent":true,"chkDisabled":true,"pId":"0"}];
        		if(treeObj != null){
        			nodes=treeObj.getNodes();
        		}
        		$("#myModal_group").removeData("bs.modal");
        		$("#myModal_group").modal({
        			show: true
        		});
        		treeObj = $.fn.zTree.init($("#provider_tree"), groupsetting, nodes);
        	}else{
        		alertmsg("需要选择所属平台才能新增供应商");
        	}
           
        })

        //保存群组基本数据
//		$("#save_base_data_button").on("click",function(){
//			$.ajax({
//				url:'savebasegroupdata',
//				data:$("#form_group_base_data").serialize(),
//				type:'post',
//				success:function(data){
//					alertmsg(data.message);
//				}
//			})
//			
//		})		
        $("#form_group_base_data").validate({
            errorLabelContainer : ".alert-danger",
            errorElement : "p",
            errorClass : "text-left",
            focusInvalid : false,
            rules: {
                groupcode:{
                    required:true
                }
            },
            messages: {
                groupcode:{
                    required: "群组编码不能为空"
                }
            }
        });
        $("#save_base_data_button").on("click", function(e) {
            var myDept = $("#organizationid").val();
            if(myDept==""){
                alert("请选择组织机构");
                return false ;
            }
            var group_kind = $("#groupkind").val();
            if(group_kind=="0"){
                alert("请选择群组类型");
                return false ;
            }
            var allOpts = $("#js_multiselect_to_1 option");
            var tasktype="";
            for(i=0;i<allOpts.length;i++){
                if(i==0){
                    tasktype+=$(allOpts[i]).val();
                }else{
                    tasktype+=","+$(allOpts[i]).val();
                }
            }
            $("#tasktype").val(tasktype);
            if($("#form_group_base_data").valid()){
                $("#form_group_base_data").submit();
            }
        })

        //初始化新增群组
        $("#add_base_data_button").on("click",function(){
            $('#groupname').val("");
            $('#groupnum').val("0");
            $('#id').val("");
            $('#groupkind').val("0");
            $('#groupcode').val("");
            $('#groupDeptOrgName').val("");
            $('#workload').val("");
        })



        //新增成员
        $("#add_members").on("click",function(){

            if(group_id==""){
                alert("请先保存群组基本信息");
                return false;
            }

            $('#myModal_group_member_add').removeData("bs.modal");
            $("#myModal_group_member_add").modal({
                show: true,
                remote: 'group2memberadd?gropId='+group_id
            });
        })

        //移除群组成员
        $("#remove_members").on("click",function(){

            var data = $('#group_member_list').bootstrapTable('getSelections');
            if(data.length == 0){
                alertmsg("至少选择一行数据！");
                return false;
            }
            if(window.confirm("确定要删除吗？")==true){
                //得到选中的id
                var data = $('#group_member_list').bootstrapTable('getSelections');
                var idArray = new Array();
                for(var i=0;i<data.length;i++){
                    idArray.push(data[i].id);
                }
                $.ajax({
                    url:'deletegroupmember',
                    type:'post',
                    data:({ids:idArray.toString(),groupId:group_id}),
                    success:function(data){
                        if(1==data.status){
                            alertmsg(data.message);
                            var group_id = $("#id").val();
                            $('#group_member_list').bootstrapTable(
                                'refresh',{url:'initgroupmemberlist?groupId='+group_id});
                        }else{
                            alertmsg(data.message);
                        }
                    }

                })
            }
        })

        //初始化成员列表
        var group_id = $("#id").val();
        $('#group_member_list').bootstrapTable({
            method: 'get',
            url: "initgroupmemberlist?groupId="+group_id,
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
                field: 'usercode',
                title: '管理员账号',
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
                field: 'userorganization',
                title: '所属层级',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'groupprivilegestr',
                title: '任务权限',
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
        $('#group_wang_list').bootstrapTable({
            method: 'get',
            url: "initgroupdeptidlist?groupId="+group_id,
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
                field: 'id',
                title: 'id',
                align: 'center',
                valign: 'middle',
                visible : false,
                sortable: true
            }, {
                field: 'deptid',
                title: '网点',
                align: 'center',
                valign: 'middle',
                sortable: true
            }]
        });
        $('#group_provider_list').bootstrapTable({
            method: 'get',
            url: "initgroupproviderlist?groupId="+group_id,
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
            },{
                field: 'id',
                title: 'id',
                align: 'center',
                valign: 'middle',
                visible : false,
                sortable: true
            },  {
                field: 'prvcode',
                title: '供应商代码',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'prvname',
                title: '供应商名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }]
        });

        var count=1;
        $('.form_datetime').datetimepicker({
            language: 'zh-CN',
            format: 'hh:ii',
            startView:1
        });

        //增加一行新工作时间
        $("#add_new_time").on("click",function(){
            var new_tr = '<tr ><td class="col-md-2"><select class="form-control" name="startworkdate"><option value="1">星期一</option>	<option value="2">星期二</option><option value="3">星期三</option><option value="4">星期四</option><option value="5">星期五</option><option value="6">星期六</option><option value="7">星期日</option></select></td><td  style="vertical-align: middle;" >到</td><td class="col-md-2" ><select class="form-control"  name="endworkdate"><option value="1">星期一</option><option value="2">星期二</option><option value="3">星期三</option><option value="4">星期四</option><option value="5">星期五</option><option value="6">星期六</option><option value="7">星期日</option></select></td><td class="col-md-2" ><input size="10"   type="text" class=" form-control date form_datetime"	name="startworktime" readonly></td><td  style="vertical-align: middle;">至</td><td class="col-md-2" ><input  type="text"  class="form-control date form_datetime" name="endworktime" readonly> </td>	<td class="col-md-2"><button type="button" onclick="delete_row(this)"  class="btn btn-primary">删除</button></td></tr>';
            $("#tbl_time").append(new_tr);
            $('.form_datetime').datetimepicker({
                language: 'zh-CN',
                format: 'hh:ii',
                startView:1
            });
        })


        //返回到列表页面
        $("#go_back").on("click",function(){
            window.location.href="menu2list";
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
        //初始化最高级供应商
        $("#provider").on("click",function(){
            $.ajax({
                url:'../provider/getgroupprovidedata',
                type:'GET',
                dataType: "json",
                data:{},
                success:function(data){
                    var providerList = data.providerList;
                    var opd="";
                    for(var i=0;i<providerList.length;i++){
                        opd =opd+ '<option value="'+providerList[i].id+'">'+providerList[i].prvname+'</option>';
                    }
                    //每次选择供应商 清除下拉框信息
                    $("#groupprovde").children().remove();

                    $("#groupprovde").append(opd);
                }
            })
        })

        //选择确定管理机构
        $("#show_selected_dept").on("click",function(){
            //得到选中的供应商code(未选中进行提示)
            var treeObj = $.fn.zTree.getZTreeObj('dept_tree');
            var nodes = treeObj.getCheckedNodes(true);
            var checkedIds = '';
            for(var i=0; i<nodes.length; i++){
                checkedIds += nodes[i].id + ',';
            }
            if(checkedIds.length > 0){
                checkedIds = checkedIds.substring(0, checkedIds.length-1);
            }

        })

        $('.js-multiselect').multiselect({
            right: '#js_multiselect_to_1',
            rightAll: '#js_right_All_1',
            rightSelected: '#js_right_Selected_1',
            leftSelected: '#js_left_Selected_1',
            leftAll: '#js_left_All_1'
        });
        deptconnSelect(0);
    })
});


function showdiv(shortdivname) {
    $('#userdiv').hide();
    $('#wangdiv').hide();
    $('#providerdiv').hide();
    $("#user").removeClass();
    $("#wang").removeClass();
    $("#provider").removeClass();
    $("#" + shortdivname).addClass("active");
    $("#" + shortdivname + "div").show();
}
function show_provider_menu() {
    var deptObj = $("#provider_name");
    var deptOffset = $("#provider_name").offset();
    $("#provider").css({left:deptOffset.left + "px", top:deptOffset.top + deptObj.outerHeight() + "px"}).slideDown("fast");
    $("body").bind("mousedown", onBody_provider_Down);
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




var deptsetting = {
    async: {
        enable: true,
        url:"initdepttree11",
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



//群组新增供应商
var groupsetting = {
    async: {
        enable: true,
        url:groupTreeGetOrgId,
        dataType: "json",
        type: "post"
    },
    check: {
        enable: true,
        chkStyle: "checkbox",
    },
    callback: {
    	onAsyncSuccess:groupTreeOnAsyncSuccess,
    	onCheck: groupTreeOnCheck
    }
};


//请求后台获取供应商的url
function groupTreeGetOrgId(){
	var treeObj = $.fn.zTree.getZTreeObj('provider_tree');
    var nodes = treeObj.getCheckedNodes(true);
    var checked = nodes.length;
	var prefix = "initgroupprovider?"
	var organizationid = $("#organizationid").val();
	var groupId = $("#id").val();
	if(checked != 0){
		return prefix+"&organizationid="+organizationid+"&id="+groupId+"&checked="+checked;
	}
	return prefix+"&organizationid="+organizationid+"&id="+groupId;
}

function deptTreeOnCheck(event, treeId, treeNode) {
    $("#organizationid").val(treeNode.id);
    $("#groupDeptOrgName").val(treeNode.name);
    $('#myModal_dept').modal("hide");
}

//父级菜单点击事件

//异步成功时,获取所有ID
function groupTreeOnAsyncSuccess(event,treeId,treeNode){
	var treeObj=$.fn.zTree.getZTreeObj("provider_tree");
    var nodes=treeObj.getCheckedNodes(true);
    //没有选中时清空所有选项
    if(nodes.length==0){
    	$("#groupprovdeids").val("");
    	$("#groupprovde").val("");
    	return;
    };
    var ids = "";
	var names = "";
    for(var i=0;i<nodes.length;i++){
    	//把顶级菜单去掉
    	if(nodes[i].id==1){
    		continue;
    	}
    	if(i == nodes.length-1){
    		ids+=nodes[i].id;
    		$("#groupprovdeids").val(ids);
    		names+=nodes[i].name;
    		$("#groupprovde").val(names);
    	}else{
    		ids+=nodes[i].id+",";
    		$("#groupprovdeids").val(ids);
    		names+=nodes[i].name + ",";
    		$("#groupprovde").val(names);
    	}
    }
}
//选择时要改变值
var count = 0;//全选反选控制变量
function groupTreeOnCheck(event,treeId,treeNode){
	var treeObj=$.fn.zTree.getZTreeObj("provider_tree");
	var nodes=treeObj.getCheckedNodes(true);
	var unnodes=treeObj.getCheckedNodes(false);
	var unchkl = unnodes.length;
	//当被改变的是父节点
	if(1==treeNode.id){
		if(unchkl > 0 & count !=0){
			if(!treeNode.checked){
				$("#provider_tree_1_check,.checkbox_true_part_focus").trigger("click");
				//如果没有点子节点就一直为0,保持全选后点击时反选
				count = 0;
				//重新初始化
				treeObj=$.fn.zTree.getZTreeObj("provider_tree");
				nodes=treeObj.getCheckedNodes(true);
			};
		};
	}else{
		count +=1;
	}
    //没有选中时清空所有选项
    if(0==nodes.length){
    	$("#groupprovdeids").val("");
    	$("#groupprovde").val("");
    	return;
    };
    var ids = "";
	var names = "";
    for(var i=0;i<nodes.length;i++){
    	//把顶级菜单去掉
    	if(nodes[i].id==1){
    		continue;
    	}
    	if(i == nodes.length-1){
    		ids+=nodes[i].id;
    		$("#groupprovdeids").val(ids);
    		names+=nodes[i].name;
    		$("#groupprovde").val(names);
    	}else{
    		ids+=nodes[i].id+",";
    		$("#groupprovdeids").val(ids);
    		names+=nodes[i].name + ",";
    		$("#groupprovde").val(names);
    	}
    }
//    $(".checkbox_true_part").css("background-position","0 -28px");
//    $(".checkbox_true_part_focus").css("background-position","0 -42px");
}

function groupprovideadd(){
    var groupid=$("#id").val();
    if(groupid==""){
        alert("请先保存群组基本信息");
        return false;
    }
    var provideids=$("#groupprovdeids").val();
    $.ajax({
        url:'savegroupprovidedata',
        type:'POST',
        dataType: "json",
        data:{
            groupid:groupid,
            provideids:provideids
        },
        success:function(data){
            if (1==data.status) {
                alertmsg(data.message);
                var groupid = $("#id").val();
                $('#group_provider_list').bootstrapTable(
                    'refresh',{url:'initgroupproviderlist?groupId='+groupid});
                return;
            }
            alertmsg(data.message);
        }
    })
    $("#groupprovdeids").val("");
    $('#myModal_group').modal('hide');
}

function getSelectedRow() {
    var data = $('#group_provider_list').bootstrapTable('getSelections');
    if (data.length == 0) {
        alertmsg("请选择！");
        return ;
    }
    var idArray = new Array();
    for(var i=0;i<data.length;i++){
        idArray.push(data[i].id);
    }
    return idArray.toString();
}
var total ="";
//新增供应商
function add_provider(){
	//清空供应商信息
	$("#groupprovdeids").val("");
    $("#groupprovde").val("");
    
	var groupDeptOrgName=$("#groupDeptOrgName").val();
	if(groupDeptOrgName!=""){
		var treeObj=$.fn.zTree.getZTreeObj("provider_tree");
		var nodes = [{"id":1,"name":"所有供应商","isParent":true,"chkDisabled":false,"pId":"0"}];
//		if(treeObj != null){
//			nodes=treeObj.getNodes();
//		}
		$("#myModal_group").removeData("bs.modal");
		$("#myModal_group").modal({
			show: true
		});
		treeObj = $.fn.zTree.init($("#provider_tree"), groupsetting, nodes);
		$("#provider_tree_1_switch").trigger("click");
	}else{
		alertmsg("需要选择所属平台才能新增供应商");
	}
}

function remove_provider(){
    var groupid=$("#id").val();
    var ids=getSelectedRow();
    if (ids==null) {
        return;
    }
    if(window.confirm("确定要删除吗？")==true){
        $.ajax({
            url:'deletegroupprovide',
            type:'post',
            data:{ids:ids,groupid:groupid},
            success:function(data){
                if(1==data.status){
                    alertmsg(data.message);
                    var groupid = $("#id").val();
                    $('#group_provider_list').bootstrapTable(
                        'refresh',{url:'initgroupproviderlist?groupId='+groupid});
                }else{
                    alertmsg(data.message);
                }
            }

        })
    }
}
function ononon(){
//	alertmsg("haoba");
}
function deptconnchange(numb) {
    if (numb > 4) {
        return;
    }
//	var partid = "deptidc" + numb;
    for (var int = numb; int < 5; int++) {
        var nextid = "deptidc" + (int + 1);
        $("#" + nextid).empty();
    }
    var nextid = "deptidc" + (numb + 1);
    $("#" + nextid).append("<option value='0'>请选择</option>")
    deptconnSelect(numb);
}
function deptconnSelect(numb) {
    $.ajax({
        url : "../provider/querydepttree",
        type : 'POST',
        dataType : "json",
        data : {
            id : $("#deptidc" + numb).val()
        },
        async : true,
        error : function() {
            alertmsg("Connection error");
        },
        success : function(data) {
            for (var int = 0; int < data.length; int++) {
                $("#" + "deptidc" + (numb+1)).append(
                        "<option value='" + data[int].id + "'>"
                        + data[int].name + "</option>");
            }
            $("#" + "deptidc" + (numb+1)).get(0).selectedIndex;
        }
    });
}

function isBlankValue(val) {
    return val == null || val == "null" || val == "";
}

//保存关联网点
function savewang() {
    var groupid=$("#id").val();
    if(groupid==""){
        alert("请先保存群组基本信息");
        return false;
    }
    var deptid=null;
    var grade=5;
    if ($('#deptidc5').val()!=0 && !isBlankValue($('#deptidc5').val())) {
        deptid=$('#deptidc5').val();
        grade=5;
    }else if($('#deptidc4').val()!=0 && !isBlankValue($('#deptidc4').val())){
        deptid=$('#deptidc4').val();
        grade=4;
    }else if($('#deptidc3').val()!=0 && !isBlankValue($('#deptidc3').val())){
        deptid=$('#deptidc3').val();
        grade=3;
    }else if($('#deptidc2').val()!=0 && !isBlankValue($('#deptidc2').val())){
        deptid=$('#deptidc2').val();
        grade=2;
    }else if($('#deptidc1').val()!=0 && !isBlankValue($('#deptidc1').val())){
        deptid=$('#deptidc1').val();
        grade=1;
    }else{
        alert("请选择！");
        return;
    }
    $.ajax({
        url : "savegroupwangdata",
        type : 'post',
        dataType : "json",
        data : {
            groupid : groupid,
            deptid : deptid,
            grade:grade
        },
        async : true,
        error : function() {
            alertmsg("Connection error");
        },
        success : function(data) {
            if(1==data.status){
                alertmsg(data.message);
                var groupid = $("#id").val();
                $('#group_wang_list').bootstrapTable(
                    'refresh',{url:'initgroupdeptidlist?groupId='+groupid});
            }else{
                alertmsg(data.message);
            }
        }
    });
}


function getSelectedRowWang() {
    var data = $('#group_wang_list').bootstrapTable('getSelections');
    if (data.length == 0) {
        alertmsg("请选择！");
        return ;
    }
    var idArray = new Array();
    for(var i=0;i<data.length;i++){
        idArray.push(data[i].id);
    }
    return idArray.toString();
}
function remove_wang(){
    var groupid=$("#id").val();
    var ids=getSelectedRowWang();
    if (ids==null) {
        return;
    }
    if(window.confirm("确定要删除吗？")==true){
        $.ajax({
            url:'deletegroupdept',
            type:'post',
            data:{ids:ids,groupid:groupid},
            success:function(data){
                if(1==data.status){
                    alertmsg(data.message);
                    var groupid = $("#id").val();
                    $('#group_wang_list').bootstrapTable(
                        'refresh',{url:'initgroupdeptidlist?groupId='+groupid});
                }else{
                    alertmsg(data.message);
                }
            }

        })
    }
}

//添加事件
function operateFormatter(value, row, index) {
    return [
        '<a class="edit m-left-5" href="javascript:void(0)" title="修改能力">',
        '<i class="glyphicon glyphicon-edit"></i>',
        '</a>'
    ].join('');
}
//事件相应
window.operateEvents = {
    'click .edit': function (e, value, row, index) {
        updat_group_privilege(row.id);
    }
};

//修改群组成员权限
function updat_group_privilege(id){
    $("#myModal_group_privilege_update1").on("hidden.bs.modal", function() {
        $(this).removeData("bs.modal");
    });
    $("#myModal_group_privilege_update1").modal({
        show: true,
        remote: 'group2privilegeupdate?userId='+id+'&groupId='+$("#id").val()
    });

}

