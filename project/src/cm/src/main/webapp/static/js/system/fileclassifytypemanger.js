require(["jquery", "fuelux","public" ], function ($) {

	$(function() {
		$('#myTree').tree({
			dataSource: function (options, callback) {
				var parentnodecode ="";
				if(!jQuery.isEmptyObject(options)){
					console.log('options:', options);
					console.log(options.dataAttributes.id);
					parentnodecode =options.dataAttributes.parentnodecode;
				}
				$.ajax({
					type : "POST",
					url : "../menu/menusmanagerlist",
					data :"parentnodecode="+parentnodecode,
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
		
		$("#iconurlbtn").on("click", function(e) {
			$('#showpic').modal();
		});
		$("#iconurlbtn_add").on("click", function(e) {
			$('#showpic').modal();
		});
		$("#addmenubtn").on("click", function(e) {
			$('#addmenu_modal').modal();
		});
		
		$("#deletemenubutton").on("click", function(e) {
			var nowid=$("#id").val();
			if(confirm("确认要删除吗？")){
				location.href="deletebyid?id="+nowid;
				alertmsg("删除成功");
			}
		});
		
		$("#addmenubutton").on("click", function(e) {
			location.href='addftl';
		});
		
		$(".fui-icons span").on("click", function(e) {
			$("#iconurl").val($(e.target).attr("class"));
			$("#iconurlpic").html("<span class="+$(e.target).attr("class")+"></span>");
			$('#showpic').modal("hide");
		});
		
		//【保存修改】 按钮
		$("#updatemenubutton").on("click", function(e) {
		    	updatemenu();
		});
		
		function updatemenu(){
			if($("#nodename").val()==""){
				alertmsg("菜单名称不能为空");
				$("#nodename").focus();
				return;
			}
			/*if($("#activeurl").val()==""){
				alertmsg("URL不能为空");
				$("#activeurl").focus();
				return;
			}*/
			if($("#nodecode").val()==""){
				alertmsg("菜单code不能为空");
				$("#nodecode").focus();
				return;
			}
			if($("#parentnodecode").val()==""){
				alertmsg("父菜单code不能为空");
				$("#parentnodecode").focus();
				return;
			}
			if($("#nodelevel").val()==""){
				alertmsg("菜单等级不能为空");
				$("#nodelevel").focus();
				return;
			}
			if($("#childflag").val()==""){
				alertmsg("子菜单标记不能为空");
				$("#childflag").focus();
				return;
			}
			if($("#orderflag").val()==""){
				alertmsg("菜单顺序不能为空");
				$("#orderflag").focus();
				return;
			}
			/*if($("#status").val()==""){
				alertmsg("菜单状态不能为空");
				$("#status").focus();
				return;
			}*/
			if($("#iconurl").val()==""){
				alertmsg("图标样式不能为空");
				$("#iconurl").focus();
				return;
			}
			
			if(confirm("确认修改该菜单吗？")){
				$('#menuform').submit();
				/*$('#menuform').ajaxSubmit({
				url : 'menu/updatemenubyid',
				type : 'POST',
				dataType : "json",
				error : function() {
					alertmsg("Connection error");
				},
				success : function(data) {
					var result = data.flag;
					alertmsg(result);
					if (result=="success") {
						alertmsg("修改成功！");
					}else{
						alertmsg("修改失败！");;
					}
				}
			   });*/
			}
			
		};
		
    });
    
});

function getdatafromid(id){
	$.ajax({
		   type: "POST",
		   url: "../menu/querybyid",
		   data: "id="+id,
		   dataType:"json",
		   success: function(datainfo){
			 $("#id").val(id);
			 $("#nodename").val(datainfo.menuobject.nodename);
			 $("#iconurlpic").html("<span class="+datainfo.menuobject.iconurl+"></span>");
			 $("#iconurl").val(datainfo.menuobject.iconurl);
			 $("#activeurl").val(datainfo.menuobject.activeurl);
		     $("#nodecode").val(datainfo.menuobject.nodecode);
		     $("#parentnodecode").val(datainfo.menuobject.parentnodecode);
		     $("#nodelevel").val(datainfo.menuobject.nodelevel);
		     $("#orderflag").val(datainfo.menuobject.orderflag);
		     
		     if(datainfo.menuobject.childflag=="1"){
//		    	 $("input[name=childflag]:eq(0)").attr("checked",'checked'); 
		    	 $("input[name=childflag]:eq(0)").prop('checked',true);
		     }else if(datainfo.menuobject.childflag=="0"){
//		    	 $("input[name=childflag]:eq(1)").attr("checked",'checked'); 
		    	 $("input[name=childflag]:eq(1)").prop('checked',true);
		     }
		     
		     var ctime=datainfo.menuobject.createtime;
		     var d=new Date(ctime);
		     var year=d.getYear()+1900;
		     var month=d.getMonth()+1;
		     $("#createtimepage").val(year+"-"+month+"-"+d.getDate()+" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds());
		     
		   }
		});
}

