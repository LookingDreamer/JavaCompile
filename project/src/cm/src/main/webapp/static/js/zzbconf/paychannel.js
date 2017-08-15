require(["jquery", "bootstrap-table", "bootstrap","bootstrapTableZhCn","jqvalidatei18n","public","zTree","zTreecheck"], function ($) {
	//数据初始化 
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
                field: 'state',
                align: 'center',
                valign: 'middle',
                checkbox: true
            }, {
           	 field: 'rownum',
             title: '序号',
             align: 'center',
             valign: 'middle',
             sortable: true
            },{
                field: 'id',
                title: 'id',
                visible: false,
                switchable:false
            }, {
                field: 'paychannelname',
                title: '支付通道名称',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
                field: 'sort',
                title: '排序权重',
                align: 'center',
                valign: 'middle',
                sortable: true
            }, {
            	field: 'stateflag',
            	title: '当前状态',
            	align: 'center',
            	valign: 'middle',
            	sortable: true
            }, {
            	field: 'paytype',
            	title: '支付类型',
            	align: 'center',
            	valign: 'middle',
            	sortable: true
            }, {
            	field: 'updatetime',
            	title: '最近更新时间',
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
		
		// 页面【批量删除】按钮
		$("#delcheckbank").on("click", function(e) {
//			confirmmsg("删除已选择银行")
			if(confirm("删除不可恢复，是否删除？")){
				var cardconfids = getSelectedRows();
				$.ajax({
					url : 'delbankcardconfbyid?cardconfids=' + cardconfids,
					type : 'GET',
					dataType : "json",
					async : true,
					error : function() {
						alertmsg("Connection error");
					},
					success : function(data) {
						var result = data.msg;
						if (result>0) {
							alertmsg("删除成功！");
							checkremoveTr();
						}else{
							alertmsg("删除失败！");
						}
					}
				});
			}
		});
		function getSelectedRows() {
			var table = $(":checkbox").parents("table");
			var sd  = table.find("input[name$='ischecked']")
			var sb  = table.find("input[name$='id']")
			if(sd.length == 0){
		    	alertmsg("没有可删除选项");
		    }else{
		    	var arrayuserid = new Array();
		    	for(var i=0;i<sd.length;i++){
		    		if(sd[i].checked){
		    			arrayuserid.push(sb[i].value)
		    		}
		    	}
		    	return arrayuserid;
		    }
		}
		function checkremoveTr() {
			var table = $(":checkbox").parents("table");
			var trs  = table.find("input[name$='ischecked']")
			if(trs.length != 0){
		    	for(var i=0;i<trs.length;i++){
		    		if(trs[i].checked){
		    			$(trs[i]).parents("tr").next().remove()
		    			$(trs[i]).parents("tr").remove();
		    		}
		    	}
			}
		}
		//修改保存ok
		$("#paychannelsaveform").on("click", function(e) {	
		});
//		返回
		$('#backbutton').on("click",function(){
			history.go(-1)
		});
		
		$(function(){ 
			if($("#poundageradio1").attr("value")=="1"){
				$("#credittr").hide();
				$("#cashtr").hide();
			}else{
				$("#credittr").show();
				$("#cashtr").show();
			}
			if($("#dontshow").attr("value")=="0"){
				$("#charge0").attr("disabled","disabled");
				$("#charge1").attr("disabled","disabled");
				$("#ratio").attr("disabled","disabled");
				$("#poundageradio1").attr("disabled","disabled");
				$("#poundageradio2").attr("disabled","disabled");
				$("#poundageratio").attr("disabled","disabled");
				$("#fixedfee").attr("disabled","disabled");
				$("#cess").attr("disabled","disabled");
			}
		}); 
//		全选操作
		$(function() {
		    var all_checked = false;
		    $(":checkbox").click(function() {
		        var table = $(this).parents("table");
		        if($(this).attr("id") === "all") {
		            table.find("input[name$='ischecked']").prop("checked", !all_checked);
		            all_checked = !all_checked;
		        }
		        else {
		            table.find(":checkbox[id!=all]").each(function (i) {
		                if(!$(this).is(":checked")) {
		                    table.find("#all").prop("checked", false);
		                    all_checked = false;
		                    return false;
		                }
		                $("#all").prop("checked", true);
		                all_checked = true;
		            });
		        }
		    });
		});
		
//		显示隐藏控制
		 $("#poundageradio1").on("click", function(e){
			  if($(this).attr("value")=="1"){
				  $("#credittr").hide();
				  $("#cashtr").hide();
			  }else{
				  $("#credittr").show();
				 $("#cashtr").show();
			  }
	     });
		 
//		显示隐藏控制
		 $("#poundageradio2").on("click", function(e){
			 if($(this).attr("value")=="0"){
				 $("#credittr").show();
				 $("#cashtr").show();
			 }else{
				 $("#credittr").hide();
				 $("#cashtr").hide();
			 }
		 });
		 
		 $("#dontshow").on("click", function(e){
			  if($(this).attr("value")=="1"){
				  $("#charge0").attr("disabled","disabled");
					$("#charge1").attr("disabled","disabled");
					$("#ratio").attr("disabled","disabled");
					$("#poundageradio1").attr("disabled","disabled");
					$("#poundageradio2").attr("disabled","disabled");
					$("#poundageratio").attr("disabled","disabled");
					$("#fixedfee").attr("disabled","disabled");
					$("#cess").attr("disabled","disabled");
			  }
	     });
		 
		 $("#show").on("click", function(e){
			 if($(this).attr("value")=="0"){
				 $("#charge0").removeAttr("disabled");
				 $("#charge1").removeAttr("disabled");
				 $("#ratio").removeAttr("disabled");
				 $("#poundageradio1").removeAttr("disabled");
				 $("#poundageradio2").removeAttr("disabled");
				 $("#poundageratio").removeAttr("disabled");
				 $("#fixedfee").removeAttr("disabled");
				 $("#cess").removeAttr("disabled");
			 }
		 });
		 
		 //table-zffs 初始化url
		 var zffsiniturl =  'initpaywaylist?id='+$('#paychannelid').val();
		 /**
		  * 支付方式初始化table数据
		  */
		 $('#table-zffs').bootstrapTable({
		        method: 'get',
		        url: zffsiniturl,
		        cache: false,
		        striped: true,
		        pagination: true,
		        sidePagination: 'server', 
		        pageNumber:true,
		        pageList:"[10, 20, 50, 100, 200]",
		        pageSize: 10,
		        minimumCountColumns: 2,
		        clickToSelect: true,
		        singleSelect:true, 
		        columns: [{
		            field: 'state',
		            align: 'center',
		            valign: 'middle',
		            checkbox: true
		        }, {
		            field: 'id',
		            title: 'id',
		            visible: false,
		            switchable:false
		        }, {
		            field: 'paychannelid',
		            title: 'paychannelid',
		            visible: false,
		            switchable:false
		        }, {
		            field: 'paychannelname',
		            title: '支付方式名称',
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
					field: 'agreementname',
					title: '协议名称',
					align: 'center',
					valign: 'middle',
					sortable: true
				}, {
		        	field: 'providename',
		        	title: '供应商名称',
		        	align: 'center',
		        	valign: 'middle',
		        	sortable: true
		        }, {
		            field: 'operating',
		            title: '操作',
		            align: 'center',
		            valign: 'middle',
		            switchable:false,
		            formatter: operateFormatterZffs,
		            events: operateEventsZffs
		        }]
		    }).on('click-row.bs.table', function (e, row, $element) {
	    		var id = row.id;
	    		select_auto_id(id);
	   		
	    	});;
		 //机构级联初始化
		 /*deptconnSelect(0);*/
		 
		 $(function(){        
				$("#edit_zzfs_form").validate({
					errorLabelContainer : ".alert-danger",
					errorElement : "p",
			        errorClass : "text-left",
			        focusInvalid : false,
			        rules: {
			        	deptid:"required",
			        	providerid:"required",
			        },  
			        messages: { 
			        	deptid:{required: "机构编码不能为空"},
			        	providerid:{required: "供应商编码不能为空"},
			        }
			    });
			 //			选择供应商ok
			 $("#prvautoname").on("click", function(e) {
				 $('#showautopic').modal();
				 $('#treeDemosearch').val('');
				 $.fn.zTree.init($("#treeautoDemo"), settingauto);
				 //settingauto();
			 });
		    });
		 $("#addbutton").on("click", function(e) {
				if($("#edit_zzfs_form").valid()){
					var pwd = $("#passwordtemp").val();
					var con_pwd = $("#password").val();
					if(pwd==con_pwd){
						$('#usersaveform').submit();
					}else{
						alertmsg("两次密码不同请重新输入");
					}
				}
			});
		 

//			选择机构ok
			$("#deptname").on("click", function(e) {
				$('#showdeptpic').modal();
				$('#treesearch').val('');
				$.fn.zTree.init($("#treeDemoDept"), settingdept);
			});
			//重置
			$("#resetbutton").on("click",function(){
				$("#confid").val('');
				$("#prvautoname").val('');
				$("#deptname").val('');
				$("#providerid").val('');
				$("#deptid").val('');
			});
			//查询支付方式
			$("#queryautoconfig").on("click",function(){
				$('#table-zffs').bootstrapTable(
						'refresh',
						{url:"querypcm?providerid="+$("#providerid").val()+"&deptid="+$("#deptid").val()+"&paychannelid="+$("#paychannelid").val(),
						});
			})
	});
	var settingauto ={
		async: {
			enable: true,
			url : "../elfconf/queryprotreeOfablity",
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
		check : {
			enable : true,
			chkStyle : "radio",
			radioType : "all"
		},
		callback : {
			onCheck : zTreeOnCheckauto
		}
	};
});
//默认一页显示十条记录
var pagesize = 10;
//当前调用的url
var pageurl = "initpaychannellist";
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
//根据支付通道ID修改银行卡信息跳转到paychanneledit页面
function updatepaychannel(id){
	location.href = "paychanneledit?id="+id;
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
			$('#table-zffs').bootstrapTable('load', data);
		}
	});
}
//添加事件
function operateFormatter(value, row, index) {
    return [
        '<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
        '<i class="glyphicon glyphicon-edit"></i>',
        '</a>'
    ].join('');
}
//事件相应
window.operateEvents = {
    'click .edit': function (e, value, row, index) {
    	updatepaychannel(row.id);
    }
};

