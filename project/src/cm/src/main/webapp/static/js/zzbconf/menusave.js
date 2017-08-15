require([ "jquery", "fuelux", "zTree", "zTreecheck","public" ], function($) {

	$(function() {
		inittree();
		/*$('#myTree').tree({
			dataSource : function(options, callback) {
				var parentnodecode = "";
				if (!jQuery.isEmptyObject(options)) {
					console.log('options:', options);
					console.log(options.dataAttributes.id);
					parentnodecode = options.dataAttributes.parentnodecode;
				}
				$.ajax({
					type : "POST",
					url : "../menu/menusmanagerlist",
					data : "parentnodecode=" + parentnodecode,
					dataType : 'json',
					success : function(data) {
						callback({
							data : data
						});
					}
				});
			},
			multiSelect : false,
			cacheItems : true,
			folderSelect : false,
		});
        $('#myTree').on('selected.fu.tree', function(e, selected) {
			getdatafromid(selected.selected[0].dataAttributes.id);
		});

		$('#myTree').on('updated.fu.tree', function(e, selected) {
			getdatafromid(selected.selected[0].dataAttributes.id);
		});

		$('#myTree').on('opened.fu.tree', function(e, info) {
			getdatafromid(info.dataAttributes.id);
		});

		$('#myTree').on('closed.fu.tree', function(e, info) {
			getdatafromid(info.dataAttributes.id);
		});*/

		$('[data-toggle="popover"]').popover();

		// $("#parentnodecodename").onblur

		$("#iconurlbtn").on("click", function(e) {
			$('#showpic').modal()
		});

		$(".fui-icons span").on(
				"click",
				function(e) {
					$("#iconurl").val($(e.target).attr("class"));
					$("#iconurlpic").html(
							"<span class=" + $(e.target).attr("class")
									+ "></span>");
					$('#showpic').modal("hide")
				});

		// 添加菜单界面【添加】按钮
		$("#addmenubutton").on("click", function(e) {
				addmenu();
				// alertmsg("添加成功！");
		});

		function addmenu() {
			if ($("#nodename").val() == "") {
				alertmsg("菜单名称不能为空");
				$("#nodename").focus();
				return;
			}

			/*if ($("#activeurl").val() == "") {
				alertmsg("URL不能为空");
				$("#activeurl").focus();
				return;
			}*/

			if ($("#nodecode").val() == "") {
				alertmsg("菜单code不能为空");
				$("#nodecode").focus();
				return;
			}
			if ($("#parentnodecode").val() == "") {
				alertmsg("父菜单code不能为空");
				$("#parentnodecode").focus();
				return;
			}
			if ($("#nodelevel").val() == "") {
				alertmsg("菜单等级不能为空");
				$("#nodelevel").focus();
				return;
			}
			if ($("#childflag").val() == "") {
				alertmsg("子菜单标记不能为空");
				$("#childflag").focus();
				return;
			}
			if ($("#orderflag").val() == "") {
				alertmsg("菜单顺序不能为空");
				$("#orderflag").focus();
				return;
			}
			if ($("#status").val() == "") {
				alertmsg("菜单状态不能为空");
				$("#status").focus();
				return;
			}
			if ($("#iconurl").val() == "") {
				alertmsg("图标样式不能为空");
				$("#iconurl").focus();
				return;
			}

			if (confirm("是否确认添加该菜单？")) {
				$('#addmenuform').submit();
				/*
				 * $('#addmenuform').ajaxSubmit({ url : 'addmenu', type :
				 * 'POST', dataType : "json", error : function() {
				 * alertmsg("Connection error"); }, success : function(data) { var
				 * result = data.flag; alertmsg(result); if (result=="success") {
				 * alertmsg("添加菜单成功"); }else{ alertmsg("添加菜单失败");; } } });
				 */
			}

		}

		// 【重置】按钮
		$("#reset").on("click", function(e) {
			/**
			 * 清空内容
			 */
			$("#nodename").val("");
			$("#activeurl").val("");
			$("#nodecode").val("");
			$("#parentnodecode").val("");
			$("#parentnodecodename").val("");
			$("#nodelevel").val("");
			$("#childflag").val("");
			$("#orderflag").val("");
			$("#status").val("");
			$("#iconurl").val("");
			$("#iconurlpic").html("<span class=" + "" + "></span>");
			$("#noti").val("");
			/**
			 * 取消只读
			 */
			$("#nodename").removeAttr("readonly");
			$("#iconurlbtn").removeAttr("disabled");
			$("#activeurl").removeAttr("readonly");
			$("#nodecode").removeAttr("readonly");
			$("#parentnodecode").removeAttr("readonly");
			$("#parentnodecodename").removeAttr("readonly");
			$("#parentmenu").removeAttr("readonly");
			$("#nodelevel").removeAttr("readonly");
			$("#childflag").removeAttr("readonly");
			$("#orderflag").removeAttr("readonly");
			$("#status").removeAttr("readonly");
			$("#noti").removeAttr("readonly");
			$("#addmenubutton").removeAttr("disabled");
		});

	});

});

