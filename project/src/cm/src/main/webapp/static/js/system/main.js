String.prototype.repeat = function(num) {
	return new Array(Math.round(num) + 1).join(this);
};
require(
		[ "jquery", "flat-ui", "jquerycontextMenu", "jqueryuiposition", "jquery-ui", "core" ],
		function($) {
			var ttflag = 0;
			var defaultoptions = {
					target : (window.top.location != window.self.location ? window.top
							: window)
			};

			$(function() {
				//当前登陆人所有菜单
				var my_menu = $("#my_menu").val();
				if(my_menu==""){
					$("#head_show").hide();
				}else{
					var my_men_array = my_menu.split(",");
					for(var i=0;i<my_men_array.length;i++){
						if(my_men_array[i]=="m0017"){
							$("#top_menu2").css("display","block");
							ttflag = 1;
						}else if(my_men_array[i]=="m0018"){
							$("#top_menu1").css("display","block");
						}else if(my_men_array[i]=="m0025"){
							$("#top_menu3").css("display","block");
						}
					}
				}
				
				// 页面布局
				$("#appmenu").height($(window).height() - $("#apphead").height() - $("#apptask").height());
				$("#appcontent").height($(window).height() - $("#apphead").height() - $("#apptask").height());
				$("#desktop").height($(window).height() - $("#apphead").height() - $("#apptask").height());
				$(".appmid-content").css("margin-top", $("#apphead").height() + "px");
				$(window).resize(
						function() {
							$("#appmenu").height($(window).height() - $("#apphead").height() - $("#apptask").height());
							$("#appcontent").height($(window).height() - $("#apphead").height() - $("#apptask").height());
							$("#desktop").height($(window).height() - $("#apphead").height() - $("#apptask").height());
							$(".appmid-content").css("margin-top", $("#apphead").height() + "px");
						});
				// nav or other 获取焦点是btn 边框颜色
				$('.input-group').on(
						'focus',
						'.form-control',
						function() {
							$(this).closest('.input-group, .form-group').addClass('focus');
						}).on(
						'blur',
						'.form-control',
						function() {
							$(this).closest('.input-group, .form-group').removeClass('focus');
						});

				// menu
				$("#appmenu a[data-bind]").on(
						"click",
						function(e) {
							if ($(this).attr("data-bind") != "") {
								var allul = $(this).parents(".dropdown");
								var ulname = "";
								var nodeName = "";
								var thistext = $(this).html().substring($(this).html().indexOf("span>") + 5);
								if (allul.length > 0) {
									for (i = allul.length - 1; i >= 0; i--) {
										ulname = $(allul[i]).html();
										nodeName = nodeName
												+ ulname.substring(ulname.indexOf("span>") + 5, ulname.indexOf("<b"))
												+ ">>";
									}
								}
								$("#showtitle").html((nodeName + thistext).replace(/>>/g," <span class='glyphicon glyphicon-menu-right'></span> "));
								$("#showmenu").show();
								var clickurl = $(this).attr("data-bind");
								var spanclass = $(this).find("span:first").attr("class");
								$("#" + $(this).attr("target")).attr("src", clickurl);
								createlionbottom(clickurl, thistext, spanclass, nodeName + thistext);
							}
						});

				// 底部轮询菜单
				$("#loopunderwriting a[data-bind]").on(
					"click",
					function(e) {
						if ($(this).attr("data-bind") != "") {
							var nodeName = "任务管理";
							var thistext = "核保轮询状态查询";

							$("#showtitle").html(nodeName + " <span class='glyphicon glyphicon-menu-right'></span> " + thistext);
							$("#showmenu").show();
							var clickurl = $(this).attr("data-bind");
							var spanclass = "fui-time cus-icon";
							$("#" + $(this).attr("target")).attr("src", clickurl);
							createlionbottom(clickurl, thistext, spanclass, nodeName + ">>" + thistext);
						}
					});

				// 底部快捷导航 绑定右击事件
				$('#div_tab').contextMenu({
					selector : 'li',
					callback : function(key, options) {
						if (key == "open") {
							$(this).click();
						} else if (key == "close") {
							removeli($(this).attr("id").split("_")[1]);
						} else if (key == "refresh") {
							refresh($(this).attr("id").split("_")[1]);
							$(this).click();
						} else if (key == "closeother") {
							removeother($(this).attr("id").split("_")[1]);
							$(this).click();
						} else if (key == "closeall") {
							removeall();
						}
					},
					items : {
						"open" : {
							name : "打开",
							icon : "edit"
						},
						"refresh" : {
							name : "刷新",
							icon : "edit"
						},
						"closeother" : {
							name : "关闭其他",
							icon : "delete"
						},
						"closeall" : {
							name : "关闭所有",
							icon : "delete"
						},
						"close" : {
							name : "关闭",
							icon : "delete"
						}
					}
				});
			});
			if(ttflag==1){
				desktop();
			}

			//隐藏CM服务中的报表菜单
			/*var serviceDomain = top.window.location.href;
			if(!serviceDomain || serviceDomain.indexOf("//report.") == -1) {
				var reportMenu = $("#appmenu a[data-bind^='report/']");
				if (reportMenu) {
					$(reportMenu[0]).parents("li.dropdown").hide();
				}
			}*/

			messageNotReadCount();
		});