/**
 * ########支付方式 table 配置 start##########
 */
//添加事件
function operateFormatterZffs(value, row, index) {
	return [
	        '<a class="edit m-left-5" href="javascript:void(0)" title="编辑">',
	        '<i class="glyphicon glyphicon-edit"></i>',
	        '</a>',
	        '<a class="remove m-left-5" href="javascript:void(0)" title="删除">',
	        '<i class="glyphicon glyphicon-remove"></i>',
	        '</a>'
	        ].join('');
}

//支付方式初始化table 弹窗操作事件
window.operateEventsZffs = {
	'click .edit': function (e, value, row, index) {
		$("#edit_zffs_new").modal({
			/*show: true,*/
			
			/*remote: deptconnSelect(0),
			remote: proconnSelect(0),*/
			remote: 'initpcm?paychannelmageid='+row.id
		});
//		agreementSelect(0);
		
	},
	'click .remove': function (e, value, row, index) {
    	deletepayway(row.id);
		e.stopPropagation();
    }
};

//清空model数据
function rmdata(){
	$("#edit_zffs_new").removeData("bs.modal");
}

function addpayway(){
	var id=$('#paychannelid').val();
	$("#edit_zffs_new").modal({
		/*show: true,*/
		remote: 'addpcm?paychannelmageid='+id
	});
}

