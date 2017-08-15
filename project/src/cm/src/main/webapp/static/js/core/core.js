/**
 * insCore frameworks
 */

;
(function($) { 
	var defaultoptions = {
		target : (window.top.location != window.self.location ? window.top
				: window)
	};
	// insLoading
	var insLoading_opts = {
		message : "<div class=\"loader cus-loader\"><div class=\"loader-inner ball-clip-rotate-multiple\"><div></div><div></div></div></div>",
		css : {
			width : "60px",
			height : "60px",
			backgroundColor : "#444",
			border : "3px solid #222",
			"border-radius" : "30px",
			"-moz-opacity" : "0.7",
			"-khtml-opacity" : "0.7",
			opacity : "0.7",
			top : "35%",
			left : "48%",
			cursor : "pointer"
		}
	};
	$.insLoading = function(options) {
		var options = $.extend(defaultoptions, options);
		if (typeof $.blockUI === 'function') {
			// $.blockUI();
			$(options.target.document.body).block(insLoading_opts);
			options.target.insLoaded = function() {
				$(options.target.document.body).unblock();
			}
			$(options.target.document.body).find(".loader.cus-loader").parent().on("click", function() {
				$(options.target.document.body).unblock();
			});
		}
	};
	$.insLoaded = function(options) {
		var options = $.extend(defaultoptions, options);
		if (typeof options.target.insLoaded === 'function') {
			options.target.insLoaded();
			$(options.target.document.body).unblock();
		}

	};
	
	//dialog modal
	$.xDialog = function(option){
		if ($(defaultoptions.target.document).find("#xDialog").size() > 0) {
			$(defaultoptions.target.document).find("#xDialog").remove();
			//remove default css 
			$(defaultoptions.target.document.body).removeClass("modal-open");
		}
		var options = $.extend({
			toggle : "modal",
			width : "720px",
			backdrop : false,
			keyboard : true
		}, option);
		if (options.remote) {
			options.remote = addRandomArg(options.remote);
		}
		var dialog = null;
		if(options.width && options.width!=''){
			dialog = $('<div class="modal fade" id="xDialog" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"><div class="modal-dialog" style="width: '+options.width+'"><div class="modal-content" id="modal-content"></div></div></div>');
		}else{
			dialog = $('<div class="modal fade" id="xDialog" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"><div class="modal-dialog"><div class="modal-content" id="modal-content"></div></div></div>');
		}
		dialog.appendTo($(defaultoptions.target.document.body)).modal(options);
		dialog.find(".modal-dialog").draggable({handle: "div.modal-header"});
	};
	
	//imgView
	$.imgView = function(option){
		if ($(defaultoptions.target.document).find("#imgView").size() > 0) {
			$(defaultoptions.target.document).find("#imgView").remove();
		}
		var options = $.extend({
			width : "600px"
		}, option);
		var dialog = null;
		if(options.width && options.width!=''){
			dialog = $('<div id="imgView" class="ui-widget-content"><div class="modal-dialog" style="width: '+options.width+'"><div class="modal-content" id="modal-content"></div></div></div>');
		}else{
			dialog = $('<div id="imgView" class="ui-widget-content"><div class="modal-dialog"><div class="modal-content" id="modal-content"></div></div></div>');
		}
		if (options.remote) {
			options.remote = addRandomArg(options.remote);
		}
		dialog.find(".modal-content").load(options.remote, function(){
			dialog.hide();
			dialog.appendTo($(defaultoptions.target.document.body));
			dialog.find("button.close").click(function(){
				dialog.hide();
			});
			dialog.find(".modal-dialog").draggable({handle: "div.modal-header"});
			dialog.show();
		});
	};
	
	$.cmTaskList = function(taskType, showType, freshFlag){
		//freshFlag: true|false
		//taskType: my|share|management
		//showType: list|list4deal
		if (showType == "list4deal") {
			$(defaultoptions.target.document).find("#desktop_content").show();
			$(defaultoptions.target.document).find("#desktop_tasks").css("width","20%");
			$("#taskViewList").attr("href","javascript:$.cmTaskList('my', 'list', false);");
			if($("#taskViewList span").hasClass("glyphicon-step-backward")){
				$("#taskViewList span").removeClass("glyphicon-step-backward").addClass("glyphicon-fullscreen");
			}
		} else if(showType == "nothing"){
			
		}else {
			$(defaultoptions.target.document).find("#desktop_content").hide();
			$(defaultoptions.target.document).find("#desktop_tasks").css("width","100%");
			$("#taskViewList").attr("href","javascript:$.cmTaskList('my', 'list4deal', false);");
			$("#taskViewList span").removeClass("glyphicon-fullscreen").addClass("glyphicon-step-backward");
		}
		if (freshFlag == false || freshFlag === "false") {
			return;
		} else if (freshFlag != 'noMask') {
			$.insLoading(); 
		}
		if (taskType == "my") {
			$(defaultoptions.target.document).find("#desktop_tasks").attr("src","business/mytask/queryTask");
		} else if (taskType == "share") {
			$(defaultoptions.target.document).find("#desktop_tasks").attr("src","business/ordermanage/ordermanagelist");
		} else if (taskType == "management") {
			$(defaultoptions.target.document).find("#desktop_tasks").attr("src","business/cartaskmanage/cartaskmanagelist");
		} else {
			
		}
	};
	
	function addRandomArg(url){
		if (!url) {
			return "";
		}
		if (url.indexOf("?") > 0) {
			return url + "&rVer=" + new Date().getTime();
		} else {
			return url + "?rVer=" + new Date().getTime();
		}
	}
})(jQuery);