function inittree(){
	$.fn.zTree.init($("#menuTree"), menuSetting);
}
var menuSetting = {
		async:{
			enable: true,
			url:"../menu/menusmanagerlist",
			autoParam:["id"],
			/*otherParam: ["id",id],*/
			dataType:"json",
			type:"post"
		},
		callback:{
			onClick: menuGetdatafromid
		}
}

/*function getdatafromid(id){*/
function menuGetdatafromid(event,treeId,treeNode){
	var id=treeNode.id;
	$.ajax({
		type : "POST",
		url : "../menu/querybyid",
		data : "id=" + id,
		dataType : "json",
		success : function(datainfo) {
			var nodelevel = Number(datainfo.menuobject.nodelevel);
			$("#parentnodecode").val(datainfo.menuobject.nodecode);
			$("#parentnodecodename").val(datainfo.menuobject.nodename);
			$("#nodelevel").val(nodelevel + 1);
			if (datainfo.menuobject.childflag == "0") {
				$("#nodename").attr({
					readonly : 'readonly'
				});
				$("#iconurlbtn").attr({
					disabled : 'disabled'
				});
				$("#activeurl").attr({
					readonly : 'readonly'
				});
				$("#nodecode").attr({
					readonly : 'readonly'
				});
				$("#parentnodecode").attr({
					readonly : 'readonly'
				});
				$("#parentnodecodename").attr({
					readonly : 'readonly'
				});
				$("#parentmenu").attr({
					readonly : 'readonly'
				});
				$("#nodelevel").attr({
					readonly : 'readonly'
				});
				$("#childflag").attr({
					readonly : 'readonly'
				});
				$("#orderflag").attr({
					readonly : 'readonly'
				});
				$("#status").attr({
					readonly : 'readonly'
				});
				$("#noti").attr({
					readonly : 'readonly'
				});
				$("#addmenubutton").attr({
					disabled : 'disabled'
				});
				alertmsg("末级功能菜单不能添加子菜单！如需添加子菜单，请先修改该菜单‘是否有子菜单’属性为‘是’。");
			} else {
				$("#nodename").removeAttr("readonly");
				$("#iconurlbtn").removeAttr("disabled");
				$("#activeurl").removeAttr("readonly");
				$("#nodecode").removeAttr("readonly");
				$("#parentnodecode").removeAttr("readonly");
				$("#parentnodecodename").removeAttr("readonly");
				$("#parentmenu").removeAttr("readonly");
				$("#nodelevel").removeAttr("readonly");
				$("#childflag").removeAttr("readonly");
				$("#orderflag").removeAttr("readonly");
				$("#status").removeAttr("readonly");
				$("#noti").removeAttr("readonly");
				$("#addmenubutton").removeAttr("disabled");
			}
		}
	});
}