function closeModal() {
	$("#edit_zffs_new").removeData("bs.modal");
}

//根据id删除paychannelmanager
function deletepayway(id){
	if(window.confirm("确定要删除吗？")==true){
		var paychannelmageid=$('#paychannelid').val();
		$.ajax({
			url : 'deletepcmbyid?paychannelmageid=' + id,
			type : 'GET',
			dataType : "json",
			async : true,
			error : function() {
				alertmsg("Connection error");
			},
			success : function(data) {
				var result = data.count;
				if (result>0) {
					alertmsg("删除成功！");
					/*reloaddata(paychannelmageid);*///重新载入
					//location.href="paychanneledit?id="+paychannelmageid;

					$('#table-zffs').bootstrapTable(
						'refresh',
						{url:"querypcm?providerid="+$("#providerid").val()+"&deptid="+$("#deptid").val()+"&paychannelid="+$("#paychannelid").val(),
						});

				}else{
					alertmsg("删除失败！");
				}
			}
		});
	}else{
      return false;
	}	
}

//重载pcm-table
function reloaddata(data){
	$.ajax({
		url : "initpaywaylist",/*'initpaywaylist?id='+$('#paychannelid').val(),*/
		type : 'GET',
		dataType : "json",
		data: "?id="+data,     
		/*data: data+"&limit="+pagesize,*/
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			$('#table-zffs').bootstrapTable('load', data);
		}
	});
}