function desktop() {
		$("#apptask ul li").removeClass("active");
		if ($("#desktop").css('display') == 'none') {
			$("#desktop").css('display', 'block');
			$("#menu").css('display', 'none');
			$(".head-task").css('display', 'block');
			$("#apptask ul li.desk").addClass("active");
			if ($("#desktop_tasks").attr("src") == "about:blank") {
				$.insLoading();
				$("#desktop_tasks").attr("src", "business/mytask/queryTask");
			}
			$("#head-title").text("工作桌面");
			$("#head-title-s").text("切换至运营管理");
		} else {
			$("#desktop").css('display', 'none');
			$("#menu").css('display', 'block');
			$(".head-task").css('display', 'none');
			var newurl = $("iframe[id^=fra_]:not(:hidden)").attr("id");
			if (newurl) {
				newurl = newurl.substring(4);
				$("#div_tab li[data-remote='" + newurl + "']").addClass("active");
			}
			$("#head-title").text("运营管理");
			$("#head-title-s").text("切换至工作桌面");
		}
}
function messageNotReadCount(){
	$.ajax({
		  type: "get",
		  url: "message/getNotReadCount",
		  data: {receiver:$("#receiver").val()},
		  dataType:"text",
		  success: function(data){
			  $("#messageNotReadCount").text(data);
		  }
		});
}

function openDialog(val) {
	$.xDialog({
		remote : val
	});
}

/*******************************************************************************
 * openDialogForCM 方法 对于CM相关页面需要的宽度和core.js的不同，重构了openDialog方法
 ******************************************************************************/
function openDialogForCM(val) {
	$.xDialog({
		remote : val,
		width : "700px"
	});
}

/*******************************************************************************
 * openDialogForCM 方法 对于CM相关页面需要的宽度需加大
 ******************************************************************************/
function openLargeDialog(val) {
	$.xDialog({
		remote : val,
		width : "900px"
	});
}

/*******************************************************************************
 * openImgView 方法 影像预览
 ******************************************************************************/
function openImgView(val) {
	$.imgView({
		remote : val,
		width : "5px"
	});
}

function createlionbottom(url, text, cssclass, showtitle) {
	var newurl = url.replace(/\//g, '');
		if(newurl.indexOf("?") > 0){
		//  newurl = newurl.substring(0, newurl.indexOf("?"));
		newurl=newurl.replace(/\?|\=/g, '');
	}
	if ($("#fra_" + newurl).length > 0) {
		// 页面已经打开
		iframeopenurl(newurl, showtitle);
	} else {
		// 第一次打开
		if ($("#appcontent").find("iframe").size() >= 6) {
			//只缓存6个页面
			$("#appcontent").find("iframe:eq(0)").remove();
			$("#div_tab").find("li:eq(0)").remove();
		}
		$.insLoading();
		var lihtml = '<li id="li_' + newurl
				+ '" class="quicknavigation active" data-remote="' + newurl
				+ '" onclick="iframeopenurl(\'' + newurl + '\',\'' + showtitle
				+ '\')"><span class="' + cssclass + '"></span>' + text
				+ '</li>';
		$("#div_tab li").removeClass("active");
		$("#div_tab").append(lihtml);
		$("iframe[id^=fra_]").hide();
		var iframehtml = '<iframe id="fra_' + newurl + '" name="fra_' + newurl
				+ '" src="' + url
				+ '" seamless="seamless" width="100%" height="100%"></iframe>';
		$("#appcontent").append(iframehtml);
	}
}

function iframeopenurl(url, showtitle) {
		$("#desktop").hide();
		$("#menu").show();
		$("#head-title").text("运营管理");
		$("#head-title-s").text("切换至工作桌面");
		$(".head-task").css('display', 'none');
		$("#apptask ul li.desk").removeClass("active");
		if (showtitle != "") {
			$("#showtitle").html(showtitle.replace(/>>/g," <span class='glyphicon glyphicon-menu-right'></span> "));
		}
		showoneiframe(url);
		$("#div_tab li").removeClass("active");
		$("#div_tab li[data-remote='" + url + "']").addClass("active");
}
// 显示一个iframe
function showoneiframe(url) {
	var alliframe = $("#appcontent").find("iframe");
	for (var i = 0; i < alliframe.length; i++) {
		if (alliframe[i].id != "fra_" + url) {
			$(alliframe[i]).hide();
		} else {
			$(alliframe[i]).show();
		}
	}
}

function removeli(newurl) {
	if ($("#li_" + newurl).length > 0) {
		$("#li_" + newurl).remove();
		$("#fra_" + newurl).remove();
	}
	var leng = $("li[id^='li_']").length;
	// 只有一个关闭时打开默认的
	if (leng == 0) {
		// $("#fra_content").attr("src","");
		$("#showmenu").hide();
	} else {
		// 打开最后一个
		$("#div_tab").find("li:last").click();
	}
}

// 刷新iframe
function refresh(id) {
	$("#fra_" + id).attr('src', $("#fra_" + id).attr('src'));
}
// 关闭其他页面
function removeother(id) {
	$("#div_tab").find("li[id!='li_" + id + "']").remove();
	$("#appcontent").find("iframe[id!='fra_" + id + "']").remove();
}
// 关闭所有页面
function removeall() {
	$("li[id^='li_']").remove();
	$("iframe[id^='fra_']").remove();
	$("#showmenu").hide();
}
function bitclick(){
	$("#newAinfoli").css("display","none");
	$("#newAinfoli").removeClass("blink");
//	$.cmTaskList('my', 'nothing', true);
	if($(window.top.document).find("#menu").css("display")=="none"){
		$.cmTaskList('my', 'nothing', true);
	}else{
		$("[data-bind='business/mytask/queryTask']").click();
		if($("#li_businessmytaskqueryTask".length>0)){
			refresh("businessmytaskqueryTask");
		}
		//$("#fra_businessmytaskqueryTask").attr('src','business/mytask/queryTask');
	}
}