function deptTreeOnCheck(event, treeId, treeNode) {
	$("#deptid").val(treeNode.id);
	$("#deptname").val(treeNode.name);
	$('#myModal_dept').modal("hide");
}

//select点击事件
function deptconnchange(numb) {
	if (numb > 4) {
		return;
	}
	/*var partid = "deptidc" + numb;*/
	var nextid = "deptidc" + (numb + 1);
	$("#" + nextid).empty();
	$("#" + nextid).append("<option value='0'>请选择</option>")
	deptconnSelect(numb);
}
//机构级联选择方法
function deptconnSelect(numb) {
	$.ajax({
		url : "../provider/querydepttree",
		type : 'POST',
		dataType : "json",
		data : {
			id : $("#deptidc" + numb).val(),
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
			/*$("#" + "deptidc" + (numb+1)).get(0).selectedIndex;*/
			deptconnchange(numb+1);
		}
	});
}

//select点击事件
function providerconnchange(numb) {
	if (numb > 4) {
		return;
	}
	/*var partid = "provideridp" + numb;*/
	var nextid = "provideridp" + (numb + 1);
	$("#" + nextid).empty();
	$("#" + nextid).append("<option value='0'>请选择</option>")
	proconnSelect(numb);
}
//供应商级联选择 
function proconnSelect(numb) {
	$.ajax({
		url : "../provider/queryprotree",
		type : 'POST',
		dataType : "json",
		data : {
			id : $("#provideridp" + numb).val(),
		},
		async : true,
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data) {
			
			
			for (var int = 0; int < data.length; int++) {
				$("#" + "provideridp" + (numb+1)).append(
						"<option value='" + data[int].id + "'>"
								+ data[int].name + "</option>");
			}
			providerconnchange(numb+1);
		}
	});
}
//显示隐藏 机构和供应商 select选择框
function showdeptsel(){
	document.getElementById("deptseltr").style.display="";
}
function hiddendeptsel(){
	document.getElementById("deptseltr").style.display="none";
}
function showprosel(){
	document.getElementById("proseltr").style.display="";
}
function hiddenprosel(){
	document.getElementById("proseltr").style.display="none";
}
//机构 关联功能
function savedeptsel(){
	var pcmid=$('#id').val();
	var deptid = null;
	var selectnum=null;
	
	if($('#deptidc5').val()!='0' && $('#deptidc5').val()!=null){
		deptid = $('#deptidc5').val();
		selectnum='5';
	} else if($('#deptidc4').val()!='0' && $('#deptidc4').val()!=null){
		deptid = $('#deptidc4').val();
		selectnum='4';
	} else if($('#deptidc3').val()!='0' && $('#deptidc3').val()!=null){
		deptid = $('#deptidc3').val();
		selectnum='3';
	} else if($('#deptidc2').val()!='0' && $('#deptidc2').val()!=null){
		deptid = $('#deptidc2').val();
		selectnum='2';
	} else if($('#deptidc1').val()!='0' && $('#deptidc1').val()!=null){
		deptid = $('#deptidc1').val();
		selectnum='1';
	}
	
	$('#deptid').val(deptid);
	var selectvalue = $('#deptidc'+selectnum).val();
	$('#deptname').val(selectvalue);
	
}
//供应商关联功能
function saveprosel(){
	var pcmid=$('#id').val();
	var providerid = null;
	var selectnum=null;
	
	if($('#provideridp5').val()!='0' && $('#provideridp5').val()!=null){
		providerid = $('#provideridp5').val();
		selectnum='5';
	} else if($('#provideridp4').val()!='0' && $('#provideridp4').val()!=null){
		providerid = $('#provideridp4').val();
		selectnum='4';
	} else if($('#provideridp3').val()!='0' && $('#provideridp3').val()!=null){
		providerid = $('#provideridp3').val();
		selectnum='3';
	} else if($('#provideridp2').val()!='0' && $('#provideridp2').val()!=null){
		providerid = $('#provideridp2').val();
		selectnum='2';
	} else if($('#provideridp1').val()!='0' && $('#provideridp1').val()!=null){
		providerid = $('#provideridp1').val();
		selectnum='1';
	}
	
	$('#providerid').val(providerid);
	var selectvalue = $('#provideridp'+selectnum).val();
	$('#providername').val(selectvalue);
}

/**
 * ########支付方式 table 配置 end##########
 */

var uu = 1;
function addtr(){
	var  tr1 =$("#tb1 tr:gt(0):eq(0)").clone();
	var  tr2 =$("#tb1 tr:gt(0):eq(1)").clone();
	tr1.find("input").val("");
	tr1.find("input[name$='commonlyusedbankflag']").removeAttr("checked")
	tr1.find("input[name$='cashcardflag']").removeAttr("checked")
	tr1.find("input[name$='cashlimitfalg']").removeAttr("checked")
	tr1.find("input[name$='cashlimitdayfalg']").removeAttr("checked")
	tr1.find("select[name$='insbcodevalue']").val("");
	tr2.find("input").val("");
	tr2.find("input[type='checkbox']").removeAttr("checked")
	var num = $("#hiddensum").val(); 
	num = parseInt(num);
	if(num==0){
		num++;
	}
    tr1.find("select[name$='insbcodevalue']").attr("name", "bankcardconflist["+num+"].insbcodevalue" );
    tr1.find("input[name$='commonlyusedbankflag']").attr("name", "bankcardconflist["+num+"].commonlyusedbankflag" );
    tr1.find("input[name$='cashcardflag']").attr("name", "bankcardconflist["+num+"].cashcardflag" );
    tr1.find("input[name$='cashlimit']").attr("name", "bankcardconflist["+num+"].cashlimit" );
    tr1.find("input[name$='cashlimitfalg']").attr("name", "bankcardconflist["+num+"].cashlimitfalg" );
    tr1.find("input[name$='cashdaylimit']").attr("name", "bankcardconflist["+num+"].cashdaylimit" );
    tr1.find("input[name$='cashlimitdayfalg']").attr("name", "bankcardconflist["+num+"].cashlimitdayfalg" );
    
    tr2.find("input[name$='creditcardflag']").attr("name", "bankcardconflist["+num+"].creditcardflag" );
    tr2.find("input[name$='creditlimit']").attr("name", "bankcardconflist["+num+"].creditlimit" );
    tr2.find("input[name$='creditlimitfalg']").attr("name", "bankcardconflist["+num+"].creditlimitfalg" );
    tr2.find("input[name$='creditdaylimit']").attr("name", "bankcardconflist["+num+"].creditdaylimit" );
    tr2.find("input[name$='creditlimitdayfalg']").attr("name", "bankcardconflist["+num+"].creditlimitdayfalg" );
//    tr1.find("input[name$='cashdaylimit']").attr("id", "fuck" + num).attr("name", "bankcardconflist["+num+"].cashdaylimit" );
	$("#tb1").append(tr1);
	$("#tb1").append(tr2);
	num++;
}
function checkboxclick(obj){
	if(obj.checked){
		$("#fuck").val("1");
		alertmsg($("#fuck").val());
	}else{
		$("#fuck").val("0");
	}
}
function haoba(val){
	var re = /^[0-9]*[1-9][0-9]*$/; 
	if (!re.test(val)) {
		alertmsg("请输入正整数！");
		return;
	}
}


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
	/*if(treeNode.comtype != "05"){
		alertmsg("下级机构会被重置");
	}*/
	$('#showdeptpic').modal("hide");
}
function select_auto_id(id){
	$.ajax({
		url : 'selectpcmbyid',
		type : 'GET',
		dataType : "json",
		async : true,
		data:({id:id}),
		error : function() {
			alertmsg("Connection error");
		},
		success : function(data){
			console.info(data);
			$("#confid").val(id);
			$("#prvautoname").val(data.providername);
			$("#deptname").val(data.deptname);
			$("#providerid").val(data.providerid);
			$("#deptid").val(data.deptid);
		}
	})
}